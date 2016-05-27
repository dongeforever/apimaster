package com.dova.apimaster.common.domain;

import java.util.List;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class RestApi {
    public enum RequestMethod {
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

}
