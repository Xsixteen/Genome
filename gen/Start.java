package gen;

import Jama.Matrix;
import gen.Tree.*;

public class Start {
	/**
	 * Launch the application.
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		Matrix m;		
		long start;
		long end;
		float full, partial;
		Genesis g = new Genesis();
		Tree T = g.getTree();

		start = System.currentTimeMillis();  
		g.generate();
	//	System.out.println("Number of Nodes: " + g.countNodes());
		System.out.println("Number of Matrices Generated: " +g.getStats());
		System.out.println("Done");
		full = T.countNodes();
		System.out.println("Number of Nodes by Tree Function: " + full);
		System.out.println("Preforming Tree Reduction");
		g.checkBoards(T.root);
		partial = T.countNodes();
		System.out.println("Number of Nodes by Tree Function: " + partial + "\nOptimatization %: " + (partial/full)*100);
		System.out.println("Total memory (bytes): " +  Runtime.getRuntime().totalMemory());
	
		end =  System.currentTimeMillis();  
		System.out.println("This Program took: "+ ((end - start)/1000) + " Seconds to execute.");
	}
}
