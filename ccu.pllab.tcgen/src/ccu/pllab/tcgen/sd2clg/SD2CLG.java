package ccu.pllab.tcgen.sd2clg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.ParseException;
import org.json.JSONException;
import org.xml.sax.SAXException;

import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
//import ccu.pllab.tcgen.home_sd2clg.CLGRF2;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;
 


public class SD2CLG {
	
	public SD2CLG(){
		
	}
	public CLGGraph convert(StateDigram statedigram) throws SAXException, IOException, ParserConfigurationException, TemplateException, ModelAccessException, ParseException, JSONException, tudresden.ocl20.pivot.parser.ParseException{
//		System.out.println("inConvert"); //uml_model_path
		String clgstr = null; //.dot 
		CLGGraph SD = null;
		CLGGraph  SG, Guard = null;	//SD=Total CLGGrapgh, SG=Sub CLGGrapgh, Guard=Sub Guard CLGGrapgh
		SD = new CLGGraph(statedigram.getStates().size());//1 start, 2final
		//System.out.println("statesize!"+statedigram.getStates().size());
		//System.out.println("gettranssize!"+statedigram.getTransitions().size());
		for(int i=0;i<statedigram.getTransitions().size();i++){
			Transition tr =statedigram.getTransitions().get(i);
			//System.out.println("source: "+tr.getSource().getName()+"id= "+tr.getSource().getId());
			//System.out.println("target: "+tr.getTarget().getName()+"id= "+tr.getTarget().getId());
			if(tr.getGuard()!=null){
				Guard = new CLGGraph(tr.getGuard());
				if(tr.getMethod()==null)
					SG=new CLGGraph();
				else
					SG=new CLGGraph(tr.getMethod());
				Guard.graphAnd(SG);
				int source=0; int target=0;
				if(tr.getSource() instanceof InitialState)
					source=1;
				else source = tr.getSource().getId();
				if(tr.getTarget() instanceof FinalState)
					target=2;
				else target = tr.getTarget().getId();
				
				SD.graphInsert(source, Guard, target);
			}
			else{
				if(tr.getMethod()==null)
					SG=new CLGGraph();
				else
					SG=new CLGGraph(tr.getMethod());
				int source=0; int target=0;
				if(tr.getSource() instanceof InitialState)
					source=1;
				else source = tr.getSource().getId();
				if(tr.getTarget() instanceof FinalState)
					target=2;
				else target = tr.getTarget().getId();
				SD.graphInsert(source, SG, target);
			}
		}
		//+--------
		/*
		for(int i=0;i<statedigram.getstate().size();i++){
			for(int j=0;j<statedigram.getstate().get(i).gettransition().size();j++){
				if(statedigram.getstate().get(i).gettransition().get(j).getguard()!=null){
					Guard = new CLGGraph(statedigram.getstate().get(i).gettransition().get(j).getguard());
					if(statedigram.getstate().get(i).gettransition().get(j).getmethod()==null)
						SG=new CLGGraph();
					else
						SG=new CLGGraph(statedigram.getstate().get(i).gettransition().get(j).getmethod());
					Guard.graphAnd(SG);
					int source = statedigram.getstate().get(i).gettransition().get(j).getsource().getid();
					int target = statedigram.getstate().get(i).gettransition().get(j).gettarget().getid();
					SD.graphInsert(source, Guard, target);
				}
				else{
					if(statedigram.getstate().get(i).gettransition().get(j).getmethod()==null)
						SG=new CLGGraph();
					else
						SG=new CLGGraph(statedigram.getstate().get(i).gettransition().get(j).getmethod());
					int source = statedigram.getstate().get(i).gettransition().get(j).getsource().getid();
					int target = statedigram.getstate().get(i).gettransition().get(j).gettarget().getid();
					SD.graphInsert(source, SG, target);
				}
			}
		}*/
		clgstr=SD.graphDraw();
//		//clgstr="123";
//		//System.out.println("HI ~ "+clgstr);
//		
//		
		File dir = new File("${project_loc}/../../Examples/"+statedigram.getSDName()+"CLG"); 
		if(dir.isDirectory())  { System.out.println("有資料夾"); }
		else { dir.mkdir(); System.out.println("no dir"); }   
		FileWriter dataFile = new FileWriter("${project_loc}/../../Examples/"+statedigram.getSDName()+"CLG/"+statedigram.getSDName()+"CLG.dot");
		BufferedWriter input = new BufferedWriter(dataFile);
		input.write(clgstr);
		input.close();
		new ProcessBuilder("dot", "-Tpng", "${project_loc}/../../Examples/"+statedigram.getSDName()+"CLG/"+statedigram.getSDName()+"CLG.dot",
				"-o", "${project_loc}/../../Examples/"+statedigram.getSDName()+"CLG/"+statedigram.getSDName()+"CLG.png").start();
		
		
		//--
		/*
		FileWriter dataFile = new FileWriter("${project_loc}/../"+statedigram.getSDName()+"CLG.dot");
		BufferedWriter input = new BufferedWriter(dataFile);
		input.write(clgstr);
		input.close();
		new ProcessBuilder("dot", "-Tpng", "${project_loc}/../"+statedigram.getSDName()+
				"CLG.dot", "-o", "${project_loc}/../"+statedigram.getSDName()+"CLG.png").start();
		*/
		((CLGStartNode)SD.getStartNode()).setClassName(statedigram.getSDName());
		((CLGStartNode)SD.getStartNode()).setMethodName(statedigram.getSDName());
		((CLGStartNode)SD.getStartNode()).setClassAttributes(statedigram.getSDAttribute());
		return SD;
	}
}