package exceptions;

public class InvalidCoordinatesException extends ProjectException {
	private static final long serialVersionUID = 1L;

	public InvalidCoordinatesException(String text) {
		super(text);
	}

}
