package com.mugen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JOptionPane;


public class automate {
	
	
	
	boolean boucle = false ;
	
	private static String mots_a_reconnaitre = "" ;
	ArrayList<Character> tableau_etats = new ArrayList<>();
	ArrayList<Character> tableau_alpha = new ArrayList<>();
	ArrayList<Integer> etat_finaux = new ArrayList<>();
	ArrayList<Integer> etat_inital = new ArrayList<>();
	//Tableau de ArrayList
	ArrayList<Character>[] ListeAutomate;
	static String tabAutomate[][];
	String chemin ;


	/**
	 * This function is used to clean the state line in the file
	 * 
	 * @param txt : 
	 * @return : #ArrayList<>()
	 */
	
	public static ArrayList cleanAlpha(String txt)
	{
		String data = txt ; 
		
		ArrayList<Character> etats = new ArrayList();
		
		for(int i = 0 ; i < txt.length() ; i++)
		{
			char element = data.charAt(i) ;
			//System.out.println(element);
			etats.add(element);
		}
		
		// clean list : erase characters like (, ] [)
		for(int i = 0 ; i < etats.size() ; i++)
		{
			if(etats.get(i).equals(',') || etats.get(i).equals(']') || etats.get(i).equals('['))
			{
				etats.remove(i);
			}
		}
		
		return etats;
		
	
	}
	
	/**
	 * This function is used to clean the alphabet line in the file
	 * @param txt
	 * @return #ArrayList<>()
	 */
	
	public static ArrayList cleanNumerique(String txt)
	{
		String data = txt ;
		
		ArrayList<Integer> alpha = new ArrayList<Integer>();
		
		for(int i = 0 ; i < data.length() ; i++)
		{
			alpha.add(Character.getNumericValue(data.charAt(i)));
		}
		
		// clean list : erase characters like (, ] [)
		for(int i = 0 ; i < alpha.size() ; i++)
		{
			if(alpha.get(i).equals(']') || alpha.get(i).equals('[') || alpha.get(i).equals(',') || alpha.get(i).equals(-1))
			{
				alpha.remove(i);
			}
		}
		
		return alpha;	
	}
	
	// Partie 2 : Methode de transition :
	/***
	 * 
	 * @param etat
	 * @param caract
	 * @param automate
	 * This function allows us to know towards which state we can go with a character given in parameter
	 */
	
	public static ArrayList<Integer> transitionEtat(int etat , char caract ) {
		
		ArrayList<Character> listeTransition = new ArrayList<Character>();
		
		int ArrayLength = tabAutomate[etat].length;
		
		//System.out.println(ArrayLength);
		
		for(int i = 0 ; i < ArrayLength ; i++) {
			listeTransition.add(tabAutomate[etat][i].charAt(0));
		}
		
		//System.out.println(listeTransition);
		
		// Now in Transition list, we have all the states accessible via our state given in parameter
		
		ArrayList<Integer> NextEtats = new ArrayList<Integer>();
		
		for(int i = 0 ; i < ArrayLength ; i++) {
			if(listeTransition.get(i).equals(caract)) {
				NextEtats.add(i);
			}
		}
		
		return NextEtats;

	}
	
	// estReconnue is :  Functions that will allow us to recognize the words 
	
