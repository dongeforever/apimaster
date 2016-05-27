package com.dova.apimaster.executor.ast.helper;


import com.dova.apimaster.executor.ast.domain.AstParseError;
import com.dova.apimaster.executor.ast.domain.AstParseException;

/**
 * Created by liuzhendong on 16/5/27.
 */
public class Assert {
    public static void assertion(boolean expression, AstParseError error, String format, Object... args){
        if(!expression){
            throw  new AstParseException(error,String.format(format,args));
        }
    }

}
