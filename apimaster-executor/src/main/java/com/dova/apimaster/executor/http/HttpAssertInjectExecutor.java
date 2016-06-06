package com.dova.apimaster.executor.http;

import com.dova.apimaster.common.domain.Expression;
import com.dova.apimaster.common.domain.RestApi;
import com.dova.apimaster.common.domain.UnitCase;
import com.dova.apimaster.common.domain.UnitInject;
import com.dova.apimaster.common.util.HttpClientFactory;
import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.ast.domain.ApiRes;
import com.dova.apimaster.executor.ast.domain.AssertResult;
import com.dova.apimaster.executor.ast.domain.AstNode;
import com.dova.apimaster.executor.ast.helper.Assert;
import com.dova.apimaster.executor.ast.impl.AstParseExecutor;
import com.dova.apimaster.executor.ast.impl.JsonBindingObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by liuzhendong on 16/6/1.
 */
public class HttpAssertInjectExecutor {

    private AstParseExecutor astParseExecutor = new AstParseExecutor();

    private HttpExecutor httpExecutor = new HttpExecutor();
    private AssertExecutor assertExecutor = new AssertExecutor(astParseExecutor);
    private InjectExecutor injectExecutor = new InjectExecutor(astParseExecutor);


    public ApiRes execute(RestApi restApi){
        JsonNode res = httpExecutor.executeHttp(restApi);
        return  new ApiRes().response(res);
    }

    public ApiRes execute(RestApi restApi,UnitCase unitCase){
        JsonNode res = httpExecutor.executeHttp(restApi);
        AssertResult assertResult = assertExecutor.executeAssert(unitCase,res);
        return new ApiRes().response(res).assertRes(assertResult);
    }

    public ApiRes execute(RestApi restApi, UnitCase unitCase, List<UnitInject> unitInjects){

        JsonNode res = httpExecutor.executeHttp(restApi);
        AssertResult result = assertExecutor.executeAssert(unitCase,res);
        if(result.errors != 0 || result.fails != 0){
            return new ApiRes().response(res).assertRes(result);
        }
        //TODO inject
        return null;
    }



}
