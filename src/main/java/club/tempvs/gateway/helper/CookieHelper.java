package club.tempvs.gateway.helper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CookieHelper {

    private static final String AUTH_COOKIE_NAME = "TEMPVS_AUTH";
    private static final String LOGGED_IN_COOKIE_NAME = "TEMPVS_LOGGED_IN";
    private static final String LOGGED_IN_COOKIE_VALUE = "true";
    private static final String COOKIE_ROOT_PATH = "/";

    private final CryptoHelper cryptoHelper;

    @SneakyThrows
    public ResponseCookie buildAuthCookie(String userInfo) {
        String encodedCookieValue = cryptoHelper.encrypt(userInfo);
        return ResponseCookie.from(AUTH_COOKIE_NAME, encodedCookieValue)
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .path(COOKIE_ROOT_PATH)
                .build();
    }

    public ResponseCookie clearAuthCookie() {
        return ResponseCookie.from(AUTH_COOKIE_NAME, null)
                .httpOnly(true)
                .secure(false)
                .maxAge(0)
                .path(COOKIE_ROOT_PATH)
                .build();
    }

    public ResponseCookie buildLoggedInCookie() {
        return ResponseCookie.from(LOGGED_IN_COOKIE_NAME, LOGGED_IN_COOKIE_VALUE)
                .httpOnly(false)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .path(COOKIE_ROOT_PATH)
                .build();
    }

    public ResponseCookie clearLoggedInCookie() {
        return ResponseCookie.from(LOGGED_IN_COOKIE_NAME, null)
                .httpOnly(false)
                .secure(false)
                .maxAge(0)
                .path(COOKIE_ROOT_PATH)
                .build();
    }
}
