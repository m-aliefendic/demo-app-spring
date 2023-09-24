package com.ba.demo.rest.user;

import com.ba.demo.api.model.token.TokenRequestDTO;
import com.ba.demo.api.model.token.TokenResponseDTO;
import com.ba.demo.core.service.TokenService;
import io.swagger.annotations.Api;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("public/web-user/login")
@Api(tags = {"web-login"})
@RequiredArgsConstructor
public class WebUserLoginController {
    @NonNull
   private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody TokenRequestDTO tokenDTO) throws BadCredentialsException {
        return ResponseEntity.ok(tokenService.createUserToken(tokenDTO));
    }



}
