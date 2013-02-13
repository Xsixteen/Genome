package gen;

import Jama.Matrix;

public class mList implements Comparable<mList> {
	private Matrix m; 
	private int wins;
	
	public mList(Matrix a, int b) {
		super();
		this.m = a;
		this.wins = b;
	}
	
	public void setMatrix(Matrix a) {
		m = a;
	}
	
	public Matrix getMatrix() {
		return m;
	}
	
	public int getWins() {
		return wins;
	}
	
	public void setWins(int a) {
		wins = a;
	}

	@Override
	public int compareTo(mList arg0) {
		int compare = ((mList) arg0).getWins();
		if(arg0.getWins() > this.getWins())
		{
			return -1;
		} else if(arg0.getWins() == this.getWins()) {
			return 0;
		} 
		return 1;
	}
}
