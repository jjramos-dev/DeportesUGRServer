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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nice.comun.Equipo;
import nice.comun.Fase;
import nice.comun.Partido;
import nice.comun.Ronda;

/**
 *
 * @author jjramos
 */
public class CienteTorneoFiltrarPorEquipo {
    
        private String baseUrl = "http://localhost:8081";
    
    
    // Quizás fuera mejor usar el id.... par aevitar las tildes!!!!
    public CienteTorneoFiltrarPorEquipo(String trofeoId, String deporteId, String equipo) {
                
        List<Fase> fases = obtenerFases(trofeoId, deporteId);
                            // Ordenamos las fases:
                    Collections.sort(fases, new Comparator<Fase>() {

            @Override
            public int compare(Fase o1, Fase o2) {
                // Cambiar el signo para orden inverso.
                return -o2.getTitulo().compareTo(o1.getTitulo());
            }});
        
        for (Fase fase : fases) {
                                        
                    List<Ronda> rondas = fase.getRondas();
                    System.out.println("\t\tFase: " + fase.getTitulo());

                    
                    
                    for (Ronda ronda : rondas) {
                        List<Partido> partidos = ronda.getPartidos();
                        System.out.println("\t\t - Ronda: " + ronda.getTitulo());

                        // /////////////////////////////////////
                        // Vamos a ordenarlos por fechas!!!!!
                        Collections.sort(partidos, new Comparator<Partido>() {

                            @Override
                            public int compare(Partido o1, Partido o2) {
                                int diff=0;
                                Date oo1 = o1.getFecha();
                                Date oo2 = o2.getFecha();
                                
                                if(oo1!=null&&oo2!=null){
                                    diff=oo1.compareTo(oo2);
                                } else if(oo1==null){
                                    diff=-1;
                                } else {
                                    diff=1;
                                }
                                
                                return -diff;
                            }
                        });
                        
                        
                        for (Partido partido : partidos) {
                            Equipo equipo1 = partido.getEquipo1();
                            Equipo equipo2 = partido.getEquipo2();

                            // Si el equipo del partido es el que yo busco, lo muestro (o lo imprimo con otro color):
                            if(equipo1.getNombre().compareTo(equipo)==0||
                                    equipo2.getNombre().compareTo(equipo)==0){
                                   
                            System.out.println("\t\t\t " + partido.getFechaString() + "," + partido.getHoraString()
                                    + " en " + partido.getLugar() + ", " + equipo1.getNombre() + " vs " + equipo2.getNombre()
                                    + " ( " + partido.getEstado() + " " + partido.getResultadoEquipo1() + " - " + partido.getResultadoEquipo2() + ")");

                            }
                        }
                    }
                }
    }
    
    
        private List<Fase> obtenerFases(String categoriaId, String deporteId) {
        List<Fase> listaFases = null;

        try {

            String respuesta = "";

            //-------------------------------
            // Aqu� hay que cambiar:
            String url = baseUrl + "/categorias/" + categoriaId + "/deportes/" + deporteId + "/calendarios";

            respuesta = leerURL(url);
            //---------------------------
            // Aqu� hay que cambiar
            // Intentamos interpertarlo con jackson:
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Ejemplo para recibir una lista de objetos:
            listaFases = mapper.readValue(respuesta, new TypeReference<List<Fase>>(){});

        } catch (IOException ex) {
            Logger.getLogger(ClienteTorneos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaFases;

    }

    
     public static void main(String[] args) {
        new CienteTorneoFiltrarPorEquipo("trofeo-rector-1a-division-2013","futbol-11", "ETSI Informática y Telecomunicacion");
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
            Logger.getLogger(ClienteTorneos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClienteTorneos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return respuesta;
    }

    
    
}
