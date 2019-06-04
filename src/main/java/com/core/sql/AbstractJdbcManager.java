package com.core.sql;

import com.core.sql.factory.DefaultEntityFactory;
import com.core.sql.factory.EntityFactory;
import com.core.sql.table.TableObject;

/**
 * JdbcExecutor 适配器
 * Created by vincent on 2019-06-03
 */
public abstract class AbstractJdbcManager {

    private EntityFactory entityFactory;

    protected TableObject getTableObjectDefault(Class<?> prototype){
        return getEntityFactory().getEntity(prototype);
    }

    private EntityFactory getEntityFactory() {
        if (null == entityFactory){
            entityFactory = DefaultEntityFactory.newEntityFactory();
        }
        return entityFactory;
    }
}
