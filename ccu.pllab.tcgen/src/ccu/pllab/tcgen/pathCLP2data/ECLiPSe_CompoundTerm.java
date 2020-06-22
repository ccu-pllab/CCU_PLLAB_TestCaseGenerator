package ccu.pllab.tcgen.pathCLP2data;



import java.util.LinkedList;
import java.util.List;


import com.parctechnologies.eclipse.CompoundTerm;

public class ECLiPSe_CompoundTerm {
	private List<Object> terms;
	private Integer intValue;
	private String stringValue;
	private Object functorValue;

	public ECLiPSe_CompoundTerm() {
		terms = new LinkedList<Object>();
	}
 
	public ECLiPSe_CompoundTerm(Object compundTerm, int i) {
		terms = new LinkedList<Object>();
		if (compundTerm instanceof LinkedList) {
			if (((List) compundTerm).size() > 0) {
				LinkedList<Object> input_terms = (LinkedList<Object>) compundTerm;
				for (Object o : input_terms) {
					if (o instanceof String) {						
						terms.add("\"" + o + "\"");
					}else if(o ==null){
						/* nothing*/
					}
					else {						
						terms.add(o);
					}
				}
			}
		}
	}
 
	public ECLiPSe_CompoundTerm(Object compundTerm) {
		if (compundTerm instanceof LinkedList) {
			terms = new LinkedList<Object>();
			@SuppressWarnings("unchecked")
			LinkedList<Object> input_terms = (LinkedList<Object>) compundTerm;
			for (Object o : input_terms) {
				terms.add(new ECLiPSe_CompoundTerm(o));
			}
		} else if (compundTerm instanceof Integer) {
			intValue = (Integer) compundTerm;
		} else if (compundTerm instanceof String) {
			
			stringValue = "\"" + (String) compundTerm + "\"";
		} else {
			functorValue = compundTerm;
		}

	}

	@Override
	public String toString() {
		if (terms != null) {
//			if (terms.size() > 0
//					&& (terms.get(0).toString().equals("uml_obj") || terms.get(0).toString().equals("uml_asc"))) {
//				return terms.toString().replaceFirst("\\[", "(").substring(0, terms.toString().length() - 1) + ")";
//			} else {
			
				return terms.toString();
//			}
		} else if (intValue != null) {
			return intValue.toString();
		} else if (stringValue != null) {
			return stringValue;
		} else if (functorValue != null) {
			return ((CompoundTerm) functorValue).functor();
		} else {
			return "[]";
		}
	}



	public Integer getIntValue() {
		return this.intValue;
	}

	public String getStringValue() {
		return this.stringValue;
	}

	public List<Object> getTerm() {
		return this.terms;
	}
}
