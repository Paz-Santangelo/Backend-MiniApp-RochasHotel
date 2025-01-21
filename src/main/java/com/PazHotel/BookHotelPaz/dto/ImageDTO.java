package com.PazHotel.BookHotelPaz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDTO {

    private Long id;
    private String urlImage;

}
