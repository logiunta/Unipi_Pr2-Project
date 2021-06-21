

//eccezione per Accesso Negato per password o id errati
public class AccessDeniedException extends Exception {
	
	public AccessDeniedException() {
		super();
	}
	public AccessDeniedException(String s) {
		super(s);
	}

}
