

import java.util.*;


public class MySecureDataContainerHashMap<E> implements SecureDataContainer<E> {
			/* OVERVIEW: Tipo modificabile che rappresenta una collezione di n oggetti di tipo <E>, ciascuno dei quali può
		  	 *		 	 essere accessibile da un insieme di m utenti.L'utente che inserisce il dato nella collezione ne diventa il "proprietario"
		  	 *		 	 e può dare poteri di condivisione,rimozione,scrittura e lettura ad altri utenti.
		     * 
			 * TYPICAL ELEMENTS: tripla < {insieme degli utenti}  , {insieme dei dati}  , {insieme delle associazioni} > nello specifico :
		     * 
		     * 					< { <Id_i,passw_i> | 0 <= i < m }, {data_j | 0 <= j < n} , { < Id_k, data_s> | 0 <= k < m && 0 <= s < n}> dove :
		     *					forall i,p | (0 <= i < p < m) => Id_i!=passw_p &&
		     * 					forall k,s | (0 <= k < m && 0 <= s < n) => (Id_k appartiene all'insieme degli utenti && 
		     * 					data_s appartiene all'insieme dei dati
		     * 
		     * Abstraction Function
		     * AF(c): < {<Id_i , passw_i> | 0 < = i < c.Utenti.keySet().size() && Id_i = c.Utenti.keySet().get(i) && passw_i = c.Utenti.get(Id_I)}, 
		     * 		    < {c.Dati.get(j) | 0 <= j < c.Dati.size()} , < Id_k , Data_t > | forall (0 < = k < c.Utenti.keySet().size()) => ( Id_k = c.Utenti.keySet().get(k)) &&
		  	 *			forall (0 < = t < c.DataMap.get(Id).size()) => (Data_t = c.DataMap.get(Id).get(t)) } >
		     *
		     *
		     * IR: Utenti!=null && DataMap!=null && Dati!=null && Utenti.keySet()!=null && Utenti.values()!=null &&
		     * 	   DataMap.keySet!=null && DataMap.values()!=null && (forall i | 0 <= i < Dati.size() => Dati.get(i)!=null) &&
		     *     forall i | 0 <= i < Utenti.keySet().size() => Utenti.keySet().get(i) != null && forall i | 0 <= i < Utenti.values().size() => Utenti.values().get(i) != null)	&&
		     *     forall i | 0 <= i < DataMap.keySet().size() => DataMap.keySet().get(i) != null) && forall i | 0 <= i < DataMap.keySet().size() => DataMap.values().get(i) != null) &&
		     *     DataMap.keySet.size()==Utenti.keySet.size() &&
			 * 	   DataMap.keySet.containsAll(Utenti.keySet) && 
			 * 	   Utenti.keySet.containsAll(DataMap.keySet) &&
			 * 	   Dati.contains(DataMap.values().get(i) forall 0 <= i < DataMap.values().size())
			 * 
			 *
			 */
		
			private HashMap<String,String > Utenti;
			private HashMap<String, Vector<E>> DataMap;
			private Vector<E> Dati;
		
		//costruttore
		public MySecureDataContainerHashMap() {
			Utenti= new HashMap<>();
			DataMap= new HashMap<>();
			Dati= new Vector<>();
			
			/* MODIFIES:this
			 * EFFECTS: istanzia a vuoti Utenti,DataMap,Dati
			 */
			
			
		}
		public void createUser(String Id, String passw) throws UserAlreadyExistsException, NullPointerException {
			if(Id==null) throw new NullPointerException();
			if(passw==null) throw new NullPointerException();
			if(UserExists(Id)) throw new UserAlreadyExistsException();
			
			Utenti.put(Id, passw);
			DataMap.put(Id, new Vector<>());
			
			
			/* REQUIRES: Id!=null && passw!=null && !UserExists(Id)
			 * 
			 * THROWS: se (Id==null || passw==null) lancia NullPointerException &&
			 * 		   se (UserExists(Id)) lancia UserAlreadyExists(checked)
			 * 
			 * MODIFIES: this
			 * 
			 * EFFECTS: crea e aggiunge un nuovo utente User(Id,passw) se e solo se l'utente non era già presente nel vettore Utenti
			 * 
			 */
		 
			
		}

