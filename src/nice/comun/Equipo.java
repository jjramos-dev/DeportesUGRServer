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
 * Clase que encapsula la información de un equipo. Se diseña para poder usarlo
 * como POJO serializable simple para las respuestas JSON desde el servidor.
 *
 * @author jjramos
 */
public class Equipo {

    private String nombre;
    private String url;

    /**
     * Constructor vacío. Necesario para serializarlo mediante Jackson.
     */
    Equipo() {

    }

    /**
     * Constructor con todos los parámetros de la clase.
     *
     * @param nombre Nombre del equipo.
     * @param url URL de la web de CAD correspondiente al deporte.
     */
    public Equipo(String nombre, String url) {
        this.nombre = nombre;
        this.url = url;
    }

    /**
     *
     * Getter del Nombre del equipo.
     *
     * @return Nombre del equipo.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Getter de la URL del equipo.
     *
     * @return URL del equipo.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter de la nombre del equipo.
     *
     * @param nombre Nombre del equipo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Setter de la URL del equipo.
     *
     * @param url URL del equipo en la web del CAD.
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
