package es.ucm.fdi.ici.c2526.practica2.grupoYY;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.data.time.Second;

import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions.*;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.*;
import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * The Class NearestPillPacMan.
 */
public class MsPacMan extends PacmanController {

	FSM fsm;
	public MsPacMan() {
		setName("MsPacMan XX");
		
    	fsm = new FSM("MsPacMan");
    	
    	GraphFSMObserver observer = new GraphFSMObserver(fsm.toString());
    	fsm.addObserver(observer);
    	
    	// transiciones entre estados simples principales
    	// a comer
    	Transition Cazar2Comer = new Cazar2Comer();
    	Transition Huir2Comer = new Huir2Comer(40,60);
    	// a cazar
		Transition Comer2Cazar = new Comer2Cazar(20);
		Transition Huir2Cazar = new Huir2Cazar(30, 20);
		// a huir
		Transition Comer2Huir = new Comer2Huir(30, 20);
		Transition Cazar2Huir = new Cazar2Huir(30, 20);
		// a start
		Transition Cazar2Start = new Cazar2Start();
		Transition Huir2Start = new Huir2Start();
    	
		// Estados compuestos
		// Estado compuesto comer:
    	FSM cfsmComer = new FSM("Comer");
    	GraphFSMObserver c1observer = new GraphFSMObserver(cfsmComer.toString());
    	cfsmComer.addObserver(c1observer);
    	
    	SimpleState cExplore = new SimpleState("Explore", new ExploreAction(30));
    	SimpleState cCatch = new SimpleState("CatchPills", new CatchPillsAction());
    	Transition cCatch2Explore = new CatchPills2Explore(20);
    	Transition cExplore2CatchPil = new Explore2CatchPills(20);
    	cfsmComer.add(cCatch, cCatch2Explore, cExplore);
    	cfsmComer.add(cExplore, cExplore2CatchPil, cCatch);
    	cfsmComer.ready(cExplore);
    	CompoundState compoundComer = new CompoundState("Comer", cfsmComer);
		
    	
		// Estado compuesto Cazar:
    	FSM cfsmCazar = new FSM("Cazar");
    	GraphFSMObserver c2observer = new GraphFSMObserver(cfsmCazar.toString());
    	cfsmCazar.addObserver(c2observer);
    	
    	SimpleState cCatchGhost = new SimpleState("CatchGhost", new CatchGhostAction());
    	SimpleState cCatchGhostGroup = new SimpleState("CatchGhostGroup", new CatchGhostGroupAction(5));
    	Transition cCatch2CatchGroup = new Cazar2CazarGrupo(5,2);
    	Transition cCatchGroup2Catch = new CazarGrupo2Cazar(5);
    	cfsmCazar.add(cCatchGhost, cCatch2CatchGroup, cCatchGhostGroup);
    	cfsmCazar.add(cCatchGhostGroup, cCatchGroup2Catch, cCatchGhost);
    	cfsmCazar.ready(cCatchGhost);
    	CompoundState compoundCazar = new CompoundState("Cazar", cfsmCazar);
    	
    	// Estado compuesto Huir:
    	FSM cfsmHuir = new FSM("Huir");
    	GraphFSMObserver c3observer = new GraphFSMObserver(cfsmHuir.toString());
    	cfsmHuir.addObserver(c3observer);
    	
    	SimpleState cRunAway = new SimpleState("RunAway", new RunAwayAction(30));
    	SimpleState cCatchPowerPill = new SimpleState("CatchPowerPill", new CatchPowerPillsAction());
    	Transition cRunAway2CatchPowerPills = new RunAway2CatchPowerPills(10);
    	Transition cCatchPowerPills2RunAway = new CatchPowerPills2RunAway(10);
    	cfsmHuir.add(cRunAway, cRunAway2CatchPowerPills, cCatchPowerPill);
    	cfsmHuir.add(cCatchPowerPill, cCatchPowerPills2RunAway, cRunAway);
    	cfsmHuir.ready(cRunAway);
    	CompoundState compoundHuir = new CompoundState("Huir", cfsmHuir);
		
		// Asignaciones de transiciones entre estados principales.
    	// Comer.
    	fsm.add(compoundComer, Comer2Cazar, compoundCazar);
    	fsm.add(compoundComer, Comer2Huir, compoundHuir);
    	// Cazar.
    	fsm.add(compoundCazar, Cazar2Start, compoundComer);
    	fsm.add(compoundCazar, Cazar2Comer, compoundComer);
    	fsm.add(compoundCazar, Cazar2Huir, compoundHuir);
    	// Huir.
    	fsm.add(compoundHuir, Huir2Start, compoundComer);
    	fsm.add(compoundHuir, Huir2Cazar, compoundCazar);
    	fsm.add(compoundHuir, Huir2Comer, compoundComer);
    	// Estado inicial
    	fsm.ready(compoundComer);
    	
    	
    	JFrame frame = new JFrame();
    	JPanel main = new JPanel();
    	main.setLayout(new BorderLayout());
    	main.add(observer.getAsPanel(true, null), BorderLayout.CENTER);
    	main.add(c3observer.getAsPanel(true, null), BorderLayout.SOUTH);
    	frame.getContentPane().add(main);
    	frame.pack();
    	frame.setVisible(true);
	}
	
	
	public void preCompute(String opponent) {
    		fsm.reset();
    }
	
	
	
    /* (non-Javadoc)
     * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
     */
    @Override
    public MOVE getMove(Game game, long timeDue) {
    	Input in = new MsPacManInput(game); 
    	return fsm.run(in);
    }
    
    
}