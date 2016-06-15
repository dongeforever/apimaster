package com.dova.apimaster.executor.ast.impl;

import com.dova.apimaster.executor.ast.BindingObject;
import com.dova.apimaster.executor.ast.OperateExecutor;
import com.dova.apimaster.executor.ast.domain.Operator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by liuzhendong on 16/6/2.
 */
public class JsonBindingObject extends BindingObject{

    public JsonBindingObject(String name, JsonNode node){
        super(name,node);
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
        if(op == Operator.AT && index == 1){
            return true;
        }
        return false;
    }
}
