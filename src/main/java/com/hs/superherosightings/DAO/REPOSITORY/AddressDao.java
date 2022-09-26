package com.hs.superherosightings.DAO.REPOSITORY;

import com.hs.superherosightings.DTO.Address;
import java.util.List;


public interface AddressDao {
   
    Address getAddressById(int id);
    List<Address> getAllAddresses();
    Address addAddress(Address address);
    void updateAddress(Address address);
    void deleteAddressById(int id);
    
}
