package com.electronic.bookstore.repositories;

import com.electronic.bookstore.data.Status;
import org.springframework.data.repository.ListCrudRepository;

public interface StatusesRepository extends ListCrudRepository<Status, String> {
}
