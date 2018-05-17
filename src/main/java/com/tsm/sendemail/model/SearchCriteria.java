package com.tsm.sendemail.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import lombok.Getter;

/**
 * Created by tomas on 5/15/18.
 */
public class SearchCriteria {

	public SearchCriteria(final String key, final String operation, final Object value) {
		this.key = key;
		this.operation = operation;
		this.value = value;
	}

	@Getter
	private String key;
	@Getter
	private String operation;
	@Getter
	private Object value;

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
