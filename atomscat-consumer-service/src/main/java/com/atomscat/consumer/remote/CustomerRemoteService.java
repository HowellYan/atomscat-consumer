package com.atomscat.consumer.remote;


import com.atomscat.provider.request.CustomerInfoRequest;
import com.atomscat.provider.response.CustomerInfoResponse;
import com.atomscat.provider.service.CustomerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerRemoteService {
    @DubboReference
    private CustomerService customerService;

    public CustomerInfoResponse getCustInfo(CustomerInfoRequest customerInfoRequest) {
        return customerService.getCustomerInfo(customerInfoRequest);
    }

    public List<CustomerInfoResponse> getCustInfoList(CustomerInfoRequest customerInfoRequest) {
        return customerService.getCustomerInfoList(customerInfoRequest);
    }

    public CustomerInfoResponse setRedis(CustomerInfoRequest customerInfoRequest) {
        return customerService.setRedis(customerInfoRequest);
    }

    public List<CustomerInfoResponse> test(CustomerInfoRequest customerInfoRequest) {
        return customerService.test(customerInfoRequest);
    }
}
