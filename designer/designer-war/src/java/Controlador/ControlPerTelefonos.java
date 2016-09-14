package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.Personas;
import Entidades.Telefonos;
import Entidades.TiposTelefonos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTelefonosInterface;
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

@ManagedBean
@SessionScoped
public class ControlPerTelefonos implements Serializable {

    @EJB
    AdministrarTelefonosInterface administrarTelefonos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //SECUENCIA DE LA PERSONA
    private BigInteger secuenciaPersona;
    private Personas persona;
    //LISTA TELEFONOS
    private List<Telefonos> listaTelefonos;
    private List<Telefonos> filtradosListaTelefonos;
    private Telefonos telefonoSeleccionado;
    //L.O.V TIPOS TELEFONOS
    private List<TiposTelefonos> listaTiposTelefonos;
    private List<TiposTelefonos> filtradoslistaTiposTelefonos;
    private TiposTelefonos seleccionTipoTelefono;
    //L.O.V CIUDADES
    private List<Ciudades> listaCiudades;
    private List<Ciudades> filtradoslistaCiudades;
    private Ciudades seleccionCiudades;
    //Columnas Tabla Telefonos
    private Column tFecha, tTipoTelefono, tNumero, tCiudad;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //AUTOCOMPLETAR
    private String TipoTelefono, Ciudad;
    //Modificar Telefono
    private List<Telefonos> listaTelefonosModificar;
    private boolean guardado, guardarOk;
    //Crear Telefono
    public Telefonos nuevoTelefono;
    private List<Telefonos> listaTelefonosCrear;
    private BigInteger l;
    private int k;
    private String mensajeValidacion;
    //Borrar Teléfono
    private List<Telefonos> listaTelefonosBorrar;
    //editar celda
    private Telefonos editarTelefono;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //Duplicar
    private Telefonos duplicarTelefono;
    //RASTRO
    private String altoTabla, infoRegistro, infoRegistroTT, infoRegistroCiudad;
    private DataTable tablaC;
    private Empleados empleado;
    private boolean activarLov;

    public ControlPerTelefonos() {
        permitirIndex = true;
        //secuenciaPersona = BigInteger.valueOf(10668967);
        aceptar = true;
        tipoLista = 0;
        listaTelefonosBorrar = new ArrayList<Telefonos>();
        listaTelefonosCrear = new ArrayList<Telefonos>();
        listaTelefonosModificar = new ArrayList<Telefonos>();
        //INICIALIZAR LOVS
        listaTiposTelefonos = new ArrayList<TiposTelefonos>();
        listaCiudades = new ArrayList<Ciudades>();
        telefonoSeleccionado = null;
        //editar
        editarTelefono = new Telefonos();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        //Crear VC
        nuevoTelefono = new Telefonos();
        nuevoTelefono.setTipotelefono(new TiposTelefonos());
        nuevoTelefono.setCiudad(new Ciudades());
        permitirIndex = true;
        k = 0;
        altoTabla = "270";
        guardado = true;
        empleado = new Empleados();
        activarLov = true;

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTelefonos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger secuencia) {
        listaTelefonos = null;
        listaTiposTelefonos = null;
        empleado = administrarTelefonos.empleadoActual(secuencia);
        getListaTelefonos();
        if (listaTelefonos == null || listaTelefonos.isEmpty()) {
            telefonoSeleccionado= null;
        } else{
            telefonoSeleccionado = listaTelefonos.get(0);
        }
        contarRegistros();
    }

    public void refrescar() {
        listaTelefonos = null;
        aceptar = true;
        getListaTelefonos();
//        RequestContext context = RequestContext.getCurrentInstance();
       RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            tFecha = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tFecha");
            tFecha.setFilterStyle("display: none; visibility: hidden;");
            tTipoTelefono = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tTipoTelefono");
            tTipoTelefono.setFilterStyle("display: none; visibility: hidden;");
            tNumero = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tNumero");
            tNumero.setFilterStyle("display: none; visibility: hidden;");
            tCiudad = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tCiudad");
            tCiudad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
            bandera = 0;
            filtradosListaTelefonos = null;
            tipoLista = 0;
            altoTabla = "270";
        }

