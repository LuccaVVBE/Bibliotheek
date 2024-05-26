package com.springBoot.bibliotheek;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domain.Boek;
import repository.BoekRepository;

@Controller
@RequestMapping("/populair")

public class PopulairController {
	
	@Autowired
	private BoekRepository boekRepository;

	@GetMapping()
	public String showBoekDetails(Model model, Principal principal) {
		List<Boek> boeken = new ArrayList<>();
		boekRepository.findAll().forEach(boek -> {
			if(boek.getFavorieten().size()!=0) {
			boeken.add(boek);
			}
		});
		Collections.sort(boeken, Comparator
		        .comparingInt((Boek boek) -> boek.getFavorieten().size())
		        .reversed()
		        .thenComparing(Boek::getTitel));
		model.addAttribute("boeken",boeken);
		if(!Objects.isNull(principal)) {
			model.addAttribute("userName", principal.getName());
		}
		return "populair";
	}

}
