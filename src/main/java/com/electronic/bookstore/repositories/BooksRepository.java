package com.electronic.bookstore.repositories;

import com.electronic.bookstore.data.Book;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends ListCrudRepository<Book, Long> {
}