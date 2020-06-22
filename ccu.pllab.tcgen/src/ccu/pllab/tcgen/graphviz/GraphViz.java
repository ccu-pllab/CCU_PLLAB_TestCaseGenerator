package ccu.pllab.tcgen.graphviz;

 
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class GraphViz {
	static public String generateGraph(List<GraphVizable> graphs) {
		StringWriter s = new StringWriter();
		PrintWriter writer = new PrintWriter(s);

		writer.println("digraph \"\" {");
		for (GraphVizable graph : graphs) {
			if (graph == null) {
				continue;
			}
			write_subgraph(graph, writer);
		}
		writer.println("}");

		return s.toString();
	}

	static public String generateGraph(GraphVizable graph) {
		StringWriter s = new StringWriter();
		PrintWriter writer = new PrintWriter(s);

		writer.println("digraph \"\" {");
		write_subgraph(graph, writer);
		writer.println("}");

		return s.toString();
	}

	private static void write_subgraph(GraphVizable graph, PrintWriter writer) {
		writer.println("\tsubgraph {");
		writer.print(graph.toGraphViz());
		writer.println("\t}");
	}
}
