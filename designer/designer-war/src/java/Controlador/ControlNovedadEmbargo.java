/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Administrar.AdministrarElementosCausasAccidentes;
import Administrar.AdministrarNovedadesEmbargos;
import Entidades.DetallesFormasDtos;
import Entidades.EersPrestamos;
import Entidades.EersPrestamosDtos;
import Entidades.Empleados;
import Entidades.FormasDtos;
import Entidades.Juzgados;
import Entidades.MotivosEmbargos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Entidades.TiposEmbargos;
import Entidades.VWPrestamoDtosRealizados;
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNovedadesEmbargosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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
public class ControlNovedadEmbargo implements Serializable {

    @EJB
    AdministrarNovedadesEmbargosInterface administrarNovedadesEmbargos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Empleados> listaEmpleados;
    private Empleados empleadoActual;
    private List<Empleados> listaEmpleadosLOV;
    private List<Empleados> filtradoListaEmpleadosLOV;
    private Empleados empleadoSeleccionado;
    //ListaEmbargos
    private List<EersPrestamos> listaEmbargos;
    private List<EersPrestamos> filtradoListaEmbargos;
    private EersPrestamos embargoSeleccionado;
    //ListaEmbargos
    private List<EersPrestamosDtos> listaDetallesEmbargos;
    private List<EersPrestamosDtos> filtradoListaDetallesEmbargos;
    private EersPrestamosDtos detallesEmbargoSeleccionado;
    //REGISTRO ACTUAL
    private int registroActual, index, tablaActual, indexD;
    //OTROS
    private boolean aceptar, mostrarTodos;
    private String altoScrollEmbargos, altoScrollDetallesEmbargos, altoScrollDescuentosRealizados;
    //Crear EersPrestamos (Embargos)
    private List<EersPrestamos> listaEmbargosCrear;
    public EersPrestamos nuevoEmbargo;
    public EersPrestamos duplicarEmbargo;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<EersPrestamosDtos> listaDetallesEmbargosModificar;
    //Borrar Novedades
    private List<EersPrestamosDtos> listaDetallesEmbargosBorrar;
    //Crear EersPrestamosDtos (Detalles Embargos)
    private List<EersPrestamosDtos> listaDetallesEmbargosCrear;
    public EersPrestamosDtos nuevoEmbargoDetalle;
    public EersPrestamosDtos duplicarEmbargoDetalle;
    //Modificar Novedades
    private List<EersPrestamos> listaEmbargosModificar;
    //Borrar Novedades
    private List<EersPrestamos> listaEmbargosBorrar;
    //AUTOCOMPLETAR D
    private String Pago, Periodicidad;
    //AUTOCOMPLETAR
    private String TipoEmbargo, Juzgado, Motivo, Demandante, Forma, Nit;
    //OTROS
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //editar celda
    private EersPrestamos editarEmbargos;
    private EersPrestamosDtos editarDetallesEmbargos;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    private int cualCeldaD;
    private int tipoListaD;
    //RASTROS
    private BigInteger secRegistro;
    private BigInteger secRegistroD;
    private boolean guardado, guardarOk;
    private boolean cambiosPagina;
    //L.O.V Tipos Embargos
    private List<TiposEmbargos> lovlistaTiposEmbargos;
    private List<TiposEmbargos> lovfiltradoslistaTiposEmbargos;
    private TiposEmbargos tiposEmbargosSeleccionado;
    //L.O.V Juzgados
    private List<Juzgados> lovlistaJuzgados;
    private List<Juzgados> lovfiltradoslistaJuzgados;
    private Juzgados juzgadosSeleccionado;
    //L.O.V Motivos
    private List<MotivosEmbargos> lovlistaMotivos;
    private List<MotivosEmbargos> lovfiltradoslistaMotivos;
    private MotivosEmbargos motivosSeleccionado;
    //L.O.V Demandantes
    private List<Terceros> lovlistaDemandantes;
    private List<Terceros> lovfiltradoslistaDemandantes;
    private Terceros demandantesSeleccionado;
    //L.O.V Terceros
    private List<Terceros> lovlistaTerceros;
    private List<Terceros> lovfiltradoslistaTerceros;
    private Terceros tercerosSeleccionado;
    //L.O.V Terceros
    private List<Periodicidades> lovlistaPeriodicidades;
    private List<Periodicidades> lovfiltradoslistaPeriodicidades;
    private Periodicidades periodicidadesSeleccionado;
    //L.O.V Detalles Formas Descuentos
    private List<DetallesFormasDtos> lovlistaDetallesFormasDtos;
    private List<DetallesFormasDtos> lovfiltradoslistaDetallesFormasDtos;
    private DetallesFormasDtos detallesFormasDtosSeleccionado;
    //L.O.V Detalles Formas Descuentos
    private List<FormasDtos> lovlistaFormasDtos;
    private List<FormasDtos> lovfiltradoslistaFormasDtos;
    private FormasDtos formasDtosSeleccionado;
    //L.O.V VWPRESTAMOS
    private List<VWPrestamoDtosRealizados> listaVWPrestamo;
    private List<VWPrestamoDtosRealizados> filtradoListaVWPrestamo;
    private VWPrestamoDtosRealizados vwPrestamoSeleccionado;
    private Integer cualTabla;
    //FILTRADO
    // Falta la columna esa ficti de embargos
    private Column embargosTipoEmbargo, embargosDocumento, embargosFechaDocumento, embargosNumero, embargosJuzgado, embargosMotivo, embargosBeneficiario, embargosNit, embargosTercero, embargosValorTotal, embargosFechaInicio, embargosCancelacionDocumento, embargosCancelacionFecha, embargosObservaciones;
    private Column detallesPagos, detallesIka, detallesPorcentaje, detallesValor, detallesSaldoInicial, detallesPeriodicidadCodigo, detallesPeriodicidadNombre, detallesConceptoCodigo, detallesConceptoNombre, detallesEmpresa;
    //Tabla a Imprimir
    private String tablaImprimir, nombreArchivo;
    //Sec Abajo Duplicar
    private int m;
    private BigInteger n;
    //SECUENCIA DEL CONCEPTO
    private BigInteger secuenciaEmbargo;
    private boolean roPorcentaje;
    private boolean roPorcentajeD;
    private boolean roValor;
    private boolean roValorD;

    public ControlNovedadEmbargo() {
        permitirIndex = true;
        cualTabla = 0;
        bandera = 0;
        registroActual = 0;
        mostrarTodos = true;
        altoScrollEmbargos = "90";
        altoScrollDetallesEmbargos = "90";
        altoScrollDescuentosRealizados = "90";
        listaEmbargosBorrar = new ArrayList<EersPrestamos>();
        listaEmbargosCrear = new ArrayList<EersPrestamos>();
        listaEmbargosModificar = new ArrayList<EersPrestamos>();
        listaDetallesEmbargosBorrar = new ArrayList<EersPrestamosDtos>();
        listaDetallesEmbargosCrear = new ArrayList<EersPrestamosDtos>();
        listaDetallesEmbargosModificar = new ArrayList<EersPrestamosDtos>();
        tablaImprimir = ":formExportar:datosEmbargosExportar";
        nombreArchivo = "EmbargosXML";
        //Crear Vigencia Formal
        nuevoEmbargo = new EersPrestamos();
        nuevoEmbargo.setTipoembargo(new TiposEmbargos());
        nuevoEmbargo.setJuzgado(new Juzgados());
        nuevoEmbargo.setMotivoembargo(new MotivosEmbargos());
        nuevoEmbargo.setDemandante(new Terceros());
        nuevoEmbargo.setTercero(new Terceros());
        nuevoEmbargo.setFormadto(new FormasDtos());
        nuevoEmbargoDetalle = new EersPrestamosDtos();
        nuevoEmbargoDetalle.setDetalleformadto(new DetallesFormasDtos());
        nuevoEmbargoDetalle.setPeriodicidad(new Periodicidades());
        m = 0;
        roPorcentaje = false;
        roValor = false;
        cambiosPagina = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNovedadesEmbargos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //CREAR Embargo
    public void agregarNuevoEmbargo() {
        int pasa = 0;
        int pasa2 = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoEmbargo.getDocumento() == null || nuevoEmbargo.getDocumento().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Numero/Nombre Oficio \n";
            pasa++;
        }

        if (nuevoEmbargo.getFechadocumento() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Oficio\n";
            pasa++;
        }
        if (nuevoEmbargo.getTercero().getNit() == 0) {
            mensajeValidacion = mensajeValidacion + " * N.I.T\n";
            pasa++;
        }

        if (nuevoEmbargo.getValortotal() == null) {
            mensajeValidacion = mensajeValidacion + " * Valor Total\n";
            pasa++;
        }

        if (nuevoEmbargo.getTipoembargo().getDescripcion() == null || nuevoEmbargo.getTipoembargo().getDescripcion().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Tipo Embargo\n";
            pasa++;
        }

        if (nuevoEmbargo.getFechainiciodescuento() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicio Descuento\n";
            pasa++;
        }

        if (nuevoEmbargo.getFormadto().getDescripcion() == null || nuevoEmbargo.getFormadto().getDescripcion().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Forma de Descuento\n";
            pasa++;
        }

        if ((nuevoEmbargo.getCancelaciondocumento() != null && nuevoEmbargo.getCancelacionfechahasta() == null) || (nuevoEmbargo.getCancelaciondocumento() == null && nuevoEmbargo.getCancelacionfechahasta() != null)) {
            RequestContext.getCurrentInstance().update("formularioDialogos:cancelacion");
            PrimefacesContextUI.ejecutar("PF('cancelacion').show()");
            pasa2++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoEmbargo");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoEmbargo').show()");
        }

