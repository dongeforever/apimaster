package com.dova.apimaster.executor.ast.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by liuzhendong on 16/5/23.
 * 抽象语法树的节点
 */
public class AstNode {
    //DATA数据节点,操作节点
    public enum Type{
        DATA,OPERATE;
    }

    public AstNode(){

    }
    public AstNode(Type type){
        this.type = type;
    }
    public Object value;

    public AstNode.Type type;
    public int status;

    public AstNode first;
    public AstNode second;
    public AstNode third;
}
