package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.REPOSITORY.LocationDao;
import com.hs.superherosightings.DAO.REPOSITORY.SightingDao;
import com.hs.superherosightings.DAO.REPOSITORY.SuperheroDao;
import com.hs.superherosightings.DTO.Location;
import com.hs.superherosightings.DTO.Sighting;
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
public class SightingDatabaseDao implements SightingDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    SuperheroDao superheroDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Override
    public Sighting getSightingById(int id) {
        try{
            final String SELECT_SIGHTING_BY_ID = "SELECT * FROM sighting "
                    + "WHERE sightingId = ?";
            Sighting sighting = jdbc.queryForObject(SELECT_SIGHTING_BY_ID, new SightingMapper(), id);
            sighting.setLocation(getLocationForSighting(id));
            sighting.setSuperhero(getSuperheroForSighting(id));
            return sighting;
        } catch (DataAccessException ex){
            return null;
        }
    }

    @Override
    public List<Sighting> getAllSightings() {
        final String SELECT_ALL_SIGHTINGS = "SELECT * FROM sighting";
        List<Sighting> sightings = jdbc.query(SELECT_ALL_SIGHTINGS, new SightingMapper());
        for (Sighting sighting : sightings){
            sighting.setLocation(getLocationForSighting(sighting.getSightingId()));
            sighting.setSuperhero(getSuperheroForSighting(sighting.getSightingId()));
        }
        return sightings;
    }
    
    private Location getLocationForSighting(int id){
        final String SELECT_LOCATION_ID_FOR_SIGHTING = "SELECT locationId FROM sighting WHERE sightingId = ?";
        int locationId = jdbc.queryForObject(SELECT_LOCATION_ID_FOR_SIGHTING, Integer.class, id);
        return locationDao.getLocationById(locationId);
    }
    
    private Superhero getSuperheroForSighting(int id){
        final String SELECT_SUPERHERO_ID_FOR_SIGHTING = "SELECT superheroId FROM sighting WHERE sightingId = ?";
        int superheroId = jdbc.queryForObject(SELECT_SUPERHERO_ID_FOR_SIGHTING, Integer.class, id);
        return superheroDao.getSuperheroById(superheroId);
    }

    @Override
    @Transactional
    public Sighting addSighting(Sighting sighting) {
        final String INSERT_SIGHTING = "INSERT INTO sighting (superheroId, locationId, sightingDatetime) "
                + "VALUES(?,?,?)";
        jdbc.update(INSERT_SIGHTING,
                sighting.getSuperhero().getSuperheroId(),
                sighting.getLocation().getLocationId(),
                sighting.getSightingDatetime());
        int newSightingId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setSightingId(newSightingId);
        return sighting;        
    }

    @Override
    public void updateSighting(Sighting sighting) {
        final String UPDATE_SIGHTING = "UPDATE sighting SET superheroId = ?, "
                + "locationId = ?, sightingDatetime = ? "
                + "WHERE sightingId = ?";
        jdbc.update(UPDATE_SIGHTING,
                sighting.getSuperhero().getSuperheroId(),
                sighting.getLocation().getLocationId(),
                sighting.getSightingDatetime(),
                sighting.getSightingId());
    }

    @Override
    public void deleteSightingById(int id) {
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE sightingId = ?";
        jdbc.update(DELETE_SIGHTING, id);
    }

    @Override
    public List<Sighting> getMostRecentSightings(int numSightings) {
        final String SELECT_RECENT_SIGHTINGS = "SELECT * FROM sighting "
                + "ORDER BY sightingDatetime DESC LIMIT ?";
        List<Sighting> sightings = jdbc.query(SELECT_RECENT_SIGHTINGS, new SightingMapper(), numSightings);
        for (Sighting sighting : sightings){
            sighting.setLocation(getLocationForSighting(sighting.getSightingId()));
            sighting.setSuperhero(getSuperheroForSighting(sighting.getSightingId()));
        }
        return sightings;
    }

    public static final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet rs, int index) throws SQLException {
            Sighting sighting = new Sighting();
            sighting.setSightingId(rs.getInt("sightingId"));
            sighting.setSightingDatetime(rs.getTimestamp("sightingDatetime").toLocalDateTime());
            return sighting;
        }
    }
    
}
