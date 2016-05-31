package com.dova.apimaster.executor.ast.helper;


import com.dova.apimaster.executor.ast.domain.AstError;
import com.dova.apimaster.executor.ast.domain.AstException;

/**
 * Created by liuzhendong on 16/5/27.
 */
public class Assert {
    public static void assertion(boolean expression, AstError error, String format, Object... args){
        if(!expression){
            throw  new AstException(error,String.format(format,args));
        }
    }

}
