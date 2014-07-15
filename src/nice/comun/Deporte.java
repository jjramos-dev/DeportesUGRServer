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
 * Clase que encapsula la información de un deporte. Se diseña para poder usarlo
 * como POJO serializable simple para las respuestas JSON desde el servidor.
 *
 * @author jjramos
 */
public class Deporte {

    private String titulo;
    private String url;
    private String id;

    /**
     * Constructor vacío. Necesario para serializarlo mediante Jackson.
     */
    public Deporte() {

    }

    /**
     * Constructor con todos los parámetros de la clase.
     *
     * @param titulo Nombre del deporte.
     * @param url URL de la web de CAD correspondiente al deporte.
     */
    public Deporte(String titulo, String url) {
        this.titulo = titulo;
        this.url = url;

        // Como identificador le ponemos la última parte de la url:
        String[] id_ = url.split("/");
        //System.out.println(url);
        if (id_.length > 1) {
            id = id_[id_.length - 1];
        } else {
            id = "";

        }

    }

    /**
     * Getter del Nombre del deporte.
     *
     * @return Nombre del deporte.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Getter de la URL del deporte.
     *
     * @return URL del deporte.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Getter del id del deporte.
     *
     * @return Identificador del deporte.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter del ide del deporte.
     *
     * @param id Identificador del deporte.
     */
    public void setId(String id) {
        this.id = id;
    }

}
