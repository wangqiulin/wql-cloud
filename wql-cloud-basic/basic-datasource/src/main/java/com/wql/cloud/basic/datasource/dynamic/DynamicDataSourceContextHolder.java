package com.wql.cloud.basic.datasource.dynamic;

import java.util.ArrayList;
import java.util.List;

public class DynamicDataSourceContextHolder {
	
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>();
    
    public static List<String> dataSourceIds = new ArrayList<String>();
    
    public static void setDataSourceType(String dataSourceType) {
    	CONTEXT_HOLDER.set(dataSourceType);
    }
    
    public static String getDataSourceType() {
        return CONTEXT_HOLDER.get();
    }
    
    public static void clearDataSourceType() {
    	CONTEXT_HOLDER.remove();
    }
    
    /**
     * 判断指定DataSrouce当前是否存在
     *
     * @param dataSourceId
     * @return
     */
    public static boolean containsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }
    
}
