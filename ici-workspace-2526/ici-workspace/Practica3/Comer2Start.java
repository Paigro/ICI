package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import org.mindswap.pellet.utils.Pair;

import java.util.Map;

public class Comer2Start implements Transition {

    private Integer prevLives = null;
    private Integer prevLevel = null;

    public Comer2Start() {}

    @Override
    public boolean evaluate(Input in) {
    	MsPacManInput m = (MsPacManInput) in;
    	int curLives = m.getNLives();
    	int curLevel = m.getLevel();
    	if (this.prevLives == null) this.prevLives = curLives;
    	if (this.prevLevel == null) this.prevLevel = curLevel;
    	
    	boolean ToStart = (this.prevLives != null && curLives < this.prevLives) ||
    			(this.prevLevel != null && curLevel != this.prevLevel) ;
    	
    	 this.prevLives = curLives;
    	 this.prevLevel = curLevel;

        return ToStart;
    }

    @Override
    public String toString() {
        return "Cazar2Start";
    }
}