package ccu.pllab.tcgen.ecl2data;

 
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.parctechnologies.eclipse.EXDRInputStream;
import com.parctechnologies.eclipse.EclipseEngine;
import com.parctechnologies.eclipse.EclipseException;
import com.parctechnologies.eclipse.EmbeddedEclipse;
import com.parctechnologies.eclipse.FromEclipseQueue;

public class Ecl2Data {
	private EclipseEngine eclipse;

	private FromEclipseQueue eclipse2Java;
	private EXDRInputStream eclipse2JavaFormatted;

	protected Ecl2Data(EclipseEngine eclipse) {
		try {
			this.eclipse = eclipse;
			this.eclipse2Java = this.eclipse.getFromEclipseQueue("eclipse_to_java");
			this.eclipse2JavaFormatted = new EXDRInputStream(this.eclipse2Java);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public void compile(File eclFile) throws EclipseException, IOException {
		this.eclipse.compile(eclFile);
	}

	public void compile(String str) throws EclipseException, IOException {
		File clpFile = File.createTempFile("tcgen", ".ecl");
		clpFile.deleteOnExit();
		FileWriter writer = new FileWriter(clpFile);
		writer.write(str);
		writer.close();
		this.compile(clpFile);
	}

	public void Destroy() {
		try {
			((EmbeddedEclipse) this.eclipse).destroy();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private List<ECLiPSeCompoundTerm> getECLiPSeCompoundTermList(List<Object> eclipseOutputTerm) {
		List<ECLiPSeCompoundTerm> instance_list = new ArrayList<ECLiPSeCompoundTerm>();
		for (int i = 0; i < eclipseOutputTerm.size(); i++) {
			if (!eclipseOutputTerm.get(i).equals(Collections.emptyList())) {
				@SuppressWarnings("unchecked")
				LinkedList<Object> instance = (LinkedList<Object>) eclipseOutputTerm.get(i);
				instance_list.add(new ECLiPSeCompoundTerm(instance));
			} else {
				instance_list.add(new ECLiPSeCompoundTerm());
			}
		}
//		System.out.println(instance_list);
		return instance_list;
	}

	public void multiplexityValidation(final String pathName) throws SolvingFailException {
		String target_predicate = String.format("%s(Instances).", pathName);
		try {
			Ecl2Data.this.eclipse.rpc(target_predicate);
		} catch (Exception e) {
			throw new SolvingFailException(pathName + " multiplexityValidation");
		}
	}

	public List<List<ECLiPSeCompoundTerm>> solvingCSP(final String pathName, int timeout) throws SolvingTimeOutException, SolvingFailException, IOException {
		Exception eclipseException = null;
		List<List<ECLiPSeCompoundTerm>> return_instance = new ArrayList<List<ECLiPSeCompoundTerm>>();
		String target_predicate = String
				.format("timeout((%s(Pre, Vars, Post), write_exdr(eclipse_to_java, Pre),write_exdr(eclipse_to_java, Vars), write_exdr(eclipse_to_java, Post), flush(eclipse_to_java)), %d, write_exdr(eclipse_to_java, [[timeout]])).",
						pathName, timeout);
		String predicate = "timeout( ( testTriangleCategory(S_pre,Arg_pre,S,Arg,Result),write_exdr(eclipse_to_java,S_pre),write_exdr(eclipse_to_java,Arg_pre),write_exdr(eclipse_to_java,S),write_exdr(eclipse_to_java,Arg),write_exdr(eclipse_to_java,Result),flush(eclipse_to_java)),1,write_exdr(eclipse_to_java,[[timeout]])).";
		String pred="testTriangleCategory(S_pre,Arg_pre,S,Arg,Result)";
		try {
			
//			System.out.println(target_predicate);
			Ecl2Data.this.eclipse.rpc(target_predicate);
//			Ecl2Data.this.eclipse.rpc(predicate);
		} catch (Exception e) {
			eclipseException = e;
		}

		while (this.eclipse2JavaFormatted.available() > 0) {
			Object term = this.eclipse2JavaFormatted.readTerm();
//			System.out.println(this.eclipse2JavaFormatted);
//			System.out.println(term.toString());
			@SuppressWarnings("unchecked")
			List<ECLiPSeCompoundTerm> obj = getECLiPSeCompoundTermList(((List<Object>) term));
//			System.out.println("value: "+obj.toString());
			return_instance.add(obj);
		}
		if (return_instance.size() < 3) {
			if (eclipseException != null) {
				throw new SolvingFailException(pathName);
			} else {
				if (!return_instance.isEmpty()) {
					throw new SolvingTimeOutException(pathName, timeout);
				}
			}
		}
//		System.out.println("value: "+return_instance.toString());
		return return_instance;
	}
}
