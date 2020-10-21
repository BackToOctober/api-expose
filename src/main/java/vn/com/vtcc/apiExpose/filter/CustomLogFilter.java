package vn.com.vtcc.apiExpose.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import vn.com.vtcc.apiExpose.utils.IDGenerator;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


public class CustomLogFilter implements Filter {

    public static final Logger logger = LoggerFactory.getLogger(CustomLogFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestTokenId = IDGenerator.genID();
        request.setAttribute("requestTokenId", requestTokenId);

        ContentCachingRequestWrapper wReq = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wRes = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(wReq, wRes);

        // 1. build log request
        Map<String, String> parameters = buildParametersMap(wReq);

        StringBuilder requestStringBuilder = new StringBuilder();
        requestStringBuilder.append("request requestTokenId=[").append(requestTokenId).append("] ");
        requestStringBuilder.append("method=[").append(wReq.getMethod()).append("] ");
        requestStringBuilder.append("path=[").append(wReq.getRequestURI()).append("] ");
        requestStringBuilder.append("headers=[").append(buildHeadersMap(wReq)).append("] ");
        if (!parameters.isEmpty()) {
            requestStringBuilder.append("parameters=[").append(parameters).append("] ");
        }
        String body = new String(wReq.getContentAsByteArray());
        requestStringBuilder.append("body=[").append(body).append("]");
        logger.info(requestStringBuilder.toString());

        // 2. build log response
        StringBuilder responseStringBuilder = new StringBuilder();
        responseStringBuilder.append("response requestTokenId=[").append(requestTokenId).append("] ");
        responseStringBuilder.append("path=[").append(wReq.getRequestURI()).append("] ");
        responseStringBuilder.append("statusHttp=[").append(wRes.getStatus()).append("]");
        logger.info(responseStringBuilder.toString());

        wRes.copyBodyToResponse();
    }


    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    @Override
    public void destroy() {

    }
}
