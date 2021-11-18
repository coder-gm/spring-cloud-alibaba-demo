package cn.com.gateway.config.route;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 动态路由的增删改查
 * <p>
 * 事件推送 Aware: 动态更新路由网关 Service
 * <p>
 * 每次在Nacos中修改了路由 就会事件推送
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/25 9:58
 */
@Service
@Slf4j
public class AddAndUpdateRoute implements ApplicationEventPublisherAware {


    /**
     * 写Gateway中的路由定义
     */
    private final RouteDefinitionWriter routeDefinitionWriter;

    /**
     * 获取Gateway的路由定义
     */
    private final RouteDefinitionLocator routeDefinitionLocator;

    /**
     * 事件发布
     */
    private ApplicationEventPublisher publisher;

    public AddAndUpdateRoute(RouteDefinitionWriter routeDefinitionWriter, RouteDefinitionLocator routeDefinitionLocator) {
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    /**
     * @param applicationEventPublisher
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        //完成事件推送句柄的初始化
        this.publisher = applicationEventPublisher;
    }



    /**
     * 增加路由定义
     *
     * @param definition
     * @return
     */
    public String addRouteDefinition(RouteDefinition definition) {

        log.info("添加网关路由信息: {}", JSON.toJSONString(definition));

        // 保存路由配置并发布
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        // 发布事件通知给 Gateway, 同步新增的路由定义
        this.publisher.publishEvent(new RefreshRoutesEvent(this));

        return "success";
    }

    /**
     * 更新路由定义
     *
     * @param routeDefinitionList
     * @return
     */
    public String updateList(List<RouteDefinition> routeDefinitionList) {

        log.info("更新网关路由所有信息:{}", JSON.toJSONString(routeDefinitionList));

        // 先拿到当前 Gateway 中存储的路由定义
        List<RouteDefinition> routeDefinitionsExits = routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
        if (!CollectionUtils.isEmpty(routeDefinitionsExits)) {
            // 清除掉之前所有的 "旧的" 路由定义
            routeDefinitionsExits.forEach(rd -> {
                log.info("删除路由信息:{}", rd);
                deleteById(rd.getId());
            });
        }

        // 把更新的路由定义同步到 gateway 中
        routeDefinitionList.forEach(definition -> updateByRouteDefinition(definition));


        return "更新路由成功!!";
    }

    /**
     * 根据路由 id 删除路由配置
     *
     * @param id
     * @return
     */
    private String deleteById(String id) {

        try {
            log.info("删除网关路由ID:{}", id);
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            // 发布事件通知给 gateway 更新路由定义
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "删除路由成功!!";

        } catch (Exception ex) {
            log.error("网关路由删除失败: {}", ex.getMessage(), ex);
            return "删除失败!!";
        }
    }


    /**
     * 更新路由
     * <p>
     * 更新的实现策略比较简单: 删除 + 新增 = 更新
     *
     * @param definition
     * @return
     */
    private String updateByRouteDefinition(RouteDefinition definition) {

        try {
            log.info("更新网关路由: {}", definition);
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));

        } catch (Exception ex) {
            return "更新失败,没有找对路由ID: " + definition.getId();
        }

        try {

            this.routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "更新路由成功!!";

        } catch (Exception ex) {
            return "更新路由失败!!";
        }


    }


}
