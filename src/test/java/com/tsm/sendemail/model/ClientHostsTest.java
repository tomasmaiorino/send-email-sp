package com.tsm.sendemail.model;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public class ClientHostsTest {

	@Test(expected = IllegalArgumentException.class)
	public void build_NullHostGiven_ShouldThrowException() {

		// Set up
		String host = null;

		// Do test
		ClientHosts hosts = new ClientHosts();
		hosts.setHost(host);
	}
}
