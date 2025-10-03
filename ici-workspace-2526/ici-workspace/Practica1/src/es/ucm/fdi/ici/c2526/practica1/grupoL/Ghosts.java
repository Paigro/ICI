package es.ucm.fdi.ici.c2526.practica1.grupoL;

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

// Clase que almacena informacion del nodo a recorrer.
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
	GHOST ghostType;
	
	
	public PersonalityGhost(GHOST gT)
	{
		ghostType = gT;
	}
	
	
	//abstract MOVE nextMove(Game game, int depth);
	public MoveCell nextMove(Game game, int startIndex, int maxDepth)
	{
    	
    	// Cola de nodos a visitar.
    	Queue<MoveCell> toVisit = new LinkedList<>();
    	int[] visited = new int[(int)Math.pow(4, maxDepth)];


    	// Inicializa los primeros movimientos sin volver atras.
        for (MOVE move : game.getPossibleMoves(startIndex)) {
            if (move != game.getGhostLastMoveMade(ghostType).opposite()) {
                int nextNode = game.getNeighbour(startIndex, move);
                if (nextNode != -1) {
                    toVisit.add(new MoveCell(nextNode, move));
                }
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
            int currentNode = current.actualCell;


            // Si hemos encontrado un camino valido terminamos la busqueda.
            if (fountPath(game, currentNode)) {
                targetCell = current;
                break;
            }

            // Marcar como visitado.
            visited[depth] = current.actualCell;

            // Expandir a los vecinos.
            for(MOVE move : game.getPossibleMoves(currentNode)) {
                if (move == current.nextMove.opposite()) continue;
                int newNode = game.getNeighbour(currentNode, move);
                if(newNode == -1 || isInVisited(visited, newNode)) continue;
                toVisit.add(new MoveCell(newNode, move, current));
            }
            depth++;
        }
    	
    	// Pinta los nodos que han sido visitados.
    	//GameView.addPoints(game, Color.ORANGE, visited);
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
	
	public RandomGhost(GHOST ghostType) 
	{
		super(ghostType);
	}
	@Override
	boolean fountPath(Game game, int id)
	{
		//System.out.println("Hola random.");
		//GameView.addLines(game, Color.YELLOW, game.getPacmanCurrentNodeIndex(), id);
		return true;
	}
}
// Comportamiento que ir a la casilla siguiente del MsPacMan.
class EvilGhost extends PersonalityGhost
{
	public EvilGhost(GHOST gT) 
	{
		super(gT);
	}
	@Override
	boolean fountPath(Game game, int id)
	{
		//System.out.println("Hola evil.");
		int nextMSID = game.getNeighbour(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		if(id == nextMSID) 
		{
			//GameView.addLines(game, Color.RED, game.getPacmanCurrentNodeIndex(), id);
			return true;
		}
		else
		{
			return false;
		}
	}
}
// Comportamiento de ir a por Pacman.
class GoToPacmanGhost extends PersonalityGhost
{
	public GoToPacmanGhost(GHOST gT) 
	{
		super(gT);
	}
	@Override
	boolean fountPath(Game game, int id)
	{
		//System.out.println("Hola ataque.");
		int pacManID = game.getPacmanCurrentNodeIndex();
		if(id == pacManID)
		{
			//GameView.addLines(game, Color.YELLOW, game.getPacmanCurrentNodeIndex(), id);
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
	public RunAwayGhost(GHOST gT) 
	{
		super(gT);
	}
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
			//GameView.addLines(game, Color.GREEN, game.getPacmanCurrentNodeIndex(), id);
			return true;
		}
	}
}
// Comportamiento para ir hacia un ghost que no es comible.
class RunToSafeGhost extends PersonalityGhost
{
	public RunToSafeGhost(GHOST gT) 
	{
		super(gT);
	}
	@Override
	boolean fountPath(Game game, int id)
	{
		//System.out.println("Hola ir a amigo.");
		for(GHOST ghostType : GHOST.values())
		{
			// Teniendo en cuenta que esta personalidad solo la tiene un fantasma comible no se va a encontrar a si mismo.
			if(game.isGhostEdible(ghostType))
			{
				//GameView.addLines(game, Color.BLUE, game.getPacmanCurrentNodeIndex(), id);
				return true;
			}
		}
		return false;
	}
}


public final class Ghosts extends GhostController
{
    private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
    private EnumMap<GHOST, PersonalityGhost> personalities = null;

    private Random rnd = new Random(); // Para generar randoms.
    
    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
    {
    	// Creacion de las personalidades, al principio todas random.
    	if(personalities == null)
    	{
			//System.out.println("Creacion de Personalidades.");
			personalities = new EnumMap<GHOST, PersonalityGhost>(GHOST.class);
        	for (GHOST ghostType : GHOST.values())
        	{
        		personalities.put(ghostType, new RandomGhost(ghostType));
        	}
    	}
    	
    	moves.clear();
    	// Recorrer los 4 fantasmas.

        for (GHOST ghostType : GHOST.values())
        {      	
        	// Calculamos camino si el fantasma lo necesita.
            if (game.doesGhostRequireAction(ghostType))
            {	         	
            	MoveCell nextMove = null;
            	
            	// Si el fantasma es comible miramos casos:
            	if(game.isGhostEdible(ghostType))
            	{
            		int nEdibles = 0;
                	for(GHOST ghost : GHOST.values())
                	{
                		if(game.isGhostEdible(ghost))
                			nEdibles++;
                	}
                	// Si hay algun fantasma no edible => personalidad de ir a por un fantasma no comible.
            		if(nEdibles > 0 && nEdibles < 4 /*&& game.getDistance(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), DM.PATH) < 20*/)
            		{
            			//System.out.println("Ahora soy ir a amigo.");
                		personalities.put(ghostType, new RunToSafeGhost(ghostType));
            		}
            		else // Sino => huir normal.
            		{
            			//System.out.println("Ahora soy huida.");
            			personalities.put(ghostType, new RunAwayGhost(ghostType));
            		}
            	}
            	else // Si no es comible:
            	{
            		// Si el fantasma esta cerca de MsPacMan => ir a por ella.
                	if(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.PATH) < 20)
                	{
            			//System.out.println("Ahora te persigo.");
                		personalities.put(ghostType, new GoToPacmanGhost(ghostType));
                	}          		
                	else  // Sino => personalidad aleatoria entre random y evil.
                	{
                		float random = rnd.nextFloat(0, 1);
                		// Por ejemplo:
                		if(random < 0.5)
                		{
                			//System.out.println("Ahora soy random.");
                			personalities.put(ghostType, new RandomGhost(ghostType));
                			moves.put(ghostType, MOVE.values()[rnd.nextInt(MOVE.values().length)]);
                			continue;
                		}
                		else
                		{
                			//System.out.println("Ahora soy evil.");
                			personalities.put(ghostType, new EvilGhost(ghostType));
                		}
                	}
            	}
            	
            	
            	// Calculo del siguiente camino teniendo en cuenta la personalidad.
            	nextMove = personalities.get(ghostType).nextMove(game,game.getGhostCurrentNodeIndex(ghostType), 7);
            	
            	if(nextMove != null)
            	{            		
            		moves.put(ghostType, nextMove.nextMove); 
            	}
            	else
            	{
            		moves.put(ghostType, MOVE.NEUTRAL);
            	}
            }
        }
        return moves;  
    }
    
    public String getName()
    {
    	return "GhostGrupoL";
    }
}