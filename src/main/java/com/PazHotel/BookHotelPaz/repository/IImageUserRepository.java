package com.PazHotel.BookHotelPaz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PazHotel.BookHotelPaz.model.ImageUser;

@Repository
public interface IImageUserRepository extends JpaRepository<ImageUser, Long> {

}
