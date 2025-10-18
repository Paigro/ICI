package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import org.mindswap.pellet.utils.Pair;

import java.util.Map;

public class CatchPills2Explore implements Transition {
    int limit;
    private int foundLimit;
    /**
     * @param limit Limite maximo que puede estar una pill.
     */
	public CatchPills2Explore(int limit) {
        this.limit = limit;
        this.foundLimit = -1;
    }

	@Override
	public boolean evaluate(Input in) {
        MsPacManInput m = (MsPacManInput) in;
        // Si la pill esta dentro del limite va a por ella
        return m.getPill().get(0).second > limit;
	}

	@Override
	public String toString() {
		return String.format("Found Pill limit");
	}
}
