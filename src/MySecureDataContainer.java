
import java.util.Iterator;
import java.util.Vector;



public class MySecureDataContainer<E> implements SecureDataContainer<E>{
	/* OVERVIEW: Tipo modificabile che rappresenta una collezione di n oggetti di tipo <E>, ciascuno dei quali può
	 * 			 essere accessibile da un insieme di m utenti.L'utente che inserisce il dato nella collezione ne diventa il "proprietario"
	 * 			 e può dare poteri di condivisione,rimozione,scrittura e lettura ad altri utenti.
	 * 
	 * TYPICAL ELEMENTS: tripla < {insieme degli utenti}  , {insieme dei dati}  , {insieme delle associazioni} > nello specifico :
	 * 
	 * 					< { <Id_i,passw_i> | 0 <= i < m }, {data_j | 0 <= j < n} , { < Id_k, data_s> | 0 <= k < m && 0 <= s < n}> dove :
	 *					forall i,p | (0 <= i < p < m) => Id_i!=passw_p &&
	 * 					forall k,s | (0 <= k < m && 0 <= s < n) => (Id_k appartiene all'insieme degli utenti && 
	 * 					data_s appartiene all'insieme dei dati
	 * 
	 * Abstraction Function
	 * AF(c): < {<c.users.get(i).getId() , c.users.get(i).getPassw()> | (0 <= i < c.users.size())} , 
	 * 		    {c.dati.get(j).getData() | (0 <= j < c.dati.size())} ,
	 * 		    { < c.users.get(k).getId() , c.dati.get(s).getData()> | (0 <= k < c.users.size()) && 
	 * 		    (0 <= s < c.dati.size()) && (c.dati.get(s).getData().IsPermitted(users.get(k).getId()))} >
	 * 
	 * IR: users!=null && dati!=null && 
	 *     forall i,j | ( 0 < = i < users.size() && 0 <= j < dati.size()) => (users.get(i)!=null && dati.get(j)!=null) &&
	 * 	   forall i,p | (0 <= i < p < users.size() )  => (! users.get(i).getId().equals(users.get(p).getId())) &&
	 *	   forall k,s | (0 <= k < users.size() && 0 <= s < dati.size()) => (dati.get(s).getData().IsPermitted(users.get(k).getId())) 
	 *	   forall t | (0 <= t < dati.size()) => (exists q | 0 <= q < users.size() && getStructureData(users.get(q).getId() , dati.get(t).getData()) != null)
	 *	   
	 */
	
	private Vector<User> users;
	private Vector<StructureData> dati;
	
	
	//costruttore
	public MySecureDataContainer() {
		users= new Vector<>();
		dati= new Vector<>();
		
		/* MODIFIES: this
		 * 
		 * EFFECTS: istanzia i vettori users e dati
		 * 
		 */
	}
	

	public void createUser(String Id, String passw) throws UserAlreadyExistsException,NullPointerException{
		if(Id==null) throw new NullPointerException();
		if(passw==null) throw new NullPointerException();
		if(UserExists(Id)) throw new UserAlreadyExistsException();
			users.add(new User(Id, passw));
			
		/* REQUIRES: Id!=null && passw!=null && !UserExists(Id)
		 * 
		 * THROWS: se (Id==null || passw==null) lancia NullPointerException &&
		 * 		   se (users.contains(new User(Id,passw))) lancia UserAlreadyExists(checked)
		 * 
		 * MODIFIES: this
		 * 
		 * EFFECTS: crea e aggiunge un nuovo utente User(Id,passw) se e solo se l'utente non era già presente nel vettore users
		 * 
		 */
	 
	}
	
	
	private boolean AccessPermitted(String Owner,String passw) {
		return users.contains(new User(Owner, passw));
		
		/* EFFECTS: restituisce true se l'utente (Owner,passw) è contenuto nel vettore users, false altrimenti
		 */
	}
	
	
	public int getSize(String Owner, String passw) throws AccessDeniedException,NullPointerException {
		if(Owner==null) throw new NullPointerException();
		if(passw==null) throw new NullPointerException();
		if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
		int c =0;
		
		Iterator<StructureData> it = dati.iterator();
		
		while(it.hasNext()) {
			StructureData<E> curr = it.next();
			if(curr.IsPermitted(Owner)) c++;

		}
		return c;
		
		/* REQUIRES: Owner!=null && passw!=null && la coppia <Owner,passw> è presente nell'insieme degli utenti
		 * 
		 * THROWS: se la coppia <Owner,passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
		 * 		   se (Owner==null ||pass==null) lancia NullPointerException
		 * 
		 * EFFECTS: restituisce la cardinalità dell'insieme di coppie : { < users.get(k).getId() , dati.get(s).getData()> | forall (0 <= k < users.size() && 
	     * 		    0 <= s < dati.size()) => (users.get(k).getId().equals(Owner))
		 * 		
		 */
	
	}
	
