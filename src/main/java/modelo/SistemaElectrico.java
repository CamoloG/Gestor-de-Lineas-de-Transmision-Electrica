
package modelo;
import java.io.IOException;
import java.util.ArrayList;
import persistencia.Archivo;        

/**
 *
 * @author USUARIO
 */
public class SistemaElectrico 
{
    private static SistemaElectrico instanciaUnica;
    
    public static SistemaElectrico getInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new SistemaElectrico();
        }
        return instanciaUnica;
    }
    
    public ArrayList<LineaTransmision> lineasTransmision;
    private Archivo archivo = new Archivo ();
        
    public SistemaElectrico () {
        
        try {
            
            this.lineasTransmision = archivo.doCargarLineas();
        }
        
        catch (IOException e){
            
            //Si es primera vez o no logra cargar las lineas, se crea una lista nueva
            lineasTransmision = new ArrayList<LineaTransmision>();
        }
    }
    
    public ArrayList<LineaTransmision> getLineasTransmision () {return lineasTransmision;}
    
    public void setLineasTransmision (ArrayList<LineaTransmision> lineasTransmision) throws IOException {
        this.lineasTransmision = lineasTransmision;
        archivo.doGuardarLineas(lineasTransmision);
    }

    public Archivo getArchivo () {return archivo;}
    
    public boolean doAgregarLinea (LineaTransmision lineaTransmision) throws IOException {
        
        if (lineaTransmision.getNombre().trim().isEmpty() || lineaTransmision.getVoltajeNominal() <= 0 || 
        lineaTransmision.getCorrienteNominal() <= 0 || lineaTransmision.getLongitudKm() <= 0) {
           return false;
        }
        
        lineaTransmision.doGenerarId(lineasTransmision);
        lineaTransmision.getSubestacion1().doGenerarId(lineasTransmision);
        lineaTransmision.getSubestacion2().doGenerarId(lineasTransmision);
                
        lineasTransmision.add(lineaTransmision);
        archivo.doGuardarLineas(lineasTransmision);
        return true;
    }
    
    public boolean doEliminarLinea (long id) throws IOException {
        
        for (int x=0;x<lineasTransmision.size();x++){
            
            if (lineasTransmision.get(x).getId() == id){
                
                lineasTransmision.remove(x);
                archivo.doGuardarLineas(lineasTransmision);
                return true;
            } 
        }
        
        return false;
    }
    
    public LineaTransmision doBuscarLinea (long id) {
        
        for (LineaTransmision l : lineasTransmision){
            
            if (l.getId() == id) return l;
        }
        
        return null; //Devuelve null si no encontró la linea
    }
    
    public boolean doActualizarLinea (long idLineaModificada, LineaTransmision nuevaLinea) throws IOException {
        
        if (nuevaLinea.getNombre().trim().isEmpty() || nuevaLinea.getVoltajeNominal() <= 0 || 
        nuevaLinea.getCorrienteNominal() <= 0 || nuevaLinea.getLongitudKm() <= 0) {
           return false;
        }        
        
        for (int x=0;x<lineasTransmision.size();x++){
            
            if (lineasTransmision.get(x).getId() == idLineaModificada) {
                
                lineasTransmision.set(x,nuevaLinea);
                lineasTransmision.get(x).setId(idLineaModificada);
                archivo.doGuardarLineas(lineasTransmision);
                return true;
            }
        }
        
        return false;
    }
    
    public boolean doCrearLineaConSubestaciones (long idSubestacion1, long idSubestacion2, String nombre, double voltajeNominal, double corrienteNominal, double longitudKm) {
        
        if (idSubestacion1 == idSubestacion2) return false;
        
        Subestacion subestacion1 = null;
        Subestacion subestacion2 = null;
        
        for (LineaTransmision l : lineasTransmision) {
            
            if ((idSubestacion1 == l.getSubestacion1().getId())) {subestacion1 = l.getSubestacion1();}
            else if ((idSubestacion1 == l.getSubestacion2().getId())) {subestacion1 = l.getSubestacion2();}
            
            if ((idSubestacion2 == l.getSubestacion1().getId())) {subestacion2 = l.getSubestacion1();}
            else if ((idSubestacion2 == l.getSubestacion2().getId())) {subestacion2 = l.getSubestacion2();}
            
            if (subestacion1 != null && subestacion2 != null) break;
        }

        if (subestacion1 == null || subestacion2 == null) return false; 
        
        try {doAgregarLinea(new LineaTransmision (nombre, voltajeNominal, corrienteNominal, longitudKm, subestacion1, subestacion2));}
        catch (IOException e) {return false;}
        
        return true;
    }
    
    public ArrayList<LineaTransmision> doFiltrarPorDepartamento (String departamento) {
        
        ArrayList<LineaTransmision> filtradas = new ArrayList<LineaTransmision>();
        
        for (LineaTransmision l : lineasTransmision) {
            if (l.getSubestacion1().getDepartamento().equalsIgnoreCase(departamento) || l.getSubestacion2().getDepartamento().equalsIgnoreCase(departamento)) {
                filtradas.add(l);
            }
        }
        
        return filtradas;
    }
    
    public ArrayList<LineaTransmision> doFiltrarPorVoltaje (double voltajeMinimo, double voltajeMaximo) {
        
        ArrayList<LineaTransmision> filtradas = new ArrayList<LineaTransmision>();
        
        for (LineaTransmision l : lineasTransmision) {
            if ((l.getVoltajeNominal() >= voltajeMinimo) && (l.getVoltajeNominal() <= voltajeMaximo) ) {
                filtradas.add(l);
            }
        }
        
        return filtradas;
    }
    
    public double doCalcularCapacidadTotal() {
        
        double capacidadTotal = 0;
        
        for (LineaTransmision l : lineasTransmision) {
            capacidadTotal += l.doCalcularCapacidadMW();
        }
        
        return capacidadTotal;
    }
    
    public Subestacion doVerificarSubestacionNueva(String nombreSubestacion)
    {
        
        for (LineaTransmision l : lineasTransmision)
        {
            
            if (l.getSubestacion1().getNombre().equalsIgnoreCase(nombreSubestacion)) return l.getSubestacion1();
            
            else if (l.getSubestacion2().getNombre().equalsIgnoreCase(nombreSubestacion)) return l.getSubestacion2();
        }
        
        return null;
    }
    
    public void doSincronizarSubestacionGlobal(Subestacion subestacionActualizada) 
    {
        for (LineaTransmision l : lineasTransmision) 
        {
            if (l.getSubestacion1().getNombre().equalsIgnoreCase(subestacionActualizada.getNombre())) 
            {
                l.setSubestacion1(subestacionActualizada);
            }
            if (l.getSubestacion2().getNombre().equalsIgnoreCase(subestacionActualizada.getNombre())) 
            {
                l.setSubestacion2(subestacionActualizada);
            }
        }
    }
}
