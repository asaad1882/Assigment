package com.daleel.student.ms.confiuration;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.daleel.student.ms.entity.APIResponse;
import com.daleel.student.ms.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Order(OrderedFilter.HIGHEST_PRECEDENCE)
public class AuthenticationHandlerFilter implements Filter{
	private static final Logger log = LoggerFactory.getLogger(AuthenticationHandlerFilter.class);
	private String apiKeyVal;
	 public static final String AUTH_KEY="X-API-KEY";
    public AuthenticationHandlerFilter(String apiKeyVal) {
        this.apiKeyVal=apiKeyVal;
    }
    @Override
    public  void init(FilterConfig filterConfig) throws ServletException{
        log.info("Initializing filter :{}",this);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        log.info(
                "Starting a transaction for req : {}, {}", 
                httpServletRequest.getRequestURI(),apiKeyVal);
        final String apiKey= httpServletRequest.getHeader(AUTH_KEY);
        try {
            if (apiKey!=null && apiKeyVal.equals(apiKey)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }else{
                throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), "Authorization header is invalid/not found");
            }
        } catch (BusinessException e) {

            // custom error response class used across my project
            APIResponse errorResponse = new APIResponse(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.getWriter().write(convertObjectToJson(errorResponse));
    }
}

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
