import java.util.Iterator;

import es.ucm.fdi.ici.c2526.practica1.grupoYY.Ghosts;
import es.ucm.fdi.ici.c2526.practica1.grupoYY.MsPacMan;
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
        
        long points = 0;
        
        for (int i = 0; i < 100; i++) 
        	points += executor.runGame(pacMan, ghosts, 1); //last parameter defines speed
        	  
        System.out.println(" MEDIA DE PUNTOS EN 100 PARTIDAS: " + points/100); 
    }
	
}
