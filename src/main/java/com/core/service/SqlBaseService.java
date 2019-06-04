package com.core.service;

import com.core.entity.Base;

public interface SqlBaseService<T extends Base>{
	
    long save(T t);
}
