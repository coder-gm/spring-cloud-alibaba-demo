package cn.com.gateway.config.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 * <p>
 * ,读取Nacos 相关的配置项,用于配置监听器
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/25 9:22
 */
@Configuration
public class ReadNacosConfigInfo {
    /**
     * 读取配置的超时时间
     */
    public static final long DEFAULT_TIMEOUT = 30000;

    /**
     * Nacos 服务器地址
     */
    public static String NACOS_SERVICE_ADDR;

    /**
     * 命名空间
     */
    public static String NACOS_NAMESPACE;


    /**
     * data-id
     */
    public static String NACOS_ROUTE_DATA_ID;


    /**
     * 分组ID
     */
    public static String NACOS_ROUTE_GROUP_ID;


    /**
     * 服务注册地址
     *
     * @param nacosServiceAddr
     */
    @Value("${spring.cloud.nacos.discovery.server-addr}")
    public void setMacosServiceAddr(String nacosServiceAddr) {
        NACOS_SERVICE_ADDR = nacosServiceAddr;
    }

    /**
     * 命名空间ID
     *
     * @param nacosNamespace
     */
    @Value("${spring.cloud.nacos.discovery.namespace}")
    public void setNacosNamespace(String nacosNamespace) {
        NACOS_NAMESPACE = nacosNamespace;
    }


    /**
     * Nacos注册中心中 配置文件 data-id
     *
     * @param nacosRouteDataId
     */
    @Value("${nacos.gateway.route.config.data-id}")
    public void setNacosRouteDataId(String nacosRouteDataId) {
        NACOS_ROUTE_DATA_ID = nacosRouteDataId;
    }

    /**
     * 分组ID
     *
     * @param nacosRouteGroupId
     */
    @Value("${nacos.gateway.route.config.group}")
    public void setNacosRouteGroupId(String nacosRouteGroupId) {
        NACOS_ROUTE_GROUP_ID = nacosRouteGroupId;
    }


}
