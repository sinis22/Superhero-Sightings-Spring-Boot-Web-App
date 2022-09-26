package com.hs.superherosightings.DAO.REPOSITORY;

import com.hs.superherosightings.DTO.Sighting;

import java.util.List;

public interface SightingDao {
    
    Sighting getSightingById(int id);
    List<Sighting> getAllSightings();
    Sighting addSighting(Sighting sighting);
    void updateSighting(Sighting sighting);
    void deleteSightingById(int id);
    
    List<Sighting> getMostRecentSightings(int numSightings);
    
}
