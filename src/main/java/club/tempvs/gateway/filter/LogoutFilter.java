package club.tempvs.gateway.filter;

import club.tempvs.gateway.helper.CookieHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Component
@RequiredArgsConstructor
public class LogoutFilter implements GlobalFilter {

    private static final String LOGOUT_HEADER = "Tempvs-Logout";

    private final CookieHelper cookieHelper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            String logoutHeader = response.getHeaders()
                    .getFirst(LOGOUT_HEADER);

            if (logoutHeader != null) {
                response.addCookie(cookieHelper.clearAuthCookie());
                response.addCookie(cookieHelper.clearLoggedInCookie());
            }
        }));
    }
}
