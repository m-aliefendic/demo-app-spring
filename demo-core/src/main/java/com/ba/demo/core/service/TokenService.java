package com.ba.demo.core.service;

import com.ba.demo.api.model.token.TokenRequestDTO;
import com.ba.demo.api.model.token.TokenResponseDTO;
import com.ba.demo.api.model.user.RoleDTO;
import com.ba.demo.api.model.user.UserDTO;
import com.ba.demo.api.model.user.exception.UserNotFoundException;
import com.ba.demo.core.config.TokenConfig;
import com.ba.demo.core.utils.ErrorCode;
import com.ba.demo.core.utils.MessagesKey;
import com.ba.demo.core.utils.SpecificException;
import com.ba.demo.dao.model.user.UserEntity;
//import com.ba.demo.dao.model.token.Token;
import com.ba.demo.dao.model.token.TokenEntity;
import com.ba.demo.dao.repository.CustomUserRepository;
import com.ba.demo.dao.repository.TokenRepository;
import com.ba.demo.dao.repository.UserRepository;
import com.ba.demo.service.internationalization.InternationalizatonService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.flywaydb.core.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TokenService {

    @NonNull
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);
    @NonNull
    private final JwtService jwtService;
    @NonNull
    private final TimeService timeService;
    @NonNull
    private final Supplier<UUID> identitySupplier = () -> UUID.randomUUID();
    @NonNull
    private final TokenRepository tokenRepository;
    @NonNull
    private final TokenConfig config;
    @NonNull
    private final SaltService saltService;
    @NonNull
    private final UserRepository abstractUserRepository;
    @NonNull
    private final InternationalizatonService internationalizatonService;

    @NonNull
    private final CustomUserRepository genericUserService;


    private  LoadingCache<UUID, Optional<UUID>> currentUsers;

    @PostConstruct
    public void init() {
        currentUsers = CacheBuilder.<UUID, Optional<UUID>>newBuilder()
                .expireAfterWrite(config.tokenCacheTtlSeconds().getSeconds(), TimeUnit.SECONDS)
                .maximumSize(config.tokenCacheMaxSize())
                .build(new CacheLoader<UUID, Optional<UUID>>() {
                    @Override
                    public Optional<UUID> load(final UUID tokenId) throws Exception {
                        return tokenRepository.findById(tokenId)
                                .filter(token -> timeService.isFuture(token.getExpiresOn()))
                                .map(TokenEntity::getUserId);
                    }
                });

    }



    @Transactional(readOnly = true)
    public MutablePair<UserDTO, Collection<? extends GrantedAuthority>>  verifyLoginToken(final String tokenEncrypted) throws UserNotFoundException, ExecutionException {
        LOGGER.debug("Verifying token : {}***", StringUtils.left(tokenEncrypted, 3));
        Optional<UUID> parse = jwtService.parse(tokenEncrypted);
        if (parse.isPresent()) {
            Optional<UUID> userId = this.currentUsers.get(parse.get());
            if (userId.isPresent()) {
                LOGGER.debug("Getting user : {}", userId.get());
                return this.genericUserService.findAbstractUser(userId.get());
            }
        }
        LOGGER.debug("User not found.");
        String msg = internationalizatonService.get("not_found", tokenEncrypted);
        throw new UserNotFoundException(msg);
    }

    @Transactional
    public TokenResponseDTO createUserToken(final TokenRequestDTO request) throws BadCredentialsException {
        LOGGER.debug("Loging in user: {}", request);
        Optional<UserEntity> byUserName = this.abstractUserRepository.findByEmail(request.getEmail());

        if (!byUserName.isPresent())
            throw new SpecificException(ErrorCode.UNAUTHORIZED, MessagesKey.bad_username);

        return byUserName
                .filter(user -> this.saltService.check(user.getSalt(), user.getPassword(), request.getPassword()))
                .map(user -> {
                    LOGGER.debug("Getting token for user: {}", user);
                    return this.findOrCreateToken(user);
                })
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("Incorrect credentials."));
    }


