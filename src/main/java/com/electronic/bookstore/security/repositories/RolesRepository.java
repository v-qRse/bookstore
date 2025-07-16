package com.electronic.bookstore.security.repositories;

import com.electronic.bookstore.security.data.Role;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends ListCrudRepository<Role, Long> {
   public Role findByName(String name);
}
