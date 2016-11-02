package Controlador;

import ClasesAyuda.EmpleadoIndividualExportar;
import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Demandas;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.Encargaturas;
import Entidades.EvalResultadosConv;
//import Entidades.Familiares;
import Entidades.Generales;
import Entidades.HVHojasDeVida;
import Entidades.HvEntrevistas;
import Entidades.HvExperienciasLaborales;
import Entidades.HvReferencias;
import Entidades.IdiomasPersonas;
import Entidades.InformacionesAdicionales;
import Entidades.Personas;
import Entidades.Telefonos;
import Entidades.TiposDocumentos;
import Entidades.VigenciasAficiones;
import Entidades.VigenciasDeportes;
import Entidades.VigenciasDomiciliarias;
import Entidades.VigenciasEstadosCiviles;
import Entidades.VigenciasEventos;
import Entidades.VigenciasFormales;
import Entidades.VigenciasIndicadores;
import Entidades.VigenciasProyectos;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmpleadoIndividualInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import com.sun.xml.ws.security.opt.impl.util.SOAPUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class ControlEmpleadoIndividual implements Serializable {

    @EJB
    AdministrarEmpleadoIndividualInterface administrarEmpleadoIndividual;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private Empleados empleado;
    private Personas persona;
    private BigInteger secuencia;
    private HVHojasDeVida hojaDeVidaPersona;
    private Telefonos telefono;
    private Direcciones direccion;
    private VigenciasEstadosCiviles estadoCivil;
    private InformacionesAdicionales informacionAdicional;
    private Encargaturas encargatura;
    private VigenciasFormales vigenciaFormal;
    private IdiomasPersonas idiomasPersona;
    private VigenciasProyectos vigenciaProyecto;
    private HvReferencias hvReferenciasPersonales;
    private HvReferencias hvReferenciasFamiliares;
    private HvExperienciasLaborales experienciaLaboral;
    private VigenciasEventos vigenciaEvento;
    private VigenciasDeportes vigenciaDeporte;
    private VigenciasAficiones vigenciaAficion;
//    private Familiares familiares;
    private HvEntrevistas entrevistas;
    private VigenciasIndicadores vigenciaIndicador;
    private Demandas demandas;
    private VigenciasDomiciliarias vigenciaDomiciliaria;
    private EvalResultadosConv pruebasAplicadas;
    //CAMPOS A MOSTRAR EN LA PAGINA
    private String telefonoP, direccionP, estadoCivilP,
            informacionAdicionalP, reemplazoP, educacionP,
            idiomasP, proyectosP, referenciasPersonalesP,
            referenciasFamiliaresP, experienciaLaboralP,
            eventosP, deportesP, aficionesP, familiaresP,
            entrevistasP, indicadoresP, demandasP,
            visitasDomiciliariasP, pruebasAplicadasP;
    //CONVERTIR FECHA
    private SimpleDateFormat formatoFecha;
    //LISTAS DE VALORES
    private List<TiposDocumentos> listaTiposDocumentos;
    private List<TiposDocumentos> filtradoListaTiposDocumentos;
    private TiposDocumentos seleccionTipoDocumento;
    private List<Ciudades> listaCiudades, listaCiudadesDocumento;
    private List<Ciudades> filtradoListaCiudades, filtradoCiudadesDocumento;
    private Ciudades seleccionCiudad, seleccionCiudadDocumento;
    private List<Cargos> listaCargos;
    private List<Cargos> filtradoListaCargos;
    private Cargos seleccionCargo;
    private boolean aceptar;
    private int modificacionCiudad;
    private String cabezeraDialogoCiudad;
    private int dialogo;
    //AUTOCOMPLETAR
    private String tipoDocumento, ciudad, ciudaddocumento, cargoPostulado;
    //EXPORTAR DATOS 
    private List<EmpleadoIndividualExportar> empleadoIndividualExportar;
    //RASTRO
    private String nombreTabla;
    private BigInteger secRastro;
    //MODIFICACIÓN
    private boolean modificacionPersona, modificacionEmpleado, modificacionHV;
    //GUARDAR
    private boolean guardado;
    //FOTO EMPLEADO
    //private String fotoEmpleado;
    //private String destino = "C:\\glassfish3\\glassfish\\domains\\domain1\\applications\\DesignerRHN\\DesignerRHN-war_war\\resources\\ArchivosCargados\\";
    private String destino;
    //private String directorioDespliegue = "C:\\\\glassfish3\\\\glassfish\\\\domains\\\\domain1\\\\applications\\\\DesignerRHN\\\\DesignerRHN-war_war";
    //private String destino = directorioDespliegue + "\\resources\\ArchivosCargados\\";
    private BigInteger identificacionEmpleado;
//    private String nombreArchivoFoto;
    //VEHICULO PROPIO
    private boolean estadoVP;
    private String vehiculoPropio;
    private StreamedContent fotoEmpleado;
    private FileInputStream fis;
    private boolean existenHV;
    private boolean paraRecargar;
    //
    private String infoRegistroTipoDocumento, infoRegistroCiudad, infoRegistroCargo, infoRegistroCiudadDocumento;

    public ControlEmpleadoIndividual() {
        imprimir("0: constructor.", null);
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        aceptar = true;
        empleado = null;
        modificacionPersona = false;
        modificacionEmpleado = false;
        modificacionHV = false;
        guardado = true;
        existenHV = true;
        persona = new Personas();
    }

    @PostConstruct
    public void inicializarAdministrador() {
        imprimir("1: inicializarAdministrador.", null);
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEmpleadoIndividual.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger sec) {
        imprimir("2: recibirEmpleado.", "recibido secuencia: " + sec.toString());
        secuencia = sec;
        imprimir("2: recibirEmpleado.", "secuencia: " + secuencia);
        empleado = null;
//        persona = administrarEmpleadoIndividual.encontrarPersona(sec);
        persona = administrarEmpleadoIndividual.obtenerPersonaPorEmpleado(sec);
        getEmpleado();
        datosEmpleado();
//        getFotoEmpleado();
        listaTiposDocumentos = null;
        listaCiudades = null;
        listaCiudadesDocumento = null;
        listaCargos = null;
        guardado = true;
    }

    public void datosEmpleado() {
        imprimir("3: datosEmpleado.", "secuencia: " + secuencia);
        BigInteger secPersona = null;
        if (persona != null) {
            secPersona = persona.getSecuencia();
        }
        BigInteger secEmpleado = empleado.getSecuencia();
        hojaDeVidaPersona = administrarEmpleadoIndividual.hvHojaDeVidaPersona(secPersona);
        if(hojaDeVidaPersona == null){
            hojaDeVidaPersona = new HVHojasDeVida();
            hojaDeVidaPersona.setCargo(new Cargos());
        }
       
        if (hojaDeVidaPersona != null && hojaDeVidaPersona.getSecuencia() != null) {
            BigInteger secHv = hojaDeVidaPersona.getSecuencia();

            referenciasPersonalesP = administrarEmpleadoIndividual.consultarPrimeraReferenciaP(secHv);
            referenciasFamiliaresP = administrarEmpleadoIndividual.consultarPrimeraReferenciaF(secHv);

            experienciaLaboralP = administrarEmpleadoIndividual.consultarPrimeraExpLaboral(secHv);
            entrevistasP = administrarEmpleadoIndividual.consultarPrimeraEntrevista(secHv);
            existenHV = false;
        } else{
        referenciasPersonalesP = "";
        referenciasFamiliaresP = "";
        experienciaLaboralP = "";
        entrevistasP = "";
        }

        telefonoP = administrarEmpleadoIndividual.consultarPrimerTelefonoPersona(secPersona);
        direccionP = administrarEmpleadoIndividual.consultarPrimeraDireccionPersona(secPersona);
        estadoCivilP = administrarEmpleadoIndividual.consultarPrimerEstadoCivilPersona(secPersona);
        informacionAdicionalP = administrarEmpleadoIndividual.consultarPrimeraInformacionAd(secPersona);
        reemplazoP = administrarEmpleadoIndividual.consultarPrimerReemplazo(secPersona);
        educacionP = administrarEmpleadoIndividual.consultarPrimeraVigenciaFormal(secPersona);
        idiomasP = administrarEmpleadoIndividual.consultarPimerIdioma(secPersona);
        proyectosP = administrarEmpleadoIndividual.consultarPrimerProyecto(secPersona);
        deportesP = administrarEmpleadoIndividual.consultarPrimerDeporte(secPersona);
        eventosP = administrarEmpleadoIndividual.consultarPrimerEvento(secPersona);
        aficionesP = administrarEmpleadoIndividual.consultarPrimeraAficion(secPersona);
        familiaresP = administrarEmpleadoIndividual.consultarPrimerFamiliar(secPersona);
        indicadoresP = administrarEmpleadoIndividual.consultarPrimerIndicador(secPersona);
        demandasP = administrarEmpleadoIndividual.consultarPrimeraDemanda(secPersona);
        visitasDomiciliariasP = administrarEmpleadoIndividual.consultarPrimeraVisita(secPersona);
        pruebasAplicadasP = administrarEmpleadoIndividual.consultarPrimeraPrueba(secPersona);

//        pruebasAplicadas = administrarEmpleadoIndividual.pruebasAplicadasPersona(secEmpleado);
        //VEHICULO PROPIO
        if (persona.getPlacavehiculo() != null) {
            estadoVP = false;
            vehiculoPropio = "S";
        } else {
            estadoVP = true;
            vehiculoPropio = "N";
        }
    }

    public void activarAceptar() {
        imprimir("4: activarAceptar.", "secuencia: " + secuencia);
        aceptar = false;
    }

    //METODOS LOVS
    public void seleccionarTipoDocumento() {
        imprimir("5: seleccionarTipoDocumento.", "secuencia: " + secuencia);
        if (seleccionTipoDocumento != null && persona != null) {
            persona.setTipodocumento(seleccionTipoDocumento);
            seleccionTipoDocumento = null;
            filtradoListaTiposDocumentos = null;
            aceptar = true;
            dialogo = -1;
            RequestContext context = RequestContext.getCurrentInstance();
            if (!modificacionPersona) {
                modificacionPersona = true;
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:tipo");
            context.reset("formDialogos:lovTiposDocumentos:globalFilter");
            RequestContext.getCurrentInstance().execute("PF('lovTiposDocumentos').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('TiposDocumentosDialogo').hide()");
            //RequestContext.getCurrentInstance().update("formDialogos:lovTiposDocumentos");
        }
    }

    public void cancelarSeleccionTipoDocumento() {
        imprimir("6: cancelarSeleccionTipoDocumento.", "secuencia: " + secuencia);
        filtradoListaTiposDocumentos = null;
        seleccionTipoDocumento = null;
        aceptar = true;
        dialogo = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovTiposDocumentos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposDocumentos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TiposDocumentosDialogo').hide()");
    }

    public void dialogoCiudad(int modificacion) {
        imprimir("7: dialogoCiudad.", "secuencia: " + secuencia);
        RequestContext context = RequestContext.getCurrentInstance();
        modificacionCiudad = modificacion;
        if (modificacionCiudad == 0) {
            cabezeraDialogoCiudad = "Ciudad documento";
            RequestContext.getCurrentInstance().update("formDialogos:CiudadesDocumentoDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadesDocumentoDialogo').show()");
        } else if (modificacionCiudad == 1) {
            cabezeraDialogoCiudad = "Ciudad nacimiento";
            RequestContext.getCurrentInstance().update("formDialogos:CiudadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadesDialogo').show()");
        }
    }

    public void seleccionarCiudad() {
        imprimir("8: seleccionarCiudad.", "secuencia: " + secuencia);
        if (seleccionCiudad != null && persona != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionCiudad == 1) {
                persona.setCiudadnacimiento(seleccionCiudad);
                RequestContext.getCurrentInstance().update("form:lugarNacimiento");
            }
            if (!modificacionPersona) {
                modificacionPersona = true;
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            filtradoListaCiudades = null;
            aceptar = true;
            context.reset("formDialogos:lovCiudades:globalFilter");
            RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('CiudadesDialogo').hide()");
            //RequestContext.getCurrentInstance().update("formDialogos:lovCiudades");
            modificacionCiudad = -1;
            dialogo = -1;
        }
    }

    public void cancelarSeleccionCiudad() {
        imprimir("9: cancelarSeleccionCiudad.", "secuencia: " + secuencia);
        filtradoListaCiudades = null;
        aceptar = true;
        modificacionCiudad = -1;
        dialogo = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CiudadesDialogo').hide()");
    }

    public void seleccionarCiudadDocumento() {
        imprimir("10: seleccionarCiudadDocumento.", "secuencia: " + secuencia);
        if (persona != null) {
            if (persona.getCiudaddocumento() != null && seleccionCiudadDocumento != null) {
//                System.out.println("entra al if 2");
                RequestContext context = RequestContext.getCurrentInstance();
                if (modificacionCiudad == 0) {
                    persona.setCiudaddocumento(seleccionCiudadDocumento);
//                    System.out.println("nueva ciudad : " + seleccionCiudadDocumento);
//                    System.out.println("nueva ciudad : " + persona.getCiudaddocumento());
                    RequestContext.getCurrentInstance().update("form:lugarExpedicion");
                }
                if (!modificacionPersona) {
                    modificacionPersona = true;
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                filtradoCiudadesDocumento = null;
                aceptar = true;
                context.reset("formDialogos:lovCiudades:globalFilter");
                RequestContext.getCurrentInstance().execute("PF('lovCiudadesDocumento').clearFilters()");
                RequestContext.getCurrentInstance().execute("PF('CiudadesDocumentoDialogo').hide()");
                //RequestContext.getCurrentInstance().update("formDialogos:lovCiudades");
                modificacionCiudad = -1;
                dialogo = -1;
            } else {
//                System.out.println("entra al else");
//                System.out.println("ciudadnacimiento" + persona.getCiudadnacimiento().getNombre());
                persona.setCiudaddocumento(persona.getCiudadnacimiento());
//                System.out.println("ciudadnacimiento" + persona.getCiudaddocumento().getNombre());
            }
        }
    }

    public void cancelarSeleccionCiudadDocumento() {
        imprimir("11: cancelarSeleccionCiudadDocumento.", "secuencia: " + secuencia);
        filtradoCiudadesDocumento = null;
        aceptar = true;
        modificacionCiudad = -1;
        dialogo = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadesDocumento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CiudadesDocumentoDialogo').hide()");
    }

    public void seleccionarCargo() {
        imprimir("12: seleccionarCargo.", "secuencia: " + secuencia);
        if (seleccionCargo != null && hojaDeVidaPersona != null && hojaDeVidaPersona.getSecuencia() != null) {
            hojaDeVidaPersona.setCargo(seleccionCargo);
            seleccionCargo = null;
            filtradoListaCargos = null;
            aceptar = true;
            dialogo = -1;
            RequestContext context = RequestContext.getCurrentInstance();
            if (!modificacionHV) {
                modificacionHV = true;
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:cargoPostulado");
            context.reset("formDialogos:lovCargos:globalFilter");
            RequestContext.getCurrentInstance().execute("PF('lovCargos').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('CargosDialogo').hide()");
            //RequestContext.getCurrentInstance().update("formDialogos:lovCargos");
        }
    }

    public void cancelarSeleccionCargo() {
        imprimir("13: cancelarSeleccionCargo.", "secuencia: " + secuencia);
        filtradoListaCargos = null;
        seleccionCargo = null;
        aceptar = true;
        dialogo = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovCargos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCargos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CargosDialogo').hide()");
    }

    //AUTOCOMPLETAR
    public void valoresBackupAutocompletar(String Campo) {
        imprimir("14: valoresBackupAutocompletar.", "secuencia: " + secuencia);
        if (Campo.equals("TIPODOCUMENTO")) {
            tipoDocumento = persona.getTipodocumento().getNombrecorto();
            dialogo = 0;
            nombreTabla = "Personas";
        } else if (Campo.equals("CIUDADDOCUMENTO")) {
            modificacionCiudad = 0;
            ciudaddocumento = persona.getCiudaddocumento().getNombre();
            dialogo = 1;
            nombreTabla = "Personas";
        } else if (Campo.equals("CIUDADNACIMIENTO")) {
            modificacionCiudad = 1;
            ciudad = persona.getCiudadnacimiento().getNombre();
            dialogo = 1;
            nombreTabla = "Personas";
        } else if (Campo.equals("CARGOPOSTULADO")) {
            cargoPostulado = hojaDeVidaPersona.getCargo().getNombre();
            dialogo = 2;
            nombreTabla = "HVHojasDeVida";
        }
    }

    public void autocompletar(String confirmarCambio, String valorConfirmar) {
        imprimir("15: autocompletar.", "secuencia: " + secuencia);
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPODOCUMENTO")) {
            persona.getTipodocumento().setNombrecorto(tipoDocumento);
            for (int i = 0; i < listaTiposDocumentos.size(); i++) {
                if (listaTiposDocumentos.get(i).getNombrecorto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                persona.setTipodocumento(listaTiposDocumentos.get(indiceUnicoElemento));
                RequestContext.getCurrentInstance().update("form:tipo");
                listaTiposDocumentos = null;
                getListaTiposDocumentos();
                if (!modificacionPersona) {
                    modificacionPersona = true;
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                RequestContext.getCurrentInstance().update("formDialogos:TiposDocumentosDialogo");
                RequestContext.getCurrentInstance().execute("PF('TiposDocumentosDialogo').show()");
                RequestContext.getCurrentInstance().update("form:tipo");
            }
        } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            if (modificacionCiudad == 0) {
                persona.getCiudaddocumento().setNombre(ciudaddocumento);
            } else if (modificacionCiudad == 1) {
                persona.getCiudadnacimiento().setNombre(ciudad);
            }
            for (int i = 0; i < listaCiudades.size(); i++) {
                if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (modificacionCiudad == 1) {
                    persona.setCiudadnacimiento(listaCiudades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("form:lugarNacimiento");
                }
                listaCiudades = null;
                getListaCiudades();

                if (!modificacionPersona) {
                    modificacionPersona = true;
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                if (modificacionCiudad == 1) {
                    cabezeraDialogoCiudad = "Ciudad nacimiento";
                    RequestContext.getCurrentInstance().update("formularioDialogos:lugarNacimiento");
                }
                RequestContext.getCurrentInstance().update("formDialogos:CiudadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('CiudadesDialogo').show()");
            }
        } else if (confirmarCambio.equalsIgnoreCase("CIUDADDOCUMENTO")) {
            if (modificacionCiudad == 0) {
                persona.getCiudaddocumento().setNombre(ciudaddocumento);
            }
            for (int i = 0; i < listaCiudadesDocumento.size(); i++) {
                if (listaCiudadesDocumento.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (modificacionCiudad == 0) {
                    persona.setCiudaddocumento(listaCiudadesDocumento.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("form:lugarExpedicion");
                }
                listaCiudadesDocumento = null;
                getListaCiudadesDocumento();

                if (!modificacionPersona) {
                    modificacionPersona = true;
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                if (modificacionCiudad == 0) {
                    cabezeraDialogoCiudad = "Ciudad documento";
                    RequestContext.getCurrentInstance().update("form:lugarExpedicion");
                }
                RequestContext.getCurrentInstance().update("formDialogos:CiudadesDocumentoDialogo");
                RequestContext.getCurrentInstance().execute("PF('CiudadesDocumentoDialogo').show()");
            }
        } else if (confirmarCambio.equalsIgnoreCase("CARGOPOSTULADO")) {
            hojaDeVidaPersona.getCargo().setNombre(cargoPostulado);
            for (int i = 0; i < listaCargos.size(); i++) {
                if (listaCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                hojaDeVidaPersona.setCargo(listaCargos.get(indiceUnicoElemento));
                RequestContext.getCurrentInstance().update("form:cargoPostulado");
                listaCargos = null;
                getListaCargos();
                if (!modificacionHV) {
                    modificacionHV = true;
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                RequestContext.getCurrentInstance().update("formDialogos:CargosDialogo");
                RequestContext.getCurrentInstance().execute("PF('CargosDialogo').show()");
                RequestContext.getCurrentInstance().update("form:cargoPostulado");
            }
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        imprimir("16: listaValoresBoton.", "secuencia: " + secuencia);
//        RequestContext context = RequestContext.getCurrentInstance();
        if (dialogo == 0) {
            RequestContext.getCurrentInstance().execute("PF('TiposDocumentosDialogo').show()");
        } else if (dialogo == 1) {
            if (modificacionCiudad == 0) {
                cabezeraDialogoCiudad = "Ciudad documento";
            } else if (modificacionCiudad == 1) {
                cabezeraDialogoCiudad = "Ciudad nacimiento";
            }
            RequestContext.getCurrentInstance().update("formDialogos:CiudadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadesDialogo').show()");
        } else if (dialogo == 2) {
            RequestContext.getCurrentInstance().execute("PF('CargosDialogo').show()");
        }
    }

    public void refrescar() {
        imprimir("17: refrescar.", "secuencia: " + secuencia);
        getEmpleado();
        datosEmpleado();
        RequestContext context = RequestContext.getCurrentInstance();
        guardado = true;
//        persona = null;
        empleado = null;
        RequestContext.getCurrentInstance().update("form");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        imprimir("18: exportPDF.", "secuencia: " + secuencia);
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "HojaVidaEmpleadoPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        imprimir("19: exportXLS.", "secuencia: " + secuencia);
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "HojaVidaEmpleadoXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
    public void cambiarTablaRastro(String tabla) {
        imprimir("20: cambiarTablaRastro.", "secuencia: " + secuencia);
        nombreTabla = tabla;
    }

    public void verificarRastro() {
        imprimir("21: verificarRastro.", "secuencia: " + secuencia);
//        RequestContext context = RequestContext.getCurrentInstance();
        int resultado = -1;
        if (nombreTabla != null) {
            if (nombreTabla.equals("Personas")) {
                secRastro = persona.getSecuencia();
                resultado = administrarRastros.obtenerTabla(secRastro, nombreTabla.toUpperCase());
            } else if (nombreTabla.equals("Empleados")) {
                secRastro = empleado.getSecuencia();
                resultado = administrarRastros.obtenerTabla(secRastro, nombreTabla.toUpperCase());
            } else if (nombreTabla.equals("HVHojasDeVida")) {
                secRastro = hojaDeVidaPersona.getSecuencia();
                resultado = administrarRastros.obtenerTabla(secRastro, nombreTabla.toUpperCase());
            }
            if (resultado == 1) {
                RequestContext.getCurrentInstance().update("formDialogos:errorObjetosDB");
                RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
                RequestContext.getCurrentInstance().update("formDialogos:confirmarRastro");
                RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
            } else if (resultado == 3) {
                RequestContext.getCurrentInstance().update("formDialogos:errorRegistroRastro");
                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
            } else if (resultado == 4) {
                RequestContext.getCurrentInstance().update("formDialogos:errorTablaConRastro");
                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
            } else if (resultado == 5) {
                RequestContext.getCurrentInstance().update("formDialogos:errorTablaSinRastro");
                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    //MODIFICACION
    public void eventoDataSelectFechaNacimiento(String tipoCampo) {
        imprimir("22: eventoDataSelectFechaNacimiento.", "secuencia: " + secuencia);
//        System.out.println(this.getClass().getName() + ".eventoDataSelectFechaNacimiento");
        if (persona.getFechanacimiento() != null) {
            if (tipoCampo.equals("P")) {
                if (modificacionPersona == false) {
                    modificacionPersona = true;
                }
            } else if (tipoCampo.equals("E")) {
                if (modificacionEmpleado == false) {
                    modificacionEmpleado = true;
                }
            } else if (tipoCampo.equals("HV")) {
                if (!modificacionHV) {
                    modificacionHV = true;
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void eventoDataSelectFechaVencimiento(String tipoCampo) {
        imprimir("23: eventoDataSelectFechaVencimiento.", "secuencia: " + secuencia);
        if (persona.getFechavencimientocertificado() != null) {
            if (tipoCampo.equals("P")) {
                if (modificacionPersona == false) {
                    modificacionPersona = true;
                }
            } else if (tipoCampo.equals("E")) {
                if (modificacionEmpleado == false) {
                    modificacionEmpleado = true;
                }
            } else if (tipoCampo.equals("HV")) {
                if (!modificacionHV) {
                    modificacionHV = true;
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void eventoDataSelectFechaFallecimiento(String tipoCampo) {
        imprimir("24: eventoDataSelectFechaFallecimiento.", "secuencia: " + secuencia);
        if (persona.getFechafallecimiento() != null) {
            if (tipoCampo.equals("P")) {
                if (modificacionPersona == false) {
                    modificacionPersona = true;
                }
            } else if (tipoCampo.equals("E")) {
                if (modificacionEmpleado == false) {
                    modificacionEmpleado = true;
                }
            } else if (tipoCampo.equals("HV")) {
                if (!modificacionHV) {
                    modificacionHV = true;
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void modificarCampo(String tipoCampo) {
        imprimir("25: modificarCampo.", "secuencia: " + secuencia);
        if (tipoCampo.equals("P")) {
            if (!modificacionPersona) {
                modificacionPersona = true;
            }
        } else if (tipoCampo.equals("E")) {
            if (!modificacionEmpleado) {
                modificacionEmpleado = true;
            }
        } else if (tipoCampo.equals("HV")) {
            if (!modificacionHV) {
                modificacionHV = true;
            }
        }
        if (guardado) {
            guardado = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }
    //GUARDAR CAMBIOS

    public void guardarCambios() {
        imprimir("26: guardarCambios.", "secuencia: " + secuencia);
//        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (!guardado) {
                if (modificacionPersona) {
                    administrarEmpleadoIndividual.modificarPersona(persona);
                    modificacionPersona = false;
                }
                if (modificacionEmpleado) {
                    administrarEmpleadoIndividual.modificarEmpleado(empleado);
                    modificacionEmpleado = false;
                }
                if (modificacionHV) {
                    if (hojaDeVidaPersona.getCargo().getSecuencia() == null) {
                        hojaDeVidaPersona.setCargo(null);
                    }
                    if (hojaDeVidaPersona.getSecuencia() == null) {
                        hojaDeVidaPersona.setSecuencia(BigInteger.valueOf(0));
                        hojaDeVidaPersona.setPersona(persona);
                        administrarEmpleadoIndividual.modificarHojaDeVida(hojaDeVidaPersona);
                    } else {
                        administrarEmpleadoIndividual.modificarHojaDeVida(hojaDeVidaPersona);
                    }
                    modificacionHV = false;
                }
                empleado = null;
                persona = null;
                getEmpleado();
                datosEmpleado();
                guardado = true;
                RequestContext.getCurrentInstance().update("form");
                RequestContext.getCurrentInstance().update("form:aceptar");
                FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    //SUBIR FOTO EMPLEADO
    public void subirFotoEmpleado(FileUploadEvent event) throws IOException {
        RequestContext context = RequestContext.getCurrentInstance();
        String extencion = event.getFile().getFileName().split("[.]")[1];
        Long tamanho = event.getFile().getSize();
        if (extencion.equals("jpg") || extencion.equals("JPG")) {
            if (tamanho <= 307200) {
                Generales general = administrarEmpleadoIndividual.obtenerRutaFoto();
                if (general != null && persona != null) {
                    destino = general.getPathfoto();
                    identificacionEmpleado = persona.getNumerodocumento();
                    transformarArchivo(tamanho, event.getFile().getInputstream());
                    RequestContext.getCurrentInstance().execute("PF('subirFoto').hide()");
                    RequestContext.getCurrentInstance().update("form:btnFoto");
                    FacesMessage msg = new FacesMessage("Información", "Archivo cargado con éxito.");
                    FacesContext fc = FacesContext.getCurrentInstance();
                    fc.addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                } else {
                    FacesMessage msg = new FacesMessage("Información", "Ruta generales ó empleado, nulo.");
                    FacesContext fc = FacesContext.getCurrentInstance();
                    fc.addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                }
            } else {
                FacesMessage msg = new FacesMessage("Información", "El tamaño maximo permitido es de 300 KB.");
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
        } else {
            FacesMessage msg = new FacesMessage("Información", "Solo se admiten imagenes con formato (.JPG).");
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

        /*HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

         response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
         response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
         response.setDateHeader("Expires", 0); // Proxies. */
        //RequestContext.getCurrentInstance().execute("PF('espera').hide()");
    }

    public void transformarArchivo(long size, InputStream in) {
        imprimir("27: transformarArchivo.", "secuencia: " + secuencia);
        try {
            //extencion = fileName.split("[.]")[1];
            //System.out.println(extencion); 
            OutputStream out = new FileOutputStream(new File(destino + identificacionEmpleado + ".png"));
            int reader = 0;
            byte[] bytes = new byte[(int) size];
            while ((reader = in.read(bytes)) != -1) {
                out.write(bytes, 0, reader);
            }
            in.close();
            out.flush();
            out.close();
            administrarEmpleadoIndividual.actualizarFotoPersona(persona);
        } catch (IOException e) {
            System.out.println("Error: ControlEmpleadoIndividual.transformarArchivo: " + e);
        }
    }

    //VEHICULO PROPIO DINAMICO
    public void estadoVehiculoPropio() {
        imprimir("28: estadoVehiculoPropio.", "secuencia: " + secuencia);
//        RequestContext context = RequestContext.getCurrentInstance();
        if (vehiculoPropio.equals("S")) {
            estadoVP = false;
            RequestContext.getCurrentInstance().update("form:placa");
        } else {
            estadoVP = true;
            persona.setPlacavehiculo(null);
            modificarCampo("P");
            RequestContext.getCurrentInstance().update("form:placa");
        }
    }

    public void validarRedirigirReferencias(String pagina) {
        imprimir("29: validarRedirigirReferencias.", "secuencia: " + secuencia);
        imprimir("29: validarRedirigirReferencias.", "pagina: " + pagina);
        if (hojaDeVidaPersona != null) {
            if (hojaDeVidaPersona.getPerfilprofesional() == null || hojaDeVidaPersona.getPerfilprofesional().isEmpty()) {
                RequestContext.getCurrentInstance().execute("PF('validarPerfilProfesional').show()");
            } else {
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pagina);
            }
        }
    }
//GETTER AND SETTER

    public Empleados getEmpleado() {
        imprimir("30: getEmpleado.", "secuencia: " + secuencia);
        if (empleado == null) {
            empleado = administrarEmpleadoIndividual.buscarEmpleado(secuencia);
            if (empleado != null) {
                persona = empleado.getPersona();
            }
        }
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        imprimir("31: setEmpleado.", "secuencia: " + secuencia);
        this.empleado = empleado;
    }

    public HVHojasDeVida getHojaDeVidaPersona() {
        imprimir("32: getHojaDeVidaPersona.", "secuencia: " + secuencia);
        return hojaDeVidaPersona;
    }

    public void setHojaDeVidaPersona(HVHojasDeVida hojaDeVidaPersona) {
        imprimir("33: setHojaDeVidaPersona.", "secuencia: " + secuencia);
        this.hojaDeVidaPersona = hojaDeVidaPersona;
    }

    public void setTelefono(Telefonos telefono) {
        imprimir("34: setTelefono.", "secuencia: " + secuencia);
        this.telefono = telefono;
    }

    public void setDireccion(Direcciones direccion) {
        imprimir("35: setDireccion.", "secuencia: " + secuencia);
        this.direccion = direccion;
    }

    public void setEstadoCivil(VigenciasEstadosCiviles estadoCivil) {
        imprimir("36: setEstadoCivil.", "secuencia: " + secuencia);
        this.estadoCivil = estadoCivil;
    }

    public void setInformacionAdicional(InformacionesAdicionales informacionAdicional) {
        imprimir("37: setInformacionAdicional.", "secuencia: " + secuencia);
        this.informacionAdicional = informacionAdicional;
    }

    public void setEncargatura(Encargaturas encargatura) {
        imprimir("38: setEncargatura.", "secuencia: " + secuencia);
        this.encargatura = encargatura;
    }

    public void setVigenciaFormal(VigenciasFormales vigenciaFormal) {
        imprimir("39: setVigenciaFormal.", "secuencia: " + secuencia);
        this.vigenciaFormal = vigenciaFormal;
    }

    public void setIdiomasPersona(IdiomasPersonas idiomasPersona) {
        imprimir("40: setIdiomasPersona.", "secuencia: " + secuencia);
        this.idiomasPersona = idiomasPersona;
    }

    public void setVigenciaProyecto(VigenciasProyectos vigenciaProyecto) {
        imprimir("41: setVigenciaProyecto.", "secuencia: " + secuencia);
        this.vigenciaProyecto = vigenciaProyecto;
    }

    public HvReferencias getHvReferenciasPersonales() {
        imprimir("42: getHvReferenciasPersonales.", "secuencia: " + secuencia);
        return hvReferenciasPersonales;
    }

    public void setHvReferenciasPersonales(HvReferencias hvReferenciasPersonales) {
        imprimir("43: setHvReferenciasPersonales.", "secuencia: " + secuencia);
        this.hvReferenciasPersonales = hvReferenciasPersonales;
    }

    public void setHvReferenciasFamiliares(HvReferencias hvReferenciasFamiliares) {
        imprimir("44: setHvReferenciasFamiliares.", "secuencia: " + secuencia);
        this.hvReferenciasFamiliares = hvReferenciasFamiliares;
    }

    public void setExperienciaLaboral(HvExperienciasLaborales experienciaLaboral) {
        imprimir("45: setExperienciaLaboral.", "secuencia: " + secuencia);
        this.experienciaLaboral = experienciaLaboral;
    }

    public void setVigenciaEvento(VigenciasEventos vigenciaEvento) {
        imprimir("46: setVigenciaEvento.", "secuencia: " + secuencia);
        this.vigenciaEvento = vigenciaEvento;
    }

    public void setVigenciaDeporte(VigenciasDeportes vigenciaDeporte) {
        imprimir("47: setVigenciaDeporte.", "secuencia: " + secuencia);
        this.vigenciaDeporte = vigenciaDeporte;
    }

    public void setVigenciaAficion(VigenciasAficiones vigenciaAficion) {
        imprimir("48: setVigenciaAficion.", "secuencia: " + secuencia);
        this.vigenciaAficion = vigenciaAficion;
    }

    /*public void setFamiliares(Familiares familiares) {
        this.familiares = familiares;
    }*/
    public void setEntrevistas(HvEntrevistas entrevistas) {
        imprimir("49: setEntrevistas.", "secuencia: " + secuencia);
        this.entrevistas = entrevistas;
    }

    public void setVigenciaIndicador(VigenciasIndicadores vigenciaIndicador) {
        imprimir("50: setVigenciaIndicador.", "secuencia: " + secuencia);
        this.vigenciaIndicador = vigenciaIndicador;
    }

    public void setDemandas(Demandas demandas) {
        imprimir("51: setDemandas.", "secuencia: " + secuencia);
        this.demandas = demandas;
    }

    public void setVigenciaDomiciliaria(VigenciasDomiciliarias vigenciaDomiciliaria) {
        imprimir("52: setVigenciaDomiciliaria.", "secuencia: " + secuencia);
        this.vigenciaDomiciliaria = vigenciaDomiciliaria;
    }

    public void setPruebasAplicadas(EvalResultadosConv pruebasAplicadas) {
        imprimir("53: setPruebasAplicadas.", "secuencia: " + secuencia);
        this.pruebasAplicadas = pruebasAplicadas;
    }

    public String getTelefonoP() {
        imprimir("54: getTelefonoP.", "secuencia: " + secuencia);
        return telefonoP;
    }

    public String getDireccionP() {
        imprimir("55: getDireccionP.", "secuencia: " + secuencia);
        return direccionP;
    }

    public String getEstadoCivilP() {
        imprimir("56: getEstadoCivilP.", "secuencia: " + secuencia);
        return estadoCivilP;
    }

    public String getInformacionAdicionalP() {
        imprimir("57: getInformacionAdicionalP.", "secuencia: " + secuencia);
        return informacionAdicionalP;
    }

    public String getReemplazoP() {
        imprimir("58: getReemplazoP.", "secuencia: " + secuencia);
        return reemplazoP;
    }

    public String getEducacionP() {
        imprimir("59: getEducacionP.", "secuencia: " + secuencia);
        return educacionP;
    }

    public String getIdiomasP() {
        imprimir("60: getIdiomasP.", "secuencia: " + secuencia);
        return idiomasP;
    }

    public String getProyectosP() {
        imprimir("61: getProyectosP.", "secuencia: " + secuencia);
        return proyectosP;
    }

    public String getReferenciasPersonalesP() {
        imprimir("62: getReferenciasPersonalesP.", "secuencia: " + secuencia);
        return referenciasPersonalesP;
    }

    public String getReferenciasFamiliaresP() {
        imprimir("63: getReferenciasFamiliaresP.", "secuencia: " + secuencia);
        return referenciasFamiliaresP;
    }

    public String getExperienciaLaboralP() {
        imprimir("64: getExperienciaLaboralP.", "secuencia: " + secuencia);
        return experienciaLaboralP;
    }

    public String getEventosP() {
        imprimir("65: getEventosP.", "secuencia: " + secuencia);
        return eventosP;
    }

    public String getDeportesP() {
        imprimir("66: getDeportesP.", "secuencia: " + secuencia);
        return deportesP;
    }

    public String getAficionesP() {
        imprimir("67: getAficionesP.", "secuencia: " + secuencia);
        return aficionesP;
    }

    public String getFamiliaresP() {
        imprimir("68: getFamiliaresP.", "secuencia: " + secuencia);
        return familiaresP;
    }

    public String getEntrevistasP() {
        imprimir("69: getEntrevistasP.", "secuencia: " + secuencia);
        return entrevistasP;
    }

    public String getIndicadoresP() {
        imprimir("70: getIndicadoresP.", "secuencia: " + secuencia);
        return indicadoresP;
    }

    public String getDemandasP() {
        imprimir("71: getDemandasP.", "secuencia: " + secuencia);
        return demandasP;
    }

    public String getVisitasDomiciliariasP() {
        imprimir("72: getVisitasDomiciliariasP.", "secuencia: " + secuencia);
        return visitasDomiciliariasP;
    }

    public String getPruebasAplicadasP() {
        imprimir("73: getPruebasAplicadasP.", "secuencia: " + secuencia);
        return pruebasAplicadasP;
    }

    //LISTAS DE VALORES
    public List<TiposDocumentos> getListaTiposDocumentos() {
        imprimir("74: getListaTiposDocumentos.", "secuencia: " + secuencia);
        if (listaTiposDocumentos == null) {
            listaTiposDocumentos = administrarEmpleadoIndividual.tiposDocumentos();
        }
        return listaTiposDocumentos;
    }

    public void setListaTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos) {
        imprimir("75: setListaTiposDocumentos.", "secuencia: " + secuencia);
        this.listaTiposDocumentos = listaTiposDocumentos;
    }

    public List<TiposDocumentos> getFiltradoListaTiposDocumentos() {
        imprimir("76: getFiltradoListaTiposDocumentos.", "secuencia: " + secuencia);
        return filtradoListaTiposDocumentos;
    }

    public void setFiltradoListaTiposDocumentos(List<TiposDocumentos> filtradoListaTiposDocumentos) {
        imprimir("77: setFiltradoListaTiposDocumentos.", "secuencia: " + secuencia);
        this.filtradoListaTiposDocumentos = filtradoListaTiposDocumentos;
    }

    public TiposDocumentos getSeleccionTipoDocumento() {
        imprimir("78: getSeleccionTipoDocumento.", "secuencia: " + secuencia);
        return seleccionTipoDocumento;
    }

    public void setSeleccionTipoDocumento(TiposDocumentos seleccionTipoDocumento) {
        imprimir("79: setSeleccionTipoDocumento.", "secuencia: " + secuencia);
        this.seleccionTipoDocumento = seleccionTipoDocumento;
    }

    public List<Ciudades> getListaCiudades() {
        imprimir("80: getListaCiudades.", "secuencia: " + secuencia);
        if (listaCiudades == null) {
            listaCiudades = administrarEmpleadoIndividual.ciudades();
        }
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudades> listaCiudades) {
        imprimir("81: setListaCiudades.", "secuencia: " + secuencia);
        this.listaCiudades = listaCiudades;
    }

    public List<Ciudades> getFiltradoListaCiudades() {
        imprimir("82: getFiltradoListaCiudades.", "secuencia: " + secuencia);
        return filtradoListaCiudades;
    }

    public void setFiltradoListaCiudades(List<Ciudades> filtradoListaCiudades) {
        imprimir("83: setFiltradoListaCiudades.", "secuencia: " + secuencia);
        this.filtradoListaCiudades = filtradoListaCiudades;
    }

    public Ciudades getSeleccionCiudades() {
        imprimir("84: getSeleccionCiudades.", "secuencia: " + secuencia);
        return seleccionCiudad;
    }

    public void setSeleccionCiudades(Ciudades seleccionCiudades) {
        imprimir("85: setSeleccionCiudades.", "secuencia: " + secuencia);
        this.seleccionCiudad = seleccionCiudades;
    }

    public Ciudades getSeleccionCiudadDocumento() {
        imprimir("86: getSeleccionCiudadDocumento.", "secuencia: " + secuencia);
        return seleccionCiudadDocumento;
    }

    public void setSeleccionCiudadDocumento(Ciudades seleccionCiudadDocumento) {
        imprimir("87: setSeleccionCiudadDocumento.", "secuencia: " + secuencia);
        this.seleccionCiudadDocumento = seleccionCiudadDocumento;
    }

    public List<Cargos> getListaCargos() {
        imprimir("88: getListaCargos.", "secuencia: " + secuencia);
        if (listaCargos == null) {
            listaCargos = administrarEmpleadoIndividual.cargos();
        }
        return listaCargos;
    }

    public void setListaCargos(List<Cargos> listaCargos) {
        imprimir("89: setListaCargos.", "secuencia: " + secuencia);
        this.listaCargos = listaCargos;
    }

    public List<Cargos> getFiltradoListaCargos() {
        imprimir("90: getFiltradoListaCargos.", "secuencia: " + secuencia);
        return filtradoListaCargos;
    }

    public void setFiltradoListaCargos(List<Cargos> filtradoListaCargos) {
        imprimir("91: setFiltradoListaCargos.", "secuencia: " + secuencia);
        this.filtradoListaCargos = filtradoListaCargos;
    }

    public Cargos getSeleccionCargo() {
        imprimir("92: getSeleccionCargo.", "secuencia: " + secuencia);
        return seleccionCargo;
    }

    public void setSeleccionCargo(Cargos seleccionCargo) {
        imprimir("93: setSeleccionCargo.", "secuencia: " + secuencia);
        this.seleccionCargo = seleccionCargo;
    }

    public String getCabezeraDialogoCiudad() {
        imprimir("94: getCabezeraDialogoCiudad.", "secuencia: " + secuencia);
        return cabezeraDialogoCiudad;
    }

    public List<EmpleadoIndividualExportar> getEmpleadoIndividualExportar() {
        imprimir("95: getEmpleadoIndividualExportar.", "secuencia: " + secuencia);
        empleadoIndividualExportar = new ArrayList<>();
        empleadoIndividualExportar.add(new EmpleadoIndividualExportar(empleado, persona, hojaDeVidaPersona));
        return empleadoIndividualExportar;
    }

    public boolean isAceptar() {
        imprimir("96: isAceptar.", "secuencia: " + secuencia);
        return aceptar;
    }

    public void setPruebasAplicadasP(String pruebasAplicadasP) {
        imprimir("97: setPruebasAplicadasP.", "secuencia: " + secuencia);
        this.pruebasAplicadasP = pruebasAplicadasP;
    }

    public String getNombreTabla() {
        imprimir("98: getNombreTabla.", "secuencia: " + secuencia);
        return nombreTabla;
    }

    public BigInteger getSecRastro() {
        imprimir("99: getSecRastro.", "secuencia: " + secuencia);
        return secRastro;
    }

    public void setSecRastro(BigInteger secRastro) {
        imprimir("100: setSecRastro.", "secuencia: " + secuencia);
        this.secRastro = secRastro;
    }

    public boolean isGuardado() {
        imprimir("101: isGuardado.", "secuencia: " + secuencia);
//        refrescar();
        return guardado;
    }

    public boolean isEstadoVP() {
        imprimir("102: isEstadoVP.", "secuencia: " + secuencia);
        return estadoVP;
    }

    public String getVehiculoPropio() {
        imprimir("103: getVehiculoPropio.", "secuencia: " + secuencia);
        return vehiculoPropio;
    }

    public void setVehiculoPropio(String vehiculoPropio) {
        imprimir("104: setVehiculoPropio.", "secuencia: " + secuencia);
        this.vehiculoPropio = vehiculoPropio;
    }

    public StreamedContent getFotoEmpleado() {
        imprimir("105: getFotoEmpleado.", "secuencia: " + secuencia);
        obtenerFotoEmpleado();
        refrescarDatos();
        return fotoEmpleado;
    }

    public boolean isExistenHV() {
        imprimir("106: isExistenHV.", "secuencia: " + secuencia);
        return existenHV;
    }

    public Personas getPersona() {
        imprimir("107: getPersona.", "secuencia: " + secuencia);
        if (persona.getCiudaddocumento() == null) {
            persona.setCiudaddocumento(new Ciudades());
        }
        return persona;
    }

    public void setPersona(Personas persona) {
        imprimir("108: setPersona.", "secuencia: " + secuencia);
        this.persona = persona;
    }

    public void obtenerFotoEmpleado() {
        imprimir("109: obtenerFotoEmpleado.", "secuencia: " + secuencia);
        empleado = administrarEmpleadoIndividual.buscarEmpleado(secuencia);
        String rutaFoto = administrarEmpleadoIndividual.fotoEmpleado(empleado);
        if (rutaFoto != null) {
            try {
                fis = new FileInputStream(new File(rutaFoto));
                fotoEmpleado = new DefaultStreamedContent(fis, "image/jpg");
            } catch (FileNotFoundException e) {
                fotoEmpleado = null;
                System.out.println("Foto del empleado no encontrada. \n" + e);
            }
        }
    }

    public void refrescarDatos() {
        imprimir("110: refrescarDatos.", "secuencia: " + secuencia);
        RequestContext context = RequestContext.getCurrentInstance();
        if (context != null) {
            datosEmpleado();
            RequestContext.getCurrentInstance().update("form:panelInferior");
        }
    }

    public String getInfoRegistroTipoDocumento() {
        imprimir("111: getInfoRegistroTipoDocumento.", "secuencia: " + secuencia);
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovTiposDocumentos");
        infoRegistroTipoDocumento = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoDocumento;
    }

    public void setInfoRegistroTipoDocumento(String infoRegistroTipoDocumento) {
        imprimir("112: setInfoRegistroTipoDocumento.", "secuencia: " + secuencia);
        this.infoRegistroTipoDocumento = infoRegistroTipoDocumento;
    }

    public String getInfoRegistroCiudad() {
        imprimir("113: getInfoRegistroCiudad.", "secuencia: " + secuencia);
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovCiudades");
        infoRegistroCiudad = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudad;
    }

    public void setInfoRegistroCiudad(String infoRegistroCiudad) {
        imprimir("114: setInfoRegistroCiudad.", "secuencia: " + secuencia);
        this.infoRegistroCiudad = infoRegistroCiudad;
    }

    public String getInfoRegistroCargo() {
        imprimir("115: getInfoRegistroCargo.", "secuencia: " + secuencia);
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovCargos");
        infoRegistroCargo = String.valueOf(tabla.getRowCount());
        return infoRegistroCargo;
    }

    public void setInfoRegistroCargo(String infoRegistroCargo) {
        imprimir("116: setInfoRegistroCargo.", "secuencia: " + secuencia);
        this.infoRegistroCargo = infoRegistroCargo;
    }

    public List<Ciudades> getListaCiudadesDocumento() {
        imprimir("117: getListaCiudadesDocumento.", "secuencia: " + secuencia);
        if (listaCiudadesDocumento == null) {
            listaCiudadesDocumento = administrarEmpleadoIndividual.ciudades();
        }
        return listaCiudadesDocumento;
    }

    public void setListaCiudadesDocumento(List<Ciudades> listaCiudadesDocumento) {
        imprimir("118: setListaCiudadesDocumento.", "secuencia: " + secuencia);
        this.listaCiudadesDocumento = listaCiudadesDocumento;
    }

    public List<Ciudades> getFiltradoCiudadesDocumento() {
        imprimir("119: getFiltradoCiudadesDocumento.", "secuencia: " + secuencia);
        return filtradoCiudadesDocumento;
    }

    public void setFiltradoCiudadesDocumento(List<Ciudades> filtradoCiudadesDocumento) {
        imprimir("120: setFiltradoCiudadesDocumento.", "secuencia: " + secuencia);
        this.filtradoCiudadesDocumento = filtradoCiudadesDocumento;
    }

    public String getInfoRegistroCiudadDocumento() {
        imprimir("121: getInfoRegistroCiudadDocumento.", "secuencia: " + secuencia);
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovCiudadesDocumento");
        infoRegistroCiudadDocumento = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudadDocumento;
    }

    public void setInfoRegistroCiudadDocumento(String infoRegistroCiudadDocumento) {
        imprimir("122: setInfoRegistroCiudadDocumento.", "secuencia: " + secuencia);
        this.infoRegistroCiudadDocumento = infoRegistroCiudadDocumento;
    }

    public String getCiudad() {
        imprimir("123: getCiudad.", "secuencia: " + secuencia);
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        imprimir("124: setCiudad.", "secuencia: " + secuencia);
        this.ciudad = ciudad;
    }

    public String getCiudaddocumento() {
        imprimir("125: getCiudaddocumento.", "secuencia: " + secuencia);
//        if(ciudaddocumento == null){
//            ciudaddocumento = persona.getCiudadnacimiento().getNombre();
//        }
        return ciudaddocumento;
    }

    public void setCiudaddocumento(String ciudaddocumento) {
        imprimir("126: setCiudaddocumento.", "secuencia: " + secuencia);
        this.ciudaddocumento = ciudaddocumento;
    }

    public boolean isParaRecargar() {
        imprimir("127: isParaRecargar.", "secuencia: " + secuencia);
        recibirEmpleado(secuencia);
        return paraRecargar;
    }

    public void setParaRecargar(boolean paraRecargar) {
        imprimir("128: setParaRecargar.", "secuencia: " + secuencia);
        this.paraRecargar = paraRecargar;
    }

    private void imprimir(String etiqueta, String texto) {
        if (false) {
            System.out.println(etiqueta + " " + texto);
        }
    }
}
