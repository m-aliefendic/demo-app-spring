package com.ba.demo.rest.user;

import com.ba.demo.dao.model.item.ItemEntity;
import com.ba.demo.dao.repository.ItemRepository;
import io.swagger.annotations.Api;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/item")
@Api(tags = {"web-login"})
@RequiredArgsConstructor
public class ItemController {
  @NonNull private final ItemRepository itemRepository;

  @PostMapping
  public ResponseEntity<ItemEntity> userItems() throws BadCredentialsException {
    return ResponseEntity.ok(itemRepository.getAllUserItems());
  }
}
