package Controlador;

import Entidades.Formulas;
import Entidades.FormulasProcesos;
import Entidades.Operandos;
import Entidades.OperandosLogs;
import Entidades.Procesos;
import Entidades.Tipospagos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarProcesosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
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
public class ControlProceso implements Serializable {

    @EJB
    AdministrarProcesosInterface administrarProcesos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //
    private List<Procesos> listaProcesos;
    private List<Procesos> filtrarListaProcesos;
    private Procesos procesoTablaSeleccionada;
    //
    private List<FormulasProcesos> listaFormulasProcesos;
    private List<FormulasProcesos> filtrarListaFormulasProcesos;
    private FormulasProcesos formulaProcesoTablaSeleccionada;
    //
    private List<OperandosLogs> listaOperandosLogs;
    private List<OperandosLogs> filtrarListaOperandosLogs;
    private OperandosLogs operandoTablaSeleccionada;

    //Activo/Desactivo Crtl + F11
    private int bandera, banderaFormulasProcesos, banderaOperandosLogs;
    //Columnas Tabla 
    private Column procesoCodigo, procesoDescripcion, procesoTipoPago, procesoComentario, procesoNumero, procesoContabilizacion, procesoSolucionNodo, procesoAdelanto, procesoSobregiro, procesoAutomatico;
    private Column formulaProcesoFormula, formulaProcesoPeriodicidad;
    private Column operandoOperando, operandoDescripcion;
    //Otros
    private boolean aceptar;
    private int index, indexFormulasProcesos, indexAux, indexOperandosLogs;
    //modificar
    private List<Procesos> listProcesosModificar;
    private List<FormulasProcesos> listFormulasProcesosModificar;
    private List<OperandosLogs> listOperandosLogsModificar;
    private boolean guardado, guardadoFormulasProcesos, guardadoOperandosLogs;
    //crear 
    private Procesos nuevoProceso;
    private FormulasProcesos nuevoFormulaProceso;
    private OperandosLogs nuevoOperandoLog;
    private List<Procesos> listProcesosCrear;
    private List<FormulasProcesos> listFormulasProcesosCrear;
    private List<OperandosLogs> listOperandosLogsCrear;
    private BigInteger l;
    private int k;
    //borrar 
    private List<Procesos> listProcesosBorrar;
    private List<FormulasProcesos> listFormulasProcesosBorrar;
    private List<OperandosLogs> listOperandosLogsBorrar;
    //editar celda
    private Procesos editarProceso;
    private FormulasProcesos editarFormulaProceso;
    private OperandosLogs editarOperandoLog;
    private int cualCelda, tipoLista, cualCeldaFormulaProceso, tipoListaFormulasProcesos, cualCeldaOperandoLog, tipoListaOperandoLog;
    //duplicar
    private Procesos duplicarProceso;
    private FormulasProcesos duplicarFormulaProceso;
    private OperandosLogs duplicarOperandoLog;
    private BigInteger secRegistro, secRegistroFormulaProceso, secRegistroOperandoLog;
    private BigInteger backUpSecRegistro, backUpSecRegistroFormulaProceso, backUpSecRegistroOperandoLog;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    private String nombreXML, nombreTabla;
    private String tipoPago, formula, operando;

    ///////////LOV///////////
    private List<Tipospagos> lovTiposPagos;
    private List<Tipospagos> filtrarLovTiposPagos;
    private Tipospagos tipoPagoSeleccionado;

    private List<Formulas> lovFormulas;
    private List<Formulas> filtrarLovFormulas;
    private Formulas formulaSeleccionado;

    private List<Operandos> lovOperandos;
    private List<Operandos> filtrarLovOperandos;
    private Operandos operandoSeleccionado;

    private boolean permitirIndex, permitirIndexFormulasProcesos, permitirIndexOperandosLogs;
    private int tipoActualizacion;
    private short auxCodigoProceso;
    private String auxDescripcionProceso;
    //
    private boolean cambiosPagina;
    private String altoTablaProcesos, altoTablaFormulasProcesos, altoTablaOperandosLogs;
    //
    private Procesos procesoNuevoClonado, procesoBaseClonado;
    private List<Procesos> lovProcesos;
    private List<Procesos> filtrarLovProcesos;
    private Procesos procesoSeleccionado;
    //
    private short auxClonadoCodigo;
    private String auxClonadoDescripcion;
    //
    private String infoRegistroFormula, infoRegistroTipoPago, infoRegistroProceso, infoRegistroOperando;
    private String altoTablaProceso;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlProceso() {
        //clonado
        procesoNuevoClonado = new Procesos();
        procesoBaseClonado = new Procesos();
        lovProcesos = null;
        //
        paginaAnterior = "nominaf";
        //altos tablas
        altoTablaProcesos = "110";
        altoTablaFormulasProcesos = "115";
        altoTablaOperandosLogs = "115";
        //Permitir index
        permitirIndexFormulasProcesos = true;
        permitirIndex = true;
        permitirIndexOperandosLogs = true;
        //lovs
        lovTiposPagos = null;
        tipoPagoSeleccionado = new Tipospagos();
        lovFormulas = null;
        formulaSeleccionado = new Formulas();
        lovOperandos = null;
        operandoSeleccionado = new Operandos();
        //index tablas
        indexFormulasProcesos = -1;
        indexOperandosLogs = -1;
        //listas de tablas
        listaProcesos = null;
        listaFormulasProcesos = null;
        listaOperandosLogs = null;
        //Otros
        aceptar = true;
        cambiosPagina = true;
        tipoActualizacion = -1;
        k = 0;
        //borrar 
        listProcesosBorrar = new ArrayList<Procesos>();
        listFormulasProcesosBorrar = new ArrayList<FormulasProcesos>();
        listOperandosLogsBorrar = new ArrayList<OperandosLogs>();
        //crear 
        listProcesosCrear = new ArrayList<Procesos>();
        listFormulasProcesosCrear = new ArrayList<FormulasProcesos>();
        listOperandosLogsCrear = new ArrayList<OperandosLogs>();
        //modificar 
        listFormulasProcesosModificar = new ArrayList<FormulasProcesos>();
        listProcesosModificar = new ArrayList<Procesos>();
        listOperandosLogsModificar = new ArrayList<OperandosLogs>();
        //editar
        editarProceso = new Procesos();
        editarFormulaProceso = new FormulasProcesos();
        editarOperandoLog = new OperandosLogs();
        //Cual Celda
        cualCelda = -1;
        cualCeldaFormulaProceso = -1;
        cualCeldaOperandoLog = -1;
        //Tipo Lista
        tipoListaFormulasProcesos = 0;
        tipoLista = 0;
        tipoListaOperandoLog = 0;
        //guardar
        guardado = true;
        guardadoFormulasProcesos = true;
        guardadoOperandosLogs = true;
        //Crear 
        nuevoProceso = new Procesos();
        nuevoProceso.setTipopago(new Tipospagos());
        nuevoFormulaProceso = new FormulasProcesos();
        nuevoFormulaProceso.setFormula(new Formulas());
        nuevoOperandoLog = new OperandosLogs();
        nuevoOperandoLog.setOperando(new Operandos());
        //Duplicar
        duplicarProceso = new Procesos();
        duplicarFormulaProceso = new FormulasProcesos();
        duplicarOperandoLog = new OperandosLogs();
        //Sec Registro
        secRegistro = null;
        backUpSecRegistro = null;
        secRegistroFormulaProceso = null;
        backUpSecRegistroFormulaProceso = null;
        secRegistroFormulaProceso = null;
        backUpSecRegistroOperandoLog = null;
        //Banderas
        bandera = 0;
        banderaFormulasProcesos = 0;
        banderaOperandosLogs = 0;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarProcesos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
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
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina();
        } else {
            String pagActual = "proceso";
            //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParametros.put("paginaAnterior", pagActual);
            //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
            controlListaNavegacion.adicionarPagina(pagActual);
        }
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

    public String valorPaginaAnterior() {
        System.out.println("Entro en valorPaginaAnterior(), retorno : " + paginaAnterior);
        return paginaAnterior;
    }

    public void inicializarPagina(String paginaLlamado) {
        paginaAnterior = paginaLlamado;
        listaFormulasProcesos = null;
        listaProcesos = null;
        getListaProcesos();
        int tam = listaProcesos.size();
        if (tam >= 1) {
            index = 0;
            getListaFormulasProcesos();
            getListaOperandosLogs();
        }
    }

