package com.ba.demo.core.service;

import com.ba.demo.api.model.user.Role;
import com.ba.demo.api.model.user.UserDTO;
import com.ba.demo.core.config.TokenConfig;
import com.ba.demo.dao.model.token.TokenEntity;
import com.ba.demo.dao.model.user.RoleEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtService {

  private final Key key;
  private UUID overriddenId;
  private final ExecutorService executor = Executors.newFixedThreadPool(10);

  @Autowired
  public JwtService(final TokenConfig config) {
    this.key =
        new SecretKeySpec(
            Objects.requireNonNull(config).jwtSecretKey().getBytes(),
            SignatureAlgorithm.HS512.getJcaName());
  }

  public String create(final TokenEntity token, final Set<RoleEntity> role) {

    return Jwts.builder()
        .setSubject(token.getId().toString())
        .setId(token.getUserId().toString())
        // check
        .setAudience(role.stream().findFirst().get().getRoleName())
        .setExpiration(Date.from(token.getExpiresOn().atZone(ZoneId.systemDefault()).toInstant()))
        .signWith(SignatureAlgorithm.HS512, key)
        .compact();
  }

  public Optional<UUID> parse(final String jwt) {
    return Optional.ofNullable(
            Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody().getSubject())
        .map(UUID::fromString);
  }

  public UUID getUUIDIdFromToken() {
    if (overriddenId != null) return overriddenId;
    Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
    UserDTO userDetails = (UserDTO) currentUser.getDetails();
    return userDetails.getId();
  }

  public void setUserId(UUID id) {
    overriddenId = id;
  }

  public UUID getIdFromToken() {
    if (overriddenId != null) return overriddenId;
    Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
    UserDTO userDetails = (UserDTO) currentUser.getDetails();
    return userDetails.getId();
  }

  //    public void checkPermission(Identity userId) {
  //        if (getUUIDIdFromToken().equals(Identity.convertToUUID(userId)))
  //            return;
  //        throw new SpecificException(ErrorCode.FORBIDDEN, MessagesKey.forbidden_user);
  //    }

  public UserDTO getUserContext() {
    Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
    return (UserDTO) currentUser.getDetails();
  }

  private String getUserType(Role role) {
    String userType;
    if (role == null || role == Role.user) userType = Role.user.toString();
    else if (role == Role.company) userType = Role.company.toString();
    else userType = "admin";

    return userType;
  }
}
