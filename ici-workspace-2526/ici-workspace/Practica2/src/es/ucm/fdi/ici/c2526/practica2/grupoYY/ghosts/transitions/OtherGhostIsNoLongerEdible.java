package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class OtherGhostIsNoLongerEdible implements Transition {

	GHOST ghost;
	public static double thresold = 50;
	public OtherGhostIsNoLongerEdible(GHOST ghost) {
		super();
		this.ghost = ghost;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		EdiblesGhostsNear edibleGhost = new EdiblesGhostsNear(ghost);
		boolean ghostNear = edibleGhost.evaluate(input);
		
		return ghostNear && input.getGame().isGhostEdible(edibleGhost.ghostNear.getKey());
	}

	@Override
	public String toString() {
		return "the other Ghost is no longer edible";
	}

	
	
}