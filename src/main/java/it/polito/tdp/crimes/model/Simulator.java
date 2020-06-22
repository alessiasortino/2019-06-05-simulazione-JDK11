package it.polito.tdp.crimes.model;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.db.EventsDao;
import it.polito.tdp.crimes.model.Evento.TipoEvento;

public class Simulator {
	
	//Parametri di input 
	private Integer N;
	private Integer anno;
	private Integer mese;
	private Integer giorno;
	
	//Stato del sistema
	private Graph<Integer,DefaultWeightedEdge> graph;
	
	//coda degli eventi
		private PriorityQueue<Evento> queue;
	
	//Modello del mondo
	private Map <Integer,Integer> agenti;//mappa <distretto, num agenti liberi>
	
	//Output
	private int malGestiti;
	
	private void init(Integer N, Integer anno, Integer mese, Integer giorno,
			Graph<Integer,DefaultWeightedEdge> graph) {
		this.N=N;
		this.anno=anno;
		this.mese=mese;
		this.giorno=giorno;
		
		this.malGestiti=0;
		this.agenti= new HashMap<>();
		for(Integer d: graph.vertexSet()) {
			agenti.put(d,0);
		}
		// devo scegliere la centrale dove metto gli N agenti
		//distretto a minor criminalità nell'anno in corso
		EventsDao dao= new EventsDao();
		Integer minD= dao.getDistrettoMin(anno);
		
		this.agenti.put(minD,N);
		
		//creo e inizializzo la coda
		
		this.queue= new PriorityQueue<>();
		
		for(Event e: dao.listEventsBYDate(anno, mese, giorno)) {
			queue.add(new Evento( TipoEvento.CRIMINE, e.getReported_date(), e));
		}
		
		
	}
	
	

}
