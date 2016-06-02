package com.dova.apimaster.executor.ast;

import com.dova.apimaster.executor.ast.OperateExecutor;
import com.dova.apimaster.executor.ast.domain.Operator;
import com.dova.apimaster.executor.ast.impl.BaseOperateExecutor;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by liuzhendong on 16/5/31.
 */
public abstract class BindingObject<T> {
    private final String name;
    @JsonIgnore
    private final T object;

    public BindingObject(String name, T object){
        this.object = object;
        this.name = name;
    }
    public T getObject() {
        return object;
    }

    public String getName() {
        return name;
    }



    public abstract OperateExecutor getOperateExecutor();

    public abstract boolean isKeyWord(Operator op,String name, int index);
}
