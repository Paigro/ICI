package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

import org.mindswap.pellet.utils.Pair;

import java.util.ArrayList;
import java.util.Map;

public class CazarGrupo2Cazar implements Transition {
    int minDistance;
    /**
     * @param minDistance Distancia minima para considerar parte del grupo
     * @param minGhostToGroup Minimo de fantasmas juntos para considerarlo un grupo
     */
	public CazarGrupo2Cazar(int minDistance) {this.minDistance = minDistance;}

	@Override
	public boolean evaluate(Input in) {
        MsPacManInput m = (MsPacManInput) in;
        Map<GHOST, Integer>  ghostPos = m.getGhostID();
        
        boolean hayGrupo = false;
        // Comprueba los fantasmas cercanos a un grupo
        ArrayList<Integer> groups;
        for (Map.Entry<GHOST,Integer> e1 : ghostPos.entrySet()) {
            GHOST ghostType1 = e1.getKey();
            int pos1 = e1.getValue();
            int nGhosts = 1;
            // si es comestible pasamos al siguiente.
            if (m.getGame().isGhostEdible(ghostType1)) continue;
            // Comprobamos la cercania con el resto de fantasmas.
            for (Map.Entry<GHOST,Integer> e2 : ghostPos.entrySet()) {
                GHOST ghostType2 = e2.getKey();
                int pos2 = e2.getValue();

				if(m.getGame().getDistance(pos1, pos2, DM.PATH) <= this.minDistance 
						&& m.getGame().isGhostEdible(ghostType2)) 
					nGhosts++;
				if(nGhosts >= 2) hayGrupo = true;
			}		
		}
		
        return hayGrupo;
	}

	@Override
	public String toString() {
		return String.format("el grupo se ha disuelto");
	}
}
