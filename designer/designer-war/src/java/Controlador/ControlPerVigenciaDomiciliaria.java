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
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasDomiciliariasInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
    private List<VigenciasDomiciliarias> lovVisitas;

    private VigenciasDomiciliarias vigenciasDomiciliariaSeleccionada;
    private VigenciasDomiciliarias nuevaVigenciaDomiciliaria;
    private VigenciasDomiciliarias duplicarVigenciaDomiciliaria;
    private VigenciasDomiciliarias editarVigenciaDomiciliaria;
    private VigenciasDomiciliarias visitaSeleccionada;
    private VigenciasDomiciliarias visitaLovSeleccionada;
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
    private TiposTelefonos tipoTelefonoSeleccionado;

    private List<Ciudades> lovCiudades;
    private List<Ciudades> lovCiudadesFiltrar;
    private Ciudades ciudadSeleccionada;

    private List<Ciudades> lovCiudadDocumento;
    private List<Ciudades> lovCiudadDocumentoFiltrar;
    private Ciudades ciudadDocumentoSeleccionada;

    private List<Ciudades> lovCiudadDireccion;
    private List<Ciudades> lovCiudadDireccionFiltrar;
    private Ciudades ciudadDireccionSeleccionada;

    private List<Ciudades> lovCiudadTelefono;
    private List<Ciudades> lovCiudadTelefonoFiltrar;
    private Ciudades ciudadTelefonoSeleccionada;

    private List<Cargos> lovCargos;
    private List<Cargos> lovCargosFiltrar;
    private Cargos cargoSeleccionada;

    private List<TiposDocumentos> lovTiposDocumentos;
    private List<TiposDocumentos> lovTiposDocumentosFiltrar;
    private TiposDocumentos tipoDocumentoSeleccionado;

    //SectoresEconomicos
    private List<SectoresEconomicos> lovSectoresEconomicos;
    private SectoresEconomicos sectorSeleccionado;
    private List<SectoresEconomicos> filtrarLovSectoresEconomicos;

//otros
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    private String altoTabla, tablaImprimir, nombreArchivo;
    private Personas persona;
    private Empleados empleado;
    private Telefonos telefonoActual;
    private HVHojasDeVida hvactual;
    private boolean guardado;
    private BigInteger l;
    private BigDecimal x;
    private int k;
    private int cualCelda, tipoLista;
    private DataTable tablaC;
    private boolean activarLov;
    private String infoRegistroFamiliar, infoRegistroPersonas, infoRegistroTipoFamiliar, infoRegistroTipoDocumento, infoRegistroCiudadDocumento, infoRegistroCiudadNacimiento;
    private String infoRegistroAntecedenteM, infoRegistroEstadoCivil, infoRegistroDireccion, infoRegistroEducacion, infoRegistroExp, infoRegistroTelefono, infoRegistroEstadoCivilLov;
    private String infoRegistroCiudadesDirecciones, infoRegistroTT, infoRegistroCiudadesTelefonos;
    private String infoRegistroMotivo, infoRegistroSector;
    private String fechaDesdeText, fechaHastaText;
    private Date fechaIni, fechaFin;
    private final SimpleDateFormat formatoFecha;
    private Date fechaParametro;
    private HVHojasDeVida hojaVida;
    private String distribucion, condiciones, descGeneral;
    private String observaciones, conceptofinal, conceptosocial, personasAtencion, profesional;
    private String mensajeValidacion, infoRegistroAntecedentes, infoRegistroTipoAntecedente;
    private String infoRegistroAdiestramiento, infoRegistroInstitucion, infoRegistroProfesion, infoRegistroTipoEducacion;
    private String infoRegistroCargo;
    private Date dateVisita;
    //Columnas Tabla Direcciones
    private Column dFecha, dUbicacionPrincipal, dDescripcionUbicacionPrincipal, dUbicacionSecundaria;
    private Column dDescripcionUbicacionSecundaria, dInterior, dCiudad, dTipoVivienda, dHipoteca, dDireccionAlternativa;
    ///columnas tabla telefono
    private Column tFecha, tTipoTelefono, tNumero, tCiudad;
    //columnas tabla estados civiles
    private Column fecha, parentesco;
    //columnas tabla antecedentes
    private Column fechaAntecedenteM, tipoAntecedenteM, antecedenteMedico, descAntecedenteM;
    //Columnas Tabla Vigencias Formales
    private Column pEFechas, pETiposEducaciones, pEProfesiones, pEInstituciones, pEAdiestramientosF, pECalificaciones;
    private Column pENumerosTarjetas, pEFechasExpediciones, pEFechasVencimientos, pEObservaciones;
    //columnas tabla exp laborales
    private Column expEmpresa, expCargoDes, expJefe, expTelefono, expSectorEco, expMotivos, expFechaInicio, expFechaRetiro;
    ///columnas tabla familiares 
    private Column nombre, ocupacion, columnaTipo, smedico, sfamiliar, beneficiario, upcadicional, valorupc;
    //columnas tablas vigencias domiciliarias
    private Column editarcalf, obsFamiliar, editarconstruccion, servicioAgua, servicioLuz, servicioTel, servicioPar, servicioTrasn;
    private Column servicioAlcan, servicioOtros, otrosServicios, ingresos, origenInd, origenArriendos, origenPension, origenSalario;
    private Column origenCDT, origenAuxilios, aportePadre, aporteMadre, aporteHermano, aporteAbuelo, aporteTio, aporteOtro, otrosIngresos;
    private Column egresoEdu, egresoRec, egresoAlimentacion, egresoMedico, egresoArriendo, egresoServicios, egresoOtro, otrosEgresos;
    private Column condicionesF, condicionesS, situacionE, nivelAc, motivacionC;
    private String infoRegistroRelaciones, infoRegistroServicios, infoRegistroIngresos, infoRegistroAportesH, infoRegistrosEgresos, infoRegistrosIndicadores, nombreDialogo;
    private int cualtabla;
    private boolean activarotroservicio;
    private boolean activarotroegreso;
    private boolean activarotroaporte;
    private String infoRegistrolovvisitas, infoRegistroVigenciaD;
    private List<String> lovAntecedentesDescripcion;

    /**
     * Creates a new instance of ControlPerVigenciaDomiciliaria
     */
    public ControlPerVigenciaDomiciliaria() {
        altoTabla = "70";
        nuevaPersona = new Personas();
        nuevahvexp = new HvExperienciasLaborales();
        nuevoTelefono = new Telefonos();
        nuevaVigenciaEstadoCivil = new VigenciasEstadosCiviles();
        nuevoFamiliar = new Familiares();
        permitirIndex = true;
        guardado = true;
        activarLov = true;
        cualCelda = -1;
        tipoLista = 0;
        persona = new Personas();
        telefonoActual = new Telefonos();
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
        observaciones = "";
        conceptofinal = "";
        conceptosocial = "";
        personasAtencion = "";
        profesional = "";
        dateVisita = new Date();
        nuevaDireccion = new Direcciones();
        nuevaDireccion.setCiudad(new Ciudades());
        nuevaDireccion.setFechavigencia(new Date());
        nuevaDireccion.setTipoppal("C");
        nuevaDireccion.setTiposecundario("K");
        nuevaDireccion.setTipovivienda("CASA");
        nuevaDireccion.setHipoteca("N");
        listDireccionesBorrar = new ArrayList<Direcciones>();
        listDireccionesCrear = new ArrayList<Direcciones>();
        listDireccionesModificar = new ArrayList<Direcciones>();
        lovCiudadDireccion = null;
        direccionSeleccionada = null;
        listTelefonosBorrar = new ArrayList<Telefonos>();
        listTelefonosCrear = new ArrayList<Telefonos>();
        listTelefonosModificar = new ArrayList<Telefonos>();
        lovTiposTelefonos = null;
        telefonoSeleccionado = null;
        lovCiudadTelefono = null;
        lovEstadosCiviles = null;
        listVigenciaEstadoCivilCrear = new ArrayList<VigenciasEstadosCiviles>();
        listVigenciaEstadoCivilBorrar = new ArrayList<VigenciasEstadosCiviles>();
        listVigenciaEstadoCivilModificar = new ArrayList<VigenciasEstadosCiviles>();
        lovAntecedentes = null;
        lovTiposAntecedentes = null;
        listAntecedentesMBorrar = new ArrayList<SoAntecedentesMedicos>();
        listAntecedentesMCrear = new ArrayList<SoAntecedentesMedicos>();
        listAntecedentesModificar = new ArrayList<SoAntecedentesMedicos>();
        lovTiposEducaciones = null;
        lovInstituciones = null;
        lovAdiestramientos = null;
        lovProfesiones = null;
        listVigenciasFormalesBorrar = new ArrayList<VigenciasFormales>();
        listVigenciasFormalesCrear = new ArrayList<VigenciasFormales>();
        listVigenciasFormalesModificar = new ArrayList<VigenciasFormales>();
        lovSectoresEconomicos = null;
        lovMotivos = null;
        listhvExpLaboralesBorrar = new ArrayList<HvExperienciasLaborales>();
        listhvExpLaboralesCrear = new ArrayList<HvExperienciasLaborales>();
        listhvExpLaboralesModificar = new ArrayList<HvExperienciasLaborales>();
        lovPersonas = null;
        lovTiposFamiliares = null;
        lovTiposDocumentos = null;
        listaFamiliaresCrear = new ArrayList<Familiares>();
        listaFamiliaresBorrar = new ArrayList<Familiares>();
        listaFamiliaresModificar = new ArrayList<Familiares>();
        nuevaVigenciaDomiciliaria = new VigenciasDomiciliarias();
        nuevaVigenciaDomiciliaria.setFecha(new Date());
        duplicarVigenciaDomiciliaria = new VigenciasDomiciliarias();
        lovCargos = null;
        listHojasdeVidaCrear = new ArrayList<HVHojasDeVida>();
        listHojasdeVidaBorrar = new ArrayList<HVHojasDeVida>();
        listHojasdeVidaModificar = new ArrayList<HVHojasDeVida>();
        nuevaHV = new HVHojasDeVida();
        nuevaHV.setCargo(new Cargos());
        cualtabla = -1;
        listVigenciasDomiciliariasCrear = new ArrayList<VigenciasDomiciliarias>();
        listVigenciasDomiciliariasBorrar = new ArrayList<VigenciasDomiciliarias>();
        listVigenciasDomiciliariasModificar = new ArrayList<VigenciasDomiciliarias>();
        activarotroservicio = true;
        activarotroegreso = true;
        activarotroaporte = true;
        nuevoAntecedentem = new SoAntecedentesMedicos();
        nuevoAntecedentem.setAntecedente(new SoAntecedentes());
        nuevoAntecedentem.setTipoantecedente(new SoTiposAntecedentes());
        nuevoAntecedentem.setAntecedenteDescripcion("");
        lovVisitas = null;
        duplicarDireccion = new Direcciones();
        lovAntecedentesDescripcion = new ArrayList<String>();
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
        System.out.println("secuencia del empleado en recibir empleado : " + secuencia);
        listVigenciasFormales = null;
        listDirecciones = null;
        listTelefonos = null;
        listVigenciaEstadoCivil = null;
        listaFamiliares = null;
        listhvExpLaborales = null;
        guardado = true;
        persona = administrarVigDomiciliarias.encontrarPersona(secuencia);
        empleado = administrarVigDomiciliarias.buscarEmpleado(persona.getSecuencia());
        if (persona != null) {
            hojaVida = administrarVigDomiciliarias.obtenerHojaVidaPersona(persona.getSecuencia());
        }

        if (hojaVida == null) {
            hojaVida = new HVHojasDeVida();
            hojaVida.setPersona(persona);
            hojaVida.setCargo(new Cargos());
        } else {
            getListhvExpLaborales();
        }

        if (administrarVigDomiciliarias.vigenciaDomiciliariaActual(persona.getSecuencia()) != null) {
            vigenciasDomiciliariaSeleccionada = administrarVigDomiciliarias.vigenciaDomiciliariaActual(persona.getSecuencia());
        } else {
            vigenciasDomiciliariaSeleccionada = new VigenciasDomiciliarias();
            vigenciasDomiciliariaSeleccionada.setPersona(persona);
            vigenciasDomiciliariaSeleccionada.setServicioagua("N");
            vigenciasDomiciliariaSeleccionada.setServicioalcantarillado("N");
            vigenciasDomiciliariaSeleccionada.setServicioaseo("N");
            vigenciasDomiciliariaSeleccionada.setServicioluz("N");
            vigenciasDomiciliariaSeleccionada.setServiciootros("N");
            vigenciasDomiciliariaSeleccionada.setServicioparabolica("N");
            vigenciasDomiciliariaSeleccionada.setServiciotelefono("N");
            vigenciasDomiciliariaSeleccionada.setServiciotransporte("N");
            vigenciasDomiciliariaSeleccionada.setInversionalimentacion("N");
            vigenciasDomiciliariaSeleccionada.setInversionarriendo("N");
            vigenciasDomiciliariaSeleccionada.setInversioneducacion("N");
            vigenciasDomiciliariaSeleccionada.setInversionmedica("N");
            vigenciasDomiciliariaSeleccionada.setInversionotros("N");
            vigenciasDomiciliariaSeleccionada.setInversionrecreacion("N");
            vigenciasDomiciliariaSeleccionada.setInversionservicios("N");
            vigenciasDomiciliariaSeleccionada.setIngresoabuelo("N");
            vigenciasDomiciliariaSeleccionada.setIngresohermano("N");
            vigenciasDomiciliariaSeleccionada.setIngresomama("N");
            vigenciasDomiciliariaSeleccionada.setIngresootro("N");
            vigenciasDomiciliariaSeleccionada.setIngresopapa("N");
            vigenciasDomiciliariaSeleccionada.setIngresotio("N");
        }

        listVigenciasDomiciliarias = null;
        getListVigenciasDomiciliarias();
        listAntecedentesM = null;
        getListAntecedentesM();
        if (listAntecedentesM != null) {
            if (!listAntecedentesM.isEmpty()) {
                antecedentemSeleccionado = listAntecedentesM.get(0);
                System.out.println("antecedente medico seleccionado en recibir empleado" + antecedentemSeleccionado.getSecuencia());
                System.out.println("tipo antecedente medico seleccionado en recibir empleado" + antecedentemSeleccionado.getTipoantecedente().getDescripcion());
            }
        }

        getLovAntecedentes();
        if (lovAntecedentes != null) {
            for (int i = 0; i < lovAntecedentes.size(); i++) {
                lovAntecedentesDescripcion.add(lovAntecedentes.get(i).getDescripcion());
            }
        }

    }

    /////FAMILIARES
    public void cambiarIndiceFamiliares(Familiares familiar, int celda) {
        if (permitirIndex == true) {
            cualCelda = celda;
            cualtabla = 5;
            familiarSeleccionado = familiar;
            if (cualCelda == 1) {
                familiarSeleccionado.getPersonafamiliar().getNombreCompleto();
                cualCelda = -1;
            } else if (cualCelda == 2) {
                familiarSeleccionado.getOcupacion();
                cualCelda = -1;
            } else if (cualCelda == 3) {
                familiarSeleccionado.getTipofamiliar().getTipo();
                cualCelda = -1;
            } else if (cualCelda == 4) {
                familiarSeleccionado.getServiciomedico();
                cualCelda = -1;
            } else if (cualCelda == 5) {
                familiarSeleccionado.getSubsidiofamiliar();
                cualCelda = -1;
            } else if (cualCelda == 6) {
                familiarSeleccionado.getBeneficiario();
                cualCelda = -1;
            } else if (cualCelda == 7) {
                familiarSeleccionado.getUpcadicional();
                cualCelda = -1;
            } else if (cualCelda == 8) {
                familiarSeleccionado.getValorupcadicional();
                cualCelda = -1;
            }
        }
//        cualtabla = -1;
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
        guardado = true;
    }

    public void cancelarModificacionFamiliares() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {

            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:nombreFamiliar");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            ocupacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:ocupacionFamiliar");
            ocupacion.setFilterStyle("display: none; visibility: hidden;");
            columnaTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:ctipoFamiliar");
            columnaTipo.setFilterStyle("display: none; visibility: hidden;");
            smedico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:servicioMedico");
            smedico.setFilterStyle("display: none; visibility: hidden;");
            sfamiliar = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:sFamiliar");
            sfamiliar.setFilterStyle("display: none; visibility: hidden;");
            beneficiario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:beneficiario");
            beneficiario.setFilterStyle("display: none; visibility: hidden;");
            upcadicional = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:upcAd");
            upcadicional.setFilterStyle("display: none; visibility: hidden;");
            valorupc = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:valorUpc");
            valorupc.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosFamiliares");
            bandera = 0;
            listaFamiliaresFiltrar = null;
            tipoLista = 0;
            altoTabla = "285";
        }

        listaFamiliaresBorrar.clear();
        listaFamiliaresCrear.clear();
        listaFamiliaresModificar.clear();
        k = 0;
        listaFamiliares = null;
        familiarSeleccionado = null;
        guardado = true;
        permitirIndex = true;
        getListaFamiliares();
        contarRegistrosFamiliares();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosFamiliares");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void agregarNuevoFamilar() {
        if (bandera == 1) {
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:nombreFamiliar");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            ocupacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:ocupacionFamiliar");
            ocupacion.setFilterStyle("display: none; visibility: hidden;");
            columnaTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:ctipoFamiliar");
            columnaTipo.setFilterStyle("display: none; visibility: hidden;");
            smedico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:servicioMedico");
            smedico.setFilterStyle("display: none; visibility: hidden;");
            sfamiliar = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:sFamiliar");
            sfamiliar.setFilterStyle("display: none; visibility: hidden;");
            beneficiario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:beneficiario");
            beneficiario.setFilterStyle("display: none; visibility: hidden;");
            upcadicional = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:upcAd");
            upcadicional.setFilterStyle("display: none; visibility: hidden;");
            valorupc = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:valorUpc");
            valorupc.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listaFamiliaresFiltrar = null;
            tipoLista = 0;
        }
        k++;
        x = BigDecimal.valueOf(k);
        nuevoFamiliar.setSecuencia(x);
        listaFamiliaresCrear.add(nuevoFamiliar);
        System.out.println("el familiar nuevo es : " + nuevoFamiliar.getPersonafamiliar().getNombreCompleto());
        if (listaFamiliares == null) {
            listaFamiliares = new ArrayList<Familiares>();
        }
        listaFamiliares.add(nuevoFamiliar);
        familiarSeleccionado = nuevoFamiliar;
        System.out.println("Persona  :" + nuevoFamiliar.getPersona().getNombreCompleto());
        System.out.println("nuevo FAMILIAR :" + nuevoFamiliar.getPersonafamiliar().getNombreCompleto());
        getListaFamiliares();
        contarRegistrosFamiliares();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosFamiliares");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFamiliarPersona').hide()");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        nuevoFamiliar = new Familiares();
        nuevoFamiliar.setPersona(persona);
        nuevoFamiliar.setPersonafamiliar(new Personas());
        nuevoFamiliar.setTipofamiliar(new TiposFamiliares());
    }

    public void limpiarNuevoFamiliar() {
        nuevoFamiliar = new Familiares();
        nuevoFamiliar.setPersona(persona);
        nuevoFamiliar.setPersonafamiliar(new Personas());
        nuevoFamiliar.setTipofamiliar(new TiposFamiliares());
    }

    public void limpiarDuplicarFamiliar() {
        duplicarFamiliar = new Familiares();
        duplicarFamiliar.setPersona(persona);
        duplicarFamiliar.setPersonafamiliar(new Personas());
        duplicarFamiliar.setTipofamiliar(new TiposFamiliares());

    }

    public void duplicarFamiliar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (familiarSeleccionado != null) {
            duplicarFamiliar = new Familiares();

            if (tipoLista == 0) {

                duplicarFamiliar.setPersonafamiliar(familiarSeleccionado.getPersonafamiliar());
                duplicarFamiliar.setOcupacion(familiarSeleccionado.getOcupacion());
                duplicarFamiliar.setTipofamiliar(familiarSeleccionado.getTipofamiliar());
                duplicarFamiliar.setServiciomedico(familiarSeleccionado.getServiciomedico());
                duplicarFamiliar.setSubsidiofamiliar(familiarSeleccionado.getSubsidiofamiliar());
                duplicarFamiliar.setBeneficiario(familiarSeleccionado.getBeneficiario());
                duplicarFamiliar.setUpcadicional(familiarSeleccionado.getUpcadicional());
                duplicarFamiliar.setValorupcadicional(familiarSeleccionado.getValorupcadicional());
            }
            if (tipoLista == 1) {

                duplicarFamiliar.setPersonafamiliar(familiarSeleccionado.getPersonafamiliar());
                duplicarFamiliar.setOcupacion(familiarSeleccionado.getOcupacion());
                duplicarFamiliar.setTipofamiliar(familiarSeleccionado.getTipofamiliar());
                duplicarFamiliar.setServiciomedico(familiarSeleccionado.getServiciomedico());
                duplicarFamiliar.setSubsidiofamiliar(familiarSeleccionado.getSubsidiofamiliar());
                duplicarFamiliar.setBeneficiario(familiarSeleccionado.getBeneficiario());
                duplicarFamiliar.setUpcadicional(familiarSeleccionado.getUpcadicional());
                duplicarFamiliar.setValorupcadicional(familiarSeleccionado.getValorupcadicional());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersonaFamiliar");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFamiliarPersona').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarFamiliar.getPersonafamiliar().getNombreCompleto() == null || duplicarFamiliar.getTipofamiliar().getTipo() == null) {
            mensajeValidacion = mensajeValidacion + " Los campos nombre y Tipo Familiar son Obligatorios\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoFamiliar");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoFamiliar').show()");
        }

        if (pasa == 0) {
            k++;
            x = BigDecimal.valueOf(k);
            duplicarFamiliar.setSecuencia(x);
            duplicarFamiliar.setPersona(persona);
            listaFamiliares.add(duplicarFamiliar);
            listaFamiliaresCrear.add(duplicarFamiliar);
            familiarSeleccionado = duplicarFamiliar;
            getListaFamiliares();
            contarRegistrosFamiliares();
            RequestContext.getCurrentInstance().update("form:datosFamiliares");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFamiliarPersona').hide()");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                //CERRAR FILTRADO
                nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:nombreFamiliar");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                ocupacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:ocupacionFamiliar");
                ocupacion.setFilterStyle("display: none; visibility: hidden;");
                columnaTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:ctipoFamiliar");
                columnaTipo.setFilterStyle("display: none; visibility: hidden;");
                smedico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:servicioMedico");
                smedico.setFilterStyle("display: none; visibility: hidden;");
                sfamiliar = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:sFamiliar");
                sfamiliar.setFilterStyle("display: none; visibility: hidden;");
                beneficiario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:beneficiario");
                beneficiario.setFilterStyle("display: none; visibility: hidden;");
                upcadicional = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:upcAd");
                upcadicional.setFilterStyle("display: none; visibility: hidden;");
                valorupc = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:valorUpc");
                valorupc.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listaFamiliaresFiltrar = null;
                tipoLista = 0;
            }
            duplicarFamiliar = new Familiares();
        }
    }

    public void borrarFamiliar() {

        if (familiarSeleccionado != null) {
            if (!listaFamiliaresModificar.isEmpty() && listaFamiliaresModificar.contains(familiarSeleccionado)) {
                int modIndex = listaFamiliaresModificar.indexOf(familiarSeleccionado);
                listaFamiliaresModificar.remove(modIndex);
                listaFamiliaresBorrar.add(familiarSeleccionado);
            } else if (!listaFamiliaresCrear.isEmpty() && listaFamiliaresCrear.contains(familiarSeleccionado)) {
                int crearIndex = listaFamiliaresCrear.indexOf(familiarSeleccionado);
                listaFamiliaresCrear.remove(crearIndex);
            } else {
                listaFamiliaresBorrar.add(familiarSeleccionado);
            }
            listaFamiliares.remove(familiarSeleccionado);
            if (tipoLista == 1) {
                listaFamiliaresFiltrar.remove(familiarSeleccionado);
            }
            contarRegistrosFamiliares();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosFamiliares");
            familiarSeleccionado = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void salirFamiliares() {
        if (bandera == 1) {
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:nombreFamiliar");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            ocupacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:ocupacionFamiliar");
            ocupacion.setFilterStyle("display: none; visibility: hidden;");
            columnaTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:ctipoFamiliar");
            columnaTipo.setFilterStyle("display: none; visibility: hidden;");
            smedico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:servicioMedico");
            smedico.setFilterStyle("display: none; visibility: hidden;");
            sfamiliar = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:sFamiliar");
            sfamiliar.setFilterStyle("display: none; visibility: hidden;");
            beneficiario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:beneficiario");
            beneficiario.setFilterStyle("display: none; visibility: hidden;");
            upcadicional = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:upcAd");
            upcadicional.setFilterStyle("display: none; visibility: hidden;");
            valorupc = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:valorUpc");
            valorupc.setFilterStyle("display: none; visibility: hidden;");
            contarRegistrosFamiliares();
            RequestContext.getCurrentInstance().update("form:datosFamiliares");
            bandera = 0;
            listaFamiliaresFiltrar = null;
            tipoLista = 0;
        }

        listaFamiliaresBorrar.clear();
        listaFamiliaresCrear.clear();
        listaFamiliaresModificar.clear();
        familiarSeleccionado = null;
        k = 0;
        listaFamiliares = null;
        guardado = true;
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

    public void dispararDialogoNuevoFamiliarPersona() {
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFamiliarPersona");
        RequestContext.getCurrentInstance().execute("PF('nuevoFamiliarPersona').show()");
    }

    public void dispararDialogoNuevoFamiliar() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroFamiliarPersona");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFamiliarPersona').show()");
    }

    public void actualizarTipoFamiliar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                familiarSeleccionado.setTipofamiliar(tipoFamiliarSeleccionado);
                if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
                    if (listaFamiliaresModificar.isEmpty()) {
                        listaFamiliaresModificar.add(familiarSeleccionado);
                    } else if (!listaFamiliaresModificar.contains(familiarSeleccionado)) {
                        listaFamiliaresModificar.add(familiarSeleccionado);
                    }
                }
            } else {
                familiarSeleccionado.setTipofamiliar(tipoFamiliarSeleccionado);
                if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
                    if (listaFamiliaresModificar.isEmpty()) {
                        listaFamiliaresModificar.add(familiarSeleccionado);
                    } else if (!listaFamiliaresModificar.contains(familiarSeleccionado)) {
                        listaFamiliaresModificar.add(familiarSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosFamiliares");
        } else if (tipoActualizacion == 1) {
            nuevoFamiliar.setTipofamiliar(tipoFamiliarSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersonaFamiliar");
        } else if (tipoActualizacion == 2) {
            duplicarFamiliar.setTipofamiliar(tipoFamiliarSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersonaFamiliar");
        }

        lovTiposFamiliaresFiltrar = null;
        tipoFamiliarSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:tipoFamiliarDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovFamiliares");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");

        context.reset("formularioDialogos:lovFamiliares:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovFamiliares').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').hide()");
    }

    public void cancelarCambioTipoFamiliar() {
        lovTiposFamiliaresFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        tipoFamiliarSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:tipoFamiliarDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovFamiliares");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");

        context.reset("formularioDialogos:lovFamiliares:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovFamiliares').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').hide()");
    }

    public void actualizarCiudadDocumento() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 1) {
            nuevaPersona.setCiudaddocumento(ciudadDocumentoSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadDocumentoModPersonal");
        }
        lovCiudadDocumentoFiltrar = null;
        ciudadDocumentoSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadDocumentoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovCiudadDocumento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCD");

        context.reset("formularioDialogos:lovCiudadDocumento:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadDocumento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadDocumentoDialogo').hide()");
    }

    public void cancelarCambioCiudadDocumento() {
        lovCiudadDocumentoFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        ciudadDocumentoSeleccionada = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadDocumentoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovCiudadDocumento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCD");

        context.reset("formularioDialogos:lovCiudadDocumento:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadDocumento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadDocumentoDialogo').hide()");
    }

    public void actualizarCiudadNacimiento() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 1) {
            nuevaPersona.setCiudadnacimiento(ciudadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadNacimientoModPersonal");
        }

        lovCiudadesFiltrar = null;
        ciudadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadNacimientoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovCiudadNacimiento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCN");

        context.reset("formularioDialogos:lovCiudadDocumento:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadNacimiento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadNacimientoDialogo').hide()");
    }

    public void cancelarCambioCiudadNacimiento() {
        lovCiudadesFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        ciudadSeleccionada = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadNacimientoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovCiudadNacimiento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCN");

        context.reset("formularioDialogos:lovCiudadDocumento:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadNacimiento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadNacimientoDialogo').hide()");
    }

    public void actualizarPersonaFamiliar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                familiarSeleccionado.setPersonafamiliar(personaSeleccionada);
                if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
                    if (listaFamiliaresModificar.isEmpty()) {
                        listaFamiliaresModificar.add(familiarSeleccionado);
                    } else if (!listaFamiliaresModificar.contains(familiarSeleccionado)) {
                        listaFamiliaresModificar.add(familiarSeleccionado);
                    }
                }
            } else {
                familiarSeleccionado.setPersonafamiliar(personaSeleccionada);
                if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
                    if (listaFamiliaresModificar.isEmpty()) {
                        listaFamiliaresModificar.add(familiarSeleccionado);
                    } else if (!listaFamiliaresModificar.contains(familiarSeleccionado)) {
                        listaFamiliaresModificar.add(familiarSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosFamiliares");
        } else if (tipoActualizacion == 1) {
            nuevoFamiliar.setPersonafamiliar(personaSeleccionada);
            nuevoFamiliar.setPersona(persona);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersonaFamiliar");
        } else if (tipoActualizacion == 2) {
            duplicarFamiliar.setPersona(persona);
            duplicarFamiliar.setPersonafamiliar(personaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersonaFamiliar");
        }
        lovTiposFamiliaresFiltrar = null;
        personaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:personaFamiliarDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovPersonasFamiliares");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarD");

        context.reset("formularioDialogos:lovPersonasFamiliares:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPersonasFamiliares').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('personaFamiliarDialogo').hide()");
    }

    public void cancelarCambioPersonaFamiliar() {
        lovTiposFamiliaresFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        personaSeleccionada = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:personaFamiliarDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovPersonasFamiliares");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");

        context.reset("formularioDialogos:lovPersonasFamiliares:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPersonasFamiliares').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('personaFamiliarDialogo').hide()");
    }

    public void actualizarTipoDocumento() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 1) {
            nuevaPersona.setTipodocumento(tipoDocumentoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFamiliarP");
        }
        lovTiposDocumentosFiltrar = null;
        tipoDocumentoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:tipoDocumentoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTipoDocumento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTD");

        context.reset("formularioDialogos:lovFamiliares:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoDocumento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tipoDocumentoDialogo').hide()");
    }

    public void cancelarCambioTipoDocumento() {
        lovTiposDocumentosFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        tipoDocumentoSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:tipoDocumentoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTipoDocumento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTD");

        context.reset("formularioDialogos:lovFamiliares:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoDocumento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tipoDocumentoDialogo').hide()");
    }

    public void valoresBackupAutocompletarFamiliares(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPODOCUMENTO")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getPersonafamiliar().getTipodocumento().getNombrelargo();
            } else if (tipoNuevo == 2) {
                duplicarFamiliar.getPersonafamiliar().getTipodocumento().getNombrelargo();
            }
        } else if (Campo.equals("CIUDAD")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getPersonafamiliar().getCiudaddocumento().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarFamiliar.getPersonafamiliar().getCiudaddocumento().getNombre();
            }
        } else if (Campo.equals("CIUDADNACIMIENTO")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getPersonafamiliar().getCiudadnacimiento().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarFamiliar.getPersonafamiliar().getCiudadnacimiento().getNombre();
            }
        } else if (Campo.equals("TIPOFAMILIAR")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getTipofamiliar().getTipo();
            } else if (tipoNuevo == 2) {
                duplicarFamiliar.getTipofamiliar().getTipo();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoFamiliares(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPO")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getTipofamiliar().setTipo(nuevoFamiliar.getTipofamiliar().getTipo());
            } else if (tipoNuevo == 2) {
                duplicarFamiliar.getTipofamiliar().setTipo(nuevoFamiliar.getTipofamiliar().getTipo());
            }
            for (int i = 0; i < lovTiposFamiliares.size(); i++) {
                if (lovTiposFamiliares.get(i).getTipo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoFamiliar.setTipofamiliar(lovTiposFamiliares.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoFamiliar");
                } else if (tipoNuevo == 2) {
                    duplicarFamiliar.setTipofamiliar(lovTiposFamiliares.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoFamiliar");
                }
                lovTiposFamiliares.clear();
                getLovTiposFamiliares();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:tipoFamiliarDialogo");
                RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoFamiliar");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoFamiliar");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getPersonafamiliar().getCiudaddocumento().setNombre(nuevoFamiliar.getPersonafamiliar().getCiudaddocumento().getNombre());
            } else if (tipoNuevo == 2) {
                duplicarFamiliar.getPersonafamiliar().getCiudaddocumento().setNombre(duplicarFamiliar.getPersonafamiliar().getCiudaddocumento().getNombre());
            }
            for (int i = 0; i < lovCiudades.size(); i++) {
                if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoFamiliar.getPersonafamiliar().setCiudaddocumento(lovCiudades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudadDocumento");
                } else if (tipoNuevo == 2) {
                    duplicarFamiliar.getPersonafamiliar().setCiudaddocumento(lovCiudades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudadDocumento");
                }
                lovCiudades.clear();
                getLovTiposFamiliares();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:ciudadDocumentoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ciudadDocumentoDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudadDocumento");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudadDocumento");
                }
            }

        } else if (confirmarCambio.equalsIgnoreCase("CIUDADNACIMIENTO")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getPersonafamiliar().getCiudadnacimiento().setNombre(nuevoFamiliar.getPersonafamiliar().getCiudadnacimiento().getNombre());
            } else if (tipoNuevo == 2) {
                duplicarFamiliar.getPersonafamiliar().getCiudadnacimiento().setNombre(duplicarFamiliar.getPersonafamiliar().getCiudadnacimiento().getNombre());
            }
            for (int i = 0; i < lovCiudades.size(); i++) {
                if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoFamiliar.getPersonafamiliar().setCiudadnacimiento(lovCiudades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudadNacimiento");
                } else if (tipoNuevo == 2) {
                    duplicarFamiliar.getPersonafamiliar().setCiudadnacimiento(lovCiudades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudadNacimiento");
                }
                lovCiudades.clear();
                getLovTiposFamiliares();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:ciudadNacimientoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ciudadNacimientoDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudadNacimiento");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudadNacimiento");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TIPODOCUMENTO")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getPersonafamiliar().getTipodocumento().setNombrelargo(nuevoFamiliar.getPersonafamiliar().getTipodocumento().getNombrelargo());
            } else if (tipoNuevo == 2) {
                duplicarFamiliar.getPersonafamiliar().getTipodocumento().setNombrelargo(nuevoFamiliar.getPersonafamiliar().getTipodocumento().getNombrelargo());
            }
            for (int i = 0; i < lovTiposDocumentos.size(); i++) {
                if (lovTiposDocumentos.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoFamiliar.getPersonafamiliar().setTipodocumento(lovTiposDocumentos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoDocumento");
                } else if (tipoNuevo == 2) {
                    duplicarFamiliar.getPersonafamiliar().setTipodocumento(lovTiposDocumentos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoDocumento");
                }
                lovTiposDocumentos.clear();
                getLovTiposFamiliares();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:tipoDocumentoDialogo");
                RequestContext.getCurrentInstance().execute("PF('tipoDocumentoDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoDocumento");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoDocumento");
                }
            }
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
            cualtabla = 4;
            antecedentemSeleccionado = antecedentesm;
            if (cualCelda == 1) {
                antecedentemSeleccionado.getFecha();
                cualCelda = -1;
            } else if (cualCelda == 2) {
                antecedentemSeleccionado.getTipoantecedente().getDescripcion();
                cualCelda = -1;
            } else if (cualCelda == 3) {
                antecedentemSeleccionado.getAntecedente().getDescripcion();
                cualCelda = -1;
            } else if (cualCelda == 4) {
                antecedentemSeleccionado.getDescripcion();
                cualCelda = -1;
            }
        }
