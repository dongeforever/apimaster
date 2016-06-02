package com.dova.apimaster.executor.ast.domain;

/**
 * Created by liuzhendong on 16/5/23.
 * 抽象语法树的节点
 */
public class AstNode {
    //DATA数据节点,OPERATE操作节点,CONTROL控制语句,FUNC函数
    public enum NodeType {
        DATA,OPERATE,CONTROL,FUNC;
    }
    //
    public enum DataType{
        NODATA,NUMRIC,QUOTEDSTRING,UNQUOTEDSTRING,BINDING
    }
    public AstNode(NodeType nodeType,DataType dataType){
        this.nodeType = nodeType;
        this.dataType = dataType;
        init();
    }

    private void init(){
        switch (nodeType){
            case DATA:
                this.childs = null;
                break;
            case OPERATE:
                this.childs = new AstNode[3];
                break;
            case FUNC:
            case CONTROL:
                this.childs = new AstNode[10];
                break;
            default:
                this.childs = null;
        }
    }
    public final NodeType nodeType; //节点的类型
    public final DataType dataType; //如果是DATA节点,则有相应的数据类型

    public Object originValue;//该节点的原始值,根据节点类型而各不相同
    public Object returnValue;//该节点在执行操作后的中间值,如函数的返回值

    private int status;  //操作节点被执行过后,则status为1
    private AstNode[] childs;
    private int childSize;

    public void setChild(int index, AstNode child){
        if(childs[index] == null){
            childSize++;
        }else if(child == null){
            childSize--;
        }
        childs[index] = child;

    }
    public AstNode getChild(int index){
        return childs[index];
    }
    public Object get(){
        return status == 1 ? returnValue : originValue;
    }

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }

    public int getChildSize(){
        return this.childSize;
    }

    public AstNode[] getChilds(){
        return childs;
    }
}
