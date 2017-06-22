package exception;

import lombok.Getter;
import lombok.Setter;

public class FieldError {

	@Getter
	@Setter
	private String error;

	@Getter
	@Setter
	private String field;

	public FieldError(final String error, final String field) {
		this.field = field;
		this.error = error;
	}

}
