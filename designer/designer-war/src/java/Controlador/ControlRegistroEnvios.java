/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Inforeportes;
import Entidades.envioCorreos;
import InterfaceAdministrar.AdministrarEnvioCorreosInterface;
import InterfaceAdministrar.AdministrarNReportesNominaInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlRegistroEnvios")
@SessionScoped
public class ControlRegistroEnvios implements Serializable {

    @EJB
    AdministrarEnvioCorreosInterface administrarEnvioCorreos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarNReportesNominaInterface administrarNReportesNomina;
    //------------------------------------------------------------------------------------------
    //ATRIBUTOS
    //------------------------------------------------------------------------------------------
    private List<envioCorreos> enviocorreos;
    private List<envioCorreos> filterEC;
    private envioCorreos envioSeleccionado;
    private DataTable tablaEC;
//Pruebas para modificar
    private List<envioCorreos> listECModificar;
    private boolean guardado;

    //borrar VC
    private List<envioCorreos> listECBorrar;
    //editar celda
    private envioCorreos editarEC;
    private int cualCelda, tipoLista;
    private boolean aceptar;
    private String infoRegistro;
    private String altoTabla;
    private int bandera;
    String paginaAnterior;
    //Columnas Tabla VC
    private Column ecFecha, ecEmpleado, ecNombre, ecCorreo, ecEstado;
//
    private Date fecha;
    //AUTOCOMPLETAR
    private String nombre, correo, estado;
    private Empleados empleado;
    private Inforeportes reporteActual;
    private String nombreReporte;
    private String reenviar;

