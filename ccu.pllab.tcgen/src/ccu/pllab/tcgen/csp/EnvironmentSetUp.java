package ccu.pllab.tcgen.csp;
 

import java.io.PrintWriter;
import java.util.List;

import ccu.pllab.tcgen.libs.pivotmodel.Association;
import ccu.pllab.tcgen.libs.pivotmodel.AssociationEnd;
import ccu.pllab.tcgen.libs.pivotmodel.Attribute;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Class;

public class EnvironmentSetUp {

	private Model model;

	public EnvironmentSetUp(Model model) {
		this.model = model;
	}

	public void generateCLP(PrintWriter writer) {
		write_class_index(writer);
		writer.println();
		writer_instanceType(writer);
		writer.println();
		write_attr_index(writer);
		writer.println();
		write_association_is_unique(writer);
		writer.println();
		write_role_index(writer);
		writer.println();
		write_role_type(writer);
		writer.println();
		write_role_min(writer);
		writer.println();
		write_role_max(writer);
		writer.println();
	}

	private void write_association_is_unique(PrintWriter writer) {
		// assocIsUnique("instruct", 1).
		for (Association info : this.model.getAssociations()) {
			writer.println(String.format("assocIsUnique(\"%s\", 1).", info.getName()));
		}
	}

	private void write_attr_index(PrintWriter writer) {
		// attIndex("Laboratory", "limit",4)
		for (UML2Class class_info : this.model.getClasses()) {
			writer.println(String.format("attIndex(\"%s\", \"%s\",%d).", class_info.getName(), "type", 1));
			writer.println(String.format("attIndex(\"%s\", \"%s\",%d).", class_info.getName(), "name", 2));
			writer.println(String.format("attIndex(\"%s\", \"%s\",%d).", class_info.getName(), "oid", 3));
			int count = 4;
			for (Attribute attr : class_info.getAttrList()) { //getAttrAndAscList
				writer.println(String.format("attIndex(\"%s\", \"%s\",%d).", class_info.getName(), attr.getName(), count));
				count++;
			}
		}

		for (Association association_info : this.model.getAssociations()) {
			writer.println(String.format("attIndex(\"%s\", \"%s\",%d).", association_info.getName(), "type", 1));
			writer.println(String.format("attIndex(\"%s\", \"%s\",%d).", association_info.getName(), "name", 2));
			int count = 3;
			for (AssociationEnd attr : association_info.getRoleList()) {
				writer.println(String.format("attIndex(\"%s\", \"%s\",%d).", association_info.getName(), attr.getName(), count));
				count++;
			}
		}
	}

	private void write_class_index(PrintWriter writer) {
		// index("Teacher",1).
		int count = 1;
		for (UML2Class info : this.model.getClasses()) {
			writer.println(String.format("index(\"%s\",%d).", info.getName(), count));
			count++;
		}
		for (Association info : this.model.getAssociations()) {
			writer.println(String.format("index(\"%s\",%d).", info.getName(), count));
			count++;
		}
	}

	private void write_role_index(PrintWriter writer) {
		// roleIndex("instruct", "instructor", 3)
		for (Association info : this.model.getAssociations()) {
			int count = 3;
			for (AssociationEnd attr : info.getRoleList()) {
				writer.println(String.format("roleIndex(\"%s\", \"%s\", %d).", info.getName(), attr.getName(), count));
				count++;
			}
		}
	}

	private void write_role_max(PrintWriter writer) {
		// roleMax("instruct", "instructor", 0).
		for (Association info : this.model.getAssociations()) {
			for (AssociationEnd attr : info.getRoleList()) {
				if (attr.getUpper() < 0) {
					writer.println(String.format("roleMax(\"%s\", \"%s\", \"*\").", info.getName(), attr.getName()));
				} else {
					writer.println(String.format("roleMax(\"%s\", \"%s\", %d).", info.getName(), attr.getName(), attr.getUpper()));
				}
			}
		}
	}

	private void write_role_min(PrintWriter writer) {
		// roleMin("instruct", "instructor", 0).
		for (Association info : this.model.getAssociations()) {
			for (AssociationEnd attr : info.getRoleList()) {
				if (attr.getLower() < 0) {
					writer.println(String.format("roleMin(\"%s\", \"%s\", \"*\").", info.getName(), attr.getName()));
				} else {
					writer.println(String.format("roleMin(\"%s\", \"%s\", %d).", info.getName(), attr.getName(), attr.getLower()));
				}
			}
		}
	}

	private void write_role_type(PrintWriter writer) {
		// roleType("instruct", "instructor", "Teacher").
		for (Association info : this.model.getAssociations()) {
			for (AssociationEnd attr : info.getRoleList()) {
				writer.println(String.format("roleType(\"%s\", \"%s\", \"%s\").", info.getName(), attr.getName(), attr.getType()));
			}
		}
		for (UML2Class info : this.model.getClasses()) {
			writer.println(String.format("roleType(\"null\", \"null\", \"%s\").", info.getName()));
		}
	}

	private void writer_instanceType(PrintWriter writer) {
		int count = 1;
		for (@SuppressWarnings("unused")
		UML2Class info : this.model.getClasses()) {
			writer.println(String.format("instanceType(%d, uml_obj).", count));
			count++;
		}

		for (@SuppressWarnings("unused")
		Association info : this.model.getAssociations()) {
			writer.println(String.format("instanceType(%d, uml_asc).", count));
			count++;
		}
	}
}
