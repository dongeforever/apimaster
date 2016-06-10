package com.dova.apimaster.executor.ast.domain;

import com.dova.apimaster.common.domain.RestApi;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by liuzhendong on 16/6/2.
 */
public class ApiRes {
    public int unitId;
    public RestApi restApi;
    public JsonNode response;
    public AssertResult assertRes;

    public ApiRes(){}
    public ApiRes(int unitId){
        this.unitId = unitId;
    }
    public ApiRes unitId(int unitId){
        this.unitId = unitId;
        return this;
    }
    public ApiRes api(RestApi restApi){
        this.restApi = restApi;
        return this;
    }
    public ApiRes response(JsonNode response){
        this.response = response;
        return this;
    }
    public ApiRes assertRes(AssertResult assertRes){
        this.assertRes = assertRes;
        return this;
    }
}
