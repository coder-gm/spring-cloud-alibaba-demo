package cn.com.micro.hystrix.cache;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/11 16:15
 */

import cn.com.micro.service.NacosClientService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;

/**
 * <h1>带有缓存功能的 Hystrix</h1>
 *
 * @author zgming
 */
@Slf4j
public class CacheHystrixCommand extends HystrixCommand<List<ServiceInstance>> {

    /**
     * 需要保护的服务
     */
    private final NacosClientService nacosClientService;

    /**
     * 方法需要传递的参数
     */
    private final String serviceId;

    private static final HystrixCommandKey CACHED_KEY = HystrixCommandKey.Factory.asKey("CacheHystrixCommand");

    public CacheHystrixCommand(NacosClientService nacosClientService, String serviceId) {

        super(
                HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CacheHystrixCommandGroup"))
                        .andCommandKey(CACHED_KEY)
        );

        this.nacosClientService = nacosClientService;
        this.serviceId = serviceId;
    }

    @Override
    protected List<ServiceInstance> run() throws Exception {
        log.info("CacheHystrixCommand 在Hystrix命令中获取服务实例: [{}], [{}]", this.serviceId, Thread.currentThread().getName());
        return this.nacosClientService.getNacosClientInfo(this.serviceId);
    }

    @Override
    protected String getCacheKey() {
        return serviceId;
    }

    /**
     * 后备模式
     *
     * @return
     */
    @Override
    protected List<ServiceInstance> getFallback() {
        return Collections.emptyList();
    }

    /**
     * <h2>根据缓存 key 清理在一次 Hystrix 请求上下文中的缓存</h2>
     */
    public static void flushRequestCache(String serviceId) {

        HystrixRequestCache.getInstance(CACHED_KEY, HystrixConcurrencyStrategyDefault.getInstance()).clear(serviceId);
        log.info("根据缓存 key 清理在一次 Hystrix 请求上下文中的缓存: [{}], [{}]",
                serviceId, Thread.currentThread().getName());
    }


}