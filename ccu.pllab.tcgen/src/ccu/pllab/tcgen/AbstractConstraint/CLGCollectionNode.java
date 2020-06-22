package ccu.pllab.tcgen.AbstractConstraint;


import java.util.ArrayList;

import org.junit.validator.PublicClassValidator;

public class CLGCollectionNode extends CLGConstraint{
	CLGConstraint initial;
	CLGConstraint condition;
	CLGConstraint body;
	CLGConstraint increment;
	CLGConstraint start;
	String acc_type;
	
	public CLGCollectionNode()
	{
		super();
	}
	
	public void setInitial(CLGConstraint initial) {
		this.initial = initial;
		System.out.println(this.initial.getCLPInfo());
	}
	
	public CLGConstraint getInitial() {
		return initial;
	}
	
	public void setCondition(CLGConstraint condition) {
		this.condition = condition;
		System.out.println(this.condition.getCLPInfo());
	}
	
	public CLGConstraint getCondition() {
		return condition;
	}
	
	public void setBody(CLGConstraint body) {
		this.body = body;
		//System.out.println(this.body.getCLPInfo());
	}
	
	public CLGConstraint getBody() {
		return body;
	}
	
	public void setIncrement(CLGConstraint increment) {
		this.increment = increment;
	}
	public CLGConstraint getIncrement() {
		return increment;
	}
	public void setStart(CLGConstraint start)
	{
		this.start=start;
	}
	public CLGConstraint getStart()
	{
		return this.start;
	}
	public void setAccType(String acc_type)
	{
		this.acc_type=acc_type;
	}
	public String getAccType()
	{
		return this.acc_type;
	}
	@Override
	public  String getImgInfo()
	{
		return "Sequence->iterate";
	}
	@Override
	public  String getCLPInfo()
	{
		return initial.getCLPInfo();
			//	return "";
	}
	@Override
	public  ArrayList<String> getInvCLPInfo()
	{
		return null;
	}
	@Override
	public  CLGConstraint clone()
	{
		CLGCollectionNode collectionNode =new CLGCollectionNode();
		collectionNode.setInitial(this.initial);
		collectionNode.setCondition(this.condition);
		collectionNode.setBody(this.body);
		return collectionNode;
	}
	@Override
	public  String getCLPValue()
	{
		return "Acc";
	}
	@Override
	public  String getLocalVariable()
	{
		return "";
	}
	@Override
	public  void setCLPValue(String data)
	{
		
	}
	@Override
	public  void negConstraint()
	{
		
	}

	@Override
	public void preconditionAddPre() {
		this.initial.preconditionAddPre();;
		this.condition.preconditionAddPre();
		this.body.preconditionAddPre();
	}

	@Override
	public void postconditionAddPre() {
		this.initial.postconditionAddPre();
		this.condition.postconditionAddPre();
		this.body.postconditionAddPre();
	}
}
