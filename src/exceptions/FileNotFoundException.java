package exceptions;

public class FileNotFoundException extends ProjectException {
	private static final long serialVersionUID = 1L;

	public FileNotFoundException(String text) {
		super(text);
	}

}
