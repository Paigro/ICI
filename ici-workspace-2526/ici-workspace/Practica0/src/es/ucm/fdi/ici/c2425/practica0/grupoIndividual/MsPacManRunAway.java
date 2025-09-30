package es.ucm.fdi.ici.c2425.practica0.grupoIndividual;

import java.awt.Color;

import pacman.controllers.PacmanController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Game;
import pacman.game.GameView;

public class MsPacManRunAway extends PacmanController
{	
    @Override
    public MOVE getMove(Game game, long timeDue)
    {   	
    	double nearestGhost = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
    	GHOST ghostToRun = GHOST.BLINKY;
    	
    	for(GHOST ghostType : GHOST.values())
    	{
    		double aux = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.MANHATTAN);
    		if(aux < nearestGhost)
    		{
    			nearestGhost = aux;
    			ghostToRun = ghostType;
    		}
    	}
    	
    	GameView.addLines(game, Color.ORANGE, game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostToRun));
    	
    	System.out.println(ghostToRun);

    	MOVE nextMove = game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostToRun), game.getPacmanLastMoveMade(), DM.MANHATTAN);
    	
        return nextMove;
    }

    public String getName()
    {
    	return "MsPacmanRunAway";
    }   
}