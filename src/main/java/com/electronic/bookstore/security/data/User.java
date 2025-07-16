package com.electronic.bookstore.security.data;

import com.electronic.bookstore.data.BooksOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@Entity(name = "users")
@RequiredArgsConstructor
public class User {
   @Id
   @GeneratedValue(strategy= GenerationType.AUTO)
   private Long id;

   @NotBlank(message = "Введите имя")
   private String firstName;

   @NotBlank(message = "Введите фамилию")
   private String lastName;

   @NotBlank(message = "Введите email")
   @Column(unique=true)
   private String email;

   @NotBlank(message = "Введите пароль")
   private String password;

   @ManyToMany
   @JoinTable(
           name = "users_roles",
           joinColumns = @JoinColumn(
                   name = "user_id", referencedColumnName = "id"),
           inverseJoinColumns = @JoinColumn(
                   name = "role_id", referencedColumnName = "id")
   )
   private Collection<Role> roles;

   @OneToMany(cascade = CascadeType.ALL)
   @JoinTable(
           joinColumns = @JoinColumn(
                   name = "userId", referencedColumnName = "id"),
           inverseJoinColumns = @JoinColumn(
                   name = "orderId", referencedColumnName = "id")
   )
   private List<BooksOrder> orders;
}
