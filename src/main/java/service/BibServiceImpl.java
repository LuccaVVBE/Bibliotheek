package service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import domain.Auteur;
import domain.Boek;
import exceptions.AuteurNotFoundException;
import exceptions.BoekNotFoundException;
import repository.AuteurRepository;
import repository.BoekRepository;

public class BibServiceImpl implements BibService{
	
	@Autowired
	private BoekRepository boekRepository;
	
	@Autowired
	private AuteurRepository auteurRepository;
	
	


	@Override
	public Boek getBoekByISBN(String boekISBN) throws BoekNotFoundException {
		Boek boek = boekRepository.findByISBNNr(boekISBN);
		if(boek == null) {
			throw new BoekNotFoundException(boekISBN);
		}
		return boekRepository.findByISBNNr(boekISBN);
	}

	@Override
	public List<Boek> getAllBoekFromAuteur(String firstName, String lastName) throws AuteurNotFoundException {
		// TODO Auto-generated method stub
		Auteur teZoeken = auteurRepository.findByVoornaamAndAchternaam(firstName, lastName);
		if(teZoeken == null) {
			throw new AuteurNotFoundException(firstName, lastName);
		}
		List<Boek> boeken = new ArrayList<>();
		boekRepository.findAll().forEach(boek->boeken.add(boek));
		return boeken.stream().filter(boek->boek.getAuteurs().contains(teZoeken)).collect(Collectors.toUnmodifiableList());
	}

}
