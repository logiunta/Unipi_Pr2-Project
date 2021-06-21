

import java.util.*;

public class StructureData<E> {
		/* OVERVIEW: Gestisce il dato e la collezione degli utenti che possono accedervi, secondo regole di permessi.
		 * 	
		 * TYPICAL ELEMENTS: <{lista_utenti.get(i) | 0<= i < lista_utenti.size()} , data>
		 * 
		 * IR: lista_utenti!=null && data!=null  forall i,j => (lista_utenti.get(i)!=lista_utenti.get(j) &&
		 * 	   (lista_utenti.get(i) && lista_utenti.get(j)) != null)
		 * 
		 */
		private Vector<String> lista_utenti;
		private E data;
		
		//costruttore
		public StructureData(String owner,E data) throws NullPointerException{
			if(owner==null) throw new NullPointerException();
			if(data==null) throw new NullPointerException();
			this.data=data;
			lista_utenti=new Vector<>();
			lista_utenti.add(owner);
			
			/* REQUIRES: owner!=null && data!=null
			 * 
			 * THROWS: se (owner==null || data==null) lancia NullPointerException
			 * 
			 * MODIFIES: this
			 * 
			 * EFFECTS: crea la coppia <{owner},data>
			 * 
			 */
		}
		
		public E getData() {
			return data;
			
			/*	EFFECTS: restituisce this
			 */
		}
		
		public void addOther(String IdOther) throws IllegalArgumentException {
			if(lista_utenti.contains(IdOther)) throw new IllegalArgumentException();
			lista_utenti.add(IdOther);
			
			/* REQUIRES: !lista_utenti.contains(IdOther)
			 * 
			 * THROWS: se lista_utenti.contains(IdOther) lancia IllegalArgumentException
			 * 
			 * MODIFIES: this
			 * 
			 * EFFECTS: aggiunge a lista_utenti IdOther se non era già presente
			 * 
			 */
			
		}
		//restituisce true se IdUser ha il permesso di vedere il dato
		public boolean IsPermitted(String IdUser) {
			return lista_utenti.contains(IdUser);
			 
			/* EFFECTS: restituisce true se l'utente con IdUser può accedere al dato, false altrimenti
			 * 
			 */
		}
		
}

		
		
		
		
		
		
		
