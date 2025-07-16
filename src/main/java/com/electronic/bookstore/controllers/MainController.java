package com.electronic.bookstore.controllers;

import com.electronic.bookstore.data.Book;
import com.electronic.bookstore.data.BookOnOrder;
import com.electronic.bookstore.data.BooksOrder;
import com.electronic.bookstore.data.UserBooksOrder;
import com.electronic.bookstore.repositories.OrdersRepository;
import com.electronic.bookstore.repositories.BooksOnOrderRepository;
import com.electronic.bookstore.repositories.BooksRepository;
import com.electronic.bookstore.repositories.UserBooksOrdersRepository;
import com.electronic.bookstore.security.User;
import com.electronic.bookstore.security.repositories.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@SessionAttributes(value = "booksOrder")
public class MainController {
   //TODO переделать в несколько контроллеров?
   @Autowired
   private BooksRepository booksRepository;
   @Autowired
   private BooksOnOrderRepository booksOnOrderRepository;
   @Autowired
   private OrdersRepository ordersRepository;
   @Autowired
   private UsersRepository usersRepository;
   @Autowired
   private UserBooksOrdersRepository userBooksOrdersRepository;

   @ModelAttribute(name = "books")
   public List<Book> books() {
      return booksRepository.findAll();
   }

   @ModelAttribute(name = "bookOnOrder")
   public BookOnOrder bookOnOrder() {
      return new BookOnOrder();
   }

   @ModelAttribute(name = "booksOrder")
   public BooksOrder booksOrder() {
      return new BooksOrder();
   }

   @GetMapping
   public String homePage() {
      return "home";
   }

   @GetMapping("/book")
   public String bookPage(@RequestParam("id") Long id, Model model) {
      Optional<Book> book = booksRepository.findById(id);
      if (book.isEmpty()) {
         return "home";
      }
      model.addAttribute("book", book.get());
      return "bookPage";
   }

   @GetMapping("/cart")
   public String order() {
      return "cartPage";
   }

   @GetMapping("/shopping-history")
   public String shoppingHistoryPage(@AuthenticationPrincipal UserDetails userDetails,
                                     Model model)
   {
      User user = usersRepository.findByEmail(userDetails.getUsername());
      model.addAttribute("orders", user.getOrders());
      return "shoppingHistoryPage";
   }

   @PostMapping("/cart")
   @Transactional
   public String createOrder(@Valid @ModelAttribute(name = "booksOrder") BooksOrder booksOrder,
                             Errors errors,
                             @AuthenticationPrincipal UserDetails userDetails,
                             SessionStatus sessionStatus)
   {
      if (errors.hasErrors()) {
         return "cartPage";
      }
      User user = usersRepository.findByEmail(userDetails.getUsername());

      booksOrder.setCreatedAt(new Date());
      //TODO разобраться кто и почему присваивает id
      booksOrder.setId(null);
      ordersRepository.save(booksOrder);

      UserBooksOrder userBooksOrder = new UserBooksOrder(user.getId(), booksOrder.getId());
      userBooksOrdersRepository.save(userBooksOrder);
      sessionStatus.setComplete();
      return "redirect:/shopping-history";
   }

   //TODO как сделать лучше?
   @PostMapping("/addBookToOrder")
   public String addBookToOrder(@RequestParam Long id,
                                @ModelAttribute(name = "booksOrder") BooksOrder booksOrder,
                                HttpServletRequest request)
   {
      Optional<Book> book = booksRepository.findById(id);
      if (book.isPresent()) {
         booksOrder.addBook(new BookOnOrder(book.get(), 1L));
      }
      return "redirect:" + request.getHeader("Referer");
   }

   @PostMapping("/putBookFromOrder")
   public String putBookFromOrder(@RequestParam Long id,
                                  @ModelAttribute(name = "booksOrder") BooksOrder booksOrder,
                                  HttpServletRequest request)
   {
      Optional<Book> book = booksRepository.findById(id);
      if (book.isPresent()) {
         booksOrder.addBook(new BookOnOrder(book.get(), -1L));
      }
      return "redirect:" + request.getHeader("Referer");
   }

   @PostMapping("/deleteBookFromOrder")
   public String deleteBookFromOrder(@RequestParam("id") Long id,
                                     @ModelAttribute(name = "booksOrder") BooksOrder booksOrder,
                                     HttpServletRequest request)
   {
      booksOrder.deleteBook(id);
      return "redirect:" + request.getHeader("Referer");
   }
}
