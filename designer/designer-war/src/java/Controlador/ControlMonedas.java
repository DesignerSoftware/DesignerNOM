/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Monedas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMonedasInterface;
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
public class ControlMonedas implements Serializable {

   private static Logger log = Logger.getLogger(ControlMonedas.class);

    @EJB
    AdministrarMonedasInterface administrarMonedas;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Monedas> listMonedas;
    private List<Monedas> filtrarMonedas;
    private List<Monedas> crearMonedas;
    private List<Monedas> modificarMonedas;
    private List<Monedas> borrarMonedas;
    private Monedas nuevoMoneda;
    private Monedas duplicarMoneda;
    private Monedas editarMoneda;
    private Monedas monedaSeleccionada;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger proyectos;
    private String a;
    private String infoRegistro;
    private String backUpCodigo;
    private String backUpMoneda;
    private int tamano;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlMonedas() {
        listMonedas = null;
        crearMonedas = new ArrayList<Monedas>();
        modificarMonedas = new ArrayList<Monedas>();
        borrarMonedas = new ArrayList<Monedas>();
        permitirIndex = true;
        editarMoneda = new Monedas();
        nuevoMoneda = new Monedas();
        duplicarMoneda = new Monedas();
        a = null;
        tamano = 220;
        guardado = true;
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
        String pagActual = "moneda";
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
            administrarMonedas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            log.info("\n ENTRE A ControlMonedas.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            log.warn("Error ControlMonedas eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void cambiarIndice(Monedas moneda, int celda) {
        log.error("TIPO LISTA = " + tipoLista);
        monedaSeleccionada = moneda;
        if (permitirIndex == true) {
            cualCelda = celda;
            if (cualCelda == 0) {
                backUpCodigo = monedaSeleccionada.getCodigo();
            }
            if (cualCelda == 1) {
                backUpMoneda = monedaSeleccionada.getNombre();
            }
        }
    }

    public void asignarIndex(Monedas moneda, int LND, int dig) {
        monedaSeleccionada = moneda;
        try {
            log.info("\n ENTRE A ControlMonedas.asignarIndex \n");
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                log.info("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            log.warn("Error ControlMonedas.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosMoneda:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMoneda:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            bandera = 0;
            filtrarMonedas = null;
            tipoLista = 0;
        }

        borrarMonedas.clear();
        crearMonedas.clear();
        modificarMonedas.clear();
        monedaSeleccionada = null;
        k = 0;
        listMonedas = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        getListMonedas();
        RequestContext.getCurrentInstance().update("form:datosMoneda");
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosMoneda:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMoneda:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            bandera = 0;
            filtrarMonedas = null;
            tipoLista = 0;
        }

        borrarMonedas.clear();
        crearMonedas.clear();
        modificarMonedas.clear();
        monedaSeleccionada = null;
        k = 0;
        listMonedas = null;
        guardado = true;
        permitirIndex = true;
        contarRegistros();
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 200;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMoneda:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMoneda:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 220;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMoneda:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMoneda:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            bandera = 0;
            filtrarMonedas = null;
            tipoLista = 0;
        }
    }

    public void modificarMoneda(Monedas moneda, String campo, String valor) {
        monedaSeleccionada = moneda;
        int contador = 0, pass = 0;
        if (campo.equalsIgnoreCase("N")) {
            log.error("ENTRE A MODIFICAR IDIOMA, CONFIRMAR CAMBIO ES N");
            if (!crearMonedas.contains(monedaSeleccionada)) {
                if (monedaSeleccionada.getCodigo() == null || monedaSeleccionada.getCodigo().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    monedaSeleccionada.setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < listMonedas.size(); j++) {
                        if (listMonedas.get(j).getSecuencia() != monedaSeleccionada.getSecuencia()) {
                            if (monedaSeleccionada.getCodigo().equals(listMonedas.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        monedaSeleccionada.setCodigo(backUpCodigo);
                    } else {
                        pass++;
                    }
                }
                if (monedaSeleccionada.getNombre() == null) {
                    monedaSeleccionada.setNombre(backUpMoneda);
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                } else if (monedaSeleccionada.getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    monedaSeleccionada.setNombre(backUpMoneda);
                } else {
                    pass++;
                }
                if (pass == 2) {
                    if (modificarMonedas.isEmpty()) {
                        modificarMonedas.add(monedaSeleccionada);
                    } else if (!modificarMonedas.contains(monedaSeleccionada)) {
                        modificarMonedas.add(monedaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
            } else {
                if (monedaSeleccionada.getCodigo() == null || monedaSeleccionada.getCodigo().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    monedaSeleccionada.setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < listMonedas.size(); j++) {
                        if (listMonedas.get(j).getSecuencia() != monedaSeleccionada.getSecuencia()) {
                            if (monedaSeleccionada.getCodigo().equals(listMonedas.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        monedaSeleccionada.setCodigo(backUpCodigo);
                    } else {
                        pass++;
                    }
                }
                if (monedaSeleccionada.getNombre() == null) {
                    monedaSeleccionada.setNombre(backUpMoneda);
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                } else if (monedaSeleccionada.getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    monedaSeleccionada.setNombre(backUpMoneda);
                } else {
                    pass++;
                }
                if (pass == 2) {
                    if (guardado == true) {
                        guardado = false;
                    }
                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosMoneda");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoMonedas() {
        if (monedaSeleccionada != null) {
            if (!modificarMonedas.isEmpty() && modificarMonedas.contains(monedaSeleccionada)) {
                modificarMonedas.remove(monedaSeleccionada);
                borrarMonedas.add(monedaSeleccionada);
            } else if (!crearMonedas.isEmpty() && crearMonedas.contains(monedaSeleccionada)) {
                crearMonedas.remove(monedaSeleccionada);
            } else {
                borrarMonedas.add(monedaSeleccionada);
            }
            listMonedas.remove(monedaSeleccionada);
            if (tipoLista == 1) {
                filtrarMonedas.remove(monedaSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            contarRegistros();
            monedaSeleccionada = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void verificarBorrado() {
        if (monedaSeleccionada != null) {
            proyectos = administrarMonedas.verificarMonedasProyecto(monedaSeleccionada.getSecuencia());
            if (proyectos.equals(new BigInteger("0"))) {
                borrandoMonedas();
            } else {
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                proyectos = new BigInteger("-1");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarMonedas.isEmpty() || !crearMonedas.isEmpty() || !modificarMonedas.isEmpty()) {
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarYSalir() {
        guardarMonedas();
        salir();
    }

    public void cancelarYSalir() {
//        cancelarModificacion();
        salir();
    }

    public void guardarMonedas() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("Realizando guardarMonedas");
            if (!borrarMonedas.isEmpty()) {
                for (int i = 0; i < borrarMonedas.size(); i++) {
                    log.info("Borrando...");
                    administrarMonedas.borrarMonedas(borrarMonedas.get(i));
                }
                registrosBorrados = borrarMonedas.size();
                borrarMonedas.clear();
            }
            if (!crearMonedas.isEmpty()) {
                for (int i = 0; i < crearMonedas.size(); i++) {

                    log.info("Creando...");
                    administrarMonedas.crearMonedas(crearMonedas.get(i));

                }
                crearMonedas.clear();
            }
            if (!modificarMonedas.isEmpty()) {
                administrarMonedas.modificarMonedas(modificarMonedas);
                modificarMonedas.clear();
            }
            monedaSeleccionada = null;
            listMonedas = null;
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            contarRegistros();
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            guardado = true;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void editarCelda() {
        if (monedaSeleccionada != null) {
            editarMoneda = monedaSeleccionada;
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

    public void agregarNuevoMonedas() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        if (nuevoMoneda.getCodigo() == null || nuevoMoneda.getCodigo().isEmpty()) {
            mensajeValidacion = " *Codigo \n";
        } else {
            for (int x = 0; x < listMonedas.size(); x++) {
                if (listMonedas.get(x).getCodigo().equals(nuevoMoneda.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
            } else {
                contador++;
            }
        }
        if (nuevoMoneda.getNombre() == null || nuevoMoneda.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Moneda \n";
        } else {
            contador++;
        }
        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                log.info("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosMoneda:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMoneda:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMoneda");
                bandera = 0;
                filtrarMonedas = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoMoneda.setSecuencia(l);
            crearMonedas.add(nuevoMoneda);
            listMonedas.add(nuevoMoneda);
            monedaSeleccionada = listMonedas.get(listMonedas.indexOf(nuevoMoneda));
            nuevoMoneda = new Monedas();
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            contarRegistros();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMoneda').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMonedas() {
        nuevoMoneda = new Monedas();
    }

    //------------------------------------------------------------------------------
    public void duplicandoMonedas() {
        if (monedaSeleccionada != null) {
            duplicarMoneda = new Monedas();
            k++;
            l = BigInteger.valueOf(k);
            duplicarMoneda.setSecuencia(l);
            duplicarMoneda.setCodigo(monedaSeleccionada.getCodigo());
            duplicarMoneda.setNombre(monedaSeleccionada.getNombre());

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMO");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMoneda').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        if (duplicarMoneda.getCodigo() == null || duplicarMoneda.getCodigo().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   * Codigo \n";
        } else {
            for (int x = 0; x < listMonedas.size(); x++) {
                if (listMonedas.get(x).getCodigo().equals(duplicarMoneda.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
            } else {
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarMoneda.getNombre() == null || duplicarMoneda.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   * Un Moneda \n";
        } else {
            contador++;
        }
        if (contador == 2) {
            listMonedas.add(duplicarMoneda);
            crearMonedas.add(duplicarMoneda);
            monedaSeleccionada = listMonedas.get(listMonedas.indexOf(duplicarMoneda));
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosMoneda:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMoneda:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMoneda");
                bandera = 0;
                filtrarMonedas = null;
                tipoLista = 0;
            }
            duplicarMoneda = new Monedas();
            contarRegistros();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMoneda').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarMonedas() {
        duplicarMoneda = new Monedas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMonedaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MONEDAS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMonedaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MONEDAS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (monedaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(monedaSeleccionada.getSecuencia(), "MONEDAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MONEDAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Monedas> getListMonedas() {
        if (listMonedas == null) {
            listMonedas = administrarMonedas.consultarMonedas();
        }
        return listMonedas;
    }

    public void setListMonedas(List<Monedas> listMonedas) {
        this.listMonedas = listMonedas;
    }

    public List<Monedas> getModificarMonedas() {
        return modificarMonedas;
    }

    public void setModificarMonedas(List<Monedas> modificarMonedas) {
        this.modificarMonedas = modificarMonedas;
    }

    public Monedas getNuevoMoneda() {
        return nuevoMoneda;
    }

    public void setNuevoMoneda(Monedas nuevoMoneda) {
        this.nuevoMoneda = nuevoMoneda;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public List<Monedas> getFiltrarMonedas() {
        return filtrarMonedas;
    }

    public void setFiltrarMonedas(List<Monedas> filtrarMonedas) {
        this.filtrarMonedas = filtrarMonedas;
    }

    public Monedas getEditarMoneda() {
        return editarMoneda;
    }

    public void setEditarMoneda(Monedas editarMoneda) {
        this.editarMoneda = editarMoneda;
    }

    public Monedas getDuplicarMoneda() {
        return duplicarMoneda;
    }

    public void setDuplicarMoneda(Monedas duplicarMoneda) {
        this.duplicarMoneda = duplicarMoneda;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public Monedas getMonedaSeleccionada() {
        return monedaSeleccionada;
    }

    public void setMonedaSeleccionada(Monedas monedaSeleccionada) {
        this.monedaSeleccionada = monedaSeleccionada;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMoneda");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

}
