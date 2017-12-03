import java.util.Scanner;


public class MenuDeProfessionnel {

	Logiciel log;
	public MenuDeProfessionnel(Logiciel log){
		this.log=log;
	}
	
	public void afficherMenu(Membre m){
		System.out.println("bienvenue a Gym "+m.getNom());
		System.out.println("veuillez choisir une option ");
		System.out.println("1:consulter les inscriptions au séances");
		System.out.println("2:modification des informations");
		Scanner sc=new Scanner(System.in);
		this.choisirOption(m, Integer.parseInt(sc.next()));
	}
	public void choisirOption(Membre m,int option){
		Scanner read=new Scanner(System.in);
		switch(option){
		case 1:
			System.out.println("entrer le code du service");
			log.consulterSeance(Integer.parseInt(read.next()));
			break;
		}
		
		
	}
	
}
