package com.dova.apimaster.executor;

import com.dova.apimaster.common.domain.RestApi;
import com.dova.apimaster.common.domain.UnitCase;
import com.dova.apimaster.common.domain.UnitInject;
import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.ast.domain.ApiRes;
import com.dova.apimaster.executor.ast.domain.AssertResult;
import com.dova.apimaster.executor.ast.domain.Keyword;
import com.dova.apimaster.executor.ast.helper.UnitCaseHelper;
import com.dova.apimaster.executor.ast.impl.AstParseExecutor;
import com.dova.apimaster.executor.ast.impl.JsonBindingObject;
import com.dova.apimaster.executor.http.AssertExecutor;
import com.dova.apimaster.executor.http.HttpExecutor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Created by liuzhendong on 16/6/1.
 */
public class HttpAssertInjectExecutor {


    private Cache<Integer, ApiRes> apiResCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build();
    private AstParseExecutor astParseExecutor = new AstParseExecutor();
    private HttpExecutor httpExecutor = new HttpExecutor();
    private AssertExecutor assertExecutor = new AssertExecutor(astParseExecutor);

    public ApiRes getApiRes(int unitId){
        return apiResCache.getIfPresent(unitId);
    }

    public ApiRes execute(RestApi restApi){
       return execute(restApi, null ,false);
    }

    public ApiRes execute(RestApi restApi,UnitCase unitCase){
        return execute(restApi,unitCase,false);
    }

    public ApiRes execute(RestApi restApi, UnitCase unitCase, boolean inject){
        ApiRes apiRes = new ApiRes(unitCase == null ? 0 : unitCase.getId()).api(restApi);
        JsonNode response = null;
        AssertResult assertResult = null;
        try{
            if(unitCase != null && inject){
                try{
                    doInject(unitCase);
                }catch (Exception e){
                    throw new RuntimeException("Inject Error:"+e.getMessage());
                }
            }
            try{
                if(unitCase != null) restApi.fillParas(unitCase);
                response = httpExecutor.executeHttp(restApi);
            }catch (Exception e){
                throw new RuntimeException("ExecuteHttp Error:" + e.getMessage());
            }
            if(unitCase != null){
                try{
                    assertResult = assertExecutor.executeAssert(unitCase,response);
                }catch (Exception e){
                    throw new RuntimeException("Assert Error" + e.getMessage());
                }
            }
        }catch (Exception e){
            if(response == null){
                response = JSON.newObjectNode();
            }
            if(unitCase != null && assertResult == null){
                assertResult = new AssertResult();
                assertResult.asserts = unitCase.getAssertions().size();
                assertResult.errors = 1;
                assertResult.remark = e.getMessage();
            }
        }finally {
            apiRes.response(response).assertRes(assertResult);
        }
        if(unitCase != null){
            apiResCache.put(unitCase.getId(), apiRes);
        }
        return apiRes;
    }

    private void doInject(UnitCase unitCase){
        if(unitCase.getInjects() == null || unitCase.getInjects().size() == 0){
            return;
        }
        ObjectNode request = UnitCaseHelper.createRequestNode(unitCase);
        astParseExecutor.bindObject(new JsonBindingObject(Keyword.REQUEST, request));

        for(UnitInject unitInject : unitCase.getInjects()){
            if(Strings.isNullOrEmpty(unitInject.getInjectExp().getValue())){
                continue;
            }
            ApiRes apiRes = apiResCache.getIfPresent(unitInject.getFromUnitId());
            if(apiRes != null){
                astParseExecutor.bindObject(new JsonBindingObject(Keyword.RESPONSE, apiRes.response));
            }
            astParseExecutor.parseAndExecute(unitInject.getInjectExp().getValue());
        }

        unitCase.setPathVariables(request.get(Keyword.PATH));
        unitCase.setHeaders(request.get(Keyword.HEADER));
        unitCase.setRequestBody(request.get(Keyword.BODY));
    }


}
