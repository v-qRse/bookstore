package com.electronic.bookstore.repositories;

import com.electronic.bookstore.data.UserBooksOrder;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBooksOrdersRepository extends ListCrudRepository<UserBooksOrder, Long> {
   public boolean existsByOrderId(Long orderId);

   public Optional<UserBooksOrder> findByOrderId(Long orderId);

   public void deleteByOrderId(Long orderId);
}
