package com.ilerning.authdemo.repository;

import com.ilerning.authdemo.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByName(String name);
}