        if (pasa == 0 && pasa2 == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                embargosTipoEmbargo = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosTipoEmbargo");
                embargosTipoEmbargo.setFilterStyle("display: none; visibility: hidden;");
                embargosDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosDocumento");
                embargosDocumento.setFilterStyle("display: none; visibility: hidden;");
                embargosFechaDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosFechaDocumento");
                embargosFechaDocumento.setFilterStyle("display: none; visibility: hidden;");
                embargosNumero = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosNumero");
                embargosNumero.setFilterStyle("display: none; visibility: hidden;");
                embargosJuzgado = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosJuzgado");
                embargosJuzgado.setFilterStyle("display: none; visibility: hidden;");
                embargosMotivo = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosMotivo");
                embargosMotivo.setFilterStyle("display: none; visibility: hidden;");
                embargosBeneficiario = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosBeneficiario");
                embargosBeneficiario.setFilterStyle("display: none; visibility: hidden;");
                embargosNit = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosNit");
                embargosNit.setFilterStyle("display: none; visibility: hidden;");
                embargosTercero = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosTercero");
                embargosTercero.setFilterStyle("display: none; visibility: hidden;");
                embargosValorTotal = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosValorTotal");
                embargosValorTotal.setFilterStyle("display: none; visibility: hidden;");
                embargosFechaInicio = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosFechaInicio");
                embargosFechaInicio.setFilterStyle("display: none; visibility: hidden;");
                embargosCancelacionDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosCancelacionDocumento");
                embargosCancelacionDocumento.setFilterStyle("display: none; visibility: hidden;");
                embargosCancelacionFecha = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosCancelacionFecha");
                embargosCancelacionFecha.setFilterStyle("display: none; visibility: hidden;");
                embargosObservaciones = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosObservaciones");
                embargosObservaciones.setFilterStyle("display: none; visibility: hidden;");

                altoScrollEmbargos = "90";
                RequestContext.getCurrentInstance().update("form:datosEmbargos");
                bandera = 0;
                filtradoListaEmbargos = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoEmbargo.setSecuencia(l);
            nuevoEmbargo.setTipoeer("EMBARGO");
            nuevoEmbargo.setEmpleado(empleadoActual);

            listaEmbargosCrear.add(nuevoEmbargo);
            listaEmbargos.add(nuevoEmbargo);
            nuevoEmbargo = new EersPrestamos();
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroEmbargos').hide()");
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
    public void cambiarEmbargo() {
        //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
        //{
        if (listaEmbargosCrear.isEmpty() && listaEmbargosBorrar.isEmpty() && listaEmbargosModificar.isEmpty()) {
            secuenciaEmbargo = embargoSeleccionado.getSecuencia();
            listaDetallesEmbargos = null;
            getListaDetallesEmbargos();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
            //}
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
            PrimefacesContextUI.ejecutar("PF('cambiar').show()");

        }
    }

    public void limpiarListas() {
        listaEmbargosCrear.clear();
        listaEmbargosBorrar.clear();
        listaEmbargosModificar.clear();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEmbargos");

    }

    //BORRAR Embargo / Detalles Embargos
    public void borrarEmbargo() {

        if (index >= 0 && cualTabla == 0) {
            if (tipoLista == 0) {
                if (!listaEmbargosModificar.isEmpty() && listaEmbargosModificar.contains(listaEmbargos.get(index))) {
                    int modIndex = listaEmbargosModificar.indexOf(listaEmbargos.get(index));
                    listaEmbargosModificar.remove(modIndex);
                    listaEmbargosBorrar.add(listaEmbargos.get(index));
                } else if (!listaEmbargosCrear.isEmpty() && listaEmbargosCrear.contains(listaEmbargos.get(index))) {
                    int crearIndex = listaEmbargosCrear.indexOf(listaEmbargos.get(index));
                    listaEmbargosCrear.remove(crearIndex);
                } else {
                    listaEmbargosBorrar.add(listaEmbargos.get(index));
                }
                listaEmbargos.remove(index);
            }

            if (tipoLista == 1) {
                if (!listaEmbargosModificar.isEmpty() && listaEmbargosModificar.contains(filtradoListaEmbargos.get(index))) {
                    int modIndex = listaEmbargosModificar.indexOf(filtradoListaEmbargos.get(index));
                    listaEmbargosModificar.remove(modIndex);
                    listaEmbargosBorrar.add(filtradoListaEmbargos.get(index));
                } else if (!listaEmbargosCrear.isEmpty() && listaEmbargosCrear.contains(filtradoListaEmbargos.get(index))) {
                    int crearIndex = listaEmbargosCrear.indexOf(filtradoListaEmbargos.get(index));
                    listaEmbargosCrear.remove(crearIndex);
                } else {
                    listaEmbargosBorrar.add(filtradoListaEmbargos.get(index));
                }
                int CIndex = listaEmbargos.indexOf(filtradoListaEmbargos.get(index));
                listaEmbargos.remove(CIndex);
                filtradoListaEmbargos.remove(index);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEmbargos");

            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else if (indexD >= 0 && cualTabla == 1) {

            if (tipoListaD == 0) {
                if (!listaDetallesEmbargosModificar.isEmpty() && listaDetallesEmbargosModificar.contains(listaDetallesEmbargos.get(indexD))) {
                    int modIndex = listaDetallesEmbargosModificar.indexOf(listaDetallesEmbargos.get(indexD));
                    listaDetallesEmbargosModificar.remove(modIndex);
                    listaDetallesEmbargosBorrar.add(listaDetallesEmbargos.get(indexD));
                } else if (!listaDetallesEmbargosCrear.isEmpty() && listaDetallesEmbargosCrear.contains(listaDetallesEmbargos.get(indexD))) {
                    int crearIndex = listaDetallesEmbargosCrear.indexOf(listaDetallesEmbargos.get(indexD));
                    listaDetallesEmbargosCrear.remove(crearIndex);
                } else {
                    listaDetallesEmbargosBorrar.add(listaDetallesEmbargos.get(indexD));
                }
                listaDetallesEmbargos.remove(indexD);
            }

            if (tipoListaD == 1) {
                if (!listaDetallesEmbargosModificar.isEmpty() && listaDetallesEmbargosModificar.contains(filtradoListaDetallesEmbargos.get(indexD))) {
                    int modIndex = listaDetallesEmbargosModificar.indexOf(filtradoListaDetallesEmbargos.get(indexD));
                    listaDetallesEmbargosModificar.remove(modIndex);
                    listaDetallesEmbargosBorrar.add(filtradoListaDetallesEmbargos.get(indexD));
                } else if (!listaDetallesEmbargosCrear.isEmpty() && listaDetallesEmbargosCrear.contains(filtradoListaDetallesEmbargos.get(indexD))) {
                    int crearIndex = listaDetallesEmbargosCrear.indexOf(filtradoListaDetallesEmbargos.get(indexD));
                    listaDetallesEmbargosCrear.remove(crearIndex);
                } else {
                    listaDetallesEmbargosBorrar.add(filtradoListaDetallesEmbargos.get(indexD));
                }
                int CIndex = listaDetallesEmbargos.indexOf(filtradoListaDetallesEmbargos.get(indexD));
                listaDetallesEmbargos.remove(CIndex);
                filtradoListaDetallesEmbargos.remove(indexD);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
            indexD = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void guardarTodo() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones Embargos");
            if (!listaEmbargosBorrar.isEmpty()) {
                for (int i = 0; i < listaEmbargosBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    if (listaEmbargosBorrar.get(i).getTipoembargo() == null) {
                        listaEmbargosBorrar.get(i).setTipoembargo(null);
                    }
                    if (listaEmbargosBorrar.get(i).getCancelaciondocumento() == null) {
                        listaEmbargosBorrar.get(i).setCancelaciondocumento(null);
                    }
                    if (listaEmbargosBorrar.get(i).getCancelacionfechahasta() == null) {
                        listaEmbargosBorrar.get(i).setCancelacionfechahasta(null);
                    }
                    if (listaEmbargosBorrar.get(i).getMotivoembargo() == null) {
                        listaEmbargosBorrar.get(i).setMotivoembargo(null);
                    }
                    if (listaEmbargosBorrar.get(i).getJuzgado() == null) {
                        listaEmbargosBorrar.get(i).setJuzgado(null);
                    }
                    if (listaEmbargosBorrar.get(i).getNumeroproceso() == null) {
                        listaEmbargosBorrar.get(i).setNumeroproceso(null);
                    }

                    administrarNovedadesEmbargos.borrarEmbargo(listaEmbargosBorrar.get(i));

                    System.out.println("Entra");
                    listaEmbargosBorrar.clear();
                }
            }
            if (!listaEmbargosCrear.isEmpty()) {
                for (int i = 0; i < listaEmbargosCrear.size(); i++) {
                    System.out.println("Creando...");
                    System.out.println(listaEmbargosCrear.size());
                    if (listaEmbargosCrear.get(i).getTipoembargo() == null) {
                        listaEmbargosCrear.get(i).setTipoembargo(null);
                    }
                    if (listaEmbargosCrear.get(i).getCancelaciondocumento() == null) {
                        listaEmbargosCrear.get(i).setCancelaciondocumento(null);
                    }
                    if (listaEmbargosCrear.get(i).getCancelacionfechahasta() == null) {
                        listaEmbargosCrear.get(i).setCancelacionfechahasta(null);
                    }
                    if (listaEmbargosCrear.get(i).getMotivoembargo() == null) {
                        listaEmbargosCrear.get(i).setMotivoembargo(null);
                    }
                    if (listaEmbargosCrear.get(i).getJuzgado() == null) {
                        listaEmbargosCrear.get(i).setJuzgado(null);
                    }
                    if (listaEmbargosCrear.get(i).getNumeroproceso() == null) {
                        listaEmbargosCrear.get(i).setNumeroproceso(null);
                    }
                    System.out.println("Guardar nuevoEmbargo : " + listaEmbargosCrear.get(i).getTercero().getNombre());
                    System.out.println("Guardar nuevoEmbargo : " + listaEmbargosCrear.get(i).getTercero().getNit());
                    administrarNovedadesEmbargos.crearEmbargo(listaEmbargosCrear.get(i));
                }

                System.out.println("LimpiaLista");
                listaEmbargosCrear.clear();
            }
            if (!listaEmbargosModificar.isEmpty()) {
                administrarNovedadesEmbargos.modificarEmbargo(listaEmbargosModificar);
                listaEmbargosModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaEmbargos = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
            guardado = true;
            permitirIndex = true;
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:aceptar");
            //  k = 0;
        }
        System.out.println("Valor k: " + k);
        index = -1;
        secRegistro = null;

        if (guardado == false) {
            System.out.println("Realizando Operaciones VigenciasNoFormales");
            if (!listaDetallesEmbargosBorrar.isEmpty()) {
                for (int i = 0; i < listaDetallesEmbargosBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    if (listaDetallesEmbargosBorrar.get(i).getValor() == null) {
                        listaDetallesEmbargosBorrar.get(i).setValor(null);
                    }

                    if (listaDetallesEmbargosBorrar.get(i).getPorcentaje() == null) {
                        listaDetallesEmbargosBorrar.get(i).setPorcentaje(null);
                    }
                    if (listaDetallesEmbargosBorrar.get(i).getSaldoinicial() == null) {
                        listaDetallesEmbargosBorrar.get(i).setSaldoinicial(null);
                    }

                    administrarNovedadesEmbargos.borrarDetalleEmbargo(listaDetallesEmbargosBorrar.get(i));
                }

                System.out.println("Entra");
                listaDetallesEmbargosBorrar.clear();
            }
        }
        if (!listaDetallesEmbargosCrear.isEmpty()) {
            for (int i = 0; i < listaDetallesEmbargosCrear.size(); i++) {
                System.out.println("Creando...");
                System.out.println(listaDetallesEmbargosCrear.size());
                if (listaDetallesEmbargosCrear.get(i).getValor() == null) {
                    listaDetallesEmbargosCrear.get(i).setValor(null);
                }

                if (listaDetallesEmbargosCrear.get(i).getPorcentaje() == null) {
                    listaDetallesEmbargosCrear.get(i).setPorcentaje(null);
                }
                if (listaDetallesEmbargosCrear.get(i).getSaldoinicial() == null) {
                    listaDetallesEmbargosCrear.get(i).setSaldoinicial(null);
                }
                System.out.println("Guardar nuevoDetalleEmbargo VALOR: " + listaDetallesEmbargosCrear.get(i).getValor());
                System.out.println("Guardar nuevoDetalleEmbargo PORCENTAJE: " + listaDetallesEmbargosCrear.get(i).getPorcentaje());

                administrarNovedadesEmbargos.crearDetalleEmbargo(listaDetallesEmbargosCrear.get(i));

            }

            System.out.println("LimpiaLista");
            listaDetallesEmbargosCrear.clear();
        }
        if (!listaDetallesEmbargosModificar.isEmpty()) {
            System.out.println("Lista Vigencias No Formales: " + listaDetallesEmbargosModificar.size());
            administrarNovedadesEmbargos.modificarDetalleEmbargo(listaDetallesEmbargosModificar);

            listaDetallesEmbargosModificar.clear();
        }

        System.out.println("Se guardaron los datos con exito");
        listaDetallesEmbargos = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
        FacesMessage msg = new FacesMessage("Información", "Se han guardado los datos exitosamente.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update("form:growl");
        guardado = true;
        permitirIndex = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        //  k = 0;

        System.out.println("Valor k: " + k);
        indexD = -1;
        cambiosPagina = true;
        secRegistro = null;

    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 1) {
            embargosTipoEmbargo = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosTipoEmbargo");
            embargosTipoEmbargo.setFilterStyle("display: none; visibility: hidden;");
            embargosDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosDocumento");
            embargosDocumento.setFilterStyle("display: none; visibility: hidden;");
            embargosFechaDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosFechaDocumento");
            embargosFechaDocumento.setFilterStyle("display: none; visibility: hidden;");
            embargosNumero = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosNumero");
            embargosNumero.setFilterStyle("display: none; visibility: hidden;");
            embargosJuzgado = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosJuzgado");
            embargosJuzgado.setFilterStyle("display: none; visibility: hidden;");
            embargosMotivo = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosMotivo");
            embargosMotivo.setFilterStyle("display: none; visibility: hidden;");
            embargosBeneficiario = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosBeneficiario");
            embargosBeneficiario.setFilterStyle("display: none; visibility: hidden;");
            embargosNit = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosNit");
            embargosNit.setFilterStyle("display: none; visibility: hidden;");
            embargosTercero = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosTercero");
            embargosTercero.setFilterStyle("display: none; visibility: hidden;");
            embargosValorTotal = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosValorTotal");
            embargosValorTotal.setFilterStyle("display: none; visibility: hidden;");
            embargosFechaInicio = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosFechaInicio");
            embargosFechaInicio.setFilterStyle("display: none; visibility: hidden;");
            embargosCancelacionDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosCancelacionDocumento");
            embargosCancelacionDocumento.setFilterStyle("display: none; visibility: hidden;");
            embargosCancelacionFecha = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosCancelacionFecha");
            embargosCancelacionFecha.setFilterStyle("display: none; visibility: hidden;");
            embargosObservaciones = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosObservaciones");
            embargosObservaciones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
            altoScrollEmbargos = "90";

            bandera = 0;
            filtradoListaEmbargos = null;
            tipoLista = 0;
        }

        if (bandera == 1) {
            //SOLUCIONES NODOS EMPLEADO
            System.out.println("Desactiva 2");
            detallesPagos = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPagos");
            detallesPagos.setFilterStyle("display: none; visibility: hidden;");
            detallesIka = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesIka");
            detallesIka.setFilterStyle("display: none; visibility: hidden;");
            detallesPorcentaje = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPorcentaje");
            detallesPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            detallesValor = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesValor");
            detallesValor.setFilterStyle("display: none; visibility: hidden;");
            detallesSaldoInicial = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesSaldoInicial");
            detallesSaldoInicial.setFilterStyle("display: none; visibility: hidden;");
            detallesPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPeriodicidadCodigo");
            detallesPeriodicidadCodigo.setFilterStyle("display: none; visibility: hidden;");
            detallesPeriodicidadNombre = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPeriodicidadNombre");
            detallesPeriodicidadNombre.setFilterStyle("display: none; visibility: hidden;");
            detallesConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesConceptoCodigo");
            detallesConceptoCodigo.setFilterStyle("display: none; visibility: hidden;");
            detallesConceptoNombre = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesConceptoNombre");
            detallesConceptoNombre.setFilterStyle("display: none; visibility: hidden;");
            detallesEmpresa = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesEmpresa");
            detallesEmpresa.setFilterStyle("display: none; visibility: hidden;");

            altoScrollDetallesEmbargos = "90";
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
            bandera = 0;
            filtradoListaEmbargos = null;
            tipoListaD = 0;
        }
        listaEmbargosBorrar.clear();
        listaEmbargosCrear.clear();
        listaEmbargosModificar.clear();
        index = -1;
        secRegistro = null;

        listaEmbargos = null;

        listaDetallesEmbargosBorrar.clear();
        listaDetallesEmbargosCrear.clear();
        listaDetallesEmbargosModificar.clear();
        indexD = -1;

        listaDetallesEmbargos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEmbargos");
        RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");

    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            embargosTipoEmbargo = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosTipoEmbargo");
            embargosTipoEmbargo.setFilterStyle("display: none; visibility: hidden;");
            embargosDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosDocumento");
            embargosDocumento.setFilterStyle("display: none; visibility: hidden;");
            embargosFechaDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosFechaDocumento");
            embargosFechaDocumento.setFilterStyle("display: none; visibility: hidden;");
            embargosNumero = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosNumero");
            embargosNumero.setFilterStyle("display: none; visibility: hidden;");
            embargosJuzgado = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosJuzgado");
            embargosJuzgado.setFilterStyle("display: none; visibility: hidden;");
            embargosMotivo = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosMotivo");
            embargosMotivo.setFilterStyle("display: none; visibility: hidden;");
            embargosBeneficiario = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosBeneficiario");
            embargosBeneficiario.setFilterStyle("display: none; visibility: hidden;");
            embargosNit = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosNit");
            embargosNit.setFilterStyle("display: none; visibility: hidden;");
            embargosTercero = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosTercero");
            embargosTercero.setFilterStyle("display: none; visibility: hidden;");
            embargosValorTotal = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosValorTotal");
            embargosValorTotal.setFilterStyle("display: none; visibility: hidden;");
            embargosFechaInicio = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosFechaInicio");
            embargosFechaInicio.setFilterStyle("display: none; visibility: hidden;");
            embargosCancelacionDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosCancelacionDocumento");
            embargosCancelacionDocumento.setFilterStyle("display: none; visibility: hidden;");
            embargosCancelacionFecha = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosCancelacionFecha");
            embargosCancelacionFecha.setFilterStyle("display: none; visibility: hidden;");
            embargosObservaciones = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosObservaciones");
            embargosObservaciones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
            altoScrollEmbargos = "90";

            bandera = 0;
            filtradoListaEmbargos = null;
            tipoLista = 0;
        }

        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            //SOLUCIONES NODOS EMPLEADO
            System.out.println("Desactiva 2");
            detallesPagos = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPagos");
            detallesPagos.setFilterStyle("display: none; visibility: hidden;");
            detallesIka = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesIka");
            detallesIka.setFilterStyle("display: none; visibility: hidden;");
            detallesPorcentaje = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPorcentaje");
            detallesPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            detallesValor = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesValor");
            detallesValor.setFilterStyle("display: none; visibility: hidden;");
            detallesSaldoInicial = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesSaldoInicial");
            detallesSaldoInicial.setFilterStyle("display: none; visibility: hidden;");
            detallesPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPeriodicidadCodigo");
            detallesPeriodicidadCodigo.setFilterStyle("display: none; visibility: hidden;");
            detallesPeriodicidadNombre = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPeriodicidadNombre");
            detallesPeriodicidadNombre.setFilterStyle("display: none; visibility: hidden;");
            detallesConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesConceptoCodigo");
            detallesConceptoCodigo.setFilterStyle("display: none; visibility: hidden;");
            detallesConceptoNombre = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesConceptoNombre");
            detallesConceptoNombre.setFilterStyle("display: none; visibility: hidden;");
            detallesEmpresa = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesEmpresa");
            detallesEmpresa.setFilterStyle("display: none; visibility: hidden;");

            altoScrollDetallesEmbargos = "90";
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
            bandera = 0;
            filtradoListaEmbargos = null;
            tipoListaD = 0;
        }
        listaEmbargosBorrar.clear();
        listaEmbargosCrear.clear();
        listaEmbargosModificar.clear();
        index = -1;
        secRegistro = null;

        listaEmbargos = null;

        listaDetallesEmbargosBorrar.clear();
        listaDetallesEmbargosCrear.clear();
        listaDetallesEmbargosModificar.clear();
        indexD = -1;

        listaDetallesEmbargos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEmbargos");
        RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
    }

    //GUARDAR
    public void guardarCambiosEmbargos() {
        if (cualTabla == 0) {
            System.out.println("Guardado: " + guardado);
            if (guardado == false) {
                System.out.println("Realizando Operaciones Embargos");
                if (!listaEmbargosBorrar.isEmpty()) {
                    for (int i = 0; i < listaEmbargosBorrar.size(); i++) {
                        System.out.println("Borrando...");
                        if (listaEmbargosBorrar.get(i).getTipoembargo() == null) {
                            listaEmbargosBorrar.get(i).setTipoembargo(null);
                        }
                        if (listaEmbargosBorrar.get(i).getCancelaciondocumento() == null) {
                            listaEmbargosBorrar.get(i).setCancelaciondocumento(null);
                        }
                        if (listaEmbargosBorrar.get(i).getCancelacionfechahasta() == null) {
                            listaEmbargosBorrar.get(i).setCancelacionfechahasta(null);
                        }
                        if (listaEmbargosBorrar.get(i).getMotivoembargo() == null) {
                            listaEmbargosBorrar.get(i).setMotivoembargo(null);
                        }
                        if (listaEmbargosBorrar.get(i).getJuzgado() == null) {
                            listaEmbargosBorrar.get(i).setJuzgado(null);
                        }
                        if (listaEmbargosBorrar.get(i).getNumeroproceso() == null) {
                            listaEmbargosBorrar.get(i).setNumeroproceso(null);
                        }

                        administrarNovedadesEmbargos.borrarEmbargo(listaEmbargosBorrar.get(i));

                        System.out.println("Entra");
                        listaEmbargosBorrar.clear();
                    }
                }
                if (!listaEmbargosCrear.isEmpty()) {
                    for (int i = 0; i < listaEmbargosCrear.size(); i++) {
                        System.out.println("Creando...");
                        System.out.println(listaEmbargosCrear.size());
                        if (listaEmbargosCrear.get(i).getTipoembargo() == null) {
                            listaEmbargosCrear.get(i).setTipoembargo(null);
                        }
                        if (listaEmbargosCrear.get(i).getCancelaciondocumento() == null) {
                            listaEmbargosCrear.get(i).setCancelaciondocumento(null);
                        }
                        if (listaEmbargosCrear.get(i).getCancelacionfechahasta() == null) {
                            listaEmbargosCrear.get(i).setCancelacionfechahasta(null);
                        }
                        if (listaEmbargosCrear.get(i).getMotivoembargo() == null) {
                            listaEmbargosCrear.get(i).setMotivoembargo(null);
                        }
                        if (listaEmbargosCrear.get(i).getJuzgado() == null) {
                            listaEmbargosCrear.get(i).setJuzgado(null);
                        }
                        if (listaEmbargosCrear.get(i).getNumeroproceso() == null) {
                            listaEmbargosCrear.get(i).setNumeroproceso(null);
                        }
                        System.out.println("Guardar nuevoEmbargo : " + listaEmbargosCrear.get(i).getTercero().getNombre());
                        System.out.println("Guardar nuevoEmbargo : " + listaEmbargosCrear.get(i).getTercero().getNit());
                        administrarNovedadesEmbargos.crearEmbargo(listaEmbargosCrear.get(i));
                    }

                    System.out.println("LimpiaLista");
                    listaEmbargosCrear.clear();
                }
                if (!listaEmbargosModificar.isEmpty()) {
                    administrarNovedadesEmbargos.modificarEmbargo(listaEmbargosModificar);
                    listaEmbargosModificar.clear();
                }

                System.out.println("Se guardaron los datos con exito");
                listaEmbargos = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosEmbargos");
                guardado = true;
                permitirIndex = true;
                RequestContext.getCurrentInstance().update("form:aceptar");
                //  k = 0;
            }
            System.out.println("Tamaño lista: " + listaEmbargosCrear.size());
            System.out.println("Valor k: " + k);
            index = -1;
            secRegistro = null;

        } else {

            System.out.println("Está en la Tabla de Abajo");

            if (guardado == false) {
                System.out.println("Realizando Operaciones VigenciasNoFormales");
                if (!listaDetallesEmbargosBorrar.isEmpty()) {
                    for (int i = 0; i < listaDetallesEmbargosBorrar.size(); i++) {
                        System.out.println("Borrando...");
                        if (listaDetallesEmbargosBorrar.get(i).getValor() == null) {
                            listaDetallesEmbargosBorrar.get(i).setValor(null);
                        }

                        if (listaDetallesEmbargosBorrar.get(i).getPorcentaje() == null) {
                            listaDetallesEmbargosBorrar.get(i).setPorcentaje(null);
                        }
                        if (listaDetallesEmbargosBorrar.get(i).getSaldoinicial() == null) {
                            listaDetallesEmbargosBorrar.get(i).setSaldoinicial(null);
                        }

                        administrarNovedadesEmbargos.borrarDetalleEmbargo(listaDetallesEmbargosBorrar.get(i));
                    }

                    System.out.println("Entra");
                    listaDetallesEmbargosBorrar.clear();
                }
            }
            if (!listaDetallesEmbargosCrear.isEmpty()) {
                for (int i = 0; i < listaDetallesEmbargosCrear.size(); i++) {
                    System.out.println("Creando...");
                    System.out.println(listaDetallesEmbargosCrear.size());
                    if (listaDetallesEmbargosCrear.get(i).getValor() == null) {
                        listaDetallesEmbargosCrear.get(i).setValor(null);
                    }

                    if (listaDetallesEmbargosCrear.get(i).getPorcentaje() == null) {
                        listaDetallesEmbargosCrear.get(i).setPorcentaje(null);
                    }
                    if (listaDetallesEmbargosCrear.get(i).getSaldoinicial() == null) {
                        listaDetallesEmbargosCrear.get(i).setSaldoinicial(null);
                    }
                    System.out.println("Guardar nuevoDetalleEmbargo VALOR: " + listaDetallesEmbargosCrear.get(i).getValor());
                    System.out.println("Guardar nuevoDetalleEmbargo PORCENTAJE: " + listaDetallesEmbargosCrear.get(i).getPorcentaje());

                    administrarNovedadesEmbargos.crearDetalleEmbargo(listaDetallesEmbargosCrear.get(i));

                }

                System.out.println("LimpiaLista");
                listaDetallesEmbargosCrear.clear();
            }
            if (!listaDetallesEmbargosModificar.isEmpty()) {
                System.out.println("Lista Vigencias No Formales: " + listaDetallesEmbargosModificar.size());
                administrarNovedadesEmbargos.modificarDetalleEmbargo(listaDetallesEmbargosModificar);

                listaDetallesEmbargosModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaDetallesEmbargos = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:aceptar");
            //  k = 0;
        }
        System.out.println("Valor k: " + k);
        indexD = -1;
        secRegistro = null;

    }

    public void validarPorcentaje(int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (nuevoEmbargoDetalle.getPorcentaje() != null) {
                roValor = true;
            }
        } else if (tipoNuevo == 2) {
            if (duplicarEmbargoDetalle.getPorcentaje() != null) {
                roValorD = true;

            }
        }
    }

    public void validarValor(int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (nuevoEmbargoDetalle.getValor() != null) {
                roPorcentaje = true;
            }
        } else if (tipoNuevo == 2) {
            if (duplicarEmbargoDetalle.getValor() != null) {
                roPorcentajeD = true;

            }
        }
    }

//CREAR Embargo
    public void agregarNuevoDetalleEmbargo() {
        int pasa = 0;
        int pasar = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoEmbargoDetalle.getDetalleformadto().getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " * Pago Comprometido \n";
            pasa++;
        }

        if (nuevoEmbargoDetalle.getPeriodicidad().getCodigoStr().equals("0") || nuevoEmbargoDetalle.getPeriodicidad().getCodigoStr() == null) {
            mensajeValidacion = mensajeValidacion + " * Periodicidad\n";
            pasa++;
        }

        if (nuevoEmbargoDetalle.getPorcentaje() != null && nuevoEmbargoDetalle.getValor() != null) {
            nuevoEmbargoDetalle.setPorcentaje(null);
            nuevoEmbargoDetalle.setValor(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:valorporcentaje");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmbargoDetalle");
            PrimefacesContextUI.ejecutar("PF('valorporcentaje').show()");
            pasar++;
        }

        if (nuevoEmbargoDetalle.getPorcentaje() == null && nuevoEmbargoDetalle.getValor() == null) {
            RequestContext.getCurrentInstance().update("formularioDialogos:valorporcentaje");
            PrimefacesContextUI.ejecutar("PF('valorporcentaje').show()");
            pasar++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoEmbargo");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoEmbargo').show()");
        }

        if (pasa == 0 && pasar == 0) {
            if (bandera == 1) {
                //SOLUCIONES NODOS EMPLEADO
                System.out.println("Desactiva 2");
                FacesContext c = FacesContext.getCurrentInstance();
                detallesPagos = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPagos");
                detallesPagos.setFilterStyle("display: none; visibility: hidden;");
                detallesIka = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesIka");
                detallesIka.setFilterStyle("display: none; visibility: hidden;");
                detallesPorcentaje = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPorcentaje");
                detallesPorcentaje.setFilterStyle("display: none; visibility: hidden;");
                detallesValor = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesValor");
                detallesValor.setFilterStyle("display: none; visibility: hidden;");
                detallesSaldoInicial = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesSaldoInicial");
                detallesSaldoInicial.setFilterStyle("display: none; visibility: hidden;");
                detallesPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPeriodicidadCodigo");
                detallesPeriodicidadCodigo.setFilterStyle("display: none; visibility: hidden;");
                detallesPeriodicidadNombre = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPeriodicidadNombre");
                detallesPeriodicidadNombre.setFilterStyle("display: none; visibility: hidden;");
                detallesConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesConceptoCodigo");
                detallesConceptoCodigo.setFilterStyle("display: none; visibility: hidden;");
                detallesConceptoNombre = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesConceptoNombre");
                detallesConceptoNombre.setFilterStyle("display: none; visibility: hidden;");
                detallesEmpresa = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesEmpresa");
                detallesEmpresa.setFilterStyle("display: none; visibility: hidden;");

                altoScrollDetallesEmbargos = "90";
                RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
                bandera = 0;
                filtradoListaEmbargos = null;
                tipoListaD = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoEmbargoDetalle.setSecuencia(l);
            nuevoEmbargoDetalle.setEerprestamo(embargoSeleccionado);

            listaDetallesEmbargosCrear.add(nuevoEmbargoDetalle);
            listaDetallesEmbargos.add(nuevoEmbargoDetalle);
            nuevoEmbargoDetalle = new EersPrestamosDtos();
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroDetallesEmbargos').hide()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {

        listaEmbargos.add(duplicarEmbargo);
        listaEmbargosCrear.add(duplicarEmbargo);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEmbargos");
        index = -1;
        secRegistro = null;
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            embargosTipoEmbargo = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosTipoEmbargo");
            embargosTipoEmbargo.setFilterStyle("display: none; visibility: hidden;");
            embargosDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosDocumento");
            embargosDocumento.setFilterStyle("display: none; visibility: hidden;");
            embargosFechaDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosFechaDocumento");
            embargosFechaDocumento.setFilterStyle("display: none; visibility: hidden;");
            embargosNumero = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosNumero");
            embargosNumero.setFilterStyle("display: none; visibility: hidden;");
            embargosJuzgado = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosJuzgado");
            embargosJuzgado.setFilterStyle("display: none; visibility: hidden;");
            embargosMotivo = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosMotivo");
            embargosMotivo.setFilterStyle("display: none; visibility: hidden;");
            embargosBeneficiario = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosBeneficiario");
            embargosBeneficiario.setFilterStyle("display: none; visibility: hidden;");
            embargosNit = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosNit");
            embargosNit.setFilterStyle("display: none; visibility: hidden;");
            embargosTercero = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosTercero");
            embargosTercero.setFilterStyle("display: none; visibility: hidden;");
            embargosValorTotal = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosValorTotal");
            embargosValorTotal.setFilterStyle("display: none; visibility: hidden;");
            embargosFechaInicio = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosFechaInicio");
            embargosFechaInicio.setFilterStyle("display: none; visibility: hidden;");
            embargosCancelacionDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosCancelacionDocumento");
            embargosCancelacionDocumento.setFilterStyle("display: none; visibility: hidden;");
            embargosCancelacionFecha = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosCancelacionFecha");
            embargosCancelacionFecha.setFilterStyle("display: none; visibility: hidden;");
            embargosObservaciones = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosObservaciones");
            embargosObservaciones.setFilterStyle("display: none; visibility: hidden;");

            altoScrollEmbargos = "90";
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
            bandera = 0;
            filtradoListaEmbargos = null;
            tipoLista = 0;
        }

        duplicarEmbargo = new EersPrestamos();
    }

    public void confirmarDuplicarD() {

        listaDetallesEmbargos.add(duplicarEmbargoDetalle);
        listaDetallesEmbargosCrear.add(duplicarEmbargoDetalle);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
        index = -1;
        secRegistro = null;
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            detallesPagos = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPagos");
            detallesPagos.setFilterStyle("display: none; visibility: hidden;");
            detallesIka = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesIka");
            detallesIka.setFilterStyle("display: none; visibility: hidden;");
            detallesPorcentaje = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPorcentaje");
            detallesPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            detallesValor = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesValor");
            detallesValor.setFilterStyle("display: none; visibility: hidden;");
            detallesSaldoInicial = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesSaldoInicial");
            detallesSaldoInicial.setFilterStyle("display: none; visibility: hidden;");
            detallesPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPeriodicidadCodigo");
            detallesPeriodicidadCodigo.setFilterStyle("display: none; visibility: hidden;");
            detallesPeriodicidadNombre = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPeriodicidadNombre");
            detallesPeriodicidadNombre.setFilterStyle("display: none; visibility: hidden;");
            detallesConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesConceptoCodigo");
            detallesConceptoCodigo.setFilterStyle("display: none; visibility: hidden;");
            detallesConceptoNombre = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesConceptoNombre");
            detallesConceptoNombre.setFilterStyle("display: none; visibility: hidden;");
            detallesEmpresa = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesEmpresa");
            detallesEmpresa.setFilterStyle("display: none; visibility: hidden;");

            altoScrollDetallesEmbargos = "90";
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
            bandera = 0;
            filtradoListaDetallesEmbargos = null;
            tipoListaD = 0;
        }

        duplicarEmbargoDetalle = new EersPrestamosDtos();
    }

