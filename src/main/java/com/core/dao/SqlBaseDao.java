package com.core.dao;

import com.core.entity.Base;

import java.util.List;

public interface SqlBaseDao<T extends Base> {

    Long getSnowflakeIdWorkerNextId();

    int persist(T objs);

    int persist(List<T> objs);

    int merge(T entity);

}
