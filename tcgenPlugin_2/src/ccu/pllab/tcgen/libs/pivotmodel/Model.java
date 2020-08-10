package ccu.pllab.tcgen.libs.pivotmodel;

 
import java.util.List;

public class Model {
	private List<Association> associations;
	private ClassDiagInfo class_diag_info;
	private List<UML2Class> classes;

	public Model(ClassDiagInfo diag_info) {
		this.classes = diag_info.getClsInfoList();
		this.associations = diag_info.getAscInfoList();
		this.class_diag_info = diag_info;
	}

	public Association findAssociation(String class_name, String property_name) {
		for (Association association : class_diag_info.getAscInfoList()) {
			AssociationEnd memberEnd1 = association.getRoleList().get(0);
			AssociationEnd memberEnd2 = association.getRoleList().get(1);
			if ((memberEnd1.getType().equals(class_name) && memberEnd2.getName().equals(property_name)) || (memberEnd2.getType().equals(class_name) && memberEnd1.getName().equals(property_name))) {
				return association;
			}
		}
		return null;
	}

	public UML2Class findClassInfoByName(String name) {
		for (UML2Class clazz : this.classes) {
			if (clazz.getName().equals(name)) {
				return clazz;
			}
		}
		return null;
	}

	private UML2Operation findMethodInfoByClassNameAndMethodName(String className, String methodName) {
		return findClassInfoByName(className).findMethod(methodName);

	}

	public AssociationEnd getAnotherMemberEnd(Association association, String class_name, String property_name) {
		AssociationEnd memberEnd1 = association.getRoleList().get(0);
		AssociationEnd memberEnd2 = association.getRoleList().get(1);
		if (memberEnd1.getType().equals(class_name) && memberEnd2.getName().equals(property_name)) {
			return memberEnd1;
		} else {
			return memberEnd2;
		}
	}

	public List<Association> getAssociations() {
		return associations;
	}

	public List<UML2Class> getClasses() {
		return classes;
	}

	public void attachConstraints(List<ccu.pllab.tcgen.ast.Constraint> ast_tree_list) {
		for (ccu.pllab.tcgen.ast.Constraint tree : ast_tree_list) {
			if (tree.getConstraintKind().equals("precondition")) {
				this.findMethodInfoByClassNameAndMethodName(tree.getConstraintedClassName(), tree.getConstraintedMethodName()).addPrecondition(tree);
			} else if (tree.getConstraintKind().equals("postcondition")) {
				this.findMethodInfoByClassNameAndMethodName(tree.getConstraintedClassName(), tree.getConstraintedMethodName()).addPostcondition(tree);
			} else if (tree.getConstraintKind().equals("invariant")) {
				this.findClassInfoByName(tree.getConstraintedClassName()).addInvariant(tree);
			}
		}
	}

	public Object getPackageName() {
		return class_diag_info.getPackageName();
	}

	public Association findAscInfo(String ascName) {
		return class_diag_info.findAscInfo(ascName);
	}

}
