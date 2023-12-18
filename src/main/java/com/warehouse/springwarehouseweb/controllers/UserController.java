package com.warehouse.springwarehouseweb.controllers;

import com.warehouse.springwarehouseweb.models.User;
import com.warehouse.springwarehouseweb.models.enums.Role;
import com.warehouse.springwarehouseweb.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.NameAlreadyBoundException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;


    @GetMapping("/signin")
    public String signIn(Principal principal, Model model) {
//        model.addAttribute("customer", userService.getUserByPrincipal(principal));
        return "signin";
    }

    @PostMapping("/signin")
    public String signInPost() {
        return "redirect:/products";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(User user) throws NameAlreadyBoundException {
        userService.createUser(user);
        return "redirect:/signin";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/profile")
    public String profile(Principal principal) {
        return "redirect:/user/" + userService.getUserByPrincipal(principal).getId();
    }

    @GetMapping("/user/{id}")
    public String myProfile(Model model, @PathVariable Long id) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        model.addAttribute("sales", user.getSales());
        return "profile";
    }

    @GetMapping("/user/{id}/edit")
    public String editUser(@PathVariable Long id, Model model, Principal principal) {
        if (userService.getUserByPrincipal(principal).isAdmin() || userService.getUserByPrincipal(principal).getId().equals(id)) {
            model.addAttribute("roles", Role.values());
            model.addAttribute("user", userService.findById(id));
            return "user-edit";
        } else {
            throw new ResponseStatusException(
                    HttpStatus.METHOD_NOT_ALLOWED, "It is not allowed for you"
            );
        }
    }

    @PostMapping("/user/{id}/edit")
    public String editUserPost(User updUser, @PathVariable Long id, Principal principal) {
        if (userService.getUserByPrincipal(principal).isAdmin() || userService.getUserByPrincipal(principal).getId().equals(id)) {
            userService.editUser(updUser, id);
            return "redirect:/users";
        } else {
            throw new ResponseStatusException(
                    HttpStatus.METHOD_NOT_ALLOWED, "It is not allowed for you"
            );
        }
    }
}
