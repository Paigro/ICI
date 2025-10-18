package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import java.util.ArrayList;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GraphCost;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class InterceptAction implements Action {

    GHOST ghost;
    GraphCost graph;
	public InterceptAction( GHOST ghost, GraphCost graph) {
		this.ghost = ghost;
		this.graph = graph;
	}

	@Override
	public MOVE execute(Game game) {
		MOVE move = null;
		int ghostNode = game.getGhostCurrentNodeIndex(ghost);
		int pacNode = game.getPacmanCurrentNodeIndex();
		MOVE pacLastMove = game.getPacmanLastMoveMade();
		int targetIntersection = findNextIntersection(game, pacNode, pacLastMove, 30);
		if (targetIntersection == -1) {
            targetIntersection = pacNode;
        }
		ArrayList<Integer> path = graph.GetPathAStar(game, ghostNode,targetIntersection );
		if (path == null || path.size() <= 2) { // Si no hay ruta o la ruta no indica movimiento, quedarnos neutro
            move = MOVE.NEUTRAL;
        }
        else {
        	int nextNode = path.get(1);
        	move = game.getApproximateNextMoveTowardsTarget(ghostNode, nextNode, game.getGhostLastMoveMade(ghost), DM.PATH);
        }
	        
		return move;
	}

	private int findNextIntersection(Game game, int pacNode, MOVE pacMove, int maxSteps) {
        if (pacMove == null || pacMove == MOVE.NEUTRAL) return -1;

        int node = pacNode;
        MOVE dir = pacMove;
        for (int step = 0; step < maxSteps; step++) {
            int next = game.getNeighbour(node, dir);
            if (next == -1) return -1;
            int[] neigh = game.getNeighbouringNodes(next);
            int degree = 0;
            for (int n : neigh) if (n != -1) degree++;
            if (degree != 2) {
                return next;
            }
            node = next;
        }
        return -1;
    }
	
	@Override
	public String getActionId() {
		return ghost + " intercept";
	}
}