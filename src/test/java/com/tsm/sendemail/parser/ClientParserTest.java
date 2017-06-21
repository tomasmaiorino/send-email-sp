package com.tsm.sendemail.parser;

import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.resources.ClientResource;
import com.tsm.sendemail.util.ClientTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ClientParserTest {

	private ClientParser parser = new ClientParser();

	@Test(expected = IllegalArgumentException.class)
	public void toModel_NullResourceGiven_ShouldThrowException() {
		// Set up
		ClientResource resource = null;

		// Do test
		parser.toModel(resource);
	}
	
	@Test
	public void toModel_ValidResourceGiven_ShouldCreateClientModel() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();

		// Do test
		Client result = parser.toModel(resource);
		
		// Assertions 
		assertNotNull(result);
//		assertThat(result, allOf(hasProperty("", is(resource.getName()))));
		
	}
}
