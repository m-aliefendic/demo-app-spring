package com.ba.demo.configuration;

import com.ba.demo.api.model.Problem;
import com.ba.demo.core.utils.ErrorCode;
import com.ba.demo.core.utils.MessagesKey;
import com.ba.demo.core.utils.SpecificException;
import com.ba.demo.service.InternationalizatonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthenticationFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
  private static final String BEARER_KEY = "Bearer ";

  private final AuthenticationManager authenticationManager;
  private final AuthenticationEntryPoint authenticationEntryPoint;

  ObjectMapper objectMapper;
  InternationalizatonService internationalizatonService;

  public AuthenticationFilter(
      final AuthenticationManager authenticationManager,
      final AuthenticationEntryPoint authenticationEntryPoint,
      ObjectMapper objectMapper,
      InternationalizatonService internationalizatonService) {
    this.authenticationManager = authenticationManager;
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.objectMapper = objectMapper;
    this.internationalizatonService = internationalizatonService;
  }

  @Override
  protected void doFilterInternal(
      final HttpServletRequest httpRequest,
      final HttpServletResponse httpResponse,
      final FilterChain filterChain)
      throws ServletException, IOException {
    try {
      LOGGER.debug("Filtering user Auth...");
      String token =
          Optional.ofNullable(httpRequest.getHeader(HttpHeaders.AUTHORIZATION))
              .filter((t) -> t.contains(BEARER_KEY))
              .map(
                  (t) -> {
                    return t.replace(BEARER_KEY, StringUtil.EMPTY_STRING);
                  })
              .orElseThrow(
                  () ->
                      new SpecificException(
                          ErrorCode.UNAUTHORIZED, MessagesKey.require_authentication));
      LOGGER.debug("Token validating: {}", token);
      final Authentication responseAuthentication =
          authenticationManager.authenticate(
              new PreAuthenticatedAuthenticationToken(token, null, Lists.newArrayList()));
      SecurityContextHolder.getContext().setAuthentication(responseAuthentication);
      if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
        throw new SpecificException(ErrorCode.UNAUTHORIZED, MessagesKey.require_authentication);
      }
      LOGGER.debug("Token validated.");
    } catch (final AuthenticationException e) {
      LOGGER.error("Error auth : {}", e.getMessage());
      SecurityContextHolder.clearContext();
      authenticationEntryPoint.commence(httpRequest, httpResponse, e);
      return;
    } catch (SpecificException e) {
      writeErrorResponse(httpResponse, e.getErrorCode(), e.getMessageKey(), e.getMessageTokens());
      return;
    } catch (Exception e) {
      writeGeneralErrorResponse(httpResponse, e.getMessage());
      return;
    }

    filterChain.doFilter(httpRequest, httpResponse);
  }

  private void writeErrorResponse(
      HttpServletResponse response,
      ErrorCode errorCode,
      MessagesKey messageKey,
      Object... messageTokens) {
    Problem error =
        new Problem(
            errorCode.getMessage(),
            internationalizatonService.get(messageKey.getName(), messageTokens),
            null);
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
    response.setHeader(
        "Access-Control-Allow-Headers",
        "Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Accept-Language, Host, Referer, Connection, User-Agent, authorization, sw-useragent, sw-version");
    error.writeAsResponse(response, objectMapper, HttpStatus.SC_UNAUTHORIZED);
  }

  private void writeGeneralErrorResponse(HttpServletResponse response, String msg) {
    Problem error = new Problem(ErrorCode.UNAUTHORIZED.getMessage(), msg, null);
    error.writeAsResponse(response, objectMapper, HttpStatus.SC_UNAUTHORIZED);
  }
}
