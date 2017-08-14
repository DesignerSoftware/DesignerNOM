package Controlador;

import Exportar.ExportarPDF;
import InterfaceAdministrar.AdministrarGeneraConsultaInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.*;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;

/**
 *
 * @author Edwin Hastamorir
 */
@ManagedBean
@SessionScoped
public class ControlGeneraConsulta implements Serializable {

   private static Logger log = Logger.getLogger(ControlGeneraConsulta.class);

   @EJB
   AdministrarGeneraConsultaInterface administrarGeneraConsulta;

   private String secuencia;
   private List<String> listaConsultas;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlGeneraConsulta() {
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
      String pagActual = "generaconsulta";
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
      administrarGeneraConsulta.salir();
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }

   @PostConstruct
   public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
      log.info("ControlGeneraConsulta.inicializarAdministrador");
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarGeneraConsulta.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e + " " + "Causa: " + e.getCause());
      }
   }

   public void obtieneConsulta() {
      log.info("ControlGeneraConsulta.obtieneConsulta");
      log.info("SECUENCIA: " + this.secuencia);
      listaConsultas = administrarGeneraConsulta.ejecutarConsulta(new BigInteger(this.secuencia));
      /*try {
            exportPDF();
        } catch (IOException ex) {
            log.info("obtieneConsulta en "+this.getClass().getName());
            ex.printStackTrace();
        }*/
   }

   public void exportPDF() throws IOException {
      DataTable tablaEx = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principal:datos");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tablaEx, "consultasRecordatoriosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public String getSecuencia() {
      log.info("ControlGeneraConsulta.getSecuencia");
      return secuencia;
   }

   public void setSecuencia(String secuencia) {
      log.info("ControlGeneraConsulta.setSecuencia");
      this.secuencia = secuencia;
   }

   public List<String> getListaConsultas() {
      return listaConsultas;
   }

   public void setListaConsultas(List<String> listaConsultas) {
      this.listaConsultas = listaConsultas;
   }

}
