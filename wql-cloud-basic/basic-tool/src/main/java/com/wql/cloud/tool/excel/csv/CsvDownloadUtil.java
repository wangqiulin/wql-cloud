package com.wql.cloud.tool.excel.csv;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuxueli.poi.excel.annotation.ExcelField;

/**
 * csv文件下载
 */
public class CsvDownloadUtil {

    private static final Logger logger = LoggerFactory.getLogger(CsvDownloadUtil.class);

    private static String EMPTY_STR = "";

    /**
     * 设置表头信息
     */
    public static Map<String, String> getCsvHeader(Class<?> clazz) throws Exception {
        Map<String, String> header = new LinkedHashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        Class<ExcelField> apiModelPropertyClass = ExcelField.class;
        for (Field field : fields) {
            if (field.isAnnotationPresent(apiModelPropertyClass)) {
                String value = field.getAnnotation(apiModelPropertyClass).name();
                header.put(field.getName(), value);
            }
        }
        return header;
    }

    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void writeHeader(Map<String, String> csvHeader, String fileName, HttpServletResponse response) throws IOException {
        logger.info("开始生成csv文件->{}", fileName);
        response.reset();
        //fileName = overrideFileName(fileName + ".csv");
        fileName = fileName + ".csv";
        fileName = URLEncoder.encode(fileName, "UTF-8");
        //跨域请求头
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Method", "*");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setCharacterEncoding("utf-8");
        response.setHeader("content-disposition", "attachment;filename=" + fileName);
        response.setContentType("application/octet-stream;file-name=" + fileName);
        //解决中文导出乱码问题
        ServletOutputStream out = response.getOutputStream();
		out.write(new byte []{( byte ) 0xEF ,( byte ) 0xBB ,( byte ) 0xBF });//加上bom头，才不会中文乱码 
        //写入第一行header
        List header = Arrays.asList(csvHeader);
        writeData(csvHeader, header, response);
    }
    
    
	public static void writeData(Map<String, String> csvHeader, List<Map<String, Object>> data, HttpServletResponse response, BiFunction<String,Object,String> converter) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> dmap : data) {
            for (String key : csvHeader.keySet()) {
                Object val = dmap.get(key);
                if (converter != null) {
                    sb.append(converter.apply(key,val)).append(",");
                } else {
                    sb.append(cover2cscValue(val)).append(",");
                }
            }
            sb.append("\n");
        }
        ServletOutputStream out = response.getOutputStream();
        out.write(sb.toString().getBytes("utf-8"));
        out.flush();
    }

	
	
    /**
     * 写数据
     */
    public static void writeData(Map<String, String> csvHeader, List<Map<String, Object>> data, HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> dmap : data) {
            for (String key : csvHeader.keySet()) {
                Object val = dmap.get(key);
                sb.append(cover2cscValue(val)).append(",");
            }
            sb.append("\n");
        }
        ServletOutputStream out = response.getOutputStream();
        out.write(sb.toString().getBytes("utf-8"));
        out.flush();
    } 

    /**
     * 重写fileName
     */
    @SuppressWarnings("unused")
	private static String overrideFileName(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return fileName;
        }
        String dateSuffix = "-" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        if (fileName.contains(".")) {
            int index = fileName.lastIndexOf(".");
            return fileName.substring(0, index) + dateSuffix + fileName.substring(index, fileName.length());
        } else {
            return fileName + dateSuffix;
        }
    }

    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * csv特殊字符转换
     * 如果字段中有逗号（,），该字段使用双引号把字段括起来（"）括起来 ,如果该字段中有双引号，该双引号前要再加一个双引号，然后把该字段使用双引号括起来。
     * ="" , 可以把大数字包起来,防止转换为科学记数法
     * @param javaValue
     * @return
     */
    private static String cover2cscValue(Object javaValue) {
        if (javaValue == null) {
            return EMPTY_STR;
        }
        String strVal;
        if(javaValue instanceof String){
            strVal = String.valueOf(javaValue);
            
        } else if(javaValue instanceof Integer || javaValue instanceof Long || 
        		javaValue instanceof Double || javaValue instanceof Float){
           strVal = String.valueOf(javaValue);
           
        } else if(javaValue instanceof BigDecimal){
            strVal = ((BigDecimal)javaValue).toPlainString();
            
        } else if (javaValue instanceof Date) {
			String datePattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
			strVal = dateFormat.format(javaValue);
			
        } else {
            //未知类型直接toString,可能存在bug
            strVal = String.valueOf(javaValue);
            logger.warn("csv转换未知类型字段{}->{}",javaValue.getClass(),strVal);
        }
        
        String returnVal = strVal;
        if(isNumeric(returnVal)){
        	//是数字直接加=""返回
            return returnVal;
        } else {
            //加空格避免日期格式化
            returnVal = " "+returnVal;
        }
        if(strVal.contains(",")){
            returnVal = "\""+returnVal+"\"";
        }
        if(strVal.contains("\"")){
            returnVal = returnVal.replaceAll("\"","\"\"");
        }
        return returnVal;
    }
    
    
    //JavaBean转换为Map
    public static Map<String,Object> bean2map(Object bean, Class<?> clazz) throws Exception{
  		Map<String,Object> map = new HashMap<>();
  		//获取指定类（Person）的BeanInfo 对象
  		BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
  		//获取所有的属性描述器
  		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
  		for(PropertyDescriptor pd:pds){
  			String key = pd.getName();
  			Method getter = pd.getReadMethod();
  			Object value = getter.invoke(bean);
  			map.put(key, value);
  		}
  		return map;
  	}
    
    
    /*public <T> boolean getWorkbook(Class<T> clazz, Object obj) throws Exception {
    	HttpServletResponse response = null;
    	
    	//1.获取excel的头信息
    	Map<String, String> csvHeader = null; //CsvDownloadUtil.getCSVHeader(clazz);
    	
    	//2.写入头信息
    	CsvDownloadUtil.writeHeader(csvHeader, "名称前缀", response);
    	
    	//3.原始list数据，重新组装
    	List<Map<String, Object>> list = new ArrayList<>();
    	list.add(CsvDownloadUtil.bean2map(obj, clazz));
    	
    	//4.写入数据
    	CsvDownloadUtil.writeData(csvHeader, list, response, new BiFunction<String, Object, String>() {
            @Override
            public String apply(String cloumnName, Object value) {
                if(cloumnName.equals("字段1")){
                	//科学计数法数据展示修改
                    return String.format("=\"%s\"", value);
                }
                if(cloumnName.equals("字段1")){
                    return String.format("=\"%s\"", value);
                }
                return value.toString();
            }
        });
    	return true;
    }*/
    
    
}
