package es.ucm.fdi.ici.c2526.practica0.IglesiasRodrigo;

import java.awt.Color;
//import java.util.Random;

import pacman.controllers.PacmanController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Game;
import pacman.game.GameView;

public class IglesiasRodrigoMsPacman extends PacmanController
{	
	//private Random rnd = new Random();
    //private MOVE[] allMoves = MOVE.values();
	
    @Override
    public MOVE getMove(Game game, long timeDue)
    {   	
    	final int limit = 40; // Distancia para la que Pacman considere cerca un fantasma.
    	   	
    	GHOST nearestGhost = null;  	
    	
    	//System.out.println(ghostToRun);
    	
    	// CASOS:
    	nearestGhost = getNearestGhost(game, limit);
    	if(nearestGhost != null) // Caso hay fantasma lo suficientemente cerca.
    	{
    		if(!game.isGhostEdible(nearestGhost)) 
    		{
    			GameView.addLines(game, Color.RED, game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost));
        		return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade(), DM.MANHATTAN);
    		}
    		else // Caso hay fantasma comestible lo suficientemente cerca.
    		{
    			GameView.addLines(game, Color.GREEN, game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost));
        		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade(), DM.MANHATTAN);
    		}	
    	}
    	
    	// Caso base no hay fantasmas cerca va a comer pills o PowerPill si hay dentro del limite.
    	int pillToGo = (int) getNearestPill(game, limit);
    	GameView.addLines(game, Color.YELLOW, game.getPacmanCurrentNodeIndex(), pillToGo);
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), pillToGo, game.getPacmanLastMoveMade(), DM.MANHATTAN);
		
    	//return allMoves[rnd.nextInt(allMoves.length)];
    }
    
    // Metodo que devuelve el fantasma mas cercano a MsPacman.
    GHOST getNearestGhost(Game game, int limit)
    {
    	double distanceToNearest = 0;
    	GHOST ghostToRun = null;
    	
    	for(GHOST ghostType : GHOST.values())
    	{   		
    		double auxDis = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.MANHATTAN);
        	if(auxDis < distanceToNearest || distanceToNearest == 0)
        	{
        		distanceToNearest = auxDis;
        		ghostToRun = ghostType;
        	}    		
    	}
    	if(distanceToNearest <= limit)
    	{
    		return ghostToRun;
    	}
    	else 
    	{
    		return null;
    	} 
    }  
    
    // Metodo que devuelve la Pill mas cercana, si dentro del limite se encuentra una PowerPill, tendra prioridad.
    double getNearestPill(Game game, int limit) 
    {
    	double distanceToPowerpill = 0;
    	double distanceToPill = 0;
    	int nearestPill = 0;
    	int nearestPowerPill = 0;
    	
    	int[] pills = game.getActivePillsIndices();
    	int[] powerPills = game.getActivePowerPillsIndices();
    	
    	// Pills normales.
    	for(int pill : pills)
    	{
    		double auxDis = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.MANHATTAN);
			if(auxDis < distanceToPill || distanceToPill == 0)
			{
				distanceToPill = auxDis;
				nearestPill = pill;
			}
    	}
    	// PowerPills.
    	for(int powerPill : powerPills)
    	{
    		double auxDis = game.getDistance(game.getPacmanCurrentNodeIndex(), powerPill, DM.MANHATTAN);
			if(auxDis < distanceToPowerpill || distanceToPowerpill == 0)
			{
				distanceToPowerpill = auxDis;
				nearestPowerPill = powerPill;
			}
    	}
    	// Si hay PowerPill pues a por ella.
    	//if(distanceToPowerpill <= limit)
    	//{
    		//return nearestPowerPill;
    	//}
    	//else 
    	//{
    		return nearestPill;
    	//}
    }
    
    public String getName()
    {
    	return "IglesiasRodrigoMsPacman";
    }   
}