    public boolean validarCamposNulosProceso(int i) {
        boolean retorno = true;
        if (i == 0) {
            Procesos aux = new Procesos();
            if (tipoLista == 0) {
                aux = listaProcesos.get(index);
            }
            if (tipoLista == 1) {
                aux = filtrarListaProcesos.get(index);
            }
            if (aux.getCodigo() <= 0 || aux.getTipopago().getSecuencia() == null) {
                retorno = false;
            }
            if (aux.getDescripcion() == null) {
                retorno = false;
            } else if (aux.getDescripcion().isEmpty()) {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevoProceso.getCodigo() <= 0 || nuevoProceso.getTipopago().getSecuencia() == null) {
                retorno = false;
            }
            if (nuevoProceso.getDescripcion() == null) {
                retorno = false;
            } else if (nuevoProceso.getDescripcion().isEmpty()) {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarProceso.getCodigo() <= 0 || duplicarProceso.getTipopago().getSecuencia() == null) {
                retorno = false;
            }
            if (duplicarProceso.getDescripcion() == null) {
                retorno = false;
            } else if (duplicarProceso.getDescripcion().isEmpty()) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarCamposNulosFormulaProceso(int i) {
        boolean retorno = true;
        if (i == 1) {
            if (nuevoFormulaProceso.getFormula().getSecuencia() == null) {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarFormulaProceso.getFormula().getSecuencia() == null) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarCamposNulosOperandoLog(int i) {
        boolean retorno = true;
        if (i == 1) {
            if (nuevoOperandoLog.getOperando().getSecuencia() == null) {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarOperandoLog.getOperando().getSecuencia() == null) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void procesoModificacionProceso(int i) {
        index = i;
        boolean respuesta = validarCamposNulosProceso(0);
        if (respuesta == true) {
            modificarProceso(i);
        } else {
            if (tipoLista == 0) {
                listaProcesos.get(index).setCodigo(auxCodigoProceso);
                listaProcesos.get(index).setDescripcion(auxDescripcionProceso);
            }
            if (tipoLista == 1) {
                filtrarListaProcesos.get(index).setCodigo(auxCodigoProceso);
                filtrarListaProcesos.get(index).setDescripcion(auxDescripcionProceso);
            }
            index = -1;
            secRegistro = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosProceso");
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullProceso').show()");
        }
    }

    public void modificarProceso(int indice) {
        int tamDes = 0;
        if (tipoLista == 0) {
            tamDes = listaProcesos.get(indice).getDescripcion().length();
        }
        if (tipoLista == 1) {
            tamDes = filtrarListaProcesos.get(indice).getDescripcion().length();
        }
        if (tamDes >= 1 && tamDes <= 30) {
            if (tipoLista == 0) {
                String textM = listaProcesos.get(indice).getDescripcion().toUpperCase();
                listaProcesos.get(indice).setDescripcion(textM);
                String textC = listaProcesos.get(indice).getComentarios().toUpperCase();
                listaProcesos.get(indice).setComentarios(textC);
                if (!listProcesosCrear.contains(listaProcesos.get(indice))) {
                    if (listProcesosModificar.isEmpty()) {
                        listProcesosModificar.add(listaProcesos.get(indice));
                    } else if (!listProcesosModificar.contains(listaProcesos.get(indice))) {
                        listProcesosModificar.add(listaProcesos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
            if (tipoLista == 1) {
                String textM = filtrarListaProcesos.get(indice).getDescripcion().toUpperCase();
                listaProcesos.get(indice).setDescripcion(textM);
                String textC = filtrarListaProcesos.get(indice).getComentarios().toUpperCase();
                listaProcesos.get(indice).setComentarios(textC);
                if (!listProcesosCrear.contains(filtrarListaProcesos.get(indice))) {
                    if (listProcesosModificar.isEmpty()) {
                        listProcesosModificar.add(filtrarListaProcesos.get(indice));
                    } else if (!listProcesosModificar.contains(filtrarListaProcesos.get(indice))) {
                        listProcesosModificar.add(filtrarListaProcesos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
            index = -1;
            secRegistro = null;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosProceso");
        } else {
            if (tipoLista == 0) {
                listaProcesos.get(index).setDescripcion(auxDescripcionProceso);
            }
            if (tipoLista == 1) {
                filtrarListaProcesos.get(index).setDescripcion(auxDescripcionProceso);
            }
            index = -1;
            secRegistro = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosProceso");
            RequestContext.getCurrentInstance().execute("PF('errorDescripcionProceso').show()");
        }
    }

    public void modificarProceso(int indice, String confirmarCambio, String valorConfirmar) {
        indexFormulasProcesos = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOPAGO")) {
            if (tipoListaFormulasProcesos == 0) {
                listaProcesos.get(indice).getTipopago().setDescripcion(tipoPago);
            } else {
                filtrarListaProcesos.get(indice).getTipopago().setDescripcion(tipoPago);
            }
            for (int i = 0; i < lovTiposPagos.size(); i++) {
                if (lovTiposPagos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaFormulasProcesos == 0) {
                    listaProcesos.get(indice).setTipopago(lovTiposPagos.get(indiceUnicoElemento));
                } else {
                    filtrarListaProcesos.get(indice).setTipopago(lovTiposPagos.get(indiceUnicoElemento));
                }
                lovTiposPagos.clear();
                getLovTiposPagos();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("form:TipoPagoDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoPagoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                String textM = listaProcesos.get(indice).getDescripcion().toUpperCase();
                listaProcesos.get(indice).setDescripcion(textM);
                if (!listProcesosCrear.contains(listaProcesos.get(indice))) {
                    if (listProcesosModificar.isEmpty()) {
                        listProcesosModificar.add(listaProcesos.get(indice));
                    } else if (!listProcesosModificar.contains(listaProcesos.get(indice))) {
                        listProcesosModificar.add(listaProcesos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
            if (tipoLista == 1) {
                String textM = filtrarListaProcesos.get(indice).getDescripcion().toUpperCase();
                filtrarListaProcesos.get(indice).setDescripcion(textM);
                if (!listProcesosCrear.contains(filtrarListaProcesos.get(indice))) {
                    if (listProcesosModificar.isEmpty()) {
                        listProcesosModificar.add(filtrarListaProcesos.get(indice));
                    } else if (!listProcesosModificar.contains(filtrarListaProcesos.get(indice))) {
                        listProcesosModificar.add(filtrarListaProcesos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosProceso");
    }

    public void modificarFormulaProceso(int indice) {
        if (tipoListaFormulasProcesos == 0) {
            if (!listFormulasProcesosCrear.contains(listaFormulasProcesos.get(indice))) {
                if (listFormulasProcesosModificar.isEmpty()) {
                    listFormulasProcesosModificar.add(listaFormulasProcesos.get(indice));
                } else if (!listFormulasProcesosModificar.contains(listaFormulasProcesos.get(indice))) {
                    listFormulasProcesosModificar.add(listaFormulasProcesos.get(indice));
                }
                if (guardadoFormulasProcesos == true) {
                    guardadoFormulasProcesos = false;
                }
            }
        }
        if (tipoListaFormulasProcesos == 1) {
            if (!listFormulasProcesosCrear.contains(filtrarListaFormulasProcesos.get(indice))) {
                if (listFormulasProcesosModificar.isEmpty()) {
                    listFormulasProcesosModificar.add(filtrarListaFormulasProcesos.get(indice));
                } else if (!listFormulasProcesosModificar.contains(filtrarListaFormulasProcesos.get(indice))) {
                    listFormulasProcesosModificar.add(filtrarListaFormulasProcesos.get(indice));
                }
                if (guardadoFormulasProcesos == true) {
                    guardadoFormulasProcesos = false;
                }
            }
        }
        indexFormulasProcesos = -1;
        secRegistroFormulaProceso = null;
        cambiosPagina = false;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
    }

    public void modificarFormulaProceso(int indice, String confirmarCambio, String valorConfirmar) {
        indexFormulasProcesos = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            if (tipoListaFormulasProcesos == 0) {
                listaFormulasProcesos.get(indice).getFormula().setNombrelargo(formula);
            } else {
                filtrarListaFormulasProcesos.get(indice).getFormula().setNombrelargo(formula);
            }
            for (int i = 0; i < lovFormulas.size(); i++) {
                if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaFormulasProcesos == 0) {
                    listaFormulasProcesos.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
                } else {
                    filtrarListaFormulasProcesos.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
                }
                lovFormulas.clear();
                getLovFormulas();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndexFormulasProcesos = false;
                RequestContext.getCurrentInstance().update("form:FormulaDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoListaFormulasProcesos == 0) {
                if (!listFormulasProcesosCrear.contains(listaFormulasProcesos.get(indice))) {
                    if (listFormulasProcesosModificar.isEmpty()) {
                        listFormulasProcesosModificar.add(listaFormulasProcesos.get(indice));
                    } else if (!listFormulasProcesosModificar.contains(listaFormulasProcesos.get(indice))) {
                        listFormulasProcesosModificar.add(listaFormulasProcesos.get(indice));
                    }
                    if (guardadoFormulasProcesos == true) {
                        guardadoFormulasProcesos = false;
                    }
                }
            }
            if (tipoListaFormulasProcesos == 1) {
                if (!listFormulasProcesosCrear.contains(filtrarListaFormulasProcesos.get(indice))) {
                    if (listFormulasProcesosModificar.isEmpty()) {
                        listFormulasProcesosModificar.add(filtrarListaFormulasProcesos.get(indice));
                    } else if (!listFormulasProcesosModificar.contains(filtrarListaFormulasProcesos.get(indice))) {
                        listFormulasProcesosModificar.add(filtrarListaFormulasProcesos.get(indice));
                    }
                    if (guardadoFormulasProcesos == true) {
                        guardadoFormulasProcesos = false;
                    }
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
    }

    public void modificarOperandoLog(int indice) {
        if (tipoListaOperandoLog == 0) {
            if (!listOperandosLogsCrear.contains(listaOperandosLogs.get(indice))) {
                if (listOperandosLogsModificar.isEmpty()) {
                    listOperandosLogsModificar.add(listaOperandosLogs.get(indice));
                } else if (!listOperandosLogsModificar.contains(listaOperandosLogs.get(indice))) {
                    listOperandosLogsModificar.add(listaOperandosLogs.get(indice));
                }
                if (guardadoOperandosLogs == true) {
                    guardadoOperandosLogs = false;
                }
            }
        }
        if (tipoListaOperandoLog == 1) {
            if (!listOperandosLogsCrear.contains(filtrarListaOperandosLogs.get(indice))) {
                if (listOperandosLogsModificar.isEmpty()) {
                    listOperandosLogsModificar.add(filtrarListaOperandosLogs.get(indice));
                } else if (!listOperandosLogsModificar.contains(filtrarListaOperandosLogs.get(indice))) {
                    listOperandosLogsModificar.add(filtrarListaOperandosLogs.get(indice));
                }
                if (guardadoOperandosLogs == true) {
                    guardadoOperandosLogs = false;
                }
            }
        }
        indexOperandosLogs = -1;
        secRegistroOperandoLog = null;
        cambiosPagina = false;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosOperando");
    }

    public void modificarOperandoLog(int indice, String confirmarCambio, String valorConfirmar) {
        indexOperandosLogs = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("OPERANDO")) {
            if (tipoListaOperandoLog == 0) {
                listaOperandosLogs.get(indice).getOperando().setNombre(operando);
            } else {
                filtrarListaOperandosLogs.get(indice).getOperando().setNombre(operando);
            }
            for (int i = 0; i < lovOperandos.size(); i++) {
                if (lovOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaOperandoLog == 0) {
                    listaOperandosLogs.get(indice).setOperando(lovOperandos.get(indiceUnicoElemento));
                } else {
                    filtrarListaOperandosLogs.get(indice).setOperando(lovOperandos.get(indiceUnicoElemento));
                }
                lovOperandos.clear();
                getLovOperandos();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndexOperandosLogs = false;
                RequestContext.getCurrentInstance().update("form:OperandoDialogo");
                RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoListaOperandoLog == 0) {
                if (!listOperandosLogsCrear.contains(listaOperandosLogs.get(indice))) {
                    if (listOperandosLogsModificar.isEmpty()) {
                        listOperandosLogsModificar.add(listaOperandosLogs.get(indice));
                    } else if (!listOperandosLogsModificar.contains(listaOperandosLogs.get(indice))) {
                        listOperandosLogsModificar.add(listaOperandosLogs.get(indice));
                    }
                    if (guardadoOperandosLogs == true) {
                        guardadoOperandosLogs = false;
                    }
                }
            }
            if (tipoListaOperandoLog == 1) {
                if (!listOperandosLogsCrear.contains(filtrarListaOperandosLogs.get(indice))) {
                    if (listOperandosLogsModificar.isEmpty()) {
                        listOperandosLogsModificar.add(filtrarListaOperandosLogs.get(indice));
                    } else if (!listOperandosLogsModificar.contains(filtrarListaOperandosLogs.get(indice))) {
                        listOperandosLogsModificar.add(filtrarListaOperandosLogs.get(indice));
                    }
                    if (guardadoOperandosLogs == true) {
                        guardadoOperandosLogs = false;
                    }
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosOperando");
    }

    public void posicionOperando() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndiceOperandoLog(indice, columna);
    }

    public void cambiarIndice(int indice, int celda) {
        if (guardadoFormulasProcesos == true && guardadoOperandosLogs == true) {
            if (permitirIndex == true) {
                cualCelda = celda;
                indexAux = indice;
                index = indice;
                indexFormulasProcesos = -1;
                indexOperandosLogs = -1;
                if (tipoLista == 0) {
                    auxCodigoProceso = listaProcesos.get(index).getCodigo();
                    secRegistro = listaProcesos.get(index).getSecuencia();
                    auxDescripcionProceso = listaProcesos.get(index).getDescripcion();
                }
                if (tipoLista == 1) {
                    auxCodigoProceso = filtrarListaProcesos.get(index).getCodigo();
                    secRegistro = filtrarListaProcesos.get(index).getSecuencia();
                    auxDescripcionProceso = filtrarListaProcesos.get(index).getDescripcion();
                }
                RequestContext context = RequestContext.getCurrentInstance();
                listaFormulasProcesos = null;
                getListaFormulasProcesos();
                RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
                listaOperandosLogs = null;
                getListaOperandosLogs();
                RequestContext.getCurrentInstance().update("form:datosOperando");
                if (banderaFormulasProcesos == 1) {
                    altoTablaFormulasProcesos = "115";
                    formulaProcesoPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoPeriodicidad");
                    formulaProcesoPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
                    formulaProcesoFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoFormula");
                    formulaProcesoFormula.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
                    banderaFormulasProcesos = 0;
                    filtrarListaFormulasProcesos = null;
                    tipoListaFormulasProcesos = 0;
                }
                if (banderaOperandosLogs == 1) {
                    altoTablaOperandosLogs = "115";
                    operandoOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoOperando");
                    operandoOperando.setFilterStyle("display: none; visibility: hidden;");
                    operandoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoDescripcion");
                    operandoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosOperando");
                    banderaOperandosLogs = 0;
                    filtrarListaOperandosLogs = null;
                    tipoListaOperandoLog = 0;
                }
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void cambiarIndiceFormulaProceso(int indice, int celda) {
        if (permitirIndexFormulasProcesos == true) {
            indexFormulasProcesos = indice;
            index = indice;
            index = -1;
            indexOperandosLogs = -1;
            cualCeldaFormulaProceso = celda;
            if (tipoListaFormulasProcesos == 0) {
                secRegistroFormulaProceso = listaFormulasProcesos.get(indexFormulasProcesos).getSecuencia();
                formula = listaFormulasProcesos.get(indexFormulasProcesos).getFormula().getNombrelargo();
            }
            if (tipoListaFormulasProcesos == 1) {
                secRegistroFormulaProceso = filtrarListaFormulasProcesos.get(indexFormulasProcesos).getSecuencia();
                formula = filtrarListaFormulasProcesos.get(indexFormulasProcesos).getFormula().getNombrelargo();
            }

        }

    }

    public void cambiarIndiceOperandoLog(int indice, int celda) {
        if (permitirIndexOperandosLogs == true) {
            indexOperandosLogs = indice;
            index = -1;
            indexFormulasProcesos = -1;
            cualCeldaOperandoLog = celda;
            if (tipoListaOperandoLog == 0) {
                secRegistroOperandoLog = listaOperandosLogs.get(indexOperandosLogs).getSecuencia();
                operando = listaOperandosLogs.get(indexOperandosLogs).getOperando().getNombre();
            }
            if (tipoListaOperandoLog == 1) {
                secRegistroOperandoLog = filtrarListaOperandosLogs.get(indexOperandosLogs).getSecuencia();
                operando = filtrarListaOperandosLogs.get(indexOperandosLogs).getOperando().getNombre();
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
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == false || guardadoFormulasProcesos == false || guardadoOperandosLogs == false) {
            if (guardado == false) {
                guardarCambiosProceso();
            }
            if (guardadoFormulasProcesos == false) {
                guardarCambiosFormulaProceso();
            }
            if (guardadoOperandosLogs == false) {
                guardarCambiosOperandoLog();
            }

        }
    }

    public void guardarCambiosProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (!listProcesosBorrar.isEmpty()) {
                for (int i = 0; i < listProcesosBorrar.size(); i++) {
                    administrarProcesos.borrarProcesos(listProcesosBorrar);
                }
                listProcesosBorrar.clear();
            }
            if (!listProcesosCrear.isEmpty()) {
                for (int i = 0; i < listProcesosCrear.size(); i++) {
                    administrarProcesos.crearProcesos(listProcesosCrear);
                }
                listProcesosCrear.clear();
            }
            if (!listProcesosModificar.isEmpty()) {
                administrarProcesos.editarProcesos(listProcesosModificar);
                listProcesosModificar.clear();
            }
            listaProcesos = null;
            RequestContext.getCurrentInstance().update("form:datosProceso");
            guardado = true;
            k = 0;
            index = -1;
            secRegistro = null;
            FacesMessage msg = new FacesMessage("Información", "Los datos de Procesos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosProceso : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Procesos, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void guardarCambiosFormulaProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (!listFormulasProcesosBorrar.isEmpty()) {
                administrarProcesos.borrarFormulasProcesos(listFormulasProcesosBorrar);
                listFormulasProcesosBorrar.clear();
            }
            if (!listFormulasProcesosCrear.isEmpty()) {
                administrarProcesos.crearFormulasProcesos(listFormulasProcesosCrear);
                listFormulasProcesosCrear.clear();
            }
            if (!listFormulasProcesosModificar.isEmpty()) {
                administrarProcesos.editarFormulasProcesos(listFormulasProcesosModificar);
                listFormulasProcesosModificar.clear();
            }
            listaFormulasProcesos = null;
            RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
            guardadoFormulasProcesos = true;
            k = 0;
            indexFormulasProcesos = -1;
            secRegistroFormulaProceso = null;
            FacesMessage msg = new FacesMessage("Información", "Los datos de Formulas se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosFormulaProceso : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Formulas, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void guardarCambiosOperandoLog() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (!listOperandosLogsBorrar.isEmpty()) {
                administrarProcesos.borrarOperandosLogs(listOperandosLogsBorrar);
                listOperandosLogsBorrar.clear();
            }
            if (!listOperandosLogsCrear.isEmpty()) {
                administrarProcesos.crearOperandosLogs(listOperandosLogsCrear);
                listOperandosLogsCrear.clear();
            }
            if (!listOperandosLogsModificar.isEmpty()) {
                administrarProcesos.editarOperandosLogs(listOperandosLogsModificar);
                listOperandosLogsModificar.clear();
            }
            listaOperandosLogs = null;
            RequestContext.getCurrentInstance().update("form:datosOperando");
            guardadoOperandosLogs = true;
            k = 0;
            indexOperandosLogs = -1;
            secRegistroOperandoLog = null;
            FacesMessage msg = new FacesMessage("Información", "Los datos de Operandos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosFormulaProceso : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Operandos, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    //CANCELAR MODIFICACIONES
    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacionGeneral() {
        RequestContext context = RequestContext.getCurrentInstance();
        cancelarModificacionProceso();
        RequestContext.getCurrentInstance().update("form:datosProceso");
        cancelarModificacionFormulaProceso();
        RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
        cancelarModificacionOperandoLog();
        RequestContext.getCurrentInstance().update("form:datosOperando");
        cambiosPagina = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        procesoBaseClonado = new Procesos();
        procesoNuevoClonado = new Procesos();
        RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
        RequestContext.getCurrentInstance().update("form:CodigoNuevoClonado");
        RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
        RequestContext.getCurrentInstance().update("form:DescripcionNuevoClonado");
    }

    public void cancelarModificacionProceso() {
        if (bandera == 1) {
            altoTablaProcesos = "110";
            procesoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoCodigo");
            procesoCodigo.setFilterStyle("display: none; visibility: hidden;");
            procesoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoDescripcion");
            procesoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            procesoTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoTipoPago");
            procesoTipoPago.setFilterStyle("display: none; visibility: hidden;");
            procesoComentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoComentario");
            procesoComentario.setFilterStyle("display: none; visibility: hidden;");
            procesoNumero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoNumero");
            procesoNumero.setFilterStyle("display: none; visibility: hidden;");
            procesoContabilizacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoContabilizacion");
            procesoContabilizacion.setFilterStyle("display: none; visibility: hidden;");
            procesoSolucionNodo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoSolucionNodo");
            procesoSolucionNodo.setFilterStyle("display: none; visibility: hidden;");
            procesoAdelanto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoAdelanto");
            procesoAdelanto.setFilterStyle("display: none; visibility: hidden;");
            procesoSobregiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoSobregiro");
            procesoSobregiro.setFilterStyle("display: none; visibility: hidden;");
            procesoAutomatico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoAutomatico");
            procesoAutomatico.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosProceso");
            bandera = 0;
            filtrarListaProcesos = null;
            tipoLista = 0;
        }
        listProcesosBorrar.clear();
        listProcesosCrear.clear();
        listProcesosModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaProcesos = null;
        getListaProcesos();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosProceso");
    }

    public void cancelarModificacionFormulaProceso() {
        if (banderaFormulasProcesos == 1) {
            altoTablaFormulasProcesos = "115";
            formulaProcesoPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoPeriodicidad");
            formulaProcesoPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
            formulaProcesoFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoFormula");
            formulaProcesoFormula.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
            banderaFormulasProcesos = 0;
            filtrarListaFormulasProcesos = null;
            tipoListaFormulasProcesos = 0;
        }
        listFormulasProcesosBorrar.clear();
        listFormulasProcesosCrear.clear();
        listFormulasProcesosModificar.clear();
        indexFormulasProcesos = -1;
        secRegistroFormulaProceso = null;
        k = 0;
        listaFormulasProcesos = null;
        getListaFormulasProcesos();
        guardadoFormulasProcesos = true;
        permitirIndexFormulasProcesos = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
    }

    public void cancelarModificacionOperandoLog() {
        if (banderaOperandosLogs == 1) {
            altoTablaOperandosLogs = "115";
            operandoOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoOperando");
            operandoOperando.setFilterStyle("display: none; visibility: hidden;");
            operandoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoDescripcion");
            operandoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosOperando");
            banderaOperandosLogs = 0;
            filtrarListaOperandosLogs = null;
            tipoListaOperandoLog = 0;
        }
        listOperandosLogsBorrar.clear();
        listOperandosLogsCrear.clear();
        listOperandosLogsModificar.clear();
        indexOperandosLogs = -1;
        secRegistroOperandoLog = null;
        k = 0;
        listaOperandosLogs = null;
        getListaOperandosLogs();
        guardadoOperandosLogs = true;
        permitirIndexOperandosLogs = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosOperando");
    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarProceso = listaProcesos.get(index);
            }
            if (tipoLista == 1) {
                editarProceso = filtrarListaProcesos.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoProcesoD");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoProcesoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionProcesoD");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionProcesoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoPagoProcesoD");
                RequestContext.getCurrentInstance().execute("PF('editarTipoPagoProcesoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarComentarioProcesoD");
                RequestContext.getCurrentInstance().execute("PF('editarComentarioProcesoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNumeroProcesoD");
                RequestContext.getCurrentInstance().execute("PF('editarNumeroProcesoD').show()");
                cualCelda = -1;
            }
            index = -1;
            secRegistro = null;
        }
        if (indexFormulasProcesos >= 0) {
            if (tipoListaFormulasProcesos == 0) {
                editarFormulaProceso = listaFormulasProcesos.get(indexFormulasProcesos);
            }
            if (tipoListaFormulasProcesos == 1) {
                editarFormulaProceso = listaFormulasProcesos.get(indexFormulasProcesos);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaFormulaProceso == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaFormulaProceso");
                RequestContext.getCurrentInstance().execute("PF('editarFormulaFormulaProceso').show()");
                cualCeldaFormulaProceso = -1;
            }
            indexFormulasProcesos = -1;
            secRegistroFormulaProceso = null;
        }
        if (indexOperandosLogs >= 0) {
            if (tipoListaOperandoLog == 0) {
                editarOperandoLog = listaOperandosLogs.get(indexOperandosLogs);
            }
            if (tipoListaOperandoLog == 1) {
                editarOperandoLog = listaOperandosLogs.get(indexOperandosLogs);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaOperandoLog == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarOperandoOperandoLogD");
                RequestContext.getCurrentInstance().execute("PF('editarOperandoOperandoLogD').show()");
                cualCeldaOperandoLog = -1;
            } else if (cualCeldaOperandoLog == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionOperandoLogD");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionOperandoLogD').show()");
                cualCeldaOperandoLog = -1;
            }
            indexOperandosLogs = -1;
            secRegistroOperandoLog = null;
        }
    }

    public void dialogoNuevoRegistro() {
        if (guardado == false || guardadoFormulasProcesos == false || guardadoOperandosLogs == false) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            int tam1 = listaProcesos.size();
            int tam2 = listaFormulasProcesos.size();
            int tam3 = listaOperandosLogs.size();
            if (tam1 == 0 || tam2 == 0 || tam3 == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
                RequestContext.getCurrentInstance().execute("PF('verificarNuevoRegistro').show()");
            } else {
                if (index >= 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroProceso");
                    RequestContext.getCurrentInstance().execute("PF('NuevoRegistroProceso').show()");
                }
                if (indexFormulasProcesos >= 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroFormulaProceso");
                    RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormulaProceso').show()");
                }
                if (indexOperandosLogs >= 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroOperandoLog");
                    RequestContext.getCurrentInstance().execute("PF('NuevoRegistroOperandoLog').show()");
                }
            }
        }
    }

    //CREAR 
    public void agregarNuevoProceso() {
        boolean respueta = validarCamposNulosProceso(1);
        if (respueta == true) {
            int tamDes;
            tamDes = nuevoProceso.getDescripcion().length();
            if (tamDes >= 1 && tamDes <= 30) {
                if (bandera == 1) {
                    altoTablaProcesos = "110";
                    procesoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoCodigo");
                    procesoCodigo.setFilterStyle("display: none; visibility: hidden;");
                    procesoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoDescripcion");
                    procesoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    procesoTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoTipoPago");
                    procesoTipoPago.setFilterStyle("display: none; visibility: hidden;");
                    procesoComentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoComentario");
                    procesoComentario.setFilterStyle("display: none; visibility: hidden;");
                    procesoNumero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoNumero");
                    procesoNumero.setFilterStyle("display: none; visibility: hidden;");
                    procesoContabilizacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoContabilizacion");
                    procesoContabilizacion.setFilterStyle("display: none; visibility: hidden;");
                    procesoSolucionNodo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoSolucionNodo");
                    procesoSolucionNodo.setFilterStyle("display: none; visibility: hidden;");
                    procesoAdelanto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoAdelanto");
                    procesoAdelanto.setFilterStyle("display: none; visibility: hidden;");
                    procesoSobregiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoSobregiro");
                    procesoSobregiro.setFilterStyle("display: none; visibility: hidden;");
                    procesoAutomatico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoAutomatico");
                    procesoAutomatico.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosProceso");
                    bandera = 0;
                    filtrarListaProcesos = null;
                    tipoLista = 0;
                }

                k++;
                l = BigInteger.valueOf(k);
                String text = nuevoProceso.getDescripcion().toUpperCase();
                nuevoProceso.setDescripcion(text);
                nuevoProceso.setSecuencia(l);
                listProcesosCrear.add(nuevoProceso);
                listaProcesos.add(nuevoProceso);
                nuevoProceso = new Procesos();
                nuevoProceso.setTipopago(new Tipospagos());
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosProceso");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroProceso').hide()");
                if (guardado == true) {
                    guardado = false;
                }
                index = -1;
                secRegistro = null;
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorDescripcionProceso').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullProceso').show()");
        }
    }

    public void agregarNuevoFormulaProceso() {
        boolean respueta = validarCamposNulosFormulaProceso(1);
        if (respueta == true) {
            if (banderaFormulasProcesos == 1) {
                altoTablaFormulasProcesos = "115";
                formulaProcesoPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoPeriodicidad");
                formulaProcesoPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
                formulaProcesoFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoFormula");
                formulaProcesoFormula.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
                banderaFormulasProcesos = 0;
                filtrarListaFormulasProcesos = null;
                tipoListaFormulasProcesos = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoFormulaProceso.setSecuencia(l);
            if (tipoLista == 0) {
                nuevoFormulaProceso.setProceso(listaProcesos.get(indexAux));
            }
            if (tipoLista == 1) {
                nuevoFormulaProceso.setProceso(filtrarListaProcesos.get(indexAux));
            }
            if (listaFormulasProcesos.size() == 0) {
                listaFormulasProcesos = new ArrayList<FormulasProcesos>();
            }
            listFormulasProcesosCrear.add(nuevoFormulaProceso);
            listaFormulasProcesos.add(nuevoFormulaProceso);
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            index = indexAux;
            RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormulaProceso').hide()");
            nuevoFormulaProceso = new FormulasProcesos();
            nuevoFormulaProceso.setFormula(new Formulas());
            if (guardadoFormulasProcesos == true) {
                guardadoFormulasProcesos = false;
            }
            indexFormulasProcesos = -1;
            secRegistroFormulaProceso = null;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullFormulaProceso').show()");
        }
    }

    public void agregarNuevoOperandoLog() {
        if (nuevoOperandoLog.getOperando().getSecuencia() != null) {
            if (banderaOperandosLogs == 1) {
                altoTablaOperandosLogs = "115";
                operandoOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoOperando");
                operandoOperando.setFilterStyle("display: none; visibility: hidden;");
                operandoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoDescripcion");
                operandoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosOperando");
                banderaOperandosLogs = 0;
                filtrarListaOperandosLogs = null;
                tipoListaOperandoLog = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoOperandoLog.setSecuencia(l);
            if (tipoLista == 0) {
                nuevoOperandoLog.setProceso(listaProcesos.get(indexAux));
            }
            if (tipoLista == 1) {
                nuevoOperandoLog.setProceso(filtrarListaProcesos.get(indexAux));
            }
            if (listaOperandosLogs.size() == 0) {
                listaOperandosLogs = new ArrayList<OperandosLogs>();
            }
            listOperandosLogsCrear.add(nuevoOperandoLog);
            listaOperandosLogs.add(nuevoOperandoLog);
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            index = indexAux;
            RequestContext.getCurrentInstance().update("form:datosOperando");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroOperandoLog').hide()");
            nuevoOperandoLog = new OperandosLogs();
            nuevoOperandoLog.setOperando(new Operandos());
            if (guardadoOperandosLogs == true) {
                guardadoOperandosLogs = false;
            }
            indexOperandosLogs = -1;
            secRegistroOperandoLog = null;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullOperando').show()");
        }
    }

    //LIMPIAR NUEVO REGISTRO
    /**
     */
    public void limpiarNuevaProceso() {
        nuevoProceso = new Procesos();
        nuevoProceso.setTipopago(new Tipospagos());
        index = -1;
        secRegistro = null;
    }

    public void limpiarNuevaFormulaProceso() {
        nuevoFormulaProceso = new FormulasProcesos();
        nuevoFormulaProceso.setFormula(new Formulas());
        indexFormulasProcesos = -1;
        secRegistroFormulaProceso = null;
    }

    public void limpiarNuevaOperandoLog() {
        nuevoOperandoLog = new OperandosLogs();
        nuevoOperandoLog.setOperando(new Operandos());
        indexOperandosLogs = -1;
        secRegistroOperandoLog = null;
    }

    //DUPLICAR VC
    /**
     */
    public void verificarRegistroDuplicar() {
        if (index >= 0) {
            duplicarProcesoM();
        }
        if (indexFormulasProcesos >= 0) {
            duplicarFormulaProcesoM();
        }
        if (indexOperandosLogs >= 0) {
            duplicarOperandoLogM();
        }
    }

    public void duplicarProcesoM() {
        duplicarProceso = new Procesos();
        if (tipoLista == 0) {
            duplicarProceso.setCodigo(listaProcesos.get(index).getCodigo());
            duplicarProceso.setDescripcion(listaProcesos.get(index).getDescripcion());
            duplicarProceso.setTipopago(listaProcesos.get(index).getTipopago());
            duplicarProceso.setContabilizacion(listaProcesos.get(index).getContabilizacion());
            duplicarProceso.setEliminarliqadelanto(listaProcesos.get(index).getEliminarliqadelanto());
            duplicarProceso.setEliminarliqsolucionnodo(listaProcesos.get(index).getEliminarliqsolucionnodo());
            duplicarProceso.setControlsobregiro(listaProcesos.get(index).getControlsobregiro());
            duplicarProceso.setAutomatico(listaProcesos.get(index).getAutomatico());
            duplicarProceso.setComentarios(listaProcesos.get(index).getComentarios());
            duplicarProceso.setNumerocierrerequerido(listaProcesos.get(index).getNumerocierrerequerido());
        } else {
            duplicarProceso.setCodigo(filtrarListaProcesos.get(index).getCodigo());
            duplicarProceso.setDescripcion(filtrarListaProcesos.get(index).getDescripcion());
            duplicarProceso.setTipopago(filtrarListaProcesos.get(index).getTipopago());
            duplicarProceso.setContabilizacion(filtrarListaProcesos.get(index).getContabilizacion());
            duplicarProceso.setEliminarliqadelanto(filtrarListaProcesos.get(index).getEliminarliqadelanto());
            duplicarProceso.setEliminarliqsolucionnodo(filtrarListaProcesos.get(index).getEliminarliqsolucionnodo());
            duplicarProceso.setControlsobregiro(filtrarListaProcesos.get(index).getControlsobregiro());
            duplicarProceso.setAutomatico(filtrarListaProcesos.get(index).getAutomatico());
            duplicarProceso.setComentarios(filtrarListaProcesos.get(index).getComentarios());
            duplicarProceso.setNumerocierrerequerido(filtrarListaProcesos.get(index).getNumerocierrerequerido());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroProceso");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroProceso').show()");
        index = -1;
        secRegistro = null;

    }

    public void duplicarFormulaProcesoM() {
        duplicarFormulaProceso = new FormulasProcesos();
        if (tipoListaFormulasProcesos == 0) {
            duplicarFormulaProceso.setFormula(listaFormulasProcesos.get(indexFormulasProcesos).getFormula());
            duplicarFormulaProceso.setPeriodicidadindependiente(listaFormulasProcesos.get(indexFormulasProcesos).getPeriodicidadindependiente());
        } else {
            duplicarFormulaProceso.setPeriodicidadindependiente(filtrarListaFormulasProcesos.get(indexFormulasProcesos).getPeriodicidadindependiente());
            duplicarFormulaProceso.setFormula(filtrarListaFormulasProcesos.get(indexFormulasProcesos).getFormula());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroFormulaProceso");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormulaProceso').show()");
        indexFormulasProcesos = -1;
        secRegistroFormulaProceso = null;

    }

    public void duplicarOperandoLogM() {
        duplicarOperandoLog = new OperandosLogs();
        if (tipoListaOperandoLog == 0) {
            duplicarOperandoLog.setOperando(listaOperandosLogs.get(indexOperandosLogs).getOperando());
        } else {
            duplicarOperandoLog.setOperando(filtrarListaOperandosLogs.get(indexOperandosLogs).getOperando());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroOperandoLog");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroOperandoLog').show()");
        indexOperandosLogs = -1;
        secRegistroOperandoLog = null;

    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
     */
    public void confirmarDuplicarProceso() {
        boolean respueta = validarCamposNulosProceso(2);
        if (respueta == true) {
            int tamDes = 0;
            tamDes = nuevoProceso.getDescripcion().length();
            if (tamDes >= 1 && tamDes <= 30) {
                if (bandera == 1) {
                    altoTablaProcesos = "110";
                    procesoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:tipoSueldoCodigo");
                    procesoCodigo.setFilterStyle("display: none; visibility: hidden;");
                    procesoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:tipoSueldoDescripcion");
                    procesoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    procesoTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:tipoSueldoCap");
                    procesoTipoPago.setFilterStyle("display: none; visibility: hidden;");
                    procesoComentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:tipoSueldoBas");
                    procesoComentario.setFilterStyle("display: none; visibility: hidden;");
                    procesoNumero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:tipoSueldoAdi");
                    procesoNumero.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosProceso");
                    bandera = 0;
                    filtrarListaProcesos = null;
                    tipoLista = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                duplicarProceso.setSecuencia(l);
                if (duplicarProceso.getDescripcion() != null) {
                    String text = duplicarProceso.getDescripcion().toUpperCase();
                    duplicarProceso.setDescripcion(text);
                }
                listaProcesos.add(duplicarProceso);
                listProcesosCrear.add(duplicarProceso);
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosProceso");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroProceso').hide()");
                index = -1;
                secRegistro = null;
                if (guardado == true) {
                    guardado = false;
                }
                duplicarProceso = new Procesos();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorDescripcionProceso').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullProceso').show()");
        }
    }

    public void confirmarDuplicarFormulaProceso() {
        boolean respueta = validarCamposNulosFormulaProceso(2);
        if (respueta == true) {
            if (banderaFormulasProcesos == 1) {
                altoTablaFormulasProcesos = "115";
                formulaProcesoPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoPeriodicidad");
                formulaProcesoPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
                formulaProcesoFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoFormula");
                formulaProcesoFormula.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
                banderaFormulasProcesos = 0;
                filtrarListaFormulasProcesos = null;
                tipoListaFormulasProcesos = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarFormulaProceso.setSecuencia(l);
            if (tipoLista == 0) {
                duplicarFormulaProceso.setProceso(listaProcesos.get(indexAux));
            }
            if (tipoLista == 1) {
                duplicarFormulaProceso.setProceso(filtrarListaProcesos.get(indexAux));
            }
            listaFormulasProcesos.add(duplicarFormulaProceso);
            listFormulasProcesosCrear.add(duplicarFormulaProceso);
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormulaProceso').hide()");
            indexFormulasProcesos = -1;
            secRegistroFormulaProceso = null;
            if (guardadoFormulasProcesos == true) {
                guardadoFormulasProcesos = false;
            }
            duplicarFormulaProceso = new FormulasProcesos();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullFormulaProceso').show()");
        }
    }

    public void confirmarDuplicarOperandoLog() {
        if (duplicarOperandoLog.getOperando().getSecuencia() != null) {
            if (banderaOperandosLogs == 1) {
                altoTablaOperandosLogs = "115";
                operandoOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoOperando");
                operandoOperando.setFilterStyle("display: none; visibility: hidden;");
                operandoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoDescripcion");
                operandoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosOperando");
                banderaOperandosLogs = 0;
                filtrarListaOperandosLogs = null;
                tipoListaOperandoLog = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarOperandoLog.setSecuencia(l);
            if (tipoLista == 0) {
                duplicarOperandoLog.setProceso(listaProcesos.get(indexAux));
            }
            if (tipoLista == 1) {
                duplicarOperandoLog.setProceso(filtrarListaProcesos.get(indexAux));
            }
            listaOperandosLogs.add(duplicarOperandoLog);
            listOperandosLogsCrear.add(duplicarOperandoLog);
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosOperando");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroOperandoLog').hide()");
            indexOperandosLogs = -1;
            secRegistroOperandoLog = null;
            if (guardadoOperandosLogs == true) {
                guardadoOperandosLogs = false;
            }

            duplicarOperandoLog = new OperandosLogs();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullOperando').show()");
        }
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar Set
     */
    public void limpiarDuplicarProceso() {
        duplicarProceso = new Procesos();
        duplicarProceso.setTipopago(new Tipospagos());
    }

    public void limpiarDuplicarFormulaProceso() {
        duplicarFormulaProceso = new FormulasProcesos();
        duplicarFormulaProceso.setFormula(new Formulas());
    }

    public void limpiarDuplicarOperandoLog() {
        duplicarOperandoLog = new OperandosLogs();
        duplicarOperandoLog.setOperando(new Operandos());
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
            int tam = listaFormulasProcesos.size();
            int tam1 = listaOperandosLogs.size();
            if (tam == 0 && tam1 == 0) {
                borrarProceso();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistro').show()");
            }
        }
        if (indexFormulasProcesos >= 0) {
            borrarFormulaProceso();
        }
        if (indexOperandosLogs >= 0) {
            borrarOperandoLog();
        }
    }

    public void borrarProceso() {
        if (tipoLista == 0) {
            if (!listProcesosModificar.isEmpty() && listProcesosModificar.contains(listaProcesos.get(index))) {
                int modIndex = listProcesosModificar.indexOf(listaProcesos.get(index));
                listProcesosModificar.remove(modIndex);
                listProcesosBorrar.add(listaProcesos.get(index));
            } else if (!listProcesosCrear.isEmpty() && listProcesosCrear.contains(listaProcesos.get(index))) {
                int crearIndex = listProcesosCrear.indexOf(listaProcesos.get(index));
                listProcesosCrear.remove(crearIndex);
            } else {
                listProcesosBorrar.add(listaProcesos.get(index));
            }
            listaProcesos.remove(index);
        }
        if (tipoLista == 1) {
            if (!listProcesosModificar.isEmpty() && listProcesosModificar.contains(filtrarListaProcesos.get(index))) {
                int modIndex = listProcesosModificar.indexOf(filtrarListaProcesos.get(index));
                listProcesosModificar.remove(modIndex);
                listProcesosBorrar.add(filtrarListaProcesos.get(index));
            } else if (!listProcesosCrear.isEmpty() && listProcesosCrear.contains(filtrarListaProcesos.get(index))) {
                int crearIndex = listProcesosCrear.indexOf(filtrarListaProcesos.get(index));
                listProcesosCrear.remove(crearIndex);
            } else {
                listProcesosBorrar.add(filtrarListaProcesos.get(index));
            }
            int VCIndex = listaProcesos.indexOf(filtrarListaProcesos.get(index));
            listaProcesos.remove(VCIndex);
            filtrarListaProcesos.remove(index);
        }

        cambiosPagina = false;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosProceso");
        index = -1;
        secRegistro = null;

        if (guardado == true) {
            guardado = false;
        }
    }

    public void borrarFormulaProceso() {
        if (tipoListaFormulasProcesos == 0) {
            if (!listFormulasProcesosModificar.isEmpty() && listFormulasProcesosModificar.contains(listaFormulasProcesos.get(indexFormulasProcesos))) {
                int modIndex = listFormulasProcesosModificar.indexOf(listaFormulasProcesos.get(indexFormulasProcesos));
                listFormulasProcesosModificar.remove(modIndex);
                listFormulasProcesosBorrar.add(listaFormulasProcesos.get(indexFormulasProcesos));
            } else if (!listFormulasProcesosCrear.isEmpty() && listFormulasProcesosCrear.contains(listaFormulasProcesos.get(indexFormulasProcesos))) {
                int crearIndex = listFormulasProcesosCrear.indexOf(listaFormulasProcesos.get(indexFormulasProcesos));
                listFormulasProcesosCrear.remove(crearIndex);
            } else {
                listFormulasProcesosBorrar.add(listaFormulasProcesos.get(indexFormulasProcesos));
            }
            listaFormulasProcesos.remove(indexFormulasProcesos);
        }
        if (tipoListaFormulasProcesos == 1) {
            if (!listFormulasProcesosModificar.isEmpty() && listFormulasProcesosModificar.contains(filtrarListaFormulasProcesos.get(indexFormulasProcesos))) {
                int modIndex = listFormulasProcesosModificar.indexOf(filtrarListaFormulasProcesos.get(indexFormulasProcesos));
                listFormulasProcesosModificar.remove(modIndex);
                listFormulasProcesosBorrar.add(filtrarListaFormulasProcesos.get(indexFormulasProcesos));
            } else if (!listFormulasProcesosCrear.isEmpty() && listFormulasProcesosCrear.contains(filtrarListaFormulasProcesos.get(indexFormulasProcesos))) {
                int crearIndex = listFormulasProcesosCrear.indexOf(filtrarListaFormulasProcesos.get(indexFormulasProcesos));
                listFormulasProcesosCrear.remove(crearIndex);
            } else {
                listFormulasProcesosBorrar.add(filtrarListaFormulasProcesos.get(indexFormulasProcesos));
            }
            int VCIndex = listaFormulasProcesos.indexOf(filtrarListaFormulasProcesos.get(indexFormulasProcesos));
            listaFormulasProcesos.remove(VCIndex);
            filtrarListaFormulasProcesos.remove(indexFormulasProcesos);
        }

        cambiosPagina = false;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
        indexFormulasProcesos = -1;
        secRegistroFormulaProceso = null;

        if (guardadoFormulasProcesos == true) {
            guardadoFormulasProcesos = false;
        }
    }

    public void borrarOperandoLog() {
        if (tipoListaOperandoLog == 0) {
            if (!listOperandosLogsModificar.isEmpty() && listOperandosLogsModificar.contains(listaOperandosLogs.get(indexOperandosLogs))) {
                int modIndex = listOperandosLogsModificar.indexOf(listaOperandosLogs.get(indexOperandosLogs));
                listOperandosLogsModificar.remove(modIndex);
                listOperandosLogsBorrar.add(listaOperandosLogs.get(indexOperandosLogs));
            } else if (!listOperandosLogsCrear.isEmpty() && listOperandosLogsCrear.contains(listaOperandosLogs.get(indexOperandosLogs))) {
                int crearIndex = listOperandosLogsCrear.indexOf(listaOperandosLogs.get(indexOperandosLogs));
                listOperandosLogsCrear.remove(crearIndex);
            } else {
                listOperandosLogsBorrar.add(listaOperandosLogs.get(indexOperandosLogs));
            }
            listaOperandosLogs.remove(indexOperandosLogs);
        }
        if (tipoListaOperandoLog == 1) {
            if (!listOperandosLogsModificar.isEmpty() && listOperandosLogsModificar.contains(filtrarListaOperandosLogs.get(indexOperandosLogs))) {
                int modIndex = listOperandosLogsModificar.indexOf(filtrarListaOperandosLogs.get(indexOperandosLogs));
                listOperandosLogsModificar.remove(modIndex);
                listOperandosLogsBorrar.add(filtrarListaOperandosLogs.get(indexOperandosLogs));
            } else if (!listOperandosLogsCrear.isEmpty() && listOperandosLogsCrear.contains(filtrarListaOperandosLogs.get(indexOperandosLogs))) {
                int crearIndex = listOperandosLogsCrear.indexOf(filtrarListaOperandosLogs.get(indexOperandosLogs));
                listOperandosLogsCrear.remove(crearIndex);
            } else {
                listOperandosLogsBorrar.add(filtrarListaOperandosLogs.get(indexOperandosLogs));
            }
            int VCIndex = listaOperandosLogs.indexOf(filtrarListaOperandosLogs.get(indexOperandosLogs));
            listaOperandosLogs.remove(VCIndex);
            filtrarListaOperandosLogs.remove(indexOperandosLogs);
        }

        cambiosPagina = false;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosOperando");
        indexOperandosLogs = -1;
        secRegistroOperandoLog = null;

        if (guardadoOperandosLogs == true) {
            guardadoOperandosLogs = false;
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
                altoTablaProcesos = "90";
                procesoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoCodigo");
                procesoCodigo.setFilterStyle("width: 85% !important");
                procesoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoDescripcion");
                procesoDescripcion.setFilterStyle("width: 85% !important");
                procesoTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoTipoPago");
                procesoTipoPago.setFilterStyle("width: 85% !important");
                procesoComentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoComentario");
                procesoComentario.setFilterStyle("width: 85% !important;");
                procesoNumero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoNumero");
                procesoNumero.setFilterStyle("width: 85% !important");
                procesoContabilizacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoContabilizacion");
                procesoContabilizacion.setFilterStyle("width: 85% !important");
                procesoSolucionNodo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoSolucionNodo");
                procesoSolucionNodo.setFilterStyle("width: 85% !important");
                procesoAdelanto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoAdelanto");
                procesoAdelanto.setFilterStyle("width: 85% !important");
                procesoSobregiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoSobregiro");
                procesoSobregiro.setFilterStyle("width: 85% !important");
                procesoAutomatico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoAutomatico");
                procesoAutomatico.setFilterStyle("width: 85% !important");
                RequestContext.getCurrentInstance().update("form:datosProceso");

                bandera = 1;
            } else if (bandera == 1) {
                altoTablaProcesos = "110";
                procesoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoCodigo");
                procesoCodigo.setFilterStyle("display: none; visibility: hidden;");
                procesoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoDescripcion");
                procesoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                procesoTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoTipoPago");
                procesoTipoPago.setFilterStyle("display: none; visibility: hidden;");
                procesoComentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoComentario");
                procesoComentario.setFilterStyle("display: none; visibility: hidden;");
                procesoNumero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoNumero");
                procesoNumero.setFilterStyle("display: none; visibility: hidden;");
                procesoContabilizacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoContabilizacion");
                procesoContabilizacion.setFilterStyle("display: none; visibility: hidden;");
                procesoSolucionNodo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoSolucionNodo");
                procesoSolucionNodo.setFilterStyle("display: none; visibility: hidden;");
                procesoAdelanto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoAdelanto");
                procesoAdelanto.setFilterStyle("display: none; visibility: hidden;");
                procesoSobregiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoSobregiro");
                procesoSobregiro.setFilterStyle("display: none; visibility: hidden;");
                procesoAutomatico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoAutomatico");
                procesoAutomatico.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosProceso");
                bandera = 0;
                filtrarListaProcesos = null;
                tipoLista = 0;
            }
        }
        if (indexFormulasProcesos >= 0) {
            if (banderaFormulasProcesos == 0) {
                altoTablaFormulasProcesos = "93";
                formulaProcesoPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoPeriodicidad");
                formulaProcesoPeriodicidad.setFilterStyle("width: 85% !important;");
                formulaProcesoFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoFormula");
                formulaProcesoFormula.setFilterStyle("width: 10px");
                RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
                RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
                banderaFormulasProcesos = 1;
            } else if (banderaFormulasProcesos == 1) {
                altoTablaFormulasProcesos = "115";
                formulaProcesoPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoPeriodicidad");
                formulaProcesoPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
                formulaProcesoFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoFormula");
                formulaProcesoFormula.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
                banderaFormulasProcesos = 0;
                filtrarListaFormulasProcesos = null;
                tipoListaFormulasProcesos = 0;
            }
        }
        if (indexOperandosLogs >= 0) {
            if (banderaOperandosLogs == 0) {
                altoTablaOperandosLogs = "93";
                operandoOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoOperando");
                operandoOperando.setFilterStyle("width: 85% !important;");
                operandoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoDescripcion");
                operandoDescripcion.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:datosOperando");
                banderaOperandosLogs = 1;
            } else if (banderaOperandosLogs == 1) {
                altoTablaOperandosLogs = "115";
                operandoOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoOperando");
                operandoOperando.setFilterStyle("display: none; visibility: hidden;");
                operandoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoDescripcion");
                operandoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosOperando");
                banderaOperandosLogs = 0;
                filtrarListaOperandosLogs = null;
                tipoListaOperandoLog = 0;
            }
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        System.out.println("Entro en Salud");
        if (bandera == 1) {
            System.out.println("Entro en bandera == 1");
            altoTablaProcesos = "110";
            procesoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoCodigo");
            procesoCodigo.setFilterStyle("display: none; visibility: hidden;");
            procesoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoDescripcion");
            procesoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            procesoTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoTipoPago");
            procesoTipoPago.setFilterStyle("display: none; visibility: hidden;");
            procesoComentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoComentario");
            procesoComentario.setFilterStyle("display: none; visibility: hidden;");
            procesoNumero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoNumero");
            procesoNumero.setFilterStyle("display: none; visibility: hidden;");
            procesoContabilizacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoContabilizacion");
            procesoContabilizacion.setFilterStyle("display: none; visibility: hidden;");
            procesoSolucionNodo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoSolucionNodo");
            procesoSolucionNodo.setFilterStyle("display: none; visibility: hidden;");
            procesoAdelanto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoAdelanto");
            procesoAdelanto.setFilterStyle("display: none; visibility: hidden;");
            procesoSobregiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoSobregiro");
            procesoSobregiro.setFilterStyle("display: none; visibility: hidden;");
            procesoAutomatico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoAutomatico");
            procesoAutomatico.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosProceso");
            bandera = 0;
            filtrarListaProcesos = null;
            tipoLista = 0;
        }
        if (banderaFormulasProcesos == 1) {
            System.out.println("Entro en banderaFormulasProcesos == 1");
            altoTablaFormulasProcesos = "115";
            formulaProcesoPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoPeriodicidad");
            formulaProcesoPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
            formulaProcesoFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoFormula");
            formulaProcesoFormula.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
            banderaFormulasProcesos = 0;
            filtrarListaFormulasProcesos = null;
            tipoListaFormulasProcesos = 0;
        }
        if (banderaOperandosLogs == 1) {
            System.out.println("Entro en banderaOperandosLogs == 1");
            altoTablaOperandosLogs = "115";
            operandoOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoOperando");
            operandoOperando.setFilterStyle("display: none; visibility: hidden;");
            operandoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoDescripcion");
            operandoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:operandoDescripcion");
            banderaOperandosLogs = 0;
            filtrarListaOperandosLogs = null;
            tipoListaOperandoLog = 0;
        }
        System.out.println("1");
        listProcesosBorrar.clear();
        listProcesosCrear.clear();
        listProcesosModificar.clear();
        listFormulasProcesosBorrar.clear();
        listFormulasProcesosCrear.clear();
        listFormulasProcesosModificar.clear();
        listOperandosLogsBorrar.clear();
        listOperandosLogsCrear.clear();
        listOperandosLogsModificar.clear();
        secRegistro = null;
        secRegistroFormulaProceso = null;
        secRegistroOperandoLog = null;
        System.out.println("2");
        k = 0;
        listaProcesos = null;
        listaFormulasProcesos = null;
        listaOperandosLogs = null;
        guardado = true;
        guardadoFormulasProcesos = true;
        guardadoOperandosLogs = true;
        cambiosPagina = true;
        lovFormulas = null;
        lovTiposPagos = null;
        lovOperandos = null;
        System.out.println("3");
        RequestContext context = RequestContext.getCurrentInstance();
        procesoBaseClonado = new Procesos();
        procesoNuevoClonado = new Procesos();
        RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
        RequestContext.getCurrentInstance().update("form:CodigoNuevoClonado");
        RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
        RequestContext.getCurrentInstance().update("form:DescripcionNuevoClonado");
        lovProcesos = null;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        System.out.println("4");
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (index >= 0) {
            if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("form:TipoPagoDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoPagoDialogo').show()");
                tipoActualizacion = 0;
            }

        }
        if (indexFormulasProcesos >= 0) {
            if (cualCeldaFormulaProceso == 0) {
                RequestContext.getCurrentInstance().update("form:FormulaDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (indexOperandosLogs >= 0) {
            if (cualCeldaOperandoLog == 0) {
                RequestContext.getCurrentInstance().update("form:OperandoDialogo");
                RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void asignarIndex(Integer indice, int dlg, int LND, int tabla) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tabla == 0) {
            if (LND == 0) {
                index = indice;
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dlg == 0) {
                RequestContext.getCurrentInstance().update("form:TipoPagoDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoPagoDialogo').show()");
            }
        }
        if (tabla == 1) {
            if (LND == 0) {
                indexFormulasProcesos = indice;
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
        }
        if (tabla == 2) {
            if (LND == 0) {
                indexOperandosLogs = indice;
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dlg == 0) {
                RequestContext.getCurrentInstance().update("form:OperandoDialogo");
                RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').show()");
            }
        }
    }

    public void valoresBackupAutocompletarGeneral(int tipoNuevo, String Campo) {
        if (Campo.equals("FORMULA")) {
            if (tipoNuevo == 1) {
                formula = nuevoFormulaProceso.getFormula().getNombrelargo();
            } else if (tipoNuevo == 2) {
                formula = duplicarFormulaProceso.getFormula().getNombrelargo();
            }
        }
        if (Campo.equals("TIPOPAGO")) {
            if (tipoNuevo == 1) {
                tipoPago = nuevoProceso.getTipopago().getDescripcion();
            } else if (tipoNuevo == 2) {
                tipoPago = duplicarProceso.getTipopago().getDescripcion();
            }
        }
        if (Campo.equals("OPERANDO")) {
            if (tipoNuevo == 1) {
                operando = nuevoOperandoLog.getOperando().getNombre();
            } else if (tipoNuevo == 2) {
                operando = duplicarOperandoLog.getOperando().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoProceso(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOPAGO")) {
            if (tipoNuevo == 1) {
                nuevoProceso.getTipopago().setDescripcion(tipoPago);
            } else if (tipoNuevo == 2) {
                duplicarProceso.getTipopago().setDescripcion(tipoPago);
            }
            for (int i = 0; i < lovTiposPagos.size(); i++) {
                if (lovTiposPagos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoProceso.setTipopago(lovTiposPagos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProcesoTipoPago");
                } else if (tipoNuevo == 2) {
                    duplicarProceso.setTipopago(lovTiposPagos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProcesoTipoPago");
                }
                lovTiposPagos.clear();
                getLovTiposPagos();
            } else {
                RequestContext.getCurrentInstance().update("form:TipoPagoDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoPagoDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProcesoTipoPago");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProcesoTipoPago");
                }
            }
        }
    }

    public void autocompletarNuevoyDuplicadoFormulaProceso(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            if (tipoNuevo == 1) {
                nuevoFormulaProceso.getFormula().setNombrelargo(tipoPago);
            } else if (tipoNuevo == 2) {
                duplicarFormulaProceso.getFormula().setNombrelargo(tipoPago);
            }
            for (int i = 0; i < lovFormulas.size(); i++) {
                if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoFormulaProceso.setFormula(lovFormulas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaProcesoFormula");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaProceso.setFormula(lovFormulas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaProcesoFormula");
                }
                lovFormulas.clear();
                getLovFormulas();
            } else {
                RequestContext.getCurrentInstance().update("form:FormulaDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaProcesoFormula");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaProcesoFormula");
                }
            }
        }
    }

    public void autocompletarNuevoyDuplicadoOperandoLog(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("OPERANDO")) {
            if (tipoNuevo == 1) {
                nuevoOperandoLog.getOperando().setNombre(operando);
            } else if (tipoNuevo == 2) {
                duplicarOperandoLog.getOperando().setNombre(operando);
            }
            for (int i = 0; i < lovOperandos.size(); i++) {
                if (lovOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoOperandoLog.setOperando(lovOperandos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoLogOperando");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoLogDescripcion");
                } else if (tipoNuevo == 2) {
                    duplicarOperandoLog.setOperando(lovOperandos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoLogOperando");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoLogOperando");
                }
                lovOperandos.clear();
                getLovOperandos();
            } else {
                RequestContext.getCurrentInstance().update("form:FormulaDialogo");
                RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoLogOperando");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoLogDescripcion");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoLogOperando");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoLogOperando");
                }
            }
        }
    }

    public void actualizarFormula() {
        if (tipoActualizacion == 0) {
            if (tipoListaFormulasProcesos == 0) {
                listaFormulasProcesos.get(indexFormulasProcesos).setFormula(formulaSeleccionado);
                if (!listFormulasProcesosCrear.contains(listaFormulasProcesos.get(indexFormulasProcesos))) {
                    if (listFormulasProcesosModificar.isEmpty()) {
                        listFormulasProcesosModificar.add(listaFormulasProcesos.get(indexFormulasProcesos));
                    } else if (!listFormulasProcesosModificar.contains(listaFormulasProcesos.get(indexFormulasProcesos))) {
                        listFormulasProcesosModificar.add(listaFormulasProcesos.get(indexFormulasProcesos));
                    }
                }
            } else {
                filtrarListaFormulasProcesos.get(indexFormulasProcesos).setFormula(formulaSeleccionado);
                if (!listFormulasProcesosCrear.contains(filtrarListaFormulasProcesos.get(indexFormulasProcesos))) {
                    if (listFormulasProcesosModificar.isEmpty()) {
                        listFormulasProcesosModificar.add(filtrarListaFormulasProcesos.get(indexFormulasProcesos));
                    } else if (!listFormulasProcesosModificar.contains(filtrarListaFormulasProcesos.get(indexFormulasProcesos))) {
                        listFormulasProcesosModificar.add(filtrarListaFormulasProcesos.get(indexFormulasProcesos));
                    }
                }
            }
            if (guardadoFormulasProcesos == true) {
                guardadoFormulasProcesos = false;
            }
            permitirIndexFormulasProcesos = true;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
        } else if (tipoActualizacion == 1) {
            nuevoFormulaProceso.setFormula(formulaSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaProcesoFormula");
        } else if (tipoActualizacion == 2) {
            duplicarFormulaProceso.setFormula(formulaSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaProcesoFormula");
        }
        filtrarLovFormulas = null;
        formulaSeleccionado = null;
        aceptar = true;
        indexFormulasProcesos = -1;
        secRegistroFormulaProceso = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:FormulaDialogo");
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
        indexFormulasProcesos = -1;
        secRegistroFormulaProceso = null;
        tipoActualizacion = -1;
        permitirIndexFormulasProcesos = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovFormula:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovFormula').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').hide()");
    }

    public void actualizarTipoPago() {
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaProcesos.get(index).setTipopago(tipoPagoSeleccionado);
                if (!listProcesosCrear.contains(listaProcesos.get(index))) {
                    if (listProcesosModificar.isEmpty()) {
                        listProcesosModificar.add(listaProcesos.get(index));
                    } else if (!listProcesosModificar.contains(listaProcesos.get(index))) {
                        listProcesosModificar.add(listaProcesos.get(index));
                    }
                }
            } else {
                filtrarListaProcesos.get(index).setTipopago(tipoPagoSeleccionado);
                if (!listProcesosCrear.contains(filtrarListaProcesos.get(index))) {
                    if (listProcesosModificar.isEmpty()) {
                        listProcesosModificar.add(filtrarListaProcesos.get(index));
                    } else if (!listProcesosModificar.contains(filtrarListaProcesos.get(index))) {
                        listProcesosModificar.add(filtrarListaProcesos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosProceso");
        } else if (tipoActualizacion == 1) {
            nuevoProceso.setTipopago(tipoPagoSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProcesoTipoPago");
        } else if (tipoActualizacion == 2) {
            duplicarProceso.setTipopago(tipoPagoSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProcesoTipoPago");
        }
        filtrarLovTiposPagos = null;
        tipoPagoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:TipoPagoDialogo");
         RequestContext.getCurrentInstance().update("form:lovTipoPago");
         RequestContext.getCurrentInstance().update("form:aceptarTP");*/
        context.reset("form:lovTipoPago:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoPago').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoPagoDialogo').hide()");
    }

    public void cancelarCambioTipoPago() {
        filtrarLovTiposPagos = null;
        tipoPagoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoPago:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoPago').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoPagoDialogo').hide()");
    }

    public void actualizarOperando() {
        if (tipoActualizacion == 0) {
            if (tipoListaOperandoLog == 0) {
                listaOperandosLogs.get(indexOperandosLogs).setOperando(operandoSeleccionado);
                if (!listOperandosLogsCrear.contains(listaOperandosLogs.get(indexOperandosLogs))) {
                    if (listOperandosLogsModificar.isEmpty()) {
                        listOperandosLogsModificar.add(listaOperandosLogs.get(indexOperandosLogs));
                    } else if (!listOperandosLogsModificar.contains(listaOperandosLogs.get(indexOperandosLogs))) {
                        listOperandosLogsModificar.add(listaOperandosLogs.get(indexOperandosLogs));
                    }
                }
            } else {
                filtrarListaOperandosLogs.get(indexOperandosLogs).setOperando(operandoSeleccionado);
                if (!listOperandosLogsCrear.contains(filtrarListaOperandosLogs.get(indexOperandosLogs))) {
                    if (listOperandosLogsModificar.isEmpty()) {
                        listOperandosLogsModificar.add(filtrarListaOperandosLogs.get(indexOperandosLogs));
                    } else if (!listOperandosLogsModificar.contains(filtrarListaOperandosLogs.get(indexOperandosLogs))) {
                        listOperandosLogsModificar.add(filtrarListaOperandosLogs.get(indexOperandosLogs));
                    }
                }
            }
            if (guardadoOperandosLogs == true) {
                guardadoOperandosLogs = false;
            }
            permitirIndexOperandosLogs = true;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosOperando");
        } else if (tipoActualizacion == 1) {
            nuevoOperandoLog.setOperando(operandoSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoLogOperando");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoLogDescripcion");
        } else if (tipoActualizacion == 2) {
            duplicarOperandoLog.setOperando(operandoSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoLogOperando");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoLogDescripcion");
        }
        filtrarLovOperandos = null;
        operandoSeleccionado = null;
        aceptar = true;
        indexOperandosLogs = -1;
        secRegistroOperandoLog = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:OperandoDialogo");
         RequestContext.getCurrentInstance().update("form:lovOperando");
         RequestContext.getCurrentInstance().update("form:aceptarO")*/
        context.reset("form:lovOperando:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovOperando').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').hide()");
    }

    public void cancelarCambioOperando() {
        filtrarLovOperandos = null;
        operandoSeleccionado = null;
        aceptar = true;
        indexOperandosLogs = -1;
        secRegistroOperandoLog = null;
        tipoActualizacion = -1;
        permitirIndexOperandosLogs = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovOperando:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovOperando').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').hide()");
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
            nombreTabla = ":formExportarP:datosProcesoExportar";
            nombreXML = "Procesos_XML";
        }
        if (indexFormulasProcesos >= 0) {
            nombreTabla = ":formExportarFP:datosFormulaProcesoExportar";
            nombreXML = "FormulasProcesos_XML";
        }
        if (indexOperandosLogs >= 0) {
            nombreTabla = ":formExportarOL:datosOperandoExportar";
            nombreXML = "OperandosLogs_XML";
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
            exportPDF_P();
        }
        if (indexFormulasProcesos >= 0) {
            exportPDF_FP();
        }
        if (indexOperandosLogs >= 0) {
            exportPDF_OL();
        }
    }

    public void exportPDF_P() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarP:datosProcesoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Procesos_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportPDF_FP() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarFP:datosFormulaProcesoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "FormulasProcesos_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexFormulasProcesos = -1;
        secRegistroFormulaProceso = null;
    }

    public void exportPDF_OL() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarOL:datosOperandoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "OperandosLogs_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexOperandosLogs = -1;
        secRegistroOperandoLog = null;
    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportXLS() throws IOException {
        if (index >= 0) {
            exportXLS_P();
        }
        if (indexFormulasProcesos >= 0) {
            exportXLS_FP();
        }
        if (indexOperandosLogs >= 0) {
            exportXLS_OL();
        }
    }

    public void exportXLS_P() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarP:datosProcesoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Procesos_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS_FP() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarFP:datosFormulaProcesoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "FormulasProcesos_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexFormulasProcesos = -1;
        secRegistroFormulaProceso = null;
    }

    public void exportXLS_OL() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarOL:datosOperandoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "OperandosLogs_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexOperandosLogs = -1;
        secRegistroOperandoLog = null;
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
        if (indexFormulasProcesos >= 0) {
            if (tipoListaFormulasProcesos == 0) {
                tipoListaFormulasProcesos = 1;
            }
        }
        if (indexOperandosLogs >= 0) {
            if (tipoListaOperandoLog == 0) {
                tipoListaOperandoLog = 1;
            }
        }
    }
    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {
        int tam = listaProcesos.size();
        int tam1 = listaFormulasProcesos.size();
        int tam2 = listaOperandosLogs.size();
        if (tam == 0 || tam1 == 0 || tam2 == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
        } else {
            if (index >= 0) {
                verificarRastroProceso();
                index = -1;
            }
            if (indexFormulasProcesos >= 0) {
                verificarRastroFormulaProceso();
                indexFormulasProcesos = -1;
            }
            if (indexOperandosLogs >= 0) {
                verificarRastroOperandoLog();
                indexOperandosLogs = -1;
            }
        }
    }

    public void verificarRastroProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaProcesos != null) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "PROCESOS");
                backUpSecRegistro = secRegistro;
                backUp = secRegistro;
                secRegistro = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "Procesos";
                    msnConfirmarRastro = "La tabla PROCESOS tiene rastros para el registro seleccionado, ¿desea continuar?";
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
        } else if (administrarRastros.verificarHistoricosTabla("PROCESOS")) {
            nombreTablaRastro = "Procesos";
            msnConfirmarRastroHistorico = "La tabla PROCESOS tiene rastros historicos, ¿Desea continuar?";
            RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    public void verificarRastroFormulaProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaFormulasProcesos != null) {
            if (secRegistroFormulaProceso != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroFormulaProceso, "FORMULASPROCESOS");
                backUpSecRegistroFormulaProceso = secRegistroFormulaProceso;
                backUp = secRegistroFormulaProceso;
                secRegistroFormulaProceso = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "FormulasProcesos";
                    msnConfirmarRastro = "La tabla FORMULASPROCESOS tiene rastros para el registro seleccionado, ¿desea continuar?";
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
        } else if (administrarRastros.verificarHistoricosTabla("FORMULASPROCESOS")) {
            nombreTablaRastro = "FormulasProcesos";
            msnConfirmarRastroHistorico = "La tabla FORMULASPROCESOS tiene rastros historicos, ¿Desea continuar?";
            RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        indexFormulasProcesos = -1;
    }

    public void verificarRastroOperandoLog() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaOperandosLogs != null) {
            if (secRegistroOperandoLog != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroOperandoLog, "OPERANDOSLOGS");
                backUpSecRegistroOperandoLog = secRegistroOperandoLog;
                backUp = backUpSecRegistroOperandoLog;
                secRegistroOperandoLog = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "OperandosLogs";
                    msnConfirmarRastro = "La tabla OPERANDOSLOGS tiene rastros para el registro seleccionado, ¿desea continuar?";
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
        } else if (administrarRastros.verificarHistoricosTabla("OPERANDOSLOGS")) {
            nombreTablaRastro = "OperandosLogs";
            msnConfirmarRastroHistorico = "La tabla OPERANDOSLOGS tiene rastros historicos, ¿Desea continuar?";
            RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        indexOperandosLogs = -1;
    }

    public void posicionProcesoClonado() {
        if (guardado == true) {
            auxClonadoCodigo = procesoBaseClonado.getCodigo();
            auxClonadoDescripcion = procesoBaseClonado.getDescripcion();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void autoCompletarSeleccionarProcesoClonado(String valorConfirmar, int tipoAutoCompletar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoAutoCompletar == 0) {
            short num = Short.parseShort(valorConfirmar);
            for (int i = 0; i < lovProcesos.size(); i++) {
                if (lovProcesos.get(i).getCodigo() == num) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                procesoBaseClonado = lovProcesos.get(indiceUnicoElemento);
                lovProcesos.clear();
                getLovProcesos();
            } else {
                procesoBaseClonado.setCodigo(auxClonadoCodigo);
                procesoBaseClonado.setDescripcion(auxDescripcionProceso);
                RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
                RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
                RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
            }
        }
        if (tipoAutoCompletar == 1) {
            for (int i = 0; i < lovProcesos.size(); i++) {
                if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                procesoBaseClonado = lovProcesos.get(indiceUnicoElemento);
                lovProcesos.clear();
                getLovProcesos();
            } else {
                procesoBaseClonado.setCodigo(auxClonadoCodigo);
                procesoBaseClonado.setDescripcion(auxDescripcionProceso);
                RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
                RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
                RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
            }
        }
    }

    public void dispararDialogoClonarProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
        RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
    }

    public void seleccionarProcesoClonado() {
        procesoBaseClonado = procesoSeleccionado;
        lovProcesos.clear();
        getLovProcesos();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
        RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
        procesoSeleccionado = new Procesos();
        filtrarLovProcesos = null;/*
         RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
         RequestContext.getCurrentInstance().update("form:lovProceso");
         RequestContext.getCurrentInstance().update("form:aceptarP");*/

        context.reset("form:lovProceso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
        RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
    }

    public void cancelarProcesoClonado() {
        RequestContext context = RequestContext.getCurrentInstance();
        procesoSeleccionado = new Procesos();
        filtrarLovProcesos = null;
        context.reset("form:lovProceso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
        RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
    }

    public boolean validarNuevoProcesoClon() {
        boolean retorno = true;
        int conteo = 0;
        for (int i = 0; i < lovProcesos.size(); i++) {
            if (lovProcesos.get(i).getCodigo() == procesoNuevoClonado.getCodigo()) {
                conteo++;
            }
        }
        if (conteo > 0) {
            retorno = false;
        }
        return retorno;
    }

    public void clonarProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!procesoNuevoClonado.getDescripcion().isEmpty() && procesoNuevoClonado.getCodigo() >= 1 && procesoBaseClonado.getSecuencia() != null) {
            if (validarNuevoProcesoClon() == true) {
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorCodigoClon').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorDatosClonado').show()");
        }
    }

    //GETTERS AND SETTERS
    public List<Procesos> getListaProcesos() {
        try {
            if (listaProcesos == null) {
                listaProcesos = new ArrayList<Procesos>();
                listaProcesos = administrarProcesos.listaProcesos();
                return listaProcesos;
            }
            return listaProcesos;
        } catch (Exception e) {
            System.out.println("Error...!! getListaProcesos " + e.toString());
            return null;
        }
    }

    public void setListaProcesos(List<Procesos> setListaProcesos) {
        this.listaProcesos = setListaProcesos;
    }

    public List<Procesos> getFiltrarListaProcesos() {
        return filtrarListaProcesos;
    }

    public void setFiltrarListaProcesos(List<Procesos> setFiltrarListaProcesos) {
        this.filtrarListaProcesos = setFiltrarListaProcesos;
    }

    public Procesos getNuevoProceso() {
        return nuevoProceso;
    }

    public void setNuevoProceso(Procesos setNuevoProceso) {
        this.nuevoProceso = setNuevoProceso;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public Procesos getEditarProceso() {
        return editarProceso;
    }

    public void setEditarProceso(Procesos setEditarProceso) {
        this.editarProceso = setEditarProceso;
    }

    public Procesos getDuplicarProceso() {
        return duplicarProceso;
    }

    public void setDuplicarProceso(Procesos setDuplicarProceso) {
        this.duplicarProceso = setDuplicarProceso;
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

    public List<FormulasProcesos> getListaFormulasProcesos() {
        if (listaFormulasProcesos == null) {
            listaFormulasProcesos = new ArrayList<FormulasProcesos>();
            if (index >= 0) {
                if (tipoLista == 0) {
                    listaFormulasProcesos = administrarProcesos.listaFormulasProcesosParaProcesoSecuencia(listaProcesos.get(index).getSecuencia());
                } else {
                    listaFormulasProcesos = administrarProcesos.listaFormulasProcesosParaProcesoSecuencia(filtrarListaProcesos.get(index).getSecuencia());
                }
            }
        }
        return listaFormulasProcesos;
    }

    public void setListaFormulasProcesos(List<FormulasProcesos> setListaFormulasProcesos) {
        this.listaFormulasProcesos = setListaFormulasProcesos;
    }

    public List<FormulasProcesos> getFiltrarListaFormulasProcesos() {
        return filtrarListaFormulasProcesos;
    }

    public void setFiltrarListaFormulasProcesos(List<FormulasProcesos> setFiltrarListaFormulasProcesos) {
        this.filtrarListaFormulasProcesos = setFiltrarListaFormulasProcesos;
    }

    public List<Procesos> getListProcesosModificar() {
        return listProcesosModificar;
    }

    public void setProcesosModificar(List<Procesos> setProcesosModificar) {
        this.listProcesosModificar = setProcesosModificar;
    }

    public List<Procesos> getListProcesosCrear() {
        return listProcesosCrear;
    }

    public void setListProcesosCrear(List<Procesos> setListProcesosCrear) {
        this.listProcesosCrear = setListProcesosCrear;
    }

    public List<Procesos> getListProcesosBorrar() {
        return listProcesosBorrar;
    }

    public void setListProcesosBorrar(List<Procesos> setListProcesosBorrar) {
        this.listProcesosBorrar = setListProcesosBorrar;
    }

    public List<FormulasProcesos> getListFormulasProcesosModificar() {
        return listFormulasProcesosModificar;
    }

    public void setListFormulasProcesosModificar(List<FormulasProcesos> setListFormulasProcesosModificar) {
        this.listFormulasProcesosModificar = setListFormulasProcesosModificar;
    }

    public FormulasProcesos getNuevoFormulaProceso() {
        return nuevoFormulaProceso;
    }

    public void setNuevoFormulaProceso(FormulasProcesos setNuevoFormulaProceso) {
        this.nuevoFormulaProceso = setNuevoFormulaProceso;
    }

    public List<FormulasProcesos> getListFormulasProcesosCrear() {
        return listFormulasProcesosCrear;
    }

    public void setListFormulasProcesosCrear(List<FormulasProcesos> setListFormulasProcesosCrear) {
        this.listFormulasProcesosCrear = setListFormulasProcesosCrear;
    }

    public List<FormulasProcesos> getLisFormulasProcesosBorrar() {
        return listFormulasProcesosBorrar;
    }

    public void setListFormulasProcesosBorrar(List<FormulasProcesos> setListFormulasProcesosBorrar) {
        this.listFormulasProcesosBorrar = setListFormulasProcesosBorrar;
    }

    public FormulasProcesos getEditarFormulaProceso() {
        return editarFormulaProceso;
    }

    public void setEditarFormulaProceso(FormulasProcesos setEditarFormulaProceso) {
        this.editarFormulaProceso = setEditarFormulaProceso;
    }

    public FormulasProcesos getDuplicarFormulaProceso() {
        return duplicarFormulaProceso;
    }

    public void setDuplicarFormulaProceso(FormulasProcesos setDuplicarFormulaProceso) {
        this.duplicarFormulaProceso = setDuplicarFormulaProceso;
    }

    public BigInteger getSecRegistroFormulaProceso() {
        return secRegistroFormulaProceso;
    }

    public void setSecRegistroFormulaProceso(BigInteger setSecRegistroFormulaProceso) {
        this.secRegistroFormulaProceso = setSecRegistroFormulaProceso;
    }

    public BigInteger getBackUpSecRegistroFormulaProceso() {
        return backUpSecRegistroFormulaProceso;
    }

    public void setBackUpSecRegistroFormulaProceso(BigInteger setBackUpSecRegistroFormulaProceso) {
        this.backUpSecRegistroFormulaProceso = setBackUpSecRegistroFormulaProceso;
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

    public List<Tipospagos> getLovTiposPagos() {
        lovTiposPagos = administrarProcesos.lovTiposPagos();
        return lovTiposPagos;
    }

    public void setLovTiposPagos(List<Tipospagos> setLovTiposPagos) {
        this.lovTiposPagos = setLovTiposPagos;
    }

    public List<Tipospagos> getFiltrarLovTiposPagos() {
        return filtrarLovTiposPagos;
    }

    public void setFiltrarLovTiposPagos(List<Tipospagos> setFiltrarLovTiposPagos) {
        this.filtrarLovTiposPagos = setFiltrarLovTiposPagos;
    }

    public Tipospagos getTipoPagoSeleccionado() {
        return tipoPagoSeleccionado;
    }

    public void setTipoPagoSeleccionado(Tipospagos setTipoPagoSeleccionado) {
        this.tipoPagoSeleccionado = setTipoPagoSeleccionado;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public String getAltoTablaProcesos() {
        return altoTablaProcesos;
    }

    public void setAltoTablaProcesos(String setAltoTablaProcesos) {
        this.altoTablaProcesos = setAltoTablaProcesos;
    }

    public String getAltoTablaFormulasProcesos() {
        return altoTablaFormulasProcesos;
    }

    public void setAltoTablaFormulasProcesos(String setAltoTablaFormulasProcesos) {
        this.altoTablaFormulasProcesos = setAltoTablaFormulasProcesos;
    }

    public List<Formulas> getLovFormulas() {
        lovFormulas = administrarProcesos.lovFormulas();
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

    public List<OperandosLogs> getListaOperandosLogs() {
        if (listaOperandosLogs == null) {
            listaOperandosLogs = new ArrayList<OperandosLogs>();
            if (index >= 0) {
                if (tipoLista == 0) {
                    listaOperandosLogs = administrarProcesos.listaOperandosLogsParaProcesoSecuencia(listaProcesos.get(index).getSecuencia());
                } else {
                    listaOperandosLogs = administrarProcesos.listaOperandosLogsParaProcesoSecuencia(filtrarListaProcesos.get(index).getSecuencia());
                }
            }

        }
        return listaOperandosLogs;
    }

    public void setListaOperandosLogs(List<OperandosLogs> setListaOperandosLogs) {
        this.listaOperandosLogs = setListaOperandosLogs;
    }

    public List<OperandosLogs> getFiltrarListaOperandosLogs() {
        return filtrarListaOperandosLogs;
    }

    public void setFiltrarListaOperandosLogs(List<OperandosLogs> setFiltrarListaOperandosLogs) {
        this.filtrarListaOperandosLogs = setFiltrarListaOperandosLogs;
    }

    public List<OperandosLogs> getListOperandosLogsModificar() {
        return listOperandosLogsModificar;
    }

    public void setListOperandosLogsModificar(List<OperandosLogs> setListOperandosLogsModificar) {
        this.listOperandosLogsModificar = setListOperandosLogsModificar;
    }

    public OperandosLogs getNuevoOperandoLog() {
        return nuevoOperandoLog;
    }

    public void setNuevoOperandoLog(OperandosLogs setNuevoOperandoLog) {
        this.nuevoOperandoLog = setNuevoOperandoLog;
    }

    public List<OperandosLogs> getListOperandosLogsCrear() {
        return listOperandosLogsCrear;
    }

    public void setListOperandosLogsCrear(List<OperandosLogs> setListOperandosLogsCrear) {
        this.listOperandosLogsCrear = setListOperandosLogsCrear;
    }

    public List<OperandosLogs> getListOperandosLogsBorrar() {
        return listOperandosLogsBorrar;
    }

    public void setListOperandosLogsBorrar(List<OperandosLogs> setListOperandosLogsBorrar) {
        this.listOperandosLogsBorrar = setListOperandosLogsBorrar;
    }

    public OperandosLogs getEditarOperandoLog() {
        return editarOperandoLog;
    }

    public void setEditarOperandoLog(OperandosLogs setEditarOperandoLog) {
        this.editarOperandoLog = setEditarOperandoLog;
    }

    public OperandosLogs getDuplicarOperandoLog() {
        return duplicarOperandoLog;
    }

    public void setDuplicarOperandoLog(OperandosLogs setDuplicarOperandoLog) {
        this.duplicarOperandoLog = setDuplicarOperandoLog;
    }

    public BigInteger getSecRegistroOperandoLog() {
        return secRegistroOperandoLog;
    }

    public void setSecRegistroOperandoLog(BigInteger setSecRegistroOperandoLog) {
        this.secRegistroOperandoLog = setSecRegistroOperandoLog;
    }

    public BigInteger getBackUpSecRegistroOperandoLog() {
        return backUpSecRegistroOperandoLog;
    }

    public void setBackUpSecRegistroOperandoLog(BigInteger setBackUpSecRegistroOperandoLog) {
        this.backUpSecRegistroOperandoLog = setBackUpSecRegistroOperandoLog;
    }

    public List<Operandos> getLovOperandos() {
        lovOperandos = administrarProcesos.lovOperandos();
        return lovOperandos;
    }

    public void setLovOperandos(List<Operandos> setLovOperandos) {
        this.lovOperandos = setLovOperandos;
    }

    public List<Operandos> getFiltrarLovOperandos() {
        return filtrarLovOperandos;
    }

    public void setFiltrarLovOperandos(List<Operandos> setFiltrarLovOperandos) {
        this.filtrarLovOperandos = setFiltrarLovOperandos;
    }

    public Operandos getOperandoSeleccionado() {
        return operandoSeleccionado;
    }

    public void setOperandoSeleccionado(Operandos setOperandoSeleccionado) {
        this.operandoSeleccionado = setOperandoSeleccionado;
    }

    public String getAltoTablaOperandosLogs() {
        return altoTablaOperandosLogs;
    }

    public void setAltoTablaOperandosLogs(String altoTablaTSGrupos) {
        this.altoTablaOperandosLogs = altoTablaTSGrupos;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public Procesos getProcesoNuevoClonado() {
        return procesoNuevoClonado;
    }

    public void setProcesoNuevoClonado(Procesos procesoNuevoClonado) {
        this.procesoNuevoClonado = procesoNuevoClonado;
    }

    public Procesos getProcesoBaseClonado() {
        return procesoBaseClonado;
    }

    public void setProcesoBaseClonado(Procesos procesoBaseClonado) {
        this.procesoBaseClonado = procesoBaseClonado;
    }

    public List<Procesos> getLovProcesos() {
        lovProcesos = administrarProcesos.listaProcesos();
        return lovProcesos;
    }

    public void setLovProcesos(List<Procesos> lovProcesos) {
        this.lovProcesos = lovProcesos;
    }

    public List<Procesos> getFiltrarLovProcesos() {
        return filtrarLovProcesos;
    }

    public void setFiltrarLovProcesos(List<Procesos> filtrarLovProcesos) {
        this.filtrarLovProcesos = filtrarLovProcesos;
    }

    public Procesos getProcesoSeleccionado() {
        return procesoSeleccionado;
    }

    public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
        this.procesoSeleccionado = procesoSeleccionado;
    }

    public Procesos getProcesoTablaSeleccionada() {
        getListaProcesos();
        if (listaProcesos != null) {
            int tam = listaProcesos.size();
            if (tam > 0) {
                procesoTablaSeleccionada = listaProcesos.get(0);
            }
        }
        return procesoTablaSeleccionada;
    }

    public void setProcesoTablaSeleccionada(Procesos procesoTablaSeleccionada) {
        this.procesoTablaSeleccionada = procesoTablaSeleccionada;
    }

    public FormulasProcesos getFormulaProcesoTablaSeleccionada() {
        getListaFormulasProcesos();
        if (listaFormulasProcesos != null) {
            int tam = listaFormulasProcesos.size();
            if (tam > 0) {
                formulaProcesoTablaSeleccionada = listaFormulasProcesos.get(0);
            }
        }
        return formulaProcesoTablaSeleccionada;
    }

    public void setFormulaProcesoTablaSeleccionada(FormulasProcesos formulaProcesoTablaSeleccionada) {
        this.formulaProcesoTablaSeleccionada = formulaProcesoTablaSeleccionada;
    }

    public OperandosLogs getOperandoTablaSeleccionada() {
        getListaOperandosLogs();
        if (listaOperandosLogs != null) {
            int tam = listaOperandosLogs.size();
            if (tam > 0) {
                operandoTablaSeleccionada = listaOperandosLogs.get(0);
            }
        }
        return operandoTablaSeleccionada;
    }

    public void setOperandoTablaSeleccionada(OperandosLogs operandoTablaSeleccionada) {
        this.operandoTablaSeleccionada = operandoTablaSeleccionada;
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

    public String getInfoRegistroTipoPago() {
        getLovTiposPagos();
        if (lovTiposPagos != null) {
            infoRegistroTipoPago = "Cantidad de registros : " + lovTiposPagos.size();
        } else {
            infoRegistroTipoPago = "Cantidad de registros : 0";
        }
        return infoRegistroTipoPago;
    }

    public void setInfoRegistroTipoPago(String infoRegistroTipoPago) {
        this.infoRegistroTipoPago = infoRegistroTipoPago;
    }

    public String getInfoRegistroProceso() {
        getLovProcesos();
        if (lovProcesos != null) {
            infoRegistroProceso = "Cantidad de registros : " + lovProcesos.size();
        } else {
            infoRegistroProceso = "Cantidad de registros : 0";
        }
        return infoRegistroProceso;
    }

    public void setInfoRegistroProceso(String infoRegistroProceso) {
        this.infoRegistroProceso = infoRegistroProceso;
    }

    public String getInfoRegistroOperando() {
        getLovOperandos();
        if (lovOperandos != null) {
            infoRegistroOperando = "Cantidad de registros : " + lovOperandos.size();
        } else {
            infoRegistroOperando = "Cantidad de registros : 0";
        }
        return infoRegistroOperando;
    }

    public void setInfoRegistroOperando(String infoRegistroOperando) {
        this.infoRegistroOperando = infoRegistroOperando;
    }

    public String getAltoTablaProceso() {
        return altoTablaProceso;
    }

    public void setAltoTablaProceso(String altoTablaProceso) {
        this.altoTablaProceso = altoTablaProceso;
    }

}
