package com.hs.superherosightings.CONTROLLER;

import com.hs.superherosightings.DAO.REPOSITORY.*;
import com.hs.superherosightings.DTO.Error;
import com.hs.superherosightings.DTO.Superpower;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SuperpowerController {

    @Autowired
    LocationDao locationDao;
    
    @Autowired
    OrganisationDao organisationDao;
    
    @Autowired
    SightingDao sightingDao;
    
    @Autowired
    SuperpowerDao superpowerDao;
    
    @Autowired
    SuperheroDao superheroDao;
    
    Set<ConstraintViolation<Superpower>> violations = new HashSet<>();
    Set<Error> customErrors = new HashSet<>();
    
    @GetMapping("superpower")
    public String displaySuperpowers(Model model){
        
        List<Superpower> superpowers = superpowerDao.getAllSuperpowers();
        
        refreshCustomErrors();
        model.addAttribute("errors", violations);
        model.addAttribute("customErrors", customErrors);
        model.addAttribute("superpowers", superpowers);
        
        return "superpower";
    }
    
    private void refreshCustomErrors(){
        customErrors = customErrors.stream().filter(e -> !e.isOld()).collect(Collectors.toSet());
        customErrors.forEach(e -> e.setOld(true));
    }
    
    @PostMapping("addSuperpower")
    public String addSuperpower(String superpowerName) {
        
        Superpower superpower = new Superpower();
        superpower.setSuperpowerName(superpowerName);
        
        // Validate
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(superpower);
        if(violations.isEmpty()){
            superpowerDao.addSuperpower(superpower);
        }
        
        return "redirect:/superpower";
    }
    
    @PostMapping("editSuperpower")
    public String editSuperpower(@Valid Superpower superpower, BindingResult result){
        
        if (result.hasErrors()){
            return "editSuperpower";
        }
        
        superpowerDao.updateSuperpower(superpower);
        return "redirect:/superpowerDetail?id="+superpower.getSuperpowerId();
    }
    
    @GetMapping("editSuperpower")
    public String editSuperpower(int id, Model model){
        Superpower superpower = superpowerDao.getSuperpowerById(id);
        model.addAttribute("superpower", superpower);
        return "editSuperpower";
    }
    
    @GetMapping("confirmDeleteSuperpower")
    public String confirmDeleteSuperpower(int id, Model model){
        Superpower superpower = superpowerDao.getSuperpowerById(id);
        
        model.addAttribute("customErrors", customErrors);
        model.addAttribute("superpower", superpower);
        
        return "confirmDeleteSuperpower";
    }
    
    @GetMapping("deleteSuperpower")
    public String deleteSuperpower(int id){
        if (!superpowerDao.deleteSuperpowerById(id)){
            customErrors.add(new Error("Superpower in use. Must edit or delete superhero with superpower first."));
        } else{
            customErrors.clear();
        }
        return "redirect:/superpower";
    }
    
    @GetMapping("superpowerDetail")
    public String superpowerDetail(int id, Model model){
        Superpower superpower = superpowerDao.getSuperpowerById(id);
        model.addAttribute("superpower", superpower);
        return "superpowerDetail";
    }
}
