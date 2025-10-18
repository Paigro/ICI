package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

import org.mindswap.pellet.utils.Pair;

import java.util.Map;

public class Comer2Cazar implements Transition {
    
	public Comer2Cazar() {}

	@Override
	public boolean evaluate(Input in) {
        MsPacManInput m = (MsPacManInput) in;
        Map<GHOST, Boolean>  ghostEdible = m.getGhostEdible();
		// Si se ha comido una powerPill hay al menos un fantasma comestible
        // Comprobar que no haya fantasma no comestible.
		for (Boolean ghost : ghostEdible.values()) {
			if (ghost) {
				return true;
			}
		}
        return false;
	}

	@Override
	public String toString() {
		return String.format("Comer2Cazar");
	}
}
