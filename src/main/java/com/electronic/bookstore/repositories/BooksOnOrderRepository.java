package com.electronic.bookstore.repositories;

import com.electronic.bookstore.data.order.BookOnOrder;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BooksOnOrderRepository extends ListCrudRepository<BookOnOrder, Long> {
}