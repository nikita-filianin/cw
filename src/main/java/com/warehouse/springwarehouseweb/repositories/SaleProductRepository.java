package com.warehouse.springwarehouseweb.repositories;

import com.warehouse.springwarehouseweb.models.SaleProduct;
import com.warehouse.springwarehouseweb.models.SaleProductPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface SaleProductRepository extends JpaRepository<SaleProduct, SaleProductPK> {

    @Modifying
    @Transactional
    @Query("DELETE FROM SaleProduct sp WHERE sp.pk.sale.id = :id")
    void deleteAllBySaleId(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM SaleProduct sp WHERE sp.pk.product.id = :id")
    void deleteAllByProductId(Long id);
}
