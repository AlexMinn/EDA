	/**
	 * Alejandro Miñambres Mateos
	 */

import java.util.*;
import java.io.*;
	/**
	 * Clase que complementa a la tabla de dispersion
	 */
	class valores{
		private int clave;
		private int padre;
		private int cant;
		
		public valores(int clave, int padre){
			this.clave=clave;
			this.padre=padre;
			cant=1;
		}
		public valores(int clave, int padre,int cant){
			this.clave=clave;
			this.padre=padre;
			this.cant=cant;
		}
		
		public int getClave(){
			return clave;
		}
		public int getPadre(){
			return padre;
		}
		public int getCantidad(){
			return cant;
		}
		public void setPadre(int padre) {
			this.padre=padre;
		}
		public void setCant(int cant) {
			this.cant=cant;
		}
	}
	/**
	 * Clase tabla de dispersion adaptada
	 * al la pracica 2 Eda
	 */
	 class tablaDispersion {
		
		private int m; //capacidad tabla
		private int n; //numero de elementos
		private final double maxL=0.6; //factor de carga maximo
		
		private ArrayList<valores> tabla;
		/**
		 * Inicializa la tabla de dispersion
		 * @param num
		 */
		public tablaDispersion(int num) {
			m=2*num;
			tabla= new ArrayList<valores>();
			for(int i=0;i<m;i++) {
				tabla.add(i,new valores(-1,-1,0));
			}
		}
		/**
		 * Permite añadir valores a la tabla
		 * @param dato
		 */
		public void añadir (int dato) {
			int num=dato%m;
			
			if (tabla.get(num).getClave()==-1) {
				tabla.set(num,new valores (dato,dato));
				n++;
			}else if (tabla.get(num).getClave()!=dato){
				n++;
				tabla.set(saltoVacio(dato),new valores (dato,dato));	
			}
			if (((double)n/(double)m)>maxL) {
				restructurar();
			}	
		}
		/**
		 * Devuelve la tabla 
		 * @return tabla
		 */
		public ArrayList<valores> getTabla(){
			return tabla;
		}
		/**
		 * Funcion que permite reestructurar la tabla si se supera
		 * el factor de carga maxima
		 */
		public void restructurar() {
			ArrayList<valores> tmp = tabla;
			n=0;
			m=2*m;
			tabla=new ArrayList<valores>();
			for(int i=0;i<m;i++) {
				tabla.add(i,new valores(-1,-1,0));
			}
			for(int i=0;i<tmp.size();i++) {
				valores cambio = tmp.get(i);
				if(cambio.getClave()!=-1) {
					añadir(cambio.getClave());
				}	
			}
		}
		/**
		 * Permite acceder al valor de la tabla asociado
		 * @param num - dato al que se quiere acceder
		 * @return el indice correspondiente de la tabla
		 */
		public int accederValor (int num) {
			if (getTabla().get(num%m).getClave()!=num) {
				return salto(num);
			}
			return num%m;
		}
		/**
		 * Permite realizar un salto para encontrar el dato
		 * @param dato
		 * @return posicion donde almacenar el dato 
		 */
		public int salto (int dato) {
			int salto=(dato%m);
			boolean encontrado=false;
			if (salto>=m) {
				salto=salto-m;
			}
			while (encontrado==false) {
				if (salto>=m) {
					salto=salto-m;
				}
				if ((getTabla().get(salto).getClave()==dato)) {
					encontrado=true;
				}
				salto=salto+1;
			}
			return (salto-1);
		}
		/**
		 *Permite realizar un salto para que el dato se almacene 
		 * en una posicion vacia
		 * @param dato
		 * @return posicion donde almacenar el dato 
		 */
		public int saltoVacio (int dato) {
			int salto=(dato%m);
			boolean encontrado=false;
			while (encontrado==false) {
				if (salto>=m) {
					salto=salto-m;
				}
				if ((getTabla().get(salto).getClave()==-1)) {
					encontrado=true;
				}
				salto=salto+1;
			}
			return (salto-1);
		}
		
	}
	 /**
	  * Clase DisjointSet adaptada a la 
	  * practica 2 Eda
	  */
	 class DisjointUnionSets {

	 	tablaDispersion parent;
	     int n;

	     // Constructor
	     public DisjointUnionSets(int n) {
	     	parent = new tablaDispersion(n);
	     	this.n = n;
	     }
	     /**
	      * Permite añadir elementos nuevos
	      * @param x
	      */
	     void añadirNuevo(int x) {
	     	parent.añadir(x);
	     }

	     /**
	      * Funcion que encuentra el padre del usuario dado
	      * @param x - usuario
	      * @return padre del usuario
	      */
	     int find(int x) {
	      
	     	if (parent.getTabla().get(parent.accederValor(x)).getPadre() != x) {
	     		parent.getTabla().get(parent.accederValor(x)).setPadre(
	     				find(parent.getTabla().get(parent.accederValor(x)).getPadre()));
	     	}

	     	return parent.getTabla().get(parent.accederValor(x)).getPadre();
	     }

	     /**
	      * Funcion que permite unir dos grumos
	      * a los que pertenecen dos usuarios
	      * @param x - usuario 1
	      * @param y - usuario 2
	      */
	     void union(int x, int y)
	     {
	     	int xRoot = find(x);
	     	int yRoot = find(y);
	     	int xCant = parent.getTabla().get(parent.accederValor(xRoot)).getCantidad();
	     	int yCant = parent.getTabla().get(parent.accederValor(yRoot)).getCantidad();

	     	if (xRoot == yRoot) {
	     		return;
	     	}else {
	     		if (xCant<yCant) {
	     			parent.getTabla().get(parent.accederValor(xRoot)).setPadre(yRoot);
	     			parent.getTabla().get(parent.accederValor(x)).setPadre(yRoot);
	     		 
	     			parent.getTabla().get(parent.accederValor(yRoot)).setCant(xCant+yCant);
	     			parent.getTabla().get(parent.accederValor(xRoot)).setCant(1);
	     		 
	     		}else {
	     			parent.getTabla().get(parent.accederValor(yRoot)).setPadre(xRoot);
	     			parent.getTabla().get(parent.accederValor(y)).setPadre(xRoot);
	     		 
	     			parent.getTabla().get(parent.accederValor(xRoot)).setCant(xCant+yCant);
	     			parent.getTabla().get(parent.accederValor(yRoot)).setCant(1);
	     		}
	     	} 
	      }
	  }
	 /**
	  * PROGRAMA PRINCIPAL
	  * @author Alejandro Miñambres Mateos
	  *
	  */
	 public class Practica2AlejandroMiñambres {

			public static void main(String[] args) {
				System.out.println ("ANALISIS DE CARALIBRO");
				System.out.println ("----------------------");
				Scanner entrada= new Scanner(System.in);
				
				Scanner fichero, fichero2;
				int n; //numero usuarios
				int m;//numero relacciones de amistad
				double Tiempant, Tiempdes, Tiempo;
				
				String entradaTeclado;
				String entradaTeclado2;
				int entradaTeclado3;
				
				ArrayList<Integer> red = new ArrayList<>();
				
		        
		        /**
		         * Primer fichero
		         */
		        System.out.print ("Fichero principal: ");
		        entradaTeclado = entrada.nextLine ();
				
				//abrimos el primer fichero 
				try {
					Tiempant=System.currentTimeMillis();
					fichero= new Scanner (new File(entradaTeclado));
				}
				catch(FileNotFoundException e) {
					System.out.println("No se encontro el fichero");
					entrada.close();
					return;
				}
				//numero de usuarios
		        n=fichero.nextInt();
		        //numero de relacciones de amistad 
		        m=fichero.nextInt();
		        
		        //creamos los grumos y red
		        DisjointUnionSets grumos= new DisjointUnionSets(n);
				
		        while(fichero.hasNextInt()) {
		          	 int x= fichero.nextInt();
		          	 //añadimos cada elemento como grumo nuevo
		               // grumos.añadirNuevo(x);
		                red.add(x);
		           }
		        
		        Tiempdes=System.currentTimeMillis();
				Tiempo=(Tiempdes-Tiempant)*0.001;
				System.out.println("Tiempo en leer el fichero: " + Tiempo + " seg." );
				
				/**
				 * Segundo fichero
				 */
				int x;
				int y;
				System.out.print ("Fichero de nuevas conexiones (pulse enter si no existe): ");
				entradaTeclado2 = entrada.nextLine ();
		        if(entradaTeclado2.isEmpty()==false) {
		        	try {
		      			fichero2= new Scanner (new File(entradaTeclado2));
		      			while (fichero2.hasNext()) {
		      				x=fichero2.nextInt();
		      	        	y=fichero2.nextInt();
		      	        	red.add(x);
		      	        	red.add(y);
		      	        	m=m+1;
		      			}
		      		  }
		      		  catch(FileNotFoundException e) {
		      			System.out.println("No se encontro el fichero 2");
		      			entrada.close();
		      			return;
		      		  }
		        }
				
		        for(int i=0;i<2*m;i=i+2) {
		        	int n1=red.get(i);
		        	int n2=red.get(i+1);
					grumos.añadirNuevo(n1);
					grumos.añadirNuevo(n2);
		        }
				//Recorremos red para realizar las uniones de los grumos 
				Tiempant=System.currentTimeMillis();
				for(int i=0;i<2*m;i=i+2) {
		        	int n1=red.get(i);
		        	int n2=red.get(i+1);
		        	grumos.union(n1, n2);
		        }
				
				Tiempdes=System.currentTimeMillis();
				Tiempo=(Tiempdes-Tiempant)*0.001;
				
		        System.out.println ("Hay " + n +" usuarios, y "+ m + " relacciones");
		        
		        /**
		         * Porcentaje tamaño mayor grumo
		         */
		        System.out.print ("Porcentaje tamaño mayor grumo: ");
		        entradaTeclado3 = entrada.nextInt();
		        
		        System.out.println("Tiempo de proceso: " + Tiempo + " seg." );
		        
		      //Selecionar los grumos principales
		        Tiempant=System.currentTimeMillis();
		        ArrayList<Integer> ls = new ArrayList<>();
		        ArrayList<Integer> lc = new ArrayList<>();
		        for (int i=0;i<grumos.parent.getTabla().size();i++) {
		        	boolean yaEsta=false;
		        	if(grumos.parent.getTabla().get(i).getCantidad()>1) {
		        		for (int j=0;j<ls.size();j++) {
		        			if(grumos.find(grumos.parent.getTabla().get(i).getClave()) == ls.get(j) ) {
		        				yaEsta=true;
		        			}
		        		}
		        	
		        		if (yaEsta==false) {
		        			ls.add(grumos.find(grumos.parent.getTabla().get(i).getClave()));
		        			lc.add(grumos.parent.getTabla().get(grumos.parent.accederValor(grumos.find
		        					(grumos.parent.getTabla().get(i).getClave()))).getCantidad());
		        		}
		        	}	
		        }
		        /**
		         * Ordenación de los grumos
		         */
		      
		    	int tamGrus= ls.size();
		    	int aux,auxC;
		    	for (int i=0;i<tamGrus-1;i++) {
		    		for(int j=tamGrus-1;j>i;j--) {
		    			if(lc.get(j-1)<lc.get(j)) {
		    				aux=ls.get(j-1);
		    				auxC=lc.get(j-1);
		    				ls.set(j-1, ls.get(j));
		    				ls.set(j, aux);
		    				lc.set(j-1, lc.get(j));
		    				lc.set(j, auxC);
		    			}
		    		}
		    	}
		    	/**
		    	 * Grumos necesarios 
		    	 */
		    	boolean PorcCumplido =false;
		    	int total=0;
		    	int GrumosUnidos = 0;
		    	double result =0;
		    		
		    	while (PorcCumplido==false) {
		    		total=total + lc.get(GrumosUnidos);
		    			
		    		result=((double)total/n)*100;
		    		if (result>= entradaTeclado3) {
		    			PorcCumplido=true;
		    		}else {
		    			GrumosUnidos= GrumosUnidos +1;
		    		}
		    	}
		    	Tiempdes=System.currentTimeMillis();
		    	Tiempo=(Tiempdes-Tiempant)*0.001;
		    	if (GrumosUnidos==0) {
		    		System.out.println("El mayor grumo contiene " + total + " usuarios" + " ("+result+"%)");
		    		System.out.println("No son necesarias nuevas relaciones de amistad");
		    	}else {
		    	
		    		
		    		System.out.println("Exiten " + tamGrus + " grumos" );
		    		System.out.println("Se deben unir los " + (GrumosUnidos +1) + " mayores" );
		    		for (int i=0;i<GrumosUnidos+1;i++) {
		    			System.out.println("#"+(i+1)+": " + lc.get(i)+ " usuarios ("+ ((double)lc.get(i)/n)*100 +"%)"   );
		    		}
		    		
		    		System.out.println("Nuevas relaciones de amistad (salvadas en extra.txt)");
		    	
		    		PrintWriter extra;
		    		extra=null;
		    		try {
		    			extra=new PrintWriter ("extra.txt");
		    			for (int i=0;i<GrumosUnidos;i++) {
			    			System.out.println(ls.get(i) +" <-> " + ls.get(i+1));
			    			extra.println(ls.get(i) +" " + ls.get(i+1));
			    		}
		    			extra.close();
		    		}
		    		catch(IOException e){
		    			System.out.println("No puede abrirse el archivo");			
		    		}
		    		
		    		
		    		entrada.close();
		    	} 	
			}		
		}	 