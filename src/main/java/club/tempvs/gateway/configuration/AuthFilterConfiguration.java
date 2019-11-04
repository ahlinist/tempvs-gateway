package club.tempvs.gateway.configuration;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Base64Utils;

import java.util.Optional;

@Configuration
public class AuthFilterConfiguration {

    private static final String AUTH_COOKIE_NAME = "TEMPVS_AUTH";
    private static final String USER_INFO_HEADER_NAME = "User-Info";

    @Bean
    public GlobalFilter authFilter() {

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            HttpCookie authCookie = request.getCookies()
                    .getFirst(AUTH_COOKIE_NAME);

            String userInfoValue = Optional.ofNullable(authCookie.getValue())
                    .map(Base64Utils::decodeFromString)
                    .map(String::new)
                    .orElse(null);

            request.mutate()
                    .header(USER_INFO_HEADER_NAME, userInfoValue)
                    .build();

            return chain.filter(exchange);
        };
    }
}
