package es.ucm.fdi.ici.c2526.practica1.AndresGarciaNavarroPabloIglesiasRodrigo;

import java.util.Vector;

import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController
{
	
    @Override
    public MOVE getMove(Game game, long timeDue)
    {
    	// Movimiento del PacMan:
    	// Si fantasma cerca:
    		// Si no es comible
    			// Ir a por Power Pill mas cercana. Sino huir.
    			// Si esta pacman detras de un fantasma no huye.
    		// Si comible ir a por el.
    	// Sino buscar la Pill mas cercana calculando los caminos. Quedandose con el que no tenga power pill o en el caso de que ambos la tengan ir a por el mas cercano. Si es la misma distancia da igual.
    	// Si hay un fantasma mas cerca de una Power Pill que yo me voy.
    	
    	// Calcular todos los caminos posibles y por pesos quedarse con el mejor.
    	
    	// Huida: descartar caminos con fantasmas.
    	
        return MOVE.NEUTRAL;
    }
    
    public String getName() {
    	return "MsPacManNeutral";
    }
    
    class Path{
    	int points;
    	Vector<MOVE> moves;
    	
    	Path(int maxCells)
    	{
    		points = 0;
    		moves = new Vector<>(maxCells); 
    	}
    	
    	public void addMove(MOVE move, int points)
    	{
    		moves.add(move);
    	}
    }
    

}