package com.electronic.bookstore.repositories;

import com.electronic.bookstore.data.order.Status;
import org.springframework.data.repository.ListCrudRepository;

public interface StatusesRepository extends ListCrudRepository<Status, String> {
}
