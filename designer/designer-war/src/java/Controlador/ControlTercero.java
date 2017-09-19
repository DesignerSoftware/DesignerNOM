/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTerceroInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlTercero implements Serializable {

   private static Logger log = Logger.getLogger(ControlTercero.class);

   @EJB
   AdministrarTerceroInterface administrarTercero;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //
   private List<Terceros> listTerceros;
   private List<Terceros> filtrarListTercero;
   private List<Terceros> listTercerosLOV;
   private List<Terceros> filtrarListTerceroLOV;
   private Terceros terceroTablaSeleccionado;
   private Terceros terceroLOVSeleccionado;
   //
   private List<TercerosSucursales> listTercerosSucursales;
   private List<TercerosSucursales> filtrarListTercerosSucursales;
   private TercerosSucursales terceroSucursalTablaSeleccionado;
   //
   private Empresas empresaSeleccionada;
   //private Empresas empresaSeleccionada;
   private List<Empresas> lovEmpresas;
   private List<Empresas> filtrarListEmpresas;
   private Empresas empresaActual;
   //
   private List<Ciudades> lovCiudades;
   private List<Ciudades> filtrarListCiudades;
   private Ciudades ciudadSeleccionada;
   //
   private List<Terceros> lovTerceroConsolidador;
   private List<Terceros> filtrarListTerceroConsolidador;
   private Terceros terceroCSeleccionado;
   //
   private Empleados empleado;
   private int tipoActualizacion;
   private int bandera;
   //Columnas Tabla VL
   private Column terceroNombre, terceroNIT, terceroDigitoVerificacion, terceroNITAlternativo, terceroCodigoSS, terceroCodigoSP, terceroCodigoSC, terceroTConsolidador, terceroNITConsolidado, terceroCiudad, terceroCodigoAlterno;
   //Columnas Tabla VP
   private Column terceroSucursalCodigo, terceroSucursalPatronal, terceroSucursalObservacion, terceroSucursalCiudad;
   //Otros
   private boolean aceptar;
   private List<Terceros> listTerceroModificar;
   private boolean guardado;
   public Terceros nuevoTercero;
   private List<Terceros> listTerceroCrear;
   private BigInteger l;
   private int k;
   private List<Terceros> listTerceroBorrar;
   private Terceros editarTercero;
   private TercerosSucursales editarTerceroSucursal;
   private int cualCelda, tipoLista;
   private Terceros duplicarTercero;
   private boolean permitirIndex, permitirIndexTS;
   //Variables Autompletar
   //private String motivoCambioSueldo, tiposEntidades, tiposSueldos, terceros;
   //Indices VigenciaProrrateo / VigenciaProrrateoProyecto
   private int cualCeldaTS, tipoListaTS;
   private String nombreXML;
   private String nombreTabla;
   private boolean cambiosTercero, cambiosTerceroSucursal;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private BigInteger backUp;
   private String nombreTablaRastro;
   private List<TercerosSucursales> listTerceroSucursalCrear, listTerceroSucursalModificar, listTerceroSucursalBorrar;
   private TercerosSucursales nuevoTerceroSucursal, duplicarTerceroSucursal;
   private String terceroConsolidador, ciudad, ciudadTS;
   private long nitConsolidado;
   //
   private String infoRegistro, infoRegistroEmpresa, infoRegistroCiudad2, infoRegistroCiudad1, infoRegistroLovTercero, infoRegistroTerceroConsolidador, infoRegistroTerceroSucursal;
   //
   private String altoTablaTercero, altoTablaSucursal, altoTablaReg;
   //
   private String auxNombreTercero;
   private long auxNitTercero;
   //
   private BigInteger auxCodigoSucursal;
   //
   private DataTable tablaT;
   //
   private boolean activarLOV;
   private int cualTabla;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   /**
    * Creates a new instance of ControlTercero
    */
   public ControlTercero() {
      altoTablaTercero = "143";
      altoTablaSucursal = "70";
      terceroLOVSeleccionado = new Terceros();
      listTercerosLOV = null;
      listTerceros = null;
      editarTerceroSucursal = new TercerosSucursales();
      lovCiudades = null;
      lovTerceroConsolidador = null;
      empresaSeleccionada = new Empresas();
      empresaActual = new Empresas();
      lovEmpresas = null;
      listTercerosSucursales = null;
      nombreTablaRastro = "";
      backUp = null;
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      terceroTablaSeleccionado = null;
      terceroSucursalTablaSeleccionado = null;
      empleado = new Empleados();
      //Otros
      aceptar = true;
      //borrar aficiones
      listTerceroBorrar = new ArrayList<Terceros>();
      //crear aficiones
      listTerceroCrear = new ArrayList<Terceros>();
      listTerceroSucursalBorrar = new ArrayList<TercerosSucursales>();
      listTerceroSucursalModificar = new ArrayList<TercerosSucursales>();
      listTerceroSucursalCrear = new ArrayList<TercerosSucursales>();
      k = 0;
      //modificar aficiones
      listTerceroModificar = new ArrayList<Terceros>();
      //editar
      editarTercero = new Terceros();
      cualCelda = -1;
      tipoLista = 0;
      tipoListaTS = 0;
      //guardar
      guardado = true;
      //Crear VC
      nuevoTercero = new Terceros();
      nuevoTercero.setTerceroconsolidador(new Terceros());
      nuevoTercero.setCiudad(new Ciudades());
      nuevoTerceroSucursal = new TercerosSucursales();
      nuevoTerceroSucursal.setCiudad(new Ciudades());
      bandera = 0;
      permitirIndex = true;
      permitirIndexTS = true;
      cualCeldaTS = -1;

      nombreTabla = ":formExportarTerceros:datosTercerosExportar";
      nombreXML = "TercerosXML";

      cambiosTerceroSucursal = false;
      duplicarTercero = new Terceros();
      cambiosTercero = false;
      activarLOV = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovCiudades = null;
      lovEmpresas = null;
      lovTerceroConsolidador = null;
   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }

   @PostConstruct
   public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTercero.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      lovEmpresas = administrarTercero.listEmpresas();
      int tam = lovEmpresas.size();
      if (tam > 0) {
         empresaActual = lovEmpresas.get(0);
         empresaSeleccionada = empresaActual;
         listTerceros = null;
         getListTerceros();
         if (listTerceros != null) {
            if (!listTerceros.isEmpty()) {
               terceroTablaSeleccionado = listTerceros.get(0);
            }
         }
         getListTercerosSucursales();
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      lovEmpresas = administrarTercero.listEmpresas();
      int tam = lovEmpresas.size();
      if (tam > 0) {
         empresaActual = lovEmpresas.get(0);
         empresaSeleccionada = empresaActual;
         listTerceros = null;
         getListTerceros();
         if (listTerceros != null) {
            if (!listTerceros.isEmpty()) {
               terceroTablaSeleccionado = listTerceros.get(0);
            }
         }
         getListTercerosSucursales();
      }
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      /*if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());

      } else {
       */
      String pagActual = "tercero";

      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
      } else {
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
         //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParaEnviar.put("paginaAnterior", pagActual);
         //mas Parametros
         //         if (pag.equals("rastrotabla")) {
         //           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
      }
      limpiarListasValor();
   }

   public String valorPaginaAnterior() {
      return paginaAnterior;
   }

   /*
     * public void recibirPaginaEntrante(String pagina) { paginaAnterior = pagina; }
    */
   /**
    * Modifica los elementos de la tabla VigenciaLocalizacion que no usan
    * autocomplete
    *
    * @param indice Fila donde se efectu el cambio
    */
   public void modificarTercero(Terceros t) {
      terceroTablaSeleccionado = t;
      if (validarCamposNulosTercero(0) == true) {
         if (!listTerceroCrear.contains(terceroTablaSeleccionado)) {
            if (listTerceroModificar.isEmpty()) {
               listTerceroModificar.add(terceroTablaSeleccionado);
            } else if (!listTerceroModificar.contains(terceroTablaSeleccionado)) {
               listTerceroModificar.add(terceroTablaSeleccionado);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         terceroTablaSeleccionado.getTerceroconsolidador().setNit(nitConsolidado);
//         terceroTablaSeleccionado.setCodigoalternativo(Long.parseLong(terceroTablaSeleccionado.getStrCodAlt()));
         terceroTablaSeleccionado.setCodigoalternativo(terceroTablaSeleccionado.getCodigoalternativo());

         cambiosTercero = true;
      } else {
         terceroTablaSeleccionado.setNit(auxNitTercero);
         terceroTablaSeleccionado.setNombre(auxNombreTercero);

         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTerceros').show()");
      }
      RequestContext.getCurrentInstance().update("form:datosTerceros");
   }

   public void modificarTerceroAutocompletar(Terceros terceros, String confirmarCambio, String valorConfirmar) {
      terceroTablaSeleccionado = terceros;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("TERCEROCONSOLIDADOR")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (!valorConfirmar.isEmpty()) {
            terceroTablaSeleccionado.getTerceroconsolidador().setNombre(terceroConsolidador);

            for (int i = 0; i < lovTerceroConsolidador.size(); i++) {
               if (lovTerceroConsolidador.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               terceroTablaSeleccionado.setTerceroconsolidador(lovTerceroConsolidador.get(indiceUnicoElemento));

               lovTerceroConsolidador.clear();
               getLovTerceroConsolidador();
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:TerceroDialogo");
               RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
            terceroTablaSeleccionado.setTerceroconsolidador(new Terceros());

            lovTerceroConsolidador.clear();
            getLovTerceroConsolidador();
         }
      }
      if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
         aceptar = false;
         if (!valorConfirmar.isEmpty()) {
            terceroTablaSeleccionado.getCiudad().setNombre(ciudad);

            for (int i = 0; i < lovCiudades.size(); i++) {
               if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               terceroTablaSeleccionado.setCiudad(lovCiudades.get(indiceUnicoElemento));

               lovCiudades.clear();
               getLovCiudades();
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:CiudadDialogo");
               RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
            terceroTablaSeleccionado.setCiudad(new Ciudades());

            lovCiudades.clear();
            getLovCiudades();
         }
      }
      if (coincidencias == 1) {
         if (!listTerceroCrear.contains(terceroTablaSeleccionado)) {
            if (listTerceroModificar.isEmpty()) {
               listTerceroModificar.add(terceroTablaSeleccionado);
            } else if (!listTerceroModificar.contains(terceroTablaSeleccionado)) {
               listTerceroModificar.add(terceroTablaSeleccionado);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         cambiosTercero = true;
      }
      RequestContext.getCurrentInstance().update("form:datosTerceros");
   }

   public void modificarTerceroSucursal(int indice) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (validarCamposNulosTerceroSucursal(0) == true) {
         if (!listTerceroSucursalCrear.contains(terceroSucursalTablaSeleccionado)) {
            if (listTerceroSucursalModificar.isEmpty()) {
               listTerceroSucursalModificar.add(terceroSucursalTablaSeleccionado);
            } else if (!listTerceroSucursalModificar.contains(terceroSucursalTablaSeleccionado)) {
               listTerceroSucursalModificar.add(terceroSucursalTablaSeleccionado);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }

         cambiosTerceroSucursal = true;
      } else {
         terceroSucursalTablaSeleccionado.setCodigosucursal(auxCodigoSucursal);

         RequestContext.getCurrentInstance().execute("PF('errorDatosNullSucursal').show()");
      }
      RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
   }

   public void modificarTerceroSucursalAutocompletar(Terceros terceros, String confirmarCambio, String valorConfirmar) {
      terceroTablaSeleccionado = terceros;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (!valorConfirmar.isEmpty()) {
            terceroSucursalTablaSeleccionado.getCiudad().setNombre(ciudadTS);

            for (int i = 0; i < lovCiudades.size(); i++) {
               if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               terceroSucursalTablaSeleccionado.setCiudad(lovCiudades.get(indiceUnicoElemento));

               lovCiudades.clear();
               getLovCiudades();
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:CiudadTSDialogo");
               RequestContext.getCurrentInstance().execute("PF('CiudadTSDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
            terceroSucursalTablaSeleccionado.setCiudad(new Ciudades());

            lovCiudades.clear();
            getLovCiudades();
         }
      }
      if (coincidencias == 1) {
         if (!listTerceroSucursalCrear.contains(terceroSucursalTablaSeleccionado)) {
            if (listTerceroSucursalModificar.isEmpty()) {
               listTerceroSucursalModificar.add(terceroSucursalTablaSeleccionado);
            } else if (!listTerceroSucursalModificar.contains(terceroSucursalTablaSeleccionado)) {
               listTerceroSucursalModificar.add(terceroSucursalTablaSeleccionado);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }

         cambiosTerceroSucursal = true;
      }
      RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
   }

   ///////////////////////////////////////////////////////////////////////////
   /**
    * Modifica los elementos de la tabla VigenciaProrrateo que no usan
    * autocomplete
    *
    * @param indice Fila donde se efectu el cambio
    */
   //Ubicacion Celda.
   /**
    * Metodo que obtiene la posicion dentro de la tabla VigenciasLocalizaciones
    *
    * @param indice Fila de la tabla
    * @param celda Columna de la tabla
    */
   public void cambiarIndice(Terceros terceros, int celda) {
      cualTabla = 1;
      if (permitirIndex == true) {
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         cualCelda = celda;
         terceroTablaSeleccionado = terceros;
         //indexAux = indice;
         auxNitTercero = terceroTablaSeleccionado.getNit();
         auxNombreTercero = terceroTablaSeleccionado.getNombre();
         if (cualCelda == 0) {
            terceroTablaSeleccionado.getNombre();
         } else if (cualCelda == 2) {
            terceroTablaSeleccionado.getNit();
         } else if (cualCelda == 3) {
            terceroTablaSeleccionado.getDigitoverificacion();
         } else if (cualCelda == 4) {
            terceroTablaSeleccionado.getCodigoss();
         } else if (cualCelda == 5) {
            terceroTablaSeleccionado.getCodigosp();
         } else if (cualCelda == 6) {
            terceroTablaSeleccionado.getCodigosc();
         } else if (cualCelda == 7) {
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            terceroConsolidador = terceroTablaSeleccionado.getTerceroconsolidador().getNombre();
         } else if (cualCelda == 8) {
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            nitConsolidado = terceroTablaSeleccionado.getTerceroconsolidador().getNit();
         } else if (cualCelda == 9) {
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            ciudad = terceroTablaSeleccionado.getCiudad().getNombre();
         } else if (cualCelda == 10) {
            terceroTablaSeleccionado.getCodigoalternativo();
         }
      }
      listTercerosSucursales = null;
      getListTercerosSucursales();
      RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
      contarRegistrosTSucur();
   }

   public void cambiarIndiceTS(TercerosSucursales tercerosSucursales, int celda) {
      cualTabla = 2;
      if (permitirIndexTS == true) {
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         cualCeldaTS = celda;
         terceroSucursalTablaSeleccionado = tercerosSucursales;
         auxCodigoSucursal = terceroSucursalTablaSeleccionado.getCodigosucursal();
         if (cualCeldaTS == 3) {
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            ciudadTS = terceroSucursalTablaSeleccionado.getCiudad().getNombre();
         }

      }
   }

   //GUARDAR
   /**
    * Metodo de guardado general para la pagina
    */
   public void guardadoGeneral() {
      if (guardado == false) {
         if (cambiosTerceroSucursal == true) {
            guardarCambiosTerceroSucursales();
         } else if (cambiosTercero == true) {
            guardarCambiosTercero();
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void guardarCambiosTercero() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listTerceroBorrar.isEmpty()) {
               for (int i = 0; i < listTerceroBorrar.size(); i++) {
                  if (listTerceroBorrar.get(i).getTerceroconsolidador().getSecuencia() == null) {
                     listTerceroBorrar.get(i).setTerceroconsolidador(null);
                  }
                  if (listTerceroBorrar.get(i).getCiudad().getSecuencia() == null) {
                     listTerceroBorrar.get(i).setCiudad(null);
                  }
                  administrarTercero.borrarTercero(listTerceroBorrar.get(i));
               }
               listTerceroBorrar.clear();
            }
            if (!listTerceroCrear.isEmpty()) {
               for (int i = 0; i < listTerceroCrear.size(); i++) {
                  if (listTerceroCrear.get(i).getTerceroconsolidador().getSecuencia() == null) {
                     listTerceroCrear.get(i).setTerceroconsolidador(null);
                  }
                  if (listTerceroCrear.get(i).getCiudad().getSecuencia() == null) {
                     listTerceroCrear.get(i).setCiudad(null);
                  }
                  administrarTercero.crearTercero(listTerceroCrear.get(i));
               }
               listTerceroCrear.clear();
            }
            if (!listTerceroModificar.isEmpty()) {
               for (int i = 0; i < listTerceroModificar.size(); i++) {
                  if (listTerceroModificar.get(i).getTerceroconsolidador().getSecuencia() == null) {
                     listTerceroModificar.get(i).setTerceroconsolidador(null);
                  }
                  if (listTerceroModificar.get(i).getCiudad().getSecuencia() == null) {
                     listTerceroModificar.get(i).setCiudad(null);
                  }
                  administrarTercero.modificarTercero(listTerceroModificar.get(i));
               }
               listTerceroModificar.clear();
            }

            contarRegistrosTerc();

            //listTerceros = null;
            RequestContext.getCurrentInstance().update("form:datosTerceros");
            k = 0;
            cambiosTercero = false;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos de Tercero con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistrosTerc();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
         }
      } catch (Exception e) {
         log.warn("Error guardarCambiosTercero Controlador : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Tercero, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosTerceroSucursales() {
      RequestContext context = RequestContext.getCurrentInstance();
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      try {
         if (guardado == false) {
            if (!listTerceroSucursalBorrar.isEmpty()) {
               for (int i = 0; i < listTerceroSucursalBorrar.size(); i++) {
                  if (listTerceroSucursalBorrar.get(i).getCiudad().getSecuencia() == null) {
                     listTerceroSucursalBorrar.get(i).setCiudad(null);
                  }
                  administrarTercero.borrarTerceroSucursales(listTerceroSucursalBorrar.get(i));
               }
               listTerceroSucursalBorrar.clear();
            }
            if (!listTerceroSucursalCrear.isEmpty()) {
               for (int i = 0; i < listTerceroSucursalCrear.size(); i++) {
                  if (listTerceroSucursalCrear.get(i).getCiudad().getSecuencia() == null) {
                     listTerceroSucursalCrear.get(i).setCiudad(null);
                  }
                  administrarTercero.crearTerceroSucursales(listTerceroSucursalCrear.get(i));
               }
               listTerceroSucursalCrear.clear();
            }
            if (!listTerceroSucursalModificar.isEmpty()) {
               for (int i = 0; i < listTerceroSucursalModificar.size(); i++) {
                  if (listTerceroSucursalModificar.get(i).getCiudad().getSecuencia() == null) {
                     listTerceroSucursalModificar.get(i).setCiudad(null);
                  }
                  administrarTercero.modificarTerceroSucursales(listTerceroSucursalModificar.get(i));
               }
               listTerceroSucursalModificar.clear();
            }
            contarRegistrosTSucur();
            RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
            k = 0;
            cambiosTerceroSucursal = false;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos de Tercero Sucursal con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistrosTSucur();
            RequestContext.getCurrentInstance().update("form:infoRegistroTerceroSucursal");
         }
      } catch (Exception e) {
         log.warn("Error guardarCambiosTercero Controlador : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Tercero Sucursal, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   //CANCELAR MODIFICACIONES
   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacion() {
      if (bandera == 1) {
         cerrarFiltradoTercero();
      }
      listTerceroBorrar.clear();
      listTerceroCrear.clear();
      listTerceroModificar.clear();
      listTerceroSucursalBorrar.clear();
      listTerceroSucursalCrear.clear();
      listTerceroSucursalModificar.clear();
      terceroSucursalTablaSeleccionado = null;
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      //indexAux = -1;
      terceroTablaSeleccionado = null;
      k = 0;
      listTerceros = null;
      listTercerosSucursales = null;
      cambiosTerceroSucursal = false;
      guardado = true;
      cambiosTercero = false;
      getListTerceros();
      getListTercerosSucursales();
      RequestContext.getCurrentInstance().update("form:datosTerceros");
      RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
      contarRegistrosTerc();
      contarRegistrosTSucur();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   //MOSTRAR DATOS CELDA
   /**
    * Metodo que muestra los dialogos de editar con respecto a la lista real o
    * la lista filtrada y a la columna
    */
   public void editarCelda() {
      if (terceroTablaSeleccionado != null) {
         editarTercero = terceroTablaSeleccionado;
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreTerceroD");
            RequestContext.getCurrentInstance().execute("PF('editarNombreTerceroD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNitTerceroD");
            RequestContext.getCurrentInstance().execute("PF('editarNitTerceroD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDigtoVTerceroD");
            RequestContext.getCurrentInstance().execute("PF('editarDigtoVTerceroD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNITATerceroD");
            RequestContext.getCurrentInstance().execute("PF('editarNITATerceroD').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCSSTerceroD");
            RequestContext.getCurrentInstance().execute("PF('editarCSSTerceroD').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCSPTerceroD");
            RequestContext.getCurrentInstance().execute("PF('editarCSPTerceroD').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCSCTerceroD");
            RequestContext.getCurrentInstance().execute("PF('editarCSCTerceroD').show()");
            cualCelda = -1;
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTCTerceroD");
            RequestContext.getCurrentInstance().execute("PF('editarTCTerceroD').show()");
            cualCelda = -1;
         } else if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNITTCTerceroD");
            RequestContext.getCurrentInstance().execute("PF('editarNITTCTerceroD').show()");
            cualCelda = -1;
         } else if (cualCelda == 9) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudadTerceroD");
            RequestContext.getCurrentInstance().execute("PF('editarCiudadTerceroD').show()");
            cualCelda = -1;
         } else if (cualCelda == 10) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoATerceroD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoATerceroD').show()");
            cualCelda = -1;
         }
      }
      if (terceroSucursalTablaSeleccionado != null) {
         editarTerceroSucursal = terceroSucursalTablaSeleccionado;

         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCeldaTS == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarSucursalTerceroSD");
            RequestContext.getCurrentInstance().execute("PF('editarSucursalTerceroSD').show()");
            cualCeldaTS = -1;
         } else if (cualCeldaTS == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPatronalTerceroSD");
            RequestContext.getCurrentInstance().execute("PF('editarPatronalTerceroSD').show()");
            cualCeldaTS = -1;
         } else if (cualCeldaTS == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacionesTerceroSD");
            RequestContext.getCurrentInstance().execute("PF('editarObservacionesTerceroSD').show()");
            cualCeldaTS = -1;
         } else if (cualCeldaTS == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudadTerceroSD");
            RequestContext.getCurrentInstance().execute("PF('editarCiudadTerceroSD').show()");
            cualCeldaTS = -1;
         }
      }
   }

   public void validarIngresoNuevoRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (terceroTablaSeleccionado != null) {
         if (cambiosTerceroSucursal == false) {
            if (listTercerosSucursales.isEmpty()) {
               RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPagina').show()");
            } else {
               RequestContext.getCurrentInstance().update("form:NuevoRegistroTercero");
               RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTercero').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
         }
      }
      if (terceroSucursalTablaSeleccionado != null) {
         RequestContext.getCurrentInstance().update("form:NuevoRegistroTerceroSucursal");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTerceroSucursal').show()");
      }
   }

   public void validarDuplicadoRegistro() {
      if (terceroSucursalTablaSeleccionado != null) {
         duplicarTerceroS();
      } else if (terceroTablaSeleccionado != null) {
         duplicarTercero();
      }
   }

   public void validarBorradoRegistro() {
      if (terceroSucursalTablaSeleccionado != null) {
         borrarTerceroSucursal();
      } else if (terceroTablaSeleccionado != null) {
         if (listTercerosSucursales == null || listTercerosSucursales.isEmpty()) {
            borrarTercero();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorBorradoTercero').show()");
         }
      }
   }

   public boolean validarCamposNulosTercero(int i) {
      boolean retorno = true;
      if (i == 0) {
         Terceros aux = null;
         aux = terceroTablaSeleccionado;

         if (aux.getNit() < 0) {
            retorno = false;
         }
         if (aux.getNombre() == null) {
            retorno = false;
         } else if (aux.getNombre().isEmpty()) {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevoTercero.getNit() < 0) {
            retorno = false;
         }
         if (nuevoTercero.getNombre() == null) {
            retorno = false;
         } else if (nuevoTercero.getNombre().isEmpty()) {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarTercero.getNit() < 0) {
            retorno = false;
         }
         if (duplicarTercero.getNombre() == null) {
            retorno = false;
         } else if (duplicarTercero.getNombre().isEmpty()) {
            retorno = false;
         }
      }
      return retorno;
   }

   public boolean validarCamposNulosTerceroSucursal(int i) {
      log.error("Estoy en validarCamposNulosTerceroSucursal()");
      boolean retorno = true;
      log.info("El valor de i: " + i);
      if (i == 0) {
         log.info("Entre a if (i == 0)");
         if (terceroSucursalTablaSeleccionado.getCodigosucursal() == null) {
            retorno = false;
         }
      }
      if (i == 1) {
         log.info("Entre a if (i == 1)");
         if (nuevoTerceroSucursal.getCodigosucursal() == null) {
            retorno = false;
         }
      }
      if (i == 2) {
         log.info("Entre a if (i == 2)");
         log.info("duplicarTerceroSucursal.getCodigosucursal(): " + duplicarTerceroSucursal.getCodigosucursal());
         if (duplicarTerceroSucursal.getCodigosucursal() == null) {
            retorno = false;
         }
      }
      log.info("retorno: " + retorno);
      return retorno;
   }

   public void agregarNuevoTercero() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (validarCamposNulosTercero(1) == true) {
         if (bandera == 1) {
            cerrarFiltradoTercero();
         }
         //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
         k++;
         BigInteger var = BigInteger.valueOf(k);
         nuevoTercero.setSecuencia(var);
         nuevoTercero.setEmpresa(empresaActual);
         listTerceroCrear.add(nuevoTercero);
         if (listTerceros == null) {
            listTerceros = new ArrayList<Terceros>();
         }
         listTerceros.add(nuevoTercero);
         terceroTablaSeleccionado = listTerceros.get(listTerceros.indexOf(nuevoTercero));
         contarRegistrosTerc();
         ////------////
         nuevoTercero = new Terceros();
         nuevoTercero.setTerceroconsolidador(new Terceros());
         nuevoTercero.setCiudad(new Ciudades());
         ////-----////

         RequestContext.getCurrentInstance().update("form:datosTerceros");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTercero').hide()");
         cambiosTercero = true;
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTerceros').show()");
      }
   }

   //LIMPIAR NUEVO REGISTRO
   /**
    * Metodo que limpia las casillas de la nueva vigencia
    */
   public void limpiarNuevoTercero() {
      nuevoTercero = new Terceros();
      nuevoTercero.setTerceroconsolidador(new Terceros());
      nuevoTercero.setCiudad(new Ciudades());
   }

   ////////--- //// ---////
   public void agregarNuevoTerceroSucursal() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (validarCamposNulosTerceroSucursal(1) == true) {
         if (bandera == 1) {
            cerrarFiltradoTercero();
         }
         //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
         k++;
         BigInteger var = BigInteger.valueOf(k);
         nuevoTerceroSucursal.setSecuencia(var);
         nuevoTerceroSucursal.setTercero(terceroTablaSeleccionado);
         listTerceroSucursalCrear.add(nuevoTerceroSucursal);
         listTercerosSucursales.add(nuevoTerceroSucursal);

         if (listTercerosSucursales == null) {
            listTercerosSucursales = new ArrayList<TercerosSucursales>();
         }
         terceroSucursalTablaSeleccionado = listTercerosSucursales.get(listTercerosSucursales.indexOf(nuevoTerceroSucursal));
         contarRegistrosTSucur();
         ////------////
         nuevoTerceroSucursal = new TercerosSucursales();
         nuevoTerceroSucursal.setCiudad(new Ciudades());
         ////-----////
         RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTerceroSucursal').hide()");
         cambiosTerceroSucursal = true;
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullSucursal').show()");
      }
      terceroSucursalTablaSeleccionado = null;
   }

   //LIMPIAR NUEVO REGISTRO
   /**
    * Metodo que limpia las casillas de la nueva vigencia
    */
   public void limpiarNuevoTerceroSucursal() {
      terceroSucursalTablaSeleccionado = null;
      nuevoTerceroSucursal = new TercerosSucursales();
      nuevoTerceroSucursal.setCiudad(new Ciudades());
   }
   //DUPLICAR VL

   /**
    * Metodo que verifica que proceso de duplicar se genera con respecto a la
    * posicion en la pagina que se tiene
    */
   /**
    * Duplica una nueva vigencia localizacion
    */
   public void duplicarTercero() {
      if (terceroTablaSeleccionado != null) {
         duplicarTercero = new Terceros();
         duplicarTercero.setNombre(terceroTablaSeleccionado.getNombre());
         duplicarTercero.setNit(terceroTablaSeleccionado.getNit());
         duplicarTercero.setDigitoverificacion(terceroTablaSeleccionado.getDigitoverificacion());
         duplicarTercero.setNitalternativo(terceroTablaSeleccionado.getNitalternativo());
         duplicarTercero.setCodigoss(terceroTablaSeleccionado.getCodigoss());
         duplicarTercero.setCodigosp(terceroTablaSeleccionado.getCodigosp());
         duplicarTercero.setCodigosc(terceroTablaSeleccionado.getCodigosc());
         duplicarTercero.setTerceroconsolidador(terceroTablaSeleccionado.getTerceroconsolidador());
         duplicarTercero.setCiudad(terceroTablaSeleccionado.getCiudad());
         duplicarTercero.setCodigoalternativo(terceroTablaSeleccionado.getCodigoalternativo());
         duplicarTercero.setTiponit(terceroTablaSeleccionado.getTiponit());
         duplicarTercero.setCodigotercerosap(terceroTablaSeleccionado.getCodigotercerosap());

         if (duplicarTercero.getTerceroconsolidador() == null) {
            duplicarTercero.setTerceroconsolidador(new Terceros());
         }
         if (duplicarTercero.getCiudad() == null) {
            duplicarTercero.setCiudad(new Ciudades());
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTercero");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTercero').show()");
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla
    * VigenciasLocalizaciones
    */
   public void confirmarDuplicar() {
      RequestContext context = RequestContext.getCurrentInstance();
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      if (validarCamposNulosTercero(2) == true) {
         if (bandera == 1) {
            cerrarFiltradoTercero();
         }
         k++;
         BigInteger var = BigInteger.valueOf(k);
         duplicarTercero.setSecuencia(var);
         duplicarTercero.setEmpresa(empresaActual);
         listTerceroCrear.add(duplicarTercero);
         listTerceros.add(duplicarTercero);
         terceroTablaSeleccionado = listTerceros.get(listTerceros.indexOf(duplicarTercero));
         contarRegistrosTerc();
         duplicarTercero = new Terceros();
         RequestContext.getCurrentInstance().update("form:datosTerceros");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTercero').hide()");
         cambiosTercero = true;
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTerceros').show()");
      }
   }

   public void limpiarDuplicarTercero() {
      duplicarTercero = new Terceros();
      duplicarTercero.setTerceroconsolidador(new Terceros());
      duplicarTercero.setCiudad(new Ciudades());
   }

   ////-- !! --- !! --///
   public void duplicarTerceroS() {
      if (terceroSucursalTablaSeleccionado != null) {
         duplicarTerceroSucursal = new TercerosSucursales();
         duplicarTerceroSucursal.setCodigosucursal(terceroSucursalTablaSeleccionado.getCodigosucursal());
         duplicarTerceroSucursal.setCodigopatronal(terceroSucursalTablaSeleccionado.getCodigopatronal());
         duplicarTerceroSucursal.setDescripcion(terceroSucursalTablaSeleccionado.getDescripcion());
         duplicarTerceroSucursal.setCiudad(terceroSucursalTablaSeleccionado.getCiudad());

         if (duplicarTerceroSucursal.getCiudad() == null) {
            duplicarTerceroSucursal.setCiudad(new Ciudades());
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTerceroSucursal");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTerceroSucursal').show()");
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla
    * VigenciasLocalizaciones
    */
   public void confirmarDuplicarTS() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (validarCamposNulosTerceroSucursal(2) == true) {
         if (bandera == 1) {
            cerrarFiltradoTercero();
         }
         k++;
         BigInteger var = BigInteger.valueOf(k);
         duplicarTerceroSucursal.setSecuencia(var);
         duplicarTerceroSucursal.setTercero(terceroTablaSeleccionado);

         listTerceroSucursalCrear.add(duplicarTerceroSucursal);
         listTercerosSucursales.add(duplicarTerceroSucursal);
         terceroSucursalTablaSeleccionado = listTercerosSucursales.get(listTercerosSucursales.indexOf(duplicarTerceroSucursal));
         duplicarTerceroSucursal = new TercerosSucursales();
         RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTerceroSucursal').hide()");
         cambiosTerceroSucursal = true;
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullSucursal').show()");
      }
   }

   public void limpiarDuplicarTerceroSucursal() {
      duplicarTerceroSucursal = new TercerosSucursales();
      duplicarTerceroSucursal.setCiudad(new Ciudades());
   }

   ///////////////////////////////////////////////////////////////
   /**
    * Valida que registro se elimina de que tabla con respecto a la posicion en
    * la pagina
    */
   public void borrarTercero() {
      log.error("Entre a borrarTercero()");
      if (terceroTablaSeleccionado != null) {
         if (!listTerceroModificar.isEmpty() && listTerceroModificar.contains(terceroTablaSeleccionado)) {
            int modIndex = listTerceroModificar.indexOf(terceroTablaSeleccionado);
            listTerceroModificar.remove(modIndex);
            listTerceroBorrar.add(terceroTablaSeleccionado);
         } else if (!listTerceroCrear.isEmpty() && listTerceroCrear.contains(terceroTablaSeleccionado)) {
            int crearIndex = listTerceroCrear.indexOf(terceroTablaSeleccionado);
            listTerceroCrear.remove(crearIndex);
         } else {
            listTerceroBorrar.add(terceroTablaSeleccionado);
         }
         log.info("ControlTercero.borrarTercero() 1");
         listTerceros.remove(terceroTablaSeleccionado);
         log.info("ControlTercero.borrarTercero() 2");
         if (tipoLista == 1) {
            filtrarListTercero.remove(terceroTablaSeleccionado);
            log.info("ControlTercero.borrarTercero() 3");
         }
         contarRegistrosTerc();
         log.info("ControlTercero.borrarTercero() 4");
         RequestContext.getCurrentInstance().update("form:datosTerceros");
         log.info("ControlTercero.borrarTercero() 5");
         cambiosTercero = true;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            log.info("ControlTercero.borrarTercero() 6");
         }
      }
   }

   ////--- !! --- !!! ///
   public void borrarTerceroSucursal() {
      if (terceroSucursalTablaSeleccionado != null) {
         if (!listTerceroSucursalModificar.isEmpty() && listTerceroSucursalModificar.contains(terceroSucursalTablaSeleccionado)) {
            int modIndex = listTerceroSucursalModificar.indexOf(terceroSucursalTablaSeleccionado);
            listTerceroSucursalModificar.remove(modIndex);
            listTerceroSucursalBorrar.add(terceroSucursalTablaSeleccionado);
         } else if (!listTerceroSucursalCrear.isEmpty() && listTerceroSucursalCrear.contains(terceroSucursalTablaSeleccionado)) {
            int crearIndex = listTerceroSucursalCrear.indexOf(terceroSucursalTablaSeleccionado);
            listTerceroSucursalCrear.remove(crearIndex);
         } else {
            listTerceroSucursalBorrar.add(terceroSucursalTablaSeleccionado);
         }
         listTercerosSucursales.remove(terceroSucursalTablaSeleccionado);
         if (tipoLista == 1) {
            filtrarListTercerosSucursales.remove(terceroSucursalTablaSeleccionado);
         }
         RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
         contarRegistrosTSucur();
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         cambiosTerceroSucursal = true;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   /**
    * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      log.error("Entre a filtradoTercero() bandera : " + bandera);
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTablaTercero = "123";
         terceroNombre = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNombre");
         terceroNombre.setFilterStyle("width: 85% !important;");
         terceroNIT = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNIT");
         terceroNIT.setFilterStyle("width: 85% !important;");
         terceroDigitoVerificacion = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroDigitoVerificacion");
         terceroDigitoVerificacion.setFilterStyle("width: 85% !important;");
         terceroNITAlternativo = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNITAlternativo");
         terceroNITAlternativo.setFilterStyle("width: 85% !important;");
         terceroCodigoSS = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCodigoSS");
         terceroCodigoSS.setFilterStyle("width: 85% !important;");
         terceroCodigoSP = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCodigoSP");
         terceroCodigoSP.setFilterStyle("width: 85% !important;");
         terceroCodigoSC = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCodigoSC");
         terceroCodigoSC.setFilterStyle("width: 85% !important;");
         terceroTConsolidador = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroTConsolidador");
         terceroTConsolidador.setFilterStyle("width: 85% !important;");
         terceroNITConsolidado = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNITConsolidado");
         terceroNITConsolidado.setFilterStyle("width: 85% !important;");
         terceroCiudad = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCiudad");
         terceroCiudad.setFilterStyle("width: 85% !important;");
         terceroCodigoAlterno = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCiudad");
         terceroCodigoAlterno.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTerceros");

         altoTablaSucursal = "50";
         terceroSucursalCodigo = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalCodigo");
         terceroSucursalCodigo.setFilterStyle("width: 85% !important;");
         terceroSucursalPatronal = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalPatronal");
         terceroSucursalPatronal.setFilterStyle("width: 85% !important;");
         terceroSucursalObservacion = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalObservacion");
         terceroSucursalObservacion.setFilterStyle("width: 85% !important;");
         terceroSucursalCiudad = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalCiudad");
         terceroSucursalCiudad.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltradoTercero();
      }
   }

   public void cerrarFiltradoTercero() {
      FacesContext c = FacesContext.getCurrentInstance();
      altoTablaTercero = "143";
      terceroNombre = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNombre");
      terceroNombre.setFilterStyle("display: none; visibility: hidden;");
      terceroNIT = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNIT");
      terceroNIT.setFilterStyle("display: none; visibility: hidden;");
      terceroDigitoVerificacion = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroDigitoVerificacion");
      terceroDigitoVerificacion.setFilterStyle("display: none; visibility: hidden;");
      terceroNITAlternativo = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNITAlternativo");
      terceroNITAlternativo.setFilterStyle("display: none; visibility: hidden;");
      terceroCodigoSS = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCodigoSS");
      terceroCodigoSS.setFilterStyle("display: none; visibility: hidden;");
      terceroCodigoSP = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCodigoSP");
      terceroCodigoSP.setFilterStyle("display: none; visibility: hidden;");
      terceroCodigoSC = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCodigoSC");
      terceroCodigoSC.setFilterStyle("display: none; visibility: hidden;");
      terceroTConsolidador = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroTConsolidador");
      terceroTConsolidador.setFilterStyle("display: none; visibility: hidden;");
      terceroNITConsolidado = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNITConsolidado");
      terceroNITConsolidado.setFilterStyle("display: none; visibility: hidden;");
      terceroCiudad = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCiudad");
      terceroCiudad.setFilterStyle("display: none; visibility: hidden;");
      terceroCodigoAlterno = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCiudad");
      terceroCodigoAlterno.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosTerceros");

      altoTablaSucursal = "70";
      terceroSucursalCodigo = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalCodigo");
      terceroSucursalCodigo.setFilterStyle("display: none; visibility: hidden;");
      terceroSucursalPatronal = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalPatronal");
      terceroSucursalPatronal.setFilterStyle("display: none; visibility: hidden;");
      terceroSucursalObservacion = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalObservacion");
      terceroSucursalObservacion.setFilterStyle("display: none; visibility: hidden;");
      terceroSucursalCiudad = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalCiudad");
      terceroSucursalCiudad.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
      filtrarListTercerosSucursales = null;

      bandera = 0;
      filtrarListTercero = null;
      tipoListaTS = 0;
      tipoLista = 0;
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         cerrarFiltradoTercero();
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      listTerceroBorrar.clear();
      listTerceroCrear.clear();
      listTerceroModificar.clear();
      listTerceroSucursalModificar.clear();
      listTerceroSucursalCrear.clear();
      listTerceroSucursalBorrar.clear();
      terceroTablaSeleccionado = null;
      terceroSucursalTablaSeleccionado = null;
      k = 0;
      listTerceros = null;
      listTercerosSucursales = null;
      guardado = true;
      cambiosTercero = true;
      cambiosTerceroSucursal = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }
   //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO) (list = ESTRUCTURAS - MOTIVOSLOCALIZACIONES - PROYECTOS)

   public void asignarIndexSucursales(TercerosSucursales sucursal, int dlg, int LND) {

      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      tipoActualizacion = LND;
      terceroSucursalTablaSeleccionado = sucursal;
      if (dlg == 0) {
         contarRegistrosCiudadTSucur();
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         RequestContext.getCurrentInstance().update("form:CiudadTSDialogo");
         RequestContext.getCurrentInstance().execute("PF('CiudadTSDialogo').show()");
      }

   }

   public void asignarIndex(Terceros tercero, int dlg, int LND) {
      RequestContext context = RequestContext.getCurrentInstance();
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      terceroTablaSeleccionado = tercero;
      tipoActualizacion = LND;
      if (dlg == 0) {
         contarRegistrosTerceroConsol();
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         RequestContext.getCurrentInstance().update("form:TerceroDialogo");
         RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
      } else if (dlg == 1) {
         contarRegistrosCiudadTerc();
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         RequestContext.getCurrentInstance().update("form:CiudadDialogo");
         RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
      }
   }

   //LISTA DE VALORES DINAMICA
   /**
    * Metodo que activa la lista de valores de todas las tablas con respecto al
    * index activo y la columna activa
    */
   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      if (terceroTablaSeleccionado != null) {
         if (cualCelda == 7) {
            contarRegistrosTerceroConsol();
            terceroCSeleccionado = null;
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            RequestContext.getCurrentInstance().update("form:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 9) {
            contarRegistrosCiudadTerc();
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (terceroSucursalTablaSeleccionado != null) {
         if (cualCeldaTS == 3) {
            contarRegistrosCiudadTSucur();
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            RequestContext.getCurrentInstance().update("form:CiudadTSDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadTSDialogo').show()");
            tipoActualizacion = 0;
         }
      }

   }

   /**
    * Valida un proceso de nuevo registro dentro de la pagina con respecto a la
    * posicion en la pagina
    */
   /**
    * Metodo que activa el boton aceptar de la pagina y los dialogos
    */
   public void valoresBackupAutocompletar(int tipoNuevo, String Campo, int tipoLista) {
      if (Campo.equals("TERCEROCONSOLIDADOR")) {
         if (tipoNuevo == 1) {
            terceroConsolidador = nuevoTercero.getTerceroconsolidador().getNombre();
         } else if (tipoNuevo == 2) {
            terceroConsolidador = duplicarTercero.getTerceroconsolidador().getNombre();
         }
      } else if (Campo.equals("CIUDAD")) {
         if (tipoNuevo == 1) {
            ciudad = nuevoTercero.getCiudad().getNombre();
         } else if (tipoNuevo == 2) {
            ciudad = duplicarTercero.getCiudad().getNombre();
         }
      }

   }

   public void autocompletarNuevoyDuplicadoTercero(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("TERCEROCONSOLIDADOR")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevoTercero.getTerceroconsolidador().setNombre(terceroConsolidador);
            } else if (tipoNuevo == 2) {
               duplicarTercero.getTerceroconsolidador().setNombre(terceroConsolidador);
            }
            for (int i = 0; i < lovTerceroConsolidador.size(); i++) {
               if (lovTerceroConsolidador.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevoTercero.setTerceroconsolidador(lovTerceroConsolidador.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
               } else if (tipoNuevo == 2) {
                  duplicarTercero.setTerceroconsolidador(lovTerceroConsolidador.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroCT");
               }
               lovTerceroConsolidador.clear();
               getLovTerceroConsolidador();
            } else {
               RequestContext.getCurrentInstance().update("form:TerceroDialogo");
               RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroCT");
               }
            }
         } else {
            if (tipoNuevo == 1) {
               nuevoTercero.setTerceroconsolidador(new Terceros());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
            } else if (tipoNuevo == 2) {
               duplicarTercero.setTerceroconsolidador(new Terceros());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroCT");
            }
            lovTerceroConsolidador.clear();
            getLovTerceroConsolidador();
         }
      } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevoTercero.getCiudad().setNombre(ciudad);
            } else if (tipoNuevo == 2) {
               duplicarTercero.getCiudad().setNombre(ciudad);
            }
            for (int i = 0; i < lovCiudades.size(); i++) {
               if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevoTercero.setCiudad(lovCiudades.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
               } else if (tipoNuevo == 2) {
                  duplicarTercero.setCiudad(lovCiudades.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
               }
               lovCiudades.clear();
               getLovCiudades();
            } else {
               RequestContext.getCurrentInstance().update("form:CiudadDialogo");
               RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
               }
            }
         } else {
            if (tipoNuevo == 1) {
               nuevoTercero.setCiudad(new Ciudades());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
            } else if (tipoNuevo == 2) {
               duplicarTercero.setCiudad(new Ciudades());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
            }
            lovCiudades.clear();
            getLovCiudades();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoTerceroSucursal(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevoTerceroSucursal.getCiudad().setNombre(ciudadTS);
            } else if (tipoNuevo == 2) {
               duplicarTerceroSucursal.getCiudad().setNombre(ciudadTS);
            }
            for (int i = 0; i < lovCiudades.size(); i++) {
               if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevoTerceroSucursal.setCiudad(lovCiudades.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudadTS");
               } else if (tipoNuevo == 2) {
                  duplicarTerceroSucursal.setCiudad(lovCiudades.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudadTS");
               }
               lovCiudades.clear();
               getLovCiudades();
            } else {
               RequestContext.getCurrentInstance().update("form:CiudadTSDialogo");
               RequestContext.getCurrentInstance().execute("PF('CiudadTSDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudadTS");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudadTS");
               }
            }
         } else {
            if (tipoNuevo == 1) {
               nuevoTerceroSucursal.setCiudad(new Ciudades());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudadTS");
            } else if (tipoNuevo == 2) {
               duplicarTerceroSucursal.setCiudad(new Ciudades());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudadTS");
            }
            lovCiudades.clear();
            getLovCiudades();
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void actualizarTerceros() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         terceroTablaSeleccionado.setTerceroconsolidador(terceroCSeleccionado);
         if (!listTerceroCrear.contains(terceroTablaSeleccionado)) {
            if (listTerceroModificar.isEmpty()) {
               listTerceroModificar.add(terceroTablaSeleccionado);
            } else if (!listTerceroModificar.contains(terceroTablaSeleccionado)) {
               listTerceroModificar.add(terceroTablaSeleccionado);
            }
         }

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         cambiosTercero = true;
         permitirIndex = true;

         RequestContext.getCurrentInstance().update("form:datosTerceros");
      } else if (tipoActualizacion == 1) {
         nuevoTercero.setTerceroconsolidador(terceroCSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCNITT");
      } else if (tipoActualizacion == 2) {
         duplicarTercero.setTerceroconsolidador(terceroCSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTerceroCT");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTerceroCNITT");
      }
      filtrarListTerceroConsolidador = null;
      terceroCSeleccionado = new Terceros();
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("form:TerceroDialogo");
      RequestContext.getCurrentInstance().update("form:lovTercero");
      RequestContext.getCurrentInstance().update("form:aceptarT");

      context.reset("form:lovTercero:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");

   }

   public void cancelarCambioTerceros() {
      filtrarListTerceroConsolidador = null;
      terceroCSeleccionado = new Terceros();
      aceptar = true;
      terceroTablaSeleccionado = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:TerceroDialogo");
      RequestContext.getCurrentInstance().update("form:lovTercero");
      RequestContext.getCurrentInstance().update("form:aceptarT");

      context.reset("form:lovTercero:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");
   }

   public void actualizarCiudad() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         terceroTablaSeleccionado.setCiudad(ciudadSeleccionada);
         if (!listTerceroCrear.contains(terceroTablaSeleccionado)) {
            if (listTerceroModificar.isEmpty()) {
               listTerceroModificar.add(terceroTablaSeleccionado);
            } else if (!listTerceroModificar.contains(terceroTablaSeleccionado)) {
               listTerceroModificar.add(terceroTablaSeleccionado);
            }
         }

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         cambiosTercero = true;
         permitirIndex = true;

         RequestContext.getCurrentInstance().update("form:datosTerceros");
      } else if (tipoActualizacion == 1) {
         nuevoTercero.setCiudad(ciudadSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaT");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudadT");
      } else if (tipoActualizacion == 2) {
         duplicarTercero.setCiudad(ciudadSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarT");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTCiudadT");
      }
      filtrarListCiudades = null;
      ciudadSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
//        
      RequestContext.getCurrentInstance().update("form:CiudadDialogo");
      RequestContext.getCurrentInstance().update("form:lovCiudad");
      RequestContext.getCurrentInstance().update("form:aceptarC");

      context.reset("form:lovCiudad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').hide()");
   }

   public void cancelarCambioCiudad() {
      filtrarListCiudades = null;
      ciudadSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:CiudadDialogo");
      RequestContext.getCurrentInstance().update("form:lovCiudad");
      RequestContext.getCurrentInstance().update("form:aceptarC");

      context.reset("form:lovCiudad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').hide()");
   }

   public void actualizarCiudadTS() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         terceroSucursalTablaSeleccionado.setCiudad(ciudadSeleccionada);
         if (!listTerceroSucursalCrear.contains(terceroSucursalTablaSeleccionado)) {
            if (listTerceroSucursalModificar.isEmpty()) {
               listTerceroSucursalModificar.add(terceroSucursalTablaSeleccionado);
            } else if (!listTerceroSucursalModificar.contains(terceroSucursalTablaSeleccionado)) {
               listTerceroSucursalModificar.add(terceroSucursalTablaSeleccionado);
            }
         }

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         cambiosTerceroSucursal = true;
         permitirIndexTS = true;
         RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
      } else if (tipoActualizacion == 1) {
         nuevoTerceroSucursal.setCiudad(ciudadSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudadTS");
      } else if (tipoActualizacion == 2) {
         duplicarTerceroSucursal.setCiudad(ciudadSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudadTS");
      }
      filtrarListCiudades = null;
      ciudadSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("form:CiudadTSDialogo");
      RequestContext.getCurrentInstance().update("form:lovCiudadTS");
      RequestContext.getCurrentInstance().update("form:aceptarCTS");

      context.reset("form:lovCiudadTS:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudadTS').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CiudadTSDialogo').hide()");
   }

   public void cancelarCambioCiudadTS() {
      filtrarListCiudades = null;
      ciudadSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:CiudadTSDialogo");
      RequestContext.getCurrentInstance().update("form:lovCiudadTS");
      RequestContext.getCurrentInstance().update("form:aceptarCTS");

      context.reset("form:lovCiudadTS:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudadTS').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CiudadTSDialogo').hide()");
   }

   public String exportXML() {
      if (terceroSucursalTablaSeleccionado != null) {
         nombreTabla = ":formExportarTercerosSucursales:datosTercerosSucursalesExportar";
         nombreXML = "TercerosSucursalesXML";
      } else if (terceroTablaSeleccionado != null) {
         nombreTabla = ":formExportarTerceros:datosTercerosExportar";
         nombreXML = "TercerosXML";
      }
      return nombreTabla;
   }

   /**
    * Valida la tabla a exportar en PDF con respecto al index activo
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      if (cualTabla == 1) {
         exportPDFT();
      } else if (cualTabla == 2) {
         exportPDFTS();
      }
   }

   /**
    * Metodo que exporta datos a PDF Vigencia Localizacion
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDFT() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTerceros:datosTercerosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TercerosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a PDF Vigencia Prorrateo
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDFTS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTercerosSucursales:datosTercerosSucursalesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TercerosSucursalesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Verifica que tabla exportar XLS con respecto al index activo
    *
    * @throws IOException
    */
   public void verificarExportXLS() throws IOException {
      if (cualTabla == 1) {
         exportXLST();
      } else if (cualTabla == 2) {
         exportXLSTS();
      }
   }

   /**
    * Metodo que exporta datos a XLS Vigencia Sueldos
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLST() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTerceros:datosTercerosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TercerosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a XLS Vigencia Afiliaciones
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLSTS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTercerosSucursales:datosTercerosSucursalesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TercerosSucursalesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void dialogoSeleccionarTercero() {
      terceroLOVSeleccionado = null;
      getFiltrarListTerceroLOV();
      getListTerceros();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:BuscarTerceroDialogo");
      RequestContext.getCurrentInstance().execute("PF('BuscarTerceroDialogo').show()");
   }

   public void cancelarSeleccionTercero() {
      terceroCSeleccionado = new Terceros();
      terceroLOVSeleccionado = null;
      filtrarListTerceroConsolidador = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovBuscarTercero:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovBuscarTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('BuscarTerceroDialogo').hide()");
   }

   public void validarSeleccionTercero() {
      RequestContext context = RequestContext.getCurrentInstance();
      terceroTablaSeleccionado = terceroLOVSeleccionado;
      listTerceros = null;
      listTerceros = new ArrayList<Terceros>();
      if (terceroLOVSeleccionado.getCiudad() == null) {
         terceroLOVSeleccionado.setCiudad(new Ciudades());
      }
      if (terceroLOVSeleccionado.getTerceroconsolidador() == null) {
         terceroLOVSeleccionado.setTerceroconsolidador(new Terceros());
      }
      listTerceros.add(terceroLOVSeleccionado);
      contarRegistrosTerc();
      RequestContext.getCurrentInstance().update("form:datosTerceros");
      listTercerosSucursales = null;
      getListTercerosSucursales();
      contarRegistrosTSucur();
      RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");

      RequestContext.getCurrentInstance().update("form:BuscarTerceroDialogo");
      RequestContext.getCurrentInstance().update("form:lovBuscarTercero");
      RequestContext.getCurrentInstance().update("form:aceptarBT");

      context.reset("form:lovBuscarTercero:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovBuscarTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('BuscarTerceroDialogo').hide()");
      aceptar = true;
   }

   public void mostrarTodos() {
      log.info("ControlTercero.mostrarTodos()");
      RequestContext context = RequestContext.getCurrentInstance();
      if (cambiosTercero == false && cambiosTerceroSucursal == false) {
         listTerceros = null;
         getListTerceros();
         RequestContext.getCurrentInstance().update("form:datosTerceros");
         getListTercerosSucursales();
         RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
      contarRegistrosTerc();
      contarRegistrosTSucur();
   }

   //METODO RASTROS PARA LAS TABLAS EN EMPLVIGENCIASUELDOS
   public void verificarRastroTabla() {
      //Cuando no se ha seleccionado ningun registro:
      if (terceroTablaSeleccionado == null && terceroSucursalTablaSeleccionado == null) {
         //Dialogo para seleccionar el rastro Historico de la tabla deseada
         RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
      } else //Cuando se selecciono registro:            
      {
         if (terceroSucursalTablaSeleccionado != null) {
            verificarRastroTercerosSucursales();
         } else if (terceroTablaSeleccionado != null) {
            verificarRastroTerceros();
         }
      }
   }

   //Verificar Rastro Vigencia Terceros
   public void verificarRastroTerceros() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(terceroTablaSeleccionado.getSecuencia(), "TERCEROS");
      backUp = terceroTablaSeleccionado.getSecuencia();
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "Terceros";
         msnConfirmarRastro = "La tabla TERCEROS tiene rastros para el registro seleccionado, ¿desea continuar?";
         RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
      } else if (resultado == 3) {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
      } else if (resultado == 4) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
      } else if (resultado == 5) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
      }
   }

   public void verificarRastroTercerosHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("TERCEROS")) {
         nombreTablaRastro = "Terceros";
         msnConfirmarRastroHistorico = "La tabla TERCEROS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroTercerosSucursales() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(terceroSucursalTablaSeleccionado.getSecuencia(), "TERCEROSSUCURSALES");
      backUp = terceroSucursalTablaSeleccionado.getSecuencia();
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "TercerosSucursales";
         msnConfirmarRastro = "La tabla TERCEROSSUCURSALES tiene rastros para el registro seleccionado, ¿desea continuar?";
         RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
      } else if (resultado == 3) {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
      } else if (resultado == 4) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
      } else if (resultado == 5) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
      }
   }

   public void verificarRastroTercerosSucursalesHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("TERCEROSSUCURSALES")) {
         nombreTablaRastro = "TercerosSucursales";
         msnConfirmarRastroHistorico = "La tabla TERCEROSSUCURSALES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void lovEmpresas() {
      empresaSeleccionada = null;
      cualCelda = -1;
      cualCeldaTS = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
   }

   public void actualizarEmpresa() {
      RequestContext context = RequestContext.getCurrentInstance();
//        terceroTablaSeleccionado = null;
//        terceroSucursalTablaSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:nombreEmpresa");
      RequestContext.getCurrentInstance().update("form:nitEmpresa");
      filtrarListEmpresas = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
      context.reset("formularioDialogos:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
      empresaActual = empresaSeleccionada;
      listTerceros = null;
      getListTerceros();
      if (listTerceros != null) {
         if (!listTerceros.isEmpty()) {
            terceroTablaSeleccionado = listTerceros.get(0);
         }
      }
      RequestContext.getCurrentInstance().update("form:datosTerceros");
      contarRegistrosTerc();
      contarRegistrosTSucur();
      if (cambiosTercero == false && cambiosTerceroSucursal == false) {
         empresaSeleccionada = empresaActual;
         listTerceros = null;
         listTercerosSucursales = null;
         getListTerceros();
         getListTercerosSucursales();
         RequestContext.getCurrentInstance().update("form:datosTerceros");
         RequestContext.getCurrentInstance().update("form:datosTercerosSucursales");
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
      // empresaSeleccionada = null;
   }

   public void cancelarCambioEmpresa() {
      RequestContext context = RequestContext.getCurrentInstance();
      cualCelda = -1;
      cualCeldaTS = -1;
      terceroSucursalTablaSeleccionado = null;
      filtrarListEmpresas = null;
      empresaSeleccionada = null;
      context.reset("formularioDialogos:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   //EVENTO FILTRAR
   /**
    * Evento que cambia la lista real a la filtrada
    */
   public void eventoFiltrarT() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      terceroTablaSeleccionado = null;
      contarRegistrosTerc();
   }

   public void eventoFiltrarTS() {
      if (tipoListaTS == 0) {
         tipoListaTS = 1;
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      terceroTablaSeleccionado = null;
      contarRegistrosTSucur();
   }

   public void contarRegistrosTerc() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosTSucur() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTerceroSucursal");
   }

   public void contarRegistrosLovTercero() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTercero");
   }

   public void contarRegistrosTerceroConsol() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTerceroConsolidador");
   }

   public void contarRegistrosCiudadTerc() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCiudad1");
   }

   public void contarRegistrosCiudadTSucur() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCiudad2");
   }

   public void contarRegistrosEmpresa() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresa");
   }

   public void recordarSeleccionT() {
      if (terceroTablaSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaT = (DataTable) c.getViewRoot().findComponent("form:datosTerceros");
         tablaT.setSelection(terceroTablaSeleccionado);
      }
   }

   public void anularLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void recordarSeleccionTS() {
      if (terceroSucursalTablaSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaT = (DataTable) c.getViewRoot().findComponent("form:datosTercerosSucursales");
         tablaT.setSelection(terceroSucursalTablaSeleccionado);
      }
   }

   //GET - SET ************************************************************************************************************//////////////////////
   public List<Terceros> getListTerceros() {
      try {
         if (listTerceros == null) {
            if (empresaActual.getSecuencia() != null) {
               listTerceros = administrarTercero.obtenerListTerceros(empresaActual.getSecuencia());
               if (listTerceros != null) {
                  for (int i = 0; i < listTerceros.size(); i++) {
                     if (listTerceros.get(i).getCiudad() == null) {
                        listTerceros.get(i).setCiudad(new Ciudades());
                     }
                     if (listTerceros.get(i).getTerceroconsolidador() == null) {
                        listTerceros.get(i).setTerceroconsolidador(new Terceros());
                     }

                     if (listTerceros.get(i).getStrCodAlt() == null) {
                        listTerceros.get(i).setStrCodAlt("");
                     }
                  }
               }
            }
         }
         return listTerceros;
      } catch (Exception e) {
         log.warn("Error getListTerceros " + e.toString());
         return null;
      }
   }

   public void setListTerceros(List<Terceros> t) {
      this.listTerceros = t;
   }

   public List<Terceros> getFiltrarListTercero() {
      return filtrarListTercero;
   }

   public void setFiltrarListTercero(List<Terceros> t) {
      this.filtrarListTercero = t;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public String getNombreXML() {
      return nombreXML;
   }

   public void setNombreXML(String nombreXML) {
      this.nombreXML = nombreXML;
   }

   public String getNombreTabla() {
      return nombreTabla;
   }

   public void setNombreTabla(String nombreTabla) {
      this.nombreTabla = nombreTabla;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public List<Terceros> getListTerceroModificar() {
      return listTerceroModificar;
   }

   public void setListTerceroModificar(List<Terceros> listTerceroModificar) {
      this.listTerceroModificar = listTerceroModificar;
   }

   public Terceros getNuevoTercero() {
      return nuevoTercero;
   }

   public void setNuevoTercero(Terceros nuevoTercero) {
      this.nuevoTercero = nuevoTercero;
   }

   public List<Terceros> getListTerceroCrear() {
      return listTerceroCrear;
   }

   public void setListTerceroCrear(List<Terceros> listTerceroCrear) {
      this.listTerceroCrear = listTerceroCrear;
   }

   public List<Terceros> getListTerceroBorrar() {
      return listTerceroBorrar;
   }

   public void setListTerceroBorrar(List<Terceros> listTerceroBorrar) {
      this.listTerceroBorrar = listTerceroBorrar;
   }

   public Terceros getEditarTercero() {
      return editarTercero;
   }

   public void setEditarTercero(Terceros editarTercero) {
      this.editarTercero = editarTercero;
   }

   public Terceros getDuplicarTecero() {
      return duplicarTercero;
   }

   public void setDuplicarTecero(Terceros duplicarTecero) {
      this.duplicarTercero = duplicarTecero;
   }

   public String getMsnConfirmarRastro() {
      return msnConfirmarRastro;
   }

   public void setMsnConfirmarRastro(String msnConfirmarRastro) {
      this.msnConfirmarRastro = msnConfirmarRastro;
   }

   public String getMsnConfirmarRastroHistorico() {
      return msnConfirmarRastroHistorico;
   }

   public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
      this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
   }

   public BigInteger getBackUp() {
      return backUp;
   }

   public void setBackUp(BigInteger backUp) {
      this.backUp = backUp;
   }

   public String getNombreTablaRastro() {
      return nombreTablaRastro;
   }

   public void setNombreTablaRastro(String nombreTablaRastro) {
      this.nombreTablaRastro = nombreTablaRastro;
   }

   public List<TercerosSucursales> getListTercerosSucursales() {
      try {
         if (listTercerosSucursales == null) {
            if (terceroTablaSeleccionado != null) {
               listTercerosSucursales = administrarTercero.obtenerListTercerosSucursales(terceroTablaSeleccionado.getSecuencia());
               if (listTercerosSucursales != null) {
                  for (int i = 0; i < listTercerosSucursales.size(); i++) {
                     if (listTercerosSucursales.get(i).getCiudad() == null) {
                        listTercerosSucursales.get(i).setCiudad(new Ciudades());
                     }
                  }
               }
            }
         }
         return listTercerosSucursales;
      } catch (Exception e) {
         log.warn("Error en getListTercerosSucursales : " + e.toString());
         return null;
      }
   }

   public void setListTercerosSucursales(List<TercerosSucursales> listTercerosSucursales) {
      this.listTercerosSucursales = listTercerosSucursales;
   }

   public List<TercerosSucursales> getFiltrarListTercerosSucursales() {
      return filtrarListTercerosSucursales;
   }

   public void setFiltrarListTercerosSucursales(List<TercerosSucursales> filtrarListTercerosSucursales) {
      this.filtrarListTercerosSucursales = filtrarListTercerosSucursales;
   }

   public List<Empresas> getLovEmpresas() {
      lovEmpresas = administrarTercero.listEmpresas();
      return lovEmpresas;
   }

   public void setLovEmpresas(List<Empresas> lovEmpresas) {
      this.lovEmpresas = lovEmpresas;
   }

   public List<Empresas> getFiltrarListEmpresas() {
      return filtrarListEmpresas;
   }

   public void setFiltrarListEmpresas(List<Empresas> filtrarListEmpresas) {
      this.filtrarListEmpresas = filtrarListEmpresas;
   }

   public Empresas getEmpresaActual() {
      return empresaActual;
   }

   public void setEmpresaActual(Empresas empresaActual) {
      this.empresaActual = empresaActual;
   }

   public Empresas getEmpresaSeleccionada() {
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
      this.empresaSeleccionada = empresaSeleccionada;
   }

   public List<Ciudades> getLovCiudades() {
      if (lovCiudades == null) {
         lovCiudades = administrarTercero.listCiudades();
      }
      return lovCiudades;
   }

   public void setLovCiudades(List<Ciudades> lovCiudades) {
      this.lovCiudades = lovCiudades;
   }

   public List<Ciudades> getFiltrarListCiudades() {
      return filtrarListCiudades;
   }

   public void setFiltrarListCiudades(List<Ciudades> filtrarListCiudades) {
      this.filtrarListCiudades = filtrarListCiudades;
   }

   public Ciudades getCiudadSeleccionada() {
      return ciudadSeleccionada;
   }

   public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
      this.ciudadSeleccionada = ciudadSeleccionada;
   }

   public List<Terceros> getLovTerceroConsolidador() {
      if (empresaActual.getSecuencia() != null) {
         lovTerceroConsolidador = administrarTercero.obtenerListTerceros(empresaActual.getSecuencia());
      }
      return lovTerceroConsolidador;
   }

   public void setLovTerceroConsolidador(List<Terceros> lovTerceroConsolidador) {
      this.lovTerceroConsolidador = lovTerceroConsolidador;
   }

   public List<Terceros> getFiltrarListTerceroConsolidador() {
      return filtrarListTerceroConsolidador;
   }

   public void setFiltrarListTerceroConsolidador(List<Terceros> filtrarListTerceroConsolidador) {
      this.filtrarListTerceroConsolidador = filtrarListTerceroConsolidador;
   }

   public Terceros getTerceroCSeleccionado() {
      return terceroCSeleccionado;
   }

   public void setTerceroCSeleccionado(Terceros terceroCSeleccionado) {
      this.terceroCSeleccionado = terceroCSeleccionado;
   }

   public TercerosSucursales getEditarTerceroSucursal() {
      return editarTerceroSucursal;
   }

   public void setEditarTerceroSucursal(TercerosSucursales editarTerceroSucursal) {
      this.editarTerceroSucursal = editarTerceroSucursal;
   }

   public long getNitConsolidado() {
      return nitConsolidado;
   }

   public void setNitConsolidado(long nitConsolidado) {
      this.nitConsolidado = nitConsolidado;
   }

   public TercerosSucursales getNuevoTerceroSucursal() {
      return nuevoTerceroSucursal;
   }

   public void setNuevoTerceroSucursal(TercerosSucursales nuevoTerceroSucursal) {
      this.nuevoTerceroSucursal = nuevoTerceroSucursal;
   }

   public TercerosSucursales getDuplicarTerceroSucursal() {
      return duplicarTerceroSucursal;
   }

   public void setDuplicarTerceroSucursal(TercerosSucursales duplicarTerceroSucursal) {
      this.duplicarTerceroSucursal = duplicarTerceroSucursal;
   }

   public Terceros getTerceroTablaSeleccionado() {
      return terceroTablaSeleccionado;
   }

   public void setTerceroTablaSeleccionado(Terceros terceroTablaSeleccionado) {
      this.terceroTablaSeleccionado = terceroTablaSeleccionado;
   }

   public TercerosSucursales getTerceroSucursalTablaSeleccionado() {
      return terceroSucursalTablaSeleccionado;
   }

   public void setTerceroSucursalTablaSeleccionado(TercerosSucursales terceroSucursalTablaSeleccionado) {
      this.terceroSucursalTablaSeleccionado = terceroSucursalTablaSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTerceros");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public String getInfoRegistroTerceroSucursal() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTercerosSucursales");
      infoRegistroTerceroSucursal = String.valueOf(tabla.getRowCount());
      return infoRegistroTerceroSucursal;
   }

   public String getInfoRegistroLovTercero() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovBuscarTercero");
      infoRegistroLovTercero = String.valueOf(tabla.getRowCount());
      return infoRegistroLovTercero;
   }

   public String getInfoRegistroCiudad2() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCiudadTS");
      infoRegistroCiudad2 = String.valueOf(tabla.getRowCount());
      return infoRegistroCiudad2;
   }

   public String getInfoRegistroCiudad1() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCiudad");
      infoRegistroCiudad1 = String.valueOf(tabla.getRowCount());
      return infoRegistroCiudad1;
   }

   public String getInfoRegistroTerceroConsolidador() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTercero");
      infoRegistroTerceroConsolidador = String.valueOf(tabla.getRowCount());
      return infoRegistroTerceroConsolidador;
   }

   public String getInfoRegistroEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEmpresas");
      infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresa;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
      this.infoRegistroEmpresa = infoRegistroEmpresa;
   }

   public void setInfoRegistroTerceroSucursal(String infoRegistroTerceroSucursal) {
      this.infoRegistroTerceroSucursal = infoRegistroTerceroSucursal;
   }

   public void setInfoRegistroCiudad2(String infoRegistroCiudad2) {
      this.infoRegistroCiudad2 = infoRegistroCiudad2;
   }

   public void setInfoRegistroCiudad1(String infoRegistroCiudad1) {
      this.infoRegistroCiudad1 = infoRegistroCiudad1;
   }

   public void setInfoRegistroLovTercero(String infoRegistroLovTercero) {
      this.infoRegistroLovTercero = infoRegistroLovTercero;
   }

   public void setInfoRegistroTerceroConsolidador(String infoRegistroTerceroConsolidador) {
      this.infoRegistroTerceroConsolidador = infoRegistroTerceroConsolidador;
   }

   public List<Terceros> getListTercerosLOV() {
      if (listTercerosLOV == null) {
         listTercerosLOV = administrarTercero.obtenerListTerceros(empresaActual.getSecuencia());
      }
      return listTercerosLOV;
   }

   public void setListTercerosLOV(List<Terceros> listTercerosLOV) {
      this.listTercerosLOV = listTercerosLOV;
   }

   public List<Terceros> getFiltrarListTerceroLOV() {
      return filtrarListTerceroLOV;
   }

   public void setFiltrarListTerceroLOV(List<Terceros> filtrarListTerceroLOV) {
      this.filtrarListTerceroLOV = filtrarListTerceroLOV;
   }

   public Terceros getTerceroLOVSeleccionado() {
      return terceroLOVSeleccionado;
   }

   public void setTerceroLOVSeleccionado(Terceros terceroLOVSeleccionado) {
      this.terceroLOVSeleccionado = terceroLOVSeleccionado;
   }

   public String getAltoTablaTercero() {
      return altoTablaTercero;
   }

   public void setAltoTablaTercero(String altoTablaTercero) {
      this.altoTablaTercero = altoTablaTercero;
   }

   public String getAltoTablaReg() {
      if (altoTablaTercero == "123") {
         altoTablaReg = "5";
      } else {
         altoTablaReg = "6";
      }
      return altoTablaReg;
   }

   public void setAltoTablaReg(String altoTablaReg) {
      this.altoTablaReg = altoTablaReg;
   }

   public String getAltoTablaSucursal() {
      return altoTablaSucursal;
   }

   public void setAltoTablaSucursal(String altoTablaSucursal) {
      this.altoTablaSucursal = altoTablaSucursal;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }
}
