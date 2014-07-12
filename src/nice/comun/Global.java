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
public class Global {

    public static final String baseUrlListaPistasReservables="http://oficinavirtual.ugr.es/CarneUniversitario/TarjetasDeportes.jsp?textoXML=<xml><numero_PIU></numero_PIU><solicitud><dni>janderclander</dni><nia></nia><tarjeta_deportiva>1</tarjeta_deportiva><pago_viable>0</pago_viable><saldo_monedero>0</saldo_monedero></solicitud></xml>#";
    public static final String baseUrlTorneos="http://cad.ugr.es/static/CADManagement";
    
    /**
     * Devuelve la URL para acceder a la página de listado de pistas, 
     * dada una acreditación general.
     * @param dni DNI admitido en el sistema.
     * @param cadId Identificador de tarjeta de CAD.
     * @return Devuelve la URL de la web de reservas.
     */
    public static String getBaseUrlListaPistasReservables(String dni, String cadId) {
        String url=baseUrlListaPistasReservables;
        url=url.replaceAll("janderclander", dni);
        return url;
    }
    
}
