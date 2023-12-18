package com.warehouse.springwarehouseweb.services.impl;

import com.warehouse.springwarehouseweb.models.*;
import com.warehouse.springwarehouseweb.models.enums.Category;
import com.warehouse.springwarehouseweb.repositories.ProductRepository;
import com.warehouse.springwarehouseweb.repositories.SaleProductRepository;
import com.warehouse.springwarehouseweb.repositories.SalesRepository;
import com.warehouse.springwarehouseweb.repositories.UserRepository;
import com.warehouse.springwarehouseweb.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final SalesRepository salesRepository;
    private final SaleProductRepository saleProductRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.findById(id).ifPresent(productRepository::delete);
    }

    @Override
    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public void createProduct(Product product, Principal principal, MultipartFile file) throws IOException {
        Image img;
        if (principal == null) {
            product.setUser(new User());
        } else {
            if (file.getSize() != 0) {
                img = toImageEntity(file);
                product.addImageToProduct(img);
            }
            product.setUser(userRepository.findUserByLogin(principal.getName()));
        }
        Product productFromDb = productRepository.save(product);
        productFromDb.setImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image img = new Image();
        img.setName(file.getName());
        img.setOriginalFileName(file.getOriginalFilename());
        img.setSize(file.getSize());
        img.setContentType(file.getContentType());
        img.setContent(file.getBytes());
        return img;
    }

    @Override
    public void deleteProduct(User user, Long id) {
        Product product = productRepository.findById(id)
                .orElse(null);
        if (product != null) {
            saleProductRepository.deleteAllByProductId(product.getId());
            productRepository.delete(product);
        } else System.err.println("Product " + id + " is not found");
    }

    @Override
    public void editProduct(Product updProduct, Long id, MultipartFile file) throws IOException {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            System.err.println("Product " + id + " is not found");
        } else {
            product.setName(updProduct.getName());
            product.setPrice(updProduct.getPrice());
            product.setQuantity(updProduct.getQuantity());
            product.setDescription(updProduct.getDescription());
            if (file.getSize() != 0) {
                Image img = toImageEntity(file);
                updProduct.addImageToProduct(img);
                product.setImages(updProduct.getImages());
            }
            try {
                product.getCategory().clear();
                String category = updProduct.getCategory().toArray()[0].toString();
                product.getCategory().add(Category.valueOf(category));
            } catch (NullPointerException e) {
                System.out.println("Category is null, setting other value");
                product.getCategory().clear();
                product.getCategory().add(Category.OTHER);
            }
            Product productFromDb = productRepository.save(product);
            if (file.getSize() != 0) {
                productFromDb.setImageId(productFromDb.getImages().get(0).getId());
                productRepository.save(product);
            }

        }
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void updateAmount(Product product, Integer amount) {
        product.setQuantity(product.getQuantity() - amount);
        productRepository.save(product);
    }

    public List<Product> getMostSoldProducts(int n) {
        Map<Product, Integer> productQuantities = saleProductRepository.findAll().stream()
                .collect(Collectors.groupingBy(SaleProduct::getProduct,
                        Collectors.summingInt(SaleProduct::getQuantity)));

        return productQuantities.
                entrySet().stream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(n)
                .collect(Collectors.toList());
    }
}
