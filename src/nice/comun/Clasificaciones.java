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
public class Clasificaciones {
    String url;
    private Deporte deporte;
    List<Fase> fases;
    
    Clasificaciones(String url) {
        this.url=url;
        fases=new ArrayList<Fase>();
    }

    void consultarWeb() {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements fases_ = doc.select(".fase");
            
            System.out.println("Calendario: "+url);
            
            // Identificamos las fases o grupos:
            for(Element fase_:fases_){
                // Fase:
                Fase fase = new Fase(fase_.select("h2.titulo2").first().text());
                fases.add(fase);
                
                // tablas por cada ronda:
                Elements fechasRondas = fase_.select("#equipos");
               
                
                for(Element tabla:fechasRondas){
                Element ronda_ = tabla.previousElementSibling();
                Ronda ronda=new Ronda(ronda_.text());
                fase.addRonda(ronda);
                    
                    Elements partidos_ = tabla.select("tbody > tr");
                    
                    // Por cada partido de cada ronda:
                    for(Element partido_:partidos_){
                        Elements campos = partido_.select("td");
                        
                        if(campos.size()>1){
                            // Si es de más info, no lo mostramos:
                            if(campos.first().className().compareTo("mas_info")==0){
                                
                            } else {
                                Partido partido=new Partido();
                                
                                // coge cada campo de la fila de la tabla de partidos:
                                partido.setFecha(campos.get(0).text());
                                partido.setHora(campos.get(1).text());
                                partido.setLugar(campos.get(2).text());
                                partido.setEquipo1(campos.get(3).select("a").text(), 
                                        campos.get(3).select("a").attr("abs:href"));
                                 partido.setEquipo2(campos.get(4).select("a").text(), 
                                        campos.get(4).select("a").attr("abs:href"));
                                 partido.setEstado(campos.get(5).text());
                                 
                                 // Interpretamos los resultados:
                                 String [] resultado=campos.get(6).text().trim().split("-");
                                       
                                 if(resultado.length>1){
                                     partido.setResultadoEquipo1(resultado[0]);
                                     partido.setResultadoEquipo2(resultado[1]);
                                 }
                                 
                                 ronda.add(partido);
                            }
                        } else {
                            System.err.println("Campos nulos?");
                        }
                    }
                }
            }
        } catch (IOException ex) {
            // Considerar las conexiones que expiran: por no existir? Por trabajo del servidor?
            Logger.getLogger(Clasificaciones.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error al leer? "+ex+" "+url);
        }
    }

    void setDeporte(Deporte deporte) {
       this.deporte=deporte;
    }

    Deporte getDeporte() {
     return deporte;
    }

    List<Fase> getFases() {
        return fases;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
