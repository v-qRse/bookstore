package com.electronic.bookstore.security;

import jakarta.persistence.*;
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

   private String firstName;
   private String lastName;
   private String email;
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
