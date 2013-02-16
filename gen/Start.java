package gen;

import java.util.Scanner;

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
		int playerid=0;
		boolean loop=true;
		final int P_X = 1;
		final int P_O = -1;
		float full, partial;

		
	    Scanner scanner = new Scanner(System.in);
		Genesis g = new Genesis(P_X);
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
		System.out.println(T.root.numChildren + " children");
		partial = T.countNodes();
		System.out.println("Number of Nodes by Tree Function: " + partial + "\nOptimatization %: " + (partial/full)*100);
		System.out.println("Total memory (bytes): " +  Runtime.getRuntime().totalMemory());
		System.out.println("Displaying Winning Branches for " + T.root.numChildren + " children");
		g.countWins(T.root);
		g.printChildrenWinTrees(T.root);
		end =  System.currentTimeMillis();  
		System.out.println("This Program took: "+ ((end - start)/1000) + " Seconds to execute.");
		
		System.out.println("Welcome to Tic-Tac-Toe Game Tree Analyzer");
		System.out.println("Create your Board!  First tell me what player you are.  1 for X, 2 for O");
		String player = scanner.nextLine();
		
		if (player.equals("1")) {
			playerid = 1;
		} else {
			playerid = -1;
		}
		
		g = new Genesis(playerid);
		T = g.getTree();
		while(loop) {
			System.out.println("Current Board as follows:");
			g.printBoard(g.getCurrentBoard());
			System.out.println("Press 1 to place your move.  Press 2 to generate Optimal Solutions");
			String menu = scanner.nextLine();
			
			//Place YOUR move
			if (menu.equals("1")) {
				System.out.println("Enter the X (row) of  your move:");
				String row = scanner.nextLine();
				System.out.println("Enter the Y (column)1 of  your move:");
				String col = scanner.nextLine();
				g.updateMove(playerid, Integer.parseInt( row )-1, Integer.parseInt(col)-1);
				
				
				System.out.println("Put in O's Move:");
				System.out.println("Enter the X (row) of  your opponents move:");
				row = scanner.nextLine();
				System.out.println("Enter the Y (column) of  your opponents move:");
				col = scanner.nextLine();
				g.updateMove((-1*playerid), Integer.parseInt( row )-1, Integer.parseInt(col)-1);
			} else if (menu.equals("2")) {
				g.create();
				g.printChildrenWinTrees(g.getTree().root);
				
				System.out.println("Number of Nodes:: " + T.countNodes());
			}
		}
	}
}
