/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import InterfaceAdministrar.AdministrarMotivosCambiosSueldosInterface;
import Entidades.MotivosCambiosSueldos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class ControlMotivosCambiosSueldos implements Serializable {

   private static Logger log = Logger.getLogger(ControlMotivosCambiosSueldos.class);

    @EJB
    AdministrarMotivosCambiosSueldosInterface administrarMotivosCambiosSueldos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<MotivosCambiosSueldos> listMotivosCambiosSueldos;
    private List<MotivosCambiosSueldos> filtrarMotivosCambiosSueldos;
    private List<MotivosCambiosSueldos> borrarMotivosCambiosSueldos;
    private List<MotivosCambiosSueldos> modificarrMotivosCambiosSueldos;
    private List<MotivosCambiosSueldos> crearMotivosCambiosSueldos;
    private MotivosCambiosSueldos editarMotivoCambioSueldo;
    private MotivosCambiosSueldos nuevoMotivoCambioSueldo;
    private MotivosCambiosSueldos duplicarMotivoCambioSueldo;
    private MotivosCambiosSueldos motivoCambioSueldoSeleccionado;
    private String mensajeValidacion;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion, estadoSueldoPromedio;
    //borrado
    private BigInteger borradoVS;
    private int registrosBorrados;
    private int tamano;
    private String infoRegistro;
    private boolean activarLov;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    //----------------------
    public ControlMotivosCambiosSueldos() {
        listMotivosCambiosSueldos = null;
        crearMotivosCambiosSueldos = new ArrayList<MotivosCambiosSueldos>();
        modificarrMotivosCambiosSueldos = new ArrayList<MotivosCambiosSueldos>();
        borrarMotivosCambiosSueldos = new ArrayList<MotivosCambiosSueldos>();
        nuevoMotivoCambioSueldo = new MotivosCambiosSueldos();
        nuevoMotivoCambioSueldo.getEstadoSueldoPromedio();
        duplicarMotivoCambioSueldo = new MotivosCambiosSueldos();
        permitirIndex = true;
        guardado = true;
        tamano = 270;
        activarLov = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
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
        String pagActual = "motivocambiosueldo";
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

    public void limpiarListasValor() {

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
            administrarMotivosCambiosSueldos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(MotivosCambiosSueldos motivo, int celda) {
        motivoCambioSueldoSeleccionado = motivo;
        cualCelda = celda;
        motivoCambioSueldoSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            motivoCambioSueldoSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            motivoCambioSueldoSeleccionado.getNombre();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            tamano = 270;
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estadoSueldoPromedio = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:estadoSueldoPromedio");
            estadoSueldoPromedio.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
            bandera = 0;
            filtrarMotivosCambiosSueldos = null;
            tipoLista = 0;
        }

        borrarMotivosCambiosSueldos.clear();
        crearMotivosCambiosSueldos.clear();
        modificarrMotivosCambiosSueldos.clear();
        motivoCambioSueldoSeleccionado = null;
        k = 0;
        listMotivosCambiosSueldos = null;
        guardado = true;
        permitirIndex = true;
        getListMotivosCambiosSueldos();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        log.error("cancelarModificacion");
        if (bandera == 1) {
            tamano = 270;
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estadoSueldoPromedio = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:estadoSueldoPromedio");
            estadoSueldoPromedio.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
            bandera = 0;
            filtrarMotivosCambiosSueldos = null;
            tipoLista = 0;
        }
        borrarMotivosCambiosSueldos.clear();
        crearMotivosCambiosSueldos.clear();
        modificarrMotivosCambiosSueldos.clear();
        motivoCambioSueldoSeleccionado = null;
        k = 0;
        listMotivosCambiosSueldos = null;
        guardado = true;
        permitirIndex = true;
        getListMotivosCambiosSueldos();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            estadoSueldoPromedio = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:estadoSueldoPromedio");
            estadoSueldoPromedio.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estadoSueldoPromedio = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:estadoSueldoPromedio");
            estadoSueldoPromedio.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
            bandera = 0;
            filtrarMotivosCambiosSueldos = null;
            tipoLista = 0;
        }
    }

    public void modificarPromedioSuelos(MotivosCambiosSueldos motivo, boolean cambio) {
        motivoCambioSueldoSeleccionado = motivo;
        log.info("modificarPromediosSuelos cambio = " + cambio);
        log.error("cambio = " + cambio);
        motivoCambioSueldoSeleccionado.setEstadoSueldoPromedio(cambio);
        if (motivoCambioSueldoSeleccionado.getEstadoSueldoPromedio() == true) {
            motivoCambioSueldoSeleccionado.setSueldopromedio("S");
        } else {
            motivoCambioSueldoSeleccionado.setSueldopromedio("N");
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");

        modificarMotivosCambiosSueldos(motivoCambioSueldoSeleccionado);
    }

    public void modificarMotivosCambiosSueldos(MotivosCambiosSueldos motivo) {
        motivoCambioSueldoSeleccionado = motivo;
        if (!crearMotivosCambiosSueldos.contains(motivoCambioSueldoSeleccionado)) {
            if (modificarrMotivosCambiosSueldos.isEmpty()) {
                modificarrMotivosCambiosSueldos.add(motivoCambioSueldoSeleccionado);
            } else if (!modificarrMotivosCambiosSueldos.contains(motivoCambioSueldoSeleccionado)) {
                modificarrMotivosCambiosSueldos.add(motivoCambioSueldoSeleccionado);
            }
        }
        if (guardado == true) {
            guardado = false;
        }

        RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void borrarMotivosCambiosSueldos() {
        if (motivoCambioSueldoSeleccionado != null) {
            if (tipoLista == 0) {
                log.info("Entro a borrarMotivosCambiosSueldos");
                if (!modificarrMotivosCambiosSueldos.isEmpty() && modificarrMotivosCambiosSueldos.contains(motivoCambioSueldoSeleccionado)) {
                    int modIndex = modificarrMotivosCambiosSueldos.indexOf(motivoCambioSueldoSeleccionado);
                    modificarrMotivosCambiosSueldos.remove(modIndex);
                    borrarMotivosCambiosSueldos.add(motivoCambioSueldoSeleccionado);
                } else if (!crearMotivosCambiosSueldos.isEmpty() && crearMotivosCambiosSueldos.contains(motivoCambioSueldoSeleccionado)) {
                    int crearIndex = crearMotivosCambiosSueldos.indexOf(motivoCambioSueldoSeleccionado);
                    crearMotivosCambiosSueldos.remove(crearIndex);
                } else {
                    borrarMotivosCambiosSueldos.add(motivoCambioSueldoSeleccionado);
                }
                listMotivosCambiosSueldos.remove(motivoCambioSueldoSeleccionado);
            }
            if (tipoLista == 1) {
                filtrarMotivosCambiosSueldos.remove(motivoCambioSueldoSeleccionado);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
            motivoCambioSueldoSeleccionado = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void editarCelda() {
        if (motivoCambioSueldoSeleccionado != null) {
            editarMotivoCambioSueldo = motivoCambioSueldoSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoMotivoCambioSueldo() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivoCambioSueldo.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligtorios";
        } else {
            for (int x = 0; x < listMotivosCambiosSueldos.size(); x++) {
                if (listMotivosCambiosSueldos.get(x).getCodigo() == nuevoMotivoCambioSueldo.getCodigo()) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya existe. Por favor ingrese otro código.";
            } else {
                contador++;
            }
        }
        if (nuevoMotivoCambioSueldo.getNombre() == null || nuevoMotivoCambioSueldo.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligtorios";
        } else {
            contador++;
        }
        if (nuevoMotivoCambioSueldo.getEstadoSueldoPromedio() == null) {
            nuevoMotivoCambioSueldo.setEstadoSueldoPromedio(false);
            nuevoMotivoCambioSueldo.setSueldopromedio("N");
        }
        if (nuevoMotivoCambioSueldo.getEstadoSueldoPromedio() == true) {
            log.error("Sueldo Promedio S");
            nuevoMotivoCambioSueldo.setSueldopromedio("S");
        }
        if (nuevoMotivoCambioSueldo.getEstadoSueldoPromedio() == false) {
            log.error("Sueldo Promedio N");
            nuevoMotivoCambioSueldo.setSueldopromedio("N");
        }
        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                log.info("Desactivar");
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                estadoSueldoPromedio = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:estadoSueldoPromedio");
                estadoSueldoPromedio.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
                bandera = 0;
                filtrarMotivosCambiosSueldos = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoCambioSueldo.setSecuencia(l);
            crearMotivosCambiosSueldos.add(nuevoMotivoCambioSueldo);
            listMotivosCambiosSueldos.add(nuevoMotivoCambioSueldo);
            motivoCambioSueldoSeleccionado = nuevoMotivoCambioSueldo;
            nuevoMotivoCambioSueldo = new MotivosCambiosSueldos();
            nuevoMotivoCambioSueldo.getEstadoSueldoPromedio();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMotivoCambiosSueldos').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoMotivoCambioSueldo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoMotivoCambioSueldo').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivoCambioSueldo() {
        nuevoMotivoCambioSueldo = new MotivosCambiosSueldos();
        nuevoMotivoCambioSueldo.getEstadoSueldoPromedio();
    }

    public void duplicarMotivosCambiosSueldos() {
        if (motivoCambioSueldoSeleccionado != null) {
            duplicarMotivoCambioSueldo = new MotivosCambiosSueldos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivoCambioSueldo.setSecuencia(l);
                duplicarMotivoCambioSueldo.setCodigo(motivoCambioSueldoSeleccionado.getCodigo());
                duplicarMotivoCambioSueldo.setNombre(motivoCambioSueldoSeleccionado.getNombre());
                duplicarMotivoCambioSueldo.setSueldopromedio(motivoCambioSueldoSeleccionado.getSueldopromedio());
                duplicarMotivoCambioSueldo.setEstadoSueldoPromedio(motivoCambioSueldoSeleccionado.getEstadoSueldoPromedio());
            }
            if (tipoLista == 1) {
                duplicarMotivoCambioSueldo.setSecuencia(l);
                duplicarMotivoCambioSueldo.setCodigo(motivoCambioSueldoSeleccionado.getCodigo());
                duplicarMotivoCambioSueldo.setNombre(motivoCambioSueldoSeleccionado.getNombre());
                duplicarMotivoCambioSueldo.setSueldopromedio(motivoCambioSueldoSeleccionado.getSueldopromedio());
                duplicarMotivoCambioSueldo.setEstadoSueldoPromedio(motivoCambioSueldoSeleccionado.getEstadoSueldoPromedio());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivosCambiosSueldos");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosCambiosSueldos').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        log.error("ConfirmarDuplicar codigo " + duplicarMotivoCambioSueldo.getCodigo());
        log.error("ConfirmarDuplicar nombre " + duplicarMotivoCambioSueldo.getNombre());
        log.error("ConfirmarDuplicar Sueldo Promedio " + duplicarMotivoCambioSueldo.getSueldopromedio());

        if (duplicarMotivoCambioSueldo.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligtorios";
        } else {
            for (int x = 0; x < listMotivosCambiosSueldos.size(); x++) {
                if (listMotivosCambiosSueldos.get(x).getCodigo() == duplicarMotivoCambioSueldo.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " El código ingresado ya existe. Por favor ingrese otro código.";
                log.info("Mensaje validacion : " + mensajeValidacion);
            } else {
                log.info("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarMotivoCambioSueldo.getNombre() == null || duplicarMotivoCambioSueldo.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligtorios";

        } else {
            contador++;
        }
        if (duplicarMotivoCambioSueldo.getEstadoSueldoPromedio() == true) {
            log.error("Sueldo Promedio S");
            duplicarMotivoCambioSueldo.setSueldopromedio("S");
        }
        if (duplicarMotivoCambioSueldo.getEstadoSueldoPromedio() == false) {
            log.error("Sueldo Promedio N");
            duplicarMotivoCambioSueldo.setSueldopromedio("N");
        }

        if (contador == 2) {
            listMotivosCambiosSueldos.add(duplicarMotivoCambioSueldo);
            crearMotivosCambiosSueldos.add(duplicarMotivoCambioSueldo);
            motivoCambioSueldoSeleccionado = duplicarMotivoCambioSueldo;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                estadoSueldoPromedio = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo:estadoSueldoPromedio");
                estadoSueldoPromedio.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
                bandera = 0;
                filtrarMotivosCambiosSueldos = null;
                tipoLista = 0;
                tamano = 270;
            }
            duplicarMotivoCambioSueldo = new MotivosCambiosSueldos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosCambiosSueldos').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarduplicarMotivosCambiosSueldos() {
        duplicarMotivoCambioSueldo = new MotivosCambiosSueldos();
    }

    public void guardarMotivosCambiosSueldos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("Realizando Operaciones Vigencias Localizacion");
            if (!borrarMotivosCambiosSueldos.isEmpty()) {
                administrarMotivosCambiosSueldos.borrarMotivosCambiosSueldos(borrarMotivosCambiosSueldos);
                //mostrarBorrados
                registrosBorrados = borrarMotivosCambiosSueldos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarMotivosCambiosSueldos.clear();
            }
            if (!crearMotivosCambiosSueldos.isEmpty()) {
                administrarMotivosCambiosSueldos.crearMotivosCambiosSueldos(crearMotivosCambiosSueldos);
                crearMotivosCambiosSueldos.clear();
            }
            if (!modificarrMotivosCambiosSueldos.isEmpty()) {
                administrarMotivosCambiosSueldos.modificarMotivosCambiosSueldos(modificarrMotivosCambiosSueldos);
                modificarrMotivosCambiosSueldos.clear();
            }
            log.info("Se guardaron los datos con exito");
            listMotivosCambiosSueldos = null;
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioSueldo");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoSueldoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosSueldoPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoSueldoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosSueldoXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (motivoCambioSueldoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(motivoCambioSueldoSeleccionado.getSecuencia(), "MOTIVOSCAMBIOSSUELDOS");
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSCAMBIOSSUELDOS")) { // igual acá
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
//---------//----------------//------------//----------------//-----------//--------------------------//-----

    public List<MotivosCambiosSueldos> getListMotivosCambiosSueldos() {
        if (listMotivosCambiosSueldos == null) {
            listMotivosCambiosSueldos = administrarMotivosCambiosSueldos.consultarMotivosCambiosSueldos();
        }
        return listMotivosCambiosSueldos;
    }

    public void setListMotivosCambiosSueldos(List<MotivosCambiosSueldos> listMotivosCambiosSueldos) {
        this.listMotivosCambiosSueldos = listMotivosCambiosSueldos;
    }

    public List<MotivosCambiosSueldos> getFiltrarMotivosCambiosSueldos() {
        return filtrarMotivosCambiosSueldos;
    }

    public void setFiltrarMotivosCambiosSueldos(List<MotivosCambiosSueldos> filtrarMotivosCambiosSueldos) {
        this.filtrarMotivosCambiosSueldos = filtrarMotivosCambiosSueldos;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public MotivosCambiosSueldos getEditarMotivoCambioSueldo() {
        return editarMotivoCambioSueldo;
    }

    public void setEditarMotivoCambioSueldo(MotivosCambiosSueldos editarMotivoCambioSueldo) {
        this.editarMotivoCambioSueldo = editarMotivoCambioSueldo;
    }

    public MotivosCambiosSueldos getNuevoMotivoCambioSueldo() {
        return nuevoMotivoCambioSueldo;
    }

    public void setNuevoMotivoCambioSueldo(MotivosCambiosSueldos nuevoMotivoCambioSueldo) {
        this.nuevoMotivoCambioSueldo = nuevoMotivoCambioSueldo;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public MotivosCambiosSueldos getDuplicarMotivoCambioSueldo() {
        return duplicarMotivoCambioSueldo;
    }

    public void setDuplicarMotivoCambioSueldo(MotivosCambiosSueldos duplicarMotivoCambioSueldo) {
        this.duplicarMotivoCambioSueldo = duplicarMotivoCambioSueldo;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public MotivosCambiosSueldos getMotivoCambioSueldoSeleccionado() {
        return motivoCambioSueldoSeleccionado;
    }

    public void setMotivoCambioSueldoSeleccionado(MotivosCambiosSueldos motivoCambioSueldoSeleccionado) {
        this.motivoCambioSueldoSeleccionado = motivoCambioSueldoSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMotivoCambioSueldo");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }
}
