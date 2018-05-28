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

	public SearchCriteria(final String key, final String operation, final Object value, Class convertClass, final boolean isEnum) {
		this.key = key;
		this.operation = operation;
		this.value = value;
		this.isEnum = isEnum;
		this.convertClass = convertClass;
	}

	@Getter
	private String key;
	@Getter
	private String operation;
	@Getter
	private Object value;

	@Getter
	private Class convertClass;

	@Getter
	private boolean isEnum = false;

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
