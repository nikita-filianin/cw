package com.warehouse.springwarehouseweb.repositories;

import com.warehouse.springwarehouseweb.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
