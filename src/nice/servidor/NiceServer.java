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
import nice.comun.Global;
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

/**
 *
 * @author jjramos
 * 
 */
public class NiceServer extends Application {

    private static HashMap<String, Categoria> baseDatosCategorias;
    private static Torneos torneos;
    // Lista de pistas disponibles en las secciones de reservas en página del CSIRC.
    private static List<PistaReservable> listaPistasReservables;
    // Credenciales para acceder a las pistas.
    private static String dni="X";
    private static String cadId="X";
    // Puerto de escucha del servidor.
    private static int puerto=8080;
    // Ruta del fichero de configuración por defecto
    private static String rutaFicheroDatosParaReserva = "./niceserver.cfg";
    // Objeto que mantiene la la configuración del fichero .cfg
    private static ConfiguracionServidorNice configuracion;

    
    /**
     * 
     * @param string 
     * @return Lista de pistas reservables que se pueden elegir
     */
    private static List<PistaReservable> incializarBaseDeDatosPistasReservables(String string) {
        ListaPistasReservables pr = new ListaPistasReservables(string);

        // Ejecuta la búsqueda de información en la web de las páginas de reserva, y obtiene la lista
        // de pistas definidas en la web:
        pr.consultarWeb();
        List<PistaReservable> lista = pr.getListaPistasReservables();

        return lista;
    }

    /**
     * Método principal del servidor.
     * @param args El primer argumento puede ser la ruta del fichero de configuración.
     */
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

            // Para HTTPS, mirar en:
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

