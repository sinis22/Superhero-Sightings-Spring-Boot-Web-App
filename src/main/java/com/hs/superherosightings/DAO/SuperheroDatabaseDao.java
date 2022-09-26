package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.REPOSITORY.SuperheroDao;
import com.hs.superherosightings.DTO.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.hs.superherosightings.DTO.Superhero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SuperheroDatabaseDao implements SuperheroDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Superhero getSuperheroById(int id) {
        try{
            final String SELECT_SUPERHERO_BY_ID = "SELECT * FROM superhero WHERE superheroId = ?";
            Superhero superhero = jdbc.queryForObject(SELECT_SUPERHERO_BY_ID, new SuperheroMapper(), id);
            superhero.setOrganisations(getOrganisationsForSuperhero(superhero));
            superhero.setSuperpower(getSuperpowerForSuperhero(superhero));
            superhero.setSupertype(getSupertypeForSuperhero(superhero));
            return superhero;
        } catch (DataAccessException ex){
            return null;
        }
    }
    
    private List<Organisation> getOrganisationsForSuperhero(Superhero superhero){
        final String SELECT_ORGANISATIONS_FOR_SUPERHERO = "SELECT * FROM organisation o "
                + "INNER JOIN membership m ON o.orgId = m.orgId "
                + "WHERE m.superheroId = ?";
        return jdbc.query(SELECT_ORGANISATIONS_FOR_SUPERHERO, new OrganisationDatabaseDao.OrganisationMapper(), superhero.getSuperheroId());
    }
    
    private Superpower getSuperpowerForSuperhero(Superhero superhero){
        final String SELECT_SUPERPOWER_FOR_SUPERHERO = "SELECT * FROM superpower spo "
                + "INNER JOIN superhero spe ON spe.superpowerId = spo.superpowerId "
                + "WHERE spe.superheroId = ?";
        return jdbc.queryForObject(SELECT_SUPERPOWER_FOR_SUPERHERO, new SuperpowerDatabaseDao.SuperpowerMapper(), superhero.getSuperheroId());
    }

    private Supertype getSupertypeForSuperhero(Superhero superhero){
        final String SELECT_SUPERTYPE_FOR_SUPERHERO = "SELECT * FROM supertype st "
                + "INNER JOIN superhero sp ON sp.supertypeId = st.supertypeId "
                + "WHERE sp.superheroId = ?";
        return jdbc.queryForObject(SELECT_SUPERTYPE_FOR_SUPERHERO, new SupertypeDatabaseDao.SupertypeMapper(), superhero.getSuperheroId());
    }
    
    @Override
    public List<Superhero> getAllSuperhero() {
        final String SELECT_ALL_SUPERHERO = "SELECT * FROM superhero";
        List<Superhero> Superhero = jdbc.query(SELECT_ALL_SUPERHERO, new SuperheroMapper());
        for (Superhero superhero : Superhero){
            superhero.setOrganisations(getOrganisationsForSuperhero(superhero));
            superhero.setSuperpower(getSuperpowerForSuperhero(superhero));
            superhero.setSupertype(getSupertypeForSuperhero(superhero));
        }
        return Superhero;
    }

    @Override
    @Transactional
    public Superhero addSuperhero(Superhero superhero) {
        
        // Add superhero to superhero table
        final String INSERT_SUPERHERO = "INSERT INTO superhero(superheroName, superheroDescription, supertypeId, superpowerId) "
                + "VALUES(?, ?, ?, ?)";
        jdbc.update(INSERT_SUPERHERO,
                superhero.getSuperheroName(),
                superhero.getSuperheroDescription(),
                superhero.getSupertype().getSupertypeId(),
                superhero.getSuperpower().getSuperpowerId());
        int newSuperheroId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superhero.setSuperheroId(newSuperheroId);
        
        // Add superhero to membership table
        for (Organisation org : superhero.getOrganisations()){
            final String INSERT_MEMBER = "INSERT INTO membership (superheroId, orgId) "
                    + "VALUES(?, ?)";
            jdbc.update(INSERT_MEMBER, superhero.getSuperheroId(), org.getOrgId());
        }
        
        return superhero;
        
    }

    @Override
    public void updateSuperhero(Superhero superhero) {
        
        // Update superhero table
        final String UPDATE_SUPERHERO = "UPDATE superhero SET superheroName = ?, "
                + "superheroDescription = ?, supertypeId = ?, superpowerId = ? "
                + "WHERE superheroId = ?";
        jdbc.update(UPDATE_SUPERHERO,
                superhero.getSuperheroName(),
                superhero.getSuperheroDescription(),
                superhero.getSupertype().getSupertypeId(),
                superhero.getSuperpower().getSuperpowerId(),
                superhero.getSuperheroId());
        
        // Delete superhero from membership table
        final String DELETE_MEMBER = "DELETE FROM membership WHERE superheroId = ?";
        jdbc.update(DELETE_MEMBER, superhero.getSuperheroId());
        
        // Add superhero to membership table
        for (Organisation org : superhero.getOrganisations()){
            final String INSERT_MEMBER = "INSERT INTO membership (superheroId, orgId) "
                    + "VALUES(?, ?)";
            jdbc.update(INSERT_MEMBER, superhero.getSuperheroId(), org.getOrgId());
        }
        
    }

    @Override
    @Transactional
    public void deleteSuperheroById(int id) {
        final String DELETE_MEMBERSHIP = "DELETE FROM membership WHERE superheroId = ?";
        jdbc.update(DELETE_MEMBERSHIP, id);
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE superheroId = ?";
        jdbc.update(DELETE_SIGHTING, id);
        final String DELETE_SUPERhero = "DELETE FROM superhero WHERE superheroId = ?";
        jdbc.update(DELETE_SUPERhero, id);
    }

    @Override
    public List<Superhero> getSuperheroLocation(Location location) {
        final String SELECT_MEMBERS_FOR_ORGANISATION = "SELECT sp.* FROM superhero sp "
                + "INNER JOIN sighting si ON si.superheroId = sp.superheroId "
                + "WHERE si.locationId = ?";
        return jdbc.query(SELECT_MEMBERS_FOR_ORGANISATION, new SuperheroMapper(), location.getLocationId());
    }

    @Override
    public List<Superhero> getSuperheroOrganisation(Organisation organisation) {
        final String SELECT_MEMBERS_FOR_ORGANISATION = "SELECT s.* FROM superhero s "
                + "INNER JOIN membership m ON m.superheroId = s.superheroId "
                + "WHERE m.orgId = ?";
        List<Superhero> Superhero = jdbc.query(SELECT_MEMBERS_FOR_ORGANISATION, new SuperheroMapper(), organisation.getOrgId());
        for (Superhero superhero : Superhero){
            superhero.setOrganisations(getOrganisationsForSuperhero(superhero));
            superhero.setSuperpower(getSuperpowerForSuperhero(superhero));
            superhero.setSupertype(getSupertypeForSuperhero(superhero));
        }
        return Superhero;
    }
    
    public static final class SuperheroMapper implements RowMapper<Superhero> {

        @Override
        public Superhero mapRow(ResultSet rs, int index) throws SQLException {
            Superhero superhero = new Superhero();
            superhero.setSuperheroId(rs.getInt("superheroId"));
            superhero.setSuperheroName(rs.getString("superheroName"));
            superhero.setSuperheroDescription(rs.getString("superheroDescription"));
            return superhero;
        }
    }

}
