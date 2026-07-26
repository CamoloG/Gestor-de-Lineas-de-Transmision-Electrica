//controlador de eliminardatos.fxml
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FifthController 
{
    @FXML
    private TextField cogerid;
    
    SistemaElectrico mav= SistemaElectrico.getInstancia();
    long id;
    
    @FXML
    private void Cancelar() throws IOException 
    {
        App.setRoot("primary");
    }
    
    @FXML
    private void borrarlinea()throws IOException 
    {
        if(cogerid.getText().trim().isEmpty())
        {
            mostrarAlertaID();
            return;
        }
        else
        {
            id=Long.parseLong(this.cogerid.getText());
        }
        try 
        {
            boolean exito= mav.doEliminarLinea((long) id);
            if(exito)
            {
                mostrarConfirmacion();
                App.setRoot("primary");
            }
            else
            {
                mostrarAlerta();
            }
        }
        catch(NumberFormatException e)
        {
             System.err.println("Error al cargar la vista: " + e.getMessage());   
        }
    }
    
    private void mostrarAlerta()
    {
        Alert alert= new Alert(Alert.AlertType.ERROR);
               alert.setHeaderText(null);
               alert.setTitle("Error");
               alert.setContentText("ID inexistente o no encontrado.");
               alert.showAndWait();
    }
    private void mostrarAlertaID()
    {
        Alert alert= new Alert(Alert.AlertType.ERROR);
               alert.setHeaderText(null);
               alert.setTitle("Error");
               alert.setContentText("Digite su ID");
               alert.showAndWait();
    }
    private void mostrarConfirmacion()
    {
        Alert alerta= new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Terminado");
        alerta.setContentText("Linea eliminada exitosamente.");
        alerta.showAndWait();
    }
}
