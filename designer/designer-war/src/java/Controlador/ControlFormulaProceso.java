package Controlador;

import Entidades.Formulas;
import Entidades.FormulasProcesos;
import Entidades.Procesos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaProcesoInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class ControlFormulaProceso implements Serializable {

    @EJB
    AdministrarFormulaProcesoInterface administrarFormulaProceso;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<FormulasProcesos> listFormulasProcesos;
    private List<FormulasProcesos> filtrarListFormulasProcesos;
    private Formulas formulaActual;
    private FormulasProcesos formulaTablaSeleccionada;
    ///////
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Columnas Tabla VC
    private Column formulaProceso;//, formulaPeriodicidad;
    //Otros
    private boolean aceptar;
    //modificar
    private List<FormulasProcesos> listFormulasProcesosModificar;
    private boolean guardado;
    //crear 
    private FormulasProcesos nuevoFormulaProceso;
    private List<FormulasProcesos> listFormulasProcesosCrear;
    private BigInteger l;
    private int k;
    //borrar 
    private List<FormulasProcesos> listFormulasProcesosBorrar;
    //editar celda
    private FormulasProcesos editarFormulaProceso;
    private int cualCelda, tipoLista;
    //duplicar
    private FormulasProcesos duplicarFormulaProceso;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private String nombreTablaRastro;
    private String nombreXML, nombreTabla;
    private String proceso;

    //////////////////////
    private List<Procesos> lovProcesos;
    private List<Procesos> filtrarLovProcesos;
    private Procesos procesoSeleccionado;

    private boolean permitirIndex;
    private int tipoActualizacion;
    //
    private String altoTabla;
    private String infoRegistro;
    private String infoRegistroProceso;
    //
    private boolean activarLOV;

    public ControlFormulaProceso() {
        altoTabla = "280";
        permitirIndex = true;
        tipoActualizacion = -1;
        lovProcesos = null;
        procesoSeleccionado = new Procesos();
        listFormulasProcesos = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listFormulasProcesosBorrar = new ArrayList<FormulasProcesos>();
        //crear aficiones
        listFormulasProcesosCrear = new ArrayList<FormulasProcesos>();
        k = 0;
        //modificar aficiones
        listFormulasProcesosModificar = new ArrayList<FormulasProcesos>();
        //editar
        editarFormulaProceso = new FormulasProcesos();
        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevoFormulaProceso = new FormulasProcesos();
        nuevoFormulaProceso.setProceso(new Procesos());
        duplicarFormulaProceso = new FormulasProcesos();
        formulaActual = new Formulas();
        //
        activarLOV = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarFormulaProceso.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirFormula(BigInteger secuencia) {
        formulaActual = administrarFormulaProceso.formulaActual(secuencia);
        getListFormulasProcesos();
        contarRegistros();
        anularBotonLOV();
    }

    public void modificarFormulaProceso(int celda) {
        if (celda == 1){
            anularBotonLOV();
        } 
        if (!listFormulasProcesosCrear.contains(formulaTablaSeleccionada)) {
            if (listFormulasProcesosModificar.isEmpty()) {
                listFormulasProcesosModificar.add(formulaTablaSeleccionada);
            } else if (!listFormulasProcesosModificar.contains(formulaTablaSeleccionada)) {
                listFormulasProcesosModificar.add(formulaTablaSeleccionada);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosFormulaProceso");

    }

    public void modificarFormulaProceso(FormulasProcesos formulaProceso, String confirmarCambio, String valorConfirmar) {
        formulaTablaSeleccionada = formulaProceso;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
            formulaTablaSeleccionada.getProceso().setDescripcion(proceso);
            for (int i = 0; i < lovProcesos.size(); i++) {
                if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                formulaTablaSeleccionada.setProceso(lovProcesos.get(indiceUnicoElemento));
                lovProcesos.clear();
                getLovProcesos();
            } else {
                permitirIndex = false;
                contarRegistrosLov(0);
                context.update("form:ProcesosDialogo");
                context.execute("ProcesosDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (!listFormulasProcesosCrear.contains(formulaTablaSeleccionada)) {
                if (listFormulasProcesosModificar.isEmpty()) {
                    listFormulasProcesosModificar.add(formulaTablaSeleccionada);
                } else if (!listFormulasProcesosModificar.contains(formulaTablaSeleccionada)) {
                    listFormulasProcesosModificar.add(formulaTablaSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
        context.update("form:datosFormulaProceso");
    }

    public void cambiarIndice(FormulasProcesos formulaProceso, int celda) {
        formulaTablaSeleccionada = formulaProceso;
        if (permitirIndex == true) {
            cualCelda = celda;
            if(celda == 0){
                activarBotonLOV();
            }
            proceso = formulaTablaSeleccionada.getProceso().getDescripcion();
        }
    }

    //GUARDAR
    public void guardarGeneral() {
        guardarCambiosFormulaProceso();
    }

    public void guardarCambiosFormulaProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listFormulasProcesosBorrar.isEmpty()) {
                    for (int i = 0; i < listFormulasProcesosBorrar.size(); i++) {
                        administrarFormulaProceso.borrarFormulasProcesos(listFormulasProcesosBorrar);
                    }
                    listFormulasProcesosBorrar.clear();
                }
                if (!listFormulasProcesosCrear.isEmpty()) {
                    for (int i = 0; i < listFormulasProcesosCrear.size(); i++) {
                        administrarFormulaProceso.crearFormulasProcesos(listFormulasProcesosCrear);
                    }
                    listFormulasProcesosCrear.clear();
                }
                if (!listFormulasProcesosModificar.isEmpty()) {
                    administrarFormulaProceso.editarFormulasProcesos(listFormulasProcesosModificar);
                    listFormulasProcesosModificar.clear();
                }
                listFormulasProcesos = null;
                getListFormulasProcesos();
                contarRegistros();
                context.update("form:datosFormulaProceso");
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                context.update("form:growl");
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }

    }

    public void cancelarModificacionGeneral() {
        if (guardado == false) {
            cancelarModificacionFormulaProceso();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosFormulaProceso");
        }
        formulaTablaSeleccionada = null;
    }

    public void cancelarModificacionFormulaProceso() {
        restaurarTabla();
        listFormulasProcesosBorrar.clear();
        listFormulasProcesosCrear.clear();
        listFormulasProcesosModificar.clear();
        k = 0;
        listFormulasProcesos = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        getListFormulasProcesos();
        contarRegistros();
        context.update("form:datosFormulaProceso");
    }

    public void editarCelda() {
        if (formulaTablaSeleccionada != null) {
            editarFormulaProceso = formulaTablaSeleccionada;
            if (cualCelda == 0) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formularioDialogos:editarFormulaProcesoD");
                context.execute("editarFormulaProcesoD.show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("seleccionarRegistro.show()");
        }
    }

    public void dialogoNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:NuevoRegistroProceso");
        context.execute("NuevoRegistroProceso.show()");
    }

    public void restaurarTabla() {
        if (bandera == 1) {
            altoTabla = "280";
            formulaProceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProceso");
            formulaProceso.setFilterStyle("display: none; visibility: hidden;");

//            formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaPeriodicidad");
//            formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
            bandera = 0;
            filtrarListFormulasProcesos = null;
            tipoLista = 0;
        }
    }

    //CREAR 
    public void agregarNuevoFormulaProceso() {
        if (nuevoFormulaProceso.getProceso().getSecuencia() != null) {
            restaurarTabla();
            k++;
            l = BigInteger.valueOf(k);
            nuevoFormulaProceso.setSecuencia(l);
            nuevoFormulaProceso.setFormula(formulaActual);
            listFormulasProcesosCrear.add(nuevoFormulaProceso);
            listFormulasProcesos.add(nuevoFormulaProceso);
            formulaTablaSeleccionada = listFormulasProcesos.get(listFormulasProcesos.indexOf(nuevoFormulaProceso));
            nuevoFormulaProceso = new FormulasProcesos();
            nuevoFormulaProceso.setProceso(new Procesos());
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistros();
            context.update("form:datosFormulaProceso");
            context.execute("NuevoRegistroProceso.hide()");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("errorRegistroFP.show()");
        }
    }

    //LIMPIAR NUEVO REGISTRO
    /**
     */
    public void limpiarNuevaFormulaProceso() {
        nuevoFormulaProceso = new FormulasProcesos();
        nuevoFormulaProceso.setProceso(new Procesos());
    }

//DUPLICAR VC
    /**
     */
    public void verificarRegistroDuplicar() {
        if (formulaTablaSeleccionada != null) {
            duplicarFormulaProcesoM();
        } else {
            RequestContext.getCurrentInstance().execute("seleccionarRegistro.show()");
        }
    }

    public void duplicarFormulaProcesoM() {
        if (formulaTablaSeleccionada != null) {
            duplicarFormulaProceso = new FormulasProcesos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarFormulaProceso.setFormula(formulaTablaSeleccionada.getFormula());
            duplicarFormulaProceso.setProceso(formulaTablaSeleccionada.getProceso());
            duplicarFormulaProceso.setPeriodicidadindependiente(formulaTablaSeleccionada.getPeriodicidadindependiente());
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:DuplicarRegistroFormulaProceso");
            context.execute("DuplicarRegistroFormulaProceso.show()");
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
     */
    public void confirmarDuplicarFormulaProceso() {
        if (duplicarFormulaProceso.getProceso().getSecuencia() != null) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarFormulaProceso.setSecuencia(l);
            listFormulasProcesos.add(duplicarFormulaProceso);
            listFormulasProcesosCrear.add(duplicarFormulaProceso);
            formulaTablaSeleccionada = listFormulasProcesos.get(listFormulasProcesos.indexOf(duplicarFormulaProceso));
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistros();
            context.update("form:datosFormulaProceso");
            context.execute("DuplicarRegistroFormulaProceso.hide()");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            restaurarTabla();
            duplicarFormulaProceso = new FormulasProcesos();
        } else {
            RequestContext.getCurrentInstance().execute("errorRegistroFP.show()");
        }
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar Set
     */
    public void limpiarDuplicarFormulaProceso() {
        duplicarFormulaProceso = new FormulasProcesos();
        duplicarFormulaProceso.setProceso(new Procesos());
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
        if (formulaTablaSeleccionada != null) {
            borrarFormulaProceso();
        } else {
            RequestContext.getCurrentInstance().execute("seleccionarRegistro.show()");
        }
    }

    public void borrarFormulaProceso() {
        if (formulaTablaSeleccionada != null) {
            if (!listFormulasProcesosModificar.isEmpty() && listFormulasProcesosModificar.contains(formulaTablaSeleccionada)) {
                listFormulasProcesosModificar.remove(formulaTablaSeleccionada);
                listFormulasProcesosBorrar.add(formulaTablaSeleccionada);
            } else if (!listFormulasProcesosCrear.isEmpty() && listFormulasProcesosCrear.contains(formulaTablaSeleccionada)) {
                listFormulasProcesosCrear.remove(formulaTablaSeleccionada);
            } else {
                listFormulasProcesosBorrar.add(formulaTablaSeleccionada);
            }
            listFormulasProcesos.remove(formulaTablaSeleccionada);
            if (tipoLista == 1) {
                filtrarListFormulasProcesos.remove(formulaTablaSeleccionada);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            getListFormulasProcesos();
            formulaTablaSeleccionada = null;
            contarRegistros();
            context.update("form:datosFormulaProceso");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    /**
     * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        if (bandera == 0) {
            altoTabla = "259";
            formulaProceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProceso");
            formulaProceso.setFilterStyle("width: 85%");

//            formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaPeriodicidad");
//            formulaPeriodicidad.setFilterStyle("width: 85%");

            RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
            bandera = 1;
        } else {
            restaurarTabla();
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        restaurarTabla();
        listFormulasProcesosBorrar.clear();
        listFormulasProcesosCrear.clear();
        listFormulasProcesosModificar.clear();
        formulaTablaSeleccionada = null;
        k = 0;
        listFormulasProcesos = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        formulaActual = null;
        lovProcesos = null;
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (formulaTablaSeleccionada != null) {
            if (cualCelda == 0) {
                contarRegistrosLov(0);
                context.update("form:ProcesosDialogo");
                context.execute("ProcesosDialogo.show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void asignarIndex(FormulasProcesos formulaProceso, int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        formulaTablaSeleccionada = formulaProceso;
        tipoActualizacion = LND;

        if (dlg == 0) {
            activarBotonLOV();
            contarRegistrosLov(0);
            context.update("form:ProcesosDialogo");
            context.execute("ProcesosDialogo.show()");
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("PROCESO")) {
            if (tipoNuevo == 1) {
                proceso = nuevoFormulaProceso.getProceso().getDescripcion();
            } else if (tipoNuevo == 2) {
                proceso = duplicarFormulaProceso.getProceso().getDescripcion();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoFormulaProceso(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
            if (tipoNuevo == 1) {
                nuevoFormulaProceso.getProceso().setDescripcion(proceso);
            } else if (tipoNuevo == 2) {
                duplicarFormulaProceso.getProceso().setDescripcion(proceso);
            }
            for (int i = 0; i < lovProcesos.size(); i++) {
                if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoFormulaProceso.setProceso(lovProcesos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoFormulaProceso");
                } else if (tipoNuevo == 2) {
                    duplicarFormulaProceso.setProceso(lovProcesos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarFormulaProceso");
                }
                lovProcesos.clear();
                getLovProcesos();
            } else {
                contarRegistrosLov(0);
                context.update("form:ProcesosDialogo");
                context.execute("ProcesosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoFormulaProceso");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarFormulaProceso");
                }
            }
        }
    }

    public void actualizarProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            formulaTablaSeleccionada.setProceso(procesoSeleccionado);
            if (!listFormulasProcesosCrear.contains(formulaTablaSeleccionada)) {
                if (listFormulasProcesosModificar.isEmpty()) {
                    listFormulasProcesosModificar.add(formulaTablaSeleccionada);
                } else if (!listFormulasProcesosModificar.contains(formulaTablaSeleccionada)) {
                    listFormulasProcesosModificar.add(formulaTablaSeleccionada);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            context.update("form:datosFormulaProceso");
        } else if (tipoActualizacion == 1) {
            nuevoFormulaProceso.setProceso(procesoSeleccionado);
            context.update("formularioDialogos:nuevoFormulaProceso");
        } else if (tipoActualizacion == 2) {
            duplicarFormulaProceso.setProceso(procesoSeleccionado);
            context.update("formularioDialogos:duplicarFormulaProceso");
        }
        filtrarLovProcesos = null;
        procesoSeleccionado = new Procesos();
        aceptar = true;
        tipoActualizacion = -1;
        context.update("form:ProcesosDialogo");
        context.update("form:lovProceso");
        context.update("form:aceptarF");
        context.reset("form:lovProceso:globalFilter");
        context.execute("lovProceso.clearFilters()");
        context.execute("ProcesosDialogo.hide()");
    }

    public void cancelarCambioProceso() {
        filtrarLovProcesos = null;
        procesoSeleccionado = new Procesos();
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:ProcesosDialogo");
        context.update("form:lovProceso");
        context.update("form:aceptarF");
        context.reset("form:lovProceso:globalFilter");
        context.execute("lovProceso.clearFilters()");
        context.execute("ProcesosDialogo.hide()");
    }

    /**
     * Metodo que activa el boton aceptar de la pantalla y dialogos
     */
    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    public String exportXML() {
        nombreTabla = ":formExportarFormula:datosFormulaProcesoExportar";
        nombreXML = "FormulaProceso_XML";
        return nombreTabla;
    }

    /**
     * Metodo que exporta datos a PDF
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        exportPDF_NF();
    }

    public void exportPDF_NF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarFormula:datosFormulaProcesoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "FormulaProceso_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportXLS() throws IOException {
        exportXLS_NF();
    }

    public void exportXLS_NF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarFormula:datosFormulaProcesoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "FormulaProceso_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    //EVENTO FILTRAR
    /**
     * Evento que cambia la lista reala a la filtrada
     */
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        formulaTablaSeleccionada = null;
        contarRegistros();
        anularBotonLOV();
    }

    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
    public void verificarRastroFormulaProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (formulaTablaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(formulaTablaSeleccionada.getSecuencia(), "FORMULASPROCESOS");
            if (resultado == 1) {
                context.execute("errorObjetosDB.show()");
            } else if (resultado == 2) {
                nombreTablaRastro = "FormulasProcesos";
                msnConfirmarRastro = "La tabla FORMULASPROCESOS tiene rastros para el registro seleccionado, ¿desea continuar?";
                context.update("form:msnConfirmarRastro");
                context.execute("confirmarRastro.show()");
            } else if (resultado == 3) {
                context.execute("errorRegistroRastro.show()");
            } else if (resultado == 4) {
                context.execute("errorTablaConRastro.show()");
            } else if (resultado == 5) {
                context.execute("errorTablaSinRastro.show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("FORMULASPROCESOS")) {
            nombreTablaRastro = "FormulasProcesos";
            msnConfirmarRastroHistorico = "La tabla FORMULASPROCESOS tiene rastros historicos, ¿Desea continuar?";
            context.update("form:confirmarRastroHistorico");
            context.execute("confirmarRastroHistorico.show()");
        } else {
            context.execute("errorRastroHistorico.show()");
        }
    }
    
    public void activarBotonLOV() {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }
    
    public void anularBotonLOV() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }
    
    public void contarRegistros() {
        if (tipoLista == 1) {
            infoRegistro = String.valueOf(filtrarListFormulasProcesos.size());
        } else if (listFormulasProcesos != null) {
            infoRegistro = String.valueOf(listFormulasProcesos.size());
        } else {
            infoRegistro = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }
    
    public void contarRegistrosLov(int tipoLov) {
        if (tipoLov == 1) {
            infoRegistroProceso = String.valueOf(filtrarLovProcesos.size());
        } else if (lovProcesos != null) {
            infoRegistroProceso = String.valueOf(lovProcesos.size());
        } else {
            infoRegistroProceso = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroProceso");
    }

    //GETTERS AND SETTERS
    public List<FormulasProcesos> getListFormulasProcesos() {
        try {
            if (listFormulasProcesos == null) {
                if (formulaActual != null) {
                    listFormulasProcesos = administrarFormulaProceso.listFormulasProcesosParaFormula(formulaActual.getSecuencia());
                }
                return listFormulasProcesos;
            }
            return listFormulasProcesos;
        } catch (Exception e) {
            System.out.println("Error...!! getListFormulasProcesos " + e.toString());
            return null;
        }
    }

    public void setListFormulasProceso(List<FormulasProcesos> setListFormulasProceso) {
        this.listFormulasProcesos = setListFormulasProceso;
    }

    public List<FormulasProcesos> getFiltrarListFormulasProcesos() {
        return filtrarListFormulasProcesos;
    }

    public void setFiltrarListFormulasProcesos(List<FormulasProcesos> setFiltrarListFormulasProcesos) {
        this.filtrarListFormulasProcesos = setFiltrarListFormulasProcesos;
    }

    public FormulasProcesos getNuevoFormulaProceso() {
        return nuevoFormulaProceso;
    }

    public void setNuevoFormulaProceso(FormulasProcesos setNuevoFormulaProceso) {
        this.nuevoFormulaProceso = setNuevoFormulaProceso;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public FormulasProcesos getEditarFormulaProceso() {
        return editarFormulaProceso;
    }

    public void setEditarFormulaProceso(FormulasProcesos setEditarFormulaProceso) {
        this.editarFormulaProceso = setEditarFormulaProceso;
    }

    public FormulasProcesos getDuplicarFormulasProcesos() {
        return duplicarFormulaProceso;
    }

    public void setDuplicarFormulasProcesos(FormulasProcesos setDuplicarFormulasProcesos) {
        this.duplicarFormulaProceso = setDuplicarFormulasProcesos;
    }

    public List<FormulasProcesos> getListFormulasProcesosModificar() {
        return listFormulasProcesosModificar;
    }

    public void setListFormulasProcesosModificar(List<FormulasProcesos> setListFormulasProcesosModificar) {
        this.listFormulasProcesosModificar = setListFormulasProcesosModificar;
    }

    public List<FormulasProcesos> getListFormulasProcesosCrear() {
        return listFormulasProcesosCrear;
    }

    public void setListFormulasProcesosCrear(List<FormulasProcesos> setListFormulasProcesosCrear) {
        this.listFormulasProcesosCrear = setListFormulasProcesosCrear;
    }

    public List<FormulasProcesos> getListFormulasProcesosBorrar() {
        return listFormulasProcesosBorrar;
    }

    public void setListFormulasProcesosBorrar(List<FormulasProcesos> setListFormulasProcesosBorrar) {
        this.listFormulasProcesosBorrar = setListFormulasProcesosBorrar;
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

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public List<Procesos> getLovProcesos() {
        if (formulaActual != null) {
            lovProcesos = administrarFormulaProceso.listProcesos(formulaActual.getSecuencia());
        }
        return lovProcesos;
    }

    public void setLovProcesos(List<Procesos> setLovProcesos) {
        this.lovProcesos = setLovProcesos;
    }

    public List<Procesos> getFiltrarLovProcesos() {
        return filtrarLovProcesos;
    }

    public void setFiltrarLovProcesos(List<Procesos> setFiltrarLovProcesos) {
        this.filtrarLovProcesos = setFiltrarLovProcesos;
    }

    public Procesos getProcesoSeleccionada() {
        return procesoSeleccionado;
    }

    public void setProcesoSeleccionada(Procesos setProcesoSeleccionada) {
        this.procesoSeleccionado = setProcesoSeleccionada;
    }

    public Formulas getFormulaActual() {
        return formulaActual;
    }

    public void setFormulaActual(Formulas formulaActual) {
        this.formulaActual = formulaActual;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public Procesos getProcesoSeleccionado() {
        return procesoSeleccionado;
    }

    public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
        this.procesoSeleccionado = procesoSeleccionado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroProceso() {
        return infoRegistroProceso;
    }

    public void setInfoRegistroProceso(String infoRegistroProceso) {
        this.infoRegistroProceso = infoRegistroProceso;
    }

    public FormulasProcesos getFormulaTablaSeleccionada() {
        return formulaTablaSeleccionada;
    }

    public void setFormulaTablaSeleccionada(FormulasProcesos formulaTablaSeleccionada) {
        this.formulaTablaSeleccionada = formulaTablaSeleccionada;
    }

    public FormulasProcesos getDuplicarFormulaProceso() {
        return duplicarFormulaProceso;
    }

    public void setDuplicarFormulaProceso(FormulasProcesos duplicarFormulaProceso) {
        this.duplicarFormulaProceso = duplicarFormulaProceso;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

}
