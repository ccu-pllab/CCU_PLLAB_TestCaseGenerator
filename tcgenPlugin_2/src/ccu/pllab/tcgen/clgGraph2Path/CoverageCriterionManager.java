package ccu.pllab.tcgen.clgGraph2Path;

//2020/4/6 整理程式，先將系統提示為"never used", "not use" 的項目做註解


//import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
//import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

//import org.apache.commons.lang.time.FastDateFormat;
//import org.apache.log4j.spi.ThrowableInformation;

import ccu.pllab.tcgen.AbstractCLG.CLGConstraintNode;
import ccu.pllab.tcgen.AbstractCLG.CLGCriterionTransformer;
import ccu.pllab.tcgen.AbstractCLG.CLGEdge;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractCLG.CLGStartNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGMethodInvocationNode;
//import ccu.pllab.tcgen.AbstractSyntaxTree.AbstractSyntaxTreeNode;
//import ccu.pllab.tcgen.AbstractSyntaxTree.OperationContext;
//import ccu.pllab.tcgen.AbstractSyntaxTree.PackageExp;
//import ccu.pllab.tcgen.AbstractSyntaxTree.StereoType;
//import ccu.pllab.tcgen.AbstractSyntaxTree.SymbolTable;
import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.TestCase.TestData;
import ccu.pllab.tcgen.TestCase.TestDataClassLevel;
import ccu.pllab.tcgen.clg2path.CriterionFactory;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.pathCLPFinder.CLPSolver;
import ccu.pllab.tcgen.pathCLPFinder.CLPTranslator;
//import scala.annotation.bridge;
//import scala.reflect.generic.Trees.CaseDef;
import ccu.pllab.tcgen.exe.main.Main;

public class CoverageCriterionManager implements CLGCoverageCriterion {
	private CLGGraph targetCLG;
	private Set<CLGPath> feasible_path;// 4
	private CLGPathEnumerator clgPathEnumerator;// 1
	private CLPTranslator clpTranslator;// 2
	private CLPSolver clpSolver;// 3
	private List<TestData> testDatas;// 5
	private List<TestDataClassLevel> testClassDatas;
	private CoverageCheckingTable coverageCheckingTable;// ....

	// --
	private Criterion criterionState;
	private CoverageCheckingTable coverageDUPCheckingTable;
	private String criteria;
	private CLGReachingDefinitionAnalyzer clgRDA;
	private ArrayList<DUP> oriDUP;
	private ArrayList<DUP> feasibledup;
	private ArrayList<CLGPath> infeasiblePath;
	private ArrayList<DUP> infeasibledup;

	private ArrayList<String> attribute = new ArrayList<String>();
	private String criterion;
	private String invCLP;
	private String pathObjInfo = "";
	private static Boolean infeasibleTilStartNode = false;
	private static Boolean skipSIPPart = false; //skipSIPPart用來跳過SIP&CCS部分的執行，用來解決建隆unbound&sortedarray的問題
	private static List<List<CLGNode>> SIPNodesList = new ArrayList<>();
	private Set<List<String>> conflictConstraints = new HashSet<>();

	/*
	 * public void setAttribute(ArrayList<String> attribute) {
	 * this.attribute=attribute; } public void setCriterion(String criterion) {
	 * this.criterion=criterion; } public void setin
	 */
	/*******************************************************************/
	
//	黑箱是使用這個
	public CoverageCriterionManager() {
		this.feasible_path = new LinkedHashSet<CLGPath>();
		this.clgPathEnumerator = new CLGPathEnumerator();
		this.clpTranslator = new CLPTranslator();
		this.clpSolver = new CLPSolver();
		this.coverageCheckingTable = new CoverageCheckingTable();
		// ---
		this.coverageDUPCheckingTable = new CoverageCheckingTable();
		this.criteria = "";
		this.infeasiblePath = new ArrayList<CLGPath>();
		SIPNodesList.clear();
		conflictConstraints.clear();
	}

	public CoverageCriterionManager(ArrayList<String> attribute, String criterion, String invCLP) {
		this.feasible_path = new LinkedHashSet<CLGPath>();
		this.clgPathEnumerator = new CLGPathEnumerator();
		this.clpTranslator = new CLPTranslator();
		this.clpSolver = new CLPSolver();
		this.coverageCheckingTable = new CoverageCheckingTable();
		// ---
		this.coverageDUPCheckingTable = new CoverageCheckingTable();
		this.criteria = "";
		this.infeasiblePath = new ArrayList<CLGPath>();

	}

//	黑箱使用
	@Override
	public void init(CLGGraph graph) {
		this.targetCLG = graph;
		// this.selectCLGGraphCriteria(graph, Main.criterion);//61和62同
		this.clgPathEnumerator.init(targetCLG);
		this.testDatas = new ArrayList<TestData>();
//		這個迴圈將clg中的每個邊(edge)取出，放置於coverageCheckingTable中
//		存放形式:{ <1> -> [9]}=0 等號後的01代表之後是否覆蓋
		for (CLGEdge edge : graph.getAllBranches()) {
			this.coverageCheckingTable.put(edge, 0);
		}

//		2020/2/10 以下為dup，目前不會用到
		oriDUP = new ArrayList<DUP>();
		if (Main.criterion.equals(Criterion.dcdup) || Main.criterion.equals(Criterion.dccdup)
				|| Main.criterion.equals(Criterion.mccdup)) {
			System.out.println("part 1 ");
			CLGReachingDefinitionAnalyzer clg2dup = new CLGReachingDefinitionAnalyzer();
			String classname = "";
			classname = ((CLGStartNode) graph.getStartNode()).getClassName();
			clg2dup.dupGenerate(graph, classname, ((CLGStartNode) graph.getStartNode()).getMethodName());

			ArrayList<DUP> clgDUP = clg2dup.dupGenerate(this.targetCLG, classname, "");
			for (int i = 0; i < clgDUP.size(); i++)
				this.coverageDUPCheckingTable.put(clgDUP.get(i), 0);
			oriDUP = clgDUP;
			for (int i1 = 0; i1 < oriDUP.size(); i1++) {
				System.out.println("([" + oriDUP.get(i1).getDefineNode().getXLabelId() + "]," + "["
						+ oriDUP.get(i1).getUseNode().getXLabelId() + "]" + "," + oriDUP.get(i1).getVariable() + ")");
			}
			feasibledup = new ArrayList<DUP>();
			infeasibledup = new ArrayList<DUP>();
			clgRDA = new CLGReachingDefinitionAnalyzer();
		}

	}

	// 可能加@Override
	public void init(CLGGraph graph, String criterion) {
		this.targetCLG = graph;
		this.attribute = attribute;
		this.criterion = criterion;
		// this.selectCLGGraphCriteria(graph, Main.criterion);//61和62同
		this.clgPathEnumerator.init(targetCLG);
		this.testDatas = new ArrayList<TestData>();
		for (CLGEdge edge : graph.getAllBranches()) {
			this.coverageCheckingTable.put(edge, 0);
		}

		oriDUP = new ArrayList<DUP>();
		if (criterion.equals("dcdup") || criterion.equals("dccdup") || criterion.equals("mccdup")) {
			System.out.println("part 1 ");
			CLGReachingDefinitionAnalyzer clg2dup = new CLGReachingDefinitionAnalyzer();
			String classname = "";
			classname = ((CLGStartNode) graph.getStartNode()).getClassName();
			clg2dup.dupGenerate(graph, classname, ((CLGStartNode) graph.getStartNode()).getMethodName());

			ArrayList<DUP> clgDUP = clg2dup.dupGenerate(this.targetCLG, classname, "");
			for (int i = 0; i < clgDUP.size(); i++)
				this.coverageDUPCheckingTable.put(clgDUP.get(i), 0);
			oriDUP = clgDUP;
			for (int i1 = 0; i1 < oriDUP.size(); i1++) {
				System.out.println("([" + oriDUP.get(i1).getDefineNode().getXLabelId() + "]," + "["
						+ oriDUP.get(i1).getUseNode().getXLabelId() + "]" + "," + oriDUP.get(i1).getVariable() + ")");
			}
			feasibledup = new ArrayList<DUP>();
			infeasibledup = new ArrayList<DUP>();
			clgRDA = new CLGReachingDefinitionAnalyzer();
		}

	}

