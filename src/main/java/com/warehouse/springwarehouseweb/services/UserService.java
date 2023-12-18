package com.warehouse.springwarehouseweb.services;

import com.warehouse.springwarehouseweb.models.User;

import javax.naming.NameAlreadyBoundException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService {
    void save(User user);

    User findById(Long id);

    List<User> findAll();

    User getUserByPrincipal(Principal principal);

    void createUser(User user) throws NameAlreadyBoundException;

    void deleteUser(Long id);

}
