package com.ba.demo.core.impl;

import com.ba.demo.api.model.user.UserDTO;
import com.ba.demo.core.utils.ErrorCode;
import com.ba.demo.core.utils.MessagesKey;
import com.ba.demo.core.utils.SpecificException;
import com.ba.demo.dao.maper.UserMapper;
import com.ba.demo.dao.model.UserEntity;
import com.ba.demo.dao.repository.AbstractUserRepository;
import com.ba.demo.dao.repository.UserRepository;
import com.ba.demo.rest.internationalization.InternationalizatonService;
import com.ba.demo.api.model.user.exception.UserException;
import com.ba.demo.serviceinterface.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @NotNull
    final private AbstractUserRepository abstractUserRepository;
    @NotNull
    final private UserMapper userMapper;
    @NotNull
    private final InternationalizatonService internationalizatonService;
    @NotNull
    private final UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public UserDTO startRegistration(UserDTO userEntity) throws UserException {
        if(abstractUserRepository.findByEmail(userEntity.getEmail()).isPresent()) {
            throw new SpecificException(ErrorCode.CONFLICT, MessagesKey.email_already_exists);
        }

        UserEntity user = userMapper.toEntity(userEntity);
        //setPassword(user, userEntity.getPassword());
        user = userRepository.save(user);

        if(user != null) {
            LOGGER.debug("Sending registration email: {} -- {}.", user.getEmail(), user);
            return userMapper.fromEntity(user);
        }
        String msg =  internationalizatonService.get("registration_not_started");
        throw new UserException(msg);

    }
}
