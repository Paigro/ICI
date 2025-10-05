package es.ucm.fdi.ici.c2526.practica1.grupoL;

import java.awt.Color;
import java.util.Vector;

import java.util.Queue;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class MsPacMan2 extends PacmanController
{
	final int pillPoints = 100;
	final int powerPillPoints = 10;
	final int edibleGhostPoints = 100;
	final int ghostPoints = -1000;
	
	MoveCell Objetive = null;
    @Override
    public MOVE getMove(Game game, long timeDue)
    {
    	MOVE nextMove = MOVE.NEUTRAL;
    	if(requiereAccionPacman(game)) {
    		nextMove = nextMove(game, game.getPacmanCurrentNodeIndex(), 50);
    	}

    	if (Objetive != null && Objetive.id != -1)
    		GameView.addLines(game, Color.RED, game.getPacmanCurrentNodeIndex(), Objetive.id);
    	return nextMove;
    }
    
    public MOVE nextMove(Game game, int pacmanID, int maxDeep) {
    	// Cola de nodos a visitar.
    	Queue<MoveCell> toVisit = new LinkedList<>();
    	// Anyade los primeros nodos.
    	for (MOVE move : game.getPossibleMoves(pacmanID)) {
    		if (move == game.getPacmanLastMoveMade().opposite()) continue;
    		int newID = game.getNeighbour(pacmanID, move);
    		CellType type = cellContains(game, newID);
    		toVisit.add(new MoveCell(newID, move, 1, type));
    	}
    	
    	List<MoveCell> PosibleWays = BFS(game,toVisit,maxDeep);
    	
    	return evaluatePath(PosibleWays);
    	
    }
    public List<MoveCell> BFS(Game game, Queue<MoveCell> toVisit, int maxDeep) {
    	List<MoveCell> finalDeCaminos = new ArrayList<>();
    	Set<Integer> visited = new HashSet<>();
    	while(!toVisit.isEmpty()) {
    		MoveCell current = toVisit.remove();
    		
    		// Si ya lo visitamos, lo ignoramos
            if (current.id == -1) {
                continue;
            }
            visited.add(current.id);
            
    		// Comprobar si es un final de camino
    		if(current.level >= maxDeep || visited.contains(current.id)) {
    			finalDeCaminos.add(current);
    			continue;
    		}
    		
    		findNehibours(game, current, toVisit, visited);
    		
    	}
    	int[] array = new int[visited.size()];
    	int i = 0;
    	for (int val : visited) {
    	    array[i++] = val;
    	}
    	
    	// Pinta los nodos que han sido visitados.
    	GameView.addPoints(game, Color.ORANGE, array);
    	return finalDeCaminos;
    }
    private void findNehibours(Game game, MoveCell parent, Queue<MoveCell> toVisit, Set<Integer> visited) {
    	int id = parent.id;
    	MOVE lastMove = parent.move;
    	int parentLevel = parent.level;
    	for (MOVE move : game.getPossibleMoves(id)) {
    		if (move == lastMove.opposite()) continue;
    		int newID = game.getNeighbour(id, move);
    		if (newID == -1 || visited.contains(newID)) continue;
    		CellType type = cellContains(game, newID);
    		//if (type != CellType.None) System.out.println(type); 
    		toVisit.add(new MoveCell(newID, move, parentLevel + 1, type, parent));
    	}
    }
    
  
    // Devuelve el contenido de una cell
    private CellType cellContains(Game game, int id) {
    	if(isActivePowerPill(game, id)) return CellType.PowerPill;
    	if(isActivePill(game, id)) return CellType.Pill;
    	GHOST ghost = isGhost(game, id);
    	if(ghost != null) 
    		if(game.isGhostEdible(ghost))
    			return CellType.GhostEdible;
    		else
    			return CellType.Ghost;
    	return CellType.None;
    	
    }
    private int cellPoints( CellType type) {
    	switch(type) {
		case CellType.Pill:
			return pillPoints;
		case CellType.PowerPill:
			return powerPillPoints;
		case CellType.GhostEdible:
			return edibleGhostPoints;
		case CellType.Ghost:
			return ghostPoints;
		default:
			return 0;
    	}
    }
    private MOVE evaluatePath(List<MoveCell> PosibleWays) {
    	MoveCell bestWay = null;
    	int bestPoints = Integer.MIN_VALUE;
    	for (MoveCell way : PosibleWays) {
    		MoveCell walker = way;
    		int points = 0;
            boolean ghostDanger = false;
            boolean edibleNear = false;
            boolean powerNear = false;
            
            // Recorremos camino hacia atras
    		while (walker != null && walker.prevCell != null ) {
    			//points += cellPoints(walker.contains);
    			walker = walker.prevCell;
    			switch (walker.contains) {
                	case Ghost:
                		// Penaliza mucho si hay un fantasma no edible.
                		points -= 5000; 
                		ghostDanger = true;
                		break;
                	case GhostEdible:
                		// Solo positivo si no hay peligro de fantasmas normales.
                		points += ghostDanger ? -100 : 1000;
                		edibleNear = true;
                		break;
                	case PowerPill:
                		// Si hay peligro, valen mucho.
                		points += ghostDanger ? 2000 : -500; 
                		powerNear = true;
                		break;
                	case Pill:
                		// Siempre vale la pena.
                		points += 100;
                		break;
                	default:
                		break;
    			}
    			walker = walker.prevCell;
    		}

    		// Huir si no hay power pills.
    		//if (ghostDanger && !powerNear) points -= 20000; 
    		// Evita gastar power pill si no hay necesidad.
    		//if (!ghostDanger && !edibleNear && powerNear) points -= 10000; 
    		
    		// Actualiza si encuentra un camino mejor.
    		if (points > bestPoints) {
    			bestPoints = points;
    			bestWay = way;
    		}
    	}
    	Objetive = bestWay;
    	return bestWay.move;
    }
    
    // Devuelve true si MsPacMan esta en una interseccion.
    private boolean requiereAccionPacman(Game game) {
        return game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade()).length > 1;
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
    		if (!game.isPowerPillStillAvailable(i))
    			powerPillIndices[i] = -1;
		}
        for (int i = 0; i < powerPillIndices.length; i++) {
            if (powerPillIndices[i] != -1 && powerPillIndices[i] == id) {
                return true;
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
    // Devuelve el fantasma (si lo hay) de la id indicada. Si no devuelve null.
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
    	// Move para llegar a esta id
    	public MOVE move;
    	public int id;
    	public int level;
    	public MoveCell prevCell; // Para reconstruir el camino.
    	public CellType contains;
    	
    	public MoveCell(int cell, MOVE mve, int lvl, CellType conts , MoveCell previous) {
    		move = mve; id = cell; level = lvl; contains = conts; prevCell = previous; 
    	}
    	public MoveCell(int cell, MOVE mve, int lvl, CellType conts) {
    		move = mve; id = cell; level = lvl; contains = conts; prevCell = null;
    	}
    			
    }
    
    public String getName() {
    	return "MsPacManGrupoL";
    }
}