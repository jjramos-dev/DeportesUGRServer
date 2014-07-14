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
package nice.comun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Clase que almacena toda la información relativa a un torneo. Se identifica
 * por un <code>id</code> único. Contiene una URL a la normativa del torneo, el
 * año al que pertenece, y los deportes y clasificaciones (partidos, calendarios
 * y resultados) asociados.
 *
 * @author jjramos
 */
public class Categoria {

    String titulo;
    private String url;
    private String urlNormativa = null;
    private List<Deporte> listaDeportes;
    private List<Clasificaciones> listaClasificaciones;
    private String id;
    private String anio;

    /**
     * Constructor de <code>Categoria</code> al que se le pasa como argumento la
     * URL base de la web del CAD que contiene información sobre la misma.
     *
     * @param url URL de la web del CAD que describe el torneo.
     */
    public Categoria(String url) {
        this.url = url;
        listaDeportes = new ArrayList<Deporte>();
        listaClasificaciones = new ArrayList<Clasificaciones>();
    }

    /**
     * Extrae de la web del CAD la información sobre los deportes definidos para
     * el torneo concreto, extrae la URL de la normativa del torneo, y por cada
     * deporte, lanza las arañas para extraer la información de los partidos y
     * los calendarios correspondientes (<code>Clasificaciones</code>).
     */
    public void consultarWeb() {
        try {

            Document doc = Jsoup.connect(url).get();
            Elements deportes = doc.select(".deporte > .accion > a");

            // Descargamos la URL de la normativa:
            Elements descarga = doc.select(".descarga > a");
            if (descarga != null && descarga.first() != null) {
                urlNormativa = descarga.first().attr("href");
            } else {
                urlNormativa = null;
            }

            // Por cada deporte, creamos una entrada:
            for (int i = 0; i < deportes.size(); i++) {
                // Se obtiene el elemento de deporte: 
                Element deporteE = deportes.get(i);

                // Un deporte tiene su nombre, y el enlace donde encontrar más información del recurso:
                Deporte deporte = new Deporte(deporteE.text(), deporteE.attr("abs:href"));

                // Añadimos el deporte a la lista de deportes definidos para este torneo.
                listaDeportes.add(deporte);

                // Creamos un objeto clasificación para este deporte, y le indicamos que 
                // extraiga de la web la información de los partidos.
                Clasificaciones clasificaciones = new Clasificaciones(deporte.getUrl() + "/calendario");
                clasificaciones.consultarWeb();

                clasificaciones.setDeporte(deporte);

                // Añadimos esta clasificación a la lista de clasificaciones.
                listaClasificaciones.add(clasificaciones);
            }

        } catch (IOException ex) {
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Getter de <code>id</code> (identificador del torneo).
     *
     * @return <code>id</code>
     */
    public String getId() {
        return id;
    }

    /**
     * Setter de <code>id</code> (identificador del torneo).
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Método de ayuda para mostrar todo el contenido de un objeto
     * <code>Categoria</code>.
     */
    void mostrar() {

        System.out.println("Normativa: " + this.urlNormativa);
        System.out.println("URL: " + this.url);
        System.out.println("Nombre: " + this.titulo);
        System.out.println("Año: " + this.getAnio());

        for (Deporte deporte : listaDeportes) {
            System.out.println("\t" + deporte.getTitulo() + " -> " + deporte.getUrl());

            // Buscamos las clasificaciones de cada deporte. Esto es súperineficiente!!!!! cambiar!!!
            for (Clasificaciones clasificaciones : listaClasificaciones) {
                // Sólo si es la clasificación para este deporte, lo mostramos:
                if (deporte == clasificaciones.getDeporte()) {
                    // Por cada fase,
                    for (Fase fase : clasificaciones.getFases()) {

                        System.out.println("\t\t" + fase.getTitulo());
                        for (Ronda ronda : fase.getRondas()) {
                            System.out.println("\t\t\t" + ronda.getTitulo());

                            for (Partido partido : ronda.getPartidos()) {
                                System.out.println("\t\t\t\tEl " + partido.getFechaString()
                                        + " a las " + partido.getHoraString()
                                        + " en " + partido.getLugar() + ", "
                                        + partido.getEquipo1().getNombre() + " vs "
                                        + partido.getEquipo2().getNombre() + " ("
                                        + partido.getEstado() + ") "
                                        + partido.getResultadoEquipo1() + "-" + partido.getResultadoEquipo2());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Getter de la <code>url</code> base (la dirección web de la información
     * del torneo en el CAD).
     *
     * @return <code>url</code>
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter de la <code>url</code> base (la dirección web de la información
     * del torneo en el CAD).
     *
     * @param url Url de la web del torneo.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter de la URL del documento de la normativa del torneo.
     *
     * @return URL de la normativa (generalmente de un fichero PDF).
     */
    public String getUrlNormativa() {
        return urlNormativa;
    }

    /**
     * Setter de la URL del documento de la normativa del torneo.
     *
     * @param urlNormativa
     */
    public void setUrlNormativa(String urlNormativa) {
        this.urlNormativa = urlNormativa;
    }

    /**
     * Getter de la lista de deportes definidas para el torneo.
     *
     * @return Lista de deportes definidas para el torneo.
     */
    public List<Deporte> getListaDeportes() {
        return listaDeportes;
    }

    /**
     * Setter de la lista de deportes definidas para el torneo.
     *
     * @param listaDeportes Lista de deportes definidas para el torneo.
     */
    public void setListaDeportes(List<Deporte> listaDeportes) {
        this.listaDeportes = listaDeportes;
    }

    /**
     * Getter de la lista de clasificicaciones (calendarios de partidos y
     * resultados)
     *
     * @return Lista de clasificaciones.
     */
    public List<Clasificaciones> getListaClasificaciones() {
        return listaClasificaciones;
    }

    /**
     * Setter de la lista de clasificicaciones (calendarios de partidos y
     * resultados)
     *
     * @param listaClasificaciones Lista de clasificaciones.
     */
    public void setListaClasificaciones(List<Clasificaciones> listaClasificaciones) {
        this.listaClasificaciones = listaClasificaciones;
    }

    /**
     * Getter del nombre del torneo.
     *
     * @return Nombre del torneo.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Getter de las fases definidas en las clasificaciones del torneo, para un
     * deporte dado, identificado por su <code>deporteId</code>.
     *
     * @param deporteId Identificador del deporte a consultar.
     * @return Lista de fases definidas para el deporte y torneo seleccionados.
     */
    public List<Fase> getFases(String deporteId) {

        List<Fase> fases = null;

// Buscamos las clasificaciones de cada deporte. Esto es súperineficiente!!!!! cambiar!!!
        for (Clasificaciones clasificaciones : listaClasificaciones) {
            // Sólo si es la clasificación para este deporte, lo mostramos:
            if (deporteId.compareTo(clasificaciones.getDeporte().getId()) == 0) {
                // Por cada fase,
                fases = clasificaciones.getFases();
            }
        }

        return fases;
    }

    /**
     * Setter del nombre del torneo
     *
     * @param text Nombre del torneo.
     */
    void setTitulo(String text) {
        titulo = text;
    }

    /**
     * Setter del año del torneo.
     *
     * @param anio Año en que comienza el curso del torneo, en formato "AAAA".
     * Por ej,: para un torneo en el curso 13/14, <code>anio="2013"</code>.
     */
    void setAnio(String anio) {
        this.anio = anio;
    }

    /**
     * Getter del año del torneo.
     *
     * @return Año en que comienza el curso del torneo, en formato "AAAA". Por
     * ej,: para un torneo en el curso 13/14, <code>anio="2013"</code>.
     */
    public String getAnio() {
        return anio;
    }

}
