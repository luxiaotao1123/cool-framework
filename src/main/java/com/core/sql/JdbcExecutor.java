package com.core.sql;

import com.core.exception.CoolException;
import com.core.sql.convert.SqlPatter;
import com.core.sql.table.TableObject;
import com.core.sql.table.TableProperty;
import com.core.tools.ReflectTools;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 2019-06-03
 */
public class JdbcExecutor extends AbstractJdbcManager implements SqlPatter {

    private JdbcTemplate jdbcTemplate;

    public JdbcExecutor() {
    }

    public JdbcExecutor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public <T> int persist(List<T> objs) {
        try {
            if (objs.isEmpty()){
                return 0;
            }
            Class<?> cls = objs.get(0).getClass();
            TableObject tableObject = getTableObjectDefault(cls);
            String tableName = tableObject.getTableName();
            List<String> columns = new ArrayList<>();
            List<String> positions = new ArrayList<>();
            for(TableProperty tableProperty : tableObject.getProperties()) {
                columns.add(tableProperty.getFieldName());
                positions.add(PLACE);
            }
            String sql = String.format(INSERT, tableName, listToStr(columns, COMMA), listToStr(positions, COMMA));
            List<Object[]> params = new ArrayList<>();
            for (T t : objs){
                List<Object> objects = new ArrayList<>();
                for (String column : columns){
                    Field field = ReflectTools.getField(cls, column);
                    if (null == field){
                        throw new CoolException(cls.getSimpleName() + "not found field " + column);
                    }
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Object param = field.get(t);
                    if (param instanceof Enum){
                        param = String.valueOf(param);
                    }
                    field.setAccessible(flag);
                    objects.add(param);
                }
                params.add(objects.toArray());
            }

            return jdbcTemplate.batchUpdate(sql,params).length;
        } catch (Exception e){
            throw new CoolException(e.getMessage());
        }

    }





    private String listToStr(List<?> list, String patter){
        StringBuilder strBuilder=new StringBuilder();
        for(Object str : list){
            strBuilder.append(str);
            strBuilder.append(patter);
        }
        if(strBuilder.length()>0){
            return strBuilder.substring(0, strBuilder.length()-patter.length());
        }
        return strBuilder.toString();
    }
}
