package controlador;

import modelo.LineaTransmision;
import modelo.SistemaElectrico;
import modelo.Subestacion;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static java.lang.Double.parseDouble;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Acer
 */
public class SecondaryControllerWithSubs implements Initializable 
{
    SistemaElectrico sisele = SistemaElectrico.getInstancia();
    
    @FXML
    private TextField ntxt;
    @FXML
    private TextField vntxt;
    @FXML
    private TextField cntxt;
    @FXML
    private TextField lkmtxt;
    @FXML
    private TextField idsub1;
    @FXML
    private TextField idsub2;
    @FXML
    private TableView<Subestacion> tablaSubestaciones;
    @FXML
    private TableColumn<Subestacion, Long> colId;
    @FXML
    private TableColumn<Subestacion, String> colNombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cargarDatosTabla();
    }

    private void cargarDatosTabla() {
        ArrayList<Subestacion> subestacionesVivas = new ArrayList<>();
        ArrayList<LineaTransmision> listaLineas = sisele.getLineasTransmision();

        if (listaLineas != null) 
        {
            for (LineaTransmision linea : listaLineas) {
                if (linea.getSubestacion1() != null && !subestacionesVivas.contains(linea.getSubestacion1())) {
                    subestacionesVivas.add(linea.getSubestacion1());
                }
                if (linea.getSubestacion2() != null && !subestacionesVivas.contains(linea.getSubestacion2())) {
                    subestacionesVivas.add(linea.getSubestacion2());
                }
            }
        }
        ObservableList<Subestacion> datosTabla = FXCollections.observableArrayList(subestacionesVivas);
        tablaSubestaciones.setItems(datosTabla);
    }

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
           
           long idS1 = Long.parseLong(this.idsub1.getText());
           long idS2 = Long.parseLong(this.idsub2.getText());
           
           boolean exito = sisele.doCrearLineaConSubestaciones(idS1, idS2, nom, vn, cn, lkm);
           
           if (exito == true) 
            {
               System.out.println("Linea de transmision agregada y guardada con exito.");
               mostrarConfirmacion();
               App.setRoot("primary");
            } 
           else 
            {
               System.out.println("Error: Datos de la linea invalidos o IDs de subestaciones no encontrados.");
               mostrarAlerta();
            } 
       } 
       catch (NumberFormatException e) 
       {
           System.err.println("Error: " + e.getMessage());
           mostrarAlerta();
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
               alert.setContentText("Informacion sin completar, campos con 0 o IDs incorrectos, mismo ID de subestacion");
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
    
    private boolean faltanCampos() 
    {
        return ntxt.getText().trim().isEmpty() ||
               vntxt.getText().trim().isEmpty() ||
               cntxt.getText().trim().isEmpty() ||
               lkmtxt.getText().trim().isEmpty() ||
               idsub1.getText().trim().isEmpty() ||
               idsub2.getText().trim().isEmpty();
    }
}