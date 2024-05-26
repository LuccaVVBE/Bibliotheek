package com.springBoot.bibliotheek;

import java.security.Principal;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import repository.BoekRepository;


@Controller
@RequestMapping("/bib")
public class BibController {
	
	@Autowired
	private BoekRepository boekRepository;
	
	@GetMapping
	public String showBibPage(@RequestParam(value = "added", required = false) String added,
            @RequestParam(value = "removed", required = false) String removed, Model model, Principal principal) {
		if(added != null) {
			model.addAttribute("added", "favoriet.added");
		}
		else {
			if(removed != null) {
				model.addAttribute("removed","favoriet.removed");
			}
		}
		model.addAttribute("boeken", boekRepository.findAll());
		if(!Objects.isNull(principal)) {
			model.addAttribute("userName", principal.getName());
		}
		return "bibpage";
	}
}
