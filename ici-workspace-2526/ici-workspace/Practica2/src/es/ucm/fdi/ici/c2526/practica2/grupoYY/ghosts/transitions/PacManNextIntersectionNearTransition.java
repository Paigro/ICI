package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import java.util.ArrayList;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GraphCost;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Transition: si la distancia del fantasma a la próxima intersección
 * donde irá PacMan es menor que un limite o si el fantasma lleva demasiado
 * tiempo patrullando cambiar a interceptar.
 */
public class PacManNextIntersectionNearTransition implements Transition {

    private GHOST ghost;
    private int threshold;
    private int maxPatrolTicks;
    private int patrolCounter;
    private GraphCost graph;
    //preguntar a dani como ha puestos los costes
    private float multiplierToIntersection = 4.0f;
    

    public PacManNextIntersectionNearTransition(GHOST ghost, int threshold, int maxPatrolTicks, GraphCost graph) {
        super();
    	this.ghost = ghost;
        this.threshold = threshold;
        this.maxPatrolTicks = maxPatrolTicks;
        this.patrolCounter = 0;
        this.graph = graph;
    }

    @Override
    public boolean evaluate(Input in) {
        GhostsInput input = (GhostsInput) in;
        Game game = input.getGame();

        int ghostNode = game.getGhostCurrentNodeIndex(ghost);
        int pacNode = game.getPacmanCurrentNodeIndex();
        MOVE pacLastMove = game.getPacmanLastMoveMade();

        if (ghostNode == -1 || pacNode == -1) return false;

        int targetIntersection = findNextIntersection(game, pacNode, pacLastMove, 30); // 30 pasos max
        if (targetIntersection == -1) {
            targetIntersection = pacNode;
        }

        double dist = game.getDistance(ghostNode, targetIntersection, pacman.game.Constants.DM.PATH);

        patrolCounter++;

        //distancia o tiempo de patrulla excedido
        boolean closeEnough = dist <= threshold;
        boolean forcedByTime = patrolCounter >= maxPatrolTicks;

        if (closeEnough || forcedByTime) {
            patrolCounter = 0;
            //actualizar costes de los nodos hacia esa interseccion 
            ArrayList<Integer> path = graph.GetPathAStar(game, pacNode, targetIntersection);
            if(path != null) {
            	graph.penalizePath(path, multiplierToIntersection, true);
            	return true;
            }
            return false;
        }

        return false;
    }

    @Override
    public String toString() {
        return ghost + " go to PacMan next intersection (th=" + threshold + ",maxT=" + maxPatrolTicks + ")";
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
