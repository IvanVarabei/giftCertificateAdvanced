package com.epam.esm.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Delegates exceptions from filters to the {@link com.epam.esm.exception.ErrorControllerAdvice}.
 * Should be the first in the filter chain.
 */
@Component
public class FilterChainExceptionHandlerFilter extends OncePerRequestFilter {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest req, @NonNull HttpServletResponse resp,
                                    @NonNull FilterChain filterChain) {
        try {
            filterChain.doFilter(req, resp);
        } catch (Exception e) {
            resolver.resolveException(req, resp, null, e);
        }
    }
}