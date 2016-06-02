package com.dova.apimaster.executor.ast.impl;

import com.dova.apimaster.executor.ast.BindingObject;
import com.dova.apimaster.executor.ast.Function;
import com.dova.apimaster.executor.ast.domain.AstNode;

/**
 * Created by liuzhendong on 16/5/31.
 */
public class AstParseExecutor {

    //every thread should have a copy
    private ThreadLocal<Binding>  bindingThreadLocal  = new ThreadLocal<Binding>(){
        @Override
        protected Binding initialValue() {
            return new Binding();
        }
    };
    private AstParser astParser = new AstParser();
    private AstExecutor astExecutor = new AstExecutor();

    public AstParseExecutor(){
        astParser.setDebug(false);
    }

    public AstNode parseAndExecute(String expression){
        AstNode root = astParser.parse(expression, getBinding());
        astExecutor.execute(root);
        return root;
    }
    public Binding getBinding(){
        return bindingThreadLocal.get();
    }

    public void bindObject(BindingObject bindingObject){
        getBinding().bindObject(bindingObject);
    }

    public void bindFunction(Function function){
        getBinding().bindFunction(function);
    }
}
