package exceptions;

public class BoekNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public BoekNotFoundException(String ISBNNr) {
		super(String.format("Boek met ISBN: %s is niet gevonden.", ISBNNr));
	}

}
