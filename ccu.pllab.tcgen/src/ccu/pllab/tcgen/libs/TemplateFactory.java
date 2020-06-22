package ccu.pllab.tcgen.libs;

 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

public class TemplateFactory {
	private static STGroup gINSTANCE;

	private TemplateFactory() {

	}

	public static ST getTemplate(String name) {
		if (gINSTANCE == null) {
			try {
				InputStreamReader isr = new InputStreamReader(TemplateFactory.class.getResourceAsStream("/ast2ecl.stg"));
				BufferedReader br = new BufferedReader(isr);
				StringWriter sr = new StringWriter();
				PrintWriter pr = new PrintWriter(sr);
				String line;
				while ((line = br.readLine()) != null) {
					pr.println(line);
				}
				gINSTANCE = new STGroupString(sr.toString());
			} catch (IOException e) {
				gINSTANCE = new STGroup();
			}
		}
		ST template = gINSTANCE.getInstanceOf(name);
		if (template == null) {
			System.err.println(name);
		}
		return template;
	}
}
