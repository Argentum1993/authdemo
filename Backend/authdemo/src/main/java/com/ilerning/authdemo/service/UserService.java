package com.ilerning.authdemo.service;

import com.ilerning.authdemo.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
  List<User>      getUsers();
  Optional<User>  getUserByName(String name);
  User            create(User rowUser);
  boolean         update(User user);
  void            ban(List<Long> ids);
  void            unBan(List<Long> ids);
  void            delete(List<Long> ids);
}
