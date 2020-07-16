package com.core.common;


import com.core.exception.ApplicationException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 缓存
 *
 */
public class Cache {

	private Map<String,Object> caches=new ConcurrentHashMap<>();
	
	@SuppressWarnings("unchecked")
	public <T>T get(Class<T> prototype) {
		return (T) get(prototype.getName());
	}
	
	public Object get(String key) {
		return get(key,true);
	}
	
	@SuppressWarnings("unchecked")
	public <T>T get(String key,Supplier<T> func){
		if(!hasKey(key)) {
			put(key,func.get());
		}
		return (T)get(key);
	}
	
	public Object get(String key,boolean exist) {
		if(exist) {
			if(!hasKey(key)) {
				throw new ApplicationException(this+"-找不到缓存对象:"+key);
			}
		}
		return caches.get(key);
	}
	
	public Cache put(Object value) {
		String key=value.getClass().getName();
		put(key,value);
		return this;
	}
	
	public Cache put(String key,Object value) {
		put(key, value,true);
		return this;
	}
	
	public Cache put(String key,Object value,boolean exist) {
		if(exist) {
			if(hasKey(key)) {
				throw new ApplicationException(this+"-缓存"+key+"已存在");
			}
		}
		caches.put(key, value);
		return this;
	}
	
	public boolean hasKey(Class<?> prototype) {
		return hasKey(prototype.getName());
	}
	
	public boolean hasKey(String key) {
		return caches.containsKey(key);
	}
	
}
