package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.REPOSITORY.*;
import com.hs.superherosightings.DTO.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SightingDatabaseDaoTest {
    
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
    
    private Superhero superhero;
    private Superpower superpower;
    private Location location;

    
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

        Address address1 = new Address();
        address1.setAddress("37 Victoria Street");
        address1.setCity("London");
        address1.setTown("Watford");
        address1.setPostcode("WD7 2CG");
        
        superpower = new Superpower();
        superpower.setSuperpowerName("Strength");
        superpower = superpowerDao.addSuperpower(superpower);
        
        Organisation organisation1 = new Organisation();
        organisation1.setAddress(address1);
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
        superhero.setSuperheroDescription("Can run fast");
        superhero.setSuperheroName("The Flash");
        superhero.setSuperpower(superpower);
        superhero.setSupertype(supertypeDao.getSupertypeById(1));
        superhero.setOrganisations(organisations);
        superhero = superheroDao.addSuperhero(superhero);
        
        location = new Location();
        location.setAddress(address1);
        location.setLatitude(34.236162);
        location.setLongitude(-162.7638235);
        location.setLocationDescription("Hulk homes");
        location.setLocationName("Strongo");
        location = locationDao.addLocation(location);
        
    }

     //Test of addSighting and getSightingById methods, of class SightingDatabaseDao.
    @Test
    public void testAddAndGetSightingById() {
        
        Sighting sighting1 = new Sighting();
        sighting1.setSuperhero(superhero);
        sighting1.setSightingDatetime(LocalDateTime.of(2022, Month.JUNE, 23, 8, 34));
        sighting1.setLocation(location);
        sighting1 = sightingDao.addSighting(sighting1);
        assertNotNull(sighting1);
        
        Sighting fromDao = sightingDao.getSightingById(sighting1.getSightingId());
        assertEquals(sighting1.getSightingDatetime(), fromDao.getSightingDatetime());
        assertEquals(sighting1.getSuperhero().getSuperheroId(), fromDao.getSuperhero().getSuperheroId());
        assertEquals(sighting1.getLocation().getLocationId(), fromDao.getLocation().getLocationId());
    }


     // Test of getAllSightings method, of class SightingDatabaseDao.
    @Test
    public void testGetAllSightings() {
        
        Sighting sighting1 = new Sighting();
        sighting1.setSuperhero(superhero);
        sighting1.setSightingDatetime(LocalDateTime.of(2022, Month.JUNE, 23, 8, 34));
        sighting1.setLocation(location);
        sighting1 = sightingDao.addSighting(sighting1);
        
        Sighting sighting2 = new Sighting();
        sighting2.setSuperhero(superhero);
        sighting2.setSightingDatetime(LocalDateTime.of(2022, Month.JANUARY, 3, 2, 12));
        sighting2.setLocation(location);
        sighting2 = sightingDao.addSighting(sighting2);
        assertNotEquals(sighting1, sighting2);
        
        List<Sighting> sightings = sightingDao.getAllSightings();
        assertEquals(2, sightings.size());
        assertFalse(sightings.contains(sighting1));
        assertFalse(sightings.contains(sighting2));
    }


     // Test of updateSighting method, of class SightingDatabaseDao.
    @Test
    public void testUpdateSighting() {
        
        Sighting sighting1 = new Sighting();
        sighting1.setSuperhero(superhero);
        sighting1.setSightingDatetime(LocalDateTime.of(2022, Month.JUNE, 23, 8, 34));
        sighting1.setLocation(location);
        sighting1 = sightingDao.addSighting(sighting1);
        
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
        
        Superhero superhero2 = new Superhero();
        superhero2.setSuperheroDescription("Can run fast");
        superhero2.setSuperheroName("The Flash");
        superhero2.setSuperpower(superpower);
        superhero2.setSupertype(supertypeDao.getSupertypeById(1));
        superhero2.setOrganisations(superhero.getOrganisations());
        superhero2 = superheroDao.addSuperhero(superhero2);
        
        sighting1.setSightingDatetime(LocalDateTime.of(2022, Month.JUNE, 23, 8, 34));
        sighting1.setLocation(location2);
        sighting1.setSuperhero(superhero2);
        sightingDao.updateSighting(sighting1);
        
        Sighting sightingFromDao = sightingDao.getSightingById(sighting1.getSightingId());
        assertNotNull(sightingFromDao);
        assertEquals(sighting1.getSightingDatetime(), sightingFromDao.getSightingDatetime());
        assertEquals(sighting1.getLocation().getLocationId(), sightingFromDao.getLocation().getLocationId());
        assertEquals(sighting1.getSuperhero().getSuperheroId(), sightingFromDao.getSuperhero().getSuperheroId());
    }


     // Test of deleteSightingById method, of class SightingDatabaseDao.
    @Test
    public void testDeleteSightingById() {
        
        Sighting sighting1 = new Sighting();
        sighting1.setSuperhero(superhero);
        sighting1.setSightingDatetime(LocalDateTime.of(2022, Month.JUNE, 23, 8, 34));
        sighting1.setLocation(location);
        sighting1 = sightingDao.addSighting(sighting1);
        sighting1 = sightingDao.getSightingById(sighting1.getSightingId());
        assertNotNull(sighting1);
        
        sightingDao.deleteSightingById(sighting1.getSightingId());
        Sighting sightingFromDao = sightingDao.getSightingById(sighting1.getSightingId());
        assertNull(sightingFromDao);
        
    }


     // Test of getMostRecentSightings method, of class SightingDatabaseDao.
    @Test
    public void testGetMostRecentSightings() {
        
        Sighting sighting1 = new Sighting();
        sighting1.setSuperhero(superhero);
        sighting1.setSightingDatetime(LocalDateTime.of(2022, Month.JUNE, 23, 8, 34));
        sighting1.setLocation(location);
        sighting1 = sightingDao.addSighting(sighting1);
        
        Sighting sighting2 = new Sighting();
        sighting2.setSuperhero(superhero);
        sighting2.setSightingDatetime(LocalDateTime.of(2022, Month.JANUARY, 3, 2, 12));
        sighting2.setLocation(location);
        sighting2 = sightingDao.addSighting(sighting2);
        
        Sighting sighting3 = new Sighting();
        sighting3.setSuperhero(superhero);
        sighting3.setSightingDatetime(LocalDateTime.of(2022, Month.MAY, 4, 7, 27));
        sighting3.setLocation(location);
        sighting3 = sightingDao.addSighting(sighting3);
        
        List<Sighting> mostRecentSightings = sightingDao.getMostRecentSightings(2);
        assertEquals(2, mostRecentSightings.size());
        assertFalse(mostRecentSightings.contains(sighting2));
        assertFalse(mostRecentSightings.contains(sighting3));
        assertFalse(mostRecentSightings.contains(sighting1));
        
    }
    
}
