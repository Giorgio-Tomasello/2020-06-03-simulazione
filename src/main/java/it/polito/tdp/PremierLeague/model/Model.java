package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	ArrayList<Player> listaPlayers;
	ArrayList<Coppia> listaCoppie;
	HashMap<Integer, Player> idMap ;
	
	public Model() {
		
		dao = new PremierLeagueDAO();
		
	}
	
	public String creaGrafo(double media) {
		
		this.idMap = new HashMap<Integer, Player>(dao.listPlayersMediaGol(media));
		this.listaCoppie = new ArrayList<Coppia>(dao.listArchi(media, idMap));
		
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		for(Coppia c : listaCoppie)
			{if(c.getPeso()>0)
				{Graphs.addEdge(this.grafo, c.getP1(), c.getP2(), c.getPeso());

				}
			else {
				Graphs.addEdge(this.grafo, c.getP2(), c.getP1(), -(c.getPeso()));

			}	
			}
		
		
		
		String output = "GRAFO CREATO" + "\n" + "Numero vertici: " + this.grafo.vertexSet().size() + 
				"\nNumero Archi: " + this.grafo.edgeSet().size();

		return output;
	}
	
	
	public String topPlayer() {
		
		//listaPlayers = new ArrayList<Player>(this.grafo.vertexSet());
		//Collections.sort(listaPlayers, new ComparatoreCoppie());
		int max = 0;
		for(Player p : this.grafo.vertexSet())
			{int n = this.grafo.outDegreeOf(p);
		
			if(n > max) {max = n;}
			
			}
		
		for(Player p : this.grafo.vertexSet())
		{int n = this.grafo.outDegreeOf(p);
	
			if (n == max) {
				this.grafo.outgoingEdgesOf(p);
			}
		}
		
			return null;
			
			
			
	}

}
