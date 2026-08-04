package exceptions;

public class InvalidCodeException extends ProjectException{
	private static final long serialVersionUID = 1L;

	public InvalidCodeException(String text) {
		super(text);
	}

}
