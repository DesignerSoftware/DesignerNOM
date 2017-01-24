/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.PryClientes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPryClientesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlPryClientes implements Serializable {

    @EJB
    AdministrarPryClientesInterface administrarPryClientes;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<PryClientes> listPryClientes;
    private List<PryClientes> filtrarPryClientes;
    private List<PryClientes> crearPryClientes;
    private List<PryClientes> modificarPryClientes;
    private List<PryClientes> borrarPryClientes;
    private PryClientes nuevoPryCliente;
    private PryClientes duplicarPryCliente;
    private PryClientes editarPryCliente;
    private PryClientes pryClienteSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column nombre, direccion, telefono, contacto;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private String infoRegistro;
    private int tamano;
    private DataTable tablaC;
    private String backUpDescripcion;
    private String backUpDireccion;
    private String backUpTelefono;
    private String backUpNombreContacto;
    private boolean activarLOV;

    public ControlPryClientes() {
        listPryClientes = null;
        crearPryClientes = new ArrayList<PryClientes>();
        modificarPryClientes = new ArrayList<PryClientes>();
        borrarPryClientes = new ArrayList<PryClientes>();
        permitirIndex = true;
        editarPryCliente = new PryClientes();
        nuevoPryCliente = new PryClientes();
        duplicarPryCliente = new PryClientes();
        guardado = true;
        tamano = 270;
        activarLOV = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPryClientes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
    private String paginaAnterior;

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listPryClientes = null;
        getListPryClientes();
        if (listPryClientes != null) {
            if (!listPryClientes.isEmpty()) {
                pryClienteSeleccionado = listPryClientes.get(0);
            }
        }
    }

    public String redirigirPaginaAnterior() {
        return paginaAnterior;
    }

    public void cambiarIndice(PryClientes pryCliente, int celda) {

        if (permitirIndex == true) {
            pryClienteSeleccionado = pryCliente;
            cualCelda = celda;
            pryClienteSeleccionado.getSecuencia();
            if (cualCelda == 0) {
                backUpDescripcion = pryClienteSeleccionado.getNombre();
            }
            if (cualCelda == 1) {
                backUpDireccion = pryClienteSeleccionado.getDireccion();
            }
            if (cualCelda == 2) {
                backUpTelefono = pryClienteSeleccionado.getTelefono();
            }
            if (cualCelda == 3) {
                backUpNombreContacto = pryClienteSeleccionado.getContacto();
            }

        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            nombre = (Column) c.getViewRoot().findComponent("form:datosPryCliente:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            direccion = (Column) c.getViewRoot().findComponent("form:datosPryCliente:direccion");
            direccion.setFilterStyle("display: none; visibility: hidden;");
            telefono = (Column) c.getViewRoot().findComponent("form:datosPryCliente:telefono");
            telefono.setFilterStyle("display: none; visibility: hidden;");
            contacto = (Column) c.getViewRoot().findComponent("form:datosPryCliente:contacto");
            contacto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPryCliente");
            bandera = 0;
            filtrarPryClientes = null;
            tipoLista = 0;
        }
        borrarPryClientes.clear();
        crearPryClientes.clear();
        modificarPryClientes.clear();
        k = 0;
        listPryClientes = null;
        pryClienteSeleccionado = null;
        guardado = true;
        permitirIndex = true;
        getListPryClientes();
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosPryCliente");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            nombre = (Column) c.getViewRoot().findComponent("form:datosPryCliente:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            direccion = (Column) c.getViewRoot().findComponent("form:datosPryCliente:direccion");
            direccion.setFilterStyle("display: none; visibility: hidden;");
            telefono = (Column) c.getViewRoot().findComponent("form:datosPryCliente:telefono");
            telefono.setFilterStyle("display: none; visibility: hidden;");
            contacto = (Column) c.getViewRoot().findComponent("form:datosPryCliente:contacto");
            contacto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPryCliente");
            bandera = 0;
            filtrarPryClientes = null;
            tipoLista = 0;
        }

        borrarPryClientes.clear();
        crearPryClientes.clear();
        modificarPryClientes.clear();
        k = 0;
        listPryClientes = null;
        guardado = true;
        permitirIndex = true;
        getListPryClientes();
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosPryCliente");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            nombre = (Column) c.getViewRoot().findComponent("form:datosPryCliente:nombre");
            nombre.setFilterStyle("width: 85% !important");
            direccion = (Column) c.getViewRoot().findComponent("form:datosPryCliente:direccion");
            direccion.setFilterStyle("width: 85% !important");
            telefono = (Column) c.getViewRoot().findComponent("form:datosPryCliente:telefono");
            telefono.setFilterStyle("width: 85% !important");
            contacto = (Column) c.getViewRoot().findComponent("form:datosPryCliente:contacto");
            contacto.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosPryCliente");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            nombre = (Column) c.getViewRoot().findComponent("form:datosPryCliente:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            direccion = (Column) c.getViewRoot().findComponent("form:datosPryCliente:direccion");
            direccion.setFilterStyle("display: none; visibility: hidden;");
            telefono = (Column) c.getViewRoot().findComponent("form:datosPryCliente:telefono");
            telefono.setFilterStyle("display: none; visibility: hidden;");
            contacto = (Column) c.getViewRoot().findComponent("form:datosPryCliente:contacto");
            contacto.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosPryCliente");
            bandera = 0;
            filtrarPryClientes = null;
            tipoLista = 0;
        }
    }

    public void modificarPryCliente(PryClientes pryCliente) {
        pryClienteSeleccionado = pryCliente;
        if (!crearPryClientes.contains(pryClienteSeleccionado)) {
            if (modificarPryClientes.isEmpty()) {
                modificarPryClientes.add(pryClienteSeleccionado);
            } else if (!modificarPryClientes.contains(pryClienteSeleccionado)) {
                modificarPryClientes.add(pryClienteSeleccionado);
            }
        }
        if (guardado == true) {
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:datosPryCliente");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoPryCliente() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (pryClienteSeleccionado != null) {
            if (!modificarPryClientes.isEmpty() && modificarPryClientes.contains(pryClienteSeleccionado)) {
                modificarPryClientes.remove(modificarPryClientes.indexOf(pryClienteSeleccionado));
                borrarPryClientes.add(pryClienteSeleccionado);
            } else if (!crearPryClientes.isEmpty() && crearPryClientes.contains(pryClienteSeleccionado)) {
                crearPryClientes.remove(crearPryClientes.indexOf(pryClienteSeleccionado));
            } else {
                borrarPryClientes.add(pryClienteSeleccionado);
            }
            listPryClientes.remove(pryClienteSeleccionado);

            if (tipoLista == 1) {
                filtrarPryClientes.remove(pryClienteSeleccionado);
                listPryClientes.remove(pryClienteSeleccionado);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosPryCliente");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");

        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        try {
            BigInteger proyectos = new BigInteger("-1");
            System.err.println("Control Secuencia de ControlPryClientes ");
            if (tipoLista == 0) {
                proyectos = administrarPryClientes.contarProyectosPryCliente(pryClienteSeleccionado.getSecuencia());
            } else {
                proyectos = administrarPryClientes.contarProyectosPryCliente(pryClienteSeleccionado.getSecuencia());
            }
            if (proyectos.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoPryCliente();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                pryClienteSeleccionado = null;
                proyectos = new BigInteger("-1");
            }
        } catch (Exception e) {
            System.err.println("ERROR ControlEvalCompetencias verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarPryClientes.isEmpty() || !crearPryClientes.isEmpty() || !modificarPryClientes.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarPryCliente() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarPryCliente");
            if (!borrarPryClientes.isEmpty()) {
                administrarPryClientes.borrarPryClientes(borrarPryClientes);

                //mostrarBorrados
                registrosBorrados = borrarPryClientes.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarPryClientes.clear();
            }
            if (!crearPryClientes.isEmpty()) {
                administrarPryClientes.crearPryClientes(crearPryClientes);
                crearPryClientes.clear();
            }
            if (!modificarPryClientes.isEmpty()) {
                administrarPryClientes.modificarPryClientes(modificarPryClientes);
                modificarPryClientes.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listPryClientes = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosPryCliente");
            k = 0;
            guardado = true;
        }
        pryClienteSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (pryClienteSeleccionado != null) {
            if (tipoLista == 0) {
                editarPryCliente = pryClienteSeleccionado;
            }
            if (tipoLista == 1) {
                editarPryCliente = pryClienteSeleccionado;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
                RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDireccion");
                RequestContext.getCurrentInstance().execute("PF('editDireccion').show()");
                cualCelda = -1;

            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTelefono");
                RequestContext.getCurrentInstance().execute("PF('editTelefono').show()");
                cualCelda = -1;

            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editContacto");
                RequestContext.getCurrentInstance().execute("PF('editContacto').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoPryClientes() {
        System.out.println("agregarNuevoPryClientes");
        int contador = 0;
        int duplicados = 0;

        Short a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoPryCliente.getNombre() == null || nuevoPryCliente.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 1) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                nombre = (Column) c.getViewRoot().findComponent("form:datosPryCliente:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                direccion = (Column) c.getViewRoot().findComponent("form:datosPryCliente:direccion");
                direccion.setFilterStyle("display: none; visibility: hidden;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosPryCliente:telefono");
                telefono.setFilterStyle("display: none; visibility: hidden;");
                contacto = (Column) c.getViewRoot().findComponent("form:datosPryCliente:contacto");
                contacto.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPryCliente");
                bandera = 0;
                filtrarPryClientes = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoPryCliente.setSecuencia(l);
            crearPryClientes.add(nuevoPryCliente);
            listPryClientes.add(nuevoPryCliente);
            contarRegistros();
            nuevoPryCliente = new PryClientes();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPryClientes').hide()");
            pryClienteSeleccionado = nuevoPryCliente;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoPryC");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoPryC').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoPryClientes() {
        System.out.println("limpiarNuevoPryClientes");
        nuevoPryCliente = new PryClientes();
        pryClienteSeleccionado = null;

    }

    //------------------------------------------------------------------------------
    public void duplicandoPryClientes() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (pryClienteSeleccionado != null) {
            duplicarPryCliente = new PryClientes();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarPryCliente.setSecuencia(l);
                duplicarPryCliente.setNombre(pryClienteSeleccionado.getNombre());
                duplicarPryCliente.setDireccion(pryClienteSeleccionado.getDireccion());
                duplicarPryCliente.setTelefono(pryClienteSeleccionado.getTelefono());
                duplicarPryCliente.setContacto(pryClienteSeleccionado.getContacto());
            }
            if (tipoLista == 1) {
                duplicarPryCliente.setSecuencia(l);
                duplicarPryCliente.setNombre(pryClienteSeleccionado.getNombre());
                duplicarPryCliente.setDireccion(pryClienteSeleccionado.getDireccion());
                duplicarPryCliente.setTelefono(pryClienteSeleccionado.getTelefono());
                duplicarPryCliente.setContacto(pryClienteSeleccionado.getContacto());
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPRYC");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPryClientes').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar nombre " + duplicarPryCliente.getNombre());
        System.err.println("ConfirmarDuplicar direccion " + duplicarPryCliente.getDireccion());

        if (duplicarPryCliente.getNombre() == null || duplicarPryCliente.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " No pueden haber cambios vacios \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (contador == 1) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarPryCliente.setSecuencia(l);
            System.out.println("Datos Duplicando: " + duplicarPryCliente.getSecuencia() + "  " + duplicarPryCliente.getNombre());
            if (crearPryClientes.contains(duplicarPryCliente)) {
                System.out.println("Ya lo contengo.");
            }
            listPryClientes.add(duplicarPryCliente);
            crearPryClientes.add(duplicarPryCliente);
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosPryCliente");
            pryClienteSeleccionado = duplicarPryCliente;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                nombre = (Column) c.getViewRoot().findComponent("form:datosPryCliente:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                direccion = (Column) c.getViewRoot().findComponent("form:datosPryCliente:direccion");
                direccion.setFilterStyle("display: none; visibility: hidden;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosPryCliente:telefono");
                telefono.setFilterStyle("display: none; visibility: hidden;");
                contacto = (Column) c.getViewRoot().findComponent("form:datosPryCliente:contacto");
                contacto.setFilterStyle("display: none; visibility: hidden;");

                RequestContext.getCurrentInstance().update("form:datosPryCliente");
                bandera = 0;
                filtrarPryClientes = null;
                tipoLista = 0;
            }
            duplicarPryCliente = new PryClientes();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPryClientes').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarPryC");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarPryC').show()");
        }
    }

    public void limpiarDuplicarPryClientes() {
        duplicarPryCliente = new PryClientes();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPryClienteExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "PRY_CLIENTES", false, false, "UTF-8", null, null);
        context.responseComplete();
        pryClienteSeleccionado = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPryClienteExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "PRY_CLIENTES", false, false, "UTF-8", null, null);
        context.responseComplete();
        pryClienteSeleccionado = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (pryClienteSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(pryClienteSeleccionado.getSecuencia(), "PRY_CLIENTES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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

        } else if (administrarRastros.verificarHistoricosTabla("PRY_CLIENTES")) { // igual acá
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
            System.out.println("ERROR ControlPryClientes eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<PryClientes> getListPryClientes() {
        if (listPryClientes == null) {
            listPryClientes = administrarPryClientes.consultarPryClientes();
        }
        return listPryClientes;
    }

    public void setListPryClientes(List<PryClientes> listPryClientes) {
        this.listPryClientes = listPryClientes;
    }

    public List<PryClientes> getFiltrarPryClientes() {
        return filtrarPryClientes;
    }

    public void setFiltrarPryClientes(List<PryClientes> filtrarPryClientes) {
        this.filtrarPryClientes = filtrarPryClientes;
    }

    public PryClientes getNuevoPryCliente() {
        return nuevoPryCliente;
    }

    public void setNuevoPryCliente(PryClientes nuevoPryCliente) {
        this.nuevoPryCliente = nuevoPryCliente;
    }

    public PryClientes getDuplicarPryCliente() {
        return duplicarPryCliente;
    }

    public void setDuplicarPryCliente(PryClientes duplicarPryCliente) {
        this.duplicarPryCliente = duplicarPryCliente;
    }

    public PryClientes getEditarPryCliente() {
        return editarPryCliente;
    }

    public void setEditarPryCliente(PryClientes editarPryCliente) {
        this.editarPryCliente = editarPryCliente;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public PryClientes getPryClienteSeleccionado() {
        return pryClienteSeleccionado;
    }

    public void setPryClienteSeleccionado(PryClientes pryClienteSeleccionado) {
        this.pryClienteSeleccionado = pryClienteSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosPryCliente");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

}
