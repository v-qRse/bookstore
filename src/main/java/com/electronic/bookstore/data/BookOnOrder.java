package com.electronic.bookstore.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BookOnOrder {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @ManyToOne
   private Book bookId;
   private Long quantity;

   public BookOnOrder(Book book, Long quantity) {
      bookId = book;
      this.quantity = quantity;
   }
}
