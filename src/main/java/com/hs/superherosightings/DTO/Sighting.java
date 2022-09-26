package com.hs.superherosightings.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import javax.validation.constraints.Past;

@Getter
@Setter
@EqualsAndHashCode
public class Sighting {

    private int sightingId;
    private Superhero superhero;
    private Location location;
    
    @Past(message = "Sighting must be in the past.")
    private LocalDateTime sightingDatetime;
}
