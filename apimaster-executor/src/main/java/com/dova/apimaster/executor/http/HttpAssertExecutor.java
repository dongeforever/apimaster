package com.dova.apimaster.executor.http;

import com.dova.apimaster.common.domain.Expression;
import com.dova.apimaster.common.domain.RestApi;
import com.dova.apimaster.common.domain.UnitCase;
import com.dova.apimaster.common.util.HttpClientFactory;
import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.ast.domain.ApiRes;
import com.dova.apimaster.executor.ast.domain.AssertResult;
import com.dova.apimaster.executor.ast.domain.AstNode;
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

/**
 * Created by liuzhendong on 16/6/1.
 */
public class HttpAssertExecutor {

    private HttpClient client = HttpClientFactory.get();


    private AstParseExecutor astParseExecutor = new AstParseExecutor();

    public ApiRes execute(RestApi restApi, UnitCase unitCase)throws Exception{
        RestApi.RequestMethod requestMethod = restApi.getMethod();
        switch (requestMethod){
            case GET:
                JsonNode res = executeGet(restApi);
                AssertResult assertResult = asserts(unitCase, res);
                ApiRes apiRes = new ApiRes();
                apiRes.response = res;
                apiRes.assertRes = assertResult;
                return apiRes;
            case POST:
                return null;
            case PUT:
                return null;
            default:
            throw new UnsupportedOperationException("unsopported method" + requestMethod);
        }
    }


    public JsonNode executeGet(RestApi restApi)throws Exception{
        HttpUriRequest request = RequestBuilder.get(restApi.getUrl()).setConfig(HttpClientFactory.getDefaultRequestConfig())
                .build();
        HttpResponse response = null;
        HttpEntity entity = null;
        int status = 0;
        String text = "";
        String exception = "";
        try {
            response = client.execute(request);
            status = response.getStatusLine().getStatusCode();
            entity = response.getEntity();
            text = EntityUtils.toString(entity);
        }catch (Exception e){
            exception = e.getMessage();
        }finally {
            if(entity != null){
                EntityUtils.consumeQuietly(entity);
            }
        }
        ObjectNode responseNode  = null;
        if(restApi.getResponseBodyType() == RestApi.BodyType.JSON
                && text.length() > 0){
            String tmp = String.format("{\"text\":%s}",
                    text);
            responseNode = JSON.of(tmp);
        }else {
            responseNode = JSON.newObjectNode();
            responseNode.put("text", (String) text);
        }
        responseNode.put("status",status);
        responseNode.put("exception",exception);
        return responseNode;

    }


    public AssertResult asserts(UnitCase unitCase, JsonNode response){
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
