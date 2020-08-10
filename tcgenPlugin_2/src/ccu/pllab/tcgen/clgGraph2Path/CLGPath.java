package ccu.pllab.tcgen.clgGraph2Path;

  
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.DataWriter.DataWriter;
import tcgenplugin_2.handlers.BlackBoxHandler;

public class CLGPath implements Cloneable{
	private static int path_count = 1;
	private int pathId;
	private List<CLGNode> nodes;
	private Set<CLGEdge> edges; 
	
	public CLGPath() {
		
	}
	
	public CLGPath(List<CLGNode> list) {
		this.nodes = new ArrayList<CLGNode>();
		this.edges = new HashSet<CLGEdge>();
		nodes.addAll(list);
		this.analyzeEdges();
		pathId = path_count++;
	}
	
	@Override
	public CLGPath clone() throws CloneNotSupportedException {
		CLGPath p = (CLGPath) super.clone();
		p.nodes = cloneList(this.nodes);
		return p;
		
	}
	
	public static List<CLGNode> cloneList(List<CLGNode> list) throws CloneNotSupportedException{
		List<CLGNode> clone = new ArrayList<CLGNode>(list.size());
		for(CLGNode n : list) clone.add(n.clone());
		
		return clone;
	}
	
	public void getHalfPathNode() {
		int mid = this.nodes.size()/2;
		this.nodes = this.nodes.subList(0, mid);
	}
	
	public void cutPathNode(int num) {
		this.nodes = this.nodes.subList(0, num);
	}
	
	public void removeAllConnectionNode() {
		List<CLGNode> tempN = new ArrayList<CLGNode>();
		
		for(CLGNode n : this.nodes) {
			if(n instanceof CLGConnectionNode) {
				
			}else {
				tempN.add(n);
			}
		}
		
		this.nodes = tempN;
	}
	
	public CLGNode getNode(int i) {
		return this.nodes.get(i);
	}
	
	public CLGNode getSecondLastNode() {
		return this.nodes.get(nodes.size()-2);
	}
	public void initPathCount() {
		path_count = 2;
	}
	public void initPathID() {
		this.pathId = 1;
	}
	public CLGNode getLastNode() {
		return this.nodes.get(nodes.size()-1);
	}
	
	public void addNode(CLGNode n) {
		this.nodes.add(n);
	}
	
	public void RemoveSecondLastNode() {
		this.nodes.remove(nodes.size()-2);
	}
	
	public void RemoveLastNode() {
		this.nodes.remove(nodes.size()-1);
	}
	
	public void RemoveNode(CLGNode n) {
		this.nodes.remove(n);
	}
	
	public void RemoveNodeByNum(int i) {
		this.nodes.remove(nodes.size()-i);
	}
	
	public Boolean CompareWithInfeasibleNodes(List<List<CLGNode>> SIPNodesList) {
		if(SIPNodesList == null) {
			return false;
		}
		
		for(List<CLGNode> SIPNodes : SIPNodesList) {
			int SIPNodesSize = SIPNodes.size();
			int equalNodes = 0;
			
			for(CLGNode n : SIPNodes) {
				if(this.nodes.contains(n)) {
					equalNodes++;
				}
			}
			
			if(equalNodes == SIPNodesSize) {
				return true;
			}
		}
		return false;
	}
	
	public List<CLGNode> getPathNodes() {
		return nodes;
	}
	
	public List<String> getPath(){
		List<String> tempList = new ArrayList<>();	
		for(int i=1; i<nodes.size()-1; i++) {
			tempList.add(nodes.get(i).toCLPInfo().replace("#", "") + ",");
		}
		
		return tempList;
	}
	
	public Map<String, Integer> getPathByMap(Hashtable<CLGNode, String> CLGht){
		Map<String, Integer> map = new HashMap<>();
		for(int i=1; i<nodes.size()-1; i++) {
			map.put(nodes.get(i).toCLPInfo().replace("#", "") + ",", 1);
//			map.put(CLGht.get(nodes.get(i)), 1);
		}
		
		return map;
	}
	
	public void setPathNodes(List<CLGNode> n) {
		this.nodes = n;
	}

	public int toGetPathId() {
		return this.pathId;
	}

	public String toGetPathInfo() {
		String result = "";
		for (CLGNode n : nodes) {
			result += n.toString() + " ";
		}
		return "Path " + this.toGetPathId() + " " + result;
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
	
	public void genCLGPathGraph(String graphMethodName) throws IOException {
		FileWriter dataFile;	
		File clgPathFolder = new File(DataWriter.testPath_output_path);
		if(!clgPathFolder.exists()) {
			clgPathFolder.mkdirs();
		}
		File clgPath = new File(clgPathFolder.getPath()+"/"+BlackBoxHandler.CurrentEditorName+graphMethodName+this.pathId+".dot");
//		DataWriter.writeInfo(clpContent, graphClassName + graphMethodName + "_" + pathNo, "ecl", DataWriter.output_folder_path, "ECL");
//		這邊借用DataWriter的output路徑
		
		dataFile = new FileWriter(clgPath.getPath());
//		dataFile = new FileWriter("${eclipse_home}../../examples/output/CLG"+number+".dot");
		BufferedWriter input = new BufferedWriter(dataFile);
		input.write(this.toPathImg());
		input.close();
						
		
		new ProcessBuilder("dot", "-Tpng", clgPath.getPath(),
		"-o", clgPathFolder+"/"+BlackBoxHandler.CurrentEditorName+graphMethodName+pathId+".png").start();
	}
	
}

