//    
//    This file is part of the DeportesUGRServer.
//
//    Copyright (C) 2014 Juan J. Ramos-Munoz <jjramosapps@gmail.com>, Juan M. Lopez-Soler, Jorge Navarro-Ortiz, Jonathan Garcia-Prados, Pablo Ameigeiras 
//
//    DeportesUGRServer is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    DeportesUGRServer is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
//

package nice.servidor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jjramos
 */
class ConfiguracionServidorNice {

    // Periodo por defecto entre actualizaciones de la base de datos, en minutos.
    // Por defecto, 6 horas.
    public final static long UPDATE_PERIOD=6*60;
    
    int error=-1;
    String rutaFichero;
    private Map<String,String> valores;

    public ConfiguracionServidorNice(String rutaFichero) {
        this.rutaFichero=rutaFichero;
        
        valores=new HashMap<String,String>();
        
        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(rutaFichero));
            String linea=null;
            
            do {
                // Leemos una línea
                linea=inputStream.readLine();
                if(linea!=null){
                    error=procesarLinea(linea);
                }
            } while(linea!=null&&error==0);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfiguracionServidorNice.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfiguracionServidorNice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int  procesarLinea(String linea) {
        int error=0;
        
        // Eliminamos espacios al principio.
        linea=linea.trim();
        
        // Miramos si es un comentario:
        if(linea.startsWith(";")){
            // Es un comentario
        } else {
            // Miramos si es una variable con valor:
            String[] variable = linea.split("=");
            
            if(variable.length==2){
                error=actualizarValor(variable[0].trim(),variable[1].trim());
            }        
        }
        
        return error;
    }

    private int actualizarValor(String variable, String valor) {
        int error=0;
        
        valores.put(variable,valor);
        
        return error;
    }

    String getId() {
        return valores.get("joker-id");
    }

    String getCadId() {
        return valores.get("joker-cad-id");
    }

    int getError() {
        return error;
    }

    int getPuerto() {
        int puerto=8080;
        
        try {
          puerto=Integer.parseInt(valores.get("puerto"));
        } catch (NumberFormatException ex){
            puerto=8080;
        }
        
        return puerto;
    }

    /**
     * Devuelve el tiempo entre actualizaciones de la base de datos. Si no se especifica
     * en el fichero de configuración, se asume <code>UPDATE_PERIOD</code> minutos.
     * @return Periodo en segundos.
     */
    long getPeriodoActualizacion() {
        long periodEnMinutos=UPDATE_PERIOD;
        String periodEnMinutos_=valores.get("refresco");
        
        if(periodEnMinutos_!=null){
            try {
               periodEnMinutos=Integer.parseInt(valores.get("refresco").trim());
             } catch (NumberFormatException ex){
                periodEnMinutos=UPDATE_PERIOD;
            }
        }
        
        return periodEnMinutos;
    }
    
    
}
