package com.hs.superherosightings.CONTROLLER;

import com.hs.superherosightings.DAO.REPOSITORY.*;
import com.hs.superherosightings.DTO.Address;
import com.hs.superherosightings.DTO.Error;
import com.hs.superherosightings.DTO.Organisation;
import com.hs.superherosightings.DTO.Superhero;
import com.hs.superherosightings.DTO.Supertype;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class OrganisationController {
    
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
    
    Set<com.hs.superherosightings.DTO.Error> customErrors = new HashSet<>();
    
    @GetMapping("organisation")
    public String displayOrganisation(Model model){
        
        List<Organisation> organisations = organisationDao.getAllOrganisations();
        List<Supertype> supertypes = supertypeDao.getAllSupertypes();
        List<Superhero> members = superheroDao.getAllSuperhero();
        
        refreshCustomErrors();
        model.addAttribute("customErrors", customErrors);
        model.addAttribute("supertypes", supertypes);
        model.addAttribute("organisations", organisations);
        model.addAttribute("members", members);
        
        return "organisation";
    }
    
    private void refreshCustomErrors(){
        customErrors = customErrors.stream().filter(e -> !e.isOld()).collect(Collectors.toSet());
        customErrors.forEach(e -> e.setOld(true));
    }
    
    @PostMapping("addOrganisation")
    public String addOrganisation(Organisation organisation, HttpServletRequest request){
        
        Address address = new Address();
        address.setAddress(request.getParameter("addressLine"));
        address.setCity(request.getParameter("city"));
        address.setTown(request.getParameter("town"));
        address.setPostcode(request.getParameter("postcode"));
        organisation.setAddress(address);
        
        int supertypeId = Integer.parseInt(request.getParameter("supertypeVal"));
        Supertype supertype = supertypeDao.getSupertypeById(supertypeId);
        organisation.setSupertype(supertype);
        
        String[] memberIds = request.getParameterValues("memberIds");
        List<Superhero> members = new ArrayList<>();
        if (memberIds != null && memberIds.length > 0){
            for (String superheroId : memberIds){
                members.add(superheroDao.getSuperheroById(Integer.parseInt(superheroId)));
            }
        }
        organisation.setMembers(members);
        
        organisationDao.addOrganisation(organisation);
        
        return "redirect:/organisation";
    }
    
    @GetMapping("organisationDetail")
    public String organisationDetail(int id, Model model){

        Organisation organisation = organisationDao.getOrganisationById(id);
        List<Supertype> supertypes = supertypeDao.getAllSupertypes();
        List<Superhero> members = superheroDao.getSuperheroOrganisation(organisation);
        
        model.addAttribute("organisation", organisation);
        model.addAttribute("supertypes", supertypes);
        model.addAttribute("members", members);
        
        return "organisationDetail";
    }
    
    @GetMapping("editOrganisation")
    public String editOrganisation(int id, Model model){
        
        Organisation organisation = organisationDao.getOrganisationById(id);
        List<Supertype> supertypes = supertypeDao.getAllSupertypes();
        List<Superhero> superhero = superheroDao.getAllSuperhero();
        List<Superhero> organisationMembers = superheroDao.getSuperheroOrganisation(organisation);
        
        model.addAttribute("organisation", organisation);
        model.addAttribute("supertypes", supertypes);
        model.addAttribute("superhero", superhero);
        model.addAttribute("organisationMembers", organisationMembers);
        
        return "editOrganisation";
    }
    
    @PostMapping("editOrganisation")
    public String editOrganisation(@Valid Organisation organisation, BindingResult result, HttpServletRequest request){
        
        if (result.hasErrors()){
            return "redirect:/editOrganisation?id="+ organisation.getOrgId();
        }
        
        Address address = new Address();
        address.setAddress(request.getParameter("addressLine"));
        address.setCity(request.getParameter("city"));
        address.setTown(request.getParameter("town"));
        address.setPostcode(request.getParameter("postcode"));
        organisation.setAddress(address);
        
        int supertypeId = Integer.parseInt(request.getParameter("supertypeVal"));
        Supertype supertype = supertypeDao.getSupertypeById(supertypeId);
        organisation.setSupertype(supertype);
        
        String[] memberIds = request.getParameterValues("memberIds");
        List<Superhero> members = new ArrayList<>();
        if (memberIds != null && memberIds.length > 0){
            for (String superheroId : memberIds){
                members.add(superheroDao.getSuperheroById(Integer.parseInt(superheroId)));
            }
        }
        organisation.setMembers(members);
        
        organisationDao.updateOrganisation(organisation);
        
        return "redirect:/organisationDetail?id="+ organisation.getOrgId();
    }
    
    @GetMapping("deleteOrganisation")
    public String deleteOrganisation(int id){
        
        List<Superhero> superheroInOrganisation = superheroDao.getSuperheroOrganisation(organisationDao.getOrganisationById(id));
        if (superheroInOrganisation != null && !superheroInOrganisation.isEmpty()){
            if (superheroInOrganisation.stream().anyMatch(s -> s.getOrganisations().size() <= 1)){
                customErrors.add(new Error("Some superhero are only members of this organisation. Must edit/delete those superhero first."));
                return "redirect:/organisation";
            }
        }
        
        organisationDao.deleteOrganisationById(id);
        return "redirect:/organisation";
    }
    
    @GetMapping("confirmDeleteOrganisation")
    public String confirmDeleteOrganisation(int id, Model model){
        
        Organisation organisation = organisationDao.getOrganisationById(id);
        List<Supertype> supertypes = supertypeDao.getAllSupertypes();
        List<Superhero> members = superheroDao.getSuperheroOrganisation(organisation);
        
        model.addAttribute("organisation", organisation);
        model.addAttribute("supertypes", supertypes);
        model.addAttribute("members", members);
        
        return "confirmDeleteOrganisation";
    }
}
