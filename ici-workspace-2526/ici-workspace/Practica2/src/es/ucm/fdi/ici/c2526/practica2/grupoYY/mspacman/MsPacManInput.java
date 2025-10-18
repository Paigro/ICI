package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman;

import es.ucm.fdi.ici.Input;
import org.mindswap.pellet.utils.Pair;
import pacman.game.Game;
import pacman.game.Constants.*;
import pacman.game.internal.Ghost;

import java.util.*;

public class MsPacManInput extends Input {

    private Map<GHOST, Integer> ghostDistance;
    private Map<GHOST, Boolean> ghostEdible;
    private Map<GHOST, Integer> ghostId;
    // Pill en orden de cercania.
    private ArrayList<Pair<Integer,Integer>> pill;
    // PowerPill en orden de cercania.
    private ArrayList<Pair<Integer,Integer>> powerPill;
    private int nLives;
    private int level;

	public MsPacManInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {

        this.ghostId = new HashMap<>();
        this.ghostEdible = new HashMap<>();
        this.ghostDistance = new HashMap<>();
        this.pill = new ArrayList<>();
        this.powerPill = new ArrayList<>();
        this.nLives = game.getPacmanNumberOfLivesRemaining();
        this.level = game.getCurrentLevel();
        int ms = game.getPacmanCurrentNodeIndex();

        // Informacion de los fantasmas
        for (GHOST ghost : GHOST.values()) {
            ghostId.put(ghost, this.game.getGhostCurrentNodeIndex(ghost));
            ghostEdible.put(ghost, this.game.isGhostEdible(ghost));
            int distance = this.game.getShortestPathDistance(
                    ms,
                    this.game.getGhostCurrentNodeIndex(ghost));
            ghostDistance.put(ghost, distance);
        }
        // Coge los indices de las powerPills
        int[] powerPillIndices = game.getCurrentMaze().powerPillIndices;
        for (int i = 0; i < powerPillIndices.length; i++) {
            if (game.isPowerPillStillAvailable(i)) {
                int distance = game.getShortestPathDistance(ms,powerPillIndices[i]);
                Pair<Integer, Integer> p = new Pair<>(powerPillIndices[i], distance);
                powerPill.add(p);
            }
        }
        // Ordenar por el segundo elemento (ascendente)
        powerPill.sort(Comparator.comparing(p -> p.second));

        // Coge las pills mas cercanas.
        bfs(ms);
    }

    /**
     * @return Devuelve las pill en orden de cercania.
     */
    public ArrayList<Pair<Integer, Integer>> getPill() {return this.pill;}

 /**
     * @return Devuelve las PowerPill en orden de cercania.
     */
    public ArrayList<Pair<Integer, Integer>> getPowerPill() {return this.powerPill;}
    /**
     * @return Devuelve la distancia a los fantasmas.
     */
    public Map<GHOST, Integer> getGhostDistance() {
        return this.ghostDistance;
    }
    /**
     * @return Devuelve si los fantasmas son comestibles.
     */
    public Map<GHOST, Boolean> getGhostEdible() {
        return this.ghostEdible;
    }
    /**
     * @return Devuelve si los indices de los fantasmas.
     */
    public Map<GHOST, Integer> getGhostID() {
        return this.ghostId;
    }
    /**
     * @return numero actual de vidas
     */
    public int getNLives() {return this.nLives;}
    /**
     * @return numero actual de nivel
     */
    public int getLevel() {return this.level;}
    private void bfs(int msPacman){
        Queue<Integer> toVisit  = new LinkedList<Integer>();
        toVisit.add(msPacman);
        ArrayList<Integer> visited  = new ArrayList<Integer>(this.game.getNumberOfNodes());

        while(!toVisit.isEmpty()){
            // Guardamos el nodo.
            int current = toVisit.poll();
            // Marcar como visitado.
            visited.add(current);

            // Guardamos informacion.
            if (isActivePill(current) && !this.pill.contains(current)) {
                int distance = game.getShortestPathDistance(msPacman,current);
                Pair<Integer,Integer> p = new Pair<>(current, distance);
                this.pill.add(p);
                continue;
            }
            // Explora vecinos.
            for (Integer i : this.game.getNeighbouringNodes(current)){
                if (visited.contains(i) || i == -1 ) continue;
                toVisit.add(i);
            }
        }
    }

    // Devuelve true si en la id indicada hay una pill.
    private boolean isActivePill(int id) {
        int[] pillIndices = game.getCurrentMaze().pillIndices;
        for (int i = 0; i < pillIndices.length; i++) {
            if (pillIndices[i] == id) {
                return game.isPillStillAvailable(i);
            }
        }
        return false;
    }
}
