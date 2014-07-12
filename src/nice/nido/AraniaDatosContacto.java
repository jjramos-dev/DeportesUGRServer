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

package nice.nido;

import java.util.ArrayList;
import java.util.List;
import nice.comun.Campus;
import nice.comun.DatosContacto;
import nice.comun.Global;
import nice.comun.Instalacion;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author jjramos
 * 
 * Obtiene los datos de contacto en la web del CAD para cada campus donde hay instalaciones deportivas.
 * 
 */
public class AraniaDatosContacto extends Arania {
    static private String baseUrl=Global.baseUrlDatosContacto;

    
    public AraniaDatosContacto(String baseUrl) {
        this.baseUrl=baseUrl;
    }

    public AraniaDatosContacto() {
    }
    
    public List<DatosContacto> explorar(){
        List<DatosContacto> listaCampus=new ArrayList<DatosContacto>();
        
        // Descargamos la web, y la almacenamos como DOM
        Document doc=descargarPagina(baseUrl);
        
        // Buscamos Cada cuadro dentro de la sección de contenidos de la página (<code>id=pagina</code>):
        // cada tabla es de la clase "departamento" 
        Elements entradaOficinaCampus = doc.select(".departamento ");
        
        // Si la encontramos, miramos los enlaces, y nos 
        // quedamos con los que contengan "Campus":
        if(entradaOficinaCampus!=null){
            // Elements entradas=entradaOficinaCampus.first().select("<strong>");
            
            // Por cada entrada (campus): 
            for(Element entrada:entradaOficinaCampus){
                String nombreCampus=entrada.select("strong").first().text();
                Elements datos=entrada.select("li.level2 > .li");
                
                DatosContacto datosContacto=new DatosContacto();
                datosContacto.setTitulo(nombreCampus);
                    // Por cada entrada (datos para campus):
                    for(Element dato:datos){
       
                        // Buscamos la imagen:
                        Elements imagenUrl_=entrada.select("li img");
                        datosContacto.setImagen(imagenUrl_.attr("abs:src"));
                        
                        
                    // Buscamos el tipo de dato:
                    Elements titulo_ = dato.select("strong");
                    String titulo=titulo_.first().text();
                    // A la misma frase le quitamos parte strong:
                    String valor = dato.text().replace(titulo, "");
                        
                    // Dirección:
                        if(titulo.contains("Direcc")&&titulo.endsWith("n")){
                            datosContacto.setDireccion(valor);
                        } else if (titulo.contains("ax")){
                            datosContacto.setFax(extraerTelefono(valor));
                        } else if (titulo.contains("fno")||titulo.contains("fono")){
                            datosContacto.setTelefonos(extraerTelefonos(valor));
                        } else if(titulo.contains("rario")){
                            datosContacto.setHorario(valor);
                        } else if(titulo.contains("bicac")){
                            Elements enlace = dato.select("a[href]");
                        // Se escoge el atributo "href" con la URL absoluta:
                            datosContacto.setUbicacion(enlace.first().attr("abs:href"));
                        } 
                                }
                    // añadimos el contacto:
                    listaCampus.add(datosContacto);
            }
        }       
        return listaCampus;
    }   
    
    
    public static void main(String[] args) {
        AraniaDatosContacto arania = new AraniaDatosContacto(baseUrl);
        arania.explorar();
    }

    /**
     * Formatea un String que contiene un número de teléfono, para contener una representación más
     * compacta, con sólo dígitos.
     * @param valor String que representa al número de teléfono.
     * @return String con sólo dígitos.
     */
    private String extraerTelefono(String valor) {
        valor=valor.replaceAll("\\p{Punct}", "");
        valor=valor.replaceAll(" ", "");
        return valor;
    }

    /**
     * Formatea un String que contiene varios números de teléfono, para contener una representación más
     * compacta, con sólo dígitos.
     * @param valor String que representa a varios números de teléfono, separados por "," o "y".
     * @return Lista de Strings con sólo dígitos.
     */
    private List<String> extraerTelefonos(String valor) {
        List<String> listaTelefonos=new ArrayList<String>();
        
        String[] tokens = valor.split("[y,]");
        
        for(String token:tokens){
            listaTelefonos.add(extraerTelefono(token));
        }
        
        return listaTelefonos;
    }
}
