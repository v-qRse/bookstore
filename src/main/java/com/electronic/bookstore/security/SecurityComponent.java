package com.electronic.bookstore.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
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

   @Bean
   public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
      String[] openPaths = {"/book", "/register", "/addBookToOrder", "/putBookFromOrder", "/deleteBookFromOrder", "/"};
      String[] userPaths = {"/cart" , "/shopping-history"};
      String[] employeePaths = {"/employee", "/employee/**"};
      String[] adminPaths = {"/admin", "/admin/**"};
      return http
              .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
              .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
              .authorizeHttpRequests((authorizeRequests) -> authorizeRequests.anyRequest()
                      .authenticated())
              //TODO настроить фильтп авторизации
//              .authorizeHttpRequests((authorizeRequests) ->
//                      authorizeRequests
//                              .requestMatchers(openPaths).permitAll()
//                              .requestMatchers("/h2-console/**", "/deleteCart").permitAll()
//                              //.requestMatchers(HttpMethod.POST, , "/cart").hasRole("USER")
//                              .requestMatchers(userPaths).hasRole("USER")
//                              .requestMatchers(employeePaths).hasRole("EMPLOYEE")
//                              .requestMatchers(adminPaths).hasRole("ADMIN")
//              )
              .formLogin(withDefaults())
              .logout(withDefaults())
              .build();
   }
}
