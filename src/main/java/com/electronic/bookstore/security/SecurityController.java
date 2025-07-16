package com.electronic.bookstore.security;

import com.electronic.bookstore.security.data.Role;
import com.electronic.bookstore.security.data.User;
import com.electronic.bookstore.security.repositories.RolesRepository;
import com.electronic.bookstore.security.repositories.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
public class SecurityController {
   @Autowired
   private UsersRepository usersRepository;
   @Autowired
   private RolesRepository rolesRepository;
   @Autowired
   private PasswordEncoder passwordEncoder;

   @ModelAttribute("user")
   public User user() {
      return new User();
   }

   @GetMapping("/register")
   public String registerUser() {
      return "/security/registerPage";
   }

   @PostMapping("/register")
   public String saveUser(@Valid User user, Errors errors) {
      if (errors.hasErrors()) {
         return "/security/registerPage";
      }
      Role role = rolesRepository.findByName("ROLE_USER");
      user.setRoles(Arrays.asList(role));
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      usersRepository.save(user);
      return "redirect:/login";
   }
}
