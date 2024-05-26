package domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import repository.LocatieRepository;

@Getter @Setter
@Entity
@NoArgsConstructor
@NamedQueries({ 
	@NamedQuery(name = "Boek.findByLocatie", 
	query = "SELECT b FROM Boek b "
			+ "JOIN b.locaties l WHERE :plaatsCode1 = l.plaatsCode1 AND :plaatsCode2 = l.plaatsCode2 AND :plaatsnaam = l.plaatsnaam"
	) })
public class Boek implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	private String titel;
	
	@ManyToMany
	private Set<Auteur> auteurs = new HashSet<>();
	
	@Id
	@Size(min = 13, max=17, message="{isbn.length}")
	private String ISBNNr;
	
	@Nullable
	@Min(1)
	@Max(99)
	@NumberFormat(pattern="#,##0.00")
	private BigDecimal prijs;
	
	@ManyToMany
	@NotNull
	private List<Locatie> locaties = new ArrayList<>();
	
	private List<String> favorieten = new ArrayList<>();
	
	public Boek(String titel, String ISBN, BigDecimal prijs) {
		setTitel(titel);
		setISBNNr(ISBN);
		setPrijs(prijs);
	}
	
	
	public void addAuteur(Auteur auteur) {
		if(auteurs.size()<3 && !auteurs.contains(auteur)) {
			auteurs.add(auteur);
		}
	}
	
	public void addLocatie(@Valid Locatie locatie) {
		if(locaties.size()<3 && !locaties.contains(locatie)) {
			locaties.add(locatie);
		}
	}


	public void saveLocaties(LocatieRepository locrepo) {
		// TODO Auto-generated method stub
		locaties.removeAll(locaties.stream().filter(loc->loc.isEmpty()).toList());
		locaties.forEach(loc->{
			addLocatie(loc);
			locrepo.save(loc);
			});
		
	}


	public String editFavoriet(String name) {
		if(favorieten.contains(name)) {
			favorieten.remove(name);
			return "removed";
		}
		favorieten.add(name);
		
		return "added";
	}
}
