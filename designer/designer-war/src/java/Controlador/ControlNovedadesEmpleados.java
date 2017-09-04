/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.FormulasConceptos;
import Entidades.Novedades;
import Entidades.Periodicidades;
import Entidades.PruebaEmpleados;
import Entidades.Terceros;
import Entidades.Usuarios;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaConceptoInterface;
import InterfaceAdministrar.AdministrarNovedadesEmpleadosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
//import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import ControlNavegacion.ListasRecurrentes;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Viktor
 */
@ManagedBean
@SessionScoped
public class ControlNovedadesEmpleados implements Serializable {

   private static Logger log = Logger.getLogger(ControlNovedadesEmpleados.class);

   @EJB
   AdministrarNovedadesEmpleadosInterface administrarNovedadesEmpleados;
   @EJB
   AdministrarFormulaConceptoInterface administrarFormulaConcepto;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //SECUENCIA DE LA PERSONA
//    private BigInteger secuenciaEmpleado;
   //LISTA NOVEDADES
   private List<Novedades> listaNovedades;
   private List<Novedades> filtrarListaNovedades;
   private Novedades novedadSeleccionada;
   //LISTA QUE NO ES LISTA
   private List<PruebaEmpleados> listaEmpleadosNovedad;
   private List<PruebaEmpleados> filtrarListaEmpleados;
   private PruebaEmpleados empleadoSeleccionado; //Seleccion Mostrar
   //LOV EMPLEADOS
   private List<PruebaEmpleados> lovEmpleados;
   private List<PruebaEmpleados> filtrarLovEmpleados;
   private PruebaEmpleados empleadoSeleccionadoLov;
   //editar celda
   private Novedades editarNovedades;
   private int cualCelda, tipoLista, tipoListaNov;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion;//Activo/Desactivo Crtl + F11
   private int bandera;
   private boolean permitirIndex;
   //RASTROS
   //private BigInteger secRegistro;
   private boolean guardado;
   //Crear Novedades
   private List<Novedades> listaNovedadesCrear;
   private Novedades nuevaNovedad;
   private int k;
   private BigInteger l;
   //Modificar Novedades
   private List<Novedades> listaNovedadesModificar;
   //Borrar Novedades
   private List<Novedades> listaNovedadesBorrar;
   //Autocompletar
   private String CodigoConcepto;
   private String NitTercero, Formula, DescripcionConcepto, DescripcionPeriodicidad, NombreTercero;
   private Date FechaFinal;
   private String CodigoPeriodicidad;
   private BigDecimal Saldo;
   private Integer HoraDia, MinutoHora;
   //L.O.V CONCEPTOS
   private List<Conceptos> lovConceptos;
   private List<Conceptos> filtradoslovConceptos;
   private Conceptos conceptoLovSeleccionado;
   BigInteger secconcepto;
   //L.O.V Periodicidades
   private List<Periodicidades> lovPeriodicidades;
   private List<Periodicidades> filtradoslovPeriodicidades;
   private Periodicidades periodicidadLovSeleccionada;
   //L.O.V TERCEROS
   private List<Terceros> lovTerceros;
   private List<Terceros> filtradoslovTerceros;
   private Terceros terceroLovSeleccionado;
   //L.O.V FORMULAS
   private List<Formulas> lovFormulas;
   private List<Formulas> filtradoslovFormulas;
   private Formulas formulaLovSeleccioiada;
   //Columnas Tabla NOVEDADES
   private Column nEEmpleadosCodigos, nEEmpleadosNombres, nEEmpleadosValor;
   private Column nEConceptoCodigo, nEConceptoDescripcion, nEFechasInicial, nEFechasFinal,
           nEValor, nESaldo, nEPeriodicidadCodigo, nEDescripcionPeriodicidad, nETercerosNit,
           nETercerosNombre, nEFormulas, nEHorasDias, nEMinutosHoras, nETipo;
   //Duplicar
   private Novedades duplicarNovedad;
   //USUARIO
   private String alias;
   private Usuarios usuarioBD;
   //VALIDAR SI EL QUE SE VA A BORRAR ESTÁ EN SOLUCIONES FORMULAS
   private int resultado;
   private boolean todas;
   private boolean actuales;
   //
   private boolean activoBtnAcumulado;
   //
   private Novedades actualNovedadTabla;
   private String altoTablaEmpl, altoTabla, altoTablaReg, altoTablaRegEmp;
   BigDecimal valor = new BigDecimal(Integer.toString(0));
   private String infoRegistro, infoRegistroConceptos, infoRegistroPeriodicidad, infoRegistroFormulas, infoRegistroTerceros, infoRegistrosEmpleadosNovedades, infoRegistroEmpleadosLOV;
   //Controlar el cargue de muchos empleados
//   private boolean cargarTodos;
   private int cantidadEmpleadosNov;
   private boolean activarLOV;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   private ListasRecurrentes listasRecurrentes;

