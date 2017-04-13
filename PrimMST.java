import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;


class Vertex {
	private int id;
	private int value;
	private Vertex parent;
	private ArrayList<Vertex> listAdj;
	
	public Vertex(int id, int value, Vertex parent){
		this.id = id;
		this.value = value;
		this.parent = parent;
		this.listAdj = new ArrayList<Vertex>();
	}

	public int getId(){return this.id;}
	public int getValue(){return this.value;}
	public Vertex getParent(){return this.parent;}
	public ArrayList<Vertex> getListAdj(){return this.listAdj;}

	public void setId(int id){this.id = id;}
	public void setValue(int value){this.value = value;}
	public void setParent(Vertex parent){this.parent = parent;}
	
	@Override
	public String toString() {
		return "Vertex [id=" + id + ", value=" + value + ", parent=" + parent + "]";
	}
}

public class PrimMST {
	
	private static int INFINITY = Integer.MAX_VALUE;

	/*PRIM MST*/

	public static void primMST(int sizeV, int[][] matrixAdj, Vertex[] listVertex){

		Vertex[] result = new Vertex[sizeV]; 
		Vertex currentVertex = null;
		int oldSize = 0;
		
		listVertex[0].setValue(0); //Atribui zero ao vertice inicial
		/*q eh a lista de vertices a serem investigados*/
		ArrayList<Vertex> q = buildMinHeap(listVertex.clone());
		
		while ((oldSize = q.size()) != 0){
			currentVertex = q.remove(0);		//Remove o menor valor (primeiro vertex da list)
			
			for (Vertex v : currentVertex.getListAdj()){
				if(q.contains(v) && matrixAdj[currentVertex.getId()][v.getId()] < v.getValue()){
					v.setParent(currentVertex);		//Atribui o vertice retirado como o novo pai
					v.setValue(matrixAdj[currentVertex.getId()][v.getId()]);	//Modifica o valor do vertice pelo peso da aresta correspondente
				}
			}
			
			q = buildMinHeap(q.toArray(new Vertex[q.size()]));
			
			result[sizeV - oldSize] = currentVertex;		//Guarda os vertices ja acessados
		}
		
		int total = 0;
		System.out.println("Result Set:");
		for(int i = 0; i < result.length; i++){

			if(result[i].getParent() != null)				
				System.out.println(result[i].getId()+"-"+result[i].getParent().getId()+" (value = "+result[i].getValue()+")");
			else				
				System.out.println(result[i].getId()+"-"+result[i].getParent()+" (value = "+result[i].getValue()+")");
			
			total += result[i].getValue();
		}
		
		System.out.println("\nTotal Value: "+total);
		
	}

	/*SUPPORT FUNCTIONS*/
	/*HEAP MIN*/

	private static ArrayList<Vertex> buildMinHeap(Vertex[] vet){
		
		for (int i = (vet.length/2)-1; i >= 0; i--)
			minHeapfy(vet,vet.length,i);
		
		ArrayList<Vertex> ret = new ArrayList<Vertex>();
		
		for (int i = 0; i < vet.length; i++)
			ret.add(vet[i]);
		
		return ret;
	}
	
	private static Vertex[] minHeapfy(Vertex[] vet, int n, int index){
		int min = index, left = 2 * index, right = 2 * index + 1;
		
		if ((left <= n - 1) && (vet[left].getValue() < vet[min].getValue()))
			min = left;
		
		if((right <= n-1) && (vet[right].getValue() < vet[min].getValue()))
			min = right;
		
		if(min != index){
			Vertex aux = vet[index];
			vet[index] = vet[min];
			vet[min] = aux;
			
			return minHeapfy(vet,n,min);
		}
		
		return vet;
	}

	private static void showBiMatrix(int[][] m){
		for (int[] rows : m) {
	    	for (int col : rows) {
	        	System.out.format("%5d", col);
	    	}
	    	System.out.println();
		}
		System.out.println();
	}

	/*
		Modelo de entrada:
			n
			mAdj[i,j]
	*/
	public static void main(String[] args) {
		BufferedReader inReader = 
	            new BufferedReader(new InputStreamReader(System.in));

		int n = 0;
		String line;
		try {
		
			n = Integer.parseInt(inReader.readLine().split("[ ,\t]")[0]);
			int matrixAdj[][] = new int[n][n];
			Vertex listVertex[] = new Vertex[n];			

			String nums = "";
			int k = 0;
			
			while (inReader.ready()){
				nums += inReader.readLine()+" ";
			}

			String[] numbers = nums.split("[\td+, d+, \rd+]");

			for (int i = 0; i < n; i++)
				listVertex[i] = new Vertex(i, INFINITY, null);			

			for (int i = 0; i < n; i++){				
				for (int j = i + 1; j < n; j++){
					if (numbers[k].matches("[-+]?\\d*\\.?\\d+")){
						int value = Integer.parseInt(numbers[k]);
						matrixAdj[i][j] = value;						
						matrixAdj[j][i] = value;

						if (value != 0){
							listVertex[i].getListAdj().add(listVertex[j]);
							listVertex[j].getListAdj().add(listVertex[i]);							
						}

						k++;
					}
					else{j--;k++;}
				}
			}

			inReader.close();
			showBiMatrix(matrixAdj);
			primMST(n, matrixAdj, listVertex);
						
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}//main
}