package com.electronic.bookstore.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Book {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @NotBlank(message = "Имя обязательно")
   private String name;

   @NotBlank(message = "Укажите автора")
   private String author;

   @NotBlank(message = "Укажите язык, на котором написана книга")
   private String language;

   @ManyToOne
   private Genre genre;

   @NotBlank(message = "Укажите год издания")
   @Size(min = 1, max = 4)
   private String publicationYear;

   @Size(max = 100, message = "Описание максимум из 100 символов")
   private String description;

   @Digits(integer=13, fraction=0, message = "неверный ISBN-13")
   @Size(min = 13, max = 13)
   private String ISBN;

   @NotNull(message = "Укажите количество книг")
   @Min(value = 1, message = "Введите положительное число")
   private Long pages;

   private Long rating;
   private Boolean isNew;
   private Long idIcon;

   @Min(value = 100, message = "Укажите цену книги")
   private Long price;

   @Min(value = 0, message = "Укажите количество книг на складе")
   private Long quantity;
}
