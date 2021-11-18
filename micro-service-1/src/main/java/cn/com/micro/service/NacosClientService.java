package cn.com.micro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/20 20:58
 */
@Slf4j
@Service
public class NacosClientService {

    private final DiscoveryClient discoveryClient;

    //使用构造方法的方式注入
    public NacosClientService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }


    /**
     * 拉取服务注册信息实例
     *
     * @param serviceId 服务ID (应用名称)
     * @return
     */
    public List<ServiceInstance> getNacosClientInfo(String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        return instances;
    }

}
