package com.zhouhai.argument;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhouhai.anno.RequestParam;
import com.zhouhai.anno.Service;

/**
* 类说明:解析除了HttpServletRequest 和 HttpServletResponse的其他参数类型
* @author zhouhai
* @version 创建时间：2018年8月31日 下午3:52:50
*/
@Service("paramArgumentResolver")
public class ParamArgumentResolver implements ArgumentResolver {

	@Override
	public Boolean isSupport(Class<?> type, int index, Method method) {
		Parameter[]  paras = method.getParameters();
		Parameter para = paras[index];
		if(para.isAnnotationPresent(RequestParam.class)) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolve(HttpServletRequest req, HttpServletResponse resp, Class<?> type, int index, Method method) {
		Parameter[]  paras = method.getParameters();
		Parameter para = paras[index];
		if(para.isAnnotationPresent(RequestParam.class)) {
			RequestParam reqParaAnno = para.getAnnotation(RequestParam.class);
			String value = reqParaAnno.value();
			return req.getParameter(value);
		}
		return null;
	}

}
