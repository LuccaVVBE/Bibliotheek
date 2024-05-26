package perform;


import org.springframework.web.reactive.function.client.WebClient;



import domain.Boek;


public class PerformRest {
	private final String SERVER_URI = "http://localhost:8080/rest";
	private WebClient webClient = WebClient.create();

	public PerformRest() throws Exception {
		System.out.println("\n------- GET BOEK BY ISBNNR: 9780140315974 -------");
		getBoekByISBNNR("9780140315974");
		
		System.out.println("------- GET ALL BOEK FROM AUTEUR: BART MOEYAERT ------- ");
		getAllBoekFromAuteur("Bart","Moeyaert");
	}

	private void getAllBoekFromAuteur(String voornaam, String achternaam) {
		webClient.get()
	    .uri(SERVER_URI + "/auteur?voornaam="+voornaam+"&achternaam="+achternaam)
	    .retrieve()
	    .bodyToFlux(Boek.class)  // Use bodyToFlux instead of bodyToMono
        .doOnNext(this::printBoekData)  // Use doOnNext to handle each Boek object individually
        .blockLast();  // Wait for the stream to complete
		
	}

	private void getBoekByISBNNR(String ISBN) {
		webClient.get()
	    .uri(SERVER_URI + "/boek/"+ISBN)
	    .retrieve()
	    .bodyToMono(Boek.class)
	    .doOnSuccess(boek -> printBoekData(boek))
	    .block();
		
	}




	private void printBoekData(Boek boek) {
		System.out.printf("Titel=%s, Prijs=%.2f ISBN=%s%n", 
				boek.getTitel(), boek.getPrijs(), boek.getISBNNr());
	}
}
