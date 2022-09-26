package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.REPOSITORY.*;
import com.hs.superherosightings.DTO.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class OrganisationDaoDBTest {
    
    @Autowired
    AddressDao addressDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    SightingDao sightingDao;
    
    @Autowired
    OrganisationDao organisationDao;
    
    @Autowired
    SuperheroDao superheroDao;
    
    @Autowired
    SupertypeDao supertypeDao;
    
    @Autowired
    SuperpowerDao superpowerDao;
    
    private Address address;
    private Superpower superpower;

    @BeforeEach
    public void setUpclass() {
        List<Organisation> organisations = organisationDao.getAllOrganisations();
        for (Organisation organisation : organisations){
            organisationDao.deleteOrganisationById(organisation.getOrgId());
        }
        
        List<Sighting> sightings = sightingDao.getAllSightings();
        for (Sighting sighting: sightings){
            sightingDao.deleteSightingById(sighting.getSightingId());
        }
        
        List<Superhero> superheros = superheroDao.getAllSuperhero();
        for (Superhero superhero : superheros){
            superheroDao.deleteSuperheroById(superhero.getSuperheroId());
        }
        
        List<Superpower> superpowers = superpowerDao.getAllSuperpowers();
        for (Superpower superpower : superpowers){
            superpowerDao.deleteSuperpowerById(superpower.getSuperpowerId());
        }
        
        List<Location> locations = locationDao.getAllLocations();
        for (Location location : locations){
            locationDao.deleteLocationById(location.getLocationId());
        }
        
        List<Address> addresses = addressDao.getAllAddresses();
        for (Address address : addresses){
            addressDao.deleteAddressById(address.getAddressId());
        }
        
        address = new Address();
        address.setAddress("37 Victoria Street");
        address.setCity("London");
        address.setTown("Watford");
        address.setPostcode("WD7 2CG");
        
        superpower = new Superpower();
        superpower.setSuperpowerName("Strength");
        superpower = superpowerDao.addSuperpower(superpower);
    }


     // Test of addOrganisation and getOrganisationById methods, of class OrganisationDatabaseDao.
    @Test
    public void testAddAndGetOrganisationById() {
        Organisation organisation1 = new Organisation();
        organisation1.setAddress(address);
        organisation1.setEmail("example@gmail.com");
        organisation1.setOrgDescription("Superheroes with Strength abilities");
        organisation1.setOrgName("Strong Squad");
        organisation1.setPhone("01234567890");
        organisation1.setSupertype(supertypeDao.getSupertypeById(1));
        organisation1.setMembers(new ArrayList<>());
        organisation1 = organisationDao.addOrganisation(organisation1);
        
        Organisation organisationFromDao = organisationDao.getOrganisationById(organisation1.getOrgId());
        assertNotNull(organisationFromDao);
        assertEquals(organisation1, organisationFromDao);
    }


    // Test of getAllOrganisations method, of class OrganisationDatabaseDao.

    @Test
    public void testGetAllOrganisations() {
        Organisation organisation1 = new Organisation();
        organisation1.setAddress(address);
        organisation1.setEmail("example@gmail.com");
        organisation1.setOrgDescription("Superheroes with Strength abilities");
        organisation1.setOrgName("Strong Squad");
        organisation1.setPhone("01234567890");
        organisation1.setSupertype(supertypeDao.getSupertypeById(1));
        organisation1.setMembers(new ArrayList<>());
        organisation1 = organisationDao.addOrganisation(organisation1);

        Organisation organisation2 = new Organisation();
        Address address2 = new Address();
        address2.setAddress("37 Victoria Street");
        address2.setCity("London");
        address2.setTown("Watford");
        address2.setPostcode("WD7 2C");
        organisation2.setAddress(address2);
        organisation2.setEmail("example@gmail.com");
        organisation2.setOrgDescription("Superheroes with Strength abilities");
        organisation2.setOrgName("Strong Squad");
        organisation2.setPhone("01234567890");
        organisation2.setSupertype(supertypeDao.getSupertypeById(2));
        organisation2.setMembers(new ArrayList<>());
        organisation2 = organisationDao.addOrganisation(organisation2);

        List<Organisation> organisations = organisationDao.getAllOrganisations();
        assertEquals(2, organisations.size());
        assertTrue(organisations.contains(organisation1));
        assertTrue(organisations.contains(organisation2));
    }


     //Test of updateOrganisation method, of class OrganisationDatabaseDao.

    @Test
    public void testUpdateOrganisation() {
        Organisation organisation1 = new Organisation();
        organisation1.setAddress(address);
        organisation1.setEmail("example@gmail.com");
        organisation1.setOrgDescription("Superheroes with Strength abilities");
        organisation1.setOrgName("Strong Squad");
        organisation1.setPhone("01234567890");
        organisation1.setSupertype(supertypeDao.getSupertypeById(1));
        organisation1.setMembers(new ArrayList<>());
        organisation1 = organisationDao.addOrganisation(organisation1);

        address.setAddress("37 Victoria Street");
        address.setCity("London");
        address.setTown("Watford");
        address.setPostcode("WD7 2C");
        organisation1.setAddress(address);
        organisation1.setEmail("example@gmail.com");
        organisation1.setOrgDescription("Superheroes with Strength abilities");
        organisation1.setOrgName("Strong Squad");
        organisation1.setPhone("01234567890");
        organisation1.setSupertype(supertypeDao.getSupertypeById(2));
        organisationDao.updateOrganisation(organisation1);
        
        Organisation fromDao = organisationDao.getOrganisationById(organisation1.getOrgId());
        assertEquals(organisation1, fromDao);
        
    }

    // Test of deleteOrganisationById method, of class OrganisationDatabaseDao.
    @Test
    public void testDeleteOrganisationById() {
        Organisation organisation1 = new Organisation();
        organisation1.setAddress(address);
        organisation1.setEmail("example@gmail.com");
        organisation1.setOrgDescription("Superheroes with Strength abilities");
        organisation1.setOrgName("Strong Squad");
        organisation1.setPhone("01234567890");
        organisation1.setSupertype(supertypeDao.getSupertypeById(1));
        organisation1.setMembers(new ArrayList<>());
        organisation1 = organisationDao.addOrganisation(organisation1);
        
        Organisation organisation2 = new Organisation();
        Address address2 = new Address();
        address2.setAddress("37 Victoria Street");
        address2.setCity("London");
        address2.setTown("Watford");
        address2.setPostcode("WD7 2C");
        organisation2.setAddress(address2);
        organisation2.setEmail("example@gmail.com");
        organisation2.setOrgDescription("Superheroes with Strength abilities");
        organisation2.setOrgName("Strong Squad");
        organisation2.setPhone("01234567890");
        organisation2.setSupertype(supertypeDao.getSupertypeById(2));
        organisation2.setMembers(new ArrayList<>());
        organisation2 = organisationDao.addOrganisation(organisation2);

        Superhero superhero1 = new Superhero();
        superhero1.setSuperheroDescription("Can run fast");
        superhero1.setSuperheroName("The Flash");
        superhero1.setSuperpower(superpower);
        superhero1.setSupertype(supertypeDao.getSupertypeById(1));
        superhero1.setOrganisations(organisationDao.getAllOrganisations());
        superhero1 = superheroDao.addSuperhero(superhero1);
        assertTrue(superhero1.getOrganisations().contains(organisation1));
        
        Address addressFromDao = addressDao.getAddressById(organisation1.getAddress().getAddressId());
        assertNotNull(addressFromDao);
        
        organisationDao.deleteOrganisationById(organisation1.getOrgId());
        Organisation organisationFromDao = organisationDao.getOrganisationById(organisation1.getOrgId());
        assertNull(organisationFromDao);
        
        superhero1 = superheroDao.getSuperheroById(superhero1.getSuperheroId());
        final int org1Id = organisation1.getOrgId();
        assertFalse(superhero1.getOrganisations().stream().anyMatch(o -> o.getOrgId() == org1Id));
        final int org2Id = organisation2.getOrgId();
        assertTrue(superhero1.getOrganisations().stream().anyMatch(o -> o.getOrgId() == org2Id));
        
        addressFromDao = addressDao.getAddressById(addressFromDao.getAddressId());
        assertNull(addressFromDao);
        
    }


     // Test of getOrganisationsForSuperhero method, of class OrganisationDatabaseDao.
    @Test
    public void testGetOrganisationsForSuperhero() {
        
        Organisation organisation1 = new Organisation();
        organisation1.setAddress(address);
        organisation1.setEmail("example@gmail.com");
        organisation1.setOrgDescription("Superheroes with Strength abilities");
        organisation1.setOrgName("Strong Squad");
        organisation1.setPhone("01234567890");
        organisation1.setSupertype(supertypeDao.getSupertypeById(1));
        organisation1.setMembers(new ArrayList<>());
        organisation1 = organisationDao.addOrganisation(organisation1);
        
        Organisation organisation2 = new Organisation();
        Address address2 = new Address();
        address2.setAddress("37 Victoria Street");
        address2.setCity("London");
        address2.setTown("Watford");
        address2.setPostcode("WD7 2C");
        organisation2.setAddress(address2);
        organisation2.setEmail("example@gmail.com");
        organisation2.setOrgDescription("Superheroes with Strength abilities");
        organisation2.setOrgName("Strong Squad");
        organisation2.setPhone("01234567890");
        organisation2.setSupertype(supertypeDao.getSupertypeById(1));
        organisation2.setMembers(new ArrayList<>());
        organisation2 = organisationDao.addOrganisation(organisation2);
        
        Organisation organisation3 = new Organisation();
        Address address3 = new Address();
        address3.setAddress("456 Real St");
        address3.setCity("Tucson");
        address3.setTown("AZ");
        address3.setPostcode("80210");
        organisation3.setAddress(address3);
        organisation3.setEmail("press@villains.com");
        organisation3.setOrgDescription("Supervillains of flight");
        organisation3.setOrgName("Evil Bird Squad");
        organisation3.setPhone("808-808-8080");
        organisation3.setSupertype(supertypeDao.getSupertypeById(2));
        organisation3.setMembers(new ArrayList<>());
        organisation3 = organisationDao.addOrganisation(organisation3);
        
        Superhero superhero1 = new Superhero();
        superhero1.setSuperheroDescription("Can run fast");
        superhero1.setSuperheroName("The Flash");
        superhero1.setSuperpower(superpower);
        superhero1.setSupertype(supertypeDao.getSupertypeById(1));
        List<Organisation> organisations = new ArrayList<>();
        organisations.add(organisation1);
        organisations.add(organisation2);
        superhero1.setOrganisations(organisations);
        superhero1 = superheroDao.addSuperhero(superhero1);
        
        organisation1 = organisationDao.getOrganisationById(organisation1.getOrgId());
        organisation2 = organisationDao.getOrganisationById(organisation2.getOrgId());
        organisation3 = organisationDao.getOrganisationById(organisation3.getOrgId());
        
        List<Organisation> expectedOrganisations = organisationDao.getOrganisationsForSuperhero(superhero1);
        assertEquals(2, expectedOrganisations.size());
        assertTrue(expectedOrganisations.contains(organisation1));
        assertTrue(expectedOrganisations.contains(organisation2));
        assertFalse(expectedOrganisations.contains(organisation3));
    }
    
}
