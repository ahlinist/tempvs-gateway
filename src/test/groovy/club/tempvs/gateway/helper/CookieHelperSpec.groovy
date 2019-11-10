package club.tempvs.gateway.helper

import org.springframework.http.HttpCookie
import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration

class CookieHelperSpec extends Specification {

    private static final String AUTH_COOKIE_NAME = "TEMPVS_AUTH"
    private static final String COOKIE_ROOT_PATH = "/"
    private static final String LOGGED_IN_COOKIE_NAME = "TEMPVS_LOGGED_IN";
    private static final String LOGGED_IN_COOKIE_VALUE = "true";

    @Subject
    private CookieHelper cookieHelper = new CookieHelper()

    def "build auth cookie"() {
        given:
        String userInfo = 'user info'
        String encodedUserInfo = 'dXNlciBpbmZv'

        when:
        HttpCookie result = cookieHelper.buildAuthCookie(userInfo)

        then:
        result.name == AUTH_COOKIE_NAME
        result.value == encodedUserInfo
        result.maxAge == Duration.ofDays(30)
        result.path == COOKIE_ROOT_PATH
        result.httpOnly
        !result.secure
    }

    def "clear auth cookie"() {
        when:
        HttpCookie result = cookieHelper.clearAuthCookie()

        then:
        result.name == AUTH_COOKIE_NAME
        !result.value
        result.maxAge == Duration.ZERO
        result.path == COOKIE_ROOT_PATH
        result.httpOnly
        !result.secure
    }

    def "build logged in cookie"() {
        when:
        HttpCookie result = cookieHelper.buildLoggedInCookie()

        then:
        result.name == LOGGED_IN_COOKIE_NAME
        result.value == LOGGED_IN_COOKIE_VALUE
        result.maxAge == Duration.ofDays(30)
        result.path == COOKIE_ROOT_PATH
        !result.httpOnly
        !result.secure
    }

    def "clear logged in cookie"() {
        when:
        HttpCookie result = cookieHelper.clearLoggedInCookie()

        and:
        0 * _

        then:
        result.name == LOGGED_IN_COOKIE_NAME
        !result.value
        result.maxAge == Duration.ZERO
        result.path == COOKIE_ROOT_PATH
        !result.httpOnly
        !result.secure
    }
}
