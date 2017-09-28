/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import InterfaceAdministrar.AdministrarRastrosInterface;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import Entidades.Empleados;
import Entidades.EnvioCorreos;
import Entidades.Inforeportes;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarEnvioCorreosInterface;
import InterfaceAdministrar.AdministrarNReportesNominaInterface;
import InterfaceAdministrar.AdministrarRegistroEnviosInterface;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlRegistroEnvios")
@SessionScoped
public class ControlRegistroEnvios implements Serializable {

   private static Logger log = Logger.getLogger(ControlRegistroEnvios.class);

    @EJB
    AdministrarRegistroEnviosInterface administrarRegistroEnvio;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarNReportesNominaInterface administrarNReportesNomina;
    @EJB
    AdministrarEnvioCorreosInterface administrarEnvioCorreos;
    @EJB
    AdministarReportesInterface administarReportes;
    //------------------------------------------------------------------------------------------
    //ATRIBUTOS
    //------------------------------------------------------------------------------------------
    private List<EnvioCorreos> enviocorreos;
    private List<EnvioCorreos> filterEC;
    private EnvioCorreos envioSeleccionado;
    private DataTable tablaEC;
    //Pruebas para modificar
    private List<EnvioCorreos> listECModificar;
    private boolean guardado;
    //Reenviar Correo
    private List<EnvioCorreos> listReenvioCorreos;
    //borrar VC
    private List<EnvioCorreos> listECBorrar;
    //editar celda
    private EnvioCorreos editarEC;
    private int cualCelda, tipoLista;
    private boolean aceptar;
    private String infoRegistro;
    private String altoTabla;
    private int bandera;
    //Columnas Tabla VC
    private Column ecFecha, ecEmpleado, ecNombre, ecCorreo, ecEstado;
//
    private Date fecha;
    //AUTOCOMPLETAR
    private String nombre, correo, estado;
    private Empleados empleado;
    private List<Empleados> listCorreoCodigos;
    private Inforeportes reporteActual;
    private String nombreReporte;
    private String pathReporteGenerado;
    private BigInteger secEmpresa;
    private String asunto;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    //private List<Empleados> empl;
    public ControlRegistroEnvios() {
        log.info("ControlRegistroEnvios()");
        bandera = 0;
        enviocorreos = null;
        fecha = new Date();
        aceptar = true;
        listECBorrar = new ArrayList();
        listECModificar = new ArrayList();
        //reenviar
        listReenvioCorreos = new ArrayList();
        //editar
        editarEC = new EnvioCorreos();
        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        altoTabla = "170";
        envioSeleccionado = null;
        correo = " ";
        asunto = "";
        pathReporteGenerado = "";
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
            administrarRegistroEnvio.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            administrarEnvioCorreos.obtenerConexion(ses.getId());
            administarReportes.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.warn("Error Controlador.ControlRegistroEnvios.inicializarAdministrador() ", e);
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
        String pagActual = "registroenvios";
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

//    public void recibirPaginaEntrante(String pagina, Inforeportes secReporte) {
//        log.info("ControlRegistroEnvios.recibirPaginaEntrante()");
//        log.info("Pagina: " + paginaAnterior);
//        log.info("secReporte: " + secReporte);
//
//        paginaAnterior = pagina;
//        recibirReporte(secReporte);
//    }
    public void recibirPaginaEntrante(String pagina, BigInteger secReporte) {
        log.info("ControlRegistroEnvios.recibirPaginaEntrante()");
        log.info("pagina: " + pagina);
        paginaAnterior = pagina;
        Inforeportes reporte = administrarRegistroEnvio.consultarPorSecuencia(secReporte);
        reporteActual = reporte;
        nombreReporte = reporteActual.getNombre();
//        if (reporteActual != null) {
//            nombreReporte = reporteActual.getNombre();
//            log.info("reporte actual: " + reporteActual.getSecuencia());
//            log.info("nombreReporte: " + nombreReporte);
//        }
        //getEnviocorreos();
        //cargarListaCorreos(reporteActual.getSecuencia());
        log.info("reporteActual: " + reporteActual);
    }

    //TOOLBAR
    //GUARDAR CAMBIOS
    public void guardarCambios() {
        log.info("ControlRegistroEnvios.guardarCambios()");
        if (guardado == false) {
            log.info("listECBorrar.size(): " + listECBorrar.size());
            if (!listECBorrar.isEmpty()) {
                for (int i = 0; i < listECBorrar.size(); i++) {
                    administrarRegistroEnvio.borrarEnvioCorreos(listECBorrar.get(i));
                }
                listECBorrar.clear();
            }
            if (!listECModificar.isEmpty()) {
                administrarRegistroEnvio.modificarEC(listECModificar);
                listECModificar.clear();
            }
            log.info("Se guardaron los datos con exito");
            enviocorreos = null;
            getEnviocorreos();
            log.info("enviocorreos: " + enviocorreos);
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosEC");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    //EDITAR
    public void editarCelda() {
        if (enviocorreos != null) {
            editarEC = envioSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
                RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleado");
                RequestContext.getCurrentInstance().execute("PF('editarEmpleado').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombre");
                RequestContext.getCurrentInstance().execute("PF('editarNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCorreo");
                RequestContext.getCurrentInstance().execute("PF('editarCorreo').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEstado");
                RequestContext.getCurrentInstance().execute("PF('editarEstado').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            log.info("Activar");
            ecFecha = (Column) c.getViewRoot().findComponent("form:datosEC:ecFecha");
            ecFecha.setFilterStyle("width: 85% !important;");
            ecEmpleado = (Column) c.getViewRoot().findComponent("form:datosEC:ecEmpleado");
            ecEmpleado.setFilterStyle("width: 85% !important;");
            ecNombre = (Column) c.getViewRoot().findComponent("form:datosEC:ecNombre");
            ecNombre.setFilterStyle("width: 85% !important;");
            ecCorreo = (Column) c.getViewRoot().findComponent("form:datosEC:ecCorreo");
            ecCorreo.setFilterStyle("width: 85% !important;");
            ecEstado = (Column) c.getViewRoot().findComponent("form:datosEC:ecEstado");
            ecEstado.setFilterStyle("width: 85% !important;");
            altoTabla = "150";
            RequestContext.getCurrentInstance().update("form:datosEC");
            bandera = 1;
        } else {
            cerrarFiltrado();
        }
        cualCelda = -1;
    }

    public void cerrarFiltrado() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            ecFecha = (Column) c.getViewRoot().findComponent("form:datosEC:ecFecha");
            ecFecha.setFilterStyle("display: none; visibility: hidden;");
            ecEmpleado = (Column) c.getViewRoot().findComponent("form:datosEC:ecEmpleado");
            ecEmpleado.setFilterStyle("display: none; visibility: hidden;");
            ecNombre = (Column) c.getViewRoot().findComponent("form:datosEC:ecNombre");
            ecNombre.setFilterStyle("display: none; visibility: hidden;");
            ecCorreo = (Column) c.getViewRoot().findComponent("form:datosEC:ecCorreo");
            ecCorreo.setFilterStyle("display: none; visibility: hidden;");
            ecEstado = (Column) c.getViewRoot().findComponent("form:datosEC:ecEstado");
            ecEstado.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "170";
            RequestContext.getCurrentInstance().update("form:datosEC");
            bandera = 0;
            filterEC = null;
            tipoLista = 0;
        }
    }

    //BORRAR EC
    public void borrarEC() {
        log.info("ControlRegistroEnvios.borrarEC()");
        log.info("envioSeleccionado: " + envioSeleccionado);
        if (envioSeleccionado != null) {
            if (!listECModificar.isEmpty() && listECModificar.contains(envioSeleccionado)) {
                int modIndex = listECModificar.indexOf(envioSeleccionado);
                listECModificar.remove(modIndex);
                listECBorrar.add(envioSeleccionado);
            } else {
                listECBorrar.add(envioSeleccionado);
            }
            enviocorreos.remove(envioSeleccionado);
            if (tipoLista == 1) {
                cerrarFiltrado();
            }
            contarRegistros();
            envioSeleccionado = null;
            RequestContext.getCurrentInstance().update("form:datosEC");
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarTodos() {
        log.info(this.getClass().getName() + ".borrarTodos()");
        log.info("enviocorreos.size(): " + enviocorreos.size());
        if (enviocorreos != null || !enviocorreos.isEmpty()) {
            log.info("listECBorrar: " + listECBorrar);
            for (int i = 0; i < enviocorreos.size(); i++) {
                listECBorrar.add(enviocorreos.get(i));
            }
            enviocorreos.removeAll(listECBorrar);
            if (guardado) {
                guardado = false;
            }
            if (tipoLista == 1) {
                cerrarFiltrado();
            }
            enviocorreos.clear();
            RequestContext.getCurrentInstance().update("form:datosEC");
        }
    }

    public void modificarCorreo(EnvioCorreos ecorreos, String valorConfirmar) {
        envioSeleccionado = ecorreos;
//            for (int i = 0; i < enviocorreos.size(); i++) {
//                if (enviocorreos.get(i) == envioSeleccionado) {
//                    i++;
//                    if (i >= enviocorreos.size()) {
//                        break;
//                    }
//                }
//                if (envioSeleccionado.getFecha().compareTo(envioSeleccionado.getFecha()) == 0) {
//                    envioSeleccionado.setFecha(fecha);
//                }
//            }
        if (listECModificar.isEmpty()) {
            listECModificar.add(envioSeleccionado);
        } else if (!listECModificar.contains(envioSeleccionado)) {
            listECModificar.add(envioSeleccionado);
        }
        if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void cancelarModificaciones() {
        log.info("ControlRegistroEnvios.cancelarModificaciones()");
        cerrarFiltrado();
        listECBorrar.clear();
        listECModificar.clear();
        enviocorreos = null;
        envioSeleccionado = null;
        guardado = true;
        //permitirIndex = true;
//        cargarListaCorreos(reporteActual.getSecuencia());
        getEnviocorreos();
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosEC");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        log.info("ControlRegistroEnvios.salir()");
        cerrarFiltrado();
        listECBorrar.clear();
        listECModificar.clear();
        envioSeleccionado = null;
        enviocorreos = null;
        guardado = true;
        navegar("atras");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void eventoFiltrar() {
        envioSeleccionado = null;
        contarRegistros();
    }

    public void cambiarIndice(EnvioCorreos correos, int celda) {
        envioSeleccionado = correos;
        cualCelda = celda;
        switch (cualCelda) {
            case 0:
                fecha = envioSeleccionado.getFecha();
                break;
            case 1:
                empleado = envioSeleccionado.getCodigoEmpleado();
                break;
            case 2:
                nombre = envioSeleccionado.getNombreEmpleado();
                break;
            case 3:
                correo = envioSeleccionado.getCorreo();
                break;
            case 4:
                estado = envioSeleccionado.getEstado();
                break;
            default:
                break;
        }
    }

    public void dispararDialogoGuardarCambios() {
        log.info(this.getClass().getName() + ".dispararDialogoGuardarCambios()");
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

    }

    public void guardarYSalir() {
        log.info(this.getClass().getName() + ".guardarYSalir()");
        guardarCambios();
        salir();
    }

    public void cancelarYSalir() {
//        log.info(this.getClass().getName() + ".cancelarYSalir()");
//        cancelarModificaciones();
        salir();
    }

    public void checkBox(EnvioCorreos reenvio) {
        log.info("ControlRegistroEnvios.reenviarCorreo()");
        log.info("reenvio: " + reenvio);
        envioSeleccionado = reenvio;
        log.info("envioSeleccionado: " + envioSeleccionado);
        log.info("ListReenvioCorreos: " + listReenvioCorreos);
        if (!listReenvioCorreos.contains(envioSeleccionado)) {
            if (listReenvioCorreos.isEmpty()) {
                listReenvioCorreos.add(envioSeleccionado);
            } else if (!listReenvioCorreos.contains(envioSeleccionado)) {
                listReenvioCorreos.add(envioSeleccionado);
            }
        } else if (!listReenvioCorreos.contains(envioSeleccionado)) {
            if (listReenvioCorreos.isEmpty()) {
                listReenvioCorreos.add(envioSeleccionado);
            } else if (!listReenvioCorreos.contains(envioSeleccionado)) {
                listReenvioCorreos.add(envioSeleccionado);
            }
        }
        log.info("Reenviar: " + reenvio.getReenviar());
    }

    //METODOS REENVIAR
    public void reenviarCorreo() {
        log.info("ControlRegistroEnvios.reenviarCorreo()");
        if (envioSeleccionado.getEstadoReenviar()) {
//            getListCorreoCodigos();
//            correo = "";
            String tipoRespCorreo = "D";
            String mensaje = "";
            log.info("correo: " + correo);
            if (listReenvioCorreos != null) {
                if (!listReenvioCorreos.isEmpty()) {
                    log.info("correo: " + correo);
                    for (int i = 0; i < listReenvioCorreos.size(); i++) {
                        log.info("Ingrese a for");
                        String[] msjResul = new String[1];
                        msjResul[0] = "";
                        if (validarCorreo()) {
                            log.info("Entre if validar el correo");
                            Map paramEmpl = new HashMap();
                            paramEmpl.put("empleadoDesde", listReenvioCorreos.get(i).getCodigoEmpleado().getCodigoempleado());
                            paramEmpl.put("empleadoHasta", listReenvioCorreos.get(i).getCodigoEmpleado().getCodigoempleado());
                            pathReporteGenerado = generaReporte(paramEmpl);
                            if (enviarReporteCorreo(secEmpresa, listCorreoCodigos.get(i).getEmailPersona(), asunto,
                                    "Mensaje enviado automáticamente, por favor no responda a este correo.",
                                    pathReporteGenerado, msjResul)) {
                                mensaje = mensaje + " " + msjResul[0];
                                if (!tipoRespCorreo.equalsIgnoreCase("E")) {
                                    tipoRespCorreo = "I";
                                }
                            }
                        }
                    }
                } else {
                    log.info("listReenvioCorreos: Lista Vacia");
                }
            } else {
                log.info("listReenvioCorreos: Lista null");
            }
            mostrarMensajes(tipoRespCorreo, mensaje);
        }
    }

    public boolean validarCorreo() {
        log.info("ControlEnvioCorreos.validarCorreo()");
        if (correo != null) {
            log.info("Ingrese a primer if");
            String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(PATTERN_EMAIL);
            Matcher matcher = pattern.matcher(correo);
            if (matcher.matches()) {
                log.info("Ingrese al segundo if");
                return true;
            } else {
                log.info("Correo Invalido");
//                FacesMessage msg = new FacesMessage("Error", "Correo inválido, por favor verifique.");
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//                RequestContext.getCurrentInstance().update("form:growl");
            }
        } else {
            log.info("Ingrese segundo else");
            return true;
        }
        return false;
    }

    private String generaReporte(Map paramEmpl) {
        log.info("ControlEnvioCorreos.validarEnviaCorreo()");
        String pathReporteGeneradoLoc = administarReportes.generarReporte(reporteActual.getNombrereporte(), reporteActual.getTipo(), paramEmpl);
        log.info("adjunto: " + pathReporteGeneradoLoc);
        return pathReporteGeneradoLoc;
    }

    private boolean enviarReporteCorreo(BigInteger secEmp, String correoE, String asuntoCorreo, String mensaje, String rutaArchivo, String[] msjResul) {
        log.info("ControlRegistroEnvios.enviarReporteCorreo()");
        boolean resultado = false;
        if (administrarEnvioCorreos.enviarCorreo(secEmp, correoE,
                asuntoCorreo, mensaje,
                rutaArchivo, msjResul)) {
            if (msjResul == null || msjResul.length < 1) {
                msjResul[0] = "Enviado sin mensaje de respuesta.";
            }
            resultado = true;
        } else {
            if (msjResul == null || msjResul.length < 1) {
                msjResul[0] = "Error general enviando correo.";
            }
            resultado = false;
        }
        return resultado;
    }

    private void mostrarMensajes(String tipo, String mensaje) {
        FacesMessage msg;
        if (tipo.equalsIgnoreCase("I")) {
            msg = new FacesMessage("Información", mensaje);

        } else if (tipo.equalsIgnoreCase("E")) {
            msg = new FacesMessage("Error", mensaje);
        } else {
            msg = new FacesMessage("Información", "Sin mensaje");
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update("form:growl");
    }

//GET & SET
    public List<EnvioCorreos> getEnviocorreos() {
//        if (enviocorreos == null && reporteActual != null) {
//            cargarListaCorreos(reporteActual.getSecuencia());
//        }
        if (enviocorreos == null) {
            enviocorreos = administrarRegistroEnvio.consultarEnvioCorreos(reporteActual.getSecuencia());
        }
        return enviocorreos;
    }

    public Inforeportes getReporteActual() {
        return reporteActual;
    }

    public void setReporteActual(Inforeportes reporteActual) {
        this.reporteActual = reporteActual;
    }

    public void setEnviocorreos(List<EnvioCorreos> enviocorreos) {
        this.enviocorreos = enviocorreos;
    }

    public List<EnvioCorreos> getFilterEC() {
        return filterEC;
    }

    public void setFilterEC(List<EnvioCorreos> filterEC) {
        this.filterEC = filterEC;
    }

    public EnvioCorreos getEnvioSeleccionado() {
        return envioSeleccionado;
    }

    public void setEnvioSeleccionado(EnvioCorreos envioSeleccionado) {
        this.envioSeleccionado = envioSeleccionado;
    }

    public DataTable getTablaEC() {
        return tablaEC;
    }

    public void setTablaEC(DataTable tablaEC) {
        this.tablaEC = tablaEC;
    }

    public List<EnvioCorreos> getListECModificar() {
        return listECModificar;
    }

    public void setListECModificar(List<EnvioCorreos> listECModificar) {
        this.listECModificar = listECModificar;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public List<EnvioCorreos> getListECBorrar() {
        return listECBorrar;
    }

    public void setListECBorrar(List<EnvioCorreos> listECBorrar) {
        this.listECBorrar = listECBorrar;
    }

    public EnvioCorreos getEditarEC() {
        return editarEC;
    }

    public void setEditarEC(EnvioCorreos editarEC) {
        this.editarEC = editarEC;
    }

    public int getCualCelda() {
        return cualCelda;
    }

    public void setCualCelda(int cualCelda) {
        this.cualCelda = cualCelda;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEC");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public Column getEcFecha() {
        return ecFecha;
    }

    public void setEcFecha(Column ecFecha) {
        this.ecFecha = ecFecha;
    }

    public Column getEcEmpleado() {
        return ecEmpleado;
    }

    public void setEcEmpleado(Column ecEmpleado) {
        this.ecEmpleado = ecEmpleado;
    }

    public Column getEcNombre() {
        return ecNombre;
    }

    public void setEcNombre(Column ecNombre) {
        this.ecNombre = ecNombre;
    }

    public Column getEcEstado() {
        return ecEstado;
    }

    public void setEcEstado(Column ecEstado) {
        this.ecEstado = ecEstado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public List<EnvioCorreos> getListReenvioCorreos() {
        return listReenvioCorreos;
    }

    public void setListReenvioCorreos(List<EnvioCorreos> listReenvioCorreos) {
        this.listReenvioCorreos = listReenvioCorreos;
    }
//
//    public List<Empleados> getListCorreoCodigos() {
//        if (listCorreoCodigos == null || listCorreoCodigos.isEmpty()) {
//            listCorreoCodigos = administrarEnvioCorreos.correoCodigoEmpleado(empleado.getCodigoempleado(), empleado.getCodigoempleado());
//        }
//        return listCorreoCodigos;
//    }
//
//    public void setListCorreoCodigos(List<Empleados> listCorreoCodigos) {
//        this.listCorreoCodigos = listCorreoCodigos;
//    }

    public BigInteger getSecEmpresa() {
        log.info("ControlEnvioCorreos.getSecEmpresa() secEmpresa: " + secEmpresa);
        if (secEmpresa == null) {
            try {
                secEmpresa = empleado.getEmpresa();
            } catch (NullPointerException npe) {
                try {
                    secEmpresa = administrarEnvioCorreos.empresaAsociada();
                } catch (NullPointerException npe2) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", "Por favor seleccione una empresa en los parametros del reporte"));
                    secEmpresa = BigInteger.ZERO;
                }
            }
        }
        return secEmpresa;
    }

    public void setSecEmpresa(BigInteger secEmpresa) {
        this.secEmpresa = secEmpresa;
    }

}
