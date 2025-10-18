package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostWaitAction implements Action {

    GHOST ghost;
	public GhostWaitAction( GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {

	        // Si aún está en la "lair", no se mueve
	        if (game.getGhostLairTime(ghost) > 0) {
	            return MOVE.NEUTRAL;
	        }

	        // En caso de que ya haya salido, por seguridad, lo dejamos quieto
	        return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return ghost + " WaitInLair";
	}
}