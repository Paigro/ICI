package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

import org.mindswap.pellet.utils.Pair;

import java.util.Map;

public class Comer2Cazar implements Transition {

    private int edibleThreshold; // Tiempo mínimo de "edible" para seguir cazando
    /**
     * @param edibleThreshold tiempo minimo de edibleTime (en ticks) para considerarlo todavia seguro
     */
	public Comer2Cazar(int edibleThreshold) {
		this.edibleThreshold = edibleThreshold;
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput m = (MsPacManInput) in;
	    Game game = m.getGame();

	    // Comprobamos si hay al menos un fantasma comestible y activo (fuera del lair)
	    for (GHOST ghost : GHOST.values()) {
	        if (game.isGhostEdible(ghost) &&
	            game.getGhostEdibleTime(ghost) > this.edibleThreshold &&   // margen minimo de seguridad
	            game.getGhostLairTime(ghost) == 0) {     // no esta en la carcel
	            return true; // hay un fantasma valido que merece ser cazado
	        }
	    }

	    return false;
	}

	@Override
	public String toString() {
		return String.format("Comer2Cazar");
	}
}
