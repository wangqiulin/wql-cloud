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

public class ServiceTask extends AbstractTask {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceTask(String className) {
        super(className);
    }

    @Override
    public void run() throws IOException, TemplateException {
        // 生成Service填充数据
        Map<String, String> serviceData = new HashMap<>();
        serviceData.put("BasePackageName", ConfigUtil.getConfiguration().getPackageName());
        serviceData.put("ServicePackageName", ConfigUtil.getConfiguration().getPath().getService());
        serviceData.put("EntityPackageName", ConfigUtil.getConfiguration().getPath().getEntity());
        serviceData.put("DaoPackageName", ConfigUtil.getConfiguration().getPath().getDao());
        serviceData.put("Author", ConfigUtil.getConfiguration().getAuthor());
        serviceData.put("Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        serviceData.put("ClassName", className);
        serviceData.put("EntityName", StringUtil.firstToLowerCase(className));
        
        //String sourcePath = FileUtil.getSourcePath();
        String sourcePath = ConfigUtil.getConfiguration().getFilePathPrefix();
        String filePath = sourcePath + StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName()) + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getService());
        String fileName;
        if (StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getInterf())) { // 表示不生成Service接口文件
            serviceData.put("Impl", "");
            serviceData.put("Override", "");
            serviceData.put("InterfaceImport", "");
            fileName = className + "Service.java";
            System.out.println("Generating " + className + "Service.java");
        } else {
            serviceData.put("Impl", "implements " + className + "Service");
            serviceData.put("Override", "\n    @Override");
            serviceData.put("InterfaceImport", "import " + ConfigUtil.getConfiguration().getPackageName() + ConfigUtil.getConfiguration().getPath().getInterf() + "." + className + "Service;");
            fileName = className + "ServiceImpl.java";
            System.out.println("Generating " + className + "ServiceImpl.java");
        }
        // 生成Service文件
        FileUtil.generateToJava(FreemarketConfigUtils.TYPE_SERVICE, serviceData, filePath + fileName);
    }
}
