package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts;

import es.ucm.fdi.ici.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class GhostsInput extends Input {

	private boolean BLINKYedible;
	private boolean INKYedible;
	private boolean PINKYedible;
	private boolean SUEedible;
	private double minPacmanDistancePPill;
	
	public GhostsInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		this.BLINKYedible = game.isGhostEdible(GHOST.BLINKY);
		this.INKYedible = game.isGhostEdible(GHOST.INKY);
		this.PINKYedible = game.isGhostEdible(GHOST.PINKY);
		this.SUEedible = game.isGhostEdible(GHOST.SUE);
	
		int pacman = game.getPacmanCurrentNodeIndex();
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		for(int ppill: game.getPowerPillIndices()) {
			double distance = game.getDistance(pacman, ppill, DM.PATH);
			this.minPacmanDistancePPill = Math.min(distance, this.minPacmanDistancePPill);
		}
		
	}

	public boolean isBLINKYedible() {
		return BLINKYedible;
	}

	public boolean isINKYedible() {
		return INKYedible;
	}

	public boolean isPINKYedible() {
		return PINKYedible;
	}

	public boolean isSUEedible() {
		return SUEedible;
	}

	public double getMinPacmanDistancePPill() {
		return minPacmanDistancePPill;
	}

	public boolean isGhostInLair(GHOST ghost) {
	    return game.getGhostLairTime(ghost) > 0;
	}
	
	public boolean moreGhostEdible() {
		return BLINKYedible || INKYedible || PINKYedible || SUEedible;
	}
	public double getDistGhostToPacman(GHOST ghost) {
		int pacman = game.getPacmanCurrentNodeIndex();
		int ghostNode = game.getGhostCurrentNodeIndex(ghost);
		double dist = game.getShortestPathDistance(ghostNode, pacman);
		return dist;
	}
	public Entry<GHOST, Double> getDistToEdibleGhost(GHOST ghost) {
		double minDist = Double.MAX_VALUE;
		GHOST nearest = null;
		for (GHOST gType : GHOST.values()) {
			if(game.isGhostEdible(gType) && gType != ghost) {
				double aux = game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(gType), DM.PATH);
				minDist = Math.min(aux, minDist);
				if(minDist == aux) {
					nearest = gType;
				}
			}
		}
		return new SimpleEntry<>(nearest, minDist);
	}
	
}
