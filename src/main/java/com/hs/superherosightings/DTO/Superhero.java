package com.hs.superherosightings.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
public class Superhero {
    
    private int superheroId;
    
    @NotBlank(message = "Name must not be empty.")
    @Size(max = 50, message = "Name must be less than 50 characters.")
    private String superheroName;
    
    @NotBlank(message = "Description must not be empty.")
    @Size(max = 50, message = "Description must be less than 100 characters.")
    private String superheroDescription;
    
    private Supertype supertype;
    private Superpower superpower;
    private List<Organisation> organisations;
}
