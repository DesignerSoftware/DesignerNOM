/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.Familiares;
import Entidades.Personas;
import Entidades.TiposDocumentos;
import Entidades.TiposFamiliares;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCiudadesInterface;
import InterfaceAdministrar.AdministrarFamiliaresInterface;
import InterfaceAdministrar.AdministrarPersonaIndividualInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposDocumentosInterface;
import InterfaceAdministrar.AdministrarTiposFamiliaresInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
@Named(value = "controlFamiliares")
@SessionScoped
public class ControlFamiliares implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarFamiliaresInterface administrarFamiliares;

    private List<Familiares> listaFamiliares;
    private List<Familiares> listaFamiliaresCrear;
    private List<Familiares> listaFamiliaresBorrar;
    private List<Familiares> listaFamiliaresEditar;
    private List<Familiares> listaFamiliaresFiltrar;
    private Familiares familiarSeleccionado;
    private Familiares nuevoFamiliar;
    private Familiares duplicarFamiliares;
    private Familiares editarFamiliar;
    //LOV PERSONAS
    private List<Personas> lovPersonas;
    private List<Personas> filtrarLovPersonas;
    private Personas personaSeleccionada;
    //LOV TIPOS FAMILIARES
    private List<TiposFamiliares> lovTiposFamiliares;
    private List<TiposFamiliares> filtrarLovTiposFamiliares;
    private TiposFamiliares tipoFamiliarSeleccionado;
    //LOV TIPOS DOCUMENTOS
    private List<TiposDocumentos> lovTiposDocumentos;
    private List<TiposDocumentos> filtrarLovTiposDocumentos;
    private TiposDocumentos tipoDocumentoSeleccionado;
    //LOV CIUDADES
    private List<Ciudades> lovCiudades;
    private List<Ciudades> filtrarLovCiudades;
    private Ciudades ciudadSeleccionada;
    //OTROS
    private int tipoActualizacion;
    private int bandera;
    private boolean guardado;
    private boolean aceptar;
    private BigDecimal l;
    private int k;
    private int cualCelda, tipoLista;
    private boolean permitirIndex;
    private String altoTabla, paginaanterior, mensajeValidacion;
    private Column nombre, ocupacion, columnaTipo, smedico, sfamiliar, beneficiario, upcadicional, valorupc;
    private String infoRegistroFamiliar, infoRegistroPersonas, infoRegistroTipoFamiliar, infoRegistroTipoDocumento, infoRegistroCiudades, infoRegistroCiudadNacimiento;
    private DataTable tablaC;
    private boolean activarLOV, activardatos;
