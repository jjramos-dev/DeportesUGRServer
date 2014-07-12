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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static nice.comun.CadTorneosOk.baseURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author jjramos
 */
public class Reservas {

    private final String urlReservasPrincipal;

    Reservas(String urlReservasPrincipal) {
        this.urlReservasPrincipal = urlReservasPrincipal;
    }

    Reservas(String urlBase, String dni) {
        this.urlReservasPrincipal = "https://oficinavirtual.ugr.es/CarneUniversitario/TarjetasDeportes.jsp?textoXML=<xml><numero_PIU></numero_PIU><solicitud><dni>" + dni + "</dni><nia></nia><tarjeta_deportiva>1</tarjeta_deportiva><pago_viable>0</pago_viable><saldo_monedero>0</saldo_monedero></solicitud></xml>#";
    }

//    public String consultarFecha(String id, String numeroFicha, String pista, String codigoPista, String fecha) {
//        String tabla = "";
//
//        try {
//            String[] f = fecha.split("/");
//            String mes = f[1];
//            String anio = f[2];
//            String dia = f[0];
//
//            String query = "Login=&parametro=20&Llave=&NumExp=&NomFic=TarjetasDeportes&Password=&nombreUsuario=&tipoUsuario="
//                    + "&dniDeportes=" + id + "&Volver=N&Anio=" + anio + "&Mes=" + mes + "&dni1=" + id + "&nia1=&numeroPIU1=&"
//                    + "saldo_monedero=0&pago_viable=0&numeroFicha=" + numeroFicha
//                    + "textoXML=<xml><numero_PIU></numero_PIU><solicitud><dni>" + id + "</dni><nia></nia><tarjeta_deportiva>1</tarjeta_deportiva><pago_viable>0</pago_viable><saldo_monedero>0</saldo_monedero></solicitud></xml>&textoXML1=<xml xml:space='preserve' >"
//                    + "<reserva>"
//                    + "<tipoActividadNombre>" + pista + "</tipoActividadNombre>"
//                    + "<tipoActividadCodigo>" + codigoPista + "</tipoActividadCodigo>"
//                    + "<numeroFicha>" + numeroFicha + "</numeroFicha>"
//                    + "<fechaReserva>" + dia + "/" + mes + "/" + anio + "</fechaReserva>"
//                    + "<pagoViable>0</pagoViable>"
//                    + "<saldoMonedero>0</saldoMonedero>"
//                    + "</reserva>"
//                    + "</xml>"
//                    + "&tipoOrigen=T&dia=" + dia + "&tipoActividad=" + codigoPista + "&fecha=";
//
//            ///////////////////////////////////////////////
//            URL oracle = new URL("http://oficinavirtual.ugr.es/CarneUniversitario/TarjetasDeportesXml.jsp");
//            HttpURLConnection yc = (HttpURLConnection) oracle.openConnection();
//       // set connection output to true
//            // instead of a GET, we're going to send using method="POST"
//            yc.setRequestMethod("POST");
//            yc.setDoOutput(true);
//            yc.setDoInput(true);
//
//            PrintWriter out = new PrintWriter(yc.getOutputStream());
//
//            out.println("Content-Type: application/x-www-form-urlencoded");
//            out.println("Content-Length: " + query.length());
//            out.println();
//            out.println(query);
//
//            out.flush();
//
//            // Obtenemos la respuesta:
//            String respuesta = "";
//
//            if (yc.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                boolean salir = false;
//                BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//
//                do {
//                    String linea = in.readLine();
//
//                    if (linea == null) {
//                        salir = true;
//                    } else {
//                        respuesta = respuesta + linea;
//                    }
//
//                } while (!salir);
//            }
//            yc.disconnect();
//
//        //
//            // Interpretamos la página web devuelta, y extraemos la tabla de reservas:
//            //
//            Document doc = Jsoup.parse(respuesta);
//            Elements tabla_ = doc.select(".borde_estadisticas");
//
//            // La tabla la reformateamos para embellcerla:
//            tabla = "<head>"
//                    + "<style>" + "td.celdaOcupada {"
//                    + "background-color: #FF0000;"
//                    + "border: 1px ridge #DFE8F4;"
//                    + "color: #000000;"
//                    + "font-family: Arial,sans-serif;"
//                    + "font-size: 7pt;"
//                    + "font-weight: bold;"
//                    + "padding: 3px 6px;"
//                    + "}"
//                    + "a {text-decoration: none;}"
//                    + "</style>"
//                    + "</head><table class=\"borde_estadisticas\" width=\"70%\" border=\"1\">" + tabla_.html() + "</table>";
//            System.out.println(tabla);
//
//        } catch (IOException ex) {
//            Logger.getLogger(Reservas.class.getName()).log(Level.SEVERE, null, ex);
//
//        }
//
//        return tabla;
//    }

    public void consultarWeb() {

        try {
            Document doc = Jsoup.connect(urlReservasPrincipal).get();

            Elements pistas_ = doc.select(".selectPIU > option");

            for (Element pista_ : pistas_) {
                String pista = pista_.text();
                String codigoPista = pista_.attr("value");
                System.out.println(codigoPista + ": " + pista);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Reservas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
