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
 * Clase que encapsula la lista de pistas reservables de la web de reservas.
 *
 * @author jjramos
 */
public class ListaPistasReservables {

// URL de la web de reservas.
    private String baseURL;
    private List<PistaReservable> listaPistasReservables;

    /**
     * Constructor con URL de la página con la información de reservas.
     *
     * @param baseURL URL base de la web de las reservas.
     */
    public ListaPistasReservables(String baseURL) {
        this.baseURL = baseURL;
        listaPistasReservables = null;
    }

    /**
     * Obtiene la lista de pistas reservables.
     *
     * @return Lista de pistas reservables, o <code>null</code> si no se pudo
     * inicializar o falló al acceder a la web de reservas.
     */
    public List<PistaReservable> getListaPistasReservables() {
        return listaPistasReservables;
    }

    /**
     * Lanza la petición para extraer la información de la web de reservas.
     *
     */
    public void consultarWeb() {

        try {
            // Se conecta a la web de reservas, e interpreta su código HTML.
            Document doc = Jsoup.connect(baseURL).get();

            // Busca todos los elementos de la clase .selectPIU que esté seguido 
            // de un elemento del tipo "option":
            Elements pistas_ = doc.select(".selectPIU > option");

            // Si se encuentra alguno de estos elementos:
            if (!pistas_.isEmpty()) {

                listaPistasReservables = new ArrayList<PistaReservable>();
                // Por cada pista definida, se obtiene su texto y el código de la pista
                for (Element pista_ : pistas_) {
                    String pista = pista_.text();
                    String codigoPista = pista_.attr("value");

                    // Se crea un objeto de la clase "Pista", con su código y el nombre
                    // y se añade a la lista de pistas:
                    PistaReservable pr = new PistaReservable(codigoPista, pista);
                    listaPistasReservables.add(pr);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Reservas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
