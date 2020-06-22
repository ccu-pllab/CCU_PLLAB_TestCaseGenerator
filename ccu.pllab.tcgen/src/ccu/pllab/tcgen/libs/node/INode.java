package ccu.pllab.tcgen.libs.node;

 
import java.util.List;

import ccu.pllab.tcgen.graphviz.GraphVizable;

public interface INode extends Cloneable, GraphVizable {

	public abstract long getId();

	public abstract void addNextNode(INode node);

	public abstract void addPreviousNode(INode node);

	public abstract INode clone();

	public abstract List<INode> getNextNodes();

	public abstract List<INode> getPreviousNodes();

	public abstract void removeNextNode(INode node);

	public abstract void removePreviousNode(INode node);

	public abstract void replaceNextNode(INode target, INode new_node);

	public abstract void replacePreviousNode(INode target, INode new_node);

	public abstract void clearPreviousNodes();

	public abstract void clearNextNodes();

	@Override
	public abstract String toGraphViz();

}