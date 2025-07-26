package com.electronic.bookstore.data;

import com.electronic.bookstore.security.data.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//TODO отделить данные от реализации, мб через сервисы
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor()
public class BooksOrder {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne
   private Status status;

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

   //TODO безопаснее вынести оплату отдельно
//   пока так
//   @CreditCardNumber(message="Not a valid credit card number")
   @Digits(integer = 16, fraction=0, message="Not a valid credit card number")
   private String ccNumber;
   @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message="Must be formatted MM/YY")
   private String ccExpiration;
   @Column(name = "cc_cvv")
   @Digits(integer=3, fraction=0, message="Invalid CVV")
   private String ccCVV;

   private Date createdAt;
   @ManyToOne
   private User user;

   @OneToMany(cascade = CascadeType.ALL)
   @JoinTable(name = "orders_books_on_order",
               joinColumns = @JoinColumn(
                       name = "order_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(
                       name = "book_on_order_id", referencedColumnName = "id"))
   private List<BookOnOrder> books = new ArrayList<>();

   public void changeOrRemoveQuantityBookOnOrder(Book book, Long difference) {
      BookOnOrder bookOnOrder = getBookOnOrderByBookId(book.getId());
      if (bookOnOrder != null) {
         Long quantity = bookOnOrder.getQuantity() + difference;
         if (quantity <= 0) {
            books.remove(bookOnOrder);
         } else if (quantity <= book.getQuantity()) {
            bookOnOrder.setQuantity(quantity);
         }
      } else {
         bookOnOrder = new BookOnOrder(book, difference);
         if (bookOnOrder.isValidQuantity()) {
            books.add(bookOnOrder);
         }
      }
   }

   public void changeQuantityBookOnOrderToMaxPossible(Long bookId) {
      BookOnOrder bookOnOrder = getBookOnOrderByBookId(bookId);
      if (bookOnOrder != null) {
         bookOnOrder.changeQuantityToMaxPossible();
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

   public boolean isBookValidQuantityById(Long bookId) {
      BookOnOrder bookOnOrder = getBookOnOrderByBookId(bookId);
      if (bookOnOrder != null) {
         return bookOnOrder.isValidQuantity();
      }
      return false;
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

   public boolean isCompleted() {
      return status.getType() == Status.StatusType.COMPLETED;
   }
}
