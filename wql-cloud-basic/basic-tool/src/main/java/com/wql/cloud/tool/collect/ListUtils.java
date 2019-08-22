package com.wql.cloud.tool.collect;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 在数据库中查出来的列表中，往往需要对不同的字段重新排序。 一般的做法都是使用排序的字段，重新到数据库中查询。 如果不到数据库查询，直接在第一次查出来的list中排序，无疑会提高系统的性能。 下面就写一个通用的方法，对list排序，
 * 
 * 至少需要满足以下5点：
 * 
 * ①.list元素对象类型任意 ---->使用泛型解决
 * 
 * ②.可以按照list元素对象的任意多个属性进行排序,即可以同时指定多个属性进行排序 --->使用java的可变参数解决
 * 
 * ③.list元素对象属性的类型可以是数字(byte、short、int、long、float、double等，包括正数、负数、0)、字符串(char、String)、日期(java.util.Date)
 * --->对于数字：统一转换为固定长度的字符串解决,比如数字3和123，转换为"003"和"123" ;再比如"-15"和"7"转换为"-015"和"007" --->对于日期：可以先把日期转化为long类型的数字，数字的解决方法如上
 * 
 * ④.list元素对象的属性可以没有相应的getter和setter方法 --->可以使用java反射进行获取private和protected修饰的属性值
 * 
 * ⑤.list元素对象的对象的每个属性都可以指定是升序还是降序 -->使用2个重写的方法(一个方法满足所有属性都按照升序(降序)，另外一个方法满足每个属性都能指定是升序(降序))
 * 
 * 
 * 使用方法:
 * 
 * 1.按userId升序、username降序、birthDate升序排序 String [] sortNameArr = {"userId","username","birthDate"}; boolean [] isAscArr =
 * {true,false,true}; ListUtils.sort(list,sortNameArr,isAscArr);
 *
 * 2.按userId、username、birthDate都升序排序 ListUtils.sort(list, true, "userId", "username","birthDate");
 * 
 * 3.按userId、username都倒序排序 ListUtils.sort(list, false, "userId", "username");
 *
 *
 * @author wangqiulin
 * @date 2018年4月8日
 */
public class ListUtils {

