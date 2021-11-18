package cn.com.gateway.config.route;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 路由的初始化与动态监听
 * <p>
 * 通过 nacos 下发动态路由配置, 监听 Nacos 中路由配置变更
 *
 * @author zgming
 */
@Slf4j
@Component
@DependsOn({"readNacosConfigInfo"}) //表示当前bean的初始化必须在另一个指定的的bean初始化之后
public class RouteInitAndDynamicListen {

    /**
     * Nacos 配置服务
     */
    private ConfigService configService;

    private final AddAndUpdateRoute dynamicRouteService;

    @Autowired
    public RouteInitAndDynamicListen(AddAndUpdateRoute dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }

    /**
     * Bean 在容器中构造完成之后会执行 init 方法
     */
    @PostConstruct
    public void init() {

        log.info("网关路由初始化开始....");

        try {
            // 初始化 Nacos 配置客户端
            configService = initConfigService();
            if (null == configService) {
                log.error("初始化配置服务失败!!");
                return;
            }

            // 通过 Nacos Config 并指定路由配置路径去获取路由配置
            String configInfo = configService.getConfig(
                    ReadNacosConfigInfo.NACOS_ROUTE_DATA_ID,
                    ReadNacosConfigInfo.NACOS_ROUTE_GROUP_ID,
                    ReadNacosConfigInfo.DEFAULT_TIMEOUT
            );

            log.info("通过 Nacos Config 并指定路由配置路径去获取路由配置信息:{}", configInfo);
            List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);

            if (CollectionUtils.isNotEmpty(definitionList)) {
                for (RouteDefinition definition : definitionList) {
                    log.info("初始化网关路由配置:{}", JSON.toJSONString(definition));
                    //增加路由定义
                    dynamicRouteService.addRouteDefinition(definition);
                }
            }

        } catch (Exception ex) {
            log.error("网关路由初始化有一些错误:{},{}", ex.getMessage(), ex);
        }

        // 设置监听器
        dynamicRouteByNacosListener(ReadNacosConfigInfo.NACOS_ROUTE_DATA_ID, ReadNacosConfigInfo.NACOS_ROUTE_GROUP_ID);
    }

    /**
     * 初始化 Nacos Config
     *
     * @return
     */
    private ConfigService initConfigService() {
        log.info("初始化 Nacos Config ....");

        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", ReadNacosConfigInfo.NACOS_SERVICE_ADDR);
            properties.setProperty("namespace", ReadNacosConfigInfo.NACOS_NAMESPACE);
            return configService = NacosFactory.createConfigService(properties);
        } catch (Exception ex) {
            log.error("初始化网关路由错误,信息:{},异常原因:{}", ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 监听 Nacos 下发的动态路由配置
     *
     * @param dataId
     * @param group
     */
    private void dynamicRouteByNacosListener(String dataId, String group) {
        log.info("监听 Nacos 下发的动态路由配置 ....");
        try {
            // 给 Nacos Config 客户端增加一个监听器
            configService.addListener(dataId, group, new Listener() {

                /**
                 * 自己提供线程池执行操作
                 *
                 * @return
                 */
                @Override
                public Executor getExecutor() {
                    return null;
                }

                /**
                 * 监听器收到配置更新
                 *
                 * @param configInfo Nacos 中最新的配置定义
                 */
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("开始更新配置:{}", configInfo);

                    List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
                    log.info("更新路由: {}", JSON.toJSONString(definitionList));

                    //更新路由定义
                    dynamicRouteService.updateList(definitionList);
                }
            });
        } catch (NacosException ex) {
            log.error("动态更新网关配置失败: [{}]", ex.getMessage(), ex);
        }
    }
}
