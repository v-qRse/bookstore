package com.electronic.bookstore.security.repositories;

import com.electronic.bookstore.security.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends ListCrudRepository<User, Long> {
   public User findByEmail(String email);
}
