package com.wql.cloud.basic.push.util;

import java.io.IOException;
import java.util.TimeZone;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper.
 */
public class JsonUtils extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private static JsonUtils mapper;

    public JsonUtils() {
        this(Include.NON_EMPTY);
    }

    public JsonUtils(Include include) {
        // 设置输出时包含属性的风格
        if (include != null) {
            this.setSerializationInclusion(include);
        }
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 空值处理为空串
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                jgen.writeString("");
            }
        });
        // 进行HTML解码。
        this.registerModule(new SimpleModule().addSerializer(String.class, new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                jgen.writeString(StringEscapeUtils.unescapeHtml4(value));
            }
        }));
        // 设置时区
        this.setTimeZone(TimeZone.getDefault());// getTimeZone("GMT+8:00")
    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.
     */
    public static JsonUtils getInstance() {
        if (mapper == null) {
            mapper = new JsonUtils().enableSimple();
        }
        return mapper;
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。
     */
    public static JsonUtils nonDefaultMapper() {
        if (mapper == null) {
            mapper = new JsonUtils(Include.NON_DEFAULT);
        }
        return mapper;
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
     */
    public String toJson(Object object) {
        try {
            return this.writeValueAsString(object);
        } catch (IOException e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("write to json string error:" + object, e);
            }
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * <p>
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     * <p>
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     *
     * @see #fromJson(String, JavaType)
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return this.readValue(jsonString, clazz);
        } catch (IOException e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("parse json string error:" + jsonString, e);
            }
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函數createCollectionType构造类型,然后调用本函数.
     *
     * @see #createCollectionType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) this.readValue(jsonString, javaType);
        } catch (IOException e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("parse json string error:" + jsonString, e);
            }
            return null;
        }
    }

    /**
     * 構造泛型的Collection Type如: ArrayList<MyBean>, 则调用constructCollectionType(ArrayList.class,MyBean.class)
     * HashMap<String,MyBean>, 则调用(HashMap.class,String.class, MyBean.class)
     */
    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return this.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
     */
    @SuppressWarnings("unchecked")
    public <T> T update(String jsonString, T object) {
        try {
            return (T) this.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
            }
        } catch (IOException e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
            }
        }
        return null;
    }

    /**
     * 輸出JSONP格式數據.
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 設定是否使用Enum的toString函數來讀寫Enum, 為False時時使用Enum的name()函數來讀寫Enum, 默認為False. 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
     */
    public JsonUtils enableEnumUseToString() {
        this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        return this;
    }

    /**
     * 支持使用Jaxb的Annotation，使得POJO上的annotation不用与Jackson耦合。 默认会先查找jaxb的annotation，如果找不到再找jackson的。
     */
    public JsonUtils enableJaxbAnnotation() {
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        this.registerModule(module);
        return this;
    }

    /**
     * 允许单引号 允许不带引号的字段名称
     */
    public JsonUtils enableSimple() {
        this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return this;
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return this;
    }

    /**
     * 对象转换为JSON字符串
     *
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        return JsonUtils.getInstance().toJson(object);
    }

    /**
     * JSON字符串转换为对象
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T fromJsonString(String jsonString, Class<T> clazz) {
        return JsonUtils.getInstance().fromJson(jsonString, clazz);
    }

    /**
     * 将obj对象转换成 class类型的对象
     *
     * @param obj
     * @param clazz
     * @return
     */
    public static <T> T parseObject(Object obj, Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(obj), clazz);
    }

    /**
     * 将obj对象转换成 class类型的对象
     *
     * @param obj
     * @param clazz
     * @return
     */
    public static <T> T parseObjectAsJackson(Object obj, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writeValueAsString(obj);
            return mapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new RuntimeException("parse object A to B error", e);
        }
    }

    /**
     * 将obj对象转换成,支持泛型模式
     *
     * @param obj
     * @param type
     * @return
     */
    public static <T> T parseObjectAsJackson(Object obj, TypeReference<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writeValueAsString(obj);
            return mapper.readValue(jsonString, type);
        } catch (Exception e) {
            throw new RuntimeException("parse object A to B error", e);
        }
    }

}