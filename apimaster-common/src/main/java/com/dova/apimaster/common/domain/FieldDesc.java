package com.dova.apimaster.common.domain;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class FieldDesc{
    public enum Type{
        UNKNOWN,INT,STRING,FLOAT,BOOL;
    }
    private String name; //名称
    private Type type; //类型
    private boolean need; //是否必须
    private String defaultValue;
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isNeed() {
        return need;
    }

    public void setNeed(boolean need) {
        this.need = need;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
