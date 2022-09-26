package com.hs.superherosightings.DAO;

import com.hs.superherosightings.DAO.REPOSITORY.SupertypeDao;
import com.hs.superherosightings.DTO.Supertype;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SupertypeDatabaseDaoTest {
    
    @Autowired
    SupertypeDao supertypeDao;

     // Test of getSupertypeById method, of class SupertypeDatabaseDao.
    @Test
    public void testGetSupertypeById() {
        Supertype supertype1 = new Supertype();
        supertype1.setSupertypeName("Superhero");
        supertype1.setSupertypeId(1);
        
        Supertype supertype2 = new Supertype();
        supertype2.setSupertypeName("Supervillain");
        supertype2.setSupertypeId(2);
        
        Supertype supertype1FromDao = supertypeDao.getSupertypeById(1);
        assertEquals(supertype1, supertype1FromDao);
        
        Supertype supertype2FromDao = supertypeDao.getSupertypeById(2);
        assertEquals(supertype2, supertype2FromDao);
    }


     // Test of getAllSupertypes method, of class SupertypeDatabaseDao.
    @Test
    public void testGetAllSupertypes() {
        Supertype supertype1 = new Supertype();
        supertype1.setSupertypeName("Superhero");
        supertype1.setSupertypeId(1);
        
        Supertype supertype2 = new Supertype();
        supertype2.setSupertypeName("Supervillain");
        supertype2.setSupertypeId(2);
        
        List<Supertype> supertypes = supertypeDao.getAllSupertypes();
        assertEquals(2, supertypes.size());
        assertTrue(supertypes.contains(supertype1));
        assertTrue(supertypes.contains(supertype2));
        
    }
    
}
