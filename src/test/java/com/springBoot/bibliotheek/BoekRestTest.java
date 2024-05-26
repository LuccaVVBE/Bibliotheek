package com.springBoot.bibliotheek;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import domain.Boek;
import exceptions.BoekNotFoundException;
import service.BibService;

@SpringBootTest
class BoekRestTest {

	@Mock
	private BibService mock;
	
	private BibRestController controller;
	private MockMvc mockMvc;

	private final String ISBN = "9789026357701";
	private final String NAME = "Test";
	private final BigDecimal bigDecimal = new BigDecimal(10);
	private final String fname = "Roald";
	private final String lname = "Dahl";

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		controller = new BibRestController();
		mockMvc = standaloneSetup(controller).build();
		ReflectionTestUtils.setField(controller, "bibService", mock);
	}

	private Boek aBoek(String isbn, String name, BigDecimal bd) {
		Boek boek = new Boek(name, isbn,  bd);
		
		return boek;
	}

	private void performRest(String uri) throws Exception {
		mockMvc.perform(get(uri))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.titel").value(NAME))
		.andExpect(jsonPath("$.prijs").value(bigDecimal))
		.andExpect(jsonPath("$.isbnnr").value(ISBN));
	}
	

	
	@Test
	public void testGetBoek_isOk() throws Exception {
		Mockito.when(mock.getBoekByISBN(ISBN)).thenReturn(aBoek(ISBN, NAME, bigDecimal));
		performRest("/rest/boek/" + ISBN);
		Mockito.verify(mock).getBoekByISBN(ISBN);
	}

	@Test
	public void testGetEmployee_notFound() throws Exception {
		Mockito.when(mock.getBoekByISBN(ISBN)).thenThrow(new BoekNotFoundException(ISBN));
		Exception exception = assertThrows(Exception.class, () -> {
			mockMvc.perform(get("/rest/boek/" + ISBN)).andReturn();
	    });
		
		assertTrue(exception.getCause() instanceof BoekNotFoundException);
		
		Mockito.verify(mock).getBoekByISBN(ISBN);
	}

	@Test
	public void testGetAllBoekFromAuteur_emptyList() throws Exception {
		Mockito.when(mock.getAllBoekFromAuteur(fname, lname)).thenReturn(new ArrayList<>());
		
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("voornaam", "Roald");
        params.add("achternaam", "Dahl");
		
		
		mockMvc.perform(get("/rest/auteur").params(params))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());
		
		Mockito.verify(mock).getAllBoekFromAuteur(fname, lname);
	}

	@Test
	public void testGetAllBoekFromAuteur_noEmptyList() throws Exception {
		
		Boek boek = new Boek(NAME, ISBN, bigDecimal);
		List<Boek> boeken = List.of(boek);
		
		Mockito.when(mock.getAllBoekFromAuteur(fname, lname)).thenReturn(boeken);
		
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("voornaam", "Roald");
        params.add("achternaam", "Dahl");
		
		
		mockMvc.perform(get("/rest/auteur").params(params))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isNotEmpty())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].titel").value(NAME))
				.andExpect(jsonPath("$[0].prijs").value(bigDecimal))
				.andExpect(jsonPath("$[0].isbnnr").value(ISBN));
		
		Mockito.verify(mock).getAllBoekFromAuteur(fname, lname);
		
		
	}

}
