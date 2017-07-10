/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Personas;
import InterfaceAdministrar.AdministrarEmpleadosInterface;
import InterfaceAdministrar.AdministrarEmpresasInterface;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpSession;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlCambioCodigoEmpl")
@SessionScoped
public class ControlCambioCodigoEmpl implements Serializable {

    @EJB
    AdministrarEmpleadosInterface administrarEmpleados;
    @EJB
    AdministrarEmpresasInterface administrarEmpresas;

    //LOV EMPLEADOS
    private List<Empleados> lovEmpleados;
    private List<Empleados> filtradoLovEmpleados;
    private Empleados empleadoSeleccionado;
    //LOV EMPRESAS
    private List<Empresas> lovEmpresas;
    private List<Empresas> filtradoLovEmpresas;
    private Empresas empresaSeleccionada;
    //CAMPOS DEL FORMULARIO
    private Empleados empleado;
    private Empresas empresa;
    private BigDecimal codEmplNuevo;
    private String infoRegistroEmpleados;
    private String infoRegistroEmpresas;
    private String mensajeValidacion;
    private boolean aceptar;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlCambioCodigoEmpl() {
        lovEmpleados = null;
        lovEmpresas = null;
        empleado = new Empleados();
        empleado.setPersona(new Personas());
        empresa = new Empresas();
        aceptar = true;
        mapParametros.put("paginaAnterior", paginaAnterior);

    }

    public void limpiarListasValor() {
        lovEmpleados = null;
        lovEmpresas = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEmpleados.obtenerConexion(ses.getId());
            administrarEmpresas.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "emplcambiocodigo";
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

    public void cancelarModificaciones() {
        empleado = new Empleados();
        empleado.setPersona(new Personas());
        empresa = new Empresas();
        codEmplNuevo = null;
        RequestContext.getCurrentInstance().update("form:codEmpleadoActual");
        RequestContext.getCurrentInstance().update("form:codEmpleadoNuevo");
        RequestContext.getCurrentInstance().update("form:empresa");
    }

    public void salir() {
        limpiarListasValor();
        RequestContext context = RequestContext.getCurrentInstance();
        empleado = new Empleados();
        empleado.setPersona(new Personas());
        empresa = new Empresas();
        codEmplNuevo = null;
        RequestContext.getCurrentInstance().update("form:codEmpleadoActual");
        RequestContext.getCurrentInstance().update("form:codEmpleadoNuevo");
        RequestContext.getCurrentInstance().update("form:empresa");
    }

    public void asignarIndex(int dlg) {

        RequestContext context = RequestContext.getCurrentInstance();
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
        } else if (dlg == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
        }
    }

    public void actualizarEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        empleado = empleadoSeleccionado;
        empleadoSeleccionado = null;
        aceptar = true;
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
        RequestContext.getCurrentInstance().update("form:codEmpleadoActual");
    }

    public void actualizarEmpresas() {
        RequestContext context = RequestContext.getCurrentInstance();
        empresa = empresaSeleccionada;
        empresaSeleccionada = null;
        aceptar = true;
        context.reset("formularioDialogos:LOVEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empresasDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpresas");
        RequestContext.getCurrentInstance().update("form:empresa");
    }

    public void cancelarCambioEmpleados() {
        empleadoSeleccionado = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
    }

    public void cancelarCambioEstructuras() {
        empresaSeleccionada = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empresasDialogo').hide()");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void contarRegistrosLovEmpresas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresas");
    }

    public void contarRegistrosLovEmpleados() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleados");
    }

    public void modificarCodigoNuevo(BigDecimal codnuevo) {
        codEmplNuevo = codnuevo;
        RequestContext.getCurrentInstance().update("form:codEmpleadoNuevo");

    }

    public void cambiarCodigoEmpleado() {
        try {
            int contador = 0;
            if (codEmplNuevo == null || empleado.getCodigoempleado() == null || empresa.getSecuencia() == null) {
                RequestContext.getCurrentInstance().execute("PF('camposVacios').show()");
            } else if (empleado.getCodigoempleado().equals(codEmplNuevo)) {
                RequestContext.getCurrentInstance().execute("PF('CodRepetido').show()");
            } else if (!empleado.getCodigoempleado().equals(codEmplNuevo)) {
                lovEmpleados = null;
                getLovEmpleados();
                for (int i = 0; i < lovEmpleados.size(); i++) {
                    if (codEmplNuevo.equals(lovEmpleados.get(i).getCodigoempleado())) {
                        contador++;
                    }
                }
                if (contador == 0) {
                    administrarEmpleados.cambiarCodEmpl(empleado.getCodigoempleado(), codEmplNuevo);
                    RequestContext.getCurrentInstance().execute("PF('exito').show()");
                } else {
                    RequestContext.getCurrentInstance().execute("PF('CodRepetido').show()");
                }
            }
        } catch (Exception e) {
            if (e instanceof PersistenceException || e instanceof SQLException) {
                mensajeValidacion = e.getMessage();
                RequestContext.getCurrentInstance().execute("PF('errorSQL').show()");
            } else {
                System.out.println("error en cambiarCodigoEmpleado : " + e.getMessage());
            }
        }
    }
    /////get y set
    public List<Empleados> getLovEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarEmpleados.listaEmpleados();
        }
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public List<Empleados> getFiltradoLovEmpleados() {
        return filtradoLovEmpleados;
    }

    public void setFiltradoLovEmpleados(List<Empleados> filtradoLovEmpleados) {
        this.filtradoLovEmpleados = filtradoLovEmpleados;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public List<Empresas> getLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarEmpresas.listaEmpresas();
        }
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public List<Empresas> getFiltradoLovEmpresas() {
        return filtradoLovEmpresas;
    }

    public void setFiltradoLovEmpresas(List<Empresas> filtradoLovEmpresas) {
        this.filtradoLovEmpresas = filtradoLovEmpresas;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public BigDecimal getCodEmplNuevo() {
        return codEmplNuevo;
    }

    public void setCodEmplNuevo(BigDecimal codEmplNuevo) {
        this.codEmplNuevo = codEmplNuevo;
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVEmpresas");
        infoRegistroEmpresas = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

}
