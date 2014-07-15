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
 * Clase que encapsula la información de un <code>Equipo</code>. Se diseña para
 * poder usarlo como POJO serializable simple para las respuestas JSON desde el
 * servidor.
 *
 * @author jjramos
 */
public class DatosEquipo {

    Equipo equipo;
    String tituloCategoria;
    String tituloDeporte;

    /**
     * Constructor con todos los parámetros.
     *
     * @param equipo <code>Equipo</code> del que se recogen los datos.
     * @param tituloCategoria Nombre del torneo en el que juega el equipo.
     * @param tituloDeporte Nombre del deporte al que juega el equipo.
     */
    public DatosEquipo(Equipo equipo, String tituloCategoria, String tituloDeporte) {
        this.equipo = equipo;
        this.tituloCategoria = tituloCategoria;
        this.tituloDeporte = tituloDeporte;
    }

    /**
     * Getter del equipo.
     *
     * @return Equipo.
     */
    public Equipo getEquipo() {
        return equipo;
    }

    /**
     * Setter del equipo.
     *
     * @param equipo Equipo.
     */
    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    /**
     * Getter del nombre del deporte.
     *
     * @return Nombre del deporte.
     */
    public String getTituloDeporte() {
        return tituloDeporte;
    }

    /**
     * Setter del nombre del deporte.
     *
     * @param tituloDeporte Nombre del deporte.
     */
    public void setTituloDeporte(String tituloDeporte) {
        this.tituloDeporte = tituloDeporte;
    }

    /**
     * Getter del nombre del torneo.
     *
     * @return Nombre del torneo.
     */
    public String getTituloCategoria() {
        return tituloCategoria;
    }

    /**
     * Setter del nombre del torneo.
     *
     * @param tituloCategoria Nombre del torneo.
     */
    public void setTituloCategoria(String tituloCategoria) {
        this.tituloCategoria = tituloCategoria;
    }

}
