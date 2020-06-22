package ccu.pllab.tcgen.AbstractConstraint;


import java.util.ArrayList;

public class CLGLiteralNode extends CLGConstraint {
	private String value;
	private String type;

	public CLGLiteralNode(String value) {
		super();
		this.value = value;
		this.type = "";
	}

	public CLGLiteralNode(String value, String type) {
		super();
		this.value = value;
		this.type = type;
	}

	public String getValue() {
		if (this.type != null && this.type.toLowerCase().equals("string")) {
			if (this.value.contains("\"") ) {
				return this.value;
			}
			if (this.value.contains("\'") ) {
				return this.value.replaceAll("\'", "\"");
			}
			else {
				return "\"" + this.value + "\"";
			}
		} else {
			return this.value;
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	@Override
	public String getImgInfo() {
		// System.out.println(this.value);
		String var = this.value;
		if (this.value.contains("\"")) {
			var = this.value.replace("\"", "\\\"");
		}
		return var;
	}

	@Override
	public String getCLPInfo() {
		return this.getValue();
	}
	@Override
	public  ArrayList<String> getInvCLPInfo()
	{
		return new ArrayList<String>();
	}
	@Override
	public CLGConstraint clone() {
		CLGConstraint cons=new CLGLiteralNode(this.value, this.type);
		cons.setCloneId(this.getConstraintId());
		return cons;
		//return new CLGLiteralNode(this.value, this.type);
	}

	@Override
	public String getCLPValue() {
		return this.getValue();
	}

	@Override
	public void setCLPValue(String data) {

	}

	@Override
	public String getLocalVariable() {
		// TODO Auto-generated method stub
		return "";
	}
	@Override
	public void negConstraint() {}
	@Override
	public void preconditionAddPre(){}
	@Override
	public void postconditionAddPre(){}
}
