package com.ba.demo.serviceinterface;

import com.ba.demo.api.model.user.UserDTO;
import com.ba.demo.api.model.user.exception.UserException;
import java.util.List;

public interface UserService {
  UserDTO startRegistration(final UserDTO userEntity) throws UserException;

  List<UserDTO> getUsers() throws UserException;
}
