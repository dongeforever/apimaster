package com.dova.apimaster.executor.test;

import com.dova.apimaster.common.domain.Expression;
import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.ast.BindingObject;
import com.dova.apimaster.executor.ast.OperateExecutor;
import com.dova.apimaster.executor.ast.domain.AstNode;
import com.dova.apimaster.executor.ast.domain.Operator;
import com.dova.apimaster.executor.ast.helper.CharHelper;
import com.dova.apimaster.executor.ast.helper.PrintUtil;
import com.dova.apimaster.executor.ast.impl.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by liuzhendong on 16/5/27.
 */
public class AstTest {
    AstParser astParser = new AstParser();
    AstExecutor astExecutor = new AstExecutor();

    ObjectNode root = JSON.newObjectNode();
    {
        root.put("status","200");
        root.put("text","this is the repsonse");
    }

    BindingObject bindingObject = new BindingObject<JsonNode>("response",root) {
        BaseOperateExecutor baseOperateExecutor = new BaseOperateExecutor();
        @JsonIgnore
        @Override
        public OperateExecutor getOperateExecutor() {
            return baseOperateExecutor;
        }

        @JsonIgnore
        @Override
        public boolean isKeyWord(Operator op, String name, int index) {
            if(op == Operator.AT && index == 1){
                return true;
            }
            return false;
        }


    };
    @Test
    public void parseNumricExpression()throws Exception{
        System.out.println(CharHelper.getOpStart());
        AstNode root = astParser.parse("(1+2)*3+4 == 13 && (1>1+1 || 1+2 >=3) || (12+1)>1");
        astExecutor.execute(root);
        System.out.println(JSON.toJson(root));
        System.out.println(root.get());
    }


    @Test
    public void testAstParse()throws Exception{
        Binding binding = new Binding();
        binding.bindObject(bindingObject);
        AstNode root = astParser.parse("response.status = 100", binding);
        System.out.println(JSON.toJson(root));
        astExecutor.execute(root);
        System.out.println(root.get());
        System.out.println(JSON.uncheckedToJson(bindingObject.getObject()));
    }


    @Test
    public void testPerformance(){
        PrintUtil.debug = false;
        AstParseExecutor astParseExecutor = new AstParseExecutor();
        ObjectNode root = JSON.newObjectNode();
        root.put("status",200);
        ObjectNode text = root.putObject("text");
        text.put("id",123);
        text.put("name","lzd");
        BindingObject bindingObject = new JsonBindingObject("response", root);
        astParseExecutor.bindObject(bindingObject);
        List<String> asserts = new ArrayList<>();
        asserts.add("response.status == 200 && (1+2)*3+4 == 13 && (1>1+1 || 1+2 >=3)");
        long start = System.currentTimeMillis();
        int num  = 10000;
        for (int i=0;i < num;i++){
            astParseExecutor.parseAndExecute(asserts.get(0));
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d cost:%d", num,end-start));
    }



}
