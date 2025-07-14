package com.electronic.bookstore.security.repositories;

import com.electronic.bookstore.security.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends ListCrudRepository<User, Long> {
   public User findByEmail(String email);
   public List<User> findUsersByFirstNameOrLastNameIgnoreCase(String firstName, String lastName);
}
