package com.electronic.bookstore.data;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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
   private Type type;

   public enum Type {
      NONE, FICTION, FANTASY, HORROR, DICTIONARY, ROMANCE;
   }
}
