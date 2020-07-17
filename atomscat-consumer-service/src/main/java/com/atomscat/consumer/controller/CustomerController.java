package com.atomscat.consumer.controller;

import com.atomscat.consumer.config.annotation.RateLimiter;
import com.atomscat.consumer.remote.CustomerRemoteService;
import com.atomscat.provider.request.CustomerInfoRequest;
import com.atomscat.provider.response.CustomerInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerRemoteService customerRemoteService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody CustomerInfoRequest customerInfoRequest) {
        log.info(customerInfoRequest.getName());
        Map<String, Object> map = new HashMap<>();
        map.put("name", customerInfoRequest.getName() + "_1");
        map.put("code","000000");
        map.put("message","成功");
        return map;
    }

    @PostMapping("/info")
    public Map<String, Object> getCustInfo(@RequestBody CustomerInfoRequest customerInfoRequest) {
        log.info(customerInfoRequest.getName());
        Map<String, Object> map = new HashMap<>();
        map.put("name", customerInfoRequest.getName() + "_2");
        map.put("list", customerRemoteService.getCustInfoList(customerInfoRequest));
        map.put("code","000000");
        map.put("message","成功");
        return map;
    }

    @PostMapping("/setRedis")
    @RateLimiter(limit = 10, timeout = 60000)
    public CustomerInfoResponse setRedis(@RequestBody CustomerInfoRequest customerInfoRequest) {
        return customerRemoteService.setRedis(customerInfoRequest);
    }
}
