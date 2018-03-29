package com.tsm.it.resource;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

public abstract class BaseItResource {

	@JsonIgnore
	@Getter
	@Setter
	protected Map<String, String> headers;
}
