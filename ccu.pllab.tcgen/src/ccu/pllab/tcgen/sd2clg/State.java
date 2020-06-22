package ccu.pllab.tcgen.sd2clg;

import java.util.ArrayList;
import java.util.List;

 
public class State{
	
	private int id;
	private String name;
	private List<Transition> transitions=new ArrayList<Transition>();
	
	public State(int id, String name){
		this.id=id;
		this.name=name;
	}
	
	public void addtransition(Transition trans){
		this.transitions.add(trans);
	}
	
	public int getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public List<Transition> getTransitions(){
		return this.transitions;
	}

}
