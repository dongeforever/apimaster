package com.dova.apimaster.executor.ast;

import com.dova.apimaster.executor.ast.domain.AstError;
import com.dova.apimaster.executor.ast.domain.AstException;
import com.dova.apimaster.executor.ast.domain.Operator;
import com.dova.apimaster.executor.ast.helper.Assert;
import com.dova.apimaster.executor.ast.helper.PrintUtil;

/**
 * Created by liuzhendong on 16/5/23.
 */
public abstract class OperateExecutor {

    //执行操作
    public  final Object exeOperate(Operator operator, Object[] args){
        Assert.assertion(operator.argNum == args.length, AstError.ValueNumError,"参数个数与操作符不符 op:%s argsNum:%d", operator.desc, args.length);
        switch (operator){
            case MINUS:
                return operateMinus(args[0],args[1]);
            case ADD:
                return operateAdd(args[0],args[1]);
            case MUTLI:
                return operateMulti(args[0],args[1]);
            case DIV:
                return operateDiv(args[0],args[1]);
            case AT:
                return operateAt(args[0],args[1]);
            case EQ:
                return operateEqual(args[0], args[1]);
            case LT:
                return operateLt(args[0], args[1]);
            case GT:
                return operateGt(args[0], args[1]);
            case LTEQ:
                return operateLtEq(args[0], args[1]);
            case GTEQ:
                return operateGtEq(args[0], args[1]);
            case OR:
                return operateOr(args[0], args[1]);
            case AND:
                return operateAnd(args[0], args[1]);
            case MOD:
                return operateMod(args[0], args[1]);
            case ASSIGN:
                return operateAssign(args[0], args[1]);
            case INDEX:
                return operateIndex(args[0], args[1]);
            default:
                throw new AstException(AstError.UnsupportedOperation, "暂时不支持的操作:" + operator.desc);
        }
    }

    protected abstract Object operateAdd(Object first, Object second);

    protected abstract Object operateMinus(Object first, Object second);

    protected abstract Object operateMulti(Object first, Object second);

    protected abstract Object operateAt(Object first, Object second);

    protected abstract Boolean operateEqual(Object first, Object second);

    protected abstract Boolean operateLt(Object first, Object second);

    protected abstract Boolean operateGt(Object first, Object second);

    protected abstract Boolean operateLtEq(Object first, Object second);
    protected abstract Boolean operateGtEq(Object first, Object second);

    protected abstract Boolean operateOr(Object first, Object second);

    protected abstract Boolean operateAnd(Object first, Object second);

    protected abstract Long operateMod(Object first, Object second);

    protected abstract Object operateDiv(Object first,Object second);

    protected abstract Object operateAssign(Object first, Object second);

    protected abstract Object operateIndex(Object first, Object second);


    public void print(String format,Object... args) {
        PrintUtil.print(format, args);
    }
}
