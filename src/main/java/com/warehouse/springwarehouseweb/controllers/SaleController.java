package com.warehouse.springwarehouseweb.controllers;

import com.warehouse.springwarehouseweb.dto.SaleProductDto;
import com.warehouse.springwarehouseweb.models.Product;
import com.warehouse.springwarehouseweb.models.SaleProduct;
import com.warehouse.springwarehouseweb.models.Sales;
import com.warehouse.springwarehouseweb.models.User;
import com.warehouse.springwarehouseweb.models.enums.Category;
import com.warehouse.springwarehouseweb.services.impl.ProductServiceImpl;
import com.warehouse.springwarehouseweb.services.impl.SaleProductServiceImpl;
import com.warehouse.springwarehouseweb.services.impl.SaleServiceImpl;
import com.warehouse.springwarehouseweb.services.impl.UserServiceImpl;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SaleController {
    private final ProductServiceImpl productService;
    private final SaleServiceImpl saleService;
    private final SaleProductServiceImpl saleProductService;
    private final UserServiceImpl userService;

    @GetMapping(value = "/sales")
    public String getAllSales(@RequestParam(name = "startDate", required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam(name = "endDate", required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              @RequestParam(name = "mySales", required = false, defaultValue = "false") boolean mySales,
                              Model model, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("username", user);
        if (startDate != null && endDate != null) {
            model.addAttribute("sales",
                    mySales ? saleService
                            .getMySalesBetweenDates(startDate, endDate, user.getId())
                            : saleService.findAllBySaleDateBetween(startDate, endDate));
        } else if (startDate != null) {
            model.addAttribute("sales",
                    mySales ? saleService
                            .getMySalesAfterDate(startDate, user.getId())
                            : saleService.findAllBySaleDateAfter(startDate));
        } else if (endDate != null) {
            model.addAttribute("sales",
                    mySales ? saleService
                            .getMySalesBeforeDate(endDate, user.getId())
                            : saleService.findAllBySaleDateBefore(endDate));
        } else {
            model.addAttribute("sales",
                    mySales ? saleService.getMySales(user.getId()) : saleService.findAll());
        }
        return "sales";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER')")
    @GetMapping(value = "/sales/create")
    public String createSale(Model model, Principal principal) {
        List<Product> products = productService.findAll();
        model.addAttribute("username", userService.getUserByPrincipal(principal));

        // Check if products is null or empty before passing it to SalesForm
        if (products != null && !products.isEmpty()) {
            model.addAttribute("products", products);
            model.addAttribute("salesForm", new SalesForm(products));
        } else {
            model.addAttribute("products", new ArrayList<Product>());
            model.addAttribute("salesForm", new SalesForm(new ArrayList<Product>()));
        }

        return "sales-create";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER')")
    @PostMapping(value = "/sales/create")
    public String createSalePost(SalesForm form, Principal principal) {
        List<SaleProductDto> formDtos = form.getProductSales();
        formDtos.removeIf(saleProductDto -> saleProductDto.getQuantity() == null || saleProductDto.getQuantity() < 1);
        formDtos.forEach(saleProductDto -> saleProductDto.setProduct(productService.findById(saleProductDto
                .getProduct()
                .getId())));
        Sales sale = new Sales();
        sale = saleService.createSale(sale, principal);
        List<SaleProduct> saleProducts = new ArrayList<>();
        for (SaleProductDto dto : formDtos) {
            saleProducts.add(saleProductService.create(new SaleProduct(sale, productService.getProduct(dto
                    .getProduct()
                    .getId()), dto.getQuantity())));
        }
        sale.setSaleProducts(saleProducts);
        for (SaleProductDto dto : formDtos) {
            productService.updateAmount(dto.getProduct(), dto.getQuantity());
        }
        saleService.update(sale);
        return "redirect:/";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER')")
    @PostMapping(value = "/sale/{id}/delete")
    public String deleteSale(@PathVariable Long id) {
        saleService.deleteById(id);
        return "redirect:/sales";
    }

    @GetMapping(value = "/sale/{id}")
    public String getSaleById(@PathVariable Long id, Model model) throws NotFoundException {
        Sales sale = saleService.findById(id);
        if (Objects.isNull(sale)) {
            throw new NotFoundException("Sale with id " + id + " not found");
        }
        model.addAttribute("sale", sale);
        return "sale-info";
    }


    // SalesForm class for form validation
    public static class SalesForm {

        private List<SaleProductDto> productSales;

        public SalesForm(List<Product> products) {
            if (products != null && !products.isEmpty()) {
                this.productSales = products.stream()
                        .map(product -> new SaleProductDto(product, 0)) // Initialize quantity to 0
                        .collect(Collectors.toList());
            } else {
                this.productSales = new ArrayList<>();
            }
        }

        public List<SaleProductDto> getProductSales() {
            return productSales;
        }

        public void setProductSales(List<SaleProductDto> productSales) {
            this.productSales = productSales;
        }
    }
}
