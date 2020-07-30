package com.atomscat.consumer.remote;


import com.atomscat.provider.request.CustomerInfoRequest;
import com.atomscat.provider.response.CustomerInfoResponse;
import com.atomscat.provider.service.CustomerService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    @GlobalTransactional
    public List<CustomerInfoResponse> test(CustomerInfoRequest customerInfoRequest) throws Exception {
        List<CustomerInfoResponse> list = new ArrayList<>();
        customerService.test(customerInfoRequest);
        customerService.test2(customerInfoRequest);
        log.info("正常");
        return list;
    }
}
