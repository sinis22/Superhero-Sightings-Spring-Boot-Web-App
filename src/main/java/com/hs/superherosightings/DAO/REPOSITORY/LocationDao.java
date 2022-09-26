package com.hs.superherosightings.DAO.REPOSITORY;

import com.hs.superherosightings.DTO.Location;
import com.hs.superherosightings.DTO.Superhero;

import java.util.List;


public interface LocationDao {

    Location getLocationById(int id);
    List<Location> getAllLocations();
    Location addLocation(Location location);
    void updateLocation(Location location);
    void deleteLocationById(int id);
    
    List<Location> getLocationsForSuperhero(Superhero superhero);
    
}
