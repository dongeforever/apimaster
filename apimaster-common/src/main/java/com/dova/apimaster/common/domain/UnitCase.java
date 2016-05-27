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
}
