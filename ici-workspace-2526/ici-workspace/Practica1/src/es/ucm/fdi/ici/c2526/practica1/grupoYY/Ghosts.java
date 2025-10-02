package es.ucm.fdi.ici.c2526.practica1.grupoYY;

import java.awt.Color;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

//Si comibles huir, si no:
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

 /*class GHOST_PARAMETERS
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
		//System.out.println(name + ": Get personality");
		return personality;
	}
	int getNextChange()
	{
		//System.out.println(name + ": Get nextPersonalityChange");
		return nextPersonalityChange;
	}
	void decrease()
	{
		nextPersonalityChange--;
		//System.out.println(name + ": Decrease: " + nextPersonalityChange);
	}
}*/
//Clase que almacena informacion del nodo a recorrer.
class MoveCell
{
    public MOVE nextMove;
    public int actualCell;
    public MoveCell prevCell; // Para reconstruir el camino.

    public MoveCell(int cell, MOVE move, MoveCell previous)
    {
        nextMove = move; actualCell = cell; prevCell = previous;
    }
    public MoveCell(int cell, MOVE move)
    {
        nextMove= move; actualCell = cell; prevCell = null;
    }
}
abstract class PersonalityGhost
{	
	//abstract MOVE nextMove(Game game, int depth);
	public MoveCell nextMove(Game game, int startIndex, int maxDepth)
	{
    	
    	// Cola de nodos a visitar.
    	Queue<MoveCell> toVisit = new LinkedList<>();
    	int[] visited = new int[(int)Math.pow(4, maxDepth)];


    	// Inicializa los primeros movimientos sin volver atras.
    	for (MOVE move : game.getPossibleMoves(startIndex)) {
    		if (move != game.getPacmanLastMoveMade().opposite()) {
    			int nextNode = game.getNeighbour(startIndex, move);
    			toVisit.add(new MoveCell(nextNode, move));
    		}
		}
    	
    	// Si no hay movimientos posibles.
    	if (toVisit.isEmpty())
    		return new MoveCell(startIndex, MOVE.NEUTRAL);
    	
    	MoveCell targetCell = null;
    	int depth = 0; // Contador de profundidad.
    	
    	// Busqueda en anchura.
    	while (!toVisit.isEmpty() && depth < Math.pow(4,  maxDepth)) {
    		MoveCell current = toVisit.remove();
    		int nextNode = game.getNeighbour(current.actualCell, current.nextMove);
    		
    		// Si no se puede avanzar o se ha visitado ya, ignorar.
    		if (nextNode == -1 || isInVisited(visited, nextNode))
    			continue;
    		
    		// Si hemos encontrado un camino valido terminamos la busqueda.
    		if (fountPath(game, nextNode)) {
    			targetCell = current;
    			break;
    		}
    		
    		// Marcar como visitado.
    		visited[depth] = current.actualCell;
    		
    		// Expandir a los vecinos.
    		for(MOVE move : game.getPossibleMoves(nextNode)) {
    			if (move == current.nextMove.opposite()) continue;
    			int newNode = game.getNeighbour(nextNode, move);
    			toVisit.add(new MoveCell(newNode, move, current));
    		}
    		depth++;
    	}
    	
    	// Pinta los nodos que han sido visitados.
    	GameView.addPoints(game, Color.ORANGE, visited);
    	return targetCell;
    }
    
    // Comprueba si un camino ha sido visitado.
    private boolean isInVisited(int[] visited, int toVisit) {
    	for (int i = 0; i < visited.length; i++) {
			if (toVisit == visited[i])
				return true;
		}
    	return false;
    }
    
    // Este metodo se usa para saber si la celda actual esta dentro de un camino valido
    // True: sale de la busqueda devolviendo el camino actual.
    // False: sigue buscando.
    abstract boolean fountPath(Game game, int id);   
}
// Comportamiento aleatorio.
class RandomGhost extends PersonalityGhost
{ 
	@Override
	boolean fountPath(Game game, int id)
	{
		//System.out.println("Hola random.");
		return true;
	}
}
// Comportamiento que busca los caminos con un mayor numero de puntos.
class EvilGhost extends PersonalityGhost
{
	@Override
	boolean fountPath(Game game, int id)
	{
		//System.out.println("Hola evil.");
		return true;	
	}
}
// Comportamiento de ir a por Pacman..
class GoToPacmanGhost extends PersonalityGhost
{
	@Override
	boolean fountPath(Game game, int id)
	{
		//System.out.println("Hola ataque.");
		int pacManID = game.getPacmanCurrentNodeIndex();
		if(id == pacManID)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}	
}
// Comportamiento de huir.
class RunAwayGhost extends PersonalityGhost
{
	@Override
	boolean fountPath(Game game, int id)
	{
		//System.out.println("Hola huir.");
		int pacManID = game.getPacmanCurrentNodeIndex();
		if(id == pacManID)
		{
			return false;
		}
		else 
		{
			return true;
		}
	}
}
// Comportamiento para ir hacia un ghost que no es comible.
class RunToSafeGhost extends PersonalityGhost
{
	@Override
	boolean fountPath(Game game, int id)
	{
		//System.out.println("Hola ir a amigo.");
		for(GHOST ghostType : GHOST.values())
		{
			// Teniendo en cuenta que esta personalidad solo la tiene un fantasma comible no se va a encontrar a si mismo.
			if(game.isGhostEdible(ghostType))
			{
				return true;
			}
		}
		return false;
	}
}


