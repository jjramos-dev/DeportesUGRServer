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

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nice.comun.Deporte;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 *
 * @author jjramos
 */
public class RecursoPerfilesDeportes extends ServerResource{
     
  NiceServer servicioRestTorneos=null;
    private ObjectMapper mapper;
    List<String> deportes;
    
  public void doInit() {

      mapper=new ObjectMapper(); 
      // Para que no falle aunque no tenga un constructtor vacío, ni getter y setters públicos:
      mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      servicioRestTorneos=(NiceServer) getApplication();
          
      deportes=servicioRestTorneos.getDeportes();
    }
    
    
    @Get("json")
    public String devolver() {
        String serializado=null;
 
        try {
        
            serializado=mapper.writeValueAsString(deportes);
      
        } catch (JsonProcessingException ex) {
          Logger.getLogger(RecursoPerfilesDeportes.class.getName()).log(Level.SEVERE, null, ex);
      }
            
      
        
        return serializado;
    }
}
