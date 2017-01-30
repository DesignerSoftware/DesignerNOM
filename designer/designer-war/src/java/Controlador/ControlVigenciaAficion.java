/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Aficiones;
import Entidades.Empleados;
import Entidades.VigenciasAficiones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciaAficionInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlVigenciaAficion implements Serializable {

    @EJB
    AdministrarVigenciaAficionInterface administrarVigenciaAficion;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    ////
    private List<VigenciasAficiones> listVigenciasAficiones;
    private List<VigenciasAficiones> filtrarListVigenciasAficiones;
    private VigenciasAficiones vigenciaTablaSeleccionada;

    private List<Aficiones> listAficiones;
    private Aficiones aficionSeleccionada;
    private List<Aficiones> filtrarListAficiones;
    private int tipoActualizacion;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Columnas Tabla VC
    private Column veFechaInicial, veFechaFinal, veDescripcion, veIndividual, veCIndividual, veGrupal, veCGrupal;
    //Otros
    private boolean aceptar;
    //modificar
    private List<VigenciasAficiones> listVigenciaAficionModificar;
    private boolean guardado;
    //crear VC
    public VigenciasAficiones nuevaVigenciaAficion;
    private List<VigenciasAficiones> listVigenciaAficionCrear;
    private BigInteger l;
    private int k;
    //borrar VC
    private List<VigenciasAficiones> listVigenciaAficionBorrar;
    //editar celda
    private VigenciasAficiones editarVigenciaAficion;
    private int cualCelda, tipoLista;
    //duplicar
    private VigenciasAficiones duplicarVigenciaAficion;
    private String aficion;
    private boolean permitirIndex, activarLov;

    private Empleados empleado;
    private Date fechaParametro;
    private Date fechaIni, fechaFin;
    //
    private String infoRegistroAficion;
    private String infoRegistro;
    private String altoTabla;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlVigenciaAficion() {
        altoTabla = "300";
        listVigenciasAficiones = null;
        listAficiones = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listVigenciaAficionBorrar = new ArrayList<VigenciasAficiones>();
        //crear aficiones
        listVigenciaAficionCrear = new ArrayList<VigenciasAficiones>();
        k = 0;
        //modificar aficiones
        listVigenciaAficionModificar = new ArrayList<VigenciasAficiones>();
        //editar
        editarVigenciaAficion = new VigenciasAficiones();
        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevaVigenciaAficion = new VigenciasAficiones();
        nuevaVigenciaAficion.setAficion(new Aficiones());
        vigenciaTablaSeleccionada = null;
        permitirIndex = true;
        activarLov = true;
        empleado = new Empleados();
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciaAficion.obtenerConexion(ses.getId());
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
            String pagActual = "vigenciaaficion";
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

    public void recibirEmpleado(BigInteger secuencia) {
        listVigenciasAficiones = null;
        listAficiones = null;
        empleado = administrarVigenciaAficion.empleadoActual(secuencia);
        getListVigenciasAficiones();
        if (listVigenciasAficiones != null) {
            if (!listVigenciasAficiones.isEmpty()) {
                vigenciaTablaSeleccionada = listVigenciasAficiones.get(0);
            }
        }
    }

    public void modificarVigenciaAficion(VigenciasAficiones vigaficion) {
        vigenciaTablaSeleccionada = vigaficion;
        if (tipoLista == 0) {
            if (!listVigenciaAficionCrear.contains(vigenciaTablaSeleccionada)) {
                if (listVigenciaAficionModificar.isEmpty()) {
                    listVigenciaAficionModificar.add(vigenciaTablaSeleccionada);
                } else if (!listVigenciaAficionModificar.contains(vigenciaTablaSeleccionada)) {
                    listVigenciaAficionModificar.add(vigenciaTablaSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else if (!listVigenciaAficionCrear.contains(vigenciaTablaSeleccionada)) {

            if (listVigenciaAficionModificar.isEmpty()) {
                listVigenciaAficionModificar.add(vigenciaTablaSeleccionada);
            } else if (!listVigenciaAficionModificar.contains(vigenciaTablaSeleccionada)) {
                listVigenciaAficionModificar.add(vigenciaTablaSeleccionada);
            }
            if (guardado == true) {
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
        boolean retorno = true;
        if (i == 0) {
            VigenciasAficiones auxiliar = null;
            if (tipoLista == 0) {
                auxiliar = vigenciaTablaSeleccionada;
            }
            if (tipoLista == 1) {
                auxiliar = vigenciaTablaSeleccionada;
            }
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
            if (nuevaVigenciaAficion.getFechafinal() != null) {
                if (nuevaVigenciaAficion.getFechainicial().after(fechaParametro) && nuevaVigenciaAficion.getFechainicial().before(nuevaVigenciaAficion.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            } else if (nuevaVigenciaAficion.getFechainicial().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarVigenciaAficion.getFechafinal() != null) {
                if (duplicarVigenciaAficion.getFechainicial().after(fechaParametro) && duplicarVigenciaAficion.getFechainicial().before(duplicarVigenciaAficion.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            } else if (duplicarVigenciaAficion.getFechainicial().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificarFechas(VigenciasAficiones vigenciaaficion, int c) {
        VigenciasAficiones auxiliar = null;
        if (tipoLista == 0) {
            auxiliar = vigenciaTablaSeleccionada;
        }
        if (tipoLista == 1) {
            auxiliar = vigenciaTablaSeleccionada;
        }
        if (auxiliar.getFechainicial() != null) {
            boolean retorno = false;
            if (auxiliar.getFechafinal() == null) {
                retorno = true;
            }
            if (auxiliar.getFechafinal() != null) {
                vigenciaTablaSeleccionada = vigenciaaficion;
                retorno = validarFechasRegistro(0);
            }
            if (retorno == true) {
                cambiarIndice(vigenciaaficion, c);
                modificarVigenciaAficion(vigenciaaficion);
            } else {
                if (tipoLista == 0) {
                    vigenciaTablaSeleccionada.setFechafinal(fechaFin);
                    vigenciaTablaSeleccionada.setFechainicial(fechaIni);
                }
                if (tipoLista == 1) {
                    vigenciaTablaSeleccionada.setFechafinal(fechaFin);
                    vigenciaTablaSeleccionada.setFechainicial(fechaIni);

                }
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
                RequestContext.getCurrentInstance().execute("PF('form:errorFechas').show()");
            }
        } else {
            if (tipoLista == 0) {
                vigenciaTablaSeleccionada.setFechainicial(fechaIni);
            }
            if (tipoLista == 1) {
                vigenciaTablaSeleccionada.setFechainicial(fechaIni);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    public void modificarVigenciaAficion(VigenciasAficiones vigenciaaficion, String confirmarCambio, String valorConfirmar) {
        vigenciaTablaSeleccionada = vigenciaaficion;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("AFICIONES")) {
            if (tipoLista == 0) {
                vigenciaTablaSeleccionada.getAficion().setDescripcion(aficion);
            } else {
                vigenciaTablaSeleccionada.getAficion().setDescripcion(aficion);
            }
            for (int i = 0; i < listAficiones.size(); i++) {
                if (listAficiones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaTablaSeleccionada.setAficion(listAficiones.get(indiceUnicoElemento));
                } else {
                    vigenciaTablaSeleccionada.setAficion(listAficiones.get(indiceUnicoElemento));
                }
                listAficiones.clear();
                getListAficiones();
            } else {
                permitirIndex = false;
                getInfoRegistroAficion();
                RequestContext.getCurrentInstance().update("form:AficionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listVigenciaAficionCrear.contains(vigenciaTablaSeleccionada)) {

                    if (listVigenciaAficionModificar.isEmpty()) {
                        listVigenciaAficionModificar.add(vigenciaTablaSeleccionada);
                    } else if (!listVigenciaAficionModificar.contains(vigenciaTablaSeleccionada)) {
                        listVigenciaAficionModificar.add(vigenciaTablaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else if (!listVigenciaAficionCrear.contains(vigenciaTablaSeleccionada)) {

                if (listVigenciaAficionModificar.isEmpty()) {
                    listVigenciaAficionModificar.add(vigenciaTablaSeleccionada);
                } else if (!listVigenciaAficionModificar.contains(vigenciaTablaSeleccionada)) {
                    listVigenciaAficionModificar.add(vigenciaTablaSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("AFICIONES")) {
            if (tipoNuevo == 1) {
                aficion = nuevaVigenciaAficion.getAficion().getDescripcion();
            } else if (tipoNuevo == 2) {
                aficion = duplicarVigenciaAficion.getAficion().getDescripcion();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("AFICIONES")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaAficion.getAficion().setDescripcion(aficion);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaAficion.getAficion().setDescripcion(aficion);
            }
            for (int i = 0; i < listAficiones.size(); i++) {
                if (listAficiones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaAficion.setAficion(listAficiones.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigencias");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaAficion.setAficion(listAficiones.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigencias");
                }
                listAficiones.clear();
                getListAficiones();
            } else {
                RequestContext.getCurrentInstance().update("form:AficionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigencias");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigencias");
                }
            }
        }
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void cambiarIndice(VigenciasAficiones vigenciaaficion, int celda) {
        if (permitirIndex == true) {
            vigenciaTablaSeleccionada = vigenciaaficion;
            cualCelda = celda;
            vigenciaTablaSeleccionada.getSecuencia();

            if (cualCelda == 0) {
                deshabilitarBotonLov();
                fechaIni = vigenciaTablaSeleccionada.getFechainicial();
            } else if (cualCelda == 1) {
                deshabilitarBotonLov();
                fechaFin = vigenciaTablaSeleccionada.getFechafinal();
            } else if (cualCelda == 2) {
                habilitarBotonLov();
                aficion = vigenciaTablaSeleccionada.getAficion().getDescripcion();
            } else if (cualCelda == 3) {
                deshabilitarBotonLov();
                vigenciaTablaSeleccionada.getValorcuantitativo();
            } else if (cualCelda == 4) {
                deshabilitarBotonLov();
                vigenciaTablaSeleccionada.getValorcualitativo();
            } else if (cualCelda == 5) {
                deshabilitarBotonLov();
                vigenciaTablaSeleccionada.getValorcuantitativogrupo();
            } else if (cualCelda == 6) {
                deshabilitarBotonLov();
                vigenciaTablaSeleccionada.getValorcualitativogrupo();

            }
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
                if (!listVigenciaAficionBorrar.isEmpty()) {
                    administrarVigenciaAficion.borrarVigenciasAficiones(listVigenciaAficionBorrar);
                    listVigenciaAficionBorrar.clear();
                }
                if (!listVigenciaAficionCrear.isEmpty()) {
                    administrarVigenciaAficion.crearVigenciasAficiones(listVigenciaAficionCrear);
                    listVigenciaAficionCrear.clear();
                }
                if (!listVigenciaAficionModificar.isEmpty()) {
                    administrarVigenciaAficion.editarVigenciasAficiones(listVigenciaAficionModificar);
                    listVigenciaAficionModificar.clear();
                }
                listVigenciasAficiones = null;
                getListVigenciasAficiones();
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:informacionRegistro");
                RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
            }
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con Éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }
    //CANCELAR MODIFICACIONES

    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            altoTabla = "300";
            veFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veFechaInicial");
            veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            veFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veFechaFinal");
            veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            veDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veDescripcion");
            veDescripcion.setFilterStyle("display: none; visibility: hidden;");
            veIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veIndividual");
            veIndividual.setFilterStyle("display: none; visibility: hidden;");
            veCIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veCIndividual");
            veCIndividual.setFilterStyle("display: none; visibility: hidden;");
            veGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veGrupal");
            veGrupal.setFilterStyle("display: none; visibility: hidden;");
            veCGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veCGrupal");
            veCGrupal.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
            bandera = 0;
            filtrarListVigenciasAficiones = null;
            tipoLista = 0;
        }

        listVigenciaAficionBorrar.clear();
        listVigenciaAficionCrear.clear();
        listVigenciaAficionModificar.clear();
        vigenciaTablaSeleccionada = null;
        k = 0;
        listVigenciasAficiones = null;
        guardado = true;
        getListVigenciasAficiones();
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    //MOSTRAR DATOS CELDA
    /**
     * Metodo que muestra los dialogos de editar con respecto a la lista real o
     * la lista filtrada y a la columna
     */
    public void editarCelda() {
        if (vigenciaTablaSeleccionada != null) {
            editarVigenciaAficion = vigenciaTablaSeleccionada;

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicialD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaFinalD').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionD");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionD').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarIndividualD");
                RequestContext.getCurrentInstance().execute("PF('editarIndividualD').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCIndividualD");
                RequestContext.getCurrentInstance().execute("PF('editarCIndividualD').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarGrupalD");
                RequestContext.getCurrentInstance().execute("PF('editarGrupalD').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCGrupalD");
                RequestContext.getCurrentInstance().execute("PF('editarCGrupalD').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    //CREAR VU
    /**
     * Metodo que se encarga de agregar un nueva VigenciaReformaLaboral
     */
    public void agregarNuevaVigenciaAficion() {
        if (nuevaVigenciaAficion.getFechainicial() != null && nuevaVigenciaAficion.getAficion() != null) {
            if (validarFechasRegistro(1) == true) {
                if (bandera == 1) {
                    //CERRAR FILTRADO
                    altoTabla = "300";
                    veFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veFechaInicial");
                    veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    veFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veFechaFinal");
                    veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    veDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veDescripcion");
                    veDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    veIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veIndividual");
                    veIndividual.setFilterStyle("display: none; visibility: hidden;");
                    veCIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veCIndividual");
                    veCIndividual.setFilterStyle("display: none; visibility: hidden;");
                    veGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veGrupal");
                    veGrupal.setFilterStyle("display: none; visibility: hidden;");
                    veCGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veCGrupal");
                    veCGrupal.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
                    bandera = 0;
                    filtrarListVigenciasAficiones = null;
                    tipoLista = 0;
                }
                //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
                k++;
                l = BigInteger.valueOf(k);
                nuevaVigenciaAficion.setSecuencia(l);
                nuevaVigenciaAficion.setPersona(empleado.getPersona());
                listVigenciaAficionCrear.add(nuevaVigenciaAficion);
                listVigenciasAficiones.add(nuevaVigenciaAficion);
                vigenciaTablaSeleccionada = nuevaVigenciaAficion;
                nuevaVigenciaAficion = new VigenciasAficiones();
                nuevaVigenciaAficion.setAficion(new Aficiones());
                contarRegistros();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:informacionRegistro");
                RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigencias').hide()");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }
    //LIMPIAR NUEVO REGISTRO

    /**
     * Metodo que limpia las casillas de la nueva vigencia
     */
    public void limpiarNuevaVigenciaAficion() {
        nuevaVigenciaAficion = new VigenciasAficiones();
        nuevaVigenciaAficion.setAficion(new Aficiones());
    }

    //DUPLICAR VC
    public void duplicarVigenciaAficionM() {
        if (vigenciaTablaSeleccionada != null) {
            duplicarVigenciaAficion = new VigenciasAficiones();

            if (tipoLista == 0) {

                duplicarVigenciaAficion.setAficion(vigenciaTablaSeleccionada.getAficion());
                duplicarVigenciaAficion.setFechafinal(vigenciaTablaSeleccionada.getFechafinal());
                duplicarVigenciaAficion.setFechainicial(vigenciaTablaSeleccionada.getFechainicial());
                duplicarVigenciaAficion.setPersona(vigenciaTablaSeleccionada.getPersona());
                duplicarVigenciaAficion.setValorcualitativo(vigenciaTablaSeleccionada.getValorcualitativo());
                duplicarVigenciaAficion.setValorcualitativogrupo(vigenciaTablaSeleccionada.getValorcualitativogrupo());
                duplicarVigenciaAficion.setValorcuantitativo(vigenciaTablaSeleccionada.getValorcuantitativo());
                duplicarVigenciaAficion.setValorcuantitativogrupo(vigenciaTablaSeleccionada.getValorcuantitativogrupo());

            }
            if (tipoLista == 1) {

                duplicarVigenciaAficion.setAficion(vigenciaTablaSeleccionada.getAficion());
                duplicarVigenciaAficion.setFechafinal(vigenciaTablaSeleccionada.getFechafinal());
                duplicarVigenciaAficion.setFechainicial(vigenciaTablaSeleccionada.getFechainicial());
                duplicarVigenciaAficion.setPersona(vigenciaTablaSeleccionada.getPersona());
                duplicarVigenciaAficion.setValorcualitativo(vigenciaTablaSeleccionada.getValorcualitativo());
                duplicarVigenciaAficion.setValorcualitativogrupo(vigenciaTablaSeleccionada.getValorcualitativogrupo());
                duplicarVigenciaAficion.setValorcuantitativo(vigenciaTablaSeleccionada.getValorcuantitativo());
                duplicarVigenciaAficion.setValorcuantitativogrupo(vigenciaTablaSeleccionada.getValorcuantitativogrupo());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigencias");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigencias').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * VigenciasReformasLaborales
     */
    public void confirmarDuplicar() {
        if (duplicarVigenciaAficion.getFechainicial() != null && duplicarVigenciaAficion.getAficion() != null) {
            if (validarFechasRegistro(2) == true) {
                duplicarVigenciaAficion.setPersona(empleado.getPersona());
                if (listVigenciasAficiones == null) {
                    listVigenciasAficiones = new ArrayList<VigenciasAficiones>();
                }
                k++;
                l = BigInteger.valueOf(k);
                duplicarVigenciaAficion.setSecuencia(l);
                listVigenciasAficiones.add(duplicarVigenciaAficion);
                listVigenciaAficionCrear.add(duplicarVigenciaAficion);
                contarRegistros();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:informacionRegistro");
                RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigencias').hide()");
                vigenciaTablaSeleccionada = null;
                vigenciaTablaSeleccionada = null;
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                if (bandera == 1) {
                    //CERRAR FILTRADO
                    altoTabla = "300";
                    veFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veFechaInicial");
                    veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    veFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veFechaFinal");
                    veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    veDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veDescripcion");
                    veDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    veIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veIndividual");
                    veIndividual.setFilterStyle("display: none; visibility: hidden;");
                    veCIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veCIndividual");
                    veCIndividual.setFilterStyle("display: none; visibility: hidden;");
                    veGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veGrupal");
                    veGrupal.setFilterStyle("display: none; visibility: hidden;");
                    veCGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veCGrupal");
                    veCGrupal.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
                    bandera = 0;
                    filtrarListVigenciasAficiones = null;
                    tipoLista = 0;
                }
                duplicarVigenciaAficion = new VigenciasAficiones();
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
    public void limpiarDuplicar() {
        duplicarVigenciaAficion = new VigenciasAficiones();
        duplicarVigenciaAficion.setAficion(new Aficiones());
    }

    //BORRAR VC
    /**
     * Metodo que borra las vigencias seleccionadas
     */
    public void borrarVigenciaAficion() {
        if (vigenciaTablaSeleccionada != null) {
            if (!listVigenciaAficionModificar.isEmpty() && listVigenciaAficionModificar.contains(vigenciaTablaSeleccionada)) {
                int modIndex = listVigenciaAficionModificar.indexOf(vigenciaTablaSeleccionada);
                listVigenciaAficionModificar.remove(modIndex);
                listVigenciaAficionBorrar.add(vigenciaTablaSeleccionada);
            } else if (!listVigenciaAficionCrear.isEmpty() && listVigenciaAficionCrear.contains(vigenciaTablaSeleccionada)) {
                int crearIndex = listVigenciaAficionCrear.indexOf(vigenciaTablaSeleccionada);
                listVigenciaAficionCrear.remove(crearIndex);
            } else {
                listVigenciaAficionBorrar.add(vigenciaTablaSeleccionada);
            }
            listVigenciasAficiones.remove(vigenciaTablaSeleccionada);
            if (tipoLista == 1) {
                filtrarListVigenciasAficiones.remove(vigenciaTablaSeleccionada);
            }

            getListVigenciasAficiones();
            contarRegistros();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
            vigenciaTablaSeleccionada = null;

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
            altoTabla = "280";
            veFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veFechaInicial");
            veFechaInicial.setFilterStyle("width: 85% !important");
            veFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veFechaFinal");
            veFechaFinal.setFilterStyle("width: 85% !important");
            veDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veDescripcion");
            veDescripcion.setFilterStyle("width: 85% !important;");
            veIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veIndividual");
            veIndividual.setFilterStyle("width: 85% !important;");
            veCIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veCIndividual");
            veCIndividual.setFilterStyle("width: 85% !important;");
            veGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veGrupal");
            veGrupal.setFilterStyle("width: 85% !important;");
            veCGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veCGrupal");
            veCGrupal.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "300";
            veFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veFechaInicial");
            veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            veFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veFechaFinal");
            veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            veDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veDescripcion");
            veDescripcion.setFilterStyle("display: none; visibility: hidden;");
            veIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veIndividual");
            veIndividual.setFilterStyle("display: none; visibility: hidden;");
            veCIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veCIndividual");
            veCIndividual.setFilterStyle("display: none; visibility: hidden;");
            veGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veGrupal");
            veGrupal.setFilterStyle("display: none; visibility: hidden;");
            veCGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veCGrupal");
            veCGrupal.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
            bandera = 0;
            filtrarListVigenciasAficiones = null;
            tipoLista = 0;
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (bandera == 1) {
            altoTabla = "300";
            veFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veFechaInicial");
            veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            veFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veFechaFinal");
            veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            veDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veDescripcion");
            veDescripcion.setFilterStyle("display: none; visibility: hidden;");
            veIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veIndividual");
            veIndividual.setFilterStyle("display: none; visibility: hidden;");
            veCIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veCIndividual");
            veCIndividual.setFilterStyle("display: none; visibility: hidden;");
            veGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veGrupal");
            veGrupal.setFilterStyle("display: none; visibility: hidden;");
            veCGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasAficiones:veCGrupal");
            veCGrupal.setFilterStyle("display: none; visibility: hidden;");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
            bandera = 0;
            filtrarListVigenciasAficiones = null;
            tipoLista = 0;
        }

        listVigenciaAficionBorrar.clear();
        listVigenciaAficionCrear.clear();
        listVigenciaAficionModificar.clear();
        vigenciaTablaSeleccionada = null;
        vigenciaTablaSeleccionada = null;
        k = 0;
        listVigenciasAficiones = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }
    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)

    /**
     * Metodo que ejecuta el dialogo de reforma laboral
     *
     * @param indice Fila de la tabla
     * @param list Lista filtrada - Lista real
     * @param LND Tipo actualizacion = LISTA - NUEVO - DUPLICADO
     */
    public void asignarIndex(VigenciasAficiones vigenciaaficion, int LND) {
        vigenciaTablaSeleccionada = vigenciaaficion;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        getInfoRegistroAficion();
        contarRegistrosAficiones();
        RequestContext.getCurrentInstance().update("form:AficionesDialogo");
        RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').show()");
    }

    //LOVS
    //CIUDAD
    /**
     * Metodo que actualiza la reforma laboral seleccionada
     */
    public void actualizarAficion() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaTablaSeleccionada.setAficion(aficionSeleccionada);
                if (!listVigenciaAficionCrear.contains(vigenciaTablaSeleccionada)) {
                    if (listVigenciaAficionModificar.isEmpty()) {
                        listVigenciaAficionModificar.add(vigenciaTablaSeleccionada);
                    } else if (!listVigenciaAficionModificar.contains(vigenciaTablaSeleccionada)) {
                        listVigenciaAficionModificar.add(vigenciaTablaSeleccionada);
                    }
                }
            } else {
                vigenciaTablaSeleccionada.setAficion(aficionSeleccionada);
                if (!listVigenciaAficionCrear.contains(vigenciaTablaSeleccionada)) {
                    if (listVigenciaAficionModificar.isEmpty()) {
                        listVigenciaAficionModificar.add(vigenciaTablaSeleccionada);
                    } else if (!listVigenciaAficionModificar.contains(vigenciaTablaSeleccionada)) {
                        listVigenciaAficionModificar.add(vigenciaTablaSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVigenciasAficiones");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaAficion.setAficion(aficionSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigencias");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaAficion.setAficion(aficionSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigencias");
        }
        filtrarListAficiones = null;
        aficionSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("form:AficionesDialogo");
        RequestContext.getCurrentInstance().update("form:lovAficiones");
        RequestContext.getCurrentInstance().update("form:aceptarA");

        context.reset("form:lovAficiones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovAficiones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').hide()");

    }

    /**
     * Metodo que cancela los cambios sobre reforma laboral
     */
    public void cancelarCambioAficion() {
        filtrarListAficiones = null;
        aficionSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();

        context.reset("form:lovAficiones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovAficiones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').hide()");
    }

    //LISTA DE VALORES DINAMICA
    /**
     * Metodo que activa la lista de valores de la tabla con respecto a las
     * reformas laborales
     */
    public void listaValoresBoton() {
        if (vigenciaTablaSeleccionada != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 2) {
                getInfoRegistroAficion();
                contarRegistrosAficiones();
                RequestContext.getCurrentInstance().update("form:AficionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').show()");
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
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasAficionesDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasAficionesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaTablaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(vigenciaTablaSeleccionada.getSecuencia(), "VIGENCIASAFICIONES");
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
        } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASAFICIONES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosAficiones() {
        RequestContext.getCurrentInstance().update("form:infoRegistroAficion");
    }

    //GETTERS AND SETTERS
    public List<VigenciasAficiones> getListVigenciasAficiones() {
        try {
            if (listVigenciasAficiones == null) {
                if (empleado.getPersona().getSecuencia() != null) {
                    listVigenciasAficiones = administrarVigenciaAficion.listVigenciasAficionesPersona(empleado.getPersona().getSecuencia());
                }
            }
            return listVigenciasAficiones;

        } catch (Exception e) {
            System.out.println("Error...!! getListVigenciasAficiones : " + e.toString());
            return null;
        }
    }

    public void setListVigenciasAficiones(List<VigenciasAficiones> setListVigenciasAficiones) {
        this.listVigenciasAficiones = setListVigenciasAficiones;
    }

    public List<VigenciasAficiones> getFiltrarListVigenciasAficiones() {
        return filtrarListVigenciasAficiones;
    }

    public void setFiltrarListVigenciasAficiones(List<VigenciasAficiones> setFiltrarListVigenciasAficiones) {
        this.filtrarListVigenciasAficiones = setFiltrarListVigenciasAficiones;
    }

    public VigenciasAficiones getNuevaVigenciaAficion() {
        return nuevaVigenciaAficion;
    }

    public void setNuevaVigenciaAficion(VigenciasAficiones setNuevaVigenciaAficion) {
        this.nuevaVigenciaAficion = setNuevaVigenciaAficion;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public List<Aficiones> getListAficiones() {
        listAficiones = administrarVigenciaAficion.listAficiones();
        return listAficiones;
    }

    public void setListAficiones(List<Aficiones> setListAficiones) {
        this.listAficiones = setListAficiones;
    }

    public List<Aficiones> getFiltrarListAficiones() {
        return filtrarListAficiones;
    }

    public void setFiltrarListAficiones(List<Aficiones> setFiltrarListAficiones) {
        this.filtrarListAficiones = setFiltrarListAficiones;
    }

    public VigenciasAficiones getEditarVigenciaAficion() {
        return editarVigenciaAficion;
    }

    public void setEditarVigenciaAficion(VigenciasAficiones setEditarVigenciaAficion) {
        this.editarVigenciaAficion = setEditarVigenciaAficion;
    }

    public VigenciasAficiones getDuplicarVigenciaAficion() {
        return duplicarVigenciaAficion;
    }

    public void setDuplicarVigenciaAficion(VigenciasAficiones setDuplicarVigenciaAficion) {
        this.duplicarVigenciaAficion = setDuplicarVigenciaAficion;
    }

    public Aficiones getAficionSeleccionada() {
        return aficionSeleccionada;
    }

    public void setAficionSeleccionada(Aficiones setAficionSeleccionada) {
        this.aficionSeleccionada = setAficionSeleccionada;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public String getInfoRegistroAficion() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovAficiones");
        infoRegistroAficion = String.valueOf(tabla.getRowCount());
        return infoRegistroAficion;
    }

    public void setInfoRegistroAficion(String infoRegistroAficion) {
        this.infoRegistroAficion = infoRegistroAficion;
    }

    public VigenciasAficiones getVigenciaTablaSeleccionada() {
        return vigenciaTablaSeleccionada;
    }

    public void setVigenciaTablaSeleccionada(VigenciasAficiones vigenciaTablaSeleccionada) {
        this.vigenciaTablaSeleccionada = vigenciaTablaSeleccionada;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasAficiones");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
