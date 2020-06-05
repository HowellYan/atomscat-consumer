package com.atomscat.consumer.controller;

import com.atomscat.consumer.response.RushToBuyResponse;
import com.atomscat.consumer.service.OrderService;
import com.atomscat.provider.request.CustomerInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/rushToBuy")
    public RushToBuyResponse rushToBuy(@RequestBody CustomerInfoRequest customerInfoRequest) {
        RushToBuyResponse rushToBuyResponse = new RushToBuyResponse();
        rushToBuyResponse.setCode("000000");
        rushToBuyResponse.setMsg(orderService.rushToBuy(customerInfoRequest.getName()));
        return rushToBuyResponse;
    }
}
