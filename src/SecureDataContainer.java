

import java.util.Iterator;

public interface SecureDataContainer<E> {
	/* OVERVIEW: Tipo modificabile che rappresenta una collezione di n oggetti di tipo <E>, ciascuno dei quali può
	 * 			 essere accessibile da un insieme di m utenti.L'utente che inserisce il dato nella collezione ne diventa il "proprietario"
	 * 			 e può dare poteri di condivisione,rimozione,scrittura e lettura ad altri utenti.
	 * 			 		
	 * TYPICAL ELEMENTS: tripla < {insieme degli utenti}  , {insieme dei dati}  , {insieme delle associazioni} > nello specifico :
	 * 
	 * 					< { <Id_i,passw_i> | 0 <= i < m }, {data_j | 0 <= j < n} , { < Id_k, data_s> | 0 <= k < m && 0 <= s < n}> dove :
	 *					forall i,p | (0 <= i < p < m) => Id_i!=passw_p &&
	 * 					forall k,s | (0 <= k < m && 0 <= s < n) =>(Id_k appartiene all'insieme degli utenti && 
	 * 					data_s appartiene all'insieme dei dati
	 */

	
	//Crea l' identità di un nuovo utente nella collezione
	public void createUser(String Id, String passw) throws UserAlreadyExistsException,NullPointerException;
		/* REQUIRES: Id!=null && pass!=null && non esiste una coppia <Id,p> con p qualsiasi nell'insieme degli utenti
		 * 
		 * THROWS: se esiste una coppia <Id,p> con p qualsiasi nell'insieme degli utenti allora lancia UserAlreadyExistsException(checked) &&
		 * 		   se (Id==null || pass==null) lancia NullPointerException
		 * 
		 * MODIFIES: modifica l'insieme degli utenti
		 * 		
		 * EFFECTS: crea e aggiunge una nuova coppia <Id,passw> all'insieme degli utenti
		 */
		
	
	// Restituisce il numero degli elementi di un utente presenti nella
	// collezione
	public int getSize(String Owner, String passw) throws AccessDeniedException,NullPointerException;
		/* REQUIRES: Owner!=null && passw!=null && la coppia <Owner,passw> è presente nell'insieme degli utenti
		 * 
		 * THROWS: se la coppia <Owner,passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
		 * 		   se (Owner==null ||pass==null) lancia NullPointerException
		 * 
		 * EFFECTS: restituisce la cardinalità dell'insieme di coppie : { <Id_i,data_j> | forall (0 <= i < m && 0 <= j < n) => Id_i==Owner}
		 * 		 
		 */
	
	
	// Inserisce il valore del dato nella collezione
	// se vengono rispettati i controlli di identità
	public boolean put(String Owner, String passw, E data) throws AccessDeniedException , NullPointerException;
		/* REQUIRES: Owner!=null && passw!=null && data!=null && la coppia <Owner,passw> è presente nell'insieme degli utenti
		 * 
		 * THROWS: se la coppia <Owner,passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
		 * 		   se (Owner==null ||pass==null ||data==null) lancia NullPointerException
		 * 
		 * MODIFIES: modifica l'insieme delle associazioni
		 * 
		 * EFFECTS: restituisce true se data è stato inserito correttamente , ovvero se è stata aggiunta sia una coppia <Owner,data>
		 * 			nell'insieme delle associazioni che data nell'insieme dei dati, restituisce false altrimenti		
		 */
	
	
	// Ottiene una copia del valore del dato nella collezione
	// se vengono rispettati i controlli di identità
	public E get(String Owner, String passw, E data) throws  NullPointerException,AccessDeniedException;
		/* REQUIRES: Owner!=null && passw!=null && data!=null && la coppia <Owner,passw> è presente nell'insieme degli utenti
		 * 
		 * THROWS: se la coppia <Owner,passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
		 * 		   se (Owner==null ||pass==null ||data==null) lancia NullPointerException
		 * 
		 * EFFECTS: se nell'insieme delle associazioni esiste una coppia <Owner,data_j> tale che data_j==data e se nell'insieme
		 * 			dei dati è presente data allora restitusce data_j , null altrimenti
		 */
	
