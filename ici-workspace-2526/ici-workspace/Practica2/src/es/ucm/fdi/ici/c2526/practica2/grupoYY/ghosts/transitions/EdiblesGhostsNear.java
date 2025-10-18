package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import java.util.Map.Entry;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class EdiblesGhostsNear implements Transition {

	GHOST ghost;
	public Entry<GHOST, Double> ghostNear;
	public static double thresold = 50;
	public EdiblesGhostsNear(GHOST ghost) {
		super();
		this.ghost = ghost;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		ghostNear = input.getDistToEdibleGhost(ghost);
		return  ghostNear.getValue() < thresold;
	}

	@Override
	public String toString() {
		return "There are edible ghosts near";
	}

	
	
}