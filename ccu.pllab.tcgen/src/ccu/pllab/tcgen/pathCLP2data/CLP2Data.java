package ccu.pllab.tcgen.pathCLP2data;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.EXDRInputStream;
import com.parctechnologies.eclipse.EclipseEngine;
import com.parctechnologies.eclipse.EclipseException;
import com.parctechnologies.eclipse.EmbeddedEclipse;
import com.parctechnologies.eclipse.FromEclipseQueue;

import ccu.pllab.tcgen.ecl2data.Ecl2Data;

public class CLP2Data {
	private EclipseEngine eclipse;

	private FromEclipseQueue eclipse2Java;
	private EXDRInputStream eclipse2JavaFormatted;

	protected CLP2Data(EclipseEngine eclipse) {
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

	private ECLiPSe_CompoundTerm getECLiPSeCompoundTerm(Object eclipseOutputTerm) {
		ECLiPSe_CompoundTerm obj = new ECLiPSe_CompoundTerm(eclipseOutputTerm,1);
		return obj;
	}
	
	private List<ECLiPSe_CompoundTerm> getECLiPSeCompoundTermList_NEW(List<Object> eclipseOutputTerm) {
		List<ECLiPSe_CompoundTerm> instance_list = new ArrayList<ECLiPSe_CompoundTerm>();

		if (!eclipseOutputTerm.equals(Collections.emptyList())) {
//			System.out.println(eclipseOutputTerm.toString());
			@SuppressWarnings("unchecked")
			LinkedList<Object> instance = (LinkedList<Object>) eclipseOutputTerm;

			instance_list.add(new ECLiPSe_CompoundTerm(instance));
		} else {
			instance_list.add(new ECLiPSe_CompoundTerm());
		}

		System.out.println("instance_list: " + instance_list);
		return instance_list;
	}

	private List<ECLiPSe_CompoundTerm> getECLiPSeCompoundTermList(List<Object> eclipseOutputTerm) {
		List<ECLiPSe_CompoundTerm> instance_list = new ArrayList<ECLiPSe_CompoundTerm>();
		for (int i = 0; i < eclipseOutputTerm.size(); i++) {
			if (!eclipseOutputTerm.get(i).equals(Collections.emptyList())) {
				@SuppressWarnings("unchecked")
				LinkedList<Object> instance = (LinkedList<Object>) eclipseOutputTerm.get(i);

				instance_list.add(new ECLiPSe_CompoundTerm(instance));
			} else {
				instance_list.add(new ECLiPSe_CompoundTerm());
			}
		}

		return instance_list;
	}

	public void multiplexityValidation(final String pathName) throws SolvingFailException {
		String target_predicate = String.format("%s(Instances).", pathName);
		try {
			CLP2Data.this.eclipse.rpc(target_predicate);
		} catch (Exception e) {
			throw new SolvingFailException(pathName + " multiplexityValidation");
		}
	}

	public List<List<ECLiPSe_CompoundTerm>> solvingCSP(final String pathName, int timeout)
			throws SolvingTimeOutException, SolvingFailException, IOException {
		Exception eclipseException = null;
		List<List<ECLiPSe_CompoundTerm>> return_instance = new ArrayList<List<ECLiPSe_CompoundTerm>>();
		String target_predicate = String.format(
				"timeout((%s(Pre, Vars, Post), write_exdr(eclipse_to_java, Pre),write_exdr(eclipse_to_java, Vars), write_exdr(eclipse_to_java, Post), flush(eclipse_to_java)), %d, write_exdr(eclipse_to_java, [[timeout]])).",
				pathName, timeout);
		String predicate = "timeout( ( testTriangleCategory(S_pre,Arg_pre,S,Arg,Result),write_exdr(eclipse_to_java,S_pre),write_exdr(eclipse_to_java,Arg_pre),write_exdr(eclipse_to_java,S),write_exdr(eclipse_to_java,Arg),write_exdr(eclipse_to_java,Result),flush(eclipse_to_java)),1,write_exdr(eclipse_to_java,[[timeout]])).";
		String pred = "testTriangleCategory(S_pre,Arg_pre,S,Arg,Result)";
		try {

			System.out.println(predicate);
			// Ecl2Data.this.eclipse.rpc(target_predicate);
			CLP2Data.this.eclipse.rpc(predicate);
		} catch (Exception e) {
			//e.printStackTrace();
			eclipseException = e;
		}

		while (this.eclipse2JavaFormatted.available() > 0) {
			Object term = this.eclipse2JavaFormatted.readTerm();
			System.out.println(this.eclipse2JavaFormatted);
			// for(int i=0;i< ((List<Object>) term).size();i++)
			// System.out.println( ((List<Object>) term).get(i) );
			getECLiPSeCompoundTermList_NEW(((List<Object>) term));
			// getECLiPSeCompoundTermList(((List<Object>) term));
			// @SuppressWarnings("unchecked")
			List<ECLiPSe_CompoundTerm> obj = getECLiPSeCompoundTermList_NEW(((List<Object>) term));
			// List<ECLiPSeCompoundTerm> obj =
			// getECLiPSeCompoundTermList(((List<Object>) term));
			// System.out.println("value: "+obj.toString());
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
		System.out.println("value: " + return_instance.toString());

		return return_instance;
	}

	// public List<List<ECLiPSeCompoundTerm>> solvingCSP_new(final String
	// pathName, int timeout) throws SolvingTimeOutException,
	// SolvingFailException, IOException {
	
	public List<List<ECLiPSe_CompoundTerm>> solvingCSP_new(final String pathName, String objPre, String argPre,
			String objPost, String argPost, String retVal, int timeout)
			throws IOException, SolvingFailException, SolvingTimeOutException {
		Exception eclipseException = null;
		List<List<ECLiPSe_CompoundTerm>> return_instance = new ArrayList<List<ECLiPSe_CompoundTerm>>();
		// String predicate = "timeout( (
		// testTriangleCategory(S_pre,Arg_pre,S,Arg,Result),write_exdr(eclipse_to_java,S_pre),write_exdr(eclipse_to_java,Arg_pre),write_exdr(eclipse_to_java,S),write_exdr(eclipse_to_java,Arg),write_exdr(eclipse_to_java,Result),flush(eclipse_to_java)),1,write_exdr(eclipse_to_java,[[timeout]])).";
		String predicate = String.format(
				"timeout( ( %s(%s,%s,%s,%s,%s),write_exdr(eclipse_to_java,%s),write_exdr(eclipse_to_java,%s),write_exdr(eclipse_to_java,%s),write_exdr(eclipse_to_java,%s),write_exdr(eclipse_to_java,%s),flush(eclipse_to_java)),1,write_exdr(eclipse_to_java,%d)).",
				pathName, objPre, argPre, objPost, argPost, retVal, objPre, argPre, objPost, argPost, retVal, timeout);

		try {
//			System.out.println(predicate);
			CLP2Data.this.eclipse.rpc(predicate);
		} catch (Exception e) {
		//	e.printStackTrace();
			eclipseException = e;
		}

		while (this.eclipse2JavaFormatted.available() > 0) {
			Object term = this.eclipse2JavaFormatted.readTerm();
//			System.out.println(this.eclipse2JavaFormatted);
			// getECLiPSeCompoundTermList_NEW(((List<Object>) term));
			@SuppressWarnings("unchecked")
			List<ECLiPSe_CompoundTerm> obj = getECLiPSeCompoundTermList_NEW(((List<Object>) term));

			return_instance.add(obj);
		}
		if (return_instance.size() < 5) {
			if (eclipseException != null) {
				throw new SolvingFailException(pathName);
			} else {
				if (!return_instance.isEmpty()) {
					throw new SolvingTimeOutException(pathName, timeout);
				}
			}
		}
		System.out.println("value: " + return_instance.toString());

		return return_instance;
	}
	  
	
	public List<ECLiPSe_CompoundTerm> solvingCSP_term(final String pathName, String objPre, String argPre,
			String objPost, String argPost, String retVal, int timeout)
			throws IOException, SolvingFailException, SolvingTimeOutException {
		Exception eclipseException = null;
		List<ECLiPSe_CompoundTerm> return_instance = new ArrayList<ECLiPSe_CompoundTerm>();
		String predicate = String.format(
				"timeout( ( %s(%s,%s,%s,%s,%s,%s),write_exdr(eclipse_to_java,%s),write_exdr(eclipse_to_java,%s),write_exdr(eclipse_to_java,%s),write_exdr(eclipse_to_java,%s),write_exdr(eclipse_to_java,%s),write_exdr(eclipse_to_java,%s),flush(eclipse_to_java)),1,write_exdr(eclipse_to_java,%d)).",
				pathName, objPre, argPre, objPost, argPost, retVal, "Exception",objPre, argPre, objPost, argPost, retVal, "Exception",timeout);

		try {
			CLP2Data.this.eclipse.rpc(predicate);
		} catch (Exception e) {
			//e.printStackTrace();
			eclipseException = e;
		}

		while (this.eclipse2JavaFormatted.available() > 0) {
			Object term = this.eclipse2JavaFormatted.readTerm();
			
		//	System.out.println("term : "+( term.getClass()) );
			
			@SuppressWarnings("unchecked")
			ECLiPSe_CompoundTerm obj = getECLiPSeCompoundTerm((List<Object>)term);
			
			return_instance.add(obj);
			System.out.println("obj "+obj.toString());
		}
		if (return_instance.size() < 5) {
			if (eclipseException != null) {
				throw new SolvingFailException(pathName);
			} else {
				if (!return_instance.isEmpty()) {
					throw new SolvingTimeOutException(pathName, timeout);
				}
			}
		}
	
		System.out.println("term value: " + return_instance.toString() + " size: " +return_instance.size());
		return return_instance;
	}

	//--714
		public List<ECLiPSe_CompoundTerm> solvingCSP_new(String pathName, int timeout)
				throws IOException, SolvingFailException, SolvingTimeOutException {
			List<ECLiPSe_CompoundTerm> return_instance = new ArrayList<ECLiPSe_CompoundTerm>();
			String predicate = pathName;
			try {
				CompoundTerm com=CLP2Data.this.eclipse.rpc(predicate);
				for(int i=1;i<=com.arity();i++){
					Object b = ((LinkedList)com.arg(i)); 
					ECLiPSe_CompoundTerm obj = new ECLiPSe_CompoundTerm(b,1);
					return_instance.add(obj);
				}
			} catch (EclipseException e) {
				return_instance.add(null); 
				//System.out.println("infeasiblePath "+return_instance);
			} catch (IOException e) {
				//e.printStackTrace();
				
			}
			return return_instance;
		}
		
		public ArrayList preambleQuery(String target, String sdName) {
			ArrayList seq = new ArrayList();
			List<CompoundTerm> CallSeq = null ;
			List<CompoundTerm> ArgSeq = null ;
			try {
				//File dir = new File("../Examples/"+ sDName + "CLP/" + sDName + ".ecl");
				/*編譯基本函式呼叫*/
				File all = new File("../Examples/All.ecl");
				this.compile(all);
				/*編譯要執行的sd2clp跟ocl2clp*/
				File dir = new File("../Examples/"+ sdName + "CLP");
				String[] filenames;
				if (dir.isDirectory()) {
					filenames = dir.list();
					for (int i = 0; i < filenames.length; i++) {
						File eclFile = new File("../Examples/"+ sdName + "CLP" + "/" + filenames[i]);
						//System.out.println("*************"+eclFile.getName());
						this.compile(eclFile);
					}
				}
				/*執行query*/
				String target_predicate = String
						.format(sdName.substring(0,1).toLowerCase()+sdName.substring(1)+"Preamble(%s, FuncSeq, ArgSeq), write_exdr(eclipse_to_java ,FuncSeq), write_exdr(eclipse_to_java ,ArgSeq).",
								target);
				eclipse.rpc(target_predicate);
				// get query result
				CallSeq = (List<CompoundTerm>) this.eclipse2JavaFormatted.readTerm();
				ArgSeq = (List<CompoundTerm>) this.eclipse2JavaFormatted.readTerm();
				// merge CallSeq & ArgSeq
				seq.clear();
				for(int i = 0;i < CallSeq.size();i++) {
					seq.add(CallSeq.get(i).functor() + ArgSeq.get(i));
				}
				// fix [] to ()
				for(int j = 0;j < seq.size();j++) {
					String s = (String) seq.get(j);
					s = s.replace("[", "(");
					s = s.replace("]", ")");
					seq.set(j, s);
				}
			}
			catch (Exception e) {
			}
			return seq;
		}

}
