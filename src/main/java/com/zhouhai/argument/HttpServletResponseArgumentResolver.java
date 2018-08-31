package com.zhouhai.argument;

import java.lang.reflect.Method;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhouhai.anno.Service;

/**
* 类说明:HttpServletResponse解析
* @author zhouhai
* @version 创建时间：2018年8月31日 下午3:44:43
*/
@Service("httpServletResponseArgumentResolver")
public class HttpServletResponseArgumentResolver implements ArgumentResolver{

	@Override
	public Boolean isSupport(Class<?> type, int index, Method method) {
		
		return ServletResponse.class.isAssignableFrom(type);
	}

	@Override
	public Object resolve(HttpServletRequest req, HttpServletResponse resp, Class<?> type, int index, Method method) {
		return resp;
	}

}
