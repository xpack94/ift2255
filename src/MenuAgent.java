import java.util.Scanner;

/**
 * 
 * @author abdesselam
 * classe representant le gui de l'agent quand il se connect sur son ordinateur 
 * @version 1
 *
 */

public class MenuAgent {

	
	public void choixPris(Main m,Logiciel a,int choix){
		/**
		 * permet de gerer le choix que l'utilisateur a pris 
		 * @param m qui repreente la class Main 
		 * @param a reprsente la class Logiciel contenant toutes les fonctionnalité @see java.GYM.Logiciel
		 * @Exception InterruptedException
		 * @return void  
		 */
		Scanner sc =new Scanner(System.in);
		switch (choix){
		case 1:
			    System.out.println("choisiser une des options");
			    System.out.println("etes-vous?");
			    System.out.println("1: membre regulier");
			    System.out.println("2: un professionnel");
			    String choix1=sc.next();
			    if(choix1.equals("1")){
			    	long n=a.ajouterMembre("membreRegulier");
			    	if(n==0){
			    		System.out.println("l'inscription est annulé car les frais d'adhesion ne sont pas payés");
			    	}else{
			    		System.out.println(" vous etes maintenant inscrit votre numero est: "+n);
			    	}
			    	
			    }else{
			    	System.out.println(" vous etes maintenant inscrit votre numero est: "+a.ajouterMembre("professionnel"));
			    }
			    
			
			break;

		case 2:
			long id=0;
			System.out.println("etes vous inscrit? oui/Non");
			String reponse =sc.nextLine();
			if((reponse.equals("Non")) ||( reponse.equals("n")) || (reponse.equals("non"))){
			 //le membre n'est pas inscrit
			 id=a.ajouterMembre("professionnel");
			 System.out.println("vous etes maintenant inscrit votre numero est :"+id);
			}else{
				System.out.println("veuillez entrer votre numero");
				id=sc.nextLong();
			}
			if(id==0){
				break;
			}
			int codeS=a.DonnerService(id);
			if(codeS==0){
				System.out.println("le service n'a pas eté crée");
			}else{
				System.out.println("le service a ete cree avec le code "+codeS);
			}
			
			
			break;


		case 3:
			System.out.println("entrer votre numero ");
			long numero=sc.nextLong();
			a.supprimerMembre(numero);
			System.out.println("le membre est supprimé");
			break;
		case 4:
			System.out.println("entrer le code du service");
			a.removeService(sc.nextInt());
			break;
		case 5:
			System.out.println("le fichier tef a eté crée dans votre espace de travail");
			a.procedureComptable();
			break;
		
		case 6:
			
			m.faireChoix(m,a);
			break;
			
		}
		try        
		{
		    Thread.sleep(4000);
		} 
		catch(InterruptedException ex) 
		{
		    Thread.currentThread().interrupt();
		}
		
		faireChoix(m,a);
		
	}
	
	
	
	
	
	public void faireChoix(Main m,Logiciel a){
		/**
		 * methode affichant toutes les actions qu'un agent peut faire 
		 * @param m qui represente la Classe Main @see java.GYM.Main
		 * @param a qui represente la classe Logiciel @see.GYM.Logicel
		 * @return void
		 */
		System.out.println("bienvenue");
		System.out.println("1:inscripton");
		System.out.println("2:donner service");
		System.out.println("3:supprimer membre");
		System.out.println("4:supprimer service ");
		System.out.println("5:procedure comptable");
		System.out.println("6:se deconnecter");
		
		Scanner sc =new Scanner(System.in);
		int choix=sc.nextInt();
		this.choixPris(m,a, choix);
	}
	
	
}
