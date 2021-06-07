package com.ilerning.authdemo.controller;

import com.ilerning.authdemo.entity.User;
import com.ilerning.authdemo.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers(){
    final List<User> users = userService.getUsers();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @PostMapping("/ban")
  public ResponseEntity ban(@RequestBody List<Long> ids){
    userService.ban(ids);
    return new ResponseEntity(HttpStatus.OK);
  }

  @PostMapping("/unban")
  public ResponseEntity unBan(@RequestBody List<Long> ids){
    userService.unBan(ids);
    return new ResponseEntity(HttpStatus.OK);
  }

  @PostMapping("/delete")
  public ResponseEntity deleteAll(@RequestBody List<Long> ids){
    userService.delete(ids);
    return new ResponseEntity(HttpStatus.OK);
  }
}
