package com.springBoot.bibliotheek;


import java.security.Principal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Boek;
import repository.BoekRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BoekControllerTest {

    @Mock
    private BoekRepository boekRepository;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private BoekController boekController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowBoekDetails() {
        // Arrange
        String ISBNnr = "123456789";
        Boek boek = new Boek();
        boek.setTitel("Test Boek");
        when(boekRepository.findByISBNNr(ISBNnr)).thenReturn(boek);
        when(principal.getName()).thenReturn("username");

        // Act
        String viewName = boekController.showBoekDetails(ISBNnr, model, principal);

        // Assert
        assertEquals("boekDetails", viewName);
        verify(model).addAttribute(eq("boek"), eq(boek));
        verify(model).addAttribute(eq("userName"), eq("username"));
        verify(model).addAttribute(eq("isFavoriet"), eq(false));
    }

    @Test
    void testShowBoekDetailsWithFavoriet() {
        // Arrange
        String ISBNnr = "123456789";
        Boek boek = new Boek();
        boek.setTitel("Test Boek");
        boek.setFavorieten(Arrays.asList("username"));
        when(boekRepository.findByISBNNr(ISBNnr)).thenReturn(boek);
        when(principal.getName()).thenReturn("username");

        // Act
        String viewName = boekController.showBoekDetails(ISBNnr, model, principal);

        // Assert
        assertEquals("boekDetails", viewName);
        verify(model).addAttribute(eq("boek"), eq(boek));
        verify(model).addAttribute(eq("userName"), eq("username"));
        verify(model).addAttribute(eq("isFavoriet"), eq(true));
    }

    @Test
    void testShowBoekDetailsWithoutPrincipal() {
        // Arrange
        String ISBNnr = "123456789";
        Boek boek = new Boek();
        boek.setTitel("Test Boek");
        when(boekRepository.findByISBNNr(ISBNnr)).thenReturn(boek);

        // Act
        String viewName = boekController.showBoekDetails(ISBNnr, model, null);

        // Assert
        assertEquals("boekDetails", viewName);
        verify(model).addAttribute(eq("boek"), eq(boek));
        verify(model, never()).addAttribute(eq("userName"), anyString());
        verify(model, never()).addAttribute(eq("isFavoriet"), anyBoolean());
    }

    @Test
    void testAddOrRemoveFavoriet() {
        // Arrange
        String ISBNnr = "123456789";
        Boek boek = mock(Boek.class);
        boek.setTitel("Test Boek");
        when(boekRepository.findByISBNNr(ISBNnr)).thenReturn(boek);
        when(principal.getName()).thenReturn("username");
        when(boek.editFavoriet("username")).thenReturn("added");
        when(boek.getTitel()).thenReturn("Test Boek");

        // Act
        String redirectUrl = boekController.AddOrRemoveFavoriet(ISBNnr, model, principal, redirectAttributes);

        // Assert
        assertEquals("redirect:/bib?added", redirectUrl);
        verify(boek).editFavoriet("username");
        verify(boekRepository).save(boek);
        verify(redirectAttributes).addFlashAttribute(eq("titel"), eq("Test Boek"));
        
        //Arrange
        when(boek.editFavoriet("username")).thenReturn("removed");
        
        //Act
        redirectUrl = boekController.AddOrRemoveFavoriet(ISBNnr, model, principal, redirectAttributes);
        assertEquals("redirect:/bib?removed", redirectUrl);
        verify(boek, times(2)).editFavoriet("username");
        verify(boekRepository, times(2)).save(boek);
        verify(redirectAttributes, times(2)).addFlashAttribute(eq("titel"), eq("Test Boek"));
        
    }
}
