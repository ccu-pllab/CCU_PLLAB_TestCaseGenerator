package ccu.pllab.tcgen.libs.node;

 
import java.util.LinkedList;
import java.util.Queue;

public class QueueFrontier<T extends INode> implements Frontier<T> {
	private Queue<T> queue;

	public QueueFrontier() {
		this.queue = new LinkedList<T>();
	}

	@Override
	public void addItem(T node) {
		queue.add(node);
	}

	@Override
	public T getNextItem() {
		return queue.poll();
	}

	@Override
	public int size() {
		return this.queue.size();
	}

	@Override
	public boolean contains(INode child) {
		return this.queue.contains(child);
	}

}
