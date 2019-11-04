package club.tempvs.gateway.filter

import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.http.HttpCookie
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.server.ServerWebExchange
import spock.lang.Specification
import spock.lang.Subject

class AuthFilterSpec extends Specification {

    private static final String AUTH_COOKIE_NAME = "TEMPVS_AUTH"
    private static final String USER_INFO_HEADER_NAME = "User-Info"

    @Subject AuthFilter authFilter

    def exchange = Mock ServerWebExchange
    def chain = Mock GatewayFilterChain

    def request = Mock ServerHttpRequest
    def mutatedRequest = Mock ServerHttpRequest
    def requestBuilder = Mock ServerHttpRequest.Builder
    def cookie = Mock HttpCookie

    def setup() {
        authFilter = new AuthFilter()
    }

    def "authFilter adds 'User-Info' header downstream"() {
        given:
        MultiValueMap<String, HttpCookie> cookies = [(AUTH_COOKIE_NAME): [cookie]] as LinkedMultiValueMap
        String userInfoValue = 'my-cookie'
        String authCookieValue = userInfoValue.bytes.encodeBase64().toString()

        when:
        authFilter.filter(exchange, chain)

        then:
        1 * exchange.request >> request
        1 * request.cookies >> cookies
        1 * cookie.value >> authCookieValue
        1 * request.mutate() >> requestBuilder
        1 * requestBuilder.header(USER_INFO_HEADER_NAME, userInfoValue) >> requestBuilder
        1 * requestBuilder.build() >> mutatedRequest
        1 * chain.filter(exchange)
        0 * _
    }
}
