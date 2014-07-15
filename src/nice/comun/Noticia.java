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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Clase que representa una noticia del tablón de noticias del CAD:
 *
 * @author jjramos
 */
public class Noticia {

    String url;
    String titulo = "Sin título";
    String textoHtml;
    private String imagenURL = null;

    final static String baseURLNoticias = "http://cad.ugr.es/pages/tablon/*";

    /**
     * Constructor vacío. Necesario para serializarlo mediante Jackson.
     */
    public Noticia() {

    }

    /**
     * Constructor que permite generar la URL a partir de los identificadores de
     * la noticia.
     *
     * @param tablon Identificador de la categoría de la noticia en el tablón
     * del CAD. P.e.: "competiciones".
     * @param noticiaId Identificador de la noticia.
     */
    public Noticia(String tablon, String noticiaId) {

        // Si se le pasa la URL de la noticia, la asigna directamente:
        if (noticiaId.startsWith("http")) {
            url = noticiaId;
        } else {
            // En caso contrario, la construye con la URL UniWeb del servicio CAD:
            url = baseURLNoticias + "/" + tablon + "/" + noticiaId;
        }
    }

    /**
     * Constructor con todos los parámetros de la clase.
     *
     * @param url URL de la noticia, dentro de la web del CAD.
     * @param titulo Título de la noticia.
     * @param pagina Contenido de la noticia, en formato HTML.
     */
    private Noticia(String url, String titulo, String pagina) {
        this.url = url;
        this.titulo = titulo;
        this.textoHtml = pagina;
    }

    /**
     * Método que extrae la información de la web del CAD, dada la URL de la
     * noticia.
     *
     * @return String con el código HTML de la noticia.
     */
    public Noticia consultaWeb() {
        Noticia noticia = null;
        try {
            // Se descargar la página web donde se incrusta la noticia, y la interpreta como DOM:
            Document doc = Jsoup.connect(url).get();

            // Extraer del DOM el título de la noticia. Dicho título está etiquetado
            // con el id "titulo_pagina". El texto delimitado por la etiqueta "<h1>" con dicha clase
            // es el título de la noticia:
            Elements tituloe = doc.select("#titulo_pagina");
            String titulo = tituloe.first().text();

            // El contenido de la noticia se delimita con un elemento <div> con el identificador "noticia"
            Elements pagina = doc.select("#noticia");
            // La imagen asociada está delimitada por la etiqueta <img> con la clase  "medialeft":
            Elements imagene = doc.select("img.medialeft");

            // Se actualzian los atributos de la clase:
            this.url = url;
            this.titulo = titulo;
            this.textoHtml = pagina.html();
            // attr permite extraer el parámetro del elemento representado en "imangene".
            this.imagenURL = imagene.attr("abs:src");

            noticia = this;

        } catch (IOException ex) {
            Logger.getLogger(Noticia.class.getName()).log(Level.SEVERE, null, ex);
        }

        return noticia;
    }

    /**
     * Getter de la URL de la imagen.
     *
     * @return URL de la imagen de la noticia.
     */
    public String getImagenURL() {
        return imagenURL;
    }

    /**
     * Setter de la URL de la imagen.
     *
     * @param imagenURL URL de la imagen de la noticia.
     */
    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    /**
     * Getter de la URL de la noticia en la web del CAD.
     *
     * @return URL de la noticia.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter de la URL de la noticia en la web del CAD.
     *
     * @param url URL de la noticia.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter del título de la noticia.
     *
     * @return Título de la noticia.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Setter del título de la noticia.
     *
     * @param titulo Título de la noticia.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Getter del contenido de la noticia en formato HTML.
     *
     * @return Contenido de la noticia en formato HTML.
     */
    public String getTextoHtml() {
        return textoHtml;
    }

    /**
     * Setter del contenido de la noticia en formato HTML.
     *
     * @param textoHtml Contenido de la noticia en formato HTML.
     */
    public void setTextoHtml(String textoHtml) {
        this.textoHtml = textoHtml;
    }
}
