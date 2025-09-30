package es.ucm.fdi.ici.c2526.practica0.IglesiasRodrigo;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Game;

public final class IglesiasRodrigoGhosts extends GhostController {
    private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
    private MOVE[] allMoves = MOVE.values(); // Posibles movimientos.
    private Random rnd = new Random(); // Para generar randoms.
    
    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
    {  	
    	moves.clear();
    	
    	for(GHOST ghostType : GHOST.values()) // Se tienen que mover.
    	{
    		if(game.doesGhostRequireAction(ghostType)) // No necesitan moverse.
    		{
    			final int limit = 40;
    			
    			int pacmanId = game.getPacmanCurrentNodeIndex(); // Posicion del Pacman. 			
    			
    			float random = rnd.nextFloat(0, 1); // Calculamos el random.
    			
    			// CASOS:
    			// Caso el fantasma es comible
    			if(game.isGhostEdible(ghostType) || isPacmanNearPowerPill(game, limit)) 
    			{
    				System.out.println(ghostType + ": Ay que me comen!");
    				moves.put(ghostType, game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType), pacmanId, game.getGhostLastMoveMade(ghostType), DM.MANHATTAN));
    			}
    			else if(random < 0.9) // probabilidad de que persiga a MsPacman.
    			{
    				System.out.println(ghostType + ": Perseguir a MsPacman.");
    				moves.put(ghostType,(game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), pacmanId, game.getGhostLastMoveMade(ghostType), DM.MANHATTAN )));
    			}
    			else if(random >= 0.1) // probabilidad de que vaya random.
    			{
    				System.out.println(ghostType + ": Perdido en los randoms.");
    				moves.put(ghostType, allMoves[rnd.nextInt(allMoves.length)]);
    			}	
    		}
    	}
    	return moves;
    }
    
    boolean isPacmanNearPowerPill(Game game, int limit)
    {
    	int [] powerPills = game.getActivePowerPillsIndices();
    	Boolean nearPowerPill = false;
    	
    	for(int powerPill : powerPills) // Voy a mantener esta estructura de for por sencillez auqnue seria mejor un while para poder acabar las iteraciones antes. Pero son solo 4 PowerPills no es para tanto.
    	{
    		if((game.getDistance(game.getPacmanCurrentNodeIndex(), powerPill, DM.MANHATTAN)) <= limit)
    		{
    			nearPowerPill = true;
    		}
    	}
	
    	return nearPowerPill;
    }
    
    public String getName()
    {
    	return "IglesiasRodrigoGhosts";
    }
}