	/* State diagram analysis */
	public void init(CLGGraph graph, Criterion criterionState, List<ccu.pllab.tcgen.ast.Constraint> OCLCons) {
		this.targetCLG = graph;
		this.selectCLGGraphCriteria(graph, Main.criterion);
		this.criterionState = criterionState;
		this.clgPathEnumerator.init(targetCLG);
		this.testDatas = new ArrayList<TestData>();
		this.testClassDatas = new ArrayList<TestDataClassLevel>();
		for (CLGEdge edge : graph.getAllBranches()) {
			this.coverageCheckingTable.put(edge, 0);
		}
		this.criteria = criteria;

		// --DUP--
		oriDUP = new ArrayList<DUP>();
		if (Main.criterion.equals(Criterion.dcdup) || Main.criterion.equals(Criterion.dccdup)
				|| Main.criterion.equals(Criterion.mccdup)) {
			System.out.println("part 1 ");
			CLGReachingDefinitionAnalyzer clg2dup = new CLGReachingDefinitionAnalyzer();
			String classname = "";
			for (int i = 0; i < OCLCons.size(); i++) {
				if (OCLCons.get(i).getConstraintKind() == "postcondition") {
					clg2dup.dupGenerate(OCLCons.get(i).OCL2CLG(), OCLCons.get(i).getConstraintedClassName(),
							OCLCons.get(i).getConstraintedMethodName());
				}
				classname = OCLCons.get(i).getConstraintedClassName();
			} // end for i

			ArrayList<DUP> clgDUP = clg2dup.dupGenerate(this.targetCLG, classname, "");
			for (int i = 0; i < clgDUP.size(); i++)
				this.coverageDUPCheckingTable.put(clgDUP.get(i), 0);
			oriDUP = clgDUP;
			for (int i1 = 0; i1 < oriDUP.size(); i1++) {
				System.out.println("([" + oriDUP.get(i1).getDefineNode().getXLabelId() + "]," + "["
						+ oriDUP.get(i1).getUseNode().getXLabelId() + "]" + "," + oriDUP.get(i1).getVariable() + ")");
			}
			feasibledup = new ArrayList<DUP>();
			infeasibledup = new ArrayList<DUP>();
			clgRDA = new CLGReachingDefinitionAnalyzer();
		}
		// ---
	}

//	黑箱使用 ~line 1622
	@SuppressWarnings("rawtypes")
	public List<TestData> genTestSuite() throws IOException, CloneNotSupportedException {
		String path = "";
		this.clpTranslator = new CLPTranslator();
		String graphClassName = ((CLGStartNode) this.targetCLG.getStartNode()).getFirstUpperClassName();
		String graphMethodName = ((CLGStartNode) this.targetCLG.getStartNode()).getFirstUpperMethodName();
		String graphRetType = ((CLGStartNode) this.targetCLG.getStartNode()).getReturnType();
		boolean graphIsConstructor = ((CLGStartNode) this.targetCLG.getStartNode()).isConstructor();
		Main.isConstructor = graphIsConstructor;
		CLGPath pathObj, boundaryPath;
		CLGPath  shortestInfeasiblePath; //for路徑縮減, 原版CLGPath保留
		List<CLGNode> SIPNodes = null; //for路徑縮減, 找出的infeasible最短節點
		Boolean isFeasible = false; //for路徑縮減, 判別該條路徑是否有解
		String graphMethodRetType = "";
		String saveTDPathDUP = "";//
		BoundaryEnumerator boundaryEnumerator = new BoundaryEnumerator();
		int pathNo = 1;
		int accept = 0;
		int boundsolution = 0;
		int totalbound = 0;
		boolean hasInitPathCount = false;
		String TestTypeSign = "";

//		判別測試種類為黑箱或白箱等
		if (Main.TestType == "BlackBox") {
			TestTypeSign = "B";
		} else if (Main.TestType == "WhiteBox") {
			TestTypeSign = "W";
		} else {
			TestTypeSign = "C";
		}

		boolean boundexit = false;
		boolean bound0_1 = false, bound1_0 = false, bound1_1 = false, bound4_0 = false, bound4_1 = false,
				bound4_2 = false, bound4_3 = false, bound4_4 = false;
		boolean sortedClass = false;
//		clgPathEnumerator.next() 對clg找路徑，每次只回傳一條完整路徑
//		pathObj代表其中一條路徑，也就是以下while迴圈一次只找其中一條路徑
//		meetCriterion() 檢查coverageCheckingTable中是否還有沒有找到的邊，coverageCheckingTable只有當該條路徑找到解時才會把
//		路徑上的邊標為走訪過

		while (!this.meetCriterion() /* i < 50 */ && (pathObj = clgPathEnumerator.next()) != null) {
			isFeasible = false;
			skipSIPPart = false;
			// 20200622  重置pathID及count
			if(!hasInitPathCount) {
				pathObj.initPathCount();
				pathObj.initPathID();
				hasInitPathCount = true;
			}
			pathObjInfo += "\n" + pathObj.toGetPathInfo() + "\n";
			//在pathinfo文件中印出每條路徑在CLG中的限制式
			String tempConStr = "";
			for (int i = 0; i < pathObj.getPathNodes().size(); i++) {
				if (pathObj.getPathNodes().get(i) instanceof CLGConstraintNode) {
					// CLGConstraintNode cons = (CLGConstraintNode)pathObj.getPathNodes().get(i);
					CLGConstraintNode cons = (CLGConstraintNode) pathObj.getPathNodes().get(i);
					tempConStr += cons.toCLPInfo().replace("#", "") + ",";
				}
			}
			tempConStr.substring(0, (tempConStr.length() - 2));
			pathObjInfo += "CLG Constraints: " + tempConStr + "\n"; // pathObjInfo 印出到檔案的字串
			tempConStr = ""; 
			
//			先註解，可能跟要處理array的地方有關
//			Main.arrayMap = new HashMap<String, Integer>();
//			Main.indexMap = new HashMap<String, Integer>();
			
			ArrayList<DUP> nowDUP = new ArrayList<DUP>();
			if (Main.criterion.equals(Criterion.dcdup) || Main.criterion.equals(Criterion.dccdup)
					|| Main.criterion.equals(Criterion.mccdup)) {
				System.out.println("control part 2 ");
				List<CLGNode> new_path1 = clgPathEnumerator.filterConstraintNode(pathObj.getPathNodes());
				ArrayList<DUP> regDUP = new ArrayList<DUP>();
				CLGPath clgp = new CLGPath(new_path1);
				regDUP = clgRDA.parsePathDUP(clgp, oriDUP);
				System.out.println(" regdup " + regDUP.size());
				for (int i = 0; i < regDUP.size(); i++) {
					if (this.coverageDUPCheckingTable.containsKey(regDUP.get(i))) {
						nowDUP.add(regDUP.get(i));
					}
				}
				System.out.println("Path = " + clgp.toGetPathInfo());
				for (DUP p : nowDUP) {
					System.out.println(p.DUP2Str());
				}
			}
			// -------

//			將pathObj的每條邊取出，加入到intersectionalEdge中
//			並且coverageCheckingTable對應的邊組合值+1
			Set<CLGEdge> intersectionalEdge = new HashSet<CLGEdge>();
			for (CLGEdge b : pathObj.getEdges()) {
				if (this.coverageCheckingTable.containsKey(b)) {
					intersectionalEdge.add(b);
					coverageCheckingTable.put(b, ((Integer) coverageCheckingTable.get(b)) + 1);
				}
			}
			
			//檢查此路徑是否提高覆蓋度，會的話累加測試資料20200519
			//若在找解後比對edge，執行邊界值分析的情況下將不會加入其他邊界的解
//			if(isIncreaseCoverage(intersectionalEdge)) {
//				testDatas.add(clpSolver.getTestData());
//			}
			
//			刪除所有pathObj中的連接節點
			pathObj.removeAllConnectionNode();
			
				boundaryEnumerator.init(pathObj);

				if (intersectionalEdge.size() > 0 || nowDUP.size() > 0) {
					System.out.println("control intersectionalEdge ");
					boolean flag = false;
//					檢查pathObj路徑上每一個限制節點是否為CLGMethodInvocationNode，沒有用到?
					ArrayList<String> methodarr = new ArrayList<String>();
					for (int pathi = 0; pathi < pathObj.getPathNodes().size(); pathi++) {
						if (pathObj.getPathNodes().get(pathi) instanceof CLGConstraintNode) {
							CLGConstraintNode clgconsn = (CLGConstraintNode) pathObj.getPathNodes().get(pathi);
							if (clgconsn.getConstraint() instanceof CLGMethodInvocationNode) {
								flag = true;
								CLGMethodInvocationNode methodpath = (CLGMethodInvocationNode) clgconsn.getConstraint();
								methodarr.add(methodpath.getMethodName());
							}
						}
					}

					if (Main.boundary_analysis) {
						boolean boundary = false;
						boolean exit = false;
//						應該沒用到，先註解
//						int bound = 0;
						boolean binarysearch = false;
						
						while ((boundaryPath = boundaryEnumerator.next()) != null) {
//							2020/3/3 constrain info.
//							String tempConStr = "";
							tempConStr = "";
							for (int i = 0; i < pathObj.getPathNodes().size(); i++) {
								if (pathObj.getPathNodes().get(i) instanceof CLGConstraintNode) {
									// CLGConstraintNode cons = (CLGConstraintNode)pathObj.getPathNodes().get(i);
									CLGConstraintNode cons = (CLGConstraintNode) pathObj.getPathNodes().get(i);
									tempConStr += cons.toCLPInfo().replace("#", "") + ",";
								}
							}
							tempConStr.substring(0, (tempConStr.length() - 2));
							pathObjInfo += tempConStr + "\n"; // pathObjInfo 印出到檔案的字串

							String clpContent = clpTranslator.genPathCLP(pathObj);
							boundary = true;
							DataWriter.writeInfo(clpContent, graphClassName + graphMethodName + TestTypeSign + pathNo,
									"ecl", DataWriter.testCons_output_path);
							if (false) {
								/* black-box: clp solves twice. */
								clpSolver.solving(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
										graphMethodRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal");
								testDatas.add(clpSolver.getTestData());
							} else {
								/* white-box: clp solves once. */
//								只有bound, unbound會用到，應該
								if (clpContent.contains("...") || clpContent.contains("Bound")
										|| clpContent.contains("#=x")) {
									if (clpContent.contains("...bound") || clpContent.contains("Bound")) {
										if (!clpContent.contains("Bound_pre")) {
											bound1_1 = true;
											bound4_1 = true;
											bound4_2 = true;
											bound4_3 = true;
											bound4_4 = true;
											totalbound = 5;
										}
										if (!bound0_1) {
											clpTranslator.setBounded(0);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(0);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound0_1 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!bound1_0) {
											clpTranslator.setBounded(1);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(0);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound1_0 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!bound1_1) {
											clpTranslator.setBounded(1);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(1);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound1_1 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!bound4_0) {
											clpTranslator.setBounded(4);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(0);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound4_0 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!bound4_1) {
											clpTranslator.setBounded(4);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(1);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound4_1 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!bound4_2) {
											clpTranslator.setBounded(4);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(2);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound4_2 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}

										if (!bound4_3) {
											clpTranslator.setBounded(4);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(3);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound4_3 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}

										if (!bound4_4) {
											clpTranslator.setBounded(4);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(4);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound4_4 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}

										if (!clpContent.contains("It")) {
											this.removeCoveredBranches(intersectionalEdge);

										} else if (totalbound == 8) {
											this.removeCoveredBranches(intersectionalEdge);
											exit = true;
											break;
										}
										clpTranslator.setBounded(-1);

									} else {
										boolean array4solution = false;
										boolean containIterate = false;

										String cutclp = clpContent.substring(clpContent.indexOf("#=x") + 2);
										int lower;
										if (clpContent.contains("Row"))
											lower = 0;
										else if (clpContent.contains("#=x")) {
											lower = 0;
										} else
											lower = Integer.parseInt(cutclp.substring(0, cutclp.indexOf("...")));
										String cutclp2 = clpContent.substring(clpContent.indexOf("#=x"));
										boolean unbound = false;
										int upper;
										if (clpContent.contains("Row")) {
											unbound = true;
											upper = 3;
										} else if (cutclp2.contains("...*") || clpContent.contains("#=x")) {
											unbound = true;
											upper = 4;
										} else
											upper = Integer.parseInt(
													cutclp2.substring(cutclp2.indexOf("...") + 3, cutclp2.indexOf(",")));
										int interval = upper - lower;
//										應該沒用到，先註解
//										int times = 1;
										boolean exitUnbound = false;
										ArrayList<Integer> index = new ArrayList<Integer>();
										for (int number = lower, i = 1; i <= interval && i <= 3; number++, i++) {
											index.add(number);
										}
										index.add(upper);

										for (int j = 0; j < 3; j++) {
											switch (j) {
											case 0:
												clpTranslator.setIterateTimes(lower);
												break;
											case 1:
												if (j < interval) {
													clpTranslator.setIterateTimes(lower + 1);
													if (clpContent.contains("Unsort"))
														clpTranslator.setArrayCount(factorial(lower + 1));
												} else {
													clpTranslator.setIterateTimes(upper);
													if (clpContent.contains("Unsort"))
														clpTranslator.setArrayCount(factorial(upper));
													exitUnbound = true;
												}
												break;
											case 2:

												clpTranslator.setIterateTimes(upper);
												//注意!這個註解不是這邊程式碼的作者寫的，exitUnbound用於跳出迴圈
												//unbounded範例不同於bounded，只要產生014的測試資料就可以，中間的23被這部分的程式跳掉了
												//具體做法看不懂
												exitUnbound = true;
												if (clpContent.contains("Unsort"))
													clpTranslator.setArrayCount(factorial(upper));
												break;
											}
											clpContent = clpTranslator.genPathCLP(pathObj);

											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												// Main.boundaryhavesolution=true;
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												
												pathObjInfo += "feasible\n";
												isFeasible = true;
												
												if (unbound) {
													if (j == 2 && clpContent.contains("It"))
														containIterate = true;
													if (j == 2) {
														array4solution = true;
														this.removeCoveredBranches(intersectionalEdge);
													}
													accept++;
												}
											}
											if (exitUnbound) {
												skipSIPPart = true;
												break;
											}
											pathNo++;
										}
										if ((array4solution && containIterate)) {
											if (clpContent.contains("testSortedArray"))
												binarysearch = true;
											exit = true;
										} else if (!array4solution || !containIterate) {

											clpTranslator.setIterateTimes(-1);
											clpTranslator.setArrayCount(0);
											continue;
										} else {
											this.removeCoveredBranches(intersectionalEdge);
											exit = true;
											break;
										}
									}
								} else {
//									bound, unbound以外
									String arraysize = "";
									if (clpContent.contains("Size_pre#=")) {
										String tempclp = clpContent.substring(clpContent.indexOf("Size_pre#=") + 10);
										arraysize = tempclp.substring(0, tempclp.indexOf(","));
										if (clpContent.contains("Unsort"))
											clpTranslator.setArrayCount(factorial(Integer.parseInt(arraysize)));
									} else if (clpContent.contains("Row#=")) {
										String tempclp = clpContent.substring(clpContent.indexOf("Row_pre#=") + 9);
										arraysize = tempclp.substring(0, tempclp.indexOf(","));
										if (clpContent.contains("Unsort"))
											clpTranslator.setArrayCount(factorial(Integer.parseInt(arraysize)));
									}
//									for路徑縮減
									if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
											graphRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal")) {
										testDatas.add(clpSolver.getTestData());
										
										pathObjInfo += "feasible\n";
										isFeasible = true;

//									將覆蓋到的edge從coverageCheckingTable移除
										this.removeCoveredBranches(intersectionalEdge);
										if (clpContent.contains("Size_pre#=")) {
											if ((clpContent.contains("It_" + (arraysize + 2)) && Main.msort)
													|| (clpContent.contains("It_" + arraysize) && !Main.msort)) {
												if (clpContent.contains("testSortedArray"))
													binarysearch = true;
												exit = true;
											}
										}
										if (Main.twoD) {
											exit = true;
										}
										pathNo++;
									}
								}
							}
						}
						
						if (exit || boundexit) {

							if (binarysearch && !sortedClass && !Main.criterion.equals(CriterionFactory.Criterion.dc)) {
								sortedClass = true;

							} else
								break;
						}
						if ((boundaryPath = boundaryEnumerator.next()) == null && !boundary && !Main.twoD && !Main.issort) {
//							紀錄路徑資訊
//							String tempConStr = "";
							tempConStr = "";
							for (int i = 0; i < pathObj.getPathNodes().size(); i++) {
								if (pathObj.getPathNodes().get(i) instanceof CLGConstraintNode) {
									CLGConstraintNode cons = (CLGConstraintNode) pathObj.getPathNodes().get(i);
									tempConStr += cons.toCLPInfo().replace("#", "") + ",";
								}
							}
							tempConStr.substring(0, (tempConStr.length() - 2));
							pathObjInfo += tempConStr + "\n";

							String clpContent = clpTranslator.genPathCLP(pathObj);

							boundary = true;
							DataWriter.writeInfo(clpContent, graphClassName + graphMethodName + TestTypeSign + pathNo,
									"ecl", DataWriter.testCons_output_path);
							if (false) {
								/* black-box: clp solves twice. */
								clpSolver.solving(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
										graphMethodRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal");
								testDatas.add(clpSolver.getTestData());
							} else {
								/* white-box: clp solves once. */
								if (clpContent.contains("...") || clpContent.contains("Bound")
										|| clpContent.contains("#=x")) {
									if (clpContent.contains("...bound") || clpContent.contains("Bound")) {

										if (!clpContent.contains("Bound_pre")) {
											bound1_1 = true;
											bound4_1 = true;
											bound4_2 = true;
											bound4_3 = true;
											bound4_4 = true;
											totalbound = 5;
										}
										if (!bound0_1) {
											clpTranslator.setBounded(0);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(0);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound0_1 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!bound1_0) {
											clpTranslator.setBounded(1);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(0);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound1_0 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!bound1_1) {
											clpTranslator.setBounded(1);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(1);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound1_1 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!bound4_0) {
											clpTranslator.setBounded(4);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(0);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound4_0 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!bound4_1) {
											clpTranslator.setBounded(4);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(1);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound4_1 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!bound4_2) {
											clpTranslator.setBounded(4);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(2);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound4_2 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!bound4_3) {
											clpTranslator.setBounded(4);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(3);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound4_3 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!bound4_4) {
											clpTranslator.setBounded(4);
											clpTranslator.setArrayCount(0);
											clpTranslator.setIterateTimes(4);
											clpContent = clpTranslator.genPathCLP(pathObj);
											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												pathNo++;
												totalbound++;
												bound4_4 = true;
												pathObjInfo += "feasible\n";
												isFeasible = true;
											}
										}
										if (!clpContent.contains("It")) {
											this.removeCoveredBranches(intersectionalEdge);

										} else if (totalbound == 8) {
											this.removeCoveredBranches(intersectionalEdge);
											exit = true;
											break;
										}
										clpTranslator.setBounded(-1);

									} else {
										boolean array4solution = false;
										boolean containIterate = false;
										String cutclp = clpContent.substring(clpContent.indexOf("#=x") + 2);
										int lower;
										if (clpContent.contains("Row"))
											lower = 0;
										else if (clpContent.contains("#=x")) {
											lower = 0;
										} else
											lower = Integer.parseInt(cutclp.substring(0, cutclp.indexOf("...")));
										String cutclp2 = clpContent.substring(clpContent.indexOf("#=x"));
										boolean unbound = false;
										int upper;
										if (clpContent.contains("Row")) {
											unbound = true;
											upper = 3;
										} else if (cutclp2.contains("...*") || clpContent.contains("#=x")) {
											unbound = true;
											upper = 4;
										} else
											upper = Integer.parseInt(
													cutclp2.substring(cutclp2.indexOf("...") + 3, cutclp2.indexOf(",")));
										int interval = upper - lower;
//										應該沒用到，先註解
//										int times = 1;
										boolean exitUnbound = false;
										ArrayList<Integer> index = new ArrayList<Integer>();
										for (int number = lower, i = 1; i <= interval && i <= 3; number++, i++) {
											index.add(number);
										}
										index.add(upper);
										for (int j = 0; j < 3; j++) {
											switch (j) {
											case 0:
												clpTranslator.setIterateTimes(lower);
												break;
											case 1:
												if (j < interval) {
													clpTranslator.setIterateTimes(lower + 1);
													if (clpContent.contains("Unsort"))
														clpTranslator.setArrayCount(factorial(lower + 1));
												}
												break;
											case 2:
												if (Main.twoD)
													clpTranslator.setIterateTimes(2);
												else
													clpTranslator.setIterateTimes(upper);
												exitUnbound = true;
												if (clpContent.contains("Unsort"))
													clpTranslator.setArrayCount(factorial(upper));
												break;
											}
											clpContent = clpTranslator.genPathCLP(pathObj);

											DataWriter.writeInfo(clpContent,
													graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
													DataWriter.testCons_output_path);
											if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
													graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
													"Arg_post", "RetVal")) {
												TestData tempdata = clpSolver.getTestData();
												testDatas.add(tempdata);
												
												pathObjInfo += "feasible\n";
												isFeasible = true;
												
												if (unbound) {
													if (j == 2 && clpContent.contains("It"))
														containIterate = true;
													if (j == 2) {
														array4solution = true;
														this.removeCoveredBranches(intersectionalEdge);
													}
													accept++;
												}
											}
											if (exitUnbound)
												break;
											pathNo++;
										}

										if ((array4solution && containIterate))// ||boundsolution==ul)
										{
											exit = true;
										} else if (!array4solution || !containIterate) {
											clpTranslator.setIterateTimes(-1);
											clpTranslator.setArrayCount(0);
											continue;
										} else {
											this.removeCoveredBranches(intersectionalEdge);
											exit = true;
											break;
										}
									}
								} else {
									String arraysize = "";
									if (clpContent.contains("Size_pre#=")) {
										String tempclp = clpContent.substring(clpContent.indexOf("Size_pre#=") + 10);
										arraysize = tempclp.substring(0, tempclp.indexOf(","));
										if (clpContent.contains("Unsort"))
											clpTranslator.setArrayCount(factorial(Integer.parseInt(arraysize)));
									}
									DataWriter.writeInfo(clpContent,
											graphClassName + graphMethodName + TestTypeSign + clpTranslator.getPathNumber(),
											"ecl", DataWriter.testCons_output_path);
									if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
											graphRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal")) {

										testDatas.add(clpSolver.getTestData());
										pathObjInfo += "feasible\n";
										isFeasible = true;

										if (clpContent.contains("Size_pre#=") || clpContent.contains("testSortedArray")) {
											System.out.println("NOOOOOOOOOOOOOOO");
											if ((clpContent.contains("It_" + (arraysize + 2))
													&& (Main.msort || clpContent.contains("testSortedArray")))
													|| (clpContent.contains("It_" + arraysize)
															&& (!Main.msort || clpContent.contains("testSortedArray")))) {
												if (clpContent.contains("testSortedArray")) {
													if (!sortedClass
															&& !Main.criterion.equals(CriterionFactory.Criterion.dc)) {
														sortedClass = true;
													} else
														this.removeCoveredBranches(intersectionalEdge);
												} else
													// break;
													this.removeCoveredBranches(intersectionalEdge);
											}
										}
										pathNo++;
									}

								}
							}
						}
						
//						for路徑縮減  最短不可行路徑
//						如果該條路徑的所有邊界路徑都為infeasible，則對該條路徑找尋最短不可行路徑
						String SIP = ""; //SIP -> shortestInfeasiblePath

