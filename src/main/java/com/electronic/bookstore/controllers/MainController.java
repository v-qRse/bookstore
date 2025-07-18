package com.electronic.bookstore.controllers;

import com.electronic.bookstore.data.Book;
import com.electronic.bookstore.data.BookOnOrder;
import com.electronic.bookstore.data.BooksOrder;
import com.electronic.bookstore.data.UserBooksOrder;
import com.electronic.bookstore.repositories.OrdersRepository;
import com.electronic.bookstore.repositories.BooksOnOrderRepository;
import com.electronic.bookstore.repositories.BooksRepository;
import com.electronic.bookstore.repositories.UserBooksOrdersRepository;
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

   //TODO закомментированная попытка сделать изменение заказа
   @GetMapping("/cart")
   public String order()//@RequestParam(value = "id", required = false) Long id,
                       //Model model)
   {
//      if (id != null) {
//         Optional<BooksOrder> order = ordersRepository.findById(id);
//         if (order.isPresent()) {
//            ordersRepository.deleteById(id);
//            model.addAttribute("booksOrder", order.get());
//         }
//      }
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

   //TODO как сделать лучше?
   @PostMapping("/addBookToOrder")
   public String addBookToOrder(@RequestParam("id") Long id,
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
   public String putBookFromOrder(@RequestParam("id") Long id,
                                  @ModelAttribute(name = "booksOrder") BooksOrder booksOrder,
                                  HttpServletRequest request)
   {
      Optional<Book> book = booksRepository.findById(id);
      if (book.isPresent()) {
         booksOrder.addBook(new BookOnOrder(book.get(), -1L));
      }
      return "redirect:" + request.getHeader("Referer");
   }

   @DeleteMapping("/deleteBookFromOrder")
   public String deleteBookFromOrder(@RequestParam("id") Long id,
                                     @ModelAttribute(name = "booksOrder") BooksOrder booksOrder,
                                     HttpServletRequest request)
   {
      booksOrder.deleteBook(id);
      return "redirect:" + request.getHeader("Referer");
   }

   @PostMapping("/cart")
   @Transactional
   public String saveOrder(@Valid @ModelAttribute(name = "booksOrder") BooksOrder booksOrder,
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

   //TODO подумать о кнопкe очищения заказа
   // мб сделать через deleteOrder() с проверкой на то сохранен заказ или нет


   //TODO
   @DeleteMapping("/deleteCart")
   @Transactional
   public String deleteOrder(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
      //TODO сделать проверку на статус заказа
      // если ещё не оплачет и не получен, то можно удалять, иначе нельзя
      if (ordersRepository.existsById(id)) {
         Optional<UserBooksOrder> userBooksOrder = userBooksOrdersRepository.findByOrderId(id);
         if (userBooksOrder.isPresent()) {
            User user = usersRepository.findByEmail(userDetails.getUsername());
            if (Objects.equals(userBooksOrder.get().getUserId(), user.getId())) {
               userBooksOrdersRepository.deleteByOrderId(id);
               ordersRepository.deleteById(id);
            } else {
               System.out.println("ошибка удаления заказа: текущий пользователь и пользователь заказа различны");
               return "redirect:/";
            }
         } else {
            System.out.println("ошибка сохранения заказа: заказ сохранен, но не привязан к пользователю");
         }
      } else {
         System.out.println("попытка удаления не сохраненного заказа");
      }
      return "redirect:/shopping-history";
   }
}