package com.dova.apimaster.executor.http;

import com.dova.apimaster.executor.ast.impl.AstParseExecutor;

/**
 * Created by liuzhendong on 16/6/6.
 */
public class InjectExecutor {


    private AstParseExecutor astParseExecutor;
    public InjectExecutor(AstParseExecutor astParseExecutor){
        this.astParseExecutor = astParseExecutor;
    }
}
