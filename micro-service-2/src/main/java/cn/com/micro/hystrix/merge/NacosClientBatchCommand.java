package cn.com.micro.hystrix.merge;

import cn.com.micro.service.NacosClientService;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;

/**
 * 批量请求Hystrix command
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/12 23:20
 */
@Slf4j
public class NacosClientBatchCommand extends HystrixCommand<List<List<ServiceInstance>>> {

    private final NacosClientService nacosClientService;
    private final List<String> serviceIds;

    protected NacosClientBatchCommand(NacosClientService nacosClientService, List<String> serviceIds) {

        super(
                HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("NacosClientBatchCommand"))
        );

        this.nacosClientService = nacosClientService;
        this.serviceIds = serviceIds;
    }

    /**
     * 要保护的方法调用 写在 run 方法中
     *
     * @return
     * @throws Exception
     */
    @Override
    protected List<List<ServiceInstance>> run() throws Exception {
        log.info("使用nacos client batch命令获取结果: [{}]", JSON.toJSONString(serviceIds));
        return nacosClientService.getNacosClientInfos(serviceIds);
    }

    /**
     * 降级处理策略
     *
     * @return
     */
    @Override
    protected List<List<ServiceInstance>> getFallback() {
        log.warn("Nacos客户端批处理命令失败，使用回退");
        return Collections.emptyList();
    }
}
