
import java.util.Scanner;
import java.util.Stack;
import java.io.*;
import java.awt.Desktop;
import java.util.ArrayList;
public class Ejercicio1 {
	public static Scanner scan = new Scanner(System.in);
	public static File tiempoEjecucion = new File("tiempoEjecucion.txt");//RUTA DEL FILE

	public static void main(String[] args) throws IOException {
	
		archivoNuevoTxt();//LA LLAMADA AL M�TODO NOS AYUDA A CREAR O NO UN NUEVO ARCHIVO DESORDENADO	 
		tiempoEjecucion.createNewFile(); 	
		BufferedWriter bw = new BufferedWriter(new FileWriter(tiempoEjecucion));	
		ArrayList<String> nombres = mostrarNombresTxt();//AC� GUARDAMOS LOS NOMBRES DE TODOS LOS FILES CON LA LLAMADA AL M�TODO
		for (int k = 0; k < nombres.size(); k++) {//ESTE FOR NOS AYUDA A OBTENER LA CANTIDAD DE ARCHIVOS QUE CONTIENE LA CARPETA
			int cantidadDatos = cantidadDatosTxt(k);	
			int arreglo[] = new int[cantidadDatos];//SE CREA UN ARRAY INDICANDO EL TAMA�O DEL ARRAY CON LA LLAMADA AL M�TODO 
													//QUE SE GUARD� EN LA VARIABLE "cantidadDatos"
			
			BufferedReader br = new BufferedReader(new FileReader("numerosAleatorios/"+nombres.get(k)));
			String linea=br.readLine();//LEE L�NEA POR L�NEA DEL ARCHIVO
			try {	
				int cont = 0;//ESTE ES UN CONTADOR QUE NOS SIRVE PARA UBICAR EL �NDICE DEL ARRAY
				while(linea != null) {//VERIFICAMOS SI AL LEER LA L�NEA YA NO TIENE ALG�N DATO
					arreglo[cont] = Integer.parseInt(linea);//AC� GUARDA CADA N�MERO DEL ARCHIVO EN UN ARRAY,
															//PERO COMO SABEMOS CADA L�NEA ES TEXTO. PARA ELLO CONVERTIMOS CADA 
															//L�NEA EN UN TIPO ENTERO PARA QUE SE PUEDA GUARDAR EN EL ARRAY DE ENTEROS
					linea = br.readLine();//CONTINUA LEYENDO CADA L�NEA
					cont++;
				}
			}catch (EOFException e) {}
			br.close();					//AC� CERRAMOS EL ARCHIVO QUE SE ESTABA LEYENDO
			
			long inicio = 0, fin = 0;//SE CREA DOS VARIABLES DE TIPO LONG PARA ALMACENAR EL TIEMPO NECESARIO PARA ORDENAR LOS DATOS
			inicio = System.nanoTime();
			ordenacionRapida(arreglo);//SE LLAMA AL M�TODO QUICKSORT PARA ORDENAR LOS DATOS DEL ARCHIVO
			fin = System.nanoTime();
			long tiempo = fin-inicio;//NOS DEVUELVE EL RESULTADO RESTANDO EL TIEMPO FINAL MENOS EL TIEMPO DEL INICIO
			bw.write("Tiempo de demora del archivo "+nombres.get(k)+" con cantidad de elementos "+cantidadDatos+": "+fin+"-"+inicio+" -> "+tiempo+" nanosegundos\n");
			//EN LA L�NEA DE ARRIBA SE UTILIZA PARA ESCRIBIR TODA LA INFORMACI�N NECESARIA PARA DAR SOLUCI�N AL EJERCICIO.
			imprimirArregloOrdenadoTxt(arreglo, nombres.get(k));//SE LLAMA AL M�TODO PARA CREAR ARCHIVOS CON LOS MISMOS NOMBRES
																//DE SU ORIGEN. EN ESTE CASO SE IMPRIME LOS N�MEROS ORDENADOS. 
		}//FIN FOR
		bw.close();//CERRAMOS EL ARCHIVO "tiempoEjecuci�n"

		Desktop.getDesktop().open(tiempoEjecucion);//SE UTILIZA PARA ABRIR EL ARCHIVO "tiempoEjecucion" AUTOM�TICAMENTE.

	}//FIN MAIN	

