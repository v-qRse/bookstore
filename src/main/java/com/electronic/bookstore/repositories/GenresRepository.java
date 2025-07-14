package com.electronic.bookstore.repositories;

import com.electronic.bookstore.data.Genre;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenresRepository extends ListCrudRepository<Genre, String> {
}