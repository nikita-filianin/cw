package com.warehouse.springwarehouseweb.services;

import com.warehouse.springwarehouseweb.models.Sales;
import com.warehouse.springwarehouseweb.models.User;

import java.security.Principal;
import java.util.List;

public interface SaleService {
    List<Sales> findAll();

    Sales findById(Long id);

    void save(Sales sales);

    void deleteById(Long id);

    Sales createSale(Sales sales, Principal principal);

    void deleteSales(User user, Long id);

//    void editSales(Sales updSales, Long id);
}
