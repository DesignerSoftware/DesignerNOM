/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmpleadosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
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
@Named(value = "controlEmpleado")
@SessionScoped
public class ControlEmpleado implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarEmpleadosInterface administrarEmpleados;

    private List<Empleados> listaEmpleados;
    private List<Empleados> listaFiltrarEmpleados;
    private List<Empleados> listaEmpleadosModificar;
    private Empleados empleadoSeleccionado;
    private Empleados editarEmpleado;

    private List<Empleados> lovEmpleados;
    private List<Empleados> filtrarlovEmpleados;
    private Empleados empleadoLovSeleccionado;

    private Column codigoEmpl, nombreEmpl, codDeudor, codAcreedor, fechaCreacion;
    private boolean aceptar;
    private boolean guardado;
    private int cualCelda, bandera, tipoLista;
    private String infoRegistro, infoRegistroLov, altoTabla, paginaanterior;
    private DataTable tablaC;
    private Boolean activarLov, activarInsertar, activarBorrar, activarDuplicar;

    public ControlEmpleado() {
        altoTabla = "315";
        lovEmpleados = null;
        aceptar = true;
        listaEmpleadosModificar = new ArrayList<Empleados>();
        listaFiltrarEmpleados = null;
        editarEmpleado = new Empleados();
        cualCelda = -1;
        guardado = true;
        tipoLista = 0;
        activarBorrar = true;
        activarDuplicar = true;
        activarInsertar = true;
        activarLov = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEmpleados.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String pagina) {
        paginaanterior = pagina;
        listaEmpleados = null;
        getListaEmpleados();
        contarRegistros();
        if (listaEmpleados != null) {
            if (!listaEmpleados.isEmpty()) {
                empleadoSeleccionado = listaEmpleados.get(0);
            }
        }
    }

    public String retornarPagina() {
        return paginaanterior;
    }

    public void cambiarIndice(Empleados empleado, int celda) {
        empleadoSeleccionado = empleado;
        cualCelda = celda;
        if (cualCelda == 0) {
            empleadoSeleccionado.getCodigoempleado();
        } else if (cualCelda == 1) {
            empleadoSeleccionado.getPersona().getNombreCompleto();
        } else if (cualCelda == 2) {
            empleadoSeleccionado.getCodigoalternativodeudor();
        } else if (cualCelda == 3) {
            empleadoSeleccionado.getCodigoalternativoacreedor();
        } else if (cualCelda == 4) {
            empleadoSeleccionado.getFechacreacion();

        }
    }

      public void guardarSalir() {
        guardarCambios();
        salir();
    }

    public void cancelarSalir() {
        cancelarModificacion();
        salir();
    }
    
    public void guardarCambios() {
        try {
            if (guardado == false) {
                if (!listaEmpleadosModificar.isEmpty()) {
                    administrarEmpleados.editarEmpleado(listaEmpleadosModificar);
                    listaEmpleadosModificar.clear();
                }
                listaEmpleados = null;
                getListaEmpleados();
                contarRegistros();
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                empleadoSeleccionado = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosEmpleados");

        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {

            codigoEmpl = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:codigoEmpl");
            codigoEmpl.setFilterStyle("display: none; visibility: hidden;");
            nombreEmpl = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:nombreEmpl");
            nombreEmpl.setFilterStyle("display: none; visibility: hidden;");
            codDeudor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:codDeudor");
            codDeudor.setFilterStyle("display: none; visibility: hidden;");
            codAcreedor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:codAcreedor");
            codAcreedor.setFilterStyle("display: none; visibility: hidden;");
            fechaCreacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:fechaCreacion");
            fechaCreacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEmpleados");
            bandera = 0;
            listaFiltrarEmpleados = null;
            tipoLista = 0;
            altoTabla = "315";
        }

        listaEmpleadosModificar.clear();
        listaEmpleados = null;
        empleadoSeleccionado = null;
        guardado = true;
        getListaEmpleados();
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:infoRegistro");
        context.update("form:datosEmpleados");
        context.update("form:ACEPTAR");
    }

    public void editarCelda() {

        if (empleadoSeleccionado != null) {
            editarEmpleado = empleadoSeleccionado;

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                context.update("formularioDialogos:editarCodigoE");
                context.execute("editarCodigoE.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editarNombreE");
                context.execute("editarNombreE.show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                context.update("formularioDialogos:editarCodDeudorE");
                context.execute("editarCodDeudorE.show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                context.update("formularioDialogos:editarCodAcreeE");
                context.execute("editarCodAcreeE.show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                context.update("formularioDialogos:editarFechaE");
                context.execute("editarFechaE.show()");
                cualCelda = -1;
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("seleccionarRegistro.show()");
        }
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            altoTabla = "291";
            codigoEmpl = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:codigoEmpl");
            codigoEmpl.setFilterStyle("width: 85%");
            nombreEmpl = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:nombreEmpl");
            nombreEmpl.setFilterStyle("width: 85%");
            codDeudor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:codDeudor");
            codDeudor.setFilterStyle("width: 85%");
            codAcreedor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:codAcreedor");
            codAcreedor.setFilterStyle("width: 85%");
            fechaCreacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:fechaCreacion");
            fechaCreacion.setFilterStyle("width: 85%");
            RequestContext.getCurrentInstance().update("form:datosEmpleados");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "315";
            codigoEmpl = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:codigoEmpl");
            codigoEmpl.setFilterStyle("display: none; visibility: hidden;");
            nombreEmpl = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:nombreEmpl");
            nombreEmpl.setFilterStyle("display: none; visibility: hidden;");
            codDeudor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:codDeudor");
            codDeudor.setFilterStyle("display: none; visibility: hidden;");
            codAcreedor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:codAcreedor");
            codAcreedor.setFilterStyle("display: none; visibility: hidden;");
            fechaCreacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:fechaCreacion");
            fechaCreacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEmpleados");
            bandera = 0;
            listaFiltrarEmpleados = null;
            tipoLista = 0;
        }
    }

    public void salir() {
        if (bandera == 1) {
            altoTabla = "315";
            codigoEmpl = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:codigoEmpl");
            codigoEmpl.setFilterStyle("display: none; visibility: hidden;");
            nombreEmpl = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:nombreEmpl");
            nombreEmpl.setFilterStyle("display: none; visibility: hidden;");
            codDeudor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:codDeudor");
            codDeudor.setFilterStyle("display: none; visibility: hidden;");
            codAcreedor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:codAcreedor");
            codAcreedor.setFilterStyle("display: none; visibility: hidden;");
            fechaCreacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpleados:fechaCreacion");
            fechaCreacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEmpleados");
            bandera = 0;
            listaFiltrarEmpleados = null;
            tipoLista = 0;
        }
        listaEmpleadosModificar.clear();
        empleadoSeleccionado = null;
        guardado = true;
        listaEmpleados = null;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void posicionOtro() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndice(listaEmpleados.get(indice), columna);

    }
    
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpleadosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "EmpleadosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpleadosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "EmpleadosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        modificarInfoRegistro(listaFiltrarEmpleados.size());
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void modificarInfoRegistroLovEmpleados(int valor) {
        infoRegistroLov = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovEmpleados");
    }

    public void eventoFiltrarLovEmpleados() {
        modificarInfoRegistroLovEmpleados(filtrarlovEmpleados.size());
    }

    public void contarRegistros() {
        if (listaEmpleados != null) {
            modificarInfoRegistro(listaEmpleados.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void recordarSeleccion() {
        if (empleadoSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosEmpleados");
            tablaC.setSelection(empleadoSeleccionado);
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (empleadoLovSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(empleadoSeleccionado.getSecuencia(), "EMPLEADOS");
            if (resultado == 1) {
                context.execute("errorObjetosDB.show()");
            } else if (resultado == 2) {
                context.execute("confirmarRastro.show()");
            } else if (resultado == 3) {
                context.execute("errorRegistroRastro.show()");
            } else if (resultado == 4) {
                context.execute("errorTablaConRastro.show()");
            } else if (resultado == 5) {
                context.execute("errorTablaSinRastro.show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("empleadoSeleccionado")) {
            context.execute("confirmarRastroHistorico.show()");
        } else {
            context.execute("errorRastroHistorico.show()");
        }
    }

    public void dispararLovEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
//     getLovEmpleados();
        modificarInfoRegistroLovEmpleados(lovEmpleados.size());
        context.update("formularioDialogos:empleadosDialogo");
        context.execute("empleadosDialogo.show()");
    }

    public void modificarEmpleados(Empleados empleado) {
        RequestContext context = RequestContext.getCurrentInstance();
        empleadoSeleccionado = empleado;
        if (tipoLista == 0) {

            if (listaEmpleadosModificar.isEmpty()) {
                listaEmpleadosModificar.add(empleadoSeleccionado);
            } else if (!listaEmpleadosModificar.contains(empleadoSeleccionado)) {
                listaEmpleadosModificar.add(empleadoSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        } else {

            if (listaEmpleadosModificar.isEmpty()) {
                listaEmpleadosModificar.add(empleadoSeleccionado);
            } else if (!listaEmpleadosModificar.contains(empleadoSeleccionado)) {
                listaEmpleadosModificar.add(empleadoSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        }
        context.update("form:datosEmpleados");
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
        contarRegistros();
        context.update("form:datosEmpleados");

        aceptar = true;
        cualCelda = -1;
    }

    public void actualizarEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        Empleados e = empleadoLovSeleccionado;

        if (!listaEmpleados.isEmpty()) {
            listaEmpleados.clear();
            listaEmpleados.add(e);
            empleadoSeleccionado = listaEmpleados.get(0);
        } else {
            listaEmpleados.add(e);
        }
        modificarInfoRegistro(listaEmpleados.size());
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        context.execute("LOVEmpleados.clearFilters()");
        context.execute("empleadosDialogo.hide()");
        context.update("formularioDialogos:LOVEmpleados");
        context.update("form:datosEmpleados");
        context.update("formularioDialogos:aceptarD");

        empleadoLovSeleccionado = null;
        aceptar = true;
        cualCelda = -1;
    }

    public void cancelarCambioEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtrarlovEmpleados = null;
        empleadoLovSeleccionado = null;
        aceptar = true;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        context.execute("LOVEmpleados.clearFilters()");
        context.execute("empleadosDialogo.hide()");
        context.update("formularioDialogos:LOVEmpleados");
        context.update("form:datosEmpleados");
        context.update("formularioDialogos:aceptarD");
    }

///////GETS Y SETS///////
    public List<Empleados> getListaEmpleados() {
        try {
            if (listaEmpleados == null) {
                listaEmpleados = administrarEmpleados.listaEmpleados();
            }
            return listaEmpleados;
        } catch (Exception e) {
            System.out.println("Error !! getListaEmpleados");
            return null;
        }
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public List<Empleados> getListaFiltrarEmpleados() {
        return listaFiltrarEmpleados;
    }

    public void setListaFiltrarEmpleados(List<Empleados> listaFiltrarEmpleados) {
        this.listaFiltrarEmpleados = listaFiltrarEmpleados;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public List<Empleados> getLovEmpleados() {
        try {
            if (lovEmpleados == null) {
                lovEmpleados = administrarEmpleados.listaEmpleados();
            }
            return lovEmpleados;
        } catch (Exception e) {
            System.out.println("Error !! getLovEmpleados : " + e.toString());
            return null;
        }

    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public List<Empleados> getFiltrarlovEmpleados() {
        return filtrarlovEmpleados;
    }

    public void setFiltrarlovEmpleados(List<Empleados> filtrarlovEmpleados) {
        this.filtrarlovEmpleados = filtrarlovEmpleados;
    }

    public Empleados getEmpleadoLovSeleccionado() {
        return empleadoLovSeleccionado;
    }

    public void setEmpleadoLovSeleccionado(Empleados empleadoLovSeleccionado) {
        this.empleadoLovSeleccionado = empleadoLovSeleccionado;
    }

    public Empleados getEditarEmpleado() {
        return editarEmpleado;
    }

    public void setEditarEmpleado(Empleados editarEmpleado) {
        this.editarEmpleado = editarEmpleado;
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

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public Boolean getActivarLov() {
        return activarLov;
    }

    public void setActivarLov(Boolean activarLov) {
        this.activarLov = activarLov;
    }

    public Boolean getActivarInsertar() {
        return activarInsertar;
    }

    public void setActivarInsertar(Boolean activarInsertar) {
        this.activarInsertar = activarInsertar;
    }

    public Boolean getActivarBorrar() {
        return activarBorrar;
    }

    public void setActivarBorrar(Boolean activarBorrar) {
        this.activarBorrar = activarBorrar;
    }

    public Boolean getActivarDuplicar() {
        return activarDuplicar;
    }

    public void setActivarDuplicar(Boolean activarDuplicar) {
        this.activarDuplicar = activarDuplicar;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getPaginaanterior() {
        return paginaanterior;
    }

    public void setPaginaanterior(String paginaanterior) {
        this.paginaanterior = paginaanterior;
    }

    public String getInfoRegistroLov() {
        return infoRegistroLov;
    }

    public void setInfoRegistroLov(String infoRegistroLov) {
        this.infoRegistroLov = infoRegistroLov;
    }

}