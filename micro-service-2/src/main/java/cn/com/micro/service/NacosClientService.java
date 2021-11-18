package cn.com.micro.service;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 获取Nacos服务实例信息实现类
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/10 14:19
 */
@Slf4j
@Service
public class NacosClientService {

    private final DiscoveryClient discoveryClient;

    public NacosClientService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /**
     * 获取Nacos微服务信息
     *
     * @param serviceId
     * @return
     */
    public List<ServiceInstance> getNacosClientInfo(String serviceId) {
        log.info("微服务ID:{}，当前线程:{}", serviceId, Thread.currentThread().getName());
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        return instances;
    }


    /**
     * <h2>提供给编程方式的 Hystrix 请求合并</h2>
     */
    public List<List<ServiceInstance>> getNacosClientInfos(List<String> serviceIds) {
        log.info("请求nacos客户端获取服务实例信息: [{}]", JSON.toJSONString(serviceIds));
        List<List<ServiceInstance>> result = new ArrayList<>(serviceIds.size());

        serviceIds.forEach(s -> result.add(discoveryClient.getInstances(s)));
        return result;
    }


    /**
     * 使用注解实现 Hystrix 请求合并
     * <p>
     * batchMethod = "批量执行的方法名称，这个方法必须在当前类"
     * <p>
     * com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL 所有的请求都合并，全局性的
     * <p>
     * //@HystrixProperty(name = "timerDelayInMilliseconds", value = "300") //时间窗口是300毫秒
     *
     * @param serviceId
     * @return
     */
    @HystrixCollapser(
            batchMethod = "findNacosClientInfos",
            scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL,
            collapserProperties = {
                    @HystrixProperty(name = "timerDelayInMilliseconds", value = "300")
            }
    )
    public Future<List<ServiceInstance>> findNacosClientInfo(String serviceId) {
        // 系统运行正常, 不会走这个方法
        throw new RuntimeException("这个方法体不应该被执行!");
    }

    @HystrixCommand
    public List<List<ServiceInstance>> findNacosClientInfos(List<String> serviceIds) {
        log.info("请求nacos客户端获取服务实例信息: [{}]", JSON.toJSONString(serviceIds));
        List<List<ServiceInstance>> result = new ArrayList<>(serviceIds.size());

        serviceIds.forEach(s -> result.add(discoveryClient.getInstances(s)));
        return result;
    }


}
