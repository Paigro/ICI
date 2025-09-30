import es.ucm.fdi.ici.c2425.practica0.grupoIndividual.GhostsRandom;
import es.ucm.fdi.ici.c2425.practica0.grupoIndividual.MsPacManRandom;
import es.ucm.fdi.ici.c2425.practica0.grupoIndividual.GhostsAggresive;
import es.ucm.fdi.ici.c2425.practica0.grupoIndividual.MsPacManRunAway;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.PacmanController;


import es.ucm.fdi.ici.c2526.practica0.IglesiasRodrigo.*;


public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();

        //PacmanController pacMan = new HumanController(new KeyBoardInput()); // Pacman humano
        //PacmanController pacMan = new MsPacManRandom(); // Pacman normal.
        PacmanController pacMan = new IglesiasRodrigoMsPacman(); // Pacman Paigro.
        //GhostController ghosts = new GhostsRandom(); // Fantasmas random.
        //GhostController ghosts = new GhostsAggresive(); // Fantasmas agresivos.
        GhostController ghosts = new IglesiasRodrigoGhosts(); // Fantasmas Paigro.

        System.out.println( 
            executor.runGame(pacMan, ghosts, 40) // Lo ultimo es la velocidad. Menos mas rapido. Estandar: 30.
        );     
    }
	
}
