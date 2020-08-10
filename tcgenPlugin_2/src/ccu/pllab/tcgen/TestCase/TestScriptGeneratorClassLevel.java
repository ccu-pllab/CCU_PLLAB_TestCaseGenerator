package ccu.pllab.tcgen.TestCase;


import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import ccu.pllab.tcgen.DataWriter.DataWriter;

public class TestScriptGeneratorClassLevel {
	private List<TestData> testDatas;
	public TestScriptGeneratorClassLevel() {

	}
	public void init(List<TestData> tds) {
		this.testDatas = tds;
	}
  
	//----
	public String genTestCase(List<TestDataClassLevel> data, String packagePosition, String packageName, ArrayList<String> att){
		String str="";
		str += "package ccu.pllab.tcgen.TCGenExample715.TestSuites715;\n";
		str += "import "+packagePosition+";\n"+
			"import junit.framework.TestCase;\n\n";
		str += "public class test"+packageName+" extends TestCase {\n";
		
		for(int i=0;i<data.size();i++){ 
			ArrayList<Object> methodname=data.get(i).getMethodName();
			str += "\tpublic void test"+packageName+"Class"+(i+1)+"(){\n";
			int count=0;
			for(int i1=0;i1<methodname.size();i1++){
				if(i1==0){ //constructor
					str += "\t\t"+methodname.get(i1)+" coffee = new "+methodname.get(i1)+"(";
					ArrayList<Object> prearg = data.get(i).getArgPre();
					ArrayList<Object> inprearg = (ArrayList<Object>)prearg.get(i1);
					if(!inprearg.isEmpty()){
						for(int argp=0;argp<inprearg.size();argp++){
							if(inprearg.get(argp) instanceof Integer){
								str += inprearg.get(argp);
							}
							else if(inprearg.get(argp) instanceof String){
								str += "\""+inprearg.get(argp)+"\"";
							}
							if(inprearg.size()!=1 && argp!=(inprearg.size()-1)) str+=",";
						}
					}
					str += ");\n";
					
					ArrayList<Object> poststate = data.get(i).getObjPost();
					ArrayList<Object> posts1 = (ArrayList<Object>)poststate.get(0);

					for(int statep=0;statep<posts1.size();statep++){
						if(posts1.get(statep) instanceof Integer){
							/*str += "\t\tObject o"+count+"= "+posts1.get(statep)+";\n"; 
							//str += "\t\tassertTrue("+"o"+count+".equals(coffee.getmoney()));\n";
							str += "\t\tassertTrue("+"o"+count+".equals(coffee.get"+att.get(statep)+"()));\n";
							*/count++;
							str += "\t\tassertEquals("+posts1.get(statep)+",coffee.get"+att.get(statep)+"());\n";
						}
						else if(posts1.get(statep) instanceof String){
							//str += "\t\tassertTrue(\""+posts1.get(statep)+"\".equals(coffee.getmoney()));\n";
							//str += "\t\tassertTrue(\""+posts1.get(statep)+"\".equals(coffee.get"+att.get(statep)+"()));\n";
							str += "\t\tassertEquals(\""+posts1.get(statep)+"\",coffee.get"+att.get(statep)+"());\n";
						}
					}
				}//end if i1=0
				else{
					ArrayList<Object> prearg = data.get(i).getArgPre();
					ArrayList<Object> inprearg = (ArrayList<Object>)prearg.get(i1);
					
					ArrayList<Object> ret = data.get(i).getRetVal();
					ArrayList<Object> inret = (ArrayList<Object>)ret.get(i1);
					
					if(inret.isEmpty()){
						if(!inprearg.isEmpty()){
							for(int argp=0;argp<inprearg.size();argp++){
								if(inprearg.get(argp) instanceof Integer){
									//str += "\t\tcoffee."+methodname.get(i1)+"("+inprearg.get(0)+");\n";
									str += "\t\tcoffee."+methodname.get(i1)+"("+inprearg.get(argp);
								}
								else if(inprearg.get(argp) instanceof String){
									//str += "\t\tcoffee."+methodname.get(i1)+"(\""+inprearg.get(0)+"\");\n";
									str += "\t\tcoffee."+methodname.get(i1)+"(\""+inprearg.get(argp)+"\"";
								}
								if(inprearg.size()!=1 && argp!=(inprearg.size()-1)) str+=",";
							}
							str += ");\n";
						}
						else{
							str += "\t\tcoffee."+methodname.get(i1)+"();\n";
						}
					}
					else{//has return val
						for(int inc=0;inc<inret.size();inc++){
							if(inret.get(inc) instanceof Integer){
								//str += "\t\tObject o"+count+" = "+inret.get(inc)+";\n"; 
								//str += "\t\tassertTrue("+"o"+count+".equals("; count++;
								str += "\t\tassertEquals("+inret.get(inc)+","; count++;
							}
							else if(inret.get(inc) instanceof String){
								//str += "\t\tObject o = \""+inret.get(0)+"\";\n";
								//str += "\t\tassertTrue(\""+inret.get(inc)+"\".equals(";
								str += "\t\tassertEquals(\""+inret.get(inc)+"\",";
							}
							if(!inprearg.isEmpty()){
								/*if(inprearg.get(0) instanceof Integer){
									str += "coffee."+methodname.get(i1)+"("+inprearg.get(0)+")));\n";
								}
								else if(inprearg.get(0) instanceof String){
									str += "coffee."+methodname.get(i1)+"(\""+inprearg.get(0)+"\")));\n";
								}*/
								for(int argp=0;argp<inprearg.size();argp++){
									if(inprearg.get(argp) instanceof Integer){
										//str += "\t\tcoffee."+methodname.get(i1)+"("+inprearg.get(0)+");\n";
										str += "coffee."+methodname.get(i1)+"("+inprearg.get(argp);
									}
									else if(inprearg.get(argp) instanceof String){
										//str += "\t\tcoffee."+methodname.get(i1)+"(\""+inprearg.get(0)+"\");\n";
										str += "coffee."+methodname.get(i1)+"(\""+inprearg.get(argp)+"\"";
									}
									if(inprearg.size()!=1 && argp!=(inprearg.size()-1)) str+=",";
								}
								str += ");\n";
							}
							else{
								str += "coffee."+methodname.get(i1)+"());\n";
							}
						}//end for inc
					}
					
					ArrayList<Object> posts = data.get(i).getObjPost();
					ArrayList<Object> inposts = (ArrayList<Object>)posts.get(i1);
					for(int statep=0;statep<inposts.size();statep++){
						if(inposts.get(statep) instanceof String){
							//str += "\t\tassertTrue(\""+posts1.get(statep)+"\".equals(coffee.getmoney()));\n";
							//str += "\t\tassertTrue(\""+inposts.get(statep)+"\".equals(coffee.get"+att.get(statep)+"()));\n";
							str += "\t\tassertEquals(\""+inposts.get(statep)+"\",coffee.get"+att.get(statep)+"());\n";
						}
						else if(inposts.get(statep) instanceof Integer){
							//str += "\t\tObject o"+count+" = "+inposts.get(statep)+";\n"; 
							//str += "\t\tassertTrue("+"o"+count+".equals(coffee.getmoney()));\n";
							//str += "\t\tassertTrue("+"o"+count+".equals(coffee.get"+att.get(statep)+"()));\n";
							count++;
							str += "\t\tassertEquals("+inposts.get(statep)+",coffee.get"+att.get(statep)+"());\n";
						}
					}
				}
			}//end methodname
			str += "\t}\n";
		}//end data
		str += "}";
		DataWriter.writeInfo(str, "test"+packageName, "java", DataWriter.output_folder_path, "TestSuites715");
		//DataWriter.writeFile(str, packageName, "TestSuites", ".java");
		return str;
	}
}
