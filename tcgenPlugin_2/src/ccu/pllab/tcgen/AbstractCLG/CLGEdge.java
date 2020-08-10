package ccu.pllab.tcgen.AbstractCLG;


public class CLGEdge<F, S> extends java.util.AbstractMap.SimpleImmutableEntry<F, S> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3668975454L;

	public CLGEdge(F f, S s) {
		super(f, s);
	}

	public F get1stElement() {
		return this.getKey();
	}

	public S get2ndElement() {
		return this.getValue();
	}

	@Override
	public String toString() {
		return "{ " + this.get1stElement().toString() + "-> " + this.get2ndElement().toString() + "}";
	}

}
