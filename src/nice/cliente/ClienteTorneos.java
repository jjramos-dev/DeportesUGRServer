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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nice.comun.DatosCategoria;
import nice.comun.Deporte;
import nice.comun.Equipo;
import nice.comun.Fase;
import nice.comun.Partido;
import nice.comun.Ronda;


/*
 * @author jjramos
 */
public class ClienteTorneos {

    private String baseUrl = "http://localhost:8080";

    public ClienteTorneos() {

        // Consulto las categor�as del a�o 2013:
        List<DatosCategoria> categorias = obtenerTorneos("2013");

        for (DatosCategoria datoCategoria : categorias) {
            System.out.println(datoCategoria.getAnio() + " " + datoCategoria.getTitulo() + " -> " + datoCategoria.getUrl() + " " + datoCategoria.getId());

            // Mostramos, para cada categor�a, mostramos los deportes disponibles:
            List<Deporte> deportes = obtenerDeportes(datoCategoria.getId());
            for (Deporte deporte : deportes) {
                System.out.println("\t" + deporte.getTitulo() + " -> " + deporte.getUrl() + " ID: " + deporte.getId());

                // Y por qu� no? Vamos a mostrar la informaci�n de los partidos:
                List<Fase> fases = obtenerFases(datoCategoria.getId(), deporte.getId());
                for (Fase fase : fases) {
                    List<Ronda> rondas = fase.getRondas();
                    System.out.println("\t\tFase: " + fase.getTitulo());

                    for (Ronda ronda : rondas) {
                        List<Partido> partidos = ronda.getPartidos();
                        System.out.println("\t\t - Ronda: " + ronda.getTitulo());

                        for (Partido partido : partidos) {
                            Equipo equipo1 = partido.getEquipo1();
                            Equipo equipo2 = partido.getEquipo2();

                            System.out.println("\t\t\t " + partido.getFechaString() + "," + partido.getHoraString()
                                    + " en " + partido.getLugar() + ", " + equipo1.getNombre() + " vs " + equipo2.getNombre()
                                    + " ( " + partido.getEstado() + " " + partido.getResultadoEquipo1() + " - " + partido.getResultadoEquipo2() + ")");

                        }

                    }
                }
            }
        }

        // 
    }

    public static void main(String[] args) {
        new ClienteTorneos();
    }

    private List<DatosCategoria> obtenerTorneos(String anio) {
        List<DatosCategoria> listaCategorias = null;

        try {

            String respuesta = "";

            //-------------------------------
            // Aqu� hay que cambiar:
            String url = baseUrl + "/torneos/" + anio;

            respuesta = leerURL(url);
            //---------------------------
            // Aqu� hay que cambiar
            // Intentamos interpertarlo con jackson:
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Ejemplo para recibir una lista de objetos:
            listaCategorias = mapper.readValue(respuesta, new TypeReference<List<DatosCategoria>>() {
            });

        } catch (IOException ex) {
            Logger.getLogger(ClienteTorneos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaCategorias;
    }

    private List<Deporte> obtenerDeportes(String id) {
        List<Deporte> listaDeportes = null;

        try {

            String respuesta = "";

            //-------------------------------
            // Aqu� hay que cambiar:
            String url = baseUrl + "/categorias/" + id + "/deportes";

            respuesta = leerURL(url);
            //---------------------------
            // Aqu� hay que cambiar
            // Intentamos interpertarlo con jackson:
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Ejemplo para recibir una lista de objetos:
            listaDeportes = mapper.readValue(respuesta, new TypeReference<List<Deporte>>() {
            });

        } catch (IOException ex) {
            Logger.getLogger(ClienteTorneos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaDeportes;

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

}

