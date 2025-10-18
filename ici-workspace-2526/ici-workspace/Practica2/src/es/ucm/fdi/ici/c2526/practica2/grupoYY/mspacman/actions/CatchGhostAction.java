package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class CatchGhostAction implements Action {

	public CatchGhostAction() {
		// TODO Auto-generated constructor stub
	}

    private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();
	
	@Override
	public MOVE execute(Game game) {
        // Va hacia el fantasma mas cercano.
        int ng = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
        for (GHOST ghost : GHOST.values()) {
            int ghostNode = game.getGhostCurrentNodeIndex(ghost);
            if (game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), ghostNode) <
                game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), ng) && game.isGhostEdible(ghost)) {
                ng = ghostNode;
            }            
        }
        return game.getApproximateNextMoveTowardsTarget(
                game.getPacmanCurrentNodeIndex(),
                ng,
                game.getPacmanLastMoveMade(),
                DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Chase Ghost";
	}

}
