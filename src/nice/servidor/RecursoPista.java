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
package nice.servidor;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nice.comun.Pista;


/**
 *
 * @author jjramos
 */

public class RecursoPista extends ServerResource{
    private String pistaId;
    private String nombre;
    private float preUniSinLuz;
    private float preUniLuz;
    private float preNoUniSinLuz;
    private float preNoUniLuz;
    private float prePenaUniSinLuz;
    private float prePenaUniLuz;
    private float prePenaNoUniSinLuz;
    private float prePenaNoUniLuz;
    //private String numero;
    
    Pista pista=null;
    ObjectMapper mapper=new ObjectMapper(); // Esto no debería estar aquí, 
                                            // debería ser un objeto compartido
                                            // Pero de momento, lo ponemos.
                                            // Es un objeto Jackson, para generar
                                            // la representación JSON a partir
                                            // de un objeto.
    
    public void doInit() {
        // Parámetro obtenido de la URL
        this.pistaId = getAttribute("pistaid");
        //this.precio = getAttribute("precio");
        //this.numero = getAttribute("numero");
        //this.nombre = getAttribute(nombre);
        // Obtenemos el objeto que tiene el id anterior:
        this.pista = new Pista(pistaId, nombre, preUniSinLuz, preUniLuz, preNoUniSinLuz, preNoUniLuz, prePenaUniSinLuz, prePenaUniLuz, prePenaNoUniSinLuz, prePenaNoUniLuz); 
    }
    
    
    @Get("json")
    public String devolver() {
        String serializado=null;
        
        try {
            
            serializado=mapper.writeValueAsString(pista);
            
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RecursoPista.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return serializado;
    }
}
