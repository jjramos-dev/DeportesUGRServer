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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

    // Constantes:
    public static final String STRING_CAM_CARTUJA="C. CARTUJA";
    public static final String STRING_CAM_CARTUJA_ABREV=" CAR";
    public static final String STRING_CAM_FUENTENUEVA="C. FUENTENUEVA";
    public static final String STRING_CAM_FUENTENUEVA_ABREV=" FUE";
    
    
    
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

                    // Se le da un formato más adecuado al nombre de la pista:
                    pista=embellecerTituloPista(pista);
                    
                    // Se crea un objeto de la clase "Pista", con su código y el nombre
                    // y se añade a la lista de pistas:
                    PistaReservable pr = new PistaReservable(codigoPista, pista);
                    listaPistasReservables.add(pr);
                }
                
                // Las ordenamos según título de la pista:
                Collections.sort(listaPistasReservables,new Comparator<PistaReservable>() {

                    @Override
                    public int compare(PistaReservable o1, PistaReservable o2) {
                       String nombre1="",nombre2="";
                       if(o1!=null){
                           nombre1=o1.getTitulo();
                       } else {
                           nombre1="1";                           
                       }
                       
                       if(o2!=null){
                           nombre2=o2.getTitulo();
                       } else {
                           nombre2="2";                           
                       }
                       
                       return nombre2.compareTo(nombre1);
                    }
                });

           }

        } catch (IOException ex) {
            Logger.getLogger(Reservas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Embellece el formato del nombre de la pista pasada por argumento. Por ejemplo:
     * <i>"PC CAR - PABELLON (60min)"</i> &gt; <i>"C. CARTUJA - PABELLON (60min)"</i>.
     * Ojo! Si añaden más pistas, es necesario modificarlo.
     * @param pista String con el nombre de la pista.
     * @return String con el formato agradable para un humano.
     */
    private String embellecerTituloPista(String pista) {
       String titulo=pista;
       String campus="";
        String[] tokens = titulo.split("-");
        
        // Cambiamos el código principal a su cadena completa.
        if(tokens[0].contains(STRING_CAM_FUENTENUEVA_ABREV)){
            campus=STRING_CAM_FUENTENUEVA;
        } else if(tokens[0].contains(STRING_CAM_CARTUJA_ABREV)){
            campus=STRING_CAM_CARTUJA;
        }
        
        titulo=campus+" - "+tokens[1].trim();
       
       return titulo;
    }
}
