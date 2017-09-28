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
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarContratosInterface;
import InterfaceAdministrar.AdministrarNormasLaboralesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarReformasLaboralesInterface;
import InterfaceAdministrar.AdministrarTiposContratosInterface;
import InterfaceAdministrar.AdministrarTiposSueldosInterface;
import InterfaceAdministrar.AdministrarTiposTrabajadoresInterface;
import InterfaceAdministrar.AdministrarTiposTrabajadoresPlantillasInterface;
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
@Named(value = "controlTTPlantilla")
@SessionScoped
public class ControlTTPlantilla implements Serializable {

    private static Logger log = Logger.getLogger(ControlTTPlantilla.class);

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
    private NormasLaborales normaLaboralSeleccionada;
    // LOV CONTRATOS
    private List<Contratos> lovContratos;
    private List<Contratos> lovContratosFiltrar;
    private Contratos contratoSeleccionado;
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
    private String infoRegistroLovTC, infoRegistroLovTS, infoRegistroLovRL, infoRegistroLovLL, infoRegistroLovNL;
    private String mensajeValidacion;
    private String nombreArchivo, tablaImprimir;

    public ControlTTPlantilla() {
        editarTT = new TiposTrabajadores();
        duplicarTT = new TiposTrabajadores();
        listaTTBorrar = new ArrayList<TiposTrabajadores>();
        listaTTCrear = new ArrayList<TiposTrabajadores>();
        listaTTModificar = new ArrayList<TiposTrabajadores>();
        nuevaPlantillaTC = new PlantillasValidaTC();
        nuevaPlantillaTC.setTipocontrato(new TiposContratos());
        duplicarPlantillaTC = new PlantillasValidaTC();
        editarPlantillaTC = new PlantillasValidaTC();
        listaTCCrear = new ArrayList<PlantillasValidaTC>();
        listaTCBorrar = new ArrayList<PlantillasValidaTC>();
        listaTCModificar = new ArrayList<PlantillasValidaTC>();
        nuevaPlantillaTS = new PlantillasValidaTS();
        nuevaPlantillaTS.setTiposueldo(new TiposSueldos());
        duplicarPlantillaTS = new PlantillasValidaTS();
        editarPlantillaTS = new PlantillasValidaTS();
        listaTSCrear = new ArrayList<PlantillasValidaTS>();
        listaTSBorrar = new ArrayList<PlantillasValidaTS>();
        listaTSModificar = new ArrayList<PlantillasValidaTS>();
        nuevaPlantillaRL = new PlantillasValidaRL();
        nuevaPlantillaRL.setReformalaboral(new ReformasLaborales());
        duplicarPlantillaRL = new PlantillasValidaRL();
        editarPlantillaRL = new PlantillasValidaRL();
        listaRLCrear = new ArrayList<PlantillasValidaRL>();
        listaRLBorrar = new ArrayList<PlantillasValidaRL>();
        listaRLModificar = new ArrayList<PlantillasValidaRL>();
        nuevaPlantillaLL = new PlantillasValidaLL();
        nuevaPlantillaLL.setContrato(new Contratos());
        duplicarPlantillaLL = new PlantillasValidaLL();
        editarPlantillaLL = new PlantillasValidaLL();
        listaLLCrear = new ArrayList<PlantillasValidaLL>();
        listaLLBorrar = new ArrayList<PlantillasValidaLL>();
        listaLLModificar = new ArrayList<PlantillasValidaLL>();
        nuevaPlantillaNL = new PlantillasValidaNL();
        nuevaPlantillaNL.setNormalaboral(new NormasLaborales());
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
        altoTabla = "60";
        altoTablaTC = "60";
        altoTablaTS = "60";
        altoTablaRL = "60";
        altoTablaLL = "60";
        altoTablaNL = "60";
        k = 0;
        tipoLista = 0;
        guardado = true;
        activarLOV = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        cualTabla = -1;
        mensajeValidacion = "";
        nombreArchivo = "";
        tablaImprimir = "";
        nuevoTT = new TiposTrabajadores();
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
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaTT = null;
        getListaTT();
        if (listaTT != null) {
            if (!listaTT.isEmpty()) {
                ttSeleccionado = listaTT.get(0);
            }
        }
        cargarDatos();
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "tipotrabajadorplantilla";
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
        contarRegistrosTS();
        contarRegistrosTC();
        contarRegistrosLL();
        contarRegistrosRL();
        contarRegistrosNL();
        RequestContext.getCurrentInstance().update("form:datosTT");
        RequestContext.getCurrentInstance().update("form:datosTC");
        RequestContext.getCurrentInstance().update("form:datosTS");
        RequestContext.getCurrentInstance().update("form:datosRL");
        RequestContext.getCurrentInstance().update("form:datosLL");
        RequestContext.getCurrentInstance().update("form:datosNL");
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
        if (ttSeleccionado != null) {
            listaTC = null;
            listaTS = null;
            listaRL = null;
            listaLL = null;
            listaNL = null;
            listaTS = administrarTTPlantillas.listaPlantillaTS(ttSeleccionado.getSecuencia());
            listaRL = administrarTTPlantillas.listaPlantillaRL(ttSeleccionado.getSecuencia());
            listaLL = administrarTTPlantillas.listaPlantillaLL(ttSeleccionado.getSecuencia());
            listaNL = administrarTTPlantillas.listaPlantillaNL(ttSeleccionado.getSecuencia());
            listaTC = administrarTTPlantillas.listaPlantillaTC(ttSeleccionado.getSecuencia());
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
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
            cargarDatos();
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
            RequestContext.getCurrentInstance().update("form:datosTT");
            RequestContext.getCurrentInstance().update("form:datosTC");
            RequestContext.getCurrentInstance().update("form:datosTS");
            RequestContext.getCurrentInstance().update("form:datosRL");
            RequestContext.getCurrentInstance().update("form:datosLL");
            RequestContext.getCurrentInstance().update("form:datosNL");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
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
            altoTabla = "60";
            altoTablaLL = "60";
            altoTablaNL = "60";
            altoTablaRL = "60";
            altoTablaTC = "60";
            altoTablaTS = "60";
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
            altoTabla = "60";
            altoTablaLL = "60";
            altoTablaNL = "60";
            altoTablaRL = "60";
            altoTablaTC = "60";
            altoTablaTS = "60";
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
        listaTC = null;
        listaTS = null;
        listaRL = null;
        listaNL = null;
        listaLL = null;
        ttSeleccionado = null;
        plantillaLLSeleccionada = null;
        plantillaRLSeleccionada = null;
        plantillaNLSeleccionada = null;
        plantillaTCSeleccionada = null;
        plantillaTSSeleccionada = null;
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
            editarPlantillaNL = plantillaNLSeleccionada;
            if (cualCeldaNL == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNormaLaboral");
                RequestContext.getCurrentInstance().execute("PF('editarNormaLaboral').show()");
                cualCeldaNL = -1;
            }
        } else if (cualTabla == 6) {
            editarPlantillaLL = plantillaLLSeleccionada;
            if (cualCeldaLL == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarContrato");
                RequestContext.getCurrentInstance().execute("PF('editarContrato').show()");
                cualCeldaLL = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoTT() {
        RequestContext context = RequestContext.getCurrentInstance();
        int cont = 0;
        Short cod = 0;
        Short n = null;
        if ((nuevoTT.getCodigo() != 0) && (nuevoTT.getNombre() != null) && (nuevoTT.getCodigo() != n)) {

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
                    altoTabla = "60";
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
        if (ttSeleccionado != null) {
            int contador = 0;
            int duplicados = 0;
            RequestContext context = RequestContext.getCurrentInstance();
            mensajeValidacion = " ";

            if (nuevaPlantillaTC.getTipocontrato().getNombre().equals(null) || nuevaPlantillaTC.getTipotrabajador().getNombre().equals("")) {
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
                    altoTablaTC = "60";
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
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccioneTT').show()");
        }
    }

    public void agregarNuevaTS() {
        if (ttSeleccionado != null) {
            int contador = 0;
            int duplicados = 0;
            RequestContext context = RequestContext.getCurrentInstance();
            mensajeValidacion = " ";

            if (nuevaPlantillaTS.getTiposueldo().getDescripcion() == null || nuevaPlantillaTS.getTiposueldo().getDescripcion().equals("")) {
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
                    altoTablaTS = "60";
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
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccioneTT').show()");
        }
    }

    public void agregarNuevaRL() {
        if (ttSeleccionado != null) {

            int contador = 0;
            int duplicados = 0;
            RequestContext context = RequestContext.getCurrentInstance();
            mensajeValidacion = " ";

            if (nuevaPlantillaRL.getReformalaboral().getNombre() == null || nuevaPlantillaRL.getReformalaboral().getNombre().equals("")) {
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
                    altoTablaRL = "60";
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
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccioneTT').show()");
        }
    }

    public void agregarNuevaLL() {
        if (ttSeleccionado != null) {

            int contador = 0;
            int duplicados = 0;
            RequestContext context = RequestContext.getCurrentInstance();
            mensajeValidacion = " ";

            if (nuevaPlantillaLL.getContrato().getDescripcion() == null || nuevaPlantillaLL.getContrato().getDescripcion().equals("")) {
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
                    altoTablaLL = "60";
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
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccioneTT').show()");
        }
    }

    public void agregarNuevaNL() {
        if (ttSeleccionado != null) {

            int contador = 0;
            int duplicados = 0;
            RequestContext context = RequestContext.getCurrentInstance();
            mensajeValidacion = " ";

            if (nuevaPlantillaNL.getNormalaboral().getNombre() == null || nuevaPlantillaNL.getNormalaboral().getNombre().equals("")) {
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
                    altoTablaNL = "60";
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
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccioneTT').show()");
        }
    }

    public void limpiarNuevaTT() {
        nuevoTT = new TiposTrabajadores();
    }

    public void limpiarNuevaTC() {
        nuevaPlantillaTC = new PlantillasValidaTC();
        nuevaPlantillaTC.setTipocontrato(new TiposContratos());
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

    public void seleccionarDuplicar() {
        if (cualTabla == 1) {
            duplicandoTT();
        } else if (cualTabla == 2) {
            duplicandoTC();
        } else if (cualTabla == 3) {
            duplicandoTS();
        } else if (cualTabla == 4) {
            duplicandoRL();
        } else if (cualTabla == 5) {
            duplicandoNL();
        } else if (cualTabla == 6) {
            duplicandoLL();
        }
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
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroTT");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTT').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeTT");
            RequestContext.getCurrentInstance().execute("PF('existeTT').show()");

        }
    }

    public void duplicandoTC() {
        if (plantillaTCSeleccionada != null) {
            duplicarPlantillaTC = new PlantillasValidaTC();
            k++;
            l = BigInteger.valueOf(k);

            duplicarPlantillaTC.setSecuencia(l);
            duplicarPlantillaTC.setTipotrabajador(plantillaTCSeleccionada.getTipotrabajador());
            duplicarPlantillaTC.setTipocontrato(plantillaTCSeleccionada.getTipocontrato());
            altoTablaTC = "60";

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTC");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPlantillaTC').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarTC() {
        RequestContext context = RequestContext.getCurrentInstance();
        int contador = 0;

        for (int i = 0; i < listaTC.size(); i++) {
            if (duplicarPlantillaTC.getTipocontrato().getSecuencia() == listaTC.get(i).getTipocontrato().getSecuencia()) {
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
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroPlantillaTC");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPlantillaTC').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeTipoContrato");
            RequestContext.getCurrentInstance().execute("PF('existeTipoContrato').show()");
        }
    }

    public void duplicandoTS() {
        if (plantillaTSSeleccionada != null) {
            duplicarPlantillaTS = new PlantillasValidaTS();
            k++;
            l = BigInteger.valueOf(k);

            duplicarPlantillaTS.setSecuencia(l);
            duplicarPlantillaTS.setTipotrabajador(plantillaTSSeleccionada.getTipotrabajador());
            duplicarPlantillaTS.setTiposueldo(plantillaTSSeleccionada.getTiposueldo());
            altoTablaTS = "60";

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTS");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPlantillaTS').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarTS() {
        int contador = 0;

        for (int i = 0; i < listaTS.size(); i++) {
            if (duplicarPlantillaTS.getTiposueldo().getSecuencia() == listaTS.get(i).getTiposueldo().getSecuencia()) {
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
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroPlantillaTS");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPlantillaTS').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeTipoSueldo");
            RequestContext.getCurrentInstance().execute("PF('existeTipoSueldo').show()");
        }
    }

    public void duplicandoRL() {
        if (plantillaRLSeleccionada != null) {
            duplicarPlantillaRL = new PlantillasValidaRL();
            k++;
            l = BigInteger.valueOf(k);

            duplicarPlantillaRL.setSecuencia(l);
            duplicarPlantillaRL.setTipotrabajador(plantillaRLSeleccionada.getTipotrabajador());
            duplicarPlantillaRL.setReformalaboral(plantillaRLSeleccionada.getReformalaboral());
            altoTablaRL = "60";

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRL");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPlantillaRL').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarRL() {
        int contador = 0;

        for (int i = 0; i < listaRL.size(); i++) {
            if (duplicarPlantillaRL.getReformalaboral().getSecuencia() == listaRL.get(i).getReformalaboral().getSecuencia()) {
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
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroPlantillaRL");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPlantillaRL').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeReformaLaboral");
            RequestContext.getCurrentInstance().execute("PF('existeReformaLaboral').show()");
        }
    }

    public void duplicandoLL() {
        if (plantillaLLSeleccionada != null) {
            duplicarPlantillaLL = new PlantillasValidaLL();
            k++;
            l = BigInteger.valueOf(k);

            duplicarPlantillaLL.setSecuencia(l);
            duplicarPlantillaLL.setTipotrabajador(plantillaLLSeleccionada.getTipotrabajador());
            duplicarPlantillaLL.setContrato(plantillaLLSeleccionada.getContrato());
            altoTablaLL = "60";

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarLL");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPlantillaLL').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarLL() {
        int contador = 0;

        for (int i = 0; i < listaLL.size(); i++) {
            if (duplicarPlantillaLL.getContrato().getSecuencia() == listaLL.get(i).getContrato().getSecuencia()) {
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
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroPlantillaLL");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPlantillaLL').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeContrato");
            RequestContext.getCurrentInstance().execute("PF('existeContrato').show()");
        }
    }

    public void duplicandoNL() {
        if (plantillaNLSeleccionada != null) {
            duplicarPlantillaNL = new PlantillasValidaNL();
            k++;
            l = BigInteger.valueOf(k);

            duplicarPlantillaNL.setSecuencia(l);
            duplicarPlantillaNL.setTipotrabajador(plantillaNLSeleccionada.getTipotrabajador());
            duplicarPlantillaNL.setNormalaboral(plantillaNLSeleccionada.getNormalaboral());
            altoTablaNL = "60";

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNL");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPlantillaNL').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarNL() {
        int contador = 0;

        for (int i = 0; i < listaNL.size(); i++) {
            if (duplicarPlantillaNL.getNormalaboral().getSecuencia() == listaNL.get(i).getNormalaboral().getSecuencia()) {
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
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroPlantillaLL");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPlantillaLL').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeNormaLaboral");
            RequestContext.getCurrentInstance().execute("PF('existeNormaLaboral').show()");
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTT:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            tipotrabajador = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTT:tipotrabajador");
            tipotrabajador.setFilterStyle("width: 85% !important;");
            tipocontrato = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTC:tipocontrato");
            tipocontrato.setFilterStyle("width: 85% !important;");
            tiposueldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTS:tiposueldo");
            tiposueldo.setFilterStyle("width: 85% !important;");
            reformalaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRL:reformalaboral");
            reformalaboral.setFilterStyle("width: 85% !important;");
            normalaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNL:normalaboral");
            normalaboral.setFilterStyle("width: 85% !important;");
            contrato = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLL:contrato");
            contrato.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTT");
            RequestContext.getCurrentInstance().update("form:datosTC");
            RequestContext.getCurrentInstance().update("form:datosTS");
            RequestContext.getCurrentInstance().update("form:datosRL");
            RequestContext.getCurrentInstance().update("form:datosLL");
            RequestContext.getCurrentInstance().update("form:datosNL");
            bandera = 1;
            altoTabla = "40";
            altoTablaLL = "40";
            altoTablaNL = "40";
            altoTablaRL = "40";
            altoTablaTC = "40";
            altoTablaTS = "40";
        } else if (bandera == 1) {
            log.info("Desactivar");
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
            RequestContext.getCurrentInstance().execute("PF('datosTT').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('datosTS').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('datosTC').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('datosRL').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('datosLL').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('datosNL').clearFilters()");
            bandera = 0;
            listaLLFiltrar = null;
            listaTTFiltrar = null;
            listaTCFiltrar = null;
            listaTSFiltrar = null;
            listaRLFiltrar = null;
            listaNLFiltrar = null;
            tipoLista = 0;
            altoTabla = "60";
            altoTablaLL = "60";
            altoTablaNL = "60";
            altoTablaRL = "60";
            altoTablaTC = "60";
            altoTablaTS = "60";
            tipoLista = 0;
        }
    }

    public void borrarTT() {
        if (ttSeleccionado != null) {
            if (!listaTTModificar.isEmpty() && listaTTModificar.contains(ttSeleccionado)) {
                listaTTModificar.remove(listaTTModificar.indexOf(ttSeleccionado));
                listaTTBorrar.add(ttSeleccionado);
            } else if (!listaTTCrear.isEmpty() && listaTTCrear.contains(ttSeleccionado)) {
                listaTTCrear.remove(listaTTCrear.indexOf(ttSeleccionado));
            } else {
                listaTTBorrar.add(ttSeleccionado);
            }
            listaTT.remove(ttSeleccionado);
            if (tipoLista == 1) {
                listaTTFiltrar.remove(ttSeleccionado);
            }
            RequestContext.getCurrentInstance().update("form:datosTT");
            contarRegistrosTT();
            ttSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrarTC() {
        if (plantillaTCSeleccionada != null) {
            if (!listaTCModificar.isEmpty() && listaTCModificar.contains(plantillaTCSeleccionada)) {
                listaTCModificar.remove(listaTCModificar.indexOf(plantillaTCSeleccionada));
                listaTCBorrar.add(plantillaTCSeleccionada);
            } else if (!listaTCCrear.isEmpty() && listaTCCrear.contains(plantillaTCSeleccionada)) {
                listaTCCrear.remove(listaTCCrear.indexOf(plantillaTCSeleccionada));
            } else {
                listaTCBorrar.add(plantillaTCSeleccionada);
            }
            listaTC.remove(plantillaTCSeleccionada);
            if (tipoLista == 1) {
                listaTCFiltrar.remove(plantillaTCSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosTC");
            contarRegistrosTC();
            plantillaTCSeleccionada = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrarTS() {
        if (plantillaTSSeleccionada != null) {
            if (!listaTSModificar.isEmpty() && listaTSModificar.contains(plantillaTSSeleccionada)) {
                listaTSModificar.remove(listaTSModificar.indexOf(plantillaTSSeleccionada));
                listaTSBorrar.add(plantillaTSSeleccionada);
            } else if (!listaTSCrear.isEmpty() && listaTSCrear.contains(plantillaTSSeleccionada)) {
                listaTSCrear.remove(listaTSCrear.indexOf(plantillaTSSeleccionada));
            } else {
                listaTSBorrar.add(plantillaTSSeleccionada);
            }
            listaTS.remove(plantillaTSSeleccionada);
            if (tipoLista == 1) {
                listaTSFiltrar.remove(plantillaTSSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosTS");
            contarRegistrosTS();
            plantillaTSSeleccionada = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrarRL() {
        if (plantillaRLSeleccionada != null) {
            if (!listaRLModificar.isEmpty() && listaRLModificar.contains(plantillaRLSeleccionada)) {
                listaRLModificar.remove(listaRLModificar.indexOf(plantillaRLSeleccionada));
                listaRLBorrar.add(plantillaRLSeleccionada);
            } else if (!listaRLCrear.isEmpty() && listaRLCrear.contains(plantillaRLSeleccionada)) {
                listaRLCrear.remove(listaRLCrear.indexOf(plantillaRLSeleccionada));
            } else {
                listaRLBorrar.add(plantillaRLSeleccionada);
            }
            listaRL.remove(plantillaRLSeleccionada);
            if (tipoLista == 1) {
                listaRLFiltrar.remove(plantillaRLSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosRL");
            contarRegistrosRL();
            plantillaRLSeleccionada = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrarLL() {
        if (plantillaLLSeleccionada != null) {
            if (!listaLLModificar.isEmpty() && listaLLModificar.contains(plantillaLLSeleccionada)) {
                listaLLModificar.remove(listaLLModificar.indexOf(plantillaLLSeleccionada));
                listaLLBorrar.add(plantillaLLSeleccionada);
            } else if (!listaLLCrear.isEmpty() && listaLLCrear.contains(plantillaLLSeleccionada)) {
                listaLLCrear.remove(listaLLCrear.indexOf(plantillaLLSeleccionada));
            } else {
                listaLLBorrar.add(plantillaLLSeleccionada);
            }
            listaLL.remove(plantillaLLSeleccionada);
            if (tipoLista == 1) {
                listaLLFiltrar.remove(plantillaLLSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosLL");
            contarRegistrosLL();
            plantillaLLSeleccionada = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrarNL() {
        if (plantillaNLSeleccionada != null) {
            if (!listaNLModificar.isEmpty() && listaNLModificar.contains(plantillaNLSeleccionada)) {
                listaNLModificar.remove(listaNLModificar.indexOf(plantillaNLSeleccionada));
                listaNLBorrar.add(plantillaNLSeleccionada);
            } else if (!listaNLCrear.isEmpty() && listaNLCrear.contains(plantillaNLSeleccionada)) {
                listaNLCrear.remove(listaNLCrear.indexOf(plantillaNLSeleccionada));
            } else {
                listaNLBorrar.add(plantillaNLSeleccionada);
            }
            listaNL.remove(plantillaNLSeleccionada);
            if (tipoLista == 1) {
                listaNLFiltrar.remove(plantillaNLSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosNL");
            contarRegistrosNL();
            plantillaNLSeleccionada = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void seleccionarBorrar() {
        if (cualTabla == 1) {
            if (administrarTTPlantillas.ConsultarRegistrosSecundarios(ttSeleccionado.getSecuencia())) {
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            } else {
                borrarTT();
            }
        } else if (cualTabla == 2) {
            borrarTC();
        } else if (cualTabla == 3) {
            borrarTS();
        } else if (cualTabla == 4) {
            borrarRL();
        } else if (cualTabla == 5) {
            borrarNL();
        } else if (cualTabla == 6) {
            borrarLL();
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void eventoFiltrarTT() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosTT();
    }

    public void eventoFiltrarTC() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosTC();
    }

    public void eventoFiltrarTS() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosTS();
    }

    public void eventoFiltrarRL() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosRL();
    }

    public void eventoFiltrarLL() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosLL();
    }

    public void eventoFiltrarNL() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosNL();
    }

    public void elegirExportarPDF() throws IOException {
        if (cualTabla == 1) {
            exportPDFTT();
        } else if (cualTabla == 2) {
            exportPDFTC();
        } else if (cualTabla == 3) {
            exportPDFTS();
        } else if (cualTabla == 4) {
            exportPDFRL();
        } else if (cualTabla == 5) {
            exportPDFLL();
        } else if (cualTabla == 6) {
            exportPDFNL();
        }
    }

    public void elegirExportarXLS() throws IOException {
        if (cualTabla == 1) {
            exportXLSTT();
        } else if (cualTabla == 2) {
            exportXLSTC();
        } else if (cualTabla == 3) {
            exportXLSTS();
        } else if (cualTabla == 4) {
            exportXLSRL();
        } else if (cualTabla == 5) {
            exportXLSLL();
        } else if (cualTabla == 6) {
            exportXLSNL();
        }
    }

    public void elegirExportarXML() {
        if (cualTabla == 1) {
            tablaImprimir = ":formExportar:datosTTExportar";
            nombreArchivo = "TIPOTRABAJADOR";
            limpiarNuevaTT();
        } else if (cualTabla == 2) {
            tablaImprimir = ":formExportar:datosTCExportar";
            nombreArchivo = "TIPOCONTRATO";
            limpiarNuevaTC();
        } else if (cualTabla == 3) {
            tablaImprimir = ":formExportar:datosTSExportar";
            nombreArchivo = "TIPOSUELDO";
            limpiarNuevaTS();
        } else if (cualTabla == 4) {
            tablaImprimir = ":formExportar:datosRLExportar";
            nombreArchivo = "REFORMALABORAL";
            limpiarNuevaRL();
        } else if (cualTabla == 5) {
            tablaImprimir = ":formExportar:datosNLExportar";
            nombreArchivo = "NORMALABORAL";
            limpiarNuevaNL();
        } else if (cualTabla == 6) {
            tablaImprimir = ":formExportar:datosLLExportar";
            nombreArchivo = "CONTRATO";
            limpiarNuevaLL();
        }
    }

    public void exportPDFTT() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTTExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOTRABAJADOR", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportPDFTC() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTCExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOCONTRATO", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportPDFTS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTSExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSUELDO", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportPDFRL() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosRLExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "RELABORAL", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportPDFLL() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLLExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "LEGISLABORAL", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportPDFNL() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNLExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "NORMALABORAL", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLSTT() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTTExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOTRABAJADOR", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLSTC() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTCExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOCONTRATO", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLSTS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTSExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSUELDO", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLSRL() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosRLExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "RELABORAL", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLSLL() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLLExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "LEGISLABORAL", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLSNL() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNLExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "NORMALABORAL", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (cualTabla == 1) {
            if (ttSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(ttSeleccionado.getSecuencia(), "TIPOTRABAJADOR"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBTT').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroTT').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("TIPOTRABAJADOR")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoTT').show()");
            }
        } else if (cualTabla == 2) {
            if (ttSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(plantillaTCSeleccionada.getSecuencia(), "TIPOCONTRATO"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBTC').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroTC').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }

            } else if (administrarRastros.verificarHistoricosTabla("TIPOCONTRATO")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoTC').show()");
            }
        } else if (cualTabla == 3) {
            if (ttSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(plantillaTSSeleccionada.getSecuencia(), "TIPOSUELDO"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBTS').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroTS').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("TIPOSUELDO")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoTS').show()");
            }
        } else if (cualTabla == 4) {
            if (ttSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(plantillaRLSeleccionada.getSecuencia(), "RELACIONLABORAL"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBRL').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroRL').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("RELACIONLABORAL")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoRL').show()");
            }
        } else if (cualTabla == 5) {
            if (ttSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(plantillaLLSeleccionada.getSecuencia(), "NORMALABORAL"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBNL').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroNL').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("NORMALABORAL")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoNL').show()");
            }
        } else if (cualTabla == 6) {
            if (ttSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(plantillaNLSeleccionada.getSecuencia(), "LEGISLACIONLABORAL"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBLL').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroLL').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("LEGISLACIONLABORAL")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoLL').show()");
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistrosTT() {
        RequestContext.getCurrentInstance().update("form:infoRegistroTT");
    }

    public void contarRegistrosTC() {
        RequestContext.getCurrentInstance().update("form:infoRegistroTC");
    }

    public void contarRegistrosTS() {
        RequestContext.getCurrentInstance().update("form:infoRegistroTS");
    }

    public void contarRegistrosRL() {
        RequestContext.getCurrentInstance().update("form:infoRegistroRL");
    }

    public void contarRegistrosLL() {
        RequestContext.getCurrentInstance().update("form:infoRegistroLL");
    }

    public void contarRegistrosNL() {
        RequestContext.getCurrentInstance().update("form:infoRegistroNL");
    }

    public void contarRegistrosLovTC() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovTC");
    }

    public void contarRegistrosLovTS() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovTS");
    }

    public void contarRegistrosLovRL() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovRL");
    }

    public void contarRegistrosLovLL() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovLL");
    }

    public void contarRegistrosLovNL() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovNL");
    }

    public void activarAceptar() {
        aceptar = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void revisarDialogoGuardar() {

        if (!listaLLBorrar.isEmpty() || !listaLLCrear.isEmpty() || !listaLLModificar.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        } else if (!listaNLBorrar.isEmpty() || !listaNLCrear.isEmpty() || !listaNLModificar.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        } else if (!listaRLBorrar.isEmpty() || !listaRLCrear.isEmpty() || !listaRLModificar.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        } else if (!listaTCBorrar.isEmpty() || !listaTCCrear.isEmpty() || !listaTCModificar.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        } else if (!listaTSBorrar.isEmpty() || !listaTSCrear.isEmpty() || !listaTSModificar.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        } else if (!listaTTBorrar.isEmpty() || !listaTTCrear.isEmpty() || !listaTTModificar.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void mostrarDialogoInsertar(int dialogo) {
        if (dialogo == 1) {
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTT').show()");
        } else if (dialogo == 2) {
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPlantillaTC').show()");
        } else if (dialogo == 3) {
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPlantillaTS').show()");
        } else if (dialogo == 4) {
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPlantillaRL').show()");
        } else if (dialogo == 5) {
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPlantillaNL').show()");
        } else if (dialogo == 6) {
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPlantillaLL').show()");
        }
    }

    public void mostrarDialogoElegirTabla() {
        RequestContext.getCurrentInstance().update("formularioDialogos:seleccionarTablaNewReg");
        RequestContext.getCurrentInstance().execute("PF('seleccionarTablaNewReg').show()");
    }

    public void asignarIndex(int tipoact, int dlg) {
        tipoActualizacion = tipoact;
        if (dlg == 1) {
            lovTipoContrato = null;
            getLovTipoContrato();
            RequestContext.getCurrentInstance().update("formularioDialogos:TCDialogo");
            RequestContext.getCurrentInstance().execute("PF('TCDialogo').show()");
        } else if (dlg == 2) {
            lovTipoSueldo = null;
            getLovTipoSueldo();
            RequestContext.getCurrentInstance().update("formularioDialogos:TSDialogo");
            RequestContext.getCurrentInstance().execute("PF('TSDialogo').show()");
        } else if (dlg == 3) {
            lovReformaLaboral = null;
            getLovReformaLaboral();
            RequestContext.getCurrentInstance().update("formularioDialogos:RLDialogo");
            RequestContext.getCurrentInstance().execute("PF('RLDialogo').show()");
        } else if (dlg == 4) {
            lovNormasLaborales = null;
            getLovNormasLaborales();
            RequestContext.getCurrentInstance().update("formularioDialogos:NLDialogo");
            RequestContext.getCurrentInstance().execute("PF('NLDialogo').show()");
        } else if (dlg == 5) {
            lovContratos = null;
            getLovContratos();
            RequestContext.getCurrentInstance().update("formularioDialogos:ContratoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ContratoDialogo').show()");

        }
    }

    public void actualizarTipoContrato() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            plantillaTCSeleccionada.setTipocontrato(tipoContratoSeleccionado);
            if (!listaTCCrear.contains(plantillaTCSeleccionada)) {
                if (listaTCModificar.isEmpty()) {
                    listaTCModificar.add(plantillaTCSeleccionada);
                } else if (!listaTCModificar.contains(plantillaTCSeleccionada)) {
                    listaTCModificar.add(plantillaTCSeleccionada);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosTC");
        } else if (tipoActualizacion == 1) {
            nuevaPlantillaTC.setTipocontrato(tipoContratoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTC");
        } else if (tipoActualizacion == 2) {
            duplicarPlantillaTC.setTipocontrato(tipoContratoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTC");
        }
        lovTipoContratoFiltrar = null;
        tipoContratoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:TCDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTC");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTC");
        context.reset("formularioDialogos:lovTC:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTC').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TCDialogo').hide()");
    }

    public void cancelarCambioTipoContrato() {
        RequestContext context = RequestContext.getCurrentInstance();
        lovTipoContratoFiltrar = null;
        tipoContratoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:TCDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTC");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTC");
        context.reset("formularioDialogos:lovTC:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTC').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TCDialogo').hide()");
    }

    public void actualizarTipoSueldo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            plantillaTSSeleccionada.setTiposueldo(tipoSueldoSeleccionado);
            if (!listaTSCrear.contains(plantillaTSSeleccionada)) {
                if (listaTSModificar.isEmpty()) {
                    listaTSModificar.add(plantillaTSSeleccionada);
                } else if (!listaTSModificar.contains(plantillaTSSeleccionada)) {
                    listaTSModificar.add(plantillaTSSeleccionada);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosTS");
        } else if (tipoActualizacion == 1) {
            nuevaPlantillaTS.setTiposueldo(tipoSueldoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTS");
        } else if (tipoActualizacion == 2) {
            duplicarPlantillaTS.setTiposueldo(tipoSueldoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTS");
        }
        lovTipoSueldoFiltrar = null;
        tipoSueldoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:TSDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTS");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTS");
        context.reset("formularioDialogos:lovTS:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTS').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TSDialogo').hide()");
    }

    public void cancelarCambioTipoSueldo() {
        lovTipoSueldoFiltrar = null;
        tipoSueldoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:TSDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTS");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTS");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovTS:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTS').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TSDialogo').hide()");
    }

    public void actualizarReformaLaboral() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            plantillaRLSeleccionada.setReformalaboral(reformaLaboralSeleccionado);
            if (!listaRLCrear.contains(plantillaRLSeleccionada)) {
                if (listaRLModificar.isEmpty()) {
                    listaRLModificar.add(plantillaRLSeleccionada);
                } else if (!listaRLModificar.contains(plantillaRLSeleccionada)) {
                    listaRLModificar.add(plantillaRLSeleccionada);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosRL");
        } else if (tipoActualizacion == 1) {
            nuevaPlantillaRL.setReformalaboral(reformaLaboralSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaRL");
        } else if (tipoActualizacion == 2) {
            duplicarPlantillaRL.setReformalaboral(reformaLaboralSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRL");
        }
        lovReformaLaboralFiltrar = null;
        reformaLaboralSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:RLDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovRL");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarRL");
        context.reset("formularioDialogos:lovRL:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovRL').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('RLDialogo').hide()");
    }

    public void cancelarCambioReformaLaboral() {
        lovReformaLaboralFiltrar = null;
        reformaLaboralSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:RLDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovRL");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarRL");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovRL:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovRL').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('RLDialogo').hide()");
    }

    public void actualizarContrato() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            plantillaLLSeleccionada.setContrato(contratoSeleccionado);
            if (!listaLLCrear.contains(plantillaLLSeleccionada)) {
                if (listaLLModificar.isEmpty()) {
                    listaLLModificar.add(plantillaLLSeleccionada);
                } else if (!listaLLModificar.contains(plantillaLLSeleccionada)) {
                    listaLLModificar.add(plantillaLLSeleccionada);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosLL");
        } else if (tipoActualizacion == 1) {
            nuevaPlantillaLL.setContrato(contratoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaLL");
        } else if (tipoActualizacion == 2) {
            duplicarPlantillaLL.setContrato(contratoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarLL");
        }
        lovContratosFiltrar = null;
        contratoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:ContratoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovLL");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarLL");
        context.reset("formularioDialogos:lovLL:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovLL').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ContratoDialogo').hide()");
    }

    public void cancelarCambioContrato() {
        lovContratosFiltrar = null;
        contratoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:ContratoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovLL");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarLL");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovLL:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovLL').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ContratoDialogo').hide()");
    }

    public void actualizarNormaLaboral() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            plantillaNLSeleccionada.setNormalaboral(normaLaboralSeleccionada);
            if (!listaNLCrear.contains(plantillaNLSeleccionada)) {
                if (listaNLModificar.isEmpty()) {
                    listaNLModificar.add(plantillaNLSeleccionada);
                } else if (!listaNLModificar.contains(plantillaNLSeleccionada)) {
                    listaNLModificar.add(plantillaNLSeleccionada);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosNL");
        } else if (tipoActualizacion == 1) {
            nuevaPlantillaNL.setNormalaboral(normaLaboralSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNL");
        } else if (tipoActualizacion == 2) {
            duplicarPlantillaNL.setNormalaboral(normaLaboralSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNL");
        }
        lovNormasLaboralesFiltrar = null;
        normaLaboralSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:NLDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovNL");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarNL");
        context.reset("formularioDialogos:lovNL:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovNL').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('NLDialogo').hide()");
    }

    public void cancelarCambioNormaLaboral() {
        lovNormasLaboralesFiltrar = null;
        normaLaboralSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:NLDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovNL");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarNL");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovNL:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovNL').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('NLDialogo').hide()");
    }

    //////////////////GETS Y SETS///////////////
    public List<TiposTrabajadores> getListaTT() {
        if (listaTT == null) {
            listaTT = administrarTTPlantillas.listaTT();
        }
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
        if (lovTipoContrato == null) {
            lovTipoContrato = administrarTiposContratos.listaTiposContratos();
        }
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
        if (lovTipoSueldo == null) {
            lovTipoSueldo = administrarTiposSueldos.listaTiposSueldos();
        }
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
        if (lovReformaLaboral == null) {
            lovReformaLaboral = administrarReformaLaboral.listaReformasLaborales();
        }
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
        if (lovNormasLaborales == null) {
            lovNormasLaborales = administrarNormaLaboral.consultarNormasLaborales();
        }
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

    public List<Contratos> getLovContratos() {
        if (lovContratos == null) {
            lovContratos = administrarContratos.consultarContratos();
        }
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

    public NormasLaborales getNormaLaboralSeleccionada() {
        return normaLaboralSeleccionada;
    }

    public void setNormaLaboralSeleccionada(NormasLaborales normaLaboralSeleccionada) {
        this.normaLaboralSeleccionada = normaLaboralSeleccionada;
    }

    public Contratos getContratoSeleccionado() {
        return contratoSeleccionado;
    }

    public void setContratoSeleccionado(Contratos contratoSeleccionado) {
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTT");
        infoRegistroTT = String.valueOf(tabla.getRowCount());
        return infoRegistroTT;
    }

    public void setInfoRegistroTT(String infoRegistroTT) {
        this.infoRegistroTT = infoRegistroTT;
    }

    public String getInfoRegistroTC() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTC");
        infoRegistroTC = String.valueOf(tabla.getRowCount());
        return infoRegistroTC;
    }

    public void setInfoRegistroTC(String infoRegistroTC) {
        this.infoRegistroTC = infoRegistroTC;
    }

    public String getInfoRegistroTS() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTS");
        infoRegistroTS = String.valueOf(tabla.getRowCount());
        return infoRegistroTS;
    }

    public void setInfoRegistroTS(String infoRegistroTS) {
        this.infoRegistroTS = infoRegistroTS;
    }

    public String getInfoRegistroRL() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosRL");
        infoRegistroRL = String.valueOf(tabla.getRowCount());
        return infoRegistroRL;
    }

    public void setInfoRegistroRL(String infoRegistroRL) {
        this.infoRegistroRL = infoRegistroRL;
    }

    public String getInfoRegistroLL() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosLL");
        infoRegistroLL = String.valueOf(tabla.getRowCount());
        return infoRegistroLL;
    }

    public void setInfoRegistroLL(String infoRegistroLL) {
        this.infoRegistroLL = infoRegistroLL;
    }

    public String getInfoRegistroNL() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNL");
        infoRegistroNL = String.valueOf(tabla.getRowCount());
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

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTablaImprimir() {
        return tablaImprimir;
    }

    public void setTablaImprimir(String tablaImprimir) {
        this.tablaImprimir = tablaImprimir;
    }

    public String getInfoRegistroLovTC() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTC");
        infoRegistroLovTC = String.valueOf(tabla.getRowCount());
        return infoRegistroLovTC;
    }

    public void setInfoRegistroLovTC(String infoRegistroLovTC) {
        this.infoRegistroLovTC = infoRegistroLovTC;
    }

    public String getInfoRegistroLovTS() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTS");
        infoRegistroLovTS = String.valueOf(tabla.getRowCount());
        return infoRegistroLovTS;
    }

    public void setInfoRegistroLovTS(String infoRegistroLovTS) {
        this.infoRegistroLovTS = infoRegistroLovTS;
    }

    public String getInfoRegistroLovRL() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovRL");
        infoRegistroLovRL = String.valueOf(tabla.getRowCount());
        return infoRegistroLovRL;
    }

    public void setInfoRegistroLovRL(String infoRegistroLovRL) {
        this.infoRegistroLovRL = infoRegistroLovRL;
    }

    public String getInfoRegistroLovLL() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovLL");
        infoRegistroLovLL = String.valueOf(tabla.getRowCount());
        return infoRegistroLovLL;
    }

    public void setInfoRegistroLovLL(String infoRegistroLovLL) {
        this.infoRegistroLovLL = infoRegistroLovLL;
    }

    public String getInfoRegistroLovNL() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovNL");
        infoRegistroLovNL = String.valueOf(tabla.getRowCount());
        return infoRegistroLovNL;
    }

    public void setInfoRegistroLovNL(String infoRegistroLovNL) {
        this.infoRegistroLovNL = infoRegistroLovNL;
    }

}