		//controllo se l'utente esiste
		private boolean UserExists(String Id) {
			return Utenti.containsKey(Id);
			
			/* EFFECTS: restituisce true se l'utente Id è contenuto in Utenti, false altrimenti
			 * 
			 */
			
		}
		
		//controllo identità
		private boolean AccessPermitted(String Id,String passw) {
				if(UserExists(Id)) 
					return Utenti.get(Id).equals(passw);
				return false;
				
				/* EFFECTS: restituisce true se l'utente (Id,passw) è contenuto nel vettore Utenti, false altrimenti
				 */
				
		}
		
		public int getSize(String Owner, String passw) throws AccessDeniedException, NullPointerException {
			if(Owner==null) throw new NullPointerException();
			if(passw==null) throw new NullPointerException();
			if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
		
			return DataMap.get(Owner).size();
			
			/* REQUIRES: Owner!=null && passw!=null && la coppia <Owner,passw> è presente nell'insieme degli utenti
			 * 
			 * THROWS: se la coppia <Owner,passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
			 * 		   se (Owner==null ||pass==null) lancia NullPointerException
			 * 
			 * EFFECTS: restituisce la cardinalità dell'insieme di coppie : { < Owner, DataMap.get(Owner).get(k) > forall (0 <= k < DataMap.get(Owner).size()) }
			 * 			   
			 * 		
			 */

		
		}


