import es.ucm.fdi.ici.c2526.practica1.grupoL.Ghosts;
import es.ucm.fdi.ici.c2526.practica1.grupoL.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
//import pacman.controllers.HumanController;
//import pacman.controllers.KeyBoardInput;
import pacman.controllers.PacmanController;

public class ExecutorTestEvaluate {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();

        //PacmanController pacMan = new pacman.controllers.HumanController.HumanController(new pacman.controllers.HumanController.KeyBoardInput());
        PacmanController pacMan = new MsPacMan();
        GhostController ghosts = new Ghosts();
        
        System.out.println( 
            executor.runGame(pacMan, ghosts, 30) //last parameter defines speed
        );     
    }
	
}
