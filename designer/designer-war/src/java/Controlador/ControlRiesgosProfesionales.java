/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.RiesgosProfesionales;
import Entidades.TiposCentrosCostos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarRiesgosProfesionalesInterface;
import InterfaceAdministrar.AdministrarTiposCentrosCostosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlRiesgosProfesionales")
@SessionScoped
public class ControlRiesgosProfesionales implements Serializable {

   private static Logger log = Logger.getLogger(ControlRiesgosProfesionales.class);

    @EJB
    AdministrarRiesgosProfesionalesInterface administrarRiesgos;
    @EJB
    AdministrarTiposCentrosCostosInterface administarTiposCC;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<RiesgosProfesionales> listRiesgos;
    private List<RiesgosProfesionales> listRiesgosFiltrar;
    private List<RiesgosProfesionales> listRiesgosCrear;
    private List<RiesgosProfesionales> listRiesgosModificar;
    private List<RiesgosProfesionales> listRiesgosBorrar;
    private RiesgosProfesionales riesgoSeleccionado;
    private RiesgosProfesionales nuevoRiesgo;
    private RiesgosProfesionales duplicarRiesgo;
    private RiesgosProfesionales editarRiesgo;
    //lov
    private List<TiposCentrosCostos> lovTipoCC;
    private List<TiposCentrosCostos> lovTipoCCFiltrar;
    private TiposCentrosCostos tipoCCSeleccionado;
    //Otros
    private int tipoActualizacion;
    private int bandera;
    private Column fechainicial, tipocc, riesgo, comentario;
    private boolean aceptar;
    private boolean guardado;
    private BigInteger l;
    private int k;
    private int cualCelda, tipoLista;
    private String altoTabla;
    private String infoRegistro;
    private String infoRegistroTipoCC;
    private DataTable tablaC;
    private boolean activarLOV;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String mensajeValidacion;

    public ControlRiesgosProfesionales() {
        altoTabla = "305";
        mensajeValidacion = "";
        aceptar = true;
        k = 0;
        cualCelda = -1;
        tipoLista = 0;
        guardado = true;
        listRiesgos = null;
        lovTipoCC = null;
        listRiesgosBorrar = new ArrayList<RiesgosProfesionales>();
        listRiesgosCrear = new ArrayList<RiesgosProfesionales>();
        listRiesgosModificar = new ArrayList<RiesgosProfesionales>();
        editarRiesgo = new RiesgosProfesionales();
        nuevoRiesgo = new RiesgosProfesionales();
        nuevoRiesgo.setTipocentrocosto(new TiposCentrosCostos());
        activarLOV = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {
        lovTipoCC = null;
    }

    @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }
   
