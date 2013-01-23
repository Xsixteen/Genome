package gen;
import gen.Tree.*;
import Jama.*;

public class Genesis {

	private static Matrix curBoard;
	private static Tree T;
	private int statistic = 0;
	final int P_X = 1;
	final int P_O = -1;
	
	public Genesis() {
		double[][] empty = {{0,0,0},{0,0,0},{0,0,0}}; 
		//initialize the current board to all 0.0
		curBoard = new Matrix (empty);
		
		//Create the Game Tree
		T = new Tree();
	
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
	
	//Generate against any matrix. 
	public Matrix generate(Matrix matrix) throws InterruptedException {
		int moves = countZero(matrix);
		spin(T.root, matrix, P_O);
		
		return matrix;
	}
	
	//Generate against the objects matrix
	public void generate() throws InterruptedException {
		int moves = countZero(curBoard);
		//Okay so now its O's turn. Leaving a possible of moves - 1 
		spin(T.root, curBoard, P_O);
	}
	
	//Spin means spin through recursion! Used to generate a game tree from possible open spaces. 
	private int spin(Node root, Matrix matrix, int Player) throws InterruptedException {
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
			if(countZero(root.getChild(q).matrix) != 0){
				if (Player == P_O)
					 spin(root.getChild(q), root.getChild(q).matrix, P_X);
				else
					 spin(root.getChild(q), root.getChild(q).matrix, P_O); 
			}			
		} 
		
		return 0;
	}
	
	public int getStats() {
		return statistic;
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
		if((x+y+z == 3.0) || (x+y+z == -3.0)){
			return 1;
		}
		x = (float) m.get(1,0);
		y = (float) m.get(1,1);
		z = (float) m.get(1,2);
		if(x+y+z == 3.0 || x+y+z == -3.0){
			return 1;
		}
		x = (float) m.get(2,0);
		y = (float) m.get(2,1);
		z = (float) m.get(2,2);
		if(x+y+z == 3.0 || x+y+z == -3.0){
			return 1;
		}
		//Check Columns for Wins
		x = (float) m.get(0,0);
		y = (float) m.get(1,0);
		z = (float) m.get(2,0);
		if(x+y+z == 3.0 || x+y+z == -3.0){
			return 1;
		}
		x = (float) m.get(0,1);
		y = (float) m.get(1,1);
		z = (float) m.get(2,1);
		if(x+y+z == 3.0 || x+y+z == -3.0){
			return 1;
		}
		x = (float) m.get(0,2);
		y = (float) m.get(1,2);
		z = (float) m.get(2,2);
		if(x+y+z == 3.0 || x+y+z == -3.0){
			return 1;
		}
		//Check Diaganals for Wins
		x = (float) m.get(0,0);
		y = (float) m.get(1,1);
		z = (float) m.get(2,2);
		if(x+y+z == 3.0 || x+y+z == -3.0){
			return 1;
		}
		x = (float) m.get(0,2);
		y = (float) m.get(1,1);
		z = (float) m.get(2,0);
		if(x+y+z == 3.0 || x+y+z == -3.0){
			return 1;
		}
		//Not a Winner Sorry Buddy
		return 0;
	}
	public void checkBoards(Node root) {
		for (int q=0; q < root.numChildren; q++) {
			if(root.getChild(q).numChildren > 0) {
					checkBoards(root.getChild(q));
			} else {
				if(validate(root.getChild(q).matrix) != 1) {
					root.removeChild(q);
				} else {
					root.winners++;
				}
			}
		}
	}
	
	public void countWins(Node root) {
		for (int q=0; q < root.numChildren; q++) {
			if(root.getChild(q).numChildren > 0) {
				root.winners = root.winners + root.getChild(q).winners;
				countWins(root.getChild(q));
				
			}
		}
	}
	public void showChildrenWins(Node root) {
		for (int q=0; q < root.numChildren; q++) {
			if(root.getChild(q).numChildren > 0){
				root.getChild(q).matrix.print(5, 2);
				System.out.println("Wins for Child Branch: " + root.getChild(q).winners);
			}
		}
			
	}
	
}
