package com.electronic.bookstore.data;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
   @Id
   private String id;
   private String name;
   @Enumerated(EnumType.STRING)
   @Column(name = "type")
   private GenreType genreType;

   public enum GenreType {
      NONE, FICTION, FANTASY, HORROR, DICTIONARY, ROMANCE;
   }
}
