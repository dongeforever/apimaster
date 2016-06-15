package com.dova.apimaster.executor.ast.impl;

import com.dova.apimaster.executor.ast.BindingObject;
import com.dova.apimaster.executor.ast.OperateExecutor;
import com.dova.apimaster.executor.ast.domain.Keyword;
import com.dova.apimaster.executor.ast.domain.Operator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Random;

/**
 * Created by liuzhendong on 16/6/2.
 */
public class TimestampBindingObject extends BindingObject{

    public TimestampBindingObject(String name, Object object){
        super(name, object);
    }
    BaseOperateExecutor baseOperateExecutor = new BaseOperateExecutor();

    @JsonIgnore
    @Override
    public OperateExecutor getOperateExecutor() {
        return baseOperateExecutor;
    }


    @JsonIgnore
    @Override
    public boolean isKeyWord(Operator op, String name, int index) {
        if(op == Operator.ADD
                || op == Operator.MINUS
                || op == Operator.DIV
                || op == Operator.MUTLI
                || op == Operator.EQ
                || op == Operator.GT
                || op == Operator.GTEQ
                || op == Operator.LT
                || op == Operator.LTEQ
                || op == Operator.MOD){
            return true;
        }
        if(op == Operator.ASSIGN && index == 2){
            return true;
        }
        return false;
    }

    @Override
    public Object getObject(){
        return System.currentTimeMillis();
    }

    private static TimestampBindingObject instance = null;

    @JsonIgnore
    public static TimestampBindingObject getInstance(){
        if(instance != null){
            return instance;
        }
        synchronized (TimestampBindingObject.class){
            if(instance != null){
                return instance;
            }
            instance = new TimestampBindingObject(Keyword.TIMESTAMP, System.currentTimeMillis());
            return instance;
        }
    }

}
