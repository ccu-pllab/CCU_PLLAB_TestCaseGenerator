package ccu.pllab.tcgen.tc;
  

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.apache.commons.lang3.tuple.Triple;

import ccu.pllab.tcgen.libs.TestData;
import ccu.pllab.tcgen.libs.pivotmodel.AscInstance;
import ccu.pllab.tcgen.libs.pivotmodel.Attribute;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.ObjectInstance;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Class;

public class BuilderGen {

	private static class AttrWarpper {
		String getter;
		String name;
		String setter;
		String type;

		public AttrWarpper(Attribute attr) {
			type = attr.getType();
			name = attr.getName();
			setter = "set" + capitalize(name);
			getter = "get" + capitalize(name);
		}

		private String capitalize(String name) {
			return name.substring(0, 1).toUpperCase() + name.substring(1);
		}

		public String getGetter() {
			return getter;
		}

		public String getName() {
			return name;
		}

		public String getSetter() {
			return setter;
		}

		public String getType() {
			return type;
		}

		public void setGetter(String getter) {
			this.getter = getter;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setSetter(String setter) {
			this.setter = setter;
		}

		public void setType(String type) {
			this.type = type;
		}
	}

	private static class RoleSet {
		String obj1;
		String obj2;
		String roleGetter;

		public RoleSet(String s1, String s2, String s3) {
			obj1 = s1;
			roleGetter = s2;
			obj2 = s3;
		}

		public String getObj1() {
			return obj1;
		}

		public String getObj2() {
			return obj2;
		}

		public String getRoleGetter() {
			return roleGetter;
		}

		public void setObj1(String obj1) {
			this.obj1 = obj1;
		}

		public void setObj2(String obj2) {
			this.obj2 = obj2;
		}

		public void setRoleGetter(String roleGetter) {
			this.roleGetter = roleGetter;
		}
	}

