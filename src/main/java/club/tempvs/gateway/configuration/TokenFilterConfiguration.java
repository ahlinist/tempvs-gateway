package club.tempvs.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;

@Configuration
public class TokenFilterConfiguration {

    private static final String CHAR_ENCODING = "UTF-8";
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private String token;

    public TokenFilterConfiguration(@Value("${authorization.token}") String token) {
        this.token = token;
    }

    @Bean
    public GlobalFilter tokenFilter() throws Exception {
        byte[] tokenBytes = token.getBytes(CHAR_ENCODING);
        String tokenHash = DigestUtils.md5DigestAsHex(tokenBytes);

        return (exchange, chain) -> {
            exchange.getRequest()
                    .mutate()
                    .header(AUTHORIZATION_HEADER_NAME, tokenHash)
                    .build();
            return chain.filter(exchange);
        };
    }
}
