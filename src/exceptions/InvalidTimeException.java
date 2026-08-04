package exceptions;

public class InvalidTimeException extends ProjectException {
	private static final long serialVersionUID = 1L;

	public InvalidTimeException(String text) {
		super(text);
	}

}
