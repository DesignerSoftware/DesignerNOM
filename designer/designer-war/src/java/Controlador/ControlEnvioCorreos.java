package Controlador;

import Entidades.ConfiguracionCorreo;
import Entidades.Empleados;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import InterfaceAdministrar.AdministrarEnvioCorreosInterface;
import InterfaceAdministrar.AdministrarNReportesNominaInterface;
import InterfaceAdministrar.AdministrarRegistroEnviosInterface;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlEnvioCorreos")
@SessionScoped
public class ControlEnvioCorreos implements Serializable {

    @EJB
    AdministrarEnvioCorreosInterface administrarEnviosCorreos;
    @EJB
    AdministrarRegistroEnviosInterface administrarRegistroEnvio;
    @EJB
    AdministrarNReportesNominaInterface administrarNReporteNomina;

    private List<Empleados> listCorreoCodigos;
    private List<Empleados> filtrarListEmpleados;
    private List<Empleados> lovEmpleados;
    private Empleados empleadoSeleccionado;
    private Empleados empleadoCorreo;
    private ConfiguracionCorreo correoRemitente;
    private Inforeportes reporteActual;
    private ParametrosReportes codigoParametros;
    private BigInteger secEmpresa;
    private String email;
    private String pathReporteGenerado;
    private String paginaAnterior;
    private String nombreReporte;
    private String infoRegistro;
    private boolean aceptar;

    public ControlEnvioCorreos() {
        listCorreoCodigos = null;
        lovEmpleados = null;
        reporteActual = new Inforeportes();
        empleadoSeleccionado = null;
        codigoParametros = null;
        email = "";
        aceptar = true;
    }

    @PostConstruct
    public void iniciarAdministradores() {
        System.out.println(this.getClass().getName() + ".iniciarAdministradores()");
        try {
            FacesContext contexto = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
            administrarEnviosCorreos.obtenerConexion(ses.getId());
            administrarRegistroEnvio.obtenerConexion(ses.getId());
            administrarNReporteNomina.obtenerConexion(ses.getId());
            getSecEmpresa();
            if (!validarConfigSMTP()) {
                System.out.println("Configuración de Servidor SMTP inválida");
                FacesMessage msg = new FacesMessage("Error", "Configuración de Servidor SMTP inválida.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
//                FacesContext.getCurrentInstance().addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", "Configuración de Servidor SMTP inválida."));
            } else {
                System.out.println("La configuración es valida");
                FacesMessage msg = new FacesMessage("Informacion", "La configuración es valida.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
//                FacesContext.getCurrentInstance().addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion: ", "La configuración es valida."));
            }
            System.out.println(this.getClass().getName() + " fin de iniciarAdministradores()");
        } catch (Exception e) {
            System.out.println("Error postconstruct controlEnvioCorreos" + e);
            System.out.println("Causa: " + e.getMessage());
        }
    }

    public void recibirPagina(String pagina, BigInteger secReporte) {
        System.out.println("Controlador.ControlRegistroEnvios.recibirPagina()");
        System.out.println("pagina: " + pagina);
        System.out.println("secReporte: " + secReporte);
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
        System.out.println("NombreReporte: " + nombreReporte);
        System.out.println("reporteActual: " + reporteActual);
    }

    public boolean validarCorreo() {
        System.out.println("Controlador.ControlEnvioCorreos.validarCorreo()");
        if (email != null) {
            String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(PATTERN_EMAIL);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return true;
            } else {
                FacesMessage msg = new FacesMessage("Error", "Correo inválido, por favor verifique.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
        } else {
            return true;
        }
        return false;
    }

    public void validarEnviaCorreo() {
        System.out.println("Controlador.ControlEnvioCorreos.validarEnviaCorreo()");
        if (administrarEnviosCorreos.enviarCorreo(secEmpresa, email,
                reporteActual.getNombre(), "Mensaje enviado automáticamente, por favor no responda a este correo.",
                pathReporteGenerado)) {
            FacesMessage msg = new FacesMessage("Error", "El reporte ha sido enviado exitosamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        } else {
            FacesMessage msg = new FacesMessage("Error", "No fue posible enviar el correo");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
//        }
    }

    public boolean validarConfigSMTP() {
        System.out.println("Controlador.ControlEnvioCorreos.validarConfigSMTP()");
        return administrarEnviosCorreos.comprobarConfigCorreo(secEmpresa);
    }

    //METODOS DIALOGO
    public void dialogos() {
        RequestContext.getCurrentInstance().update("formDialogos:correoEmpleadosDialogo");
        RequestContext.getCurrentInstance().execute("PF('correoEmpleadosDialogo').show()");
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistro");
    }

    public void activarAceptar() {
        System.out.println(this.getClass().getName() + ".activarAceptar()");
        aceptar = false;
    }

    public void actualizarCorreoEmpl() {
        System.out.println("Controlador.ControlEnvioCorreos.actualizarCorreoEmpl()");
//        parametroDeReporte.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
//        parametroModificacion = parametroDeReporte;
//        cambiosReporte = false;
        setEmail(empleadoSeleccionado.getPersona().getEmail());
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovCorreoEmpleado:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCorreoEmpleado').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('correoEmpleadosDialogo').hide()");

        RequestContext.getCurrentInstance().update("formDialogos:aceptar");
        RequestContext.getCurrentInstance().update("form:para");
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
    }

    public void cancelarCambioCorreoEmpl() {
        System.out.println("Controlador.ControlEnvioCorreos.cancelarCambioCorreoEmpl()");
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovCorreoEmpleado:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCorreoEmpleado').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('correoEmpleadosDialogo').hide()");
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

    public String getEmail() {
        System.out.println("Controlador.ControlEnvioCorreos.getEmail(). email= " + email);
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
        System.out.println("Controlador.ControlEnvioCorreos.getCodigoParametros()");
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

    public Empleados getEmpleadoCorreo() {
        if (empleadoCorreo == null) {
            empleadoCorreo = administrarNReporteNomina.listEmpleados().get(0);
        }
        return empleadoCorreo;
    }

    public void setEmpleadoCorreo(Empleados empleadoCorreo) {
        this.empleadoCorreo = empleadoCorreo;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovCorreoEmpleado");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public List<Empleados> getLovEmpleados() {
        if (lovEmpleados == null || lovEmpleados.isEmpty()) {
            lovEmpleados = administrarNReporteNomina.listEmpleados();
        }
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public BigInteger getSecEmpresa() {
        System.out.println("Controlador.ControlEnvioCorreos.getSecEmpresa() secEmpresa: " + secEmpresa);
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

}
