package com.springBoot.bibliotheek;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import domain.Boek;
import exceptions.AuteurNotFoundException;
import exceptions.BoekNotFoundException;
import service.BibService;



@RestController
@RequestMapping(value = "/rest")
public class BibRestController {
    
	@Autowired
	private BibService bibService;

    
    @GetMapping(value = "/boek/{ISBNNr}") 
    public Boek getBoek(@PathVariable("ISBNNr") String boekISBN) throws BoekNotFoundException {
    	return bibService.getBoekByISBN(boekISBN);

    }

    @GetMapping(value = "/auteur")
    public List<Boek> getAllBoekFromAuteur(@RequestParam(required = true) String voornaam, @RequestParam(required = true) String achternaam) throws AuteurNotFoundException {
        return bibService.getAllBoekFromAuteur(voornaam, achternaam); 
    }
    
    
}
