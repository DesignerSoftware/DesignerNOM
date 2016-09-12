/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Causasausentismos;
import Entidades.Clasesausentismos;
import Entidades.Diagnosticoscategorias;
import Entidades.Empleados;
import Entidades.EnfermeadadesProfesionales;
import Entidades.Ibcs;
import Entidades.Soaccidentes;
import Entidades.Soausentismos;
import Entidades.Terceros;
import Entidades.Tiposausentismos;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSoausentismosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import utilidadesUI.PrimefacesContextUI;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlAusentismos implements Serializable {

    @EJB
    AdministrarSoausentismosInterface administrarAusentismos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //LISTA FICTI PORCENTAJES
    private List<String> listaPorcentaje;
    private List<String> filtradosListaPorcentajes;
    private String seleccionPorcentajes;
    //LISTA FICTI IBCS
    private List<Ibcs> listaIBCS;
    private List<Ibcs> filtradosListaIBCS;
    private Ibcs seleccionIBCS;
    //LISTA FICTI FORMA LIQUIDACION
    private List<String> listaForma;
    private List<String> filtradosListaForma;
    private String seleccionForma;
    //SECUENCIA DEL EMPLEADO
    private BigInteger secuenciaEmpleado;
    //Secuencia de la Causa
    private BigInteger secuenciaCausa;
    //Secuencia del ausentismo
    private BigInteger secuenciaAusentismo;
    //LISTA AUSENTISMOS
    private List<Soausentismos> listaAusentismos;
    private List<Soausentismos> filtradosListaAusentismos;
    private Soausentismos ausentismoSeleccionado;
    //LISTA DE ARRIBA
    private List<Empleados> listaEmpleadosAusentismo;
    private List<Empleados> filtradosListaEmpleadosAusentismo;
    private Empleados seleccionMostrar; //Seleccion Mostrar
    //editar celda
    private Soausentismos editarAusentismos;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private int banderaBotones;
    private int banderaBotonesD;
    private boolean permitirIndex;
    //RASTROS
    private boolean guardado, guardarOk;
    //Crear Novedades
    private List<Soausentismos> listaAusentismosCrear;
    public Soausentismos nuevoAusentismo;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<Soausentismos> listaAusentismosModificar;
    //Borrar Novedades
    private List<Soausentismos> listaAusentismosBorrar;
    //L.O.V EMPLEADOS
    private List<Empleados> listaEmpleados;
    private List<Empleados> filtradoslistaEmpleados;
    private Empleados seleccionEmpleados;
    //Autocompletar
    private String TipoAusentismo, Tercero, ClaseAusentismo, CausaAusentismo, Porcentaje, Forma, AD, Enfermedad, Diagnostico;
    private String BaseLiquidacion, Dias, Horas, Fechafinaus, Fechaexpedicion, InicioPago, FinPago, NumeroCertificado, Prorrogas; // faltan más campos
    private String Relacionn, Observacion; // faltan más campos
    // faltan más campos
    //L.O.V TIPO AUSENTISMO
    private List<Tiposausentismos> listaTiposAusentismos;
    private List<Tiposausentismos> filtradoslistaTiposAusentismos;
    private Tiposausentismos seleccionTiposAusentismos;
    //L.O.V CLASE AUSENTISMO
    private List<Clasesausentismos> listaClasesAusentismos;
    private List<Clasesausentismos> filtradoslistaClasesAusentismos;
    private Clasesausentismos seleccionClasesAusentismos;
    //L.O.V CAUSA AUSENTISMO
    private List<Causasausentismos> listaCausasAusentismos;
    private List<Causasausentismos> filtradoslistaCausasAusentismos;
    private Causasausentismos seleccionCausasAusentismos;
    //L.O.V Descripcion Accidente
    private List<Soaccidentes> listaAccidentes;
    private List<Soaccidentes> filtradoslistaAccidentes;
    private Soaccidentes seleccionAccidentes;
    //L.O.V Enfermedades Profesionales
    private List<EnfermeadadesProfesionales> listaEnfermeadadesProfesionales;
    private List<EnfermeadadesProfesionales> filtradoslistaEnfermeadadesProfesionales;
    private EnfermeadadesProfesionales seleccionEnfermeadadesProfesionales;
    //L.O.V Terceros
    private List<Terceros> listaTerceros;
    private List<Terceros> filtradoslistaTerceros;
    private Terceros seleccionTerceros;
    //L.O.V Diagnostivos
    private List<Diagnosticoscategorias> listaDiagnosticos;
    private List<Diagnosticoscategorias> filtradoslistaDiagnosticos;
    private Diagnosticoscategorias seleccionDiagnosticos;
    //L.O.V Prorrogas
    private List<Soausentismos> listaProrrogas;
    private List<Soausentismos> filtradoslistaProrrogas;
    private Soausentismos seleccionProrrogas;
    //Duplicar
    public Soausentismos duplicarAusentismo;
    //PRORROGA MOSTRAR
    private String Prorroga, Relacion;
    //Columnas Tabla NOVEDADES
    private Column ATipo, AClase, ACausa, ADias, AHoras, AFecha, AFechaFinaus, AFechaExpedicion, AFechaInipago,
            AFechaFinpago, APorcentaje, ABase, AForma, ADescripcionCaso, AEnfermedad, ANumero, ADiagnostico,
            AProrroga, ARelacion, ARelacionada, ATercero, AObservaciones;
    //
    private CommandButton botonAgregar, botonCancelar, botonLimpiar;
    private CommandButton botonAgregarD, botonCancelarD, botonLimpiarD;
    //
    private boolean cambiosPagina;
    //
    private String altoTabla;
    private String altoDialogoNuevo;
    private String altoDialogoDuplicar;
    private boolean colapsado;
    private String infoRegistroAusentismos;
    private String infoRegistroTipo;
    private String infoRegistroClase;
    private String infoRegistroCausa;
    private String infoRegistroPorcentaje;
    private String infoRegistroBase;
    private String infoRegistroForma;
    private String infoRegistroAccidente;
    private String infoRegistroEnfermedad;
    private String infoRegistroDiagnostico;
    private String infoRegistroProrroga;
    private String infoRegistroTercero;
    private String infoRegistroEmpleado;
    private String infoRegistroEmpleadoLov;
    private boolean activarLov;
    private DataTable tablaC;

    public ControlAusentismos() {
        colapsado = true;
        altoDialogoNuevo = "430";
        altoDialogoDuplicar = "430";
        altoTabla = "145";
        cambiosPagina = true;
        Relacion = null;
        Prorroga = null;
        listaProrrogas = null;
        listaIBCS = null;
        listaAccidentes = null;
        listaDiagnosticos = null;
        listaPorcentaje = new ArrayList<String>();
        listaPorcentaje.add("50");
        listaPorcentaje.add("66.6666");
        listaPorcentaje.add("80");
        listaPorcentaje.add("100");
        listaForma = new ArrayList<String>();
        listaForma.add("BASICO");
        listaForma.add("IBC MES ANTERIOR");
        listaForma.add("IBC MES ENERO");
        listaForma.add("IBC MES INCAPACIDAD");
        listaForma.add("PROMEDIO ACUMULADOS 12 MESES");
        listaForma.add("PROMEDIO IBC 12 MESES");
        listaForma.add("PROMEDIO IBC 6 MESES");
        permitirIndex = true;
        listaAusentismos = null;
        listaEmpleados = null;
        listaEmpleadosAusentismo = null;
        permitirIndex = true;
        aceptar = true;
        ausentismoSeleccionado = null;
        guardado = true;
        tipoLista = 0;
        listaAusentismosBorrar = new ArrayList<Soausentismos>();
        listaAusentismosCrear = new ArrayList<Soausentismos>();
        listaAusentismosModificar = new ArrayList<Soausentismos>();
        //Crear VC
        nuevoAusentismo = new Soausentismos();
        nuevoAusentismo.setFecha(new Date());
        nuevoAusentismo.setTipo(new Tiposausentismos());
        nuevoAusentismo.setClase(new Clasesausentismos());
        nuevoAusentismo.setCausa(new Causasausentismos());
        nuevoAusentismo.setPorcentajeindividual(BigDecimal.valueOf(0));
        nuevoAusentismo.setBaseliquidacion(BigInteger.valueOf(0));
        nuevoAusentismo.setRelacionadaBool(false);
        nuevoAusentismo.setAccidente(new Soaccidentes());
        nuevoAusentismo.setEnfermedad(new EnfermeadadesProfesionales());
        nuevoAusentismo.setDiagnosticocategoria(new Diagnosticoscategorias());
        nuevoAusentismo.setProrroga(new Soausentismos());
        nuevoAusentismo.setTercero(new Terceros());
        bandera = 0;
        banderaBotones = 0;
        banderaBotonesD = 0;
        ausentismoSeleccionado = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarAusentismos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            contarRegistros();
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //Ubicacion Celda Arriba 
    public void cambiarEmpleado() {
        //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
        //{
        if (listaAusentismosCrear.isEmpty() && listaAusentismosBorrar.isEmpty() && listaAusentismosModificar.isEmpty()) {
            secuenciaEmpleado = seleccionMostrar.getSecuencia();
            listaAusentismos = null;
            getListaAusentismos();
            modificarInfoRegistroAusentismos(listaAusentismos.size());
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
            //}
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:cambiar");
            PrimefacesContextUI.ejecutar("PF('cambiar').show()");
        }
    }

    public void limpiarListas() {
        listaAusentismosCrear.clear();
        listaAusentismosBorrar.clear();
        listaAusentismosModificar.clear();
        secuenciaEmpleado = seleccionMostrar.getSecuencia();
        listaAusentismos = null;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
    }

    public void asignarIndex(Soausentismos ausentismo, int dlg, int LND) {
        ausentismoSeleccionado = ausentismo;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 0) {
            habilitarBotonLov();
            modificarInfoRegistroEmpleadoLov(listaEmpleados.size());
            PrimefacesContextUI.actualizar("formularioDialogos:empleadosDialogo");
            PrimefacesContextUI.ejecutar("PF('empleadosDialogo').show()");
        } else if (dlg == 1) {
            habilitarBotonLov();
            modificarInfoRegistroTipo(listaTiposAusentismos.size());
            PrimefacesContextUI.actualizar("formularioDialogos:tiposAusentismosDialogo");
            PrimefacesContextUI.ejecutar("PF('tiposAusentismosDialogo').show()");
        } else if (dlg == 2) {
            habilitarBotonLov();
            modificarInfoRegistroClase(listaClasesAusentismos.size());
            PrimefacesContextUI.actualizar("formularioDialogos:clasesAusentismosDialogo");
            PrimefacesContextUI.ejecutar("PF('clasesAusentismosDialogo').show()");
        } else if (dlg == 3) {
            habilitarBotonLov();
            modificarInfoRegistroCausa(listaCausasAusentismos.size());
            PrimefacesContextUI.actualizar("formularioDialogos:causasAusentismosDialogo");
            PrimefacesContextUI.ejecutar("PF('causasAusentismosDialogo').show()");
        } else if (dlg == 4) {
            habilitarBotonLov();
            modificarInfoRegistroPorcentaje(listaPorcentaje.size());
            PrimefacesContextUI.actualizar("formularioDialogos:porcentajesDialogo");
            PrimefacesContextUI.ejecutar("PF('porcentajesDialogo').show()");
        } else if (dlg == 5) {
            habilitarBotonLov();
            modificarInfoRegistroBase(listaIBCS.size());
            PrimefacesContextUI.actualizar("formularioDialogos:ibcsDialogo");
            PrimefacesContextUI.ejecutar("PF('ibcsDialogo').show()");
        } else if (dlg == 6) {
            habilitarBotonLov();
            modificarInfoRegistroForma(listaForma.size());
            PrimefacesContextUI.actualizar("formularioDialogos:formasDialogo");
            PrimefacesContextUI.ejecutar("PF('formasDialogo').show()");
        } else if (dlg == 7) {
            habilitarBotonLov();
            modificarInfoRegistroAccidente(listaAccidentes.size());
            PrimefacesContextUI.actualizar("formularioDialogos:accidentesDialogo");
            PrimefacesContextUI.ejecutar("PF('accidentesDialogo').show()");
        } else if (dlg == 8) {
            habilitarBotonLov();
            modificarInfoRegistroTercero(listaTerceros.size());
            PrimefacesContextUI.actualizar("formularioDialogos:tercerosDialogo");
            PrimefacesContextUI.ejecutar("PF('tercerosDialogo').show()");
        } else if (dlg == 9) {
            habilitarBotonLov();
            modificarInfoRegistroEnfermedad(listaEnfermeadadesProfesionales.size());
            PrimefacesContextUI.actualizar("formularioDialogos:enfermedadesDialogo");
            PrimefacesContextUI.ejecutar("PF('enfermedadesDialogo').show()");
        } else if (dlg == 10) {
            habilitarBotonLov();
            modificarInfoRegistroProrroga(listaProrrogas.size());
            PrimefacesContextUI.actualizar("formularioDialogos:prorrogasDialogo");
            PrimefacesContextUI.ejecutar("PF('prorrogasDialogo').show()");
        } else if (dlg == 11) {
            habilitarBotonLov();
            modificarInfoRegistroDiagnostico(listaDiagnosticos.size());
            PrimefacesContextUI.actualizar("formularioDialogos:diagnosticosDialogo");
            PrimefacesContextUI.ejecutar("PF('diagnosticosDialogo').show()");
        }
    }

    public void asignarIndex(int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;

        if (dlg == 0) {
            habilitarBotonLov();
            modificarInfoRegistroEmpleadoLov(listaEmpleados.size());
            PrimefacesContextUI.actualizar("formularioDialogos:empleadosDialogo");
            PrimefacesContextUI.ejecutar("PF('empleadosDialogo').show()");
        } else if (dlg == 1) {
            habilitarBotonLov();
            modificarInfoRegistroTipo(listaTiposAusentismos.size());
            PrimefacesContextUI.actualizar("formularioDialogos:tiposAusentismosDialogo");
            PrimefacesContextUI.ejecutar("PF('tiposAusentismosDialogo').show()");
        } else if (dlg == 2) {
            habilitarBotonLov();
            modificarInfoRegistroClase(listaClasesAusentismos.size());
            PrimefacesContextUI.actualizar("formularioDialogos:clasesAusentismosDialogo");
            PrimefacesContextUI.ejecutar("PF('clasesAusentismosDialogo').show()");
        } else if (dlg == 3) {
            habilitarBotonLov();
            modificarInfoRegistroCausa(listaCausasAusentismos.size());
            PrimefacesContextUI.actualizar("formularioDialogos:causasAusentismosDialogo");
            PrimefacesContextUI.ejecutar("PF('causasAusentismosDialogo').show()");
        } else if (dlg == 4) {
            habilitarBotonLov();
            modificarInfoRegistroPorcentaje(listaPorcentaje.size());
            PrimefacesContextUI.actualizar("formularioDialogos:porcentajesDialogo");
            PrimefacesContextUI.ejecutar("PF('porcentajesDialogo').show()");
        } else if (dlg == 5) {
            habilitarBotonLov();
            modificarInfoRegistroBase(listaIBCS.size());
            PrimefacesContextUI.actualizar("formularioDialogos:ibcsDialogo");
            PrimefacesContextUI.ejecutar("PF('ibcsDialogo').show()");
        } else if (dlg == 6) {
            habilitarBotonLov();
            modificarInfoRegistroForma(listaForma.size());
            PrimefacesContextUI.actualizar("formularioDialogos:formasDialogo");
            PrimefacesContextUI.ejecutar("PF('formasDialogo').show()");
        } else if (dlg == 7) {
            habilitarBotonLov();
            modificarInfoRegistroAccidente(listaAccidentes.size());
            PrimefacesContextUI.actualizar("formularioDialogos:accidentesDialogo");
            PrimefacesContextUI.ejecutar("PF('accidentesDialogo').show()");
        } else if (dlg == 8) {
            habilitarBotonLov();
            modificarInfoRegistroTercero(listaTerceros.size());
            PrimefacesContextUI.actualizar("formularioDialogos:tercerosDialogo");
            PrimefacesContextUI.ejecutar("PF('tercerosDialogo').show()");
        } else if (dlg == 9) {
            habilitarBotonLov();
            modificarInfoRegistroEnfermedad(listaEnfermeadadesProfesionales.size());
            PrimefacesContextUI.actualizar("formularioDialogos:enfermedadesDialogo");
            PrimefacesContextUI.ejecutar("PF('enfermedadesDialogo').show()");
        } else if (dlg == 10) {
            habilitarBotonLov();
            modificarInfoRegistroProrroga(listaProrrogas.size());
            PrimefacesContextUI.actualizar("formularioDialogos:prorrogasDialogo");
            PrimefacesContextUI.ejecutar("PF('prorrogasDialogo').show()");
        } else if (dlg == 11) {
            habilitarBotonLov();
            modificarInfoRegistroDiagnostico(listaDiagnosticos.size());
            PrimefacesContextUI.actualizar("formularioDialogos:diagnosticosDialogo");
            PrimefacesContextUI.ejecutar("PF('diagnosticosDialogo').show()");
        }
    }

    public void mostrarTodos() {

        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaEmpleadosAusentismo.isEmpty()) {
            listaEmpleadosAusentismo.clear();
        }
        //listaEmpleadosNovedad = listaValEmpleados;
        if (listaEmpleadosAusentismo != null) {
            for (int i = 0; i < listaEmpleados.size(); i++) {
                listaEmpleadosAusentismo.add(listaEmpleados.get(i));
            }

        }
        seleccionEmpleados = listaEmpleadosAusentismo.get(0);
        listaAusentismos = administrarAusentismos.ausentismosEmpleado(seleccionEmpleados.getSecuencia());
        modificarInforegistroEmpleado(listaEmpleadosAusentismo.size());
        PrimefacesContextUI.actualizar("form:datosEmpleados");
        PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        //getListaConceptosNovedad();

        listaAusentismos = null;
        filtradosListaAusentismos = null;
        aceptar = true;
        ausentismoSeleccionado = null;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        Empleados e = seleccionEmpleados;

        if (!listaEmpleadosAusentismo.isEmpty()) {
            listaEmpleadosAusentismo.clear();
            listaEmpleadosAusentismo.add(e);
            seleccionMostrar = listaEmpleadosAusentismo.get(0);
        } else {
            listaEmpleadosAusentismo.add(e);
        }
        cambiosPagina = false;
        secuenciaEmpleado = seleccionEmpleados.getSecuencia();
        listaAusentismos = null;
        modificarInforegistroEmpleado(listaEmpleados.size());
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVEmpleados').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('empleadosDialogo').hide()");
        PrimefacesContextUI.actualizar("formularioDialogos:LOVEmpleados");
        PrimefacesContextUI.actualizar("form:datosEmpleados");
        PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        filtradosListaAusentismos = null;
        seleccionEmpleados = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void actualizarProrrogas() {
        RequestContext context = RequestContext.getCurrentInstance();
        ausentismoSeleccionado.setProrroga(seleccionProrrogas);
        context.reset("formularioDialogos:LOVProrrogas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVProrrogas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('prorrogasDialogo').hide()");
        ///PrimefacesContextUI.actualizar("formularioDialogos:LOVProrrogas");
        //PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        cambiosPagina = false;
        filtradosListaAusentismos = null;
        seleccionProrrogas = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void actualizarTiposAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setTipo(seleccionTiposAusentismos);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            } else {
                ausentismoSeleccionado.setTipo(seleccionTiposAusentismos);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setTipo(seleccionTiposAusentismos);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setTipo(seleccionTiposAusentismos);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");

        }

        filtradoslistaTiposAusentismos = null;
        seleccionTiposAusentismos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
//        PrimefacesContextUI.actualizar("formularioDialogos:LOVTiposAusentismos");
        context.reset("formularioDialogos:LOVTiposAusentismos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVTiposAusentismos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('tiposAusentismosDialogo').hide()");
    }

    public void actualizarClasesAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setClase(seleccionClasesAusentismos);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            } else {
                ausentismoSeleccionado.setClase(seleccionClasesAusentismos);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setClase(seleccionClasesAusentismos);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setClase(seleccionClasesAusentismos);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");
        }

        filtradoslistaClasesAusentismos = null;
        seleccionClasesAusentismos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVClasesAusentismos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVClasesAusentismos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('clasesAusentismosDialogo').hide()");
        //PrimefacesContextUI.actualizar("formularioDialogos:LOVClasesAusentismos");
    }

    public void actualizarCausasAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setCausa(seleccionCausasAusentismos);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            } else {
                ausentismoSeleccionado.setCausa(seleccionCausasAusentismos);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setCausa(seleccionCausasAusentismos);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setCausa(seleccionCausasAusentismos);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");
        }
        filtradoslistaCausasAusentismos = null;
        seleccionCausasAusentismos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVCausasAusentismos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVCausasAusentismos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('causasAusentismosDialogo').hide()");
        //PrimefacesContextUI.actualizar("formularioDialogos:LOVCausasAusentismos");
    }

    public void actualizarPorcentajes() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setPorcentajeindividual(new BigDecimal(seleccionPorcentajes));
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            } else {
                ausentismoSeleccionado.setPorcentajeindividual(new BigDecimal(seleccionPorcentajes));
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setPorcentajeindividual(new BigDecimal(seleccionPorcentajes));
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setPorcentajeindividual(new BigDecimal(seleccionPorcentajes));
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");

        }
        filtradosListaPorcentajes = null;
        seleccionPorcentajes = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVPorcentajes:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVPorcentajes').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('porcentajesDialogo').hide()");
        //PrimefacesContextUI.actualizar("formularioDialogos:LOVPorcentajes");
    }

    public void actualizarIBCS() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setBaseliquidacion(seleccionIBCS.getValor().toBigInteger());
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            } else {
                ausentismoSeleccionado.setBaseliquidacion(seleccionIBCS.getValor().toBigInteger());
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setBaseliquidacion((seleccionIBCS.getValor().toBigInteger()));
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setBaseliquidacion((seleccionIBCS.getValor().toBigInteger()));
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");

        }
        filtradosListaIBCS = null;
        seleccionIBCS = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVIbcs:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVIbcs').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ibcsDialogo').hide()");
        //PrimefacesContextUI.actualizar("formularioDialogos:LOVIbcs");
    }

    public void actualizarEnfermedades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setEnfermedad(seleccionEnfermeadadesProfesionales);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            } else {
                ausentismoSeleccionado.setEnfermedad(seleccionEnfermeadadesProfesionales);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setEnfermedad(seleccionEnfermeadadesProfesionales);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setEnfermedad(seleccionEnfermeadadesProfesionales);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");

        }
        filtradoslistaEnfermeadadesProfesionales = null;
        seleccionEnfermeadadesProfesionales = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVEnfermedades:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVEnfermedades').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('enfermedadesDialogo').hide()");
        //PrimefacesContextUI.actualizar("formularioDialogos:LOVEnfermedades");
    }

    public void actualizarFormas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setFormaliquidacion(seleccionForma);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            } else {
                ausentismoSeleccionado.setFormaliquidacion(seleccionForma);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setFormaliquidacion(seleccionForma);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setFormaliquidacion(seleccionForma);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");

        }
        filtradosListaForma = null;
        seleccionForma = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVFormas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVFormas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('formasDialogo').hide()");
        //PrimefacesContextUI.actualizar("formularioDialogos:LOVFormas");
    }

    public void actualizarAD() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setAccidente(seleccionAccidentes);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            } else {
                ausentismoSeleccionado.setAccidente(seleccionAccidentes);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setAccidente((seleccionAccidentes));
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setAccidente((seleccionAccidentes));
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");

        }
        filtradoslistaAccidentes = null;
        seleccionAccidentes = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVAccidentes:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVAccidentes').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('accidentesDialogo').hide()");
        //PrimefacesContextUI.actualizar("formularioDialogos:LOVAccidentes");
    }

    public void actualizarTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setTercero(seleccionTerceros);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            } else {
                ausentismoSeleccionado.setTercero(seleccionTerceros);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setTercero(seleccionTerceros);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setTercero(seleccionTerceros);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");

        }
        filtradoslistaTerceros = null;
        seleccionTerceros = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVTerceros:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVTerceros').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('tercerosDialogo').hide()");
        //PrimefacesContextUI.actualizar("formularioDialogos:LOVTerceros");
    }

    public void actualizarDiagnosticos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setDiagnosticocategoria(seleccionDiagnosticos);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            } else {
                ausentismoSeleccionado.setDiagnosticocategoria(seleccionDiagnosticos);
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setDiagnosticocategoria(seleccionDiagnosticos);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setDiagnosticocategoria(seleccionDiagnosticos);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");

        }
        filtradoslistaDiagnosticos = null;
        seleccionDiagnosticos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVDiagnosticos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVDiagnosticos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('diagnosticosDialogo').hide()");
        //PrimefacesContextUI.actualizar("formularioDialogos:LOVDiagnosticos");
    }

    public void cancelarCambioDiagnosticos() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaDiagnosticos = null;
        seleccionDiagnosticos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formularioDialogos:LOVDiagnosticos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVDiagnosticos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('diagnosticosDialogo').hide()");
    }

    public void cancelarCambioEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaEmpleados = null;
        seleccionEmpleados = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVEmpleados').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('empleadosDialogo').hide()");
    }

    public void cancelarCambioTiposAusentismos() {
        filtradoslistaTiposAusentismos = null;
        seleccionTiposAusentismos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTiposAusentismos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVTiposAusentismos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('tiposAusentismosDialogo').hide()");
    }

    public void cancelarCambioClasesAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaClasesAusentismos = null;
        seleccionClasesAusentismos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formularioDialogos:LOVClasesAusentismos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVClasesAusentismos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('clasesAusentismosDialogo').hide()");
    }

    public void cancelarCambioCausasAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaCausasAusentismos = null;
        seleccionCausasAusentismos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formularioDialogos:LOVCausasAusentismos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVCausasAusentismos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('causasAusentismosDialogo').hide()");
    }

    public void cancelarCambioPorcentajes() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradosListaPorcentajes = null;
        seleccionPorcentajes = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formularioDialogos:LOVPorcentajes:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVPorcentajes').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('porcentajesDialogo').hide()");
    }

    public void cancelarCambioFormas() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradosListaForma = null;
        seleccionForma = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formularioDialogos:LOVFormas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVFormas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('formasDialogo').hide()");
    }

    public void cancelarCambioIBCS() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradosListaIBCS = null;
        seleccionIBCS = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formularioDialogos:LOVIbcs:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVIbcs').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ibcsDialogo').hide()");
    }

    public void cancelarCambioEnfermedades() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaEnfermeadadesProfesionales = null;
        seleccionEnfermeadadesProfesionales = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formularioDialogos:LOVEnfermedades:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVEnfermedades').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('enfermedadesDialogo').hide()");
    }

    public void cancelarCambioProrrogas() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaProrrogas = null;
        seleccionProrrogas = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formularioDialogos:LOVProrrogas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVProrrogas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('prorrogasDialogo').hide()");
    }

    public void cancelarCambioAD() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaAccidentes = null;
        seleccionAccidentes = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formularioDialogos:LOVAccidentes:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVAccidentes').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('accidentesDialogo').hide()");
    }

    public void cancelarCambioTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaTerceros = null;
        seleccionTerceros = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formularioDialogos:LOVTerceros:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVTerceros').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('tercerosDialogo').hide()");
    }

    //RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listaAusentismos.isEmpty()) {
            if (ausentismoSeleccionado != null) {
                System.out.println("lol 2");
                int result = administrarRastros.obtenerTabla(ausentismoSeleccionado.getSecuencia(), "SOAUSENTISMOS");
                System.out.println("resultado: " + result);
                if (result == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                } else if (result == 2) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
                } else if (result == 3) {
                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                } else if (result == 4) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
                } else if (result == 5) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("SOAUSENTISMOS")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        ausentismoSeleccionado = null;
    }

    //AUTOCOMPLETAR
    public void modificarAusentismos(Soausentismos ausentismo, String confirmarCambio, String valorConfirmar) {
        ausentismoSeleccionado = ausentismo;
        System.out.println("modificarAusentismos");
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");

                    }
                }
            } else {
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");

                    }
                }
            }

            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        } else if (confirmarCambio.equalsIgnoreCase("TIPO")) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.getTipo().setDescripcion(TipoAusentismo);
            } else {
                ausentismoSeleccionado.getTipo().setDescripcion(TipoAusentismo);
            }

            for (int i = 0; i < listaTiposAusentismos.size(); i++) {
                if (listaTiposAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    ausentismoSeleccionado.setTipo(listaTiposAusentismos.get(indiceUnicoElemento));
                } else {
                    ausentismoSeleccionado.setTipo(listaTiposAusentismos.get(indiceUnicoElemento));
                }
                listaTiposAusentismos.clear();
                getListaTiposAusentismos();
                cambiosPagina = false;
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:tiposAusentismosDialogo");
                PrimefacesContextUI.ejecutar("PF('tiposAusentismosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.getTercero().setNombre(Tercero);
            } else {
                ausentismoSeleccionado.getTercero().setNombre(Tercero);
            }

            for (int i = 0; i < listaAusentismos.size(); i++) {
                if (listaAusentismos.get(i).getTercero().getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    ausentismoSeleccionado.setTercero(listaTerceros.get(indiceUnicoElemento));
                } else {
                    ausentismoSeleccionado.setTercero(listaTerceros.get(indiceUnicoElemento));
                }
                listaTerceros.clear();
                getListaTerceros();
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:tercerosDialogo");
                PrimefacesContextUI.ejecutar("PF('tercerosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("CLASE")) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.getClase().setDescripcion(ClaseAusentismo);
            } else {
                ausentismoSeleccionado.getClase().setDescripcion(ClaseAusentismo);
            }

            for (int i = 0; i < listaClasesAusentismos.size(); i++) {
                if (listaClasesAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    ausentismoSeleccionado.setClase(listaClasesAusentismos.get(indiceUnicoElemento));
                } else {
                    ausentismoSeleccionado.setClase(listaClasesAusentismos.get(indiceUnicoElemento));
                }
                listaClasesAusentismos.clear();
                getListaClasesAusentismos();
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:clasesAusentismosDialogo");
                PrimefacesContextUI.ejecutar("PF('clasesAusentismosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("CAUSA")) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.getCausa().setDescripcion(CausaAusentismo);
            } else {
                ausentismoSeleccionado.getCausa().setDescripcion(CausaAusentismo);
            }

            for (int i = 0; i < listaCausasAusentismos.size(); i++) {
                if (listaCausasAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    ausentismoSeleccionado.setCausa(listaCausasAusentismos.get(indiceUnicoElemento));
                } else {
                    ausentismoSeleccionado.setCausa(listaCausasAusentismos.get(indiceUnicoElemento));
                }
                listaCausasAusentismos.clear();
                getListaCausasAusentismos();
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:causasAusentismosDialogo");
                PrimefacesContextUI.ejecutar("PF('causasAusentismosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("PORCENTAJE")) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setPorcentajeindividual(new BigDecimal(Porcentaje));
            } else {
                ausentismoSeleccionado.setPorcentajeindividual(new BigDecimal(Porcentaje));
            }

            for (int i = 0; i < listaPorcentaje.size(); i++) {
                if ((listaPorcentaje.get(i)).toString().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    ausentismoSeleccionado.setPorcentajeindividual(new BigDecimal(listaPorcentaje.get(indiceUnicoElemento)));
                } else {
                    ausentismoSeleccionado.setPorcentajeindividual(new BigDecimal(listaPorcentaje.get(indiceUnicoElemento)));
                }
                listaPorcentaje.clear();
                getListaPorcentaje();
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:porcentajesDialogo");
                PrimefacesContextUI.ejecutar("PF('porcentajesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("BASE")) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setBaseliquidacion(new BigInteger(BaseLiquidacion));
            } else {
                ausentismoSeleccionado.setBaseliquidacion(new BigInteger(BaseLiquidacion));
            }

            for (int i = 0; i < listaIBCS.size(); i++) {
                if ((listaIBCS.get(i)).toString().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    ausentismoSeleccionado.setBaseliquidacion(listaIBCS.get(indiceUnicoElemento).getSecuencia());
                } else {
                    ausentismoSeleccionado.setBaseliquidacion(listaIBCS.get(indiceUnicoElemento).getSecuencia());
                }
                listaIBCS.clear();
                getListaIBCS();
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:ibcsDialogo");
                PrimefacesContextUI.ejecutar("PF('ibcsDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("FORMA")) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.setFormaliquidacion(Forma);
            } else {
                ausentismoSeleccionado.setFormaliquidacion(Forma);
            }

            for (int i = 0; i < listaForma.size(); i++) {
                if ((listaForma.get(i)).startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    ausentismoSeleccionado.setFormaliquidacion(listaForma.get(indiceUnicoElemento));
                } else {
                    ausentismoSeleccionado.setFormaliquidacion(listaForma.get(indiceUnicoElemento));
                }
                listaForma.clear();
                getListaForma();
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:formasDialogo");
                PrimefacesContextUI.ejecutar("PF('formasDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("AD")) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.getAccidente().setDescripcioncaso(AD);
            } else {
                ausentismoSeleccionado.getAccidente().setDescripcioncaso(AD);
            }

            for (int i = 0; i < listaAccidentes.size(); i++) {
                if (listaAccidentes.get(i).getDescripcioncaso().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    ausentismoSeleccionado.setAccidente(listaAccidentes.get(indiceUnicoElemento));
                } else {
                    ausentismoSeleccionado.setAccidente(listaAccidentes.get(indiceUnicoElemento));
                }
                listaAccidentes.clear();
                getListaAccidentes();
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:accidentesDialogo");
                PrimefacesContextUI.ejecutar("PF('accidentesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("ENFERMEDADES")) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.getEnfermedad().getCategoria().setDescripcion(Enfermedad);
            } else {
                ausentismoSeleccionado.getEnfermedad().getCategoria().setDescripcion(Enfermedad);
            }

            for (int i = 0; i < listaEnfermeadadesProfesionales.size(); i++) {
                if (listaEnfermeadadesProfesionales.get(i).getCategoria().getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    ausentismoSeleccionado.setEnfermedad(listaEnfermeadadesProfesionales.get(indiceUnicoElemento));
                } else {
                    ausentismoSeleccionado.setEnfermedad(listaEnfermeadadesProfesionales.get(indiceUnicoElemento));
                }
                listaEnfermeadadesProfesionales.clear();
                getListaEnfermeadadesProfesionales();
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:enfermedadesDialogo");
                PrimefacesContextUI.ejecutar("PF('enfermedadesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("DIAGNOSTICO")) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.getDiagnosticocategoria().setCodigo(Diagnostico);
            } else {
                ausentismoSeleccionado.getDiagnosticocategoria().setCodigo(Diagnostico);
            }

            for (int i = 0; i < listaDiagnosticos.size(); i++) {
                if (listaDiagnosticos.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    ausentismoSeleccionado.setDiagnosticocategoria(listaDiagnosticos.get(indiceUnicoElemento));
                } else {
                    ausentismoSeleccionado.setDiagnosticocategoria(listaDiagnosticos.get(indiceUnicoElemento));
                }
                listaDiagnosticos.clear();
                getListaDiagnosticos();
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:diagnosticosDialogo");
                PrimefacesContextUI.ejecutar("PF('diagnosticosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("PRORROGA")) {
            if (tipoLista == 0) {
                ausentismoSeleccionado.getProrroga().setProrrogaAusentismo(Prorroga);
            } else {
                ausentismoSeleccionado.getProrroga().setProrrogaAusentismo(Prorroga);
            }

            for (int i = 0; i < listaProrrogas.size(); i++) {
                if (listaProrrogas.get(i).getProrrogaAusentismo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    ausentismoSeleccionado.setProrroga(listaProrrogas.get(indiceUnicoElemento));
                } else {
                    ausentismoSeleccionado.setProrroga(listaProrrogas.get(indiceUnicoElemento));
                }
                listaProrrogas.clear();
                getListaProrrogas();
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:prorrogasDialogo");
                PrimefacesContextUI.ejecutar("PF('prorrogasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");

                    }
                }
            } else {
                if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {

                    if (listaAusentismosModificar.isEmpty()) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                        listaAusentismosModificar.add(ausentismoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");

                    }
                }
            }
        }
        PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
    }

    //BORRAR Novedades
    public void borrarAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (ausentismoSeleccionado == null) {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        } else {
            if (ausentismoSeleccionado != null) {
                cambiosPagina = false;
                if (!listaAusentismosModificar.isEmpty() && listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    int modIndex = listaAusentismosModificar.indexOf(ausentismoSeleccionado);
                    listaAusentismosModificar.remove(modIndex);
                    listaAusentismosBorrar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosCrear.isEmpty() && listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                    int crearIndex = listaAusentismosCrear.indexOf(ausentismoSeleccionado);
                    listaAusentismosCrear.remove(crearIndex);
                } else {
                    listaAusentismosBorrar.add(ausentismoSeleccionado);
                }
                listaAusentismos.remove(ausentismoSeleccionado);
                if (tipoLista == 1) {
                    filtradosListaAusentismos.remove(ausentismoSeleccionado);
                    System.out.println("Realizado");
                }

                PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
                PrimefacesContextUI.actualizar("form:ACEPTAR");
                ausentismoSeleccionado = null;
                ausentismoSeleccionado = null;

                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            }
        }
    }

//    public void periodoAusentismo() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
//        String celda = map.get("celda"); // name attribute of node 
//        String registro = map.get("registro"); // type attribute of node 
//        int indice = Integer.parseInt(registro);
//        int columna = Integer.parseInt(celda);
//        ausentismoSeleccionado = listaAusentismos.get(indice);
//        cambiarIndice(ausentismoSeleccionado, columna);
//    }
    //Ubicacion Celda Indice Abajo. //Van los que no son NOT NULL.

    public void cambiarIndice(Soausentismos ausentismo, int celda) {
        System.out.println("Cambiar Indice");
        if (permitirIndex == true) {
            ausentismoSeleccionado = ausentismo;
            cualCelda = celda;
            if (tipoLista == 0) {
                secuenciaCausa = ausentismoSeleccionado.getCausa().getSecuencia();
                secuenciaAusentismo = ausentismoSeleccionado.getSecuencia();
                ausentismoSeleccionado.getSecuencia();
                Relacion = administrarAusentismos.mostrarRelacion(ausentismoSeleccionado.getSecuencia());
                if (cualCelda == 0) {
                    habilitarBotonLov();
                    TipoAusentismo = ausentismoSeleccionado.getTipo().getDescripcion();
                } else if (cualCelda == 1) {
                    habilitarBotonLov();
                    ClaseAusentismo = ausentismoSeleccionado.getClaseausentismo();
                } else if (cualCelda == 2) {
                    habilitarBotonLov();
                    CausaAusentismo = ausentismoSeleccionado.getCausa().getDescripcion();
                } else if (cualCelda == 3) {
                    deshabilitarBotonLov();
                    Dias = ausentismoSeleccionado.getDias().toString();
                } else if (cualCelda == 4) {
                    deshabilitarBotonLov();
                    Horas = ausentismoSeleccionado.getHoras().toString();
                } else if (cualCelda == 6) {
                    deshabilitarBotonLov();
                    Fechafinaus = ausentismoSeleccionado.getFechafinaus().toString();
                } else if (cualCelda == 7) {
                    deshabilitarBotonLov();
                    Fechaexpedicion = ausentismoSeleccionado.getFechaexpedicion().toString();
                } else if (cualCelda == 8) {
                    deshabilitarBotonLov();
                    InicioPago = ausentismoSeleccionado.getFechainipago().toString();
                } else if (cualCelda == 9) {
                    deshabilitarBotonLov();
                    FinPago = ausentismoSeleccionado.getFechafinpago().toString();
                } else if (cualCelda == 10) {
                    habilitarBotonLov();
                    Porcentaje = ausentismoSeleccionado.getPorcentajeindividual().toString();
                } else if (cualCelda == 11) {
                    habilitarBotonLov();
                    BaseLiquidacion = ausentismoSeleccionado.getBaseliquidacion().toString();
                } else if (cualCelda == 12) {
                    habilitarBotonLov();
                    Forma = ausentismoSeleccionado.getFormaliquidacion();
                } else if (cualCelda == 13) {
                    habilitarBotonLov();
                    AD = ausentismoSeleccionado.getAccidente().getDescripcioncaso();
                } else if (cualCelda == 14) {
                    habilitarBotonLov();
                    Enfermedad = ausentismoSeleccionado.getEnfermedad().getCategoria().getDescripcion();
                } else if (cualCelda == 15) {
                    deshabilitarBotonLov();
                    NumeroCertificado = ausentismoSeleccionado.getNumerocertificado();
                } else if (cualCelda == 16) {
                    habilitarBotonLov();
                    Diagnostico = ausentismoSeleccionado.getDiagnosticocategoria().getCodigo();
                } else if (cualCelda == 17) {
                    habilitarBotonLov();
                    Prorrogas = ausentismoSeleccionado.getProrroga().getProrrogaAusentismo();
                } else if (cualCelda == 18) {
                    deshabilitarBotonLov();
                    Relacionn = ausentismoSeleccionado.getRelacion();
                } else if (cualCelda == 19) {
                    habilitarBotonLov();
                    Tercero = ausentismoSeleccionado.getTercero().getNombre();
                } else if (cualCelda == 20) {
                    deshabilitarBotonLov();
                    Observacion = ausentismoSeleccionado.getObservaciones();
                }
            } else {
                secuenciaCausa = ausentismoSeleccionado.getCausa().getSecuencia();
                secuenciaAusentismo = ausentismoSeleccionado.getSecuencia();
                ausentismoSeleccionado.getSecuencia();
                Relacion = administrarAusentismos.mostrarRelacion(ausentismoSeleccionado.getSecuencia());
                if (cualCelda == 3) {
                    Dias = ausentismoSeleccionado.getDias().toString();
                } else if (cualCelda == 4) {
                    Horas = ausentismoSeleccionado.getHoras().toString();
                } else if (cualCelda == 6) {
                    Fechafinaus = ausentismoSeleccionado.getFechafinaus().toString();
                } else if (cualCelda == 7) {
                    Fechaexpedicion = ausentismoSeleccionado.getFechaexpedicion().toString();
                } else if (cualCelda == 8) {
                    InicioPago = ausentismoSeleccionado.getFechainipago().toString();
                } else if (cualCelda == 9) {
                    FinPago = ausentismoSeleccionado.getFechafinpago().toString();
                } else if (cualCelda == 10) {
                    Porcentaje = ausentismoSeleccionado.getPorcentajeindividual().toString();
                } else if (cualCelda == 11) {
                    BaseLiquidacion = ausentismoSeleccionado.getBaseliquidacion().toString();
                } else if (cualCelda == 12) {
                    Forma = ausentismoSeleccionado.getFormaliquidacion();
                } else if (cualCelda == 13) {
                    AD = ausentismoSeleccionado.getAccidente().getDescripcioncaso();
                } else if (cualCelda == 14) {
                    Enfermedad = ausentismoSeleccionado.getEnfermedad().getCategoria().getDescripcion();
                } else if (cualCelda == 15) {
                    NumeroCertificado = ausentismoSeleccionado.getNumerocertificado();
                } else if (cualCelda == 16) {
                    Diagnostico = ausentismoSeleccionado.getDiagnosticocategoria().getCodigo();
                } else if (cualCelda == 17) {
                    Prorrogas = ausentismoSeleccionado.getProrroga().getProrrogaAusentismo();
                } else if (cualCelda == 18) {
                    Relacionn = ausentismoSeleccionado.getRelacion();
                } else if (cualCelda == 20) {
                    Tercero = ausentismoSeleccionado.getTercero().getNombre();
                } else if (cualCelda == 21) {
                    Observacion = ausentismoSeleccionado.getObservaciones();
                }
            }
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAusentismosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "AusentismosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAusentismosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "AusentismosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

//MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (ausentismoSeleccionado != null) {
            if (tipoLista == 0) {
                editarAusentismos = ausentismoSeleccionado;
            }
            if (tipoLista == 1) {
                editarAusentismos = ausentismoSeleccionado;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarTiposAusentismos");
                PrimefacesContextUI.ejecutar("PF('editarTiposAusentismos').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarClasesAusentismos");
                PrimefacesContextUI.ejecutar("PF('editarClasesAusentismos').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCausasAusentismos");
                PrimefacesContextUI.ejecutar("PF('editarCausasAusentismos').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarDiasD");
                PrimefacesContextUI.ejecutar("PF('editarDiasD').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarHorasD");
                PrimefacesContextUI.ejecutar("PF('editarHorasD').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFIausencias");
                PrimefacesContextUI.ejecutar("PF('editarFIausencias').show()");
            } else if (cualCelda == 6) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFFausencias");
                PrimefacesContextUI.ejecutar("PF('editarFFausencias').show()");
            } else if (cualCelda == 7) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFexpediciones");
                PrimefacesContextUI.ejecutar("PF('editarFexpediciones').show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFIpagos");
                PrimefacesContextUI.ejecutar("PF('editarFIpagos').show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFFpagos");
                PrimefacesContextUI.ejecutar("PF('editarFFpagos').show()");
                cualCelda = -1;
            } else if (cualCelda == 10) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarPorcentajes");
                PrimefacesContextUI.ejecutar("PF('editarPorcentajes').show()");
                cualCelda = -1;
            } else if (cualCelda == 11) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarBliquidaciones");
                PrimefacesContextUI.ejecutar("PF('editarBliquidaciones').show()");
                cualCelda = -1;
            } else if (cualCelda == 12) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFliquidaciones");
                PrimefacesContextUI.ejecutar("PF('editarFliquidaciones').show()");
                cualCelda = -1;
            } else if (cualCelda == 13) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarAccidentes");
                PrimefacesContextUI.ejecutar("PF('editarAccidentes').show()");
                cualCelda = -1;
            } else if (cualCelda == 14) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarEnfermedades");
                PrimefacesContextUI.ejecutar("PF('editarEnfermedades').show()");
                cualCelda = -1;
            } else if (cualCelda == 15) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarNcertificados");
                PrimefacesContextUI.ejecutar("PF('editarNcertificados').show()");
                cualCelda = -1;
            } else if (cualCelda == 16) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarDiagnosticos");
                PrimefacesContextUI.ejecutar("PF('editarDiagnosticos').show()");
                cualCelda = -1;
            } else if (cualCelda == 17) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarProrrogas");
                PrimefacesContextUI.ejecutar("PF('editarProrrogas').show()");
                cualCelda = -1;
            } else if (cualCelda == 18) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarRelacion");
                PrimefacesContextUI.ejecutar("PF('editarRelacion').show()");
                cualCelda = -1;
            } else if (cualCelda == 19) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarTerceros");
                PrimefacesContextUI.ejecutar("PF('editarTerceros').show()");
                cualCelda = -1;
            } else if (cualCelda == 20) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarObservacionesD");
                PrimefacesContextUI.ejecutar("PF('editarObservacionesD').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("seleccionarRegistro').show()");
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPO")) {
            if (tipoNuevo == 1) {
                TipoAusentismo = nuevoAusentismo.getTipo().getDescripcion();
            } else if (tipoNuevo == 2) {
                TipoAusentismo = duplicarAusentismo.getTipo().getDescripcion();
            }
        } else if (Campo.equals("CLASE")) {
            if (tipoNuevo == 1) {
                ClaseAusentismo = nuevoAusentismo.getClase().getDescripcion();
            } else if (tipoNuevo == 2) {
                ClaseAusentismo = duplicarAusentismo.getClase().getDescripcion();
            }
        } else if (Campo.equals("CAUSA")) {
            if (tipoNuevo == 1) {
                CausaAusentismo = nuevoAusentismo.getCausa().getDescripcion();
            } else if (tipoNuevo == 2) {
                CausaAusentismo = duplicarAusentismo.getCausa().getDescripcion();
            }
        } else if (Campo.equals("PORCENTAJE")) {
            if (tipoNuevo == 1) {
                Porcentaje = nuevoAusentismo.getPorcentajeindividual().toString();
            } else if (tipoNuevo == 2) {
                Porcentaje = duplicarAusentismo.getPorcentajeindividual().toString();
            }
        } else if (Campo.equals("BASE")) {
            if (tipoNuevo == 1) {
                BaseLiquidacion = nuevoAusentismo.getBaseliquidacion().toString();
            } else if (tipoNuevo == 2) {
                BaseLiquidacion = duplicarAusentismo.getBaseliquidacion().toString();
            }
        } else if (Campo.equals("FORMA")) {
            if (tipoNuevo == 1) {
                Forma = nuevoAusentismo.getFormaliquidacion();
            } else if (tipoNuevo == 2) {
                Forma = duplicarAusentismo.getFormaliquidacion();
            }
        } else if (Campo.equals("AD")) {
            if (tipoNuevo == 1) {
                AD = nuevoAusentismo.getAccidente().getDescripcioncaso();
            } else if (tipoNuevo == 2) {
                AD = duplicarAusentismo.getAccidente().getDescripcioncaso();
            }
        } else if (Campo.equals("ENFERMEDADES")) {
            if (tipoNuevo == 1) {
                Enfermedad = nuevoAusentismo.getEnfermedad().getCategoria().getDescripcion();
            } else if (tipoNuevo == 2) {
                Enfermedad = duplicarAusentismo.getEnfermedad().getCategoria().getDescripcion();
            }
        } else if (Campo.equals("DIAGNOSTICO")) {
            if (tipoNuevo == 1) {
                Diagnostico = nuevoAusentismo.getDiagnosticocategoria().getCodigo();
            } else if (tipoNuevo == 2) {
                Diagnostico = duplicarAusentismo.getDiagnosticocategoria().getCodigo();
            }
        } else if (Campo.equals("PRORROGA")) {
            if (tipoNuevo == 1) {
                Prorroga = nuevoAusentismo.getProrroga().getProrrogaAusentismo();
            } else if (tipoNuevo == 2) {
                Prorroga = duplicarAusentismo.getProrroga().getProrrogaAusentismo();
            }
        } else if (Campo.equals("TERCERO")) {
            if (tipoNuevo == 1) {
                Tercero = nuevoAusentismo.getTercero().getNombre();
            } else if (tipoNuevo == 2) {
                Tercero = duplicarAusentismo.getTercero().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPO")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getTipo().setDescripcion(TipoAusentismo);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getTipo().setDescripcion(TipoAusentismo);
            }
            for (int i = 0; i < listaTiposAusentismos.size(); i++) {
                if (listaTiposAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setTipo(listaTiposAusentismos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoTipo");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setTipo(listaTiposAusentismos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTipo");
                }
                listaTiposAusentismos.clear();
                getListaTiposAusentismos();
            } else {
                PrimefacesContextUI.actualizar("form:tiposAusentismosDialogo");
                PrimefacesContextUI.ejecutar("PF('tiposAusentismosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoTipo");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTipo");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CLASE")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getClase().setDescripcion(ClaseAusentismo);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getClase().setDescripcion(ClaseAusentismo);
            }

            for (int i = 0; i < listaClasesAusentismos.size(); i++) {
                if (listaClasesAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setClase(listaClasesAusentismos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaClase");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setClase(listaClasesAusentismos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarClase");
                }
                listaClasesAusentismos.clear();
                getListaClasesAusentismos();
            } else {
                PrimefacesContextUI.actualizar("form:clasesAusentismosDialogo");
                PrimefacesContextUI.ejecutar("PF('clasesAusentismosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaClase");

                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarClase");

                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CAUSA")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getCausa().setDescripcion(CausaAusentismo);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getCausa().setDescripcion(CausaAusentismo);
            }

            for (int i = 0; i < listaCausasAusentismos.size(); i++) {
                if (listaCausasAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setCausa(listaCausasAusentismos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaCausa");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setCausa(listaCausasAusentismos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarCausa");
                }
                listaCausasAusentismos.clear();
                getListaCausasAusentismos();
            } else {
                PrimefacesContextUI.actualizar("form:causasAusentismosDialogo");
                PrimefacesContextUI.ejecutar("PF('causasAusentismosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaCausa");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarCausa");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("PORCENTAJE")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.setPorcentajeindividual(new BigDecimal(Porcentaje));
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.setPorcentajeindividual(new BigDecimal(Porcentaje));
            }

            for (int i = 0; i < listaPorcentaje.size(); i++) {
                if (listaPorcentaje.get(i).startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setPorcentajeindividual(new BigDecimal(listaPorcentaje.get(indiceUnicoElemento)));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarPorcentaje");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setPorcentajeindividual(new BigDecimal(listaPorcentaje.get(indiceUnicoElemento)));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarPorcentaje");
                }
                listaPorcentaje.clear();
                getListaPorcentaje();
            } else {
                PrimefacesContextUI.actualizar("form:porcentajesDialogo");
                PrimefacesContextUI.ejecutar("PF('porcentajesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarPorcentaje");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarPorcentaje");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("BASE")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.setBaseliquidacion(new BigInteger(BaseLiquidacion));
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.setBaseliquidacion(new BigInteger(BaseLiquidacion));
            }

            for (int i = 0; i < listaIBCS.size(); i++) {
                if (listaIBCS.get(i).getValor().toString().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setBaseliquidacion(listaIBCS.get(indiceUnicoElemento).getValor().toBigInteger());
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarIBCS");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setBaseliquidacion(listaIBCS.get(indiceUnicoElemento).getValor().toBigInteger());
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarIBCS");
                }
                listaIBCS.clear();
                getListaIBCS();
            } else {
                PrimefacesContextUI.actualizar("form:ibcsDialogo");
                PrimefacesContextUI.ejecutar("PF('ibcsDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarIBCS");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarIBCS");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("FORMA")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.setFormaliquidacion(Forma);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.setFormaliquidacion(Forma);
            }

            for (int i = 0; i < listaForma.size(); i++) {
                if (listaForma.get(i).startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setFormaliquidacion(listaForma.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaForma");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setFormaliquidacion(listaForma.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarForma");
                }
                listaForma.clear();
                getListaForma();
            } else {
                PrimefacesContextUI.actualizar("form:formasDialogo");
                PrimefacesContextUI.ejecutar("PF('formasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaForma");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarForma");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("AD")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getAccidente().setDescripcioncaso(AD);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getAccidente().setDescripcioncaso(AD);
            }

            for (int i = 0; i < listaAccidentes.size(); i++) {
                if (listaAccidentes.get(i).getDescripcioncaso().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setAccidente(listaAccidentes.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarAccidente");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setAccidente(listaAccidentes.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarAccidente");
                }
                listaAccidentes.clear();
                getListaAccidentes();
            } else {
                PrimefacesContextUI.actualizar("form:accidentesDialogo");
                PrimefacesContextUI.ejecutar("PF('accidentesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarAccidente");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarAccidente");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("ENFERMEDADES")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getEnfermedad().getCategoria().setDescripcion(Enfermedad);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getEnfermedad().getCategoria().setDescripcion(Enfermedad);
            }

            for (int i = 0; i < listaEnfermeadadesProfesionales.size(); i++) {
                if (listaEnfermeadadesProfesionales.get(i).getCategoria().getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setEnfermedad(listaEnfermeadadesProfesionales.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaEnfermedad");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setEnfermedad(listaEnfermeadadesProfesionales.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarEnfermedad");
                }
                listaAccidentes.clear();
                getListaAccidentes();
            } else {
                PrimefacesContextUI.actualizar("form:enfermedadesDialogo");
                PrimefacesContextUI.ejecutar("PF('enfermedadesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaEnfermedad");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarEnfermedad");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("DIAGNOSTICO")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getDiagnosticocategoria().setDescripcion(Diagnostico);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getDiagnosticocategoria().setDescripcion(Diagnostico);
            }

            for (int i = 0; i < listaDiagnosticos.size(); i++) {
                if (listaDiagnosticos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setDiagnosticocategoria(listaDiagnosticos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarDiagnostico");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setDiagnosticocategoria(listaDiagnosticos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarDiagnostico");
                }
                listaDiagnosticos.clear();
                getListaDiagnosticos();
            } else {
                PrimefacesContextUI.actualizar("form:diagnosticosDialogo");
                PrimefacesContextUI.ejecutar("PF('diagnosticosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarDiagnostico");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarDiagnostico");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("PRORROGA")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getProrroga().setProrrogaAusentismo(Prorroga);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getProrroga().setProrrogaAusentismo(Prorroga);
            }

            for (int i = 0; i < listaProrrogas.size(); i++) {
                if (listaProrrogas.get(i).getProrrogaAusentismo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setProrroga(listaProrrogas.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaProrroga");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setProrroga(listaProrrogas.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarProrroga");
                }
                listaProrrogas.clear();
                getListaProrrogas();
            } else {
                PrimefacesContextUI.actualizar("form:prorrogasDialogo");
                PrimefacesContextUI.ejecutar("PF('prorrogasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaProrroga");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarProrroga");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getTercero().setNombre(Tercero);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getTercero().setNombre(Tercero);
            }

            for (int i = 0; i < listaTerceros.size(); i++) {
                if (listaTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setTercero(listaTerceros.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoTercero");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setTercero(listaTerceros.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTercero");
                }
                listaTerceros.clear();
                getListaTerceros();
            } else {
                PrimefacesContextUI.actualizar("form:tercerosDialogo");
                PrimefacesContextUI.ejecutar("PF('tercerosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoTercero");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTercero");
                }
            }
        }
    }

    //DUPLICAR ENCARGATURA
    public void duplicarA() {
        if (ausentismoSeleccionado != null) {
            duplicarAusentismo = new Soausentismos();

            if (tipoLista == 0) {
                duplicarAusentismo.setEmpleado(seleccionMostrar);
                duplicarAusentismo.setTipo(ausentismoSeleccionado.getTipo());
                duplicarAusentismo.setClase(ausentismoSeleccionado.getClase());
                duplicarAusentismo.setCausa(ausentismoSeleccionado.getCausa());
                duplicarAusentismo.setDias(ausentismoSeleccionado.getDias());
                duplicarAusentismo.setHoras(ausentismoSeleccionado.getHoras());
                duplicarAusentismo.setFecha(ausentismoSeleccionado.getFecha());
                duplicarAusentismo.setFechafinaus(ausentismoSeleccionado.getFechafinaus());
                duplicarAusentismo.setFechaexpedicion(ausentismoSeleccionado.getFechaexpedicion());
                duplicarAusentismo.setFechainipago(ausentismoSeleccionado.getFechainipago());
                duplicarAusentismo.setFechafinpago(ausentismoSeleccionado.getFechafinpago());
                duplicarAusentismo.setPorcentajeindividual(ausentismoSeleccionado.getPorcentajeindividual());
                duplicarAusentismo.setBaseliquidacion(ausentismoSeleccionado.getBaseliquidacion());
                duplicarAusentismo.setFormaliquidacion(ausentismoSeleccionado.getFormaliquidacion());
                duplicarAusentismo.setAccidente(ausentismoSeleccionado.getAccidente());
                duplicarAusentismo.setEnfermedad(ausentismoSeleccionado.getEnfermedad());
                duplicarAusentismo.setNumerocertificado(ausentismoSeleccionado.getNumerocertificado());
                duplicarAusentismo.setDiagnosticocategoria(ausentismoSeleccionado.getDiagnosticocategoria());
                duplicarAusentismo.setProrroga(ausentismoSeleccionado.getProrroga());
                duplicarAusentismo.setRelacion(ausentismoSeleccionado.getRelacion());
                duplicarAusentismo.setRelacionada(ausentismoSeleccionado.getRelacionada());
                duplicarAusentismo.setTercero(ausentismoSeleccionado.getTercero());
                duplicarAusentismo.setObservaciones(ausentismoSeleccionado.getObservaciones());
            }
            if (tipoLista == 1) {
                duplicarAusentismo.setEmpleado(seleccionMostrar);
                duplicarAusentismo.setTipo(ausentismoSeleccionado.getTipo());
                duplicarAusentismo.setClase(ausentismoSeleccionado.getClase());
                duplicarAusentismo.setCausa(ausentismoSeleccionado.getCausa());
                duplicarAusentismo.setDias(ausentismoSeleccionado.getDias());
                duplicarAusentismo.setHoras(ausentismoSeleccionado.getHoras());
                duplicarAusentismo.setFecha(ausentismoSeleccionado.getFecha());
                duplicarAusentismo.setFechafinaus(ausentismoSeleccionado.getFechafinaus());
                duplicarAusentismo.setFechaexpedicion(ausentismoSeleccionado.getFechaexpedicion());
                duplicarAusentismo.setFechainipago(ausentismoSeleccionado.getFechainipago());
                duplicarAusentismo.setFechafinpago(ausentismoSeleccionado.getFechafinpago());
                duplicarAusentismo.setPorcentajeindividual(ausentismoSeleccionado.getPorcentajeindividual());
                duplicarAusentismo.setBaseliquidacion(ausentismoSeleccionado.getBaseliquidacion());
                duplicarAusentismo.setFormaliquidacion(ausentismoSeleccionado.getFormaliquidacion());
                duplicarAusentismo.setAccidente(ausentismoSeleccionado.getAccidente());
                duplicarAusentismo.setEnfermedad(ausentismoSeleccionado.getEnfermedad());
                duplicarAusentismo.setNumerocertificado(ausentismoSeleccionado.getNumerocertificado());
                duplicarAusentismo.setDiagnosticocategoria(ausentismoSeleccionado.getDiagnosticocategoria());
                duplicarAusentismo.setProrroga(ausentismoSeleccionado.getProrroga());
                duplicarAusentismo.setRelacion(ausentismoSeleccionado.getRelacion());
                duplicarAusentismo.setRelacionada(ausentismoSeleccionado.getRelacionada());
                duplicarAusentismo.setTercero(ausentismoSeleccionado.getTercero());
                duplicarAusentismo.setObservaciones(ausentismoSeleccionado.getObservaciones());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");
            PrimefacesContextUI.ejecutar("PF('DuplicarAusentismoEmpleado').show()");
        } else {
            RequestContext.getCurrentInstance().execute("seleccionarRegistro').show()");
        }
    }

    //LIMPIAR NUEVO AUSENTISMO
    public void limpiarNuevoAusentismo() {
        FacesContext c = FacesContext.getCurrentInstance();
        nuevoAusentismo = new Soausentismos();
        nuevoAusentismo.setTipo(new Tiposausentismos());
        nuevoAusentismo.setCausa(new Causasausentismos());
        nuevoAusentismo.setClase(new Clasesausentismos());
        System.out.println("Entro a Bandera B. 1");
    }

//Salir NUEVO AUSENTISMO
    public void salirNuevoAusentismo() {
        FacesContext c = FacesContext.getCurrentInstance();
        nuevoAusentismo = new Soausentismos();
        nuevoAusentismo.setTipo(new Tiposausentismos());
        nuevoAusentismo.setCausa(new Causasausentismos());
        nuevoAusentismo.setClase(new Clasesausentismos());
        ausentismoSeleccionado = null;
        ausentismoSeleccionado = null;
        System.out.println("Entro a Bandera B. 1");
        botonLimpiar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:limpiar");
        botonLimpiar.setStyle("position: absolute; left: 50px; top: 400px;");
        botonAgregar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:agregarNA");
        botonAgregar.setStyle("position: absolute; left: 350px; top: 400px;");
        botonCancelar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:cancelarNA");
        botonCancelar.setStyle("position: absolute; left: 450px; top: 400px;");
        altoDialogoNuevo = "430";
        banderaBotones = 0;
        colapsado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:NuevoAusentismoEmpleado");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        System.out.println("TipoLista= " + tipoLista);
        if (bandera == 0) {
            altoTabla = "125";
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            ATipo = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ATipo");
            ATipo.setFilterStyle("width: 85%");
            AClase = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AClase");
            AClase.setFilterStyle("width: 85%");
            ACausa = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ACausa");
            ACausa.setFilterStyle("width: 85%");
            ADias = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ADias");
            ADias.setFilterStyle("width: 85%");
            AHoras = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AHoras");
            AHoras.setFilterStyle("width: 85%");
            AFecha = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFecha");
            AFecha.setFilterStyle("width: 85%");
            AFechaFinaus = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaFinaus");
            AFechaFinaus.setFilterStyle("width: 85%");
            AFechaExpedicion = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaExpedicion");
            AFechaExpedicion.setFilterStyle("width: 85%");
            AFechaInipago = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaInipago");
            AFechaInipago.setFilterStyle("width: 85%");
            AFechaFinpago = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaFinpago");
            AFechaFinpago.setFilterStyle("width: 85%");
            APorcentaje = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:APorcentaje");
            APorcentaje.setFilterStyle("width: 85%");
            ABase = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ABase");
            ABase.setFilterStyle("width: 85%");
            AForma = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AForma");
            AForma.setFilterStyle("width: 85%");
            ADescripcionCaso = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ADescripcionCaso");
            ADescripcionCaso.setFilterStyle("width: 85%");
            AEnfermedad = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AEnfermedad");
            AEnfermedad.setFilterStyle("width: 85%");
            ANumero = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ANumero");
            ANumero.setFilterStyle("width: 85%");
            ADiagnostico = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ADiagnostico");
            ADiagnostico.setFilterStyle("width: 85%");
            AProrroga = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AProrroga");
            AProrroga.setFilterStyle("width: 85%");
            ANumero = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ANumero");
            ANumero.setFilterStyle("width: 85%");
            ARelacion = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ARelacion");
            ARelacion.setFilterStyle("width: 85%");
            ARelacionada = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ARelacionada");
            ARelacionada.setFilterStyle("width: 85%");
            ATercero = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ATercero");
            ATercero.setFilterStyle("width: 85%");
            AObservaciones = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AObservaciones");
            AObservaciones.setFilterStyle("width: 85%");
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altoTabla = "145";
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            cerrarFiltrado();
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (ausentismoSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                habilitarBotonLov();
                PrimefacesContextUI.actualizar("formularioDialogos:tiposAusentismosDialogo");
                PrimefacesContextUI.ejecutar("PF('tiposAusentismosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 1) {
                habilitarBotonLov();
                PrimefacesContextUI.actualizar("formularioDialogos:clasesAusentismosDialogo");
                PrimefacesContextUI.ejecutar("PF('clasesAusentismosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 2) {
                habilitarBotonLov();
                PrimefacesContextUI.actualizar("formularioDialogos:causasAusentismosDialogo");
                PrimefacesContextUI.ejecutar("PF('causasAusentismosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 10) {
                habilitarBotonLov();
                PrimefacesContextUI.actualizar("formularioDialogos:porcentajesDialogo");
                PrimefacesContextUI.ejecutar("PF('porcentajesDialogo').show()");
            } else if (cualCelda == 11) {
                habilitarBotonLov();
                PrimefacesContextUI.actualizar("formularioDialogos:ibcsDialogo");
                PrimefacesContextUI.ejecutar("PF('ibcsDialogo').show()");
            } else if (cualCelda == 12) {
                habilitarBotonLov();
                PrimefacesContextUI.actualizar("formularioDialogos:formasDialogo");
                PrimefacesContextUI.ejecutar("PF('formasDialogo').show()");
            } else if (cualCelda == 13) {
                habilitarBotonLov();
                PrimefacesContextUI.actualizar("formularioDialogos:accidentesDialogo");
                PrimefacesContextUI.ejecutar("PF('accidentesDialogo').show()");
            } else if (cualCelda == 14) {
                habilitarBotonLov();
                PrimefacesContextUI.actualizar("formularioDialogos:enfermedadesDialogo");
                PrimefacesContextUI.ejecutar("PF('enfermedadesDialogo').show()");
            } else if (cualCelda == 16) {
                habilitarBotonLov();
                PrimefacesContextUI.actualizar("formularioDialogos:diagnosticosDialogo");
                PrimefacesContextUI.ejecutar("PF('diagnosticosDialogo').show()");
            } else if (cualCelda == 17) {
                habilitarBotonLov();
                PrimefacesContextUI.actualizar("formularioDialogos:prorrogasDialogo");
                PrimefacesContextUI.ejecutar("PF('prorrogasDialogo').show()");
            } else if (cualCelda == 19) {
                habilitarBotonLov();
                PrimefacesContextUI.actualizar("formularioDialogos:tercerosDialogo");
                PrimefacesContextUI.ejecutar("PF('tercerosDialogo').show()");
            }
        }
    }

    public void experimento(Date aux) {
        System.out.println("experimento Valor experimento : " + aux);
        System.out.println("experimento Nuevo Ausentismo Fecha Inicial: " + nuevoAusentismo.getFecha());
    }

    //CREAR NOVEDADES
    public void agregarNuevoAusentismo() {
        int pasa = 0;
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("Nuevo Ausentismo Fecha Inicial: " + nuevoAusentismo.getFecha());

        if (nuevoAusentismo.getFecha() == null) {
            System.out.println("Entro a Fecha ");
            mensajeValidacion = mensajeValidacion + " * Fecha Inicio Ausentismo\n";
            pasa++;
        }

        if (nuevoAusentismo.getTipo().getDescripcion().equals(" ") || nuevoAusentismo.getTipo().getDescripcion().equals("")) {
            System.out.println("Entro a Tipo");
            mensajeValidacion = mensajeValidacion + " * Tipo\n";
            pasa++;
        }

        if (nuevoAusentismo.getCausa().getDescripcion().equals("")) {
            System.out.println("Entro a Causa");
            mensajeValidacion = mensajeValidacion + " * Causa\n";
            pasa++;
        }
        if (nuevoAusentismo.getClase().getDescripcion().equals("")) {
            System.out.println("Entro a Clase");
            mensajeValidacion = mensajeValidacion + " * Clase\n";
            pasa++;
        }

        if (nuevoAusentismo.getFormaliquidacion() == null) {
            System.out.println("Entro a Forma");
            mensajeValidacion = mensajeValidacion + " * Forma Liquidación\n";
            pasa++;
        }

        System.out.println("Valor Pasa: " + pasa);
        System.out.println("Fecha Inicio " + nuevoAusentismo.getFecha());
        if (pasa != 0) {
            PrimefacesContextUI.actualizar("formularioDialogos:validacionNuevoAusentismo");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoAusentismo').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                altoTabla = "135";
                System.out.println("Activar");
                System.out.println("TipoLista= " + tipoLista);
                cerrarFiltrado();
            }
            cambiosPagina = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");

            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoAusentismo.setSecuencia(l);
            nuevoAusentismo.setEmpleado(seleccionMostrar);
            listaAusentismosCrear.add(nuevoAusentismo);
            listaAusentismos.add(nuevoAusentismo);
            ausentismoSeleccionado = nuevoAusentismo;
            modificarInfoRegistroAusentismos(listaAusentismos.size());
            nuevoAusentismo = new Soausentismos();
            nuevoAusentismo.setTipo(new Tiposausentismos());
            nuevoAusentismo.setClase(new Clasesausentismos());
            nuevoAusentismo.setCausa(new Causasausentismos());
            nuevoAusentismo.setPorcentajeindividual(BigDecimal.valueOf(0));
            nuevoAusentismo.setBaseliquidacion(BigInteger.valueOf(0));
            nuevoAusentismo.setFormaliquidacion(" ");
            nuevoAusentismo.setRelacionadaBool(false);
            nuevoAusentismo.setAccidente(new Soaccidentes());
            nuevoAusentismo.setEnfermedad(new EnfermeadadesProfesionales());
            nuevoAusentismo.setDiagnosticocategoria(new Diagnosticoscategorias());
            nuevoAusentismo.setProrroga(new Soausentismos());
            nuevoAusentismo.setTercero(new Terceros());

            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoAusentismoEmpleado').hide()");
        }
    }

    public void confirmarDuplicar() {

        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        cambiosPagina = false;
        PrimefacesContextUI.actualizar("form:ACEPTAR");

        if (duplicarAusentismo.getFecha() == null) {
            System.out.println("Entro a Fecha ");
            mensajeValidacion = mensajeValidacion + " * Fecha Inicio Ausentismo\n";
            pasa++;
        }

        if (duplicarAusentismo.getTipo().getDescripcion().equals(" ") || duplicarAusentismo.getTipo().getDescripcion().equals("")) {
            System.out.println("Entro a Tipo");
            mensajeValidacion = mensajeValidacion + " * Tipo\n";
            pasa++;
        }

        if (duplicarAusentismo.getCausa().getDescripcion().equals(" ")) {
            System.out.println("Entro a Causa");
            mensajeValidacion = mensajeValidacion + " * Causa\n";
            pasa++;
        }
        if (duplicarAusentismo.getClase().getDescripcion().equals(" ")) {
            System.out.println("Entro a Clase");
            mensajeValidacion = mensajeValidacion + " * Clase\n";
            pasa++;
        }

        if (duplicarAusentismo.getFormaliquidacion().equals(" ")) {
            System.out.println("Entro a Forma");
            mensajeValidacion = mensajeValidacion + " * Forma Liquidación\n";
            pasa++;
        }

        System.out.println("Valor Pasa: " + pasa);
        if (pasa != 0) {
            PrimefacesContextUI.actualizar("formularioDialogos:validacionNuevoAusentismo");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoAusentismo').show()");
        }
        k++;
        l = BigInteger.valueOf(k);
        duplicarAusentismo.setSecuencia(l);
        listaAusentismosCrear.add(duplicarAusentismo);
        listaAusentismos.add(duplicarAusentismo);
        ausentismoSeleccionado = duplicarAusentismo;
        modificarInfoRegistroAusentismos(listaAusentismos.size());
        PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        if (guardado == true) {
            guardado = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }

        if (bandera == 1) {
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            altoTabla = "135";
            cerrarFiltrado();
        }
        duplicarAusentismo.setEmpleado(seleccionMostrar);
        duplicarAusentismo = new Soausentismos();
        PrimefacesContextUI.actualizar("formularioDialogos:DuplicarAusentismoEmpleado");
        PrimefacesContextUI.ejecutar("PF('DuplicarAusentismoEmpleado').hide()");
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar Ausentismos
     */
    public void limpiarduplicarAusentismos() {
        duplicarAusentismo = new Soausentismos();
        duplicarAusentismo.setTipo(new Tiposausentismos());
        duplicarAusentismo.setCausa(new Causasausentismos());
        duplicarAusentismo.setClase(new Clasesausentismos());
    }

    public void salirduplicarAusentismos() {
        duplicarAusentismo = new Soausentismos();
        duplicarAusentismo.setTipo(new Tiposausentismos());
        duplicarAusentismo.setCausa(new Causasausentismos());
        duplicarAusentismo.setClase(new Clasesausentismos());
        ausentismoSeleccionado = null;
        ausentismoSeleccionado = null;
        FacesContext c = FacesContext.getCurrentInstance();
        System.out.println("Entro a Bandera B. 1");
        botonLimpiarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:limpiarDuplicado");
        botonLimpiarD.setStyle("position: absolute; left: 50px; top: 400px;");
        botonAgregarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:agregarNADuplicado");
        botonAgregarD.setStyle("position: absolute; left: 350px; top: 400px;");
        botonCancelarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:cancelarNADuplicado");
        botonCancelarD.setStyle("position: absolute; left: 450px; top: 400px;");
        altoDialogoDuplicar = "430";
        banderaBotonesD = 0;
        colapsado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:DuplicarAusentismoEmpleado");
    }

    //GUARDAR
    public void guardarCambiosAusentismos() {

        if (guardado == false) {
            System.out.println("Realizando Operaciones Ausentismos");

            if (!listaAusentismosBorrar.isEmpty()) {
                for (int i = 0; i < listaAusentismosBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaAusentismosBorrar.size());

                    if (listaAusentismosBorrar.get(i).getDias() == null) {
                        listaAusentismosBorrar.get(i).setDias(null);
                    }
                    if (listaAusentismosBorrar.get(i).getHoras() == null) {
                        listaAusentismosBorrar.get(i).setHoras(null);
                    }
                    if (listaAusentismosBorrar.get(i).getFechafinaus() == null) {
                        listaAusentismosBorrar.get(i).setFechafinaus(null);
                    }
                    if (listaAusentismosBorrar.get(i).getFechaexpedicion() == null) {
                        listaAusentismosBorrar.get(i).setFechaexpedicion(null);
                    }
                    if (listaAusentismosBorrar.get(i).getFechainipago() == null) {
                        listaAusentismosBorrar.get(i).setFechainipago(null);
                    }
                    if (listaAusentismosBorrar.get(i).getFechafinpago() == null) {
                        listaAusentismosBorrar.get(i).setFechafinpago(null);
                    }
                    if (listaAusentismosBorrar.get(i).getPorcentajeindividual() == null) {
                        listaAusentismosBorrar.get(i).setPorcentajeindividual(null);
                    }
                    if (listaAusentismosBorrar.get(i).getBaseliquidacion() == null) {
                        listaAusentismosBorrar.get(i).setBaseliquidacion(null);
                    }
                    if (listaAusentismosBorrar.get(i).getFormaliquidacion() == null) {
                        listaAusentismosBorrar.get(i).setFormaliquidacion(null);
                    }
                    if (listaAusentismosBorrar.get(i).getAccidente().getSecuencia() == null) {
                        listaAusentismosBorrar.get(i).setAccidente(null);
                    }
                    if (listaAusentismosBorrar.get(i).getEnfermedad().getSecuencia() == null) {
                        listaAusentismosBorrar.get(i).setEnfermedad(null);
                    }
                    if (listaAusentismosBorrar.get(i).getNumerocertificado() == null) {
                        listaAusentismosBorrar.get(i).setNumerocertificado(null);
                    }
                    if (listaAusentismosBorrar.get(i).getDiagnosticocategoria().getSecuencia() == null) {
                        listaAusentismosBorrar.get(i).setDiagnosticocategoria(null);
                    }
                    if (listaAusentismosBorrar.get(i).getProrroga().getSecuencia() == null) {
                        listaAusentismosBorrar.get(i).setProrroga(null);
                    }
                    if (listaAusentismosBorrar.get(i).getRelacion() == null) {
                        listaAusentismosBorrar.get(i).setRelacion(null);
                    }
                    /*
                     * if (listaAusentismosBorrar.get(i).getRelacionadaBool()==
                     * false) {
                     * listaAusentismosBorrar.get(i).setRelacionada("N"); }
                     */
                    if (listaAusentismosBorrar.get(i).getTercero().getSecuencia() == null) {
                        listaAusentismosBorrar.get(i).setTercero(null);
                    }
                    if (listaAusentismosBorrar.get(i).getObservaciones() == null) {
                        listaAusentismosBorrar.get(i).setObservaciones(null);
                    }
                    administrarAusentismos.borrarAusentismos(listaAusentismosBorrar.get(i));
                }
                System.out.println("Entra");
                listaAusentismosBorrar.clear();
            }

            if (!listaAusentismosCrear.isEmpty()) {
                for (int i = 0; i < listaAusentismosCrear.size(); i++) {
                    System.out.println("Creando...");

                    if (listaAusentismosCrear.get(i).getDias() == null) {
                        listaAusentismosCrear.get(i).setDias(null);
                    }
                    if (listaAusentismosCrear.get(i).getHoras() == null) {
                        listaAusentismosCrear.get(i).setHoras(null);
                    }
                    if (listaAusentismosCrear.get(i).getFechafinaus() == null) {
                        listaAusentismosCrear.get(i).setFechafinaus(null);
                    }
                    if (listaAusentismosCrear.get(i).getFechaexpedicion() == null) {
                        listaAusentismosCrear.get(i).setFechaexpedicion(null);
                    }
                    if (listaAusentismosCrear.get(i).getFechainipago() == null) {
                        listaAusentismosCrear.get(i).setFechainipago(null);
                    }
                    if (listaAusentismosCrear.get(i).getFechafinpago() == null) {
                        listaAusentismosCrear.get(i).setFechafinpago(null);
                    }
                    if (listaAusentismosCrear.get(i).getPorcentajeindividual() == null) {
                        listaAusentismosCrear.get(i).setPorcentajeindividual(null);
                    }
                    if (listaAusentismosCrear.get(i).getBaseliquidacion() == null) {
                        listaAusentismosCrear.get(i).setBaseliquidacion(null);
                    }
                    if (listaAusentismosCrear.get(i).getFormaliquidacion() == null) {
                        listaAusentismosCrear.get(i).setFormaliquidacion(null);
                    }
                    if (listaAusentismosCrear.get(i).getAccidente().getSecuencia() == null) {
                        listaAusentismosCrear.get(i).setAccidente(null);
                    }
                    if (listaAusentismosCrear.get(i).getEnfermedad().getSecuencia() == null) {
                        listaAusentismosCrear.get(i).setEnfermedad(null);
                    }
                    if (listaAusentismosCrear.get(i).getNumerocertificado() == null) {
                        listaAusentismosCrear.get(i).setNumerocertificado(null);
                    }
                    if (listaAusentismosCrear.get(i).getDiagnosticocategoria().getSecuencia() == null) {
                        listaAusentismosCrear.get(i).setDiagnosticocategoria(null);
                    }
                    if (listaAusentismosCrear.get(i).getProrroga().getSecuencia() == null) {
                        listaAusentismosCrear.get(i).setProrroga(null);
                    }
                    if (listaAusentismosCrear.get(i).getRelacion() == null) {
                        listaAusentismosCrear.get(i).setRelacion(null);
                    }
                    if (listaAusentismosCrear.get(i).getRelacionadaBool() == false) {
                        listaAusentismosCrear.get(i).setRelacionada("N");
                    }
                    if (listaAusentismosCrear.get(i).getTercero().getSecuencia() == null) {
                        listaAusentismosCrear.get(i).setTercero(null);
                    }
                    if (listaAusentismosCrear.get(i).getObservaciones() == null) {
                        listaAusentismosCrear.get(i).setObservaciones(null);
                    }
                    administrarAusentismos.crearAusentismos(listaAusentismosCrear.get(i));
                }
                System.out.println("LimpiaLista");
                listaAusentismosCrear.clear();
            }
            if (!listaAusentismosModificar.isEmpty()) {
                administrarAusentismos.modificarAusentismos(listaAusentismosModificar);
                listaAusentismosModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaAusentismos = null;
            getListaAusentismos();
            contarRegistros();
            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = true;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
            guardado = true;
            ausentismoSeleccionado = null;
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }
        System.out.println("Tamaño lista: " + listaAusentismos.size());
        System.out.println("Valor k: " + k);
    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            altoTabla = "135";
            cerrarFiltrado();
        }
        FacesContext d = FacesContext.getCurrentInstance();

        botonLimpiar = (CommandButton) d.getViewRoot().findComponent("formularioDialogos:limpiar");
        botonLimpiar.setStyle("position: absolute; left: 50px; top: 400px;");
        botonAgregar = (CommandButton) d.getViewRoot().findComponent("formularioDialogos:agregarNA");
        botonAgregar.setStyle("position: absolute; left: 350px; top: 400px;");
        botonCancelar = (CommandButton) d.getViewRoot().findComponent("formularioDialogos:cancelarNA");
        botonCancelar.setStyle("position: absolute; left: 450px; top: 400px;");
        altoDialogoNuevo = "430";
        botonLimpiarD = (CommandButton) d.getViewRoot().findComponent("formularioDialogos:limpiarDuplicado");
        botonLimpiarD.setStyle("position: absolute; left: 50px; top: 400px;");
        botonAgregarD = (CommandButton) d.getViewRoot().findComponent("formularioDialogos:agregarNADuplicado");
        botonAgregarD.setStyle("position: absolute; left: 350px; top: 400px;");
        botonCancelarD = (CommandButton) d.getViewRoot().findComponent("formularioDialogos:cancelarNADuplicado");
        botonCancelarD.setStyle("position: absolute; left: 450px; top: 400px;");
        altoDialogoDuplicar = "430";
        banderaBotonesD = 0;
        banderaBotones = 0;
        listaAusentismosBorrar.clear();
        listaAusentismosCrear.clear();
        listaAusentismosModificar.clear();
        colapsado = true;
        ausentismoSeleccionado = null;
        listaAusentismos = null;
        getListaAusentismos();
        contarRegistros();
        guardado = true;
        permitirIndex = true;
        cambiosPagina = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        PrimefacesContextUI.actualizar("formularioDialogos:nuevoAusentismo");
        PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");
        PrimefacesContextUI.actualizar("formularioDialogos:NuevoAusentismoEmpleado");
        PrimefacesContextUI.actualizar("formularioDialogos:DuplicarAusentismoEmpleado");

    }

    public void cambiosToggle() {
        System.out.println("cambiosToggle");
        FacesContext c = FacesContext.getCurrentInstance();
        if (banderaBotones == 0) {
            System.out.println("Entro a Bandera B. 0");
            botonLimpiar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:limpiar");
            botonLimpiar.setStyle("position: absolute; left: 50px; top: 570px;");
            botonAgregar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:agregarNA");
            botonAgregar.setStyle("position: absolute; left: 350px; top: 570px;");
            botonCancelar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:cancelarNA");
            botonCancelar.setStyle("position: absolute; left: 450px; top: 570px;");
            altoDialogoNuevo = "600";
            banderaBotones = 1;
        } else if (banderaBotones == 1) {
            System.out.println("Entro a Bandera B. 1");
            botonLimpiar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:limpiar");
            botonLimpiar.setStyle("position: absolute; left: 50px; top: 400px;");
            botonAgregar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:agregarNA");
            botonAgregar.setStyle("position: absolute; left: 350px; top: 400px;");
            botonCancelar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:cancelarNA");
            botonCancelar.setStyle("position: absolute; left: 450px; top: 400px;");
            altoDialogoNuevo = "430";
            banderaBotones = 0;
        }

        RequestContext context = RequestContext.getCurrentInstance();
        //PrimefacesContextUI.actualizar("formularioDialogos:limpiar");
        PrimefacesContextUI.actualizar("formularioDialogos:nuevoAusentismo");
        PrimefacesContextUI.actualizar("formularioDialogos:NuevoAusentismoEmpleado");
        PrimefacesContextUI.ejecutar("PF('NuevoAusentismoEmpleado').show()");
    }

    public void cambiosToggleD() {
        System.out.println("cambiosToggle");
        FacesContext c = FacesContext.getCurrentInstance();
        if (banderaBotonesD == 0) {
            System.out.println("Entro a Bandera B. 0");
            botonLimpiarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:limpiarDuplicado");
            botonLimpiarD.setStyle("position: absolute; left: 50px; top: 570px;");
            botonAgregarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:agregarNADuplicado");
            botonAgregarD.setStyle("position: absolute; left: 350px; top: 570px;");
            botonCancelarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:cancelarNADuplicado");
            botonCancelarD.setStyle("position: absolute; left: 450px; top: 570px;");
            altoDialogoDuplicar = "530";
            banderaBotonesD = 1;
        } else if (banderaBotonesD == 1) {
            System.out.println("Entro a Bandera B. 1");
            botonLimpiarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:limpiarDuplicado");
            botonLimpiarD.setStyle("position: absolute; left: 50px; top: 400px;");
            botonAgregarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:agregarNADuplicado");
            botonAgregarD.setStyle("position: absolute; left: 350px; top: 400px;");
            botonCancelarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:cancelarNADuplicado");
            botonCancelarD.setStyle("position: absolute; left: 450px; top: 400px;");
            altoDialogoDuplicar = "430";
            banderaBotonesD = 0;
        }

        RequestContext context = RequestContext.getCurrentInstance();
        //PrimefacesContextUI.actualizar("formularioDialogos:limpiar");
        PrimefacesContextUI.actualizar("formularioDialogos:duplicarAusentismo");
        PrimefacesContextUI.actualizar("formularioDialogos:DuplicarAusentismoEmpleado");
        PrimefacesContextUI.ejecutar("PF('DuplicarAusentismoEmpleado').show()");
    }

    //SALIR
    public void salir() {
        if (bandera == 1) {
            altoTabla = "135";
            cerrarFiltrado();
        }
        listaAusentismosBorrar.clear();
        listaAusentismosCrear.clear();
        listaAusentismosModificar.clear();
        ausentismoSeleccionado = null;
        listaAusentismos = null;
        guardado = true;
        cambiosPagina = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        ATipo = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ATipo");
        ATipo.setFilterStyle("display: none; visibility: hidden;");
        AClase = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AClase");
        AClase.setFilterStyle("display: none; visibility: hidden;");
        ACausa = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ACausa");
        ACausa.setFilterStyle("display: none; visibility: hidden;");
        ADias = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ADias");
        ADias.setFilterStyle("display: none; visibility: hidden;");
        AHoras = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AHoras");
        AHoras.setFilterStyle("display: none; visibility: hidden;");
        AFecha = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFecha");
        AFecha.setFilterStyle("display: none; visibility: hidden;");
        AFechaFinaus = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaFinaus");
        AFechaFinaus.setFilterStyle("display: none; visibility: hidden;");
        AFechaExpedicion = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaExpedicion");
        AFechaExpedicion.setFilterStyle("display: none; visibility: hidden;");
        AFechaInipago = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaInipago");
        AFechaInipago.setFilterStyle("display: none; visibility: hidden;");
        AFechaFinpago = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaFinpago");
        AFechaFinpago.setFilterStyle("display: none; visibility: hidden;");
        APorcentaje = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:APorcentaje");
        APorcentaje.setFilterStyle("display: none; visibility: hidden;");
        ABase = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ABase");
        ABase.setFilterStyle("display: none; visibility: hidden;");
        AForma = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AForma");
        AForma.setFilterStyle("display: none; visibility: hidden;");
        ADescripcionCaso = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ADescripcionCaso");
        ADescripcionCaso.setFilterStyle("display: none; visibility: hidden;");
        AEnfermedad = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AEnfermedad");
        AEnfermedad.setFilterStyle("display: none; visibility: hidden;");
        ANumero = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ANumero");
        ANumero.setFilterStyle("display: none; visibility: hidden;");
        ADiagnostico = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ADiagnostico");
        ADiagnostico.setFilterStyle("display: none; visibility: hidden;");
        AProrroga = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AProrroga");
        AProrroga.setFilterStyle("display: none; visibility: hidden;");
        ANumero = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ANumero");
        ANumero.setFilterStyle("display: none; visibility: hidden;");
        ARelacion = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ARelacion");
        ARelacion.setFilterStyle("display: none; visibility: hidden;");
        ARelacionada = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ARelacionada");
        ARelacionada.setFilterStyle("display: none; visibility: hidden;");
        ATercero = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ATercero");
        ATercero.setFilterStyle("display: none; visibility: hidden;");
        AObservaciones = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AObservaciones");
        AObservaciones.setFilterStyle("display: none; visibility: hidden;");
        PrimefacesContextUI.actualizar("form:datosAusentismosEmpleado");
        bandera = 0;
        filtradosListaAusentismos = null;
        tipoLista = 0;
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        modificarInfoRegistroAusentismos(filtradosListaAusentismos.size());
    }

    public void eventoFiltrarEmpleadosLov() {
        System.out.println("lista filtrar empleados : " + filtradoslistaEmpleados.size());
        modificarInfoRegistroEmpleadoLov(filtradoslistaEmpleados.size());
    }

    public void eventoFiltrarTipo() {
        modificarInfoRegistroTipo(filtradoslistaTiposAusentismos.size());
    }

    public void eventoFiltrarBase() {
        modificarInfoRegistroBase(filtradosListaIBCS.size());
    }

    public void eventoFiltrarDiagnosticos() {
        modificarInfoRegistroDiagnostico(filtradoslistaDiagnosticos.size());
    }

    public void eventoFiltrarPorcentaje() {
        modificarInfoRegistroPorcentaje(filtradosListaPorcentajes.size());
    }

    public void eventoFiltrarCausa() {
        modificarInfoRegistroCausa(filtradoslistaCausasAusentismos.size());
    }

    public void eventoFiltrarClase() {
        modificarInfoRegistroClase(filtradoslistaClasesAusentismos.size());
    }

    public void eventoFiltrarForma() {
        modificarInfoRegistroForma(filtradosListaForma.size());
    }

    public void eventoFiltrarAccidente() {
        modificarInfoRegistroAccidente(filtradoslistaAccidentes.size());
    }

    public void eventoFiltrarEnfermedad() {
        modificarInfoRegistroEnfermedad(filtradoslistaEnfermeadadesProfesionales.size());
    }

    public void eventoFiltrarProrroga() {
        modificarInfoRegistroProrroga(filtradoslistaProrrogas.size());
    }

    public void eventoFiltrarTerceros() {
        modificarInfoRegistroTercero(filtradoslistaTerceros.size());
    }

    public void modificarInfoRegistroAusentismos(int valor) {
        infoRegistroAusentismos = String.valueOf(valor);
        PrimefacesContextUI.actualizar("form:infoRegistroAusentismos");
    }

    public void modificarInforegistroEmpleado(int valor) {
        infoRegistroEmpleado = String.valueOf(valor);
        PrimefacesContextUI.actualizar("form:infoRegistroEmpleados");
    }

    public void modificarInfoRegistroEmpleadoLov(int valor) {
        infoRegistroEmpleadoLov = String.valueOf(valor);
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroEmpleadoLOV");
    }

    public void modificarInfoRegistroAccidente(int valor) {
        infoRegistroAccidente = String.valueOf(valor);
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroAccidente");
    }

    public void modificarInfoRegistroDiagnostico(int valor) {
        infoRegistroDiagnostico = String.valueOf(valor);
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroDiagnostico");
    }

    public void modificarInfoRegistroProrroga(int valor) {
        infoRegistroProrroga = String.valueOf(valor);
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroProrroga");
    }

    public void modificarInfoRegistroTercero(int valor) {
        infoRegistroTercero = String.valueOf(valor);
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroTercero");
    }

    public void modificarInfoRegistroEnfermedad(int valor) {
        infoRegistroEnfermedad = String.valueOf(valor);
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroEnfermedad");
    }

    public void modificarInfoRegistroTipo(int valor) {
        infoRegistroTipo = String.valueOf(valor);
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroTipos");
    }

    public void modificarInfoRegistroClase(int valor) {
        infoRegistroClase = String.valueOf(valor);
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroClases");
    }

    public void modificarInfoRegistroCausa(int valor) {
        infoRegistroCausa = String.valueOf(valor);
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroCausas");
    }

    public void modificarInfoRegistroPorcentaje(int valor) {
        infoRegistroPorcentaje = String.valueOf(valor);
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroPorcentaje");
    }

    public void modificarInfoRegistroBase(int valor) {
        infoRegistroBase = String.valueOf(valor);
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroIBCS");
    }

    public void modificarInfoRegistroForma(int valor) {
        infoRegistroForma = String.valueOf(valor);
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroForma");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    public void contarRegistros() {
        if (listaAusentismos != null) {
            modificarInfoRegistroAusentismos(listaAusentismos.size());
        } else {
            modificarInfoRegistroAusentismos(0);
        }
    }

      public void recordarSeleccion() {
        if (ausentismoSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosAusentismosEmpleado");
            tablaC.setSelection(ausentismoSeleccionado);
        }
    }
    
    //GETTER & SETTER
    public List<Soausentismos> getListaAusentismos() {
        if (listaAusentismos == null && secuenciaEmpleado != null) {
            listaAusentismos = administrarAusentismos.ausentismosEmpleado(secuenciaEmpleado);
            if (listaAusentismos != null && !listaAusentismos.isEmpty()) {
                for (int i = 0; i < listaAusentismos.size(); i++) {
                    listaAusentismos.get(i).setRelacion(administrarAusentismos.mostrarRelacion(listaAusentismos.get(i).getSecuencia()));
                }
            }
        }
        return listaAusentismos;
    }

    public void setListaAusentismos(List<Soausentismos> listaAusentismos) {
        this.listaAusentismos = listaAusentismos;
    }

    public List<Soausentismos> getFiltradosListaAusentismos() {
        return filtradosListaAusentismos;
    }

    public void setFiltradosListaAusentismos(List<Soausentismos> filtradosListaAusentismos) {
        this.filtradosListaAusentismos = filtradosListaAusentismos;
    }

    public List<Empleados> getListaEmpleadosAusentismo() {
        if (listaEmpleadosAusentismo == null) {
            listaEmpleadosAusentismo = administrarAusentismos.lovEmpleados();
            if (!listaEmpleadosAusentismo.isEmpty()) {
                seleccionMostrar = listaEmpleadosAusentismo.get(0);
//                secuenciaEmpleado = seleccionMostrar.getSecuencia();
//                System.out.println(seleccionMostrar.getSecuencia());
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:datosEmpleados");
                //System.out.println("Seleccionado: " + seleccionMostrar.getPersona().getNombreCompleto());
            }
        }
        return listaEmpleadosAusentismo;
    }

    public void setListaEmpleadosAusentismo(List<Empleados> listaEmpleadosAusentismo) {
        this.listaEmpleadosAusentismo = listaEmpleadosAusentismo;
    }

    public List<Empleados> getFiltradosListaEmpleadosAusentismo() {
        return filtradosListaEmpleadosAusentismo;
    }

    public void setFiltradosListaEmpleadosAusentismo(List<Empleados> filtradosListaEmpleadosAusentismo) {
        this.filtradosListaEmpleadosAusentismo = filtradosListaEmpleadosAusentismo;
    }

    public Empleados getSeleccionMostrar() {
        return seleccionMostrar;
    }

    public void setSeleccionMostrar(Empleados seleccionMostrar) {
        this.seleccionMostrar = seleccionMostrar;
    }

    public List<Empleados> getListaEmpleados() {
//        if (listaEmpleados == null) {
//            listaEmpleados = administrarAusentismos.lovEmpleados();
//            if (!listaEmpleados.isEmpty()) {
//                seleccionMostrar = listaEmpleados.get(0);
//            }
//        }
//        return listaEmpleados;
        if (listaEmpleados == null) {
            listaEmpleados = administrarAusentismos.lovEmpleados();
        }
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public List<Empleados> getFiltradoslistaEmpleados() {
        return filtradoslistaEmpleados;
    }

    public void setFiltradoslistaEmpleados(List<Empleados> filtradoslistaEmpleados) {
        this.filtradoslistaEmpleados = filtradoslistaEmpleados;
    }

    public Empleados getSeleccionEmpleados() {
        return seleccionEmpleados;
    }

    public void setSeleccionEmpleados(Empleados seleccionEmpleados) {
        this.seleccionEmpleados = seleccionEmpleados;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<Tiposausentismos> getListaTiposAusentismos() {
        if (listaTiposAusentismos == null) {
            listaTiposAusentismos = administrarAusentismos.lovTiposAusentismos();
        }
        return listaTiposAusentismos;
    }

    public void setListaTiposAusentismos(List<Tiposausentismos> listaTiposAusentismos) {
        this.listaTiposAusentismos = listaTiposAusentismos;
    }

    public List<Tiposausentismos> getFiltradoslistaTiposAusentismos() {
        return filtradoslistaTiposAusentismos;
    }

    public void setFiltradoslistaTiposAusentismos(List<Tiposausentismos> filtradoslistaTiposAusentismos) {
        this.filtradoslistaTiposAusentismos = filtradoslistaTiposAusentismos;
    }

    public Tiposausentismos getSeleccionTiposAusentismos() {
        return seleccionTiposAusentismos;
    }

    public void setSeleccionTiposAusentismos(Tiposausentismos seleccionTiposAusentismos) {
        this.seleccionTiposAusentismos = seleccionTiposAusentismos;
    }

    public Clasesausentismos getSeleccionClasesAusentismos() {
        return seleccionClasesAusentismos;
    }

    public void setSeleccionClasesAusentismos(Clasesausentismos seleccionClasesAusentismos) {
        this.seleccionClasesAusentismos = seleccionClasesAusentismos;
    }

    public Causasausentismos getSeleccionCausasAusentismos() {
        return seleccionCausasAusentismos;
    }

    public void setSeleccionCausasAusentismos(Causasausentismos seleccionCausasAusentismos) {
        this.seleccionCausasAusentismos = seleccionCausasAusentismos;
    }

    public List<Clasesausentismos> getListaClasesAusentismos() {
        if (listaClasesAusentismos == null) {
            listaClasesAusentismos = administrarAusentismos.lovClasesAusentismos();
        }
        return listaClasesAusentismos;
    }

    public void setListaClasesAusentismos(List<Clasesausentismos> listaClasesAusentismos) {
        this.listaClasesAusentismos = listaClasesAusentismos;
    }

    public List<Clasesausentismos> getFiltradoslistaClasesAusentismos() {
        return filtradoslistaClasesAusentismos;
    }

    public void setFiltradoslistaClasesAusentismos(List<Clasesausentismos> filtradoslistaClasesAusentismos) {
        this.filtradoslistaClasesAusentismos = filtradoslistaClasesAusentismos;
    }

    public List<Causasausentismos> getListaCausasAusentismos() {

        if (listaCausasAusentismos == null) {
            listaCausasAusentismos = administrarAusentismos.lovCausasAusentismos();
        }
        return listaCausasAusentismos;
    }

    public void setListaCausasAusentismos(List<Causasausentismos> listaCausasAusentismos) {
        this.listaCausasAusentismos = listaCausasAusentismos;
    }

    public List<Causasausentismos> getFiltradoslistaCausasAusentismos() {
        return filtradoslistaCausasAusentismos;
    }

    public void setFiltradoslistaCausasAusentismos(List<Causasausentismos> filtradoslistaCausasAusentismos) {
        this.filtradoslistaCausasAusentismos = filtradoslistaCausasAusentismos;
    }

    public List<String> getListaPorcentaje() {
        return listaPorcentaje;
    }

    public void setListaPorcentaje(List<String> listaPorcentaje) {
        this.listaPorcentaje = listaPorcentaje;
    }

    public List<String> getFiltradosListaPorcentajes() {
        return filtradosListaPorcentajes;
    }

    public void setFiltradosListaPorcentajes(List<String> filtradosListaPorcentajes) {
        this.filtradosListaPorcentajes = filtradosListaPorcentajes;
    }

    public String getSeleccionPorcentajes() {
        return seleccionPorcentajes;
    }

    public void setSeleccionPorcentajes(String seleccionPorcentajes) {
        this.seleccionPorcentajes = seleccionPorcentajes;
    }

    public BigInteger getSecuenciaEmpleado() {
        return secuenciaEmpleado;
    }

    public void setSecuenciaEmpleado(BigInteger secuenciaEmpleado) {
        this.secuenciaEmpleado = secuenciaEmpleado;
    }

    public List<Ibcs> getListaIBCS() {
        if (listaIBCS == null) {
            listaIBCS = administrarAusentismos.empleadosIBCS(secuenciaEmpleado);
        }
        return listaIBCS;
    }

    public void setListaIBCS(List<Ibcs> listaIBCS) {
        this.listaIBCS = listaIBCS;
    }

    public List<Ibcs> getFiltradosListaIBCS() {
        return filtradosListaIBCS;
    }

    public void setFiltradosListaIBCS(List<Ibcs> filtradosListaIBCS) {
        this.filtradosListaIBCS = filtradosListaIBCS;
    }

    public Ibcs getSeleccionIBCS() {
        return seleccionIBCS;
    }

    public void setSeleccionIBCS(Ibcs seleccionIBCS) {
        this.seleccionIBCS = seleccionIBCS;
    }

    public List<String> getListaForma() {
        return listaForma;
    }

    public void setListaForma(List<String> listaForma) {
        this.listaForma = listaForma;
    }

    public List<String> getFiltradosListaForma() {
        return filtradosListaForma;
    }

    public void setFiltradosListaForma(List<String> filtradosListaForma) {
        this.filtradosListaForma = filtradosListaForma;
    }

    public String getSeleccionForma() {
        return seleccionForma;
    }

    public void setSeleccionForma(String seleccionForma) {
        this.seleccionForma = seleccionForma;
    }

    public List<Soaccidentes> getListaAccidentes() {
        if (listaAccidentes == null) {
            listaAccidentes = administrarAusentismos.lovAccidentes(secuenciaEmpleado);
        }
        return listaAccidentes;
    }

    public void setListaAccidentes(List<Soaccidentes> listaAccidentes) {
        this.listaAccidentes = listaAccidentes;
    }

    public List<Soaccidentes> getFiltradoslistaAccidentes() {
        return filtradoslistaAccidentes;
    }

    public void setFiltradoslistaAccidentes(List<Soaccidentes> filtradoslistaAccidentes) {
        this.filtradoslistaAccidentes = filtradoslistaAccidentes;
    }

    public Soaccidentes getSeleccionAccidentes() {
        return seleccionAccidentes;
    }

    public void setSeleccionAccidentes(Soaccidentes seleccionAccidentes) {
        this.seleccionAccidentes = seleccionAccidentes;
    }

    public List<EnfermeadadesProfesionales> getListaEnfermeadadesProfesionales() {

        listaEnfermeadadesProfesionales = administrarAusentismos.empleadosEP(secuenciaEmpleado);
        return listaEnfermeadadesProfesionales;
    }

    public void setListaEnfermeadadesProfesionales(List<EnfermeadadesProfesionales> listaEnfermeadadesProfesionales) {
        this.listaEnfermeadadesProfesionales = listaEnfermeadadesProfesionales;
    }

    public List<EnfermeadadesProfesionales> getFiltradoslistaEnfermeadadesProfesionales() {
        return filtradoslistaEnfermeadadesProfesionales;
    }

    public void setFiltradoslistaEnfermeadadesProfesionales(List<EnfermeadadesProfesionales> filtradoslistaEnfermeadadesProfesionales) {
        this.filtradoslistaEnfermeadadesProfesionales = filtradoslistaEnfermeadadesProfesionales;
    }

    public EnfermeadadesProfesionales getSeleccionEnfermeadadesProfesionales() {
        return seleccionEnfermeadadesProfesionales;
    }

    public void setSeleccionEnfermeadadesProfesionales(EnfermeadadesProfesionales seleccionEnfermeadadesProfesionales) {
        this.seleccionEnfermeadadesProfesionales = seleccionEnfermeadadesProfesionales;
    }

    public List<Diagnosticoscategorias> getListaDiagnosticos() {

        if (listaDiagnosticos == null) {
            listaDiagnosticos = administrarAusentismos.lovDiagnosticos();
        }
        return listaDiagnosticos;
    }

    public void setListaDiagnosticos(List<Diagnosticoscategorias> listaDiagnosticos) {
        this.listaDiagnosticos = listaDiagnosticos;
    }

    public List<Diagnosticoscategorias> getFiltradoslistaDiagnosticos() {
        return filtradoslistaDiagnosticos;
    }

    public void setFiltradoslistaDiagnosticos(List<Diagnosticoscategorias> filtradoslistaDiagnosticos) {
        this.filtradoslistaDiagnosticos = filtradoslistaDiagnosticos;
    }

    public Diagnosticoscategorias getSeleccionDiagnosticos() {
        return seleccionDiagnosticos;
    }

    public void setSeleccionDiagnosticos(Diagnosticoscategorias seleccionDiagnosticos) {
        this.seleccionDiagnosticos = seleccionDiagnosticos;
    }

    public List<Terceros> getListaTerceros() {
        if (listaTerceros == null) {
            listaTerceros = administrarAusentismos.lovTerceros();
        }
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

    public List<Soausentismos> getListaProrrogas() {
        if (listaProrrogas == null) {
            listaProrrogas = administrarAusentismos.lovProrrogas(secuenciaEmpleado, secuenciaCausa, secuenciaAusentismo);
        }
        return listaProrrogas;
    }

    public void setListaProrrogas(List<Soausentismos> listaProrrogas) {
        this.listaProrrogas = listaProrrogas;
    }

    public List<Soausentismos> getFiltradoslistaProrrogas() {
        return filtradoslistaProrrogas;
    }

    public void setFiltradoslistaProrrogas(List<Soausentismos> filtradoslistaProrrogas) {
        this.filtradoslistaProrrogas = filtradoslistaProrrogas;
    }

    public Soausentismos getSeleccionProrrogas() {
        return seleccionProrrogas;
    }

    public void setSeleccionProrrogas(Soausentismos seleccionProrrogas) {
        this.seleccionProrrogas = seleccionProrrogas;
    }

    public String getProrroga() {
        if (seleccionProrrogas != null) {
            if (Prorroga == null) {
                Prorroga = administrarAusentismos.mostrarProrroga(seleccionProrrogas.getSecuencia());
            }
        }
        return Prorroga;

    }

    public String getRelacion() {
        if (ausentismoSeleccionado != null) {
            if (Relacion == null) {
                Relacion = administrarAusentismos.mostrarRelacion(ausentismoSeleccionado.getSecuencia());
            }
        }
        return Relacion;
    }

    public Soausentismos getEditarAusentismos() {
        return editarAusentismos;
    }

    public void setEditarAusentismos(Soausentismos editarAusentismos) {
        this.editarAusentismos = editarAusentismos;
    }

    public Soausentismos getNuevoAusentismo() {
        return nuevoAusentismo;
    }

    public void setNuevoAusentismo(Soausentismos nuevoAusentismo) {
        this.nuevoAusentismo = nuevoAusentismo;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public Soausentismos getDuplicarAusentismo() {
        return duplicarAusentismo;
    }

    public void setDuplicarAusentismo(Soausentismos duplicarAusentismo) {
        this.duplicarAusentismo = duplicarAusentismo;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public Soausentismos getAusentismoSeleccionado() {
        return ausentismoSeleccionado;
    }

    public void setAusentismoSeleccionado(Soausentismos ausentismoSeleccionado) {
        this.ausentismoSeleccionado = ausentismoSeleccionado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getAltoDialogoNuevo() {
        return altoDialogoNuevo;
    }

    public void setAltoDialogoNuevo(String altoDialogoNuevo) {
        this.altoDialogoNuevo = altoDialogoNuevo;
    }

    public String getAltoDialogoDuplicar() {
        return altoDialogoDuplicar;
    }

    public void setAltoDialogoDuplicar(String altoDialogoDuplicar) {
        this.altoDialogoDuplicar = altoDialogoDuplicar;
    }

    public String getInfoRegistroTipo() {
//        getListaTiposAusentismos();
//        if (listaTiposAusentismos != null) {
//            modificarInfoRegistroTipo(listaTiposAusentismos.size());
//        } else {
//            modificarInfoRegistroTipo(0);
//        }

        return infoRegistroTipo;
    }

    public void setInfoRegistroTipo(String infoRegistroTipo) {
        this.infoRegistroTipo = infoRegistroTipo;
    }

    public String getInfoRegistroClase() {
//        getListaClasesAusentismos();
//        if (listaClasesAusentismos != null) {
//            modificarInfoRegistroClase(listaClasesAusentismos.size());
//        } else {
//            modificarInfoRegistroClase(0);
//        }
        return infoRegistroClase;
    }

    public void setInfoRegistroClase(String infoRegistroClase) {
        this.infoRegistroClase = infoRegistroClase;
    }

    public String getInfoRegistroCausa() {
//        getListaCausasAusentismos();
//        if (listaCausasAusentismos != null) {
//            modificarInfoRegistroCausa(listaCausasAusentismos.size());
//        } else {
//            modificarInfoRegistroCausa(0);
//        }
        return infoRegistroCausa;
    }

    public void setInfoRegistroCausa(String infoRegistroCausa) {
        this.infoRegistroCausa = infoRegistroCausa;
    }

    public String getInfoRegistroPorcentaje() {
//        getListaPorcentaje();
//        if (listaPorcentaje != null) {
//            modificarInfoRegistroPorcentaje(listaPorcentaje.size());
//        } else {
//            modificarInfoRegistroPorcentaje(0);
//        }
        return infoRegistroPorcentaje;
    }

    public void setInfoRegistroPorcentaje(String infoRegistroPorcentaje) {
        this.infoRegistroPorcentaje = infoRegistroPorcentaje;
    }

    public String getInfoRegistroBase() {
//        getListaIBCS();
//        if (listaIBCS != null) {
//            modificarInfoRegistroBase(listaIBCS.size());
//        } else {
//            modificarInfoRegistroBase(0);
//        }
        return infoRegistroBase;
    }

    public void setInfoRegistroBase(String infoRegistroBase) {
        this.infoRegistroBase = infoRegistroBase;
    }

    public String getInfoRegistroAccidente() {
//        getListaAccidentes();
//        if (listaAccidentes != null) {
//            modificarInfoRegistroAccidente(listaAccidentes.size());
//        } else {
//            modificarInfoRegistroAccidente(0);
//        }
        return infoRegistroAccidente;
    }

    public void setInfoRegistroAccidente(String infoRegistroAccidente) {
        this.infoRegistroAccidente = infoRegistroAccidente;
    }

    public String getInfoRegistroEnfermedad() {
//        getListaEnfermeadadesProfesionales();
//        if (listaEnfermeadadesProfesionales != null) {
//            modificarInfoRegistroEnfermedad(listaEnfermeadadesProfesionales.size());
//        } else {
//            modificarInfoRegistroEnfermedad(0);
//        }
        return infoRegistroEnfermedad;
    }

    public void setInfoRegistroEnfermedad(String infoRegistroEnfermedad) {
        this.infoRegistroEnfermedad = infoRegistroEnfermedad;
    }

    public String getInfoRegistroDiagnostico() {
//        getListaDiagnosticos();
//        if (listaDiagnosticos != null) {
//            modificarInfoRegistroDiagnostico(listaDiagnosticos.size());
//        } else {
//            modificarInfoRegistroDiagnostico(0);
//        }
        return infoRegistroDiagnostico;
    }

    public void setInfoRegistroDiagnostico(String infoRegistroDiagnostico) {
        this.infoRegistroDiagnostico = infoRegistroDiagnostico;
    }

    public String getInfoRegistroProrroga() {
//       getListaProrrogas();
//        if (listaProrrogas != null) {
//            modificarInfoRegistroProrroga(listaProrrogas.size());
//        } else {
//            modificarInfoRegistroProrroga(0);
//        }
        return infoRegistroProrroga;
    }

    public void setInfoRegistroProrroga(String infoRegistroProrroga) {
        this.infoRegistroProrroga = infoRegistroProrroga;
    }

    public String getInfoRegistroTercero() {
//        getListaTerceros();
//        if (listaTerceros != null) {
//            modificarInfoRegistroTercero(listaTerceros.size());
//        } else {
//            modificarInfoRegistroTercero(0);
//        }
        return infoRegistroTercero;
    }

    public void setInfoRegistroTercero(String infoRegistroTercero) {
        this.infoRegistroTercero = infoRegistroTercero;
    }

    public String getInfoRegistroEmpleado() {
        getListaEmpleadosAusentismo();
        if (listaEmpleadosAusentismo != null) {
            modificarInforegistroEmpleado(listaEmpleadosAusentismo.size());
        } else {
            modificarInforegistroEmpleado(0);
        }
        return infoRegistroEmpleado;
    }

    public void setInfoRegistroEmpleado(String infoRegistroEmpleado) {
        this.infoRegistroEmpleado = infoRegistroEmpleado;
    }

    public String getInfoRegistroForma() {
//        getListaForma();
//        if (listaForma != null) {
//            modificarInfoRegistroForma(listaForma.size());
//        } else {
//            modificarInfoRegistroForma(0);
//        }
        return infoRegistroForma;
    }

    public void setInfoRegistroForma(String infoRegistroForma) {
        this.infoRegistroForma = infoRegistroForma;
    }

    public boolean isColapsado() {
        return colapsado;
    }

    public void setColapsado(boolean colapsado) {
        this.colapsado = colapsado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getInfoRegistroAusentismos() {
        return infoRegistroAusentismos;
    }

    public void setInfoRegistroAusentismos(String infoRegistroAusentismos) {
        this.infoRegistroAusentismos = infoRegistroAusentismos;
    }

    public String getInfoRegistroEmpleadoLov() {
//        getListaEmpleados();
//        if (listaEmpleados != null) {
//            modificarInfoRegistroEmpleadoLov(listaEmpleados.size());
//        } else {
//            modificarInfoRegistroEmpleadoLov(0);
//        }
//        
        return infoRegistroEmpleadoLov;
    }

    public void setInfoRegistroEmpleadoLov(String infoRegistroEmpleadoLov) {
        this.infoRegistroEmpleadoLov = infoRegistroEmpleadoLov;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
