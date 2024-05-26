package validators;

import java.util.Set;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Boek;
import domain.Locatie;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import repository.BoekRepository;



public class BoekValidator implements Validator {
	
	@Autowired
	private BoekRepository boekRepository;

	@Override
	public boolean supports(Class<?> klass) {
		return Boek.class.isAssignableFrom(klass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Boek boek = (Boek) target;
		
		if(boek.getTitel().isBlank()) {
			errors.rejectValue("titel", "titel.blank", "Empty");
		}
		try {
		if(boek.getPrijs().compareTo(BigDecimal.ONE)<0 || boek.getPrijs().compareTo(BigDecimal.valueOf(100.0))>0) {
			Field field = null;
			try {
				field = Boek.class.getDeclaredField("prijs");
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
        	Min minAnnotation = field.getAnnotation(Min.class);
        	Max maxAnnotation = field.getAnnotation(Max.class);

        	int min = (int)minAnnotation.value();
        	int max = (int)maxAnnotation.value();
        	
            errors.rejectValue("prijs", "boek.prijs", new Object[] {min, max},"prijsrange");
		}
		}catch(NullPointerException npe) {
			//caught met typeMismatch
		}
		
		if(boek.getAuteurs().isEmpty() || boek.getAuteurs().size()>3) {
			Object[] arguments = {1, 3};
			errors.rejectValue("auteurs","auteurs.empty",arguments, "Empty");
		}
		
		if(boekRepository.findByISBNNr(boek.getISBNNr()) != null) {
			errors.rejectValue("ISBNNr", "isbn.exists", "Exists");
		}
		
		int teller = 1;
		int even = 0;
		int onEven = 0;
		for(String getal : boek.getISBNNr().replaceAll(" ", "").replaceAll("-", "").split("")) {
			
			if(getal.matches("\\d")) {
				if(teller==13) {
					//zero based index!
					System.out.println("Laatste getal: " + getal);
					even*=3;
					even+=onEven;
					System.out.println(even);
					//charAt is zero based = 12
					if(Integer.parseInt(String.valueOf(boek.getISBNNr().charAt(teller-1)))!=(10-even%10)) {
						errors.rejectValue("ISBNNr", "isbn.format", "format");
					}
					
					
					break;
				}
				if(teller%2==0) {
					
					even+=Integer.parseInt(getal);
					System.out.println("even ik heb " + getal + " erbij geteld. " + even);
				}
				else {
					onEven+=Integer.parseInt(getal);
					System.out.println("on ik heb " + getal + " erbij geteld. " + onEven);
				}
			}
			else {
				errors.rejectValue("ISBNNr", "isbn.numOnly", "numOnly");
				break;
			}
			
			teller++;
			
		}
		

		List<Locatie> loclijst = boek.getLocaties().stream().filter(loc->!loc.isEmpty()).collect(Collectors.toList());
		
		if(loclijst.size()<1) {
			errors.rejectValue("locaties", "locaties.min", "Er moet minstens 1 locatie zijn.");
		}
		
		for (Locatie locatie : loclijst) {
			if(boekRepository.findByLocatie(locatie.getPlaatsCode1(), locatie.getPlaatsCode2(), locatie.getPlaatsnaam())!=null) {
				errors.rejectValue("locaties", "locatie.occupied", new Object[] {locatie.getPlaatsCode1(), locatie.getPlaatsCode2(), locatie.getPlaatsnaam()}, "Occupied");
			}
			
			
			
	        // Manually validate each Locatie object
	        jakarta.validation.Validator locatieValidator = Validation.buildDefaultValidatorFactory().getValidator();
	        Set<ConstraintViolation<Locatie>> locatieViolations = locatieValidator.validate(locatie);
	        for (ConstraintViolation<Locatie> violation : locatieViolations) {
	        	if(violation.getMessage().equals("plaatscode.range")) {
	        	Field field = null;
				try {
					field = Locatie.class.getDeclaredField("plaatsCode1");
				} catch (NoSuchFieldException | SecurityException e) {
					e.printStackTrace();
				}
	        	Range rangeAnnotation = field.getAnnotation(Range.class);

	        	int min = (int)rangeAnnotation.min();
	        	int max = (int)rangeAnnotation.max();
	        	
	            errors.rejectValue("locaties", violation.getMessage(), new Object[] {min, max},"Niet resource bundel + " + violation.getMessage());
	        }
	        	else {
	        		errors.rejectValue("locaties", violation.getMessage(),"Niet resource bundel + " + violation.getMessage());
	        	}
	        }
	        
	        if(Math.abs(locatie.getPlaatsCode1()-locatie.getPlaatsCode2())<50) {
	        	errors.rejectValue("locaties", "plaatscodes.difference", new Object[] {50}, "Difference");
	        }
	    }
	}
	
	
}
