package cn.com.micro.controller;

import cn.com.micro.hystrix.cache.CacheHystrixCommand;
import cn.com.micro.hystrix.cache.CacheHystrixCommandAnnotation;
import cn.com.micro.hystrix.demotion.HystrixCommandFusingDemotion;
import cn.com.micro.hystrix.demotion.HystrixObservableCommandFusingDemotion;
import cn.com.micro.hystrix.merge.NacosClientCollapseCommand;
import cn.com.micro.service.NacosClientService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.*;
import rx.Observable;
import rx.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/10 14:39
 */
@Slf4j
@RestController
@RequestMapping("/hystrixTest")
public class HystrixTestController {


    private final NacosClientService hystrixTestServiceImpl;
    private final CacheHystrixCommandAnnotation cacheHystrixCommandAnnotation;

    public HystrixTestController(NacosClientService hystrixTestServiceImpl, CacheHystrixCommandAnnotation cacheHystrixCommandAnnotation) {
        this.hystrixTestServiceImpl = hystrixTestServiceImpl;
        this.cacheHystrixCommandAnnotation = cacheHystrixCommandAnnotation;
    }


    /**
     * 拉取服务注册信息实例
     *
     * @param serviceId 服务ID (应用名称)
     * @return
     */
    @PostMapping(value = "/getNacosClientInfo")
    public List<ServiceInstance> getNacosClientInfo(String serviceId) {
        return hystrixTestServiceImpl.getNacosClientInfo(serviceId);
    }


    /**
     * 拉取服务注册信息实例
     *
     * @param serviceId 服务ID (应用名称)
     * @return
     */
    @PostMapping(value = "/getNacosClientInfo1")
    public List<ServiceInstance> getNacosClientInfo1(String serviceId) throws ExecutionException, InterruptedException {
//        //第一种调用方式（同步阻塞方式）
//        List<ServiceInstance> serviceInstanceList0 = new NacosClientHystrixCommand(hystrixTestServiceImpl, serviceId).execute();
//
//        //第二种调用方式（异步非阻塞方式）
//        Future<List<ServiceInstance>> future = new NacosClientHystrixCommand(hystrixTestServiceImpl, serviceId).queue();
//        List<ServiceInstance> serviceInstanceList1 = future.get();
//
//        //第三种调用方式热响应调用
//        Observable<List<ServiceInstance>> listObservable = new NacosClientHystrixCommand(hystrixTestServiceImpl, serviceId).observe();
//        List<ServiceInstance> serviceInstanceList2 = listObservable.toBlocking().single();

        //第四种调用方式冷响应调用
        Observable<List<ServiceInstance>> listObservable1 = new HystrixCommandFusingDemotion(hystrixTestServiceImpl, serviceId).toObservable();
        List<ServiceInstance> serviceInstanceList3 = listObservable1.toBlocking().single();

        return serviceInstanceList3;
    }


