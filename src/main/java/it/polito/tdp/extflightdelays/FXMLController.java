package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="compagnieMinimo"
    private TextField compagnieMinimo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoDestinazione"
    private ComboBox<Airport> cmbBoxAeroportoDestinazione; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessione"
    private Button btnConnessione; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	txtResult.clear();
    	
    	//lettura del parametro
    	int n = 0;
    	try {
    		n = Integer.parseInt(this.compagnieMinimo.getText());
    	}
    	catch(NumberFormatException e) {
    		this.txtResult.setText("inserire un numero");
    	}
    	
    	//creazione del grafo
    	this.model.creaGrafo(n);
    	
    	// Avendo creato il grafo, possiamo popolare le tendine
    	// ATTENZIONE a rimuovere quello che gia' e' presente nella tendina
    	// perché se ricreiamo il grafo e aggiungiamo solo i nuovi vertici
    	// alla tendina, i vecchi vertici potrebbero restare presenti!
    	cmbBoxAeroportoPartenza.getItems().clear();
    	cmbBoxAeroportoPartenza.getItems().addAll(this.model.getVertici());
    	cmbBoxAeroportoDestinazione.getItems().clear();
    	cmbBoxAeroportoDestinazione.getItems().addAll(this.model.getVertici());
    	
    }

    @FXML
    void doTestConnessione(ActionEvent event) {
     	txtResult.clear();
    	//Selezione aeroporti
    	Airport origine = cmbBoxAeroportoPartenza.getValue();
    	Airport destinazione = cmbBoxAeroportoDestinazione.getValue();
    	if(origine == null || destinazione == null) {
    		txtResult.appendText("Seleziona i due aeroporti!");
    		return ;
    	}
    	
    	//Check che i due aeroporti siano connessi. Possiamo usare il metodo
    	// this.model.esistePercorso(origin, destination)
    	// che sfrutta il ConnectivityInspector
    	// oppure possiamo usare 
    	// this.model.trovaPercorso(origine, destinazione)
    	// e controllare che non ritorni null.
    	List<Airport> percorso = this.model.trovaPercorso(origine, destinazione);  	
    	if(percorso == null) {
    		txtResult.appendText("I due aeroporti non sono collegati");
    	} else {
    		txtResult.appendText(percorso.toString());
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert compagnieMinimo != null : "fx:id=\"compagnieMinimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbBoxAeroportoDestinazione != null : "fx:id=\"cmbBoxAeroportoDestinazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessione != null : "fx:id=\"btnConnessione\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    public void setModel(Model model) {
    	this.model = model;
    }
}