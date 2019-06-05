package com.core.service;

import com.core.entity.Base;

import java.util.List;

public interface SqlBaseService<T extends Base>{

    long save(List<T> list);

    long save(T t);
}
