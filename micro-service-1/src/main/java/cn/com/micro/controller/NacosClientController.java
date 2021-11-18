package cn.com.micro.controller;

import cn.com.micro.service.NacosClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/20 21:10
 */
@Slf4j
@RestController
@RequestMapping("/nacos-client")
public class NacosClientController {

    @Autowired
    private NacosClientService nacosClientService;

    @PostMapping("/getNacosClientInfo")
    public List<ServiceInstance> getNacosClientInfo(String serviceId) {
        return nacosClientService.getNacosClientInfo(serviceId);
    }

}
