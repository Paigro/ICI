package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RandomMovementAction implements Action {

    GHOST ghost;
    private MOVE[] allMoves = MOVE.values();
    private Random rnd = new Random();
	public RandomMovementAction( GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
            return allMoves[rnd.nextInt(allMoves.length)];
        }
        return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return ghost + "random movement";
	}
}