						if(!isFeasible && skipSIPPart == false) {
							pathObjInfo += "This path is infeasible!\n";
							
							//20200616試驗
//							preRemovalInfeasiblePathManager(pathObj, graphClassName, graphMethodName, pathNo, graphIsConstructor, graphRetType);
							
							//20200808修改為僅在MCC覆蓋度下使用CCS方法
							if(Main.criterion == Criterion.mcc) {
								preRemovalInfeasiblePathManager(pathObj, graphClassName, graphMethodName, pathNo, graphIsConstructor, graphRetType);
							}
							
//							binary search:時間複雜度為O(log n)
//							infeasibleTilStartNode = false;
//							CLGNode endNode = pathObj.getLastNode();
//							String clpContent = clpTranslator.genPathCLP(pathObj);
////							pathObj = SIPBinarySearch(pathObj, graphClassName, graphMethodName, pathNo, graphIsConstructor, graphRetType);
//							pathObj = SIPSearch(clpContent, pathObj, graphClassName, graphMethodName, pathNo, graphIsConstructor, graphRetType);
//							
////							找到SIP後對其做後續處理
////							test, 如果整條路徑直到startNode都找不到解的話
//							if(infeasibleTilStartNode) {
////								不將此SIP放入SIPNodesList中
//								SIP = pathObj.toGetPathInfo() + "\n";
//								pathObjInfo += SIP + "This path will not enter SIP check table!\n";
//							}else {
//								SIPNodes = pathObj.getPathNodes();
//								
////								test
//								String temp2 = "";
//								List<String> tempCon = new ArrayList<>();
//								List<String> tempList = new ArrayList<>();
//								
////								找到衝突限制組
////								tempList = findConflictConstraint(pathObj, endNode, graphClassName, graphMethodName, pathNo, graphIsConstructor, graphRetType);
//								tempList = CCSSearch(clpContent, pathObj, endNode, graphClassName, graphMethodName, pathNo, graphIsConstructor, graphRetType);
//								
//								if(tempList.size() != 0) {
//									for(String conflict : tempList) {
//										tempCon.add(conflict);
//									}
//									
////									SIP最後一個節點，必須加入
//									CLGConstraintNode cons2 = (CLGConstraintNode) pathObj.getPathNodes().get(pathObj.getPathNodes().size()-1);
//									temp2 += cons2.toCLPInfo() + ",";
////									temp2 += clgPathEnumerator.getCLGht().get(cons2) +",";
//									tempCon.add(temp2);
//									
//									conflictConstraints.add(tempCon);
//									
//									pathObjInfo += "conflict constraint set: ";
//									for(String s : tempCon) {
//										pathObjInfo += s + " ";
//									}
//									pathObjInfo += "\n";
//								}
//
//								SIPNodesList.add(SIPNodes);
//								SIP = pathObj.toGetPathInfo() + "\n";
//								pathObjInfo += SIP ;
//							}
						}
//						clpTranslator.setShortestInfeai(false);
						
					} else {
//						以下是不做邊界值分析，SIP&CCS還沒有做
						String clpContent = clpTranslator.genPathCLP(pathObj);
						if (false) {
							/* black-box: clp solves twice. */
							clpSolver.solving(graphClassName, graphMethodName, clpTranslator.getPathNumber(), 1,
									graphIsConstructor, graphMethodRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post",
									"RetVal");
							testDatas.add(clpSolver.getTestData());
						} else {
							/* white-box: clp solves once. */
							if (clpContent.contains("...") || clpContent.contains("Bound") || clpContent.contains("#=x")) {
								if (clpContent.contains("...bound") || clpContent.contains("Bound")) {
									System.out.println("true in");
									if (!clpContent.contains("Bound_pre")) {
										bound1_1 = true;
										bound4_1 = true;
										bound4_2 = true;
										bound4_3 = true;
										bound4_4 = true;
										totalbound = 5;
									}
									if (!bound0_1) {
										clpTranslator.setBounded(0);
										clpTranslator.setArrayCount(0);
										clpTranslator.setIterateTimes(0);
										clpContent = clpTranslator.genPathCLP(pathObj);
										DataWriter.writeInfo(clpContent,
												graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
												DataWriter.testCons_output_path);
										if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
												graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
												"Arg_post", "RetVal")) {
											TestData tempdata = clpSolver.getTestData();
											testDatas.add(tempdata);
											pathNo++;
											totalbound++;
											bound0_1 = true;
										}
									}
									if (!bound1_0) {
										clpTranslator.setBounded(1);
										clpTranslator.setArrayCount(0);
										clpTranslator.setIterateTimes(0);
										clpContent = clpTranslator.genPathCLP(pathObj);
										DataWriter.writeInfo(clpContent,
												graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
												DataWriter.testCons_output_path);
										if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
												graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
												"Arg_post", "RetVal")) {
											TestData tempdata = clpSolver.getTestData();
											testDatas.add(tempdata);
											pathNo++;
											totalbound++;
											bound1_0 = true;
										}
									}
									if (!bound1_1) {
										clpTranslator.setBounded(1);
										clpTranslator.setArrayCount(0);
										clpTranslator.setIterateTimes(1);
										clpContent = clpTranslator.genPathCLP(pathObj);
										DataWriter.writeInfo(clpContent,
												graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
												DataWriter.testCons_output_path);
										if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
												graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
												"Arg_post", "RetVal")) {
											TestData tempdata = clpSolver.getTestData();
											testDatas.add(tempdata);
											pathNo++;
											totalbound++;
											bound1_1 = true;
										}
									}
									if (!bound4_0) {
										clpTranslator.setBounded(4);
										clpTranslator.setArrayCount(0);
										clpTranslator.setIterateTimes(0);
										clpContent = clpTranslator.genPathCLP(pathObj);
										DataWriter.writeInfo(clpContent,
												graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
												DataWriter.testCons_output_path);
										if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
												graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
												"Arg_post", "RetVal")) {
											TestData tempdata = clpSolver.getTestData();
											testDatas.add(tempdata);
											pathNo++;
											totalbound++;
											bound4_0 = true;
										}
									}
									if (!bound4_1) {
										clpTranslator.setBounded(4);
										clpTranslator.setArrayCount(0);
										clpTranslator.setIterateTimes(1);
										clpContent = clpTranslator.genPathCLP(pathObj);
										DataWriter.writeInfo(clpContent,
												graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
												DataWriter.testCons_output_path);
										if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
												graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
												"Arg_post", "RetVal")) {
											TestData tempdata = clpSolver.getTestData();
											testDatas.add(tempdata);
											pathNo++;
											totalbound++;
											bound4_1 = true;
										}
									}
									if (!bound4_2) {
										clpTranslator.setBounded(4);
										clpTranslator.setArrayCount(0);
										clpTranslator.setIterateTimes(2);
										clpContent = clpTranslator.genPathCLP(pathObj);
										DataWriter.writeInfo(clpContent,
												graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
												DataWriter.testCons_output_path);
										if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
												graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
												"Arg_post", "RetVal")) {
											TestData tempdata = clpSolver.getTestData();
											testDatas.add(tempdata);
											pathNo++;
											totalbound++;
											bound4_2 = true;
										}
									}
									if (!bound4_3) {
										clpTranslator.setBounded(4);
										clpTranslator.setArrayCount(0);
										clpTranslator.setIterateTimes(3);
										clpContent = clpTranslator.genPathCLP(pathObj);
										DataWriter.writeInfo(clpContent,
												graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
												DataWriter.testCons_output_path);
										if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
												graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
												"Arg_post", "RetVal")) {
											TestData tempdata = clpSolver.getTestData();
											testDatas.add(tempdata);
											pathNo++;
											totalbound++;
											bound4_3 = true;
										}
									}
									if (!bound4_4) {
										clpTranslator.setBounded(4);
										clpTranslator.setArrayCount(0);
										clpTranslator.setIterateTimes(4);
										clpContent = clpTranslator.genPathCLP(pathObj);
										DataWriter.writeInfo(clpContent,
												graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
												DataWriter.testCons_output_path);
										if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
												graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
												"Arg_post", "RetVal")) {
											TestData tempdata = clpSolver.getTestData();
											testDatas.add(tempdata);
											pathNo++;
											totalbound++;
											bound4_4 = true;
										}
									}
									if (!clpContent.contains("It")) {
										this.removeCoveredBranches(intersectionalEdge);

									} else if (totalbound == 8) {
										this.removeCoveredBranches(intersectionalEdge);
										if (!clgPathEnumerator.hasNext())
											break;
										else {
											clpTranslator.setBounded(-1);
											clpTranslator.setIterateTimes(-1);
										}
									}
									clpTranslator.setBounded(-1);

								} else {
									boolean array4solution = false;
									boolean arraysolution = false;
									boolean containIterate = false;

									String cutclp = clpContent.substring(clpContent.indexOf("#=x") + 2);
									int lower;
									if (clpContent.contains("Row"))
										lower = 0;
									else if (clpContent.contains("#=x")) {
										lower = 0;
									} else
										lower = Integer.parseInt(cutclp.substring(0, cutclp.indexOf("...")));
									String cutclp2 = clpContent.substring(clpContent.indexOf("#=x"));
									boolean unbound = false;
									int upper;
									if (clpContent.contains("Row")) {
										unbound = true;
										upper = 3;
									} else if (cutclp2.contains("...*") || clpContent.contains("#=x")) {
										unbound = true;
										upper = 4;
									} else if (cutclp2.contains("0...,")) {
										upper = 0;
									} else
										upper = Integer.parseInt(
												cutclp2.substring(cutclp2.indexOf("...") + 3, cutclp2.indexOf(",")));
									int interval = upper - lower;
									ArrayList<Integer> index = new ArrayList<Integer>();
									boolean exit = false, arrayUpper = false;

									for (int number = lower, i = 1; i <= interval && i <= 3; number++, i++) {
										index.add(number);
									}
									index.add(upper);

									for (int j = 0; j < 3; j++) {
										switch (j) {
										case 0:
											clpTranslator.setIterateTimes(lower);
											break;
										case 1:
											if (j < interval) {
												clpTranslator.setIterateTimes(lower + 1);
											}
											break;
										case 2:
											clpTranslator.setIterateTimes(upper);
											exit = true;
											if (clpContent.contains("Unsort"))
												clpTranslator.setArrayCount(factorial(upper));
											break;
										}
										clpContent = clpTranslator.genPathCLP(pathObj);

										DataWriter.writeInfo(clpContent,
												graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
												DataWriter.testCons_output_path);
										if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
												graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
												"Arg_post", "RetVal")) {
											TestData tempdata = clpSolver.getTestData();
											testDatas.add(tempdata);
											if (unbound) {
												if (j == 2 && clpContent.contains("It"))
													containIterate = true;

												if (j == 2) {
													array4solution = true;
													this.removeCoveredBranches(intersectionalEdge);
												}
												accept++;
											}
											pathNo++;
										}

										if (exit)
											break;
									}
									if ((array4solution && containIterate)) {
										if (clpContent.contains("testSortedArray")) {
											if ((!sortedClass && !Main.criterion.equals(CriterionFactory.Criterion.dc))
													|| (clpContent.contains("BinarySearch") && !sortedClass)) {
												sortedClass = true;
												containIterate = false;
											} else
												break;
										} else
											break;
									} else if (!array4solution || !containIterate) {
										clpTranslator.setIterateTimes(-1);
										clpTranslator.setArrayCount(0);
										continue;
									} else {
										this.removeCoveredBranches(intersectionalEdge);
										break;
									}
								}
							} else {
								String arraysize = "";
								if (clpContent.contains("Size_pre")) {
									String tempclp = clpContent.substring(clpContent.indexOf("Size_pre#=") + 10);
									arraysize = tempclp.substring(0, tempclp.indexOf(","));
									if (clpContent.contains("Unsort"))
										clpTranslator.setArrayCount(factorial(Integer.parseInt(arraysize)));
								}

								DataWriter.writeInfo(clpContent,
										graphClassName + graphMethodName + TestTypeSign + clpTranslator.getPathNumber(),
										"ecl", DataWriter.testCons_output_path);
								if (clpSolver.solving(graphClassName, graphMethodName, clpTranslator.getPathNumber(), 1,
										graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post",
										"RetVal")) {

									TestData tempdata = clpSolver.getTestData();
									testDatas.add(tempdata);
									this.removeCoveredBranches(intersectionalEdge);

									if ((clpContent.contains("It_" + (arraysize + 2)) && Main.msort)
											|| (clpContent.contains("It_" + arraysize) && !Main.msort)) {
										if (clpContent.contains("testSortedArray")) {
											if ((!sortedClass && !Main.criterion.equals(CriterionFactory.Criterion.dc))
													|| (clpContent.contains("BinarySearch") && !sortedClass)) {
												sortedClass = true;
											} else
												break;
										} else
											break;
									}
								}
							}
						}
					}