//    private Empleados empleado;
    private Personas personas, nuevaPersona;
    private List<Personas> crearPersonas;

    public ControlFamiliares() {
        altoTabla = "285";
        listaFamiliares = null;
        listaFamiliaresBorrar = new ArrayList<Familiares>();
        listaFamiliaresCrear = new ArrayList<Familiares>();
        listaFamiliaresEditar = new ArrayList<Familiares>();
        listaFamiliaresFiltrar = null;
        k = 0;
        guardado = true;
        activarLOV = true;
        permitirIndex = true;
        nuevoFamiliar = new Familiares();
        nuevoFamiliar.setPersona(new Personas());
        nuevoFamiliar.setTipofamiliar(new TiposFamiliares());
        nuevoFamiliar.setPersonafamiliar(new Personas());
        mensajeValidacion = " ";
//        empleado = new Empleados();
        personas = new Personas();
        nuevaPersona = new Personas();
        nuevaPersona.setTipodocumento(new TiposDocumentos());
        nuevaPersona.setCiudaddocumento(new Ciudades());
        nuevaPersona.setCiudadnacimiento(new Ciudades());
        lovCiudades = null;
        lovPersonas = null;
        lovTiposFamiliares = null;
        lovTiposDocumentos = null;
        crearPersonas = new ArrayList<Personas>();
        activardatos = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarFamiliares.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String pagina, BigInteger secuencia) {
        paginaanterior = pagina;
        getListaFamiliares();
        personas = administrarFamiliares.consultarPersona(secuencia);
        if (listaFamiliares != null) {
            if (!listaFamiliares.isEmpty()) {
                familiarSeleccionado = listaFamiliares.get(0);
            }
        }

    }

    public String retornarPagina() {
        return paginaanterior;
    }

    public void cambiarIndice(Familiares familiar, int celda) {
        if (permitirIndex == true) {
            cualCelda = celda;
            familiarSeleccionado = familiar;
            activarDatos();
            if (cualCelda == 1) {
                deshabilitarBotonLov();
                familiarSeleccionado.getPersonafamiliar().getNombreCompleto();
            } else if (cualCelda == 2) {
                deshabilitarBotonLov();
                familiarSeleccionado.getOcupacion();
            } else if (cualCelda == 3) {
                habilitarBotonLov();
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
                deshabilitarBotonLov();
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
                    if (listaFamiliaresEditar.isEmpty()) {
                        listaFamiliaresEditar.add(familiarSeleccionado);
                    } else if (!listaFamiliaresEditar.contains(familiarSeleccionado)) {
                        listaFamiliaresEditar.add(familiarSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
            } else if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
                if (listaFamiliaresEditar.isEmpty()) {
                    listaFamiliaresEditar.add(familiarSeleccionado);
                } else if (!listaFamiliaresEditar.contains(familiarSeleccionado)) {
                    listaFamiliaresEditar.add(familiarSeleccionado);
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
                if (listaFamiliaresEditar.isEmpty()) {
                    listaFamiliaresEditar.add(familiarSeleccionado);
                } else if (!listaFamiliaresEditar.contains(familiarSeleccionado)) {
                    listaFamiliaresEditar.add(familiarSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
            if (listaFamiliaresEditar.isEmpty()) {
                listaFamiliaresEditar.add(familiarSeleccionado);
            } else if (!listaFamiliaresEditar.contains(familiarSeleccionado)) {
                listaFamiliaresEditar.add(familiarSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                deshabilitarBotonLov();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPO")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getTipofamiliar().setTipo(nuevoFamiliar.getTipofamiliar().getTipo());
            } else if (tipoNuevo == 2) {
                duplicarFamiliares.getTipofamiliar().setTipo(nuevoFamiliar.getTipofamiliar().getTipo());
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
                    duplicarFamiliares.setTipofamiliar(lovTiposFamiliares.get(indiceUnicoElemento));
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
                duplicarFamiliares.getPersonafamiliar().getCiudaddocumento().setNombre(duplicarFamiliares.getPersonafamiliar().getCiudaddocumento().getNombre());
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
                    duplicarFamiliares.getPersonafamiliar().setCiudaddocumento(lovCiudades.get(indiceUnicoElemento));
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
                duplicarFamiliares.getPersonafamiliar().getCiudadnacimiento().setNombre(duplicarFamiliares.getPersonafamiliar().getCiudadnacimiento().getNombre());
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
                    duplicarFamiliares.getPersonafamiliar().setCiudadnacimiento(lovCiudades.get(indiceUnicoElemento));
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
                duplicarFamiliares.getPersonafamiliar().getTipodocumento().setNombrelargo(nuevoFamiliar.getPersonafamiliar().getTipodocumento().getNombrelargo());
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
                    duplicarFamiliares.getPersonafamiliar().setTipodocumento(lovTiposDocumentos.get(indiceUnicoElemento));
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

    public void guardarSalir() {
        guardarCambios();
        salir();
    }

    public void cancelarSalir() {
        cancelarModificacion();
        salir();
    }

    public void guardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listaFamiliaresBorrar.isEmpty()) {
                    administrarFamiliares.borrarFamiliares(listaFamiliaresBorrar);
                    listaFamiliaresBorrar.clear();
                }
                if (!listaFamiliaresCrear.isEmpty()) {
                    administrarFamiliares.crearFamilares(listaFamiliaresCrear);
                    listaFamiliaresCrear.clear();
                }
                if (!listaFamiliaresEditar.isEmpty()) {
                    administrarFamiliares.modificarFamiliares(listaFamiliaresEditar);
                    listaFamiliaresEditar.clear();
                }
                listaFamiliares = null;
                getListaFamiliares();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                familiarSeleccionado = null;
            }

            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosFamiliares");
            deshabilitarBotonLov();
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacion() {
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
        listaFamiliaresEditar.clear();
        k = 0;
        listaFamiliares = null;
        familiarSeleccionado = null;
        guardado = true;
        permitirIndex = true;
        activardatos = true;
        getListaFamiliares();
        contarRegistros();
        deshabilitarBotonLov();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:infoRegistro");
        RequestContext.getCurrentInstance().update("form:datosFamiliares");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void editarCelda() {

        if (familiarSeleccionado != null) {
            editarFamiliar = familiarSeleccionado;

            if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreD");
                RequestContext.getCurrentInstance().execute("PF('editarNombreD').show()");
                deshabilitarBotonLov();
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarOcupacionD");
                RequestContext.getCurrentInstance().execute("PF('editarOcupacionD').show()");
                deshabilitarBotonLov();
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoFamiliarD");
                RequestContext.getCurrentInstance().execute("PF('editarTipoFamiliarD').show()");
                habilitarBotonLov();
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSMedicoD");
                RequestContext.getCurrentInstance().execute("PF('editarSMedicoD').show()");
                deshabilitarBotonLov();
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSFamiliarD");
                RequestContext.getCurrentInstance().execute("PF('editarSFamiliarD').show()");
                cualCelda = -1;
                deshabilitarBotonLov();
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarBeneficiarioD");
                RequestContext.getCurrentInstance().execute("PF('editarBeneficiarioD').show()");
                cualCelda = -1;
                deshabilitarBotonLov();
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarUpcAD");
                RequestContext.getCurrentInstance().execute("PF('editarUpcAD').show()");
                cualCelda = -1;
                deshabilitarBotonLov();
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorUpcD");
                RequestContext.getCurrentInstance().execute("PF('editarValorUpcD').show()");
                cualCelda = -1;
                deshabilitarBotonLov();
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoFamilar() {
        if (bandera == 1) {
            altoTabla = "285";
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
        k++;
        l = BigDecimal.valueOf(k);
        nuevoFamiliar.setSecuencia(l);
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
        contarRegistros();
        deshabilitarBotonLov();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:infoRegistro");
        RequestContext.getCurrentInstance().update("form:datosFamiliares");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFamiliarPersona').hide()");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        nuevoFamiliar = new Familiares();
        nuevoFamiliar.setPersona(personas);
        nuevoFamiliar.setPersonafamiliar(new Personas());
        nuevoFamiliar.setTipofamiliar(new TiposFamiliares());
    }

    public void limpiarNuevoFamiliar() {
        nuevoFamiliar = new Familiares();
        nuevoFamiliar.setPersona(personas);
        nuevoFamiliar.setPersonafamiliar(new Personas());
        nuevoFamiliar.setTipofamiliar(new TiposFamiliares());
    }

    public void limpiarDuplicarFamiliar() {
        duplicarFamiliares = new Familiares();
        duplicarFamiliares.setPersona(personas);
        duplicarFamiliares.setPersonafamiliar(new Personas());
        duplicarFamiliares.setTipofamiliar(new TiposFamiliares());

    }

    public void duplicarFamiliar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (familiarSeleccionado != null) {
            duplicarFamiliares = new Familiares();

            if (tipoLista == 0) {

                duplicarFamiliares.setPersonafamiliar(familiarSeleccionado.getPersonafamiliar());
                duplicarFamiliares.setOcupacion(familiarSeleccionado.getOcupacion());
                duplicarFamiliares.setTipofamiliar(familiarSeleccionado.getTipofamiliar());
                duplicarFamiliares.setServiciomedico(familiarSeleccionado.getServiciomedico());
                duplicarFamiliares.setSubsidiofamiliar(familiarSeleccionado.getSubsidiofamiliar());
                duplicarFamiliares.setBeneficiario(familiarSeleccionado.getBeneficiario());
                duplicarFamiliares.setUpcadicional(familiarSeleccionado.getUpcadicional());
                duplicarFamiliares.setValorupcadicional(familiarSeleccionado.getValorupcadicional());
            }
            if (tipoLista == 1) {

                duplicarFamiliares.setPersonafamiliar(familiarSeleccionado.getPersonafamiliar());
                duplicarFamiliares.setOcupacion(familiarSeleccionado.getOcupacion());
                duplicarFamiliares.setTipofamiliar(familiarSeleccionado.getTipofamiliar());
                duplicarFamiliares.setServiciomedico(familiarSeleccionado.getServiciomedico());
                duplicarFamiliares.setSubsidiofamiliar(familiarSeleccionado.getSubsidiofamiliar());
                duplicarFamiliares.setBeneficiario(familiarSeleccionado.getBeneficiario());
                duplicarFamiliares.setUpcadicional(familiarSeleccionado.getUpcadicional());
                duplicarFamiliares.setValorupcadicional(familiarSeleccionado.getValorupcadicional());
            }
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersonaFamiliar");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFamiliarPersona').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarFamiliares.getPersonafamiliar().getNombreCompleto() == null || duplicarFamiliares.getTipofamiliar().getTipo() == null) {
            mensajeValidacion = mensajeValidacion + " Los campos nombre y Tipo Familiar son Obligatorios\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoFamiliar");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoFamiliar').show()");
        }

        if (pasa == 0) {
            k++;
            l = BigDecimal.valueOf(k);
            duplicarFamiliares.setSecuencia(l);
            duplicarFamiliares.setPersona(personas);
            listaFamiliares.add(duplicarFamiliares);
            listaFamiliaresCrear.add(duplicarFamiliares);
            familiarSeleccionado = duplicarFamiliares;
            getListaFamiliares();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosFamiliares");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFamiliarPersona').hide()");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                altoTabla = "285";
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
            duplicarFamiliares = new Familiares();
        }
    }

    public void borrarFamiliar() {

        if (familiarSeleccionado != null) {
            if (!listaFamiliaresEditar.isEmpty() && listaFamiliaresEditar.contains(familiarSeleccionado)) {
                int modIndex = listaFamiliaresEditar.indexOf(familiarSeleccionado);
                listaFamiliaresEditar.remove(modIndex);
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
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosFamiliares");
            contarRegistros();
            familiarSeleccionado = null;
            deshabilitarBotonLov();

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            altoTabla = "265";
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
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "285";
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
        }
    }

    public void salir() {
        if (bandera == 1) {
            altoTabla = "285";
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
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosFamiliares");
            bandera = 0;
            listaFamiliaresFiltrar = null;
            tipoLista = 0;
            deshabilitarBotonLov();
        }

        listaFamiliaresBorrar.clear();
        listaFamiliaresCrear.clear();
        listaFamiliaresEditar.clear();
        familiarSeleccionado = null;
        k = 0;
        listaFamiliares = null;
        guardado = true;
    }

    public void asignarIndex(Familiares familiar, int dlg, int LND) {
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
                    if (listaFamiliaresEditar.isEmpty()) {
                        listaFamiliaresEditar.add(familiarSeleccionado);
                    } else if (!listaFamiliaresEditar.contains(familiarSeleccionado)) {
                        listaFamiliaresEditar.add(familiarSeleccionado);
                    }
                }
            } else {
                familiarSeleccionado.setTipofamiliar(tipoFamiliarSeleccionado);
                if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
                    if (listaFamiliaresEditar.isEmpty()) {
                        listaFamiliaresEditar.add(familiarSeleccionado);
                    } else if (!listaFamiliaresEditar.contains(familiarSeleccionado)) {
                        listaFamiliaresEditar.add(familiarSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosFamiliares");
        } else if (tipoActualizacion == 1) {
            nuevoFamiliar.setTipofamiliar(tipoFamiliarSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersonaFamiliar");
        } else if (tipoActualizacion == 2) {
            duplicarFamiliares.setTipofamiliar(tipoFamiliarSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersonaFamiliar");
        }

        filtrarLovTiposFamiliares = null;
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
        filtrarLovTiposFamiliares = null;
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

    public void actualizarCiudad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 1) {
            nuevaPersona.setCiudaddocumento(ciudadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadDocumentoModPersonal");
        }
        filtrarLovCiudades = null;
        ciudadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadDocumentoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovCiudadDocumento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCD");

        context.reset("formularioDialogos:lovCiudadDocumento:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadDocumento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadDocumentoDialogo').hide()");
    }

    public void cancelarCambioCiudad() {
        filtrarLovCiudades = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        ciudadSeleccionada = null;
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
//        if (tipoActualizacion == 0) {
//            if (tipoLista == 0) {
//                familiarSeleccionado.getPersonafamiliar().setCiudadnacimiento(ciudadSeleccionada);
//                if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
//                    if (listaFamiliaresEditar.isEmpty()) {
//                        listaFamiliaresEditar.add(familiarSeleccionado);
//                    } else if (!listaFamiliaresEditar.contains(familiarSeleccionado)) {
//                        listaFamiliaresEditar.add(familiarSeleccionado);
//                    }
//                }
//            } else {
//                familiarSeleccionado.getPersonafamiliar().setCiudadnacimiento(ciudadSeleccionada);
//                if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
//                    if (listaFamiliaresEditar.isEmpty()) {
//                        listaFamiliaresEditar.add(familiarSeleccionado);
//                    } else if (!listaFamiliaresEditar.contains(familiarSeleccionado)) {
//                        listaFamiliaresEditar.add(familiarSeleccionado);
//                    }
//                }
//            }
//            if (guardado == true) {
//                guardado = false;
//                RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            }
//            permitirIndex = true;
//            deshabilitarBotonLov();
//            RequestContext.getCurrentInstance().update("form:datosFamiliares");
//        } 
        if (tipoActualizacion == 1) {
            nuevaPersona.setCiudadnacimiento(ciudadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadNacimientoModPersonal");
        }

        filtrarLovCiudades = null;
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
        filtrarLovCiudades = null;
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
                    if (listaFamiliaresEditar.isEmpty()) {
                        listaFamiliaresEditar.add(familiarSeleccionado);
                    } else if (!listaFamiliaresEditar.contains(familiarSeleccionado)) {
                        listaFamiliaresEditar.add(familiarSeleccionado);
                    }
                }
            } else {
                familiarSeleccionado.setPersonafamiliar(personaSeleccionada);
                if (!listaFamiliaresCrear.contains(familiarSeleccionado)) {
                    if (listaFamiliaresEditar.isEmpty()) {
                        listaFamiliaresEditar.add(familiarSeleccionado);
                    } else if (!listaFamiliaresEditar.contains(familiarSeleccionado)) {
                        listaFamiliaresEditar.add(familiarSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosFamiliares");
        } else if (tipoActualizacion == 1) {
            nuevoFamiliar.setPersonafamiliar(personaSeleccionada);
            nuevoFamiliar.setPersona(personas);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersonaFamiliar");
        } else if (tipoActualizacion == 2) {
            duplicarFamiliares.setPersona(personas);
            duplicarFamiliares.setPersonafamiliar(personaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersonaFamiliar");
        }
        filtrarLovTiposFamiliares = null;
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
        filtrarLovPersonas = null;
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
        filtrarLovTiposDocumentos = null;
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
        filtrarLovTiposDocumentos = null;
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

    public void listaValoresBoton() {
        if (familiarSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 3) {
                habilitarBotonLov();
                contarRegistroTipoFamiliar();
                RequestContext.getCurrentInstance().update("formularioDialogos:tipoFamiliarDialogo");
                RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPODOCUMENTO")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getPersonafamiliar().getTipodocumento().getNombrelargo();
            } else if (tipoNuevo == 2) {
                duplicarFamiliares.getPersonafamiliar().getTipodocumento().getNombrelargo();
            }
        } else if (Campo.equals("CIUDAD")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getPersonafamiliar().getCiudaddocumento().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarFamiliares.getPersonafamiliar().getCiudaddocumento().getNombre();
            }
        } else if (Campo.equals("CIUDADNACIMIENTO")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getPersonafamiliar().getCiudadnacimiento().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarFamiliares.getPersonafamiliar().getCiudadnacimiento().getNombre();
            }
        } else if (Campo.equals("TIPOFAMILIAR")) {
            if (tipoNuevo == 1) {
                nuevoFamiliar.getTipofamiliar().getTipo();
            } else if (tipoNuevo == 2) {
                duplicarFamiliares.getTipofamiliar().getTipo();
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFamiliaresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "FamiliaresPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFamiliaresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "FamiliaresXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void habilitarBotonLov() {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void mostrarDialogoElegirTabla() {
        RequestContext.getCurrentInstance().update("formularioDialogos:seleccionarTablaNewReg");
        RequestContext.getCurrentInstance().execute("PF('seleccionarTablaNewReg').show()");
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void contarRegistroTipoFamiliar() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTiposFamiliares");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
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

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (familiarSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(familiarSeleccionado.getSecuencia().toBigInteger(), "FAMILIARES");
            familiarSeleccionado.getSecuencia();
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
            deshabilitarBotonLov();
        } else if (administrarRastros.verificarHistoricosTabla("FAMILIARES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void recordarSeleccionVD() {
        if (familiarSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosFamiliares");
            tablaC.setSelection(familiarSeleccionado);
        }
    }

    public void crearNuevaPersona() {
//        crearPersonas.add(personas);
        try {
            k++;
            l = BigDecimal.valueOf(k);
            nuevaPersona.setSecuencia(l.toBigInteger());
            System.out.println("nueva persona : "+ nuevaPersona);
            administrarFamiliares.crearPersona(nuevaPersona);
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

    public void activarDatos() {
        if (familiarSeleccionado != null) {
            activardatos = false;
        } else {
            activardatos = true;
        }

        RequestContext.getCurrentInstance().update("form:btneducacion");
        RequestContext.getCurrentInstance().update("form:btntelefono");
        RequestContext.getCurrentInstance().update("form:btndireccion");
    }

    ///////////GETS Y SETS //////
    public List<Familiares> getListaFamiliares() {
        if (listaFamiliares == null) {
            if (personas != null) {
                listaFamiliares = administrarFamiliares.consultarFamiliares(personas.getSecuencia());
            }
        }
        return listaFamiliares;
    }

    public void setListaFamiliares(List<Familiares> listaFamiliares) {
        this.listaFamiliares = listaFamiliares;
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

    public Familiares getNuevoFamiliar() {
        return nuevoFamiliar;
    }

    public void setNuevoFamiliar(Familiares nuevoFamiliar) {
        this.nuevoFamiliar = nuevoFamiliar;
    }

    public Familiares getDuplicarFamiliares() {
        return duplicarFamiliares;
    }

    public void setDuplicarFamiliares(Familiares duplicarFamiliares) {
        this.duplicarFamiliares = duplicarFamiliares;
    }

    public Familiares getEditarFamiliar() {
        return editarFamiliar;
    }

    public void setEditarFamiliar(Familiares editarFamiliar) {
        this.editarFamiliar = editarFamiliar;
    }

    public List<Personas> getLovPersonas() {
        if (lovPersonas == null) {
            lovPersonas = administrarFamiliares.consultarPersonas();
        }
        return lovPersonas;
    }

    public void setLovPersonas(List<Personas> lovPersonas) {
        this.lovPersonas = lovPersonas;
    }

    public List<Personas> getFiltrarLovPersonas() {
        return filtrarLovPersonas;
    }

    public void setFiltrarLovPersonas(List<Personas> filtrarLovPersonas) {
        this.filtrarLovPersonas = filtrarLovPersonas;
    }

    public Personas getPersonaSeleccionada() {
        return personaSeleccionada;
    }

    public void setPersonaSeleccionada(Personas personaSeleccionada) {
        this.personaSeleccionada = personaSeleccionada;
    }

    public List<TiposFamiliares> getLovTiposFamiliares() {
        if (lovTiposFamiliares == null) {
            lovTiposFamiliares = administrarFamiliares.consultarTiposFamiliares();
        }
        return lovTiposFamiliares;
    }

    public void setLovTiposFamiliares(List<TiposFamiliares> lovTiposFamiliares) {
        this.lovTiposFamiliares = lovTiposFamiliares;
    }

    public List<TiposFamiliares> getFiltrarLovTiposFamiliares() {
        return filtrarLovTiposFamiliares;
    }

    public void setFiltrarLovTiposFamiliares(List<TiposFamiliares> filtrarLovTiposFamiliares) {
        this.filtrarLovTiposFamiliares = filtrarLovTiposFamiliares;
    }

    public TiposFamiliares getTipoFamiliarSeleccionado() {
        return tipoFamiliarSeleccionado;
    }

    public void setTipoFamiliarSeleccionado(TiposFamiliares tipoFamiliarSeleccionado) {
        this.tipoFamiliarSeleccionado = tipoFamiliarSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
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

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public Personas getPersonas() {
        return personas;
    }

    public void setPersonas(Personas personas) {
        this.personas = personas;
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

    public String getInfoRegistroCiudades() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCiudadDocumento");
        infoRegistroCiudades = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudades;
    }

    public void setInfoRegistroCiudades(String infoRegistroCiudades) {
        this.infoRegistroCiudades = infoRegistroCiudades;
    }

    public List<TiposDocumentos> getLovTiposDocumentos() {
        if (lovTiposDocumentos == null) {
            lovTiposDocumentos = administrarFamiliares.consultarTiposDocumentos();
        }
        return lovTiposDocumentos;
    }

    public void setLovTiposDocumentos(List<TiposDocumentos> lovTiposDocumentos) {
        this.lovTiposDocumentos = lovTiposDocumentos;
    }

    public List<TiposDocumentos> getFiltrarLovTiposDocumentos() {
        return filtrarLovTiposDocumentos;
    }

    public void setFiltrarLovTiposDocumentos(List<TiposDocumentos> filtrarLovTiposDocumentos) {
        this.filtrarLovTiposDocumentos = filtrarLovTiposDocumentos;
    }

    public TiposDocumentos getTipoDocumentoSeleccionado() {
        return tipoDocumentoSeleccionado;
    }

    public void setTipoDocumentoSeleccionado(TiposDocumentos tipoDocumentoSeleccionado) {
        this.tipoDocumentoSeleccionado = tipoDocumentoSeleccionado;
    }

    public List<Ciudades> getLovCiudades() {
        if (lovCiudades == null) {
            lovCiudades = administrarFamiliares.consultarCiudades();
        }
        return lovCiudades;
    }

    public void setLovCiudades(List<Ciudades> lovCiudades) {
        this.lovCiudades = lovCiudades;
    }

    public List<Ciudades> getFiltrarLovCiudades() {
        return filtrarLovCiudades;
    }

    public void setFiltrarLovCiudades(List<Ciudades> filtrarLovCiudades) {
        this.filtrarLovCiudades = filtrarLovCiudades;
    }

    public Ciudades getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
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

    public boolean isActivardatos() {
        return activardatos;
    }

    public void setActivardatos(boolean activardatos) {
        this.activardatos = activardatos;
    }

    public Personas getNuevaPersona() {
        return nuevaPersona;
    }

    public void setNuevaPersona(Personas nuevaPersona) {
        this.nuevaPersona = nuevaPersona;
    }

}
