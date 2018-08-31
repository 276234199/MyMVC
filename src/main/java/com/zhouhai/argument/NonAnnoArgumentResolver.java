package com.zhouhai.argument;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhouhai.anno.RequestParam;
import com.zhouhai.anno.Service;

/**
* 类说明:解析除了HttpServletRequest 和 HttpServletResponse之外的不带注解的类型
* @author zhouhai
* @version 创建时间：2018年8月31日 下午3:52:50
*/
@Service("nonAnnoArgumentResolver")
public class NonAnnoArgumentResolver implements ArgumentResolver {

	@Override
	public Boolean isSupport(Class<?> type, int index, Method method) {
		Parameter[]  paras = method.getParameters();
		Parameter para = paras[index];
		if(!ServletResponse.class.isAssignableFrom(type) && 
		   !ServletRequest.class.isAssignableFrom(type) && 
		   !para.isAnnotationPresent(RequestParam.class)) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolve(HttpServletRequest req, HttpServletResponse resp, Class<?> type, int index, Method method) {
		Parameter[]  paras = method.getParameters();
		Parameter para = paras[index];
		return req.getParameter(para.getName());
	}

}
