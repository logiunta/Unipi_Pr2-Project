

import java.util.Iterator;

//test in cui si prova il funzionamento dei metodi
public class testMain {

	public static void main(String[] args) {
		SecureDataContainer<Integer> lista = new MySecureDataContainer<>();
		//SecureDataContainer<Integer> lista = new MySecureDataContainerHashMap<>();
		
		Integer dato1 = 10;
		Integer dato2 = 5;
		Integer dato3 = 2;
		String Id1 = "Francesco";
		String Id2 = "Marco";
		String passw1 = "123";
		String passw2 = "456";
		
		
		//creazione di Id1 che va a buon fine
		try {
			lista.createUser(Id1, passw1);
			System.out.println("Utente "+Id1+" creato correttamente");
		} 
		catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} catch (UserAlreadyExistsException e) {
			// TODO Auto-generated catch block
			System.out.println("Utente "+Id1+" già esistente");
		}
		
		//creazione di Id1 che non va a buon fine perchè già esistente
		//lancia UserAlreadyExistsException
		try {
			lista.createUser(Id1, passw1);
			System.out.println("Utente "+Id1+" creato correttamente");
		} 
		catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} catch (UserAlreadyExistsException e) {
			// TODO Auto-generated catch block
			System.out.println("Utente "+Id1+" già esistente");
		}
		
		//creazione di Id2 che va a buon fine
		try {
			lista.createUser(Id2, passw2);
			System.out.println("Utente "+Id2+" creato correttamente");
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
			
		} catch (UserAlreadyExistsException e) {
			// TODO Auto-generated catch block
			System.out.println("Utente "+Id2+" già esistente");
		}
		
		//aggiunta di un dato al collezione di Id1 che va a buon fine
		try {
			if(lista.put(Id1, passw1, dato1))
				System.out.println("Dato aggiunto correttamente alla collezione di "+Id1);
			else 
				System.out.println("Non è stato possibile aggiungere il Dato alla collezione di "+Id1);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		}
		
		//aggiunta di un dato al collezione di Id1 che non va a buon fine perchè l'username Id2 è sbagliato
		//lancia AccessDeniedException (eccezione da me creata)
		try {
			if(lista.put(Id2, passw1, dato1))
				System.out.println("Dato aggiunto correttamente alla collezione di "+Id1);
			else 
				System.out.println("Non è stato possibile aggiungere il Dato "+Id1);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		}
		
		//copia del dato che va a buon fine perchè il login viene eseguito correttamente e
		//il dato è presente nella collezione di Id1
		try {
			lista.copy(Id1, passw1, dato1);
			System.out.println("Dato copiato correttamente");
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println("Dato non trovato");
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		}
		
		//copia del dato che non va a buon fine perchè il login viene eseguito correttamente ma 
		//il dato non è presente nella collezione di Id1
		//lancia IllegalArgumentException
		try {
			lista.copy(Id1, passw1, dato3);
			System.out.println("Dato copiato correttamente");
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println("Dato non trovato");
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		}
		
		//condivisione del dato posseduto da Id1 con Id2 che va a buon fine, 
		//perchè le credenziali sono corrette, il dato esiste nella collezione di Id1 ma non era già stato condiviso 
		//in precedenza con Id2 ,e Id1 non sta condividendo con se stesso.
		try {
			lista.share(Id1, passw1, Id2, dato1);
			System.out.println("Dato correttamente condivisio con "+Id2);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println("Impossibile condivedere con "+Id2);
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Utente "+Id2+"non trovato");
		} catch (IllegalDataException e) {
			// TODO Auto-generated catch block
			System.out.println("Dato trovato");
		}
		
		//condivisione del dato posseduto da Id1 con Id2 che non va a buon fine, 
		//perchè le credenziali sono corrette, ma il dato non esiste nella collezione di Id1.
		//lancia IllegalDataException (eccezione da me creata)
		try {
			lista.share(Id1, passw1, Id2, dato3);
			System.out.println("Dato correttamente condivisio con "+Id2);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println("Impossibile condividere con "+Id2);
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Utente "+Id2+"non trovato");
		} catch (IllegalDataException e) {
			// TODO Auto-generated catch block
			System.out.println("Dato non trovato");
		}	
				
		//condivisione del dato posseduto da Id1 con Id2 che non va a buon fine, 
		//perchè le credenzialidi Id1 sono corrette, ma il dato era già stato condiviso in precedenza
		//lancia IllegalArgumentException
		try {
			lista.share(Id1, passw1, Id2, dato1);
			System.out.println("Dato correttamente condivisio con "+Id2);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println("Impossibile condividere");
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Utente Gianluca non trovato");
		} catch (IllegalDataException e) {
			// TODO Auto-generated catch block
			System.out.println("Dato non trovato");
		}
		
		//getSize restituisce il numero di dati contenuti nella collezione di Id1 correttamente perchè le credenziali sono esatte
		try {
			int c =lista.getSize(Id1,passw1);
			System.out.println(Id1+" ha accesso a "+c+" dati");
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		}
		
		//getSize non procede perchè la password è errata.
		//lancia AccessDeniedException
		try {
			int c =lista.getSize(Id1,passw2);
			System.out.println(Id1+" ha accesso a "+c+" dati");
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		}
		
		//restituisce correttamente una copia del dato voluto, poichè i controlli vanno a buon fine
		//e il dato è stato trovato
		try {
			Integer c = lista.get(Id1, passw1, dato1);
			if(c!=null)
				System.out.println(c);
			else
				System.out.println("Nessun Dato trovato");
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		}
		
		//restituisce correttamente un iteratore di tutti i dati accessibili da Id1 ,
		//poichè vengono superati i controlli di identità
		try {
			lista.getIterator(Id1, passw1);
			Iterator<Integer> it = lista.getIterator(Id1, passw1);
			System.out.print("I Dati di "+Id1+" : ");
			while(it.hasNext()) {
				Integer curr = it.next();
				System.out.print(curr+"  ");
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} 
		catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		}
		
		System.out.println();
		
		//getIterator non prosegue poichè i controlli non vengono superati
		//lancia AccessDeniedException
		try {
			lista.getIterator(Id2, passw1);
			Iterator<Integer> it = lista.getIterator(Id1, passw1);
			System.out.print("I Dati di "+Id1+" : ");
			while(it.hasNext()) {
				Integer curr = it.next();
				System.out.print(curr+"  ");
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Campo nullo");
		} 
		catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		}
		
		
		//eliminazione del dato1 che va a buon fine poichè il dato è visibile da Id1 e i controlli vengono superati
		try {
			Integer c = lista.remove(Id1, passw1, dato1);
			if(c!=null) 
				System.out.println("Dato rimosso: "+ c);
			else 
				System.out.println("Dato non rimosso");
			
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Errore: Campo nullo");
		}  catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		}
		
		//eliminazione del dato2 fallita, poichè il dato non è visibile da Id1
		try {
			Integer c = lista.remove(Id1, passw1, dato2);
			if(c!=null) 
				System.out.println("Dato rimosso: "+ c);
			else 
				System.out.println("Dato non rimosso");
			
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Errore: Campo nullo");
		}  catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			System.out.println("Accesso Negato");
		}
		
	
	}

}
