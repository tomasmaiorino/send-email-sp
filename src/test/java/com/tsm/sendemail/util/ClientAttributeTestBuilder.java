package com.tsm.sendemail.util;

import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.HashMap;
import java.util.Map;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.ClientAttribute;

public class ClientAttributeTestBuilder {

	public static ClientAttribute buildModel() {
		return buildModel(getClient(), getKey(), getValue());
	}

	public static String getKey() {
		return random(5, true, true);
	}

	public static Client getClient() {
		return ClientTestBuilder.buildModel();
	}

	public static String getValue() {
		return random(20, true, true);
	}

	public static Map<String, String> getAttributes(Integer size) {
		Map<String, String> atts = new HashMap<>();
		while (size-- > 0) {
			atts.put(getKey(), getValue());
		}
		return atts;
	}

	public static ClientAttribute buildModel(final Client client, final String key, final String value) {
		return ClientAttribute.ClientAttributeBuilder.ClientAttribute(client, key, value).build();
	}

}
