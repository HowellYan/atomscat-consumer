package com.atomscat.consumer.remote;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atomscat.provider.request.CustomerInfoRequest;
import com.atomscat.provider.response.CustomerInfoResponse;
import com.atomscat.provider.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerRemoteService {
    @Reference
    private CustomerService customerService;

    public CustomerInfoResponse getCustInfo(CustomerInfoRequest customerInfoRequest) {
        return customerService.getCustomerInfo(customerInfoRequest);
    }

    public CustomerInfoResponse setRedis(CustomerInfoRequest customerInfoRequest) {
        return customerService.setRedis(customerInfoRequest);
    }
}
