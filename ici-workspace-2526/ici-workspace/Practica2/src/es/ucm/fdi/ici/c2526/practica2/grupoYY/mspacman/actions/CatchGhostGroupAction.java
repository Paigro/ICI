package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class CatchGhostGroupAction implements Action {

    int minDistance;
    /**
     * @param minDistance Distancia minima para ser considerado parte de un grupo.
     */
	public CatchGhostGroupAction(int minDistance) {
		this.minDistance = minDistance;
	}
	
	@Override
	public MOVE execute(Game game) {
		
		int id = getGroupID(game);
		if (id >=0)
			return game.getApproximateNextMoveTowardsTarget(
					game.getPacmanCurrentNodeIndex(), id, 
					game.getPacmanLastMoveMade(), DM.PATH);
        return MOVE.NEUTRAL;
	}
	
	private int getGroupID(Game game) {        
        // Comprueba los fantasmas cercanos a un grupo
        ArrayList<Integer> groups;
        int maxnGhost = 0;
        int idMaxGhost = -1;
		for (GHOST ghost : GHOST.values()) {
			int nGhosts = 1;
			for(GHOST ghost2 : GHOST.values()) {
				if(game.getDistance(
						game.getGhostCurrentNodeIndex(ghost),
						game.getGhostCurrentNodeIndex(ghost2),
						DM.PATH) <= this.minDistance) nGhosts++;
			}	
			// Si se encuentra un grupo mayor cambia el grupo mejor
			if (nGhosts > maxnGhost) {
				maxnGhost = nGhosts;
				idMaxGhost = game.getGhostCurrentNodeIndex(ghost); 
			}
		}
        return idMaxGhost;
	}
	
	@Override
	public String getActionId() {
		return "Catch Ghost Group";
	}

}
