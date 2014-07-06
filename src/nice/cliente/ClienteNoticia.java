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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nice.comun.Noticia;
import nice.comun.PistaReservable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author jjramos
 */
public class ClienteNoticia {

    String baseURL="http://localhost:8081";
    
    ClienteNoticia(){
        try {
            // Ejemplo: del enlace a una noticia, extrae sus elementos y hace la llamda:
            String enlaceNoticia="http://cad.ugr.es/pages/tablon/*/competiciones/i-circuito-spartan-triatlon-de-granada-2";

            
            // Obtenemos el tablón y el identificador de la noticia:
            String[] tokens = enlaceNoticia.split("/");
            String tablon=tokens[tokens.length-2];
            String noticiaId=tokens[tokens.length-1];
            
            
            String respuesta = "";
            // URL del servicio Restlet
            String url = baseURL + "/noticias/"+noticiaId+"/tablones/"+tablon;
            
            // Leemos y almacenamos la respuesta:
            respuesta = leerURL(url);
          
            // Interpretamos la respuesta JSON:
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            
            // Ejemplo para recibir una noticia:
            Noticia noticia= mapper.readValue(respuesta, new TypeReference<Noticia>() {
            });
            
            
            // Lo mostramos:
            System.out.println("Título: "+noticia.getTitulo());
            System.out.println("Imagen: "+noticia.getImagenURL());
            System.out.println("Noticia: "+noticia.getTextoHtml());
            System.out.println("Enlace Original: "+noticia.getUrl());
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(ClienteNoticia.class.getName()).log(Level.SEVERE, null, ex);
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
        new ClienteNoticia();
    }

}
