package com.electronic.bookstore.repositories;

import com.electronic.bookstore.data.BookOnOrder;
import com.electronic.bookstore.data.BooksOrder;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends ListCrudRepository<BooksOrder, Long> {
}
