package com.core.service.impl;

import com.core.dao.SqlBaseDao;
import com.core.entity.Base;
import com.core.service.SqlBaseService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 2019-06-03
 */
public class SqlBaseServiceImpl<T extends Base> implements SqlBaseService<T> {

    private final SqlBaseDao<T> sqlBaseDao;

    public SqlBaseServiceImpl(SqlBaseDao<T> baseDao){
        this.sqlBaseDao = baseDao;
    }

    @Override
    public long save(List<T> list) {
        for (T t : list){
            t.setId(String.valueOf(sqlBaseDao.getSnowflakeIdWorkerNextId()));
        }
        return sqlBaseDao.persist(list);
    }

    @Override
    public long save(T t) {
        if (t.getId() == null){
            t.setId(String.valueOf(sqlBaseDao.getSnowflakeIdWorkerNextId()));
            List<T> list = new ArrayList<>();
            list.add(t);
            return sqlBaseDao.persist(list);
        }
        return sqlBaseDao.merge(t);
    }
}
