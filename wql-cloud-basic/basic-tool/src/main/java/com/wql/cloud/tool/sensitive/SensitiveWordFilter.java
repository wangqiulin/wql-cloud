package com.wql.cloud.tool.sensitive;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 创建一个FilterSet，枚举了0~65535的所有char是否是某个敏感词开头的状态
 * <p>
 * 判断是否是 敏感词开头 | | 是 不是 获取头节点 OK--下一个字 然后逐级遍历，DFA算法
 */
@SuppressWarnings("rawtypes")
public class SensitiveWordFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveWordFilter.class);

    private static final String dir = "sensitive";

    private static final String wd = "wd.txt";

    private static final String wd_dir = dir + "/" + wd;

    private static final String stopwd = "stopwd.txt";

    private static final String stopwd_dir = dir + "/" + stopwd;

    private static SensitiveWordFilter instance;

    private static SensitiveWordFilter getInstance() {
        if (instance == null) {
            synchronized (LOGGER) {
                if (instance == null) {
                    instance = new SensitiveWordFilter();
                }
            }
        }
        return instance;
    }

    static {
        new Thread(() -> {
            File dirs = new File(dir);
            if (!dirs.exists()) {
                dirs.mkdirs();
                LOGGER.info("创建敏感词外部目录->{}", dirs.getAbsolutePath());
            }
            try {
                Path path = Paths.get(dirs.getAbsolutePath());
                WatchService watcher = path.getFileSystem().newWatchService();
                path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

                WatchKey watchKey = watcher.take();
                while (true) {
                    List<WatchEvent<?>> events = watchKey.pollEvents();
                    boolean isUpdate = false;
                    for (WatchEvent event : events) {
                        String eventName = event.context().toString();
                        if (eventName.contains(wd) || eventName.contains(stopwd)) {
                            isUpdate = true;
                            break;
                        }
                    }
                    if (isUpdate) {
                        instance = new SensitiveWordFilter();
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                LOGGER.error("敏感词文件监听异常", e);
            }
        }).start();
    }

    /**
     * 存储首字
     */
    private final FilterSet set = new FilterSet();
    /**
     * 存储节点
     */
    private final Map<Integer, WordNode> nodes = new HashMap<>(1024, 1);
    /**
     * 停顿词
     */
    private final Set<Integer> stopwdSet = new HashSet<>();
    /**
     * 敏感词过滤替换
     */
    private final char SIGN = '*';

    private SensitiveWordFilter() {
        init();
    }

    private void init() {
        long a = System.nanoTime();
        // 获取敏感词
        addSensitiveWord(readWordFromFile(wd_dir));
        addStopWord(readWordFromFile(stopwd_dir));
        a = System.nanoTime() - a;
        LOGGER.info("加载时间 : " + a + "ns");
        LOGGER.info("加载时间 : " + a / 1000000 + "ms");
    }

    /**
     * 增加敏感词
     *
     * @param path
     * @return
     */
    private static List<String> readWordFromFile(String path) {
        List<String> words = new ArrayList<>();
        File file = new File(path);
        InputStream is = null;
        try {
            if (file.exists()) {
                is = new FileInputStream(file);
                LOGGER.info("加载war外敏感词->{}", file.getAbsolutePath());
            } else {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                LOGGER.info("加载war内敏感词->{}", path);
            }
            try (InputStreamReader isr = new InputStreamReader(is, "utf-8")) {
                try (BufferedReader br = new BufferedReader(isr)) {
                    for (String buf = ""; (buf = br.readLine()) != null; ) {
                        if (buf == null || buf.trim().equals("")) {
                            continue;
                        }
                        words.add(buf);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("读取文件失败:", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("is流关闭失败:", e);
                }
            }
        }
        return words;
    }

    /**
     * 增加停顿词
     *
     * @param words
     */
    private void addStopWord(final List<String> words) {
        if (words != null && words.size() > 0) {
            char[] chs;
            for (String curr : words) {
                chs = curr.toCharArray();
                for (char c : chs) {
                    stopwdSet.add(charConvert(c));
                }
            }
        }
    }

    /**
     * 添加DFA节点
     *
     * @param words
     */
    private void addSensitiveWord(final List<String> words) {
        if (words != null && words.size() > 0) {
            char[] chs;
            int fchar;
            int lastIndex;
            // 首字母节点
            WordNode fnode;
            for (String curr : words) {
                chs = curr.toCharArray();
                fchar = charConvert(chs[0]);
                // 没有首字定义
                if (!set.contains(fchar)) {
                    // 首字标志位 可重复add,反正判断了，不重复了
                    set.add(fchar);
                    fnode = new WordNode(fchar, chs.length == 1);
                    nodes.put(fchar, fnode);
                } else {
                    fnode = nodes.get(fchar);
                    if (!fnode.isLast() && chs.length == 1) {
                        fnode.setLast(true);
                    }
                }
                lastIndex = chs.length - 1;
                for (int i = 1; i < chs.length; i++) {
                    fnode = fnode.addIfNoExist(charConvert(chs[i]), i == lastIndex);
                }
            }
        }
    }

    /**
     * 过滤判断 将敏感词转化为成屏蔽词
     *
     * @param src
     * @return
     */
    public static final String doFilter(final String src) {
        SensitiveWordFilter instance = getInstance();
        char[] chs = src.toCharArray();
        int length = chs.length;
        int currc;
        int k;
        WordNode node;
        for (int i = 0; i < length; i++) {
            currc = charConvert(chs[i]);
            if (!instance.set.contains(currc)) {
                continue;
            }
            node = instance.nodes.get(currc);// 日 2
            if (node == null) {
                continue;
            }
            boolean couldMark = false;
            int markNum = -1;
            if (node.isLast()) {// 单字匹配（日）
                couldMark = true;
                markNum = 0;
            }
            // 继续匹配（日你/日你妹），以长的优先
            // 你-3 妹-4 夫-5
            k = i;
            for (; ++k < length; ) {
                int temp = charConvert(chs[k]);
                if (instance.stopwdSet.contains(temp)) {
                    continue;
                }
                node = node.querySub(temp);
                // 没有了
                if (node == null) {
                    break;
                }
                if (node.isLast()) {
                    couldMark = true;
                    markNum = k - i;// 3-2
                }
            }
            if (couldMark) {
                for (k = 0; k <= markNum; k++) {
                    chs[k + i] = instance.SIGN;
                }
                i = i + markNum;
            }
        }
        return new String(chs);
    }


    /**
     * 过滤判断 将敏感词转化为成屏蔽词
     *
     * @param src
     * @return
     */
    public static final String searchSensitiveWord(final String src) {
        SensitiveWordFilter instance = getInstance();
        char[] chs = src.toCharArray();
        int length = chs.length;
        int currc;
        int k;
        WordNode node;
        for (int i = 0; i < length; i++) {
            currc = charConvert(chs[i]);
            if (!instance.set.contains(currc)) {
                continue;
            }
            node = instance.nodes.get(currc);// 日 2
            if (node == null) {
                continue;
            }
            boolean couldMark = false;
            int markNum = -1;
            if (node.isLast()) {// 单字匹配（日）
                couldMark = true;
                markNum = 0;
            }
            // 继续匹配（日你/日你妹），以长的优先
            // 你-3 妹-4 夫-5
            k = i;
            for (; ++k < length; ) {
                int temp = charConvert(chs[k]);
                if (instance.stopwdSet.contains(temp)) {
                    continue;
                }
                node = node.querySub(temp);
                // 没有了
                if (node == null) {
                    break;
                }
                if (node.isLast()) {
                    couldMark = true;
                    markNum = k - i;// 3-2
                }
            }
            if (couldMark) {
                char[] word = new char[markNum + 1];
                for (k = 0; k <= markNum; k++) {
                    word[k] = chs[k + i];
                }
                return new String(word);
            }
        }

        return null;
    }

    /**
     * 是否包含敏感词
     *
     * @param src
     * @return
     */
    public static final boolean isContains(final String src) {
        SensitiveWordFilter instance = getInstance();
        char[] chs = src.toCharArray();
        int length = chs.length;
        int currc;
        int k;
        WordNode node;
        for (int i = 0; i < length; i++) {
            currc = charConvert(chs[i]);
            if (!instance.set.contains(currc)) {
                continue;
            }
            node = instance.nodes.get(currc);// 日 2
            if (node == null) {
                continue;
            }
            boolean couldMark = false;
            if (node.isLast()) {// 单字匹配（日）
                couldMark = true;
            }
            // 继续匹配（日你/日你妹），以长的优先
            // 你-3 妹-4 夫-5
            k = i;
            for (; ++k < length; ) {
                int temp = charConvert(chs[k]);
                if (instance.stopwdSet.contains(temp)) {
                    continue;
                }
                node = node.querySub(temp);
                if (node == null) {
                    break;
                }
                if (node.isLast()) {
                    couldMark = true;
                }
            }
            if (couldMark) {
                return true;
            }
        }

        return false;
    }

    /**
     * 大写转化为小写 全角转化为半角
     *
     * @param src
     * @return
     */
    private static int charConvert(char src) {
        int r = BCConvertUtil.qj2bj(src);
        return (r >= 'A' && r <= 'Z') ? r + 32 : r;
    }


}
