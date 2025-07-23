package com.electronic.bookstore.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Status {
   @Id
   private String id;
   private String description;
   @Enumerated(EnumType.STRING)
   @Column(name = "type")
   private StatusType type;

   //по идее
   // заказ изменяется
   // заказ доставляется
   public enum StatusType {
      CREATED, SAVED, COMPLETED;
   }
}
