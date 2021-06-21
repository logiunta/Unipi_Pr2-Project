

//eccezione per utente non trovato
public class UserNotFoundException extends Exception{
	
	public UserNotFoundException() {
		super();
		
	}
	
	public UserNotFoundException(String s) {
		super(s);
	}
}
