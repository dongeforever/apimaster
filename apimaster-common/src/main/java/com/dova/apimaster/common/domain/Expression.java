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

}
