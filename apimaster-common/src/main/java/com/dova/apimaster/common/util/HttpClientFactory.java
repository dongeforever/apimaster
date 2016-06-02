package com.dova.apimaster.common.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

/**
 * Created by liuzhendong on 16/5/14.
 */
public class HttpClientFactory {
    public final static int CONNECTION_TIMEOUT = 3 * 1000;
    private static HttpClient defaultClient;

    public synchronized static HttpClient get() {
        if (defaultClient == null) {
            defaultClient = HttpClientBuilder.create()
                    .setUserAgent("Home-Cooked HTTP Client")
                    .setRetryHandler(new StandardHttpRequestRetryHandler(3, true))  //IOException的重试策略
                    .setServiceUnavailableRetryStrategy(new DefaultServiceUnavailableRetryStrategy())  //有返回结果的重试策略
                    .setMaxConnPerRoute(32)
                    .setMaxConnTotal(256)
                    .build();
        }
        return defaultClient;
    }

    static RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
            .setConnectTimeout(CONNECTION_TIMEOUT)
            .setSocketTimeout(CONNECTION_TIMEOUT)
            .build();

    public static RequestConfig getDefaultRequestConfig() {
        return requestConfig;
    }
}

class DefaultServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy {
    public final static int MAX_RETRIES    = 3; //重试次数
    public final static int RETRY_INTERVAL = 1000; //重试间隔

    @Override
    public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
        return executionCount < MAX_RETRIES &&
                response.getStatusLine().getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }

    @Override
    public long getRetryInterval() {
        return RETRY_INTERVAL;
    }
}