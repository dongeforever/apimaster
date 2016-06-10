package com.dova.apimaster.common.domain;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class UnitCase {

    private int id;
    private int restApiId;
    private int groupId;  // 默认为0表示没有分组


    //来自RestApi表的冗余字段
    private JsonNode pathVariables;
    private JsonNode headers;
    private JsonNode requestBody;
    private JsonNode responseBody;
    //注入
    private List<UnitInject>  injects;
    //校验
    private List<Expression> assertions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestApiId() {
        return restApiId;
    }

    public void setRestApiId(int restApiId) {
        this.restApiId = restApiId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<Expression> getAssertions() {
        return assertions;
    }

    public void setAssertions(List<Expression> assertions) {
        this.assertions = assertions;
    }

    public JsonNode getPathVariables() {
        return pathVariables;
    }

    public void setPathVariables(JsonNode pathVariables) {
        this.pathVariables = pathVariables;
    }

    public JsonNode getHeaders() {
        return headers;
    }

    public void setHeaders(JsonNode headers) {
        this.headers = headers;
    }

    public JsonNode getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(JsonNode requestBody) {
        this.requestBody = requestBody;
    }

    public JsonNode getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(JsonNode responseBody) {
        this.responseBody = responseBody;
    }

    public List<UnitInject> getInjects() {
        return injects;
    }

    public void setInjects(List<UnitInject> injects) {
        this.injects = injects;
    }
}
