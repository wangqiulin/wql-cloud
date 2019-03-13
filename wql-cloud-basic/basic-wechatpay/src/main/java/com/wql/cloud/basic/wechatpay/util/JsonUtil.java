package com.wql.cloud.basic.wechatpay.util;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtil {
	public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static JsonUtil allJsonUtil;
	private static JsonUtil notNullJsonUtil;
	private static JsonUtil notDefJsonUtil;
	private static JsonUtil notEmpJsonUtil;

	public static JsonUtil getAllJsonUtil() {
		return allJsonUtil;
	}

	public static void setAllJsonUtil(JsonUtil allJsonUtil) {
		JsonUtil.allJsonUtil = allJsonUtil;
	}

	public static JsonUtil getNotNullJsonUtil() {
		return notNullJsonUtil;
	}

	public static void setNotNullJsonUtil(JsonUtil notNullJsonUtil) {
		JsonUtil.notNullJsonUtil = notNullJsonUtil;
	}

	public static JsonUtil getNotDefJsonUtil() {
		return notDefJsonUtil;
	}

	public static void setNotDefJsonUtil(JsonUtil notDefJsonUtil) {
		JsonUtil.notDefJsonUtil = notDefJsonUtil;
	}

	public static JsonUtil getNotEmpJsonUtil() {
		return notEmpJsonUtil;
	}

	public static void setNotEmpJsonUtil(JsonUtil notEmpJsonUtil) {
		JsonUtil.notEmpJsonUtil = notEmpJsonUtil;
	}

	private ObjectMapper mapper;

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public JsonUtil(Include include) {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(include);
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 禁止使用int代表Enum的order()來反序列化Enum,非常危險
		mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);

		// 增加对map的过滤
		switch (include) {
		case NON_NULL:
		case NON_EMPTY:
			mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
		default:
			break;
		}
		setDateFormat(DATE_TIME_FORMAT);

	}

	/**
	 * 创建输出全部属性
	 * 
	 * @return
	 */
	public static JsonUtil buildNormalBinder() {
		synchronized (JsonUtil.class) {
			if (JsonUtil.allJsonUtil == null) {
				JsonUtil.allJsonUtil = new JsonUtil(Include.ALWAYS);
			}
		}
		return JsonUtil.allJsonUtil;
	}

	/**
	 * 创建只输出非空属性的
	 * 
	 * @return
	 */
	public static JsonUtil buildNonNullBinder() {
		synchronized (JsonUtil.class) {
			if (JsonUtil.notNullJsonUtil == null) {
				JsonUtil.notNullJsonUtil = new JsonUtil(Include.NON_NULL);
			}
		}
		return JsonUtil.notNullJsonUtil;
	}

	/**
	 * 创建只输出初始值被改变的属性
	 * 
	 * @return
	 */
	public static JsonUtil buildNonDefaultBinder() {
		synchronized (JsonUtil.class) {
			if (JsonUtil.notDefJsonUtil == null) {
				JsonUtil.notDefJsonUtil = new JsonUtil(Include.NON_DEFAULT);
			}
		}
		return JsonUtil.notDefJsonUtil;
	}

	/**
	 * 创建只输出初始值被改变的属性
	 * 
	 * @return
	 */
	public static JsonUtil buildNonEmptyBinder() {
		synchronized (JsonUtil.class) {
			if (JsonUtil.notEmpJsonUtil == null) {
				JsonUtil.notEmpJsonUtil = new JsonUtil(Include.NON_EMPTY);
			}
		}
		return JsonUtil.notEmpJsonUtil;
	}

	/**
	 * 把json字符串转成对象
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public <T> T getJsonToObject(String json, Class<T> clazz) {
		T object = null;
		if (StringUtils.isNotBlank(json)) {
			try {
				object = getMapper().readValue(json, clazz);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	/**
	 * 把JSON转成list
	 * 
	 * @param json
	 *            [{"...":"...","...":"...","...":"..."}]
	 * @param clazz
	 *            map
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Object getJsonToList(String json, Class clazz) {
		Object object = null;
		if (StringUtils.isNotBlank(json)) {
			try {
				object = getMapper().readValue(json,
						TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	/**
	 * 把JSON转成list中是MAP《String，clazz》
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Object getJsonToListByMap(String json, Class clazz) {
		Object object = null;
		if (StringUtils.isNotBlank(json)) {
			try {
				object = getMapper().readValue(json, TypeFactory.defaultInstance().constructArrayType(
						TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, clazz)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	@SuppressWarnings({ "rawtypes" })
	public Object[] getJsonToArray(String json, Class clazz) {
		Object[] object = null;
		if (StringUtils.isNotBlank(json)) {
			try {
				object = getMapper().readValue(json, TypeFactory.defaultInstance().constructArrayType(clazz));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	public byte[] getJsonTobyteArray(String json) {
		byte[] object = null;
		if (StringUtils.isNotBlank(json)) {
			try {
				object = getMapper().readValue(json, TypeFactory.defaultInstance().constructArrayType(byte.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	/**
	 * 复杂例子：
	 * 
	 * Map<String,List<Map<String,String>>> aa=new HashMap<String,
	 * List<Map<String,String>>>(); List<Map<String,String>> list=new
	 * ArrayList<Map<String,String>>(); HashMap<String,String> hm=new
	 * HashMap<String, String>(); hm.put("province", "210000"); hm.put("name",
	 * "大连万达集团股份有限公司"); HashMap<String,String> hm2=new HashMap<String, String>();
	 * hm2.put("province", "220000"); hm2.put("name", "大连万达集团股份有限公司1");
	 * list.add(hm); list.add(hm2); aa.put("3", list); String
	 * a=JsonUtil.buildNormalBinder().toJson(aa); System.out.println(aa); 1*
	 * Map<String,Object> as=(Map<String, Object>)
	 * JsonUtil.buildNormalBinder().getJsonToMap(a, String.class, Object.class); 2*
	 * List<Map<String,String>> lll=(List<Map<String, String>>) as.get("3"); 3*
	 * System.out.println(((Map<String, String>)lll.get(0)).get("name"));
	 * 1\2\3行等效于4\5 4*Map<String,List<Map<String,String>>>
	 * as=(Map<String,List<Map<String,String>>>)
	 * JsonUtil.buildNormalBinder().getJsonToMap(a, String.class, Object.class); 5 *
	 * System.out.println(as.get("3").get(0).get("name"));
	 * 
	 * 把JSON转成Map
	 * 
	 * @param json
	 * @param keyclazz
	 * @param valueclazz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Object getJsonToMap(String json, Class keyclazz, Class valueclazz) {
		Object object = null;
		if (StringUtils.isNotBlank(json)) {
			try {
				object = getMapper().readValue(json,
						TypeFactory.defaultInstance().constructParametricType(HashMap.class, keyclazz, valueclazz));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	/**
	 * 把JSON转成并发Map
	 * 
	 * @param json
	 * @param keyclazz
	 * @param valueclazz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Object getJsonToConcMap(String json, Class keyclazz, Class valueclazz) {
		Object object = null;
		if (StringUtils.isNotBlank(json)) {
			try {
				object = getMapper().readValue(json, TypeFactory.defaultInstance()
						.constructParametricType(ConcurrentHashMap.class, keyclazz, valueclazz));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	/**
	 * 把JSON转成Map<> value是MAP类型(KEY string )
	 * 
	 * @param json
	 * @param keyclazz
	 * @param valueclazz
	 *            MAP中的value CLASS
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public Object getJsonToMapByMap(String json, Class keyclazz, Class valueclazz) {
		Object object = null;
		if (StringUtils.isNotBlank(json)) {
			try {
				object = getMapper().readValue(json, TypeFactory.defaultInstance().constructMapType(HashMap.class,
						TypeFactory.defaultInstance().uncheckedSimpleType(keyclazz),
						TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, valueclazz)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	/**
	 * 把JSON转成Map<> value是LIST类型
	 * 
	 * @param json
	 * @param keyclazz
	 * @param valueclazz
	 *            LIST中的CLASS
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public Object getJsonToMapByList(String json, Class keyclazz, Class valueclazz) {
		Object object = null;
		if (StringUtils.isNotBlank(json)) {
			try {
				object = getMapper().readValue(json,
						TypeFactory.defaultInstance().constructMapType(HashMap.class,
								TypeFactory.defaultInstance().uncheckedSimpleType(keyclazz),
								TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, valueclazz)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	/**
	 * 把map转成combo数据格式的json格式
	 * 
	 * @return String (json)
	 */
	public String getMapToJson(Map<String, String> map) {
		List<String[]> list = new ArrayList<String[]>();
		if (null != map && !map.isEmpty()) {
			for (String key : map.keySet()) {
				String[] strS = new String[2];
				strS[0] = key;
				strS[1] = map.get(key);
				list.add(strS);
			}
		}
		return jsonObject(list);
	}

	/**
	 * 把对象转成json格式
	 * 
	 * @param obj
	 *            需要转的对象
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String jsonObject(List list) {
		StringWriter sw = new StringWriter();
		JsonGenerator gen;
		try {
			gen = new JsonFactory().createGenerator(sw);
			getMapper().writeValue(gen, list);
			gen.close();
		} catch (Exception e) {

		}
		return sw.toString();
	}

	/**
	 * 把JSON转成Object
	 * 
	 * @param json
	 * @param keyclazz
	 * @param valueclazz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Object getJsonToObject(String json, Class objclazz, Class... pclazz) {
		Object object = null;
		if (StringUtils.isNotBlank(json)) {
			try {
				object = getMapper().readValue(json,
						TypeFactory.defaultInstance().constructParametricType(objclazz, pclazz));
			} catch (Exception e) {
			}
		}
		return object;
	}

	/**
	 * 把对象转成字符串
	 * 
	 * @param object
	 * @return
	 */
	public String toJson(Object object) {
		String json = null;
		try {
			json = getMapper().writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 设置日期格式
	 * 
	 * @param pattern
	 */
	public void setDateFormat(String pattern) {
		if (StringUtils.isNotBlank(pattern)) {
			DateFormat df = new SimpleDateFormat(pattern);
			getMapper().setDateFormat(df);
		}
	}

	@SuppressWarnings("unchecked")
	public static Object getResultObject(String json) {
		Map<String, Object> map = (Map<String, Object>) buildNormalBinder().getJsonToMap(json, String.class,
				Object.class);
		if (map != null) {
			return map.get("result");
		} else {
			return json;
		}
	}

}
