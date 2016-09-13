/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
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

    public void recibirPagina(String pagina) {
        paginaAnterior = pagina;
        listPryClientes = null;
        getListPryClientes();
        contarRegistros();
        if(listPryClientes == null || listPryClientes.isEmpty()){
            pryClienteSeleccionado= null;
        } else {
        pryClienteSeleccionado = listPryClientes.get(0);
        }
        
    }

    public String redirigirPaginaAnterior() {
        return paginaAnterior;
    }

    public void cambiarIndice(PryClientes pryCliente, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            pryClienteSeleccionado = pryCliente;
            cualCelda = celda;
            pryClienteSeleccionado.getSecuencia();
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpDescripcion = pryClienteSeleccionado.getNombre();
                } else {
                    backUpDescripcion = pryClienteSeleccionado.getNombre();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDireccion = pryClienteSeleccionado.getDireccion();
                } else {
                    backUpDireccion = pryClienteSeleccionado.getDireccion();
                }
            }
            if (cualCelda == 2) {
                if (tipoLista == 0) {
                    backUpTelefono = pryClienteSeleccionado.getTelefono();
                } else {
                    backUpTelefono = pryClienteSeleccionado.getTelefono();
                }
            }
            if (cualCelda == 3) {
                if (tipoLista == 0) {
                    backUpNombreContacto = pryClienteSeleccionado.getContacto();
                } else {
                    backUpNombreContacto = pryClienteSeleccionado.getContacto();
                }
            }

        }
        System.out.println("Indice: " + pryClienteSeleccionado + " Celda: " + cualCelda);
    }

    public void asignarIndex(PryClientes pryCliente, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlPryClientes.asignarIndex \n");
            pryClienteSeleccionado = pryCliente;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlPryClientes.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
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
        RequestContext.getCurrentInstance().update("form:infoRegistro");
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
        RequestContext.getCurrentInstance().update("form:infoRegistro");
        RequestContext.getCurrentInstance().update("form:datosPryCliente");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            nombre = (Column) c.getViewRoot().findComponent("form:datosPryCliente:nombre");
            nombre.setFilterStyle("width: 85%");
            direccion = (Column) c.getViewRoot().findComponent("form:datosPryCliente:direccion");
            direccion.setFilterStyle("width: 85%");
            telefono = (Column) c.getViewRoot().findComponent("form:datosPryCliente:telefono");
            telefono.setFilterStyle("width: 85%");
            contacto = (Column) c.getViewRoot().findComponent("form:datosPryCliente:contacto");
            contacto.setFilterStyle("width: 85%");
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

    public void modificarPryCliente(PryClientes pryCliente, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR PRY CLIENTE");
        pryClienteSeleccionado = pryCliente;

        int contador = 0;
        boolean banderita = false;
        Short a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR PRY CLIENTE, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearPryClientes.contains(pryClienteSeleccionado)) {

                    if (pryClienteSeleccionado.getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        System.err.println("nombre");
                        pryClienteSeleccionado.setNombre(backUpDescripcion);
                    } else if (pryClienteSeleccionado.getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        System.err.println("nombre");
                        pryClienteSeleccionado.setNombre(backUpDescripcion);
                    } else {
                        banderita = true;
                    }

                    System.err.println("VALIDACION BANDERITA == " + banderita);
                    if (banderita == true) {
                        if (modificarPryClientes.isEmpty()) {
                            modificarPryClientes.add(pryClienteSeleccionado);
                        } else if (!modificarPryClientes.contains(pryClienteSeleccionado)) {
                            modificarPryClientes.add(pryClienteSeleccionado);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                } else {

                    if (pryClienteSeleccionado.getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        System.err.println("nombre");
                        pryClienteSeleccionado.setNombre(backUpDescripcion);
                    } else if (pryClienteSeleccionado.getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        System.err.println("nombre");
                        pryClienteSeleccionado.setNombre(backUpDescripcion);
                    } else {
                        banderita = true;
                    }

                    System.err.println("VALIDACION BANDERITA == " + banderita);
                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                }
            } else {

                if (!crearPryClientes.contains(pryClienteSeleccionado)) {

                    if (pryClienteSeleccionado.getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        pryClienteSeleccionado.setNombre(backUpDescripcion);
                        System.err.println("nombre");
                    } else if (pryClienteSeleccionado.getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        pryClienteSeleccionado.setNombre(backUpDescripcion);
                        banderita = false;
                        System.err.println("nombre");
                    } else {
                        banderita = true;
                    }

                    if (banderita == true) {
                        if (modificarPryClientes.isEmpty()) {
                            modificarPryClientes.add(pryClienteSeleccionado);
                        } else if (!modificarPryClientes.contains(pryClienteSeleccionado)) {
                            modificarPryClientes.add(pryClienteSeleccionado);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                } else {

                    if (pryClienteSeleccionado.getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        pryClienteSeleccionado.setNombre(backUpDescripcion);
                        System.err.println("nombre");
                    } else if (pryClienteSeleccionado.getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        pryClienteSeleccionado.setNombre(backUpDescripcion);
                        banderita = false;
                        System.err.println("nombre");
                    } else {
                        banderita = true;
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                }

            }
            RequestContext.getCurrentInstance().update("form:datosPryCliente");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

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
            modificarInfoRegistro(listPryClientes.size());
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosPryCliente");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");

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
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
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
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
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
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
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
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
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
                PrimefacesContextUI.ejecutar("PF('editNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDireccion");
                PrimefacesContextUI.ejecutar("PF('editDireccion').show()");
                cualCelda = -1;

            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTelefono");
                PrimefacesContextUI.ejecutar("PF('editTelefono').show()");
                cualCelda = -1;

            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editContacto");
                PrimefacesContextUI.ejecutar("PF('editContacto').show()");
                cualCelda = -1;
            }

        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
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
            modificarInfoRegistro(listPryClientes.size());
            RequestContext.getCurrentInstance().update("form:datosPryCliente");
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            nuevoPryCliente = new PryClientes();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroPryClientes').hide()");
            pryClienteSeleccionado = nuevoPryCliente;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoPryC");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoPryC').show()");
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
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroPryClientes').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
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
            modificarInfoRegistro(listPryClientes.size());
            RequestContext.getCurrentInstance().update("form:datosPryCliente");
            RequestContext.getCurrentInstance().update("form:infoRegistro");
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
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroPryClientes').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarPryC");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarPryC').show()");
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
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(pryClienteSeleccionado.getSecuencia(), "PRY_CLIENTES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            System.out.println("resultado: " + resultado);
            if (resultado == 1) {
                PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
            } else if (resultado == 3) {
                PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
            } else if (resultado == 4) {
                PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
            } else if (resultado == 5) {
                PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
            }

        } else {
            if (administrarRastros.verificarHistoricosTabla("PRY_CLIENTES")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            modificarInfoRegistro(filtrarPryClientes.size());
            RequestContext.getCurrentInstance().update("form:infoRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlPryClientes eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void contarRegistros() {
        if (listPryClientes != null) {
            modificarInfoRegistro(listPryClientes.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void recordarSeleccionMotivoCambioCargo() {
        if (pryClienteSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosPryCliente");
            tablaC.setSelection(pryClienteSeleccionado);
        }
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
