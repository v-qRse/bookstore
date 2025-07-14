package com.electronic.bookstore.controllers;

import com.electronic.bookstore.data.Book;
import com.electronic.bookstore.data.BookOnOrder;
import com.electronic.bookstore.data.BooksOrder;
import com.electronic.bookstore.repositories.OrdersRepository;
import com.electronic.bookstore.repositories.BooksOnOrderRepository;
import com.electronic.bookstore.repositories.BooksRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@SessionAttributes(value = "booksOrder")
public class MainController {
   @Autowired
   private BooksRepository booksRepository;
   @Autowired
   private BooksOnOrderRepository booksOnOrderRepository;
   @Autowired
   private OrdersRepository ordersRepository;

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

   //TODO не работает
   @PostMapping("/cart")
   public String createOrder(@Valid BooksOrder booksOrder,
                             SessionStatus sessionStatus, Errors errors)
   {
      if (errors.hasErrors()) {
         return "cartPage";
      }
      ordersRepository.save(booksOrder);
      sessionStatus.setComplete();
      //TODO заменить на список заказов после исправлении ошибки
      return "redirect:/";
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
