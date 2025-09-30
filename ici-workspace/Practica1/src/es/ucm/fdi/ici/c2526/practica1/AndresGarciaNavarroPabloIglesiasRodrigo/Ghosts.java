package es.ucm.fdi.ici.c2526.practica1.AndresGarciaNavarroPabloIglesiasRodrigo;

import java.util.EnumMap;

import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public final class Ghosts extends GhostController {
    private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);

    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
    {
        moves.clear();
        for (GHOST ghost : GHOST.values()) {
            if (game.doesGhostRequireAction(ghost)) {
            	
            	// Si comibles huir, si no:
            	// Si MS. mas cerca de power pill que fantasma, huir.
            	// Sino ir a por ella.
            	
            	// 5 tipos?:
            	// Aleatorio. Si se acerca mucho a por MS.
            	// Perseguir por detras
            	// Perseguir por delante
            	// Rutita por Power Pills. Si se acerca mucho a por MS.
            	// Evil Ms Pacman.
            	
            	// Huir: 
            	// Descartar caminos con otros fantasmas comibles.
            	// Si hay no comibles ir con ellos
            	// Intentar ir por el camino con menos pills.
            	
            	
                moves.put(ghost, MOVE.NEUTRAL);
            }
        }
        return moves;
        
        
        
        
        
    }
    
    public String getName() {
    	return "GhostsNeutral";
    }
}
