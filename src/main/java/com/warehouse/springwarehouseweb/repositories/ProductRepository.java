package com.warehouse.springwarehouseweb.repositories;

import com.warehouse.springwarehouseweb.models.Product;
import com.warehouse.springwarehouseweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);


    List<Product> findAllByUser(User user);
}
