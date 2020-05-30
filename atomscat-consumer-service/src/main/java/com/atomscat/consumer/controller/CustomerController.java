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

@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerRemoteService customerRemoteService;

    @PostMapping("/info")
    public CustomerInfoResponse getCustInfo(@RequestBody CustomerInfoRequest customerInfoRequest) {
        log.info(customerInfoRequest.getName());
        for (int i=0;i < 200; i++) {
            customerRemoteService.getCustInfo(customerInfoRequest);
        }
        return customerRemoteService.getCustInfo(customerInfoRequest);
    }

    @PostMapping("/setRedis")
    @RateLimiter(limit = 10, timeout = 60000)
    public CustomerInfoResponse setRedis(@RequestBody CustomerInfoRequest customerInfoRequest) {
        return customerRemoteService.setRedis(customerInfoRequest);
    }
}
