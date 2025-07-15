package com.electronic.bookstore.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BooksOrder {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   //private Status status;

   //TODO мб отдельная бд, привязанная к аккаунту
   @NotBlank(message="Delivery name is required")
   private String deliveryName;

   @NotBlank(message="Street is required")
   private String deliveryStreet;

   @NotBlank(message="City is required")
   private String deliveryCity;

   @NotBlank(message="State is required")
   private String deliveryState;

   @NotBlank(message="Zip code is required")
   private String deliveryZip;

   //TODO мб отдельная бд, привязанная к аккаунту
//   @CreditCardNumber(message="Not a valid credit card number")
   //TODO пока так
   @Digits(integer = 16, fraction=0, message="Not a valid credit card number")
   private String ccNumber;

   @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message="Must be formatted MM/YY")
   private String ccExpiration;

   @Digits(integer=3, fraction=0, message="Invalid CVV")
   private String ccCVV;

   //TODO добавить дату

   @OneToMany(cascade = CascadeType.ALL)
   private List<BookOnOrder> books = new ArrayList<>();

   //TODO подумать как изменить
   public void addBook(BookOnOrder book) {
      Long bookId = book.getBook().getId();
      BookOnOrder bookOnOrder = getBookOnOrderByBookId(bookId);
      if (bookOnOrder != null) {
         Long quantity = bookOnOrder.getQuantity() + book.getQuantity();
         if (quantity <= 0) {
            books.remove(bookOnOrder);
         } else if (quantity <= bookOnOrder.getBook().getQuantity()) {
            bookOnOrder.setQuantity(quantity);
         }
      } else {
         books.add(book);
      }
   }

   public void deleteBook(Long bookId) {
      BookOnOrder bookOnOrder = getBookOnOrderByBookId(bookId);
      if (bookOnOrder != null) {
         books.remove(bookOnOrder);
      }
   }

   public Long getQuantityById(Long bookId) {
      BookOnOrder bookOnOrder = getBookOnOrderByBookId(bookId);
      if (bookOnOrder != null) {
         return bookOnOrder.getQuantity();
      }
      return 0L;
   }

   public boolean containBook(Long bookId) {
      BookOnOrder bookOnOrder = getBookOnOrderByBookId(bookId);
      return bookOnOrder != null;
   }

   private BookOnOrder getBookOnOrderByBookId(Long id) {
      for (BookOnOrder book: books) {
         if (Objects.equals(book.getBook().getId(), id)) {
            return book;
         }
      }
      return null;
   }

   public Long getPrice() {
      long count = 0;
      for (BookOnOrder bookOnOrder: books) {
         count += bookOnOrder.getQuantity()*bookOnOrder.getBook().getPrice();
      }
      return count;
   }
}
