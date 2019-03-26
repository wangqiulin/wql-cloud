package com.wql.cloud.tool.excel.xxl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuxueli.poi.excel.ExcelExportUtil;
import com.xuxueli.poi.excel.ExcelImportUtil;

/**
 * excel导入导出工具类
 * 
 * @author wangqiulin
 *
 */
public class ExcelUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	//=============================导出======================================//
	
	/**
	 * 导出到文件
	 * 
	 * @param filePath
	 * @param dataList
	 */
	public static void exportToFile(String filePath, List<?> dataList) {
		File file = new File(filePath);
		/*如果路径不存在则创建*/
		if (!file.exists()) {
			try {
				/*getParentFile返回此抽象路径名的父路径名的抽象路径名，如果直接使用f.mkdirs则会将111.txt也创建成目录*/
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				logger.error("创建文件失败", e);
				throw new RuntimeException(e.getMessage());
			}
		}
		ExcelExportUtil.exportToFile(filePath, dataList);
	}

	/**
	 * 导出字节数据
	 * 
	 * @param dataList
	 */
	public static byte[] exportToBytes(List<?> dataList) {
		return ExcelExportUtil.exportToBytes(dataList);
	}
	
	/**
	 * 导出excel对象
	 * 
	 * @param dataList
	 */
	public static Workbook exportWorkbook(List<?> dataList) {
		return ExcelExportUtil.exportWorkbook(dataList);
	}
	
	
	//=============================导入======================================//
	
	public static List<?> importExcel(String excelFile, Class<?> clazz){
		return ExcelImportUtil.importExcel(excelFile, clazz);
	}
	
	public static List<?> importExcel(InputStream inputStream, Class<?> clazz){
		return ExcelImportUtil.importExcel(inputStream, clazz);
	}
	
	public static List<?> importExcel(File excelFile, Class<?> clazz){
		return ExcelImportUtil.importExcel(excelFile, clazz);
	}
	
	public static List<?> importExcel(Workbook workbook, Class<?> clazz){
		return ExcelImportUtil.importExcel(workbook, clazz);
	}
	
	public static List<?> importSheet(Workbook workbook, Class<?> clazz){
		return ExcelImportUtil.importSheet(workbook, clazz);
	}
	
}
