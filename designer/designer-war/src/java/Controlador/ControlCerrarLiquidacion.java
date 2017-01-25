package Controlador;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import Entidades.Parametros;
import Entidades.ParametrosEstructuras;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCerrarLiquidacionInterface;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;import ControlNavegacion.ControlListaNavegacion;
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
 * @author -Feliipe-
 */
@ManagedBean
@SessionScoped
public class ControlCerrarLiquidacion implements Serializable {

    @EJB
    AdministrarCerrarLiquidacionInterface administrarCerrarLiquidacion;

    private Integer totalEmpleadosParaLiquidar;
    private boolean permisoParaLiquidar;
    private String usuarioBD;
    private ParametrosEstructuras parametroEstructura;
    private List<Parametros> listaParametros;
    private List<Parametros> filtradoListaParametros;
    private Parametros empleadoTablaSeleccionado;
    private String opcionLiquidacion;
    private SimpleDateFormat formatoFecha;
    //
    private String altoTabla;
    //
    private Column codigoEmpleado, nombreEmpleado;
    private int bandera, tipoLista;
    //
    private String infoRegistro;

    public ControlCerrarLiquidacion() {
        tipoLista = 0;
        bandera = 0;
        altoTabla = "160";
        totalEmpleadosParaLiquidar = 0;
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    }

       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>(); mapParametros.put ("paginaAnterior", paginaAnterior);
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
         String pagActual = "cargo"XXX;
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
            administrarCerrarLiquidacion.obtenerConexion(ses.getId());
            listaParametros = null;
            getListaParametros();
            contarRegistros();
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void tipoLiquidacion(String tipoLiquidacion) {
        opcionLiquidacion = tipoLiquidacion;
    }

    public void confirmarCierreLiquidacion() {
        Integer conteo = administrarCerrarLiquidacion.consultarConteoProcesoSN(parametroEstructura.getProceso().getSecuencia());
        if (conteo == totalEmpleadosParaLiquidar) {
            cerrarLiquidacion();
            RequestContext.getCurrentInstance().execute("formularioDialogos:liquidacionRealizada').show()");
        } else {
            RequestContext.getCurrentInstance().execute("confirmarCerrarConteoFallo').show()");
        }
    }

