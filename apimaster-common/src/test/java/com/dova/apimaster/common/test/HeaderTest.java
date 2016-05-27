package com.dova.apimaster.common.test;

import com.dova.apimaster.common.domain.Field;
import com.dova.apimaster.common.domain.Header;
import com.dova.apimaster.common.util.JSON;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class HeaderTest {


    @Test
    public void testJsonForInheritFields()throws Exception{
        Field field = new Field();
        field.setDesc("test");
        field.setKey("parent");
        field.setValue("hehe");
        String json = JSON.toJson(field);
        System.out.println("field:" + json);
        Header header = JSON.safeRead(json, Header.class);
        String json2 = JSON.toJson(header);
        System.out.println("header:" + json2);
        Assert.assertTrue(header.getDesc().equals("test"));
        Assert.assertTrue(header.getKey().equals("parent"));
        Assert.assertTrue(header.getValue().equals("hehe"));


    }
}
