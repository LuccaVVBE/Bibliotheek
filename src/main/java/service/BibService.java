package service;

import java.util.List;

import domain.Boek;
import exceptions.AuteurNotFoundException;
import exceptions.BoekNotFoundException;

public interface BibService {


	public Boek getBoekByISBN(String boekISBN) throws BoekNotFoundException ;

	public List<Boek> getAllBoekFromAuteur(String voornaam, String achternaam) throws AuteurNotFoundException ;



}
