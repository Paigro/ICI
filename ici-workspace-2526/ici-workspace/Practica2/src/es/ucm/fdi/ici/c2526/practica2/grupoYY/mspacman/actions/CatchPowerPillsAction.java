package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import es.ucm.fdi.ici.Action;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class CatchPowerPillsAction implements Action {

	public CatchPowerPillsAction() {}
	
	@Override
	public MOVE execute(Game game) {
        // Va hacia la pill mas cercana
        int np = nearestPowerPill(game);
        if(np!=-1){
        return game.getApproximateNextMoveTowardsTarget(
                game.getPacmanCurrentNodeIndex(),
                np,
                game.getPacmanLastMoveMade(),
                Constants.DM.PATH);
        }
        return MOVE.NEUTRAL;
	}

    private int nearestPowerPill(Game game){
        Queue<Integer> toVisit  = new LinkedList<Integer>();
        toVisit.add(game.getPacmanCurrentNodeIndex());
        ArrayList<Integer> visited  = new ArrayList<Integer>(game.getNumberOfNodes());

        while(!toVisit.isEmpty()){
            // Guardamos el nodo.
            int current = toVisit.poll();
            // Marcar como visitado.
            visited.add(current);

            // Guardamos informacion.
            if (isActivePowerPill(game, current)) return current;

            // Explora vecinos.
            for (Integer i : game.getNeighbouringNodes(current)){
                if (visited.contains(i) || i == -1 ) continue;
                toVisit.add(i);
            }
        }
        return -1;
    }
    // Devuelve true si en la id indicada hay una pill.
    private boolean isActivePowerPill(Game game, int id) {
        int[] powerPillIndices = game.getCurrentMaze().powerPillIndices;
        for (int i = 0; i < powerPillIndices.length; i++) {
            if (powerPillIndices[i] == id) {
                return game.isPowerPillStillAvailable(i);
            }
        }
        return false;
    }

	@Override
	public String getActionId() {
		return "Cath Pills Action";
	}

}
