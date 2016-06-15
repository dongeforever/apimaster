package com.dova.apimaster.executor.http;

import com.dova.apimaster.common.domain.RestApi;
import com.dova.apimaster.common.util.HttpClientFactory;
import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.ast.domain.Keyword;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Iterator;
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
            if(entity != null) text = EntityUtils.toString(entity);
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
                responseNode.set(Keyword.BODY, JSON.of(text));
            }else {
                responseNode.put(Keyword.BODY,text);
            }
        }catch (Exception e){
            exception = e.getMessage();
            responseNode.put(Keyword.BODY, text);
        }
        responseNode.put("status",status);
        responseNode.put("exception",exception);
        return responseNode;

    }

    private String injectPathVariables(String originUrl, JsonNode pathVariables){
        if(pathVariables == null || pathVariables.size() == 0){
            return originUrl;
        }
        Iterator<String> it = pathVariables.fieldNames();
        while (it.hasNext()){
            String key = it.next();
            String value = pathVariables.get(key).asText();
            originUrl = originUrl.replaceAll(String.format("\\{%s\\}",key), value);
        }
        return originUrl;
    }


    private RequestBuilder addHeaders(RequestBuilder buider, RestApi restApi){
        if(restApi.getHeaders() == null || restApi.getHeaders().size() == 0) return buider;
        Iterator<String> it = restApi.getHeaders().fieldNames();
        while (it.hasNext()){
            String key = it.next();
            String value = restApi.getHeaders().get(key).asText();
            buider.addHeader(key, value);
        }
        return buider;
    }


    private RequestBuilder addHttpEntity(RequestBuilder buider, RestApi restApi)throws Exception{
        if(restApi.getRequestBody() == null || restApi.getRequestBody().size()==0){
            return buider;
        }
        HttpEntity entity = null;
        if(restApi.getRequestBodyType() == RestApi.BodyType.FORM){
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            Iterator<String> it = restApi.getRequestBody().fieldNames();
            while (it.hasNext()){
                String key = it.next();
                String value = restApi.getRequestBody().get(key).asText();
                data.add(new BasicNameValuePair(key, value));
            }
            entity = new UrlEncodedFormEntity(data, "UTF-8");
        }else {
            if(restApi.getRequestBodyType() == RestApi.BodyType.JSON){
                entity = new StringEntity(JSON.uncheckedToJson(restApi.getRequestBody()), ContentType.APPLICATION_JSON);
            }else {
                entity = new StringEntity(JSON.uncheckedToJson(restApi.getRequestBody()),"utf-8");
            }
        }
        return  buider.setEntity(entity);
    }
}
