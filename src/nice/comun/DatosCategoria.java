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

/**
 * Clase que encapsula la información de un torneo o <code>Categoria</code>. Se
 * diseña para poder usarlo como POJO serializable simple para las respuestas
 * JSON desde el servidor.
 *
 * @author jjramos
 */
public class DatosCategoria {

    String titulo;
    private String url;
    private String urlNormativa = null;
    private String id;
    private String anio;

    /**
     * Constructor vacío. Necesario para serializarlo mediante Jackson.
     */
    public DatosCategoria() {

    }

    /**
     * Constructor de la clase con todos atributos.
     *
     * @param id Identificador del torneo o <code>Categoria</code>.
     * @param titulo Nombre del torneo.
     * @param url URL de la web del CAD con información del torneo.
     * @param urlNormativa URL del documento que especifica la normativa del
     * torneo.
     * @param anio Año del torneo.
     * @see Categoria
     */
    public DatosCategoria(String id, String titulo, String url, String urlNormativa, String anio) {
        this.id = id;
        this.titulo = titulo;
        this.url = url;
        this.urlNormativa = urlNormativa;
        this.anio = anio;
    }

    /**
     * Getter de <code>titulo</codigo>, el nombre del torneo.
     *
     * @return El nombre del torneo.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Setter de <code>titulo</codigo>, el nombre del torneo.
     *
     * @param titulo El nombre del torneo.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Getter de la URL de la web del torneo.
     *
     * @return URL de la web del torneo dentro de la página del CAD.
     * @see Categoria
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter de la URL de la web del torneo.
     *
     * @param url URL de la web del torneo dentro de la página del CAD.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter de <code>urlNormativa</code>, la URL de la normativa del torneo.
     *
     * @return La URL de la normativa del torneo.
     */
    public String getUrlNormativa() {
        return urlNormativa;
    }

    /**
     * Setter de <code>urlNormativa</code>, la URL de la normativa del torneo.
     *
     * @param urlNormativa La URL de la normativa del torneo.
     */
    public void setUrlNormativa(String urlNormativa) {
        this.urlNormativa = urlNormativa;
    }

    /**
     * Getter del identificador <code>id</code> del torneo.
     *
     * @return <code>id</code> del torneo.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter del identificador <code>id</code> del torneo.
     *
     * @param id Identificador del torneo.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter del año del torneo.
     *
     * @return Año del torneo, con formato: "AAAA".
     * @see Categoria#anio
     */
    public String getAnio() {
        return anio;
    }

    /**
     * Setter del año del torneo.
     *
     * @param anio Año del torneo, con formato: "AAAA".
     * @see Categoria#anio
     */
    public void setAnio(String anio) {
        this.anio = anio;
    }

}
