package com.core.sql.convert;

public interface SqlPatter {

    String INSERT="Insert Into %s(%s) Values(%s)";
    String UPDATE="Update %s Set %s Where %s=?";
    String AND=" And ";
    String COMMA=",";
    String PLACE = "?";

}
