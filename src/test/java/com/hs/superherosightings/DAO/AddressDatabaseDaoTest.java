package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.REPOSITORY.*;
import com.hs.superherosightings.DTO.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AddressDatabaseDaoTest {
    
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
    }

    //Test of addAddress and getAddressById method, of class AddressDatabaseDao.
    @Test
    public void testAddAndGetAddress() {
        Address address1 = new Address();
        address1.setAddress("37 Victoria Street");
        address1.setCity("London");
        address1.setTown("Watford");
        address1.setPostcode("WD7 2CG");
        
        Address fromDao = addressDao.addAddress(address1);

        address1.setAddressId(fromDao.getAddressId());
        assertEquals(address1, fromDao);
        
        fromDao = addressDao.getAddressById(address1.getAddressId());
        assertEquals(address1, fromDao);
    }
    
    //Test of getAllAddresses method, of class AddressDatabaseDao.
    @Test
    public void testGetAllAddresses(){
        Address address1 = new Address();
        address1.setAddress("37 Victoria Street");
        address1.setCity("London");
        address1.setTown("Watford");
        address1.setPostcode("WD7 2CG");
        address1 = addressDao.addAddress(address1);
        
        Address address2 = new Address();
        address2.setAddress("37 Victoria Street");
        address2.setCity("London");
        address2.setTown("Watford");
        address2.setPostcode("WD7 2CG");
        address2 = addressDao.addAddress(address2);
        
        List<Address> addresses = addressDao.getAllAddresses();
        assertEquals(2, addresses.size());
        
        assertTrue(addresses.contains(address1));
        assertTrue(addresses.contains(address2));
    }


     //Test of updateAddress method, of class AddressDatabaseDao.
    @Test
    public void testUpdateAddress() {
        Address address1 = new Address();
        address1.setAddress("37 Victoria Street");
        address1.setCity("London");
        address1.setTown("Watford");
        address1.setPostcode("WD7 2CG");
        
        address1 = addressDao.addAddress(address1);
        address1.setAddress("6 George Street");
        address1.setPostcode("BA7 3NV");
        
        addressDao.updateAddress(address1);
        
        Address fromDao = addressDao.getAddressById(address1.getAddressId());
        assertEquals(address1, fromDao);
    }


     //Test of deleteAddressById method, of class AddressDatabaseDao.
    @Test
    public void testDeleteAddressById() {
        Address address1 = new Address();
        address1.setAddress("37 Victoria Street");
        address1.setCity("London");
        address1.setTown("Watford");
        address1.setPostcode("WD7 2CG");
        
        address1 = addressDao.addAddress(address1);
        Address fromDao = addressDao.getAddressById(address1.getAddressId());
        assertNotNull(fromDao);
        
        addressDao.deleteAddressById(fromDao.getAddressId());
        fromDao = addressDao.getAddressById(address1.getAddressId());
        assertNull(fromDao);
    }
    
}
