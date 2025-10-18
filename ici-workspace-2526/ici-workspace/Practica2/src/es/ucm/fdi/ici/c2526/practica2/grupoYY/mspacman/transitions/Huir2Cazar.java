package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

import org.mindswap.pellet.utils.Pair;

import java.util.Map;

public class Huir2Cazar implements Transition {
	private int safeDistance;
    private int edibleThreshold;

    /**
     * @param safeDistance distancia minima para sentirse seguro (no hay fantasmas peligrosos cerca).
     * @param edibleThreshold tiempo minimo de edibleTime para que merezca la pena atacar.
     */
    public Huir2Cazar(int safeDistance, int edibleThreshold) {
        this.safeDistance = safeDistance;
        this.edibleThreshold = edibleThreshold;
    }

	public boolean evaluate(Input in) {
        MsPacManInput m = (MsPacManInput) in;
        Game game = m.getGame();
        Map<GHOST, Integer> ghostDistance = m.getGhostDistance();

        boolean edibleNearby = false;

        for (Map.Entry<GHOST, Integer> e : ghostDistance.entrySet()) {
            GHOST ghost = e.getKey();
            int dist = e.getValue();

            // Ignorar fantasmas no visibles
            if (dist == -1)
                continue;
            // Si hay algun fantasma peligroso demasiado cerca, todavia no es seguro cazar.
            if (!game.isGhostEdible(ghost) && game.getGhostLairTime(ghost) == 0 && dist < safeDistance)
            	return false;
            // Si hay algun fantasma comestible con suficiente tiempo, marcarlo.
            if (game.isGhostEdible(ghost) && game.getGhostEdibleTime(ghost) > edibleThreshold)
                edibleNearby = true;
        }

        // Solo pasamos a cazar si hay al menos un fantasma cazable y ninguno peligroso cerca.
        return edibleNearby;
    }

	@Override
	public String toString() {
		return String.format("Huir2Cazar");
	}
}
