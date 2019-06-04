package com.core.sql.factory;

import com.core.annotation.Column;
import com.core.annotation.Table;
import com.core.sql.table.TableObject;
import com.core.sql.table.TableProperty;
import com.core.tools.ReflectTools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ORM实体类工厂实现累（单例）
 * Created by vincent on 2019-06-03
 */
public class DefaultEntityFactory implements EntityFactory {

    private volatile static DefaultEntityFactory instance = null;

    private DefaultEntityFactory(){
    }

    public static DefaultEntityFactory newEntityFactory(){
        if (instance == null){
            synchronized (DefaultEntityFactory.class){
                instance = new DefaultEntityFactory();
                return instance;
            }
        }
        return instance;
    }

    private Map<Class<?>, TableObject> tableCache = new ConcurrentHashMap<>();

    @Override
    public TableObject getEntity(Class<?> prototype) {
        TableObject obj = tableCache.get(prototype);
        if (null == obj){
            obj = new TableObject();
            String tableName = prototype.isAnnotationPresent(Table.class) ? prototype.getAnnotation(Table.class).value() : prototype.getSimpleName();
            obj.setTableName(tableName);
            Field[] allFields = ReflectTools.getAllFields(prototype);
            Field[] fields = ReflectTools.removeStaticField(allFields);
            if (null == fields || 0 == fields.length){
                return null;
            }
            List<TableProperty> properties = new ArrayList<>();
            for (Field field : fields){
                TableProperty tableProperty = new TableProperty();
                String fieldName = field.getName();
                if(field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    fieldName = column.value();
                }
                tableProperty.setFieldName(fieldName);
                tableProperty.setFieldType(field.getType());
                properties.add(tableProperty);
            }
            obj.setProperties(properties);
            tableCache.put(prototype, obj);
        }
        return obj;
    }

}
