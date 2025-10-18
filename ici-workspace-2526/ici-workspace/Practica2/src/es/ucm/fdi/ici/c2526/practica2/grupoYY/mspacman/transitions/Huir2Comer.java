package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

import org.mindswap.pellet.utils.Pair;

import java.util.Map;

public class Huir2Comer implements Transition {
	private int dangerLimit;
    private int edibleSearchLimit;

    /**
     * @param dangerLimit distancia maxima para considerar a un fantasma peligroso.
     * @param edibleSearchLimit distancia maxima para considerar a un fantasma comestible cercano.
     */
    public Huir2Comer(int dangerLimit, int edibleSearchLimit) {
        this.dangerLimit = dangerLimit;
        this.edibleSearchLimit = edibleSearchLimit;
    }

    @Override
    public boolean evaluate(Input in) {
        MsPacManInput m = (MsPacManInput) in;
        Game game = m.getGame();
        Map<GHOST, Integer> ghostDistance = m.getGhostDistance();

        boolean peligro = false;
        boolean oportunidad = false;

        for (Map.Entry<GHOST, Integer> e : ghostDistance.entrySet()) {
            GHOST ghost = e.getKey();
            int dist = e.getValue();

            if (dist == -1) continue; // ignorar fantasmas no visibles.
            // Fantasma peligroso cerca.
            if (!game.isGhostEdible(ghost) && game.getGhostLairTime(ghost) == 0 && dist <= dangerLimit) {
                peligro = true;
                break;
            }
            // Fantasma comestible cercano.
            if (game.isGhostEdible(ghost) && dist <= edibleSearchLimit)
                oportunidad = true;
        }

        // Devolver true solo si no hay peligro ni oportunidad de caza.
        return !peligro && !oportunidad;
    }

    @Override
    public String toString() {
        return String.format("No Peligro Ni Comestibles");
    }
}
