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

import java.util.List;

/**
 * Clase que encapsula la información de contacto <code>DatosContacto</code>. Se
 * diseña para poder usarlo como POJO serializable simple para las respuestas
 * JSON desde el servidor.
 *
 * @author jjramos
 */
public class DatosContacto {

    String titulo;
    String direccion;
    String horario;
    String ubicacion;
    String imagen;
    List<String> telefonos;
    String fax;

    /**
     * Constructor vacío. Necesario para serializarlo mediante Jackson.
     */
    public DatosContacto() {
//        “titulo”: “CAMPUS UNIVERSITARIO DE FUENTENUEVA”,
//“direccion”: “Paseo Profesor Juan Ossorio s/n (Paseos Universitarios)”,
//“horario”: “De lunes a viernes de 9 a 14 horas y de 15 a 20 horas (hasta nuevo aviso, en
//horario de tarde permanecerá cerrado).”,
//“ubicacion”: “http://goo.gl/maps/CjNwJ”,
//“imagen”:
//“http://cad.ugr.es/pages/galeria_imagenes/instalaciones-cad/administracionfuentenueva/!”
//“telefono”: [{“numero”: “958240956”}, {“numero”: “958243144”}],
//“fax”:[{“numero”: “958243143”}
//    
    }

    /**
     * Getter de <code>titulo</codigo>, el nombre del campus
     *
     * @return El nombre del campus.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Setter de <code>titulo</codigo>, el nombre del campus
     *
     * @param titulo El nombre del campus.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Getter de la dirección de las oficinas administrativas del CAD en el
     * campus.
     *
     * @return dirección de las oficinas.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Setter de la dirección de las oficinas administrativas del CAD en el
     * campus.
     *
     * @param direccion dirección de las oficinas.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Getter del horario de atención de la oficina del CAD en el campus.
     *
     * @return Horario, en formato literal. P.e.: "De lunes a viernes de 9 a 14
     * horas y de 15 a 20 horas (hasta nuevo aviso, en horario de tarde
     * permanecerá cerrado)."
     */
    public String getHorario() {
        return horario;
    }

    /**
     * Setter del horario de atención de la oficina del CAD en el campus.
     *
     * @param horario Horario, en formato literal.
     */
    public void setHorario(String horario) {
        this.horario = horario;
    }

    /**
     * Getter de la ubicación de las oficinas, como URL de Google Maps.
     *
     * @return URL de Google Maps.
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Setter de la ubicación de las oficinas, como URL de Google Maps.
     *
     * @param ubicacion URL de Google Maps.
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Getter de la URL de la imagen de las oficinas del campus.
     *
     * @return URL de la imagen.
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Setter de la URL de la imagen de las oficinas del campus.
     *
     * @param imagen URL de la imagen.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Getter de la lista de teléfonos de la oficina del CAD en el campus.
     *
     * @return Lista de Strings con los números de teléfono.
     * @see DatosContacto#setFax(java.lang.String)
     */
    public List<String> getTelefonos() {
        return telefonos;
    }

    /**
     * Setter de la lista de teléfonos de la oficina del CAD en el campus.
     *
     * @param telefonos Lista de Strings con los números de teléfono.
     * @see DatosContacto#setFax(java.lang.String)
     */
    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }

    /**
     * Getter del número de teléfonos del fax de la oficina del CAD en el
     * campus.
     *
     * @return String con el número de fax. P.e.: "958244444"
     */
    public String getFax() {
        return fax;
    }

    /**
     * Setter del número de teléfonos del fax de la oficina del CAD en el
     * campus.
     *
     * @param fax String con el número de fax. P.e.: "958244444"
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

}
