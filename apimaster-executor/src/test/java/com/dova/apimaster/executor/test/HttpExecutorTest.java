package com.dova.apimaster.executor.test;

import com.dova.apimaster.common.domain.Expression;
import com.dova.apimaster.common.domain.RestApi;
import com.dova.apimaster.common.domain.UnitCase;
import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.ast.domain.ApiRes;
import com.dova.apimaster.executor.ast.helper.PrintUtil;
import com.dova.apimaster.executor.http.HttpAssertExecutor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhendong on 16/6/2.
 */
public class HttpExecutorTest {

    HttpAssertExecutor assertExecutor = new HttpAssertExecutor();
    {
        PrintUtil.debug = false;
    }

    @Test
    public void testGet()throws Exception{
        RestApi restApi = new RestApi();
        restApi.setUrl("http://localhost:8081/sellers/dishes/4835");
        restApi.setMethod(RestApi.RequestMethod.GET);
        restApi.setResponseBodyType(RestApi.BodyType.JSON);
        UnitCase unitCase = new UnitCase();
        List<Expression>  asserts = new ArrayList<>();
        asserts.add(new Expression().value("response.status / 100 == 2"));
        asserts.add(new Expression().value("response.text.id == 4835"));
        asserts.add(new Expression().value("response.text.name == chaojidan"));
        asserts.add(new Expression().value("response.text.price == 100 || response.text.price == 20"));

        unitCase.setAssertions(asserts);
        long start = System.currentTimeMillis();
        for(int i = 0; i< 1;i++){
            ApiRes apiRes = assertExecutor.execute(restApi, unitCase);
            System.out.println(JSON.toJson(apiRes.response));
            System.out.println(JSON.toJson(apiRes.assertRes));
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
