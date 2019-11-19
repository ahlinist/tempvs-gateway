package club.tempvs.gateway.filter;

import club.tempvs.gateway.helper.CookieHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CookieFilter implements GlobalFilter {

    private static final String REFRESH_COOKIES_HEADER = "Tempvs-Refresh-Cookies";

    private final CookieHelper cookieHelper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.fromRunnable(refreshCookies(exchange)));
    }

    Runnable refreshCookies(ServerWebExchange exchange) {
        return () -> {
            ServerHttpResponse response = exchange.getResponse();
            String userInfo = response.getHeaders()
                    .getFirst(REFRESH_COOKIES_HEADER);

            if (userInfo != null) {
                response.addCookie(cookieHelper.buildAuthCookie(userInfo));
                response.addCookie(cookieHelper.buildLoggedInCookie());
            }
        };
    }
}
