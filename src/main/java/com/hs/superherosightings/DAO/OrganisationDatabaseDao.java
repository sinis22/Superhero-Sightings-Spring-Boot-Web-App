package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.REPOSITORY.AddressDao;
import com.hs.superherosightings.DAO.REPOSITORY.OrganisationDao;
import com.hs.superherosightings.DTO.Address;
import com.hs.superherosightings.DTO.Organisation;
import com.hs.superherosightings.DTO.Superhero;
import com.hs.superherosightings.DTO.Supertype;
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
public class OrganisationDatabaseDao implements OrganisationDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    AddressDao addressDao;
    
    @Override
    public Organisation getOrganisationById(int id) {
        try{
            final String SELECT_ORGANISATION_BY_ID = "SELECT * FROM organisation "
                    + "WHERE orgId = ?";
            Organisation organisation = jdbc.queryForObject(SELECT_ORGANISATION_BY_ID, new OrganisationMapper(), id);
            organisation.setAddress(getAddressForOrganisation(id));
            organisation.setSupertype(getSupertypeForOrganisation(id));
            organisation.setMembers(getMembersForOrganisation(id));
            return organisation;
        } catch (DataAccessException ex){
            return null;
        }
    }
    
    private Address getAddressForOrganisation(int id){
        final String SELECT_ADDRESS_FOR_ORGANISATION = "SELECT a.* FROM address a "
                + "INNER JOIN organisation o ON o.addressId = a.addressId WHERE o.orgId = ?";
        return jdbc.queryForObject(SELECT_ADDRESS_FOR_ORGANISATION, new AddressDatabaseDao.AddressMapper(), id);
    }
    
    private Supertype getSupertypeForOrganisation(int id){
        final String SELECT_SUPERTYPE_FOR_ORGANISATION = "SELECT s.* FROM supertype s "
                + "INNER JOIN organisation o ON s.supertypeId= o.supertypeId WHERE o.orgId = ?";
        return jdbc.queryForObject(SELECT_SUPERTYPE_FOR_ORGANISATION, new SupertypeDatabaseDao.SupertypeMapper(), id);
    }
    
    private List<Superhero> getMembersForOrganisation(int id){
        final String SELECT_MEMBERS_FOR_ORGANISATION = "SELECT s.* FROM superhero s "
                + "INNER JOIN membership m ON m.superheroId = s.superheroId "
                + "WHERE m.orgId = ?";
        return jdbc.query(SELECT_MEMBERS_FOR_ORGANISATION, new SuperheroDatabaseDao.SuperheroMapper(), id);
    }
    
    @Override
    public List<Organisation> getOrganisationsForSuperhero(Superhero superhero){
        final String SELECT_ORGANISATION_FOR_SUPERHERO = "SELECT * FROM organisation o "
                + "INNER JOIN membership m ON o.orgId = m.orgId "
                + "WHERE m.superheroId = ?";
        List<Organisation> organisations = jdbc.query(SELECT_ORGANISATION_FOR_SUPERHERO, new OrganisationMapper(), superhero.getSuperheroId());
        for (Organisation org : organisations){
            org.setSupertype(getSupertypeForOrganisation(org.getOrgId()));
            org.setAddress(getAddressForOrganisation(org.getOrgId()));
            org.setMembers(getMembersForOrganisation(org.getOrgId()));
        }
        return organisations;
    }

    @Override
    public List<Organisation> getAllOrganisations() {
        final String SELECT_ALL_ORGANISATION = "SELECT * FROM organisation";
        List<Organisation> organisations = jdbc.query(SELECT_ALL_ORGANISATION, new OrganisationMapper());
        for (Organisation organisation : organisations){
            int id = organisation.getOrgId();
            organisation.setAddress(getAddressForOrganisation(id));
            organisation.setSupertype(getSupertypeForOrganisation(id));
            organisation.setMembers(getMembersForOrganisation(id));
        }
        return organisations;
    }

    @Override
    @Transactional
    public Organisation addOrganisation(Organisation organisation) {
        
        // Update Address Table
        Address address = organisation.getAddress();
        organisation.setAddress(addressDao.addAddress(address));
        
        // Update Organisation Table
        final String INSERT_ORGANISATION = "INSERT INTO organisation(orgName, orgDescription, email, phone, addressId, supertypeId) "
                + "VALUES(?,?,?,?,?,?)";
        jdbc.update(INSERT_ORGANISATION,
                organisation.getOrgName(),
                organisation.getOrgDescription(),
                organisation.getEmail(),
                organisation.getPhone(),
                organisation.getAddress().getAddressId(),
                organisation.getSupertype().getSupertypeId());
        int newOrgId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organisation.setOrgId(newOrgId);
        
        // Update Membership Table
        for (Superhero member : organisation.getMembers()){
            final String INSERT_MEMBER = "INSERT INTO membership (superheroId, orgId) "
                    + "VALUES(?,?)";
            jdbc.update(INSERT_MEMBER,
                    member.getSuperheroId(),
                    organisation.getOrgId());
        }
        
        return organisation;
    }

    @Override
    @Transactional
    public void updateOrganisation(Organisation organisation) {
        
        // Update address table
        Address address = organisation.getAddress();
        final String SELECT_ADDRESS_ID_FOR_ORGANISATION = "SELECT addressId FROM organisation WHERE orgId = ?";
        int addressId = jdbc.queryForObject(SELECT_ADDRESS_ID_FOR_ORGANISATION, Integer.class, organisation.getOrgId());
        address.setAddressId(addressId);
        addressDao.updateAddress(address);
        
        // Update organisation table
        final String UPDATE_ORGANISATION= "UPDATE organisation "
                + "SET orgName = ?, orgDescription = ?, email = ?, phone = ?, "
                + "supertypeId = ? "
                + "WHERE orgId = ?";
        jdbc.update(UPDATE_ORGANISATION,
                organisation.getOrgName(),
                organisation.getOrgDescription(),
                organisation.getEmail(),
                organisation.getPhone(),
                organisation.getSupertype().getSupertypeId(),
                organisation.getOrgId());
        
        // Delete members from membership table
        final String DELETE_MEMBERSHIP = "DELETE FROM membership WHERE orgId = ?";
        jdbc.update(DELETE_MEMBERSHIP, organisation.getOrgId());
        
        // Add members to membership table
        for (Superhero member : organisation.getMembers()){
            final String INSERT_MEMBER = "INSERT INTO membership (superheroId, orgId) "
                    + "VALUES(?,?)";
            jdbc.update(INSERT_MEMBER,
                    member.getSuperheroId(),
                    organisation.getOrgId());
        }
    }

    @Override
    @Transactional
    public void deleteOrganisationById(int id) {
        
        final String DELETE_MEMBERSHIP = "DELETE FROM membership WHERE orgId = ?";
        jdbc.update(DELETE_MEMBERSHIP, id);
        
        final String DELETE_ADDRESS_AND_ORGANISATION = "DELETE organisation, address FROM address INNER JOIN organisation ON organisation.addressId = address.addressId WHERE orgId = ?";
        jdbc.update(DELETE_ADDRESS_AND_ORGANISATION, id);
    }

    public static final class OrganisationMapper implements RowMapper<Organisation> {

        @Override
        public Organisation mapRow(ResultSet rs, int index) throws SQLException {
            Organisation organisation = new Organisation();
            organisation.setOrgId(rs.getInt("orgId"));
            organisation.setEmail(rs.getString("email"));
            organisation.setOrgDescription(rs.getString("orgDescription"));
            organisation.setOrgName(rs.getString("orgName"));
            organisation.setPhone(rs.getString("phone"));
            
            return organisation;
        }
    }
    
}
