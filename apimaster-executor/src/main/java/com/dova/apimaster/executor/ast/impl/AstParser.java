package com.dova.apimaster.executor.ast.impl;

import com.dova.apimaster.executor.ast.domain.AstException;
import com.dova.apimaster.executor.ast.domain.AstNode;
import com.dova.apimaster.executor.ast.domain.AstError;
import com.dova.apimaster.executor.ast.domain.Operator;
import com.dova.apimaster.executor.ast.helper.Assert;
import com.dova.apimaster.executor.ast.helper.CharHelper;
import com.dova.apimaster.executor.ast.helper.PrintUtil;

import java.util.ArrayDeque;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class AstParser {

    private boolean debug = true;

    class ParseResult{
        int offset;
        AstNode opNode;
        AstNode valueNode;
        public ParseResult(){}
        public ParseResult(ParseResult other){
            this.offset = other.offset;
            this.opNode = other.opNode;
            this.valueNode = other.valueNode;
        }
    }
    public AstNode parse(String expression){
        return parse(expression, null);
    }
    public AstNode parse(String expression, Binding binding){
        int i = 0;
        int lastStatus = -1; //表示默认,0表示值,1表示操作符
        ArrayDeque<AstNode>  opStack = new ArrayDeque<>();
        ArrayDeque<AstNode>  valueStack = new ArrayDeque<>();
        int leftBracket=0,rightBracket = 0;
        for(;i < expression.length();){
            char c = expression.charAt(i);
            //空白符直接跳过,//TODO 有些操作符与值之间的空白符不能跳过,比如.
            if(CharHelper.isBlank(c)){
                i++;
                continue;
            }
            if(c == '('){
                leftBracket++;
            }else if(c == ')'){
                rightBracket++;
            }
            Assert.assertion(leftBracket>=rightBracket,AstError.UnExpected,"the left bracket num must gteq the right bracket while processing");
            //可能是操作符
            if(CharHelper.isOpStart(c)){
                ParseResult opRes = parseOperator(expression,i);
                print("parse Op succ start:%d offset:%d", i, opRes.offset);
                if(opRes.offset > 0){
                    lastStatus = checkParseResult(opRes, opStack, valueStack, binding, lastStatus,expression,i);
                    i += opRes.offset;
                    continue;
                }
            }

            //值
            ParseResult valueRes = parseValue(expression,i);
            print("parse Value succ start:%d offset:%d", i, valueRes.offset);
            if(valueRes.offset > 0){
                lastStatus = checkParseResult(valueRes, opStack, valueStack, binding, lastStatus, expression,i);
                i += valueRes.offset;
                continue;
            }
        }
        Assert.assertion(leftBracket==rightBracket,AstError.UnExpected,"the left bracket num must eq the right bracket while finished");

        finishConcat(opStack, valueStack,binding);
        Assert.assertion(opStack.size() == 0 && valueStack.size() == 1, AstError.UnExpected, "unexpected result opStack:%d valueStack:%d", opStack.size(), valueStack.size());
        return valueStack.pop();
    }

    /*
    private int labelBindingObject(ParseResult res,Binding binding){
        if(res.offset <= 0
                || res.opNode != null
                || res.valueNode == null
                || res.valueNode.dataType != AstNode.DataType.UNQUOTEDSTRING){
            return 0;
        }
        String name = (String)res.valueNode.originValue;
        if(binding.hasBindingObject(name)){
            AstNode astNode = new AstNode(AstNode.NodeType.DATA, AstNode.DataType.BINDING);
            astNode.originValue = binding.getBindingObject(name);
            res.valueNode = astNode;
        }
        return 0;
    }
    */

    private int checkParseResult(ParseResult res,ArrayDeque<AstNode> opStack, ArrayDeque<AstNode> valueStack, Binding binding,
                                 int lastStatus, String expression,int start){
        PrintUtil.print("check result lastStatus:%d op:%b originValue:%b opSize:%d valueSize:%d", lastStatus, res.opNode != null, res.valueNode != null,opStack.size(),valueStack.size());
        //检测操作符与值直接的关系是否符合规范
        if(lastStatus == -1){
            if(res.opNode != null && ((Operator)res.opNode.originValue) == Operator.BRACKET1) {
                //仍然可以接左括号
                opStack.push(res.opNode);
                return 1;
            }else {
                Assert.assertion(res.opNode == null && res.valueNode != null, AstError.ValueNoValue,"expression must not start with Operator index:%d char:%c", start, expression.charAt(start));
                valueStack.push(res.valueNode);
                return 0;
            }
        }
        if(lastStatus == 0){
            //上一个是值
            Assert.assertion(res.opNode != null && res.valueNode == null, AstError.ValueNoValue,"Value-No-Value index:%d char:%c", start, expression.charAt(start));
            Operator currOp = (Operator)res.opNode.originValue;
            //值后面不能接左括号
            Assert.assertion(currOp != Operator.BRACKET1,AstError.ValueNoLeftBracket,"Value-No-Left-Bracket index:%d char:%c",start,expression.charAt(start));
            if(currOp == Operator.BRACKET2){
                Assert.assertion(opStack.size() > 1 && (Operator)(opStack.peek().originValue)!=Operator.BRACKET1, AstError.UnExpected,"右括号前必须要有表达式");
                finishConcatUntilLeftBracket(opStack,valueStack,binding);
                return 0;
            }
            //TODO 单目运算符应该直接取值
            //循环比较
            while (opStack.peek() != null){
                print("opStack:%d valueStack:%d", opStack.size(), valueStack.size());
                Operator lastOp = (Operator)opStack.peek().originValue;
                //如果当前运算符优先级 > 操作符栈顶的操作符.priority越小,优先级越大;
                //或者前一个操作符是左括号
                if(lastOp == Operator.BRACKET1 || currOp.priority < lastOp.priority){
                    break;
                }
                //出栈并拼接值
                concatOperatorAndValue(opStack, valueStack, binding);
            }
            //入栈
            opStack.push(res.opNode);
            return 1;
        }else {
            //上一个是操作符
            if(res.opNode != null && ((Operator)res.opNode.originValue) == Operator.BRACKET1){
                //仍然可以接左括号
                opStack.push(res.opNode);
                return 1;
            }else {
                Assert.assertion(res.opNode == null && res.valueNode != null, AstError.OpNoOP, "Op-No-Op index:%d char:%c", start, expression.charAt(start));
                valueStack.push(res.valueNode);
                return 0;
            }
        }
    }

    private void finishConcat(ArrayDeque<AstNode> opStack, ArrayDeque<AstNode> valueStack,Binding binding){
        while (true){
            if(opStack.size() == 0) return;
            concatOperatorAndValue(opStack,valueStack,binding);
        }
    }

    private void finishConcatUntilLeftBracket(ArrayDeque<AstNode> opStack, ArrayDeque<AstNode> valueStack,Binding binding){
        while (true){
            if(opStack.size() == 0) return;
            Operator operator = (Operator) opStack.peek().originValue;
            if(operator == Operator.BRACKET1){
                opStack.pop();
                return;
            }
            concatOperatorAndValue(opStack,valueStack,binding);
        }
    }
    private void concatOperatorAndValue(ArrayDeque<AstNode> opStack, ArrayDeque<AstNode> valueStack,Binding binding){
        AstNode lastOpNode = opStack.pop();
        Operator lastOp = (Operator) lastOpNode.originValue;
        Assert.assertion(valueStack.size() >= lastOp.argNum, AstError.ValueNumError,"Value num is error op:%s required:%d actual:%d",
                lastOp.desc, lastOp.argNum,valueStack.size());

        //注意出栈的顺序
        for(int i = lastOp.argNum-1; i>=0;i--){
            AstNode child = valueStack.pop();
            if(binding != null && child.dataType == AstNode.DataType.UNQUOTEDSTRING){
                String name = (String)child.originValue;
                if(binding.hasBindingObject(name) && binding.getBindingObject(name).isKeyWord(lastOp,name,i+1)){
                    AstNode newChild = new AstNode(AstNode.NodeType.DATA, AstNode.DataType.BINDING);
                    newChild.originValue = binding.getBindingObject(name);
                    child = newChild;
                }
            }
            lastOpNode.setChild(i, child);
        }
        valueStack.push(lastOpNode);
    }


    private ParseResult parseOperator(String expression, int start){
        print("parse op index:%d char:%c",start, expression.charAt(start));
        ParseResult res = new ParseResult();
        int max = 0;
        Operator op = null;
        for(Operator tmp : Operator.values()){
            int n = 0;
            int i = 0, j = start;
            for (;i < tmp.desc.length() && j < expression.length();i++,j++){
                if(tmp.desc.charAt(i) == expression.charAt(j)){
                    n++;
                }else{
                    break;
                }
            }
            if(n != tmp.desc.length()){
                continue;
            }
            if(n > max){
                op = tmp;
            }
        }
        Assert.assertion(op != null, AstError.UnExpected,"parse op failed index:%d char:%c",start, expression.charAt(start));
        res.offset = op.desc.length();
        AstNode astNode = new AstNode(AstNode.NodeType.OPERATE, AstNode.DataType.NODATA);
        astNode.originValue = op;
        res.opNode = astNode;
        return res;
    }

    private ParseResult parseValue(String expression, int start){
        ParseResult res = new ParseResult();
        int p = start;
        StringBuilder sb = new StringBuilder();
        int digitNum = 0;
        boolean isQuoted = false;
        for(;p < expression.length();p++){
            char c = expression.charAt(p);
            print("p:%d char:%c",p,c);
            if(c == '\"'){
                if(isQuoted){
                    p++;
                    break;
                }else if(sb.length() == 0){
                    isQuoted = true;
                    continue;
                }else {
                    break;
                }
            }

            if(c=='\\'){
                c = expression.charAt(++p);
                switch (c){
                    case 'n':
                        c = '\n';
                        break;
                    case 'b':
                        c = '\b';
                        break;
                    case 't':
                        c = '\t';
                        break;
                    case 'r':
                        c ='\r';
                        break;
                    case '\"':
                        c = '\"';
                        break;
                    default:
                        throw new AstException(AstError.UnknownEscapeChar,"未知的转义符\\" + c);
                }
            }

            if(isQuoted){
                sb.append(c);
                continue;
            }
            //消除歧义
            if(c == '.' && digitNum == sb.length()){
                digitNum++;
                sb.append(c);
                continue;
            }
            if(CharHelper.isOpStart(c) || CharHelper.isBlank(c)){
                break;
            }
            if(CharHelper.isDigit(c)){
                digitNum++;
            }
            sb.append(c);
        }
        Object value = null;
        AstNode.DataType dataType = null;

        print("parse origin value:%s start:%d",sb.toString(),start);
        if(isQuoted){
            value = sb.toString();
            dataType = AstNode.DataType.QUOTEDSTRING;
        }else if(sb.length() == digitNum){
            if(sb.toString().contains(".")){
                value = Double.valueOf(sb.toString());
            }else {
                value = Integer.valueOf(sb.toString());
            }
            dataType = AstNode.DataType.NUMRIC;
        }else {
            value = sb.toString();
            dataType = AstNode.DataType.UNQUOTEDSTRING;
        }
        res.offset = p - start;
        AstNode astNode = new AstNode(AstNode.NodeType.DATA, dataType);
        astNode.originValue = value;
        res.valueNode = astNode;
        return res;
    }


    private void  print(String format,Object... args){
        if(debug){
            PrintUtil.print(format,args);
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
