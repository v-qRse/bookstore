package com.electronic.bookstore;

import com.electronic.bookstore.data.Book;
import com.electronic.bookstore.data.BookOnOrder;
import com.electronic.bookstore.data.BooksOrder;
import com.electronic.bookstore.repositories.BooksRepository;
import com.electronic.bookstore.repositories.GenresRepository;
import com.electronic.bookstore.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class BookstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	public ApplicationRunner bookDataLoader(
			BooksRepository repository,
			GenresRepository genresRepository)
	{
		return args -> {
			Book book = new Book();
			book.setName("Book1");
			book.setAuthor("Я");
			book.setLanguage("ru");
			book.setGenre(genresRepository.findById("NONE").get());
			book.setPublicationYear("2020");
			book.setDescription("Книга");
			book.setISBN("1111111111111");
			book.setPages(14L);
			book.setPrice(100L);
			book.setQuantity(0L);
			repository.save(book);
			book = new Book();
			book.setName("Book2");
			book.setAuthor("Я");
			book.setLanguage("en");
			book.setGenre(genresRepository.findById("FCTN").get());
			book.setPublicationYear("2010");
			book.setDescription("Книга прикинь");
			book.setISBN("1111111111112");
			book.setPages(1L);
			book.setPrice(111L);
			book.setQuantity(1L);
			repository.save(book);
			book = new Book();
			book.setName("Book3");
			book.setAuthor("Я");
			book.setLanguage("gn");
			book.setGenre(genresRepository.findById("DICT").get());
			book.setPublicationYear("2000");
			book.setDescription("Книга прикинь да");
			book.setISBN("1111111111113");
			book.setPages(1123L);
			book.setPrice(100500L);
			book.setQuantity(5L);
			repository.save(book);
		};
	}

}
