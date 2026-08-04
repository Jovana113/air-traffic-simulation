package exceptions;

public class InvalidAirportException extends ProjectException {
	private static final long serialVersionUID = 1L;
	
	public InvalidAirportException(String text) {
		super(text);
	}

}
