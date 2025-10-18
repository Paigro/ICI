package es.ucm.fdi.ici.c2526.practica2.grupoYY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GraphCost {
	public float[] costVertices;
	public float[] currentCostVertices;
	private float pillsCost = 1.0f;
	private float powerPillCost = 0.5f;
	private float defaultCost = 2.0f;
	private Game game;
	public interface Heuristic {
        double estimate(int aNode, int bNode);
    }
	public GraphCost() {
	}
	public GraphCost(Game game) { //para pacman
		this.game = game;
		costVertices = new float[game.getNumberOfNodes()];
		currentCostVertices = new float[game.getNumberOfNodes()];
		Arrays.fill(costVertices, 2.0f);
		Arrays.fill(currentCostVertices, 2.0f);
		for(int i = 0; i < game.getPillIndices().length; i++) {
			costVertices[game.getPillIndices()[i]] = pillsCost;
			currentCostVertices[game.getPillIndices()[i]] = pillsCost;
		}
		for(int i = 0; i < game.getPowerPillIndices().length; i++) {
			costVertices[game.getPowerPillIndices()[i]] = defaultCost;
			currentCostVertices[game.getPowerPillIndices()[i]] = defaultCost;
		}
	}
	public GraphCost(Game game, float defaultcost) { // para fantasmas
		this.defaultCost = defaultcost;
		this.game = game;
		costVertices = new float[game.getNumberOfNodes()]; // es el que se va actualizando
		currentCostVertices = new float[game.getNumberOfNodes()]; //guarda el estado base del grafo para actualizaciones
		setAllCosts(defaultCost);
	}
	
	
	public void init(Game game,float defaultcost) { // para fantasmas
		this.defaultCost = defaultcost;
		this.game = game;
		costVertices = new float[game.getNumberOfNodes()];
		currentCostVertices = new float[game.getNumberOfNodes()]; 
		setAllCosts(defaultCost);
	}
	
	public void setAllCosts(float defaultCost) {
		Arrays.fill(costVertices, defaultCost);
		Arrays.fill(currentCostVertices, defaultCost);
	}
	public void InitialicePillsCost() {
		for(int i = 0; i < game.getPillIndices().length; i++) {
			costVertices[game.getPillIndices()[i]] = pillsCost;
		}
	}
	public float[] GetNeighboursCosts(Game game, int v) {
		int[] vecinos = game.getNeighbouringNodes(v);
		float[] costsV = new float[vecinos.length];
		for(int i = 0; i < vecinos.length; i++) {
			costsV[i] = costVertices[vecinos[i]];
		}
		return costsV;
	}
	public void SetVertexCost(int node, float cost) {
		costVertices[node] = cost;
	}
	public void resetCosts() {
		System.arraycopy(currentCostVertices, 0, costVertices, 0, game.getNumberOfNodes());
	}
	/**
	 * Actualiza el coste de una unica posicion.
	 *
	 * @param pos		  posicion donde se realiza la actualizacion de coste
	 * @param multiplier  factor global de penalizacion.
	 */
	public void UpdateVertexCost(Game game, int pos, float mult) {
		costVertices[pos] = pillsCost * mult;
		currentCostVertices[pos] = pillsCost * mult;
	}
	/**
	 * Actualiza el coste de los nodos dentro de un rango.
	 *
	 * @param centerNode		 posicion donde se realiza la actualizacion de coste
	 * @param multiplier  factor global de penalizacion.
	 * @param radius  radio de penalizacion
	 * @param uniform todo los nodos del rango tienen el mismo coste
	 */
	public void updateVertexCostRange(Game game, int centerNode, float multiplier, int radius, boolean uniform) {
	    if (centerNode < 0 || centerNode >= game.getNumberOfNodes()) return;
	    if (radius < 0) radius = 0;
	    if (multiplier <= 0f) multiplier = 1f;
	    
	    int[] dist = new int[game.getNumberOfNodes()];
	    for (int i = 0; i < game.getNumberOfNodes(); i++) dist[i] = -1;

	    Queue<Integer> q = new LinkedList<>();
	    q.add(centerNode);
	    dist[centerNode] = 0;
	    while (!q.isEmpty()) {
	        int cur = q.poll();
	        int d = dist[cur];
	        if (d >= radius) continue;

	        int[] neigh = game.getNeighbouringNodes(cur);
	        if (neigh == null) continue;

	        for (int nb : neigh) {
	            if (nb < 0 || nb >= game.getNumberOfNodes()) continue;
	            if (dist[nb] != -1) continue;
	            dist[nb] = d + 1;
	            q.add(nb);
	        }
	    }
	    for (int node = 0; node < game.getNumberOfNodes(); node++) {
	        if (dist[node] == -1 || dist[node] > radius) continue;
	        if (uniform) {
	            costVertices[node] = pillsCost* multiplier;
	        } else {
	            double t = (double)dist[node] / (double)Math.max(radius, 1);
	            double factor = 1.0 + (multiplier - 1.0) * (1.0 - t);
	            costVertices[node] *= factor;
	        }
	    }
	}
	/**
	 * Penaliza los nodos que aparecen en la ruta `path`.
	 *
	 * @param path      ruta (lista de node indices del Game). Si es null o vacia no hace nada.
	 * @param multiplier  factor global de penalizacion.
	 * @param falloff   si true aplica una caida linear desde el inicio de la ruta
	 *                  hasta el final. Si false aplica el mismo factor a todos los nodos.
	 */
	public void penalizePath(ArrayList<Integer> path, float multiplier, boolean falloff) {
	    if (path == null || path.isEmpty()) return;
	    if (multiplier <= 1.0f && !falloff) return; // nada que hacer si multiplier no aumenta nada

	    int len = path.size();
	    // si solo hay un nodo, aplicamos el multiplicador directamente
	    if (len == 1) {
	        int node = path.get(0);
	        if (node >= 0 && node < costVertices.length) {
	            costVertices[node] = (float)(costVertices[node] * multiplier);
	        }
	        return;
	    }

	    for (int idx = 0; idx < len; idx++) {
	        int node = path.get(idx);
	        if (node < 0 || node >= costVertices.length) continue;

	        float factor;
	        if (!falloff) {
	            factor = multiplier;
	        } else {
	            double t = (double) idx / (double) (len - 1); // falloff linear: en idx=0 factor=multiplier ; en idx=len-1 factor=1.0
	            factor = (float) (1.0 + (multiplier - 1.0) * (1.0 - t));
	        }
	        costVertices[node] = (float) (defaultCost * factor);
	    }
	}
	private boolean isEdibleGhostAtNode(Game game, int nodeIndex) {
	    for (GHOST g : GHOST.values()) {
	        int ghostNode = game.getGhostCurrentNodeIndex(g);
	        if (ghostNode == nodeIndex && game.isGhostEdible(g)) {
	            return true;
	        }
	    }
	    return false;
	}
	public ArrayList<Integer> GetPathBFS(Game game, int startNode){
		if(startNode == -1) {
			return new ArrayList<>();
		}
		double[] costSoFar = new double[game.getCurrentMaze().graph.length]; 
		 PriorityQueue<Integer> openSet = new PriorityQueue<>((a, b) -> Double.compare(costSoFar[a], costSoFar[b]));
		
		 boolean[] visited = new boolean[game.getCurrentMaze().graph.length];
		int[] prevList = new int[game.getCurrentMaze().graph.length];
		
		
		for (int i = 0; i < game.getCurrentMaze().graph.length; i++)
        {
            visited[i] = false;
            prevList[i] = -1;
            costSoFar[i] = Double.POSITIVE_INFINITY;
        }
		
		openSet.add(startNode);
		visited[startNode] = true;
		costSoFar[startNode] = 0;
		
		int excludedNode = -1;
		MOVE lastMove = game.getPacmanLastMoveMade();
		excludedNode = game.getNeighbour(startNode, lastMove.opposite());
		while(openSet.size() > 0) {
			int current = openSet.poll();
			int nodePill = game.getPillIndex(current);
			int nodePowePill = game.getPowerPillIndex(current);
			//ruta a la pildora / o la pildora de poder / o al fantasma comible
			if ((nodePill != -1 && game.isPillStillAvailable(nodePill)) ||(nodePowePill != -1 && game.isPillStillAvailable(nodePowePill))   || isEdibleGhostAtNode(game, current)) {
				 return BuildPath(game,startNode, current, prevList);
			}
			int[] vecinos = game.getNeighbouringNodes(game.getCurrentMaze().graph[current].nodeIndex);
			float[] costes = GetNeighboursCosts(game, game.getCurrentMaze().graph[current].nodeIndex);
			
			for (int j = 0; j < vecinos.length; j++) {
				 int vecinoID = vecinos[j];
				 if(visited[vecinoID]) {
					 continue;
				 }
				 if (current == startNode && vecinoID == excludedNode) {
		                continue;
		            }
				 double newCost = costSoFar[current] + costes[j];
				 if (newCost < costSoFar[vecinoID]) {
					 costSoFar[vecinoID] = newCost;
					 visited[vecinoID] = true;
					 openSet.add(vecinoID);
					 prevList[vecinoID] = current;
		         }
				 
			 }
		}
		
		return new ArrayList<>();
				
	}
	public ArrayList<Integer> GetPathAStar(Game game, int startNode, int goalNode) {
	    ArrayList<Integer> path = new ArrayList<>();

	    if (startNode == -1 || goalNode == -1 || startNode == goalNode) {
	        return path;
	    }

	    int n = game.getNumberOfNodes();
	    double[] gScore = new double[n];
	    double[] fScore = new double[n];
	    int[] prev = new int[n];
	    boolean[] closedSet = new boolean[n];

	    Arrays.fill(gScore, Double.POSITIVE_INFINITY);
	    Arrays.fill(fScore, Double.POSITIVE_INFINITY);
	    Arrays.fill(prev, -1);

	    // Heurística: estimación euclídea rápida (no depende de costes)
	    java.util.function.BiFunction<Integer,Integer,Double> heuristic = 
	        (a, b) -> {
	            int ax = game.getNodeXCood(a);
	            int ay = game.getNodeYCood(a);
	            int bx = game.getNodeXCood(b);
	            int by = game.getNodeYCood(b);
	            return Math.sqrt((ax-bx)*(ax-bx) + (ay-by)*(ay-by));
	        };

	    gScore[startNode] = 0.0;
	    fScore[startNode] = heuristic.apply(startNode, goalNode);

	    PriorityQueue<Integer> openSet = new PriorityQueue<>(
	        (a, b) -> Double.compare(fScore[a], fScore[b])
	    );
	    openSet.add(startNode);

	    while (!openSet.isEmpty()) {
	        int current = openSet.poll();

	        if (current == goalNode) {
	            // reconstruir path
	            LinkedList<Integer> revPath = new LinkedList<>();
	            int node = goalNode;
	            while (node != -1) {
	                revPath.addFirst(node);
	                node = prev[node];
	            }
	            path.addAll(revPath);
	            return path;
	        }

	        closedSet[current] = true;

	        int[] vecinos = game.getNeighbouringNodes(current);

	        for (int vecino : vecinos) {
	            if (closedSet[vecino]) continue;

	            double tentative_g = gScore[current] + costVertices[vecino];

	            if (tentative_g < gScore[vecino]) {
	                prev[vecino] = current;
	                gScore[vecino] = tentative_g;
	                fScore[vecino] = tentative_g + heuristic.apply(vecino, goalNode);

	                if (!openSet.contains(vecino)) {
	                    openSet.add(vecino);
	                }
	            }
	        }
	    }
	    return path;
	}
	//BuildPath
	private ArrayList<Integer> BuildPath(Game game,int srcId, int dstId, int[] prevList){
		ArrayList<Integer> path =  new ArrayList<>();
		if (dstId < 0 || dstId >= game.getCurrentMaze().graph.length) 
            return path;
		int prev = dstId;
		if(srcId == dstId) {
			 path.addFirst(game.getCurrentMaze().graph[prev].nodeIndex);
			 return path;
		}
        do
        {
            path.addFirst(game.getCurrentMaze().graph[prev].nodeIndex);
            prev = prevList[prev];
        } while (prev != srcId);
        return path;
	}
}
