package exceptions;

public class ProjectException extends Exception {
	private static final long serialVersionUID = 1L;

	public ProjectException(String text){
		super(text);
	}
}
