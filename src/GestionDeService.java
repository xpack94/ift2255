import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;


/**
 * 
 * @author abdesselam 
 * classe qui contient toutes le fonctionalité qui son propre au service 
 * 
 * @verison 3
 * 
 *
 */

public class GestionDeService {

	//la methode qui permet a un professionnel de donner un service 
			protected int DonnerService(long numeroUnique , CentreDeDonnes ctrDonne){
				/**
				 * methode qui permet a un professionnel donné de donner un service , en entrant toutes les informations 
				 * relative  a se service 
				 * @param numeroUnique du professionnel 
				 * @param ctrDonne qui est la base de donné ou le service crée va etre stocké 
				 * @Exception ParseException
				 * @return entier representant le code du service crée
				 */
				
				if(!new GestionDeMembre().verificationMembre(numeroUnique, ctrDonne)){
					//le professionnel n'est pas inscrit 
					//long number=this.ajouterMembre("professionnel");
					System.out.println("le numero n'est pas correct");
				}
				//cree le service avec les informations necessaire 
				ctrDonne.setServiceId(ctrDonne.getServiceId()+1);
				int serviceId= ctrDonne.getServiceId();
				String dateEtHeuresAct= new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(Calendar.getInstance().getTime());
				//String dateDebutService=new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
				Scanner sc=new Scanner(System.in);
				System.out.println("entrer la date du debut du service sous forme dd-MM-yyyy");
				String dateDebutService=sc.next();
				System.out.println("entrer la date fin du service sous forme dd-MM-yyyy");
				String dateFinService=sc.next();
				System.out.println("entrer l'heure du service sous forme HH:MM");
				String heureDuService=sc.next();
				System.out.println("entrer le nom du service (20 caractaires)");
				String nom=sc.next();
				System.out.println("entrer la reccurence hebdomadaire du service");
				sc.nextLine();
				String reccurenceHebdo=sc.nextLine();
				
				System.out.println("entrer un commentaire sur le service (au plus 100 caractaires)");
				String commentaire=sc.next();
				
				if(!this.isValidFormat(dateDebutService)){
					System.out.println("le format de date debut du service n'est pas valide");
					return 0;
				}
				if(!this.isValidFormat(dateFinService)){
					System.out.println("le format de date fin du service n'est pas valide");
					return 0;
				}
				if(!this.isValidHourFormat(heureDuService)){
					System.out.println("l'heure du service entrée n'est pas valide");
					return 0;
				}
				if(commentaire.length()>100){
					System.out.println("le commentaire a plus que 100 caractaire");
					return 0;
				}
				
				if(nom.length()>20){
					System.out.println("le nom contient plus que 20 caractaires");
					System.out.println("inscription echoué ");
				}
				
				//faire la comparaison des dates entrées par le professionnel
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				
				try {
					
					if(this.compareDatesByCompareTo(df, df.parse(dateEtHeuresAct.substring(0,10)), df.parse(dateDebutService)) ==1){
						System.out.println("la date du debut de service est deja passé et donc elle est invalid");
						return 0;
					}
					if(this.compareDatesByCompareTo(df, df.parse(dateEtHeuresAct.substring(0,10)), df.parse(dateFinService)) ==1){
						System.out.println("la date fin du service est deja passé et donc elle est invalid");
						return 0;
					}
					if(this.compareDatesByCompareTo(df, df.parse(dateDebutService), df.parse(dateFinService)) ==1){
						System.out.println("la date fin du service ne peut pas etre avant la date debut du service");
						return 0;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					System.out.println("erreur");
					e.printStackTrace();
				}
				
				String [] occurences=reccurenceHebdo.split(" ");
				Service s =new Service(dateEtHeuresAct,dateDebutService,dateFinService,heureDuService,nom,reccurenceHebdo,30,numeroUnique,serviceId,commentaire);
				ctrDonne.services.put( s.getCodeDuService(), s);
				//le nombre de seance depend des occurences hebdomadaire
				s.setNombreDeSeance(occurences.length);
				//generer les codes des seances correspondants
				genererCodeSeances(occurences ,s);
				
				//ajouter le service creé dans l'array codeDesServiceDonnés de la class professionnel
				Professionnel p =ctrDonne.professionnels.get(numeroUnique);
				ArrayList <Integer> list=p.getCodeDesServiceDonnés();
				list.add(serviceId);
				p.setCodeDesServiceDonnés(list);
				p.setNombreDeServiceDonné(p.getNombreDeServiceDonné()+1);
				
				return serviceId;
			}
			
			
			private void genererCodeSeances(String [] occurences ,Service s ){
				/**
				 * methode qui genere un code de seance a 7 chiffre tel que les 3 premier chiffre 
				 * represente le code du service , les 2 prochains chiffres represente le numero de la seance 
				 * et les 3 derniers chiffre represente les deux derniers chiffres du numero du professionnel
				 * @param occurences un tableau contenant les jours ou le service sera donné a la meme heure 
				 * @param s qui represente le service en question 
				 * @return void
				 */
				
				for(int i=0;i<occurences.length;i++){
					//generer le code de la seance de occurences[i]
					String numDuProfessionnel=String.valueOf(s.getNumeroDuProfessionnel());
					String deuxDerniersChiffres=numDuProfessionnel.substring(numDuProfessionnel.length()-2, numDuProfessionnel.length());
					String code= String.valueOf(s.getCodeDuService())+String.valueOf(s.getNumeroDeSeance())+deuxDerniersChiffres;
					int n=s.getNumeroDeSeance()+1;
					s.setNumeroDeSeance(n);
					int codeSeance=Integer.parseInt(code);
					s.setSeancesList(codeSeance, new Seance(codeSeance,occurences[i]));
					System.out.println("la seance du "+occurences[i]+" a le code "+codeSeance);
					s.setCodeDesSeance(codeSeance);
				}
				
			}
			
			
			protected void removeService(int codeDuService,CentreDeDonnes ctrDonne){
				Service s=ctrDonne.services.get(codeDuService);
				if(s!=null){
					ctrDonne.services.remove(codeDuService);
					System.out.println("le service a bien eté supprimé ");
				}else{
					System.out.println("le service n'existe pas ");
				}
				
				
				}
			
			
			
			
			
			public void metterAjourService(int n,CentreDeDonnes ctrDonne){
				/**
				 * methode qui mets a jour les information d'un service donné 
				 * @param n representant le numero du service 
				 * @param ctrDonne qui represente la base de donné
				 * @Exception ParseException
				 *  
				 * @return void 
				 */
				
				Service s=ctrDonne.services.get(n);
				if(s==null){
					System.out.println("le code du service est invalide");
					return;
				}
				System.out.println("que voulez-vous mettre a jour?");
				System.out.println("1:date fin du service");
				System.out.println("2:heure du service");
				System.out.println("3:recurrence hebdomadaire du service");
				System.out.println("4:frais du service");
				Scanner sc=new Scanner(System.in);
				
				
				int rep=Integer.parseInt(sc.next());
				switch (rep){
				case 1:
					System.out.println("veuillez entrer la nouvelle date de fin du service sous forme dd-MM-yyyy");
					String dateFin=sc.next();
					DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					if(!isValidFormat(dateFin)   ){
						System.out.println("la date que vous avez entrée ne correspond pas a la bonne forme");
						
					}else{
						
						String dateEtHeuresAct= new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(Calendar.getInstance().getTime());
						
							try {
								if(this.compareDatesByCompareTo(df, df.parse(s.getDateDebutService().substring(0,10)), df.parse(dateFin)) ==1
										|| this.compareDatesByCompareTo(df, df.parse(dateEtHeuresAct.substring(0,10)), df.parse(dateFin)) ==1){
									System.out.println("la nouvelle date n'est pas correcte");
								}else{
									s.setDateFinService(dateFin);
									System.out.println("la nouvelle date a eté mise a jour");
								}
								
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
					}
					break;
				case 2:
					System.out.println("veuillez entrer le nouveau heure du service sous forme HH:MM");
					String heure=sc.next();
					if(!isValidHourFormat(heure)){
						System.out.println("l'heure entrée ne correspond pas a la bonne forme");
						
					}else{
						s.setHeureDuService(heure);
						System.out.println("l'heure du service a eté mis a jour");
					}
					break;
				case 3:
					System.out.println("veuillez entrer la nouvelle reccurence hebdomadaire du service");
					String reccurence=sc.nextLine();
					s.setRecurrenceHebdo(reccurence);
					System.out.println("la reccurence hebdomadaire du service a eté mise a jour");
					
					break;
				
				case 4:
					System.out.println("veuillez-entrer la nouvelle somme des frais du service (mois que 100$)");
					String frais=sc.next();
					if(Double.parseDouble(frais) >100){
						System.out.println("les frais ne doivent pas depasser 100$");
					}
				default:
					break;
				}
				
			}
			
			
			public long sinscrirAService(long numeroUnique,CentreDeDonnes ctrDonne){
				/**
				 * methode permetant a un membre de s'inscrir a une seance choisit dans le repertoire des services
				 * @param numeroUnique du membre qui veut s'inscrir a la seance 
				 * @param ctrDonne qui represente la base de donné ou tout les services sont stocké 
				 * @return long qui represente le numero a 7 chiffre de la seance 
				 */
				
				if(new GestionDeMembre(). verificationMembre(numeroUnique,ctrDonne)){
					
				
				//s'inscrir au dernier service disponible 
			    int nombreDeServiceDispo=this.afficherToutLesServices(ctrDonne);
			    if(nombreDeServiceDispo==0){
			    	System.out.println("aucun service n'est disponible pour aujourdh'ui");
			    	return 0;
			    }
				Scanner sc =new Scanner(System.in);
				System.out.println("entrer le numero du service choisit");
				int num=sc.nextInt();
				int codeDuServiceChoisit=Integer.parseInt( String.valueOf(num).substring(0, 3));
				Service service=ctrDonne.services.get(codeDuServiceChoisit);
				Seance s=service.getSeancesList().get(num);
				if(service==null){
					System.out.println("le numero du service entrée est invalide");
					return 0;
				}
				//tester si la capacité maximale n'est pas atteinte
				while(s.getIndex()==31){
					System.out.println("il y'a plus de place disponible pour s'inscrir a la seance");
					System.out.println("essayer une autre seance!");
					this.afficherToutLesServices(ctrDonne);
					System.out.println("entrer le numero de la seance choisit");
					int number=sc.nextInt();
					codeDuServiceChoisit=Integer.parseInt( String.valueOf(number).substring(0, 3));
					service=ctrDonne.services.get(codeDuServiceChoisit);
					
				}
				//verifier si le membre n'est pas suspendu
				//verifier si le numero nest pas celui du professionnel
				if(service.getNumeroDuProfessionnel()==numeroUnique){
					System.out.println("vous ne pouvez pas vous inscrir a votre propre service");
				}else if(( (MembreRegulier)ctrDonne.membres.get(numeroUnique)).getEtat().equals("suspendu")){
					System.out.println("vous ne pouvez pas vous inscrir car vous etes suspendu");
					
				}else if(!verifierSiDansService(s,numeroUnique)){
					String dateEtHeuresAct= new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(Calendar.getInstance().getTime());
					//generer le code de la seance a 7 chiffres 
					
					MembreInscrit memberInscrit=new MembreInscrit(dateEtHeuresAct, service.getDateDebutService()
							, service.getNumeroDuProfessionnel(),numeroUnique , service.getCodeDuService(),"");
					
					//ajouter les information relatif a cette senace dans le ArrayList du centre de donnée
					//service.setSeances(seance);		
					s.setMembreInscrit(memberInscrit);
					//ajouter le code de la seance dans la listes des seance du membre 
					Membre m=ctrDonne.membres.get(numeroUnique);
					if(m!=null){
						m.setCodeDesSeancesInscirit(num);
					}
					
					System.out.println("vous etes maintenant inscrit");
					return service.getCodeDuService();
				}else{
					System.out.println("vous etes deja inscrit dans ce service ");
				}
				
				}else{
					System.out.println("le numero entrée est invalide");
				}
				
				return 0000000;
			}
			
			//afficher tous les service disponible pour aujourd'hui 
			private int afficherToutLesServices(CentreDeDonnes ctrDonne){
				/**
				 * methode qui permet d'afficher tout les service qui sont donné aujourdh'ui 
				 * @param ctrDonne qui represente la base de donné qui contient tout les services 
				 * @Exception ParseException
				 * @return int representant le nombre de service trouvé 
				 */
				
				int g=101;
				int nombreDeServiceDispo=0;
				//obtenir le jour d'aujourdh'ui 
				String jour= this.getCurrentDay();
				Service s=ctrDonne.services.get(g);
				System.out.println("les services disponible pour aujourd'hui sont : ");
				while(s!=null){
					
					//afficher tout les service dispobible pour aujourd'hui
					int codeDuServiceDispo=this.seDonneAujourdhui(s,jour);
					
					String dateEtHeuresAct= new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(Calendar.getInstance().getTime());
					DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					try {
						if(codeDuServiceDispo!=0 && (this.compareDatesByCompareTo(df, df.parse(dateEtHeuresAct), df.parse( s.getDateDebutService()))!=-1 )
								&& (this.compareDatesByCompareTo(df, df.parse(dateEtHeuresAct), df.parse( s.getDateFinService()))==-1 )){
							nombreDeServiceDispo++;
							System.out.println("seance numero"+codeDuServiceDispo+"du service "+s.getCodeDuService()+" avec le professionnel " +s.getNumeroDuProfessionnel());
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					s=ctrDonne.services.get(++g);
				}
				return nombreDeServiceDispo;
			}
			
			
			
			
			public  int compareDatesByCompareTo(DateFormat df, java.util.Date oldDate, java.util.Date newDate) {
				/**
				 * methose qui permet de faire la comparaison de deux dates 
				 * @param df de type DateFormat 
				 * @param oldDate qui represente la 1ere date a comparér 
				 * @param newDate qui represente la deuxiemme date a comparér 
				 * @return int qui est 1 si oldDate > newDate , 0 si il sont egaux et -1 si newDate >oldDate
				 * 
				 */
		        
		        if (oldDate.compareTo(newDate) == 0) {  
		            return 0;
		        }
		        if (oldDate.compareTo(newDate) < 0) {
		          
		            return -1;
		        }
		        
		        return 1;
		    }
			
			
			//verifie si un service se donne a une journée donnée
			private  int seDonneAujourdhui(Service s,String jour){
				/**
				 * verifie si un service est donné aujourdh'ui 
				 * @param s qui represente le service en question 
				 * @param jour represente le jour avec lequel on compare 
				 * @return un entier qui le code de la seance ou 0 par defaut si la seance ne se donne pas au jour donné
				 * 
				 * 
				 */
				
				ArrayList<Integer> seanceCodes=s.getCodeDesSeance();
				for(int i =0; i<seanceCodes.size();i++){
					Seance seance=s.getSeancesList().get(seanceCodes.get(i));
					if(seance.getJour().equals(jour)){
						return seanceCodes.get(i);
					}
				}
				return 0;
			}
			
			
			
			
			//methode qui permet au professionnel de verifier et confirmer la presence d'un membre a la seance 
			
			public void confirmationPresence(CentreDeDonnes ctrDonne,long num){
				/**
				 * permet au professionnel de verifier et comfirmer la presence d'un membre 
				 * @param ctrDonne qui represente la base de donné 
				 * @param num qui represente le numero du membre a verifier 
				 * @return void
				 */
				
				int codeDeSeance=this.chercherCodeDeSeance(num, ctrDonne);
				if(codeDeSeance!=0){
					//la seance a ete trouvé 
					System.out.println("entrer le numero unique du membre a verifier  ");
					Scanner sc=new Scanner(System.in);
					long numeroUniqueMembre =sc.nextLong();
					int numeroDuService=Integer.parseInt(String.valueOf(codeDeSeance).substring(0, 3));
					Seance s=ctrDonne.services.get(numeroDuService).getSeancesList().get(codeDeSeance);
					MembreInscrit[]  m=s.getMembreInscrit();
					for(int i=0;i<m.length;i++){
						//verifier que le membre est dans la seance
						if(m[i].getNumero_du_membre()==numeroUniqueMembre){
							System.out.println("validé");
							Membre member=ctrDonne.membres.get(numeroUniqueMembre);
							if(member !=null){
								String dateEtHeuresAct= new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(Calendar.getInstance().getTime());
								member.setPresence(new Presence(dateEtHeuresAct, num, numeroUniqueMembre, codeDeSeance, "present"));
							}
							return;
						}
					}
					System.out.println("vous n'etes pas inscrit");
					
					
				}
				
				

				
			}
			
			private int chercherCodeDeSeance(long numeroUnique,CentreDeDonnes  ctr){
				/**
				 * permet de chercher le code d'une seance donné par un professionnel
				 * @param numeroUnique representant le code du professionnel pour lequel on cherche le code de la seance
				 * @param ctr qui represente le centre de donné
				 * @return int qui reprente le code de la seance trouvé 
				 */
				
			Professionnel p=ctr.professionnels.get(numeroUnique);
			//chercher dans tout les services donnés 
			ArrayList<Integer> codes=p.getCodeDesServiceDonnés();
			for(int i=0;i<codes.size();i++){
				Service s=ctr.services.get(codes.get(i));
				ArrayList<Integer> codeDesSeances=s.getCodeDesSeance();
				for(int j=0;j<codeDesSeances.size();j++ ){
					Seance seance=s.getSeancesList().get(codeDesSeances.get(j));
					String jour=this.getCurrentDay();
					//verifier si le jour d'aujourdh'ui et le meme jour 
					//ou la seance est donnée 
					if(jour.equals(seance.getJour())){
						return codeDesSeances.get(j);
					}
				}
			}
			//valeur par defaut 
			return 0;
			}
			
			

			private String getCurrentDay(){
				/**
				 * permet d'avoir le jour d'aujourdui 
				 * @return String correspondant au jour
				 */
				
				Calendar today = Calendar.getInstance();
				int day =today.get(Calendar.DAY_OF_WEEK);
				switch (day) {
			    case Calendar.SUNDAY:
			    	return "dimanche";

			    case Calendar.MONDAY:
			        return "lundi";

			    case Calendar.TUESDAY:
			        return "mardi";
			    case Calendar.WEDNESDAY:
			    	return "mercredi";
			    case Calendar.THURSDAY:
			    	return "jeudi";
			    case Calendar.FRIDAY:
			    	return "vendredi";
			    case Calendar.SATURDAY:
			    	return "samedi";
			}
				return "defaut";
			}
			private boolean verifierSiDansService(Seance  s,long num){
				/**
				 * methode qui verifie si un membre existe dans une seance 
				 * @param s qui represente la seance 
				 * @param num correspondant au numero du membre 
				 * @retuen boolean
				 */
				for(int i=0;i<s.getIndex();i++){
					if(s.getMembreInscrit()[i].getNumero_du_membre()==num){
						return true;
					}
				}
				return false;
			}
			
		   //methode qui verifie si l'heure du service est valide
			private static boolean isValidHourFormat(String value){
				/**
				 * permet de verifier si un horraire est dans la bonne forme 
				 * @param value qui represente l'horraire pour lequel on fait la verification
				 * @return boolean
				 */
				if(value.matches("([0-9]{2}):([0-9]{2})")){
					return true;
				}
				return false;
			}
		
			
			//methode qui verifie si la date entrée par l'utilisateur correspond au format voulu
			private static boolean isValidFormat( String value) {
				/**methode qui verifie si la date entrée par l'utilisateur correspond au format voulu
				 * @param value representant la date qu'on verifier 
				 * @return boolean
				 * 
				 */
				 boolean checkFormat;

				 if (value.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})")){
					 return true;
				 }
				   
				 
				 return false;
			    }
	
}
