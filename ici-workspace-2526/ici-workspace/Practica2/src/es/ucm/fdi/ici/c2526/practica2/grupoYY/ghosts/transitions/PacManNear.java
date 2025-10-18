package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class PacManNear implements Transition {

	GHOST ghost;
	public static double thresold = 80;
	public int num = 0;
	public PacManNear(GHOST ghost) {
		super();
		this.ghost = ghost;
		this.num++;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		return input.getDistGhostToPacman(ghost) < thresold;
	}

	@Override
	public String toString() {
		return this.ghost + " is near to Pacman " + this.num;
	}

	
	
}