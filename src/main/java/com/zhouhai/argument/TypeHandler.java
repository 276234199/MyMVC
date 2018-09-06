package com.zhouhai.argument;

/**
 * 类说明:
 * 参数类型转换器
 * @author zhouhai
 * @version 创建时间：2018年9月6日 下午4:46:16
 */
public class TypeHandler {

	public static Object typeConvert(String source, Class<?> clazz) {
		
		if(source == null) {
			return null;
		}

		Object result = null;

		switch (clazz.getName()) {
		case "int":
			result = Integer.parseInt(source);
			break;
		case "String":
			result = source;
		case "boolean":
			result = Boolean.parseBoolean(source);
		default:
			result = source;
		}

		return result;
	}

}
