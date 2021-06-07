package com.ilerning.authdemo.service.impl;

import com.ilerning.authdemo.entity.User;
import com.ilerning.authdemo.repository.UserRepository;
import com.ilerning.authdemo.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public List<User> getUsers() {
    List<User> result = new ArrayList<>();
    userRepository.findAll().forEach(result::add);
    return result;
  }

  @Override
  public boolean update(User user) {
    if (!userRepository.existsById(user.getId()))
      return false;
    userRepository.save(user);
    return true;
  }

  @Override
  public Optional<User> getUserByName(String name) {
    return userRepository.findByName(name);
  }

  @Override
  public User create(User rowUser) {
    rowUser.setBanned(false);
    if (!userRepository.findByName(rowUser.getName()).isEmpty())
      return null;
    return userRepository.save(rowUser);
  }

  @Override
  public void ban(List<Long> ids) {
    mapBanUser(ids, true);
  }

  @Override
  public void unBan(List<Long> ids) {
    mapBanUser(ids, false);
  }

  private void mapBanUser(List<Long> ids, Boolean status){
    User user;
    for (Long id : ids){
      user = userRepository.findById(id).get();
      if (user != null) {
        user.setBanned(status);
        userRepository.save(user);
      }
    }
  }

  @Override
  public void delete(List<Long> ids) {
    userRepository.deleteAll(userRepository.findAllById(ids));
  }
}