public final class Ghosts extends GhostController
{
    private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
    //private EnumMap<GHOST, GHOST_PARAMETERS> parameters = null;
    private EnumMap<GHOST, PersonalityGhost> personalities = null;

    private Random rnd = new Random(); // Para generar randoms.
    
    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
    {
    	/*if(parameters == null)
    	{
			System.out.println("Creacion de Fantasmas.");
        	parameters = new EnumMap<GHOST, GHOST_PARAMETERS>(GHOST.class);
        	for (GHOST ghostType : GHOST.values())
        	{
        		parameters.put(ghostType, new GHOST_PARAMETERS(ghostType, 1, 0)); // Todos los fantasmas empiezan con personalidad 1.
        		parameters.get(ghostType).changeGhostPersonality(rnd.nextInt(1, 6));
        	}
    	}*/
    	// Creacion de las personalidades, al principio todas random.
    	if(personalities == null)
    	{
			System.out.println("Creacion de Personalidades.");
			personalities = new EnumMap<GHOST, PersonalityGhost>(GHOST.class);
        	for (GHOST ghostType : GHOST.values())
        	{
        		personalities.put(ghostType, new RandomGhost());
        	}
    	}
    	
    	moves.clear();
    	// Recorrer los 4 fantasmas.

        for (GHOST ghostType : GHOST.values())
        {      	
        	// Calculamos camino si el fantasma lo necesita.
            if (game.doesGhostRequireAction(ghostType))
            {	
            	/*int personality = parameters.get(ghostType).getPersonality();
            	// AQUI CALCULAR EL PATH
            	
            	switch (personality) 
            	{
				case 1: {
					nextMove = personalityRandom();
					break;
				}
				case 2: {
					nextMove = personalityEvil();
					break;
				}
				case 3: {
					nextMove = personalityFront();
					break;
				}
				case 4: {
					nextMove = personalityBehind();
					break;
				}
				case 5: {
					nextMove = personalityPowerPills();
					break;
				}
				default:
					throw new IllegalArgumentException("Switch mal por: " + personality);
				}
            	
           
            	
            	
            	
            	if(parameters.get(ghostType).getNextChange() <= 0)
            	{
            		parameters.get(ghostType).changeGhostPersonality(rnd.nextInt(1, 6));
            	}
            	else
            	{
            		parameters.get(ghostType).decrease();
            	}
            	
            	if(nextMove == null)
            	{
            		throw new IllegalArgumentException("No movimento calculado");
            	}
            	else 
            	{
            		moves.put(ghostType, nextMove);            		
            	}*/
            	
            	MoveCell nextMove = null;
          
            	if(game.isGhostEdible(ghostType))
            	{
            		int nEdibles = 0;
                	for(GHOST ghost : GHOST.values())
                	{
                		if(game.isGhostEdible(ghost))
                			nEdibles++;
                	}
                	//------
            		if(nEdibles > 0 && nEdibles < 4)
            		{
            			System.out.println("Ahora soy ir a amigo.");
                		personalities.put(ghostType, new RunToSafeGhost());
            		}
            		else
            		{
            			System.out.println("Ahora soy huida.");
            			personalities.put(ghostType, new RunAwayGhost());
            		}
            	}
            	else
            	{
            		//------
                	if(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.PATH) < 10)
                	{
            			System.out.println("Ahora te persigo.");
                		personalities.put(ghostType, new GoToPacmanGhost());
                	}          		
                	else 
                	{
                		float random = rnd.nextFloat(0, 1);
                		// Por ejemplo:
                		if(random < 0.5)
                		{
                			System.out.println("Ahora soy random.");
                			personalities.put(ghostType, new RandomGhost());
                		}
                		else
                		{
                			System.out.println("Ahora soy evil.");
                			personalities.put(ghostType, new EvilGhost());
                		}
                	}
                	//------
                	double distanceToPill = 0;
                	for(int pill : game.getActivePowerPillsIndices())
                	{
                		double auxDis = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH);
    					if(auxDis < distanceToPill || distanceToPill == 0)
            			{
    						distanceToPill = auxDis;
            			}
                	}
                	if(distanceToPill < 10)
                	{
                		System.out.println("Ahora soy huida.");
                		personalities.put(ghostType, new RunAwayGhost());
                	}
            	}
            	
            	
            	// Calculo del siguiente camino.
            	nextMove = personalities.get(ghostType).nextMove(game,game.getGhostCurrentNodeIndex(ghostType), 5);
            	
            	if(nextMove != null)
            	{            		
            		moves.put(ghostType, nextMove.nextMove); 
            	}
            	else
            	{
            		System.out.println("MAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAL");
            		moves.put(ghostType, MOVE.NEUTRAL);
            	}
            }
        }
        return moves;  
    }
    
    public String getName() {
    	return "GhostsNeutral";
    }
}