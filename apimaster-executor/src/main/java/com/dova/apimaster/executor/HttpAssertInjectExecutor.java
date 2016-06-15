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
        JsonNode response = httpExecutor.executeHttp(restApi);
        return  new ApiRes().api(restApi).response(response);
    }

    public ApiRes execute(RestApi restApi,UnitCase unitCase){
        return execute(restApi,unitCase,false);
    }

    public ApiRes execute(RestApi restApi, UnitCase unitCase, boolean inject){
        if(inject){
            doInject(unitCase);
        }
        restApi.fillParas(unitCase);
        JsonNode response = httpExecutor.executeHttp(restApi);
        AssertResult assertResult = assertExecutor.executeAssert(unitCase,response);
        ApiRes apiRes= new ApiRes(unitCase.getId()).api(restApi).response(response).assertRes(assertResult);
        apiResCache.put(unitCase.getId(), apiRes);
        return apiRes;
    }

    private void doInject(UnitCase unitCase){
        if(unitCase.getInjects() == null || unitCase.getInjects().size() == 0){
            return;
        }
        ObjectNode request = UnitCaseHelper.createRequestNode(unitCase);
        astParseExecutor.bindObject(new JsonBindingObject(Keyword.REQUEST, request));
        for(UnitInject unitInject : unitCase.getInjects()){
            if(unitInject.getFromUnitId() <= 0
                    || Strings.isNullOrEmpty(unitInject.getInjectExp().getValue())
                    || apiResCache.getIfPresent(unitInject.getFromUnitId()) == null){
                continue;
            }
            ApiRes apiRes = apiResCache.getIfPresent(unitInject.getFromUnitId());
            astParseExecutor.bindObject(new JsonBindingObject(Keyword.RESPONSE, apiRes.response));
            astParseExecutor.parseAndExecute(unitInject.getInjectExp().getValue());
        }

        unitCase.setPathVariables(request.get(Keyword.PATH));
        unitCase.setHeaders(request.get(Keyword.HEADER));
        unitCase.setRequestBody(request.get(Keyword.BODY));
    }


}
