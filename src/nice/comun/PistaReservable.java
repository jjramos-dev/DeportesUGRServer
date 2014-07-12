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
 *
 * @author jjramos
 */
public class PistaReservable {

    private String codigo = null;
    private String titulo = null;

    /**
     * 
     */
    PistaReservable() {

    }

    /**
     * 
     * @param codigoPista
     * @param pista 
     */
    PistaReservable(String codigoPista, String pista) {
        this.codigo = codigoPista;
        this.titulo = pista;
    }

    /**
     * 
     * @return 
     */
    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    /**
     * 
     * @param codigo 
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * 
     * @param titulo 
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
