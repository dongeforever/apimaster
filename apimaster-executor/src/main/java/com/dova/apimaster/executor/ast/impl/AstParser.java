package com.dova.apimaster.executor.ast.impl;

import com.dova.apimaster.executor.ast.domain.AstNode;
import com.dova.apimaster.executor.ast.domain.AstParseError;
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
    }
    public AstNode parse(String expression){
        int i = 0;
        int lastStatus = -1; //表示默认,0表示值,1表示操作符
        ArrayDeque<AstNode>  opStack = new ArrayDeque<>();
        ArrayDeque<AstNode>  valueStack = new ArrayDeque<>();
        for(;i < expression.length();){
            char c = expression.charAt(i);
            //空白符直接跳过,//TODO 有些操作符与值之间的空白符不能跳过,比如.
            if(CharHelper.isBlank(c)){
                i++;
                continue;
            }
            //可能是操作符
            if(CharHelper.isOpStart(c)){
                AstNode node = new AstNode();
                ParseResult opRes = parseOperator(expression,i);
                print("parse Op succ start:%d offset:%d", i, opRes.offset);
                if(opRes.offset > 0){
                    lastStatus = checkParseResult(opRes, opStack, valueStack, lastStatus,expression,i);
                    i += opRes.offset;
                    continue;
                }
            }

            //值
            ParseResult valueRes = parseValue(expression,i);
            print("parse Value succ start:%d offset:%d", i, valueRes.offset);
            if(valueRes.offset > 0){
                lastStatus = checkParseResult(valueRes, opStack, valueStack, lastStatus, expression,i);
                i += valueRes.offset;

                continue;
            }
        }
        finishConcat(opStack, valueStack);
        Assert.assertion(opStack.size() == 0 && valueStack.size() == 1, AstParseError.UnExpected, "unexpected result opStack:%d valueStack:%d", opStack.size(), valueStack.size());
        return valueStack.pop();
    }

    private int checkParseResult(ParseResult res,ArrayDeque<AstNode> opStack, ArrayDeque<AstNode> valueStack, int lastStatus, String expression,int start){
        //检测操作符与值直接的关系是否符合规范
        if(lastStatus == -1){
            Assert.assertion(res.opNode == null && res.valueNode != null, AstParseError.ValueNoValue,"expression must start with Value index:%d char:%c", start, expression.charAt(start));
            valueStack.push(res.valueNode);
            return 0;
        }
        PrintUtil.print("check result lastStatus:%d op:%b value:%b", lastStatus, res.opNode != null, res.valueNode != null);
        if(lastStatus == 0){
            //上一个是值
            Assert.assertion(res.opNode != null && res.valueNode == null, AstParseError.ValueNoValue,"Value-No-Value index:%d char:%c", start, expression.charAt(start));

            if(opStack.size() == 0){
                opStack.push(res.opNode);
                return 1;
            }
            Operator currOp = (Operator)res.opNode.value;
            //TODO 单目运算符应该直接取值
            //非单目
            while (true){
                print("opStack:%d valueStack:%d", opStack.size(), valueStack.size());
                Operator lastOp = (Operator)opStack.peek().value;
                //如果当前运算符优先级 > 操作符栈顶的操作符.priority越小,优先级越大
                if(currOp.priority < lastOp.priority){
                    opStack.push(res.opNode);
                    return 1;
                }
                //如果当前运算符优先级 <= 操作符栈顶的操作符, 则出栈并拼接值,直至操作符栈为空
                concatOperatorAndValue(opStack, valueStack);
                if(opStack.size() == 0){
                    opStack.push(res.opNode);
                    return 1;
                }
            }
        }else {
            //上一个是操作符
            Assert.assertion(res.opNode == null && res.valueNode != null,AstParseError.OpNoOP, "Op-No-Op index:%d char:%c", start, expression.charAt(start));
            valueStack.push(res.valueNode);
            return 0;
        }
    }

    private void finishConcat(ArrayDeque<AstNode> opStack, ArrayDeque<AstNode> valueStack){
        while (true){
            if(opStack.size() == 0) return;
            concatOperatorAndValue(opStack,valueStack);
        }
    }
    private void concatOperatorAndValue(ArrayDeque<AstNode> opStack, ArrayDeque<AstNode> valueStack){
        AstNode lastOpNode = opStack.pop();
        Operator lastOp = (Operator) lastOpNode.value;
        Assert.assertion(valueStack.size() >= lastOp.argNum, AstParseError.ValueNumError,"Value num is error op:%s required:%d actual:%d",
                lastOp.desc, lastOp.argNum,valueStack.size());
        if(lastOp.argNum >= 1) lastOpNode.first = valueStack.pop();
        if(lastOp.argNum >= 2) lastOpNode.second = valueStack.pop();
        if(lastOp.argNum >= 3) lastOpNode.third = valueStack.pop();
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
        Assert.assertion(op != null, AstParseError.UnExpected,"parse op failed index:%d char:%c",start, expression.charAt(start));
        res.offset = op.desc.length();
        AstNode astNode = new AstNode(AstNode.Type.OPERATE);
        astNode.value = op;
        res.opNode = astNode;
        return res;
    }

    private ParseResult parseValue(String expression, int start){
        print("parse value index:%d char:%c",start, expression.charAt(start));
        ParseResult res = new ParseResult();
        int p = start;
        StringBuilder sb = new StringBuilder();
        int digitNum = 0;
        for(;p < expression.length();p++){
            char c = expression.charAt(p);
            print("p:%d char:%c",p,c);
            if(CharHelper.isOpStart(c)){
                break;
            }
            if(CharHelper.isDigit(c)){
                digitNum++;
            }
            sb.append(c);
        }
        Object value = null;
        if(sb.length() == digitNum){
            value = Integer.valueOf(sb.toString());
        }else {
            value = sb.toString();
        }
        res.offset = sb.length();
        AstNode astNode = new AstNode(AstNode.Type.DATA);
        astNode.value = value;
        res.valueNode = astNode;
        return res;
    }


    private void  print(String format,Object... args){
        if(debug){
            PrintUtil.print(format,args);
        }
    }
}
