package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

import org.mindswap.pellet.utils.Pair;

import java.util.Map;

public class Comer2Huir implements Transition {
	private int limit;          // Distancia de peligro
    private int edibleThreshold; // Tiempo mínimo de "edible" para seguir cazando
    /**
     * @param limit distancia máxima a la que se considera peligro un fantasma
     * @param edibleThreshold tiempo mínimo de edibleTime (en ticks) para considerarlo todavía seguro
     */
    public Comer2Huir(int limit, int edibleThreshold) {
        this.limit = limit;
        this.edibleThreshold = edibleThreshold;
    }

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput m = (MsPacManInput)in;
		Game game = m.getGame();
		Map<GHOST, Integer> ghostDistance = m.getGhostDistance();

		for (Map.Entry<GHOST, Integer> e : ghostDistance.entrySet()) {
			GHOST ghost = e.getKey();
			int dist = e.getValue();

			// Ignorar fantasmas lejanos.
			if (dist == -1 || dist > this.limit)
				continue;
			// Si el fantasma NO es comestible y esta fuera de la carcel
			if (!game.isGhostEdible(ghost) && game.getGhostLairTime(ghost) == 0)
				return true; // huir
			// Si el fantasma ES comestible pero su tiempo esta a punto de acabarse
			if (game.isGhostEdible(ghost) && game.getGhostEdibleTime(ghost) <= this.edibleThreshold)
				return true;
			
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("Huir2Comer");
	}
}
