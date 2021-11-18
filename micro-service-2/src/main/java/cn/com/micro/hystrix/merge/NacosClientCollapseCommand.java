package cn.com.micro.hystrix.merge;


import cn.com.micro.service.NacosClientService;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 请求合并器
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/12 23:30
 */
//HystrixCollapser<批量返回的类型, 单个对象返回类型, 请求参数类型>
@Slf4j
public class NacosClientCollapseCommand extends HystrixCollapser<List<List<ServiceInstance>>, List<ServiceInstance>, String> {

    private final NacosClientService nacosClientService;
    private final String serviceId;


    /**
     * 构造器
     *
     * @param nacosClientService
     * @param serviceId
     */
    public NacosClientCollapseCommand(NacosClientService nacosClientService, String serviceId) {

        super(
                HystrixCollapser.Setter
                        .withCollapserKey(HystrixCollapserKey.Factory.asKey("NacosClientCollapseCommand"))
                        //等待300毫秒再次合并请求
                        .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(300))
        );

        this.nacosClientService = nacosClientService;
        this.serviceId = serviceId;
    }

    /**
     * <h2>获取请求中的参数</h2>
     */
    @Override
    public String getRequestArgument() {
        return this.serviceId;
    }

    /**
     * <h2>创建批量请求 Hystrix Command</h2>
     */
    @Override
    protected HystrixCommand<List<List<ServiceInstance>>> createCommand(Collection<CollapsedRequest<List<ServiceInstance>,
            String>> collapsedRequests) {

        //拿到所有的请求入参
        List<String> serviceIds = new ArrayList<>(collapsedRequests.size());
        serviceIds.addAll(collapsedRequests.stream().map(CollapsedRequest::getArgument).collect(Collectors.toList()));

        return new NacosClientBatchCommand(nacosClientService, serviceIds);
    }

    /**
     * <h2>响应分发给单独的请求</h2>
     */
    @Override
    protected void mapResponseToRequests(List<List<ServiceInstance>> batchResponse,
                                         Collection<CollapsedRequest<List<ServiceInstance>,
                                                 String>> collapsedRequests) {

        int count = 0;

        for (CollapsedRequest<List<ServiceInstance>, String> collapsedRequest : collapsedRequests) {

            // 从批量响应集合中按顺序取出结果
            List<ServiceInstance> instances = batchResponse.get(count++);
            // 将结果返回原 Response 中
            collapsedRequest.setResponse(instances);
        }
    }
}