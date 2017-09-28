/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.RetencionesMinimas;
import Entidades.VigenciasRetencionesMinimas;
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarRetencionesMinimasInterface;
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
public class ControlRetencionMinima implements Serializable {

   private static Logger log = Logger.getLogger(ControlRetencionMinima.class);

    @EJB
    AdministrarRetencionesMinimasInterface administrarRetencionesMinimas;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //Lista Vigencias RetencionesMinimas (Arriba)
    private List<VigenciasRetencionesMinimas> listaVigenciasRetenciones;
    private List<VigenciasRetencionesMinimas> filtradoListaVigenciasRetenciones;
    private VigenciasRetencionesMinimas vigenciaRetencionSeleccionado;
    //Lista RetencionesMinimas
    private List<RetencionesMinimas> listaRetenciones;
    private List<RetencionesMinimas> filtradoListaRetenciones;
    private RetencionesMinimas retencionSeleccionado;
    //REGISTRO ACTUAL
    private int registroActual, index, tablaActual, indexD;
    //OTROS
    private boolean aceptar, mostrarTodos;
    private String altoScrollVigenciasRetenciones, altoScrollRetenciones;
    //Crear Vigencias RetencionesMinimas (Arriba)
    private List<VigenciasRetencionesMinimas> listaVigenciasRetencionesCrear;
    public VigenciasRetencionesMinimas nuevoVigenciasRetenciones;
    public VigenciasRetencionesMinimas duplicarVigenciasRetenciones;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Vigencias RetencionesMinimas
    private List<VigenciasRetencionesMinimas> listaVigenciasRetencionesModificar;
    //Borrar Vigencias RetencionesMinimas
    private List<VigenciasRetencionesMinimas> listaVigenciasRetencionesBorrar;
    //Crear RetencionesMinimas (Abajo)
    private List<RetencionesMinimas> listaRetencionesCrear;
    public RetencionesMinimas nuevoRetencion;
    public RetencionesMinimas duplicarRetencion;
    //Modificar RetencionesMinimas
    private List<RetencionesMinimas> listaRetencionesModificar;
    //Borrar RetencionesMinimas
    private List<RetencionesMinimas> listaRetencionesBorrar;
    //OTROS
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //editar celda
    private VigenciasRetencionesMinimas editarVigenciasRetenciones;
    private RetencionesMinimas editarRetenciones;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    private int cualCeldaD;
    private int tipoListaD;
    //RASTROS
    private BigInteger secRegistro;
    private BigInteger secRegistroD;
    private boolean guardado, guardarOk;
    private boolean cambiosPagina;
    //FILTRADO
    private Column vCodigo, vFechaVigencia;
    private Column rMensualizado, rRetencion, rPorcentaje, rRestaUvt;
    //Tabla a Imprimir
    private String tablaImprimir, nombreArchivo;
    //Sec Abajo Duplicar
    private int m;
    private BigInteger n;
    //SECUENCIA DE VIGENCIA
    private BigInteger secuenciaVigenciaRetencion;
    private Date fechaParametro;
    private Date fechaVigencia;