//					目前DUP部分未使用，
					if (Main.criterion.equals(Criterion.dcdup) || Main.criterion.equals(Criterion.dccdup)
							|| Main.criterion.equals(Criterion.mccdup)) {
						System.out.println("part 3 ");
						if (clpSolver.getTestDataclass() == null) {
							for (int dupi = 0; dupi < nowDUP.size(); dupi++) {
								int count = (int) this.coverageDUPCheckingTable.get(nowDUP.get(dupi));
								this.coverageDUPCheckingTable.put(nowDUP.get(dupi), (count + 1));
							}
							this.infeasiblePath.add(pathObj);
							this.removeCoveredBranches(intersectionalEdge);
						} else {// sol
							for (int dupi = 0; dupi < nowDUP.size(); dupi++) {
								this.feasible_path.add(pathObj);
								this.feasibledup.add(nowDUP.get(dupi));
								this.coverageDUPCheckingTable.remove(nowDUP.get(dupi));
								this.removeCoveredBranches(intersectionalEdge);
							}
							List<CLGNode> new_path1 = clgPathEnumerator.filterConstraintNode(pathObj.getPathNodes());
							CLGPath clgp = new CLGPath(new_path1);
							saveTDPathDUP += "Path = " + clgp.toGetPathInfo();
							for (DUP p : nowDUP) {
								saveTDPathDUP += "\n" + p.DUP2Str();
							}
							saveTDPathDUP += "\n" + clpSolver.getTestData() + "\n\n";
						}
					}
					path += pathObj.toGetPathInfo() + "\n";
					System.out.println("path:" + pathObj.toGetPathInfo());

				}
			}

//		matchConflictConstraints
//		}
		