   @PostConstruct
    public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administarTiposCC.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            administrarRiesgos.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listRiesgos = null;
        getListRiesgos();
        if (listRiesgos != null) {
            if (!listRiesgos.isEmpty()) {
                riesgoSeleccionado = listRiesgos.get(0);
            }
        }

    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "riesgosprofesionales";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
            //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParaEnviar.put("paginaAnterior", pagActual);
            //mas Parametros
            //         if (pag.equals("rastrotabla")) {
            //           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
        }
        limpiarListasValor();
    }

    public void modificarRiesgo(RiesgosProfesionales riesgo) {
        riesgoSeleccionado = riesgo;
        if (!listRiesgosCrear.contains(riesgoSeleccionado)) {
            if (listRiesgosModificar.isEmpty()) {
                listRiesgosModificar.add(riesgoSeleccionado);
            } else if (!listRiesgosModificar.contains(riesgoSeleccionado)) {
                listRiesgosModificar.add(riesgoSeleccionado);
            }
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cambiarIndice(RiesgosProfesionales riesgo, int celda) {
        riesgoSeleccionado = riesgo;
        cualCelda = celda;
        riesgoSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            deshabilitarBotonLov();
            riesgoSeleccionado.getFechavigencia();
        } else if (cualCelda == 1) {
            habilitarBotonLov();
            riesgoSeleccionado.getTipocentrocosto().getNombre();
        } else if (cualCelda == 2) {
            riesgoSeleccionado.getRiesgo();
            deshabilitarBotonLov();
        } else if (cualCelda == 3) {
            riesgoSeleccionado.getComentario();
            deshabilitarBotonLov();
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
                if (!listRiesgosBorrar.isEmpty()) {
                    administrarRiesgos.borrarRiesgoProfesional(listRiesgosBorrar);
                    listRiesgosBorrar.clear();
                }
                if (!listRiesgosCrear.isEmpty()) {
                    administrarRiesgos.crearRiesgoProfesional(listRiesgosCrear);
                    listRiesgosCrear.clear();
                }
                if (!listRiesgosModificar.isEmpty()) {
                    administrarRiesgos.editarRiesgoProfesional(listRiesgosModificar);
                    listRiesgosModificar.clear();
                }
                listRiesgos = null;
                getListRiesgos();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                riesgoSeleccionado = null;
            }

            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosRiesgos");
            deshabilitarBotonLov();
        } catch (Exception e) {
            log.warn("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {

            fechainicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:fechainicial");
            fechainicial.setFilterStyle("display: none; visibility: hidden;");
            tipocc = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:tipocc");
            tipocc.setFilterStyle("display: none; visibility: hidden;");
            riesgo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:riesgo");
            riesgo.setFilterStyle("display: none; visibility: hidden;");
            comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:comentario");
            comentario.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosRiesgos");
            bandera = 0;
            listRiesgosFiltrar = null;
            tipoLista = 0;
            altoTabla = "305";
        }

        listRiesgosBorrar.clear();
        listRiesgosCrear.clear();
        listRiesgosModificar.clear();
        k = 0;
        listRiesgos = null;
        riesgoSeleccionado = null;
        guardado = true;
        getListRiesgos();
        contarRegistros();
        deshabilitarBotonLov();
        RequestContext.getCurrentInstance().update("form:datosRiesgos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void editarCelda() {

        if (riesgoSeleccionado != null) {
            editarRiesgo = riesgoSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicialD').show()");
                deshabilitarBotonLov();
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoCC");
                RequestContext.getCurrentInstance().execute("PF('editarTipoCC').show()");
                deshabilitarBotonLov();
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarRiesgoProfesional");
                RequestContext.getCurrentInstance().execute("PF('editarRiesgoProfesional').show()");
                habilitarBotonLov();
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarComentario");
                RequestContext.getCurrentInstance().execute("PF('editarComentario').show()");
                deshabilitarBotonLov();
                cualCelda = -1;
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoRiesgo() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";

        if (nuevoRiesgo.getFechavigencia() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        } else if (nuevoRiesgo.getTipocentrocosto() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        } else if (nuevoRiesgo.getRiesgo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        } else if (nuevoRiesgo.getComentario().equals("") || nuevoRiesgo.getComentario().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        for (int i = 0; i < listRiesgos.size(); i++) {
            if ((listRiesgos.get(i).getFechavigencia() == nuevoRiesgo.getFechavigencia())
                    && (listRiesgos.get(i).getTipocentrocosto().getSecuencia() == nuevoRiesgo.getTipocentrocosto().getSecuencia())) {
                duplicados++;
            }
        }
        if (contador == 0) {
            if (duplicados == 0) {

                if (bandera == 1) {
                    altoTabla = "305";
                    //CERRAR FILTRADO
                    fechainicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:fechainicial");
                    fechainicial.setFilterStyle("display: none; visibility: hidden;");
                    tipocc = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:tipocc");
                    tipocc.setFilterStyle("display: none; visibility: hidden;");
                    riesgo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:riesgo");
                    riesgo.setFilterStyle("display: none; visibility: hidden;");
                    comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:comentario");
                    comentario.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosRiesgos");
                    bandera = 0;
                    listRiesgosFiltrar = null;
                    tipoLista = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevoRiesgo.setSecuencia(l);
                listRiesgosCrear.add(nuevoRiesgo);
                listRiesgos.add(nuevoRiesgo);
                riesgoSeleccionado = nuevoRiesgo;
                deshabilitarBotonLov();
                contarRegistros();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosRiesgos");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigencias').hide()");
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                nuevoRiesgo = new RiesgosProfesionales();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeRiesgo");
                RequestContext.getCurrentInstance().execute("PF('existeRiesgo').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoRiesgo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoRiesgo').show()");
        }
    }

    public void limpiarNuevoRiesgo() {
        nuevoRiesgo = new RiesgosProfesionales();

    }

    public void duplicarRiesgoM() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (riesgoSeleccionado != null) {
            duplicarRiesgo = new RiesgosProfesionales();

            duplicarRiesgo.setFechavigencia(riesgoSeleccionado.getFechavigencia());
            duplicarRiesgo.setTipocentrocosto(riesgoSeleccionado.getTipocentrocosto());
            duplicarRiesgo.setRiesgo(riesgoSeleccionado.getRiesgo());
            duplicarRiesgo.setComentario(riesgoSeleccionado.getComentario());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigencias");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigencias').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {

        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";

        if (duplicarRiesgo.getFechavigencia() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        } else if (duplicarRiesgo.getTipocentrocosto() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        } else if (duplicarRiesgo.getRiesgo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        } else if (duplicarRiesgo.getComentario().equals("") || duplicarRiesgo.getComentario().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        for (int i = 0; i < listRiesgos.size(); i++) {
            if ((listRiesgos.get(i).getFechavigencia() == duplicarRiesgo.getFechavigencia())
                    && (listRiesgos.get(i).getTipocentrocosto().getSecuencia() == duplicarRiesgo.getTipocentrocosto().getSecuencia())) {
                duplicados++;
            }
        }
        if (contador == 0) {
            if (duplicados == 0) {
                k++;
                l = BigInteger.valueOf(k);
                duplicarRiesgo.setSecuencia(l);
                listRiesgos.add(duplicarRiesgo);
                listRiesgosCrear.add(duplicarRiesgo);
                riesgoSeleccionado = duplicarRiesgo;
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:datosRiesgos");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigencias').hide()");
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                if (bandera == 1) {
                    altoTabla = "305";
                    fechainicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:fechainicial");
                    fechainicial.setFilterStyle("display: none; visibility: hidden;");
                    tipocc = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:tipocc");
                    tipocc.setFilterStyle("display: none; visibility: hidden;");
                    riesgo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:riesgo");
                    riesgo.setFilterStyle("display: none; visibility: hidden;");
                    comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:comentario");
                    comentario.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosRiesgos");
                    bandera = 0;
                    listRiesgosFiltrar = null;
                    tipoLista = 0;
                }
                duplicarRiesgo = new RiesgosProfesionales();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeRiesgo");
                RequestContext.getCurrentInstance().execute("PF('existeRiesgo').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoRiesgo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoRiesgo').show()");
        }
    }

    public void limpiarDuplicar() {
        duplicarRiesgo = new RiesgosProfesionales();
    }

    public void borrarRiesgo() {
        if (riesgoSeleccionado != null) {
            if (!listRiesgosModificar.isEmpty() && listRiesgosModificar.contains(riesgoSeleccionado)) {
                int modIndex = listRiesgosModificar.indexOf(riesgoSeleccionado);
                listRiesgosModificar.remove(modIndex);
                listRiesgosBorrar.add(riesgoSeleccionado);
            } else if (!listRiesgosCrear.isEmpty() && listRiesgosCrear.contains(riesgoSeleccionado)) {
                int crearIndex = listRiesgosCrear.indexOf(riesgoSeleccionado);
                listRiesgosCrear.remove(crearIndex);
            } else {
                listRiesgosBorrar.add(riesgoSeleccionado);
            }
            listRiesgos.remove(riesgoSeleccionado);
            if (tipoLista == 1) {
                listRiesgosFiltrar.remove(riesgoSeleccionado);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosRiesgos");
            riesgoSeleccionado = null;
            deshabilitarBotonLov();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            altoTabla = "285";
            fechainicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:fechainicial");
            fechainicial.setFilterStyle("width: 85% !important");
            tipocc = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:tipocc");
            tipocc.setFilterStyle("width: 85% !important");
            riesgo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:riesgo");
            riesgo.setFilterStyle("width: 85% !important");
            comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:comentario");
            comentario.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosRiesgos");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "305";
            fechainicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:fechainicial");
            fechainicial.setFilterStyle("display: none; visibility: hidden;");
            tipocc = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:tipocc");
            tipocc.setFilterStyle("display: none; visibility: hidden;");
            riesgo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:riesgo");
            riesgo.setFilterStyle("display: none; visibility: hidden;");
            comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:comentario");
            comentario.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosRiesgos");
            bandera = 0;
            listRiesgosFiltrar = null;
            tipoLista = 0;
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            altoTabla = "305";
            fechainicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:fechainicial");
            fechainicial.setFilterStyle("display: none; visibility: hidden;");
            tipocc = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:tipocc");
            tipocc.setFilterStyle("display: none; visibility: hidden;");
            riesgo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:riesgo");
            riesgo.setFilterStyle("display: none; visibility: hidden;");
            comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRiesgos:comentario");
            comentario.setFilterStyle("display: none; visibility: hidden;");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosRiesgos");
            bandera = 0;
            listRiesgosFiltrar = null;
            tipoLista = 0;
            deshabilitarBotonLov();
        }
        limpiarListasValor();
        listRiesgosBorrar.clear();
        listRiesgosCrear.clear();
        listRiesgosModificar.clear();
        riesgoSeleccionado = null;
        k = 0;
        listRiesgos = null;
        guardado = true;
        navegar("atras");
    }

    public void asignarIndex(RiesgosProfesionales riesgop, int LND, int dlg) {
        riesgoSeleccionado = riesgop;
        tipoActualizacion = LND;
        if (dlg == 1) {
            lovTipoCC = null;
            getLovTipoCC();
            contarRegistrosTCC();
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposCCDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposCCDialogo').show()");
        }
    }

    public void actualizarRiesgo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            riesgoSeleccionado.setTipocentrocosto(tipoCCSeleccionado);
            if (!listRiesgosCrear.contains(riesgoSeleccionado)) {
                if (listRiesgosModificar.isEmpty()) {
                    listRiesgosModificar.add(riesgoSeleccionado);
                } else if (!listRiesgosModificar.contains(riesgoSeleccionado)) {
                    listRiesgosModificar.add(riesgoSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosRiesgos");
        } else if (tipoActualizacion == 1) {
            nuevoRiesgo.setTipocentrocosto(tipoCCSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigencias");
        } else if (tipoActualizacion == 2) {
            duplicarRiesgo.setTipocentrocosto(tipoCCSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigencias");
        }
        listRiesgosFiltrar = null;
        tipoCCSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:tiposCCDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTiposCC");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarD");

        context.reset("formularioDialogos:lovTiposCC:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposCC').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposCCDialogo').hide()");
    }

    public void cancelarCambioRiesgo() {
        listRiesgosFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        tipoCCSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovTiposCC:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposCC').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposCCDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposCCDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTiposCC");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarD");
    }

    public void listaValoresBoton() {
        if (riesgoSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 1) {
                lovTipoCC = null;
                getLovTipoCC();
                contarRegistrosTCC();
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposCCDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposCCDialogo').show()");
                tipoActualizacion = 0;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosRiesgosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "RiesgosLaboralesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosRiesgosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "RiesgosLaboralesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }
    //EVENTO FILTRAR

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (riesgoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(riesgoSeleccionado.getSecuencia(), "RIESGOSLABORALES");
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
        } else if (administrarRastros.verificarHistoricosTabla("RIESGOSLABORALES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void contarRegistrosTCC() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTCC");
    }

    public void habilitarBotonLov() {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    ///////////////////SETS Y GETS//////////////
    public List<RiesgosProfesionales> getListRiesgos() {
        if (listRiesgos == null) {
            listRiesgos = administrarRiesgos.listRiesgoProfesional();
        }
        return listRiesgos;
    }

    public void setListRiesgos(List<RiesgosProfesionales> listRiesgos) {
        this.listRiesgos = listRiesgos;
    }

    public List<RiesgosProfesionales> getListRiesgosFiltrar() {
        return listRiesgosFiltrar;
    }

    public void setListRiesgosFiltrar(List<RiesgosProfesionales> listRiesgosFiltrar) {
        this.listRiesgosFiltrar = listRiesgosFiltrar;
    }

    public RiesgosProfesionales getRiesgoSeleccionado() {
        return riesgoSeleccionado;
    }

    public void setRiesgoSeleccionado(RiesgosProfesionales riesgoSeleccionado) {
        this.riesgoSeleccionado = riesgoSeleccionado;
    }

    public RiesgosProfesionales getNuevoRiesgo() {
        return nuevoRiesgo;
    }

    public void setNuevoRiesgo(RiesgosProfesionales nuevoRiesgo) {
        this.nuevoRiesgo = nuevoRiesgo;
    }

    public RiesgosProfesionales getDuplicarRiesgo() {
        return duplicarRiesgo;
    }

    public void setDuplicarRiesgo(RiesgosProfesionales duplicarRiesgo) {
        this.duplicarRiesgo = duplicarRiesgo;
    }

    public RiesgosProfesionales getEditarRiesgo() {
        return editarRiesgo;
    }

    public void setEditarRiesgo(RiesgosProfesionales editarRiesgo) {
        this.editarRiesgo = editarRiesgo;
    }

    public List<TiposCentrosCostos> getLovTipoCC() {
        if (lovTipoCC == null) {
            lovTipoCC = administarTiposCC.consultarTiposCentrosCostos();
        }
        return lovTipoCC;
    }

    public void setLovTipoCC(List<TiposCentrosCostos> lovTipoCC) {
        this.lovTipoCC = lovTipoCC;
    }

    public List<TiposCentrosCostos> getLovTipoCCFiltrar() {
        return lovTipoCCFiltrar;
    }

    public void setLovTipoCCFiltrar(List<TiposCentrosCostos> lovTipoCCFiltrar) {
        this.lovTipoCCFiltrar = lovTipoCCFiltrar;
    }

    public TiposCentrosCostos getTipoCCSeleccionado() {
        return tipoCCSeleccionado;
    }

    public void setTipoCCSeleccionado(TiposCentrosCostos tipoCCSeleccionado) {
        this.tipoCCSeleccionado = tipoCCSeleccionado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosRiesgos");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroTipoCC() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTiposCC");
        infoRegistroTipoCC = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoCC;
    }

    public void setInfoRegistroTipoCC(String infoRegistroTipoCC) {
        this.infoRegistroTipoCC = infoRegistroTipoCC;
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
}
