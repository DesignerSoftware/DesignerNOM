package Controlador;


import Entidades.Rastros;
import Entidades.RastrosValores;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;import ControlNavegacion.ControlListaNavegacion;
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

@ManagedBean
@SessionScoped
public class ControlRastro implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Rastros> listaRastros;
    private List<Rastros> filtradoListaRastros;
    private Rastros seleccionRastro;
    private List<Rastros> listaLOVRastros;
    private List<Rastros> filtradoListaLOVRastros;
    private Rastros seleccionRastroLOV;
    private List<Rastros> backUplistaLOVRastros;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Columnas Tabla datosRastros
    private Column nombreTabla, fechaRastro, estacion, usuarioSO, direccionIP, usuarioID, usuarioBD, tipoManipulacion;
    //Parametros
    private String nombreTablaRastro, nomPagina;
    private BigInteger secRegistroT;
    //ESTADO RASTRO (HISTORICO - REGISTRO - ELIMINADOS)
    private int estado;
    //LOV
    private boolean aceptar, btnMostrarTodos;
    //VALOR RASTRO
    private List<RastrosValores> rastroValor;
    //TABLA TIENE CAMPO EMPLEADO
    private boolean campoEmpl;
    //BOTON ELIMINADOS O INSERTADOS Y ACTUALIZADOS
    private String btnValor;
    private int estadoBtn;

    public ControlRastro() {
        listaRastros = null;
        bandera = 0;
        aceptar = true;
        seleccionRastro = null;
        btnMostrarTodos = true;
        campoEmpl = true;
        btnValor = "Consultar Eliminados";
        estadoBtn = 1;
    }
    
       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   mapParametros.put ("paginaAnterior", paginaAnterior);
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
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //PARAMETROS PARA EL RASTRO
    public void recibirDatosTabla(BigInteger secRegistro, String nombreTablaR, String nombrePagina) {
        listaRastros = null;
        secRegistroT = secRegistro;
        nombreTablaRastro = nombreTablaR;
        nomPagina = nombrePagina;
        listaRastros = administrarRastros.rastrosTabla(secRegistroT, nombreTablaRastro);
        System.out.println("recibirDatosTabla() : " );
        System.out.println("Secuencia tabla " + secRegistroT);
        System.out.println("nombreTablaRastro : " + nombreTablaRastro);
        //System.out.println("listaRastros : " + listaRastros.get(bandera));
        estado = 1;
        campoEmpl = administrarRastros.existenciaEmpleadoTabla(nombreTablaRastro);
        System.out.println("Empleado: " + campoEmpl);
    }

    //HISTORICOS RASTRO TABLA
    public void historicosTabla(String nombreTablaR, String nombrePagina) {
        listaRastros = null;
        secRegistroT = null;
        nombreTablaRastro = nombreTablaR;
        nomPagina = nombrePagina;
        getListaRastros();
        System.out.println("ControlRastro.historicosTabla() : " );
        System.out.println("Secuencia tabla " + secRegistroT);
        System.out.println("nombreTablaRastro : " + nombreTablaRastro);
        System.out.println("nombrePagina : " + nomPagina);
        listaRastros = administrarRastros.rastrosTablaHistoricos(nombreTablaRastro);
        estado = 2;
        campoEmpl = administrarRastros.existenciaEmpleadoTabla(nombreTablaRastro);
    }

    //HISTORICOS ELIMINADOS RASTRO TABLA
    public void historicosTablaEliminados() {
        RequestContext context = RequestContext.getCurrentInstance();
        backUplistaLOVRastros = listaRastros;
        listaRastros = administrarRastros.rastrosTablaHistoricosEliminados(nombreTablaRastro);
        if (!listaRastros.isEmpty()) {
            RequestContext.getCurrentInstance().update("form:datosRastro");
            backUplistaLOVRastros = null;
            btnValor = "Consultar (Insertados - Actualizados)";
            RequestContext.getCurrentInstance().update("form:btnConsultar");
            estadoBtn = 2;
        } else {
            listaRastros = backUplistaLOVRastros;
            backUplistaLOVRastros = null;
            RequestContext.getCurrentInstance().execute("PF('errorEliminados').show()");
        }
    }
    public void historicosTablaEliminadosEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();
        backUplistaLOVRastros = listaRastros;
        listaRastros = administrarRastros.rastrosTablaHistoricosEliminadosEmpleado(nombreTablaRastro);
        if (!listaRastros.isEmpty()) {
            RequestContext.getCurrentInstance().update("form:datosRastro");
            backUplistaLOVRastros = null;
            btnValor = "Consultar (Insertados - Actualizados)";
            RequestContext.getCurrentInstance().update("form:btnConsultar");
            estadoBtn = 2;
        } else {
            listaRastros = backUplistaLOVRastros;
            backUplistaLOVRastros = null;
            RequestContext.getCurrentInstance().execute("PF('errorEliminados').show()");
        }
    }

    public void btnConsultarDinamico() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (estadoBtn == 1) {
            RequestContext.getCurrentInstance().execute("PF('confirmarEliminados').show()");
        } else {
            System.out.println("Estado : " + estado);
            if (estado == 1) {
                listaRastros = administrarRastros.rastrosTabla(secRegistroT, nombreTablaRastro);
            } else if (estado == 2) {
                listaRastros = administrarRastros.rastrosTablaHistoricos(nombreTablaRastro);
            }
            estadoBtn = 1;
            btnValor = "Consultar Eliminados";
            RequestContext.getCurrentInstance().update("form:btnConsultar");
            RequestContext.getCurrentInstance().update("form:datosRastro");
        }
    }

    public void seguirDialogos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (campoEmpl == true) {
            RequestContext.getCurrentInstance().execute("PF('confirmarEliminadosEmpleado').show()");
        } else {
            historicosTablaEliminados();
        }
    }

    //HISTORICOS ELIMINADOS RASTRO TABLA
    public void lovFecha() {
        if (estado == 1) {
            if (seleccionRastro != null) {
                listaLOVRastros = administrarRastros.rastrosTabla(new BigInteger(seleccionRastro.getSecuenciatabla()), nombreTablaRastro);
            } else {
                listaRastros = administrarRastros.rastrosTabla(secRegistroT, nombreTablaRastro);
            }
        } else if (estado == 2) {
            if (seleccionRastro != null) {
                listaLOVRastros = administrarRastros.rastrosTabla(new BigInteger(seleccionRastro.getSecuenciatabla()), nombreTablaRastro);
            } else {
                listaLOVRastros = administrarRastros.rastrosTablaHistoricos(nombreTablaRastro);
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:fechaDialogo");
        RequestContext.getCurrentInstance().execute("PF('fechaDialogo').show()");
    }

    public void seleccionarFecha() {
        listaRastros = administrarRastros.rastrosTablaFecha(seleccionRastroLOV.getFecharastro(), nombreTablaRastro);
        filtradoListaLOVRastros = null;
        listaLOVRastros = null;
        seleccionRastroLOV = null;
        aceptar = true;
        seleccionRastro = null;
        btnMostrarTodos = false;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('fechaDialogo').hide()");
        context.reset("form:lovFecha:globalFilter");
        RequestContext.getCurrentInstance().update("form:lovFecha");
        RequestContext.getCurrentInstance().update("form:btnMostrarT");
        RequestContext.getCurrentInstance().update("form:datosRastro");
    }

    public void cancelarSeleccionFecha() {
        filtradoListaLOVRastros = null;
        seleccionRastroLOV = null;
        aceptar = true;
        listaLOVRastros = null;
        seleccionRastro = null;
        RequestContext.getCurrentInstance().update("form:datosRastro");
    }

    //MOSTRAR TODOS
    public void mostrarTodos() {
        if (estado == 1) {
            listaRastros = administrarRastros.rastrosTabla(secRegistroT, nombreTablaRastro);
        } else if (estado == 2) {
            listaRastros = administrarRastros.rastrosTablaHistoricos(nombreTablaRastro);
        }
        btnMostrarTodos = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:btnMostrarT");
    }
    //CTRL + F11 ACTIVAR/DESACTIVAR

    public void activarCtrlF11() {
        if (bandera == 0) {
            nombreTabla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:nombreTabla");
            nombreTabla.setFilterStyle("width: 85% !important;");
            fechaRastro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:fechaRastro");
            fechaRastro.setFilterStyle("width: 85% !important;");
            estacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:estacion");
            estacion.setFilterStyle("width: 85% !important;");
            usuarioSO = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:usuarioSO");
            usuarioSO.setFilterStyle("width: 85% !important;");
            direccionIP = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:direccionIP");
            direccionIP.setFilterStyle("width: 85% !important;");
            usuarioID = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:usuarioID");
            usuarioID.setFilterStyle("width: 85% !important;");
            usuarioBD = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:usuarioBD");
            usuarioBD.setFilterStyle("width: 85% !important;");
            tipoManipulacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:tipoManipulacion");
            tipoManipulacion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosRastro");
            bandera = 1;
        } else if (bandera == 1) {
            nombreTabla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:nombreTabla");
            nombreTabla.setFilterStyle("display: none; visibility: hidden;");
            fechaRastro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:fechaRastro");
            fechaRastro.setFilterStyle("display: none; visibility: hidden;");
            estacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:estacion");
            estacion.setFilterStyle("display: none; visibility: hidden;");
            usuarioSO = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:usuarioSO");
            usuarioSO.setFilterStyle("display: none; visibility: hidden;");
            direccionIP = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:direccionIP");
            direccionIP.setFilterStyle("display: none; visibility: hidden;");
            usuarioID = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:usuarioID");
            usuarioID.setFilterStyle("display: none; visibility: hidden;");
            usuarioBD = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:usuarioBD");
            usuarioBD.setFilterStyle("display: none; visibility: hidden;");
            tipoManipulacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRastro:tipoManipulacion");
            tipoManipulacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosRastro");

            bandera = 0;
            filtradoListaRastros = null;
        }
    }

    public void detallesRastro(Rastros rastro) {
        seleccionRastro = rastro;
        RequestContext context = RequestContext.getCurrentInstance();
        rastroValor = administrarRastros.valorRastro(seleccionRastro.getSecuencia());
        if (!rastroValor.isEmpty()) {
            RequestContext.getCurrentInstance().update("form:valorRastroDialogo");
            RequestContext.getCurrentInstance().execute("PF('valorRastroDialogo').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorValorRastro').show()");
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosRastroExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "RastroPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosRastroExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "RastroXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public String volverPaginaAnterior() {
        secRegistroT = null;
        nombreTablaRastro = null;
        btnValor = "Consultar Eliminados";
        seleccionRastro = null;
        return nomPagina;
    }

    public void activarAceptar() {
        aceptar = false;
    }
//GETTER AND SETTER

    public List<Rastros> getListaRastros() {
        return listaRastros;
    }

    public void setListaRastros(List<Rastros> listaRastros) {
        this.listaRastros = listaRastros;
    }

    public List<Rastros> getFiltradoListaRastros() {
        return filtradoListaRastros;
    }

    public void setFiltradoListaRastros(List<Rastros> filtradoListaRastros) {
        this.filtradoListaRastros = filtradoListaRastros;
    }

    public Rastros getSeleccionRastro() {
        return seleccionRastro;
    }

    public void setSeleccionRastro(Rastros seleccionRastro) {
        this.seleccionRastro = seleccionRastro;
    }

    public List<Rastros> getListaLOVRastros() {
        return listaLOVRastros;
    }

    public void setListaLOVRastros(List<Rastros> listaLOVRastros) {
        this.listaLOVRastros = listaLOVRastros;
    }

    public List<Rastros> getFiltradoListaLOVRastros() {
        return filtradoListaLOVRastros;
    }

    public void setFiltradoListaLOVRastros(List<Rastros> filtradoListaLOVRastros) {
        this.filtradoListaLOVRastros = filtradoListaLOVRastros;
    }

    public Rastros getSeleccionRastroLOV() {
        return seleccionRastroLOV;
    }

    public void setSeleccionRastroLOV(Rastros seleccionRastroLOV) {
        this.seleccionRastroLOV = seleccionRastroLOV;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public boolean isBtnMostrarTodos() {
        return btnMostrarTodos;
    }

    public List<RastrosValores> getRastroValor() {
        return rastroValor;
    }

    public String getNombreTablaRastro() {
        return nombreTablaRastro.toUpperCase();
    }

    public String getBtnValor() {
        return btnValor;
    }
}
