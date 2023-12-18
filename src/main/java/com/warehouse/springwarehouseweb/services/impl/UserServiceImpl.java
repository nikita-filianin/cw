package com.warehouse.springwarehouseweb.services.impl;

import com.warehouse.springwarehouseweb.models.Product;
import com.warehouse.springwarehouseweb.models.Sales;
import com.warehouse.springwarehouseweb.models.User;
import com.warehouse.springwarehouseweb.models.enums.Category;
import com.warehouse.springwarehouseweb.models.enums.Role;
import com.warehouse.springwarehouseweb.repositories.ProductRepository;
import com.warehouse.springwarehouseweb.repositories.SaleProductRepository;
import com.warehouse.springwarehouseweb.repositories.SalesRepository;
import com.warehouse.springwarehouseweb.repositories.UserRepository;
import com.warehouse.springwarehouseweb.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final SalesRepository salesRepository;
    private final ProductRepository productRepository;
    private final SaleProductRepository saleProductRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findUserByLogin(principal.getName());
    }

    @Override
    public void createUser(User user) throws NameAlreadyBoundException {
        if (userRepository.findUserByLogin(user.getLogin()) != null) {
            throw new NameAlreadyBoundException("This login is already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.CUSTOMER);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        List<Sales> sales = salesRepository.findAllByUserId(id);
        List<Product> products = productRepository.findAllByUser(userRepository.findById(id).get());
        for (Product product : products) {
            saleProductRepository.deleteAllByProductId(product.getId());
            productRepository.delete(product);
        }
        for (Sales sale : sales) {
            saleProductRepository.deleteAllBySaleId(sale.getId());
            salesRepository.delete(sale);
        }
        userRepository.findById(id).ifPresent(userRepository::delete);
    }

    public void editUser(User updUser, Long id) {
        User user = userRepository.findById(id).get();
        if (!(updUser.getName() == null)) user.setName(updUser.getName());
        if (!(updUser.getLogin() == null)) user.setLogin(updUser.getLogin());
        try {
            String role = updUser.getRoles().toArray()[0].toString();
            user.getRoles().clear();
            user.getRoles().add(Role.valueOf(role));
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Role is null, leave same value");
        }
        userRepository.save(user);
    }
}
