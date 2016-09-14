/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.Formulas;
import Entidades.Operandos;
import Entidades.TiposFormulas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposFormulasInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class ControlTipoFormula implements Serializable {

    @EJB
    AdministrarTiposFormulasInterface administrarTiposFormulas;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //Parametros que llegan
    private BigInteger secOperando;
    private String tOperando;
    private Operandos operando;
    //LISTA INFOREPORTES
    private List<TiposFormulas> listaTiposFormulas;
    private List<TiposFormulas> filtradosListaTiposFormulas;
    private TiposFormulas tipoFormulaSeleccionado;
    //L.O.V INFOREPORTES
    private List<TiposFormulas> lovlistaTiposFormulas;
    private List<TiposFormulas> lovfiltradoslistaTiposFormulas;
    private TiposFormulas operandosSeleccionado;
    //editar celda
    private TiposFormulas editarTiposFormulas;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //OTROS
    private boolean aceptar;
    private int index;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //RASTROS
    private BigInteger secRegistro;
    private boolean guardado, guardarOk;
    //Crear Novedades
    private List<TiposFormulas> listaTiposFormulasCrear;
    public TiposFormulas nuevoTipoFormula;
    public TiposFormulas duplicarTipoFormula;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<TiposFormulas> listaTiposFormulasModificar;
    //Borrar Novedades
    private List<TiposFormulas> listaTiposFormulasBorrar;
    //AUTOCOMPLETAR
    private String Formula;
    //Columnas Tabla Ciudades
    private Column tiposFormulasIniciales, tiposFormulasFinales, tiposFormulasObjetos;
    //ALTO SCROLL TABLA
    private String altoTabla;
    private boolean cambiosPagina;
    //L.O.V FORMULAS
    private List<Formulas> lovListaFormulas;
    private List<Formulas> lovFiltradosListaFormulas;
    private Formulas seleccionFormulas;
    //Enviar a Formulas
    private TiposFormulas tiposFormulasRegistro;
    private BigInteger secuenciaTiposFormulas;

    public ControlTipoFormula() {
        cambiosPagina = true;
        nuevoTipoFormula = new TiposFormulas();
        nuevoTipoFormula.setFechainicial(new Date());
        permitirIndex = true;
        lovListaFormulas = null;
        permitirIndex = true;
        aceptar = true;
        secRegistro = null;
        guardado = true;
        tipoLista = 0;
        listaTiposFormulasBorrar = new ArrayList<TiposFormulas>();
        listaTiposFormulasCrear = new ArrayList<TiposFormulas>();
        listaTiposFormulasModificar = new ArrayList<TiposFormulas>();
        altoTabla = "270";
        duplicarTipoFormula = new TiposFormulas();
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposFormulas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            tiposFormulasRegistro = listaTiposFormulas.get(index);
            if (tipoLista == 0) {
                secRegistro = listaTiposFormulas.get(index).getSecuencia();
            } else {
                secRegistro = filtradosListaTiposFormulas.get(index).getSecuencia();
            }
        }
    }

    public void recibirDatosOperando(BigInteger secuenciaOperando, String tipoOperando, Operandos operandoRegistro) {
        secOperando = secuenciaOperando;
        tOperando = tipoOperando;
        operando = operandoRegistro;
        listaTiposFormulas = null;
        getListaTiposFormulas();
    }

    //AUTOCOMPLETAR
    public void modificarTiposFormulas(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaTiposFormulasCrear.contains(listaTiposFormulas.get(index))) {

                    if (listaTiposFormulasModificar.isEmpty()) {
                        listaTiposFormulasModificar.add(listaTiposFormulas.get(index));
                    } else if (!listaTiposFormulasModificar.contains(listaTiposFormulas.get(index))) {
                        listaTiposFormulasModificar.add(listaTiposFormulas.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaTiposFormulasCrear.contains(filtradosListaTiposFormulas.get(index))) {

                    if (listaTiposFormulasCrear.isEmpty()) {
                        listaTiposFormulasCrear.add(filtradosListaTiposFormulas.get(index));
                    } else if (!listaTiposFormulasCrear.contains(filtradosListaTiposFormulas.get(index))) {
                        listaTiposFormulasCrear.add(filtradosListaTiposFormulas.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
        } else if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            if (tipoLista == 0) {
                listaTiposFormulas.get(indice).getFormula().setNombrecorto(Formula);
            } else {
                filtradosListaTiposFormulas.get(indice).getFormula().setNombrecorto(Formula);
            }

            for (int i = 0; i < listaTiposFormulas.size(); i++) {
                if (listaTiposFormulas.get(i).getFormula().getNombrecorto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaTiposFormulas.get(indice).setFormula(lovListaFormulas.get(indiceUnicoElemento));
                } else {
                    filtradosListaTiposFormulas.get(indice).setFormula(lovListaFormulas.get(indiceUnicoElemento));
                }
                lovListaFormulas.clear();
                getLovListaFormulas();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:formulasDialogo");
                PrimefacesContextUI.ejecutar("PF('formulasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void asignarIndex(Integer indice, int dlg, int LND) {

        index = indice;
        RequestContext context = RequestContext.getCurrentInstance();

        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            index = -1;
            secRegistro = null;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            index = -1;
            secRegistro = null;
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:formulasDialogo");
            PrimefacesContextUI.ejecutar("PF('formulasDialogo').show()");
        }

    }

    public void guardarVariables(BigInteger secuencia) {
        if (index < 0) {
            System.out.println("INDEX " + index);
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
        if (listaTiposFormulasCrear.isEmpty() && listaTiposFormulasBorrar.isEmpty() && listaTiposFormulasModificar.isEmpty()) {
            if (tiposFormulasRegistro != null) {
                secuenciaTiposFormulas = tiposFormulasRegistro.getSecuencia();

                System.out.println("secuenciaOperando" + secuenciaTiposFormulas + "operandoRegistro" + tiposFormulasRegistro);
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('dirigirFormula()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("form:formulasDialogo");
                PrimefacesContextUI.ejecutar("PF('formulasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("FORMULA")) {
            if (tipoNuevo == 1) {
                Formula = nuevoTipoFormula.getFormula().getNombrecorto();
            } else if (tipoNuevo == 2) {
                Formula = duplicarTipoFormula.getFormula().getNombrecorto();
            }
        }

    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            if (tipoNuevo == 1) {
                nuevoTipoFormula.getFormula().setNombrecorto(Formula);
            } else if (tipoNuevo == 2) {
                duplicarTipoFormula.getFormula().setNombrecorto(Formula);
            }
            for (int i = 0; i < lovListaFormulas.size(); i++) {
                if (lovListaFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoTipoFormula.setFormula(lovListaFormulas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEstado");
                } else if (tipoNuevo == 2) {
                    duplicarTipoFormula.setFormula(lovListaFormulas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstado");
                }
                lovListaFormulas.clear();
                getLovListaFormulas();
            } else {
                RequestContext.getCurrentInstance().update("form:formulasDialogo");
                PrimefacesContextUI.ejecutar("PF('formulasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEstado");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstado");
                }
            }
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarTipoFormula.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoFormula");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoTipoFormula').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                altoTabla = "270";
                tiposFormulasIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasIniciales");
                tiposFormulasIniciales.setFilterStyle("display: none; visibility: hidden;");
                tiposFormulasFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasFinales");
                tiposFormulasFinales.setFilterStyle("display: none; visibility: hidden;");
                tiposFormulasObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasObjetos");
                tiposFormulasObjetos.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
                bandera = 0;
                filtradosListaTiposFormulas = null;
                tipoLista = 0;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            //Falta Ponerle el Operando al cual se agregará
            duplicarTipoFormula.setOperando(operando);
            listaTiposFormulas.add(duplicarTipoFormula);
            listaTiposFormulasCrear.add(duplicarTipoFormula);

            index = -1;
            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
            duplicarTipoFormula = new TiposFormulas();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarTipoFormula");
            PrimefacesContextUI.ejecutar("PF('DuplicarTipoFormula').hide()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            altoTabla = "250";
            tiposFormulasIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasIniciales");
            tiposFormulasIniciales.setFilterStyle("width: 85% !important;");
            tiposFormulasFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasFinales");
            tiposFormulasFinales.setFilterStyle("width: 85% !important;");
            tiposFormulasObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasObjetos");
            tiposFormulasObjetos.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altoTabla = "270";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tiposFormulasIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasIniciales");
            tiposFormulasIniciales.setFilterStyle("display: none; visibility: hidden;");
            tiposFormulasFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasFinales");
            tiposFormulasFinales.setFilterStyle("display: none; visibility: hidden;");
            tiposFormulasObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasObjetos");
            tiposFormulasObjetos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
            bandera = 0;
            filtradosListaTiposFormulas = null;
            tipoLista = 0;
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            altoTabla = "270";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tiposFormulasIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasIniciales");
            tiposFormulasIniciales.setFilterStyle("display: none; visibility: hidden;");
            tiposFormulasFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasFinales");
            tiposFormulasFinales.setFilterStyle("display: none; visibility: hidden;");
            tiposFormulasObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasObjetos");
            tiposFormulasObjetos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
            bandera = 0;
            filtradosListaTiposFormulas = null;
            tipoLista = 0;
        }

        listaTiposFormulasBorrar.clear();
        listaTiposFormulasCrear.clear();
        listaTiposFormulasModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaTiposFormulas = null;
        guardado = true;
        permitirIndex = true;
        cambiosPagina = true;

        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTiposFormulas = listaTiposFormulas.get(index);
            }
            if (tipoLista == 1) {
                editarTiposFormulas = filtradosListaTiposFormulas.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasIniciales");
                PrimefacesContextUI.ejecutar("PF('editarFechasIniciales').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasFinales");
                PrimefacesContextUI.ejecutar("PF('editarFechasFinales').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulas");
                PrimefacesContextUI.ejecutar("PF('editarFormulas').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEstados");
                PrimefacesContextUI.ejecutar("PF('editarEstados').show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposFormulasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TiposFormulasPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposFormulasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TiposFormulasXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO REGISTRO CIUDAD
    public void limpiarNuevoTiposFormulas() {
        nuevoTipoFormula = new TiposFormulas();
        index = -1;
        secRegistro = null;
    }

    public void limpiarduplicarTiposFormulas() {
        duplicarTipoFormula = new TiposFormulas();
        index = -1;
        secRegistro = null;
    }

    //DUPLICAR Operando
    public void duplicarTF() {
        if (index >= 0) {
            duplicarTipoFormula = new TiposFormulas();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTipoFormula.setSecuencia(l);
                duplicarTipoFormula.setFormula(listaTiposFormulas.get(index).getFormula());
                duplicarTipoFormula.setFechainicial(listaTiposFormulas.get(index).getFechainicial());
                duplicarTipoFormula.setFechafinal(listaTiposFormulas.get(index).getFechafinal());
                duplicarTipoFormula.setOperando(listaTiposFormulas.get(index).getOperando());
            }
            if (tipoLista == 1) {
                duplicarTipoFormula.setSecuencia(l);
                duplicarTipoFormula.setFormula(filtradosListaTiposFormulas.get(index).getFormula());
                duplicarTipoFormula.setFechainicial(filtradosListaTiposFormulas.get(index).getFechainicial());
                duplicarTipoFormula.setFechafinal(filtradosListaTiposFormulas.get(index).getFechafinal());
                duplicarTipoFormula.setOperando(filtradosListaTiposFormulas.get(index).getOperando());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoFormula");
            PrimefacesContextUI.ejecutar("PF('DuplicarTipoFormula').show()");
            index = -1;
            secRegistro = null;
        }
    }

    //GUARDAR
    public void guardarCambiosTiposFormulas() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones Novedades");

            if (!listaTiposFormulasBorrar.isEmpty()) {
                for (int i = 0; i < listaTiposFormulasBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaTiposFormulasBorrar.size());
                    administrarTiposFormulas.borrarTiposFormulas(listaTiposFormulasBorrar.get(i));
                }
                System.out.println("Entra");
                listaTiposFormulasBorrar.clear();
            }

            if (!listaTiposFormulasCrear.isEmpty()) {
                for (int i = 0; i < listaTiposFormulasCrear.size(); i++) {
                    System.out.println("Creando...");
                    administrarTiposFormulas.crearTiposFormulas(listaTiposFormulasCrear.get(i));
                }
                System.out.println("LimpiaLista");
                listaTiposFormulasCrear.clear();
            }
            if (!listaTiposFormulasModificar.isEmpty()) {
                for (int i = 0; i < listaTiposFormulasModificar.size(); i++) {
                    administrarTiposFormulas.modificarTiposFormulas(listaTiposFormulasCrear.get(i));
                }
                listaTiposFormulasModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaTiposFormulas = null;

            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:aceptar");
            //  k = 0;
        }
        index = -1;
        secRegistro = null;
    }

//RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaTiposFormulas.isEmpty()) {
            if (secRegistro != null) {
                int result = administrarRastros.obtenerTabla(secRegistro, "TIPOSFUNCIONES");
                System.out.println("resultado: " + result);
                if (result == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                } else if (result == 2) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
                } else if (result == 3) {
                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                } else if (result == 4) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
                } else if (result == 5) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("TIPOSFUNCIONES")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    public void actualizarFormulas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaTiposFormulas.get(index).setFormula(seleccionFormulas);
                if (!listaTiposFormulasCrear.contains(listaTiposFormulas.get(index))) {
                    if (listaTiposFormulasModificar.isEmpty()) {
                        listaTiposFormulasModificar.add(listaTiposFormulas.get(index));
                    } else if (!listaTiposFormulasModificar.contains(listaTiposFormulas.get(index))) {
                        listaTiposFormulasModificar.add(listaTiposFormulas.get(index));
                    }
                }
            } else {
                filtradosListaTiposFormulas.get(index).setFormula(seleccionFormulas);
                if (!listaTiposFormulasCrear.contains(filtradosListaTiposFormulas.get(index))) {
                    if (listaTiposFormulasModificar.isEmpty()) {
                        listaTiposFormulasModificar.add(filtradosListaTiposFormulas.get(index));
                    } else if (!listaTiposFormulasModificar.contains(filtradosListaTiposFormulas.get(index))) {
                        listaTiposFormulasModificar.add(filtradosListaTiposFormulas.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            cambiosPagina = false;

            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
        } else if (tipoActualizacion == 1) {
            nuevoTipoFormula.setFormula(seleccionFormulas);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoFormula");
        } else if (tipoActualizacion == 2) {
            duplicarTipoFormula.setFormula(seleccionFormulas);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoFormula");

        }
        filtradosListaTiposFormulas = null;
        seleccionFormulas = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVFormulas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVFormulas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('formulasDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVFormulas");
    }

    public void cancelarCambioFormulas() {
        lovFiltradosListaFormulas = null;
        seleccionFormulas = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVFormulas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVFormulas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('formulasDialogo').hide()");
    }

    public void agregarNuevoTipoFormula() {
        int pasa = 0;
        int pasa2 = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoTipoFormula.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }
        if (nuevoTipoFormula.getFechafinal() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Final\n";
            pasa++;
        }
        if (nuevoTipoFormula.getFormula().getNombrecorto() == null) {
            mensajeValidacion = mensajeValidacion + " * Nombre Corto\n";
            pasa++;
        }

        if (nuevoTipoFormula.getFechainicial() != null && nuevoTipoFormula.getFechafinal() != null) {
            if (nuevoTipoFormula.getFechafinal().before(nuevoTipoFormula.getFechainicial())) {
                RequestContext.getCurrentInstance().update("formularioDialogos:errorFechas");
                PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
                pasa2++;
            }
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoFormula");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoTipoFormula').show()");
        }

        if (pasa == 0 && pasa2 == 0) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();

                altoTabla = "270";
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                tiposFormulasIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasIniciales");
                tiposFormulasIniciales.setFilterStyle("display: none; visibility: hidden;");
                tiposFormulasFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasFinales");
                tiposFormulasFinales.setFilterStyle("display: none; visibility: hidden;");
                tiposFormulasObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasObjetos");
                tiposFormulasObjetos.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
                bandera = 0;
                filtradosListaTiposFormulas = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoFormula.setSecuencia(l);
            System.out.println("Operando: " + operando);
            nuevoTipoFormula.setOperando(operando);

            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaTiposFormulasCrear.add(nuevoTipoFormula);
            listaTiposFormulas.add(nuevoTipoFormula);
            nuevoTipoFormula = new TiposFormulas();
            RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:aceptar");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoTipoFormula').hide()");
            index = -1;
            secRegistro = null;
        }
    }

    //BORRAR CIUDADES
    public void borrarTipoFormula() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listaTiposFormulasModificar.isEmpty() && listaTiposFormulasModificar.contains(listaTiposFormulas.get(index))) {
                    int modIndex = listaTiposFormulasModificar.indexOf(listaTiposFormulas.get(index));
                    listaTiposFormulasModificar.remove(modIndex);
                    listaTiposFormulasBorrar.add(listaTiposFormulas.get(index));
                } else if (!listaTiposFormulasCrear.isEmpty() && listaTiposFormulasCrear.contains(listaTiposFormulas.get(index))) {
                    int crearIndex = listaTiposFormulasCrear.indexOf(listaTiposFormulas.get(index));
                    listaTiposFormulasCrear.remove(crearIndex);
                } else {
                    listaTiposFormulasBorrar.add(listaTiposFormulas.get(index));
                }
                listaTiposFormulas.remove(index);
            }

            if (tipoLista == 1) {
                if (!listaTiposFormulasModificar.isEmpty() && listaTiposFormulasModificar.contains(filtradosListaTiposFormulas.get(index))) {
                    int modIndex = listaTiposFormulasModificar.indexOf(filtradosListaTiposFormulas.get(index));
                    listaTiposFormulasModificar.remove(modIndex);
                    listaTiposFormulasBorrar.add(filtradosListaTiposFormulas.get(index));
                } else if (!listaTiposFormulasCrear.isEmpty() && listaTiposFormulasCrear.contains(filtradosListaTiposFormulas.get(index))) {
                    int crearIndex = listaTiposFormulasCrear.indexOf(filtradosListaTiposFormulas.get(index));
                    listaTiposFormulasCrear.remove(crearIndex);
                } else {
                    listaTiposFormulasBorrar.add(filtradosListaTiposFormulas.get(index));
                }
                int CIndex = listaTiposFormulas.indexOf(filtradosListaTiposFormulas.get(index));
                listaTiposFormulas.remove(CIndex);
                filtradosListaTiposFormulas.remove(index);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:aceptar");
            }
        }
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            altoTabla = "270";
            FacesContext c = FacesContext.getCurrentInstance();

            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tiposFormulasIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasIniciales");
            tiposFormulasIniciales.setFilterStyle("display: none; visibility: hidden;");
            tiposFormulasFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasFinales");
            tiposFormulasFinales.setFilterStyle("display: none; visibility: hidden;");
            tiposFormulasObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasObjetos");
            tiposFormulasObjetos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
            bandera = 0;
            filtradosListaTiposFormulas = null;
            tipoLista = 0;
        }
        listaTiposFormulasBorrar.clear();
        listaTiposFormulasCrear.clear();
        listaTiposFormulasModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaTiposFormulas = null;
        guardado = true;
        permitirIndex = true;

    }

    //Getter & Setter
    public List<TiposFormulas> getListaTiposFormulas() {
        if (listaTiposFormulas == null) {
            System.out.println("secOperando" + secOperando);
            System.out.println("tOperando" + tOperando);
            System.out.println("operando seleccionado" + operando);
            listaTiposFormulas = administrarTiposFormulas.buscarTiposFormulas(secOperando, tOperando);
        }
        return listaTiposFormulas;
    }

    public void setListaTiposFormulas(List<TiposFormulas> listaTiposFormulas) {
        this.listaTiposFormulas = listaTiposFormulas;
    }

    public List<TiposFormulas> getFiltradosListaTiposFormulas() {
        return filtradosListaTiposFormulas;
    }

    public void setFiltradosListaTiposFormulas(List<TiposFormulas> filtradosListaTiposFormulas) {
        this.filtradosListaTiposFormulas = filtradosListaTiposFormulas;
    }

    public TiposFormulas getEditarTiposFormulas() {
        return editarTiposFormulas;
    }

    public void setEditarTiposFormulas(TiposFormulas editarTiposFormulas) {
        this.editarTiposFormulas = editarTiposFormulas;
    }

    public boolean isAceptarEditar() {
        return aceptarEditar;
    }

    public void setAceptarEditar(boolean aceptarEditar) {
        this.aceptarEditar = aceptarEditar;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public TiposFormulas getNuevoTipoFormula() {
        return nuevoTipoFormula;
    }

    public void setNuevoTipoFormula(TiposFormulas nuevoTipoFormula) {
        this.nuevoTipoFormula = nuevoTipoFormula;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public BigInteger getSecOperando() {
        return secOperando;
    }

    public void setSecOperando(BigInteger secOperando) {
        this.secOperando = secOperando;
    }

    public String gettOperando() {
        return tOperando;
    }

    public void settOperando(String tOperando) {
        this.tOperando = tOperando;
    }

    public Operandos getOperando() {
        return operando;
    }

    public void setOperando(Operandos operando) {
        this.operando = operando;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public TiposFormulas getDuplicarTipoFormula() {
        return duplicarTipoFormula;
    }

    public void setDuplicarTipoFormula(TiposFormulas duplicarTipoFormula) {
        this.duplicarTipoFormula = duplicarTipoFormula;
    }

    public List<Formulas> getLovListaFormulas() {
        if (lovListaFormulas == null) {
            lovListaFormulas = administrarTiposFormulas.lovFormulas();

        }
        return lovListaFormulas;
    }

    public void setLovListaFormulas(List<Formulas> lovListaFormulas) {
        this.lovListaFormulas = lovListaFormulas;
    }

    public List<Formulas> getLovFiltradosListaFormulas() {
        return lovFiltradosListaFormulas;
    }

    public void setLovFiltradosListaFormulas(List<Formulas> lovFiltradosListaFormulas) {
        this.lovFiltradosListaFormulas = lovFiltradosListaFormulas;
    }

    public Formulas getSeleccionFormulas() {
        return seleccionFormulas;
    }

    public void setSeleccionFormulas(Formulas seleccionFormulas) {
        this.seleccionFormulas = seleccionFormulas;
    }

    public TiposFormulas getTiposFormulasRegistro() {
        return tiposFormulasRegistro;
    }

    public void setTiposFormulasRegistro(TiposFormulas tiposFormulasRegistro) {
        this.tiposFormulasRegistro = tiposFormulasRegistro;
    }

    public BigInteger getSecuenciaTiposFormulas() {
        return secuenciaTiposFormulas;
    }

    public void setSecuenciaTiposFormulas(BigInteger secuenciaTiposFormulas) {
        this.secuenciaTiposFormulas = secuenciaTiposFormulas;
    }

    public TiposFormulas getTipoFormulaSeleccionado() {
        return tipoFormulaSeleccionado;
    }

    public void setTipoFormulaSeleccionado(TiposFormulas tipoFormulaSeleccionado) {
        this.tipoFormulaSeleccionado = tipoFormulaSeleccionado;
    }

}
