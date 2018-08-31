package me.icharm.orange.Config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.removeStart;

/**
 * TokenAuthenticationFilter is responsible of extracting the authentication token from the request headers.
 * It takes the Authorization header value and attempts to extract the token from it.
 * After this filter, Authentication is then delegated to the AuthenticationManager.
 * The filter is only enabled for a given set of urls (SecurityConfig).
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/23 15:29
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String BEARER = "Bearer";

    TokenAuthenticationFilter(final RequestMatcher requestAuth) {
        super(requestAuth);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
        final String param = ofNullable(request.getHeader(AUTHORIZATION)).orElse(request.getHeader("t"));

        final String token = ofNullable(param)
                .map(value -> removeStart(value, BEARER))
                .map(String::trim)
                .orElseThrow(() -> new BadCredentialsException("Missing Authentication Token"));

        final Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
        return getAuthenticationManager().authenticate(auth);
    }

    @Override
    public void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication auth
    ) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, auth);
        chain.doFilter(request, response);
    }
}
