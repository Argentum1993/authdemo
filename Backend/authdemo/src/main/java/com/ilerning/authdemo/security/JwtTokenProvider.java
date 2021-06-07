package com.ilerning.authdemo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final UserDetailsService userDetailsService;



  @Value("${jwt.secret}")
  private String secretKey;
  @Value("${jwt.header}")
  private String authorizationHeader;
  @Value("${jwt.expiration}")
  private long validityInMilliseconds;

  public JwtTokenProvider(
      @Qualifier("UserDetailsServiceImpl") UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @PostConstruct
  protected void init(){
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public  String createToken(String username){
    Claims claims = Jwts.claims().setSubject(username);
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds * 1000);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public boolean validateToken(String token){
    try {
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
      return !claimsJws.getBody().getExpiration().before(new Date()) && userDetails.isEnabled();
    } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e){
      throw new JwtAuthenticationException("JWT token is expired or invalid", HttpStatus.UNAUTHORIZED);
    }
  }

  public Authentication getAuthentication(String token){
    try {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
      return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    } catch (UsernameNotFoundException e) {
      throw new JwtAuthenticationException("User not found");
    }
  }

  public String getUsername(String token){
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest request){
    return request.getHeader(authorizationHeader);
  }
}
