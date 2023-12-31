package com.ba.demo.configuration;

import com.ba.demo.core.utils.Utils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

public class LoggerFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFilter.class);
  private static final String X_REQUEST_ID = "x-request-id";

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain)
      throws ServletException, IOException {

    String requestId = getRequestId(httpRequest);
    MDC.put("requestId", requestId);
    MDC.put("userId", httpRequest.getHeader("UserId"));
    MDC.put("sessionId ", httpRequest.getHeader("SessionId"));
    httpResponse.setHeader(X_REQUEST_ID, requestId);
    filterChain.doFilter(httpRequest, httpResponse);
    MDC.remove("requestId");
    MDC.remove("userId");
    MDC.remove("sessionId");
  }

  private String getRequestId(HttpServletRequest httpRequest) {
    return new StringBuilder()
        .append(httpRequest.getHeader(X_REQUEST_ID))
        .append(Utils.generateRandomText(10))
        .toString();
  }
}