        listaTelefonosBorrar.clear();
        listaTelefonosCrear.clear();
        listaTelefonosModificar.clear();
        k = 0;
        contarRegistros();
        telefonoSeleccionado = null;
        listaTelefonos = null;
        guardado = true;
        permitirIndex = true;
        getListaTelefonos();
        contarRegistros();
        deshabilitarBotonLov();
        RequestContext context = RequestContext.getCurrentInstance();
        //RequestContext.getCurrentInstance().update("form:infoRegistro");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
    }

    public void actualizarTiposTelefonos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                telefonoSeleccionado.setTipotelefono(seleccionTipoTelefono);
                if (!listaTelefonosCrear.contains(telefonoSeleccionado)) {
                    if (listaTelefonosModificar.isEmpty()) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listaTelefonosModificar.contains(telefonoSeleccionado)) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    }
                }
            } else {
                telefonoSeleccionado.setTipotelefono(seleccionTipoTelefono);
                if (!listaTelefonosCrear.contains(telefonoSeleccionado)) {
                    if (listaTelefonosModificar.isEmpty()) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listaTelefonosModificar.contains(telefonoSeleccionado)) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
        } else if (tipoActualizacion == 1) {
            nuevoTelefono.setTipotelefono(seleccionTipoTelefono);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTelefono");
        } else if (tipoActualizacion == 2) {
            duplicarTelefono.setTipotelefono(seleccionTipoTelefono);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTelefono");
        }
        filtradoslistaTiposTelefonos = null;
        seleccionTipoTelefono = null;
        aceptar = true;
        tipoActualizacion = -1;
        //cualCelda = -1;
        RequestContext.getCurrentInstance().update("form:tiposTelefonosDialogo");
        RequestContext.getCurrentInstance().update("form:LOVTiposTelefonos");
        RequestContext.getCurrentInstance().update("form:aceptarTT");

        context.reset("formularioDialogos:LOVTiposTelefonos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVTiposTelefonos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('tiposTelefonosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposTelefonos");
    }

    public void cancelarCambioTiposTelefonos() {
        filtradoslistaTiposTelefonos = null;
        seleccionTipoTelefono = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tiposTelefonosDialogo");
        RequestContext.getCurrentInstance().update("form:LOVTiposTelefonos");
        RequestContext.getCurrentInstance().update("form:aceptarTT");
        context.reset("formularioDialogos:LOVTiposTelefonos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVTiposTelefonos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('tiposTelefonosDialogo').hide()");
    }
