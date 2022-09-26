package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.AddressDatabaseDao.AddressMapper;
import com.hs.superherosightings.DAO.REPOSITORY.AddressDao;
import com.hs.superherosightings.DAO.REPOSITORY.LocationDao;
import com.hs.superherosightings.DTO.Address;
import com.hs.superherosightings.DTO.Location;
import com.hs.superherosightings.DTO.Superhero;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class LocationDatabaseDao implements LocationDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    AddressDao addressDao;
        
    @Override
    public Location getLocationById(int id) {
        try{
            final String SELECT_LOCATION_BY_ID = "SELECT * FROM location WHERE locationId = ?";
            Location location = jdbc.queryForObject(SELECT_LOCATION_BY_ID, new LocationMapper(), id);
            location.setAddress(getAddressForLocation(id));
            return location;
        } catch (DataAccessException ex){
            return null;
        }
    }
    
    private Address getAddressForLocation(int id){
        final String SELECT_ADDRESS_FOR_LOCATION = "SELECT a.* FROM address a "
                + "INNER JOIN location l ON l.addressId = a.addressId WHERE l.locationId = ?";
        return jdbc.queryForObject(SELECT_ADDRESS_FOR_LOCATION, new AddressMapper(), id);
    }

    @Override
    public List<Location> getAllLocations() {
        final String SELECT_ALL_LOCATIONS = "SELECT * FROM location";
        List<Location> locations =  jdbc.query(SELECT_ALL_LOCATIONS, new LocationMapper());
        for (Location location : locations){
            location.setAddress(getAddressForLocation(location.getLocationId()));
        }
        return locations;
    }

    @Override
    @Transactional
    public Location addLocation(Location location) {
        
        Address address = location.getAddress();
        location.setAddress(addressDao.addAddress(address));
        
        final String INSERT_LOCATION = "INSERT INTO location(locationName, locationDescription, latitude, longitude, addressId) "
                + "VALUES(?,?,?,?,?)";
        jdbc.update(INSERT_LOCATION,
                location.getLocationName(),
                location.getLocationDescription(),
                location.getLatitude(),
                location.getLongitude(),
                location.getAddress().getAddressId());
        int newLocationId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setLocationId(newLocationId);
        return location;
    }

    @Override
    @Transactional
    public void updateLocation(Location location) {
        Address address = location.getAddress();
        
        final String SELECT_ADDRESS_ID_FROM_LOCATION = "SELECT addressId FROM location WHERE locationId = ?";
        address.setAddressId(jdbc.queryForObject(SELECT_ADDRESS_ID_FROM_LOCATION, Integer.class, location.getLocationId()));
        
        addressDao.updateAddress(address);
        
        final String UPDATE_LOCATION = "UPDATE location SET locationName = ?, locationDescription = ?, "
                + "latitude = ?, longitude = ? WHERE locationId = ?";
        jdbc.update(UPDATE_LOCATION,
                location.getLocationName(),
                location.getLocationDescription(),
                location.getLatitude(),
                location.getLongitude(),
                location.getLocationId());
    }

    @Override
    @Transactional
    public void deleteLocationById(int id) {
        final String DELETE_SIGHTINGS = "DELETE FROM sighting WHERE locationId = ?";
        jdbc.update(DELETE_SIGHTINGS, id);
        
        final String DELETE_LOCATION_AND_ADDRESS = "DELETE location, address FROM location INNER JOIN address ON address.addressId = location.addressId WHERE locationId = ?";
        jdbc.update(DELETE_LOCATION_AND_ADDRESS, id);
    }

    @Override
    public List<Location> getLocationsForSuperhero(Superhero superhero) {
        final String SELECT_LOCATIONS_FOR_SUPERhero = "SELECT l.* FROM location l "
                + "INNER JOIN sighting s ON s.locationId = l.locationId WHERE "
                + "s.superheroId = ?";
        List<Location> locations = jdbc.query(SELECT_LOCATIONS_FOR_SUPERhero, new LocationMapper(), superhero.getSuperheroId());
        for (Location location : locations){
            location.setAddress(getAddressForLocation(location.getLocationId()));
        }
        return locations;
    }

    public static final class LocationMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet rs, int index) throws SQLException {
            Location location = new Location();
            location.setLocationName(rs.getString("locationName"));
            location.setLocationDescription(rs.getString("locationDescription"));
            location.setLatitude(rs.getDouble("latitude"));
            location.setLongitude(rs.getDouble("longitude"));
            location.setLocationId(rs.getInt("locationId"));

            return location;
        }
    }
    
}
