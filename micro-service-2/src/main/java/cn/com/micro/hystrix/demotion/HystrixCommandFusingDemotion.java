package cn.com.micro.hystrix.demotion;

import cn.com.micro.service.NacosClientService;
import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.THREAD;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/10 17:25
 */
//HystrixCommand<被保护的方法返回的类型>
//Hystrix 舱璧模式有两种实现方式：1.线程池（注解方式实现熔断、降级就是使用的线程池）（常用） 2.信号量：算法+数据结构，有限状态语机
@Slf4j
public class HystrixCommandFusingDemotion extends HystrixCommand<List<ServiceInstance>> {


    /**
     * 需要被保护的服务类
     */
    private final NacosClientService hystrixTestServiceImpl;

    /**
     * 需要被保护的服务类的方法入参
     */
    private final String serviceId;


    public HystrixCommandFusingDemotion(NacosClientService hystrixTestServiceImpl, String serviceId) {


        //配置线程池隔离策略
        super(

                Setter
                        //用于对 Hystrix 命令进行分组, 分组之后便于统计展示于仪表盘、上传报告和预警等等
                        //内部进行度量统计时候的分组标识, 数据上报和统计的最小维度就是 groupKey
                        .withGroupKey(HystrixCommandGroupKey.Factory.asKey("hystrixTestServiceImpl"))

                        //HystrixCommand 的名字, 默认是当前类的名字, 主要方便 Hystrix 进行监控、报警等
                        .andCommandKey(HystrixCommandKey.Factory.asKey("hystrixTestServiceImpl"))

                        //舱壁模式 （线程池所用到的key）
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HystrixTestServiceImpl"))

                        //配置线程池
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties
                                        .Setter()
                                        //线程池隔离策略
                                        .withExecutionIsolationStrategy(THREAD)

                                        //开启降级
                                        .withFallbackEnabled(true)

                                        //是否开启熔断器
                                        .withCircuitBreakerEnabled(true)
                        )

        );

//        //配置信号量隔离策略（基本不用，要使用线程的方式）
//        super(
//
//                Setter
//                        .withGroupKey(HystrixCommandGroupKey.Factory.asKey("hystrixTestServiceImpl"))
//
//                        //HystrixCommand 的名字, 默认是当前类的名字, 主要方便 Hystrix 进行监控、报警等
//                        .andCommandKey(HystrixCommandKey.Factory.asKey("hystrixTestServiceImpl"))
//                        //配置线程池
//                        .andCommandPropertiesDefaults(
//                                HystrixCommandProperties
//                                        .Setter()
//                                        //至少有10个请求熔断器才会错误率计算
//                                        .withCircuitBreakerRequestVolumeThreshold(10)
//                                        //熔断器中断请求 5秒之后，进入一个半打开的状态，放开部分请求进行重试（对线程池隔离策略也是适用的）
//                                        .withCircuitBreakerSleepWindowInMilliseconds(5000)
//                                        //当错误率达到50%的时候,就开启熔断保护
//                                        .withCircuitBreakerErrorThresholdPercentage(50)
//                                        //指定使用信号量隔离策略
//                                        .withExecutionIsolationStrategy(SEMAPHORE)
//                        );
//
//        );


        this.hystrixTestServiceImpl = hystrixTestServiceImpl;
        this.serviceId = serviceId;
    }

    /**
     * 要保护的方法调用 写在 run 方法中
     *
     * @return
     * @throws Exception
     */
    @Override
    protected List<ServiceInstance> run() throws Exception {
        log.info("微服务ID:{}，当前线程:{}", serviceId, Thread.currentThread().getName());
        return this.hystrixTestServiceImpl.getNacosClientInfo(this.serviceId);
    }


    /**
     * 降级处理策略
     *
     * @return
     */
    @Override
    protected List<ServiceInstance> getFallback() {
        log.warn("微服务ID:{}，当前线程:{}", serviceId, Thread.currentThread().getName());
        //返回一个空的集合
        return Collections.emptyList();
    }


}