	private boolean UserExists(String IdUser) {
		Iterator<User> it = users.iterator();
				
		while(it.hasNext()) {
			User curr = (User) it.next();
			if(curr.getId().equals(IdUser)) return true;
		}
		return false;
		
		/* EFFECTS: restituisce true se : exists i | 0 <= i < users.size() && users.get(i).getId().equals(IdUser),
		 * 			restituisce false altrimenti
		 */
	}
	
	public void share(String Owner, String passw, String Other, E data) throws AccessDeniedException , NullPointerException, UserNotFoundException,IllegalArgumentException,IllegalDataException{
		if(Owner==null) throw new NullPointerException();
		if(passw==null) throw new NullPointerException();
		if(data==null) throw new NullPointerException();
		if(Other==null) throw new NullPointerException();
		if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
		if(!UserExists(Other)) throw new UserNotFoundException();
		if(Owner.equals(Other)) throw new IllegalArgumentException();
		if(getStructureData(Other,data)==null) throw new IllegalArgumentException();
		
		StructureData<E> found = getStructureData(Owner, data);
		
		 if(found == null)
			 throw new IllegalDataException();
		 else found.addOther(Other);
		 
		    /* REQUIRES: Owner!=null && passw!=null && data!=null && Other !=null && la coppia <Owner,passw> è presente nell'insieme degli utenti
			 *			 && esiste la coppia <Owner , data> nell'insieme delle associazioni && 
			 *			 esiste la coppia <Other , users.get(p).getPassw()> , con users.get(p).getPassw() qualunque !=null, nell'insieme degli utenti && !Owner.equals(Other)
			 *			 && getStructureData(Other,data)!=null
			 *
			 * THROWS: se la coppia <Owner,passw> non è presente nell'insieme di tutti gli utenti allora lancia AccessDeniedException(checked) &&
			 * 		   se (Owner==null ||pass==null || data==null || Other==null) lancia NullPointerException &&
			 * 		   se non esiste la coppia <Owner , data> nell'insieme delle associazioni allora lancia IllegalDataException(checked) &&
			 * 		   se non esiste la coppia <Other , users.get(p).getPassw()> nell'insieme degli utenti lancia UserNotFoundException(checked) &&
			 * 		   se Owner.equals(Other) lancia IllegalArgumentException &&
			 *         se getStructureData(Other,data)==null lancia IllegalArgumentException
			 * 
			 * MODIFIES: modifica l'insieme delle associazioni
			 * 
			 * EFFECTS: aggiunge la coppia <Other,data> all'insieme delle associazioni
			 * 	
			 */
	}
	
	public E get(String Owner, String passw, E data) throws  NullPointerException,AccessDeniedException {
		if(Owner==null) throw new NullPointerException();
		if(passw==null) throw new NullPointerException();
		if(data==null) throw new NullPointerException();
		if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
		
		StructureData<E> found = getStructureData(Owner, data);
		
		if(found == null) 
			return null;
		else return found.getData();
		
		/* REQUIRES: Owner!=null && passw!=null && data!=null && la coppia <Owner , passw> è presente nell'insieme degli utenti
		 * 
		 * THROWS: se la coppia <Owner , passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
		 * 		   se (Owner==null ||pass==null ||data==null) lancia NullPointerException
		 * 
		 * EFFECTS: se nell'insieme delle associazioni esiste una coppia <Owner , dati.get(j).getData()> tale che dati.get(j).getData().equals(data) e se nell'insieme
		 * 			dei dati è presente data allora restitusce dati.get(j).getData() , null altrimenti
		 */
		
	}
	
	public boolean put(String Owner, String passw, E data) throws NullPointerException,AccessDeniedException{
		if(Owner==null) throw new NullPointerException();
		if(passw==null) throw new NullPointerException();
		if(data==null) throw new NullPointerException();
		if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
		
		return dati.add(new StructureData<E>(Owner, data));
		
		/* REQUIRES: Owner!=null && passw!=null && data!=null && la coppia <Owner , passw> è presente nell'insieme degli utenti
		 * 
		 * THROWS: se la coppia <Owner , passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
		 * 		   se (Owner==null ||pass==null ||data==null) lancia NullPointerException
		 * 
		 * MODIFIES: modifica l'insieme delle associazioni
		 * 
		 * EFFECTS: restituisce true se data è stato inserito correttamente , ovvero se è stata aggiunta sia una coppia <Owner,data>
		 * 			nell'insieme delle associazioni che data nell'insieme dei dati, restituisce false altrimenti
		 * 			
		 * 			
		 */
	}
	
