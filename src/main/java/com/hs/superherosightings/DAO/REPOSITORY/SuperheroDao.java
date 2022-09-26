package com.hs.superherosightings.DAO.REPOSITORY;

import com.hs.superherosightings.DTO.Location;
import com.hs.superherosightings.DTO.Organisation;
import com.hs.superherosightings.DTO.Superhero;

import java.util.List;


public interface SuperheroDao {
    
    Superhero getSuperheroById(int id);
    List<Superhero> getAllSuperhero();
    Superhero addSuperhero(Superhero superhero);
    void updateSuperhero(Superhero superhero);
    void deleteSuperheroById(int id);
    
    List<Superhero> getSuperheroLocation(Location location);
    List<Superhero> getSuperheroOrganisation(Organisation organisation);
    
}
