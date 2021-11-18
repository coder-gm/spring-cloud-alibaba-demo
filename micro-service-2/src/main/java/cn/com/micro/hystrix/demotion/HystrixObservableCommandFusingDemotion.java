package cn.com.micro.hystrix.demotion;

import cn.com.micro.service.NacosClientService;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import rx.Observable;
import rx.Subscriber;

import java.util.Collections;
import java.util.List;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/10 22:12
 */
@Slf4j
public class HystrixObservableCommandFusingDemotion extends HystrixObservableCommand<List<ServiceInstance>> {

    /**
     * 需要被保护的服务类
     */
    private final NacosClientService hystrixTestServiceImpl;

    /**
     * 方法需要传递的参数
     * <p>
     * 这里是List的原因是，这里可以匹配执行方法
     */
    private final List<String> serviceIdList;

    public HystrixObservableCommandFusingDemotion(NacosClientService hystrixTestServiceImpl, List<String> serviceIdList) {
        super(
                HystrixObservableCommand.Setter
                        //用于对 Hystrix 命令进行分组, 分组之后便于统计展示于仪表盘、上传报告和预警等等
                        //内部进行度量统计时候的分组标识, 数据上报和统计的最小维度就是 groupKey
                        .withGroupKey(HystrixCommandGroupKey.Factory.asKey("hystrixTestServiceImpl"))

                        //HystrixCommand 的名字, 默认是当前类的名字, 主要方便 Hystrix 进行监控、报警等
                        .andCommandKey(HystrixCommandKey.Factory.asKey("hystrixTestServiceImpl"))

                        //舱壁模式
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties.Setter()

                                        // 开启降级
                                        .withFallbackEnabled(true)

                                        // 开启熔断器
                                        .withCircuitBreakerEnabled(true)
                        )
        );

        this.hystrixTestServiceImpl = hystrixTestServiceImpl;
        this.serviceIdList = serviceIdList;
    }


    /**
     * 要保护的方法  放在这里
     *
     * @return
     */
    @Override
    protected Observable<List<ServiceInstance>> construct() {
        return Observable.create(new Observable.OnSubscribe<List<ServiceInstance>>() {
            // Observable 有三个关键的事件方法,
            // 分别是
            // onNext：发起服务的调用
            // onCompleted：服务已经调用完成
            // onError：在调用过程中出错了
            @Override
            public void call(Subscriber<? super List<ServiceInstance>> subscriber) {

                try {
                    if (!subscriber.isUnsubscribed()) {

                        log.info("subscriber command task: [{}], [{}]", JSON.toJSONString(serviceIdList), Thread.currentThread().getName());

                        serviceIdList.forEach(
                                //发起服务的调用
                                s -> subscriber.onNext(hystrixTestServiceImpl.getNacosClientInfo(s))
                        );

                        //服务已经调用完成
                        subscriber.onCompleted();

                        log.info("command task completed: [{}], [{}]", JSON.toJSONString(serviceIdList), Thread.currentThread().getName());
                    }
                } catch (Exception ex) {
                    //在调用过程中出错了
                    subscriber.onError(ex);
                }
            }
        });
    }


    /**
     * 服务降级策略
     *
     * @return
     */
    @Override
    protected Observable<List<ServiceInstance>> resumeWithFallback() {
        return Observable.create(new Observable.OnSubscribe<List<ServiceInstance>>() {
            @Override
            public void call(Subscriber<? super List<ServiceInstance>> subscriber) {

                try {
                    if (!subscriber.isUnsubscribed()) {

                        log.info("(fallback) subscriber command task: [{}], [{}]", JSON.toJSONString(serviceIdList), Thread.currentThread().getName());
                        //返回一个空集合
                        subscriber.onNext(Collections.emptyList());
                        subscriber.onCompleted();

                        log.info("(fallback) command task completed: [{}], [{}]", JSON.toJSONString(serviceIdList), Thread.currentThread().getName());
                    }
                } catch (Exception ex) {
                    subscriber.onError(ex);
                }
            }
        });
    }


}
