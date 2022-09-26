package com.hs.superherosightings.DAO.REPOSITORY;

import com.hs.superherosightings.DTO.Organisation;
import com.hs.superherosightings.DTO.Superhero;

import java.util.List;


public interface OrganisationDao {

    Organisation getOrganisationById(int id);
    List<Organisation> getAllOrganisations();
    Organisation addOrganisation(Organisation organisation);
    void updateOrganisation(Organisation organisation);
    void deleteOrganisationById(int id);
    
    List<Organisation> getOrganisationsForSuperhero(Superhero superhero);
    
}