	/*
	 * ESTE M�TODO CREA CUALQUIER ARCHIVO CON CUALQUIER EXTENSI�N QUE QUERAMOS CREAR.
	 * ESTO SE DA CON EL FIN DE CREAR EN CADA ARCHIVO UNA SERIE DE N�MEROS REALES EN DESORDEN.
	 * ESTE M�TODO CREA UN ARCHIVO QUE CONTIENE UNA SERIE DE N�MEROS REALES, UNO POR L�NEA.
	 */
	public static void crearArregloAleatorioTxt(int num) throws IOException {	
		ArrayList<String> nombres = mostrarNombresTxt();
		File numeroReal = new File("numerosAleatorios/real"+(nombres.size()+1)+".txt");
		numeroReal.createNewFile(); 
		BufferedWriter bw = new BufferedWriter(new FileWriter("numerosAleatorios/real"+(nombres.size()+1)+".txt"));
		int[] array = new int[num];
		Stack<Integer> aleatorio = numerosAleatorios(num);	
		for (int i = 0; i < array.length; i++) {	
			array[i] = aleatorio.get(i);
			bw.write(array[i]+"\n");
		}
		bw.close();	
	}

	public static void ordenacionRapida(int[] vec){
		final int N=vec.length;	
		quickSort(vec, 0, N-1);
	}	

	/* 
     * ESTA M�TODO TOMA EL �LTIMO ELEMENTO COMO PIVOTE, COLOCA EL ELEMENTO DE PIVOTE EN SU POSICI�N CORRECTA
     * Y COLOCA TODOS LOS MENORES QUE EL PIVOTE A LA IZQUIERDA Y TODOS LOS ELEMENTOS MAYORES A LA DERECHA
     */
	public static void quickSort(int A[], int izq, int der) {
		int pivote=A[izq]; //SE TOMA EL PRIMER ELEMENTO COMO PIVOTE
		int i=izq;         // i REALIZA LA B�SQUEDA DE IZQUIERDA A DERECHA
		int j=der;         // j REALIZA LA B�SQUEDA DE DERECHA A IZQUIERDA
		int aux;
		while(i < j){                          //MIENTRAS NO SE CRUCEN LAS B�SQUEDAS                                  
			while(A[i] <= pivote && i < j) i++; //BUSCA EL ELEMENTO MAYOR
			while(A[j] > pivote) j--;           //BUSCA EL ELEMENTO MENOR
			if (i < j) {                        //SI NO SE HAN CRUZADO                    
				aux= A[i];                      //LOS INTERCAMBIA
				A[i]=A[j];
				A[j]=aux;
			}
		}
		A[izq]=A[j];      //SE COLOCA EL PIVOTE EN SU LUGAR DE FORMA QUE TENDREMOS                                    
		A[j]=pivote;      //LO SMENORES A SU IZQUIERDA Y LOS MAYORES A SU DERECHA

		if(izq < j-1)
			quickSort(A,izq,j-1);//ORDENAMOS SUBARRAY IZQUIERDA	
		if(j+1 < der)
			quickSort(A,j+1,der);//ORDENAMOS SUBARRAY DERECHA
	}
	
	/*
	 * ESTE M�TODO NOS SIRVE PARA MOSTRAR LOS NOMBRES DE CADA ARCHIVO INCLUYENDO LA EXTENSI�N DEL ARCHIVO. 
	 * ESTE M�TODO ES IMPORTANTE PORQUE AS� SABREMOS A QU� ARCHIVO NOS ESTAMOS REFIRIENDO A LA HORA DE 
	 * ORDENAR Y SACAR EL TIEMPO DE EJECUCI�N. 
	 */
	public static ArrayList<String> mostrarNombresTxt() {		
		File directorio = new File("numerosAleatorios"); //RUTA DE LA CARPETA CON ARCHIVOS 
		File archivos[]=directorio.listFiles(); //CARGA TODOS LOS ARCHIVOS	
		ArrayList<String> nombresTxt = new ArrayList<String>();
		for(int i=0; i<archivos.length;i++) {
			if(archivos[i].isFile()) {	
				nombresTxt.add(archivos[i].getName());
			}
		}
		return nombresTxt;				
	}		
					
