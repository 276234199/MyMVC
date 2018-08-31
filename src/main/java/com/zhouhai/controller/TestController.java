package com.zhouhai.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhouhai.anno.Autowired;
import com.zhouhai.anno.Controller;
import com.zhouhai.anno.RequestMapping;
import com.zhouhai.anno.RequestParam;
import com.zhouhai.service.TestService;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired("testService")
	TestService testService;
	//http://localhost/SimpleMVC/test/getInfo.do?name=qas123123de&age=77&sex=12&ssss=2
	@RequestMapping("/getInfo.do")
	public void getInfo(HttpServletRequest req,HttpServletResponse resp,@RequestParam("name") String name, String age) throws IOException {
		resp.setContentType("utf-8");
		resp.getWriter().write(testService.getInfo(name, age));
	}
	//http://localhost/SimpleMVC/test/getInfo2.do?name=qas123123de&age=77&sex=12&ssss=2
	@RequestMapping("/getInfo2.do")
	public void getInfo2(HttpServletRequest req,HttpServletResponse resp,@RequestParam("name") String name, int age) throws IOException {
		resp.setContentType("utf-8");
		resp.getWriter().write(testService.getInfo2(name, age));
	}
}
