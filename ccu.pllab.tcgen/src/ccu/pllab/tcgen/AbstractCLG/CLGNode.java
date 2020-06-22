package ccu.pllab.tcgen.AbstractCLG;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/*** This class describe a constraint logic node.
* @author <b>PLLAB</b>
* @version 1.0 
*/
public abstract class CLGNode {
	
	/**
	 *Stand for the counts of node.
	 */
	private static int node_count = 0;
	/**
	 * Node set behind this node.
	 */
	private Set<CLGNode> successor;
	/**
	 * Node set after this node.
	 */
	private Set<CLGNode> predecessor;
	/**
	 * Endnode.
	 */
	private CLGNode endNode;
	/**
	 * Stand for the id of the node.
	 */
	private int id;
	/**
	 * Describe the node is visited or not.
	 */
	private boolean visited = false;
	/**
	 * Create the set of node behind and after this node.<br>
	 * After the set created, id+1, id taken from private integer node_count<br>
	 * Set endNode to itself.
	 */
	public CLGNode() {
		successor = new HashSet<CLGNode>();
		predecessor = new HashSet<CLGNode>();
		id = node_count++;
		endNode = this;
	}
	/**
	 * 這個方法將設為參數的點加入此點前面的點集合<br>
	 * 如果參數點後面的點集合不包含此點, 則將此點加入參數點的後面點集合<br>
	 * @param node 類別為CLGNode
	 * <pre>{@code 
	 * Example: 
	 * 		CLGNode a;
	 * 		CLGNode b;
	 * 		a.addPredecessor(b);
	 * }</pre>
	 */	
	public void addPredecessor(CLGNode node) {
		this.predecessor.add(node);
		if (!node.getSuccessor().contains(this)) {
			node.addSuccessor(this);
		}
	}
	/**
	 * 這個方法將設為參數的點加入此點後面的點集合<br>
	 * 如果參數點前面的點集合不包含此點, 則將此點加入參數點的前面點集合<br>
	 * @param node 類別為CLGNode
	  * @param node 類別為CLGNode
	 * <pre>{@code 
	 * Example: 
	 * 		CLGNode a;
	 * 		CLGNode b;
	 * 		a.addSuccessor(b);
	 * }</pre>
	 */
	public void addSuccessor(CLGNode node) {
		this.successor.add(node);
		if (!node.getPredecessor().contains(this)) {
			node.addPredecessor(this);
		}
	}
	/**
	 * 這個方法將設為參數的點從此點前面的點集合中去除<br>
	 * 若參數點後面的點集合包含此點. 則將此點從集合中去除<br>
	 * @param node 類別為CLGNode
	 */
	public void removePredecessor(CLGNode node) {
		this.predecessor.remove(node);
		if (node.getSuccessor().contains(this)) {
			node.getSuccessor().remove(this);
		}
	}
	
	/**
	 * 這個方法將設為參數的點從此點後面的點集合中去除<br>
	 * 若參數點前面的點集合包含此點. 則將此點從集合中去除<br>
	 * @param node 類別為CLGNode
	 */
	public void removeSuccessor(CLGNode node) {
		this.successor.remove(node);
		if (node.getPredecessor().contains(this)) {
			node.getPredecessor().remove(this);
		}
	}
	/**
	 * 此方法回傳此點前面的點集合
	 * @return 返回型態為List&lt;CLGNode&gt;
	 */
	public List<CLGNode> getPredecessor() {
		return new ArrayList<CLGNode>(this.predecessor);
	}
	/**
	 * 此方法回傳此點後面的點集合
	 * @return 返回型態為List&lt;CLGNode&gt;
	 */
	public List<CLGNode> getSuccessor() {
		return new ArrayList<CLGNode>(this.successor);
	}
	/**
	 * 此方法清除此點前面的點集合
	 */
	public void clearPredecessors() {
		this.predecessor.clear();
	}
	/**
	 *  此方法清除此點後面的點集合
	 */
	public void clearSuccessors() {
		this.successor.clear();
	}
	/**
	 * 此方法回傳此點的id值
	 * @return 返回型態為int
	 */
	public final int getId() {
		return this.id;
	}
	/**
	 * 將此作為參數的點設定為此點的endNode
	 * @param node 參數型態為CLGNode
	 */
	public void setEndNode(CLGNode node) {
		this.endNode = node;
	}
	/**
	 * 若此點的endNode為null值則回傳此點，否則回傳此點的endNode
	 * @return 返回型態為CLGNode
	 */
	public CLGNode getEndNode() {
		if (endNode == null) {
			return this;
		} else {
			return this.endNode;
		}
	}
	/**
	 * 此方法將此點的visited屬性設為true
	 */
	private void setVisitedTrue() {
		this.visited = true;
	}
	/**
	 * 此方法回傳字串值來描述這個點到下個點集合的連接方式
	 * @return
	 */
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
	/**
	 * 若此點的visited屬性為true，此方法將此點的visited屬性設為false<br>
	 * 以及將此點後面的點集合依遞迴處理把此點後面全部的點的visited屬性都設為false
	 */
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
	/**
	 * 將此點以字串方式表現
	 */
	public String toString(){
		return this.toString();
		
	}
	/**
	 * 此方法為抽象方法，回傳的字串為這個點的ImgInfo
	 * @return
	 */
	public abstract String toGetImgInfo();
	/**
	 * 提供CLP資訊
	 * @return 回傳型別為字串
	 */
	public abstract String toCLPInfo();
	/**
	 * 此方法為抽象方法，傳入類別名稱、方法名稱、類別屬性、函式參數等，返回函式的CLP
	 * @param className 參數型態為String
	 * @param methodName 參數型態為String
	 * @param classAttributes 參數型態為ArrayList
	 * @param methodParameters 參數型態為ArrayList
	 * @param localParameters 參數型態為ArrayList
	 * @param result 參數型態為String
	 * @return 回傳型態為ArrayList
	 */
	public abstract ArrayList genMethodCLP(String className, String methodName, ArrayList classAttributes, ArrayList methodParameters, ArrayList localParameters, String result);
	/**
	 * 此方法回傳此點
	 * 	@return 回傳值為CLGNode
	 */
	public  CLGNode clone(){
		return this;
	};
}
