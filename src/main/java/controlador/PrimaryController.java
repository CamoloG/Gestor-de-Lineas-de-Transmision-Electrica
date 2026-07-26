//controlador de la interfaz principal primary.fxml
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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.concurrent.Worker; 
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;

public class PrimaryController implements Initializable
{
    @FXML
    private WebView mapalineas;
    @FXML
    private TextField mvtotal;
    @FXML
    private Button imporarchi;
    @FXML
    private MenuItem cambiarruta;
    
    SistemaElectrico mar = SistemaElectrico.getInstancia();
    String res;
    double capacidadTotal = mar.doCalcularCapacidadTotal();
    
   @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        imprimirEnUI(res);
        WebEngine webEngine = mapalineas.getEngine();

        webEngine.setJavaScriptEnabled(true);

        File archivoMapa = new File("data/Mapa.html");

        if (archivoMapa.exists()) {
            String urlLocal = archivoMapa.toURI().toString();
            webEngine.load(urlLocal);
            System.out.println("Mapa cargado exitosamente desde: " + urlLocal);
        } 
        else 
        {
            System.err.println("ERROR CRITICO: No se encontro el archivo en: " + archivoMapa.getAbsolutePath());
        }
        
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) 
            {
                System.out.println("El HTML del mapa cargo con exito. Procediendo a inyectar subestaciones dinamicamente...");
                cargarMapaDesdeMemoria(webEngine);
            }
        });
        
        webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldExc, newExc) -> {
            if (newExc != null) {
                System.err.println("Error interno en el WebView: " + newExc.getMessage());
            }
        });
    }
    
    private void cargarMapaDesdeMemoria(WebEngine engine) 
    {
        try 
        {
            engine.executeScript("limpiarMapa();");
        } 
        catch (Exception e) 
        {
            System.err.println("Aviso: No se pudo limpiar el mapa de forma nativa: " + e.getMessage());
        }

        ArrayList<LineaTransmision> listaLineas = mar.getLineasTransmision();
        
        if (listaLineas == null || listaLineas.isEmpty()) 
        {
            System.out.println("No hay líneas de transmisión cargadas para dibujar.");
            return;
        }

        ArrayList<Subestacion> subestacionesVivas = new ArrayList<>();

        for (LineaTransmision linea : listaLineas) 
        {
            if (linea.getSubestacion1() != null && !subestacionesVivas.contains(linea.getSubestacion1())) 
            {
                subestacionesVivas.add(linea.getSubestacion1());
            }
            if (linea.getSubestacion2() != null && !subestacionesVivas.contains(linea.getSubestacion2())) 
            {
                subestacionesVivas.add(linea.getSubestacion2());
            }
        }

        for (Subestacion sub : subestacionesVivas) 
        {
            try 
            {
                String nombreJS = sub.getNombre().replace("'", "\\'"); 
                String tipoJS = sub.getEsGenerador() ? "Generador" : "Subestacion";
                double latitud = sub.getLatitud();
                double longitud = sub.getLongitud();

                String scriptPunto = String.format(java.util.Locale.US, "agregarPuntoDesdeJava(%.6f, %.6f, '%s', '%s');", latitud, longitud, nombreJS, tipoJS);
                engine.executeScript(scriptPunto);
            } 
            catch (Exception e) 
            {
                System.err.println("Error dibujando subestación: " + e.getMessage());
            }
        }

        for (LineaTransmision linea : listaLineas) 
        {
            try 
            {
                if (linea.getSubestacion1() != null && linea.getSubestacion2() != null) 
                {
                    double lat1 = linea.getSubestacion1().getLatitud();
                    double lng1 = linea.getSubestacion1().getLongitud();
                    double lat2 = linea.getSubestacion2().getLatitud();
                    double lng2 = linea.getSubestacion2().getLongitud();
                    
                    String nombreLineaJS = linea.getNombre().replace("'", "\\'");
                    
                    String scriptLinea = String.format(java.util.Locale.US, 
                        "agregarLineaDesdeJava(%.6f, %.6f, %.6f, %.6f, '%s');", 
                        lat1, lng1, lat2, lng2, nombreLineaJS);
                    
                    engine.executeScript(scriptLinea);
                }
            } 
            catch (Exception e) 
            {
                System.err.println("Error procesando linea individual: " + e.getMessage());
            }
        }
        
        System.out.println("Proceso de graficacion dinámica finalizado con exito");
    }
    
    @FXML
    private void leer() throws IOException
    {
        App.setRoot("leerdatos");
    }
    
    @FXML
    private void crear() throws IOException
    {
        App.setRoot("puentedecreardatos");
    }
    
    @FXML
    private void eliminar() throws IOException
    {
        App.setRoot("eliminardatos");
    }
    
    @FXML
    private void actualizar() throws IOException
    {
        App.setRoot("actualizardatos");
    }
    
    @FXML
    private void buscarlinea() throws IOException
    {
        App.setRoot("buscarlinea");
    }
    
    @FXML
    private void CargaArchivo() throws IOException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione el archivo");

        fileChooser.getExtensionFilters().addAll
        (
            new FileChooser.ExtensionFilter("Archivos de Datos", "*.txt", "*.csv"),
            new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        Stage stage = (Stage) imporarchi.getScene().getWindow();

        File archivoSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoSeleccionado != null) 
        {
            try
            {
            String rutaAbsoluta = archivoSeleccionado.getAbsolutePath();
            System.out.println("Ruta obtenida: " + rutaAbsoluta);
            ArrayList<LineaTransmision> NuevoArchivo = mar.getArchivo().doImportarLineas(rutaAbsoluta);
            mar.setLineasTransmision(NuevoArchivo);
            capacidadTotal = mar.doCalcularCapacidadTotal();
            imprimirEnUI(res);
            
            cargarMapaDesdeMemoria(mapalineas.getEngine());
            
            }
            catch(NumberFormatException e)
            {
                System.out.println(e);
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
        } 
        else 
        {
            System.out.println("El usuario cancelo la seleccion.");
        }
    }
    
    @FXML
    private void cambiarRutaGuardado() 
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione o cree el archivo de guardado");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"),
            new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"),
            new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        // Obtenemos el Stage usando directamente el botón "cambiarruta"
        Stage stage = (Stage) imporarchi.getScene().getWindow();

        File archivoSeleccionado = fileChooser.showSaveDialog(stage);

        if (archivoSeleccionado != null) 
        {
            String nuevaRuta = archivoSeleccionado.getAbsolutePath();
            
            mar.getArchivo().setRuta(nuevaRuta);
            mar.getArchivo().doGuardarLineas(mar.getLineasTransmision());
            System.out.println("Ruta de guardado actualizada a: " + nuevaRuta);
            mostrarConfirmacion("Ruta actualizada", "Los datos se guardaran ahora en:\n" + nuevaRuta);
        } 
        else 
        {
            System.out.println("El usuario cancelo el cambio de ruta.");
        }
    }
    
    private void imprimirEnUI(String res) 
    {
        res = String.valueOf(capacidadTotal);
        System.out.println("[MV TOTAL]: " + res);
        mvtotal.setText(res);
    }
    
    @FXML
    private void Salir()
    {
        Platform.exit();
        System.exit(0);
    }
    
    private void mostrarConfirmacion(String titulo, String mensaje) 
    {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}