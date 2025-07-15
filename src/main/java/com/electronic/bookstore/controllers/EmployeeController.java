package com.electronic.bookstore.controllers;

import com.electronic.bookstore.data.Book;
import com.electronic.bookstore.data.Genre;
import com.electronic.bookstore.repositories.BooksRepository;
import com.electronic.bookstore.repositories.GenresRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
   @Autowired
   private BooksRepository booksRepository;
   @Autowired
   private GenresRepository genresRepository;

   @ModelAttribute("book")
   public Book book() {
      return new Book();
   }

   @ModelAttribute("genres")
   public List<Genre> genres() {
      return genresRepository.findAll();
   }

   @GetMapping
   public String employeePage() {
      return "/employee/employeePage";
   }

   @GetMapping("/add-book")
   public String bookDesign() {
      return "/employee/bookDesign";
   }

   @PostMapping("/add-book")
   public String saveBook(@Valid Book book, Errors errors) {
      if (errors.hasErrors()) {
         return "/employee/bookDesign";
      }
      booksRepository.save(book);
      return "redirect:/";
   }
}
