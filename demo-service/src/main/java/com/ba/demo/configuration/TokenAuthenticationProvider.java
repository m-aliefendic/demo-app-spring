package com.ba.demo.configuration;


import com.ba.demo.api.model.user.UserDTO;
import com.ba.demo.api.model.user.exception.UserNotFoundException;
import com.ba.demo.core.service.TokenService;
import com.ba.demo.core.utils.ErrorCode;
import com.ba.demo.core.utils.SpecificException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import com.ba.demo.core.utils.MessagesKey;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationProvider.class);

    @NonNull
    private final TokenService tokenService;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String jwt = readToken(authentication);
        try {
            var response = tokenService.verifyLoginToken(jwt);
            UserDTO userDTO = response.getKey();
            Collection<? extends GrantedAuthority> authorities  = response.getValue();
            LOGGER.debug("Setting user : {}", userDTO);
            return createAuthentication(userDTO, authorities);
        } catch (UserNotFoundException | ExecutionException e) {
            throw new AuthenticationCredentialsNotFoundException(e.getMessage(), e);
        }
    }

    private Authentication createAuthentication(UserDTO userDTO, Collection<? extends GrantedAuthority> authorities) {
        AbstractAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDTO, null, authorities);
        result.setDetails(userDTO);
        return result;
    }

    protected String readToken(Authentication authentication) {
        LOGGER.debug("Reading auth data.");
        return Optional
                .of(authentication.getPrincipal())
                .filter(principal -> principal instanceof String)
                .map(principal -> (String) principal)
                .filter(principal -> !principal.isEmpty())
                .orElseThrow(() -> new SpecificException(ErrorCode.UNAUTHORIZED, MessagesKey.invalid_token));
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}

