package com.dova.apimaster.common.domain;

import java.beans.FeatureDescriptor;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class Field {
    protected String key;
    protected String value;
    protected String desc;
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
