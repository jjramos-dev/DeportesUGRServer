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
    
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<String> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
    
    
}
