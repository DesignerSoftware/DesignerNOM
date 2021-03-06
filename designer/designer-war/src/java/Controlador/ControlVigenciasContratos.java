package Controlador;

import Entidades.Contratos;
import Entidades.Empleados;
import Entidades.TiposContratos;
import Entidades.VigenciasContratos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasContratosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
@ManagedBean
@SessionScoped
public class ControlVigenciasContratos implements Serializable {

    private static Logger log = Logger.getLogger(ControlVigenciasContratos.class);

    @EJB
    AdministrarVigenciasContratosInterface administrarVigenciasContratos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Vigencias Contratos
    private List<VigenciasContratos> vigenciasContratos;
    private List<VigenciasContratos> filtrarVC;
    private VigenciasContratos vigenciaSeleccionada;
    //Contratos
    private List<Contratos> lovContratos;
    private Contratos contratoSelecionado;
    private List<Contratos> filtradoContratos;
    //Tipos Contratos
    private List<TiposContratos> lovTiposContratos;
    private TiposContratos tipoContratoSelecionado;
    private List<TiposContratos> filtradoTiposContratos;
    private Empleados empleado;
    private int tipoActualizacion;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Columnas Tabla VC
    private Column vcFechaInicial, vcFechaFinal, vcContrato, vcTipoContrato;
    //Otros
    private boolean aceptar;
    //private int index;
    //modificar
    private List<VigenciasContratos> listVCModificar;
    private boolean guardado;
    //private boolean guardarOk;
    //crear VC
    public VigenciasContratos nuevaVigencia;
    private List<VigenciasContratos> listVCCrear;
    private BigInteger nuevaVContratoSecuencia;
    private int paraNuevaVContrato;
    //borrar VC
    private List<VigenciasContratos> listVCBorrar;
    //editar celda
    private VigenciasContratos editarVC;
    private int cualCelda, tipoLista;
    //private boolean cambioEditor, aceptarEditar;
    //duplicar
    private VigenciasContratos duplicarVC;
    //String Variables AutoCompletar
    private String tipoContrato, legislacionLaboral;
    //Boolean AutoCompletar
    private boolean permitirIndex;
    //private BigInteger secRegistro;
    private Date fechaParametro;
    private Date fechaIni, fechaFin;
    private String altoTabla;
    public String infoRegistro;
    //
    private String infoRegistroContrato, infoRegistroTipoContrato;
    //
    private DataTable tablaC;
    private boolean activarLOV;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlVigenciasContratos() {
        vigenciasContratos = null;
        lovContratos = null;
        lovTiposContratos = null;
        empleado = new Empleados();
        //Otros
        aceptar = true;
        //borrar aficiones
        listVCBorrar = new ArrayList<VigenciasContratos>();
        //crear aficiones
        listVCCrear = new ArrayList<VigenciasContratos>();
        paraNuevaVContrato = 0;
        //modificar aficiones
        listVCModificar = new ArrayList<VigenciasContratos>();
        //editar
        editarVC = new VigenciasContratos();
        //cambioEditor = false;
        //aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        //guardar 
        guardado = true;
        //Crear VC
        nuevaVigencia = new VigenciasContratos();
        nuevaVigencia.setContrato(new Contratos());
        nuevaVigencia.setTipocontrato(new TiposContratos());
        permitirIndex = true;
        altoTabla = "295";
        activarLOV = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {
        lovContratos = null;
        lovTiposContratos = null;
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
            administrarVigenciasContratos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct ControlVigenciasCargos:  ", e);
            log.error("Causa: " + e.getCause());
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
        String pagActual = "empllegislacionlaboral";
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

    //EMPLEADO DE LA VIGENCIA
    /**
     * Metodo que recibe la secuencia empleado desde la pagina anterior y
     * obtiene el empleado referenciado
     *
     * @param sec Secuencia del Empleado
     */
    public void recibirEmpleado(Empleados empl) {

        vigenciasContratos = null;
        empleado = empl;

        getVigenciasContratos();
        //INICIALIZAR BOTONES NAVEGACION
        if (!vigenciasContratos.isEmpty()) {
            if (vigenciasContratos != null) {
                vigenciaSeleccionada = vigenciasContratos.get(0);
            }
        } else {
            vigenciaSeleccionada = null;
        }
    }

    public void modificarVC() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listVCCrear.contains(vigenciaSeleccionada)) {

            if (listVCModificar.isEmpty()) {
                listVCModificar.add(vigenciaSeleccionada);
            } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
                listVCModificar.add(vigenciaSeleccionada);
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public boolean validarFechasRegistro(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        log.error("fechaparametro : " + fechaParametro);
        boolean retorno = true;
        if (i == 0) {
            VigenciasContratos auxiliar = null;
            auxiliar = vigenciaSeleccionada;

            if (auxiliar.getFechafinal() != null) {
                if (auxiliar.getFechainicial().after(fechaParametro) && auxiliar.getFechainicial().before(auxiliar.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (auxiliar.getFechafinal() == null) {
                if (auxiliar.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (i == 1) {
            if (nuevaVigencia.getFechafinal() != null) {
                if (nuevaVigencia.getFechainicial().after(fechaParametro) && nuevaVigencia.getFechainicial().before(nuevaVigencia.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (nuevaVigencia.getFechafinal() == null) {
                if (nuevaVigencia.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (i == 2) {
            if (duplicarVC.getFechafinal() != null) {
                if (duplicarVC.getFechainicial().after(fechaParametro) && duplicarVC.getFechainicial().before(duplicarVC.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (duplicarVC.getFechafinal() == null) {
                if (duplicarVC.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public void modificarFechas(VigenciasContratos vContratos, int c) {

        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        vigenciaSeleccionada = vContratos;

        if (vigenciaSeleccionada.getFechainicial() != null) {
            boolean retorno = false;
            retorno = validarFechasRegistro(0);
            if (retorno) {
                cambiarIndice(vigenciaSeleccionada, c);
                modificarVC();
            } else {
                vigenciaSeleccionada.setFechafinal(fechaFin);
                vigenciaSeleccionada.setFechainicial(fechaIni);

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
        } else {
            vigenciaSeleccionada.setFechainicial(fechaIni);

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    /**
     * Metodo que modifica los cambios efectuados en la tabla VigenciasContratos
     * de la pagina
     *
     * @param indice Fila en la cual se realizo el cambio
     */
    public void modificarVC(VigenciasContratos vContratos, String confirmarCambio, String valorConfirmar) {
        vigenciaSeleccionada = vContratos;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("LEGISLACIONLABORAL")) {
            vigenciaSeleccionada.getContrato().setDescripcion(legislacionLaboral);

            for (int i = 0; i < lovContratos.size(); i++) {
                if (lovContratos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaSeleccionada.setContrato(lovContratos.get(indiceUnicoElemento));

                lovContratos.clear();
                getLovContratos();
            } else {
                permitirIndex = false;
                getInfoRegistroContrato();
                RequestContext.getCurrentInstance().update("form:ContratosDialogo");
                RequestContext.getCurrentInstance().execute("PF('ContratosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("TIPOCONTRATO")) {
            if (!valorConfirmar.isEmpty()) {
                vigenciaSeleccionada.getTipocontrato().setNombre(tipoContrato);

                for (int i = 0; i < lovTiposContratos.size(); i++) {
                    if (lovTiposContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaSeleccionada.setTipocontrato(lovTiposContratos.get(indiceUnicoElemento));

                    lovTiposContratos.clear();
                    getLovTiposContratos();
                } else {
                    permitirIndex = false;
                    getInfoRegistroTipoContrato();
                    RequestContext.getCurrentInstance().update("form:TiposContratoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TiposContratoDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                lovTiposContratos.clear();
                getLovTiposContratos();
                vigenciaSeleccionada.setTipocontrato(new TiposContratos());

                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
        if (coincidencias == 1) {
            if (!listVCCrear.contains(vigenciaSeleccionada)) {

                if (listVCModificar.isEmpty()) {
                    listVCModificar.add(vigenciaSeleccionada);
                } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
                    listVCModificar.add(vigenciaSeleccionada);
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
        RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
    }

    /**
     * Metodo que obtiene los valores de los dialogos para realizar los
     * autocomplete de los campos
     *
     * @param tipoNuevo Tipo de operacion: Nuevo Registro - Duplicar Registro
     * @param Campo Campo que toma el cambio de autocomplete
     */
    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("LEGISLACIONLABORAL")) {
            if (tipoNuevo == 1) {
                legislacionLaboral = nuevaVigencia.getContrato().getDescripcion();
            } else if (tipoNuevo == 2) {
                legislacionLaboral = duplicarVC.getContrato().getDescripcion();
            }
        } else if (Campo.equals("TIPOCONTRATO")) {
            if (tipoNuevo == 1) {
                tipoContrato = nuevaVigencia.getTipocontrato().getNombre();
            } else if (tipoNuevo == 2) {
                tipoContrato = duplicarVC.getTipocontrato().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("LEGISLACIONLABORAL")) {
            if (tipoNuevo == 1) {
                nuevaVigencia.getContrato().setDescripcion(tipoContrato);
            } else if (tipoNuevo == 2) {
                duplicarVC.getContrato().setDescripcion(tipoContrato);
            }
            for (int i = 0; i < lovContratos.size(); i++) {
                if (lovContratos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigencia.setContrato(lovContratos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCentroCosto");
                } else if (tipoNuevo == 2) {
                    duplicarVC.setContrato(lovContratos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCentroCosto");
                }
                lovContratos.clear();
                getLovContratos();
            } else {
                RequestContext.getCurrentInstance().update("form:ContratosDialogo");
                RequestContext.getCurrentInstance().execute("PF('ContratosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaLegislacionLaboral");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarLegislacionLaboral");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TIPOCONTRATO")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaVigencia.getTipocontrato().setNombre(tipoContrato);
                } else if (tipoNuevo == 2) {
                    duplicarVC.getTipocontrato().setNombre(tipoContrato);
                }
                for (int i = 0; i < lovTiposContratos.size(); i++) {
                    if (lovTiposContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaVigencia.setTipocontrato(lovTiposContratos.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoContrato");
                    } else if (tipoNuevo == 2) {
                        duplicarVC.setTipocontrato(lovTiposContratos.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoContrato");
                    }
                    lovTiposContratos.clear();
                    getLovTiposContratos();
                } else {
                    RequestContext.getCurrentInstance().update("form:TiposContratoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TiposContratoDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoContrato");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoContrato");
                    }
                }
            }
        } else {
            lovTiposContratos.clear();
            getLovTiposContratos();
            if (tipoNuevo == 1) {
                nuevaVigencia.setTipocontrato(new TiposContratos());
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoContrato");
            } else if (tipoNuevo == 2) {
                duplicarVC.setTipocontrato(new TiposContratos());
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoContrato");
            }
        }
    }

    //Ubicacion Celda.
    /**
     * Metodo que obtiene la posicion dentro de la tabla Vigencias Contratos
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    public void cambiarIndice(VigenciasContratos vContrato, int celda) {
        if (permitirIndex) {
            vigenciaSeleccionada = vContrato;
            cualCelda = celda;
            fechaIni = vigenciaSeleccionada.getFechainicial();
            fechaFin = vigenciaSeleccionada.getFechafinal();

            if (cualCelda == 2) {
                activarLOV = false;
                legislacionLaboral = vigenciaSeleccionada.getContrato().getDescripcion();
            } else if (cualCelda == 3) {
                activarLOV = false;
                tipoContrato = vigenciaSeleccionada.getTipocontrato().getNombre();
            } else {
                activarLOV = true;
            }
            RequestContext.getCurrentInstance().update("form:listaValores");

        }
    }

    //GUARDAR
    /**
     * Metodo que guarda los cambios efectuados en la pagina Vigencias Contratos
     */
    public void guardarYSalir() {
        guardarCambiosVC();
        salir();
    }

    public void guardarCambiosVC() {
        if (guardado == false) {
            if (!listVCBorrar.isEmpty()) {
                for (int i = 0; i < listVCBorrar.size(); i++) {
                    administrarVigenciasContratos.borrarVC(listVCBorrar.get(i));
                }
                listVCBorrar.clear();
            }
            if (!listVCCrear.isEmpty()) {
                for (int i = 0; i < listVCCrear.size(); i++) {
                    administrarVigenciasContratos.crearVC(listVCCrear.get(i));
                }
                listVCCrear.clear();
            }
            if (!listVCModificar.isEmpty()) {
                administrarVigenciasContratos.modificarVC(listVCModificar);
                listVCModificar.clear();
            }
            vigenciasContratos = null;
            getVigenciasContratos();
            if (!vigenciasContratos.isEmpty()) {
                if (vigenciasContratos != null) {
                    vigenciaSeleccionada = vigenciasContratos.get(0);
                }

            }
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            contarRegistros();
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            paraNuevaVContrato = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        vigenciaSeleccionada = null;
    }
    //CANCELAR MODIFICACIONES

    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarYSalir() {
//      cancelarModificacion();
        salir();
    }

    public void cancelarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            vcFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFechaInicial");
            vcFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vcFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFechaFinal");
            vcFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            vcContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcContrato");
            vcContrato.setFilterStyle("display: none; visibility: hidden;");
            vcTipoContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcTipoContrato");
            vcTipoContrato.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "295";
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            bandera = 0;
            filtrarVC = null;
            tipoLista = 0;
        }

        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        listVCBorrar.clear();
        listVCCrear.clear();
        listVCModificar.clear();
        paraNuevaVContrato = 0;
        vigenciasContratos = null;
        getVigenciasContratos();
        if (vigenciasContratos != null) {
            vigenciaSeleccionada = vigenciasContratos.get(0);
        }

        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
        contarRegistros();

        vigenciaSeleccionada = null;
    }

    //MOSTRAR DATOS CELDA
    /**
     * Metodo que muestra los dialogos de editar con respecto a la lista real o
     * la lista filtrada y a la columna
     */
    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        //Si no hay registro selecciionado
        if (vigenciaSeleccionada == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else {
            editarVC = vigenciaSeleccionada;

            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicial");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicial').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinal");
                RequestContext.getCurrentInstance().execute("PF('editarFechaFinal').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarContrato");
                RequestContext.getCurrentInstance().execute("PF('editarContrato').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoContrato");
                RequestContext.getCurrentInstance().execute("PF('editarTipoContrato').show()");
                cualCelda = -1;
            }
        }
    }

    //CREAR VU
    /**
     * Metodo que se encarga de agregar un nueva VigenciasContratos
     */
    public void agregarNuevaVC() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaVigencia.getFechainicial() != null && nuevaVigencia.getContrato().getSecuencia() != null) {
            if (validarFechasRegistro(1)) {

                if (bandera == 1) {
                    //CERRAR FILTRADO
                    FacesContext c = FacesContext.getCurrentInstance();
                    vcFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFechaInicial");
                    vcFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    vcFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFechaFinal");
                    vcFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    vcContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcContrato");
                    vcContrato.setFilterStyle("display: none; visibility: hidden;");
                    vcTipoContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcTipoContrato");
                    vcTipoContrato.setFilterStyle("display: none; visibility: hidden;");
                    altoTabla = "295";
                    RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
                    bandera = 0;
                    filtrarVC = null;
                    tipoLista = 0;
                }
                //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
                paraNuevaVContrato++;
                nuevaVContratoSecuencia = BigInteger.valueOf(paraNuevaVContrato);
                nuevaVigencia.setSecuencia(nuevaVContratoSecuencia);
                nuevaVigencia.setEmpleado(empleado);
                listVCCrear.add(nuevaVigencia);
                vigenciasContratos.add(nuevaVigencia);
                vigenciaSeleccionada = vigenciasContratos.get(vigenciasContratos.indexOf(nuevaVigencia));

                activarLOV = true;
                RequestContext.getCurrentInstance().update("form:listaValores");
                nuevaVigencia = new VigenciasContratos();
                nuevaVigencia.setContrato(new Contratos());
                nuevaVigencia.setTipocontrato(new TiposContratos());
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVC').hide()");
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }
    //LIMPIAR NUEVO REGISTRO

    /**
     * Metodo que limpia las casillas de la nueva vigencia
     */
    public void limpiarNuevaVC() {
        nuevaVigencia = new VigenciasContratos();
        nuevaVigencia.setContrato(new Contratos());
        nuevaVigencia.setTipocontrato(new TiposContratos());
    }
    //DUPLICAR VC

    /**
     * Metodo que duplica una vigencia especifica dado por la posicion de la
     * fila
     */
    public void duplicarVigenciaC() {
        RequestContext context = RequestContext.getCurrentInstance();
        //Si no hay registro selecciionado
        if (vigenciaSeleccionada == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else {
            duplicarVC = new VigenciasContratos();

            duplicarVC.setEmpleado(vigenciaSeleccionada.getEmpleado());
            duplicarVC.setFechainicial(vigenciaSeleccionada.getFechainicial());
            duplicarVC.setFechafinal(vigenciaSeleccionada.getFechafinal());
            duplicarVC.setContrato(vigenciaSeleccionada.getContrato());
            duplicarVC.setTipocontrato(vigenciaSeleccionada.getTipocontrato());

            if (duplicarVC.getTipocontrato() == null) {
                duplicarVC.setTipocontrato(new TiposContratos());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVC");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVC').show()");
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * VigenciasContratos
     */
    public void confirmarDuplicar() {
        if (duplicarVC.getFechainicial() != null && duplicarVC.getContrato().getSecuencia() != null) {
            if (validarFechasRegistro(2)) {
                paraNuevaVContrato++;
                nuevaVContratoSecuencia = BigInteger.valueOf(paraNuevaVContrato);
                vigenciasContratos.add(duplicarVC);
                duplicarVC.setSecuencia(nuevaVContratoSecuencia);
                listVCCrear.add(duplicarVC);
                vigenciaSeleccionada = vigenciasContratos.get(vigenciasContratos.lastIndexOf(duplicarVC));
                RequestContext context = RequestContext.getCurrentInstance();
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVC').hide()");
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                if (bandera == 1) {
                    //CERRAR FILTRADO
                    FacesContext c = FacesContext.getCurrentInstance();
                    vcFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVRLEmpleado:vcFechaInicial");
                    vcFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    vcFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFechaFinal");
                    vcFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    vcContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcContrato");
                    vcContrato.setFilterStyle("display: none; visibility: hidden;");
                    vcTipoContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcTipoContrato");
                    vcTipoContrato.setFilterStyle("display: none; visibility: hidden;");
                    altoTabla = "295";
                    RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
                    bandera = 0;
                    filtrarVC = null;
                    tipoLista = 0;
                }
                activarLOV = true;
                RequestContext.getCurrentInstance().update("form:listaValores");
                duplicarVC = new VigenciasContratos();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }
    //LIMPIAR DUPLICAR

    /**
     * Metodo que limpia los datos de un duplicar Vigencia
     */
    public void limpiarduplicarVC() {
        duplicarVC = new VigenciasContratos();
        duplicarVC.setContrato(new Contratos());
        duplicarVC.setTipocontrato(new TiposContratos());
    }

    //BORRAR VC
    /**
     * Metodo que borra las vigencias seleccionadas
     */
    public void borrarVC() {
        RequestContext context = RequestContext.getCurrentInstance();
        //Si no hay registro selecciionado
        if (vigenciaSeleccionada == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else {
            if (!listVCModificar.isEmpty() && listVCModificar.contains(vigenciaSeleccionada)) {
                int modIndex = listVCModificar.indexOf(vigenciaSeleccionada);
                listVCModificar.remove(modIndex);
                listVCBorrar.add(vigenciaSeleccionada);
            } else if (!listVCCrear.isEmpty() && listVCCrear.contains(vigenciaSeleccionada)) {
                int crearIndex = listVCCrear.indexOf(vigenciaSeleccionada);
                listVCCrear.remove(crearIndex);
            } else {
                listVCBorrar.add(vigenciaSeleccionada);
            }
            vigenciasContratos.remove(vigenciaSeleccionada);
            if (tipoLista == 1) {
                filtrarVC.remove(vigenciaSeleccionada);
            }
            contarRegistros();

            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            vigenciaSeleccionada = null;
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void anularLOV() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }
    //CTRL + F11 ACTIVAR/DESACTIVAR

    /**
     * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            vcFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFechaInicial");
            vcFechaInicial.setFilterStyle("width: 86% !important");
            vcFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFechaFinal");
            vcFechaFinal.setFilterStyle("width: 86% !important");
            vcContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcContrato");
            vcContrato.setFilterStyle("width: 86% !important");
            vcTipoContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcTipoContrato");
            vcTipoContrato.setFilterStyle("width: 86% !important");
            altoTabla = "275";
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            bandera = 1;
        } else if (bandera == 1) {
            vcFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFechaInicial");
            vcFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vcFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFechaFinal");
            vcFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            vcContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcContrato");
            vcContrato.setFilterStyle("display: none; visibility: hidden;");
            vcTipoContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcTipoContrato");
            vcTipoContrato.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "295";
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            bandera = 0;
            filtrarVC = null;
            tipoLista = 0;
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            vcFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFechaInicial");
            vcFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vcFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFechaFinal");
            vcFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            vcContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcContrato");
            vcContrato.setFilterStyle("display: none; visibility: hidden;");
            vcTipoContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcTipoContrato");
            vcTipoContrato.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "295";
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            bandera = 0;
            filtrarVC = null;
            tipoLista = 0;
        }
        limpiarListasValor();
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        listVCBorrar.clear();
        listVCCrear.clear();
        listVCModificar.clear();
        vigenciaSeleccionada = null;
        paraNuevaVContrato = 0;
        vigenciasContratos = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }
    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO) (list = CONTRATOS - TIPOSCONTRATOS)

    /**
     * Metodo que ejecuta los dialogos de contratos y tipos contratos
     *
     * @param indice Fila de la tabla
     * @param list Lista filtrada - Lista real
     * @param LND Tipo actualizacion = LISTA - NUEVO - DUPLICADO
     */
    public void asignarIndex(VigenciasContratos vContratos, int list, int LND) {
        vigenciaSeleccionada = vContratos;
        RequestContext context = RequestContext.getCurrentInstance();
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }

        if (list == 0) {
            contarRegistrosC();
            RequestContext.getCurrentInstance().update("form:ContratosDialogo");
            RequestContext.getCurrentInstance().execute("PF('ContratosDialogo').show()");

        } else if (list == 1) {
            contarRegistrosTC();
            RequestContext.getCurrentInstance().update("form:TiposContratoDialogo");
            RequestContext.getCurrentInstance().execute("PF('TiposContratoDialogo').show()");
        }
    }

    //LOVS
    //CONTRATO
    /**
     * Metodo que actualiza el contrato seleccionado
     */
    public void actualizarContrato() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
            vigenciaSeleccionada.setContrato(contratoSelecionado);
            if (!listVCCrear.contains(vigenciaSeleccionada)) {
                if (listVCModificar.isEmpty()) {
                    listVCModificar.add(vigenciaSeleccionada);
                } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
                    listVCModificar.add(vigenciaSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {// Para crear registro
            nuevaVigencia.setContrato(contratoSelecionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
        } else if (tipoActualizacion == 2) {// Para duplicar registro
            duplicarVC.setContrato(contratoSelecionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVC");
        }
        filtradoContratos = null;
        contratoSelecionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        /*
         * RequestContext.getCurrentInstance().update("form:ContratosDialogo");
         * RequestContext.getCurrentInstance().update("form:lovContratos"); RequestContext.getCurrentInstance().update("form:aceptarC");
         */
        context.reset("form:lovContratos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovContratos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ContratosDialogo').hide()");
    }

    /**
     * Metodo que cancela los cambios sobre contrato
     */
    public void cancelarCambioContrato() {
        filtradoContratos = null;
        contratoSelecionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovContratos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovContratos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ContratosDialogo').hide()");
    }

    //TIPO CONTRATO
    /**
     * Metodo que actualiza el tipo contrato seleccionado
     */
    public void actualizarTipoContrato() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
            vigenciaSeleccionada.setTipocontrato(tipoContratoSelecionado);
            if (!listVCCrear.contains(vigenciaSeleccionada)) {
                if (listVCModificar.isEmpty()) {
                    listVCModificar.add(vigenciaSeleccionada);
                } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
                    listVCModificar.add(vigenciaSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {// Para crear registro
            nuevaVigencia.setTipocontrato(tipoContratoSelecionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
        } else if (tipoActualizacion == 2) {// Para duplicar registro
            duplicarVC.setTipocontrato(tipoContratoSelecionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVC");
        }
        filtradoTiposContratos = null;
        tipoContratoSelecionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("form:lovTiposContratos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposContratos').clearFilters()");
        RequestContext.getCurrentInstance().update("form:lovTiposContratos");
        RequestContext.getCurrentInstance().execute("PF('TiposContratoDialogo').hide()");
    }

    /**
     * Metodo que cancela la seleccion del tipo contrato seleccionado
     */
    public void cancelarCambioTipoContrato() {
        filtradoTiposContratos = null;
        tipoContratoSelecionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTiposContratos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposContratos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TiposContratoDialogo').hide()");
    }

    //LISTA DE VALORES DINAMICA
    /**
     * Metodo que activa la lista de valores de la tabla con respecto a la
     * columna tipos contratos o contratos
     */
    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        //Si no hay registro selecciionado
        if (vigenciaSeleccionada == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else {
            if (cualCelda == 2) {
                contarRegistrosC();
                RequestContext.getCurrentInstance().update("form:ContratosDialogo");
                RequestContext.getCurrentInstance().execute("PF('ContratosDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 3) {
                contarRegistrosTC();
                RequestContext.getCurrentInstance().update("form:TiposContratoDialogo");
                RequestContext.getCurrentInstance().execute("PF('TiposContratoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    /**
     * Metodo que activa el boton aceptar de la pagina y los dialogos
     */
    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    /**
     * Metodo que exporta datos a PDF
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVCEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasContratosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVCEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasContratosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        //Si hay registro seleccionado
        if (vigenciaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASCONTRATOS");
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
        } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASCONTRATOS")) {
            //RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    //EVENTO FILTRAR
    /**
     * Evento que cambia la lista reala a la filtrada
     */
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        vigenciaSeleccionada = null;
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosC() {
        RequestContext.getCurrentInstance().update("form:infoRegistroContrato");
    }

    public void contarRegistrosTC() {
        RequestContext.getCurrentInstance().update("form:infoRegistroTipoContrato");
    }

    public void recordarSeleccion() {
        if (vigenciaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVCEmpleado");
            tablaC.setSelection(vigenciaSeleccionada);
            log.info("vigenciaSeleccionada: " + vigenciaSeleccionada);
        }
    }
    //GETTERS AND SETTERS

    /**
     * Metodo que obtiene las VigenciasContratos de un empleado, en caso de ser
     * null por medio del administrar hace el llamado para almacenarlo
     *
     * @return listVC Lista Vigencias Contratos
     */
    public List<VigenciasContratos> getVigenciasContratos() {
        try {
            if (vigenciasContratos == null) {
                vigenciasContratos = administrarVigenciasContratos.VigenciasContratosEmpleado(empleado.getSecuencia());
                if (vigenciasContratos != null) {
                    for (VigenciasContratos recVTC : vigenciasContratos) {
                        if (recVTC.getTipocontrato() == null) {
                            recVTC.setTipocontrato(new TiposContratos());
                        }
                    }
                }
            }
            return vigenciasContratos;
        } catch (Exception e) {
            log.warn("Error....................!!!!!!!!!!!! getVigenciasContratos ");
            return null;
        }
    }

    public void setVigenciasContratos(List<VigenciasContratos> vigenciasContratos) {
        this.vigenciasContratos = vigenciasContratos;
    }

    /**
     * Metodo que obtiene el empleado usando en el momento, en caso de ser null
     * por medio del administrar obtiene el valor
     *
     * @return empl Empleado que esta siendo usando en el momento
     */
    public Empleados getEmpleado() {
        return empleado;
    }

    public List<VigenciasContratos> getFiltrarVC() {
        return filtrarVC;
    }

    public void setFiltrarVC(List<VigenciasContratos> filtrarVC) {
        this.filtrarVC = filtrarVC;
    }

    public VigenciasContratos getNuevaVigencia() {
        return nuevaVigencia;
    }

    public void setNuevaVigencia(VigenciasContratos nuevaVigencia) {
        this.nuevaVigencia = nuevaVigencia;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    /**
     * Metodo que obtiene la lista de contratos, en caso de ser null por medio
     * del administrar los obtiene
     *
     * @return listTC Lista Tipos Contratos
     */
    public List<Contratos> getLovContratos() {
        if (lovContratos == null) {
            lovContratos = administrarVigenciasContratos.contratos();
        }
        return lovContratos;
    }

    public void setLovContratos(List<Contratos> lovContratos) {
        this.lovContratos = lovContratos;
    }

    public List<Contratos> getFiltradoContratos() {
        return filtradoContratos;
    }

    public void setFiltradoContratos(List<Contratos> filtradoContratos) {
        this.filtradoContratos = filtradoContratos;
    }

    /**
     * Metodo que obtiene los tipos contratos, en caso de ser null por medio del
     * administrar obtiene los valores
     *
     * @return listTC Lista Tipos Contratos
     */
    public List<TiposContratos> getLovTiposContratos() {
        if (lovTiposContratos == null) {
            lovTiposContratos = administrarVigenciasContratos.tiposContratos();
        }
        return lovTiposContratos;
    }

    public void setLovTiposContratos(List<TiposContratos> lovTiposContratos) {
        this.lovTiposContratos = lovTiposContratos;
    }

    public List<TiposContratos> getFiltradoTiposContratos() {
        return filtradoTiposContratos;
    }

    public void setFiltradoTiposContratos(List<TiposContratos> filtradoTiposContratos) {
        this.filtradoTiposContratos = filtradoTiposContratos;
    }

    public VigenciasContratos getEditarVC() {
        return editarVC;
    }

    public void setEditarVC(VigenciasContratos editarVC) {
        this.editarVC = editarVC;
    }

    public VigenciasContratos getDuplicarVC() {
        return duplicarVC;
    }

    public void setDuplicarVC(VigenciasContratos duplicarVC) {
        this.duplicarVC = duplicarVC;
    }

    public Contratos getContratoSelecionado() {
        return contratoSelecionado;
    }

    public void setContratoSelecionado(Contratos contratoSelecionado) {
        this.contratoSelecionado = contratoSelecionado;
    }

    public TiposContratos getTipoContratoSelecionado() {
        return tipoContratoSelecionado;
    }

    public void setTipoContratoSelecionado(TiposContratos tipoContratoSelecionado) {
        this.tipoContratoSelecionado = tipoContratoSelecionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public VigenciasContratos getVigenciaSeleccionada() {
        return vigenciaSeleccionada;
    }

    public void setVigenciaSeleccionada(VigenciasContratos vigenciaSeleccionada) {
        this.vigenciaSeleccionada = vigenciaSeleccionada;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVCEmpleado");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public String getInfoRegistroContrato() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovContratos");
        infoRegistroContrato = String.valueOf(tabla.getRowCount());
        return infoRegistroContrato;
    }

    public String getInfoRegistroTipoContrato() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTiposContratos");
        infoRegistroTipoContrato = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoContrato;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
