package Controlador;

import ClasesAyuda.EmpleadoIndividualExportar;
import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Demandas;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.Encargaturas;
import Entidades.EvalResultadosConv;
import Entidades.Familiares;
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
    private Familiares familiares;
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
    //
    private String infoRegistroTipoDocumento, infoRegistroCiudad, infoRegistroCargo, infoRegistroCiudadDocumento;

    public ControlEmpleadoIndividual() {
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        aceptar = true;
        empleado = null;
        modificacionPersona = false;
        modificacionEmpleado = false;
        modificacionHV = false;
        guardado = true;
        existenHV = true;
        persona = new Personas();
//        persona.setCiudaddocumento(new Ciudades());
//        persona.setCiudadnacimiento(new Ciudades());
//        hojaDeVidaPersona = new HVHojasDeVida();
    }

    @PostConstruct
    public void inicializarAdministrador() {
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
        secuencia = sec;
        empleado = null;
        persona = administrarEmpleadoIndividual.encontrarPersona(sec);
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
        BigInteger secPersona = null;
        if (persona != null) {
            secPersona = persona.getSecuencia();
        }
        BigInteger secEmpleado = empleado.getSecuencia();
        hojaDeVidaPersona = administrarEmpleadoIndividual.hvHojaDeVidaPersona(secPersona);
        if (hojaDeVidaPersona != null && hojaDeVidaPersona.getSecuencia() != null) {
            BigInteger secHv = hojaDeVidaPersona.getSecuencia();

            hvReferenciasPersonales = administrarEmpleadoIndividual.referenciasPersonalesPersona(secHv);
            if (hvReferenciasPersonales != null) {
                referenciasPersonalesP = hvReferenciasPersonales.getNombrepersona();
            } else {
                referenciasPersonalesP = "";
            }

            hvReferenciasFamiliares = administrarEmpleadoIndividual.referenciasFamiliaresPersona(secHv);
            if (hvReferenciasFamiliares != null) {
                referenciasFamiliaresP = hvReferenciasFamiliares.getNombrepersona();
            } else {
                referenciasFamiliaresP = "";
            }
            experienciaLaboral = administrarEmpleadoIndividual.experienciaLaboralPersona(secHv);
            if (experienciaLaboral != null) {
                experienciaLaboralP = experienciaLaboral.getEmpresa() + "  " + formatoFecha.format(experienciaLaboral.getFechadesde());
            } else {
                experienciaLaboralP = "";
            }
            entrevistas = administrarEmpleadoIndividual.entrevistasPersona(secHv);
            if (entrevistas != null) {
                entrevistasP = entrevistas.getNombre() + "  " + formatoFecha.format(entrevistas.getFecha());
            } else {
                entrevistasP = "";
            }
            existenHV = false;
        } else {
            hojaDeVidaPersona = new HVHojasDeVida();
//            hojaDeVidaPersona.setPerfilprofesional(" ");
            referenciasPersonalesP = "";
            referenciasFamiliaresP = "";
            experienciaLaboralP = "";
            entrevistasP = "";
            existenHV = true;
        }
        telefono = administrarEmpleadoIndividual.primerTelefonoPersona(secPersona);
        if (telefono != null) {
            telefonoP = telefono.getTipotelefono().getNombre() + " :  " + telefono.getNumerotelefono();
        } else {
            telefonoP = "";
        }
        direccion = administrarEmpleadoIndividual.primeraDireccionPersona(secPersona);
        if (direccion != null) {
            direccionP = direccion.getDireccionalternativa();
        } else {
            direccionP = "";
        }
        estadoCivil = administrarEmpleadoIndividual.estadoCivilPersona(secPersona);
        if (estadoCivil != null) {
            estadoCivilP = estadoCivil.getEstadocivil().getDescripcion() + "   " + formatoFecha.format(estadoCivil.getFechavigencia());
        } else {
            estadoCivilP = "";
        }
        informacionAdicional = administrarEmpleadoIndividual.informacionAdicionalPersona(secEmpleado);
        if (informacionAdicional != null) {
            informacionAdicionalP = informacionAdicional.getDescripcion() + "  " + formatoFecha.format(informacionAdicional.getFechainicial());
        } else {
            informacionAdicionalP = "";
        }
        encargatura = administrarEmpleadoIndividual.reemplazoPersona(secEmpleado);
        if (encargatura != null) {
            reemplazoP = encargatura.getTiporeemplazo().getNombre() + "  " + formatoFecha.format(encargatura.getFechainicial());
        } else {
            reemplazoP = "";
        }
        vigenciaFormal = administrarEmpleadoIndividual.educacionPersona(secPersona);
        if (vigenciaFormal != null) {
            educacionP = vigenciaFormal.getTipoeducacion().getNombre() + "  " + formatoFecha.format(vigenciaFormal.getFechavigencia());
        } else {
            educacionP = "";
        }
        idiomasPersona = administrarEmpleadoIndividual.idiomasPersona(secPersona);
        if (idiomasPersona != null) {
            idiomasP = idiomasPersona.getIdioma().getNombre();
        } else {
            idiomasP = "";
        }
        vigenciaProyecto = administrarEmpleadoIndividual.proyectosPersona(secEmpleado);
        if (vigenciaProyecto != null) {
            proyectosP = vigenciaProyecto.getProyecto().getNombreproyecto() + "  " + formatoFecha.format(vigenciaProyecto.getFechafinal());
        } else {
            proyectosP = "";
        }
        vigenciaEvento = administrarEmpleadoIndividual.eventosPersona(secEmpleado);
        if (vigenciaEvento != null) {
            eventosP = vigenciaEvento.getEvento().getDescripcion() + "  " + formatoFecha.format(vigenciaEvento.getFechainicial());
        } else {
            eventosP = "";
        }
        vigenciaDeporte = administrarEmpleadoIndividual.deportesPersona(secPersona);
        if (vigenciaDeporte != null) {
            deportesP = vigenciaDeporte.getDeporte().getNombre() + "  " + formatoFecha.format(vigenciaDeporte.getFechainicial());
        } else {
            deportesP = "";
        }
        vigenciaAficion = administrarEmpleadoIndividual.aficionesPersona(secPersona);
        if (vigenciaAficion != null) {
            aficionesP = vigenciaAficion.getAficion().getDescripcion() + "  " + formatoFecha.format(vigenciaAficion.getFechainicial());
        } else {
            aficionesP = "";
        }
        familiares = administrarEmpleadoIndividual.familiaresPersona(secPersona);
        if (familiares != null) {
            familiaresP = familiares.getTipofamiliar().getTipo() + "  " + familiares.getPersonafamiliar().getPrimerapellido() + "  " + familiares.getPersonafamiliar().getNombre();
        } else {
            familiaresP = "";
        }
        vigenciaIndicador = administrarEmpleadoIndividual.indicadoresPersona(secEmpleado);
        if (vigenciaIndicador != null) {
            indicadoresP = vigenciaIndicador.getIndicador().getDescripcion() + "  " + formatoFecha.format(vigenciaIndicador.getFechainicial());
        } else {
            indicadoresP = "";
        }
        demandas = administrarEmpleadoIndividual.demandasPersona(secEmpleado);
        if (demandas != null) {
            demandasP = demandas.getMotivo().getDescripcion();
        } else {
            demandasP = "";
        }
        vigenciaDomiciliaria = administrarEmpleadoIndividual.visitasDomiciliariasPersona(secPersona);
        if (vigenciaDomiciliaria != null) {
            visitasDomiciliariasP = "VISITADO EL:  " + formatoFecha.format(vigenciaDomiciliaria.getFecha());
        } else {
            visitasDomiciliariasP = "";
        }
        pruebasAplicadas = administrarEmpleadoIndividual.pruebasAplicadasPersona(secEmpleado);
        if (pruebasAplicadas != null) {
            pruebasAplicadasP = pruebasAplicadas.getNombreprueba() + " -> " + pruebasAplicadas.getPuntajeobtenido() + "%";
        } else {
            pruebasAplicadasP = "";
        }
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
        aceptar = false;
    }

    //METODOS LOVS
    public void seleccionarTipoDocumento() {
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
        if (persona != null) {
            if (persona.getCiudaddocumento() != null && seleccionCiudadDocumento != null) {
                System.out.println("entra al if 2");
                RequestContext context = RequestContext.getCurrentInstance();
                if (modificacionCiudad == 0) {
                    persona.setCiudaddocumento(seleccionCiudadDocumento);
                    System.out.println("nueva ciudad : " + seleccionCiudadDocumento);
                    System.out.println("nueva ciudad : " + persona.getCiudaddocumento());
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
                System.out.println("entra al else");
                System.out.println("ciudadnacimiento" + persona.getCiudadnacimiento().getNombre());
                persona.setCiudaddocumento(persona.getCiudadnacimiento());
                System.out.println("ciudadnacimiento" + persona.getCiudaddocumento().getNombre());
            }
        }
    }

    public void cancelarSeleccionCiudadDocumento() {
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
        RequestContext context = RequestContext.getCurrentInstance();
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
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "HojaVidaEmpleadoPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "HojaVidaEmpleadoXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
    public void cambiarTablaRastro(String tabla) {
        System.out.println("cambiarTablaRastro");
        nombreTabla = tabla;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
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
        System.out.println(this.getClass().getName() + ".eventoDataSelectFechaNacimiento");
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
        System.out.println("Here");
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
        System.out.println("Here");
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
        System.out.println("modificarCampo");
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
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (modificacionPersona == true) {
                    administrarEmpleadoIndividual.modificarPersona(persona);
                    modificacionPersona = false;
                }
                if (modificacionEmpleado == true) {
                    administrarEmpleadoIndividual.modificarEmpleado(empleado);
                    modificacionEmpleado = false;
                }
                if (modificacionHV == true) {
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
        RequestContext context = RequestContext.getCurrentInstance();
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
        if (empleado == null) {
            empleado = administrarEmpleadoIndividual.buscarEmpleado(secuencia);
            if (empleado != null) {
                persona = empleado.getPersona();
            }
        }
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public HVHojasDeVida getHojaDeVidaPersona() {
        return hojaDeVidaPersona;
    }

    public void setHojaDeVidaPersona(HVHojasDeVida hojaDeVidaPersona) {
        this.hojaDeVidaPersona = hojaDeVidaPersona;
    }

    public void setTelefono(Telefonos telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(Direcciones direccion) {
        this.direccion = direccion;
    }

    public void setEstadoCivil(VigenciasEstadosCiviles estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public void setInformacionAdicional(InformacionesAdicionales informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }

    public void setEncargatura(Encargaturas encargatura) {
        this.encargatura = encargatura;
    }

    public void setVigenciaFormal(VigenciasFormales vigenciaFormal) {
        this.vigenciaFormal = vigenciaFormal;
    }

    public void setIdiomasPersona(IdiomasPersonas idiomasPersona) {
        this.idiomasPersona = idiomasPersona;
    }

    public void setVigenciaProyecto(VigenciasProyectos vigenciaProyecto) {
        this.vigenciaProyecto = vigenciaProyecto;
    }

    public HvReferencias getHvReferenciasPersonales() {
        return hvReferenciasPersonales;
    }

    public void setHvReferenciasPersonales(HvReferencias hvReferenciasPersonales) {
        this.hvReferenciasPersonales = hvReferenciasPersonales;
    }

    public void setHvReferenciasFamiliares(HvReferencias hvReferenciasFamiliares) {
        this.hvReferenciasFamiliares = hvReferenciasFamiliares;
    }

    public void setExperienciaLaboral(HvExperienciasLaborales experienciaLaboral) {
        this.experienciaLaboral = experienciaLaboral;
    }

    public void setVigenciaEvento(VigenciasEventos vigenciaEvento) {
        this.vigenciaEvento = vigenciaEvento;
    }

    public void setVigenciaDeporte(VigenciasDeportes vigenciaDeporte) {
        this.vigenciaDeporte = vigenciaDeporte;
    }

    public void setVigenciaAficion(VigenciasAficiones vigenciaAficion) {
        this.vigenciaAficion = vigenciaAficion;
    }

    public void setFamiliares(Familiares familiares) {
        this.familiares = familiares;
    }

    public void setEntrevistas(HvEntrevistas entrevistas) {
        this.entrevistas = entrevistas;
    }

    public void setVigenciaIndicador(VigenciasIndicadores vigenciaIndicador) {
        this.vigenciaIndicador = vigenciaIndicador;
    }

    public void setDemandas(Demandas demandas) {
        this.demandas = demandas;
    }

    public void setVigenciaDomiciliaria(VigenciasDomiciliarias vigenciaDomiciliaria) {
        this.vigenciaDomiciliaria = vigenciaDomiciliaria;
    }

    public void setPruebasAplicadas(EvalResultadosConv pruebasAplicadas) {
        this.pruebasAplicadas = pruebasAplicadas;
    }

    public String getTelefonoP() {
        return telefonoP;
    }

    public String getDireccionP() {
        return direccionP;
    }

    public String getEstadoCivilP() {
        return estadoCivilP;
    }

    public String getInformacionAdicionalP() {
        return informacionAdicionalP;
    }

    public String getReemplazoP() {
        return reemplazoP;
    }

    public String getEducacionP() {
        return educacionP;
    }

    public String getIdiomasP() {
        return idiomasP;
    }

    public String getProyectosP() {
        return proyectosP;
    }

    public String getReferenciasPersonalesP() {
        return referenciasPersonalesP;
    }

    public String getReferenciasFamiliaresP() {
        return referenciasFamiliaresP;
    }

    public String getExperienciaLaboralP() {
        return experienciaLaboralP;
    }

    public String getEventosP() {
        return eventosP;
    }

    public String getDeportesP() {
        return deportesP;
    }

    public String getAficionesP() {
        return aficionesP;
    }

    public String getFamiliaresP() {
        return familiaresP;
    }

    public String getEntrevistasP() {
        return entrevistasP;
    }

    public String getIndicadoresP() {
        return indicadoresP;
    }

    public String getDemandasP() {
        return demandasP;
    }

    public String getVisitasDomiciliariasP() {
        return visitasDomiciliariasP;
    }

    public String getPruebasAplicadasP() {
        return pruebasAplicadasP;
    }

    //LISTAS DE VALORES
    public List<TiposDocumentos> getListaTiposDocumentos() {
        if (listaTiposDocumentos == null) {
            listaTiposDocumentos = administrarEmpleadoIndividual.tiposDocumentos();
        }
        return listaTiposDocumentos;
    }

    public void setListaTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos) {
        this.listaTiposDocumentos = listaTiposDocumentos;
    }

    public List<TiposDocumentos> getFiltradoListaTiposDocumentos() {
        return filtradoListaTiposDocumentos;
    }

    public void setFiltradoListaTiposDocumentos(List<TiposDocumentos> filtradoListaTiposDocumentos) {
        this.filtradoListaTiposDocumentos = filtradoListaTiposDocumentos;
    }

    public TiposDocumentos getSeleccionTipoDocumento() {
        return seleccionTipoDocumento;
    }

    public void setSeleccionTipoDocumento(TiposDocumentos seleccionTipoDocumento) {
        this.seleccionTipoDocumento = seleccionTipoDocumento;
    }

    public List<Ciudades> getListaCiudades() {
        if (listaCiudades == null) {
            listaCiudades = administrarEmpleadoIndividual.ciudades();
        }
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudades> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public List<Ciudades> getFiltradoListaCiudades() {
        return filtradoListaCiudades;
    }

    public void setFiltradoListaCiudades(List<Ciudades> filtradoListaCiudades) {
        this.filtradoListaCiudades = filtradoListaCiudades;
    }

    public Ciudades getSeleccionCiudades() {
        return seleccionCiudad;
    }

    public void setSeleccionCiudades(Ciudades seleccionCiudades) {
        this.seleccionCiudad = seleccionCiudades;
    }

    public Ciudades getSeleccionCiudadDocumento() {
        return seleccionCiudadDocumento;
    }

    public void setSeleccionCiudadDocumento(Ciudades seleccionCiudadDocumento) {
        this.seleccionCiudadDocumento = seleccionCiudadDocumento;
    }

    public List<Cargos> getListaCargos() {
        if (listaCargos == null) {
            listaCargos = administrarEmpleadoIndividual.cargos();
        }
        return listaCargos;
    }

    public void setListaCargos(List<Cargos> listaCargos) {
        this.listaCargos = listaCargos;
    }

    public List<Cargos> getFiltradoListaCargos() {
        return filtradoListaCargos;
    }

    public void setFiltradoListaCargos(List<Cargos> filtradoListaCargos) {
        this.filtradoListaCargos = filtradoListaCargos;
    }

    public Cargos getSeleccionCargo() {
        return seleccionCargo;
    }

    public void setSeleccionCargo(Cargos seleccionCargo) {
        this.seleccionCargo = seleccionCargo;
    }

    public String getCabezeraDialogoCiudad() {
        return cabezeraDialogoCiudad;
    }

    public List<EmpleadoIndividualExportar> getEmpleadoIndividualExportar() {
        empleadoIndividualExportar = new ArrayList<EmpleadoIndividualExportar>();
        empleadoIndividualExportar.add(new EmpleadoIndividualExportar(empleado, persona, hojaDeVidaPersona));
        return empleadoIndividualExportar;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setPruebasAplicadasP(String pruebasAplicadasP) {
        this.pruebasAplicadasP = pruebasAplicadasP;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public BigInteger getSecRastro() {
        return secRastro;
    }

    public void setSecRastro(BigInteger secRastro) {
        this.secRastro = secRastro;
    }

    public boolean isGuardado() {
//        refrescar();
        return guardado;
    }

    public boolean isEstadoVP() {
        return estadoVP;
    }

    public String getVehiculoPropio() {
        return vehiculoPropio;
    }

    public void setVehiculoPropio(String vehiculoPropio) {
        this.vehiculoPropio = vehiculoPropio;
    }

    public StreamedContent getFotoEmpleado() {
        obtenerFotoEmpleado();
        refrescarDatos();
        return fotoEmpleado;
    }

    public boolean isExistenHV() {
        return existenHV;
    }

    public Personas getPersona() {
        if (persona.getCiudaddocumento() == null) {
            persona.setCiudaddocumento(new Ciudades());
        }
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public void obtenerFotoEmpleado() {
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
        RequestContext context = RequestContext.getCurrentInstance();
        if (context != null) {
            datosEmpleado();
            RequestContext.getCurrentInstance().update("form:panelInferior");
        }
    }

    public String getInfoRegistroTipoDocumento() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovTiposDocumentos");
        infoRegistroTipoDocumento = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoDocumento;
    }

    public void setInfoRegistroTipoDocumento(String infoRegistroTipoDocumento) {
        this.infoRegistroTipoDocumento = infoRegistroTipoDocumento;
    }

    public String getInfoRegistroCiudad() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovCiudades");
        infoRegistroCiudad = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudad;
    }

    public void setInfoRegistroCiudad(String infoRegistroCiudad) {
        this.infoRegistroCiudad = infoRegistroCiudad;
    }

    public String getInfoRegistroCargo() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovCargos");
        infoRegistroCargo = String.valueOf(tabla.getRowCount());
        return infoRegistroCargo;
    }

    public void setInfoRegistroCargo(String infoRegistroCargo) {
        this.infoRegistroCargo = infoRegistroCargo;
    }

    public List<Ciudades> getListaCiudadesDocumento() {
        if (listaCiudadesDocumento == null) {
            listaCiudadesDocumento = administrarEmpleadoIndividual.ciudades();
        }
        return listaCiudadesDocumento;
    }

    public void setListaCiudadesDocumento(List<Ciudades> listaCiudadesDocumento) {
        this.listaCiudadesDocumento = listaCiudadesDocumento;
    }

    public List<Ciudades> getFiltradoCiudadesDocumento() {
        return filtradoCiudadesDocumento;
    }

    public void setFiltradoCiudadesDocumento(List<Ciudades> filtradoCiudadesDocumento) {
        this.filtradoCiudadesDocumento = filtradoCiudadesDocumento;
    }

    public String getInfoRegistroCiudadDocumento() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovCiudadesDocumento");
        infoRegistroCiudadDocumento = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudadDocumento;
    }

    public void setInfoRegistroCiudadDocumento(String infoRegistroCiudadDocumento) {
        this.infoRegistroCiudadDocumento = infoRegistroCiudadDocumento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCiudaddocumento() {
//        if(ciudaddocumento == null){
//            ciudaddocumento = persona.getCiudadnacimiento().getNombre();
//        }
        return ciudaddocumento;
    }

    public void setCiudaddocumento(String ciudaddocumento) {
        this.ciudaddocumento = ciudaddocumento;
    }

}
