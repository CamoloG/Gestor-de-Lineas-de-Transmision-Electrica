package controlador;

import java.io.IOException;
import javafx.fxml.FXML;
import modelo.LineaTransmision;
import modelo.SistemaElectrico;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SixthController 
{
    SistemaElectrico mav = SistemaElectrico.getInstancia();

    @FXML private TextField txtBuscarId; 
    @FXML private TextArea txtResultado; 

    @FXML
    public void initialize() {
        txtResultado.setVisible(false);
        txtResultado.setManaged(false);
    }

    @FXML
    private void buscarLinea() {
        txtResultado.clear(); 
        String inputId = txtBuscarId.getText().trim();

        if (inputId.isEmpty()) 
        {
            mostrarAlerta(AlertType.WARNING, "Campo Vacio", "Por favor, digite un ID para buscar.");
            ocultarResultado();
            return;
        }

        try 
        {
            long idReal = Long.parseLong(inputId);
            LineaTransmision encontrada = mav.doBuscarLinea(idReal);

            if (encontrada != null) {
                StringBuilder info = new StringBuilder();
                info.append("--- INFORMACION DE LA LINEA ENCONTRADA ---\n\n");
                info.append("ID: ").append(encontrada.getId()).append("\n");
                info.append("Nombre: ").append(encontrada.getNombre()).append("\n");
                info.append("Voltaje Nominal: ").append(encontrada.getVoltajeNominal()).append(" kV\n");
                info.append("Corriente Nominal: ").append(encontrada.getCorrienteNominal()).append(" A\n");
                info.append("Longitud: ").append(encontrada.getLongitudKm()).append(" km\n");
                info.append("Capacidad: ").append(encontrada.doCalcularCapacidadMW()).append(" MW\n");

                if (encontrada.getSubestacion1() != null) {
                    info.append("\nSubestación Origen:\n");
                    info.append("  - Nombre: ").append(encontrada.getSubestacion1().getNombre()).append("\n");
                    info.append("  - Departamento: ").append(encontrada.getSubestacion1().getDepartamento()).append("\n");
                }
                
                if (encontrada.getSubestacion2() != null) {
                    info.append("\nSubestación Destino:\n");
                    info.append("  - Nombre: ").append(encontrada.getSubestacion2().getNombre()).append("\n");
                    info.append("  - Departamento: ").append(encontrada.getSubestacion2().getDepartamento()).append("\n");
                }

                txtResultado.setText(info.toString());
                txtResultado.setVisible(true);
                txtResultado.setManaged(true);
                
            } 
            else 
            {
                mostrarAlerta(AlertType.INFORMATION, "No Encontrada", "No existe ninguna linea con el ID: " + idReal);
                ocultarResultado();
            }

        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Error de Formato", "El ID ingresado no es válido.");
            txtBuscarId.clear();
            ocultarResultado();
        }
    }

    // Método auxiliar para no repetir la lógica de ocultación en los errores
    private void ocultarResultado() 
    {
        txtResultado.setVisible(false);
        txtResultado.setManaged(false);
    }

    @FXML
    private void Cancelar() throws IOException 
    {
        App.setRoot("primary");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) 
    {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}