package com.electronic.bookstore.controllers;

import com.electronic.bookstore.data.Book;
import com.electronic.bookstore.data.BooksOrder;
import com.electronic.bookstore.data.Genre;
import com.electronic.bookstore.repositories.BooksRepository;
import com.electronic.bookstore.repositories.GenresRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee")
//@SessionAttributes(value = "bookOrder", types = BooksOrder.class)
public class EmployeeController {
   @Autowired
   private BooksRepository booksRepository;
   @Autowired
   private GenresRepository genresRepository;

   @ModelAttribute("book")
   public Book book() {
      return new Book();
   }

   @GetMapping
   public String employeePage() {
      return "employeePage";
   }

   @GetMapping("/add-book")
   public String bookDesign(Model model) {
      List<Genre> genres = genresRepository.findAll();
      model.addAttribute("genres", genres);
      return "bookDesign";
   }

   @PostMapping("/add-book")
   public String saveBook(@Valid Book book, Errors errors, Model model) {
      if (errors.hasErrors()) {
         //TODO убрать повторы
         List<Genre> genres = genresRepository.findAll();
         model.addAttribute("genres", genres);
         return "bookDesign";
      }
      booksRepository.save(book);
      return "redirect:/";
   }
}
