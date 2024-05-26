package repository;

import java.util.List;


import org.springframework.data.repository.CrudRepository;

import domain.Boek;

public interface BoekRepository extends CrudRepository<Boek, String> {
	
	//herkent findby en kan zo guest filteren op property name
	List<Boek> findByTitel(String titel);

	Boek findByISBNNr(String boekISBN);

	Boek findByLocatie(int plaatsCode1, int plaatsCode2, String plaatsnaam);

}
