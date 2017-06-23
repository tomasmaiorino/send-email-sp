package exception;

import lombok.Getter;
import lombok.Setter;

public class FieldError {

	@Getter
	@Setter
	private String message;

	@Getter
	@Setter
	private String field;

	public FieldError(final String message, final String field) {
		this.field = field;
		this.message = message;
	}

}
