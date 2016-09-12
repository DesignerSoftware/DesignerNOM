package Controlador;

import Entidades.*;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasTiposContratosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
import utilidadesUI.PrimefacesContextUI;

@ManagedBean
@SessionScoped
public class ControlVigenciasTiposContratos implements Serializable {

    @EJB
    AdministrarVigenciasTiposContratosInterface administrarVigenciasTiposContratos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Vigencias Cargos
    private List<VigenciasTiposContratos> vigenciasTiposContratoEmpleado;
    private List<VigenciasTiposContratos> filtrarVTC;
    private VigenciasTiposContratos vigenciaSeleccionada;
    private List<Ciudades> listaCiudades;
    private Ciudades ciudadSelecionada;
    private List<Ciudades> filtradoCiudades;
    private List<MotivosContratos> listaMotivosContratos;
    private MotivosContratos MotivoContratoSelecionado;
    private List<MotivosContratos> filtradoMotivoContrato;
    private List<TiposContratos> listaTiposContratos;
    private List<TiposContratos> filtradoTiposContrato;
    private TiposContratos TipoContratoSelecionado;
    //private BigInteger secuenciaEmpleado;
    private Empleados empleado;
    private int tipoActualizacion;
    private boolean permitirIndex;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Columnas Tabla VC
    private Column vtcFecha, vtcContrato, vtcTipoContrato, vtcCiudad, vtcFechaSP, vtcInicioFlexibilizacion, vtcObservacion;
    //Otros
    private boolean aceptar;
    //modificar
    private List<VigenciasTiposContratos> listVTCModificar;
    private boolean guardado, guardarOk;
    //crear VC
    public VigenciasTiposContratos nuevaVigencia;
    private List<VigenciasTiposContratos> listVTCCrear;
    private BigInteger l;
    private int k;
    private String mensajeValidacion;
    //borrar VC
    private List<VigenciasTiposContratos> listVTCBorrar;
    //editar celda
    private VigenciasTiposContratos editarVTC;
    private int cualCelda, tipoLista;
    private boolean cambioEditor, aceptarEditar;
    //duplicar
    private VigenciasTiposContratos duplicarVTC;
    //AUTOCOMPLETAR
    private String Motivo, TipoContrato, Ciudad;
    //RASTRO
    private String altoTabla;
    public String infoRegistro;
    private Date fechaVigenciaBck;
    public String infoRegistroCiudades, infoRegistroTiposContratos, infoRegistroMotivos;
    //
    private DataTable tablaC;
    private boolean activarLOV;

