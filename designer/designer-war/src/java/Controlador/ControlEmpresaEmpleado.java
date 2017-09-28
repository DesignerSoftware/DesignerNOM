/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Empleados;
import Entidades.Empresas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmpleadosInterface;
import InterfaceAdministrar.AdministrarEmpresasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
@Named(value = "controlEmpresaEmpleado")
@SessionScoped
public class ControlEmpresaEmpleado implements Serializable {

    private static Logger log = Logger.getLogger(ControlTiposCursos.class);

    @EJB
    AdministrarEmpleadosInterface administarEmpleados;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarEmpresasInterface administrarEmpresas;

    private List<Empleados> listaEmpleados;
    private List<Empleados> listaEmpleadosFiltrar;
    private List<Empleados> listaEmpleadosModificar;
    private Empleados empleadoSeleccionado;
    private Empleados editarEmpleado;

    private List<Empleados> lovEmpleados;
    private List<Empleados> lovEmpleadosFiltrar;
    private Empleados empleadoLovSeleccionado;
    private List<Empresas> lovEmpresas;
    private List<Empresas> lovEmpresasFiltrar;
    private Empresas empresaLovSeleccionado;
    private int cualCelda, tipoLista, bandera;
    private boolean aceptar, guardado;
    private String mensajeValidacion, altoTabla;
    private boolean activarLov, activarMTodos, activarBuscar;
    private String infoRegistro, infoRegistroEmpleados, infoRegistroEmpresas;
    private Column codigo, primerapellido, segundoapellido, nombres, empresa, digitado, codalter;
    private DataTable tablaC;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlEmpresaEmpleado() {
        listaEmpleados = null;
        listaEmpleadosModificar = new ArrayList<Empleados>();
        editarEmpleado = new Empleados();
        guardado = true;
        activarLov = true;
        altoTabla = "320";
        cualCelda = -1;
        bandera = 0;
        empleadoSeleccionado = null;
        mapParametros.put("paginaAnterior", paginaAnterior);
        activarMTodos = true;
        activarBuscar = false;
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
            administarEmpleados.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            administrarEmpresas.obtenerConexion(ses.getId());
            listaEmpleados = null;
            getListaEmpleados();
            if (listaEmpleados != null) {
                if (!listaEmpleados.isEmpty()) {
                    empleadoSeleccionado = listaEmpleados.get(0);
                }
            }
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;

    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "empresaempleado";
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

    public String redirigir() {
        return paginaAnterior;
    }

    public void cambiarIndice(Empleados empl, int celda) {
        empleadoSeleccionado = empl;
        cualCelda = celda;
        empleadoSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            empleadoSeleccionado.getCodigoempleado();
        } else if (cualCelda == 1) {
            empleadoSeleccionado.getPrimerApellidoPersona();
        } else if (cualCelda == 2) {
            empleadoSeleccionado.getSegundoApellidoPersona();
        } else if (cualCelda == 3) {
            empleadoSeleccionado.getNombreCompleto();
        } else if (cualCelda == 4) {
            empleadoSeleccionado.getNombreEmpresa();
        } else if (cualCelda == 5) {
            empleadoSeleccionado.getFechacreacion();
        } else if (cualCelda == 6) {
            empleadoSeleccionado.getCodigoalternativo();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            primerapellido = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:primerapellido");
            primerapellido.setFilterStyle("display: none; visibility: hidden;");
            segundoapellido = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:segundoapellido");
            segundoapellido.setFilterStyle("display: none; visibility: hidden;");
            nombres = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:nombres");
            nombres.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            digitado = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:digitado");
            digitado.setFilterStyle("display: none; visibility: hidden;");
            codalter = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:codalter");
            codalter.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listaEmpleadosFiltrar = null;
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosEmpresaEmpleado");
            tipoLista = 0;
        }

        listaEmpleadosModificar.clear();
        empleadoSeleccionado = null;
        contarRegistros();
        listaEmpleados = null;
        getListaEmpleados();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEmpresaEmpleado");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            primerapellido = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:primerapellido");
            primerapellido.setFilterStyle("display: none; visibility: hidden;");
            segundoapellido = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:segundoapellido");
            segundoapellido.setFilterStyle("display: none; visibility: hidden;");
            nombres = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:nombres");
            nombres.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            digitado = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:digitado");
            digitado.setFilterStyle("display: none; visibility: hidden;");
            codalter = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:codalter");
            codalter.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listaEmpleadosFiltrar = null;
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosEmpresaEmpleado");
            tipoLista = 0;
        }
        listaEmpleadosModificar.clear();
        empleadoSeleccionado = null;
        listaEmpleados = null;
        guardado = true;
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "300";
            codigo = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            primerapellido = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:primerapellido");
            primerapellido.setFilterStyle("width: 85% !important;");
            segundoapellido = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:segundoapellido");
            segundoapellido.setFilterStyle("width: 85% !important;");
            nombres = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:nombres");
            nombres.setFilterStyle("width: 85% !important;");
            empresa = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:empresa");
            empresa.setFilterStyle("width: 85% !important;");
            digitado = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:digitado");
            digitado.setFilterStyle("width: 85% !important;");
            codalter = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:codalter");
            codalter.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosEmpresaEmpleado");
            bandera = 1;
        } else if (bandera == 1) {
            log.info("Desactivar");
            altoTabla = "320";
            codigo = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            primerapellido = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:primerapellido");
            primerapellido.setFilterStyle("display: none; visibility: hidden;");
            segundoapellido = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:segundoapellido");
            segundoapellido.setFilterStyle("display: none; visibility: hidden;");
            nombres = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:nombres");
            nombres.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            digitado = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:digitado");
            digitado.setFilterStyle("display: none; visibility: hidden;");
            codalter = (Column) c.getViewRoot().findComponent("form:datosEmpresaEmpleado:codalter");
            codalter.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEmpresaEmpleado");
            bandera = 0;
            listaEmpleadosFiltrar = null;
            tipoLista = 0;
        }
    }

    public void modificarEmpleado(Empleados empl) {
        empleadoSeleccionado = empl;
        if (listaEmpleadosModificar.isEmpty()) {
            listaEmpleadosModificar.add(empleadoSeleccionado);
        } else if (!listaEmpleadosModificar.contains(empleadoSeleccionado)) {
            listaEmpleadosModificar.add(empleadoSeleccionado);
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosEmpresaEmpleado");
    }

    public void revisarDialogoGuardar() {
        if (!listaEmpleadosModificar.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listaEmpleadosModificar.isEmpty()) {
                    administarEmpleados.editarEmpleado(listaEmpleadosModificar);
                    listaEmpleadosModificar.clear();
                }
                listaEmpleados = null;
                getListaEmpleados();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                empleadoSeleccionado = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosEmpresaEmpleado");
        } catch (Exception e) {
            log.warn("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void editarCelda() {
        if (empleadoSeleccionado != null) {
            editarEmpleado = empleadoSeleccionado;

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editPrimerApellido");
                RequestContext.getCurrentInstance().execute("PF('editPrimerApellido').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editSegundoApellido");
                RequestContext.getCurrentInstance().execute("PF('editSegundoApellido').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
                RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editEmpresa");
                RequestContext.getCurrentInstance().execute("PF('editEmpresa').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDigitado");
                RequestContext.getCurrentInstance().execute("PF('editDigitado').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodAlternativo");
                RequestContext.getCurrentInstance().execute("PF('editCodAlternativo').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void limpiarExportar() {
        editarEmpleado = new Empleados();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpresaEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Empleados", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpresaEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Empleados", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (empleadoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(empleadoSeleccionado.getSecuencia(), "Empleados"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("Empleados")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            log.warn("Error ControlTiposCursos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosEmpresas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresas");
    }

    public void contarRegistrosEmpleados() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpl");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
    }

    public void habilitarBotonLov() {
        activarLov = false;
    }
    
     public void posicionOtro() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        empleadoSeleccionado = listaEmpleados.get(indice);
        cualCelda = columna;
    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        empleadoSeleccionado.setEmpresa(empresaLovSeleccionado.getSecuencia());
        empleadoSeleccionado.setNombreEmpresa(empresaLovSeleccionado.getNombre());
        if (listaEmpleadosModificar.isEmpty()) {
            listaEmpleadosModificar.add(empleadoSeleccionado);
        } else if (!listaEmpleadosModificar.contains(empleadoSeleccionado)) {
            listaEmpleadosModificar.add(empleadoSeleccionado);
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        deshabilitarBotonLov();
        RequestContext.getCurrentInstance().update("form:datosEmpresaEmpleado");
        listaEmpleadosFiltrar = null;
        empresaLovSeleccionado = null;
        aceptar = true;
        RequestContext.getCurrentInstance().update("formularioDialogos:EmpresaDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarD");

        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
    }

    public void cancelarCambioEmpresa() {
        listaEmpleadosFiltrar = null;
        aceptar = true;
        empresaLovSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:EmpresaDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarD");
    }

    public void listaValoresBoton() {
        if (empleadoSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 4) {
                habilitarBotonLov();
                lovEmpresas = null;
                getLovEmpresas();
                contarRegistrosEmpresas();
                RequestContext.getCurrentInstance().update("formularioDialogos:EmpresaDialogo");
                RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            }
        }
    }

    public void asignarIndex(int dlg) {
        if (dlg == 0) {
            lovEmpresas = null;
            getLovEmpresas();
            contarRegistrosEmpresas();
            RequestContext.getCurrentInstance().update("formularioDialogos:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");

        } else if (dlg == 1) {
            lovEmpleados = null;
            getLovEmpleados();
            contarRegistrosEmpleados();
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
        }
    }

    public void actualizarEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaEmpleados.isEmpty()) {
            listaEmpleados.clear();
            listaEmpleados.add(empleadoLovSeleccionado);
            empleadoSeleccionado = listaEmpleados.get(0);
        }
        aceptar = true;
        cualCelda = -1;
        activarMTodos = false;
        contarRegistros();
        RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:datosEmpresaEmpleado");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
    }

    public void cancelarCambioEmpleados() {
        lovEmpleadosFiltrar = null;
        empleadoLovSeleccionado = null;
        aceptar = true;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
    }

    public void mostrarTodos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaEmpleados.isEmpty()) {
            listaEmpleados.clear();
        }
        if (listaEmpleados != null) {
            for (int i = 0; i < lovEmpleados.size(); i++) {
                listaEmpleados.add(lovEmpleados.get(i));
            }
        }
        empleadoSeleccionado = listaEmpleados.get(0);
        contarRegistros();
        aceptar = true;
        cualCelda = -1;
        activarMTodos = true;
        RequestContext.getCurrentInstance().update("form:datosEmpresaEmpleado");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
    }

    ////////get y set/////////////////////
    public List<Empleados> getListaEmpleados() {
        if (listaEmpleados == null) {
            listaEmpleados = administarEmpleados.listaEmpleadosEmpresa();
        }
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public List<Empleados> getListaEmpleadosFiltrar() {
        return listaEmpleadosFiltrar;
    }

    public void setListaEmpleadosFiltrar(List<Empleados> listaEmpleadosFiltrar) {
        this.listaEmpleadosFiltrar = listaEmpleadosFiltrar;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public Empleados getEditarEmpleado() {
        return editarEmpleado;
    }

    public void setEditarEmpleado(Empleados editarEmpleado) {
        this.editarEmpleado = editarEmpleado;
    }

    public List<Empleados> getLovEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administarEmpleados.listaEmpleadosEmpresa();
        }
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

    public Empleados getEmpleadoLovSeleccionado() {
        return empleadoLovSeleccionado;
    }

    public void setEmpleadoLovSeleccionado(Empleados empleadoLovSeleccionado) {
        this.empleadoLovSeleccionado = empleadoLovSeleccionado;
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

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpresaEmpleado");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public List<Empresas> getLovEmpresas() {
        if(lovEmpresas == null){
            lovEmpresas = administrarEmpresas.listaEmpresas();
        }
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public List<Empresas> getLovEmpresasFiltrar() {
        return lovEmpresasFiltrar;
    }

    public void setLovEmpresasFiltrar(List<Empresas> lovEmpresasFiltrar) {
        this.lovEmpresasFiltrar = lovEmpresasFiltrar;
    }

    public Empresas getEmpresaLovSeleccionado() {
        return empresaLovSeleccionado;
    }

    public void setEmpresaLovSeleccionado(Empresas empresaLovSeleccionado) {
        this.empresaLovSeleccionado = empresaLovSeleccionado;
    }

    public boolean isActivarMTodos() {
        return activarMTodos;
    }

    public void setActivarMTodos(boolean activarMTodos) {
        this.activarMTodos = activarMTodos;
    }

    public boolean isActivarBuscar() {
        return activarBuscar;
    }

    public void setActivarBuscar(boolean activarBuscar) {
        this.activarBuscar = activarBuscar;
    }

    public String getInfoRegistroEmpleados() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVEmpleados");
        infoRegistroEmpleados = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleados;
    }

    public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
        this.infoRegistroEmpleados = infoRegistroEmpleados;
    }

    public String getInfoRegistroEmpresas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEmpresas");
        infoRegistroEmpresas = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
    }

}
