/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.FormulasConceptos;
import Entidades.Novedades;
import Entidades.Periodicidades;
import Entidades.PruebaEmpleados;
import Entidades.Terceros;
import Entidades.Usuarios;
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaConceptoInterface;
import InterfaceAdministrar.AdministrarNovedadesEmpleadosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
//import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Viktor
 */
@ManagedBean
@SessionScoped
public class ControlNovedadesEmpleados implements Serializable {

    @EJB
    AdministrarNovedadesEmpleadosInterface administrarNovedadesEmpleados;
    @EJB
    AdministrarFormulaConceptoInterface administrarFormulaConcepto;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //SECUENCIA DE LA PERSONA
//    private BigInteger secuenciaEmpleado;
    //LISTA NOVEDADES
    private List<Novedades> listaNovedades;
    private List<Novedades> filtradosListaNovedades;
    private Novedades novedadSeleccionada;
    //LISTA QUE NO ES LISTA
    private List<PruebaEmpleados> listaEmpleadosNovedad;
    private List<PruebaEmpleados> filtrarListaEmpleadosNovedad;
    private PruebaEmpleados empleadoSeleccionado; //Seleccion Mostrar
    //LOV EMPLEADOS
    private List<PruebaEmpleados> lovEmpleados;
    private List<PruebaEmpleados> filtrarLovEmpleados;
    private PruebaEmpleados empleadoSeleccionadoLov;
    //editar celda
    private Novedades editarNovedades;
    private int cualCelda, tipoLista;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //RASTROS
//    private BigInteger secRegistro;
    private boolean guardado;
    //Crear Novedades
    private List<Novedades> listaNovedadesCrear;
    private Novedades nuevaNovedad;
    private int k;
    private BigInteger l;
    //Modificar Novedades
    private List<Novedades> listaNovedadesModificar;
    //Borrar Novedades
    private List<Novedades> listaNovedadesBorrar;
    //Autocompletar
    private String CodigoConcepto;
    private String NitTercero, Formula, DescripcionConcepto, DescripcionPeriodicidad, NombreTercero;
    private Date FechaFinal;
    private String CodigoPeriodicidad;
    private BigDecimal Saldo;
    private Integer HoraDia, MinutoHora;
    //L.O.V CONCEPTOS
    private List<Conceptos> listaConceptos;
    private List<Conceptos> filtradoslistaConceptos;
    private Conceptos seleccionConceptos;
    BigInteger secconcepto;
    //L.O.V Periodicidades
    private List<Periodicidades> listaPeriodicidades;
    private List<Periodicidades> filtradoslistaPeriodicidades;
    private Periodicidades seleccionPeriodicidades;
    //L.O.V TERCEROS
    private List<Terceros> listaTerceros;
    private List<Terceros> filtradoslistaTerceros;
    private Terceros seleccionTerceros;
    //L.O.V FORMULAS
    private List<Formulas> listaFormulas;
    private List<Formulas> filtradoslistaFormulas;
    private Formulas seleccionFormulas;
    //Columnas Tabla NOVEDADES
    private Column nEConceptoCodigo, nEConceptoDescripcion, nEFechasInicial, nEFechasFinal,
            nEValor, nESaldo, nEPeriodicidadCodigo, nEDescripcionPeriodicidad, nETercerosNit,
            nETercerosNombre, nEFormulas, nEHorasDias, nEMinutosHoras, nETipo;
    //Duplicar
    private Novedades duplicarNovedad;
    //USUARIO
    private String alias;
    private Usuarios usuarioBD;
    //VALIDAR SI EL QUE SE VA A BORRAR ESTÁ EN SOLUCIONES FORMULAS
    private int resultado;
    private boolean todas;
    private boolean actuales;
    //
    private boolean activoBtnAcumulado;
    //
    private Novedades actualNovedadTabla;
    private String altoTabla;
    BigDecimal valor = new BigDecimal(Integer.toString(0));
    private String infoRegistro, infoRegistroConceptos, infoRegistroPeriodicidad, infoRegistroFormulas, infoRegistroTerceros, infoRegistrosEmpleadosNovedades, infoRegistroEmpleadosLOV;
    //Controlar el cargue de muchos empleados
    private boolean cargarTodos;
    private int cantidadEmpleadosNov;
    private boolean activarLOV;

    public ControlNovedadesEmpleados() {
        actualNovedadTabla = new Novedades();
        activoBtnAcumulado = true;
        permitirIndex = true;
        listaNovedades = null;
        lovEmpleados = null;
        listaFormulas = null;
        listaConceptos = null;
        todas = false;
        actuales = true;
        listaPeriodicidades = null;
        listaEmpleadosNovedad = null;
        aceptar = true;
        empleadoSeleccionado = null;
        novedadSeleccionada = null;
        guardado = true;
        tipoLista = 0;
        listaNovedadesBorrar = new ArrayList<Novedades>();
        listaNovedadesCrear = new ArrayList<Novedades>();
        listaNovedadesModificar = new ArrayList<Novedades>();
        //Crear VC
        nuevaNovedad = new Novedades();
        nuevaNovedad.setPeriodicidad(new Periodicidades());
        nuevaNovedad.getPeriodicidad().setNombre(" ");
        nuevaNovedad.getPeriodicidad().setCodigoStr(" ");
        nuevaNovedad.setTercero(new Terceros());
        nuevaNovedad.getTercero().setNombre(" ");
        nuevaNovedad.setConcepto(new Conceptos());
        nuevaNovedad.getConcepto().setDescripcion(" ");
        nuevaNovedad.getConcepto().setCodigoSTR("0");
        nuevaNovedad.setTipo("FIJA");
        nuevaNovedad.setUsuarioreporta(new Usuarios());
        nuevaNovedad.setTerminal(" ");
        nuevaNovedad.setFechareporte(new Date());
        altoTabla = "155";
        nuevaNovedad.setValortotal(valor);
        cargarTodos = false;
        cantidadEmpleadosNov = 0;
        CodigoConcepto = "0";
        activarLOV = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNovedadesEmpleados.obtenerConexion(ses.getId());
            administrarFormulaConcepto.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            getListaEmpleadosNovedad();
            if (listaEmpleadosNovedad != null) {
                if (!listaEmpleadosNovedad.isEmpty()) {
                    empleadoSeleccionado = listaEmpleadosNovedad.get(0);
                }
                if (cargarTodos) {
                    modificarInfoRegistroEmpleados(cantidadEmpleadosNov);
                } else {
                    modificarInfoRegistroEmpleados(listaEmpleadosNovedad.size());
                }
            }
            listaNovedades = null;
            getListaNovedades();
            contarRegistros();
            if (listaNovedades != null) {
                RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            }
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        anularBotonLOV();
        cerrarFiltrado();
        listaNovedadesBorrar.clear();
        listaNovedadesCrear.clear();
        listaNovedadesModificar.clear();
        empleadoSeleccionado = null;
        novedadSeleccionada = null;
//        k = 0;
        listaNovedades = null;
        guardado = true;
        permitirIndex = true;
        resultado = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnAcumulado = true;
        context.update("form:ACUMULADOS");
        context.update("form:datosNovedadesEmpleado");
    }

    public void salir() {
        anularBotonLOV();
        cerrarFiltrado();
        listaNovedadesBorrar.clear();
        listaNovedadesCrear.clear();
        listaNovedadesModificar.clear();
//        empleadoSeleccionado = null;
        novedadSeleccionada = null;
        listaEmpleadosNovedad = null;
        lovEmpleados = null;
        listaNovedades = null;
        resultado = 0;
        guardado = true;
        permitirIndex = true;
        activoBtnAcumulado = true;
        RequestContext.getCurrentInstance().update("form:ACUMULADOS");
    }

