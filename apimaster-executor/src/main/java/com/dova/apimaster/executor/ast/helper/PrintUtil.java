package com.dova.apimaster.executor.ast.helper;

/**
 * Created by liuzhendong on 16/5/27.
 */
public class PrintUtil {

    public static void print(String format, Object... args){
        System.out.println(String.format(format, args));
    }

}
