package ccu.pllab.tcgen.sd2clg;

import java.util.ArrayList;
import java.util.List;
 

public class StateDigram {

	private List<State> states=new ArrayList<State>();
	private List<Transition> transitions=new ArrayList<Transition>();
	private String sdName = "";
	private ArrayList<String> sdAttribute = new ArrayList<String>();

	public StateDigram(){
		
	}
	public StateDigram(String sdName, ArrayList<String> sdAttribute){
		this.sdName = sdName;
		this.sdAttribute = sdAttribute;
	}
	public List<State> getStates(){
		return this.states;
	}
	public void addstate(State states){
		this.states.add(states);
	}
	public List<Transition> getTransitions(){
		return this.transitions;
	}
	public void addtransition(Transition trans){
		this.transitions.add(trans);
	}
	public String getSDName(){
		return this.sdName;
	}
	public ArrayList<String> getSDAttribute(){
		return this.sdAttribute;
	}
}
