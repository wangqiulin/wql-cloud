package com.wql.generator.task;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.wql.generator.task.base.AbstractTask;
import com.wql.generator.utils.ConfigUtil;
import com.wql.generator.utils.FileUtil;
import com.wql.generator.utils.FreemarketConfigUtils;
import com.wql.generator.utils.StringUtil;

public class DaoTask extends AbstractTask {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DaoTask(String className) {
        super(className);
    }

    @Override
    public void run() throws IOException, TemplateException {
        // 生成Dao填充数据
        System.out.println("Generating " + className + "Mapper.java");
        Map<String, String> daoData = new HashMap<>();
        daoData.put("BasePackageName", ConfigUtil.getConfiguration().getPackageName());
        daoData.put("DaoPackageName", ConfigUtil.getConfiguration().getPath().getDao());
        daoData.put("EntityPackageName", ConfigUtil.getConfiguration().getPath().getEntity());
        daoData.put("Author", ConfigUtil.getConfiguration().getAuthor());
        daoData.put("Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        daoData.put("ClassName", className);
        daoData.put("EntityName", StringUtil.firstToLowerCase(className));
        
        //String sourcePath = FileUtil.getSourcePath();
        String sourcePath = ConfigUtil.getConfiguration().getFilePathPrefix();
        String filePath = sourcePath + StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName()) + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getDao());
        String fileName = className + "Mapper.java";
        // 生成dao文件
        FileUtil.generateToJava(FreemarketConfigUtils.TYPE_DAO, daoData, filePath + fileName);
    }
}
