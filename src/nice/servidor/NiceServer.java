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
package nice.servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nice.comun.Categoria;
import nice.comun.Clasificaciones;
import nice.comun.DatosCategoria;
import nice.comun.DatosEquipo;
import nice.comun.Deporte;
import nice.comun.Equipo;
import nice.comun.Fase;
import nice.comun.ListaPistasReservables;
import nice.comun.ListaPistasReservablesFechas;
import nice.comun.Noticia;
import nice.comun.Partido;
import nice.comun.PistaReservable;
import nice.comun.Ronda;
import nice.comun.Torneos;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jjramos
 */
public class NiceServer extends Application {

    private static HashMap<String, Categoria> baseDatosCategorias;
    private static Torneos torneos;
    private static List<PistaReservable> listaPistasReservables;
    private static String dni="X";
    private static String cadId="X";
    private static int puerto=8080;
    private static String rutaFicheroDatosParaReserva = "./niceserver.cfg";
    private static ConfiguracionServidorNice configuracion;

    private static List<PistaReservable> incializarBaseDeDatosPistasReservables(String string) {
        ListaPistasReservables pr = new ListaPistasReservables(string);

        pr.consultarWeb();
        List<PistaReservable> lista = pr.getListaPistasReservables();

        return lista;
    }

    public static void main(String[] args) {
        try {
            // Miramos si hay fichero de configuración:
            if (args.length >= 1) {
                rutaFicheroDatosParaReserva = args[0];
            }

            // Cargamos los datos de la web con las arañas...
            // Esto debería ser una base de datos...
            inicializarBaseDeDatos();

            // Creamos un componente de restlet, que escuchará mediante HTTP en 8080:
            Component componente = new Component();
            componente.getServers().add(Protocol.HTTP, puerto);

            // Para HTTPS:
            // http://restlet.com/learn/guide/2.2/core/security/https
            
            // Creamos una aplicación. ¿Se pueden crear varias aplicaciones para un componente? Sería lo suyo...
            Application aplicacion = new NiceServer();

            // Asignamos esta aplicación al componente, y lo iniciamos:
            componente.getDefaultHost().attachDefault(aplicacion);
            componente.start();

        } catch (Exception ex) {
            Logger.getLogger(NiceServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static int inicializarBaseDeDatos() {
        int error = 0;

            ////////////////////////////////////
        //Datos de prueba. Los lee de un fichero.
        configuracion = new ConfiguracionServidorNice(rutaFicheroDatosParaReserva);

        if (configuracion.getError() == 0) {
            dni = configuracion.getId();
            cadId = configuracion.getCadId();
            puerto=configuracion.getPuerto();
            
            ///////////////////////////////////
            listaPistasReservables = incializarBaseDeDatosPistasReservables("http://oficinavirtual.ugr.es/CarneUniversitario/TarjetasDeportes.jsp?textoXML=<xml><numero_PIU></numero_PIU><solicitud><dni>" + dni + "</dni><nia></nia><tarjeta_deportiva>1</tarjeta_deportiva><pago_viable>0</pago_viable><saldo_monedero>0</saldo_monedero></solicitud></xml>#");

           torneos = new Torneos("http://cad.ugr.es/static/CADManagement");

            // Comprobamos si tenemos nuestra base de datos:
            File db = new File("torneos.serial");

            if (true || !db.exists()) {
                FileOutputStream fout = null;
                try {

                    torneos.consultarWeb();
                    fout = new FileOutputStream("torneos.serial");
                    ObjectOutputStream oos = new ObjectOutputStream(fout);
                    oos.writeObject(torneos);

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(NiceServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(NiceServer.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        fout.close();
                    } catch (IOException ex) {
                        Logger.getLogger(NiceServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                FileInputStream fin = null;
                try {
                    // Si existe, lo leemos de fichero:
                    System.out.println("Leyendo base de datos:");
                    fin = new FileInputStream("torneos.serial");
                    ObjectInputStream oos = new ObjectInputStream(fin);
                    torneos = (Torneos) oos.readObject();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(NiceServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(NiceServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(NiceServer.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        fin.close();
                    } catch (IOException ex) {
                        Logger.getLogger(NiceServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

            Categoria categoria;
            baseDatosCategorias = new HashMap<String, Categoria>();

        // Copia lo siguiente por cada enlace de: 
//        categoria = new Categoria("http://cad.ugr.es/static/CADManagement/*/trofeo-rector-1a-division-2013");      
//        categoria.consultarWeb();
//        baseDatosCategorias.put(categoria.getTitulo(), categoria);
//        
        } else {
            error = 1;
            System.err.println("Error al leer la configuración...");
        }

        return error;
    }

    // Hace falta añadir estos ¿"filtros"? a la aplicación: 
    public Restlet createInboundRoot() {
        // Creamos una encamidor:
        Router encaminador = new Router(getContext());

        // enlaza cada recurso al encaminador:
        //encaminador.attach("/categorias/{categoria}", RecursoCategoria.class);
        encaminador.attach("/perfiles/deportes", RecursoPerfilesDeportes.class);
        encaminador.attach("/perfiles/equipos", RecursoPerfilesEquipos.class);
        encaminador.attach("/noticias/{noticiaid}/tablones/{tablon}", RecursoNoticia.class);
        encaminador.attach("/categorias/{categoria}/deportes", RecursoDeportes.class);
        encaminador.attach("/categorias/{categoria}/deportes/{deporte}/calendarios", RecursoCalendarios.class);
        encaminador.attach("/torneos/{anio}", RecursoCategorias.class);
        encaminador.attach("/pista/{pistaid}", RecursoPista.class);
        encaminador.attach("/reservas/pistas", RecursoPistasReservas.class);
        encaminador.attach("/reservas/pistas/{codigoPista}/fecha/{fecha}", RecursoPistasReservasFechas.class);
        encaminador.attach("/categorias/{categoria}/deportes/{deporte}/equipos", RecursoEquiposCategoriaDeporte.class);
        // encaminador.attach("/categorias/{categoria}/deportes", RecursoDeportes.class);

        // encaminador.attach("/pista/{pistaid}/precio", RecursoPrecio.class);
        // router.attach("/users/{user}/orders/{order}", OrderResource.class);
        // Devuelve la raíz del encaminador
        return encaminador;
    }

    List<Deporte> getDeportes(String categoriaId) {
        List<Deporte> listaDeportes = null;

        //Categoria categoria=baseDatosCategorias.get(categoriaId);
        Categoria categoria = torneos.getCategoria(categoriaId);

        listaDeportes = categoria.getListaDeportes();

        return listaDeportes;
    }

    List<Fase> getFases(String categoriaId, String deporteId) {
        List<Fase> fases = null;

        //  Categoria categoria=baseDatosCategorias.get(categoriaId);
        Categoria categoria = torneos.getCategoria(categoriaId);

        fases = categoria.getFases(deporteId);

        return fases;
    }

    List<Categoria> getCategorias(String anio) {
        List<Categoria> categorias = null;

        List<Categoria> categorias_ = torneos.getListaCategorias();

        for (Categoria categoria_ : categorias_) {
            if (anio.compareTo(categoria_.getAnio()) == 0) {
                categorias.add(categoria_);
            }
        }

        // Si no hay ninguno, es que no hay nada de ese año:
        if (categorias.isEmpty()) {
            categorias = null;
        }

        return categorias;
    }

    List<DatosCategoria> getListaCategorias(String anio) {
        List<DatosCategoria> categorias = new ArrayList<DatosCategoria>();

        List<Categoria> categorias_ = torneos.getListaCategorias();

        for (Categoria categoria_ : categorias_) {
            if (anio.compareTo(categoria_.getAnio()) == 0) {
                categorias.add(
                        new DatosCategoria(
                                categoria_.getId(),
                                categoria_.getTitulo(),
                                categoria_.getUrl(),
                                categoria_.getUrlNormativa(),
                                categoria_.getAnio()));
            }
        }

        // Si no hay ninguno, es que no hay nada de ese año:
        if (categorias.isEmpty()) {
            categorias = null;
        }

        return categorias;

    }

    List<PistaReservable> getListaPistasReservables() {
        return listaPistasReservables;
    }

    String getTablaReservas(String codigoPista, String nombrePista, String fecha) {
        String tablaReservas = "";

        ListaPistasReservablesFechas lprf = new ListaPistasReservablesFechas("");

        tablaReservas = lprf.consultarFecha(dni, cadId, nombrePista, codigoPista, fecha);

        return tablaReservas;
    }

    List<String> getDeportes() {
        List<Deporte> listaDeportes = null;
        List<String> listaCadenaDeportes = null;
        HashMap<String, Deporte> mapaDeportes = new HashMap<String, Deporte>();

        //Categoria categoria=baseDatosCategorias.get(categoriaId);
        List<Categoria> listaCategorias = torneos.getListaCategorias();

        // Por cada categoría, obtenemos los deportes definidos:
        for (Categoria categoria : listaCategorias) {
            listaDeportes = categoria.getListaDeportes();
            for (Deporte deporte : listaDeportes) {
                mapaDeportes.put(deporte.getTitulo(), deporte);
            }
        }

        // Lo pasamos a lista:
        if (!mapaDeportes.isEmpty()) {
            listaCadenaDeportes = new ArrayList();
            for (String deporteString : mapaDeportes.keySet()) {
                listaCadenaDeportes.add(deporteString);
            }

            // Ordenamos la lista...
            Collections.sort(listaCadenaDeportes);
        }

        return listaCadenaDeportes;
    }

    List<DatosEquipo> getDatosEquipos() {
        List<DatosEquipo> listaEquipos = null;
        Map<String, DatosEquipo> mapaEquipos = new HashMap();

        List<Categoria> categorias = torneos.getListaCategorias();

        for (Categoria categoria : categorias) {
            String tituloCategoria = categoria.getTitulo();
            List<Deporte> listaDeportes = categoria.getListaDeportes();

            for (Deporte deporte : listaDeportes) {
                String tituloDeporte = deporte.getTitulo();
                List<Fase> listaclasificaciones = categoria.getFases(deporte.getId());

                for (Fase fase : listaclasificaciones) {
                    List<Ronda> listaRondas = fase.getRondas();

                    for (Ronda ronda : listaRondas) {
                        List<Partido> listaPartidos = ronda.getPartidos();

                        for (Partido partido : listaPartidos) {
                            Equipo equipo1 = partido.getEquipo1();
                            Equipo equipo2 = partido.getEquipo2();

                            DatosEquipo datosEquipo = new DatosEquipo(equipo1, tituloCategoria, tituloDeporte);
                            mapaEquipos.put(datosEquipo.getTituloCategoria() + ">" + datosEquipo.getTituloDeporte() + ">" + equipo1.getNombre(), datosEquipo);

                            datosEquipo = new DatosEquipo(equipo2, tituloCategoria, tituloDeporte);
                            mapaEquipos.put(datosEquipo.getTituloCategoria() + ">" + datosEquipo.getTituloDeporte() + ">" + equipo2.getNombre(), datosEquipo);

                        }
                    }
                }
            }
        }

        if (!mapaEquipos.isEmpty()) {
            listaEquipos = new ArrayList<DatosEquipo>();

            for (DatosEquipo datosEquipo : mapaEquipos.values()) {
                listaEquipos.add(datosEquipo);
            }

            // Ordenamos la lista...
            Collections.sort(listaEquipos, new Comparator<DatosEquipo>() {

                @Override
                public int compare(DatosEquipo t, DatosEquipo t1) {
                    String s = t.getTituloCategoria() + ">" + t.getTituloDeporte() + ">" + t.getEquipo().getNombre();
                    String s1 = t1.getTituloCategoria() + ">" + t1.getTituloDeporte() + ">" + t1.getEquipo().getNombre();

                    return s1.compareTo(s);
                }
            });
        }

        return listaEquipos;
    }

    Noticia getNoticia(String tablon,String noticiaid) {
      Noticia noticia=new Noticia(tablon,noticiaid);  
      noticia=noticia.consultaWeb();
      
      return noticia;
    }

    List<Equipo> getEquipos(String categoriaId, String deporteId) {
        
        List<Equipo> listaEquipos=null;
        String tituloDeporte="deporteId";
        Map<String, Equipo> mapaEquipos = new HashMap();

        Categoria categoria = torneos.getCategoria(categoriaId);
        String tituloCategoria = categoria.getTitulo();
        
        // Buscamos la información del deporte:
        
        List<Deporte> listaDeportes = categoria.getListaDeportes();
        Deporte deporte=null;
        for(int i=0;deporte==null&&i<listaDeportes.size();i++){
            Deporte deporte_=listaDeportes.get(i);
            
            if(deporte_.getId().compareTo(deporteId)==0){
                deporte=deporte_;
            }
        }    
        if(deporte!=null){
            tituloDeporte=deporte.getTitulo();
        }
        
        
        List<Fase> listaclasificaciones = categoria.getFases(deporteId);

                for (Fase fase : listaclasificaciones) {
                    List<Ronda> listaRondas = fase.getRondas();

                    for (Ronda ronda : listaRondas) {
                        List<Partido> listaPartidos = ronda.getPartidos();

                        for (Partido partido : listaPartidos) {
                            Equipo equipo1 = partido.getEquipo1();
                            Equipo equipo2 = partido.getEquipo2();

                            DatosEquipo datosEquipo = new DatosEquipo(equipo1, tituloCategoria, tituloDeporte);
                            mapaEquipos.put(datosEquipo.getTituloCategoria() + ">" + datosEquipo.getTituloDeporte() + ">" + equipo1.getNombre(), equipo1);

                            datosEquipo = new DatosEquipo(equipo2, tituloCategoria, tituloDeporte);
                            mapaEquipos.put(datosEquipo.getTituloCategoria() + ">" + datosEquipo.getTituloDeporte() + ">" + equipo2.getNombre(), equipo2);
                        }
                    }
                }

        if (!mapaEquipos.isEmpty()) {
            listaEquipos = new ArrayList<Equipo>();

            for (Equipo datosEquipo : mapaEquipos.values()) {
                listaEquipos.add(datosEquipo);
            }

            // Ordenamos la lista...
            Collections.sort(listaEquipos, new Comparator<Equipo>() {

                @Override
                public int compare(Equipo t, Equipo t1) {
                    String s = t.getNombre();
                    String s1 =t1.getNombre();

                    return s1.compareTo(s);
                }
            });
        }

        return listaEquipos;
    }
    
    
//        List<DatosEquipo> getEquipos(String categoriaId, String deporteId) {
//        
//        List<DatosEquipo> listaEquipos=null;
//        String tituloDeporte="deporteId";
//        Map<String, DatosEquipo> mapaEquipos = new HashMap();
//
//        Categoria categoria = torneos.getCategoria(categoriaId);
//        String tituloCategoria = categoria.getTitulo();
//        
//        // Buscamos la información del deporte:
//        
//        List<Deporte> listaDeportes = categoria.getListaDeportes();
//        Deporte deporte=null;
//        for(int i=0;deporte==null&&i<listaDeportes.size();i++){
//            Deporte deporte_=listaDeportes.get(i);
//            
//            if(deporte_.getId().compareTo(deporteId)==0){
//                deporte=deporte_;
//            }
//        }    
//        if(deporte!=null){
//            tituloDeporte=deporte.getTitulo();
//        }
//        
//        
//        List<Fase> listaclasificaciones = categoria.getFases(deporteId);
//
//                for (Fase fase : listaclasificaciones) {
//                    List<Ronda> listaRondas = fase.getRondas();
//
//                    for (Ronda ronda : listaRondas) {
//                        List<Partido> listaPartidos = ronda.getPartidos();
//
//                        for (Partido partido : listaPartidos) {
//                            Equipo equipo1 = partido.getEquipo1();
//                            Equipo equipo2 = partido.getEquipo2();
//
//                            DatosEquipo datosEquipo = new DatosEquipo(equipo1, tituloCategoria, tituloDeporte);
//                            mapaEquipos.put(datosEquipo.getTituloCategoria() + ">" + datosEquipo.getTituloDeporte() + ">" + equipo1.getNombre(), datosEquipo);
//
//                            datosEquipo = new DatosEquipo(equipo2, tituloCategoria, tituloDeporte);
//                            mapaEquipos.put(datosEquipo.getTituloCategoria() + ">" + datosEquipo.getTituloDeporte() + ">" + equipo2.getNombre(), datosEquipo);
//
//                        }
//                    }
//                }
//            
//        
//
//        if (!mapaEquipos.isEmpty()) {
//            listaEquipos = new ArrayList<DatosEquipo>();
//
//            for (DatosEquipo datosEquipo : mapaEquipos.values()) {
//                listaEquipos.add(datosEquipo);
//            }
//
//            // Ordenamos la lista...
//            Collections.sort(listaEquipos, new Comparator<DatosEquipo>() {
//
//                @Override
//                public int compare(DatosEquipo t, DatosEquipo t1) {
//                    String s = t.getTituloCategoria() + ">" + t.getTituloDeporte() + ">" + t.getEquipo().getNombre();
//                    String s1 = t.getTituloCategoria() + ">" + t.getTituloDeporte() + ">" + t.getEquipo().getNombre();
//
//                    return s1.compareTo(s);
//                }
//            });
//        }
//
//        return listaEquipos;
//    }

}
