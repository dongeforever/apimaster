package com.dova.apimaster.executor.ast.domain;

/**
 * Created by liuzhendong on 16/5/27.
 */
public class AstParseException extends RuntimeException {

    private AstParseError error;
    private String msg;
    public AstParseException(AstParseError error, String msg){
        super(error.desc + " " + msg);
        this.error = error;
        this.msg = msg;
    }

}
