package com.wql.generator.utils;

import org.yaml.snakeyaml.Yaml;

import com.wql.generator.entity.Configuration;

import java.io.InputStream;
import java.net.URL;

public class ConfigUtil {
    private static Configuration configuration;

    static {
        URL url = ConfigUtil.class.getClassLoader().getResource("generator.yaml");
        if (url.getPath().contains("jar")) { // 用户未提供配置文件
            System.err.println("Can not find file named 'generator.yaml' at resources path, please make sure that you have defined that file.");
            System.exit(0);
        } else {
            InputStream inputStream = ConfigUtil.class.getClassLoader().getResourceAsStream("generator.yaml");
            Yaml yaml = new Yaml();
            configuration = yaml.loadAs(inputStream, Configuration.class);
        }
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

}