//		寫出路徑資料
		pathObjInfo = pathObjInfo.replaceAll("[\\(\\)]", "");
		pathObjInfo = pathObjInfo.replaceAll("CLGStartNode", "[0]");
		pathObjInfo = pathObjInfo.replaceAll("CLGEndNode", "[-1]");
		DataWriter.writeInfo(pathObjInfo, graphClassName + graphMethodName + "_pathInfo", "txt",
				DataWriter.testPath_output_path);
		/*
		 * try { FileWriter fWriter=new FileWriter(
		 * "C:\\Users\\chienLung\\tcgen\\examples\\output\\CLG\\TestDatas\\path.txt");
		 * fWriter.write(path); fWriter.flush(); fWriter.close(); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		saveTDPathDUP += "\n\nfeasiblepathsize = " + feasible_path.size() + "\n"; //
		System.out.println("feasiblepathsize = " + feasible_path.size());
		for (CLGPath p : feasible_path) {
			System.out.println(p.toGetPathInfo());
			List<CLGNode> new_path1 = clgPathEnumerator.filterConstraintNode(p.getPathNodes());
			CLGPath clgp = new CLGPath(new_path1);
			saveTDPathDUP += clgp.toGetPathInfo() + "\n";
		}

//		目前DUP部分未使用
		if (Main.criterion.equals(Criterion.dcdup) || Main.criterion.equals(Criterion.dccdup)
				|| Main.criterion.equals(Criterion.mccdup)) {
			System.out.println("part 4 ");
			ArrayList<DUP> indup = new ArrayList<DUP>();
			for (Object dup : this.coverageDUPCheckingTable.keySet()) {
				if (dup instanceof DUP) {
					if (!indup.contains((DUP) dup))
						indup.add((DUP) dup);
				}
			}
			for (int i = 0; i < indup.size(); i++) {
				if (!this.infeasibledup.contains(indup.get(i)))
					this.infeasibledup.add(indup.get(i));
				if (this.coverageDUPCheckingTable != null) {
					if (this.coverageDUPCheckingTable.containsKey(indup.get(i)))
						this.coverageDUPCheckingTable.remove(indup.get(i));
				}
			}
			saveTDPathDUP += "\n\nfeasibledup size = " + feasibledup.size();//
			System.out.println("feasibledup size = " + feasibledup.size());
			for (int i = 0; i < feasibledup.size(); i++) {
				saveTDPathDUP += "\n" + feasibledup.get(i).DUP2Str();//
			}

			saveTDPathDUP += "\n\ninfeasibledup size = " + infeasibledup.size();//
			System.out.println("infeasibledup size = " + infeasibledup.size());
			for (int i = 0; i < infeasibledup.size(); i++) {
				saveTDPathDUP += "\n" + infeasibledup.get(i).DUP2Str();//
			}
		}

		String content = "";
		for (CLGPath p : feasible_path) {
			System.out.println(p.toGetPathInfo());
		}
		List<TestData> newDatas = new ArrayList<TestData>();
		List<TestData> tempDatas = new ArrayList<TestData>();
		for (TestData td : testDatas) {
			if (Main.twoD) {
				String obj_pre = td.getObjPre();
				if (obj_pre.contains("arity")) {
					String[] cutobjpre = obj_pre.split("arity");
					ArrayList<String> all_row_element_pre = new ArrayList<String>();
					for (int i = 2; i < cutobjpre.length; i++)
						all_row_element_pre.add(cutobjpre[i]);
					int row_pre = 0;
					int col_pre = 0;
					String obj_list = "[[";
					for (String element : all_row_element_pre) {
						row_pre++;
						col_pre = 0;
						obj_list += "[";
						element = element.substring(0, element.indexOf("]"));
						String[] elements = element.split(" arg");
						for (int i = 1; i < elements.length; i++) {
							obj_list += elements[i].substring(elements[i].indexOf("=") + 1) + ",";
							col_pre++;
						}
						obj_list = obj_list.substring(0, obj_list.length() - 1) + "],";
					}
					if (all_row_element_pre.size() > 0) {
						obj_list = obj_list.substring(0, obj_list.length() - 1);
					}
					obj_list += "]";
					if (Main.symbolTable.getAttributeMap().containsKey("row"))
						obj_list += ", " + row_pre + ", " + col_pre;
					obj_list += "]";
					td.setObjPre(obj_list);
				}
				String arg_pre = td.getArgPre();
				if (arg_pre.contains("arity")) {
					String[] cutargpre = arg_pre.split("arity");
					ArrayList<String> all_row_element_arg_pre = new ArrayList<String>();
					for (int i = 2; i < cutargpre.length; i++)
						all_row_element_arg_pre.add(cutargpre[i]);
					int arg_row_pre = 0;
					int arg_col_pre = 0;
					String arg_list = "[[";
					for (String element : all_row_element_arg_pre) {
						arg_row_pre++;
						arg_col_pre = 0;
						arg_list += "[";
						element = element.substring(0, element.indexOf("]"));
						String[] elements = element.split(" arg");
						for (int i = 1; i < elements.length; i++) {
							arg_list += elements[i].substring(elements[i].indexOf("=") + 1) + ",";
							arg_col_pre++;
						}
						arg_list = arg_list.substring(0, arg_list.length() - 1) + "],";
					}
					if (all_row_element_arg_pre.size() > 0) {
						arg_list = arg_list.substring(0, arg_list.length() - 1);
					}
					arg_list += "]";
					if (Main.symbolTable.getAttributeMap().containsKey("row"))
						arg_list += ", " + arg_row_pre + ", " + arg_col_pre;
					arg_list += "]";
					td.setArgPre(arg_list);
				}
				String arg_post = td.getArgPost();
				if (arg_post.contains("arity")) {
					String[] cutargpost = arg_post.split("arity");
					ArrayList<String> all_row_element_arg_post = new ArrayList<String>();
					for (int i = 2; i < cutargpost.length; i++)
						all_row_element_arg_post.add(cutargpost[i]);
					int arg_row_post = 0;
					int arg_col_post = 0;
					String arg_list = "[[";
					for (String element : all_row_element_arg_post) {
						arg_row_post++;
						arg_col_post = 0;
						arg_list += "[";
						element = element.substring(0, element.indexOf("]"));
						String[] elements = element.split(" arg");
						for (int i = 1; i < elements.length; i++) {
							arg_list += elements[i].substring(elements[i].indexOf("=") + 1) + ",";
							arg_col_post++;
						}
						arg_list = arg_list.substring(0, arg_list.length() - 1) + "],";
					}
					if (all_row_element_arg_post.size() > 0) {
						arg_list = arg_list.substring(0, arg_list.length() - 1);
					}
					arg_list += "]";
					if (Main.symbolTable.getAttributeMap().containsKey("row"))
						arg_list += ", " + arg_row_post + ", " + arg_col_post;
					arg_list += "]";
					td.setArgPost(arg_list);
				}
				String obj_post = td.getObjPost();
				String[] cutobjpost = obj_post.split("arity");
				ArrayList<String> all_row_element_post = new ArrayList<String>();
				for (int i = 2; i < cutobjpost.length; i++)
					all_row_element_post.add(cutobjpost[i]);
				int row_post = 0;
				int col_post = 0;
				String obj_list = "[[";
				for (String element : all_row_element_post) {
					row_post++;
					col_post = 0;
					obj_list += "[";
					element = element.substring(0, element.indexOf("]"));
					String[] elements = element.split(" arg");
					for (int i = 1; i < elements.length; i++) {
						obj_list += elements[i].substring(elements[i].indexOf("=") + 1) + ",";
						col_post++;
					}
					obj_list = obj_list.substring(0, obj_list.length() - 1) + "],";
				}
				if (all_row_element_post.size() > 0) {
					obj_list = obj_list.substring(0, obj_list.length() - 1);
				}
				obj_list += "]";
				if (Main.symbolTable.getAttributeMap().containsKey("row"))
					obj_list += ", " + row_post + ", " + col_post;
				obj_list += "]";
				td.setObjPost(obj_list);
				String ret_val = td.getRetVal();
				if (ret_val.contains("arity")) {
					String[] cutret = ret_val.split("arity");
					ArrayList<String> all_row_element_ret = new ArrayList<String>();
					for (int i = 2; i < cutret.length; i++)
						all_row_element_ret.add(cutret[i]);
					int row_ret = 0;
					int col_ret = 0;
					String ret_list = "[[";
					for (String element : all_row_element_ret) {
						row_ret++;
						col_ret = 0;
						ret_list += "[";
						element = element.substring(0, element.indexOf("]"));
						String[] elements = element.split(" arg");
						for (int i = 1; i < elements.length; i++) {
							ret_list += elements[i].substring(elements[i].indexOf("=") + 1) + ",";
							col_ret++;
						}
						ret_list = ret_list.substring(0, ret_list.length() - 1) + "],";
					}
					if (all_row_element_ret.size() > 0) {
						ret_list = ret_list.substring(0, ret_list.length() - 1);
					}
					ret_list += "]";
					if (Main.symbolTable.getAttributeMap().containsKey("row"))
						ret_list += ", " + row_ret + ", " + col_ret;
					ret_list += "]";
					td.setRetVal(ret_list);
				}
				tempDatas.add(td);
			} else if (td.getObjPre().contains("[[")) {
				if (Main.issort) {
					if (td.getObjPre().indexOf("],") == td.getObjPre().lastIndexOf("],")) {
						String retval = td.getRetVal();
						// retval=retval.replaceAll("]]", "]");
						// retval=retval.substring(1);
						td.setRetVal(retval);
						tempDatas.add(td);
					} else {
						if (Main.symbolTable.getAttributeMap().containsKey("size")) {
							String origanobjpre = td.getObjPre().substring(1, td.getObjPre().length() - 1);
							String[] token1 = origanobjpre.split("], ");
							for (int i = 0; i < token1.length - 1; i++) {
								token1[i] = "[" + token1[i] + "], " + token1[token1.length - 1] + "]";
							}
							String origanobjpost = td.getObjPost().substring(2, td.getObjPost().lastIndexOf("]],"));
							String[] token2 = origanobjpost.split("], ");
							for (int i = 0; i < token2.length; i++) {
								token2[i] = "[" + token2[i] + "], "
										+ td.getObjPost().substring(td.getObjPost().lastIndexOf("], ") + 3);
							}
							String result = td.getRetVal().substring(1, td.getRetVal().lastIndexOf("]],"));
							String[] token3 = result.split("]], ");
							for (int i = 0; i < token3.length; i++) {
								token3[i] = (token3[i] + td.getRetVal().substring(td.getRetVal().lastIndexOf("]],")))
										.replaceAll("]]", "]");

							}
							for (int i = 0; i < token2.length; i++)
								tempDatas.add(new TestData(td.getClassName(), td.getMethodName(), td.getPathId(),
										td.getTestDataId(), i, td.isConstructor(), td.getRetType(), token1[i],
										td.getArgPre(), token2[i], td.getArgPost(), token3[i]));
						} else {
							String origanobjpre = td.getObjPre().substring(1, td.getObjPre().length() - 1);
							String[] token1 = origanobjpre.split("], ");
							for (int i = 0; i < token1.length - 1; i++) {
								token1[i] = "[" + token1[i] + "]]";
							}
							String origanobjpost = td.getObjPost().substring(2, td.getObjPost().lastIndexOf("]") - 1);
							String[] token2 = origanobjpost.split("], ");
							for (int i = 0; i < token2.length; i++) {
								if (i < 2)
									token2[i] = token2[i] + "]";
								token2[i] = "[" + token2[i] + "]";
							}
							String result = td.getRetVal().substring(1, td.getRetVal().lastIndexOf("]"));
							String[] token3 = result.split("]], ");
							for (int i = 0; i < token3.length - 1; i++) {
								token3[i] = token3[i] + "]]";
							}
							for (int i = 0; i < token2.length; i++)
								tempDatas.add(new TestData(td.getClassName(), td.getMethodName(), td.getPathId(),
										td.getTestDataId(), i, td.isConstructor(), td.getRetType(), token1[i],
										td.getArgPre(), token2[i], td.getArgPost(), token3[i]));

						}
					}
				} else {
					tempDatas.add(td);
				}
			} else
				tempDatas.add(td);
		}
		// for (TestData td : testDatas) {
		for (TestData td : tempDatas) {
			String temp_content = "OBJ_PRE = " + td.getObjPre() + ", ARG_PRE = " + td.getArgPre() + ",OBJ_POST = "
					+ td.getObjPost() + ",ARG_POST = " + td.getArgPost() + ", RETVAL = " + td.getRetVal()
					+ ", EXCEPTION =" + td.getException() + "\n";
			if (!content.contains(temp_content))// 加
			{
				content += temp_content;// "OBJ_PRE = " + td.getObjPre() + ", ARG_PRE = " + td.getArgPre() + ",OBJ_POST
										// = " + td.getObjPost() + ",ARG_POST = " + td.getArgPost() + ", RETVAL = " +
										// td.getRetVal() + "\n";
				newDatas.add(td);
			}
		}
		this.testDatas = newDatas;
		DataWriter.writeInfo(content, graphClassName + graphMethodName, "txt",
				DataWriter.testData_output_path);

		return this.testDatas;
	}

	// this mine
	public List<TestData> genTestSuite(String criterion, String output_path) {
		this.clpTranslator = new CLPTranslator();
		String graphClassName = ((CLGStartNode) this.targetCLG.getStartNode()).getFirstUpperClassName();
		String graphMethodName = ((CLGStartNode) this.targetCLG.getStartNode()).getFirstUpperMethodName();
		String graphRetType = ((CLGStartNode) this.targetCLG.getStartNode()).getReturnType();
		boolean graphIsConstructor = ((CLGStartNode) this.targetCLG.getStartNode()).isConstructor();
		CLGPath pathObj, boundaryPath;
		String graphMethodRetType = "";
		String saveTDPathDUP = "";//
		BoundaryEnumerator boundaryEnumerator = new BoundaryEnumerator();
		int pathNo = 1;
		int accept = 0;

		String TestTypeSign = "";

//		判別測試種類為黑箱或白箱等
		if (Main.TestType == "BlackBox") {
			TestTypeSign = "B";
		} else if (Main.TestType == "WhiteBox") {
			TestTypeSign = "W";
		} else {
			TestTypeSign = "C";
		}

		while (!this.meetCriterion(criterion) /* i < 50 */ && (pathObj = clgPathEnumerator.next()) != null) {

			ArrayList<DUP> nowDUP = new ArrayList<DUP>();
			if (criterion.equals("dcdup") || criterion.equals("dccdup") || criterion.equals("mccdup")) {
				System.out.println("control part 2 ");
				List<CLGNode> new_path1 = clgPathEnumerator.filterConstraintNode(pathObj.getPathNodes());
				ArrayList<DUP> regDUP = new ArrayList<DUP>();
				CLGPath clgp = new CLGPath(new_path1);
				regDUP = clgRDA.parsePathDUP(clgp, oriDUP);
				System.out.println(" regdup " + regDUP.size());
				for (int i = 0; i < regDUP.size(); i++) {
					if (this.coverageDUPCheckingTable.containsKey(regDUP.get(i))) {
						nowDUP.add(regDUP.get(i));
					}
				}
				System.out.println("Path = " + clgp.toGetPathInfo());
				for (DUP p : nowDUP) {
					System.out.println(p.DUP2Str());
				}
			}
			// -------

			Set<CLGEdge> intersectionalEdge = new HashSet<CLGEdge>();
			for (CLGEdge b : pathObj.getEdges()) {
				if (this.coverageCheckingTable.containsKey(b)) {
					intersectionalEdge.add(b);
					coverageCheckingTable.put(b, ((Integer) coverageCheckingTable.get(b)) + 1);
				}
			}

			boundaryEnumerator.init(pathObj);

			if (intersectionalEdge.size() > 0 || nowDUP.size() > 0) {
				System.out.println("control intersectionalEdge ");
				boolean flag = false;
				ArrayList<String> methodarr = new ArrayList<String>();
				for (int pathi = 0; pathi < pathObj.getPathNodes().size(); pathi++) {
					if (pathObj.getPathNodes().get(pathi) instanceof CLGConstraintNode) {
						CLGConstraintNode clgconsn = (CLGConstraintNode) pathObj.getPathNodes().get(pathi);
						if (clgconsn.getConstraint() instanceof CLGMethodInvocationNode) {
							flag = true;
							CLGMethodInvocationNode methodpath = (CLGMethodInvocationNode) clgconsn.getConstraint();
							methodarr.add(methodpath.getMethodName());
						} // else
							// continue;
					}
				}

				if (Main.boundary_analysis) {
					boolean boundary = false;
					boolean exit = false;
					int bound = 0;
					while ((boundaryPath = boundaryEnumerator.next()) != null) {
						// CLPTranslator clpTranslator = new CLPTranslator();
						String clpContent = clpTranslator.genPathCLP(pathObj);
						boundary = true;
						DataWriter.writeInfo(clpContent, graphClassName + graphMethodName + TestTypeSign + pathNo,
								"ecl", output_path, "ecl");
						if (false) {
							/* black-box: clp solves twice. */
							clpSolver.solving(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
									graphMethodRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal",
									output_path);
							testDatas.add(clpSolver.getTestData());
						} else {
							/* white-box: clp solves once. */
							if (clpContent.contains("...")) {
								String cutclp = clpContent.substring(clpContent.indexOf("size#=") + 6);
								System.out.println("testString:" + cutclp);
								int lower = Integer.parseInt(cutclp.substring(0, cutclp.indexOf("...")));
								String cutclp2 = clpContent.substring(clpContent.indexOf("..."));
								boolean unbound = false;
								int upper;
								if (cutclp2.contains("...*")) {
									unbound = true;
									upper = 4;
								} else
									upper = Integer.parseInt(
											cutclp2.substring(cutclp2.indexOf("...") + 3, cutclp2.indexOf(",")));
								int interval = upper - lower;
								int times = 1;
								boolean exitUnbound = false;
								ArrayList<Integer> index = new ArrayList<Integer>();
								for (int number = lower, i = 1; i <= interval && i <= 3; number++, i++) {
									index.add(number);
								}
								index.add(upper);
								for (int j = 0; j < 5; j++) {
									switch (j) {
									case 0:
										clpTranslator.setIterateTimes(lower);
										break;
									case 1:
										if (j < interval) {
											clpTranslator.setIterateTimes(lower + 1);
											clpTranslator.setArrayCount(factorial(lower + 1));
										} else {
											clpTranslator.setIterateTimes(upper);
											clpTranslator.setArrayCount(factorial(upper));
										}
										break;
									case 2:
										if (j < interval && !unbound) {
											clpTranslator.setIterateTimes(lower + 2);
											clpTranslator.setArrayCount(factorial(lower + 2));
										} else {
											clpTranslator.setIterateTimes(upper);
											exitUnbound = true;
											clpTranslator.setArrayCount(factorial(upper));
										}
										break;
									case 3:
										if (j < interval && !unbound) {
											clpTranslator.setIterateTimes(upper - 1);
											clpTranslator.setArrayCount(factorial(upper) - 1);
										}
										break;
									case 4:
										if (!unbound) {
											clpTranslator.setIterateTimes(upper);
											clpTranslator.setArrayCount(factorial(upper));
										}
										break;
									default:
										break;
									}
									clpContent = clpTranslator.genPathCLP(pathObj);

									DataWriter.writeInfo(clpContent,
											graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl",
											output_path, "ecl");
									if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1,
											graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post",
											"Arg_post", "RetVal", output_path)) {
										TestData tempdata = clpSolver.getTestData();
										testDatas.add(tempdata);
										accept++;
									}
									if (exitUnbound)
										break;
									pathNo++;
								}

								if (unbound && accept >= 3) {
									exit = true;
									break;
								}
								if (accept < interval + 1 && accept < 5) {
									clpTranslator.setIterateTimes(-1);
									clpTranslator.setArrayCount(0);
									continue;
								} else {
									this.removeCoveredBranches(intersectionalEdge);
									exit = true;
									break;
								}
							} else {
								if (clpContent.contains("size#=")) {
									String arraysize = "";
									String tempclp = clpContent.substring(clpContent.indexOf("size#=") + 6);
									arraysize = tempclp.substring(0, tempclp.indexOf(","));
									clpTranslator.setArrayCount(factorial(Integer.parseInt(arraysize)));
								}
								if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
										graphRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal",
										output_path)) {
									testDatas.add(clpSolver.getTestData());
									this.removeCoveredBranches(intersectionalEdge);
									if (clpContent.contains("size#=")) {
										exit = true;
										break;
									}
									pathNo++;
								}
							}
						}

					}
					if (exit)
						break;
					if ((boundaryPath = boundaryEnumerator.next()) == null && !boundary) {// 自加
						String clpContent = clpTranslator.genPathCLP(pathObj);
						DataWriter.writeInfo(clpContent, graphClassName + graphMethodName + TestTypeSign + pathNo,
								"ecl", output_path, "ecl");
						if (false) {
							/* black-box: clp solves twice. */
							clpSolver.solving(graphClassName, graphMethodName, clpTranslator.getPathNumber(), 1,
									graphIsConstructor, graphMethodRetType, "Obj_pre", "Arg_pre", "Obj_post",
									"Arg_post", "RetVal", output_path);
							testDatas.add(clpSolver.getTestData());
						} else {
							/* white-box: clp solves once. */

							if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
									graphRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal",
									output_path)) {
								TestData tempdata = clpSolver.getTestData();
								// testDatas.add(clpSolver.getTestData());
								testDatas.add(tempdata);
								pathNo++;
								// System.out.println("clpSolver.getTestData(): " +
								// clpSolver.getTestData().toString());
								this.removeCoveredBranches(intersectionalEdge);
								if (clpContent.contains("size#="))
									break;
							}

						}
					}
				} else {
					String clpContent = clpTranslator.genPathCLP(pathObj);
					if (false) {
						/* black-box: clp solves twice. */
						clpSolver.solving(graphClassName, graphMethodName, clpTranslator.getPathNumber(), 1,
								graphIsConstructor, graphMethodRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post",
								"RetVal", output_path);
						testDatas.add(clpSolver.getTestData());
					} else {
						/* white-box: clp solves once. */
						if (clpContent.contains("...")) {
							String cutclp = clpContent.substring(clpContent.indexOf("size#=") + 6);
							System.out.println("testString:" + cutclp);
							int lower = Integer.parseInt(cutclp.substring(0, cutclp.indexOf("...")));
							String cutclp2 = clpContent.substring(clpContent.indexOf("..."));
							boolean unbound = false;
							int upper;
							if (cutclp2.contains("...*")) {
								unbound = true;
								upper = 4;
							} else
								upper = Integer
										.parseInt(cutclp2.substring(cutclp2.indexOf("...") + 3, cutclp2.indexOf(",")));
							int interval = upper - lower;
							ArrayList<Integer> index = new ArrayList<Integer>();
							boolean exit = false;
							for (int number = lower, i = 1; i <= interval && i <= 3; number++, i++) {
								index.add(number);
							}
							index.add(upper);

							for (int j = 0; j < 5; j++) {
								switch (j) {
								case 0:
									clpTranslator.setIterateTimes(lower);
									break;
								case 1:
									if (j < interval) {
										clpTranslator.setIterateTimes(lower + 1);
										clpTranslator.setArrayCount(factorial(lower + 1));
									} else {
										clpTranslator.setIterateTimes(upper);
										clpTranslator.setArrayCount(factorial(upper));
									}
									break;
								case 2:
									if (j < interval && !unbound) {
										clpTranslator.setIterateTimes(lower + 2);
										clpTranslator.setArrayCount(factorial(lower + 2));
									} else {
										clpTranslator.setIterateTimes(upper);
										exit = true;
										clpTranslator.setArrayCount(factorial(upper));
									}
									break;
								case 3:
									if (j < interval && !unbound) {
										clpTranslator.setIterateTimes(upper - 1);
										clpTranslator.setArrayCount(factorial(upper - 1));
									}
									break;
								case 4:
									if (!unbound) {
										clpTranslator.setIterateTimes(upper);
										clpTranslator.setArrayCount(factorial(upper));
									}
									break;
								default:
									break;
								}
								clpContent = clpTranslator.genPathCLP(pathObj);

								DataWriter.writeInfo(clpContent,
										graphClassName + graphMethodName + TestTypeSign + pathNo, "ecl", output_path,
										"ecl");
								if (clpSolver.solving(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
										graphRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal",
										output_path)) {
									TestData tempdata = clpSolver.getTestData();
									testDatas.add(tempdata);
									accept++;
								}

								pathNo++;
								if (exit)
									break;
							}
							if (accept < interval + 1 && accept < 5 && accept > 0) {
								if (unbound && accept >= 3)
									break;
								clpTranslator.setIterateTimes(-1);
								clpTranslator.setArrayCount(0);
								continue;
							} else {
								this.removeCoveredBranches(intersectionalEdge);
								break;
							}
						} else {
							if (clpContent.contains("size#=")) {
								String arraysize = "";
								String tempclp = clpContent.substring(clpContent.indexOf("size#=") + 6);
								arraysize = tempclp.substring(0, tempclp.indexOf(","));
								clpTranslator.setArrayCount(factorial(Integer.parseInt(arraysize)));
							}
							DataWriter.writeInfo(clpContent,
									graphClassName + graphMethodName + TestTypeSign + clpTranslator.getPathNumber(),
									"ecl", output_path, "ecl");
							if (clpSolver.solving(graphClassName, graphMethodName, clpTranslator.getPathNumber(), 1,
									graphIsConstructor, graphRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post",
									"RetVal", output_path)) {

								TestData tempdata = clpSolver.getTestData();
								// testDatas.add(clpSolver.getTestData());
								testDatas.add(tempdata);
								// System.out.println("clpSolver.getTestData(): " +
								// clpSolver.getTestData().toString());
								this.removeCoveredBranches(intersectionalEdge);
								if (clpContent.contains("size#="))
									break;
							}
						}
					}
				}
				if (criterion.equals("dcdup") || criterion.equals("dccdup") || criterion.equals("mccdup")) {
					System.out.println("part 3 ");
					if (clpSolver.getTestDataclass() == null) {
						for (int dupi = 0; dupi < nowDUP.size(); dupi++) {
							int count = (int) this.coverageDUPCheckingTable.get(nowDUP.get(dupi));
							this.coverageDUPCheckingTable.put(nowDUP.get(dupi), (count + 1));
						}
						this.infeasiblePath.add(pathObj);
						this.removeCoveredBranches(intersectionalEdge);
					} else {// sol
						for (int dupi = 0; dupi < nowDUP.size(); dupi++) {
							this.feasible_path.add(pathObj);
							this.feasibledup.add(nowDUP.get(dupi));
							this.coverageDUPCheckingTable.remove(nowDUP.get(dupi));
							this.removeCoveredBranches(intersectionalEdge);
						}
						List<CLGNode> new_path1 = clgPathEnumerator.filterConstraintNode(pathObj.getPathNodes());
						CLGPath clgp = new CLGPath(new_path1);
						saveTDPathDUP += "Path = " + clgp.toGetPathInfo();
						for (DUP p : nowDUP) {
							saveTDPathDUP += "\n" + p.DUP2Str();
						}
						saveTDPathDUP += "\n" + clpSolver.getTestData() + "\n\n";
					}
				}
				System.out.println(pathObj.toGetPathInfo());

			}
		}

		saveTDPathDUP += "\n\nfeasiblepathsize = " + feasible_path.size() + "\n"; //
		System.out.println("feasiblepathsize = " + feasible_path.size());
		for (CLGPath p : feasible_path) {
			System.out.println(p.toGetPathInfo());
			List<CLGNode> new_path1 = clgPathEnumerator.filterConstraintNode(p.getPathNodes());
			CLGPath clgp = new CLGPath(new_path1);
			saveTDPathDUP += clgp.toGetPathInfo() + "\n";
		}

		if (criterion.equals("dcdup") || criterion.equals("dccdup") || criterion.equals("mccdup")) {
			System.out.println("part 4 ");
			ArrayList<DUP> indup = new ArrayList<DUP>();
			for (Object dup : this.coverageDUPCheckingTable.keySet()) {
				if (dup instanceof DUP) {
					if (!indup.contains((DUP) dup))
						indup.add((DUP) dup);
				}
			}
			for (int i = 0; i < indup.size(); i++) {
				if (!this.infeasibledup.contains(indup.get(i)))
					this.infeasibledup.add(indup.get(i));
				if (this.coverageDUPCheckingTable != null) {
					if (this.coverageDUPCheckingTable.containsKey(indup.get(i)))
						this.coverageDUPCheckingTable.remove(indup.get(i));
				}
			}
			saveTDPathDUP += "\n\nfeasibledup size = " + feasibledup.size();//
			System.out.println("feasibledup size = " + feasibledup.size());
			for (int i = 0; i < feasibledup.size(); i++) {
				saveTDPathDUP += "\n" + feasibledup.get(i).DUP2Str();//
			}

			saveTDPathDUP += "\n\ninfeasibledup size = " + infeasibledup.size();//
			System.out.println("infeasibledup size = " + infeasibledup.size());
			for (int i = 0; i < infeasibledup.size(); i++) {
				saveTDPathDUP += "\n" + infeasibledup.get(i).DUP2Str();//
			}
		}

		String content = "";
		for (CLGPath p : feasible_path) {
			System.out.println(p.toGetPathInfo());
		}
		List<TestData> newDatas = new ArrayList<TestData>();
		List<TestData> tempDatas = new ArrayList<TestData>();
		for (TestData td : testDatas) {

			if (td.getObjPre().contains("[[")) {
				String origanobjpre = td.getObjPre().substring(1, td.getObjPre().length() - 1);
				// System.out.println("origanobjpre:"+origanobjpre);
				String origanobjpost = td.getObjPost().substring(2, td.getObjPost().length() - 2);
				if (origanobjpre.contains("[[")) {
					origanobjpre = origanobjpre.substring(1, origanobjpre.length() - 1);
				}
				String[] token1 = origanobjpre.split("], ");
				String[] token2 = origanobjpost.split("], ");

				int arrayID;
				for (arrayID = 0; arrayID < token1.length - 1; arrayID++) {
					if (token1[arrayID].contains("]]")) {
						token1[arrayID] = token1[arrayID].substring(0, token1[arrayID].length() - 2);
						token2[arrayID] = token2[arrayID].substring(0, token2[arrayID].length() - 2);
					}
					tempDatas
							.add(new TestData(td.getClassName(), td.getMethodName(), td.getPathId(), td.getTestDataId(),
									arrayID, td.isConstructor(), td.getRetType(), "[" + token1[arrayID] + "]]",
									td.getArgPre(), "[" + token2[arrayID] + "]]", td.getArgPost(), td.getRetVal()));

				}
				if (td.getObjPre().contains("[]"))
					tempDatas.add(new TestData(td.getClassName(), td.getMethodName(), td.getPathId(),
							td.getTestDataId(), arrayID, td.isConstructor(), td.getRetType(), "[[]]", td.getArgPre(),
							"[[]]", td.getArgPost(), td.getRetVal()));
				else
					tempDatas
							.add(new TestData(td.getClassName(), td.getMethodName(), td.getPathId(), td.getTestDataId(),
									arrayID, td.isConstructor(), td.getRetType(), "[" + token1[arrayID] + "]",
									td.getArgPre(), "[" + token2[arrayID] + "]", td.getArgPost(), td.getRetVal()));
			} else
				tempDatas.add(td);
		}
		// for (TestData td : testDatas) {
		for (TestData td : tempDatas) {
			String temp_content = "OBJ_PRE = " + td.getObjPre() + ", ARG_PRE = " + td.getArgPre() + ",OBJ_POST = "
					+ td.getObjPost() + ",ARG_POST = " + td.getArgPost() + ", RETVAL = " + td.getRetVal() + "\n";
			if (!content.contains(temp_content))// 加
			{
				content += temp_content;// "OBJ_PRE = " + td.getObjPre() + ", ARG_PRE = " + td.getArgPre() + ",OBJ_POST
										// = " + td.getObjPost() + ",ARG_POST = " + td.getArgPost() + ", RETVAL = " +
										// td.getRetVal() + "\n";
				newDatas.add(td);
			}
		}
		this.testDatas = newDatas;
