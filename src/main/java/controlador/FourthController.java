package controlador;

import java.io.IOException;
import javafx.fxml.FXML;
import modelo.LineaTransmision;
import modelo.SistemaElectrico;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import modelo.Subestacion;

public class FourthController 
{
    SistemaElectrico mav = SistemaElectrico.getInstancia();
    
    private LineaTransmision lineaAEditar = null;

    @FXML private TextField id;

    @FXML private TextField txtNombre;
    @FXML private TextField txtVoltaje;
    @FXML private TextField txtCorriente;
    @FXML private TextField txtLongitud;

    @FXML private TextField txtNombreSub1;
    @FXML private TextField txtDeptoSub1;
    @FXML private TextField txtMuniSub1;
    @FXML private TextField txtLatSub1;
    @FXML private TextField txtLonSub1;
    @FXML private CheckBox chkGenerador1;

    @FXML private TextField txtNombreSub2;
    @FXML private TextField txtDeptoSub2;
    @FXML private TextField txtMuniSub2;
    @FXML private TextField txtLatSub2;
    @FXML private TextField txtLonSub2;
    @FXML private CheckBox chkGenerador2;

    @FXML
    private void buscarLineaExistente() {
        try 
        {
 
            long idReal = Long.parseLong(id.getText());
            LineaTransmision encontrada = mav.doBuscarLinea(idReal);
            
            if (encontrada != null) {

                lineaAEditar = encontrada;

                txtNombre.setText(lineaAEditar.getNombre());
                txtVoltaje.setText(String.valueOf(lineaAEditar.getVoltajeNominal()));
                txtCorriente.setText(String.valueOf(lineaAEditar.getCorrienteNominal()));
                txtLongitud.setText(String.valueOf(lineaAEditar.getLongitudKm()));
                
                txtNombreSub1.setText(lineaAEditar.getSubestacion1().getNombre());
                txtDeptoSub1.setText(lineaAEditar.getSubestacion1().getDepartamento());
                txtMuniSub1.setText(lineaAEditar.getSubestacion1().getMunicipio());
                txtLatSub1.setText(String.valueOf(lineaAEditar.getSubestacion1().getLatitud()));
                txtLonSub1.setText(String.valueOf(lineaAEditar.getSubestacion1().getLongitud()));
                chkGenerador1.setSelected(lineaAEditar.getSubestacion1().getEsGenerador());

                txtNombreSub2.setText(lineaAEditar.getSubestacion2().getNombre());
                txtDeptoSub2.setText(lineaAEditar.getSubestacion2().getDepartamento());
                txtMuniSub2.setText(lineaAEditar.getSubestacion2().getMunicipio());
                txtLatSub2.setText(String.valueOf(lineaAEditar.getSubestacion2().getLatitud()));
                txtLonSub2.setText(String.valueOf(lineaAEditar.getSubestacion2().getLongitud()));
                chkGenerador2.setSelected(lineaAEditar.getSubestacion2().getEsGenerador());
                
            } 
            else 
            {
                mostrarAlerta(AlertType.WARNING, "No Encontrado", "No existe ninguna línea con ese ID.");
                limpiarCampos();
            }
            
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Por favor ingresa un número de ID válido.");
        }
    }

    @FXML
    private void guardarActualizacion() throws IOException 
    {
       if (lineaAEditar == null) 
       {
            mostrarAlerta(AlertType.WARNING, "Alerta", "Primero debes buscar una línea por su ID.");
            return;
       }

       try 
       {
        long idOriginal = lineaAEditar.getId();

        lineaAEditar.setNombre(txtNombre.getText());
        lineaAEditar.setVoltajeNominal(Double.parseDouble(txtVoltaje.getText()));
        lineaAEditar.setCorrienteNominal(Double.parseDouble(txtCorriente.getText()));
        lineaAEditar.setLongitudKm(Double.parseDouble(txtLongitud.getText()));

        String nomSub1 = txtNombreSub1.getText().trim();
        Subestacion sub1Final = mav.doVerificarSubestacionNueva(nomSub1);
        
        if (sub1Final == null) 
        {
            sub1Final = lineaAEditar.getSubestacion1();
            sub1Final.setNombre(nomSub1);
        }
        
        sub1Final.setDepartamento(txtDeptoSub1.getText());
        sub1Final.setMunicipio(txtMuniSub1.getText());
        sub1Final.setLatitud(Double.parseDouble(txtLatSub1.getText()));
        sub1Final.setLongitud(Double.parseDouble(txtLonSub1.getText()));
        sub1Final.setEsGenerador(chkGenerador1.isSelected());
        
        lineaAEditar.setSubestacion1(sub1Final);
        mav.doSincronizarSubestacionGlobal(sub1Final);

        String nomSub2 = txtNombreSub2.getText().trim();
        Subestacion sub2Final = mav.doVerificarSubestacionNueva(nomSub2);
        
        if (sub2Final == null) 
        {
            sub2Final = lineaAEditar.getSubestacion2();
            sub2Final.setNombre(nomSub2);
        }
        
        sub2Final.setDepartamento(txtDeptoSub2.getText());
        sub2Final.setMunicipio(txtMuniSub2.getText());
        sub2Final.setLatitud(Double.parseDouble(txtLatSub2.getText()));
        sub2Final.setLongitud(Double.parseDouble(txtLonSub2.getText()));
        sub2Final.setEsGenerador(chkGenerador2.isSelected());

        lineaAEditar.setSubestacion2(sub2Final);
        mav.doSincronizarSubestacionGlobal(sub2Final);

        boolean exito = mav.doActualizarLinea(idOriginal, lineaAEditar);

        if (exito) 
        {
            mostrarAlerta(AlertType.INFORMATION, "Actualización Exitosa", "La linea de transmisión y sus subestaciones han sido actualizadas globalmente.");
            id.clear();
            limpiarCampos();
            lineaAEditar = null;
            App.setRoot("primary");
        } 
        else 
        {
            mostrarAlerta(AlertType.ERROR, "Error de Validación", "No se pudo actualizar. Verifique que los campos no estén vacíos y los valores numéricos sean mayores a cero.");
        }
        
       }    
       catch (NumberFormatException e) 
       {
           mostrarAlerta(AlertType.ERROR, "Error de Datos", "Asegúrate de que Voltaje, Corriente, Longitud y Coordenadas sean números válidos (Usa punto para decimales).");
       }
    }

    @FXML
    private void validarGenerador1() {
        if (chkGenerador1.isSelected()) {
            chkGenerador2.setSelected(false);
        }
    }

    @FXML
    private void validarGenerador2() {
        if (chkGenerador2.isSelected()) {
            chkGenerador1.setSelected(false);
        }
    }

    @FXML
    private void Cancelar() throws IOException 
    {
        App.setRoot("primary");
    }
    
    private void limpiarCampos() 
    {
        txtNombre.clear();
        txtVoltaje.clear();
        txtCorriente.clear();
        txtLongitud.clear();
        
        txtNombreSub1.clear();
        txtDeptoSub1.clear();
        txtMuniSub1.clear();
        txtLatSub1.clear();
        txtLonSub1.clear();
        chkGenerador1.setSelected(false);

        txtNombreSub2.clear();
        txtDeptoSub2.clear();
        txtMuniSub2.clear();
        txtLatSub2.clear();
        txtLonSub2.clear();
        chkGenerador2.setSelected(false);
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