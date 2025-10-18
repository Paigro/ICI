package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;

public class MoreEdibleGhost implements Transition {

	public MoreEdibleGhost() {
		super();
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		return input.moreGhostEdible();
	}

	@Override
	public String toString() {
		return "There are more edible ghost";
	}

	
	
}