package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import java.util.ArrayList;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GraphCost;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ProtectEdibleGhost implements Action {

    GHOST ghost;
    GraphCost graph;
    private int maxLookaheadSteps = 30;
	public ProtectEdibleGhost( GHOST ghost, GraphCost graph) {
		this.ghost = ghost;
		this.graph = graph;
	}

	@Override
	public MOVE execute(Game game) {
		 // Encontrar el fantasma comestible mas cercano
        GHOST edible = getEdibleGhostNear(game);
        
        int myNode = game.getGhostCurrentNodeIndex(ghost);
        int edibleNode = game.getGhostCurrentNodeIndex(edible);
        if (myNode == -1 || edibleNode == -1) {
            return MOVE.NEUTRAL;
        }
        
        // Determinar la "posición adelantada" del fantasma comestible en su direccion actual:
        MOVE edibleLastMove = game.getGhostLastMoveMade(edible);
        int targetNode;
        if (edibleLastMove == null || edibleLastMove == MOVE.NEUTRAL) {
            // Si no tiene dirección, objetivo = su nodo actual
            targetNode = edibleNode;
        } else {
            // Buscar la proxima interseccion hacia donde se dirige el comestible
            int nextIntersection = findNextIntersection(game, edibleNode, edibleLastMove, maxLookaheadSteps);
            if (nextIntersection == -1) {
                // fallback: intenta ocupar el vecino inmediato en su dirección
                int neighbor = game.getNeighbour(edibleNode, edibleLastMove);
                targetNode = (neighbor == -1) ? edibleNode : neighbor;
            } else {
                targetNode = nextIntersection;
            }
        }

        // Calcular el camino A* (usando tu GraphCost) hacia targetNode
        ArrayList<Integer> path = graph.GetPathAStar(game, myNode, targetNode);

        if (path != null && path.size() > 1) {
            int nextNode = path.get(1);
            return game.getApproximateNextMoveTowardsTarget(myNode, nextNode, game.getGhostLastMoveMade(ghost), DM.PATH);
        }

        return game.getApproximateNextMoveTowardsTarget(myNode, targetNode, game.getGhostLastMoveMade(ghost), DM.PATH);
	}

	@Override
	public String getActionId() {
		return ghost + "go to pretect edible ghost";
	}
	public GHOST getEdibleGhostNear(Game game) {
		double minDist = Double.MAX_VALUE;
		GHOST nearest = null;
		for (GHOST gType : GHOST.values()) {
			if(game.isGhostEdible(gType) && gType != ghost) {
				double aux = game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(gType), DM.PATH);
				minDist = Math.min(aux, minDist);
				if(minDist == aux) {
					nearest = gType;
				}
			}
		}
		return nearest;
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
	
}