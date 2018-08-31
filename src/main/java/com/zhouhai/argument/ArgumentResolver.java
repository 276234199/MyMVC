package com.zhouhai.argument;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* 类说明: 参数解析器
* @author zhouhai
* @version 创建时间：2018年8月31日 下午3:37:09
*/
public interface ArgumentResolver {

	/**
	 * 是否需要解析
	 * @param type 参数类型
	 * @param index 参数在方法中的index
	 * @param method 参数所在的方法
	 * @return true 需要解析。 false 不需要
	 */
	public Boolean isSupport(Class<?> type , int index , Method method);
	
	/**
	 * 解析参数
	 * @param req
	 * @param resp
	 * @param type 参数类型
	 * @param index 参数在方法中的index
	 * @param method 参数所在的方法
	 * @return 解析结果
	 */
	public Object resolve(HttpServletRequest req , HttpServletResponse resp,Class<?> type , int index , Method method);
}