//        cualtabla = -1;
    }

    public void modificarAntecedenteM(SoAntecedentesMedicos antecedentem, String confirmarCambio, String valorConfirmar) {
        antecedentemSeleccionado = antecedentem;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (!valorConfirmar.isEmpty()) {
                antecedentemSeleccionado.setTipoantecedente(new SoTiposAntecedentes());
                for (int i = 0; i < lovTiposAntecedentes.size(); i++) {
                    if (lovTiposAntecedentes.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    antecedentemSeleccionado.setTipoantecedente(lovTiposAntecedentes.get(indiceUnicoElemento));
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
                antecedentemSeleccionado.setTipoantecedente(new SoTiposAntecedentes());
                getLovTiposAntecedentes();
            }

        }
        if (coincidencias == 1) {
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
        }
        RequestContext.getCurrentInstance().update("form:datosAntecedentes");
    }

    public void modificarAntecedenteDescripcion(SoAntecedentesMedicos antecedentem) {
        antecedentemSeleccionado = antecedentem;
        SoAntecedentes auxiliar = antecedentem.getAntecedente();
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        System.out.println("valor confirmar : " + antecedentem.getAntecedenteDescripcion());

        if (!antecedentem.getAntecedenteDescripcion().isEmpty()) {
            antecedentemSeleccionado.setAntecedente(new SoAntecedentes());
            for (int i = 0; i < lovAntecedentes.size(); i++) {
                if (lovAntecedentes.get(i).getDescripcion().startsWith(antecedentem.getAntecedenteDescripcion().toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                antecedentemSeleccionado.setAntecedente(lovAntecedentes.get(indiceUnicoElemento));
            } else {
                antecedentemSeleccionado.setAntecedente(auxiliar);
            }
        }
        if (coincidencias == 1) {
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
        }
        RequestContext.getCurrentInstance().update("form:datosAntecedentes");
    }

    public void nuevoAntecedenteDescripcion(SoAntecedentesMedicos antecedentemnuevo){
        nuevoAntecedentem = antecedentemnuevo;
        SoAntecedentes auxiliar = new SoAntecedentes();
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        System.out.println("valor confirmar : " + antecedentemnuevo.getAntecedenteDescripcion());

        if (!antecedentemnuevo.getAntecedenteDescripcion().isEmpty()) {
            nuevoAntecedentem.setAntecedente(new SoAntecedentes());
            for (int i = 0; i < lovAntecedentes.size(); i++) {
                if (lovAntecedentes.get(i).getDescripcion().startsWith(nuevoAntecedentem.getAntecedenteDescripcion().toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                nuevoAntecedentem.setAntecedente(lovAntecedentes.get(indiceUnicoElemento));
            } else {
                nuevoAntecedentem.setAntecedente(auxiliar);
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        
    }
    
    public void duplicarAntecedenteDescripcion(SoAntecedentesMedicos antecedentemduplicado){
     duplicarAntecedenteM = antecedentemduplicado;
        SoAntecedentes auxiliar = antecedentemduplicado.getAntecedente();
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        System.out.println("valor confirmar : " + antecedentemduplicado.getAntecedenteDescripcion());

        if (!antecedentemduplicado.getAntecedenteDescripcion().isEmpty()) {
            duplicarAntecedenteM.setAntecedente(new SoAntecedentes());
            for (int i = 0; i < lovAntecedentes.size(); i++) {
                if (lovAntecedentes.get(i).getDescripcion().startsWith(duplicarAntecedenteM.getAntecedenteDescripcion().toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                duplicarAntecedenteM.setAntecedente(lovAntecedentes.get(indiceUnicoElemento));
            } else {
                duplicarAntecedenteM.setAntecedente(auxiliar);
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
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

    public void agregarAntecedenteMedico() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("Desactivar");
            fechaAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:fechaAntecedenteM");
            fechaAntecedenteM.setFilterStyle("display: none; visibility: hidden;");
            tipoAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:tipoAntecedenteM");
            tipoAntecedenteM.setFilterStyle("display: none; visibility: hidden;");
            antecedenteMedico = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:antecedenteMedico");
            antecedenteMedico.setFilterStyle("display: none; visibility: hidden;");
            descAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:descAntecedenteM");
            descAntecedenteM.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            bandera = 0;
            listAntecedentesMFiltrar = null;
            tipoLista = 0;
        }
        System.out.println("Despues de la bandera");

        k++;
        l = BigInteger.valueOf(k);
        System.out.println("agregar antecedente medico : " + persona.getNombreCompleto());
        empleado = administrarVigDomiciliarias.buscarEmpleado(persona.getSecuencia());
        System.out.println("insertar antecedente medico : empleado " + empleado);
        nuevoAntecedentem.setSecuencia(l);
        nuevoAntecedentem.setEmpleado(empleado);
        listAntecedentesMCrear.add(nuevoAntecedentem);
        listAntecedentesM.add(nuevoAntecedentem);
        contarRegistrosAntecedentesM();
        antecedentemSeleccionado = nuevoAntecedentem;
        nuevoAntecedentem = new SoAntecedentesMedicos();
        nuevoAntecedentem.setTipoantecedente(new SoTiposAntecedentes());
        nuevoAntecedentem.setAntecedente(new SoAntecedentes());
        nuevoAntecedentem.setAntecedenteDescripcion("");
        RequestContext.getCurrentInstance().update("form:datosAntecedentes");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

        RequestContext.getCurrentInstance().execute("PF('nuevoRegistroAntecedentesM').hide()");

    }

    public void borrarAntecedenteMedico() {
        if (antecedentemSeleccionado != null) {
            System.out.println("Entro a borrandoEvalCompetencias");
            if (!listAntecedentesModificar.isEmpty() && listAntecedentesModificar.contains(antecedentemSeleccionado)) {
                int modIndex = listAntecedentesModificar.indexOf(antecedentemSeleccionado);
                listAntecedentesModificar.remove(modIndex);
                listAntecedentesMBorrar.add(antecedentemSeleccionado);
            } else if (!listAntecedentesMCrear.isEmpty() && listAntecedentesMCrear.contains(antecedentemSeleccionado)) {
                int crearIndex = listAntecedentesMCrear.indexOf(antecedentemSeleccionado);
                listAntecedentesMCrear.remove(crearIndex);
            } else {
                listAntecedentesMBorrar.add(antecedentemSeleccionado);
            }
            listAntecedentesM.remove(antecedentemSeleccionado);
            if (tipoLista == 1) {
                listAntecedentesMFiltrar.remove(antecedentemSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            contarRegistrosAntecedentesM();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            antecedentemSeleccionado = null;
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void confirmarDuplicarAntecedenteMedico() {
        k++;
        l = BigInteger.valueOf(k);
        duplicarAntecedenteM.setSecuencia(l);
        duplicarAntecedenteM.setEmpleado(empleado);
        listAntecedentesM.add(duplicarAntecedenteM);
        listAntecedentesMCrear.add(duplicarAntecedenteM);
        RequestContext.getCurrentInstance().update("form:datosAntecedentes");
        antecedentemSeleccionado = duplicarAntecedenteM;
        if (guardado == true) {
            guardado = false;
        }
        contarRegistrosAntecedentesM();
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            fechaAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:fechaAntecedenteM");
            fechaAntecedenteM.setFilterStyle("display: none; visibility: hidden;");
            tipoAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:tipoAntecedenteM");
            tipoAntecedenteM.setFilterStyle("display: none; visibility: hidden;");
            antecedenteMedico = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:antecedenteMedico");
            antecedenteMedico.setFilterStyle("display: none; visibility: hidden;");
            descAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:descAntecedenteM");
            descAntecedenteM.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            bandera = 0;
            listAntecedentesMFiltrar = null;
            tipoLista = 0;
        }
        duplicarAntecedenteM = new SoAntecedentesMedicos();
        RequestContext.getCurrentInstance().execute("PF('duplicarRegistroAntecedentesM').hide()");
    }

    public void duplicarAntecedenteMedico() {
        if (antecedentemSeleccionado != null) {
            duplicarAntecedenteM = new SoAntecedentesMedicos();
            duplicarAntecedenteM.setEmpleado(new Empleados());
            duplicarAntecedenteM.setAntecedente(new SoAntecedentes());
            duplicarAntecedenteM.setTipoantecedente(new SoTiposAntecedentes());
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarAntecedenteM.setSecuencia(l);
                duplicarAntecedenteM.setEmpleado(antecedentemSeleccionado.getEmpleado());
                duplicarAntecedenteM.setFecha(antecedentemSeleccionado.getFecha());
                duplicarAntecedenteM.setTipoantecedente(antecedentemSeleccionado.getTipoantecedente());
                duplicarAntecedenteM.setAntecedente(antecedentemSeleccionado.getAntecedente());
                duplicarAntecedenteM.setDescripcion(antecedentemSeleccionado.getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarAntecedenteM.setSecuencia(l);
                duplicarAntecedenteM.setEmpleado(antecedentemSeleccionado.getEmpleado());
                duplicarAntecedenteM.setFecha(antecedentemSeleccionado.getFecha());
                duplicarAntecedenteM.setTipoantecedente(antecedentemSeleccionado.getTipoantecedente());
                duplicarAntecedenteM.setAntecedente(antecedentemSeleccionado.getAntecedente());
                duplicarAntecedenteM.setDescripcion(antecedentemSeleccionado.getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAntecedenteM");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroAntecedentesM').show()");

        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void limpiarDuplicarAntecedenteM() {
        duplicarAntecedenteM = new SoAntecedentesMedicos();
        duplicarAntecedenteM.setEmpleado(new Empleados());
        duplicarAntecedenteM.setAntecedente(new SoAntecedentes());
        duplicarAntecedenteM.setTipoantecedente(new SoTiposAntecedentes());
    }

    public void cancelarModificacionAntecedenteM() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            fechaAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:fechaAntecedenteM");
            fechaAntecedenteM.setFilterStyle("display: none; visibility: hidden;");
            tipoAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:tipoAntecedenteM");
            tipoAntecedenteM.setFilterStyle("display: none; visibility: hidden;");
            antecedenteMedico = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:antecedenteMedico");
            antecedenteMedico.setFilterStyle("display: none; visibility: hidden;");
            descAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:descAntecedenteM");
            descAntecedenteM.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            bandera = 0;
            listAntecedentesMFiltrar = null;
            tipoLista = 0;
        }
        listAntecedentesMBorrar.clear();
        listAntecedentesMCrear.clear();
        listAntecedentesModificar.clear();
        antecedentemSeleccionado = null;
        k = 0;
        listAntecedentesM = null;
        guardado = true;
        permitirIndex = true;
        getListAntecedentesM();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistrosAntecedentesM();
        RequestContext.getCurrentInstance().update("form:datosAntecedentes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salirAntecedenteM() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            fechaAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:fechaAntecedenteM");
            fechaAntecedenteM.setFilterStyle("display: none; visibility: hidden;");
            tipoAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:tipoAntecedenteM");
            tipoAntecedenteM.setFilterStyle("display: none; visibility: hidden;");
            antecedenteMedico = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:antecedenteMedico");
            antecedenteMedico.setFilterStyle("display: none; visibility: hidden;");
            descAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:descAntecedenteM");
            descAntecedenteM.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            bandera = 0;
            listAntecedentesMFiltrar = null;
            tipoLista = 0;
        }
        listAntecedentesMBorrar.clear();
        listAntecedentesMCrear.clear();
        listAntecedentesModificar.clear();
        antecedentemSeleccionado = null;
        k = 0;
        listAntecedentesM = null;
        lovAntecedentes = null;
        guardado = true;
        permitirIndex = true;
        getListAntecedentesM();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistrosAntecedentesM();
        RequestContext.getCurrentInstance().update("form:datosAntecedentes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void actualizarTipoAntecedente() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                antecedentemSeleccionado.setTipoantecedente(tipoAntecedenteSeleccionado);
                antecedentemSeleccionado.setAntecedente(new SoAntecedentes());
                antecedentemSeleccionado.setAntecedenteDescripcion("");

                if (!listAntecedentesMCrear.contains(antecedentemSeleccionado)) {
                    if (listAntecedentesModificar.isEmpty()) {
                        listAntecedentesModificar.add(antecedentemSeleccionado);
                    } else if (!listAntecedentesModificar.contains(antecedentemSeleccionado)) {
                        listAntecedentesModificar.add(antecedentemSeleccionado);
                    }
                }
            } else {
                antecedentemSeleccionado.setTipoantecedente(tipoAntecedenteSeleccionado);
                antecedentemSeleccionado.setAntecedente(new SoAntecedentes());

                if (!listAntecedentesMCrear.contains(antecedentemSeleccionado)) {
                    if (listAntecedentesModificar.isEmpty()) {
                        listAntecedentesModificar.add(antecedentemSeleccionado);
                    } else if (!listAntecedentesModificar.contains(antecedentemSeleccionado)) {
                        listAntecedentesModificar.add(antecedentemSeleccionado);
                    }
                }
            }
            guardado = false;
            permitirIndex = true;
            System.out.println("tipo antecedente seleccionado : " + antecedentemSeleccionado.getTipoantecedente().getDescripcion());
            lovAntecedentes = null;
            lovAntecedentes = administrarVigDomiciliarias.lovAntecedentes(antecedentemSeleccionado.getTipoantecedente().getSecuencia());
            System.out.println("actualizar tipo antecedente seleccionado" + antecedentemSeleccionado.getTipoantecedente().getDescripcion() + ", lov antecedentes : " + lovAntecedentes);
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        } else if (tipoActualizacion == 1) {
            nuevoAntecedentem.setTipoantecedente(tipoAntecedenteSeleccionado);
            nuevoAntecedentem.setAntecedente(new SoAntecedentes());
            nuevoAntecedentem.setAntecedenteDescripcion("");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoAntecedente");
            lovAntecedentesDescripcion = new ArrayList<String>();
            lovAntecedentes = null;
            lovAntecedentes = administrarVigDomiciliarias.lovAntecedentes(nuevoAntecedentem.getTipoantecedente().getSecuencia());
            System.out.println("actualizar nuevo tipo antecedente seleccionado" + nuevoAntecedentem.getTipoantecedente().getDescripcion() + ", lov antecedentes : " + lovAntecedentes);
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNuevoAntecedente");
        } else if (tipoActualizacion == 2) {
            duplicarAntecedenteM.setTipoantecedente(tipoAntecedenteSeleccionado);
            duplicarAntecedenteM.setAntecedente(new SoAntecedentes());
            duplicarAntecedenteM.setAntecedenteDescripcion("");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoAntecedente");
            lovAntecedentes = null;
            lovAntecedentes = administrarVigDomiciliarias.lovAntecedentes(duplicarAntecedenteM.getTipoantecedente().getSecuencia());
            System.out.println("actualizar duplicar tipo antecedente seleccionado" + duplicarAntecedenteM.getTipoantecedente().getDescripcion() + ", lov antecedentes : " + lovAntecedentes);
            lovAntecedentesDescripcion = new ArrayList<String>();
//            RequestContext.getCurrentInstance().update("formularioDialogos:editarNuevoAntecedente");
        }
        lovTiposAntecedentesFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:lovTiposAntecedentes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposAntecedentes').clearFilters()");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTipoAnt");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposAntecedentesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTiposAntecedentes");
        RequestContext.getCurrentInstance().execute("PF('tiposAntecedentesDialogo').hide()");
        lovAntecedentesDescripcion = new ArrayList<String>();
        for (int i = 0; i < lovAntecedentes.size(); i++) {
            lovAntecedentesDescripcion.add(lovAntecedentes.get(i).getDescripcion());
        }
        System.out.println("lov antecedentes descripcion: " + lovAntecedentesDescripcion);
    }

    public void cancelarCambioTipoAntecedente() {
        lovTiposAntecedentesFiltrar = null;
        tipoAntecedenteSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovTiposAntecedentes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposAntecedentes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposAntecedentesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposAntecedentesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTiposAntecedentes");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTipoAnt");
    }

    public void limpiarAntecedenteM() {
        nuevoAntecedentem = new SoAntecedentesMedicos();
        nuevoAntecedentem.setAntecedente(new SoAntecedentes());
        nuevoAntecedentem.setTipoantecedente(new SoTiposAntecedentes());
        nuevoAntecedentem.setAntecedenteDescripcion("");
    }

    public void asignarIndexAntecedentesM(SoAntecedentesMedicos antecedenteM, int dlg, int LND) {
        antecedentemSeleccionado = antecedenteM;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 2) {
            contarRegistroTipoAntecedentes();
            RequestContext.getCurrentInstance().execute("PF('tiposAntecedentesDialogo').show()");
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposAntecedentesDialogo");
            RequestContext.getCurrentInstance().update("formularioDialogos:lovTiposAntecedentes");
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
        if (direccionSeleccionada != null) {
            direccionSeleccionada = direccion;
            cualCelda = celda;
            cualtabla = 1;
            direccionSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                direccionSeleccionada.getFechavigencia();
                cualCelda = -1;
            } else if (cualCelda == 1) {
                direccionSeleccionada.getEstadoTipoPpal();
                cualCelda = -1;
            } else if (cualCelda == 2) {
                direccionSeleccionada.getPpal();
                cualCelda = -1;
            } else if (cualCelda == 3) {
                direccionSeleccionada.getEstadoTipoSecundario();
                cualCelda = -1;
            } else if (cualCelda == 4) {
                direccionSeleccionada.getSecundario();
                cualCelda = -1;
            } else if (cualCelda == 5) {
                direccionSeleccionada.getInterior();
                cualCelda = -1;
            } else if (cualCelda == 6) {
                contarRegistroCiudades();
                direccionSeleccionada.getCiudad().getNombre();
                cualCelda = -1;
            } else if (cualCelda == 7) {
                direccionSeleccionada.getTipovivienda();
                cualCelda = -1;
            } else if (cualCelda == 8) {
                direccionSeleccionada.getHipoteca();
                cualCelda = -1;
            } else if (cualCelda == 9) {
                direccionSeleccionada.getDireccionalternativa();
                cualCelda = -1;
            }
        }
//        cualtabla = -1;
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
        contarRegistrosCiudadesDirecciones();
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDireccionesDialogo");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDireccionesDialogo').show()");
        tipoActualizacion = 0;
    }

    public void contarRegistrosDirecciones() {
        RequestContext.getCurrentInstance().update("form:infoRegistroDireccion");
    }

    public void contarRegistrosCiudadesDirecciones() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCiudadesDireccion");
    }

    public void eventoFiltrarDirecciones() {
        contarRegistrosDirecciones();
    }

    public void llamarLovCiudad(int tipoN) {
        if (tipoN == 1) {
            tipoActualizacion = 1;
        } else if (tipoN == 2) {
            tipoActualizacion = 2;
        }
        contarRegistrosCiudadesDirecciones();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDireccionesDialogo");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDireccionesDialogo').show()");
    }

    public void borrarDirecciones() {

        if (direccionSeleccionada != null) {
            if (!listDireccionesModificar.isEmpty() && listDireccionesModificar.contains(direccionSeleccionada)) {
                int modIndex = listDireccionesModificar.indexOf(direccionSeleccionada);
                listDireccionesModificar.remove(modIndex);
                listDireccionesBorrar.add(direccionSeleccionada);
            } else if (!listDireccionesCrear.isEmpty() && listDireccionesCrear.contains(direccionSeleccionada)) {
                int crearIndex = listDireccionesCrear.indexOf(direccionSeleccionada);
                listDireccionesCrear.remove(crearIndex);
            } else {
                listDireccionesBorrar.add(direccionSeleccionada);
            }
            listDirecciones.remove(direccionSeleccionada);

            if (tipoLista == 1) {

                listDireccionesFiltrar.remove(direccionSeleccionada);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            direccionSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void actualizarCiudadDireccion() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                direccionSeleccionada.setCiudad(ciudadDireccionSeleccionada);
                if (!listDireccionesCrear.contains(direccionSeleccionada)) {
                    if (listDireccionesModificar.isEmpty()) {
                        listDireccionesModificar.add(direccionSeleccionada);
                    } else if (!listDireccionesModificar.contains(direccionSeleccionada)) {
                        listDireccionesModificar.add(direccionSeleccionada);
                    }
                }
            } else {
                direccionSeleccionada.setCiudad(ciudadDireccionSeleccionada);
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
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaDireccion.setCiudad(ciudadDireccionSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDireccion");
        } else if (tipoActualizacion == 2) {
            System.out.println(ciudadDireccionSeleccionada.getNombre());
            duplicarDireccion.setCiudad(ciudadDireccionSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDireccion");
        }
        lovCiudadDireccionFiltrar = null;
        ciudadDireccionSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVDireccionesCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVDireccionesCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDireccionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDireccionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVDireccionesCiudades");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarDC");
    }

    public void cancelarCambioCiudadDireccion() {
        lovCiudadDireccionFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVDireccionesCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVDireccionesCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDireccionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDireccionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVDireccionesCiudades");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarDC");

    }

    public void pregunta(int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (nuevaDireccion.getTipoppal() != null && nuevaDireccion.getPpal() != null && nuevaDireccion.getSecundario() != null && nuevaDireccion.getTiposecundario() != null) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formularioDialogos:pregunta");
                RequestContext.getCurrentInstance().execute("PF('pregunta').show()");
            }
        } else if (tipoNuevo == 2) {
            if (duplicarDireccion.getTipoppal() != null && duplicarDireccion.getPpal() != null && duplicarDireccion.getSecundario() != null && duplicarDireccion.getTiposecundario() != null) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formularioDialogos:pregunta");
                RequestContext.getCurrentInstance().execute("PF('pregunta').show()");
            }
        }
    }

    public void copiarDireccion(int tipoNuevo) {
        if (tipoNuevo == 1) {
            nuevaDireccion.setDireccionalternativa(nuevaDireccion.getEstadoTipoPpal() + (" ") + nuevaDireccion.getPpal() + (" ") + nuevaDireccion.getEstadoTipoSecundario() + (" ") + nuevaDireccion.getSecundario());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDireccion");
        } else {
            duplicarDireccion.setDireccionalternativa(nuevaDireccion.getTipoppal() + (" ") + duplicarDireccion.getPpal() + (" ") + duplicarDireccion.getTiposecundario() + (" ") + duplicarDireccion.getSecundario());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDireccion");
        }

    }

    public void agregarNuevaDireccion() {
        int pasa = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext c = FacesContext.getCurrentInstance();
        if (nuevaDireccion.getFechavigencia() == null) {
            mensajeValidacion = " * Fecha Vigencia\n";
            pasa++;
        }
        if (nuevaDireccion.getTipoppal() == null) {
            mensajeValidacion = " * Ubicacion Principal\n";
            pasa++;
        }
        if (nuevaDireccion.getPpal() == null) {
            mensajeValidacion = " * Descripcion U. Principal\n";
            pasa++;
        }
        if (nuevaDireccion.getTiposecundario() == null) {
            mensajeValidacion = " * Ubicacion Secundaria\n";
            pasa++;
        }
        if (nuevaDireccion.getCiudad().getNombre().equals(" ")) {
            mensajeValidacion = " * Ciudad \n";
            pasa++;
        }

        if (nuevaDireccion.getInterior() == null) {
            mensajeValidacion = "* Barrio \n";
            pasa++;
        }

        if (pasa == 0) {
            if (bandera == 1) {
                dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
                dFecha.setFilterStyle("display: none; visibility: hidden;");
                dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
                dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
                dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
                dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
                dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
                dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
                dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
                dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
                dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
                dInterior.setFilterStyle("display: none; visibility: hidden;");
                dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
                dCiudad.setFilterStyle("display: none; visibility: hidden;");
                dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
                dTipoVivienda.setFilterStyle("display: none; visibility: hidden;");
                dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
                dHipoteca.setFilterStyle("display: none; visibility: hidden;");
                dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
                dDireccionAlternativa.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
                bandera = 0;
                listDireccionesFiltrar = null;
                tipoLista = 0;
                altoTabla = "270";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaDireccion.setSecuencia(l);
            nuevaDireccion.setPersona(persona);
            listDireccionesCrear.add(nuevaDireccion);
            if (listDirecciones == null) {
                listDirecciones = new ArrayList<Direcciones>();
            }
            listDirecciones.add(nuevaDireccion);
            System.out.print("Lista direcciones");
            System.out.println(listDirecciones.size());

            direccionSeleccionada = nuevaDireccion;
            getListDirecciones();
            contarRegistrosDirecciones();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            nuevaDireccion = new Direcciones();
            nuevaDireccion.setCiudad(new Ciudades());
            nuevaDireccion.setFechavigencia(new Date());
            nuevaDireccion.setTipoppal("C");
            nuevaDireccion.setTiposecundario("K");
            nuevaDireccion.setTipovivienda("CASA");
            nuevaDireccion.setHipoteca("N");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDireccion').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaDireccion");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaDireccion').show()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void limpiarNuevaDireccion() {
        nuevaDireccion = new Direcciones();
        nuevaDireccion.setCiudad(new Ciudades());
        nuevaDireccion.setFechavigencia(new Date());
        nuevaDireccion.setTipoppal("C");
        nuevaDireccion.setTiposecundario("K");
        nuevaDireccion.setTipovivienda("CASA");
        nuevaDireccion.setHipoteca("N");
    }

    public void duplicarDireccion() {
        if (direccionSeleccionada != null) {
            duplicarDireccion = new Direcciones();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarDireccion.setSecuencia(l);
                duplicarDireccion.setFechavigencia(direccionSeleccionada.getFechavigencia());
                duplicarDireccion.setTipoppal(direccionSeleccionada.getTipoppal());
                duplicarDireccion.setPpal(direccionSeleccionada.getPpal());
                duplicarDireccion.setTiposecundario(direccionSeleccionada.getTiposecundario());
                duplicarDireccion.setSecundario(direccionSeleccionada.getSecundario());
                duplicarDireccion.setInterior(direccionSeleccionada.getInterior());
                duplicarDireccion.setCiudad(direccionSeleccionada.getCiudad());
                duplicarDireccion.setHipoteca(direccionSeleccionada.getHipoteca());
                duplicarDireccion.setDireccionalternativa(direccionSeleccionada.getDireccionalternativa());
            }
            if (tipoLista == 1) {
                duplicarDireccion.setSecuencia(l);
                duplicarDireccion.setFechavigencia(direccionSeleccionada.getFechavigencia());
                duplicarDireccion.setTipoppal(direccionSeleccionada.getTipoppal());
                duplicarDireccion.setPpal(direccionSeleccionada.getPpal());
                duplicarDireccion.setTiposecundario(direccionSeleccionada.getTiposecundario());
                duplicarDireccion.setSecundario(direccionSeleccionada.getSecundario());
                duplicarDireccion.setInterior(direccionSeleccionada.getInterior());
                duplicarDireccion.setCiudad(direccionSeleccionada.getCiudad());
                duplicarDireccion.setTipovivienda(direccionSeleccionada.getTipovivienda());
                duplicarDireccion.setHipoteca(direccionSeleccionada.getHipoteca());
                duplicarDireccion.setDireccionalternativa(direccionSeleccionada.getDireccionalternativa());
                altoTabla = "270";
            }
            RequestContext context = RequestContext.getCurrentInstance();
            duplicarDireccion.setPersona(persona);
            listDireccionesCrear.add(duplicarDireccion);
            listDirecciones.add(duplicarDireccion);
            direccionSeleccionada = duplicarDireccion;

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDireccion");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDireccion').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarDireccion() {
        int pasa = 0;
        int pasaA = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext c = FacesContext.getCurrentInstance();
        if (duplicarDireccion.getFechavigencia() == null) {
            mensajeValidacion = " Campo fecha vacío \n";
            pasa++;

        }

        System.out.println("fecha direccion " + duplicarDireccion.getFechavigencia() + "\n");
        System.out.println("secuencia direccion " + duplicarDireccion.getSecuencia() + "\n");
        for (int i = 0; i < listDirecciones.size(); i++) {
            if ((listDirecciones.get(i).getSecuencia().equals(duplicarDireccion.getSecuencia())) && ((listDirecciones.get(i).getFechavigencia().equals(duplicarDireccion.getFechavigencia())))) {// && !(duplicarTelefono.getFechavigencia().before(listaTelefonos.get(i).getFechavigencia())))) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeDireccion");
                RequestContext.getCurrentInstance().execute("PF('existeDireccion').show()");
                pasaA++;
            }
            if (pasa != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaDireccion");
                RequestContext.getCurrentInstance().execute("PF('validacionNuevaDireccion').show()");
            }
        }
//
        if (pasa == 0 && pasaA == 0) {

            listDirecciones.add(duplicarDireccion);
            listDireccionesCrear.add(duplicarDireccion);
            direccionSeleccionada = duplicarDireccion;
            getListDirecciones();
            contarRegistrosDirecciones();
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
                dFecha.setFilterStyle("display: none; visibility: hidden;");
                dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
                dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
                dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
                dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
                dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
                dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
                dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
                dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
                dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
                dInterior.setFilterStyle("display: none; visibility: hidden;");
                dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
                dCiudad.setFilterStyle("display: none; visibility: hidden;");
                dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
                dTipoVivienda.setFilterStyle("display: none; visibility: hidden;");
                dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
                dHipoteca.setFilterStyle("display: none; visibility: hidden;");
                dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
                dDireccionAlternativa.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "270";
                RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
                bandera = 0;
                listDireccionesFiltrar = null;
                tipoLista = 0;
            }
            duplicarDireccion = new Direcciones();
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDireccion').hide()");
        }
    }

//    public void guardarCambiosDireccion() {
//        if (guardado == false) {
//            if (!listDireccionesBorrar.isEmpty()) {
//                administrarVigDomiciliarias.borrarDirecciones(listDireccionesBorrar);
//                listDireccionesBorrar.clear();
//            }
//            if (!listDireccionesCrear.isEmpty()) {
//                administrarVigDomiciliarias.crearDirecciones(listDireccionesCrear);
//                listDireccionesCrear.clear();
//            }
//            if (!listDireccionesModificar.isEmpty()) {
//                administrarVigDomiciliarias.modificarDirecciones(listDireccionesModificar);
//                listDireccionesModificar.clear();
//            }
//            System.out.println("Se guardaron los datos con exito");
//            listDirecciones = null;
//            getListDirecciones();
//            contarRegistrosDirecciones();
//            RequestContext context = RequestContext.getCurrentInstance();
//            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
//            guardado = true;
//            permitirIndex = true;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("form:growl");
//        }
//        direccionSeleccionada = null;
//    }
    public void salirDirecciones() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
            dFecha.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
            dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
            dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
            dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
            dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
            dInterior.setFilterStyle("display: none; visibility: hidden;");
            dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
            dCiudad.setFilterStyle("display: none; visibility: hidden;");
            dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
            dTipoVivienda.setFilterStyle("display: none; visibility: hidden;");
            dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
            dHipoteca.setFilterStyle("display: none; visibility: hidden;");
            dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
            dDireccionAlternativa.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            bandera = 0;
            listDireccionesFiltrar = null;
            tipoLista = 0;
        }
        listDireccionesBorrar.clear();
        listDireccionesCrear.clear();
        listDireccionesModificar.clear();
        direccionSeleccionada = null;
        //    k = 0;
        listDirecciones = null;
        guardado = true;
        contarRegistrosDirecciones();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        permitirIndex = true;
    }

    public void cancelarModificacionDireccion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
            dFecha.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
            dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
            dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
            dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
            dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
            dInterior.setFilterStyle("display: none; visibility: hidden;");
            dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
            dCiudad.setFilterStyle("display: none; visibility: hidden;");
            dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
            dTipoVivienda.setFilterStyle("display: none; visibility: hidden;");
            dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
            dHipoteca.setFilterStyle("display: none; visibility: hidden;");
            dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
            dDireccionAlternativa.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            bandera = 0;
            listDireccionesFiltrar = null;
            tipoLista = 0;

        }

        listDireccionesBorrar.clear();
        listDireccionesCrear.clear();
        listDireccionesModificar.clear();
        k = 0;
        listDirecciones = null;
        direccionSeleccionada = null;
        contarRegistrosDirecciones();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");

    }

//// telefonos
    public void cambiarIndiceTelefonos(Telefonos telefono, int celda) {
        if (permitirIndex == true) {
            telefonoSeleccionado = telefono;
            cualCelda = celda;
            cualtabla = 2;
            telefonoSeleccionado.getSecuencia();
            if (cualCelda == 0) {
                telefonoSeleccionado.getFechavigencia();
                cualCelda = -1;
            } else if (cualCelda == 1) {
                telefonoSeleccionado.getTipotelefono().getNombre();
                cualCelda = -1;
            } else if (cualCelda == 2) {
                telefonoSeleccionado.getNumerotelefono();
                cualCelda = -1;
            } else if (cualCelda == 3) {
                telefonoSeleccionado.getCiudad().getNombre();
                cualCelda = -1;
            }
        }
//        cualtabla = -1;
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
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesTelefonoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ciudadesTelefonoDialogo').show()");
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

    public void cancelarModificacionTelefono() {
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
            listTelefonosFiltrar = null;
            tipoLista = 0;
            altoTabla = "270";
        }

        listTelefonosBorrar.clear();
        listTelefonosCrear.clear();
        listTelefonosModificar.clear();
        k = 0;
        contarRegistrosTelefono();
        telefonoSeleccionado = null;
        listTelefonos = null;
        guardado = true;
        permitirIndex = true;
        getListTelefonos();
        contarRegistrosTelefono();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
    }

    public void actualizarTiposTelefonos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                telefonoSeleccionado.setTipotelefono(tipoTelefonoSeleccionado);
                if (!listTelefonosCrear.contains(telefonoSeleccionado)) {
                    if (listTelefonosModificar.isEmpty()) {
                        listTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listTelefonosModificar.contains(telefonoSeleccionado)) {
                        listTelefonosModificar.add(telefonoSeleccionado);
                    }
                }
            } else {
                telefonoSeleccionado.setTipotelefono(tipoTelefonoSeleccionado);
                if (!listTelefonosCrear.contains(telefonoSeleccionado)) {
                    if (listTelefonosModificar.isEmpty()) {
                        listTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listTelefonosModificar.contains(telefonoSeleccionado)) {
                        listTelefonosModificar.add(telefonoSeleccionado);
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
            nuevoTelefono.setTipotelefono(tipoTelefonoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTelefono");
        } else if (tipoActualizacion == 2) {
            duplicarTelefono.setTipotelefono(tipoTelefonoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTelefono");
        }
        lovTiposTelefonosFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        //cualCelda = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposTelefonosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposTelefonos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTT");
        context.reset("formularioDialogos:LOVTiposTelefonos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposTelefonos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposTelefonosDialogo').hide()");
    }

    public void cancelarCambioTiposTelefonos() {
        lovTiposTelefonosFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposTelefonosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposTelefonos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTT");
        context.reset("formularioDialogos:LOVTiposTelefonos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposTelefonos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposTelefonosDialogo').hide()");
    }

    public void borrarTelefonos() {

        if (telefonoSeleccionado != null) {
            if (!listTelefonosModificar.isEmpty() && listTelefonosModificar.contains(telefonoSeleccionado)) {
                listTelefonosModificar.remove(listTelefonosModificar.indexOf(telefonoSeleccionado));
                listTelefonosBorrar.add(telefonoSeleccionado);
            } else if (!listTelefonosCrear.isEmpty() && listTelefonosCrear.contains(telefonoSeleccionado)) {
                listTelefonosCrear.remove(listTelefonosCrear.indexOf(telefonoSeleccionado));
            } else {
                listTelefonosBorrar.add(telefonoSeleccionado);
            }
            listTelefonos.remove(telefonoSeleccionado);
            if (tipoLista == 1) {
                listTelefonosFiltrar.remove(telefonoSeleccionado);
            }
            contarRegistrosTelefono();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
            telefonoSeleccionado = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void duplicarTelefono() {
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
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTelefono').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarTelefono() {
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
        for (int i = 0; i < listTelefonos.size(); i++) {
            if ((listTelefonos.get(i).getTipotelefono().getNombre().equals(duplicarTelefono.getTipotelefono().getNombre())) && (!(listTelefonos.get(i).getFechavigencia().before(duplicarTelefono.getFechavigencia())) && !(duplicarTelefono.getFechavigencia().before(listTelefonos.get(i).getFechavigencia())))) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeTipoTelefono");
                RequestContext.getCurrentInstance().execute("PF('existeTipoTelefono').show()");
                pasaA++;
            }
            if (pasa != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTelefono");
                RequestContext.getCurrentInstance().execute("PF('validacionNuevoTelefono').show()");
            }
        }

        if (pasa == 0 && pasaA == 0) {
            listTelefonos.add(duplicarTelefono);
            listTelefonosCrear.add(duplicarTelefono);
            telefonoSeleccionado = duplicarTelefono;
            getListTelefonos();
            contarRegistrosTelefono();
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
                listTelefonosFiltrar = null;
                tipoLista = 0;
            }
            duplicarTelefono = new Telefonos();
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTelefono').hide()");
        }

    }

    public void limpiarduplicarTelefono() {
        duplicarTelefono = new Telefonos();
        duplicarTelefono.setTipotelefono(new TiposTelefonos());
        duplicarTelefono.setCiudad(new Ciudades());
    }

    public void limpiarNuevoTelefono() {
        nuevoTelefono = new Telefonos();
        nuevoTelefono.setTipotelefono(new TiposTelefonos());
        nuevoTelefono.setCiudad(new Ciudades());
    }

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
        for (int i = 0; i < listTelefonos.size(); i++) {
            if ((listTelefonos.get(i).getTipotelefono().getNombre().equals(nuevoTelefono.getTipotelefono().getNombre())) && (!(listTelefonos.get(i).getFechavigencia().before(nuevoTelefono.getFechavigencia())) && !(nuevoTelefono.getFechavigencia().before(listTelefonos.get(i).getFechavigencia())))) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeTipoTelefono");
                RequestContext.getCurrentInstance().execute("PF('existeTipoTelefono').show()");
                pasaA++;
            }
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
                listTelefonosFiltrar = null;
                tipoLista = 0;
                altoTabla = "270";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTelefono.setSecuencia(l);
            nuevoTelefono.setPersona(persona);
            if (nuevoTelefono.getCiudad().getSecuencia() == null) {
                nuevoTelefono.setCiudad(null);
            }
            listTelefonosCrear.add(nuevoTelefono);
            listTelefonos.add(nuevoTelefono);
            telefonoSeleccionado = nuevoTelefono;
            nuevoTelefono = new Telefonos();
            nuevoTelefono.setTipotelefono(new TiposTelefonos());
            nuevoTelefono.setCiudad(new Ciudades());
            getListTelefonos();
            contarRegistrosTelefono();
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTelefono').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTelefono");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoTelefono').show()");
        }
    }

    public void actualizarCiudadTelefono() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                telefonoSeleccionado.setCiudad(ciudadTelefonoSeleccionada);
                if (!listTelefonosCrear.contains(telefonoSeleccionado)) {
                    if (listTelefonosModificar.isEmpty()) {
                        listTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listTelefonosModificar.contains(telefonoSeleccionado)) {
                        listTelefonosModificar.add(telefonoSeleccionado);
                    }
                }
            } else {
                telefonoSeleccionado.setCiudad(ciudadTelefonoSeleccionada);
                if (!listTelefonosCrear.contains(telefonoSeleccionado)) {
                    if (listTelefonosModificar.isEmpty()) {
                        listTelefonosModificar.add(telefonoSeleccionado);
                    } else if (!listTelefonosModificar.contains(telefonoSeleccionado)) {
                        listTelefonosModificar.add(telefonoSeleccionado);
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
            nuevoTelefono.setCiudad(ciudadTelefonoSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTelefono");
        } else if (tipoActualizacion == 2) {
            duplicarTelefono.setCiudad(ciudadTelefonoSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTelefono");
        }
        lovCiudadesFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVCiudadesTelefono:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCiudadesTelefono').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadesTelefonoDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesTelefonoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVCiudadesTelefono");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCT");
    }

    public void cancelarCambioCiudadTelefono() {
        lovCiudadTelefonoFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVCiudadesTelefono:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCiudadesTelefono').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadesTelefonoDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesTelefonoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVCiudadesTelefono");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCT");
    }

//    public void guardarCambiosTelefono() {
//        if (guardado == false) {
//            if (!listTelefonosBorrar.isEmpty()) {
//                for (int i = 0; i < listTelefonosBorrar.size(); i++) {
//                    if (listTelefonosBorrar.get(i).getTipotelefono().getSecuencia() == null) {
//                        listTelefonosBorrar.get(i).setTipotelefono(null);
//                        administrarVigDomiciliarias.borrarTelefonos(listTelefonosBorrar.get(i));
//                    } else if (listTelefonosBorrar.get(i).getCiudad().getSecuencia() == null) {
//                        listTelefonosBorrar.get(i).setCiudad(null);
//                        administrarVigDomiciliarias.borrarTelefonos(listTelefonosBorrar.get(i));
//                    } else {
//                        administrarVigDomiciliarias.borrarTelefonos(listTelefonosBorrar.get(i));
//                    }
//                }
//                listTelefonosBorrar.clear();
//            }
//
//            if (!listTelefonosCrear.isEmpty()) {
//                for (int i = 0; i < listTelefonosCrear.size(); i++) {
//                    System.out.println("Creando...");
//                    if (listTelefonosCrear.get(i).getTipotelefono().getSecuencia() == null) {
//                        listTelefonosCrear.get(i).setTipotelefono(null);
//                        administrarVigDomiciliarias.crearTelefonos(listTelefonosCrear.get(i));
//                    } else if (listTelefonosCrear.get(i).getCiudad().getSecuencia() == null) {
//                        listTelefonosCrear.get(i).setCiudad(null);
//                        administrarVigDomiciliarias.crearTelefonos(listTelefonosCrear.get(i));
//                    } else {
//                        administrarVigDomiciliarias.crearTelefonos(listTelefonosCrear.get(i));
//                    }
//                }
//                listTelefonosCrear.clear();
//            }
//            if (!listTelefonosModificar.isEmpty()) {
//                administrarVigDomiciliarias.modificarTelefonos(listTelefonosModificar);
//                listTelefonosModificar.clear();
//            }
//            listTelefonos = null;
//            getListTelefonos();
//            contarRegistrosCiudadesTelefonos();
//            RequestContext context = RequestContext.getCurrentInstance();
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            k = 0;
//            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("form:growl");
//        }
//        guardado = true;
//        RequestContext.getCurrentInstance().update("form:ACEPTAR");
//        RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
//        permitirIndex = true;
//        telefonoSeleccionado = null;
//    }
    public void salirTelefonos() {
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
            listTelefonosFiltrar = null;
            tipoLista = 0;
        }

        listTelefonosBorrar.clear();
        listTelefonosCrear.clear();
        listTelefonosModificar.clear();
        contarRegistrosTelefono();
        telefonoSeleccionado = null;
        k = 0;
        listTelefonos = null;
        guardado = true;
        permitirIndex = true;
    }

    public void contarRegistroTT() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTT");
    }

    public void contarRegistrosCiudadesTelefonos() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCiudadesTelefono");
    }

