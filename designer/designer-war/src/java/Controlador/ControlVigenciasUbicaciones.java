package Controlador;

import Entidades.Empleados;
import Entidades.UbicacionesGeograficas;
import Entidades.VigenciasUbicaciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasUbicacionesInterface;
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

@ManagedBean
@SessionScoped
public class ControlVigenciasUbicaciones implements Serializable {

    private static Logger log = Logger.getLogger(ControlVigenciasUbicaciones.class);

    @EJB
    AdministrarVigenciasUbicacionesInterface administrarVigenciasUbicaciones;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Vigencias Cargos
    private List<VigenciasUbicaciones> vigenciasUbicaciones;
    private List<VigenciasUbicaciones> filtrarVU;
    private List<UbicacionesGeograficas> lovUbicaciones;
    private VigenciasUbicaciones vigenciaSeleccionada;
    private UbicacionesGeograficas UbicacionSelecionada;
    private List<UbicacionesGeograficas> filtradoUbicaciones;
    private BigInteger secuenciaEmpleado;
    private Empleados empleado;
    private int tipoActualizacion;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Columnas Tabla VC
    private Column vuFecha, vuDescripcion, vuCiudad;
    //Otros
    private boolean aceptar;
    //modificar
    private List<VigenciasUbicaciones> listVUModificar;
    private boolean guardado;
    //crear VC
    public VigenciasUbicaciones nuevaVigencia;
    private List<VigenciasUbicaciones> listVUCrear;
    private BigInteger l;
    private int k;
    //borrar VC
    private List<VigenciasUbicaciones> listVUBorrar;
    //editar celda
    private VigenciasUbicaciones editarVU;
    private int cualCelda, tipoLista;
    private boolean cambioEditor, aceptarEditar;
    //duplicar
    private VigenciasUbicaciones duplicarVU;
    //AUTOCOMPLETAR
    private boolean permitirIndex;
    private String ubicacion;
    //MENSAJE VALIDACION NUEVA VIGENCIA UBICACION
    private String mensajeValidacion;
    private String altoTabla;
    public String infoRegistro;
    //
    private String infoRegistroUbicacion;
    //CONTROL FECHA
    private Date fechaVigenciaBck;
    private DataTable tablaC;
    private boolean activarLOV;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlVigenciasUbicaciones() {
        vigenciasUbicaciones = null;
        lovUbicaciones = new ArrayList<UbicacionesGeograficas>();
        empleado = new Empleados();
        //UbicacionSelecionada = new UbicacionesGeograficas();
        UbicacionSelecionada = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listVUBorrar = new ArrayList<VigenciasUbicaciones>();
        //crear aficiones
        listVUCrear = new ArrayList<VigenciasUbicaciones>();
        k = 0;
        //modificar aficiones
        listVUModificar = new ArrayList<VigenciasUbicaciones>();
        //editar
        editarVU = new VigenciasUbicaciones();
        cambioEditor = false;
        aceptarEditar = true;
        // cualCelda = 0;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevaVigencia = new VigenciasUbicaciones();
        nuevaVigencia.setUbicacion(new UbicacionesGeograficas());
        //Autocompletar
        permitirIndex = true;
        altoTabla = "292";
        vigenciaSeleccionada = null;
        activarLOV = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {
        lovUbicaciones = null;
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
            administrarVigenciasUbicaciones.obtenerConexion(ses.getId());
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
        String pagActual = "vigenciasubicaciones";
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
    public void recibirEmpleado(BigInteger sec) {
        secuenciaEmpleado = sec;
        empleado = administrarVigenciasUbicaciones.buscarEmpleado(secuenciaEmpleado);
        //vigenciasUbicaciones = null;
        getVigenciasUbicaciones();
        if (!vigenciasUbicaciones.isEmpty()) {
            if (vigenciasUbicaciones != null) {
                vigenciaSeleccionada = vigenciasUbicaciones.get(0);
            }
        } else {
            vigenciaSeleccionada = null;
        }
    }

    public void modificarVU(VigenciasUbicaciones vUbicacion, String confirmarCambio, String valorConfirmar) {
        vigenciaSeleccionada = vUbicacion;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            log.info("valor Confirmar: " + valorConfirmar);
            if (!valorConfirmar.isEmpty()) {
                int control = 0;
                for (int i = 0; i < vigenciasUbicaciones.size(); i++) {
                    if (vigenciasUbicaciones.get(i) == vUbicacion) {
                        i++;
                        if (i >= vigenciasUbicaciones.size()) {
                            break;
                        }
                    }
                    if (vigenciaSeleccionada.getFechavigencia().compareTo(vigenciaSeleccionada.getFechavigencia()) == 0) {
                        control++;
                        vigenciaSeleccionada.setFechavigencia(fechaVigenciaBck);
                    }
                }

                if (control == 0) {
                    if (!listVUCrear.contains(vigenciaSeleccionada)) {

                        if (listVUModificar.isEmpty()) {
                            listVUModificar.add(vigenciaSeleccionada);
                        } else if (!listVUModificar.contains(vigenciaSeleccionada)) {
                            listVUModificar.add(vigenciaSeleccionada);
                        }
                    }

                    if (guardado) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                } else {
                    RequestContext.getCurrentInstance().execute("PF('validacionFechaDuplicada').show();");
                }
            } else {
                vigenciaSeleccionada.setFechavigencia(fechaVigenciaBck);
                RequestContext.getCurrentInstance().execute("PF('validacionFechaVacia').show();");
            }
        } else if (confirmarCambio.equalsIgnoreCase("UBICACION")) {
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            vigenciaSeleccionada.getUbicacion().setDescripcion(ubicacion);
            for (int i = 0; i < lovUbicaciones.size(); i++) {
                if (lovUbicaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaSeleccionada.setUbicacion(lovUbicaciones.get(indiceUnicoElemento));
                lovUbicaciones.clear();
                getLovUbicaciones();
            } else {
                permitirIndex = false;
                getInfoRegistroUbicacion();
                RequestContext.getCurrentInstance().update("form:UbicacionesGeograficasDialogo");
                RequestContext.getCurrentInstance().execute("PF('UbicacionesGeograficasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (!listVUCrear.contains(vigenciaSeleccionada)) {

                if (listVUModificar.isEmpty()) {
                    listVUModificar.add(vigenciaSeleccionada);
                } else if (!listVUModificar.contains(vigenciaSeleccionada)) {
                    listVUModificar.add(vigenciaSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        RequestContext.getCurrentInstance().update("form:datosVUEmpleado");
    }

    //Ubicacion Celda.
    public void cambiarIndice(VigenciasUbicaciones vUbicaciones, int celda) {
        if (permitirIndex) {
            vigenciaSeleccionada = vUbicaciones;
            cualCelda = celda;
            if (cualCelda == 0) {
                activarLOV = true;
                RequestContext.getCurrentInstance().update("form:listaValores");
                fechaVigenciaBck = vigenciaSeleccionada.getFechavigencia();
            }
            if (cualCelda == 1) {
                activarLOV = false;
                RequestContext.getCurrentInstance().update("form:listaValores");
                ubicacion = vigenciaSeleccionada.getUbicacion().getDescripcion();
            }
        }
    }

    public void cambioIndiceReadOnly() {
        if (permitirIndex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            RequestContext context = RequestContext.getCurrentInstance();
            Map<String, String> map = contexto.getExternalContext().getRequestParameterMap();
            String campo = map.get("INDEX");
            int poss = Integer.parseInt(campo);
            RequestContext.getCurrentInstance().execute("PF('datosVUEmpleado').unselectAllRows();PF('datosVUEmpleado').selectRow(" + poss + ");");
            vigenciaSeleccionada = vigenciasUbicaciones.get(poss);
            cualCelda = 2;
        }
    }

    //AUTOCOMPLETAR NUEVO Y DUPLICADO
    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("UBICACION")) {
            if (tipoNuevo == 1) {
                ubicacion = nuevaVigencia.getUbicacion().getDescripcion();
            } else if (tipoNuevo == 2) {
                ubicacion = duplicarVU.getUbicacion().getDescripcion();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("UBICACION")) {
            if (tipoNuevo == 1) {
                nuevaVigencia.getUbicacion().setDescripcion(ubicacion);
            } else if (tipoNuevo == 2) {
                duplicarVU.getUbicacion().setDescripcion(ubicacion);
            }
            for (int i = 0; i < lovUbicaciones.size(); i++) {
                if (lovUbicaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigencia.setUbicacion(lovUbicaciones.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDescripcion");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudad");
                } else if (tipoNuevo == 2) {
                    duplicarVU.setUbicacion(lovUbicaciones.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcion");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudad");
                }
                lovUbicaciones.clear();
                getLovUbicaciones();
            } else {
                RequestContext.getCurrentInstance().update("form:UbicacionesGeograficasDialogo");
                RequestContext.getCurrentInstance().execute("PF('UbicacionesGeograficasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDescripcion");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudad");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcion");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudad");
                }
            }
        }
    }

    public void guardarYSalir() {
        guardarCambiosVU();
        salir();
    }

    //GUARDAR
    public void guardarCambiosVU() {
        if (guardado == false) {
            log.info("Realizando Operaciones Vigencias Tipos Contratos");
            if (!listVUBorrar.isEmpty()) {
                for (int i = 0; i < listVUBorrar.size(); i++) {
                    log.info("Borrando...");
                    administrarVigenciasUbicaciones.borrarVU(listVUBorrar.get(i));
                }
                listVUBorrar.clear();
            }
            if (!listVUCrear.isEmpty()) {
                for (int i = 0; i < listVUCrear.size(); i++) {
                    log.info("Creando...");
                    administrarVigenciasUbicaciones.crearVU(listVUCrear.get(i));
                }
                listVUCrear.clear();
            }
            if (!listVUModificar.isEmpty()) {
                administrarVigenciasUbicaciones.modificarVU(listVUModificar);
                listVUModificar.clear();
            }
            log.info("Se guardaron los datos con exito");
//            vigenciasUbicaciones = null;
            getVigenciasUbicaciones();
            contarRegistrosUbicaciones();
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            RequestContext.getCurrentInstance().update("form:datosVUEmpleado");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }
    //CANCELAR MODIFICACIONES

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            cerrarFiltrado();
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        listVUBorrar.clear();
        listVUCrear.clear();
        listVUModificar.clear();
        vigenciaSeleccionada = null;
        k = 0;
        vigenciasUbicaciones = null;
        getVigenciasUbicaciones();
        contarRegistrosUbicaciones();
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosVUEmpleado");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    private void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        vuFecha = (Column) c.getViewRoot().findComponent("form:datosVUEmpleado:vuFecha");
        vuFecha.setFilterStyle("display: none; visibility: hidden;");
        vuDescripcion = (Column) c.getViewRoot().findComponent("form:datosVUEmpleado:vuDescripcion");
        vuDescripcion.setFilterStyle("display: none; visibility: hidden;");
        vuCiudad = (Column) c.getViewRoot().findComponent("form:datosVUEmpleado:vuCiudad");
        vuCiudad.setFilterStyle("display: none; visibility: hidden;");
        altoTabla = "292";
        RequestContext.getCurrentInstance().update("form:datosVUEmpleado");
        bandera = 0;
        filtrarVU = null;
        tipoLista = 0;
    }

    //RASTROS 
    public void verificarRastro() {
        if (vigenciaSeleccionada != null) {
            log.info("lol 2");
            int result = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASUBICACIONES");
            log.info("resultado: " + result);
            if (result == 1) {
                RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (result == 2) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
            } else if (result == 3) {
                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
            } else if (result == 4) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
            } else if (result == 5) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASUBICACIONES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (vigenciaSeleccionada == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else {
            editarVU = vigenciaSeleccionada;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
                RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcion').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudad");
                RequestContext.getCurrentInstance().execute("PF('editarCiudad').show()");
                cualCelda = -1;
            }
        }
    }

    //CREAR VU
    public void agregarNuevaVU() {
        int contador = 0;
        int fechas = 0;
        int pasa = 0;
        mensajeValidacion = " ";
        nuevaVigencia.setEmpleado(empleado);
        if (nuevaVigencia.getFechavigencia() == null || nuevaVigencia.getFechavigencia().equals("")) {
            mensajeValidacion = " *Fecha\n";
        } else {
            if (vigenciasUbicaciones != null) {
                for (int j = 0; j < vigenciasUbicaciones.size(); j++) {
                    if (nuevaVigencia.getFechavigencia().equals(vigenciasUbicaciones.get(j).getFechavigencia())) {
                        fechas++;
                    }
                }
            }
            if (fechas > 0) {
                RequestContext.getCurrentInstance().update("form:fechas");
                RequestContext.getCurrentInstance().execute("PF('fechas').show()");
                pasa++;

            } else {
                contador++;
            }
        }
        if (nuevaVigencia.getUbicacion().getSecuencia() == null) {
            mensajeValidacion = mensajeValidacion + " * Ubicación \n";

        } else {
            contador++;
        }
        if (contador == 2 && pasa == 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                cerrarFiltrado();
            }
            //AGREGAR REGISTRO A LA LISTA 
            k++;
            l = BigInteger.valueOf(k);
            nuevaVigencia.setSecuencia(l);
            nuevaVigencia.setEmpleado(empleado);

            listVUCrear.add(nuevaVigencia);
            vigenciasUbicaciones.add(nuevaVigencia);
            vigenciaSeleccionada = vigenciasUbicaciones.get(vigenciasUbicaciones.indexOf(nuevaVigencia));
            contarRegistros();
            nuevaVigencia = new VigenciasUbicaciones();
            nuevaVigencia.setUbicacion(new UbicacionesGeograficas());
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            RequestContext.getCurrentInstance().update("form:datosVUEmpleado");
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVU').hide()");
        } else if (pasa == 0 && contador != 2) {
            RequestContext.getCurrentInstance().update("form:validacionNuevo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
            contador = 0;
            pasa = 0;
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVU");
    }

//LIMPIAR NUEVO REGISTRO}
    public void limpiarNuevaVU() {
        nuevaVigencia = new VigenciasUbicaciones();
        nuevaVigencia.setUbicacion(new UbicacionesGeograficas());
    }
    //DUPLICAR VC

    public void duplicarVigenciaU() {
        if (vigenciaSeleccionada == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else if (vigenciaSeleccionada != null) {
            duplicarVU = new VigenciasUbicaciones();
            k++;
            l = BigInteger.valueOf(k);
            duplicarVU.setSecuencia(l);
            duplicarVU.setEmpleado(vigenciaSeleccionada.getEmpleado());
            duplicarVU.setFechavigencia(vigenciaSeleccionada.getFechavigencia());
            duplicarVU.setUbicacion(vigenciaSeleccionada.getUbicacion());

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVU");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVU').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        for (int j = 0; j < vigenciasUbicaciones.size(); j++) {
            if (duplicarVU.getFechavigencia().equals(vigenciasUbicaciones.get(j).getFechavigencia())) {
                contador++;
            }
        }
        if (contador > 0) {
            mensajeValidacion = "Fechas NO Repetidas";
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionFechaDuplicada");
            RequestContext.getCurrentInstance().execute("PF('validacionFechaDuplicada').show()");
        } else {
            vigenciasUbicaciones.add(duplicarVU);
            listVUCrear.add(duplicarVU);
            vigenciaSeleccionada = vigenciasUbicaciones.get(vigenciasUbicaciones.indexOf(duplicarVU));
            contarRegistros();
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            RequestContext.getCurrentInstance().update("form:datosVUEmpleado");
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            if (bandera == 1) {
                cerrarFiltrado();
            }
            duplicarVU = new VigenciasUbicaciones();
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVU').hide()");
        }

    }
    //LIMPIAR DUPLICAR

    public void limpiarduplicarVU() {
        duplicarVU = new VigenciasUbicaciones();
    }

    //BORRAR VC
    public void borrarVU() {
        if (vigenciaSeleccionada == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else if (vigenciaSeleccionada != null) {
            if (!listVUModificar.isEmpty() && listVUModificar.contains(vigenciaSeleccionada)) {
                int modIndex = listVUModificar.indexOf(vigenciaSeleccionada);
                listVUModificar.remove(modIndex);
                listVUBorrar.add(vigenciaSeleccionada);
            } else if (!listVUCrear.isEmpty() && listVUCrear.contains(vigenciaSeleccionada)) {
                int crearIndex = listVUCrear.indexOf(vigenciaSeleccionada);
                listVUCrear.remove(crearIndex);
            } else {
                listVUBorrar.add(vigenciaSeleccionada);
            }
            vigenciasUbicaciones.remove(vigenciaSeleccionada);
            contarRegistros();
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            RequestContext.getCurrentInstance().update("form:datosVUEmpleado");

            vigenciaSeleccionada = null;

            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            log.info("Activar");
            vuFecha = (Column) c.getViewRoot().findComponent("form:datosVUEmpleado:vuFecha");
            vuFecha.setFilterStyle("width: 85% !important;");
            vuDescripcion = (Column) c.getViewRoot().findComponent("form:datosVUEmpleado:vuDescripcion");
            vuDescripcion.setFilterStyle("width: 85% !important;");
            vuCiudad = (Column) c.getViewRoot().findComponent("form:datosVUEmpleado:vuCiudad");
            vuCiudad.setFilterStyle("width: 85% !important;");
            altoTabla = "272";
            RequestContext.getCurrentInstance().update("form:datosVUEmpleado");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
        }
    }

    //SALIR
    public void salir() {
        limpiarListasValor();
        cerrarFiltrado();
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        listVUBorrar.clear();
        listVUCrear.clear();
        listVUModificar.clear();
        vigenciaSeleccionada = null;
        k = 0;
        limpiarListasValor();
        vigenciasUbicaciones = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }
    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)

    public void asignarIndex(VigenciasUbicaciones vu, int LND) {
        vigenciaSeleccionada = vu;
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
        UbicacionSelecionada = null;
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            log.info("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        contarRegistrosUbicaciones();
        RequestContext.getCurrentInstance().update("form:UbicacionesGeograficasDialogo");
        RequestContext.getCurrentInstance().execute("PF('UbicacionesGeograficasDialogo').show()");
    }

    //LOVS
    //CIUDAD
    public void actualizarUbicacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            vigenciaSeleccionada.setUbicacion(UbicacionSelecionada);
            if (!listVUCrear.contains(vigenciaSeleccionada)) {
                if (listVUModificar.isEmpty()) {
                    listVUModificar.add(vigenciaSeleccionada);
                } else if (!listVUModificar.contains(vigenciaSeleccionada)) {
                    listVUModificar.add(vigenciaSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosVUEmpleado");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {
            nuevaVigencia.setUbicacion(UbicacionSelecionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVU");
        } else if (tipoActualizacion == 2) {
            duplicarVU.setUbicacion(UbicacionSelecionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVU");
        }
        filtradoUbicaciones = null;
        UbicacionSelecionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

        context.reset("form:lovUbicaciones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUbicaciones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('UbicacionesGeograficasDialogo').hide()");
    }

    public void cancelarCambioUbicacion() {
        filtradoUbicaciones = null;
        UbicacionSelecionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovUbicaciones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUbicaciones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('UbicacionesGeograficasDialogo').hide()");
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (vigenciaSeleccionada == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else if (vigenciaSeleccionada != null) {
            if (cualCelda == 1) {
                UbicacionSelecionada = null;
                //getInfoRegistroUbicacion();
                contarRegistrosUbicaciones();
                RequestContext.getCurrentInstance().update("form:UbicacionesGeograficasDialogo");
                RequestContext.getCurrentInstance().execute("PF('UbicacionesGeograficasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    public void exportPDF() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosVUEmpleadoExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasUbicacionesGeograficasPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosVUEmpleadoExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasUbicacionesGeograficasXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }
    //EVENTO FILTRAR

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosUbicaciones() {
        RequestContext.getCurrentInstance().update("form:infoRegistroUbicacion");
    }

    public void recordarSeleccion() {
        if (vigenciaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVUEmpleado");
            tablaC.setSelection(vigenciaSeleccionada);
        } else {
            vigenciaSeleccionada = null;
        }
    }

    public void anularLOV() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    //GETTERS AND SETTERS
    public List<VigenciasUbicaciones> getVigenciasUbicaciones() {
        try {
            if (vigenciasUbicaciones == null) {
                vigenciasUbicaciones = administrarVigenciasUbicaciones.vigenciasUbicacionesEmpleado(secuenciaEmpleado);
            }
            return vigenciasUbicaciones;

        } catch (Exception e) {
            log.warn("Error...!! getVigenciasUbicacionsEmpleado ");
            return null;
        }
    }

    public void setVigenciasUbicaciones(List<VigenciasUbicaciones> vigenciasUbicaciones) {
        this.vigenciasUbicaciones = vigenciasUbicaciones;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public List<VigenciasUbicaciones> getFiltrarVU() {
        return filtrarVU;
    }

    public void setFiltrarVU(List<VigenciasUbicaciones> filtrarVU) {
        this.filtrarVU = filtrarVU;
    }

    public VigenciasUbicaciones getNuevaVigencia() {
        return nuevaVigencia;
    }

    public void setNuevaVigencia(VigenciasUbicaciones nuevaVigencia) {
        this.nuevaVigencia = nuevaVigencia;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public List<UbicacionesGeograficas> getLovUbicaciones() {
        if (lovUbicaciones == null || lovUbicaciones.isEmpty()) {
            log.info("Aviso " + this.getClass().getName() + " getListaUbicaciones() va a traer la lista de Ubicaciones");
            lovUbicaciones = administrarVigenciasUbicaciones.ubicacionesGeograficas();
        }
        return lovUbicaciones;
    }

    public void setLovUbicaciones(List<UbicacionesGeograficas> lovUbicaciones) {
        this.lovUbicaciones = lovUbicaciones;
    }

    public UbicacionesGeograficas getUbicacionSelecionada() {
        return UbicacionSelecionada;
    }

    public void setUbicacionSelecionada(UbicacionesGeograficas UbicacionSelecionada) {
        this.UbicacionSelecionada = UbicacionSelecionada;
    }

    public List<UbicacionesGeograficas> getFiltradoUbicaciones() {
        return filtradoUbicaciones;
    }

    public void setFiltradoUbicaciones(List<UbicacionesGeograficas> filtradoUbicaciones) {
        this.filtradoUbicaciones = filtradoUbicaciones;
    }

    public VigenciasUbicaciones getEditarVU() {
        return editarVU;
    }

    public void setEditarVU(VigenciasUbicaciones editarVU) {
        this.editarVU = editarVU;
    }

    public VigenciasUbicaciones getDuplicarVU() {
        return duplicarVU;
    }

    public void setDuplicarVU(VigenciasUbicaciones duplicarVU) {
        this.duplicarVU = duplicarVU;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public VigenciasUbicaciones getVigenciaSeleccionada() {
        return vigenciaSeleccionada;
    }

    public void setVigenciaSeleccionada(VigenciasUbicaciones vigenciaSeleccionada) {
        this.vigenciaSeleccionada = vigenciaSeleccionada;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVUEmpleado");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public String getInfoRegistroUbicacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovUbicaciones");
        infoRegistroUbicacion = String.valueOf(tabla.getRowCount());
        return infoRegistroUbicacion;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
