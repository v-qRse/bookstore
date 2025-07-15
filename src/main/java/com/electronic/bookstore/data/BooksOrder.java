package com.electronic.bookstore.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BooksOrder {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   //private Status status;

   //TODO мб отдельная бд, привязанная к аккаунту
   private String deliveryName;
   private String deliveryStreet;
   private String deliveryCity;
   private String deliveryState;
   private String deliveryZip;

   //TODO мб отдельная бд, привязанная к аккаунту
   private String ccNumber;
   private String ccExpiration;
   @Column(name = "cc_cvv")
   private String ccCVV;

   @OneToMany(cascade = CascadeType.ALL)
   @JoinTable(name = "orders_books_on_order",
               joinColumns = @JoinColumn(
                       name = "order_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(
                       name = "book_on_order_id", referencedColumnName = "id"))
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
}
