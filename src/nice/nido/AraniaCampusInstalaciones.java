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

import java.util.ArrayList;
import java.util.List;
import nice.comun.Campus;
import nice.comun.Instalacion;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author jjramos
 *
 * Obtiene la lista de campus indicadas en la web del CAD, sección de
 * "instalaciones". Actualizado a 13/06/2014
 */
public class AraniaCampusInstalaciones extends Arania {

    static private String baseUrl = "http://cad.ugr.es/pages/instalaciones_deport/instalaciones";

    /**
     * Constructor con la URL base de la página web de instalaciones.
     *
     * @param baseUrl URL de la página de instalaciones.
     */
    public AraniaCampusInstalaciones(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<Campus> explorar() {
        List<Campus> listaCampus = new ArrayList();

        // Descargamos la web, y la almacenamos como DOM
        Document doc = descargarPagina(baseUrl);

        // Buscamos la entrada en el menú que ponga: "li.mainmenu_itemname_instalaciones"
        Elements entradaInstalaciones = doc.select("li.mainmenu_itemname_instalaciones");

        // Si la encontramos, miramos los enlaces, y nos 
        // quedamos con los que contengan "Campus":
        if (entradaInstalaciones != null) {
            Elements entradas = entradaInstalaciones.first().select("a");

            // Por cada enlace...
            for (Element entrada : entradas) {
                String enlace = entrada.attr("abs:href");
                String texto = entrada.text();

                // Si tiene la subcadena "ampus":
                if (texto.contains("ampus")) {
                    AraniaInstalacion arania = new AraniaInstalacion(enlace, texto);

                    System.out.println(enlace + " -> " + texto);
                }
            }
        }
        //System.out.println("---> "+e0.first().text());

        List<Instalacion> listaInstalaciones = null;

        return listaCampus;
    }

    /**
     * Método para lanzar desde línea de comandos la araña.
     *
     * @param args
     */
    public static void main(String[] args) {
        AraniaCampusInstalaciones arania = new AraniaCampusInstalaciones(baseUrl);
        arania.explorar();
    }
}
