//package com.warehouse.springwarehouseweb.controllers;
//
//import com.warehouse.springwarehouseweb.models.enums.Category;
//import com.warehouse.springwarehouseweb.services.impl.ProductServiceImpl;
//import com.warehouse.springwarehouseweb.services.impl.SaleProductServiceImpl;
//import com.warehouse.springwarehouseweb.services.impl.SaleServiceImpl;
//import com.warehouse.springwarehouseweb.services.impl.UserServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.security.Principal;
//import java.util.Arrays;
//
//@Controller
//@RequiredArgsConstructor
//public class HomeController {
//    private final ProductServiceImpl productService;
//    private final UserServiceImpl userService;
//    private final SaleServiceImpl saleService;
//    private final SaleProductServiceImpl saleProductService;
//    @GetMapping(value = "/")
//    public String homePage(Model model, Principal principal) {
//        model.addAttribute("categories", Arrays.stream(Category.values()).toList());
//        model.addAttribute("users", userService.findAll());
//        model.addAttribute("sales", saleService.findAll());
//        model.addAttribute("username", userService.getUserByPrincipal(principal));
//        model.addAttribute("products", productService.findAll());
//        model.addAttribute("last3Sales", saleService.getLast3Sales());
//        model.addAttribute("mostSoldProducts", productService.getMostSoldProducts(5));
//        return "index";
//    }
//}

package com.warehouse.springwarehouseweb.controllers;

import com.warehouse.springwarehouseweb.models.enums.Category;
import com.warehouse.springwarehouseweb.services.impl.ProductServiceImpl;
import com.warehouse.springwarehouseweb.services.impl.SaleProductServiceImpl;
import com.warehouse.springwarehouseweb.services.impl.SaleServiceImpl;
import com.warehouse.springwarehouseweb.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;
    private final SaleServiceImpl saleService;
    private final SaleProductServiceImpl saleProductService;

    @GetMapping(value = "/")
    public String homePage(Model model, Principal principal) {
        List<Category> categories = Arrays.stream(Category.values()).collect(Collectors.toList());

        model.addAttribute("categories", categories);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("sales", saleService.findAll());
        model.addAttribute("username", userService.getUserByPrincipal(principal));
        model.addAttribute("products", productService.findAll());
        model.addAttribute("last3Sales", saleService.getLast3Sales());
        model.addAttribute("mostSoldProducts", productService.getMostSoldProducts(5));

        return "index";
    }
}
