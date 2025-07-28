package com.electronic.bookstore.controllers;

import com.electronic.bookstore.data.Book;
import com.electronic.bookstore.data.BookOnOrder;
import com.electronic.bookstore.data.BooksOrder;
import com.electronic.bookstore.data.Status;
import com.electronic.bookstore.repositories.BooksRepository;
import com.electronic.bookstore.repositories.OrdersRepository;
import com.electronic.bookstore.repositories.StatusesRepository;
import com.electronic.bookstore.repositories.UserBooksOrdersRepository;
import com.electronic.bookstore.security.data.User;
import com.electronic.bookstore.security.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
   @Autowired
   private BooksRepository booksRepository;
   @Autowired
   private OrdersRepository ordersRepository;
   @Autowired
   private UsersRepository usersRepository;
   @Autowired
   private UserBooksOrdersRepository userBooksOrdersRepository;
   @Autowired
   private StatusesRepository statusesRepository;

   @ModelAttribute("user")
   public User user(@AuthenticationPrincipal UserDetails userDetails) {
      return usersRepository.findByEmail(userDetails.getUsername());
   }

   @GetMapping
   public String userPage() {
      return "/user/userPage";
   }

   @GetMapping("/profile")
   public String userProfile()
   {
      return "/user/profile";
   }

   //TODO можно ли заменить на User user = (User) model.getAttribute("user");
   // чтобы повторно не обращаться к бд
   @GetMapping("/shopping-history")
   public String shoppingHistoryPage(@AuthenticationPrincipal UserDetails userDetails,
                                     Model model)
   {
      User user = usersRepository.findByEmail(userDetails.getUsername());
      model.addAttribute("orders", user.getOrders());
      return "/user/shoppingHistoryPage";
   }

   @PostMapping("/profile")
   @Transactional
   public String changeProfile(@Valid @ModelAttribute("user") User user, Errors errors,
                               @AuthenticationPrincipal UserDetails userDetails)
   {
      User profile = usersRepository.findByEmail(userDetails.getUsername());
      if (errors.hasErrors()) {
         return "/user/profile";
      }
      if (Objects.equals(profile.getId(), user.getId())) {
         usersRepository.save(user);
      }
      return "redirect:/user";
   }

   @PostMapping("/paidCart")
   @Transactional
   //не потокобезопасно
   public String paidCart(@RequestParam("id") Long id) {
      Optional<BooksOrder> booksOrder = ordersRepository.findById(id);
      if (booksOrder.isPresent()) {
         BooksOrder order = booksOrder.get();
         List<Book> books = new ArrayList<>();
         for (BookOnOrder bookOnOrder: order.getBooks()) {
            Book book = bookOnOrder.getBook();
            if (book.getQuantity() >= bookOnOrder.getQuantity()) {
               book.setQuantity(book.getQuantity() - bookOnOrder.getQuantity());
               books.add(book);
            } else {
               //TODO ошибка запроса
               return "redirect:/user/shopping-history";
            }
         }
         Status status = findStatusOrderById("CMPL");
         order.setStatus(status);
         booksRepository.saveAll(books);
         ordersRepository.save(order);
      }
      return "redirect:/user/shopping-history";
   }

   @DeleteMapping("/deleteCart")
   @Transactional
   public String deleteOrder(@RequestParam("id") Long id,
                             @AuthenticationPrincipal UserDetails userDetails)
   {
      Optional<BooksOrder> booksOrder = ordersRepository.findById(id);
      if (booksOrder.isPresent()) {
         BooksOrder order = booksOrder.get();
         if (!order.isCompleted() && Objects.equals(userDetails.getUsername(), order.getUser().getEmail())) {
            userBooksOrdersRepository.deleteByOrderId(id);
            ordersRepository.deleteById(id);
            return "redirect:/user/shopping-history";
         }
      }
      return "redirect:/";
   }

   //TODO сделать самостоятельное удаление аккаунта пользователем

   //TODO надо придумать как реализовать ошибку
   private Status findStatusOrderById(String id) {
      Optional<Status> status = statusesRepository.findById(id);
      if (status.isPresent()) {
         return status.get();
      }
      //надо выводить ошибку
      return null;
   }
}
