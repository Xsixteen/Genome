package gen;
import java.util.Arrays;

import gen.Tree.*;
import Jama.*;

public class Genesis {

	public final int P_X = 1;
	public final int P_O = -1;
	
	private static Matrix curBoard;
	private static Tree T;
	private int player, start, end;
	private int statistic = 0;

	  //////////////////////////
	 //     Public Methods   //
	//////////////////////////
	
	public Genesis(int aPlayer) {
		double[][] empty = {{0,0,0},{0,0,0},{0,0,0}}; 
		//initialize the current board to all 0.0
		curBoard = new Matrix (empty);
		player = aPlayer;
		//Create the Game Tree
		T = new Tree();
	
	}
	//Getters / Setters
	public Matrix getCurrentBoard() {
		return curBoard;
	}
	public Tree getTree() {
		return T;
	}
	public int countNodes() {
		return T.countNodes();
	}
	
	public void updateMove(Matrix matrix, int player, int x, int y) {
		matrix.set(x, y, (double) player);
	}
	public void updateMove(int player, int x, int y) {
		curBoard.set(x, y, (double) player);
	}
	
	public void create() {
		updateRoot();
		generate();
		checkBoards(T.root);
		countWins(T.root);
	}
	
	//updates the root of the tree to the current matrix.
	public void updateRoot() {
		T = new Tree();
		T.root.matrix = curBoard;
	}
	
	//Generate against any matrix. 
	public Matrix generate(Matrix matrix, int aPlayer) {
		start = (int) System.currentTimeMillis();  
		spin(T.root, matrix, aPlayer);
		end = (int) System.currentTimeMillis();  
		return matrix;
	}
	
	//Generate against the objects matrix
	public void generate() {
		start = (int) System.currentTimeMillis();  
		//Okay so now its O's turn. Leaving a possible of moves - 1 
		spin(T.root, curBoard, player);
		end = (int) System.currentTimeMillis();  
	}

	public int getStats() {
		return statistic;
	}
	
	public void printBoard(Matrix m) {
		for (int i=0; i< m.getRowDimension(); i++) {
			System.out.print("| ");
			for(int j=0; j< m.getColumnDimension(); j++) {
				if(m.get(i,j) == P_X) {
					System.out.print(" X ");
				} else if(m.get(i,j) == P_O) {
					System.out.print(" O ");
				} else {
					System.out.print(" - ");
				}
			}
			System.out.println(" |");
		}
	}
	
	//This function prunes the tree for only winning children leaves
	public void checkBoards(Node root) {
		for (int q=0; q < root.numChildren; q++) {
			if(root.getChild(q).numChildren > 0) {
					checkBoards(root.getChild(q));
			} else {
				//Validate Leaf Nodes only.
				if(validate(root.getChild(q).matrix) != 1 || validate(root.getChild(q).matrix, (player*-1)) == 1) {
					root.removeChild(q);
				} else {
					// This is a winning leaf
					root.getChild(q).winners++;
				}
			}
		}
	
		}
	
	public void countWins(Node root) {
		if (root.numChildren > 0) {
			for (int q=0; q < root.numChildren; q++) {
					countWins(root.getChild(q));
					root.winners = root.winners + root.getChild(q).winners;
			}
		} else { 
			//root.winners++;
		}
	}
	public void printChildrenWinTrees(Node root) {
		mList[] list = new mList[root.numChildren];
		int z = 0;
		//Now Print!

		for (int q=0; q < root.numChildren; q++) {
			if(root.getChild(q).numChildren > 0){
				list[z] = new mList(root.getChild(q).matrix, root.getChild(q).winners);
				z++;
			
			}
		}
		Arrays.sort(list);

		for(mList temp: list){
			printBoard(temp.getMatrix());
			if(validate(temp.getMatrix()) == 1) {
				System.out.println("WINNING MOVE!");
			} else { 
				System.out.println("Wins for Child Branch: " + temp.getWins());
			}
			System.out.print("\n\n\n"); 
		}
	
			
	}
	
	  //////////////////////////
	 //     Private Methods  //
	//////////////////////////
	