//MOSTRAR DATOS CELDA

    public void editarCelda() {
        if (telefonoSeleccionado != null) {
            if (tipoLista == 0) {
                editarTelefono = telefonoSeleccionado;
            }
            if (tipoLista == 1) {
                editarTelefono = telefonoSeleccionado;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
                PrimefacesContextUI.ejecutar("PF('editarFecha').show()");
                cualCelda = -1;
                deshabilitarBotonLov();
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTT");
                PrimefacesContextUI.ejecutar("PF('editarTT').show()");
                habilitarBotonLov();
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNumeroTelefono");
                PrimefacesContextUI.ejecutar("PF('editarNumeroTelefono').show()");
                cualCelda = -1;
                deshabilitarBotonLov();
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudad");
                PrimefacesContextUI.ejecutar("PF('editarCiudad').show()");
                cualCelda = -1;
                habilitarBotonLov();
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (telefonoSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 1) {
                habilitarBotonLov();
                modificarInfoRegistroTT(listaTiposTelefonos.size());
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposTelefonosDialogo");
                PrimefacesContextUI.ejecutar("PF('tiposTelefonosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 3) {
                habilitarBotonLov();
                modificarInfoRegistroCiudad(listaCiudades.size());
                RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
                PrimefacesContextUI.ejecutar("PF('ciudadesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    //BORRAR CIUDADES
    public void borrarTelefonos() {

        if (telefonoSeleccionado != null) {
            if (!listaTelefonosModificar.isEmpty() && listaTelefonosModificar.contains(telefonoSeleccionado)) {
                listaTelefonosModificar.remove(listaTelefonosModificar.indexOf(telefonoSeleccionado));
                listaTelefonosBorrar.add(telefonoSeleccionado);
            } else if (!listaTelefonosCrear.isEmpty() && listaTelefonosCrear.contains(telefonoSeleccionado)) {
                listaTelefonosCrear.remove(listaTelefonosCrear.indexOf(telefonoSeleccionado));
            } else {
                listaTelefonosBorrar.add(telefonoSeleccionado);
            }
            listaTelefonos.remove(telefonoSeleccionado);
            if (tipoLista == 1) {
                filtradosListaTelefonos.remove(telefonoSeleccionado);
            }
            modificarInfoRegistro(listaTelefonos.size());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
            telefonoSeleccionado = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    //DUPLICAR TELEFONO
    public void duplicarT() {
        if (telefonoSeleccionado != null) {
            duplicarTelefono = new Telefonos();
            k++;
            l = BigInteger.valueOf(k);
            if (tipoLista == 0) {
                duplicarTelefono.setSecuencia(l);
                duplicarTelefono.setFechavigencia(telefonoSeleccionado.getFechavigencia());
                duplicarTelefono.setTipotelefono(telefonoSeleccionado.getTipotelefono());
                duplicarTelefono.setNumerotelefono(telefonoSeleccionado.getNumerotelefono());
                duplicarTelefono.setCiudad(telefonoSeleccionado.getCiudad());
                duplicarTelefono.setPersona(telefonoSeleccionado.getPersona());
            }
            if (tipoLista == 1) {
                duplicarTelefono.setSecuencia(l);
                duplicarTelefono.setFechavigencia(telefonoSeleccionado.getFechavigencia());
                duplicarTelefono.setTipotelefono(telefonoSeleccionado.getTipotelefono());
                duplicarTelefono.setNumerotelefono(telefonoSeleccionado.getNumerotelefono());
                duplicarTelefono.setCiudad(telefonoSeleccionado.getCiudad());
                duplicarTelefono.setPersona(telefonoSeleccionado.getPersona());
                altoTabla = "270";
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTelefono");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroTelefono').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        int pasaA = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarTelefono.getFechavigencia() == null) {
            System.out.println("Entro a Fecha");
            mensajeValidacion = " Campo fecha vacío \n";
            pasa++;

        }
        if (duplicarTelefono.getTipotelefono().getSecuencia() == null) {
            System.out.println("Entro a TipoTelefono");
            mensajeValidacion = mensajeValidacion + " Campo Tipo Teléfono vacío \n";
            pasa++;
        }
        if (duplicarTelefono.getNumerotelefono() == 0) {
            System.out.println("Entro a Numero");
            mensajeValidacion = mensajeValidacion + " * Campo Número Teléfono vacío \n";
            pasa++;
        }
        for (int i = 0; i < listaTelefonos.size(); i++) {
            if ((listaTelefonos.get(i).getTipotelefono().getNombre().equals(duplicarTelefono.getTipotelefono().getNombre())) && (!(listaTelefonos.get(i).getFechavigencia().before(duplicarTelefono.getFechavigencia())) && !(duplicarTelefono.getFechavigencia().before(listaTelefonos.get(i).getFechavigencia())))) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeTipoTelefono");
                PrimefacesContextUI.ejecutar("PF('existeTipoTelefono').show()");
                pasaA++;
            }
            if (pasa != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTelefono");
                PrimefacesContextUI.ejecutar("PF('validacionNuevoTelefono').show()");
            }
        }

        if (pasa == 0 && pasaA == 0) {
            listaTelefonos.add(duplicarTelefono);
            listaTelefonosCrear.add(duplicarTelefono);
            telefonoSeleccionado = duplicarTelefono;
            getListaTelefonos();
            modificarInfoRegistro(listaTelefonos.size());
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                tFecha = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tFecha");
                tFecha.setFilterStyle("display: none; visibility: hidden;");
                tTipoTelefono = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tTipoTelefono");
                tTipoTelefono.setFilterStyle("display: none; visibility: hidden;");
                tNumero = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tNumero");
                tNumero.setFilterStyle("display: none; visibility: hidden;");
                tCiudad = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tCiudad");
                tCiudad.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "270";
                RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
                bandera = 0;
                filtradosListaTelefonos = null;
                tipoLista = 0;
            }
            duplicarTelefono = new Telefonos();
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroTelefono').hide()");
        }

    }
    //LIMPIAR DUPLICAR

    public void limpiarduplicarTelefono() {
        duplicarTelefono = new Telefonos();
        duplicarTelefono.setTipotelefono(new TiposTelefonos());
        duplicarTelefono.setCiudad(new Ciudades());
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTelefonosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TelefonosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        telefonoSeleccionado = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTelefonosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TelefonosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        telefonoSeleccionado = null;
    }
    //LIMPIAR NUEVO REGISTRO

    public void limpiarNuevoTelefono() {
        nuevoTelefono = new Telefonos();
        nuevoTelefono.setTipotelefono(new TiposTelefonos());
        nuevoTelefono.setCiudad(new Ciudades());
    }
    //FILTRADO

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            tFecha = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tFecha");
            tFecha.setFilterStyle("width: 85% !important");
            tTipoTelefono = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tTipoTelefono");
            tTipoTelefono.setFilterStyle("width: 85% !important");
            tNumero = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tNumero");
            tNumero.setFilterStyle("width: 85% !important");
            tCiudad = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tCiudad");
            tCiudad.setFilterStyle("width: 85% !important");
            altoTabla = "250";
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
            bandera = 1;

        } else if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tFecha = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tFecha");
            tFecha.setFilterStyle("display: none; visibility: hidden;");
            tTipoTelefono = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tTipoTelefono");
            tTipoTelefono.setFilterStyle("display: none; visibility: hidden;");
            tNumero = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tNumero");
            tNumero.setFilterStyle("display: none; visibility: hidden;");
            tCiudad = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tCiudad");
            tCiudad.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
            bandera = 0;
            filtradosListaTelefonos = null;
            tipoLista = 0;
        }
    }
//CREAR TELEFONO

    public void agregarNuevoTelefono() {
        Long a = null;
        int pasa = 0;
        int pasaA = 0;
        mensajeValidacion = " ";
        FacesContext c = FacesContext.getCurrentInstance();
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoTelefono.getFechavigencia() == null && nuevoTelefono.getTipotelefono().getSecuencia() == null && nuevoTelefono.getNumerotelefono() == 0) {
            System.out.println("Entro a Fecha");
            mensajeValidacion = " Existen campos vacíos \n";
            pasa++;
        }

        if (nuevoTelefono.getFechavigencia() == null) {
            System.out.println("Entro a Fecha");
            mensajeValidacion = " Campo fecha vacío \n";
            pasa++;
        }
        if (nuevoTelefono.getTipotelefono().getSecuencia() == null) {
            System.out.println("Entro a TipoTelefono");
            mensajeValidacion = " Campo Tipo Teléfono vacío\n";
            pasa++;
        }
        if (nuevoTelefono.getNumerotelefono() == 0) {
            System.out.println("Entro a Numero");
            mensajeValidacion = " Campo Número de Teléfono vacío\n";
            pasa++;
        }
        for (int i = 0; i < listaTelefonos.size(); i++) {
            if ((listaTelefonos.get(i).getTipotelefono().getNombre().equals(nuevoTelefono.getTipotelefono().getNombre())) && (!(listaTelefonos.get(i).getFechavigencia().before(nuevoTelefono.getFechavigencia())) && !(nuevoTelefono.getFechavigencia().before(listaTelefonos.get(i).getFechavigencia())))) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeTipoTelefono");
                PrimefacesContextUI.ejecutar("PF('existeTipoTelefono').show()");
                pasaA++;
            }
//            if (pasa != 0) {
//                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTelefono");
//                PrimefacesContextUI.ejecutar("PF('validacionNuevoTelefono').show()");
//            }
        }

        if (pasa == 0 && pasaA == 0) {
            if (bandera == 1) {
                tFecha = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tFecha");
                tFecha.setFilterStyle("display: none; visibility: hidden;");
                tTipoTelefono = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tTipoTelefono");
                tTipoTelefono.setFilterStyle("display: none; visibility: hidden;");
                tNumero = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tNumero");
                tNumero.setFilterStyle("display: none; visibility: hidden;");
                tCiudad = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tCiudad");
                tCiudad.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
                bandera = 0;
                filtradosListaTelefonos = null;
                tipoLista = 0;
                altoTabla = "270";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTelefono.setSecuencia(l);
            nuevoTelefono.setPersona(empleado.getPersona());
            if (nuevoTelefono.getCiudad().getSecuencia() == null) {
                nuevoTelefono.setCiudad(null);
            }
            listaTelefonosCrear.add(nuevoTelefono);
            listaTelefonos.add(nuevoTelefono);
            telefonoSeleccionado = nuevoTelefono;
            nuevoTelefono = new Telefonos();
            nuevoTelefono.setTipotelefono(new TiposTelefonos());
            nuevoTelefono.setCiudad(new Ciudades());
            getListaTelefonos();
            modificarInfoRegistro(listaTelefonos.size());
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroTelefono').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTelefono");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoTelefono').show()");
        }
    }

    //METODOS L.O.V CIUDADES
    public void actualizarCiudad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                telefonoSeleccionado.setCiudad(seleccionCiudades);
                if (!listaTelefonosCrear.contains(telefonoSeleccionado)) {
                    if (listaTelefonosModificar.isEmpty()) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listaTelefonosModificar.contains(telefonoSeleccionado)) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    }
                }
            } else {
                telefonoSeleccionado.setCiudad(seleccionCiudades);
                if (!listaTelefonosCrear.contains(telefonoSeleccionado)) {
                    if (listaTelefonosModificar.isEmpty()) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listaTelefonosModificar.contains(telefonoSeleccionado)) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
        } else if (tipoActualizacion == 1) {
            nuevoTelefono.setCiudad(seleccionCiudades);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTelefono");
        } else if (tipoActualizacion == 2) {
            duplicarTelefono.setCiudad(seleccionCiudades);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTelefono");
        }
        filtradoslistaCiudades = null;
        seleccionCiudades = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVCiudades:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVCiudades').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ciudadesDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVCiudades");
    }

    public void cancelarCambioCiudad() {
        filtradoslistaCiudades = null;
        seleccionCiudades = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVCiudades:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVCiudades').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ciudadesDialogo').hide()");
    }

    //Ubicacion Celda.
    public void cambiarIndice(Telefonos telefono, int celda) {
        if (permitirIndex == true) {
            telefonoSeleccionado = telefono;
            cualCelda = celda;
            deshabilitarBotonLov();
            if (tipoLista == 0) {
                telefonoSeleccionado.getSecuencia();
                if (cualCelda == 1) {
                    modificarInfoRegistroTT(listaTiposTelefonos.size());
                    TipoTelefono = telefonoSeleccionado.getTipotelefono().getNombre();
                    habilitarBotonLov();
                } else if (cualCelda == 3) {
                    habilitarBotonLov();
                    modificarInfoRegistroCiudad(listaCiudades.size());
                    Ciudad = telefonoSeleccionado.getCiudad().getNombre();
                }
            } else {
                telefonoSeleccionado.getSecuencia();
                deshabilitarBotonLov();
                if (cualCelda == 1) {
                    habilitarBotonLov();
                    modificarInfoRegistroTT(listaTiposTelefonos.size());
                    TipoTelefono = telefonoSeleccionado.getTipotelefono().getNombre();
                } else if (cualCelda == 3) {
                    habilitarBotonLov();
                    modificarInfoRegistroCiudad(listaCiudades.size());
                    Ciudad = telefonoSeleccionado.getCiudad().getNombre();
                }
            }
        }
    }

    //AUTOCOMPLETAR
    public void modificarTelefonos(Telefonos telefono, String confirmarCambio, String valorConfirmar) {
        telefonoSeleccionado = telefono;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaTelefonosCrear.contains(telefonoSeleccionado)) {

                    if (listaTelefonosModificar.isEmpty()) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listaTelefonosModificar.contains(telefonoSeleccionado)) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                deshabilitarBotonLov();
                telefonoSeleccionado = null;

            } else {
                if (!listaTelefonosCrear.contains(telefonoSeleccionado)) {

                    if (listaTelefonosModificar.isEmpty()) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listaTelefonosModificar.contains(telefonoSeleccionado)) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                deshabilitarBotonLov();
                telefonoSeleccionado = null;
            }
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
        } else if (confirmarCambio.equalsIgnoreCase("TIPOTELEFONO")) {
            if (tipoLista == 0) {
                telefonoSeleccionado.getTipotelefono().setNombre(TipoTelefono);
            } else {
                telefonoSeleccionado.getTipotelefono().setNombre(TipoTelefono);
            }

            for (int i = 0; i < listaTiposTelefonos.size(); i++) {
                if (listaTiposTelefonos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    telefonoSeleccionado.setTipotelefono(listaTiposTelefonos.get(indiceUnicoElemento));
                } else {
                    telefonoSeleccionado.setTipotelefono(listaTiposTelefonos.get(indiceUnicoElemento));
                }
                deshabilitarBotonLov();
                listaTiposTelefonos.clear();
                getListaTiposTelefonos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposTelefonosDialogo");
                PrimefacesContextUI.ejecutar("PF('tiposTelefonosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            if (tipoLista == 0) {
                telefonoSeleccionado.getCiudad().setNombre(Ciudad);
            } else {
                telefonoSeleccionado.getCiudad().setNombre(Ciudad);
            }
            for (int i = 0; i < listaCiudades.size(); i++) {
                if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    telefonoSeleccionado.setCiudad(listaCiudades.get(indiceUnicoElemento));
                } else {
                    telefonoSeleccionado.setCiudad(listaCiudades.get(indiceUnicoElemento));
                }
                listaCiudades.clear();
                getListaCiudades();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
                PrimefacesContextUI.ejecutar("PF('ciudadesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listaTelefonosCrear.contains(telefonoSeleccionado)) {
                    if (listaTelefonosModificar.isEmpty()) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listaTelefonosModificar.contains(telefonoSeleccionado)) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                deshabilitarBotonLov();
                telefonoSeleccionado = null;
            } else {
                if (!listaTelefonosCrear.contains(telefonoSeleccionado)) {

                    if (listaTelefonosModificar.isEmpty()) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listaTelefonosModificar.contains(telefonoSeleccionado)) {
                        listaTelefonosModificar.add(telefonoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                telefonoSeleccionado = null;
            }
        }
        RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
    }

    //GUARDAR
    public void guardarCambiosTelefono() {
        if (guardado == false) {
            if (!listaTelefonosBorrar.isEmpty()) {
                for (int i = 0; i < listaTelefonosBorrar.size(); i++) {
                    if (listaTelefonosBorrar.get(i).getTipotelefono().getSecuencia() == null) {
                        listaTelefonosBorrar.get(i).setTipotelefono(null);
                        administrarTelefonos.borrarTelefono(listaTelefonosBorrar.get(i));
                    } else {
                        if (listaTelefonosBorrar.get(i).getCiudad().getSecuencia() == null) {
                            listaTelefonosBorrar.get(i).setCiudad(null);
                            administrarTelefonos.borrarTelefono(listaTelefonosBorrar.get(i));
                        } else {
                            administrarTelefonos.borrarTelefono(listaTelefonosBorrar.get(i));
                        }
                    }
                }
                listaTelefonosBorrar.clear();
            }

            if (!listaTelefonosCrear.isEmpty()) {
                for (int i = 0; i < listaTelefonosCrear.size(); i++) {
                    System.out.println("Creando...");
                    if (listaTelefonosCrear.get(i).getTipotelefono().getSecuencia() == null) {
                        listaTelefonosCrear.get(i).setTipotelefono(null);
                        administrarTelefonos.crearTelefono(listaTelefonosCrear.get(i));
                    } else {
                        if (listaTelefonosCrear.get(i).getCiudad().getSecuencia() == null) {
                            listaTelefonosCrear.get(i).setCiudad(null);
                            administrarTelefonos.crearTelefono(listaTelefonosCrear.get(i));
                        } else {
                            administrarTelefonos.crearTelefono(listaTelefonosCrear.get(i));
                        }
                    }
                }
                listaTelefonosCrear.clear();
            }
            if (!listaTelefonosModificar.isEmpty()) {
                administrarTelefonos.modificarTelefono(listaTelefonosModificar);
                listaTelefonosModificar.clear();
            }
            listaTelefonos = null;
            getListaTelefonos();
            contarRegistros();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        deshabilitarBotonLov();
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
        permitirIndex = true;
        telefonoSeleccionado = null;

    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 1) {

            tFecha = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tFecha");
            tFecha.setFilterStyle("display: none; visibility: hidden;");
            tTipoTelefono = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tTipoTelefono");
            tTipoTelefono.setFilterStyle("display: none; visibility: hidden;");
            tNumero = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tNumero");
            tNumero.setFilterStyle("display: none; visibility: hidden;");
            tCiudad = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tCiudad");
            tCiudad.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
            bandera = 0;
            filtradosListaTelefonos = null;
            tipoLista = 0;
        }

        listaTelefonosBorrar.clear();
        listaTelefonosCrear.clear();
        listaTelefonosModificar.clear();
        contarRegistros();
        telefonoSeleccionado = null;
        k = 0;
        listaTelefonos = null;
        guardado = true;
        permitirIndex = true;

    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPOT")) {
            if (tipoNuevo == 1) {
                TipoTelefono = nuevoTelefono.getTipotelefono().getNombre();
            } else if (tipoNuevo == 2) {
                TipoTelefono = duplicarTelefono.getTipotelefono().getNombre();
            }
        } else if (Campo.equals("CIUDAD")) {
            if (tipoNuevo == 1) {
                Ciudad = nuevoTelefono.getCiudad().getNombre();
            } else if (tipoNuevo == 2) {
                Ciudad = duplicarTelefono.getCiudad().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOT")) {
            if (tipoNuevo == 1) {
                nuevoTelefono.getTipotelefono().setNombre(TipoTelefono);
            } else if (tipoNuevo == 2) {
                duplicarTelefono.getTipotelefono().setNombre(TipoTelefono);
            }
            for (int i = 0; i < listaTiposTelefonos.size(); i++) {
                if (listaTiposTelefonos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoTelefono.setTipotelefono(listaTiposTelefonos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoTelefono");
                } else if (tipoNuevo == 2) {
                    duplicarTelefono.setTipotelefono(listaTiposTelefonos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoTelefono");
                }
                listaTiposTelefonos.clear();
                getListaTiposTelefonos();
            } else {
                RequestContext.getCurrentInstance().update("form:tiposTelefonosDialogo");
                PrimefacesContextUI.ejecutar("PF('tiposTelefonosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoTelefono");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoTelefono");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            if (tipoNuevo == 1) {
                nuevoTelefono.getCiudad().setNombre(Ciudad);
            } else if (tipoNuevo == 2) {
                duplicarTelefono.getCiudad().setNombre(Ciudad);
            }
            for (int i = 0; i < listaCiudades.size(); i++) {
                if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoTelefono.setCiudad(listaCiudades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudad");
                } else if (tipoNuevo == 2) {
                    duplicarTelefono.setCiudad(listaCiudades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudad");
                }
                listaCiudades.clear();
                getListaCiudades();
            } else {
                RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
                PrimefacesContextUI.ejecutar("PF('ciudadesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudad");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudad");
                }
            }
        }
    }

    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)
    public void asignarIndex(Telefonos telefono, int dlg, int LND) {
        telefonoSeleccionado = telefono;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            telefonoSeleccionado = null;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            telefonoSeleccionado = null;
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            getListaTiposTelefonos();
            modificarInfoRegistroTT(listaTiposTelefonos.size());
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposTelefonosDialogo");
            PrimefacesContextUI.ejecutar("PF('tiposTelefonosDialogo').show()");
        } else if (dlg == 1) {
            modificarInfoRegistroCiudad(listaCiudades.size());
            //contarRegistros();
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
            PrimefacesContextUI.ejecutar("PF('ciudadesDialogo').show()");
        }

    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (telefonoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(telefonoSeleccionado.getSecuencia(), "TELEFONOS");
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
            deshabilitarBotonLov();
        } else {
            if (administrarRastros.verificarHistoricosTabla("TELEFONOS")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }
        }
    }

    public void recordarSeleccion() {
        if (telefonoSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosTelefonosPersona");
            tablaC.setSelection(telefonoSeleccionado);
        }
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        modificarInfoRegistro(filtradosListaTelefonos.size());
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void eventoFiltrarTT() {
        modificarInfoRegistroTT(filtradoslistaTiposTelefonos.size());
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTT");
    }

    public void eventoFiltrarCiudad() {
        modificarInfoRegistroCiudad(filtradoslistaCiudades.size());
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCiudades");
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void modificarInfoRegistroTT(int valor) {
        infoRegistroTT = String.valueOf(valor);
    }

    public void modificarInfoRegistroCiudad(int valor) {
        infoRegistroCiudad = String.valueOf(valor);
    }

    public void contarRegistros() {
        if (listaTelefonos != null) {
            modificarInfoRegistro(listaTelefonos.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    //GETTER AND SETTER
    public Personas getPersona() {
        if (persona == null) {
            persona = administrarTelefonos.encontrarPersona(secuenciaPersona);
        }
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public List<Telefonos> getListaTelefonos() {
        if (listaTelefonos == null) {
            if (empleado.getPersona().getSecuencia() != null) {
                listaTelefonos = administrarTelefonos.telefonosPersona(empleado.getPersona().getSecuencia());
            }
        }
        return listaTelefonos;
    }

    public void setListaTelefonos(List<Telefonos> listaTelefonos) {
        this.listaTelefonos = listaTelefonos;
    }

    public List<Telefonos> getFiltradosListaTelefonos() {
        return filtradosListaTelefonos;
    }

    public void setFiltradosListaTelefonos(List<Telefonos> filtradosListaTelefonos) {
        this.filtradosListaTelefonos = filtradosListaTelefonos;
    }

    public List<TiposTelefonos> getListaTiposTelefonos() {
        if (listaTiposTelefonos == null) {
            listaTiposTelefonos = administrarTelefonos.lovTiposTelefonos();
        }
        return listaTiposTelefonos;
    }

    public void setListaTiposTelefonos(List<TiposTelefonos> listaTiposTelefonos) {
        this.listaTiposTelefonos = listaTiposTelefonos;
    }

    public List<TiposTelefonos> getFiltradoslistaTiposTelefonos() {
        return filtradoslistaTiposTelefonos;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setFiltradoslistaTiposTelefonos(List<TiposTelefonos> filtradoslistaTiposTelefonos) {
        this.filtradoslistaTiposTelefonos = filtradoslistaTiposTelefonos;
    }

    public TiposTelefonos getSeleccionTipoTelefono() {
        return seleccionTipoTelefono;
    }

    public void setSeleccionTipoTelefono(TiposTelefonos seleccionTipoTelefono) {
        this.seleccionTipoTelefono = seleccionTipoTelefono;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public List<Ciudades> getListaCiudades() {
        if (listaCiudades.isEmpty()) {
            listaCiudades = administrarTelefonos.lovCiudades();
        }

        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudades> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public List<Ciudades> getFiltradoslistaCiudades() {
        return filtradoslistaCiudades;
    }

    public void setFiltradoslistaCiudades(List<Ciudades> filtradoslistaCiudades) {
        this.filtradoslistaCiudades = filtradoslistaCiudades;
    }

    public Ciudades getSeleccionCiudades() {
        return seleccionCiudades;
    }

    public void setSeleccionCiudades(Ciudades seleccionCiudades) {
        this.seleccionCiudades = seleccionCiudades;
    }

    public Telefonos getEditarTelefono() {
        return editarTelefono;
    }

    public void setEditarTelefono(Telefonos editarTelefono) {
        this.editarTelefono = editarTelefono;
    }

    public Telefonos getNuevoTelefono() {
        return nuevoTelefono;
    }

    public void setNuevoTelefono(Telefonos nuevoTelefono) {
        this.nuevoTelefono = nuevoTelefono;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public Telefonos getDuplicarTelefono() {
        return duplicarTelefono;
    }

    public void setDuplicarTelefono(Telefonos duplicarTelefono) {
        this.duplicarTelefono = duplicarTelefono;
    }

    public Telefonos getTelefonoSeleccionado() {
        return telefonoSeleccionado;
    }

    public void setTelefonoSeleccionado(Telefonos telefonoSeleccionado) {
        this.telefonoSeleccionado = telefonoSeleccionado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public String getInfoRegistroTT() {
        return infoRegistroTT;
    }

    public String getInfoRegistroCiudad() {
        return infoRegistroCiudad;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
