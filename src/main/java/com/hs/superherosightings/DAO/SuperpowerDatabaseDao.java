package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.REPOSITORY.SuperpowerDao;
import com.hs.superherosightings.DAO.SuperheroDatabaseDao.SuperheroMapper;
import com.hs.superherosightings.DTO.Superhero;
import com.hs.superherosightings.DTO.Superpower;
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
public class SuperpowerDatabaseDao implements SuperpowerDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Superpower getSuperpowerById(int id) {
        try{
            final String SELECT_SUPERPOWER_BY_ID = "SELECT * FROM superpower WHERE superpowerId = ?";
            return jdbc.queryForObject(SELECT_SUPERPOWER_BY_ID, new SuperpowerMapper(), id);
        } catch (DataAccessException ex){
            return null;
        }
    }

    @Override
    public List<Superpower> getAllSuperpowers() {
        final String SELECT_ALL_SUPERPOWERS = "SELECT * FROM superpower";
        return jdbc.query(SELECT_ALL_SUPERPOWERS, new SuperpowerMapper());
    }

    @Override
    @Transactional
    public Superpower addSuperpower(Superpower superpower) {
        final String INSERT_SUPERPOWER = "INSERT INTO superpower (superpowerName) "
                + "VALUES (?)";
        jdbc.update(INSERT_SUPERPOWER,
                superpower.getSuperpowerName());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superpower.setSuperpowerId(newId);
        return superpower;
    }

    @Override
    public void updateSuperpower(Superpower superpower) {
        final String UPDATE_SUPERPOWER = "UPDATE superpower "
                + "SET superpowerName = ? "
                + "WHERE superpowerId = ?";
        jdbc.update(UPDATE_SUPERPOWER,
                superpower.getSuperpowerName(),
                superpower.getSuperpowerId());
    }

    @Override
    public boolean deleteSuperpowerById(int id) {
        final String SELECT_SUPERHERO_WITH_SUPERPOWER = "SELECT * FROM superhero WHERE superpowerId = ?";
        List<Superhero> superheroWithSuperpower = jdbc.query(SELECT_SUPERHERO_WITH_SUPERPOWER, new SuperheroMapper(), id);
        if (superheroWithSuperpower == null || superheroWithSuperpower.isEmpty()){
            final String DELETE_SUPERPOWER = "DELETE FROM superpower WHERE superpowerId = ?";
            jdbc.update(DELETE_SUPERPOWER, id);
            return true;
        }
        return false;
    }

    public static final class SuperpowerMapper implements RowMapper<Superpower> {

        @Override
        public Superpower mapRow(ResultSet rs, int index) throws SQLException {
            Superpower superpower = new Superpower();
            superpower.setSuperpowerId(rs.getInt("superpowerId"));
            superpower.setSuperpowerName(rs.getString("superpowerName"));
            return superpower;
        }
    }
    
}
