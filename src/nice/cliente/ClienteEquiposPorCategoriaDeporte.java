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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nice.cliente;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nice.comun.Equipo;
import nice.comun.Noticia;

/**
 *
 * @author jjramos
 */
public class ClienteEquiposPorCategoriaDeporte {

    String baseURL="http://localhost:8081";

    private ClienteEquiposPorCategoriaDeporte(String trofeoId, String deporteId) {
            
            String respuesta = "";
            // URL del servicio Restlet
            String url = baseURL + "/categorias/"+trofeoId+"/deportes/"+deporteId+"/equipos";
            
            // Leemos y almacenamos la respuesta:
            respuesta = leerURL(url);
          
            // Interpretamos la respuesta JSON:
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            
        try {
            // Miramos los equipos:
            List<Equipo> listaEquipos= mapper.readValue(respuesta, new TypeReference<List<Equipo>>() {});
            
            // Los ordenamos:
            Collections.sort(listaEquipos, new Comparator<Equipo>() {

                @Override
                // Comparamos las cadenas de caracteres de los nombres de los equipos:
                public int compare(Equipo o1, Equipo o2) {
                    return o2.getNombre().compareTo(o1.getNombre());
                }
            });
            
            for(Equipo equipo:listaEquipos){
                System.out.println(" "+equipo.getNombre()+" -> "+equipo.getUrl());
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(ClienteEquiposPorCategoriaDeporte.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        
    }
    
    private String leerURL(String url) {
        String respuesta = "";
        try {
            // Hacemos una petici�n HTTP GET... Esto s�lo sirve para cosultar! de momento no modificamos nada:
            URL servicio = new URL(url);
            URLConnection conexion = servicio.openConnection();        
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conexion.getInputStream()));

            // Recibimos la respuesta l�nea a l�nea (el JSON):
            respuesta = "";
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                // System.out.println(inputLine);
                respuesta = respuesta + inputLine;
            }
            in.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ClienteReservas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClienteReservas.class.getName()).log(Level.SEVERE, null, ex);
        }

        return respuesta;
    }

   
    public static void main(String[] args) {
        new ClienteEquiposPorCategoriaDeporte("trofeo-rector-1a-division-2013","futbol-11");
    }

    
}
