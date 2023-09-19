package com.ba.demo.dao.maper;


import com.ba.demo.api.model.user.UserDTO;
import com.ba.demo.dao.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {
    UserEntity toEntity(UserDTO userEntity);
    UserDTO fromEntity(UserEntity user);
}
