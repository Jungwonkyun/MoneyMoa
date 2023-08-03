package com.d210.moneymoa.config;

import com.d210.moneymoa.domain.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        logger.info("path info: "+ path);

        if (path.startsWith("/login") ||path.startsWith("/signup")||path.startsWith("/findpassword")||path.startsWith("/emailauth")
                ||path.startsWith("/swagger-ui")||path.startsWith("/v2")||path.startsWith("/webjars")|| path.startsWith("/swagger-resources")
                ||path.startsWith("/file")){
            logger.info("얘는 필터에 들어가지 않음");
            filterChain.doFilter(request, response);
            return;
        }

        String token = tokenProvider.resolveToken(request);

        logger.info(token);

        if (token != null && tokenProvider.validateToken(token)) {
            Authentication auth = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}