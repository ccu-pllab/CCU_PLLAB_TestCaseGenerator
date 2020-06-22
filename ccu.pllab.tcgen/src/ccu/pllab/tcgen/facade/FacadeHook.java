package ccu.pllab.tcgen.facade;

 
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.ast.ASTNode;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg2path.Path;
import ccu.pllab.tcgen.graphviz.GraphViz;
import ccu.pllab.tcgen.graphviz.GraphVizable;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.Operation;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Class;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Operation;
import ccu.pllab.tcgen.AbstractCLG.CLGStartNode;

public class FacadeHook {

	public FacadeHook() {
	}

	public void when_gen_ast_graph(Model context, FacadeConfig config) throws IOException {
		List<GraphVizable> nodes = new ArrayList<GraphVizable>();
		for (UML2Class clazz : context.getClasses()) {
			nodes.addAll(clazz.getInvariants());
			for (Operation op : clazz.getOwnedOperations()) {
				UML2Operation uml2Operation = (UML2Operation) op;
				List<GraphVizable> method_nodes = new ArrayList<GraphVizable>();
				method_nodes.addAll(uml2Operation.getPreConstraints());
				nodes.addAll(uml2Operation.getPreConstraints());
				method_nodes.addAll(uml2Operation.getPostConstraints());
				nodes.addAll(uml2Operation.getPostConstraints());
				if (method_nodes.size() > 0) {
					File ast_dot_file = new File(config.getOutputFolder().getFile(),
							String.format("%s%s" + File.separatorChar + "ast.dot", uml2Operation.getOwner().getName(), uml2Operation.getName()));
					File ast_dot_img_file = new File(config.getOutputFolder().getFile(), String.format("%s%s" + File.separatorChar + "ast.png", uml2Operation.getOwner().getName(),
							uml2Operation.getName()));
					if (!ast_dot_img_file.getParentFile().exists()) {
						ast_dot_img_file.getParentFile().mkdirs();
					}
					writeFile(GraphViz.generateGraph(method_nodes), ast_dot_file);
					new ProcessBuilder("dot", "-Tpng", ast_dot_file.getAbsolutePath(), "-o", ast_dot_img_file.getAbsolutePath()).start();

				}
			}
		}
		File ast_dot_file = new File(config.getOutputFolder().getFile(), "ast.dot");
		File ast_dot_img_file = new File(config.getOutputFolder().getFile(), "ast.png");
		writeFile(GraphViz.generateGraph(nodes), ast_dot_file);
		new ProcessBuilder("dot", "-Tpng", ast_dot_file.getAbsolutePath(), "-o", ast_dot_img_file.getAbsolutePath()).start();
	}

	public void when_gen_clg_graph(Model context, FacadeConfig config, List<JSONObject> config_list) throws IOException, JSONException {
		String allDotContent = "";
		for (UML2Class clazz : context.getClasses()) {
			for (Operation op : clazz.getOwnedOperations()) {
				UML2Operation uml2Operation = (UML2Operation) op;
				JSONObject method_config = new JSONObject();
				for (JSONObject it_config : config_list) {
					if (it_config.getString("target_class").equals(String.format("%s.%s", context.getPackageName(), clazz.getName())) && it_config.getString("target_method").equals(op.getName())) {
						method_config = it_config;
						break;
					}
				}
				
				CLGGraph clg_graph = uml2Operation.getCLG(FacadeConfig.parsePathCoverage(method_config.optString("path_coverage", "node")));
				if (clg_graph != null) {
					/*start  ocl2clp*/
					String methodCLP = ((CLGStartNode) clg_graph.getStartNode()).OCL2CLP();
					System.out.println(methodCLP);
					/*end ocl2clp*/
					File clg_dot_file = new File(config.getOutputFolder().getFile(),
							String.format("%s%s" + File.separatorChar + "clg.dot", uml2Operation.getOwner().getName(), uml2Operation.getName()));
					File clg_dot_img_file = new File(config.getOutputFolder().getFile(), String.format("%s%s" + File.separatorChar + "clg.png", uml2Operation.getOwner().getName(),
							uml2Operation.getName()));
					String dotContent = clg_graph.graphDraw();
					allDotContent += dotContent;
					writeFile(dotContent, clg_dot_file);
					new ProcessBuilder("dot", "-Tpng", clg_dot_file.getAbsolutePath(), "-o", clg_dot_img_file.getAbsolutePath()).start();
				}
			}
		}
		File clg_dot_file = new File(config.getOutputFolder().getFile(), "clg.dot");
		File clg_dot_img_file = new File(config.getOutputFolder().getFile(), "clg.png");
		try (FileWriter fw = new FileWriter(clg_dot_file)) {
			fw.write(allDotContent.replaceAll("\\}[\\r\\n\\s]{1,2}digraph\\s\"\"\\s\\{", ""));
		}

		new ProcessBuilder("dot", "-Tpng", clg_dot_file.getAbsolutePath(), "-o", clg_dot_img_file.getAbsolutePath()).start();

	}