   public ControlNovedadesEmpleados() {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
      actualNovedadTabla = new Novedades();
      activoBtnAcumulado = true;
      permitirIndex = true;
      listaNovedades = null;
      lovEmpleados = null;
      lovFormulas = null;
      lovConceptos = null;
      todas = false;
      actuales = true;
      lovPeriodicidades = null;
      listaEmpleadosNovedad = null;
      aceptar = true;
      empleadoSeleccionado = null;
      novedadSeleccionada = null;
      guardado = true;
      tipoLista = 0;
      tipoListaNov = 0;
      listaNovedadesBorrar = new ArrayList<Novedades>();
      listaNovedadesCrear = new ArrayList<Novedades>();
      listaNovedadesModificar = new ArrayList<Novedades>();
      //Crear VC
      nuevaNovedad = new Novedades();
      nuevaNovedad.setPeriodicidad(new Periodicidades());
      nuevaNovedad.getPeriodicidad().setNombre("");
      nuevaNovedad.getPeriodicidad().setCodigoStr("");
      nuevaNovedad.setTercero(new Terceros());
      nuevaNovedad.getTercero().setNombre("");
      nuevaNovedad.setConcepto(new Conceptos());
      nuevaNovedad.getConcepto().setDescripcion("");
      nuevaNovedad.getConcepto().setCodigoSTR("0");
      nuevaNovedad.setTipo("FIJA");
      nuevaNovedad.setUsuarioreporta(new Usuarios());
      nuevaNovedad.setTerminal(" ");
      nuevaNovedad.setFechareporte(new Date());
      altoTabla = "125";
      altoTablaEmpl = "95";
      nuevaNovedad.setValortotal(valor);
//      cargarTodos = false;
      cantidadEmpleadosNov = 0;
      CodigoConcepto = "0";
      activarLOV = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "novedadempleado";
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
      lovConceptos = null;
      lovEmpleados = null;
      lovFormulas = null;
      lovPeriodicidades = null;
      lovTerceros = null;
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
         administrarNovedadesEmpleados.obtenerConexion(ses.getId());
         administrarFormulaConcepto.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         getListaEmpleadosNovedad();
         if (listaEmpleadosNovedad != null) {
            if (!listaEmpleadosNovedad.isEmpty()) {
               empleadoSeleccionado = listaEmpleadosNovedad.get(0);
            }
         }
         listaNovedades = null;
         getListaNovedades();
         contarRegistros();
         if (listaNovedades != null) {
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         }
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   //CANCELAR MODIFICACIONES
   public void cancelarModificacion() {
      anularBotonLOV();
      cerrarFiltrado();
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      empleadoSeleccionado = null;
      novedadSeleccionada = null;
      listaNovedades = null;
      guardado = true;
      permitirIndex = true;
      resultado = 0;
      activoBtnAcumulado = true;
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
   }

   public void salir() {
      limpiarListasValor();
      anularBotonLOV();
      cerrarFiltrado();
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      novedadSeleccionada = null;
      listaEmpleadosNovedad = null;
      lovEmpleados = null;
      listaNovedades = null;
      resultado = 0;
      guardado = true;
      permitirIndex = true;
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      navegar("atras");
   }

   //Ubicacion Celda Arriba 
   public void cambiarEmpleado() {
      listaNovedades = null;
      //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
      if (listaNovedadesCrear.isEmpty() && listaNovedadesBorrar.isEmpty() && listaNovedadesModificar.isEmpty()) {
         novedadSeleccionada = null;
         activoBtnAcumulado = true;
         getListaNovedades();
         if (tipoListaNov == 1) {
            RequestContext.getCurrentInstance().execute("PF('datosNovedadesEmpleado').clearFilters()");
         }
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         contarRegistros();
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
         RequestContext.getCurrentInstance().execute("PF('cambiar').show()");
      }
   }

   public void limpiarListas() {
      listaNovedadesCrear.clear();
      listaNovedadesBorrar.clear();
      listaNovedadesModificar.clear();
      listaNovedades = null;
      guardado = true;
      getListaNovedades();
      log.info("listaNovedades Valor" + listaNovedades.get(0).getValortotal());
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void seleccionarTipoNuevaNovedad(String tipo, int tipoNuevo) {
      if (tipoNuevo == 1) {
         if (tipo.equals("FIJA")) {
            nuevaNovedad.setTipo("FIJA");
         } else if (tipo.equals("OCASIONAL")) {
            nuevaNovedad.setTipo("OCASIONAL");
         } else if (tipo.equals("PAGO POR FUERA")) {
            nuevaNovedad.setTipo("PAGO POR FUERA");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
      } else {
         if (tipo.equals("FIJA")) {
            duplicarNovedad.setTipo("FIJA");
         } else if (tipo.equals("OCASIONAL")) {
            duplicarNovedad.setTipo("OCASIONAL");
         } else if (tipo.equals("PAGO POR FUERA")) {
            duplicarNovedad.setTipo("PAGO POR FUERA");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");
      }
   }

   public void seleccionarTipo(String estadoTipo, Novedades novedad, int celda) {
      novedadSeleccionada = novedad;
      if (estadoTipo.equals("FIJA")) {
         novedadSeleccionada.setTipo("FIJA");
      } else if (estadoTipo.equals("OCASIONAL")) {
         novedadSeleccionada.setTipo("OCASIONAL");
      } else if (estadoTipo.equals("PAGO POR FUERA")) {
         novedadSeleccionada.setTipo("PAGO POR FUERA");
      }

      if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
         if (listaNovedadesModificar.isEmpty()) {
            listaNovedadesModificar.add(novedadSeleccionada);
         } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
            listaNovedadesModificar.add(novedadSeleccionada);
         }
      }

      if (guardado) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
   }

   //GUARDAR
   public void guardarCambiosNovedades() {
//        Empleados emp = new Empleados();
      if (guardado == false) {
         log.info("Realizando Operaciones Novedades");

         getResultado();
         log.info("Resultado: " + resultado);
         if (resultado > 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:solucionesFormulas");
            RequestContext.getCurrentInstance().execute("PF('solucionesFormulas').show()");
            listaNovedadesBorrar.clear();
         }

         if (!listaNovedadesBorrar.isEmpty()) {
            for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
               log.info("Borrando..." + listaNovedadesBorrar.size());

               if (listaNovedadesBorrar.get(i).getPeriodicidad().getSecuencia() == null) {
                  listaNovedadesBorrar.get(i).setPeriodicidad(null);
               }
               if (listaNovedadesBorrar.get(i).getTercero().getSecuencia() == null) {
                  listaNovedadesBorrar.get(i).setTercero(null);
               }
               if (listaNovedadesBorrar.get(i).getSaldo() == null) {
                  listaNovedadesBorrar.get(i).setSaldo(null);
               }
               if (listaNovedadesBorrar.get(i).getUnidadesparteentera() == null) {
                  listaNovedadesBorrar.get(i).setUnidadesparteentera(null);
               }
               if (listaNovedadesBorrar.get(i).getUnidadespartefraccion() == null) {
                  listaNovedadesBorrar.get(i).setUnidadespartefraccion(null);
               }
               administrarNovedadesEmpleados.borrarNovedades(listaNovedadesBorrar.get(i));
            }
            log.info("Entra");
            listaNovedadesBorrar.clear();
         }

         if (!listaNovedadesCrear.isEmpty()) {
            for (int i = 0; i < listaNovedadesCrear.size(); i++) {
               log.info("Creando...");

               if (listaNovedadesCrear.get(i).getTercero().getSecuencia() == null) {
                  listaNovedadesCrear.get(i).setTercero(null);
               }
               if (listaNovedadesCrear.get(i).getPeriodicidad().getSecuencia() == null) {
                  listaNovedadesCrear.get(i).setPeriodicidad(null);
               }

               if (listaNovedadesCrear.get(i).getSaldo() == null) {
                  listaNovedadesCrear.get(i).setSaldo(null);
               }
               if (listaNovedadesCrear.get(i).getUnidadesparteentera() == null) {
                  listaNovedadesCrear.get(i).setUnidadesparteentera(null);
               }
               if (listaNovedadesCrear.get(i).getUnidadespartefraccion() == null) {
                  listaNovedadesCrear.get(i).setUnidadespartefraccion(null);
               }
               if (listaNovedadesCrear.get(i).getValortotal() == null) {
                  listaNovedadesCrear.get(i).setValortotal(new BigDecimal(0));
               }
               log.info(listaNovedadesCrear.get(i).getTipo());
               administrarNovedadesEmpleados.crearNovedades(listaNovedadesCrear.get(i));
            }
            log.info("LimpiaLista");
            listaNovedadesCrear.clear();
         }
         if (!listaNovedadesModificar.isEmpty()) {
            administrarNovedadesEmpleados.modificarNovedades(listaNovedadesModificar);
            listaNovedadesModificar.clear();
         }

         log.info("Se guardaron los datos con exito");
         listaNovedades = null;
         RequestContext context = RequestContext.getCurrentInstance();
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         guardado = true;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   //CREAR NOVEDADES
   public void agregarNuevaNovedadEmpleado() throws UnknownHostException {
      int pasa = 0;
      int pasa2 = 0;
      Empleados emp = new Empleados();
//        Empleados emp2 = new Empleados();
      RequestContext context = RequestContext.getCurrentInstance();

      log.info("nuevaNovedad Fechainicial : " + nuevaNovedad.getFechainicial());
      log.info("Concepto : " + nuevaNovedad.getConcepto().getSecuencia());
      log.info("Concepto codigo : " + nuevaNovedad.getConcepto().getCodigoSTR());
      log.info("Formula : " + nuevaNovedad.getFormula().getSecuencia());
      log.info("Formula NL :" + nuevaNovedad.getFormula().getNombrelargo() + "...");
      log.info("Periodicidad : " + nuevaNovedad.getPeriodicidad().getNombre());
      log.info("getTipo() : " + nuevaNovedad.getTipo());

      if (nuevaNovedad.getFechainicial() == null || nuevaNovedad.getConcepto().getCodigoSTR() == null || nuevaNovedad.getConcepto().getCodigoSTR().equals("0")
              || nuevaNovedad.getFormula().getNombrelargo() == null || nuevaNovedad.getFormula().getNombrelargo().equals("") || nuevaNovedad.getTipo() == null
              || ((nuevaNovedad.getValortotal() == null || nuevaNovedad.getValortotal() == valor) && nuevaNovedad.getUnidadesparteentera() == null && nuevaNovedad.getUnidadespartefraccion() == null)) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadEmpleado");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadEmpleado').show()");
      } else {
         Date fechaIngreso = administrarNovedadesEmpleados.vigenciaTipoContratoSecEmpleado(empleadoSeleccionado.getId());

         if (fechaIngreso != null) {
            if (nuevaNovedad.getFechainicial() != null) {
               if (nuevaNovedad.getFechainicial().compareTo(fechaIngreso) < 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
                  RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
                  pasa2++;
               }
            } else if (nuevaNovedad.getFechafinal() != null) {
               if (nuevaNovedad.getFechainicial().compareTo(nuevaNovedad.getFechafinal()) > 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
                  RequestContext.getCurrentInstance().execute("PF('fechas').show()");
                  pasa2++;
               }
            }
         } else {
            log.error("La fecha de ingreso retorno null");
            pasa2++;
         }

         if (pasa2 == 0) {
            log.info("Paso todas las validaciones");
            cerrarFiltrado();
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevaNovedad.setSecuencia(l);

//            for (int i = 0; i < lovEmpleados.size(); i++) {
//                if (empleadoSeleccionado.getId().compareTo(lovEmpleados.get(i).getId()) == 0) {
//                    emp = administrarNovedadesEmpleados.elEmpleado(lovEmpleados.get(i).getId());
            emp = administrarNovedadesEmpleados.elEmpleado(empleadoSeleccionado.getId());
//                }
//            }

            // OBTENER EL TERMINAL 
            HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
            String equipo = null;
            java.net.InetAddress localMachine = null;
            if (request.getRemoteAddr().startsWith("127.0.0.1")) {
               localMachine = java.net.InetAddress.getLocalHost();
               equipo = localMachine.getHostAddress();
            } else {
               equipo = request.getRemoteAddr();
            }
            localMachine = java.net.InetAddress.getByName(equipo);

            getAlias();
            log.info("Alias: " + alias);
            getUsuarioBD();
            log.info("UsuarioBD: " + usuarioBD);
            nuevaNovedad.setTerminal(localMachine.getHostName());
            nuevaNovedad.setUsuarioreporta(usuarioBD);
            nuevaNovedad.setEmpleado(emp); //Envia empleado
            log.info("Empleado enviado: " + emp.getNombreCompleto());
            listaNovedadesCrear.add(nuevaNovedad);
            listaNovedades.add(nuevaNovedad);
            contarRegistros();
            novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(nuevaNovedad));
            nuevaNovedad = new Novedades();
            nuevaNovedad.setPeriodicidad(new Periodicidades());
            nuevaNovedad.getPeriodicidad().setNombre("");
            nuevaNovedad.getPeriodicidad().setCodigoStr("");
            nuevaNovedad.setTercero(new Terceros());
            nuevaNovedad.getTercero().setNombre("");
            nuevaNovedad.setConcepto(new Conceptos());
            nuevaNovedad.getConcepto().setDescripcion("");
            nuevaNovedad.getConcepto().setCodigoSTR("0");
            nuevaNovedad.setTipo("FIJA");
            nuevaNovedad.setUsuarioreporta(new Usuarios());
            nuevaNovedad.setTerminal(" ");
            nuevaNovedad.setFechareporte(new Date());

            log.info("nuevaNovedad : " + nuevaNovedad.getFechareporte());
            activoBtnAcumulado = true;
            RequestContext.getCurrentInstance().update("form:ACUMULADOS");
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevaNovedadEmpleado').hide()");
         }
      }
   }