    /**
     * 拉取服务注册信息实例
     *
     * @param serviceId 服务ID (应用名称)
     * @return
     */
    @PostMapping(value = "/getNacosClientInfo2")
    public List<ServiceInstance> getNacosClientInfo2(String serviceId) {
        List<String> serviceIdList = Arrays.asList(serviceId, serviceId, serviceId);

        List<List<ServiceInstance>> result = new ArrayList<>(serviceIdList.size());
        HystrixObservableCommandFusingDemotion observableCommand =
                new HystrixObservableCommandFusingDemotion(hystrixTestServiceImpl, serviceIdList);

        //异步执行命令
        Observable<List<ServiceInstance>> observe = observableCommand.observe();

        //注册获取结果
        observe.subscribe(new Observer<List<ServiceInstance>>() {

            // 执行 onNext 之后再去执行 onCompleted
            @Override
            public void onCompleted() {
                log.info("all tasks is complete: [{}], [{}]", serviceId, Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onNext(List<ServiceInstance> serviceInstances) {
                result.add(serviceInstances);
            }
        });

        log.info("observable command result is : [{}], [{}]", JSON.toJSONString(result), Thread.currentThread().getName());
        return result.get(0);
    }


    @GetMapping("/useCache")
    public void useCache(@RequestParam String serviceId) {
        //使用缓存 Command，发起两次请求
        CacheHystrixCommand cacheHystrixCommand1 = new CacheHystrixCommand(hystrixTestServiceImpl, serviceId);
        CacheHystrixCommand cacheHystrixCommand2 = new CacheHystrixCommand(hystrixTestServiceImpl, serviceId);

        List<ServiceInstance> serviceInstanceList1 = cacheHystrixCommand1.execute();
        List<ServiceInstance> serviceInstanceList2 = cacheHystrixCommand2.execute();

        log.info("serviceInstanceList1:{}", JSON.toJSONString(serviceInstanceList1));
        log.info("serviceInstanceList2:{}", JSON.toJSONString(serviceInstanceList2));

        //清除缓存
        CacheHystrixCommand.flushRequestCache(serviceId);


        //使用缓存 Command，发起两次请求
        CacheHystrixCommand cacheHystrixCommand3 = new CacheHystrixCommand(hystrixTestServiceImpl, serviceId);
        CacheHystrixCommand cacheHystrixCommand4 = new CacheHystrixCommand(hystrixTestServiceImpl, serviceId);

        List<ServiceInstance> serviceInstanceList3 = cacheHystrixCommand3.execute();
        List<ServiceInstance> serviceInstanceList4 = cacheHystrixCommand4.execute();

        log.info("serviceInstanceList3:{}", JSON.toJSONString(serviceInstanceList3));
        log.info("serviceInstanceList4:{}", JSON.toJSONString(serviceInstanceList4));
    }


    @GetMapping("/cache-annotation-01")
    public List<ServiceInstance> useCacheByAnnotation01(@RequestParam String serviceId) {

        log.info("使用annotation01(controller)来获取nacos客户端信息: [{}]", serviceId);

        List<ServiceInstance> result01 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);
        List<ServiceInstance> result02 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);

        // 清除掉缓存
        cacheHystrixCommandAnnotation.flushCacheByAnnotation01(serviceId);

        List<ServiceInstance> result03 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);
        // 这里有第四次调用
        return cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);
    }

    @GetMapping("/cache-annotation-02")
    public List<ServiceInstance> useCacheByAnnotation02(@RequestParam String serviceId) {

        log.info("使用annotation02(controller)来获取nacos客户端信息: [{}]", serviceId);

        List<ServiceInstance> result01 = cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);
        List<ServiceInstance> result02 =
                cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);

        // 清除掉缓存
        cacheHystrixCommandAnnotation.flushCacheByAnnotation02(serviceId);

        List<ServiceInstance> result03 = cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);
        // 这里有第四次调用
        return cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);
    }

    @GetMapping("/cache-annotation-03")
    public List<ServiceInstance> useCacheByAnnotation03(@RequestParam String serviceId) {

        log.info("使用annotation03(controller)来获取nacos客户端信息: [{}]", serviceId);

        List<ServiceInstance> result01 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);
        List<ServiceInstance> result02 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);

        // 清除掉缓存
        cacheHystrixCommandAnnotation.flushCacheByAnnotation03(serviceId);

        List<ServiceInstance> result03 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);
        // 这里有第四次调用
        return cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);
    }


    /**
     * <h2>编程方式实现请求合并</h2>
     */
    @GetMapping("/request-merge")
    public void requestMerge() throws Exception {

        // 前三个请求会被合并
        NacosClientCollapseCommand collapseCommand01 = new NacosClientCollapseCommand(hystrixTestServiceImpl, "e-commerce-nacos-client1");
        NacosClientCollapseCommand collapseCommand02 = new NacosClientCollapseCommand(hystrixTestServiceImpl, "e-commerce-nacos-client2");
        NacosClientCollapseCommand collapseCommand03 = new NacosClientCollapseCommand(hystrixTestServiceImpl, "e-commerce-nacos-client3");

        Future<List<ServiceInstance>> future01 = collapseCommand01.queue();
        Future<List<ServiceInstance>> future02 = collapseCommand02.queue();
        Future<List<ServiceInstance>> future03 = collapseCommand03.queue();

        future01.get();
        future02.get();
        future03.get();

        Thread.sleep(2000);

        // 过了合并的时间窗口, 第四个请求单独发起
        NacosClientCollapseCommand collapseCommand04 = new NacosClientCollapseCommand(hystrixTestServiceImpl, "e-commerce-nacos-client4");
        Future<List<ServiceInstance>> future04 = collapseCommand04.queue();
        future04.get();
    }


    /**
     * <h2>注解的方式实现请求合并</h2>
     */
    @GetMapping("/request-merge-annotation")
    public void requestMergeAnnotation() throws Exception {

        Future<List<ServiceInstance>> future01 = hystrixTestServiceImpl.findNacosClientInfo("e-commerce-nacos-client1");
        Future<List<ServiceInstance>> future02 = hystrixTestServiceImpl.findNacosClientInfo("e-commerce-nacos-client2");
        Future<List<ServiceInstance>> future03 = hystrixTestServiceImpl.findNacosClientInfo("e-commerce-nacos-client3");

        future01.get();
        future02.get();
        future03.get();

        //睡眠2000毫秒
        Thread.sleep(2000);

        // 过了合并的时间窗口, 第四个请求单独发起
        Future<List<ServiceInstance>> future04 = hystrixTestServiceImpl.findNacosClientInfo("e-commerce-nacos-client4");
        future04.get();
    }

}
