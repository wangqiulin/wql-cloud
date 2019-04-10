package com.wql.cloud.config.propertyhandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

/**
 * https://www.cnblogs.com/huanzi-qch/p/10149547.html
 * 
 * 在使用 spring cloud config 时，如果在 properties 文件里面有中文的话，会出现乱码。 乱码的原因是：spring
 * 默认使用org.springframework.boot.env.PropertiesPropertySourceLoader 来加载配置，底层是通过调用
 * Properties 的 load 方法，而load方法输入流的编码是 ISO 8859-1
 * 
 * @author wangqiulin
 *
 */
public class MyPropertiesHandler implements PropertySourceLoader {

	private static final Logger logger = LoggerFactory.getLogger(MyPropertiesHandler.class);

	@Override
	public String[] getFileExtensions() {
		return new String[] { "properties", "xml" };
	}

	@Override
	public PropertySource<?> load(String name, Resource resource, String profile) throws IOException {
		if (profile == null) {
			Properties properties = getProperties(resource);
			if (!properties.isEmpty()) {
				PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource(name, properties);
				return propertiesPropertySource;
			}
		}
		return null;
	}

	private Properties getProperties(Resource resource) {
		Properties properties = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = resource.getInputStream();
			properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			inputStream.close();
		} catch (IOException e) {
			logger.error("load inputstream failure...", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("close IO failure ....", e);
				}
			}
		}
		return properties;
	}

}