    public ControlVigenciasTiposContratos() {
        permitirIndex = true;
        vigenciasTiposContratoEmpleado = null;
        listaCiudades = null;
        empleado = new Empleados();
        ciudadSelecionada = new Ciudades();
        //Otros
        aceptar = true;
        //borrar aficiones
        listVTCBorrar = new ArrayList<VigenciasTiposContratos>();
        listaTiposContratos = null;
        //crear aficiones
        listVTCCrear = new ArrayList<VigenciasTiposContratos>();
        k = 0;
        //modificar aficiones
        listVTCModificar = new ArrayList<VigenciasTiposContratos>();
        //editar
        editarVTC = new VigenciasTiposContratos();
        listaMotivosContratos = null;
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevaVigencia = new VigenciasTiposContratos();
        nuevaVigencia.setMotivocontrato(new MotivosContratos());
        nuevaVigencia.setTipocontrato(new TiposContratos());
        nuevaVigencia.setCiudad(new Ciudades());
        vigenciaSeleccionada = null;
        altoTabla = "280";
        activarLOV = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciasTiposContratos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlVigenciasCargos: " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //EMPLEADO DE LA VIGENCIA
    public void recibirEmpleado(Empleados emp) {
        empleado = emp;
        getVigenciasTiposContratoEmpleado();
        contarRegistrosVTC();
        if (vigenciasTiposContratoEmpleado != null) {
            if (!vigenciasTiposContratoEmpleado.isEmpty()) {
                vigenciaSeleccionada = vigenciasTiposContratoEmpleado.get(0);
            }
        }
    }

    public void modificarVTC(VigenciasTiposContratos vTiposC, String confirmarCambio, String valorConfirmar) {
        vigenciaSeleccionada = vTiposC;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            activarLOV = true;
            PrimefacesContextUI.actualizar("form:listaValores");
            int control = 0;
            for (int i = 0; i < vigenciasTiposContratoEmpleado.size(); i++) {
                if (vigenciasTiposContratoEmpleado.get(i) == vigenciaSeleccionada) {
                    i++;
                    if (i >= vigenciasTiposContratoEmpleado.size()) {
                        break;
                    }
                }
                if (vigenciaSeleccionada.getFechavigencia().compareTo(vigenciaSeleccionada.getFechavigencia()) == 0) {
                    control++;
                    vigenciaSeleccionada.setFechavigencia(fechaVigenciaBck);
                }
            }

            if (control == 0) {
                if (!listVTCCrear.contains(vigenciaSeleccionada)) {

                    if (listVTCModificar.isEmpty()) {
                        listVTCModificar.add(vigenciaSeleccionada);
                    } else if (!listVTCModificar.contains(vigenciaSeleccionada)) {
                        listVTCModificar.add(vigenciaSeleccionada);
                    }
                    if (guardado) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");
                    }
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('validacionFechaDuplicada.show();");
            }
        } else if (confirmarCambio.equalsIgnoreCase("MOTIVOC")) {
            vigenciaSeleccionada.getMotivocontrato().setNombre(Motivo);
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");

            for (int i = 0; i < listaMotivosContratos.size(); i++) {
                if (listaMotivosContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaSeleccionada.setMotivocontrato(listaMotivosContratos.get(indiceUnicoElemento));

                listaMotivosContratos.clear();
                getListaMotivosContratos();
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:MotivosContratoDialogo");
                PrimefacesContextUI.ejecutar("PF('MotivosContratoDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("TIPOC")) {
            vigenciaSeleccionada.getTipocontrato().setNombre(TipoContrato);
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");

            for (int i = 0; i < listaTiposContratos.size(); i++) {
                if (listaTiposContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaSeleccionada.setTipocontrato(listaTiposContratos.get(indiceUnicoElemento));

                listaTiposContratos.clear();
                getListaTiposContratos();
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:TiposContratoDialogo");
                PrimefacesContextUI.ejecutar("PF('TiposContratoDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            if (!valorConfirmar.isEmpty()) {
                vigenciaSeleccionada.getCiudad().setNombre(Ciudad);

                for (int i = 0; i < listaCiudades.size(); i++) {
                    if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaSeleccionada.setCiudad(listaCiudades.get(indiceUnicoElemento));

                    listaCiudades.clear();
                    getListaCiudades();
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("formularioDialogos:ciudadesDialogo");
                    PrimefacesContextUI.ejecutar("PF('ciudadesDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                vigenciaSeleccionada.getCiudad().setNombre(Ciudad);
                vigenciaSeleccionada.setCiudad(new Ciudades());

                coincidencias = 1;
            }
        }
        if (coincidencias == 1) {
            if (!listVTCCrear.contains(vigenciaSeleccionada)) {

                if (listVTCModificar.isEmpty()) {
                    listVTCModificar.add(vigenciaSeleccionada);
                } else if (!listVTCModificar.contains(vigenciaSeleccionada)) {
                    listVTCModificar.add(vigenciaSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
            }
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }
        PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
    }

    public void modificarFechasNoObligatorias(int indice) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listVTCCrear.contains(vigenciaSeleccionada)) {

            if (listVTCModificar.isEmpty()) {
                listVTCModificar.add(vigenciaSeleccionada);
            } else if (!listVTCModificar.contains(vigenciaSeleccionada)) {
                listVTCModificar.add(vigenciaSeleccionada);
            }
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("MOTIVOC")) {
            if (tipoNuevo == 1) {
                Motivo = nuevaVigencia.getMotivocontrato().getNombre();
            } else if (tipoNuevo == 2) {
                Motivo = duplicarVTC.getMotivocontrato().getNombre();
            }
        } else if (Campo.equals("TIPOC")) {
            if (tipoNuevo == 1) {
                TipoContrato = nuevaVigencia.getTipocontrato().getNombre();
            } else if (tipoNuevo == 2) {
                TipoContrato = duplicarVTC.getTipocontrato().getNombre();
            }
        } else if (Campo.equals("CIUDAD")) {
            if (tipoNuevo == 1) {
                Ciudad = nuevaVigencia.getCiudad().getNombre();
            } else if (tipoNuevo == 2) {
                Ciudad = duplicarVTC.getCiudad().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("MOTIVOC")) {
            if (tipoNuevo == 1) {
                nuevaVigencia.getMotivocontrato().setNombre(Motivo);
            } else if (tipoNuevo == 2) {
                duplicarVTC.getMotivocontrato().setNombre(Motivo);
            }
            for (int i = 0; i < listaMotivosContratos.size(); i++) {
                if (listaMotivosContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigencia.setMotivocontrato(listaMotivosContratos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoMotivoContrato");
                } else if (tipoNuevo == 2) {
                    duplicarVTC.setMotivocontrato(listaMotivosContratos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarMotivoContrato");
                }
                listaMotivosContratos.clear();
                getListaMotivosContratos();
            } else {
                PrimefacesContextUI.actualizar("formularioDialogos:MotivosContratoDialogo");
                PrimefacesContextUI.ejecutar("PF('MotivosContratoDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoMotivoContrato");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarMotivoContrato");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TIPOC")) {
            if (tipoNuevo == 1) {
                nuevaVigencia.getTipocontrato().setNombre(TipoContrato);
            } else if (tipoNuevo == 2) {
                duplicarVTC.getTipocontrato().setNombre(TipoContrato);
            }
            for (int i = 0; i < listaTiposContratos.size(); i++) {
                if (listaTiposContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigencia.setTipocontrato(listaTiposContratos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoTipoContrato");
                } else if (tipoNuevo == 2) {
                    duplicarVTC.setTipocontrato(listaTiposContratos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTipoContrato");
                }
                listaTiposContratos.clear();
                getListaTiposContratos();
            } else {
                PrimefacesContextUI.actualizar("formularioDialogos:TiposContratoDialogo");
                PrimefacesContextUI.ejecutar("PF('TiposContratoDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoTipoContrato");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTipoContrato");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            if (tipoNuevo == 1) {
                nuevaVigencia.getCiudad().setNombre(Ciudad);
            } else if (tipoNuevo == 2) {
                duplicarVTC.getCiudad().setNombre(Ciudad);
            }
            for (int i = 0; i < listaCiudades.size(); i++) {
                if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigencia.setCiudad(listaCiudades.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaCiudad");
                } else if (tipoNuevo == 2) {
                    duplicarVTC.setCiudad(listaCiudades.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarCiudad");
                }
                listaCiudades.clear();
                getListaCiudades();
            } else {
                PrimefacesContextUI.actualizar("formularioDialogos:ciudadesDialogo");
                PrimefacesContextUI.ejecutar("PF('ciudadesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaCiudad");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarCiudad");
                }
            }
        }
    }

//Ubicacion Celda.
    public void cambiarIndice(VigenciasTiposContratos vTiposC, int celda) {
        vigenciaSeleccionada = vTiposC;
        if (permitirIndex) {
            cualCelda = celda;
            if (cualCelda == 0) {
                activarLOV = true;
                PrimefacesContextUI.actualizar("form:listaValores");
                fechaVigenciaBck = vigenciaSeleccionada.getFechavigencia();

            } else if (cualCelda == 1) {
                activarLOV = false;
                PrimefacesContextUI.actualizar("form:listaValores");
                Motivo = vigenciaSeleccionada.getMotivocontrato().getNombre();

            } else if (cualCelda == 2) {
                activarLOV = false;
                PrimefacesContextUI.actualizar("form:listaValores");
                TipoContrato = vigenciaSeleccionada.getTipocontrato().getNombre();

            } else if (cualCelda == 3) {
                activarLOV = false;
                PrimefacesContextUI.actualizar("form:listaValores");
                Ciudad = null;
                if (vigenciaSeleccionada.getCiudad() != null) {
                    Ciudad = vigenciaSeleccionada.getCiudad().getNombre();
                }
            } else if (cualCelda == 4) {
                activarLOV = true;
                PrimefacesContextUI.actualizar("form:listaValores");
            } else if (cualCelda == 5) {
                activarLOV = true;
                PrimefacesContextUI.actualizar("form:listaValores");
            } else if (cualCelda == 6) {
                activarLOV = true;
                PrimefacesContextUI.actualizar("form:listaValores");
            }
        }
    }

    //GUARDAR
    public void guardarCambiosVTC() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones Vigencias Tipos Contratos");
            if (!listVTCBorrar.isEmpty()) {
                for (int i = 0; i < listVTCBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    if (listVTCBorrar.get(i).getCiudad().getSecuencia() == null) {
                        listVTCBorrar.get(i).setCiudad(null);
                        administrarVigenciasTiposContratos.borrarVTC(listVTCBorrar.get(i));
                    } else {
                        administrarVigenciasTiposContratos.borrarVTC(listVTCBorrar.get(i));
                    }

                }
                listVTCBorrar.clear();
            }
            if (!listVTCCrear.isEmpty()) {
                for (int i = 0; i < listVTCCrear.size(); i++) {
                    System.out.println("Creando...");
                    if (listVTCCrear.get(i).getCiudad().getSecuencia() == null) {
                        listVTCCrear.get(i).setCiudad(null);
                        administrarVigenciasTiposContratos.crearVTC(listVTCCrear.get(i));
                    } else {
                        administrarVigenciasTiposContratos.crearVTC(listVTCCrear.get(i));
                    }
                }
                listVTCCrear.clear();
            }
            if (!listVTCModificar.isEmpty()) {
                administrarVigenciasTiposContratos.modificarVTC(listVTCModificar);
                listVTCModificar.clear();
            }
            if (vigenciasTiposContratoEmpleado != null && !vigenciasTiposContratoEmpleado.isEmpty()) {
                vigenciaSeleccionada = vigenciasTiposContratoEmpleado.get(0);
                //infoRegistro = "Cantidad de registros: " + vigenciasTiposContratoEmpleado.size();
                modificarInfoRegistro(vigenciasTiposContratoEmpleado.size());
            } else {
                //infoRegistro = "Cantidad de registros: 0";
                modificarInfoRegistro(0);
            }
            activarLOV = true;
            PrimefacesContextUI.actualizar("form:listaValores");
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
            PrimefacesContextUI.actualizar("form:informacionRegistro");

            guardado = true;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            k = 0;
            permitirIndex = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
            vigenciasTiposContratoEmpleado = null;
            getVigenciasTiposContratoEmpleado();
            contarRegistrosVTC();
        }
        vigenciaSeleccionada = null;
    }
    //CANCELAR MODIFICACIONES

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            recargarTablaDefault();
        }

        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        listVTCBorrar.clear();
        listVTCCrear.clear();
        listVTCModificar.clear();
        vigenciaSeleccionada = null;
        k = 0;
        vigenciasTiposContratoEmpleado = null;
        getVigenciasTiposContratoEmpleado();
        contarRegistrosVTC();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        } else {
            if (vigenciaSeleccionada != null) {
                editarVTC = vigenciaSeleccionada;

                System.out.println("Entro a editar... valor celda: " + cualCelda);
                if (cualCelda == 0) {
                    PrimefacesContextUI.actualizar("formularioDialogos:editarFecha");
                    PrimefacesContextUI.ejecutar("PF('editarFecha').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:editarMotivoContrato");
                    PrimefacesContextUI.ejecutar("PF('editarMotivoContrato').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:editarTipoContrato");
                    PrimefacesContextUI.ejecutar("PF('editarTipoContrato').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    PrimefacesContextUI.actualizar("formularioDialogos:editarCiudad");
                    PrimefacesContextUI.ejecutar("PF('editarCiudad').show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    PrimefacesContextUI.actualizar("formularioDialogos:editarFechaSP");
                    PrimefacesContextUI.ejecutar("PF('editarFechaSP').show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    PrimefacesContextUI.actualizar("formularioDialogos:editarFechaF");
                    PrimefacesContextUI.ejecutar("PF('editarFechaF').show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    PrimefacesContextUI.actualizar("formularioDialogos:editarObservacion");
                    PrimefacesContextUI.ejecutar("PF('editarObservacion').show()");
                    cualCelda = -1;
                }
            }
        }
    }

    //CREAR VTC
    public void agregarNuevaVTC() {
        int contador = 0;
        int fechas = 0;
        int pasa = 0;

        mensajeValidacion = " ";
        nuevaVigencia.setEmpleado(empleado);
        RequestContext context = RequestContext.getCurrentInstance();
        //boolean banderaConfirmar = false;

        if (nuevaVigencia.getFechavigencia() == null || nuevaVigencia.getFechavigencia().equals("")) {
            mensajeValidacion = " *Fecha\n";
        } else {
            if (vigenciasTiposContratoEmpleado != null) {
                for (int j = 0; j < vigenciasTiposContratoEmpleado.size(); j++) {
                    if (nuevaVigencia.getFechavigencia().equals(vigenciasTiposContratoEmpleado.get(j).getFechavigencia())) {
                        fechas++;
                    }
                }
            }
            if (fechas > 0) {
                PrimefacesContextUI.actualizar("form:fechas");
                PrimefacesContextUI.ejecutar("PF('fechas').show()");
                pasa++;

            } else {
                contador++;
            }
        }
        if (nuevaVigencia.getMotivocontrato().getSecuencia() == null || nuevaVigencia.getMotivocontrato().getSecuencia().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Motivo del Contrato \n";
            pasa++;
        } else {
            contador++;
        }
        if (nuevaVigencia.getTipocontrato().getSecuencia() == null || nuevaVigencia.getTipocontrato().getSecuencia().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Tipo del Contrato \n";

        } else {
            contador++;
        }

        if (contador == 3 && pasa == 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                recargarTablaDefault();
            }
            //AGREGAR REGISTRO A LA LISTA 
            k++;
            l = BigInteger.valueOf(k);
            nuevaVigencia.setSecuencia(l);
            nuevaVigencia.setEmpleado(empleado);
            if (nuevaVigencia.getCiudad().getSecuencia() == null) {
                nuevaVigencia.setCiudad(null);
            }
            listVTCCrear.add(nuevaVigencia);
            vigenciasTiposContratoEmpleado.add(nuevaVigencia);
            modificarInfoRegistro(vigenciasTiposContratoEmpleado.size());
            vigenciaSeleccionada = vigenciasTiposContratoEmpleado.get(vigenciasTiposContratoEmpleado.indexOf(nuevaVigencia));
            activarLOV = true;
            PrimefacesContextUI.actualizar("form:listaValores");
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            nuevaVigencia = new VigenciasTiposContratos();
            nuevaVigencia.setMotivocontrato(new MotivosContratos());
            nuevaVigencia.setTipocontrato(new TiposContratos());
            nuevaVigencia.setCiudad(new Ciudades());
            PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroVTC').hide()");
        } else if (contador != 3 && pasa == 0) {
            PrimefacesContextUI.actualizar("form:validacionNuevo");
            PrimefacesContextUI.ejecutar("PF('validacionNuevo').show()");
            contador = 0;
            pasa = 0;
        }
        PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevaVC() {
        nuevaVigencia = new VigenciasTiposContratos();
        nuevaVigencia.setMotivocontrato(new MotivosContratos());
        nuevaVigencia.setTipocontrato(new TiposContratos());
        nuevaVigencia.setCiudad(new Ciudades());
    }
    //DUPLICAR VC

    public void duplicarVigenciaC() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        } else {
            if (vigenciaSeleccionada != null) {
                duplicarVTC = new VigenciasTiposContratos();
                k++;
                l = BigInteger.valueOf(k);

                duplicarVTC.setSecuencia(l);
                duplicarVTC.setFechavigencia(vigenciaSeleccionada.getFechavigencia());
                duplicarVTC.setMotivocontrato(vigenciaSeleccionada.getMotivocontrato());
                duplicarVTC.setTipocontrato(vigenciaSeleccionada.getTipocontrato());
                duplicarVTC.setCiudad(vigenciaSeleccionada.getCiudad());
                duplicarVTC.setIniciosustitucion(vigenciaSeleccionada.getIniciosustitucion());
                duplicarVTC.setInicioflexibiliza(vigenciaSeleccionada.getInicioflexibiliza());
                duplicarVTC.setEmpleado(vigenciaSeleccionada.getEmpleado());
                duplicarVTC.setObservaciones(vigenciaSeleccionada.getObservaciones());

                PrimefacesContextUI.actualizar("formularioDialogos:duplicarVTC");
                PrimefacesContextUI.ejecutar("PF('DuplicarRegistroVTC').show()");
            }
        }
    }

    public void confirmarDuplicar() {
        RequestContext context = RequestContext.getCurrentInstance();
        int contador = 0;
        mensajeValidacion = " ";

        for (int j = 0; j < vigenciasTiposContratoEmpleado.size(); j++) {
            if (duplicarVTC.getFechavigencia().equals(vigenciasTiposContratoEmpleado.get(j).getFechavigencia())) {
                contador++;
            }
        }
        if (contador > 0) {
            mensajeValidacion = "Fechas NO Repetidas";
            PrimefacesContextUI.actualizar("form:validacionFechaDuplicada");
            PrimefacesContextUI.ejecutar("PF('validacionFechaDuplicada').show()");

        } else {
            vigenciasTiposContratoEmpleado.add(duplicarVTC);
            listVTCCrear.add(duplicarVTC);
            modificarInfoRegistro(vigenciasTiposContratoEmpleado.size());
            vigenciaSeleccionada = vigenciasTiposContratoEmpleado.get(vigenciasTiposContratoEmpleado.indexOf(duplicarVTC));
            activarLOV = true;
            PrimefacesContextUI.actualizar("form:listaValores");
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            if (bandera == 1) {
                recargarTablaDefault();
            }
            duplicarVTC = new VigenciasTiposContratos();
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroVTC').hide()");
            PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
        }
    }

    //LIMPIAR DUPLICAR
    public void limpiarduplicarVTC() {
        duplicarVTC = new VigenciasTiposContratos();
    }

    //BORRAR VC
    public void borrarVTC() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        } else {
            if (vigenciaSeleccionada != null) {
                if (!listVTCModificar.isEmpty() && listVTCModificar.contains(vigenciaSeleccionada)) {
                    int modIndex = listVTCModificar.indexOf(vigenciaSeleccionada);
                    listVTCModificar.remove(modIndex);
                    listVTCBorrar.add(vigenciaSeleccionada);
                } else if (!listVTCCrear.isEmpty() && listVTCCrear.contains(vigenciaSeleccionada)) {
                    int crearIndex = listVTCCrear.indexOf(vigenciaSeleccionada);
                    listVTCCrear.remove(crearIndex);
                } else {
                    listVTCBorrar.add(vigenciaSeleccionada);
                }
                vigenciasTiposContratoEmpleado.remove(vigenciaSeleccionada);
                if (tipoLista == 1) {
                    filtrarVTC.remove(vigenciaSeleccionada);
                }
                modificarInfoRegistro(vigenciasTiposContratoEmpleado.size());
                activarLOV = true;
                PrimefacesContextUI.actualizar("form:listaValores");

                PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
                PrimefacesContextUI.actualizar("form:informacionRegistro");

                vigenciaSeleccionada = null;

                if (guardado) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            }
        }
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            System.out.println("Activar");
            vtcFecha = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcFecha");
            vtcFecha.setFilterStyle("width: 85%");
            vtcContrato = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcContrato");
            vtcContrato.setFilterStyle("width: 85%");
            vtcTipoContrato = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcTipoContrato");
            vtcTipoContrato.setFilterStyle("width: 85%");
            vtcCiudad = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcCiudad");
            vtcCiudad.setFilterStyle("width: 85%");
            vtcFechaSP = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcFechaSP");
            vtcFechaSP.setFilterStyle("width: 85%");
            vtcInicioFlexibilizacion = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcInicioFlexibilizacion");
            vtcInicioFlexibilizacion.setFilterStyle("width: 85%");
            vtcObservacion = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcObservacion");
            vtcObservacion.setFilterStyle("width: 85%");
            altoTabla = "260";
            PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
            bandera = 1;

        } else if (bandera == 1) {
            recargarTablaDefault();
        }
        PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
    }

    //SALIR
    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            recargarTablaDefault();
        }
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        listVTCBorrar.clear();
        listVTCCrear.clear();
        listVTCModificar.clear();
        vigenciaSeleccionada = null;
        k = 0;
        vigenciasTiposContratoEmpleado = null;
        guardado = true;
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        permitirIndex = true;

        PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
    }
    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)

    public void asignarIndex(VigenciasTiposContratos vTiposC, int dlg) {
        RequestContext context = RequestContext.getCurrentInstance();
        vigenciaSeleccionada = vTiposC;
        tipoActualizacion = 0;
        activarLOV = false;
        PrimefacesContextUI.actualizar("form:listaValores");
        if (dlg == 0) {
            modificarInfoRegistroMotivos(listaMotivosContratos.size());
            PrimefacesContextUI.actualizar("formularioDialogos:MotivosContratoDialogo");
            PrimefacesContextUI.ejecutar("PF('MotivosContratoDialogo').show()");
        } else if (dlg == 1) {
            modificarInfoRegistroTiposC(listaTiposContratos.size());
            PrimefacesContextUI.actualizar("formularioDialogos:TiposContratoDialogo");
            PrimefacesContextUI.ejecutar("PF('TiposContratoDialogo').show()");
        } else if (dlg == 2) {
            modificarInfoRegistroCiudades(listaCiudades.size());
            PrimefacesContextUI.actualizar("formularioDialogos:ciudadesDialogo");
            PrimefacesContextUI.ejecutar("PF('ciudadesDialogo').show()");
        }
    }

    public void asignarIndexNueYDup(int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;

        if (dlg == 0) {
            modificarInfoRegistroMotivos(listaMotivosContratos.size());
            PrimefacesContextUI.actualizar("formularioDialogos:MotivosContratoDialogo");
            PrimefacesContextUI.ejecutar("PF('MotivosContratoDialogo').show()");
        } else if (dlg == 1) {
            modificarInfoRegistroTiposC(listaTiposContratos.size());
            PrimefacesContextUI.actualizar("formularioDialogos:TiposContratoDialogo");
            PrimefacesContextUI.ejecutar("PF('TiposContratoDialogo').show()");
        } else if (dlg == 2) {
            modificarInfoRegistroCiudades(listaCiudades.size());
            PrimefacesContextUI.actualizar("formularioDialogos:ciudadesDialogo");
            PrimefacesContextUI.ejecutar("PF('ciudadesDialogo').show()");
        }
    }

    //LOVS
    //CIUDAD
    public void actualizarCiudad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            vigenciaSeleccionada.setCiudad(ciudadSelecionada);

            if (!listVTCCrear.contains(vigenciaSeleccionada)) {
                if (listVTCModificar.isEmpty()) {
                    listVTCModificar.add(vigenciaSeleccionada);
                } else if (!listVTCModificar.contains(vigenciaSeleccionada)) {
                    listVTCModificar.add(vigenciaSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {
            nuevaVigencia.setCiudad(ciudadSelecionada);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaCiudad");
        } else if (tipoActualizacion == 2) {
            duplicarVTC.setCiudad(ciudadSelecionada);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarCiudad");
        }
        filtradoCiudades = null;
        aceptar = true;
        tipoActualizacion = -1;
        ciudadSelecionada = null;
        cualCelda = -1;
    }

    public void cancelarCambioCiudad() {
        filtradoCiudades = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        ciudadSelecionada = null;

    }
    //MOTIVO CONTRATO

    public void actualizarMotivoContrato() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            vigenciaSeleccionada.setMotivocontrato(MotivoContratoSelecionado);

            if (!listVTCCrear.contains(vigenciaSeleccionada)) {
                if (listVTCModificar.isEmpty()) {
                    listVTCModificar.add(vigenciaSeleccionada);
                } else if (!listVTCModificar.contains(vigenciaSeleccionada)) {
                    listVTCModificar.add(vigenciaSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {
            nuevaVigencia.setMotivocontrato(MotivoContratoSelecionado);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoMotivoContrato");
        } else if (tipoActualizacion == 2) {
            duplicarVTC.setMotivocontrato(MotivoContratoSelecionado);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarMotivoContrato");
        }
        filtradoMotivoContrato = null;
        MotivoContratoSelecionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void cancelarCambioMotivoContrato() {
        filtradoMotivoContrato = null;
        MotivoContratoSelecionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
    }

    //TIPO CONTRATO
    public void actualizarTipoContrato() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            vigenciaSeleccionada.setTipocontrato(TipoContratoSelecionado);

            if (!listVTCCrear.contains(vigenciaSeleccionada)) {
                if (listVTCModificar.isEmpty()) {
                    listVTCModificar.add(vigenciaSeleccionada);
                } else if (!listVTCModificar.contains(vigenciaSeleccionada)) {
                    listVTCModificar.add(vigenciaSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.actualizar("form:datosVTCEmpleado");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {
            nuevaVigencia.setTipocontrato(TipoContratoSelecionado);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoTipoContrato");
        } else if (tipoActualizacion == 2) {
            duplicarVTC.setTipocontrato(TipoContratoSelecionado);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarTipoContrato");
        }
        filtradoTiposContrato = null;
        TipoContratoSelecionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void cancelarTipoContrato() {
        //RequestContext context = RequestContext.getCurrentInstance();
        filtradoTiposContrato = null;
        TipoContratoSelecionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
    }
    //LISTA DE VALORES DINAMICA

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        } else {
            if (!vigenciasTiposContratoEmpleado.isEmpty()) {
                if (vigenciaSeleccionada.getSecuencia() != null) {
                    if (vigenciaSeleccionada != null) {
                        if (cualCelda == 1) {
                            modificarInfoRegistroMotivos(listaMotivosContratos.size());
                            PrimefacesContextUI.actualizar("formularioDialogos:MotivosContratoDialogo");
                            PrimefacesContextUI.ejecutar("PF('MotivosContratoDialogo').show()");
                            tipoActualizacion = 0;
                        } else if (cualCelda == 2) {
                            tipoActualizacion = 0;
                            modificarInfoRegistroTiposC(listaTiposContratos.size());
                            PrimefacesContextUI.actualizar("formularioDialogos:TiposContratoDialogo");
                            PrimefacesContextUI.ejecutar("PF('TiposContratoDialogo').show()");
                        } else if (cualCelda == 3) {
                            tipoActualizacion = 0;
                            modificarInfoRegistroCiudades(listaCiudades.size());
                            PrimefacesContextUI.actualizar("formularioDialogos:ciudadesDialogo");
                            PrimefacesContextUI.ejecutar("PF('ciudadesDialogo').show()");
                        }
                    }
                }
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void limpiarNuevaVTC() {
        nuevaVigencia = new VigenciasTiposContratos();
    }
    //EXPORTAR

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVTCEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciaTipoContratoPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVTCEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciaTipoContratoXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void recargarTablaDefault() {
        FacesContext c = FacesContext.getCurrentInstance();
        vtcFecha = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcFecha");
        vtcFecha.setFilterStyle("display: none; visibility: hidden;");
        vtcContrato = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcContrato");
        vtcContrato.setFilterStyle("display: none; visibility: hidden;");
        vtcTipoContrato = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcTipoContrato");
        vtcTipoContrato.setFilterStyle("display: none; visibility: hidden;");
        vtcCiudad = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcCiudad");
        vtcCiudad.setFilterStyle("display: none; visibility: hidden;");
        vtcFechaSP = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcFechaSP");
        vtcFechaSP.setFilterStyle("display: none; visibility: hidden;");
        vtcInicioFlexibilizacion = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcInicioFlexibilizacion");
        vtcInicioFlexibilizacion.setFilterStyle("display: none; visibility: hidden;");
        vtcObservacion = (Column) c.getViewRoot().findComponent("form:datosVTCEmpleado:vtcObservacion");
        vtcObservacion.setFilterStyle("display: none; visibility: hidden;");
        altoTabla = "280";
        bandera = 0;
        filtrarVTC = null;
        tipoLista = 0;
    }

    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (vigenciaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASTIPOSCONTRATOS");
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
            if (administrarRastros.verificarHistoricosTabla("VIGENCIASTIPOSCONTRATOS")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }
        }
    }

    public void anularLOV() {
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        vigenciaSeleccionada = null;
        modificarInfoRegistro(filtrarVTC.size());
        PrimefacesContextUI.actualizar("form:informacionRegistro");
    }

    public void eventoFiltrarCiudades() {
        modificarInfoRegistroCiudades(filtradoCiudades.size());
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroCiudades");
    }

    public void eventoFiltrarMotivos() {
        modificarInfoRegistroMotivos(filtradoMotivoContrato.size());
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroMotivos");
    }

    public void eventoFiltrarTiposC() {
        modificarInfoRegistroTiposC(filtradoTiposContrato.size());
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroTiposContratos");
    }

    private void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    private void modificarInfoRegistroCiudades(int valor) {
        infoRegistroCiudades = String.valueOf(valor);
    }

    private void modificarInfoRegistroTiposC(int valor) {
        infoRegistroTiposContratos = String.valueOf(valor);
    }

    private void modificarInfoRegistroMotivos(int valor) {
        infoRegistroMotivos = String.valueOf(valor);
    }

    public void contarRegistrosVTC() {
        if (vigenciasTiposContratoEmpleado != null) {
            modificarInfoRegistro(vigenciasTiposContratoEmpleado.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void recordarSeleccion() {
        if (vigenciaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVTCEmpleado");
            tablaC.setSelection(vigenciaSeleccionada);
        }
    }

    //GETTERS AND SETTERS
    public List<VigenciasTiposContratos> getVigenciasTiposContratoEmpleado() {
        try {
            if (vigenciasTiposContratoEmpleado == null) {
                vigenciasTiposContratoEmpleado = administrarVigenciasTiposContratos.vigenciasTiposContratosEmpleado(empleado.getSecuencia());
            }
            return vigenciasTiposContratoEmpleado;

        } catch (Exception e) {
            System.out.println("Error...!! getVigenciasTiposContratosEmpleado " + e);
            return null;
        }
    }

    public void setVigenciasTiposContratoEmpleado(List<VigenciasTiposContratos> vigenciasTiposContratoEmpleado) {
        this.vigenciasTiposContratoEmpleado = vigenciasTiposContratoEmpleado;
    }

    public Empleados getEmpleado() {
        //empleado = administrarVigenciasTiposContratos.buscarEmpleado(BigInteger.valueOf(10661039));
        return empleado;
    }

    public List<VigenciasTiposContratos> getFiltrarVTC() {
        return filtrarVTC;
    }

    public void setFiltrarVTC(List<VigenciasTiposContratos> filtrarVTC) {
        this.filtrarVTC = filtrarVTC;
    }

    public VigenciasTiposContratos getEditarVTC() {
        return editarVTC;
    }

    public void setEditarVTC(VigenciasTiposContratos editarVTC) {
        this.editarVTC = editarVTC;
    }

    public VigenciasTiposContratos getNuevaVigencia() {
        return nuevaVigencia;
    }

    public void setNuevaVigencia(VigenciasTiposContratos nuevaVigencia) {
        this.nuevaVigencia = nuevaVigencia;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void verProceso() {
        System.out.println(".................................................");
        for (int i = 0; i < listVTCCrear.size(); i++) {
            System.out.println("Las que se van a Crear:         " + " Fecha:   " + listVTCCrear.get(i).getFechavigencia() + "|   Motivo:  " + listVTCCrear.get(i).getMotivocontrato().getNombre() + "|   Tipo:  " + listVTCCrear.get(i).getTipocontrato().getNombre() + "|   Ciudad:  " + listVTCCrear.get(i).getCiudad().getSecuencia() + "|   Empleado:  " + listVTCCrear.get(i).getEmpleado().getSecuencia());
        }
    }

    public VigenciasTiposContratos getDuplicarVTC() {
        return duplicarVTC;
    }

    public void setDuplicarVTC(VigenciasTiposContratos duplicarVTC) {
        this.duplicarVTC = duplicarVTC;
    }

    public List<Ciudades> getListaCiudades() {
        if (listaCiudades == null) {
            listaCiudades = administrarVigenciasTiposContratos.ciudades();
        }
        return listaCiudades;
    }

    public Ciudades getCiudadSelecionada() {
        return ciudadSelecionada;
    }

    public void setCiudadSelecionada(Ciudades ciudadSelecionada) {
        this.ciudadSelecionada = ciudadSelecionada;
    }

    public List<Ciudades> getFiltradoCiudades() {
        return filtradoCiudades;
    }

    public void setFiltradoCiudades(List<Ciudades> filtradoCiudades) {
        this.filtradoCiudades = filtradoCiudades;
    }

    public List<MotivosContratos> getListaMotivosContratos() {
        if (listaMotivosContratos == null) {
            listaMotivosContratos = administrarVigenciasTiposContratos.motivosContratos();
        }
        return listaMotivosContratos;
    }

    public void setListaMotivosContratos(List<MotivosContratos> listaMotivosContratos) {
        this.listaMotivosContratos = listaMotivosContratos;
    }

    public MotivosContratos getMotivoContratoSelecionado() {
        return MotivoContratoSelecionado;
    }

    public void setMotivoContratoSelecionado(MotivosContratos MotivoContratoSelecionado) {
        this.MotivoContratoSelecionado = MotivoContratoSelecionado;
    }

    public List<MotivosContratos> getFiltradoMotivoContrato() {
        return filtradoMotivoContrato;
    }

    public void setFiltradoMotivoContrato(List<MotivosContratos> filtradoMotivoContrato) {
        this.filtradoMotivoContrato = filtradoMotivoContrato;
    }

    public List<TiposContratos> getFiltradoTiposContrato() {
        return filtradoTiposContrato;
    }

    public void setFiltradoTiposContrato(List<TiposContratos> filtradoTiposContrato) {
        this.filtradoTiposContrato = filtradoTiposContrato;
    }

    public List<TiposContratos> getListaTiposContratos() {
        if (listaTiposContratos == null) {
            listaTiposContratos = administrarVigenciasTiposContratos.tiposContratos();
        }
        return listaTiposContratos;
    }

    public void setListaTiposContratos(List<TiposContratos> listaTiposContratos) {
        this.listaTiposContratos = listaTiposContratos;
    }

    public TiposContratos getTipoContratoSelecionado() {
        return TipoContratoSelecionado;
    }

    public void setTipoContratoSelecionado(TiposContratos TipoContratoSelecionado) {
        this.TipoContratoSelecionado = TipoContratoSelecionado;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public VigenciasTiposContratos getVigenciaSeleccionada() {
        return vigenciaSeleccionada;
    }

    public void setVigenciaSeleccionada(VigenciasTiposContratos vigenciaSeleccionada) {
        this.vigenciaSeleccionada = vigenciaSeleccionada;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public String getInfoRegistroCiudades() {
        return infoRegistroCiudades;
    }

    public String getInfoRegistroTiposContratos() {
        return infoRegistroTiposContratos;
    }

    public String getInfoRegistroMotivos() {
        return infoRegistroMotivos;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
