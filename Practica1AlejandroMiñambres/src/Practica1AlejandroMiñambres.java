/**
 * 
 * @author Alejandro Miñambres Mateos
 *
 */

import java.io.*;
import java.util.*;



class conexion {
	private int x;
	private int y;
	
	public void cone (int x, int y) {
		this.x=x;
		this.y=y;
	}
	public int getX () {
		
		return x;
	}
	public int getY () {
		
		return y;
	}
	
}

public class Practica1AlejandroMiñambres {

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
		
		ArrayList<conexion> red = new ArrayList<>();
		ArrayList<Integer> usr = new ArrayList<>();
		ArrayList<ArrayList<Integer>> grus = new ArrayList<>();
		ArrayList<Integer> asig = new ArrayList<>();
        
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
        
        //creamos red
        creacionRed(n,m,fichero,red);
        
        //tiempo que tarda en crear red
        Tiempdes=System.currentTimeMillis();
		Tiempo=(Tiempdes-Tiempant)*0.001;
		System.out.println("Tiempo en abrir el fichero: " + Tiempo + " seg." );
		
		
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
      	        	conexion c= new conexion();
      	        	c.cone(x, y);
      	        	red.add(c);
      	        	m=m+1;
      			}
      		  }
      		  catch(FileNotFoundException e) {
      			System.out.println("No se encontro el fichero 2");
      			entrada.close();
      			return;
      		  }
        }

        System.out.println ("Hay " + n +" usuarios, y "+ m + " relacciones");
        
        /**
         * Porcentaje tamaño mayor grumo
         */
        System.out.print ("Porcentaje tamaño mayor grumo: ");
        entradaTeclado3 = entrada.nextInt();
        
        /**
         * creamos usr
         */
        Tiempant=System.currentTimeMillis();
        creacionUsr(m,red,usr);
        Tiempdes=System.currentTimeMillis();
		Tiempo=(Tiempdes-Tiempant)*0.001;
		System.out.println("Creación lista usuarios: " + Tiempo + " seg." );
        /**
         * grus y asig
         */
		Tiempant=System.currentTimeMillis();
		creacionListaGrumos(n,usr,asig,grus,red);
		
		Tiempdes=System.currentTimeMillis();
		Tiempo=(Tiempdes-Tiempant)*0.001;
		System.out.println("Creación lista grumos: " + Tiempo + " seg." );
		/**
		 * ordenar grus 
		 * 
		 */
		Tiempant=System.currentTimeMillis();
		ordenarGrus(grus);
		/**
		 * Grumos necesarios 
		 */
		boolean PorcCumplido =false;
		int total=0;
		int GrumosUnidos = 0;
		double result =0;
		int tamGrus= grus.size();
		
		while (PorcCumplido==false) {
			total=total + grus.get(GrumosUnidos).size();
			
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
		
			System.out.println("Ordenación y creación de grumos: " + Tiempo + " seg." );
			System.out.println("Exiten " + tamGrus + " grumos" );
			System.out.println("Se deben unir los " + (GrumosUnidos +1) + " mayores" );
			for (int i=0;i<GrumosUnidos+1;i++) {
				System.out.println("#"+(i+1)+": " + grus.get(i).size()+ " usuarios ("+ ((double)grus.get(i).size()/n)*100 +"%)"   );
			}
			
			System.out.println("Nuevas relaciones de amistad (salvadas en extra.txt)");
		
			PrintWriter extra;
			extra=null;
			try {
				extra=new PrintWriter ("extra.txt");
			}
			catch(IOException e){
				System.out.println("No puede abrirse el archivo");			
			}
			for (int i=0;i<GrumosUnidos;i++) {
				System.out.println(grus.get(i).get(0) +" <-> " + grus.get(i+1).get(1));
				extra.println(grus.get(i).get(0) +" " + grus.get(i+1).get(1));
			}
			extra.close();
			entrada.close();
		}
		
		
	}
	/**
	 * Función que crear Red, que es el ArrayList que tiene los datos del fichero introducido
	 * @param n
	 * @param m
	 * @param fichero
	 * @param red
	 */
	public static void creacionRed (int n, int m, Scanner fichero, ArrayList<conexion> red) {
		int x;
        int y;
        for (int i=0;i<m;i++) {
        	x=fichero.nextInt();
        	y=fichero.nextInt();
        	conexion c= new conexion();
        	c.cone(x, y);
        	red.add(c);
        }
		
		
	}
	/**
	 * Función que crea usr, para ello se recorren todas las conexiones almacenadas en red y si alguno de
	 * los dos usarios de cada conexión no están todavía en usr se les añade
	 * @param m
	 * @param red
	 * @param usr
	 */
	public static void creacionUsr (int m,ArrayList<conexion> red, ArrayList<Integer> usr ){
		for (int i=0;i<m;i++) {
        	int x=red.get(i).getX();
        	int y=red.get(i).getY();
        	if (usr.contains(x)==false) {
        		usr.add(x);
        	}
        	if (usr.contains(y)==false) {
        		usr.add(y);
        	}
        }
	}
	/**
	 * Función que recorre todos los usuarios (usr), y para cada
     * uno, si no está en asig, se añadirá a la lista de grumos (grus), grumo al que
     * pertenece, obtenido mediante la función uber_amigos
	 * @param n
	 * @param usr
	 * @param asig
	 * @param grus
	 * @param red
	 */
	public static void creacionListaGrumos(int n,ArrayList<Integer> usr,ArrayList<Integer> asig,
			ArrayList<ArrayList<Integer>> grus,ArrayList<conexion> red){
		int usuario;
		for (int i=0;i<n;i++) {
			usuario= usr.get(i);
			
			if (asig.contains(usuario)==false) {
				ArrayList<Integer> grumoj = new ArrayList<>();
				grumoj.add(usuario);
				
				grumoj=uber_amigos (usuario,red,grumoj);
				
				grus.add(grumoj);
				
				for (int j=0;j<grumoj.size();j++) {
					asig.add(grumoj.get(j));
				}
			}
		}
	}
	/**
	 * Función que obtiene el grumo al que pertenece
	 * @param u
	 * @param conex
	 * @param grumo
	 * @return
	 */
	public static ArrayList<Integer> uber_amigos (int u, ArrayList<conexion> conex , ArrayList<Integer> grumo ) {
		int size = conex.size();
		
		for(int i=0;i<size;i++) {
			int x=conex.get(i).getX();
        	int y=conex.get(i).getY();
        	
        	if (x==u) {
        		if(grumo.contains(y)==false) {
        			grumo.add(y);
        			uber_amigos(y,conex,grumo);
        		}
        	}
        	if (y==u) {
        		if(grumo.contains(x)==false) {
        			grumo.add(x);
        			uber_amigos(x,conex,grumo);
        		}
        	}
		}
		return grumo;
	}
	/**
	 * Funcion que ordena Grus en orden descendente 
	 * @param grus
	 */
	public static void ordenarGrus(ArrayList<ArrayList<Integer>> grus) {
		int tamGrus= grus.size();
		ArrayList<Integer> aux = new ArrayList<>();
		for (int i=0;i<tamGrus-1;i++) {
			for(int j=tamGrus-1;j>i;j--) {
				if(grus.get(j-1).size()<grus.get(j).size()) {
					aux=grus.get(j-1);
					grus.set(j-1, grus.get(j));
					grus.set(j, aux);
				}
			}
		}
	}


}
