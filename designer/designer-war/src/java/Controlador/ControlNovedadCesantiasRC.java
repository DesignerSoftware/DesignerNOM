/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Administrar.AdministrarNovedadCesantiasRC;
import Entidades.Empleados;
import Entidades.MotivosCesantias;
import Entidades.NovedadesSistema;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNovedadesSistemaInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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
public class ControlNovedadCesantiasRC implements Serializable {

   @EJB
   AdministrarNovedadCesantiasRC administrarNovedadesPagoCesantias;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministrarNovedadesSistemaInterface administrarNovedadesSistema;
   //SECUENCIA DE LA PERSONA
   //Lista Empleados Novedad Pago Parcial Cesantías
   private List<Empleados> listaEmpleadosNovedad;
   private List<Empleados> filtrarListaEmpleados;
   private Empleados empleadoSeleccionado;
   //Lista Novedades Pago Parcial Cesantias
   private List<NovedadesSistema> listaNovedades;
   private List<NovedadesSistema> filtrarListaNovedades;
   private NovedadesSistema novedadSeleccionada;
   //LOV EMPLEADOS
   private List<Empleados> lovEmpleados;
   private List<Empleados> lovEmpleadosFiltrar;
   private Empleados empleadoSeleccionadoLOV;
   //LISTA CESANTIAS NO LIQUIDADAS
   private List<Empleados> listaaux;
   //LOV MOTIVO CESANTIAS
   private List<MotivosCesantias> lovMotivosCesantia;
   private List<MotivosCesantias> filtrarLovMotivosCesantias;
   private MotivosCesantias motivoCesantiaLovSeleccionado;
   //Duplicar
   private NovedadesSistema duplicarNovedad;
   //editar celda
   private NovedadesSistema editarNovedades;
   private int cualCelda, tipoLista, tipoListaNov;
   //Crear Novedades
   private List<NovedadesSistema> listaNovedadesCrear;
   private NovedadesSistema nuevaNovedad;
   //Modificar Novedades
   private List<NovedadesSistema> listaNovedadesModificar;
   //Borrar Novedades
   private List<NovedadesSistema> listaNovedadesBorrar;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   private boolean permitirIndex;
   //RASTROS
   private boolean guardado;
   //COLUMNAS TABLA NOVEDADES
   private Column nEEmpleadosNombres, nEEmpleadosCodigos;
   private Column fechaCorteCesantias, fechaCorteIntCesantias, valorCesantias, valorIntCesantias, valorSolicitado, motivoCesantia, observacion, beneficiarios;
   private String motivoCesantias;
   private int altotabla, altotablaReg;
   //variable para activar boton mostrar todos
   private boolean activarMTodos;
   private String infoRegistroEmpleados, infoRegistroEmpleadosLov, infoRegistroMotivosCesantias, infoRegistroNovedadCesantias;
   private int paraNuevaNovedad;
   private String mensajeValidacion;
   private int k;
   private String observaciones, beneficiario, motivo;
   //LISTA QUE TRAE EL VALOR DE LA CESANTIAS
   private List<BigDecimal> listavalorcesantias;
   private List<BigDecimal> listafiltrarvalorcesantias;
   private BigDecimal valorcesantiaSeleccionado;
   //LISTA QUE TRAE EL VALOR DE LOS INTERESES DE CESANTIAS
   private List<BigDecimal> listavalorintcesantias;
   private List<BigDecimal> listafiltrarvalorintcesantias;
   private BigDecimal valorintcesantiaSeleccionado;
   private BigDecimal provisionlov, provisioncesantias;
   private boolean todas, actuales, activarLov;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlNovedadCesantiasRC() {
      listaEmpleadosNovedad = null;
      listaNovedadesBorrar = new ArrayList<NovedadesSistema>();
      listaNovedadesCrear = new ArrayList<NovedadesSistema>();
      listaNovedadesModificar = new ArrayList<NovedadesSistema>();
      aceptar = true;
      guardado = true;
      tipoLista = 0;
      tipoListaNov = 0;
      lovEmpleados = null;
      lovMotivosCesantia = null;
      permitirIndex = true;
      altotabla = 120;
      altotablaReg = 6;
      paginaAnterior = "nominaf";
      empleadoSeleccionado = null;
      novedadSeleccionada = null;
      nuevaNovedad = new NovedadesSistema();
      nuevaNovedad.setEstado("ABIERTO");
      nuevaNovedad.setDias(BigInteger.valueOf(0));
      nuevaNovedad.setPagarporfuera("S");
      nuevaNovedad.setTipo("CESANTIA");
      nuevaNovedad.setSubtipo("DINERO");
      nuevaNovedad.setFechasistema(new Date());
      nuevaNovedad.setMotivocesantia(new MotivosCesantias());
      duplicarNovedad = new NovedadesSistema();
      duplicarNovedad.setFechainicialdisfrute(new Date());
      duplicarNovedad.setEstado("ABIERTO");
      duplicarNovedad.setDias(BigInteger.valueOf(0));
      duplicarNovedad.setPagarporfuera("S");
      duplicarNovedad.setTipo("CESANTIA");
      duplicarNovedad.setSubtipo("DINERO");
      duplicarNovedad.setFechasistema(new Date());
      duplicarNovedad.setMotivocesantia(new MotivosCesantias());
      provisionlov = null;
      provisioncesantias = null;
      actuales = false;
      activarLov = true;
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
      String pagActual = "novedadcesantiasrc";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);
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
      lovMotivosCesantia = null;
      lovEmpleados = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarNovedadesPagoCesantias.obtenerConexion(ses.getId());
         administrarNovedadesSistema.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         getListaEmpleadosNovedad();
         if (listaEmpleadosNovedad != null) {
            if (!listaEmpleadosNovedad.isEmpty()) {
               empleadoSeleccionado = listaEmpleadosNovedad.get(0);
            }
         }
         listaNovedades = null;
         getListaNovedades();
         cambiarEmpleado();
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPag(String pag) {
      paginaAnterior = pag;
      listaNovedades = null;
      getListaNovedades();
      if (listaEmpleadosNovedad != null) {
         if (!listaEmpleadosNovedad.isEmpty()) {
            empleadoSeleccionado = listaEmpleadosNovedad.get(0);
         }
      }
   }

