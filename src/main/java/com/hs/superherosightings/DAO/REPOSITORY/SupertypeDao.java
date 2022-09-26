package com.hs.superherosightings.DAO.REPOSITORY;

import com.hs.superherosightings.DTO.Supertype;
import java.util.List;

public interface SupertypeDao {
    
    Supertype getSupertypeById(int id);
    List<Supertype> getAllSupertypes();
    
}
