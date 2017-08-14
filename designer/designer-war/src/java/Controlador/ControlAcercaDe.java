/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empresas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPapelesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlAcercaDe implements Serializable {

   private static Logger log = Logger.getLogger(ControlAcercaDe.class);

   @EJB
   AdministrarPapelesInterface administrarPapeles;

   private boolean aceptar;

//EMPRESA
   private List<Empresas> listaEmpresas;
   private List<Empresas> filtradoListaEmpresas;

   private Empresas empresaSeleccionada;
   private Empresas nuevoEmpresas;
   private String correo;
   private String version;
   private String grh;
   private String licensiaDeUso;
   private String textoCopyRight;
   private String correo1;
   private String correo2;
   private String derechos;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private String intcontable;
   private String infoRegistro;

   /**
    * Creates a new instance of ControlAcercaDe
    */
   public ControlAcercaDe() {
      listaEmpresas = null;
      empresaSeleccionada = null;
      aceptar = true;
      filtradoListaEmpresas = null;
      correo = "www.nomina.com.co";
      version = "Version 2017";
      grh = "Gerencia Integral de Recursos Humanos       y Administración de Nómina";
      licensiaDeUso = "Acme Corp";
      textoCopyRight = "ADVERTENCIA:\n"
              + "Este programa está protegido por leyes y tratados nacionales e internacionales. La reproducción o distribución no autorizada de este programa o de parte del mismo dará lugar a graves penalizaciones tanto civiles como penales y será objeto de cuantas acciones judiciales correspondan en derecho.\n"
              + "-----------------------------\n"
              + "MÓDULOS\n"
              + "Designer Personal \n"
              + "Designer Nómina\n"
              + "Designer Integración\n"
              + "Designer Gerencial\n"
              + "Designer Administración\n"
              + "---------------------------\n"
              + "ESTE PRODUCTO SE HA CREADO USANDO:\n"
              + "Glassfish [32 bits] Versión 3.1.2 (Community version)\n"
              + "JasperReports [32 bits] Versión 9.0.2.12.2 (Community version)\n"
              + "NetBeans [32 bits] Versión 7.4\n"
              + "JSF Versión 2\n"
              + "JPA 2 implementación EclipseLink Versión 2.5.0 \n"
              + "Java JEE 6 \n"
              + "Iconos basados en diseños de freepic";
      correo1 = "gerencia@nomina.com.co";
      correo2 = "www.nomina.com.co/wiki";
      derechos = "1998 - 2017 Todos los Derechos Reservados";
      nuevoEmpresas = new Empresas();
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
      String pagActual = "acercade";
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
         administrarPapeles.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void lovEmpresas() {
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cambiarEmpresa() {
      RequestContext context = RequestContext.getCurrentInstance();
//        RequestContext.getCurrentInstance().update("form:nombreEmpresa");
      filtradoListaEmpresas = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
      context.reset("formularioDialogos:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:PanelTotal");
   }

   public void cancelarCambioEmpresa() {
      filtradoListaEmpresas = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
      context.reset("formularioDialogos:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
   }

   public void limpiarNuevoEmpresas() {
      nuevoEmpresas = new Empresas();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ACERCADE", false, false, "UTF-8", null, null);
      context.responseComplete();

   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ACERCADE", false, false, "UTF-8", null, null);
      context.responseComplete();

   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistro");
   }

   /////////SETS Y GETS /////
   public List<Empresas> getListaEmpresas() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         if (listaEmpresas == null) {
            listaEmpresas = administrarPapeles.consultarEmpresas();
            if (!listaEmpresas.isEmpty()) {
               empresaSeleccionada = listaEmpresas.get(0);
            }
            RequestContext.getCurrentInstance().update("form:PanelTotal");
         }
         return listaEmpresas;
      } catch (Exception e) {
         log.warn("Error LISTA EMPRESAS " + e);
         return null;
      }
   }

   public void setListaEmpresas(List<Empresas> listaEmpresas) {
      this.listaEmpresas = listaEmpresas;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public List<Empresas> getFiltradoListaEmpresas() {
      return filtradoListaEmpresas;
   }

   public void setFiltradoListaEmpresas(List<Empresas> filtradoListaEmpresas) {
      this.filtradoListaEmpresas = filtradoListaEmpresas;
   }

   public Empresas getEmpresaSeleccionada() {
      if (empresaSeleccionada == null) {
         getListaEmpresas();
         RequestContext context = RequestContext.getCurrentInstance();
         log.info("EMPRESA SELECCIONADA  : " + empresaSeleccionada.getNombre());
         RequestContext.getCurrentInstance().update("form:PanelTotal");
      }
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
      this.empresaSeleccionada = empresaSeleccionada;
   }

   public String getCorreo() {
      return correo;
   }

   public void setCorreo(String correo) {
      this.correo = correo;
   }

   public String getVersion() {
      return version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getGrh() {
      return grh;
   }

   public void setGrh(String grh) {
      this.grh = grh;
   }

   public String getLicensiaDeUso() {
      return licensiaDeUso;
   }

   public void setLicensiaDeUso(String licensiaDeUso) {
      this.licensiaDeUso = licensiaDeUso;
   }

   public String getTextoCopyRight() {
      return textoCopyRight;
   }

   public void setTextoCopyRight(String textoCopyRight) {
      this.textoCopyRight = textoCopyRight;
   }

   public String getCorreo1() {
      return correo1;
   }

   public void setCorreo1(String correo1) {
      this.correo1 = correo1;
   }

   public String getCorreo2() {
      return correo2;
   }

   public void setCorreo2(String correo2) {
      this.correo2 = correo2;
   }

   public String getDerechos() {
      return derechos;
   }

   public void setDerechos(String derechos) {
      this.derechos = derechos;
   }

   public String getIntcontable() {
      if (empresaSeleccionada != null) {
         intcontable = administrarPapeles.interfaceContable(empresaSeleccionada.getSecuencia());
      }
      return intcontable;
   }

   public void setIntcontable(String intcontable) {
      this.intcontable = intcontable;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEmpresas");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }
}
