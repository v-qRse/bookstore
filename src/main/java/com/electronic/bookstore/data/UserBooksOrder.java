package com.electronic.bookstore.data;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "users_orders")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class UserBooksOrder {
   @Id
   @GeneratedValue()
   private Long id;
   private Long userId;
   private Long orderId;

   public UserBooksOrder(Long userId, Long orderId) {
      this.userId = userId;
      this.orderId = orderId;
   }
}
