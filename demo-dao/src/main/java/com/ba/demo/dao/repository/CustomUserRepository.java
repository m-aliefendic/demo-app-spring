package com.ba.demo.dao.repository;


import com.ba.demo.api.model.user.RoleDTO;
import com.ba.demo.api.model.user.UserDTO;
import com.ba.demo.api.model.user.exception.UserNotFoundException;
import com.ba.demo.dao.maper.UserMapper;
import com.ba.demo.dao.model.user.UserEntity;
import com.ba.demo.service.internationalization.InternationalizatonService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.rmi.server.UID;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomUserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserRepository.class);
    @NotNull
    final private UserMapper userMapper;

    @NonNull
    final private UserRepository userRepository;
    @NonNull
    private final InternationalizatonService internationalizatonService;

    @Transactional(readOnly = true)
    public MutablePair<UserDTO, Collection<? extends GrantedAuthority>> findAbstractUser(final UUID userId) throws UserNotFoundException {
        LOGGER.debug("Find user : {}", userId);
        String msg = internationalizatonService.get("not_found", userId);
        return userRepository.findById(userId).map(u -> {
                    UserDTO abstractUserDTO = this.userMapper.fromEntity(u);
                    return new MutablePair<UserDTO, Collection<? extends GrantedAuthority>>(abstractUserDTO, u.getAuthorities());
                })
                .orElseThrow(() -> new UserNotFoundException(msg));
    }
}
