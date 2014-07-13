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
 * Clase que permite leer el fichero de configuración del servidor, y almacena los parámetros de configuración
 * como pares de parámetro y valor.
 * El fichero debe tener como formato:
 * - por cada línea, se puede indicar un parámetro y su valor, mediante el signo "=": ej: puerto = 8080
 * - Se pueden añadir comentarios, simplemente comenzando la línea con un punto y coma (";")
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

    /**
     * Constructor al que se le pasa la ruta del fichero del que leer la configuración.
     * @param rutaFichero Ruta del fichero de configuración.
     */
    public ConfiguracionServidorNice(String rutaFichero) {
        this.rutaFichero=rutaFichero;
        
        valores=new HashMap<String,String>();
        
        try {
            // Se lee del fichero línea a línea
            BufferedReader inputStream = new BufferedReader(new FileReader(rutaFichero));
            String linea=null;
            
            do {
                // Leemos una línea
                linea=inputStream.readLine();
                if(linea!=null){
                    // Se procesa la línea para almacenar el nombre de cada parámetro y su valor.
                    error=procesarLinea(linea);
                }
            } while(linea!=null&&error==0);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfiguracionServidorNice.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfiguracionServidorNice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para procesar cada línea, de forma que se pueda obtener el nombre de la variable y su valor, separados por "=",
     * o un comentario, si la linea comienza por ";". ¡Ojo! la sintaxis debe ser muy estricta. 
     * @param linea String que representa una línea del fichero de configuración.
     * @return 
     */
    private int  procesarLinea(String linea) {
        int error=0;
        
        // Eliminamos espacios al principio y final.
        linea=linea.trim();
        
        // Miramos si es un comentario:
        if(linea.startsWith(";")){
            // Es un comentario
        } else {
            // Miramos si es una variable con valor:
            String[] variable = linea.split("=");
            
            // Si es una variable con su asignación:
            if(variable.length==2){
                error=actualizarValor(variable[0].trim(),variable[1].trim());
            }        
        }
        
        return error;
    }

    /**
     * Almacena la pareja <code>variable</code> y su <code>valor</code> correspondiente
     * @param variable Nombre del parámetro a almacenar. 
     * @param valor Valor del parámetro.
     * @return 
     */
    private int actualizarValor(String variable, String valor) {
        int error=0;
        
        valores.put(variable,valor);
        
        return error;
    }

    /**
     * 
     * @return 
     */
    String getId() {
        return valores.get("joker-id");
    }

    /**
     * 
     * @return 
     */
    String getCadId() {
        return valores.get("joker-cad-id");
    }

    /**
     * 
     * @return 
     */
    int getError() {
        return error;
    }

    /**
     * Devuelve el puerto donde escuchará el servidor, configurado en el fichero, u <code>8080</code> por defecto.
     * @return Puerto configurado.
     */
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
