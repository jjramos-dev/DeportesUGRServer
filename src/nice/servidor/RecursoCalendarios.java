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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nice.comun.Fase;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Clase requerida por RESTlet para servir los recursos de la clase
 * <code>Fase</code>
 *
 * @author jjramos
 */
public class RecursoCalendarios extends ServerResource {

    NiceServer servicioRestTorneos = null;
    private String categoria;
    private String deporte;

    private ObjectMapper mapper;
    List<Fase> fases;

    /**
     * Inicialización del recurso. Requiere inicializar los objetos que se
     * utilizarán en la serialización, y recoge los parámetros introducidos en
     * las URL del recurso.
     */
    public void doInit() {

        mapper = new ObjectMapper();
        // Para que no falle aunque no tenga un constructtor vacío, ni getter y setters públicos:
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        servicioRestTorneos = (NiceServer) getApplication();

        // Parámetro obtenido de la URL
        this.categoria = getAttribute("categoria");
        this.deporte = getAttribute("deporte");
        fases = servicioRestTorneos.getFases(categoria, deporte);
    }

    /**
     * Método que devuelve el recurso serializado con representación JSON.
     *
     * @return String con la representación en JSON del recurso.
     */
    @Get("json")
    public String devolver() {
        String serializado = null;
        System.out.println("Devolver!");
        try {

            // Serializa el objeto mediante Jackson.
            serializado = mapper.writeValueAsString(fases);

        } catch (JsonProcessingException ex) {
            Logger.getLogger(RecursoCalendarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return serializado;
    }
}
