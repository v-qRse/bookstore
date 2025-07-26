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
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne
   private Book book;
   private Long quantity;

   public BookOnOrder(Book book, Long quantity) {
      this.book = book;
      this.quantity = quantity;
   }

   public boolean isValidQuantity() {
      if (book == null) {
         return false;
      }
      return quantity > 0 && quantity <= book.getQuantity();
   }

   public void changeQuantityToMaxPossible() {
      quantity = book.getQuantity();
   }
}
