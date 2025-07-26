package com.electronic.bookstore.controllers;

import com.electronic.bookstore.data.*;
import com.electronic.bookstore.repositories.*;
import com.electronic.bookstore.security.data.User;
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

import java.util.*;

@Controller
@RequestMapping("/")
@SessionAttributes(value = "booksOrder")
public class MainController {
   private final String SESSION_BOOKS_ORDER = "booksOrder";
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

   @ModelAttribute(name = "books")
   public List<Book> books() {
      return booksRepository.findAll();
   }

   @ModelAttribute(name = "bookOnOrder")
   public BookOnOrder bookOnOrder() {
      return new BookOnOrder();
   }

   @ModelAttribute(name = SESSION_BOOKS_ORDER)
   @Transactional
   public BooksOrder booksOrder() {
      BooksOrder booksOrder = new BooksOrder();
      Status status = findStatusOrderById("CRTD");
      booksOrder.setStatus(status);
      return booksOrder;
   }

   @GetMapping
   public String homePage() {
      return "home";
   }

   @GetMapping("/book")
   public String bookPage(@RequestParam("id") Long id, Model model) {
      Optional<Book> book = booksRepository.findById(id);
      if (book.isEmpty()) {
         return "redirect:/";
      }
      model.addAttribute("book", book.get());
      return "bookPage";
   }

   //TODO сделать обновление заказа
   // заказ обновляется, но не с страницы /cart?id="cartId",
   // так как при обращении к изменению, он изменяется и пересылается на ту же страницу и
   // запрашивает заказ из бд снова
   @GetMapping("/cart")
   public String order(@RequestParam(value = "id", required = false) Long id,
                       @AuthenticationPrincipal UserDetails userDetails,
                       Model model)
   {
      if (id != null) {
         Optional<BooksOrder> booksOrder = ordersRepository.findById(id);
         if (booksOrder.isPresent()) {
            BooksOrder order = booksOrder.get();
            if (Objects.equals(userDetails.getUsername(), order.getUser().getEmail())) {
               model.addAttribute(SESSION_BOOKS_ORDER, order);
            }
         }
      }
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

   @PostMapping("/addBookToOrder")
   public String addBookToOrder(@RequestParam("id") Long bookId,
                                Model model,
                                HttpServletRequest request)
   {
      BooksOrder booksOrder = (BooksOrder) model.getAttribute(SESSION_BOOKS_ORDER);
      Optional<Book> book = booksRepository.findById(bookId);
      if (book.isPresent() && booksOrder != null) {
         booksOrder.changeOrRemoveQuantityBookOnOrder(book.get(), 1L);
      }
      return "redirect:" + request.getHeader("Referer");
   }

   @PostMapping("/putBookFromOrder")
   public String putBookFromOrder(@RequestParam("id") Long bookId,
                                  Model model,
                                  HttpServletRequest request)
   {
      BooksOrder booksOrder = (BooksOrder) model.getAttribute(SESSION_BOOKS_ORDER);
      Optional<Book> book = booksRepository.findById(bookId);
      if (book.isPresent() && booksOrder != null) {
         booksOrder.changeOrRemoveQuantityBookOnOrder(book.get(), -1L);
      }
      return "redirect:" + request.getHeader("Referer");
   }

   @PostMapping("/changeBookFromOrderToMax")
   public String changeBookFromOrderToMax(@RequestParam("id") Long bookId,
                                  Model model,
                                  HttpServletRequest request)
   {
      BooksOrder booksOrder = (BooksOrder) model.getAttribute(SESSION_BOOKS_ORDER);
      if (booksOrder != null) {
         booksOrder.changeQuantityBookOnOrderToMaxPossible(bookId);
      }
      return "redirect:" + request.getHeader("Referer");
   }

   @DeleteMapping("/deleteBookFromOrder")
   public String deleteBookFromOrder(@RequestParam("id") Long bookId,
                                     Model model,
                                     HttpServletRequest request)
   {
      BooksOrder booksOrder = (BooksOrder) model.getAttribute(SESSION_BOOKS_ORDER);
      booksOrder.deleteBook(bookId);
      return "redirect:" + request.getHeader("Referer");
   }

   @PostMapping("/cart")
   @Transactional
   public String saveOrder(@Valid @ModelAttribute(name = SESSION_BOOKS_ORDER) BooksOrder booksOrder,
                           Errors errors,
                           @AuthenticationPrincipal UserDetails userDetails,
                           SessionStatus sessionStatus)
   {
      if (errors.hasErrors()) {
         return "cartPage";
      }
      User user = usersRepository.findByEmail(userDetails.getUsername());
      booksOrder.setUser(user);
      Status status = findStatusOrderById("SAVD");
      booksOrder.setStatus(status);
      booksOrder.setCreatedAt(new Date());
      ordersRepository.save(booksOrder);

      if (!userBooksOrdersRepository.existsByOrderId(booksOrder.getId())) {
         UserBooksOrder userBooksOrder = new UserBooksOrder(user.getId(), booksOrder.getId());
         userBooksOrdersRepository.save(userBooksOrder);
      }
      sessionStatus.setComplete();
      return "redirect:/shopping-history";
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
               return "redirect:/shopping-history";
            }
         }
         Status status = findStatusOrderById("CMPL");
         order.setStatus(status);
         booksRepository.saveAll(books);
         ordersRepository.save(order);
      }
      return "redirect:/shopping-history";
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
            return "redirect:/shopping-history";
         }
      }
      return "redirect:/";
   }

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