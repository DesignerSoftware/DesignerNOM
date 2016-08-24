package Controlador;

import Entidades.Conceptos;
import Entidades.Empresas;
import Entidades.Formulas;
import Entidades.FormulasConceptos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaConceptoInterface;
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
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlFormulaConcepto implements Serializable {

    @EJB
    AdministrarFormulaConceptoInterface administrarFormulaConcepto;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //////////////Formulas//////////////////
    private Formulas formulaActual;
    ///////////FormulasConceptos////////////
    private List<FormulasConceptos> listFormulasConceptosFormula;
    private List<FormulasConceptos> filtrarListFormulasConceptosFormula;
    private FormulasConceptos formulaTablaSeleccionada;
    ///////////FormulasConceptos/////////////
    private int banderaFormulasConceptos;
    private int indexFormulasConceptos;
    private List<FormulasConceptos> listFormulasConceptosModificar;
    private FormulasConceptos nuevaFormulaConcepto;
    private List<FormulasConceptos> listFormulasConceptosCrear;
    private List<FormulasConceptos> listFormulasConceptosBorrar;
    private FormulasConceptos editarFormulaConcepto;
    private int cualCeldaFormulasConceptos, tipoListaFormulasConceptos;
    private FormulasConceptos duplicarFormulaConcepto;
    private boolean cambiosFormulasConceptos;
    private BigInteger secRegistroFormulasConceptos;
    private BigInteger backUpSecRegistroFormulasConceptos;
    private String concepto, empresa, orden, codigoConcepto, nitEmpresa;
    private Date fechaIni, fechaFin;
    private Column formulaFechaInicial, formulaFechaFinal, formulaCodigo, formulaDescripcion, formulaOrden, formulaEmpresa, formulaNIT;
    private boolean permitirIndexFormulasConceptos;
    ////////////Listas Valores FormulasConceptos/////////////
    private List<FormulasConceptos> listFormulasConceptos;
    private List<FormulasConceptos> filtrarListFormulasConceptos;
    private FormulasConceptos formulaSeleccionado;
    private List<Conceptos> listConceptos;
    private List<Conceptos> filtrarListConceptos;
    private Conceptos conceptoSeleccionada;
    //////////////Otros////////////////Otros////////////////////
    private boolean aceptar;
    private boolean guardado;
    private BigInteger l;
    private int k;
    private String nombreXML, nombreExportar;
    private String nombreTabla;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    private int tipoActualizacion;
    private Date fechaParametro;
    //
    private String infoRegistro;
    private String altoTabla;
    private String infoRegistroConcepto, infoRegistroOrden;

    public ControlFormulaConcepto() {
        altoTabla = "310";
        formulaActual = new Formulas();
        listFormulasConceptosFormula = null;
        fechaParametro = new Date(1, 1, 0);
        permitirIndexFormulasConceptos = true;

        listFormulasConceptos = null;
        listConceptos = null;

        formulaSeleccionado = new FormulasConceptos();
        conceptoSeleccionada = new Conceptos();

        nombreExportar = "";
        nombreTablaRastro = "";
        backUp = null;
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";

        secRegistroFormulasConceptos = null;
        backUpSecRegistroFormulasConceptos = null;
        aceptar = true;
        k = 0;

        listFormulasConceptosBorrar = new ArrayList<FormulasConceptos>();
        listFormulasConceptosCrear = new ArrayList<FormulasConceptos>();
        listFormulasConceptosModificar = new ArrayList<FormulasConceptos>();
        editarFormulaConcepto = new FormulasConceptos();
        cualCeldaFormulasConceptos = -1;
        tipoListaFormulasConceptos = 0;
        guardado = true;
        nuevaFormulaConcepto = new FormulasConceptos();
        nuevaFormulaConcepto.setConcepto(new Conceptos());
        nuevaFormulaConcepto.getConcepto().setEmpresa(new Empresas());
        indexFormulasConceptos = -1;
        banderaFormulasConceptos = 0;
        nombreTabla = ":formExportarFormula:datosFormulaConceptoExportar";
        nombreXML = "FormulaConcepto_XML";
        duplicarFormulaConcepto = new FormulasConceptos();
        cambiosFormulasConceptos = false;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarFormulaConcepto.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirFormula(BigInteger secuencia) {
        formulaActual = administrarFormulaConcepto.formulaActual(secuencia);
        listFormulasConceptosFormula = null;
        getListFormulasConceptosFormula();
        if (listFormulasConceptosFormula != null) {
            infoRegistro = "Cantidad de registros : " + listFormulasConceptosFormula.size();
        } else {
            infoRegistro = "Cantidad de registros : 0";
        }
    }

    public void modificarFormulaConcepto(int indice) {
        if (tipoListaFormulasConceptos == 0) {
            if (!listFormulasConceptosCrear.contains(listFormulasConceptosFormula.get(indice))) {
                if (listFormulasConceptosModificar.isEmpty()) {
                    listFormulasConceptosModificar.add(listFormulasConceptosFormula.get(indice));
                } else if (!listFormulasConceptosModificar.contains(listFormulasConceptosFormula.get(indice))) {
                    listFormulasConceptosModificar.add(listFormulasConceptosFormula.get(indice));
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            indexFormulasConceptos = -1;
            secRegistroFormulasConceptos = null;
        } else {
            if (!listFormulasConceptosCrear.contains(filtrarListFormulasConceptosFormula.get(indice))) {
                if (listFormulasConceptosModificar.isEmpty()) {
                    listFormulasConceptosModificar.add(filtrarListFormulasConceptosFormula.get(indice));
                } else if (!listFormulasConceptosModificar.contains(filtrarListFormulasConceptosFormula.get(indice))) {
                    listFormulasConceptosModificar.add(filtrarListFormulasConceptosFormula.get(indice));
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            indexFormulasConceptos = -1;
            secRegistroFormulasConceptos = null;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosFormulaConcepto");
        cambiosFormulasConceptos = true;
    }

    public void modificarFormulaConcepto(int indice, String confirmarCambio, String valorConfirmar) {
        indexFormulasConceptos = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CODIGO")) {
            if (tipoListaFormulasConceptos == 0) {
                listFormulasConceptosFormula.get(indice).getConcepto().setCodigoSTR(codigoConcepto);
            } else {
                filtrarListFormulasConceptosFormula.get(indice).getConcepto().setCodigoSTR(codigoConcepto);
            }
            for (int i = 0; i < listConceptos.size(); i++) {
                if (listConceptos.get(i).getCodigoSTR().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaFormulasConceptos == 0) {
                    listFormulasConceptosFormula.get(indice).setConcepto(listConceptos.get(indiceUnicoElemento));
                } else {
                    filtrarListFormulasConceptosFormula.get(indice).setConcepto(listConceptos.get(indiceUnicoElemento));
                }
                listConceptos.clear();
                getListConceptos();
            } else {
                permitirIndexFormulasConceptos = false;
                context.update("form:ConceptoDialogo");
                context.execute("ConceptoDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            if (tipoListaFormulasConceptos == 0) {
                listFormulasConceptosFormula.get(indice).getConcepto().setDescripcion(concepto);
            } else {
                filtrarListFormulasConceptosFormula.get(indice).getConcepto().setDescripcion(concepto);
            }
            for (int i = 0; i < listConceptos.size(); i++) {
                if (listConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaFormulasConceptos == 0) {
                    listFormulasConceptosFormula.get(indice).setConcepto(listConceptos.get(indiceUnicoElemento));
                } else {
                    filtrarListFormulasConceptosFormula.get(indice).setConcepto(listConceptos.get(indiceUnicoElemento));
                }
                listConceptos.clear();
                getListConceptos();
            } else {
                permitirIndexFormulasConceptos = false;
                context.update("form:ConceptoDialogo");
                context.execute("ConceptoDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("ORDEN")) {
            if (tipoListaFormulasConceptos == 0) {
                listFormulasConceptosFormula.get(indice).setStrOrden(orden);
            } else {
                filtrarListFormulasConceptosFormula.get(indice).setStrOrden(orden);
            }
            for (int i = 0; i < listFormulasConceptos.size(); i++) {
                if (listFormulasConceptos.get(i).getStrOrden().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaFormulasConceptos == 0) {
                    listFormulasConceptosFormula.get(indice).setStrOrden(listFormulasConceptos.get(indiceUnicoElemento).getStrOrden());
                } else {
                    filtrarListFormulasConceptosFormula.get(indice).setStrOrden(listFormulasConceptos.get(indiceUnicoElemento).getStrOrden());
                }
                listFormulasConceptos.clear();
                getListFormulasConceptos();
            } else {
                permitirIndexFormulasConceptos = false;
                context.update("form:OrdenDialogo");
                context.execute("OrdenDialogo.show()");
                tipoActualizacion = 0;
            }

        }
        if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
            if (tipoListaFormulasConceptos == 0) {
                listFormulasConceptosFormula.get(indice).getConcepto().getEmpresa().setNombre(empresa);
            } else {
                filtrarListFormulasConceptosFormula.get(indice).getConcepto().getEmpresa().setNombre(empresa);
            }
            for (int i = 0; i < listConceptos.size(); i++) {
                if (listConceptos.get(i).getEmpresa().getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaFormulasConceptos == 0) {
                    listFormulasConceptosFormula.get(indice).setConcepto(listConceptos.get(indiceUnicoElemento));
                } else {
                    filtrarListFormulasConceptosFormula.get(indice).setConcepto(listConceptos.get(indiceUnicoElemento));
                }
                listConceptos.clear();
                getListConceptos();
            } else {
                permitirIndexFormulasConceptos = false;
                context.update("form:ConceptoDialogo");
                context.execute("ConceptoDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("NIT")) {
            if (tipoListaFormulasConceptos == 0) {
                listFormulasConceptosFormula.get(indice).getConcepto().getEmpresa().setStrNit(nitEmpresa);
            } else {
                filtrarListFormulasConceptosFormula.get(indice).getConcepto().getEmpresa().setStrNit(nitEmpresa);
            }
            for (int i = 0; i < listConceptos.size(); i++) {
                if (listConceptos.get(i).getEmpresa().getStrNit().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaFormulasConceptos == 0) {
                    listFormulasConceptosFormula.get(indice).setConcepto(listConceptos.get(indiceUnicoElemento));
                } else {
                    filtrarListFormulasConceptosFormula.get(indice).setConcepto(listConceptos.get(indiceUnicoElemento));
                }
                listConceptos.clear();
                getListConceptos();
            } else {
                permitirIndexFormulasConceptos = false;
                context.update("form:ConceptoDialogo");
                context.execute("ConceptoDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoListaFormulasConceptos == 0) {
                if (!listFormulasConceptosCrear.contains(listFormulasConceptosFormula.get(indice))) {
                    if (listFormulasConceptosModificar.isEmpty()) {
                        listFormulasConceptosModificar.add(listFormulasConceptosFormula.get(indice));
                    } else if (!listFormulasConceptosModificar.contains(listFormulasConceptosFormula.get(indice))) {
                        listFormulasConceptosModificar.add(listFormulasConceptosFormula.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                indexFormulasConceptos = -1;
                secRegistroFormulasConceptos = null;
            } else {
                if (!listFormulasConceptosCrear.contains(filtrarListFormulasConceptosFormula.get(indice))) {
                    if (listFormulasConceptosModificar.isEmpty()) {
                        listFormulasConceptosModificar.add(filtrarListFormulasConceptosFormula.get(indice));
                    } else if (!listFormulasConceptosModificar.contains(filtrarListFormulasConceptosFormula.get(indice))) {
                        listFormulasConceptosModificar.add(filtrarListFormulasConceptosFormula.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                indexFormulasConceptos = -1;
                secRegistroFormulasConceptos = null;
            }
            cambiosFormulasConceptos = true;
        }
        context.update("form:datosFormulaConcepto");
    }

    ///////////////////////////////////////////////////////////////////////////
    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("CODIGO")) {
            if (tipoNuevo == 1) {
                codigoConcepto = nuevaFormulaConcepto.getConcepto().getCodigoSTR();
            } else if (tipoNuevo == 2) {
                codigoConcepto = duplicarFormulaConcepto.getConcepto().getCodigoSTR();
            }
        } else if (Campo.equals("CONCEPTO")) {
            if (tipoNuevo == 1) {
                concepto = nuevaFormulaConcepto.getConcepto().getDescripcion();
            } else if (tipoNuevo == 2) {
                concepto = duplicarFormulaConcepto.getConcepto().getDescripcion();
            }
        } else if (Campo.equals("ORDEN")) {
            if (tipoNuevo == 1) {
                orden = nuevaFormulaConcepto.getStrOrden();
            } else if (tipoNuevo == 2) {
                orden = duplicarFormulaConcepto.getStrOrden();
            }
        } else if (Campo.equals("EMPRESA")) {
            if (tipoNuevo == 1) {
                empresa = nuevaFormulaConcepto.getConcepto().getEmpresa().getNombre();
            } else if (tipoNuevo == 2) {
                empresa = duplicarFormulaConcepto.getConcepto().getEmpresa().getNombre();
            }
        } else if (Campo.equals("NIT")) {
            if (tipoNuevo == 1) {
                nitEmpresa = nuevaFormulaConcepto.getConcepto().getEmpresa().getStrNit();
            } else if (tipoNuevo == 2) {
                nitEmpresa = duplicarFormulaConcepto.getConcepto().getEmpresa().getStrNit();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoFormulaConcepto(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CODIGO")) {
            if (tipoNuevo == 1) {
                nuevaFormulaConcepto.getConcepto().setCodigoSTR(codigoConcepto);
            } else if (tipoNuevo == 2) {
                duplicarFormulaConcepto.getConcepto().setCodigoSTR(codigoConcepto);
            }
            for (int i = 0; i < listConceptos.size(); i++) {
                if (listConceptos.get(i).getCodigoSTR().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaFormulaConcepto.setConcepto(listConceptos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaCodigo");
                    context.update("formularioDialogos:nuevaConcepto");
                    context.update("formularioDialogos:nuevaEmpresa");
                    context.update("formularioDialogos:nuevaNIT");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaConcepto.setConcepto(listConceptos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarCodigo");
                    context.update("formularioDialogos:duplicarConcepto");
                    context.update("formularioDialogos:duplicarEmpresa");
                    context.update("formularioDialogos:duplicarNIT");
                }
                listConceptos.clear();
                getListConceptos();
            } else {
                context.update("form:ConceptoDialogo");
                context.execute("ConceptoDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaCodigo");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarCodigo");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            if (tipoNuevo == 1) {
                nuevaFormulaConcepto.getConcepto().setDescripcion(concepto);
            } else if (tipoNuevo == 2) {
                duplicarFormulaConcepto.getConcepto().setDescripcion(concepto);
            }
            for (int i = 0; i < listConceptos.size(); i++) {
                if (listConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaFormulaConcepto.setConcepto(listConceptos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaCodigo");
                    context.update("formularioDialogos:nuevaConcepto");
                    context.update("formularioDialogos:nuevaEmpresa");
                    context.update("formularioDialogos:nuevaNIT");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaConcepto.setConcepto(listConceptos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarCodigo");
                    context.update("formularioDialogos:duplicarConcepto");
                    context.update("formularioDialogos:duplicarEmpresa");
                    context.update("formularioDialogos:duplicarNIT");
                }
                listConceptos.clear();
                getListConceptos();
            } else {
                context.update("form:ConceptoDialogo");
                context.execute("ConceptoDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaConcepto");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarConcepto");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("ORDEN")) {
            if (tipoNuevo == 1) {
                nuevaFormulaConcepto.setStrOrden(orden);
            } else if (tipoNuevo == 2) {
                duplicarFormulaConcepto.setStrOrden(orden);
            }
            for (int i = 0; i < listFormulasConceptos.size(); i++) {
                if (listFormulasConceptos.get(i).getStrOrden().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaFormulaConcepto.setStrOrden(listFormulasConceptos.get(indiceUnicoElemento).getStrOrden());
                    context.update("formularioDialogos:nuevaOrden");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaConcepto.setStrOrden(listFormulasConceptos.get(indiceUnicoElemento).getStrOrden());
                    context.update("formularioDialogos:duplicarOrden");
                }
                listFormulasConceptos.clear();
                getListFormulasConceptos();
            } else {
                context.update("form:OrdenDialogo");
                context.execute("OrdenDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaOrden");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarOrden");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
            if (tipoNuevo == 1) {
                nuevaFormulaConcepto.getConcepto().getEmpresa().setNombre(empresa);
            } else if (tipoNuevo == 2) {
                duplicarFormulaConcepto.getConcepto().getEmpresa().setNombre(empresa);
            }
            for (int i = 0; i < listConceptos.size(); i++) {
                if (listConceptos.get(i).getEmpresa().getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaFormulaConcepto.setConcepto(listConceptos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaCodigo");
                    context.update("formularioDialogos:nuevaConcepto");
                    context.update("formularioDialogos:nuevaEmpresa");
                    context.update("formularioDialogos:nuevaNIT");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaConcepto.setConcepto(listConceptos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarCodigo");
                    context.update("formularioDialogos:duplicarConcepto");
                    context.update("formularioDialogos:duplicarEmpresa");
                    context.update("formularioDialogos:duplicarNIT");
                }
                listConceptos.clear();
                getListConceptos();
            } else {
                context.update("form:ConceptoDialogo");
                context.execute("ConceptoDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaEmpresa");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarEmpresa");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
            if (tipoNuevo == 1) {
                nuevaFormulaConcepto.getConcepto().getEmpresa().setStrNit(nitEmpresa);
            } else if (tipoNuevo == 2) {
                duplicarFormulaConcepto.getConcepto().getEmpresa().setStrNit(nitEmpresa);
            }
            for (int i = 0; i < listConceptos.size(); i++) {
                if (listConceptos.get(i).getEmpresa().getStrNit().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaFormulaConcepto.setConcepto(listConceptos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaCodigo");
                    context.update("formularioDialogos:nuevaConcepto");
                    context.update("formularioDialogos:nuevaEmpresa");
                    context.update("formularioDialogos:nuevaNIT");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaConcepto.setConcepto(listConceptos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarCodigo");
                    context.update("formularioDialogos:duplicarConcepto");
                    context.update("formularioDialogos:duplicarEmpresa");
                    context.update("formularioDialogos:duplicarNIT");
                }
                listConceptos.clear();
                getListConceptos();
            } else {
                context.update("form:ConceptoDialogo");
                context.execute("ConceptoDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaNIT");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarNIT");
                }
            }
        }
    }

    public void cambiarIndiceFormulaConcepto(int indice, int celda) {
        if (permitirIndexFormulasConceptos == true) {
            cualCeldaFormulasConceptos = celda;
            indexFormulasConceptos = indice;
            if (tipoListaFormulasConceptos == 0) {
                secRegistroFormulasConceptos = listFormulasConceptosFormula.get(indexFormulasConceptos).getSecuencia();
                ///////// Captura Objetos Para Campos NotNull ///////////
                fechaIni = listFormulasConceptosFormula.get(indexFormulasConceptos).getFechainicial();
                fechaFin = listFormulasConceptosFormula.get(indexFormulasConceptos).getFechafinal();
                concepto = listFormulasConceptosFormula.get(indexFormulasConceptos).getConcepto().getDescripcion();
                orden = listFormulasConceptosFormula.get(indexFormulasConceptos).getStrOrden();
                empresa = listFormulasConceptosFormula.get(indexFormulasConceptos).getConcepto().getEmpresa().getNombre();
                nitEmpresa = listFormulasConceptosFormula.get(indexFormulasConceptos).getConcepto().getEmpresa().getStrNit();
                codigoConcepto = listFormulasConceptosFormula.get(indexFormulasConceptos).getConcepto().getCodigoSTR();
            } else {
                secRegistroFormulasConceptos = filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getSecuencia();
                ///////// Captura Objetos Para Campos NotNull ///////////
                fechaIni = filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getFechainicial();
                fechaFin = filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getFechafinal();
                concepto = filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getConcepto().getDescripcion();
                orden = filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getStrOrden();
                empresa = filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getConcepto().getEmpresa().getNombre();
                nitEmpresa = filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getConcepto().getEmpresa().getStrNit();
                codigoConcepto = filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getConcepto().getCodigoSTR();
            }
        }
    }

    public boolean validarFechasRegistroFormulas(int i) {
        boolean retorno = false;
        if (i == 0) {
            FormulasConceptos auxiliar = listFormulasConceptosFormula.get(i);
            if (auxiliar.getFechainicial().after(fechaParametro) && (auxiliar.getFechainicial().before(auxiliar.getFechafinal()))) {
                retorno = true;
            }
        }
        if (i == 1) {
            if (nuevaFormulaConcepto.getFechainicial().after(fechaParametro) && (nuevaFormulaConcepto.getFechainicial().before(nuevaFormulaConcepto.getFechafinal()))) {
                retorno = true;
            }
        }
        if (i == 2) {
            if (duplicarFormulaConcepto.getFechainicial().after(fechaParametro) && (duplicarFormulaConcepto.getFechainicial().before(duplicarFormulaConcepto.getFechafinal()))) {
                retorno = true;
            }
        }
        return retorno;
    }

    public void modificacionesFechaFormula(int i, int c) {
        FormulasConceptos auxiliar = null;
        if (tipoListaFormulasConceptos == 0) {
            auxiliar = listFormulasConceptosFormula.get(i);
        }
        if (tipoListaFormulasConceptos == 1) {
            auxiliar = filtrarListFormulasConceptosFormula.get(i);
        }
        if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() != null)) {
            boolean validacion = validarFechasRegistroFormulas(0);
            if (validacion == true) {
                cambiarIndiceFormulaConcepto(i, c);
                modificarFormulaConcepto(i);
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:datosFormulaConcepto");
            } else {
                System.out.println("Error de fechas de ingreso");
                RequestContext context = RequestContext.getCurrentInstance();
                if (tipoListaFormulasConceptos == 0) {
                    listFormulasConceptosFormula.get(indexFormulasConceptos).setFechainicial(fechaIni);
                    listFormulasConceptosFormula.get(indexFormulasConceptos).setFechafinal(fechaFin);
                }
                if (tipoListaFormulasConceptos == 1) {
                    filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).setFechainicial(fechaIni);
                    filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).setFechafinal(fechaFin);
                }
                context.update("form:datosFormulaConcepto");
                context.execute("errorFechasFC.show()");
                indexFormulasConceptos = -1;
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            if (tipoListaFormulasConceptos == 0) {
                listFormulasConceptosFormula.get(indexFormulasConceptos).setFechainicial(fechaIni);
                listFormulasConceptosFormula.get(indexFormulasConceptos).setFechafinal(fechaFin);
            }
            if (tipoListaFormulasConceptos == 1) {
                filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).setFechainicial(fechaIni);
                filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).setFechafinal(fechaFin);
            }
            context.update("form:datosFormulaConcepto");
            context.execute("errorRegNuevo.show()");
            indexFormulasConceptos = -1;
        }
    }

    //GUARDAR
    /**
     */
    public void guardadoGeneral() {
        if (cambiosFormulasConceptos == true) {
            guardarCambiosFormula();
        }
    }

    public void guardarCambiosFormula() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (!listFormulasConceptosBorrar.isEmpty()) {
                administrarFormulaConcepto.borrarFormulasConceptos(listFormulasConceptosBorrar);
                listFormulasConceptosBorrar.clear();
            }
            if (!listFormulasConceptosCrear.isEmpty()) {
                administrarFormulaConcepto.crearFormulasConceptos(listFormulasConceptosCrear);
                listFormulasConceptosCrear.clear();
            }
            if (!listFormulasConceptosModificar.isEmpty()) {
                administrarFormulaConcepto.editarFormulasConceptos(listFormulasConceptosModificar);
                listFormulasConceptosModificar.clear();
            }
            listFormulasConceptosFormula = null;
            getListFormulasConceptosFormula();
            if (listFormulasConceptosFormula != null) {
                infoRegistro = "Cantidad de registros : " + listFormulasConceptosFormula.size();
            } else {
                infoRegistro = "Cantidad de registros : 0";
            }
            context.update("form:informacionRegistro");
            context.update("form:datosFormulaConcepto");
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            k = 0;
            indexFormulasConceptos = -1;
            secRegistroFormulasConceptos = null;
            cambiosFormulasConceptos = false;
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosFormula  : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
    }

    //CANCELAR MODIFICACIONES
    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacion() {
        if (banderaFormulasConceptos == 1) {
            altoTabla = "310";
            formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaInicial");
            formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaFinal");
            formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            formulaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaCodigo");
            formulaCodigo.setFilterStyle("display: none; visibility: hidden;");
            formulaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaDescripcion");
            formulaDescripcion.setFilterStyle("display: none; visibility: hidden;");
            formulaOrden = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaOrden");
            formulaOrden.setFilterStyle("display: none; visibility: hidden;");
            formulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaEmpresa");
            formulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
            formulaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaNIT");
            formulaNIT.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
            banderaFormulasConceptos = 0;
            filtrarListFormulasConceptosFormula = null;
            tipoListaFormulasConceptos = 0;
        }

        listFormulasConceptosBorrar.clear();
        listFormulasConceptosCrear.clear();
        listFormulasConceptosModificar.clear();
        indexFormulasConceptos = -1;
        secRegistroFormulasConceptos = null;
        k = 0;
        listFormulasConceptosFormula = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        cambiosFormulasConceptos = false;
        permitirIndexFormulasConceptos = true;
        getListFormulasConceptosFormula();
        RequestContext context = RequestContext.getCurrentInstance();
        getListFormulasConceptosFormula();
        if (listFormulasConceptosFormula != null) {
            infoRegistro = "Cantidad de registros : " + listFormulasConceptosFormula.size();
        } else {
            infoRegistro = "Cantidad de registros : 0";
        }
        context.update("form:informacionRegistro");
        context.update("form:datosFormulaConcepto");
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (indexFormulasConceptos >= 0) {
            if (cualCeldaFormulasConceptos == 2) {
                context.update("form:ConceptoDialogo");
                context.execute("ConceptoDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaFormulasConceptos == 3) {
                context.update("form:ConceptoDialogo");
                context.execute("ConceptoDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaFormulasConceptos == 4) {
                context.update("form:OrdenDialogo");
                context.execute("OrdenDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaFormulasConceptos == 5) {
                context.update("form:ConceptoDialogo");
                context.execute("ConceptoDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaFormulasConceptos == 6) {
                context.update("form:ConceptoDialogo");
                context.execute("ConceptoDialogo.show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void asignarIndex(Integer indice, int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
        } else if (dlg == 1) {
            context.update("form:OrdenDialogo");
            context.execute("OrdenDialogo.show()");
        }
    }

    public void editarCelda() {
        if (indexFormulasConceptos >= 0) {
            if (tipoListaFormulasConceptos == 0) {
                editarFormulaConcepto = listFormulasConceptosFormula.get(indexFormulasConceptos);
            }
            if (tipoListaFormulasConceptos == 1) {
                editarFormulaConcepto = filtrarListFormulasConceptosFormula.get(indexFormulasConceptos);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaFormulasConceptos == 0) {
                context.update("formularioDialogos:editarFechaInicialFD");
                context.execute("editarFechaInicialFD.show()");
                cualCeldaFormulasConceptos = -1;
            } else if (cualCeldaFormulasConceptos == 1) {
                context.update("formularioDialogos:editarFechaFinalFD");
                context.execute("editarFechaFinalFD.show()");
                cualCeldaFormulasConceptos = -1;
            } else if (cualCeldaFormulasConceptos == 2) {
                context.update("formularioDialogos:editarCodigoFD");
                context.execute("editarCodigoFD.show()");
                cualCeldaFormulasConceptos = -1;
            } else if (cualCeldaFormulasConceptos == 3) {
                context.update("formularioDialogos:editarConceptoFD");
                context.execute("editarConceptoFD.show()");
                cualCeldaFormulasConceptos = -1;
            } else if (cualCeldaFormulasConceptos == 4) {
                context.update("formularioDialogos:editarOrdenFD");
                context.execute("editarOrdenFD.show()");
                cualCeldaFormulasConceptos = -1;
            } else if (cualCeldaFormulasConceptos == 5) {
                context.update("formularioDialogos:editarEmpresaFD");
                context.execute("editarEmpresaFD.show()");
                cualCeldaFormulasConceptos = -1;
            } else if (cualCeldaFormulasConceptos == 6) {
                context.update("formularioDialogos:editarNitFD");
                context.execute("editarNitFD.show()");
                cualCeldaFormulasConceptos = -1;
            }
            indexFormulasConceptos = -1;
            secRegistroFormulasConceptos = null;
        }
    }

    public void ingresoNuevoRegistro() {
        validarIngresoNuevaFormula();
    }

    public void validarIngresoNuevaFormula() {
        RequestContext context = RequestContext.getCurrentInstance();
        limpiarNuevoFormula();
        context.update("form:nuevaF");
        context.update("formularioDialogos:NuevoRegistroFormula");
        context.execute("NuevoRegistroFormula.show()");

    }

    public void validarDuplicadoRegistro() {
        if (indexFormulasConceptos >= 0) {
            duplicarFormulaM();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("seleccionarRegistro.show()");
        }
    }

    public void validarBorradoRegistro() {
        if (indexFormulasConceptos >= 0) {
            borrarFormula();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("seleccionarRegistro.show()");
        }
    }

    public boolean validarNuevosDatosFormula(int i) {
        boolean retorno = false;
        if (i == 1) {
            if ((nuevaFormulaConcepto.getConcepto().getSecuencia() != null)
                    && (nuevaFormulaConcepto.getFechainicial() != null)
                    && (nuevaFormulaConcepto.getFechafinal() != null)
                    && (!nuevaFormulaConcepto.getStrOrden().isEmpty())) {
                return true;
            }
        }
        if (i == 2) {
            if ((duplicarFormulaConcepto.getConcepto().getSecuencia() != null)
                    && (duplicarFormulaConcepto.getFechainicial() != null)
                    && (duplicarFormulaConcepto.getFechafinal() != null)
                    && (!duplicarFormulaConcepto.getStrOrden().isEmpty())) {
                return true;
            }
        }
        return retorno;
    }

    public void agregarNuevoFormula() {
        boolean resp = validarNuevosDatosFormula(1);
        if (resp == true) {
            boolean validacion = validarFechasRegistroFormulas(1);
            if (validacion == true) {
                if (banderaFormulasConceptos == 1) {
                    altoTabla = "310";
                    formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaInicial");
                    formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaFinal");
                    formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    formulaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaCodigo");
                    formulaCodigo.setFilterStyle("display: none; visibility: hidden;");
                    formulaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaDescripcion");
                    formulaDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    formulaOrden = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaOrden");
                    formulaOrden.setFilterStyle("display: none; visibility: hidden;");
                    formulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaEmpresa");
                    formulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
                    formulaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaNIT");
                    formulaNIT.setFilterStyle("display: none; visibility: hidden;");

                    RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
                    banderaFormulasConceptos = 0;
                    filtrarListFormulasConceptosFormula = null;
                    tipoListaFormulasConceptos = 0;
                }
                k++;

                BigInteger var = BigInteger.valueOf(k);
                nuevaFormulaConcepto.setSecuencia(var);
                nuevaFormulaConcepto.setTipo("C");
                nuevaFormulaConcepto.setFormula(formulaActual);
                listFormulasConceptosCrear.add(nuevaFormulaConcepto);
                listFormulasConceptosFormula.add(nuevaFormulaConcepto);
                ////------////
                nuevaFormulaConcepto = new FormulasConceptos();
                nuevaFormulaConcepto.setConcepto(new Conceptos());
                nuevaFormulaConcepto.getConcepto().setEmpresa(new Empresas());
                ////-----////
                RequestContext context = RequestContext.getCurrentInstance();
                infoRegistro = "Cantidad de registros : " + listFormulasConceptosFormula.size();
                context.update("form:informacionRegistro");
                context.execute("NuevoRegistroFormula.hide()");
                context.update("form:datosFormulaConcepto");
                context.update("formularioDialogos:NuevoRegistroFormula");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                cambiosFormulasConceptos = true;
                indexFormulasConceptos = -1;
                secRegistroFormulasConceptos = null;
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("errorFechasFC.show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorRegNuevo.show()");
        }
    }

    public void limpiarNuevoFormula() {
        nuevaFormulaConcepto = new FormulasConceptos();
        nuevaFormulaConcepto.setConcepto(new Conceptos());
        nuevaFormulaConcepto.getConcepto().setEmpresa(new Empresas());
        indexFormulasConceptos = -1;
        secRegistroFormulasConceptos = null;
    }

    public void duplicarFormulaM() {

        if (indexFormulasConceptos >= 0) {
            duplicarFormulaConcepto = new FormulasConceptos();
            if (tipoListaFormulasConceptos == 0) {
                duplicarFormulaConcepto.setConcepto(listFormulasConceptosFormula.get(indexFormulasConceptos).getConcepto());
                duplicarFormulaConcepto.setFechafinal(listFormulasConceptosFormula.get(indexFormulasConceptos).getFechafinal());
                duplicarFormulaConcepto.setFechainicial(listFormulasConceptosFormula.get(indexFormulasConceptos).getFechainicial());
                duplicarFormulaConcepto.setStrOrden(listFormulasConceptosFormula.get(indexFormulasConceptos).getStrOrden());
                duplicarFormulaConcepto.setTipo(listFormulasConceptosFormula.get(indexFormulasConceptos).getTipo());
            }
            if (tipoListaFormulasConceptos == 1) {
                duplicarFormulaConcepto.setConcepto(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getConcepto());
                duplicarFormulaConcepto.setFechafinal(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getFechafinal());
                duplicarFormulaConcepto.setFechainicial(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getFechainicial());
                duplicarFormulaConcepto.setStrOrden(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getStrOrden());
                duplicarFormulaConcepto.setTipo(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).getTipo());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:DuplicarRegistroFormula");
            context.execute("DuplicarRegistroFormula.show()");

        }
    }

    public void confirmarDuplicarFormula() {
        boolean resp = validarNuevosDatosFormula(2);
        if (resp == true) {
            boolean validacion = validarFechasRegistroFormulas(2);
            if (validacion == true) {

                if (banderaFormulasConceptos == 1) {
                    altoTabla = "310";
                    formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaInicial");
                    formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaFinal");
                    formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    formulaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaCodigo");
                    formulaCodigo.setFilterStyle("display: none; visibility: hidden;");
                    formulaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaDescripcion");
                    formulaDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    formulaOrden = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaOrden");
                    formulaOrden.setFilterStyle("display: none; visibility: hidden;");
                    formulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaEmpresa");
                    formulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
                    formulaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaNIT");
                    formulaNIT.setFilterStyle("display: none; visibility: hidden;");

                    RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
                    banderaFormulasConceptos = 0;
                    filtrarListFormulasConceptosFormula = null;
                    tipoListaFormulasConceptos = 0;
                }
                k++;
                BigInteger var = BigInteger.valueOf(k);

                duplicarFormulaConcepto.setSecuencia(var);
                duplicarFormulaConcepto.setFormula(formulaActual);
                duplicarFormulaConcepto.setTipo("C");
                listFormulasConceptosCrear.add(duplicarFormulaConcepto);
                listFormulasConceptosFormula.add(duplicarFormulaConcepto);

                duplicarFormulaConcepto = new FormulasConceptos();
                duplicarFormulaConcepto.setConcepto(new Conceptos());
                duplicarFormulaConcepto.getConcepto().setEmpresa(new Empresas());

                RequestContext context = RequestContext.getCurrentInstance();

                infoRegistro = "Cantidad de registros : " + listFormulasConceptosFormula.size();

                context.update("form:informacionRegistro");
                context.update("form:datosFormulaConcepto");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                context.execute("DuplicarRegistroFormula.hide()");
                cambiosFormulasConceptos = true;
                indexFormulasConceptos = -1;
                secRegistroFormulasConceptos = null;

            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("errorFechasFC.show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorRegNuevo.show()");
        }
    }

    public void limpiarDuplicarFormula() {
        duplicarFormulaConcepto = new FormulasConceptos();
        duplicarFormulaConcepto.setConcepto(new Conceptos());
        duplicarFormulaConcepto.getConcepto().setEmpresa(new Empresas());
        indexFormulasConceptos = -1;
        secRegistroFormulasConceptos = null;
    }

    ///////////////////////////////////////////////////////////////
    public void borrarFormula() {

        if (indexFormulasConceptos >= 0) {
            if (tipoListaFormulasConceptos == 0) {
                if (!listFormulasConceptosModificar.isEmpty() && listFormulasConceptosModificar.contains(listFormulasConceptosFormula.get(indexFormulasConceptos))) {
                    int modIndex = listFormulasConceptosModificar.indexOf(listFormulasConceptosFormula.get(indexFormulasConceptos));
                    listFormulasConceptosModificar.remove(modIndex);
                    listFormulasConceptosBorrar.add(listFormulasConceptosFormula.get(indexFormulasConceptos));
                } else if (!listFormulasConceptosCrear.isEmpty() && listFormulasConceptosCrear.contains(listFormulasConceptosFormula.get(indexFormulasConceptos))) {
                    int crearIndex = listFormulasConceptosCrear.indexOf(listFormulasConceptosFormula.get(indexFormulasConceptos));
                    listFormulasConceptosCrear.remove(crearIndex);
                } else {
                    listFormulasConceptosBorrar.add(listFormulasConceptosFormula.get(indexFormulasConceptos));
                }
                listFormulasConceptosFormula.remove(indexFormulasConceptos);
            }
            if (tipoListaFormulasConceptos == 1) {
                if (!listFormulasConceptosModificar.isEmpty() && listFormulasConceptosModificar.contains(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos))) {
                    int modIndex = listFormulasConceptosModificar.indexOf(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos));
                    listFormulasConceptosModificar.remove(modIndex);
                    listFormulasConceptosBorrar.add(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos));
                } else if (!listFormulasConceptosCrear.isEmpty() && listFormulasConceptosCrear.contains(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos))) {
                    int crearIndex = listFormulasConceptosCrear.indexOf(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos));
                    listFormulasConceptosCrear.remove(crearIndex);
                } else {
                    listFormulasConceptosBorrar.add(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos));
                }
                int VLIndex = listFormulasConceptosFormula.indexOf(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos));
                listFormulasConceptosFormula.remove(VLIndex);
                filtrarListFormulasConceptosFormula.remove(indexFormulasConceptos);
            }
            RequestContext context = RequestContext.getCurrentInstance();

            infoRegistro = "Cantidad de registros : " + listFormulasConceptosFormula.size();

            context.update("form:informacionRegistro");
            context.update("form:datosFormulaConcepto");
            indexFormulasConceptos = -1;
            secRegistroFormulasConceptos = null;
            cambiosFormulasConceptos = true;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
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
     */
    public void filtradoFormula() {
        if (banderaFormulasConceptos == 0) {
            altoTabla = "288";
            formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaInicial");
            formulaFechaInicial.setFilterStyle("width: 60px");
            formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaFinal");
            formulaFechaFinal.setFilterStyle("width: 60px");
            formulaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaCodigo");
            formulaCodigo.setFilterStyle("width: 80px");
            formulaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaDescripcion");
            formulaDescripcion.setFilterStyle("width: 80px");
            formulaOrden = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaOrden");
            formulaOrden.setFilterStyle("width: 80px");
            formulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaEmpresa");
            formulaEmpresa.setFilterStyle("width: 80px");
            formulaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaNIT");
            formulaNIT.setFilterStyle("width: 80px");

            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
            banderaFormulasConceptos = 1;
        } else if (banderaFormulasConceptos == 1) {
            altoTabla = "310";
            formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaInicial");
            formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaFinal");
            formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            formulaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaCodigo");
            formulaCodigo.setFilterStyle("display: none; visibility: hidden;");
            formulaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaDescripcion");
            formulaDescripcion.setFilterStyle("display: none; visibility: hidden;");
            formulaOrden = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaOrden");
            formulaOrden.setFilterStyle("display: none; visibility: hidden;");
            formulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaEmpresa");
            formulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
            formulaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaNIT");
            formulaNIT.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
            banderaFormulasConceptos = 0;
            filtrarListFormulasConceptosFormula = null;
            tipoListaFormulasConceptos = 0;
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (banderaFormulasConceptos == 1) {
            altoTabla = "310";
            formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaInicial");
            formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaFinal");
            formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            formulaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaCodigo");
            formulaCodigo.setFilterStyle("display: none; visibility: hidden;");
            formulaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaDescripcion");
            formulaDescripcion.setFilterStyle("display: none; visibility: hidden;");
            formulaOrden = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaOrden");
            formulaOrden.setFilterStyle("display: none; visibility: hidden;");
            formulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaEmpresa");
            formulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
            formulaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaNIT");
            formulaNIT.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
            banderaFormulasConceptos = 0;
            filtrarListFormulasConceptosFormula = null;
            tipoListaFormulasConceptos = 0;
        }

        listFormulasConceptosBorrar.clear();
        listFormulasConceptosCrear.clear();
        listFormulasConceptosModificar.clear();
        indexFormulasConceptos = -1;
        secRegistroFormulasConceptos = null;
        formulaActual = null;
        k = 0;
        listFormulasConceptosFormula = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        cambiosFormulasConceptos = false;
        nuevaFormulaConcepto = new FormulasConceptos();
        duplicarFormulaConcepto = new FormulasConceptos();
        listConceptos = null;
        listFormulasConceptos = null;
    }

    public void actualizarOrden() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaFormulasConceptos == 0) {
                listFormulasConceptosFormula.get(indexFormulasConceptos).setStrOrden(formulaSeleccionado.getStrOrden());
                if (!listFormulasConceptosCrear.contains(listFormulasConceptosFormula.get(indexFormulasConceptos))) {
                    if (listFormulasConceptosModificar.isEmpty()) {
                        listFormulasConceptosModificar.add(listFormulasConceptosFormula.get(indexFormulasConceptos));
                    } else if (!listFormulasConceptosModificar.contains(listFormulasConceptosFormula.get(indexFormulasConceptos))) {
                        listFormulasConceptosModificar.add(listFormulasConceptosFormula.get(indexFormulasConceptos));
                    }
                }
            } else {
                filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).setStrOrden(formulaSeleccionado.getStrOrden());
                if (!listFormulasConceptosCrear.contains(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos))) {
                    if (listFormulasConceptosModificar.isEmpty()) {
                        listFormulasConceptosModificar.add(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos));
                    } else if (!listFormulasConceptosModificar.contains(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos))) {
                        listFormulasConceptosModificar.add(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexFormulasConceptos = true;
            cambiosFormulasConceptos = true;
            context.update("form:datosFormulaConcepto");
        } else if (tipoActualizacion == 1) {
            nuevaFormulaConcepto.setStrOrden(formulaSeleccionado.getStrOrden());
            context.update("formularioDialogos:nuevaOrden");
        } else if (tipoActualizacion == 2) {
            duplicarFormulaConcepto.setStrOrden(formulaSeleccionado.getStrOrden());
            context.update("formularioDialogos:duplicarOrden");
        }
        filtrarListFormulasConceptos = null;
        formulaSeleccionado = null;
        aceptar = true;
        indexFormulasConceptos = -1;
        secRegistroFormulasConceptos = null;
        tipoActualizacion = -1;
        /*
         context.update("form:OrdenDialogo");
         context.update("form:lovOrden");
         context.update("form:aceptarO");*/
        context.reset("form:lovOrden:globalFilter");
        context.execute("lovOrden.clearFilters()");
        context.execute("OrdenDialogo.hide()");
    }

    public void cancelarCambioOrden() {
        filtrarListFormulasConceptos = null;
        formulaSeleccionado = null;
        aceptar = true;
        indexFormulasConceptos = -1;
        secRegistroFormulasConceptos = null;
        tipoActualizacion = -1;
        permitirIndexFormulasConceptos = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovOrden:globalFilter");
        context.execute("lovOrden.clearFilters()");
        context.execute("OrdenDialogo.hide()");
    }

    public void actualizarConcepto() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaFormulasConceptos == 0) {
                listFormulasConceptosFormula.get(indexFormulasConceptos).setConcepto(conceptoSeleccionada);
                if (!listFormulasConceptosCrear.contains(listFormulasConceptosFormula.get(indexFormulasConceptos))) {
                    if (listFormulasConceptosModificar.isEmpty()) {
                        listFormulasConceptosModificar.add(listFormulasConceptosFormula.get(indexFormulasConceptos));
                    } else if (!listFormulasConceptosModificar.contains(listFormulasConceptosFormula.get(indexFormulasConceptos))) {
                        listFormulasConceptosModificar.add(listFormulasConceptosFormula.get(indexFormulasConceptos));
                    }
                }
            } else {
                filtrarListFormulasConceptosFormula.get(indexFormulasConceptos).setConcepto(conceptoSeleccionada);
                if (!listFormulasConceptosCrear.contains(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos))) {
                    if (listFormulasConceptosModificar.isEmpty()) {
                        listFormulasConceptosModificar.add(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos));
                    } else if (!listFormulasConceptosModificar.contains(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos))) {
                        listFormulasConceptosModificar.add(filtrarListFormulasConceptosFormula.get(indexFormulasConceptos));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexFormulasConceptos = true;
            cambiosFormulasConceptos = true;
            context.update("form:datosFormulaConcepto");
        } else if (tipoActualizacion == 1) {
            nuevaFormulaConcepto.setConcepto(conceptoSeleccionada);
            context.update("formularioDialogos:nuevaCodigo");
            context.update("formularioDialogos:nuevaConcepto");
            context.update("formularioDialogos:nuevaEmpresa");
            context.update("formularioDialogos:nuevaNIT");
        } else if (tipoActualizacion == 2) {
            duplicarFormulaConcepto.setConcepto(conceptoSeleccionada);
            context.update("formularioDialogos:duplicarCodigo");
            context.update("formularioDialogos:duplicarConcepto");
            context.update("formularioDialogos:duplicarEmpresa");
            context.update("formularioDialogos:duplicarNIT");
        }
        filtrarListConceptos = null;
        conceptoSeleccionada = null;
        aceptar = true;
        indexFormulasConceptos = -1;
        secRegistroFormulasConceptos = null;
        tipoActualizacion = -1;
        /*
         context.update("form:ConceptoDialogo");
         context.update("form:lovConcepto");
         context.update("form:aceptarC");*/
        context.reset("form:lovConcepto:globalFilter");
        context.execute("lovConcepto.clearFilters()");
        context.execute("ConceptoDialogo.hide()");
    }

    public void cancelarCambioConcepto() {
        filtrarListConceptos = null;
        conceptoSeleccionada = null;
        aceptar = true;
        indexFormulasConceptos = -1;
        secRegistroFormulasConceptos = null;
        tipoActualizacion = -1;
        permitirIndexFormulasConceptos = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovConcepto:globalFilter");
        context.execute("lovConcepto.clearFilters()");
        context.execute("ConceptoDialogo.hide()");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    /**
     *
     * @return Nombre del dialogo a exportar en XML
     */
    public String exportXML() {
        nombreTabla = ":formExportarFormula:datosFormulaConceptoExportar";
        nombreXML = "FormulaConcepto_XML";
        return nombreTabla;
    }

    /**
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        nombreTabla = ":formExportarFormula:datosFormulaConceptoExportar";
        nombreExportar = "FormulaConcepto_PDF";
        exportPDF_Tabla();
        indexFormulasConceptos = -1;
        secRegistroFormulasConceptos = null;

    }

    /**
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDF_Tabla() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(nombreTabla);
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, nombreExportar, false, false, "UTF-8", null, null);
        context.responseComplete();

    }

    /**
     * Verifica que tabla exportar XLS con respecto al index activo
     *
     * @throws IOException
     */
    public void verificarExportXLS() throws IOException {
        nombreTabla = ":formExportarFormula:datosFormulaConceptoExportar";
        nombreExportar = "FormulaConcepto_XLS";
        exportXLS_Tabla();
        indexFormulasConceptos = -1;
        secRegistroFormulasConceptos = null;
    }

    /**
     * Metodo que exporta datos a XLS Vigencia Sueldos
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLS_Tabla() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(nombreTabla);
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, nombreExportar, false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    //EVENTO FILTRAR
    /**
     * Evento que cambia la lista real a la filtrada
     */
    public void eventoFiltrar() {
        if (tipoListaFormulasConceptos == 0) {
            tipoListaFormulasConceptos = 1;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        infoRegistro = "Cantidad de registros : " + filtrarListFormulasConceptosFormula.size();
        context.update("form:informacionRegistro");

    }

    public void verificarRastroTabla() {
        verificarRastroFormulaConcepto();
        indexFormulasConceptos = -1;

    }

    public void verificarRastroFormulaConcepto() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listFormulasConceptosFormula != null) {
            if (secRegistroFormulasConceptos != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroFormulasConceptos, "FORMULASCONCEPTOS");
                backUpSecRegistroFormulasConceptos = secRegistroFormulasConceptos;
                backUp = secRegistroFormulasConceptos;
                secRegistroFormulasConceptos = null;
                if (resultado == 1) {
                    context.execute("errorObjetosDB.show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "FormulasConceptos";
                    msnConfirmarRastro = "La tabla FORMULASCONCEPTOS tiene rastros para el registro seleccionado, ¿desea continuar?";
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
            if (administrarRastros.verificarHistoricosTabla("FORMULASCONCEPTOS")) {
                nombreTablaRastro = "FormulasConceptos";
                msnConfirmarRastroHistorico = "La tabla FORMULASCONCEPTOS tiene rastros historicos, ¿Desea continuar?";
                context.update("form:confirmarRastroHistorico");
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }
        }
        indexFormulasConceptos = -1;
    }

    public void limpiarMSNRastros() {
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";
    }

    //GET - SET 
    public List<FormulasConceptos> getListFormulasConceptosFormula() {
        try {
            if (listFormulasConceptosFormula == null) {
                if (formulaActual.getSecuencia() != null) {
                    listFormulasConceptosFormula = administrarFormulaConcepto.formulasConceptosParaFormula(formulaActual.getSecuencia());
                }
            }
            return listFormulasConceptosFormula;
        } catch (Exception e) {
            System.out.println("Error getListFormulasConceptosFormula " + e.toString());
            return null;
        }
    }

    public void setListFormulasConceptosFormula(List<FormulasConceptos> t) {
        this.listFormulasConceptosFormula = t;
    }

    public List<FormulasConceptos> getFiltrarListFormulasConceptosFormula() {
        return filtrarListFormulasConceptosFormula;
    }

    public void setFiltrarListFormulasConceptosFormula(List<FormulasConceptos> t) {
        this.filtrarListFormulasConceptosFormula = t;
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

    public void setSecRegistroFormulasConceptos(BigInteger secRegistro) {
        this.secRegistroFormulasConceptos = secRegistro;
    }

    public BigInteger getBackUpSecRegistroFormulasConceptos() {
        return backUpSecRegistroFormulasConceptos;
    }

    public void setBackUpSecRegistroFormulasConceptos(BigInteger b) {
        this.backUpSecRegistroFormulasConceptos = b;
    }

    public List<FormulasConceptos> getListFormulasConceptosModificar() {
        return listFormulasConceptosModificar;
    }

    public void setListFormulasConceptosModificar(List<FormulasConceptos> setListFormulasConceptosModificar) {
        this.listFormulasConceptosModificar = setListFormulasConceptosModificar;
    }

    public FormulasConceptos getNuevaFormulaConcepto() {
        return nuevaFormulaConcepto;
    }

    public void setNuevaFormulaConcepto(FormulasConceptos setNuevaFormulaConcepto) {
        this.nuevaFormulaConcepto = setNuevaFormulaConcepto;
    }

    public List<FormulasConceptos> getListFormulasConceptosCrear() {
        return listFormulasConceptosCrear;
    }

    public void setListFormulasConceptosCrear(List<FormulasConceptos> setListFormulasConceptosCrear) {
        this.listFormulasConceptosCrear = setListFormulasConceptosCrear;
    }

    public List<FormulasConceptos> getListFormulasConceptosBorrar() {
        return listFormulasConceptosBorrar;
    }

    public void setListFormulasConceptosBorrar(List<FormulasConceptos> setListFormulasConceptosBorrar) {
        this.listFormulasConceptosBorrar = setListFormulasConceptosBorrar;
    }

    public FormulasConceptos getEditarFormulaConcepto() {
        return editarFormulaConcepto;
    }

    public void setEditarFormulaConcepto(FormulasConceptos setEditarFormulaConcepto) {
        this.editarFormulaConcepto = setEditarFormulaConcepto;
    }

    public FormulasConceptos getDuplicarFormulaConcepto() {
        return duplicarFormulaConcepto;
    }

    public void setDuplicarFormulaConcepto(FormulasConceptos setDuplicarFormulaConcepto) {
        this.duplicarFormulaConcepto = setDuplicarFormulaConcepto;
    }

    public BigInteger getSecRegistroFormulasConceptos() {
        return secRegistroFormulasConceptos;
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

    public Formulas getFormulaActual() {
        return formulaActual;
    }

    public void setFormulaActual(Formulas setFormulaActual) {
        this.formulaActual = setFormulaActual;
    }

    public List<FormulasConceptos> getListFormulasConceptos() {
        listFormulasConceptos = administrarFormulaConcepto.listFormulasConceptos();

        return listFormulasConceptos;
    }

    public void setListFormulasConceptos(List<FormulasConceptos> setListFormulasConceptos) {
        this.listFormulasConceptos = setListFormulasConceptos;
    }

    public List<FormulasConceptos> getFiltrarListFormulasConceptos() {
        return filtrarListFormulasConceptos;
    }

    public void setFiltrarListFormulasConceptos(List<FormulasConceptos> setFiltrarListFormulasConceptos) {
        this.filtrarListFormulasConceptos = setFiltrarListFormulasConceptos;
    }

    public FormulasConceptos getFormulaSeleccionado() {
        return formulaSeleccionado;
    }

    public void setFormulaSeleccionado(FormulasConceptos setFormulaSeleccionado) {
        this.formulaSeleccionado = setFormulaSeleccionado;
    }

    public List<Conceptos> getListConceptos() {
        listConceptos = administrarFormulaConcepto.listConceptos();

        return listConceptos;
    }

    public void setListConceptos(List<Conceptos> setListConceptos) {
        this.listConceptos = setListConceptos;
    }

    public List<Conceptos> getFiltrarListConceptos() {
        return filtrarListConceptos;
    }

    public void setFiltrarListConceptos(List<Conceptos> setFiltrarListConceptos) {
        this.filtrarListConceptos = setFiltrarListConceptos;
    }

    public Conceptos getConceptoSeleccionada() {
        return conceptoSeleccionada;
    }

    public void setConceptoSeleccionada(Conceptos setConceptoSeleccionada) {
        this.conceptoSeleccionada = setConceptoSeleccionada;
    }

    public FormulasConceptos getFormulaTablaSeleccionada() {
        getListFormulasConceptos();
        if (listFormulasConceptosFormula != null) {
            int tam = listFormulasConceptosFormula.size();
            if (tam > 0) {
                formulaTablaSeleccionada = listFormulasConceptosFormula.get(0);
            }
        }
        return formulaTablaSeleccionada;
    }

    public void setFormulaTablaSeleccionada(FormulasConceptos formulaTablaSeleccionada) {
        this.formulaTablaSeleccionada = formulaTablaSeleccionada;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistroConcepto() {
        getListConceptos();
        if (listConceptos != null) {
            infoRegistroConcepto = "Cantidad de registros : " + listConceptos.size();
        } else {
            infoRegistroConcepto = "Cantidad de registros : 0";
        }
        return infoRegistroConcepto;
    }

    public void setInfoRegistroConcepto(String infoRegistroConcepto) {
        this.infoRegistroConcepto = infoRegistroConcepto;
    }

    public String getInfoRegistroOrden() {
        getListFormulasConceptos();
        if (listFormulasConceptos != null) {
            infoRegistroOrden = "Cantidad de registros : " + listFormulasConceptos.size();
        } else {
            infoRegistroOrden = "Cantidad de registros : 0";
        }
        return infoRegistroOrden;
    }

    public void setInfoRegistroOrden(String infoRegistroOrden) {
        this.infoRegistroOrden = infoRegistroOrden;
    }

}
