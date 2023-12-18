package com.warehouse.springwarehouseweb.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class SaleProduct {
    @EmbeddedId
    private SaleProductPK pk;
    @Column(nullable = false)
    private Integer quantity;

    public SaleProduct(Sales sales, Product product, Integer quantity) {
        pk = new SaleProductPK();
        pk.setSale(sales);
        pk.setProduct(product);
        this.quantity = quantity;
    }

    public Product getProduct() {
        return this.pk.getProduct();
    }

    public Double getTotalPrice() {
        return getProduct().getPrice() * getQuantity();
    }
}
