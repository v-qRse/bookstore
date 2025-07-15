package com.electronic.bookstore.security;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@Data
@Entity(name = "users")
@RequiredArgsConstructor
public class User{
   @Id
   @GeneratedValue(strategy= GenerationType.AUTO)
   private Long id;

   @NotBlank(message = "Введите имя")
   private String firstName;

   @NotBlank(message = "Введите фамилию")
   private String lastName;

   @NotBlank(message = "Введите email")
   private String email;

   @NotBlank(message = "Введите пароль")
   private String password;

   @ManyToMany
   @JoinTable(
           name = "users_roles",
           joinColumns = @JoinColumn(
                   name = "user_id", referencedColumnName = "id"),
           inverseJoinColumns = @JoinColumn(
                   name = "role_id", referencedColumnName = "id"))
   private Collection<Role> roles;
}
