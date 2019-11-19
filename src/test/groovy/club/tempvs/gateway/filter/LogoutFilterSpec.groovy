package club.tempvs.gateway.filter

import club.tempvs.gateway.helper.CookieHelper
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import spock.lang.Specification
import spock.lang.Subject

class LogoutFilterSpec extends Specification {

    def cookieHelper = Mock CookieHelper

    @Subject
    LogoutFilter logoutFilter = new LogoutFilter(cookieHelper)

    def exchange = Mock ServerWebExchange
    def headers = Mock HttpHeaders
    def response = Mock ServerHttpResponse

    def "logoutFilter removes auth and loggedIn cookies"() {
        given:
        ResponseCookie authCookie = ResponseCookie.from("TEMPVS_AUTH", null)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .build()
        ResponseCookie loggedInCookie = ResponseCookie.from("TEMPVS_LOGGED_IN", null)
                .maxAge(0)
                .path("/")
                .build()
        Runnable logoutRunnable = logoutFilter.clearCookies(exchange)

        when:
        logoutRunnable.run()

        then:
        1 * exchange.response >> response
        1 * response.headers >> headers
        1 * headers.getFirst("Tempvs-Logout") >> ""
        1 * cookieHelper.clearAuthCookie() >> authCookie
        1 * cookieHelper.clearLoggedInCookie() >> loggedInCookie
        1 * response.addCookie(authCookie)
        1 * response.addCookie(loggedInCookie)
        0 * _
    }

    def "logoutFilter keeps auth and loggedIn cookies intact"() {
        given:
        ResponseCookie authCookie = ResponseCookie.from("TEMPVS_AUTH", null)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .build()
        ResponseCookie loggedInCookie = ResponseCookie.from("TEMPVS_LOGGED_IN", null)
                .maxAge(0)
                .path("/")
                .build()
        Runnable logoutRunnable = logoutFilter.clearCookies(exchange)

        when:
        logoutRunnable.run()

        then:
        1 * exchange.response >> response
        1 * response.headers >> headers
        1 * headers.getFirst("Tempvs-Logout") >> null
        0 * _
    }
}
