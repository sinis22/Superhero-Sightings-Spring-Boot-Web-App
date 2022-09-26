package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.REPOSITORY.AddressDao;
import com.hs.superherosightings.DTO.Address;
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
public class AddressDatabaseDao implements AddressDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Address getAddressById(int id) {
        try {
            final String SELECT_ADDRESS_BY_ID = "SELECT * FROM address WHERE addressId = ?";
            return jdbc.queryForObject(SELECT_ADDRESS_BY_ID, new AddressMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public Address addAddress(Address address) {
        final String INSERT_ADDRESS = "INSERT INTO address(address, city, town, postcode) "
                + "VALUES(?,?,?,?)";
        jdbc.update(INSERT_ADDRESS,
                address.getAddress(),
                address.getCity(),
                address.getTown(),
                address.getPostcode());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        address.setAddressId(newId);
        return address;
    }

    @Override
    public void updateAddress(Address address) {
        final String UPDATE_ADDRESS = "UPDATE address SET address = ?, city = ?, "
                + "town = ?, postcode = ? WHERE addressId = ?";
        jdbc.update(UPDATE_ADDRESS, 
                address.getAddress(),
                address.getCity(),
                address.getTown(),
                address.getPostcode(),
                address.getAddressId());
    }

    @Override
    public void deleteAddressById(int id) {
        final String DELETE_ADDRESS = "DELETE FROM address WHERE addressId = ?";
        jdbc.update(DELETE_ADDRESS, id);
    }

    @Override
    public List<Address> getAllAddresses() {
        final String SELECT_ALL_ADDRESSES = "SELECT * FROM address";
        return jdbc.query(SELECT_ALL_ADDRESSES, new AddressMapper());
    }
    
    public static final class AddressMapper implements RowMapper<Address> {

        @Override
        public Address mapRow(ResultSet rs, int index) throws SQLException {
            Address address = new Address();
            address.setAddress(rs.getString("address"));
            address.setCity(rs.getString("city"));
            address.setTown(rs.getString("town"));
            address.setPostcode(rs.getString("postcode"));
            address.setAddressId(rs.getInt("addressId"));

            return address;
        }
    }

}
