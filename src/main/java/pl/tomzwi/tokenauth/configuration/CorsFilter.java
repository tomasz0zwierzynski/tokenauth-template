package pl.tomzwi.tokenauth.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order( Ordered.HIGHEST_PRECEDENCE )
@Slf4j
public class CorsFilter implements Filter {

    @Override
    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        log.debug("CORS Filter - adding headers");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, Authorization");

        if ( request.getMethod().equals(HttpMethod.OPTIONS.name() ) ) {
            response.setStatus( HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void init( FilterConfig filterConfig ) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