	/*
	 * ESTE M�TODO SE UTILIZA PARA CREAR N�MEROS ALEATORIOS NO REPETIDOS EN CADA ARCHIVO. 
	 * CADA N�MERO SE CREA EN CADA L�NEA DEL ARCHIVO.
	 */
	public static Stack<Integer> numerosAleatorios(int num) {	
		int posicion;
		Stack<Integer> numeroAleatorio = new Stack <Integer> ();		
		for (int i = 0; i < num ; i++) {
			posicion = (int) (Math.floor(Math.random() * num)+1);
			while (numeroAleatorio.contains(posicion)) {	
				posicion = (int) (Math.floor(Math.random() * num)+1);
			}
			numeroAleatorio.push(posicion);	
		}
		return numeroAleatorio;
	}	
		
	/*
	 * ESTE M�TODO IMPRIME LOS DATOS ORDENADOS EN UN ARCHIVO Y EN UNA DIFERENTE CARPETA. EN ESTE CASO LA CARPETA SE LLAMA 
	 * "numerosAleatoriosOrdenados". EN ESA CARPETA SE VA A CREAR DIFERENTES ARCHIVOS. CADA ARCHIVO QUE SE CREE VA A TENER
	 * EL MISMO NOMBRE DE SU ARCHIVO ORIGINAL DESORNADO. 
	 */
	public static void imprimirArregloOrdenadoTxt(int arreglo[], String nom) throws IOException {	
		File numeroRealOrdenado = new File("numerosAleatoriosOrdenados/"+nom);	
		numeroRealOrdenado.createNewFile(); 
		BufferedWriter bw = new BufferedWriter(new FileWriter("numerosAleatoriosOrdenados/"+nom));
		for(int i = 0; i<arreglo.length; i++) {
			bw.write(arreglo[i]+"\n");
		}
		bw.close();
	}		
	
	/*
	 * ESTE M�TODO NOS DEVUELVE LA CANTIDAD DE DATOS QUE CONTIENE EL ARCHIVO.
	 * ESTO SE DA CON EL FIN DE SABER QU� CANTIDAD DE DATOS, EN REALIDAD, CONTIENE CADA ARCHIVO. 
	 * ESTE M�TODO NOS SERVIR� PARA SABER QU� TAMA�O DE UN ARRAY DEBE DE TENER.
	 */
	public static int cantidadDatosTxt(int conta) throws IOException {	
		ArrayList<String> nombres = mostrarNombresTxt();	
		int contador = 0;
		BufferedReader br = new BufferedReader(new FileReader("numerosAleatorios/"+nombres.get(conta)));
		String linea=br.readLine();	
		try {	
			while(linea != null) {
				linea = br.readLine();	
				contador++;
			}
		}catch (EOFException e) {}
		br.close();

		return contador;
	}	
	
	/*
	 * ESTE M�TODO NOS AYUDA PARA SABER SI EL USUARIO QUIERE CREAR UN NUEVO ARCHIVO CON LA CANTIDAD DE DATOS DESORDENADOS 
	 * QUE QUISIERA.
	 */
	public static void archivoNuevoTxt() throws IOException {
		System.out.println("Desea crear un nuevo archivo.txt? (y or n)");
		String of = scan.nextLine();
		String on = "Y";
		while(of.equalsIgnoreCase(on)) {
			System.out.println("Digite la cantidad de datos enteros que debe de contener el archivo.txt");
			int cantidad = scan.nextInt();
			crearArregloAleatorioTxt(cantidad);
			System.out.println("Desea crear un nuevo archivo.txt? (y or n)");
			of = scan.nextLine();
		}
	}
	
	
}//FIN CLASE