    //DUPLICAR EMBARGOS/DETALLES
    public void duplicarE() {
        if (index >= 0 && cualTabla == 0) {
            duplicarEmbargo = new EersPrestamos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarEmbargo.setSecuencia(l);
                duplicarEmbargo.setTipoeer(listaEmbargos.get(index).getTipoeer());
                duplicarEmbargo.setEmpleado(listaEmbargos.get(index).getEmpleado());
                duplicarEmbargo.setTipoembargo(listaEmbargos.get(index).getTipoembargo());
                duplicarEmbargo.setDocumento(listaEmbargos.get(index).getDocumento());
                duplicarEmbargo.setFechadocumento(listaEmbargos.get(index).getFechadocumento());
                duplicarEmbargo.setNumeroproceso(listaEmbargos.get(index).getNumeroproceso());
                duplicarEmbargo.setJuzgado(listaEmbargos.get(index).getJuzgado());
                duplicarEmbargo.setMotivoembargo(listaEmbargos.get(index).getMotivoembargo());
                duplicarEmbargo.setDemandante(listaEmbargos.get(index).getDemandante());
                duplicarEmbargo.setTercero(listaEmbargos.get(index).getTercero());
                duplicarEmbargo.setValortotal(listaEmbargos.get(index).getValortotal());
                duplicarEmbargo.setFechainiciodescuento(listaEmbargos.get(index).getFechainiciodescuento());
                duplicarEmbargo.setFormadto(listaEmbargos.get(index).getFormadto());
                duplicarEmbargo.setCancelaciondocumento(listaEmbargos.get(index).getCancelaciondocumento());
                duplicarEmbargo.setCancelacionfechahasta(listaEmbargos.get(index).getCancelacionfechahasta());
                duplicarEmbargo.setObservaciones(listaEmbargos.get(index).getObservaciones());
            }
            if (tipoLista == 1) {
                duplicarEmbargo.setSecuencia(l);
                duplicarEmbargo.setTipoeer(filtradoListaEmbargos.get(index).getTipoeer());
                duplicarEmbargo.setEmpleado(filtradoListaEmbargos.get(index).getEmpleado());
                duplicarEmbargo.setTipoembargo(filtradoListaEmbargos.get(index).getTipoembargo());
                duplicarEmbargo.setDocumento(filtradoListaEmbargos.get(index).getDocumento());
                duplicarEmbargo.setFechadocumento(filtradoListaEmbargos.get(index).getFechadocumento());
                duplicarEmbargo.setNumeroproceso(filtradoListaEmbargos.get(index).getNumeroproceso());
                duplicarEmbargo.setJuzgado(filtradoListaEmbargos.get(index).getJuzgado());
                duplicarEmbargo.setMotivoembargo(filtradoListaEmbargos.get(index).getMotivoembargo());
                duplicarEmbargo.setDemandante(filtradoListaEmbargos.get(index).getDemandante());
                duplicarEmbargo.setTercero(filtradoListaEmbargos.get(index).getTercero());
                duplicarEmbargo.setValortotal(filtradoListaEmbargos.get(index).getValortotal());
                duplicarEmbargo.setFechainiciodescuento(filtradoListaEmbargos.get(index).getFechainiciodescuento());
                duplicarEmbargo.setFormadto(filtradoListaEmbargos.get(index).getFormadto());
                duplicarEmbargo.setCancelaciondocumento(filtradoListaEmbargos.get(index).getCancelaciondocumento());
                duplicarEmbargo.setCancelacionfechahasta(filtradoListaEmbargos.get(index).getCancelacionfechahasta());
                duplicarEmbargo.setObservaciones(filtradoListaEmbargos.get(index).getObservaciones());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmbargo");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroEmbargos').show()");
            index = -1;
            secRegistro = null;
        } else if (indexD >= 0 && cualTabla == 1) {
            System.out.println("Entra Duplicar Detalle Embargo");

            duplicarEmbargoDetalle = new EersPrestamosDtos();
            m++;
            n = BigInteger.valueOf(m);

            if (tipoListaD == 0) {
                duplicarEmbargoDetalle.setSecuencia(n);
                duplicarEmbargoDetalle.setDetalleformadto(listaDetallesEmbargos.get(indexD).getDetalleformadto());
                //duplicarEmbargoDetalle.getDetalleformadto().getTipodto().setIka(listaDetallesEmbargos.get(indexD).getDetalleformadto().getTipodto().getIka());
                duplicarEmbargoDetalle.setPorcentaje(listaDetallesEmbargos.get(indexD).getPorcentaje());
                duplicarEmbargoDetalle.setValor(listaDetallesEmbargos.get(indexD).getValor());
                duplicarEmbargoDetalle.setSaldoinicial(listaDetallesEmbargos.get(indexD).getSaldoinicial());
                duplicarEmbargoDetalle.setPeriodicidad(listaDetallesEmbargos.get(indexD).getPeriodicidad());
                //duplicarEmbargoDetalle.getDetalleformadto().setConcepto(listaDetallesEmbargos.get(indexD).getDetalleformadto().getConcepto());
            }
            if (tipoListaD == 1) {
                duplicarEmbargoDetalle.setSecuencia(n);
                duplicarEmbargoDetalle.setDetalleformadto(filtradoListaDetallesEmbargos.get(indexD).getDetalleformadto());
                //duplicarEmbargoDetalle.getDetalleformadto().getTipodto().setIka(filtradoListaDetallesEmbargos.get(indexD).getDetalleformadto().getTipodto().getIka());
                duplicarEmbargoDetalle.setPorcentaje(filtradoListaDetallesEmbargos.get(indexD).getPorcentaje());
                duplicarEmbargoDetalle.setValor(filtradoListaDetallesEmbargos.get(indexD).getValor());
                duplicarEmbargoDetalle.setSaldoinicial(filtradoListaDetallesEmbargos.get(indexD).getSaldoinicial());
                duplicarEmbargoDetalle.setPeriodicidad(filtradoListaDetallesEmbargos.get(indexD).getPeriodicidad());
                //duplicarEmbargoDetalle.getDetalleformadto().setConcepto(filtradoListaDetallesEmbargos.get(indexD).getDetalleformadto().getConcepto());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmbargoDetalle");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroDetallesEmbargos').show()");
            indexD = -1;
            secRegistro = null;

        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0 && cualTabla == 0) {
            if (tipoLista == 0) {
                editarEmbargos = listaEmbargos.get(index);
            }
            if (tipoLista == 1) {
                editarEmbargos = filtradoListaEmbargos.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoEmbargoE");
                PrimefacesContextUI.ejecutar("PF('editarTipoEmbargoE').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNumeroOficioE");
                PrimefacesContextUI.ejecutar("PF('editarNumeroOficioE').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaOficioE");
                PrimefacesContextUI.ejecutar("PF('editarFechaOficioE').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNumeroProcesoE");
                PrimefacesContextUI.ejecutar("PF('editarNumeroProcesoE').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarJuzgadoE");
                PrimefacesContextUI.ejecutar("PF('editarJuzgadoE').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoEmbargoE");
                PrimefacesContextUI.ejecutar("PF('editarMotivoEmbargoE').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreDemandanteE");
                PrimefacesContextUI.ejecutar("PF('editarNombreDemandanteE').show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceroNITE");
                PrimefacesContextUI.ejecutar("PF('editarTerceroNITE').show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceroNombreE");
                PrimefacesContextUI.ejecutar("PF('editarTerceroNombreE').show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorE");
                PrimefacesContextUI.ejecutar("PF('editarValorE').show()");
                cualCelda = -1;
            } else if (cualCelda == 10) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialDescuentoE");
                PrimefacesContextUI.ejecutar("PF('editarFechaInicialDescuentoE').show()");
                cualCelda = -1;
            } else if (cualCelda == 11) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFormaDtoE");
                PrimefacesContextUI.ejecutar("PF('editarFormaDtoE').show()");
                cualCelda = -1;
            } else if (cualCelda == 12) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreOficioE");
                PrimefacesContextUI.ejecutar("PF('editarNombreOficioE').show()");
                cualCelda = -1;
            } else if (cualCelda == 13) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaCancelacionE");
                PrimefacesContextUI.ejecutar("PF('editarFechaCancelacionE').show()");
                cualCelda = -1;
            } else if (cualCelda == 14) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacionesE");
                PrimefacesContextUI.ejecutar("PF('editarObservacionesE').show()");
                cualCelda = -1;
            }
            index = -1;
        } else if (indexD >= 0 && cualTabla == 1) {
            if (tipoListaD == 0) {
                editarDetallesEmbargos = listaDetallesEmbargos.get(indexD);
            }
            if (tipoListaD == 1) {
                editarDetallesEmbargos = filtradoListaDetallesEmbargos.get(indexD);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCeldaD);
            System.out.println("Cual Tabla: " + cualTabla);
            if (cualCeldaD == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPagosD");
                PrimefacesContextUI.ejecutar("PF('editarPagosD').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarIkaD");
                PrimefacesContextUI.ejecutar("PF('editarIkaD').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPorcentajeD");
                PrimefacesContextUI.ejecutar("PF('editarPorcentajeD').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorD");
                PrimefacesContextUI.ejecutar("PF('editarValorD').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSaldoD");
                PrimefacesContextUI.ejecutar("PF('editarSaldoD').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPeriodicidadCodigoD");
                PrimefacesContextUI.ejecutar("PF('editarPeriodicidadCodigoD').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPeriodicidadNombreD");
                PrimefacesContextUI.ejecutar("PF('editarPeriodicidadNombreD').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoCodigoD");
                PrimefacesContextUI.ejecutar("PF('editarConceptoCodigoD').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoNombreD");
                PrimefacesContextUI.ejecutar("PF('editarConceptoNombreD').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaD");
                PrimefacesContextUI.ejecutar("PF('editarEmpresaD').show()");
                cualCeldaD = -1;
            }
            indexD = -1;
        }
        secRegistro = null;
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        System.out.println("cualTabla= " + cualTabla);
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0 && cualTabla == 0) {
            System.out.println("Activa 1");
            //SOLUCIONES NODOS EMPLEADO
            embargosTipoEmbargo = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosTipoEmbargo");
            embargosTipoEmbargo.setFilterStyle("width: 85% !important;");
            embargosDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosDocumento");
            embargosDocumento.setFilterStyle("width: 85% !important;");
            embargosFechaDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosFechaDocumento");
            embargosFechaDocumento.setFilterStyle("width: 85% !important;");
            embargosNumero = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosNumero");
            embargosNumero.setFilterStyle("width: 85% !important;");
            embargosJuzgado = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosJuzgado");
            embargosJuzgado.setFilterStyle("width: 85% !important;");
            embargosMotivo = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosMotivo");
            embargosMotivo.setFilterStyle("width: 85% !important;");
            embargosBeneficiario = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosBeneficiario");
            embargosBeneficiario.setFilterStyle("width: 85% !important;");
            embargosNit = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosNit");
            embargosNit.setFilterStyle("width: 50px");
            embargosTercero = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosTercero");
            embargosTercero.setFilterStyle("width: 85% !important;");
            embargosValorTotal = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosValorTotal");
            embargosValorTotal.setFilterStyle("width: 50px");
            embargosFechaInicio = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosFechaInicio");
            embargosFechaInicio.setFilterStyle("width: 85% !important;");
            embargosCancelacionDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosCancelacionDocumento");
            embargosCancelacionDocumento.setFilterStyle("width: 85% !important;");
            embargosCancelacionFecha = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosCancelacionFecha");
            embargosCancelacionFecha.setFilterStyle("width: 85% !important;");
            embargosObservaciones = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosObservaciones");
            embargosObservaciones.setFilterStyle("width: 85% !important;");

            altoScrollEmbargos = "70";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
            bandera = 1;

        } else if (bandera == 1 && cualTabla == 0) {
            System.out.println("Desactiva 1");
            embargosTipoEmbargo = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosTipoEmbargo");
            embargosTipoEmbargo.setFilterStyle("display: none; visibility: hidden;");
            embargosDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosDocumento");
            embargosDocumento.setFilterStyle("display: none; visibility: hidden;");
            embargosFechaDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosFechaDocumento");
            embargosFechaDocumento.setFilterStyle("display: none; visibility: hidden;");
            embargosNumero = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosNumero");
            embargosNumero.setFilterStyle("display: none; visibility: hidden;");
            embargosJuzgado = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosJuzgado");
            embargosJuzgado.setFilterStyle("display: none; visibility: hidden;");
            embargosMotivo = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosMotivo");
            embargosMotivo.setFilterStyle("display: none; visibility: hidden;");
            embargosBeneficiario = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosBeneficiario");
            embargosBeneficiario.setFilterStyle("display: none; visibility: hidden;");
            embargosNit = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosNit");
            embargosNit.setFilterStyle("display: none; visibility: hidden;");
            embargosTercero = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosTercero");
            embargosTercero.setFilterStyle("display: none; visibility: hidden;");
            embargosValorTotal = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosValorTotal");
            embargosValorTotal.setFilterStyle("display: none; visibility: hidden;");
            embargosFechaInicio = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosFechaInicio");
            embargosFechaInicio.setFilterStyle("display: none; visibility: hidden;");
            embargosCancelacionDocumento = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosCancelacionDocumento");
            embargosCancelacionDocumento.setFilterStyle("display: none; visibility: hidden;");
            embargosCancelacionFecha = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosCancelacionFecha");
            embargosCancelacionFecha.setFilterStyle("display: none; visibility: hidden;");
            embargosObservaciones = (Column) c.getViewRoot().findComponent("form:datosEmbargos:embargosObservaciones");
            embargosObservaciones.setFilterStyle("display: none; visibility: hidden;");

            altoScrollEmbargos = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
            bandera = 0;
            filtradoListaEmbargos = null;

        } else if (bandera == 0 && cualTabla == 1) {
            System.out.println("Activa 2");

            detallesPagos = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPagos");
            detallesPagos.setFilterStyle("width: 85% !important;");
            detallesIka = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesIka");
            detallesIka.setFilterStyle("width: 85% !important;");
            detallesPorcentaje = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPorcentaje");
            detallesPorcentaje.setFilterStyle("width: 85% !important;");
            detallesValor = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesValor");
            detallesValor.setFilterStyle("");
            detallesSaldoInicial = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesSaldoInicial");
            detallesSaldoInicial.setFilterStyle("");
            detallesPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPeriodicidadCodigo");
            detallesPeriodicidadCodigo.setFilterStyle("width: 85% !important;");
            detallesPeriodicidadNombre = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPeriodicidadNombre");
            detallesPeriodicidadNombre.setFilterStyle("width: 85% !important;");
            detallesConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesConceptoCodigo");
            detallesConceptoCodigo.setFilterStyle("width: 50px");
            detallesConceptoNombre = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesConceptoNombre");
            detallesConceptoNombre.setFilterStyle("width: 85% !important;");
            detallesEmpresa = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesEmpresa");
            detallesEmpresa.setFilterStyle("width: 50px");

            altoScrollDetallesEmbargos = "66";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
            bandera = 1;
            tipoListaD = 1;

        } else if (bandera == 1 && cualTabla == 1) {
            //SOLUCIONES NODOS EMPLEADO
            System.out.println("Desactiva 2");
            detallesPagos = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPagos");
            detallesPagos.setFilterStyle("display: none; visibility: hidden;");
            detallesIka = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesIka");
            detallesIka.setFilterStyle("display: none; visibility: hidden;");
            detallesPorcentaje = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPorcentaje");
            detallesPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            detallesValor = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesValor");
            detallesValor.setFilterStyle("display: none; visibility: hidden;");
            detallesSaldoInicial = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesSaldoInicial");
            detallesSaldoInicial.setFilterStyle("display: none; visibility: hidden;");
            detallesPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPeriodicidadCodigo");
            detallesPeriodicidadCodigo.setFilterStyle("display: none; visibility: hidden;");
            detallesPeriodicidadNombre = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesPeriodicidadNombre");
            detallesPeriodicidadNombre.setFilterStyle("display: none; visibility: hidden;");
            detallesConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesConceptoCodigo");
            detallesConceptoCodigo.setFilterStyle("display: none; visibility: hidden;");
            detallesConceptoNombre = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesConceptoNombre");
            detallesConceptoNombre.setFilterStyle("display: none; visibility: hidden;");
            detallesEmpresa = (Column) c.getViewRoot().findComponent("form:datosEmbargosDetalles:detallesEmpresa");
            detallesEmpresa.setFilterStyle("display: none; visibility: hidden;");

            altoScrollDetallesEmbargos = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
            bandera = 0;
            tipoListaD = 0;
            filtradoListaDetallesEmbargos = null;
        }
    }

    public void mostarTodosEmpleados() {
        registroActual = 0;
        listaEmpleados = null;
        getListaEmpleados();
        if (!listaEmpleados.isEmpty()) {
            empleadoActual = listaEmpleados.get(0);
        }
        registroActual = 0;
        embargoSeleccionado = null;
        aceptar = true;
        listaEmbargos = null;
        getListaEmbargos();
        listaDetallesEmbargos = null;
        mostrarTodos = true;
        getListaDetallesEmbargos();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:panelInf");
        RequestContext.getCurrentInstance().update("form:datosSolucionesNodosEmpleado");
        RequestContext.getCurrentInstance().update("form:datosSolucionesNodosEmpleador");
    }

    public void anteriorEmpleado() {
        if (registroActual > 0) {
            registroActual--;
            empleadoActual = listaEmpleados.get(registroActual);
            System.out.println("Empleado Actual: " + empleadoActual.getSecuencia() + empleadoActual.getPersona().getNombreCompleto());
            listaEmbargos = null;
            getListaEmbargos();
            if (!listaEmbargos.isEmpty()) {
                embargoSeleccionado = listaEmbargos.get(0);
            } else {
                embargoSeleccionado = null;
            }
            listaDetallesEmbargos = null;
            getListaDetallesEmbargos();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:panelInf");
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
        }
    }

    public void siguienteEmpleado() {
        if (registroActual < (listaEmpleados.size() - 1)) {
            registroActual++;
            empleadoActual = listaEmpleados.get(registroActual);
            System.out.println("Empleado Actual: " + empleadoActual.getSecuencia() + empleadoActual.getPersona().getNombreCompleto());
            listaEmbargos = null;
            getListaEmbargos();
            if (!listaEmbargos.isEmpty()) {
                embargoSeleccionado = listaEmbargos.get(0);
            } else {
                embargoSeleccionado = null;
            }
            listaDetallesEmbargos = null;
            getListaDetallesEmbargos();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:panelInf");
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void chiste() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (!listaEmbargos.isEmpty() && listaDetallesEmbargos.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
            PrimefacesContextUI.ejecutar("PF('elegirTabla').show()");
        }
        int tamaño = listaEmbargos.size();

        if (tamaño == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroEmbargos");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroEmbargos').show()");
        }

        if (listaDetallesEmbargos.isEmpty() && !listaEmbargos.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
            PrimefacesContextUI.ejecutar("PF('elegirTabla').show()");
        } else if (cualTabla == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroEmbargos");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroEmbargos').show()");
        } else if (cualTabla == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroDetallesEmbargos");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroDetallesEmbargos').show()");
        }
    }

    public void dialogoEmbargos() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:NuevoRegistroEmbargos");
        PrimefacesContextUI.ejecutar("PF('NuevoRegistroEmbargos').show()");
    }

    public void dialogoDetallesEmbargos() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:NuevoRegistroDetallesEmbargos");
        PrimefacesContextUI.ejecutar("PF('NuevoRegistroDetallesEmbargos').show()");
    }

    public void seleccionarEmpleado() {
        listaEmpleados.clear();
        listaEmpleados.add(empleadoSeleccionado);
        empleadoActual = empleadoSeleccionado;
        registroActual = 0;
        filtradoListaEmpleadosLOV = null;
        empleadoSeleccionado = null;
        embargoSeleccionado = null;
        aceptar = true;
        mostrarTodos = false;
        listaEmbargos = null;
        listaDetallesEmbargos = null;
        getListaEmbargos();
        getListaDetallesEmbargos();
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpleados:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleados').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('buscarEmpleadoDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:panelInf");
        RequestContext.getCurrentInstance().update("form:datosEmbargos");
        RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
    }

    public void actualizarDetallesFormasDtos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaD == 0) {
                listaDetallesEmbargos.get(indexD).setDetalleformadto(detallesFormasDtosSeleccionado);
                if (!listaDetallesEmbargosCrear.contains(listaDetallesEmbargos.get(indexD))) {
                    if (listaDetallesEmbargosModificar.isEmpty()) {
                        listaDetallesEmbargosModificar.add(listaDetallesEmbargos.get(indexD));
                    } else if (!listaDetallesEmbargosModificar.contains(listaDetallesEmbargos.get(indexD))) {
                        listaDetallesEmbargosModificar.add(listaDetallesEmbargos.get(indexD));
                    }
                }
            } else {
                filtradoListaDetallesEmbargos.get(indexD).setDetalleformadto(detallesFormasDtosSeleccionado);
                if (!listaDetallesEmbargosCrear.contains(filtradoListaDetallesEmbargos.get(indexD))) {
                    if (listaDetallesEmbargosModificar.isEmpty()) {
                        listaDetallesEmbargosModificar.add(filtradoListaDetallesEmbargos.get(indexD));
                    } else if (!listaDetallesEmbargosModificar.contains(filtradoListaDetallesEmbargos.get(indexD))) {
                        listaDetallesEmbargosModificar.add(filtradoListaDetallesEmbargos.get(indexD));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
        } else if (tipoActualizacion == 1) {
            nuevoEmbargoDetalle.setDetalleformadto(detallesFormasDtosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmbargoDetalle");
        } else if (tipoActualizacion == 2) {
            duplicarEmbargoDetalle.setDetalleformadto(detallesFormasDtosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmbargoDetalle");
        }
        lovfiltradoslistaDetallesFormasDtos = null;
        detallesFormasDtosSeleccionado = null;
        aceptar = true;
        indexD = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        context.reset("formularioDialogos:LOVFormas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVFormas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('formasDescuentosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVFormas");
    }

    public void cancelarCambioDetallesFormasDtos() {
        lovfiltradoslistaDetallesFormasDtos = null;
        detallesFormasDtosSeleccionado = null;
        aceptar = true;
        indexD = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVFormas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVFormas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('formasDescuentosDialogo').hide()");
    }

    public void actualizarTipoEmbargo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaEmbargos.get(index).setTipoembargo(tiposEmbargosSeleccionado);
                if (!listaEmbargosCrear.contains(listaEmbargos.get(index))) {
                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(listaEmbargos.get(index))) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    }
                }
            } else {
                filtradoListaEmbargos.get(index).setTipoembargo(tiposEmbargosSeleccionado);
                if (!listaEmbargosCrear.contains(filtradoListaEmbargos.get(index))) {
                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(filtradoListaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(filtradoListaEmbargos.get(index))) {
                        listaEmbargosModificar.add(filtradoListaEmbargos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
        } else if (tipoActualizacion == 1) {
            nuevoEmbargo.setTipoembargo(tiposEmbargosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmbargo");
        } else if (tipoActualizacion == 2) {
            duplicarEmbargo.setTipoembargo(tiposEmbargosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmbargo");
        }
        lovfiltradoslistaTiposEmbargos = null;
        tiposEmbargosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        context.reset("formularioDialogos:LOVTiposEmbargos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVTiposEmbargos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('tiposEmbargosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposEmbargos");
    }

    public void cancelarCambioTiposEmbargos() {
        lovfiltradoslistaTiposEmbargos = null;
        tiposEmbargosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTiposEmbargos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVTiposEmbargos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('tiposEmbargosDialogo').hide()");
    }

    public void actualizarJuzgado() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaEmbargos.get(index).setJuzgado(juzgadosSeleccionado);
                if (!listaEmbargosCrear.contains(listaEmbargos.get(index))) {
                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(listaEmbargos.get(index))) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    }
                }
            } else {
                filtradoListaEmbargos.get(index).setJuzgado(juzgadosSeleccionado);
                if (!listaEmbargosCrear.contains(filtradoListaEmbargos.get(index))) {
                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(filtradoListaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(filtradoListaEmbargos.get(index))) {
                        listaEmbargosModificar.add(filtradoListaEmbargos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
        } else if (tipoActualizacion == 1) {
            nuevoEmbargo.setJuzgado(juzgadosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmbargo");
        } else if (tipoActualizacion == 2) {
            duplicarEmbargo.setJuzgado(juzgadosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmbargo");
        }
        lovfiltradoslistaJuzgados = null;
        juzgadosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        context.reset("formularioDialogos:LOVJuzgados:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVJuzgados').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('juzgadosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVJuzgados");
    }

    public void cancelarCambioJuzgados() {
        lovfiltradoslistaJuzgados = null;
        juzgadosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVJuzgados:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVJuzgados').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('juzgadosDialogo').hide()");
    }

    public void actualizarPeriodicidades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaDetallesEmbargos.get(indexD).setPeriodicidad(periodicidadesSeleccionado);
                if (!listaDetallesEmbargosCrear.contains(listaDetallesEmbargos.get(indexD))) {
                    if (listaDetallesEmbargosModificar.isEmpty()) {
                        listaDetallesEmbargosModificar.add(listaDetallesEmbargos.get(indexD));
                    } else if (!listaDetallesEmbargosModificar.contains(listaDetallesEmbargos.get(indexD))) {
                        listaDetallesEmbargosModificar.add(listaDetallesEmbargos.get(indexD));
                    }
                }
            } else {
                filtradoListaDetallesEmbargos.get(indexD).setPeriodicidad(periodicidadesSeleccionado);
                if (!listaDetallesEmbargosCrear.contains(filtradoListaDetallesEmbargos.get(indexD))) {
                    if (listaDetallesEmbargosModificar.isEmpty()) {
                        listaDetallesEmbargosModificar.add(filtradoListaDetallesEmbargos.get(indexD));
                    } else if (!listaDetallesEmbargosModificar.contains(filtradoListaDetallesEmbargos.get(indexD))) {
                        listaDetallesEmbargosModificar.add(filtradoListaDetallesEmbargos.get(indexD));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
        } else if (tipoActualizacion == 1) {
            nuevoEmbargoDetalle.setPeriodicidad(periodicidadesSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmbargoDetalle");
        } else if (tipoActualizacion == 2) {
            duplicarEmbargoDetalle.setPeriodicidad(periodicidadesSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmbargoDetalle");
        }
        lovfiltradoslistaPeriodicidades = null;
        periodicidadesSeleccionado = null;
        aceptar = true;
        indexD = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        context.reset("formularioDialogos:LOVPeriodicidades:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVPeriodicidades').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVPeriodicidades");
    }

    public void cancelarCambioPeriodicidades() {
        lovfiltradoslistaPeriodicidades = null;
        periodicidadesSeleccionado = null;
        aceptar = true;
        indexD = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVPeriodicidades:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVPeriodicidades').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').hide()");
    }

    public void actualizarMotivoEmbargo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaEmbargos.get(index).setMotivoembargo(motivosSeleccionado);
                if (!listaEmbargosCrear.contains(listaEmbargos.get(index))) {
                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(listaEmbargos.get(index))) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    }
                }
            } else {
                filtradoListaEmbargos.get(index).setMotivoembargo(motivosSeleccionado);
                if (!listaEmbargosCrear.contains(filtradoListaEmbargos.get(index))) {
                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(filtradoListaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(filtradoListaEmbargos.get(index))) {
                        listaEmbargosModificar.add(filtradoListaEmbargos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
        } else if (tipoActualizacion == 1) {
            nuevoEmbargo.setMotivoembargo(motivosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmbargo");
        } else if (tipoActualizacion == 2) {
            duplicarEmbargo.setMotivoembargo(motivosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmbargo");
        }
        lovfiltradoslistaTiposEmbargos = null;
        motivosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        context.reset("formularioDialogos:LOVTiposEmbargos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVMotivosEmbargos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('motivosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposEmbargos");
    }

    public void cancelarCambioMotivosEmbargos() {
        lovfiltradoslistaTiposEmbargos = null;
        motivosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTiposEmbargos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVMotivosEmbargos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('motivosDialogo').hide()");
    }

    public void actualizarTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaEmbargos.get(index).setTercero(tercerosSeleccionado);
                if (!listaEmbargosCrear.contains(listaEmbargos.get(index))) {
                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(listaEmbargos.get(index))) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    }
                }
            } else {
                filtradoListaEmbargos.get(index).setTercero(tercerosSeleccionado);
                if (!listaEmbargosCrear.contains(filtradoListaEmbargos.get(index))) {
                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(filtradoListaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(filtradoListaEmbargos.get(index))) {
                        listaEmbargosModificar.add(filtradoListaEmbargos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
        } else if (tipoActualizacion == 1) {
            nuevoEmbargo.setTercero(tercerosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmbargo");
            System.out.println("nuevoEmbargo : " + nuevoEmbargo.getTercero().getNombre());
            System.out.println("nuevoEmbargo : " + nuevoEmbargo.getTercero().getNit());
        } else if (tipoActualizacion == 2) {
            duplicarEmbargo.setTercero(tercerosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmbargo");
        }
        lovfiltradoslistaTerceros = null;
        tercerosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        context.reset("formularioDialogos:LOVTerceros:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVTerceros').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('tercerosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVTerceros");
    }

    public void cancelarCambioTerceros() {
        lovfiltradoslistaTerceros = null;
        tercerosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTerceros:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVTerceros').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('tercerosDialogo').hide()");
    }

    public void actualizarFormasDtos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaEmbargos.get(index).setFormadto(formasDtosSeleccionado);
                if (!listaEmbargosCrear.contains(listaEmbargos.get(index))) {
                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(listaEmbargos.get(index))) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    }
                }
            } else {
                filtradoListaEmbargos.get(index).setFormadto(formasDtosSeleccionado);
                if (!listaEmbargosCrear.contains(filtradoListaEmbargos.get(index))) {
                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(filtradoListaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(filtradoListaEmbargos.get(index))) {
                        listaEmbargosModificar.add(filtradoListaEmbargos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
        } else if (tipoActualizacion == 1) {
            nuevoEmbargo.setFormadto(formasDtosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmbargo");
        } else if (tipoActualizacion == 2) {
            duplicarEmbargo.setFormadto(formasDtosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmbargo");
        }
        lovfiltradoslistaFormasDtos = null;
        formasDtosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        context.reset("formularioDialogos:LOVFormasDtos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVFormasDtos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('formasDtosDialogo').hide()");

    }

    public void cancelarCambioFormasDtos() {
        lovfiltradoslistaFormasDtos = null;
        formasDtosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVFormasDtos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVFormasDtos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('formasDtosDialogo').hide()");
    }

    public void actualizarDemandantes() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaEmbargos.get(index).setDemandante(demandantesSeleccionado);
                if (!listaEmbargosCrear.contains(listaEmbargos.get(index))) {
                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(listaEmbargos.get(index))) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    }
                }
            } else {
                filtradoListaEmbargos.get(index).setDemandante(demandantesSeleccionado);
                if (!listaEmbargosCrear.contains(filtradoListaEmbargos.get(index))) {
                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(filtradoListaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(filtradoListaEmbargos.get(index))) {
                        listaEmbargosModificar.add(filtradoListaEmbargos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
        } else if (tipoActualizacion == 1) {
            nuevoEmbargo.setDemandante(demandantesSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmbargo");
        } else if (tipoActualizacion == 2) {
            duplicarEmbargo.setDemandante(demandantesSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmbargo");
        }
        lovfiltradoslistaDemandantes = null;
        demandantesSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        context.reset("formularioDialogos:LOVDemandantes:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVDemandantes').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('demandantesDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVDemandantes");
    }

    public void cancelarCambioDemandantes() {
        lovfiltradoslistaDemandantes = null;
        demandantesSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cualCeldaD = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVDemandantes:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVDemandantes').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('demandantesDialogo').hide()");
    }

    //AUTOCOMPLETAR
    public void modificarEmbargos(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaEmbargosCrear.contains(listaEmbargos.get(index))) {

                    if (listaEmbargosModificar.isEmpty()) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    } else if (!listaEmbargosModificar.contains(listaEmbargos.get(index))) {
                        listaEmbargosModificar.add(listaEmbargos.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }

                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaEmbargosCrear.contains(filtradoListaEmbargos.get(index))) {

                    if (listaEmbargosCrear.isEmpty()) {
                        listaEmbargosCrear.add(filtradoListaEmbargos.get(index));
                    } else if (!listaEmbargosCrear.contains(filtradoListaEmbargos.get(index))) {
                        listaEmbargosCrear.add(filtradoListaEmbargos.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }

                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosEmbargos");
        } else if (confirmarCambio.equalsIgnoreCase("TIPO")) {
            if (tipoLista == 0) {
                listaEmbargos.get(indice).getTipoembargo().setDescripcion(TipoEmbargo);
            } else {
                filtradoListaEmbargos.get(indice).getTipoembargo().setDescripcion(TipoEmbargo);
            }

            for (int i = 0; i < lovlistaTiposEmbargos.size(); i++) {
                if (lovlistaTiposEmbargos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaEmbargos.get(indice).setTipoembargo(lovlistaTiposEmbargos.get(indiceUnicoElemento));
                } else {
                    filtradoListaEmbargos.get(indice).setTipoembargo(lovlistaTiposEmbargos.get(indiceUnicoElemento));
                }
                lovlistaTiposEmbargos.clear();
                getLovlistaTiposEmbargos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposEmbargosDialogo");
                PrimefacesContextUI.ejecutar("PF('tiposEmbargosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("JUZGADO")) {
            if (tipoLista == 0) {
                listaEmbargos.get(indice).getJuzgado().setNombre(Juzgado);
            } else {
                filtradoListaEmbargos.get(indice).getJuzgado().setNombre(Juzgado);
            }

            for (int i = 0; i < lovlistaJuzgados.size(); i++) {
                if (lovlistaJuzgados.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaEmbargos.get(indice).setJuzgado(lovlistaJuzgados.get(indiceUnicoElemento));
                } else {
                    filtradoListaEmbargos.get(indice).setJuzgado(lovlistaJuzgados.get(indiceUnicoElemento));
                }
                lovlistaJuzgados.clear();
                getLovlistaJuzgados();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:juzgadosDialogo");
                PrimefacesContextUI.ejecutar("PF('juzgadosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("MOTIVO")) {
            if (tipoLista == 0) {
                listaEmbargos.get(indice).getMotivoembargo().setNombre(Motivo);
            } else {
                filtradoListaEmbargos.get(indice).getMotivoembargo().setNombre(Motivo);
            }

            for (int i = 0; i < lovlistaMotivos.size(); i++) {
                if (lovlistaMotivos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaEmbargos.get(indice).setMotivoembargo(lovlistaMotivos.get(indiceUnicoElemento));
                } else {
                    filtradoListaEmbargos.get(indice).setMotivoembargo(lovlistaMotivos.get(indiceUnicoElemento));
                }
                lovlistaMotivos.clear();
                getLovlistaMotivos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:motivosDialogo");
                PrimefacesContextUI.ejecutar("PF('motivosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("DEMANDANTE")) {
            if (tipoLista == 0) {
                listaEmbargos.get(indice).getDemandante().setNombre(Demandante);
            } else {
                filtradoListaEmbargos.get(indice).getDemandante().setNombre(Demandante);
            }

            for (int i = 0; i < lovlistaDemandantes.size(); i++) {
                if (lovlistaDemandantes.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaEmbargos.get(indice).setDemandante(lovlistaDemandantes.get(indiceUnicoElemento));
                } else {
                    filtradoListaEmbargos.get(indice).setDemandante(lovlistaDemandantes.get(indiceUnicoElemento));
                }
                lovlistaMotivos.clear();
                getLovlistaMotivos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:demandantesDialogo");
                PrimefacesContextUI.ejecutar("PF('demandantesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (tipoLista == 0) {
                listaEmbargos.get(indice).getTercero().setStrNit(Nit);
            } else {
                filtradoListaEmbargos.get(indice).getTercero().setStrNit(Nit);
            }

            for (int i = 0; i < listaEmbargos.size(); i++) {
                Long valorcillo;
                valorcillo = listaEmbargos.get(i).getTercero().getNit();
                String valor;
                valor = valorcillo.toString();

                if (valor.startsWith(valorConfirmar.toString())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaEmbargos.get(indice).setTercero(lovlistaTerceros.get(indiceUnicoElemento));
                } else {
                    filtradoListaEmbargos.get(indice).setDemandante(lovlistaTerceros.get(indiceUnicoElemento));
                }
                lovlistaMotivos.clear();
                getLovlistaMotivos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
                PrimefacesContextUI.ejecutar("PF('tercerosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    //AUTOCOMPLETAR Detalles
    public void modificarDetallesEmbargos(int indiceD, String confirmarCambio, String valorConfirmar) {
        indexD = indiceD;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaDetallesEmbargosCrear.contains(listaDetallesEmbargos.get(indexD))) {

                    if (listaDetallesEmbargosModificar.isEmpty()) {
                        listaDetallesEmbargosModificar.add(listaDetallesEmbargos.get(indexD));
                    } else if (!listaDetallesEmbargosModificar.contains(listaDetallesEmbargos.get(indexD))) {
                        listaDetallesEmbargosModificar.add(listaDetallesEmbargos.get(indexD));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }

                }
                indexD = -1;
                secRegistro = null;
            } else {
                if (!listaDetallesEmbargosCrear.contains(filtradoListaDetallesEmbargos.get(indexD))) {

                    if (listaDetallesEmbargosCrear.isEmpty()) {
                        listaDetallesEmbargosCrear.add(filtradoListaDetallesEmbargos.get(indexD));
                    } else if (!listaDetallesEmbargosCrear.contains(filtradoListaDetallesEmbargos.get(indexD))) {
                        listaDetallesEmbargosCrear.add(filtradoListaDetallesEmbargos.get(indexD));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }

                }
                indexD = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosEmbargosDetalles");
        } else if (confirmarCambio.equalsIgnoreCase("TIPO")) {
            if (tipoLista == 0) {
                listaDetallesEmbargos.get(indexD).getDetalleformadto().setDescripcion(Pago);
            } else {
                filtradoListaDetallesEmbargos.get(indexD).getDetalleformadto().setDescripcion(Pago);
            }

            for (int i = 0; i < lovlistaDetallesFormasDtos.size(); i++) {
                if (lovlistaDetallesFormasDtos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaDetallesEmbargos.get(indexD).setDetalleformadto(lovlistaDetallesFormasDtos.get(indiceUnicoElemento));
                } else {
                    filtradoListaDetallesEmbargos.get(indexD).setDetalleformadto(lovlistaDetallesFormasDtos.get(indiceUnicoElemento));
                }
                lovlistaDetallesFormasDtos.clear();
                getLovlistaDetallesFormasDtos();
            } else {

                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:formasDescuentosDialogo");
                PrimefacesContextUI.ejecutar("PF('formasDescuentosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("PERIODICIDAD")) {
            if (tipoLista == 0) {
                listaDetallesEmbargos.get(indexD).getPeriodicidad().setCodigoStr(Periodicidad);
            } else {
                filtradoListaDetallesEmbargos.get(indexD).getPeriodicidad().setCodigoStr(Periodicidad);
            }

            for (int i = 0; i < lovlistaPeriodicidades.size(); i++) {
                if (lovlistaPeriodicidades.get(i).getCodigoStr().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaDetallesEmbargos.get(indexD).setPeriodicidad(lovlistaPeriodicidades.get(indiceUnicoElemento));
                } else {
                    filtradoListaDetallesEmbargos.get(indexD).setPeriodicidad(lovlistaPeriodicidades.get(indiceUnicoElemento));
                }
                lovlistaPeriodicidades.clear();
                getLovlistaPeriodicidades();
            } else {

                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
                PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
                tipoActualizacion = 0;
            }
        }

    }

    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            cualTabla = 0;
            tablaImprimir = ":formExportar:datosEmbargosExportar";
            nombreArchivo = "NovedadesEmbargosXML";
            System.out.println("CualTabla = " + cualTabla);
            embargoSeleccionado = listaEmbargos.get(index);
            cambiarEmbargo();

            if (tipoLista == 0) {
                secRegistro = listaEmbargos.get(index).getSecuencia();
                if (cualCelda == 0) {
                    TipoEmbargo = listaEmbargos.get(index).getTipoembargo().getDescripcion();
                } else if (cualCelda == 4) {
                    Juzgado = listaEmbargos.get(index).getJuzgado().getNombre();
                } else if (cualCelda == 5) {
                    Motivo = listaEmbargos.get(index).getMotivoembargo().getNombre();
                } else if (cualCelda == 6) {
                    Demandante = listaEmbargos.get(index).getDemandante().getNombre();
                } else if (cualCelda == 7) {
                    Nit = listaEmbargos.get(index).getTercero().getStrNit();
                } else if (cualCelda == 11) {
                    Forma = listaEmbargos.get(index).getFormadto().getDescripcion();
                }

            } else {
                secRegistro = filtradoListaEmbargos.get(index).getSecuencia();
            }
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPOEMBARGO")) {
            if (tipoNuevo == 1) {
                TipoEmbargo = nuevoEmbargo.getTipoembargo().getDescripcion();
            } else if (tipoNuevo == 2) {
                TipoEmbargo = duplicarEmbargo.getTipoembargo().getDescripcion();
            } else if (Campo.equals("JUZGADO")) {
                if (tipoNuevo == 1) {
                    Juzgado = nuevoEmbargo.getJuzgado().getNombre();
                } else if (tipoNuevo == 2) {
                    Juzgado = duplicarEmbargo.getJuzgado().getNombre();
                }
            } else if (Campo.equals("MOTIVO")) {
                if (tipoNuevo == 1) {
                    Motivo = nuevoEmbargo.getMotivoembargo().getNombre();
                } else if (tipoNuevo == 2) {
                    Motivo = duplicarEmbargo.getMotivoembargo().getNombre();
                }
            } else if (Campo.equals("DEMANDANTE")) {
                if (tipoNuevo == 1) {
                    Demandante = nuevoEmbargo.getDemandante().getNombre();
                } else if (tipoNuevo == 2) {
                    Demandante = duplicarEmbargo.getDemandante().getNombre();
                }
            } else if (Campo.equals("NIT")) {
                if (tipoNuevo == 1) {
                    Nit = nuevoEmbargo.getTercero().getStrNit();
                } else if (tipoNuevo == 2) {
                    Nit = duplicarEmbargo.getTercero().getStrNit();
                }
            } else if (Campo.equals("FORMA")) {
                if (tipoNuevo == 1) {
                    Forma = nuevoEmbargo.getFormadto().getDescripcion();
                } else if (tipoNuevo == 2) {
                    Forma = duplicarEmbargo.getFormadto().getDescripcion();
                }
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOEMBARGO")) {
            if (tipoNuevo == 1) {
                nuevoEmbargo.getTipoembargo().setDescripcion(TipoEmbargo);
            } else if (tipoNuevo == 2) {
                duplicarEmbargo.getTipoembargo().setDescripcion(TipoEmbargo);
            }
            for (int i = 0; i < lovlistaTiposEmbargos.size(); i++) {
                if (lovlistaTiposEmbargos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoEmbargo.setTipoembargo(lovlistaTiposEmbargos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoEmbargo");
                } else if (tipoNuevo == 2) {
                    duplicarEmbargo.setTipoembargo(lovlistaTiposEmbargos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEmbargo");
                }
                lovlistaTiposEmbargos.clear();
                getLovlistaTiposEmbargos();
            } else {
                RequestContext.getCurrentInstance().update("form:tiposEmbargosDialogo");
                PrimefacesContextUI.ejecutar("PF('tiposEmbargosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoEmbargo");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEmbargo");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("JUZGADO")) {
            if (tipoNuevo == 1) {
                nuevoEmbargo.getJuzgado().setNombre(Juzgado);
            } else if (tipoNuevo == 2) {
                duplicarEmbargo.getJuzgado().setNombre(Juzgado);
            }
            for (int i = 0; i < lovlistaJuzgados.size(); i++) {
                if (lovlistaJuzgados.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoEmbargo.setJuzgado(lovlistaJuzgados.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoJuzgado");
                } else if (tipoNuevo == 2) {
                    duplicarEmbargo.setJuzgado(lovlistaJuzgados.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarJuzgado");
                }
                lovlistaJuzgados.clear();
                getLovlistaJuzgados();
            } else {
                RequestContext.getCurrentInstance().update("form:juzgadosDialogo");
                PrimefacesContextUI.ejecutar("PF('juzgadosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoJuzgado");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarJuzgado");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("MOTIVO")) {
            if (tipoNuevo == 1) {
                nuevoEmbargo.getMotivoembargo().setNombre(Motivo);
            } else if (tipoNuevo == 2) {
                duplicarEmbargo.getMotivoembargo().setNombre(Motivo);
            }
            for (int i = 0; i < lovlistaMotivos.size(); i++) {
                if (lovlistaMotivos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoEmbargo.setMotivoembargo(lovlistaMotivos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoMotivo");
                } else if (tipoNuevo == 2) {
                    duplicarEmbargo.setMotivoembargo(lovlistaMotivos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivo");
                }
                lovlistaMotivos.clear();
                getLovlistaMotivos();
            } else {
                RequestContext.getCurrentInstance().update("form:motivosDialogo");
                PrimefacesContextUI.ejecutar("PF('motivosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoMotivo");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivo");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("DEMANDANTE")) {
            if (tipoNuevo == 1) {
                nuevoEmbargo.getDemandante().setNombre(Demandante);
            } else if (tipoNuevo == 2) {
                duplicarEmbargo.getDemandante().setNombre(Demandante);
            }
            for (int i = 0; i < lovlistaDemandantes.size(); i++) {
                if (lovlistaDemandantes.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoEmbargo.setDemandante(lovlistaDemandantes.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDemandante");
                } else if (tipoNuevo == 2) {
                    duplicarEmbargo.setDemandante(lovlistaDemandantes.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDemandante");
                }
                lovlistaDemandantes.clear();
                getLovlistaDemandantes();
            } else {
                RequestContext.getCurrentInstance().update("form:demandantesDialogo");
                PrimefacesContextUI.ejecutar("PF('demandantesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDemandante");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDemandante");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
            if (tipoNuevo == 1) {
                nuevoEmbargo.getTercero().setStrNit(Nit);
            } else if (tipoNuevo == 2) {
                duplicarEmbargo.getTercero().setStrNit(Nit);
            }

            for (int i = 0; i < lovlistaTerceros.size(); i++) {
                if (lovlistaTerceros.get(i).getStrNit().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoEmbargo.setTercero(lovlistaTerceros.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDemandante");
                } else if (tipoNuevo == 2) {
                    duplicarEmbargo.setTercero(lovlistaTerceros.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDemandante");
                }
                lovlistaTerceros.clear();
                getLovlistaTerceros();
            } else {
                RequestContext.getCurrentInstance().update("form:tercerosDialogo");
                PrimefacesContextUI.ejecutar("PF('tercerosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNIT");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTercero");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNIT");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("FORMA")) {
            if (tipoNuevo == 1) {
                nuevoEmbargo.getFormadto().setDescripcion(Forma);
            } else if (tipoNuevo == 2) {
                duplicarEmbargo.getFormadto().setDescripcion(Forma);
            }
            for (int i = 0; i < lovlistaFormasDtos.size(); i++) {
                if (lovlistaFormasDtos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoEmbargo.setFormadto(lovlistaFormasDtos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaForma");
                } else if (tipoNuevo == 2) {
                    duplicarEmbargo.setFormadto(lovlistaFormasDtos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarForma");
                }
                lovlistaFormasDtos.clear();
                getLovlistaFormasDtos();
            } else {
                RequestContext.getCurrentInstance().update("form:formasDtosDialogo");
                PrimefacesContextUI.ejecutar("PF('formasDtosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaForma");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarForma");
                }
            }
        }
    }

    //METODOS DEL REGISTRO NUEVO DE DETALLE EMBARGOS 
    public void valoresBackupAutocompletarD(int tipoNuevoD, String Campo) {
        if (Campo.equals("PAGO")) {
            if (tipoNuevoD == 1) {
                Pago = nuevoEmbargoDetalle.getDetalleformadto().getDescripcion();
            } else if (tipoNuevoD == 2) {
                Pago = duplicarEmbargoDetalle.getDetalleformadto().getDescripcion();
            } else if (Campo.equals("PERIODICIDAD")) {
                if (tipoNuevoD == 1) {
                    Periodicidad = nuevoEmbargoDetalle.getPeriodicidad().getCodigoStr();
                } else if (tipoNuevoD == 2) {
                    Periodicidad = duplicarEmbargoDetalle.getPeriodicidad().getCodigoStr();
                }
            }
        }
    }

    public void autocompletarNuevoyDuplicadoD(String confirmarCambio, String valorConfirmar, int tipoNuevoD) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("PAGO")) {
            if (tipoNuevoD == 1) {
                nuevoEmbargoDetalle.getDetalleformadto().setDescripcion(Pago);
            } else if (tipoNuevoD == 2) {
                duplicarEmbargoDetalle.getDetalleformadto().setDescripcion(Pago);
            }
            for (int i = 0; i < lovlistaDetallesFormasDtos.size(); i++) {
                if (lovlistaDetallesFormasDtos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevoD == 1) {
                    nuevoEmbargoDetalle.setDetalleformadto(lovlistaDetallesFormasDtos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPagoComprometido");
                } else if (tipoNuevoD == 2) {
                    duplicarEmbargoDetalle.setDetalleformadto(lovlistaDetallesFormasDtos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPagoComprometido");
                }
                lovlistaDetallesFormasDtos.clear();
                getLovlistaDetallesFormasDtos();
            } else {
                RequestContext.getCurrentInstance().update("form:formasDescuentosDialogo");
                PrimefacesContextUI.ejecutar("PF('formasDescuentosDialogo').show()");
                tipoActualizacion = tipoNuevoD;
                if (tipoNuevoD == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPagoComprometido");
                } else if (tipoNuevoD == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPagoComprometido");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("PERIODICIDAD")) {
            if (tipoNuevoD == 1) {
                nuevoEmbargoDetalle.getPeriodicidad().setCodigoStr(Periodicidad);
            } else if (tipoNuevoD == 2) {
                duplicarEmbargoDetalle.getPeriodicidad().setCodigoStr(Periodicidad);
            }
            for (int i = 0; i < lovlistaPeriodicidades.size(); i++) {
                if (lovlistaPeriodicidades.get(i).getCodigoStr().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevoD == 1) {
                    nuevoEmbargoDetalle.setPeriodicidad(lovlistaPeriodicidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigoPeriodicidad");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombrePeriodicidad");
                } else if (tipoNuevoD == 2) {
                    duplicarEmbargoDetalle.setPeriodicidad(lovlistaPeriodicidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoPeriodicidad");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombrePeriodicidad");
                }
                lovlistaPeriodicidades.clear();
                getLovlistaPeriodicidades();
            } else {
                RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
                PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
                tipoActualizacion = tipoNuevoD;
                if (tipoNuevoD == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigoPeriodicidad");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombrePeriodicidad");
                } else if (tipoNuevoD == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoPeriodicidad");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombrePeriodicidad");
                }
            }

        }
    }

    //UBICACION CELDA
    public void cambiarIndiceD(int indiceD, int celda) {
        if (permitirIndex == true) {
            indexD = indiceD;
            cualCeldaD = celda;
            cualTabla = 1;
            tablaImprimir = ":formExportar:datosDetallesEmbargosExportar";
            nombreArchivo = "DetallesEmbargosXML";
            System.out.println("CualTabla = " + cualTabla);
            detallesEmbargoSeleccionado = listaDetallesEmbargos.get(indexD);
            if (tipoLista == 0) {
                secRegistro = listaDetallesEmbargos.get(indexD).getSecuencia();
                if (cualCeldaD == 0) {
                    Pago = listaDetallesEmbargos.get(indexD).getDetalleformadto().getDescripcion();
                } else if (cualCeldaD == 5) {
                    Periodicidad = listaDetallesEmbargos.get(indexD).getPeriodicidad().getCodigoStr();
                }

            } else {
                secRegistro = filtradoListaDetallesEmbargos.get(indexD).getSecuencia();
            }
        }
    }

    public void asignarIndex(Integer indice, int dlg, int LND) {
        index = indice;
        RequestContext context = RequestContext.getCurrentInstance();

        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            index = -1;
            secRegistro = null;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            index = -1;
            secRegistro = null;
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposEmbargosDialogo");
            PrimefacesContextUI.ejecutar("PF('tiposEmbargosDialogo').show()");
        } else if (dlg == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:juzgadosDialogo");
            PrimefacesContextUI.ejecutar("PF('juzgadosDialogo').show()");
        } else if (dlg == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:motivosDialogo");
            PrimefacesContextUI.ejecutar("PF('motivosDialogo').show()");
        } else if (dlg == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:demandantesDialogo");
            PrimefacesContextUI.ejecutar("PF('demandantesDialogo').show()");
        } else if (dlg == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
            PrimefacesContextUI.ejecutar("PF('tercerosDialogo').show()");
        } else if (dlg == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:formasDtosDialogo");
            PrimefacesContextUI.ejecutar("PF('formasDtosDialogo').show()");
        }
    }

    public void asignarIndexD(Integer indiceD, int dlg, int LND) {
        index = indiceD;
        RequestContext context = RequestContext.getCurrentInstance();

        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            index = -1;
            secRegistro = null;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            index = -1;
            secRegistro = null;
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:formasDescuentosDialogo");
            PrimefacesContextUI.ejecutar("PF('formasDescuentosDialogo').show()");
        } else if (dlg == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
            PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (index >= 0 && cualTabla == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("form:tiposEmbargosDialogo");
                PrimefacesContextUI.ejecutar("PF('tiposEmbargosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("form:juzgadosDialogo");
                PrimefacesContextUI.ejecutar("PF('juzgadosDialogo').show()");
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("form:motivosDialogo");
                PrimefacesContextUI.ejecutar("PF('motivosDialogo').show()");
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("form:demandantesDialogo");
                PrimefacesContextUI.ejecutar("PF('demandantesDialogo').show()");
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("form:tercerosDialogo");
                PrimefacesContextUI.ejecutar("PF('tercerosDialogo').show()");
            } else if (cualCelda == 11) {
                RequestContext.getCurrentInstance().update("form:tercerosDialogo");
                PrimefacesContextUI.ejecutar("PF('tercerosDialogo').show()");
            }// Falta la Lista de Valores de la columna esa ficti
        } else if (indexD >= 0 && cualTabla == 1) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaD == 0) {
                RequestContext.getCurrentInstance().update("form:pagosDialogo");
                PrimefacesContextUI.ejecutar("PF('pagosDialogos').show()");
            } else if (cualCeldaD == 5) {
                RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
                PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
            }
        }
    }

    public void cancelarSeleccionEmpleado() {
        filtradoListaEmpleadosLOV = null;
        empleadoSeleccionado = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpleados:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleados').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('buscarEmpleadoDialogo').hide()");
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        if (cualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmbargosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDFTablasAnchas();
            exporter.export(context, tabla, "EmbargosPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            index = -1;
            secRegistro = null;
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDetallesEmbargosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDFTablasAnchas();
            exporter.export(context, tabla, "DetallesEmbargosPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexD = -1;
            secRegistro = null;
        }
    }

    public void exportXLS() throws IOException {
        if (cualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmbargosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "EmbargosXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            index = -1;
            secRegistro = null;
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDetallesEmbargosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "DetallesEmbargosXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexD = -1;
            secRegistro = null;
        }
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevoEmbargo() {
        nuevoEmbargo = new EersPrestamos();
        nuevoEmbargo.setTipoembargo(new TiposEmbargos());
        nuevoEmbargo.setJuzgado(new Juzgados());
        nuevoEmbargo.setMotivoembargo(new MotivosEmbargos());
        nuevoEmbargo.setDemandante(new Terceros());
        nuevoEmbargo.setTercero(new Terceros());
        nuevoEmbargo.setFormadto(new FormasDtos());
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO DETALLE EMBARGO
    public void limpiarNuevoDetalleEmbargo() {
        nuevoEmbargoDetalle = new EersPrestamosDtos();
        nuevoEmbargoDetalle.setDetalleformadto(new DetallesFormasDtos());
        nuevoEmbargoDetalle.setPeriodicidad(new Periodicidades());
        indexD = -1;
        secRegistro = null;
    }

    //LIMPIAR DUPLICAR
    public void limpiarduplicarEmbargo() {
        duplicarEmbargo = new EersPrestamos();
    }
    //LIMPIAR DUPLICAR NO FORMAL

    public void limpiarduplicarEmbargoDetalle() {
        duplicarEmbargoDetalle = new EersPrestamosDtos();
    }

    public void verificarRastro() {
        if (cualTabla == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("lol");
            if (!listaEmbargos.isEmpty()) {
                if (secRegistro != null) {
                    System.out.println("lol 2");
                    int resultado = administrarRastros.obtenerTabla(secRegistro, "EERSPRESTAMOS");
                    System.out.println("resultado: " + resultado);
                    if (resultado == 1) {
                        PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                    } else if (resultado == 2) {
                        PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
                    } else if (resultado == 3) {
                        PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                    } else if (resultado == 4) {
                        PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
                    } else if (resultado == 5) {
                        PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                    }
                } else {
                    PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
                }
            } else {
                if (administrarRastros.verificarHistoricosTabla("EERSPRESTAMOS")) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
                } else {
                    PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
                }

            }
            index = -1;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("D");
            if (!listaDetallesEmbargos.isEmpty()) {
                if (secRegistro != null) {
                    System.out.println("NF2");
                    int resultadoNF = administrarRastros.obtenerTabla(secRegistro, "EERSPRESTAMOSDTOS");
                    System.out.println("resultado: " + resultadoNF);
                    if (resultadoNF == 1) {
                        PrimefacesContextUI.ejecutar("PF('errorObjetosDBNF').show()");
                    } else if (resultadoNF == 2) {
                        PrimefacesContextUI.ejecutar("PF('confirmarRastroNF').show()");
                    } else if (resultadoNF == 3) {
                        PrimefacesContextUI.ejecutar("PF('errorRegistroRastroNF').show()");
                    } else if (resultadoNF == 4) {
                        PrimefacesContextUI.ejecutar("PF('errorTablaConRastroNF').show()");
                    } else if (resultadoNF == 5) {
                        PrimefacesContextUI.ejecutar("PF('errorTablaSinRastroNF').show()");
                    }
                } else {
                    PrimefacesContextUI.ejecutar("PF('seleccionarRegistroNF').show()");
                }
            } else {
                if (administrarRastros.verificarHistoricosTabla("EERSPRESTAMOSDTOS")) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastroHistoricoNF').show()");
                } else {
                    PrimefacesContextUI.ejecutar("PF('errorRastroHistoricoNF').show()");
                }

            }
            indexD = -1;
        }

    }

    //Getter & Setter
    public List<Empleados> getListaEmpleadosLOV() {
        if (listaEmpleadosLOV == null) {
            listaEmpleadosLOV = administrarNovedadesEmbargos.lovEmpleados();
        }
        return listaEmpleadosLOV;
    }

    public void setListaEmpleadosLOV(List<Empleados> listaEmpleadosLOV) {
        this.listaEmpleadosLOV = listaEmpleadosLOV;
    }

    public List<Empleados> getFiltradoListaEmpleadosLOV() {
        return filtradoListaEmpleadosLOV;
    }

    public void setFiltradoListaEmpleadosLOV(List<Empleados> filtradoListaEmpleadosLOV) {
        this.filtradoListaEmpleadosLOV = filtradoListaEmpleadosLOV;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public List<EersPrestamos> getListaEmbargos() {
        if (listaEmbargos == null) {
            System.out.println("empleadoActual.getSecuencia()" + empleadoActual.getSecuencia());
            listaEmbargos = administrarNovedadesEmbargos.eersPrestamosEmpleado(empleadoActual.getSecuencia());
            if (!listaEmbargos.isEmpty()) {
                embargoSeleccionado = listaEmbargos.get(0);
                secuenciaEmbargo = embargoSeleccionado.getSecuencia();
            }
        }
        return listaEmbargos;
    }

    public void setListaEmbargos(List<EersPrestamos> listaEmbargos) {
        this.listaEmbargos = listaEmbargos;
    }

    public List<EersPrestamos> getFiltradoListaEmbargos() {
        return filtradoListaEmbargos;
    }

    public void setFiltradoListaEmbargos(List<EersPrestamos> filtradoListaEmbargos) {
        this.filtradoListaEmbargos = filtradoListaEmbargos;
    }

    public EersPrestamos getEmbargoSeleccionado() {
        return embargoSeleccionado;
    }

    public void setEmbargoSeleccionado(EersPrestamos embargoSeleccionado) {
        this.embargoSeleccionado = embargoSeleccionado;
    }

    public List<Empleados> getListaEmpleados() {
        listaEmpleados = administrarNovedadesEmbargos.listaEmpleados();
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public Empleados getEmpleadoActual() {
        if (empleadoActual == null) {
            getListaEmpleados();
            if (listaEmpleados != null && !listaEmpleados.isEmpty()) {
                empleadoActual = listaEmpleados.get(registroActual);
            }
        }
        return empleadoActual;
    }

    public void setEmpleadoActual(Empleados empleadoActual) {
        this.empleadoActual = empleadoActual;
    }

    public int getRegistroActual() {
        return registroActual;
    }

    public void setRegistroActual(int registroActual) {
        this.registroActual = registroActual;
    }

    public boolean isMostrarTodos() {
        return mostrarTodos;
    }

    public void setMostrarTodos(boolean mostrarTodos) {
        this.mostrarTodos = mostrarTodos;
    }

    public String getAltoScrollEmbargos() {
        return altoScrollEmbargos;
    }

    public void setAltoScrollEmbargos(String altoScrollEmbargos) {
        this.altoScrollEmbargos = altoScrollEmbargos;
    }

    public String getAltoScrollDetallesEmbargos() {
        return altoScrollDetallesEmbargos;
    }

    public void setAltoScrollDetallesEmbargos(String altoScrollDetallesEmbargos) {
        this.altoScrollDetallesEmbargos = altoScrollDetallesEmbargos;
    }

    public List<TiposEmbargos> getLovlistaTiposEmbargos() {
        if (lovlistaTiposEmbargos == null) {
            lovlistaTiposEmbargos = administrarNovedadesEmbargos.lovTiposEmbargos();
        }
        return lovlistaTiposEmbargos;
    }

    public void setLovlistaTiposEmbargos(List<TiposEmbargos> lovlistaTiposEmbargos) {
        this.lovlistaTiposEmbargos = lovlistaTiposEmbargos;
    }

    public List<TiposEmbargos> getLovfiltradoslistaTiposEmbargos() {
        return lovfiltradoslistaTiposEmbargos;
    }

    public void setLovfiltradoslistaTiposEmbargos(List<TiposEmbargos> lovfiltradoslistaTiposEmbargos) {
        this.lovfiltradoslistaTiposEmbargos = lovfiltradoslistaTiposEmbargos;
    }

    public TiposEmbargos getTiposEmbargosSeleccionado() {
        return tiposEmbargosSeleccionado;
    }

    public void setTiposEmbargosSeleccionado(TiposEmbargos tiposEmbargosSeleccionado) {
        this.tiposEmbargosSeleccionado = tiposEmbargosSeleccionado;
    }

    public List<Juzgados> getLovlistaJuzgados() {
        if (lovlistaJuzgados == null) {
            lovlistaJuzgados = administrarNovedadesEmbargos.lovJuzgados();
        }
        return lovlistaJuzgados;
    }

    public void setLovlistaJuzgados(List<Juzgados> lovlistaJuzgados) {
        this.lovlistaJuzgados = lovlistaJuzgados;
    }

    public List<Juzgados> getLovfiltradoslistaJuzgados() {
        return lovfiltradoslistaJuzgados;
    }

    public void setLovfiltradoslistaJuzgados(List<Juzgados> lovfiltradoslistaJuzgados) {
        this.lovfiltradoslistaJuzgados = lovfiltradoslistaJuzgados;
    }

    public Juzgados getJuzgadosSeleccionado() {
        return juzgadosSeleccionado;
    }

    public void setJuzgadosSeleccionado(Juzgados juzgadosSeleccionado) {
        this.juzgadosSeleccionado = juzgadosSeleccionado;
    }

    public List<MotivosEmbargos> getLovlistaMotivos() {
        if (lovlistaMotivos == null) {
            lovlistaMotivos = administrarNovedadesEmbargos.lovMotivosEmbargos();
        }
        return lovlistaMotivos;
    }

    public void setLovlistaMotivos(List<MotivosEmbargos> lovlistaMotivos) {
        this.lovlistaMotivos = lovlistaMotivos;
    }

    public List<MotivosEmbargos> getLovfiltradoslistaMotivos() {
        return lovfiltradoslistaMotivos;
    }

    public void setLovfiltradoslistaMotivos(List<MotivosEmbargos> lovfiltradoslistaMotivos) {
        this.lovfiltradoslistaMotivos = lovfiltradoslistaMotivos;
    }

    public MotivosEmbargos getMotivosSeleccionado() {
        return motivosSeleccionado;
    }

    public void setMotivosSeleccionado(MotivosEmbargos motivosSeleccionado) {
        this.motivosSeleccionado = motivosSeleccionado;
    }

    public List<Terceros> getLovlistaDemandantes() {
        if (lovlistaDemandantes == null) {
            lovlistaDemandantes = administrarNovedadesEmbargos.lovTerceros();
        }
        return lovlistaDemandantes;
    }

    public void setLovlistaDemandantes(List<Terceros> lovlistaDemandantes) {
        this.lovlistaDemandantes = lovlistaDemandantes;
    }

    public List<Terceros> getLovfiltradoslistaDemandantes() {
        return lovfiltradoslistaDemandantes;
    }

    public void setLovfiltradoslistaDemandantes(List<Terceros> lovfiltradoslistaDemandantes) {
        this.lovfiltradoslistaDemandantes = lovfiltradoslistaDemandantes;
    }

    public Terceros getDemandantesSeleccionado() {
        return demandantesSeleccionado;
    }

    public void setDemandantesSeleccionado(Terceros demandantesSeleccionado) {
        this.demandantesSeleccionado = demandantesSeleccionado;
    }

    public List<Terceros> getLovlistaTerceros() {
        if (lovlistaTerceros == null) {
            lovlistaTerceros = administrarNovedadesEmbargos.lovTerceros();
        }
        return lovlistaTerceros;
    }

    public void setLovlistaTerceros(List<Terceros> lovlistaTerceros) {
        this.lovlistaTerceros = lovlistaTerceros;
    }

    public List<Terceros> getLovfiltradoslistaTerceros() {
        return lovfiltradoslistaTerceros;
    }

    public void setLovfiltradoslistaTerceros(List<Terceros> lovfiltradoslistaTerceros) {
        this.lovfiltradoslistaTerceros = lovfiltradoslistaTerceros;
    }

    public Terceros getTercerosSeleccionado() {
        return tercerosSeleccionado;
    }

    public void setTercerosSeleccionado(Terceros tercerosSeleccionado) {
        this.tercerosSeleccionado = tercerosSeleccionado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<EersPrestamosDtos> getListaDetallesEmbargos() {
        if (listaDetallesEmbargos == null && embargoSeleccionado != null) {
            listaDetallesEmbargos = administrarNovedadesEmbargos.eersPrestamosEmpleadoDtos(secuenciaEmbargo);
        }
        return listaDetallesEmbargos;
    }

    public void setListaDetallesEmbargos(List<EersPrestamosDtos> listaDetallesEmbargos) {
        this.listaDetallesEmbargos = listaDetallesEmbargos;
    }

    public List<EersPrestamosDtos> getFiltradoListaDetallesEmbargos() {
        return filtradoListaDetallesEmbargos;
    }

    public void setFiltradoListaDetallesEmbargos(List<EersPrestamosDtos> filtradoListaDetallesEmbargos) {
        this.filtradoListaDetallesEmbargos = filtradoListaDetallesEmbargos;
    }

    public EersPrestamosDtos getDetallesEmbargoSeleccionado() {
        return detallesEmbargoSeleccionado;
    }

    public void setEmbargoSeleccionado(EersPrestamosDtos detallesEmbargoSeleccionado) {
        this.detallesEmbargoSeleccionado = detallesEmbargoSeleccionado;
    }

    public List<Periodicidades> getLovlistaPeriodicidades() {
        if (lovlistaPeriodicidades == null) {
            lovlistaPeriodicidades = administrarNovedadesEmbargos.lovPeriodicidades();
        }
        return lovlistaPeriodicidades;
    }

    public void setLovlistaPeriodicidades(List<Periodicidades> lovlistaPeriodicidades) {
        this.lovlistaPeriodicidades = lovlistaPeriodicidades;
    }

    public List<Periodicidades> getLovfiltradoslistaPeriodicidades() {
        return lovfiltradoslistaPeriodicidades;
    }

    public void setLovfiltradoslistaPeriodicidades(List<Periodicidades> lovfiltradoslistaPeriodicidades) {
        this.lovfiltradoslistaPeriodicidades = lovfiltradoslistaPeriodicidades;
    }

    public Periodicidades getPeriodicidadesSeleccionado() {
        return periodicidadesSeleccionado;
    }

    public void setPeriodicidadesSeleccionado(Periodicidades periodicidadesSeleccionado) {
        this.periodicidadesSeleccionado = periodicidadesSeleccionado;
    }

    public List<DetallesFormasDtos> getLovlistaDetallesFormasDtos() {
        if (lovlistaDetallesFormasDtos == null) {
            lovlistaDetallesFormasDtos = administrarNovedadesEmbargos.lovDetallesFormasDescuentos(embargoSeleccionado.getFormadto().getSecuencia());
        }
        return lovlistaDetallesFormasDtos;
    }

    public void setLovlistaDetallesFormasDtos(List<DetallesFormasDtos> lovlistaDetallesFormasDtos) {
        this.lovlistaDetallesFormasDtos = lovlistaDetallesFormasDtos;
    }

    public List<DetallesFormasDtos> getLovfiltradoslistaDetallesFormasDtos() {
        return lovfiltradoslistaDetallesFormasDtos;
    }

    public void setLovfiltradoslistaDetallesFormasDtos(List<DetallesFormasDtos> lovfiltradoslistaDetallesFormasDtos) {
        this.lovfiltradoslistaDetallesFormasDtos = lovfiltradoslistaDetallesFormasDtos;
    }

    public DetallesFormasDtos getDetallesFormasDtosSeleccionado() {
        return detallesFormasDtosSeleccionado;
    }

    public void setDetallesFormasDtosSeleccionado(DetallesFormasDtos detallesFormasDtosSeleccionado) {
        this.detallesFormasDtosSeleccionado = detallesFormasDtosSeleccionado;
    }

    public List<FormasDtos> getLovlistaFormasDtos() {
        if (lovlistaFormasDtos == null) {
            lovlistaFormasDtos = administrarNovedadesEmbargos.lovFormasDtos(embargoSeleccionado.getTipoembargo().getSecuencia());
        }
        return lovlistaFormasDtos;
    }

    public void setLovlistaFormasDtos(List<FormasDtos> lovlistaFormasDtos) {
        this.lovlistaFormasDtos = lovlistaFormasDtos;
    }

    public List<FormasDtos> getLovfiltradoslistaFormasDtos() {
        return lovfiltradoslistaFormasDtos;
    }

    public void setLovfiltradoslistaFormasDtos(List<FormasDtos> lovfiltradoslistaFormasDtos) {
        this.lovfiltradoslistaFormasDtos = lovfiltradoslistaFormasDtos;
    }

    public FormasDtos getFormasDtosSeleccionado() {
        return formasDtosSeleccionado;
    }

    public void setFormasDtosSeleccionado(FormasDtos formasDtosSeleccionado) {
        this.formasDtosSeleccionado = formasDtosSeleccionado;
    }

    public List<VWPrestamoDtosRealizados> getListaVWPrestamo() {
        if (listaVWPrestamo == null && detallesEmbargoSeleccionado != null) {
            System.out.println("Entra");
            listaVWPrestamo = administrarNovedadesEmbargos.prestamosRealizados(detallesEmbargoSeleccionado.getSecuencia());
        }
        return listaVWPrestamo;
    }

    public void setListaVWPrestamo(List<VWPrestamoDtosRealizados> listaVWPrestamo) {
        this.listaVWPrestamo = listaVWPrestamo;
    }

    public List<VWPrestamoDtosRealizados> getFiltradoListaVWPrestamo() {
        return filtradoListaVWPrestamo;
    }

    public void setFiltradoListaVWPrestamo(List<VWPrestamoDtosRealizados> filtradoListaVWPrestamo) {
        this.filtradoListaVWPrestamo = filtradoListaVWPrestamo;
    }

    public VWPrestamoDtosRealizados getVwPrestamoSeleccionado() {
        return vwPrestamoSeleccionado;
    }

    public void setVwPrestamoSeleccionado(VWPrestamoDtosRealizados vwPrestamoSeleccionado) {
        this.vwPrestamoSeleccionado = vwPrestamoSeleccionado;
    }

    public String getAltoScrollDescuentosRealizados() {
        return altoScrollDescuentosRealizados;
    }

    public void setAltoScrollDescuentosRealizados(String altoScrollDescuentosRealizados) {
        this.altoScrollDescuentosRealizados = altoScrollDescuentosRealizados;
    }

    public EersPrestamos getEditarEmbargos() {
        return editarEmbargos;
    }

    public void setEditarEmbargos(EersPrestamos editarEmbargos) {
        this.editarEmbargos = editarEmbargos;
    }

    public EersPrestamosDtos getEditarDetallesEmbargos() {
        return editarDetallesEmbargos;
    }

    public void setEditarDetallesEmbargos(EersPrestamosDtos editarDetallesEmbargos) {
        this.editarDetallesEmbargos = editarDetallesEmbargos;
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

    public EersPrestamos getNuevoEmbargo() {
        return nuevoEmbargo;
    }

    public void setNuevoEmbargo(EersPrestamos nuevoEmbargo) {
        this.nuevoEmbargo = nuevoEmbargo;
    }

    public EersPrestamosDtos getNuevoDetalleEmbargo() {
        return nuevoEmbargoDetalle;
    }

    public void setNuevoDetalleEmbargo(EersPrestamosDtos nuevoEmbargoDetalle) {
        this.nuevoEmbargoDetalle = nuevoEmbargoDetalle;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public EersPrestamos getDuplicarEmbargo() {
        return duplicarEmbargo;
    }

    public void setDuplicarEmbargo(EersPrestamos duplicarEmbargo) {
        this.duplicarEmbargo = duplicarEmbargo;
    }

    public EersPrestamosDtos getDuplicarDetalleEmbargo() {
        return duplicarEmbargoDetalle;
    }

    public void setDuplicarDetalleEmbargo(EersPrestamosDtos duplicarEmbargoDetalle) {
        this.duplicarEmbargoDetalle = duplicarEmbargoDetalle;
    }

    public boolean isRoValor() {
        return roValor;
    }

    public void setRoValor(boolean roValor) {
        this.roValor = roValor;
    }

    public boolean isRoValorD() {
        return roValorD;
    }

    public void setRoValorD(boolean roValorD) {
        this.roValorD = roValorD;
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

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

}
