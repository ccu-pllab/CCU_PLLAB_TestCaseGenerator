package ccu.pllab.tcgen.sd2clp;


import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.EXDRInputStream;
import com.parctechnologies.eclipse.EclipseEngine;
import com.parctechnologies.eclipse.EclipseEngineOptions;
import com.parctechnologies.eclipse.EclipseException;
import com.parctechnologies.eclipse.EmbeddedEclipse;
import com.parctechnologies.eclipse.FromEclipseQueue;

import ccu.pllab.tcgen.ecl2data.ECLiPSeCompoundTerm;
 
public class ExecuteCLP {
	private ArrayList seq;
	private EclipseEngine eclipse;
	private FromEclipseQueue eclipse2Java;
	private EXDRInputStream eclipse2JavaFormatted;
	
	public ExecuteCLP() {
		 try {
			 
			EclipseEngineOptions eclipseEngineOptions = new EclipseEngineOptions(new File("C:/ECLiPSe"));
			eclipseEngineOptions.setUseQueues(false);

			eclipse = EmbeddedEclipse.getInstance(eclipseEngineOptions);
			
			this.eclipse2Java = this.eclipse.getFromEclipseQueue("eclipse_to_java");
			this.eclipse2JavaFormatted = new EXDRInputStream(this.eclipse2Java);
			seq = new ArrayList();
			
		 } 
		 catch (Exception e) {
			 throw new IllegalStateException(e);
		 }
	}

	
	public void destroy() {
		try {
			// Destroy the Eclipse process
		    ((EmbeddedEclipse) eclipse).destroy();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void compileCLP(File CLPfile){
		try {
			eclipse.compile(CLPfile);
		} 
		catch (EclipseException e) {	
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList query(String target, String sdName) {
		List<CompoundTerm> CallSeq = null ;
		List<CompoundTerm> ArgSeq = null ;
		try {
			//File dir = new File("../Examples/"+ sDName + "CLP/" + sDName + ".ecl");
			/*編譯基本函式呼叫*/
			File all = new File("../Examples/All.ecl");
			this.compileCLP(all);
			/*編譯要執行的sd2clp跟ocl2clp*/
			File dir = new File("../Examples/"+ sdName + "CLP");
			String[] filenames;
			if (dir.isDirectory()) {
				filenames = dir.list();
				for (int i = 0; i < filenames.length; i++) {
					File eclFile = new File("../Examples/"+ sdName + "CLP" + "/" + filenames[i]);
					this.compileCLP(eclFile);
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
//			CompoundTerm result  = eclipse.rpc("start("+target +", Result, FuncSeq)");
//			System.out.println(result.arg(3));
//			String[] arg3 = result.arg(3).toString().substring(1, result.arg(3).toString().length()-1).split(", ");
//			for(int i=0;i<arg3.length;i++){
//				System.out.println(arg3[i]);
//			}
		}
		catch (Exception e) {
		}
		return seq;
	}
}