   public void cambioFecha(Date fecha) {
      log.info("feche : " + fecha);
      log.info("controlNovedadesEmpleados.getNuevaNovedad().getFechainicial() : " + nuevaNovedad.getFechainicial());
   }

   public void confirmarDuplicar() {
      int pasa2 = 0;
      int pasa = 0;
      Empleados emp2 = new Empleados();
      if (duplicarNovedad.getFechainicial() == null || duplicarNovedad.getEmpleado() == null
              || duplicarNovedad.getFormula().getNombrelargo() == null || duplicarNovedad.getTipo() == null
              || (duplicarNovedad.getValortotal() == null && duplicarNovedad.getUnidadesparteentera() == null && duplicarNovedad.getUnidadespartefraccion() == null)) {
         pasa++;
      }
      if (pasa == 0) {
         emp2 = administrarNovedadesEmpleados.elEmpleado(empleadoSeleccionado.getId());
         if (duplicarNovedad.getFechainicial() != null) {
            if (duplicarNovedad.getFechainicial().compareTo(emp2.getFechacreacion()) < 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
               RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
               pasa2++;
            }
         } else if (duplicarNovedad.getFechafinal() != null) {
            if (duplicarNovedad.getFechainicial().compareTo(duplicarNovedad.getFechafinal()) > 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
               RequestContext.getCurrentInstance().execute("PF('fechas').show()");
               pasa2++;
            }
         }
         if (pasa2 == 0) {
            listaNovedades.add(duplicarNovedad);
            listaNovedadesCrear.add(duplicarNovedad);
            contarRegistros();
            novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(duplicarNovedad));
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cerrarFiltrado();
            // OBTENER EL TERMINAL 
            String equipo = null;
            java.net.InetAddress localMachine = null;
            getAlias();
            log.info("Alias: " + alias);
            getUsuarioBD();
            log.info("UsuarioBD: " + usuarioBD);
            duplicarNovedad.setTerminal(localMachine.getHostName());
            duplicarNovedad = new Novedades();
            activoBtnAcumulado = true;
            RequestContext.getCurrentInstance().update("form:ACUMULADOS");
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroNovedad");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroNovedad').hide()");
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadEmpleado");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadEmpleado').show()");
      }
   }

   public void asignarIndex(int cualLista, int tipoAct) {
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      tipoActualizacion = tipoAct;

      if (cualLista == 0) {
//         if (cargarTodos) {
         listaEmpleadosNovedad = null;
         cargarTodosEmpleados();
//         cargarTodos = false;
//         }
         contarRegistrosLovEmpleados();
         RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
      } else if (cualLista == 1) {
         cargarLOVConceptos();
         contarRegistrosConceptos();
         RequestContext.getCurrentInstance().update("formLovConceptos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else if (cualLista == 2) {
         cargarLOVFormulas();
         contarRegistrosFormulas();
         RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
         RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
      } else if (cualLista == 3) {
         cargarLOVPeriodicidades();
         contarRegistrosPeriodicidad();
         RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
      } else if (cualLista == 4) {
         cargarLOVTerceros();
         contarRegistrosTerceros();
         RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
      }
      log.info("cual celda :" + cualLista);
      log.info("tipo Actualización :" + tipoActualizacion);
   }

   public void asignarIndex(Novedades novedad, int cualLista, int tipoAct) {

      novedadSeleccionada = novedad;
      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      tipoActualizacion = tipoAct;

      if (cualLista == 1) {
         activarBotonLOV();
         cargarLOVConceptos();
         contarRegistrosConceptos();
         RequestContext.getCurrentInstance().update("formLovConceptos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else if (cualLista == 2) {
         activarBotonLOV();
         cargarLOVFormulas();
         contarRegistrosFormulas();
         RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
         RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
      } else if (cualLista == 3) {
         activarBotonLOV();
         cargarLOVPeriodicidades();
         contarRegistrosPeriodicidad();
         RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
      } else if (cualLista == 4) {
         activarBotonLOV();
         cargarLOVTerceros();
         contarRegistrosTerceros();
         RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
      } else {
         anularBotonLOV();
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   //AUTOCOMPLETAR
   public void modificarNovedades(Novedades novedad, String confirmarCambio, String valor) {

      novedadSeleccionada = novedad;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      } else if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         cargarLOVFormulas();
         novedadSeleccionada.getFormula().setNombresFormula(Formula);
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombresFormula().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setFormula(lovFormulas.get(indiceUnicoElemento));
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
         cargarLOVTerceros();
         novedadSeleccionada.getTercero().setNitalternativo(NitTercero);
         for (int i = 0; i < lovTerceros.size(); i++) {
            if (lovTerceros.get(i).getNitalternativo().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setTercero(lovTerceros.get(indiceUnicoElemento));

         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
            tipoActualizacion = 0;
         }

      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         cargarLOVConceptos();

      } else if (confirmarCambio.equalsIgnoreCase("CODIGOPERIODICIDAD")) {
         cargarLOVConceptos();
         novedadSeleccionada.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
         for (int i = 0; i < lovPeriodicidades.size(); i++) {
            if ((lovPeriodicidades.get(i).getCodigoStr()).startsWith(valor.toString().toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
   }

   public void activarCtrlF11() {
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      FacesContext c = FacesContext.getCurrentInstance();

      if (bandera == 0) {
         nEConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEConceptoCodigo");
         nEConceptoCodigo.setFilterStyle("width: 85% !important");
         nEConceptoDescripcion = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEConceptoDescripcion");
         nEConceptoDescripcion.setFilterStyle("width: 85% !important");
         nEFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechasInicial");
         nEFechasInicial.setFilterStyle("width: 85% !important");
         nEFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechasFinal");
         nEFechasFinal.setFilterStyle("width: 85% !important");
         nEValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEValor");
         nEValor.setFilterStyle("width: 85% !important");
         nESaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nESaldo");
         nESaldo.setFilterStyle("width: 85% !important");
         nEPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEPeriodicidadCodigo");
         nEPeriodicidadCodigo.setFilterStyle("width: 85% !important");
         nEDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDescripcionPeriodicidad");
         nEDescripcionPeriodicidad.setFilterStyle("width: 85% !important");
         nETercerosNit = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETercerosNit");
         nETercerosNit.setFilterStyle("width: 85% !important");
         nETercerosNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETercerosNombre");
         nETercerosNombre.setFilterStyle("width: 85% !important");
         nEFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFormulas");
         nEFormulas.setFilterStyle("width: 85% !important");
         nEHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEHorasDias");
         nEHorasDias.setFilterStyle("width: 85% !important");
         nEMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEMinutosHoras");
         nEMinutosHoras.setFilterStyle("width: 85% !important");
         nETipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETipo");
         nETipo.setFilterStyle("width: 85% !important");
         altoTabla = "105";
         altoTablaEmpl = "77";
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         bandera = 1;

         nEEmpleadosCodigos = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosCodigos");
         nEEmpleadosCodigos.setFilterStyle("width: 80% !important");
         nEEmpleadosNombres = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosNombres");
         nEEmpleadosNombres.setFilterStyle("width: 85% !important");
         nEEmpleadosValor = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosValor");
         nEEmpleadosValor.setFilterStyle("width: 80% !important");
         RequestContext.getCurrentInstance().update("form:datosEmpleados");
      } else {
         cerrarFiltrado();
      }
      contarRegistros();
      contarRegistrosEmpleados();
   }

   public void cerrarFiltrado() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         nEConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEConceptoCodigo");
         nEConceptoCodigo.setFilterStyle("display: none; visibility: hidden;");
         nEConceptoDescripcion = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEConceptoDescripcion");
         nEConceptoDescripcion.setFilterStyle("display: none; visibility: hidden;");
         nEFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechasInicial");
         nEFechasInicial.setFilterStyle("display: none; visibility: hidden;");
         nEFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechasFinal");
         nEFechasFinal.setFilterStyle("display: none; visibility: hidden;");
         nEValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEValor");
         nEValor.setFilterStyle("display: none; visibility: hidden;");
         nESaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nESaldo");
         nESaldo.setFilterStyle("display: none; visibility: hidden;");
         nEPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEPeriodicidadCodigo");
         nEPeriodicidadCodigo.setFilterStyle("display: none; visibility: hidden;");
         nEDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDescripcionPeriodicidad");
         nEDescripcionPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
         nETercerosNit = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETercerosNit");
         nETercerosNit.setFilterStyle("display: none; visibility: hidden;");
         nETercerosNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETercerosNombre");
         nETercerosNombre.setFilterStyle("display: none; visibility: hidden;");
         nEFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFormulas");
         nEFormulas.setFilterStyle("display: none; visibility: hidden;");
         nEHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEHorasDias");
         nEHorasDias.setFilterStyle("display: none; visibility: hidden;");
         nEMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEMinutosHoras");
         nEMinutosHoras.setFilterStyle("display: none; visibility: hidden;");
         nETipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETipo");
         nETipo.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "125";
         altoTablaEmpl = "95";
         bandera = 0;
         nEEmpleadosCodigos = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosCodigos");
         nEEmpleadosCodigos.setFilterStyle("display: none; visibility: hidden;");
         nEEmpleadosNombres = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosNombres");
         nEEmpleadosNombres.setFilterStyle("display: none; visibility: hidden;");
         nEEmpleadosValor = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosValor");
         nEEmpleadosValor.setFilterStyle("display: none; visibility: hidden;");
         filtrarListaNovedades = null;
         filtrarListaEmpleados = null;
         RequestContext.getCurrentInstance().execute("PF('datosNovedadesEmpleado').clearFilters()");
         RequestContext.getCurrentInstance().execute("PF('datosEmpleados').clearFilters()");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         RequestContext.getCurrentInstance().update("form:datosEmpleados");
         tipoLista = 0;
         tipoListaNov = 0;
      }
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (novedadSeleccionada != null) {
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         if (cualCelda == 0) {
            cargarLOVConceptos();
            RequestContext.getCurrentInstance().update("formLovConceptos:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 6) {
            cargarLOVPeriodicidades();
            RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 8) {
            cargarLOVTerceros();
            RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 10) {
            cargarLOVFormulas();
            RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
         }
      }
   }
   //Ubicacion Celda Indice Abajo. //Van los que no son NOT NULL.

   public void cambiarIndice(Novedades novedad, int celda) {
      novedadSeleccionada = novedad;
      if (permitirIndex == true) {
         RequestContext context = RequestContext.getCurrentInstance();
         activoBtnAcumulado = false;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         cualCelda = celda;
         actualNovedadTabla = novedadSeleccionada;
         if (cualCelda == 0) {
            activarBotonLOV();
            CodigoConcepto = novedadSeleccionada.getConcepto().getCodigoSTR();
         } else if (cualCelda == 1) {
            activarBotonLOV();
            DescripcionConcepto = novedadSeleccionada.getConcepto().getDescripcion();
         } else if (cualCelda == 3) {
            anularBotonLOV();
            FechaFinal = novedadSeleccionada.getFechafinal();
         } else if (cualCelda == 5) {
            anularBotonLOV();
            Saldo = novedadSeleccionada.getSaldo();
         } else if (cualCelda == 6) {
            activarBotonLOV();
            CodigoPeriodicidad = novedadSeleccionada.getPeriodicidad().getCodigoStr();
         } else if (cualCelda == 7) {
            activarBotonLOV();
            DescripcionPeriodicidad = novedadSeleccionada.getPeriodicidad().getNombre();
         } else if (cualCelda == 8) {
            activarBotonLOV();
            NitTercero = novedadSeleccionada.getTercero().getNitalternativo();
         } else if (cualCelda == 9) {
            activarBotonLOV();
            NombreTercero = novedadSeleccionada.getTercero().getNombre();
         } else if (cualCelda == 10) {
            anularBotonLOV();
            HoraDia = novedadSeleccionada.getUnidadesparteentera();
         } else if (cualCelda == 11) {
            anularBotonLOV();
            MinutoHora = novedadSeleccionada.getUnidadespartefraccion();
         } else {
            anularBotonLOV();
         }
      }
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (novedadSeleccionada != null) {
         editarNovedades = novedadSeleccionada;

         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptosCodigos");
            RequestContext.getCurrentInstance().execute("PF('editarConceptosCodigos').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptosDescripciones");
            RequestContext.getCurrentInstance().execute("PF('editarConceptosDescripciones').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaInicial");
            RequestContext.getCurrentInstance().execute("PF('editFechaInicial').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechasFinales");
            RequestContext.getCurrentInstance().execute("PF('editFechasIniciales').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValores");
            RequestContext.getCurrentInstance().execute("PF('editarSaldos').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarSaldos");
            RequestContext.getCurrentInstance().execute("PF('editarSaldos').show()");
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPeriodicidadesCodigos");
            RequestContext.getCurrentInstance().execute("PF('editarPeriodicidadesCodigos').show()");
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPeriodicidadesDescripciones");
            RequestContext.getCurrentInstance().execute("PF('editarPeriodicidadesDescripciones').show()");
            cualCelda = -1;
         } else if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTercerosNit");
            RequestContext.getCurrentInstance().execute("PF('editarTercerosNit').show()");
            cualCelda = -1;
         } else if (cualCelda == 9) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTercerosNombres");
            RequestContext.getCurrentInstance().execute("PF('editarTercerosNombres').show()");
            cualCelda = -1;
         } else if (cualCelda == 10) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulas");
            RequestContext.getCurrentInstance().execute("PF('editarFormulas').show()");
            cualCelda = -1;
         } else if (cualCelda == 11) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarHorasDias");
            RequestContext.getCurrentInstance().execute("PF('editarHorasDias').show()");
            cualCelda = -1;
         } else if (cualCelda == 12) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarMinutosHoras");
            RequestContext.getCurrentInstance().execute("PF('editarMinutosHoras').show()");
            cualCelda = -1;
         } else if (cualCelda == 13) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipos");
            RequestContext.getCurrentInstance().execute("PF('editarTipos').show()");
            cualCelda = -1;
         }
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("CONCEPTO")) {
         if (tipoNuevo == 1) {
            CodigoConcepto = nuevaNovedad.getConcepto().getCodigoSTR();
         } else if (tipoNuevo == 2) {
            CodigoConcepto = duplicarNovedad.getConcepto().getCodigoSTR();
         }
         log.info("tipoNuevo: " + tipoNuevo);
         log.info("campo:" + Campo);
      } else if (Campo.equals("CODIGO")) {
         if (tipoNuevo == 1) {
            CodigoPeriodicidad = nuevaNovedad.getPeriodicidad().getCodigoStr();
         } else if (tipoNuevo == 2) {
            CodigoPeriodicidad = duplicarNovedad.getPeriodicidad().getCodigoStr();
         }
      } else if (Campo.equals("NIT")) {
         if (tipoNuevo == 1) {
            NitTercero = nuevaNovedad.getTercero().getNitalternativo();
         } else if (tipoNuevo == 2) {
            NitTercero = duplicarNovedad.getTercero().getNitalternativo();
         }
      } else if (Campo.equals("FORMULA")) {
         if (tipoNuevo == 1) {
            Formula = nuevaNovedad.getFormula().getNombrelargo();
         } else if (tipoNuevo == 2) {
            Formula = duplicarNovedad.getFormula().getNombrelargo();
         }
      }
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valor, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         cargarLOVFormulas();
         if (tipoNuevo == 1) {
            nuevaNovedad.getFormula().setNombrelargo(Formula);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getFormula().setNombrelargo(Formula);
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrelargo().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
            }
         } else {
            RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
         cargarLOVTerceros();
         if (tipoNuevo == 1) {
            nuevaNovedad.getTercero().setNitalternativo(NitTercero);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getTercero().setNitalternativo(NitTercero);
         }

         for (int i = 0; i < lovTerceros.size(); i++) {
            if (lovTerceros.get(i).getNitalternativo() != null) {
               if (lovTerceros.get(i).getNitalternativo().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setTercero(lovTerceros.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNombre");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setTercero(lovTerceros.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNombre");
            }
         } else {
            RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNombre");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNombre");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("CODIGO")) {
         cargarLOVPeriodicidades();
         if (tipoNuevo == 1) {
            nuevaNovedad.getPeriodicidad().setCodigoStr(nuevaNovedad.getPeriodicidad().getCodigoStr());
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getPeriodicidad().setCodigoStr(duplicarNovedad.getPeriodicidad().getCodigoStr());
         }

         for (int i = 0; i < lovPeriodicidades.size(); i++) {
            if (lovPeriodicidades.get(i).getCodigoStr().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriocidadDescripcion");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriocidadDescripcion");
            }
         } else {
            RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriocidadDescripcion");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriocidadDescripcion");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         cargarLOVConceptos();
         if (tipoNuevo == 1) {
            nuevaNovedad.getConcepto().setCodigoSTR(CodigoConcepto);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getConcepto().setCodigoSTR(CodigoConcepto);
         }
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getCodigoSTR().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setConcepto(lovConceptos.get(indiceUnicoElemento));
               Formulas formula = verificarFormulaConcepto(nuevaNovedad.getConcepto().getSecuencia());
               nuevaNovedad.setFormula(formula);
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConceptoDescripcion");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setConcepto(lovConceptos.get(indiceUnicoElemento));
               Formulas formula = verificarFormulaConcepto(duplicarNovedad.getConcepto().getSecuencia());
               duplicarNovedad.setFormula(formula);
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConceptoDescripcion");
            }
         } else {
            RequestContext.getCurrentInstance().update("formLovConceptos:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConceptoDescripcion");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConceptoDescripcion");
            }
         }
      }
   }

   public void actualizarEmpleadosNovedad() {
      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
//        PruebaEmpleados pe = administrarNovedadesEmpleados.novedadEmpleado(empleadoSeleccionadoLov.getId());
//        if (pe != null) {
//            listaEmpleadosNovedad.add(pe);
//        } else {
//            pe = new PruebaEmpleados();
//            pe.setId(empleadoSeleccionadoLov.getId());
//            pe.setCodigo(empleadoSeleccionadoLov.getCodigo());
//            pe.setNombre(empleadoSeleccionadoLov.getNombre());
//            pe.setValor(null);
//            pe.setTipo(empleadoSeleccionadoLov.getTipo());
//            listaEmpleadosNovedad.add(pe);
//        }
      log.info("empleadoSeleccionadoLov : " + empleadoSeleccionadoLov + "  " + empleadoSeleccionadoLov.getNombre());
      listaEmpleadosNovedad.clear();
      listaEmpleadosNovedad.add(empleadoSeleccionadoLov);
      empleadoSeleccionado = listaEmpleadosNovedad.get(0);
      contarRegistrosLovEmpleados();
      //            secuenciaEmpleado = empleadoSeleccionadoLov.getId();
      listaNovedades = null;
      getListaNovedades();

      context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      //RequestContext.getCurrentInstance().update("form:datosEmpleados");
      //RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      contarRegistros();
      contarRegistrosEmpleados();

      filtrarListaEmpleados = null;
      empleadoSeleccionadoLov = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   public void actualizarFormulas() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("seleccionFormulas: " + formulaLovSeleccioiada);
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setFormula(formulaLovSeleccioiada);
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      } else if (tipoActualizacion == 1) {
         log.info("seleccionFormulas: " + formulaLovSeleccioiada);
         nuevaNovedad.setFormula(formulaLovSeleccioiada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setFormula(formulaLovSeleccioiada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");

      }
      filtradoslovFormulas = null;
      formulaLovSeleccioiada = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formLovFormulas:LOVFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('formulasDialogo').hide()");
      //RequestContext.getCurrentInstance().update("formLovFormulas:LOVFormulas");
   }

   public void actualizarConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      Formulas formula;
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setConcepto(conceptoLovSeleccionado);
         formula = verificarFormulaConcepto(conceptoLovSeleccionado.getSecuencia());
         novedadSeleccionada.setFormula(formula);

         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
         }

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");

      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setConcepto(conceptoLovSeleccionado);
         formula = verificarFormulaConcepto(conceptoLovSeleccionado.getSecuencia());
         nuevaNovedad.setFormula(formula);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");

      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setConcepto(conceptoLovSeleccionado);
         formula = verificarFormulaConcepto(conceptoLovSeleccionado.getSecuencia());
         duplicarNovedad.setFormula(formula);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslovConceptos = null;
      conceptoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formLovConceptos:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   public Formulas verificarFormulaConcepto(BigInteger secCon) {
      FormulasConceptos formulasConceptoSel = administrarFormulaConcepto.cargarFormulasConcepto(secCon);
      Formulas formulaR = new Formulas();
      BigInteger autoFormula;
      cargarLOVFormulas();
      if (formulasConceptoSel != null) {
//         if (!formulasConceptoSel.isEmpty()) {
         autoFormula = formulasConceptoSel.getFormula();
      } else {
         autoFormula = new BigInteger("4621544");
      }
      for (int i = 0; i < lovFormulas.size(); i++) {
         if (autoFormula.equals(lovFormulas.get(i).getSecuencia())) {
            formulaR = lovFormulas.get(i);
         }
      }
      return formulaR;
   }

   public void actualizarPeriodicidades() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setPeriodicidad(periodicidadLovSeleccionada);
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setPeriodicidad(periodicidadLovSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setPeriodicidad(periodicidadLovSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslovPeriodicidades = null;
      periodicidadLovSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formLovPeriodicidad:LOVPeriodicidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
      //RequestContext.getCurrentInstance().update("formLovPeriodicidad:LOVPeriodicidades");
   }

   public void actualizarTerceros() {
      listaNovedadesModificar.add(novedadSeleccionada);
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setTercero(terceroLovSeleccionado);
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setTercero(terceroLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setTercero(terceroLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslovTerceros = null;
      terceroLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formLovTerceros:LOVTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
      //RequestContext.getCurrentInstance().update("formLovTerceros:LOVTerceros");
   }

   public void mostrarTodos() {
      log.info("controlNovedadesEmpleados.mostrarTodos...");
//      if (cargarTodos) {
      listaEmpleadosNovedad = null;
      cargarTodosEmpleados();
//         cargarTodos = false;
//      }

      listaEmpleadosNovedad.clear();
      if (lovEmpleados != null) {
         for (int i = 0; i < lovEmpleados.size(); i++) {
            listaEmpleadosNovedad.add(lovEmpleados.get(i));
         }
      }
      listaNovedades = null;
      empleadoSeleccionado = null;
      getListaNovedades();
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      contarRegistros();
      contarRegistrosEmpleados();
      activoBtnAcumulado = true;
      filtrarListaEmpleados = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   //DUPLICAR ENCARGATURA
   public void duplicarEN() {
      if (novedadSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         duplicarNovedad = new Novedades();
         k++;
         l = BigInteger.valueOf(k);
         Empleados emple = administrarNovedadesEmpleados.elEmpleado(empleadoSeleccionado.getId());

         duplicarNovedad.setSecuencia(l);
         duplicarNovedad.setEmpleado(emple);
         duplicarNovedad.setConcepto(novedadSeleccionada.getConcepto());
         duplicarNovedad.setFechainicial(novedadSeleccionada.getFechainicial());
         duplicarNovedad.setFechafinal(novedadSeleccionada.getFechafinal());
         duplicarNovedad.setFechareporte(novedadSeleccionada.getFechareporte());
         duplicarNovedad.setValortotal(novedadSeleccionada.getValortotal());
         duplicarNovedad.setSaldo(novedadSeleccionada.getSaldo());
         duplicarNovedad.setPeriodicidad(novedadSeleccionada.getPeriodicidad());
         duplicarNovedad.setTercero(novedadSeleccionada.getTercero());
         duplicarNovedad.setFormula(novedadSeleccionada.getFormula());
         duplicarNovedad.setUnidadesparteentera(novedadSeleccionada.getUnidadesparteentera());
         duplicarNovedad.setUnidadespartefraccion(novedadSeleccionada.getUnidadespartefraccion());
         duplicarNovedad.setTipo(novedadSeleccionada.getTipo());
         duplicarNovedad.setTerminal(novedadSeleccionada.getTerminal());
         duplicarNovedad.setUsuarioreporta(novedadSeleccionada.getUsuarioreporta());

         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroNovedad').show()");
      }
   }

   public void cancelarCambioEmpleados() {
      filtrarLovEmpleados = null;
      empleadoSeleccionadoLov = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");

      RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formLovEmpleados:aceptarE");

   }

   public void cancelarCambioFormulas() {
      filtradoslovFormulas = null;
      formulaLovSeleccioiada = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovFormulas:LOVFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('formulasDialogo').hide()");
   }

   public void cancelarCambioConceptos() {
      filtradoslovConceptos = null;
      conceptoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovConceptos:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   public void cancelarCambioPeriodicidades() {
      filtradoslovPeriodicidades = null;
      periodicidadLovSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovPeriodicidad:LOVPeriodicidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
   }

   public void cancelarCambioTerceros() {
      filtradoslovTerceros = null;
      terceroLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovTerceros:LOVTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
   }

   public void todasNovedades() {
      log.info("Ingrese a todasNovedades()");
      listaNovedades = administrarNovedadesEmpleados.todasNovedades(empleadoSeleccionado.getId());
      activoBtnAcumulado = true;
      contarRegistros();
      todas = true;
      actuales = false;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:TODAS");
      RequestContext.getCurrentInstance().update("form:ACTUALES");
      log.info("Sali de todasNovedades() listaNovedades : " + listaNovedades);
   }

   public void actualesNovedades() {
      listaNovedades.clear();
      listaNovedades = administrarNovedadesEmpleados.novedadesEmpleado(empleadoSeleccionado.getId());
      activoBtnAcumulado = true;
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      todas = false;
      actuales = true;
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:TODAS");
      RequestContext.getCurrentInstance().update("form:ACTUALES");
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDFTablasAnchas();
      exporter.export(context, tabla, "NovedadesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      RequestContext contexto = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      contexto.update("form:ACUMULADOS");
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "NovedadesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      RequestContext contexto = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      contexto.update("form:ACUMULADOS");
   }

   //LIMPIAR NUEVO REGISTRO NOVEDAD
   public void limpiarNuevaNovedad() {
      nuevaNovedad = new Novedades();
      nuevaNovedad.setPeriodicidad(new Periodicidades());
      nuevaNovedad.getPeriodicidad().setNombre("");
      nuevaNovedad.getPeriodicidad().setCodigoStr("");
      nuevaNovedad.setTercero(new Terceros());
      nuevaNovedad.getTercero().setNombre("");
      nuevaNovedad.setConcepto(new Conceptos());
      nuevaNovedad.getConcepto().setDescripcion("");
      nuevaNovedad.getConcepto().setCodigoSTR("0");
      nuevaNovedad.setTipo("FIJA");
      nuevaNovedad.setUsuarioreporta(new Usuarios());
      nuevaNovedad.setTerminal(" ");
      nuevaNovedad.setFechareporte(new Date());
      resultado = 0;
   }

   //LIMPIAR DUPLICAR
   /**
    * Metodo que limpia los datos de un duplicar Encargaturas
    */
   public void limpiarduplicarNovedades() {
      duplicarNovedad = new Novedades();
      duplicarNovedad.setPeriodicidad(new Periodicidades());
      duplicarNovedad.getPeriodicidad().setNombre(" ");
      duplicarNovedad.getPeriodicidad().setCodigoStr(" ");
      duplicarNovedad.setTercero(new Terceros());
      duplicarNovedad.getTercero().setNombre(" ");
      duplicarNovedad.setConcepto(new Conceptos());
      duplicarNovedad.getConcepto().setDescripcion(" ");
      duplicarNovedad.getConcepto().setCodigoSTR("0");
      duplicarNovedad.setTipo("FIJA");
      duplicarNovedad.setUsuarioreporta(new Usuarios());
      duplicarNovedad.setTerminal(" ");
      duplicarNovedad.setFechareporte(new Date());
   }

   //RASTROS 
   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      log.info("lol");
      if (novedadSeleccionada != null) {
         log.info("lol 2");
         int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADES");
         log.info("resultado: " + result);
         if (result == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (result == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (result == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (result == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (result == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
//            } else {
//                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//            }
      } else if (administrarRastros.verificarHistoricosTabla("NOVEDADES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //BORRAR NOVEDADES
   public void borrarNovedades() {
      if (novedadSeleccionada != null) {
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         if (!listaNovedadesModificar.isEmpty() && listaNovedadesModificar.contains(novedadSeleccionada)) {
            listaNovedadesModificar.remove(novedadSeleccionada);
            listaNovedadesBorrar.add(novedadSeleccionada);
         } else if (!listaNovedadesCrear.isEmpty() && listaNovedadesCrear.contains(novedadSeleccionada)) {
            listaNovedadesCrear.remove(novedadSeleccionada);
         } else {
            listaNovedadesBorrar.add(novedadSeleccionada);
         }
         listaNovedades.remove(novedadSeleccionada);
         if (tipoListaNov == 1) {
            filtrarListaNovedades.remove(novedadSeleccionada);
         }
         contarRegistrosLovEmpleados();
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         novedadSeleccionada = null;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void anularBotonLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void activarBotonLOV() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void cargarLOVConceptos() {
      if (lovConceptos == null || lovConceptos.isEmpty()) {
         if (listasRecurrentes.getLovConceptos().isEmpty()) {
            lovConceptos = administrarNovedadesEmpleados.lovConceptos();
            if (lovConceptos != null) {
               listasRecurrentes.setLovConceptos(lovConceptos);
               log.warn("GUARDANDO lovConceptos en Listas recurrentes");
            }
         } else {
            lovConceptos = new ArrayList<Conceptos>(listasRecurrentes.getLovConceptos());
            log.warn("CONSULTANDO lovConceptos de Listas recurrentes");
         }
      }
   }

   public void cargarLOVPeriodicidades() {
      if (lovPeriodicidades == null || lovPeriodicidades.isEmpty()) {
         lovPeriodicidades = administrarNovedadesEmpleados.lovPeriodicidades();
      }
   }

   public void cargarLOVFormulas() {
      if (lovFormulas == null || lovFormulas.isEmpty()) {
         if (listasRecurrentes.getLovFormulas().isEmpty()) {
            lovFormulas = administrarNovedadesEmpleados.lovFormulas();
            if (lovFormulas != null) {
               listasRecurrentes.setLovFormulas(lovFormulas);
            }
         } else {
            lovFormulas = new ArrayList<Formulas>(listasRecurrentes.getLovFormulas());
         }
      }
   }

   public void cargarLOVTerceros() {
      if (lovTerceros == null || lovTerceros.isEmpty()) {
         lovTerceros = administrarNovedadesEmpleados.lovTerceros();
      }
   }

   public void cargarTodosEmpleados() {
      if (listaEmpleadosNovedad == null) {
         listaEmpleadosNovedad = administrarNovedadesEmpleados.empleadosNovedades();
         lovEmpleados = new ArrayList<PruebaEmpleados>();
         for (int i = 0; i < listaEmpleadosNovedad.size(); i++) {
            lovEmpleados.add(listaEmpleadosNovedad.get(i));
         }
      }
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      if (tipoListaNov == 0) {
         tipoListaNov = 1;
      }
      novedadSeleccionada = null;
      anularBotonLOV();
      contarRegistros();
   }

   public void eventoFiltrarEmpleados() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      novedadSeleccionada = null;
      empleadoSeleccionado = null;
      listaNovedades = null;
      filtrarListaNovedades = null;
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      if (tipoListaNov == 1) {
         RequestContext.getCurrentInstance().execute("PF('datosNovedadesEmpleado').clearFilters()");
      }
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      anularBotonLOV();
      contarRegistrosEmpleados();
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosEmpleados() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
   }

   public void contarRegistrosLovEmpleados() {
      RequestContext.getCurrentInstance().update("formLovEmpleados:infoRegistroEmpleadosLOV");
   }

   public void contarRegistrosConceptos() {
      RequestContext.getCurrentInstance().update("formLovConceptos:infoRegistroConceptos");
   }

   public void contarRegistrosTerceros() {
      RequestContext.getCurrentInstance().update("formLovTerceros:infoRegistroTerceros");
   }

   public void contarRegistrosFormulas() {
      RequestContext.getCurrentInstance().update("formLovFormulas:infoRegistroFormulas");
   }

   public void contarRegistrosPeriodicidad() {
      RequestContext.getCurrentInstance().update("formLovPeriodicidad:infoRegistroPeriodicidad");
   }

   //------------------------INICIO GET's & SET's-----------------------------
   public void setListaEmpleadosNovedad(List<PruebaEmpleados> listaEmpleados) {
      this.listaEmpleadosNovedad = listaEmpleados;
   }

   public List<PruebaEmpleados> getFiltrarListaEmpleados() {
      return filtrarListaEmpleados;
   }

   public void setFiltrarListaEmpleados(List<PruebaEmpleados> filtradosListaEmpleadosNovedad) {
      this.filtrarListaEmpleados = filtradosListaEmpleadosNovedad;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public PruebaEmpleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(PruebaEmpleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   //LISTA NOVEDADES
   public List<Novedades> getListaNovedades() {
      if (listaNovedades == null && empleadoSeleccionado != null) {
         listaNovedades = administrarNovedadesEmpleados.novedadesEmpleado(empleadoSeleccionado.getId());
      }
      return listaNovedades;
   }

   public void setListaNovedades(List<Novedades> listaNovedades) {
      this.listaNovedades = listaNovedades;
   }

   public List<Novedades> getFiltrarListaNovedades() {
      return filtrarListaNovedades;
   }

   public void setFiltrarListaNovedades(List<Novedades> filtrarListaNovedades) {
      this.filtrarListaNovedades = filtrarListaNovedades;
   }
   //L.O.V PERIODICIDADES

   public List<Periodicidades> getLovPeriodicidades() {
      return lovPeriodicidades;
   }

   public void setLovPeriodicidades(List<Periodicidades> lovPeriodicidades) {
      this.lovPeriodicidades = lovPeriodicidades;
   }

   public List<Periodicidades> getFiltradoslovPeriodicidades() {
      return filtradoslovPeriodicidades;
   }

   public void setFiltradoslovPeriodicidades(List<Periodicidades> filtradoslovPeriodicidades) {
      this.filtradoslovPeriodicidades = filtradoslovPeriodicidades;
   }

   public Periodicidades getPeriodicidadLovSeleccionada() {
      return periodicidadLovSeleccionada;
   }

   public void setPeriodicidadLovSeleccionada(Periodicidades periodicidadLovSeleccionada) {
      this.periodicidadLovSeleccionada = periodicidadLovSeleccionada;
   }
   //L.O.V CONCEPTOS

   public List<Conceptos> getLovConceptos() {
      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> lovConceptos) {
      this.lovConceptos = lovConceptos;
   }
   //L.O.V FORMULAS

   public List<Formulas> getLovFormulas() {
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> lovFormulas) {
      this.lovFormulas = lovFormulas;
   }

   public List<Conceptos> getFiltradoslovConceptos() {
      return filtradoslovConceptos;
   }

   public void setFiltradoslovConceptos(List<Conceptos> filtradoslovConceptos) {
      this.filtradoslovConceptos = filtradoslovConceptos;
   }

   public Conceptos getConceptoLovSeleccionado() {
      return conceptoLovSeleccionado;
   }

   public void setConceptoLovSeleccionado(Conceptos conceptoLovSeleccionado) {
      this.conceptoLovSeleccionado = conceptoLovSeleccionado;
   }

   public List<Formulas> getFiltradoslovFormulas() {
      return filtradoslovFormulas;
   }

   public void setFiltradoslovFormulas(List<Formulas> filtradoslovFormulas) {
      this.filtradoslovFormulas = filtradoslovFormulas;
   }

   public Formulas getFormulaLovSeleccioiada() {
      return formulaLovSeleccioiada;
   }

   public void setFormulaLovSeleccioiada(Formulas formulaLovSeleccioiada) {
      this.formulaLovSeleccioiada = formulaLovSeleccioiada;
   }
   //L.O.V TERCEROS

   public List<Terceros> getLovTerceros() {
      return lovTerceros;
   }

   public void setLovTerceros(List<Terceros> lovTerceros) {
      this.lovTerceros = lovTerceros;
   }

   public List<Terceros> getFiltradoslovTerceros() {
      return filtradoslovTerceros;
   }

   public void setFiltradoslovTerceros(List<Terceros> filtradoslovTerceros) {
      this.filtradoslovTerceros = filtradoslovTerceros;
   }

   public Terceros getTerceroLovSeleccionado() {
      return terceroLovSeleccionado;
   }

   public void setTerceroLovSeleccionado(Terceros terceroLovSeleccionado) {
      this.terceroLovSeleccionado = terceroLovSeleccionado;
   }

   public Novedades getNuevaNovedad() {
      return nuevaNovedad;
   }

   public void setNuevaNovedad(Novedades nuevaNovedad) {
      this.nuevaNovedad = nuevaNovedad;
   }

   public Novedades getEditarNovedades() {
      return editarNovedades;
   }

   public void setEditarNovedades(Novedades editarNovedades) {
      this.editarNovedades = editarNovedades;
   }

   public Novedades getDuplicarNovedad() {
      return duplicarNovedad;
   }

   public void setDuplicarNovedad(Novedades duplicarNovedad) {
      this.duplicarNovedad = duplicarNovedad;
   }

   public List<PruebaEmpleados> getListaEmpleadosNovedad() {
      if (listaEmpleadosNovedad == null) {
         cargarTodosEmpleados();
      }
      return listaEmpleadosNovedad;
   }

   public String getAlias() {
      alias = administrarNovedadesEmpleados.alias();
      log.info("Alias: " + alias);
      return alias;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   public Usuarios getUsuarioBD() {
      getAlias();
      usuarioBD = administrarNovedadesEmpleados.usuarioBD(alias);
      return usuarioBD;
   }

   public void setUsuarioBD(Usuarios usuarioBD) {
      this.usuarioBD = usuarioBD;
   }

   public int getResultado() {
      if (!listaNovedadesBorrar.isEmpty()) {
         for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
            resultado = administrarNovedadesEmpleados.solucionesFormulas(listaNovedadesBorrar.get(i).getSecuencia());
         }
      }
      return resultado;
   }

   public boolean isTodas() {
      return todas;
   }

   public boolean isActuales() {
      return actuales;
   }

   public boolean isActivoBtnAcumulado() {
      return activoBtnAcumulado;
   }

   public void setActivoBtnAcumulado(boolean activoBtnAcumulado) {
      this.activoBtnAcumulado = activoBtnAcumulado;
   }

   public Novedades getActualNovedadTabla() {
      return actualNovedadTabla;
   }

   public void setActualNovedadTabla(Novedades actualNovedadTabla) {
      this.actualNovedadTabla = actualNovedadTabla;
   }

   public Novedades getNovedadSeleccionada() {
      return novedadSeleccionada;
   }

   public void setNovedadSeleccionada(Novedades novedadSeleccionada) {
      this.novedadSeleccionada = novedadSeleccionada;
   }

   public String getAltoTablaEmpl() {
      return altoTablaEmpl;
   }

   public void setAltoTablaEmpl(String altoTablaEmpl) {
      this.altoTablaEmpl = altoTablaEmpl;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getAltoTablaReg() {
      if (altoTabla.equals("105")) {
         altoTablaReg = "4";
      } else {
         altoTablaReg = "5";
      }
      return altoTablaReg;
   }

   public void setAltoTablaReg(String altoTablaReg) {
      this.altoTablaReg = altoTablaReg;
   }

   public String getAltoTablaRegEmp() {
      if (altoTabla.equals("105")) {
         altoTablaRegEmp = "4";
      } else {
         altoTablaRegEmp = "5";
      }
      return altoTablaRegEmp;
   }

   public void setAltoTablaRegEmp(String altoTablaRegEmp) {
      this.altoTablaRegEmp = altoTablaRegEmp;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public PruebaEmpleados getEmpleadoSeleccionadoLov() {
      return empleadoSeleccionadoLov;
   }

   public void setEmpleadoSeleccionadoLov(PruebaEmpleados empleadoSeleccionadoLov) {
      this.empleadoSeleccionadoLov = empleadoSeleccionadoLov;
   }

   public List<PruebaEmpleados> getFiltrarLovEmpleados() {
      return filtrarLovEmpleados;
   }

   public void setFiltrarLovEmpleados(List<PruebaEmpleados> filtrarLovEmpleados) {
      this.filtrarLovEmpleados = filtrarLovEmpleados;
   }

   public List<PruebaEmpleados> getLovEmpleados() {
      return lovEmpleados;
   }

   public void setLovEmpleados(List<PruebaEmpleados> lovEmpleados) {
      this.lovEmpleados = lovEmpleados;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNovedadesEmpleado");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovConceptos:LOVConceptos");
      if (filtradoslovConceptos != null) {
         if (filtradoslovConceptos.size() == 1) {
            conceptoLovSeleccionado = filtradoslovConceptos.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVConceptos').unselectAllRows();PF('LOVConceptos').selectRow(0);");
         } else {
            conceptoLovSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('LOVConceptos').unselectAllRows();");
         }
      } else {
         conceptoLovSeleccionado = null;
         aceptar = true;
      }
      infoRegistroConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroConceptos;
   }

   public void setInfoRegistroConceptos(String infoRegistroConceptos) {
      this.infoRegistroConceptos = infoRegistroConceptos;
   }

   public String getInfoRegistroPeriodicidad() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovPeriodicidad:LOVPeriodicidades");
      if (filtradoslovPeriodicidades != null) {
         if (filtradoslovPeriodicidades.size() == 1) {
            periodicidadLovSeleccionada = filtradoslovPeriodicidades.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').unselectAllRows();PF('LOVPeriodicidades').selectRow(0);");
         } else {
            periodicidadLovSeleccionada = null;
            RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').unselectAllRows();");
         }
      } else {
         periodicidadLovSeleccionada = null;
         aceptar = true;
      }
      infoRegistroPeriodicidad = String.valueOf(tabla.getRowCount());
      return infoRegistroPeriodicidad;
   }

   public void setInfoRegistroPeriodicidad(String infoRegistroPeriodicidad) {
      this.infoRegistroPeriodicidad = infoRegistroPeriodicidad;
   }

   public String getInfoRegistroFormulas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovFormulas:LOVFormulas");
      if (filtradoslovFormulas != null) {
         if (filtradoslovFormulas.size() == 1) {
            formulaLovSeleccioiada = filtradoslovFormulas.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVFormulas').unselectAllRows();PF('LOVFormulas').selectRow(0);");
         } else {
            formulaLovSeleccioiada = null;
            RequestContext.getCurrentInstance().execute("PF('LOVFormulas').unselectAllRows();");
         }
      } else {
         formulaLovSeleccioiada = null;
         aceptar = true;
      }
      infoRegistroFormulas = String.valueOf(tabla.getRowCount());
      return infoRegistroFormulas;
   }

   public void setInfoRegistroFormulas(String infoRegistroFormulas) {
      this.infoRegistroFormulas = infoRegistroFormulas;
   }

   public String getInfoRegistroTerceros() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovTerceros:LOVTerceros");
      if (filtradoslovTerceros != null) {
         if (filtradoslovTerceros.size() == 1) {
            terceroLovSeleccionado = filtradoslovTerceros.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVTerceros').unselectAllRows();PF('LOVTerceros').selectRow(0);");
         } else {
            terceroLovSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('LOVTerceros').unselectAllRows();");
         }
      } else {
         terceroLovSeleccionado = null;
         aceptar = true;
      }
      infoRegistroTerceros = String.valueOf(tabla.getRowCount());
      return infoRegistroTerceros;
   }

   public void setInfoRegistroTerceros(String infoRegistroTerceros) {
      this.infoRegistroTerceros = infoRegistroTerceros;
   }

   public String getInfoRegistrosEmpleadosNovedades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpleados");
      infoRegistrosEmpleadosNovedades = String.valueOf(tabla.getRowCount());
      return infoRegistrosEmpleadosNovedades;
   }

   public void setInfoRegistrosEmpleadosNovedades(String infoRegistrosEmpleadosNovedades) {
      this.infoRegistrosEmpleadosNovedades = infoRegistrosEmpleadosNovedades;
   }

   public String getInfoRegistroEmpleadosLOV() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovEmpleados:LOVEmpleados");
      if (filtrarLovEmpleados != null) {
         if (filtrarLovEmpleados.size() == 1) {
            empleadoSeleccionadoLov = filtrarLovEmpleados.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();PF('LOVEmpleados').selectRow(0);");
         } else {
            empleadoSeleccionadoLov = null;
            RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();");
         }
      } else {
         empleadoSeleccionadoLov = null;
         aceptar = true;
      }
      infoRegistroEmpleadosLOV = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadosLOV;
   }

   public void setInfoRegistroEmpleadosLOV(String infoRegistroEmpleadosLOV) {
      this.infoRegistroEmpleadosLOV = infoRegistroEmpleadosLOV;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

}
