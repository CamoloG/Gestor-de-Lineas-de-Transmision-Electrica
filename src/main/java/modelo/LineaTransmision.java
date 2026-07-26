
package modelo;
import java.util.ArrayList;

public class LineaTransmision {
    
    private long id = 0;
    private String nombre;
    private double voltajeNominal,corrienteNominal,longitudKm;
    private Subestacion subestacion1, subestacion2;
    
    public LineaTransmision (String nombre, double voltajeNominal, double corrienteNominal, double longitudKm, Subestacion subestacion1, Subestacion subestacion2){
        
        this.nombre = nombre;
        this.voltajeNominal = voltajeNominal;
        this.corrienteNominal = corrienteNominal;
        this.longitudKm = longitudKm;
        this.subestacion1 = subestacion1;
        this.subestacion2 = subestacion2;
    }
    
    public long getId () {return id;}
    public String getNombre () {return nombre;}
    public double getVoltajeNominal () {return voltajeNominal;}
    public double getCorrienteNominal () {return corrienteNominal;}
    public double getLongitudKm () {return longitudKm;}
    public Subestacion getSubestacion1 () {return subestacion1;}
    public Subestacion getSubestacion2 () {return subestacion2;}
    
    public void setId (long id) {this.id = id;}
    public void setNombre (String nombre) {this.nombre = nombre;}
    public void setVoltajeNominal (double voltajeNominal) {this.voltajeNominal = voltajeNominal;}
    public void setCorrienteNominal (double corrienteNominal) {this.corrienteNominal = corrienteNominal;}
    public void setLongitudKm (double longitudKm) {this.longitudKm = longitudKm;}
    public void setSubestacion1(Subestacion subestacion1) {this.subestacion1 = subestacion1;}
    public void setSubestacion2(Subestacion subestacion2) {this.subestacion2 = subestacion2;}
    public double doCalcularCapacidadMW(){return ((Math.sqrt(3)*voltajeNominal*corrienteNominal)/1000)*0.95;}
    
    protected void doGenerarId(ArrayList<LineaTransmision> lineasTransmision){
        
        if (this.id != 0) return;
        
        for (int i=1;i<=10000;i++){
            
            boolean Ocupado = false;
            
            for (LineaTransmision l: lineasTransmision){
            
                if (i == l.getId()){
                    Ocupado = true;
                    break;
                }
            }
            
            if (Ocupado == false) {
                this.id = i;
                break;
            }
        }  
    }
}