    //private List<Empleados> empl;
    public ControlRegistroEnvios() {
        System.out.println("Controlador.ControlRegistroEnvios()");
        bandera = 0;
        enviocorreos = null;
        fecha = new Date();
        aceptar = true;
        //borrar aficiones
        listECBorrar = new ArrayList();
        listECModificar = new ArrayList();
        //editar
        editarEC = new envioCorreos();
        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        altoTabla = "170";
        envioSeleccionado = null;
//        reporteActual = new Inforeportes();
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEnvioCorreos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error Controlador.ControlRegistroEnvios.inicializarAdministrador()" + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

//    public void recibirPagina(String pagina, Inforeportes secReporte) {
//        System.out.println("Controlador.ControlRegistroEnvios.recibirPagina()");
//        System.out.println("Pagina: " + paginaAnterior);
//        System.out.println("secReporte: " + secReporte);
//
//        paginaAnterior = pagina;
//        recibirReporte(secReporte);
//    }
    public void recibirPagina(String pagina, BigInteger secReporte) {
        System.out.println("Controlador.ControlRegistroEnvios.recibirPagina()");
        System.out.println("pagina: " + pagina);
        paginaAnterior = pagina;
        Inforeportes reporte = administrarEnvioCorreos.consultarPorSecuencia(secReporte);
        reporteActual = reporte;
        nombreReporte = reporteActual.getNombre();
//        if (reporteActual != null) {
//            nombreReporte = reporteActual.getNombre();
//            System.out.println("reporte actual: " + reporteActual.getSecuencia());
//            System.out.println("nombreReporte: " + nombreReporte);
//        }
        //getEnviocorreos();
        //cargarListaCorreos(reporteActual.getSecuencia());
        System.out.println("reporteActual: " + reporteActual);
    }

    //TOOLBAR
    //GUARDAR CAMBIOS
    public void guardarCambios() {
        System.out.println("Controlador.ControlRegistroEnvios.guardarCambios()");
        if (guardado == false) {
            System.out.println("listECBorrar.size(): " + listECBorrar.size());
            if (!listECBorrar.isEmpty()) {
                for (int i = 0; i < listECBorrar.size(); i++) {
                    administrarEnvioCorreos.borrarEnvioCorreos(listECBorrar.get(i));
                }
                listECBorrar.clear();
            }
            if (!listECModificar.isEmpty()) {
                administrarEnvioCorreos.modificarEC(listECModificar);
                listECModificar.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            enviocorreos = null;
            getEnviocorreos();
            System.out.println("enviocorreos: " + enviocorreos);
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosEC");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    //EDITAR
    public void editarCelda() {
        if (enviocorreos != null) {
            editarEC = envioSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
                RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleado");
                RequestContext.getCurrentInstance().execute("PF('editarEmpleado').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombre");
                RequestContext.getCurrentInstance().execute("PF('editarNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCorreo");
                RequestContext.getCurrentInstance().execute("PF('editarCorreo').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEstado");
                RequestContext.getCurrentInstance().execute("PF('editarEstado').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            System.out.println("Activar");
            ecFecha = (Column) c.getViewRoot().findComponent("form:datosEC:ecFecha");
            ecFecha.setFilterStyle("width: 85% !important;");
            ecEmpleado = (Column) c.getViewRoot().findComponent("form:datosEC:ecEmpleado");
            ecEmpleado.setFilterStyle("width: 85% !important;");
            ecNombre = (Column) c.getViewRoot().findComponent("form:datosEC:ecNombre");
            ecNombre.setFilterStyle("width: 85% !important;");
            ecCorreo = (Column) c.getViewRoot().findComponent("form:datosEC:ecCorreo");
            ecCorreo.setFilterStyle("width: 85% !important;");
            ecEstado = (Column) c.getViewRoot().findComponent("form:datosEC:ecEstado");
            ecEstado.setFilterStyle("width: 85% !important;");
            altoTabla = "150";
            RequestContext.getCurrentInstance().update("form:datosEC");
            bandera = 1;
        } else {
            cerrarFiltrado();
        }
        cualCelda = -1;
    }

    public void cerrarFiltrado() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            ecFecha = (Column) c.getViewRoot().findComponent("form:datosEC:ecFecha");
            ecFecha.setFilterStyle("display: none; visibility: hidden;");
            ecEmpleado = (Column) c.getViewRoot().findComponent("form:datosEC:ecEmpleado");
            ecEmpleado.setFilterStyle("display: none; visibility: hidden;");
            ecNombre = (Column) c.getViewRoot().findComponent("form:datosEC:ecNombre");
            ecNombre.setFilterStyle("display: none; visibility: hidden;");
            ecCorreo = (Column) c.getViewRoot().findComponent("form:datosEC:ecCorreo");
            ecCorreo.setFilterStyle("display: none; visibility: hidden;");
            ecEstado = (Column) c.getViewRoot().findComponent("form:datosEC:ecEstado");
            ecEstado.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "170";
            RequestContext.getCurrentInstance().update("form:datosEC");
            bandera = 0;
            filterEC = null;
            tipoLista = 0;
        }
    }

    //BORRAR EC
    public void borrarEC() {
        System.out.println("Controlador.ControlRegistroEnvios.borrarEC()");
        System.out.println("envioSeleccionado: " + envioSeleccionado);
        if (envioSeleccionado != null) {
            if (!listECModificar.isEmpty() && listECModificar.contains(envioSeleccionado)) {
                int modIndex = listECModificar.indexOf(envioSeleccionado);
                listECModificar.remove(modIndex);
                listECBorrar.add(envioSeleccionado);
            } else {
                listECBorrar.add(envioSeleccionado);
            }
            enviocorreos.remove(envioSeleccionado);
            if (tipoLista == 1) {
                cerrarFiltrado();
            }
            contarRegistros();
            envioSeleccionado = null;
            RequestContext.getCurrentInstance().update("form:datosEC");
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarTodos() {
        System.out.println(this.getClass().getName() + ".borrarTodos()");
        System.out.println("enviocorreos.size(): " + enviocorreos.size());
        if (enviocorreos != null || !enviocorreos.isEmpty()) {
            System.out.println("listECBorrar: " + listECBorrar);
            for (int i = 0; i < enviocorreos.size(); i++) {
                listECBorrar.add(enviocorreos.get(i));
            }
            enviocorreos.removeAll(listECBorrar);
            if (guardado) {
                guardado = false;
            }
            if (tipoLista == 1) {
                cerrarFiltrado();
            }
            enviocorreos.clear();
            RequestContext.getCurrentInstance().update("form:datosEC");
        }
    }

    public void modificarCorreo(envioCorreos ecorreos, String valorConfirmar) {
        envioSeleccionado = ecorreos;
//            for (int i = 0; i < enviocorreos.size(); i++) {
//                if (enviocorreos.get(i) == envioSeleccionado) {
//                    i++;
//                    if (i >= enviocorreos.size()) {
//                        break;
//                    }
//                }
//                if (envioSeleccionado.getFecha().compareTo(envioSeleccionado.getFecha()) == 0) {
//                    envioSeleccionado.setFecha(fecha);
//                }
//            }
        if (listECModificar.isEmpty()) {
            listECModificar.add(envioSeleccionado);
        } else if (!listECModificar.contains(envioSeleccionado)) {
            listECModificar.add(envioSeleccionado);
        }
        if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void cancelarModificaciones() {
        System.out.println("Controlador.ControlRegistroEnvios.cancelarModificaciones()");
        cerrarFiltrado();
        listECBorrar.clear();
        listECModificar.clear();
        enviocorreos = null;
        envioSeleccionado = null;
        guardado = true;
        //permitirIndex = true;
//        cargarListaCorreos(reporteActual.getSecuencia());
        getEnviocorreos();
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosEC");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        System.out.println("Controlador.ControlRegistroEnvios.salir()");
        cerrarFiltrado();
        listECBorrar.clear();
        listECModificar.clear();
        envioSeleccionado = null;
        enviocorreos = null;
        guardado = true;
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void eventoFiltrar() {
        envioSeleccionado = null;
        contarRegistros();
    }

    public void cambiarIndice(envioCorreos correos, int celda) {
        envioSeleccionado = correos;
        cualCelda = celda;
        if (cualCelda == 0) {
            fecha = envioSeleccionado.getFecha();

        } else if (cualCelda == 1) {
            empleado = envioSeleccionado.getCodigoEmpleado();

        } else if (cualCelda == 2) {
            nombre = envioSeleccionado.getNombreEmpleado();

        } else if (cualCelda == 3) {
            correo = envioSeleccionado.getCorreo();

        } else if (cualCelda == 4) {
            estado = envioSeleccionado.getEstado();
        }
    }

    public void dispararDialogoGuardarCambios() {
        System.out.println(this.getClass().getName() + ".dispararDialogoGuardarCambios()");
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

    }

    public void guardarYSalir() {
        System.out.println(this.getClass().getName() + ".guardarYSalir()");
        guardarCambios();
        salir();
    }

    public void cancelarYSalir() {
        System.out.println(this.getClass().getName() + ".cancelarYSalir()");
        cancelarModificaciones();
        salir();
    }
    
//    public void validarReenviar() {
//        if (conexionEmpleado.isEnvioCorreo()) {
//            if (administrarGenerarReporte.enviarCorreo(conexionEmpleado.getEmpleado().getEmpresa().getSecuencia(), email,
//                    "Reporte Kiosko - " + reporte.getDescripcion(), "Mensaje enviado automáticamente, por favor no responda a este correo.",
//                    pathReporteGenerado)) {
//                MensajesUI.info("El reporte ha sido enviado exitosamente.");
//                PrimefacesContextUI.actualizar("principalForm:growl");
//            } else {
//                MensajesUI.error("No fue posible enviar el correo, por favor comuníquese con soporte.");
//                PrimefacesContextUI.actualizar("principalForm:growl");
//            }
//        }
//    }

//    public void cargarListaCorreos(BigInteger secReporte) {
//        System.out.println("Controlador.ControlRegistroEnvios.cargarListaCorreos()");
//        System.out.println("secReporte: " + secReporte);
//        if (enviocorreos == null) {
//            enviocorreos = administrarEnvioCorreos.consultarEnvioCorreos(secReporte);
//        }
//    }
    //GET & SET
    public List<envioCorreos> getEnviocorreos() {
//        if (enviocorreos == null && reporteActual != null) {
//            cargarListaCorreos(reporteActual.getSecuencia());
//        }
        if (enviocorreos == null) {
            enviocorreos = administrarEnvioCorreos.consultarEnvioCorreos(reporteActual.getSecuencia());
        }
        return enviocorreos;
    }

    public Inforeportes getReporteActual() {
        return reporteActual;
    }

    public void setReporteActual(Inforeportes reporteActual) {
        this.reporteActual = reporteActual;
    }

    public void setEnviocorreos(List<envioCorreos> enviocorreos) {
        this.enviocorreos = enviocorreos;
    }

    public List<envioCorreos> getFilterEC() {
        return filterEC;
    }

    public void setFilterEC(List<envioCorreos> filterEC) {
        this.filterEC = filterEC;
    }

    public envioCorreos getEnvioSeleccionado() {
        return envioSeleccionado;
    }

    public void setEnvioSeleccionado(envioCorreos envioSeleccionado) {
        this.envioSeleccionado = envioSeleccionado;
    }

    public DataTable getTablaEC() {
        return tablaEC;
    }

    public void setTablaEC(DataTable tablaEC) {
        this.tablaEC = tablaEC;
    }

    public List<envioCorreos> getListECModificar() {
        return listECModificar;
    }

    public void setListECModificar(List<envioCorreos> listECModificar) {
        this.listECModificar = listECModificar;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public List<envioCorreos> getListECBorrar() {
        return listECBorrar;
    }

    public void setListECBorrar(List<envioCorreos> listECBorrar) {
        this.listECBorrar = listECBorrar;
    }

    public envioCorreos getEditarEC() {
        return editarEC;
    }

    public void setEditarEC(envioCorreos editarEC) {
        this.editarEC = editarEC;
    }

    public int getCualCelda() {
        return cualCelda;
    }

    public void setCualCelda(int cualCelda) {
        this.cualCelda = cualCelda;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEC");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public Column getEcFecha() {
        return ecFecha;
    }

    public void setEcFecha(Column ecFecha) {
        this.ecFecha = ecFecha;
    }

    public Column getEcEmpleado() {
        return ecEmpleado;
    }

    public void setEcEmpleado(Column ecEmpleado) {
        this.ecEmpleado = ecEmpleado;
    }

    public Column getEcNombre() {
        return ecNombre;
    }

    public void setEcNombre(Column ecNombre) {
        this.ecNombre = ecNombre;
    }

    public Column getEcEstado() {
        return ecEstado;
    }

    public void setEcEstado(Column ecEstado) {
        this.ecEstado = ecEstado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public String getReenviar() {
        return reenviar;
    }

    public void setReenviar(String reenviar) {
        this.reenviar = reenviar;
    }

}
