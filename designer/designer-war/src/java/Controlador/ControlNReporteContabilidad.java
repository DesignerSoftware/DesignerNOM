/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.Procesos;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarInforeportesInterface;
import InterfaceAdministrar.AdministrarNReporteContabilidadInterface;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author AndresPineda
 */
@ManagedBean
@SessionScoped
public class ControlNReporteContabilidad implements Serializable {

   @EJB
   AdministrarNReporteContabilidadInterface administrarNReporteContabilidad;
   @EJB
   AdministarReportesInterface administarReportes;
   @EJB
   AdministrarInforeportesInterface administrarInforeportes;
   ///
   private ParametrosReportes parametroDeReporte;
   private ParametrosReportes parametroModificacion;
//
   private List<Inforeportes> listaIR;
   private List<Inforeportes> listaInfoReportesModificados;
   private List<Inforeportes> filtrarListInforeportesUsuario;
   private Inforeportes inforreporteSeleccionado;
   //LOV
   private List<Inforeportes> listInforeportes;
   private Inforeportes reporteLovSeleccionado;
   private List<Inforeportes> filtrarLovInforeportes;
   private List<Inforeportes> filtrarReportes;
   //
   private String reporteGenerar;
   private String requisitosReporte;
   private String proceso, empresa;
   private String altoTabla;
   private String color, decoracion;
   private String color2, decoracion2;
   private String infoRegistroProceso, infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroEmpresa, infoRegistroReportes;
   private String infoRegistro;
   private String nombreReporte, tipoReporte;
   private String pathReporteGenerado;
   private String cabezeraVisor;
   //
   private int bandera;
   private int casilla;
   private int posicionReporte;
   private int casillaInforReporte;
   private int tipoLista;
   //
   private boolean aceptar, cambiosReporte;
   private boolean permitirIndex;
   private boolean activoMostrarTodos, activoBuscarReporte, activarEnvio;
   //
   private Column codigoIR, reporteIR, tipoIR;
   //
   private InputText empleadoDesdeParametroL, empleadoHastaParametroL;
   //
   private List<Empleados> listEmpleados;
   private List<Empleados> filtrarListEmpleados;
   private Empleados empleadoSeleccionado;
   //
   private List<Procesos> listProcesos;
   private Procesos procesoSeleccionado;
   private List<Procesos> filtrarListProcesos;
   //
   private List<Empresas> listEmpresas;
   private Empresas empresaSeleccionada;
   private List<Empresas> filtrarListEmpresas;
   //
   private SelectOneMenu estadoParametro;
   //EXPORTAR REPORTE
   private StreamedContent file;
   //Listener reporte
   private AsynchronousFilllListener asistenteReporte;
   //
   private Date fechaDesde, fechaHasta;

