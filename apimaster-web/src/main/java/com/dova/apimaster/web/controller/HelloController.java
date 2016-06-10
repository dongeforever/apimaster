package com.dova.apimaster.web.controller;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by liuzhendong on 16/6/10.
 */
@Controller
public class HelloController {

    @RequestMapping("hello")
    public ResponseEntity hello(){
        return ResponseEntity.ok("中国");
    }

    @RequestMapping("hello2")
    public ResponseEntity hello2(){
        return ResponseEntity.ok(Lists.newArrayList("中","国"));
    }

    @RequestMapping("hello3")
    public String hello3(){
        return "index";
    }
}
