package repository;

import org.springframework.data.repository.CrudRepository;

import domain.Auteur;

public interface AuteurRepository extends CrudRepository<Auteur, Long>{

	Auteur findByVoornaamAndAchternaam(String firstName, String lastName);

}
