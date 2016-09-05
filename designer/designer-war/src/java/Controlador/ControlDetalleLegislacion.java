package Controlador;

import Entidades.Contratos;
import Entidades.Formulas;
import Entidades.Formulascontratos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarDetalleLegislacionInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlDetalleLegislacion implements Serializable {

    @EJB
    AdministrarDetalleLegislacionInterface administrarDetalleLegislacion;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //
    private List<Formulascontratos> listFormulasContratosDetalle;
    private List<Formulascontratos> filtrarListFormulasContratosDetalle;
    private List<Formulascontratos> listFormulasContratos;
    private List<Formulascontratos> filtrarListFormulasContratos;
    private Contratos contratoActual;
    private Formulascontratos formulaContratoActual;
    private Formulascontratos formulaContratosSeleccionado;
    //
    private List<Terceros> listTerceros;
    private List<Terceros> filtrarListTerceros;
    private Terceros terceroSeleccionado;
    //
    private List<Periodicidades> listPeriodicidades;
    private List<Periodicidades> filtrarListPeriodicidades;
    private Periodicidades periodicidadSeleccionada;
    //
    private List<Formulas> listFormulas;
    private List<Formulas> filtrarListFormulas;
    private Formulas formulaSeleccionada;
    //
    private int tipoActualizacion;
    private int bandera;
    private Contratos backUpContratoActual;
    //Columnas Tabla VL
    private Column formulaFechaInicial, formulaFechaFinal, formulaNombreLargo, formulaNombreCorto, formulaEstado, formulaPeriodicidad, formulaNit, formulaTercero, formulaObservacion, formulaConcepto;
    //Otros
    private boolean aceptar;
    private int index;
    private List<Formulascontratos> listFormulasContratosModificar;
    private boolean guardado, guardarOk;
    public Formulascontratos nuevoFormulaContrato;
    private List<Formulascontratos> listFormulasContratosCrear;
    private BigInteger l;
    private int k;
    private List<Formulascontratos> listFormulasContratosBorrar;
    private Formulascontratos editarFormulaContrato;
    private int cualCelda, tipoLista;
    private boolean cambioEditor, aceptarEditar;
    private Formulascontratos duplicarFormulaContrato;
    private boolean permitirIndex;
    //Variables Autompletar
    //private String motivoCambioSueldo, tiposEntidades, tiposSueldos, terceros;
    //Indices VigenciaProrrateo / VigenciaProrrateoProyecto
    private String nombreXML;
    private String nombreTabla;
    private boolean cambiosFormulaContrato;
    private BigInteger secRegistroFormulaContrato;
    private BigInteger backUpSecRegistroFormulaCuenta;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    private int indexAux;
    private String nombreLargo, nombreCorto, estado, periodicidad, nit, tercero, concepto, observacionFormula;
    private Date fechaIni;
    private Date fechaParametro;

    public ControlDetalleLegislacion() {
        listFormulasContratos = null;
        fechaParametro = new Date(1, 1, 0);
        formulaContratoActual = new Formulascontratos();
        indexAux = 0;
        listTerceros = null;
        listPeriodicidades = null;
        backUpContratoActual = new Contratos();
        nombreTablaRastro = "";
        backUp = null;
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        secRegistroFormulaContrato = null;
        backUpSecRegistroFormulaCuenta = null;
        listFormulasContratosDetalle = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listFormulasContratosBorrar = new ArrayList<Formulascontratos>();
        //crear aficiones
        listFormulasContratosCrear = new ArrayList<Formulascontratos>();
        k = 0;
        //modificar aficiones
        listFormulasContratosModificar = new ArrayList<Formulascontratos>();
        //editar
        editarFormulaContrato = new Formulascontratos();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevoFormulaContrato = new Formulascontratos();
        nuevoFormulaContrato.setTercero(new Terceros());
        nuevoFormulaContrato.setPeriodicidad(new Periodicidades());
        nuevoFormulaContrato.setFormula(new Formulas());
        index = -1;
        bandera = 0;
        permitirIndex = true;
        nombreTabla = ":formExportarFormula:datosFormulaContratosExportar";
        nombreXML = "CuentasXML";
        duplicarFormulaContrato = new Formulascontratos();
        cambiosFormulaContrato = false;

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarDetalleLegislacion.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void obtenerContrato(BigInteger secuencia) {
        index = -1;
        contratoActual = new Contratos();
        listFormulasContratosDetalle = null;
        contratoActual = administrarDetalleLegislacion.consultarContrato(secuencia);
//        System.out.println("contratoActual : " + contratoActual.getSecuencia());
        getListFormulasContratosDetalle();
        listFormulasContratos = null;
    }

    public boolean validarRegistrosNuevos(int i) {
        boolean valor = false;
        if (i == 1) {
            if (nuevoFormulaContrato.getFechainicial() != null
                    && nuevoFormulaContrato.getFormula().getSecuencia() != null
                    && nuevoFormulaContrato.getPeriodicidad().getSecuencia() != null) {
                valor = true;
            }
        }
        if (i == 2) {
            if (duplicarFormulaContrato.getFechainicial() != null
                    && duplicarFormulaContrato.getFormula().getSecuencia() != null
                    && duplicarFormulaContrato.getPeriodicidad().getSecuencia() != null) {
                valor = true;
            }
        }
        return valor;
    }

    public boolean validarFechasRegistroHistoriaFormula(int i) {
        boolean retorno = false;
        if (i == 0) {
            Formulascontratos auxiliar = listFormulasContratosDetalle.get(i);
            if (auxiliar.getFechafinal() != null) {
                if (auxiliar.getFechainicial().after(fechaParametro) && (auxiliar.getFechainicial().before(auxiliar.getFechafinal()))) {
                    retorno = true;
                }
            }
            if (auxiliar.getFechafinal() == null) {
                if (auxiliar.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                }
            }
        }
        if (i == 1) {
            if (nuevoFormulaContrato.getFechafinal() != null) {
                if (nuevoFormulaContrato.getFechainicial().after(fechaParametro) && (nuevoFormulaContrato.getFechainicial().before(nuevoFormulaContrato.getFechafinal()))) {
                    retorno = true;
                }
            }
            if (nuevoFormulaContrato.getFechafinal() == null) {
                if (nuevoFormulaContrato.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                }
            }
        }
        if (i == 2) {
            if (duplicarFormulaContrato.getFechafinal() != null) {
                if (duplicarFormulaContrato.getFechainicial().after(fechaParametro) && (duplicarFormulaContrato.getFechainicial().before(duplicarFormulaContrato.getFechafinal()))) {
                    retorno = true;
                }
            }
            if (duplicarFormulaContrato.getFechafinal() == null) {
                if (duplicarFormulaContrato.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                }
            }
        }
        return retorno;
    }

    public void modificacionesFechaHistoriaFormula(int i, int c) {
        Formulascontratos auxiliar = null;
        if (tipoLista == 0) {
            auxiliar = listFormulasContratosDetalle.get(i);
        }
        if (tipoLista == 1) {
            auxiliar = filtrarListFormulasContratosDetalle.get(i);
        }
        if ((auxiliar.getFechainicial() != null)) {
            boolean validacion = validarFechasRegistroHistoriaFormula(0);
            if (validacion == true) {
                cambiarIndice(i, c);
                modificarFormulaContrato(i);
                indexAux = i;
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:datosFormulaContrato");
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                if (tipoLista == 0) {
                    listFormulasContratosDetalle.get(index).setFechainicial(fechaIni);
                }
                if (tipoLista == 1) {
                    filtrarListFormulasContratosDetalle.get(index).setFechainicial(fechaIni);
                }
                context.update("form:datosFormulaContrato");
                context.execute("errorFechasHF.show()");
                index = -1;
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            if (tipoLista == 0) {
                listFormulasContratosDetalle.get(index).setFechainicial(fechaIni);
            }
            if (tipoLista == 1) {
                filtrarListFormulasContratosDetalle.get(index).setFechainicial(fechaIni);
            }
            context.update("form:datosFormulaContrato");
            context.execute("errorRegNuevo.show()");
            index = -1;
        }
    }

    /**
     * Modifica los elementos de la tabla VigenciaLocalizacion que no usan
     * autocomplete
     *
     * @param indice Fila donde se efectu el cambio
     */
    public void modificarFormulaContrato(int indice) {
        if (tipoLista == 0) {
            listFormulasContratosDetalle.get(indice).getFormula().setObservaciones(observacionFormula);
            listFormulasContratosDetalle.get(indice).setStrConcepto(concepto);
            if (!listFormulasContratosCrear.contains(listFormulasContratosDetalle.get(indice))) {
                if (listFormulasContratosModificar.isEmpty()) {
                    listFormulasContratosModificar.add(listFormulasContratosDetalle.get(indice));
                } else if (!listFormulasContratosModificar.contains(listFormulasContratosDetalle.get(indice))) {
                    listFormulasContratosModificar.add(listFormulasContratosDetalle.get(indice));
                }
                if (guardado == true) {
                    guardado = false;
                }
            }
            index = -1;
            secRegistroFormulaContrato = null;
        } else {
            filtrarListFormulasContratosDetalle.get(indice).getFormula().setObservaciones(observacionFormula);
            filtrarListFormulasContratosDetalle.get(indice).setStrConcepto(concepto);
            if (!listFormulasContratosCrear.contains(filtrarListFormulasContratosDetalle.get(indice))) {
                if (listFormulasContratosModificar.isEmpty()) {
                    listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(indice));
                } else if (!listFormulasContratosModificar.contains(filtrarListFormulasContratosDetalle.get(indice))) {
                    listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(indice));
                }
                if (guardado == true) {
                    guardado = false;
                }
            }
            index = -1;
            secRegistroFormulaContrato = null;
        }
        cambiosFormulaContrato = true;
    }

    public void modificarFormulaContratoAutocompletar(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("NOMBRELARGO")) {
            if (tipoLista == 0) {
                listFormulasContratosDetalle.get(indice).getFormula().setNombrelargo(nombreLargo);
            } else {
                filtrarListFormulasContratosDetalle.get(indice).getFormula().setNombrelargo(nombreLargo);
            }
            for (int i = 0; i < listFormulas.size(); i++) {
                if (listFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listFormulasContratosDetalle.get(indice).setFormula(listFormulas.get(indiceUnicoElemento));
                } else {
                    filtrarListFormulasContratosDetalle.get(indice).setFormula(listFormulas.get(indiceUnicoElemento));
                }
                listFormulas.clear();
                getListFormulas();
            } else {
                permitirIndex = false;
                context.update("form:FormulaDialogo");
                context.execute("FormulaDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("NOMBRECORTO")) {
            if (tipoLista == 0) {
                listFormulasContratosDetalle.get(indice).getFormula().setNombrecorto(nombreCorto);
            } else {
                filtrarListFormulasContratosDetalle.get(indice).getFormula().setNombrecorto(nombreCorto);
            }
            for (int i = 0; i < listFormulas.size(); i++) {
                if (listFormulas.get(i).getNombrecorto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listFormulasContratosDetalle.get(indice).setFormula(listFormulas.get(indiceUnicoElemento));
                } else {
                    filtrarListFormulasContratosDetalle.get(indice).setFormula(listFormulas.get(indiceUnicoElemento));
                }
                listFormulas.clear();
                getListFormulas();
            } else {
                permitirIndex = false;
                context.update("form:FormulaDialogo");
                context.execute("FormulaDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("ESTADO")) {
            if (tipoLista == 0) {
                listFormulasContratosDetalle.get(indice).getFormula().setEstado(estado);
            } else {
                filtrarListFormulasContratosDetalle.get(indice).getFormula().setEstado(estado);
            }
            for (int i = 0; i < listFormulas.size(); i++) {
                if (listFormulas.get(i).getEstado().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listFormulasContratosDetalle.get(indice).setFormula(listFormulas.get(indiceUnicoElemento));
                } else {
                    filtrarListFormulasContratosDetalle.get(indice).setFormula(listFormulas.get(indiceUnicoElemento));
                }
                listTerceros.clear();
                getListFormulas();
            } else {
                permitirIndex = false;
                context.update("form:FormulaDialogo");
                context.execute("FormulaDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("PERIODICIDAD")) {
            if (tipoLista == 0) {
                listFormulasContratosDetalle.get(indice).getPeriodicidad().setNombre(periodicidad);
            } else {
                filtrarListFormulasContratosDetalle.get(indice).getPeriodicidad().setNombre(periodicidad);
            }
            for (int i = 0; i < listPeriodicidades.size(); i++) {
                if (listPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listFormulasContratosDetalle.get(indice).setPeriodicidad(listPeriodicidades.get(indiceUnicoElemento));
                } else {
                    filtrarListFormulasContratosDetalle.get(indice).setPeriodicidad(listPeriodicidades.get(indiceUnicoElemento));
                }
                listPeriodicidades.clear();
                getListPeriodicidades();
            } else {
                permitirIndex = false;
                context.update("form:PeriodicidadDialogo");
                context.execute("PeriodicidadDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("NIT")) {
            if (tipoLista == 0) {
                listFormulasContratosDetalle.get(indice).getTercero().setStrNit(nit);
            } else {
                filtrarListFormulasContratosDetalle.get(indice).getTercero().setStrNit(nit);
            }
            for (int i = 0; i < listTerceros.size(); i++) {
                if (listTerceros.get(i).getStrNit().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listFormulasContratosDetalle.get(indice).setTercero(listTerceros.get(indiceUnicoElemento));
                } else {
                    filtrarListFormulasContratosDetalle.get(indice).setTercero(listTerceros.get(indiceUnicoElemento));
                }
                listTerceros.clear();
                getListTerceros();
            } else {
                permitirIndex = false;
                context.update("form:TerceroDialogo");
                context.execute("TerceroDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (tipoLista == 0) {
                listFormulasContratosDetalle.get(indice).getTercero().setNombre(tercero);
            } else {
                filtrarListFormulasContratosDetalle.get(indice).getTercero().setNombre(tercero);
            }
            for (int i = 0; i < listTerceros.size(); i++) {
                if (listTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listFormulasContratosDetalle.get(indice).setTercero(listTerceros.get(indiceUnicoElemento));
                } else {
                    filtrarListFormulasContratosDetalle.get(indice).setTercero(listTerceros.get(indiceUnicoElemento));
                }
                listTerceros.clear();
                getListTerceros();
            } else {
                permitirIndex = false;
                context.update("form:TerceroDialogo");
                context.execute("TerceroDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listFormulasContratosCrear.contains(listFormulasContratosDetalle.get(indice))) {
                    if (listFormulasContratosModificar.isEmpty()) {
                        listFormulasContratosModificar.add(listFormulasContratosDetalle.get(indice));
                    } else if (!listFormulasContratosModificar.contains(listFormulasContratosDetalle.get(indice))) {
                        listFormulasContratosModificar.add(listFormulasContratosDetalle.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
                index = -1;
                secRegistroFormulaContrato = null;
            } else {
                if (!listFormulasContratosCrear.contains(filtrarListFormulasContratosDetalle.get(indice))) {
                    if (listFormulasContratosModificar.isEmpty()) {
                        listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(indice));
                    } else if (!listFormulasContratosModificar.contains(filtrarListFormulasContratosDetalle.get(indice))) {
                        listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
                index = -1;
                secRegistroFormulaContrato = null;
            }
            cambiosFormulaContrato = true;
        }
        context.update("form:datosFormulaContrato");
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * Modifica los elementos de la tabla VigenciaProrrateo que no usan
     * autocomplete
     *
     * @param indice Fila donde se efectu el cambio
     */
    //Ubicacion Celda.
    /**
     * Metodo que obtiene la posicion dentro de la tabla VigenciasLocalizaciones
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    public void cambiarIndice(int indice, int celda) {

        cualCelda = celda;
        index = indice;
        indexAux = indice;
        if (tipoLista == 0) {
            secRegistroFormulaContrato = listFormulasContratosDetalle.get(index).getSecuencia();
            formulaContratoActual = listFormulasContratosDetalle.get(index);
            fechaIni = listFormulasContratosDetalle.get(index).getFechainicial();
            if (cualCelda == 2) {
                nombreLargo = listFormulasContratosDetalle.get(index).getFormula().getNombrelargo();
            }
            if (cualCelda == 3) {
                nombreCorto = listFormulasContratosDetalle.get(index).getFormula().getNombrecorto();
            }
            if (cualCelda == 4) {
                estado = listFormulasContratosDetalle.get(index).getFormula().getEstado();
            }
            if (cualCelda == 5) {
                periodicidad = listFormulasContratosDetalle.get(index).getPeriodicidad().getNombre();
            }
            if (cualCelda == 6) {
                nit = listFormulasContratosDetalle.get(index).getTercero().getStrNit();
            }
            if (cualCelda == 7) {
                tercero = listFormulasContratosDetalle.get(index).getTercero().getNombre();
            }
            if (cualCelda == 8) {
                observacionFormula = listFormulasContratosDetalle.get(index).getFormula().getObservaciones();
            }
            if (cualCelda == 9) {
                concepto = listFormulasContratosDetalle.get(index).getStrConcepto();
            }
        }
        if (tipoLista == 1) {
            secRegistroFormulaContrato = filtrarListFormulasContratosDetalle.get(index).getSecuencia();
            formulaContratoActual = filtrarListFormulasContratosDetalle.get(index);
            fechaIni = filtrarListFormulasContratosDetalle.get(index).getFechainicial();
            if (cualCelda == 2) {
                nombreLargo = filtrarListFormulasContratosDetalle.get(index).getFormula().getNombrelargo();
            }
            if (cualCelda == 3) {
                nombreCorto = filtrarListFormulasContratosDetalle.get(index).getFormula().getNombrecorto();
            }
            if (cualCelda == 4) {
                estado = filtrarListFormulasContratosDetalle.get(index).getFormula().getEstado();
            }
            if (cualCelda == 5) {
                periodicidad = filtrarListFormulasContratosDetalle.get(index).getPeriodicidad().getNombre();
            }
            if (cualCelda == 6) {
                nit = filtrarListFormulasContratosDetalle.get(index).getTercero().getStrNit();
            }
            if (cualCelda == 7) {
                tercero = filtrarListFormulasContratosDetalle.get(index).getTercero().getNombre();
            }
            if (cualCelda == 8) {
                observacionFormula = filtrarListFormulasContratosDetalle.get(index).getFormula().getObservaciones();
            }
            if (cualCelda == 9) {
                concepto = filtrarListFormulasContratosDetalle.get(index).getStrConcepto();
            }
        }
    }

    //GUARDAR
    /**
     * Metodo de guardado general para la pagina
     */
    public void guardayYSalir() {
        guardadoGeneral();
        salir();
    }

    public void guardadoGeneral() {
        if (cambiosFormulaContrato == true) {
            guardarCambiosFormulaContrato();
        }
        guardado = true;
        RequestContext.getCurrentInstance().update("form:aceptar");
    }

    /**
     * Metodo que guarda los cambios efectuados en la pagina
     * VigenciasLocalizaciones
     */
    public void guardarCambiosFormulaContrato() {
        if (guardado == false) {
            if (!listFormulasContratosBorrar.isEmpty()) {
                administrarDetalleLegislacion.borrarFormulasContratos(listFormulasContratosBorrar);
                listFormulasContratosBorrar.clear();
            }
            if (!listFormulasContratosCrear.isEmpty()) {
                administrarDetalleLegislacion.crearFormulasContratos(listFormulasContratosCrear);
                listFormulasContratosCrear.clear();
            }
            if (!listFormulasContratosModificar.isEmpty()) {
                administrarDetalleLegislacion.modificarFormulasContratos(listFormulasContratosModificar);
                listFormulasContratosModificar.clear();
            }
            listFormulasContratosDetalle = null;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosFormulaContrato");
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            context.update("form:growl");
            k = 0;
        }
        index = -1;
        secRegistroFormulaContrato = null;
        cambiosFormulaContrato = false;
    }
    //CANCELAR MODIFICACIONES

    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacion() {
        if (bandera == 1) {
            formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
            formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
            formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            formulaNombreLargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreLargo");
            formulaNombreLargo.setFilterStyle("display: none; visibility: hidden;");
            formulaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreCorto");
            formulaNombreCorto.setFilterStyle("display: none; visibility: hidden;");
            formulaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaEstado");
            formulaEstado.setFilterStyle("display: none; visibility: hidden;");
            formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
            formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
            formulaNit = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNit");
            formulaNit.setFilterStyle("display: none; visibility: hidden;");
            formulaTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
            formulaTercero.setFilterStyle("display: none; visibility: hidden;");
            formulaObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaObservacion");
            formulaObservacion.setFilterStyle("display: none; visibility: hidden;");
            formulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaConcepto");
            formulaConcepto.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
            bandera = 0;
            filtrarListFormulasContratosDetalle = null;
            tipoLista = 0;
        }
        listFormulasContratosBorrar.clear();
        listFormulasContratosCrear.clear();
        listFormulasContratosModificar.clear();
        index = -1;
        indexAux = -1;
        secRegistroFormulaContrato = null;
        k = 0;
        listFormulasContratosDetalle = null;
        listPeriodicidades = null;
        listTerceros = null;
        listFormulas = null;
        guardado = true;
        aceptar = true;
        cambiosFormulaContrato = false;
        listFormulasContratos = null;
        getListFormulasContratos();
        getListFormulas();
        getListFormulasContratosModificar();
        getListPeriodicidades();
        getListTerceros();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosFormulaContrato");
    }

    //MOSTRAR DATOS CELDA
    /**
     * Metodo que muestra los dialogos de editar con respecto a la lista real o
     * la lista filtrada y a la columna
     */
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarFormulaContrato = listFormulasContratosDetalle.get(index);
            }
            if (tipoLista == 1) {
                editarFormulaContrato = filtrarListFormulasContratosDetalle.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                context.update("formularioDialogos:editarFechaIniFormula");
                context.execute("editarFechaIniFormula.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editarFechaFinFormula");
                context.execute("editarFechaFinFormula.show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                context.update("formularioDialogos:editarNombreLargoFormula");
                context.execute("editarNombreLargoFormula.show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                context.update("formularioDialogos:editarNombreCortoFormula");
                context.execute("editarNombreCortoFormula.show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                context.update("formularioDialogos:editarEstadoFormula");
                context.execute("editarEstadoFormula.show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                context.update("formularioDialogos:editarPeriodicidadFormula");
                context.execute("editarPeriodicidadFormula.show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                context.update("formularioDialogos:editarNitFormula");
                context.execute("editarNitFormula.show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                context.update("formularioDialogos:editarTerceroFormula");
                context.execute("editarTerceroFormula.show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                context.update("formularioDialogos:editarObservacionFormula");
                context.execute("editarObservacionFormula.show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                context.update("formularioDialogos:editarConceptosFormula");
                context.execute("editarConceptosFormula.show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistroFormulaContrato = null;
    }

    public void validarIngresoNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:NuevoRegistroFormula");
        context.execute("NuevoRegistroFormula.show()");

    }

    public void validarDuplicadoRegistro() {
        if (index >= 0) {
            duplicarFormula();
        }
    }

    public void validarBorradoRegistro() {
        if (index >= 0) {
            borrarFormula();
        }
    }

    //CREAR VL
    /**
     * Metodo que se encarga de agregar un nueva VigenciasLocalizaciones
     */
    public void agregarNuevoFormula() {
        if (validarRegistrosNuevos(1) == true) {
            boolean validacion = validarFechasRegistroHistoriaFormula(1);
            if (validacion == true) {
                if (bandera == 1) {
                    formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
                    formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
                    formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    formulaNombreLargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreLargo");
                    formulaNombreLargo.setFilterStyle("display: none; visibility: hidden;");
                    formulaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreCorto");
                    formulaNombreCorto.setFilterStyle("display: none; visibility: hidden;");
                    formulaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaEstado");
                    formulaEstado.setFilterStyle("display: none; visibility: hidden;");
                    formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
                    formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
                    formulaNit = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNit");
                    formulaNit.setFilterStyle("display: none; visibility: hidden;");
                    formulaTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
                    formulaTercero.setFilterStyle("display: none; visibility: hidden;");
                    formulaObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaObservacion");
                    formulaObservacion.setFilterStyle("display: none; visibility: hidden;");
                    formulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaConcepto");
                    formulaConcepto.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
                    bandera = 0;
                    filtrarListFormulasContratosDetalle = null;
                    tipoLista = 0;
                }
                //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
                k++;
                if (!nuevoFormulaContrato.getFormula().getObservaciones().isEmpty()) {
                    String aux = nuevoFormulaContrato.getFormula().getObservaciones().toUpperCase();
                    nuevoFormulaContrato.getFormula().setObservaciones(aux);
                }
                BigInteger var = BigInteger.valueOf(k);
                nuevoFormulaContrato.setSecuencia(var);
                nuevoFormulaContrato.setContrato(contratoActual);
                nuevoFormulaContrato.setStrConcepto(" ");
                listFormulasContratosCrear.add(nuevoFormulaContrato);
                listFormulasContratosDetalle.add(nuevoFormulaContrato);
                ////------////
                nuevoFormulaContrato = new Formulascontratos();
                nuevoFormulaContrato.setTercero(new Terceros());
                nuevoFormulaContrato.setPeriodicidad(new Periodicidades());
                nuevoFormulaContrato.setFormula(new Formulas());
                ////-----////
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:datosFormulaContrato");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:aceptar");
                }
                context.execute("NuevoRegistroFormula.hide()");
                cambiosFormulaContrato = true;
                index = -1;
                secRegistroFormulaContrato = null;
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("errorFechasHF.show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorRegNuevo.show()");
            System.out.println("Error");
        }
    }

    //LIMPIAR NUEVO REGISTRO
    /**
     * Metodo que limpia las casillas de la nueva vigencia
     */
    public void limpiarNuevoFormula() {
        nuevoFormulaContrato = new Formulascontratos();
        nuevoFormulaContrato.setTercero(new Terceros());
        nuevoFormulaContrato.setPeriodicidad(new Periodicidades());
        nuevoFormulaContrato.setFormula(new Formulas());
        index = -1;
        secRegistroFormulaContrato = null;
    }

    ////////--- //// ---////
    //DUPLICAR VL
    /**
     * Metodo que verifica que proceso de duplicar se genera con respecto a la
     * posicion en la pagina que se tiene
     */
    /**
     * Duplica una nueva vigencia localizacion
     */
    public void duplicarFormula() {
        if (index >= 0) {
            duplicarFormulaContrato = new Formulascontratos();
            if (tipoLista == 0) {
                duplicarFormulaContrato.setFechafinal(listFormulasContratosDetalle.get(index).getFechafinal());
                duplicarFormulaContrato.setFechainicial(listFormulasContratosDetalle.get(index).getFechainicial());
                duplicarFormulaContrato.setFormula(listFormulasContratosDetalle.get(index).getFormula());
                duplicarFormulaContrato.setPeriodicidad(listFormulasContratosDetalle.get(index).getPeriodicidad());
                duplicarFormulaContrato.setStrConcepto(listFormulasContratosDetalle.get(index).getStrConcepto());
                duplicarFormulaContrato.setTercero(listFormulasContratosDetalle.get(index).getTercero());
            }
            if (tipoLista == 1) {

                duplicarFormulaContrato.setFechafinal(filtrarListFormulasContratosDetalle.get(index).getFechafinal());
                duplicarFormulaContrato.setFechainicial(filtrarListFormulasContratosDetalle.get(index).getFechainicial());
                duplicarFormulaContrato.setFormula(filtrarListFormulasContratosDetalle.get(index).getFormula());
                duplicarFormulaContrato.setPeriodicidad(filtrarListFormulasContratosDetalle.get(index).getPeriodicidad());
                duplicarFormulaContrato.setStrConcepto(filtrarListFormulasContratosDetalle.get(index).getStrConcepto());
                duplicarFormulaContrato.setTercero(filtrarListFormulasContratosDetalle.get(index).getTercero());
            }
            if (duplicarFormulaContrato.getFormula().getSecuencia() == null) {
                duplicarFormulaContrato.setFormula(new Formulas());
            }
            if (duplicarFormulaContrato.getPeriodicidad().getSecuencia() == null) {
                duplicarFormulaContrato.setPeriodicidad(new Periodicidades());
            }
            if (duplicarFormulaContrato.getTercero().getSecuencia() == null) {
                duplicarFormulaContrato.setTercero(new Terceros());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:DuplicarRegistroFormula");
            context.execute("DuplicarRegistroFormula.show()");
            index = -1;
            secRegistroFormulaContrato = null;
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * VigenciasLocalizaciones
     */
    public void confirmarDuplicar() {
        if (validarRegistrosNuevos(2) == true) {
            boolean validacion = validarFechasRegistroHistoriaFormula(2);
            if (validacion == true) {
                if (bandera == 1) {
                    formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
                    formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
                    formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    formulaNombreLargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreLargo");
                    formulaNombreLargo.setFilterStyle("display: none; visibility: hidden;");
                    formulaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreCorto");
                    formulaNombreCorto.setFilterStyle("display: none; visibility: hidden;");
                    formulaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaEstado");
                    formulaEstado.setFilterStyle("display: none; visibility: hidden;");
                    formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
                    formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
                    formulaNit = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNit");
                    formulaNit.setFilterStyle("display: none; visibility: hidden;");
                    formulaTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
                    formulaTercero.setFilterStyle("display: none; visibility: hidden;");
                    formulaObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaObservacion");
                    formulaObservacion.setFilterStyle("display: none; visibility: hidden;");
                    formulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaConcepto");
                    formulaConcepto.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
                    bandera = 0;
                    filtrarListFormulasContratosDetalle = null;
                    tipoLista = 0;
                }
                k++;
                BigInteger var = BigInteger.valueOf(k);
                if (!duplicarFormulaContrato.getFormula().getObservaciones().isEmpty()) {
                    String aux = duplicarFormulaContrato.getFormula().getObservaciones().toUpperCase();
                    duplicarFormulaContrato.getFormula().setObservaciones(aux);
                }
                duplicarFormulaContrato.setSecuencia(var);
                duplicarFormulaContrato.setContrato(contratoActual);
                duplicarFormulaContrato.setStrConcepto(" ");
                listFormulasContratosCrear.add(duplicarFormulaContrato);
                listFormulasContratosDetalle.add(duplicarFormulaContrato);
                duplicarFormulaContrato = new Formulascontratos();
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:datosFormulaContrato");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:aceptar");
                }
                context.execute("DuplicarRegistroFormula.hide()");
                cambiosFormulaContrato = true;
                index = -1;
                secRegistroFormulaContrato = null;
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("errorFechasHF.show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorRegNuevo.show()");
            System.out.println("Error");
        }
    }

    public void limpiarDuplicarFormula() {
        duplicarFormulaContrato = new Formulascontratos();
        duplicarFormulaContrato.setTercero(new Terceros());
        duplicarFormulaContrato.setPeriodicidad(new Periodicidades());
        duplicarFormulaContrato.setFormula(new Formulas());
        index = -1;
        secRegistroFormulaContrato = null;
    }

    ///////////////////////////////////////////////////////////////
    /**
     * Valida que registro se elimina de que tabla con respecto a la posicion en
     * la pagina
     */
    public void borrarFormula() {
        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listFormulasContratosModificar.isEmpty() && listFormulasContratosModificar.contains(listFormulasContratosDetalle.get(index))) {
                    int modIndex = listFormulasContratosModificar.indexOf(listFormulasContratosDetalle.get(index));
                    listFormulasContratosModificar.remove(modIndex);
                    listFormulasContratosBorrar.add(listFormulasContratosDetalle.get(index));
                } else if (!listFormulasContratosCrear.isEmpty() && listFormulasContratosCrear.contains(listFormulasContratosDetalle.get(index))) {
                    int crearIndex = listFormulasContratosCrear.indexOf(listFormulasContratosDetalle.get(index));
                    listFormulasContratosCrear.remove(crearIndex);
                } else {
                    listFormulasContratosBorrar.add(listFormulasContratosDetalle.get(index));
                }
                listFormulasContratosDetalle.remove(index);
            }
            if (tipoLista == 1) {
                if (!listFormulasContratosModificar.isEmpty() && listFormulasContratosModificar.contains(filtrarListFormulasContratosDetalle.get(index))) {
                    int modIndex = listFormulasContratosModificar.indexOf(filtrarListFormulasContratosDetalle.get(index));
                    listFormulasContratosModificar.remove(modIndex);
                    listFormulasContratosBorrar.add(filtrarListFormulasContratosDetalle.get(index));
                } else if (!listFormulasContratosCrear.isEmpty() && listFormulasContratosCrear.contains(filtrarListFormulasContratosDetalle.get(index))) {
                    int crearIndex = listFormulasContratosCrear.indexOf(filtrarListFormulasContratosDetalle.get(index));
                    listFormulasContratosCrear.remove(crearIndex);
                } else {
                    listFormulasContratosBorrar.add(filtrarListFormulasContratosDetalle.get(index));
                }
                int VLIndex = listFormulasContratosDetalle.indexOf(filtrarListFormulasContratosDetalle.get(index));
                listFormulasContratosDetalle.remove(VLIndex);
                filtrarListFormulasContratosDetalle.remove(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosFormulaContrato");

            index = -1;
            secRegistroFormulaContrato = null;
            cambiosFormulaContrato = true;
            if (guardado == true) {
                guardado = false;
            }
        }
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    /**
     * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        filtradoFormula();

    }

    /**
     * Metodo que acciona el filtrado de la tabla vigencia localizacion
     */
    public void filtradoFormula() {
        if (bandera == 0) {
            formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
            formulaFechaInicial.setFilterStyle("width: 85%");
            formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
            formulaFechaFinal.setFilterStyle("width: 85%");
            formulaNombreLargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreLargo");
            formulaNombreLargo.setFilterStyle("width: 85%");
            formulaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreCorto");
            formulaNombreCorto.setFilterStyle("width: 85%");
            formulaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaEstado");
            formulaEstado.setFilterStyle("width: 85%");
            formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
            formulaPeriodicidad.setFilterStyle("width: 85%");
            formulaNit = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNit");
            formulaNit.setFilterStyle("width: 85%");
            formulaTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
            formulaTercero.setFilterStyle("width: 85%");
            formulaObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaObservacion");
            formulaObservacion.setFilterStyle("width: 85%");
            formulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaConcepto");
            formulaConcepto.setFilterStyle("width: 85%");

            RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
            bandera = 1;
        } else if (bandera == 1) {
            formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
            formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
            formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            formulaNombreLargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreLargo");
            formulaNombreLargo.setFilterStyle("display: none; visibility: hidden;");
            formulaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreCorto");
            formulaNombreCorto.setFilterStyle("display: none; visibility: hidden;");
            formulaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaEstado");
            formulaEstado.setFilterStyle("display: none; visibility: hidden;");
            formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
            formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
            formulaNit = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNit");
            formulaNit.setFilterStyle("display: none; visibility: hidden;");
            formulaTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
            formulaTercero.setFilterStyle("display: none; visibility: hidden;");
            formulaObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaObservacion");
            formulaObservacion.setFilterStyle("display: none; visibility: hidden;");
            formulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaConcepto");
            formulaConcepto.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
            bandera = 0;
            filtrarListFormulasContratosDetalle = null;
            tipoLista = 0;
        }

    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (bandera == 1) {
            formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
            formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
            formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            formulaNombreLargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreLargo");
            formulaNombreLargo.setFilterStyle("display: none; visibility: hidden;");
            formulaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreCorto");
            formulaNombreCorto.setFilterStyle("display: none; visibility: hidden;");
            formulaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaEstado");
            formulaEstado.setFilterStyle("display: none; visibility: hidden;");
            formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
            formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
            formulaNit = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNit");
            formulaNit.setFilterStyle("display: none; visibility: hidden;");
            formulaTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
            formulaTercero.setFilterStyle("display: none; visibility: hidden;");
            formulaObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaObservacion");
            formulaObservacion.setFilterStyle("display: none; visibility: hidden;");
            formulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaConcepto");
            formulaConcepto.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
            bandera = 0;
            filtrarListFormulasContratosDetalle = null;
            tipoLista = 0;
        }
        listFormulasContratosBorrar.clear();
        listFormulasContratosCrear.clear();
        listFormulasContratosModificar.clear();
        index = -1;
        secRegistroFormulaContrato = null;
        listFormulasContratosDetalle = null;
        k = 0;
        listFormulasContratosDetalle = null;
        listFormulasContratos = null;
        guardado = true;
    }
    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO) (list = ESTRUCTURAS - MOTIVOSLOCALIZACIONES - PROYECTOS)

    /**
     * Metodo que ejecuta los dialogos de estructuras, motivos localizaciones,
     * proyectos
     *
     * @param indice Fila de la tabla
     * @param dlg Dialogo
     * @param LND Tipo actualizacion = LISTA - NUEVO - DUPLICADO
     *
     */
    public void asignarIndex(Integer indice, int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            index = indice;
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            context.update("form:FormulaDialogo");
            context.execute("FormulaDialogo.show()");
        } else if (dlg == 1) {
            context.update("form:PeriodicidadDialogo");
            context.execute("PeriodicidadDialogo.show()");
        } else if (dlg == 2) {
            context.update("form:TerceroDialogo");
            context.execute("TerceroDialogo.show()");
        }
    }

    //LISTA DE VALORES DINAMICA
    /**
     * Metodo que activa la lista de valores de todas las tablas con respecto al
     * index activo y la columna activa
     */
    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (index >= 0) {
            if (cualCelda == 2) {
                context.update("form:FormulaDialogo");
                context.execute("FormulaDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 3) {
                context.update("form:FormulaDialogo");
                context.execute("FormulaDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 4) {
                context.update("form:FormulaDialogo");
                context.execute("FormulaDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 5) {
                context.update("form:PeriodicidadDialogo");
                context.execute("PeriodicidadDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 6) {
                context.update("form:TerceroDialogo");
                context.execute("TerceroDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 7) {
                context.update("form:TerceroDialogo");
                context.execute("TerceroDialogo.show()");
                tipoActualizacion = 0;
            }
        }
    }

    /**
     * Valida un proceso de nuevo registro dentro de la pagina con respecto a la
     * posicion en la pagina
     */
    /**
     * Metodo que activa el boton aceptar de la pagina y los dialogos
     *
     * @param tipoNuevo
     * @param Campo
     */
    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("NOMBRELARGO")) {
            if (tipoNuevo == 1) {
                nombreLargo = nuevoFormulaContrato.getFormula().getNombrelargo();
            } else if (tipoNuevo == 2) {
                nombreLargo = duplicarFormulaContrato.getFormula().getNombrelargo();
            }
        } else if (Campo.equals("NOMBRECORTO")) {
            if (tipoNuevo == 1) {
                nombreCorto = nuevoFormulaContrato.getFormula().getNombrecorto();
            } else if (tipoNuevo == 2) {
                nombreCorto = duplicarFormulaContrato.getFormula().getNombrecorto();
            }
        } else if (Campo.equals("ESTADO")) {
            if (tipoNuevo == 1) {
                nombreCorto = nuevoFormulaContrato.getFormula().getEstado();
            } else if (tipoNuevo == 2) {
                nombreCorto = duplicarFormulaContrato.getFormula().getEstado();
            }
        } else if (Campo.equals("PERIODICIDAD")) {
            if (tipoNuevo == 1) {
                nombreCorto = nuevoFormulaContrato.getPeriodicidad().getNombre();
            } else if (tipoNuevo == 2) {
                nombreCorto = duplicarFormulaContrato.getPeriodicidad().getNombre();
            }
        } else if (Campo.equals("NIT")) {
            if (tipoNuevo == 1) {
                nombreCorto = nuevoFormulaContrato.getTercero().getStrNit();
            } else if (tipoNuevo == 2) {
                nombreCorto = duplicarFormulaContrato.getTercero().getStrNit();
            }
        } else if (Campo.equals("TERCERO")) {
            if (tipoNuevo == 1) {
                nombreCorto = nuevoFormulaContrato.getTercero().getNombre();
            } else if (tipoNuevo == 2) {
                nombreCorto = duplicarFormulaContrato.getTercero().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoFormula(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("NOMBRELARGO")) {
            if (tipoNuevo == 1) {
                nuevoFormulaContrato.getFormula().setNombrelargo(nombreLargo);
            } else if (tipoNuevo == 2) {
                duplicarFormulaContrato.getFormula().setNombrelargo(nombreLargo);
            }
            for (int i = 0; i < listFormulas.size(); i++) {
                if (listFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoFormulaContrato.setFormula(listFormulas.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaFormulaNombreLargo");
                    context.update("formularioDialogos:nuevaFormulaNombreCorto");
                    context.update("formularioDialogos:nuevaFormulaEstado");
                    context.update("formularioDialogos:nuevaFormulaObservacion");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaContrato.setFormula(listFormulas.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicaFormulaNombreLargo");
                    context.update("formularioDialogos:duplicaFormulaNombreCorto");
                    context.update("formularioDialogos:duplicaFormulaEstado");
                    context.update("formularioDialogos:duplicaFormulaObservacion");
                }
                listFormulas.clear();
                getListFormulas();
            } else {
                context.update("form:TerceroDialogo");
                context.execute("TerceroDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaFormulaNombreLargo");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicaFormulaNombreLargo");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("NOMBRECORTO")) {
            if (tipoNuevo == 1) {
                nuevoFormulaContrato.getFormula().setNombrecorto(nombreCorto);
            } else if (tipoNuevo == 2) {
                duplicarFormulaContrato.getFormula().setNombrecorto(nombreCorto);
            }
            for (int i = 0; i < listFormulas.size(); i++) {
                if (listFormulas.get(i).getNombrecorto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoFormulaContrato.setFormula(listFormulas.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaFormulaNombreLargo");
                    context.update("formularioDialogos:nuevaFormulaNombreCorto");
                    context.update("formularioDialogos:nuevaFormulaEstado");
                    context.update("formularioDialogos:nuevaFormulaObservacion");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaContrato.setFormula(listFormulas.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicaFormulaNombreLargo");
                    context.update("formularioDialogos:duplicaFormulaNombreCorto");
                    context.update("formularioDialogos:duplicaFormulaEstado");
                    context.update("formularioDialogos:duplicaFormulaObservacion");
                }
                listFormulas.clear();
                getListFormulas();
            } else {
                context.update("form:CiudadDialogo");
                context.execute("CiudadDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaFormulaNombreCorto");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicaFormulaNombreCorto");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("ESTADO")) {
            if (tipoNuevo == 1) {
                nuevoFormulaContrato.getFormula().setEstado(estado);
            } else if (tipoNuevo == 2) {
                duplicarFormulaContrato.getFormula().setEstado(estado);
            }
            for (int i = 0; i < listFormulas.size(); i++) {
                if (listFormulas.get(i).getEstado().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoFormulaContrato.setFormula(listFormulas.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaFormulaNombreLargo");
                    context.update("formularioDialogos:nuevaFormulaNombreCorto");
                    context.update("formularioDialogos:nuevaFormulaEstado");
                    context.update("formularioDialogos:nuevaFormulaObservacion");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaContrato.setFormula(listFormulas.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicaFormulaNombreLargo");
                    context.update("formularioDialogos:duplicaFormulaNombreCorto");
                    context.update("formularioDialogos:duplicaFormulaEstado");
                    context.update("formularioDialogos:duplicaFormulaObservacion");
                }
                listFormulas.clear();
                getListFormulas();
            } else {
                context.update("form:CiudadDialogo");
                context.execute("CiudadDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaFormulaEstado");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicaFormulaEstado");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("PERIODICIDADES")) {
            if (tipoNuevo == 1) {
                nuevoFormulaContrato.getPeriodicidad().setNombre(periodicidad);
            } else if (tipoNuevo == 2) {
                duplicarFormulaContrato.getPeriodicidad().setNombre(periodicidad);
            }
            for (int i = 0; i < listPeriodicidades.size(); i++) {
                if (listPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoFormulaContrato.setPeriodicidad(listPeriodicidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaFormulaPeriodicidad");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaContrato.setPeriodicidad(listPeriodicidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicaFormulaPeriodicidad");
                }
                listPeriodicidades.clear();
                getListPeriodicidades();
            } else {
                context.update("form:CiudadDialogo");
                context.execute("CiudadDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaFormulaPeriodicidad");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicaFormulaPeriodicidad");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
            if (tipoNuevo == 1) {
                nuevoFormulaContrato.getTercero().setStrNit(nit);
            } else if (tipoNuevo == 2) {
                duplicarFormulaContrato.getTercero().setStrNit(nit);
            }
            for (int i = 0; i < listTerceros.size(); i++) {
                if (listTerceros.get(i).getStrNit().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoFormulaContrato.setTercero(listTerceros.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaFormulaNit");
                    context.update("formularioDialogos:nuevaFormulaTercero");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaContrato.setTercero(listTerceros.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicaFormulaNit");
                    context.update("formularioDialogos:duplicaFormulaTercero");
                }
                listTerceros.clear();
                getListTerceros();
            } else {
                context.update("form:CiudadDialogo");
                context.execute("CiudadDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaFormulaNit");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicaFormulaNit");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (tipoNuevo == 1) {
                nuevoFormulaContrato.getTercero().setNombre(tercero);
            } else if (tipoNuevo == 2) {
                duplicarFormulaContrato.getTercero().setNombre(tercero);
            }
            for (int i = 0; i < listTerceros.size(); i++) {
                if (listTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoFormulaContrato.setTercero(listTerceros.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaFormulaNit");
                    context.update("formularioDialogos:nuevaFormulaTercero");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaContrato.setTercero(listTerceros.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicaFormulaNit");
                    context.update("formularioDialogos:duplicaFormulaTercero");
                }
                listTerceros.clear();
                getListTerceros();
            } else {
                context.update("form:CiudadDialogo");
                context.execute("CiudadDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaFormulaTercero");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicaFormulaTercero");
                }
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarFormula() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (!formulaSeleccionada.getObservaciones().isEmpty()) {
                String aux = formulaSeleccionada.getObservaciones().toUpperCase();
                formulaSeleccionada.setObservaciones(aux);
            } else {
                formulaSeleccionada.setObservaciones(" ");
            }
            if (tipoLista == 0) {
                listFormulasContratosDetalle.get(index).setFormula(formulaSeleccionada);
                if (!listFormulasContratosCrear.contains(listFormulasContratosDetalle.get(index))) {
                    if (listFormulasContratosModificar.isEmpty()) {
                        listFormulasContratosModificar.add(listFormulasContratosDetalle.get(index));
                    } else if (!listFormulasContratosModificar.contains(listFormulasContratosDetalle.get(index))) {
                        listFormulasContratosModificar.add(listFormulasContratosDetalle.get(index));
                    }
                }
            } else {
                filtrarListFormulasContratosDetalle.get(index).setFormula(formulaSeleccionada);
                if (!listFormulasContratosCrear.contains(filtrarListFormulasContratosDetalle.get(index))) {
                    if (listFormulasContratosModificar.isEmpty()) {
                        listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(index));
                    } else if (!listFormulasContratosModificar.contains(filtrarListFormulasContratosDetalle.get(index))) {
                        listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            cambiosFormulaContrato = true;
            permitirIndex = true;
            context.update(":form:datosFormulaContrato");
        } else if (tipoActualizacion == 1) {
            nuevoFormulaContrato.setFormula(formulaSeleccionada);
            context.update("formularioDialogos:nuevaFormulaNombreLargo");
            context.update("formularioDialogos:nuevaFormulaNombreCorto");
            context.update("formularioDialogos:nuevaFormulaEstado");
            context.update("formularioDialogos:nuevaFormulaObservacion");
        } else if (tipoActualizacion == 2) {
            duplicarFormulaContrato.setFormula(formulaSeleccionada);
            context.update("formularioDialogos:duplicaFormulaNombreLargo");
            context.update("formularioDialogos:duplicaFormulaNombreCorto");
            context.update("formularioDialogos:duplicaFormulaEstado");
            context.update("formularioDialogos:duplicaFormulaObservacion");
        }
        filtrarListFormulas = null;
        formulaSeleccionada = null;
        aceptar = true;
        index = -1;
        secRegistroFormulaContrato = null;
        tipoActualizacion = -1;

        context.reset("form:lovFormula:globalFilter");
        context.execute("lovFormula.clearFilters()");
        context.execute("FormulaDialogo.hide()");

    }

    public void cancelarCambioFormula() {
        filtrarListFormulas = null;
        formulaSeleccionada = null;
        aceptar = true;
        index = -1;
        secRegistroFormulaContrato = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovFormula:globalFilter");
        context.execute("lovFormula.clearFilters()");
        context.execute("FormulaDialogo.hide()");
    }

    public void actualizarTercero() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listFormulasContratosDetalle.get(index).setTercero(terceroSeleccionado);
                if (!listFormulasContratosCrear.contains(listFormulasContratosDetalle.get(index))) {
                    if (listFormulasContratosModificar.isEmpty()) {
                        listFormulasContratosModificar.add(listFormulasContratosDetalle.get(index));
                    } else if (!listFormulasContratosModificar.contains(listFormulasContratosDetalle.get(index))) {
                        listFormulasContratosModificar.add(listFormulasContratosDetalle.get(index));
                    }
                }
            } else {
                filtrarListFormulasContratosDetalle.get(index).setTercero(terceroSeleccionado);
                if (!listFormulasContratosCrear.contains(filtrarListFormulasContratosDetalle.get(index))) {
                    if (listFormulasContratosModificar.isEmpty()) {
                        listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(index));
                    } else if (!listFormulasContratosModificar.contains(filtrarListFormulasContratosDetalle.get(index))) {
                        listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            cambiosFormulaContrato = true;
            permitirIndex = true;

            context.update(":form:datosFormulaContrato");
        } else if (tipoActualizacion == 1) {
            nuevoFormulaContrato.setTercero(terceroSeleccionado);
            context.update("formularioDialogos:nuevaFormulaNit");
            context.update("formularioDialogos:nuevaFormulaTercero");
        } else if (tipoActualizacion == 2) {
            duplicarFormulaContrato.setTercero(terceroSeleccionado);
            context.update("formularioDialogos:duplicaFormulaNit");
            context.update("formularioDialogos:duplicaFormulaTercero");
        }
        filtrarListTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistroFormulaContrato = null;
        tipoActualizacion = -1;

        context.reset("form:lovTercero:globalFilter");
        context.execute("lovTercero.clearFilters()");
        context.execute("TerceroDialogo.hide()");
    }

    public void cancelarCambioTercero() {
        filtrarListTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistroFormulaContrato = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTercero:globalFilter");
        context.execute("lovTercero.clearFilters()");
        context.execute("TerceroDialogo.hide()");
    }

    public void actualizarPeriodicidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listFormulasContratosDetalle.get(index).setPeriodicidad(periodicidadSeleccionada);
                if (!listFormulasContratosCrear.contains(listFormulasContratosDetalle.get(index))) {
                    if (listFormulasContratosModificar.isEmpty()) {
                        listFormulasContratosModificar.add(listFormulasContratosDetalle.get(index));
                    } else if (!listFormulasContratosModificar.contains(listFormulasContratosDetalle.get(index))) {
                        listFormulasContratosModificar.add(listFormulasContratosDetalle.get(index));
                    }
                }
            } else {
                filtrarListFormulasContratosDetalle.get(index).setPeriodicidad(periodicidadSeleccionada);
                if (!listFormulasContratosCrear.contains(filtrarListFormulasContratosDetalle.get(index))) {
                    if (listFormulasContratosModificar.isEmpty()) {
                        listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(index));
                    } else if (!listFormulasContratosModificar.contains(filtrarListFormulasContratosDetalle.get(index))) {
                        listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            cambiosFormulaContrato = true;
            permitirIndex = true;
            context.update(":form:datosFormulaContrato");
        } else if (tipoActualizacion == 1) {
            nuevoFormulaContrato.setPeriodicidad(periodicidadSeleccionada);
            context.update("formularioDialogos:nuevaFormulaPeriodicidad");
        } else if (tipoActualizacion == 2) {
            duplicarFormulaContrato.setPeriodicidad(periodicidadSeleccionada);
            context.update("formularioDialogos:duplicaFormulaPeriodicidad");
        }
        filtrarListPeriodicidades = null;
        periodicidadSeleccionada = null;
        aceptar = true;
        index = -1;
        secRegistroFormulaContrato = null;
        tipoActualizacion = -1;

        context.reset("form:lovPeriodicidad:globalFilter");
        context.execute("lovPeriodicidad.clearFilters()");
        context.execute("PeriodicidadDialogo.hide()");
    }

    public void cancelarCambioPeriodicidad() {
        filtrarListPeriodicidades = null;
        periodicidadSeleccionada = null;
        aceptar = true;
        index = -1;
        secRegistroFormulaContrato = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovPeriodicidad:globalFilter");
        context.execute("lovPeriodicidad.clearFilters()");
        context.execute("PeriodicidadDialogo.hide()");
    }

    /**
     * Selecciona la tabla a exportar XML con respecto al index activo
     *
     * @return Nombre del dialogo a exportar en XML
     */
    public String exportXML() {
        nombreTabla = ":formExportarFormula:datosFormulaContratosExportar";
        nombreXML = "FormulaContrato_XML";

        return nombreTabla;
    }

    /**
     * Valida la tabla a exportar en PDF con respecto al index activo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        exportPDF_FC();

    }

    /**
     * Metodo que exporta datos a PDF Vigencia Localizacion
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDF_FC() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":formExportarFormula:datosFormulaContratosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "FormulaContrato_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistroFormulaContrato = null;
    }

    /**
     * Verifica que tabla exportar XLS con respecto al index activo
     *
     * @throws IOException
     */
    public void verificarExportXLS() throws IOException {
        exportXLS_FC();

    }

    /**
     * Metodo que exporta datos a XLS Vigencia Sueldos
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLS_FC() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":formExportarFormula:datosFormulaContratosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "FormulaContrato_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistroFormulaContrato = null;
    }

    //EVENTO FILTRAR
    /**
     * Evento que cambia la lista real a la filtrada
     */
    public void eventoFiltrar() {
        if (index >= 0) {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
        }
    }

    public void dialogoSeleccionarFormula() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:BuscarFormulaDialogo");
        context.execute("BuscarFormulaDialogo.show()");
    }

    public void cancelarSeleccionFormula() {
        formulaContratosSeleccionado = new Formulascontratos();
        filtrarListFormulasContratosDetalle = null;
    }

    public void validarSeleccionFormula() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (cambiosFormulaContrato == false) {
            listFormulasContratosDetalle = null;
            listFormulasContratosDetalle = new ArrayList<Formulascontratos>();
            listFormulasContratosDetalle.add(formulaContratosSeleccionado);
            formulaContratosSeleccionado = new Formulascontratos();
            filtrarListPeriodicidades = null;
            aceptar = true;
            context.update("form:datosFormulaContrato");
        } else {
            formulaContratosSeleccionado = new Formulascontratos();
            filtrarListFormulasContratosDetalle = null;
            context.execute("confirmarGuardar.show()");
        }
    }

    public void mostrarTodos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (cambiosFormulaContrato == false) {
            listFormulasContratosDetalle = null;
            getListFormulasContratosDetalle();
            aceptar = true;
            context.update("form:datosFormulaContrato");
        } else {
            context.execute("confirmarGuardar.show()");
        }
    }

    //METODO RASTROS PARA LAS TABLAS EN EMPLVIGENCIASUELDOS
    public void verificarRastroTabla() {
        verificarRastroFormulaContrato();
        index = -1;
    }

    //Verificar Rastro Vigencia Sueldos
    public void verificarRastroFormulaContrato() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listFormulasContratosDetalle != null) {
            if (secRegistroFormulaContrato != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroFormulaContrato, "FORMULASCONTRATOS");
                backUpSecRegistroFormulaCuenta = secRegistroFormulaContrato;
                backUp = secRegistroFormulaContrato;
                secRegistroFormulaContrato = null;
                if (resultado == 1) {
                    context.execute("errorObjetosDB.show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "Formulascontratos";
                    msnConfirmarRastro = "La tabla FORMULASCONTRATOS tiene rastros para el registro seleccionado, ¿desea continuar?";
                    context.update("form:msnConfirmarRastro");
                    context.execute("confirmarRastro.show()");
                } else if (resultado == 3) {
                    context.execute("errorRegistroRastro.show()");
                } else if (resultado == 4) {
                    context.execute("errorTablaConRastro.show()");
                } else if (resultado == 5) {
                    context.execute("errorTablaSinRastro.show()");
                }
            } else {
                context.execute("seleccionarRegistro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("FORMULASCONTRATOS")) {
                nombreTablaRastro = "Formulascontratos";
                msnConfirmarRastroHistorico = "La tabla FORMULASCONTRATOS tiene rastros historicos, ¿Desea continuar?";
                context.update("form:confirmarRastroHistorico");
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }
        }
        index = -1;
    }

    public void limpiarMSNRastros() {
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";
    }

    //GET - SET 
    public List<Formulascontratos> getListFormulasContratosDetalle() {
        try {
            if (listFormulasContratosDetalle == null) {
                listFormulasContratosDetalle = new ArrayList<Formulascontratos>();
                listFormulasContratosDetalle = administrarDetalleLegislacion.consultarListaFormulasContratosContrato(contratoActual.getSecuencia());
                if (listFormulasContratosDetalle != null) {
                    for (int i = 0; i < listFormulasContratosDetalle.size(); i++) {
                        if (listFormulasContratosDetalle.get(i).getFormula() == null) {
                            listFormulasContratosDetalle.get(i).setFormula(new Formulas());
                        }
                        if (listFormulasContratosDetalle.get(i).getTercero() == null) {
                            listFormulasContratosDetalle.get(i).setTercero(new Terceros());
                        }
                        if (listFormulasContratosDetalle.get(i).getPeriodicidad() == null) {
                            listFormulasContratosDetalle.get(i).setPeriodicidad(new Periodicidades());
                        }
                        if (!listFormulasContratosDetalle.get(i).getFormula().getObservaciones().isEmpty()) {
                            String aux = listFormulasContratosDetalle.get(i).getFormula().getObservaciones();
                            listFormulasContratosDetalle.get(i).getFormula().setObservaciones(aux);
                        }
                    }
                }
            }
            return listFormulasContratosDetalle;
        } catch (Exception e) {
            System.out.println("Error getListFormulasContratosDetalle : " + e.toString());
            return null;
        }
    }

    public void setListFormulasContratosDetalle(List<Formulascontratos> t) {
        this.listFormulasContratosDetalle = t;
    }

    public List<Formulascontratos> getFiltrarListFormulasContratosDetalle() {
        return filtrarListFormulasContratosDetalle;
    }

    public void setFiltrarListFormulasContratosDetalle(List<Formulascontratos> t) {
        this.filtrarListFormulasContratosDetalle = t;
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

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public BigInteger getSecRegistroFormulaContrato() {
        return secRegistroFormulaContrato;
    }

    public void setSecRegistroFormulaContrato(BigInteger setSecRegistroFormulaContrato) {
        this.secRegistroFormulaContrato = setSecRegistroFormulaContrato;
    }

    public BigInteger getBackUpSecRegistroFormulaCuenta() {
        return backUpSecRegistroFormulaCuenta;
    }

    public void setBackUpSecRegistroFormulaCuenta(BigInteger b) {
        this.backUpSecRegistroFormulaCuenta = b;
    }

    public List<Formulascontratos> getListFormulasContratosModificar() {
        return listFormulasContratosModificar;
    }

    public void setListFormulasContratosModificar(List<Formulascontratos> setListFormulasContratosModificar) {
        this.listFormulasContratosModificar = setListFormulasContratosModificar;
    }

    public Formulascontratos getNuevoFormulaContrato() {
        return nuevoFormulaContrato;
    }

    public void setNuevoFormulaContrato(Formulascontratos setNuevoFormulaContrato) {
        this.nuevoFormulaContrato = setNuevoFormulaContrato;
    }

    public List<Formulascontratos> getListFormulasContratosCrear() {
        return listFormulasContratosCrear;
    }

    public void setListFormulasContratosCrear(List<Formulascontratos> setListFormulasContratosCrear) {
        this.listFormulasContratosCrear = setListFormulasContratosCrear;
    }

    public List<Formulascontratos> getListFormulasContratosBorrar() {
        return listFormulasContratosBorrar;
    }

    public void setListFormulasContratosBorrar(List<Formulascontratos> setListFormulasContratosBorrar) {
        this.listFormulasContratosBorrar = setListFormulasContratosBorrar;
    }

    public Formulascontratos getEditarFormulaContrato() {
        return editarFormulaContrato;
    }

    public void setEditarFormulaContrato(Formulascontratos setEditarFormulaContrato) {
        this.editarFormulaContrato = setEditarFormulaContrato;
    }

    public Formulascontratos getDuplicarFormulaContrato() {
        return duplicarFormulaContrato;
    }

    public void setDuplicarFormulaContrato(Formulascontratos setDuplicarFormulaContrato) {
        this.duplicarFormulaContrato = setDuplicarFormulaContrato;
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

    public Contratos getBackUpContratoActual() {
        return backUpContratoActual;
    }

    public void setBackUpContratoActual(Contratos setBackUpContratoActual) {
        this.backUpContratoActual = setBackUpContratoActual;
    }

    public List<Terceros> getListTerceros() {
        if (listTerceros == null) {
            listTerceros = administrarDetalleLegislacion.consultarLOVTerceros();
        }
        return listTerceros;
    }

    public void setListTerceros(List<Terceros> setListTerceros) {
        this.listTerceros = setListTerceros;
    }

    public List<Terceros> getFiltrarListTerceros() {
        return filtrarListTerceros;
    }

    public void setFiltrarListTerceros(List<Terceros> filtrar) {
        this.filtrarListTerceros = filtrar;
    }

    public Terceros getTerceroSeleccionado() {
        return terceroSeleccionado;
    }

    public void setTerceroSeleccionado(Terceros seleccionado) {
        this.terceroSeleccionado = seleccionado;
    }

    public Formulascontratos getFormulaContratoActual() {
        return formulaContratoActual;
    }

    public void setFormulaContratoActual(Formulascontratos formulaContratoActual) {
        this.formulaContratoActual = formulaContratoActual;
    }

    public Contratos getContratoActual() {
        return contratoActual;
    }

    public void setContratoActual(Contratos contratoActual) {
        this.contratoActual = contratoActual;
    }

    public Formulascontratos getFormulaContratosSeleccionado() {
        return formulaContratosSeleccionado;
    }

    public void setFormulaContratosSeleccionado(Formulascontratos formulaContratosSeleccionado) {
        this.formulaContratosSeleccionado = formulaContratosSeleccionado;
    }

    public List<Periodicidades> getListPeriodicidades() {
        if (listPeriodicidades == null) {
            listPeriodicidades = administrarDetalleLegislacion.consultarLOVPeriodicidades();
        }
        return listPeriodicidades;
    }

    public void setListPeriodicidades(List<Periodicidades> listPeriodicidades) {
        this.listPeriodicidades = listPeriodicidades;
    }

    public List<Periodicidades> getFiltrarListPeriodicidades() {
        return filtrarListPeriodicidades;
    }

    public void setFiltrarListPeriodicidades(List<Periodicidades> filtrarListPeriodicidades) {
        this.filtrarListPeriodicidades = filtrarListPeriodicidades;
    }

    public Periodicidades getPeriodicidadSeleccionada() {
        return periodicidadSeleccionada;
    }

    public void setPeriodicidadSeleccionada(Periodicidades periodicidadSeleccionada) {
        this.periodicidadSeleccionada = periodicidadSeleccionada;
    }

    public List<Formulas> getListFormulas() {
        if (listFormulas == null) {
            listFormulas = administrarDetalleLegislacion.consultarLOVFormulas();
        }
        return listFormulas;
    }

    public void setListFormulas(List<Formulas> listFormulas) {
        this.listFormulas = listFormulas;
    }

    public List<Formulas> getFiltrarListFormulas() {
        return filtrarListFormulas;
    }

    public void setFiltrarListFormulas(List<Formulas> filtrarListFormulas) {
        this.filtrarListFormulas = filtrarListFormulas;
    }

    public Formulas getFormulaSeleccionada() {
        return formulaSeleccionada;
    }

    public void setFormulaSeleccionada(Formulas formulaSeleccionada) {
        this.formulaSeleccionada = formulaSeleccionada;
    }

    public List<Formulascontratos> getListFormulasContratos() {
        if (listFormulasContratos == null) {
            listFormulasContratos = administrarDetalleLegislacion.consultarListaFormulasContratosContrato(contratoActual.getSecuencia());
        }
        return listFormulasContratos;
    }

    public void setListFormulasContratos(List<Formulascontratos> listFormulasContratos) {
        this.listFormulasContratos = listFormulasContratos;
    }

    public List<Formulascontratos> getFiltrarListFormulasContratos() {
        return filtrarListFormulasContratos;
    }

    public void setFiltrarListFormulasContratos(List<Formulascontratos> filtrarListFormulasContratos) {
        this.filtrarListFormulasContratos = filtrarListFormulasContratos;
    }

}
