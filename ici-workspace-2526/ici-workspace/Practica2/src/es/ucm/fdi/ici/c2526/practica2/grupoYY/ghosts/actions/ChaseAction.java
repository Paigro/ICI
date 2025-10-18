package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import java.util.ArrayList;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GraphCost;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChaseAction implements Action {

    GHOST ghost;
    GraphCost graph;
	public ChaseAction( GHOST ghost, GraphCost graph) {
		this.ghost = ghost;
		this.graph = graph;
	}

	@Override
	public MOVE execute(Game game) {
		MOVE move = null;
		int startNode = game.getGhostCurrentNodeIndex(ghost);
		int pacManNode = game.getPacmanCurrentNodeIndex();
		ArrayList<Integer> path = graph.GetPathAStar(game, startNode, pacManNode);
        if (path == null || path.size() <= 2) { // Si no hay ruta o la ruta no indica movimiento, quedarnos neutro
            move = MOVE.NEUTRAL;
        }
        else {
        	int nextNode = path.get(1);
        	move = game.getApproximateNextMoveTowardsTarget(startNode, nextNode, game.getGhostLastMoveMade(ghost), DM.PATH);
        }
        return move;
	}

	@Override
	public String getActionId() {
		return ghost + "chases";
	}
}
