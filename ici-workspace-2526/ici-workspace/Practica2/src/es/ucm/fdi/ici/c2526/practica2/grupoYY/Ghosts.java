package es.ucm.fdi.ici.c2526.practica2.grupoYY;

import java.awt.Dimension;
import java.util.EnumMap;

import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.ChaseAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.GhostWaitAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.GoToLastActivePowerPill;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.InterceptAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.ProtectEdibleGhost;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.RandomMovementAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.RunAwayAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.EdiblesGhostsNear;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostReadyToExitTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsEdibleTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsNotEdibleAndPacManFarPPill;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.MoreEdibleGhost;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.OtherGhostIsNoLongerEdible;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.PacManNear;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.PacManNearPPillTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.PacManNextIntersectionNearTransition;
import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	EnumMap<GHOST,FSM> fsms;
	public GraphCost ghostGraph; // grafo para los fantasmas normales ( pesar fantasmas normales y PPils)
	private float wDefault = 5.0f ;
	private float wGhost = 500.0f;
	private float wEdibleGhost = 1.0f;
	private float limit = 50.0f;
	private float wPPills = 200.0f;
	private float wPacMan = 50.0f;
	
	private int lastLevel = 0;
	public Ghosts()
	{
		setName("Ghosts XX");
		if (ghostGraph == null) {
        	ghostGraph = new GraphCost();   // recrea el grafo
        }

		fsms = new EnumMap<GHOST,FSM>(GHOST.class);
		for(GHOST ghost: GHOST.values()) {
			FSM fsm = new FSM(ghost.name());
			
			//fsm.addObserver(new ConsoleFSMObserver(ghost.name()));
			GraphFSMObserver graphObserver = new GraphFSMObserver(ghost.name());
			fsm.addObserver(graphObserver);
			//-------------------------Nuestros Estados-------------------------
			SimpleState waitToExit = new SimpleState(new GhostWaitAction(ghost));
			CompoundState patrol = new CompoundState(ghost + "-PatrolCompound", buildPatroFSM(ghost));
			SimpleState protect = new SimpleState(new ProtectEdibleGhost(ghost, ghostGraph));
			//------------------------------------------------------------------
			
			SimpleState chase = new SimpleState(new ChaseAction(ghost, ghostGraph));
			SimpleState runAway = new SimpleState(new RunAwayAction(ghost));
			
			//-------------------------Nuestras Transiciones-------------------------
			GhostReadyToExitTransition readyToExit = new GhostReadyToExitTransition(ghost);
			PacManNear pacManNear = new PacManNear(ghost);
			EdiblesGhostsNear edibleGhostNear = new EdiblesGhostsNear(ghost);
			OtherGhostIsNoLongerEdible otherGhost = new OtherGhostIsNoLongerEdible(ghost);
			//-----------------------------------------------------------------------
			
			GhostsEdibleTransition edible = new GhostsEdibleTransition(ghost);
			PacManNearPPillTransition near = new PacManNearPPillTransition();
			GhostsNotEdibleAndPacManFarPPill toChaseTransition = 
					new GhostsNotEdibleAndPacManFarPPill(ghost);
			
			//--------------------Construccion Maquina estados----------------------
			fsm.add(waitToExit, readyToExit, patrol);
			fsm.add(patrol, edible, runAway);
			edible.num++;
			fsm.add(patrol, near, runAway);
			near.num++;
			fsm.add(patrol, pacManNear, chase);
			pacManNear.num++;
			fsm.add(patrol, edibleGhostNear, protect);
			fsm.add(runAway, toChaseTransition, patrol);
			fsm.add(chase, edible, runAway);
			edible.num++;
			fsm.add(chase, near, runAway);
			near.num++;
			fsm.add(protect, pacManNear, chase);
			fsm.add(protect, edible, runAway);
			fsm.add(protect, near, runAway);
			fsm.add(protect, otherGhost, patrol);
			
			//----------------------------------------------------------------------
			
			
			
			
			fsm.ready(waitToExit);
			
			graphObserver.showInFrame(new Dimension(300,200));
			
			fsms.put(ghost, fsm);
		}
	}
	private FSM buildPatroFSM(GHOST ghost) {
		FSM fsm = new FSM(ghost.name() + "Patrol");
		SimpleState randomMove = new SimpleState(new RandomMovementAction(ghost));
		SimpleState intercept = new SimpleState(new InterceptAction(ghost, ghostGraph));
		SimpleState goToActivePPill = new SimpleState(new GoToLastActivePowerPill(ghost,100,ghostGraph));
		
		PacManNextIntersectionNearTransition interNear = new PacManNextIntersectionNearTransition(ghost,18, 80,ghostGraph );
		MoreEdibleGhost moreEdibleGhost = new MoreEdibleGhost();
		
		fsm.add(randomMove,interNear , intercept);
		fsm.add(randomMove,moreEdibleGhost , goToActivePPill);
		fsm.ready(randomMove);
		return fsm;
	}
	private void SetGhostGraph(Game game) {
    	
		ghostGraph.setAllCosts(wDefault);
		for (GHOST ghost : GHOST.values()) { //setear los costes
    		int ghostNode = game.getGhostCurrentNodeIndex(ghost);
    		if (!game.isGhostEdible(ghost)) {
    			ghostGraph.SetVertexCost(ghostNode, wGhost);
    		}
    		else {
    			ghostGraph.SetVertexCost(ghostNode, wEdibleGhost);;
    		}
        }

		Integer PPill = ClosestPPillsToPacMan(game, limit);
		if (PPill != null) {
			ghostGraph.updateVertexCostRange(game, PPill, wPPills, 50, true);
		}
		

		MOVE lastMove = game.getPacmanLastMoveMade();
		int LastNodePacman = game.getNeighbour(game.getPacmanCurrentNodeIndex(), lastMove.opposite());
		ghostGraph.updateVertexCostRange(game, LastNodePacman, wPacMan, 5, true);
		
    }
	 private Integer ClosestPPillsToPacMan(Game game, float limit) { // Todas PPils que están dentro del limite
	    	Integer closestPill = null;
	    	for (int powerPill : game.getActivePowerPillsIndices()) {
				double aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), powerPill);
	    		if(aux < limit) {
	    			closestPill = powerPill;
	    		}
			}
	    	return closestPill;
	    }
	public void preCompute(String opponent) {
    	for(FSM fsm: fsms.values())
    		fsm.reset();
    }
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);
		int currentLevel = game.getCurrentLevel();
        if (ghostGraph == null || currentLevel != lastLevel) {
        	ghostGraph = new GraphCost(game, wDefault);   // recrea el grafo
            lastLevel = currentLevel;  // actualiza el nivel registrado
        }
        if(ghostGraph.costVertices == null) {
        	ghostGraph.init(game, wDefault);
        }
        SetGhostGraph(game);
		GhostsInput in = new GhostsInput(game);
		// ¿Cada fantasma deberia tener un grafo? Porque para los fantasmas normales los fantasmas malitos
		// deberian tener menos coste para asi salvarlos. 
		// Darle un vuelta para cambiarlo.
		for(GHOST ghost: GHOST.values())
		{
			if(game.isGhostEdible(ghost)) {
				int ghostNode = game.getGhostCurrentNodeIndex(ghost);
				ghostGraph.SetVertexCost(ghostNode, wGhost);
			}
			FSM fsm = fsms.get(ghost);
			MOVE move = fsm.run(in);
			result.put(ghost, move);
		}
		
		return result;
		
	
		
	}

}
