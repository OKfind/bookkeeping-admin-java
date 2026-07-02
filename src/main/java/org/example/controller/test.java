package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : XR
 * @date :2026/6/24 17:16
 * @description :TODO
 */
@RestController
public class test {
    @GetMapping("/test")
    public String SayHello(){
        return "你好....";
    }
}