	public void copy(String Owner, String passw, E data) throws NullPointerException,AccessDeniedException,IllegalArgumentException {
		if(Owner==null) throw new NullPointerException();
		if(passw==null) throw new NullPointerException();
		if(data==null) throw new NullPointerException();
		if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
		
		StructureData<E> copia = null;
		Iterator<StructureData> it = dati.iterator();
		
		while(it.hasNext()) {
			StructureData<E> curr = it.next();
			if(curr.IsPermitted(Owner)) {
				if(curr.getData().equals(data))	{
					copia = new StructureData<>(Owner, data);
					dati.add(copia);
					break;
				}
			}
		}
		
		if(copia == null) throw new IllegalArgumentException();
		
	   /* REQUIRES: Owner!=null && passw!=null && data!=null && la coppia <Owner , passw> è presente nell'insieme degli utenti
		*			 && esiste la coppia <Owner , data> nell'insieme delle associazioni
		* 
		* THROWS: se la coppia <Owner , passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
		* 		  se (Owner==null ||pass==null || data==null) lancia NullPointerException &&
		* 		  se non esiste la coppia <Owner , data> nell'insieme delle associazioni allora lancia IllegalArgumentException
		* 	
		* MODIFIES: modifica l'insieme dei dati e l'insieme delle associazioni
		* 
		* EFFECTS: aggiunge sia una nuova coppia <Owner , data> nell'insieme delle associazioni che una copia di data nell'insieme dei dati
		*/
		
	}
	public E remove(String Owner, String passw, E data) throws  NullPointerException,AccessDeniedException {
		if(Owner==null) throw new NullPointerException();
		if(passw==null) throw new NullPointerException();
		if(data==null) throw new NullPointerException();
		if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
		

		StructureData<E> found = getStructureData(Owner, data);
		
		 if(found == null)
			 return null;
		 else  if(dati.remove(found))
				 return found.getData();
		 else return null;
		 
		    /* REQUIRES: Owner!=null && passw!=null && data!=null && la coppia <Owner , passw> è presente nell'insieme degli utenti
			 * 
			 * THROWS: se la coppia <Owner , passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
			 * 		   se (Owner==null ||pass==null || data==null) lancia NullPointerException
			 * 
			 * MODIFIES: modifica l'insieme dei dati e l'insieme delle associazioni
			 * 
			 * EFFECTS:	se esiste la coppia <Owner,data> nell'insieme delle associazioni ed esiste il dato nell'insieme dei dati allora 
			 * 			rimuove sia questa coppia che il dato e lo restituisce, altrimenti restituisce null
			 * 			
			 */
	 }
	
	
	private StructureData<E> getStructureData(String Owner,E data) {
		Iterator<StructureData> it = dati.iterator();
		
		while(it.hasNext()) {
			StructureData<E> curr = it.next();
			if (curr.IsPermitted(Owner))
				if(curr.getData().equals(data))
					return curr;
		}
		return null;
		
		/*EFFECTS: restituisce data, se lo trovo e Owner può accedervi, altrimenti restituisce null
		 * 
		 */
	}
	

	public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException,AccessDeniedException {
		if(Owner==null) throw new NullPointerException();
		if(passw==null) throw new NullPointerException();
		if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
		
		Vector<E> datiOwner = new Vector<>();
		
		Iterator<StructureData> it = dati.iterator();
		
		while(it.hasNext()) {
			StructureData<E> curr = it.next();
			if(curr.IsPermitted(Owner)) 
				datiOwner.add((E)curr.getData());
			
		}
		return new Generator<E>(datiOwner);
		
		/* REQUIRES: Owner!=null && passw!=null && la coppia <Owner , passw> è presente nell'insieme degli utenti
		 * 
		 * THROWS: se la coppia <Owner , passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
		 * 		   se (Owner==null ||pass==null) lancia NullPointerException
		 * 
		 * EFFECTS: restituisce tutti gli oggetti appartenenti all'insieme: { dati.get(j).getData() | forall 0 <= j < dati.size() =>
		 * 			(<Owner , dati.get(j).getData()> appartiene all'insieme delle associazioni)
		 */
	
	}
	
	private static class Generator<T> implements Iterator<T>  {
		/* OVERVIEW: Tipo statico che definisce la struttura di un iteratore di dati appartenenti ad un utente
		 * 
		 * TYPICAL ELEMENTS: < {iteratorList_i } , i > dove forall i => (0 <= i < iteratorList)
		 */
		
		private Vector<T> iteratorList ;
		private int i;
		
		//costruttore
		public Generator(Vector<T> data) throws NullPointerException {
			if(data==null) throw new NullPointerException();
			i=0;
			iteratorList= new Vector<>(data);
			
			/* REQUIRES: data!=null 
			 * 
			 * THROWS: se data==null lancia NullPointerException
			 * 
			 * MODIFIES: this
			 * 
			 * EFFECTS: istanzia il vettore iteratorList
			 * 
			 */
		}
		
		@Override
		public boolean hasNext() {
			return i<iteratorList.size();
			
			/* EFFECTS: restituisce true se se l'indice i di iteratorList è minore della sua dimensione, cioè
			 * 		   se è presente un elemento in posizione i, restituisce false altrimenti
			 * 
			 */
		}

		@Override
		public T next() {
			T curr = iteratorList.get(i);
			i++;
			return curr;
			
			/* EFFECTS: restituisce l'elemento in posizione i e incrementa di 1 l'indice i
			 * 
			 */
		}
		
		public void remove () {
			throw new UnsupportedOperationException();
			
			/* THROWS: lancia UnsupportedOperationException
			 * 
			 */
		}
		
		
	}
	
	
}

