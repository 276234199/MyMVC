package com.zhouhai.handle;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* 类说明:
* @author zhouhai
* @version 创建时间：2018年8月31日 下午3:23:09
*/
public interface HandleToolsService {

	/**
	 * 
	 * @param req
	 * @param resp
	 * @param method 方法
	 * @param instanceMap ioc大Map
	 * @return method.invoke() 所需参数
	 */
	public Object[] handle(HttpServletRequest req, HttpServletResponse resp,
			Method method , Map<String , Object> instanceMap); 
	
}
