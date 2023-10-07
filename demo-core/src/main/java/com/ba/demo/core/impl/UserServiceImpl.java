package com.ba.demo.core.impl;

import com.ba.demo.api.model.user.UserActivationDTO;
import com.ba.demo.api.model.user.UserDTO;
import com.ba.demo.api.model.user.exception.UserException;
import com.ba.demo.api.model.user.exception.UserNotFoundException;
import com.ba.demo.core.config.TokenConfig;
import com.ba.demo.core.service.SaltService;
import com.ba.demo.core.utils.ErrorCode;
import com.ba.demo.core.utils.MessagesKey;
import com.ba.demo.core.utils.SpecificException;
import com.ba.demo.dao.maper.UserMapper;
import com.ba.demo.dao.model.user.UserEntity;
import com.ba.demo.dao.repository.UserRepository;
import com.ba.demo.service.EmailService;
import com.ba.demo.service.InternationalizatonService;
import com.ba.demo.service.TimeService;
import com.ba.demo.serviceinterface.UserService;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
  @NotNull private final Supplier<String> saltSupplier = SaltService::random;
  @NotNull private final UserRepository abstractUserRepository;
  @NotNull private final UserMapper userMapper;
  @NotNull private final InternationalizatonService internationalizatonService;
  @NotNull private final UserRepository userRepository;
  @NotNull private final SaltService saltService;
  @NotNull private final EmailService emailService;
  @NotNull private final TokenConfig tokenConfig;
  @NotNull private final TimeService timeService;

  @Override
  public UserDTO startRegistration(UserDTO userEntity) throws UserException {
    if (abstractUserRepository.findByEmail(userEntity.getEmail()).isPresent()) {
      throw new SpecificException(ErrorCode.CONFLICT, MessagesKey.email_already_exists);
    }

    UserEntity user = userMapper.toEntity(userEntity);
    user.setId(UUID.randomUUID());
    user.setActivationToken(UUID.randomUUID());
    user.setSalt(this.saltSupplier.get());
    user.setActivationExpiresOn(getActivationExpirationDate());
    setPassword(user, userEntity.getPassword());
    user = userRepository.save(user);

    if (user != null) {
      LOGGER.debug("Sending registration email: {} -- {}.", user.getEmail(), user);
      UserDTO userDTO = userMapper.fromEntity(user);
      this.sendRegistrationEmail(userDTO.getEmail(), user.getActivationToken(), 1, "");
      return userDTO;
    }
    String msg = internationalizatonService.get("registration_not_started");
    throw new UserException(msg);
  }

  @Transactional
  @Override
  public UserDTO completeRegistration(UserActivationDTO userActivationDTO)
      throws UserNotFoundException {
    LOGGER.debug("Completing activation: {}", userActivationDTO);
    UserEntity user = this.checkTokenValidation(userActivationDTO);
    user.setActive(Boolean.TRUE);
    user.setActivationExpiresOn(null);
    user.setActivationToken(null);
    abstractUserRepository.save(user);

    Optional<UserEntity> company = abstractUserRepository.findById(user.getId());
    if (!company.isPresent()) {
      String msg =
          internationalizatonService.get("not_found", userActivationDTO.getActivationToken());
      throw new UserNotFoundException(msg);
    }

    UserDTO userDTO = userMapper.fromEntity(company.get());
    return userDTO;
  }

  @Override
  public List<UserDTO> getUsers() throws UserException {
    Iterable<UserEntity> userEntities = userRepository.findAll();

    List<UserDTO> users = new ArrayList<>();

    for (UserEntity u : userEntities) {
      users.add(userMapper.fromEntity(u));
    }

    return users;
  }

  private void setPassword(UserEntity u, String passwordString) {
    Optional.ofNullable(passwordString)
        .ifPresent(
            v -> {
              String password = this.saltService.apply(u.getSalt(), v);
              u.setPassword(password);
            });
  }

  private void sendRegistrationEmail(
      String email, UUID activationToken, int language, String callbackUrl) {
    String subject =
        internationalizatonService.getByLanguageId(language, "email_verification_subject");
    String content =
        internationalizatonService.getByLanguageId(language, "email_verification_body_web");
    String buttonText = internationalizatonService.getByLanguageId(language, "confirm");
    Map<String, Object> params = new HashMap<>();
    params.put("title", subject);
    params.put("body", content);
    params.put("url", "http://localhost:5000/public/web-user/registration/confirm");
    params.put("buttonText", buttonText);
    params.put("email", "meho.aliefendic@gmail.com");
    params.put("activationToken", activationToken.toString());
    this.emailService.sendNotification(
        email, subject, "templates/email/confirmation.code.html", params);
  }

  private UserEntity checkTokenValidation(UserActivationDTO userDTO) throws UserNotFoundException {
    Optional<UserEntity> user =
        this.abstractUserRepository.findByEmailAndActivationToken(
            userDTO.getEmail(), UUID.fromString(userDTO.getActivationToken()));
    user.filter(u -> u.getActivationExpiresOn().isAfter(this.timeService.now()));
    String msg = internationalizatonService.get("not_found", userDTO.getActivationToken());
    return user.orElseThrow(() -> new UserNotFoundException(msg));
  }

  private LocalDateTime getActivationExpirationDate() {
    return this.timeService.after(this.tokenConfig.activationTokenTtlDays());
  }
}
