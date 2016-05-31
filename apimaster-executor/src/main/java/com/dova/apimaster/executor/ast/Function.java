package com.dova.apimaster.executor.ast;

import com.dova.apimaster.executor.ast.domain.Operator;

/**
 * Created by liuzhendong on 16/5/28.
 */
public abstract class Function {

    private final String name;

    public Function(String name){
        this.name = name;
    }
    public abstract  Object run(Object[] args);


    public abstract Boolean allowedArgsNum(int num);


    public String getName() {
        return name;
    }
}
