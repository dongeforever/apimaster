package com.dova.apimaster.executor.http;

import com.dova.apimaster.common.domain.Field;
import com.dova.apimaster.common.domain.Header;
import com.dova.apimaster.common.domain.RestApi;
import com.dova.apimaster.common.util.HttpClientFactory;
import com.dova.apimaster.common.util.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhendong on 16/6/6.
 */
public class HttpExecutor {

    private HttpClient client = HttpClientFactory.get();

    public JsonNode executeHttp(RestApi restApi){
        String url = injectPathVariables(restApi.getUrl(), restApi.getPathVariables());
        RequestBuilder builder = null;
        switch (restApi.getMethod()){
            case GET:
                builder = RequestBuilder.get(url);
                break;
            case POST:
                builder = RequestBuilder.post(url);
                break;
            case PUT:
                builder = RequestBuilder.put(url);
                break;
            case DELETE:
                builder = RequestBuilder.delete(url);
                break;
            default:
                throw  new RuntimeException(String.format("unknown method apiId:%s method:%s",restApi.getId(),restApi.getMethod()));
        }
        HttpEntity entity = null;
        int status = 0;
        String text = "";
        String exception = "";
        try {
            addHeaders(builder, restApi);
            addHttpEntity(builder, restApi);
            HttpUriRequest request = builder.build();
            HttpResponse response = client.execute(request);
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
        ObjectNode responseNode  = JSON.newObjectNode();
        try{
            if(restApi.getResponseBodyType() == RestApi.BodyType.JSON && text.length() > 0){
                responseNode.set("text", JSON.of(text));
            }else {
                responseNode.put("text",text);
            }
        }catch (Exception e){
            exception = e.getMessage();
            responseNode.put("text", text);
        }
        responseNode.put("status",status);
        responseNode.put("exception",exception);
        return responseNode;

    }

    private String injectPathVariables(String originUrl, List<Field> pathVariables){
        if(pathVariables == null || pathVariables.size() == 0){
            return originUrl;
        }
        for (Field field: pathVariables){
            originUrl.replaceAll(String.format("{%s}",field.getKey()),field.getValue());
        }
        return originUrl;
    }


    private RequestBuilder addHeaders(RequestBuilder buider, RestApi restApi){
        if(restApi.getHeaders() == null) return buider;
        for (Header header : restApi.getHeaders()){
            buider.addHeader(header.getKey(), header.getValue());
        }
        return buider;
    }


    private RequestBuilder addHttpEntity(RequestBuilder buider, RestApi restApi)throws Exception{
        if(Strings.isNullOrEmpty(restApi.getRequestBody())){
            return buider;
        }
        HttpEntity entity = null;
        if(restApi.getRequestBodyType() == RestApi.BodyType.FORM){
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            List<Field>  paras = JSON.safeReadList(restApi.getRequestBody(), Field.class);
            for(Field field : paras){
                data.add(new BasicNameValuePair(field.getKey(), field.getValue()));
            }
            entity = new UrlEncodedFormEntity(data, "UTF-8");
        }else {
            entity = new StringEntity(restApi.getRequestBody(),"utf-8");
            if(restApi.getRequestBodyType() == RestApi.BodyType.JSON){
                buider.addHeader("Content-Type","application/json");
            }
        }
        return  buider.setEntity(entity);
    }
}
