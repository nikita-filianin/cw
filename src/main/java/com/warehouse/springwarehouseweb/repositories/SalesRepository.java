package com.warehouse.springwarehouseweb.repositories;

import com.warehouse.springwarehouseweb.models.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    List<Sales> findAllBySaleDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("select a from Sales a where a.saleDate >= :startDate")
    List<Sales> findAllBySaleDateAfter(LocalDate startDate);

    @Query("select a from Sales a where a.saleDate <= :endDate")
    List<Sales> findAllBySaleDateBefore(LocalDate endDate);

    List<Sales> findAllBySaleDateBetweenAndUserId(LocalDate startDate, LocalDate endDate, Long id);


    @Query("select a from Sales a where a.saleDate >= :startDate and a.user.id = :id")
    List<Sales> findAllBySaleDateAfterAndUserId(LocalDate startDate, Long id);

    @Query("select a from Sales a where a.saleDate <= :endDate and a.user.id = :id")
    List<Sales> findAllBySaleDateBeforeAndUserId(LocalDate endDate, Long id);

    List<Sales> findAllByUserId(Long id);
}
