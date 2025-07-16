package com.electronic.bookstore.security.repositories;

import com.electronic.bookstore.security.data.Privilege;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegesRepository extends ListCrudRepository<Privilege, Long> {
   public Privilege findByName(String name);
}
