package ccu.pllab.tcgen.libs.pivotmodel;

 
import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ccu.pllab.tcgen.ast.ASTNode;

public class ClassDiagToJson {

	private org.eclipse.uml2.uml.Package oPackageModel;

	private URI typesUri = null;

	/* umlDumpByEMF construct the package of input uml file */
	public ClassDiagToJson(File oUmlModel) {
		if (oUmlModel.exists()) {
			typesUri = URI.createFileURI(oUmlModel.getPath());
			init();
		} else
			System.err.println("No such file!");
	}

	public ClassDiagToJson(URI uri) {
		typesUri = uri;
		init();
	}

	public JSONArray getAssociationJSON() {
		JSONArray association_array = new JSONArray();
		for (Element et : this.oPackageModel.getOwnedElements()) {
			/* Association(MemberEnd) */
			if (et.eClass().getName().toString().equals("Association")) {
				JSONObject association_object = new JSONObject();
				try {
					association_object.put("name", et.eGet(et.eClass().getEAllAttributes().get(0)).toString());

					Association ass = (Association) et;
					JSONArray member_array = new JSONArray();
					for (Property ass_end : ass.getMemberEnds()) {
						JSONObject member_object = new JSONObject();
						member_object.put("name", ass_end.getLabel());
						member_object.put("type", ass_end.getType().getLabel());
						member_object.put("upper", String.valueOf(ass_end.upperBound()));
						member_object.put("lower", String.valueOf(ass_end.lowerBound()));
						member_array.put(member_object);
					}
					association_object.put("roleList", member_array);
					association_array.put(association_object);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		}
		return association_array;
	}

	// should modify
	private String getAttrType(Property prop) {
		if (prop.getType().getLabel() == null) {
			String type_long_name = prop.getType().toString();
			return (type_long_name.substring(type_long_name.indexOf("#") + 1, type_long_name.length() - 1));
		} else {
			return prop.getType().getLabel();
		}
	}

	public JSONArray getClassJSON() {
		JSONArray class_array = new JSONArray();
		for (Element et : this.oPackageModel.getOwnedElements()) {
			/* Class(Property, Operation) */
			if (et.eClass().getName().toString().equals("Class")) {
				JSONObject class_object = new JSONObject();
				try {
					class_object.put("name", et.eGet(et.eClass().getEAllAttributes().get(0)).toString());
					JSONArray property_array = new JSONArray();
					JSONArray operation_array = new JSONArray();

					for (Element et_nest : et.getOwnedElements()) {
						int attr_idx = 0;

						if (et_nest.eClass().getName().equals("Property")) {
							JSONObject property_object = new JSONObject();
							Property prop = (Property) et_nest;
							property_object.put("id", attr_idx++);
							property_object.put("name", prop.getLabel());
							property_object.put("type", getAttrType(prop));
							if (prop.getUpper() == -1) {
								property_object.put("upper", 999);
							} else {
								property_object.put("upper", prop.getUpper());
							}
							property_object.put("lower", prop.getLower());
							property_object.put("unique", String.valueOf(prop.isUnique()));
							// if the property have association
							if (prop.getAssociation() != null) {
								property_object.put("association", prop.getAssociation().getLabel());
							} else {
								property_object.put("association", "");
							}
							property_array.put(property_object);

						} else if (et_nest.eClass().getName().equals("Operation")) {
							Operation oper = (Operation) et_nest;
							JSONObject operation_object = new JSONObject();
							String return_type = "void";
							operation_object.put("name", oper.getLabel());
							// operation_object.put("class",
							// et.eGet(et.eClass().getEAllAttributes().get(0)).toString());

							JSONArray arg_array = new JSONArray();
							int arg_idx = 0;
							for (Parameter oper_param : oper.getOwnedParameters()) {
								if (oper_param.getLabel() == null) {
									return_type = getParamType(oper_param);
								} else {
									JSONObject arg_object = new JSONObject();
									arg_object.put("id", arg_idx++);
									arg_object.put("name", oper_param.getLabel());
									arg_object.put("type", getParamType(oper_param));
									arg_array.put(arg_object);
								}
							}
							operation_object.put("argList", arg_array);
							operation_object.put("ret_type", return_type);
							operation_array.put(operation_object);
						}
						class_object.put("attrList", property_array);
						class_object.put("methodList", operation_array);
					}
					class_array.put(class_object);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return class_array;
	}

	public JSONArray getClassOpJSON(ASTNode parentTreeNode) {
		JSONArray class_array = new JSONArray();
		for (Element et : this.oPackageModel.getOwnedElements()) {
			/* Class(Operation) */
			if (et.eClass().getName().toString().equals("Class")) {
				try {
					JSONObject class_object = new JSONObject();
					JSONArray operation_array = new JSONArray();
					for (Element et_nest : et.getOwnedElements()) {
						class_object.put("name", et.eGet(et.eClass().getEAllAttributes().get(0)).toString());
						if (et_nest.eClass().getName().equals("Operation")) {
							Operation oper = (Operation) et_nest;
							JSONObject operation_object = new JSONObject();
							String return_type = null;
							operation_object.put("name", oper.getLabel());
							operation_object.put("class", et.eGet(et.eClass().getEAllAttributes().get(0)).toString());

							JSONArray arg_array = new JSONArray();
							for (Parameter oper_param : oper.getOwnedParameters()) {
								JSONObject arg_object = new JSONObject();
								if (oper_param.getLabel() == null) {
									return_type = getParamType(oper_param);
								} else {
									arg_object.put("name", oper_param.getLabel());
									arg_object.put("type", getParamType(oper_param));
								}
								arg_array.put(arg_object);
							}
							operation_object.put("argList", arg_array);
							operation_object.put("ret_type", return_type);
							operation_array.put(operation_object);
						}

					}
					class_object.put("methodList", operation_array);
					class_array.put(class_object);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return class_array;
	}

	public JSONObject getPackageNameJSON() {
		String pkgname = oPackageModel.getQualifiedName().toString();
		JSONObject prjJSON = new JSONObject();
		try {
			prjJSON.put("package", pkgname);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return prjJSON;
	}

	private String getParamType(Parameter param) {
		if (param.getType().getLabel() == null) {
			String type_long_name = param.getType().toString();
			return (type_long_name.substring(type_long_name.indexOf("#") + 1, type_long_name.length() - 1));
		} else {
			return param.getType().getLabel();
		}
	}

	private void init() {
		ResourceSet oResSet = new ResourceSetImpl();
		oResSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		oResSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
		oResSet.createResource(typesUri);
		Resource r = oResSet.getResource(typesUri, true);

		oPackageModel = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(r.getContents(), UMLPackage.Literals.PACKAGE);
	}

	public JSONObject toJSON() {
		JSONObject uml_json = new JSONObject();
		JSONObject pgkJSON = this.getPackageNameJSON();
		JSONArray class_array = getClassJSON();
		JSONArray association_array = getAssociationJSON();
		try {
			for (int index = 0; index < class_array.length(); index++) {
				JSONArray new_attr_array = new JSONArray();
				for (int i = 0; i < class_array.getJSONObject(index).getJSONArray("attrList").length(); i++) {
					JSONObject new_attr = class_array.getJSONObject(index).getJSONArray("attrList").getJSONObject(i);
					if (new_attr.getString("association").equals("")) {
						
						new_attr.remove("association");
						new_attr_array.put(new_attr);
					}
				}
				class_array.getJSONObject(index).put("attrList", new_attr_array);
			}
			for (int index = 0; index < association_array.length(); index++) {
				JSONArray new_roleList = new JSONArray();
				for (int i = 0; i < association_array.getJSONObject(index).getJSONArray("roleList").length(); i++) {
					JSONObject new_attr = association_array.getJSONObject(index).getJSONArray("roleList").getJSONObject(i);
					new_attr.remove("lower");
					new_attr.remove("upper");
					new_roleList.put(new_attr);
				}
				association_array.getJSONObject(index).put("roleList", new_roleList);
			}
			uml_json.put("package", pgkJSON.get("package"));
			uml_json.put("class", class_array);
			uml_json.put("association", association_array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return uml_json;
	}

	public String toJSONString() {
		JSONObject json_obj = this.toJSON();
		JSONArray class_array = new JSONArray();
		String output = "{";

		try {
			output += String.format("\"package\":\"%s\",", json_obj.get("package"));
			output += "\"class\":[";
			class_array = json_obj.getJSONArray("class");
			String cls_str = "";
			for (int i = 0; i < class_array.length(); i++) {
				JSONObject class_json = class_array.getJSONObject(i);
				cls_str += String.format("{\"name\":\"%s\"," + "\"attrList\":%s," + "\"methodList\":%s}", class_json.get("name"), class_json.get("attrList"), class_json.get("methodList"));
				if (i < class_array.length() - 1)
					cls_str += ",";
			}
			output += cls_str + "],";

			output += "\"association\":" + this.getAssociationJSON().toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		output += "}";

		return output;
	}
}
