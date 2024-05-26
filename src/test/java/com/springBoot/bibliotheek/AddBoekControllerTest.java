package com.springBoot.bibliotheek;

import org.apache.catalina.realm.GenericPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import domain.Boek;
import repository.AuteurRepository;
import repository.BoekRepository;
import repository.LocatieRepository;
import validators.BoekValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

class AddBoekControllerTest {

    @Mock
    private AuteurRepository auteurRepository;

    @Mock
    private BoekRepository boekRepository;

    @Mock
    private LocatieRepository locatieRepository;

    @Mock
    private BoekValidator boekValidator;

    @Mock
    private Model model;

    @InjectMocks
    private AddBoekController addBoekController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowAddBoekForm() {
        // Arrange
        when(auteurRepository.findAll()).thenReturn(Collections.emptyList());
        Principal princ = new GenericPrincipal("admin", List.of("ADMIN"));
        // Act
        
        String viewName = addBoekController.showAddBoekForm(model, princ);

        // Assert
        assertEquals("voegBoekToe", viewName);
        verify(model).addAttribute(eq("nieuwBoek"), any(Boek.class));
        verify(model).addAttribute(eq("alleAuteurs"), eq(Collections.emptyList()));
    }

    @Test
    void testShowAddBoekFormWithPrincipal() {
        // Arrange
        when(auteurRepository.findAll()).thenReturn(Collections.emptyList());
        Principal princ = new GenericPrincipal("admin", List.of("ADMIN"));

        // Act
        String viewName = addBoekController.showAddBoekForm(model, princ);

        // Assert
        assertEquals("voegBoekToe", viewName);
        verify(model).addAttribute(eq("nieuwBoek"), any(Boek.class));
        verify(model).addAttribute(eq("alleAuteurs"), eq(Collections.emptyList()));
        verify(model).addAttribute(eq("userName"), anyString());
    }

    @Test
    void testMaakNieuwBoekWithValidData() {
        // Arrange
        Boek nieuwBoek = new Boek();
        BindingResult bindingResult = mock(BindingResult.class);

        // Act
        String viewName = addBoekController.maakNieuwBoek(nieuwBoek, bindingResult, model);

        // Assert
        assertFalse(bindingResult.hasErrors());
        verify(boekValidator).validate(eq(nieuwBoek), eq(bindingResult));
        verify(boekRepository).save(eq(nieuwBoek));
        assertEquals("redirect:/bib", viewName);
    }

    @Test
    void testMaakNieuwBoekWithInvalidData() {
        // Arrange
        Boek nieuwBoek = new Boek();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(auteurRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        String viewName = addBoekController.maakNieuwBoek(nieuwBoek, bindingResult, model);

        // Assert
        assertTrue(bindingResult.hasErrors());
        verify(boekValidator).validate(eq(nieuwBoek), eq(bindingResult));
        verify(auteurRepository).findAll();
        assertEquals("voegBoekToe", viewName);
    }

}
