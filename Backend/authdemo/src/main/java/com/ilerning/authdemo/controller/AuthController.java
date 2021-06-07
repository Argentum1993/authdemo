package com.ilerning.authdemo.controller;

import com.ilerning.authdemo.entity.User;
import com.ilerning.authdemo.security.JwtTokenProvider;
import com.ilerning.authdemo.service.UserService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AuthController(UserService userService,
      AuthenticationManager authenticationManager,
      JwtTokenProvider jwtTokenProvider,
      PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/sign-up")
  public ResponseEntity<String> registration(@RequestBody User user){
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    final User userEntity = userService.create(user);
    return userEntity != null
        ? new ResponseEntity<>(user.getName() + " you are register!", HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthDTO request){
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      User user = userService.getUserByName(request.getUsername())
          .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
      user.setLastLogin(new Date());
      userService.update(user);
      String token = jwtTokenProvider.createToken(request.getUsername());

      Map<Object, Object> response = new HashMap<>();
      response.put("name", request.getUsername());
      response.put("accessToken", token);
      return ResponseEntity.ok(response);
    } catch (AuthenticationException e){
      return new ResponseEntity<>("Invalid username/password combination", HttpStatus.FORBIDDEN);
    }
  }

  @PostMapping("/logout")
  public void logout(HttpServletRequest request, HttpServletResponse response){
    SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
    securityContextLogoutHandler.logout(request, response, null);
  }
}
