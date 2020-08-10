package ccu.pllab.tcgen.TestCase;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.omg.PortableServer.ServantActivator;
import org.stringtemplate.v4.*;
import org.xml.sax.SAXException;

import com.parctechnologies.eclipse.EclipseException;

import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.ecl2data.Ecl2Data;
import ccu.pllab.tcgen.ecl2data.Ecl2DataFactory;
import ccu.pllab.tcgen.exe.main.Main;
import ccu.pllab.tcgen.pathCLP2data.CLP2Data;
import ccu.pllab.tcgen.pathCLP2data.CLP2DataFactory;
import ccu.pllab.tcgen.sd2clg.SDXML2SD;
import ccu.pllab.tcgen.sd2clg.StateDigram;
import ccu.pllab.tcgen.sd2clp.ExecuteCLP;
import ccu.pllab.tcgen.sd2clp.SD2CLP;
import ccu.pllab.tcgen.transform.Splitter;
import ccu.pllab.tcgen.transform.UmlTransformer;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;

public class TestScriptGenerator {
	private List<TestData> testDatas;
	static int scriptNo = 1;

	public TestScriptGenerator() {
		scriptNo = 1;
	}

	public void init(List<TestData> tds) {
		this.testDatas = tds;
	}

	public String genTestScriptByPreamble(File uml) throws ParserConfigurationException, SAXException, IOException,
			TemplateException, ModelAccessException, ParseException, EclipseException, TransformerException {
//		String className = this.testDatas.get(0).getClassName();
//		String methodName = this.testDatas.get(0).getMethodName();
//		String testScript = "import junit.framework.TestCase;\n" + 
//							"import java.util.ArrayList;\n" + 
//							"import java.util.Arrays;\n\n" +
//							"public class Test"+className+"_"+methodName+" extends TestCase {\n";
		String testScript = "";
		Splitter split = new Splitter(uml);
		UmlTransformer trans = new UmlTransformer();
		File umlPre = split.split2CDuml();
		File cdUml = trans.transform(umlPre);
		File sdUml = split.split2SDuml();
		/* gen test method */
		for (TestData testData : this.testDatas) {
			/* STG set up */
			URL stgFileURL;
			InputStreamReader fr = null;
			if (!testData.isInvalidated()) {
				stgFileURL = TestScriptGenerator.class.getResource("testscript.stg");
				fr = new InputStreamReader(TestScriptGenerator.class.getResourceAsStream("testscript.stg"));
				System.out.println("invalid " + stgFileURL);
			} else {
				stgFileURL = TestScriptGenerator.class.getResource("testscriptfail.stg");
				System.out.println("valid " + stgFileURL);
			}
			File stgFileDir = null;
			try {
				stgFileDir = new File(stgFileURL.getPath());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			STGroup stg = new STGroupFile(stgFileDir.getAbsolutePath());
			;
			ST template = stg.getInstanceOf("testscript");

			/* sys_decl */
			String sys_decl = testData.getClassName() + " obj" + testData.getClassName() + " = null;\n";
//			if(testData.getObjPre().substring(1, testData.getObjPre().length() - 1).contains("["))
//				sys_decl += testData.getClassName() + " obj" + testData.getClassName() + "Post = null;\n";
//			else
//				sys_decl += testData.getClassName() + " obj" + testData.getClassName() + "_Post = null;\n";
			template.add("sys_decl", sys_decl);

			/* preamble begins */
			String sys_init = "";
			SDXML2SD parsd = new SDXML2SD();
			StateDigram st = parsd.convert(sdUml, cdUml);
			// StateDigram st = parsd.convert(new
			// File("examples/"+testData.getClassName()+"/"+testData.getClassName()+"SD.uml"),new
			// File("examples/"+testData.getClassName()+"/"+testData.getClassName()+"(old).uml"));
			SD2CLP s = new SD2CLP();
			s.convert(st);
			ArrayList seq = new ArrayList();
			CLP2Data clp = CLP2DataFactory.getEcl2DataInstance();

			if (testData.isConstructor()) {
				seq = clp.preambleQuery(testData.getObjPost(), testData.getClassName());
			} else {
				seq = clp.preambleQuery(testData.getObjPre(), testData.getClassName());
			}
			for (int i = 0; i < seq.size(); i++) {
				if (i == 0)
					sys_init = "obj" + testData.getClassName() + " = new "
							+ seq.get(i).toString().substring(0, 1).toUpperCase() + seq.get(i).toString().substring(1)
							+ ";\n";
				else
					sys_init += "obj" + testData.getClassName() + "." + seq.get(i) + ";\n";
			}
			template.add("sys_init", sys_init);
			/* preamble ends */

			/* sys_cleanup */
			String sys_cleanup = "obj" + testData.getClassName() + " = null;\n";
			if (testData.getObjPre().substring(1, testData.getObjPre().length() - 1).contains("["))
				sys_cleanup += "obj" + testData.getClassName() + "Post = null;\n";
			else
				sys_cleanup += "obj" + testData.getClassName() + "_Post = null;\n";
			template.add("sys_cleanup", sys_cleanup);

			/* targetObj */
			String target_obj = "obj" + testData.getClassName();
			template.add("target_obj", target_obj);

			/* arg_list */
			String arg_list = testData.getArgPre().substring(1, testData.getArgPre().length() - 1);
			template.add("arg_list", arg_list);

			/* isConstructor */
			template.add("is_constructor", testData.isConstructor());
			if (testData.getRetType().equals("OclVoid"))
				template.add("return_void", true);

			/* ret_type */
			String ret_type = "String";
			template.add("ret_type", ret_type);
			/* assert */
			String ret_val = testData.getRetVal().substring(1, testData.getRetVal().length() - 1);

//			2020/3/4 處理tkeclipse 回傳值(testdata)有字串的情況，下面的寫法為date, time範例做
			if (ret_val.contains("com.parctechnologies.eclipse.CompoundTermImpl with [functor=, arity=2 ")) {
				ret_val = ret_val.replaceAll("com.parctechnologies.eclipse.CompoundTermImpl with \\[functor=, arity=2 ",
						"");
				ret_val = ret_val.replaceAll("arg\\(1\\)=", "");
				ret_val = ret_val.replaceAll("arg\\(2\\)=", ",");
				ret_val = ret_val.replaceAll(" ,", ", ");
				ret_val = ret_val.replaceAll("\\]", "");
			}

			String assertStr = "";
			assertStr = "assertTrue(" + "result.equals(" + ret_val + "))";
			if (ret_val.contains("true"))
				ret_val = "true";
			else if (ret_val.contains("false"))
				ret_val = "false";
			if (Main.twoD) {
				String ret_list = ret_val;
				ret_val = "\"" + ret_val.substring(0, ret_val.lastIndexOf("]") + 1) + "\"";
			} else if (ret_val.contains("[")) {
				if (ret_val.contains("]"))
					ret_val = ret_val.substring(0, ret_val.indexOf("]"));
				ret_val = ret_val.replaceAll("\\[", "");
				ret_val = "\"[" + ret_val + "]\"";
			}
			template.add("ret_val", ret_val);
			if (!testData.isConstructor())
				template.add("assert", assertStr);

			template.add("exception", "Exception");
			template.add("testCasePackage", testData.getClassName());
			template.add("classPackage", testData.getClassName());
			template.add("class_name", testData.getClassName());
			template.add("method_name", testData.getMethodName());
			template.add("new_method_name",
					testData.getMethodName().substring(0, 1).toUpperCase() + testData.getMethodName().substring(1));
			template.add("obj_list", "\"" + testData.getObjPost() + "\"");
			template.add("case_no", scriptNo);

			testScript += template.render();
			scriptNo++;
		}
//		testScript += "}";
//		DataWriter.writeInfo(testScript, "Test" + className +"_"+methodName, "java", DataWriter.output_folder_path, "TestSuites");
		cdUml.delete();
		sdUml.delete();
		umlPre.delete();

		return testScript;
	}

//	public String genTestScript() throws ParserConfigurationException, SAXException, IOException, TemplateException, ModelAccessException, ParseException, EclipseException {
//		for (TestData testData : this.testDatas) {
//			this.genTestCase(testData);
//		}
//
//		return null;
//	}
	public String genTestScript(String output_path) {
		for (TestData testData : this.testDatas) {
			this.genTestCase(testData, output_path);
		}
		return null;
	}

//	2020/2/25調整，原版看建隆
	public String genTestCase() throws ParserConfigurationException, SAXException, IOException, TemplateException,
			ModelAccessException, ParseException, EclipseException {
		String testScript = "";
		for (TestData testData : this.testDatas) {
//			String testCaseName = String.format("Test%s%s", testData.getTestDataName(), scriptNo);
//			System.out.println(testCaseName);

			URL stgFileURL;
			InputStreamReader fr = null;
			if (!testData.isInvalidated()) {
				stgFileURL = TestScriptGenerator.class.getResource("testscript.stg");
				fr = new InputStreamReader(TestScriptGenerator.class.getResourceAsStream("testcase.stg"));
				System.out.println("invalid " + stgFileURL);

			} else {
				stgFileURL = TestScriptGenerator.class.getResource("testscriptfail.stg");
				System.out.println("valid " + stgFileURL);
			}

			File stgFileDir = null;
			try {
//				stgFileDir = new File(stgFileURL.toURI());
				stgFileDir = new File(stgFileURL.getPath());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			STGroup stg;
			ST template;
			if (!testData.isInvalidated()) {
//				stg = new STGroupFile(stgFileDir);
				stg = new STGroupFile(stgFileDir.getAbsolutePath());
				template = stg.getInstanceOf("testscript");
			} else {
				stg = new STGroupFile(stgFileDir.getAbsolutePath());
				template = stg.getInstanceOf("testscript");
			}
			/* sys_decl */
			String sys_decl = testData.getClassName() + " obj" + testData.getClassName() + " = null;\n";
			if (testData.getObjPre().substring(1, testData.getObjPre().length() - 1).contains("["))
				sys_decl += testData.getClassName() + " obj" + testData.getClassName() + "Post = null;\n";
			else
				sys_decl += testData.getClassName() + " obj" + testData.getClassName() + "_Post = null;\n";

			template.add("sys_decl", sys_decl);

			String exception = "Exception";

			/* sys_init */
			String obj_pre = testData.getObjPre().substring(1, testData.getObjPre().length() - 1);
			String obj_post = testData.getObjPost().substring(1, testData.getObjPost().length() - 1);
			obj_pre = obj_pre.replace(", ", ",");
			obj_post = obj_post.replace(", ", ",");
			String sys_init = "";
			String arg_list = testData.getArgPre().substring(1, testData.getArgPre().length() - 1);
			
String ret_val = testData.getRetVal().substring(1, testData.getRetVal().length() - 1);
			
			boolean isReturnClass = false;
//			2020/3/4 處理tkeclipse 回傳值(testdata)有字串的情況，下面的寫法為date, time範例做
			if (ret_val.contains("com.parctechnologies.eclipse.CompoundTermImpl with [functor=, arity=2 ")) {
				ret_val = ret_val.replaceAll("com.parctechnologies.eclipse.CompoundTermImpl with \\[functor=, arity=2 ",
						"");
				ret_val = ret_val.replaceAll("arg\\(1\\)=", "");
				ret_val = ret_val.replaceAll("arg\\(2\\)=", ",");
				ret_val = ret_val.replaceAll(" ,", ", ");
				ret_val = ret_val.replaceAll("\\]", "");
				isReturnClass = true;
			}
			if (ret_val.contains("true"))
				ret_val = "true";
			else if (ret_val.contains("false"))
				ret_val = "false";
			if (Main.twoD) {
				String ret_list = ret_val;

				ret_val = "\"" + ret_val.substring(0, ret_val.lastIndexOf("]") + 1) + "\"";
			} else if (ret_val.contains("[")) {
				if (ret_val.contains("]"))
					ret_val = ret_val.substring(0, ret_val.indexOf("]"));
				ret_val = ret_val.replaceAll("\\[", "");
				ret_val = "\"[" + ret_val + "]\"";
			}
			
			
			if (obj_pre.contains("[") || obj_post.contains("[")) {
				exception = "ArraySizeException";
				if (testData.isConstructor()) {
					if (Main.twoD) {
						sys_init += "int[][] objArray=";
						String temparg = arg_list;
						int row = 0;
						int col = 1;
						for (String temp = temparg; temp.indexOf("],") > 0; temp = temp
								.substring(temp.indexOf("],") + 2))
							row++;
						for (String temp = temparg; temp.indexOf(",") > 0
								&& temp.indexOf(",") < temp.indexOf("]"); temp = temp.substring(temp.indexOf(",") + 1))
							col++;

						if (temparg.contains("[]")) {
							row = 0;
							col = 0;
						}
						temparg = temparg.replaceAll("\\[", "{").replaceAll("]", "}");
						sys_init += temparg.substring(0, temparg.lastIndexOf("}") + 1) + ";\n";

						if (Main.symbolTable.getAttributeMap().containsKey("row"))
							sys_init += "obj" + testData.getClassName() + "=new " + testData.getClassName()
									+ "(objArray, " + row + ", " + col + ");\n";// ,"+row_pre+","+col_pre+");\n";
						else
							sys_init += "obj" + testData.getClassName() + "=new " + testData.getClassName()
									+ "(objArray);\n";// ,"+row_pre+","+col_pre+");\n";

						String obj_list = "\"" + obj_post.substring(0, obj_post.lastIndexOf("]") + 1) + "\"";
						template.add("obj_list", obj_list);
					} else if (testData.getClassName().contains("Stack") || testData.getClassName().contains("Queue")) {
						String bound = "";
						if (obj_post.contains("],")) {
							bound = obj_post.substring(obj_post.indexOf("],") + 2);
							if (bound.contains(","))
								bound = bound.substring(0, bound.indexOf(","));
							else
								bound = "";
						}
						if (!testData.getObjPost().equals("[]"))
							obj_post = testData.getObjPost().substring(2, testData.getObjPost().length() - 2);
						else
							obj_post = "";
						template.add("is_array", true);

						if (testData.getClassName().contains("Stack")) {
							if (testData.getArgPre().contains("-32768"))
								sys_init += "objStack_Post=new Stack(-32768);\n";
							else {
								sys_init += "objStack_Post=new Stack(" + bound + ");\n";
								if (obj_post.contains(",")) {
									String[] pushPost = obj_post.split(",");
									for (String token : pushPost) {
										if (token.contains("]")) {
											if (token.substring(0, token.length() - 1).length() > 0)
												sys_init += "objStack_Post.push("
														+ token.substring(0, token.length() - 1) + ");\n";
											break;
										}
										sys_init += "objStack_Post.push(" + token + ");\n";
									}
								} else if (obj_post.length() > 0) {
									sys_init += "objStack_Post.push(" + obj_post + ");\n";
								}

								// template.add("is_bound",true);
								// arg_list="\""+data.getObjPost().substring(1,
								// data.getObjPost().indexOf("]")+1)+"\"";
								template.add("obj_list", "\"[" + obj_post.substring(0, obj_post.indexOf("]")) + "]\"");
							}

							// template.add("assert","assertEquals(objStack.toString(),\"[]\");\n");
						} else if (testData.getClassName().contains("Queue")) {
							if (testData.getArgPre().contains("-32768"))
								sys_init += "objQueue_Post=new Queue(-32768);\n";
							else {
								sys_init += "objQueue_Post = new Queue(" + bound + ");\n";
								if (obj_post.contains(",")) {
									String[] pushPost = obj_post.split(",");
									for (String token : pushPost) {
										if (token.contains("]")) {
											if (token.substring(0, token.length() - 1).length() > 0)
												sys_init += "objQueue_Post.enqueue("
														+ token.substring(0, token.length() - 1) + ");\n";
											break;
										}
										sys_init += "objQueue_Post.enqueue(" + token + ");\n";
									}
								} else if (obj_post.length() > 0) {
									sys_init += "objQueue_Post.enqueue(" + obj_post + ");\n";
								}

								// template.add("is_bound",true);
								// arg_list="\""+data.getObjPost().substring(1,
								// data.getObjPost().indexOf("]")+1)+"\"";
								template.add("obj_list", "\"[" + obj_post.substring(0, obj_post.indexOf("]")) + "]\"");

							}
							// template.add("assert","assertEquals(objStack.toString(),\"[]\");\n");
						}
					} else {
						if (obj_post.contains("]"))
							obj_post = obj_post.substring(0, obj_post.indexOf("]"));
						String size = testData.getObjPost().substring(testData.getObjPost().indexOf("]") + 1,
								testData.getObjPost().length() - 1);
						sys_init += "int[] objArrayPost= {" + obj_post.substring(1) + "};\n" + "obj"
								+ testData.getClassName() + " = new " + testData.getClassName() + "(objArrayPost" + size
								+ ");\n";
						template.add("obj_list", "\"[" + obj_post.substring(1) + "]\"");
					}
				} else {
					if (Main.twoD) {
						sys_init += "int[][] objArray={";
						String tempObjpre = obj_pre.substring(1, obj_pre.lastIndexOf("]"));
						int row = 0;
						int col = 0;
						for (String temp = tempObjpre; temp.indexOf("],") > 0; temp = temp
								.substring(temp.indexOf("],") + 2))
							row++;
						for (String temp = tempObjpre; temp.indexOf(",") > 0
								&& temp.indexOf(",") < temp.indexOf("]"); temp = temp.substring(temp.indexOf(",") + 1))
							col++;

						if (tempObjpre.length() > 0) {
							row++;
							col++;
						}
						tempObjpre = tempObjpre.replaceAll("\\[", "{").replaceAll("]", "}");
						sys_init += tempObjpre.substring(0, tempObjpre.lastIndexOf("}") + 1) + "};\n";

						if (Main.symbolTable.getAttributeMap().containsKey("row"))
							sys_init += "obj" + testData.getClassName() + "=new " + testData.getClassName()
									+ "(objArray, " + row + ", " + col + ");\n";// ,"+row_pre+","+col_pre+");\n";
						else
							sys_init += "obj" + testData.getClassName() + "=new " + testData.getClassName()
									+ "(objArray);\n";// ,"+row_pre+","+col_pre+");\n";
						String obj_list = "";

						template.add("obj_list", obj_list);
						// sys_init+="obj"+data.getClassName()+"Post =new
						// "+data.getClassName()+"(objArrayPost);\n";//,"+row_post+","+col_post+");\n";
						template.add("is_array", true);
					} else {
						String bound = "";
						if ((testData.getClassName().contains("Stack") || testData.getClassName().contains("Queue"))
								&& obj_pre.contains("],")) {
							bound = obj_pre.substring(obj_pre.indexOf("],") + 2);
							if (bound.contains(","))
								bound = bound.substring(0, bound.indexOf(","));
							else
								bound = "";
						}
						if (!obj_pre.equals("[]")) {
							obj_pre = testData.getObjPre().substring(2, testData.getObjPre().indexOf("]"));
							if (!testData.getObjPost().equals("[]"))
								obj_post = testData.getObjPost().substring(2, testData.getObjPost().length() - 2);
						} else {
							obj_pre = "";
							if (!testData.getObjPost().equals("[]"))
								obj_post = testData.getObjPost().substring(2, testData.getObjPost().length() - 2);
							else
								obj_post = "";
						}
						template.add("is_array", true);
						if (testData.getClassName().contains("Stack")) {
							sys_init += "objStack = new Stack(" + bound;// );\n";

							sys_init += ");\n";
							if (obj_pre.contains(",")) {
								String[] push = obj_pre.split(",");
								for (String token : push) {
									if (token.contains("]")) {
										sys_init += "objStack.push(" + token.substring(0, token.indexOf("]")) + ");\n";
										break;
									}
									sys_init += "objStack.push(" + token + ");\n";
								}
							} else if (obj_pre.length() > 0) {
								sys_init += "objStack.push(" + obj_pre + ");\n";
							}
							template.add("obj_list", "\"[" + obj_post.substring(0, obj_post.indexOf("]")) + "]\"");
						} else if (testData.getClassName().contains("Queue")) {
							sys_init = "objQueue = new Queue(" + bound + ");\n";
							if (obj_pre.contains(",")) {
								String[] queue = obj_pre.split(",");
								for (String token : queue) {
									if (token.contains("]")) {
										sys_init += "objQueue.enqueue(" + token.substring(0, token.indexOf("]"))
												+ ");\n";
										break;
									}
									sys_init += "objQueue.enqueue(" + token + ");\n";
								}
							} else if (obj_pre.length() > 0) {
								sys_init += "objQueue.enqueue(" + obj_pre + ");\n";
							}
							template.add("obj_list", "\"[" + obj_post.substring(0, obj_post.indexOf("]")) + "]\"");
						}

						else {
							String size = testData.getObjPre().substring(testData.getObjPre().indexOf("]") + 1,
									testData.getObjPre().length() - 1);
							sys_init = "int[] objArray= {" + obj_pre + "};\n" + "obj" + testData.getClassName()
									+ " = new " + testData.getClassName() + "(objArray" + size + ");\n";
							if (obj_post.contains("]"))
								obj_post = obj_post.substring(0, obj_post.indexOf("]"));
							size = testData.getObjPost().substring(testData.getObjPost().indexOf("]") + 1,
									testData.getObjPost().length() - 1);
							template.add("obj_list", "\"[" + obj_post + "]\"");
							// sys_init += "int[] objArrayPost= {"+obj_post+"};\n"+"obj" +
							// data.getClassName() + "Post = new " + data.getClassName() +
							// "(objArrayPost"+size+");\n";
						}
					}
				}
			} else {
				if (!Main.twoD && !testData.getClassName().equals("Stack")
						&& !testData.getClassName().equals("Queue")) {
					sys_init = "obj" + testData.getClassName() + " = new " + testData.getClassName() + "(" + obj_post
							+ ");\n";
//					sys_init += "obj" + testData.getClassName() + "_Post = new " + testData.getClassName() + "("
//							+ obj_post + ");\n";
					if (testData.isInvalidated() && testData.isConstructor()) {
						sys_init = "";
					}else if(isReturnClass) {
						sys_init += "obj" + testData.getClassName() + "_Post = new " + testData.getClassName() + "("
								+ ret_val + ");\n";
					}
					template.add("obj_list", "\"" + testData.getObjPost() + "\"");
				}
			}
			
			if(isReturnClass) {
				template.add("ret_val","obj" + testData.getClassName() + "_Post");
			}else {
				template.add("ret_val", ret_val);
			}
			template.add("is_ReturnClass", isReturnClass);
			
			template.add("sys_init", sys_init);

			/* sys_cleanup */
			String sys_cleanup = "obj" + testData.getClassName() + " = null;\n";
			if (testData.getObjPre().substring(1, testData.getObjPre().length() - 1).contains("["))
				sys_cleanup += "obj" + testData.getClassName() + "Post = null;\n";
			else
				sys_cleanup += "obj" + testData.getClassName() + "_Post = null;\n";

			template.add("sys_cleanup", sys_cleanup);

			/* targetObj */
			String target_obj = "obj" + testData.getClassName();
			template.add("target_obj", target_obj);

			/* arg_list */

			template.add("arg_list", arg_list);

			/* isConstructor */
			template.add("is_constructor", testData.isConstructor());
			if (testData.getRetType().equals("OclVoid"))
				template.add("return_void", true);
			/* ret_type */
			String ret_type = "String";
			template.add("ret_type", ret_type);

			/* assert */
			

			String assertStr = "";
			// if(data.getRetVal().length()-1==0)
			assertStr = "assertTrue(" + "result.equals(" + ret_val + "))";
			
			// else
			// assertStr = "assertTrue(" + "obj" + data.getClassName() +".equals(" + "obj" +
			// data.getClassName() + "_Post" + "))";
			if (!testData.isConstructor())
				template.add("assert", assertStr);
			template.add("exception", exception);
			template.add("testCasePackage", testData.getClassName());
			template.add("classPackage", testData.getClassName());
			template.add("class_name", testData.getClassName());
			template.add("method_name", testData.getMethodName());
			template.add("new_method_name", testData.getMethodName());
			// template.add("case_no", data.getTestDataID());
			template.add("case_no", scriptNo);
			// System.out.println(template.render());
			String testCasePreName;
			if (testData.isInvalidated()) {
				testCasePreName = "errTest";
			} else {
				testCasePreName = "Test";
			}
			if (!obj_pre.equals("[]"))
				testScript += template.render();
			scriptNo++;
		}
		return testScript;
	}

	public void genTestCase(TestData data, String output_path) {
		String testCaseName = String.format("Test%s%s", data.getTestDataName(), scriptNo);
		System.out.println("testpath:" + TestScriptGenerator.class.getResource(""));

		URL stgFileURL;
		InputStreamReader fr = null;
		/*
		 * if (!data.isInvalidated()) { stgFileURL =
		 * TestScriptGenerator.class.getResource("testcase.stg"); fr= new
		 * InputStreamReader(TestScriptGenerator.class.getResourceAsStream(
		 * "testcase.stg")); System.out.println("invalid " +stgFileURL);
		 * 
		 * } else { stgFileURL =
		 * TestScriptGenerator.class.getResource("testcasefail.stg");
		 * System.out.println("valid "+stgFileURL); }
		 */

		File stgFileDir = null;
		try {
			stgFileDir = new File("./ccu/pllab/tcgen/TestCase/testcase.stg");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		STGroup stg;
		ST template;
		if (!data.isInvalidated()) {
			stg = new STGroupFile("/ccu/pllab/tcgen/TestCase/testcase.stg");
			template = stg.getInstanceOf("testcase");
		} else {
			stg = new STGroupFile("ccu/pllab/tcgen/TestCase/testcase.stg");
			template = stg.getInstanceOf("testcase");
		}
		/* sys_decl */
		String sys_decl = data.getClassName() + " obj" + data.getClassName() + " = null;\n";
		if (data.getObjPre().substring(1, data.getObjPre().length() - 1).contains("["))
			sys_decl += data.getClassName() + " obj" + data.getClassName() + "Post = null;\n";
		else
			sys_decl += data.getClassName() + " obj" + data.getClassName() + "_Post = null;\n";

		template.add("sys_decl", sys_decl);

		/* sys_init */
		String obj_pre = data.getObjPre().substring(1, data.getObjPre().length() - 1);
		String obj_post = data.getObjPost().substring(1, data.getObjPost().length() - 1);
		String sys_init = "";
		if (obj_pre.contains("[")) {
			if (!obj_pre.equals("[]")) {
				obj_pre = data.getObjPre().substring(2, data.getObjPre().indexOf("]"));
				obj_post = data.getObjPost().substring(2, data.getObjPost().length() - 2);
			} else {
				obj_pre = "";
				obj_post = "";
			}
			template.add("is_array", true);
			// obj_pre=obj_pre.replaceAll("[", "");
			// obj_pre=obj_pre.replaceAll("]", "");
			sys_init = "int[] objArray= {" + obj_pre + "};\n" + "obj" + data.getClassName() + " = new "
					+ data.getClassName() + "(objArray);\n";
			// sys_init=sys_init.replaceAll("[", "");
			// sys_init=sys_init.replaceAll("]", "");
			sys_init += "int[] objArrayPost= {" + obj_post + "};\n" + "obj" + data.getClassName() + "Post = new "
					+ data.getClassName() + "(objArrayPost);\n";
			// sys_init=sys_init.replaceAll("[", "");
			// sys_init=sys_init.replaceAll("]", "");
		} else {
			sys_init = "obj" + data.getClassName() + " = new " + data.getClassName() + "(" + obj_pre + ");\n";
			sys_init += "obj" + data.getClassName() + "_Post = new " + data.getClassName() + "(" + obj_post + ");\n";
		}

		template.add("sys_init", sys_init);

		/* sys_cleanup */
		String sys_cleanup = "obj" + data.getClassName() + " = null;\n";
		if (data.getObjPre().substring(1, data.getObjPre().length() - 1).contains("["))
			sys_cleanup += "obj" + data.getClassName() + "Post = null;\n";
		else
			sys_cleanup += "obj" + data.getClassName() + "_Post = null;\n";

		template.add("sys_cleanup", sys_cleanup);

		/* targetObj */
		String target_obj = "obj" + data.getClassName();
		template.add("target_obj", target_obj);

		/* arg_list */
		String arg_list = data.getArgPre().substring(1, data.getArgPre().length() - 1);
		template.add("arg_list", arg_list);

		/* isConstructor */
		template.add("is_constructor", data.isConstructor());
		if (data.getRetType().equals("OclVoid"))
			template.add("return_void", true);
		/* ret_type */
		String ret_type = "String";
		template.add("ret_type", ret_type);

		/* assert */
		String ret_val = data.getRetVal().substring(1, data.getRetVal().length() - 1);
		String assertStr = "";
		// if(data.getRetVal().length()-1==0)
		assertStr = "assertTrue(" + "result.equals(" + ret_val + "))";
		// else
		// assertStr = "assertTrue(" + "obj" + data.getClassName() +".equals(" + "obj" +
		// data.getClassName() + "_Post" + "))";
		template.add("assert", assertStr);

		template.add("testCasePackage", data.getClassName());
		template.add("classPackage", data.getClassName());
		template.add("class_name", data.getClassName());
		template.add("method_name", data.getMethodName());
		// template.add("case_no", data.getTestDataID());
		template.add("case_no", scriptNo);
		// System.out.println(template.render());
		String testCasePreName;
		if (data.isInvalidated()) {
			testCasePreName = "errTest";
		} else {
			testCasePreName = "Test";
		}
		if (!obj_pre.equals("[]"))
			// DataWriter.writeInfo(template.render(), testCasePreName +
			// data.getTestDataName() + "_c" + data.getTestDataID(), "java",
			// DataWriter.output_folder_path, "TestSuites");
			DataWriter.writeInfo(template.render(), testCasePreName + data.getClassName() + scriptNo, "java",
					output_path, "test");
		scriptNo++;
		System.out.println(template.toString());
	}
}
