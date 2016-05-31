package com.dova.apimaster.executor.ast.domain;

/**
 * Created by liuzhendong on 16/5/27.
 */
public class AstException extends RuntimeException {

    private AstError error;
    private String msg;
    public AstException(AstError error, String msg){
        super(error.desc + " " + msg);
        this.error = error;
        this.msg = msg;
    }

}
