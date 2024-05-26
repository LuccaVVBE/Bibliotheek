package com.springBoot.bibliotheek;

import java.security.Principal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Boek;
import repository.BoekRepository;

@Controller
@RequestMapping("/boek")

public class BoekController {
	@Autowired
	private BoekRepository boekRepository;

	@GetMapping(value="/{ISBNnr}")
	public String showBoekDetails(@PathVariable String ISBNnr, Model model, Principal principal) {
		Boek boek = boekRepository.findByISBNNr(ISBNnr);
		model.addAttribute("boek", boek);
		if(!Objects.isNull(principal)) {
			model.addAttribute("userName", principal.getName());
			if(boek.getFavorieten().contains(principal.getName())) {
				model.addAttribute("isFavoriet", true);
			} else {
				model.addAttribute("isFavoriet",false);
			}
		}
		
		return "boekDetails";
	}
	
	public static String formatCurrency(final Number target, Locale locale) { 
		  
	     if(locale==null) {
	    	 locale = Locale.GERMANY;
	     }
	  
	     if (target == null) { 
	         return null; 
	     } 
	  
	     NumberFormat format = NumberFormat.getCurrencyInstance(locale); 
	  
	     return format.format(target); 
	 } 
	
	@PostMapping(value="/{ISBNnr}")
	public String AddOrRemoveFavoriet(@PathVariable String ISBNnr, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		Boek boek = boekRepository.findByISBNNr(ISBNnr);
		String addOrRemove = boek.editFavoriet(principal.getName());
		boekRepository.save(boek);
		redirectAttributes.addFlashAttribute("titel", boek.getTitel());
		return "redirect:/bib?" + addOrRemove;
	}
	
}
