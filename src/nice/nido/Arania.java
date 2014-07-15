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
package nice.nido;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Clase básica con los métodos comunes para las arañas.
 *
 * @author jjramos
 */
public class Arania {

    /**
     * Método que realiza la descarga de una página web y que devuelve el
     * documento DOM interpretado por JSoup.
     *
     * @param url URL de la página web descargada.
     * @return Documento DOM correspondiente a la web descargada.
     */
    public Document descargarPagina(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();

        } catch (IOException ex) {
            Logger.getLogger(Arania.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doc;
    }
}