//// estados civiles
    public void cambiarIndiceEstadosCiviles(VigenciasEstadosCiviles vigenciaEstadoCivil, int celda) {

        if (permitirIndex == true) {
//            deshabilitarBotonLov();
            vigenciaEstadoCivilSeleccionado = vigenciaEstadoCivil;
            cualCelda = celda;
            cualtabla = 3;
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
//        cualtabla = -1;
    }

    public void asignarIndexEstadosCiviles(VigenciasEstadosCiviles vigenciaEstadoCivil, int LND, int dig) {
        RequestContext context = RequestContext.getCurrentInstance();
        vigenciaEstadoCivilSeleccionado = vigenciaEstadoCivil;
        tipoActualizacion = LND;
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

    public void contarRegistroLovEstadosCiviles() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEstadoCivilLov");
    }

    public void cancelarModificacionEstadosCiviles() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            fecha = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:parentesco");
            parentesco.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
            bandera = 0;
            listVigenciaEstadoCivilFiltrar = null;
            tipoLista = 0;
        }

        listVigenciaEstadoCivilBorrar.clear();
        listVigenciaEstadoCivilCrear.clear();
        listVigenciaEstadoCivilModificar.clear();
        vigenciaEstadoCivilSeleccionado = null;
        k = 0;
        listVigenciaEstadoCivil = null;
        guardado = true;
        permitirIndex = true;
        getListVigenciaEstadoCivil();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistrosEstadoCivil();
        RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salirEstadosCiviles() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            fecha = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:parentesco");
            parentesco.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
            bandera = 0;
            listVigenciaEstadoCivilFiltrar = null;
            tipoLista = 0;
        }

        listVigenciaEstadoCivilBorrar.clear();
        listVigenciaEstadoCivilCrear.clear();
        listVigenciaEstadoCivilModificar.clear();
        vigenciaEstadoCivilSeleccionado = null;
        k = 0;
        listVigenciaEstadoCivil = null;
        guardado = true;
        permitirIndex = true;
        getListVigenciaEstadoCivil();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistrosEstadoCivil();
        RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void actualizarEstadoCivil() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaEstadoCivilSeleccionado.setEstadocivil(estadoCivilSeleccionado);

                if (!listVigenciaEstadoCivilCrear.contains(vigenciaEstadoCivilSeleccionado)) {
                    if (listVigenciaEstadoCivilModificar.isEmpty()) {
                        listVigenciaEstadoCivilModificar.add(vigenciaEstadoCivilSeleccionado);
                    } else if (!listVigenciaEstadoCivilModificar.contains(vigenciaEstadoCivilSeleccionado)) {
                        listVigenciaEstadoCivilModificar.add(vigenciaEstadoCivilSeleccionado);
                    }
                }
            } else {
                vigenciaEstadoCivilSeleccionado.setEstadocivil(estadoCivilSeleccionado);

                if (!listVigenciaEstadoCivilCrear.contains(vigenciaEstadoCivilSeleccionado)) {
                    if (listVigenciaEstadoCivilModificar.isEmpty()) {
                        listVigenciaEstadoCivilModificar.add(vigenciaEstadoCivilSeleccionado);
                    } else if (!listVigenciaEstadoCivilModificar.contains(vigenciaEstadoCivilSeleccionado)) {
                        listVigenciaEstadoCivilModificar.add(vigenciaEstadoCivilSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaEstadoCivil.setEstadocivil(estadoCivilSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreEstadoCivil");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaEstadoCivil.setEstadocivil(estadoCivilSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstadoCivil");
        }
        listVigenciaEstadoCivilFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:lovEstadosCiviles:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstadosCiviles').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:EstadoCivilDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEstadosCiviles");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarS");
    }

    public void cancelarCambioEstadoCivil() {
        listVigenciaEstadoCivilFiltrar = null;
        estadoCivilSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEstadosCiviles:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstadosCiviles').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:EstadoCivilDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEstadosCiviles");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarS");

    }

    public void mostrarDialogoEstadosCiviles(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:EstadoCivilDialogo");
        RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
    }

    public void limpiarNuevoEstadoCivil() {
        System.out.println("\n ENTRE A LIMPIAR NUEVO ESTADO CIVIL  \n");
        nuevaVigenciaEstadoCivil = new VigenciasEstadosCiviles();
        nuevaVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
        vigenciaEstadoCivilSeleccionado = null;
    }

    public void borrandoVigenciasEstadosCiviles() {

        if (vigenciaEstadoCivilSeleccionado != null) {
            System.out.println("Entro a borrandoEvalCompetencias");
            if (!listVigenciaEstadoCivilModificar.isEmpty() && listVigenciaEstadoCivilModificar.contains(vigenciaEstadoCivilSeleccionado)) {
                int modIndex = listVigenciaEstadoCivilModificar.indexOf(vigenciaEstadoCivilSeleccionado);
                listVigenciaEstadoCivilModificar.remove(modIndex);
                listVigenciaEstadoCivilBorrar.add(vigenciaEstadoCivilSeleccionado);
            } else if (!listVigenciaEstadoCivilCrear.isEmpty() && listVigenciaEstadoCivilCrear.contains(vigenciaEstadoCivilSeleccionado)) {
                int crearIndex = listVigenciaEstadoCivilCrear.indexOf(vigenciaEstadoCivilSeleccionado);
                listVigenciaEstadoCivilCrear.remove(crearIndex);
            } else {
                listVigenciaEstadoCivilBorrar.add(vigenciaEstadoCivilSeleccionado);
            }
            listVigenciaEstadoCivil.remove(vigenciaEstadoCivilSeleccionado);
            if (tipoLista == 1) {
                listVigenciaEstadoCivilFiltrar.remove(vigenciaEstadoCivilSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
            contarRegistrosEstadoCivil();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            vigenciaEstadoCivilSeleccionado = null;
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

//    public void guardarVigenciaEstadoCivil() {
//        RequestContext context = RequestContext.getCurrentInstance();
//
//        if (guardado == false) {
//            System.out.println("Realizando guardarEvalCompetencias");
//            if (!listVigenciaEstadoCivilBorrar.isEmpty()) {
//                for (int i = 0; i < listVigenciaEstadoCivilBorrar.size(); i++) {
//                    System.out.println("Borrando...");
//                }
//                administrarVigDomiciliarias.borrarVigenciasEstadosCiviles(listVigenciaEstadoCivilBorrar);
//                //mostrarBorrados
//                listVigenciaEstadoCivilBorrar.clear();
//            }
//            if (!listVigenciaEstadoCivilCrear.isEmpty()) {
//                administrarVigDomiciliarias.crearVigenciasEstadosCiviles(listVigenciaEstadoCivilCrear);
//                listVigenciaEstadoCivilCrear.clear();
//            }
//            if (!listVigenciaEstadoCivilModificar.isEmpty()) {
//                System.out.println("Modificando...");
//                administrarVigDomiciliarias.modificarVigenciasEstadosCiviles(listVigenciaEstadoCivilModificar);
//                listVigenciaEstadoCivilModificar.clear();
//            }
//            listVigenciaEstadoCivil = null;
//            getListVigenciaEstadoCivil();
//            contarRegistrosEstadoCivil();
//            RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
//            k = 0;
//            guardado = true;
//            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("form:growl");
//        }
//        vigenciaEstadoCivilSeleccionado = null;
//        RequestContext.getCurrentInstance().update("form:ACEPTAR");
//
//    }
    public void agregarNuevoVigenciaEstadoCivil() {
        System.out.println("agregarNuevoVigenciaEstadoCivil");
        int contador = 0;
        //nuevaVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
        Short a = 0;
        a = null;
        int fechas = 0;
        mensajeValidacion = " ";
        nuevaVigenciaEstadoCivil.setPersona(persona);
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaVigenciaEstadoCivil.getFechavigencia() == null || nuevaVigenciaEstadoCivil.getFechavigencia().equals("")) {
            mensajeValidacion = "Campo Fecha vacío \n";
        } else {
            for (int i = 0; i < listVigenciaEstadoCivil.size(); i++) {
                if (nuevaVigenciaEstadoCivil.getFechavigencia().equals(listVigenciaEstadoCivil.get(i).getFechavigencia())) {
                    fechas++;
                }
            }
            if (fechas > 0) {
                mensajeValidacion = "Fechas repetidas ";
            } else {
                contador++;
            }
        }
        if (nuevaVigenciaEstadoCivil.getEstadocivil().getSecuencia() == null || nuevaVigenciaEstadoCivil.getEstadocivil().getDescripcion().isEmpty()) {
            mensajeValidacion = "Campo Estado Civil vacío\n";
        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                System.out.println("Desactivar");
                fecha = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:fecha");
                fecha.setFilterStyle("display: none; visibility: hidden;");
                parentesco = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:parentesco");
                parentesco.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "270";
                RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
                bandera = 0;
                listVigenciaEstadoCivilFiltrar = null;
                tipoLista = 0;
            }

            k++;
            l = BigInteger.valueOf(k);
            nuevaVigenciaEstadoCivil.setSecuencia(l);
            listVigenciaEstadoCivilCrear.add(nuevaVigenciaEstadoCivil);
            listVigenciaEstadoCivil.add(nuevaVigenciaEstadoCivil);
            contarRegistrosEstadoCivil();
            vigenciaEstadoCivilSeleccionado = nuevaVigenciaEstadoCivil;
            nuevaVigenciaEstadoCivil = new VigenciasEstadosCiviles();
            nuevaVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
            RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEstadoCivil').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoEstadoCivil");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoEstadoCivil').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoVigenciaEstadoCivil() {
        nuevaVigenciaEstadoCivil = new VigenciasEstadosCiviles();
        nuevaVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
        nuevaVigenciaEstadoCivil.setFechavigencia(new Date());
    }

    public void duplicandoVigenciaEstadoCivil() {
        if (vigenciaEstadoCivilSeleccionado != null) {
            duplicarVigenciaEstadoCivil = new VigenciasEstadosCiviles();
            duplicarVigenciaEstadoCivil.setPersona(new Personas());
            duplicarVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarVigenciaEstadoCivil.setSecuencia(l);
                duplicarVigenciaEstadoCivil.setPersona(vigenciaEstadoCivilSeleccionado.getPersona());
                duplicarVigenciaEstadoCivil.setFechavigencia(vigenciaEstadoCivilSeleccionado.getFechavigencia());
                duplicarVigenciaEstadoCivil.setEstadocivil(vigenciaEstadoCivilSeleccionado.getEstadocivil());
            }
            if (tipoLista == 1) {
                duplicarVigenciaEstadoCivil.setSecuencia(l);
                duplicarVigenciaEstadoCivil.setPersona(vigenciaEstadoCivilSeleccionado.getPersona());
                duplicarVigenciaEstadoCivil.setFechavigencia(vigenciaEstadoCivilSeleccionado.getFechavigencia());
                duplicarVigenciaEstadoCivil.setEstadocivil(vigenciaEstadoCivilSeleccionado.getEstadocivil());
//                altoTabla = "270";
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEvC");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEstadoCivil').show()");

        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarVigenciaEstadoCivil() {
        int contador = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        int fechas = 0;
        a = null;
        if (duplicarVigenciaEstadoCivil.getFechavigencia() == null) {
            mensajeValidacion = mensajeValidacion + "   * Fecha \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {

            for (int j = 0; j < listVigenciaEstadoCivil.size(); j++) {
                if (duplicarVigenciaEstadoCivil.getFechavigencia().equals(listVigenciaEstadoCivil.get(j).getFechavigencia())) {
                    System.out.println("Se repiten");
                    fechas++;
                }
            }
            if (fechas > 0) {
                mensajeValidacion = "FECHAS REPETIDAS";
            } else {
                System.out.println("bandera");
                contador++;
            }

        }
        if (duplicarVigenciaEstadoCivil.getEstadocivil().getDescripcion() == null || duplicarVigenciaEstadoCivil.getEstadocivil().getDescripcion().isEmpty() || duplicarVigenciaEstadoCivil.getEstadocivil().getDescripcion().equals(" ") || duplicarVigenciaEstadoCivil.getEstadocivil().getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   * Estado Civil \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("bandera");
            contador++;

        }
        if (duplicarVigenciaEstadoCivil.getPersona().getSecuencia() == null) {
            duplicarVigenciaEstadoCivil.setPersona(persona);
        }
        if (contador == 2) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarVigenciaEstadoCivil.setSecuencia(l);
            if (listVigenciaEstadoCivilCrear.contains(duplicarVigenciaEstadoCivil)) {
                System.out.println("Ya lo contengo.");
            }
            listVigenciaEstadoCivil.add(duplicarVigenciaEstadoCivil);
            listVigenciaEstadoCivilCrear.add(duplicarVigenciaEstadoCivil);
            RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
            vigenciaEstadoCivilSeleccionado = duplicarVigenciaEstadoCivil;
            if (guardado == true) {
                guardado = false;
            }
            contarRegistrosEstadoCivil();
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                fecha = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:fecha");
                fecha.setFilterStyle("display: none; visibility: hidden;");
                parentesco = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:parentesco");
                parentesco.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "270";
                RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
                bandera = 0;
                listVigenciaEstadoCivilFiltrar = null;
                tipoLista = 0;
            }
            duplicarVigenciaEstadoCivil = new VigenciasEstadosCiviles();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEstadoCivil').hide()");

        } else {
            contador = 0;
            fechas = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void limpiarDuplicarVigenciaEstadoCivil() {
        duplicarVigenciaEstadoCivil = new VigenciasEstadosCiviles();
        duplicarVigenciaEstadoCivil.setPersona(new Personas());
        duplicarVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
    }

//////nivel academico
    public void cambiarIndiceEducacion(VigenciasFormales vigenciaformal, int celda) {
        if (permitirIndex == true) {
            vigenciaFormalSeleccionada = vigenciaformal;
            cualCelda = celda;
            cualtabla = 6;
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
//        cualtabla = -1;
    }

    public void asignarIndexEducacion(VigenciasFormales vigenciaFormal, int dlg, int LND) {
        vigenciaFormalSeleccionada = vigenciaFormal;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 0) {
            contarRegistrosTipoEducacion();
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposEducacionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposEducacionesDialogo').show()");
        } else if (dlg == 1) {
            contarRegistrosProfesion();
            RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').show()");
        } else if (dlg == 2) {
            contarRegistrosInstituciones();
            RequestContext.getCurrentInstance().update("formularioDialogos:institucionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').show()");
        } else if (dlg == 3) {
            contarRegistrosAdiestramiento();
            RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosFDialogo");
            RequestContext.getCurrentInstance().execute("PF('adiestramientosFDialogo').show()");
        }
    }

    public void modificarEducacion(VigenciasFormales vigenciaformal) {
        vigenciaFormalSeleccionada = vigenciaformal;
        if (tipoLista == 0) {
            if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                if (listVigenciasFormalesModificar.isEmpty()) {
                    listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                    listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext context = RequestContext.getCurrentInstance();
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
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
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

    public void actualizarTiposEducaciones() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.setTipoeducacion(tipoEducacionSeleccionado);
                if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listVigenciasFormalesModificar.isEmpty()) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            } else {
                vigenciaFormalSeleccionada.setTipoeducacion(tipoEducacionSeleccionado);
                if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listVigenciasFormalesModificar.isEmpty()) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormal.setTipoeducacion(tipoEducacionSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormal.setTipoeducacion(tipoEducacionSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormal");
        }
        lovTiposEducacionesFiltrar = null;
        tipoEducacionSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVTiposEducaciones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposEducaciones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposEducacionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposEducacionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposEducaciones");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTE");
    }

    public void cancelarCambioTiposEducaciones() {
        lovTiposEducacionesFiltrar = null;
        tipoEducacionSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTiposEducaciones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposEducaciones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposEducacionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposEducacionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposEducaciones");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTE");
    }

    public void actualizarProfesiones() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.setProfesion(profesionSeleccionada);
                if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listVigenciasFormalesModificar.isEmpty()) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            } else {
                vigenciaFormalSeleccionada.setProfesion(profesionSeleccionada);
                if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listVigenciasFormalesModificar.isEmpty()) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormal.setProfesion(profesionSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormal.setProfesion(profesionSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormal");
        }
        lovProfesionesFiltrar = null;
        profesionSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVProfesiones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVProfesiones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVProfesiones");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
    }

    public void cancelarCambioProfesiones() {
        lovProfesionesFiltrar = null;
        profesionSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVProfesiones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVProfesiones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVProfesiones");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
    }

    public void actualizarInstituciones() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.setInstitucion(institucionSeleccionada);
                if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listVigenciasFormalesModificar.isEmpty()) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            } else {
                vigenciaFormalSeleccionada.setInstitucion(institucionSeleccionada);
                if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listVigenciasFormalesModificar.isEmpty()) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormal.setInstitucion(institucionSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormal.setInstitucion(institucionSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormal");
        }
        lovInstitucionesFiltrar = null;
        institucionSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVInstituciones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVInstituciones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:institucionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVInstituciones");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarI");
    }

    public void cancelarCambioInstituciones() {
        lovInstitucionesFiltrar = null;
        institucionSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVInstituciones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVInstituciones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:institucionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVInstituciones");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarI");
    }

    public void cancelarCambioAdiestramientoF() {
        lovAdiestramientosFiltrar = null;
        adiestramientoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVAdiestramientosF:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVAdiestramientosF').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('adiestramientosFDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosFDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVAdiestramientosF");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarAF");
    }

    public void actualizarAdiestramientoF() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.setAdiestramientof(adiestramientoSeleccionado);
                if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listVigenciasFormalesModificar.isEmpty()) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            } else {
                vigenciaFormalSeleccionada.setAdiestramientof(adiestramientoSeleccionado);
                if (!listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listVigenciasFormalesModificar.isEmpty()) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormal.setAdiestramientof(adiestramientoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormal.setAdiestramientof(adiestramientoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormal");
        }
        lovAdiestramientosFiltrar = null;
        adiestramientoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVAdiestramientosF:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVAdiestramientosF').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('adiestramientosFDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosFDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVAdiestramientosF");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarAF");
    }

    public void agregarNuevaVigenciaFormal() {
        int pasa = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaVigenciaFormal.getFechavigencia() == null) {
            mensajeValidacion = " * Fecha \n";
            pasa++;
        }
        if (nuevaVigenciaFormal.getTipoeducacion().getSecuencia() == null) {
            System.out.println("Entro a TipoEducacion");
            mensajeValidacion = mensajeValidacion + " * Tipo de Educacion\n";
            pasa++;
        }
        if (nuevaVigenciaFormal.getProfesion().getSecuencia() == null) {
            System.out.println("Entro a Profesion");
            mensajeValidacion = mensajeValidacion + " * Profesion\n";
            pasa++;
        }
        if (nuevaVigenciaFormal.getInstitucion().getSecuencia() == null) {
            System.out.println("Entro a Institucion");
            mensajeValidacion = mensajeValidacion + " * Institucion \n";
            pasa++;
        }
        if (nuevaVigenciaFormal.getAdiestramientof().getSecuencia() == null) {
            System.out.println("Entro a AdiestramientoF");
            mensajeValidacion = mensajeValidacion + " * Adiestramiento\n";
            pasa++;
        }

        if (pasa == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                pEFechas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechas");
                pEFechas.setFilterStyle("display: none; visibility: hidden;");
                pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
                pETiposEducaciones.setFilterStyle("display: none; visibility: hidden;");
                pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
                pEProfesiones.setFilterStyle("display: none; visibility: hidden;");
                pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
                pEInstituciones.setFilterStyle("display: none; visibility: hidden;");
                pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
                pEAdiestramientosF.setFilterStyle("display: none; visibility: hidden;");
                pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
                pECalificaciones.setFilterStyle("display: none; visibility: hidden;");
                pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
                pENumerosTarjetas.setFilterStyle("display: none; visibility: hidden;");
                pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
                pEFechasExpediciones.setFilterStyle("display: none; visibility: hidden;");
                pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
                pEFechasVencimientos.setFilterStyle("display: none; visibility: hidden;");
                pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
                pEObservaciones.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
                bandera = 0;
                listVigenciasFormalesFiltrar = null;
                tipoLista = 0;

            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaVigenciaFormal.setSecuencia(l);
            nuevaVigenciaFormal.setPersona(persona);
            listVigenciasFormalesCrear.add(nuevaVigenciaFormal);
            listVigenciasFormales.add(nuevaVigenciaFormal);
            contarRegistrosEducacion();
            vigenciaFormalSeleccionada = nuevaVigenciaFormal;
            nuevaVigenciaFormal = new VigenciasFormales();
            nuevaVigenciaFormal.setTipoeducacion(new TiposEducaciones());
            nuevaVigenciaFormal.setProfesion(new Profesiones());
            nuevaVigenciaFormal.setInstitucion(new Instituciones());
            nuevaVigenciaFormal.setAdiestramientof(new AdiestramientosF());
            nuevaVigenciaFormal.setFechavigencia(new Date());
            nuevaVigenciaFormal.setFechavencimientotarjeta(new Date());
            nuevaVigenciaFormal.setFechaexpediciontarjeta(new Date());
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaFormal').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaVigenciaFormal");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaVigenciaFormal').show()");
        }
    }

    public void borrarVigenciaFormal() {

        if (vigenciaFormalSeleccionada != null) {
            if (!listVigenciasFormalesModificar.isEmpty() && listVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                int modIndex = listVigenciasFormalesModificar.indexOf(vigenciaFormalSeleccionada);
                listVigenciasFormalesModificar.remove(modIndex);
                listVigenciasFormalesBorrar.add(vigenciaFormalSeleccionada);
            } else if (!listVigenciasFormalesCrear.isEmpty() && listVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                int crearIndex = listVigenciasFormalesCrear.indexOf(vigenciaFormalSeleccionada);
                listVigenciasFormalesCrear.remove(crearIndex);
            } else {
                listVigenciasFormalesBorrar.add(vigenciaFormalSeleccionada);
            }
            listVigenciasFormales.remove(vigenciaFormalSeleccionada);
            if (tipoLista == 1) {
                listVigenciasFormalesFiltrar.remove(vigenciaFormalSeleccionada);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistrosEducacion();
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            vigenciaFormalSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void duplicarVF() {
        if (vigenciaFormalSeleccionada != null) {
            duplicarVigenciaFormal = new VigenciasFormales();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarVigenciaFormal.setSecuencia(l);
                duplicarVigenciaFormal.setFechavigencia(vigenciaFormalSeleccionada.getFechavigencia());
                duplicarVigenciaFormal.setTipoeducacion(vigenciaFormalSeleccionada.getTipoeducacion());
                duplicarVigenciaFormal.setProfesion(vigenciaFormalSeleccionada.getProfesion());
                duplicarVigenciaFormal.setInstitucion(vigenciaFormalSeleccionada.getInstitucion());
                duplicarVigenciaFormal.setAdiestramientof(vigenciaFormalSeleccionada.getAdiestramientof());
                duplicarVigenciaFormal.setCalificacionobtenida(vigenciaFormalSeleccionada.getCalificacionobtenida());
                duplicarVigenciaFormal.setNumerotarjeta(vigenciaFormalSeleccionada.getNumerotarjeta());
                duplicarVigenciaFormal.setFechaexpediciontarjeta(vigenciaFormalSeleccionada.getFechaexpediciontarjeta());
                duplicarVigenciaFormal.setFechavencimientotarjeta(vigenciaFormalSeleccionada.getFechavencimientotarjeta());
                duplicarVigenciaFormal.setObservacion(vigenciaFormalSeleccionada.getObservacion());
            }
            if (tipoLista == 1) {
                duplicarVigenciaFormal.setSecuencia(l);
                duplicarVigenciaFormal.setFechavigencia(vigenciaFormalSeleccionada.getFechavigencia());
                duplicarVigenciaFormal.setTipoeducacion(vigenciaFormalSeleccionada.getTipoeducacion());
                duplicarVigenciaFormal.setProfesion(vigenciaFormalSeleccionada.getProfesion());
                duplicarVigenciaFormal.setInstitucion(vigenciaFormalSeleccionada.getInstitucion());
                duplicarVigenciaFormal.setAdiestramientof(vigenciaFormalSeleccionada.getAdiestramientof());
                duplicarVigenciaFormal.setCalificacionobtenida(vigenciaFormalSeleccionada.getCalificacionobtenida());
                duplicarVigenciaFormal.setNumerotarjeta(vigenciaFormalSeleccionada.getNumerotarjeta());
                duplicarVigenciaFormal.setFechaexpediciontarjeta(vigenciaFormalSeleccionada.getFechaexpediciontarjeta());
                duplicarVigenciaFormal.setFechavencimientotarjeta(vigenciaFormalSeleccionada.getFechavencimientotarjeta());
                duplicarVigenciaFormal.setObservacion(vigenciaFormalSeleccionada.getObservacion());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormal");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaFormal').show()");

        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarEducacion() {

        listVigenciasFormales.add(duplicarVigenciaFormal);
        listVigenciasFormalesCrear.add(duplicarVigenciaFormal);
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistrosEducacion();
        RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
        vigenciaFormalSeleccionada = null;
        vigenciaFormalSeleccionada = null;
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            pEFechas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechas");
            pEFechas.setFilterStyle("display: none; visibility: hidden;");
            pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
            pETiposEducaciones.setFilterStyle("display: none; visibility: hidden;");
            pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
            pEProfesiones.setFilterStyle("display: none; visibility: hidden;");
            pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
            pEInstituciones.setFilterStyle("display: none; visibility: hidden;");
            pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
            pEAdiestramientosF.setFilterStyle("display: none; visibility: hidden;");
            pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
            pECalificaciones.setFilterStyle("display: none; visibility: hidden;");
            pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
            pENumerosTarjetas.setFilterStyle("display: none; visibility: hidden;");
            pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
            pEFechasExpediciones.setFilterStyle("display: none; visibility: hidden;");
            pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
            pEFechasVencimientos.setFilterStyle("display: none; visibility: hidden;");
            pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
            pEObservaciones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            bandera = 0;
            listVigenciasFormalesFiltrar = null;
            tipoLista = 0;

        }
        duplicarVigenciaFormal = new VigenciasFormales();
    }

    public void limpiarduplicarVigenciaFormal() {
        duplicarVigenciaFormal = new VigenciasFormales();
    }

    public void salirEducacion() {

        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            pEFechas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechas");
            pEFechas.setFilterStyle("display: none; visibility: hidden;");
            pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
            pETiposEducaciones.setFilterStyle("display: none; visibility: hidden;");
            pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
            pEProfesiones.setFilterStyle("display: none; visibility: hidden;");
            pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
            pEInstituciones.setFilterStyle("display: none; visibility: hidden;");
            pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
            pEAdiestramientosF.setFilterStyle("display: none; visibility: hidden;");
            pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
            pECalificaciones.setFilterStyle("display: none; visibility: hidden;");
            pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
            pENumerosTarjetas.setFilterStyle("display: none; visibility: hidden;");
            pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
            pEFechasExpediciones.setFilterStyle("display: none; visibility: hidden;");
            pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
            pEFechasVencimientos.setFilterStyle("display: none; visibility: hidden;");
            pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
            pEObservaciones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            bandera = 0;
            listVigenciasFormalesFiltrar = null;
            tipoLista = 0;
        }

        listVigenciasFormalesBorrar.clear();
        listVigenciasFormalesCrear.clear();
        listVigenciasFormalesModificar.clear();
        vigenciaFormalSeleccionada = null;
        listVigenciasFormales = null;
        getListVigenciasFormales();
        contarRegistrosEducacion();
        //  k = 0;
        guardado = true;
        permitirIndex = true;
    }

    public void cancelarModificacionEducacion() {

        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            pEFechas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechas");
            pEFechas.setFilterStyle("display: none; visibility: hidden;");
            pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
            pETiposEducaciones.setFilterStyle("display: none; visibility: hidden;");
            pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
            pEProfesiones.setFilterStyle("display: none; visibility: hidden;");
            pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
            pEInstituciones.setFilterStyle("display: none; visibility: hidden;");
            pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
            pEAdiestramientosF.setFilterStyle("display: none; visibility: hidden;");
            pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
            pECalificaciones.setFilterStyle("display: none; visibility: hidden;");
            pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
            pENumerosTarjetas.setFilterStyle("display: none; visibility: hidden;");
            pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
            pEFechasExpediciones.setFilterStyle("display: none; visibility: hidden;");
            pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
            pEFechasVencimientos.setFilterStyle("display: none; visibility: hidden;");
            pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
            pEObservaciones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            bandera = 0;
            listVigenciasFormalesFiltrar = null;
            tipoLista = 0;
        }

        listVigenciasFormalesBorrar.clear();
        listVigenciasFormalesCrear.clear();
        listVigenciasFormalesModificar.clear();
        vigenciaFormalSeleccionada = null;
        listVigenciasFormales = null;
        getListVigenciasFormales();
        contarRegistrosEducacion();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
    }

//    public void guardarVigenciasFormales() {
//        try {
//            RequestContext context = RequestContext.getCurrentInstance();
//            if (!listVigenciasFormalesBorrar.isEmpty()) {
//                administrarVigDomiciliarias.borrarVigenciaFormal(listVigenciasFormalesBorrar);
//                listVigenciasFormalesBorrar.clear();
//            }
//            if (!listVigenciasFormalesCrear.isEmpty()) {
//                administrarVigDomiciliarias.crearVigenciaFormal(listVigenciasFormalesCrear);
//                listVigenciasFormalesCrear.clear();
//            }
//            if (!listVigenciasFormalesModificar.isEmpty()) {
//                administrarVigDomiciliarias.modificarVigenciaFormal(listVigenciasFormalesModificar);
//                listVigenciasFormalesModificar.clear();
//            }
//            listVigenciasFormales = null;
//            getListVigenciasFormales();
//            contarRegistrosEducacion();
//            vigenciaFormalSeleccionada = null;
//            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
//            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("form:growl");
//            guardado = true;
//            permitirIndex = true;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//        } catch (Exception e) {
//            System.out.println("Error guardarVigenciasNoFormales  Controlador : " + e.toString());
//            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Educación Formal, Por favor intente nuevamente.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("form:growl");
//        }
//    }
    public void limpiarNuevaVigenciaFormal() {
        nuevaVigenciaFormal = new VigenciasFormales();
        nuevaVigenciaFormal.setTipoeducacion(new TiposEducaciones());
        nuevaVigenciaFormal.setProfesion(new Profesiones());
        nuevaVigenciaFormal.setInstitucion(new Instituciones());
        nuevaVigenciaFormal.setAdiestramientof(new AdiestramientosF());
        nuevaVigenciaFormal.setFechavigencia(new Date());
        nuevaVigenciaFormal.setFechavencimientotarjeta(new Date());
        nuevaVigenciaFormal.setFechaexpediciontarjeta(new Date());
    }

    public void valoresBackupAutocompletarEducacion(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPOEDUCACION")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaFormal.getTipoeducacion().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarVigenciaFormal.getTipoeducacion().getNombre();
            } else if (Campo.equals("PROFESION")) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormal.getProfesion().getDescripcion();
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormal.getProfesion().getDescripcion();
                }
            } else if (Campo.equals("INSTITUCION")) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormal.getInstitucion().getDescripcion();
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormal.getInstitucion().getDescripcion();
                }
            } else if (Campo.equals("ADIESTRAMIENTOF")) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormal.getAdiestramientof().getDescripcion();
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormal.getAdiestramientof().getDescripcion();
                }
            }
        }
    }

    public void autocompletarNuevoyDuplicadoEducacion(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOEDUCACION")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaFormal.getTipoeducacion().setNombre(nuevaVigenciaFormal.getTipoeducacion().getNombre());
            } else if (tipoNuevo == 2) {
                duplicarVigenciaFormal.getTipoeducacion().setNombre(duplicarVigenciaFormal.getTipoeducacion().getNombre());
            }
            for (int i = 0; i < lovTiposEducaciones.size(); i++) {
                if (lovTiposEducaciones.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormal.setTipoeducacion(lovTiposEducaciones.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoEducacion");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormal.setTipoeducacion(lovTiposEducaciones.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEducacion");
                }
                lovTiposEducaciones.clear();
                getLovTiposEducaciones();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposEducacionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposEducacionesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoEducacion");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEducacion");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("PROFESION")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaFormal.getProfesion().setDescripcion(nuevaVigenciaFormal.getProfesion().getDescripcion());
            } else if (tipoNuevo == 2) {
                duplicarVigenciaFormal.getProfesion().setDescripcion(duplicarVigenciaFormal.getProfesion().getDescripcion());
            }
            for (int i = 0; i < lovProfesiones.size(); i++) {
                if (lovProfesiones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormal.setProfesion(lovProfesiones.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProfesion");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormal.setProfesion(lovProfesiones.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProfesion");
                }
                lovProfesiones.clear();
                getLovProfesiones();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProfesion");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProfesion");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("INSTITUCION")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaFormal.getInstitucion().setDescripcion(nuevaVigenciaFormal.getInstitucion().getDescripcion());
            } else if (tipoNuevo == 2) {
                duplicarVigenciaFormal.getInstitucion().setDescripcion(duplicarVigenciaFormal.getInstitucion().getDescripcion());
            }
            for (int i = 0; i < lovInstituciones.size(); i++) {
                if (lovInstituciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormal.setInstitucion(lovInstituciones.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaInstitucion");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormal.setInstitucion(lovInstituciones.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarInstitucion");
                }
                lovInstituciones.clear();
                getLovInstituciones();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:institucionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaInstitucion");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarInstitucion");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("ADIESTRAMIENTOF")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaFormal.getAdiestramientof().setDescripcion(nuevaVigenciaFormal.getAdiestramientof().getDescripcion());
            } else if (tipoNuevo == 2) {
                duplicarVigenciaFormal.getAdiestramientof().setDescripcion(duplicarVigenciaFormal.getAdiestramientof().getDescripcion());
            }
            for (int i = 0; i < lovAdiestramientos.size(); i++) {
                if (lovAdiestramientos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormal.setAdiestramientof(lovAdiestramientos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAdiestramientoF");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormal.setAdiestramientof(lovAdiestramientos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAdiestramientoF");
                }
                lovAdiestramientos.clear();
                getLovAdiestramientos();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosFDialogo");
                RequestContext.getCurrentInstance().execute("PF('adiestramientosFDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAdiestramientoF");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAdiestramientoF");
                }
            }
        }
    }

    public void eventoFiltrarEducacion() {
        contarRegistrosEducacion();
    }

    public void contarRegistrosEducacion() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEducacion");
    }

    public void contarRegistrosAdiestramiento() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroAdiestramiento");

    }

    public void contarRegistrosInstituciones() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroInstitucion");

    }

    public void contarRegistrosProfesion() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroProfesion");

    }

    public void contarRegistrosTipoEducacion() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTipoEducacion");

    }

    //////////experiencia Laboral//////
    public void cambiarIndiceExperiencia(HvExperienciasLaborales experienciaLaboral, int celda) {
        if (permitirIndex == true) {
            hvexpSeleccionada = experienciaLaboral;
            cualCelda = celda;
            cualtabla = 7;
//                    fechaFin = hvexpSeleccionada.getFechahasta();
            if (cualCelda == 0) {
                hvexpSeleccionada.getEmpresa();
                cualCelda = -1;
            } else if (cualCelda == 1) {
                hvexpSeleccionada.getFechadesde();
                cualCelda = -1;
            } else if (cualCelda == 2) {
                hvexpSeleccionada.getFechahasta();
                cualCelda = -1;
            } else if (cualCelda == 3) {
                hvexpSeleccionada.getCargo();
                cualCelda = -1;
            } else if (cualCelda == 4) {
                hvexpSeleccionada.getJefeinmediato();
                cualCelda = -1;
            } else if (cualCelda == 5) {
                hvexpSeleccionada.getTelefono();
                cualCelda = -1;
            } else if (cualCelda == 6) {
                hvexpSeleccionada.getSectoreconomico().getDescripcion();
                cualCelda = -1;
            } else if (cualCelda == 7) {
                hvexpSeleccionada.getMotivoretiro().getNombre();
                cualCelda = -1;
            }
        }
//        cualtabla = -1;
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
            contarRegistroSector();
            RequestContext.getCurrentInstance().update("formularioDialogos:SectorDialogo");
            RequestContext.getCurrentInstance().execute("PF('SectorDialogo').show()");
        } else if (dlg == 2) {
            contarRegistroMotivo();
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

    public void valoresBackupAutocompletarExperiencia(int tipoNuevo, String Campo) {

        if (Campo.equals("SECTORES")) {
            if (tipoNuevo == 1) {
                nuevahvexp.getSectoreconomico().getDescripcion();
            } else if (tipoNuevo == 2) {
                duplicarhvexp.getSectoreconomico().getDescripcion();
            }
        } else if (Campo.equals("MOTIVOS")) {
            if (tipoNuevo == 1) {
                nuevahvexp.getMotivoretiro().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarhvexp.getMotivoretiro().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoExperiencia(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("SECTORES")) {
            if (tipoNuevo == 1) {
                nuevahvexp.getSectoreconomico().setDescripcion(nuevahvexp.getSectoreconomico().getDescripcion());
            } else if (tipoNuevo == 2) {
                duplicarhvexp.getSectoreconomico().setDescripcion(duplicarhvexp.getSectoreconomico().getDescripcion());
            }
            for (int i = 0; i < lovSectoresEconomicos.size(); i++) {
                if (lovSectoresEconomicos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevahvexp.setSectoreconomico(lovSectoresEconomicos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSectorEP");
                } else if (tipoNuevo == 2) {
                    duplicarhvexp.setSectoreconomico(lovSectoresEconomicos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSectorEP");
                }
                lovSectoresEconomicos = null;
                getLovSectoresEconomicos();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:SectorDialogo");
                RequestContext.getCurrentInstance().execute("PF('SectorDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSectorEP");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSectorEP");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("MOTIVOS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevahvexp.getMotivoretiro().setNombre(nuevahvexp.getMotivoretiro().getNombre());
                } else if (tipoNuevo == 2) {
                    duplicarhvexp.getMotivoretiro().setNombre(duplicarhvexp.getMotivoretiro().getNombre());
                }
                for (int i = 0; i < lovMotivos.size(); i++) {
                    if (lovMotivos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevahvexp.setMotivoretiro(lovMotivos.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoEP");
                    } else if (tipoNuevo == 2) {
                        duplicarhvexp.setMotivoretiro(lovMotivos.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoEP");
                    }
                    lovMotivos = null;
                    getLovMotivos();
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:MotivosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivosDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoEP");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoEP");
                    }
                }
            } else {
                lovMotivos = null;
                getLovMotivos();
                if (tipoNuevo == 1) {
                    nuevahvexp.setMotivoretiro(new MotivosRetiros());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoEP");
                } else if (tipoNuevo == 2) {
                    duplicarhvexp.setMotivoretiro(new MotivosRetiros());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoEP");
                }
            }
        }
    }

//    public void guardarCambiosExpLaboral() {
//        RequestContext context = RequestContext.getCurrentInstance();
//        try {
//            if (guardado == false) {
//                if (!listhvExpLaboralesBorrar.isEmpty()) {
//                    administrarVigDomiciliarias.borrarExperienciaLaboral(listhvExpLaboralesBorrar);
//                    listhvExpLaboralesBorrar.clear();
//                }
//                if (!listhvExpLaboralesCrear.isEmpty()) {
//                    administrarVigDomiciliarias.crearExperienciaLaboral(listhvExpLaboralesCrear);
//                    listhvExpLaboralesCrear.clear();
//                }
//                if (!listhvExpLaboralesModificar.isEmpty()) {
//                    administrarVigDomiciliarias.editarExperienciaLaboral(listhvExpLaboralesModificar);
//                    listhvExpLaboralesModificar.clear();
//                }
//                listhvExpLaborales = null;
//                getListhvExpLaborales();
//                RequestContext.getCurrentInstance().update("form:ACEPTAR");
//                k = 0;
//                FacesMessage msg = new FacesMessage("Información", "Se gurdaron los datos con éxito");
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//                RequestContext.getCurrentInstance().update("form:growl");
//                contarRegistrosExp();
//                hvexpSeleccionada = null;
//            }
//            guardado = true;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            RequestContext.getCurrentInstance().update("form:datosExperiencia");
//        } catch (Exception e) {
//            System.out.println("Error guardarCambios : " + e.toString());
//            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("form:growl");
//        }
//    }
    public void cancelarModificacionExpLaboral() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "190";
            expEmpresa = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expEmpresa");
            expEmpresa.setFilterStyle("display: none; visibility: hidden;");
            expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
            expCargoDes.setFilterStyle("display: none; visibility: hidden;");
            expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
            expJefe.setFilterStyle("display: none; visibility: hidden;");
            expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
            expTelefono.setFilterStyle("display: none; visibility: hidden;");
            expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
            expSectorEco.setFilterStyle("display: none; visibility: hidden;");
            expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
            expMotivos.setFilterStyle("display: none; visibility: hidden;");
            expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
            expFechaInicio.setFilterStyle("display: none; visibility: hidden;");
            expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
            expFechaRetiro.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            bandera = 0;
            listhvExpLaboralesFiltrar = null;
            tipoLista = 0;
        }
        lovMotivos = null;
        lovSectoresEconomicos = null;
        listhvExpLaboralesBorrar.clear();
        listhvExpLaboralesCrear.clear();
        listhvExpLaboralesModificar.clear();
        hvexpSeleccionada = null;
        k = 0;
        listhvExpLaborales = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        getListhvExpLaborales();
        contarRegistrosExp();
        RequestContext.getCurrentInstance().update("form:datosExperiencia");

        nuevahvexp = new HvExperienciasLaborales();
        nuevahvexp.setSectoreconomico(new SectoresEconomicos());
        nuevahvexp.setMotivoretiro(new MotivosRetiros());
        fechaFin = null;
        fechaIni = null;
    }

    public void agregarNuevaE() {
        if (nuevahvexp.getFechadesde() != null && nuevahvexp.getMotivoretiro() != null) {
            if (validarFechasRegistro(1) == true) {
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    expEmpresa = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expEmpresa");
                    expEmpresa.setFilterStyle("display: none; visibility: hidden;");
                    expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
                    expCargoDes.setFilterStyle("display: none; visibility: hidden;");
                    expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
                    expJefe.setFilterStyle("display: none; visibility: hidden;");
                    expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
                    expTelefono.setFilterStyle("display: none; visibility: hidden;");
                    expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
                    expSectorEco.setFilterStyle("display: none; visibility: hidden;");
                    expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
                    expMotivos.setFilterStyle("display: none; visibility: hidden;");
                    expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
                    expFechaInicio.setFilterStyle("display: none; visibility: hidden;");
                    expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
                    expFechaRetiro.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosExperiencia");
                    bandera = 0;
                    listhvExpLaboralesFiltrar = null;
                    tipoLista = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevahvexp.setSecuencia(l);
                nuevahvexp.setHojadevida(hojaVida);
                listhvExpLaborales.add(nuevahvexp);
                listhvExpLaboralesCrear.add(nuevahvexp);
                hvexpSeleccionada = nuevahvexp;
                limpiarNuevaExpL();
                getListhvExpLaborales();
                contarRegistrosExp();
//                    RequestContext.getCurrentInstance().update("form:informacionRegistro");
                RequestContext.getCurrentInstance().update("form:datosExperiencia");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroExpLaborales').hide()");

                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechasIngresoReg').show()");
        }
    }

    public void limpiarNuevaExpL() {
        nuevahvexp = new HvExperienciasLaborales();
        nuevahvexp.setSectoreconomico(new SectoresEconomicos());
        nuevahvexp.setMotivoretiro(new MotivosRetiros());
        nuevahvexp.setFechadesde(new Date());
    }

    public void duplicarExpLaboral() {
        if (hvexpSeleccionada != null) {
            duplicarhvexp = new HvExperienciasLaborales();
            if (tipoLista == 0) {
                duplicarhvexp.setCargo(hvexpSeleccionada.getCargo());
                duplicarhvexp.setEmpresa(hvexpSeleccionada.getEmpresa());
                duplicarhvexp.setFechadesde(hvexpSeleccionada.getFechadesde());
                duplicarhvexp.setFechahasta(hvexpSeleccionada.getFechahasta());
                duplicarhvexp.setHojadevida(hvexpSeleccionada.getHojadevida());
                duplicarhvexp.setJefeinmediato(hvexpSeleccionada.getJefeinmediato());
                duplicarhvexp.setMotivoretiro(hvexpSeleccionada.getMotivoretiro());
                duplicarhvexp.setSectoreconomico(hvexpSeleccionada.getSectoreconomico());
                duplicarhvexp.setTelefono(hvexpSeleccionada.getTelefono());
            }
            if (tipoLista == 1) {
                duplicarhvexp.setCargo(hvexpSeleccionada.getCargo());
                duplicarhvexp.setEmpresa(hvexpSeleccionada.getEmpresa());
                duplicarhvexp.setFechadesde(hvexpSeleccionada.getFechadesde());
                duplicarhvexp.setFechahasta(hvexpSeleccionada.getFechahasta());
                duplicarhvexp.setHojadevida(hvexpSeleccionada.getHojadevida());
                duplicarhvexp.setJefeinmediato(hvexpSeleccionada.getJefeinmediato());
                duplicarhvexp.setMotivoretiro(hvexpSeleccionada.getMotivoretiro());
                duplicarhvexp.setSectoreconomico(hvexpSeleccionada.getSectoreconomico());
                duplicarhvexp.setTelefono(hvexpSeleccionada.getTelefono());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEP");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroExpLaborales').show()");
        }
    }

    public void confirmarDuplicarEXpLaboral() {
        if (duplicarhvexp.getFechadesde() != null) {
            fechaDesdeText = formatoFecha.format(duplicarhvexp.getFechadesde());
        } else {
            fechaDesdeText = "";
        }
        boolean respuesta = validarFechasRegistro(2);
        if (respuesta == true) {
//            if (validarCamposRegistro(2) == true) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarhvexp.setSecuencia(l);
            duplicarhvexp.setHojadevida(hojaVida);
            listhvExpLaborales.add(duplicarhvexp);
            listhvExpLaboralesCrear.add(duplicarhvexp);
            hvexpSeleccionada = duplicarhvexp;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTabla = "190";
                expEmpresa = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expEmpresa");
                expEmpresa.setFilterStyle("display: none; visibility: hidden;");
                expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
                expCargoDes.setFilterStyle("display: none; visibility: hidden;");
                expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
                expJefe.setFilterStyle("display: none; visibility: hidden;");
                expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
                expTelefono.setFilterStyle("display: none; visibility: hidden;");
                expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
                expSectorEco.setFilterStyle("display: none; visibility: hidden;");
                expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
                expMotivos.setFilterStyle("display: none; visibility: hidden;");
                expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
                expFechaInicio.setFilterStyle("display: none; visibility: hidden;");
                expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
                expFechaRetiro.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosExperiencia");
                bandera = 0;
                listhvExpLaboralesFiltrar = null;
                tipoLista = 0;
            }
            duplicarhvexp = new HvExperienciasLaborales();
            limpiarduplicarExpL();
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistrosExp();
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroExpLaborales').hide()");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechasIngresoReg').show()");
        }
    }

    public void limpiarduplicarExpL() {
        duplicarhvexp = new HvExperienciasLaborales();
        duplicarhvexp.setSectoreconomico(new SectoresEconomicos());
        duplicarhvexp.setMotivoretiro(new MotivosRetiros());
    }

    public void borrarExpLaborales() {
        if (hvexpSeleccionada != null) {
            if (!listhvExpLaboralesModificar.isEmpty() && listhvExpLaboralesModificar.contains(hvexpSeleccionada)) {
                int modIndex = listhvExpLaboralesModificar.indexOf(hvexpSeleccionada);
                listhvExpLaboralesModificar.remove(modIndex);
                listhvExpLaboralesBorrar.add(hvexpSeleccionada);
            } else if (!listhvExpLaboralesCrear.isEmpty() && listhvExpLaboralesCrear.contains(hvexpSeleccionada)) {
                int crearIndex = listhvExpLaboralesCrear.indexOf(hvexpSeleccionada);
                listhvExpLaboralesCrear.remove(crearIndex);
            } else {
                listhvExpLaboralesBorrar.add(hvexpSeleccionada);
            }
            listhvExpLaborales.remove(hvexpSeleccionada);
            if (tipoLista == 1) {
                listhvExpLaboralesFiltrar.remove(hvexpSeleccionada);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistrosExp();
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            hvexpSeleccionada = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void salirExpLaborales() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            expEmpresa = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expEmpresa");
            expEmpresa.setFilterStyle("display: none; visibility: hidden;");
            expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
            expCargoDes.setFilterStyle("display: none; visibility: hidden;");
            expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
            expJefe.setFilterStyle("display: none; visibility: hidden;");
            expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
            expTelefono.setFilterStyle("display: none; visibility: hidden;");
            expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
            expSectorEco.setFilterStyle("display: none; visibility: hidden;");
            expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
            expMotivos.setFilterStyle("display: none; visibility: hidden;");
            expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
            expFechaInicio.setFilterStyle("display: none; visibility: hidden;");
            expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
            expFechaRetiro.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            bandera = 0;
            listhvExpLaboralesFiltrar = null;
            tipoLista = 0;
        }
        listhvExpLaboralesBorrar.clear();
        listhvExpLaboralesCrear.clear();
        listhvExpLaboralesModificar.clear();
        hvexpSeleccionada = null;
        k = 0;
        listhvExpLaborales = null;
        lovMotivos = null;
        lovSectoresEconomicos = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        tipoActualizacion = -1;
        hojaVida = null;

    }

    public void actualizarSector() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                hvexpSeleccionada.setSectoreconomico(sectorSeleccionado);
                if (!listhvExpLaboralesCrear.contains(hvexpSeleccionada)) {
                    if (listhvExpLaboralesModificar.isEmpty()) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    } else if (!listhvExpLaboralesModificar.contains(hvexpSeleccionada)) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    }
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                permitirIndex = true;

            } else {
                hvexpSeleccionada.setSectoreconomico(sectorSeleccionado);
                if (!listhvExpLaboralesCrear.contains(hvexpSeleccionada)) {
                    if (listhvExpLaboralesModificar.isEmpty()) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    } else if (!listhvExpLaboralesModificar.contains(hvexpSeleccionada)) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    }
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }

                permitirIndex = true;

            }
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
        } else if (tipoActualizacion == 1) {
            nuevahvexp.setSectoreconomico(sectorSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSectorEP");
        } else if (tipoActualizacion == 2) {
            duplicarhvexp.setSectoreconomico(sectorSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSectorEP");
        }
        listhvExpLaboralesFiltrar = null;
        sectorSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        context.reset("formularioDialogos:lovSector:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSector').clearFilters()");
        RequestContext.getCurrentInstance().update("formularioDialogos:SectorDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovSector");
        RequestContext.getCurrentInstance().execute("PF('SectorDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarSector");
    }

    public void cancelarCambioSector() {
        listhvExpLaboralesFiltrar = null;
        sectorSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovSector:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSector').clearFilters()");
        RequestContext.getCurrentInstance().update("formularioDialogos:SectorDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovSector");
        RequestContext.getCurrentInstance().execute("PF('SectorDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarSector");
    }

    public void actualizarMotivo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                hvexpSeleccionada.setMotivoretiro(motivoSeleccionado);
                if (!listhvExpLaboralesCrear.contains(hvexpSeleccionada)) {
                    if (listhvExpLaboralesModificar.isEmpty()) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    } else if (!listhvExpLaboralesModificar.contains(hvexpSeleccionada)) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    }
                }
            } else {
                hvexpSeleccionada.setMotivoretiro(motivoSeleccionado);
                if (!listhvExpLaboralesCrear.contains(hvexpSeleccionada)) {
                    if (listhvExpLaboralesModificar.isEmpty()) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    } else if (!listhvExpLaboralesModificar.contains(hvexpSeleccionada)) {
                        listhvExpLaboralesModificar.add(hvexpSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
        } else if (tipoActualizacion == 1) {
            nuevahvexp.setMotivoretiro(motivoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoEP");
        } else if (tipoActualizacion == 2) {
            duplicarhvexp.setMotivoretiro(motivoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoEP");
        }
        listhvExpLaboralesFiltrar = null;
        motivoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        context.reset("formularioDialogos:lovMotivos:globalFilter");
        RequestContext.getCurrentInstance().update("formularioDialogos:MotivosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovMotivos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
        RequestContext.getCurrentInstance().execute("PF('lovMotivos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivosDialogo').hide()");
    }

    public void cancelarCambioMotivo() {
        listhvExpLaboralesFiltrar = null;
        motivoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovMotivos:globalFilter");
        RequestContext.getCurrentInstance().update("formularioDialogos:MotivosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovMotivos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
        RequestContext.getCurrentInstance().execute("PF('lovMotivos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivosDialogo').hide()");
    }

    public void contarRegistrosExp() {
        RequestContext.getCurrentInstance().update("form:infoRegistroExp");
    }

    public void eventoFiltrarExp() {
        contarRegistrosExp();
    }

    public void contarRegistroSector() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroSector");
    }

    public void contarRegistroMotivo() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMotivo");
    }

    public void cambiarIndiceVigenciaDomiciliaria(VigenciasDomiciliarias vigenciaD, int celda) {
        if (permitirIndex == true) {
            vigenciasDomiciliariaSeleccionada = vigenciaD;
            cualCelda = celda;
            cualtabla = 8;
            if (cualCelda == 1) {
                vigenciasDomiciliariaSeleccionada.getCalificacionfamiliar();
            } else if (cualCelda == 2) {
                vigenciasDomiciliariaSeleccionada.getObservacionfamiliar();
            } else if (cualCelda == 3) {
                condiciones = vigenciasDomiciliariaSeleccionada.getCondicionesgenerales();
            } else if (cualCelda == 4) {
                distribucion = vigenciasDomiciliariaSeleccionada.getDistribucionvivienda();
            } else if (cualCelda == 5) {
                descGeneral = vigenciasDomiciliariaSeleccionada.getDescripcionvivienda();
            } else if (cualCelda == 6) {
                vigenciasDomiciliariaSeleccionada.getConstruccion();
            } else if (cualCelda == 7) {
                vigenciasDomiciliariaSeleccionada.getServicioagua();
            } else if (cualCelda == 8) {
                vigenciasDomiciliariaSeleccionada.getServicioluz();
            } else if (cualCelda == 9) {
                vigenciasDomiciliariaSeleccionada.getServiciotelefono();
            } else if (cualCelda == 10) {
                vigenciasDomiciliariaSeleccionada.getServicioparabolica();
            } else if (cualCelda == 11) {
                vigenciasDomiciliariaSeleccionada.getServiciotransporte();
            } else if (cualCelda == 12) {
                vigenciasDomiciliariaSeleccionada.getServicioalcantarillado();
            } else if (cualCelda == 13) {
                vigenciasDomiciliariaSeleccionada.getServiciootros();
            } else if (cualCelda == 14) {
                vigenciasDomiciliariaSeleccionada.getDetalleotrosservicios();
            } else if (cualCelda == 15) {
                vigenciasDomiciliariaSeleccionada.getIngresos();
            } else if (cualCelda == 16) {
                vigenciasDomiciliariaSeleccionada.getOrigenindependiente();
            } else if (cualCelda == 17) {
                vigenciasDomiciliariaSeleccionada.getOrigenarrendamiento();
            } else if (cualCelda == 18) {
                vigenciasDomiciliariaSeleccionada.getOrigenpension();
            } else if (cualCelda == 19) {
                vigenciasDomiciliariaSeleccionada.getOrigensalario();
            } else if (cualCelda == 20) {
                vigenciasDomiciliariaSeleccionada.getOrigencdt();
            } else if (cualCelda == 21) {
                vigenciasDomiciliariaSeleccionada.getOrigenauxilios();
            } else if (cualCelda == 22) {
                vigenciasDomiciliariaSeleccionada.getIngresopapa();
            } else if (cualCelda == 23) {
                vigenciasDomiciliariaSeleccionada.getIngresomama();
            } else if (cualCelda == 24) {
                vigenciasDomiciliariaSeleccionada.getIngresohermano();
            } else if (cualCelda == 25) {
                vigenciasDomiciliariaSeleccionada.getIngresoabuelo();
            } else if (cualCelda == 26) {
                vigenciasDomiciliariaSeleccionada.getIngresotio();
            } else if (cualCelda == 27) {
                vigenciasDomiciliariaSeleccionada.getIngresootro();
            } else if (cualCelda == 28) {
                vigenciasDomiciliariaSeleccionada.getDetalleotroingreso();
            } else if (cualCelda == 29) {
                vigenciasDomiciliariaSeleccionada.getInversioneducacion();
            } else if (cualCelda == 30) {
                vigenciasDomiciliariaSeleccionada.getInversionrecreacion();
            } else if (cualCelda == 31) {
                vigenciasDomiciliariaSeleccionada.getInversionalimentacion();
            } else if (cualCelda == 32) {
                vigenciasDomiciliariaSeleccionada.getInversionmedica();
            } else if (cualCelda == 33) {
                vigenciasDomiciliariaSeleccionada.getInversionarriendo();
            } else if (cualCelda == 34) {
                vigenciasDomiciliariaSeleccionada.getInversionservicios();
            } else if (cualCelda == 35) {
                vigenciasDomiciliariaSeleccionada.getInversionotros();
            } else if (cualCelda == 36) {
                vigenciasDomiciliariaSeleccionada.getDetalleotrasinversiones();
            } else if (cualCelda == 37) {
                observaciones = vigenciasDomiciliariaSeleccionada.getObservaciones();
            } else if (cualCelda == 38) {
                conceptofinal = vigenciasDomiciliariaSeleccionada.getConceptofinal();
            } else if (cualCelda == 39) {
                conceptosocial = vigenciasDomiciliariaSeleccionada.getConceptosocial();
            } else if (cualCelda == 40) {
                vigenciasDomiciliariaSeleccionada.getCondicionfamiliar();
            } else if (cualCelda == 41) {
                vigenciasDomiciliariaSeleccionada.getCondicionsocial();
            } else if (cualCelda == 42) {
                vigenciasDomiciliariaSeleccionada.getSituacioneconomica();
            } else if (cualCelda == 43) {
                vigenciasDomiciliariaSeleccionada.getNivelacademico();
            } else if (cualCelda == 44) {
                vigenciasDomiciliariaSeleccionada.getMotivacioncargo();
            } else if (cualCelda == 45) {
                vigenciasDomiciliariaSeleccionada.getPersonaspresentes();
            } else if (cualCelda == 46) {
                vigenciasDomiciliariaSeleccionada.getFecha();
            } else if (cualCelda == 47) {
                vigenciasDomiciliariaSeleccionada.getProfesional();
            } else if (cualCelda == 48) {
                vigenciasDomiciliariaSeleccionada.getServicioaseo();
            }
        }
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
        actualizarCamposVigDomiciliaria();
    }

    public void modificarFechaVigenciaDomiciliaria(VigenciasDomiciliarias vigenciaD, int c) {
        vigenciasDomiciliariaSeleccionada = vigenciaD;
        cambiarIndiceVigenciaDomiciliaria(vigenciaD, c);
        modificarVigenciaDomiciliaria(vigenciaD);
        vigenciasDomiciliariaSeleccionada.setFecha(vigenciasDomiciliariaSeleccionada.getFecha());
        RequestContext.getCurrentInstance().update("form:editarFechaDomiciliaria");
    }

    public void seleccionarCalificacionFamiliar(String calificacion, VigenciasDomiciliarias vigDom) {
        if (calificacion.equals(" ")) {
            vigenciasDomiciliariaSeleccionada.setCalificacionfamiliar("NULL");
        } else if (calificacion.equals("BUENA")) {
            vigenciasDomiciliariaSeleccionada.setCalificacionfamiliar("BUENA");
        } else if (calificacion.equals("REGULAR")) {
            vigenciasDomiciliariaSeleccionada.setCalificacionfamiliar("REGULAR");
        } else if (calificacion.equals("MALA")) {
            vigenciasDomiciliariaSeleccionada.setCalificacionfamiliar("MALA");
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
        RequestContext.getCurrentInstance().update("form:editarCalificacionF");
    }

    public void seleccionarConstruccion(String construccion, VigenciasDomiciliarias vigDom) {
        if (construccion.equals(" ")) {
            vigenciasDomiciliariaSeleccionada.setConstruccion("NULL");
        } else if (construccion.equals("LADRILLO")) {
            vigenciasDomiciliariaSeleccionada.setConstruccion("LADRILLO");
        } else if (construccion.equals("MADERA")) {
            vigenciasDomiciliariaSeleccionada.setConstruccion("MADERA");
        } else if (construccion.equals("OBRA NEGRA")) {
            vigenciasDomiciliariaSeleccionada.setConstruccion("OBRA NEGRA");
        } else if (construccion.equals("OBRA BLANCA")) {
            vigenciasDomiciliariaSeleccionada.setConstruccion("OBRA BLANCA");
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
        RequestContext.getCurrentInstance().update("form:editarConst");
    }

    public void seleccionarIngreso(String calificacion, VigenciasDomiciliarias vigDom) {
        if (calificacion.equals(" ")) {
            vigenciasDomiciliariaSeleccionada.setIngresos("NULL");
        } else if (calificacion.equals("DE 1 A 3 SALARIOS MINIMOS")) {
            vigenciasDomiciliariaSeleccionada.setIngresos("DE 1 A 3 SALARIOS MINIMOS");
        } else if (calificacion.equals("DE 4 A 6 SALARIOS MINIMOS")) {
            vigenciasDomiciliariaSeleccionada.setIngresos("DE 4 A 6 SALARIOS MINIMOS");
        } else if (calificacion.equals("DE 7 A 10 SALARIOS MINIMOS")) {
            vigenciasDomiciliariaSeleccionada.setIngresos("DE 7 A 10 SALARIOS MINIMOS");
        } else if (calificacion.equals("MÁS DE 10 SALARIOS MINIMOS")) {
            vigenciasDomiciliariaSeleccionada.setIngresos("MÁS DE 10 SALARIOS MINIMOS");
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
        RequestContext.getCurrentInstance().update("form:editarIngresos");
    }

    public void seleccionarCondicionF(String condicionf, VigenciasDomiciliarias vigDom) {
        if (condicionf.equals(" ")) {
            vigenciasDomiciliariaSeleccionada.setCondicionfamiliar("NULL");
        } else if (condicionf.equals("ADECUADA")) {
            vigenciasDomiciliariaSeleccionada.setCondicionfamiliar("ADECUADA");
        } else if (condicionf.equals("INADECUADA")) {
            vigenciasDomiciliariaSeleccionada.setCondicionfamiliar("INADECUADA");
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
        RequestContext.getCurrentInstance().update("form:editarCondicionesF");
    }

    public void seleccionarCondicionS(String condicionsocial, VigenciasDomiciliarias vigDom) {
        if (condicionsocial.equals(" ")) {
            vigenciasDomiciliariaSeleccionada.setCondicionsocial("NULL");
        } else if (condicionsocial.equals("ADECUADA")) {
            vigenciasDomiciliariaSeleccionada.setCondicionsocial("ADECUADA");
        } else if (condicionsocial.equals("INADECUADA")) {
            vigenciasDomiciliariaSeleccionada.setCondicionsocial("INADECUADA");
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
        RequestContext.getCurrentInstance().update("form:editarCondicionesS");
    }

    public void seleccionarSituacionEconomica(String situacion, VigenciasDomiciliarias vigDom) {
        if (situacion.equals(" ")) {
            vigenciasDomiciliariaSeleccionada.setSituacioneconomica("NULL");
        } else if (situacion.equals("ADECUADA")) {
            vigenciasDomiciliariaSeleccionada.setSituacioneconomica("ADECUADA");
        } else if (situacion.equals("INADECUADA")) {
            vigenciasDomiciliariaSeleccionada.setSituacioneconomica("INADECUADA");
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
        RequestContext.getCurrentInstance().update("form:editarSituacionE");
    }

    public void seleccionarNivelAcademico(String nivel, VigenciasDomiciliarias vigDom) {
        if (nivel.equals(" ")) {
            vigenciasDomiciliariaSeleccionada.setNivelacademico("NULL");
        } else if (nivel.equals("ADECUADA")) {
            vigenciasDomiciliariaSeleccionada.setNivelacademico("ADECUADA");
        } else if (nivel.equals("INADECUADA")) {
            vigenciasDomiciliariaSeleccionada.setNivelacademico("INADECUADA");
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
        RequestContext.getCurrentInstance().update("form:editarNivelAc");
    }

    public void seleccionarMotivacionCargo(String motivacionCargo, VigenciasDomiciliarias vigDom) {
        if (motivacionCargo.equals(" ")) {
            vigenciasDomiciliariaSeleccionada.setMotivacioncargo("NULL");
        } else if (motivacionCargo.equals("ADECUADA")) {
            vigenciasDomiciliariaSeleccionada.setMotivacioncargo("ADECUADA");
        } else if (motivacionCargo.equals("INADECUADA")) {
            vigenciasDomiciliariaSeleccionada.setMotivacioncargo("INADECUADA");
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
        RequestContext.getCurrentInstance().update("form:editarMotivacionC");
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {

            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
            dFecha.setFilterStyle("width: 85% !important");
            dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
            dUbicacionPrincipal.setFilterStyle("width: 85% !important");
            dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
            dDescripcionUbicacionPrincipal.setFilterStyle("width: 85% !important");
            dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
            dUbicacionSecundaria.setFilterStyle("width: 85% !important");
            dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
            dDescripcionUbicacionSecundaria.setFilterStyle("width: 85% !important");
            dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
            dInterior.setFilterStyle("width: 85% !important");
            dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
            dCiudad.setFilterStyle("width: 85% !important");
            dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
            dTipoVivienda.setFilterStyle("width: 85% !important");
            dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
            dHipoteca.setFilterStyle("width: 85% !important");
            dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
            dDireccionAlternativa.setFilterStyle("width: 85% !important");
            tFecha = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tFecha");
            tFecha.setFilterStyle("width: 85%");
            tTipoTelefono = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tTipoTelefono");
            tTipoTelefono.setFilterStyle("width: 85%");
            tNumero = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tNumero");
            tNumero.setFilterStyle("width: 85%");
            tCiudad = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tCiudad");
            tCiudad.setFilterStyle("width: 85%");
            //
            fecha = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:fecha");
            fecha.setFilterStyle("width: 85% !important;");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:parentesco");
            parentesco.setFilterStyle("width: 85% !important;");
            //
            fechaAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:fechaAntecedenteM");
            fechaAntecedenteM.setFilterStyle("width: 85% !important;");
            tipoAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:tipoAntecedenteM");
            tipoAntecedenteM.setFilterStyle("width: 85% !important;");
            antecedenteMedico = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:antecedenteMedico");
            antecedenteMedico.setFilterStyle("width: 85% !important;");
            descAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:descAntecedenteM");
            descAntecedenteM.setFilterStyle("width: 85% !important;");
            //
            pEFechas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechas");
            pEFechas.setFilterStyle("width: 85% !important");
            pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
            pETiposEducaciones.setFilterStyle("width: 85% !important");
            pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
            pEProfesiones.setFilterStyle("width: 85% !important");
            pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
            pEInstituciones.setFilterStyle("width: 85% !important");
            pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
            pEAdiestramientosF.setFilterStyle("width: 85% !important");
            pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
            pECalificaciones.setFilterStyle("width: 85% !important");
            pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
            pENumerosTarjetas.setFilterStyle("width: 85% !important");
            pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
            pEFechasExpediciones.setFilterStyle("width: 85% !important");
            pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
            pEFechasVencimientos.setFilterStyle("width: 85% !important");
            pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
            pEObservaciones.setFilterStyle("width: 85% !important");
            expEmpresa = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expEmpresa");
            expEmpresa.setFilterStyle("width: 85% !important;");
            expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
            expCargoDes.setFilterStyle("width: 85% !important;");
            expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
            expJefe.setFilterStyle("width: 85% !important;");
            expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
            expTelefono.setFilterStyle("width: 85% !important;");
            expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
            expSectorEco.setFilterStyle("width: 85% !important;");
            expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
            expMotivos.setFilterStyle("width: 85% !important;");
            expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
            expFechaInicio.setFilterStyle("width: 85% !important;");
            expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
            expFechaRetiro.setFilterStyle("width: 85% !important;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:nombreFamiliar");
            nombre.setFilterStyle("width: 85% !important");
            ocupacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:ocupacionFamiliar");
            ocupacion.setFilterStyle("width: 85% !important");
            columnaTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:ctipoFamiliar");
            columnaTipo.setFilterStyle("width: 85% !important");
            smedico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:servicioMedico");
            smedico.setFilterStyle("width: 85% !important");
            sfamiliar = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:sFamiliar");
            sfamiliar.setFilterStyle("width: 85% !important");
            beneficiario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:beneficiario");
            beneficiario.setFilterStyle("width: 85% !important");
            upcadicional = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:upcAd");
            upcadicional.setFilterStyle("width: 85% !important");
            valorupc = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:valorUpc");
            valorupc.setFilterStyle("width: 85% !important");

            RequestContext.getCurrentInstance().update("form:datosFamiliares");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            altoTabla = "250";
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
            dFecha.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
            dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
            dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
            dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
            dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
            dInterior.setFilterStyle("display: none; visibility: hidden;");
            dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
            dCiudad.setFilterStyle("display: none; visibility: hidden;");
            dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
            dTipoVivienda.setFilterStyle("display: none; visibility: hidden;");
            dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
            dHipoteca.setFilterStyle("display: none; visibility: hidden;");
            dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
            dDireccionAlternativa.setFilterStyle("display: none; visibility: hidden;");
            tFecha = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tFecha");
            tFecha.setFilterStyle("display: none; visibility: hidden;");
            tTipoTelefono = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tTipoTelefono");
            tTipoTelefono.setFilterStyle("display: none; visibility: hidden;");
            tNumero = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tNumero");
            tNumero.setFilterStyle("display: none; visibility: hidden;");
            tCiudad = (Column) c.getViewRoot().findComponent("form:datosTelefonosPersona:tCiudad");
            tCiudad.setFilterStyle("display: none; visibility: hidden;");
            fecha = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:parentesco");
            parentesco.setFilterStyle("display: none; visibility: hidden;");
            fechaAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:fechaAntecedenteM");
            fechaAntecedenteM.setFilterStyle("width: 85% !important;");
            tipoAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:tipoAntecedenteM");
            tipoAntecedenteM.setFilterStyle("width: 85% !important;");
            antecedenteMedico = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:antecedenteMedico");
            antecedenteMedico.setFilterStyle("width: 85% !important;");
            descAntecedenteM = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:descAntecedenteM");
            descAntecedenteM.setFilterStyle("width: 85% !important;");
            pEFechas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechas");
            pEFechas.setFilterStyle("display: none; visibility: hidden;");
            pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
            pETiposEducaciones.setFilterStyle("display: none; visibility: hidden;");
            pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
            pEProfesiones.setFilterStyle("display: none; visibility: hidden;");
            pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
            pEInstituciones.setFilterStyle("display: none; visibility: hidden;");
            pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
            pEAdiestramientosF.setFilterStyle("display: none; visibility: hidden;");
            pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
            pECalificaciones.setFilterStyle("display: none; visibility: hidden;");
            pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
            pENumerosTarjetas.setFilterStyle("display: none; visibility: hidden;");
            pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
            pEFechasExpediciones.setFilterStyle("display: none; visibility: hidden;");
            pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
            pEFechasVencimientos.setFilterStyle("display: none; visibility: hidden;");
            pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
            pEObservaciones.setFilterStyle("display: none; visibility: hidden;");
            expEmpresa = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expEmpresa");
            expEmpresa.setFilterStyle("display: none; visibility: hidden;");
            expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
            expCargoDes.setFilterStyle("display: none; visibility: hidden;");
            expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
            expJefe.setFilterStyle("display: none; visibility: hidden;");
            expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
            expTelefono.setFilterStyle("display: none; visibility: hidden;");
            expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
            expSectorEco.setFilterStyle("display: none; visibility: hidden;");
            expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
            expMotivos.setFilterStyle("display: none; visibility: hidden;");
            expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
            expFechaInicio.setFilterStyle("display: none; visibility: hidden;");
            expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
            expFechaRetiro.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:nombreFamiliar");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            ocupacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:ocupacionFamiliar");
            ocupacion.setFilterStyle("display: none; visibility: hidden;");
            columnaTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:ctipoFamiliar");
            columnaTipo.setFilterStyle("display: none; visibility: hidden;");
            smedico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:servicioMedico");
            smedico.setFilterStyle("display: none; visibility: hidden;");
            sfamiliar = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:sFamiliar");
            sfamiliar.setFilterStyle("display: none; visibility: hidden;");
            beneficiario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:beneficiario");
            beneficiario.setFilterStyle("display: none; visibility: hidden;");
            upcadicional = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:upcAd");
            upcadicional.setFilterStyle("display: none; visibility: hidden;");
            valorupc = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFamiliares:valorUpc");
            valorupc.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosFamiliares");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            altoTabla = "270";
            bandera = 0;
            listTelefonosFiltrar = null;
            listDireccionesFiltrar = null;
            listVigenciaEstadoCivilFiltrar = null;
            listAntecedentesMFiltrar = null;
            listaFamiliaresFiltrar = null;
            tipoLista = 0;
        }
    }

    public void mostrarDialogoCargos() {
        RequestContext.getCurrentInstance().update("formularioDialogos:cargoDialogo");
        RequestContext.getCurrentInstance().execute("PF('cargoDialogo').show()");
    }

    public void actualizarCargo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            hojaVida.setCargo(cargoSeleccionada);
            if (!listHojasdeVidaCrear.contains(hojaVida)) {
                if (listHojasdeVidaModificar.isEmpty()) {
                    listHojasdeVidaModificar.add(hojaVida);
                } else if (!listHojasdeVidaModificar.contains(hojaVida)) {
                    listHojasdeVidaModificar.add(hojaVida);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:cargoPostulado");
        } else if (tipoActualizacion == 1) {
            hojaVida.setCargo(cargoSeleccionada);
            RequestContext.getCurrentInstance().update("form:cargoPostulado");
        }

        lovCargosFiltrar = null;
        cargoSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:cargoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovCargo");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCargo");

        context.reset("formularioDialogos:lovCargo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCargo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('cargoDialogo').hide()");
    }

    public void cancelarCambioCargo() {
        lovCargosFiltrar = null;
        cargoSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:cargoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovCargo");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCargo");

        RequestContext.getCurrentInstance().reset("formularioDialogos:lovCargo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCargo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('cargoDialogo').hide()");
    }

    public void contarRegistrosCargo() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCargos");
    }

    public void modificarCargo(Cargos cargo) {
        cargoSeleccionada = cargo;
        nuevaHV.setCargo(cargoSeleccionada);
        if (!listHojasdeVidaCrear.contains(nuevaHV)) {
            if (listHojasdeVidaModificar.isEmpty()) {
                listHojasdeVidaModificar.add(nuevaHV);
            } else if (!listHojasdeVidaModificar.contains(nuevaHV)) {
                listHojasdeVidaModificar.add(nuevaHV);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:cargoPostulado");
            }
        }
        guardado = true;
    }

    public void modificarPersona(Personas personam) {
        persona = personam;

        if (!listPersonasCrear.contains(persona)) {
            if (listPersonasModificar.isEmpty()) {
                listPersonasModificar.add(persona);
            } else if (!listPersonasModificar.contains(persona)) {
                listPersonasModificar.add(persona);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:editarEdad");
                RequestContext.getCurrentInstance().update("form:editarCiudadDoc");
                RequestContext.getCurrentInstance().update("form:editarDocumento");
                RequestContext.getCurrentInstance().update("form:editarPersona");
            }
        }
        guardado = true;
    }

    public void actualizarCamposVigDomiciliaria() {
        RequestContext.getCurrentInstance().update("form:editarFechaDomiciliaria");
        RequestContext.getCurrentInstance().update("form:editarProfesional");
        RequestContext.getCurrentInstance().update("form:editarCalificacionF");
        RequestContext.getCurrentInstance().update("form:editarObsFamiliar");
        RequestContext.getCurrentInstance().update("form:editarCondiciones");
        RequestContext.getCurrentInstance().update("form:editarDistribucion");
        RequestContext.getCurrentInstance().update("form:editarDescripcionGeneral");
        RequestContext.getCurrentInstance().update("form:editarConst");
        RequestContext.getCurrentInstance().update("form:otrosservicios");
        RequestContext.getCurrentInstance().update("form:editarIngresos");
        RequestContext.getCurrentInstance().update("form:otrosIngresos");
        RequestContext.getCurrentInstance().update("form:otrosEgresos");
        RequestContext.getCurrentInstance().update("form:editarObservaciones");
        RequestContext.getCurrentInstance().update("form:editarConceptoF");
        RequestContext.getCurrentInstance().update("form:editarConceptoS");
        RequestContext.getCurrentInstance().update("form:editarCondicionesF");
        RequestContext.getCurrentInstance().update("form:editarCondicionesS");
        RequestContext.getCurrentInstance().update("form:editarSituacionE");
        RequestContext.getCurrentInstance().update("form:editarNivelAc");
        RequestContext.getCurrentInstance().update("form:editarMotivacionC");
        RequestContext.getCurrentInstance().update("form:editarPersonas");
    }

    public void exportPDF() throws IOException {
        if (cualtabla == 1) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDireccionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "DireccionesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 2) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTelefonosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "TelefonosPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 3) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEstadoCivilExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "VigenciasEstadosCivilesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 4) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAntecedentesMExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "AntecedentesMedicosPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 5) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFamiliaresExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "FamiliaresPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 6) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasFormalesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "VigenciasFormalesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 7) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosExperienciaExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "ExperienciasLaboralesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 8) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVisitaExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDFTablasAnchas();
            exporter.export(context, tabla, "VisitasDomiciliariasXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        }
    }

    public void exportXLS() throws IOException {
        if (cualtabla == 1) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDireccionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "DireccionesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 2) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTelefonosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "TelefonosXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 3) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEstadoCivilExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "VigenciasEstadosCivilesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 4) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAntecedentesMExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "AntecedentesMedicosXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 5) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFamiliaresExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "FamiliaresXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 6) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasFormalesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "VigenciasFormalesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 7) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosExperienciaExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "VigenciasFormalesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualtabla == 8) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVisitaExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "VisitasDomiciliariasXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        }

    }

    public void limpiarVigenciaDomiciliaria() {
        vigenciasDomiciliariaSeleccionada = administrarVigDomiciliarias.vigenciaDomiciliariaActual(persona.getSecuencia());
        vigenciasDomiciliariaSeleccionada.setPersona(persona);
    }

    public void limpiarExportar() {
        if (cualtabla == 1) {
            limpiarNuevaDireccion();
            nombreArchivo = "DireccionesXML";
            tablaImprimir = ":formExportar:datosDireccionesExportar";
        } else if (cualtabla == 2) {
            limpiarNuevoTelefono();
            nombreArchivo = "TelefonosXML";
            tablaImprimir = ":formExportar:datosTelefonosExportar";
        } else if (cualtabla == 3) {
            limpiarNuevoEstadoCivil();
            nombreArchivo = "VigenciasEstadoCivilXML";
            tablaImprimir = ":formExportar:datosEstadoCivilExportar";
        } else if (cualtabla == 4) {
            limpiarAntecedenteM();
            nombreArchivo = "AntecedentesMedicosXML";
            tablaImprimir = ":formExportar:datosAntecedentesMExportar";
        } else if (cualtabla == 5) {
            limpiarNuevoFamiliar();
            nombreArchivo = "FamiliaresXML";
            tablaImprimir = ":formExportar:datosFamiliaresExportar";
        } else if (cualtabla == 6) {
            limpiarNuevaVigenciaFormal();
            nombreArchivo = "EducacionXML";
            tablaImprimir = ":formExportar:datosVigenciasFormalesExportar";
        } else if (cualtabla == 7) {
            limpiarNuevaExpL();
            nombreArchivo = "ExperienciaLaboralXML";
            tablaImprimir = ":formExportar:datosExperienciaExportar";
        } else if (cualtabla == 8) {
            nombreArchivo = "VisitasDomiciliariasXML";
            tablaImprimir = ":formExportar:datosVisitaExportar";
            limpiarVigenciaDomiciliaria();
        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }

    }

    public void editarCelda() {
        if (cualtabla == 1) {
            if (direccionSeleccionada != null) {
                editarDireccion = direccionSeleccionada;
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
                    RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarUbicacionesPrincipales");
                    RequestContext.getCurrentInstance().execute("PF('editarUbicacionesPrincipales').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionesUbicacionesPrincipales");
                    RequestContext.getCurrentInstance().execute("PF('editarDescripcionesUbicacionesPrincipales').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarUbicacionesSecundarias");
                    RequestContext.getCurrentInstance().execute("PF('editarUbicacionesSecundarias').show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionesUbicacionesSecundarias");
                    RequestContext.getCurrentInstance().execute("PF('editarDescripcionesUbicacionesSecundarias').show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarBarrios");
                    RequestContext.getCurrentInstance().execute("PF('editarBarrios').show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudades");
                    RequestContext.getCurrentInstance().execute("PF('editarCiudades').show()");
                    cualCelda = -1;
                } else if (cualCelda == 7) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTiposViviendas");
                    RequestContext.getCurrentInstance().execute("PF('editarTiposViviendas').show()");
                    cualCelda = -1;
                } else if (cualCelda == 8) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarHipotecas");
                    RequestContext.getCurrentInstance().execute("PF('editarHipotecas').show()");
                    cualCelda = -1;
                } else if (cualCelda == 9) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDireccionesAlternativas");
                    RequestContext.getCurrentInstance().execute("PF('editarDireccionesAlternativas').show()");
                    cualCelda = -1;
                }
            }
        } else if (cualtabla == 2) {
            if (telefonoSeleccionado != null) {
                editarTelefono = telefonoSeleccionado;

                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaTelefono");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaTelefono').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTT");
                    RequestContext.getCurrentInstance().execute("PF('editarTT').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNumeroTelefono");
                    RequestContext.getCurrentInstance().execute("PF('editarNumeroTelefono').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudadTelefono");
                    RequestContext.getCurrentInstance().execute("PF('editarCiudadTelefono').show()");
                    cualCelda = -1;
                }
            }
        } else if (cualtabla == 3) {
            if (vigenciaEstadoCivilSeleccionado != null) {
                editarVigenciaEstadoCivil = vigenciaEstadoCivilSeleccionado;
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaEstadoCivil");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaEstadoCivil').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editEstadoCivil");
                    RequestContext.getCurrentInstance().execute("PF('editEstadoCivil').show()");
                    cualCelda = -1;
                }
            }
        } else if (cualtabla == 4) {
            if (antecedentemSeleccionado != null) {
                editarAntecedenteM = antecedentemSeleccionado;
                if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editFechaAntecedenteM");
                    RequestContext.getCurrentInstance().execute("PF('editFechaAntecedenteM').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editTipoAntecedenteM");
                    RequestContext.getCurrentInstance().execute("PF('editTipoAntecedenteM').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editAntecedenteD");
                    RequestContext.getCurrentInstance().execute("PF('editAntecedenteD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editAntecedenteMDescripcion");
                    RequestContext.getCurrentInstance().execute("PF('editAntecedenteMDescripcion').show()");
                    cualCelda = -1;
                }
            }
        } else if (cualtabla == 5) {
            if (familiarSeleccionado != null) {
                editarFamiliar = familiarSeleccionado;

                if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreD");
                    RequestContext.getCurrentInstance().execute("PF('editarNombreD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarOcupacionD");
                    RequestContext.getCurrentInstance().execute("PF('editarOcupacionD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoFamiliarD");
                    RequestContext.getCurrentInstance().execute("PF('editarTipoFamiliarD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 8) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarValorUpcD");
                    RequestContext.getCurrentInstance().execute("PF('editarValorUpcD').show()");
                    cualCelda = -1;
                }
            }
        } else if (cualtabla == 6) {
            if (vigenciaFormalSeleccionada != null) {

                editarVigenciaFormal = vigenciaFormalSeleccionada;
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaEducacion");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaEducacion').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoEducacion");
                    RequestContext.getCurrentInstance().execute("PF('editarTipoEducacion').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarProfesion");
                    RequestContext.getCurrentInstance().execute("PF('editarProfesion').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarInstitucion");
                    RequestContext.getCurrentInstance().execute("PF('editarInstitucion').show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAdiestramientoF");
                    RequestContext.getCurrentInstance().execute("PF('editarAdiestramientoF').show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarValoracion");
                    RequestContext.getCurrentInstance().execute("PF('editarValoracion').show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNumeroTarjeta");
                    RequestContext.getCurrentInstance().execute("PF('editarNumeroTarjeta').show()");
                    cualCelda = -1;
                } else if (cualCelda == 7) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaExpedicion");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaExpedicion').show()");
                    cualCelda = -1;
                } else if (cualCelda == 8) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVencimiento");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaVencimiento').show()");
                    cualCelda = -1;
                } else if (cualCelda == 9) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacion");
                    RequestContext.getCurrentInstance().execute("PF('editarObservacion').show()");
                    cualCelda = -1;
                }
            }
        } else if (cualtabla == 7) {
            if (hvexpSeleccionada != null) {
                editarhvexp = hvexpSeleccionada;
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaD");
                    RequestContext.getCurrentInstance().execute("PF('editarEmpresaD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialD");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaInicialD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalD");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaFinalD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCargoD");
                    RequestContext.getCurrentInstance().execute("PF('editarCargoD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarJefeD");
                    RequestContext.getCurrentInstance().execute("PF('editarJefeD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTelefonoD");
                    RequestContext.getCurrentInstance().execute("PF('editarTelefonoD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSectorD");
                    RequestContext.getCurrentInstance().execute("PF('editarSectorD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 7) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoD");
                    RequestContext.getCurrentInstance().execute("PF('editarMotivoD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 8) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarLogroD");
                    RequestContext.getCurrentInstance().execute("PF('editarLogroD').show()");
                    cualCelda = -1;
                }
            }
        } else if (cualtabla == 8) {
            if (vigenciasDomiciliariaSeleccionada != null) {
                editarVigenciaDomiciliaria = vigenciasDomiciliariaSeleccionada;

                if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacionFamiliarD");
                    RequestContext.getCurrentInstance().execute("PF('editarObservacionFamiliarD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCondicionesGeneralesD");
                    RequestContext.getCurrentInstance().execute("PF('editarCondicionesGeneralesD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDistribucionD");
                    RequestContext.getCurrentInstance().execute("PF('editarDistribucionD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDescrGeneralD");
                    RequestContext.getCurrentInstance().execute("PF('editarDescrGeneralD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 14) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarOtrosServiciosD");
                    RequestContext.getCurrentInstance().execute("PF('editarOtrosServiciosD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 28) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarOtrosAportesD");
                    RequestContext.getCurrentInstance().execute("PF('editarOtrosAportesD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 36) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarOtrosEgresosD");
                    RequestContext.getCurrentInstance().execute("PF('editarOtrosEgresosD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 37) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacionesD");
                    RequestContext.getCurrentInstance().execute("PF('editarObservacionesD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 38) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoFinalD");
                    RequestContext.getCurrentInstance().execute("PF('editarConceptoFinalD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 39) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoSocialD");
                    RequestContext.getCurrentInstance().execute("PF('editarConceptoSocialD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 45) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarPersonasPresentesD");
                    RequestContext.getCurrentInstance().execute("PF('editarPersonasPresentesD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 46) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVisitaD");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaVisitaD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 47) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarProfesionalD");
                    RequestContext.getCurrentInstance().execute("PF('editarProfesionalD').show()");
                    cualCelda = -1;
                }
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void guardarTodo() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listAntecedentesMBorrar.isEmpty()) {
                    System.out.println("entra a antecedentes borrar");
                    administrarVigDomiciliarias.borrarAntecedenteM(listAntecedentesMBorrar);
                    listAntecedentesMBorrar.clear();
                }
                if (!listAntecedentesMCrear.isEmpty()) {
                    System.out.println("entra a antecedentes crear");
                    administrarVigDomiciliarias.crearAntecedenteM(listAntecedentesMCrear);
                    listAntecedentesMCrear.clear();
                }
                if (!listAntecedentesModificar.isEmpty()) {
                    System.out.println("Modificando...");
                    System.out.println("entra a antecedentes modificar");
                    administrarVigDomiciliarias.modificarAntecedenteM(listAntecedentesModificar);
                    listAntecedentesModificar.clear();
                }
                if (!listaFamiliaresBorrar.isEmpty()) {
                    System.out.println("entra a familiares borrar");
                    administrarVigDomiciliarias.borrarFamiliares(listaFamiliaresBorrar);
                    listaFamiliaresBorrar.clear();
                }
                if (!listaFamiliaresCrear.isEmpty()) {
                    System.out.println("entra a familiares crear");
                    administrarVigDomiciliarias.crearFamilares(listaFamiliaresCrear);
                    listaFamiliaresCrear.clear();
                }
                if (!listaFamiliaresModificar.isEmpty()) {
                    System.out.println("entra a familiares modificar");
                    administrarVigDomiciliarias.modificarFamiliares(listaFamiliaresModificar);
                    listaFamiliaresModificar.clear();
                }
                if (!listDireccionesBorrar.isEmpty()) {
                    System.out.println("entra a Direcciones borrar");
                    administrarVigDomiciliarias.borrarDirecciones(listDireccionesBorrar);
                    listDireccionesBorrar.clear();
                }
                if (!listDireccionesCrear.isEmpty()) {
                    System.out.println("entra a direcciones crear");
                    administrarVigDomiciliarias.crearDirecciones(listDireccionesCrear);
                    listDireccionesCrear.clear();
                }
                if (!listDireccionesModificar.isEmpty()) {
                    System.out.println("entra a direcciones modificar");
                    administrarVigDomiciliarias.modificarDirecciones(listDireccionesModificar);
                    listDireccionesModificar.clear();
                }

                if (!listTelefonosBorrar.isEmpty()) {
                    System.out.println("entra a telefonos borrar");
                    for (int i = 0; i < listTelefonosBorrar.size(); i++) {
                        if (listTelefonosBorrar.get(i).getTipotelefono().getSecuencia() == null) {
                            listTelefonosBorrar.get(i).setTipotelefono(null);
                            administrarVigDomiciliarias.borrarTelefonos(listTelefonosBorrar.get(i));
                        } else if (listTelefonosBorrar.get(i).getCiudad().getSecuencia() == null) {
                            listTelefonosBorrar.get(i).setCiudad(null);
                            administrarVigDomiciliarias.borrarTelefonos(listTelefonosBorrar.get(i));
                        } else {
                            administrarVigDomiciliarias.borrarTelefonos(listTelefonosBorrar.get(i));
                        }
                    }
                    listTelefonosBorrar.clear();
                }

                if (!listTelefonosCrear.isEmpty()) {
                    System.out.println("entra a telefonos crear");
                    for (int i = 0; i < listTelefonosCrear.size(); i++) {
                        System.out.println("Creando...");
                        if (listTelefonosCrear.get(i).getTipotelefono().getSecuencia() == null) {
                            listTelefonosCrear.get(i).setTipotelefono(null);
                            administrarVigDomiciliarias.crearTelefonos(listTelefonosCrear.get(i));
                        } else if (listTelefonosCrear.get(i).getCiudad().getSecuencia() == null) {
                            listTelefonosCrear.get(i).setCiudad(null);
                            administrarVigDomiciliarias.crearTelefonos(listTelefonosCrear.get(i));
                        } else {
                            administrarVigDomiciliarias.crearTelefonos(listTelefonosCrear.get(i));
                        }
                    }
                    listTelefonosCrear.clear();
                }
                if (!listTelefonosModificar.isEmpty()) {
                    System.out.println("entra a telefonos modificar");
                    administrarVigDomiciliarias.modificarTelefonos(listTelefonosModificar);
                    listTelefonosModificar.clear();
                }
                if (!listVigenciaEstadoCivilBorrar.isEmpty()) {
                    System.out.println("entra a estado civil borrar");
                    for (int i = 0; i < listVigenciaEstadoCivilBorrar.size(); i++) {
                        System.out.println("Borrando...");
                    }
                    administrarVigDomiciliarias.borrarVigenciasEstadosCiviles(listVigenciaEstadoCivilBorrar);
                    listVigenciaEstadoCivilBorrar.clear();
                }
                if (!listVigenciaEstadoCivilCrear.isEmpty()) {
                    System.out.println("entra a estado civil crear");
                    administrarVigDomiciliarias.crearVigenciasEstadosCiviles(listVigenciaEstadoCivilCrear);
                    listVigenciaEstadoCivilCrear.clear();
                }
                if (!listVigenciaEstadoCivilModificar.isEmpty()) {
                    System.out.println("entra a estado civil modificar");
                    System.out.println("Modificando...");
                    administrarVigDomiciliarias.modificarVigenciasEstadosCiviles(listVigenciaEstadoCivilModificar);
                    listVigenciaEstadoCivilModificar.clear();
                }

                if (!listVigenciasFormalesBorrar.isEmpty()) {
                    System.out.println("entra a educación borrar");
                    administrarVigDomiciliarias.borrarVigenciaFormal(listVigenciasFormalesBorrar);
                    listVigenciasFormalesBorrar.clear();
                }
                if (!listVigenciasFormalesCrear.isEmpty()) {
                    System.out.println("entra a educación crear");
                    administrarVigDomiciliarias.crearVigenciaFormal(listVigenciasFormalesCrear);
                    listVigenciasFormalesCrear.clear();
                }
                if (!listVigenciasFormalesModificar.isEmpty()) {
                    System.out.println("entra a educación modificar");
                    administrarVigDomiciliarias.modificarVigenciaFormal(listVigenciasFormalesModificar);
                    listVigenciasFormalesModificar.clear();
                }

                if (!listhvExpLaboralesBorrar.isEmpty()) {
                    System.out.println("entra a exp laboral modificar");
                    administrarVigDomiciliarias.borrarExperienciaLaboral(listhvExpLaboralesBorrar);
                    listhvExpLaboralesBorrar.clear();
                }
                if (!listhvExpLaboralesCrear.isEmpty()) {
                    System.out.println("entra a exp laboral crear");
                    administrarVigDomiciliarias.crearExperienciaLaboral(listhvExpLaboralesCrear);
                    listhvExpLaboralesCrear.clear();
                }
                if (!listhvExpLaboralesModificar.isEmpty()) {
                    System.out.println("entra a exp laboral modificar");
                    administrarVigDomiciliarias.editarExperienciaLaboral(listhvExpLaboralesModificar);
                    listhvExpLaboralesModificar.clear();
                }

                if (!listHojasdeVidaModificar.isEmpty()) {
                    System.out.println("entra a hoja de vida modificar");
                    administrarVigDomiciliarias.editarHojadeVida(listHojasdeVidaModificar);
                    listHojasdeVidaModificar.clear();
                }

