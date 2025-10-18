package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import org.mindswap.pellet.utils.Pair;

import java.util.Map;

public class RunAway2CatchPowerPills implements Transition {
    int limit;
    private int foundLimit;
    /**
     * @param limit Limite maximo que puede estar una pill.
     */
	public RunAway2CatchPowerPills(int limit) {
        this.limit = limit;
    }

	@Override
	public boolean evaluate(Input in) {
        MsPacManInput m = (MsPacManInput) in;
        // Si la pill esta dentro del limite va a por ella
        return m.getPowerPill().get(0).second < limit;
	}

	@Override
	public String toString() {
		return String.format("Not found PowerPill in limit");
	}
}
