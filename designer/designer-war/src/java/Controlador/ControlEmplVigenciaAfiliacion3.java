/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasAfiliaciones3Interface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import Entidades.Ciudades;
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
import Entidades.VigenciasAfiliaciones;
import Entidades.Empleados;
import Entidades.TiposEntidades;
import Entidades.Terceros;
import Entidades.EstadosAfiliaciones;
import Entidades.TercerosSucursales;

/**
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlEmplVigenciaAfiliacion3 implements Serializable {

   private static Logger log = Logger.getLogger(ControlEmplVigenciaAfiliacion3.class);

   @EJB
   AdministrarVigenciasAfiliaciones3Interface administrarVigenciasAfiliaciones3;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //Vigencias Afiliaciones
   private List<VigenciasAfiliaciones> listVigenciasAfiliaciones;
   private List<VigenciasAfiliaciones> filtrarVigenciasAfiliaciones;
   private VigenciasAfiliaciones vigenciaSeleccionada;
   //Tipos Entidades
   private List<TiposEntidades> listTiposEntidades;
   private TiposEntidades tipoEntidadSeleccionado;
   private List<TiposEntidades> filtrarTiposEntidades;
   //Terceros
   private List<Terceros> listTerceros;
   private Terceros terceroSeleccionado;
   private List<Terceros> filtrarTerceros;
   //EstadosAfiliaciones
   private List<EstadosAfiliaciones> listEstadosAfiliaciones;
   private EstadosAfiliaciones estadoSSeleccionado;
   private List<EstadosAfiliaciones> filtrarEstadosAfiliaciones;
   //Empleado
   private Empleados empleado;
   //Tipo Actualizacion
   private int tipoActualizacion;
   //Autocompletar
   private boolean permitirIndexVA;
   //Activo/Desactivo VP Crtl + F11
   private int banderaVA;
   //Columnas Tabla VP
   private Column vAFechaInicial, vAFechaFinal, vATercero, vATipoEntidad, vANITTercero, vACodigo, vAEstadoAfiliacion, vAObservaciones;
   //Otros
   private boolean aceptar;
   //modificar
   private List<VigenciasAfiliaciones> listVAModificar;
   private boolean guardado;
   //crear VA
   public VigenciasAfiliaciones nuevaVigenciaA;
   private List<VigenciasAfiliaciones> listVACrear;
   private BigInteger l;
   private int k;
   //borrar VA
   private List<VigenciasAfiliaciones> listVABorrar;
   //editar celda
   private VigenciasAfiliaciones editarVA;
   //Indice de celdas VigenciaProrrateo / VigenciaProrrateoProyecto
   private int cualCeldaVA, tipoListaVA;
   //duplicar
   private VigenciasAfiliaciones duplicarVA;
   //Variables Autompletar
   private String tiposEntidades, terceros, estadosAfiliacion;
   private long nit;
   //Duplicar Vigencia Prorrateo
   private boolean cambioVigenciaA;
   private VigenciasAfiliaciones vigenciaValidaciones;
   private Date fechaContratacion;
   private Date fechaParametro;
   private Date fechaIni, fechaFin;
   //ALTO TABLA
   private String altoTabla;
   private String infoRegistro;
   //RASTRO
   private String infoRegistroTipoEntidad, infoRegistroTercero, infoRegistroEstado;
   //Para validaciones de tipo entidades y terceros
   private int tipoValidacion;
   //
   private DataTable tablaC;
   //
   private boolean activarLOV;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEmplVigenciaAfiliacion3() {
      tipoValidacion = 0;
      empleado = new Empleados();
      //Otros
      aceptar = true;
      //borrar aficiones
      listVABorrar = new ArrayList<VigenciasAfiliaciones>();
      //crear aficiones
      k = 0;
      //modificar aficiones
      listVAModificar = new ArrayList<VigenciasAfiliaciones>();
      //editar
      editarVA = new VigenciasAfiliaciones();
      tipoListaVA = 0;
      //guardar
      guardado = true;
      banderaVA = 0;
      permitirIndexVA = true;
      //cualCeldaVA = 0;
      listEstadosAfiliaciones = null;
      estadoSSeleccionado = new EstadosAfiliaciones();
      listTerceros = null;
      //terceroSeleccionado = new Terceros();
      terceroSeleccionado = null;
      tipoEntidadSeleccionado = null;
      estadoSSeleccionado = null;

      listTiposEntidades = null;
      tipoEntidadSeleccionado = new TiposEntidades();
      nuevaVigenciaA = new VigenciasAfiliaciones();
      nuevaVigenciaA.setTipoentidad(new TiposEntidades());
      nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
      nuevaVigenciaA.getTercerosucursal().setCiudad(new Ciudades());
      nuevaVigenciaA.getTercerosucursal().setTercero(new Terceros());
      nuevaVigenciaA.setEstadoafiliacion(new EstadosAfiliaciones());
      listVACrear = new ArrayList<VigenciasAfiliaciones>();
      fechaContratacion = new Date();
      cambioVigenciaA = false;
      altoTabla = "292";
      vigenciaSeleccionada = null;
      activarLOV = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "emplvigenciaafiliacion3";
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

   public void limpiarListasValor() {
      listEstadosAfiliaciones = null;
      listTerceros = null;
      listTiposEntidades = null;
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
         administrarVigenciasAfiliaciones3.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   //EMPLEADO DE LA VIGENCIA 
   /**
    * Metodo que recibe la secuencia empleado desde la pagina anterior y obtiene
    * el empleado referenciado
    *
    * @param empl Secuencia del Empleado
    */
   public void recibirEmpleado(BigInteger empl) {
      empleado = administrarVigenciasAfiliaciones3.obtenerEmpleado(empl);
      fechaContratacion = administrarVigenciasAfiliaciones3.fechaContratacion(empleado);
      getListVigenciasAfiliaciones();
      if (listVigenciasAfiliaciones != null) {
         if (!listVigenciasAfiliaciones.isEmpty()) {
            vigenciaSeleccionada = listVigenciasAfiliaciones.get(0);
         }
      }
   }

   ///////////////////////////////////////////////////////////////////////////
   /**
    * Modifica los elementos de la tabla VigenciaProrrateo que no usan
    * autocomplete
    *
    * @param indice Fila donde se efectu el cambio
    */
   public void modificarVA(VigenciasAfiliaciones va) {
//      log.info("ControlEmplVigenciaAfiliacion3.modificarVA() vigenciaSeleccionada.getFechafinal(): " + vigenciaSeleccionada.getFechafinal());
//      log.info("ControlEmplVigenciaAfiliacion3.modificarVA() vigenciaSeleccionada.getFechainicial(): " + vigenciaSeleccionada.getFechainicial());
      vigenciaSeleccionada = va;
      vigenciaValidaciones = vigenciaSeleccionada;
      boolean validacion = true;
      if (cualCeldaVA != 0 && cualCeldaVA != 1) {
         validacion = validarModificacionRegistroTabla();
      }
      if (validacion == true) {
         if (!listVACrear.contains(vigenciaSeleccionada)) {
            if (listVAModificar.isEmpty()) {
               listVAModificar.add(vigenciaSeleccionada);
            } else if (!listVAModificar.contains(vigenciaSeleccionada)) {
               listVAModificar.add(vigenciaSeleccionada);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         vigenciaSeleccionada.getTercerosucursal().getTercero().setNit(nit);
         cambioVigenciaA = true;
      } else {
         VigenciasAfiliaciones cambio = administrarVigenciasAfiliaciones3.vigenciaAfiliacionSecuencia(vigenciaSeleccionada.getSecuencia());
         listVigenciasAfiliaciones.set(listVigenciasAfiliaciones.indexOf(vigenciaSeleccionada), cambio);
      }
      RequestContext.getCurrentInstance().update("form:datosVAVigencia");
   }

   public boolean validarFechasRegistro(int i) {
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      log.info("fecha parametro : " + fechaParametro);
      boolean retorno = true;
//      log.info("ControlEmplVigenciaAfiliacion3.validarFechasRegistro i: " + i);
      if (i == 0) {
         VigenciasAfiliaciones auxiliar;
         auxiliar = vigenciaSeleccionada;
//         log.info("Auxiliar: " + auxiliar);
         retorno = auxiliar.getFechainicial().after(fechaParametro);
         if (auxiliar.getFechafinal() != null && retorno) {
            retorno = auxiliar.getFechainicial().before(auxiliar.getFechafinal());
         }
      }
      if (i == 1) {
         retorno = nuevaVigenciaA.getFechainicial().after(fechaParametro);
         if (nuevaVigenciaA.getFechafinal() != null && retorno) {
            retorno = nuevaVigenciaA.getFechainicial().before(nuevaVigenciaA.getFechafinal());
         }
         if (nuevaVigenciaA.getFechafinal() == null && retorno) {
            retorno = nuevaVigenciaA.getFechainicial().after(fechaParametro);
         }
      }
      if (i == 2) {
         retorno = duplicarVA.getFechainicial().after(fechaParametro);
         if (duplicarVA.getFechafinal() != null && retorno) {
            retorno = duplicarVA.getFechainicial().before(duplicarVA.getFechafinal());
         }
         if (duplicarVA.getFechafinal() == null && retorno) {
            retorno = duplicarVA.getFechainicial().after(fechaParametro);
         }
      }
//      log.info("Valor retorno: " + retorno);
      return retorno;
   }

   public void modificarFechas(VigenciasAfiliaciones va, int c) {
//      log.info("ControlEmplVigenciaAfiliacion.modificarFechas");
      vigenciaSeleccionada = va;
      if (vigenciaSeleccionada.getFechainicial() != null) {
         boolean retorno = validarFechasRegistro(0);
         if (retorno) {
            if (validarFechasRegistroTabla()) {
//               log.info("FechasRegistroTabla true");
               cambiarIndiceVA(vigenciaSeleccionada, c);
               modificarVA(vigenciaSeleccionada);
            }
//            log.info("retorno true");
         } else {
            vigenciaSeleccionada.setFechafinal(fechaFin);
            vigenciaSeleccionada.setFechainicial(fechaIni);

            RequestContext.getCurrentInstance().update("form:datosVAVigencia");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         vigenciaSeleccionada.setFechainicial(fechaIni);
         RequestContext.getCurrentInstance().update("form:datosVAVigencia");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void modificarFechasFinales(VigenciasAfiliaciones vAfiliacion, int c) {
      vigenciaSeleccionada = vAfiliacion;
//      log.info("ControlEmplVigenciaAfiliacion.modificarFechas vigenciaSeleccionada.getFechainicial(): " + vigenciaSeleccionada.getFechainicial());
      if (vigenciaSeleccionada.getFechainicial() != null) {
         boolean retorno = validarFechasRegistro(0);
//         log.info("ControlEmplVigenciaAfiliacion.modificarFechas retorno: " + retorno);
         if (retorno) {
            cambiarIndiceVA(vAfiliacion, c);
            modificarVA(vAfiliacion);
         } else {
            cambiarIndiceVA(vAfiliacion, c);
            vigenciaSeleccionada.setFechafinal(fechaFin);
            vigenciaSeleccionada.setFechainicial(fechaIni);
            RequestContext.getCurrentInstance().update("form:datosVAVigencia");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         vigenciaSeleccionada.setFechainicial(fechaIni);
         RequestContext.getCurrentInstance().update("form:datosVAVigencia");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   /**
    * Metodo que modifica los cambios efectuados en la tabla VigenciaProrrateo
    * de la pagina
    *
    * @param indice Fila en la cual se realizo el cambio
    * @param confirmarCambio Activa la validación de modificación
    * @param valor Nombre del tercero o de la entidad a confirmar
    */
   public void modificarVA(VigenciasAfiliaciones va, String confirmarCambio, String valor) {
      vigenciaSeleccionada = va;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
         RequestContext.getCurrentInstance().update("form:listaValores");

         vigenciaSeleccionada.getTipoentidad().setNombre(tiposEntidades);
         for (int i = 0; i < listTiposEntidades.size(); i++) {
            if (listTiposEntidades.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaValidaciones = new VigenciasAfiliaciones();
            vigenciaValidaciones.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));
            //vigenciaSeleccionada.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));
            vigenciaSeleccionada = va;
            boolean cambio = validarModificacionRegistroTabla();
            if (cambio == false) {
               cambioVigenciaA = false;
               vigenciaSeleccionada.getTipoentidad().setNombre(tiposEntidades);
            } else {
               cambioVigenciaA = true;
               vigenciaSeleccionada.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));
            }
         } else {
            permitirIndexVA = false;
            contarRegistrosTipoEntidad();
            RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("TERCEROS")) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         vigenciaSeleccionada.getTercerosucursal().getTercero().setNombre(terceros);

         for (int i = 0; i < listTerceros.size(); i++) {
            if (listTerceros.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaValidaciones = new VigenciasAfiliaciones();
            vigenciaValidaciones.setTercerosucursal(new TercerosSucursales());
            vigenciaValidaciones.getTercerosucursal().setTercero(new Terceros());
            vigenciaValidaciones.getTercerosucursal().setTercero(listTerceros.get(indiceUnicoElemento));
            boolean cambio = validarModificacionRegistroTabla();
            if (cambio == false) {
               cambioVigenciaA = false;
               vigenciaSeleccionada.getTercerosucursal().getTercero().setNombre(terceros);
            } else {
               cambioVigenciaA = true;
               boolean banderaEncuentra = false;
               int posicion = -1;
               List<TercerosSucursales> listTercerosSucursales = administrarVigenciasAfiliaciones3.listTercerosSucursales();
               for (int i = 0; i < listTercerosSucursales.size(); i++) {
                  if (listTercerosSucursales.get(i).getTercero().getNombre().equalsIgnoreCase(terceroSeleccionado.getNombre())) {
                     banderaEncuentra = true;
                     posicion = i;
                  }
               }
               if ((banderaEncuentra == true) && (posicion != -1)) {
                  vigenciaSeleccionada.setTercerosucursal(listTercerosSucursales.get(posicion));
               }
            }
         } else {
            permitirIndexVA = false;
            contarRegistrosTercero();
            RequestContext.getCurrentInstance().update("form:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("ESTADOAFILIACION")) {
//            RequestContext.getCurrentInstance().update("form:listaValores");
//            if (!valor.isEmpty()) {
//                vigenciaSeleccionada.getEstadoafiliacion().setNombre(estadosAfiliacion);
//
//                for (int i = 0; i < listEstadosAfiliaciones.size(); i++) {
//                    if (listEstadosAfiliaciones.get(i).getNombre().startsWith(valor.toUpperCase())) {
//                        indiceUnicoElemento = i;
//                        coincidencias++;
//                    }
//                }
//                if (coincidencias == 1) {
//                    cambioVigenciaA = true;
//                    vigenciaSeleccionada.setEstadoafiliacion(listEstadosAfiliaciones.get(indiceUnicoElemento));
////                    cambioVigenciaA = true;
//                } else {
//                    permitirIndexVA = false;
//                    contarRegistrosEstado();
//                    RequestContext.getCurrentInstance().update("form:EstadoAfiliacionDialogo");
//                    RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').show()");
//                    tipoActualizacion = 0;
//                }
//            } else {
//                vigenciaSeleccionada.setEstadoafiliacion(new EstadosAfiliaciones());
//                if (guardado) {
//                    guardado = false;
//                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
//                    RequestContext.getCurrentInstance().update("form:datosVAVigencia");
//                }
//            }
      }
      if (coincidencias == 1) {
         if (!listVACrear.contains(vigenciaSeleccionada)) {

            if (listVAModificar.isEmpty()) {
               listVAModificar.add(vigenciaSeleccionada);
            } else if (!listVAModificar.contains(vigenciaSeleccionada)) {
               listVAModificar.add(vigenciaSeleccionada);
            }
         }
         cambioVigenciaA = true;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosVAVigencia");
         }
      }
   }

   public void modificarVAEstado(VigenciasAfiliaciones va) {
      vigenciaSeleccionada = va;
      RequestContext context = RequestContext.getCurrentInstance();
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext.getCurrentInstance().update("form:listaValores");
      if (vigenciaSeleccionada.getEstadoafiliacion().getNombre() != null) {
         if (!vigenciaSeleccionada.getEstadoafiliacion().getNombre().isEmpty()) {
            vigenciaSeleccionada.getEstadoafiliacion().setNombre(estadosAfiliacion);

            for (int i = 0; i < listEstadosAfiliaciones.size(); i++) {
               if (listEstadosAfiliaciones.get(i).getNombre().startsWith(vigenciaSeleccionada.getEstadoafiliacion().getNombre().toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               cambioVigenciaA = true;
               vigenciaSeleccionada.setEstadoafiliacion(listEstadosAfiliaciones.get(indiceUnicoElemento));
               cambioVigenciaA = true;
            } else {
               permitirIndexVA = false;
               contarRegistrosEstado();
               RequestContext.getCurrentInstance().update("form:EstadoAfiliacionDialogo");
               RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            vigenciaSeleccionada.setEstadoafiliacion(new EstadosAfiliaciones());
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
               RequestContext.getCurrentInstance().update("form:datosVAVigencia");
            }
         }
      } else {
         vigenciaSeleccionada.setEstadoafiliacion(new EstadosAfiliaciones());
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosVAVigencia");
         }
      }
   }

   /**
    * Metodo que obtiene los valores de los dialogos para realizar los
    * autocomplete de los campos (VigenciaProrrateo)
    *
    * @param tipoNuevo Tipo de operacion: Nuevo Registro - Duplicar Registro
    * @param Campo Campo que toma el cambio de autocomplete
    */
   public void valoresBackupAutocompletarVA(int tipoNuevo, String Campo) {
      if (Campo.equals("TIPOENTIDAD")) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (tipoNuevo == 1) {
            tiposEntidades = nuevaVigenciaA.getTipoentidad().getNombre();
         } else if (tipoNuevo == 2) {
            tiposEntidades = duplicarVA.getTipoentidad().getNombre();
         }
      } else if (Campo.equals("TERCERO")) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (tipoNuevo == 1) {
            terceros = nuevaVigenciaA.getTercerosucursal().getTercero().getNombre();
         } else if (tipoNuevo == 2) {
            terceros = duplicarVA.getTercerosucursal().getTercero().getNombre();
         }
      } else if (Campo.equals("ESTADOAFILIACION")) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (tipoNuevo == 1) {
            estadosAfiliacion = nuevaVigenciaA.getEstadoafiliacion().getNombre();
         } else if (tipoNuevo == 2) {
            estadosAfiliacion = duplicarVA.getEstadoafiliacion().getNombre();
         }
      }
   }

   /**
    *
    * Metodo que genera el auto completar de un proceso nuevoRegistro o
    * duplicarRegistro de VigenciasProrrateos
    *
    * @param confirmarCambio Tipo de elemento a modificar: CENTROCOSTO -
    * PROYECTO
    * @param valorConfirmar Valor ingresado para confirmar
    * @param tipoNuevo Tipo de proceso: Nuevo - Duplicar
    */
   public void autocompletarNuevoyDuplicadoVA(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (tipoNuevo == 1) {
            nuevaVigenciaA.getTipoentidad().setNombre(tiposEntidades);
         } else if (tipoNuevo == 2) {
            duplicarVA.getTipoentidad().setNombre(tiposEntidades);
         }
         for (int i = 0; i < listTiposEntidades.size(); i++) {
            if (listTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigenciaA.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoEntidadVA");
            } else if (tipoNuevo == 2) {
               duplicarVA.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEntidadVA");
            }
            RequestContext.getCurrentInstance().execute("PF('precaucionTercero').show()");
         } else {
            RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoEntidadVA");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEntidadVA");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (tipoNuevo == 1) {
            nuevaVigenciaA.getTercerosucursal().getTercero().setNombre(terceros);
         } else if (tipoNuevo == 2) {
            duplicarVA.getTercerosucursal().getTercero().setNombre(terceros);
         }
         for (int i = 0; i < listTerceros.size(); i++) {
            if (listTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }

         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigenciaA.getTercerosucursal().setTercero(listTerceros.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroVA");
            } else if (tipoNuevo == 2) {
               duplicarVA.getTercerosucursal().setTercero(listTerceros.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroVA");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroVA");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoTerceroVA");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("ESTADOAFILIACION")) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaVigenciaA.getEstadoafiliacion().setNombre(terceros);
            } else if (tipoNuevo == 2) {
               duplicarVA.getEstadoafiliacion().setNombre(terceros);
            }
            for (int i = 0; i < listEstadosAfiliaciones.size(); i++) {
               if (listEstadosAfiliaciones.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaVigenciaA.setEstadoafiliacion(listEstadosAfiliaciones.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstadoAfiliacionVA");
               } else if (tipoNuevo == 2) {
                  duplicarVA.setEstadoafiliacion(listEstadosAfiliaciones.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoEstadoAfiliacionVA");
               }
            } else {
               RequestContext.getCurrentInstance().update("form:EstadoAfiliacionDialogo");
               RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstadoAfiliacionVA");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoEstadoAfiliacionVA");
               }
            }
         }
      } else if (tipoNuevo == 1) {
         nuevaVigenciaA.setEstadoafiliacion(new EstadosAfiliaciones());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstadoAfiliacionVA");
      } else if (tipoNuevo == 2) {
         duplicarVA.setEstadoafiliacion(new EstadosAfiliaciones());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoEstadoAfiliacionVA");
      }
   }

   public boolean validarInformacionRegistro(int tipo) {
      boolean retorno = true;
      if (tipo == 1) {
         if ((nuevaVigenciaA.getFechainicial() == null) || (nuevaVigenciaA.getTipoentidad().getNombre().isEmpty()) || (nuevaVigenciaA.getTercerosucursal().getTercero().getNombre().isEmpty())) {
//         if ((nuevaVigenciaA.getFechainicial() == null) || (nuevaVigenciaA.getTipoentidad().getSecuencia() == null) || (nuevaVigenciaA.getTercerosucursal().getSecuencia() == null)) {
            retorno = false;
         }
         if ((nuevaVigenciaA.getObservacion() != null) && (nuevaVigenciaA.getObservacion().length() > 100)) {
            retorno = false;
         }
      }
      if (tipo == 2) {
         if ((duplicarVA.getFechainicial() == null) || (duplicarVA.getTipoentidad().getNombre().isEmpty()) || (duplicarVA.getTercerosucursal().getTercero().getNombre().isEmpty())) {
//         if ((duplicarVA.getFechainicial() == null) || (duplicarVA.getTipoentidad().getNombre().isEmpty()) || (duplicarVA.getTercerosucursal().getTercero().getNombre().isEmpty())) {
            retorno = false;
         }
         if ((duplicarVA.getObservacion() != null) && (duplicarVA.getObservacion().length() > 100)) {
            retorno = false;
         }
      }
      return retorno;
   }

   public boolean validarExistenciaTipoEntidad(int i) {
      boolean retorno = true;
      List<VigenciasAfiliaciones> listMismoTipo = new ArrayList<VigenciasAfiliaciones>();
      VigenciasAfiliaciones provisional = null;
      if (i == 1) {
         provisional = nuevaVigenciaA;
      }
      if (i == 2) {
         provisional = duplicarVA;
      }
      if (listVigenciasAfiliaciones != null) {
         for (int j = 0; j < listVigenciasAfiliaciones.size(); j++) {
            if (listVigenciasAfiliaciones.get(j).getTipoentidad().getSecuencia().equals(provisional.getTipoentidad().getSecuencia())) {
               listMismoTipo.add(listVigenciasAfiliaciones.get(j));
            }
         }
      }
      if (!listMismoTipo.isEmpty()) {
         for (VigenciasAfiliaciones recVA : listMismoTipo) {
            if (recVA.getFechafinal() != null) {
               if (recVA.getFechafinal().after(provisional.getFechainicial())) {
                  retorno = false;
               }
            } else {
               retorno = false;
            }
         }
      }
      return retorno;
   }

   public boolean validacionTercerorSucursalesNuevoRegistro(int i) {
//      log.info("Entro en validacionTercerorSucursalesNuevoRegistro con i = " + i);
      boolean retorno = true;
      if (i == 1) {
         Long r = administrarVigenciasAfiliaciones3.validacionTercerosSurcursalesNuevaVigencia(empleado.getSecuencia(), nuevaVigenciaA.getFechainicial(), nuevaVigenciaA.getEstadoafiliacion().getSecuencia(), nuevaVigenciaA.getTercerosucursal().getTercero().getSecuencia());
         if (r > 0) {
            retorno = false;
         }
      }
      if (i == 2) {
         Long r = administrarVigenciasAfiliaciones3.validacionTercerosSurcursalesNuevaVigencia(empleado.getSecuencia(), duplicarVA.getFechainicial(), duplicarVA.getEstadoafiliacion().getSecuencia(), duplicarVA.getTercerosucursal().getTercero().getSecuencia());
         if (r > 0) {
            retorno = false;
         }
      }
      return retorno;
   }

   public boolean validacionFechasNuevoRegistro(int i) {
      boolean retorno = true;
      if (i == 1) {
         if ((nuevaVigenciaA.getFechainicial() != null) && (nuevaVigenciaA.getFechafinal() != null)) {
            if (nuevaVigenciaA.getFechainicial().after(nuevaVigenciaA.getFechafinal())) {
               retorno = false;
            }
         }
         if ((nuevaVigenciaA.getFechainicial() != null) && (nuevaVigenciaA.getFechafinal() == null)) {
            retorno = true;
         }
      }
      if (i == 2) {
         if ((duplicarVA.getFechainicial() != null) && (duplicarVA.getFechafinal() != null)) {
            if (duplicarVA.getFechainicial().after(duplicarVA.getFechafinal())) {
               retorno = false;
            }
         }
         if ((duplicarVA.getFechainicial() != null) && (duplicarVA.getFechafinal() == null)) {
            retorno = true;
         }
      }
      return retorno;
   }

   public void dialogoCamposNulos() {
      RequestContext.getCurrentInstance().execute("PF('errorIngresoRegistro').show()");
   }

   public void dialogoFechasErroneas() {
      RequestContext.getCurrentInstance().execute("PF('errorFechasRegistro').show()");
   }

   public void dialogoFechaContratacionError() {
      RequestContext.getCurrentInstance().execute("PF('errorFechaMenorContratacion').show()");
   }

   public void dialogoErrorTercero() {
      RequestContext.getCurrentInstance().execute("PF('errorTerceroSucursal').show()");
   }

   public boolean validarFechasRegistroTabla() {
      boolean retorno = true;
      List<VigenciasAfiliaciones> listAuxiliar = null;
      listAuxiliar = listVigenciasAfiliaciones;
      boolean validacionCamposNulos;
      boolean validacionFechas;
      boolean validacionFechaMayorContratacion;
      boolean validacionDiaInicioFecha;
      validacionCamposNulos = camposModificacionRegistro(listAuxiliar);
      if (validacionCamposNulos) {
         validacionFechas = fechasModificacionRegistro(listAuxiliar);
         if (!validacionFechas) {
            retorno = false;
            dialogoFechasErroneas();
         }
         validacionFechaMayorContratacion = fechaContratacionModificacionEmpleado();
         if (!validacionFechaMayorContratacion) {
            dialogoFechaContratacionError();
         }
         if (vigenciaValidaciones.getFechainicial().getDate() > 1) {
            RequestContext.getCurrentInstance().execute("PF('errorFechaInicialRegistro').show()");
         }
         if (!validacionFechas) {
            retorno = false;
         }
      } else {
         dialogoCamposNulos();
         retorno = false;
      }
      if (retorno) {
         listVigenciasAfiliaciones = listAuxiliar;

      }
      return retorno;
   }

   public boolean validarModificacionRegistroTabla() {
      //booleano que determina si las validaciones fueron cumplidas
      boolean retorno = true;
      //lista con la que se haran las validaciones
      List<VigenciasAfiliaciones> listAuxiliar = null;
      listAuxiliar = listVigenciasAfiliaciones;

      boolean validacionTiposEntidades;
      boolean validacionTerceroSucursal;
//      boolean validacionCamposNulos = camposModificacionRegistro(listAuxiliar);
//      if (validacionCamposNulos) {//Si no hay campos nulos
      if (tipoValidacion == 2) {
         validacionTerceroSucursal = terceroModificacionRegistro();
         if (validacionTerceroSucursal == false) {
            dialogoErrorTercero();
            retorno = false;
         }
      } else if (tipoValidacion == 1) {
         validacionTiposEntidades = tipoEntidadModificacionRegistro(listAuxiliar);
         if (validacionTiposEntidades == false) {
            RequestContext.getCurrentInstance().execute("PF('errorExistenciaTipoEntidad').show()");
            retorno = false;
         }
      }
//      } else {
//         //dialogoCamposNulos();
//         retorno = false;
//      }
      if (retorno) {
         listVigenciasAfiliaciones = listAuxiliar;

      }
      tipoValidacion = 0;
      return retorno;
   }

   public boolean fechaContratacionModificacionEmpleado() {
      boolean retorna = true;
      if (vigenciaSeleccionada.getFechainicial().before(fechaContratacion)) {
         retorna = false;
      }
      return retorna;
   }

   public boolean tipoEntidadModificacionRegistro(List<VigenciasAfiliaciones> listaAuxiliar) {
      boolean retorno = true;
      //Objeto con el tipo de entidad seleccionado
      VigenciasAfiliaciones provisional = vigenciaValidaciones;
      //Lista para llenar con los datos del tipo seleccionado
      List<VigenciasAfiliaciones> listAuxiliarTipoESeleccionado = new ArrayList<VigenciasAfiliaciones>();

      for (int cont = 0; cont < listaAuxiliar.size(); cont++) {
         if (listaAuxiliar.get(cont).getTipoentidad().getSecuencia() == provisional.getTipoentidad().getSecuencia()) {
            listAuxiliarTipoESeleccionado.add(listaAuxiliar.get(cont));
         }
      }
      ///////////--------////////////
      if (listAuxiliarTipoESeleccionado.size() >= 2) {
         if (provisional.getFechafinal() == null) {
            int conteo = 0;
            for (int de = 0; de < listAuxiliarTipoESeleccionado.size(); de++) {
               if (listAuxiliarTipoESeleccionado.get(de).getFechafinal() == null) {
                  conteo++;
               }
            }
            if (conteo > 0) {
               conteo = 0;
               for (int de = 0; de < listAuxiliarTipoESeleccionado.size(); de++) {
                  if (listAuxiliarTipoESeleccionado.get(de).getSecuencia() == provisional.getSecuencia()) {
                     conteo++;
                  }
               }
               if (conteo != 1) {
                  retorno = false;
               }
               if (conteo == 1) {
                  retorno = true;
               }
            } ///////////--------////////////
            else {
               VigenciasAfiliaciones auxiliar = listAuxiliarTipoESeleccionado.get(0);
               retorno = provisional.getFechainicial().after(auxiliar.getFechafinal());
            }
         } ///////////--------///////////////////////--------////////////
         else {
            boolean cambiosBefore = false;
            VigenciasAfiliaciones reg1 = listAuxiliarTipoESeleccionado.get(0);
            VigenciasAfiliaciones regN = listAuxiliarTipoESeleccionado.get(listAuxiliarTipoESeleccionado.size() - 1);
            ///////////--------////////////
            if ((provisional.getFechafinal().before(reg1.getFechainicial())) && (provisional.getFechainicial().before(reg1.getFechainicial()))) {
               cambiosBefore = true;
            }
            ///////////--------////////////
            if (regN.getFechafinal() != null) {
               if ((provisional.getFechafinal().after(regN.getFechafinal())) && (provisional.getFechainicial().after(regN.getFechafinal()))) {
                  cambiosBefore = true;
               }
            } else if (provisional.getFechainicial().after(regN.getFechafinal())) {
               cambiosBefore = true;
            }
            ///////////--------////////////
            if (cambiosBefore == true) {
               retorno = true;
            } else {
               boolean ubicoReg = false;
               int cambios = 0;
               for (int o = 1; o < listAuxiliarTipoESeleccionado.size() - 1; o++) {
                  if (listAuxiliarTipoESeleccionado.get(o + 1).getFechafinal() != null) {
                     if ((provisional.getFechainicial().after(listAuxiliarTipoESeleccionado.get(o - 1).getFechafinal())) && (provisional.getFechafinal().before(listAuxiliarTipoESeleccionado.get(o + 1).getFechainicial()))) {
                        cambios++;
                        ubicoReg = true;
                     }
                  }
               }
               ///////////--------////////////
               if (cambios == 1) {
                  if (ubicoReg == true) {
                     retorno = true;
                  }
               } ///////////--------////////////
               else {
                  retorno = false;
               }
            }
         }
      }
      ///////////--------///////////////////////--------////////////
      if (listAuxiliarTipoESeleccionado.size() == 1) {
         boolean ubico = false;
         if (provisional.getFechafinal() != null) {
            if ((provisional.getFechainicial().before(listAuxiliarTipoESeleccionado.get(0).getFechainicial())) && (provisional.getFechafinal().before(listAuxiliarTipoESeleccionado.get(0).getFechainicial()))) {
               ubico = true;
            } else if ((provisional.getFechainicial().after(listAuxiliarTipoESeleccionado.get(0).getFechafinal())) && (provisional.getFechafinal().after(listAuxiliarTipoESeleccionado.get(0).getFechafinal()))) {
               ubico = true;
            }
         } else if (listAuxiliarTipoESeleccionado.get(0).getFechafinal() != null) {
            if ((provisional.getFechainicial().after(listAuxiliarTipoESeleccionado.get(0).getFechafinal()))) {
               ubico = true;
            }
         } else {
            ubico = false;
         }
         retorno = ubico;
      }
      if (listAuxiliarTipoESeleccionado.size() == 0) {
         retorno = true;
      }
      return retorno;
   }

   public boolean camposModificacionRegistro(List<VigenciasAfiliaciones> listaAuxiliar) {
      boolean retorno = true;
      int posicion = listVigenciasAfiliaciones.indexOf(vigenciaSeleccionada);
      //Si alguno de los campos obligatorios es nulo 
      if ((listaAuxiliar.get(posicion).getFechainicial() == null) || (listaAuxiliar.get(posicion).getTipoentidad().getNombre().isEmpty()) || (listaAuxiliar.get(posicion).getTercerosucursal().getTercero().getNombre().isEmpty())) {
         dialogoCamposNulos();
         retorno = false;
      }
      //Si Observacion excede su limite de caracteres
      if ((listaAuxiliar.get(posicion).getObservacion() != null) && (listaAuxiliar.get(posicion).getObservacion().length() > 100)) {
         retorno = false;
      }
      return retorno;
   }

   public boolean fechasModificacionRegistro(List<VigenciasAfiliaciones> listaAuxuliar) {
      boolean retorno = true;
      VigenciasAfiliaciones auxiliar = null;
      auxiliar = listaAuxuliar.get(listVigenciasAfiliaciones.indexOf(vigenciaSeleccionada));
      if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() != null)) {
         if (auxiliar.getFechainicial().after(auxiliar.getFechafinal())) {
            retorno = false;
         }
      }
      if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() == null)) {
         retorno = true;
      }
      return retorno;
   }

   public boolean terceroModificacionRegistro() {
      boolean retorno = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      if (vigenciaSeleccionada.getEstadoafiliacion() == null) {
         vigenciaSeleccionada.setEstadoafiliacion(new EstadosAfiliaciones());
      }
      Long r = administrarVigenciasAfiliaciones3.validacionTercerosSurcursalesNuevaVigencia(empleado.getSecuencia(), vigenciaSeleccionada.getFechainicial(), vigenciaSeleccionada.getEstadoafiliacion().getSecuencia(), vigenciaSeleccionada.getTercerosucursal().getTercero().getSecuencia());
      if (r != null) {
         if (r > 0) {
            retorno = false;
         }
      }
      return retorno;
   }

   public boolean validarFechaContratacionEmpleado(int i) {
      boolean retorna = true;
      if (i == 1) {
         if (nuevaVigenciaA.getFechainicial().before(fechaContratacion)) {
            retorna = false;
         }
      }
      if (i == 2) {
         if (duplicarVA.getFechainicial().before(fechaContratacion)) {
            retorna = false;
         }
      }
      return retorna;
   }

   public boolean validarDiaInicialRegistro(int i) {
      boolean retorna = true;
      if (i == 1) {
         int dia = nuevaVigenciaA.getFechainicial().getDate();
         if (dia >= 2) {
            retorna = false;
         }
      }
      if (i == 2) {
         int dia = duplicarVA.getFechainicial().getDate();
         if (dia >= 2) {
            retorna = false;
         }
      }
      return retorna;
   }

   public boolean validarIngresoNuevoRegistro(int i) {
      boolean mensaje = true;
      boolean validacionCamposNulos;
      boolean validacionTiposEntidades;
      boolean validacionFechas;
      boolean validacionTerceroSucursal;
      //////----------////////////
      validacionCamposNulos = validarInformacionRegistro(i);
      if (!validacionCamposNulos) {
         dialogoCamposNulos();
      }
      //////----------////////////
      if (validacionCamposNulos) {
         validacionFechas = validacionFechasNuevoRegistro(i);
         if (!validacionFechas) {
            dialogoFechasErroneas();
         }
         //////----------////////////
         validacionTerceroSucursal = validacionTercerorSucursalesNuevoRegistro(i);
         if (!validacionTerceroSucursal) {
            dialogoErrorTercero();
         }
         //////----------////////////
         validacionTiposEntidades = validarExistenciaTipoEntidad(i);
         if (!validacionTiposEntidades) {
            RequestContext.getCurrentInstance().execute("PF('errorExistenciaTipoEntidad').show()");
         }
         if (!validacionFechas || !validacionTerceroSucursal || !validacionTiposEntidades) {
            mensaje = false;
         }
      } //////----------////////////
      else {
         mensaje = false;
      }
      return mensaje;
   }

   public void posicionNit() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      vigenciaSeleccionada = listVigenciasAfiliaciones.get(indice);
      cambiarIndiceVA(vigenciaSeleccionada, columna);
   }

   public void cambiarIndiceVA(VigenciasAfiliaciones va, int celda) {
      if (permitirIndexVA == true) {
         vigenciaSeleccionada = va;
         cualCeldaVA = celda;
         if (cualCeldaVA == 0) {
            anularLOV();
            fechaIni = vigenciaSeleccionada.getFechainicial();

         } else if (cualCeldaVA == 1) {
            anularLOV();
            fechaFin = vigenciaSeleccionada.getFechafinal();
            log.info("Asigno celda fechaFin: " + fechaFin);
         } else if (cualCeldaVA == 2) {
            habilitarLov();
            tiposEntidades = vigenciaSeleccionada.getTipoentidad().getNombre();

         } else if (cualCeldaVA == 3) {
            habilitarLov();
            terceros = vigenciaSeleccionada.getTercerosucursal().getTercero().getNombre();

         } else if (cualCeldaVA == 4) {
            anularLOV();
            nit = vigenciaSeleccionada.getTercerosucursal().getTercero().getNit();

         } else if (cualCeldaVA == 5) {
            anularLOV();
            vigenciaSeleccionada.getCodigo();
         } else if (cualCeldaVA == 6) {
            habilitarLov();
            estadosAfiliacion = vigenciaSeleccionada.getEstadoafiliacion().getNombre();
         } else if (cualCeldaVA == 7) {
            vigenciaSeleccionada.getObservaciones();
            anularLOV();
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('datosVCEmpleado.selectRow(" + va + ", false); datosVCEmpleado').unselectAllRows()");
      }
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //GUARDAR
   /**
    * Metodo de guardado general para la pagina
    */
   public void guardarYSalir() {
      guardadoGeneral();
      salir();
   }

   public void guardadoGeneral() {
      guardarCambiosVA();
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   /**
    * Metodo que guarda los cambios efectuados en la pagina VigenciasProrrateos
    */
   public void guardarCambiosVA() {
//      log.info("ControlEmplVigenciaAfiliacion3.guardarCambiosVA");
      if (!guardado) {
//         log.info("Realizando Operaciones.....");
         if (!listVABorrar.isEmpty()) {
            for (int i = 0; i < listVABorrar.size(); i++) {
//               log.info("Borrando...");
               if (listVABorrar.get(i).getTipoentidad() != null) {
                  if (listVABorrar.get(i).getTipoentidad().getSecuencia() == null) {
                     listVABorrar.get(i).setTipoentidad(null);
                  }
               }
               if (listVABorrar.get(i).getTercerosucursal().getTercero() != null) {
                  if (listVABorrar.get(i).getTercerosucursal().getTercero().getSecuencia() == null) {
                     listVABorrar.get(i).getTercerosucursal().setTercero(null);
                  }
               }
               if (listVABorrar.get(i).getEstadoafiliacion() != null) {
                  if (listVABorrar.get(i).getEstadoafiliacion().getSecuencia() == null) {
                     listVABorrar.get(i).setEstadoafiliacion(null);
                  }
               }
               administrarVigenciasAfiliaciones3.borrarVigenciaAfiliacion(listVABorrar.get(i));
            }
            listVABorrar.clear();
         }
         if (!listVACrear.isEmpty()) {
            for (int i = 0; i < listVACrear.size(); i++) {
//               log.info("Creando...");
               if (listVACrear.get(i).getTipoentidad() != null) {
                  if (listVACrear.get(i).getTipoentidad().getSecuencia() == null) {
                     listVACrear.get(i).setTipoentidad(null);
                  }
               }
               if (listVACrear.get(i).getTercerosucursal().getTercero() != null) {
                  if (listVACrear.get(i).getTercerosucursal().getTercero().getSecuencia() == null) {
                     listVACrear.get(i).getTercerosucursal().setTercero(null);
                  }
               }
               if (listVACrear.get(i).getEstadoafiliacion() != null) {
                  if (listVACrear.get(i).getEstadoafiliacion().getSecuencia() == null) {
                     listVACrear.get(i).setEstadoafiliacion(null);
                  }
               }
               administrarVigenciasAfiliaciones3.crearVigenciaAfiliacion(listVACrear.get(i));
            }
            listVACrear.clear();
         }
         if (!listVAModificar.isEmpty()) {
            for (int i = 0; i < listVAModificar.size(); i++) {
//               log.info("Modificando...");
               if (listVAModificar.get(i).getTipoentidad() != null) {
                  if (listVAModificar.get(i).getTipoentidad().getSecuencia() == null) {
                     listVAModificar.get(i).setTipoentidad(null);
                  }
               }
               if (listVAModificar.get(i).getTercerosucursal() != null) {
                  if (listVAModificar.get(i).getTercerosucursal().getTercero() != null) {
                     if (listVAModificar.get(i).getTercerosucursal().getTercero().getSecuencia() == null) {
                        listVAModificar.get(i).getTercerosucursal().setTercero(null);
                     }
                  }
               }
               if (listVAModificar.get(i).getEstadoafiliacion() != null) {
                  if (listVAModificar.get(i).getEstadoafiliacion().getSecuencia() == null) {
                     listVAModificar.get(i).setEstadoafiliacion(null);
                  }
               }
               administrarVigenciasAfiliaciones3.editarVigenciaAfiliacion(listVAModificar.get(i));
            }
            listVAModificar.clear();
         }
         log.info("Se guardaron los datos con exito");
         RequestContext.getCurrentInstance().update("form:listaValores");
         int tam = 0;
         if (listVigenciasAfiliaciones != null) {
            tam = listVigenciasAfiliaciones.size();
         }
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosVAVigencia");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;
         permitirIndexVA = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      cambioVigenciaA = false;
   }

   //CANCELAR MODIFICACIONES
   /**
    * Cancela las modificaciones realizas en la pagina Cancela las modifaciones
    * de la tabla vigencias prorrateos
    */
   public void cancelarModificacionVA() {
      cerrarFiltrado();
      RequestContext.getCurrentInstance().update("form:listaValores");
      listVABorrar.clear();
      listVACrear.clear();
      listVAModificar.clear();
      k = 0;
      listVigenciasAfiliaciones = null;
      vigenciaSeleccionada = null;
      guardado = true;
      permitirIndexVA = true;
      getListVigenciasAfiliaciones();
      for (int i = 0; i < listVigenciasAfiliaciones.size(); i++) {
         log.info("tercero: " + listVigenciasAfiliaciones.get(i).getTercerosucursal().getTercero().getNombre()
                 + ", Codigo: " + listVigenciasAfiliaciones.get(i).getCodigo() + ", "
                 + ", Estado: " + listVigenciasAfiliaciones.get(i).getEstadoafiliacion());
      }
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosVAVigencia");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   private void cerrarFiltrado() {
      FacesContext c = FacesContext.getCurrentInstance();
      vAFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaInicial");
      vAFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      vAFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaFinal");
      vAFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      vATercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATercero");
      vATercero.setFilterStyle("display: none; visibility: hidden;");
      vATipoEntidad = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATipoEntidad");
      vATipoEntidad.setFilterStyle("display: none; visibility: hidden;");
      vANITTercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vANITTercero");
      vANITTercero.setFilterStyle("display: none; visibility: hidden;");
      vACodigo = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vACodigo");
      vACodigo.setFilterStyle("display: none; visibility: hidden;");
      vAEstadoAfiliacion = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAEstadoAfiliacion");
      vAEstadoAfiliacion.setFilterStyle("display: none; visibility: hidden;");
      vAObservaciones = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAObservaciones");
      vAObservaciones.setFilterStyle("display: none; visibility: hidden;");
      altoTabla = "292";
      RequestContext.getCurrentInstance().update("form:datosVAVigencia");
      banderaVA = 0;
      filtrarVigenciasAfiliaciones = null;
      tipoListaVA = 0;
   }
   //MOSTRAR DATOS CELDA

   /**
    * Metodo que muestra los dialogos de editar con respecto a la lista real o
    * la lista filtrada y a la columna
    */
   public void editarCelda() {
      if (vigenciaSeleccionada != null) {
         editarVA = vigenciaSeleccionada;
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCeldaVA == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialVAD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicialVAD').show()");
            cualCeldaVA = -1;
         } else if (cualCeldaVA == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalVAD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinalVAD').show()");
            cualCeldaVA = -1;
         } else if (cualCeldaVA == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoEntidadVAD");
            RequestContext.getCurrentInstance().execute("PF('editarTipoEntidadVAD').show()");
            cualCeldaVA = -1;
         } else if (cualCeldaVA == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceroVAD");
            RequestContext.getCurrentInstance().execute("PF('editarTerceroVAD').show()");
            cualCeldaVA = -1;
         } else if (cualCeldaVA == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNITTerceroVAD");
            RequestContext.getCurrentInstance().execute("PF('editarNITTerceroVAD').show()");
            cualCeldaVA = -1;
         } else if (cualCeldaVA == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoVAD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoVAD').show()");
            cualCeldaVA = -1;
         } else if (cualCeldaVA == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEstadoAfiliacionVAD");
            RequestContext.getCurrentInstance().execute("PF('editarEstadoAfiliacionVAD').show()");
            cualCeldaVA = -1;
         } else if (cualCeldaVA == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacionesVAD");
            RequestContext.getCurrentInstance().execute("PF('editarObservacionesVAD').show()");
            cualCeldaVA = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   ///////////////////////////////////////////////////////////////////////////
   /**
    * Agrega una nueva Vigencia Prorrateo
    */
   public void agregarNuevaVA() {
      boolean msn = validarIngresoNuevoRegistro(1);
      if (msn) {
         if (validarFechasRegistro(1) == true) {
            if (nuevaVigenciaA.getFechainicial().before(fechaContratacion)) {
               dialogoFechaContratacionError();
            } else {
               boolean repetir = false;
               if (listVigenciasAfiliaciones != null) {
                  for (int j = 0; j < listVigenciasAfiliaciones.size(); j++) {
                     if (listVigenciasAfiliaciones.get(j).getTipoentidad().getSecuencia().equals(nuevaVigenciaA.getTipoentidad().getSecuencia())) {
                        repetir = true;
                     }
                  }
               }
               if ((nuevaVigenciaA.getFechainicial().getDate() == fechaContratacion.getDate()
                       && nuevaVigenciaA.getFechainicial().getMonth() == fechaContratacion.getMonth()
                       && nuevaVigenciaA.getFechainicial().getYear() == fechaContratacion.getYear()
                       && repetir) || nuevaVigenciaA.getFechainicial().getDate() > 1) {
                  RequestContext.getCurrentInstance().execute("PF('errorFechaInicialRegistro').show()");
               } else {
                  cambioVigenciaA = true;
                  //CERRAR FILTRADO
                  if (banderaVA == 1) {
                     cerrarFiltrado();
                  }
                  //AGREGAR REGISTRO A LA LISTA VIGENCIAS
                  k++;
                  l = BigInteger.valueOf(k);

                  nuevaVigenciaA.setSecuencia(l);
                  nuevaVigenciaA.setEmpleado(empleado);
                  listVigenciasAfiliaciones.add(nuevaVigenciaA);
                  listVACrear.add(nuevaVigenciaA);
                  vigenciaSeleccionada = listVigenciasAfiliaciones.get(listVigenciasAfiliaciones.indexOf(nuevaVigenciaA));
                  contarRegistros();
                  RequestContext.getCurrentInstance().update("form:listaValores");
                  //
                  nuevaVigenciaA = new VigenciasAfiliaciones();
                  nuevaVigenciaA.setTipoentidad(new TiposEntidades());
                  nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
                  nuevaVigenciaA.setEstadoafiliacion(new EstadosAfiliaciones());
                  RequestContext.getCurrentInstance().update("form:datosVAVigencia");
                  //
                  if (guardado) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
                  limpiarNuevaVA();
                  RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVA').hide()");
               }

            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }

      }
   }

   /**
    * Limpia los elementos de una nueva vigencia prorrateo
    */
   public void limpiarNuevaVA() {
      nuevaVigenciaA = new VigenciasAfiliaciones();
      nuevaVigenciaA.setTipoentidad(new TiposEntidades());
      nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
      nuevaVigenciaA.getTercerosucursal().setCiudad(new Ciudades());
      nuevaVigenciaA.getTercerosucursal().setTercero(new Terceros());
      nuevaVigenciaA.setEstadoafiliacion(new EstadosAfiliaciones());

   }

   public void cancelarNuevaVA() {
      nuevaVigenciaA = new VigenciasAfiliaciones();
      nuevaVigenciaA.setTipoentidad(new TiposEntidades());
      nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
      nuevaVigenciaA.getTercerosucursal().setCiudad(new Ciudades());
      nuevaVigenciaA.getTercerosucursal().setTercero(new Terceros());
      nuevaVigenciaA.setEstadoafiliacion(new EstadosAfiliaciones());
      // vigenciaSeleccionada = null;
      cambioVigenciaA = false;
   }

   public void duplicarVigenciaA() {
      if (vigenciaSeleccionada != null) {
         duplicarVA = new VigenciasAfiliaciones();
         k++;
         BigInteger var = BigInteger.valueOf(k);
         l = BigInteger.valueOf(k);
         duplicarVA.setSecuencia(l);
         duplicarVA.setEmpleado(vigenciaSeleccionada.getEmpleado());
         duplicarVA.setFechafinal(vigenciaSeleccionada.getFechafinal());
         duplicarVA.setFechainicial(vigenciaSeleccionada.getFechainicial());
         duplicarVA.setTercerosucursal(vigenciaSeleccionada.getTercerosucursal());
         duplicarVA.setTipoentidad(vigenciaSeleccionada.getTipoentidad());
         duplicarVA.setCodigo(vigenciaSeleccionada.getCodigo());
         duplicarVA.setEstadoafiliacion(vigenciaSeleccionada.getEstadoafiliacion());
         duplicarVA.setObservacion(vigenciaSeleccionada.getObservacion());
         cambioVigenciaA = true;
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoVA");
         RequestContext.getCurrentInstance().execute("PF('DuplicadoRegistroVA').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla
    * VigenciaProrrateo
    */
   public void confirmarDuplicarVA() {
      boolean msn = validarIngresoNuevoRegistro(2);
      if (msn) {
         if (validarFechasRegistro(2)) {
            if (duplicarVA.getFechainicial().before(fechaContratacion)) {
               dialogoFechaContratacionError();
            } else {
               boolean repetir = false;
               if (listVigenciasAfiliaciones != null) {
                  for (int j = 0; j < listVigenciasAfiliaciones.size(); j++) {
                     if (listVigenciasAfiliaciones.get(j).getTipoentidad().getSecuencia().equals(duplicarVA.getTipoentidad().getSecuencia())) {
                        repetir = true;
                     }
                  }
               }
               if ((duplicarVA.getFechainicial().getDate() == fechaContratacion.getDate()
                       && duplicarVA.getFechainicial().getMonth() == fechaContratacion.getMonth()
                       && duplicarVA.getFechainicial().getYear() == fechaContratacion.getYear()
                       && repetir) || duplicarVA.getFechainicial().getDate() > 1) {
                  RequestContext.getCurrentInstance().execute("PF('errorFechaInicialRegistro').show()");
               } else {
                  RequestContext context = RequestContext.getCurrentInstance();
                  cambioVigenciaA = true;
                  k++;
                  l = BigInteger.valueOf(k);
                  duplicarVA.setSecuencia(l);
                  listVigenciasAfiliaciones.add(duplicarVA);
                  listVACrear.add(duplicarVA);
                  vigenciaSeleccionada = listVigenciasAfiliaciones.get(listVigenciasAfiliaciones.indexOf(duplicarVA));
                  contarRegistros();
                  RequestContext.getCurrentInstance().update("form:datosVAVigencia");

                  if (guardado) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
                  if (banderaVA == 1) {
                     cerrarFiltrado();
                  }
                  duplicarVA = new VigenciasAfiliaciones();
                  limpiarduplicarVA();
                  RequestContext.getCurrentInstance().update("form:listaValores");
                  RequestContext.getCurrentInstance().execute("PF('DuplicadoRegistroVA').hide()");
               }
            }
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      }
   }

   /**
    * Limpia los elementos del duplicar registro Vigencia Prorrateo
    */
   public void limpiarduplicarVA() {
      duplicarVA = new VigenciasAfiliaciones();
      duplicarVA.setTipoentidad(new TiposEntidades());
      duplicarVA.setTercerosucursal(new TercerosSucursales());
      duplicarVA.setEstadoafiliacion(new EstadosAfiliaciones());

   }

   public void cancelarDuplicadoVA() {
      cambioVigenciaA = false;
      duplicarVA = new VigenciasAfiliaciones();
      duplicarVA.setTipoentidad(new TiposEntidades());
      duplicarVA.setTercerosucursal(new TercerosSucursales());
      duplicarVA.setEstadoafiliacion(new EstadosAfiliaciones());
   }

   /**
    * Valida que registro se elimina de que tabla con respecto a la posicion en
    * la pagina
    */
   public void validarBorradoVigencia() {
      if (vigenciaSeleccionada != null) {
         borrarVA();
      }
   }

   /**
    * Metodo que borra una vigencia prorrateo
    */
   public void borrarVA() {
      if (vigenciaSeleccionada != null) {

         cambioVigenciaA = true;
         if (!listVAModificar.isEmpty() && listVAModificar.contains(vigenciaSeleccionada)) {
            int modIndex = listVAModificar.indexOf(vigenciaSeleccionada);
            listVAModificar.remove(modIndex);
            listVABorrar.add(vigenciaSeleccionada);
         } else if (!listVACrear.isEmpty() && listVACrear.contains(vigenciaSeleccionada)) {
            int crearIndex = listVACrear.indexOf(vigenciaSeleccionada);
            listVACrear.remove(crearIndex);
         } else {
            listVABorrar.add(vigenciaSeleccionada);
         }
         listVigenciasAfiliaciones.remove(vigenciaSeleccionada);
         if (tipoListaVA == 1) {
            filtrarVigenciasAfiliaciones.remove(vigenciaSeleccionada);
         }
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:listaValores");
         RequestContext.getCurrentInstance().update("form:datosVAVigencia");
         vigenciaSeleccionada = null;
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }

   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaVA == 0) {
         vAFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaInicial");
         vAFechaInicial.setFilterStyle("width: 85% !important");
         vAFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaFinal");
         vAFechaFinal.setFilterStyle("width: 85% !important");
         vATercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATercero");
         vATercero.setFilterStyle("width: 85% !important");
         vATipoEntidad = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATipoEntidad");
         vATipoEntidad.setFilterStyle("width: 85% !important");
         vANITTercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vANITTercero");
         vANITTercero.setFilterStyle("width: 85% !important");
         vACodigo = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vACodigo");
         vACodigo.setFilterStyle("width: 85% !important");
         vAEstadoAfiliacion = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAEstadoAfiliacion");
         vAEstadoAfiliacion.setFilterStyle("width: 85% !important");
         vAObservaciones = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAObservaciones");
         vAObservaciones.setFilterStyle("width: 85% !important");
         altoTabla = "272";
         RequestContext.getCurrentInstance().update("form:datosVAVigencia");
         banderaVA = 1;
      } else if (banderaVA == 1) {
         cerrarFiltrado();
      }
   }

   public void salir() {
      limpiarListasValor();
      cerrarFiltrado();
      RequestContext.getCurrentInstance().update("form:listaValores");
      listVABorrar.clear();
      listVACrear.clear();
      listVAModificar.clear();
      vigenciaSeleccionada = null;
      k = 0;
      fechaFin = null;
      fechaIni = null;
      listVigenciasAfiliaciones = null;
      guardado = true;
      vigenciaValidaciones = null;
      cambioVigenciaA = false;
      tipoActualizacion = -1;
      limpiarListasValor();
      navegar("atras");
   }

   public void asignarIndex(VigenciasAfiliaciones va, int dlg, int LND) {
      vigenciaSeleccionada = va;
      tipoActualizacion = 0;
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         contarRegistrosTipoEntidad();
         tipoEntidadSeleccionado = null;
         RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
      } else if (dlg == 1) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         contarRegistrosTercero();
         terceroSeleccionado = null;
         RequestContext.getCurrentInstance().update("form:TerceroDialogo");
         RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
      } else if (dlg == 2) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         contarRegistrosEstado();
         estadoSSeleccionado = null;
         RequestContext.getCurrentInstance().update("form:EstadoAfiliacionDialogo");
         RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').show()");
      }
   }

   public void asignarIndex(int dlg, int LND) {
      if (LND == 1) {
         tipoActualizacion = 1;
      } else if (LND == 2) {
         tipoActualizacion = 2;
      }
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         contarRegistrosTipoEntidad();
         RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
      } else if (dlg == 1) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         contarRegistrosTercero();
         RequestContext.getCurrentInstance().update("form:TerceroDialogo");
         RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
      } else if (dlg == 2) {
         RequestContext.getCurrentInstance().update("form:listaValores");
         contarRegistrosEstado();
         RequestContext.getCurrentInstance().update("form:EstadoAfiliacionDialogo");
         RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').show()");
      }
   }

   public void actualizarTipoEntidad() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:listaValores");
      if (tipoActualizacion == 0) {
         vigenciaValidaciones = new VigenciasAfiliaciones();
         vigenciaValidaciones.setTipoentidad(tipoEntidadSeleccionado);
         tipoValidacion = 1;//Tipo Entidad
         boolean cambio = validarModificacionRegistroTabla();
         if (cambio == true) {
            vigenciaSeleccionada.setTipoentidad(tipoEntidadSeleccionado);
            if (!listVACrear.contains(vigenciaSeleccionada)) {
               if (listVAModificar.isEmpty()) {
                  listVAModificar.add(vigenciaSeleccionada);
               } else if (!listVAModificar.contains(vigenciaSeleccionada)) {
                  listVAModificar.add(vigenciaSeleccionada);
               }
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambioVigenciaA = true;
            permitirIndexVA = true;
         }

         RequestContext.getCurrentInstance().update("form:datosVAVigencia");
      } else if (tipoActualizacion == 1) {
         nuevaVigenciaA.setTipoentidad(tipoEntidadSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoEntidadVA");
         RequestContext.getCurrentInstance().execute("PF('precaucionTercero').show()");
      } else if (tipoActualizacion == 2) {
         duplicarVA.setTipoentidad(tipoEntidadSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEntidadVA");
         RequestContext.getCurrentInstance().execute("PF('precaucionTercero').show()");
      }
      filtrarTiposEntidades = null;
      tipoEntidadSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      context.reset("form:lovTipoEntidad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').hide()");
      RequestContext.getCurrentInstance().execute("PF('lovTipoEntidad').clearFilters()");
      RequestContext.getCurrentInstance().update("form:datosVAVigencia");
   }

   public void cancelarCambioTipoEntidad() {
      filtrarTiposEntidades = null;
      tipoEntidadSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVA = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTipoEntidad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoEntidad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').hide()");
   }

   public void actualizarTerceros() {
      log.info("tipoActualizacion : " + tipoActualizacion);
      boolean banderaEncuentra = false;//booleano que se activa 
      RequestContext.getCurrentInstance().update("form:listaValores");
      if (tipoActualizacion == 0) {//Si la actualizacion no es para nuevo registro o duplicar
         vigenciaValidaciones = new VigenciasAfiliaciones();
         vigenciaValidaciones.setTercerosucursal(new TercerosSucursales());
         vigenciaValidaciones.getTercerosucursal().setTercero(new Terceros());
         vigenciaValidaciones.getTercerosucursal().setTercero(terceroSeleccionado);
         //booleano cambio que determina si las validaciones se cumplieron
         tipoValidacion = 2;//Tipo Terceros
         boolean cambio = validarModificacionRegistroTabla();
         int posicion = -1;

         List<TercerosSucursales> listTercerosSucursales = administrarVigenciasAfiliaciones3.listTercerosSucursales();

         for (int i = 0; i < listTercerosSucursales.size(); i++) {
            if (terceroSeleccionado == null) {
               log.info("terceroSeleccionado : " + terceroSeleccionado);
            }
            if (listTercerosSucursales.get(i).getTercero() != null) {
               if (listTercerosSucursales.get(i).getTercero().getSecuencia() != null) {
                  if (listTercerosSucursales.get(i).getTercero().getSecuencia().equals(terceroSeleccionado.getSecuencia())) {
                     banderaEncuentra = true;
                     log.info("Entro en el i : " + i);
                     posicion = i;
                  }
               } else {
                  log.info("listTercerosSucursales.get(" + i + ").getTercero().getSecuencia() == null");
               }
            } else {
               log.info("listTercerosSucursales.get(" + i + ").getTercero() == null");
            }
         }
         log.info("cambio : " + cambio);
         if ((banderaEncuentra == true) && (posicion != -1)) {
            vigenciaSeleccionada.setTercerosucursal(listTercerosSucursales.get(posicion));
         }
         if (!listVACrear.contains(vigenciaSeleccionada)) {
            if (listVAModificar.isEmpty()) {
               listVAModificar.add(vigenciaSeleccionada);
            } else if (!listVAModificar.contains(vigenciaSeleccionada)) {
               listVAModificar.add(vigenciaSeleccionada);
            }
         }
         if (!cambio) {
            RequestContext.getCurrentInstance().execute("PF('recomendacionCambioTercero').show()");
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         cambioVigenciaA = true;
         permitirIndexVA = true;
         RequestContext.getCurrentInstance().update("form:datosVAVigencia");
         log.info("llego al final de la funcion");
      } else if (tipoActualizacion == 1) {//Si es para un nuevo registro
         int posicion = -1;
         List<TercerosSucursales> listTercerosSucursales = administrarVigenciasAfiliaciones3.listTercerosSucursales();
         for (int i = 0; i < listTercerosSucursales.size(); i++) {
            if (listTercerosSucursales.get(i).getTercero().getNombre().equalsIgnoreCase(terceroSeleccionado.getNombre())) {
               banderaEncuentra = true;
               posicion = i;
            }
         }
         if (banderaEncuentra && (posicion != -1)) {
            nuevaVigenciaA.setTercerosucursal(listTercerosSucursales.get(posicion));
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroVA");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNITTerceroVA");
      } else if (tipoActualizacion == 2) {// Si es para duplicar
         int posicion = -1;
         List<TercerosSucursales> listTercerosSucursales = administrarVigenciasAfiliaciones3.listTercerosSucursales();
         for (int i = 0; i < listTercerosSucursales.size(); i++) {
            if (listTercerosSucursales.get(i).getTercero().getNombre().equalsIgnoreCase(terceroSeleccionado.getNombre())) {
               banderaEncuentra = true;
               posicion = i;
            }
         }
         if (banderaEncuentra && (posicion != -1)) {
            duplicarVA.setTercerosucursal(listTercerosSucursales.get(posicion));
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroVA");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNITTerceroVA");
      }
      filtrarTerceros = null;
      terceroSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().reset("form:lovTercero:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:datosVAVigencia");
   }

   public void cancelarCambioTerceros() {
      filtrarTerceros = null;
      terceroSeleccionado = null;
      aceptar = true;
      // vigenciaSeleccionada = null;
      tipoActualizacion = -1;
      permitirIndexVA = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTercero:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");
   }

   public void actualizarEstadoAfiliacion() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:listaValores");
      if (tipoActualizacion == 0) {
         //if (tipoListaVA == 0) {
         vigenciaValidaciones = new VigenciasAfiliaciones();
         vigenciaValidaciones.setEstadoafiliacion(estadoSSeleccionado);
         // vigenciaSeleccionada.setEstadoafiliacion(estadoSSeleccionado);
         tipoValidacion = 3;//Estado Afiliacion
         boolean cambio = validarModificacionRegistroTabla();
         if (cambio == true) {
            vigenciaSeleccionada.setEstadoafiliacion(estadoSSeleccionado);
            if (!listVACrear.contains(vigenciaSeleccionada)) {
               if (listVAModificar.isEmpty()) {
                  listVAModificar.add(vigenciaSeleccionada);
               } else if (!listVAModificar.contains(vigenciaSeleccionada)) {
                  listVAModificar.add(vigenciaSeleccionada);
               }
            }

            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambioVigenciaA = true;
            permitirIndexVA = true;
         }
         RequestContext.getCurrentInstance().update("form:datosVAVigencia");
      } else if (tipoActualizacion == 1) {
         nuevaVigenciaA.setEstadoafiliacion(estadoSSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstadoAfiliacionVA");
      } else if (tipoActualizacion == 2) {
         duplicarVA.setEstadoafiliacion(estadoSSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstadoAfiliacionVA");
      }
      filtrarEstadosAfiliaciones = null;
      estadoSSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      context.reset("form:lovEstadoAfiliacion:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEstadoAfiliacion').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:datosVAVigencia");
   }

   public void cancelarCambioEstadoAfiliacion() {
      filtrarEstadosAfiliaciones = null;
      estadoSSeleccionado = null;
      aceptar = true;
      //vigenciaSeleccionada = null;
      tipoActualizacion = -1;
      permitirIndexVA = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovEstadoAfiliacion:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEstadoAfiliacion').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').hide()");
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (vigenciaSeleccionada != null) {
         if (cualCeldaVA == 2) {
            contarRegistrosTipoEntidad();
            RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
            tipoEntidadSeleccionado = null;
            tipoActualizacion = 0;
         }
         if (cualCeldaVA == 3) {
            contarRegistrosTercero();
            RequestContext.getCurrentInstance().update("form:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            terceroSeleccionado = null;
            tipoActualizacion = 0;
         }
         if (cualCeldaVA == 6) {
            contarRegistrosEstado();
            RequestContext.getCurrentInstance().update("form:EstadoAfiliacionDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').show()");
            estadoSSeleccionado = null;
            tipoActualizacion = 0;
         }
      }
   }

   public void validarNuevoRegistro() {
      nuevaVigenciaA = new VigenciasAfiliaciones();
      nuevaVigenciaA.setTipoentidad(new TiposEntidades());
      nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
      nuevaVigenciaA.getTercerosucursal().setCiudad(new Ciudades());
      nuevaVigenciaA.getTercerosucursal().setTercero(new Terceros());
      nuevaVigenciaA.setEstadoafiliacion(new EstadosAfiliaciones());
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVA");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVA').show()");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void validarExportPDF() throws IOException {
      exportPDFVA();
   }

   public void exportPDFVA() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVA:datosVAVigenciaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "VigenciasAfiliacionesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Verifica que tabla exportar XLS con respecto al index activo
    *
    * @throws IOException
    */
   public void verificarExportXLS() throws IOException {
      exportXLSVA();
   }

   /**
    * Metodo que exporta datos a XLS Vigencia Afiliaciones
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLSVA() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVA:datosVAVigenciaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "VigenciasAfiliacionesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void verificarRastro() {
      log.info("ControlEmplVigenciaAfiliacion3.verificarRastro");
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASAFILIACIONES");
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASAFILIACIONES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void eventoFiltrar() {
      if (tipoListaVA == 0) {
         tipoListaVA = 1;
      }
      RequestContext.getCurrentInstance().update("form:listaValores");
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosTipoEntidad() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTipoEntidad");
   }

   public void contarRegistrosTercero() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTercero");
   }

   public void contarRegistrosEstado() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEstado");
   }

   public void recordarSeleccion() {
      if (vigenciaSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVAVigencia");
         tablaC.setSelection(vigenciaSeleccionada);
      } else {
         vigenciaSeleccionada = null;
      }
      //log.info("vigenciaSeleccionada: " + vigenciaSeleccionada);
   }

   public void anularLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void habilitarLov() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //GET - SET 
   public List<VigenciasAfiliaciones> getListVigenciasAfiliaciones() {
      try {
         if (listVigenciasAfiliaciones == null) {
            listVigenciasAfiliaciones = new ArrayList<VigenciasAfiliaciones>();
            if (empleado != null) {
               listVigenciasAfiliaciones = administrarVigenciasAfiliaciones3.listVigenciasAfiliacionesEmpleado(empleado.getSecuencia());

               for (int i = 0; i < listVigenciasAfiliaciones.size(); i++) {

                  if (listVigenciasAfiliaciones.get(i).getEstadoafiliacion() == null) {
                     listVigenciasAfiliaciones.get(i).setEstadoafiliacion(new EstadosAfiliaciones());
                     listVigenciasAfiliaciones.get(i).getEstadoafiliacion().setNombre("");
                  } else if (listVigenciasAfiliaciones.get(i).getEstadoafiliacion().getNombre() == null) {
                     listVigenciasAfiliaciones.get(i).getEstadoafiliacion().setNombre("");
                  }

                  if (listVigenciasAfiliaciones.get(i).getTercerosucursal().getTercero() == null) {
                     listVigenciasAfiliaciones.get(i).getTercerosucursal().setTercero(new Terceros());
                  }
                  if (listVigenciasAfiliaciones.get(i).getTipoentidad() == null) {
                     listVigenciasAfiliaciones.get(i).setTipoentidad(new TiposEntidades());
                  }
               }
            }
         }
         return listVigenciasAfiliaciones;
      } catch (Exception e) {
         log.warn("Error en getListVigenciasAfiliaciones : " + e.toString());
         return null;
      }
   }

   public void setListVigenciasAfiliaciones(List<VigenciasAfiliaciones> listVigenciasAfiliaciones) {
      this.listVigenciasAfiliaciones = listVigenciasAfiliaciones;
   }

   public List<VigenciasAfiliaciones> getFiltrarVigenciasAfiliaciones() {
      return filtrarVigenciasAfiliaciones;
   }

   public void setFiltrarVigenciasAfiliaciones(List<VigenciasAfiliaciones> filtrarVigenciasAfiliaciones) {
      this.filtrarVigenciasAfiliaciones = filtrarVigenciasAfiliaciones;
   }

   public List<TiposEntidades> getListTiposEntidades() {
      if (listTiposEntidades == null) {
         listTiposEntidades = administrarVigenciasAfiliaciones3.listTiposEntidades();
      }
      return listTiposEntidades;
   }

   public void setListTiposEntidades(List<TiposEntidades> listTiposEntidades) {
      this.listTiposEntidades = listTiposEntidades;
   }

   public TiposEntidades getTipoEntidadSeleccionado() {
      return tipoEntidadSeleccionado;
   }

   public void setTipoEntidadSeleccionado(TiposEntidades tipoEntidadSeleccionado) {
      this.tipoEntidadSeleccionado = tipoEntidadSeleccionado;
   }

   public List<TiposEntidades> getFiltrarTiposEntidades() {
      return filtrarTiposEntidades;
   }

   public void setFiltrarTiposEntidades(List<TiposEntidades> filtrarTiposEntidades) {
      this.filtrarTiposEntidades = filtrarTiposEntidades;
   }

   public List<Terceros> getListTerceros() {
      if (listTerceros == null) {
         listTerceros = administrarVigenciasAfiliaciones3.listTerceros();
      }
      return listTerceros;
   }

   public void setListTerceros(List<Terceros> listTerceros) {
      this.listTerceros = listTerceros;
   }

   public Terceros getTerceroSeleccionado() {
      return terceroSeleccionado;
   }

   public void setTerceroSeleccionado(Terceros terceroSeleccionado) {
      this.terceroSeleccionado = terceroSeleccionado;
   }

   public List<Terceros> getFiltrarTerceros() {
      return filtrarTerceros;
   }

   public void setFiltrarTerceros(List<Terceros> filtrarTerceros) {
      this.filtrarTerceros = filtrarTerceros;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public List<VigenciasAfiliaciones> getListVAModificar() {
      return listVAModificar;
   }

   public void setListVAModificar(List<VigenciasAfiliaciones> listVAModificar) {
      this.listVAModificar = listVAModificar;
   }

   public VigenciasAfiliaciones getNuevaVigenciaA() {
      return nuevaVigenciaA;
   }

   public void setNuevaVigenciaA(VigenciasAfiliaciones nuevaVigenciaA) {
      this.nuevaVigenciaA = nuevaVigenciaA;
   }

   public List<VigenciasAfiliaciones> getListVABorrar() {
      return listVABorrar;
   }

   public void setListVABorrar(List<VigenciasAfiliaciones> listVABorrar) {
      this.listVABorrar = listVABorrar;
   }

   public VigenciasAfiliaciones getEditarVA() {
      return editarVA;
   }

   public void setEditarVA(VigenciasAfiliaciones editarVA) {
      this.editarVA = editarVA;
   }

   public VigenciasAfiliaciones getDuplicarVA() {
      return duplicarVA;
   }

   public void setDuplicarVA(VigenciasAfiliaciones duplicarVA) {
      this.duplicarVA = duplicarVA;
   }

   public List<VigenciasAfiliaciones> getListVACrear() {
      return listVACrear;
   }

   public void setListVACrear(List<VigenciasAfiliaciones> listVACrear) {
      this.listVACrear = listVACrear;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public List<EstadosAfiliaciones> getListEstadosAfiliaciones() {
      try {
         listEstadosAfiliaciones = new ArrayList<EstadosAfiliaciones>();
         listEstadosAfiliaciones = administrarVigenciasAfiliaciones3.listEstadosAfiliaciones();

         return listEstadosAfiliaciones;
      } catch (Exception e) {
         return null;
      }
//        if (listEstadosAfiliaciones == null) {
//            listEstadosAfiliaciones = administrarVigenciasAfiliaciones3.listEstadosAfiliaciones();
//        }
//        return listEstadosAfiliaciones;
   }

   public void setListEstadosAfiliaciones(List<EstadosAfiliaciones> listEstadosAfiliaciones) {
      this.listEstadosAfiliaciones = listEstadosAfiliaciones;
   }

   public EstadosAfiliaciones getEstadoSSeleccionado() {
      return estadoSSeleccionado;
   }

   public void setEstadoSSeleccionado(EstadosAfiliaciones estadoSSeleccionado) {
      this.estadoSSeleccionado = estadoSSeleccionado;
   }

   public List<EstadosAfiliaciones> getFiltrarEstadosAfiliaciones() {
      return filtrarEstadosAfiliaciones;
   }

   public void setFiltrarEstadosAfiliaciones(List<EstadosAfiliaciones> filtrarEstadosAfiliaciones) {
      this.filtrarEstadosAfiliaciones = filtrarEstadosAfiliaciones;
   }

   public VigenciasAfiliaciones getVigenciaSeleccionada() {
      return vigenciaSeleccionada;
   }

   public void setVigenciaSeleccionada(VigenciasAfiliaciones vigenciaSeleccionada) {
      this.vigenciaSeleccionada = vigenciaSeleccionada;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVAVigencia");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public String getInfoRegistroTipoEntidad() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoEntidad");
      infoRegistroTipoEntidad = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoEntidad;
   }

   public String getInfoRegistroTercero() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTercero");
      infoRegistroTercero = String.valueOf(tabla.getRowCount());
      return infoRegistroTercero;
   }

   public String getInfoRegistroEstado() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEstadoAfiliacion");
      infoRegistroEstado = String.valueOf(tabla.getRowCount());
      return infoRegistroEstado;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }
}