    public void cerrarLiquidacion() {
        if (opcionLiquidacion.equals("AUTOMATICO")) {
            administrarCerrarLiquidacion.cerrarLiquidacionAutomatico();
        } else if (opcionLiquidacion.equals("NO AUTOMATICO")) {
            administrarCerrarLiquidacion.cerrarLiquidacionNoAutomatico();
        }
        FacesMessage msg = new FacesMessage("Información", "Liquidación cerrada con éxito.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update("form:growl");
    }

    public void confirmarAbrirLiquidacion() {
        String fechaDesde = formatoFecha.format(parametroEstructura.getFechadesdecausado());
        String fechaHasta = formatoFecha.format(parametroEstructura.getFechahastacausado());
        Integer conteo = administrarCerrarLiquidacion.contarLiquidacionesCerradas(parametroEstructura.getProceso().getSecuencia(), fechaDesde, fechaHasta);
        RequestContext context = RequestContext.getCurrentInstance();
        if (conteo > 0) {
            RequestContext.getCurrentInstance().execute("PF('confirmarBorrarComprobante').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorComprobante').show();");
        }
    }

    public void abrirLiquidacion() {
        String fechaDesde = formatoFecha.format(parametroEstructura.getFechadesdecausado());
        String fechaHasta = formatoFecha.format(parametroEstructura.getFechahastacausado());
        administrarCerrarLiquidacion.abrirLiquidacion(parametroEstructura.getProceso().getCodigo(), fechaDesde, fechaHasta);
        FacesMessage msg = new FacesMessage("Información", "Comprobante eliminado exitosamente.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update("form:growl");
    }

    public void exportPDF() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosEmpleadosParametrosExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "EmpleadosLiquidacion_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosEmpleadosParametrosExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "EmpleadosLiqudacion_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void salir() {
        parametroEstructura = null;
        listaParametros = null;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            codigoEmpleado = (Column) c.getViewRoot().findComponent("form:datosEmpleadosParametros:codigoEmpleado");
            codigoEmpleado.setFilterStyle("width: 85% !important");
            nombreEmpleado = (Column) c.getViewRoot().findComponent("form:datosEmpleadosParametros:nombreEmpleado");
            nombreEmpleado.setFilterStyle("width: 85% !important");
            altoTabla = "140";
            RequestContext.getCurrentInstance().update("form:datosEmpleadosParametros");
            bandera = 1;
        } else if (bandera == 1) {
            codigoEmpleado = (Column) c.getViewRoot().findComponent("form:datosEmpleadosParametros:codigoEmpleado");
            codigoEmpleado.setFilterStyle("display: none; visibility: hidden;");
            nombreEmpleado = (Column) c.getViewRoot().findComponent("form:datosEmpleadosParametros:nombreEmpleado");
            nombreEmpleado.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "160";
            RequestContext.getCurrentInstance().update("form:datosEmpleadosParametros");
            bandera = 0;
            tipoLista = 0;
            filtradoListaParametros = null;
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        modificarInfoRegistro(filtradoListaParametros.size());

    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistros(){
        if(listaParametros != null){
            modificarInfoRegistro(listaParametros.size());
        } else {
            modificarInfoRegistro(0);
        }
    }
    
    //GETTER AND SETTER
    public Integer getTotalEmpleadosParaLiquidar() {
        totalEmpleadosParaLiquidar = administrarCerrarLiquidacion.contarEmpleadosParaLiquidar();
        return totalEmpleadosParaLiquidar;
    }

    public void setTotalEmpleadosParaLiquidar(Integer totalEmpleadosParaLiquidar) {
        this.totalEmpleadosParaLiquidar = totalEmpleadosParaLiquidar;
    }

    public boolean isPermisoParaLiquidar() {
        return permisoParaLiquidar;
    }

    public void setPermisoParaLiquidar(boolean permisoParaLiquidar) {
        this.permisoParaLiquidar = permisoParaLiquidar;
    }

    public String getUsuarioBD() {
        usuarioBD = administrarCerrarLiquidacion.consultarAliasUsuarioBD();
        return usuarioBD;
    }

    public void setUsuarioBD(String usuarioBD) {
        this.usuarioBD = usuarioBD;
    }

    public ParametrosEstructuras getParametroEstructura() {
        if (parametroEstructura == null) {
            parametroEstructura = administrarCerrarLiquidacion.consultarParametrosLiquidacion();
        }
        return parametroEstructura;
    }

    public void setParametroEstructura(ParametrosEstructuras parametroEstructura) {
        this.parametroEstructura = parametroEstructura;
    }

    public List<Parametros> getListaParametros() {
        if (listaParametros == null) {
            getUsuarioBD();
            listaParametros = administrarCerrarLiquidacion.consultarEmpleadosCerrarLiquidacion(usuarioBD);
        }
        return listaParametros;
    }

    public void setListaParametros(List<Parametros> listaParametros) {
        this.listaParametros = listaParametros;
    }

    public List<Parametros> getFiltradoListaParametros() {
        return filtradoListaParametros;
    }

    public void setFiltradoListaParametros(List<Parametros> filtradoListaParametros) {
        this.filtradoListaParametros = filtradoListaParametros;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public Parametros getEmpleadoTablaSeleccionado() {
        getListaParametros();
        if (listaParametros != null) {
            int tam = listaParametros.size();
            if (tam > 0) {
                empleadoTablaSeleccionado = listaParametros.get(0);
            }
        }
        return empleadoTablaSeleccionado;
    }

    public void setEmpleadoTablaSeleccionado(Parametros empleadoTablaSeleccionado) {
        this.empleadoTablaSeleccionado = empleadoTablaSeleccionado;
    }

}
