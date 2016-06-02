package com.dova.apimaster.executor.ast.impl;

import com.dova.apimaster.executor.ast.Function;
import com.dova.apimaster.executor.ast.BindingObject;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by liuzhendong on 16/5/31.
 */
public class Binding {

    private Map<String, BindingObject> objectMap = new HashMap<>();
    private Map<String, Function> functionMap = new HashMap<>();


    public void bindObject(BindingObject object){
        objectMap.put(object.getName(),object);
    }

    public void bindFunction(Function function){
        functionMap.put(function.getName(),function);
    }
    public BindingObject getBindingObject(String name){
        BindingObject res = objectMap.get(name);
        if(res == null){
            throw new NoSuchElementException();
        }
        return res;
    }

    public Function getFunction(String name){
        Function res = functionMap.get(name);
        if(res == null){
            throw new NoSuchElementException();
        }
        return res;
    }

    public Boolean hasBindingObject(String name){
        return objectMap.containsKey(name);
    }
    public Boolean hasFunction(String name){
        return functionMap.containsKey(name);
    }


}
