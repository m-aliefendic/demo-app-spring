package com.ba.demo.rest.user;

import com.ba.demo.api.model.user.UserDTO;
import com.ba.demo.api.model.user.exception.UserException;
import com.ba.demo.serviceinterface.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import javax.validation.Valid;
@RestController
@RequestMapping("public/web-user/registration")
@Api(tags = {"registration"})
@RequiredArgsConstructor
public class UserRegistrationController {

    @NonNull
    final private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> startRegistration(@Valid @RequestBody UserDTO userEntity) throws UserException {
        return ResponseEntity.ok(userService.startRegistration(userEntity));
    }
}
