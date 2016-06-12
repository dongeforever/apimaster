package com.dova.apimaster.common.domain;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Created by liuzhendong on 16/6/11.
 */
public class RestCaseSummary {
    public int caseId;
    public RestApi.RequestMethod method;     // 请求函数 get,post,put,delete
    public String url;

    public JsonNode pathVariables; //
    public JsonNode headers;
    public RestApi.BodyType requestBodyType; //form,text,json
    public JsonNode requestBody;

    //校验
    public List<Expression> assertions;
    //注入
    public List<UnitInject>  injects;



    public RestApi asRestApi(){
        RestApi restApi = new RestApi();
        restApi.setMethod(method);
        restApi.setUrl(url);
        restApi.setPathVariables(pathVariables);
        restApi.setHeaders(headers);
        restApi.setRequestBodyType(requestBodyType);
        restApi.setRequestBody(requestBody);
        return  restApi;
    }


    public UnitCase asUnitCase(){
        UnitCase unitCase = new UnitCase();
        unitCase.setId(caseId);
        unitCase.setPathVariables(pathVariables);
        unitCase.setHeaders(headers);
        unitCase.setRequestBody(requestBody);
        unitCase.setAssertions(assertions);
        unitCase.setInjects(injects);
        return unitCase;
    }

}