	//Spin means spin through recursion! Used to generate a game tree from possible open spaces. 
	private int spin(Node root, Matrix matrix, int Player) {
		int open = countZero(matrix);
		Node n;
		Matrix temp;
		
		if (open == 0)
			return 1;
		
		for(int i=0; i< matrix.getRowDimension(); i++){
			for(int j=0; j < matrix.getColumnDimension(); j++){
					//Iterate through all the open spaces and create a matrix with a following subsequent move
				
					if (matrix.get(i,j) == 0.0) {
						//fresh matrix each time is required
						temp = matrix.copy();
						updateMove(temp, Player, i, j);
						System.out.println("Matrix Number Generated: "+getStats() +"  Zeroes found: "+countZero(temp)+"");
						//temp.print(5, 2);
						
						//Create a new node.
						n = new Node();
						statistic++;
						n.setMatrix(temp);
						
						if(countZero(temp) > 0)
							root.setChildren(n);
						
					}
			}
		}
		for (int q=0; q < root.numChildren; q++) {
			if(countZero(root.getChild(q).matrix) != 0 && validate(root.getChild(q).matrix, -1*player) != 1){
				if (Player == P_O)
					 spin(root.getChild(q), root.getChild(q).matrix, P_X);
				else
					 spin(root.getChild(q), root.getChild(q).matrix, P_O); 
			}			
		} 
		
		return 0;
	}
	
	
	private int countZero(Matrix matrix) {
		int zeroes = 0;
		for(int i=0; i< matrix.getRowDimension(); i++){
			for(int j=0; j < matrix.getColumnDimension(); j++){
   			 	if(matrix.get(i, j) == 0.0)
   			 		zeroes++;
   		 	}
   		 }
		return zeroes;
	}
	

	
	private int validate(Matrix m) {
		float x,y,z;
		//Check Rows for Wins
		x = (float) m.get(0,0);
		y = (float) m.get(0,1);
		z = (float) m.get(0,2);
		if(x+y+z == (3.0 * player)){
			return 1;
		}
		x = (float) m.get(1,0);
		y = (float) m.get(1,1);
		z = (float) m.get(1,2);
		if(x+y+z == (3.0 * player)){
			return 1;
		}
		x = (float) m.get(2,0);
		y = (float) m.get(2,1);
		z = (float) m.get(2,2);
		if(x+y+z == (3.0 * player)){
			return 1;
		}
		//Check Columns for Wins
		x = (float) m.get(0,0);
		y = (float) m.get(1,0);
		z = (float) m.get(2,0);
		if(x+y+z == (3.0 * player)){
			return 1;
		}
		x = (float) m.get(0,1);
		y = (float) m.get(1,1);
		z = (float) m.get(2,1);
		if(x+y+z == (3.0 * player)){
			return 1;
		}
		x = (float) m.get(0,2);
		y = (float) m.get(1,2);
		z = (float) m.get(2,2);
		if(x+y+z == (3.0 * player)){
			return 1;
		}
		//Check Diaganals for Wins
		x = (float) m.get(0,0);
		y = (float) m.get(1,1);
		z = (float) m.get(2,2);
		if(x+y+z == (3.0 * player)){
			return 1;
		}
		x = (float) m.get(0,2);
		y = (float) m.get(1,1);
		z = (float) m.get(2,0);
		if(x+y+z == (3.0 * player)){
			return 1;
		}
		//Not a Winner Sorry Buddy
		return 0;
	}
	
	private int validate(Matrix m, int who) {
		float x,y,z;
		//Check Rows for Wins
		x = (float) m.get(0,0);
		y = (float) m.get(0,1);
		z = (float) m.get(0,2);
		if(x+y+z == (3.0 * who)){
			return 1;
		}
		x = (float) m.get(1,0);
		y = (float) m.get(1,1);
		z = (float) m.get(1,2);
		if(x+y+z == (3.0 * who)){
			return 1;
		}
		x = (float) m.get(2,0);
		y = (float) m.get(2,1);
		z = (float) m.get(2,2);
		if(x+y+z == (3.0 * who)){
			return 1;
		}
		//Check Columns for Wins
		x = (float) m.get(0,0);
		y = (float) m.get(1,0);
		z = (float) m.get(2,0);
		if(x+y+z == (3.0 * who)){
			return 1;
		}
		x = (float) m.get(0,1);
		y = (float) m.get(1,1);
		z = (float) m.get(2,1);
		if(x+y+z == (3.0 * who)){
			return 1;
		}
		x = (float) m.get(0,2);
		y = (float) m.get(1,2);
		z = (float) m.get(2,2);
		if(x+y+z == (3.0 * who)){
			return 1;
		}
		//Check Diaganals for Wins
		x = (float) m.get(0,0);
		y = (float) m.get(1,1);
		z = (float) m.get(2,2);
		if(x+y+z == (3.0 * who)){
			return 1;
		}
		x = (float) m.get(0,2);
		y = (float) m.get(1,1);
		z = (float) m.get(2,0);
		if(x+y+z == (3.0 * who)){
			return 1;
		}
		//Not a Winner Sorry Buddy
		return 0;
	}

	
}
