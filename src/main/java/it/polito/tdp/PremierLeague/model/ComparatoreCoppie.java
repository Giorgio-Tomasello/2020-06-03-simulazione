package it.polito.tdp.PremierLeague.model;

import java.util.Comparator;

public class ComparatoreCoppie implements Comparator<Player> {
	
	public int compare(Player c1, Player c2) {
			
		if(c1.getPeso()>c2.getPeso())
			return 1;
		if(c1.getPeso()<c2.getPeso())
			return -1;
		return 0;
		
	}

}
