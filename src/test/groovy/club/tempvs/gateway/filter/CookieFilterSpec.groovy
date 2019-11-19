package club.tempvs.gateway.filter

import club.tempvs.gateway.helper.CookieHelper
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration

class CookieFilterSpec extends Specification {

    def cookieHelper = Mock CookieHelper

    @Subject
    CookieFilter cookieFilter = new CookieFilter(cookieHelper)

    def exchange = Mock ServerWebExchange
    def headers = Mock HttpHeaders
    def response = Mock ServerHttpResponse

    def "cookieFilter refreshes cookies when a proper header is passed"() {
        given:
        ResponseCookie authCookie = ResponseCookie.from("TEMPVS_AUTH", "")
                .httpOnly(true)
                .maxAge(Duration.ofDays(30))
                .path("/")
                .build()
        ResponseCookie loggedInCookie = ResponseCookie.from("TEMPVS_LOGGED_IN", "")
                .maxAge(Duration.ofDays(30))
                .path("/")
                .build()
        String userInfo = 'user info'
        Runnable refreshCookieRunnable = cookieFilter.refreshCookies(exchange)

        when:
        refreshCookieRunnable.run()

        then:
        1 * exchange.response >> response
        1 * response.headers >> headers
        1 * headers.getFirst("Tempvs-Refresh-Cookies") >> userInfo
        1 * cookieHelper.buildAuthCookie(userInfo) >> authCookie
        1 * cookieHelper.buildLoggedInCookie() >> loggedInCookie
        1 * response.addCookie(authCookie)
        1 * response.addCookie(loggedInCookie)
        0 * _
    }

    def "cookieFilter doesn't refresh cookies"() {
        given:
        Runnable refreshCookieRunnable = cookieFilter.refreshCookies(exchange)

        when:
        refreshCookieRunnable.run()

        then:
        1 * exchange.response >> response
        1 * response.headers >> headers
        1 * headers.getFirst("Tempvs-Refresh-Cookies") >> null
        0 * _
    }
}
