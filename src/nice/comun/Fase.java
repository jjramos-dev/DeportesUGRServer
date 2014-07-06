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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jjramos
 */
public class Fase {
    private String titulo;
    //private List<Partido> partidos;
    private List<Ronda> rondas;

    
    Fase(){
        
    }
    
    public Fase(String fase) {
        this.titulo=fase;
        //this.partidos=new ArrayList<Partido>();
        this.rondas=new ArrayList<Ronda>();
    }

//    void addPartido(Partido partido) {
//        partidos.add(partido);
//    }

//    public List<Partido> getPartidos() {
//        return partidos;
//    }

    void addRonda(Ronda ronda) {
        rondas.add(ronda);
    }

    public String getTitulo() {
        return titulo;
    }

    public List<Ronda> getRondas() {
        return rondas;
    }
}
