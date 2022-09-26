package com.hs.superherosightings.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
public class Organisation {

    private int orgId;
    
    @NotBlank(message = "Name must not be empty.")
    @Size(max=50, message = "Name must be less than 50 characters.")
    private String orgName;
    
    @NotBlank(message = "Description must not be empty.")
    @Size(max=100, message = "Description must be less than 100 characters.")
    private String orgDescription;
    
    @NotBlank(message = "Email must not be empty.")
    @Size(max=50, message = "Email must be less than 50 characters.")
    @Email
    private String email;
    
    @Pattern(regexp="^\\d{3}-\\d{3}-\\d{4}$", message="Phone must match pattern 555-555-5555")
    private String phone;
    
    private Address address;
    private Supertype supertype;
    private List<Superhero> members;

}
