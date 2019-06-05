package com.core.dao.impl;

import com.core.dao.SqlBaseDao;
import com.core.entity.Base;
import com.core.sql.JdbcExecutor;
import com.core.tools.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by vincent on 2019-06-03
 */
public class SqlBaseDaoImpl<T extends Base> implements SqlBaseDao<T> {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private JdbcExecutor jdbcExecutor;

    /**
     * Prototype
     */
    private final Class<T> prototype;

    public SqlBaseDaoImpl(Class<T> prototype){
        this.prototype=prototype;
    }

    @Override
    public Long getSnowflakeIdWorkerNextId() {
        return snowflakeIdWorker.nextId();
    }

    @Override
    public int persist(List<T> objs) {
        return jdbcExecutor.persist(objs);
    }

    @Override
    public int merge(T entity) {
        return jdbcExecutor.merge(entity);
    }

}
