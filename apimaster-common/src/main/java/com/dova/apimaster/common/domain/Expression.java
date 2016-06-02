package com.dova.apimaster.common.domain;


/**
 * Created by liuzhendong on 16/5/23.
 * 表达式,正则或者JSON类型;
 * 可以用来assert结果, 也可以用来inject参数
 */

public class Expression {

    public enum Type{
        JSON,RE;
    }
    private String name;
    private String value;
    private Type type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Expression name(String name){
        this.name = name;
        return this;
    }

    public Expression type(Type type){
        this.type = type;
        return this;
    }
    public Expression value(String value){
        this.value = value;
        return this;
    }
}