    public void cerrarFiltrado() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            nEConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEConceptoCodigo");
            nEConceptoCodigo.setFilterStyle("display: none; visibility: hidden;");
            nEConceptoDescripcion = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEConceptoDescripcion");
            nEConceptoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            nEFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechasInicial");
            nEFechasInicial.setFilterStyle("display: none; visibility: hidden;");
            nEFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechasFinal");
            nEFechasFinal.setFilterStyle("display: none; visibility: hidden;");
            nEValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEValor");
            nEValor.setFilterStyle("display: none; visibility: hidden;");
            nESaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nESaldo");
            nESaldo.setFilterStyle("display: none; visibility: hidden;");
            nEPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEPeriodicidadCodigo");
            nEPeriodicidadCodigo.setFilterStyle("display: none; visibility: hidden;");
            nEDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDescripcionPeriodicidad");
            nEDescripcionPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
            nETercerosNit = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETercerosNit");
            nETercerosNit.setFilterStyle("display: none; visibility: hidden;");
            nETercerosNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETercerosNombre");
            nETercerosNombre.setFilterStyle("display: none; visibility: hidden;");
            nEFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFormulas");
            nEFormulas.setFilterStyle("display: none; visibility: hidden;");
            nEHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEHorasDias");
            nEHorasDias.setFilterStyle("display: none; visibility: hidden;");
            nEMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEMinutosHoras");
            nEMinutosHoras.setFilterStyle("display: none; visibility: hidden;");
            nETipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETipo");
            nETipo.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "155";
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            bandera = 0;
            filtradosListaNovedades = null;
            tipoLista = 0;
        }
    }

    //Ubicacion Celda Arriba 
    public void cambiarEmpleado() {
        //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
        //{
        if (listaNovedadesCrear.isEmpty() && listaNovedadesBorrar.isEmpty() && listaNovedadesModificar.isEmpty()) {
            //secuenciaEmpleado = empleadoSeleccionado.getId();
            listaNovedades = null;
            getListaNovedades();
            contarRegistros();
            // novedadSeleccionada = null;
            RequestContext context = RequestContext.getCurrentInstance();
            activoBtnAcumulado = true;
            context.update("form:ACUMULADOS");
            context.update("form:datosNovedadesEmpleado");
            //}
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:cambiar");
            context.execute("cambiar.show()");
        }
    }

    public void limpiarListas() {
        listaNovedadesCrear.clear();
        listaNovedadesBorrar.clear();
        listaNovedadesModificar.clear();
//        secuenciaEmpleado = empleadoSeleccionado.getId();
        listaNovedades = null;
        guardado = true;
        getListaNovedades();
        System.out.println("listaNovedades Valor" + listaNovedades.get(0).getValortotal());
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosNovedadesEmpleado");
        context.update("form:ACEPTAR");
    }

    public void seleccionarTipoNuevaNovedad(String tipo, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (tipo.equals("FIJA")) {
                nuevaNovedad.setTipo("FIJA");
            } else if (tipo.equals("OCASIONAL")) {
                nuevaNovedad.setTipo("OCASIONAL");
            } else if (tipo.equals("PAGO POR FUERA")) {
                nuevaNovedad.setTipo("PAGO POR FUERA");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
        } else {
            if (tipo.equals("FIJA")) {
                duplicarNovedad.setTipo("FIJA");
            } else if (tipo.equals("OCASIONAL")) {
                duplicarNovedad.setTipo("OCASIONAL");
            } else if (tipo.equals("PAGO POR FUERA")) {
                duplicarNovedad.setTipo("PAGO POR FUERA");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");
        }
    }

    public void seleccionarTipo(String estadoTipo, Novedades novedad, int celda) {
        novedadSeleccionada = novedad;
        if (estadoTipo.equals("FIJA")) {
            novedadSeleccionada.setTipo("FIJA");
        } else if (estadoTipo.equals("OCASIONAL")) {
            novedadSeleccionada.setTipo("OCASIONAL");
        } else if (estadoTipo.equals("PAGO POR FUERA")) {
            novedadSeleccionada.setTipo("PAGO POR FUERA");
        }

        if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
                listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                listaNovedadesModificar.add(novedadSeleccionada);
            }
        }

        if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnAcumulado = true;
        context.update("form:ACUMULADOS");
        RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
    }

    //GUARDAR
    public void guardarCambiosNovedades() {
//        Empleados emp = new Empleados();
        if (guardado == false) {
            System.out.println("Realizando Operaciones Novedades");

            getResultado();
            System.out.println("Resultado: " + resultado);
            if (resultado > 0) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formularioDialogos:solucionesFormulas");
                context.execute("solucionesFormulas.show()");
                listaNovedadesBorrar.clear();
            }

            if (!listaNovedadesBorrar.isEmpty()) {
                for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaNovedadesBorrar.size());

                    if (listaNovedadesBorrar.get(i).getPeriodicidad().getSecuencia() == null) {
                        listaNovedadesBorrar.get(i).setPeriodicidad(null);
                    }
                    if (listaNovedadesBorrar.get(i).getTercero().getSecuencia() == null) {
                        listaNovedadesBorrar.get(i).setTercero(null);
                    }
                    if (listaNovedadesBorrar.get(i).getSaldo() == null) {
                        listaNovedadesBorrar.get(i).setSaldo(null);
                    }
                    if (listaNovedadesBorrar.get(i).getUnidadesparteentera() == null) {
                        listaNovedadesBorrar.get(i).setUnidadesparteentera(null);
                    }
                    if (listaNovedadesBorrar.get(i).getUnidadespartefraccion() == null) {
                        listaNovedadesBorrar.get(i).setUnidadespartefraccion(null);
                    }
                    administrarNovedadesEmpleados.borrarNovedades(listaNovedadesBorrar.get(i));
                }
                System.out.println("Entra");
                listaNovedadesBorrar.clear();
            }

            if (!listaNovedadesCrear.isEmpty()) {
                for (int i = 0; i < listaNovedadesCrear.size(); i++) {
                    System.out.println("Creando...");

                    if (listaNovedadesCrear.get(i).getTercero().getSecuencia() == null) {
                        listaNovedadesCrear.get(i).setTercero(null);
                    }
                    if (listaNovedadesCrear.get(i).getPeriodicidad().getSecuencia() == null) {
                        listaNovedadesCrear.get(i).setPeriodicidad(null);
                    }

                    if (listaNovedadesCrear.get(i).getSaldo() == null) {
                        listaNovedadesCrear.get(i).setSaldo(null);
                    }
                    if (listaNovedadesCrear.get(i).getUnidadesparteentera() == null) {
                        listaNovedadesCrear.get(i).setUnidadesparteentera(null);
                    }
                    if (listaNovedadesCrear.get(i).getUnidadespartefraccion() == null) {
                        listaNovedadesCrear.get(i).setUnidadespartefraccion(null);
                    }
                    if (listaNovedadesCrear.get(i).getValortotal() == null) {
                        listaNovedadesCrear.get(i).setValortotal(new BigDecimal(0));
                    }
                    System.out.println(listaNovedadesCrear.get(i).getTipo());
                    administrarNovedadesEmpleados.crearNovedades(listaNovedadesCrear.get(i));
                }
                System.out.println("LimpiaLista");
                listaNovedadesCrear.clear();
            }
            if (!listaNovedadesModificar.isEmpty()) {
                administrarNovedadesEmpleados.modificarNovedades(listaNovedadesModificar);
                listaNovedadesModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaNovedades = null;
            RequestContext context = RequestContext.getCurrentInstance();
            activoBtnAcumulado = true;
            context.update("form:ACUMULADOS");
            context.update("form:datosNovedadesEmpleado");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            //  k = 0;
        }
        System.out.println("Tamaño lista: " + listaNovedadesCrear.size());
        System.out.println("Valor k: " + k);
    }

    //CREAR NOVEDADES
    public void agregarNuevaNovedadEmpleado() throws UnknownHostException {
        int pasa = 0;
        int pasa2 = 0;
        Empleados emp = new Empleados();
        Empleados emp2 = new Empleados();
        RequestContext context = RequestContext.getCurrentInstance();

        System.out.println("nuevaNovedad Fechainicial : " + nuevaNovedad.getFechainicial());
        System.out.println("Concepto : " + nuevaNovedad.getConcepto().getDescripcion());
        System.out.println("Formula : " + nuevaNovedad.getFormula());
        System.out.println("Periodicidad : " + nuevaNovedad.getPeriodicidad().getNombre());
        System.out.println("getTipo() : " + nuevaNovedad.getTipo());

        if (nuevaNovedad.getFechainicial() == null || nuevaNovedad.getConcepto().getCodigoSTR() == null
                || nuevaNovedad.getFormula().getNombrelargo() == null || nuevaNovedad.getTipo() == null
                || (nuevaNovedad.getValortotal() == null && nuevaNovedad.getUnidadesparteentera() == null && nuevaNovedad.getUnidadespartefraccion() == null)) {
            pasa++;
        }
        System.out.println("agregarNuevaNovedadEmpleado() pasa : " + pasa);

        if (pasa == 0) {

            emp2 = administrarNovedadesEmpleados.elEmpleado(empleadoSeleccionado.getId());

            if (nuevaNovedad.getFechainicial() != null) {
                if (nuevaNovedad.getFechainicial().compareTo(emp2.getFechacreacion()) < 0) {
                    context.update("formularioDialogos:inconsistencia");
                    context.execute("inconsistencia.show()");
                    pasa2++;
                }
            } else if (nuevaNovedad.getFechafinal() != null) {
                if (nuevaNovedad.getFechainicial().compareTo(nuevaNovedad.getFechafinal()) > 0) {
                    context.update("formularioDialogos:fechas");
                    context.execute("fechas.show()");
                    pasa2++;
                }
            }

            if (pasa2 == 0) {
                System.out.println("Paso todas las validaciones");
                cerrarFiltrado();
                //AGREGAR REGISTRO A LA LISTA NOVEDADES .
                k++;
                l = BigInteger.valueOf(k);
                nuevaNovedad.setSecuencia(l);

//            for (int i = 0; i < lovEmpleados.size(); i++) {
//                if (empleadoSeleccionado.getId().compareTo(lovEmpleados.get(i).getId()) == 0) {
//                    emp = administrarNovedadesEmpleados.elEmpleado(lovEmpleados.get(i).getId());
                emp = administrarNovedadesEmpleados.elEmpleado(empleadoSeleccionado.getId());
//                }
//            }

                // OBTENER EL TERMINAL 
                HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
                String equipo = null;
                java.net.InetAddress localMachine = null;
                if (request.getRemoteAddr().startsWith("127.0.0.1")) {
                    localMachine = java.net.InetAddress.getLocalHost();
                    equipo = localMachine.getHostAddress();
                } else {
                    equipo = request.getRemoteAddr();
                }
                localMachine = java.net.InetAddress.getByName(equipo);

                getAlias();
                System.out.println("Alias: " + alias);
                getUsuarioBD();
                System.out.println("UsuarioBD: " + usuarioBD);
                nuevaNovedad.setTerminal(localMachine.getHostName());
                nuevaNovedad.setUsuarioreporta(usuarioBD);
                nuevaNovedad.setEmpleado(emp); //Envia empleado
                System.out.println("Empleado enviado: " + emp.getPersona().getNombreCompleto());
                listaNovedadesCrear.add(nuevaNovedad);
                listaNovedades.add(nuevaNovedad);
                modificarInfoRegistro(listaNovedades.size());
                novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(nuevaNovedad));
                nuevaNovedad = new Novedades();
                nuevaNovedad.setPeriodicidad(new Periodicidades());
                nuevaNovedad.getPeriodicidad().setNombre(" ");
                nuevaNovedad.getPeriodicidad().setCodigoStr(" ");
                nuevaNovedad.setTercero(new Terceros());
                nuevaNovedad.getTercero().setNombre(" ");
                nuevaNovedad.setConcepto(new Conceptos());
                nuevaNovedad.getConcepto().setDescripcion(" ");
                nuevaNovedad.getConcepto().setCodigoSTR("0");
                nuevaNovedad.setTipo("FIJA");
                nuevaNovedad.setUsuarioreporta(new Usuarios());
                nuevaNovedad.setTerminal(" ");
                nuevaNovedad.setFechareporte(new Date());

                System.out.println("nuevaNovedad : " + nuevaNovedad.getFechareporte());
                activoBtnAcumulado = true;
                context.update("form:ACUMULADOS");
                context.update("form:datosNovedadesEmpleado");
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                context.execute("NuevaNovedadEmpleado.hide()");
            }
        } else {
            context.update("formularioDialogos:validacionNuevaNovedadEmpleado");
            context.execute("validacionNuevaNovedadEmpleado.show()");
        }
    }

    public void confirmarDuplicar() throws UnknownHostException {

        int pasa2 = 0;
        int pasa = 0;
//        Empleados emp = new Empleados();
        Empleados emp2 = new Empleados();
        RequestContext context = RequestContext.getCurrentInstance();

        System.out.println("duplicarNovedad Fechainicial : " + duplicarNovedad.getFechainicial());
        System.out.println("Concepto : " + duplicarNovedad.getConcepto().getDescripcion());
        System.out.println("Formula : " + duplicarNovedad.getFormula());
        System.out.println("Periodicidad : " + duplicarNovedad.getPeriodicidad().getNombre());
        System.out.println("getTipo() : " + duplicarNovedad.getTipo());

        if (duplicarNovedad.getFechainicial() == null || duplicarNovedad.getEmpleado() == null
                || duplicarNovedad.getFormula().getNombrelargo() == null || duplicarNovedad.getTipo() == null
                || (duplicarNovedad.getValortotal() == null && duplicarNovedad.getUnidadesparteentera() == null && duplicarNovedad.getUnidadespartefraccion() == null)) {
            pasa++;
        }

        System.out.println("confirmarDuplicar() pasa : " + pasa);

        if (pasa == 0) {
            emp2 = administrarNovedadesEmpleados.elEmpleado(empleadoSeleccionado.getId());

            if (duplicarNovedad.getFechainicial() != null) {
                if (duplicarNovedad.getFechainicial().compareTo(emp2.getFechacreacion()) < 0) {
                    context.update("formularioDialogos:inconsistencia");
                    context.execute("inconsistencia.show()");
                    pasa2++;
                }
            } else if (duplicarNovedad.getFechafinal() != null) {
                if (duplicarNovedad.getFechainicial().compareTo(duplicarNovedad.getFechafinal()) > 0) {
                    context.update("formularioDialogos:fechas");
                    context.execute("fechas.show()");
                    pasa2++;
                }
            }

            if (pasa2 == 0) {
                listaNovedades.add(duplicarNovedad);
                listaNovedadesCrear.add(duplicarNovedad);
                modificarInfoRegistro(listaNovedades.size());
                novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(duplicarNovedad));

                context.update("form:datosNovedadesEmpleado");
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                cerrarFiltrado();

                // OBTENER EL TERMINAL 
                HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
                String equipo = null;
                java.net.InetAddress localMachine = null;
                if (request.getRemoteAddr().startsWith("127.0.0.1")) {
                    localMachine = java.net.InetAddress.getLocalHost();
                    equipo = localMachine.getHostAddress();
                } else {
                    equipo = request.getRemoteAddr();
                }
                localMachine = java.net.InetAddress.getByName(equipo);

                getAlias();
                System.out.println("Alias: " + alias);
                getUsuarioBD();
                System.out.println("UsuarioBD: " + usuarioBD);
                duplicarNovedad.setTerminal(localMachine.getHostName());

                duplicarNovedad = new Novedades();
                activoBtnAcumulado = true;
                context.update("form:ACUMULADOS");
                context.update("formularioDialogos:DuplicarRegistroNovedad");
                context.execute("DuplicarRegistroNovedad.hide()");
            }
        } else {
            context.update("formularioDialogos:validacionNuevaNovedadEmpleado");
            context.execute("validacionNuevaNovedadEmpleado.show()");
        }
    }

    public void asignarIndex(int cualLista, int tipoAct) {

        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnAcumulado = true;
        context.update("form:ACUMULADOS");
        tipoActualizacion = tipoAct;

        if (cualLista == 0) {
            if (cargarTodos) {
                listaEmpleadosNovedad = null;
                cargarTodosEmpleados();
                cargarTodos = false;
            }
            modificarInfoRegistroLovEmpleados(lovEmpleados.size());
            context.update("formularioDialogos:empleadosDialogo");
            context.execute("empleadosDialogo.show()");
        } else if (cualLista == 1) {
            cargarLOVConceptos();
            modificarInfoRegistroConceptos(listaConceptos.size());
            context.update("formularioDialogos:conceptosDialogo");
            context.execute("conceptosDialogo.show()");
        } else if (cualLista == 2) {
            cargarLOVFormulas();
            modificarInfoRegistroFormulas(listaFormulas.size());
            context.update("formularioDialogos:formulasDialogo");
            context.execute("formulasDialogo.show()");
        } else if (cualLista == 3) {
            cargarLOVPeriodicidades();
            modificarInfoRegistroPeriodicidad(listaPeriodicidades.size());
            context.update("formularioDialogos:periodicidadesDialogo");
            context.execute("periodicidadesDialogo.show()");
        } else if (cualLista == 4) {
            cargarLOVTerceros();
            modificarInfoRegistroTerceros(listaTerceros.size());
            context.update("formularioDialogos:tercerosDialogo");
            context.execute("tercerosDialogo.show()");
        }
        System.out.println("cual celda :" + cualLista);
        System.out.println("tipo Actualización :" + tipoActualizacion);
    }

    public void asignarIndex(Novedades novedad, int cualLista, int tipoAct) {

        novedadSeleccionada = novedad;
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnAcumulado = true;
        context.update("form:ACUMULADOS");
        tipoActualizacion = tipoAct;

        if (cualLista == 1) {
            activarBotonLOV();
            cargarLOVConceptos();
            modificarInfoRegistroConceptos(listaConceptos.size());
            context.update("formularioDialogos:conceptosDialogo");
            context.execute("conceptosDialogo.show()");
        } else if (cualLista == 2) {
            activarBotonLOV();
            cargarLOVFormulas();
            modificarInfoRegistroFormulas(listaFormulas.size());
            context.update("formularioDialogos:formulasDialogo");
            context.execute("formulasDialogo.show()");
        } else if (cualLista == 3) {
            activarBotonLOV();
            cargarLOVPeriodicidades();
            modificarInfoRegistroPeriodicidad(listaPeriodicidades.size());
            context.update("formularioDialogos:periodicidadesDialogo");
            context.execute("periodicidadesDialogo.show()");
        } else if (cualLista == 4) {
            activarBotonLOV();
            cargarLOVTerceros();
            modificarInfoRegistroTerceros(listaTerceros.size());
            context.update("formularioDialogos:tercerosDialogo");
            context.execute("tercerosDialogo.show()");
        } else {
            anularBotonLOV();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    //AUTOCOMPLETAR
    public void modificarNovedades(Novedades novedad, String confirmarCambio, String valor) {

        novedadSeleccionada = novedad;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnAcumulado = true;
        context.update("form:ACUMULADOS");
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            context.update("form:datosNovedadesEmpleado");
        } else if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            cargarLOVFormulas();
            novedadSeleccionada.getFormula().setNombresFormula(Formula);
            for (int i = 0; i < listaFormulas.size(); i++) {
                if (listaFormulas.get(i).getNombresFormula().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setFormula(listaFormulas.get(indiceUnicoElemento));
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:formulasDialogo");
                context.execute("formulasDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
            cargarLOVTerceros();
            novedadSeleccionada.getTercero().setNitalternativo(NitTercero);
            for (int i = 0; i < listaTerceros.size(); i++) {
                if (listaTerceros.get(i).getNitalternativo().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setTercero(listaTerceros.get(indiceUnicoElemento));

            } else {
                permitirIndex = false;
                context.update("formularioDialogos:tercerosDialogo");
                context.execute("tercerosDialogo.show()");
                tipoActualizacion = 0;
            }
            
        } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            cargarLOVConceptos();
            
        } else if (confirmarCambio.equalsIgnoreCase("CODIGOPERIODICIDAD")) {
            cargarLOVConceptos();
            novedadSeleccionada.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
            for (int i = 0; i < listaPeriodicidades.size(); i++) {
                if ((listaPeriodicidades.get(i).getCodigoStr()).startsWith(valor.toString().toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setPeriodicidad(listaPeriodicidades.get(indiceUnicoElemento));
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:periodicidadesDialogo");
                context.execute("periodicidadesDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
        context.update("form:datosNovedadesEmpleado");
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnAcumulado = true;
        context.update("form:ACUMULADOS");
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            nEConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEConceptoCodigo");
            nEConceptoCodigo.setFilterStyle("width: 85%");
            nEConceptoDescripcion = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEConceptoDescripcion");
            nEConceptoDescripcion.setFilterStyle("");
            nEFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechasInicial");
            nEFechasInicial.setFilterStyle("width: 85%");
            nEFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechasFinal");
            nEFechasFinal.setFilterStyle("width: 85%");
            nEValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEValor");
            nEValor.setFilterStyle("width: 85%");
            nESaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nESaldo");
            nESaldo.setFilterStyle("width: 85%");
            nEPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEPeriodicidadCodigo");
            nEPeriodicidadCodigo.setFilterStyle("width: 85%");
            nEDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDescripcionPeriodicidad");
            nEDescripcionPeriodicidad.setFilterStyle("width: 85%");
            nETercerosNit = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETercerosNit");
            nETercerosNit.setFilterStyle("width: 85%");
            nETercerosNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETercerosNombre");
            nETercerosNombre.setFilterStyle("width: 85%");
            nEFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFormulas");
            nEFormulas.setFilterStyle("width: 85%");
            nEHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEHorasDias");
            nEHorasDias.setFilterStyle("width: 85%");
            nEMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEMinutosHoras");
            nEMinutosHoras.setFilterStyle("width: 85%");
            nETipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETipo");
            nETipo.setFilterStyle("width: 85%");
            altoTabla = "131";
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            bandera = 1;
            tipoLista = 1;
        } else {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            cerrarFiltrado();
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else if (novedadSeleccionada != null) {
            activoBtnAcumulado = true;
            context.update("form:ACUMULADOS");
            if (cualCelda == 0) {
                cargarLOVConceptos();
                context.update("formularioDialogos:conceptosDialogo");
                context.execute("conceptosDialogo.show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 6) {
                cargarLOVPeriodicidades();
                context.update("formularioDialogos:periodicidadesDialogo");
                context.execute("periodicidadesDialogo.show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 8) {
                cargarLOVTerceros();
                context.update("formularioDialogos:tercerosDialogo");
                context.execute("tercerosDialogo.show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 10) {
                cargarLOVFormulas();
                context.update("formularioDialogos:formulasDialogo");
                context.execute("formulasDialogo.show()");
            }
        }
    }
    //Ubicacion Celda Indice Abajo. //Van los que no son NOT NULL.

    public void cambiarIndice(Novedades novedad, int celda) {
        novedadSeleccionada = novedad;
        if (permitirIndex == true) {
            RequestContext context = RequestContext.getCurrentInstance();
            activoBtnAcumulado = false;
            context.update("form:ACUMULADOS");
            cualCelda = celda;
            actualNovedadTabla = novedadSeleccionada;
            if (cualCelda == 0) {
                activarBotonLOV();
                CodigoConcepto = novedadSeleccionada.getConcepto().getCodigoSTR();
            } else if (cualCelda == 1) {
                activarBotonLOV();
                DescripcionConcepto = novedadSeleccionada.getConcepto().getDescripcion();
            } else if (cualCelda == 3) {
                anularBotonLOV();
                FechaFinal = novedadSeleccionada.getFechafinal();
            } else if (cualCelda == 5) {
                anularBotonLOV();
                Saldo = novedadSeleccionada.getSaldo();
            } else if (cualCelda == 6) {
                activarBotonLOV();
                CodigoPeriodicidad = novedadSeleccionada.getPeriodicidad().getCodigoStr();
            } else if (cualCelda == 7) {
                activarBotonLOV();
                DescripcionPeriodicidad = novedadSeleccionada.getPeriodicidad().getNombre();
            } else if (cualCelda == 8) {
                activarBotonLOV();
                NitTercero = novedadSeleccionada.getTercero().getNitalternativo();
            } else if (cualCelda == 9) {
                activarBotonLOV();
                NombreTercero = novedadSeleccionada.getTercero().getNombre();
            } else if (cualCelda == 10) {
                anularBotonLOV();
                HoraDia = novedadSeleccionada.getUnidadesparteentera();
            } else if (cualCelda == 11) {
                anularBotonLOV();
                MinutoHora = novedadSeleccionada.getUnidadespartefraccion();
            } else {
                anularBotonLOV();
            }
        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else if (novedadSeleccionada != null) {
            editarNovedades = novedadSeleccionada;

            activoBtnAcumulado = true;
            context.update("form:ACUMULADOS");
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                context.update("formularioDialogos:editarConceptosCodigos");
                context.execute("editarConceptosCodigos.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editarConceptosDescripciones");
                context.execute("editarConceptosDescripciones.show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                context.update("formularioDialogos:editFechaInicial");
                context.execute("editFechaInicial.show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                context.update("formularioDialogos:editFechasFinales");
                context.execute("editFechasIniciales.show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                context.update("formularioDialogos:editarValores");
                context.execute("editarSaldos.show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                context.update("formularioDialogos:editarSaldos");
                context.execute("editarSaldos.show()");
            } else if (cualCelda == 6) {
                context.update("formularioDialogos:editarPeriodicidadesCodigos");
                context.execute("editarPeriodicidadesCodigos.show()");
            } else if (cualCelda == 7) {
                context.update("formularioDialogos:editarPeriodicidadesDescripciones");
                context.execute("editarPeriodicidadesDescripciones.show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                context.update("formularioDialogos:editarTercerosNit");
                context.execute("editarTercerosNit.show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                context.update("formularioDialogos:editarTercerosNombres");
                context.execute("editarTercerosNombres.show()");
                cualCelda = -1;
            } else if (cualCelda == 10) {
                context.update("formularioDialogos:editarFormulas");
                context.execute("editarFormulas.show()");
                cualCelda = -1;
            } else if (cualCelda == 11) {
                context.update("formularioDialogos:editarHorasDias");
                context.execute("editarHorasDias.show()");
                cualCelda = -1;
            } else if (cualCelda == 12) {
                context.update("formularioDialogos:editarMinutosHoras");
                context.execute("editarMinutosHoras.show()");
                cualCelda = -1;
            } else if (cualCelda == 13) {
                context.update("formularioDialogos:editarTipos");
                context.execute("editarTipos.show()");
                cualCelda = -1;
            }
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("CONCEPTO")) {
            if (tipoNuevo == 1) {
                CodigoConcepto = nuevaNovedad.getConcepto().getCodigoSTR();
            } else if (tipoNuevo == 2) {
                CodigoConcepto = duplicarNovedad.getConcepto().getCodigoSTR();
            }
            System.out.println("tipoNuevo: " + tipoNuevo);
            System.out.println("campo:" + Campo);
        } else if (Campo.equals("CODIGO")) {
            if (tipoNuevo == 1) {
                CodigoPeriodicidad = nuevaNovedad.getPeriodicidad().getCodigoStr();
            } else if (tipoNuevo == 2) {
                CodigoPeriodicidad = duplicarNovedad.getPeriodicidad().getCodigoStr();
            }
        } else if (Campo.equals("NIT")) {
            if (tipoNuevo == 1) {
                NitTercero = nuevaNovedad.getTercero().getNitalternativo();
            } else if (tipoNuevo == 2) {
                NitTercero = duplicarNovedad.getTercero().getNitalternativo();
            }
        } else if (Campo.equals("FORMULA")) {
            if (tipoNuevo == 1) {
                Formula = nuevaNovedad.getFormula().getNombrelargo();
            } else if (tipoNuevo == 2) {
                Formula = duplicarNovedad.getFormula().getNombrelargo();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valor, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            cargarLOVFormulas();
            if (tipoNuevo == 1) {
                nuevaNovedad.getFormula().setNombrelargo(Formula);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getFormula().setNombrelargo(Formula);
            }
            for (int i = 0; i < listaFormulas.size(); i++) {
                if (listaFormulas.get(i).getNombrelargo().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setFormula(listaFormulas.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaFormula");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setFormula(listaFormulas.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarFormula");
                }
            } else {
                context.update("form:formulasDialogo");
                context.execute("formulasDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaFormula");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarFormula");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
            cargarLOVTerceros();
            if (tipoNuevo == 1) {
                nuevaNovedad.getTercero().setNitalternativo(NitTercero);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getTercero().setNitalternativo(NitTercero);
            }

            for (int i = 0; i < listaTerceros.size(); i++) {
                if (listaTerceros.get(i).getNitalternativo() != null) {
                    if (listaTerceros.get(i).getNitalternativo().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setTercero(listaTerceros.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoTerceroNit");
                    context.update("formularioDialogos:nuevoTerceroNombre");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setTercero(listaTerceros.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarTerceroNit");
                    context.update("formularioDialogos:duplicarTerceroNombre");
                }
            } else {
                context.update("form:tercerosDialogo");
                context.execute("tercerosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoTerceroNit");
                    context.update("formularioDialogos:nuevoTerceroNombre");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarTerceroNit");
                    context.update("formularioDialogos:duplicarTerceroNombre");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CODIGO")) {
            cargarLOVPeriodicidades();
            if (tipoNuevo == 1) {
                nuevaNovedad.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
            }

            for (int i = 0; i < listaPeriodicidades.size(); i++) {
                if (listaPeriodicidades.get(i).getCodigoStr().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setPeriodicidad(listaPeriodicidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaPeriodicidadCodigo");
                    context.update("formularioDialogos:nuevaPeriodicidadDescripcion");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setPeriodicidad(listaPeriodicidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarPeriodicidadCodigo");
                    context.update("formularioDialogos:duplicarPeriodicidadDescripcion");
                }
            } else {
                context.update("form:periodicidadesDialogo");
                context.execute("periodicidadesDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaPeriodicidadCodigo");
                    context.update("formularioDialogos:nuevaPeriodicidadDescripcion");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarPeriodicidadCodigo");
                    context.update("formularioDialogos:duplicarPeriodicidadDescripcion");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            cargarLOVConceptos();
            if (tipoNuevo == 1) {
                nuevaNovedad.getConcepto().setCodigoSTR(CodigoConcepto);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getConcepto().setCodigoSTR(CodigoConcepto);
            }
            for (int i = 0; i < listaConceptos.size(); i++) {
                if (listaConceptos.get(i).getCodigoSTR().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setConcepto(listaConceptos.get(indiceUnicoElemento));
                    Formulas formula = verificarFormulaConcepto(nuevaNovedad.getConcepto().getSecuencia());
                    nuevaNovedad.setFormula(formula);
                    context.update("formularioDialogos:nuevaNovedad");
                    context.update("formularioDialogos:nuevoConceptoDescripcion");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setConcepto(listaConceptos.get(indiceUnicoElemento));
                    Formulas formula = verificarFormulaConcepto(duplicarNovedad.getConcepto().getSecuencia());
                    duplicarNovedad.setFormula(formula);
                    context.update("formularioDialogos:duplicarNovedad");
                    context.update("formularioDialogos:duplicarConceptoDescripcion");
                }
            } else {
                context.update("form:conceptosDialogo");
                context.execute("conceptosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoConcepto");
                    context.update("formularioDialogos:nuevoConceptoDescripcion");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarConcepto");
                    context.update("formularioDialogos:duplicarConceptoDescripcion");
                }
            }
        }
    }

    public void actualizarEmpleadosNovedad() {
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnAcumulado = true;
        context.update("form:ACUMULADOS");
//        PruebaEmpleados pe = administrarNovedadesEmpleados.novedadEmpleado(empleadoSeleccionadoLov.getId());
//        if (pe != null) {
//            listaEmpleadosNovedad.add(pe);
//        } else {
//            pe = new PruebaEmpleados();
//            pe.setId(empleadoSeleccionadoLov.getId());
//            pe.setCodigo(empleadoSeleccionadoLov.getCodigo());
//            pe.setNombre(empleadoSeleccionadoLov.getNombre());
//            pe.setValor(null);
//            pe.setTipo(empleadoSeleccionadoLov.getTipo());
//            listaEmpleadosNovedad.add(pe);
//        }
        System.out.println("empleadoSeleccionadoLov : " + empleadoSeleccionadoLov + "  " + empleadoSeleccionadoLov.getNombre());
        listaEmpleadosNovedad.clear();
        listaEmpleadosNovedad.add(empleadoSeleccionadoLov);
        empleadoSeleccionado = listaEmpleadosNovedad.get(0);
        modificarInfoRegistroEmpleados(listaEmpleadosNovedad.size());
//            secuenciaEmpleado = empleadoSeleccionadoLov.getId();
        listaNovedades = null;
        getListaNovedades();
        modificarInfoRegistro(listaNovedades.size());

        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        context.execute("LOVEmpleados.clearFilters()");
        context.execute("empleadosDialogo.hide()");
        context.update("formularioDialogos:LOVEmpleados");
        context.update("form:datosEmpleados");
        context.update("form:datosNovedadesEmpleado");
        //context.update("form:datosEmpleados");
        //context.update("form:datosNovedadesEmpleado");

        filtrarListaEmpleadosNovedad = null;
        empleadoSeleccionadoLov = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void actualizarFormulas() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("seleccionFormulas: " + seleccionFormulas);
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setFormula(seleccionFormulas);
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            activoBtnAcumulado = true;
            context.update("form:ACUMULADOS");
            context.update("form:datosNovedadesEmpleado");
        } else if (tipoActualizacion == 1) {
            System.out.println("seleccionFormulas: " + seleccionFormulas);
            nuevaNovedad.setFormula(seleccionFormulas);
            context.update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setFormula(seleccionFormulas);
            context.update("formularioDialogos:duplicarNovedad");

        }
        filtradoslistaFormulas = null;
        seleccionFormulas = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVFormulas:globalFilter");
        context.execute("LOVFormulas.clearFilters()");
        context.execute("formulasDialogo.hide()");
        //context.update("formularioDialogos:LOVFormulas");
    }

    public void actualizarConceptos() {
        RequestContext context = RequestContext.getCurrentInstance();
        Formulas formula;
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setConcepto(seleccionConceptos);
            formula = verificarFormulaConcepto(seleccionConceptos.getSecuencia());
            novedadSeleccionada.setFormula(formula);

            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            activoBtnAcumulado = true;
            context.update("form:ACUMULADOS");
            context.update("form:datosNovedadesEmpleado");

        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setConcepto(seleccionConceptos);
            formula = verificarFormulaConcepto(seleccionConceptos.getSecuencia());
            nuevaNovedad.setFormula(formula);
            context.update("formularioDialogos:nuevaNovedad");

        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setConcepto(seleccionConceptos);
            formula = verificarFormulaConcepto(seleccionConceptos.getSecuencia());
            duplicarNovedad.setFormula(formula);
            context.update("formularioDialogos:duplicarNovedad");
        }
        filtradoslistaConceptos = null;
        seleccionConceptos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVConceptos:globalFilter");
        context.execute("LOVConceptos.clearFilters()");
        context.execute("conceptosDialogo.hide()");
    }

    public Formulas verificarFormulaConcepto(BigInteger secCon) {
        List<FormulasConceptos> formulasConceptoSel = administrarFormulaConcepto.cargarFormulasConcepto(secCon);
        Formulas formulaR = new Formulas();
        BigInteger autoFormula;
        cargarLOVFormulas();
        if (formulasConceptoSel != null) {
            if (!formulasConceptoSel.isEmpty()) {
                autoFormula = formulasConceptoSel.get(0).getFormula().getSecuencia();
            } else {
                autoFormula = new BigInteger("4621544");
            }
        } else {
            autoFormula = new BigInteger("4621544");
        }

        for (int i = 0; i < listaFormulas.size(); i++) {
            if (autoFormula.equals(listaFormulas.get(i).getSecuencia())) {
                formulaR = listaFormulas.get(i);
            }
        }
        return formulaR;
    }

    public void actualizarPeriodicidades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setPeriodicidad(seleccionPeriodicidades);
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            activoBtnAcumulado = true;
            context.update("form:ACUMULADOS");
            context.update("form:datosNovedadesEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setPeriodicidad(seleccionPeriodicidades);
            context.update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setPeriodicidad(seleccionPeriodicidades);
            context.update("formularioDialogos:duplicarNovedad");
        }
        filtradoslistaPeriodicidades = null;
        seleccionPeriodicidades = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVPeriodicidades:globalFilter");
        context.execute("LOVPeriodicidades.clearFilters()");
        context.execute("periodicidadesDialogo.hide()");
        //context.update("formularioDialogos:LOVPeriodicidades");
    }

    public void actualizarTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setTercero(seleccionTerceros);
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            activoBtnAcumulado = true;
            context.update("form:ACUMULADOS");
            context.update("form:datosNovedadesEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setTercero(seleccionTerceros);
            context.update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setTercero(seleccionTerceros);
            context.update("formularioDialogos:duplicarNovedad");
        }
        filtradoslistaTerceros = null;
        seleccionTerceros = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVTerceros:globalFilter");
        context.execute("LOVTerceros.clearFilters()");
        context.execute("tercerosDialogo.hide()");
        //context.update("formularioDialogos:LOVTerceros");
    }

    public void mostrarTodos() {
        System.out.println("controlNovedadesEmpleados.mostrarTodos...");
        RequestContext context = RequestContext.getCurrentInstance();
        if (cargarTodos) {
            listaEmpleadosNovedad = null;
            cargarTodosEmpleados();
            cargarTodos = false;
        }

        listaEmpleadosNovedad.clear();
        if (lovEmpleados != null) {
            for (int i = 0; i < lovEmpleados.size(); i++) {
                listaEmpleadosNovedad.add(lovEmpleados.get(i));
            }
        }
        listaNovedades = null;
        empleadoSeleccionado = null;
        modificarInfoRegistroEmpleados(listaEmpleadosNovedad.size());
        getListaNovedades();
        modificarInfoRegistro(0);
        context.update("form:ACUMULADOS");
        context.update("form:datosEmpleados");
        context.update("form:datosNovedadesEmpleado");
        activoBtnAcumulado = true;
        filtrarListaEmpleadosNovedad = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    //DUPLICAR ENCARGATURA
    public void duplicarEN() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else if (novedadSeleccionada != null) {
            duplicarNovedad = new Novedades();
            k++;
            l = BigInteger.valueOf(k);
            Empleados emple = administrarNovedadesEmpleados.elEmpleado(empleadoSeleccionado.getId());

            duplicarNovedad.setSecuencia(l);
            duplicarNovedad.setEmpleado(emple);
            duplicarNovedad.setConcepto(novedadSeleccionada.getConcepto());
            duplicarNovedad.setFechainicial(novedadSeleccionada.getFechainicial());
            duplicarNovedad.setFechafinal(novedadSeleccionada.getFechafinal());
            duplicarNovedad.setFechareporte(novedadSeleccionada.getFechareporte());
            duplicarNovedad.setValortotal(novedadSeleccionada.getValortotal());
            duplicarNovedad.setSaldo(novedadSeleccionada.getSaldo());
            duplicarNovedad.setPeriodicidad(novedadSeleccionada.getPeriodicidad());
            duplicarNovedad.setTercero(novedadSeleccionada.getTercero());
            duplicarNovedad.setFormula(novedadSeleccionada.getFormula());
            duplicarNovedad.setUnidadesparteentera(novedadSeleccionada.getUnidadesparteentera());
            duplicarNovedad.setUnidadespartefraccion(novedadSeleccionada.getUnidadespartefraccion());
            duplicarNovedad.setTipo(novedadSeleccionada.getTipo());
            duplicarNovedad.setTerminal(novedadSeleccionada.getTerminal());
            duplicarNovedad.setUsuarioreporta(novedadSeleccionada.getUsuarioreporta());

            activoBtnAcumulado = true;
            context.update("form:ACUMULADOS");
            context.update("formularioDialogos:duplicarNovedad");
            context.execute("DuplicarRegistroNovedad.show()");
        }
    }

    public void cancelarCambioEmpleados() {
        filtrarLovEmpleados = null;
        empleadoSeleccionadoLov = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        context.execute("LOVEmpleados.clearFilters()");
        context.execute("empleadosDialogo.hide()");

        context.update("formularioDialogos:empleadosDialogo");
        context.update("formularioDialogos:LOVEmpleados");
        context.update("formularioDialogos:aceptarE");

    }

    public void cancelarCambioFormulas() {
        filtradoslistaFormulas = null;
        seleccionFormulas = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVFormulas:globalFilter");
        context.execute("LOVFormulas.clearFilters()");
        context.execute("formulasDialogo.hide()");
    }

    public void cancelarCambioConceptos() {
        filtradoslistaConceptos = null;
        seleccionConceptos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVConceptos:globalFilter");
        context.execute("LOVConceptos.clearFilters()");
        context.execute("conceptosDialogo.hide()");
    }

    public void cancelarCambioPeriodicidades() {
        filtradoslistaPeriodicidades = null;
        seleccionPeriodicidades = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVPeriodicidades:globalFilter");
        context.execute("LOVPeriodicidades.clearFilters()");
        context.execute("periodicidadesDialogo.hide()");
    }

    public void cancelarCambioTerceros() {
        filtradoslistaTerceros = null;
        seleccionTerceros = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTerceros:globalFilter");
        context.execute("LOVTerceros.clearFilters()");
        context.execute("tercerosDialogo.hide()");
    }

    public void todasNovedades() {
        System.out.println("Ingrese a todasNovedades()");
        listaNovedades.clear();
        listaNovedades = administrarNovedadesEmpleados.todasNovedades(empleadoSeleccionado.getId());
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnAcumulado = true;
        modificarInfoRegistro(listaNovedades.size());
        context.update("form:ACUMULADOS");
        todas = true;
        actuales = false;
        context.update("form:datosNovedadesEmpleado");
        context.update("form:TODAS");
        context.update("form:ACTUALES");
        System.out.println("Sali de todasNovedades()");

    }

    public void actualesNovedades() {
        listaNovedades.clear();
        listaNovedades = administrarNovedadesEmpleados.novedadesEmpleado(empleadoSeleccionado.getId());
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnAcumulado = true;
        modificarInfoRegistro(listaNovedades.size());
        context.update("form:ACUMULADOS");
        todas = false;
        actuales = true;
        context.update("form:datosNovedadesEmpleado");
        context.update("form:TODAS");
        context.update("form:ACTUALES");
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "NovedadesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        RequestContext contexto = RequestContext.getCurrentInstance();
        activoBtnAcumulado = true;
        contexto.update("form:ACUMULADOS");
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "NovedadesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        RequestContext contexto = RequestContext.getCurrentInstance();
        activoBtnAcumulado = true;
        contexto.update("form:ACUMULADOS");
    }

    //LIMPIAR NUEVO REGISTRO NOVEDAD
    public void limpiarNuevaNovedad() {
        nuevaNovedad = new Novedades();
        nuevaNovedad.setPeriodicidad(new Periodicidades());
        nuevaNovedad.getPeriodicidad().setNombre(" ");
        nuevaNovedad.getPeriodicidad().setCodigoStr(" ");
        nuevaNovedad.setTercero(new Terceros());
        nuevaNovedad.getTercero().setNombre(" ");
        nuevaNovedad.setConcepto(new Conceptos());
        nuevaNovedad.getConcepto().setDescripcion(" ");
        nuevaNovedad.getConcepto().setCodigoSTR("0");
        nuevaNovedad.setTipo("FIJA");
        nuevaNovedad.setUsuarioreporta(new Usuarios());
        nuevaNovedad.setTerminal(" ");
        nuevaNovedad.setFechareporte(new Date());
        resultado = 0;
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar Encargaturas
     */
    public void limpiarduplicarNovedades() {
        duplicarNovedad = new Novedades();
        duplicarNovedad.setPeriodicidad(new Periodicidades());
        duplicarNovedad.getPeriodicidad().setNombre(" ");
        duplicarNovedad.getPeriodicidad().setCodigoStr(" ");
        duplicarNovedad.setTercero(new Terceros());
        duplicarNovedad.getTercero().setNombre(" ");
        duplicarNovedad.setConcepto(new Conceptos());
        duplicarNovedad.getConcepto().setDescripcion(" ");
        duplicarNovedad.getConcepto().setCodigoSTR("0");
        duplicarNovedad.setTipo("FIJA");
        duplicarNovedad.setUsuarioreporta(new Usuarios());
        duplicarNovedad.setTerminal(" ");
        duplicarNovedad.setFechareporte(new Date());
    }

    //RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnAcumulado = true;
        context.update("form:ACUMULADOS");
        System.out.println("lol");
        if (novedadSeleccionada != null) {
            System.out.println("lol 2");
            int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADES");
            System.out.println("resultado: " + result);
            if (result == 1) {
                context.execute("errorObjetosDB.show()");
            } else if (result == 2) {
                context.execute("confirmarRastro.show()");
            } else if (result == 3) {
                context.execute("errorRegistroRastro.show()");
            } else if (result == 4) {
                context.execute("errorTablaConRastro.show()");
            } else if (result == 5) {
                context.execute("errorTablaSinRastro.show()");
            }
//            } else {
//                context.execute("seleccionarRegistro.show()");
//            }
        } else if (administrarRastros.verificarHistoricosTabla("NOVEDADES")) {
            context.execute("confirmarRastroHistorico.show()");
        } else {
            context.execute("errorRastroHistorico.show()");
        }
    }

    //BORRAR NOVEDADES
    public void borrarNovedades() {

        if (novedadSeleccionada != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            activoBtnAcumulado = true;
            context.update("form:ACUMULADOS");

            if (!listaNovedadesModificar.isEmpty() && listaNovedadesModificar.contains(novedadSeleccionada)) {
                listaNovedadesModificar.remove(novedadSeleccionada);
                listaNovedadesBorrar.add(novedadSeleccionada);
            } else if (!listaNovedadesCrear.isEmpty() && listaNovedadesCrear.contains(novedadSeleccionada)) {
                listaNovedadesCrear.remove(novedadSeleccionada);
            } else {
                listaNovedadesBorrar.add(novedadSeleccionada);
            }
            listaNovedades.remove(novedadSeleccionada);
            if (tipoLista == 1) {
                filtradosListaNovedades.remove(novedadSeleccionada);
            }
            modificarInfoRegistro(listaNovedades.size());
            context.update("form:datosNovedadesEmpleado");
            novedadSeleccionada = null;
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("seleccionarRegistro.show()");
        }
    }

    public void anularBotonLOV() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void activarBotonLOV() {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void cargarLOVConceptos() {
        if (listaConceptos == null || listaConceptos.isEmpty()) {
            listaConceptos = administrarNovedadesEmpleados.lovConceptos();
        }
    }

    public void cargarLOVPeriodicidades() {
        if (listaPeriodicidades == null || listaPeriodicidades.isEmpty()) {
            listaPeriodicidades = administrarNovedadesEmpleados.lovPeriodicidades();
        }
    }

    public void cargarLOVFormulas() {
        if (listaFormulas == null || listaFormulas.isEmpty()) {
            listaFormulas = administrarNovedadesEmpleados.lovFormulas();
        }
    }

    public void cargarLOVTerceros() {
        if (listaTerceros == null || listaTerceros.isEmpty()) {
            listaTerceros = administrarNovedadesEmpleados.lovTerceros();
        }
    }

    public void cargarTodosEmpleados() {
        if (listaEmpleadosNovedad == null) {
            listaEmpleadosNovedad = administrarNovedadesEmpleados.empleadosNovedad();
            lovEmpleados = new ArrayList<PruebaEmpleados>();
            for (int i = 0; i < listaEmpleadosNovedad.size(); i++) {
                lovEmpleados.add(listaEmpleadosNovedad.get(i));
            }
        }
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        novedadSeleccionada = null;
        anularBotonLOV();
        modificarInfoRegistro(filtradosListaNovedades.size());
    }

    public void contarRegistros() {
        if (listaNovedades != null) {
            modificarInfoRegistro(listaNovedades.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void modificarInfoRegistroEmpleados(int valor) {
        infoRegistrosEmpleadosNovedades = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
    }

    public void modificarInfoRegistroLovEmpleados(int valor) {
        infoRegistroEmpleadosLOV = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleadosLOV");
    }

    public void modificarInfoRegistroConceptos(int valor) {
        infoRegistroConceptos = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroConceptos");
    }

    public void modificarInfoRegistroTerceros(int valor) {
        infoRegistroTerceros = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTerceros");
    }

    public void modificarInfoRegistroFormulas(int valor) {
        infoRegistroFormulas = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroFormulas");
    }

    public void modificarInfoRegistroPeriodicidad(int valor) {
        infoRegistroPeriodicidad = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPeriodicidad");
    }

    public void eventoFiltrarFormulas() {
        modificarInfoRegistroFormulas(filtradoslistaFormulas.size());
    }

    public void eventoFiltrarPeriodicidades() {
        modificarInfoRegistroPeriodicidad(filtradoslistaPeriodicidades.size());
    }

    public void eventoFiltrarTerceros() {
        modificarInfoRegistroTerceros(filtradoslistaTerceros.size());
    }

    public void eventoFiltrarConceptos() {
        modificarInfoRegistroConceptos(filtradoslistaConceptos.size());
    }

    public void eventoFiltrarEmpleadosLOV() {
        modificarInfoRegistroLovEmpleados(filtrarLovEmpleados.size());
    }

    //------------------------INICIO GET's & SET's-----------------------------
    public void setListaEmpleadosNovedad(List<PruebaEmpleados> listaEmpleados) {
        this.listaEmpleadosNovedad = listaEmpleados;
    }

    public List<PruebaEmpleados> getFiltrarListaEmpleadosNovedad() {
        return filtrarListaEmpleadosNovedad;
    }

    public void setFiltrarListaEmpleadosNovedad(List<PruebaEmpleados> filtradosListaEmpleadosNovedad) {
        this.filtrarListaEmpleadosNovedad = filtradosListaEmpleadosNovedad;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public PruebaEmpleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(PruebaEmpleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    //LISTA NOVEDADES
    public List<Novedades> getListaNovedades() {
        if (listaNovedades == null && empleadoSeleccionado != null) {
            listaNovedades = administrarNovedadesEmpleados.novedadesEmpleado(empleadoSeleccionado.getId());
        }
        return listaNovedades;
    }

    public void setListaNovedades(List<Novedades> listaNovedades) {
        this.listaNovedades = listaNovedades;
    }

    public List<Novedades> getFiltradosListaNovedades() {
        return filtradosListaNovedades;
    }

    public void setFiltradosListaNovedades(List<Novedades> filtradosListaNovedades) {
        this.filtradosListaNovedades = filtradosListaNovedades;
    }
    //L.O.V PERIODICIDADES

    public List<Periodicidades> getListaPeriodicidades() {
        return listaPeriodicidades;
    }

    public void setListaPeriodicidades(List<Periodicidades> listaPeriodicidades) {
        this.listaPeriodicidades = listaPeriodicidades;
    }

    public List<Periodicidades> getFiltradoslistaPeriodicidades() {
        return filtradoslistaPeriodicidades;
    }

    public void setFiltradoslistaPeriodicidades(List<Periodicidades> filtradoslistaPeriodicidades) {
        this.filtradoslistaPeriodicidades = filtradoslistaPeriodicidades;
    }

    public Periodicidades getSeleccionPeriodicidades() {
        return seleccionPeriodicidades;
    }

    public void setSeleccionPeriodicidades(Periodicidades seleccionPeriodicidades) {
        this.seleccionPeriodicidades = seleccionPeriodicidades;
    }
    //L.O.V CONCEPTOS

    public List<Conceptos> getListaConceptos() {
        return listaConceptos;
    }

    public void setListaConceptos(List<Conceptos> listaConceptos) {
        this.listaConceptos = listaConceptos;
    }
    //L.O.V FORMULAS

    public List<Formulas> getListaFormulas() {
        return listaFormulas;
    }

    public void setListaFormulas(List<Formulas> listaFormulas) {
        this.listaFormulas = listaFormulas;
    }

    public List<Conceptos> getFiltradoslistaConceptos() {
        return filtradoslistaConceptos;
    }

    public void setFiltradoslistaConceptos(List<Conceptos> filtradoslistaConceptos) {
        this.filtradoslistaConceptos = filtradoslistaConceptos;
    }

    public Conceptos getSeleccionConceptos() {
        return seleccionConceptos;
    }

    public void setSeleccionConceptos(Conceptos seleccionConceptos) {
        this.seleccionConceptos = seleccionConceptos;
    }

    public List<Formulas> getFiltradoslistaFormulas() {
        return filtradoslistaFormulas;
    }

    public void setFiltradoslistaFormulas(List<Formulas> filtradoslistaFormulas) {
        this.filtradoslistaFormulas = filtradoslistaFormulas;
    }

    public Formulas getSeleccionFormulas() {
        return seleccionFormulas;
    }

    public void setSeleccionFormulas(Formulas seleccionFormulas) {
        this.seleccionFormulas = seleccionFormulas;
    }
    //L.O.V TERCEROS

    public List<Terceros> getListaTerceros() {
        return listaTerceros;
    }

    public void setListaTerceros(List<Terceros> listaTerceros) {
        this.listaTerceros = listaTerceros;
    }

    public List<Terceros> getFiltradoslistaTerceros() {
        return filtradoslistaTerceros;
    }

    public void setFiltradoslistaTerceros(List<Terceros> filtradoslistaTerceros) {
        this.filtradoslistaTerceros = filtradoslistaTerceros;
    }

    public Terceros getSeleccionTerceros() {
        return seleccionTerceros;
    }

    public void setSeleccionTerceros(Terceros seleccionTerceros) {
        this.seleccionTerceros = seleccionTerceros;
    }

    public Novedades getNuevaNovedad() {
        return nuevaNovedad;
    }

    public void setNuevaNovedad(Novedades nuevaNovedad) {
        this.nuevaNovedad = nuevaNovedad;
    }

    public Novedades getEditarNovedades() {
        return editarNovedades;
    }

    public void setEditarNovedades(Novedades editarNovedades) {
        this.editarNovedades = editarNovedades;
    }

    public Novedades getDuplicarNovedad() {
        return duplicarNovedad;
    }

    public void setDuplicarNovedad(Novedades duplicarNovedad) {
        this.duplicarNovedad = duplicarNovedad;
    }

    public List<PruebaEmpleados> getListaEmpleadosNovedad() {
        if (listaEmpleadosNovedad == null) {

            cantidadEmpleadosNov = administrarNovedadesEmpleados.cuantosEmpleadosNovedad();
            System.out.println("getListaEmpleadosNovedad() cantidadEmpleadosNov : " + cantidadEmpleadosNov);

            if (cantidadEmpleadosNov > 150) {
                listaEmpleadosNovedad = administrarNovedadesEmpleados.empleadosNovedadSoloAlgunos();
                cargarTodos = true;
            } else if (cantidadEmpleadosNov == -1) {
                System.err.println("ERROR EN getListaEmpleadosNovedad() NO TRAE EL CONTEO DE LOS EMPLEADOS");
            } else {
                cargarTodosEmpleados();
            }
        }
        return listaEmpleadosNovedad;
    }

    public String getAlias() {
        alias = administrarNovedadesEmpleados.alias();
        System.out.println("Alias: " + alias);
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Usuarios getUsuarioBD() {
        getAlias();
        usuarioBD = administrarNovedadesEmpleados.usuarioBD(alias);
        return usuarioBD;
    }

    public void setUsuarioBD(Usuarios usuarioBD) {
        this.usuarioBD = usuarioBD;
    }

    public int getResultado() {
        if (!listaNovedadesBorrar.isEmpty()) {
            for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
                resultado = administrarNovedadesEmpleados.solucionesFormulas(listaNovedadesBorrar.get(i).getSecuencia());
            }
        }
        return resultado;
    }

    public boolean isTodas() {
        return todas;
    }

    public boolean isActuales() {
        return actuales;
    }

    public boolean isActivoBtnAcumulado() {
        return activoBtnAcumulado;
    }

    public void setActivoBtnAcumulado(boolean activoBtnAcumulado) {
        this.activoBtnAcumulado = activoBtnAcumulado;
    }

    public Novedades getActualNovedadTabla() {
        return actualNovedadTabla;
    }

    public void setActualNovedadTabla(Novedades actualNovedadTabla) {
        this.actualNovedadTabla = actualNovedadTabla;
    }

    public Novedades getNovedadSeleccionada() {
        return novedadSeleccionada;
    }

    public void setNovedadSeleccionada(Novedades novedadSeleccionada) {
        this.novedadSeleccionada = novedadSeleccionada;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public PruebaEmpleados getEmpleadoSeleccionadoLov() {
        return empleadoSeleccionadoLov;
    }

    public void setEmpleadoSeleccionadoLov(PruebaEmpleados empleadoSeleccionadoLov) {
        this.empleadoSeleccionadoLov = empleadoSeleccionadoLov;
    }

    public List<PruebaEmpleados> getFiltrarLovEmpleados() {
        return filtrarLovEmpleados;
    }

    public void setFiltrarLovEmpleados(List<PruebaEmpleados> filtrarLovEmpleados) {
        this.filtrarLovEmpleados = filtrarLovEmpleados;
    }

    public List<PruebaEmpleados> getLovEmpleados() {
        return lovEmpleados;
    }

    public void setLovEmpleados(List<PruebaEmpleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroConceptos() {
        return infoRegistroConceptos;
    }

    public void setInfoRegistroConceptos(String infoRegistroConceptos) {
        this.infoRegistroConceptos = infoRegistroConceptos;
    }

    public String getInfoRegistroPeriodicidad() {
        return infoRegistroPeriodicidad;
    }

    public void setInfoRegistroPeriodicidad(String infoRegistroPeriodicidad) {
        this.infoRegistroPeriodicidad = infoRegistroPeriodicidad;
    }

    public String getInfoRegistroFormulas() {
        return infoRegistroFormulas;
    }

    public void setInfoRegistroFormulas(String infoRegistroFormulas) {
        this.infoRegistroFormulas = infoRegistroFormulas;
    }

    public String getInfoRegistroTerceros() {
        return infoRegistroTerceros;
    }

    public void setInfoRegistroTerceros(String infoRegistroTerceros) {
        this.infoRegistroTerceros = infoRegistroTerceros;
    }

    public String getInfoRegistrosEmpleadosNovedades() {
        return infoRegistrosEmpleadosNovedades;
    }

    public void setInfoRegistrosEmpleadosNovedades(String infoRegistrosEmpleadosNovedades) {
        this.infoRegistrosEmpleadosNovedades = infoRegistrosEmpleadosNovedades;
    }

    public String getInfoRegistroEmpleadosLOV() {
        return infoRegistroEmpleadosLOV;
    }

    public void setInfoRegistroEmpleadosLOV(String infoRegistroEmpleadosLOV) {
        this.infoRegistroEmpleadosLOV = infoRegistroEmpleadosLOV;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

}
