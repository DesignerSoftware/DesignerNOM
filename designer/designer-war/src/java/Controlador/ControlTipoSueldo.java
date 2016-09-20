package Controlador;


import Entidades.Conceptos;
import Entidades.Empresas;
import Entidades.Formulas;
import Entidades.Grupostiposentidades;
import Entidades.TEFormulasConceptos;
import Entidades.TSFormulasConceptos;
import Entidades.TSGruposTiposEntidades;
import Entidades.TiposEntidades;
import Entidades.TiposSueldos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposSueldosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

/**
 *
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlTipoSueldo implements Serializable {

    @EJB
    AdministrarTiposSueldosInterface administrarTipoSueldo;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //
    private List<TiposSueldos> listaTiposSueldos;
    private List<TiposSueldos> filtrarListaTiposSueldos;
    private TiposSueldos tipoSueldoTablaSeleccionado;
    //
    private List<TSFormulasConceptos> listaTSFormulasConceptos;
    private List<TSFormulasConceptos> filtrarListaTSFormulasConceptos;
    private TSFormulasConceptos tsFormulaTablaSeleccionado;
    //
    private List<TSGruposTiposEntidades> listaTSGruposTiposEntidades;
    private List<TSGruposTiposEntidades> filtrarListaTSGruposTiposEntidades;
    private TSGruposTiposEntidades tsGrupoTablaSeleccionado;
    //
    private List<TEFormulasConceptos> listaTEFormulasConceptos;
    private List<TEFormulasConceptos> filtrarListaTEFormulasConceptos;
    private TEFormulasConceptos teFormulaTablaSeleccionado;
    //Activo/Desactivo Crtl + F11
    private int bandera, banderaTSFormulas, banderaTSGrupos, banderaTEFormulas;
    //Columnas Tabla 
    private Column tipoSueldoCodigo, tipoSueldoDescripcion, tipoSueldoCap, tipoSueldoBas, tipoSueldoAdi;
    private Column tsFormulaFormula, tsFormulaConcepto, tsFormulaEmpresa, tsFormulaOrigen;
    private Column teFormulaFormula, teFormulaConcepto, teFormulaEmpresa, teFormulaTipoEntidad;
    private Column tsGrupoGrupo;
    //Otros
    private boolean aceptar;
    private int index, indexTSFormulas, indexAux, indexTSGrupos, indexTEFormulas, indexAuxTSGrupos;
    //modificar
    private List<TiposSueldos> listTiposSueldosModificar;
    private List<TSFormulasConceptos> listTSFormulasConceptosModificar;
    private List<TSGruposTiposEntidades> listTSGruposTiposEntidadesModificar;
    private List<TEFormulasConceptos> listTEFormulasConceptosModificar;
    private boolean guardado, guardadoTSFormulas, guardadoTSGrupos, guardadoTEFormulas;
    //crear 
    private TiposSueldos nuevoTipoSueldo;
    private TSFormulasConceptos nuevoTSFormulaConcepto;
    private TSGruposTiposEntidades nuevoTSGrupoTipoEntidad;
    private TEFormulasConceptos nuevoTEFormulaConcepto;
    private List<TiposSueldos> listTiposSueldosCrear;
    private List<TSFormulasConceptos> listTSFormulasConceptosCrear;
    private List<TSGruposTiposEntidades> listTSGruposTiposEntidadesCrear;
    private List<TEFormulasConceptos> listTEFormulasConceptosCrear;
    private BigInteger l;
    private int k;
    //borrar 
    private List<TiposSueldos> listTiposSueldosBorrar;
    private List<TSFormulasConceptos> listTSFormulasConceptosBorrar;
    private List<TSGruposTiposEntidades> listTSGruposTiposEntidadesBorrar;
    private List<TEFormulasConceptos> listTEFormulasConceptosBorrar;
    //editar celda
    private TiposSueldos editarTipoSueldo;
    private TSFormulasConceptos editarTSFormulaConcepto;
    private TSGruposTiposEntidades editarTSGrupoTipoEntidad;
    private TEFormulasConceptos editarTEFormulaConcepto;
    private int cualCelda, tipoLista, cualCeldaTSFormulas, tipoListaTSFormulas, cualCeldaTSGrupos, tipoListaTSGrupos, cualCeldaTEFormulas, tipoListaTEFormulas;
    //duplicar
    private TiposSueldos duplicarTipoSueldo;
    private TSFormulasConceptos duplicarTSFormulaConcepto;
    private TSGruposTiposEntidades duplicarTSGrupoTipoEntidad;
    private TEFormulasConceptos duplicarTEFormulaConcepto;
    private BigInteger secRegistro, secRegistroTSFormulas, secRegistroTSGrupos, secRegistroTEFormulas;
    private BigInteger backUpSecRegistro, backUpSecRegistroTSFormulas, backUpSecRegistroTSGrupos, backUpSecRegistroTEFormulas;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    private String nombreXML, nombreTabla;
    private String formula, concepto, grupo, tipoEntidad;

    ///////////LOV///////////
    private List<Formulas> lovFormulas;
    private List<Formulas> filtrarLovFormulas;
    private Formulas formulaSeleccionado;

    private List<Conceptos> lovConceptos;
    private List<Conceptos> filtrarLovConceptos;
    private Conceptos conceptoSeleccionado;

    private List<Grupostiposentidades> lovGruposTiposEntidades;
    private List<Grupostiposentidades> filtrarLovGruposTiposEntidades;
    private Grupostiposentidades grupoTipoEntidadSeleccionado;

    private List<TiposEntidades> lovTiposEntidades;
    private List<TiposEntidades> filtrarLovTiposEntidades;
    private TiposEntidades tipoEntidadSeleccionado;

    private boolean permitirIndexTSFormulas, permitirIndexTSGrupos, permitirIndexTEFormulas;
    private int tipoActualizacion;
    private short auxCodigoTipoSueldo;
    private String auxDescripcionTipoSueldo;
    //
    private boolean cambiosPagina;
    private String altoTablaTiposSueldos, altoTablaTSFormulas, altoTablaTSGrupos, altoTablaTEFormulas;
    //
    private String paginaAnterior;
    //
    private Conceptos auxConceptoTE;
    private TiposEntidades auxTipoEntidadTE;
    //
    private boolean activoFormulaConcepto, activoGrupoDistribucion, activoTipoEntidad;
    //
    private String infoRegistroFormula, infoRegistroConcepto, infoRegistroGrupo, infoRegistroTipoEntidad, infoRegistroFormulaTE, infoRegistroConceptoTE;

    public ControlTipoSueldo() {
        activoFormulaConcepto = true;
        activoGrupoDistribucion = true;
        activoTipoEntidad = true;
        auxConceptoTE = new Conceptos();
        auxTipoEntidadTE = new TiposEntidades();
        paginaAnterior = "";
        //altos tablas
        altoTablaTiposSueldos = "80";
        altoTablaTSFormulas = "230";
        altoTablaTSGrupos = "73";
        altoTablaTEFormulas = "73";
        //Permitir index
        permitirIndexTSFormulas = true;
        permitirIndexTEFormulas = true;
        permitirIndexTSGrupos = true;
        //lovs
        lovFormulas = null;
        formulaSeleccionado = new Formulas();
        lovConceptos = null;
        conceptoSeleccionado = new Conceptos();
        lovGruposTiposEntidades = null;
        grupoTipoEntidadSeleccionado = new Grupostiposentidades();
        lovTiposEntidades = null;
        tipoEntidadSeleccionado = new TiposEntidades();
        //index tablas
        indexTSFormulas = -1;
        indexTSGrupos = -1;
        indexTEFormulas = -1;
        //listas de tablas
        listaTiposSueldos = null;
        listaTSFormulasConceptos = null;
        listaTSGruposTiposEntidades = null;
        listaTEFormulasConceptos = null;
        //Otros
        aceptar = true;
        cambiosPagina = true;
        tipoActualizacion = -1;
        k = 0;
        //borrar 
        listTiposSueldosBorrar = new ArrayList<TiposSueldos>();
        listTSFormulasConceptosBorrar = new ArrayList<TSFormulasConceptos>();
        listTSGruposTiposEntidadesBorrar = new ArrayList<TSGruposTiposEntidades>();
        listTEFormulasConceptosBorrar = new ArrayList<TEFormulasConceptos>();
        //crear 
        listTiposSueldosCrear = new ArrayList<TiposSueldos>();
        listTSFormulasConceptosCrear = new ArrayList<TSFormulasConceptos>();
        listTSGruposTiposEntidadesCrear = new ArrayList<TSGruposTiposEntidades>();
        listTEFormulasConceptosCrear = new ArrayList<TEFormulasConceptos>();
        //modificar 
        listTSFormulasConceptosModificar = new ArrayList<TSFormulasConceptos>();
        listTiposSueldosModificar = new ArrayList<TiposSueldos>();
        listTSGruposTiposEntidadesModificar = new ArrayList<TSGruposTiposEntidades>();
        listTEFormulasConceptosModificar = new ArrayList<TEFormulasConceptos>();
        //editar
        editarTipoSueldo = new TiposSueldos();
        editarTSFormulaConcepto = new TSFormulasConceptos();
        editarTSGrupoTipoEntidad = new TSGruposTiposEntidades();
        editarTEFormulaConcepto = new TEFormulasConceptos();
        //Cual Celda
        cualCelda = -1;
        cualCeldaTSFormulas = -1;
        cualCeldaTEFormulas = -1;
        cualCeldaTSGrupos = -1;
        //Tipo Lista
        tipoListaTSFormulas = 0;
        tipoListaTEFormulas = 0;
        tipoLista = 0;
        tipoListaTSGrupos = 0;
        //guardar
        guardado = true;
        guardadoTSFormulas = true;
        guardadoTEFormulas = true;
        guardadoTSGrupos = true;
        //Crear 
        nuevoTipoSueldo = new TiposSueldos();
        nuevoTSFormulaConcepto = new TSFormulasConceptos();
        nuevoTSFormulaConcepto.setFormula(new Formulas());
        nuevoTSFormulaConcepto.setConcepto(new Conceptos());
        nuevoTSFormulaConcepto.getConcepto().setEmpresa(new Empresas());
        nuevoTSGrupoTipoEntidad = new TSGruposTiposEntidades();
        nuevoTSGrupoTipoEntidad.setGrupotipoentidad(new Grupostiposentidades());
        nuevoTEFormulaConcepto = new TEFormulasConceptos();
        nuevoTEFormulaConcepto.setFormula(new Formulas());
        nuevoTEFormulaConcepto.setTipoentidad(new TiposEntidades());
        nuevoTEFormulaConcepto.setConcepto(new Conceptos());
        nuevoTEFormulaConcepto.getConcepto().setEmpresa(new Empresas());
        //Duplicar
        duplicarTipoSueldo = new TiposSueldos();
        duplicarTSFormulaConcepto = new TSFormulasConceptos();
        duplicarTEFormulaConcepto = new TEFormulasConceptos();
        duplicarTSGrupoTipoEntidad = new TSGruposTiposEntidades();
        //Sec Registro
        secRegistro = null;
        backUpSecRegistro = null;
        secRegistroTSFormulas = null;
        secRegistroTEFormulas = null;
        backUpSecRegistroTSFormulas = null;
        backUpSecRegistroTEFormulas = null;
        secRegistroTSFormulas = null;
        backUpSecRegistroTSGrupos = null;
        //Banderas
        bandera = 0;
        banderaTSFormulas = 0;
        banderaTEFormulas = 0;
        banderaTSGrupos = 0;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTipoSueldo.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public String valorPaginaAnterior() {
        return paginaAnterior;
    }

    public void inicializarPagina(String paginaLlamado) {
        paginaAnterior = paginaLlamado;
        listaTSFormulasConceptos = null;
        listaTiposSueldos = null;
        getListaTiposSueldos();
        int tam = listaTiposSueldos.size();
        if (tam >= 1) {
            index = 0;
            getListaTSFormulasConceptos();
            indexTSGrupos = 0;
            getListaTSGruposTiposEntidades();
        }
    }

    public boolean validarCamposNulosTipoSueldo(int i) {
        boolean retorno = true;
        if (i == 0) {
            TiposSueldos aux = new TiposSueldos();
            if (tipoLista == 0) {
                aux = listaTiposSueldos.get(index);
            }
            if (tipoLista == 1) {
                aux = filtrarListaTiposSueldos.get(index);
            }
            if (aux.getCodigo() == null || aux.getDescripcion().isEmpty()) {
                retorno = false;
            }
            if (aux.getCodigo() != null) {
                if (aux.getCodigo() <= 0) {
                    retorno = false;
                }
            }
        }
        if (i == 1) {
            if (nuevoTipoSueldo.getCodigo() == null || nuevoTipoSueldo.getDescripcion().isEmpty()) {
                retorno = false;
            }
            if (nuevoTipoSueldo.getCodigo() != null) {
                if (nuevoTipoSueldo.getCodigo() <= 0) {
                    retorno = false;
                }
            }
        }
        if (i == 2) {
            if (duplicarTipoSueldo.getCodigo() == null || duplicarTipoSueldo.getDescripcion().isEmpty()) {
                retorno = false;
            }
            if (duplicarTipoSueldo.getCodigo() != null) {
                if (duplicarTipoSueldo.getCodigo() <= 0) {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public boolean validarCamposNulosTSFormula(int i) {
        boolean retorno = true;
        if (i == 1) {
            if (nuevoTSFormulaConcepto.getConcepto().getSecuencia() == null || nuevoTSFormulaConcepto.getFormula().getSecuencia() == null) {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarTSFormulaConcepto.getConcepto().getSecuencia() == null || duplicarTSFormulaConcepto.getFormula().getSecuencia() == null) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarCamposNulosTEFormula(int i) {
        boolean retorno = true;
        if (i == 1) {
            if (nuevoTEFormulaConcepto.getConcepto().getSecuencia() == null || nuevoTEFormulaConcepto.getFormula().getSecuencia() == null || nuevoTEFormulaConcepto.getTipoentidad().getSecuencia() == null) {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarTEFormulaConcepto.getConcepto().getSecuencia() == null || duplicarTEFormulaConcepto.getFormula().getSecuencia() == null || duplicarTEFormulaConcepto.getTipoentidad().getSecuencia() == null) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarTipoEntidadYEmpresaNuevoRegistro(int tipoNuevo) {

        boolean retorno = true;
        if (tipoNuevo == 0) {
            int conteo = 0;
            int tam = listaTEFormulasConceptos.size();
            if (tam > 0) {
                List<TEFormulasConceptos> lista = administrarTipoSueldo.listaTEFormulasConceptos();
                if (tipoListaTEFormulas == 0) {
                    for (int i = 0; i < lista.size(); i++) {
                        TEFormulasConceptos aux = lista.get(i);
                        if ((aux.getConcepto().getEmpresa().getSecuencia().equals(listaTEFormulasConceptos.get(indexTEFormulas).getConcepto().getEmpresa().getSecuencia()))
                                && (aux.getTipoentidad().getSecuencia().equals(listaTEFormulasConceptos.get(indexTEFormulas).getTipoentidad().getSecuencia()))) {
                            conteo++;
                        }
                    }
                }
                if (tipoListaTEFormulas == 1) {
                    for (int i = 0; i < lista.size(); i++) {
                        TEFormulasConceptos aux = lista.get(i);
                        if ((aux.getConcepto().getEmpresa().getSecuencia().equals(filtrarListaTEFormulasConceptos.get(indexTEFormulas).getConcepto().getEmpresa().getSecuencia()))
                                && (aux.getTipoentidad().getSecuencia().equals(filtrarListaTEFormulasConceptos.get(indexTEFormulas).getTipoentidad().getSecuencia()))) {
                            conteo++;
                        }
                    }
                }
            }
            if (conteo > 0) {
                retorno = false;
            }
        }
        if (tipoNuevo == 1) {
            int conteo = 0;
            List<TEFormulasConceptos> lista = administrarTipoSueldo.listaTEFormulasConceptos();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getConcepto().getEmpresa().getSecuencia().equals(nuevoTEFormulaConcepto.getConcepto().getEmpresa().getSecuencia())
                        && lista.get(i).getTipoentidad().getSecuencia().equals(nuevoTEFormulaConcepto.getTipoentidad().getSecuencia())) {
                    conteo++;
                    break;
                }
            }
            if (conteo > 0) {
                retorno = false;
            }
        }
        if (tipoNuevo == 2) {
            int conteo = 0;
            List<TEFormulasConceptos> lista = administrarTipoSueldo.listaTEFormulasConceptos();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getConcepto().getEmpresa().getSecuencia().equals(duplicarTEFormulaConcepto.getConcepto().getEmpresa().getSecuencia())
                        && lista.get(i).getTipoentidad().getSecuencia().equals(duplicarTEFormulaConcepto.getTipoentidad().getSecuencia())) {
                    conteo++;
                    break;
                }
            }
            if (conteo > 0) {
                retorno = false;
            }
        }

        return retorno;
    }

    public void procesoModificacionTipoSueldo(int i) {
        index = i;
        boolean respuesta = validarCamposNulosTipoSueldo(0);
        if (respuesta == true) {
            modificarTipoSueldo(i);
        } else {
            if (tipoLista == 0) {
                listaTiposSueldos.get(index).setCodigo(auxCodigoTipoSueldo);
                listaTiposSueldos.get(index).setDescripcion(auxDescripcionTipoSueldo);
            }
            if (tipoLista == 1) {
                filtrarListaTiposSueldos.get(index).setCodigo(auxCodigoTipoSueldo);
                filtrarListaTiposSueldos.get(index).setDescripcion(auxDescripcionTipoSueldo);
            }
            index = -1;
            secRegistro = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullTipoSueldo').show()");
        }
    }

    public void modificarTipoSueldo(int indice) {
        int tamDes = 0;
        if (tipoLista == 0) {
            tamDes = listaTiposSueldos.get(indice).getDescripcion().length();
        }
        if (tipoLista == 1) {
            tamDes = filtrarListaTiposSueldos.get(indice).getDescripcion().length();
        }
        if (tamDes >= 1 && tamDes <= 30) {
            if (tipoLista == 0) {
                String textM = listaTiposSueldos.get(indice).getDescripcion().toUpperCase();
                listaTiposSueldos.get(indice).setDescripcion(textM);
                if (!listTiposSueldosCrear.contains(listaTiposSueldos.get(indice))) {
                    if (listTiposSueldosModificar.isEmpty()) {
                        listTiposSueldosModificar.add(listaTiposSueldos.get(indice));
                    } else if (!listTiposSueldosModificar.contains(listaTiposSueldos.get(indice))) {
                        listTiposSueldosModificar.add(listaTiposSueldos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
            if (tipoLista == 1) {
                String textM = filtrarListaTiposSueldos.get(indice).getDescripcion().toUpperCase();
                filtrarListaTiposSueldos.get(indice).setDescripcion(textM);
                if (!listTiposSueldosCrear.contains(filtrarListaTiposSueldos.get(indice))) {
                    if (listTiposSueldosModificar.isEmpty()) {
                        listTiposSueldosModificar.add(filtrarListaTiposSueldos.get(indice));
                    } else if (!listTiposSueldosModificar.contains(filtrarListaTiposSueldos.get(indice))) {
                        listTiposSueldosModificar.add(filtrarListaTiposSueldos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        //RequestContext.getCurrentInstance().update("form:aceptar");
                    }
                }
            }
            index = -1;
            secRegistro = null;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
        } else {
            if (tipoLista == 0) {
                listaTiposSueldos.get(index).setDescripcion(auxDescripcionTipoSueldo);
            }
            if (tipoLista == 1) {
                filtrarListaTiposSueldos.get(index).setDescripcion(auxDescripcionTipoSueldo);
            }
            index = -1;
            secRegistro = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
            RequestContext.getCurrentInstance().execute("PF('errorDescripcionTipoSueldo').show()");
        }

    }

    public void modificarTSFormula(int indice) {
        if (tipoListaTSFormulas == 0) {
            if (!listTSFormulasConceptosCrear.contains(listaTSFormulasConceptos.get(indice))) {
                if (listTSFormulasConceptosModificar.isEmpty()) {
                    listTSFormulasConceptosModificar.add(listaTSFormulasConceptos.get(indice));
                } else if (!listTSFormulasConceptosModificar.contains(listaTSFormulasConceptos.get(indice))) {
                    listTSFormulasConceptosModificar.add(listaTSFormulasConceptos.get(indice));
                }
                if (guardadoTSFormulas == true) {
                    guardadoTSFormulas = false;
                }
            }
        }
        if (tipoListaTSFormulas == 1) {
            if (!listTSFormulasConceptosCrear.contains(filtrarListaTSFormulasConceptos.get(indice))) {
                if (listTSFormulasConceptosModificar.isEmpty()) {
                    listTSFormulasConceptosModificar.add(filtrarListaTSFormulasConceptos.get(indice));
                } else if (!listTSFormulasConceptosModificar.contains(filtrarListaTSFormulasConceptos.get(indice))) {
                    listTSFormulasConceptosModificar.add(filtrarListaTSFormulasConceptos.get(indice));
                }
                if (guardadoTSFormulas == true) {
                    guardadoTSFormulas = false;
                    //RequestContext.getCurrentInstance().update("form:aceptar");
                }
            }
        }
        indexTSFormulas = -1;
        secRegistroTSFormulas = null;
        cambiosPagina = false;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTSFormula");
    }

    public void modificarTSFormula(int indice, String confirmarCambio, String valorConfirmar) {
        indexTSFormulas = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            if (tipoListaTSFormulas == 0) {
                listaTSFormulasConceptos.get(indice).getFormula().setNombrelargo(formula);
            } else {
                filtrarListaTSFormulasConceptos.get(indice).getFormula().setNombrelargo(formula);
            }
            for (int i = 0; i < lovFormulas.size(); i++) {
                if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaTSFormulas == 0) {
                    listaTSFormulasConceptos.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
                } else {
                    filtrarListaTSFormulasConceptos.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
                }
                lovFormulas.clear();
                getLovFormulas();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndexTSFormulas = false;
                RequestContext.getCurrentInstance().update("form:FormulaDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
                tipoActualizacion = 0;
            }
        }

        if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            if (tipoListaTSFormulas == 0) {
                listaTSFormulasConceptos.get(indice).getConcepto().setDescripcion(concepto);
            } else {
                filtrarListaTSFormulasConceptos.get(indice).getConcepto().setDescripcion(concepto);
            }
            for (int i = 0; i < lovConceptos.size(); i++) {
                if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaTSFormulas == 0) {
                    listaTSFormulasConceptos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                } else {
                    filtrarListaTSFormulasConceptos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                }
                lovConceptos.clear();
                getLovConceptos();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndexTSFormulas = false;
                RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
                tipoActualizacion = 0;
            }
        }

        if (coincidencias == 1) {
            if (tipoListaTSFormulas == 0) {
                if (!listTSFormulasConceptosCrear.contains(listaTSFormulasConceptos.get(indice))) {
                    if (listTSFormulasConceptosModificar.isEmpty()) {
                        listTSFormulasConceptosModificar.add(listaTSFormulasConceptos.get(indice));
                    } else if (!listTSFormulasConceptosModificar.contains(listaTSFormulasConceptos.get(indice))) {
                        listTSFormulasConceptosModificar.add(listaTSFormulasConceptos.get(indice));
                    }
                    if (guardadoTSFormulas == true) {
                        guardadoTSFormulas = false;
                    }
                }
            }
            if (tipoListaTSFormulas == 1) {
                if (!listTSFormulasConceptosCrear.contains(filtrarListaTSFormulasConceptos.get(indice))) {
                    if (listTSFormulasConceptosModificar.isEmpty()) {
                        listTSFormulasConceptosModificar.add(filtrarListaTSFormulasConceptos.get(indice));
                    } else if (!listTSFormulasConceptosModificar.contains(filtrarListaTSFormulasConceptos.get(indice))) {
                        listTSFormulasConceptosModificar.add(filtrarListaTSFormulasConceptos.get(indice));
                    }
                    if (guardadoTSFormulas == true) {
                        guardadoTSFormulas = false;
                        //RequestContext.getCurrentInstance().update("form:aceptar");
                    }
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosTSFormula");
    }

    public void modificarTSGrupo(int indice) {
        if (tipoListaTSGrupos == 0) {
            if (!listTSGruposTiposEntidadesCrear.contains(listaTSGruposTiposEntidades.get(indice))) {
                if (listTSGruposTiposEntidadesModificar.isEmpty()) {
                    listTSGruposTiposEntidadesModificar.add(listaTSGruposTiposEntidades.get(indice));
                } else if (!listTSGruposTiposEntidadesModificar.contains(listaTSGruposTiposEntidades.get(indice))) {
                    listTSGruposTiposEntidadesModificar.add(listaTSGruposTiposEntidades.get(indice));
                }
                if (guardadoTSGrupos == true) {
                    guardadoTSGrupos = false;
                }
            }
        }
        if (tipoListaTSGrupos == 1) {
            if (!listTSGruposTiposEntidadesCrear.contains(filtrarListaTSGruposTiposEntidades.get(indice))) {
                if (listTSGruposTiposEntidadesModificar.isEmpty()) {
                    listTSGruposTiposEntidadesModificar.add(filtrarListaTSGruposTiposEntidades.get(indice));
                } else if (!listTSGruposTiposEntidadesModificar.contains(filtrarListaTSGruposTiposEntidades.get(indice))) {
                    listTSGruposTiposEntidadesModificar.add(filtrarListaTSGruposTiposEntidades.get(indice));
                }
                if (guardadoTSGrupos == true) {
                    guardadoTSGrupos = false;
                    //RequestContext.getCurrentInstance().update("form:aceptar");
                }
            }
        }
        indexTSGrupos = -1;
        secRegistroTSGrupos = null;
        cambiosPagina = false;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTSGrupo");
    }

    public void modificarTSGrupo(int indice, String confirmarCambio, String valorConfirmar) {
        indexTSGrupos = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("GRUPO")) {
            if (tipoListaTSGrupos == 0) {
                listaTSGruposTiposEntidades.get(indice).getGrupotipoentidad().setNombre(grupo);
            } else {
                filtrarListaTSGruposTiposEntidades.get(indice).getGrupotipoentidad().setNombre(grupo);
            }
            for (int i = 0; i < lovGruposTiposEntidades.size(); i++) {
                if (lovGruposTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaTSGrupos == 0) {
                    listaTSGruposTiposEntidades.get(indice).setGrupotipoentidad(lovGruposTiposEntidades.get(indiceUnicoElemento));
                } else {
                    filtrarListaTSGruposTiposEntidades.get(indice).setGrupotipoentidad(lovGruposTiposEntidades.get(indiceUnicoElemento));
                }
                lovGruposTiposEntidades.clear();
                getLovGruposTiposEntidades();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndexTSGrupos = false;
                RequestContext.getCurrentInstance().update("form:GrupoDialogo");
                RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoListaTSGrupos == 0) {
                if (!listTSGruposTiposEntidadesCrear.contains(listaTSGruposTiposEntidades.get(indice))) {
                    if (listTSGruposTiposEntidadesModificar.isEmpty()) {
                        listTSGruposTiposEntidadesModificar.add(listaTSGruposTiposEntidades.get(indice));
                    } else if (!listTSGruposTiposEntidadesModificar.contains(listaTSGruposTiposEntidades.get(indice))) {
                        listTSGruposTiposEntidadesModificar.add(listaTSGruposTiposEntidades.get(indice));
                    }
                    if (guardadoTSGrupos == true) {
                        guardadoTSGrupos = false;
                    }
                }
            }
            if (tipoListaTSGrupos == 1) {
                if (!listTSGruposTiposEntidadesCrear.contains(filtrarListaTSGruposTiposEntidades.get(indice))) {
                    if (listTSGruposTiposEntidadesModificar.isEmpty()) {
                        listTSGruposTiposEntidadesModificar.add(filtrarListaTSGruposTiposEntidades.get(indice));
                    } else if (!listTSGruposTiposEntidadesModificar.contains(filtrarListaTSGruposTiposEntidades.get(indice))) {
                        listTSGruposTiposEntidadesModificar.add(filtrarListaTSGruposTiposEntidades.get(indice));
                    }
                    if (guardadoTSGrupos == true) {
                        guardadoTSGrupos = false;
                        //RequestContext.getCurrentInstance().update("form:aceptar");
                    }
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosTSGrupo");
    }

    public void modificarTEFormula(int indice) {
        if (validarTipoEntidadYEmpresaNuevoRegistro(0) == true) {
            if (tipoListaTEFormulas == 0) {
                if (!listTEFormulasConceptosCrear.contains(listaTEFormulasConceptos.get(indice))) {
                    if (listTEFormulasConceptosModificar.isEmpty()) {
                        listTEFormulasConceptosModificar.add(listaTEFormulasConceptos.get(indice));
                    } else if (!listTEFormulasConceptosModificar.contains(listaTEFormulasConceptos.get(indice))) {
                        listTEFormulasConceptosModificar.add(listaTEFormulasConceptos.get(indice));
                    }
                    if (guardadoTEFormulas == true) {
                        guardadoTEFormulas = false;
                    }
                }
            }
            if (tipoListaTEFormulas == 1) {
                if (!listTEFormulasConceptosCrear.contains(filtrarListaTEFormulasConceptos.get(indice))) {
                    if (listTEFormulasConceptosModificar.isEmpty()) {
                        listTEFormulasConceptosModificar.add(filtrarListaTEFormulasConceptos.get(indice));
                    } else if (!listTEFormulasConceptosModificar.contains(filtrarListaTEFormulasConceptos.get(indice))) {
                        listTEFormulasConceptosModificar.add(filtrarListaTEFormulasConceptos.get(indice));
                    }
                    if (guardadoTEFormulas == true) {
                        guardadoTEFormulas = false;
                        //RequestContext.getCurrentInstance().update("form:aceptar");
                    }
                }
            }
            indexTEFormulas = -1;
            secRegistroTEFormulas = null;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTEFormula");
        } else {
            if (tipoListaTEFormulas == 0) {
                listaTEFormulasConceptos.get(indexTEFormulas).setConcepto(auxConceptoTE);
                listaTEFormulasConceptos.get(indexTEFormulas).setTipoentidad(auxTipoEntidadTE);
            }
            if (tipoListaTEFormulas == 1) {
                filtrarListaTEFormulasConceptos.get(indexTEFormulas).setConcepto(auxConceptoTE);
                filtrarListaTEFormulasConceptos.get(indexTEFormulas).setTipoentidad(auxTipoEntidadTE);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorNuevoRegistroTEFormula').show()");
            RequestContext.getCurrentInstance().update("form:datosTEFormula");
        }
    }

    public void modificarTEFormula(int indice, String confirmarCambio, String valorConfirmar) {
        indexTEFormulas = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("FORMULATE")) {
            if (tipoListaTEFormulas == 0) {
                listaTEFormulasConceptos.get(indice).getFormula().setNombrelargo(formula);
            } else {
                filtrarListaTEFormulasConceptos.get(indice).getFormula().setNombrelargo(formula);
            }
            for (int i = 0; i < lovFormulas.size(); i++) {
                if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaTEFormulas == 0) {
                    listaTEFormulasConceptos.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
                } else {
                    filtrarListaTEFormulasConceptos.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
                }
                lovFormulas.clear();
                getLovFormulas();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndexTEFormulas = false;
                RequestContext.getCurrentInstance().update("form:FormulaTEDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaTEDialogo').show()");
                tipoActualizacion = 0;
            }
        }

        if (confirmarCambio.equalsIgnoreCase("CONCEPTOTE")) {
            if (tipoListaTEFormulas == 0) {
                listaTEFormulasConceptos.get(indice).getConcepto().setDescripcion(concepto);
            } else {
                filtrarListaTEFormulasConceptos.get(indice).getConcepto().setDescripcion(concepto);
            }
            for (int i = 0; i < lovConceptos.size(); i++) {
                if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaTEFormulas == 0) {
                    listaTEFormulasConceptos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                } else {
                    filtrarListaTEFormulasConceptos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                }
                lovConceptos.clear();
                getLovConceptos();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndexTEFormulas = false;
                RequestContext.getCurrentInstance().update("form:ConceptoTEDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoTEDialogo').show()");
                tipoActualizacion = 0;
            }
        }

        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            if (tipoListaTEFormulas == 0) {
                listaTEFormulasConceptos.get(indice).getTipoentidad().setNombre(tipoEntidad);
            } else {
                filtrarListaTEFormulasConceptos.get(indice).getTipoentidad().setNombre(concepto);
            }
            for (int i = 0; i < lovTiposEntidades.size(); i++) {
                if (lovTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaTEFormulas == 0) {
                    listaTEFormulasConceptos.get(indice).setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
                } else {
                    filtrarListaTEFormulasConceptos.get(indice).setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
                }
                lovTiposEntidades.clear();
                getLovTiposEntidades();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndexTEFormulas = false;
                RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
                tipoActualizacion = 0;
            }
        }

        if (coincidencias == 1) {
            if (tipoListaTEFormulas == 0) {
                if (!listTEFormulasConceptosCrear.contains(listaTEFormulasConceptos.get(indice))) {
                    if (listTEFormulasConceptosModificar.isEmpty()) {
                        listTEFormulasConceptosModificar.add(listaTEFormulasConceptos.get(indice));
                    } else if (!listTEFormulasConceptosModificar.contains(listaTEFormulasConceptos.get(indice))) {
                        listTEFormulasConceptosModificar.add(listaTEFormulasConceptos.get(indice));
                    }
                    if (guardadoTEFormulas == true) {
                        guardadoTEFormulas = false;
                    }
                }
            }
            if (tipoListaTEFormulas == 1) {
                if (!listTEFormulasConceptosCrear.contains(filtrarListaTEFormulasConceptos.get(indice))) {
                    if (listTEFormulasConceptosModificar.isEmpty()) {
                        listTEFormulasConceptosModificar.add(filtrarListaTEFormulasConceptos.get(indice));
                    } else if (!listTEFormulasConceptosModificar.contains(filtrarListaTEFormulasConceptos.get(indice))) {
                        listTEFormulasConceptosModificar.add(filtrarListaTEFormulasConceptos.get(indice));
                    }
                    if (guardadoTEFormulas == true) {
                        guardadoTEFormulas = false;
                        //RequestContext.getCurrentInstance().update("form:aceptar");
                    }
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosTEFormula");
    }

    public void posicionTSFormula() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndiceTSFormula(indice, columna);
    }

    public void posicionTEFormula() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndiceTEFormula(indice, columna);
    }

    public void cambiarIndice(int indice, int celda) {
        if (guardadoTSFormulas == true && guardadoTSGrupos == true && guardadoTEFormulas == true) {
            cualCelda = celda;
            indexAux = indice;
            index = indice;
            indexTSFormulas = -1;
            indexTSGrupos = -1;
            indexAuxTSGrupos = -1;
            indexTEFormulas = -1;
            if (tipoLista == 0) {
                auxCodigoTipoSueldo = listaTiposSueldos.get(index).getCodigo();
                secRegistro = listaTiposSueldos.get(index).getSecuencia();
                auxDescripcionTipoSueldo = listaTiposSueldos.get(index).getDescripcion();
            }
            if (tipoLista == 1) {
                auxCodigoTipoSueldo = filtrarListaTiposSueldos.get(index).getCodigo();
                secRegistro = filtrarListaTiposSueldos.get(index).getSecuencia();
                auxDescripcionTipoSueldo = filtrarListaTiposSueldos.get(index).getDescripcion();
            }
            RequestContext context = RequestContext.getCurrentInstance();
            listaTSFormulasConceptos = null;
            getListaTSFormulasConceptos();
            RequestContext.getCurrentInstance().update("form:datosTSFormula");
            listaTSGruposTiposEntidades = null;
            getListaTSGruposTiposEntidades();
            RequestContext.getCurrentInstance().update("form:datosTSGrupo");
            listaTEFormulasConceptos = null;
            RequestContext.getCurrentInstance().update("form:datosTEFormula");
            if (banderaTSFormulas == 1) {
                altoTablaTSFormulas = "230";
                tsFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaConcepto");
                tsFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
                tsFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaFormula");
                tsFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
                tsFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaEmpresa");
                tsFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
                tsFormulaOrigen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaOrigen");
                tsFormulaOrigen.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTSFormula");
                banderaTSFormulas = 0;
                filtrarListaTSFormulasConceptos = null;
                tipoListaTSFormulas = 0;
            }
            if (banderaTSGrupos == 1) {
                altoTablaTSGrupos = "73";
                tsGrupoGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSGrupo:tsGrupoGrupo");
                tsGrupoGrupo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTSGrupo");
                banderaTSGrupos = 0;
                filtrarListaTSGruposTiposEntidades = null;
                tipoListaTSGrupos = 0;
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void cambiarIndiceTSFormula(int indice, int celda) {
        if (guardadoTEFormulas == true) {
            if (permitirIndexTSFormulas == true) {
                indexTSFormulas = indice;
                index = indice;
                index = -1;
                indexTSGrupos = -1;
                indexTEFormulas = -1;
                indexAuxTSGrupos = -1;
                cualCeldaTSFormulas = celda;
                if (tipoListaTSFormulas == 0) {
                    secRegistroTSFormulas = listaTSFormulasConceptos.get(indexTSFormulas).getSecuencia();
                    formula = listaTSFormulasConceptos.get(indexTSFormulas).getFormula().getNombrelargo();
                    concepto = listaTSFormulasConceptos.get(indexTSFormulas).getConcepto().getDescripcion();
                }
                if (tipoListaTSFormulas == 1) {
                    secRegistroTSFormulas = filtrarListaTSFormulasConceptos.get(indexTSFormulas).getSecuencia();
                    formula = filtrarListaTSFormulasConceptos.get(indexTSFormulas).getFormula().getNombrelargo();
                    concepto = filtrarListaTSFormulasConceptos.get(indexTSFormulas).getConcepto().getDescripcion();
                }

            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void cambiarIndiceTSGrupo(int indice, int celda) {
        if (guardadoTEFormulas == true) {
            if (permitirIndexTSGrupos == true) {
                indexTSGrupos = indice;
                indexAuxTSGrupos = indexTSGrupos;
                index = -1;
                indexTEFormulas = -1;
                indexTSFormulas = -1;
                cualCeldaTSGrupos = celda;
                if (tipoListaTSGrupos == 0) {
                    secRegistroTSGrupos = listaTSGruposTiposEntidades.get(indexTSGrupos).getSecuencia();
                    grupo = listaTSGruposTiposEntidades.get(indexTSGrupos).getGrupotipoentidad().getNombre();
                }
                if (tipoListaTSGrupos == 1) {
                    secRegistroTSGrupos = filtrarListaTSGruposTiposEntidades.get(indexTSGrupos).getSecuencia();
                    grupo = filtrarListaTSGruposTiposEntidades.get(indexTSGrupos).getGrupotipoentidad().getNombre();
                }
                if (banderaTSFormulas == 1) {
                    altoTablaTSFormulas = "230";
                    tsFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaConcepto");
                    tsFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
                    tsFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaFormula");
                    tsFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
                    tsFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaEmpresa");
                    tsFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
                    tsFormulaOrigen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaOrigen");
                    tsFormulaOrigen.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosTSFormula");
                    banderaTSFormulas = 0;
                    filtrarListaTSFormulasConceptos = null;
                    tipoListaTSFormulas = 0;
                }
                if (banderaTEFormulas == 1) {
                    altoTablaTEFormulas = "73";
                    teFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaConcepto");
                    teFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
                    teFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaFormula");
                    teFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
                    teFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaEmpresa");
                    teFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
                    teFormulaTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaTipoEntidad");
                    teFormulaTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosTEFormula");
                    banderaTEFormulas = 0;
                    filtrarListaTEFormulasConceptos = null;
                    tipoListaTEFormulas = 0;
                }

                listaTEFormulasConceptos = null;
                getListaTEFormulasConceptos();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('form:lovTipoEntidad').clearFilters()");
                RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
                RequestContext.getCurrentInstance().update("form:lovTipoEntidad");
                RequestContext.getCurrentInstance().update("form:datosTEFormula");
                lovTiposEntidades = null;
                getLovTiposEntidades();
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void cambiarIndiceTEFormula(int indice, int celda) {
        if (permitirIndexTEFormulas == true) {
            indexTEFormulas = indice;
            index = -1;
            indexTSFormulas = -1;
            indexTSGrupos = -1;
            cualCeldaTEFormulas = celda;
            lovTiposEntidades = null;
            getLovTiposEntidades();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('form:lovTipoEntidad').clearFilters()");
            RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
            RequestContext.getCurrentInstance().update("form:lovTipoEntidad");
            if (tipoListaTEFormulas == 0) {
                secRegistroTEFormulas = listaTEFormulasConceptos.get(indexTEFormulas).getSecuencia();
                formula = listaTEFormulasConceptos.get(indexTEFormulas).getFormula().getNombrelargo();
                concepto = listaTEFormulasConceptos.get(indexTEFormulas).getConcepto().getDescripcion();
                tipoEntidad = listaTEFormulasConceptos.get(indexTEFormulas).getTipoentidad().getNombre();
                auxTipoEntidadTE = listaTEFormulasConceptos.get(indexTEFormulas).getTipoentidad();
                auxConceptoTE = listaTEFormulasConceptos.get(indexTEFormulas).getConcepto();
            }
            if (tipoListaTEFormulas == 1) {
                secRegistroTEFormulas = filtrarListaTEFormulasConceptos.get(indexTEFormulas).getSecuencia();
                formula = filtrarListaTEFormulasConceptos.get(indexTEFormulas).getFormula().getNombrelargo();
                concepto = filtrarListaTEFormulasConceptos.get(indexTEFormulas).getConcepto().getDescripcion();
                tipoEntidad = filtrarListaTEFormulasConceptos.get(indexTEFormulas).getTipoentidad().getNombre();
                auxTipoEntidadTE = filtrarListaTEFormulasConceptos.get(indexTEFormulas).getTipoentidad();
                auxConceptoTE = filtrarListaTEFormulasConceptos.get(indexTEFormulas).getConcepto();
            }
            if (banderaTSGrupos == 1) {
                altoTablaTSGrupos = "73";
                tsGrupoGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSGrupo:tsGrupoGrupo");
                tsGrupoGrupo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTSGrupo");
                banderaTSGrupos = 0;
                filtrarListaTSGruposTiposEntidades = null;
                tipoListaTSGrupos = 0;
            }
        }
    }
    //GUARDAR

    public void guardarYSalir() {
        guardarGeneral();
        salir();
    }

    public void cancelarYSalir() {
        cancelarModificacionGeneral();
        salir();
    }

    public void guardarGeneral() {
        if (guardado == false || guardadoTSFormulas == false || guardadoTSGrupos == false || guardadoTEFormulas == false) {
            if (guardado == false) {
                guardarCambiosTipoSueldo();
            }
            if (guardadoTSFormulas == false) {
                guardarCambiosTSFormula();
            }
            if (guardadoTSGrupos == false) {
                guardarCambiosTSGrupo();
            }
            if (guardadoTEFormulas == false) {
                guardarCambiosTEFormula();
            }
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            cambiosPagina = true;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void guardarCambiosTipoSueldo() {

        if (!listTiposSueldosBorrar.isEmpty()) {
            for (int i = 0; i < listTiposSueldosBorrar.size(); i++) {
                administrarTipoSueldo.borrarTiposSueldos(listTiposSueldosBorrar);
            }
            listTiposSueldosBorrar.clear();
        }
        if (!listTiposSueldosCrear.isEmpty()) {
            for (int i = 0; i < listTiposSueldosCrear.size(); i++) {
                administrarTipoSueldo.crearTiposSueldos(listTiposSueldosCrear);
            }
            listTiposSueldosCrear.clear();
        }
        if (!listTiposSueldosModificar.isEmpty()) {
            administrarTipoSueldo.editarTiposSueldos(listTiposSueldosModificar);
            listTiposSueldosModificar.clear();
        }
        listaTiposSueldos = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
        guardado = true;
        RequestContext.getCurrentInstance().update("form:aceptar");
        k = 0;
        index = -1;
        secRegistro = null;

    }

    public void guardarCambiosTSFormula() {
        if (!listTSFormulasConceptosBorrar.isEmpty()) {
            administrarTipoSueldo.borrarTSFormulasConceptos(listTSFormulasConceptosBorrar);
            listTSFormulasConceptosBorrar.clear();
        }
        if (!listTSFormulasConceptosCrear.isEmpty()) {
            administrarTipoSueldo.crearTSFormulasConceptos(listTSFormulasConceptosCrear);
            listTSFormulasConceptosCrear.clear();
        }
        if (!listTSFormulasConceptosModificar.isEmpty()) {
            administrarTipoSueldo.editarTSFormulasConceptos(listTSFormulasConceptosModificar);
            listTSFormulasConceptosModificar.clear();
        }
        listaTSFormulasConceptos = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTSFormula");
        guardadoTSFormulas = true;
        RequestContext.getCurrentInstance().update("form:aceptar");
        k = 0;
        indexTSFormulas = -1;
        secRegistroTSFormulas = null;
    }

    public void guardarCambiosTSGrupo() {
        if (!listTSGruposTiposEntidadesBorrar.isEmpty()) {
            administrarTipoSueldo.borrarTSGruposTiposEntidades(listTSGruposTiposEntidadesBorrar);
            listTSGruposTiposEntidadesBorrar.clear();
        }
        if (!listTSGruposTiposEntidadesCrear.isEmpty()) {
            administrarTipoSueldo.crearTSGruposTiposEntidades(listTSGruposTiposEntidadesCrear);
            listTSGruposTiposEntidadesCrear.clear();
        }
        if (!listTSGruposTiposEntidadesModificar.isEmpty()) {
            administrarTipoSueldo.editarTSGruposTiposEntidades(listTSGruposTiposEntidadesModificar);
            listTSGruposTiposEntidadesModificar.clear();
        }
        listaTSGruposTiposEntidades = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTSGrupo");
        guardadoTSGrupos = true;
        RequestContext.getCurrentInstance().update("form:aceptar");
        k = 0;
        indexTSGrupos = -1;
        secRegistroTSGrupos = null;
    }

    public void guardarCambiosTEFormula() {
        if (!listTEFormulasConceptosBorrar.isEmpty()) {
            administrarTipoSueldo.borrarTEFormulasConceptos(listTEFormulasConceptosBorrar);
            listTEFormulasConceptosBorrar.clear();
        }
        if (!listTEFormulasConceptosCrear.isEmpty()) {
            administrarTipoSueldo.crearTEFormulasConceptos(listTEFormulasConceptosCrear);
            listTEFormulasConceptosCrear.clear();
        }
        if (!listTEFormulasConceptosModificar.isEmpty()) {
            administrarTipoSueldo.editarTEFormulasConceptos(listTEFormulasConceptosModificar);
            listTEFormulasConceptosModificar.clear();
        }
        listaTEFormulasConceptos = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTEFormula");
        guardadoTEFormulas = true;
        RequestContext.getCurrentInstance().update("form:aceptar");
        k = 0;
        indexTEFormulas = -1;
        secRegistroTEFormulas = null;
    }
    //CANCELAR MODIFICACIONES

    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacionGeneral() {
        if (guardado == false) {
            cancelarModificacionTipoSueldo();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
        }
        if (guardadoTSFormulas == false) {
            cancelarModificacionTSFormula();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTSFormula");
        }
        if (guardadoTSGrupos == false) {
            cancelarModificacionTSGrupo();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTSGrupo");
        }
        if (guardadoTEFormulas == false) {
            cancelarModificacionTEFormula();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTEFormula");
        }
        indexAuxTSGrupos = -1;
        lovTiposEntidades = null;
        listaTEFormulasConceptos = null;
        getListaTEFormulasConceptos();
        cambiosPagina = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTEFormula");
    }

    public void cancelarModificacionTipoSueldo() {
        if (bandera == 1) {
            altoTablaTiposSueldos = "80";
            tipoSueldoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCodigo");
            tipoSueldoCodigo.setFilterStyle("display: none; visibility: hidden;");
            tipoSueldoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoDescripcion");
            tipoSueldoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            tipoSueldoCap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCap");
            tipoSueldoCap.setFilterStyle("display: none; visibility: hidden;");
            tipoSueldoBas = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoBas");
            tipoSueldoBas.setFilterStyle("display: none; visibility: hidden;");
            tipoSueldoAdi = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoAdi");
            tipoSueldoAdi.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
            bandera = 0;
            filtrarListaTiposSueldos = null;
            tipoLista = 0;
        }
        listTiposSueldosBorrar.clear();
        listTiposSueldosCrear.clear();
        listTiposSueldosModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaTiposSueldos = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
    }

    public void cancelarModificacionTSFormula() {
        if (banderaTSFormulas == 1) {
            altoTablaTSFormulas = "160";
            tsFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaConcepto");
            tsFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
            tsFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaFormula");
            tsFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
            tsFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaEmpresa");
            tsFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
            tsFormulaOrigen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaOrigen");
            tsFormulaOrigen.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTSFormula");
            banderaTSFormulas = 0;
            filtrarListaTSFormulasConceptos = null;
            tipoListaTSFormulas = 0;
        }
        listTSFormulasConceptosBorrar.clear();
        listTSFormulasConceptosCrear.clear();
        listTSFormulasConceptosModificar.clear();
        indexTSFormulas = -1;
        secRegistroTSFormulas = null;
        k = 0;
        listaTSFormulasConceptos = null;
        guardadoTSFormulas = true;
        permitirIndexTSFormulas = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTSFormula");
    }

    public void cancelarModificacionTSGrupo() {
        if (banderaTSGrupos == 1) {
            altoTablaTSGrupos = "73";
            tsGrupoGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSGrupo:tsGrupoGrupo");
            tsGrupoGrupo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTSGrupo");
            banderaTSGrupos = 0;
            filtrarListaTSGruposTiposEntidades = null;
            tipoListaTSGrupos = 0;
        }
        listTSGruposTiposEntidadesBorrar.clear();
        listTSGruposTiposEntidadesCrear.clear();
        listTSGruposTiposEntidadesModificar.clear();
        indexTSGrupos = -1;
        secRegistroTSGrupos = null;
        k = 0;
        listaTSGruposTiposEntidades = null;
        guardadoTSGrupos = true;
        permitirIndexTSGrupos = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTSGrupo");
    }

    public void cancelarModificacionTEFormula() {
        if (banderaTEFormulas == 1) {
            altoTablaTEFormulas = "73";
            teFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaConcepto");
            teFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
            teFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaFormula");
            teFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
            teFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaEmpresa");
            teFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
            teFormulaTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaTipoEntidad");
            teFormulaTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTEFormula");
            banderaTEFormulas = 0;
            filtrarListaTEFormulasConceptos = null;
            tipoListaTEFormulas = 0;
        }
        listTEFormulasConceptosBorrar.clear();
        listTEFormulasConceptosCrear.clear();
        listTEFormulasConceptosModificar.clear();
        indexTEFormulas = -1;
        secRegistroTEFormulas = null;
        k = 0;
        listaTEFormulasConceptos = null;
        guardadoTEFormulas = true;
        permitirIndexTEFormulas = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTEFormula");
    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTipoSueldo = listaTiposSueldos.get(index);
            }
            if (tipoLista == 1) {
                editarTipoSueldo = filtrarListaTiposSueldos.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoTipoSueldoD");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoTipoSueldoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionTipoSueldoD");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionTipoSueldoD').show()");
                cualCelda = -1;
            }
            index = -1;
            secRegistro = null;
        }
        if (indexTSFormulas >= 0) {
            if (tipoListaTSFormulas == 0) {
                editarTSFormulaConcepto = listaTSFormulasConceptos.get(indexTSFormulas);
            }
            if (tipoListaTSFormulas == 1) {
                editarTSFormulaConcepto = listaTSFormulasConceptos.get(indexTSFormulas);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaTSFormulas == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaTSFormulaD");
                RequestContext.getCurrentInstance().execute("PF('editarFormulaTSFormulaD').show()");
                cualCeldaTSFormulas = -1;
            } else if (cualCeldaTSFormulas == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoTSFormulaD");
                RequestContext.getCurrentInstance().execute("PF('editarConceptoTSFormulaD').show()");
                cualCeldaTSFormulas = -1;
            } else if (cualCeldaTSFormulas == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaTSFormulaD");
                RequestContext.getCurrentInstance().execute("PF('editarEmpresaTSFormulaD').show()");
                cualCeldaTSFormulas = -1;
            }
            indexTSFormulas = -1;
            secRegistroTSFormulas = null;
        }
        if (indexTSGrupos >= 0) {
            if (tipoListaTSGrupos == 0) {
                editarTSGrupoTipoEntidad = listaTSGruposTiposEntidades.get(indexTSGrupos);
            }
            if (tipoListaTSGrupos == 1) {
                editarTSGrupoTipoEntidad = listaTSGruposTiposEntidades.get(indexTSGrupos);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaTSGrupos == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarGrupoTSGrupoD");
                RequestContext.getCurrentInstance().execute("PF('editarGrupoTSGrupoD').show()");
                cualCeldaTSGrupos = -1;
            }
            indexTSGrupos = -1;
            secRegistroTSGrupos = null;
        }
        if (indexTEFormulas >= 0) {
            if (tipoListaTEFormulas == 0) {
                editarTEFormulaConcepto = listaTEFormulasConceptos.get(indexTEFormulas);
            }
            if (tipoListaTEFormulas == 1) {
                editarTEFormulaConcepto = listaTEFormulasConceptos.get(indexTEFormulas);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaTEFormulas == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaTETipoEntidadD");
                RequestContext.getCurrentInstance().execute("PF('editarFormulaTETipoEntidadD').show()");
                cualCeldaTEFormulas = -1;
            } else if (cualCeldaTEFormulas == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaTEFormulaD");
                RequestContext.getCurrentInstance().execute("PF('editarFormulaTEFormulaD').show()");
                cualCeldaTEFormulas = -1;
            } else if (cualCeldaTEFormulas == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoTEFormulaD");
                RequestContext.getCurrentInstance().execute("PF('editarConceptoTEFormulaD').show()");
                cualCeldaTEFormulas = -1;
            } else if (cualCeldaTEFormulas == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaTEFormulaD");
                RequestContext.getCurrentInstance().execute("PF('editarEmpresaTEFormulaD').show()");
                cualCeldaTEFormulas = -1;
            }
            indexTEFormulas = -1;
            secRegistroTEFormulas = null;
        }
    }

    public void dialogoNuevoRegistro() {
        if (guardado == false || guardadoTSFormulas == false || guardadoTSGrupos == false || guardadoTEFormulas == false) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            int tam1 = listaTiposSueldos.size();
            int tam2 = listaTSFormulasConceptos.size();
            int tam3 = listaTSGruposTiposEntidades.size();
            int tam4 = listaTEFormulasConceptos.size();
            if (tam1 == 0 || tam2 == 0 || tam3 == 0) {
                activoFormulaConcepto = false;
                activoGrupoDistribucion = false;
                activoTipoEntidad = true;
                RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
                RequestContext.getCurrentInstance().execute("PF('verificarNuevoRegistro').show()");
            } else {
                if (index >= 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroTipoSueldo");
                    RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoSueldo').show()");
                }
                if (indexTSFormulas >= 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroTSFormula");
                    RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTSFormula').show()");
                }
                if (indexTSGrupos >= 0) {
                    if (tam4 > 0) {
                        lovTiposEntidades = null;
                        getLovTiposEntidades();
                        RequestContext.getCurrentInstance().execute("PF('form:lovTipoEntidad').clearFilters()");
                        RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
                        RequestContext.getCurrentInstance().update("form:lovTipoEntidad");
                        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroTSGrupo");
                        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTSGrupo').show()");

                    } else {
                        activoFormulaConcepto = false;
                        activoGrupoDistribucion = false;
                        activoTipoEntidad = false;
                        RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
                        RequestContext.getCurrentInstance().execute("PF('verificarNuevoRegistro').show()");
                    }
                }
                if (indexTEFormulas >= 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroTEFormula");
                    RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTEFormula').show()");
                }
            }
        }
    }

    //CREAR 
    public void agregarNuevoTipoSueldo() {
        boolean respueta = validarCamposNulosTipoSueldo(1);
        if (respueta == true) {
            int tamDes = 0;
            tamDes = nuevoTipoSueldo.getDescripcion().length();
            if (tamDes >= 1 && tamDes <= 30) {
                if (bandera == 1) {
                    altoTablaTiposSueldos = "80";
                    tipoSueldoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCodigo");
                    tipoSueldoCodigo.setFilterStyle("display: none; visibility: hidden;");
                    tipoSueldoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoDescripcion");
                    tipoSueldoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    tipoSueldoCap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCap");
                    tipoSueldoCap.setFilterStyle("display: none; visibility: hidden;");
                    tipoSueldoBas = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoBas");
                    tipoSueldoBas.setFilterStyle("display: none; visibility: hidden;");
                    tipoSueldoAdi = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoAdi");
                    tipoSueldoAdi.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
                    bandera = 0;
                    filtrarListaTiposSueldos = null;
                    tipoLista = 0;
                }

                k++;
                l = BigInteger.valueOf(k);
                String text = nuevoTipoSueldo.getDescripcion().toUpperCase();
                nuevoTipoSueldo.setDescripcion(text);
                nuevoTipoSueldo.setSecuencia(l);
                listTiposSueldosCrear.add(nuevoTipoSueldo);
                listaTiposSueldos.add(nuevoTipoSueldo);
                nuevoTipoSueldo = new TiposSueldos();
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoSueldo').hide()");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:aceptar");
                }
                index = -1;
                secRegistro = null;
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorDescripcionTipoSueldo').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullTipoSueldo').show()");
        }
    }

    public void agregarNuevoTSFormula() {
        boolean respueta = validarCamposNulosTSFormula(1);
        if (respueta == true) {
            if (banderaTSFormulas == 1) {
                altoTablaTSFormulas = "230";
                tsFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaConcepto");
                tsFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
                tsFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaFormula");
                tsFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
                tsFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaEmpresa");
                tsFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
                tsFormulaOrigen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaOrigen");
                tsFormulaOrigen.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTSFormula");
                banderaTSFormulas = 0;
                filtrarListaTSFormulasConceptos = null;
                tipoListaTSFormulas = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTSFormulaConcepto.setSecuencia(l);
            if (tipoLista == 0) {
                nuevoTSFormulaConcepto.setTiposueldo(listaTiposSueldos.get(indexAux));
            }
            if (tipoLista == 1) {
                nuevoTSFormulaConcepto.setTiposueldo(filtrarListaTiposSueldos.get(indexAux));
            }
            if (listaTSFormulasConceptos.size() == 0) {
                listaTSFormulasConceptos = new ArrayList<TSFormulasConceptos>();
            }
            listTSFormulasConceptosCrear.add(nuevoTSFormulaConcepto);
            listaTSFormulasConceptos.add(nuevoTSFormulaConcepto);
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            index = indexAux;
            RequestContext.getCurrentInstance().update("form:datosTSFormula");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTSFormula').hide()");
            nuevoTSFormulaConcepto = new TSFormulasConceptos();
            nuevoTSFormulaConcepto.setFormula(new Formulas());
            nuevoTSFormulaConcepto.setConcepto(new Conceptos());
            nuevoTSFormulaConcepto.getConcepto().setEmpresa(new Empresas());
            if (guardadoTSFormulas == true) {
                guardadoTSFormulas = false;
                RequestContext.getCurrentInstance().update("form:aceptar");
            }
            indexTSFormulas = -1;
            secRegistroTSFormulas = null;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullTSFormula').show()");
        }
    }

    public void agregarNuevoTSGrupo() {
        if (nuevoTSGrupoTipoEntidad.getGrupotipoentidad().getSecuencia() != null) {
            if (banderaTSGrupos == 1) {
                altoTablaTSGrupos = "73";
                tsGrupoGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSGrupo:tsGrupoGrupo");
                tsGrupoGrupo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTSGrupo");
                banderaTSGrupos = 0;
                filtrarListaTSGruposTiposEntidades = null;
                tipoListaTSGrupos = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTSGrupoTipoEntidad.setSecuencia(l);
            if (tipoLista == 0) {
                nuevoTSGrupoTipoEntidad.setTiposueldo(listaTiposSueldos.get(indexAux));
            }
            if (tipoLista == 1) {
                nuevoTSGrupoTipoEntidad.setTiposueldo(filtrarListaTiposSueldos.get(indexAux));
            }
            if (listaTSGruposTiposEntidades.size() == 0) {
                listaTSGruposTiposEntidades = new ArrayList<TSGruposTiposEntidades>();
            }
            listTSGruposTiposEntidadesCrear.add(nuevoTSGrupoTipoEntidad);
            listaTSGruposTiposEntidades.add(nuevoTSGrupoTipoEntidad);
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            index = indexAux;
            RequestContext.getCurrentInstance().update("form:datosTSGrupo");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTSGrupo').hide()");
            nuevoTSGrupoTipoEntidad = new TSGruposTiposEntidades();
            nuevoTSGrupoTipoEntidad.setGrupotipoentidad(new Grupostiposentidades());
            if (guardadoTSGrupos == true) {
                guardadoTSGrupos = false;
                RequestContext.getCurrentInstance().update("form:aceptar");
            }
            indexTSGrupos = -1;
            secRegistroTSGrupos = null;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullTSGrupo').show()");
        }
    }

    public void agregarNuevoTEFormula() {
        boolean respueta = validarCamposNulosTEFormula(1);
        if (respueta == true) {
            if (validarTipoEntidadYEmpresaNuevoRegistro(1) == true) {
                if (banderaTEFormulas == 1) {
                    altoTablaTEFormulas = "73";
                    teFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaConcepto");
                    teFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
                    teFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaFormula");
                    teFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
                    teFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaEmpresa");
                    teFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
                    teFormulaTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaTipoEntidad");
                    teFormulaTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosTEFormula");
                    banderaTEFormulas = 0;
                    filtrarListaTEFormulasConceptos = null;
                    tipoListaTEFormulas = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevoTEFormulaConcepto.setSecuencia(l);
                if (tipoListaTSGrupos == 0) {
                    nuevoTEFormulaConcepto.setTsgrupotipoentidad(listaTSGruposTiposEntidades.get(indexAuxTSGrupos));
                }
                if (tipoListaTSGrupos == 1) {
                    nuevoTEFormulaConcepto.setTsgrupotipoentidad(listaTSGruposTiposEntidades.get(indexAuxTSGrupos));
                }
                if (listaTEFormulasConceptos.size() == 0) {
                    listaTEFormulasConceptos = new ArrayList<TEFormulasConceptos>();
                }
                listTEFormulasConceptosCrear.add(nuevoTEFormulaConcepto);
                listaTEFormulasConceptos.add(nuevoTEFormulaConcepto);
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

                RequestContext.getCurrentInstance().update("form:datosTEFormula");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTEFormula').hide()");
                nuevoTEFormulaConcepto = new TEFormulasConceptos();
                nuevoTEFormulaConcepto.setFormula(new Formulas());
                nuevoTEFormulaConcepto.setTipoentidad(new TiposEntidades());
                nuevoTEFormulaConcepto.setConcepto(new Conceptos());
                nuevoTEFormulaConcepto.getConcepto().setEmpresa(new Empresas());
                if (guardadoTEFormulas == true) {
                    guardadoTEFormulas = false;
                    RequestContext.getCurrentInstance().update("form:aceptar");
                }
                indexTEFormulas = -1;
                secRegistroTEFormulas = null;
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorNuevoRegistroTEFormula').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullTEFormula').show()");
        }
    }
    //LIMPIAR NUEVO REGISTRO

    /**
     */
    public void limpiarNuevaTipoSueldo() {
        nuevoTipoSueldo = new TiposSueldos();
        index = -1;
        secRegistro = null;
    }

    public void limpiarNuevaTSFormula() {
        nuevoTSFormulaConcepto = new TSFormulasConceptos();
        nuevoTSFormulaConcepto.setFormula(new Formulas());
        nuevoTSFormulaConcepto.setConcepto(new Conceptos());
        nuevoTSFormulaConcepto.getConcepto().setEmpresa(new Empresas());
        indexTSFormulas = -1;
        secRegistroTSFormulas = null;
    }

    public void limpiarNuevaTSGrupo() {
        nuevoTSGrupoTipoEntidad = new TSGruposTiposEntidades();
        nuevoTSGrupoTipoEntidad.setGrupotipoentidad(new Grupostiposentidades());
        indexTSGrupos = -1;
        secRegistroTSGrupos = null;
    }

    public void limpiarNuevaTEFormula() {
        nuevoTEFormulaConcepto = new TEFormulasConceptos();
        nuevoTEFormulaConcepto.setFormula(new Formulas());
        nuevoTEFormulaConcepto.setTipoentidad(new TiposEntidades());
        nuevoTEFormulaConcepto.setConcepto(new Conceptos());
        nuevoTEFormulaConcepto.getConcepto().setEmpresa(new Empresas());
        indexTEFormulas = -1;
        secRegistroTEFormulas = null;
    }

    //DUPLICAR VC
    /**
     */
    public void verificarRegistroDuplicar() {
        if (index >= 0) {
            duplicarTipoSueldoM();
        }
        if (indexTSFormulas >= 0) {
            duplicarTSFormulaM();
        }
        if (indexTSGrupos >= 0) {
            duplicarTSGrupoM();
        }
        if (indexTEFormulas >= 0) {
            duplicarTEFormulaM();
        }
    }

    public void duplicarTipoSueldoM() {
        duplicarTipoSueldo = new TiposSueldos();
        k++;
        l = BigInteger.valueOf(k);
        if (tipoLista == 0) {
            duplicarTipoSueldo.setCodigo(listaTiposSueldos.get(index).getCodigo());
            duplicarTipoSueldo.setDescripcion(listaTiposSueldos.get(index).getDescripcion());
            duplicarTipoSueldo.setBasico(listaTiposSueldos.get(index).getBasico());
            duplicarTipoSueldo.setCapacidadendeudamiento(listaTiposSueldos.get(index).getCapacidadendeudamiento());
            duplicarTipoSueldo.setAdicionalbasico(listaTiposSueldos.get(index).getAdicionalbasico());
        }
        if (tipoLista == 1) {
            duplicarTipoSueldo.setCodigo(filtrarListaTiposSueldos.get(index).getCodigo());
            duplicarTipoSueldo.setDescripcion(filtrarListaTiposSueldos.get(index).getDescripcion());
            duplicarTipoSueldo.setBasico(filtrarListaTiposSueldos.get(index).getBasico());
            duplicarTipoSueldo.setCapacidadendeudamiento(filtrarListaTiposSueldos.get(index).getCapacidadendeudamiento());
            duplicarTipoSueldo.setAdicionalbasico(filtrarListaTiposSueldos.get(index).getAdicionalbasico());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTipoSueldo");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoSueldo').show()");
        index = -1;
        secRegistro = null;

    }

    public void duplicarTSFormulaM() {
        duplicarTSFormulaConcepto = new TSFormulasConceptos();
        k++;
        l = BigInteger.valueOf(k);
        if (tipoListaTSFormulas == 0) {
            duplicarTSFormulaConcepto.setOrigen(listaTSFormulasConceptos.get(indexTSFormulas).getOrigen());
            duplicarTSFormulaConcepto.setFormula(listaTSFormulasConceptos.get(indexTSFormulas).getFormula());
            duplicarTSFormulaConcepto.setConcepto(listaTSFormulasConceptos.get(indexTSFormulas).getConcepto());
        }
        if (tipoListaTSFormulas == 1) {
            duplicarTSFormulaConcepto.setOrigen(filtrarListaTSFormulasConceptos.get(indexTSFormulas).getOrigen());
            duplicarTSFormulaConcepto.setFormula(filtrarListaTSFormulasConceptos.get(indexTSFormulas).getFormula());
            duplicarTSFormulaConcepto.setConcepto(filtrarListaTSFormulasConceptos.get(indexTSFormulas).getConcepto());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTSFormula");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTSFormula').show()");
        indexTSFormulas = -1;
        secRegistroTSFormulas = null;

    }

    public void duplicarTSGrupoM() {
        duplicarTSGrupoTipoEntidad = new TSGruposTiposEntidades();
        k++;
        l = BigInteger.valueOf(k);
        if (tipoListaTSGrupos == 0) {
            duplicarTSGrupoTipoEntidad.setGrupotipoentidad(listaTSGruposTiposEntidades.get(indexTSGrupos).getGrupotipoentidad());
        }
        if (tipoListaTSGrupos == 1) {
            duplicarTSGrupoTipoEntidad.setGrupotipoentidad(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos).getGrupotipoentidad());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTSGrupo");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTSGrupo').show()");
        indexTSGrupos = -1;
        secRegistroTSGrupos = null;

    }

    public void duplicarTEFormulaM() {
        duplicarTEFormulaConcepto = new TEFormulasConceptos();

        if (tipoListaTEFormulas == 0) {

            duplicarTEFormulaConcepto.setTipoentidad(listaTEFormulasConceptos.get(indexTEFormulas).getTipoentidad());
            duplicarTEFormulaConcepto.setFormula(listaTEFormulasConceptos.get(indexTEFormulas).getFormula());
            duplicarTEFormulaConcepto.setConcepto(listaTEFormulasConceptos.get(indexTEFormulas).getConcepto());
        }
        if (tipoListaTEFormulas == 1) {

            duplicarTEFormulaConcepto.setTipoentidad(filtrarListaTEFormulasConceptos.get(indexTEFormulas).getTipoentidad());
            duplicarTEFormulaConcepto.setFormula(filtrarListaTEFormulasConceptos.get(indexTEFormulas).getFormula());
            duplicarTEFormulaConcepto.setConcepto(filtrarListaTEFormulasConceptos.get(indexTEFormulas).getConcepto());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTEFormula");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTEFormula').show()");
        indexTEFormulas = -1;
        secRegistroTEFormulas = null;

    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
     */
    public void confirmarDuplicarTipoSueldo() {
        boolean respueta = validarCamposNulosTipoSueldo(2);
        if (respueta == true) {
            int tamDes = 0;
            tamDes = nuevoTipoSueldo.getDescripcion().length();
            if (tamDes >= 1 && tamDes <= 30) {
                if (bandera == 1) {
                    altoTablaTiposSueldos = "80";
                    tipoSueldoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCodigo");
                    tipoSueldoCodigo.setFilterStyle("display: none; visibility: hidden;");
                    tipoSueldoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoDescripcion");
                    tipoSueldoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    tipoSueldoCap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCap");
                    tipoSueldoCap.setFilterStyle("display: none; visibility: hidden;");
                    tipoSueldoBas = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoBas");
                    tipoSueldoBas.setFilterStyle("display: none; visibility: hidden;");
                    tipoSueldoAdi = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoAdi");
                    tipoSueldoAdi.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
                    bandera = 0;
                    filtrarListaTiposSueldos = null;
                    tipoLista = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                duplicarTipoSueldo.setSecuencia(l);
                String text = duplicarTipoSueldo.getDescripcion().toUpperCase();
                duplicarTipoSueldo.setDescripcion(text);
                listaTiposSueldos.add(duplicarTipoSueldo);
                listTiposSueldosCrear.add(duplicarTipoSueldo);
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoSueldo').hide()");
                index = -1;
                secRegistro = null;
                if (guardado == true) {
                    guardado = false;
                    //RequestContext.getCurrentInstance().update("form:aceptar");
                }
                duplicarTipoSueldo = new TiposSueldos();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorDescripcionTipoSueldo').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullTipoSueldo').show()");
        }
    }

    public void confirmarDuplicarTSFormula() {
        boolean respueta = validarCamposNulosTSFormula(2);
        if (respueta == true) {
            if (banderaTSFormulas == 1) {
                altoTablaTSFormulas = "230";
                tsFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaConcepto");
                tsFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
                tsFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaFormula");
                tsFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
                tsFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaEmpresa");
                tsFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
                tsFormulaOrigen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaOrigen");
                tsFormulaOrigen.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTSFormula");
                banderaTSFormulas = 0;
                filtrarListaTSFormulasConceptos = null;
                tipoListaTSFormulas = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarTSFormulaConcepto.setSecuencia(l);
            if (tipoLista == 0) {
                duplicarTSFormulaConcepto.setTiposueldo(listaTiposSueldos.get(indexAux));
            }
            if (tipoLista == 1) {
                duplicarTSFormulaConcepto.setTiposueldo(filtrarListaTiposSueldos.get(indexAux));
            }
            listaTSFormulasConceptos.add(duplicarTSFormulaConcepto);
            listTSFormulasConceptosCrear.add(duplicarTSFormulaConcepto);
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTSFormula");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTSFormula').hide()");
            indexTSFormulas = -1;
            secRegistroTSFormulas = null;
            if (guardadoTSFormulas == true) {
                guardadoTSFormulas = false;
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }

            duplicarTSFormulaConcepto = new TSFormulasConceptos();
            duplicarTSFormulaConcepto.setFormula(new Formulas());
            duplicarTSFormulaConcepto.setConcepto(new Conceptos());
            duplicarTSFormulaConcepto.getConcepto().setEmpresa(new Empresas());
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullTSFormula').show()");
        }
    }

    public void confirmarDuplicarTSGrupo() {
        if (duplicarTSGrupoTipoEntidad.getGrupotipoentidad().getSecuencia() != null) {
            if (banderaTSGrupos == 1) {
                altoTablaTSGrupos = "73";
                tsGrupoGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSGrupo:tsGrupoGrupo");
                tsGrupoGrupo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTSGrupo");
                banderaTSGrupos = 0;
                filtrarListaTSGruposTiposEntidades = null;
                tipoListaTSGrupos = 0;
            }
            if (tipoLista == 0) {
                duplicarTSGrupoTipoEntidad.setTiposueldo(listaTiposSueldos.get(indexAux));
            }
            if (tipoLista == 1) {
                duplicarTSGrupoTipoEntidad.setTiposueldo(filtrarListaTiposSueldos.get(indexAux));
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarTSGrupoTipoEntidad.setSecuencia(l);
            listaTSGruposTiposEntidades.add(duplicarTSGrupoTipoEntidad);
            listTSGruposTiposEntidadesCrear.add(duplicarTSGrupoTipoEntidad);
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTSGrupo");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTSGrupo').hide()");
            indexTSGrupos = -1;
            secRegistroTSGrupos = null;
            if (guardadoTSGrupos == true) {
                guardadoTSGrupos = false;
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }

            duplicarTSGrupoTipoEntidad = new TSGruposTiposEntidades();
            duplicarTSGrupoTipoEntidad.setGrupotipoentidad(new Grupostiposentidades());
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullTSGrupo').show()");
        }
    }

    public void confirmarDuplicarTEFormula() {
        boolean respueta = validarCamposNulosTEFormula(2);
        if (respueta == true) {
            if (validarTipoEntidadYEmpresaNuevoRegistro(2) == true) {
                if (banderaTEFormulas == 1) {
                    altoTablaTEFormulas = "73";
                    teFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaConcepto");
                    teFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
                    teFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaFormula");
                    teFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
                    teFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaEmpresa");
                    teFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
                    teFormulaTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaTipoEntidad");
                    teFormulaTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosTEFormula");
                    banderaTEFormulas = 0;
                    filtrarListaTEFormulasConceptos = null;
                    tipoListaTEFormulas = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                duplicarTEFormulaConcepto.setSecuencia(l);
                if (tipoListaTSGrupos == 0) {
                    duplicarTEFormulaConcepto.setTsgrupotipoentidad(listaTSGruposTiposEntidades.get(indexAuxTSGrupos));
                }
                if (tipoListaTSGrupos == 1) {
                    duplicarTEFormulaConcepto.setTsgrupotipoentidad(listaTSGruposTiposEntidades.get(indexAuxTSGrupos));
                }
                listaTEFormulasConceptos.add(duplicarTEFormulaConcepto);
                listTEFormulasConceptosCrear.add(duplicarTEFormulaConcepto);
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosTEFormula");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTEFormula').hide()");
                indexTEFormulas = -1;
                secRegistroTEFormulas = null;
                if (guardadoTEFormulas == true) {
                    guardadoTEFormulas = false;
                    //RequestContext.getCurrentInstance().update("form:aceptar");
                }
                duplicarTEFormulaConcepto = new TEFormulasConceptos();
                duplicarTEFormulaConcepto.setFormula(new Formulas());
                duplicarTEFormulaConcepto.setTipoentidad(new TiposEntidades());
                duplicarTEFormulaConcepto.setConcepto(new Conceptos());
                duplicarTEFormulaConcepto.getConcepto().setEmpresa(new Empresas());
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorNuevoRegistroTEFormula').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullTEFormula').show()");
        }
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar Set
     */
    public void limpiarDuplicarTipoSueldo() {
        duplicarTipoSueldo = new TiposSueldos();
    }

    public void limpiarDuplicarTSFormula() {
        duplicarTSFormulaConcepto = new TSFormulasConceptos();
        duplicarTSFormulaConcepto.setFormula(new Formulas());
        duplicarTSFormulaConcepto.setConcepto(new Conceptos());
        duplicarTSFormulaConcepto.getConcepto().setEmpresa(new Empresas());
    }

    public void limpiarDuplicarTSGrupo() {
        duplicarTSGrupoTipoEntidad = new TSGruposTiposEntidades();
        duplicarTSGrupoTipoEntidad.setGrupotipoentidad(new Grupostiposentidades());
    }

    public void limpiarDuplicarTEFormula() {
        duplicarTEFormulaConcepto = new TEFormulasConceptos();
        duplicarTEFormulaConcepto.setFormula(new Formulas());
        duplicarTEFormulaConcepto.setTipoentidad(new TiposEntidades());
        duplicarTEFormulaConcepto.setConcepto(new Conceptos());
        duplicarTEFormulaConcepto.getConcepto().setEmpresa(new Empresas());
    }

    public void limpiarMSNRastros() {
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";
    }

    //BORRAR VC
    /**
     */
    public void verificarRegistroBorrar() {
        if (index >= 0) {
            int tam = listaTSFormulasConceptos.size();
            if (tam == 0) {
                borrarTipoSueldo();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistro').show()");
            }
        }
        if (indexTSFormulas >= 0) {
            borrarTSFormula();
        }
        if (indexTSGrupos >= 0) {
            int tam = listaTEFormulasConceptos.size();
            if (tam == 0) {
                borrarTSGrupo();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistroTSGrupo').show()");
            }
        }
        if (indexTEFormulas >= 0) {
            borrarTEFormula();
        }
    }

    public void borrarTipoSueldo() {
        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listTiposSueldosModificar.isEmpty() && listTiposSueldosModificar.contains(listaTiposSueldos.get(index))) {
                    int modIndex = listTiposSueldosModificar.indexOf(listaTiposSueldos.get(index));
                    listTiposSueldosModificar.remove(modIndex);
                    listTiposSueldosBorrar.add(listaTiposSueldos.get(index));
                } else if (!listTiposSueldosCrear.isEmpty() && listTiposSueldosCrear.contains(listaTiposSueldos.get(index))) {
                    int crearIndex = listTiposSueldosCrear.indexOf(listaTiposSueldos.get(index));
                    listTiposSueldosCrear.remove(crearIndex);
                } else {
                    listTiposSueldosBorrar.add(listaTiposSueldos.get(index));
                }
                listaTiposSueldos.remove(index);
            }
            if (tipoLista == 1) {
                if (!listTiposSueldosModificar.isEmpty() && listTiposSueldosModificar.contains(filtrarListaTiposSueldos.get(index))) {
                    int modIndex = listTiposSueldosModificar.indexOf(filtrarListaTiposSueldos.get(index));
                    listTiposSueldosModificar.remove(modIndex);
                    listTiposSueldosBorrar.add(filtrarListaTiposSueldos.get(index));
                } else if (!listTiposSueldosCrear.isEmpty() && listTiposSueldosCrear.contains(filtrarListaTiposSueldos.get(index))) {
                    int crearIndex = listTiposSueldosCrear.indexOf(filtrarListaTiposSueldos.get(index));
                    listTiposSueldosCrear.remove(crearIndex);
                } else {
                    listTiposSueldosBorrar.add(filtrarListaTiposSueldos.get(index));
                }
                int VCIndex = listaTiposSueldos.indexOf(filtrarListaTiposSueldos.get(index));
                listaTiposSueldos.remove(VCIndex);
                filtrarListaTiposSueldos.remove(index);
            }

            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
        }
    }

    public void borrarTSFormula() {
        if (indexTSFormulas >= 0) {
            if (tipoListaTSFormulas == 0) {
                if (!listTSFormulasConceptosModificar.isEmpty() && listTSFormulasConceptosModificar.contains(listaTSFormulasConceptos.get(indexTSFormulas))) {
                    int modIndex = listTSFormulasConceptosModificar.indexOf(listaTSFormulasConceptos.get(indexTSFormulas));
                    listTSFormulasConceptosModificar.remove(modIndex);
                    listTSFormulasConceptosBorrar.add(listaTSFormulasConceptos.get(indexTSFormulas));
                } else if (!listTSFormulasConceptosCrear.isEmpty() && listTSFormulasConceptosCrear.contains(listaTSFormulasConceptos.get(indexTSFormulas))) {
                    int crearIndex = listTSFormulasConceptosCrear.indexOf(listaTSFormulasConceptos.get(indexTSFormulas));
                    listTSFormulasConceptosCrear.remove(crearIndex);
                } else {
                    listTSFormulasConceptosBorrar.add(listaTSFormulasConceptos.get(indexTSFormulas));
                }
                listaTSFormulasConceptos.remove(indexTSFormulas);
            }
            if (tipoListaTSFormulas == 1) {
                if (!listTSFormulasConceptosModificar.isEmpty() && listTSFormulasConceptosModificar.contains(filtrarListaTSFormulasConceptos.get(indexTSFormulas))) {
                    int modIndex = listTSFormulasConceptosModificar.indexOf(filtrarListaTSFormulasConceptos.get(indexTSFormulas));
                    listTSFormulasConceptosModificar.remove(modIndex);
                    listTSFormulasConceptosBorrar.add(filtrarListaTSFormulasConceptos.get(indexTSFormulas));
                } else if (!listTSFormulasConceptosCrear.isEmpty() && listTSFormulasConceptosCrear.contains(filtrarListaTSFormulasConceptos.get(indexTSFormulas))) {
                    int crearIndex = listTSFormulasConceptosCrear.indexOf(filtrarListaTSFormulasConceptos.get(indexTSFormulas));
                    listTSFormulasConceptosCrear.remove(crearIndex);
                } else {
                    listTSFormulasConceptosBorrar.add(filtrarListaTSFormulasConceptos.get(indexTSFormulas));
                }
                int VCIndex = listaTSFormulasConceptos.indexOf(filtrarListaTSFormulasConceptos.get(indexTSFormulas));
                listaTSFormulasConceptos.remove(VCIndex);
                filtrarListaTSFormulasConceptos.remove(indexTSFormulas);
            }

            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTSFormula");
            indexTSFormulas = -1;
            secRegistroTSFormulas = null;

            if (guardadoTSFormulas == true) {
                guardadoTSFormulas = false;
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
        }
    }

    public void borrarTSGrupo() {
        if (indexTSGrupos >= 0) {
            if (tipoListaTSGrupos == 0) {
                if (!listTSGruposTiposEntidadesModificar.isEmpty() && listTSGruposTiposEntidadesModificar.contains(listaTSGruposTiposEntidades.get(indexTSGrupos))) {
                    int modIndex = listTSGruposTiposEntidadesModificar.indexOf(listaTSGruposTiposEntidades.get(indexTSGrupos));
                    listTSGruposTiposEntidadesModificar.remove(modIndex);
                    listTSGruposTiposEntidadesBorrar.add(listaTSGruposTiposEntidades.get(indexTSGrupos));
                } else if (!listTSGruposTiposEntidadesCrear.isEmpty() && listTSGruposTiposEntidadesCrear.contains(listaTSGruposTiposEntidades.get(indexTSGrupos))) {
                    int crearIndex = listTSGruposTiposEntidadesCrear.indexOf(listaTSGruposTiposEntidades.get(indexTSGrupos));
                    listTSGruposTiposEntidadesCrear.remove(crearIndex);
                } else {
                    listTSGruposTiposEntidadesBorrar.add(listaTSGruposTiposEntidades.get(indexTSGrupos));
                }
                listaTSGruposTiposEntidades.remove(indexTSGrupos);
            }
            if (tipoListaTSGrupos == 1) {
                if (!listTSGruposTiposEntidadesModificar.isEmpty() && listTSGruposTiposEntidadesModificar.contains(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos))) {
                    int modIndex = listTSGruposTiposEntidadesModificar.indexOf(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos));
                    listTSGruposTiposEntidadesModificar.remove(modIndex);
                    listTSGruposTiposEntidadesBorrar.add(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos));
                } else if (!listTSGruposTiposEntidadesCrear.isEmpty() && listTSGruposTiposEntidadesCrear.contains(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos))) {
                    int crearIndex = listTSGruposTiposEntidadesCrear.indexOf(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos));
                    listTSGruposTiposEntidadesCrear.remove(crearIndex);
                } else {
                    listTSGruposTiposEntidadesBorrar.add(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos));
                }
                int VCIndex = listaTSGruposTiposEntidades.indexOf(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos));
                listaTSGruposTiposEntidades.remove(VCIndex);
                filtrarListaTSGruposTiposEntidades.remove(indexTSGrupos);
            }

            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTSGrupo");
            indexTSGrupos = -1;
            secRegistroTSGrupos = null;

            if (guardadoTSGrupos == true) {
                guardadoTSGrupos = false;
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
        }
    }

    public void borrarTEFormula() {
        if (indexTEFormulas >= 0) {
            if (tipoListaTEFormulas == 0) {
                if (!listTEFormulasConceptosModificar.isEmpty() && listTEFormulasConceptosModificar.contains(listaTEFormulasConceptos.get(indexTEFormulas))) {
                    int modIndex = listTEFormulasConceptosModificar.indexOf(listaTEFormulasConceptos.get(indexTEFormulas));
                    listTEFormulasConceptosModificar.remove(modIndex);
                    listTEFormulasConceptosBorrar.add(listaTEFormulasConceptos.get(indexTEFormulas));
                } else if (!listTEFormulasConceptosCrear.isEmpty() && listTEFormulasConceptosCrear.contains(listaTEFormulasConceptos.get(indexTEFormulas))) {
                    int crearIndex = listTEFormulasConceptosCrear.indexOf(listaTEFormulasConceptos.get(indexTEFormulas));
                    listTEFormulasConceptosCrear.remove(crearIndex);
                } else {
                    listTEFormulasConceptosBorrar.add(listaTEFormulasConceptos.get(indexTEFormulas));
                }
                listaTEFormulasConceptos.remove(indexTEFormulas);
            }
            if (tipoListaTEFormulas == 1) {
                if (!listTEFormulasConceptosModificar.isEmpty() && listTEFormulasConceptosModificar.contains(filtrarListaTEFormulasConceptos.get(indexTEFormulas))) {
                    int modIndex = listTEFormulasConceptosModificar.indexOf(filtrarListaTEFormulasConceptos.get(indexTEFormulas));
                    listTEFormulasConceptosModificar.remove(modIndex);
                    listTEFormulasConceptosBorrar.add(filtrarListaTEFormulasConceptos.get(indexTEFormulas));
                } else if (!listTEFormulasConceptosCrear.isEmpty() && listTEFormulasConceptosCrear.contains(filtrarListaTEFormulasConceptos.get(indexTEFormulas))) {
                    int crearIndex = listTEFormulasConceptosCrear.indexOf(filtrarListaTEFormulasConceptos.get(indexTEFormulas));
                    listTEFormulasConceptosCrear.remove(crearIndex);
                } else {
                    listTEFormulasConceptosBorrar.add(filtrarListaTEFormulasConceptos.get(indexTEFormulas));
                }
                int VCIndex = listaTEFormulasConceptos.indexOf(filtrarListaTEFormulasConceptos.get(indexTEFormulas));
                listaTEFormulasConceptos.remove(VCIndex);
                filtrarListaTEFormulasConceptos.remove(indexTEFormulas);
            }

            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTEFormula");
            indexTEFormulas = -1;
            secRegistroTEFormulas = null;

            if (guardadoTEFormulas == true) {
                guardadoTEFormulas = false;
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
        }
    }
    //CTRL + F11 ACTIVAR/DESACTIVAR

    /**
     * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        if (index >= 0) {
            if (bandera == 0) {
                altoTablaTiposSueldos = "60";
                tipoSueldoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCodigo");
                tipoSueldoCodigo.setFilterStyle("width: 85% !important;");
                tipoSueldoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoDescripcion");
                tipoSueldoDescripcion.setFilterStyle("width: 85% !important;");
                tipoSueldoCap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCap");
                tipoSueldoCap.setFilterStyle("width: 85% !important;");
                tipoSueldoBas = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoBas");
                tipoSueldoBas.setFilterStyle("width: 85% !important;");
                tipoSueldoAdi = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoAdi");
                tipoSueldoAdi.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
                bandera = 1;
            } else {
                altoTablaTiposSueldos = "80";
                tipoSueldoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCodigo");
                tipoSueldoCodigo.setFilterStyle("display: none; visibility: hidden;");
                tipoSueldoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoDescripcion");
                tipoSueldoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                tipoSueldoCap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCap");
                tipoSueldoCap.setFilterStyle("display: none; visibility: hidden;");
                tipoSueldoBas = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoBas");
                tipoSueldoBas.setFilterStyle("display: none; visibility: hidden;");
                tipoSueldoAdi = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoAdi");
                tipoSueldoAdi.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
                bandera = 0;
                filtrarListaTiposSueldos = null;
                tipoLista = 0;
            }
        }
        if (indexTSFormulas >= 0) {
            if (banderaTSFormulas == 0) {
                altoTablaTSFormulas = "208";
                tsFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaConcepto");
                tsFormulaConcepto.setFilterStyle("width: 85% !important;");
                tsFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaFormula");
                tsFormulaFormula.setFilterStyle("width: 85% !important;");
                tsFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaEmpresa");
                tsFormulaEmpresa.setFilterStyle("width: 85% !important;");
                tsFormulaOrigen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaOrigen");
                tsFormulaOrigen.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:datosTSFormula");
                banderaTSFormulas = 1;
            } else {
                altoTablaTSFormulas = "230";
                tsFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaConcepto");
                tsFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
                tsFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaFormula");
                tsFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
                tsFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaEmpresa");
                tsFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
                tsFormulaOrigen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaOrigen");
                tsFormulaOrigen.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTSFormula");
                banderaTSFormulas = 0;
                filtrarListaTSFormulasConceptos = null;
                tipoListaTSFormulas = 0;
            }
        }
        if (indexTSGrupos >= 0) {
            if (banderaTSGrupos == 0) {
                altoTablaTSGrupos = "51";
                tsGrupoGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSGrupo:tsGrupoGrupo");
                tsGrupoGrupo.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:datosTSGrupo");
                banderaTSGrupos = 1;
            } else {
                altoTablaTSGrupos = "73";
                tsGrupoGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSGrupo:tsGrupoGrupo");
                tsGrupoGrupo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTSGrupo");
                banderaTSGrupos = 0;
                filtrarListaTSGruposTiposEntidades = null;
                tipoListaTSGrupos = 0;
            }
        }
        if (indexTEFormulas >= 0) {
            if (banderaTEFormulas == 0) {
                altoTablaTEFormulas = "51";
                teFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaConcepto");
                teFormulaConcepto.setFilterStyle("width: 85% !important;");
                teFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaFormula");
                teFormulaFormula.setFilterStyle("width: 85% !important;");
                teFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaEmpresa");
                teFormulaEmpresa.setFilterStyle("width: 85% !important;");
                teFormulaTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaTipoEntidad");
                teFormulaTipoEntidad.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:datosTEFormula");
                banderaTEFormulas = 1;
            } else {
                altoTablaTEFormulas = "73";
                teFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaConcepto");
                teFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
                teFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaFormula");
                teFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
                teFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaEmpresa");
                teFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
                teFormulaTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaTipoEntidad");
                teFormulaTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTEFormula");
                banderaTEFormulas = 0;
                filtrarListaTEFormulasConceptos = null;
                tipoListaTEFormulas = 0;
            }
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (bandera == 1) {
            altoTablaTiposSueldos = "80";
            tipoSueldoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCodigo");
            tipoSueldoCodigo.setFilterStyle("display: none; visibility: hidden;");
            tipoSueldoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoDescripcion");
            tipoSueldoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            tipoSueldoCap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCap");
            tipoSueldoCap.setFilterStyle("display: none; visibility: hidden;");
            tipoSueldoBas = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoBas");
            tipoSueldoBas.setFilterStyle("display: none; visibility: hidden;");
            tipoSueldoAdi = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoAdi");
            tipoSueldoAdi.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
            bandera = 0;
            filtrarListaTiposSueldos = null;
            tipoLista = 0;
        }
        if (banderaTSFormulas == 1) {
            altoTablaTSFormulas = "230";
            tsFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaConcepto");
            tsFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
            tsFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaFormula");
            tsFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
            tsFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaEmpresa");
            tsFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
            tsFormulaOrigen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaOrigen");
            tsFormulaOrigen.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTSFormula");
            banderaTSFormulas = 0;
            filtrarListaTSFormulasConceptos = null;
            tipoListaTSFormulas = 0;
        }
        if (banderaTSGrupos == 1) {
            altoTablaTSGrupos = "73";
            tsGrupoGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSGrupo:tsGrupoGrupo");
            tsGrupoGrupo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTSGrupo");
            banderaTSGrupos = 0;
            filtrarListaTSGruposTiposEntidades = null;
            tipoListaTSGrupos = 0;
        }
        if (banderaTEFormulas == 1) {
            altoTablaTEFormulas = "73";
            teFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaConcepto");
            teFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
            teFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaFormula");
            teFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
            teFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaEmpresa");
            teFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
            teFormulaTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaTipoEntidad");
            teFormulaTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTEFormula");
            banderaTEFormulas = 0;
            filtrarListaTEFormulasConceptos = null;
            tipoListaTEFormulas = 0;
        }
        listTiposSueldosBorrar.clear();
        listTiposSueldosCrear.clear();
        listTiposSueldosModificar.clear();
        listTSFormulasConceptosBorrar.clear();
        listTSFormulasConceptosCrear.clear();
        listTSFormulasConceptosModificar.clear();
        listTSGruposTiposEntidadesBorrar.clear();
        listTSGruposTiposEntidadesCrear.clear();
        listTSGruposTiposEntidadesModificar.clear();
        listTEFormulasConceptosBorrar.clear();
        listTEFormulasConceptosCrear.clear();
        listTEFormulasConceptosModificar.clear();
        index = -1;
        indexAux = -1;
        indexTSFormulas = -1;
        indexTSGrupos = -1;
        indexAuxTSGrupos = -1;
        indexTEFormulas = -1;
        secRegistro = null;
        secRegistroTSFormulas = null;
        secRegistroTEFormulas = null;
        secRegistroTSGrupos = null;
        k = 0;
        listaTiposSueldos = null;
        listaTSFormulasConceptos = null;
        listaTEFormulasConceptos = null;
        listaTSGruposTiposEntidades = null;
        guardado = true;
        guardadoTSFormulas = true;
        guardadoTEFormulas = true;
        guardadoTSGrupos = true;
        cambiosPagina = true;
        lovConceptos = null;
        lovFormulas = null;
        lovGruposTiposEntidades = null;
        lovTiposEntidades = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (indexTSFormulas >= 0) {
            if (cualCeldaTSFormulas == 0) {
                RequestContext.getCurrentInstance().update("form:FormulaDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaTSFormulas == 1) {
                RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (indexTSGrupos >= 0) {
            if (cualCeldaTSGrupos == 0) {
                RequestContext.getCurrentInstance().update("form:GrupoDialogo");
                RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (indexTEFormulas >= 0) {
            if (cualCeldaTEFormulas == 0) {
                RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaTEFormulas == 1) {
                RequestContext.getCurrentInstance().update("form:FormulaTEDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaTEDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaTEFormulas == 2) {
                RequestContext.getCurrentInstance().update("form:ConceptoTEDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoTEDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void asignarIndex(Integer indice, int dlg, int LND, int tabla) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tabla == 1) {
            if (LND == 0) {
                indexTSFormulas = indice;
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dlg == 0) {
                RequestContext.getCurrentInstance().update("form:FormulaDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            }
            if (dlg == 1) {
                RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            }
        }
        if (tabla == 2) {
            if (LND == 0) {
                indexTSGrupos = indice;
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dlg == 0) {
                RequestContext.getCurrentInstance().update("form:GrupoDialogo");
                RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').show()");
            }
        }
        if (tabla == 3) {
            if (LND == 0) {
                indexTEFormulas = indice;
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dlg == 0) {
                RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
            }
            if (dlg == 1) {
                RequestContext.getCurrentInstance().update("form:FormulaTEDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaTEDialogo').show()");
            }
            if (dlg == 2) {
                RequestContext.getCurrentInstance().update("form:ConceptoTEDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoTEDialogo').show()");
            }
        }
    }

    public void valoresBackupAutocompletarGeneral(int tipoNuevo, String Campo) {
        if (Campo.equals("FORMULA")) {
            if (tipoNuevo == 1) {
                formula = nuevoTSFormulaConcepto.getFormula().getNombrelargo();
            } else if (tipoNuevo == 2) {
                formula = duplicarTSFormulaConcepto.getFormula().getNombrelargo();
            }
        }
        if (Campo.equals("CONCEPTO")) {
            if (tipoNuevo == 1) {
                concepto = nuevoTSFormulaConcepto.getConcepto().getDescripcion();
            } else if (tipoNuevo == 2) {
                concepto = duplicarTSFormulaConcepto.getConcepto().getDescripcion();
            }
        }
        if (Campo.equals("GRUPO")) {
            if (tipoNuevo == 1) {
                grupo = nuevoTSGrupoTipoEntidad.getGrupotipoentidad().getNombre();
            } else if (tipoNuevo == 2) {
                grupo = nuevoTSGrupoTipoEntidad.getGrupotipoentidad().getNombre();
            }
        }
        if (Campo.equals("FORMULATE")) {
            if (tipoNuevo == 1) {
                formula = nuevoTSFormulaConcepto.getFormula().getNombrelargo();
            } else if (tipoNuevo == 2) {
                formula = duplicarTSFormulaConcepto.getFormula().getNombrelargo();
            }
        }
        if (Campo.equals("CONCEPTOTE")) {
            if (tipoNuevo == 1) {
                concepto = nuevoTSFormulaConcepto.getConcepto().getDescripcion();
            } else if (tipoNuevo == 2) {
                concepto = duplicarTSFormulaConcepto.getConcepto().getDescripcion();
            }
        }
        if (Campo.equals("TIPOENTIDAD")) {
            if (tipoNuevo == 1) {
                tipoEntidad = nuevoTEFormulaConcepto.getTipoentidad().getNombre();
            } else if (tipoNuevo == 2) {
                tipoEntidad = duplicarTEFormulaConcepto.getTipoentidad().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoTSFormula(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            if (tipoNuevo == 1) {
                nuevoTSFormulaConcepto.getFormula().setNombrelargo(formula);
            } else if (tipoNuevo == 2) {
                duplicarTSFormulaConcepto.getFormula().setNombrelargo(formula);
            }
            for (int i = 0; i < lovFormulas.size(); i++) {
                if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoTSFormulaConcepto.setFormula(lovFormulas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaFormula");
                } else if (tipoNuevo == 2) {
                    duplicarTSFormulaConcepto.setFormula(lovFormulas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaFormula");
                }
                lovFormulas.clear();
                getLovFormulas();
            } else {
                RequestContext.getCurrentInstance().update("form:FormulaDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaFormula");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaFormula");
                }
            }
        }
        if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            if (tipoNuevo == 1) {
                nuevoTSFormulaConcepto.getConcepto().setDescripcion(concepto);
            } else if (tipoNuevo == 2) {
                duplicarTSFormulaConcepto.getConcepto().setDescripcion(concepto);
            }
            for (int i = 0; i < lovConceptos.size(); i++) {
                if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoTSFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaCConcepto");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaEmpresa");
                } else if (tipoNuevo == 2) {
                    duplicarTSFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaCConcepto");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaEmpresa");
                }
                lovConceptos.clear();
                getLovConceptos();
            } else {
                RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaCConcepto");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaCConcepto");
                }
            }
        }
    }

    public void autocompletarNuevoyDuplicadoTSGrupo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("GRUPO")) {
            if (tipoNuevo == 1) {
                nuevoTSGrupoTipoEntidad.getGrupotipoentidad().setNombre(grupo);
            } else if (tipoNuevo == 2) {
                duplicarTSGrupoTipoEntidad.getGrupotipoentidad().setNombre(grupo);
            }
            for (int i = 0; i < lovGruposTiposEntidades.size(); i++) {
                if (lovGruposTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoTSGrupoTipoEntidad.setGrupotipoentidad(lovGruposTiposEntidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSGrupoGrupo");
                } else if (tipoNuevo == 2) {
                    duplicarTSGrupoTipoEntidad.setGrupotipoentidad(lovGruposTiposEntidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSGrupoGrupo");
                }
                lovGruposTiposEntidades.clear();
                getLovGruposTiposEntidades();
            } else {
                RequestContext.getCurrentInstance().update("form:FormulaDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSGrupoGrupo");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSGrupoGrupo");
                }
            }
        }
    }

    public void autocompletarNuevoyDuplicadoTEFormula(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            if (tipoNuevo == 1) {
                nuevoTEFormulaConcepto.getTipoentidad().setNombre(formula);
            } else if (tipoNuevo == 2) {
                duplicarTEFormulaConcepto.getTipoentidad().setNombre(formula);
            }
            for (int i = 0; i < lovTiposEntidades.size(); i++) {
                if (lovTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoTEFormulaConcepto.setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaTipoEntidad");
                } else if (tipoNuevo == 2) {
                    duplicarTEFormulaConcepto.setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaTipoEntidad");
                }
                lovTiposEntidades.clear();
                getLovTiposEntidades();
            } else {
                RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaTipoEntidad");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaTipoEntidad");
                }
            }
        }
        if (confirmarCambio.equalsIgnoreCase("FORMULATE")) {
            if (tipoNuevo == 1) {
                nuevoTEFormulaConcepto.getFormula().setNombrelargo(formula);
            } else if (tipoNuevo == 2) {
                duplicarTEFormulaConcepto.getFormula().setNombrelargo(formula);
            }
            for (int i = 0; i < lovFormulas.size(); i++) {
                if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoTEFormulaConcepto.setFormula(lovFormulas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaFormula");
                } else if (tipoNuevo == 2) {
                    duplicarTEFormulaConcepto.setFormula(lovFormulas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaFormula");
                }
                lovFormulas.clear();
                getLovFormulas();
            } else {
                RequestContext.getCurrentInstance().update("form:FormulaTEDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaTEDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaFormula");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaFormula");
                }
            }
        }
        if (confirmarCambio.equalsIgnoreCase("CONCEPTOTE")) {
            if (tipoNuevo == 1) {
                nuevoTEFormulaConcepto.getConcepto().setDescripcion(concepto);
            } else if (tipoNuevo == 2) {
                duplicarTEFormulaConcepto.getConcepto().setDescripcion(concepto);
            }
            for (int i = 0; i < lovConceptos.size(); i++) {
                if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoTEFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaCConcepto");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaEmpresa");
                } else if (tipoNuevo == 2) {
                    duplicarTEFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaCConcepto");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaEmpresa");
                }
                lovConceptos.clear();
                getLovConceptos();
            } else {
                RequestContext.getCurrentInstance().update("form:ConceptoTEDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoTEDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaCConcepto");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaCConcepto");
                }
            }
        }
    }

    public void actualizarFormula() {
        if (tipoActualizacion == 0) {
            if (tipoListaTSFormulas == 0) {
                listaTSFormulasConceptos.get(indexTSFormulas).setFormula(formulaSeleccionado);
                if (!listTSFormulasConceptosCrear.contains(listaTSFormulasConceptos.get(indexTSFormulas))) {
                    if (listTSFormulasConceptosModificar.isEmpty()) {
                        listTSFormulasConceptosModificar.add(listaTSFormulasConceptos.get(indexTSFormulas));
                    } else if (!listTSFormulasConceptosModificar.contains(listaTSFormulasConceptos.get(indexTSFormulas))) {
                        listTSFormulasConceptosModificar.add(listaTSFormulasConceptos.get(indexTSFormulas));
                    }
                }
            } else {
                filtrarListaTSFormulasConceptos.get(indexTSFormulas).setFormula(formulaSeleccionado);
                if (!listTSFormulasConceptosCrear.contains(filtrarListaTSFormulasConceptos.get(indexTSFormulas))) {
                    if (listTSFormulasConceptosModificar.isEmpty()) {
                        listTSFormulasConceptosModificar.add(filtrarListaTSFormulasConceptos.get(indexTSFormulas));
                    } else if (!listTSFormulasConceptosModificar.contains(filtrarListaTSFormulasConceptos.get(indexTSFormulas))) {
                        listTSFormulasConceptosModificar.add(filtrarListaTSFormulasConceptos.get(indexTSFormulas));
                    }
                }
            }
            if (guardadoTSFormulas == true) {
                guardadoTSFormulas = false;
            }
            permitirIndexTSFormulas = true;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTSFormula");
        } else if (tipoActualizacion == 1) {
            nuevoTSFormulaConcepto.setFormula(formulaSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaFormula");
        } else if (tipoActualizacion == 2) {
            duplicarTSFormulaConcepto.setFormula(formulaSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaFormula");
        }
        filtrarLovFormulas = null;
        formulaSeleccionado = null;
        aceptar = true;
        indexTSFormulas = -1;
        secRegistroTSFormulas = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*RequestContext.getCurrentInstance().update("form:FormulaDialogo");
        RequestContext.getCurrentInstance().update("form:lovFormula");
        RequestContext.getCurrentInstance().update("form:aceptarF");*/
        context.reset("form:lovFormula:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovFormula').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').hide()");
    }

    public void cancelarCambioFormula() {
        filtrarLovFormulas = null;
        formulaSeleccionado = null;
        aceptar = true;
        indexTSFormulas = -1;
        secRegistroTSFormulas = null;
        tipoActualizacion = -1;
        permitirIndexTSFormulas = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovFormula:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovFormula').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').hide()");
    }

    public void actualizarConcepto() {
        if (tipoActualizacion == 0) {
            if (tipoListaTSFormulas == 0) {
                listaTSFormulasConceptos.get(indexTSFormulas).setConcepto(conceptoSeleccionado);
                if (!listTSFormulasConceptosCrear.contains(listaTSFormulasConceptos.get(indexTSFormulas))) {
                    if (listTSFormulasConceptosModificar.isEmpty()) {
                        listTSFormulasConceptosModificar.add(listaTSFormulasConceptos.get(indexTSFormulas));
                    } else if (!listTSFormulasConceptosModificar.contains(listaTSFormulasConceptos.get(indexTSFormulas))) {
                        listTSFormulasConceptosModificar.add(listaTSFormulasConceptos.get(indexTSFormulas));
                    }
                }
            } else {
                filtrarListaTSFormulasConceptos.get(indexTSFormulas).setConcepto(conceptoSeleccionado);
                if (!listTSFormulasConceptosCrear.contains(filtrarListaTSFormulasConceptos.get(indexTSFormulas))) {
                    if (listTSFormulasConceptosModificar.isEmpty()) {
                        listTSFormulasConceptosModificar.add(filtrarListaTSFormulasConceptos.get(indexTSFormulas));
                    } else if (!listTSFormulasConceptosModificar.contains(filtrarListaTSFormulasConceptos.get(indexTSFormulas))) {
                        listTSFormulasConceptosModificar.add(filtrarListaTSFormulasConceptos.get(indexTSFormulas));
                    }
                }
            }
            if (guardadoTSFormulas == true) {
                guardadoTSFormulas = false;
            }
            permitirIndexTSFormulas = true;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTSFormula");
        } else if (tipoActualizacion == 1) {
            nuevoTSFormulaConcepto.setConcepto(conceptoSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaCConcepto");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaEmpresa");
        } else if (tipoActualizacion == 2) {
            duplicarTSFormulaConcepto.setConcepto(conceptoSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaCConcepto");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaEmpresa");
        }
        filtrarLovConceptos = null;
        conceptoSeleccionado = null;
        aceptar = true;
        indexTSFormulas = -1;
        secRegistroTSFormulas = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
        RequestContext.getCurrentInstance().update("form:lovConcepto");
        RequestContext.getCurrentInstance().update("form:aceptarC");*/
        context.reset("form:lovConcepto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovConcepto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').hide()");
    }

    public void cancelarCambioConcepto() {
        filtrarLovConceptos = null;
        conceptoSeleccionado = null;
        aceptar = true;
        indexTSFormulas = -1;
        secRegistroTSFormulas = null;
        tipoActualizacion = -1;
        permitirIndexTSFormulas = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovConcepto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovConcepto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').hide()");
    }

    public void actualizarGrupo() {
        if (tipoActualizacion == 0) {
            if (tipoListaTSGrupos == 0) {
                listaTSGruposTiposEntidades.get(indexTSGrupos).setGrupotipoentidad(grupoTipoEntidadSeleccionado);
                if (!listTSGruposTiposEntidadesCrear.contains(listaTSGruposTiposEntidades.get(indexTSGrupos))) {
                    if (listTSGruposTiposEntidadesModificar.isEmpty()) {
                        listTSGruposTiposEntidadesModificar.add(listaTSGruposTiposEntidades.get(indexTSGrupos));
                    } else if (!listTSGruposTiposEntidadesModificar.contains(listaTSGruposTiposEntidades.get(indexTSGrupos))) {
                        listTSGruposTiposEntidadesModificar.add(listaTSGruposTiposEntidades.get(indexTSGrupos));
                    }
                }
            } else {
                filtrarListaTSGruposTiposEntidades.get(indexTSGrupos).setGrupotipoentidad(grupoTipoEntidadSeleccionado);
                if (!listTSGruposTiposEntidadesCrear.contains(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos))) {
                    if (listTSGruposTiposEntidadesModificar.isEmpty()) {
                        listTSGruposTiposEntidadesModificar.add(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos));
                    } else if (!listTSGruposTiposEntidadesModificar.contains(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos))) {
                        listTSGruposTiposEntidadesModificar.add(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos));
                    }
                }
            }
            if (guardadoTSGrupos == true) {
                guardadoTSGrupos = false;
            }
            permitirIndexTSGrupos = true;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTSGrupo");
        } else if (tipoActualizacion == 1) {
            nuevoTSGrupoTipoEntidad.setGrupotipoentidad(grupoTipoEntidadSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSGrupoGrupo");
        } else if (tipoActualizacion == 2) {
            duplicarTSGrupoTipoEntidad.setGrupotipoentidad(grupoTipoEntidadSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSGrupoGrupo");
        }
        filtrarLovGruposTiposEntidades = null;
        grupoTipoEntidadSeleccionado = null;
        aceptar = true;
        indexTSGrupos = -1;
        secRegistroTSGrupos = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*RequestContext.getCurrentInstance().update("form:GrupoDialogo");
         RequestContext.getCurrentInstance().update("form:lovGrupo");
         RequestContext.getCurrentInstance().update("form:aceptarG");*/
        context.reset("form:lovGrupo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovGrupo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').hide()");
    }

    public void cancelarCambioGrupo() {
        filtrarLovGruposTiposEntidades = null;
        grupoTipoEntidadSeleccionado = null;
        aceptar = true;
        indexTSGrupos = -1;
        secRegistroTSGrupos = null;
        tipoActualizacion = -1;
        permitirIndexTSGrupos = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovGrupo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovGrupo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').hide()");
    }

    public void actualizarTipoEntidad() {
        if (tipoActualizacion == 0) {
            if (tipoListaTEFormulas == 0) {
                listaTEFormulasConceptos.get(indexTEFormulas).setTipoentidad(tipoEntidadSeleccionado);
                if (!listTEFormulasConceptosCrear.contains(listaTEFormulasConceptos.get(indexTEFormulas))) {
                    if (listTEFormulasConceptosModificar.isEmpty()) {
                        listTEFormulasConceptosModificar.add(listaTEFormulasConceptos.get(indexTEFormulas));
                    } else if (!listTEFormulasConceptosModificar.contains(listaTEFormulasConceptos.get(indexTEFormulas))) {
                        listTEFormulasConceptosModificar.add(listaTEFormulasConceptos.get(indexTEFormulas));
                    }
                }
            } else {
                filtrarListaTEFormulasConceptos.get(indexTEFormulas).setTipoentidad(tipoEntidadSeleccionado);
                if (!listTEFormulasConceptosCrear.contains(filtrarListaTEFormulasConceptos.get(indexTEFormulas))) {
                    if (listTEFormulasConceptosModificar.isEmpty()) {
                        listTEFormulasConceptosModificar.add(filtrarListaTEFormulasConceptos.get(indexTEFormulas));
                    } else if (!listTEFormulasConceptosModificar.contains(filtrarListaTEFormulasConceptos.get(indexTEFormulas))) {
                        listTEFormulasConceptosModificar.add(filtrarListaTEFormulasConceptos.get(indexTEFormulas));
                    }
                }
            }
            if (guardadoTEFormulas == true) {
                guardadoTEFormulas = false;
            }
            permitirIndexTEFormulas = true;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTEFormula");
        } else if (tipoActualizacion == 1) {
            nuevoTEFormulaConcepto.setTipoentidad(tipoEntidadSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaTipoEntidad");
        } else if (tipoActualizacion == 2) {
            duplicarTEFormulaConcepto.setTipoentidad(tipoEntidadSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaTipoEntidad");
        }
        lovTiposEntidades = null;
        tipoEntidadSeleccionado = null;
        aceptar = true;
        indexTEFormulas = -1;
        secRegistroTEFormulas = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
         RequestContext.getCurrentInstance().update("form:lovTipoEntidad");
         RequestContext.getCurrentInstance().update("form:aceptarTE");*/
        context.reset("form:lovTipoEntidad:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoEntidad').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').hide()");
    }

    public void cancelarCambioTipoEntidad() {
        lovTiposEntidades = null;
        tipoEntidadSeleccionado = null;
        aceptar = true;
        indexTEFormulas = -1;
        secRegistroTEFormulas = null;
        tipoActualizacion = -1;
        permitirIndexTEFormulas = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoEntidad:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoEntidad').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').hide()");
    }

    public void actualizarFormulaTE() {
        if (tipoActualizacion == 0) {
            if (tipoListaTEFormulas == 0) {
                listaTEFormulasConceptos.get(indexTEFormulas).setFormula(formulaSeleccionado);
                if (!listTEFormulasConceptosCrear.contains(listaTEFormulasConceptos.get(indexTEFormulas))) {
                    if (listTEFormulasConceptosModificar.isEmpty()) {
                        listTEFormulasConceptosModificar.add(listaTEFormulasConceptos.get(indexTEFormulas));
                    } else if (!listTEFormulasConceptosModificar.contains(listaTEFormulasConceptos.get(indexTEFormulas))) {
                        listTEFormulasConceptosModificar.add(listaTEFormulasConceptos.get(indexTEFormulas));
                    }
                }
            } else {
                filtrarListaTEFormulasConceptos.get(indexTEFormulas).setFormula(formulaSeleccionado);
                if (!listTEFormulasConceptosCrear.contains(filtrarListaTEFormulasConceptos.get(indexTEFormulas))) {
                    if (listTEFormulasConceptosModificar.isEmpty()) {
                        listTEFormulasConceptosModificar.add(filtrarListaTEFormulasConceptos.get(indexTEFormulas));
                    } else if (!listTEFormulasConceptosModificar.contains(filtrarListaTEFormulasConceptos.get(indexTEFormulas))) {
                        listTEFormulasConceptosModificar.add(filtrarListaTEFormulasConceptos.get(indexTEFormulas));
                    }
                }
            }
            if (guardadoTEFormulas == true) {
                guardadoTEFormulas = false;
            }
            permitirIndexTEFormulas = true;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTEFormula");
        } else if (tipoActualizacion == 1) {
            nuevoTEFormulaConcepto.setFormula(formulaSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaFormula");
        } else if (tipoActualizacion == 2) {
            duplicarTEFormulaConcepto.setFormula(formulaSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaFormula");
        }
        filtrarLovFormulas = null;
        formulaSeleccionado = null;
        aceptar = true;
        indexTEFormulas = -1;
        secRegistroTEFormulas = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*RequestContext.getCurrentInstance().update("form:FormulaTEDialogo");
         RequestContext.getCurrentInstance().update("form:lovFormulaTE");
         RequestContext.getCurrentInstance().update("form:aceptarFTE");*/
        context.reset("form:lovFormulaTE:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovFormulaTE').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('FormulaTEDialogo').hide()");
    }

    public void cancelarCambioFormulaTE() {
        filtrarLovFormulas = null;
        formulaSeleccionado = null;
        aceptar = true;
        indexTEFormulas = -1;
        secRegistroTEFormulas = null;
        tipoActualizacion = -1;
        permitirIndexTEFormulas = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovFormulaTE:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovFormulaTE').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('FormulaTEDialogo').hide()");
    }

    public void actualizarConceptoTE() {
        if (tipoActualizacion == 0) {
            if (tipoListaTEFormulas == 0) {
                listaTEFormulasConceptos.get(indexTEFormulas).setConcepto(conceptoSeleccionado);
                if (!listTEFormulasConceptosCrear.contains(listaTEFormulasConceptos.get(indexTEFormulas))) {
                    if (listTEFormulasConceptosModificar.isEmpty()) {
                        listTEFormulasConceptosModificar.add(listaTEFormulasConceptos.get(indexTEFormulas));
                    } else if (!listTEFormulasConceptosModificar.contains(listaTEFormulasConceptos.get(indexTEFormulas))) {
                        listTEFormulasConceptosModificar.add(listaTEFormulasConceptos.get(indexTEFormulas));
                    }
                }
            } else {
                filtrarListaTEFormulasConceptos.get(indexTEFormulas).setConcepto(conceptoSeleccionado);
                if (!listTEFormulasConceptosCrear.contains(filtrarListaTEFormulasConceptos.get(indexTEFormulas))) {
                    if (listTEFormulasConceptosModificar.isEmpty()) {
                        listTEFormulasConceptosModificar.add(filtrarListaTEFormulasConceptos.get(indexTEFormulas));
                    } else if (!listTEFormulasConceptosModificar.contains(filtrarListaTEFormulasConceptos.get(indexTEFormulas))) {
                        listTEFormulasConceptosModificar.add(filtrarListaTEFormulasConceptos.get(indexTEFormulas));
                    }
                }
            }
            if (guardadoTEFormulas == true) {
                guardadoTEFormulas = false;
            }
            permitirIndexTEFormulas = true;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTEFormula");
        } else if (tipoActualizacion == 1) {
            nuevoTEFormulaConcepto.setConcepto(conceptoSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaCConcepto");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaEmpresa");
        } else if (tipoActualizacion == 2) {
            duplicarTEFormulaConcepto.setConcepto(conceptoSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaCConcepto");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaEmpresa");
        }
        filtrarLovConceptos = null;
        conceptoSeleccionado = null;
        aceptar = true;
        indexTEFormulas = -1;
        secRegistroTEFormulas = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:ConceptoTEDialogo");
         RequestContext.getCurrentInstance().update("form:lovConceptoTE");
         RequestContext.getCurrentInstance().update("form:aceptarCTE");*/
        context.reset("form:lovConceptoTE:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovConceptoTE').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ConceptoTEDialogo').hide()");
    }

    public void cancelarCambioConceptoTE() {
        filtrarLovConceptos = null;
        conceptoSeleccionado = null;
        aceptar = true;
        indexTEFormulas = -1;
        secRegistroTEFormulas = null;
        tipoActualizacion = -1;
        permitirIndexTEFormulas = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovConceptoTE:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovConceptoTE').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ConceptoTEDialogo').hide()");
    }

    /**
     * Metodo que activa el boton aceptar de la pantalla y dialogos
     */
    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    public String exportXML() {
        if (index >= 0) {
            nombreTabla = ":formExportarTS:datosTipoSueldoExportar";
            nombreXML = "TiposSueldos_XML";
        }
        if (indexTSFormulas >= 0) {
            nombreTabla = ":formExportarTSF:datosTSFormulaExportar";
            nombreXML = "FormulasConceptos_XML";
        }
        if (indexTSGrupos >= 0) {
            nombreTabla = ":formExportarTSG:datosTSGrupoExportar";
            nombreXML = "GruposDistribucion_XML";
        }
        if (indexTEFormulas >= 0) {
            nombreTabla = ":formExportarTEF:datosTEFormulaExportar";
            nombreXML = "TiposEntidadesDistribucion_XML";
        }
        return nombreTabla;
    }

    /**
     * Metodo que exporta datos a PDF
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        if (index >= 0) {
            exportPDF_TS();
        }
        if (indexTSFormulas >= 0) {
            exportPDF_TSF();
        }
        if (indexTSGrupos >= 0) {
            exportPDF_TSG();
        }
        if (indexTEFormulas >= 0) {
            exportPDF_TEF();
        }
    }

    public void exportPDF_TS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTS:datosTipoSueldoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TiposSueldos_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportPDF_TSF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTSF:datosTSFormulaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "FormulasConceptos_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexTSFormulas = -1;
        secRegistroTSFormulas = null;
    }

    public void exportPDF_TSG() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTSG:datosTSGrupoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "GruposDistribucion_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexTSGrupos = -1;
        secRegistroTSGrupos = null;
    }

    public void exportPDF_TEF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTEF:datosTEFormulaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TiposEntidadesDistribucion_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexTEFormulas = -1;
        secRegistroTEFormulas = null;
    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportXLS() throws IOException {
        if (index >= 0) {
            exportXLS_TS();
        }
        if (indexTSFormulas >= 0) {
            exportXLS_TSF();
        }
        if (indexTSGrupos >= 0) {
            exportXLS_TSG();
        }
        if (indexTEFormulas >= 0) {
            exportXLS_TEF();
        }
    }

    public void exportXLS_TS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTS:datosTipoSueldoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TiposSueldos_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS_TSF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTSF:datosTSFormulaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "FormulasConceptos_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexTSFormulas = -1;
        secRegistroTSFormulas = null;
    }

    public void exportXLS_TSG() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTSG:datosTSGrupoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "GruposDistribucion_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexTSGrupos = -1;
        secRegistroTSGrupos = null;
    }

    public void exportXLS_TEF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTEF:datosTEFormulaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TiposEntidadesDistribucion_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexTEFormulas = -1;
        secRegistroTEFormulas = null;
    }
    //EVENTO FILTRAR

    /**
     * Evento que cambia la lista reala a la filtrada
     */
    public void eventoFiltrar() {
        if (index >= 0) {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
        }
        if (indexTSFormulas >= 0) {
            if (tipoListaTSFormulas == 0) {
                tipoListaTSFormulas = 1;
            }
        }
        if (indexTSGrupos >= 0) {
            if (tipoListaTSGrupos == 0) {
                tipoListaTSGrupos = 1;
            }
        }
        if (indexTEFormulas >= 0) {
            if (tipoListaTEFormulas == 0) {
                tipoListaTEFormulas = 1;
            }
        }
    }
    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {
        int tam = listaTiposSueldos.size();
        int tam1 = listaTSFormulasConceptos.size();
        int tam2 = listaTSGruposTiposEntidades.size();
        int tam3 = listaTEFormulasConceptos.size();
        if (tam == 0 || tam1 == 0 || tam2 == 0 || tam3 == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
        } else {
            if (index >= 0) {
                verificarRastroTipoSueldo();
                index = -1;
            }
            if (indexTSFormulas >= 0) {
                verificarRastroTSFormula();
                indexTSFormulas = -1;
            }
            if (indexTSGrupos >= 0) {
                verificarRastroTSGrupo();
                indexTSGrupos = -1;
            }
            if (indexTEFormulas >= 0) {
                verificarRastroTEFormula();
                indexTEFormulas = -1;
            }
        }
    }

    public void verificarRastroTipoSueldo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaTiposSueldos != null) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSSUELDOS");
                backUpSecRegistro = secRegistro;
                backUp = secRegistro;
                secRegistro = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "TiposSueldos";
                    msnConfirmarRastro = "La tabla TIPOSSUELDOS tiene rastros para el registro seleccionado, ¿desea continuar?";
                    RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("TIPOSSUELDOS")) {
                nombreTablaRastro = "TiposSueldos";
                msnConfirmarRastroHistorico = "La tabla TIPOSSUELDOS tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    public void verificarRastroTSFormula() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaTSFormulasConceptos != null) {
            if (secRegistroTSFormulas != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroTSFormulas, "TSFORMULASCONCEPTOS");
                backUpSecRegistroTSFormulas = secRegistroTSFormulas;
                backUp = secRegistroTSFormulas;
                secRegistroTSFormulas = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "TSFormulasConceptos";
                    msnConfirmarRastro = "La tabla TSFORMULASCONCEPTOS tiene rastros para el registro seleccionado, ¿desea continuar?";
                    RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("TSFORMULASCONCEPTOS")) {
                nombreTablaRastro = "TSFormulasConceptos";
                msnConfirmarRastroHistorico = "La tabla TSFORMULASCONCEPTOS tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
        indexTSFormulas = -1;
    }

    public void verificarRastroTSGrupo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaTSGruposTiposEntidades != null) {
            if (secRegistroTSGrupos != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroTSGrupos, "TSGRUPOSTIPOSENTIDADES");
                backUpSecRegistroTSGrupos = secRegistroTSGrupos;
                backUp = backUpSecRegistroTSGrupos;
                secRegistroTSGrupos = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "TSGruposTiposEntidades";
                    msnConfirmarRastro = "La tabla TSGRUPOSTIPOSENTIDADES tiene rastros para el registro seleccionado, ¿desea continuar?";
                    RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("TSGRUPOSTIPOSENTIDADES")) {
                nombreTablaRastro = "TSGruposTiposEntidades";
                msnConfirmarRastroHistorico = "La tabla TSGRUPOSTIPOSENTIDADES tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
        indexTSGrupos = -1;
    }

    public void verificarRastroTEFormula() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaTEFormulasConceptos != null) {
            if (secRegistroTEFormulas != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroTEFormulas, "TEFORMULASCONCEPTOS");
                backUpSecRegistroTEFormulas = secRegistroTEFormulas;
                backUp = secRegistroTEFormulas;
                secRegistroTEFormulas = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "TEFormulasConceptos";
                    msnConfirmarRastro = "La tabla TEFORMULASCONCEPTOS tiene rastros para el registro seleccionado, ¿desea continuar?";
                    RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("TEFORMULASCONCEPTOS")) {
                nombreTablaRastro = "TEFormulasConceptos";
                msnConfirmarRastroHistorico = "La tabla TEFORMULASCONCEPTOS tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
        indexTEFormulas = -1;
    }

    //GETTERS AND SETTERS
    public List<TiposSueldos> getListaTiposSueldos() {
        try {
            if (listaTiposSueldos == null) {
                listaTiposSueldos = new ArrayList<TiposSueldos>();
                listaTiposSueldos = administrarTipoSueldo.listaTiposSueldos();
                return listaTiposSueldos;
            } else {
                return listaTiposSueldos;
            }
        } catch (Exception e) {
            System.out.println("Error...!! getListaTiposSueldos " + e.toString());
            return null;
        }
    }

    public void setListaTiposSueldos(List<TiposSueldos> setListaTiposSueldos) {
        this.listaTiposSueldos = setListaTiposSueldos;
    }

    public List<TiposSueldos> getFiltrarListaTiposSueldos() {
        return filtrarListaTiposSueldos;
    }

    public void setFiltrarListaTiposSueldos(List<TiposSueldos> setFiltrarListaTiposSueldos) {
        this.filtrarListaTiposSueldos = setFiltrarListaTiposSueldos;
    }

    public TiposSueldos getNuevoTipoSueldo() {
        return nuevoTipoSueldo;
    }

    public void setNuevoTipoSueldo(TiposSueldos setNuevoTipoSueldo) {
        this.nuevoTipoSueldo = setNuevoTipoSueldo;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public TiposSueldos getEditarTipoSueldo() {
        return editarTipoSueldo;
    }

    public void setEditarTipoSueldo(TiposSueldos setEditarTipoSueldo) {
        this.editarTipoSueldo = setEditarTipoSueldo;
    }

    public TiposSueldos getDuplicarTipoSueldo() {
        return duplicarTipoSueldo;
    }

    public void setDuplicarTipoSueldo(TiposSueldos setDuplicarTipoSueldo) {
        this.duplicarTipoSueldo = setDuplicarTipoSueldo;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger BackUpSecRegistro) {
        this.backUpSecRegistro = BackUpSecRegistro;
    }

    public List<TSFormulasConceptos> getListaTSFormulasConceptos() {
        if (listaTSFormulasConceptos == null) {
            listaTSFormulasConceptos = new ArrayList<TSFormulasConceptos>();
            if (index >= 0) {

                if (tipoLista == 0) {
                    listaTSFormulasConceptos = administrarTipoSueldo.listaTSFormulasConceptosParaTipoSueldoSecuencia(listaTiposSueldos.get(index).getSecuencia());
                }
                if (tipoLista == 1) {
                    listaTSFormulasConceptos = administrarTipoSueldo.listaTSFormulasConceptosParaTipoSueldoSecuencia(filtrarListaTiposSueldos.get(index).getSecuencia());
                }
            }
        }
        return listaTSFormulasConceptos;
    }

    public void setListaTSFormulasConceptos(List<TSFormulasConceptos> setListaTSFormulasConceptos) {
        this.listaTSFormulasConceptos = setListaTSFormulasConceptos;
    }

    public List<TSFormulasConceptos> getFiltrarListaTSFormulasConceptos() {
        return filtrarListaTSFormulasConceptos;
    }

    public void setFiltrarListaTSFormulasConceptos(List<TSFormulasConceptos> setFiltrarListaTSFormulasConceptos) {
        this.filtrarListaTSFormulasConceptos = setFiltrarListaTSFormulasConceptos;
    }

    public List<TiposSueldos> getListTiposSueldosModificar() {
        return listTiposSueldosModificar;
    }

    public void setListTiposSueldosModificar(List<TiposSueldos> setListTiposSueldosModificar) {
        this.listTiposSueldosModificar = setListTiposSueldosModificar;
    }

    public List<TiposSueldos> getListTiposSueldosCrear() {
        return listTiposSueldosCrear;
    }

    public void setListTiposSueldosCrear(List<TiposSueldos> setListTiposSueldosCrear) {
        this.listTiposSueldosCrear = setListTiposSueldosCrear;
    }

    public List<TiposSueldos> getListTiposSueldosBorrar() {
        return listTiposSueldosBorrar;
    }

    public void setListTiposSueldosBorrar(List<TiposSueldos> setListTiposSueldosBorrar) {
        this.listTiposSueldosBorrar = setListTiposSueldosBorrar;
    }

    public List<TSFormulasConceptos> getListTSFormulasConceptosModificar() {
        return listTSFormulasConceptosModificar;
    }

    public void setListTSFormulasConceptosModificar(List<TSFormulasConceptos> setListTSFormulasConceptosModificar) {
        this.listTSFormulasConceptosModificar = setListTSFormulasConceptosModificar;
    }

    public TSFormulasConceptos getNuevoTSFormulaConcepto() {
        return nuevoTSFormulaConcepto;
    }

    public void setNuevoTSFormulaConcepto(TSFormulasConceptos setNuevoTSFormulaConcepto) {
        this.nuevoTSFormulaConcepto = setNuevoTSFormulaConcepto;
    }

    public List<TSFormulasConceptos> getListTSFormulasConceptosCrear() {
        return listTSFormulasConceptosCrear;
    }

    public void setListTSFormulasConceptosCrear(List<TSFormulasConceptos> setListTSFormulasConceptosCrear) {
        this.listTSFormulasConceptosCrear = setListTSFormulasConceptosCrear;
    }

    public List<TSFormulasConceptos> getLisTSFormulasConceptosBorrar() {
        return listTSFormulasConceptosBorrar;
    }

    public void setListTSFormulasConceptosBorrar(List<TSFormulasConceptos> setListTSFormulasConceptosBorrar) {
        this.listTSFormulasConceptosBorrar = setListTSFormulasConceptosBorrar;
    }

    public TSFormulasConceptos getEditarTSFormulaConcepto() {
        return editarTSFormulaConcepto;
    }

    public void setEditarTSFormulaConcepto(TSFormulasConceptos setEditarTSFormulaConcepto) {
        this.editarTSFormulaConcepto = setEditarTSFormulaConcepto;
    }

    public TSFormulasConceptos getDuplicarTSFormulaConcepto() {
        return duplicarTSFormulaConcepto;
    }

    public void setDuplicarTSFormulaConcepto(TSFormulasConceptos setDuplicarTSFormulaConcepto) {
        this.duplicarTSFormulaConcepto = setDuplicarTSFormulaConcepto;
    }

    public BigInteger getSecRegistroTSFormulas() {
        return secRegistroTSFormulas;
    }

    public void setSecRegistroTSFormulas(BigInteger setSecRegistroTSFormulas) {
        this.secRegistroTSFormulas = setSecRegistroTSFormulas;
    }

    public BigInteger getBackUpSecRegistroTSFormulas() {
        return backUpSecRegistroTSFormulas;
    }

    public void setBackUpSecRegistroTSFormulas(BigInteger setBackUpSecRegistroTSFormulas) {
        this.backUpSecRegistroTSFormulas = setBackUpSecRegistroTSFormulas;
    }

    public String getMsnConfirmarRastro() {
        return msnConfirmarRastro;
    }

    public void setMsnConfirmarRastro(String msnConfirmarRastro) {
        this.msnConfirmarRastro = msnConfirmarRastro;
    }

    public String getMsnConfirmarRastroHistorico() {
        return msnConfirmarRastroHistorico;
    }

    public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
        this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
    }

    public BigInteger getBackUp() {
        return backUp;
    }

    public void setBackUp(BigInteger backUp) {
        this.backUp = backUp;
    }

    public String getNombreTablaRastro() {
        return nombreTablaRastro;
    }

    public void setNombreTablaRastro(String nombreTablaRastro) {
        this.nombreTablaRastro = nombreTablaRastro;
    }

    public String getNombreXML() {
        return nombreXML;
    }

    public void setNombreXML(String nombreXML) {
        this.nombreXML = nombreXML;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String setNombreTabla) {
        this.nombreTabla = setNombreTabla;
    }

    public List<Formulas> getLovFormulas() {
        lovFormulas = administrarTipoSueldo.lovFormulas();

        return lovFormulas;
    }

    public void setLovFormulas(List<Formulas> setLovFormulas) {
        this.lovFormulas = setLovFormulas;
    }

    public List<Formulas> getFiltrarLovFormulas() {
        return filtrarLovFormulas;
    }

    public void setFiltrarLovFormulas(List<Formulas> setFiltrarLovFormulas) {
        this.filtrarLovFormulas = setFiltrarLovFormulas;
    }

    public Formulas getFormulaSeleccionado() {
        return formulaSeleccionado;
    }

    public void setFormulaSeleccionado(Formulas setFormulaSeleccionado) {
        this.formulaSeleccionado = setFormulaSeleccionado;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public String getAltoTablaTiposSueldos() {
        return altoTablaTiposSueldos;
    }

    public void setAltoTablaTiposSueldos(String setAltoTablaTiposSueldos) {
        this.altoTablaTiposSueldos = setAltoTablaTiposSueldos;
    }

    public String getAltoTablaTSFormulas() {
        return altoTablaTSFormulas;
    }

    public void setAltoTablaTSFormulas(String setAltoTablaTSFormulas) {
        this.altoTablaTSFormulas = setAltoTablaTSFormulas;
    }

    public List<Conceptos> getLovConceptos() {
        lovConceptos = administrarTipoSueldo.lovConceptos();

        return lovConceptos;
    }

    public void setLovConceptos(List<Conceptos> lovConceptos) {
        this.lovConceptos = lovConceptos;
    }

    public List<Conceptos> getFiltrarLovConceptos() {
        return filtrarLovConceptos;
    }

    public void setFiltrarLovConceptos(List<Conceptos> filtrarLovConceptos) {
        this.filtrarLovConceptos = filtrarLovConceptos;
    }

    public Conceptos getConceptoSeleccionado() {
        return conceptoSeleccionado;
    }

    public void setConceptoSeleccionado(Conceptos conceptoSeleccionado) {
        this.conceptoSeleccionado = conceptoSeleccionado;
    }

    public List<TSGruposTiposEntidades> getListaTSGruposTiposEntidades() {
        if (listaTSGruposTiposEntidades == null) {
            listaTSGruposTiposEntidades = new ArrayList<TSGruposTiposEntidades>();
            if (index >= 0) {
                if (tipoLista == 0) {
                    listaTSGruposTiposEntidades = administrarTipoSueldo.listaTSGruposTiposEntidadesParaTipoSueldoSecuencia(listaTiposSueldos.get(index).getSecuencia());
                }
                if (tipoLista == 1) {
                    listaTSGruposTiposEntidades = administrarTipoSueldo.listaTSGruposTiposEntidadesParaTipoSueldoSecuencia(filtrarListaTiposSueldos.get(index).getSecuencia());
                }
            }

        }
        return listaTSGruposTiposEntidades;
    }

    public void setListaTSGruposTiposEntidades(List<TSGruposTiposEntidades> listaTSGruposTiposEntidades) {
        this.listaTSGruposTiposEntidades = listaTSGruposTiposEntidades;
    }

    public List<TSGruposTiposEntidades> getFiltrarListaTSGruposTiposEntidades() {
        return filtrarListaTSGruposTiposEntidades;
    }

    public void setFiltrarListaTSGruposTiposEntidades(List<TSGruposTiposEntidades> filtrarListaTSGruposTiposEntidades) {
        this.filtrarListaTSGruposTiposEntidades = filtrarListaTSGruposTiposEntidades;
    }

    public List<TSGruposTiposEntidades> getListTSGruposTiposEntidadesModificar() {
        return listTSGruposTiposEntidadesModificar;
    }

    public void setListTSGruposTiposEntidadesModificar(List<TSGruposTiposEntidades> listTSGruposTiposEntidadesModificar) {
        this.listTSGruposTiposEntidadesModificar = listTSGruposTiposEntidadesModificar;
    }

    public TSGruposTiposEntidades getNuevoTSGrupoTipoEntidad() {
        return nuevoTSGrupoTipoEntidad;
    }

    public void setNuevoTSGrupoTipoEntidad(TSGruposTiposEntidades nuevoTSGrupoTipoEntidad) {
        this.nuevoTSGrupoTipoEntidad = nuevoTSGrupoTipoEntidad;
    }

    public List<TSGruposTiposEntidades> getListTSGruposTiposEntidadesCrear() {
        return listTSGruposTiposEntidadesCrear;
    }

    public void setListTSGruposTiposEntidadesCrear(List<TSGruposTiposEntidades> listTSGruposTiposEntidadessCrear) {
        this.listTSGruposTiposEntidadesCrear = listTSGruposTiposEntidadessCrear;
    }

    public List<TSGruposTiposEntidades> getListTSGruposTiposEntidadesBorrar() {
        return listTSGruposTiposEntidadesBorrar;
    }

    public void setListTSGruposTiposEntidadesBorrar(List<TSGruposTiposEntidades> listTSGruposTiposEntidadesBorrar) {
        this.listTSGruposTiposEntidadesBorrar = listTSGruposTiposEntidadesBorrar;
    }

    public TSGruposTiposEntidades getEditarTSGrupoTipoEntidad() {
        return editarTSGrupoTipoEntidad;
    }

    public void setEditarTSGrupoTipoEntidad(TSGruposTiposEntidades editarTSGrupoTipoEntidad) {
        this.editarTSGrupoTipoEntidad = editarTSGrupoTipoEntidad;
    }

    public TSFormulasConceptos getDuplicarTSFormulaCConcepto() {
        return duplicarTSFormulaConcepto;
    }

    public void setDuplicarTSFormulaCConcepto(TSFormulasConceptos duplicarTSFormulaConcepto) {
        this.duplicarTSFormulaConcepto = duplicarTSFormulaConcepto;
    }

    public TSGruposTiposEntidades getDuplicarTSGrupoTipoEntidad() {
        return duplicarTSGrupoTipoEntidad;
    }

    public void setDuplicarTSGrupoTipoEntidad(TSGruposTiposEntidades duplicarTSGrupoTipoEntidad) {
        this.duplicarTSGrupoTipoEntidad = duplicarTSGrupoTipoEntidad;
    }

    public BigInteger getSecRegistroTSGrupos() {
        return secRegistroTSGrupos;
    }

    public void setSecRegistroTSGrupos(BigInteger secRegistroTSGrupos) {
        this.secRegistroTSGrupos = secRegistroTSGrupos;
    }

    public BigInteger getBackUpSecRegistroTSGrupos() {
        return backUpSecRegistroTSGrupos;
    }

    public void setBackUpSecRegistroTSGrupos(BigInteger backUpSecRegistroTSGrupos) {
        this.backUpSecRegistroTSGrupos = backUpSecRegistroTSGrupos;
    }

    public List<Grupostiposentidades> getLovGruposTiposEntidades() {
        lovGruposTiposEntidades = administrarTipoSueldo.lovGruposTiposEntidades();

        return lovGruposTiposEntidades;
    }

    public void setLovGruposTiposEntidades(List<Grupostiposentidades> lovGruposTiposEntidades) {
        this.lovGruposTiposEntidades = lovGruposTiposEntidades;
    }

    public List<Grupostiposentidades> getFiltrarLovGruposTiposEntidades() {
        return filtrarLovGruposTiposEntidades;
    }

    public void setFiltrarLovGruposTiposEntidades(List<Grupostiposentidades> filtrarLovGruposTiposEntidades) {
        this.filtrarLovGruposTiposEntidades = filtrarLovGruposTiposEntidades;
    }

    public Grupostiposentidades getGrupoTipoEntidadSeleccionado() {
        return grupoTipoEntidadSeleccionado;
    }

    public void setGrupoTipoEntidadSeleccionado(Grupostiposentidades grupoTipoEntidadSeleccionado) {
        this.grupoTipoEntidadSeleccionado = grupoTipoEntidadSeleccionado;
    }

    public String getAltoTablaTSGrupos() {
        return altoTablaTSGrupos;
    }

    public void setAltoTablaTSGrupos(String altoTablaTSGrupos) {
        this.altoTablaTSGrupos = altoTablaTSGrupos;
    }

    public List<TEFormulasConceptos> getListaTEFormulasConceptos() {
        if (listaTEFormulasConceptos == null) {
            listaTEFormulasConceptos = new ArrayList<TEFormulasConceptos>();

            if (indexTSGrupos >= 0) {
                if (tipoListaTSGrupos == 0) {
                    int tam = listaTSGruposTiposEntidades.size();
                    if (tam > 0) {
                        listaTEFormulasConceptos = administrarTipoSueldo.listaTEFormulasConceptosParaTSGrupoTipoEntidadSecuencia(listaTSGruposTiposEntidades.get(indexTSGrupos).getSecuencia());
                    }
                }
                if (tipoListaTSGrupos == 1) {
                    int tam = filtrarListaTSGruposTiposEntidades.size();
                    if (tam > 0) {
                        listaTEFormulasConceptos = administrarTipoSueldo.listaTEFormulasConceptosParaTSGrupoTipoEntidadSecuencia(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos).getSecuencia());
                    }
                }
            }

        }
        return listaTEFormulasConceptos;
    }

    public void setListaTEFormulasConceptos(List<TEFormulasConceptos> listaTEFormulasConceptos) {
        this.listaTEFormulasConceptos = listaTEFormulasConceptos;
    }

    public List<TEFormulasConceptos> getFiltrarListaTEFormulasConceptos() {
        return filtrarListaTEFormulasConceptos;
    }

    public void setFiltrarListaTEFormulasConceptos(List<TEFormulasConceptos> filtrarListaTEFormulasConceptos) {
        this.filtrarListaTEFormulasConceptos = filtrarListaTEFormulasConceptos;
    }

    public List<TEFormulasConceptos> getListTEFormulasConceptosModificar() {
        return listTEFormulasConceptosModificar;
    }

    public void setListTEFormulasConceptosModificar(List<TEFormulasConceptos> listTEFormulasConceptosModificar) {
        this.listTEFormulasConceptosModificar = listTEFormulasConceptosModificar;
    }

    public TEFormulasConceptos getNuevoTEFormulaConcepto() {
        return nuevoTEFormulaConcepto;
    }

    public void setNuevoTEFormulaConcepto(TEFormulasConceptos nuevoTEFormulaConcepto) {
        this.nuevoTEFormulaConcepto = nuevoTEFormulaConcepto;
    }

    public List<TEFormulasConceptos> getListTEFormulasConceptosCrear() {
        return listTEFormulasConceptosCrear;
    }

    public void setListTEFormulasConceptosCrear(List<TEFormulasConceptos> listTEFormulasConceptosCrear) {
        this.listTEFormulasConceptosCrear = listTEFormulasConceptosCrear;
    }

    public List<TEFormulasConceptos> getListTEFormulasConceptosBorrar() {
        return listTEFormulasConceptosBorrar;
    }

    public void setListTEFormulasConceptosBorrar(List<TEFormulasConceptos> listTEFormulasConceptosBorrar) {
        this.listTEFormulasConceptosBorrar = listTEFormulasConceptosBorrar;
    }

    public TEFormulasConceptos getEditarTEFormulaConcepto() {
        return editarTEFormulaConcepto;
    }

    public void setEditarTEFormulaConcepto(TEFormulasConceptos editarTEFormulaConcepto) {
        this.editarTEFormulaConcepto = editarTEFormulaConcepto;
    }

    public TEFormulasConceptos getDuplicarTEFormulaConcepto() {
        return duplicarTEFormulaConcepto;
    }

    public void setDuplicarTEFormulaConcepto(TEFormulasConceptos duplicarTEFormulaConcepto) {
        this.duplicarTEFormulaConcepto = duplicarTEFormulaConcepto;
    }

    public BigInteger getSecRegistroTEFormulas() {
        return secRegistroTEFormulas;
    }

    public void setSecRegistroTEFormulas(BigInteger secRegistroTEFormulas) {
        this.secRegistroTEFormulas = secRegistroTEFormulas;
    }

    public BigInteger getBackUpSecRegistroTEFormulas() {
        return backUpSecRegistroTEFormulas;
    }

    public void setBackUpSecRegistroTEFormulas(BigInteger backUpSecRegistroTEFormulas) {
        this.backUpSecRegistroTEFormulas = backUpSecRegistroTEFormulas;
    }

    public List<TiposEntidades> getLovTiposEntidades() {
        if (lovTiposEntidades == null) {
            if (indexTSGrupos >= 0) {
                if (tipoListaTSGrupos == 0) {
                    int tam = listaTSGruposTiposEntidades.size();
                    if (tam > 0) {
                        lovTiposEntidades = administrarTipoSueldo.lovTiposEntidades(listaTSGruposTiposEntidades.get(indexTSGrupos).getGrupotipoentidad().getSecuencia());
                    }
                }
                if (tipoListaTSGrupos == 1) {
                    int tam = listaTSGruposTiposEntidades.size();
                    if (tam > 0) {
                        lovTiposEntidades = administrarTipoSueldo.lovTiposEntidades(filtrarListaTSGruposTiposEntidades.get(indexTSGrupos).getGrupotipoentidad().getSecuencia());
                    }
                }
            }
        }
        return lovTiposEntidades;
    }

    public void setLovTiposEntidades(List<TiposEntidades> lovTiposEntidades) {
        this.lovTiposEntidades = lovTiposEntidades;
    }

    public List<TiposEntidades> getFiltrarLovTiposEntidades() {
        return filtrarLovTiposEntidades;
    }

    public void setFiltrarLovTiposEntidades(List<TiposEntidades> filtrarLovTiposEntidades) {
        this.filtrarLovTiposEntidades = filtrarLovTiposEntidades;
    }

    public TiposEntidades getTipoEntidadSeleccionado() {
        return tipoEntidadSeleccionado;
    }

    public void setTipoEntidadSeleccionado(TiposEntidades tipoEntidadSeleccionado) {
        this.tipoEntidadSeleccionado = tipoEntidadSeleccionado;
    }

    public String getAltoTablaTEFormulas() {
        return altoTablaTEFormulas;
    }

    public void setAltoTablaTEFormulas(String altoTablaTEFormulas) {
        this.altoTablaTEFormulas = altoTablaTEFormulas;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public boolean isActivoFormulaConcepto() {
        return activoFormulaConcepto;
    }

    public void setActivoFormulaConcepto(boolean activoFormulaConcepto) {
        this.activoFormulaConcepto = activoFormulaConcepto;
    }

    public boolean isActivoGrupoDistribucion() {
        return activoGrupoDistribucion;
    }

    public void setActivoGrupoDistribucion(boolean activoGrupoDistribucion) {
        this.activoGrupoDistribucion = activoGrupoDistribucion;
    }

    public boolean isActivoTipoEntidad() {
        return activoTipoEntidad;
    }

    public void setActivoTipoEntidad(boolean activoTipoEntidad) {
        this.activoTipoEntidad = activoTipoEntidad;
    }

    public TiposSueldos getTipoSueldoTablaSeleccionado() {
        getListaTiposSueldos();
        if (listaTiposSueldos != null) {
            int tam = listaTiposSueldos.size();
            if (tam > 0) {
                tipoSueldoTablaSeleccionado = listaTiposSueldos.get(0);
            }
        }
        return tipoSueldoTablaSeleccionado;
    }

    public void setTipoSueldoTablaSeleccionado(TiposSueldos tipoSueldoTablaSeleccionado) {
        this.tipoSueldoTablaSeleccionado = tipoSueldoTablaSeleccionado;
    }

    public TSFormulasConceptos getTsFormulaTablaSeleccionado() {
        getListaTSFormulasConceptos();
        if (listaTSFormulasConceptos != null) {
            int tam = listaTSFormulasConceptos.size();
            if (tam > 0) {
                tsFormulaTablaSeleccionado = listaTSFormulasConceptos.get(0);
            }
        }
        return tsFormulaTablaSeleccionado;
    }

    public void setTsFormulaTablaSeleccionado(TSFormulasConceptos tsFormulaTablaSeleccionado) {
        this.tsFormulaTablaSeleccionado = tsFormulaTablaSeleccionado;
    }

    public TSGruposTiposEntidades getTsGrupoTablaSeleccionado() {
        getListaTSGruposTiposEntidades();
        if (listaTSGruposTiposEntidades != null) {
            int tam = listaTSGruposTiposEntidades.size();
            if (tam > 0) {
                tsGrupoTablaSeleccionado = listaTSGruposTiposEntidades.get(0);
            }
        }
        return tsGrupoTablaSeleccionado;
    }

    public void setTsGrupoTablaSeleccionado(TSGruposTiposEntidades tsGrupoTablaSeleccionado) {
        this.tsGrupoTablaSeleccionado = tsGrupoTablaSeleccionado;
    }

    public TEFormulasConceptos getTeFormulaTablaSeleccionado() {
        getListaTEFormulasConceptos();
        if (listaTEFormulasConceptos != null) {
            int tam = listaTEFormulasConceptos.size();
            if (tam > 0) {
                teFormulaTablaSeleccionado = listaTEFormulasConceptos.get(0);
            }
        }
        return teFormulaTablaSeleccionado;
    }

    public void setTeFormulaTablaSeleccionado(TEFormulasConceptos teFormulaTablaSeleccionado) {
        this.teFormulaTablaSeleccionado = teFormulaTablaSeleccionado;
    }

    public String getInfoRegistroFormula() {
        getLovFormulas();
        if (lovFormulas != null) {
            infoRegistroFormula = "Cantidad de registros : " + lovFormulas.size();
        } else {
            infoRegistroFormula = "Cantidad de registros : 0";
        }
        return infoRegistroFormula;
    }

    public void setInfoRegistroFormula(String infoRegistroFormula) {
        this.infoRegistroFormula = infoRegistroFormula;
    }

    public String getInfoRegistroConcepto() {
        getLovConceptos();
        if (lovConceptos != null) {
            infoRegistroConcepto = "Cantidad de registros : " + lovConceptos.size();
        } else {
            infoRegistroConcepto = "Cantidad de registros : 0";
        }
        return infoRegistroConcepto;
    }

    public void setInfoRegistroConcepto(String infoRegistroConcepto) {
        this.infoRegistroConcepto = infoRegistroConcepto;
    }

    public String getInfoRegistroGrupo() {
        getLovGruposTiposEntidades();
        if (lovGruposTiposEntidades != null) {
            infoRegistroGrupo = "Cantidad de registros : " + lovGruposTiposEntidades.size();
        } else {
            infoRegistroGrupo = "Cantidad de registros : 0";
        }
        return infoRegistroGrupo;
    }

    public void setInfoRegistroGrupo(String infoRegistroGrupo) {
        this.infoRegistroGrupo = infoRegistroGrupo;
    }

    public String getInfoRegistroTipoEntidad() {
        getLovTiposEntidades();
        if (lovTiposEntidades != null) {
            infoRegistroTipoEntidad = "Cantidad de registros : " + lovTiposEntidades.size();
        } else {
            infoRegistroTipoEntidad = "Cantidad de registros : 0";
        }
        return infoRegistroTipoEntidad;
    }

    public void setInfoRegistroTipoEntidad(String infoRegistroTipoEntidad) {
        this.infoRegistroTipoEntidad = infoRegistroTipoEntidad;
    }

    public String getInfoRegistroFormulaTE() {
        getLovFormulas();
        if (lovFormulas != null) {
            infoRegistroFormulaTE = "Cantidad de registros : " + lovFormulas.size();
        } else {
            infoRegistroFormulaTE = "Cantidad de registros : 0";
        }
        return infoRegistroFormulaTE;
    }

    public void setInfoRegistroFormulaTE(String infoRegistroFormulaTE) {
        this.infoRegistroFormulaTE = infoRegistroFormulaTE;
    }

    public String getInfoRegistroConceptoTE() {
        getLovConceptos();
        if (lovConceptos != null) {
            infoRegistroConceptoTE = "Cantidad de registros : " + lovConceptos.size();
        } else {
            infoRegistroConceptoTE = "Cantidad de registros : 0";
        }
        return infoRegistroConceptoTE;
    }

    public void setInfoRegistroConceptoTE(String infoRegistroConceptoTe) {
        this.infoRegistroConceptoTE = infoRegistroConceptoTe;
    }

}
