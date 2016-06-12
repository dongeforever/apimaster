package com.dova.apimaster.executor.ast.helper;

/**
 * Created by liuzhendong on 16/5/27.
 */
public class PrintUtil {

    public static Boolean debug  = false;
    public static void print(String format, Object... args){
        if(debug){
            System.out.println(String.format(format, args));
        }
    }
}
