/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Bancos;
import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.Procesos;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarReportesBancosInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.component.column.Column;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlReportesBancos implements Serializable {

    @EJB
    AdministrarReportesBancosInterface administrarReportesBancos;
    @EJB
    AdministarReportesInterface administarReportes;
    //
    private ParametrosReportes parametroDeReporte;
    private List<Inforeportes> listaIR;
    private List<Inforeportes> filtrarListInforeportesUsuario;
    private List<Inforeportes> listaIRRespaldo;
    private String reporteGenerar;
    private Inforeportes inforreporteSeleccionado;
    private int bandera;
    private boolean aceptar;
    private Column codigoIR, reporteIR;
    private int casilla;
    private ParametrosReportes parametroModificacion;
    private int tipoLista;
    private int posicionReporte;
    private String requisitosReporte;
    private InputText empleadoDesdeParametro, empleadoHastaParametro, tipoTrabajadorParametro, ciudadParametro;
    ///
    private List<Empleados> lovEmpleados;
    private List<Empresas> lovEmpresas;
    private List<Bancos> lovBancos;
    private List<Procesos> lovProcesos;
    private List<TiposTrabajadores> lovTiposTrabajadores;
    private List<Ciudades> lovCiudades;
    //
    private Empleados empleadoSeleccionado;
    private Empresas empresaSeleccionada;
    private TiposTrabajadores tipoTSeleccionado;
    private Procesos procesoSeleccionado;
    private Bancos bancoSeleccionado;
    private Ciudades ciudadSeleccionada;
    //////////////////
    private List<Empleados> filtrarLovEmpleados;
    private List<Empresas> filtrarLovEmpresas;
    private List<TiposTrabajadores> filtrarLovTiposTrabajadores;
    private List<Procesos> filtrarLovProcesos;
    private List<Bancos> filtrarLovBancos;
    private List<Ciudades> filtrarLovCiudades;
    //
    private String banco, empresa, tipoTrabajador, proceso, ciudad;
    private boolean permitirIndex, cambiosReporte;
    //ALTO SCROLL TABLA
    private String altoTabla;
    private int indice;
    //EXPORTAR REPORTE
    private StreamedContent file;
    //
    private List<Inforeportes> listaInfoReportesModificados;
    //
    private String color, decoracion;
    private String color2, decoracion2;
    //
    private int casillaInforReporte;
    //
    private Date fechaDesde, fechaHasta;
    private BigDecimal emplDesde, emplHasta;
    //
    private String infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroEmpresa, infoRegistroTipoTrabajador, infoRegistroProceso, infoRegistroBanco, infoRegistroCiudad;
    private String infoRegistro, infoRegistroReportes;
    //MOSTRAR TODOS
    private boolean activoMostrarTodos, activoBuscarReporte, activarEnvio;
    //LOV INFOREPORTES
    private List<Inforeportes> listValInforeportes;
    private Inforeportes reporteSeleccionadoLOV;
    private List<Inforeportes> filtrarLovInforeportes;
    private List<Inforeportes> filtrarReportes;
    private DataTable tabla;
    //
    private String nombreReporte;
    private String pathReporteGenerado;
    private String tipoReporte;
    private boolean estadoReporte;
    private String resultadoReporte;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private ExternalContext externalContext;
    private String userAgent;
    private boolean activarLov;
    private ParametrosReportes parametroFecha;

    public ControlReportesBancos() {
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        activarEnvio = true;
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
        casillaInforReporte = -1;
        cambiosReporte = true;
        listaInfoReportesModificados = new ArrayList<>();
        altoTabla = "180";
        parametroDeReporte = null;
        listaIR = null;
        listaIRRespaldo = new ArrayList<>();
        filtrarListInforeportesUsuario = null;
        bandera = 0;
        aceptar = true;
        casilla = -1;
        parametroModificacion = new ParametrosReportes();
        parametroFecha = new ParametrosReportes();
        tipoLista = 0;
        reporteGenerar = "";
        requisitosReporte = "";
        posicionReporte = -1;
        lovEmpleados = null;
        lovEmpresas = null;
        lovProcesos = null;
        lovBancos = null;
        lovTiposTrabajadores = null;
        lovCiudades = null;
        listValInforeportes = null;
        empleadoSeleccionado = new Empleados();
        empresaSeleccionada = new Empresas();
        tipoTSeleccionado = new TiposTrabajadores();
        procesoSeleccionado = new Procesos();
        ciudadSeleccionada = new Ciudades();
        permitirIndex = true;
        reporteSeleccionadoLOV = null;
        mapParametros.put("paginaAnterior", paginaAnterior);
        activarLov = true;
    }

    public void limpiarListasValor() {
        lovBancos = null;
        lovCiudades = null;
        lovEmpleados = null;
        lovEmpresas = null;
        lovProcesos = null;
        lovTiposTrabajadores = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            externalContext = x.getExternalContext();
            userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
            administrarReportesBancos.obtenerConexion(ses.getId());
            administarReportes.obtenerConexion(ses.getId());
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
        String pagActual = "reportesbancos";

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

    public void iniciarPagina() {
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        listaIR = null;
        getListaIR();
    }

    public void requisitosParaReporte() {
        RequestContext context = RequestContext.getCurrentInstance();
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
        if (inforreporteSeleccionado.getTipotrabajador().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Tipo Trabajador -";
        }
        if (inforreporteSeleccionado.getGrupo().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Ciudad -";
        }
        if (!requisitosReporte.isEmpty()) {
            RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
            RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
        }
    }

    private void activarEnvioCorreo() {
        if (inforreporteSeleccionado != null) {
            activarEnvio = false;
        } else {
            activarEnvio = true;
        }
        RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
    }

    public void seleccionRegistro() {
        try {
            activarEnvioCorreo();
            defaultPropiedadesParametrosReporte();
            if (inforreporteSeleccionado.getFecdesde().equals("SI")) {
                color = "red";
                RequestContext.getCurrentInstance().update("formParametros");
            }
            if (inforreporteSeleccionado.getFechasta().equals("SI")) {
                color2 = "red";
                RequestContext.getCurrentInstance().update("formParametros");
            }
            if (inforreporteSeleccionado.getEmdesde().equals("SI")) {
                empleadoDesdeParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametro");
                //empleadoDesdeParametro.setStyle("position: absolute; top: 41px; left: 150px; height: 10px; font-size: 11px; width: 90px; color: red;");
                if (!empleadoDesdeParametro.getStyle().contains(" color: red;")) {
                    empleadoDesdeParametro.setStyle(empleadoDesdeParametro.getStyle() + " color: red;");

                }
            } else {
                empleadoDesdeParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametro");
                try {
                    if (empleadoDesdeParametro.getStyle().contains(" color: red;")) {
                        empleadoDesdeParametro.setStyle(empleadoDesdeParametro.getStyle().replace(" color: red;", ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
            if (inforreporteSeleccionado.getEmhasta().equals("SI")) {
                empleadoHastaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
                empleadoHastaParametro.setStyle(empleadoHastaParametro.getStyle() + "color: red;");
                RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
            }

            if (inforreporteSeleccionado.getTipotrabajador().equals("SI")) {
                requisitosReporte = requisitosReporte + "- Tipo Trabajador -";
                tipoTrabajadorParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:tipoTrabajadorParametro");
                tipoTrabajadorParametro.setStyle("position: absolute; top: 115px; left: 390px;height: 15px;width: 180px; text-decoration: underline; color: red;");
                RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
            }

            if (inforreporteSeleccionado.getCiudad().equals("SI")) {
                requisitosReporte = requisitosReporte + "- Ciudad -";
                ciudadParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:ciudadParametro");
                ciudadParametro.setStyle("position: absolute; top: 40px; left: 690px;height: 15px;text-decoration: underline; color: red;");
                RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
            }

        } catch (Exception ex) {
            System.out.println("Error en selecccion Registro : " + ex.getMessage());
        }
    }

    public void dispararDialogoGuardarCambios() {
        RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
    }

    public void guardarYSalir() {
        guardarCambios();
        salir();
    }

    public void guardarCambios() {
        try {
            if (!cambiosReporte) {
                if (parametroDeReporte.getUsuario() != null) {
                    if (parametroDeReporte.getCodigoempleadodesde() == null) {
                        parametroDeReporte.setCodigoempleadodesde(null);
                    }
                    if (parametroDeReporte.getCodigoempleadohasta() == null) {
                        parametroDeReporte.setCodigoempleadohasta(null);
                    }
                    if (parametroDeReporte.getTipotrabajador().getSecuencia() == null) {
                        parametroDeReporte.setTipotrabajador(null);
                    }
                    if (parametroDeReporte.getProceso().getSecuencia() == null) {
                        parametroDeReporte.setProceso(null);
                    }
                    if (parametroDeReporte.getEmpresa().getSecuencia() == null) {
                        parametroDeReporte.setEmpresa(null);
                    }
                    if (parametroDeReporte.getBanco().getSecuencia() == null) {
                        parametroDeReporte.setBanco(null);
                    }
                    if (parametroDeReporte.getCiudad().getSecuencia() == null) {
                        parametroDeReporte.setCiudad(null);
                    }
                    administrarReportesBancos.modificarParametrosReportes(parametroDeReporte);
                }
                if (!listaInfoReportesModificados.isEmpty()) {
                    administrarReportesBancos.guardarCambiosInfoReportes(listaInfoReportesModificados);
                    listaInfoReportesModificados.clear();
                }
                cambiosReporte = true;
                FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else if (parametroDeReporte.getFechadesde().after(parametroDeReporte.getFechahasta())) {
                fechaDesde = parametroDeReporte.getFechadesde();
                fechaHasta = parametroDeReporte.getFechahasta();
                System.out.println("parametroDeReporte.getFechadesde: " + parametroDeReporte.getFechadesde());
                System.out.println("parametroDeReporte.getFechahasta: " + parametroDeReporte.getFechahasta());
                parametroDeReporte.setFechadesde(fechaDesde);
                parametroDeReporte.setFechahasta(fechaHasta);
                RequestContext.getCurrentInstance().update("formParametros");
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
        } catch (Exception e) {
            System.out.println("Error en guardar Cambios Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "ha ocurrido un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void modificarParametroInforme() {
        System.out.println("parametroDeReporte.getFechadesde(): " + parametroDeReporte.getFechadesde());
        System.out.println("parametroDeReporte.getFechahasta(): " + parametroDeReporte.getFechahasta());
        if (parametroDeReporte.getFechadesde() != null && parametroDeReporte.getFechahasta() != null) {
            if (parametroDeReporte.getFechadesde().before(parametroDeReporte.getFechahasta())) {
                parametroModificacion = parametroDeReporte;
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                cambiosReporte = true;
            }
        }
    }

    public void posicionCelda(int i) {
        if (permitirIndex == true) {
            casilla = i;
            switch (casilla) {
                case 1:
                    deshabilitarLov();
                    fechaDesde = parametroDeReporte.getFechadesde();
                    break;
                case 2:
                    habilitarLov();
                    emplDesde = parametroDeReporte.getCodigoempleadodesde();
                    break;
                case 3:
                    habilitarLov();
                    empresa = parametroDeReporte.getEmpresa().getNombre();
                    break;
                case 4:
                    habilitarLov();
                    proceso = parametroDeReporte.getProceso().getDescripcion();
                    break;
                case 5:
                    deshabilitarLov();
                    fechaHasta = parametroDeReporte.getFechahasta();
                    break;
                case 6:
                    habilitarLov();
                    emplHasta = parametroDeReporte.getCodigoempleadohasta();
                    break;
                case 8:
                    habilitarLov();
                    banco = parametroDeReporte.getBanco().getNombre();
                    break;
                case 9:
                    habilitarLov();
                    tipoTrabajador = parametroDeReporte.getTipotrabajador().getNombre();
                    break;
                case 11:
                    habilitarLov();
                    ciudad = parametroDeReporte.getCiudad().getNombre();
                    break;
                default:
                    break;
            }
        }
    }

    public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
        RequestContext context = RequestContext.getCurrentInstance();
        int indiceUnicoElemento = -1;
        int coincidencias = 0;

        if (campoConfirmar.equalsIgnoreCase("EMPRESA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getEmpresa().setNombre(empresa);
                for (int i = 0; i < lovEmpresas.size(); i++) {
                    if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar)) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    lovEmpresas.clear();
                    getLovEmpresas();
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
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }

        if (campoConfirmar.equalsIgnoreCase("TIPOTRABAJADOR")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getTipotrabajador().setNombre(tipoTrabajador);
                for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
                    if (lovTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    lovTiposTrabajadores.clear();
                    getLovTiposTrabajadores();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formDialogos:TipoTrabajadorDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
                }
            } else {
                parametroDeReporte.setTipotrabajador(new TiposTrabajadores());
                parametroModificacion = parametroDeReporte;
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }

        if (campoConfirmar.equalsIgnoreCase("PROCESO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getProceso().setDescripcion(proceso);
                for (int i = 0; i < lovProcesos.size(); i++) {
                    if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setProceso(lovProcesos.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    lovProcesos.clear();
                    getLovProcesos();
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
                lovProcesos.clear();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("BANCO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getBanco().setNombre(banco);
                for (int i = 0; i < lovBancos.size(); i++) {
                    if (lovBancos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setBanco(lovBancos.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    lovBancos.clear();
                    getLovBancos();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formDialogos:BancoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('BancoDialogo').show()");
                }
            } else {
                parametroDeReporte.setBanco(new Bancos());
                parametroModificacion = parametroDeReporte;
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }

        if (campoConfirmar.equalsIgnoreCase("CIUDAD")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getCiudad().setNombre(ciudad);
                for (int i = 0; i < lovCiudades.size(); i++) {
                    if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setCiudad(lovCiudades.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    lovCiudades.clear();
                    getLovCiudades();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formDialogos:CiudadDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
                }
            } else {
                parametroDeReporte.setCiudad(new Ciudades());
                parametroModificacion = parametroDeReporte;
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

    public void listaValoresBoton(int pos) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (pos == 2) {
            lovEmpleados = null;
            empleadoSeleccionado = null;
            cargarLovEmpleados();
            contarRegistrosEmpeladoD();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
        }
        if (pos == 3) {
            lovEmpresas = null;
            cargarLovEmpresas();
            contarRegistrosEmpresa();
            RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
        }
        if (pos == 4) {
            lovProcesos = null;
            cargarLovProcesos();
            contarRegistrosProceso();
            RequestContext.getCurrentInstance().update("formDialogos:ProcesoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
        }
        if (pos == 6) {
            lovEmpleados = null;
            empleadoSeleccionado = null;
            cargarLovEmpleados();
            contarRegistrosEmpeladoH();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
        }
        if (pos == 8) {
            lovBancos = null;
            cargarLovBancos();
            contarRegistrosBanco();
            RequestContext.getCurrentInstance().update("formDialogos:BancoDialogo");
            RequestContext.getCurrentInstance().execute("PF('BancoDialogo').show()");
        }
        if (pos == 9) {
            lovTiposTrabajadores = null;
            cargarLovTiposTrabajadores();
            contarRegistrosTipoTrabajador();
            RequestContext.getCurrentInstance().update("formDialogos:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
        }
        if (pos == 11) {
            lovCiudades = null;
            cargarLovCiudades();
            contarRegistrosCiudades();
            RequestContext.getCurrentInstance().update("formDialogos:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
        }

    }

    public void listasValores(int pos) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (pos == 2) {
            lovEmpleados = null;
            empleadoSeleccionado = null;
            cargarLovEmpleados();
            contarRegistrosEmpeladoD();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
        }
        if (pos == 3) {
            lovEmpresas = null;
            cargarLovEmpresas();
            contarRegistrosEmpresa();
            RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
        }
        if (pos == 4) {
            lovProcesos = null;
            cargarLovProcesos();
            contarRegistrosProceso();
            RequestContext.getCurrentInstance().update("formDialogos:ProcesoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
        }
        if (pos == 6) {
            lovEmpleados = null;
            empleadoSeleccionado = null;
            cargarLovEmpleados();
            contarRegistrosEmpeladoH();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
        }

        if (pos == 8) {
            lovBancos = null;
            cargarLovBancos();
            contarRegistrosBanco();
            RequestContext.getCurrentInstance().update("formDialogos:BancoDialogo");
            RequestContext.getCurrentInstance().execute("PF('BancoDialogo').show()");
        }
        if (pos == 9) {
            lovTiposTrabajadores = null;
            cargarLovTiposTrabajadores();
            contarRegistrosTipoTrabajador();
            RequestContext.getCurrentInstance().update("formDialogos:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
        }
        if (pos == 11) {
            lovCiudades = null;
            cargarLovCiudades();
            contarRegistrosCiudades();
            RequestContext.getCurrentInstance().update("formDialogos:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:empresa");
                RequestContext.getCurrentInstance().execute("PF('empresa').show()");
            }
            if (casilla == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:proceso");
                RequestContext.getCurrentInstance().execute("PF('proceso').show()");
            }
            if (casilla == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHasta");
                RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
            }
            if (casilla == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:empleadoHasta");
                RequestContext.getCurrentInstance().execute("PF('empleadoHasta').show()");
            }
            if (casilla == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:numeroCuenta");
                RequestContext.getCurrentInstance().execute("PF('numeroCuenta').show()");
            }
            if (casilla == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:banco");
                RequestContext.getCurrentInstance().execute("PF('banco').show()");
            }
            if (casilla == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:tipoTrabajador");
                RequestContext.getCurrentInstance().execute("PF('tipoTrabajador').show()");
            }
            if (casilla == 10) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaCorte");
                RequestContext.getCurrentInstance().execute("PF('editarFechaCorte').show()");
            }
            if (casilla == 11) {
                RequestContext.getCurrentInstance().update("formularioDialogos:ciudad");
                RequestContext.getCurrentInstance().execute("PF('ciudad').show()");
            }
            casilla = -1;
        }
        if (casillaInforReporte >= 1) {
            if (casillaInforReporte == 1) {
                RequestContext.getCurrentInstance().update("formParametros:infoReporteCodigoD");
                RequestContext.getCurrentInstance().execute("PF('infoReporteCodigoD').show()");
            }
            if (casillaInforReporte == 2) {
                RequestContext.getCurrentInstance().update("formParametros:infoReporteNombreD");
                RequestContext.getCurrentInstance().execute("PF('infoReporteNombreD').show()");
            }
            casillaInforReporte = -1;
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarEmplDesde() {
        permitirIndex = true;
        parametroDeReporte.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarLovEmpleados = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");

    }

    public void cancelarCambioEmplDesde() {
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarLovEmpleados = null;
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
        cambiosReporte = false;
        filtrarLovEmpleados = null;
        empleadoSeleccionado = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
    }

    public void cancelarCambioEmplHasta() {
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarLovEmpleados = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
    }

    public void actualizarEmpresa() {
        permitirIndex = true;
        parametroDeReporte.setEmpresa(empresaSeleccionada);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        empresaSeleccionada = null;
        aceptar = true;
        filtrarLovEmpresas = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
    }

    public void cancelarEmpresa() {
        empresaSeleccionada = null;
        aceptar = true;
        filtrarLovEmpresas = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
    }

    public void actualizarTipoTrabajador() {
        parametroDeReporte.setTipotrabajador(tipoTSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        tipoTSeleccionado = null;
        aceptar = true;
        filtrarLovTiposTrabajadores = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovTipoTrabajador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
    }

    public void cancelarTipoTrabajador() {
        tipoTSeleccionado = null;
        aceptar = true;
        filtrarLovTiposTrabajadores = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovTipoTrabajador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
    }

    public void actualizarProceso() {
        permitirIndex = true;
        parametroDeReporte.setProceso(procesoSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        procesoSeleccionado = null;
        aceptar = true;
        filtrarLovProcesos = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovProceso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:procesoParametro");
    }

    public void cancelarProceso() {
        procesoSeleccionado = null;
        aceptar = true;
        filtrarLovProcesos = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovProceso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
    }

    public void actualizarBanco() {
        permitirIndex = true;
        parametroDeReporte.setBanco(bancoSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        bancoSeleccionado = null;
        aceptar = true;
        filtrarLovBancos = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovBancos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBancos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('BancoDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:bancoParametro");

    }

    public void cancelarBanco() {
        bancoSeleccionado = null;
        aceptar = true;
        filtrarLovBancos = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovBancos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBancos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('BancoDialogo').hide()");
    }

    public void actualizarCiudad() {
        permitirIndex = true;
        parametroDeReporte.setCiudad(ciudadSeleccionada);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        ciudadSeleccionada = null;
        aceptar = true;
        filtrarLovCiudades = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:ciudadParametro");

    }

    public void cancelarCiudad() {
        ciudadSeleccionada = null;
        aceptar = true;
        filtrarLovCiudades = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').hide()");
    }

    public void archivoPlanoReporte(int i) {
        defaultPropiedadesParametrosReporte();
        if (tipoLista == 0) {
            reporteGenerar = listaIR.get(i).getNombre();
            posicionReporte = i;
        }
        if (tipoLista == 1) {
            if (listaIR.contains(filtrarListInforeportesUsuario.get(i))) {
                int posicion = listaIR.indexOf(filtrarListInforeportesUsuario.get(i));
                reporteGenerar = listaIR.get(posicion).getNombre();
                posicionReporte = posicion;
            }
        }
        mostrarDialogoGenerarReporte();
    }

    public void mostrarDialogoGenerarReporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formDialogos:reporteAGenerar");
        RequestContext.getCurrentInstance().execute("PF('reporteAGenerar').show()");
    }

    public void cancelarGenerarReporte() {
        reporteGenerar = "";
        posicionReporte = -1;
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            cerrarFiltrado();
        }
        listaIR = null;
        parametroDeReporte = null;
        parametroModificacion = null;
        lovEmpleados = null;
        lovEmpresas = null;
        lovProcesos = null;
        lovTiposTrabajadores = null;
        lovBancos = null;
        lovCiudades = null;
        casilla = -1;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        inforreporteSeleccionado = null;
        reporteSeleccionadoLOV = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void cancelarYSalir() {
        cancelarModificaciones();
        salir();
    }

    public void cancelarModificaciones() {
        if (bandera == 1) {
            cerrarFiltrado();
        }
        defaultPropiedadesParametrosReporte();
        listaIR = null;
        parametroDeReporte = null;
        parametroModificacion = null;
        casilla = -1;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        getParametroDeReporte();
        getListaIR();
        inforreporteSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
        RequestContext.getCurrentInstance().update("form:reportesBancos");
        RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametroL");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
        RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
        RequestContext.getCurrentInstance().update("formParametros:procesoParametro");
        RequestContext.getCurrentInstance().update("formParametros:tipoCuentaParametro");
        RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametroL");
        RequestContext.getCurrentInstance().update("formParametros:numeroCuentaParametro");
        RequestContext.getCurrentInstance().update("formParametros:bancoParametro");
        RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
        RequestContext.getCurrentInstance().update("formParametros:fechaCorteParametro");
        RequestContext.getCurrentInstance().update("formParametros:ciudadParametro");
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            altoTabla = "160";
            codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBancos:codigoIR");
            codigoIR.setFilterStyle("width: 85% !important;");
            reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBancos:reporteIR");
            reporteIR.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:reportesBancos");
            tipoLista = 1;
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
            defaultPropiedadesParametrosReporte();
        }
    }

    private void cerrarFiltrado() {
        altoTabla = "180";
        codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBancos:codigoIR");
        codigoIR.setFilterStyle("display: none; visibility: hidden;");
        reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBancos:reporteIR");
        reporteIR.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:reportesBancos");
        bandera = 0;
        filtrarListInforeportesUsuario = null;
        tipoLista = 0;
    }

    public void defaultPropiedadesParametrosReporte() {
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
    }

    public void cancelarRequisitosReporte() {
        requisitosReporte = "";
    }

    public void generarReporte(Inforeportes reporte) throws IOException {
        try {
            guardarCambios();
            inforreporteSeleccionado = reporte;
            seleccionRegistro();
            generarDocumentoReporte();
        } catch (Exception e) {
            System.out.println("error en generarReporte : " + e.getMessage());
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
    }

    public void generarDocumentoReporte() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (inforreporteSeleccionado != null) {
                nombreReporte = inforreporteSeleccionado.getNombrereporte();
                tipoReporte = inforreporteSeleccionado.getTipo();

                if (nombreReporte != null && tipoReporte != null) {
                    pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
                }
                System.out.println("pathReporteGenerado: " + pathReporteGenerado.startsWith("ERROR"));
                if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error")) {
                    validarDescargaReporte();
                } else {
                    RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
                    RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                    RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                }
            } else {
                System.out.println("Reporte Seleccionado es nulo");
            }
        } catch (Exception e) {

            if (e instanceof FileNotFoundException) {
                RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                file = null;
            }
            System.out.println("error en generarDocumentoReporte : " + e.getMessage());
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
    }

    public void validarDescargaReporte() {
        System.out.println("Controlador.ControlReportesBancos.validarDescargaReporte()");
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            System.out.println("pathReporteGenerado: " + pathReporteGenerado);
            if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error")) {
                if (tipoReporte.equals("TXT") || tipoReporte.equals("XLS") || tipoReporte.equals("PDF") || tipoReporte.equals("DELIMITED")) {
                    RequestContext.getCurrentInstance().execute("PF('descargarReporte').show()");
                }
                System.out.println("userAgent: " + userAgent);
                if (userAgent.toUpperCase().contains("Mobile".toUpperCase()) || userAgent.toUpperCase().contains("Tablet".toUpperCase()) || userAgent.toUpperCase().contains("Android".toUpperCase())) {
                    context.execute("PF('descargarReporte').show();");
                }

            } else {
                RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
                RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                file = null;
            }
            System.out.println("error en validarDescargarReporte : " + e.getMessage());
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }

    }

    public void exportarReporte() throws IOException {
        try {
            if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
                File reporteF = new File(pathReporteGenerado);
                System.out.println("ReporteF: " + reporteF);
                FacesContext ctx = FacesContext.getCurrentInstance();
                System.out.println("ctx: " + ctx);
                FileInputStream fis = new FileInputStream(reporteF);
                System.out.println("fis: " + fis);
                byte[] bytes = new byte[1024];
                int read;
                {
                    String fileName = reporteF.getName();
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
            } else {
                RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
                RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
        } catch (Exception e) {
            System.out.println("error en exportarReporte : " + e.getMessage());
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
    }

    public AsynchronousFilllListener listener() {
        return new AsynchronousFilllListener() {

            @Override
            public void reportFinished(JasperPrint jp) {
                try {
                    estadoReporte = true;
                    resultadoReporte = "Exito";
                } catch (Exception e) {
                    System.out.println("ControlNReportePersonal reportFinished ERROR: " + e.toString());
                }
            }

            @Override
            public void reportCancelled() {
                estadoReporte = true;
                resultadoReporte = "Cancelación";
            }

            @Override
            public void reportFillError(Throwable e) {
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

    public void mostrarDialogoBuscarReporte() {
        try {
            if (cambiosReporte == true) {
                listValInforeportes = null;
                cargarLovInfoReportes();
                contarRegistrosLovReportes();
                RequestContext context = RequestContext.getCurrentInstance();
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

    public void actualizarSeleccionInforeporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            cerrarFiltrado();
        }
        defaultPropiedadesParametrosReporte();
        listaIR.clear();
        listaIR.add(reporteSeleccionadoLOV);
        filtrarListInforeportesUsuario = null;
        filtrarLovInforeportes = null;
        aceptar = true;
        activoBuscarReporte = true;
        activoMostrarTodos = false;
        inforreporteSeleccionado = reporteSeleccionadoLOV;
        reporteSeleccionadoLOV = null;
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        context.reset("formDialogos:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:reportesBancos");
        contarRegistros();
    }

    public void cancelarSeleccionInforeporte() {
        filtrarListInforeportesUsuario = null;
        reporteSeleccionadoLOV = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
    }

    public void mostrarTodos() {
        if (cambiosReporte == true) {
            defaultPropiedadesParametrosReporte();
            listaIR.clear();
            for (int i = 0; i < listValInforeportes.size(); i++) {
                listaIR.add(listValInforeportes.get(i));
            }
            RequestContext context = RequestContext.getCurrentInstance();
            activoBuscarReporte = false;
            activoMostrarTodos = true;
            RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
            RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
            RequestContext.getCurrentInstance().update("form:reportesBancos");
            contarRegistros();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
        }
    }

    //CONTARREGISTROS
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

    public void contarRegistrosEmpresa() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpresa");
    }

    public void contarRegistrosTipoTrabajador() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroTipoTrabajador");
    }

    public void contarRegistrosProceso() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroProceso");
    }

    public void contarRegistrosBanco() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroBanco");
    }

    public void contarRegistrosCiudades() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroCiudad");
    }

    public void cargarLovEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarReportesBancos.listEmpleados();
        }
    }

    public void cargarLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarReportesBancos.listEmpresas();
        }
    }

    public void cargarLovTiposTrabajadores() {
        if (lovTiposTrabajadores == null) {
            lovTiposTrabajadores = administrarReportesBancos.listTiposTrabajadores();
        }
    }

    public void cargarLovProcesos() {
        if (lovProcesos == null) {
            lovProcesos = administrarReportesBancos.listProcesos();
        }
    }

    public void cargarLovBancos() {
        if (lovBancos == null) {
            lovBancos = administrarReportesBancos.listBancos();
        }
    }

    public void cargarLovCiudades() {
        if (lovCiudades == null) {
            lovCiudades = administrarReportesBancos.listCiudades();
        }
    }

    public void cargarLovInfoReportes() {
        if (listValInforeportes == null) {
            listValInforeportes = administrarReportesBancos.listInforeportesUsuario();
        }
    }

    public void habilitarLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void mostrarDialogoNuevaFecha() {
        getParametroDeReporte();
        if (parametroDeReporte.getFechadesde() == null && parametroDeReporte.getFechahasta() == null) {
            RequestContext.getCurrentInstance().update("formDialogos:nuevoRegistroFechas");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroFechas').show()");
        }
    }

    public void agregarFecha() {
        int contador = 0;
        if (parametroFecha.getFechadesde() == null) {
            contador++;
        }
        if (parametroFecha.getFechahasta() == null) {
            contador++;
        }

        if (contador == 0) {
            parametroDeReporte.setFechadesde(parametroFecha.getFechadesde());
            parametroDeReporte.setFechahasta(parametroFecha.getFechahasta());
            RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametro");
            RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametro");
            aceptar = false;
            RequestContext.getCurrentInstance().execute("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroFechas').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formDialogos:validacionRegistroFechas");
            RequestContext.getCurrentInstance().execute("PF('validacionRegistroFechas').show()");
        }

    }

    public void limpiarNuevaFecha() {
        parametroFecha.setFechadesde(null);
        parametroFecha.setFechahasta(null);
    }

    //GETTER && SETTER
    public ParametrosReportes getParametroDeReporte() {
        try {
            if (parametroDeReporte == null) {
                parametroDeReporte = new ParametrosReportes();
                parametroDeReporte = administrarReportesBancos.parametrosDeReporte();
            }

            if (parametroDeReporte.getBanco() == null) {
                parametroDeReporte.setBanco(new Bancos());
            }

            if (parametroDeReporte.getCiudad() == null) {
                parametroDeReporte.setCiudad(new Ciudades());
            }

            if (parametroDeReporte.getTipotrabajador() == null) {
                parametroDeReporte.setTipotrabajador(new TiposTrabajadores());
            }

            if (parametroDeReporte.getProceso() == null) {
                parametroDeReporte.setProceso(new Procesos());
            }

            if (parametroDeReporte.getEmpresa() == null) {
                parametroDeReporte.setEmpresa(new Empresas());
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
        if (listaIR == null) {
            listaIR = administrarReportesBancos.listInforeportesUsuario();
        }
        return listaIR;
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

    public List<Inforeportes> getListaIRRespaldo() {
        return listaIRRespaldo;
    }

    public void setListaIRRespaldo(List<Inforeportes> listaIRRespaldo) {
        this.listaIRRespaldo = listaIRRespaldo;
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

    public List<Empleados> getLovEmpleados() {
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public List<Empresas> getLovEmpresas() {
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public List<TiposTrabajadores> getLovTiposTrabajadores() {
        return lovTiposTrabajadores;
    }

    public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadores) {
        this.lovTiposTrabajadores = lovTiposTrabajadores;
    }

    public List<Procesos> getLovProcesos() {
        return lovProcesos;
    }

    public void setLovProcesos(List<Procesos> lovProcesos) {
        this.lovProcesos = lovProcesos;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public TiposTrabajadores getTipoTSeleccionado() {
        return tipoTSeleccionado;
    }

    public void setTipoTSeleccionado(TiposTrabajadores tipoTSeleccionado) {
        this.tipoTSeleccionado = tipoTSeleccionado;
    }

    public Procesos getProcesoSeleccionado() {
        return procesoSeleccionado;
    }

    public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
        this.procesoSeleccionado = procesoSeleccionado;
    }

    public List<Empleados> getFiltrarLovEmpleados() {
        return filtrarLovEmpleados;
    }

    public void setFiltrarLovEmpleados(List<Empleados> filtrarLovEmpleados) {
        this.filtrarLovEmpleados = filtrarLovEmpleados;
    }

    public List<Empresas> getFiltrarLovEmpresas() {
        return filtrarLovEmpresas;
    }

    public void setFiltrarLovEmpresas(List<Empresas> filtrarLovEmpresas) {
        this.filtrarLovEmpresas = filtrarLovEmpresas;
    }

    public List<Procesos> getFiltrarLovProcesos() {
        return filtrarLovProcesos;
    }

    public void setFiltrarLovProcesos(List<Procesos> filtrarLovProcesos) {
        this.filtrarLovProcesos = filtrarLovProcesos;
    }

    public List<TiposTrabajadores> getFiltrarLovTiposTrabajadores() {
        return filtrarLovTiposTrabajadores;
    }

    public void setFiltrarLovTiposTrabajadores(List<TiposTrabajadores> filtrarLovTiposTrabajadores) {
        this.filtrarLovTiposTrabajadores = filtrarLovTiposTrabajadores;
    }

    public List<Bancos> getLovBancos() {
        return lovBancos;
    }

    public void setLovBancos(List<Bancos> lovBancos) {
        this.lovBancos = lovBancos;
    }

    public Bancos getBancoSeleccionado() {
        return bancoSeleccionado;
    }

    public void setBancoSeleccionado(Bancos bancoSeleccionado) {
        this.bancoSeleccionado = bancoSeleccionado;
    }

    public List<Bancos> getFiltrarLovBancos() {
        return filtrarLovBancos;
    }

    public void setFiltrarLovBancos(List<Bancos> filtrarLovBancos) {
        this.filtrarLovBancos = filtrarLovBancos;
    }

    public List<Ciudades> getLovCiudades() {
        return lovCiudades;

    }

    public List<Inforeportes> getListValInforeportes() {
        return listValInforeportes;
    }

    public void setListValInforeportes(List<Inforeportes> listValInforeportes) {
        this.listValInforeportes = listValInforeportes;
    }

    public void setLovCiudades(List<Ciudades> lovCiudades) {
        this.lovCiudades = lovCiudades;
    }

    public Ciudades getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public List<Ciudades> getFiltrarLovCiudades() {
        return filtrarLovCiudades;
    }

    public void setFiltrarLovCiudades(List<Ciudades> filtrarLovCiudades) {
        this.filtrarLovCiudades = filtrarLovCiudades;
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

    public String getInfoRegistroEmpleadoDesde() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpleadoDesde");
        if (filtrarLovEmpleados != null) {
            if (filtrarLovEmpleados.size() == 1) {
                empleadoSeleccionado = filtrarLovEmpleados.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').unselectAllRows();PF('lovEmpleadoDesde').selectRow(0);");
            } else {
                empleadoSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').unselectAllRows();");
            }
        } else {
            empleadoSeleccionado = null;
            aceptar = true;
        }
        infoRegistroEmpleadoDesde = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleadoDesde;
    }

    public String getInfoRegistroEmpleadoHasta() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpleadoHasta");
       if (filtrarLovEmpleados != null) {
            if (filtrarLovEmpleados.size() == 1) {
                empleadoSeleccionado = filtrarLovEmpleados.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').unselectAllRows();PF('lovEmpleadoHasta').selectRow(0);");
            } else {
                empleadoSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').unselectAllRows();");
            }
        } else {
            empleadoSeleccionado = null;
            aceptar = true;
        }
        infoRegistroEmpleadoHasta = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleadoHasta;
    }

    public String getInfoRegistroEmpresa() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpresa");
        if (filtrarLovEmpresas != null) {
            if (filtrarLovEmpresas.size() == 1) {
                empresaSeleccionada = filtrarLovEmpresas.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('lovEmpresa').unselectAllRows();PF('lovEmpresa').selectRow(0);");
            } else {
                empresaSeleccionada = null;
                RequestContext.getCurrentInstance().execute("PF('lovEmpresa').unselectAllRows();");
            }
        } else {
            empresaSeleccionada = null;
            aceptar = true;
        }
        infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresa;
    }

    public String getInfoRegistroProceso() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovProceso");
        if (filtrarLovProcesos != null) {
            if (filtrarLovProcesos.size() == 1) {
                procesoSeleccionado = filtrarLovProcesos.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('lovProceso').unselectAllRows();PF('lovProceso').selectRow(0);");
            } else {
                procesoSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('lovProceso').unselectAllRows();");
            }
        } else {
            procesoSeleccionado = null;
            aceptar = true;
        }
        infoRegistroProceso = String.valueOf(tabla.getRowCount());
        return infoRegistroProceso;
    }

    public String getInfoRegistroBanco() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovBancos");
        if (filtrarLovBancos != null) {
            if (filtrarLovBancos.size() == 1) {
                bancoSeleccionado = filtrarLovBancos.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('lovBancos').unselectAllRows();PF('lovBancos').selectRow(0);");
            } else {
                bancoSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('lovBancos').unselectAllRows();");
            }
        } else {
            bancoSeleccionado = null;
            aceptar = true;
        }
        infoRegistroBanco = String.valueOf(tabla.getRowCount());
        return infoRegistroBanco;
    }

    public String getInfoRegistroCiudad() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovCiudades");
        if (filtrarLovCiudades != null) {
            if (filtrarLovCiudades.size() == 1) {
                ciudadSeleccionada = filtrarLovCiudades.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('lovCiudades').unselectAllRows();PF('lovCiudades').selectRow(0);");
            } else {
                ciudadSeleccionada = null;
                RequestContext.getCurrentInstance().execute("PF('lovCiudades').unselectAllRows();");
            }
        } else {
            ciudadSeleccionada = null;
            aceptar = true;
        }
        infoRegistroCiudad = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudad;
    }

    public String getInfoRegistroTipoTrabajador() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovTipoTrabajador");
        if (filtrarLovTiposTrabajadores != null) {
            if (filtrarLovTiposTrabajadores.size() == 1) {
                tipoTSeleccionado = filtrarLovTiposTrabajadores.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').unselectAllRows();PF('lovTipoTrabajador').selectRow(0);");
            } else {
                tipoTSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').unselectAllRows();");
            }
        } else {
            tipoTSeleccionado = null;
            aceptar = true;
        }
        infoRegistroTipoTrabajador = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoTrabajador;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:reportesBancos");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public String getInfoRegistroReportes() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovReportesDialogo");
         if (filtrarLovInforeportes != null) {
            if (filtrarLovInforeportes.size() == 1) {
                reporteSeleccionadoLOV = filtrarLovInforeportes.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').unselectAllRows();PF('lovReportesDialogo').selectRow(0);");
            } else {
                reporteSeleccionadoLOV = null;
                RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').unselectAllRows();");
            }
        } else {
            reporteSeleccionadoLOV = null;
            aceptar = true;
        }
        infoRegistroReportes = String.valueOf(tabla.getRowCount());
        return infoRegistroReportes;
    }

    public Inforeportes getReporteSeleccionadoLOV() {
        return reporteSeleccionadoLOV;
    }

    public void setReporteSeleccionadoLOV(Inforeportes reporteSeleccionadoLOV) {
        this.reporteSeleccionadoLOV = reporteSeleccionadoLOV;
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

    public String getPathReporteGenerado() {
        return pathReporteGenerado;
    }

    public void setPathReporteGenerado(String pathReporteGenerado) {
        this.pathReporteGenerado = pathReporteGenerado;
    }

    public boolean isActivarEnvio() {
        return activarEnvio;
    }

    public void setActivarEnvio(boolean activarEnvio) {
        this.activarEnvio = activarEnvio;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public ParametrosReportes getParametroFecha() {
        return parametroFecha;
    }

    public void setParametroFecha(ParametrosReportes parametroFecha) {
        this.parametroFecha = parametroFecha;
    }

    }
