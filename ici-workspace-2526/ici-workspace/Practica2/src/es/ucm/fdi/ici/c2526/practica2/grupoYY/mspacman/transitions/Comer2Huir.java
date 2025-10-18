package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

import org.mindswap.pellet.utils.Pair;

import java.util.Map;

public class Comer2Huir implements Transition {
    int limit;
    /**
     * @param limit limite para ser considerado un fantasma dentro de la zona de peligro.
     */
	public Comer2Huir(int limit) {this.limit = limit;}

	@Override
	public boolean evaluate(Input in) {
        MsPacManInput m = (MsPacManInput) in;
        Map<GHOST, Integer>  ghostEdible = m.getGhostDistance();
		// Si no hay ningun fantasma comestible pasa a comer
		for (Integer ghost : ghostEdible.values()) {
			if (ghost <= this.limit) {
				return false;
			}
		}
        return true;
	}

	@Override
	public String toString() {
		return String.format("Huir2Comer");
	}
}
