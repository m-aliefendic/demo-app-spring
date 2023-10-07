package com.ba.demo.serviceinterface;

import com.ba.demo.api.model.user.UserActivationDTO;
import com.ba.demo.api.model.user.UserDTO;
import com.ba.demo.api.model.user.exception.UserException;
import com.ba.demo.api.model.user.exception.UserNotFoundException;
import java.util.List;

public interface UserService {
  UserDTO startRegistration(final UserDTO userEntity) throws UserException;

  UserDTO completeRegistration(UserActivationDTO userActivationDTO) throws UserNotFoundException;

  List<UserDTO> getUsers() throws UserException;
}
