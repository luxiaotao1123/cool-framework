package com.core.sql.factory;

import com.core.sql.table.TableObject;

/**
 * ORM实体类工厂
 * Created by vincent on 2019-06-03
 */
public interface EntityFactory {

    TableObject getEntity(Class<?> prototype);

}
