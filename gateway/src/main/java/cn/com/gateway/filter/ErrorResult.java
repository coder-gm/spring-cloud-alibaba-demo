package cn.com.gateway.filter;

import cn.com.common.handle.exception.BusinessException;
import com.alibaba.nacos.common.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/9 17:25
 */
@Slf4j
public class ErrorResult {


    /**
     * 网关拦截错误结果返回
     *
     * @Author: zgming
     * @Param1: exchange
     * @Param2: chain
     * @Return reactor.core.publisher.Mono<java.lang.Void>
     * @Throws:
     **/
    public static Mono<Void> responseData(ServerWebExchange exchange, GatewayFilterChain chain, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        // 封装错误信息
        Map<String, Object> responseData = Maps.newHashMap();
        responseData.put("status", 401);
        responseData.put("ret", -1);
        responseData.put("msg", msg);

        try {
            // 将信息转换为 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] data = objectMapper.writeValueAsBytes(responseData);

            // 输出错误信息到页面
            DataBuffer buffer = response.bufferFactory().wrap(data);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return chain.filter(exchange);
    }


    /**
     * 返回response
     *
     * @param exchange
     * @param message  异常信息
     * @param status   data中的status
     * @return
     */
    public static Mono<Void> errorInfo(ServerWebExchange exchange, String message, Integer status) {
        // 自定义返回格式
        Map<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("ret", status == null ? 2 : status);
        resultMap.put("message", StringUtils.isBlank(message) ? "服务异常！" : message);
        resultMap.put("info", null);
        resultMap.put("data", null);
        return Mono.defer(() -> {
            byte[] bytes;
            try {
                bytes = new ObjectMapper().writeValueAsBytes(resultMap);
            } catch (JsonProcessingException e) {
                log.error("网关响应异常：", e);
                throw new BusinessException("信息序列化异常");
            } catch (Exception e) {
                log.error("网关响应异常：", e);
                throw new BusinessException("写入响应异常");
            }
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString());
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
        });
    }


}