	// Rimuove il dato nella collezione
	// se vengono rispettati i controlli di identità
	public E remove(String Owner, String passw, E data) throws  NullPointerException,AccessDeniedException;
		/* REQUIRES: Owner!=null && passw!=null && data!=null && la coppia <Owner,passw> è presente nell'insieme degli utenti
		 * 
		 * THROWS: se la coppia <Owner,passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
		 * 		   se (Owner==null ||pass==null || data==null) lancia NullPointerException
		 * 
		 * MODIFIES: modifica l'insieme dei dati e l'insieme delle associazioni
		 * 
		 * EFFECTS:	se esiste la coppia <Owner,data> nell'insieme delle associazioni ed esiste il dato nell'insieme dei dati allora 
		 * 			rimuove sia questa coppia che il dato e restituisce il dato, altrimenti restituisce null
		 * 			
		 */
	

	// Crea una copia del dato nella collezione
	// se vengono rispettati i controlli di identità
	public void copy(String Owner, String passw, E data) throws NullPointerException,AccessDeniedException,IllegalArgumentException;
		/* REQUIRES: Owner!=null && passw!=null && data!=null && la coppia <Owner,passw> è presente nell'insieme degli utenti
					&& esiste la coppia <Owner,data> nell'insieme delle associazioni
		 * 
		 * THROWS: se la coppia <Owner,passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
		 * 		   se (Owner==null ||pass==null || data==null) lancia NullPointerException &&
		 * 		   se non esiste la coppia <Owner,data> nell'insieme delle associazioni allora lancia IllegalArgumentException
		 * 	
		 * MODIFIES: modifica l'insieme dei dati e l'insieme delle associazioni
		 * 
		 * EFFECTS: aggiunge sia una nuova coppia <Owner,data> nell'insieme delle associazioni che una copia di data nell'insieme dei dati
		 */
		
	// Condivide il dato nella collezione con un altro utente
	// se vengono rispettati i controlli di identità
	public void share(String Owner, String passw, String Other, E data) throws AccessDeniedException , NullPointerException, UserNotFoundException,IllegalArgumentException,IllegalDataException;
		/* REQUIRES: Owner!=null && passw!=null && data!=null && Other !=null && la coppia <Owner,passw> è presente nell'insieme degli utenti
		 *			&& esiste la coppia <Owner,data> nell'insieme delle associazioni && 
		 *			esiste la coppia <Other,p> , p qualunque !=null, nell'insieme degli utenti && !Owner.equals(Other)
		 *			
		 * THROWS: se la coppia <Owner,passw> non è presente nell'insieme di tutti gli utenti allora lancia AccessDeniedException(checked) &&
		 * 		   se (Owner==null ||pass==null || data==null) lancia NullPointerException &&
		 * 		   se non esiste la coppia <Owner,data> nell'insieme delle associazioni allora lancia IllegalDataException(checked) &&
		 * 		   se non esiste la coppia <Other,p> nell'insieme degli utenti lancia UserNotFoundException(checked) &&
		 * 		   se Owner.equals(Other) lancia IllegalArgumentException
		 * 
		 * MODIFIES: modifica l'insieme delle associazioni
		 * 
		 * EFFECTS: aggiunge la coppia <Other,data> all'insieme delle associazioni
		 * 	
		 */
					
	
	// restituisce un iteratore (senza remove) che genera tutti i dati
	//dell'utente in ordine arbitrario
	// se vengono rispettati i controlli di identità
	public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException,AccessDeniedException;
		/* REQUIRES: Owner!=null && passw!=null && la coppia <Owner,passw> è presente nell'insieme degli utenti
		 * 
		 * THROWS: se la coppia <Owner,passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
		 * 		   se (Owner==null ||pass==null) lancia NullPointerException
		 * 
		 * EFFECTS: restituisce tutti i data_j appartenenti all'insieme: { data_j | forall 0 <= j < n => 
		 * 			(<Owner,data_j> appartiene all'insieme delle associazioni && data_j è contenuto nell'insieme dei dati)
		 * 
		 */
	
			
}	//fine interfaccia



