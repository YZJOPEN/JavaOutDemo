package com.yunzhijia.appdemo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json处理工具类
 * @author YangJie [2014年11月13日 下午8:35:48]
 */
public class JsonUtil {

	public static ObjectMapper om = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 忽略未知属性
	
	
	/**
	 * 对象转json
	 * @author YangJie [2015年11月4日 下午1:52:48]
	 * @param object
	 * @return
	 */
	public static String toJson(Object object){
		try {
			return om.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * json转对象
	 * @author YangJie [2015年11月4日 下午1:52:59]
	 * @param json
	 * @param valueType
	 * @return
	 */
	public static <T> T toObject(String json, Class<T> valueType) {
		try {
			return om.readValue(json, valueType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * json转对象(处理复杂类型对象) 
	 * List<bean> : json, List.class, Bean.class
	 * Map<bean1, bean2> : json, Map.class, Bean1.class, Bean2.class
	 * @author 
	 * @param json
	 * @param valueTypeRef
	 * @return
	 */
	public static <T> T toObject(String json, Class<?> collectionClass, Class<?>... elementClasses) {
		try {
			return om.readValue(json, om.getTypeFactory().constructParametricType(collectionClass, elementClasses));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}