package com.dova.apimaster.executor.test;

import com.dova.apimaster.common.domain.Expression;
import com.dova.apimaster.common.domain.RestApi;
import com.dova.apimaster.common.domain.RestCaseSummary;
import com.dova.apimaster.common.domain.UnitCase;
import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.RestCaseTool;
import com.dova.apimaster.executor.ast.helper.PrintUtil;
import com.google.common.collect.Lists;
import org.junit.Test;

/**
 * Created by liuzhendong on 16/6/11.
 */
public class RestSummaryTest {


    @Test
    public void testRestSummary(){
        RestCaseSummary summary = new RestCaseSummary();
        summary.caseId = 1;
        summary.method = RestApi.RequestMethod.GET;
        summary.url = "http://localhost:8081/sellers/dishes/{dishId}";
        summary.requestBodyType = RestApi.BodyType.JSON;
        summary.headers = JSON.newObjectNode().put("authorization","lzzdong");
        summary.pathVariables = JSON.newObjectNode().put("dishId",4835);
        summary.assertions = Lists.newArrayList(new Expression().value("response.status / 100 == 2").name("status").type(Expression.Type.JSON));
        System.out.println(JSON.uncheckedToJson(summary));
    }

    @Test
    public void testRun()throws Exception{
        RestCaseTool.runFromStream(this.getClass().getClassLoader().getResourceAsStream("example.conf"),false);
    }

    @Test
    public void testRunExpress()throws Exception{
        RestCaseTool.runFromStream(this.getClass().getClassLoader().getResourceAsStream("express.conf"),true);

    }
}
