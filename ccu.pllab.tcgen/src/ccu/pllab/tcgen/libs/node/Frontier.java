package ccu.pllab.tcgen.libs.node;

 
public interface Frontier<T> {
	public void addItem(T node);

	public T getNextItem();

	public int size();

	public boolean contains(T child);
}
