package com.dova.apimaster.executor.ast.impl;

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

}
