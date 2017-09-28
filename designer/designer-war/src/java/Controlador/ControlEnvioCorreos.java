package Controlador;

import Entidades.ConfiguracionCorreo;
import Entidades.Empleados;
import Entidades.EnvioCorreos;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
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
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlEnvioCorreos")
@SessionScoped
public class ControlEnvioCorreos implements Serializable {

    private static Logger log = Logger.getLogger(ControlEnvioCorreos.class);

    @EJB
    AdministrarEnvioCorreosInterface administrarEnviosCorreos;
    @EJB
    AdministrarRegistroEnviosInterface administrarRegistroEnvio;
    @EJB
    AdministrarNReportesNominaInterface administrarNReporteNomina;
    @EJB
    AdministarReportesInterface administarReportes;

    private List<Empleados> listCorreoCodigos;
    private List<Empleados> filtrarListEmpleados;
    private List<Empleados> lovEmpleados;
    private Empleados empleadoSeleccionado;
    private EnvioCorreos registrofallocorreo;
    private List<EnvioCorreos> listRegistrosFallos;
    private List<String> correosM;
    private ConfiguracionCorreo correoRemitente;
    private Inforeportes reporteActual;
    private ParametrosReportes codigoParametros;
    private BigInteger secEmpresa;
    private String email, cc, cco, asunto, remitente;
    private String pathReporteGenerado;
    private String nombreReporte;
    private String infoRegistro;
    private boolean aceptar;
    private boolean activoRemitente;
    private BigInteger l;
    private int k;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlEnvioCorreos() {
        listCorreoCodigos = null;
        lovEmpleados = null;
        reporteActual = new Inforeportes();
        empleadoSeleccionado = null;
        registrofallocorreo = new EnvioCorreos();
        codigoParametros = null;
        email = "";
        cc = "";
        cco = "";
        asunto = "";
        remitente = "";
        pathReporteGenerado = "";
        aceptar = true;
        activoRemitente = false;
        listRegistrosFallos = new ArrayList<>();
        k = 0;
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
        String pagActual = "enviocorreo";
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
        lovEmpleados = null;
    }

    @PreDestroy
    public void destruyendoce() {
        log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
    }

