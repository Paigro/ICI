package es.ucm.fdi.ici.c2425.practica0.grupoIndividual;

import java.util.EnumMap;

import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Game;

public final class GhostsAggresive extends GhostController {
    private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);

    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
    {
        moves.clear();
        for (GHOST ghostType : GHOST.values())
        {
            if (game.doesGhostRequireAction(ghostType))
            {
            	MOVE move = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghostType), DM.MANHATTAN );
            	moves.put(ghostType, move);
            }
        }
        return moves;
    }
    
    public String getName()
    {
    	return "GhostsRandomAggresive";
    }
}
