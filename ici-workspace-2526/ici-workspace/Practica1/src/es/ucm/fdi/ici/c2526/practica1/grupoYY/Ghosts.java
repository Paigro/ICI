package es.ucm.fdi.ici.c2526.practica1.grupoYY;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

class GHOST_PARAMETERS
{
	public GHOST name;
	public int personality;
	public int nextPersonalityChange;
	
    private Random rnd = new Random(); // Para generar randoms.

	GHOST_PARAMETERS(GHOST n, int p, int np)
	{
		name = n;
		personality = p;
		nextPersonalityChange = np;
	}
	void changeGhostPersonality(int newPersonality)
	{
		personality = newPersonality;
		nextPersonalityChange = rnd.nextInt(1, 10);
		System.out.println(name + ": Change to: " + personality + " " + nextPersonalityChange);
	}
	int getPersonality()
	{
		System.out.println(name + ": Get nextPersonalityChange");
		return personality;
	}
	int getNextChange()
	{
		System.out.println(name + ": Get nextPersonalityChange");
		return nextPersonalityChange;
	}
	void decrease()
	{
		nextPersonalityChange--;
		System.out.println(name + ": Decrease: " + nextPersonalityChange);
	}
}

public final class Ghosts extends GhostController
{
    private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
    private EnumMap<GHOST, GHOST_PARAMETERS> parameters = null;
    private Random rnd = new Random(); // Para generar randoms.
    
    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
    {
    	if(parameters == null)
    	{
			System.out.println("Creacion de Fantasmas.");
        	parameters = new EnumMap<GHOST, GHOST_PARAMETERS>(GHOST.class);
        	for (GHOST ghostType : GHOST.values())
        	{
        		parameters.put(ghostType, new GHOST_PARAMETERS(ghostType, 1, 0)); // Todos los fantasmas empiezan con personalidad 1.
        		parameters.get(ghostType).changeGhostPersonality(rnd.nextInt(1, 6));
        	}
    	}
    	
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
    	
    	// Recorrer los 4 fantasmas.
        moves.clear();
        for (GHOST ghostType : GHOST.values())
        {
        	// Calculamos camino si el fantasma lo necesita.
            if (game.doesGhostRequireAction(ghostType))
            {
            	int personality = parameters.get(ghostType).getPersonality();
            	
            	
            	
            	// HACER LO DE LOS CAMINOS AQUI.
            	
            	
            	
            	
            	if(parameters.get(ghostType).getNextChange() <= 0)
            	{
            		parameters.get(ghostType).changeGhostPersonality(rnd.nextInt(1, 6));
            	}
            	else
            	{
            		parameters.get(ghostType).decrease();
            	}
            	
            	moves.put(ghostType, MOVE.NEUTRAL);
            }
        }
        return moves;  
    }
    
    
    int changePersonality()
    {
    	return 0;	
    }
    
    /*MOVE searchPath() 
    {
    	
    }*/
    
    public String getName() {
    	return "GhostsNeutral";
    }
}
