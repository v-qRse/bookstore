package com.electronic.bookstore.repositories;

import com.electronic.bookstore.data.UserBooksOrder;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBooksOrdersRepository extends ListCrudRepository<UserBooksOrder, Long> {
   public boolean existsByOrderId(Long orderId);
}
