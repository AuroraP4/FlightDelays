package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private Graph<Airport,DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao;
	private Map<Integer, Airport> idMap;
	
	public Model() {
		this.grafo = new SimpleWeightedGraph(DefaultWeightedEdge.class);   //per resettare ogni volta che chiamamiamo FMXLController
		this.dao = new ExtFlightDelaysDAO();
		this.idMap = new HashMap<Integer, Airport>();
		this.dao.loadAllAirports(idMap);
	}
	
	public void creaGrafo( int nAirlines) {
		
		this.grafo = new SimpleWeightedGraph(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, this.dao.getVertici(nAirlines, idMap));
		
		List<Rotta> edges = dao.getRotte(idMap);
		
		for(Rotta r: edges) {
			Airport origin = r.getOrigin();
			Airport destination = r.getDestination();
			int N = r.getN();
			
			//check se questi aeroporti siano tra i vertici:
			if(grafo.vertexSet().contains(origin) && grafo.vertexSet().contains(destination)) {
				
				DefaultWeightedEdge edge = grafo.getEdge(origin, destination);
				if(edge !=null) {  // ci dice se l'edge esiste oppure no
					double weight = grafo.getEdgeWeight(edge);
					weight +=  N;
					grafo.setEdgeWeight(origin, destination, weight);
				} else {
					this.grafo.addEdge(origin, destination);
					grafo.setEdgeWeight(origin, destination, N);    }
				
			}
						
		}
		System.out.println("Grafo creato");
		System.out.println("Ci sono " + grafo.vertexSet().size() + " vertici");
		System.out.println("Ci sono " + grafo.edgeSet().size() + " edges");
	}
	

	/**
	 * Metodo getter che restituisce i vertici del grafo. Serve per popolare le tendine 
	 * nell'interfaccia grafica, una volta che il grafo é stato creato
	 * @return
	 */
	public List<Airport> getVertici(){
		List<Airport> vertici = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(vertici);
		return vertici;
	}
	

	/**
	 * Metodo per verificare se due aeroporti sono connessi nel grafo, e quindi se esiste un percorso tra i due
	 * @param origin
	 * @param destination
	 * @return
	 */
	public boolean esistePercorso(Airport origin, Airport destination) {
		ConnectivityInspector<Airport, DefaultWeightedEdge> inspect = new ConnectivityInspector<Airport, DefaultWeightedEdge>(this.grafo);
		Set<Airport> componenteConnessaOrigine = inspect.connectedSetOf(origin);
		return componenteConnessaOrigine.contains(destination);
	}
	
	
	
	/**
	 * Metodo che calcola il percorso tra due aeroporti. Se il percorso non viene trovato, 
	 * il metodo restituisce null.
	 * @param origin
	 * @param destination
	 * @return
	 */
	public List<Airport> trovaPercorso(Airport origin, Airport destination){
		List<Airport> percorso = new ArrayList<>();
	 	BreadthFirstIterator<Airport,DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.grafo, origin);
	 	Boolean trovato = false;
	 	
	 	//visito il grafo fino alla fine o fino a che non trovo la destinazione
	 	while(it.hasNext() & !trovato) {
	 		Airport visitato = it.next();
	 		if(visitato.equals(destination))
	 			trovato = true;
	 	}
	 
	 
	 	/* se ho trovato la destinazione, costruisco il percorso risalendo l'albero di visita in senso
	 	 * opposto, ovvero partendo dalla destinazione fino ad arrivare all'origine, ed aggiiungo gli aeroporti
	 	 * ad ogni step IN TESTA alla lista
	 	 * se non ho trovato la destinazione, restituisco null.
	 	 */
	 	if(trovato) {
	 		percorso.add(destination);
	 		Airport step = it.getParent(destination);
	 		while (!step.equals(origin)) {
	 			percorso.add(0,step);
	 			step = it.getParent(step);
	 		}
		 
		 percorso.add(0,origin);
		 return percorso;
	 	} else {
	 		return null;
	 	}
		
	}
	
	//public List<Airport> trovaPercorso(Airport origin, Airport destination) {
	// 1) Creare iteratore BreadthWidthtFirst it
	// 2) Con l'iteratore, visitare il grafo a partire  origin
	// 3)
	
	
	// step = it.getParent(destination);
	
	//while (step !=null) {
	// percorso.add(step)
	//step = it.getParent(step); }
	//}
}
