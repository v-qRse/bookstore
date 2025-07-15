package com.electronic.bookstore.controllers;

import com.electronic.bookstore.security.Role;
import com.electronic.bookstore.security.User;
import com.electronic.bookstore.security.repositories.RolesRepository;
import com.electronic.bookstore.security.repositories.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
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

   @GetMapping
   public String adminMainPage() {
      return "/admin/adminPage";
   }

   @GetMapping("/clients")
   public String clientsPage(@RequestParam(value = "request", required = false) String request, Model model) {
      Role role = rolesRepository.findByName("ROLE_USER");

      //TODO изменить одинаковый код
      List<User> users;
      if (request == null || request.isEmpty()) {
         users = usersRepository.findAll();
      } else {
         users = usersRepository.findUsersByFirstNameOrLastNameIgnoreCase(request, request);
      }
      List<User> clients = new ArrayList<>();
      for(User user: users) {
         for (Role r: user.getRoles()) {
            if (r.equals(role)) {
               clients.add(user);
               break;
            }
         }
      }

      model.addAttribute("request", request);
      model.addAttribute("clients", clients);
      return "/admin/clientsPage";
   }

   @GetMapping("/employees")
   public String employeesPage(@RequestParam(value = "request", required = false) String request, Model model) {
      Role role = rolesRepository.findByName("ROLE_EMPLOYEE");
      //TODO изменить одинаковый код
      List<User> users;
      if (request == null || request.isEmpty()) {
         users = usersRepository.findAll();
      } else {
         users = usersRepository.findUsersByFirstNameOrLastNameIgnoreCase(request, request);
      }
      List<User> clients = new ArrayList<>();
      for(User user: users) {
         for (Role r: user.getRoles()) {
            if (r.equals(role)) {
               clients.add(user);
               break;
            }
         }
      }

      model.addAttribute("request", request);
      model.addAttribute("clients", clients);
      return "/admin/employeesPage";
   }

   @GetMapping("/add/employee")
   public String designEmployee() {
      return "/admin/employeeDesign";
   }

   @PostMapping("/add/employee")
   public String addEmployee(@Valid User user, Errors errors) {
      if (errors.hasErrors()) {
         return "/admin/employeeDesign";
      }
      Role role = rolesRepository.findByName("ROLE_EMPLOYEE");
      user.setRoles(Arrays.asList(role));
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      usersRepository.save(user);
      return "redirect:/admin/employees";
   }
}
