package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class RunAwayAction implements Action {
	int minDist;
	public RunAwayAction(int minDist) {
		this.minDist = minDist;
	}
	
	@Override
	public MOVE execute(Game game) {
		int objetivo = getFarID(game,game.getPacmanCurrentNodeIndex());
		if (objetivo>=0)
		return game.getApproximateNextMoveTowardsTarget(
				game.getPacmanCurrentNodeIndex(), 
				objetivo, game.getPacmanLastMoveMade(),
				DM.PATH);
		return MOVE.NEUTRAL;
	}
	
    private int getFarID(Game game,  int start) {
        Queue<Integer> q = new ArrayDeque<>();
        ArrayList<Integer> visit = new ArrayList<Integer>();
        q.add(start);
        while (!q.isEmpty()) {
            int u = q.poll();
            visit.add(u);
            for (int v : game.getNeighbouringNodes(u)) {
            	double dist = game.getDistance(
            			game.getPacmanCurrentNodeIndex(), 
            			v, DM.PATH);
                if (!visit.contains(v) && v != -1 && !isGhost(game, v)) {
                	if(dist >= this.minDist)
                		return v;
                    q.add(v);
                }
            }
        }
        return -1;

    }
    // Devuelve el fantasma (si lo hay) de la id indicada. Si no devuelve null
    private boolean isGhost(Game game, int id) {
        for (GHOST ghostType : GHOST.values()){
            int ghostIndex = game.getGhostCurrentNodeIndex(ghostType);
            if (id == ghostIndex && !game.isGhostEdible(ghostType))
                return true;
        }
        return false;
    }

	@Override
	public String getActionId() {
		return "Run Away Action";
	}

}
