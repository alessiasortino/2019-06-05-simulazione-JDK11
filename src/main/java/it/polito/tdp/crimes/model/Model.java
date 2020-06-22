package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	Graph <Integer, DefaultWeightedEdge> graph;
	List <Integer> anni;
	List <Integer> distretti;
	EventsDao dao;
	
	
	public Model() {
		anni= new ArrayList <>();
		distretti= new ArrayList<>();
		dao= new EventsDao();
		anni= dao.getYear();
	}
	
	public List<Integer> getAnni() {
		Collections.sort(anni);
		return anni;
	}

	public void setAnni(List<Integer> anni) {
		this.anni = anni;
	}

	public  void creaGrafo(Integer anno) {
		graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// vertici 
		distretti= dao.getDistrict();
		Graphs.addAllVertices(graph, distretti);
		
		for(Integer v1: this.graph.vertexSet()) {
			for(Integer v2: this.graph.vertexSet()) {
				if(!v1.equals(v2)) {
					if(this.graph.getEdge(v1, v2)==null) {
						
						
						Double latMediaV1= dao.getLatMedia(anno, v1);
						Double latMediaV2= dao.getLatMedia(anno,v2);
					
						Double longMediaV1= dao.getLongMedia(anno,v1);
						Double longMediaV2= dao.getLongMedia(anno,v2);
					
						Double distanzaMedia= LatLngTool.distance(new LatLng(latMediaV1,longMediaV1),
								new LatLng(latMediaV2,longMediaV2), LengthUnit.KILOMETER);
						
						Graphs.addEdgeWithVertices(this.graph, v1, v2, distanzaMedia);
					}
					
				}
			}
		}
		System.out.println("Grafo creato con "+ this.graph.vertexSet().size()+ " archi e "+ graph.edgeSet().size()+ " archi");
	}
	
	public List<Vicino> getVicini(Integer distretto){
		List <Vicino> vicini= new ArrayList<>();
		List< Integer> viciniId= Graphs.neighborListOf(graph, distretto);
		for(Integer v: viciniId) {
			vicini.add(new Vicino( v, this.graph.getEdgeWeight(this.graph.getEdge(distretto, v))));
		}
		Collections.sort(vicini);
		return vicini;
	}

	public Set <Integer> getVertici() {
		// TODO Auto-generated method stub
		return this.graph.vertexSet();
	}
}
