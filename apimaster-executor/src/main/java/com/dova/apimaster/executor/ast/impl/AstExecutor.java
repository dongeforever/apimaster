package com.dova.apimaster.executor.ast.impl;

import com.dova.apimaster.executor.ast.BindingObject;
import com.dova.apimaster.executor.ast.OperateExecutor;
import com.dova.apimaster.executor.ast.domain.AstException;
import com.dova.apimaster.executor.ast.domain.AstNode;
import com.dova.apimaster.executor.ast.domain.AstError;
import com.dova.apimaster.executor.ast.domain.Operator;
import com.dova.apimaster.executor.ast.helper.Assert;

/**
 * Created by liuzhendong on 16/5/27.
 */
public class AstExecutor {

    private BaseOperateExecutor operateExecutor = new BaseOperateExecutor();

    public void execute(AstNode root){
        //TODO 递归改为循环,可以提高效率,并且避免StackOverflow
        Assert.assertion(root != null, AstError.InvalidArgs,"root is null");
        Assert.assertion(root.nodeType != AstNode.NodeType.DATA, AstError.InvalidArgs,"root's nodeType must not be DATA");
        for(int i = 0; i < root.getChildSize(); i++){
            AstNode node = root.getChild(i);
            if(node.nodeType != AstNode.NodeType.DATA){
                execute(node);
            }
        }
        //需要先修正参数
        Object[] args = new Object[root.getChildSize()];
        BindingObject bindingObject = null;
        for(int i = 0; i < root.getChildSize(); i++) {
            if(bindingObject == null && root.getChild(i).dataType == AstNode.DataType.BINDING) {
                bindingObject = (BindingObject)root.getChild(i).originValue;
            }
            args[i] = root.getChild(i).get();
        }
        switch (root.nodeType){
            case OPERATE:
                root.setStatus(1);
                Operator operator = (Operator)root.originValue;
                OperateExecutor operateExecutor = getDefaultOperateExecutor();
                if(bindingObject != null){
                   operateExecutor = bindingObject.getOperateExecutor();
                }
                root.returnValue = operateExecutor.exeOperate(operator, args);
                break;
            case FUNC:
            case CONTROL:
                throw new AstException(AstError.UnsupportedOperation,"暂时不支持函数或者控制语句");
            default:
                throw new AstException(AstError.UnExpected,"不符合预期的节点类型" + root.nodeType.toString());

        }
    }

    public OperateExecutor getDefaultOperateExecutor(){
        return  operateExecutor;
    }
}
