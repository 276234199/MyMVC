package com.zhouhai.handle;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhouhai.anno.Service;
import com.zhouhai.argument.ArgumentResolver;

/**
* 类说明:
* @author zhouhai
* @version 创建时间：2018年8月31日 下午3:26:05
*/
@Service("handleTool")
public class HandleToolsServiceImpl implements HandleToolsService{

	@Override
	public Object[] handle(HttpServletRequest req, HttpServletResponse resp, Method method,
			Map<String, Object> instanceMap) {
		Object[] args = new Object[method.getParameterCount()];
		//从大map中获取实现了ArgumentResolver接口的实例
		Map<String, Object> argReolovers = getIntanceType(instanceMap,ArgumentResolver.class);

		Parameter[] paras = method.getParameters();
		
		for(int index = 0 ; index<paras.length;index++) {
			Parameter para = paras[index];
			for(Map.Entry<String, Object> entry : argReolovers.entrySet()) {
				ArgumentResolver ar = (ArgumentResolver) entry.getValue();
				if(ar.isSupport(para.getType(), index, method)) {
					args[index] = ar.resolve(req, resp, para.getType(), index, method);
					break;
				}
			}
		}
		
		return args;
	}

	/**
	 * 从map中获取指定类型的instance
	 * @param instanceMap
	 * @param clazz
	 * @return
	 */
	private Map<String, Object> getIntanceType(Map<String, Object> instanceMap, Class<?> clazz) {
		Map<String, Object>  resultBeans =  new HashMap<>();
		
		for(Map.Entry<String, Object> entry : instanceMap.entrySet()) {
			//判断clazz是不是父类 父接口
			if(clazz.isAssignableFrom(entry.getValue().getClass())) {
				resultBeans.put(entry.getKey(), entry.getValue());
			}
		}
		return resultBeans;
	}

	
	
}
