package controlador;

import modelo.LineaTransmision;
import modelo.SistemaElectrico;
import modelo.Subestacion;
import java.io.*;
import java.io.IOException;
import javafx.fxml.FXML;
import persistencia.Archivo;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PuenteDeCrearDatos 
{
    
    @FXML
    private void creardesdecero() throws IOException
    {
        App.setRoot("creardatos");
    }
    
    @FXML
    private void crearconsubs() throws IOException
    {
        App.setRoot("creardatosconsubs");
    }
    
    @FXML
    private void Cancelar() throws IOException 
    {
        App.setRoot("primary");
    }
}
