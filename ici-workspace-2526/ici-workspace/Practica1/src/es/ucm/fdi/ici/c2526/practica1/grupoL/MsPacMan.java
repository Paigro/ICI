package es.ucm.fdi.ici.c2526.practica1.grupoL;

import java.awt.Color;
import java.util.Vector;
import java.util.Queue;
import java.util.List;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.LinkedList;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class MsPacMan extends PacmanController
{
	int posObjetive = -1;
	boolean runAway = false;
	boolean edibleGhost = false;
    @Override
    public MOVE getMove(Game game, long timeDue)
    {
    	MoveCell objetiveCell = null;
    	if(requiereAccionPacman(game)) {
    		objetiveCell = nextMove(game, game.getPacmanCurrentNodeIndex(), 7);
    		if (objetiveCell != null) {
    			posObjetive = objetiveCell.actualCell;
    			while(objetiveCell.prevCell != null)
    				objetiveCell = objetiveCell.prevCell;
    		}
    	}
    	
    	if(posObjetive != -1)
    		GameView.addLines(game, Color.ORANGE, game.getPacmanCurrentNodeIndex(), posObjetive);
        
    	if (objetiveCell != null)
    		return objetiveCell.nextMove;
    	else 
    		return MOVE.NEUTRAL;
    }
    
    public boolean requiereAccionPacman(Game game) {
        return game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade()).length > 1;
    }
    
    public MoveCell nextMove(Game game, int startIndex, int maxDepth) {
    	
    	// Cola de nodos a visitar.
    	Queue<MoveCell> toVisit = new LinkedList<>();
    	int[] visited = new int[(int)Math.pow(4, maxDepth)];


    	// Inicializa los primeros movimientos sin volver atras.
    	for (MOVE move : game.getPossibleMoves(startIndex)) {
    		if (move != game.getPacmanLastMoveMade().opposite()) {
    			int nextNode = game.getNeighbour(startIndex, move);
    			if (nextNode != -1) {
    				toVisit.add(new MoveCell(nextNode, move));
    			}
    		}
		}
    	
    	MoveCell targetCell = null;
    	int depth = 0; // Contador de profundidad.
    	
    	// No Runaway.
    	runAway = false;
    	// Hay fantasmas comestibles cerca.
    	edibleGhost = false;
    	for (GHOST ghostType : GHOST.values()) {
    		if (game.isGhostEdible(ghostType))
    			edibleGhost = true;
    	}
    	
    	// Busqueda en anchura.
    	while (!toVisit.isEmpty() && depth < Math.pow(4,  maxDepth)) {
    		MoveCell current = toVisit.remove();
    		int currentNode = current.actualCell;
    		
    		// Mira si hay un fantasma para marcar la ruta como huida. 
    		GHOST ghost = isGhost(game, currentNode);        	
        	if (ghost != null )
        		if(!game.isGhostEdible(ghost)) {
        			//System.out.println("HUYE");
        			runAway = true;
        		}
            
    		// Si hemos encontrado un camino valido terminamos la busqueda.
    		if (evaluatePath(game, currentNode)) {
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
    	GameView.addPoints(game, Color.ORANGE, visited);
    	
    	return targetCell;
    }
    
    private boolean evaluatePath(Game game, int id) {
    	if (runAway)
    		return isRunAwayPath(game, id);
    	else
    		return isPointPath(game, id);
    }
    
    // Este metodo se usa para saber si la celda actual esta dentro de un camino valido
    // True: sale de la busqueda devolviendo el camino actual.
    // False: sigue buscando.    
    private boolean isRunAwayPath(Game game, int id) {
    	GHOST ghost = isGhost(game, id);
    	
    	if (ghost!= null && game.isGhostEdible(ghost)) { 			
    			return isActivePowerPill(game, id);
    		}
    	return false;
    }
    
    // Este metodo se usa para saber si la celda actual esta dentro de un camino valido
    // True: sale de la busqueda devolviendo el camino actual.
    // False: sigue buscando.  
    private boolean isPointPath(Game game, int id) {	
    	GHOST ghost = isGhost(game, id);
    	
    	if (edibleGhost) {
    		if (ghost!= null && game.isGhostEdible(ghost)) { 			
    			return true;
    		}
    			return false;
    	}
    	return isActivePill(game, id);
    }
    
    // Comprueba si un camino ha sido visitado.
    private boolean isInVisited(int[] visited, int toVisit) {
    	for (int i = 0; i < visited.length; i++) {
			if (toVisit == visited[i])
				return true;
		}
    	return false;
    }
    
    // Devuelve true si en la id indicada hay una power pill.
    private boolean isActivePowerPill(Game game, int id) {
    	int[] powerPillIndices = game.getCurrentMaze().powerPillIndices; 
        for (int i = 0; i < powerPillIndices.length; i++) {
            if (powerPillIndices[i] == id) {
                return game.isPowerPillStillAvailable(i);
            }
        }
        return false;
    }
    // Devuelve true si en la id indicada hay una pill.
    private boolean isActivePill(Game game, int id) {
    	int[] pillIndices = game.getCurrentMaze().pillIndices; 
        for (int i = 0; i < pillIndices.length; i++) { 
            if (pillIndices[i] == id) {
                return game.isPillStillAvailable(i);
            }
        }
        return false;
    }
    // Devuelve el fantasma (si lo hay) de la id indicada. Si no devuelve null
    private GHOST isGhost(Game game, int id) {
    	for (GHOST ghostType : GHOST.values()){
    		int ghostIndex = game.getGhostCurrentNodeIndex(ghostType);
    		if (id == ghostIndex)
    			return ghostType;
    	}
    	return null;
    }
    enum CellType{Ghost, GhostEdible, Pill, PowerPill, None};
    
    // Clase que almacena informacion del nodo a recorrer.
    class MoveCell{
    	public MOVE nextMove;
    	public int actualCell;
    	public MoveCell prevCell; // Para reconstruir el camino.
    	
    	public MoveCell(int cell, MOVE move, MoveCell previous) {
    		nextMove= move; actualCell = cell; prevCell = previous; 
    	}
    	public MoveCell(int cell, MOVE move) {
    		nextMove= move; actualCell = cell; prevCell = null;
    	}
    			
    }
    
    public String getName() {
    	return "MsPacManGrupoL";
    }
}