//                if (!listPersonasModificar.isEmpty()) {
//                    System.out.println("entra a personas modificar");
//                    administrarVigDomiciliarias.editarPersona(listPersonasModificar);
//                    listPersonasModificar.clear();
//                }
                if (!listVigenciasDomiciliariasBorrar.isEmpty()) {
                    System.out.println("entra a visita domiciliaris borrar");
                    System.out.println("tamaño lista visitas domiciliarias borrar " + listVigenciasDomiciliariasBorrar.size());
                    administrarVigDomiciliarias.borrarVigencia(listVigenciasDomiciliariasBorrar);
                    listVigenciasDomiciliariasBorrar.clear();
                }
                if (!listVigenciasDomiciliariasCrear.isEmpty()) {
                    System.out.println("entra a visita domiciliaris crear");
                    System.out.println("tamaño lista visitas domiciliarias crear " + listVigenciasDomiciliariasCrear.size());
                    administrarVigDomiciliarias.crearVigencia(listVigenciasDomiciliariasCrear);
                    listVigenciasDomiciliariasCrear.clear();
                }
                if (!listVigenciasDomiciliariasModificar.isEmpty()) {
                    System.out.println("entra a visita domiciliaris modificar");
                    System.out.println("tamaño lista visitas domiciliarias modificar " + listVigenciasDomiciliariasModificar.size());
                    administrarVigDomiciliarias.modificarVigencia(listVigenciasDomiciliariasModificar);
                    listVigenciasDomiciliariasModificar.clear();
                }

                listaFamiliares = null;
                getListaFamiliares();
                listAntecedentesM = null;
                getListAntecedentesM();
                listDirecciones = null;
                getListDirecciones();
                listTelefonos = null;
                getListTelefonos();
                listVigenciaEstadoCivil = null;
                listVigenciasFormales = null;
                listhvExpLaborales = null;
                getListhvExpLaborales();
                getListVigenciasFormales();
                contarRegistrosEducacion();
                getListVigenciaEstadoCivil();
                contarRegistrosEstadoCivil();
                contarRegistrosCiudadesTelefonos();
                contarRegistrosAntecedentesM();
                contarRegistrosFamiliares();
                contarRegistrosDirecciones();
                contarRegistrosExp();
                RequestContext.getCurrentInstance().update("form:datosAntecedentes");
                RequestContext.getCurrentInstance().update("form:datosFamiliares");
                RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
                RequestContext.getCurrentInstance().update("form:datosTelefonosPersona");
                RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
                RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
                RequestContext.getCurrentInstance().update("form:datosExperiencia");
                RequestContext.getCurrentInstance().update("form:cargoPostulado");
                System.out.println("entra a actualizar campos");
                actualizarCamposVigDomiciliaria();
                RequestContext.getCurrentInstance().update("formularioDialogos:lovBuscarVisita");
                k = 0;
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                System.out.println("se guardaron los cambios");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                familiarSeleccionado = null;
                antecedentemSeleccionado = null;
                vigenciaFormalSeleccionada = null;
                direccionSeleccionada = null;
                telefonoSeleccionado = null;
                vigenciaEstadoCivilSeleccionado = null;
                hvexpSeleccionada = null;
                lovVisitas = null;
                getLovVisitas();
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.getMessage());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void activarOtroServicio() {
        if (vigenciasDomiciliariaSeleccionada.isRecotro() == true) {
            System.out.println("activo otros servicios");
            activarotroservicio = false;
            RequestContext.getCurrentInstance().update("form:otrosservicios");
        } else {
            System.out.println("desactivo otros servicios");
            activarotroservicio = true;
            RequestContext.getCurrentInstance().update("form:otrosservicios");
        }
    }

    public void activarOtroEgreso() {
        if (vigenciasDomiciliariaSeleccionada.isEgresoOtros() == true) {
            System.out.println("activo otros egresos");
            activarotroegreso = false;
            RequestContext.getCurrentInstance().update("form:otrosEgresos");
        } else {
            System.out.println("desactivo otros egresos");
            activarotroegreso = true;
            RequestContext.getCurrentInstance().update("form:otrosEgresos");
        }
    }

    public void activarOtroAporte() {
        if (vigenciasDomiciliariaSeleccionada.isAporteotro() == true) {
            System.out.println("activo otros aportes");
            activarotroaporte = false;
            RequestContext.getCurrentInstance().update("form:otrosIngresos");
        } else {
            System.out.println("desactivo otros aportes");
            activarotroaporte = true;
            RequestContext.getCurrentInstance().update("form:otrosIngresos");
        }
    }

    public void agregarNuevaVigenciaDomiciliaria() {
        vigenciasDomiciliariaSeleccionada = nuevaVigenciaDomiciliaria;
        vigenciasDomiciliariaSeleccionada.setPersona(persona);
        actualizarCamposVigDomiciliaria();
        checkDefault();
    }

    public void duplicarVigenciaDomiciliaria() {

        System.out.println("entró a duplicar Vigencia Domiciliaria");

        k++;
        x = BigDecimal.valueOf(k);
        duplicarVigenciaDomiciliaria = new VigenciasDomiciliarias();
        duplicarVigenciaDomiciliaria.setSecuencia(x);
        duplicarVigenciaDomiciliaria.setPersona(persona);
        duplicarVigenciaDomiciliaria.setCalificacionfamiliar(vigenciasDomiciliariaSeleccionada.getCalificacionfamiliar());
        duplicarVigenciaDomiciliaria.setConceptofinal(vigenciasDomiciliariaSeleccionada.getConceptofinal());
        duplicarVigenciaDomiciliaria.setConceptosocial(vigenciasDomiciliariaSeleccionada.getConceptosocial());
        duplicarVigenciaDomiciliaria.setCondicionesgenerales(vigenciasDomiciliariaSeleccionada.getCondicionesgenerales());
        duplicarVigenciaDomiciliaria.setCondicionfamiliar(vigenciasDomiciliariaSeleccionada.getCondicionfamiliar());
        duplicarVigenciaDomiciliaria.setCondicionsocial(vigenciasDomiciliariaSeleccionada.getCondicionsocial());
        duplicarVigenciaDomiciliaria.setConstruccion(vigenciasDomiciliariaSeleccionada.getConstruccion());
        duplicarVigenciaDomiciliaria.setDescripcionvivienda(vigenciasDomiciliariaSeleccionada.getDescripcionvivienda());
        duplicarVigenciaDomiciliaria.setDetalleotrasinversiones(vigenciasDomiciliariaSeleccionada.getDetalleotrasinversiones());
        duplicarVigenciaDomiciliaria.setDetalleotroingreso(vigenciasDomiciliariaSeleccionada.getDetalleotroingreso());
        duplicarVigenciaDomiciliaria.setDetalleotrosservicios(vigenciasDomiciliariaSeleccionada.getDetalleotrosservicios());
        duplicarVigenciaDomiciliaria.setDistribucionvivienda(vigenciasDomiciliariaSeleccionada.getDistribucionvivienda());
        duplicarVigenciaDomiciliaria.setFecha(new Date());
        duplicarVigenciaDomiciliaria.setIngresoabuelo(vigenciasDomiciliariaSeleccionada.getIngresoabuelo());
        duplicarVigenciaDomiciliaria.setIngresohermano(vigenciasDomiciliariaSeleccionada.getIngresohermano());
        duplicarVigenciaDomiciliaria.setIngresomama(vigenciasDomiciliariaSeleccionada.getIngresomama());
        duplicarVigenciaDomiciliaria.setIngresootro(vigenciasDomiciliariaSeleccionada.getIngresootro());
        duplicarVigenciaDomiciliaria.setIngresopapa(vigenciasDomiciliariaSeleccionada.getIngresopapa());
        duplicarVigenciaDomiciliaria.setIngresos(vigenciasDomiciliariaSeleccionada.getIngresos());
        duplicarVigenciaDomiciliaria.setIngresotio(vigenciasDomiciliariaSeleccionada.getIngresotio());
        duplicarVigenciaDomiciliaria.setInversionalimentacion(vigenciasDomiciliariaSeleccionada.getInversionalimentacion());
        duplicarVigenciaDomiciliaria.setInversionarriendo(vigenciasDomiciliariaSeleccionada.getInversionarriendo());
        duplicarVigenciaDomiciliaria.setInversioneducacion(vigenciasDomiciliariaSeleccionada.getInversioneducacion());
        duplicarVigenciaDomiciliaria.setInversionmedica(vigenciasDomiciliariaSeleccionada.getInversionmedica());
        duplicarVigenciaDomiciliaria.setInversionotros(vigenciasDomiciliariaSeleccionada.getInversionotros());
        duplicarVigenciaDomiciliaria.setInversionrecreacion(vigenciasDomiciliariaSeleccionada.getInversionrecreacion());
        duplicarVigenciaDomiciliaria.setInversionservicios(vigenciasDomiciliariaSeleccionada.getInversionservicios());
        duplicarVigenciaDomiciliaria.setMotivacioncargo(vigenciasDomiciliariaSeleccionada.getMotivacioncargo());
        duplicarVigenciaDomiciliaria.setNivelacademico(vigenciasDomiciliariaSeleccionada.getNivelacademico());
        duplicarVigenciaDomiciliaria.setObservaciones(vigenciasDomiciliariaSeleccionada.getObservaciones());
        duplicarVigenciaDomiciliaria.setObservacionfamiliar(vigenciasDomiciliariaSeleccionada.getObservacionfamiliar());
        duplicarVigenciaDomiciliaria.setOrigenarrendamiento(vigenciasDomiciliariaSeleccionada.getOrigenarrendamiento());
        duplicarVigenciaDomiciliaria.setOrigenauxilios(vigenciasDomiciliariaSeleccionada.getOrigenauxilios());
        duplicarVigenciaDomiciliaria.setOrigencdt(vigenciasDomiciliariaSeleccionada.getOrigencdt());
        duplicarVigenciaDomiciliaria.setOrigenindependiente(vigenciasDomiciliariaSeleccionada.getOrigenindependiente());
        duplicarVigenciaDomiciliaria.setOrigenpension(vigenciasDomiciliariaSeleccionada.getOrigenpension());
        duplicarVigenciaDomiciliaria.setOrigensalario(vigenciasDomiciliariaSeleccionada.getOrigensalario());
        duplicarVigenciaDomiciliaria.setPersonaspresentes(vigenciasDomiciliariaSeleccionada.getPersonaspresentes());
        duplicarVigenciaDomiciliaria.setProfesional(vigenciasDomiciliariaSeleccionada.getProfesional());
        duplicarVigenciaDomiciliaria.setServicioagua(vigenciasDomiciliariaSeleccionada.getServicioagua());
        duplicarVigenciaDomiciliaria.setServicioalcantarillado(vigenciasDomiciliariaSeleccionada.getServicioalcantarillado());
        duplicarVigenciaDomiciliaria.setServicioaseo(vigenciasDomiciliariaSeleccionada.getServicioaseo());
        duplicarVigenciaDomiciliaria.setServicioluz(vigenciasDomiciliariaSeleccionada.getServicioluz());
        duplicarVigenciaDomiciliaria.setServiciootros(vigenciasDomiciliariaSeleccionada.getServiciootros());
        duplicarVigenciaDomiciliaria.setServicioparabolica(vigenciasDomiciliariaSeleccionada.getServicioparabolica());
        duplicarVigenciaDomiciliaria.setServiciotelefono(vigenciasDomiciliariaSeleccionada.getServiciotelefono());
        duplicarVigenciaDomiciliaria.setServiciotransporte(vigenciasDomiciliariaSeleccionada.getServiciotransporte());
        System.out.println("terminó de duplicar Campos visita");
        listVigenciasDomiciliariasCrear.add(duplicarVigenciaDomiciliaria);
        vigenciasDomiciliariaSeleccionada = duplicarVigenciaDomiciliaria;
        actualizarCamposVigDomiciliaria();
        checkDefault();
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void checkDefault() {
        activarotroaporte = true;
        activarotroegreso = true;
        activarotroservicio = true;
        RequestContext.getCurrentInstance().update("form:servagua");
        RequestContext.getCurrentInstance().update("form:servluz");
        RequestContext.getCurrentInstance().update("form:servtel");
        RequestContext.getCurrentInstance().update("form:servpara");
        RequestContext.getCurrentInstance().update("form:servtrans");
        RequestContext.getCurrentInstance().update("form:servalc");
        RequestContext.getCurrentInstance().update("form:servaseo");
        RequestContext.getCurrentInstance().update("form:servotro");
        RequestContext.getCurrentInstance().update("form:otrosservicios");

        RequestContext.getCurrentInstance().update("form:editarIngresos");
        RequestContext.getCurrentInstance().update("form:comind");
        RequestContext.getCurrentInstance().update("form:arriendo");
        RequestContext.getCurrentInstance().update("form:pension");
        RequestContext.getCurrentInstance().update("form:Salario");
        RequestContext.getCurrentInstance().update("form:cdt");
        RequestContext.getCurrentInstance().update("form:Auxilios");

        RequestContext.getCurrentInstance().update("form:Padre");
        RequestContext.getCurrentInstance().update("form:Madre");
        RequestContext.getCurrentInstance().update("form:Hermano");
        RequestContext.getCurrentInstance().update("form:Abuelo");
        RequestContext.getCurrentInstance().update("form:Tío");
        RequestContext.getCurrentInstance().update("form:Otro");
        RequestContext.getCurrentInstance().update("form:otrosIngresos");

        RequestContext.getCurrentInstance().update("form:educacion");
        RequestContext.getCurrentInstance().update("form:recreacion");
        RequestContext.getCurrentInstance().update("form:alimentacion");
        RequestContext.getCurrentInstance().update("form:medica");
        RequestContext.getCurrentInstance().update("form:Arriendo");
        RequestContext.getCurrentInstance().update("form:Servicios");
        RequestContext.getCurrentInstance().update("form:egresootro");
        RequestContext.getCurrentInstance().update("form:otrosEgresos");

        RequestContext.getCurrentInstance().update("form:editarObservaciones");
        RequestContext.getCurrentInstance().update("form:editarConceptoF");
        RequestContext.getCurrentInstance().update("form:editarConceptoS");
        RequestContext.getCurrentInstance().update("form:editarCondicionesF");
        RequestContext.getCurrentInstance().update("form:editarCondicionesS");
        RequestContext.getCurrentInstance().update("form:editarSituacionE");
        RequestContext.getCurrentInstance().update("form:editarNivelAc");
        RequestContext.getCurrentInstance().update("form:editarMotivacionC");
        RequestContext.getCurrentInstance().update("form:editarPersonas");

    }

    public void mostrarDialogoInsertarTelefono() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroTelefono");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTelefono').show()");
    }

    public void mostrarDialogoInsertarDireccion() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroDireccion");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDireccion').show()");
    }

    public void mostrarDialogoInsertarAntecedente() {
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoRegistroAntecedentesM");
        RequestContext.getCurrentInstance().execute("PF('nuevoRegistroAntecedentesM').show()");
        lovAntecedentesDescripcion = new ArrayList<String>();
    }

    public void mostrarDialogoInsertarEstadoCivil() {
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoRegistroEstadoCivil");
        RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEstadoCivil').show()");
    }

    public void mostrarDialogoInsertarEducacion() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaFormal");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaFormal').show()");
    }

    public void mostrarDialogoInsertarExpLaboral() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroExpLaborales");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroExpLaborales').show()");
    }

    public void mostrarDialogoInsertarFamiliares() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroFamiliarPersona");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFamiliarPersona').show()");
    }

    public void mostrarDialogoInsertarPersona() {
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFamiliarPersona");
        RequestContext.getCurrentInstance().execute("PF('nuevoFamiliarPersona').show()");
    }

    public void guardarSalir() {
        guardarTodo();
        salirTodo();
    }

    public void cancelarModificacionTodo() {
        cancelarModificacionAntecedenteM();
        cancelarModificacionDireccion();
        cancelarModificacionEducacion();
        cancelarModificacionEstadosCiviles();
        cancelarModificacionExpLaboral();
        cancelarModificacionFamiliares();
        cancelarModificacionTelefono();
        checkDefault();
        actualizarCamposVigDomiciliaria();

    }

    public void salirTodo() {
        salirAntecedenteM();
        salirDirecciones();
        salirEducacion();
        salirEstadosCiviles();
        salirExpLaborales();
        salirFamiliares();
        salirTelefonos();
    }

    public void cancelarSalir() {
        cancelarModificacionTodo();
        salirTodo();
    }

    public void contarRegistrosBuscarVisita() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroBuscarVisita");
    }

    public void contarRegistrosLovVisita() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovVisita");

    }

    public void mostrarDialogoBuscarVisita() {
        System.out.println("entró a la funcion mostrarDialogoBuscarVisita");
        lovVisitas = null;
        getLovVisitas();
        System.out.println("lov visitas : " + lovVisitas);
        if (lovVisitas == null) {

            if (lovVisitas.isEmpty()) {
                RequestContext.getCurrentInstance().execute("PF('sinVisitas').show()");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:buscarVisita");
                RequestContext.getCurrentInstance().execute("PF('buscarVisita').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:buscarVisita");
            RequestContext.getCurrentInstance().execute("PF('buscarVisita').show()");
        }

    }

    public void actualizarVisita() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listVigenciasDomiciliarias.isEmpty()) {
            listVigenciasDomiciliarias.clear();
            listVigenciasDomiciliarias.add(visitaSeleccionada);
//            empleadoSeleccionado = listaEmpleadosNovedad.get(0);
        }
        vigenciasDomiciliariaSeleccionada = visitaSeleccionada;
        aceptar = true;
        listVigenciasDomiciliariasFiltrar = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:lovBuscarVisita:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBuscarVisita').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('buscarVisita').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:buscarVisita");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovBuscarVisita");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarBuscar");
        actualizarCamposVigDomiciliaria();
        checkDefault();

    }

    public void verificarRastro() {
        if (cualtabla == 1) {
            if (direccionSeleccionada != null) {
                int resultado = administrarRastros.obtenerTabla(direccionSeleccionada.getSecuencia(), "DIRECCIONES");
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBDirecciones').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroDirecciones').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroDirecciones').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroDirecciones').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroDirecciones').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("DIRECCIONES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoDirecciones').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoDirecciones').show()");
            }
        } else if (cualtabla == 2) {
            if (telefonoSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(telefonoSeleccionado.getSecuencia(), "TELEFONOS");
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBTelefonos').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroTelefonos').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroTelefonos').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroTelefonos').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroTelefonos').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("TELEFONOS")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoTelefonos').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoTelefonos').show()");
            }

        } else if (cualtabla == 3) {
            if (vigenciaEstadoCivilSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(vigenciaEstadoCivilSeleccionado.getSecuencia(), "VIGENCIASESTADOSCIVILES");
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBEstadosCiviles').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroEstadosCiviles').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroEstadosCiviles').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroEstadosCiviles').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroEstadosCiviles').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASESTADOSCIVILES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoEstadosCiviles').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoEstadosCiviles').show()");
            }

        } else if (cualtabla == 4) {

            if (antecedentemSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(antecedentemSeleccionado.getSecuencia(), "SOANTECEDENTESMEDICOS");
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBAntecedentes').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroAntecedentes').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroAntecedentes').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroAntecedentes').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroAntecedentes').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("SOANTECEDENTESMEDICOS")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoAntecedentes').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoAntecedentes').show()");
            }

        } else if (cualtabla == 5) {

            if (familiarSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(familiarSeleccionado.getSecuencia().toBigInteger(), "FAMILIARES");
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBFamiliares').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroFamiliares').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroFamiliares').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroFamiliares').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroFamiliares').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("FAMILIARES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoFamiliares').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoFamiliares').show()");
            }

        } else if (cualtabla == 6) {

            if (vigenciaFormalSeleccionada != null) {
                int resultado = administrarRastros.obtenerTabla(vigenciaFormalSeleccionada.getSecuencia(), "VIGENCIASFORMALES");
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBEducacion').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroEducacion').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroEducacion').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroEducacion').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroEducacion').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASFORMALES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoEducacion').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoEducacion').show()");
            }

        } else if (cualtabla == 7) {

            if (hvexpSeleccionada != null) {
                int resultado = administrarRastros.obtenerTabla(hvexpSeleccionada.getSecuencia(), "HVEXPERIENCIASLABORALES");
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBExpLaboral').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroExpLaboral').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroExpLaboral').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroExpLaboral').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroExpLaboral').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("HVEXPERIENCIASLABORALES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoExpLaboral').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoExpLaboral').show()");
            }

        } else if (cualtabla == 8) {
            if (vigenciasDomiciliariaSeleccionada != null) {
                int resultado = administrarRastros.obtenerTabla(vigenciasDomiciliariaSeleccionada.getSecuencia().toBigInteger(), "VIGENCIASDOMICILIARIAS");
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBVisitaDomiciliaria').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroVisitaDomiciliaria').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroVisitaDomiciliaria').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroVisitaDomiciliaria').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroVisitaDomiciliaria').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASDOMICILIARIAS")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoVisitaDomiciliaria').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoVisitaDomiciliaria').show()");
            }
        }

    }

    public void borrarVigenciaDomiciliaria() {
        if (vigenciasDomiciliariaSeleccionada != null) {
            if (!listVigenciasDomiciliariasModificar.isEmpty() && listVigenciasDomiciliariasModificar.contains(vigenciasDomiciliariaSeleccionada)) {
                listVigenciasDomiciliariasModificar.remove(listVigenciasDomiciliariasModificar.indexOf(vigenciasDomiciliariaSeleccionada));
                listVigenciasDomiciliariasBorrar.add(vigenciasDomiciliariaSeleccionada);
            } else if (!listVigenciasDomiciliariasCrear.isEmpty() && listVigenciasDomiciliariasCrear.contains(vigenciasDomiciliariaSeleccionada)) {
                listVigenciasDomiciliariasCrear.remove(listVigenciasDomiciliariasCrear.indexOf(vigenciasDomiciliariaSeleccionada));
            } else {
                listVigenciasDomiciliariasBorrar.add(vigenciasDomiciliariaSeleccionada);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (guardado == true) {
                guardado = false;
            }
            vigenciasDomiciliariaSeleccionada = new VigenciasDomiciliarias();
            vigenciasDomiciliariaSeleccionada.setPersona(persona);
            actualizarCamposVigDomiciliaria();
            checkDefault();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void cancelarCambioVisita() {
        listVigenciasDomiciliariasFiltrar = null;
        visitaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovBuscarVisita:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBuscarVisita').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('buscarVisita').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:buscarVisita");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovBuscarVisita");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarBuscar");
    }

    public void elegirBorrar() {
        if (cualtabla == 1) {
            borrarDirecciones();
        } else if (cualtabla == 2) {
            borrarTelefonos();
        } else if (cualtabla == 3) {
            borrandoVigenciasEstadosCiviles();
        } else if (cualtabla == 4) {
            borrarAntecedenteMedico();
        } else if (cualtabla == 4) {
            borrarFamiliar();
        } else if (cualtabla == 6) {
            borrarVigenciaFormal();
        } else if (cualtabla == 7) {
            borrarExpLaborales();
        } else if (cualtabla == 8) {
            borrarVigenciaDomiciliaria();
        }
    }

    public void elegirDuplicar() {
        if (cualtabla == 1) {
            duplicarDireccion();
        } else if (cualtabla == 2) {
            duplicarTelefono();
        } else if (cualtabla == 3) {
            duplicandoVigenciaEstadoCivil();
        } else if (cualtabla == 4) {
            duplicarAntecedenteMedico();
        } else if (cualtabla == 4) {
            duplicarFamiliar();
        } else if (cualtabla == 6) {
            duplicarVF();
        } else if (cualtabla == 7) {
            duplicarExpLaboral();
        } else if (cualtabla == 8) {
            duplicarVigenciaDomiciliaria();
        }
    }

    public void modificarCondiciones() {
        modificarVigenciaDomiciliaria(vigenciasDomiciliariaSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:editarCondiciones");
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void modificarDistribucion() {
        modificarVigenciaDomiciliaria(vigenciasDomiciliariaSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:editarDistribucion");
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void modificarDescripcion() {
        modificarVigenciaDomiciliaria(vigenciasDomiciliariaSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:editarDescripcionGeneral");
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void modificarObservaciones() {
        modificarVigenciaDomiciliaria(vigenciasDomiciliariaSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:editarObservaciones");
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void modificarConceptoF() {
        modificarVigenciaDomiciliaria(vigenciasDomiciliariaSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:editarConceptoF");
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void modificarConceptoS() {
        modificarVigenciaDomiciliaria(vigenciasDomiciliariaSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:editarConceptoS");
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void modificarPersonasAsistenes() {
        modificarVigenciaDomiciliaria(vigenciasDomiciliariaSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:editarPersonas");
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

/// /////SETS Y GETS///////
    public List<VigenciasDomiciliarias> getListVigenciasDomiciliarias() {
        if (listVigenciasDomiciliarias == null) {
            listVigenciasDomiciliarias = administrarVigDomiciliarias.vigenciasDomiciliariasporPersona(persona.getSecuencia());
        }
        return listVigenciasDomiciliarias;
    }

    public void setListVigenciasDomiciliarias(List<VigenciasDomiciliarias> listVigenciasDomiciliarias) {
        this.listVigenciasDomiciliarias = listVigenciasDomiciliarias;
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
        if (listAntecedentesM == null) {
            listAntecedentesM = administrarVigDomiciliarias.buscarAntecedentesMedicos(persona.getSecuencia());
        }
        return listAntecedentesM;
    }

    public List<Ciudades> getLovCiudades() {
        if (lovCiudades == null) {
            lovCiudades = administrarVigDomiciliarias.lovCiudades();
        }
        return lovCiudades;
    }

    public List<Cargos> getLovCargos() {
        if (lovCargos == null) {
            lovCargos = administrarVigDomiciliarias.lovCargos();
        }
        return lovCargos;
    }

    public List<TiposTelefonos> getLovTiposTelefonos() {
        if (lovTiposTelefonos == null) {
            lovTiposTelefonos = administrarVigDomiciliarias.lovTiposTelefonos();
        }
        return lovTiposTelefonos;
    }

    public List<TiposEducaciones> getLovTiposEducaciones() {
        if (lovTiposEducaciones == null) {
            lovTiposEducaciones = administrarVigDomiciliarias.lovTiposEducaciones();
        }
        return lovTiposEducaciones;
    }

    public List<Profesiones> getLovProfesiones() {
        if (lovProfesiones == null) {
            lovProfesiones = administrarVigDomiciliarias.lovProfesiones();
        }
        return lovProfesiones;
    }

    public List<AdiestramientosF> getLovAdiestramientos() {
        if (lovAdiestramientos == null) {
            lovAdiestramientos = administrarVigDomiciliarias.lovAdiestramientos();
        }
        return lovAdiestramientos;
    }

    public List<Instituciones> getLovInstituciones() {
        if (lovInstituciones == null) {
            lovInstituciones = administrarVigDomiciliarias.lovInstituciones();
        }
        return lovInstituciones;
    }

    public List<MotivosRetiros> getLovMotivos() {
        if (lovMotivos == null) {
            lovMotivos = administrarVigDomiciliarias.lovMotivosRetiros();
        }
        return lovMotivos;
    }

    public List<Personas> getLovPersonas() {
        if (lovPersonas == null) {
            lovPersonas = administrarVigDomiciliarias.lovPersonas();
        }
        return lovPersonas;
    }

    public List<SoTiposAntecedentes> getLovTiposAntecedentes() {
        if (lovTiposAntecedentes == null) {
            lovTiposAntecedentes = administrarVigDomiciliarias.lovTiposAntecedentes();
        }
        return lovTiposAntecedentes;
    }

    public List<EstadosCiviles> getLovEstadosCiviles() {
        if (lovEstadosCiviles == null) {
            lovEstadosCiviles = administrarVigDomiciliarias.lovVigenciasEstadosCiviles();
        }
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

    public Telefonos getTelefonoActual() {
        telefonoActual = administrarVigDomiciliarias.telefonoActualPersona(persona.getSecuencia());
        return telefonoActual;
    }

    public void setTelefonoActual(Telefonos telefonoActual) {
        this.telefonoActual = telefonoActual;
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
        if (lovTiposFamiliares == null) {
            lovTiposFamiliares = administrarVigDomiciliarias.lovTiposFamiliares();
        }
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovPersonasFamiliares");
        infoRegistroPersonas = String.valueOf(tabla.getRowCount());
        return infoRegistroPersonas;
    }

    public void setInfoRegistroPersonas(String infoRegistroPersonas) {
        this.infoRegistroPersonas = infoRegistroPersonas;
    }

    public String getInfoRegistroTipoFamiliar() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovFamiliares");
        infoRegistroTipoFamiliar = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoFamiliar;
    }

    public void setInfoRegistroTipoFamiliar(String infoRegistroTipoFamiliar) {
        this.infoRegistroTipoFamiliar = infoRegistroTipoFamiliar;
    }

    public String getInfoRegistroTipoDocumento() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTipoDocumento");
        infoRegistroTipoDocumento = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoDocumento;
    }

    public void setInfoRegistroTipoDocumento(String infoRegistroTipoDocumento) {
        this.infoRegistroTipoDocumento = infoRegistroTipoDocumento;
    }

    public String getInfoRegistroCiudadDocumento() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCiudadDocumento");
        infoRegistroCiudadDocumento = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudadDocumento;
    }

    public void setInfoRegistroCiudadDocumento(String infoRegistroCiudadDocumento) {
        this.infoRegistroCiudadDocumento = infoRegistroCiudadDocumento;
    }

    public String getInfoRegistroCiudadNacimiento() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCiudadNacimiento");
        infoRegistroCiudadNacimiento = String.valueOf(tabla.getRowCount());
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
        if (lovSectoresEconomicos == null) {
            lovSectoresEconomicos = administrarVigDomiciliarias.lovSectoresEconomicos();
        }
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDireccionesPersona");
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
        return distribucion;
    }

    public void setDistribucion(String distribucion) {
        this.distribucion = distribucion;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public String getDescGeneral() {
        return descGeneral;
    }

    public void setDescGeneral(String descGeneral) {
        this.descGeneral = descGeneral;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getConceptofinal() {
        return conceptofinal;
    }

    public void setConceptofinal(String conceptofinal) {
        this.conceptofinal = conceptofinal;
    }

    public String getConceptosocial() {
        return conceptosocial;
    }

    public void setConceptosocial(String conceptosocial) {
        this.conceptosocial = conceptosocial;
    }

    public String getPersonasAtencion() {
        return personasAtencion;
    }

    public void setPersonasAtencion(String personasAtencion) {
        this.personasAtencion = personasAtencion;
    }

    public String getProfesional() {
        return profesional;
    }

    public void setProfesional(String profesional) {
        this.profesional = profesional;
    }

    public Date getDateVisita() {
        return dateVisita;
    }

    public void setDateVisita(Date dateVisita) {
        this.dateVisita = dateVisita;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public List<Ciudades> getLovCiudadDocumento() {
        if (lovCiudadDocumento == null) {
            lovCiudadDocumento = administrarVigDomiciliarias.lovCiudades();
        }
        return lovCiudadDocumento;
    }

    public void setLovCiudadDocumento(List<Ciudades> lovCiudadDocumento) {
        this.lovCiudadDocumento = lovCiudadDocumento;
    }

    public List<Ciudades> getLovCiudadDocumentoFiltrar() {
        return lovCiudadDocumentoFiltrar;
    }

    public void setLovCiudadDocumentoFiltrar(List<Ciudades> lovCiudadDocumentoFiltrar) {
        this.lovCiudadDocumentoFiltrar = lovCiudadDocumentoFiltrar;
    }

    public List<Ciudades> getLovCiudadDireccion() {
        if (lovCiudadDireccion == null) {
            lovCiudadDireccion = administrarVigDomiciliarias.lovCiudades();
        }
        return lovCiudadDireccion;
    }

    public void setLovCiudadDireccion(List<Ciudades> lovCiudadDireccion) {
        this.lovCiudadDireccion = lovCiudadDireccion;
    }

    public List<Ciudades> getLovCiudadDireccionFiltrar() {
        return lovCiudadDireccionFiltrar;
    }

    public void setLovCiudadDireccionFiltrar(List<Ciudades> lovCiudadDireccionFiltrar) {
        this.lovCiudadDireccionFiltrar = lovCiudadDireccionFiltrar;
    }

    public List<Ciudades> getLovCiudadTelefono() {
        if (lovCiudadTelefono == null) {
            lovCiudadTelefono = administrarVigDomiciliarias.lovCiudades();
        }
        return lovCiudadTelefono;
    }

    public void setLovCiudadTelefono(List<Ciudades> lovCiudadTelefono) {
        this.lovCiudadTelefono = lovCiudadTelefono;
    }

    public List<Ciudades> getLovCiudadTelefonoFiltrar() {
        return lovCiudadTelefonoFiltrar;
    }

    public void setLovCiudadTelefonoFiltrar(List<Ciudades> lovCiudadTelefonoFiltrar) {
        this.lovCiudadTelefonoFiltrar = lovCiudadTelefonoFiltrar;
    }

    public Ciudades getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public Ciudades getCiudadDocumentoSeleccionada() {
        return ciudadDocumentoSeleccionada;
    }

    public void setCiudadDocumentoSeleccionada(Ciudades ciudadDocumentoSeleccionada) {
        this.ciudadDocumentoSeleccionada = ciudadDocumentoSeleccionada;
    }

    public Ciudades getCiudadDireccionSeleccionada() {
        return ciudadDireccionSeleccionada;
    }

    public void setCiudadDireccionSeleccionada(Ciudades ciudadDireccionSeleccionada) {
        this.ciudadDireccionSeleccionada = ciudadDireccionSeleccionada;
    }

    public Ciudades getCiudadTelefonoSeleccionada() {
        return ciudadTelefonoSeleccionada;
    }

    public void setCiudadTelefonoSeleccionada(Ciudades ciudadTelefonoSeleccionada) {
        this.ciudadTelefonoSeleccionada = ciudadTelefonoSeleccionada;
    }

    public String getInfoRegistroCiudadesDirecciones() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVDireccionesCiudades");
        infoRegistroCiudadesDirecciones = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudadesDirecciones;
    }

    public void setInfoRegistroCiudadesDirecciones(String infoRegistroCiudadesDirecciones) {
        this.infoRegistroCiudadesDirecciones = infoRegistroCiudadesDirecciones;
    }

    public Direcciones getDuplicarDireccion() {
        return duplicarDireccion;
    }

    public void setDuplicarDireccion(Direcciones duplicarDireccion) {
        this.duplicarDireccion = duplicarDireccion;
    }

    public String getInfoRegistroTT() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVTiposTelefonos");
        infoRegistroTT = String.valueOf(tabla.getRowCount());
        return infoRegistroTT;
    }

    public void setInfoRegistroTT(String infoRegistroTT) {
        this.infoRegistroTT = infoRegistroTT;
    }

    public String getInfoRegistroCiudadesTelefonos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVCiudadesTelefono");
        infoRegistroCiudadesTelefonos = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudadesTelefonos;
    }

    public void setInfoRegistroCiudadesTelefonos(String infoRegistroCiudadesTelefonos) {
        this.infoRegistroCiudadesTelefonos = infoRegistroCiudadesTelefonos;
    }

    public List<TiposTelefonos> getLovTiposTelefonosFiltrar() {
        return lovTiposTelefonosFiltrar;
    }

    public void setLovTiposTelefonosFiltrar(List<TiposTelefonos> lovTiposTelefonosFiltrar) {
        this.lovTiposTelefonosFiltrar = lovTiposTelefonosFiltrar;
    }

    public TiposTelefonos getTipoTelefonoSeleccionado() {
        return tipoTelefonoSeleccionado;
    }

    public void setTipoTelefonoSeleccionado(TiposTelefonos tipoTelefonoSeleccionado) {
        this.tipoTelefonoSeleccionado = tipoTelefonoSeleccionado;
    }

    public Telefonos getDuplicarTelefono() {
        return duplicarTelefono;
    }

    public void setDuplicarTelefono(Telefonos duplicarTelefono) {
        this.duplicarTelefono = duplicarTelefono;
    }

    public List<Profesiones> getLovProfesionesFiltrar() {
        return lovProfesionesFiltrar;
    }

    public void setLovProfesionesFiltrar(List<Profesiones> lovProfesionesFiltrar) {
        this.lovProfesionesFiltrar = lovProfesionesFiltrar;
    }

    public MotivosRetiros getMotivoSeleccionado() {
        return motivoSeleccionado;
    }

    public void setMotivoSeleccionado(MotivosRetiros motivoSeleccionado) {
        this.motivoSeleccionado = motivoSeleccionado;
    }

    public Personas getLovPersonaSeleccionado() {
        return lovPersonaSeleccionado;
    }

    public void setLovPersonaSeleccionado(Personas lovPersonaSeleccionado) {
        this.lovPersonaSeleccionado = lovPersonaSeleccionado;
    }

    public String getInfoRegistroEstadoCivilLov() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEstadosCiviles");
        infoRegistroEstadoCivilLov = String.valueOf(tabla.getRowCount());
        return infoRegistroEstadoCivilLov;
    }

    public void setInfoRegistroEstadoCivilLov(String infoRegistroEstadoCivilLov) {
        this.infoRegistroEstadoCivilLov = infoRegistroEstadoCivilLov;
    }

    public EstadosCiviles getEstadoCivilSeleccionado() {
        return estadoCivilSeleccionado;
    }

    public void setEstadoCivilSeleccionado(EstadosCiviles estadoCivilSeleccionado) {
        this.estadoCivilSeleccionado = estadoCivilSeleccionado;
    }

    public VigenciasEstadosCiviles getNuevaVigenciaEstadoCivil() {
        return nuevaVigenciaEstadoCivil;
    }

    public void setNuevaVigenciaEstadoCivil(VigenciasEstadosCiviles nuevaVigenciaEstadoCivil) {
        this.nuevaVigenciaEstadoCivil = nuevaVigenciaEstadoCivil;
    }

    public VigenciasEstadosCiviles getDuplicarVigenciaEstadoCivil() {
        return duplicarVigenciaEstadoCivil;
    }

    public void setDuplicarVigenciaEstadoCivil(VigenciasEstadosCiviles duplicarVigenciaEstadoCivil) {
        this.duplicarVigenciaEstadoCivil = duplicarVigenciaEstadoCivil;
    }

    public List<SoAntecedentes> getLovAntecedentes() {
        if (lovAntecedentes == null) {
            if (tipoActualizacion == 0) {
                System.out.println("tipo antecedente que entra al get lov antecedentes" + " " + antecedentemSeleccionado.getTipoantecedente().getDescripcion());
                lovAntecedentes = administrarVigDomiciliarias.lovAntecedentes(antecedentemSeleccionado.getTipoantecedente().getSecuencia());
                System.out.println("lov antecedentes en el get" + " " + lovAntecedentes);
            } else if (tipoActualizacion == 1) {
                System.out.println("tipo antecedente que entra al get lov antecedentes" + " " + nuevoAntecedentem.getTipoantecedente().getDescripcion());
                lovAntecedentes = administrarVigDomiciliarias.lovAntecedentes(nuevoAntecedentem.getTipoantecedente().getSecuencia());
                System.out.println("lov antecedentes en el get" + " " + lovAntecedentes);
            } else if (tipoActualizacion == 2) {
                System.out.println("tipo antecedente que entra al get lov antecedentes" + " " + duplicarAntecedenteM.getTipoantecedente().getDescripcion());
                lovAntecedentes = administrarVigDomiciliarias.lovAntecedentes(duplicarAntecedenteM.getTipoantecedente().getSecuencia());
                System.out.println("lov antecedentes en el get" + " " + lovAntecedentes);
            }
        }
        return lovAntecedentes;
    }

    public void setLovAntecedentes(List<SoAntecedentes> lovAntecedentes) {
        this.lovAntecedentes = lovAntecedentes;
    }

    public SoAntecedentes getAntecedenteSeleccionado() {
        return antecedenteSeleccionado;
    }

    public void setAntecedenteSeleccionado(SoAntecedentes antecedenteSeleccionado) {
        this.antecedenteSeleccionado = antecedenteSeleccionado;
    }

    public SoTiposAntecedentes getTipoAntecedenteSeleccionado() {
        return tipoAntecedenteSeleccionado;
    }

    public void setTipoAntecedenteSeleccionado(SoTiposAntecedentes tipoAntecedenteSeleccionado) {
        this.tipoAntecedenteSeleccionado = tipoAntecedenteSeleccionado;
    }

    public String getInfoRegistroAntecedentes() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovAntecedentes");
        infoRegistroAntecedentes = String.valueOf(tabla.getRowCount());
        return infoRegistroAntecedentes;
    }

    public void setInfoRegistroAntecedentes(String infoRegistroAntecedentes) {
        this.infoRegistroAntecedentes = infoRegistroAntecedentes;
    }

    public String getInfoRegistroTipoAntecedente() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTiposAntecedentes");
        infoRegistroTipoAntecedente = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoAntecedente;
    }

    public void setInfoRegistroTipoAntecedente(String infoRegistroTipoAntecedente) {
        this.infoRegistroTipoAntecedente = infoRegistroTipoAntecedente;
    }

    public SoAntecedentesMedicos getDuplicarAntecedenteM() {
        return duplicarAntecedenteM;
    }

    public void setDuplicarAntecedenteM(SoAntecedentesMedicos duplicarAntecedenteM) {
        this.duplicarAntecedenteM = duplicarAntecedenteM;
    }

    public String getInfoRegistroAdiestramiento() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVAdiestramientosF");
        infoRegistroAdiestramiento = String.valueOf(tabla.getRowCount());
        return infoRegistroAdiestramiento;
    }

    public void setInfoRegistroAdiestramiento(String infoRegistroAdiestramiento) {
        this.infoRegistroAdiestramiento = infoRegistroAdiestramiento;
    }

    public String getInfoRegistroInstitucion() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVInstituciones");
        infoRegistroAdiestramiento = String.valueOf(tabla.getRowCount());
        return infoRegistroInstitucion;
    }

    public void setInfoRegistroInstitucion(String infoRegistroInstitucion) {
        this.infoRegistroInstitucion = infoRegistroInstitucion;
    }

    public String getInfoRegistroProfesion() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVProfesiones");
        infoRegistroProfesion = String.valueOf(tabla.getRowCount());
        return infoRegistroProfesion;
    }

    public void setInfoRegistroProfesion(String infoRegistroProfesion) {
        this.infoRegistroProfesion = infoRegistroProfesion;
    }

    public String getInfoRegistroTipoEducacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVTiposEducaciones");
        infoRegistroProfesion = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoEducacion;
    }

    public void setInfoRegistroTipoEducacion(String infoRegistroTipoEducacion) {
        this.infoRegistroTipoEducacion = infoRegistroTipoEducacion;
    }

    public VigenciasFormales getDuplicarVigenciaFormal() {
        return duplicarVigenciaFormal;
    }

    public void setDuplicarVigenciaFormal(VigenciasFormales duplicarVigenciaFormal) {
        this.duplicarVigenciaFormal = duplicarVigenciaFormal;
    }

    public HVHojasDeVida getDuplicarHV() {
        return duplicarHV;
    }

    public void setDuplicarHV(HVHojasDeVida duplicarHV) {
        this.duplicarHV = duplicarHV;
    }

    public HVHojasDeVida getHvSeleccionada() {
        return hvSeleccionada;
    }

    public void setHvSeleccionada(HVHojasDeVida hvSeleccionada) {
        this.hvSeleccionada = hvSeleccionada;
    }

    public Personas getDuplicarPersona() {
        return duplicarPersona;
    }

    public void setDuplicarPersona(Personas duplicarPersona) {
        this.duplicarPersona = duplicarPersona;
    }

    public HvExperienciasLaborales getDuplicarhvexp() {
        return duplicarhvexp;
    }

    public void setDuplicarhvexp(HvExperienciasLaborales duplicarhvexp) {
        this.duplicarhvexp = duplicarhvexp;
    }

    public Familiares getDuplicarFamiliar() {
        return duplicarFamiliar;
    }

    public void setDuplicarFamiliar(Familiares duplicarFamiliar) {
        this.duplicarFamiliar = duplicarFamiliar;
    }

    public TiposEducaciones getTipoEducacionSeleccionado() {
        return tipoEducacionSeleccionado;
    }

    public void setTipoEducacionSeleccionado(TiposEducaciones tipoEducacionSeleccionado) {
        this.tipoEducacionSeleccionado = tipoEducacionSeleccionado;
    }

    public AdiestramientosF getAdiestramientoSeleccionado() {
        return adiestramientoSeleccionado;
    }

    public void setAdiestramientoSeleccionado(AdiestramientosF adiestramientoSeleccionado) {
        this.adiestramientoSeleccionado = adiestramientoSeleccionado;
    }

    public Instituciones getInstitucionSeleccionada() {
        return institucionSeleccionada;
    }

    public void setInstitucionSeleccionada(Instituciones institucionSeleccionada) {
        this.institucionSeleccionada = institucionSeleccionada;
    }

    public TiposFamiliares getTipoFamiliarSeleccionado() {
        return tipoFamiliarSeleccionado;
    }

    public void setTipoFamiliarSeleccionado(TiposFamiliares tipoFamiliarSeleccionado) {
        this.tipoFamiliarSeleccionado = tipoFamiliarSeleccionado;
    }

    public HVHojasDeVida getHojaVida() {
        return hojaVida;
    }

    public void setHojaVida(HVHojasDeVida hojaVida) {
        this.hojaVida = hojaVida;
    }

    public List<AdiestramientosF> getLovAdiestramientosFiltrar() {
        return lovAdiestramientosFiltrar;
    }

    public void setLovAdiestramientosFiltrar(List<AdiestramientosF> lovAdiestramientosFiltrar) {
        this.lovAdiestramientosFiltrar = lovAdiestramientosFiltrar;
    }

    public String getInfoRegistroMotivo() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovMotivos");
        infoRegistroMotivo = String.valueOf(tabla.getRowCount());
        return infoRegistroMotivo;
    }

    public void setInfoRegistroMotivo(String infoRegistroMotivo) {
        this.infoRegistroMotivo = infoRegistroMotivo;
    }

    public String getInfoRegistroSector() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovSector");
        infoRegistroSector = String.valueOf(tabla.getRowCount());
        return infoRegistroSector;
    }

    public void setInfoRegistroSector(String infoRegistroSector) {
        this.infoRegistroSector = infoRegistroSector;
    }

    public Cargos getCargoSeleccionada() {
        return cargoSeleccionada;
    }

    public void setCargoSeleccionada(Cargos cargoSeleccionada) {
        this.cargoSeleccionada = cargoSeleccionada;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public List<TiposDocumentos> getLovTiposDocumentos() {
        if (lovTiposDocumentos == null) {
            lovTiposDocumentos = administrarVigDomiciliarias.consultarTiposDocumentos();
        }
        return lovTiposDocumentos;
    }

    public void setLovTiposDocumentos(List<TiposDocumentos> lovTiposDocumentos) {
        this.lovTiposDocumentos = lovTiposDocumentos;
    }

    public List<TiposDocumentos> getLovTiposDocumentosFiltrar() {
        return lovTiposDocumentosFiltrar;
    }

    public void setLovTiposDocumentosFiltrar(List<TiposDocumentos> lovTiposDocumentosFiltrar) {
        this.lovTiposDocumentosFiltrar = lovTiposDocumentosFiltrar;
    }

    public TiposDocumentos getTipoDocumentoSeleccionado() {
        return tipoDocumentoSeleccionado;
    }

    public void setTipoDocumentoSeleccionado(TiposDocumentos tipoDocumentoSeleccionado) {
        this.tipoDocumentoSeleccionado = tipoDocumentoSeleccionado;
    }

    public String getInfoRegistroCargo() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCargo");
        infoRegistroCargo = String.valueOf(tabla.getRowCount());
        return infoRegistroCargo;
    }

    public void setInfoRegistroCargo(String infoRegistroCargo) {
        this.infoRegistroCargo = infoRegistroCargo;
    }

    public String getInfoRegistroRelaciones() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosRelaciones");
        infoRegistroRelaciones = String.valueOf(tabla.getRowCount());
        return infoRegistroRelaciones;
    }

    public void setInfoRegistroRelaciones(String infoRegistroRelaciones) {
        this.infoRegistroRelaciones = infoRegistroRelaciones;
    }

    public String getInfoRegistroServicios() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosServicios");
        infoRegistroServicios = String.valueOf(tabla.getRowCount());
        return infoRegistroServicios;
    }

    public void setInfoRegistroServicios(String infoRegistroServicios) {
        this.infoRegistroServicios = infoRegistroServicios;
    }

    public String getInfoRegistroIngresos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosIngresos");
        infoRegistroIngresos = String.valueOf(tabla.getRowCount());
        return infoRegistroIngresos;
    }

    public void setInfoRegistroIngresos(String infoRegistroIngresos) {
        this.infoRegistroIngresos = infoRegistroIngresos;
    }

    public String getInfoRegistroAportesH() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosAportesHogar");
        infoRegistroAportesH = String.valueOf(tabla.getRowCount());
        return infoRegistroAportesH;
    }

    public void setInfoRegistroAportesH(String infoRegistroAportesH) {
        this.infoRegistroAportesH = infoRegistroAportesH;
    }

    public String getInfoRegistrosEgresos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEgresos");
        infoRegistrosEgresos = String.valueOf(tabla.getRowCount());
        return infoRegistrosEgresos;
    }

    public void setInfoRegistrosEgresos(String infoRegistrosEgresos) {
        this.infoRegistrosEgresos = infoRegistrosEgresos;
    }

    public String getInfoRegistrosIndicadores() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosIndicadores");
        infoRegistrosIndicadores = String.valueOf(tabla.getRowCount());
        return infoRegistrosIndicadores;
    }

    public void setInfoRegistrosIndicadores(String infoRegistrosIndicadores) {
        this.infoRegistrosIndicadores = infoRegistrosIndicadores;
    }

    public boolean isActivarotroservicio() {
        return activarotroservicio;
    }

    public void setActivarotroservicio(boolean activarotroservicio) {
        this.activarotroservicio = activarotroservicio;
    }

    public boolean isActivarotroegreso() {
        return activarotroegreso;
    }

    public void setActivarotroegreso(boolean activarotroegreso) {
        this.activarotroegreso = activarotroegreso;
    }

    public boolean isActivarotroaporte() {
        return activarotroaporte;
    }

    public void setActivarotroaporte(boolean activarotroaporte) {
        this.activarotroaporte = activarotroaporte;
    }

    public VigenciasFormales getEditarVigenciaFormal() {
        return editarVigenciaFormal;
    }

    public void setEditarVigenciaFormal(VigenciasFormales editarVigenciaFormal) {
        this.editarVigenciaFormal = editarVigenciaFormal;
    }

    public HVHojasDeVida getEditarHV() {
        return editarHV;
    }

    public void setEditarHV(HVHojasDeVida editarHV) {
        this.editarHV = editarHV;
    }

    public Personas getEditarPersona() {
        return editarPersona;
    }

    public void setEditarPersona(Personas editarPersona) {
        this.editarPersona = editarPersona;
    }

    public HvExperienciasLaborales getEditarhvexp() {
        return editarhvexp;
    }

    public void setEditarhvexp(HvExperienciasLaborales editarhvexp) {
        this.editarhvexp = editarhvexp;
    }

    public Direcciones getEditarDireccion() {
        return editarDireccion;
    }

    public void setEditarDireccion(Direcciones editarDireccion) {
        this.editarDireccion = editarDireccion;
    }

    public Telefonos getEditarTelefono() {
        return editarTelefono;
    }

    public void setEditarTelefono(Telefonos editarTelefono) {
        this.editarTelefono = editarTelefono;
    }

    public VigenciasEstadosCiviles getEditarVigenciaEstadoCivil() {
        return editarVigenciaEstadoCivil;
    }

    public void setEditarVigenciaEstadoCivil(VigenciasEstadosCiviles editarVigenciaEstadoCivil) {
        this.editarVigenciaEstadoCivil = editarVigenciaEstadoCivil;
    }

    public Familiares getEditarFamiliar() {
        return editarFamiliar;
    }

    public void setEditarFamiliar(Familiares editarFamiliar) {
        this.editarFamiliar = editarFamiliar;
    }

    public SoAntecedentesMedicos getEditarAntecedenteM() {
        return editarAntecedenteM;
    }

    public void setEditarAntecedenteM(SoAntecedentesMedicos editarAntecedenteM) {
        this.editarAntecedenteM = editarAntecedenteM;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getInfoRegistrolovvisitas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovVisitas");
        infoRegistrolovvisitas = String.valueOf(tabla.getRowCount());
        return infoRegistrolovvisitas;
    }

    public void setInfoRegistrolovvisitas(String infoRegistrolovvisitas) {
        this.infoRegistrolovvisitas = infoRegistrolovvisitas;
    }

    public String getInfoRegistroVigenciaD() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovBuscarVisita");
        infoRegistroVigenciaD = String.valueOf(tabla.getRowCount());
        return infoRegistroVigenciaD;
    }

    public void setInfoRegistroVigenciaD(String infoRegistroVigenciaD) {
        this.infoRegistroVigenciaD = infoRegistroVigenciaD;
    }

    public List<VigenciasDomiciliarias> getLovVisitas() {
        if (lovVisitas == null) {
            lovVisitas = administrarVigDomiciliarias.vigenciasDomiciliariasporPersona(persona.getSecuencia());
        }
        return lovVisitas;
    }

    public void setLovVisitas(List<VigenciasDomiciliarias> lovVisitas) {
        this.lovVisitas = lovVisitas;
    }

    public List<VigenciasDomiciliarias> getListVigenciasDomiciliariasFiltrar() {
        return listVigenciasDomiciliariasFiltrar;
    }

    public void setListVigenciasDomiciliariasFiltrar(List<VigenciasDomiciliarias> listVigenciasDomiciliariasFiltrar) {
        this.listVigenciasDomiciliariasFiltrar = listVigenciasDomiciliariasFiltrar;
    }

    public VigenciasDomiciliarias getVisitaSeleccionada() {
        return visitaSeleccionada;
    }

    public void setVisitaSeleccionada(VigenciasDomiciliarias visitaSeleccionada) {
        this.visitaSeleccionada = visitaSeleccionada;
    }

    public VigenciasDomiciliarias getVisitaLovSeleccionada() {
        return visitaLovSeleccionada;
    }

    public void setVisitaLovSeleccionada(VigenciasDomiciliarias visitaLovSeleccionada) {
        this.visitaLovSeleccionada = visitaLovSeleccionada;
    }

    public String getTablaImprimir() {
        return tablaImprimir;
    }

    public void setTablaImprimir(String tablaImprimir) {
        this.tablaImprimir = tablaImprimir;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public List<String> getLovAntecedentesDescripcion() {

        return lovAntecedentesDescripcion;
    }

    public void setLovAntecedentesDescripcion(List<String> lovAntecedentesDescripcion) {
        this.lovAntecedentesDescripcion = lovAntecedentesDescripcion;
    }

}
