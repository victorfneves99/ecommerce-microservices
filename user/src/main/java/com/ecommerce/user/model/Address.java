package com.ecommerce.user.model;


import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Address {

    @Id
    private Long id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

}