    /**
     * 对list的元素按照多个属性名称排序, list元素的属性可以是数字（byte、short、int、long、float、double等，支持正数、负数、0）、char、String、java.util.Date
     * 
     * @param lsit
     * @param sortname: list元素的属性名称
     * @param isAsc: true升序，false降序
     */
    public static <E> void sort(List<E> list, final boolean isAsc, final String... sortnameArr) {
        Collections.sort(list, new Comparator<E>() {
        	@Override
            public int compare(E a, E b) {
                int ret = 0;
                try {
                    for (int i = 0; i < sortnameArr.length; i++) {
                        ret = ListUtils.compareObject(sortnameArr[i], isAsc, a, b);
                        if (0 != ret) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ret;
            }
        });
    }

    /**
     * 给list的每个属性都指定是升序还是降序
     * 
     * @param list
     * @param sortnameArr: 参数数组
     * @param typeArr: 每个属性对应的升降序数组， true升序，false降序
     */
    public static <E> void sort(List<E> list, final String[] sortnameArr, final boolean[] typeArr) {
        if (sortnameArr.length != typeArr.length) {
            throw new RuntimeException("属性数组元素个数和升降序数组元素个数不相等");
        }
        Collections.sort(list, new Comparator<E>() {
        	@Override
            public int compare(E a, E b) {
                int ret = 0;
                try {
                    for (int i = 0; i < sortnameArr.length; i++) {
                        ret = ListUtils.compareObject(sortnameArr[i], typeArr[i], a, b);
                        if (0 != ret) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ret;
            }
        });
    }

    /**
     * 对2个对象按照指定属性名称进行排序
     * 
     * @param sortname: 属性名称
     * @param isAsc: true升序，false降序
     * @param a
     * @param b
     * @return
     * @throws Exception
     */
    private static <E> int compareObject(final String sortname, final boolean isAsc, E a, E b) throws Exception {
        int ret;
        Object value1 = ListUtils.forceGetFieldValue(a, sortname);
        Object value2 = ListUtils.forceGetFieldValue(b, sortname);
        String str1 = value1.toString();
        String str2 = value2.toString();
        if (value1 instanceof Number && value2 instanceof Number) {
            int maxlen = Math.max(str1.length(), str2.length());
            str1 = ListUtils.addZero2Str((Number)value1, maxlen);
            str2 = ListUtils.addZero2Str((Number)value2, maxlen);
        } else if (value1 instanceof Date && value2 instanceof Date) {
            long time1 = ((Date)value1).getTime();
            long time2 = ((Date)value2).getTime();
            int maxlen = Long.toString(Math.max(time1, time2)).length();
            str1 = ListUtils.addZero2Str(time1, maxlen);
            str2 = ListUtils.addZero2Str(time2, maxlen);
        }
        if (isAsc) {
            ret = str1.compareTo(str2);
        } else {
            ret = str2.compareTo(str1);
        }
        return ret;
    }

    /**
     * 给数字对象按照指定长度在左侧补0.
     * 
     * 使用案例: addZero2Str(11,4) 返回 "0011", addZero2Str(-18,6)返回 "-000018"
     * 
     * @param numObj: 数字对象
     * @param length: 指定的长度
     * @return
     */
    public static String addZero2Str(Number numObj, int length) {
        NumberFormat nf = NumberFormat.getInstance();
        // 设置是否使用分组
        nf.setGroupingUsed(false);
        // 设置最大整数位数
        nf.setMaximumIntegerDigits(length);
        // 设置最小整数位数
        nf.setMinimumIntegerDigits(length);
        return nf.format(numObj);
    }

    /**
     * 给数字对象按照指定长度在左侧补0.
     * 
     * 使用案例: addZero2Str(11,4) 返回 "0011", addZero2Str(-18,6)返回 "-000018"
     * 
     * @param numObj: 数字对象
     * @param length: 指定的长度
     * @return
     */
    public static String addZero2Str2(String numObj, int length) {
        NumberFormat nf = NumberFormat.getInstance();
        // 设置是否使用分组
        nf.setGroupingUsed(false);
        // 设置最大整数位数
        nf.setMaximumIntegerDigits(length);
        // 设置最小整数位数
        nf.setMinimumIntegerDigits(length);
        return nf.format(Double.parseDouble(numObj) * 100);
    }

    /**
     * 获取指定对象的指定属性值（去除private,protected的限制）
     * 
     * @param obj: 属性名称所在的对象
     * @param fieldName: 属性名称
     * @return
     * @throws Exception
     */
    public static Object forceGetFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        Object object = null;
        boolean accessible = field.isAccessible();
        if (!accessible) {
            // 如果是private,protected修饰的属性，需要修改为可以访问的
            field.setAccessible(true);
            object = field.get(obj);
            // 还原private,protected属性的访问性质
            field.setAccessible(accessible);
            return object;
        }
        object = field.get(obj);
        return object;
    }

    public static List<Object> strArrayToList(String[] strs) {
        List<Object> list = new ArrayList<Object>();
        Collections.addAll(list, strs);
        return list;
    }
    
    /**
	 * 分割List
	 * 
	 * @param list     待分割的list
	 * @param pageSize 每段list的大小
	 * @return List<<List<T>>
	 */
	public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
		int listSize = list.size(); // list的大小
		int page = (listSize + (pageSize - 1)) / pageSize; // 页数
		List<List<T>> listArray = new ArrayList<List<T>>(); // 创建list数组 ,用来保存分割后的list
		for (int i = 0; i < page; i++) { // 按照数组大小遍历
			List<T> subList = new ArrayList<T>(); // 数组每一位放入一个分割后的list
			for (int j = 0; j < listSize; j++) { // 遍历待分割的list
				int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize; // 当前记录的页码(第几页)
				if (pageIndex == (i + 1)) { // 当前记录的页码等于要放入的页码时
					subList.add(list.get(j)); // 放入list中的元素到分割后的list(subList)
				}
				if ((j + 1) == ((j + 1) * pageSize)) { // 当放满一页时退出当前循环
					break;
				}
			}
			listArray.add(subList); // 将分割后的list放入对应的数组的位中
		}
		return listArray;
	}
    
    
}