		public boolean put(String Owner, String passw, E data) throws AccessDeniedException, NullPointerException {
			if(Owner==null) throw new NullPointerException();
			if(passw==null) throw new NullPointerException();
			if(data==null) throw new NullPointerException();
			if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
			
			if(!DataForAll(data)) return DataMap.get(Owner).add(data) && Dati.add(data);
			else return DataMap.get(Owner).add(data);
			

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
		
			
		public E get(String Owner, String passw, E data) throws NullPointerException, AccessDeniedException {
			if(Owner==null) throw new NullPointerException();
			if(passw==null) throw new NullPointerException();
			if(data==null) throw new NullPointerException();
			if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
			
			if(DataExists(Owner,data)) {
					int i= DataMap.get(Owner).indexOf(data);
					return DataMap.get(Owner).get(i);
			}
					
			return null;
			
			/* REQUIRES: Owner!=null && passw!=null && data!=null && la coppia <Owner , passw> è presente nell'insieme degli utenti
			 * 
			 * THROWS: se la coppia <Owner , passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
			 * 		   se (Owner==null ||pass==null ||data==null) lancia NullPointerException
			 * 
			 * EFFECTS: se nell'insieme delle associazioni esiste una coppia <Owner , DataMap.get(Owner).get(j)> tale che DataMap.get(Owner).get(j).equals(data) e se nell'insieme
			 * 			dei dati è presente data allora restitusce DataMap.get(Owner).get(j) , null altrimenti
			 */
			
		}
		
		//controllo se il dato esiste nella collezione di Owner
		private boolean DataExists(String Owner, E data) {
				return DataMap.get(Owner).contains(data);
				
				/* EFFECTS: restituisce true se data è contenuto nel vettore di dati posseduti da Owner, false altrimenti
				 * 
				 */
			
		}
		//controllo se il dato esiste in Dati
		private boolean DataForAll(E data) {
			return Dati.contains(data);
			
			/* EFFECTS: restituisce true se data è contenuto nel vettore Dati, false altrimenti
			 * 
			 */
		}
		
		

		public E remove(String Owner, String passw, E data) throws NullPointerException, AccessDeniedException {
			if(Owner==null) throw new NullPointerException();
			if(passw==null) throw new NullPointerException();
			if(data==null) throw new NullPointerException();
			if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
			
			if(DataExists(Owner,data)) {
				Dati.remove(data);
				return removeAll(data);
				
			}	
			return null;
			
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
		
		//rimuove il dato da tutte le collezioni degli utenti che lo possono vedere
		private E removeAll(E data) {
			int eliminati=0;
			 Set<String> users = DataMap.keySet();
			 Iterator<String> utenti = users.iterator();
			 Iterator<Vector<E>> it = DataMap.values().iterator();
			 
			 while(it.hasNext()) {
				   Vector<E> curr = it.next();
				 if(curr.contains(data)) {
					 while(utenti.hasNext()) {
						 String s = utenti.next();
						 if(DataMap.get(s).contains(data)) {
							 DataMap.get(s).remove(data);
							 eliminati++;
						 }
					 }
				 }
			 }
			 if(eliminati>0) return data;
			 else return null;
			 
			 /* MODIFIES: insieme delle associazioni
			  * 
			  * EFFECTS: cerca data in tutti i vettori di ciascun utente che può accedervi, se lo trova lo elimina. Se ne ha eliminato almeno uno restituisce il dato,
			  * 		 null altrimenti
			  */
			 
		}
		
		public void copy(String Owner, String passw, E data) throws NullPointerException, AccessDeniedException, IllegalArgumentException {
			if(Owner==null) throw new NullPointerException();
			if(passw==null) throw new NullPointerException();
			if(data==null) throw new NullPointerException();
			if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
			if(!DataExists(Owner,data)) throw new IllegalArgumentException();
			
			int i= DataMap.get(Owner).indexOf(data);
			E copia = DataMap.get(Owner).get(i);
			DataMap.get(Owner).add(copia);
			Dati.add(copia);
			
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

		
		public void share(String Owner, String passw, String Other, E data) throws AccessDeniedException, NullPointerException, UserNotFoundException, IllegalArgumentException, IllegalDataException {
			if(Owner==null) throw new NullPointerException();
			if(passw==null) throw new NullPointerException();
			if(data==null) throw new NullPointerException();
			if(Other==null) throw new NullPointerException();
			if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
			if(!UserExists(Other)) throw new UserNotFoundException();
			if(Owner.equals(Other)) throw new IllegalArgumentException();
			if(DataMap.get(Other).contains(data)) throw new IllegalArgumentException();
			if(!DataExists(Owner,data)) throw new IllegalDataException();
			
			int i= DataMap.get(Owner).indexOf(data);
			DataMap.get(Other).add(DataMap.get(Owner).get(i));
			
			  
			/* REQUIRES: Owner!=null && passw!=null && data!=null && Other !=null && la coppia <Owner,passw> è presente nell'insieme degli utenti
			 *			&& esiste la coppia <Owner , data> nell'insieme delle associazioni && 
			 *			esiste la coppia <Other , Utenti.get(Other)>  nell'insieme degli utenti && !Owner.equals(Other) &&
			 *			!DataMap.get(Other).contains(data)
			 *			
			 *			
			 * THROWS: se la coppia <Owner,passw> non è presente nell'insieme di tutti gli utenti allora lancia AccessDeniedException(checked) &&
			 * 		   se (Owner==null ||pass==null || data==null || Other==null) lancia NullPointerException &&
			 * 		   se non esiste la coppia <Owner , Utenti.get(Owner)> nell'insieme degli utenti lancia UserNotFoundException(checked) &&
			 * 		   se Owner.equals(Other) lancia IllegalArgumentException &&
			 * 		   se !DataExists(Owner,data) lancia IllegalDataException &&
			 * 		   se DataMap.get(Other).contains(data) lancia IllegalArgumentException
			 * 
			 * MODIFIES: modifica l'insieme delle associazioni
			 * 
			 * EFFECTS: aggiunge la coppia <Other,data> all'insieme delle associazioni
			 * 	
			 */
			
		}
		

		public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, AccessDeniedException {
			if(Owner==null) throw new NullPointerException();
			if(passw==null) throw new NullPointerException();
			if(!AccessPermitted(Owner, passw)) throw new AccessDeniedException();
			
			return new Generator<E>(DataMap.get(Owner));
			
			/* REQUIRES: Owner!=null && passw!=null && la coppia <Owner , passw> è presente nell'insieme degli utenti
			 * 
			 * THROWS: se la coppia <Owner , passw> non è presente nell'insieme degli utenti allora lancia AccessDeniedException(checked) &&
			 * 		   se (Owner==null ||pass==null) lancia NullPointerException
			 * 
			 * EFFECTS: restituisce tutti gli oggetti appartenenti all'insieme: { Dati.get(j) | forall 0 <= j < Dati.size() =>
			 * 			(<Owner , Dati.get(j)> appartiene all'insieme delle associazioni)
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

