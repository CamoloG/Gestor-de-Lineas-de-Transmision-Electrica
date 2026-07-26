
package persistencia;
import modelo.Subestacion;
import modelo.LineaTransmision;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class Archivo 
{
    
    private String ruta;
    
    public Archivo () {
    
        String rutaGuardada = doCargarRuta();
        if (rutaGuardada == null) setRuta("data/LineasTransmision.csv");
        else this.ruta = rutaGuardada;
    }
            
    public void setRuta (String ruta) {this.ruta = ruta; doGuardarRuta();}
    
    private void doGuardarRuta () {
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/ruta.txt"))) {
            
            bw.write(ruta);
        }
        catch (IOException e){
            
        }
    }
    
    private String doCargarRuta () {
        
        String s;
        
        try (BufferedReader br = new BufferedReader(new FileReader("data/ruta.txt"))) {
            
            while ((s=br.readLine())!=null) return s;
            return null;
            
        }
        catch (IOException e){

            return null; //Devuelve null si no hay ruta o no existe el archivo en el que se almacena
        }
        
    }
    
    private String doObtenerInformacionSubestacion(String nombreSubestacion){
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("data/informacion.csv"), java.nio.charset.StandardCharsets.UTF_8))){
            String s;
            while ((s=br.readLine())!=null){
                
                String token[] = s.split(";");
                if (token[0].equals(nombreSubestacion)) return s;
            }
            
            br.close();
            return null;
        }
        catch (IOException e){
             return null;
        }
    }
    
    public synchronized boolean doGuardarLineas(ArrayList<LineaTransmision> lineasTransmision) {
        
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruta), java.nio.charset.StandardCharsets.UTF_8))) {

            String tipoSubestacion1 = "", tipoSubestacion2 = "";
            bw.write("Nombre;Subestaciones;Relacion;Voltaje Nominal;Corriente Nominal;Longitud (Km);Departamentos;Municipios;Latitudes;Longitudes");
            bw.newLine();
            
            for (LineaTransmision l : lineasTransmision){
            
                if (l.getSubestacion1().getEsGenerador()) {tipoSubestacion1 = "Generador";tipoSubestacion2 = "Subestacion";}
                else if (l.getSubestacion2().getEsGenerador()) {tipoSubestacion1 = "Subestacion";tipoSubestacion2 = "Generador";}
                else if (!l.getSubestacion1().getEsGenerador() && !l.getSubestacion2().getEsGenerador()){tipoSubestacion1 = "Subestacion";tipoSubestacion2 = "Subestacion";}
                
                bw.write(l.getNombre() + ";" + l.getSubestacion1().getNombre() + " - " + l.getSubestacion2().getNombre() + ";" + tipoSubestacion1 + " - " + tipoSubestacion2 + ";" +
                l.getVoltajeNominal() + ";" + l.getCorrienteNominal() + ";" + l.getLongitudKm() + ";" +
                l.getSubestacion1().getDepartamento() + " - " + l.getSubestacion2().getDepartamento() + ";" + l.getSubestacion1().getMunicipio() + " - " + l.getSubestacion2().getMunicipio() + ";" 
                + l.getSubestacion1().getLatitud() + " - " + l.getSubestacion2().getLatitud() + ";" + l.getSubestacion1().getLongitud() + " - " + l.getSubestacion2().getLongitud());
                bw.newLine();
            }
            
            return true;
        }
        catch (IOException e){
            
            return false;
        }
    }

    public ArrayList<LineaTransmision> doCargarLineas() throws IOException {
        
        ArrayList<LineaTransmision> lineasTransmision = new ArrayList<LineaTransmision>();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), java.nio.charset.StandardCharsets.UTF_8))) {
            
            String s;
            long id = 1;
            long idSubestacion = 1;
            double voltajeNominal, corrienteNominal, longitudKm;
            Subestacion subestacion1, subestacion2;
            double latitud1, latitud2, longitud1, longitud2;
            boolean esGenerador1, esGenerador2;
            
            while ((s=br.readLine())!=null){
                
                if (s.equals("Nombre;Subestaciones;Relacion;Voltaje Nominal;Corriente Nominal;Longitud (Km);Departamentos;Municipios;Latitudes;Longitudes")){
                   continue;
                }
                
                String token[] = s.split(";");
                String nombreSubestacion[] = token[1].split(" - ");
                String relacionSubestaciones[] = token[2].split(" - ");
                String departamentoSubestacion[] = token[6].split(" - ");
                String municipioSubestacion[] = token[7].split(" - ");
                String latitudSubestacion[] = token[8].split(" - ");
                String longitudSubestacion[] = token[9].split(" - ");
                latitud1 = Double.parseDouble(latitudSubestacion[0]);
                latitud2 = Double.parseDouble(latitudSubestacion[1]);
                longitud1 = Double.parseDouble(longitudSubestacion[0]);
                longitud2 = Double.parseDouble(longitudSubestacion[1]);
                if (relacionSubestaciones[0].equals("Generador")) esGenerador1 = true;
                else esGenerador1 = false;
                if (relacionSubestaciones[1].equals("Generador")) esGenerador2 = true;
                else esGenerador2 = false;
                
                subestacion1 = new Subestacion (nombreSubestacion[0],departamentoSubestacion[0],municipioSubestacion[0],latitud1,longitud1,esGenerador1);
                subestacion1.setId(idSubestacion);
                idSubestacion++;
                subestacion2 = new Subestacion (nombreSubestacion[1],departamentoSubestacion[1],municipioSubestacion[1],latitud2,longitud2,esGenerador2);
                subestacion2.setId(idSubestacion);
                idSubestacion++;
                
                voltajeNominal = Double.parseDouble(token[3]);
                corrienteNominal = Double.parseDouble(token[4]);
                longitudKm = Double.parseDouble(token[5]);
                
                lineasTransmision.add(new LineaTransmision(token[0],voltajeNominal,corrienteNominal,longitudKm,subestacion1,subestacion2));
                lineasTransmision.get(lineasTransmision.size()-1).setId(id);
                id++;                
            }
            
        } 
        
        return lineasTransmision;
    }    
            
    public ArrayList<LineaTransmision> doImportarLineas(String rutaImportacion) throws IOException {
        
        ArrayList<LineaTransmision> lineasTransmision = new ArrayList<LineaTransmision>();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaImportacion), java.nio.charset.StandardCharsets.UTF_8))) {
            
            String s, nombre, tipo;
            long id = 1; 
            double voltajeNominal, corrienteNominal, longitudKm; 
            Subestacion subestacion1, subestacion2;
            long idSubestacion = 1;
            String departamento, municipio;
            double latitudSubestacion, longitudSubestacion;
            boolean primeraLinea = true;
        
            while ((s=br.readLine())!=null){
                
                if (primeraLinea) {
                    s = s.replace("\uFEFF", ""); // eliminamos el BOM si está presente
                    primeraLinea = false;
                }

                String token[] = s.split(";",-1);
                if (token.length < 31 || token[0].trim().isEmpty() || (token[0].equals("INFORMACIÓN BÁSICA")) || token[0].equals("Nombre")) continue;
                nombre = token[0];
                voltajeNominal = Double.parseDouble((token[8].replace(".", "")).replace(",","."));
                corrienteNominal = Double.parseDouble((token[10].replace(".", "")).replace(",","."));
                longitudKm = Double.parseDouble((token[13].replace(".", "")).replace(",","."));
                String nombreSubestacion[] = token[5].split(" - ");
                departamento = token[26];
                municipio = token[27];
                String nombreSubestacion2 = "";
                
                if (nombreSubestacion.length < 2){
                    
                    String separadorLinea[] = token[0].split(" - ");
                    String separadorLinea2 = separadorLinea[1].replaceAll("\\s+\\d+\\s+\\d+\\s*(kV)?$", "").trim();
                    String nombreSubestacionLimpio = nombreSubestacion[0].trim();
                    
                    if (nombreSubestacionLimpio.equals(separadorLinea[0])){
                        
                        nombreSubestacion2 = separadorLinea2;
                    }
                    
                    else if (nombreSubestacionLimpio.equals(separadorLinea2)){
                        
                        nombreSubestacion2 = separadorLinea[0];
                    }
                }
                
                else nombreSubestacion2 = nombreSubestacion[1];
                
                boolean esGenerador;
                String infoSubestacion1 = doObtenerInformacionSubestacion(nombreSubestacion[0]);
                String infoSubestacion2 = doObtenerInformacionSubestacion(nombreSubestacion2);
                if (infoSubestacion1 != null) { //SI ENCUENTRA INFORMACION DE LA LINEA, RELLENA LOS DATOS CORRESPONDIENTES
                    
                    String separador[] = infoSubestacion1.split(";");
                    String tipoLinea = separador[1];
                    String latitud = separador[2];
                    String longitud = separador[3];
                    if (tipoLinea.equals("GENERADOR")) esGenerador = true;
                    else esGenerador = false;
                    latitudSubestacion = Double.parseDouble(latitud);
                    longitudSubestacion = Double.parseDouble(longitud);
                    subestacion1 = new Subestacion (nombreSubestacion[0],departamento,municipio,latitudSubestacion,longitudSubestacion,esGenerador);
                }
                
                else {subestacion1 = new Subestacion (nombreSubestacion[0],departamento,municipio,0,0, false);}
                
                if (infoSubestacion2 != null) {
                    
                    String separador[] = infoSubestacion2.split(";");
                    String tipoLinea = separador[1];
                    String latitud = separador[2];
                    String longitud = separador[3];
                    if (tipoLinea.equals("GENERADOR")) esGenerador = true;
                    else esGenerador = false;
                    latitudSubestacion = Double.parseDouble(latitud);
                    longitudSubestacion = Double.parseDouble(longitud);
                    subestacion2 = new Subestacion (nombreSubestacion2,departamento,municipio,latitudSubestacion,longitudSubestacion,esGenerador);
                }
                
                else {subestacion2 = new Subestacion (nombreSubestacion2,departamento,municipio,0,0, false);}
                
                //ASIGNAMOS LOS ID A LAS SUBESTACIONES
                subestacion1.setId(idSubestacion);
                idSubestacion++;
                subestacion2.setId(idSubestacion);
                idSubestacion++;
                
                lineasTransmision.add(new LineaTransmision(nombre, voltajeNominal, corrienteNominal, longitudKm,subestacion1,subestacion2));
                lineasTransmision.get(lineasTransmision.size()-1).setId(id);
                id++;
            }
        }
        
        return lineasTransmision;
    }
}
