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

package nice.servidor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nice.comun.DatosCategoria;
import nice.comun.PistaReservable;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 *
 * @author aulas
 */
public class RecursoPistasReservas extends ServerResource{
    private ObjectMapper mapper;
    private NiceServer servicioRestTorneos;
    private String anio;
    List<PistaReservable> pistasReservables;
    
      public void doInit() {
       
          mapper=new ObjectMapper(); 
      // Para que no falle aunque no tenga un constructtor vacío, ni getter y setters públicos:
      mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        servicioRestTorneos=(NiceServer) getApplication();
        
        
        pistasReservables=servicioRestTorneos.getListaPistasReservables();

      }

          @Get("json")
    public String devolver() {
            String serializado=null;
        
        try {
          
            serializado=mapper.writeValueAsString(pistasReservables);
    
          } catch (JsonProcessingException ex) {
            Logger.getLogger(RecursoPistasReservas.class.getName()).log(Level.SEVERE, null, ex);
        }
          return serializado;
        
    }

}
