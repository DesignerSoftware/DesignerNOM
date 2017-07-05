/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Administrar.AdministrarTiposContratos;
import ControlNavegacion.ControlListaNavegacion;
import Entidades.Contratos;
import Entidades.NormasLaborales;
import Entidades.PlantillasValidaLL;
import Entidades.PlantillasValidaNL;
import Entidades.PlantillasValidaRL;
import Entidades.PlantillasValidaTC;
import Entidades.PlantillasValidaTS;
import Entidades.ReformasLaborales;
import Entidades.TiposContratos;
import Entidades.TiposSueldos;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministrarContratosInterface;
import InterfaceAdministrar.AdministrarNormasLaboralesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarReformasLaboralesInterface;
import InterfaceAdministrar.AdministrarTiposContratosInterface;
import InterfaceAdministrar.AdministrarTiposSueldosInterface;
import InterfaceAdministrar.AdministrarTiposTrabajadoresInterface;
import InterfaceAdministrar.AdministrarTiposTrabajadoresPlantillasInterface;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlTTPlantilla")
@SessionScoped
public class ControlTTPlantilla implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarTiposTrabajadoresPlantillasInterface administrarTTPlantillas;
    @EJB
    AdministrarContratosInterface administrarContratos;
    @EJB
    AdministrarTiposContratosInterface administrarTiposContratos;
    @EJB
    AdministrarTiposSueldosInterface administrarTiposSueldos;
    @EJB
    AdministrarReformasLaboralesInterface administrarReformaLaboral;
    @EJB
    AdministrarNormasLaboralesInterface administrarNormaLaboral;
    @EJB
    AdministrarTiposTrabajadoresInterface administrarTiposTrabajadores;

    //lista TT
    private List<TiposTrabajadores> listaTT;
    private List<TiposTrabajadores> listaTTFiltrar;
    private List<TiposTrabajadores> listaTTCrear;
    private List<TiposTrabajadores> listaTTModificar;
    private List<TiposTrabajadores> listaTTBorrar;
    private TiposTrabajadores ttSeleccionado;
    private TiposTrabajadores nuevoTT;
    private TiposTrabajadores duplicarTT;
    private TiposTrabajadores editarTT;
    //lista TC
    private List<PlantillasValidaTC> listaTC;
    private List<PlantillasValidaTC> listaTCFiltrar;
    private List<PlantillasValidaTC> listaTCCrear;
    private List<PlantillasValidaTC> listaTCModificar;
    private List<PlantillasValidaTC> listaTCBorrar;
    private PlantillasValidaTC plantillaTCSeleccionada;
    private PlantillasValidaTC nuevaPlantillaTC;
    private PlantillasValidaTC duplicarPlantillaTC;
    private PlantillasValidaTC editarPlantillaTC;
    //Lista TS
    private List<PlantillasValidaTS> listaTS;
    private List<PlantillasValidaTS> listaTSFiltrar;
    private List<PlantillasValidaTS> listaTSCrear;
    private List<PlantillasValidaTS> listaTSModificar;
    private List<PlantillasValidaTS> listaTSBorrar;
    private PlantillasValidaTS plantillaTSSeleccionada;
    private PlantillasValidaTS nuevaPlantillaTS;
    private PlantillasValidaTS duplicarPlantillaTS;
    private PlantillasValidaTS editarPlantillaTS;
    //Lista RL
    private List<PlantillasValidaRL> listaRL;
    private List<PlantillasValidaRL> listaRLFiltrar;
    private List<PlantillasValidaRL> listaRLCrear;
    private List<PlantillasValidaRL> listaRLModificar;
    private List<PlantillasValidaRL> listaRLBorrar;
    private PlantillasValidaRL plantillaRLSeleccionada;
    private PlantillasValidaRL nuevaPlantillaRL;
    private PlantillasValidaRL duplicarPlantillaRL;
    private PlantillasValidaRL editarPlantillaRL;
    //Lista LL
    private List<PlantillasValidaLL> listaLL;
    private List<PlantillasValidaLL> listaLLFiltrar;
    private List<PlantillasValidaLL> listaLLCrear;
    private List<PlantillasValidaLL> listaLLModificar;
    private List<PlantillasValidaLL> listaLLBorrar;
    private PlantillasValidaLL plantillaLLSeleccionada;
    private PlantillasValidaLL nuevaPlantillaLL;
    private PlantillasValidaLL duplicarPlantillaLL;
    private PlantillasValidaLL editarPlantillaLL;
    //Lista NL
    private List<PlantillasValidaNL> listaNL;
    private List<PlantillasValidaNL> listaNLFiltrar;
    private List<PlantillasValidaNL> listaNLCrear;
    private List<PlantillasValidaNL> listaNLModificar;
    private List<PlantillasValidaNL> listaNLBorrar;
    private PlantillasValidaNL plantillaNLSeleccionada;
    private PlantillasValidaNL nuevaPlantillaNL;
    private PlantillasValidaNL duplicarPlantillaNL;
    private PlantillasValidaNL editarPlantillaNL;
    //LOV TIPO CONTRATO
    private List<TiposContratos> lovTipoContrato;
    private List<TiposContratos> lovTipoContratoFiltrar;
    private TiposContratos tipoContratoSeleccionado;
    //LOV TIPO SUELDO
    private List<TiposSueldos> lovTipoSueldo;
    private List<TiposSueldos> lovTipoSueldoFiltrar;
    private TiposSueldos tipoSueldoSeleccionado;
    //LOV REFORMA LABORAL
    private List<ReformasLaborales> lovReformaLaboral;
    private List<ReformasLaborales> lovReformaLaboralFiltrar;
    private ReformasLaborales reformaLaboralSeleccionado;
    //LOV NORMA LABORAL
    private List<NormasLaborales> lovNormasLaborales;
    private List<NormasLaborales> lovNormasLaboralesFiltrar;
    private List<NormasLaborales> normaLaboralSeleccionada;
    // LOV CONTRATOS
    private List<Contratos> lovContratos;
    private List<Contratos> lovContratosFiltrar;
    private List<Contratos> contratoSeleccionado;
    //Columnas 
    private Column codigo, tipotrabajador, tipocontrato, tiposueldo, reformalaboral, normalaboral, contrato;
    //Otros
    private int tipoActualizacion;
    private int bandera;
    private boolean aceptar;
    private boolean guardado;
    private BigInteger l;
    private int k, cualTabla;
    private int cualCeldaTT, cualCeldaTC, cualCeldaTS, cualCeldaRL, cualCeldaLL, cualCeldaNL, tipoLista;
    private DataTable tablaC;
    private boolean activarLOV;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String altoTabla, altoTablaTC, altoTablaTS, altoTablaRL, altoTablaLL, altoTablaNL;
    private String infoRegistroTT, infoRegistroTC, infoRegistroTS, infoRegistroRL, infoRegistroLL, infoRegistroNL;
    private String mensajeValidacion;

    public ControlTTPlantilla() {
        nuevoTT = new TiposTrabajadores();
        editarTT = new TiposTrabajadores();
        duplicarTT = new TiposTrabajadores();
        listaTTBorrar = new ArrayList<TiposTrabajadores>();
        listaTTCrear = new ArrayList<TiposTrabajadores>();
        listaTTModificar = new ArrayList<TiposTrabajadores>();
        nuevaPlantillaTC = new PlantillasValidaTC();
        duplicarPlantillaTC = new PlantillasValidaTC();
        editarPlantillaTC = new PlantillasValidaTC();
        listaTCCrear = new ArrayList<PlantillasValidaTC>();
        listaTCBorrar = new ArrayList<PlantillasValidaTC>();
        listaTCModificar = new ArrayList<PlantillasValidaTC>();
        nuevaPlantillaTS = new PlantillasValidaTS();
        duplicarPlantillaTS = new PlantillasValidaTS();
        editarPlantillaTS = new PlantillasValidaTS();
        listaTSCrear = new ArrayList<PlantillasValidaTS>();
        listaTSBorrar = new ArrayList<PlantillasValidaTS>();
        listaTSModificar = new ArrayList<PlantillasValidaTS>();
        nuevaPlantillaRL = new PlantillasValidaRL();
        duplicarPlantillaRL = new PlantillasValidaRL();
        editarPlantillaRL = new PlantillasValidaRL();
        listaRLCrear = new ArrayList<PlantillasValidaRL>();
        listaRLBorrar = new ArrayList<PlantillasValidaRL>();
        listaRLModificar = new ArrayList<PlantillasValidaRL>();
        nuevaPlantillaLL = new PlantillasValidaLL();
        duplicarPlantillaLL = new PlantillasValidaLL();
        editarPlantillaLL = new PlantillasValidaLL();
        listaLLCrear = new ArrayList<PlantillasValidaLL>();
        listaLLBorrar = new ArrayList<PlantillasValidaLL>();
        listaLLModificar = new ArrayList<PlantillasValidaLL>();
        nuevaPlantillaNL = new PlantillasValidaNL();
        duplicarPlantillaNL = new PlantillasValidaNL();
        editarPlantillaNL = new PlantillasValidaNL();
        listaNLCrear = new ArrayList<PlantillasValidaNL>();
        listaNLBorrar = new ArrayList<PlantillasValidaNL>();
        listaNLModificar = new ArrayList<PlantillasValidaNL>();
        lovContratos = null;
        lovNormasLaborales = null;
        lovReformaLaboral = null;
        lovTipoContrato = null;
        lovTipoSueldo = null;
        cualCeldaLL = -1;
        cualCeldaTC = -1;
        cualCeldaTS = -1;
        cualCeldaTT = -1;
        cualCeldaRL = -1;
        cualCeldaNL = -1;
        aceptar = true;
        altoTabla = "120";
        altoTablaTC = "120";
        altoTablaTS = "120";
        altoTablaRL = "120";
        altoTablaLL = "120";
        altoTablaNL = "120";
        k = 0;
        tipoLista = 0;
        guardado = true;
        activarLOV = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        cualTabla = -1;
        mensajeValidacion = "";
    }

    public void limpiarListasValor() {
        lovContratos = null;
        lovNormasLaborales = null;
        lovReformaLaboral = null;
        lovTipoContrato = null;
        lovTipoSueldo = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarContratos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            administrarNormaLaboral.obtenerConexion(ses.getId());
            administrarReformaLaboral.obtenerConexion(ses.getId());
            administrarTTPlantillas.obtenerConexion(ses.getId());
            administrarTiposContratos.obtenerConexion(ses.getId());
            administrarTiposSueldos.obtenerConexion(ses.getId());
            administrarTiposTrabajadores.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        //inicializarCosas(); Inicializar cosas de ser necesario
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
        String pagActual = "tipotrabajadorplantilla";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
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

    public void modificarTTPLantilla(TiposTrabajadores tipot) {
        ttSeleccionado = tipot;
        if (!listaTTCrear.contains(ttSeleccionado)) {
            if (listaTTModificar.isEmpty()) {
                listaTTModificar.add(ttSeleccionado);
            } else if (!listaTTModificar.contains(ttSeleccionado)) {
                listaTTModificar.add(ttSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void modificarPLantillaTC(PlantillasValidaTC plantillatc) {
        plantillaTCSeleccionada = plantillatc;
        if (!listaTCCrear.contains(plantillaTCSeleccionada)) {
            if (listaTCModificar.isEmpty()) {
                listaTCModificar.add(plantillaTCSeleccionada);
            } else if (!listaTCModificar.contains(plantillaTCSeleccionada)) {
                listaTCModificar.add(plantillaTCSeleccionada);
            }
            guardado = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void modificarPLantillaTS(PlantillasValidaTS plantillats) {
        plantillaTSSeleccionada = plantillats;
        if (!listaTSCrear.contains(plantillaTSSeleccionada)) {
            if (listaTSModificar.isEmpty()) {
                listaTSModificar.add(plantillaTSSeleccionada);
            } else if (!listaTSModificar.contains(plantillaTSSeleccionada)) {
                listaTSModificar.add(plantillaTSSeleccionada);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void modificarPLantillaRL(PlantillasValidaRL plantillarl) {
        plantillaRLSeleccionada = plantillarl;
        if (!listaRLCrear.contains(plantillaRLSeleccionada)) {
            if (listaRLModificar.isEmpty()) {
                listaRLModificar.add(plantillaRLSeleccionada);
            } else if (!listaRLModificar.contains(plantillaRLSeleccionada)) {
                listaRLModificar.add(plantillaRLSeleccionada);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void modificarPLantillaLL(PlantillasValidaLL plantillall) {
        plantillaLLSeleccionada = plantillall;
        if (!listaLLCrear.contains(plantillaLLSeleccionada)) {
            if (listaLLModificar.isEmpty()) {
                listaLLModificar.add(plantillaLLSeleccionada);
            } else if (!listaLLModificar.contains(plantillaLLSeleccionada)) {
                listaLLModificar.add(plantillaLLSeleccionada);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void modificarPLantillaNL(PlantillasValidaNL plantillanl) {
        plantillaNLSeleccionada = plantillanl;
        if (!listaNLCrear.contains(plantillaNLSeleccionada)) {
            if (listaNLModificar.isEmpty()) {
                listaNLModificar.add(plantillaNLSeleccionada);
            } else if (!listaNLModificar.contains(plantillaNLSeleccionada)) {
                listaNLModificar.add(plantillaNLSeleccionada);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void cambiarIndiceTT(TiposTrabajadores tipot, int celda) {
        ttSeleccionado = tipot;
        cualCeldaTT = celda;
        cualTabla = 1;
        ttSeleccionado.getSecuencia();
        deshabilitarBotonLov();
        if (cualCeldaTT == 0) {
            ttSeleccionado.getCodigo();
        } else if (cualCeldaTT == 1) {
            ttSeleccionado.getNombre();
        }
        cargarDatos();
    }

    public void cambiarIndiceTC(PlantillasValidaTC plantillatc, int celda) {
        plantillaTCSeleccionada = plantillatc;
        cualCeldaTC = celda;
        cualTabla = 2;
        plantillaTCSeleccionada.getSecuencia();
        if (cualCeldaTC == 0) {
            plantillaTCSeleccionada.getTipocontrato().getNombre();
        }
        habilitarBotonLov();
    }

    public void cambiarIndiceTS(PlantillasValidaTS plantillats, int celda) {
        plantillaTSSeleccionada = plantillats;
        cualCeldaTS = celda;
        cualTabla = 3;
        plantillaTSSeleccionada.getSecuencia();
        if (cualCeldaTS == 0) {
            plantillaTSSeleccionada.getTiposueldo().getDescripcion();
        }
        habilitarBotonLov();
    }

    public void cambiarIndiceRL(PlantillasValidaRL plantillarl, int celda) {
        plantillaRLSeleccionada = plantillarl;
        cualCeldaRL = celda;
        cualTabla = 4;
        plantillaRLSeleccionada.getSecuencia();
        if (cualCeldaRL == 0) {
            plantillaRLSeleccionada.getReformalaboral().getNombre();
        }
        habilitarBotonLov();
    }

    public void cambiarIndiceNL(PlantillasValidaNL plantillanl, int celda) {
        plantillaNLSeleccionada = plantillanl;
        cualCeldaNL = celda;
        cualTabla = 5;
        plantillaNLSeleccionada.getSecuencia();
        if (cualCeldaNL == 0) {
            plantillaNLSeleccionada.getNormalaboral().getNombre();
        }
        habilitarBotonLov();
    }

    public void cambiarIndiceLL(PlantillasValidaLL plantillall, int celda) {
        plantillaLLSeleccionada = plantillall;
        cualCeldaLL = celda;
        cualTabla = 6;
        plantillaLLSeleccionada.getSecuencia();
        if (cualCeldaLL == 0) {
            plantillaLLSeleccionada.getContrato().getDescripcion();
        }
        habilitarBotonLov();
    }

    public void habilitarBotonLov() {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void cargarDatos() {

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
                if (!listaTTBorrar.isEmpty()) {
                    for (int i = 0; i < listaTTBorrar.size(); i++) {
                        administrarTiposTrabajadores.borrarTT(listaTTBorrar.get(i));
                    }
                    listaTTBorrar.clear();
                }

                if (!listaTTCrear.isEmpty()) {
                    for (int i = 0; i < listaTTCrear.size(); i++) {
                        administrarTiposTrabajadores.crearTT(listaTTCrear.get(i));
                    }
                    listaTTCrear.clear();
                }
                if (!listaTTModificar.isEmpty()) {
                    for (int i = 0; i < listaTTModificar.size(); i++) {
                        administrarTiposTrabajadores.editarTT(listaTTModificar.get(i));
                    }
                    listaTTModificar.clear();
                }
                if (!listaTCBorrar.isEmpty()) {
                    for (int i = 0; i < listaTCBorrar.size(); i++) {
                        administrarTTPlantillas.borrarPlantillaTC(listaTCBorrar.get(i));
                    }
                    listaTCBorrar.clear();
                }
                if (!listaTCCrear.isEmpty()) {
                    for (int i = 0; i < listaTCCrear.size(); i++) {
                        administrarTTPlantillas.crearPlantillaTC(listaTCCrear.get(i));
                    }
                    listaTCCrear.clear();
                }
                if (!listaTCModificar.isEmpty()) {
                    for (int i = 0; i < listaTCModificar.size(); i++) {
                        administrarTTPlantillas.editarPlantillaTC(listaTCModificar.get(i));
                    }
                    listaTCModificar.clear();
                }
                if (!listaTSBorrar.isEmpty()) {
                    for (int i = 0; i < listaTSBorrar.size(); i++) {
                        administrarTTPlantillas.borrarPlantillaTS(listaTSBorrar.get(i));
                    }
                    listaTSBorrar.clear();
                }
                if (!listaTSCrear.isEmpty()) {
                    for (int i = 0; i < listaTSCrear.size(); i++) {
                        administrarTTPlantillas.crearPlantillaTS(listaTSCrear.get(i));
                    }
                    listaTSCrear.clear();
                }
                if (!listaTSModificar.isEmpty()) {
                    for (int i = 0; i < listaTSModificar.size(); i++) {
                        administrarTTPlantillas.editarPlantillaTS(listaTSModificar.get(i));
                    }
                    listaTSModificar.clear();
                }
                if (!listaRLBorrar.isEmpty()) {
                    for (int i = 0; i < listaRLBorrar.size(); i++) {
                        administrarTTPlantillas.borrarPlantillaRL(listaRLBorrar.get(i));
                    }
                    listaRLBorrar.clear();
                }
                if (!listaRLCrear.isEmpty()) {
                    for (int i = 0; i < listaRLCrear.size(); i++) {
                        administrarTTPlantillas.crearPlantillaRL(listaRLCrear.get(i));
                    }
                    listaRLCrear.clear();
                }
                if (!listaRLModificar.isEmpty()) {
                    for (int i = 0; i < listaRLModificar.size(); i++) {
                        administrarTTPlantillas.editarPlantillaRL(listaRLModificar.get(i));
                    }
                    listaRLModificar.clear();
                }
                if (!listaLLBorrar.isEmpty()) {
                    for (int i = 0; i < listaLLBorrar.size(); i++) {
                        administrarTTPlantillas.borrarPlantillaLL(listaLLBorrar.get(i));
                    }
                    listaLLBorrar.clear();
                }
                if (!listaLLCrear.isEmpty()) {
                    for (int i = 0; i < listaLLCrear.size(); i++) {
                        administrarTTPlantillas.crearPlantillaLL(listaLLCrear.get(i));
                    }
                    listaLLCrear.clear();
                }
                if (!listaLLModificar.isEmpty()) {
                    for (int i = 0; i < listaLLModificar.size(); i++) {
                        administrarTTPlantillas.editarPlantillaLL(listaLLModificar.get(i));
                    }
                    listaLLModificar.clear();
                }
                if (!listaNLBorrar.isEmpty()) {
                    for (int i = 0; i < listaNLBorrar.size(); i++) {
                        administrarTTPlantillas.borrarPlantillaNL(listaNLBorrar.get(i));
                    }
                    listaNLBorrar.clear();
                }
                if (!listaNLCrear.isEmpty()) {
                    for (int i = 0; i < listaNLCrear.size(); i++) {
                        administrarTTPlantillas.crearPlantillaNL(listaNLCrear.get(i));
                    }
                    listaNLCrear.clear();
                }
                if (!listaNLModificar.isEmpty()) {
                    for (int i = 0; i < listaNLModificar.size(); i++) {
                        administrarTTPlantillas.editarPlantillaNL(listaNLModificar.get(i));
                    }
                    listaNLModificar.clear();
                }
            }
            listaTT = null;
            getListaTT();
            listaTC = null;
            listaTS = null;
            listaRL = null;
            listaLL = null;
            listaNL = null;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistrosLL();
            contarRegistrosNL();
            contarRegistrosRL();
            contarRegistrosTC();
            contarRegistrosTS();
            contarRegistrosTT();
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTT");
            RequestContext.getCurrentInstance().update("form:datosTC");
            RequestContext.getCurrentInstance().update("form:datosTS");
            RequestContext.getCurrentInstance().update("form:datosRL");
            RequestContext.getCurrentInstance().update("form:datosLL");
            RequestContext.getCurrentInstance().update("form:datosNL");
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

            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTT:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            tipotrabajador = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTT:tipotrabajador");
            tipotrabajador.setFilterStyle("display: none; visibility: hidden;");
            tipocontrato = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTC:tipocontrato");
            tipocontrato.setFilterStyle("display: none; visibility: hidden;");
            tiposueldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTS:tiposueldo");
            tiposueldo.setFilterStyle("display: none; visibility: hidden;");
            reformalaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRL:reformalaboral");
            reformalaboral.setFilterStyle("display: none; visibility: hidden;");
            normalaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNL:normalaboral");
            normalaboral.setFilterStyle("display: none; visibility: hidden;");
            contrato = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLL:contrato");
            contrato.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTT");
            RequestContext.getCurrentInstance().update("form:datosTC");
            RequestContext.getCurrentInstance().update("form:datosTS");
            RequestContext.getCurrentInstance().update("form:datosRL");
            RequestContext.getCurrentInstance().update("form:datosLL");
            RequestContext.getCurrentInstance().update("form:datosNL");
            bandera = 0;
            listaLLFiltrar = null;
            listaTTFiltrar = null;
            listaTCFiltrar = null;
            listaTSFiltrar = null;
            listaRLFiltrar = null;
            listaNLFiltrar = null;
            tipoLista = 0;
            altoTabla = "120";
            altoTablaLL = "120";
            altoTablaNL = "120";
            altoTablaRL = "120";
            altoTablaTC = "120";
            altoTablaTS = "120";
        }

        listaLLBorrar.clear();
        listaLLCrear.clear();
        listaLLModificar.clear();
        listaTCBorrar.clear();
        listaTCCrear.clear();
        listaTCModificar.clear();
        listaTSBorrar.clear();
        listaTSCrear.clear();
        listaTSModificar.clear();
        listaRLBorrar.clear();
        listaRLCrear.clear();
        listaRLModificar.clear();
        listaNLBorrar.clear();
        listaNLCrear.clear();
        listaNLModificar.clear();
        listaTTBorrar.clear();
        listaTTCrear.clear();
        listaTTModificar.clear();
        k = 0;
        listaTT = null;
        getListaTT();
        listaTC = null;
        listaTS = null;
        listaRL = null;
        listaLL = null;
        listaNL = null;
        guardado = true;
        deshabilitarBotonLov();
        RequestContext.getCurrentInstance().update("form:datosTT");
        RequestContext.getCurrentInstance().update("form:datosTC");
        RequestContext.getCurrentInstance().update("form:datosTS");
        RequestContext.getCurrentInstance().update("form:datosRL");
        RequestContext.getCurrentInstance().update("form:datosLL");
        RequestContext.getCurrentInstance().update("form:datosNL");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTT:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            tipotrabajador = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTT:tipotrabajador");
            tipotrabajador.setFilterStyle("display: none; visibility: hidden;");
            tipocontrato = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTC:tipocontrato");
            tipocontrato.setFilterStyle("display: none; visibility: hidden;");
            tiposueldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTS:tiposueldo");
            tiposueldo.setFilterStyle("display: none; visibility: hidden;");
            reformalaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRL:reformalaboral");
            reformalaboral.setFilterStyle("display: none; visibility: hidden;");
            normalaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNL:normalaboral");
            normalaboral.setFilterStyle("display: none; visibility: hidden;");
            contrato = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLL:contrato");
            contrato.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTT");
            RequestContext.getCurrentInstance().update("form:datosTC");
            RequestContext.getCurrentInstance().update("form:datosTS");
            RequestContext.getCurrentInstance().update("form:datosRL");
            RequestContext.getCurrentInstance().update("form:datosLL");
            RequestContext.getCurrentInstance().update("form:datosNL");
            bandera = 0;
            listaLLFiltrar = null;
            listaTTFiltrar = null;
            listaTCFiltrar = null;
            listaTSFiltrar = null;
            listaRLFiltrar = null;
            listaNLFiltrar = null;
            tipoLista = 0;
            altoTabla = "120";
            altoTablaLL = "120";
            altoTablaNL = "120";
            altoTablaRL = "120";
            altoTablaTC = "120";
            altoTablaTS = "120";
        }
        listaLLBorrar.clear();
        listaLLCrear.clear();
        listaLLModificar.clear();
        listaTCBorrar.clear();
        listaTCCrear.clear();
        listaTCModificar.clear();
        listaTSBorrar.clear();
        listaTSCrear.clear();
        listaTSModificar.clear();
        listaRLBorrar.clear();
        listaRLCrear.clear();
        listaRLModificar.clear();
        listaNLBorrar.clear();
        listaNLCrear.clear();
        listaNLModificar.clear();
        listaTTBorrar.clear();
        listaTTCrear.clear();
        listaTTModificar.clear();
        k = 0;
        listaTT = null;
        guardado = true;
        navegar("atras");
    }

    public void editarCelda() {
        if (cualTabla == 1) {
            editarTT = ttSeleccionado;
            if (cualCeldaTT == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigo");
                RequestContext.getCurrentInstance().execute("PF('editarCodigo').show()");
                cualCeldaTT = -1;
            } else if (cualCeldaTT == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoTrabajador");
                RequestContext.getCurrentInstance().execute("PF('editarTipoTrabajador').show()");
                cualCeldaTT = -1;
            }
        } else if (cualTabla == 2) {
            editarPlantillaTC = plantillaTCSeleccionada;
            if (cualCeldaTC == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoContrato");
                RequestContext.getCurrentInstance().execute("PF('editarTipoContrato').show()");
                cualCeldaTC = -1;
            }
        } else if (cualTabla == 3) {
            editarPlantillaTS = plantillaTSSeleccionada;
            if (cualCeldaTS == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoSueldo");
                RequestContext.getCurrentInstance().execute("PF('editarTipoSueldo').show()");
                cualCeldaTS = -1;
            }
        } else if (cualTabla == 4) {
            editarPlantillaRL = plantillaRLSeleccionada;
            if (cualCeldaRL == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarReformaLaboral");
                RequestContext.getCurrentInstance().execute("PF('editarReformaLaboral').show()");
                cualCeldaRL = -1;
            }
        } else if (cualTabla == 5) {
            editarPlantillaLL = plantillaLLSeleccionada;
            if (cualCeldaLL == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNormaLaboral");
                RequestContext.getCurrentInstance().execute("PF('editarNormaLaboral').show()");
                cualCeldaLL = -1;
            }
        } else if (cualTabla == 6) {
            editarPlantillaNL = plantillaNLSeleccionada;
            if (cualCeldaNL == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarContrato");
                RequestContext.getCurrentInstance().execute("PF('editarContrato').show()");
                cualCeldaNL = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoTT() {
        RequestContext context = RequestContext.getCurrentInstance();
        int cont = 0;
        Short cod = new Short("0");
        Short n = new Short("null");
        if ((nuevoTT.getCodigo() != cod) && (nuevoTT.getNombre() != null) && (nuevoTT.getCodigo() != n)) {

            for (int j = 0; j < listaTT.size(); j++) {
                if (nuevoTT.getCodigo() == listaTT.get(j).getCodigo()) {
                    cont++;
                }
            }
            if (cont > 0) {
                RequestContext.getCurrentInstance().update("form:validacionNuevo");
                RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
            } else {
                if (bandera == 1) {
                    altoTabla = "120";
                    codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTT:codigo");
                    codigo.setFilterStyle("display: none; visibility: hidden;");
                    tipotrabajador = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTT:tipotrabajador");
                    tipotrabajador.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosTT");
                    bandera = 0;
                    listaTTFiltrar = null;
                    tipoLista = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevoTT.setSecuencia(l);
                listaTTCrear.add(nuevoTT);
                listaTT.add(nuevoTT);
                ttSeleccionado = nuevoTT;
                contarRegistrosTT();
                nuevoTT = new TiposTrabajadores();
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosTT");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTT').hide()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorNewRegNulos').show()");
        }
    }

    public void agregarNuevaTC() {
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (nuevaPlantillaTC.getTipotrabajador() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        for (int i = 0; i < listaTC.size(); i++) {
            if (listaTC.get(i).getTipocontrato() == nuevaPlantillaTC.getTipocontrato()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeTipoContrato");
                RequestContext.getCurrentInstance().execute("PF('existeTipoContrato').show()");
                duplicados++;
            }
        }

        if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoRegistro");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoRegistro').show()");
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                tipocontrato = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTC:tipocontrato");
                tipocontrato.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listaTCFiltrar = null;
                tipoLista = 0;
                altoTablaTC = "120";
                RequestContext.getCurrentInstance().update("form:datosTC");
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaPlantillaTC.setSecuencia(l);
            nuevaPlantillaTC.setTipotrabajador(ttSeleccionado);
            listaTCCrear.add(nuevaPlantillaTC);
            listaTC.add(nuevaPlantillaTC);
            contarRegistrosTC();
            plantillaTCSeleccionada = nuevaPlantillaTC;
            RequestContext.getCurrentInstance().update("form:datosTC");
            guardado = false;
            nuevaPlantillaTC = new PlantillasValidaTC();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPlantillaTC').hide()");
        }
    }

    public void agregarNuevaTS() {
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (nuevaPlantillaTS.getTipotrabajador() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        for (int i = 0; i < listaTS.size(); i++) {
            if (listaTS.get(i).getTiposueldo() == nuevaPlantillaTS.getTiposueldo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeTipoSueldo");
                RequestContext.getCurrentInstance().execute("PF('existeTipoSueldo').show()");
                duplicados++;
            }
        }

        if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoRegistro");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoRegistro').show()");
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                tiposueldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTS:tiposueldo");
                tiposueldo.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listaTSFiltrar = null;
                tipoLista = 0;
                altoTablaTS = "120";
                RequestContext.getCurrentInstance().update("form:datosTS");
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaPlantillaTS.setSecuencia(l);
            nuevaPlantillaTS.setTipotrabajador(ttSeleccionado);
            listaTSCrear.add(nuevaPlantillaTS);
            listaTS.add(nuevaPlantillaTS);
            contarRegistrosTS();
            plantillaTSSeleccionada = nuevaPlantillaTS;
            RequestContext.getCurrentInstance().update("form:datosTS");
            guardado = false;
            nuevaPlantillaTS = new PlantillasValidaTS();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPlantillaTS').hide()");
        }
    }

    public void agregarNuevaRL() {
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (nuevaPlantillaRL.getTipotrabajador() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        for (int i = 0; i < listaRL.size(); i++) {
            if (listaRL.get(i).getReformalaboral() == nuevaPlantillaRL.getReformalaboral()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeReforma");
                RequestContext.getCurrentInstance().execute("PF('existeReforma').show()");
                duplicados++;
            }
        }

        if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoRegistro");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoRegistro').show()");
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                reformalaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRL:reformalaboral");
                reformalaboral.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listaRLFiltrar = null;
                tipoLista = 0;
                altoTablaRL = "120";
                RequestContext.getCurrentInstance().update("form:datosRL");
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaPlantillaRL.setSecuencia(l);
            nuevaPlantillaRL.setTipotrabajador(ttSeleccionado);
            listaRLCrear.add(nuevaPlantillaRL);
            listaRL.add(nuevaPlantillaRL);
            contarRegistrosRL();
            plantillaRLSeleccionada = nuevaPlantillaRL;
            RequestContext.getCurrentInstance().update("form:datosRL");
            guardado = false;
            nuevaPlantillaRL = new PlantillasValidaRL();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPlantillaRL').hide()");
        }
    }

    public void agregarNuevaLL() {
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (nuevaPlantillaLL.getTipotrabajador() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        for (int i = 0; i < listaLL.size(); i++) {
            if (listaLL.get(i).getContrato() == nuevaPlantillaLL.getContrato()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeContrato");
                RequestContext.getCurrentInstance().execute("PF('existeContrato').show()");
                duplicados++;
            }
        }

        if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoRegistro");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoRegistro').show()");
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                contrato = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLL:contrato");
                contrato.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listaLLFiltrar = null;
                tipoLista = 0;
                altoTablaLL = "120";
                RequestContext.getCurrentInstance().update("form:datosLL");
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaPlantillaLL.setSecuencia(l);
            nuevaPlantillaLL.setTipotrabajador(ttSeleccionado);
            listaLLCrear.add(nuevaPlantillaLL);
            listaLL.add(nuevaPlantillaLL);
            contarRegistrosLL();
            plantillaLLSeleccionada = nuevaPlantillaLL;
            RequestContext.getCurrentInstance().update("form:datosLL");
            guardado = false;
            nuevaPlantillaLL = new PlantillasValidaLL();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPlantillaLL').hide()");
        }
    }

    public void agregarNuevaNL() {
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (nuevaPlantillaNL.getTipotrabajador() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        for (int i = 0; i < listaNL.size(); i++) {
            if (listaNL.get(i).getNormalaboral() == nuevaPlantillaNL.getNormalaboral()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeNorma");
                RequestContext.getCurrentInstance().execute("PF('existeNorma').show()");
                duplicados++;
            }
        }

        if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoRegistro");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoRegistro').show()");
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                normalaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNL:normalaboral");
                normalaboral.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listaNLFiltrar = null;
                tipoLista = 0;
                altoTablaNL = "120";
                RequestContext.getCurrentInstance().update("form:datosNL");
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaPlantillaNL.setSecuencia(l);
            nuevaPlantillaNL.setTipotrabajador(ttSeleccionado);
            listaNLCrear.add(nuevaPlantillaNL);
            listaNL.add(nuevaPlantillaNL);
            contarRegistrosNL();
            plantillaNLSeleccionada = nuevaPlantillaNL;
            RequestContext.getCurrentInstance().update("form:datosNL");
            guardado = false;
            nuevaPlantillaNL = new PlantillasValidaNL();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPlantillaNL').hide()");
        }
    }

    public void limpiarNuevaTT() {
        nuevoTT = new TiposTrabajadores();
    }

    public void limpiarNuevaTC() {
        nuevaPlantillaTC = new PlantillasValidaTC();
    }

    public void limpiarNuevaTS() {
        nuevaPlantillaTS = new PlantillasValidaTS();
    }

    public void limpiarNuevaRL() {
        nuevaPlantillaRL = new PlantillasValidaRL();
    }

    public void limpiarNuevaLL() {
        nuevaPlantillaLL = new PlantillasValidaLL();
    }

    public void limpiarNuevaNL() {
        nuevaPlantillaNL = new PlantillasValidaNL();
    }

    public void limpiarDuplicarTT() {
        duplicarTT = new TiposTrabajadores();
    }

    public void limpiarDuplicarTC() {
        duplicarPlantillaTC = new PlantillasValidaTC();
    }

    public void limpiarDuplicarTS() {
        duplicarPlantillaTS = new PlantillasValidaTS();
    }

    public void limpiarDuplicarRL() {
        duplicarPlantillaRL = new PlantillasValidaRL();
    }

    public void limpiarDuplicarLL() {
        duplicarPlantillaLL = new PlantillasValidaLL();
    }

    public void limpiarDuplicarNL() {
        duplicarPlantillaNL = new PlantillasValidaNL();
    }

    public void duplicandoTT() {
        if (ttSeleccionado != null) {
            duplicarTT = new TiposTrabajadores();
            k++;
            l = BigInteger.valueOf(k);

            duplicarTT.setSecuencia(l);
            duplicarTT.setCodigo(ttSeleccionado.getCodigo());
            duplicarTT.setNombre(ttSeleccionado.getNombre());

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTT");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTT').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarTT() {
        int contador = 0;
        for (int i = 0; i < listaTT.size(); i++) {
            if (duplicarTT.getCodigo() == listaTT.get(i).getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeTT");
                RequestContext.getCurrentInstance().execute("PF('existeTT').show()");
                contador++;
            }
        }

        if (contador == 0) {
            listaTT.add(duplicarTT);
            listaTTCrear.add(duplicarTT);
            ttSeleccionado = duplicarTT;
            contarRegistrosTT();
            RequestContext.getCurrentInstance().update("form:datosTT");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTT:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                tipotrabajador = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTT:tipotrabajador");
                tipotrabajador.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listaTTFiltrar = null;
                RequestContext.getCurrentInstance().update("form:datosTT");
                tipoLista = 0;
            }
            duplicarTT = new TiposTrabajadores();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroTT");
        RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTT').hide()");
    }

    public void duplicandoTC() {
        if (plantillaTCSeleccionada != null) {
            duplicarPlantillaTC = new PlantillasValidaTC();
            k++;
            l = BigInteger.valueOf(k);

            duplicarPlantillaTC.setSecuencia(l);
            duplicarPlantillaTC.setTipotrabajador(plantillaTCSeleccionada.getTipotrabajador());
            duplicarPlantillaTC.setTipocontrato(plantillaTCSeleccionada.getTipocontrato());
            altoTablaTC = "120";

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTC");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTC').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarTC() {
        RequestContext context = RequestContext.getCurrentInstance();
        int contador = 0;

        for (int i = 0; i < listaTC.size(); i++) {
            if (duplicarPlantillaTC.getTipocontrato().getSecuencia() == listaTC.get(i).getTipocontrato().getSecuencia()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeTipoContrato");
                RequestContext.getCurrentInstance().execute("PF('existeTipoContrato').show()");
                contador++;
            }
        }

        if (contador == 0) {
            listaTC.add(duplicarPlantillaTC);
            listaTCCrear.add(duplicarPlantillaTC);
            plantillaTCSeleccionada = duplicarPlantillaTC;
            contarRegistrosTC();
            RequestContext.getCurrentInstance().update("form:datosTC");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                tipocontrato = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTC:tipocontrato");
                tipocontrato.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listaTCFiltrar = null;
                RequestContext.getCurrentInstance().update("form:datosTC");
                tipoLista = 0;
            }
            duplicarPlantillaTC = new PlantillasValidaTC();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroTC");
        RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTC').hide()");
    }

    public void duplicandoTS() {
        if (plantillaTSSeleccionada != null) {
            duplicarPlantillaTS = new PlantillasValidaTS();
            k++;
            l = BigInteger.valueOf(k);

            duplicarPlantillaTS.setSecuencia(l);
            duplicarPlantillaTS.setTipotrabajador(plantillaTSSeleccionada.getTipotrabajador());
            duplicarPlantillaTS.setTiposueldo(plantillaTSSeleccionada.getTiposueldo());
            altoTablaTS = "120";

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTS");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTS').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarTS() {
        int contador = 0;

        for (int i = 0; i < listaTS.size(); i++) {
            if (duplicarPlantillaTS.getTiposueldo().getSecuencia() == listaTS.get(i).getTiposueldo().getSecuencia()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeTipoSueldo");
                RequestContext.getCurrentInstance().execute("PF('existeTipoSueldo').show()");
                contador++;
            }
        }

        if (contador == 0) {
            listaTS.add(duplicarPlantillaTS);
            listaTSCrear.add(duplicarPlantillaTS);
            plantillaTSSeleccionada = duplicarPlantillaTS;
            contarRegistrosTS();
            RequestContext.getCurrentInstance().update("form:datosTS");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                tiposueldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTS:tiposueldo");
                tiposueldo.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listaTSFiltrar = null;
                RequestContext.getCurrentInstance().update("form:datosTS");
                tipoLista = 0;
            }
            duplicarPlantillaTS = new PlantillasValidaTS();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroTS");
        RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTS').hide()");
    }

    public void duplicandoRL() {
        if (plantillaRLSeleccionada != null) {
            duplicarPlantillaRL = new PlantillasValidaRL();
            k++;
            l = BigInteger.valueOf(k);

            duplicarPlantillaRL.setSecuencia(l);
            duplicarPlantillaRL.setTipotrabajador(plantillaRLSeleccionada.getTipotrabajador());
            duplicarPlantillaRL.setReformalaboral(plantillaRLSeleccionada.getReformalaboral());
            altoTablaRL = "120";

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRL");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroRL').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarRL() {
        int contador = 0;

        for (int i = 0; i < listaRL.size(); i++) {
            if (duplicarPlantillaRL.getReformalaboral().getSecuencia() == listaRL.get(i).getReformalaboral().getSecuencia()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeReformaLaboral");
                RequestContext.getCurrentInstance().execute("PF('existeReformaLaboral').show()");
                contador++;
            }
        }

        if (contador == 0) {
            listaRL.add(duplicarPlantillaRL);
            listaRLCrear.add(duplicarPlantillaRL);
            plantillaRLSeleccionada = duplicarPlantillaRL;
            contarRegistrosRL();
            RequestContext.getCurrentInstance().update("form:datosRL");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                reformalaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRL:reformalaboral");
                reformalaboral.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listaRLFiltrar = null;
                RequestContext.getCurrentInstance().update("form:datosRL");
                tipoLista = 0;
            }
            duplicarPlantillaRL = new PlantillasValidaRL();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroRL");
        RequestContext.getCurrentInstance().execute("PF('duplicarRegistroRL').hide()");
    }

    public void duplicandoLL() {
        if (plantillaLLSeleccionada != null) {
            duplicarPlantillaLL = new PlantillasValidaLL();
            k++;
            l = BigInteger.valueOf(k);

            duplicarPlantillaLL.setSecuencia(l);
            duplicarPlantillaLL.setTipotrabajador(plantillaLLSeleccionada.getTipotrabajador());
            duplicarPlantillaLL.setContrato(plantillaLLSeleccionada.getContrato());
            altoTablaLL = "120";

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarLL");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroLL').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarLL() {
        int contador = 0;

        for (int i = 0; i < listaLL.size(); i++) {
            if (duplicarPlantillaLL.getContrato().getSecuencia() == listaLL.get(i).getContrato().getSecuencia()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeContrato");
                RequestContext.getCurrentInstance().execute("PF('existeContrato').show()");
                contador++;
            }
        }

        if (contador == 0) {
            listaLL.add(duplicarPlantillaLL);
            listaLLCrear.add(duplicarPlantillaLL);
            plantillaLLSeleccionada = duplicarPlantillaLL;
            contarRegistrosLL();
            RequestContext.getCurrentInstance().update("form:datosLL");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                contrato = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLL:contrato");
                contrato.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listaLLFiltrar = null;
                RequestContext.getCurrentInstance().update("form:datosLL");
                tipoLista = 0;
            }
            duplicarPlantillaLL = new PlantillasValidaLL();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroLL");
        RequestContext.getCurrentInstance().execute("PF('duplicarRegistroLL').hide()");
    }

    public void duplicandoNL() {
        if (plantillaNLSeleccionada != null) {
            duplicarPlantillaNL = new PlantillasValidaNL();
            k++;
            l = BigInteger.valueOf(k);

            duplicarPlantillaNL.setSecuencia(l);
            duplicarPlantillaNL.setTipotrabajador(plantillaNLSeleccionada.getTipotrabajador());
            duplicarPlantillaNL.setNormalaboral(plantillaNLSeleccionada.getNormalaboral());
            altoTablaNL = "120";

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNL");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroNL').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarNL() {
        int contador = 0;

        for (int i = 0; i < listaNL.size(); i++) {
            if (duplicarPlantillaNL.getNormalaboral().getSecuencia() == listaNL.get(i).getNormalaboral().getSecuencia()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeNormaLaboral");
                RequestContext.getCurrentInstance().execute("PF('existeNormaLaboral').show()");
                contador++;
            }
        }

        if (contador == 0) {
            listaNL.add(duplicarPlantillaNL);
            listaNLCrear.add(duplicarPlantillaNL);
            plantillaNLSeleccionada = duplicarPlantillaNL;
            contarRegistrosNL();
            RequestContext.getCurrentInstance().update("form:datosNL");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                normalaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNL:normalaboral");
                normalaboral.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listaNLFiltrar = null;
                RequestContext.getCurrentInstance().update("form:datosNL");
                tipoLista = 0;
            }
            duplicarPlantillaNL = new PlantillasValidaNL();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroLL");
        RequestContext.getCurrentInstance().execute("PF('duplicarRegistroLL').hide()");
    }

    /*
     public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTiposCursos");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposCursos");
         bandera = 0;
         filtrarTiposCursos = null;
         tipoLista = 0;
      }
   }
    */
    
    
//    public void exportPDF() throws IOException {
//      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCursosExportar");
//      FacesContext context = FacesContext.getCurrentInstance();
//      Exporter exporter = new ExportarPDF();
//      exporter.export(context, tabla, "TIPOSCURSOS", false, false, "UTF-8", null, null);
//      context.responseComplete();
//   }
//
//   public void exportXLS() throws IOException {
//      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCursosExportar");
//      FacesContext context = FacesContext.getCurrentInstance();
//      Exporter exporter = new ExportarXLS();
//      exporter.export(context, tabla, "TIPOSCURSOS", false, false, "UTF-8", null, null);
//      context.responseComplete();
//   }
//    
    
    /*
     public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (tipoCursoSeleccionado != null) {
         System.out.println("lol 2");
         int resultado = administrarRastros.obtenerTabla(tipoCursoSeleccionado.getSecuencia(), "TIPOSCURSOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
         System.out.println("resultado: " + resultado);
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSCURSOS")) { // igual acá
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
         System.out.println("ERROR ControlTiposCursos eventoFiltrar ERROR===" + e.getMessage());
      }
   }

    
    */
    
    public void contarRegistrosTT() {

    }

    public void contarRegistrosTC() {

    }

    public void contarRegistrosTS() {

    }

    public void contarRegistrosRL() {

    }

    public void contarRegistrosLL() {

    }

    public void contarRegistrosNL() {

    }

    //////////////////GETS Y SETS///////////////
    public List<TiposTrabajadores> getListaTT() {
        return listaTT;
    }

    public void setListaTT(List<TiposTrabajadores> listaTT) {
        this.listaTT = listaTT;
    }

    public List<TiposTrabajadores> getListaTTFiltrar() {
        return listaTTFiltrar;
    }

    public void setListaTTFiltrar(List<TiposTrabajadores> listaTTFiltrar) {
        this.listaTTFiltrar = listaTTFiltrar;
    }

    public TiposTrabajadores getTtSeleccionado() {
        return ttSeleccionado;
    }

    public void setTtSeleccionado(TiposTrabajadores ttSeleccionado) {
        this.ttSeleccionado = ttSeleccionado;
    }

    public TiposTrabajadores getNuevoTT() {
        return nuevoTT;
    }

    public void setNuevoTT(TiposTrabajadores nuevoTT) {
        this.nuevoTT = nuevoTT;
    }

    public TiposTrabajadores getDuplicarTT() {
        return duplicarTT;
    }

    public void setDuplicarTT(TiposTrabajadores duplicarTT) {
        this.duplicarTT = duplicarTT;
    }

    public TiposTrabajadores getEditarTT() {
        return editarTT;
    }

    public void setEditarTT(TiposTrabajadores editarTT) {
        this.editarTT = editarTT;
    }

    public List<PlantillasValidaTC> getListaTC() {
        return listaTC;
    }

    public void setListaTC(List<PlantillasValidaTC> listaTC) {
        this.listaTC = listaTC;
    }

    public List<PlantillasValidaTC> getListaTCFiltrar() {
        return listaTCFiltrar;
    }

    public void setListaTCFiltrar(List<PlantillasValidaTC> listaTCFiltrar) {
        this.listaTCFiltrar = listaTCFiltrar;
    }

    public PlantillasValidaTC getPlantillaTCSeleccionada() {
        return plantillaTCSeleccionada;
    }

    public void setPlantillaTCSeleccionada(PlantillasValidaTC plantillaTCSeleccionada) {
        this.plantillaTCSeleccionada = plantillaTCSeleccionada;
    }

    public PlantillasValidaTC getNuevaPlantillaTC() {
        return nuevaPlantillaTC;
    }

    public void setNuevaPlantillaTC(PlantillasValidaTC nuevaPlantillaTC) {
        this.nuevaPlantillaTC = nuevaPlantillaTC;
    }

    public PlantillasValidaTC getDuplicarPlantillaTC() {
        return duplicarPlantillaTC;
    }

    public void setDuplicarPlantillaTC(PlantillasValidaTC duplicarPlantillaTC) {
        this.duplicarPlantillaTC = duplicarPlantillaTC;
    }

    public PlantillasValidaTC getEditarPlantillaTC() {
        return editarPlantillaTC;
    }

    public void setEditarPlantillaTC(PlantillasValidaTC editarPlantillaTC) {
        this.editarPlantillaTC = editarPlantillaTC;
    }

    public List<PlantillasValidaTS> getListaTS() {
        return listaTS;
    }

    public void setListaTS(List<PlantillasValidaTS> listaTS) {
        this.listaTS = listaTS;
    }

    public List<PlantillasValidaTS> getListaTSFiltrar() {
        return listaTSFiltrar;
    }

    public void setListaTSFiltrar(List<PlantillasValidaTS> listaTSFiltrar) {
        this.listaTSFiltrar = listaTSFiltrar;
    }

    public PlantillasValidaTS getPlantillaTSSeleccionada() {
        return plantillaTSSeleccionada;
    }

    public void setPlantillaTSSeleccionada(PlantillasValidaTS plantillaTSSeleccionada) {
        this.plantillaTSSeleccionada = plantillaTSSeleccionada;
    }

    public PlantillasValidaTS getNuevaPlantillaTS() {
        return nuevaPlantillaTS;
    }

    public void setNuevaPlantillaTS(PlantillasValidaTS nuevaPlantillaTS) {
        this.nuevaPlantillaTS = nuevaPlantillaTS;
    }

    public PlantillasValidaTS getDuplicarPlantillaTS() {
        return duplicarPlantillaTS;
    }

    public void setDuplicarPlantillaTS(PlantillasValidaTS duplicarPlantillaTS) {
        this.duplicarPlantillaTS = duplicarPlantillaTS;
    }

    public PlantillasValidaTS getEditarPlantillaTS() {
        return editarPlantillaTS;
    }

    public void setEditarPlantillaTS(PlantillasValidaTS editarPlantillaTS) {
        this.editarPlantillaTS = editarPlantillaTS;
    }

    public List<PlantillasValidaRL> getListaRL() {
        return listaRL;
    }

    public void setListaRL(List<PlantillasValidaRL> listaRL) {
        this.listaRL = listaRL;
    }

    public List<PlantillasValidaRL> getListaRLFiltrar() {
        return listaRLFiltrar;
    }

    public void setListaRLFiltrar(List<PlantillasValidaRL> listaRLFiltrar) {
        this.listaRLFiltrar = listaRLFiltrar;
    }

    public PlantillasValidaRL getPlantillaRLSeleccionada() {
        return plantillaRLSeleccionada;
    }

    public void setPlantillaRLSeleccionada(PlantillasValidaRL plantillaRLSeleccionada) {
        this.plantillaRLSeleccionada = plantillaRLSeleccionada;
    }

    public PlantillasValidaRL getNuevaPlantillaRL() {
        return nuevaPlantillaRL;
    }

    public void setNuevaPlantillaRL(PlantillasValidaRL nuevaPlantillaRL) {
        this.nuevaPlantillaRL = nuevaPlantillaRL;
    }

    public PlantillasValidaRL getDuplicarPlantillaRL() {
        return duplicarPlantillaRL;
    }

    public void setDuplicarPlantillaRL(PlantillasValidaRL duplicarPlantillaRL) {
        this.duplicarPlantillaRL = duplicarPlantillaRL;
    }

    public PlantillasValidaRL getEditarPlantillaRL() {
        return editarPlantillaRL;
    }

    public void setEditarPlantillaRL(PlantillasValidaRL editarPlantillaRL) {
        this.editarPlantillaRL = editarPlantillaRL;
    }

    public List<PlantillasValidaLL> getListaLL() {
        return listaLL;
    }

    public void setListaLL(List<PlantillasValidaLL> listaLL) {
        this.listaLL = listaLL;
    }

    public List<PlantillasValidaLL> getListaLLFiltrar() {
        return listaLLFiltrar;
    }

    public void setListaLLFiltrar(List<PlantillasValidaLL> listaLLFiltrar) {
        this.listaLLFiltrar = listaLLFiltrar;
    }

    public PlantillasValidaLL getPlantillaLLSeleccionada() {
        return plantillaLLSeleccionada;
    }

    public void setPlantillaLLSeleccionada(PlantillasValidaLL plantillaLLSeleccionada) {
        this.plantillaLLSeleccionada = plantillaLLSeleccionada;
    }

    public PlantillasValidaLL getNuevaPlantillaLL() {
        return nuevaPlantillaLL;
    }

    public void setNuevaPlantillaLL(PlantillasValidaLL nuevaPlantillaLL) {
        this.nuevaPlantillaLL = nuevaPlantillaLL;
    }

    public PlantillasValidaLL getDuplicarPlantillaLL() {
        return duplicarPlantillaLL;
    }

    public void setDuplicarPlantillaLL(PlantillasValidaLL duplicarPlantillaLL) {
        this.duplicarPlantillaLL = duplicarPlantillaLL;
    }

    public PlantillasValidaLL getEditarPlantillaLL() {
        return editarPlantillaLL;
    }

    public void setEditarPlantillaLL(PlantillasValidaLL editarPlantillaLL) {
        this.editarPlantillaLL = editarPlantillaLL;
    }

    public List<PlantillasValidaNL> getListaNL() {
        return listaNL;
    }

    public void setListaNL(List<PlantillasValidaNL> listaNL) {
        this.listaNL = listaNL;
    }

    public List<PlantillasValidaNL> getListaNLFiltrar() {
        return listaNLFiltrar;
    }

    public void setListaNLFiltrar(List<PlantillasValidaNL> listaNLFiltrar) {
        this.listaNLFiltrar = listaNLFiltrar;
    }

    public PlantillasValidaNL getPlantillaNLSeleccionada() {
        return plantillaNLSeleccionada;
    }

    public void setPlantillaNLSeleccionada(PlantillasValidaNL plantillaNLSeleccionada) {
        this.plantillaNLSeleccionada = plantillaNLSeleccionada;
    }

    public PlantillasValidaNL getNuevaPlantillaNL() {
        return nuevaPlantillaNL;
    }

    public void setNuevaPlantillaNL(PlantillasValidaNL nuevaPlantillaNL) {
        this.nuevaPlantillaNL = nuevaPlantillaNL;
    }

    public PlantillasValidaNL getDuplicarPlantillaNL() {
        return duplicarPlantillaNL;
    }

    public void setDuplicarPlantillaNL(PlantillasValidaNL duplicarPlantillaNL) {
        this.duplicarPlantillaNL = duplicarPlantillaNL;
    }

    public PlantillasValidaNL getEditarPlantillaNL() {
        return editarPlantillaNL;
    }

    public void setEditarPlantillaNL(PlantillasValidaNL editarPlantillaNL) {
        this.editarPlantillaNL = editarPlantillaNL;
    }

    public List<TiposContratos> getLovTipoContrato() {
        return lovTipoContrato;
    }

    public void setLovTipoContrato(List<TiposContratos> lovTipoContrato) {
        this.lovTipoContrato = lovTipoContrato;
    }

    public List<TiposContratos> getLovTipoContratoFiltrar() {
        return lovTipoContratoFiltrar;
    }

    public void setLovTipoContratoFiltrar(List<TiposContratos> lovTipoContratoFiltrar) {
        this.lovTipoContratoFiltrar = lovTipoContratoFiltrar;
    }

    public TiposContratos getTipoContratoSeleccionado() {
        return tipoContratoSeleccionado;
    }

    public void setTipoContratoSeleccionado(TiposContratos tipoContratoSeleccionado) {
        this.tipoContratoSeleccionado = tipoContratoSeleccionado;
    }

    public List<TiposSueldos> getLovTipoSueldo() {
        return lovTipoSueldo;
    }

    public void setLovTipoSueldo(List<TiposSueldos> lovTipoSueldo) {
        this.lovTipoSueldo = lovTipoSueldo;
    }

    public List<TiposSueldos> getLovTipoSueldoFiltrar() {
        return lovTipoSueldoFiltrar;
    }

    public void setLovTipoSueldoFiltrar(List<TiposSueldos> lovTipoSueldoFiltrar) {
        this.lovTipoSueldoFiltrar = lovTipoSueldoFiltrar;
    }

    public TiposSueldos getTipoSueldoSeleccionado() {
        return tipoSueldoSeleccionado;
    }

    public void setTipoSueldoSeleccionado(TiposSueldos tipoSueldoSeleccionado) {
        this.tipoSueldoSeleccionado = tipoSueldoSeleccionado;
    }

    public List<ReformasLaborales> getLovReformaLaboral() {
        return lovReformaLaboral;
    }

    public void setLovReformaLaboral(List<ReformasLaborales> lovReformaLaboral) {
        this.lovReformaLaboral = lovReformaLaboral;
    }

    public List<ReformasLaborales> getLovReformaLaboralFiltrar() {
        return lovReformaLaboralFiltrar;
    }

    public void setLovReformaLaboralFiltrar(List<ReformasLaborales> lovReformaLaboralFiltrar) {
        this.lovReformaLaboralFiltrar = lovReformaLaboralFiltrar;
    }

    public ReformasLaborales getReformaLaboralSeleccionado() {
        return reformaLaboralSeleccionado;
    }

    public void setReformaLaboralSeleccionado(ReformasLaborales reformaLaboralSeleccionado) {
        this.reformaLaboralSeleccionado = reformaLaboralSeleccionado;
    }

    public List<NormasLaborales> getLovNormasLaborales() {
        return lovNormasLaborales;
    }

    public void setLovNormasLaborales(List<NormasLaborales> lovNormasLaborales) {
        this.lovNormasLaborales = lovNormasLaborales;
    }

    public List<NormasLaborales> getLovNormasLaboralesFiltrar() {
        return lovNormasLaboralesFiltrar;
    }

    public void setLovNormasLaboralesFiltrar(List<NormasLaborales> lovNormasLaboralesFiltrar) {
        this.lovNormasLaboralesFiltrar = lovNormasLaboralesFiltrar;
    }

    public List<NormasLaborales> getNormaLaboralSeleccionada() {
        return normaLaboralSeleccionada;
    }

    public void setNormaLaboralSeleccionada(List<NormasLaborales> normaLaboralSeleccionada) {
        this.normaLaboralSeleccionada = normaLaboralSeleccionada;
    }

    public List<Contratos> getLovContratos() {
        return lovContratos;
    }

    public void setLovContratos(List<Contratos> lovContratos) {
        this.lovContratos = lovContratos;
    }

    public List<Contratos> getLovContratosFiltrar() {
        return lovContratosFiltrar;
    }

    public void setLovContratosFiltrar(List<Contratos> lovContratosFiltrar) {
        this.lovContratosFiltrar = lovContratosFiltrar;
    }

    public List<Contratos> getContratoSeleccionado() {
        return contratoSeleccionado;
    }

    public void setContratoSeleccionado(List<Contratos> contratoSeleccionado) {
        this.contratoSeleccionado = contratoSeleccionado;
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

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getAltoTablaTC() {
        return altoTablaTC;
    }

    public void setAltoTablaTC(String altoTablaTC) {
        this.altoTablaTC = altoTablaTC;
    }

    public String getAltoTablaTS() {
        return altoTablaTS;
    }

    public void setAltoTablaTS(String altoTablaTS) {
        this.altoTablaTS = altoTablaTS;
    }

    public String getAltoTablaRL() {
        return altoTablaRL;
    }

    public void setAltoTablaRL(String altoTablaRL) {
        this.altoTablaRL = altoTablaRL;
    }

    public String getAltoTablaLL() {
        return altoTablaLL;
    }

    public void setAltoTablaLL(String altoTablaLL) {
        this.altoTablaLL = altoTablaLL;
    }

    public String getAltoTablaNL() {
        return altoTablaNL;
    }

    public void setAltoTablaNL(String altoTablaNL) {
        this.altoTablaNL = altoTablaNL;
    }

    public String getInfoRegistroTT() {
        return infoRegistroTT;
    }

    public void setInfoRegistroTT(String infoRegistroTT) {
        this.infoRegistroTT = infoRegistroTT;
    }

    public String getInfoRegistroTC() {
        return infoRegistroTC;
    }

    public void setInfoRegistroTC(String infoRegistroTC) {
        this.infoRegistroTC = infoRegistroTC;
    }

    public String getInfoRegistroTS() {
        return infoRegistroTS;
    }

    public void setInfoRegistroTS(String infoRegistroTS) {
        this.infoRegistroTS = infoRegistroTS;
    }

    public String getInfoRegistroRL() {
        return infoRegistroRL;
    }

    public void setInfoRegistroRL(String infoRegistroRL) {
        this.infoRegistroRL = infoRegistroRL;
    }

    public String getInfoRegistroLL() {
        return infoRegistroLL;
    }

    public void setInfoRegistroLL(String infoRegistroLL) {
        this.infoRegistroLL = infoRegistroLL;
    }

    public String getInfoRegistroNL() {
        return infoRegistroNL;
    }

    public void setInfoRegistroNL(String infoRegistroNL) {
        this.infoRegistroNL = infoRegistroNL;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }
}
