package com.springBoot.bibliotheek;


import java.security.Principal;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domain.Boek;
import domain.Locatie;
import jakarta.validation.Valid;
import repository.AuteurRepository;
import repository.BoekRepository;
import repository.LocatieRepository;
import validators.BoekValidator;

@Controller
@RequestMapping("/addBoek")
public class AddBoekController {

	@Autowired
	private AuteurRepository auteurRepository;
	
	@Autowired
	private BoekRepository boekRepository;
	
	@Autowired
	private LocatieRepository locatieRepository;
	
	@Autowired
	private BoekValidator boekValidator;
	
	@GetMapping
	public String showAddBoekForm(Model model, Principal principal) {
		Boek boek = new Boek();
		boek.addLocatie(new Locatie());
		boek.addLocatie(new Locatie());
		boek.addLocatie(new Locatie());
	    model.addAttribute("nieuwBoek", boek);
	    model.addAttribute("alleAuteurs", auteurRepository.findAll());
	    if(!Objects.isNull(principal)) {
			model.addAttribute("userName", principal.getName());
		}
	    return "voegBoekToe";
	}
	
	@PostMapping
	public String maakNieuwBoek(
	        @Valid @ModelAttribute("nieuwBoek") Boek nieuwBoek,
	        BindingResult result,
	        Model model) {	
		boekValidator.validate(nieuwBoek, result);
		
		if (result.hasErrors()) {
			model.addAttribute("alleAuteurs", auteurRepository.findAll());
			return "voegBoekToe" ;  
		}
		
		nieuwBoek.saveLocaties(locatieRepository);
		boekRepository.save(nieuwBoek);
			
		return "redirect:/bib";
	}  

}
