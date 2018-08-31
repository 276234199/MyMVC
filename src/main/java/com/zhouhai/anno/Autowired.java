package com.zhouhai.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

/**
 * spring源码只有required 
 * 这里省略了required
 * 这里添加一个value 
 * 类似于 @Resource 注解
 * 集成了 @Qualifier
 * @author zhouhai
 *
 */
public @interface Autowired {

	public String value() default "";
	
}
