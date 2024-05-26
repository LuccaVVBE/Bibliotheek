package repository;

import org.springframework.data.repository.CrudRepository;

import domain.Locatie;

public interface LocatieRepository extends CrudRepository<Locatie, Long>{

	Locatie findByAllFields(String pc1, String pc2, String pn);
}
