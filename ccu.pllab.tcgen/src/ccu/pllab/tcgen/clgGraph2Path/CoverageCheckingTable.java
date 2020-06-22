package ccu.pllab.tcgen.clgGraph2Path;

  
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class CoverageCheckingTable implements Map{

	private Map<Object, Integer> coverageCheckingTable;
	
	public CoverageCheckingTable(){
		this.coverageCheckingTable = new HashMap<Object, Integer>();
	}


	@Override
	public boolean isEmpty() {
		return this.coverageCheckingTable.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.coverageCheckingTable.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		
		return this.coverageCheckingTable.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return this.coverageCheckingTable.get(key);
	}

	@Override
	public Object put(Object key, Object value) {
		return this.coverageCheckingTable.put(key, (Integer)value);
	}

	@Override
	public Object remove(Object key) {
		return this.coverageCheckingTable.remove(key);
	}

	@Override
	public void clear() {
		this.coverageCheckingTable.clear();
		
	}

	@Override
	public Set keySet() {
		return this.coverageCheckingTable.keySet();
	}

	@Override
	public Collection values() {
		
		return this.coverageCheckingTable.values();
	}

	@Override
	public Set entrySet() {
		return this.coverageCheckingTable.entrySet();
	}

	@Override
	public void putAll(Map m) {
		this.coverageCheckingTable.putAll(m);
	}


	@Override
	public int size() {
		return this.coverageCheckingTable.size();
	}

}