//    @Transactional(readOnly = true)
//    public UUID verifyForgotToken(final String tokenString) throws UserNotFoundException {
//        Optional<TokenForgot> token = this.tokenForgotRepository.findById(Identity.unsafeFromString(tokenString));
//        if (token.isPresent() && token.get().getExpiresOn().isAfter(this.timeService.now())) {
//            return token.get().getUserId();
//        }
//        String msg = internationalizatonService.get("not_found", tokenString);
//        throw new UserNotFoundException(msg);
//    }


//    @Transactional
//    public void createUserForgotToken(ForgotRequestDTO request, Boolean isWebUser) throws UserNotFoundException {
//        LOGGER.debug("Creating user forgot token: {}", request);
//        Optional<AbstractUser> byUserName = this.abstractUserRepository.findByEmail(request.getEmail());
//        String msg = internationalizatonService.get("not_found", request.getEmail());
//        TokenResponseDTO result = byUserName
//                .map(user -> {
//                    TokenForgot tokenForgot;
//                    if (isWebUser)
//                        tokenForgot = new TokenForgot(identitySupplier.get(), user.getId(), timeService.now(), timeService.after(config.activationTokenTtlDays()), isWebUser, null);
//                    else
//                        tokenForgot = new TokenForgot(identitySupplier.get(), user.getId(), timeService.now(), timeService.after(config.activationNumberTtlSeconds()), isWebUser, Utils.generateRandomNumber(this.config.getActivationNumberLength()));
//                    LOGGER.debug("Saving new forgot token: {}", tokenForgot);
//                    tokenForgot = tokenForgotRepository.save(tokenForgot);
//
//                    if (isWebUser)
//                        return new TokenResponseDTO(tokenForgot.getId().stringValue());
//                    else
//                        return new TokenResponseDTO(tokenForgot.getActivationNumber());
//
//                })
//                .orElseThrow(() -> new UserNotFoundException(msg));
//
//
//        String subject = internationalizatonService.getByLanguageId(byUserName.get().getLanguage(), "recovery_password_subject");
//
//        if (isWebUser) {
//            String content = internationalizatonService.getByLanguageId(byUserName.get().getLanguage(), "code_verification_body_web");
//            String buttonText = internationalizatonService.getByLanguageId(byUserName.get().getLanguage(), "confirm");
//            String tokenUrl = request.getCallbackUrl() + result.getToken();
//            Map<String, Object> params = ImmutableMap.of("title", subject,
//                    "body", content, "url", tokenUrl, "buttonText", buttonText);
//            this.emailService.sendNotification(request.getEmail(), subject, "templates/email/confirmation.code.html", params);
//        } else {
//            String content = internationalizatonService.getByLanguageId(byUserName.get().getLanguage(), "code_verification_body");
//            Map<String, Object> params = ImmutableMap.of("title", subject,
//                    "body", content, "confirmationCode", result.getToken());
//            emailService.sendNotification(request.getEmail(), subject, "templates/email/confirmation.code.mobile.html", params);
//        }
//    }


//    @Transactional
//    public void changePassword(ForgotChangePasswordRequestDTO requestDTO) throws UserNotFoundException {
//        LOGGER.debug("Changing forgotten pass: {} ", requestDTO);
//        Identity userId = this.verifyForgotToken(requestDTO.getToken());
//        this.tokenForgotRepository.deleteById(Identity.unsafeFromString(requestDTO.getToken()));
//        this.genericUserService.changePassword(userId, requestDTO.getPassword());
//    }




    private TokenResponseDTO findOrCreateToken(UserEntity user) {
        TokenEntity tkn = new TokenEntity(identitySupplier.get(), user.getId(), timeService.now(), timeService.after(config.tokenTtlDays()));
        tkn = tokenRepository.save(tkn);
        return new TokenResponseDTO(jwtService.create(tkn, user.getRoles()));
    }


}
