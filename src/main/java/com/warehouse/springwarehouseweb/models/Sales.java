package com.warehouse.springwarehouseweb.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "sales")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn
    private User user;

    private LocalDate saleDate;

    @OneToMany(mappedBy = "pk.sale")
    private List<SaleProduct> saleProducts = new ArrayList<>();

    public double getTotalSaleAmount() {
        return saleProducts.stream().mapToDouble(SaleProduct::getTotalPrice).sum();
    }

    public String getSaleTitle() {
        return "Sale #" + id + " - " + saleDate;
    }
}
