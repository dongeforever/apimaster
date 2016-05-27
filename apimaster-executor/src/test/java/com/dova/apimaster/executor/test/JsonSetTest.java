package com.dova.apimaster.executor.test;

import com.dova.apimaster.common.util.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class JsonSetTest {

    @Test
    public void testSetJson()throws Exception{
        String json = "{\"id\":1}";
        ObjectNode root = JSON.of(json);
        root.put("id",12);
        System.out.println(JSON.toJson(root));
        System.out.println(Integer.MAX_VALUE);
    }
}