//		DataWriter.writeInfo(content, graphClassName + graphMethodName, "txt", output_path+"/testDatas", "TestDatas");
		DataWriter.writeInfo(content, graphClassName + graphMethodName, "txt",
				DataWriter.testData_output_path);
		DataWriter.writeInfo(saveTDPathDUP, graphClassName + graphMethodName, "java", output_path, "TDPathDUP");
		return this.testDatas;
	}

	// --------DUP analysis
	@SuppressWarnings("rawtypes")
	public List<TestDataClassLevel> genClassLevelTestSuite() throws IOException {
		String graphClassName = ((CLGStartNode) this.targetCLG.getStartNode()).getFirstUpperClassName();
		String graphMethodName = ((CLGStartNode) this.targetCLG.getStartNode()).getFirstUpperMethodName();
		boolean graphMethodIsConstructor = ((CLGStartNode) this.targetCLG.getStartNode()).isConstructor();
		String graphMethodRetType = " ";
		CLGPath pathObj;
		int pathnum = clpTranslator.getPathNumber() + 1;
		String saveTDPathDUP = "";// --816
		while (!this.meetCriterion() && (pathObj = clgPathEnumerator.next()) != null) {

			// ---dup 816
			ArrayList<DUP> nowDUP = new ArrayList<DUP>();
			if (Main.criterion.equals(Criterion.dcdup) || Main.criterion.equals(Criterion.dccdup)
					|| Main.criterion.equals(Criterion.mccdup)) {
				System.out.println("part 2 ");
				List<CLGNode> new_path1 = clgPathEnumerator.filterConstraintNode(pathObj.getPathNodes());
				ArrayList<DUP> regDUP = new ArrayList<DUP>();
				CLGPath clgp = new CLGPath(new_path1);
				regDUP = clgRDA.parsePathDUP(clgp, oriDUP);
				System.out.println(" regdup " + regDUP.size());
				for (int i = 0; i < regDUP.size(); i++) {
					if (this.coverageDUPCheckingTable.containsKey(regDUP.get(i))) {
						nowDUP.add(regDUP.get(i));
					}
				}
				System.out.println("Path = " + clgp.toGetPathInfo());
				for (DUP p : nowDUP) {
					System.out.println(p.DUP2Str());
				}
			}
			// -------

			Set<CLGEdge> intersectionalEdge = new HashSet<CLGEdge>();
			for (CLGEdge b : pathObj.getEdges()) {
				if (this.coverageCheckingTable.containsKey(b)) {
					intersectionalEdge.add(b);
				}
			}

			if (nowDUP.size() > 0) {
				// 824
				// --
				boolean flag = false;
				ArrayList<String> methodarr = new ArrayList<String>();
				for (int pathi = 0; pathi < pathObj.getPathNodes().size(); pathi++) {
					if (pathObj.getPathNodes().get(pathi) instanceof CLGConstraintNode) {
						CLGConstraintNode clgconsn = (CLGConstraintNode) pathObj.getPathNodes().get(pathi);
						if (clgconsn.getConstraint() instanceof CLGMethodInvocationNode) {
							flag = true;
							CLGMethodInvocationNode methodpath = (CLGMethodInvocationNode) clgconsn.getConstraint();
							methodarr.add(methodpath.getMethodName());
						} else
							continue;
					}
				}
				String clpContent = "";
				if (flag) { // method
					clpContent = clpTranslator.genMethodPathCLP(pathObj, pathnum);
					// DataWriter.writeFile(clpContent, graphClassName, "ECL",
					// pathnum);
					// 715
					DataWriter.writeInfo(clpContent, graphClassName + "_" + pathnum, "ecl",
							DataWriter.testCons_output_path);
					System.out.println(" method 620 ++" + methodarr);
					if (false) {
						/* black-box: clp solves twice. */
						if (clpSolver.solving(graphClassName, pathnum, methodarr)) {
							testClassDatas.add(clpSolver.getTestDataclass());
							pathnum += 1;
						}
						// clpSolver.solving(graphClassName, pathnum,
						// methodarr);
						// if(clpSolver.getTestData()!=null){
						// testDatas.add(clpSolver.getTestData());
						// pathnum+=1;
						// }
					} else {
						/* white-box: clp solves once. */
						// clpSolver.solving((graphClassName), pathnum,
						// methodarr);
						// if(clpSolver.getTestDataclass()!=null){
						// testDatas.add(clpSolver.getTestDataclass());
						// //System.out.println("Obj_pre="+clpSolver.getTestData().getObjPre());
						// //System.out.println("Arg_pre="+clpSolver.getTestData().getArgPre());
						// //System.out.println("Obj_post="+clpSolver.getTestData().getObjPost());
						// //System.out.println("Arg_post="+clpSolver.getTestData().getArgPost());
						// //System.out.println("Reu_val="+clpSolver.getTestData().getRetVal());
						// pathnum+=1;
						// }
						if (clpSolver.solving(graphClassName, pathnum, methodarr)
								&& clpSolver.getTestDataclass() != null) {
							testClassDatas.add(clpSolver.getTestDataclass());
							pathnum += 1;
						}
					}
					System.out.println(pathObj.toGetPathInfo());
				} // ---
				else {
					clpContent = clpTranslator.genPathCLP(pathObj);
					DataWriter.writeInfo(clpContent,
							graphClassName + graphMethodName + "_" + clpTranslator.getPathNumber(), "ecl",
							DataWriter.testCons_output_path);
					/*
					 * if (false) { /* black-box: clp solves twice.
					 * clpSolver.solving(graphClassName, graphMethodName,
					 * clpTranslator.getPathNumber(), 1, "Obj_pre", "Arg_pre", "Obj_post",
					 * "Arg_post", "RetVal"); testDatas.add(clpSolver.getTestData()); } else { /*
					 * white-box: clp solves once. clpSolver.solving(graphClassName,
					 * graphMethodName, clpTranslator.getPathNumber(), 1, "Obj_pre", "Arg_pre",
					 * "Obj_post", "Arg_post", "RetVal"); testDatas.add(clpSolver.getTestData());
					 * System.out.println("clpSolver.getTestData(): " +
					 * clpSolver.getTestData().toString()); }
					 * System.out.println(pathObj.toGetPathInfo());
					 */
					// this.removeCoveredBranches(intersectionalEdge);
				}
				// check testdata
				if (Main.criterion.equals(Criterion.dcdup) || Main.criterion.equals(Criterion.dccdup)
						|| Main.criterion.equals(Criterion.mccdup)) {
					System.out.println("part 3 ");
					if (clpSolver.getTestDataclass() == null) {
						for (int dupi = 0; dupi < nowDUP.size(); dupi++) {
							int count = (int) this.coverageDUPCheckingTable.get(nowDUP.get(dupi));
							this.coverageDUPCheckingTable.put(nowDUP.get(dupi), (count + 1));
						}
						this.infeasiblePath.add(pathObj);
						this.removeCoveredBranches(intersectionalEdge);
					} else {// sol
						for (int dupi = 0; dupi < nowDUP.size(); dupi++) {
							this.feasible_path.add(pathObj);
							this.feasibledup.add(nowDUP.get(dupi));
							this.coverageDUPCheckingTable.remove(nowDUP.get(dupi));
							this.removeCoveredBranches(intersectionalEdge);
						}
						List<CLGNode> new_path1 = clgPathEnumerator.filterConstraintNode(pathObj.getPathNodes());
						CLGPath clgp = new CLGPath(new_path1);
						saveTDPathDUP += "Path = " + clgp.toGetPathInfo();
						for (DUP p : nowDUP) {
							saveTDPathDUP += "\n" + p.DUP2Str();
						}
						saveTDPathDUP += "\n" + clpSolver.getTestData() + "\n\n";
					}
				} else {// control
					if (clpSolver.getTestDataclass() == null) {

						this.infeasiblePath.add(pathObj);
						// this.removeCoveredBranches(intersectionalEdge);
					} else {// sol
						for (int dupi = 0; dupi < intersectionalEdge.size(); dupi++) {
							this.feasible_path.add(pathObj);
							// this.feasibledup.add(nowDUP.get(dupi));
							// this.coverageDUPCheckingTable.remove(nowDUP.get(dupi));
							this.removeCoveredBranches(intersectionalEdge);
						}
					}
					// this.removeCoveredBranches(intersectionalEdge);
					// this.feasible_path.add(pathObj);
				}
				// --

				// String clpContent = clpTranslator.genPathCLP(pathObj);
				// DataWriter.writeInfo(clpContent, graphClassName +
				// graphMethodName + "_" + clpTranslator.getPathNumber(),
				// "ecl",DataWriter.output_folder_path,"ECL");
				//
				// if (false) {
				// /* black-box: clp solves twice. */
				// clpSolver.solving(graphClassName, graphMethodName,
				// clpTranslator.getPathNumber(),
				// 1,graphMethodIsConstructor,graphMethodRetType, "Obj_pre",
				// "Arg_pre", "Obj_post", "Arg_post", "RetVal");
				// testDatas.add(clpSolver.getTestData());
				// } else {
				// /* white-box: clp solves once. */
				// clpSolver.solving(graphClassName, graphMethodName,
				// clpTranslator.getPathNumber(),
				// 1,graphMethodIsConstructor,graphMethodRetType, "Obj_pre",
				// "Arg_pre", "Obj_post", "Arg_post", "RetVal");
				// testDatas.add(clpSolver.getTestData());
				// System.out.println("clpSolver.getTestData(): " +
				// clpSolver.getTestData().toString());
				// }
				// System.out.println(pathObj.toGetPathInfo());
				// this.removeCoveredBranches(intersectionalEdge);
			}
		}

		String content = "";
		System.out.println("\n");
		saveTDPathDUP += "\n\nfeasiblepathsize = " + feasible_path.size() + "\n"; //
		System.out.println("feasiblepathsize = " + feasible_path.size());
		for (CLGPath p : feasible_path) {
			System.out.println(p.toGetPathInfo());
			List<CLGNode> new_path1 = clgPathEnumerator.filterConstraintNode(p.getPathNodes());
			CLGPath clgp = new CLGPath(new_path1);
			saveTDPathDUP += clgp.toGetPathInfo() + "\n";
		}

		if (Main.criterion.equals(Criterion.dcdup) || Main.criterion.equals(Criterion.dccdup)
				|| Main.criterion.equals(Criterion.mccdup)) {
			System.out.println("part 4 ");
			ArrayList<DUP> indup = new ArrayList<DUP>();
			for (Object dup : this.coverageDUPCheckingTable.keySet()) {
				if (dup instanceof DUP) {
					if (!indup.contains((DUP) dup))
						indup.add((DUP) dup);
				}
			}
			for (int i = 0; i < indup.size(); i++) {
				if (!this.infeasibledup.contains(indup.get(i)))
					this.infeasibledup.add(indup.get(i));
				if (this.coverageDUPCheckingTable != null) {
					if (this.coverageDUPCheckingTable.containsKey(indup.get(i)))
						this.coverageDUPCheckingTable.remove(indup.get(i));
				}
			}
			saveTDPathDUP += "\n\nfeasibledup size = " + feasibledup.size();//
			System.out.println("feasibledup size = " + feasibledup.size());
			for (int i = 0; i < feasibledup.size(); i++) {
				saveTDPathDUP += "\n" + feasibledup.get(i).DUP2Str();//
			}

			saveTDPathDUP += "\n\ninfeasibledup size = " + infeasibledup.size();//
			System.out.println("infeasibledup size = " + infeasibledup.size());
			for (int i = 0; i < infeasibledup.size(); i++) {
				saveTDPathDUP += "\n" + infeasibledup.get(i).DUP2Str();//
			}
		}
		saveTDPathDUP += "\n\ninfeasiblepath size = " + infeasiblePath.size() + "\n";//
		System.out.println("infeasiblepath size = " + infeasiblePath.size());
		for (int i = 0; i < infeasiblePath.size(); i++) {
			System.out.println(infeasiblePath.get(i).toGetPathInfo());
			List<CLGNode> new_path1 = clgPathEnumerator.filterConstraintNode(infeasiblePath.get(i).getPathNodes());
			CLGPath clgp = new CLGPath(new_path1);
			saveTDPathDUP += clgp.toGetPathInfo() + "\n";
		}
		for (TestDataClassLevel td : testClassDatas) {
			content += "OBJ_PRE = " + td.getObjPre() + ", ARG_PRE = " + td.getArgPre() + ",OBJ_POST = "
					+ td.getObjPost() + ",ARG_POST = " + td.getArgPost() + ", RETVAL = " + td.getRetVal() + "\n\n";
		}
		DataWriter.writeInfo(content, graphClassName + graphMethodName, "txt", DataWriter.testData_output_path);

//		new ProcessBuilder("dot", "-Tpng", DataWriter.output_folder_path+"/clg/"+SampleHandler.CurrentEditorName+"CLG"+"1"+".dot",
//				"-o", DataWriter.output_folder_path+"/clg/"+SampleHandler.CurrentEditorName+"CLG"+graphMethodName+".png").start();

//		new ProcessBuilder("dot", "-Tpng", DataWriter.output_folder_path+"/clg/"+SampleHandler.CurrentEditorName+"CLG"+"2"+".dot",
//				"-o", DataWriter.output_folder_path+"/clg/"+SampleHandler.CurrentEditorName+"CLG"+graphMethodName+".png").start();

//		DataWriter.writeInfo(saveTDPathDUP, graphClassName + graphMethodName, "java", DataWriter.output_folder_path, "TDPathDUP");
		// DataWriter.writeFile(content, graphClassName, "TestDatas", ".txt");
		// saveTDPathDUP
		return this.testClassDatas;
	}

	public void selectCLGGraphCriteria(CLGGraph graph, Criterion criterionState) {
		/* designed */
		CLGCriterionTransformer transformer = new CLGCriterionTransformer();
		Criterion criterion = null;
		switch (criterionState) {
		case dc:
		case dcdup:
			this.targetCLG = graph;
			break;
		case dcc:
		case dccdup:
			this.targetCLG = transformer.CriterionTransformer(this.targetCLG, criterion.dcc);
			break;
		case mcc:
		case mccdup:
			this.targetCLG = transformer.CriterionTransformer(this.targetCLG, criterion.mcc);
			break;
		}

	}

	public boolean meetCriterion() {

		for (Object edgeTimes : this.coverageCheckingTable.values()) {
			if ((Integer) edgeTimes < 50) {
				return false;
			} else
				continue;
		}
		// ----
		if (Main.criterion.equals(Criterion.dcdup) || Main.criterion.equals(Criterion.dccdup)
				|| Main.criterion.equals(Criterion.mccdup))
			System.out.println("(Integer) DUPTimes");
		for (Object DUPTimes : this.coverageDUPCheckingTable.values()) {

			if ((Integer) DUPTimes < 5) {// ?
				return false;
			} else
				continue;
		}
		// --
		return true;
	}

	public boolean meetCriterion(String criterion) {

		for (Object edgeTimes : this.coverageCheckingTable.values()) {
			if ((Integer) edgeTimes < 6000) {// ??
				return false;
			} else
				continue;
		}
		// ----
		if (criterion.equals("dcdup") || criterion.equals("dccdup") || criterion.equals("mccdup"))
			System.out.println("(Integer) DUPTimes");
		for (Object DUPTimes : this.coverageDUPCheckingTable.values()) {

			if ((Integer) DUPTimes < 5) {// ?
				return false;
			} else
				continue;
		}
		// --
		return true;
	}

	public void removeCoveredBranches(Set<CLGEdge> edges) {
		for (CLGEdge e : edges) {
			this.coverageCheckingTable.remove(e);
			System.out.println("this.unCoveredBranches size " + this.coverageCheckingTable.size());
		}
	}

	public int factorial(int number) {
		if (number == 1)
			return number;
		else
			return number * factorial(number - 1);
	}

	public void quickSort(int array[], int low, int high) {
		int mid = (low + high) / 2;
		int left = low;
		int right = high;
		int pivot = array[mid];
		while (left <= right) {
			while (array[left] < pivot)
				left++;
			while (array[right] > pivot)
				right--;
			if (left <= right) {
				int temp = array[left];
				array[left] = array[right];
				array[right] = temp;
				left++;
				right--;
			}
		}
		// Recursion on left and right of the pivot
		if (low < right)
			quickSort(array, low, right);
		if (left < high)
			quickSort(array, left, high);
	}
	
	public CLGPath SIPBinarySearch(CLGPath fullPath, String graphClassName, String graphMethodName, int pathNo, Boolean graphIsConstructor, String graphRetType) throws CloneNotSupportedException {
		int low = 0, mid;
		int high = fullPath.getPathNodes().size()-1;
		Boolean solved;
		CLGPath halfPath = fullPath.clone();
		CLGNode endNode = fullPath.getLastNode();
		
		while(low <= high) {
			mid = (low + high)/2;
			if(mid == 0) {
				infeasibleTilStartNode = true;
				break;
			}
			
			halfPath = fullPath.clone();
			halfPath.cutPathNode(mid);
			halfPath.addNode(endNode);
			
			//嘗試僅修改clp內容，不進行重複轉換
			String tempCLP = clpTranslator.genPathCLP(halfPath);
			
			DataWriter.writeInfo(tempCLP, graphClassName + graphMethodName + "SIP" + pathNo,
					"ecl", DataWriter.output_folder_path, "eclSIP");
			solved = clpSolver.solvingInfeasiblePath(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
					graphRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal");
			halfPath.RemoveLastNode();
			
			if(solved) {
				low = mid+1;
			}else if(low == mid) {
				if(mid == 0) infeasibleTilStartNode = true;
				return halfPath;
			}else {
				high = mid;
			}
		}
		return halfPath;
	}
	
	//20200520 嘗試僅修改clp內容，不進行重複轉換
	public CLGPath SIPSearch(String clpContent, CLGPath fullPath, String graphClassName, String graphMethodName, int pathNo, Boolean graphIsConstructor, String graphRetType) throws CloneNotSupportedException {
		int low = 0, mid;
		int high = fullPath.getPathNodes().size()-1;
		Boolean solved;
		CLGPath SIP = fullPath;
		
		while(low <= high) {
			mid = (low + high)/2;
			if(mid == 0) {
				infeasibleTilStartNode = true;
				break;
			}
			
//			不做轉換改做修改
			String tempCLP = clpTranslator.getTempSIPCLP(fullPath, clpContent, mid);
			
			DataWriter.writeInfo(tempCLP, graphClassName + graphMethodName + "SIP" + pathNo,
					"ecl", "Examples/", "eclSIP");
			solved = clpSolver.solvingInfeasiblePath(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
					graphRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal");
			
			if(solved) {
				low = mid+1;
			}else if(low == mid) {
				if(mid == 0) infeasibleTilStartNode = true;
				SIP.cutPathNode(mid);
				return SIP;
			}else {
				high = mid;
			}
		}
		return SIP;
	}
	
	public List<String> findConflictConstraint(CLGPath pathObj, CLGNode endNode, String graphClassName, String graphMethodName, int pathNo, Boolean graphIsConstructor, String graphRetType) throws CloneNotSupportedException {
		List<String> tempList = new ArrayList<>();
		String temp = "";
		Boolean solved;
		CLGPath tempPath = pathObj.clone();
		
		for(int i=2; i<pathObj.getPathNodes().size(); i++) {
			temp = "";
			tempPath = pathObj.clone();
			tempPath.RemoveNodeByNum(i);
			tempPath.addNode(endNode);
			
			String tempCLP = clpTranslator.genPathCLP(tempPath);
			DataWriter.writeInfo(tempCLP, graphClassName + graphMethodName + "SIP" + pathNo,
					"ecl", DataWriter.output_folder_path, "eclSIP");
			solved = clpSolver.solvingInfeasiblePath(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
					graphRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal");
			
			if(solved) {
				CLGConstraintNode cons = (CLGConstraintNode) pathObj.getPathNodes().get(pathObj.getPathNodes().size()-i);
				temp += cons.toCLPInfo().replace("#", "") + ",";
				tempList.add(temp);
			}
		}
		
		return tempList;
	}
	
	//20200520 嘗試僅修改clp內容，不進行重複轉換
	public List<String> CCSSearch(String clpContent, CLGPath SIP, CLGNode endNode, String graphClassName, String graphMethodName, int pathNo, Boolean graphIsConstructor, String graphRetType) throws CloneNotSupportedException {
		List<String> CCS = new ArrayList<>();
		String temp = "";
		Boolean solved;
		
//		for(int i=2; i<SIP.getPathNodes().size(); i++) {
//			temp = "";
//			
//			String tempCLP = clpTranslator.getTempCCSCLP(SIP, SIP.getPathNodes().size()-i);
//			
////			DataWriter.writeInfo(tempCLP, graphClassName + graphMethodName + "SIP" + pathNo,
////					"ecl", DataWriter.output_folder_path, "eclSIP");
//			DataWriter.writeInfo(tempCLP, graphClassName + graphMethodName + "SIP" + pathNo,
//					"ecl", "Examples/", "eclSIP");
//			solved = clpSolver.solvingInfeasiblePath(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
//					graphRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal");
//			
//			if(solved) {
//				CLGConstraintNode cons = (CLGConstraintNode) SIP.getPathNodes().get(SIP.getPathNodes().size()-i);
//				temp += cons.toCLPInfo() + ",";
////				temp += clgPathEnumerator.getCLGht().get(cons) +",";
//				CCS.add(temp);
//			}
//		}
		
//		20200808 CCS方法修改
		for(int i=2; i<SIP.getPathNodes().size(); i++) {
			temp = "";
			
			String tempCLP = clpTranslator.getTempCCSCLP(SIP, SIP.getPathNodes().size()-i);
			

			DataWriter.writeInfo(tempCLP, graphClassName + graphMethodName + "SIP" + pathNo,
					"ecl", "Examples/", "eclSIP");
			solved = clpSolver.solvingInfeasiblePath(graphClassName, graphMethodName, pathNo, 1, graphIsConstructor,
					graphRetType, "Obj_pre", "Arg_pre", "Obj_post", "Arg_post", "RetVal");
			
			if(solved) {
				CLGConstraintNode cons = (CLGConstraintNode) SIP.getPathNodes().get(SIP.getPathNodes().size()-i);
				temp += cons.toCLPInfo() + ",";
				CCS.add(temp);
			}else {
				clpTranslator.setTempCLPByCCS();
			}
		}
		
		return CCS;
	}
	
	public void preRemovalInfeasiblePathManager(CLGPath pathObj, String graphClassName, String graphMethodName, int pathNo, Boolean graphIsConstructor, String graphRetType) throws CloneNotSupportedException {
		infeasibleTilStartNode = false;
		CLGNode endNode = pathObj.getLastNode();
		String clpContent = clpTranslator.genPathCLP(pathObj);
		//最短不可實行路徑搜尋
		pathObj = SIPSearch(clpContent, pathObj, graphClassName, graphMethodName, pathNo, graphIsConstructor, graphRetType);
		String SIP = "";
		List<CLGNode> SIPNodes = null;
		
		if(infeasibleTilStartNode) {
//			不將此SIP放入SIPNodesList中
			SIP = pathObj.toGetPathInfo() + "\n";
			pathObjInfo += SIP + "This path will not enter SIP check table!\n";
		}else {
			SIPNodes = pathObj.getPathNodes();
			
//			test
			String temp2 = "";
			List<String> tempCon = new ArrayList<>();
			List<String> tempList = new ArrayList<>();
			
//			找到衝突限制組
			tempList = CCSSearch(clpContent, pathObj, endNode, graphClassName, graphMethodName, pathNo, graphIsConstructor, graphRetType);
			
			if(tempList.size() != 0) {
				for(String conflict : tempList) {
					tempCon.add(conflict);
				}
				
//				SIP最後一個節點，必須加入
				CLGConstraintNode cons2 = (CLGConstraintNode) pathObj.getPathNodes().get(pathObj.getPathNodes().size()-1);
				temp2 += cons2.toCLPInfo() + ",";
				tempCon.add(temp2);
				
				conflictConstraints.add(tempCon);
				
				//在pathinfo文件中紀錄CCS訊息
				pathObjInfo += "CCS: ";
				for(String s : tempCon) {
					pathObjInfo += s + " ";
				}
				pathObjInfo += "\n";
			}
			//在pathinfo文件中紀錄SIP訊息
			SIP = "SIP: " + pathObj.toGetPathInfo() + "\n";
			pathObjInfo += SIP ;
		}
		
		//預先去除
		Queue<LinkedList<CLGNode>> path_queue = new LinkedList<>();
		Queue<LinkedList<CLGNode>> removeQueue = new LinkedList<>();
		path_queue = clgPathEnumerator.getQueue();
		for(LinkedList<CLGNode> unfinishPath : path_queue) {
			
//			SIP
//			if(SIPNodes != null && unfinishPath.containsAll(SIPNodes)) {
//				removeQueue.add(unfinishPath);
//			}else {
////				CCS
//				for(List<String> subset : conflictConstraints) {
//					int size = subset.size();
//					int count = 0;
//					for(String s : subset) {
//						for(CLGNode node : unfinishPath) {
//							if(s.equals(node.toCLPInfo() + ",")) {
//								count++;
//							}
//						}
//					}
//					if(count == size) {
//						removeQueue.add(unfinishPath);
//					}
//				}
//			}
			
			//20200808 修改為僅判斷CCS
			for(List<String> subset : conflictConstraints) {
				int size = subset.size();
				int count = 0;
				for(String s : subset) {
					for(CLGNode node : unfinishPath) {
						if(s.equals(node.toCLPInfo() + ",")) {
							count++;
						}
					}
				}
				if(count == size) {
					removeQueue.add(unfinishPath);
				}
			}
		}
		clgPathEnumerator.removePathQueue(removeQueue);
	}
	
	public Boolean matchConflictConstraints(CLGPath pathObj) {
		Map<String, Integer> map = new HashMap<>();
		map = pathObj.getPathByMap(clgPathEnumerator.getCLGht());
		
		for(List<String> subset : conflictConstraints) {
			int size = subset.size();
			int count = 0;
			Integer check = 0;
			for(String s : subset) {
				check = map.get(s);
				if(check != null) {
					count++;
				}
			}
			if(count == size) {
				return true;
			}
		}
		
		return false;
	}
	
	//確認該路徑否提高覆蓋度
	public Boolean isIncreaseCoverage(Set<CLGEdge> edges) {
		Boolean flag = false;
		
		for (CLGEdge e : edges) {
			if(this.coverageCheckingTable.containsKey(e)) {
				flag = true;
				break;
			}
		}
		
		return flag;
	}
}
