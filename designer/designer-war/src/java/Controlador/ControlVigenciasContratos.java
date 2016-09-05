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
public class ControlVigenciasContratos implements Serializable {

    @EJB
    AdministrarVigenciasContratosInterface administrarVigenciasContratos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Vigencias Contratos
    private List<VigenciasContratos> vigenciasContratos;
    private List<VigenciasContratos> filtrarVC;
    private VigenciasContratos vigenciaSeleccionada;
    //Contratos
    private List<Contratos> listaContratos;
    private Contratos contratoSelecionado;
    private List<Contratos> filtradoContratos;
    //Tipos Contratos
    private List<TiposContratos> listaTiposContratos;
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

    public ControlVigenciasContratos() {
        vigenciasContratos = null;
        listaContratos = null;
        listaTiposContratos = null;
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
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciasContratos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlVigenciasCargos: " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //EMPLEADO DE LA VIGENCIA
    /**
     * Metodo que recibe la secuencia empleado desde la pagina anterior y
     * obtiene el empleado referenciado
     *
     * @param sec Secuencia del Empleado
     */
    public void recibirEmpleado(Empleados empl) {
        RequestContext context = RequestContext.getCurrentInstance();

        vigenciasContratos = null;
        empleado = empl;

        getVigenciasContratos();
        //INICIALIZAR BOTONES NAVEGACION
        if (vigenciasContratos != null) {
            vigenciaSeleccionada = vigenciasContratos.get(0);
            modificarInfoRegistro(vigenciasContratos.size());
        } else {
            modificarInfoRegistro(0);
        }
        context.update("form:informacionRegistro");
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
                context.update("form:ACEPTAR");
            }
        }
    }

    public boolean validarFechasRegistro(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        System.err.println("fechaparametro : " + fechaParametro);
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
                context.update("form:datosVCEmpleado");
                context.execute("errorFechas.show()");
            }
        } else {
            vigenciaSeleccionada.setFechainicial(fechaIni);

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVCEmpleado");
            context.execute("errorRegNew.show()");
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

            for (int i = 0; i < listaContratos.size(); i++) {
                if (listaContratos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaSeleccionada.setContrato(listaContratos.get(indiceUnicoElemento));

                listaContratos.clear();
                getListaContratos();
            } else {
                permitirIndex = false;
                getInfoRegistroContrato();
                context.update("form:ContratosDialogo");
                context.execute("ContratosDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("TIPOCONTRATO")) {
            if (!valorConfirmar.isEmpty()) {
                vigenciaSeleccionada.getTipocontrato().setNombre(tipoContrato);

                for (int i = 0; i < listaTiposContratos.size(); i++) {
                    if (listaTiposContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaSeleccionada.setTipocontrato(listaTiposContratos.get(indiceUnicoElemento));

                    listaTiposContratos.clear();
                    getListaTiposContratos();
                } else {
                    permitirIndex = false;
                    getInfoRegistroTipoContrato();
                    context.update("form:TiposContratoDialogo");
                    context.execute("TiposContratoDialogo.show()");
                    tipoActualizacion = 0;
                }
            } else {
                listaTiposContratos.clear();
                getListaTiposContratos();
                vigenciaSeleccionada.setTipocontrato(new TiposContratos());

                if (guardado) {
                    guardado = false;
                    context.update("form:ACEPTAR");
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
                    context.update("form:ACEPTAR");
                }
            }
        }
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
        context.update("form:datosVCEmpleado");
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
            for (int i = 0; i < listaContratos.size(); i++) {
                if (listaContratos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigencia.setContrato(listaContratos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaCentroCosto");
                } else if (tipoNuevo == 2) {
                    duplicarVC.setContrato(listaContratos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarCentroCosto");
                }
                listaContratos.clear();
                getListaContratos();
            } else {
                context.update("form:ContratosDialogo");
                context.execute("ContratosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaLegislacionLaboral");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarLegislacionLaboral");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TIPOCONTRATO")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaVigencia.getTipocontrato().setNombre(tipoContrato);
                } else if (tipoNuevo == 2) {
                    duplicarVC.getTipocontrato().setNombre(tipoContrato);
                }
                for (int i = 0; i < listaTiposContratos.size(); i++) {
                    if (listaTiposContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaVigencia.setTipocontrato(listaTiposContratos.get(indiceUnicoElemento));
                        context.update("formularioDialogos:nuevoTipoContrato");
                    } else if (tipoNuevo == 2) {
                        duplicarVC.setTipocontrato(listaTiposContratos.get(indiceUnicoElemento));
                        context.update("formularioDialogos:duplicarTipoContrato");
                    }
                    listaTiposContratos.clear();
                    getListaTiposContratos();
                } else {
                    context.update("form:TiposContratoDialogo");
                    context.execute("TiposContratoDialogo.show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        context.update("formularioDialogos:nuevoTipoContrato");
                    } else if (tipoNuevo == 2) {
                        context.update("formularioDialogos:duplicarTipoContrato");
                    }
                }
            }
        } else {
            listaTiposContratos.clear();
            getListaTiposContratos();
            if (tipoNuevo == 1) {
                nuevaVigencia.setTipocontrato(new TiposContratos());
                context.update("formularioDialogos:nuevoTipoContrato");
            } else if (tipoNuevo == 2) {
                duplicarVC.setTipocontrato(new TiposContratos());
                context.update("formularioDialogos:duplicarTipoContrato");
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
            RequestContext context = RequestContext.getCurrentInstance();
            vigenciasContratos = null;
            getVigenciasContratos();
            if (vigenciasContratos != null) {
                vigenciaSeleccionada = vigenciasContratos.get(0);
                modificarInfoRegistro(vigenciasContratos.size());
            } else {
                modificarInfoRegistro(0);
            }
            context.update("form:datosVCEmpleado");
            context.update("form:informacionRegistro");
            guardado = true;
            context.update("form:ACEPTAR");
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            paraNuevaVContrato = 0;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
        vigenciaSeleccionada = null;
    }
    //CANCELAR MODIFICACIONES

    /**
     * Cancela las modificaciones realizas en la pagina
     */
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
            context.update("form:datosVCEmpleado");
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
            modificarInfoRegistro(vigenciasContratos.size());
        } else {
            modificarInfoRegistro(0);
        }

        guardado = true;
        context.update("form:ACEPTAR");
        context.update("form:datosVCEmpleado");
        context.update("form:informacionRegistro");

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
            context.execute("seleccionarRegistro.show()");
        } else {
            editarVC = vigenciaSeleccionada;

            if (cualCelda == 0) {
                context.update("formularioDialogos:editarFechaInicial");
                context.execute("editarFechaInicial.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editarFechaFinal");
                context.execute("editarFechaFinal.show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                context.update("formularioDialogos:editarContrato");
                context.execute("editarContrato.show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                context.update("formularioDialogos:editarTipoContrato");
                context.execute("editarTipoContrato.show()");
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
                modificarInfoRegistro(vigenciasContratos.size());
                context.update("form:informacionRegistro");
                context.update("form:datosVCEmpleado");
                context.execute("NuevoRegistroVC.hide()");
                if (guardado) {
                    guardado = false;
                    context.update("form:ACEPTAR");
                }
            } else {
                context.execute("errorFechas.show()");
            }
        } else {
            context.execute("errorRegNew.show()");
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
            context.execute("seleccionarRegistro.show()");
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
            context.update("formularioDialogos:duplicarVC");
            context.execute("DuplicarRegistroVC.show()");
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
                modificarInfoRegistro(vigenciasContratos.size());
                context.update("form:informacionRegistro");
                context.update("form:datosVCEmpleado");
                context.execute("DuplicarRegistroVC.hide()");
                if (guardado) {
                    guardado = false;
                    context.update("form:ACEPTAR");
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
                context.execute("errorFechas.show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorRegNew.show()");
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
            context.execute("seleccionarRegistro.show()");
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
            modificarInfoRegistro(vigenciasContratos.size());

            context.update("form:datosVCEmpleado");
            context.update("form:informacionRegistro");
            vigenciaSeleccionada = null;
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
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
            vcFechaInicial.setFilterStyle("width: 86%");
            vcFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFechaFinal");
            vcFechaFinal.setFilterStyle("width: 86%");
            vcContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcContrato");
            vcContrato.setFilterStyle("width: 86%");
            vcTipoContrato = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcTipoContrato");
            vcTipoContrato.setFilterStyle("width: 86%");
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
        RequestContext context = RequestContext.getCurrentInstance();
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
            context.update("form:datosVCEmpleado");
            bandera = 0;
            filtrarVC = null;
            tipoLista = 0;
        }

        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        listVCBorrar.clear();
        listVCCrear.clear();
        listVCModificar.clear();
        vigenciaSeleccionada = null;
        paraNuevaVContrato = 0;
        vigenciasContratos = null;
        guardado = true;
        context.update("form:ACEPTAR");
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
            if (listaContratos != null) {
                modificarInfoRegistroC(listaContratos.size());
            } else {
                modificarInfoRegistroC(0);
            }
            context.update("form:ContratosDialogo");
            context.execute("ContratosDialogo.show()");

        } else if (list == 1) {
            if (listaTiposContratos != null) {
                modificarInfoRegistroTC(listaTiposContratos.size());
            } else {
                modificarInfoRegistroTC(0);
            }
            context.update("form:TiposContratoDialogo");
            context.execute("TiposContratoDialogo.show()");
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
                context.update("form:ACEPTAR");
            }
            context.update("form:datosVCEmpleado");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {// Para crear registro
            nuevaVigencia.setContrato(contratoSelecionado);
            context.update("formularioDialogos:nuevaVC");
        } else if (tipoActualizacion == 2) {// Para duplicar registro
            duplicarVC.setContrato(contratoSelecionado);
            context.update("formularioDialogos:duplicarVC");
        }
        filtradoContratos = null;
        contratoSelecionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        /*
         * context.update("form:ContratosDialogo");
         * context.update("form:lovContratos"); context.update("form:aceptarC");
         */
        context.reset("form:lovContratos:globalFilter");
        context.execute("lovContratos.clearFilters()");
        context.execute("ContratosDialogo.hide()");
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
        context.execute("lovContratos.clearFilters()");
        context.execute("ContratosDialogo.hide()");
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
                context.update("form:ACEPTAR");
            }
            context.update("form:datosVCEmpleado");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {// Para crear registro
            nuevaVigencia.setTipocontrato(tipoContratoSelecionado);
            context.update("formularioDialogos:nuevaVC");
        } else if (tipoActualizacion == 2) {// Para duplicar registro
            duplicarVC.setTipocontrato(tipoContratoSelecionado);
            context.update("formularioDialogos:duplicarVC");
        }
        filtradoTiposContratos = null;
        tipoContratoSelecionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("form:lovTiposContratos:globalFilter");
        context.execute("lovTiposContratos.clearFilters()");
        context.update("form:lovTiposContratos");
        context.execute("TiposContratoDialogo.hide()");
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
        context.execute("lovTiposContratos.clearFilters()");
        context.execute("TiposContratoDialogo.hide()");
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
            context.execute("seleccionarRegistro.show()");
        } else {
            if (cualCelda == 2) {
                if (listaContratos != null) {
                    modificarInfoRegistroC(listaContratos.size());
                } else {
                    modificarInfoRegistroC(0);
                }
                context.update("form:ContratosDialogo");
                context.execute("ContratosDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 3) {
                if (listaTiposContratos != null) {
                    modificarInfoRegistroTC(listaTiposContratos.size());
                } else {
                    modificarInfoRegistroTC(0);
                }
                context.update("form:TiposContratoDialogo");
                context.execute("TiposContratoDialogo.show()");
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
                context.execute("errorObjetosDB.show()");
            } else if (resultado == 2) {
                context.execute("confirmarRastro.show()");
            } else if (resultado == 3) {
                context.execute("errorRegistroRastro.show()");
            } else if (resultado == 4) {
                context.execute("errorTablaConRastro.show()");
            } else if (resultado == 5) {
                context.execute("errorTablaSinRastro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("VIGENCIASCONTRATOS")) {
                //context.update("form:confirmarRastroHistorico");
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }
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
        modificarInfoRegistro(filtrarVC.size());
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void eventoFiltrarC() {
        modificarInfoRegistroC(filtradoContratos.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroContrato");
    }

    public void eventoFiltrarTC() {
        modificarInfoRegistroTC(filtradoTiposContratos.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroTipoContrato");
    }

    private void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    private void modificarInfoRegistroC(int valor) {
        infoRegistroContrato = String.valueOf(valor);
    }

    private void modificarInfoRegistroTC(int valor) {
        infoRegistroTipoContrato = String.valueOf(valor);
    }

    public void recordarSeleccion() {
        if (vigenciaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVCEmpleado");
            tablaC.setSelection(vigenciaSeleccionada);
            System.out.println("vigenciaSeleccionada: " + vigenciaSeleccionada);
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
            }
            return vigenciasContratos;
        } catch (Exception e) {
            System.out.println("Error....................!!!!!!!!!!!! getVigenciasContratos ");
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
    public List<Contratos> getListaContratos() {
        if (listaContratos == null) {
            listaContratos = administrarVigenciasContratos.contratos();
        }
        return listaContratos;
    }

    public void setListaContratos(List<Contratos> listaContratos) {
        this.listaContratos = listaContratos;
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
    public List<TiposContratos> getListaTiposContratos() {
        if (listaTiposContratos == null) {
            listaTiposContratos = administrarVigenciasContratos.tiposContratos();
        }
        return listaTiposContratos;
    }

    public void setListaTiposContratos(List<TiposContratos> listaTiposContratos) {
        this.listaTiposContratos = listaTiposContratos;
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
        return infoRegistro;
    }

    public String getInfoRegistroContrato() {
        return infoRegistroContrato;
    }

    public String getInfoRegistroTipoContrato() {
        return infoRegistroTipoContrato;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
