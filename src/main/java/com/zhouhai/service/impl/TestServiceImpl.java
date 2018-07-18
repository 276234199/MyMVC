package com.zhouhai.service.impl;

import com.zhouhai.anno.Service;
import com.zhouhai.service.TestService;

@Service("testService")
public class TestServiceImpl implements TestService{

	public String getInfo(String name, String age) {
		return "name:"+name+" -- age:"+age;
	}

	@Override
	public String getInfo(String name, String age, String sex) {
		return "name:"+name+" -- age:"+age +" --- sex:" + sex;
	}

}
