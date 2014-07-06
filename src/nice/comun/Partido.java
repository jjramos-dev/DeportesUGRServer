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

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jjramos
 */
public class Partido {
    private Date fecha=null;
    private String fechaString=null;
    private String horaString=null;
    private Date hora=null;
    private String lugar;
    private Equipo equipo1;
    private Equipo equipo2;
    private String estado;
    private String resultadoEquipo1=null;
    private String resultadoEquipo2=null;
    

    public Partido() {
    }

    public Date getFecha() {
        return fecha;
    }

    @JsonIgnore
    void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFechaString() {
        return fechaString;
    }

    public void setFechaString(String fechaString) {
        this.fechaString = fechaString;
    }

    public String getHoraString() {
        return horaString;
    }

    public void setHoraString(String horaString) {
        this.horaString = horaString;
    }

    public Date getHora() {
        return hora;
    }

    @JsonIgnore
    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Equipo getEquipo1() {
        return equipo1;
    }

    public void setEquipo1(Equipo equipo1) {
        this.equipo1 = equipo1;
    }

    public Equipo getEquipo2() {
        return equipo2;
    }

    public void setEquipo2(Equipo equipo2) {
        this.equipo2 = equipo2;
    }

    void setFecha(String text) {
        
        this.fechaString=text;
            
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fecha = formatter.parse(text);
            
        } catch (ParseException ex) {
            //Logger.getLogger(Partido.class.getName()).log(Level.SEVERE, null, ex);
            fecha=null;
        }
    }

    void setHora(String text) {
        this.horaString=text;
        
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            hora = formatter.parse(text);
        } catch (ParseException ex) {
            hora=null;
        }
    }

    public void setLugar(String text) {
        lugar=text;
    }

    void setEquipo1(String nombre, String url) {
        equipo1=new Equipo(nombre,url);
    }

     void setEquipo2(String nombre, String url) {
         equipo2=new Equipo(nombre,url);
    }

    void setEstado(String text) {
        estado=text;
    }

    void setResultadoEquipo1(String string) {
        resultadoEquipo1 = string;
    }
    
    void setResultadoEquipo2(String string) {
        resultadoEquipo2 = string;
    }

    public String getLugar() {
        return lugar;
    }

    public String getEstado() {
        return estado;
    }

    public String getResultadoEquipo1() {
       return resultadoEquipo1;
    }
    
    public String getResultadoEquipo2() {
       return resultadoEquipo2;
    }

}
