/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.*;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaConceptoInterface;
import InterfaceAdministrar.AdministrarNovedadesTercerosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlNovedadesTerceros implements Serializable {

   private static Logger log = Logger.getLogger(ControlNovedadesTerceros.class);

   @EJB
   AdministrarNovedadesTercerosInterface administrarNovedadesTerceros;
   @EJB
   AdministrarFormulaConceptoInterface administrarFormulaConcepto;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //LISTA NOVEDADES
   private List<Novedades> listaNovedades;
   private List<Novedades> filtradosListaNovedades;
   private Novedades novedadSeleccionada;
   //LISTA DE ARRIBA
   private List<Terceros> listaTercerosNovedad;
   private List<Terceros> filtradosListaTercerosNovedad;
   private Terceros terceroSeleccionado; //Seleccion Mostrar
   //editar celda
   private Novedades editarNovedades;
   private int cualCelda, tipoLista, tipoListaNov;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   private boolean permitirIndex;
   //RASTROS
   private boolean guardado;
   //Crear Novedades
   private List<Novedades> listaNovedadesCrear;
   public Novedades nuevaNovedad;
   private int k;
   private BigInteger l;
   private String mensajeValidacion;
   //Modificar Novedades
   private List<Novedades> listaNovedadesModificar;
   //Borrar Novedades
   private List<Novedades> listaNovedadesBorrar;
   //Autocompletar
   private String CodigoEmpleado, CodigoConcepto, NitTercero, Formula, DescripcionConcepto, DescripcionPeriodicidad, NombreTercero;
   private Date FechaInicial;
   private Date FechaFinal;
   private String CodigoPeriodicidad;
   private BigDecimal Saldo;
   private Integer HoraDia, MinutoHora;
   //L.O.V Conceptos
   private List<Conceptos> lovConceptos;
   private List<Conceptos> filtradoslistaConceptos;
   private Conceptos seleccionConceptos;
   //L.O.V Empleados
   private List<Empleados> lovEmpleados;
   private List<Empleados> filtradoslistaEmpleados;
   private Empleados seleccionEmpleados;
   //L.O.V PERIODICIDADES
   private List<Periodicidades> lovPeriodicidades;
   private List<Periodicidades> filtradoslistaPeriodicidades;
   private Periodicidades seleccionPeriodicidades;
   //L.O.V TERCEROS
   private List<Terceros> lovTerceros;
   private List<Terceros> filtradolovTerceros;
   private Terceros terceroSeleccionadoLOV;
   //L.O.V FORMULAS
   private List<Formulas> lovFormulas;
   private List<Formulas> filtradoslistaFormulas;
   private Formulas seleccionFormulas;
   //Columnas Tabla NOVEDADES
   private Column nTTercerosNombre, nTTercerosNit;
   private Column nTEmpleadoCodigo, nTEmpleadoNombre, nTConceptoCodigo, nTConceptoDescripcion, nTFechasInicial, nTFechasFinal,
           nTValor, nTSaldo, nTPeriodicidadCodigo, nTDescripcionPeriodicidad, nTFormulas, nTHorasDias, nTMinutosHoras, nTTipo;
   //Duplicar
   private Novedades duplicarNovedad;
   //USUARIO
   private String alias;
   private Usuarios usuarioBD;
   //VALIDAR SI EL QUE SE VA A BORRAR ESTÁ EN SOLUCIONES FORMULAS
   private int resultado;
   private boolean todas;
   private boolean actuales;
   private String altoTabla, altoTablaTerc, altoTablaReg, altoTablaRegTer;
   BigDecimal valor = new BigDecimal(Integer.toString(0));
   //Conteo de registros
   private String infoRegistroTerceros, infoRegistroLovTerceros, infoRegistroNovedades, infoRegistroPeriodi, infoRegistroEmpleados, infoRegistroConceptos, infoRegistroFormulas;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlNovedadesTerceros() {
      permitirIndex = true;
      listaNovedades = null;
      lovEmpleados = null;
      lovConceptos = null;
      lovPeriodicidades = null;
      lovFormulas = null;
      lovEmpleados = null;
      todas = false;
      actuales = true;
      listaTercerosNovedad = null;
      aceptar = true;
      guardado = true;
      tipoLista = 0;
      tipoListaNov = 0;
      listaNovedadesBorrar = new ArrayList<Novedades>();
      listaNovedadesCrear = new ArrayList<Novedades>();
      listaNovedadesModificar = new ArrayList<Novedades>();
      //Crear VC
      nuevaNovedad = new Novedades();
      nuevaNovedad.setFormula(new Formulas());
      nuevaNovedad.setTercero(new Terceros());
      nuevaNovedad.setPeriodicidad(new Periodicidades());
      nuevaNovedad.setFechareporte(new Date());
      nuevaNovedad.setTipo("FIJA");
      altoTabla = "145";
      nuevaNovedad.setValortotal(valor);
      terceroSeleccionado = null;
      terceroSeleccionadoLOV = null;
      novedadSeleccionada = null;
      infoRegistroTerceros = "0";
      infoRegistroLovTerceros = "0";
      infoRegistroNovedades = "0";
      infoRegistroPeriodi = "0";
      infoRegistroEmpleados = "0";
      infoRegistroConceptos = "0";
      infoRegistroFormulas = "0";
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
      String pagActual = "novedadtercero";
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
      lovEmpleados = null;
      lovConceptos = null;
      lovPeriodicidades = null;
      lovFormulas = null;
      lovEmpleados = null;
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
         administrarNovedadesTerceros.obtenerConexion(ses.getId());
         administrarFormulaConcepto.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         getListaTercerosNovedad();
         if (listaTercerosNovedad != null) {
            if (!listaTercerosNovedad.isEmpty()) {
               terceroSeleccionado = listaTercerosNovedad.get(0);
               getListaNovedades();
            }
         }
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void limpiarListas() {
      listaNovedadesCrear.clear();
      listaNovedadesBorrar.clear();
      listaNovedadesModificar.clear();
      RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
   }

   //Ubicacion Celda Arriba 
   public void cambiarTercero() {
      //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
      if (listaNovedadesCrear.isEmpty() && listaNovedadesBorrar.isEmpty() && listaNovedadesModificar.isEmpty()) {
         novedadSeleccionada = null;
         listaNovedades = null;
         getListaNovedades();
         if (tipoListaNov == 1) {
            RequestContext.getCurrentInstance().execute("PF('datosNovedadesTercero').clearFilters()");
         }
         contarRegistrosNove();
         RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
         RequestContext.getCurrentInstance().execute("PF('cambiar').show()");
      }
   }

   //RASTROS 
   public void verificarRastro() {
      if (novedadSeleccionada != null) {
         int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADES");
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
      } else if (administrarRastros.verificarHistoricosTabla("NOVEDADES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void cambiarIndice(Novedades novedad, int celda) {
      novedadSeleccionada = novedad;
      if (permitirIndex == true) {
         cualCelda = celda;
         if (cualCelda == 0) {
            CodigoEmpleado = novedadSeleccionada.getEmpleado().getCodigoempleadoSTR();
         } else if (cualCelda == 2) {
            CodigoConcepto = novedadSeleccionada.getConcepto().getCodigoSTR();
         } else if (cualCelda == 4) {
            FechaInicial = novedadSeleccionada.getFechainicial();
         } else if (cualCelda == 5) {
            FechaFinal = novedadSeleccionada.getFechafinal();
         } else if (cualCelda == 7) {
            Saldo = novedadSeleccionada.getSaldo();
         } else if (cualCelda == 8) {
            CodigoPeriodicidad = novedadSeleccionada.getPeriodicidad().getCodigoStr();
         } else if (cualCelda == 9) {
            DescripcionPeriodicidad = novedadSeleccionada.getPeriodicidad().getNombre();
         } else if (cualCelda == 10) {
            NitTercero = novedadSeleccionada.getTercero().getNitalternativo();
         } else if (cualCelda == 11) {
            NombreTercero = novedadSeleccionada.getTercero().getNombre();
         } else if (cualCelda == 13) {
            HoraDia = novedadSeleccionada.getUnidadesparteentera();
         } else if (cualCelda == 14) {
            MinutoHora = novedadSeleccionada.getUnidadespartefraccion();
         }
      }
   }

   public void asignarIndex(Novedades novedad, int columnLOV, int tipoAct) {
      novedadSeleccionada = novedad;
      tipoActualizacion = tipoAct;

      if (columnLOV == 0) {
         cargarLovEmpleados();
         contarRegistrosLovEmpl();
         RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
      } else if (columnLOV == 1) {
         cargarlovConceptos();
         contarRegistrosLovConceptos();
         RequestContext.getCurrentInstance().update("formlovConceptos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else if (columnLOV == 2) {
         cargarLovFormulas();
         contarRegistrosLovFormulas();
         RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
         RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
      } else if (columnLOV == 3) {
         cargarLovPeriodicidades();
         contarRegistrosLovPeriod();
         RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
      } else if (columnLOV == 4) {
         terceroSeleccionadoLOV = null;
         contarRegistrosLovTerceros();
         RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
      }
   }

   public void asignarIndex(int columnLOV, int tipoAct) {
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = tipoAct;

      if (columnLOV == 0) {
         cargarLovEmpleados();
         contarRegistrosLovEmpl();
         RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
      } else if (columnLOV == 1) {
         cargarlovConceptos();
         contarRegistrosLovConceptos();
         RequestContext.getCurrentInstance().update("formlovConceptos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else if (columnLOV == 2) {
         cargarLovFormulas();
         contarRegistrosLovFormulas();
         RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
         RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
      } else if (columnLOV == 3) {
         cargarLovPeriodicidades();
         contarRegistrosLovPeriod();
         RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
      } else if (columnLOV == 4) {
         terceroSeleccionadoLOV = null;
         contarRegistrosLovTerceros();
         RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
      }
   }

   //GUARDAR
   public void guardarCambiosNovedades() {

      int pasas = 0;
      if (guardado == false) {
         log.info("Realizando Operaciones Novedades Terceros");

         getResultado();
         log.info("Resultado: " + resultado);
         if (resultado > 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:solucionesFormulas");
            RequestContext.getCurrentInstance().execute("PF('solucionesFormulas').show()");
            listaNovedadesBorrar.clear();
         }
         if (!listaNovedadesBorrar.isEmpty() && pasas == 0) {
            for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
               log.info("Borrando..." + listaNovedadesBorrar.size());
               if (listaNovedadesBorrar.get(i).getPeriodicidad().getSecuencia() == null) {
                  listaNovedadesBorrar.get(i).setPeriodicidad(null);
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
               administrarNovedadesTerceros.borrarNovedades(listaNovedadesBorrar.get(i));
            }
            log.info("Entra");
            listaNovedadesBorrar.clear();
         }

         if (!listaNovedadesCrear.isEmpty()) {
            for (int i = 0; i < listaNovedadesCrear.size(); i++) {
               log.info("Creando...");

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
               log.info(listaNovedadesCrear.get(i).getTipo());
               administrarNovedadesTerceros.crearNovedades(listaNovedadesCrear.get(i));
            }
            log.info("LimpiaLista");
            listaNovedadesCrear.clear();
         }
         if (!listaNovedadesModificar.isEmpty()) {
            administrarNovedadesTerceros.modificarNovedades(listaNovedadesModificar);
            listaNovedadesModificar.clear();
         }

         log.info("Se guardaron los datos con exito");
         listaNovedades = null;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
         guardado = true;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      log.info("Tamaño lista: " + listaNovedadesCrear.size());
      log.info("Valor k: " + k);
   }

   //BORRAR Novedades
   public void borrarNovedades() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada != null) {
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
            filtradosListaNovedades.remove(novedadSeleccionada);
         }
         RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
         novedadSeleccionada = null;

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //CREAR NOVEDADES
   public void agregarNuevaNovedadTercero() throws UnknownHostException {
      int pasa = 0;
      int pasa2 = 0;
      mensajeValidacion = new String();
      RequestContext context = RequestContext.getCurrentInstance();

      if (nuevaNovedad.getFechainicial() == null) {
         log.info("Entro a Fecha Inicial");
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }

      if (nuevaNovedad.getFechafinal() != null) {
         if (nuevaNovedad.getFechainicial().compareTo(nuevaNovedad.getFechafinal()) > 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
            RequestContext.getCurrentInstance().execute("PF('fechas').show()");
            pasa2++;
         }
      }

      log.info("getEmpleado " + nuevaNovedad.getEmpleado());
      if (nuevaNovedad.getEmpleado().getSecuencia() == null) {
         RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
         RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
         pasa2++;
      }

      if (nuevaNovedad.getEmpleado().getCodigoempleadoSTR().equals("0")) {
         log.info("Entro a Empleado");
         mensajeValidacion = mensajeValidacion + " * Empleado\n";
         pasa++;
      }

      if (nuevaNovedad.getEmpleado() != null && pasa == 0) {
         for (int i = 0; i < lovEmpleados.size(); i++) {
            if (nuevaNovedad.getEmpleado().getSecuencia().compareTo(lovEmpleados.get(i).getSecuencia()) == 0) {
               if (nuevaNovedad.getFechainicial() != null) {
                  if (nuevaNovedad.getFechainicial().compareTo(nuevaNovedad.getEmpleado().getFechacreacion()) < 0) {
                     RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
                     RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
                     pasa2++;
                  }
               }
            }
         }
      }
      if (nuevaNovedad.getConcepto().getCodigoSTR().equals("0") || nuevaNovedad.getConcepto().getCodigoSTR().equals("")) {
         log.info("Entro a Concepto");
         mensajeValidacion = mensajeValidacion + " * Concepto\n";
         pasa++;
      }

      if (nuevaNovedad.getFormula().getNombrelargo().equals("")) {
         log.info("Entro a Formula");
         mensajeValidacion = mensajeValidacion + " * Formula\n";
         pasa++;
      }
      if (nuevaNovedad.getValortotal() == null) {
         log.info("Entro a Valor");
         mensajeValidacion = mensajeValidacion + " * Valor\n";
         pasa++;
      }

      if (nuevaNovedad.getTipo() == null) {
         log.info("Entro a Tipo");
         mensajeValidacion = mensajeValidacion + " * Tipo\n";
         pasa++;
      }

      if (pasa == 0 && pasa2 == 0) {
         if (bandera == 1) {
            cerrarFiltrado();
         }
         //AGREGAR REGISTRO A LA LISTA NOVEDADES .
         k++;
         l = BigInteger.valueOf(k);
         nuevaNovedad.setSecuencia(l);
         // OBTENER EL TERMINAL {
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
         nuevaNovedad.setTercero(terceroSeleccionado);
         nuevaNovedad.setTerminal(localMachine.getHostName());
         nuevaNovedad.setUsuarioreporta(usuarioBD);
         listaNovedadesCrear.add(nuevaNovedad);
         listaNovedades.add(nuevaNovedad);
         log.info(listaNovedadesCrear.size());
         log.info(listaNovedadesCrear.get(0).getTipo());
         log.info(nuevaNovedad.getTipo());
         novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(nuevaNovedad));
         nuevaNovedad = new Novedades();
         nuevaNovedad.setFormula(new Formulas());
         nuevaNovedad.setConcepto(new Conceptos());
         nuevaNovedad.setFechareporte(new Date());
         nuevaNovedad.setPeriodicidad(new Periodicidades());
         nuevaNovedad.setTipo("FIJA");

         RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevaNovedadTercero').hide()");
      } else {
      }
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

   public void seleccionarTipo(String estadoTipo, int indice, int celda) {
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
      RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
   }

   public void eventoFiltrar() {
      if (tipoListaNov == 0) {
         tipoListaNov = 1;
      }
      novedadSeleccionada = null;
      contarRegistrosNove();
   }

   public void eventoFiltrarTerceros() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      novedadSeleccionada = null;
      terceroSeleccionado = null;
      listaNovedades = null;
      filtradosListaNovedades = null;
      if (tipoListaNov == 1) {
         RequestContext.getCurrentInstance().execute("PF('datosNovedadesTercero').clearFilters()");
      }
      RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
      contarRegistrosTerc();
      contarRegistrosNove();
   }

   public void eventoFiltrarLovTerc() {
      contarRegistrosLovTerceros();
   }

   public void eventoFiltrarLovPeriod() {
      contarRegistrosLovPeriod();
   }

   public void eventoFiltrarLovEmpl() {
      contarRegistrosLovEmpl();
   }

   public void eventoFiltrarLovForm() {
      contarRegistrosLovFormulas();
   }

   public void eventoFiltrarLovConcep() {
      contarRegistrosLovConceptos();
   }

//AUTOCOMPLETAR
   public void modificarNovedades(Novedades novedad, String confirmarCambio, String valorConfirmar) {

      novedadSeleccionada = novedad;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();

      if (novedadSeleccionada.getFechafinal().compareTo(novedadSeleccionada.getFechainicial()) < 0) {
         log.info("La fecha Final es Menor que la Inicial");
         RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
         RequestContext.getCurrentInstance().execute("PF('fechas').show()");
//            novedadSeleccionada.setFechainicial(novedadBackup.getFechainicial());
//            novedadSeleccionada.setFechafinal(novedadBackup.getFechafinal());
         RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
      }

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

         RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
      } else if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         cargarLovFormulas();
         contarRegistrosLovFormulas();
         novedadSeleccionada.getFormula().setNombresFormula(Formula);
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombresFormula().startsWith(valorConfirmar.toUpperCase())) {
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
         novedadSeleccionada.getTercero().setNitalternativo(NitTercero);

         for (int i = 0; i < lovTerceros.size(); i++) {
            if (lovTerceros.get(i).getNitalternativo().startsWith(valorConfirmar.toUpperCase())) {
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
      } else if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {
         cargarLovEmpleados();
         contarRegistrosLovEmpl();
         novedadSeleccionada.getEmpleado().setCodigoempleadoSTR(CodigoEmpleado);

         for (int i = 0; i < lovEmpleados.size(); i++) {
            if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toString().toUpperCase())) {

               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("CODIGOPERIODICIDAD")) {
         cargarLovPeriodicidades();
         contarRegistrosLovPeriod();
         log.info("modificarNovedades.seleccionarPeriocidades: " + seleccionPeriodicidades);
         novedadSeleccionada.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
         log.info("modificarNovedades.seleccionarPeriocidades: " + seleccionPeriodicidades);

         for (int i = 0; i < lovPeriodicidades.size(); i++) {
            if ((lovPeriodicidades.get(i).getCodigoStr()).startsWith(valorConfirmar.toString().toUpperCase())) {
               log.info("modificarNovedades.seleccionarPeriocidades: " + seleccionPeriodicidades);
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
            log.info("modificarNovedades.seleccionarPeriocidades: " + seleccionPeriodicidades);
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         cargarlovConceptos();
         contarRegistrosLovConceptos();
         novedadSeleccionada.getConcepto().setCodigoSTR(CodigoConcepto);
         log.info("");

         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getCodigoSTR().startsWith(valorConfirmar.toString().toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setConcepto(lovConceptos.get(indiceUnicoElemento));
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formlovConceptos:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
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
      RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
   }

   //DUPLICAR TERCERO NOVEDAD
   public void duplicarTN() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada != null) {
         duplicarNovedad = new Novedades();
         k++;
         l = BigInteger.valueOf(k);

         duplicarNovedad.setSecuencia(l);
         duplicarNovedad.setEmpleado(novedadSeleccionada.getEmpleado());
         duplicarNovedad.setConcepto(novedadSeleccionada.getConcepto());
         duplicarNovedad.setFechainicial(novedadSeleccionada.getFechainicial());
         duplicarNovedad.setFechafinal(novedadSeleccionada.getFechafinal());
         duplicarNovedad.setFechareporte(novedadSeleccionada.getFechareporte());
         duplicarNovedad.setValortotal(novedadSeleccionada.getValortotal());
         duplicarNovedad.setSaldo(novedadSeleccionada.getSaldo());
         duplicarNovedad.setPeriodicidad(novedadSeleccionada.getPeriodicidad());
         duplicarNovedad.setTercero(terceroSeleccionado);
         duplicarNovedad.setFormula(novedadSeleccionada.getFormula());
         duplicarNovedad.setUnidadesparteentera(novedadSeleccionada.getUnidadesparteentera());
         duplicarNovedad.setUnidadespartefraccion(novedadSeleccionada.getUnidadespartefraccion());
         duplicarNovedad.setTipo(novedadSeleccionada.getTipo());
         duplicarNovedad.setTerminal(novedadSeleccionada.getTerminal());
         duplicarNovedad.setUsuarioreporta(novedadSeleccionada.getUsuarioreporta());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroNovedad').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //LIMPIAR DUPLICAR
   /**
    * Metodo que limpia los datos de un duplicar Novedades
    */
   public void limpiarduplicarNovedades() {
      duplicarNovedad = new Novedades();
      duplicarNovedad.setPeriodicidad(new Periodicidades());
      duplicarNovedad.getPeriodicidad().setNombre(" ");
      duplicarNovedad.setTercero(new Terceros());
      duplicarNovedad.getTercero().setNombre(" ");
      duplicarNovedad.setConcepto(new Conceptos());
      duplicarNovedad.getConcepto().setDescripcion(" ");
      duplicarNovedad.setTipo("FIJA");
      duplicarNovedad.setUsuarioreporta(new Usuarios());
      duplicarNovedad.setTerminal(" ");
      duplicarNovedad.setFechareporte(new Date());
   }

   public void confirmarDuplicar() throws UnknownHostException {
      int pasa2 = 0;
      int pasa = 0;

      if (duplicarNovedad.getFechainicial() == null) {
         log.info("Entro a Fecha Inicial");
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (duplicarNovedad.getFechainicial().compareTo(duplicarNovedad.getFechafinal()) > 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
         RequestContext.getCurrentInstance().execute("PF('fechas').show()");
         pasa2++;
      }
      if (duplicarNovedad.getEmpleado().getPersona().getNombreCompleto().equals(" ")) {
         log.info("Entro a Empleado");
         mensajeValidacion = mensajeValidacion + " * Empleado\n";
         pasa++;
      }
      if (duplicarNovedad.getFormula().getNombrelargo() == null) {
         log.info("Entro a Formula");
         mensajeValidacion = mensajeValidacion + " * Formula\n";
         pasa++;
      }
      if (duplicarNovedad.getValortotal() == null) {
         log.info("Entro a Valor");
         mensajeValidacion = mensajeValidacion + " * Valor\n";
         pasa++;
      }

      if (duplicarNovedad.getEmpleado() == null) {
         log.info("Entro a Empleado");
         mensajeValidacion = mensajeValidacion + " * Empleado\n";
         pasa++;
      }

      if (duplicarNovedad.getTipo() == null) {
         log.info("Entro a Tipo");
         mensajeValidacion = mensajeValidacion + " * Tipo\n";
         pasa++;
      }

      for (int i = 0; i < lovEmpleados.size(); i++) {
         if (duplicarNovedad.getEmpleado().getSecuencia().compareTo(lovEmpleados.get(i).getSecuencia()) == 0) {

            if (duplicarNovedad.getFechainicial().compareTo(duplicarNovedad.getEmpleado().getFechacreacion()) < 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
               RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
               pasa2++;
            }
         }
      }

      /*
         * if
         * (duplicarNovedad.getFechainicial().compareTo(duplicarNovedad.getFechafinal())
         * > 0) { RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
         * RequestContext.getCurrentInstance().execute("PF('fechas').show()"); pasa2++; }
       */
      log.info("Valor Pasa: " + pasa);
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadTercero");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadTercero').show()");
      }

      if (pasa2 == 0) {
         listaNovedades.add(duplicarNovedad);
         listaNovedadesCrear.add(duplicarNovedad);
         novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(duplicarNovedad));
         RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            cerrarFiltrado();
         }

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

         duplicarNovedad.setTerminal(localMachine.getHostName());
         duplicarNovedad.setTercero(terceroSeleccionado);
         duplicarNovedad = new Novedades();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroNovedad");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroNovedad').hide()");
      }
   }

   public void mostrarTodos() {
      RequestContext context = RequestContext.getCurrentInstance();
      listaTercerosNovedad.clear();
//        listaTercerosNovedad = administrarNovedadesTerceros.Terceros();

      if (lovTerceros != null) {
         for (int i = 0; i < lovTerceros.size(); i++) {
            listaTercerosNovedad.add(lovTerceros.get(i));
         }
      }
      contarRegistrosTerc();
      terceroSeleccionado = null;
      listaNovedades = null;
      getListaNovedades();
      contarRegistrosNove();
      filtradosListaTercerosNovedad = null;
      aceptar = true;
      novedadSeleccionada = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext.getCurrentInstance().update("form:datosTerceros");
      RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void actualizarEmpleados() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setEmpleado(seleccionEmpleados);
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
         RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setEmpleado(seleccionEmpleados);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setEmpleado(seleccionEmpleados);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");

      }
      filtradoslistaEmpleados = null;
      seleccionEmpleados = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formLovEmpleados:aceptarE");
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada != null) {
         editarNovedades = novedadSeleccionada;

         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadosCodigos");
            RequestContext.getCurrentInstance().execute("PF('editarEmpleadosCodigos').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadosNombres");
            RequestContext.getCurrentInstance().execute("PF('editarEmpleadosNombres').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptosCodigos");
            RequestContext.getCurrentInstance().execute("PF('editarConceptosCodigos').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptosDescripciones");
            RequestContext.getCurrentInstance().execute("PF('editarConceptosDescripciones').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaInicial");
            RequestContext.getCurrentInstance().execute("PF('editFechaInicial').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechasFinales");
            RequestContext.getCurrentInstance().execute("PF('editFechasFinales').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValores");
            RequestContext.getCurrentInstance().execute("PF('editarValores').show()");
            cualCelda = -1;
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarSaldos");
            RequestContext.getCurrentInstance().execute("PF('editarSaldos').show()");
         } else if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPeriodicidadesCodigos");
            RequestContext.getCurrentInstance().execute("PF('editarPeriodicidadesCodigos').show()");
         } else if (cualCelda == 9) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPeriodicidadesDescripciones");
            RequestContext.getCurrentInstance().execute("PF('editarPeriodicidadesDescripciones').show()");
            cualCelda = -1;
         } else if (cualCelda == 12) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulas");
            RequestContext.getCurrentInstance().execute("PF('editarFormulas').show()");
            cualCelda = -1;
         } else if (cualCelda == 13) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarHorasDias");
            RequestContext.getCurrentInstance().execute("PF('editarHorasDias').show()");
            cualCelda = -1;
         } else if (cualCelda == 14) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarMinutosHoras");
            RequestContext.getCurrentInstance().execute("PF('editarMinutosHoras').show()");
            cualCelda = -1;
         } else if (cualCelda == 15) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipos");
            RequestContext.getCurrentInstance().execute("PF('editarTipos').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      if (novedadSeleccionada != null) {
         if (cualCelda == 0) {
            cargarLovEmpleados();
            contarRegistrosLovEmpl();
            RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 2) {
            cargarlovConceptos();
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("formlovConceptos:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 8) {
            cargarLovPeriodicidades();
            contarRegistrosLovPeriod();
            RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 12) {
            cargarLovFormulas();
            contarRegistrosLovFormulas();
            RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         nTEmpleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTEmpleadoCodigo");
         nTEmpleadoCodigo.setFilterStyle("width: 85% !important;");
         nTEmpleadoNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTEmpleadoNombre");
         nTEmpleadoNombre.setFilterStyle("width: 85% !important;");
         nTConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTConceptoCodigo");
         nTConceptoCodigo.setFilterStyle("width: 85% !important;");
         nTConceptoDescripcion = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTConceptoDescripcion");
         nTConceptoDescripcion.setFilterStyle("width: 85% !important;");
         nTFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTFechasInicial");
         nTFechasInicial.setFilterStyle("width: 85% !important;");
         nTFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTFechasFinal");
         nTFechasFinal.setFilterStyle("width: 85% !important;");
         nTValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTValor");
         nTValor.setFilterStyle("width: 85% !important;");
         nTSaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTSaldo");
         nTSaldo.setFilterStyle("width: 85% !important;");
         nTPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTPeriodicidadCodigo");
         nTPeriodicidadCodigo.setFilterStyle("width: 85% !important;");
         nTDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTDescripcionPeriodicidad");
         nTDescripcionPeriodicidad.setFilterStyle("width: 85% !important;");
         nTFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTFormulas");
         nTFormulas.setFilterStyle("width: 85% !important;");
         nTHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTHorasDias");
         nTHorasDias.setFilterStyle("width: 85% !important;");
         nTMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTMinutosHoras");
         nTMinutosHoras.setFilterStyle("width: 85% !important;");
         nTTipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTTipo");
         nTTipo.setFilterStyle("width: 85% !important;");
         altoTabla = "125";
         RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");

         nTTercerosNombre = (Column) c.getViewRoot().findComponent("form:datosTerceros:nTTercerosNombre");
         nTTercerosNombre.setFilterStyle("width: 85% !important;");
         nTTercerosNit = (Column) c.getViewRoot().findComponent("form:datosTerceros:nTTercerosNit");
         nTTercerosNit.setFilterStyle("width: 80% !important;");
         RequestContext.getCurrentInstance().update("form:datosTerceros");
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltrado();
      }
      contarRegistrosTerc();
      contarRegistrosNove();
   }

   public void cerrarFiltrado() {
      FacesContext c = FacesContext.getCurrentInstance();
      nTEmpleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTEmpleadoCodigo");
      nTEmpleadoCodigo.setFilterStyle("display: none; visibility: hidden;");
      nTEmpleadoNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTEmpleadoNombre");
      nTEmpleadoNombre.setFilterStyle("display: none; visibility: hidden;");
      nTConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTConceptoCodigo");
      nTConceptoCodigo.setFilterStyle("display: none; visibility: hidden;");
      nTConceptoDescripcion = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTConceptoDescripcion");
      nTConceptoDescripcion.setFilterStyle("display: none; visibility: hidden;");
      nTFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTFechasInicial");
      nTFechasInicial.setFilterStyle("display: none; visibility: hidden;");
      nTFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTFechasFinal");
      nTFechasFinal.setFilterStyle("display: none; visibility: hidden;");
      nTValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTValor");
      nTValor.setFilterStyle("display: none; visibility: hidden;");
      nTSaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTSaldo");
      nTSaldo.setFilterStyle("display: none; visibility: hidden;");
      nTPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTPeriodicidadCodigo");
      nTPeriodicidadCodigo.setFilterStyle("display: none; visibility: hidden;");
      nTDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTDescripcionPeriodicidad");
      nTDescripcionPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
      nTFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTFormulas");
      nTFormulas.setFilterStyle("display: none; visibility: hidden;");
      nTHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTHorasDias");
      nTHorasDias.setFilterStyle("display: none; visibility: hidden;");
      nTMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTMinutosHoras");
      nTMinutosHoras.setFilterStyle("display: none; visibility: hidden;");
      nTTipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesTercero:nTTipo");
      nTTipo.setFilterStyle("display: none; visibility: hidden;");
      bandera = 0;
      altoTabla = "145";
      nTTercerosNombre = (Column) c.getViewRoot().findComponent("form:datosTerceros:nTTercerosNombre");
      nTTercerosNombre.setFilterStyle("display: none; visibility: hidden;");
      nTTercerosNit = (Column) c.getViewRoot().findComponent("form:datosTerceros:nTTercerosNit");
      nTTercerosNit.setFilterStyle("display: none; visibility: hidden;");
      filtradosListaNovedades = null;
      filtradosListaTercerosNovedad = null;
      RequestContext.getCurrentInstance().execute("PF('datosNovedadesTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('datosTerceros').clearFilters()");
      RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
      RequestContext.getCurrentInstance().update("form:datosTerceros");
      tipoLista = 0;
      tipoListaNov = 0;
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesTercerosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDFTablasAnchas();
      exporter.export(context, tabla, "NovedadesTerceroPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesTercerosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "NovedadesTercerosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //LIMPIAR NUEVO REGISTRO NOVEDAD
   public void limpiarNuevaNovedad() {
      nuevaNovedad = new Novedades();
      nuevaNovedad.setValortotal(valor);
      nuevaNovedad.setPeriodicidad(new Periodicidades());
      nuevaNovedad.getPeriodicidad().setNombre(" ");
      nuevaNovedad.setTercero(new Terceros());
      nuevaNovedad.getTercero().setNombre(" ");
      nuevaNovedad.setEmpleado(new Empleados());
      nuevaNovedad.setConcepto(new Conceptos());
      nuevaNovedad.getConcepto().setDescripcion(" ");
      nuevaNovedad.setFormula(new Formulas());
      nuevaNovedad.setTipo("FIJA");
      nuevaNovedad.setUsuarioreporta(new Usuarios());
      nuevaNovedad.setTerminal(" ");
      nuevaNovedad.setFechareporte(new Date());
      resultado = 0;
   }

   public void actualizarFormulas() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("actualizarFormulas().seleccionFormulas: " + seleccionFormulas);
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setFormula(seleccionFormulas);
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
         RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
      } else if (tipoActualizacion == 1) {
         log.info("SeleccionPeriocidades: " + seleccionPeriodicidades);
         nuevaNovedad.setFormula(seleccionFormulas);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setFormula(seleccionFormulas);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslistaFormulas = null;
      seleccionFormulas = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formLovFormulas:LOVFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('formulasDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovFormulas:LOVFormulas");
      RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
      RequestContext.getCurrentInstance().update("formLovFormulas:aceptarF");
   }

   public void actualizarPeriodicidades() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("actualizarPeriodicidades().SeleccionPeriocidades: " + seleccionPeriodicidades);
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setPeriodicidad(seleccionPeriodicidades);
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
         RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
      } else if (tipoActualizacion == 1) {
         log.info("SeleccionPeriocidades: " + seleccionPeriodicidades);
         nuevaNovedad.setPeriodicidad(seleccionPeriodicidades);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setPeriodicidad(seleccionPeriodicidades);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslistaPeriodicidades = null;
      seleccionPeriodicidades = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formLovPeriodicidad:LOVPeriodicidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovPeriodicidad:LOVPeriodicidades");
      RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
      RequestContext.getCurrentInstance().update("formLovPeriodicidad:aceptarP");
   }

   public void actualizarConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      Formulas formula;
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setConcepto(seleccionConceptos);
         formula = verificarFormulaConcepto(seleccionConceptos.getSecuencia());
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
         RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");

      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setConcepto(seleccionConceptos);
         formula = verificarFormulaConcepto(seleccionConceptos.getSecuencia());
         nuevaNovedad.setFormula(formula);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");

      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setConcepto(seleccionConceptos);
         formula = verificarFormulaConcepto(seleccionConceptos.getSecuencia());
         duplicarNovedad.setFormula(formula);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslistaConceptos = null;
      seleccionConceptos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formlovConceptos:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formlovConceptos:LOVConceptos");
      RequestContext.getCurrentInstance().update("formlovConceptos:conceptosDialogo");
      RequestContext.getCurrentInstance().update("formlovConceptos:aceptarC");
   }

   public Formulas verificarFormulaConcepto(BigInteger secCon) {
      cargarLovFormulas();
      List<FormulasConceptos> formulasConceptoSel = administrarFormulaConcepto.cargarFormulasConcepto(secCon);
      Formulas formulaR = new Formulas();
      BigInteger autoFormula;

      if (formulasConceptoSel != null) {
         if (!formulasConceptoSel.isEmpty()) {
            autoFormula = formulasConceptoSel.get(0).getFormula();
         } else {
            autoFormula = new BigInteger("4621544");
         }
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

   public void cancelarCambioEmpleados() {
      filtradoslistaEmpleados = null;
      seleccionEmpleados = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formLovEmpleados:aceptarE");
   }

   public void cancelarCambioPeriodicidades() {
      filtradoslistaPeriodicidades = null;
      seleccionPeriodicidades = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovPeriodicidad:LOVPeriodicidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovPeriodicidad:LOVPeriodicidades");
      RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
      RequestContext.getCurrentInstance().update("formLovPeriodicidad:aceptarP");
   }

   public void cancelarCambioFormulas() {
      filtradoslistaFormulas = null;
      seleccionFormulas = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovFormulas:LOVFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('formulasDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovFormulas:LOVFormulas");
      RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
      RequestContext.getCurrentInstance().update("formLovFormulas:aceptarF");
   }

   public void cancelarCambioConceptos() {
      filtradoslistaConceptos = null;
      seleccionConceptos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formlovConceptos:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formlovConceptos:LOVConceptos");
      RequestContext.getCurrentInstance().update("formlovConceptos:conceptosDialogo");
      RequestContext.getCurrentInstance().update("formlovConceptos:aceptarC");
   }

   public void cancelarCambioTerceros() {
      filtradolovTerceros = null;
      terceroSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovTerceros:LOVTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovTerceros:LOVTerceros");
      RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
      RequestContext.getCurrentInstance().update("formLovTerceros:aceptarT");
   }

   public void cancelarCambioTercerosNovedad() {
      filtradosListaTercerosNovedad = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
   }

   public void actualizarTercerosNovedad() {
      RequestContext context = RequestContext.getCurrentInstance();
      //Terceros t = seleccionTerceros;
      listaTercerosNovedad.clear();
      listaNovedades = null;
      listaTercerosNovedad.add(terceroSeleccionadoLOV);
      terceroSeleccionado = listaTercerosNovedad.get(0);
      contarRegistrosTerc();
      getListaNovedades();
      contarRegistrosNove();
      //seleccionMostrar = listaTercerosNovedad.get(0);
      /*
         * else { listaTercerosNovedad.add(seleccionTerceros); }
       */
      listaNovedades = null;
      context.reset("formLovTerceros:LOVTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovTerceros:LOVTerceros");
      RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
      RequestContext.getCurrentInstance().update("formLovTerceros:aceptarT");
      RequestContext.getCurrentInstance().update("form:datosTerceros");
      RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
      filtradosListaTercerosNovedad = null;
      //seleccionTerceros = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("EMPLEADO")) {
         if (tipoNuevo == 1) {
            CodigoEmpleado = nuevaNovedad.getEmpleado().getCodigoempleadoSTR();
         } else if (tipoNuevo == 2) {
            CodigoEmpleado = duplicarNovedad.getEmpleado().getCodigoempleadoSTR();
         }
      } else if (Campo.equals("CODIGO")) {
         if (tipoNuevo == 1) {
            CodigoPeriodicidad = nuevaNovedad.getPeriodicidad().getCodigoStr();
         } else if (tipoNuevo == 2) {
            CodigoPeriodicidad = duplicarNovedad.getPeriodicidad().getCodigoStr();
         }
      } else if (Campo.equals("CONCEPTO")) {
         if (tipoNuevo == 1) {
            CodigoConcepto = nuevaNovedad.getConcepto().getCodigoSTR();
         } else if (tipoNuevo == 2) {
            CodigoConcepto = duplicarNovedad.getConcepto().getCodigoSTR();
         }
      } else if (Campo.equals("FORMULAS")) {
         if (tipoNuevo == 1) {
            Formula = nuevaNovedad.getFormula().getNombrelargo();
         } else if (tipoNuevo == 2) {
            Formula = duplicarNovedad.getFormula().getNombrelargo();
         }
      }
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         cargarLovFormulas();
         contarRegistrosLovFormulas();
         if (tipoNuevo == 1) {
            nuevaNovedad.getFormula().setNombrelargo(Formula);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getFormula().setNombrelargo(Formula);
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
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
            RequestContext.getCurrentInstance().update("form:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         cargarlovConceptos();
         contarRegistrosLovConceptos();
         if (tipoNuevo == 1) {
            nuevaNovedad.getConcepto().setCodigoSTR(CodigoConcepto);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getConcepto().setCodigoSTR(CodigoConcepto);
         }

         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getCodigoSTR().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setConcepto(lovConceptos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConceptoCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConceptoDescripcion");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setConcepto(lovConceptos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConceptoCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConceptoDescripcion");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConceptoCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConceptoDescripcion");

            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConceptoCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConceptoDescripcion");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("CODIGO")) {
         cargarLovPeriodicidades();
         contarRegistrosLovPeriod();
         log.info("seleccionPeriodicidades: " + seleccionPeriodicidades);
         if (tipoNuevo == 1) {
            nuevaNovedad.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
         }

         for (int i = 0; i < lovPeriodicidades.size(); i++) {
            if (lovPeriodicidades.get(i).getCodigoStr().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadDescripcion");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadDescripcion");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadDescripcion");

            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadDescripcion");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {
         cargarLovEmpleados();
         contarRegistrosLovEmpl();
         if (tipoNuevo == 1) {
            nuevaNovedad.getEmpleado().setCodigoempleadoSTR(CodigoEmpleado);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getEmpleado().setCodigoempleadoSTR(CodigoEmpleado);
         }

         for (int i = 0; i < lovEmpleados.size(); i++) {
            if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleadoCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleadoNombre");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleadoCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleadoNombre");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleadoCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleadoNombre");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleadoNombre");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleadoCodigo");
            }
         }
      }
   }

   //CANCELAR MODIFICACIONES
   public void cancelarModificacion() {
      if (bandera == 1) {
         cerrarFiltrado();
      }
      mostrarTodos();
      terceroSeleccionado = null;
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      novedadSeleccionada = null;
      listaNovedades = null;
      guardado = true;
      permitirIndex = true;
      resultado = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         cerrarFiltrado();
      }
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      terceroSeleccionado = null;
      listaNovedades = null;
      novedadSeleccionada = null;
      resultado = 0;
      guardado = true;
      permitirIndex = true;
      navegar("atras");
   }

   public void todasNovedades() {
      listaNovedades.clear();
      listaNovedades = administrarNovedadesTerceros.todasNovedadesTercero(terceroSeleccionado.getSecuencia());
      todas = true;
      actuales = false;
      RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
      RequestContext.getCurrentInstance().update("form:TODAS");
      RequestContext.getCurrentInstance().update("form:ACTUALES");
      contarRegistrosNove();
   }

   public void actualesNovedades() {
      listaNovedades.clear();
      listaNovedades = administrarNovedadesTerceros.novedadesTercero(terceroSeleccionado.getSecuencia());
      todas = false;
      actuales = true;
      RequestContext.getCurrentInstance().update("form:datosNovedadesTercero");
      RequestContext.getCurrentInstance().update("form:TODAS");
      RequestContext.getCurrentInstance().update("form:ACTUALES");
      contarRegistrosNove();
   }

   public void contarRegistrosTerc() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroTerceros");
   }

   public void contarRegistrosNove() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroNovedades");
   }

   public void contarRegistrosLovEmpl() {
      RequestContext.getCurrentInstance().update("formLovEmpleados:informacionRegistroEmpleados");
   }

   public void contarRegistrosLovTerceros() {
      RequestContext.getCurrentInstance().update("formLovTerceros:informacionRegistroLovTerce");
   }

   public void contarRegistrosLovPeriod() {
      RequestContext.getCurrentInstance().update("formLovPeriodicidad:informacionRegistroPeriod");
   }

   public void contarRegistrosLovConceptos() {
      RequestContext.getCurrentInstance().update("formlovConceptos:informacionRegistroConceptos");
   }

   public void contarRegistrosLovFormulas() {
      RequestContext.getCurrentInstance().update("formLovFormulas:informacionRegistroFormulas");
   }

   public void cargarLovPeriodicidades() {
      if (lovPeriodicidades == null) {
         lovPeriodicidades = administrarNovedadesTerceros.lovPeriodicidades();
      }
   }

   public void cargarLovFormulas() {
      if (lovFormulas == null) {
         lovFormulas = administrarNovedadesTerceros.lovFormulas();
      }
   }

   public void cargarlovConceptos() {
      if (lovConceptos == null) {
         lovConceptos = administrarNovedadesTerceros.lovConceptos();
      }
   }

   public void cargarLovEmpleados() {
      if (lovEmpleados == null) {
         lovEmpleados = administrarNovedadesTerceros.lovEmpleados();
      }
   }
   //GETTER & SETTER

   public List<Terceros> getListaTercerosNovedad() {

      if (listaTercerosNovedad == null) {
         listaTercerosNovedad = administrarNovedadesTerceros.lovTerceros();
         if (listaTercerosNovedad != null) {
            lovTerceros = new ArrayList<Terceros>();
            for (int i = 0; i < listaTercerosNovedad.size(); i++) {
               lovTerceros.add(listaTercerosNovedad.get(i));
            }
         }
      }
      return listaTercerosNovedad;
   }

   public void setListaTercerosNovedad(List<Terceros> listaTercerosNovedad) {
      this.listaTercerosNovedad = listaTercerosNovedad;
   }

   public List<Periodicidades> getLovPeriodicidades() {
      return lovPeriodicidades;
   }

   public void setLovPeriodicidades(List<Periodicidades> listaPeriodicidades) {
      this.lovPeriodicidades = listaPeriodicidades;
   }

   public List<Periodicidades> getFiltradoslistaPeriodicidades() {
      return filtradoslistaPeriodicidades;
   }

   public void setFiltradoslistaPeriodicidades(List<Periodicidades> filtradoslistaPeriodicidades) {
      this.filtradoslistaPeriodicidades = filtradoslistaPeriodicidades;
   }

   public Periodicidades getSeleccionPeriodicidades() {
      return seleccionPeriodicidades;
   }

   public void setSeleccionPeriodicidades(Periodicidades seleccionPeriodicidades) {
      this.seleccionPeriodicidades = seleccionPeriodicidades;
   }

   public List<Novedades> getListaNovedades() {
      if (listaNovedades == null && terceroSeleccionado != null) {
         listaNovedades = administrarNovedadesTerceros.novedadesTercero(terceroSeleccionado.getSecuencia());
      }
      return listaNovedades;
   }

   public void setListaNovedades(List<Novedades> listaNovedades) {
      this.listaNovedades = listaNovedades;
   }

   public List<Novedades> getFiltradosListaNovedades() {
      return filtradosListaNovedades;
   }

   public void setFiltradosListaNovedades(List<Novedades> filtradosListaNovedades) {
      this.filtradosListaNovedades = filtradosListaNovedades;
   }

   public Terceros getTerceroSeleccionado() {
      return terceroSeleccionado;
   }

   public void setTerceroSeleccionado(Terceros seleccionMostrar) {
      this.terceroSeleccionado = seleccionMostrar;
   }

   public List<Terceros> getFiltradosListaTercerosNovedad() {
      return filtradosListaTercerosNovedad;
   }

   public void setFiltradosListaTercerosNovedad(List<Terceros> filtradosListaTercerosNovedad) {
      this.filtradosListaTercerosNovedad = filtradosListaTercerosNovedad;
   }

   public List<Terceros> getLovTerceros() {
      return lovTerceros;
   }

   public void setLovTerceros(List<Terceros> listaTerceros) {
      this.lovTerceros = listaTerceros;
   }

   public List<Terceros> getFiltradolovTerceros() {
      return filtradolovTerceros;
   }

   public void setFiltradolovTerceros(List<Terceros> filtradoslistaTerceros) {
      this.filtradolovTerceros = filtradoslistaTerceros;
   }

   public Terceros getSeleccionTerceros() {
      return terceroSeleccionadoLOV;
   }

   public void setSeleccionTerceros(Terceros seleccionTerceros) {
      this.terceroSeleccionadoLOV = seleccionTerceros;
   }

   public List<Formulas> getLovFormulas() {
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> listaFormulas) {

      this.lovFormulas = listaFormulas;
   }

   public List<Formulas> getFiltradoslistaFormulas() {
      return filtradoslistaFormulas;
   }

   public void setFiltradoslistaFormulas(List<Formulas> filtradoslistaFormulas) {
      this.filtradoslistaFormulas = filtradoslistaFormulas;
   }

   public Formulas getSeleccionFormulas() {
      return seleccionFormulas;
   }

   public void setSeleccionFormulas(Formulas seleccionFormulas) {
      this.seleccionFormulas = seleccionFormulas;
   }

   public List<Conceptos> getLovConceptos() {
      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> listaConceptos) {
      this.lovConceptos = listaConceptos;
   }

   public List<Conceptos> getFiltradoslistaConceptos() {
      return filtradoslistaConceptos;
   }

   public void setFiltradoslistaConceptos(List<Conceptos> filtradoslistaConceptos) {
      this.filtradoslistaConceptos = filtradoslistaConceptos;
   }

   public Conceptos getSeleccionConceptos() {
      return seleccionConceptos;
   }

   public void setSeleccionConceptos(Conceptos seleccionConceptos) {
      this.seleccionConceptos = seleccionConceptos;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public int getResultado() {
      if (!listaNovedadesBorrar.isEmpty()) {
         for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
            resultado = administrarNovedadesTerceros.solucionesFormulas(listaNovedadesBorrar.get(i).getSecuencia());
         }
      }
      return resultado;
   }

   public void setResultado(int resultado) {
      this.resultado = resultado;
   }
