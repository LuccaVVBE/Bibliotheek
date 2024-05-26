package domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(of ={"voornaam", "achternaam"})
public class Auteur implements Serializable{
	
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
    private Long id;
	
	private String voornaam, achternaam;
	
	@ManyToMany(mappedBy = "auteurs")
	@JsonIgnore
	private Set<Boek> boeken;
	
	public Auteur(String vnaam, String aNaam) {
		setVoornaam(vnaam);
		setAchternaam(aNaam);
		setBoeken(new HashSet<>());
		
	}
	
	public void addBoek(Boek boek) {
		boeken.add(boek);
	}

}
