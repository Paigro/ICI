import java.util.Iterator;

import es.ucm.fdi.ici.c2526.practica2.grupoYY.Ghosts;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();

        PacmanController pacMan = new MsPacMan();
        GhostController ghosts = new Ghosts();
        
        for (int i = 0; i < 1; i++) {
        	System.out.println( 
        			executor.runGame(pacMan, ghosts, 20) //last parameter defines speed
        			);     
		}
    }
	
}
