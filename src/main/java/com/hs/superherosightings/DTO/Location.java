package com.hs.superherosightings.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
public class Location {
    
    private int locationId;
    
    @NotBlank(message = "Name must not be empty.")
    @Size(max = 50, message = "Name must be less than 50 characters.")
    private String locationName;
    
    @NotBlank(message = "Description must not be empty.")
    @Size(max = 50, message = "Description must be less than 50 characters.")
    private String locationDescription;
    
    @Min(value=-90, message="Latitude must be greater than -90.")
    @Max(value=90, message="Latitude must be less than 90.")
    private double latitude;
    
    @Min(value=-180, message="Longitude must be greater than -180.")
    @Max(value=180, message="Longitude must be less than 180.")
    private double longitude;
    
    private Address address;

}
