package com.hs.superherosightings.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
public class Address {

    private int addressId;
    
    @NotBlank(message = "Address must not be empty.")
    @Size(max=100, message = "Address must be less than 100 characters.")
    private String address;
    
    @NotBlank(message = "City must not be empty.")
    @Size(max=100, message = "City must be less than 50 characters.")
    private String city;
    
    @NotBlank(message = "Town must not be empty.")
    @Size(max=100, message = "Town must be 2 characters.")
    private String town;
    
    @NotBlank(message = "Postcode must not be empty.")
    @Size(min=6, max=8, message = "Postcode must be 6-8 digits.")
    private String postcode;

}
