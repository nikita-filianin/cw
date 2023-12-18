package com.warehouse.springwarehouseweb.repositories;

import com.warehouse.springwarehouseweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);

//    User findUserByLogin(String login);
}
