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
 * @author aulas
 */
public class Torneos {
    private String url;
    private List<Categoria> listaCategorias;
    
    public Torneos(String url){
        this.url=url;
        this.listaCategorias=new ArrayList<Categoria>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Categoria> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<Categoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }
    
    public void consultarWeb(){
        try {
            Document doc = Jsoup.connect(url).get();
            
            
            // Por cada año:
            Elements anios_=doc.select("#cuerpo_competicion");
            
            for(Element anio_:anios_){
                Elements selectorAnio_=anio_.select(".selector_ano > .titulo2");
                String anio=selectorAnio_.first().text();
                        
                System.out.println("Año: "+selectorAnio_.first().text());
                
            Elements categorias_ = anio_.select(".competicion > a");
        
            // Por cada competicion:
            for(Element categoria_:categorias_){
                Categoria categoria = new Categoria(categoria_.attr("abs:href"));
                categoria.setTitulo(categoria_.text());
                categoria.setAnio(anio);
                categoria.setId(getIdFromUrl(categoria.getUrl()));
                
                categoria.consultarWeb();
                
                listaCategorias.add(categoria);
            }
            }
        } catch (IOException ex) {
            Logger.getLogger(Torneos.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    } 

    public Categoria getCategoria(String categoriaId) {
        Categoria categoria=null;
        for(Categoria categoria_:listaCategorias){
            if(categoriaId.compareTo(categoria_.getId())==0){
                categoria=categoria_;
            }
        }
        
        return categoria;
    }

    private String getIdFromUrl(String url) {
        String id="";
        String []palabras=url.split("/");
        int l=palabras.length;
        
        if(palabras[l-1].compareTo("")==0){
            l--;
        }
        id=palabras[l-1];
        
        return id;
    }
}