//Terminal y Usuario{

   public String getAlias() {
      alias = administrarNovedadesTerceros.alias();
      log.info("Alias: " + alias);
      return alias;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   public Usuarios getUsuarioBD() {
      getAlias();
      usuarioBD = administrarNovedadesTerceros.usuarioBD(alias);
      return usuarioBD;

   }

   public void setUsuarioBD(Usuarios usuarioBD) {
      this.usuarioBD = usuarioBD;
   }

   public Novedades getNuevaNovedad() {
      return nuevaNovedad;
   }

   public void setNuevaNovedad(Novedades nuevaNovedad) {
      this.nuevaNovedad = nuevaNovedad;
   }

   public boolean isTodas() {
      return todas;
   }

   public boolean isActuales() {
      return actuales;
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

   public List<Empleados> getLovEmpleados() {
      return lovEmpleados;
   }

   public void setListaEmpleados(List<Empleados> listaEmpleados) {
      this.lovEmpleados = listaEmpleados;
   }

   public List<Empleados> getFiltradoslistaEmpleados() {
      return filtradoslistaEmpleados;
   }

   public void setFiltradoslistaEmpleados(List<Empleados> filtradoslistaEmpleados) {
      this.filtradoslistaEmpleados = filtradoslistaEmpleados;
   }

   public Empleados getSeleccionEmpleados() {
      return seleccionEmpleados;
   }

   public void setSeleccionEmpleados(Empleados seleccionEmpleados) {
      this.seleccionEmpleados = seleccionEmpleados;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public Novedades getNovedadSeleccionada() {
      return novedadSeleccionada;
   }

   public void setNovedadSeleccionada(Novedades novedadSeleccionada) {
      this.novedadSeleccionada = novedadSeleccionada;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getAltoTablaReg() {
      if (altoTabla.equals("125")) {
         altoTablaReg = "5";
      } else {
         altoTablaReg = "6";
      }
      return altoTablaReg;
   }

   public void setAltoTablaReg(String altoTablaReg) {
      this.altoTablaReg = altoTablaReg;
   }

   public String getAltoTablaRegTer() {
      if (altoTabla.equals("125")) {
         altoTablaRegTer = "3";
      } else {
         altoTablaRegTer = "4";
      }
      return altoTablaRegTer;
   }

   public void setAltoTablaRegTer(String altoTablaRegTer) {
      this.altoTablaRegTer = altoTablaRegTer;
   }

   public String getAltoTablaTerc() {
      if (altoTabla.equals("125")) {
         altoTablaTerc = "56";
      } else {
         altoTablaTerc = "74";
      }
      return altoTablaTerc;
   }

   public void setAltoTablaTerc(String altoTablaTerc) {
      this.altoTablaTerc = altoTablaTerc;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistroConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovConceptos:LOVConceptos");
      if (filtradoslistaConceptos != null) {
         if (filtradoslistaConceptos.size() == 1) {
            seleccionConceptos = filtradoslistaConceptos.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVConceptos').unselectAllRows();PF('LOVConceptos').selectRow(0);");
         } else {
            seleccionConceptos = null;
            RequestContext.getCurrentInstance().execute("PF('LOVConceptos').unselectAllRows();");
         }
      } else {
         seleccionConceptos = null;
         aceptar = true;
      }
      infoRegistroConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroConceptos;
   }

   public String getInfoRegistroEmpleados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovEmpleados:LOVEmpleados");
      if (filtradoslistaEmpleados != null) {
         if (filtradoslistaEmpleados.size() == 1) {
            seleccionEmpleados = filtradoslistaEmpleados.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();PF('LOVEmpleados').selectRow(0);");
         } else {
            seleccionEmpleados = null;
            RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();");
         }
      } else {
         seleccionEmpleados = null;
         aceptar = true;
      }
      infoRegistroEmpleados = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleados;
   }

   public String getInfoRegistroFormulas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovFormulas:LOVFormulas");
      if (filtradoslistaFormulas != null) {
         if (filtradoslistaFormulas.size() == 1) {
            seleccionFormulas = filtradoslistaFormulas.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVFormulas').unselectAllRows();PF('LOVFormulas').selectRow(0);");
         } else {
            seleccionFormulas = null;
            RequestContext.getCurrentInstance().execute("PF('LOVFormulas').unselectAllRows();");
         }
      } else {
         seleccionFormulas = null;
         aceptar = true;
      }
      infoRegistroFormulas = String.valueOf(tabla.getRowCount());
      return infoRegistroFormulas;
   }

   public String getInfoRegistroLovTerceros() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovTerceros:LOVTerceros");
      if (filtradolovTerceros != null) {
         if (filtradolovTerceros.size() == 1) {
            terceroSeleccionadoLOV = filtradolovTerceros.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVTerceros').unselectAllRows();PF('LOVTerceros').selectRow(0);");
         } else {
            terceroSeleccionadoLOV = null;
            RequestContext.getCurrentInstance().execute("PF('LOVTerceros').unselectAllRows();");
         }
      } else {
         terceroSeleccionado = null;
         aceptar = true;
      }
      infoRegistroLovTerceros = String.valueOf(tabla.getRowCount());
      return infoRegistroLovTerceros;
   }

   public String getInfoRegistroNovedades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNovedadesTercero");
      infoRegistroNovedades = String.valueOf(tabla.getRowCount());
      return infoRegistroNovedades;
   }

   public String getInfoRegistroPeriodi() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovPeriodicidad:LOVPeriodicidades");
      if (filtradoslistaPeriodicidades != null) {
         if (filtradoslistaPeriodicidades.size() == 1) {
            seleccionPeriodicidades = filtradoslistaPeriodicidades.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').unselectAllRows();PF('LOVPeriodicidades').selectRow(0);");
         } else {
            seleccionPeriodicidades = null;
            RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').unselectAllRows();");
         }
      } else {
         terceroSeleccionado = null;
         aceptar = true;
      }
      infoRegistroPeriodi = String.valueOf(tabla.getRowCount());
      return infoRegistroPeriodi;
   }

   public String getInfoRegistroTerceros() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTerceros");
      infoRegistroTerceros = String.valueOf(tabla.getRowCount());
      return infoRegistroTerceros;
   }

   public void setInfoRegistroConceptos(String infoRegistroCopnceptos) {
      this.infoRegistroConceptos = infoRegistroCopnceptos;
   }

   public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
      this.infoRegistroEmpleados = infoRegistroEmpleados;
   }

   public void setInfoRegistroFormulas(String infoRegistroFormulas) {
      this.infoRegistroFormulas = infoRegistroFormulas;
   }

   public void setInfoRegistroLovTerceros(String infoRegistroLovTerceros) {
      this.infoRegistroLovTerceros = infoRegistroLovTerceros;
   }

   public void setInfoRegistroNovedades(String infoRegistroNovedades) {
      this.infoRegistroNovedades = infoRegistroNovedades;
   }

   public void setInfoRegistroPeriodi(String infoRegistroPeriodi) {
      this.infoRegistroPeriodi = infoRegistroPeriodi;
   }

   public void setInfoRegistroTerceros(String infoRegistroTerceros) {
      this.infoRegistroTerceros = infoRegistroTerceros;
   }
}
