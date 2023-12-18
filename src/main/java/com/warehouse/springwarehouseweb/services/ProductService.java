package com.warehouse.springwarehouseweb.services;

import com.warehouse.springwarehouseweb.models.Product;
import com.warehouse.springwarehouseweb.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface ProductService {
    List<Product> findAll();

    Product findById(Long id);

    void save(Product product);

    void deleteById(Long id);

    List<Product> findByName(String name);

    void createProduct(Product product, Principal principal, MultipartFile image) throws IOException;

    void deleteProduct(User user, Long id);

    void editProduct(Product updProduct, Long id, MultipartFile file) throws IOException;


}
