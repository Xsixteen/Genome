package gen.Tree;

import java.util.ArrayList;

import Jama.Matrix;

import gen.Tree.*;

public class Node {
	public ArrayList<Node> children;
    public Node parent;
    public Matrix matrix;
    public int winners=0;
    public int numChildren=0;
    
    public Node() {
    	children = new ArrayList<Node>();
    }
    public Node getChild(int ith) {
    	return children.get(ith);
    }
    
    public void setChildren(Node child) {
    	children.add(child);
    	numChildren++;
    }
    public void setChild(int index, Node child) {
    	children.set(index, child);
    }
    public void removeChild(int index) {
    	children.remove(index);
    	numChildren --;
    }
    public void setMatrix(Matrix m) {
    	matrix = m.copy();
    }
}

