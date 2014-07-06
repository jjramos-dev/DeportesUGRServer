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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
 *
 * @author jjramos
 */
public class ListaPistasReservables {
     private String baseURL;
    private List<PistaReservable> listaPistasReservables;

    public ListaPistasReservables(String string) {
        this.baseURL=string;     
        listaPistasReservables=null;
    }

    public List<PistaReservable> getListaPistasReservables() {
        
        return listaPistasReservables;
    }
    
      public void consultarWeb(){
       
        try {
            Document doc = Jsoup.connect(baseURL).get();
            
            
             Elements pistas_=doc.select(".selectPIU > option");
             
            if (!pistas_.isEmpty()) {
                listaPistasReservables = new ArrayList<PistaReservable>();
                for (Element pista_ : pistas_) {
                    String pista = pista_.text();
                    String codigoPista = pista_.attr("value");

                    PistaReservable pr = new PistaReservable(codigoPista, pista);
                    listaPistasReservables.add(pr);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Reservas.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    } 

}