	public void when_gen_invalid_ast_graph(Model context, FacadeConfig config) throws IOException {
		List<GraphVizable> nodes = new ArrayList<GraphVizable>();
		for (UML2Class clazz : context.getClasses()) {
			nodes.addAll(clazz.getInvariants());
			for (Operation op : clazz.getOwnedOperations()) {
				UML2Operation uml2Operation = (UML2Operation) op;
				for (ccu.pllab.tcgen.ast.Constraint pre : uml2Operation.getPreConstraints()) {
					ccu.pllab.tcgen.ast.Constraint new_pre = (ccu.pllab.tcgen.ast.Constraint) pre.clone();				
					ASTNode de_not = pre.getSpecification().clone().toDeMorgan();
					new_pre.setSpecification(de_not);
					nodes.add(new_pre);
					File ast_dot_file = new File(config.getOutputFolder().getFile(),
							String.format("%s%s" + File.separatorChar + "invalid_ast.dot", uml2Operation.getOwner().getName(), uml2Operation.getName()));
					File ast_dot_img_file = new File(config.getOutputFolder().getFile(), String.format("%s%s" + File.separatorChar + "invalid_ast.png", uml2Operation.getOwner().getName(),
							uml2Operation.getName()));
					if (!ast_dot_img_file.getParentFile().exists()) {
						ast_dot_img_file.getParentFile().mkdirs();
					}
					writeFile(GraphViz.generateGraph(new_pre), ast_dot_file);
					new ProcessBuilder("dot", "-Tpng", ast_dot_file.getAbsolutePath(), "-o", ast_dot_img_file.getAbsolutePath()).start();

				}
			}
		}
		File ast_dot_file = new File(config.getOutputFolder().getFile(), "invalid_ast.dot");
		File ast_dot_img_file = new File(config.getOutputFolder().getFile(), "invalid_ast.png");
		writeFile(GraphViz.generateGraph(nodes), ast_dot_file);
		new ProcessBuilder("dot", "-Tpng", ast_dot_file.getAbsolutePath(), "-o", ast_dot_img_file.getAbsolutePath()).start();
	}
	
	public void when_gen_invalid_clg_graph(Model context, FacadeConfig config, List<JSONObject> config_list) throws IOException, JSONException {
		String allDotContent = "";
		for (UML2Class clazz : context.getClasses()) {
			for (Operation op : clazz.getOwnedOperations()) {
				UML2Operation uml2Operation = (UML2Operation) op;
				JSONObject method_config = new JSONObject();
				for (JSONObject it_config : config_list) {
					if (it_config.getString("target_class").equals(String.format("%s.%s", context.getPackageName(), clazz.getName())) && it_config.getString("target_method").equals(op.getName())
							&& it_config.optBoolean("invalid_case", false)) {
						method_config = it_config;
						break;
					}
				}
				CLGGraph clg_graph_list = uml2Operation.getInvalidCLG(FacadeConfig.parsePathCoverage(method_config.optString("path_coverage", "dcc")));
				if (clg_graph_list != null ) {
					File clg_dot_file = new File(config.getOutputFolder().getFile(), String.format("%s%s" + File.separatorChar + "invalid_clg.dot", uml2Operation.getOwner().getName(),
							uml2Operation.getName()));
					File clg_dot_img_file = new File(config.getOutputFolder().getFile(), String.format("%s%s" + File.separatorChar + "invalid_clg.png", uml2Operation.getOwner().getName(),
							uml2Operation.getName()));
					String dotContent = "";
					dotContent = clg_graph_list.graphDraw();
					/*for (CLGNode clgs : clg_graph_list) {
						dotContent += GraphViz.generateGraph(clgs);
					}*/
					allDotContent += dotContent;
					writeFile(dotContent, clg_dot_file);
					new ProcessBuilder("dot", "-Tpng", clg_dot_file.getAbsolutePath(), "-o", clg_dot_img_file.getAbsolutePath()).start();
				}
			}
		}
		File clg_dot_file = new File(config.getOutputFolder().getFile(), "invalid_clg.dot");
		File clg_dot_img_file = new File(config.getOutputFolder().getFile(), "invalid_clg.png");
		try (FileWriter fw = new FileWriter(clg_dot_file)) {
			fw.write(allDotContent.replaceAll("\\}[\\r\\n\\s]{1,2}digraph\\s\"\"\\s\\{", ""));
		}

		new ProcessBuilder("dot", "-Tpng", clg_dot_file.getAbsolutePath(), "-o", clg_dot_img_file.getAbsolutePath()).start();

	}

	public void when_gen_path_graph(Model context, FacadeConfig config, List<Path> csp_path_list) throws IOException {
		for (Path p : csp_path_list) {
			File path_dot_file = File.createTempFile("path", "dot");
			path_dot_file.deleteOnExit();
			File path_dot_img_file = new File(config.getOutputFolder().getFile(), p.getPredicateName().replaceFirst("tcgen_\\d+_", "") + File.separatorChar + p.getPredicateName() + ".png");
			writeFile(GraphViz.generateGraph(p), path_dot_file);
			new ProcessBuilder("dot", "-Tpng", path_dot_file.getAbsolutePath(), "-o", path_dot_img_file.getAbsolutePath()).start();
		}
		List<GraphVizable> paths = new ArrayList<GraphVizable>(csp_path_list);
		File path_dot_file = new File(config.getOutputFolder().getFile(), "path.dot");
		File path_dot_img_file = new File(config.getOutputFolder().getFile(), "path.png");
		writeFile(GraphViz.generateGraph(paths), path_dot_file);
		new ProcessBuilder("dot", "-Tpng", path_dot_file.getAbsolutePath(), "-o", path_dot_img_file.getAbsolutePath()).redirectErrorStream(true).start();

	}

	public void writeFile(String content, File output) throws IOException {
		if (!output.getParentFile().exists()) {
			output.getParentFile().mkdirs();
		}
		if (!output.exists()) {
			output.createNewFile();
		}
		FileWriter fw = new FileWriter(output);
		fw.write(content);
		fw.flush();
		fw.close();
	}
}
