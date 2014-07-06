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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nice.comun;

/**
 *
 * @author jjramos
 */
public class Deporte {
    private String titulo;
    private String url;
    private String id;
    
    public Deporte(){
        
    }
    
    public Deporte(String titulo, String url){
        this.titulo=titulo;
        this.url=url;
        
            // Como identificador le ponemos la última parte de la url:
            String[] id_ = url.split("/");
            //System.out.println(url);
            if(id_.length>1){
                id=id_[id_.length-1];
            } else {
                id="";
                
            }
            
            System.out.println("Deporte: "+titulo+" -> "+id);
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
