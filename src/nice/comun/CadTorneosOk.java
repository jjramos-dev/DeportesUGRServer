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
import java.util.List;

/**
 * Ejemplo de cliente que solicita información para un torneo concreto. Eliminar en próximas releases.
 * @author jjramos
 */
public class CadTorneosOk {

    static String baseURL="http://cad.ugr.es";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        new CadTorneosOk(baseURL+"/static/CADManagement/*/trofeo-rector-1a-division-2013");
    }

    private CadTorneosOk(String url) throws IOException {

        
        // Buscamos todas las categorías/Torneos:
        
        Torneos torneos=new Torneos("http://cad.ugr.es/static/CADManagement");
        torneos.consultarWeb();
//        Categoria categoria = new Categoria(url);      
//        categoria.consultarWeb();
        List<Categoria> categorias = torneos.getListaCategorias();
        for(Categoria categoria:categorias){
            categoria.mostrar();
        }    
    }
}
