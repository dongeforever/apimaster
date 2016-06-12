package com.dova.apimaster.executor.test;

import com.dova.apimaster.common.domain.Expression;
import com.dova.apimaster.common.domain.RestApi;
import com.dova.apimaster.common.domain.UnitCase;
import com.dova.apimaster.common.domain.UnitInject;
import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.ast.domain.ApiRes;
import com.dova.apimaster.executor.ast.helper.PrintUtil;
import com.dova.apimaster.executor.HttpAssertInjectExecutor;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * Created by liuzhendong on 16/6/2.
 */
public class HttpExecutorTest {

    HttpAssertInjectExecutor assertExecutor = new HttpAssertInjectExecutor();
    {
        PrintUtil.debug = false;
    }

    @Test
    public void testGet()throws Exception{
        RestApi restApi = new RestApi();
        restApi.setUrl("http://localhost:8081/sellers/dishes/{dishId}");
        restApi.setMethod(RestApi.RequestMethod.GET);
        restApi.setResponseBodyType(RestApi.BodyType.JSON);
        UnitCase unitCase1 = new UnitCase();
        unitCase1.setId(1);
        unitCase1.setPathVariables(JSON.newObjectNode().put("dishId",4835));
        unitCase1.setAssertions(Lists.newArrayList(new Expression().value("response.status / 100 == 2")));

        UnitCase unitCase2 = new UnitCase();
        unitCase2.setId(2);
        unitCase2.setPathVariables(JSON.newObjectNode().put("dishId",4832));
        unitCase2.setAssertions(Lists.newArrayList(new Expression().value("response.status / 100 == 2")));
        unitCase2.setInjects(Lists.newArrayList(new UnitInject().fromUnitId(1).injectExp(new Expression().value("request.path.dishId = response.body.id"))));

        List<UnitCase> unitCases = Lists.newArrayList();
        unitCases.add(unitCase1);
        unitCases.add(unitCase2);

       for (UnitCase unitCase : unitCases){
           System.out.println("=====================================");
           ApiRes apiRes = assertExecutor.execute(restApi, unitCase, true);
           System.out.println(JSON.toJson(apiRes.unitId));
           System.out.println(JSON.toJson(apiRes.restApi));
           System.out.println(JSON.toJson(apiRes.response));
           System.out.println(JSON.toJson(apiRes.assertRes));
       }
    }
}
