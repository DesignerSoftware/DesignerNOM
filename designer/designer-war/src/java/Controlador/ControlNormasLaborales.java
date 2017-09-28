/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.NormasLaborales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNormasLaboralesInterface;
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
 * @author John Pineda
 */
@ManagedBean
@SessionScoped
public class ControlNormasLaborales implements Serializable {

   private static Logger log = Logger.getLogger(ControlNormasLaborales.class);

    @EJB
    AdministrarNormasLaboralesInterface administrarNormasLaborales;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<NormasLaborales> listNormasLaborales;
    private List<NormasLaborales> filtrarNormasLaborales;
    private List<NormasLaborales> crearNormaLaboral;
    private List<NormasLaborales> modificarNormaLaboral;
    private List<NormasLaborales> borrarNormaLaboral;
    private NormasLaborales nuevoNormaLaboral;
    private NormasLaborales duplicarNormaLaboral;
    private NormasLaborales editarNormaLaboral;
    private NormasLaborales normaLaboralSeleccionada;
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
    private BigInteger borradoVC;
    private String backUpDescripcion;
    private Integer backUpCodigo;
    private int tamano;
    //    
    private String infoRegistro;
    //
    private DataTable tablaC;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlNormasLaborales() {
        listNormasLaborales = null;
        normaLaboralSeleccionada = null;
        crearNormaLaboral = new ArrayList<NormasLaborales>();
        modificarNormaLaboral = new ArrayList<NormasLaborales>();
        borrarNormaLaboral = new ArrayList<NormasLaborales>();
        permitirIndex = true;
        editarNormaLaboral = new NormasLaborales();
        nuevoNormaLaboral = new NormasLaborales();
        duplicarNormaLaboral = new NormasLaborales();
        guardado = true;
        tamano = 330;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        getListNormasLaborales();
        if (listNormasLaborales != null) {
            if (!listNormasLaborales.isEmpty()) {
                normaLaboralSeleccionada = listNormasLaborales.get(0);
            }
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        getListNormasLaborales();
        if (listNormasLaborales != null) {
            if (!listNormasLaborales.isEmpty()) {
                normaLaboralSeleccionada = listNormasLaborales.get(0);
            }
        }
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "normalaboral";
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
            administrarNormasLaborales.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        normaLaboralSeleccionada = null;
        contarRegistros();
    }

    public void cambiarIndice(NormasLaborales normaL, int celda) {
        log.error("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            normaLaboralSeleccionada = normaL;
            cualCelda = celda;
            if (cualCelda == 0) {
                backUpCodigo = normaLaboralSeleccionada.getCodigo();
                log.info(" backUpCodigo : " + backUpCodigo);
            } else if (cualCelda == 1) {
                backUpDescripcion = normaLaboralSeleccionada.getNombre();
                log.info(" backUpDescripcion : " + backUpDescripcion);
            }
//                secRegistro = normaLaboralSeleccionada.getSecuencia();
        }
        log.info("Indice: " + normaLaboralSeleccionada + " Celda: " + cualCelda);
    }

    public void asignarIndex(NormasLaborales normaL, int LND, int dig) {
        try {
            log.info("\n ENTRE A ControlNormasLaborales.asignarIndex \n");
            normaLaboralSeleccionada = normaL;
            RequestContext context = RequestContext.getCurrentInstance();
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                log.info("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            log.warn("Error ControlNormasLaborales.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        cerrarFiltrado();
        borrarNormaLaboral.clear();
        crearNormaLaboral.clear();
        modificarNormaLaboral.clear();
        k = 0;
        listNormasLaborales = null;
        normaLaboralSeleccionada = null;
        guardado = true;
        permitirIndex = true;
        getListNormasLaborales();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosNormaLaboral");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        cerrarFiltrado();
        borrarNormaLaboral.clear();
        crearNormaLaboral.clear();
        modificarNormaLaboral.clear();
        normaLaboralSeleccionada = null;
        k = 0;
        listNormasLaborales = null;
        guardado = true;
        navegar("atras");
    }

    public void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        tamano = 330;
        codigo = (Column) c.getViewRoot().findComponent("form:datosNormaLaboral:codigo");
        codigo.setFilterStyle("display: none; visibility: hidden;");
        descripcion = (Column) c.getViewRoot().findComponent("form:datosNormaLaboral:descripcion");
        descripcion.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:datosNormaLaboral");
        bandera = 0;
        filtrarNormasLaborales = null;
        tipoLista = 0;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 310;
            codigo = (Column) c.getViewRoot().findComponent("form:datosNormaLaboral:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosNormaLaboral:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosNormaLaboral");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
        }
    }

    public void modificarNormaLaboral(NormasLaborales normaL, String confirmarCambio, String valorConfirmar) {
        log.error("ENTRE A MODIFICAR NORMA LABORAL");
        normaLaboralSeleccionada = normaL;

        int contador = 0;
        boolean banderita = false;
//        Integer a;
//        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        log.error("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            log.error("ENTRE A MODIFICAR NORMA LABORAL, CONFIRMAR CAMBIO ES N");
            if (!crearNormaLaboral.contains(normaLaboralSeleccionada)) {
                if (normaLaboralSeleccionada.getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    normaLaboralSeleccionada.setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < listNormasLaborales.size(); j++) {
                        if (j != listNormasLaborales.indexOf(normaLaboralSeleccionada)) {
                            if (normaLaboralSeleccionada.getCodigo().equals(listNormasLaborales.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                        normaLaboralSeleccionada.setCodigo(backUpCodigo);
                    } else {
                        banderita = true;
                    }

                }
                if (normaLaboralSeleccionada.getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    normaLaboralSeleccionada.setNombre(backUpDescripcion);

                }
                if (normaLaboralSeleccionada.getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    normaLaboralSeleccionada.setNombre(backUpDescripcion);
                    banderita = false;
                }

                if (banderita == true) {
                    if (modificarNormaLaboral.isEmpty()) {
                        modificarNormaLaboral.add(normaLaboralSeleccionada);
                    } else if (!modificarNormaLaboral.contains(normaLaboralSeleccionada)) {
                        modificarNormaLaboral.add(normaLaboralSeleccionada);
                    }
                    if (guardado) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
            } else if (normaLaboralSeleccionada.getCodigo() == null) {
                mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                banderita = false;
                normaLaboralSeleccionada.setCodigo(backUpCodigo);
            }
            RequestContext.getCurrentInstance().update("form:datosNormaLaboral");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrarNormasLaborales() {

        if (normaLaboralSeleccionada != null) {
            log.info("Entro a borrarNormasLaborales");
            if (!modificarNormaLaboral.isEmpty() && modificarNormaLaboral.contains(normaLaboralSeleccionada)) {
                int modIndex = modificarNormaLaboral.indexOf(normaLaboralSeleccionada);
                modificarNormaLaboral.remove(modIndex);
                borrarNormaLaboral.add(normaLaboralSeleccionada);
            } else if (!crearNormaLaboral.isEmpty() && crearNormaLaboral.contains(normaLaboralSeleccionada)) {
                int crearIndex = crearNormaLaboral.indexOf(normaLaboralSeleccionada);
                crearNormaLaboral.remove(crearIndex);
            } else {
                borrarNormaLaboral.add(normaLaboralSeleccionada);
            }
            listNormasLaborales.remove(normaLaboralSeleccionada);
            if (tipoLista == 1) {
                filtrarNormasLaborales.remove(normaLaboralSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosNormaLaboral");
            //infoRegistro = "Cantidad de registros: " + listNormasLaborales.size();
            contarRegistros();
            normaLaboralSeleccionada = null;
            if (guardado) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        log.info("Estoy en verificarBorrado");
        try {
            borradoVC = administrarNormasLaborales.contarVigenciasNormasEmpleadoNormaLaboral(normaLaboralSeleccionada.getSecuencia());

            if (borradoVC.equals(new BigInteger("0"))) {
                log.info("Borrado==0");
                borrarNormasLaborales();
            } else {
                log.info("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                normaLaboralSeleccionada = null;
                borradoVC = new BigInteger("-1");
            }

        } catch (Exception e) {
            log.error("ERROR ControlNormasLaborales verificarBorrado ERROR  ", e);
        }
    }

    public void guardarNormasLaborales() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("Realizando Normas Labolares");
            if (!borrarNormaLaboral.isEmpty()) {
                administrarNormasLaborales.borrarNormasLaborales(borrarNormaLaboral);
                //mostrarBorrados
                registrosBorrados = borrarNormaLaboral.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarNormaLaboral.clear();
            }
            if (!crearNormaLaboral.isEmpty()) {
                administrarNormasLaborales.crearNormasLaborales(crearNormaLaboral);
                crearNormaLaboral.clear();
            }
            if (!modificarNormaLaboral.isEmpty()) {
                administrarNormasLaborales.modificarNormasLaborales(modificarNormaLaboral);
                modificarNormaLaboral.clear();
            }
            log.info("Se guardaron los datos con exito");
            listNormasLaborales = null;
            getListNormasLaborales();
            if (listNormasLaborales != null) {
                normaLaboralSeleccionada = listNormasLaborales.get(0);
                contarRegistros();
            }
            RequestContext.getCurrentInstance().update("form:datosNormaLaboral");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            k = 0;
            guardado = true;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (normaLaboralSeleccionada != null) {
            editarNormaLaboral = normaLaboralSeleccionada;
            log.info("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            }

        }
    }

    public void agregarNuevoNormaLaboral() {
        log.info("Agregar Norma Laboral");
        int contador = 0;
        int duplicados = 0;

        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoNormaLaboral.getCodigo() == null) {
            mensajeValidacion = " *Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            log.info("codigo en Motivo Cambio Cargo: " + nuevoNormaLaboral.getCodigo());

            for (int x = 0; x < listNormasLaborales.size(); x++) {
                if (listNormasLaborales.get(x).getCodigo().equals(nuevoNormaLaboral.getCodigo())) {
                    duplicados++;
                }
            }
            log.info("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                log.info("Mensaje validacion : " + mensajeValidacion);
            } else {
                log.info("bandera");
                contador++;
            }
        }
        if (nuevoNormaLaboral.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoNormaLaboral.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("bandera");
            contador++;

        }

        log.info("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                cerrarFiltrado();
            }
            log.info("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoNormaLaboral.setSecuencia(l);

            crearNormaLaboral.add(nuevoNormaLaboral);
            listNormasLaborales.add(nuevoNormaLaboral);
            normaLaboralSeleccionada = listNormasLaborales.get(listNormasLaborales.indexOf(nuevoNormaLaboral));
            nuevoNormaLaboral = new NormasLaborales();

            RequestContext.getCurrentInstance().update("form:datosNormaLaboral");
            contarRegistros();
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroNormaLaboral').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoNormaLaboral() {
        log.info("limpiarNuevoNormaLaboral");
        nuevoNormaLaboral = new NormasLaborales();
        normaLaboralSeleccionada = null;

    }

    //------------------------------------------------------------------------------
    public void duplicarNormaLaborales() {
        log.info("duplicarNormaLaboral");
        if (normaLaboralSeleccionada != null) {
            duplicarNormaLaboral = new NormasLaborales();
            k++;
            l = BigInteger.valueOf(k);
            duplicarNormaLaboral.setSecuencia(l);
            duplicarNormaLaboral.setCodigo(normaLaboralSeleccionada.getCodigo());
            duplicarNormaLaboral.setNombre(normaLaboralSeleccionada.getNombre());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNormaLaboral");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroNormasLaborales').show()");
        }
    }

    public void confirmarDuplicar() {
        log.error("ESTOY EN CONFIRMAR DUPLICAR CONTROLNORMASLABORALES");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        log.error("ConfirmarDuplicar codigo " + duplicarNormaLaboral.getCodigo());
        log.error("ConfirmarDuplicar nombre " + duplicarNormaLaboral.getNombre());

        if (duplicarNormaLaboral.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listNormasLaborales.size(); x++) {
                if (listNormasLaborales.get(x).getCodigo().equals(duplicarNormaLaboral.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                log.info("Mensaje validacion : " + mensajeValidacion);
            } else {
                log.info("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarNormaLaboral.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarNormaLaboral.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("bandera");
            contador++;

        }

        if (contador == 2) {
            if (crearNormaLaboral.contains(duplicarNormaLaboral)) {
                log.info("Ya lo contengo.");
            }
            listNormasLaborales.add(duplicarNormaLaboral);
            crearNormaLaboral.add(duplicarNormaLaboral);
            normaLaboralSeleccionada = listNormasLaborales.get(listNormasLaborales.indexOf(duplicarNormaLaboral));
            RequestContext.getCurrentInstance().update("form:datosNormaLaboral");
            contarRegistros();
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                cerrarFiltrado();
            }
            duplicarNormaLaboral = new NormasLaborales();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroNormasLaborales').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarNormasLaborales() {
        duplicarNormaLaboral = new NormasLaborales();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNormaLaboralExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "NormasLaborales", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNormaLaboralExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "NormasLaborales", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (normaLaboralSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(normaLaboralSeleccionada.getSecuencia(), "NORMASLABORALES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("NORMASLABORALES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void recordarSeleccion() {
        if (normaLaboralSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosNormaLaboral");
            tablaC.setSelection(normaLaboralSeleccionada);
        }
    }

    //-------------------------------------------------------------------------- 
    public List<NormasLaborales> getListNormasLaborales() {
        if (listNormasLaborales == null) {
            listNormasLaborales = administrarNormasLaborales.consultarNormasLaborales();
        }
        return listNormasLaborales;
    }

    public void setListNormasLaborales(List<NormasLaborales> listNormasLaborales) {
        this.listNormasLaborales = listNormasLaborales;
    }

    public List<NormasLaborales> getFiltrarNormasLaborales() {
        return filtrarNormasLaborales;
    }

    public void setFiltrarNormasLaborales(List<NormasLaborales> filtrarNormasLaborales) {
        this.filtrarNormasLaborales = filtrarNormasLaborales;
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

    public NormasLaborales getNuevoNormaLaboral() {
        return nuevoNormaLaboral;
    }

    public void setNuevoNormaLaboral(NormasLaborales nuevoNormaLaboral) {
        this.nuevoNormaLaboral = nuevoNormaLaboral;
    }

    public NormasLaborales getDuplicarNormaLaboral() {
        return duplicarNormaLaboral;
    }

    public void setDuplicarNormaLaboral(NormasLaborales duplicarNormaLaboral) {
        this.duplicarNormaLaboral = duplicarNormaLaboral;
    }

    public NormasLaborales getEditarNormaLaboral() {
        return editarNormaLaboral;
    }

    public void setEditarNormaLaboral(NormasLaborales editarNormaLaboral) {
        this.editarNormaLaboral = editarNormaLaboral;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public NormasLaborales getNormaLaboralSeleccionada() {
        return normaLaboralSeleccionada;
    }

    public void setNormaLaboralSeleccionada(NormasLaborales normaLaboralSeleccionada) {
        this.normaLaboralSeleccionada = normaLaboralSeleccionada;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNormaLaboral");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }
}
