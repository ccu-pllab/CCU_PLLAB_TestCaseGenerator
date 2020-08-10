package ccu.pllab.tcgen.AbstractCLG;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CLGNode implements Cloneable{
	private static int node_count = 0;
	private Set<CLGNode> successor;
	private Set<CLGNode> predecessor;
	private CLGNode endNode;
	private int id;
	private boolean visited = false;

	public CLGNode() {
		successor = new HashSet<CLGNode>();
		predecessor = new HashSet<CLGNode>();
		id = node_count++;
		endNode = this;
	}
	
	@Override
	public CLGNode clone() throws CloneNotSupportedException {
		CLGNode n = (CLGNode) super.clone();
		return n;
	}

	public void addPredecessor(CLGNode node) {
		this.predecessor.add(node);
		if (!node.getSuccessor().contains(this)) {
			node.addSuccessor(this);
		}
	}

	public void addSuccessor(CLGNode node) {
		this.successor.add(node);
		if (!node.getPredecessor().contains(this)) {
			node.addPredecessor(this);
		}
	}

	public void removePredecessor(CLGNode node) {
		this.predecessor.remove(node);
		if (node.getSuccessor().contains(this)) {
			node.getSuccessor().remove(this);
		}
	}

	public void removeSuccessor(CLGNode node) {
		this.successor.remove(node);
		if (node.getPredecessor().contains(this)) {
			node.getPredecessor().remove(this);
		}
	}

	public List<CLGNode> getPredecessor() {
		return new ArrayList<CLGNode>(this.predecessor);
	}

	public List<CLGNode> getSuccessor() {
		return new ArrayList<CLGNode>(this.successor);
	}

	public void clearPredecessors() {
		this.predecessor.clear();
	}

	public void clearSuccessors() {
		this.successor.clear();
	}

	public final int getId() {
		return this.id;
	}

	public void setEndNode(CLGNode node) {
		this.endNode = node;
	}

	public CLGNode getEndNode() {
		if (endNode == null) {
			return this;
		} else {
			return this.endNode;
		}
	}

	private void setVisitedTrue() {
		this.visited = true;
	}

	public String toGenImg() {
		String content = "";
		content += this.toGetImgInfo();
		if (!this.visited)
		{
			for (CLGNode node : this.getSuccessor()) {
				this.setVisitedTrue();
				content += this.getId() + "->" + node.getId() + "\n" + node.toGenImg();
				}
		}
		this.setVisitedTrue();
		return content;
	}

	public void setVisitFalse() {
		if (this.visited==true)
		{
			for (CLGNode node : this.getSuccessor()) {
				this.visited=false;
				node.setVisitFalse();
				}
		}
	}
	@Override
	public String toString(){
		return this.toString();
		
	}
	
	@Override
	public boolean equals(Object obj) {

		if(obj instanceof CLGNode) {
			CLGNode n = (CLGNode) obj;
			return this.endNode.toString().equals(n.endNode.toString());
		}
		return false;
	}
	
	public abstract String toGetImgInfo();

	public abstract String toCLPInfo();
	
	public abstract ArrayList genMethodCLP(String className, String methodName, ArrayList classAttributes, ArrayList methodParameters, ArrayList localParameters, String result);
	
//	public  CLGNode clone(){
//		return this;
//	};
}
