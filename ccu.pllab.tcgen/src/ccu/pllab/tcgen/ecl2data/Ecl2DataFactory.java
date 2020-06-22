package ccu.pllab.tcgen.ecl2data;

 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.parctechnologies.eclipse.EclipseEngine;
import com.parctechnologies.eclipse.EclipseEngineOptions;
import com.parctechnologies.eclipse.EclipseException;
import com.parctechnologies.eclipse.EmbeddedEclipse;

public class Ecl2DataFactory {
	private static EclipseEngine instance = null;

	private static void eclipseRPCInputStream(EclipseEngine eclipse2, InputStream resourceAsStream) throws IOException, EclipseException {
		BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
		File file = File.createTempFile("tmp", ".ecl");
		file.deleteOnExit();
		FileWriter sb = new FileWriter(file);
		PrintWriter writer = new PrintWriter(sb);
		String line;
		while ((line = br.readLine()) != null) {
			writer.println(line);
		}
		writer.close();
		eclipse2.compile(file);
	}

	public static Ecl2Data getEcl2DataInstance() throws EclipseException, IOException {
		if (instance == null) {
			EclipseEngineOptions eclipseEngineOptions = new EclipseEngineOptions();
			eclipseEngineOptions.setUseQueues(false);
			instance = EmbeddedEclipse.getInstance(eclipseEngineOptions);
			eclipseRPCInputStream(instance, Ecl2Data.class.getResourceAsStream("/imports/ccu_pllab_lib.ecl"));
			eclipseRPCInputStream(instance, Ecl2Data.class.getResourceAsStream("/imports/ocl_basicops.ecl"));
			eclipseRPCInputStream(instance, Ecl2Data.class.getResourceAsStream("/imports/ocl_collections.ecl"));
			eclipseRPCInputStream(instance, Ecl2Data.class.getResourceAsStream("/imports/ocl_iterators.ecl"));
			eclipseRPCInputStream(instance, Ecl2Data.class.getResourceAsStream("/imports/properties.ecl"));
			eclipseRPCInputStream(instance, Ecl2Data.class.getResourceAsStream("/imports/uml_basic.ecl"));
		}
		return new Ecl2Data(instance);

	}
}
