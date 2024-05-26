package com.springBoot.bibliotheek;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import repository.BoekRepository;
import repository.AuteurRepository;
import repository.LocatieRepository;
import domain.Auteur;
import domain.Boek;
import domain.Locatie;

@Component 
//@Component implements commandlinerunner: als de applicatie gestart is voer ik deze uit.
public class InitData implements CommandLineRunner {

	@Autowired
	private BoekRepository boekRepository;
	
	@Autowired 
	private AuteurRepository auteurRepository;
	
	@Autowired
	private LocatieRepository locatieRepositoy;
	


	@Override
	public void run(String... args) {
//goede manier voor het project
		Auteur roald = new Auteur("Roald","Dahl");
		Auteur marc = new Auteur("Marc","De Bel");
		Auteur bart = new Auteur("Bart","Moeyaert");
		
		auteurRepository.save(roald);
		auteurRepository.save(marc);
		auteurRepository.save(bart);

		
		Locatie loc1 = new Locatie(125, 150, "FRJ");
		Locatie loc2 = new Locatie(200, 215, "NFRC");
		Locatie loc3 = new Locatie(50, 90, "RRA");
		Locatie loc4 = new Locatie(300, 270, "ABC");
		Locatie loc5 = new Locatie(75, 100, "XYZ");
		Locatie loc6 = new Locatie(180, 200, "PQR");
		Locatie loc7 = new Locatie(80, 120, "MNO");
		Locatie loc8 = new Locatie(250, 280, "DEF");

		locatieRepositoy.save(loc1);
		locatieRepositoy.save(loc2);
		locatieRepositoy.save(loc3);
		locatieRepositoy.save(loc4);
		locatieRepositoy.save(loc5);
		locatieRepositoy.save(loc6);
		locatieRepositoy.save(loc7);
		locatieRepositoy.save(loc8);
		

		
		Boek boek1 = new Boek("De GVR", "9780140315974", BigDecimal.valueOf(29.99));
		Boek boek2 = new Boek("De GVR 2", "9780140315975", BigDecimal.valueOf(39.99));
		Boek boek3 = new Boek("De GVR Homecoming", "9780140315976", BigDecimal.valueOf(49.99));
		
		boek1.addAuteur(bart);
		boek1.addLocatie(loc1);
		boek1.addLocatie(loc2);
		
		boek2.addAuteur(marc);
		boek2.addAuteur(bart);
		boek2.addLocatie(loc3);
		boek2.addLocatie(loc4);
		boek2.addLocatie(loc5);
		
		boek3.addAuteur(bart);
		boek3.addAuteur(marc);
		boek3.addAuteur(roald);
		boek3.addLocatie(loc6);
		boek3.addLocatie(loc7);
		boek3.addLocatie(loc8);
		
		boekRepository.save(boek1);
		boekRepository.save(boek2);
		boekRepository.save(boek3);
		
	}

}
