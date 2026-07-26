
package modelo;
import java.util.ArrayList;

public class Subestacion {
    
    private long id = 0;
    private String nombre, departamento, municipio;
    private double latitud, longitud;
    private boolean esGenerador;
    
    public Subestacion (String nombre, String departamento, String municipio, double latitud, double longitud, boolean esGenerador){
        
        this.nombre = nombre;
        this.departamento = departamento;
        this.municipio = municipio;
        this.latitud = latitud;
        this.longitud = longitud;
        this.esGenerador = esGenerador;
    }
    
    public long getId () {return id;}
    public String getNombre () {return nombre;}
    public String getDepartamento () {return departamento;}
    public String getMunicipio () {return municipio;}
    public double getLatitud () {return latitud;}
    public double getLongitud () {return longitud;}
    public boolean getEsGenerador() {return esGenerador;}
    
    public void setId (long id) {this.id = id;}
    public void setNombre (String nombre) {this.nombre = nombre;}
    public void setDepartamento (String departamento) {this.departamento = departamento;}
    public void setMunicipio (String municipio) {this.municipio = municipio;}
    public void setLatitud (double latitud) {this.latitud = latitud;}
    public void setLongitud (double longitud) {this.longitud = longitud;}
    public void setEsGenerador (boolean esGenerador) {this.esGenerador = esGenerador;}
    
    protected void doGenerarId(ArrayList<LineaTransmision> lineasTransmision){
        
        if (this.id != 0) return;
        
        for (int i=1;i<=10000;i++){
            
            boolean Ocupado = false;
            
            for (LineaTransmision l: lineasTransmision){
            
                if ((i == l.getSubestacion1().getId()) || (i == l.getSubestacion2().getId())){
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

