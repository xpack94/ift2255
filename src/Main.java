import java.util.ArrayList;
import java.util.Scanner;



public class Main {

	/**
	 * @param args
	 */
	
	public void choixPris(Main m,Logiciel a,int choix){
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
			
			System.out.println("veuillez entrer votre numero");
			long num = sc.nextLong();
			a.sinscrirAService(num);
			
			break;
		case 3:
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
			int codeS=a.DonnerService(id);
			if(codeS==0){
				System.out.println("le service n'a pas eté crée");
			}else{
				System.out.println("le service a ete cree avec le code "+codeS);
			}
			
			
			break;
		case 4:
			System.out.println("entrer le code du service que vous voulez consulter");
			int code=sc.nextInt();
			a.consulterSeance(code);
			
			
			break;
		case 5:
			System.out.println("veuillez entrer le code ");
			int codeSeance=sc.nextInt();
			System.out.println("veuillez entrer votre numero");
			long numeroUnique=sc.nextLong();
			
			break;
		case 6:
			System.out.println("entrer votre numero ");
			long numero=sc.nextLong();
			a.supprimerMembre(numero);
			System.out.println("le membre est supprimé");
			break;
		case 7:
			System.out.println("entrer votre numero");
			long newNum=sc.nextLong();
			a.mettreAjourMembre(newNum);
			break;
		case 8:
			System.out.println("entrer le code du service");
			a.removeService(sc.nextInt());
			break;
		case 9:
			System.out.println("entrer le code du service");
			a.metterAjourService(sc.nextInt());
			break;
		case 10:
			System.out.println("le fichier tef a eté crée dans votre espace de travail");
			a.procedureComptable();
			break;

		case 11:
			System.out.println("entrer votre email");
			String email =sc.next();
			Membre member=a.verifierEmail(email);
			if(member!=null) {
				System.out.println("connecté");
				new MenuMembreRegulier(a).afficherMenu(member);
			}else{
				System.out.println("l'email est invalide");
			}
			break;
		case 12:
			System.out.println("entrer votre email");
			String email1=sc.next();
			Professionnel mem=a.verifierEmailDeProfessionnel(email1);
			if(mem!=null){
				System.out.println("connecté");
				new MenuDeProfessionnel(a).afficherMenu(mem);
			}else{
				System.out.println("l'email est invalide");
			}
			break;
		
		case 13:
			System.exit(0);
			
		}
		try        
		{
		    Thread.sleep(4000);
		} 
		catch(InterruptedException ex) 
		{
		    Thread.currentThread().interrupt();
		}
		faireChoix(m, a);
		
	}
	
	public void faireChoix(Main m, Logiciel a){
		
		
		
		

		
		System.out.println("Bienvenu a GYM");
		System.out.println("que voulez-vous faire?");
		System.out.println("1:inscripton");
		System.out.println("2:s'inscrire a une séance");
		System.out.println("3:donner service");
		System.out.println("4:consulter inscription");
		System.out.println("5:confirmer presence");
		System.out.println("6:supprimer membre");
		System.out.println("7:mettre a jour membre");
		System.out.println("8:supprimer service ");
		System.out.println("9:mettre a jour service");
		System.out.println("10:procedure comptable");
		System.out.println("11:menu membre");
		System.out.println("12:menu professionnel");
		System.out.println("13:quitter");
		Scanner sc =new Scanner(System.in);
		int choix=sc.nextInt();
		m.choixPris(m,a, choix);
	}
	
	public static void main(String[] args) {
		
		Presence p=new Presence("07-12-2017 ", 100000001, 100000000, 1011101, "present");
		Presence p1=new Presence("08-12-2017 ", 100000001, 100000000, 1011101, "present");
		ArrayList<Presence> t= new ArrayList<Presence>();
		t.add(p);
		t.add(p1);
		Sorting s=new Sorting();
		s.quickSort(0, t.size());
		System.out.println(s.array.get(0).getDateEtHeuresActueles());
		System.out.println(s.array.get(1).getDateEtHeuresActueles());
		
		Main m =new Main();
		Logiciel a =new Logiciel(new CentreDeDonnes());
		a.runTask();
		m.faireChoix(m, a);
		
		
		
		
		
		
		

	}

}
