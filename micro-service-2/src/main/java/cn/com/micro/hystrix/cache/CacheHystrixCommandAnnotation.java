package cn.com.micro.hystrix.cache;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/12 17:05
 */

import cn.com.micro.service.NacosClientService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <h1>使用注解方式开启 Hystrix 请求缓存</h1>
 *
 * @author zgming
 */
@Slf4j
@Service
public class CacheHystrixCommandAnnotation {

    private final NacosClientService nacosClientService;

    public CacheHystrixCommandAnnotation(NacosClientService nacosClientService) {
        this.nacosClientService = nacosClientService;
    }

    // 第一种 Hystrix Cache 注解的使用方法

    /**
     * 添加缓存
     * <p>
     * //@CacheResult(cacheKeyMethod = "获取缓存的ke")
     *
     * @param serviceId
     * @return
     */
    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(commandKey = "CacheHystrixCommandAnnotation")
    public List<ServiceInstance> useCacheByAnnotation01(String serviceId) {
        log.info("使用cache01获取nacos客户端信息: [{}]", serviceId);
        return nacosClientService.getNacosClientInfo(serviceId);
    }

    /**
     * 清理缓存
     * <p>
     * //@CacheRemove(commandKey = "必须要和@HystrixCommand的commandKey一样", cacheKeyMethod = "获取缓存的ke")
     * <p>
     * //@HystrixCommand 这个注解不需要添加什么参数，因为 useCacheByAnnotation01方法上已经添加过了
     *
     * @param cacheId
     */
    @CacheRemove(commandKey = "CacheHystrixCommandAnnotation", cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public void flushCacheByAnnotation01(String cacheId) {
        log.info("刷新hystrix缓存key: [{}]", cacheId);
    }

    /**
     * 指定缓存的key
     * <p>
     * 1.这个方法必须要要和 @CacheResult(cacheKeyMethod = "getCacheKey") 名称一样
     * <p>
     * 2.getCacheKey(这个入参必须要和缓存的方法（useCacheByAnnotation01()）入参一致)
     * <p>
     * 3.这个方法的返回值必须是String类型，否则不生效
     *
     * @param cacheId
     * @return
     */
    public String getCacheKey(String cacheId) {
        return cacheId;
    }


    // 第二种 Hystrix Cache 注解的使用方法

    /**
     * 添加缓存
     *
     * @param serviceId
     * @return
     */
    @CacheResult
    @HystrixCommand(commandKey = "CacheHystrixCommandAnnotation")
    public List<ServiceInstance> useCacheByAnnotation02(@CacheKey String serviceId) {
        log.info(" 使用cache02获取nacos客户端信息: [{}]", serviceId);
        return nacosClientService.getNacosClientInfo(serviceId);
    }


    /**
     * 清理缓存
     * <p>
     * //@CacheRemove(commandKey = "必须要和@HystrixCommand的commandKey一样")
     * <p>
     * //@HystrixCommand 这个注解不需要添加什么参数，因为 useCacheByAnnotation02方法上已经添加过了
     *
     * @param cacheId
     */
    @CacheRemove(commandKey = "CacheHystrixCommandAnnotation")
    @HystrixCommand
    public void flushCacheByAnnotation02(@CacheKey String cacheId) {
        log.info("刷新hystrix缓存key: [{}]", cacheId);
    }


    // 第三种 Hystrix Cache 注解的使用方法

    /**
     * 添加缓存
     * <p>
     * 注意：会使用入参的所有参数作为cacheKey
     *
     * @param serviceId
     * @return
     */
    @CacheResult
    @HystrixCommand(commandKey = "CacheHystrixCommandAnnotation")
    public List<ServiceInstance> useCacheByAnnotation03(String serviceId) {
        log.info("使用cache03获取nacos客户端信息: [{}]", serviceId);
        return nacosClientService.getNacosClientInfo(serviceId);
    }


    /**
     * 清理缓存
     * <p>
     * //@CacheRemove(commandKey = "必须要和@HystrixCommand的commandKey一样")
     * <p>
     * //@HystrixCommand 这个注解不需要添加什么参数，因为 useCacheByAnnotation03方法上已经添加过了
     * <p>
     * 这个入参必须要和useCacheByAnnotation03方法的入参一致
     *
     * @param cacheId
     */
    @CacheRemove(commandKey = "CacheHystrixCommandAnnotation")
    @HystrixCommand
    public void flushCacheByAnnotation03(String cacheId) {
        log.info("刷新hystrix缓存key: [{}]", cacheId);
    }
}