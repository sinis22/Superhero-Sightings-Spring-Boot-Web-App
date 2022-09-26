package com.hs.superherosightings.CONTROLLER;

import com.hs.superherosightings.DAO.REPOSITORY.*;
import com.hs.superherosightings.DTO.Error;
import com.hs.superherosightings.DTO.Organisation;
import com.hs.superherosightings.DTO.Superhero;
import com.hs.superherosightings.DTO.Superpower;
import com.hs.superherosightings.DTO.Supertype;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SuperheroController {

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
    
    @Autowired
    SupertypeDao supertypeDao;
    
    Set<ConstraintViolation<Superhero>> violations = new HashSet<>();
    Set<com.hs.superherosightings.DTO.Error> customErrors = new HashSet<>();
    
    @GetMapping("superhero")
    public String displaySuperhero(Model model){
        
        List<Superpower> superpowers = superpowerDao.getAllSuperpowers();
        List<Organisation> organisations = organisationDao.getAllOrganisations();
        List<Supertype> supertypes = supertypeDao.getAllSupertypes();
        List<Superhero> superhero = superheroDao.getAllSuperhero();
        
        model.addAttribute("superpowers", superpowers);
        model.addAttribute("organisations", organisations);
        model.addAttribute("supertypes", supertypes);
        model.addAttribute("superhero", superhero);
        model.addAttribute("customErrors", customErrors);
        model.addAttribute("errors", violations);
        
        return "superhero";
    }
    
    @PostMapping("addSuperhero")
    public String addSuperhero(Superhero superhero, HttpServletRequest request){
        
        customErrors.clear();
        
        // Gather organisations
        String[] orgIds = request.getParameterValues("organisationsVal");
        List<Organisation> organisations = new ArrayList<>();
        if (orgIds == null || orgIds.length == 0){
            customErrors.add(new com.hs.superherosightings.DTO.Error("Superhero must belong to at least 1 organisation"));
        }
        else{
            for (String orgId : orgIds){
                organisations.add(organisationDao.getOrganisationById(Integer.parseInt(orgId)));
            }
        }
        superhero.setOrganisations(organisations);
        
        // Get Supertype
        int supertypeId = Integer.parseInt(request.getParameter("supertypeVal"));
        Supertype supertype = supertypeDao.getSupertypeById(supertypeId);
        superhero.setSupertype(supertype);
        
        // Get Superpower
        int superpowerId = Integer.parseInt(request.getParameter("superpowerVal"));
        Superpower superpower = superpowerDao.getSuperpowerById(superpowerId);
        superhero.setSuperpower(superpower);
        
        // Validate
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(superhero);
        if (violations.isEmpty() && customErrors.isEmpty()){
            superheroDao.addSuperhero(superhero);
        }
        
        return "redirect:/superhero";
        
    }
    
    @GetMapping("editSuperhero")
    public String editSuperhero(int id, Model model){
        
        List<Superpower> superpowers = superpowerDao.getAllSuperpowers();
        List<Organisation> organisations = organisationDao.getAllOrganisations();
        List<Supertype> supertypes = supertypeDao.getAllSupertypes();
        Superhero superhero = superheroDao.getSuperheroById(id);
        List<Organisation> superheroOrganisations = organisationDao.getOrganisationsForSuperhero(superhero);
        
        model.addAttribute("superpowers", superpowers);
        model.addAttribute("organisations", organisations);
        model.addAttribute("supertypes", supertypes);
        model.addAttribute("superhero", superhero);
        model.addAttribute("superheroOrganisations", superheroOrganisations);
        model.addAttribute("customErrors", customErrors);
        model.addAttribute("errors", violations);
        
        return "editSuperhero";
    }
    
    @PostMapping("editSuperhero")
    public String editSuperhero(Superhero superhero, HttpServletRequest request){
        
        customErrors.clear();
        
        // Gather organisations
        String[] orgIds = request.getParameterValues("organisationsVal");
        List<Organisation> organisations = new ArrayList<>();
        if (orgIds == null || orgIds.length == 0){
            customErrors.add(new Error("Superhero must belong to at least 1 organisation."));
        }
        else{
            for (String orgId : orgIds){
                organisations.add(organisationDao.getOrganisationById(Integer.parseInt(orgId)));
            }
        }
        superhero.setOrganisations(organisations);
        
        // Get Supertype
        int supertypeId = Integer.parseInt(request.getParameter("supertypeVal"));
        Supertype supertype = supertypeDao.getSupertypeById(supertypeId);
        superhero.setSupertype(supertype);
        
        // Get Superpower
        int superpowerId = Integer.parseInt(request.getParameter("superpowerVal"));
        Superpower superpower = superpowerDao.getSuperpowerById(superpowerId);
        superhero.setSuperpower(superpower);
        
        // Validate
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(superhero);
        if (violations.isEmpty() && customErrors.isEmpty()){
            superheroDao.updateSuperhero(superhero);
            return "redirect:/superheroDetail?id="+ superhero.getSuperheroId();
        }
        
        return "redirect:/editSuperhero?id="+ superhero.getSuperheroId();
    }
    
    @GetMapping("superheroDetail")
    public String superheroDetail(int id, Model model){
        
        Superhero superhero = superheroDao.getSuperheroById(id);
        List<Superpower> superpowers = superpowerDao.getAllSuperpowers();
        List<Organisation> superheroOrganisations = organisationDao.getOrganisationsForSuperhero(superhero);
        List<Supertype> supertypes = supertypeDao.getAllSupertypes();
        
        model.addAttribute("superhero", superhero);
        model.addAttribute("superpowers", superpowers);
        model.addAttribute("organisations", superheroOrganisations);
        model.addAttribute("supertypes", supertypes);
        
        return "superheroDetail";
    }
    
    @GetMapping("confirmDeleteSuperhero")
    public String confirmDeleteSuperhero(int id, Model model){
        
        Superhero superhero = superheroDao.getSuperheroById(id);
        List<Superpower> superpowers = superpowerDao.getAllSuperpowers();
        List<Organisation> superheroOrganisations = organisationDao.getOrganisationsForSuperhero(superhero);
        List<Supertype> supertypes = supertypeDao.getAllSupertypes();
        
        model.addAttribute("superhero", superhero);
        model.addAttribute("superpowers", superpowers);
        model.addAttribute("organisations", superheroOrganisations);
        model.addAttribute("supertypes", supertypes);
        
        return "confirmDeleteSuperhero";
    }
    
    @GetMapping("deleteSuperhero")
    public String deleteSuperhero(int id){
        superheroDao.deleteSuperheroById(id);
        return "redirect:/superhero";
    }
    
}
