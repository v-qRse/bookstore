package com.electronic.bookstore.controllers;

import com.electronic.bookstore.security.data.Role;
import com.electronic.bookstore.security.data.User;
import com.electronic.bookstore.security.repositories.RolesRepository;
import com.electronic.bookstore.security.repositories.UsersRepository;
import jakarta.transaction.Transactional;
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
import java.util.Optional;

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
      List<User> clients = getUsersByNameAndRole(request, role);

      model.addAttribute("request", request);
      model.addAttribute("clients", clients);
      return "/admin/clientsPage";
   }

   @GetMapping("/employees")
   public String employeesPage(@RequestParam(value = "request", required = false) String request, Model model) {
      Role role = rolesRepository.findByName("ROLE_EMPLOYEE");
      List<User> clients = getUsersByNameAndRole(request, role);

      model.addAttribute("request", request);
      model.addAttribute("clients", clients);
      return "/admin/employeesPage";
   }

   @GetMapping("/employee/add")
   public String designEmployee() {
      return "/admin/employeeDesign";
   }

   @PostMapping("/employee/add")
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

   //TODO завершение сеансов аккаунта
   @DeleteMapping("/employee/delete")
   @Transactional
   public String deleteEmployee(@RequestParam("id") Long id) {
      Optional<User> user = usersRepository.findById(id);
      if (user.isPresent()) {
         for (Role role: user.get().getRoles()) {
            if (role.getName().equals("ROLE_EMPLOYEE")) {
               usersRepository.delete(user.get());
               break;
            }
         }
      }
      return "redirect:/admin/employees";
   }

   private List<User> getUsersByNameAndRole(String name, Role role) {
      List<User> users = getUsersByName(name);
      return getUsersByRoleFromList(users, role);
   }

   private List<User> getUsersByName(String name) {
      if (name == null || name.isEmpty()) {
         return usersRepository.findAll();
      }
      return usersRepository.findUsersByFirstNameOrLastNameIgnoreCase(name, name);
   }

   private List<User> getUsersByRoleFromList(List<User> users, Role role) {
      List<User> clients = new ArrayList<>();
      for(User user: users) {
         for (Role r: user.getRoles()) {
            if (r.equals(role)) {
               clients.add(user);
               break;
            }
         }
      }
      return clients;
   }
}
