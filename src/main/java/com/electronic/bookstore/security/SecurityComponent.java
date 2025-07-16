package com.electronic.bookstore.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Customizer.withDefaults;

@Component
public class SecurityComponent {
   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   //TODO добавлять иерархию?
   /*
   @Bean
   public RoleHierarchy roleHierarchy() {
      RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
      String hierarchy = "ROLE_ADMIN > ROLE_EMPLOYEE \n ROLE_EMPLOYEE > ROLE_USER";
      roleHierarchy.setHierarchy(hierarchy);
      return roleHierarchy;
   }

   @Bean
   public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler() {
      DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
      expressionHandler.setRoleHierarchy(roleHierarchy());
      return expressionHandler;
   }
   */

   @Bean
   public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
      String[] openPaths = {"/", "/book", "/register"};
      String[] userPaths = {"/addBookToOrder", "/putBookFromOrder", "/deleteBookFromOrder", "/cart" , "/shopping-history"};
      String[] employeePaths = {"/employee", "/employee/**"};
      String[] adminPaths = {"/admin", "/admin/**"};
      return http
              .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
              .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
              .authorizeHttpRequests((authorizeRequests) ->
                      authorizeRequests
                              .requestMatchers(adminPaths).hasRole("ADMIN")
                              .requestMatchers(employeePaths).hasRole("EMPLOYEE")
                              .requestMatchers(userPaths).hasRole("USER")
                              .requestMatchers(openPaths).permitAll()
                              .requestMatchers("/h2-console").permitAll().anyRequest().authenticated()
              )
              .formLogin(withDefaults())
              .logout(withDefaults())
              .build();
   }
}
