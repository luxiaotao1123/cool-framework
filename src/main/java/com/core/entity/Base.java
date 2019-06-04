package com.core.entity;

import com.core.annotation.PrimaryKey;

import java.io.Serializable;

/**
 * Created by vincent on 2019-06-03
 */
public class Base implements Serializable,Cloneable {

    private static final long serialVersionUID = 43948582629964283L;

    @PrimaryKey
    private String id;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
