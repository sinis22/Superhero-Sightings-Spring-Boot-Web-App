package com.hs.superherosightings.DAO.REPOSITORY;

import com.hs.superherosightings.DTO.Superpower;
import java.util.List;

public interface SuperpowerDao {

    Superpower getSuperpowerById(int id);
    List<Superpower> getAllSuperpowers();
    Superpower addSuperpower(Superpower superpower);
    void updateSuperpower(Superpower superpower);
    boolean deleteSuperpowerById(int id);
    
}
