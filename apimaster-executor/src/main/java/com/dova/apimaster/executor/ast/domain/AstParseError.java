package com.dova.apimaster.executor.ast.domain;

/**
 * Created by liuzhendong on 16/5/27.
 */
public enum  AstParseError {
    InvalidArgs(0,"参数非法"),
    OpNoOP(1,"操作符后面不能再接操作符"),
    ValueNoValue(2,"值后面不能再接值"),
    ValueNumError(3,"值的个数不正确"),
    ValueTypeError(4,"值的类型不正确"),
    UnExpected(5,"不符合预期");

    int code;
    String desc;
    private AstParseError(int code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
