package com.blockeng.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author qiang
 */
@Configuration
public class GatewayConfig {

    //这里为支持的请求头，如果有自定义的header字段请自己添加（不知道为什么不能使用*）
    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,token,username,client";
    private static final String ALLOWED_METHODS = "*";
    private static final String ALLOWED_Expose = "*";
    private static final String MAX_AGE = "18000L";

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                String origin = request.getHeaders().getOrigin();

                headers.add("Access-Control-Allow-Origin", origin);
                headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                headers.add("Access-Control-Max-Age", MAX_AGE);
                headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
                headers.add("Access-Control-Expose-Headers", ALLOWED_Expose);
                headers.add("Access-Control-Allow-Credentials", "true");
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/v2/u/**")
                        .filters(f ->
                                f.rewritePath("/v2/u/(?<segment>.*)", "/$\\{segment}"))
                        .uri("lb://exchange-user-service")
                )
                .route(r -> r.path("/v2/m/**")
                        .filters(f ->
                                f.rewritePath("/v2/m/(?<segment>.*)", "/$\\{segment}"))
                        .uri("lb://exchange-mining")
                )
                .route(r -> r.path("/v2/s/trade/order/entrusts/**").and().method(HttpMethod.POST)
                        .filters(f ->
                                f.rewritePath("/v2/s/(?<segment>.*)", "/$\\{segment}"))
                        .uri("lb://exchange-trade-service")
                )
                .route(r -> r.path("/v2/s/trade/order/entrusts/**").and().method(HttpMethod.DELETE)
                        .filters(f ->
                                f.rewritePath("/v2/s/(?<segment>.*)", "/$\\{segment}"))
                        .uri("lb://exchange-trade-service")
                )
                .route(r -> r.path("/v2/s/trade/order/deal")
                        .filters(f ->
                                f.rewritePath("/v2/s/(?<segment>.*)", "/$\\{segment}"))
                        .uri("lb://exchange-trade-service")
                )
                .route(r -> r.path("/v2/s/**")
                        .filters(f ->
                                f.rewritePath("/v2/s/(?<segment>.*)", "/$\\{segment}"))
                        .uri("lb://exchange-service")
                ) .route(r -> r.path("/v2/o/**")
                        .filters(f ->
                                f.rewritePath("/v2/o/(?<segment>.*)", "/$\\{segment}"))
                        .uri("lb://exchange-openapi")
                ).route(r -> r.path("/v2/e/**")
                        .filters(f ->
                                f.rewritePath("/v2/e/(?<segment>.*)", "/$\\{segment}"))
                        .uri("lb://exchange-extend-gt210")
                )
                .build();
    }

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http.httpBasic().and()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/v2/s/coin/trade",
                        "/v2/s/config/getConfig",
                        "/v2/s/sms/sendTo",
                        "/v2/s/trade/market/refresh/**",
                        "/v2/s/api/v1/dm/verify",
                        "/v2/s/reward/register/**",
                        "/v2/s/reward/invite/**",
                        "/v2/s/sms/sendTo/**",
                        "/v2/u/v3/cards/selectUserBankByUserId",
                        "/v2/u/user/selectById",
                        "/v2/u/user/admin",
                        "/v2/u/user/loadUserByUsername").authenticated()
                .anyExchange().permitAll()
                .and()
                .build();
    }

    /*
    @Bean
    public FirewallGatewayFilter firewallGatewayFilter(){
        return new FirewallGatewayFilter();
    }*/
}
