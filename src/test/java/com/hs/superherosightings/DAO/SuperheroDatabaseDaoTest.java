package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.REPOSITORY.*;
import com.hs.superherosightings.DTO.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class SuperheroDatabaseDaoTest {
    
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
    
    public SuperheroDatabaseDaoTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
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
    
    @AfterEach
    public void tearDown() {
    }


     // Test of addSuperhero and getSuperheroById methods, of class SuperheroDatabaseDao.
    @Test
    public void testAddAndGetSuperheroById() {
        
        Superhero superhero1 = new Superhero();
        superhero1.setSuperheroDescription("Can run fast");
        superhero1.setSuperheroName("The Flash");
        superhero1.setSuperpower(superpower);
        superhero1.setSupertype(supertypeDao.getSupertypeById(1));
        List<Organisation> organisations = organisationDao.getAllOrganisations();
        superhero1.setOrganisations(organisations);
        superhero1 = superheroDao.addSuperhero(superhero1);
        
        Superhero superheroFromDao = superheroDao.getSuperheroById(superhero1.getSuperheroId());
        assertNotNull(superheroFromDao);
        assertEquals(superhero1, superheroFromDao);
        
    }


     // Test of getAllSuperhero method, of class SuperheroDatabaseDao.
    @Test
    public void testGetAllSuperhero() {
        
        // Create first superhero
        Superhero superhero1 = new Superhero();
        superhero1.setSuperheroDescription("Can run fast");
        superhero1.setSuperheroName("The Flash");
        superhero1.setSuperpower(superpower);
        superhero1.setSupertype(supertypeDao.getSupertypeById(1));
        List<Organisation> organisations = organisationDao.getAllOrganisations();
        superhero1.setOrganisations(organisations);
        superhero1 = superheroDao.addSuperhero(superhero1);
        
        // Create second superhero
        Superhero superhero2 = new Superhero();
        superhero2.setSuperheroDescription("Has great strength");
        superhero2.setSuperheroName("Hulk");
        superhero2.setSuperpower(superpower);
        superhero2.setSupertype(supertypeDao.getSupertypeById(1));
        superhero2.setOrganisations(superhero1.getOrganisations());
        superhero2 = superheroDao.addSuperhero(superhero2);
        
        List<Superhero> superhero = superheroDao.getAllSuperhero();
        assertEquals(2, superhero.size());
        assertTrue(superhero.contains(superhero1));
        assertTrue(superhero.contains(superhero2));
        
    }


     // Test of updateSuperhero method, of class SuperheroDatabaseDao.
    @Test
    public void testUpdateSuperhero() {
        
        // Create superhero
        Superhero superhero1 = new Superhero();
        superhero1.setSuperheroDescription("Can run fast");
        superhero1.setSuperheroName("The Flash");
        superhero1.setSuperpower(superpower);
        superhero1.setSupertype(supertypeDao.getSupertypeById(1));
        List<Organisation> organisations = organisationDao.getAllOrganisations();
        superhero1.setOrganisations(organisations);
        superhero1 = superheroDao.addSuperhero(superhero1);
        
        // Update superhero
        superhero1.setSuperheroDescription("has great strength");
        superhero1.setSuperheroName("Hulk");
        
        Superpower superpower2 = new Superpower();
        superpower2.setSuperpowerName("Invisibility");
        superpower2 = superpowerDao.addSuperpower(superpower2);
        superhero1.setSuperpower(superpower2);
        
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
        organisationDao.addOrganisation(organisation2);
        superhero1.setOrganisations(organisationDao.getAllOrganisations());
        
        superheroDao.updateSuperhero(superhero1);
        
        Superhero superheroFromDao = superheroDao.getSuperheroById(superhero1.getSuperheroId());
        assertNotNull(superheroFromDao);
        assertEquals(superhero1.getSuperpower(), superheroFromDao.getSuperpower());
        assertEquals(superhero1.getSuperheroName(), superheroFromDao.getSuperheroName());
        
    }


     // Test of deleteSuperheroById method, of class SuperheroDatabaseDao.
    @Test
    public void testDeleteSuperheroById() {
        
        // Create superhero
        Superhero superhero1 = new Superhero();
        superhero1.setSuperheroDescription("Can run fast");
        superhero1.setSuperheroName("The Flash");
        superhero1.setSuperpower(superpower);
        superhero1.setSupertype(supertypeDao.getSupertypeById(1));
        List<Organisation> organisations = organisationDao.getAllOrganisations();
        superhero1.setOrganisations(organisations);
        superhero1 = superheroDao.addSuperhero(superhero1);
        
        Location location1 = new Location();
        location1.setAddress(address);
        location1.setLatitude(34.236162);
        location1.setLongitude(-162.7638235);
        location1.setLocationDescription("Hulk homes");
        location1.setLocationName("Strongo");
        location1 = locationDao.addLocation(location1);
        
        Sighting sighting1 = new Sighting();
        sighting1.setSuperhero(superhero1);
        sighting1.setSightingDatetime(LocalDateTime.now());
        sighting1.setLocation(location1);
        sighting1 = sightingDao.addSighting(sighting1);
        assertNotNull(sighting1);
        
        superheroDao.deleteSuperheroById(superhero1.getSuperheroId());
        Superhero superheroFromDao = superheroDao.getSuperheroById(superhero1.getSuperheroId());
        assertNull(superheroFromDao);
        
        Sighting sightingFromDao = sightingDao.getSightingById(sighting1.getSightingId());
        assertNull(sightingFromDao);
        
        organisations = organisationDao.getAllOrganisations();
        for (Organisation organisation : organisations){
            List<Superhero> members = organisation.getMembers();
            assertFalse(members.contains(superhero1));
        }
    }


     // Test of getSuperheroLocation method, of class SuperheroDatabaseDao.
    @Test
    public void testGetSuperheroForLocation() {
        
        // Create superhero expected at location
        Superhero superhero1 = new Superhero();
        superhero1.setSuperheroDescription("Can run fast");
        superhero1.setSuperheroName("The Flash");
        superhero1.setSuperpower(superpower);
        superhero1.setSupertype(supertypeDao.getSupertypeById(1));
        List<Organisation> organisations = organisationDao.getAllOrganisations();
        superhero1.setOrganisations(organisations);
        superhero1 = superheroDao.addSuperhero(superhero1);
        
        Superhero superhero2 = new Superhero();
        superhero2.setSuperheroDescription("Has great strength");
        superhero2.setSuperheroName("Hulk");
        superhero2.setSuperpower(superpower);
        superhero2.setSupertype(supertypeDao.getSupertypeById(1));
        superhero2.setOrganisations(superhero1.getOrganisations());
        superhero2 = superheroDao.addSuperhero(superhero2);
        
        // Create superhero not expected at location
        Superhero superhero3 = new Superhero();
        superhero3.setSuperheroDescription("Lightning as weapon");
        superhero3.setSuperheroName("Thunderbolt");
        superhero3.setSuperpower(superpower);
        superhero3.setSupertype(supertypeDao.getSupertypeById(1));
        superhero3.setOrganisations(superhero1.getOrganisations());
        superhero3 = superheroDao.addSuperhero(superhero3);
        
        // Create Locations        
        Location location1 = new Location();
        location1.setAddress(address);
        location1.setLatitude(34.236162);
        location1.setLongitude(-162.7638235);
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
        
        // Add sightings for superhero
        Sighting sighting1 = new Sighting();
        sighting1.setSuperhero(superhero1);
        sighting1.setSightingDatetime(LocalDateTime.now());
        sighting1.setLocation(location1);
        sightingDao.addSighting(sighting1);
        
        Sighting sighting2 = new Sighting();
        sighting2.setSuperhero(superhero2);
        sighting2.setSightingDatetime(LocalDateTime.now());
        sighting2.setLocation(location1);
        sightingDao.addSighting(sighting2);
        
        Sighting sighting3 = new Sighting();
        sighting3.setSuperhero(superhero3);
        sighting3.setSightingDatetime(LocalDateTime.now());
        sighting3.setLocation(location2);
        sightingDao.addSighting(sighting3);
        
        // Get superhero for location1
        List<Superhero> superheroForLocation = superheroDao.getSuperheroLocation(location1);
        assertEquals(2, superheroForLocation.size());
        final int superhero1Id = superhero1.getSuperheroId();
        final int superhero2Id = superhero2.getSuperheroId();
        final int superhero3Id = superhero3.getSuperheroId();
        assertTrue(superheroForLocation.stream().anyMatch(s -> s.getSuperheroId() == superhero1Id));
        assertTrue(superheroForLocation.stream().anyMatch(s -> s.getSuperheroId() == superhero2Id));
        assertFalse(superheroForLocation.stream().anyMatch(s -> s.getSuperheroId() == superhero3Id));
    }


     // Test of getSuperheroOrganisation method, of class SuperheroDatabaseDao.
    @Test
    public void testGetSuperheroForOrganisation() {
        
        Organisation organisation1 = new Organisation();
        organisation1.setAddress(address);
        organisation1.setEmail("example@gmail.com");
        organisation1.setOrgDescription("Superheroes with Strength abilities");
        organisation1.setOrgName("Strong Squad");
        organisation1.setPhone("01234567890");
        organisation1.setSupertype(supertypeDao.getSupertypeById(1));
        organisation1.setMembers(new ArrayList<>());
        organisation1 = organisationDao.addOrganisation(organisation1);
        List<Organisation> expectedOrganisations = new ArrayList<>();
        expectedOrganisations.add(organisation1);
        
        Organisation organisation2 = new Organisation();
        organisation2.setAddress(address);
        organisation2.setEmail("example@gmail.com");
        organisation2.setOrgDescription("Superheroes with Strength abilities");
        organisation2.setOrgName("Strong Squad");
        organisation2.setPhone("01234567890");
        organisation2.setSupertype(supertypeDao.getSupertypeById(1));
        organisation2.setMembers(new ArrayList<>());
        organisation2 = organisationDao.addOrganisation(organisation2);
        List<Organisation> unexpectedOrganisations = new ArrayList<>();
        unexpectedOrganisations.add(organisation2);
        
        Superhero superhero1 = new Superhero();
        superhero1.setSuperheroDescription("Can run fast");
        superhero1.setSuperheroName("The Flash");
        superhero1.setSuperpower(superpower);
        superhero1.setSupertype(supertypeDao.getSupertypeById(1));
        superhero1.setOrganisations(expectedOrganisations);
        superhero1 = superheroDao.addSuperhero(superhero1);
        
        Superhero superhero2 = new Superhero();
        superhero2.setSuperheroDescription("Can run at speed of light");
        superhero2.setSuperheroName("Reverse Flash");
        superhero2.setSuperpower(superpower);
        superhero2.setSupertype(supertypeDao.getSupertypeById(1));
        superhero2.setOrganisations(expectedOrganisations);
        superhero2 = superheroDao.addSuperhero(superhero2);
        
        Superhero superhero3 = new Superhero();
        superhero3.setSuperheroDescription("Is invisibile");
        superhero3.setSuperheroName("Mr Invisible");
        superhero3.setSuperpower(superpower);
        superhero3.setSupertype(supertypeDao.getSupertypeById(1));
        superhero3.setOrganisations(unexpectedOrganisations);
        superhero3 = superheroDao.addSuperhero(superhero3);
        
        List<Superhero> superheroFromDao = superheroDao.getSuperheroOrganisation(organisation1);
        assertEquals(2, superheroFromDao.size());
        final int superhero1Id = superhero1.getSuperheroId();
        final int superhero2Id = superhero2.getSuperheroId();
        final int superhero3Id = superhero3.getSuperheroId();
        assertTrue(superheroFromDao.stream().anyMatch(s -> s.getSuperheroId() == superhero1Id));
        assertTrue(superheroFromDao.stream().anyMatch(s -> s.getSuperheroId() == superhero2Id));
        assertFalse(superheroFromDao.stream().anyMatch(s -> s.getSuperheroId() == superhero3Id));
    }
    
}
