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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jjramos
 */
public class Ronda {
    private String titulo;
    private List<Partido> partidos;

    Ronda(){
        
    }
    
    Ronda(String text) {
        this.titulo=text;
        this.partidos=new ArrayList<Partido>();
    }

    void add(Partido partido) {
        partidos.add(partido);
    }

    public String getTitulo() {
        return titulo;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }
    
}
