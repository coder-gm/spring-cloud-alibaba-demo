package cn.com.micro.config;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * OpenFeign 配置类
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/9 21:32
 */
//@Configuration
public class OpenFeignConfig {

    //请求连接超时时间 5000毫秒
    public static final int CONNECT_TIMEOUT_MILLS = 15000;

    //读取请求结果的响应时间 5000毫秒
    public static final int READ_TIMEOUT_MILLS = 15000;

    /**
     * 开启OpenFeign日志
     *
     * @return
     */
    @Bean
    public Logger.Level feignLogger() {
        //需要注意: 日志级别需要修改成debug级别才能被打印

        // NONE :默认行为不记录任何日志,
        // BASIC:记录请求方法,URL,以及响应状态码和执行时间
        // HEADERS:记录请求方法,URL,以及响应状态码和执行时间,还有头信息
        // FULL:包含了HTTP请求的所有部分
        return Logger.Level.FULL;
    }

    /**
     * 开启OpenFeign 重试
     *
     * @return
     */
    @Bean
    public Retryer feignRetryer() {
        //period: 发起当前请求的最小时间间隔,单位毫秒 (请求失败,会间隔100--1000毫秒 区间中再次发起请求)
        //maxPeriod:发起当前请求的最大时间间隔,单位毫秒 (请求失败,会间隔100--1000毫秒 区间中再次发起请求)
        //maxAttempts: 最多请求次数
        return new Retryer.Default(100, SECONDS.toMillis(1), 5);
    }


    /**
     * 对请求的连接和响应时间进行限制
     *
     * @return
     */
    @Bean
    public Request.Options options() {
        //followRedirects 请求转发的时间是否也算在内,true算在内,false不算在内
        return new Request.Options(CONNECT_TIMEOUT_MILLS, TimeUnit.MICROSECONDS,
                READ_TIMEOUT_MILLS, TimeUnit.MICROSECONDS,
                true);
    }


}
