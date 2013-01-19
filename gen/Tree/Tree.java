package gen.Tree;

import gen.Tree.*;

public class Tree{
    public Node root;
    public int countNodes(){
    	int c = 1;
    	Tree T = new Tree();
    	
		if(root.children.size() != 0) {
			for (Node s : root.children){
				T.root = s;
				c += T.countNodes();
			} 
		} else {
			c++;
		}
		
    
	return c;
    }
    public Tree() {
    	root = new Node();
    }
    
}