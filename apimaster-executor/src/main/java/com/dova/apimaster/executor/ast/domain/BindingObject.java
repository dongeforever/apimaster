package com.dova.apimaster.executor.ast.domain;

import com.dova.apimaster.executor.ast.OperateExecutor;
import com.dova.apimaster.executor.ast.impl.BaseOperateExecutor;

/**
 * Created by liuzhendong on 16/5/31.
 */
public class BindingObject<T> {
    private final String name;
    private final T object;

    private final OperateExecutor operateExecutor;

    public BindingObject(T object,String name){
        this.object = object;
        this.name = name;
        this.operateExecutor = new BaseOperateExecutor();
    }
    public BindingObject(T object, OperateExecutor operateExecutor,String name){
        this.object = object;
        this.name = name;
        this.operateExecutor = operateExecutor;
    }

    public T getObject() {
        return object;
    }

    public OperateExecutor getOperateExecutor() {
        return operateExecutor;
    }

    public String getName() {
        return name;
    }
}
