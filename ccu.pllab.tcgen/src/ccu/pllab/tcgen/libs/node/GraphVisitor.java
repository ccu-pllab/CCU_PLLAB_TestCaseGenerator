package ccu.pllab.tcgen.libs.node;

 
public class GraphVisitor<T extends INode> {
	public enum TRAVERSAL_ORDER {
		PREORDER, POSTORDER;
	}

	private TRAVERSAL_ORDER order;
	private Frontier<T> frontier;

	public GraphVisitor(TRAVERSAL_ORDER order, Frontier<T> frontier) {
		this.order = order;
		this.frontier = frontier;
	}

	public void traverse(T node, NodeVisitHandler<T> handler) {
		frontier.addItem(node);

		while (frontier.size() > 0 && isNeedContinue()) {
			final T current_node = frontier.getNextItem();

			if (this.order.equals(TRAVERSAL_ORDER.PREORDER)) {
				handler.visit(current_node);
			}

			for (INode child : current_node.getNextNodes()) {
				@SuppressWarnings("unchecked")
				T t = (T) child;
				if (!frontier.contains(t)) {
					frontier.addItem(t);
				}

			}

			if (this.order.equals(TRAVERSAL_ORDER.POSTORDER)) {
				handler.visit(current_node);
			}
		}
	}

	public boolean isNeedContinue() {
		return true;
	}
}
