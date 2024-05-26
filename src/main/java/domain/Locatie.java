package domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "boeken"})
@NamedQueries({ 
	@NamedQuery(name = "Locatie.findByAllFields", 
	query = "SELECT l FROM Locatie l WHERE :pc1 = l.plaatsCode1 AND :pc2 = l.plaatsCode2 AND :pn = l.plaatsnaam") })
public class Locatie implements Serializable {
	
	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
    private Long id;
	
	@ManyToMany(mappedBy = "locaties")
	@JsonIgnore
	private Set<Boek> boeken = new HashSet<>();
	
	@Range(min = 50, max=300, message = "plaatscode.range")
	private int plaatsCode1;
	@Min(value=50, message = "plaatscode.range")
	@Max(value=300, message="plaatscode.range")
	private int plaatsCode2;
	@Pattern(regexp = "[A-Za-z]+", message = "plaatsnaam.charOnly")
	private String plaatsnaam;
	
	public Locatie(int pc1, int pc2, String pn) {
		setPlaatsCode1(pc1);
		setPlaatsCode2(pc2);
		setPlaatsnaam(pn);
	}
	
	public void addBoek(Boek boek) {
		boeken.add(boek);
	}
	
	@JsonIgnore
	public boolean isEmpty() {
		
		return (plaatsCode1==0 || plaatsCode2==0 || plaatsnaam == null || plaatsnaam.isBlank());
	}
}
