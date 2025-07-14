package com.electronic.bookstore.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

//TODO hashCode, toString?
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Book {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @NotBlank(message = "Имя обязательно")
   private String name;

   @NotBlank(message = "Укажите автора")
   private String author;//

   //TODO подумать об создании аналога жанра
   @NotBlank(message = "Укажите язык, на котором написана книга")
   private String language;//

   @ManyToOne
   private Genre genre;//

   //только год
   @NotBlank(message = "Укажите год издания")
   private String publicationYear;//

   @Size(max = 100, message = "Описание максимум из 100 символов")
   private String description;

   @Size(min = 13, max = 13, message = "ISBN-13")
   private String ISBN;

   @NotNull(message = "Укажите количество книг")
   private Long pages;

   private Long rating;
   private Boolean isNew;
   private Long idIcon;

   @Min(value = 100, message = "Укажите цену книги")
   private Long price;

   @Min(value = 0, message = "Укажите количество книг на складе")
   private Long quantity;
}
