
public class User {
	/* OVERVIEW: Rappresenta la struttura di un utente con le sue credenziali private,
	 * 
	 * TYPICAL ELEMENTS: <Id,Passw>
	 * 
	 * IR: Id!=null && passw!=null
	 */
	private String Id;
	private String passw;
	
	//costruttore 
	public User(String Id,String passw) {
		this.Id=Id;
		this.passw=passw;
		
		/* EFFECTS: inizializza this = <Id,passw>
		 */
	}
	
	@Override
	public boolean equals(Object other) {
		User o = (User) other;
		return (Id.equals(o.getId()) && passw.equals(o.getPassw()));
		
		/* EFFECTS: restituisce true se l'utente other ha Id e passw uguali a quelle di this,
		 * 		   false altrimenti

		 */
	}
	
	public String getId() {
		return Id;
		
		/*EFFECTS: restituisce l'Id di this
		 */
	}
	public String getPassw() {
		return passw;
		
		/*EFFECTS: restituisce la passw di this
		 * 
		 */
	}
	
	@Override
	public String toString() {
		return Id+","+passw;
		
		/*EFFECTS: restituisce sotto-forma di stringhe Id e passw di this
		 * 
		 */
	}
	
	
}
