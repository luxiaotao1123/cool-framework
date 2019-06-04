package com.core.service.impl;

import com.core.dao.SqlBaseDao;
import com.core.entity.Base;
import com.core.service.SqlBaseService;

/**
 * Created by vincent on 2019-06-03
 */
public class SqlBaseServiceImpl<T extends Base> implements SqlBaseService<T> {

    private final SqlBaseDao<T> sqlBaseDao;

    public SqlBaseServiceImpl(SqlBaseDao<T> baseDao){
        this.sqlBaseDao = baseDao;
    }

    @Override
    public long save(T t) {
        if (t.getId() == null){
            t.setId(String.valueOf(sqlBaseDao.getSnowflakeIdWorkerNextId()));
            return sqlBaseDao.persist(t);
        }
        return sqlBaseDao.merge(t);
    }
}
