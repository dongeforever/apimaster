package com.dova.apimaster.executor.test;

import com.dova.apimaster.common.util.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Test;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class JsonSetTest {

    @Test
    public void testSetJson()throws Exception{
        String json = "{\"id\":1}";
        ObjectNode root = (ObjectNode) JSON.of(json);
        root.put("id",12);
        JsonNode idNode = root.path("id");
        System.out.println(JSON.toJson(root));
    }


    @Test
    public void testJsonOrder()throws Exception{
        String json = "{\"id\":1}";
        ObjectNode root = (ObjectNode) JSON.of(json);
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

    @Test
    public void testThree(){
        StringBuilder sb = new StringBuilder("200");
        Object value = null;
        value = false ? (Double.valueOf("200")) : Integer.valueOf("100");
        System.out.println(String.format("%s %s",value.getClass(), value.toString()));

        if(false){
            value = Double.valueOf(sb.toString());
        }else {
            value = Integer.valueOf(sb.toString());
        }
        System.out.println(String.format("%s",value.getClass()));

        int a = false ? 2:3;
        System.out.println(a);

        Integer b = null;
        Integer c = 1 > 2 ? 0 : b;
    }

    @Test
    public void testReplace(){
        String origin = "http://localhost:8081/sellers/dishes/{dishId}";
        System.out.println(origin.replaceAll("\\{dishId\\}","4835"));
    }
}