    private Integer cualTabla;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlRetencionMinima() {
        permitirIndex = true;
        cualTabla = 0;
        bandera = 0;
        registroActual = 0;
        mostrarTodos = true;
        altoScrollVigenciasRetenciones = "90";
        altoScrollRetenciones = "90";
        listaVigenciasRetencionesBorrar = new ArrayList<VigenciasRetencionesMinimas>();
        listaVigenciasRetencionesCrear = new ArrayList<VigenciasRetencionesMinimas>();
        listaVigenciasRetencionesModificar = new ArrayList<VigenciasRetencionesMinimas>();
        listaRetencionesBorrar = new ArrayList<RetencionesMinimas>();
        listaRetencionesCrear = new ArrayList<RetencionesMinimas>();
        listaRetencionesModificar = new ArrayList<RetencionesMinimas>();
        tablaImprimir = ":formExportar:datosVigenciasRetencionesExportar";
        nombreArchivo = "VigenciasRetencionesXML";
        //Crear Vigencia RetencionesMinimas
        nuevoVigenciasRetenciones = new VigenciasRetencionesMinimas();
        //Crear RetencionesMinimas
        nuevoRetencion = new RetencionesMinimas();
        m = 0;
        cambiosPagina = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
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
            administrarRetencionesMinimas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
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
        /*if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
         
        } else {
            */
String pagActual = "retencionminima";
            
            
            


            
            
            
            
            
            
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

    //CREAR Vigencia Retencion
    public void agregarNuevoVigencia() {
        int pasa = 0;
        int pasar = 0;

        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoVigenciasRetenciones.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + " * Codigo\n";
            pasa++;
        }
        if (nuevoVigenciasRetenciones.getFechavigencia() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Vigencia\n";
            pasa++;
        }

        for (int i = 0; i < listaVigenciasRetenciones.size(); i++) {
            if (nuevoVigenciasRetenciones.getCodigo() == listaVigenciasRetenciones.get(i).getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:codigos");
                RequestContext.getCurrentInstance().execute("PF('codigos').show()");
                pasar++;
            }
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
        }

        if (pasa == 0 && pasar == 0) {
            if (bandera == 1) {
                vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
                vCodigo.setFilterStyle("display: none; visibility: hidden;");
                vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
                vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                altoScrollVigenciasRetenciones = "90";
                RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
                bandera = 0;
                filtradoListaVigenciasRetenciones = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoVigenciasRetenciones.setSecuencia(l);
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaVigenciasRetencionesCrear.add(nuevoVigenciasRetenciones);
            listaVigenciasRetenciones.add(nuevoVigenciasRetenciones);
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasRetenciones').hide()");
            nuevoVigenciasRetenciones = new VigenciasRetencionesMinimas();
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciasRetenciones");
            index = -1;
            secRegistro = null;
        }
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    //EVENTO FILTRAR
    public void eventoFiltrarD() {
        if (tipoListaD == 0) {
            tipoListaD = 1;
        }
    }

    //Ubicacion Celda Arriba 
    public void cambiarVigencia() {
        //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
        //{
        if (listaVigenciasRetencionesCrear.isEmpty() && listaVigenciasRetencionesBorrar.isEmpty() && listaVigenciasRetencionesModificar.isEmpty()) {
            secuenciaVigenciaRetencion = vigenciaRetencionSeleccionado.getSecuencia();
            listaRetenciones = null;
            getListaRetenciones();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
            RequestContext.getCurrentInstance().execute("PF('cambiar').show()");

        }
    }

    public void limpiarListas() {
        listaVigenciasRetencionesCrear.clear();
        listaVigenciasRetencionesBorrar.clear();
        listaVigenciasRetencionesModificar.clear();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");

    }

    //BORRAR RETENCION / TABLA RETENCION
    public void borrar() {

        if (index >= 0 && cualTabla == 0) {
            if (listaRetenciones.isEmpty()) {
                if (tipoLista == 0) {
                    if (!listaVigenciasRetencionesModificar.isEmpty() && listaVigenciasRetencionesModificar.contains(listaVigenciasRetenciones.get(index))) {
                        int modIndex = listaVigenciasRetencionesModificar.indexOf(listaVigenciasRetenciones.get(index));
                        listaVigenciasRetencionesModificar.remove(modIndex);
                        listaVigenciasRetencionesBorrar.add(listaVigenciasRetenciones.get(index));
                    } else if (!listaVigenciasRetencionesCrear.isEmpty() && listaVigenciasRetencionesCrear.contains(listaVigenciasRetenciones.get(index))) {
                        int crearIndex = listaVigenciasRetencionesCrear.indexOf(listaVigenciasRetenciones.get(index));
                        listaVigenciasRetencionesCrear.remove(crearIndex);
                    } else {
                        listaVigenciasRetencionesBorrar.add(listaVigenciasRetenciones.get(index));
                    }
                    listaVigenciasRetenciones.remove(index);
                }

                if (tipoLista == 1) {
                    if (!listaVigenciasRetencionesModificar.isEmpty() && listaVigenciasRetencionesModificar.contains(filtradoListaVigenciasRetenciones.get(index))) {
                        int modIndex = listaVigenciasRetencionesModificar.indexOf(filtradoListaVigenciasRetenciones.get(index));
                        listaVigenciasRetencionesModificar.remove(modIndex);
                        listaVigenciasRetencionesBorrar.add(filtradoListaVigenciasRetenciones.get(index));
                    } else if (!listaVigenciasRetencionesCrear.isEmpty() && listaVigenciasRetencionesCrear.contains(filtradoListaVigenciasRetenciones.get(index))) {
                        int crearIndex = listaVigenciasRetencionesCrear.indexOf(filtradoListaVigenciasRetenciones.get(index));
                        listaVigenciasRetencionesCrear.remove(crearIndex);
                    } else {
                        listaVigenciasRetencionesBorrar.add(filtradoListaVigenciasRetenciones.get(index));
                    }
                    int CIndex = listaVigenciasRetenciones.indexOf(filtradoListaVigenciasRetenciones.get(index));
                    listaVigenciasRetenciones.remove(CIndex);
                    filtradoListaVigenciasRetenciones.remove(index);
                    log.info("Realizado");
                }

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
                cambiosPagina = false;
                index = -1;
                secRegistro = null;

                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                log.info("No se puede borrar porque tiene registros en la tabla de abajo");
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formularioDialogos:registro");
                RequestContext.getCurrentInstance().execute("PF('registro').show()");
            }
        } else if (indexD >= 0 && cualTabla == 1) {

            if (tipoListaD == 0) {
                if (!listaRetencionesModificar.isEmpty() && listaRetencionesModificar.contains(listaRetenciones.get(indexD))) {
                    int modIndex = listaRetencionesModificar.indexOf(listaRetenciones.get(indexD));
                    listaRetencionesModificar.remove(modIndex);
                    listaRetencionesBorrar.add(listaRetenciones.get(indexD));
                } else if (!listaRetencionesCrear.isEmpty() && listaRetencionesCrear.contains(listaRetenciones.get(indexD))) {
                    int crearIndex = listaRetencionesCrear.indexOf(listaRetenciones.get(indexD));
                    listaRetencionesCrear.remove(crearIndex);
                } else {
                    listaRetencionesBorrar.add(listaRetenciones.get(indexD));
                }
                listaRetenciones.remove(indexD);
            }

            if (tipoListaD == 1) {
                if (!listaRetencionesModificar.isEmpty() && listaRetencionesModificar.contains(filtradoListaRetenciones.get(indexD))) {
                    int modIndex = listaRetencionesModificar.indexOf(filtradoListaRetenciones.get(indexD));
                    listaRetencionesModificar.remove(modIndex);
                    listaRetencionesBorrar.add(filtradoListaRetenciones.get(indexD));
                } else if (!listaRetencionesCrear.isEmpty() && listaRetencionesCrear.contains(filtradoListaRetenciones.get(indexD))) {
                    int crearIndex = listaRetencionesCrear.indexOf(filtradoListaRetenciones.get(indexD));
                    listaRetencionesCrear.remove(crearIndex);
                } else {
                    listaRetencionesBorrar.add(filtradoListaRetenciones.get(indexD));
                }
                int CIndex = listaRetenciones.indexOf(filtradoListaRetenciones.get(indexD));
                listaRetenciones.remove(CIndex);
                filtradoListaRetenciones.remove(indexD);
                log.info("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            indexD = -1;
            secRegistro = null;
            cambiosPagina = false;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void guardarTodo() {
        if (guardado == false) {
            log.info("Realizando Operaciones retenciones");
            if (!listaVigenciasRetencionesBorrar.isEmpty()) {
                for (int i = 0; i < listaVigenciasRetencionesBorrar.size(); i++) {
                    log.info("Borrando...");
                    administrarRetencionesMinimas.borrarVigenciaRetencion(listaVigenciasRetencionesBorrar.get(i));
                    log.info("Entra");
                    listaVigenciasRetencionesBorrar.clear();
                }
            }
            if (!listaVigenciasRetencionesCrear.isEmpty()) {
                for (int i = 0; i < listaVigenciasRetencionesCrear.size(); i++) {
                    log.info("Creando...");
                    log.info(listaVigenciasRetencionesCrear.size());

                    administrarRetencionesMinimas.crearVigenciaRetencion(listaVigenciasRetencionesCrear.get(i));
                }

                log.info("LimpiaLista");
                listaVigenciasRetencionesCrear.clear();
            }
            if (!listaVigenciasRetencionesModificar.isEmpty()) {
                administrarRetencionesMinimas.modificarVigenciaRetencion(listaVigenciasRetencionesModificar);
                listaVigenciasRetencionesModificar.clear();
            }

            log.info("Se guardaron los datos con exito");
            listaVigenciasRetenciones = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            guardado = true;
            permitirIndex = true;
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            //  k = 0;
        }
        log.info("Valor k: " + k);
        index = -1;
        secRegistro = null;

        if (guardado == false) {
            log.info("Realizando Operaciones VigenciasNoFormales");
            if (!listaRetencionesBorrar.isEmpty()) {
                for (int i = 0; i < listaRetencionesBorrar.size(); i++) {
                    log.info("Borrando...");
                    administrarRetencionesMinimas.borrarRetencion(listaRetencionesBorrar.get(i));
                }

                log.info("Entra");
                listaRetencionesBorrar.clear();
            }
        }
        if (!listaRetencionesCrear.isEmpty()) {
            for (int i = 0; i < listaRetencionesCrear.size(); i++) {
                log.info("Creando...");
                log.info(listaRetencionesCrear.size());

                administrarRetencionesMinimas.crearRetencion(listaRetencionesCrear.get(i));

            }

            log.info("LimpiaLista");
            listaRetencionesCrear.clear();
        }
        if (!listaRetencionesModificar.isEmpty()) {
            administrarRetencionesMinimas.modificarRetencion(listaRetencionesModificar);

            listaRetencionesModificar.clear();
        }

        log.info("Se guardaron los datos con exito");
        listaRetenciones = null;
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage("Información", "Se han guardado los datos exitosamente.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update("form:datosRetenciones");
        RequestContext.getCurrentInstance().update("form:growl");
        guardado = true;
        permitirIndex = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        //  k = 0;

        log.info("Valor k: " + k);
        indexD = -1;
        cambiosPagina = true;
        secRegistro = null;

    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        log.info("cualTabla= " + cualTabla);
        if (bandera == 0 && cualTabla == 0) {
            log.info("Activa 1");
            //Tabla Vigencias RetencionesMinimas
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
            vCodigo.setFilterStyle("width: 85% !important;");
            vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
            vFechaVigencia.setFilterStyle("width: 85% !important;");
            altoScrollVigenciasRetenciones = "70";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            bandera = 1;

        } else if (bandera == 1 && cualTabla == 0) {
            log.info("Desactiva 1");
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
            vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasRetenciones = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            bandera = 0;
            filtradoListaVigenciasRetenciones = null;

        } else if (bandera == 0 && cualTabla == 1) {
            log.info("Activa 2");
            rMensualizado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rMensualizado");
            rMensualizado.setFilterStyle("width: 85% !important;");
            rRetencion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rRetencion");
            rRetencion.setFilterStyle("width: 85% !important;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("width: 85% !important;");
            rRestaUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rRestaUvt");
            rRestaUvt.setFilterStyle("");
            altoScrollRetenciones = "66";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 1;
            tipoListaD = 1;

        } else if (bandera == 1 && cualTabla == 1) {
            //SOLUCIONES NODOS EMPLEADO
            log.info("Desactiva 2");
            rMensualizado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rMensualizado");
            rMensualizado.setFilterStyle("display: none; visibility: hidden;");
            rRetencion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rRetencion");
            rRetencion.setFilterStyle("display: none; visibility: hidden;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            rRestaUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rRestaUvt");
            rRestaUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollRetenciones = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 0;
            tipoListaD = 0;
            filtradoListaRetenciones = null;
        }
    }

    public void salir() {  limpiarListasValor();
        if (bandera == 1) {
            log.info("Desactiva 1");
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
            vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasRetenciones = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            bandera = 0;
            filtradoListaVigenciasRetenciones = null;
            altoScrollVigenciasRetenciones = "90";

            bandera = 0;
            filtradoListaVigenciasRetenciones = null;
            tipoLista = 0;
        }

        if (bandera == 1) {
            //SOLUCIONES NODOS EMPLEADO
            log.info("Desactiva 2");
            rMensualizado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rMensualizado");
            rMensualizado.setFilterStyle("display: none; visibility: hidden;");
            rRetencion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rRetencion");
            rRetencion.setFilterStyle("display: none; visibility: hidden;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            rRestaUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rRestaUvt");
            rRestaUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollRetenciones = "90";
            bandera = 0;
            tipoListaD = 0;
            filtradoListaRetenciones = null;

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 0;
            filtradoListaRetenciones = null;
            tipoListaD = 0;
        }
        listaVigenciasRetencionesBorrar.clear();
        listaVigenciasRetencionesCrear.clear();
        listaVigenciasRetencionesModificar.clear();
        index = -1;
        secRegistro = null;

        listaVigenciasRetenciones = null;

        listaRetencionesBorrar.clear();
        listaRetencionesCrear.clear();
        listaRetencionesModificar.clear();
        indexD = -1;

        listaRetenciones = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
        RequestContext.getCurrentInstance().update("form:datosRetenciones");

    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            log.info("Desactiva 1");
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
            vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasRetenciones = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            bandera = 0;
            filtradoListaVigenciasRetenciones = null;
            altoScrollVigenciasRetenciones = "90";

            bandera = 0;
            filtradoListaVigenciasRetenciones = null;
            tipoLista = 0;
        }

        if (bandera == 1) {
            //SOLUCIONES NODOS EMPLEADO
            log.info("Desactiva 2");
            rMensualizado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rMensualizado");
            rMensualizado.setFilterStyle("display: none; visibility: hidden;");
            rRetencion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rRetencion");
            rRetencion.setFilterStyle("display: none; visibility: hidden;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            rRestaUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rRestaUvt");
            rRestaUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollRetenciones = "90";
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetencionesDetalles");
            bandera = 0;
            filtradoListaRetenciones = null;
            tipoListaD = 0;
        }
        listaVigenciasRetencionesBorrar.clear();
        listaVigenciasRetencionesCrear.clear();
        listaVigenciasRetencionesModificar.clear();
        index = -1;
        secRegistro = null;

        listaVigenciasRetenciones = null;

        listaRetencionesBorrar.clear();
        listaRetencionesCrear.clear();
        listaRetencionesModificar.clear();
        indexD = -1;

        listaRetenciones = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
        RequestContext.getCurrentInstance().update("form:datosVigenciasRetencionesDetalles");
    }

    //GUARDAR
    public void guardarCambiosRetenciones() {
        if (cualTabla == 0) {
            log.info("Guardado: " + guardado);
            if (guardado == false) {
                log.info("Realizando Operaciones RetencionesMinimas");
                if (!listaVigenciasRetencionesBorrar.isEmpty()) {
                    for (int i = 0; i < listaVigenciasRetencionesBorrar.size(); i++) {
                        log.info("Borrando...");

                        administrarRetencionesMinimas.borrarVigenciaRetencion(listaVigenciasRetencionesBorrar.get(i));

                        log.info("Entra");
                        listaVigenciasRetencionesBorrar.clear();
                    }
                }
                if (!listaVigenciasRetencionesCrear.isEmpty()) {
                    for (int i = 0; i < listaVigenciasRetencionesCrear.size(); i++) {
                        log.info("Creando...");
                        log.info(listaVigenciasRetencionesCrear.size());

                        administrarRetencionesMinimas.crearVigenciaRetencion(listaVigenciasRetencionesCrear.get(i));
                    }

                    log.info("LimpiaLista");
                    listaVigenciasRetencionesCrear.clear();
                }
                if (!listaVigenciasRetencionesModificar.isEmpty()) {
                    administrarRetencionesMinimas.modificarVigenciaRetencion(listaVigenciasRetencionesModificar);
                    listaVigenciasRetencionesModificar.clear();
                }

                log.info("Se guardaron los datos con exito");
                listaVigenciasRetenciones = null;
                RequestContext context = RequestContext.getCurrentInstance();
                FacesMessage msg = new FacesMessage("Información", "Se han guardado los datos exitosamente.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
                guardado = true;
                permitirIndex = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                //  k = 0;
            }
            log.info("Tamaño lista: " + listaVigenciasRetencionesCrear.size());
            log.info("Valor k: " + k);
            index = -1;
            secRegistro = null;

        } else {

            log.info("Está en la Tabla de Abajo");

            if (guardado == false) {
                log.info("Realizando Operaciones VigenciasNoFormales");
                if (!listaRetencionesBorrar.isEmpty()) {
                    for (int i = 0; i < listaRetencionesBorrar.size(); i++) {
                        log.info("Borrando...");

                        administrarRetencionesMinimas.borrarRetencion(listaRetencionesBorrar.get(i));
                    }

                    log.info("Entra");
                    listaRetencionesBorrar.clear();
                }
            }
            if (!listaRetencionesCrear.isEmpty()) {
                for (int i = 0; i < listaRetencionesCrear.size(); i++) {
                    log.info("Creando...");
                    log.info(listaRetencionesCrear.size());

                    administrarRetencionesMinimas.crearRetencion(listaRetencionesCrear.get(i));

                }

                log.info("LimpiaLista");
                listaRetencionesCrear.clear();
            }
            if (!listaRetencionesModificar.isEmpty()) {
                administrarRetencionesMinimas.modificarRetencion(listaRetencionesModificar);

                listaRetencionesModificar.clear();
            }

            log.info("Se guardaron los datos con exito");
            listaRetenciones = null;
            RequestContext context = RequestContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage("Información", "Se han guardado los datos exitosamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            RequestContext.getCurrentInstance().update("form:growl");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            //  k = 0;
        }
        log.info("Valor k: " + k);
        indexD = -1;
        secRegistro = null;

    }

//CREAR Retencion
    public void agregarNuevoRetencion() {
        int pasa = 0;
        int pasar = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoRetencion.getMensualizado() == null) {
            mensajeValidacion = mensajeValidacion + " * Mensualizado\n";
            pasa++;
        }

        if (nuevoRetencion.getRetencion() == null) {
            mensajeValidacion = mensajeValidacion + " * Retencion en Uvt\n";
            pasa++;
        }

        if (nuevoRetencion.getPorcentaje() == null) {
            mensajeValidacion = mensajeValidacion + " * Porcentaje \n";
            pasa++;
        }

        if (nuevoRetencion.getRestauvt() == null) {
            mensajeValidacion = mensajeValidacion + " * Resta UVT\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
        }

        if (pasa == 0 && pasar == 0) {
            if (bandera == 1) {
                //SOLUCIONES NODOS EMPLEADO
                log.info("Desactiva 2");
                rMensualizado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rMensualizado");
                rMensualizado.setFilterStyle("display: none; visibility: hidden;");
                rRetencion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rRetencion");
                rRetencion.setFilterStyle("display: none; visibility: hidden;");
                rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
                rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
                rRestaUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rRestaUvt");
                rRestaUvt.setFilterStyle("display: none; visibility: hidden;");
                altoScrollRetenciones = "90";
                RequestContext.getCurrentInstance().update("form:datosRetenciones");
                bandera = 0;
                filtradoListaRetenciones = null;
                tipoListaD = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoRetencion.setSecuencia(l);
            log.info("vigenciaRetencionSeleccionado" + vigenciaRetencionSeleccionado.getCodigo());
            nuevoRetencion.setVigenciaretencionminima(vigenciaRetencionSeleccionado);
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaRetencionesCrear.add(nuevoRetencion);
            listaRetenciones.add(nuevoRetencion);

            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroRetenciones').hide()");
            nuevoRetencion = new RetencionesMinimas();
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroRetenciones");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (pasa == 0) {
            listaVigenciasRetenciones.add(duplicarVigenciasRetenciones);
            listaVigenciasRetencionesCrear.add(duplicarVigenciasRetenciones);
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
                vCodigo.setFilterStyle("display: none; visibility: hidden;");
                vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
                vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                altoScrollVigenciasRetenciones = "90";
                RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
                bandera = 0;
                filtradoListaVigenciasRetenciones = null;
                tipoLista = 0;
            }
        }
        duplicarVigenciasRetenciones = new VigenciasRetencionesMinimas();
    }

    public void confirmarDuplicarD() {

        listaRetenciones.add(duplicarRetencion);
        listaRetencionesCrear.add(duplicarRetencion);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosRetenciones");
        index = -1;
        secRegistro = null;
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            rMensualizado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rMensualizado");
            rMensualizado.setFilterStyle("display: none; visibility: hidden;");
            rRetencion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rRetencion");
            rRetencion.setFilterStyle("display: none; visibility: hidden;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            rRestaUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rRestaUvt");
            rRestaUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollRetenciones = "90";
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 0;
            filtradoListaRetenciones = null;
            tipoListaD = 0;
        }

        duplicarRetencion = new RetencionesMinimas();
    }

    //DUPLICAR VIGENCIAS RETENCIONES/RETENCIONES
    public void duplicarE() {
        if (index >= 0 && cualTabla == 0) {
            duplicarVigenciasRetenciones = new VigenciasRetencionesMinimas();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarVigenciasRetenciones.setSecuencia(l);
                duplicarVigenciasRetenciones.setCodigo(listaVigenciasRetenciones.get(index).getCodigo());
                duplicarVigenciasRetenciones.setFechavigencia(listaVigenciasRetenciones.get(index).getFechavigencia());
            }
            if (tipoLista == 1) {
                duplicarVigenciasRetenciones.setSecuencia(l);
                duplicarVigenciasRetenciones.setCodigo(filtradoListaVigenciasRetenciones.get(index).getCodigo());
                duplicarVigenciasRetenciones.setFechavigencia(filtradoListaVigenciasRetenciones.get(index).getFechavigencia());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaRetencion");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciasRetenciones').show()");
            index = -1;
            secRegistro = null;
        } else if (indexD >= 0 && cualTabla == 1) {
            log.info("Entra Duplicar Detalle Embargo");

            duplicarRetencion = new RetencionesMinimas();
            m++;
            n = BigInteger.valueOf(m);

            if (tipoListaD == 0) {
                duplicarRetencion.setSecuencia(n);
                duplicarRetencion.setMensualizado(listaRetenciones.get(indexD).getMensualizado());
                duplicarRetencion.setRetencion(listaRetenciones.get(indexD).getRetencion());
                duplicarRetencion.setPorcentaje(listaRetenciones.get(indexD).getPorcentaje());
                duplicarRetencion.setRestauvt(listaRetenciones.get(indexD).getRestauvt());
                duplicarRetencion.setVigenciaretencionminima(listaRetenciones.get(indexD).getVigenciasretencionminima());

            }
            if (tipoListaD == 1) {

                duplicarRetencion.setSecuencia(n);
                duplicarRetencion.setMensualizado(filtradoListaRetenciones.get(indexD).getMensualizado());
                duplicarRetencion.setRetencion(filtradoListaRetenciones.get(indexD).getRetencion());
                duplicarRetencion.setPorcentaje(filtradoListaRetenciones.get(indexD).getPorcentaje());
                duplicarRetencion.setRestauvt(filtradoListaRetenciones.get(indexD).getRestauvt());
                duplicarRetencion.setVigenciaretencionminima(filtradoListaRetenciones.get(indexD).getVigenciasretencionminima());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRetencion");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroRetencion').show()");
            indexD = -1;
            secRegistro = null;

        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0 && cualTabla == 0) {
            if (tipoLista == 0) {
                editarVigenciasRetenciones = listaVigenciasRetenciones.get(index);
            }
            if (tipoLista == 1) {
                editarVigenciasRetenciones = filtradoListaVigenciasRetenciones.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            log.info("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoVR");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoVR').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVigenciaVR");
                RequestContext.getCurrentInstance().execute("PF('editarFechaVigenciaVR').show()");
                cualCelda = -1;
            }
            index = -1;
        } else if (indexD >= 0 && cualTabla == 1) {
            if (tipoListaD == 0) {
                editarRetenciones = listaRetenciones.get(indexD);
            }
            if (tipoListaD == 1) {
                editarRetenciones = filtradoListaRetenciones.get(indexD);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            log.info("Entro a editar... valor celda: " + cualCeldaD);
            log.info("Cual Tabla: " + cualTabla);
            if (cualCeldaD == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMensualizadoR");
                RequestContext.getCurrentInstance().execute("PF('editarMensualizadoR').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarRetencionR");
                RequestContext.getCurrentInstance().execute("PF('editarRetencionR').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPorcentajeR");
                RequestContext.getCurrentInstance().execute("PF('editarPorcentajeR').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarRestaR");
                RequestContext.getCurrentInstance().execute("PF('editarRestaR').show()");
                cualCeldaD = -1;
            }
            indexD = -1;
        }
        secRegistro = null;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void chiste() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (!listaVigenciasRetenciones.isEmpty() && listaRetenciones.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
            RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
        }
        int tamaño = listaVigenciasRetenciones.size();

        if (tamaño == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciasRetenciones");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasRetenciones').show()");
        }

        if (listaRetenciones.isEmpty() && !listaVigenciasRetenciones.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
            RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
        } else if (cualTabla == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciasRetenciones");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasRetenciones').show()");
        } else if (cualTabla == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroRetenciones");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroRetenciones').show()");
        }
    }

    public void dialogoVigencias() {
        cualTabla = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:NuevoRegistroVigenciasRetenciones");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasRetenciones').show()");
    }

    public void dialogoRetenciones() {
        cualTabla = 1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:NuevoRegistroRetenciones");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroRetenciones').show()");
    }

    public void modificarFechas(int i, int c) {
        VigenciasRetencionesMinimas auxiliar = null;
        RequestContext context = RequestContext.getCurrentInstance();

        if (tipoLista == 0) {
            auxiliar = listaVigenciasRetenciones.get(i);
        }
        if (tipoLista == 1) {
            auxiliar = filtradoListaVigenciasRetenciones.get(i);
        }

        if (auxiliar.getFechavigencia() != null) {
            log.info("Yay");
            /*
             if (listaDeclarantes.get(index).getFechafinal().before(listaDeclarantes.get(index).getFechainicial())) {
             listaDeclarantes.get(index).setFechafinal(fechaFinal);
             RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
             RequestContext.getCurrentInstance().execute("PF('fechas').show()");
             RequestContext.getCurrentInstance().update("form:datosDeclarantes");
             }

             if (listaDeclarantes.get(index).getFechainicial().after(listaDeclarantes.get(index).getFechafinal())) {
             listaDeclarantes.get(index).setFechainicial(fechaInicial);
             RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
             RequestContext.getCurrentInstance().execute("PF('fechas').show()");
             RequestContext.getCurrentInstance().update("form:datosDeclarantes");
             }
             */
        } else {
            if (tipoLista == 0) {
                listaVigenciasRetenciones.get(i).setFechavigencia(fechaVigencia);
            }
            if (tipoLista == 1) {
                filtradoListaVigenciasRetenciones.get(i).setFechavigencia(fechaVigencia);

            }
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
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
            VigenciasRetencionesMinimas auxiliar = null;
            if (tipoLista == 0) {
                auxiliar = listaVigenciasRetenciones.get(index);
            }
            if (tipoLista == 1) {
                auxiliar = filtradoListaVigenciasRetenciones.get(index);
            }

        }

        return retorno;
    }

    //AUTOCOMPLETAR Vigencias
    public void modificarVigenciasRetenciones(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaVigenciasRetencionesCrear.contains(listaVigenciasRetenciones.get(index))) {

                    if (listaVigenciasRetencionesModificar.isEmpty()) {
                        listaVigenciasRetencionesModificar.add(listaVigenciasRetenciones.get(index));
                    } else if (!listaVigenciasRetencionesModificar.contains(listaVigenciasRetenciones.get(index))) {
                        listaVigenciasRetencionesModificar.add(listaVigenciasRetenciones.get(index));
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
                if (!listaVigenciasRetencionesCrear.contains(filtradoListaVigenciasRetenciones.get(index))) {

                    if (listaVigenciasRetencionesCrear.isEmpty()) {
                        listaVigenciasRetencionesCrear.add(filtradoListaVigenciasRetenciones.get(index));
                    } else if (!listaVigenciasRetencionesCrear.contains(filtradoListaVigenciasRetenciones.get(index))) {
                        listaVigenciasRetencionesCrear.add(filtradoListaVigenciasRetenciones.get(index));
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
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
        }
    }

    //AUTOCOMPLETAR RetencionesMinimas
    public void modificarRetenciones(int indiceD, String confirmarCambio, String valorConfirmar) {
        indexD = indiceD;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaRetencionesCrear.contains(listaRetenciones.get(indexD))) {

                    if (listaRetencionesModificar.isEmpty()) {
                        listaRetencionesModificar.add(listaRetenciones.get(indexD));
                    } else if (!listaRetencionesModificar.contains(listaRetenciones.get(indexD))) {
                        listaRetencionesModificar.add(listaRetenciones.get(indexD));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                indexD = -1;
                secRegistro = null;
            } else {
                if (!listaRetencionesCrear.contains(filtradoListaRetenciones.get(indexD))) {

                    if (listaRetencionesCrear.isEmpty()) {
                        listaRetencionesCrear.add(filtradoListaRetenciones.get(indexD));
                    } else if (!listaRetencionesCrear.contains(filtradoListaRetenciones.get(indexD))) {
                        listaRetencionesCrear.add(filtradoListaRetenciones.get(indexD));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                indexD = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
        }

    }

    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            cualTabla = 0;
            tablaImprimir = ":formExportar:datosVigenciasRetencionesExportar";
            nombreArchivo = "VigenciasRetencionesXML";
            log.info("CualTabla = " + cualTabla);
            fechaVigencia = listaVigenciasRetenciones.get(index).getFechavigencia();
            vigenciaRetencionSeleccionado = listaVigenciasRetenciones.get(index);
            cambiarVigencia();

            if (tipoLista == 0) {
                secRegistro = listaVigenciasRetenciones.get(index).getSecuencia();

            } else {
                secRegistro = filtradoListaVigenciasRetenciones.get(index).getSecuencia();
            }
        }
    }

    //UBICACION CELDA
    public void cambiarIndiceD(int indiceD, int celda) {
        if (permitirIndex == true) {
            indexD = indiceD;
            cualCeldaD = celda;
            cualTabla = 1;
            tablaImprimir = ":formExportar:datosRetencionesExportar";
            nombreArchivo = "RetencionesXML";
            log.info("CualTabla = " + cualTabla);
            retencionSeleccionado = listaRetenciones.get(indexD);
            if (tipoLista == 0) {
                secRegistro = listaRetenciones.get(indexD).getSecuencia();

            } else {
                secRegistro = filtradoListaRetenciones.get(indexD).getSecuencia();
            }
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        if (cualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasRetencionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDFTablasAnchas();
            exporter.export(context, tabla, "VigenciasRetencionesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            index = -1;
            secRegistro = null;
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosRetencionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDFTablasAnchas();
            exporter.export(context, tabla, "RetencionesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexD = -1;
            secRegistro = null;
        }
    }

    public void exportXLS() throws IOException {
        if (cualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasRetencionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "VigenciasRetencionesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            index = -1;
            secRegistro = null;
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosRetencionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "RetencionesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexD = -1;
            secRegistro = null;
        }
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevoVigencia() {
        nuevoVigenciasRetenciones = new VigenciasRetencionesMinimas();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO DETALLE EMBARGO
    public void limpiarNuevoRetencion() {
        nuevoRetencion = new RetencionesMinimas();
        indexD = -1;
        secRegistro = null;
    }

    //LIMPIAR DUPLICAR
    public void limpiarduplicarVigencia() {
        duplicarVigenciasRetenciones = new VigenciasRetencionesMinimas();
    }
    //LIMPIAR DUPLICAR NO FORMAL

    public void limpiarduplicarRetencion() {
        duplicarRetencion = new RetencionesMinimas();
    }

    public void verificarRastro() {
        if (cualTabla == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            log.info("lol");
            if (!listaVigenciasRetenciones.isEmpty()) {
                if (secRegistro != null) {
                    log.info("lol 2");
                    int resultado = administrarRastros.obtenerTabla(secRegistro, "VIGENCIASRETENCIONES");
                    log.info("resultado: " + resultado);
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
                } else {
                    RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASRETENCIONES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
            index = -1;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            log.info("D");
            if (!listaRetenciones.isEmpty()) {
                if (secRegistro != null) {
                    log.info("NF2");
                    int resultadoNF = administrarRastros.obtenerTabla(secRegistro, "RETENCIONES");
                    log.info("resultado: " + resultadoNF);
                    if (resultadoNF == 1) {
                        RequestContext.getCurrentInstance().execute("PF('errorObjetosDBNF').show()");
                    } else if (resultadoNF == 2) {
                        RequestContext.getCurrentInstance().execute("PF('confirmarRastroNF').show()");
                    } else if (resultadoNF == 3) {
                        RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroNF').show()");
                    } else if (resultadoNF == 4) {
                        RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroNF').show()");
                    } else if (resultadoNF == 5) {
                        RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroNF').show()");
                    }
                } else {
                    RequestContext.getCurrentInstance().execute("PF('seleccionarRegistroNF').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("RETENCIONES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoNF').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoNF').show()");
            }
            indexD = -1;
        }

    }

    //Getter & Setter
    public List<VigenciasRetencionesMinimas> getListaVigenciasRetenciones() {
        if (listaVigenciasRetenciones == null) {
            listaVigenciasRetenciones = administrarRetencionesMinimas.consultarVigenciasRetenciones();
            if (!listaVigenciasRetenciones.isEmpty()) {
                vigenciaRetencionSeleccionado = listaVigenciasRetenciones.get(0);
                secuenciaVigenciaRetencion = vigenciaRetencionSeleccionado.getSecuencia();
            }
        }
        return listaVigenciasRetenciones;
    }

    public void setListaVigenciasRetenciones(List<VigenciasRetencionesMinimas> listaVigenciasRetenciones) {
        this.listaVigenciasRetenciones = listaVigenciasRetenciones;
    }

    public List<VigenciasRetencionesMinimas> getFiltradoListaVigenciasRetenciones() {
        return filtradoListaVigenciasRetenciones;
    }

    public void setFiltradoListaVigenciasRetenciones(List<VigenciasRetencionesMinimas> filtradoListaVigenciasRetenciones) {
        this.filtradoListaVigenciasRetenciones = filtradoListaVigenciasRetenciones;
    }

    public VigenciasRetencionesMinimas getVigenciaRetencionSeleccionado() {
        return vigenciaRetencionSeleccionado;
    }

    public void setVigenciaRetencionSeleccionado(VigenciasRetencionesMinimas vigenciaRetencionSeleccionado) {
        this.vigenciaRetencionSeleccionado = vigenciaRetencionSeleccionado;
    }

    public int getRegistroActual() {
        return registroActual;
    }

    public void setRegistroActual(int registroActual) {
        this.registroActual = registroActual;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<RetencionesMinimas> getListaRetenciones() {
        if (listaRetenciones == null && vigenciaRetencionSeleccionado != null) {
            listaRetenciones = administrarRetencionesMinimas.consultarRetenciones(secuenciaVigenciaRetencion);
        }
        return listaRetenciones;
    }

    public void setListaRetenciones(List<RetencionesMinimas> listaRetenciones) {
        this.listaRetenciones = listaRetenciones;
    }

    public List<RetencionesMinimas> getFiltradoListaRetenciones() {
        return filtradoListaRetenciones;
    }

    public void setFiltradoListaRetenciones(List<RetencionesMinimas> filtradoListaRetenciones) {
        this.filtradoListaRetenciones = filtradoListaRetenciones;
    }

    public RetencionesMinimas getRetencionSeleccionado() {
        return retencionSeleccionado;
    }

    public void setRetencionSeleccionado(RetencionesMinimas retencionSeleccionado) {
        this.retencionSeleccionado = retencionSeleccionado;
    }

    public String getAltoScrollVigenciasRetenciones() {
        return altoScrollVigenciasRetenciones;
    }

    public void setAltoScrollVigenciasRetenciones(String altoScrollVigenciasRetenciones) {
        this.altoScrollVigenciasRetenciones = altoScrollVigenciasRetenciones;
    }

    public String getAltoScrollRetenciones() {
        return altoScrollRetenciones;
    }

    public void setAltoScrollRetenciones(String altoScrollRetenciones) {
        this.altoScrollRetenciones = altoScrollRetenciones;
    }

    public VigenciasRetencionesMinimas getEditarVigenciasRetenciones() {
        return editarVigenciasRetenciones;
    }

    public void setEditarVigenciasRetenciones(VigenciasRetencionesMinimas editarVigenciasRetenciones) {
        this.editarVigenciasRetenciones = editarVigenciasRetenciones;
    }

    public RetencionesMinimas getEditarRetenciones() {
        return editarRetenciones;
    }

    public void setEditarRetenciones(RetencionesMinimas editarRetenciones) {
        this.editarRetenciones = editarRetenciones;
    }

    public String getTablaImprimir() {
        return tablaImprimir;
    }

    public void setTablaImprimir(String tablaImprimir) {
        this.tablaImprimir = tablaImprimir;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public VigenciasRetencionesMinimas getNuevoVigenciasRetenciones() {
        return nuevoVigenciasRetenciones;
    }

    public void setNuevoVigenciasRetenciones(VigenciasRetencionesMinimas nuevoVigenciasRetenciones) {
        this.nuevoVigenciasRetenciones = nuevoVigenciasRetenciones;
    }

    public RetencionesMinimas getNuevoRetencion() {
        return nuevoRetencion;
    }

    public void setNuevoRetencion(RetencionesMinimas nuevoRetencion) {
        this.nuevoRetencion = nuevoRetencion;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public VigenciasRetencionesMinimas getDuplicarVigenciasRetenciones() {
        return duplicarVigenciasRetenciones;
    }

    public void setDuplicarVigenciasRetenciones(VigenciasRetencionesMinimas duplicarVigenciasRetenciones) {
        this.duplicarVigenciasRetenciones = duplicarVigenciasRetenciones;
    }

    public RetencionesMinimas getDuplicarRetencion() {
        return duplicarRetencion;
    }

    public void setDuplicarRetencion(RetencionesMinimas duplicarRetencion) {
        this.duplicarRetencion = duplicarRetencion;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

}
