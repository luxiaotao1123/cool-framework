package com.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应用端认证标签
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppAuth {
	
	Auth value() default Auth.CHECK;
	
	public enum Auth{
		//权限检测
		CHECK,
		//不检测权限
		NONE
	}
	
}
