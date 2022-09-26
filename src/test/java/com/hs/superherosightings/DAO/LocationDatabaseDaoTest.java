package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.REPOSITORY.*;
import com.hs.superherosightings.DTO.Superhero;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hs.superherosightings.DTO.*;

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
public class LocationDatabaseDaoTest {
    
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
    private Superhero superhero;
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
        
        Organisation organisation1 = new Organisation();
        organisation1.setAddress(address);
        organisation1.setEmail("example@gmail.com");
        organisation1.setOrgDescription("Superheroes with Strength abilities");
        organisation1.setOrgName("Strong Squad");
        organisation1.setPhone("01234567890");
        organisation1.setSupertype(supertypeDao.getSupertypeById(1));
        organisation1.setMembers(new ArrayList<>());
        organisation1 = organisationDao.addOrganisation(organisation1);
        organisations = new ArrayList<>();
        organisations.add(organisation1);
        
        superhero = new Superhero();
        superhero.setSuperheroDescription("Can lift a car with one hand");
        superhero.setSuperheroName("Hulk");
        superhero.setSuperpower(superpower);
        superhero.setSupertype(supertypeDao.getSupertypeById(1));
        superhero.setOrganisations(organisations);
        superhero = superheroDao.addSuperhero(superhero);
        
    }

     // Test of addLocation and getLocationById method, of class LocationDatabaseDao.
    @Test
    public void testAddAndGetLocation() {
        Location location1 = new Location();
        location1.setAddress(address);
        location1.setLatitude(34.236162);
        location1.setLongitude(-162.7638235);
        location1.setLocationDescription("Hulk homes");
        location1.setLocationName("Strongo");
        
        Location fromDao = locationDao.addLocation(location1);
        assertNotNull(fromDao);
        
        location1.setLocationId(fromDao.getLocationId());
        assertEquals(location1, fromDao);
    }


    // Test of getAllLocations method, of class LocationDatabaseDao.
    @Test
    public void testGetAllLocations() {
        Location location1 = new Location();
        location1.setAddress(address);
        location1.setLatitude(45.426441);
        location1.setLongitude(-122.766197);
        location1.setLocationDescription("Hulk homes");
        location1.setLocationName("Strongo");
        location1 = locationDao.addLocation(location1);
        
        Location location2 = new Location();
        Address address2 = new Address();
        address2.setAddress("37 Victoria Street");
        address2.setCity("London");
        address2.setTown("Watford");
        address2.setPostcode("WD7 2C");
        location2.setAddress(address2);
        location2.setLatitude(34.236162);
        location2.setLongitude(-162.7638235);
        location2.setLocationDescription("Heaven");
        location2.setLocationName("Thunderstruck");
        location2 = locationDao.addLocation(location2);
        
        List<Location> locations = locationDao.getAllLocations();
        assertEquals(2, locations.size());
        
        assertTrue(locations.contains(location1));
        assertTrue(locations.contains(location2));
    }


     //Test of updateLocation method, of class LocationDatabaseDao.
    @Test
    public void testUpdateLocation() {
        Location location1 = new Location();
        location1.setAddress(address);
        location1.setLatitude(34.236162);
        location1.setLongitude(-162.7638235);
        location1.setLocationDescription("Hulk homes");
        location1.setLocationName("Strongo");
        location1 = locationDao.addLocation(location1);

        address.setAddress("37 Victoria Street");
        address.setCity("London");
        address.setTown("Watford");
        address.setPostcode("WD7 2C");
        location1.setLatitude(34.236162);
        location1.setLongitude(-162.7638235);
        location1.setLocationDescription("Heaven");
        location1.setLocationName("Thunderstruck");
        locationDao.updateLocation(location1);
        Location fromDao = locationDao.getLocationById(location1.getLocationId());
        
        assertEquals(location1, fromDao);
    }


     //Test of deleteLocationById method, of class LocationDatabaseDao.
    @Test
    public void testDeleteLocationById() {
        
        Location location1 = new Location();
        location1.setAddress(address);
        location1.setLatitude(34.236162);
        location1.setLongitude(-162.7638235);
        location1.setLocationDescription("Hulk homes");
        location1.setLocationName("Strongo");
        location1 = locationDao.addLocation(location1);
        Location locationFromDao = locationDao.getLocationById(location1.getLocationId());
        assertEquals(location1, locationFromDao);
        
        Sighting sighting1 = new Sighting();
        sighting1.setSuperhero(superhero);
        sighting1.setSightingDatetime(LocalDateTime.now());
        sighting1.setLocation(location1);
        sighting1 = sightingDao.addSighting(sighting1);
        assertNotNull(sighting1);
        
        locationDao.deleteLocationById(location1.getLocationId());
        locationFromDao = locationDao.getLocationById(location1.getLocationId());
        assertNull(locationFromDao);
        
        Address addressFromDao = addressDao.getAddressById(location1.getAddress().getAddressId());
        assertNull(addressFromDao);
        
        Sighting sightingFromDao =sightingDao.getSightingById(sighting1.getSightingId());
        assertNull(sightingFromDao);
        
    }


     //Test of getLocationsForSuperhero method, of class LocationDatabaseDao.
    @Test
    public void testGetLocationsForSuperhero() {
        
        Location location1 = new Location();
        location1.setAddress(address);
        location1.setLatitude(34.236162);
        location1.setLongitude(-162.7638235);
        location1.setLocationDescription("Hulk homes");
        location1.setLocationName("Strongo");
        location1 = locationDao.addLocation(location1);
        assertNotNull(location1);
        
        Location location2 = new Location();
        Address address2 = new Address();
        address2.setAddress("37 Victoria Street");
        address2.setCity("London");
        address2.setTown("Watford");
        address2.setPostcode("WD7 2C");
        location2.setAddress(address2);
        location2.setLatitude(34.236162);
        location2.setLongitude(-162.7638235);
        location2.setLocationDescription("Heaven");
        location2.setLocationName("Thunderstruck");
        location2 = locationDao.addLocation(location2);
        assertNotNull(location2);
        
        Location location3 = new Location();
        Address address3 = new Address();
        address3.setAddress("37 Victoria Street");
        address3.setCity("Machester");
        address3.setTown("Bury");
        address3.setPostcode("M1 1AG");
        location3.setAddress(address3);
        location3.setLatitude(25.827237);
        location3.setLongitude(-121.9232345);
        location3.setLocationDescription("For Junior Superheros");
        location3.setLocationName("Superherjos");
        location3 = locationDao.addLocation(location3);
        assertNotNull(location3);
        
        Superhero superhero2 = new Superhero();
        superhero2.setSuperheroDescription("Can run fast");
        superhero2.setSuperheroName("The Flash");
        superhero2.setSuperpower(superpower);
        superhero2.setSupertype(supertypeDao.getSupertypeById(1));
        superhero2.setOrganisations(superhero.getOrganisations());
        superheroDao.addSuperhero(superhero2);
        
        Sighting sighting1 = new Sighting();
        sighting1.setSuperhero(superhero);
        sighting1.setSightingDatetime(LocalDateTime.now());
        sighting1.setLocation(location1);
        sighting1 = sightingDao.addSighting(sighting1);
        assertNotNull(sighting1);
        
        Sighting sighting2 = new Sighting();
        sighting2.setSuperhero(superhero);
        sighting2.setSightingDatetime(LocalDateTime.now());
        sighting2.setLocation(location2);
        sighting2 = sightingDao.addSighting(sighting2);
        assertNotNull(sighting2);
        
        Sighting sighting3 = new Sighting();
        sighting3.setSuperhero(superhero2);
        sighting3.setSightingDatetime(LocalDateTime.now());
        sighting3.setLocation(location3);
        sighting3 = sightingDao.addSighting(sighting3);
        assertNotNull(sighting3);
        
        List<Location> locationsFromDao = locationDao.getLocationsForSuperhero(superhero);
        assertEquals(2, locationsFromDao.size());
        assertTrue(locationsFromDao.contains(location1));
        assertTrue(locationsFromDao.contains(location2));
        assertFalse(locationsFromDao.contains(location3));
        
    }
    
}
