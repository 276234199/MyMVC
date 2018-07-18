package com.zhouhai.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhouhai.anno.Autowired;
import com.zhouhai.anno.Controller;
import com.zhouhai.anno.RequestMapping;
import com.zhouhai.anno.RequestParam;
import com.zhouhai.anno.Service;

public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = -38802039993674064L;

	private List<String> classList = new ArrayList<String>();
	private Map<String, Object> instanceMap = new HashMap<String, Object>();
	private Map<String, Object> handleMap = new HashMap<String, Object>();

	@Override
	public void init() throws ServletException {
		super.init();
		String basePackage = "com.zhouhai";

		// basePackage转为basePath: /com/zhouhai,然后扫描带注解的类
		scanPackage(("/" + basePackage).replace(".", "/"));

		// 实例化service controller，并放入instanceMap
		initialInstance();

		// 依赖注入autowired
		ioc();

		// 建立映射关系
		handle();

	}

	private void scanPackage(String basePath) {
		File file = new File(DispatcherServlet.class.getResource(basePath).getFile()); //
		if (file.isDirectory()) {
			for (String path : file.list()) {
				scanPackage(basePath + "/" + path);
			}
		} else {
			// 转成.com.zhouhai.controller.TestController
			String replacedpath = basePath.replace(".class", "").replace("/", ".");
			// 去掉第一个.得到com.zhouhai.controller.TestController
			replacedpath = replacedpath.substring(1, replacedpath.length());

			classList.add(replacedpath);
		}

		if (classList.size() == 0) {
			System.out.println("没有扫描到一个类");
		}
	}

	private void initialInstance() {
		for (String className : classList) {
			try {
				Class<?> clazz = Class.forName(className);
				if (clazz.isAnnotationPresent(Controller.class)) {
					Object controllerObj = clazz.newInstance();
					String controllerKey = "";
					
					if (controllerObj.getClass().isAnnotationPresent(RequestMapping.class)) {
						RequestMapping controllerRQM = controllerObj.getClass().getAnnotation(RequestMapping.class);
						controllerKey = controllerRQM.value();
						if(controllerKey.equals("")) {
							controllerKey = controllerObj.getClass().getTypeName();
						}
						controllerKey = controllerKey.replaceAll("/", "");
						instanceMap.put(controllerKey, controllerObj);
					}else {
						try {
							throw new Exception("controller上必须有requestmapping");
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
					}
					
					
					instanceMap.put(controllerKey, controllerObj);
				} else if (clazz.isAnnotationPresent(Service.class)) {
					Object serviceObj = clazz.newInstance();
					Service serviceAnno = clazz.getAnnotation(Service.class);
					instanceMap.put(serviceAnno.value(), serviceObj);
				} else {

				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		if (instanceMap.size() == 0) {
			System.out.println("没有实例化任何controller、service之类的东西");
		}
	}

	private void ioc() {
		if (instanceMap.isEmpty()) {
			return;
		}

		for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
			// 拿到里面的所有属性
			Field fields[] = entry.getValue().getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);// 可访问私有属性
				if (field.isAnnotationPresent(Autowired.class)) {
					// private也能访问
					field.setAccessible(true);
					Autowired autowiredAnno = field.getAnnotation(Autowired.class);
					try {
						field.set(entry.getValue(), instanceMap.get(autowiredAnno.value()));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void handle() {
		if (instanceMap.size() <= 0) {
			return;
		}
		for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
			if (entry.getValue().getClass().isAnnotationPresent(Controller.class)) {

				String controllerPath = "";

				if (entry.getValue().getClass().isAnnotationPresent(RequestMapping.class)) {
					RequestMapping controllerRQM = entry.getValue().getClass().getAnnotation(RequestMapping.class);
					if (!controllerRQM.value().equals("")) {
						controllerPath = controllerRQM.value();
					}
				}

				Method[] methods = entry.getValue().getClass().getDeclaredMethods();
				for (Method method : methods) {
					method.setAccessible(true);
					if (method.isAnnotationPresent(RequestMapping.class)) {
						RequestMapping methodRQM = method.getAnnotation(RequestMapping.class);
						if (methodRQM.value().equals("")) {
							try {
								throw new Exception("method上的requestmapping不可为空");
							} catch (Exception e) {
								e.printStackTrace();
								return;
							}
						}
						handleMap.put(controllerPath  + methodRQM.value(), method);
					}
				}
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
//http://localhost/SimpleMVC/test/getInfo.do?name=qas123123de&age=asdasdasd&sex=12&ssss=2
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getRequestURI();
		String context = req.getContextPath();
		String path = url.replace(context, "");
		Method method = (Method) handleMap.get(path);
		if(method == null) {
			resp.getWriter().write("bad request");
		}
		Object controller = instanceMap.get(path.split("/")[1]);
		try {
			
			Parameter[] paras = method.getParameters();
			
			//用于保存invoke所需参数
			Object[] methodParams = new Object[paras.length];
		
			for(int i = 0 ;i<paras.length ; i++) {
				Parameter para = paras[i];
				
				if(para.getType().equals(HttpServletRequest.class)) {
					methodParams[i] = req;
					continue;
				}
				
				if(para.getType().equals(HttpServletResponse.class)) {
					methodParams[i] = resp;
					continue;
				}
				
				RequestParam[] paramAnnos = para.getDeclaredAnnotationsByType(RequestParam.class);
				if(paramAnnos.length>0) {
					RequestParam requestParamAnno = paramAnnos[0];
					String reqRaraName = requestParamAnno.value();
					methodParams[i] = req.getParameter(reqRaraName);
				}else {
					methodParams[i] = req.getParameter(para.getName());
				}
			}
			
			method.invoke(controller, methodParams);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
