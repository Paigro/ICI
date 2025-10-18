package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

import org.mindswap.pellet.utils.Pair;

import java.util.Map;

public class Cazar2Comer implements Transition {
    
	public Cazar2Comer() {}

	@Override
	public boolean evaluate(Input in) {
        MsPacManInput m = (MsPacManInput) in;
        Map<GHOST, Boolean>  ghostEdible = m.getGhostEdible();
		// Si no encuentra fantasma comestible pasa a comer
		for (Boolean ghost : ghostEdible.values()) {
			if (ghost) {
				return false;
			}
		}
        return true;
	}

	@Override
	public String toString() {
		return String.format("Cazar2Comer");
	}
}