    /**
     * Método para inicializar todas listas con la información extraída de las webs.
     * Así genera el "nido".
     * @return Devuelve 0 si no hubo ningún error, u otro valor en caso contrario.
     */
    private static int inicializarBaseDeDatos() {
        int error = 0;

        // Se crea la configuración a partir del fichero de configuración:
        configuracion = new ConfiguracionServidorNice(rutaFicheroDatosParaReserva);

        // Si no hubo erro al interpretar el fichero
        if (configuracion.getError() == 0) {
            // Se obtienen las credenciales, necesarias para las consultas de reservas
            dni = configuracion.getId();
            cadId = configuracion.getCadId();
            // Se lee el puerto desde el que servir las consultas
            puerto=configuracion.getPuerto();
            

            // Se extrae la información de reservas
            listaPistasReservables = incializarBaseDeDatosPistasReservables(Global.getBaseUrlListaPistasReservables(dni,cadId));

           torneos = new Torneos(Global.baseUrlTorneos);

            // Comprobamos si tenemos una copia del nido (base de datos de memoria):
            File db = new File("torneos.serial");

            // Siempre entra aquí: no lee el finchero por ahora.
            if (true || !db.exists()) {
                FileOutputStream fout = null;
                try {

                    // Se extrae la información de qué torneos se han definido en la web del CAD:
                    torneos.consultarWeb();
                    
                    // Hacemos una copia de la base de datos:
                    fout = new FileOutputStream("torneos.serial");
                    ObjectOutputStream oos = new ObjectOutputStream(fout);
                    oos.writeObject(torneos);

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(NiceServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(NiceServer.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        // Si no hubo problemas al escribir, se cierra el fichero
                        fout.close();
                    } catch (IOException ex) {
                        Logger.getLogger(NiceServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                
            } else {
                // Si existiera una copia de la base de datos, se lee:
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

            // Creamos una base de datos de categorías:
            baseDatosCategorias = new HashMap<String, Categoria>();

        } else {
            error = 1;
            System.err.println("Error al leer la configuración...");
        }

        return error;
    }

    /**
     * Inicializa los recursos accesibles con el RESTlet: 
     * @return 
     */
    public Restlet createInboundRoot() {
        
        // Creamos una encamidor de Restlet que recogerá las peticiones:
        Router encaminador = new Router(getContext());

        // enlaza cada recurso al encaminador:
        // Servicios de deportes existentes:
        encaminador.attach("/perfiles/deportes", RecursoPerfilesDeportes.class);
        // Servicios para listar equipos definidos en todas las competiciones
        encaminador.attach("/perfiles/equipos", RecursoPerfilesEquipos.class);
        // Servicio para obtener una noticia concreta
        encaminador.attach("/noticias/{noticiaid}/tablones/{tablon}", RecursoNoticia.class);
        // Servicio para obtener la lista de deportes por categoría (torneo)
        encaminador.attach("/categorias/{categoria}/deportes", RecursoDeportes.class);
        // Servicio para obtener los calendarios de partidos por torneo y deporte
        encaminador.attach("/categorias/{categoria}/deportes/{deporte}/calendarios", RecursoCalendarios.class);
        // Servicio para obtener los torneos de un año determinado
        encaminador.attach("/torneos/{anio}", RecursoCategorias.class);
        // Servicio para obtener información de una pista determinada
        encaminador.attach("/pista/{pistaid}", RecursoPista.class);
        // Servicio para obtener la lista de pistas reservables
        encaminador.attach("/reservas/pistas", RecursoPistasReservas.class);
        // Servicio para obtener la parrilla de reservas de una pista y una fecha
        encaminador.attach("/reservas/pistas/{codigoPista}/fecha/{fecha}", RecursoPistasReservasFechas.class);
        // Servicio para consultar los equipos definidos para un torneo y un deporte
        encaminador.attach("/categorias/{categoria}/deportes/{deporte}/equipos", RecursoEquiposCategoriaDeporte.class);
 
        
        // Devuelve la raíz del encaminador
        return encaminador;
    }

    /**
     * Método para obtener la lista de deportes definidos para un código de torneo.
     * @param categoriaId Código del torneo
     * @return Devuelve la lista de deportes definidos para el torneo <code>categoriaId</code>
     */
    List<Deporte> getDeportes(String categoriaId) {
        List<Deporte> listaDeportes = null;

        // Se busca el torneo de la lista de torneos con el identificador "categoriaId"
        Categoria categoria = torneos.getCategoria(categoriaId);

        if(listaDeportes!=null)
            listaDeportes = categoria.getListaDeportes();

        return listaDeportes;
    }

    /**
     * Método para obtener las fases definidas en un torneo, dado su código <code>categoriaId</code>
     * y seleccionado el deporte con el código <code>deporteId</code>.
     * @param categoriaId Identificador del torneo
     * @param deporteId identificador del deporte
     * @return lista de fases definidas para el torneo y el deporte. Devuelve una lista vacía si no hay fases, 
     *          <code>null</code> si hubo problemas en la extracción de la información.
     */
    List<Fase> getFases(String categoriaId, String deporteId) {
        List<Fase> fases = null;

        //  Categoria categoria=baseDatosCategorias.get(categoriaId);
        Categoria categoria = torneos.getCategoria(categoriaId);

        if(categoria!=null) 
            fases = categoria.getFases(deporteId);

        return fases;
    }

    /**
     * Método para consultar la lista de torneos definidos para el año <code>anio</code>.
     * @param anio Año de los torneos a consultar, con formato: "AAAA" 
     * @return Devuelve la lista de los torneos del año seleccionado. Si no hay ninguno, devueve <code>null</code>
     */
    List<Categoria> getCategorias(String anio) {
        List<Categoria> categorias = null;

        List<Categoria> categorias_ = torneos.getListaCategorias();
        
        if (categorias_ != null) {
            for (Categoria categoria_ : categorias_) {
                if (anio.compareTo(categoria_.getAnio()) == 0) {
                    categorias.add(categoria_);
                }
            }

            // Si no hay ninguno, es que no hay nada de ese año:
            if (categorias.isEmpty()) {
                categorias = null;
            }
        }

        return categorias;
    }
    
    /**
     * 
     * 
     * @param anio
     * @return 
     */
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
