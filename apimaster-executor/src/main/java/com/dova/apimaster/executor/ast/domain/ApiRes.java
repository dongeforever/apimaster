package com.dova.apimaster.executor.ast.domain;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by liuzhendong on 16/6/2.
 */
public class ApiRes {
    public JsonNode response;
    public AssertResult assertRes;



    public ApiRes response(JsonNode response){
        this.response = response;
        return this;
    }
    public ApiRes assertRes(AssertResult assertRes){
        this.assertRes = assertRes;
        return this;
    }
}
