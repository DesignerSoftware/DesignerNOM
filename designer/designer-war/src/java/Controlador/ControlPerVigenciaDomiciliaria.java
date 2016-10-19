/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.HVHojasDeVida;
import Entidades.VigenciasDomiciliarias;
import Entidades.VigenciasFormales;
import Entidades.AdiestramientosF;
import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.EstadosCiviles;
import Entidades.Familiares;
import Entidades.HvExperienciasLaborales;
import Entidades.Instituciones;
import Entidades.MotivosRetiros;
import Entidades.Personas;
import Entidades.Profesiones;
import Entidades.SectoresEconomicos;
import Entidades.SoAntecedentes;
import Entidades.SoAntecedentesMedicos;
import Entidades.SoTiposAntecedentes;
import Entidades.Telefonos;
import Entidades.TiposDocumentos;
import Entidades.TiposEducaciones;
import Entidades.TiposFamiliares;
import Entidades.TiposTelefonos;
import Entidades.VigenciasDomiciliarias;
import Entidades.VigenciasEstadosCiviles;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasDomiciliariasInterface;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlPerVigenciaDomiciliaria")
@SessionScoped
public class ControlPerVigenciaDomiciliaria implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarVigenciasDomiciliariasInterface administrarVigDomiciliarias;

    ///lista vigencias domiciliarias
    private List<VigenciasDomiciliarias> listVigenciasDomiciliarias;
    private List<VigenciasDomiciliarias> listVigenciasDomiciliariasCrear;
    private List<VigenciasDomiciliarias> listVigenciasDomiciliariasModificar;
    private List<VigenciasDomiciliarias> listVigenciasDomiciliariasBorrar;
    private List<VigenciasDomiciliarias> listVigenciasDomiciliariasFiltrar;
    private VigenciasDomiciliarias vigenciasDomiciliariaSeleccionada;
    private VigenciasDomiciliarias nuevaVigenciaDomiciliaria;
    private VigenciasDomiciliarias duplicarVigenciaDomiciliaria;
    private VigenciasDomiciliarias editarVigenciaDomiciliaria;
    //lista vigencias Formales
    private List<VigenciasFormales> listVigenciasFormales;
    private List<VigenciasFormales> listVigenciasFormalesCrear;
    private List<VigenciasFormales> listVigenciasFormalesModificar;
    private List<VigenciasFormales> listVigenciasFormalesBorrar;
    private List<VigenciasFormales> listVigenciasFormalesFiltrar;
    private VigenciasFormales vigenciaFormalSeleccionada;
    private VigenciasFormales nuevaVigenciaFormal;
    private VigenciasFormales duplicarVigenciaFormal;
    private VigenciasFormales editarVigenciaFormal;
    //lista HV
    private List<HVHojasDeVida> listHojasdeVida;
    private List<HVHojasDeVida> listHojasdeVidaFiltrar;
    private List<HVHojasDeVida> listHojasdeVidaCrear;
    private List<HVHojasDeVida> listHojasdeVidaBorrar;
    private List<HVHojasDeVida> listHojasdeVidaModificar;
    private HVHojasDeVida nuevaHV;
    private HVHojasDeVida duplicarHV;
    private HVHojasDeVida editarHV;
    private HVHojasDeVida hvSeleccionada;
    //lista Personas
    private List<Personas> listPersonas;
    private List<Personas> listPersonasCrear;
    private List<Personas> listPersonasBorrar;
    private List<Personas> listPersonasModificar;
    private List<Personas> listPersonasFiltrar;
    private Personas nuevaPersona;
    private Personas editarPersona;
    private Personas duplicarPersona;
    private Personas personaSeleccionada;
    ///list Hv exp laboral
    private List<HvExperienciasLaborales> listhvExpLaborales;
    private List<HvExperienciasLaborales> listhvExpLaboralesCrear;
    private List<HvExperienciasLaborales> listhvExpLaboralesModificar;
    private List<HvExperienciasLaborales> listhvExpLaboralesBorrar;
    private List<HvExperienciasLaborales> listhvExpLaboralesFiltrar;
    private HvExperienciasLaborales hvexpSeleccionada;
    private HvExperienciasLaborales nuevahvexp;
    private HvExperienciasLaborales editarhvexp;
    private HvExperienciasLaborales duplicarhvexp;
    //list direcciones
    private List<Direcciones> listDirecciones;
    private List<Direcciones> listDireccionesCrear;
    private List<Direcciones> listDireccionesModificar;
    private List<Direcciones> listDireccionesFiltrar;
    private List<Direcciones> listDireccionesBorrar;
    private Direcciones direccionSeleccionada;
    private Direcciones nuevaDireccion;
    private Direcciones duplicarDireccion;
    private Direcciones editarDireccion;
    //listTelefonos
    private List<Telefonos> listTelefonos;
    private List<Telefonos> listTelefonosCrear;
    private List<Telefonos> listTelefonosModificar;
    private List<Telefonos> listTelefonosBorrar;
    private List<Telefonos> listTelefonosFiltrar;
    private Telefonos telefonoSeleccionado;
    private Telefonos nuevoTelefono;
    private Telefonos duplicarTelefono;
    private Telefonos editarTelefono;
    ///list estadosciviles
    private List<VigenciasEstadosCiviles> listVigenciaEstadoCivil;
    private List<VigenciasEstadosCiviles> listVigenciaEstadoCivilCrear;
    private List<VigenciasEstadosCiviles> listVigenciaEstadoCivilModificar;
    private List<VigenciasEstadosCiviles> listVigenciaEstadoCivilBorrar;
    private List<VigenciasEstadosCiviles> listVigenciaEstadoCivilFiltrar;
    private VigenciasEstadosCiviles vigenciaEstadoCivilSeleccionado;
    private VigenciasEstadosCiviles nuevaVigenciaEstadoCivil;
    private VigenciasEstadosCiviles duplicarVigenciaEstadoCivil;
    private VigenciasEstadosCiviles editarVigenciaEstadoCivil;
    //lista familiares
    private List<Familiares> listaFamiliares;
    private List<Familiares> listaFamiliaresCrear;
    private List<Familiares> listaFamiliaresModificar;
    private List<Familiares> listaFamiliaresBorrar;
    private List<Familiares> listaFamiliaresFiltrar;
    private Familiares familiarSeleccionado;
    private Familiares nuevoFamiliar;
    private Familiares duplicarFamiliar;
    private Familiares editarFamiliar;
//list antecedentes medicos
    private List<SoAntecedentesMedicos> listAntecedentesM;
    private List<SoAntecedentesMedicos> listAntecedentesMCrear;
    private List<SoAntecedentesMedicos> listAntecedentesModificar;
    private List<SoAntecedentesMedicos> listAntecedentesMBorrar;
    private List<SoAntecedentesMedicos> listAntecedentesMFiltrar;
    private SoAntecedentesMedicos antecedentemSeleccionado;
    private SoAntecedentesMedicos nuevoAntecedentem;
    private SoAntecedentesMedicos duplicarAntecedenteM;
    private SoAntecedentesMedicos editarAntecedenteM;
//lovs
    private List<TiposEducaciones> lovTiposEducaciones;
    private List<TiposEducaciones> lovTiposEducacionesFiltrar;
    private TiposEducaciones tipoEducacionSeleccionado;

    private List<Profesiones> lovProfesiones;
    private List<Profesiones> lovProfesionesFiltrar;
    private Profesiones profesionSeleccionada;

    private List<AdiestramientosF> lovAdiestramientos;
    private List<AdiestramientosF> lovAdiestramientosFiltrar;
    private AdiestramientosF adiestramientoSeleccionado;

    private List<Instituciones> lovInstituciones;
    private List<Instituciones> lovInstitucionesFiltrar;
    private Instituciones institucionSeleccionada;

    private List<MotivosRetiros> lovMotivos;
    private List<MotivosRetiros> lovMotivosFiltrar;
    private MotivosRetiros motivoSeleccionado;

    private List<TiposFamiliares> lovTiposFamiliares;
    private List<TiposFamiliares> lovTiposFamiliaresFiltrar;
    private TiposFamiliares tipoFamiliarSeleccionado;

    private List<Personas> lovPersonas;
    private List<Personas> lovPersonasFiltrar;
    private Personas lovPersonaSeleccionado;

    private List<SoAntecedentes> lovAntecedentes;
    private List<SoAntecedentes> lovAntecedentesFiltrar;
    private SoAntecedentes antecedenteSeleccionado;

    private List<SoTiposAntecedentes> lovTiposAntecedentes;
    private List<SoTiposAntecedentes> lovTiposAntecedentesFiltrar;
    private SoTiposAntecedentes tipoAntecedenteSeleccionado;

    private List<EstadosCiviles> lovEstadosCiviles;
    private List<EstadosCiviles> lovEstadosCivilesFiltrar;
    private EstadosCiviles estadoCivilSeleccionado;

    private List<TiposTelefonos> lovTiposTelefonos;
    private List<TiposTelefonos> lovTiposTelefonosFiltrar;
    private List<TiposTelefonos> tipoTelefonoSeleccionado;

    private List<Ciudades> lovCiudades;
    private List<Ciudades> lovCiudadesFiltrar;
    private Ciudades ciudadSeleccionada;

    private List<Cargos> lovCargos;
    private List<Cargos> lovCargosFiltrar;
    private Cargos cargoSeleccionada;

    //SectoresEconomicos
    private List<SectoresEconomicos> lovSectoresEconomicos;
    private SectoresEconomicos sectorSeleccionado;
    private List<SectoresEconomicos> filtrarLovSectoresEconomicos;

