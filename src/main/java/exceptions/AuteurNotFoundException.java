package exceptions;

public class AuteurNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public AuteurNotFoundException(String vNaam, String aNaam) {
		super(String.format("Auteur %s %s niet gevonden.", vNaam, aNaam));
	}

}
