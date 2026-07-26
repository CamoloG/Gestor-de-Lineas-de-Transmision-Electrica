package controlador;

import java.io.IOException;
import javafx.fxml.FXML;
import modelo.LineaTransmision;
import modelo.SistemaElectrico;
import modelo.Subestacion;
import java.io.*;
import static java.lang.Double.parseDouble;
import persistencia.Archivo;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.beans.property.SimpleStringProperty;

public class ThirdController 
{
    @FXML private TableView<LineaTransmision> tablaLineas;
    @FXML private TableColumn<LineaTransmision, Long> id;
    @FXML private TableColumn<LineaTransmision, String> colNombre;
    @FXML private TableColumn<LineaTransmision, String> colSubestaciones;
    @FXML private TableColumn<LineaTransmision, String> colRelacion;
    @FXML private TableColumn<LineaTransmision, String> colVoltajeNominal;
    @FXML private TableColumn<LineaTransmision, String> colCorrienteNominal;
    @FXML private TableColumn<LineaTransmision, String> colLongitudKM;
    @FXML private TableColumn<LineaTransmision, String> colDepartamento;
    @FXML private TableColumn<LineaTransmision, String> colMunicipio;
    @FXML private TableColumn<LineaTransmision, String> colLatitudes;
    @FXML private TableColumn<LineaTransmision, String> colLonguitudes;
    @FXML private TextField txtFiltroDepartamento;
    @FXML private TextField txtFiltroVoltajeMin;
    @FXML private TextField txtFiltroVoltajeMax;
    
    @FXML
    private void initialize()
    {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getNombre()
        ));

        colSubestaciones.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getSubestacion1().getNombre() + " / " + cellData.getValue().getSubestacion2().getNombre()
        ));

        colVoltajeNominal.setCellValueFactory(cellData -> new SimpleStringProperty(
            String.valueOf(cellData.getValue().getVoltajeNominal())
        ));

        colCorrienteNominal.setCellValueFactory(cellData -> new SimpleStringProperty(
            String.valueOf(cellData.getValue().getCorrienteNominal())
        ));

        colLongitudKM.setCellValueFactory(cellData -> new SimpleStringProperty(
            String.valueOf(cellData.getValue().getLongitudKm()) 
        ));
        
        colDepartamento.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getSubestacion1().getDepartamento() + " / " + cellData.getValue().getSubestacion2().getDepartamento()
        ));
        
        colMunicipio.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getSubestacion1().getMunicipio() + " / " + cellData.getValue().getSubestacion2().getMunicipio()
        ));
        
        colLatitudes.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getSubestacion1().getLatitud() + " / " + cellData.getValue().getSubestacion2().getLatitud()
        ));
        
        colLonguitudes.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getSubestacion1().getLongitud() + " / " + cellData.getValue().getSubestacion2().getLongitud()
        ));
        
        colRelacion.setCellValueFactory(cellData -> {
            boolean esGenerador1 = cellData.getValue().getSubestacion1().getEsGenerador();
            boolean esGenerador2 = cellData.getValue().getSubestacion2().getEsGenerador();
            
            // Si cualquiera de las dos es un generador, el tipo es "Generacion"
            // Si ninguna es generador (Subestacion - Subestacion), el tipo es "Transmision"
            String tipoRelacion = (esGenerador1 || esGenerador2) ? "Generacion" : "Transmision";
            
            return new SimpleStringProperty(tipoRelacion);
        });
        cargarDatosEnTabla();
    }
    
    private void cargarDatosEnTabla() {
        try 
        {

            ArrayList<LineaTransmision> lineasCargadas = SistemaElectrico.getInstancia().getLineasTransmision();

            ObservableList<LineaTransmision> datosObservable = FXCollections.observableArrayList(lineasCargadas);

            tablaLineas.setItems(datosObservable);
            
        } catch (Exception e) {
            System.out.println("Error al cargar datos en la tabla: " + e.getMessage());
        }
    }
    
    public void actualizarTablaConLista(ArrayList<LineaTransmision> listaNuevosDatos) 
    {

        ObservableList<LineaTransmision> datosObservable = FXCollections.observableArrayList(listaNuevosDatos);
        tablaLineas.setItems(datosObservable);
    }
    
    @FXML
    private void filtrarPorDepartamento() {
        String depto = txtFiltroDepartamento.getText();
        
        if (depto != null && !depto.trim().isEmpty()) {
            ArrayList<LineaTransmision> filtradas = SistemaElectrico.getInstancia().doFiltrarPorDepartamento(depto.trim());
            actualizarTablaConLista(filtradas);
            
            if (filtradas.isEmpty()) {
                mostrarAlertaInformacion("Sin resultados", "No se encontraron líneas de transmisión en el departamento: " + depto);
            }
        } 
        else 
        {
            cargarDatosEnTabla();
        }
    }

    @FXML
    private void filtrarPorVoltaje() {
        try {
            double vMin = Double.parseDouble(txtFiltroVoltajeMin.getText());
            double vMax = Double.parseDouble(txtFiltroVoltajeMax.getText());
            
            ArrayList<LineaTransmision> filtradas = SistemaElectrico.getInstancia().doFiltrarPorVoltaje(vMin, vMax);
            actualizarTablaConLista(filtradas);
            
            if (filtradas.isEmpty()) {
                mostrarAlertaInformacion("Sin resultados", "No se encontraron líneas en ese rango de voltaje.");
            }
            
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error de formato");
            alert.setContentText("Por favor, ingrese valores numéricos válidos (ej. 110.5) en los campos de voltaje.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void restaurarTabla() 
    {
        txtFiltroDepartamento.clear();
        txtFiltroVoltajeMin.clear();
        txtFiltroVoltajeMax.clear();

        cargarDatosEnTabla();
    }

    private void mostrarAlertaInformacion(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void Volver() throws IOException 
    {
        App.setRoot("primary");
    }
}