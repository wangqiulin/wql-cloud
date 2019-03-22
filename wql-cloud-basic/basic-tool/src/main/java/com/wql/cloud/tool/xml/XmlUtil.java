package com.wql.cloud.tool.xml;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXB;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);

	/**
	 * 给定map和root节点生成xml,map必须是String,String的，其他类型会涉及到递归，请使用map2xml
	 * 
	 * @param Map
	 *            <String,String>
	 * @param rootStr
	 * @return
	 */
	public static String mapToXml(Map<String, String> map, String rootStr) {
		String returnXml=null;
		StringBuffer sb = new StringBuffer();
		sb.append("<").append(rootStr).append(">");
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String val = entry.getValue();
			if (IsNumeric(val)) {
				sb.append("<");
				sb.append(key);
				sb.append(">");
				sb.append(val);
				sb.append("</");
				sb.append(key);
				sb.append(">");
			} else {
				sb.append("<");
				sb.append(key);
				sb.append("><![CDATA[");
				sb.append(val);
				sb.append("]]></");
				sb.append(key);
				sb.append(">");
			}
		}
		sb.append("</").append(rootStr).append(">");
		returnXml = sb.toString();
		logger.debug("XmlUtil.MapToXml:" + returnXml);
		return returnXml;
	}

	/**
	 * map转xml常用，可多层递归
	 * 
	 * @param vo
	 * @param rootElement
	 * @return
	 */
	public static String map2xml(Map<String, Object> vo, String rootElement) {
		Document doc = DocumentHelper.createDocument();
		Element body = DocumentHelper.createElement(rootElement);
		doc.add(body);
		__buildMap2xml(body, vo);
		return doc.asXML();
	}

	/**
	 * 根据xml消息体转化为Map
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String, Object> xml2map(String xml) throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);
		Element rootElement = doc.getRootElement();
		Map<String, Object> vo = __buildXmlBody2map(rootElement);
		return vo;
	}

	/**
	 * 给定xml和对象的class，将xml转成对象
	 * 
	 * @param xmlStr
	 * @param clazz
	 * @return
	 */
	public static <T> T XmlParseObject(String xmlStr, Class<T> clazz) {
		T t = null;
		try {
			StringReader reder = new StringReader(xmlStr);
			t = JAXB.unmarshal(reder, clazz);
		} catch (Exception e) {
			logger.error("XmlUtil.XmlParseObject-xml转对象异常！", e);
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	private static void __buildMap2xml(Element body, Map<String, Object> vo) {
		if (vo != null) {
			Iterator<String> it = vo.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (StringUtils.isNotEmpty(key)) {
					Object obj = vo.get(key);
					Element element = DocumentHelper.createElement(key);
					if (obj != null) {
						if (obj instanceof java.lang.String) {
							element.setText((String) obj);
						} else {
							if (obj instanceof java.lang.Character || obj instanceof java.lang.Boolean || obj instanceof java.lang.Number || obj instanceof java.math.BigInteger || obj instanceof java.math.BigDecimal) {
								org.dom4j.Attribute attr = DocumentHelper.createAttribute(element, "type", obj.getClass().getCanonicalName());
								element.add(attr);
								element.setText(String.valueOf(obj));
							} else if (obj instanceof java.util.Map) {
								org.dom4j.Attribute attr = DocumentHelper.createAttribute(element, "type", java.util.Map.class.getCanonicalName());
								element.add(attr);
								__buildMap2xml(element, (Map<String, Object>) obj);
							} else {
							}
						}
					}
					body.add(element);
				}
			}
		}
	}

	private static Map<String, Object> __buildXmlBody2map(Element body) {
		Map<String, Object> vo = new HashMap<String, Object>();
		if (body != null) {
			List<Element> elements = body.elements();
			for (Element element : elements) {
				String key = element.getName();
				if (StringUtils.isNotEmpty(key)) {
					String type = element.attributeValue("type", "java.lang.String");
					String text = element.getText().trim();
					Object value = null;
					if (java.lang.String.class.getCanonicalName().equals(type)) {
						value = text;
					} else if (java.lang.Character.class.getCanonicalName().equals(type)) {
						value = new java.lang.Character(text.charAt(0));
					} else if (java.lang.Boolean.class.getCanonicalName().equals(type)) {
						value = new java.lang.Boolean(text);
					} else if (java.lang.Short.class.getCanonicalName().equals(type)) {
						value = java.lang.Short.parseShort(text);
					} else if (java.lang.Integer.class.getCanonicalName().equals(type)) {
						value = java.lang.Integer.parseInt(text);
					} else if (java.lang.Long.class.getCanonicalName().equals(type)) {
						value = java.lang.Long.parseLong(text);
					} else if (java.lang.Float.class.getCanonicalName().equals(type)) {
						value = java.lang.Float.parseFloat(text);
					} else if (java.lang.Double.class.getCanonicalName().equals(type)) {
						value = java.lang.Double.parseDouble(text);
					} else if (java.math.BigInteger.class.getCanonicalName().equals(type)) {
						value = new java.math.BigInteger(text);
					} else if (java.math.BigDecimal.class.getCanonicalName().equals(type)) {
						value = new java.math.BigDecimal(text);
					} else if (java.util.Map.class.getCanonicalName().equals(type)) {
						value = __buildXmlBody2map(element);
					} else {
					}
					vo.put(key, value);
				}
			}
		}
		return vo;
	}

	private static boolean IsNumeric(String str) {
		if (StringUtils.isBlank(str))
			return false;
        return str.matches("\\d *");
	}

}
