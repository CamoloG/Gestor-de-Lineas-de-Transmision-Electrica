//controlador de creardatos.fxml
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SecondaryController 
{
    SistemaElectrico sisele = SistemaElectrico.getInstancia();
    LineaTransmision nuevaLinea;
    @FXML
    private TextField ntxt;
    @FXML
    private TextField vntxt;
    @FXML
    private TextField cntxt;
    @FXML
    private TextField lkmtxt;
    @FXML
    private TextField nsub1;
    @FXML
    private TextField nsub2;
    @FXML
    private TextField dsub1;
    @FXML
    private TextField dsub2;
    @FXML
    private TextField msub1;
    @FXML
    private TextField msub2;
    @FXML
    private TextField lasub1;
    @FXML
    private TextField losub1;
    @FXML
    private TextField lasub2;
    @FXML
    private TextField losub2;
    @FXML 
    private CheckBox chkGenerador1;
    @FXML 
    private CheckBox chkGenerador2;
    
    @FXML
    private void Cancelar() throws IOException 
    {
        App.setRoot("primary");
    }
    
    @FXML
    private void Guardar() throws IOException
    {
       if (faltanCampos()) 
       {
            mostrarAlerta();
            return;
       }
       try 
       {
           String nom = this.ntxt.getText();
           double vn = parseDouble(this.vntxt.getText());
           double cn = parseDouble(this.cntxt.getText());
           double lkm = parseDouble(this.lkmtxt.getText());
           
           String n1 = this.nsub1.getText();
           String d1 = this.dsub1.getText();
           String m1 = this.msub1.getText();
           double la1 = parseDouble(this.lasub1.getText());
           double lo1 = parseDouble(this.losub1.getText());
           boolean esGenSub1 = chkGenerador1.isSelected();
           
           String n2 = this.nsub2.getText();
           String d2 = this.dsub2.getText();
           String m2 = this.msub2.getText();
           double la2 = parseDouble(this.lasub2.getText());
           double lo2 = parseDouble(this.losub2.getText());
           boolean esGenSub2 = chkGenerador2.isSelected();

           Subestacion sub1 = sisele.doVerificarSubestacionNueva(n1);
           if (sub1 == null) 
           {
               sub1 = new Subestacion(n1, d1, m1, la1, lo1, esGenSub1);
           } 
           else 
           {
               sub1.setDepartamento(d1);
               sub1.setMunicipio(m1);
               sub1.setLatitud(la1);
               sub1.setLongitud(lo1);
               sub1.setEsGenerador(esGenSub1); 
               sisele.doSincronizarSubestacionGlobal(sub1);
           }

           Subestacion sub2 = sisele.doVerificarSubestacionNueva(n2);
           if (sub2 == null) 
           {
               sub2 = new Subestacion(n2, d2, m2, la2, lo2, esGenSub2);
           } 
           else 
           {
               sub2.setDepartamento(d2);
               sub2.setMunicipio(m2);
               sub2.setLatitud(la2);
               sub2.setLongitud(lo2);
               sub2.setEsGenerador(esGenSub2); 
               sisele.doSincronizarSubestacionGlobal(sub2);
           }

           LineaTransmision nuevaLinea = new LineaTransmision(nom, vn, cn, lkm, sub1, sub2);
           
           boolean exito = sisele.doAgregarLinea(nuevaLinea);
           if (exito) 
           {
               System.out.println("Línea de transmisión agregada y guardada con éxito.");
               mostrarConfirmacion();
               App.setRoot("primary");
           } 
           else 
           {
               System.out.println("Error: Datos de la línea inválidos (valores en cero o vacíos).");
               mostrarAlerta();
           } 
       } 
       catch (NumberFormatException e) 
       {
           System.err.println("Error: " + e.getMessage());
           e.printStackTrace();
       }
       catch (IOException e) 
       {
           System.err.println("Error al cargar la vista: " + e.getMessage());
           e.printStackTrace();
       }
    }
    
    private void mostrarAlerta()
    {
        Alert alert= new Alert(Alert.AlertType.ERROR);
               alert.setHeaderText(null);
               alert.setTitle("Error");
               alert.setContentText("Informacion sin completar o campos con 0 o menos");
               alert.showAndWait();
    }
    private void mostrarConfirmacion()
    {
        Alert alerta= new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Terminado");
        alerta.setContentText("Informacion completada exitosamente");
        alerta.showAndWait();
    }
    
    @FXML
    private void validarGenerador1() 
    {
        if (chkGenerador1.isSelected()) 
        {
            chkGenerador2.setSelected(false);
        }
    }

    @FXML
    private void validarGenerador2() 
    {
        if (chkGenerador2.isSelected()) 
        {
            chkGenerador1.setSelected(false);
        }
    }
    
    private boolean faltanCampos() 
    {
      
        return ntxt.getText().trim().isEmpty() ||
               vntxt.getText().trim().isEmpty() ||
               cntxt.getText().trim().isEmpty() ||
               lkmtxt.getText().trim().isEmpty() ||
               nsub1.getText().trim().isEmpty() ||
               dsub1.getText().trim().isEmpty() ||
               msub1.getText().trim().isEmpty() ||
               lasub1.getText().trim().isEmpty() ||
               losub1.getText().trim().isEmpty() ||
               nsub2.getText().trim().isEmpty() ||
               dsub2.getText().trim().isEmpty() ||
               msub2.getText().trim().isEmpty() ||
               lasub2.getText().trim().isEmpty() ||
               losub2.getText().trim().isEmpty();
    }
}