    @PostConstruct
    public void iniciarAdministradores() {
        log.info(this.getClass().getName() + ".iniciarAdministradores()");
        try {
            FacesContext contexto = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
            administrarEnviosCorreos.obtenerConexion(ses.getId());
            administrarRegistroEnvio.obtenerConexion(ses.getId());
            administrarNReporteNomina.obtenerConexion(ses.getId());
            administarReportes.obtenerConexion(ses.getId());
            getSecEmpresa();
            if (!validarConfigSMTP()) {
                log.info("Configuración de Servidor SMTP inválida");
                FacesMessage msg = new FacesMessage("Error", "Configuración de Servidor SMTP inválida.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
//                FacesContext.getCurrentInstance().addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", "Configuración de Servidor SMTP inválida."));
            } else {
                log.info("La configuración es valida");
                FacesMessage msg = new FacesMessage("Informacion", "La configuración es valida.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
//                FacesContext.getCurrentInstance().addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion: ", "La configuración es valida."));
            }
            log.info(this.getClass().getName() + " fin de iniciarAdministradores()");
        } catch (Exception e) {
            log.error("Error postconstruct controlEnvioCorreos ", e);
            log.info("Causa: " + e.getMessage());
        }
    }

    public void recibirPaginaEntrante(String pagina, BigInteger secReporte) {
        log.info("ControlRegistroEnvios.recibirPaginaEntrante()");
        log.info("pagina: " + pagina);
        log.info("secReporte: " + secReporte);
        paginaAnterior = pagina;
        Inforeportes reporte = administrarRegistroEnvio.consultarPorSecuencia(secReporte);
        reporteActual = reporte;
        nombreReporte = reporteActual.getNombre();
        getSecEmpresa();
        if (!validarConfigSMTP()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", "Configuración de Servidor SMTP inválida."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion: ", "La configuración es valida."));
        }
//        validarConfigSMTP();
        log.info("NombreReporte: " + nombreReporte);
        log.info("reporteActual: " + reporteActual);
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public boolean validarCorreo() {
        log.info("ControlEnvioCorreos.validarCorreo()");
        System.out.println("email:   " + email);
        if (email != null) {
            if (!email.isEmpty()) {
                String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                Pattern pattern = Pattern.compile(PATTERN_EMAIL);
                Matcher matcher = pattern.matcher(email);
                if (matcher.matches()) {
                    return true;
                } else {
                    log.info("Correo Invalido");
                    FacesMessage msg = new FacesMessage("Error", "Correo inválido, por favor verifique.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                }
            } else {
                log.info("Correo Vacío");
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    public void envioMasivo() {
        if (reporteActual.isEstadoEnvioMasivo() == true) {
            getListCorreoCodigos();
            getCorreosM();
            email = "";
            String tipoRespCorreo = "D";
            String mensaje = "";
            if (listCorreoCodigos != null) {
                if (!listCorreoCodigos.isEmpty()) {
                    for (int i = 0; i < listCorreoCodigos.size(); i++) {
                        String[] msjResul = new String[1];
                        msjResul[0] = "";
                        email = correosM.get(i);
//                        email = listCorreoCodigos.get(i).getEmailPersona();
                        if (validarCorreo()) {
                            Map paramEmpl = new HashMap();
                            paramEmpl.put("empleadoDesde", listCorreoCodigos.get(i).getCodigoempleado());
                            paramEmpl.put("empleadoHasta", listCorreoCodigos.get(i).getCodigoempleado());
                            pathReporteGenerado = generaReporte(paramEmpl);
//                            generaReporte(paramEmpl);
                            if (enviarReporteCorreo(secEmpresa, email, asunto, "Mensaje enviado automáticamente, por favor no responda a este correo.", pathReporteGenerado, msjResul)) {
                                mensaje = mensaje + " " + msjResul[0];
                                if (!tipoRespCorreo.equalsIgnoreCase("E")) {
                                    tipoRespCorreo = "I";
                                }
                                email = "";
                            } else {
                                mensaje = mensaje + " Hubo error en los envíos. " + msjResul[0];
                                tipoRespCorreo = "E";
                            }
                        } else {
                            mensaje = mensaje + " Hubo error en los envíos." + "\n No olvide consultar el Registro de Envíos para verificar el envio." + msjResul[0];
                            log.info("mensaje: " + mensaje);
                            tipoRespCorreo = "E";
                            ///Reportar error en el envio masivo para la tabla.
                            Date fecha = new Date();
                            log.info("fecha: " + fecha);
                            registrofallocorreo.setFecha(fecha);

                            k++;
                            l = BigInteger.valueOf(k);
                            registrofallocorreo.setSecuencia(l);
                            registrofallocorreo.setCodigoEmpleado(listCorreoCodigos.get(i));
                            registrofallocorreo.setNombreEmpleado(listCorreoCodigos.get(i).getNombreCompleto());
                            registrofallocorreo.setCorreo(listCorreoCodigos.get(i).getEmailPersona());
                            registrofallocorreo.setCorreoorigen(remitente);
                            registrofallocorreo.setEstado("NO ENVIADO");
                            registrofallocorreo.setReporte(reporteActual);
                            registrofallocorreo.setReenviar("N");
                            listRegistrosFallos.add(registrofallocorreo);
                            log.info("listRegistrosFallos: " + listRegistrosFallos);
                            if (!listRegistrosFallos.isEmpty()) {
                                for (int j = 0; j < listRegistrosFallos.size(); j++) {
                                    administrarEnviosCorreos.insertarRegistroEnvios(listRegistrosFallos.get(j));
                                }
                                listRegistrosFallos.clear();
                            }
                        }
                    }
                    log.info("Finalizó el ciclo de envío masivo.");
                } else {
                    log.info("Lista vacia");
                }
            } else {
                log.info("Lista null");
            }
            mostrarMensajes(tipoRespCorreo, mensaje);
        } else {
            validarEnviaCorreo();
        }
    }

    private String generaReporte(Map paramEmpl) {
        log.info("ControlEnvioCorreos.generaReporte(Map paramEmpl)");
        System.out.println("paramEmpl: " + paramEmpl);
        //for para que pathReporteGenerado sea para una sola persona
        pathReporteGenerado = administarReportes.generarReporte(reporteActual.getNombrereporte(), reporteActual.getTipo(), paramEmpl);
        log.info("adjunto: " + pathReporteGenerado);
        return pathReporteGenerado;
    }

    private String generaReporte() {
        log.info("ControlEnvioCorreos.generaReporte()");
//        String pathReporteGeneradoLoc = administarReportes.generarReporte(reporteActual.getNombrereporte(), reporteActual.getTipo());
        pathReporteGenerado = administarReportes.generarReporte(reporteActual.getNombrereporte(), reporteActual.getTipo());
        log.info("adjunto: " + pathReporteGenerado);
        return pathReporteGenerado;
    }

    private boolean enviarReporteCorreo(BigInteger secEmp, String correoE, String asuntoCorreo, String mensaje, String rutaArchivo, String[] msjResul) {
        System.out.println("Buenas Estoy enviarReporteCorreo()");
        boolean resultado = false;
        rutaArchivo = pathReporteGenerado;
        if (administrarEnviosCorreos.enviarCorreo(secEmp, correoE,
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

    public void validarEnviaCorreo() {
        generaReporte();
        String[] msjResul = new String[1];
        msjResul[0] = "";

        if (enviarReporteCorreo(secEmpresa, email,
                asunto, "Mensaje enviado automáticamente, por favor no responda a este correo.",
                pathReporteGenerado, msjResul)) {
//            FacesMessage msg = new FacesMessage("Error", "El reporte ha sido enviado exitosamente.");
//            FacesMessage msg = new FacesMessage("Información", msjResul[0]);
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("form:growl");
            mostrarMensajes("I", msjResul[0]);
        } else {
//            FacesMessage msg = new FacesMessage("Error", "No fue posible enviar el correo");
//            FacesMessage msg = new FacesMessage("Error", msjResul[0]);
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("form:growl");
            mostrarMensajes("E", msjResul[0]);
        }
    }

    public boolean validarConfigSMTP() {
        log.info("ControlEnvioCorreos.validarConfigSMTP()");
        return administrarEnviosCorreos.comprobarConfigCorreo(secEmpresa);
    }

    //METODOS DIALOGO
    public void dialogos() {
        cargarListEmpleados();
        RequestContext.getCurrentInstance().update("formDialogos:correoEmpleadosDialogo");
        RequestContext.getCurrentInstance().execute("PF('correoEmpleadosDialogo').show()");
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistro");
    }

    public void activarAceptar() {
        log.info(this.getClass().getName() + ".activarAceptar()");
        aceptar = false;
    }

    public void cargarListEmpleados() {
        if (lovEmpleados == null || lovEmpleados.isEmpty()) {
            lovEmpleados = administrarNReporteNomina.listEmpleados();
        }
    }

    public void actualizarCorreoEmpl() {
        log.info("ControlEnvioCorreos.actualizarCorreoEmpl()");
//        parametroDeReporte.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
//        parametroModificacion = parametroDeReporte;
//        cambiosReporte = false;
        setEmail(empleadoSeleccionado.getEmailPersona());
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovCorreoEmpleado:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCorreoEmpleado').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('correoEmpleadosDialogo').hide()");

        RequestContext.getCurrentInstance().update("formDialogos:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:destinatario");
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
    }

    public void cancelarCambioCorreoEmpl() {
        log.info("ControlEnvioCorreos.cancelarCambioCorreoEmpl()");
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovCorreoEmpleado:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCorreoEmpleado').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('correoEmpleadosDialogo').hide()");
    }

    public void activarRemitente() {
        if (remitente != null) {
            activoRemitente = true;
        }
    }

    public void salir() {
        limpiarListasValor();
        log.info("ControlRegistroEnvios.salir()");
        empleadoSeleccionado = null;
        email = "";
        navegar("atras");
    }

    //GETTER & SETTER
    public List<Empleados> getListCorreoCodigos() {
        getCodigoParametros();
        if (listCorreoCodigos == null || listCorreoCodigos.isEmpty()) {
            listCorreoCodigos = administrarEnviosCorreos.correoCodigoEmpleado(codigoParametros.getCodigoempleadodesde(), codigoParametros.getCodigoempleadohasta());
        }
        return listCorreoCodigos;
    }

    public void setListCorreoCodigos(List<Empleados> listCorreoCodigos) {
        this.listCorreoCodigos = listCorreoCodigos;
    }

    public List<String> getCorreosM() {
        if (correosM == null || correosM.isEmpty()) {
            correosM = administrarEnviosCorreos.correos();
        }
        return correosM;
    }

    public void setCorreosM(List<String> correosM) {
        this.correosM = correosM;
    }

    public String getEmail() {
        log.info("ControlEnvioCorreos.getEmail(). email= " + email);
        if (email == null) {
            email = " ";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public Inforeportes getReporteActual() {
        return reporteActual;
    }

    public void setReporteActual(Inforeportes reporteActual) {
        this.reporteActual = reporteActual;
    }

    public ParametrosReportes getCodigoParametros() {
        log.info("ControlEnvioCorreos.getCodigoParametros()");
        if (codigoParametros == null) {
            codigoParametros = new ParametrosReportes();
            codigoParametros = administrarNReporteNomina.parametrosDeReporte();
        }
        return codigoParametros;
    }

    public void setCodigoParametros(ParametrosReportes codigoParametros) {
        this.codigoParametros = codigoParametros;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public String getPathReporteGenerado() {
//        log.info("ControlEnvioCorreos.getPathReporteGenerado()");
//        if (pathReporteGenerado == null || pathReporteGenerado.isEmpty()) {
//            pathReporteGenerado = administarReportes.generarReporte(reporteActual.getNombrereporte(), reporteActual.getTipo());
//        }
        return pathReporteGenerado;
    }

    public void setPathReporteGenerado(String pathReporteGenerado) {
        this.pathReporteGenerado = pathReporteGenerado;
    }

    public List<Empleados> getFiltrarListEmpleados() {
        return filtrarListEmpleados;
    }

    public void setFiltrarListEmpleados(List<Empleados> filtrarListEmpleados) {
        this.filtrarListEmpleados = filtrarListEmpleados;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }
//
//    public List<Empleados> getEmpleadoCorreo() {
//        if (empleadoCorreo == null || empleadoCorreo.isEmpty()) {
//            empleadoCorreo = administrarEnviosCorreos.correoCodigoEmpleado(codigoParametros.getCodigoempleadodesde(), codigoParametros.getCodigoempleadohasta());
//        }
//        return empleadoCorreo;
//    }
//
//    public void setEmpleadoCorreo(List<Empleados> empleadoCorreo) {
//        this.empleadoCorreo = empleadoCorreo;
//    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovCorreoEmpleado");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public List<Empleados> getLovEmpleados() {
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public BigInteger getSecEmpresa() {
        log.info("ControlEnvioCorreos.getSecEmpresa() secEmpresa: " + secEmpresa);
        if (secEmpresa == null) {
            try {
                secEmpresa = codigoParametros.getEmpresa().getSecuencia();
            } catch (NullPointerException npe) {
                try {
                    secEmpresa = administrarEnviosCorreos.empresaAsociada();
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

    public ConfiguracionCorreo getCorreoRemitente() {
        return correoRemitente;
    }

    public void setCorreoRemitente(ConfiguracionCorreo correoRemitente) {
        this.correoRemitente = correoRemitente;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCco() {
        return cco;
    }

    public void setCco(String cco) {
        this.cco = cco;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getRemitente() {
        getSecEmpresa();
        if (remitente.isEmpty() || remitente == null) {
            remitente = administrarEnviosCorreos.consultarRemitente(secEmpresa).getRemitente();
        }
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public boolean isActivoRemitente() {
        return activoRemitente;
    }

    public void setActivoRemitente(boolean activoRemitente) {
        this.activoRemitente = activoRemitente;
    }

    public EnvioCorreos getRegistrofallocorreo() {
        return registrofallocorreo;
    }

    public void setRegistrofallocorreo(EnvioCorreos registrofallocorreo) {
        this.registrofallocorreo = registrofallocorreo;
    }
}
