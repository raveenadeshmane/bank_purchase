package org.bank.purchase.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/api/purchase")) {
            String correlationId = UUID.randomUUID().toString();
            MDC.put("correlationId", correlationId);

            long startTime = System.currentTimeMillis();
            try {
                LOGGER.info("Request started: {} {} [Client: {}]",
                        request.getMethod(),
                        request.getRequestURI(),
                        request.getRemoteAddr());

                filterChain.doFilter(request, response);
            } finally {
                long duration = System.currentTimeMillis() - startTime;
                LOGGER.info("Request completed: Status {} [Duration: {}ms]",
                        response.getStatus(),
                        duration);
                MDC.clear();
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
