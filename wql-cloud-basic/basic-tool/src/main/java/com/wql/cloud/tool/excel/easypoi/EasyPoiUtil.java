package com.wql.cloud.tool.excel.easypoi;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;

public class EasyPoiUtil {
	
	//==========================导出=================================//

	/**
	 * 带表头的导出到excel
	 * 
	 * @param title
	 * @param sheetName
	 * @param fileName
	 * @param pojoClass
	 * @param data
	 * @param response
	 * @throws Exception
	 */
	public static void exportExcel(String title, String sheetName, String fileName, Class<?> pojoClass, List<?> data,
			HttpServletResponse response) throws Exception {
		exportExcel(title, sheetName, fileName, true, pojoClass, data, response);
	}
	
	/**
	 * easypoi导出excel
	 * 
	 * 在导出的实体类上添加注解：
	 * 
	 * @Excel(name = "密码", orderNum = "0", width = 15.0)
	 * @Excel(name = "创建时间", orderNum = "1", width = 30.0, exportFormat =
	 *             "yyyy-MM-dd HH:mm:ss")
	 * 
	 * 
	 * @param title                      : 表的标题对应title
	 * @param sheetName：工作表名称对应sheetName
	 * @param isCreateHeader             为false时表格的标题和表头都没了
	 * @param pojoClass：                 User.class
	 * @param data                       ： list数据
	 * @param response
	 * @param fileName                   ： 文件名称.xls
	 * @throws Exception
	 */
	public static void exportExcel(String title, String sheetName, String fileName, boolean isCreateHeader,
			Class<?> pojoClass, List<?> data, HttpServletResponse response) throws Exception {
		// 创建出Workbook
		ExportParams exportParams = new ExportParams(title, sheetName);
		exportParams.setCreateHeadRows(isCreateHeader);
		Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, data);
		if (workbook == null) {
			throw new Exception();
		}
		downloadExcel(response, fileName, workbook);
	}
	
	
	private static void downloadExcel(HttpServletResponse response, String fileName, Workbook workbook) throws Exception {
        // 设置响应实体的编码格式
        response.setCharacterEncoding("UTF-8");
        // 通知浏览器使用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }
	
	
	
	//==========================导入=================================//
	
	public static <T> List<T> importExcel(String filePath, Class<T> pojoClass) {
		return importExcel(filePath, 1, 1, pojoClass);
	}
	
	public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass) {
		return importExcel(file, 1, 1, pojoClass);
	}
	
	/**
	 * 从excel导入数据出来
	 * 
	 * @param <T>
	 * @param filePath
	 * @param titleRows ： 标题占用行数
	 * @param headerRows ： 表头占用行数
	 * @param pojoClass
	 * @return
	 */
	public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
		if (StringUtils.isBlank(filePath)) {
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		List<T> list = null;
		try {
			list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	

	public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
		if (file == null) {
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		List<T> list = null;
		try {
			list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
