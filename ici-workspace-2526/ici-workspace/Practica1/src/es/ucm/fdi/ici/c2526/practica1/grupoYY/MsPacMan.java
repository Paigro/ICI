package es.ucm.fdi.ici.c2526.practica1.grupoYY;

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
    @Override
    public MOVE getMove(Game game, long timeDue)
    {
    	// Movimiento del PacMan:
    		
    			
    			// Si esta pacman detras de un fantasma no huye.
    		
    	// Sino buscar la Pill mas cercana calculando los caminos. Quedandose con el que no tenga power pill o en el caso de que ambos la tengan ir a por el mas cercano. Si es la misma distancia da igual.
    	// Si hay un fantasma mas cerca de una Power Pill que yo me voy.
    	
    	// Calcular todos los caminos posibles y por pesos quedarse con el mejor.
    	
    	// Huida: descartar caminos
    	
    	MoveCell objetiveCell = null;
    	if(requiereAccionPacman(game)) {

    		objetiveCell = nextMove(game, game.getPacmanCurrentNodeIndex(), 5);
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
    
    public String getName() {
    	return "MsPacManNeutral";
    }
    
    
    public MoveCell nextMove(Game game, int startIndex, int maxDepth) {
    	
    	// Cola de nodos a visitar.
    	Queue<MoveCell> toVisit = new LinkedList<>();
    	List<MoveCell> posibleWay = new ArrayList<MsPacMan.MoveCell>();
    	int[] visited = new int[(int)Math.pow(4, maxDepth)];


    	// Inicializa los primeros movimientos sin volver atras.
    	for (MOVE move : game.getPossibleMoves(startIndex)) {
    		if (move != game.getPacmanLastMoveMade().opposite()) {
    			int nextNode = game.getNeighbour(startIndex, move);
    			CellType onCell = onCell(game, nextNode);
    			toVisit.add(new MoveCell(nextNode, move, onCell));
    		}
		}
    	
    	MoveCell targetCell = null;
    	int depth = 0; // Contador de profundidad.
    	
    	// Busqueda en anchura.
    	while (!toVisit.isEmpty() && depth < Math.pow(4,  maxDepth)) {
    		MoveCell current = toVisit.remove();
    		int nextNode = game.getNeighbour(current.actualCell, current.nextMove);
    		
    		// Si no se puede avanzar o se ha visitado ya, ignorar.
    		if (nextNode == -1 || isInVisited(visited, nextNode)){
    			posibleWay.add(current);
    			continue;
    		}
    		
    		// Si hemos encontrado un camino valido terminamos la busqueda.
    		if (fountPath(game, nextNode)) {
    			// Pinta los nodos que han sido visitados.
    	    	GameView.addPoints(game, Color.ORANGE, visited);
    			return targetCell;
    		}
    		
    		// Marcar como visitado.
    		visited[depth] = current.actualCell;
    		
    		// Expandir a los vecinos.
    		for(MOVE move : game.getPossibleMoves(nextNode)) {
    			if (move == current.nextMove.opposite()) continue;
    			int newNode = game.getNeighbour(nextNode, move);
    			CellType onCell = onCell(game, newNode);
    			System.out.println(onCell);
    			toVisit.add(new MoveCell(newNode, move, current, onCell));
    		}
    		depth++;
    	}
    	
    	// Pinta los nodos que han sido visitados.
    	GameView.addPoints(game, Color.ORANGE, visited);
    	
    	return getCellToMove(posibleWay);
    }
    
    private MoveCell getCellToMove(List<MoveCell> posibleWays) {
    	MoveCell bestMoveCell = null;
    	int bestPoints = 0;
    	for (MoveCell moveCell : posibleWays) {
    		int points = 0;
    		MoveCell auxCell = moveCell;
			while (auxCell.prevCell != null) {
				points += cellPoints(auxCell.contains);
				auxCell = auxCell.prevCell;
			}
			if (points > bestPoints) {
				bestPoints = points;
				bestMoveCell = auxCell;
			}
		}
    	
    	return bestMoveCell;
    }
    
      
    private CellType onCell(Game game, int id) {
    	GHOST ghost = isGhost(game, id);
    	
    	/*if (ghost != null) {
    		if (game.isGhostEdible(ghost))
    			return CellType.GhostEdible;
    		else
    			return CellType.Ghost;
    	}*/
    		    			
    	if (isActivePowerPill(game,id))
    		return CellType.PowerPill;
    	if (isActivePill(game,id))
    		return CellType.Pill; 
    	
    	return CellType.None;
    }
    
    
    private int cellPoints(CellType cellType) {
    	switch(cellType) {
    	case CellType.GhostEdible:
    		return Constants.GHOST_EAT_SCORE;
    	case CellType.Pill:
    		return Constants.PILL;
    	case CellType.PowerPill:
    		return Constants.POWER_PILL;
    	default:
    		return 0;
    	}
    }
    
    // Este metodo se usa para saber si la celda actual esta dentro de un camino valido
    // True: sale de la busqueda devolviendo el camino actual.
    // False: sigue buscando.
    GHOST ghost;
    private boolean fountPath(Game game, int id) {
    	 ghost = isGhost(game, id);
    	
    	// Bool para indicar que huye?
    	 // Si fantasma cerca:
    	 	// Si comible ir a por el.
    	 	// Si no es comible.
    	 		// Ir a por Power Pill.  
    	 		// Huir.
    	
    	/*if (ghost != null) {
    		if (game.isGhostEdible(ghost))
    			return true;
    		return isActivePowerPill(game, id);
    	}*/
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
                return game.isPillStillAvailable(i);
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
    	public CellType contains;
    	
    	public MoveCell(int cell, MOVE move, MoveCell previous, CellType cellContains) {
    		nextMove= move; actualCell = cell; prevCell = previous; contains = cellContains;
    	}
    	public MoveCell(int cell, MOVE move, CellType cellContains) {
    		nextMove= move; actualCell = cell; prevCell = null; contains = cellContains;
    	}
    			
    }
    

}