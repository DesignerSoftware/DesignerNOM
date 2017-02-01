/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.LiquidacionesLogs;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarLiquidacionesLogsInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import Entidades.Operandos;
import java.util.Map;
import java.util.LinkedHashMap;
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
public class ControlLiquidacionesLogs implements Serializable {

   @EJB
   AdministrarLiquidacionesLogsInterface administrarLiquidacionesLogs;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<LiquidacionesLogs> listLiquidacionesLogs;
   private List<LiquidacionesLogs> filtrarLiquidacionesLogs;
   private LiquidacionesLogs editarLiquidacionesLogs;
   private LiquidacionesLogs liquidacionLogSeleccionada;
   //otros
   private int cualCelda, tipoLista, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private Column fechaInicial, fechaFinal, empleado, operando, proceso, valor;
   //borrado
   //filtrado table
   private int tamano;
   private String infoRegistroEmpleados;
   private String infoRegistroOperandos;

   private List<Empleados> lovEmpleados;
   private List<Empleados> filtrarEmpleados;
   private Empleados empleadoSeleccionado;

   private List<Operandos> lovOperandos;
   private List<Operandos> filtrarOperandos;
   private Operandos operandoSeleccionado;

   private String nombreVariable, nombreDato;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   private Date backUpFechaDesde;
   private Date backUpFechaHasta;
   private String backUpEmpleado;
   private String backUpOperando;
   private String backUpProceso;
   private String backUpValor;

   public ControlLiquidacionesLogs() {
      listLiquidacionesLogs = null;
      permitirIndex = true;
      editarLiquidacionesLogs = new LiquidacionesLogs();
      guardado = true;
      aceptar = true;
      tamano = 15;
      operandoSeleccionado = null;
      empleadoSeleccionado = null;
      mapParametros.put("paginaAnterior", paginaAnterior);
      nombreVariable = "Empleado: ";
      nombreDato = "";
      lovEmpleados = null;
      lovOperandos = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         System.out.println("ControlLiquidacionesLogs PostConstruct ");
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarLiquidacionesLogs.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         getLovEmpleados();
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
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
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "liquidacionlog";
         //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParametros.put("paginaAnterior", pagActual);
         //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
         controlListaNavegacion.adicionarPagina(pagActual);
      }
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
      liquidacionLogSeleccionada = null;
   }

   public void cambiarIndice(LiquidacionesLogs liqLog, int celda) {
      liquidacionLogSeleccionada = liqLog;

      if (permitirIndex == true) {
         cualCelda = celda;
         if (cualCelda == 0) {
            backUpFechaDesde = liquidacionLogSeleccionada.getFechadesde();
         }
         if (cualCelda == 1) {
            backUpFechaHasta = liquidacionLogSeleccionada.getFechahasta();
         }
         if (cualCelda == 2) {
            backUpEmpleado = liquidacionLogSeleccionada.getEmpleado().getPersona().getNombreCompleto();
         }
         if (cualCelda == 3) {
            backUpOperando = liquidacionLogSeleccionada.getOperando().getDescripcion();
         }
         if (cualCelda == 4) {
            backUpProceso = liquidacionLogSeleccionada.getProceso().getDescripcion();
         }
         if (cualCelda == 5) {
            backUpValor = liquidacionLogSeleccionada.getValor();
         }
      }
   }

