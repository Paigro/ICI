package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class ExploreAction implements Action {
	int minDist;
	public ExploreAction(int minDist) {
		this.minDist = minDist;
	}

	@Override
	public MOVE execute(Game game) {
		// Encontrar el nodo mas seguro del mapa.
        int safestNode = getFarthestSafeNode(game);

        if (safestNode != -1) {
            return game.getNextMoveTowardsTarget(
                game.getPacmanCurrentNodeIndex(),
                safestNode,
                game.getPacmanLastMoveMade(),
                DM.PATH);
        }
        return MOVE.NEUTRAL;
	}
	private int getFarthestSafeNode(Game game) {
	    int msp = game.getPacmanCurrentNodeIndex();

	    // Guardar las posiciones de los fantasmas peligrosos.
	    List<Integer> ghostNodes = new ArrayList<>();
	    for (GHOST ghost : GHOST.values()) {
	        if (!game.isGhostEdible(ghost) && game.getGhostLairTime(ghost) == 0) {
	            int gNode = game.getGhostCurrentNodeIndex(ghost);
	            if (gNode != -1)
	                ghostNodes.add(gNode);
	        }
	    }

	    Node[] allNodes = game.getCurrentMaze().graph;
	    int safestNode = -1;
	    double maxMinDist = -1;

	    // Buscar el nodo mas alejado de cualquier fantasma.
	    for (Node node : allNodes) {
	        double minDistToGhost = Double.MAX_VALUE;
	        for (int ghostNode : ghostNodes) {
	            double dist = game.getShortestPathDistance(node.nodeIndex, ghostNode);
	            if (dist < minDistToGhost)
	                minDistToGhost = dist;
	        }
	        // Evitar nodos demasiado cerca de fantasmas.
	        if (minDistToGhost < 15)
	            continue;

	        // Si es más seguro, actualizar
	        if (minDistToGhost > maxMinDist && minDistToGhost >= this.minDist) {
	            maxMinDist = minDistToGhost;
	            safestNode = node.nodeIndex;
	        }
	    }

	    if (safestNode == -1)
	        return -1; // no hay nodo seguro

	    // Encontrar el primer paso del camino mas seguro usando DFS.
	    boolean[] visited = new boolean[game.getNumberOfNodes()];
	    int[] parent = new int[game.getNumberOfNodes()];
	    for (int i = 0; i < parent.length; i++) parent[i] = -1;

	    boolean found = dfs(game, msp, safestNode, visited, parent, ghostNodes);

	    if (!found)
	        return -1;

	    // Retroceder desde el destino hasta encontrar el primer paso.
	    int current = safestNode;
	    int prev = parent[current];
	    while (prev != -1 && prev != msp) {
	        current = prev;
	        prev = parent[current];
	    }

	    return current;
	}

	/**
	 * DFS que busca un camino desde PacMan hasta el nodo seguro evitando fantasmas.
	 * Devuelve true si se ha encontrado un camino valido.
	 */
	private boolean dfs(Game game, int current, int target, boolean[] visited, int[] parent, List<Integer> ghostNodes) {
	    visited[current] = true;
	    if (current == target)
	        return true;

	    for (int neighbor : game.getNeighbouringNodes(current)) {
	        if (!visited[neighbor] && isSafeNode(game, neighbor, ghostNodes)) {
	            parent[neighbor] = current;
	            if (dfs(game, neighbor, target, visited, parent, ghostNodes))
	                return true;
	        }
	    }
	    return false;
	}

	/**
	 * Un nodo es seguro si no contiene un fantasma y no está demasiado cerca.
	 */
	private boolean isSafeNode(Game game, int node, List<Integer> ghostNodes) {
	    for (int ghostNode : ghostNodes) {
	        double dist = game.getShortestPathDistance(node, ghostNode);
	        if (dist < 10.0) // margen de seguridad
	            return false;
	    }
	    return true;
	}

	@Override
	public String getActionId() {
		return "Explore Action";
	}

}
