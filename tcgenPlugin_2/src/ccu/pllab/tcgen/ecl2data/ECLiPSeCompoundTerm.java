package ccu.pllab.tcgen.ecl2data;


import java.util.LinkedList;
import java.util.List;

import org.stringtemplate.v4.ST;

import com.parctechnologies.eclipse.CompoundTerm;

public class ECLiPSeCompoundTerm {
	private List<Object> terms;
	private Integer intValue;
	private String stringValue;
	private Object functorValue;

	public ECLiPSeCompoundTerm() {
		terms = new LinkedList<Object>();
	}

	public ECLiPSeCompoundTerm(Object compundTerm) {
		if (compundTerm instanceof LinkedList) {
			terms = new LinkedList<Object>();
			@SuppressWarnings("unchecked")
			LinkedList<Object> input_terms = (LinkedList<Object>) compundTerm;
			for (Object o : input_terms) {
				terms.add(new ECLiPSeCompoundTerm(o));
			}
		} else if (compundTerm instanceof Integer) {
			intValue = (Integer) compundTerm;
		} else if (compundTerm instanceof String) {
			stringValue = (String) compundTerm;
		} else {
			functorValue = compundTerm;
		}

	}
 
	@Override
	public String toString() {
		if (terms != null) {
			if (terms.size() > 0 && (terms.get(0).toString().equals("uml_obj") || terms.get(0).toString().equals("uml_asc"))) {
				return terms.toString().replaceFirst("\\[", "(").substring(0, terms.toString().length() - 1) + ")";
			} else {
				return terms.toString();
			}
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

	public static String toParsableFormat(List<List<ECLiPSeCompoundTerm>> allList) {
		ST result_ST = new ST("PRE = <pre>\nARG = <arg>\nPOST = <post>");
		result_ST.add("pre", toOneListString(allList.get(0)));
		result_ST.add("arg", toVarsString(allList.get(1)));
		result_ST.add("post", toOneListString(allList.get(2)));
		return result_ST.render();

	}

	public static String toOneListString(List<ECLiPSeCompoundTerm> list) {
		ST list_ST = new ST("[<term>]");
		for (int i = 0; i < list.size(); i++) {
			list_ST.add("term", list.get(i));
			if (i < (list.size() - 1)) {
				list_ST.add("term", ", ");
			}
		}
		return list_ST.render();
	}

	private static String toVarsString(List<ECLiPSeCompoundTerm> list) {
		ST list_ST = new ST("[<term>]");

		if (((ECLiPSeCompoundTerm) list.get(0).terms.get(1)).getIntValue() != null) {
			ECLiPSeCompoundTerm return_int = ((ECLiPSeCompoundTerm) list.get(0).terms.get(1));
			list_ST.add("term", return_int.toString() + ", ");
		} else if (((ECLiPSeCompoundTerm) list.get(0).terms.get(1)).getCollectionValue() != null) {
			ECLiPSeCompoundTerm return_value = ((ECLiPSeCompoundTerm) list.get(0).terms.get(1));
			list_ST.add("term", return_value.toString() + ", ");
		} else if(((ECLiPSeCompoundTerm) list.get(0).terms.get(1)).getStringValue() != null){
			ECLiPSeCompoundTerm return_value = ((ECLiPSeCompoundTerm) list.get(0).terms.get(1));
			list_ST.add("term", "\""+return_value.toString()+ "\""+ ", ");
		} else {
			list_ST.add("term", "void, ");
		}
		for (int i = 1; i < list.size(); i++) {
			list_ST.add("term", list.get(i));
			if (i < (list.size() - 1)) {
				list_ST.add("term", ", ");
			}
		}
		return list_ST.render();
	}

	private List<Object> getCollectionValue() {
		return this.terms;
	}

	public Integer getIntValue() {
		return this.intValue;
	}
	
	public String getStringValue(){
		return this.stringValue;
	}
}