	private static String capitalize(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public static String genArgList(Model clsInfo, TestData testData) {
		String ret = "";

		// name, type, value
		for (Triple<String, String, String> arg : testData.getArgList()) {
			UML2Class info = clsInfo.findClassInfoByName(arg.getMiddle());
			// check if the type is an listed class in the class diagram
			if (info != null) {
				// convert into assocated object
				ret += genObjName(arg.getMiddle(), Integer.valueOf(arg.getRight())) + ",";
			} else
				ret += arg.getRight() + ",";
		}

		// remove tailing "," if needed
		if (!ret.equals("")) {
			ret = ret.substring(0, ret.length() - 1);
		}

		return ret;
	}

	public static String genAscSetter(TestData testData) {
		InputStreamReader fr = null;
		String ret = "";

		fr = new InputStreamReader(BuilderGen.class.getResourceAsStream("SetAssociation.stg"));

		StringTemplateGroup group = new StringTemplateGroup(fr, DefaultTemplateLexer.class);
		StringTemplate template = group.getInstanceOf("setAsc");

		template.setAttribute("roleSetList", genRoleSet(testData.getPreAscList()));

		// ret = template.toString();

		template.setAttribute("roleSetList", genRoleSetPost(testData.getPostAscList()));

		ret += template.toString();

		return ret;
	}

	private static ArrayList<AttrWarpper> genAttrWrapperList(ArrayList<Attribute> attrList) {
		ArrayList<AttrWarpper> list = new ArrayList<AttrWarpper>();

		for (Attribute attr : attrList) {
			AttrWarpper wrapper = new AttrWarpper(attr);
			list.add(wrapper);
		}

		return list;
	}

	public static String genBuilder(Model clsInfo, String packageName, String testCaseFolder) {
		InputStreamReader fr = null;
		String ret = "";

		fr = new InputStreamReader(BuilderGen.class.getResourceAsStream("builder.stg"));

		StringTemplateGroup group = new StringTemplateGroup(fr, DefaultTemplateLexer.class);
		StringTemplate template = group.getInstanceOf("builder");

		for (UML2Class info : clsInfo.getClasses()) {
			String sPath = testCaseFolder + "/" + "Builder" + info.getName() + ".java";
			template.removeAttribute("classPackage");
			template.setAttribute("classPackage", clsInfo.getPackageName());
			template.removeAttribute("class");
			template.setAttribute("class", info.getName());
			template.removeAttribute("properties");
			template.setAttribute("properties", genAttrWrapperList(info.getAttrList()));
			template.removeAttribute("testCasePackage");
			template.setAttribute("testCasePackage", packageName);

			outputFile(sPath, template.toString());

			ret += template.toString();
		}

		return ret;
	}

	public static String genExpectedResult(Model clsInfo, String retType, String expectedResult) {
		String ret = "";

		// if expectedResult is a listed type in the class diagram, convert into
		// asocated object instances
		UML2Class info = clsInfo.findClassInfoByName(retType);

		if (info != null) {
			ret = genObjName(retType, Integer.valueOf(expectedResult));
		} else {
			ret = expectedResult;
		}
		return ret;
	}

	public static String genObjName(String type, Integer id) {
		return "obj" + BuilderGen.capitalize(type) + "_" + String.valueOf(id);
	}

	public static String genPreStateClean(TestData testData) {
		String ret = "";

		for (ObjectInstance obj : testData.getPreObjList()) {
			String obj_type = obj.getName();
			int oid = obj.getOid();
			ret += String.format("%s = null;\n", BuilderGen.genObjName(obj_type, oid));
		}

		return ret;
	}

	public static String genPostStateClean(TestData testData) {
		String ret = "";

		for (ObjectInstance obj : testData.getPostObjList()) {
			String obj_type = obj.getName();
			int oid = obj.getOid();
			ret += String.format("%s = null;\n", BuilderGen.genObjName(obj_type, oid) + "Post");
		}

		return ret;
	}

	public static String genPreStateDecl(TestData testData) {
		String ret = "";

		for (ObjectInstance obj : testData.getPreObjList()) {
			String obj_type = obj.getName();
			int oid = obj.getOid();
			ret += String.format("%s %s = null;\n", obj_type, BuilderGen.genObjName(obj_type, oid));
		}

		return ret;
	}

	public static String genPostStateDecl(TestData testData) {
		String ret = "";

		for (ObjectInstance obj : testData.getPostObjList()) {
			String obj_type = obj.getName();
			int oid = obj.getOid();
			ret += String.format("%s %s = null;\n", obj_type, BuilderGen.genObjName(obj_type, oid) + "Post");
		}

		return ret;
	}

	public static String genPreStateInit(Model clsInfo, TestData testData) {
		String ret = "";

		for (ObjectInstance obj : testData.getPreObjList()) {
			String obj_type = obj.getName();
			int oid = obj.getOid();
			ret += String.format("%s = new %s().", BuilderGen.genObjName(obj_type, oid), "Builder" + obj_type);
			for (Triple<String, String, String> attr : obj.getAttrValueList()) {
				// if attr is a listed object, then it's the id
				UML2Class info = clsInfo.findClassInfoByName(attr.getMiddle());
				if (info != null) {
					ret += String.format("%s(%s).", attr.getLeft(), BuilderGen.genObjName(attr.getMiddle(), Integer.valueOf(attr.getRight())));
				} else {
						ret += String.format("%s(%s).", attr.getLeft(), attr.getRight());
				}
			}

			ret += "build();\n";
		}

		return ret;
	}

	public static String genPostStateInit(Model clsInfo, TestData testData) {
		String ret = "";

		for (ObjectInstance obj : testData.getPostObjList()) {
			String obj_type = obj.getName();
			int oid = obj.getOid();
			ret += String.format("%s = new %s().", BuilderGen.genObjName(obj_type, oid) + "Post", "Builder" + obj_type);
			for (Triple<String, String, String> attr : obj.getAttrValueList()) {
				// if attr is a listed object, then it's the id
				UML2Class info = clsInfo.findClassInfoByName(attr.getMiddle());
				if (info != null) {
					ret += String.format("%s(%s).", attr.getLeft(), BuilderGen.genObjName(attr.getMiddle(), Integer.valueOf(attr.getRight())));
				} else {
					ret += String.format("%s(%s).", attr.getLeft(), attr.getRight());
				}
			}

			ret += "build();\n";
		}

		return ret;
	}

	private static ArrayList<RoleSet> genRoleSet(ArrayList<AscInstance> ascList) {
		ArrayList<RoleSet> roleSet = new ArrayList<RoleSet>();

		for (AscInstance asc : ascList) {
			ArrayList<Triple<String, String, Integer>> roleList = asc.getRoleList();
			for (Triple<String, String, Integer> thisRole : roleList) {
				for (Triple<String, String, Integer> otherRole : roleList) {
					String obj2 = BuilderGen.genObjName(thisRole.getMiddle(), thisRole.getRight());
					String roleGetter = "get" + BuilderGen.capitalize(thisRole.getLeft());

					if (!otherRole.getLeft().equals(thisRole.getLeft())) {
						// otherRole add thisRole
						String obj1 = BuilderGen.genObjName(otherRole.getMiddle(), otherRole.getRight());
						RoleSet role = new RoleSet(obj1, roleGetter, obj2);
						roleSet.add(role);
					}
				}
			}
		}

		return roleSet;
	}

	private static ArrayList<RoleSet> genRoleSetPost(ArrayList<AscInstance> ascList) {
		ArrayList<RoleSet> roleSet = new ArrayList<RoleSet>();

		for (AscInstance asc : ascList) {
			ArrayList<Triple<String, String, Integer>> roleList = asc.getRoleList();
			for (Triple<String, String, Integer> thisRole : roleList) {
				for (Triple<String, String, Integer> otherRole : roleList) {
					String obj2 = BuilderGen.genObjName(thisRole.getMiddle(), thisRole.getRight());
					String roleGetter = "get" + BuilderGen.capitalize(thisRole.getLeft());

					if (!otherRole.getLeft().equals(thisRole.getLeft())) {
						// otherRole add thisRole
						String obj1 = BuilderGen.genObjName(otherRole.getMiddle(), otherRole.getRight()) + "Post";
						RoleSet role = new RoleSet(obj1, roleGetter, obj2);
						roleSet.add(role);
					}
				}
			}
		}

		return roleSet;
	}

	private static void outputFile(String path, String content) {
		FileWriter output = null;

		try {
			final File file = new File(path);
			final File parent_directory = file.getParentFile();

			if (null != parent_directory) {
				parent_directory.mkdirs();
			}

			output = new FileWriter(path);
			BufferedWriter writer = new BufferedWriter(output);
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
