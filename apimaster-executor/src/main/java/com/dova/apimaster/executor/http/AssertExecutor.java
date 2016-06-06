package com.dova.apimaster.executor.http;

import com.dova.apimaster.common.domain.Expression;
import com.dova.apimaster.common.domain.UnitCase;
import com.dova.apimaster.executor.ast.domain.AssertResult;
import com.dova.apimaster.executor.ast.domain.AstNode;
import com.dova.apimaster.executor.ast.impl.AstParseExecutor;
import com.dova.apimaster.executor.ast.impl.JsonBindingObject;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by liuzhendong on 16/6/6.
 */
public class AssertExecutor {

    private AstParseExecutor astParseExecutor;
    public AssertExecutor(AstParseExecutor astParseExecutor){
        this.astParseExecutor = astParseExecutor;
    }
    public AssertResult executeAssert(UnitCase unitCase, JsonNode response){
        astParseExecutor.bindObject(new JsonBindingObject("response",response));
        AssertResult assertResult = new AssertResult();
        assertResult.asserts = unitCase.getAssertions().size();

        if(response.path("status").asInt() == 0){
            assertResult.errors = 1;
            assertResult.remark = response.path("exception").asText();
            return assertResult;
        }
        StringBuilder remark = new StringBuilder();
        for(int i = 0;i < unitCase.getAssertions().size();i++){
            Expression expression = unitCase.getAssertions().get(i);
            try {
                AstNode astNode = astParseExecutor.parseAndExecute(expression.getValue());
                Boolean bool = (Boolean)astNode.get();
                if(!bool){
                    assertResult.fails++;
                    remark.append(String.format("%d{%s}\t",i,expression.getValue()));
                }
            }catch (Exception e){
                assertResult.errors++;
                remark.append(String.format("%d{%s}\t",i,e.getMessage()));
            }
        }
        assertResult.remark = remark.toString();
        return assertResult;
    }
}
