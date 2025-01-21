package com.PazHotel.BookHotelPaz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PazHotel.BookHotelPaz.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
