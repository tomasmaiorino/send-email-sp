package com.tsm.sendemail.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import lombok.Getter;

import java.util.function.Supplier;

/**
 * Created by tomas on 5/15/18.
 */
public class SearchCriteria {

	public SearchCriteria(final String key, final String operation, final Object value) {
		this.key = key;
		this.operation = operation;
		this.value = value;
	}

	public SearchCriteria(final String key, final String operation, final Object value, Class pConvertEnumClass) {
		this.key = key;
		this.operation = operation;
		this.value = value;
		this.convertEnumClass = pConvertEnumClass;
	}

	@Getter
	private String key;
	@Getter
	private String operation;
	@Getter
	private Object value;

	@Getter
	public Class convertEnumClass;

	@Getter
	private boolean isEnum = false;

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
