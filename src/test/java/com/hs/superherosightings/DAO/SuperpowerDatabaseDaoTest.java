package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.REPOSITORY.*;
import com.hs.superherosightings.DTO.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SuperpowerDatabaseDaoTest {
    
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

        Address address1 = new Address();
        address1.setAddress("37 Victoria Street");
        address1.setCity("London");
        address1.setTown("Watford");
        address1.setPostcode("WD7 2CG");

        Organisation organisation1 = new Organisation();
        organisation1.setAddress(address1);
        organisation1.setAddress(address1);
        organisation1.setEmail("example@gmail.com");
        organisation1.setOrgDescription("Superheroes with Strength abilities");
        organisation1.setOrgName("Strong Squad");
        organisation1.setPhone("01234567890");
        organisation1.setSupertype(supertypeDao.getSupertypeById(1));
        organisation1.setMembers(new ArrayList<>());
        organisations = new ArrayList<>();
        organisations.add(organisation1);
        organisationDao.addOrganisation(organisation1);

    }

     // Test of addSuperpower and getSuperpowerById methods, of class SuperpowerDatabaseDao.
    @Test
    public void testAddAndGetSuperpowerById() {
        
        Superpower superpower1 = new Superpower();
        superpower1.setSuperpowerName("Speed");
        superpower1 = superpowerDao.addSuperpower(superpower1);
        
        Superpower superpowerFromDao = superpowerDao.getSuperpowerById(superpower1.getSuperpowerId());
        assertNotNull(superpowerFromDao);
        assertEquals(superpower1, superpowerFromDao);
        
    }


     // Test of getAllSuperpowers method, of class SuperpowerDatabaseDao.
    @Test
    public void testGetAllSuperpowers() {
        
        Superpower superpower1 = new Superpower();
        superpower1.setSuperpowerName("Speed");
        superpower1 = superpowerDao.addSuperpower(superpower1);
        
        Superpower superpower2 = new Superpower();
        superpower2.setSuperpowerName("Flying");
        superpower2 = superpowerDao.addSuperpower(superpower2);
        
        List<Superpower> superpowersFromDao = superpowerDao.getAllSuperpowers();
        assertEquals(2, superpowersFromDao.size());
        assertTrue(superpowersFromDao.contains(superpower1));
        assertTrue(superpowersFromDao.contains(superpower2));
        
    }


     // Test of updateSuperpower method, of class SuperpowerDatabaseDao.
    @Test
    public void testUpdateSuperpower() {
        
        Superpower superpower1 = new Superpower();
        superpower1.setSuperpowerName("Speed");
        superpower1 = superpowerDao.addSuperpower(superpower1);
        
        superpower1.setSuperpowerName("Flying");
        superpowerDao.updateSuperpower(superpower1);
        Superpower superpowerFromDao = superpowerDao.getSuperpowerById(superpower1.getSuperpowerId());
        assertEquals(superpower1, superpowerFromDao);
    }


     // Test of deleteSuperpowerById method, of class SuperpowerDatabaseDao.
    @Test
    public void testDeleteSuperpowerById() {
        
        Superpower superpower1 = new Superpower();
        superpower1.setSuperpowerName("Speed");
        superpower1 = superpowerDao.addSuperpower(superpower1);
        Superpower superpowerFromDao = superpowerDao.getSuperpowerById(superpower1.getSuperpowerId());
        assertNotNull(superpowerFromDao);
        
        superpowerDao.deleteSuperpowerById(superpower1.getSuperpowerId());
        superpowerFromDao = superpowerDao.getSuperpowerById(superpower1.getSuperpowerId());
        assertNull(superpowerFromDao);
        
        superpowerDao.addSuperpower(superpower1);
    }
    
}