   private BigDecimal emplDesde, emplHasta;
   //
   private DataTable tabla;
//
   private boolean estadoReporte;
   private String resultadoReporte;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlNReporteContabilidad() {
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      activarEnvio = true;
      color = "black";
      decoracion = "none";
      color2 = "black";
      decoracion2 = "none";
      casillaInforReporte = -1;
      inforreporteSeleccionado = null;
      cambiosReporte = true;
      listaInfoReportesModificados = new ArrayList<>();
      altoTabla = "185";
      parametroDeReporte = null;
      listaIR = null;
      bandera = 0;
      aceptar = true;
      casilla = -1;
      parametroModificacion = new ParametrosReportes();
      tipoLista = 0;
      reporteGenerar = "";
      requisitosReporte = "";
      posicionReporte = -1;
      //
      listEmpleados = null;
      listEmpresas = null;
      //
      empleadoSeleccionado = new Empleados();
      empresaSeleccionada = new Empresas();
      reporteLovSeleccionado = null;
      //
      permitirIndex = true;
      cabezeraVisor = null;
      System.out.println(this.getClass().getName() + " fin del Constructor()");
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
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "nreportescontabilidad";
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

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarNReporteContabilidad.obtenerConexion(ses.getId());
         administarReportes.obtenerConexion(ses.getId());
         administrarInforeportes.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Controlador.ControlNReporteContabilidad.inicializarAdministrador()");
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void iniciarPagina() {
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      getListaIR();
   }

   //TOOLTIP
   public void guardarCambios() {
      try {
         if (cambiosReporte == false) {
            if (parametroDeReporte.getUsuario() != null) {
               if (parametroDeReporte.getProceso().getSecuencia() == null) {
                  parametroDeReporte.setProceso(null);
               }
               if (parametroDeReporte.getEmpresa().getSecuencia() == null) {
                  parametroDeReporte.setEmpresa(null);
               }
               administrarNReporteContabilidad.modificarParametrosReportes(parametroDeReporte);
            }
            if (!listaInfoReportesModificados.isEmpty()) {
               administrarNReporteContabilidad.guardarCambiosInfoReportes(listaInfoReportesModificados);
            }
            cambiosReporte = true;
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            parametroDeReporte.setFechadesde(fechaDesde);
            parametroDeReporte.setFechahasta(fechaHasta);
            RequestContext.getCurrentInstance().update("formParametros");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } catch (Exception e) {
         System.out.println("Error en guardar Cambios Controlador : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (casilla >= 1) {
         if (casilla == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaDesde");
            RequestContext.getCurrentInstance().execute("PF('editarFechaDesde').show()");
         }
         if (casilla == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadoDesde");
            RequestContext.getCurrentInstance().execute("PF('empleadoDesde').show()");
         }

         if (casilla == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHasta");
            RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
         }
         if (casilla == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadoHasta");
            RequestContext.getCurrentInstance().execute("PF('empleadoHasta').show()");
         }
         if (casilla == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:proceso");
            RequestContext.getCurrentInstance().execute("PF('proceso').show()");
         }
         if (casilla == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:empresa");
            RequestContext.getCurrentInstance().execute("PF('empresa').show()");
         }
         casilla = -1;
      }
      if (casillaInforReporte >= 1) {
         if (casillaInforReporte == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:infoReporteCodigoD");
            RequestContext.getCurrentInstance().execute("PF('infoReporteCodigoD').show()");
         }
         if (casillaInforReporte == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:infoReporteNombreD");
            RequestContext.getCurrentInstance().execute("PF('infoReporteNombreD').show()");
         }
         casillaInforReporte = -1;
      }
   }

   public void mostrarDialogosListas() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (casilla == 2) {
         if ((listEmpleados == null) || listEmpleados.isEmpty()) {
            listEmpleados = null;
         }
         RequestContext.getCurrentInstance().update("formDialogos:EmpleadoDesdeDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
      }
      if (casilla == 4) {
         RequestContext.getCurrentInstance().update("formDialogos:EmpleadoHastaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
      }
      if (casilla == 5) {
         RequestContext.getCurrentInstance().update("formDialogos:ProcesoDialogo");
         RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
      }
      if (casilla == 6) {
         RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
      }
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         altoTabla = "165";
         codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:codigoIR");
         codigoIR.setFilterStyle("width: 85% !important;");
         reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:reporteIR");
         reporteIR.setFilterStyle("width: 85% !important;");
         tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:tipoIR");
         tipoIR.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:reportesContabilidad");
         tipoLista = 1;
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltrado();
         defaultPropiedadesParametrosReporte();
      }

   }

   private void cerrarFiltrado() {
      altoTabla = "185";
      codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:codigoIR");
      codigoIR.setFilterStyle("display: none; visibility: hidden;");
      reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:reporteIR");
      reporteIR.setFilterStyle("display: none; visibility: hidden;");
      tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:tipoIR");
      tipoIR.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:reportesContabilidad");
      bandera = 0;
      filtrarListInforeportesUsuario = null;
      tipoLista = 0;
   }

   public void salir() {
      if (bandera == 1) {
         cerrarFiltrado();
      }
      listEmpleados = null;
      listaIR = null;
      listProcesos = null;
      listEmpresas = null;
      listInforeportes = null;
      parametroDeReporte = null;
      parametroModificacion = null;
      casilla = -1;
      procesoSeleccionado = null;
      listaInfoReportesModificados.clear();
      cambiosReporte = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void cancelarModificaciones() {
      if (bandera == 1) {
         cerrarFiltrado();
      }
      listaIR = null;
      casilla = -1;
      listaInfoReportesModificados.clear();
      cambiosReporte = true;
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      inforreporteSeleccionado = null;
      getParametroDeReporte();
      getListaIR();
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:reportesContabilidad");
      RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametroL");
      RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametroL");
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");
      RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
      RequestContext.getCurrentInstance().update("formParametros:procesoParametroL");
      RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
   }

   public void cancelarYSalir() {
      cancelarModificaciones();
      salir();
   }

   public void eventoFiltrar() {
      contarRegistros();
   }

   private void activarEnvioCorreo() {
      if (inforreporteSeleccionado != null) {
         activarEnvio = false;
      } else {
         activarEnvio = false;
      }
      RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
   }

   public void seleccionRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
      activarEnvioCorreo();
      defaultPropiedadesParametrosReporte();
      if (inforreporteSeleccionado.getFecdesde().equals("SI")) {
         color = "red";
         RequestContext.getCurrentInstance().update("formParametros");
      }
      System.out.println("reporteSeleccionado.getFechasta(): " + inforreporteSeleccionado.getFechasta());
      if (inforreporteSeleccionado.getFechasta().equals("SI")) {
         color2 = "red";
         RequestContext.getCurrentInstance().update("formParametros");
      }
      System.out.println("reporteSeleccionado.getEmdesde(): " + inforreporteSeleccionado.getEmdesde());
      if (inforreporteSeleccionado.getEmdesde().equals("SI")) {
         empleadoDesdeParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametroL");
         //empleadoDesdeParametro.setStyle("position: absolute; top: 41px; left: 150px; height: 10px; font-size: 11px; width: 90px; color: red;");
         if (!empleadoDesdeParametroL.getStyle().contains(" color: red;")) {
            empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle() + " color: red;");
         }
      } else {
         try {
            System.out.println("empleadoDesdeParametro: " + empleadoDesdeParametroL);
            if (empleadoDesdeParametroL.getStyle().contains(" color: red;")) {

               System.out.println("reeemplazarr " + empleadoDesdeParametroL.getStyle().replace(" color: red;", ""));
               empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle().replace(" color: red;", ""));
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");

      System.out.println("reporteSeleccionado.getEmhasta(): " + inforreporteSeleccionado.getEmhasta());
      if (inforreporteSeleccionado.getEmhasta().equals("SI")) {
         empleadoHastaParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametroL");
         //empleadoHastaParametro.setStyle("position: absolute; top: 41px; left: 330px; height: 10px; font-size: 11px; width: 90px; color: red;");
         empleadoHastaParametroL.setStyle(empleadoHastaParametroL.getStyle() + "color: red;");
         RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
      }
      RequestContext.getCurrentInstance().update("formParametros");
      // RequestContext.getCurrentInstance().update("form:reportesNomina");
   }

   public void requisitosParaReporte() {
      requisitosReporte = "";
      if (inforreporteSeleccionado.getFecdesde().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Fecha Desde -";
      }
      if (inforreporteSeleccionado.getFechasta().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Fecha Hasta -";
      }
      if (inforreporteSeleccionado.getEmdesde().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Empleado Desde -";
      }
      if (inforreporteSeleccionado.getEmhasta().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Empleado Hasta -";
      }
      setRequisitosReporte(requisitosReporte);
      if (!requisitosReporte.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
         RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
      }
   }

   public void modificacionTipoReporte(Inforeportes reporte) {
      inforreporteSeleccionado = reporte;
      cambiosReporte = false;
      if (listaInfoReportesModificados.isEmpty()) {
         listaInfoReportesModificados.add(inforreporteSeleccionado);
      } else if ((!listaInfoReportesModificados.isEmpty()) && (!listaInfoReportesModificados.contains(inforreporteSeleccionado))) {
         listaInfoReportesModificados.add(inforreporteSeleccionado);
      } else {
         int posicion = listaInfoReportesModificados.indexOf(inforreporteSeleccionado);
         listaInfoReportesModificados.set(posicion, inforreporteSeleccionado);
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void generarReporte(Inforeportes reporte) {
      System.out.println(this.getClass().getName() + ".generarReporte()");
      inforreporteSeleccionado = reporte;
      seleccionRegistro();
      RequestContext.getCurrentInstance().execute("PF('generandoReporte').show();");
      generarDocumentoReporte();
   }

   public AsynchronousFilllListener listener() {
      System.out.println(this.getClass().getName() + ".listener()");
      return new AsynchronousFilllListener() {
         //RequestContext context = c;

         @Override
         public void reportFinished(JasperPrint jp) {
            System.out.println(this.getClass().getName() + ".listener().reportFinished()");
            try {
               estadoReporte = true;
               resultadoReporte = "Exito";
               /*
                     * final RequestContext currentInstance =
                     * RequestContext.getCurrentInstance();
                     * Renderer.instance().render(template);
                     * RequestContext.setCurrentInstance(currentInstance)
                */
               // RequestContext.getCurrentInstance().execute("PF('formDialogos:generandoReporte");
               //generarArchivoReporte(jp);
            } catch (Exception e) {
               System.out.println("ControlNReporteNomina reportFinished ERROR: " + e.toString());
            }
         }

         @Override
         public void reportCancelled() {
            System.out.println(this.getClass().getName() + ".listener().reportCancelled()");
            estadoReporte = true;
            resultadoReporte = "Cancelación";
         }

         @Override
         public void reportFillError(Throwable e) {
            System.out.println(this.getClass().getName() + ".listener().reportFillError()");
            if (e.getCause() != null) {
               pathReporteGenerado = "ControlNReportePersonal reportFillError Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
               pathReporteGenerado = "ControlNReportePersonal reportFillError Error: " + e.toString();
            }
            estadoReporte = true;
            resultadoReporte = "Se estallo";
         }
      };
   }

   public void exportarReporte() throws IOException {
      if (pathReporteGenerado != null) {
         File reporte = new File(pathReporteGenerado);
         FacesContext ctx = FacesContext.getCurrentInstance();
         FileInputStream fis = new FileInputStream(reporte);
         byte[] bytes = new byte[1024];
         int read;
         if (!ctx.getResponseComplete()) {
            String fileName = reporte.getName();
            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream out = response.getOutputStream();

            while ((read = fis.read(bytes)) != -1) {
               out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            ctx.responseComplete();
         }
      }
   }

   public void generarDocumentoReporte() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (inforreporteSeleccionado != null) {
         System.out.println("generando reporte - ingreso al if");
         nombreReporte = inforreporteSeleccionado.getNombrereporte();
         tipoReporte = inforreporteSeleccionado.getTipo();

         if (nombreReporte != null && tipoReporte != null) {
            System.out.println("generando reporte - ingreso al 2 if");
            pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
         }
         if (pathReporteGenerado != null) {
            System.out.println("generando reporte - ingreso al 3 if");
            validarDescargaReporte();
         } else {
            System.out.println("generando reporte - ingreso al 3 if else");
            RequestContext.getCurrentInstance().execute("PF('generandoReporte.hide();");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show();");
         }
      } else {
         System.out.println("generando reporte - ingreso al if else");
         System.out.println("Reporte Seleccionado es nulo");
      }
   }

   public void validarDescargaReporte() {
      System.out.println(this.getClass().getName() + ".validarDescargaReporte()");
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
      if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
         System.out.println("validar descarga reporte - ingreso al if 1");
         if (!tipoReporte.equals("PDF")) {
            System.out.println("validar descarga reporte - ingreso al if 2");
            RequestContext.getCurrentInstance().execute("PF('descargarReporte').show()");
         } else {
            System.out.println("validar descarga reporte - ingreso al if 2 else");
            FileInputStream fis;
            try {
               System.out.println("pathReporteGenerado : " + pathReporteGenerado);
               fis = new FileInputStream(new File(pathReporteGenerado));
               System.out.println("fis : " + fis);
               file = new DefaultStreamedContent(fis, "application/pdf");
            } catch (FileNotFoundException ex) {
               System.out.println("validar descarga reporte - ingreso al catch 1");
               System.out.println(ex);
               file = null;
            }
            if (file != null) {
               System.out.println("validar descarga reporte - ingreso al if 3");
               if (inforreporteSeleccionado != null) {
                  System.out.println("validar descarga reporte - ingreso al if 4");
                  cabezeraVisor = "Reporte - " + nombreReporte;
               } else {
                  System.out.println("validar descarga reporte - ingreso al if 4 else ");
                  cabezeraVisor = "Reporte - ";
               }
               RequestContext.getCurrentInstance().update("formDialogos:verReportePDF");
               RequestContext.getCurrentInstance().execute("PF('verReportePDF').show()");
            }
            pathReporteGenerado = null;
         }
      } else {
         System.out.println("validar descarga reporte - ingreso al if 1 else");
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public void dispararDialogoGuardarCambios() {
      RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

   }

   public void cancelarReporte() {
      System.out.println(this.getClass().getName() + ".cancelarReporte()");
      administarReportes.cancelarReporte();
   }

   public void defaultPropiedadesParametrosReporte() {
      color = "black";
      decoracion = "none";
      color2 = "black";
      decoracion2 = "none";
   }

   public void reiniciarStreamedContent() {
      System.out.println(this.getClass().getName() + ".reiniciarStreamedContent()");
      file = null;
   }

   public void mostrarDialogoBuscarReporte() {
      try {
         if (cambiosReporte == true) {
            contarRegistrosLovReportes();
            RequestContext.getCurrentInstance().update("formDialogos:ReportesDialogo");
            RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').show()");
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
         }
      } catch (Exception e) {
         System.out.println("Error mostrarDialogoBuscarReporte : " + e.toString());
      }
   }

   public void mostrarTodos() {
      System.out.println(this.getClass().getName() + ".mostrarTodos()");
      if (cambiosReporte == true) {
         defaultPropiedadesParametrosReporte();
         listaIR.clear();
         for (int i = 0; i < listInforeportes.size(); i++) {
            listaIR.add(listInforeportes.get(i));
         }
         RequestContext context = RequestContext.getCurrentInstance();
         activoBuscarReporte = false;
         activoMostrarTodos = true;
         RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
         RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
         RequestContext.getCurrentInstance().update("form:reportesContabilidad");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
      }
   }

   public void cancelarRequisitosReporte() {
      requisitosReporte = "";
   }

   public void guardarYSalir() {
      guardarCambios();
      salir();
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void actualizarEmpresa() {
      System.out.println(this.getClass().getName() + ".actualizarEmpresa()");
      permitirIndex = true;
      parametroDeReporte.setEmpresa(empresaSeleccionada);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
      empresaSeleccionada = null;
      aceptar = true;
      filtrarListEmpresas = null;

   }

   public void cancelarEmpresa() {
      System.out.println(this.getClass().getName() + ".cancelarEmpresa()");
      empresaSeleccionada = null;
      aceptar = true;
      filtrarListEmpresas = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
   }

   public void actualizarProceso() {
      permitirIndex = true;
      parametroDeReporte.setProceso(procesoSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      procesoSeleccionado = null;
      aceptar = true;
      filtrarListProcesos = null;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovProceso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:procesoParametroL");
   }

   public void cancelarCambioProceso() {
      procesoSeleccionado = null;
      aceptar = true;
      filtrarListProcesos = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovProceso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
   }

   public void actualizarEmplDesde() {
      permitirIndex = true;
      parametroDeReporte.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
      parametroModificacion = parametroDeReporte;
      empleadoSeleccionado = null;
      aceptar = true;
      filtrarListEmpleados = null;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");
   }

   public void cancelarCambioEmplDesde() {
      empleadoSeleccionado = null;
      aceptar = true;
      filtrarListEmpleados = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
   }

   public void actualizarEmplHasta() {
      permitirIndex = true;
      parametroDeReporte.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
      parametroModificacion = parametroDeReporte;
      empleadoSeleccionado = null;
      aceptar = true;
      filtrarListEmpleados = null;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
   }

   public void cancelarCambioEmplHasta() {
      empleadoSeleccionado = null;
      aceptar = true;
      filtrarListEmpleados = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
   }

   public void actualizarSeleccionInforeporte() {
      System.out.println(this.getClass().getName() + ".actualizarSeleccionInforeporte()");
      RequestContext context = RequestContext.getCurrentInstance();
      if (bandera == 1) {
         cerrarFiltrado();
      }
      defaultPropiedadesParametrosReporte();
      listaIR.clear();
      listaIR.add(reporteLovSeleccionado);
      filtrarListInforeportesUsuario = null;
      filtrarLovInforeportes = null;
      aceptar = true;
      activoBuscarReporte = true;
      activoMostrarTodos = false;
      inforreporteSeleccionado = null;
      reporteLovSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
      context.reset("formDialogos:lovReportesDialogo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:reportesContabilidad");
      contarRegistros();
   }

   public void cancelarSeleccionInforeporte() {
      System.out.println(this.getClass().getName() + ".cancelarSeleccionInforeporte()");
      filtrarListInforeportesUsuario = null;
      reporteLovSeleccionado = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovReportesDialogo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
   }

   public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
      RequestContext context = RequestContext.getCurrentInstance();
      int indiceUnicoElemento = -1;
      int coincidencias = 0;
      if (campoConfirmar.equalsIgnoreCase("PROCESO")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getProceso().setDescripcion(proceso);
            for (int i = 0; i < listProcesos.size(); i++) {
               if (listProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setProceso(listProcesos.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               listProcesos.clear();
               getListProcesos();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formDialogos:ProcesoDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
            }
         } else {
            parametroDeReporte.setProceso(new Procesos());
            parametroModificacion = parametroDeReporte;
            listProcesos.clear();
            getListProcesos();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("EMPRESA")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getEmpresa().setNombre(empresa);
            for (int i = 0; i < listEmpresas.size(); i++) {
               if (listEmpresas.get(i).getNombre().startsWith(valorConfirmar)) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setEmpresa(listEmpresas.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               listEmpresas.clear();
               getListEmpresas();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
               RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            }
         } else {
            parametroDeReporte.setEmpresa(new Empresas());
            parametroModificacion = parametroDeReporte;
            listEmpresas.clear();
            getListEmpresas();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void modificarParametroEmpleadoDesde(BigDecimal empldesde) {
      if (empldesde.equals(BigDecimal.valueOf(0))) {
         parametroDeReporte.setCodigoempleadodesde(BigDecimal.valueOf(0));
      }
      cambiosReporte = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void modificarParametroEmpleadoHasta(BigDecimal emphasta) {
      String h = "99999999999999999999999999";
      BigDecimal b = new BigDecimal(h);
      if (emphasta.equals(b)) {
         parametroDeReporte.setCodigoempleadodesde(b);
      }
      cambiosReporte = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void posicionCelda(int i) {
      if (permitirIndex) {
         casilla = i;
         switch (casilla) {
            case 1:
               fechaDesde = parametroDeReporte.getFechadesde();
               break;
            case 2:
               emplDesde = parametroDeReporte.getCodigoempleadodesde();
               break;
            case 3:
               fechaHasta = parametroDeReporte.getFechahasta();
               break;
            case 4:
               emplHasta = parametroDeReporte.getCodigoempleadohasta();
               break;
            case 5:
               proceso = parametroDeReporte.getProceso().getDescripcion();
               break;
            default:
               break;
         }
         casillaInforReporte = -1;
      }
   }

   public void modificarParametroInforme() {
      if (parametroDeReporte.getFechadesde() != null && parametroDeReporte.getFechahasta() != null) {
         if (parametroDeReporte.getFechadesde().before(parametroDeReporte.getFechahasta())) {
            System.out.println("Entre al segundo if");
            parametroModificacion = parametroDeReporte;
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            System.out.println("entre a else del segundo if");
            cambiosReporte = true;
         }
      }
   }

   public void mostrarDialogoGenerarReporte() {
      RequestContext.getCurrentInstance().update("formDialogos:reporteAGenerar");
      RequestContext.getCurrentInstance().execute("PF('reporteAGenerar').show()");
   }

   public void cancelarGenerarReporte() {
      reporteGenerar = "";
      posicionReporte = -1;
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLovReportes() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroReportes");
   }

   public void contarRegistrosEmpeladoD() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpleadoDesde");
   }

   public void contarRegistrosEmpeladoH() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpleadoHasta");
   }

   public void contarRegistrosProceso() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroProceso");
   }

   public void contarRegistrosEmpresa() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpresa");
   }

//******************GETTER AND SETTER***************************
   public ParametrosReportes getParametroDeReporte() {
      try {
         if (parametroDeReporte == null) {
            parametroDeReporte = new ParametrosReportes();
            parametroDeReporte = administrarNReporteContabilidad.parametrosDeReporte();
         }

         if (parametroDeReporte.getProceso() == null) {
            parametroDeReporte.setProceso(new Procesos());
         }
         return parametroDeReporte;
      } catch (Exception e) {
         System.out.println("Error getParametroDeInforme : " + e);
         return null;
      }
   }

   public void setParametroDeReporte(ParametrosReportes parametroDeReporte) {
      this.parametroDeReporte = parametroDeReporte;
   }

   public List<Inforeportes> getListaIR() {
      try {
         if (listaIR == null) {
            listaIR = administrarNReporteContabilidad.listInforeportesUsuario();
         }
         return listaIR;
      } catch (Exception e) {
         System.out.println("Error getListInforeportesUsuario : " + e);
         return null;
      }
   }

   public void setListaIR(List<Inforeportes> listaIR) {
      this.listaIR = listaIR;
   }

   public List<Inforeportes> getFiltrarListInforeportesUsuario() {
      return filtrarListInforeportesUsuario;
   }

   public void setFiltrarListInforeportesUsuario(List<Inforeportes> filtrarListInforeportesUsuario) {
      this.filtrarListInforeportesUsuario = filtrarListInforeportesUsuario;
   }

   public Inforeportes getInforreporteSeleccionado() {
      return inforreporteSeleccionado;
   }

   public void setInforreporteSeleccionado(Inforeportes inforreporteSeleccionado) {
      this.inforreporteSeleccionado = inforreporteSeleccionado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public ParametrosReportes getParametroModificacion() {
      return parametroModificacion;
   }

   public void setParametroModificacion(ParametrosReportes parametroModificacion) {
      this.parametroModificacion = parametroModificacion;
   }

   public String getReporteGenerar() {
      if (posicionReporte != -1) {
         reporteGenerar = listaIR.get(posicionReporte).getNombre();
      }
      return reporteGenerar;
   }

   public void setReporteGenerar(String reporteGenerar) {
      this.reporteGenerar = reporteGenerar;
   }

   public String getRequisitosReporte() {
      return requisitosReporte;
   }

   public void setRequisitosReporte(String requisitosReporte) {
      this.requisitosReporte = requisitosReporte;
   }

   public List<Empleados> getListEmpleados() {
      if (listEmpleados == null) {
         listEmpleados = administrarNReporteContabilidad.listEmpleados();
      }
      return listEmpleados;
   }

   public void setListEmpleados(List<Empleados> listEmpleados) {
      this.listEmpleados = listEmpleados;
   }

   public List<Empleados> getFiltrarListEmpleados() {
      return filtrarListEmpleados;
   }

   public void setFiltrarListEmpleados(List<Empleados> filtrarListEmpleados) {
      this.filtrarListEmpleados = filtrarListEmpleados;
   }

   public Empleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public List<Procesos> getListProcesos() {
      if (listProcesos == null || listProcesos.isEmpty()) {
         listProcesos = administrarNReporteContabilidad.listProcesos();
      }
      return listProcesos;
   }

   public void setListProcesos(List<Procesos> listProcesos) {
      this.listProcesos = listProcesos;
   }

   public Procesos getProcesoSeleccionado() {
      return procesoSeleccionado;
   }

   public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
      this.procesoSeleccionado = procesoSeleccionado;
   }

   public List<Procesos> getFiltrarListProcesos() {
      return filtrarListProcesos;
   }

   public void setFiltrarListProcesos(List<Procesos> filtrarListProcesos) {
      this.filtrarListProcesos = filtrarListProcesos;
   }

   public List<Empresas> getListEmpresas() {
      if (listEmpresas == null || listEmpresas.isEmpty()) {
         listEmpresas = administrarNReporteContabilidad.listEmpresas();
      }
      return listEmpresas;
   }

   public void setListEmpresas(List<Empresas> listEmpresas) {
      this.listEmpresas = listEmpresas;
   }

   public List<Empresas> getFiltrarListEmpresas() {
      return filtrarListEmpresas;
   }

   public void setFiltrarListEmpresas(List<Empresas> filtrarListEmpresas) {
      this.filtrarListEmpresas = filtrarListEmpresas;
   }

   public List<Inforeportes> getListInforeportes() {
      if (listInforeportes == null || listInforeportes.isEmpty()) {
         listInforeportes = administrarNReporteContabilidad.listInforeportesUsuario();
      }
      return listInforeportes;
   }

   public void setListInforeportes(List<Inforeportes> listInforeportes) {
      this.listInforeportes = listInforeportes;
   }

   public boolean isCambiosReporte() {
      return cambiosReporte;
   }

   public void setCambiosReporte(boolean cambiosReporte) {
      this.cambiosReporte = cambiosReporte;
   }

   public List<Inforeportes> getListaInfoReportesModificados() {
      return listaInfoReportesModificados;
   }

   public void setListaInfoReportesModificados(List<Inforeportes> listaInfoReportesModificados) {
      this.listaInfoReportesModificados = listaInfoReportesModificados;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public StreamedContent getFile() {
      return file;
   }

   public void setFile(StreamedContent file) {
      this.file = file;
   }

   public String getColor() {
      return color;
   }

   public void setColor(String color) {
      this.color = color;
   }

   public String getDecoracion() {
      return decoracion;
   }

   public void setDecoracion(String decoracion) {
      this.decoracion = decoracion;
   }

   public String getColor2() {
      return color2;
   }

   public void setColor2(String color) {
      this.color2 = color;
   }

   public String getDecoracion2() {
      return decoracion2;
   }

   public void setDecoracion2(String decoracion) {
      this.decoracion2 = decoracion;
   }

   public String getInfoRegistroReportes() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovReportesDialogo");
      infoRegistroReportes = String.valueOf(tabla.getRowCount());
      return infoRegistroReportes;
   }

   public String getInfoRegistroProceso() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovProceso");
      infoRegistroProceso = String.valueOf(tabla.getRowCount());
      return infoRegistroProceso;
   }

   public String getInfoRegistroEmpleadoDesde() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpleadoDesde");
      infoRegistroEmpleadoDesde = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadoDesde;
   }

   public String getInfoRegistroEmpleadoHasta() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpleadoHasta");
      infoRegistroEmpleadoHasta = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadoHasta;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:reportesContabilidad");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public String getInfoRegistroEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpresas");
      infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresa;
   }

   public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
      this.infoRegistroEmpresa = infoRegistroEmpresa;
   }

   public boolean isActivoMostrarTodos() {
      return activoMostrarTodos;
   }

   public void setActivoMostrarTodos(boolean activoMostrarTodos) {
      this.activoMostrarTodos = activoMostrarTodos;
   }

   public boolean isActivoBuscarReporte() {
      return activoBuscarReporte;
   }

   public void setActivoBuscarReporte(boolean activoBuscarReporte) {
      this.activoBuscarReporte = activoBuscarReporte;
   }

   public Inforeportes getReporteLovSeleccionado() {
      return reporteLovSeleccionado;
   }

   public void setReporteLovSeleccionado(Inforeportes reporteLovSeleccionado) {
      this.reporteLovSeleccionado = reporteLovSeleccionado;
   }

   public List<Inforeportes> getFiltrarLovInforeportes() {
      return filtrarLovInforeportes;
   }

   public void setFiltrarLovInforeportes(List<Inforeportes> filtrarLovInforeportes) {
      this.filtrarLovInforeportes = filtrarLovInforeportes;
   }

   public List<Inforeportes> getFiltrarReportes() {
      return filtrarReportes;
   }

   public void setFiltrarReportes(List<Inforeportes> filtrarReportes) {
      this.filtrarReportes = filtrarReportes;
   }

   public Empresas getEmpresaSeleccionada() {
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
      this.empresaSeleccionada = empresaSeleccionada;
   }

   public String getPathReporteGenerado() {
      return pathReporteGenerado;
   }

   public void setPathReporteGenerado(String pathReporteGenerado) {
      this.pathReporteGenerado = pathReporteGenerado;
   }

   public String getCabezeraVisor() {
      return cabezeraVisor;
   }

   public void setCabezeraVisor(String cabezeraVisor) {
      this.cabezeraVisor = cabezeraVisor;
   }

   public boolean isActivarEnvio() {
      return activarEnvio;
   }

   public void setActivarEnvio(boolean activarEnvio) {
      this.activarEnvio = activarEnvio;
   }

}
