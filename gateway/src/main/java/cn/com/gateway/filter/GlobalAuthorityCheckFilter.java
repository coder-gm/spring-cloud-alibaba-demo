package cn.com.gateway.filter;

import cn.com.common.handle.exception.BusinessException;
import cn.com.common.model.constant.CommonConstant;
import cn.com.common.model.jwt.UsernameAndPassword;
import cn.com.common.utils.security.TokenParseUtil;
import cn.com.gateway.constant.GatewayConstant;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局权限校验过滤器
 *
 * @author zgming
 */
@Slf4j
@Component
public class GlobalAuthorityCheckFilter implements GlobalFilter, Ordered {


    /**
     * 针对所有进来的接口进行权限校验
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String requestPath = request.getURI().getPath();
        log.info("请求路由:{}", requestPath);

        // 1. 如果是登录或者注册接口直接放行
        if (requestPath.contains(GatewayConstant.LOGIN_URI) || requestPath.contains(GatewayConstant.REGISTER_URI)) {
            // 解析通过, 则放行
            return chain.filter(exchange);
        }

        // 2. 访问其他的服务, 则鉴权, 校验是否能够从 Token 中解析出用户信息
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(CommonConstant.JWT_USER_INFO_KEY);
        UsernameAndPassword usernameAndPassword = null;

        try {
            //从 JWT Token 中解析 UsernameAndPassword 对象
            usernameAndPassword = TokenParseUtil.parseUserInfoFromToken(token);
        } catch (Exception ex) {
            log.error("从 JWT Token 中解析 UsernameAndPassword 对象错误: [{}]", ex.getMessage(), ex);
            return ErrorResult.errorInfo(exchange, "从 JWT Token 中解析 UsernameAndPassword 对象失败", -1);
        }

        // 获取不到登录用户信息, 返回 401
        if (null == usernameAndPassword) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 解析通过, 则放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }


}
