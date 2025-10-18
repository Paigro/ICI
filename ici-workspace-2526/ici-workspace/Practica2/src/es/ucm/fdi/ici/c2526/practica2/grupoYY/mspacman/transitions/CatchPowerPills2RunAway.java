package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import org.mindswap.pellet.utils.Pair;

import java.util.Iterator;
import java.util.Map;

public class CatchPowerPills2RunAway implements Transition {
    int limit;
    private int foundLimit;
    /**
     * @param limit Limite maximo que puede estar una pill.
     */
	public CatchPowerPills2RunAway(int limit) {
        this.limit = limit;
    }

	@Override
	public boolean evaluate(Input in) {
        MsPacManInput m = (MsPacManInput) in;
        // Si las PowerPill estan fuera del limite huye.
        for (Pair<Integer, Integer> p : m.getPowerPill()) {
			if(p.second < limit)
				return false;
			
		}
        return true;
	}

	@Override
	public String toString() {
		return String.format("Not found PowerPill RunAway");
	}
}
