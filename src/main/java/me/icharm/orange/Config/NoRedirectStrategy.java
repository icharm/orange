package me.icharm.orange.Config;

import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * implement redirectStrategy to no redirect in case of authentication failure
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/23 14:53
 */
public class NoRedirectStrategy implements RedirectStrategy {

    @Override
    public void sendRedirect(HttpServletRequest var1, HttpServletResponse var2, String var3) throws IOException {
        var2.setStatus(UNAUTHORIZED.value());
    }
}
