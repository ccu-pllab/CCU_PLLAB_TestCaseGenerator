package ccu.pllab.tcgen.libs.node;

 
public interface NodeVisitHandler<T extends INode> {
	public void visit(T node);
}