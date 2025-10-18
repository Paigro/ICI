package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostReadyToExitTransition implements Transition {

	GHOST ghost;
	public GhostReadyToExitTransition(GHOST ghost) {
		super();
		this.ghost = ghost;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		return !input.isGhostInLair(ghost);
	}

	@Override
	public String toString() {
		return ghost + " ready to exit lair";
	}

	
	
}
