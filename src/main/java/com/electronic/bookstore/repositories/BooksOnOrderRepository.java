package com.electronic.bookstore.repositories;

import com.electronic.bookstore.data.Book;
import com.electronic.bookstore.data.BookOnOrder;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BooksOnOrderRepository extends ListCrudRepository<BookOnOrder, Long> {
}
