package Controlador;

import Entidades.ConsultasLiquidaciones;
import Entidades.ParametrosEstructuras;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarBarraInterface;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class ControlBarra implements Serializable {

   @EJB
   AdministrarBarraInterface administrarBarra;
   private Integer totalEmpleadosParaLiquidar;
   private Integer totalEmpleadosLiquidados;
   private boolean permisoParaLiquidar;
   private String usuarioBD;
   private Integer barra;
   private ParametrosEstructuras parametroEstructura;
   private boolean empezar, botonLiquidar, botonCancelar, cambioImagen;
   private boolean bandera, preparandoDatos;
   private String horaInicialLiquidacion, horaFinalLiquidacion, mensajeBarra, mensajeEstado, imagenEstado;
   private SimpleDateFormat formato, formatoFecha;
   //LIQUIDACIONES CERRADAS - ABIERTAS
   private List<ConsultasLiquidaciones> liquidacionesCerradas, liquidacionesAbiertas, filtradoLiquidacionesCerradas, filtradoLiquidacionesAbiertas;
   private Column corte, empresa, proceso, totalEmpleados, observacion;
   private Column corteLA, empresaLA, procesoLA, totalEmpleadosLA, observacionLA;
   private String altoScrollLiquidacionesCerradas, altoScrollLiquidacionesAbiertas;
   private int banderaFiltros;
   private boolean liquifinalizada;
   private String infoRegistroCerradas, infoRegistroEnProceso;

   public ControlBarra() {
      totalEmpleadosParaLiquidar = 0;
      totalEmpleadosLiquidados = 0;
      barra = 0;
      empezar = false;
      bandera = true;
      preparandoDatos = false;
      botonCancelar = true;
      botonLiquidar = false;
      horaInicialLiquidacion = "--:--:--";
      horaFinalLiquidacion = "--:--:--";
      formato = new SimpleDateFormat("hh:mm:ss a");
      formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
      mensajeEstado = "Oprima el boton liquidar para iniciar.";
      imagenEstado = "nom_parametros_liq.png";
      cambioImagen = true;
      liquidacionesAbiertas = null;
      liquidacionesCerradas = null;
      altoScrollLiquidacionesAbiertas = "125";
      altoScrollLiquidacionesCerradas = "125";
      banderaFiltros = 0;
      liquifinalizada = false;
      infoRegistroCerradas = "0";
      infoRegistroEnProceso = "0";
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarBarra.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void contarLiquidados() {
      totalEmpleadosLiquidados = administrarBarra.contarEmpleadosLiquidados();
      System.out.println("totalEmpleadosLiquidados: " + totalEmpleadosLiquidados);
      RequestContext.getCurrentInstance().update("form:empleadosLiquidados");
   }

   public void liquidar() {
      RequestContext context = RequestContext.getCurrentInstance();
      empezar = true;
      liquifinalizada = false;
      usuarioBD = administrarBarra.consultarUsuarioBD();
      permisoParaLiquidar = administrarBarra.verificarPermisosLiquidar(usuarioBD);
      System.out.println("permisoParaLiquidar : " + permisoParaLiquidar);
      if (permisoParaLiquidar) {
         administrarBarra.inicializarParametrosEstados();
         administrarBarra.liquidarNomina();
         mensajeBarra = "Preparando datos...";
         mensajeEstado = "Un momento mientras se analizan los parametros.";
         imagenEstado = "hand2.png";
         preparandoDatos = true;
         botonLiquidar = true;
         botonCancelar = false;
         Date horaInicio = new Date();
         System.out.println("Hora Inicio: " + formato.format(horaInicio));
         horaInicialLiquidacion = formato.format(horaInicio);
         horaFinalLiquidacion = "--:--:--";
         RequestContext.getCurrentInstance().execute("PF('barra').start()");
         RequestContext.getCurrentInstance().update("form:liquidar");
         RequestContext.getCurrentInstance().update("form:cancelar");
         RequestContext.getCurrentInstance().update("form:horaF");
         RequestContext.getCurrentInstance().update("form:horaI");
         RequestContext.getCurrentInstance().update("form:estadoLiquidacion");
         RequestContext.getCurrentInstance().update("form:imagen");
      } else {
         RequestContext.getCurrentInstance().execute("PF('permisoLiquidacion').show()");
      }
      System.err.println("Termino la funcion liquidar()");
   }

   public void limpiarbarra() {
      barra = null;
      empezar = !empezar;
      liquifinalizada = false;
   }

   public void cancelarLiquidacion() {
      RequestContext.getCurrentInstance().execute("PF('barra').cancel()");
      empezar = false;
      liquifinalizada = true;
      administrarBarra.cancelarLiquidacion(usuarioBD);
      Date horaFinal = new Date();
      horaFinalLiquidacion = formato.format(horaFinal);
      mensajeBarra = "Liquidacion Cancelada (" + barra + "%)";
      mensajeEstado = "El proceso de liquidacion fue cancelado.";
      imagenEstado = "cancelarLiquidacion.png";
      botonCancelar = true;
      botonLiquidar = false;
      FacesMessage msg = new FacesMessage("Información", "Liquidación cancelada.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext.getCurrentInstance().update("form:horaF");
      RequestContext.getCurrentInstance().update("form:liquidar");
      RequestContext.getCurrentInstance().update("form:cancelar");
      RequestContext.getCurrentInstance().update("form:estadoLiquidacion");
      RequestContext.getCurrentInstance().update("form:imagen");
      RequestContext.getCurrentInstance().execute("PF('barra').setValue(" + barra + ")");
      RequestContext.getCurrentInstance().update("form:barra");
      RequestContext.getCurrentInstance().update("form:growl");
      consultarEstadoDatos();
      contarRegistrosCerrada();
      contarRegistrosEnProceso();
      empezar = false;
   }

   public void salir() {
      totalEmpleadosParaLiquidar = 0;
      totalEmpleadosLiquidados = 0;
      barra = 0;
      mensajeBarra = "";
      empezar = false;
      liquifinalizada = false;
      bandera = true;
      preparandoDatos = false;
      botonCancelar = true;
      botonLiquidar = false;
      horaInicialLiquidacion = "--:--:--";
      horaFinalLiquidacion = "--:--:--";
      formato = new SimpleDateFormat("hh:mm:ss a");
      mensajeEstado = "Oprima el boton liquidar para iniciar.";
      imagenEstado = "nom_parametros_liq.png";
      cambioImagen = true;
      liquidacionesAbiertas = null;
      liquidacionesCerradas = null;
      filtradoLiquidacionesAbiertas = null;
      filtradoLiquidacionesCerradas = null;
      parametroEstructura = null;
   }

   public void consultarDatos() {
      System.out.println("Entro en consultarDatos() parametroEstructura : " + parametroEstructura);
      if (parametroEstructura != null) {
         liquidacionesCerradas = administrarBarra.liquidacionesCerradas(formatoFecha.format(parametroEstructura.getFechadesdecausado()), formatoFecha.format(parametroEstructura.getFechahastacausado()));
      }
      liquidacionesAbiertas = administrarBarra.consultarPreNomina();
      contarRegistrosCerrada();
      contarRegistrosEnProceso();
      RequestContext.getCurrentInstance().update("form:datosLiquidacionesCerradas");
      RequestContext.getCurrentInstance().update("form:datosLiquidacionesAbiertas");
   }

   public void consultarEstadoDatos() {
      if (parametroEstructura != null && administrarBarra.consultarEstadoConsultaDatos(parametroEstructura.getEstructura().getOrganigrama().getEmpresa().getSecuencia()).equals("S")) {
         consultarDatos();
      }
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR FILTROS
   public void activarCtrlF11() {
      if (liquidacionesAbiertas != null && liquidacionesCerradas != null) {
         if (banderaFiltros == 0) {
            //LIQUIDACIONES CERRADAS
            corte = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:corte");
            corte.setFilterStyle("width: 85% !important;");
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:empresa");
            empresa.setFilterStyle("width: 85% !important;");
            proceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:proceso");
            proceso.setFilterStyle("width: 85% !important;");
            totalEmpleados = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:totalEmpleados");
            totalEmpleados.setFilterStyle("width: 85% !important;");
            observacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:observacion");
            observacion.setFilterStyle("width: 85% !important;");

            //LIQUIDACIONES ABIERTAS
            corteLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:corteLA");
            corteLA.setFilterStyle("width: 85% !important;");
            empresaLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:empresaLA");
            empresaLA.setFilterStyle("width: 85% !important;");
            procesoLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:procesoLA");
            procesoLA.setFilterStyle("width: 85% !important;");
            totalEmpleadosLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:totalEmpleadosLA");
            totalEmpleadosLA.setFilterStyle("width: 85% !important;");
            observacionLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:observacionLA");
            observacionLA.setFilterStyle("width: 85% !important;");

            altoScrollLiquidacionesCerradas = "105";
            altoScrollLiquidacionesAbiertas = "105";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesCerradas");
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesAbiertas");
            banderaFiltros = 1;

         } else if (banderaFiltros == 1) {
            //LIQUIDACIONES CERRADAS
            corte = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:corte");
            corte.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            proceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:proceso");
            proceso.setFilterStyle("display: none; visibility: hidden;");
            totalEmpleados = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:totalEmpleados");
            totalEmpleados.setFilterStyle("display: none; visibility: hidden;");
            observacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:observacion");
            observacion.setFilterStyle("display: none; visibility: hidden;");

            //LIQUIDACIONES ABIERTAS
            corteLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:corteLA");
            corteLA.setFilterStyle("display: none; visibility: hidden;");
            empresaLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:empresaLA");
            empresaLA.setFilterStyle("display: none; visibility: hidden;");
            procesoLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:procesoLA");
            procesoLA.setFilterStyle("display: none; visibility: hidden;");
            totalEmpleadosLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:totalEmpleadosLA");
            totalEmpleadosLA.setFilterStyle("display: none; visibility: hidden;");
            observacionLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:observacionLA");
            observacionLA.setFilterStyle("display: none; visibility: hidden;");

            altoScrollLiquidacionesCerradas = "125";
            altoScrollLiquidacionesAbiertas = "125";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesCerradas");
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesAbiertas");
            banderaFiltros = 0;
            filtradoLiquidacionesAbiertas = null;
            filtradoLiquidacionesCerradas = null;
         }
      }
   }

   public void exportPDF(int tablaExportar) throws IOException {
      DataTable tabla;
      Exporter exporter = new ExportarPDF();
      FacesContext context = FacesContext.getCurrentInstance();
      if (tablaExportar == 0) {
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLiquidacionesCerradasExportar");
         exporter.export(context, tabla, "LiquidacionesCerradasPDF", false, false, "UTF-8", null, null);
      } else {
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLiquidacionesAbiertasExportar");
         exporter.export(context, tabla, "LiquidacionesPrenominaPDF", false, false, "UTF-8", null, null);
      }
      context.responseComplete();
   }

   public void exportXLS(int tablaExportar) throws IOException {
      DataTable tabla;
      Exporter exporter = new ExportarXLS();
      FacesContext context = FacesContext.getCurrentInstance();
      if (tablaExportar == 0) {
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLiquidacionesCerradasExportar");
         exporter.export(context, tabla, "LiquidacionesCerradasXLS", false, false, "UTF-8", null, null);
      } else {
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLiquidacionesAbiertasExportar");
         exporter.export(context, tabla, "LiquidacionesPrenominaXLS", false, false, "UTF-8", null, null);
      }
      context.responseComplete();
   }

   public void contarRegistrosCerrada() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCerradas");
   }

   public void contarRegistrosEnProceso() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEnProceso");
   }

   //GETTER AND SETTER
   public Integer getTotalEmpleadosParaLiquidar() {
      totalEmpleadosParaLiquidar = administrarBarra.contarEmpleadosParaLiquidar();
      if (totalEmpleadosParaLiquidar == 0) {
         botonLiquidar = true;
      } else {
         botonLiquidar = false;
      }
      return totalEmpleadosParaLiquidar;
   }

   public void setTotalEmpleadosParaLiquidar(Integer totalEmpleadosParaLiquidar) {
      this.totalEmpleadosParaLiquidar = totalEmpleadosParaLiquidar;
   }

   public Integer getTotalEmpleadosLiquidados() {
      return totalEmpleadosLiquidados;
   }

   public void setTotalEmpleadosLiquidados(Integer totalEmpleadosLiquidados) {
      this.totalEmpleadosLiquidados = totalEmpleadosLiquidados;
   }

   public Integer getBarra() {
      System.out.println("Entra GetBarra");
      if (empezar == true) {
         String estado = administrarBarra.consultarEstadoLiquidacion(usuarioBD);
         System.out.println("Estado: 1" + estado);
         if (preparandoDatos == true) {
            barra = 101;
            RequestContext.getCurrentInstance().update("form:barra");
            if (!estado.equalsIgnoreCase("INICIADO") && !estado.equalsIgnoreCase("EN COLA")) {
               preparandoDatos = false;
               barra = null;
               bandera = true;
               mensajeEstado = "Liquidando personal.";
               RequestContext.getCurrentInstance().update("form:estadoLiquidacion");
            }
         } else if (barra == null) {
            barra = 0;
         } else {
            barra = administrarBarra.consultarProgresoLiquidacion(totalEmpleadosParaLiquidar);
            if (!estado.equalsIgnoreCase("FINALIZADO")) {
               System.out.println("NO ES FINALIZADO");
               if (barra >= 100) {
                  barra = 100;
                  bandera = false;
               }
               mensajeBarra = "Liquidando... " + barra + "%";
               RequestContext.getCurrentInstance().update("form:barra");
               RequestContext.getCurrentInstance().update("form:estadoLiquidacion");
               System.out.println("Liquidando... " + barra + "%");
               if (bandera == true) {
                  if (cambioImagen == true) {
                     cambioImagen = false;
                     imagenEstado = "hand2.png";
                  } else {
                     cambioImagen = true;
                     imagenEstado = "hand1.png";
                  }
                  Date horaFinal = new Date();
                  horaFinalLiquidacion = formato.format(horaFinal);
                  RequestContext.getCurrentInstance().update("form:barra");
                  RequestContext.getCurrentInstance().update("form:horaF");
                  RequestContext.getCurrentInstance().update("form:imagen");
                  contarLiquidados();
               }
            } else {
               System.out.println("ES FINALIZADO");
               if (barra < 100) {
                  System.out.println("Liquidacion Terminada Parcialmente");
                  RequestContext.getCurrentInstance().execute("PF('barra').cancel()");
                  empezar = false;
                  liquifinalizada = true;
                  Date horaFinal = new Date();
                  horaFinalLiquidacion = formato.format(horaFinal);
                  mensajeBarra = "Liquidacion Finalizada (" + barra + "%)";
                  mensajeEstado = "Liquidacion terminada parcialmente.";
                  imagenEstado = "hand3.png";
                  botonCancelar = true;
                  botonLiquidar = false;
                  FacesMessage msg = new FacesMessage("Información", "Liquidación terminada parcialmente.");
                  FacesContext.getCurrentInstance().addMessage(null, msg);
                  RequestContext.getCurrentInstance().update("form:horaF");
                  RequestContext.getCurrentInstance().update("form:liquidar");
                  RequestContext.getCurrentInstance().update("form:cancelar");
                  RequestContext.getCurrentInstance().update("form:estadoLiquidacion");
                  RequestContext.getCurrentInstance().update("form:imagen");
                  RequestContext.getCurrentInstance().execute("PF('barra').setValue(" + barra + ")");
                  RequestContext.getCurrentInstance().update("form:barra");
                  RequestContext.getCurrentInstance().update("form:estadoLiquidacion");
                  RequestContext.getCurrentInstance().update("form:growl");
               } else if (barra >= 100) {
                  barra = 100;
                  System.out.println("Esta en teoría completa...Barra: " + barra);
                  liquidacionCompleta();
               }
            }
         }
         System.out.println("Estado: 2" + estado);
      }
      return barra;
   }

   public void liquidacionCompleta() {
      System.err.println("Liquidación Completada");
      mensajeBarra = "Liquidación Completa (" + barra + "%)";
      mensajeEstado = "Liquidación terminada con exito.";
      imagenEstado = "hand3.png";
      empezar = false;
      liquifinalizada = true;
      botonCancelar = true;
      botonLiquidar = false;
      totalEmpleadosLiquidados = administrarBarra.contarEmpleadosLiquidados();
      System.out.println("totalEmpleadosLiquidados: " + totalEmpleadosLiquidados);
      Date horaFinal = new Date();
      System.out.println("Hora Final: " + formato.format(horaFinal));
      horaFinalLiquidacion = formato.format(horaFinal);
      RequestContext context = RequestContext.getCurrentInstance();

      context.update("form:horaF");
      context.update("form:empleadosLiquidados");
      context.update("form:liquidar");
      context.update("form:cancelar");
      context.update("form:estadoLiquidacion");

      System.out.println("1");
      context.update("form:imagen");
      context.update("form:barra");
      context.update("form:panelLiquidacion");
      context.update("form:PanelTotal");
      context.update("form:estadoLiquidacion");

      System.out.println("2");
      if (totalEmpleadosLiquidados == 1) {
         RequestContext.getCurrentInstance().execute("location.reload(true)");
      }
      System.out.println("3");
      FacesMessage msg = new FacesMessage("Información", "Liquidación terminada con Éxito.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      context.update("form:growl");
//      context.execute("PF('barra').stop()");
   }

   public void setBarra(Integer barra) {
      this.barra = barra;
   }

   public ParametrosEstructuras getParametroEstructura() {
      if (parametroEstructura == null) {
         parametroEstructura = administrarBarra.consultarParametrosLiquidacion();
      }
      return parametroEstructura;
   }

   public String getHoraInicialLiquidacion() {
      return horaInicialLiquidacion;
   }

   public String getHoraFinalLiquidacion() {
      return horaFinalLiquidacion;
   }

   public String getMensajeBarra() {
      return mensajeBarra;
   }

   public boolean isBotonLiquidar() {
      return botonLiquidar;
   }

   public boolean isBotonCancelar() {
      return botonCancelar;
   }

   public String getMensajeEstado() {
      return mensajeEstado;
   }

   public String getImagenEstado() {
      return imagenEstado;
   }

   public List<ConsultasLiquidaciones> getLiquidacionesCerradas() {
      return liquidacionesCerradas;
   }

   public List<ConsultasLiquidaciones> getLiquidacionesAbiertas() {
      return liquidacionesAbiertas;
   }

   public List<ConsultasLiquidaciones> getFiltradoLiquidacionesCerradas() {
      return filtradoLiquidacionesCerradas;
   }

   public void setFiltradoLiquidacionesCerradas(List<ConsultasLiquidaciones> filtradoLiquidacionesCerradas) {
      this.filtradoLiquidacionesCerradas = filtradoLiquidacionesCerradas;
   }

   public List<ConsultasLiquidaciones> getFiltradoLiquidacionesAbiertas() {
      return filtradoLiquidacionesAbiertas;
   }

   public void setFiltradoLiquidacionesAbiertas(List<ConsultasLiquidaciones> filtradoLiquidacionesAbiertas) {
      this.filtradoLiquidacionesAbiertas = filtradoLiquidacionesAbiertas;
   }

   public String getAltoScrollLiquidacionesCerradas() {
      return altoScrollLiquidacionesCerradas;
   }

   public String getAltoScrollLiquidacionesAbiertas() {
      return altoScrollLiquidacionesAbiertas;
   }

   public boolean isLiquifinalizada() {
      return liquifinalizada;
   }

   public void setLiquifinalizada(boolean liquifinalizada) {
      this.liquifinalizada = liquifinalizada;
   }

   public String getInfoRegistroCerradas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosLiquidacionesCerradas");
      infoRegistroCerradas = String.valueOf(tabla.getRowCount());
      return infoRegistroCerradas;
   }

   public void setInfoRegistroCerradas(String infoRegistroCerradas) {
      this.infoRegistroCerradas = infoRegistroCerradas;
   }

   public String getInfoRegistroEnProceso() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosLiquidacionesAbiertas");
      infoRegistroEnProceso = String.valueOf(tabla.getRowCount());
      return infoRegistroEnProceso;
   }

   public void setInfoRegistroEnProceso(String infoRegistroEnProceso) {
      this.infoRegistroEnProceso = infoRegistroEnProceso;
   }
}
