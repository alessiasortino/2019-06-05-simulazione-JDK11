package it.polito.tdp.crimes.model;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

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
	
	public void init(Integer N, Integer anno, Integer mese, Integer giorno,
			Graph<Integer,DefaultWeightedEdge> graph) {
		this.N=N;
		this.anno=anno;
		this.mese=mese;
		this.giorno=giorno;
		this.graph=graph;
		
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
	
	public int run() {
		Evento e;
		
		while ((e=queue.poll())!=null) {
			switch(e.getType()) {
			
			case CRIMINE:
				System.out.println("NUOVO CRIMINE!"+ e.getCrimine().getIncident_id());
				
				//cerco l'agnete libero più vicino
				Integer partenza= null;
				partenza = cercaAgente(e.getCrimine().getDistrict_id());
				
				if(partenza!=null) {
					
					//c'è un agente libero in partenza
					agenti.put(partenza, agenti.get(partenza)-1);
					
					// cerco di capire il tempo che impiegherà a raggiungere il distretto
					Double distanza ;
					Integer arrivo= e.getCrimine().getDistrict_id();
					
					if(partenza.equals(arrivo))
						distanza = 0.0;
					
					else
						//la distanza sarà il peso dell'arco
						distanza= this.graph.getEdgeWeight(graph.getEdge(partenza,arrivo));
					
					Long seconds = (long) ((distanza*1000)/(60/3.6));
					
					this.queue.add(new Evento(TipoEvento.ARRIVA_AGENTE,e.getData().plusSeconds(seconds), e.getCrimine()));
					
					
				}else {
					//non c'è nessun agente libero al momento--> crimine mal gestito
					this.malGestiti++;
					System.out.println("CRIMINE "+ e.getCrimine().getIncident_id()+" MALGESTITO!\n");
				}
				
				
				
				
				
				break;
			case  ARRIVA_AGENTE:
				
				System.out.println("ARRIVA AGENTE PER CRIMINE!"+ e.getCrimine().getIncident_id());
				Long duration= getDurata(e.getCrimine().getOffense_category_id());
				this.queue.add((new Evento(TipoEvento.GESTITO,e.getData().plusSeconds(duration), e.getCrimine())));
				
				
				if(e.getData().isAfter(e.getCrimine().getReported_date().plusMinutes(15))) {
					System.out.println("CRIMINE MAL GESTITO "+e.getCrimine().getIncident_id());
					this.malGestiti++;
				}
					
				break;
			case GESTITO:
				agenti.put(e.getCrimine().getDistrict_id(), agenti.get(e.getCrimine().getDistrict_id())+1 );
			
				
				break;
			}
		}
		
			
			return this.malGestiti;
	}

	private Long getDurata(String offense_category_id) {
		
		if(offense_category_id.equals("all_other_crimes")) {
			Random r= new Random();
			if(r.nextDouble()>0.5)
				return Long.valueOf((2*60*60));
				else
					return Long.valueOf(60*60);
		}else {
					return Long.valueOf(2*60*60);
		}
	
	}

	private Integer cercaAgente(Integer district_id) {
		Double distanza= Double.MAX_VALUE;
		Integer distretto=null;
		
		for (Integer a: this.agenti.keySet()) {
			if(this.agenti.get(a)>0) {
				if(district_id.equals(a)) {
					distanza=0.0;
					distretto=a;
					
			}else {
				
			DefaultWeightedEdge arco=this.graph.getEdge(district_id, a);
				if(this.graph.getEdgeWeight(arco)<distanza) {
			
					distanza= this.graph.getEdgeWeight(graph.getEdge(district_id,a));
					distretto= a;
					
				}
			}
		}
		}
		return distretto;
	
	
	}

}
