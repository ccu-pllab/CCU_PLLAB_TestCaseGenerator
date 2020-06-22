package ccu.pllab.tcgen.libs.node;

 
import java.util.Stack;

public class StackFrontier<T extends INode> implements Frontier<T> {

	private Stack<T> stack;

	public StackFrontier() {
		this.stack = new Stack<T>();
	}

	@Override
	public void addItem(T node) {
		stack.push(node);
	}

	@Override
	public T getNextItem() {
		return stack.pop();
	}

	@Override
	public int size() {
		return this.stack.size();
	}

	@Override
	public boolean contains(INode child) {
		return this.stack.contains(child);
	}

}
