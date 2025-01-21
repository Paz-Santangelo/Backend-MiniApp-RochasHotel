package com.PazHotel.BookHotelPaz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PazHotel.BookHotelPaz.model.ImageRoom;

@Repository
public interface IImageRoomRepository extends JpaRepository<ImageRoom, Long>{

    List<ImageRoom> findByRoom_Id(Long roomId);

}
