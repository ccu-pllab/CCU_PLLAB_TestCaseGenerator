package ccu.pllab.tcgen.clgGraph2Path;

  
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ccu.pllab.tcgen.AbstractCLG.*;

public class CLGPath {
	private static int path_count = 1;
	private int pathId;
	private List<CLGNode> nodes;
	private Set<CLGEdge> edges; 
	
	public CLGPath(List<CLGNode> list) {
		this.nodes = new ArrayList<CLGNode>();
		this.edges = new HashSet<CLGEdge>();
		nodes.addAll(list);
		this.analyzeEdges();
		pathId = path_count++;
	}

	public List<CLGNode> getPathNodes() {
		return nodes;
	}

	public int toGetPathId() {
		return this.pathId;
	}

	public String toGetPathInfo() {
		String result = "";
		for (CLGNode n : nodes) {
			result += n.toString() + " ";
		}
		return "Path No." + this.toGetPathId() + " " + result;
	}


	private void analyzeEdges(){
		System.out.println("\n/******\nedges size:" + this.nodes.size());
		
		for(int i =0; i <this.nodes.size()-1;i++){
			final CLGEdge<CLGNode, CLGNode> node_pair = new CLGEdge<CLGNode, CLGNode>(this.nodes.get(i), this.nodes.get(i + 1));
			this.edges.add(node_pair);
		}
	}
	public Set<CLGEdge> getEdges(){
		
		return this.edges;
	}
	
	public String toPathImg(){
		String content="";
		List<CLGNode> pathImgNodes= new ArrayList<CLGNode>();
		for(CLGNode n: this.nodes){
			if(n instanceof CLGConstraintNode){
				pathImgNodes.add(n);
			}
		}
		content += String.format("digraph \"%s_%s_%d\" {\n", ((CLGStartNode)this.nodes.get(0)).getClassName(),  ((CLGStartNode)this.nodes.get(0)).getMethodName(),this.pathId);
		for(int i =0;i<pathImgNodes.size()-1;i++){
			content += pathImgNodes.get(i).getId() +"->"+pathImgNodes.get(i+1).getId()+"\n";
		}
		for(CLGNode n :pathImgNodes){
			content+=n.toGetImgInfo();
		}
		content+="\n}";
		System.out.println(content);
		return content;
	}
	
	
}

