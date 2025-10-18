package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import java.util.ArrayList;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GraphCost;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToLastActivePowerPill implements Action {

    GHOST ghost;
    int limit;
    GraphCost graph;
	public GoToLastActivePowerPill(GHOST ghost, int limit, GraphCost graph) {
		this.ghost = ghost;
		this.limit = limit;
		this.graph = graph;
	}

	@Override
	public MOVE execute(Game game) {
		MOVE move = null;
        int lastPowerPill = lastEatenPowerPill(game);
        int ghostNode = game.getGhostCurrentNodeIndex(ghost);
        ArrayList<Integer> path = graph.GetPathAStar(game, ghostNode,lastPowerPill );
        if (path == null || path.size() <= 2) { // Si no hay ruta o la ruta no indica movimiento, quedarnos neutro
            move = MOVE.NEUTRAL;
        }
        else {
        	int nextNode = path.get(1);
        	move = game.getApproximateNextMoveTowardsTarget(ghostNode, nextNode, game.getGhostLastMoveMade(ghost), DM.PATH);
        }
	        
		return move;
	}
	int lastEatenPowerPill(Game game) {
		for (int powerPill : game.getPowerPillIndices()) {
			if(!game.isPowerPillStillAvailable(powerPill)) {
				double aux = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), powerPill);
	    		if(aux < limit) {
	    			return powerPill;
	    		}
			}
		}
		return -1;
	}
	@Override
	public String getActionId() {
		return ghost+ "go to last active power pill";
	}
}
