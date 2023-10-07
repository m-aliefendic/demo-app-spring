package com.ba.demo.core.impl;

import com.ba.demo.core.service.JwtService;
import com.ba.demo.core.service.SaltService;
import com.ba.demo.core.utils.ErrorCode;
import com.ba.demo.core.utils.MessagesKey;
import com.ba.demo.core.utils.SpecificException;
import com.ba.demo.dao.maper.UserMapper;
import com.ba.demo.dao.model.item.ItemEntity;
import com.ba.demo.dao.model.user.UserEntity;
import com.ba.demo.dao.repository.CustomItemRepository;
import com.ba.demo.dao.repository.UserRepository;
import com.ba.demo.service.InternationalizatonService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements CustomItemRepository {

    @NotNull
    final private UserMapper userMapper;
    @NotNull
    private final InternationalizatonService internationalizatonService;
    @NotNull
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemRepositoryImpl.class);
    @NotNull
    private final Supplier<String> saltSupplier = SaltService::random;
    @NotNull
    private final JwtService jwtService;

    @Override
    public ItemEntity getAllUserItems() {

        UUID currentUserId = jwtService.getUUIDIdFromToken();
        Optional<UserEntity> user = userRepository.findById(currentUserId);
        if (!user.isPresent()) {
            throw new SpecificException(ErrorCode.RESOURCE_NOT_FOUND, MessagesKey.user_not_found);
        }



        return null;
    }
}