   public String volverPagAnterior() {
      return paginaAnterior;
   }

   public void editarCelda() {
      if (novedadSeleccionada != null) {
         editarNovedades = novedadSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaCorteCesantias");
            RequestContext.getCurrentInstance().execute("PF('editFechaCorteCesantias').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaCorteIntCesantias");
            RequestContext.getCurrentInstance().execute("PF('editFechaCorteIntCesantias').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editValorCesantias");
            RequestContext.getCurrentInstance().execute("PF('editValorCesantias').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editValorIntCesantias");
            RequestContext.getCurrentInstance().execute("PF('editValorIntCesantias').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editValorSolicitado");
            RequestContext.getCurrentInstance().execute("PF('editValorSolicitado').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editMotivoCesantias");
            RequestContext.getCurrentInstance().execute("PF('editMotivoCesantias').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editObservaciones");
            RequestContext.getCurrentInstance().execute("PF('editObservaciones').show()");
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editBeneficiarios");
            RequestContext.getCurrentInstance().execute("PF('editBeneficiarios').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void limpiarNuevaNovedad() {
      nuevaNovedad = new NovedadesSistema();
      nuevaNovedad.setEstado("ABIERTO");
      nuevaNovedad.setDias(BigInteger.valueOf(0));
      nuevaNovedad.setPagarporfuera("S");
      nuevaNovedad.setTipo("CESANTIA");
      nuevaNovedad.setSubtipo("DINERO");
      nuevaNovedad.setFechasistema(new Date());
      provisioncesantias = null;
      provisionlov = null;

   }

   public void limpiarDuplicarNovedad() {
      duplicarNovedad = new NovedadesSistema();
      duplicarNovedad.setEstado("ABIERTO");
      duplicarNovedad.setDias(BigInteger.valueOf(0));
      duplicarNovedad.setPagarporfuera("S");
      duplicarNovedad.setTipo("CESANTIA");
      duplicarNovedad.setSubtipo("DINERO");
      duplicarNovedad.setFechasistema(new Date());
      provisioncesantias = null;
      provisionlov = null;
   }

   public void empleadosCesantiasNoLiquidadas() {
      if (!listaEmpleadosNovedad.isEmpty()) {
         listaEmpleadosNovedad.clear();
      }
      listaaux = administrarNovedadesPagoCesantias.empleadoscesantiasnoliquidados();
      System.out.println("lista aux :" + listaaux);
      if (listaaux != null) {
         for (int i = 0; i < listaaux.size(); i++) {
            listaEmpleadosNovedad.add(listaaux.get(i));
         }
      }
      contarRegistroEmpleados();
      actuales = false;
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      RequestContext.getCurrentInstance().update("form:NOLIQUIDADAS");
   }

   public void modificarNovedadCesantias(NovedadesSistema novedadS) {
      novedadSeleccionada = novedadS;
      if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
         if (listaNovedadesModificar.isEmpty()) {
            listaNovedadesModificar.add(novedadSeleccionada);
         } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
            listaNovedadesModificar.add(novedadSeleccionada);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
   }

   public void posicionOtro() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      novedadSeleccionada = listaNovedades.get(indice);
      cambiarIndice(novedadSeleccionada, columna);
   }

   public void agregarNuevaNovedadPagoParcialCesantias() {
      int pasa = 0;
      mensajeValidacion = new String();
      if (nuevaNovedad.getFechainicialdisfrute() == null) {
         System.out.println("Entro a Fecha Inicial Disfrute");
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial Disfrute\n";
         pasa++;
      }
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadEmpleado");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadEmpleado').show()");
      }
      if (pasa == 0) {
         if (bandera == 1) {
            cargarTablaDefault();
         }
         //AGREGAR REGISTRO A LA LISTA NOVEDADES .
         paraNuevaNovedad++;
         nuevaNovedad.setSecuencia(BigInteger.valueOf(paraNuevaNovedad));
         nuevaNovedad.setEmpleado(empleadoSeleccionado); //Envia empleado
         System.out.println("Empleado enviado: " + empleadoSeleccionado.getPersona().getNombreCompleto());
         //-------Datos Ingresados    
         System.out.println("empleado : " + empleadoSeleccionado);
         System.out.println("fecha cesantias" + nuevaNovedad.getFechacortecesantia());
         System.out.println("valor cesantias " + nuevaNovedad.getValorcesantia());
         System.out.println("valor intereses cesantias " + nuevaNovedad.getValorinterescesantia());
         System.out.println("valor solicitado " + nuevaNovedad.getValorsolicitado());
         System.out.println("observaciones " + nuevaNovedad.getObservaciones());
         System.out.println("beneficiario " + nuevaNovedad.getBeneficiario());
         listaNovedadesCrear.add(nuevaNovedad);
         listaNovedades.add(nuevaNovedad);
         nuevaNovedad.setObservaciones(nuevaNovedad.getObservaciones());
         nuevaNovedad.setBeneficiario(nuevaNovedad.getBeneficiario());
         System.out.println("agregarNuevaNovedadPagoParcialCesantias.observacion : " + nuevaNovedad.getObservaciones());
         System.out.println("agregarNuevaNovedadPagoParcialCesantias.Beneficiario : " + nuevaNovedad.getBeneficiario());
         novedadSeleccionada = nuevaNovedad;
         contarRegistroMotivosNovedades();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         nuevaNovedad = new NovedadesSistema();
         nuevaNovedad.setEstado("ABIERTO");
         nuevaNovedad.setDias(BigInteger.valueOf(0));
         nuevaNovedad.setPagarporfuera("S");
         nuevaNovedad.setTipo("CESANTIA");
         nuevaNovedad.setSubtipo("DINERO");
         nuevaNovedad.setFechasistema(new Date());
         RequestContext.getCurrentInstance().execute("PF('nuevanovedadpagoparcialcesantias').hide()");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      }
   }

   public void duplicarNV() {
      if (novedadSeleccionada != null) {
         duplicarNovedad = new NovedadesSistema();
         paraNuevaNovedad++;

         duplicarNovedad.setSecuencia(BigInteger.valueOf(paraNuevaNovedad));
         duplicarNovedad.setEmpleado(novedadSeleccionada.getEmpleado());
         duplicarNovedad.setFechainicialdisfrute(novedadSeleccionada.getFechainicialdisfrute());
         duplicarNovedad.setFechacortecesantia(novedadSeleccionada.getFechacortecesantia());
         duplicarNovedad.setValorcesantia(novedadSeleccionada.getValorcesantia());
         duplicarNovedad.setValorinterescesantia(novedadSeleccionada.getValorinterescesantia());
         duplicarNovedad.setValorsolicitado(novedadSeleccionada.getValorsolicitado());
         duplicarNovedad.setSubtipo(novedadSeleccionada.getSubtipo());
         duplicarNovedad.setMotivocesantia(novedadSeleccionada.getMotivocesantia());
         duplicarNovedad.setObservaciones(novedadSeleccionada.getObservaciones());
         duplicarNovedad.setBeneficiario(novedadSeleccionada.getBeneficiario());
         duplicarNovedad.setDias(BigInteger.valueOf(0));

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
         RequestContext.getCurrentInstance().execute("PF('duplicarregistroNovedad').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int pasa = 0;
      if (duplicarNovedad.getFechainicialdisfrute() == null) {
         System.out.println("Entro a Fecha Inicial");
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (duplicarNovedad.getEmpleado() == null) {
         System.out.println("Entro a Empleado");
         mensajeValidacion = mensajeValidacion + " * Empleado\n";
         pasa++;
      }
      System.out.println("Valor Pasa: " + pasa);
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadEmpleado");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadEmpleado').show()");
      }
      if (pasa == 0) {
         listaNovedadesCrear.add(duplicarNovedad);
         listaNovedades.add(duplicarNovedad);
         novedadSeleccionada = duplicarNovedad;
         contarRegistroMotivosNovedades();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            cargarTablaDefault();
         }
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         duplicarNovedad = new NovedadesSistema();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarregistroNovedad");
         RequestContext.getCurrentInstance().execute("PF('duplicarregistroNovedad').hide()");
      }
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "NovedadPagoParcialCesantiasPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "NovedadPagoParcialCesantiasXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altotabla = 100;
         altotablaReg = 5;
         fechaCorteCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:fechacortecesantias");
         fechaCorteCesantias.setFilterStyle("width: 85% !important");
         fechaCorteIntCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:fechacorteintcesantias");
         fechaCorteIntCesantias.setFilterStyle("");
         valorCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:valorcesantias");
         valorCesantias.setFilterStyle("width: 85% !important");
         valorIntCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:valorintcesantias");
         valorIntCesantias.setFilterStyle("width: 85% !important");
         valorSolicitado = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:valorsolicitado");
         valorSolicitado.setFilterStyle("width: 85% !important");
         motivoCesantia = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:motivocesantia");
         motivoCesantia.setFilterStyle("width: 85% !important");
         observacion = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:observaciones");
         observacion.setFilterStyle("width: 85% !important");
         beneficiarios = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:beneficiarios");
         beneficiarios.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         bandera = 1;

         nEEmpleadosCodigos = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosCodigos");
         nEEmpleadosCodigos.setFilterStyle("width: 80% !important");
         nEEmpleadosNombres = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosNombres");
         nEEmpleadosNombres.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosEmpleados");
      } else if (bandera == 1) {
         cargarTablaDefault();
      }
   }

   public void cargarTablaDefault() {
      FacesContext c = FacesContext.getCurrentInstance();
      altotabla = 120;
      altotablaReg = 6;
      fechaCorteCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:fechacortecesantias");
      fechaCorteCesantias.setFilterStyle("display: none; visibility: hidden;");
      fechaCorteIntCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:fechacorteintcesantias");
      fechaCorteIntCesantias.setFilterStyle("display: none; visibility: hidden;");
      valorCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:valorcesantias");
      valorCesantias.setFilterStyle("display: none; visibility: hidden;");
      valorIntCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:valorintcesantias");
      valorIntCesantias.setFilterStyle("display: none; visibility: hidden;");
      valorSolicitado = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:valorsolicitado");
      valorSolicitado.setFilterStyle("display: none; visibility: hidden;");
      motivoCesantia = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:motivocesantia");
      motivoCesantia.setFilterStyle("display: none; visibility: hidden;");
      observacion = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:observaciones");
      observacion.setFilterStyle("display: none; visibility: hidden;");
      beneficiarios = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:beneficiarios");
      beneficiarios.setFilterStyle("display: none; visibility: hidden;");
      bandera = 0;
      nEEmpleadosCodigos = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosCodigos");
      nEEmpleadosCodigos.setFilterStyle("display: none; visibility: hidden;");
      nEEmpleadosNombres = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosNombres");
      nEEmpleadosNombres.setFilterStyle("display: none; visibility: hidden;");
      filtrarListaNovedades = null;
      RequestContext.getCurrentInstance().execute("PF('datosEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('datosNovedadesEmpleado').clearFilters()");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      tipoLista = 0;
      tipoListaNov = 0;
   }

   public void mostrarTodos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listaEmpleadosNovedad.isEmpty()) {
         listaEmpleadosNovedad.clear();
      }
      if (lovEmpleados != null) {
         for (int i = 0; i < lovEmpleados.size(); i++) {
            listaEmpleadosNovedad.add(lovEmpleados.get(i));
         }
      }

      empleadoSeleccionado = listaEmpleadosNovedad.get(0);
      listaNovedades = administrarNovedadesSistema.cesantiasEmpleado(empleadoSeleccionado.getSecuencia());
      contarRegistrosNovedades();
      filtrarListaNovedades = null;
      aceptar = true;
      novedadSeleccionada = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      activarMTodos = true;
      contarRegistroEmpleados();
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
   }

   public void cambiarIndice(NovedadesSistema novedadS, int celda) {
      if (permitirIndex == true) {
         novedadSeleccionada = novedadS;
         cualCelda = celda;
         novedadSeleccionada.getSecuencia();
         if (cualCelda == 0) {
            deshabilitarBotonLov();
            novedadSeleccionada.getFechainicialdisfrute();
         } else if (cualCelda == 1) {
            deshabilitarBotonLov();
            novedadSeleccionada.getFechacortecesantia();
         } else if (cualCelda == 2) {
            deshabilitarBotonLov();
            novedadSeleccionada.getValorcesantia();
         } else if (cualCelda == 3) {
            deshabilitarBotonLov();
            novedadSeleccionada.getValorinterescesantia();
         } else if (cualCelda == 4) {
            deshabilitarBotonLov();
            novedadSeleccionada.getValorsolicitado();
         } else if (cualCelda == 5) {
            habilitarBotonLov();
            novedadSeleccionada.getMotivocesantia().getNombre();
         } else if (cualCelda == 6) {
            deshabilitarBotonLov();
            novedadSeleccionada.getObservaciones();
         } else if (cualCelda == 7) {
            deshabilitarBotonLov();
            novedadSeleccionada.getBeneficiario();
         }
      }
   }

   public void asignarIndex(NovedadesSistema novedadS, int dlg, int LND) {
      novedadSeleccionada = novedadS;
      tipoActualizacion = LND;
      contarRegistrosNovedades();
      if (dlg == 5) {
         cargarLovMotivos();
         RequestContext.getCurrentInstance().update("formLovMotivosC:motivoscesantiasDialogo");
         RequestContext.getCurrentInstance().execute("PF('motivoscesantiasDialogo').show()");
      }
   }

   public void asignarIndex2(int dlg, int LND) {
      tipoActualizacion = LND;
      contarRegistrosNovedades();
      if (dlg == 5) {
         cargarLovMotivos();
         RequestContext.getCurrentInstance().update("formLovMotivosC:motivoscesantiasDialogo");
         RequestContext.getCurrentInstance().execute("PF('motivoscesantiasDialogo').show()");
      }
   }

   public void autocompletarVlrCesantias() {
      System.out.println("Entró a autocompletarVlrCesantias");
      System.out.println("controlador valor cesantias : " + retornarprovisioncesantias());
      if (retornarprovisioncesantias().toBigInteger().equals(BigInteger.ZERO)) {
         RequestContext.getCurrentInstance().execute("PF('validacionvlrcesantias').show()");
         nuevaNovedad.setValorcesantia(BigInteger.ZERO);
         RequestContext.getCurrentInstance().update("formularioDialogos:vlrcesantias");
      } else if (retornarprovision() != null) {
         nuevaNovedad.setValorcesantia(retornarprovisioncesantias().toBigInteger());
         RequestContext.getCurrentInstance().update("formularioDialogos:vlrcesantias");
      }
   }

   public void autocompletarVlrIntCesantias() {
      System.out.println("Entró a autocompletarVlrIntCesantias");
      System.out.println("VlrIntCesantias del controlador " + retornarprovision());
      if (retornarprovision().toBigInteger().equals(BigInteger.ZERO)) {
         RequestContext.getCurrentInstance().execute("PF('validacionvlrintcesantias').show()");
         nuevaNovedad.setValorinterescesantia(BigInteger.ZERO);
         RequestContext.getCurrentInstance().update("formularioDialogos:vlrintcesantias");
      } else if (retornarprovision() != null) {
         nuevaNovedad.setValorinterescesantia(retornarprovision().toBigInteger());
         RequestContext.getCurrentInstance().update("formularioDialogos:vlrintcesantias");
      }
   }

   public void autocompletarVlrSolicitado() {
      System.out.println("Entró a autocompletarVlrSolicitado");
      if (retornarprovision() != null && retornarprovisioncesantias() != null) {
         nuevaNovedad.setValorsolicitado(sumarvalorsolicitado(nuevaNovedad.getValorcesantia(), nuevaNovedad.getValorinterescesantia()));
         RequestContext.getCurrentInstance().update("formularioDialogos:vlrsolicitado");
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:validacionvlrsolicitado");
      }
   }

   public void autocompletarduplicarVlrSolicitado() {
      System.out.println("Entró a autocompletarduplicarVlrSolicitado");
      duplicarNovedad.setValorsolicitado(sumarvalorsolicitado(duplicarNovedad.getValorcesantia(), duplicarNovedad.getValorinterescesantia()));
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarvlrsolicitado");
   }

   public void autocompletarduplicarVlrIntCesantias() {
      System.out.println("Entró a autocompletarduplicaVlrIntCesantias");
      duplicarNovedad.setValorinterescesantia(retornarprovision().toBigInteger());
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarvlrintcesantias");
   }

   public void autocompletarduplicarVlrCesantias() {
      System.out.println("Entró a autocompletarduplicaVlrCesantias");
      duplicarNovedad.setValorcesantia(retornarprovisioncesantias().toBigInteger());
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarvlrcesantias");
   }

   public void cambiarEmpleado() {
      if (listaNovedadesCrear.isEmpty() && listaNovedadesBorrar.isEmpty() && listaNovedadesModificar.isEmpty()) {
         //Se recargan las novedades para el empleado
         novedadSeleccionada = null;
         if (tipoListaNov == 1) {
            RequestContext.getCurrentInstance().execute("PF('datosNovedadesEmpleado').clearFilters()");
         }
         listaNovedades = administrarNovedadesSistema.cesantiasEmpleado(empleadoSeleccionado.getSecuencia());
         contarRegistrosNovedades();
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      }
   }

   public void abrirLista(int listaV) {
      if (listaV == 0) {
         cargarLovEmpleados();
         contarRegistroEmpleadosLov();
         RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
      }
   }

   public void actualizarEmpleadosNovedad() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listaEmpleadosNovedad.isEmpty()) {
         listaEmpleadosNovedad.clear();
         listaEmpleadosNovedad.add(empleadoSeleccionadoLOV);
         empleadoSeleccionado = listaEmpleadosNovedad.get(0);
      }
      listaNovedades = administrarNovedadesSistema.cesantiasEmpleado(empleadoSeleccionado.getSecuencia());
      aceptar = true;
      filtrarListaEmpleados = null;
      novedadSeleccionada = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      activarMTodos = false;
      contarRegistroEmpleados();
      contarRegistrosNovedades();
      RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formLovEmpleados:aceptarE");
      context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
   }

   public void actualizarMotivosCesantias() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setMotivocesantia(motivoCesantiaLovSeleccionado);
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("formularioDialogos:motivoces");
      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setMotivocesantia(motivoCesantiaLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:motivoces");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setMotivocesantia(motivoCesantiaLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarmotivoces");
      }
      filtrarLovMotivosCesantias = null;
      motivoCesantiaLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext.getCurrentInstance().update("formLovMotivosC:motivoscesantiasDialogo");
      RequestContext.getCurrentInstance().update("formLovMotivosC:lovmotivoscesantias");
      RequestContext.getCurrentInstance().update("formLovMotivosC:aceptarP");
      context.reset("formLovMotivosC:lovmotivoscesantias:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovmotivoscesantias').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('motivoscesantiasDialogo').hide()");
   }

   public BigInteger sumarvalorsolicitado(BigInteger valorcesantia, BigInteger valorintcesantia) {
      BigInteger valorsolicitado;
      if (valorcesantia.equals(BigInteger.ZERO) && valorintcesantia.equals(BigInteger.ZERO)) {
         valorsolicitado = BigInteger.ZERO;
      } else {
         valorsolicitado = valorcesantia.add(valorintcesantia);
      }
      System.out.println("El valor solicitado es : " + valorsolicitado);
      return valorsolicitado;
   }

   public void borrarNovedades() {
      if (novedadSeleccionada != null) {
         if (!listaNovedadesModificar.isEmpty() && listaNovedadesModificar.contains(novedadSeleccionada)) {
            int modIndex = listaNovedadesModificar.indexOf(novedadSeleccionada);
            listaNovedadesModificar.remove(modIndex);
            listaNovedadesBorrar.add(novedadSeleccionada);
         } else if (!listaNovedadesCrear.isEmpty() && listaNovedadesCrear.contains(novedadSeleccionada)) {
            int crearIndex = listaNovedadesCrear.indexOf(novedadSeleccionada);
            listaNovedadesCrear.remove(crearIndex);
         } else {
            listaNovedadesBorrar.add(novedadSeleccionada);
         }
         listaNovedades.remove(novedadSeleccionada);
         if (tipoListaNov == 1) {
            filtrarListaNovedades.remove(novedadSeleccionada);
         }
         contarRegistroMotivosNovedades();
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         novedadSeleccionada = null;

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   /////GUARDARCAMBIOSNOVEDADES
   public void guardarCambiosNovedades() {
      try {
         Empleados emp = new Empleados();
         if (guardado == false) {
            System.out.println("Realizando Operaciones Novedades");

            if (!listaNovedadesBorrar.isEmpty()) {
               for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
                  System.out.println("Borrando..." + listaNovedadesBorrar.size());
                  if (listaNovedadesBorrar.get(i).getObservaciones() == null) {
                     listaNovedadesBorrar.get(i).setObservaciones(" ");
                  }
                  if (listaNovedadesBorrar.get(i).getBeneficiario() == null) {
                     listaNovedadesBorrar.get(i).setBeneficiario(null);
                  }
                  administrarNovedadesSistema.borrarNovedades(listaNovedadesBorrar.get(i));
               }
               System.out.println("Entra");
               listaNovedadesBorrar.clear();
            }

            if (!listaNovedadesCrear.isEmpty()) {
               for (int i = 0; i < listaNovedadesCrear.size(); i++) {
                  System.out.println("Creando...");
                  if (listaNovedadesCrear.get(i).getObservaciones() == null) {
                     listaNovedadesCrear.get(i).setObservaciones(" ");
                  }
                  if (listaNovedadesCrear.get(i).getBeneficiario() == null) {
                     listaNovedadesCrear.get(i).setBeneficiario(null);
                  }
                  System.out.println(listaNovedadesCrear.get(i).getTipo());
                  administrarNovedadesSistema.crearNovedades(listaNovedadesCrear.get(i));
               }
               System.out.println("LimpiaLista");
               listaNovedadesCrear.clear();
            }

            if (!listaNovedadesModificar.isEmpty()) {
               for (int i = 0; i < listaNovedadesModificar.size(); i++) {
                  System.out.println(" modificando");
//                        if (listaNovedadesModificar.get(i).getObservaciones() == null) {
//                            listaNovedadesModificar.get(i).setObservaciones(" ");
//                        }
//                        if (listaNovedadesModificar.get(i).getBeneficiario() == null) {
//                            listaNovedadesModificar.get(i).setBeneficiario(null);
//                        }
                  administrarNovedadesSistema.modificarNovedades(listaNovedadesModificar.get(i));
               }
               listaNovedadesModificar.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listaNovedades = null;
            getListaNovedades();
            contarRegistrosNovedades();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            k = 0;
         }
         System.out.println("Valor k: " + k);
      } catch (Exception e) {
         System.out.println("Error guardando datos : " + e.getMessage());
         RequestContext.getCurrentInstance().execute("PF('errorGuardado').show()");
         FacesMessage msg = new FacesMessage("Información", "Error en el guardado, Intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cancelarCambioMotivoCesantias() {
      filtrarLovMotivosCesantias = null;
      motivoCesantiaLovSeleccionado = null;
      aceptar = true;
      provisioncesantias = null;
      provisionlov = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formLovMotivosC:motivoscesantiasDialogo");
      RequestContext.getCurrentInstance().update("formLovMotivosC:lovmotivoscesantias");
      RequestContext.getCurrentInstance().update("formLovMotivosC:aceptarP");
      context.reset("formLovMotivosC:lovmotivoscesantias:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovmotivoscesantias').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('motivoscesantiasDialogo').hide()");

   }

   //////SUMAR VALOR CESANTIA E INT CESANTIAS PARA SACAR EL VALOR SOLICITADO
   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarCambioEmpleados() {
      lovEmpleadosFiltrar = null;
      empleadoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formLovEmpleados:aceptarE");
      context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         cargarTablaDefault();
      }
      if (!listaEmpleadosNovedad.isEmpty()) {
         listaEmpleadosNovedad.clear();
      }
      if (lovEmpleados != null) {
         for (int i = 0; i < lovEmpleados.size(); i++) {
            listaEmpleadosNovedad.add(lovEmpleados.get(i));
         }
      }
      contarRegistrosNovedades();
      aceptar = true;
      guardado = true;
      tipoLista = 0;
      tipoListaNov = 0;
      permitirIndex = true;
      nuevaNovedad = new NovedadesSistema();
      nuevaNovedad.setEstado("ABIERTO");
      nuevaNovedad.setDias(BigInteger.valueOf(0));
      nuevaNovedad.setPagarporfuera("S");
      nuevaNovedad.setTipo("CESANTIA");
      nuevaNovedad.setSubtipo("DINERO");
      nuevaNovedad.setFechasistema(new Date());
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      listaNovedades = null;
      lovEmpleados = null;
      activarMTodos = true;
      empleadoSeleccionado = null;
      novedadSeleccionada = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         cargarTablaDefault();
      }
      listaEmpleadosNovedad = null;
      aceptar = true;
      guardado = true;
      tipoLista = 0;
      tipoListaNov = 0;
      lovEmpleados = null;
      permitirIndex = true;
      nuevaNovedad = new NovedadesSistema();
      nuevaNovedad.setEstado("ABIERTO");
      nuevaNovedad.setDias(BigInteger.valueOf(0));
      nuevaNovedad.setPagarporfuera("S");
      nuevaNovedad.setTipo("CESANTIA");
      nuevaNovedad.setSubtipo("DINERO");
      nuevaNovedad.setFechasistema(new Date());
      altotabla = 120;
      altotablaReg = 6;
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      novedadSeleccionada = null;
      listaNovedades = null;
      activarMTodos = true;
      navegar("atras");
   }

   public void eventoFiltrar() {
      if (tipoListaNov == 0) {
         tipoListaNov = 1;
      }
      novedadSeleccionada = null;
      contarRegistrosNovedades();
   }

   public void eventoFiltrarEmpleados() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      novedadSeleccionada = null;
      empleadoSeleccionado = null;
      listaNovedades = null;
      filtrarListaNovedades = null;
      if (tipoListaNov == 1) {
         RequestContext.getCurrentInstance().execute("PF('datosNovedadesEmpleado').clearFilters()");
      }
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      contarRegistrosNovedades();
      contarRegistroEmpleados();
   }

   public void contarRegistroEmpleados() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
   }

   public void contarRegistroEmpleadosLov() {
      RequestContext.getCurrentInstance().update("formLovEmpleados:infoRegistroEmpleadosLOV");
   }

   public void contarRegistroMotivosNovedades() {
      RequestContext.getCurrentInstance().update("formLovMotivosC:infoRegistroMotivosNovedades");
   }

   public void contarRegistrosNovedades() {
      RequestContext.getCurrentInstance().update("form:infoRegistroNovedad");
   }

   public void entrarNuevoRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("entrarNuevoRegistro.empleadoSeleccionado : " + empleadoSeleccionado.getSecuencia());

      if (empleadoSeleccionado != null) {
         System.out.println("empleadoSeleccionado : " + empleadoSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
         RequestContext.getCurrentInstance().execute("PF('nuevanovedadpagoparcialcesantias').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada != null) {
         int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADESSISTEMA");
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
      } else if (administrarRastros.verificarHistoricosTabla("NOVEDADESSISTEMA")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      novedadSeleccionada = null;
   }

   public BigDecimal retornarprovision() {
      if (provisionlov == null) {
         getProvisionlov();
      }
      return provisionlov;
   }

   public BigDecimal retornarprovisioncesantias() {
      if (provisioncesantias == null) {
         getProvisioncesantias();
      }
      return provisioncesantias;
   }

   public void autocompletar(int tipoNuevo, String campo) {

      if (campo.equals("MOTIVO")) {
         if (tipoNuevo == 1) {
            motivo = nuevaNovedad.getMotivocesantia().getNombre();
         } else if (tipoNuevo == 2) {
            motivo = duplicarNovedad.getMotivocesantia().getNombre();
         }

      }
   }

   public void valoresautocompletarnuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("MOTIVO")) {
         if (tipoNuevo == 1) {
            nuevaNovedad.getMotivocesantia().setNombre(motivo);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getMotivocesantia().setNombre(motivo);
         }
         for (int i = 0; i < lovMotivosCesantia.size(); i++) {
            if (lovMotivosCesantia.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setMotivocesantia(lovMotivosCesantia.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigencias");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setMotivocesantia(lovMotivosCesantia.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigencias");
            }
            lovMotivosCesantia.clear();
            getLovMotivosCesantia();
         } else {
            RequestContext.getCurrentInstance().update("form:motivoscesantiasDialogo");
            RequestContext.getCurrentInstance().execute("PF('motivoscesantiasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevanovedadpagoparcialcesantias");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarregistroNovedad");
            }
         }
      }
   }

   public void listaValoresBoton() {
      if (cualCelda == 5) {
         cargarLovMotivos();
         contarRegistroMotivosNovedades();
         RequestContext.getCurrentInstance().update("formLovMotivosC:motivoscesantiasDialogo");
         RequestContext.getCurrentInstance().execute("PF('motivoscesantiasDialogo').show()");
      }
   }

   public void cargarLovEmpleados() {
      if (lovEmpleados == null) {
         lovEmpleados = administrarNovedadesPagoCesantias.empleadosCesantias();
      }

   }

   public void cargarLovMotivos() {
      if (lovMotivosCesantia == null) {
         lovMotivosCesantia = administrarNovedadesPagoCesantias.consultarMotivosCesantias();
      }
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   ///////////////////////GETS Y SETS////////////////////////////////////////////////
   public List<Empleados> getListaEmpleadosNovedad() {
      if (listaEmpleadosNovedad == null) {
         listaEmpleadosNovedad = administrarNovedadesPagoCesantias.empleadosCesantias();
      }
      return listaEmpleadosNovedad;
   }

   public void setListaEmpleadosNovedad(List<Empleados> listaEmpleadosNovedad) {
      this.listaEmpleadosNovedad = listaEmpleadosNovedad;
   }

   public List<Empleados> getFiltrarListaEmpleados() {
      return filtrarListaEmpleados;
   }

   public void setFiltrarListaEmpleados(List<Empleados> filtrarListaEmpleados) {
      this.filtrarListaEmpleados = filtrarListaEmpleados;
   }

   public Empleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public List<NovedadesSistema> getListaNovedades() {
      if (listaNovedades == null) {
         listaNovedades = new ArrayList<NovedadesSistema>();
      }
      return listaNovedades;
   }

   public void setListaNovedades(List<NovedadesSistema> listaNovedades) {
      this.listaNovedades = listaNovedades;
   }

   public List<NovedadesSistema> getFiltrarListaNovedades() {
      return filtrarListaNovedades;
   }

   public void setFiltrarListaNovedades(List<NovedadesSistema> filtrarListaNovedades) {
      this.filtrarListaNovedades = filtrarListaNovedades;
   }

   public NovedadesSistema getNovedadSeleccionada() {
      return novedadSeleccionada;
   }

   public void setNovedadSeleccionada(NovedadesSistema novedadSeleccionada) {
      this.novedadSeleccionada = novedadSeleccionada;
   }

   public NovedadesSistema getDuplicarNovedad() {
      return duplicarNovedad;
   }

   public void setDuplicarNovedad(NovedadesSistema duplicarNovedad) {
      this.duplicarNovedad = duplicarNovedad;
   }

   public NovedadesSistema getEditarNovedades() {
      return editarNovedades;
   }

   public void setEditarNovedades(NovedadesSistema editarNovedades) {
      this.editarNovedades = editarNovedades;
   }

   public List<NovedadesSistema> getListaNovedadesCrear() {
      return listaNovedadesCrear;
   }

   public void setListaNovedadesCrear(List<NovedadesSistema> listaNovedadesCrear) {
      this.listaNovedadesCrear = listaNovedadesCrear;
   }

   public NovedadesSistema getNuevaNovedad() {
      return nuevaNovedad;
   }

   public void setNuevaNovedad(NovedadesSistema nuevaNovedad) {
      this.nuevaNovedad = nuevaNovedad;
   }

   public List<NovedadesSistema> getListaNovedadesModificar() {
      return listaNovedadesModificar;
   }

   public void setListaNovedadesModificar(List<NovedadesSistema> listaNovedadesModificar) {
      this.listaNovedadesModificar = listaNovedadesModificar;
   }

   public List<NovedadesSistema> getListaNovedadesBorrar() {
      return listaNovedadesBorrar;
   }

   public void setListaNovedadesBorrar(List<NovedadesSistema> listaNovedadesBorrar) {
      this.listaNovedadesBorrar = listaNovedadesBorrar;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public List<Empleados> getLovEmpleados() {
      return lovEmpleados;
   }

   public void setLovEmpleados(List<Empleados> lovEmpleados) {
      this.lovEmpleados = lovEmpleados;
   }

   public List<Empleados> getLovEmpleadosFiltrar() {
      return lovEmpleadosFiltrar;
   }

   public void setLovEmpleadosFiltrar(List<Empleados> lovEmpleadosFiltrar) {
      this.lovEmpleadosFiltrar = lovEmpleadosFiltrar;
   }

   public Empleados getEmpleadoSeleccionadoLOV() {
      return empleadoSeleccionadoLOV;
   }

   public void setEmpleadoSeleccionadoLOV(Empleados empleadoSeleccionadoLOV) {
      this.empleadoSeleccionadoLOV = empleadoSeleccionadoLOV;
   }

   public List<MotivosCesantias> getLovMotivosCesantia() {
      return lovMotivosCesantia;
   }

   public void setLovMotivosCesantia(List<MotivosCesantias> lovMotivosCesantia) {
      this.lovMotivosCesantia = lovMotivosCesantia;
   }

   public List<MotivosCesantias> getFiltrarLovMotivosCesantias() {
      return filtrarLovMotivosCesantias;
   }

   public void setFiltrarLovMotivosCesantias(List<MotivosCesantias> filtrarLovMotivosCesantias) {
      this.filtrarLovMotivosCesantias = filtrarLovMotivosCesantias;
   }

   public MotivosCesantias getMotivoCesantiaLovSeleccionado() {
      return motivoCesantiaLovSeleccionado;
   }

   public void setMotivoCesantiaLovSeleccionado(MotivosCesantias motivoCesantiaLovSeleccionado) {
      this.motivoCesantiaLovSeleccionado = motivoCesantiaLovSeleccionado;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

   public int getAltotabla() {
      return altotabla;
   }

   public void setAltotabla(int altotabla) {
      this.altotabla = altotabla;
   }

   public int getAltotablaReg() {
//      if (altotabla == 100) {
//         altotablaReg = 5;
//      } else {
//         altotablaReg = 6;
//      }
      return altotablaReg;
   }

   public void setAltotablaReg(int altotablaReg) {
      this.altotablaReg = altotablaReg;
   }

   public boolean isActivarMTodos() {
      return activarMTodos;
   }

   public void setActivarMTodos(boolean activarMTodos) {
      this.activarMTodos = activarMTodos;
   }

   public String getInfoRegistroEmpleados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpleados");
      infoRegistroEmpleados = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleados;
   }

   public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
      this.infoRegistroEmpleados = infoRegistroEmpleados;
   }

   public String getInfoRegistroEmpleadosLov() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovEmpleados:LOVEmpleados");
      if (lovEmpleadosFiltrar != null) {
         if (lovEmpleadosFiltrar.size() == 1) {
            empleadoSeleccionadoLOV = lovEmpleadosFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();PF('LOVEmpleados').selectRow(0);");
         } else {
            empleadoSeleccionadoLOV = null;
            RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();");
         }
      } else {
         empleadoSeleccionadoLOV = null;
         aceptar = true;
      }
      infoRegistroEmpleadosLov = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadosLov;
   }

   public void setInfoRegistroEmpleadosLov(String infoRegistroEmpleadosLov) {
      this.infoRegistroEmpleadosLov = infoRegistroEmpleadosLov;
   }

   public String getInfoRegistroMotivosCesantias() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovMotivosC:lovmotivoscesantias");
      if (filtrarLovMotivosCesantias != null) {
         if (filtrarLovMotivosCesantias.size() == 1) {
            motivoCesantiaLovSeleccionado = filtrarLovMotivosCesantias.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovmotivoscesantias').unselectAllRows();PF('lovmotivoscesantias').selectRow(0);");
         } else {
            motivoCesantiaLovSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('lovmotivoscesantias').unselectAllRows();");
         }
      } else {
         motivoCesantiaLovSeleccionado = null;
         aceptar = true;
      }
      infoRegistroMotivosCesantias = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivosCesantias;
   }

   public void setInfoRegistroMotivosCesantias(String infoRegistroMotivosCesantias) {
      this.infoRegistroMotivosCesantias = infoRegistroMotivosCesantias;
   }

   public String getInfoRegistroNovedadCesantias() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNovedadesEmpleado");
      infoRegistroNovedadCesantias = String.valueOf(tabla.getRowCount());
      return infoRegistroNovedadCesantias;
   }

   public void setInfoRegistroNovedadCesantias(String infoRegistroNovedadCesantias) {
      this.infoRegistroNovedadCesantias = infoRegistroNovedadCesantias;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public List<BigDecimal> getListavalorcesantias() {
      return listavalorcesantias;
   }

   public void setListavalorcesantias(List<BigDecimal> listavalorcesantias) {
      this.listavalorcesantias = listavalorcesantias;
   }

   public List<BigDecimal> getListavalorintcesantias() {
      return listavalorintcesantias;
   }

   public void setListavalorintcesantias(List<BigDecimal> listavalorintcesantias) {
      this.listavalorintcesantias = listavalorintcesantias;
   }

   public List<BigDecimal> getListafiltrarvalorcesantias() {
      return listafiltrarvalorcesantias;
   }

   public void setListafiltrarvalorcesantias(List<BigDecimal> listafiltrarvalorcesantias) {
      this.listafiltrarvalorcesantias = listafiltrarvalorcesantias;
   }

   public BigDecimal getValorcesantiaSeleccionado() {
      return valorcesantiaSeleccionado;
   }

   public void setValorcesantiaSeleccionado(BigDecimal valorcesantiaSeleccionado) {
      this.valorcesantiaSeleccionado = valorcesantiaSeleccionado;
   }

   public List<BigDecimal> getListafiltrarvalorintcesantias() {
      return listafiltrarvalorintcesantias;
   }

   public void setListafiltrarvalorintcesantias(List<BigDecimal> listafiltrarvalorintcesantias) {
      this.listafiltrarvalorintcesantias = listafiltrarvalorintcesantias;
   }

   public BigDecimal getValorintcesantiaSeleccionado() {
      return valorintcesantiaSeleccionado;
   }

   public void setValorintcesantiaSeleccionado(BigDecimal valorintcesantiaSeleccionado) {
      this.valorintcesantiaSeleccionado = valorintcesantiaSeleccionado;
   }

   public BigDecimal getProvisionlov() {
      provisionlov = administrarNovedadesSistema.valorIntCesantias(empleadoSeleccionado.getSecuencia());
      return provisionlov;
   }

   public void setProvisionlov(BigDecimal provisionlov) {
      this.provisionlov = provisionlov;
   }

   public BigDecimal getProvisioncesantias() {
      provisioncesantias = administrarNovedadesSistema.valorCesantias(empleadoSeleccionado.getSecuencia());
      return provisioncesantias;
   }

   public void setProvisioncesantias(BigDecimal provisioncesantias) {
      this.provisioncesantias = provisioncesantias;
   }

   public boolean isTodas() {
      return todas;
   }

   public void setTodas(boolean todas) {
      this.todas = todas;
   }

   public boolean isActuales() {
      return actuales;
   }

   public void setActuales(boolean actuales) {
      this.actuales = actuales;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
