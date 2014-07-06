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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class ListaPistasReservablesFechas {

    private String baseURL;
    private List<PistaReservable> listaPistasReservables;

    public ListaPistasReservablesFechas(String string) {
        this.baseURL = string;
        listaPistasReservables = null;
    }

    // Fecha con formato: dd-mm-aaaa
    public String consultarFecha(String id, String numeroFicha, String pista, String codigoPista, String fecha) {
        String tabla = "";

        try {
            String[] f = fecha.split("-");
            String mes = f[1];
            String anio = f[2];
            String dia = f[0];

            String query = "Login=&parametro=20&Llave=&NumExp=&NomFic=TarjetasDeportes&Password=&nombreUsuario=&tipoUsuario="
                    + "&dniDeportes=" + id + "&Volver=N&Anio=" + anio + "&Mes=" + mes + "&dni1=" + id + "&nia1=&numeroPIU1=&"
                    + "saldo_monedero=0&pago_viable=0&numeroFicha=" + numeroFicha
                    + "textoXML=<xml><numero_PIU></numero_PIU><solicitud><dni>" + id + "</dni><nia></nia><tarjeta_deportiva>1</tarjeta_deportiva><pago_viable>0</pago_viable><saldo_monedero>0</saldo_monedero></solicitud></xml>&textoXML1=<xml xml:space='preserve' >"
                    + "<reserva>"
                    + "<tipoActividadNombre>" + pista + "</tipoActividadNombre>"
                    + "<tipoActividadCodigo>" + codigoPista + "</tipoActividadCodigo>"
                    + "<numeroFicha>" + numeroFicha + "</numeroFicha>"
                    + "<fechaReserva>" + dia + "/" + mes + "/" + anio + "</fechaReserva>"
                    + "<pagoViable>0</pagoViable>"
                    + "<saldoMonedero>0</saldoMonedero>"
                    + "</reserva>"
                    + "</xml>"
                    + "&tipoOrigen=T&dia=" + dia + "&tipoActividad=" + codigoPista + "&fecha=";

            ///////////////////////////////////////////////
            URL oracle = new URL("http://oficinavirtual.ugr.es/CarneUniversitario/TarjetasDeportesXml.jsp");
            HttpURLConnection yc = (HttpURLConnection) oracle.openConnection();
       // set connection output to true
            // instead of a GET, we're going to send using method="POST"
            yc.setRequestMethod("POST");
            yc.setDoOutput(true);
            yc.setDoInput(true);

            PrintWriter out = new PrintWriter(yc.getOutputStream());

            out.println("Content-Type: application/x-www-form-urlencoded");
            out.println("Content-Length: " + query.length());
            out.println();
            out.println(query);

            out.flush();

            // Obtenemos la respuesta:
            String respuesta = "";

            if (yc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                boolean salir = false;
                BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                do {
                    String linea = in.readLine();

                    if (linea == null) {
                        salir = true;
                    } else {
                        respuesta = respuesta + linea;
                    }

                } while (!salir);
            }
            yc.disconnect();

            //////////////////////////////////////////////
            Document doc = Jsoup.parse(respuesta);

            Elements tabla_ = doc.select(".borde_estadisticas");
            tabla = "<head>"
                    + "<style>" + "td.celdaOcupada {"
                    + "background-color: #888888;" + // fondo de la tabla
                    "border: 1px ridge #DFE8F4;"
                    + "color: #000000;"
                    + "font-family: Arial,sans-serif;"
                    + "font-size: 7pt;"
                    + "font-weight: bold;"
                    + "padding: 1px 6px;" + // 3px 6px
                    "}"
                    + "</style>"
                    + "</head><table class=\"borde_estadisticas\" width=\"70%\" border=\"1\">" + tabla_.html() + "</table>";

            tabla = tabla.replaceAll("Ocupaci\uFFFDn", "Ocupaci&oacute;n");
            tabla = tabla.replaceAll("el d\uFFFDa", "el d&iacute;a");
            System.out.println(tabla);

            // Comprobamos si es día incorrecto:
            tabla = tabla.replaceAll("d\uFFFDas de antelaci\uFFFDn", "d&iacute;as de antelaci&oacute;n");
            if (tabla.contains("Se ha indicado una fecha anterior")) {
                // Crear tabla con error:
                tabla = "<html>\n"
                        + "<head>\n"
                        + "<meta content=\\\"text/html; charset=ISO-8859-1\\\"\n"
                        + "http-equiv=\\\"content-type\\\">\n"
                        + "<title></title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<div style=\\\"text-align: center;\\\"><span\n"
                        + "style=\\\"font-family: Helvetica,Arial,sans-serif;\\\"><br>\n"
                        + "&iexcl;Atenci&oacute;n! <br>\n"
                        + "<br>\n"
                        + "La fecha indicada ya ha pasado, no puede visualizarla. Seleccione\n"
                        + " fechas posteriores al d&iacute;a hoy.</span> <br>\n"
                        + "<br>\n"
                        + "</div>\n"
                        + "</body>\n"
                        + "</html>";

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date now = new Date();
                String strDate = sdf.format(now);

                tabla = tabla.replaceAll("hoy", strDate);
            } else if (tabla.contains("Demasiados d&iacute;as de antelaci")) {
                // Crear tabla con error:
                tabla = "<html>\n"
                        + "<head>\n"
                        + "<meta content=\\\"text/html; charset=ISO-8859-1\\\"\n"
                        + "http-equiv=\\\"content-type\\\">\n"
                        + "<title></title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<div style=\\\"text-align: center;\\\"><span\n"
                        + "style=\\\"font-family: Helvetica,Arial,sans-serif;\\\"><br>\n"
                        + "&iexcl;Atenci&oacute;n! <br>\n"
                        + "<br>\n"
                        + "El sistema de reservas del CAD no permite reservar con tanta\n"
                        + " antelaci&oacute;n. <br>\n"
                        + "Pruebe con una fecha m&aacute;s cercana.</span><br>\n"
                        + "<br>\n"
                        + "</div>\n"
                        + "</body>\n"
                        + "</html>";
            }

        } catch (IOException ex) {
            Logger.getLogger(Reservas.class.getName()).log(Level.SEVERE, null, ex);

        }

        return tabla;
    }
}
