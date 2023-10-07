package com.ba.demo.rest.user;

import com.ba.demo.api.model.user.UserActivationDTO;
import com.ba.demo.api.model.user.UserDTO;
import com.ba.demo.api.model.user.exception.UserException;
import com.ba.demo.api.model.user.exception.UserNotFoundException;
import com.ba.demo.serviceinterface.UserService;
import io.swagger.annotations.Api;
import java.util.List;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("public/web-user/registration")
@Api(tags = {"registration"})
@RequiredArgsConstructor
public class UserRegistrationController {

  @NonNull private final UserService userService;

  @PostMapping
  public ResponseEntity<UserDTO> startRegistration(@Valid @RequestBody UserDTO userEntity)
      throws UserException {
    return ResponseEntity.ok(userService.startRegistration(userEntity));
  }

  @PostMapping(value = "confirm")
  public ResponseEntity<UserDTO> completeRegistration(
      @RequestParam String activationToken, @RequestParam String email)
      throws UserNotFoundException {
    UserActivationDTO userActivationDTO = new UserActivationDTO(activationToken, email);
    return ResponseEntity.ok(userService.completeRegistration(userActivationDTO));
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getAll() throws UserException {
    return ResponseEntity.ok(userService.getUsers());
  }
}
