package com.dova.apimaster.common.domain;

import java.util.List;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class UnitCase {

    private int id;
    private int restApiId;
    private int groupId;  // 默认为0表示没有分组
    private List<Expression> assertions;


    //来自RestApi表的冗余字段
    private List<Field> pathVariables;
    private List<Header> headers;
    private String requestBody;
    private String responseBody;

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

    public List<Field> getPathVariables() {
        return pathVariables;
    }

    public void setPathVariables(List<Field> pathVariables) {
        this.pathVariables = pathVariables;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