//otros
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    private String altoTabla;
    private Personas persona;
    private Empleados empleado;
    private Direcciones direccionActual;
    private Telefonos telefonoActual;
    private VigenciasEstadosCiviles estadoCivilActual;
    private HVHojasDeVida hvactual;
    private boolean guardado;
    private BigInteger l;
    private int k;
    private int cualCelda, tipoLista;
    private DataTable tablaC;
    private boolean activarLov;
    private String infoRegistroFamiliar, infoRegistroPersonas, infoRegistroTipoFamiliar, infoRegistroTipoDocumento, infoRegistroCiudades, infoRegistroCiudadNacimiento;
    private String infoRegistroAntecedenteM, infoRegistroEstadoCivil, infoRegistroDireccion, infoRegistroEducacion, infoRegistroExp, infoRegistroTelefono;
    private String fechaDesdeText, fechaHastaText;
    private Date fechaIni, fechaFin;
    private final SimpleDateFormat formatoFecha;
    private Date fechaParametro;
    private HVHojasDeVida hojaVida;
    private String distribucion, condiciones, descGeneral;

    /**
     * Creates a new instance of ControlPerVigenciaDomiciliaria
     */
    public ControlPerVigenciaDomiciliaria() {
        altoTabla = "70";
        nuevaVigenciaDomiciliaria = new VigenciasDomiciliarias();
        hojaVida = new HVHojasDeVida();
        nuevaHV = new HVHojasDeVida();
        nuevaPersona = new Personas();
        nuevahvexp = new HvExperienciasLaborales();
        nuevaDireccion = new Direcciones();
        nuevoTelefono = new Telefonos();
        nuevaVigenciaEstadoCivil = new VigenciasEstadosCiviles();
        nuevoFamiliar = new Familiares();
        nuevoAntecedentem = new SoAntecedentesMedicos();
        permitirIndex = true;
        guardado = true;
        activarLov = true;
        cualCelda = -1;
        tipoLista = 0;
        direccionActual = new Direcciones();
        persona = new Personas();
        telefonoActual = new Telefonos();
        estadoCivilActual = new VigenciasEstadosCiviles();
        hvactual = new HVHojasDeVida();
        antecedentemSeleccionado = new SoAntecedentesMedicos();
        antecedentemSeleccionado.setAntecedente(new SoAntecedentes());
        antecedentemSeleccionado.setTipoantecedente(new SoTiposAntecedentes());
        lovTiposTelefonos = null;
        nuevaVigenciaFormal = new VigenciasFormales();
        nuevaVigenciaFormal.setTipoeducacion(new TiposEducaciones());
        nuevaVigenciaFormal.setProfesion(new Profesiones());
        nuevaVigenciaFormal.setInstitucion(new Instituciones());
        nuevaVigenciaFormal.setAdiestramientof(new AdiestramientosF());
        nuevaVigenciaFormal.setFechavigencia(new Date());
        nuevaVigenciaFormal.setFechavencimientotarjeta(new Date());
        nuevaVigenciaFormal.setFechaexpediciontarjeta(new Date());
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        distribucion = "";
        descGeneral = "";
        condiciones = "";

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigDomiciliarias.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger secuencia) {
        listVigenciasDomiciliarias = null;
        listVigenciasFormales = null;
        listDirecciones = null;
        listTelefonos = null;
        listVigenciaEstadoCivil = null;
        listAntecedentesM = null;
        listaFamiliares = null;
        listhvExpLaborales = null;
        listAntecedentesM = null;
        persona = administrarVigDomiciliarias.encontrarPersona(secuencia);
        empleado = administrarVigDomiciliarias.buscarEmpleado(persona.getSecuencia());
//        fecha = direccionActual.getFechavigencia();
//        getListaTelefonos();

        if (persona != null) {
            hojaVida = administrarVigDomiciliarias.obtenerHojaVidaPersona(persona.getSecuencia());
        }
        if (hojaVida != null) {
            getListhvExpLaborales();
        }

    }

    /////FAMILIARES
    public void cambiarIndiceFamiliares(Familiares familiar, int celda) {
        if (permitirIndex == true) {
            cualCelda = celda;
            familiarSeleccionado = familiar;
            if (cualCelda == 1) {
                familiarSeleccionado.getPersonafamiliar().getNombreCompleto();
            } else if (cualCelda == 2) {
                familiarSeleccionado.getOcupacion();
            } else if (cualCelda == 3) {
                familiarSeleccionado.getTipofamiliar().getTipo();
            } else if (cualCelda == 4) {
                familiarSeleccionado.getServiciomedico();
            } else if (cualCelda == 5) {
                familiarSeleccionado.getSubsidiofamiliar();
            } else if (cualCelda == 6) {
                familiarSeleccionado.getBeneficiario();
            } else if (cualCelda == 7) {
                familiarSeleccionado.getUpcadicional();
            } else if (cualCelda == 8) {
                familiarSeleccionado.getValorupcadicional();
            }
        }
    }

    public void modificarFamiliar(Familiares familiar, String confirmarCambio, String valorConfirmar) {
        familiarSeleccionado = familiar;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    familiarSeleccionado.getTipofamiliar().setTipo(familiarSeleccionado.getTipofamiliar().getTipo());
                } else {
                    familiarSeleccionado.getTipofamiliar().setTipo(familiarSeleccionado.getTipofamiliar().getTipo());
                }
                for (int i = 0; i < lovTiposFamiliares.size(); i++) {
                    if (lovTiposFamiliares.get(i).getTipo().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        familiarSeleccionado.setTipofamiliar(lovTiposFamiliares.get(indiceUnicoElemento));
                    } else {
                        familiarSeleccionado.setTipofamiliar(lovTiposFamiliares.get(indiceUnicoElemento));
                    }
                    lovTiposFamiliares.clear();
                    getLovTiposFamiliares();
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formularioDialogos:tipoFamiliarDialogo");
                    RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                if (tipoLista == 0) {
                    familiarSeleccionado.setTipofamiliar(new TiposFamiliares());
                } else {
                    familiarSeleccionado.setTipofamiliar(new TiposFamiliares());
                }
                lovTiposFamiliares.clear();
                getLovTiposFamiliares();
            }

        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
                    if (listaFamiliaresModificar.isEmpty()) {
                        listaFamiliaresModificar.add(familiarSeleccionado);
                    } else if (!listaFamiliaresModificar.contains(familiarSeleccionado)) {
                        listaFamiliaresModificar.add(familiarSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
                if (listaFamiliaresModificar.isEmpty()) {
                    listaFamiliaresModificar.add(familiarSeleccionado);
                } else if (!listaFamiliaresModificar.contains(familiarSeleccionado)) {
                    listaFamiliaresModificar.add(familiarSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosFamiliares");
    }

    public void modificarFamiliares(Familiares familiar) {
        familiarSeleccionado = familiar;
        if (tipoLista == 0) {
            if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
                if (listaFamiliaresModificar.isEmpty()) {
                    listaFamiliaresModificar.add(familiarSeleccionado);
                } else if (!listaFamiliaresModificar.contains(familiarSeleccionado)) {
                    listaFamiliaresModificar.add(familiarSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
            if (listaFamiliaresModificar.isEmpty()) {
                listaFamiliaresModificar.add(familiarSeleccionado);
            } else if (!listaFamiliaresModificar.contains(familiarSeleccionado)) {
                listaFamiliaresModificar.add(familiarSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void crearNuevaPersona() {
//        crearPersonas.add(personas);
        try {
            k++;
            l = BigInteger.valueOf(k);
            nuevaPersona.setSecuencia(l);
            System.out.println("nueva persona : " + nuevaPersona);
            administrarVigDomiciliarias.crearPersona(nuevaPersona);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFamiliarP");
            RequestContext.getCurrentInstance().execute("PF('nuevoFamiliarPersona').hide()");
            RequestContext.getCurrentInstance().execute("PF('confirmarPersona').show()");
            RequestContext.getCurrentInstance().update("formularioDialogos:lovPersonasFamiliares");
            lovPersonas = null;
        } catch (Exception e) {
            System.out.println("error crear persona " + e.getMessage());
        }
    }

    public void limpiarPersona() {
        nuevaPersona = new Personas();
        nuevaPersona.setTipodocumento(new TiposDocumentos());
        nuevaPersona.setCiudaddocumento(new Ciudades());
        nuevaPersona.setCiudadnacimiento(new Ciudades());
    }

    public void asignarIndexFamiliares(Familiares familiar, int dlg, int LND) {
        familiarSeleccionado = familiar;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 1) {
            contarRegistroPersonas();
            RequestContext.getCurrentInstance().update("formularioDialogos:personaFamiliarDialogo");
            RequestContext.getCurrentInstance().execute("PF('personaFamiliarDialogo').show()");
        } else if (dlg == 2) {
            contarRegistroTipoFamiliar();
            RequestContext.getCurrentInstance().update("formularioDialogos:tipoFamiliarDialogo");
            RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').show()");
        } else if (dlg == 3) {
            contarRegistrosTipoDocumento();
            RequestContext.getCurrentInstance().update("formularioDialogos:tipoDocumentoDialogo");
            RequestContext.getCurrentInstance().execute("PF('tipoDocumentoDialogo').show()");
        } else if (dlg == 4) {
            contarRegistroCiudades();
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadDocumentoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ciudadDocumentoDialogo').show()");
        } else if (dlg == 5) {
            contarRegistroCiudadNacimiento();
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadNacimientoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ciudadNacimientoDialogo').show()");
        }
    }

    public void eventoFiltrarFamiliares() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosFamiliares();
    }

    public void contarRegistroTipoFamiliar() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTiposFamiliares");
    }

    public void contarRegistrosFamiliares() {
        RequestContext.getCurrentInstance().update("form:infoRegistroFamiliares");
    }

    public void contarRegistroPersonas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPersonas");
    }

    public void contarRegistrosTipoDocumento() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTipoDocumento");
    }

    public void contarRegistroCiudades() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCiudades");
    }

    public void contarRegistroCiudadNacimiento() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCiudadNacimiento");
    }

    /////antecedentes 
    public void cambiarIndiceAntecedentes(SoAntecedentesMedicos antecedentesm, int celda) {
        if (permitirIndex == true) {
            cualCelda = celda;
            antecedentemSeleccionado = antecedentesm;
            if (cualCelda == 1) {
                antecedentemSeleccionado.getFecha();
            } else if (cualCelda == 2) {
                antecedentemSeleccionado.getTipoantecedente().getDescripcion();
            } else if (cualCelda == 3) {
                antecedentemSeleccionado.getAntecedente().getDescripcion();
            } else if (cualCelda == 4) {
                antecedentemSeleccionado.getDescripcion();
            }
        }
    }

    public void modificarAntecedenteM(SoAntecedentesMedicos antecedentem, String confirmarCambio, String valorConfirmar) {
        antecedentemSeleccionado = antecedentem;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    antecedentemSeleccionado.setTipoantecedente(new SoTiposAntecedentes());
                } else {
                    antecedentemSeleccionado.setTipoantecedente(new SoTiposAntecedentes());
                }
                for (int i = 0; i < lovTiposAntecedentes.size(); i++) {
                    if (lovTiposAntecedentes.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        antecedentemSeleccionado.setTipoantecedente(lovTiposAntecedentes.get(indiceUnicoElemento));
                    } else {
                        antecedentemSeleccionado.setTipoantecedente(lovTiposAntecedentes.get(indiceUnicoElemento));
                    }
                    lovTiposAntecedentes.clear();
                    getLovTiposAntecedentes();
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formularioDialogos:tipoAntecedenteDialogo");
                    RequestContext.getCurrentInstance().execute("PF('tipoAntecedenteDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                if (tipoLista == 0) {
                    antecedentemSeleccionado.setTipoantecedente(new SoTiposAntecedentes());
                } else {
                    antecedentemSeleccionado.setTipoantecedente(new SoTiposAntecedentes());
                    lovTiposAntecedentes.clear();
                    getLovTiposAntecedentes();
                }

            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!listAntecedentesMCrear.contains(antecedentemSeleccionado)) {
                        if (listAntecedentesModificar.isEmpty()) {
                            listAntecedentesModificar.add(antecedentemSeleccionado);
                        } else if (!listAntecedentesModificar.contains(antecedentemSeleccionado)) {
                            listAntecedentesModificar.add(antecedentemSeleccionado);
                        }
                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }
                    }
                } else if (!listAntecedentesMCrear.contains(antecedentemSeleccionado)) {
                    if (listAntecedentesModificar.isEmpty()) {
                        listAntecedentesModificar.add(antecedentemSeleccionado);
                    } else if (!listAntecedentesModificar.contains(antecedentemSeleccionado)) {
                        listAntecedentesModificar.add(antecedentemSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            }
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
        }
    }

    public void modificarAntecedenteM(SoAntecedentesMedicos antecedentem) {
        antecedentemSeleccionado = antecedentem;
        if (tipoLista == 0) {
            if (!listAntecedentesMCrear.contains(antecedentemSeleccionado)) {
                if (listAntecedentesModificar.isEmpty()) {
                    listAntecedentesModificar.add(antecedentemSeleccionado);
                } else if (!listAntecedentesModificar.contains(antecedentemSeleccionado)) {
                    listAntecedentesModificar.add(antecedentemSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else if (!listAntecedentesMCrear.contains(antecedentemSeleccionado)) {
            if (listAntecedentesModificar.isEmpty()) {
                listAntecedentesModificar.add(antecedentemSeleccionado);
            } else if (!listAntecedentesModificar.contains(antecedentemSeleccionado)) {
                listAntecedentesModificar.add(antecedentemSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void limpiarAntecedenteM() {
        nuevoAntecedentem = new SoAntecedentesMedicos();
        nuevoAntecedentem.setAntecedente(new SoAntecedentes());
        nuevoAntecedentem.setTipoantecedente(new SoTiposAntecedentes());
    }

    public void asignarIndexAntecedentesM(SoAntecedentesMedicos antecedenteM, int dlg, int LND) {
        antecedentemSeleccionado = antecedenteM;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 1) {
            contarRegistroPersonas();
            RequestContext.getCurrentInstance().update("formularioDialogos:personaFamiliarDialogo");
            RequestContext.getCurrentInstance().execute("PF('personaFamiliarDialogo').show()");
        } else if (dlg == 2) {
            contarRegistroTipoFamiliar();
            RequestContext.getCurrentInstance().update("formularioDialogos:tipoFamiliarDialogo");
            RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').show()");
        }
    }

    public void eventoFiltrarAntecedenteM() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosAntecedentesM();
    }

    public void contarRegistrosAntecedentesM() {
        RequestContext.getCurrentInstance().update("form:infoRegistroAntecedentesM");
    }

    public void contarRegistroTipoAntecedentes() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTiposAntecedentes");
    }

    public void contarRegistroAntecedentes() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroAntecedentes");
    }

    ////direcciones
    public void cambiarIndiceDirecciones(Direcciones direccion, int celda) {
        if (permitirIndex == true) {
            direccionSeleccionada = direccion;
            cualCelda = celda;
            if (tipoLista == 0) {
                direccionSeleccionada.getSecuencia();
//                deshabilitarBotonLOV();
                if (cualCelda == 6) {
                    contarRegistroCiudades();
                    direccionSeleccionada.getCiudad().getNombre();
//                    habilitarBotonLOV();
                }
            } else {
                direccionSeleccionada.getSecuencia();
//                deshabilitarBotonLOV();
                if (cualCelda == 6) {
                    contarRegistroCiudades();
                    direccionSeleccionada.getCiudad().getNombre();
//                    habilitarBotonLOV();
                }
            }
        }
    }

    public void seleccionarTipoPpal(String estadoTipoPpal, Direcciones direccion, int celda) {
        if (tipoLista == 0) {
            if (estadoTipoPpal.equals("CALLE")) {
                direccionSeleccionada.setTipoppal("C");
            } else if (estadoTipoPpal.equals("CARRERA")) {
                direccionSeleccionada.setTipoppal("K");
            } else if (estadoTipoPpal.equals("AVENIDA CALLE")) {
                direccionSeleccionada.setTipoppal("A");
            } else if (estadoTipoPpal.equals("AVENIDA CARRERA")) {
                direccionSeleccionada.setTipoppal("M");
            } else if (estadoTipoPpal.equals("DIAGONAL")) {
                direccionSeleccionada.setTipoppal("D");
            } else if (estadoTipoPpal.equals("TRANSVERSAL")) {
                direccionSeleccionada.setTipoppal("T");
            } else if (estadoTipoPpal.equals("OTROS")) {
                direccionSeleccionada.setTipoppal("O");
            }

            if (!listDireccionesCrear.contains(direccionSeleccionada)) {
                if (listDireccionesModificar.isEmpty()) {
                    listDireccionesModificar.add(direccionSeleccionada);
                } else if (!listDireccionesModificar.contains(direccionSeleccionada)) {
                    listDireccionesModificar.add(direccionSeleccionada);
                }
            }
        } else {
            if (estadoTipoPpal.equals("CALLE")) {
                direccionSeleccionada.setTipoppal("C");
            } else if (estadoTipoPpal.equals("CARRERA")) {
                direccionSeleccionada.setTipoppal("K");
            } else if (estadoTipoPpal.equals("AVENIDA CALLE")) {
                direccionSeleccionada.setTipoppal("A");
            } else if (estadoTipoPpal.equals("AVENIDA CARRERA")) {
                direccionSeleccionada.setTipoppal("M");
            } else if (estadoTipoPpal.equals("DIAGONAL")) {
                direccionSeleccionada.setTipoppal("D");
            } else if (estadoTipoPpal.equals("TRANSVERSAL")) {
                direccionSeleccionada.setTipoppal("T");
            } else if (estadoTipoPpal.equals("OTROS")) {
                direccionSeleccionada.setTipoppal("O");
            }

            if (!listDireccionesCrear.contains(direccionSeleccionada)) {
                if (listDireccionesModificar.isEmpty()) {
                    listDireccionesModificar.add(direccionSeleccionada);
                } else if (!listDireccionesModificar.contains(direccionSeleccionada)) {
                    listDireccionesModificar.add(direccionSeleccionada);
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
    }

    public void seleccionarTipoPpalNuevaDireccion(String estadoTipoPpal, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoTipoPpal.equals("CALLE")) {
                nuevaDireccion.setTipoppal("C");
            } else if (estadoTipoPpal.equals("CARRERA")) {
                nuevaDireccion.setTipoppal("K");
            } else if (estadoTipoPpal.equals("AVENIDA CALLE")) {
                nuevaDireccion.setTipoppal("A");
            } else if (estadoTipoPpal.equals("AVENIDA CARRERA")) {
                nuevaDireccion.setTipoppal("M");
            } else if (estadoTipoPpal.equals("DIAGONAL")) {
                nuevaDireccion.setTipoppal("D");
            } else if (estadoTipoPpal.equals("TRANSVERSAL")) {
                nuevaDireccion.setTipoppal("T");
            } else if (estadoTipoPpal.equals("OTROS")) {
                nuevaDireccion.setTipoppal("O");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUbicacionPrincipal");
        } else {
            if (estadoTipoPpal.equals("CALLE")) {
                duplicarDireccion.setTipoppal("C");
            } else if (estadoTipoPpal.equals("CARRERA")) {
                duplicarDireccion.setTipoppal("K");
            } else if (estadoTipoPpal.equals("AVENIDA CALLE")) {
                duplicarDireccion.setTipoppal("A");
            } else if (estadoTipoPpal.equals("AVENIDA CARRERA")) {
                duplicarDireccion.setTipoppal("M");
            } else if (estadoTipoPpal.equals("DIAGONAL")) {
                duplicarDireccion.setTipoppal("D");
            } else if (estadoTipoPpal.equals("TRANSVERSAL")) {
                duplicarDireccion.setTipoppal("T");
            } else if (estadoTipoPpal.equals("OTROS")) {
                duplicarDireccion.setTipoppal("O");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUbicacionPrincipal");
        }

    }

    public void seleccionarTipoSecundario(String estadoTipoSecundario, int indice, int celda) {
        if (tipoLista == 0) {
            if (estadoTipoSecundario.equals("CALLE")) {
                direccionSeleccionada.setTiposecundario("C");
            } else if (estadoTipoSecundario.equals("CARRERA")) {
                direccionSeleccionada.setTiposecundario("K");
            } else if (estadoTipoSecundario.equals("AVENIDA CALLE")) {
                direccionSeleccionada.setTiposecundario("A");
            } else if (estadoTipoSecundario.equals("AVENIDA CARRERA")) {
                direccionSeleccionada.setTiposecundario("M");
            } else if (estadoTipoSecundario.equals("DIAGONAL")) {
                direccionSeleccionada.setTiposecundario("D");
            } else if (estadoTipoSecundario.equals("TRANSVERSAL")) {
                direccionSeleccionada.setTiposecundario("T");
            } else if (estadoTipoSecundario.equals("OTROS")) {
                direccionSeleccionada.setTiposecundario("O");
            }

            if (!listDireccionesCrear.contains(direccionSeleccionada)) {
                if (listDireccionesModificar.isEmpty()) {
                    listDireccionesModificar.add(direccionSeleccionada);
                } else if (!listDireccionesModificar.contains(direccionSeleccionada)) {
                    listDireccionesModificar.add(direccionSeleccionada);
                }
            }
        } else {
            if (estadoTipoSecundario.equals("CALLE")) {
                listDireccionesFiltrar.get(indice).setTiposecundario("C");
            } else if (estadoTipoSecundario.equals("CARRERA")) {
                listDireccionesFiltrar.get(indice).setTiposecundario("K");
            } else if (estadoTipoSecundario.equals("AVENIDA CALLE")) {
                listDireccionesFiltrar.get(indice).setTiposecundario("A");
            } else if (estadoTipoSecundario.equals("AVENIDA CARRERA")) {
                listDireccionesFiltrar.get(indice).setTiposecundario("M");
            } else if (estadoTipoSecundario.equals("DIAGONAL")) {
                listDireccionesFiltrar.get(indice).setTiposecundario("D");
            } else if (estadoTipoSecundario.equals("TRANSVERSAL")) {
                listDireccionesFiltrar.get(indice).setTiposecundario("T");
            } else if (estadoTipoSecundario.equals("OTROS")) {
                listDireccionesFiltrar.get(indice).setTiposecundario("O");
            }

            if (!listDireccionesCrear.contains(listDireccionesFiltrar.get(indice))) {
                if (listDireccionesModificar.isEmpty()) {
                    listDireccionesModificar.add(listDireccionesFiltrar.get(indice));
                } else if (!listDireccionesModificar.contains(listDireccionesFiltrar.get(indice))) {
                    listDireccionesModificar.add(listDireccionesFiltrar.get(indice));
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
    }

    public void seleccionarTipoSecundarioNuevaDireccion(String estadoTipoSecundario, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoTipoSecundario.equals("CALLE")) {
                nuevaDireccion.setTiposecundario("C");
            } else if (estadoTipoSecundario.equals("CARRERA")) {
                nuevaDireccion.setTiposecundario("K");
            } else if (estadoTipoSecundario.equals("AVENIDA CALLE")) {
                nuevaDireccion.setTiposecundario("A");
            } else if (estadoTipoSecundario.equals("AVENIDA CARRERA")) {
                nuevaDireccion.setTiposecundario("M");
            } else if (estadoTipoSecundario.equals("DIAGONAL")) {
                nuevaDireccion.setTiposecundario("D");
            } else if (estadoTipoSecundario.equals("TRANSVERSAL")) {
                nuevaDireccion.setTiposecundario("T");
            } else if (estadoTipoSecundario.equals("OTROS")) {
                nuevaDireccion.setTiposecundario("O");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUbicacionSecundaria");
        } else {
            if (estadoTipoSecundario.equals("CALLE")) {
                duplicarDireccion.setTiposecundario("C");
            } else if (estadoTipoSecundario.equals("CARRERA")) {
                duplicarDireccion.setTiposecundario("K");
            } else if (estadoTipoSecundario.equals("AVENIDA CALLE")) {
                duplicarDireccion.setTiposecundario("A");
            } else if (estadoTipoSecundario.equals("AVENIDA CARRERA")) {
                duplicarDireccion.setTiposecundario("M");
            } else if (estadoTipoSecundario.equals("DIAGONAL")) {
                duplicarDireccion.setTiposecundario("D");
            } else if (estadoTipoSecundario.equals("TRANSVERSAL")) {
                duplicarDireccion.setTiposecundario("T");
            } else if (estadoTipoSecundario.equals("OTROS")) {
                duplicarDireccion.setTiposecundario("O");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUbicacionSecundaria");
        }

    }

    public void seleccionarTipoVivienda(String estadoTipoVivienda, int indice, int celda) {
        if (tipoLista == 0) {
            if (estadoTipoVivienda.equals("CASA")) {
                direccionSeleccionada.setTipovivienda("CASA");
            } else if (estadoTipoVivienda.equals("APARTAMENTO")) {
                direccionSeleccionada.setTipovivienda("APARTAMENTO");
            } else if (estadoTipoVivienda.equals("FINCA")) {
                direccionSeleccionada.setTipovivienda("FINCA");
            }

            if (!listDireccionesCrear.contains(direccionSeleccionada)) {
                if (listDireccionesModificar.isEmpty()) {
                    listDireccionesModificar.add(direccionSeleccionada);
                } else if (!listDireccionesModificar.contains(direccionSeleccionada)) {
                    listDireccionesModificar.add(direccionSeleccionada);
                }
            }
        } else {
            if (estadoTipoVivienda.equals("CASA")) {
                listDireccionesFiltrar.get(indice).setTipovivienda("CASA");
            } else if (estadoTipoVivienda.equals("APARTAMENTO")) {
                listDireccionesFiltrar.get(indice).setTipovivienda("APARTAMENTO");
            } else if (estadoTipoVivienda.equals("FINCA")) {
                listDireccionesFiltrar.get(indice).setTipovivienda("FINCA");
            }

            if (!listDireccionesCrear.contains(listDireccionesFiltrar.get(indice))) {
                if (listDireccionesModificar.isEmpty()) {
                    listDireccionesModificar.add(listDireccionesFiltrar.get(indice));
                } else if (!listDireccionesModificar.contains(listDireccionesFiltrar.get(indice))) {
                    listDireccionesModificar.add(listDireccionesFiltrar.get(indice));
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
    }

    public void seleccionarTipoViviendaNuevaDireccion(String estadoTipoVivienda, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoTipoVivienda.equals("FINCA")) {
                nuevaDireccion.setTipovivienda("FINCA");
            } else if (estadoTipoVivienda.equals("CASA")) {
                nuevaDireccion.setTipovivienda("CASA");
            } else if (estadoTipoVivienda.equals("APARTAMENTO")) {
                nuevaDireccion.setTipovivienda("APARTAMENTO");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoVivienda");
        } else {
            if (estadoTipoVivienda.equals("FINCA")) {
                duplicarDireccion.setTipovivienda("FINCA");
            } else if (estadoTipoVivienda.equals("CASA")) {
                duplicarDireccion.setTipovivienda("CASA");
            } else if (estadoTipoVivienda.equals("APARTAMENTO")) {
                duplicarDireccion.setTipovivienda("APARTAMENTO");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoVivienda");
        }
    }

    public void seleccionarHipoteca(String estadoHipoteca, int indice, int celda) {
        if (tipoLista == 0) {
            if (estadoHipoteca.equals("SI")) {
                direccionSeleccionada.setTipovivienda("S");
            } else if (estadoHipoteca.equals("NO")) {
                direccionSeleccionada.setTiposecundario("N");
            }

            if (!listDireccionesCrear.contains(direccionSeleccionada)) {
                if (listDireccionesModificar.isEmpty()) {
                    listDireccionesModificar.add(direccionSeleccionada);
                } else if (!listDireccionesModificar.contains(direccionSeleccionada)) {
                    listDireccionesModificar.add(direccionSeleccionada);
                }
            }
        } else if (estadoHipoteca.equals("SI")) {
            listDireccionesFiltrar.get(indice).setTiposecundario("S");
        } else if (estadoHipoteca.equals("NO")) {
            listDireccionesFiltrar.get(indice).setTiposecundario("N");

            if (!listDireccionesCrear.contains(listDireccionesFiltrar.get(indice))) {
                if (listDireccionesModificar.isEmpty()) {
                    listDireccionesModificar.add(listDireccionesFiltrar.get(indice));
                } else if (!listDireccionesModificar.contains(listDireccionesFiltrar.get(indice))) {
                    listDireccionesModificar.add(listDireccionesFiltrar.get(indice));
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
    }

    public void seleccionarHipotecaNuevaDireccion(String estadoHipoteca, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoHipoteca.equals("SI")) {
                nuevaDireccion.setHipoteca("S");
            } else {
                nuevaDireccion.setHipoteca("N");
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaHipoteca");
        } else if (estadoHipoteca.equals("SI")) {
            duplicarDireccion.setHipoteca("S");
        } else {
            duplicarDireccion.setTipovivienda("N");
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarHipoteca");
    }

    public void modificarDirecciones(Direcciones direccion, String confirmarCambio, String valorConfirmar) {
        direccionSeleccionada = direccion;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listDireccionesCrear.contains(direccionSeleccionada)) {

                    if (listDireccionesModificar.isEmpty()) {
                        listDireccionesModificar.add(direccionSeleccionada);
                    } else if (!listDireccionesModificar.contains(direccionSeleccionada)) {
                        listDireccionesModificar.add(direccionSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }

            } else if (!listDireccionesCrear.contains(direccionSeleccionada)) {

                if (listDireccionesModificar.isEmpty()) {
                    listDireccionesModificar.add(direccionSeleccionada);
                } else if (!listDireccionesModificar.contains(direccionSeleccionada)) {
                    listDireccionesModificar.add(direccionSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                }
            }
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
        } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            if (tipoLista == 0) {
                direccionSeleccionada.getCiudad().setNombre(direccionSeleccionada.getCiudad().getNombre());
            } else {
                direccionSeleccionada.getCiudad().setNombre(direccionSeleccionada.getCiudad().getNombre());
            }

            for (int i = 0; i < lovCiudades.size(); i++) {
                if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    direccionSeleccionada.setCiudad(lovCiudades.get(indiceUnicoElemento));
                } else {
                    direccionSeleccionada.setCiudad(lovCiudades.get(indiceUnicoElemento));
                }
                lovCiudades.clear();
                getLovCiudades();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listDireccionesCrear.contains(direccionSeleccionada)) {
                    if (listDireccionesModificar.isEmpty()) {
                        listDireccionesModificar.add(direccionSeleccionada);
                    } else if (!listDireccionesModificar.contains(direccionSeleccionada)) {
                        listDireccionesModificar.add(direccionSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            } else if (!listDireccionesCrear.contains(direccionSeleccionada)) {

                if (listDireccionesModificar.isEmpty()) {
                    listDireccionesModificar.add(direccionSeleccionada);
                } else if (!listDireccionesModificar.contains(direccionSeleccionada)) {
                    listDireccionesModificar.add(direccionSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
    }

    public void asignarIndexDirecciones(Direcciones direccion) {
        direccionSeleccionada = direccion;
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistroCiudades();
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
        tipoActualizacion = 0;
    }

    public void contarRegistrosDirecciones() {
        RequestContext.getCurrentInstance().update("form:infoRegistroDireccion");
    }

    public void eventoFiltrarDirecciones() {
        contarRegistrosDirecciones();
    }

////// telefonos
    public void cambiarIndiceTelefonos(Telefonos telefono, int celda) {
        if (permitirIndex == true) {
            telefonoSeleccionado = telefono;
            cualCelda = celda;
            if (tipoLista == 0) {
                telefonoSeleccionado.getSecuencia();
                if (cualCelda == 1) {
                    telefonoSeleccionado.getTipotelefono().getNombre();
                } else if (cualCelda == 3) {
                    telefonoSeleccionado.getCiudad().getNombre();
                }
            } else {
                telefonoSeleccionado.getSecuencia();
                if (cualCelda == 1) {
                    telefonoSeleccionado.getTipotelefono().getNombre();
                } else if (cualCelda == 3) {
                    telefonoSeleccionado.getCiudad().getNombre();
                }
            }
        }
    }

    public void asignarIndexTelefono(Telefonos telefono, int dlg, int LND) {
        telefonoSeleccionado = telefono;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;

        if (dlg == 0) {
            getLovTiposTelefonos();
//            contarRegistroTT();
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposTelefonosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposTelefonosDialogo').show()");
        } else if (dlg == 1) {
            getLovCiudades();
//            contarRegistroCiudad();
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
        }

    }

    public void modificarTelefonos(Telefonos telefono, String confirmarCambio, String valorConfirmar) {
        telefonoSeleccionado = telefono;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listTelefonosCrear.contains(telefonoSeleccionado)) {

                    if (listTelefonosModificar.isEmpty()) {
                        listTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listTelefonosModificar.contains(telefonoSeleccionado)) {
                        listTelefonosModificar.add(telefonoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else if (!listTelefonosCrear.contains(telefonoSeleccionado)) {

                if (listTelefonosModificar.isEmpty()) {
                    listTelefonosModificar.add(telefonoSeleccionado);
                } else if (!listTelefonosModificar.contains(telefonoSeleccionado)) {
                    listTelefonosModificar.add(telefonoSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
        } else if (confirmarCambio.equalsIgnoreCase("TIPOTELEFONO")) {
            if (tipoLista == 0) {
                telefonoSeleccionado.getTipotelefono().setNombre(telefonoSeleccionado.getTipotelefono().getNombre());
            } else {
                telefonoSeleccionado.getTipotelefono().setNombre(telefonoSeleccionado.getTipotelefono().getNombre());
            }

            for (int i = 0; i < lovTiposTelefonos.size(); i++) {
                if (lovTiposTelefonos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    telefonoSeleccionado.setTipotelefono(lovTiposTelefonos.get(indiceUnicoElemento));
                } else {
                    telefonoSeleccionado.setTipotelefono(lovTiposTelefonos.get(indiceUnicoElemento));
                }
                lovTiposTelefonos.clear();
                getLovTiposTelefonos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposTelefonosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposTelefonosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            if (tipoLista == 0) {
                telefonoSeleccionado.getCiudad().setNombre(telefonoSeleccionado.getCiudad().getNombre());
            } else {
                telefonoSeleccionado.getCiudad().setNombre(telefonoSeleccionado.getCiudad().getNombre());
            }
            for (int i = 0; i < lovCiudades.size(); i++) {
                if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    telefonoSeleccionado.setCiudad(lovCiudades.get(indiceUnicoElemento));
                } else {
                    telefonoSeleccionado.setCiudad(lovCiudades.get(indiceUnicoElemento));
                }
                lovCiudades.clear();
                getLovCiudades();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listTelefonosCrear.contains(telefonoSeleccionado)) {
                    if (listTelefonosModificar.isEmpty()) {
                        listTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listTelefonosModificar.contains(telefonoSeleccionado)) {
                        listTelefonosModificar.add(telefonoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else if (!listTelefonosCrear.contains(telefonoSeleccionado)) {

                if (listTelefonosModificar.isEmpty()) {
                    listTelefonosModificar.add(telefonoSeleccionado);
                } else if (!listTelefonosModificar.contains(telefonoSeleccionado)) {
                    listTelefonosModificar.add(telefonoSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
    }

    public void eventoFiltrarTelefonos() {
        contarRegistrosTelefono();
    }

    public void contarRegistrosTelefono() {
        RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
    }

    //// estados civiles
    public void cambiarIndiceEstadosCiviles(VigenciasEstadosCiviles vigenciaEstadoCivil, int celda) {

        if (permitirIndex == true) {
//            deshabilitarBotonLov();
            vigenciaEstadoCivilSeleccionado = vigenciaEstadoCivil;
            cualCelda = celda;
            vigenciaEstadoCivilSeleccionado.getSecuencia();
            if (cualCelda == 1) {
//                habilitarBotonLov();
//                contarRegistroEC();
                if (tipoLista == 0) {
                    vigenciaEstadoCivilSeleccionado.getEstadocivil().getDescripcion();
                } else {
                    vigenciaEstadoCivilSeleccionado.getEstadocivil().getDescripcion();
                }
            }

        }
    }

    public void asignarIndexEstadosCiviles(VigenciasEstadosCiviles vigenciaEstadoCivil, int LND, int dig) {
        RequestContext context = RequestContext.getCurrentInstance();
        vigenciaEstadoCivilSeleccionado = vigenciaEstadoCivil;
        if (LND == 0) {
//                deshabilitarBotonLov();
            tipoActualizacion = 0;
        } else if (LND == 1) {
//                deshabilitarBotonLov();
            tipoActualizacion = 1;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
//                deshabilitarBotonLov();
            tipoActualizacion = 2;
        }
        if (dig == 1) {
//                habilitarBotonLov();
//                contarRegistroEC();
            RequestContext.getCurrentInstance().update("formularioDialogos:EstadoCivilDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
        }
    }

    public void modificarVigenciaEstadoCivil(VigenciasEstadosCiviles vigenciaEstadoCivil, String confirmarCambio, String valorConfirmar) {
        vigenciaEstadoCivilSeleccionado = vigenciaEstadoCivil;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        int contador = 0;
        boolean banderita = false;
        Short a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("VIGESTADOSCIVILES")) {
            System.out.println("MODIFICANDO ESTADO CIVIL : " + vigenciaEstadoCivilSeleccionado.getEstadocivil().getDescripcion());
            if (!vigenciaEstadoCivilSeleccionado.getEstadocivil().getDescripcion().equals("")) {
                if (tipoLista == 0) {
                    vigenciaEstadoCivilSeleccionado.getEstadocivil().setDescripcion(vigenciaEstadoCivilSeleccionado.getEstadocivil().getDescripcion());
                } else {
                    vigenciaEstadoCivilSeleccionado.getEstadocivil().setDescripcion(vigenciaEstadoCivilSeleccionado.getEstadocivil().getDescripcion());
                }

                for (int i = 0; i < lovEstadosCiviles.size(); i++) {
                    if (lovEstadosCiviles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        vigenciaEstadoCivilSeleccionado.setEstadocivil(lovEstadosCiviles.get(indiceUnicoElemento));
                    } else {
                        vigenciaEstadoCivil.setEstadocivil(lovEstadosCiviles.get(indiceUnicoElemento));
                    }
                    lovEstadosCiviles.clear();
                    lovEstadosCiviles = null;

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formularioDialogos:EstadoCivilDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (tipoLista == 0) {
                    vigenciaEstadoCivilSeleccionado.getEstadocivil().setDescripcion(vigenciaEstadoCivilSeleccionado.getEstadocivil().getDescripcion());
                } else {
                    vigenciaEstadoCivilSeleccionado.getEstadocivil().setDescripcion(vigenciaEstadoCivilSeleccionado.getEstadocivil().getDescripcion());
                }
                RequestContext.getCurrentInstance().update("formularioDialogos:EstadoCivilDialogo");
                RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!listVigenciaEstadoCivilCrear.contains(vigenciaEstadoCivilSeleccionado)) {

                        if (listVigenciaEstadoCivilModificar.isEmpty()) {
                            listVigenciaEstadoCivilModificar.add(vigenciaEstadoCivilSeleccionado);
                        } else if (!listVigenciaEstadoCivilModificar.contains(vigenciaEstadoCivilSeleccionado)) {
                            listVigenciaEstadoCivilModificar.add(vigenciaEstadoCivilSeleccionado);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                } else if (!listVigenciaEstadoCivilCrear.contains(vigenciaEstadoCivil)) {

                    if (listVigenciaEstadoCivilModificar.isEmpty()) {
                        listVigenciaEstadoCivilModificar.add(vigenciaEstadoCivil);
                    } else if (!listVigenciaEstadoCivilModificar.contains(vigenciaEstadoCivil)) {
                        listVigenciaEstadoCivilModificar.add(vigenciaEstadoCivil);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }

            RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        }

    }

    public void eventoFiltrarEstadoCivil() {
        contarRegistrosEstadoCivil();
    }

    public void contarRegistrosEstadoCivil() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEstadoCivil");
    }

    //////nivel academico
    public void cambiarIndiceEducacion(VigenciasFormales vigenciaformal, int celda) {
        if (permitirIndex == true) {
            vigenciaFormalSeleccionada = vigenciaformal;
            cualCelda = celda;
//            CualTabla = 0;
//            deshabilitarBotonLov();
//            tablaImprimir = ":formExportar:datosVigenciasFormalesExportar";
//            nombreArchivo = "VigenciasFormalesXML";
//            RequestContext context = RequestContext.getCurrentInstance();
//            RequestContext.getCurrentInstance().update("form:exportarXML");
            vigenciaFormalSeleccionada.getSecuencia();
            if (cualCelda == 1) {
//                habilitarBotonLov();
                vigenciaFormalSeleccionada.getTipoeducacion().getNombre();
            } else if (cualCelda == 2) {
//                habilitarBotonLov();
                vigenciaFormalSeleccionada.getProfesion().getDescripcion();
            } else if (cualCelda == 3) {
//                habilitarBotonLov();
                vigenciaFormalSeleccionada.getInstitucion().getDescripcion();
            } else if (cualCelda == 4) {
//                habilitarBotonLov();
                vigenciaFormalSeleccionada.getAdiestramientof().getDescripcion();
            } else if (cualCelda == 5) {
                vigenciaFormalSeleccionada.getCalificacionobtenida();
            } else if (cualCelda == 4) {
                vigenciaFormalSeleccionada.getNumerotarjeta();
            } else if (cualCelda == 4) {
                vigenciaFormalSeleccionada.getFechaexpediciontarjeta();
            } else if (cualCelda == 4) {
                vigenciaFormalSeleccionada.getFechavencimientotarjeta();
            } else if (cualCelda == 4) {
                vigenciaFormalSeleccionada.getObservacion();
            }
        }
    }

    public void asignarIndexEducacion(VigenciasFormales vigenciaFormal, int dlg, int LND) {
        vigenciaFormalSeleccionada = vigenciaFormal;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 0) {
//            contarRegistroEducacion();
//            RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEducacion");
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposEducacionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposEducacionesDialogo').show()");
        } else if (dlg == 1) {
//            contarRegistroProfesion();
//            RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistrosProfesion");
            RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').show()");
        } else if (dlg == 2) {
//            contarRegistroInstitucionesF();
//            RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroInstitucionesF");
            RequestContext.getCurrentInstance().update("formularioDialogos:institucionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').show()");
        } else if (dlg == 3) {
//            contarRegistroAdiestramientoF();
//            RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroAdiestramientosF");
            RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosFDialogo");
            RequestContext.getCurrentInstance().execute("PF('adiestramientosFDialogo').show()");
        }
    }

    public void modificarEducacion(VigenciasFormales vigenciaformal, String confirmarCambio, String valorConfirmar) {
        vigenciaFormalSeleccionada = vigenciaformal;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {

                    if (listVigenciasFormalesModificar.isEmpty()) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
            } else if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {

                if (listVigenciasFormalesModificar.isEmpty()) {
                    listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                    listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            }
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
        } else if (confirmarCambio.equalsIgnoreCase("TIPOEDUCACION")) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.getTipoeducacion().setNombre(vigenciaFormalSeleccionada.getTipoeducacion().getNombre());
            } else {
                vigenciaFormalSeleccionada.getTipoeducacion().setNombre(vigenciaFormalSeleccionada.getTipoeducacion().getNombre());
            }

            for (int i = 0; i < lovTiposEducaciones.size(); i++) {
                if (lovTiposEducaciones.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaFormalSeleccionada.setTipoeducacion(lovTiposEducaciones.get(indiceUnicoElemento));
                } else {
                    vigenciaFormalSeleccionada.setTipoeducacion(lovTiposEducaciones.get(indiceUnicoElemento));
                }
                lovTiposEducaciones.clear();
                getLovTiposEducaciones();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposEducacionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposEducacionesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("PROFESION")) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.getProfesion().setDescripcion(vigenciaFormalSeleccionada.getProfesion().getDescripcion());
            } else {
                vigenciaFormalSeleccionada.getProfesion().setDescripcion(vigenciaFormalSeleccionada.getProfesion().getDescripcion());
            }
            for (int i = 0; i < lovProfesiones.size(); i++) {
                if (lovProfesiones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaFormalSeleccionada.setProfesion(lovProfesiones.get(indiceUnicoElemento));
                } else {
                    vigenciaFormalSeleccionada.setProfesion(lovProfesiones.get(indiceUnicoElemento));
                }
                lovProfesiones.clear();
                getLovProfesiones();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("INSTITUCION")) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.getInstitucion().setDescripcion(vigenciaFormalSeleccionada.getInstitucion().getDescripcion());
            } else {
                vigenciaFormalSeleccionada.getInstitucion().setDescripcion(vigenciaFormalSeleccionada.getInstitucion().getDescripcion());
            }
            for (int i = 0; i < lovInstituciones.size(); i++) {
                if (lovInstituciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaFormalSeleccionada.setInstitucion(lovInstituciones.get(indiceUnicoElemento));
                } else {
                    vigenciaFormalSeleccionada.setInstitucion(lovInstituciones.get(indiceUnicoElemento));
                }
                lovInstituciones.clear();
                getLovInstituciones();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:institucionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("ADIESTRAMENTOF")) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.getAdiestramientof().setDescripcion(vigenciaFormalSeleccionada.getAdiestramientof().getDescripcion());
            } else {
                vigenciaFormalSeleccionada.getAdiestramientof().setDescripcion(vigenciaFormalSeleccionada.getAdiestramientof().getDescripcion());
            }
            for (int i = 0; i < lovAdiestramientos.size(); i++) {
                if (lovAdiestramientos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaFormalSeleccionada.setAdiestramientof(lovAdiestramientos.get(indiceUnicoElemento));
                } else {
                    vigenciaFormalSeleccionada.setAdiestramientof(lovAdiestramientos.get(indiceUnicoElemento));
                }
                lovAdiestramientos.clear();
                getLovAdiestramientos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listVigenciasFormalesModificar.isEmpty()) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
            } else if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {

                if (listVigenciasFormalesModificar.isEmpty()) {
                    listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                    listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
    }

    public void eventoFiltrarEducacion() {
        contarRegistrosEducacion();
    }

    public void contarRegistrosEducacion() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEducacion");
    }

    //////////experiencia Laboral//////
    public void cambiarIndiceExperiencia(HvExperienciasLaborales experienciaLaboral, int celda) {
        if (permitirIndex == true) {
            hvexpSeleccionada = experienciaLaboral;
            cualCelda = celda;
//                    fechaFin = hvexpSeleccionada.getFechahasta();
            if (cualCelda == 0) {
                hvexpSeleccionada.getEmpresa();
            } else if (cualCelda == 1) {
                hvexpSeleccionada.getFechadesde();
            } else if (cualCelda == 2) {
                hvexpSeleccionada.getFechahasta();
            } else if (cualCelda == 3) {
                hvexpSeleccionada.getCargo();
            } else if (cualCelda == 4) {
                hvexpSeleccionada.getJefeinmediato();
            } else if (cualCelda == 5) {
                hvexpSeleccionada.getTelefono();
            } else if (cualCelda == 6) {
                hvexpSeleccionada.getSectoreconomico().getDescripcion();
            } else if (cualCelda == 7) {
                hvexpSeleccionada.getMotivoretiro().getNombre();
            }
        }
    }

    public void asignarIndexExperiencia(HvExperienciasLaborales experienciaLaboral, int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            hvexpSeleccionada = experienciaLaboral;
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        if (dlg == 1) {
//            contarRegistroSector();
            RequestContext.getCurrentInstance().update("formularioDialogos:SectorDialogo");
            RequestContext.getCurrentInstance().execute("PF('SectorDialogo').show()");
        } else if (dlg == 2) {
//            contarRegistroMotivo();
            RequestContext.getCurrentInstance().update("formularioDialogos:MotivosDialogo");
            RequestContext.getCurrentInstance().execute("PF('MotivosDialogo').show()");
        }
    }

    public void modificarExperiencia(HvExperienciasLaborales experienciaLaboral) {
        hvexpSeleccionada = experienciaLaboral;
        if (tipoLista == 0) {
            if (hvexpSeleccionada.getFechadesde() != null) {
                fechaDesdeText = formatoFecha.format(hvexpSeleccionada.getFechadesde());
            } else {
                fechaDesdeText = "";
            }
        }
        if (tipoLista == 1) {
            int ind = listhvExpLaborales.indexOf(hvexpSeleccionada);
            int aux = ind;
            if (listhvExpLaborales.get(aux).getFechadesde() != null) {
                fechaDesdeText = formatoFecha.format(listhvExpLaborales.get(aux).getFechadesde());
            } else {
                fechaDesdeText = "";
            }
        }
        boolean retorno = validarFechasRegistro(0);
        if (retorno == true) {
//            if (validarCamposRegistro(0) == true) {
            if (tipoLista == 0) {
                if (!listhvExpLaboralesCrear.contains(hvexpSeleccionada)) {
                    if (listhvExpLaboralesModificar.isEmpty()) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    } else if (!listhvExpLaboralesModificar.contains(hvexpSeleccionada)) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }

                fechaIni = null;
                fechaFin = null;

            } else {
                int ind = listhvExpLaborales.indexOf(hvexpSeleccionada);
                if (!listhvExpLaboralesCrear.contains(hvexpSeleccionada)) {
                    if (listhvExpLaboralesModificar.isEmpty()) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    } else if (!listhvExpLaboralesModificar.contains(hvexpSeleccionada)) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }

                fechaIni = null;
                fechaFin = null;

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosExperiencia");

        } else {
            if (tipoLista == 0) {
            }
            if (tipoLista == 1) {
                int ind = listhvExpLaboralesCrear.indexOf(hvexpSeleccionada);
                listhvExpLaborales.get(ind).setFechadesde(fechaIni);
                listhvExpLaborales.get(ind).setFechahasta(fechaFin);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechasIngresoReg').show()");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            fechaIni = null;
            fechaFin = null;
        }
    }

    public boolean validarFechasRegistro(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (i == 0) {
            HvExperienciasLaborales auxiliar = null;
            if (tipoLista == 0) {
                auxiliar = hvexpSeleccionada;
            }
            if (tipoLista == 1) {
                auxiliar = hvexpSeleccionada;
            }
            if (auxiliar.getFechahasta() != null) {
                if (auxiliar.getFechadesde().after(fechaParametro) && auxiliar.getFechadesde().before(auxiliar.getFechahasta())) {
                    retorno = true;
                } else {
                    retorno = false;
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            }
            if (auxiliar.getFechahasta() == null) {
                if (auxiliar.getFechadesde().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (i == 1) {
            if (nuevahvexp.getFechahasta() != null) {
                if (nuevahvexp.getFechadesde().after(fechaParametro) && nuevahvexp.getFechadesde().before(nuevahvexp.getFechahasta())) {
                    retorno = true;
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                    retorno = false;
                }
            } else if (nuevahvexp.getFechadesde().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarhvexp.getFechahasta() != null) {
                if (duplicarhvexp.getFechadesde().after(fechaParametro) && duplicarhvexp.getFechadesde().before(duplicarhvexp.getFechahasta())) {
                    retorno = true;
                } else {
                    retorno = false;
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            } else if (duplicarhvexp.getFechadesde().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificarFechasExperiencia(HvExperienciasLaborales explaboral, int c) {
        HvExperienciasLaborales auxiliar = null;
        if (tipoLista == 0) {
            auxiliar = hvexpSeleccionada;
        }
        if (tipoLista == 1) {
            auxiliar = hvexpSeleccionada;
        }
        if (auxiliar.getFechadesde() != null) {
            boolean retorno = false;
            if (auxiliar.getFechahasta() == null) {
                retorno = true;
            }
            if (auxiliar.getFechahasta() != null) {
                hvexpSeleccionada = explaboral;
                retorno = validarFechasRegistro(0);
            }
            if (retorno == true) {
                cambiarIndiceExperiencia(explaboral, c);
                modificarExperiencia(explaboral);
            } else {
                if (tipoLista == 0) {
                    hvexpSeleccionada.setFechahasta(fechaFin);
                    hvexpSeleccionada.setFechadesde(fechaIni);
                }
                if (tipoLista == 1) {
                    hvexpSeleccionada.setFechahasta(fechaFin);
                    hvexpSeleccionada.setFechadesde(fechaIni);

                }
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosExperiencia");
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
        } else {
            if (tipoLista == 0) {
                hvexpSeleccionada.setFechadesde(fechaIni);
            }
            if (tipoLista == 1) {
                hvexpSeleccionada.setFechadesde(fechaIni);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    public void modificarExperiencia(HvExperienciasLaborales experienciaLaboral, String confirmarCambio, String valorConfirmar) {
        hvexpSeleccionada = experienciaLaboral;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("SECTORES")) {
            if (tipoLista == 0) {
                hvexpSeleccionada.getSectoreconomico().setDescripcion(hvexpSeleccionada.getSectoreconomico().getDescripcion());
            } else {
                hvexpSeleccionada.getSectoreconomico().setDescripcion(hvexpSeleccionada.getSectoreconomico().getDescripcion());
            }
            if (lovSectoresEconomicos != null) {
                for (int i = 0; i < lovSectoresEconomicos.size(); i++) {
                    if (lovSectoresEconomicos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    hvexpSeleccionada.setSectoreconomico(lovSectoresEconomicos.get(indiceUnicoElemento));
                } else {
                    hvexpSeleccionada.setSectoreconomico(lovSectoresEconomicos.get(indiceUnicoElemento));
                }
                lovSectoresEconomicos = null;
                getLovSectoresEconomicos();

            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:SectorDialogo");
                RequestContext.getCurrentInstance().execute("PF('SectorDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("MOTIVOS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    hvexpSeleccionada.getMotivoretiro().setNombre(hvexpSeleccionada.getMotivoretiro().getNombre());
                } else {
                    hvexpSeleccionada.getMotivoretiro().setNombre(hvexpSeleccionada.getMotivoretiro().getNombre());
                }
                if (lovMotivos != null) {
                    for (int i = 0; i < lovMotivos.size(); i++) {
                        if (lovMotivos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        hvexpSeleccionada.setMotivoretiro(lovMotivos.get(indiceUnicoElemento));
                    } else {
                        hvexpSeleccionada.setMotivoretiro(lovMotivos.get(indiceUnicoElemento));
                    }
                    lovMotivos = null;
                    getLovMotivos();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formularioDialogos:MotivosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivosDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                lovMotivos = null;
                getLovMotivos();
                if (tipoLista == 0) {
                    hvexpSeleccionada.setMotivoretiro(new MotivosRetiros());
                } else {
                    hvexpSeleccionada.setMotivoretiro(new MotivosRetiros());
                }
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listhvExpLaboralesCrear.contains(hvexpSeleccionada)) {

                    if (listhvExpLaboralesModificar.isEmpty()) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    } else if (!listhvExpLaboralesModificar.contains(hvexpSeleccionada)) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }

            } else if (!listhvExpLaboralesCrear.contains(hvexpSeleccionada)) {

                if (listhvExpLaboralesModificar.isEmpty()) {
                    listhvExpLaboralesModificar.add(hvexpSeleccionada);
                } else if (!listhvExpLaboralesModificar.contains(hvexpSeleccionada)) {
                    listhvExpLaboralesModificar.add(hvexpSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }

        }
        RequestContext.getCurrentInstance().update("form:datosExperiencia");
    }

    public void contarRegistrosExp() {
        RequestContext.getCurrentInstance().update("form:infoRegistroExp");
    }

    public void eventoFiltrarExp() {
        contarRegistrosExp();
    }

    //////
    public void modificarVigenciaDomiciliaria(VigenciasDomiciliarias vigenciaD) {
        vigenciasDomiciliariaSeleccionada = vigenciaD;
        if (tipoLista == 0) {
            if (!listVigenciasDomiciliariasCrear.contains(vigenciasDomiciliariaSeleccionada)) {
                if (listVigenciasDomiciliariasModificar.isEmpty()) {
                    listVigenciasDomiciliariasModificar.add(vigenciasDomiciliariaSeleccionada);
                } else if (!listVigenciasDomiciliariasModificar.contains(vigenciasDomiciliariaSeleccionada)) {
                    listVigenciasDomiciliariasModificar.add(vigenciasDomiciliariaSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else if (!listVigenciasDomiciliariasCrear.contains(vigenciasDomiciliariaSeleccionada)) {
            if (listVigenciasDomiciliariasModificar.isEmpty()) {
                listVigenciasDomiciliariasModificar.add(vigenciasDomiciliariaSeleccionada);
            } else if (!listVigenciasDomiciliariasModificar.contains(vigenciasDomiciliariaSeleccionada)) {
                listVigenciasDomiciliariasModificar.add(vigenciasDomiciliariaSeleccionada);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void seleccionarCalificacionFamiliar(String calificacion, VigenciasDomiciliarias vigDom) {
        if (calificacion.equals("BUENA")) {
            direccionSeleccionada.setTipoppal("BUENA");
        } else if (calificacion.equals("REGULAR")) {
            direccionSeleccionada.setTipoppal("REGULAR");
        } else if (calificacion.equals("MALA")) {
            direccionSeleccionada.setTipoppal("MALA");
        }
        if (!listVigenciasDomiciliariasCrear.contains(vigenciasDomiciliariaSeleccionada)) {
            if (listVigenciasDomiciliariasModificar.isEmpty()) {
                listVigenciasDomiciliariasModificar.add(vigenciasDomiciliariaSeleccionada);
            } else if (!listVigenciasDomiciliariasModificar.contains(vigenciasDomiciliariaSeleccionada)) {
                listVigenciasDomiciliariasModificar.add(vigenciasDomiciliariaSeleccionada);
            }
        }

        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosRelaciones");
    }

    public void cambiarIndiceCalificacionFamiliar(VigenciasDomiciliarias vigDom, int celda) {
        cualCelda = celda;
        if (cualCelda == 2) {
            vigenciasDomiciliariaSeleccionada.getObservacionfamiliar();
        }
    }

    public void cambiarIndiceDatosSector(VigenciasDomiciliarias vigDom, int celda) {
        cualCelda = celda;
        if (cualCelda == 1) {
            vigenciasDomiciliariaSeleccionada.getCondicionesgenerales();
        }
    }

    //
    /////SETS Y GETS///////
    public List<VigenciasDomiciliarias> getListVigenciasDomiciliarias() {
        if (listVigenciasDomiciliarias == null) {
            listVigenciasDomiciliarias = administrarVigDomiciliarias.vigenciasDomiciliariasporPersona(persona.getSecuencia());
        }
        return listVigenciasDomiciliarias;
    }

    public void setListVigenciasDomiciliarias(List<VigenciasDomiciliarias> listVigenciasDomiciliarias) {
        this.listVigenciasDomiciliarias = listVigenciasDomiciliarias;
    }

    public List<VigenciasDomiciliarias> getListVigenciasDomiciliariasFiltrar() {
        return listVigenciasDomiciliariasFiltrar;
    }

    public void setListVigenciasDomiciliariasFiltrar(List<VigenciasDomiciliarias> listVigenciasDomiciliariasFiltrar) {
        this.listVigenciasDomiciliariasFiltrar = listVigenciasDomiciliariasFiltrar;
    }

    public VigenciasDomiciliarias getVigenciasDomiciliariaSeleccionada() {
        return vigenciasDomiciliariaSeleccionada;
    }

    public void setVigenciasDomiciliariaSeleccionada(VigenciasDomiciliarias vigenciasDomiciliariaSeleccionada) {
        this.vigenciasDomiciliariaSeleccionada = vigenciasDomiciliariaSeleccionada;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public VigenciasDomiciliarias getNuevaVigenciaDomiciliaria() {
        return nuevaVigenciaDomiciliaria;
    }

    public void setNuevaVigenciaDomiciliaria(VigenciasDomiciliarias nuevaVigenciaDomiciliaria) {
        this.nuevaVigenciaDomiciliaria = nuevaVigenciaDomiciliaria;
    }

    public VigenciasDomiciliarias getDuplicarVigenciaDomiciliaria() {
        return duplicarVigenciaDomiciliaria;
    }

    public void setDuplicarVigenciaDomiciliaria(VigenciasDomiciliarias duplicarVigenciaDomiciliaria) {
        this.duplicarVigenciaDomiciliaria = duplicarVigenciaDomiciliaria;
    }

    public VigenciasDomiciliarias getEditarVigenciaDomiciliaria() {
        return editarVigenciaDomiciliaria;
    }

    public void setEditarVigenciaDomiciliaria(VigenciasDomiciliarias editarVigenciaDomiciliaria) {
        this.editarVigenciaDomiciliaria = editarVigenciaDomiciliaria;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public VigenciasFormales getNuevaVigenciaFormal() {
        return nuevaVigenciaFormal;
    }

    public void setNuevaVigenciaFormal(VigenciasFormales nuevaVigenciaFormal) {
        this.nuevaVigenciaFormal = nuevaVigenciaFormal;
    }

    public HVHojasDeVida getNuevaHV() {
        return nuevaHV;
    }

    public void setNuevaHV(HVHojasDeVida nuevaHV) {
        this.nuevaHV = nuevaHV;
    }

    public Personas getNuevaPersona() {
        return nuevaPersona;
    }

    public void setNuevaPersona(Personas nuevaPersona) {
        this.nuevaPersona = nuevaPersona;
    }

    public Personas getPersonaSeleccionada() {
        return personaSeleccionada;
    }

    public void setPersonaSeleccionada(Personas personaSeleccionada) {
        this.personaSeleccionada = personaSeleccionada;
    }

    public HvExperienciasLaborales getNuevahvexp() {
        return nuevahvexp;
    }

    public void setNuevahvexp(HvExperienciasLaborales nuevahvexp) {
        this.nuevahvexp = nuevahvexp;
    }

    public Direcciones getNuevaDireccion() {
        return nuevaDireccion;
    }

    public void setNuevaDireccion(Direcciones nuevaDireccion) {
        this.nuevaDireccion = nuevaDireccion;
    }

    public Telefonos getNuevoTelefono() {
        return nuevoTelefono;
    }

    public void setNuevoTelefono(Telefonos nuevoTelefono) {
        this.nuevoTelefono = nuevoTelefono;
    }

    public Familiares getNuevoFamiliar() {
        return nuevoFamiliar;
    }

    public List<VigenciasFormales> getListVigenciasFormales() {
        if (listVigenciasFormales == null) {
            if (persona != null) {
                listVigenciasFormales = administrarVigDomiciliarias.vigenciasFormalesPersona(persona.getSecuencia());
            }
        }
        return listVigenciasFormales;
    }

    public SoAntecedentesMedicos getNuevoAntecedentem() {
        return nuevoAntecedentem;
    }

    public Profesiones getProfesionSeleccionada() {
        return profesionSeleccionada;
    }

    public List<HVHojasDeVida> getListHojasdeVida() {
        return listHojasdeVida;
    }

    public List<Personas> getListPersonas() {
        return listPersonas;
    }

    public List<HvExperienciasLaborales> getListhvExpLaborales() {
        try {
            if (listhvExpLaborales == null) {
                if (hojaVida.getSecuencia() != null) {
                    listhvExpLaborales = administrarVigDomiciliarias.experienciasLaboralesEmpleado(hojaVida.getSecuencia());
                    if (listhvExpLaborales != null) {
                        for (int i = 0; i < listhvExpLaborales.size(); i++) {
                            if (listhvExpLaborales.get(i).getSectoreconomico() == null) {
                                listhvExpLaborales.get(i).setSectoreconomico(new SectoresEconomicos());
                            }
                            if (listhvExpLaborales.get(i).getMotivoretiro() == null) {
                                listhvExpLaborales.get(i).setMotivoretiro(new MotivosRetiros());
                            }
                        }
                    }
                }
            }
            return listhvExpLaborales;
        } catch (Exception e) {
            System.out.println("Error en getListProyectos : " + e.toString());
            return null;
        }
    }

    public List<Direcciones> getListDirecciones() {
        if (listDirecciones == null) {
            if (persona.getSecuencia() != null) {
                listDirecciones = administrarVigDomiciliarias.consultarDireccionesPersona(persona.getSecuencia());
            }
        }
        return listDirecciones;
    }

    public List<Telefonos> getListTelefonos() {
        if (listTelefonos == null) {
            if (persona.getSecuencia() != null) {
                listTelefonos = administrarVigDomiciliarias.telefonosPersona(persona.getSecuencia());
            }
        }
        return listTelefonos;
    }

    public List<VigenciasEstadosCiviles> getListVigenciaEstadoCivil() {
        if (listVigenciaEstadoCivil == null) {
            listVigenciaEstadoCivil = administrarVigDomiciliarias.estadosCivilesPersona(persona.getSecuencia());
        }
        return listVigenciaEstadoCivil;
    }

    public List<Familiares> getListaFamiliares() {
        if (listaFamiliares == null) {
            if (persona != null) {
                listaFamiliares = administrarVigDomiciliarias.buscarFamiliares(persona.getSecuencia());
            }
        }
        return listaFamiliares;
    }

    public List<SoAntecedentesMedicos> getListAntecedentesM() {
        return listAntecedentesM;
    }

    public List<Ciudades> getLovCiudades() {
        return lovCiudades;
    }

    public List<Cargos> getLovCargos() {
        return lovCargos;
    }

    public List<TiposTelefonos> getLovTiposTelefonos() {
        if (lovTiposTelefonos == null) {
            lovTiposTelefonos = administrarVigDomiciliarias.lovTiposTelefonos();
        }
        return lovTiposTelefonos;
    }

    public List<TiposEducaciones> getLovTiposEducaciones() {
        return lovTiposEducaciones;
    }

    public List<Profesiones> getLovProfesiones() {
        return lovProfesiones;
    }

    public List<AdiestramientosF> getLovAdiestramientos() {
        return lovAdiestramientos;
    }

    public List<Instituciones> getLovInstituciones() {
        return lovInstituciones;
    }

    public List<MotivosRetiros> getLovMotivos() {
        return lovMotivos;
    }

    public List<Personas> getLovPersonas() {
        return lovPersonas;
    }

    public List<SoTiposAntecedentes> getLovTiposAntecedentes() {
        return lovTiposAntecedentes;
    }

    public List<EstadosCiviles> getLovEstadosCiviles() {
        return lovEstadosCiviles;
    }

    public void setLovMotivos(List<MotivosRetiros> lovMotivos) {
        this.lovMotivos = lovMotivos;
    }

    public void setLovPersonas(List<Personas> lovPersonas) {
        this.lovPersonas = lovPersonas;
    }

    public void setLovTiposAntecedentes(List<SoTiposAntecedentes> lovTiposAntecedentes) {
        this.lovTiposAntecedentes = lovTiposAntecedentes;
    }

    public void setLovEstadosCiviles(List<EstadosCiviles> lovEstadosCiviles) {
        this.lovEstadosCiviles = lovEstadosCiviles;
    }

    public void setLovTiposTelefonos(List<TiposTelefonos> lovTiposTelefonos) {
        this.lovTiposTelefonos = lovTiposTelefonos;
    }

    public void setLovCiudades(List<Ciudades> lovCiudades) {
        this.lovCiudades = lovCiudades;
    }

    public void setLovCargos(List<Cargos> lovCargos) {
        this.lovCargos = lovCargos;
    }

    public void setLovTiposEducaciones(List<TiposEducaciones> lovTiposEducaciones) {
        this.lovTiposEducaciones = lovTiposEducaciones;
    }

    public void setLovProfesiones(List<Profesiones> lovProfesiones) {
        this.lovProfesiones = lovProfesiones;
    }

    public void setLovAdiestramientos(List<AdiestramientosF> lovAdiestramientos) {
        this.lovAdiestramientos = lovAdiestramientos;
    }

    public void setLovInstituciones(List<Instituciones> lovInstituciones) {
        this.lovInstituciones = lovInstituciones;
    }

    public void setListHojasdeVida(List<HVHojasDeVida> listHojasdeVida) {
        this.listHojasdeVida = listHojasdeVida;
    }

    public void setListPersonas(List<Personas> listPersonas) {
        this.listPersonas = listPersonas;
    }

    public void setListVigenciasFormales(List<VigenciasFormales> listVigenciasFormales) {
        this.listVigenciasFormales = listVigenciasFormales;
    }

    public void setListhvExpLaborales(List<HvExperienciasLaborales> listhvExpLaborales) {
        this.listhvExpLaborales = listhvExpLaborales;
    }

    public void setListDirecciones(List<Direcciones> listDirecciones) {
        this.listDirecciones = listDirecciones;
    }

    public void setListTelefonos(List<Telefonos> listTelefonos) {
        this.listTelefonos = listTelefonos;
    }

    public void setListVigenciaEstadoCivil(List<VigenciasEstadosCiviles> listVigenciaEstadoCivil) {
        this.listVigenciaEstadoCivil = listVigenciaEstadoCivil;
    }

    public void setListaFamiliares(List<Familiares> listaFamiliares) {
        this.listaFamiliares = listaFamiliares;
    }

    public void setListAntecedentesM(List<SoAntecedentesMedicos> listAntecedentesM) {
        this.listAntecedentesM = listAntecedentesM;
    }

    public void setNuevoFamiliar(Familiares nuevoFamiliar) {
        this.nuevoFamiliar = nuevoFamiliar;
    }

    public void setNuevoAntecedentem(SoAntecedentesMedicos nuevoAntecedentem) {
        this.nuevoAntecedentem = nuevoAntecedentem;
    }

    public void setProfesionSeleccionada(Profesiones profesionSeleccionada) {
        this.profesionSeleccionada = profesionSeleccionada;
    }

    public Direcciones getDireccionActual() {
        direccionActual = administrarVigDomiciliarias.direccionesPersona(persona.getSecuencia());
        return direccionActual;
    }

    public void setDireccionActual(Direcciones direccionActual) {
        this.direccionActual = direccionActual;
    }

    public Telefonos getTelefonoActual() {
        telefonoActual = administrarVigDomiciliarias.telefonoActualPersona(persona.getSecuencia());
        return telefonoActual;
    }

    public void setTelefonoActual(Telefonos telefonoActual) {
        this.telefonoActual = telefonoActual;
    }

    public VigenciasEstadosCiviles getEstadoCivilActual() {
        estadoCivilActual = administrarVigDomiciliarias.estadoCivilActualPersona(persona.getSecuencia());
        return estadoCivilActual;
    }

    public void setEstadoCivilActual(VigenciasEstadosCiviles estadoCivilActual) {
        this.estadoCivilActual = estadoCivilActual;
    }

    public HVHojasDeVida getHvactual() {
        return hvactual;
    }

    public void setHvactual(HVHojasDeVida hvactual) {
        this.hvactual = hvactual;
    }

    public SoAntecedentesMedicos getAntecedentemSeleccionado() {
        return antecedentemSeleccionado;
    }

    public void setAntecedentemSeleccionado(SoAntecedentesMedicos antecedentemSeleccionado) {
        this.antecedentemSeleccionado = antecedentemSeleccionado;
    }

    public List<TiposFamiliares> getLovTiposFamiliares() {
        return lovTiposFamiliares;
    }

    public void setLovTiposFamiliares(List<TiposFamiliares> lovTiposFamiliares) {
        this.lovTiposFamiliares = lovTiposFamiliares;
    }

    public String getInfoRegistroFamiliar() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosFamiliares");
        infoRegistroFamiliar = String.valueOf(tabla.getRowCount());
        return infoRegistroFamiliar;
    }

    public void setInfoRegistroFamiliar(String infoRegistroFamiliar) {
        this.infoRegistroFamiliar = infoRegistroFamiliar;
    }

    public String getInfoRegistroPersonas() {
        return infoRegistroPersonas;
    }

    public void setInfoRegistroPersonas(String infoRegistroPersonas) {
        this.infoRegistroPersonas = infoRegistroPersonas;
    }

    public String getInfoRegistroTipoFamiliar() {
        return infoRegistroTipoFamiliar;
    }

    public void setInfoRegistroTipoFamiliar(String infoRegistroTipoFamiliar) {
        this.infoRegistroTipoFamiliar = infoRegistroTipoFamiliar;
    }

    public String getInfoRegistroTipoDocumento() {
        return infoRegistroTipoDocumento;
    }

    public void setInfoRegistroTipoDocumento(String infoRegistroTipoDocumento) {
        this.infoRegistroTipoDocumento = infoRegistroTipoDocumento;
    }

    public String getInfoRegistroCiudades() {
        return infoRegistroCiudades;
    }

    public void setInfoRegistroCiudades(String infoRegistroCiudades) {
        this.infoRegistroCiudades = infoRegistroCiudades;
    }

    public String getInfoRegistroCiudadNacimiento() {
        return infoRegistroCiudadNacimiento;
    }

    public void setInfoRegistroCiudadNacimiento(String infoRegistroCiudadNacimiento) {
        this.infoRegistroCiudadNacimiento = infoRegistroCiudadNacimiento;
    }

    public List<Familiares> getListaFamiliaresFiltrar() {
        return listaFamiliaresFiltrar;
    }

    public void setListaFamiliaresFiltrar(List<Familiares> listaFamiliaresFiltrar) {
        this.listaFamiliaresFiltrar = listaFamiliaresFiltrar;
    }

    public Familiares getFamiliarSeleccionado() {
        return familiarSeleccionado;
    }

    public void setFamiliarSeleccionado(Familiares familiarSeleccionado) {
        this.familiarSeleccionado = familiarSeleccionado;
    }

    public List<VigenciasFormales> getListVigenciasFormalesFiltrar() {
        return listVigenciasFormalesFiltrar;
    }

    public void setListVigenciasFormalesFiltrar(List<VigenciasFormales> listVigenciasFormalesFiltrar) {
        this.listVigenciasFormalesFiltrar = listVigenciasFormalesFiltrar;
    }

    public List<HVHojasDeVida> getListHojasdeVidaFiltrar() {
        return listHojasdeVidaFiltrar;
    }

    public void setListHojasdeVidaFiltrar(List<HVHojasDeVida> listHojasdeVidaFiltrar) {
        this.listHojasdeVidaFiltrar = listHojasdeVidaFiltrar;
    }

    public List<Personas> getListPersonasFiltrar() {
        return listPersonasFiltrar;
    }

    public void setListPersonasFiltrar(List<Personas> listPersonasFiltrar) {
        this.listPersonasFiltrar = listPersonasFiltrar;
    }

    public List<HvExperienciasLaborales> getListhvExpLaboralesFiltrar() {
        return listhvExpLaboralesFiltrar;
    }

    public void setListhvExpLaboralesFiltrar(List<HvExperienciasLaborales> listhvExpLaboralesFiltrar) {
        this.listhvExpLaboralesFiltrar = listhvExpLaboralesFiltrar;
    }

    public List<Direcciones> getListDireccionesFiltrar() {
        return listDireccionesFiltrar;
    }

    public void setListDireccionesFiltrar(List<Direcciones> listDireccionesFiltrar) {
        this.listDireccionesFiltrar = listDireccionesFiltrar;
    }

    public List<Telefonos> getListTelefonosFiltrar() {
        return listTelefonosFiltrar;
    }

    public void setListTelefonosFiltrar(List<Telefonos> listTelefonosFiltrar) {
        this.listTelefonosFiltrar = listTelefonosFiltrar;
    }

    public List<VigenciasEstadosCiviles> getListVigenciaEstadoCivilFiltrar() {
        return listVigenciaEstadoCivilFiltrar;
    }

    public void setListVigenciaEstadoCivilFiltrar(List<VigenciasEstadosCiviles> listVigenciaEstadoCivilFiltrar) {
        this.listVigenciaEstadoCivilFiltrar = listVigenciaEstadoCivilFiltrar;
    }

    public List<SoAntecedentesMedicos> getListAntecedentesMFiltrar() {
        return listAntecedentesMFiltrar;
    }

    public void setListAntecedentesMFiltrar(List<SoAntecedentesMedicos> listAntecedentesMFiltrar) {
        this.listAntecedentesMFiltrar = listAntecedentesMFiltrar;
    }

    public List<TiposEducaciones> getLovTiposEducacionesFiltrar() {
        return lovTiposEducacionesFiltrar;
    }

    public void setLovTiposEducacionesFiltrar(List<TiposEducaciones> lovTiposEducacionesFiltrar) {
        this.lovTiposEducacionesFiltrar = lovTiposEducacionesFiltrar;
    }

    public List<Instituciones> getLovInstitucionesFiltrar() {
        return lovInstitucionesFiltrar;
    }

    public void setLovInstitucionesFiltrar(List<Instituciones> lovInstitucionesFiltrar) {
        this.lovInstitucionesFiltrar = lovInstitucionesFiltrar;
    }

    public List<MotivosRetiros> getLovMotivosFiltrar() {
        return lovMotivosFiltrar;
    }

    public void setLovMotivosFiltrar(List<MotivosRetiros> lovMotivosFiltrar) {
        this.lovMotivosFiltrar = lovMotivosFiltrar;
    }

    public List<TiposFamiliares> getLovTiposFamiliaresFiltrar() {
        return lovTiposFamiliaresFiltrar;
    }

    public void setLovTiposFamiliaresFiltrar(List<TiposFamiliares> lovTiposFamiliaresFiltrar) {
        this.lovTiposFamiliaresFiltrar = lovTiposFamiliaresFiltrar;
    }

    public List<Personas> getLovPersonasFiltrar() {
        return lovPersonasFiltrar;
    }

    public void setLovPersonasFiltrar(List<Personas> lovPersonasFiltrar) {
        this.lovPersonasFiltrar = lovPersonasFiltrar;
    }

    public List<SoAntecedentes> getLovAntecedentesFiltrar() {
        return lovAntecedentesFiltrar;
    }

    public void setLovAntecedentesFiltrar(List<SoAntecedentes> lovAntecedentesFiltrar) {
        this.lovAntecedentesFiltrar = lovAntecedentesFiltrar;
    }

    public List<SoTiposAntecedentes> getLovTiposAntecedentesFiltrar() {
        return lovTiposAntecedentesFiltrar;
    }

    public void setLovTiposAntecedentesFiltrar(List<SoTiposAntecedentes> lovTiposAntecedentesFiltrar) {
        this.lovTiposAntecedentesFiltrar = lovTiposAntecedentesFiltrar;
    }

    public List<EstadosCiviles> getLovEstadosCivilesFiltrar() {
        return lovEstadosCivilesFiltrar;
    }

    public void setLovEstadosCivilesFiltrar(List<EstadosCiviles> lovEstadosCivilesFiltrar) {
        this.lovEstadosCivilesFiltrar = lovEstadosCivilesFiltrar;
    }

    public List<Ciudades> getLovCiudadesFiltrar() {
        return lovCiudadesFiltrar;
    }

    public void setLovCiudadesFiltrar(List<Ciudades> lovCiudadesFiltrar) {
        this.lovCiudadesFiltrar = lovCiudadesFiltrar;
    }

    public List<Cargos> getLovCargosFiltrar() {
        return lovCargosFiltrar;
    }

    public void setLovCargosFiltrar(List<Cargos> lovCargosFiltrar) {
        this.lovCargosFiltrar = lovCargosFiltrar;
    }

    public String getInfoRegistroAntecedenteM() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosAntecedentes");
        infoRegistroAntecedenteM = String.valueOf(tabla.getRowCount());
        return infoRegistroAntecedenteM;
    }

    public void setInfoRegistroAntecedenteM(String infoRegistroAntecedenteM) {
        this.infoRegistroAntecedenteM = infoRegistroAntecedenteM;
    }

    public Telefonos getTelefonoSeleccionado() {
        return telefonoSeleccionado;
    }

    public void setTelefonoSeleccionado(Telefonos telefonoSeleccionado) {
        this.telefonoSeleccionado = telefonoSeleccionado;
    }

    public Direcciones getDireccionSeleccionada() {
        return direccionSeleccionada;
    }

    public void setDireccionSeleccionada(Direcciones direccionSeleccionada) {
        this.direccionSeleccionada = direccionSeleccionada;
    }

    public VigenciasEstadosCiviles getVigenciaEstadoCivilSeleccionado() {
        return vigenciaEstadoCivilSeleccionado;
    }

    public void setVigenciaEstadoCivilSeleccionado(VigenciasEstadosCiviles vigenciaEstadoCivilSeleccionado) {
        this.vigenciaEstadoCivilSeleccionado = vigenciaEstadoCivilSeleccionado;
    }

    public VigenciasFormales getVigenciaFormalSeleccionada() {
        return vigenciaFormalSeleccionada;
    }

    public void setVigenciaFormalSeleccionada(VigenciasFormales vigenciaFormalSeleccionada) {
        this.vigenciaFormalSeleccionada = vigenciaFormalSeleccionada;
    }

    public String getFechaDesdeText() {
        return fechaDesdeText;
    }

    public void setFechaDesdeText(String fechaDesdeText) {
        this.fechaDesdeText = fechaDesdeText;
    }

    public String getFechaHastaText() {
        if (hvexpSeleccionada != null) {
            if (hvexpSeleccionada.getFechadesde() != null) {
                fechaDesdeText = formatoFecha.format(hvexpSeleccionada.getFechadesde());
            } else {
                fechaDesdeText = "";
            }
        } else {
            fechaDesdeText = "";
        }
        return fechaDesdeText;
    }

    public void setFechaHastaText(String fechaHastaText) {
        this.fechaHastaText = fechaHastaText;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<SectoresEconomicos> getLovSectoresEconomicos() {
        return lovSectoresEconomicos;
    }

    public void setLovSectoresEconomicos(List<SectoresEconomicos> lovSectoresEconomicos) {
        this.lovSectoresEconomicos = lovSectoresEconomicos;
    }

    public SectoresEconomicos getSectorSeleccionado() {
        return sectorSeleccionado;
    }

    public void setSectorSeleccionado(SectoresEconomicos sectorSeleccionado) {
        this.sectorSeleccionado = sectorSeleccionado;
    }

    public List<SectoresEconomicos> getFiltrarLovSectoresEconomicos() {
        return filtrarLovSectoresEconomicos;
    }

    public void setFiltrarLovSectoresEconomicos(List<SectoresEconomicos> filtrarLovSectoresEconomicos) {
        this.filtrarLovSectoresEconomicos = filtrarLovSectoresEconomicos;
    }

    public HvExperienciasLaborales getHvexpSeleccionada() {
        return hvexpSeleccionada;
    }

    public void setHvexpSeleccionada(HvExperienciasLaborales hvexpSeleccionada) {
        this.hvexpSeleccionada = hvexpSeleccionada;
    }

    public String getInfoRegistroEstadoCivil() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEstadoCivil");
        infoRegistroEstadoCivil = String.valueOf(tabla.getRowCount());
        return infoRegistroEstadoCivil;
    }

    public void setInfoRegistroEstadoCivil(String infoRegistroEstadoCivil) {
        this.infoRegistroEstadoCivil = infoRegistroEstadoCivil;
    }

    public String getInfoRegistroDireccion() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTelefonosPersona");
        infoRegistroDireccion = String.valueOf(tabla.getRowCount());
        return infoRegistroDireccion;
    }

    public void setInfoRegistroDireccion(String infoRegistroDireccion) {
        this.infoRegistroDireccion = infoRegistroDireccion;
    }

    public String getInfoRegistroEducacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona");
        infoRegistroEducacion = String.valueOf(tabla.getRowCount());
        return infoRegistroEducacion;
    }

    public void setInfoRegistroEducacion(String infoRegistroEducacion) {
        this.infoRegistroEducacion = infoRegistroEducacion;
    }

    public String getInfoRegistroExp() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosExperiencia");
        infoRegistroExp = String.valueOf(tabla.getRowCount());
        return infoRegistroExp;
    }

    public void setInfoRegistroExp(String infoRegistroExp) {
        this.infoRegistroExp = infoRegistroExp;
    }

    public String getInfoRegistroTelefono() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTelefonosPersona");
        infoRegistroTelefono = String.valueOf(tabla.getRowCount());
        return infoRegistroTelefono;
    }

    public void setInfoRegistroTelefono(String infoRegistroTelefono) {
        this.infoRegistroTelefono = infoRegistroTelefono;
    }

    public String getDistribucion() {
//          descGeneral = vigenciasDomiciliariaSeleccionada.getDistribucionvivienda();
//           RequestContext.getCurrentInstance().update("form:editarDistribucion");
        return distribucion;
    }

    public void setDistribucion(String distribucion) {
        this.distribucion = distribucion;
    }

    public String getCondiciones() {
//          descGeneral = vigenciasDomiciliariaSeleccionada.getCondicionesgenerales();
//           RequestContext.getCurrentInstance().update("form:editarCondiciones");
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public String getDescGeneral() {
//           descGeneral = vigenciasDomiciliariaSeleccionada.getDescripcionvivienda();
//           RequestContext.getCurrentInstance().update("form:editarDescripcionGeneral");
        return descGeneral;
    }

    public void setDescGeneral(String descGeneral) {
        this.descGeneral = descGeneral;
    }

    
    
    
}
