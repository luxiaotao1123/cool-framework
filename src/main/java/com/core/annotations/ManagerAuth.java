package com.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 管理端认证标签
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagerAuth {
	
	Auth value() default Auth.CHECK;

	// 备注
	String memo() default "";
	
	public enum Auth{
		//权限检测
		CHECK,
		//不检测权限
		NONE
	}
	
}
