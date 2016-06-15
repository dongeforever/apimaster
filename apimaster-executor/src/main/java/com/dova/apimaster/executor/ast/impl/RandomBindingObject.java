package com.dova.apimaster.executor.ast.impl;

import com.dova.apimaster.executor.ast.BindingObject;
import com.dova.apimaster.executor.ast.OperateExecutor;
import com.dova.apimaster.executor.ast.domain.Keyword;
import com.dova.apimaster.executor.ast.domain.Operator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by liuzhendong on 16/6/2.
 */
public class RandomBindingObject extends BindingObject{

    public RandomBindingObject(String name, Random object){
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
        Random random = (Random)super.getObject();
        return random.nextInt(1000);
    }

    private static RandomBindingObject instance = null;

    @JsonIgnore
    public static RandomBindingObject getInstance(){
        if(instance != null){
            return instance;
        }
        synchronized (RandomBindingObject.class){
            if(instance != null){
                return instance;
            }
            instance = new RandomBindingObject(Keyword.RANDOM, new Random());
            return instance;
        }
    }

}