	public void estReconnue(String mots) {
		
		      ArrayList<Integer> states = new ArrayList<>();
		      ArrayList<Integer> validStates = etat_inital;
		      ArrayList<Integer> mbStates = new ArrayList<>();
		      
		      	int t = mots.length();
		      	char l;
		      	String sub;
				
				
		      for (int i = 0; i < t ; i++){   
		          l = mots.charAt(i);
		          // On affecte toutes char dans la variable l 
		          
		          if (tableau_alpha.indexOf(Character.valueOf(l)) < 0) {
		        	  // On s'assure que les chars du mots correspondent a tableau_alpha
		              break;
		          }
		          
		          states = validStates;
		          System.out.println("Etat Actuel : "+ states);
		          
		          for (Integer state : states){
		        	  
						sub = i == 0 ? mots : mots.substring(i); 
						// Ici on reduit le mots a chaque transition reussie
						System.out.println("Entrer : " + state.intValue() + "|" + sub + "\t");
		
						if (ListeAutomate[state.intValue()].contains(Character.valueOf(l))){
						
						// Si la premiere ligne(etape) de notre matrice contient le premier char de notre mots
						  
		                  mbStates = transitionEtat(state.intValue() , l);
		                  
							System.out.println("sortie :" + mbStates + " | " + mots.substring(i+1));
							
		                  if (mbStates.size()==0){
		                	  // Si il n'ya pas de transition possible 
		                      continue;
		                  }
		                  
		                  else if( (i+1) == t ){
		                	  // Si on se trouve a l'avant derniere position du char 
		                	  
		                      for (Integer mb : mbStates){
		
		                          if(etat_finaux.contains(mb)){   
		                        	  	// Si toutes les transition marche et qu'on arrive au dernier char :
		                        	  	// On verifie si c'est un etat finale
		                        	  	System.out.println();
										System.out.println("Le mots est reconnue par l'automate".toString());
									}else
										//continue;
										System.out.println("Le mots n'est pas reconnue");
		                      }
		                  
		                  }else
							  // Si la transition est possible : On affecte valideStates : les etats possible pour le prochains deplacement 	
		                      validStates = mbStates;
		              }
						
		          }
		          
					System.out.println();
		      }
	}
	
	
	public automate(String chemin) {
		
		// TODO Auto-generated constructor stub
		
			this.chemin = chemin ;

				File automate = new File(chemin);
				
				if(!automate.exists())
				{
					System.out.println("L'automate n'existe pas");
					
					try {
						automate.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Fichier creer");
				}
				
				// If the file exists : start operation
				
				else
				{
					System.out.println("=> L'automate est present");
					System.out.println();	
					
					int comptLine = 1 ; String etats = "";
					int comptAlpha = 1 ; String alpha = "" ;
					int comptFinaux = 1 ; String finaux = "";
					int comptInitial = 1 ; String iniial = "";
			
					
					
					try {
						
						Scanner line = new Scanner(automate);
						Scanner alphabets = new Scanner(automate);
						Scanner firstToEndState = new Scanner(automate);
						Scanner initState = new Scanner(automate);
						
						
						// Select (line) the list of states
						while(line.hasNextLine())
						{
							if(comptLine == 3)
								break ;
							
							etats = line.nextLine();
							comptLine++;
						}
						
						// Alphabet (line) selections
						while(alphabets.hasNextLine())
						{
							if(comptAlpha == 2)
								break;
							alpha = alphabets.nextLine();
							comptAlpha++;
						}
						
						while(firstToEndState.hasNextLine()) {
							if(comptFinaux == 5) 
								break ;
							finaux = firstToEndState.nextLine();
							comptFinaux++;
					
						}
						
						while(initState.hasNextLine()) {
							if(comptInitial == 4)
								break;
							iniial = initState.nextLine();
							comptInitial++ ;
						}
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					System.out.println("Les etats de l'automate : " + etats);
					System.out.println("L'alphabet de l'automate : " + alpha);
					System.out.println("Les etats finaux de l'automate : " + finaux);
					System.out.println("L'etat finaux de l'automate : " + iniial);
					
					// TODO : Remplir nos arrays 
					tableau_etats = cleanNumerique(etats);
					tableau_alpha = cleanAlpha(alpha);
					etat_inital = cleanNumerique(iniial);
					etat_finaux = cleanNumerique(finaux);
				
					
					System.out.println(tableau_alpha);
					System.out.println(tableau_etats);
					System.out.println(etat_finaux);
					System.out.println(etat_inital);
					
					// Create matrix : la taille du la matrice est a la taille des sommets
					int n = tableau_etats.size();
					tabAutomate = new String[n][n];
					ListeAutomate = new ArrayList[n];
					
					
					// initialize the matrix :
					
					for(int i = 0 ; i < n ; i++)
					{
						for(int j = 0 ; j < n ; j++)
						{
							tabAutomate[i][j] = "*";
						}
					}
					
					// Display matrix :
					System.out.println();
					System.out.println("=> Representation matricielle d'un automate avant les transition");
					System.out.println();
					
					for (int i = 0; i < n; i++) {
						for (int j = 0; j < n; j++) {
							
							System.out.print(tabAutomate[i][j]+"\t");
						}	
						System.out.println();
						System.out.println();
					}
					
					// Creation of a list that will store the information of a file
					
					ArrayList<String> phrases = new ArrayList<>();
					
					try
					{
						// Recuperation de la ligne transition : 
						
						Scanner transitions = new Scanner(automate);
						int cmp = 1 ; String element = "";
						
						while(transitions.hasNextLine())
						{
							element = transitions.nextLine();				
							phrases.add(element);
							cmp++;
						}
						
					}catch(FileNotFoundException e)
					{
						e.printStackTrace();
					}
			
					// Supprimer les 4 premiere lignes du fichier 
					
					int x = 0 ;
					
					while(x < 4)
					{
						phrases.remove(0);
						x++;
					}
					
					// Transition function
					
					System.out.println("=> L'ensemble des transitions effectuer");
					System.out.println();
					
					for(String tmp : phrases)
					{
						System.out.println("index : {" + tmp.charAt(1) + "}{" + tmp.charAt(5) + "} = " + tmp.charAt(3));
						
						int i ; int j ;
						
						i = Character.getNumericValue(tmp.charAt(1));
						j = Character.getNumericValue(tmp.charAt(5));
						
						/**
						 * 1 etape : On a initialiser une matrice 
						 * 2 etape : On change les valeurs
						 */
						tabAutomate[i][j] = String.valueOf(tmp.charAt(3));
						
					}
					
					//Mettre chaque ligne de la matrice dans une arrayList qui est contenue dans tableau 

					for(int i = 0 ; i < n ; i++) {
						ListeAutomate[i] = new ArrayList<>();
						for(int j = 0 ; j < n ; j++) {
							ListeAutomate[i].add(tabAutomate[i][j].charAt(0));
						}
					}
					
					System.out.println();
					System.out.println("=> Representation Graphique de l'automate");
					System.out.println();
					
					for (int i = 0; i < n; i++) {
						for (int j = 0; j < n; j++) {
							
							System.out.print(tabAutomate[i][j]+"\t");
						}	
						System.out.println();
						System.out.println();
					}
					
					
					// deuxieme partie du travail : Reconnaissance du mots
					
					mots_a_reconnaitre = JOptionPane.showInputDialog("Entre le mots : ?").toString();
					System.out.println("Le mots reconnaitre : " + mots_a_reconnaitre);
					System.out.println();
					estReconnue(mots_a_reconnaitre);
			
				}	
				
			}								
	}


