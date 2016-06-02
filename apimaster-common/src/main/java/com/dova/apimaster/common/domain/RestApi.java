package com.dova.apimaster.common.domain;

import java.util.List;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class RestApi {
    public  enum RequestMethod {
        GET, POST, PUT, DELETE;
        public static RequestMethod of(String method){
            switch (method){
                case "GET":
                    return GET;
                case "POST":
                    return POST;
                case "PUT":
                    return PUT;
                case "DELETE":
                    return DELETE;
                default:
                    throw new UnsupportedOperationException("unknown http method:" + method);
            }
        }
    }
    public enum BodyType{
        FORM,TEXT,JSON;
    }
    private int id;
    private int projectId;
    private String name;
    private String url;
    private List<FieldDesc> requestFieldDescs; //请求相关的字段说明
    private List<FieldDesc> responseFieldDescs; //返回结果的字段说明
    private RequestMethod method;     // 请求函数 get,post,put,delete


    private List<Field> pathVariables; //
    private List<Header> headers;
    private BodyType requestBodyType; //form,text,json
    private String requestBody;   //
    private BodyType responseBodyType;   //json,text,xml
    private String responseBody;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<FieldDesc> getRequestFieldDescs() {
        return requestFieldDescs;
    }

    public void setRequestFieldDescs(List<FieldDesc> requestFieldDescs) {
        this.requestFieldDescs = requestFieldDescs;
    }

    public List<FieldDesc> getResponseFieldDescs() {
        return responseFieldDescs;
    }

    public void setResponseFieldDescs(List<FieldDesc> responseFieldDescs) {
        this.responseFieldDescs = responseFieldDescs;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
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

    public BodyType getRequestBodyType() {
        return requestBodyType;
    }

    public void setRequestBodyType(BodyType requestBodyType) {
        this.requestBodyType = requestBodyType;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public BodyType getResponseBodyType() {
        return responseBodyType;
    }

    public void setResponseBodyType(BodyType responseBodyType) {
        this.responseBodyType = responseBodyType;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