//   public void asignarIndex(LiquidacionesLogs liqLog, int LND, int dig) {
//      liquidacionLogSeleccionada = liqLog;
//      try {
////         tipoActualizacion = LND;
//      } catch (Exception e) {
//         System.out.println("ERROR ControlLiquidacionesLogs.asignarIndex ERROR======" + e.getMessage());
//      }
//   }
//
//   public void modificarLiquidacionesLogSinGuardar(int indice, String confirmarCambio, String valorConfirmar) {
//      index = indice;
//      int coincidencias = 0;
//      int indiceUnicoElemento = 0, pass = 0;
//      RequestContext context = RequestContext.getCurrentInstance();
//      if (confirmarCambio.equalsIgnoreCase("N")) {
//         System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar");
//         if (!crearLiquidacionesLogs.contains(liquidacionLogSeleccionada)) {
//
//            if (liquidacionLogSeleccionada.getEmpleado().getPersona().getNombreCompleto() == null) {
//               System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpEmpleado : " + backUpEmpleado);
//               liquidacionLogSeleccionada.getEmpleado().getPersona().setNombreCompleto(backUpEmpleado);
//            } else if (!liquidacionLogSeleccionada.getEmpleado().getPersona().getNombreCompleto().equals(backUpEmpleado) && backUpEmpleado != null) {
//               liquidacionLogSeleccionada.getEmpleado().getPersona().setNombreCompleto(backUpEmpleado);
//               System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpEmpleado : " + backUpEmpleado);
//            }
//            if (liquidacionLogSeleccionada.getOperando().getDescripcion() == null) {
//               liquidacionLogSeleccionada.getOperando().setDescripcion(backUpOperando);
//               System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpOperando : " + backUpOperando);
//            } else if (!liquidacionLogSeleccionada.getOperando().getDescripcion().equals(backUpOperando) && backUpOperando != null) {
//               liquidacionLogSeleccionada.getOperando().setDescripcion(backUpOperando);
//               System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpOperando : " + backUpOperando);
//            }
//            if (liquidacionLogSeleccionada.getProceso().getDescripcion() == null) {
//               liquidacionLogSeleccionada.getProceso().setDescripcion(backUpProceso);
//               System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpProceso : " + backUpProceso);
//            } else if (!liquidacionLogSeleccionada.getProceso().getDescripcion().equals(backUpProceso) && backUpProceso != null) {
//               liquidacionLogSeleccionada.getProceso().setDescripcion(backUpProceso);
//               System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpProceso : " + backUpProceso);
//            }
//            if (liquidacionLogSeleccionada.getValor() == null) {
//               liquidacionLogSeleccionada.setValor(backUpValor);
//               System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpValor : " + backUpValor);
//            } else if (!liquidacionLogSeleccionada.getValor().equals(backUpValor) && backUpValor != null) {
//               liquidacionLogSeleccionada.setValor(backUpValor);
//               System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpValor : " + backUpValor);
//            }
//
//            liquidacionLogSeleccionada = null;
//         }
//      }
//      RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
//   }
   public void activarAceptar() {
      aceptar = false;
   }

   private String infoRegistro;

   public void cancelarModificacion() {
      if (bandera == 1) {
         restaurarTabla();
      }
      liquidacionLogSeleccionada = null;
      k = 0;
      guardado = true;
      permitirIndex = true;
      listLiquidacionesLogs = null;
      getListLiquidacionesLogs();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
   }

   public void restaurarTabla() {
      //CERRAR FILTRADO
      FacesContext c = FacesContext.getCurrentInstance();
      fechaInicial = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaInicial");
      fechaInicial.setFilterStyle("display: none; visibility: hidden;");
      fechaFinal = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaFinal");
      fechaFinal.setFilterStyle("display: none; visibility: hidden;");
      empleado = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:empleado");
      empleado.setFilterStyle("display: none; visibility: hidden;");
      operando = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:operando");
      operando.setFilterStyle("display: none; visibility: hidden;");
      proceso = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:proceso");
      proceso.setFilterStyle("display: none; visibility: hidden;");
      valor = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:valor");
      valor.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
      bandera = 0;
      filtrarLiquidacionesLogs = null;
      tipoLista = 0;
      tamano = 15;
   }

   public void salir() {
      if (bandera == 1) {
         restaurarTabla();
      }
      liquidacionLogSeleccionada = null;
      k = 0;
      listLiquidacionesLogs = null;
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 14;
         fechaInicial = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaInicial");
         fechaInicial.setFilterStyle("width: 85% !important;");
         fechaFinal = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaFinal");
         fechaFinal.setFilterStyle("width: 85% !important;");
         empleado = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:empleado");
         empleado.setFilterStyle("width: 85% !important;");
         operando = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:operando");
         operando.setFilterStyle("width: 85% !important;");
         proceso = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:proceso");
         proceso.setFilterStyle("width: 85% !important;");
         valor = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:valor");
         valor.setFilterStyle("width: 85% !important;");

         System.out.println("Activar");
         bandera = 1;
         RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

   public void editarCelda() {
      if (liquidacionLogSeleccionada != null) {
         editarLiquidacionesLogs = liquidacionLogSeleccionada;

         System.out.println("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicial");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicial').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHasta");
            RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleado");
            RequestContext.getCurrentInstance().execute("PF('editarEmpleado').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarOperandoE");
            RequestContext.getCurrentInstance().execute("PF('editarOperandoE').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarProcesoE");
            RequestContext.getCurrentInstance().execute("PF('editarProcesoE').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValorE");
            RequestContext.getCurrentInstance().execute("PF('editarValorE').show()");
            cualCelda = -1;
         }
      }
   }

   public void revisarDialogoGuardar() {
      RequestContext.getCurrentInstance().update("form:confirmarGuardar");
      RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
   }

   public void seleccionarEmpleado() {
      RequestContext context = RequestContext.getCurrentInstance();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         restaurarTabla();
      }
      listLiquidacionesLogs = null;
      getListLiquidacionesLogs();
      aceptar = true;
      nombreVariable = "Empleado : ";
      nombreDato = empleadoSeleccionado.getPersona().getNombreCompleto();
      operandoSeleccionado = null;
      context.execute("PF('lovOperandos').unselectAllRows();");
      context.update("form:datosLiquidacionesLogs");
      context.update("form:nombredato");
      context.update("form:nombrevariable");
      context.reset("form:lovEmpleados:globalFilter");
      context.execute("PF('lovEmpleados').clearFilters()");
      context.execute("PF('EMPLEADOS').hide()");
      contarRegistros();
   }

   public void seleccionarOperando() {
      RequestContext context = RequestContext.getCurrentInstance();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         restaurarTabla();
      }
      listLiquidacionesLogs = null;
      listLiquidacionesLogs = administrarLiquidacionesLogs.consultarLiquidacionesLogsPorOperando(operandoSeleccionado.getSecuencia());
      aceptar = true;
      nombreVariable = "Operando : ";
      nombreDato = operandoSeleccionado.getNombre();
      empleadoSeleccionado = null;
      context.execute("PF('lovEmpleados').unselectAllRows();");
      context.update("form:datosLiquidacionesLogs");
      context.update("form:nombredato");
      context.update("form:nombrevariable");
      context.reset("form:lovOperandos:globalFilter");
      context.execute("PF('lovOperandos').clearFilters()");
      context.execute("PF('OPERANDOS').hide()");
      contarRegistros();
   }

   public void cancelarCambioEmpleado() {
      filtrarEmpleados = null;
      empleadoSeleccionado = null;
      aceptar = true;
//      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EMPLEADOS').hide()");
   }

   public void cancelarCambioOperando() {
      filtrarOperandos = null;
      operandoSeleccionado = null;
      aceptar = true;
//      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovOperandos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOperandos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('OPERANDOS').hide()");
   }

   public void llamarDialogoEmpleado() {
      RequestContext.getCurrentInstance().update("form:EMPLEADOS");
      RequestContext.getCurrentInstance().execute("PF('EMPLEADOS').show()");
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLiquidacionesLogsExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "LIQUIDACIONESLOGS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLiquidacionesLogsExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "LIQUIDACIONESLOGS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      System.out.println("lol");
      if (!listLiquidacionesLogs.isEmpty()) {
         if (liquidacionLogSeleccionada != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(liquidacionLogSeleccionada.getSecuencia(), "LIQUIDACIONESLOGS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            System.out.println("resultado: " + resultado);
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
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("LIQUIDACIONESLOGS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosEmpl() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
   }

   public void contarRegistrosOper() {
      RequestContext.getCurrentInstance().update("form:infoRegistroOperandos");
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<LiquidacionesLogs> getListLiquidacionesLogs() {
      if (empleadoSeleccionado == null) {
         getLovEmpleados();
      }
      if (listLiquidacionesLogs == null) {
         listLiquidacionesLogs = administrarLiquidacionesLogs.consultarLiquidacionesLogsPorEmpleado(empleadoSeleccionado.getSecuencia());
         nombreVariable = "Empleado : ";
         nombreDato = empleadoSeleccionado.getPersona().getNombreCompleto();
      }
      return listLiquidacionesLogs;
   }

   public void setListLiquidacionesLogs(List<LiquidacionesLogs> listLiquidacionesLogs) {
      this.listLiquidacionesLogs = listLiquidacionesLogs;
   }

   public List<LiquidacionesLogs> getFiltrarLiquidacionesLogs() {
      return filtrarLiquidacionesLogs;
   }

   public void setFiltrarLiquidacionesLogs(List<LiquidacionesLogs> filtrarLiquidacionesLogs) {
      this.filtrarLiquidacionesLogs = filtrarLiquidacionesLogs;
   }

   public LiquidacionesLogs getEditarLiquidacionesLogs() {
      return editarLiquidacionesLogs;
   }

   public void setEditarLiquidacionesLogs(LiquidacionesLogs editarLiquidacionesLogs) {
      this.editarLiquidacionesLogs = editarLiquidacionesLogs;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public LiquidacionesLogs getLiquidacionLogSeleccionada() {
      return liquidacionLogSeleccionada;
   }

   public void setLiquidacionLogSeleccionada(LiquidacionesLogs clasesPensionesSeleccionado) {
      this.liquidacionLogSeleccionada = clasesPensionesSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosLiquidacionesLogs");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public List<Empleados> getLovEmpleados() {
      if (lovEmpleados == null) {
         lovEmpleados = administrarLiquidacionesLogs.consultarLOVEmpleados();
         if (empleadoSeleccionado == null && lovEmpleados != null) {
            if (!lovEmpleados.isEmpty()) {
               empleadoSeleccionado = lovEmpleados.get(0);
               nombreDato = empleadoSeleccionado.getPersona().getNombreCompleto();
            }
         }
      }
      return lovEmpleados;
   }

   public void setLovEmpleados(List<Empleados> lovEmpleados) {
      this.lovEmpleados = lovEmpleados;
   }

   public List<Operandos> getLovOperandos() {
      if (lovOperandos == null) {
         lovOperandos = administrarLiquidacionesLogs.consultarLOVOperandos();
      }
      return lovOperandos;
   }

   public void setLovOperandos(List<Operandos> lovOperandos) {
      this.lovOperandos = lovOperandos;
   }

   public List<Operandos> getFiltrarOperandos() {
      return filtrarOperandos;
   }

   public void setFiltrarOperandos(List<Operandos> filtrarOperandos) {
      this.filtrarOperandos = filtrarOperandos;
   }

   public Operandos getOperandoSeleccionado() {
      return operandoSeleccionado;
   }

   public void setOperandoSeleccionado(Operandos operandoSeleccionado) {
      this.operandoSeleccionado = operandoSeleccionado;
   }

   public List<Empleados> getFiltrarEmpleados() {
      return filtrarEmpleados;
   }

   public void setFiltrarEmpleados(List<Empleados> filtrarEmpleados) {
      this.filtrarEmpleados = filtrarEmpleados;
   }

   public Empleados getEmpleadoSeleccionado() {
      if (empleadoSeleccionado == null) {
         getLovEmpleados();
      }
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public String getInfoRegistroEmpleados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpleados");
      infoRegistroEmpleados = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleados;
   }

   public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
      this.infoRegistroEmpleados = infoRegistroEmpleados;
   }

   public String getInfoRegistroOperandos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOperandos");
      infoRegistroOperandos = String.valueOf(tabla.getRowCount());
      return infoRegistroOperandos;
   }

   public void setInfoRegistroOperandos(String infoRegistroOperandos) {
      this.infoRegistroOperandos = infoRegistroOperandos;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getNombreVariable() {
      return nombreVariable;
   }

   public void setNombreVariable(String nombreVariable) {
      this.nombreVariable = nombreVariable;
   }

   public String getNombreDato() {
      return nombreDato;
   }

   public void setNombreDato(String nombreDato) {
      this.nombreDato = nombreDato;
   }

}
