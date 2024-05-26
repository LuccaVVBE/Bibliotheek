package com.springBoot.bibliotheek;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void loginGet() throws Exception {
	    	mockMvc.perform(get("/login"))
	        .andExpect(status().isOk())
	    	.andExpect(view().name("login"));
	}
	 

	 
	@WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testAccessWithUserRole() throws Exception {
        mockMvc.perform(get("/bib"))
        .andExpect(status().isOk())
		.andExpect(view().name("bibpage"))
		.andExpect(model().attributeExists("userName"))
		.andExpect(model().attribute("userName", "user"));
    }
	
}
