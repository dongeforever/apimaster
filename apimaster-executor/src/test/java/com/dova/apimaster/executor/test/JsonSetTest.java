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
    }


    @Test
    public void testJsonOrder()throws Exception{
        String json = "{\"id\":1}";
        ObjectNode root = JSON.of(json);
        root.put("23",12);
        root.put("11",11);
        root.put("12",11);
        root.put("13",11);
        root.put("14",11);
        root.put("15",11);
        root.put("16",11);

        System.out.println(JSON.toJson(root));
    }

    @Test
    public void testToString(){
        Integer a = new Integer(123);
        Double b = new Double(1231.34);
        System.out.println(a.toString());
        System.out.println(b.toString());
    }
}
