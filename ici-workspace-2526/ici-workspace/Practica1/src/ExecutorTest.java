import java.util.Iterator;

<<<<<<< Updated upstream
import es.ucm.fdi.ici.c2526.practica1.grupoYY.Ghosts;
import es.ucm.fdi.ici.c2526.practica1.grupoYY.MsPacMan;
=======
import es.ucm.fdi.ici.c2526.practica1.grupoL.Ghosts;
import es.ucm.fdi.ici.c2526.practica1.grupoL.MsPacMan;
import es.ucm.fdi.ici.c2526.practica1.grupoL.MsPacMan2;
>>>>>>> Stashed changes
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

        PacmanController pacMan = new MsPacMan2();
        GhostController ghosts = new Ghosts();
        
        long points = 0;
        int n = 100;
        int speed = 1;
        
        for (int i = 0; i < n; i++) 
        	points += executor.runGame(pacMan, ghosts, speed); //last parameter defines speed
        	  
        System.out.println(" MEDIA DE PUNTOS EN "+n+" PARTIDAS: " + points/n); 
    }
	
}
