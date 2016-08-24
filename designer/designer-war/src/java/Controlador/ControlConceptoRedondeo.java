package Controlador;

import Entidades.Conceptos;
import Entidades.Personas;
import Entidades.ConceptosRedondeos;
import Entidades.TiposRedondeos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarConceptosRedondeosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
 * @author Victor Algarin
 */
@ManagedBean
@SessionScoped
public class ControlConceptoRedondeo implements Serializable {

    @EJB
    AdministrarConceptosRedondeosInterface administrarConceptosRedondeos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //Lista ConceptosRedondeos
    private List<ConceptosRedondeos> listaConceptosRedondeos;
    private List<ConceptosRedondeos> filtradoListaConceptosRedondeos;
    private Personas persona;
    //Columnas Tabla VC
    private Column conceptoRedondeoConcepto, conceptoRedondeoTipoRedondeo;
    //OTROS
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    private boolean aceptar;
    private int index;
    //AUTOCOMPLETAR
    private String Concepto, TipoRedondeo;
    private String altoScrollConceptosRedondeos;
    //modificar
    private List<ConceptosRedondeos> listaConceptosRedondeosModificar;
    private boolean guardado, guardarOk;
    //crear VC
    public ConceptosRedondeos nuevoConceptoRedondeo;
    private List<ConceptosRedondeos> listaConceptosRedondeosCrear;
    private BigInteger l;
    private int k;
    private String mensajeValidacion;
    //borrar VC
    private List<ConceptosRedondeos> listaConceptosRedondeosBorrar;
    //editar celda
    private ConceptosRedondeos editarConceptosRedondeos;
    private int cualCelda, tipoLista;
    private boolean cambioEditor, aceptarEditar;
    //duplicar
    private ConceptosRedondeos duplicarConceptoRedondeo;
    //RASTROS
    private BigInteger secRegistro;
    private BigInteger secRegistroD;
    private boolean cambiosPagina;
    //L.O.V Conceptos Redondeos
    private List<ConceptosRedondeos> lovlistaConceptosRedondeos;
    private List<ConceptosRedondeos> lovfiltradoslistaConceptosRedondeos;
    private ConceptosRedondeos conceptosRedondeosSeleccionado;
    //L.O.V Conceptos
    private List<Conceptos> lovlistaConceptos;
    private List<Conceptos> lovfiltradoslistaConceptos;
    public Conceptos conceptosSeleccionado;
    //L.O.V Tipo
    private List<TiposRedondeos> lovlistaTiposRedondeos;
    private List<TiposRedondeos> lovfiltradoslistaTiposRedondeos;
    private TiposRedondeos tiposRedondeosSeleccionado;
    
    private String paginaAnterior;

    /**
     * Constructor de ControlConceptoRedondeo
     */
    public ControlConceptoRedondeo() {
        altoScrollConceptosRedondeos = "245";
        permitirIndex = true;
        listaConceptosRedondeos = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listaConceptosRedondeosBorrar = new ArrayList<ConceptosRedondeos>();
        //crear aficiones
        listaConceptosRedondeosCrear = new ArrayList<ConceptosRedondeos>();
        k = 0;
        //modificar aficiones
        listaConceptosRedondeosModificar = new ArrayList<ConceptosRedondeos>();
        //editar
        editarConceptosRedondeos = new ConceptosRedondeos();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevoConceptoRedondeo = new ConceptosRedondeos();
        duplicarConceptoRedondeo = new ConceptosRedondeos();
        secRegistro = null;
        cambiosPagina = true;
        paginaAnterior = "";
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarConceptosRedondeos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                context.update("formularioDialogos:conceptosDialogo");
                context.execute("conceptosDialogo.show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:tiposRedondeosDialogo");
                context.execute("tiposRedondeosDialogo.show()");
                tipoActualizacion = 0;
            }
        }
    }

    //AUTOCOMPLETAR
    public void modificarConceptosRedondeos(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        System.out.println("Entro a Modificar ConceptosRedondeos");

        RequestContext context = RequestContext.getCurrentInstance();

        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaConceptosRedondeosCrear.contains(listaConceptosRedondeos.get(index))) {

                    if (listaConceptosRedondeosModificar.isEmpty()) {
                        listaConceptosRedondeosModificar.add(listaConceptosRedondeos.get(index));
                    } else if (!listaConceptosRedondeosModificar.contains(listaConceptosRedondeos.get(index))) {
                        listaConceptosRedondeosModificar.add(listaConceptosRedondeos.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    context.update("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaConceptosRedondeosCrear.contains(filtradoListaConceptosRedondeos.get(index))) {

                    if (listaConceptosRedondeosCrear.isEmpty()) {
                        listaConceptosRedondeosCrear.add(filtradoListaConceptosRedondeos.get(index));
                    } else if (!listaConceptosRedondeosCrear.contains(filtradoListaConceptosRedondeos.get(index))) {
                        listaConceptosRedondeosCrear.add(filtradoListaConceptosRedondeos.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    context.update("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            }
            context.update("form:datosConceptosRedondeos");
        } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            if (tipoLista == 0) {
                listaConceptosRedondeos.get(indice).getConcepto().setDescripcion(Concepto);
            } else {
                filtradoListaConceptosRedondeos.get(indice).getConcepto().setDescripcion(Concepto);
            }

            for (int i = 0; i < lovlistaConceptos.size(); i++) {
                if (lovlistaConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaConceptosRedondeos.get(indice).setConcepto(lovlistaConceptos.get(indiceUnicoElemento));
                } else {
                    filtradoListaConceptosRedondeos.get(indice).setConcepto(lovlistaConceptos.get(indiceUnicoElemento));
                }
                lovlistaConceptos.clear();
                getLovlistaConceptos();
                cambiosPagina = false;
                context.update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:conceptosDialogo");
                context.execute("conceptosDialogo.show()");
                tipoActualizacion = 0;
            }
        }

    }

    //Ubicacion Celda.
    /**
     * Metodo que obtiene la posicion dentro de la tabla ConceptosRedondeos
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;

            if (tipoLista == 0) {
                secRegistro = listaConceptosRedondeos.get(index).getSecuencia();
                if (cualCelda == 0) {
                    Concepto = listaConceptosRedondeos.get(index).getConcepto().getDescripcion();
                } else if (cualCelda == 1) {
                    TipoRedondeo = listaConceptosRedondeos.get(index).getTiporedondeo().getDescripcion();
                }
            } else {
                secRegistro = filtradoListaConceptosRedondeos.get(index).getSecuencia();
            }

        }
    }

    public void actualizarConceptos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaConceptosRedondeos.get(index).setConcepto(conceptosSeleccionado);
                if (!listaConceptosRedondeosCrear.contains(listaConceptosRedondeos.get(index))) {
                    if (listaConceptosRedondeosModificar.isEmpty()) {
                        listaConceptosRedondeosModificar.add(listaConceptosRedondeos.get(index));
                    } else if (!listaConceptosRedondeosModificar.contains(listaConceptosRedondeos.get(index))) {
                        listaConceptosRedondeosModificar.add(listaConceptosRedondeos.get(index));
                    }
                }
            } else {
                filtradoListaConceptosRedondeos.get(index).setConcepto(conceptosSeleccionado);
                if (!listaConceptosRedondeosCrear.contains(filtradoListaConceptosRedondeos.get(index))) {
                    if (listaConceptosRedondeosModificar.isEmpty()) {
                        listaConceptosRedondeosModificar.add(filtradoListaConceptosRedondeos.get(index));
                    } else if (!listaConceptosRedondeosModificar.contains(filtradoListaConceptosRedondeos.get(index))) {
                        listaConceptosRedondeosModificar.add(filtradoListaConceptosRedondeos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            cambiosPagina = false;
            permitirIndex = true;
            context.update("form:datosConceptosRedondeos");
        } else if (tipoActualizacion == 1) {
            System.out.println("Entro al Nuevo, como esperaba");
            nuevoConceptoRedondeo.setConcepto(conceptosSeleccionado);
            context.update("formularioDialogos:nuevoConcepto");
        } else if (tipoActualizacion == 2) {
            duplicarConceptoRedondeo.setConcepto(conceptosSeleccionado);
            context.update("formularioDialogos:duplicarConcepto");

        }
        lovfiltradoslistaConceptos = null;
        conceptosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVConceptos:globalFilter");
        context.execute("LOVConceptos.clearFilters()");
        context.execute("conceptosDialogo.hide()");
        //context.update("formularioDialogos:LOVConceptos");
    }

    public void cancelarCambiosConceptos() {
        lovfiltradoslistaConceptos = null;
        conceptosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVConceptos:globalFilter");
        context.execute("LOVConceptos.clearFilters()");
        context.execute("conceptosDialogo.hide()");
    }

    public void actualizarTiposRedondeos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaConceptosRedondeos.get(index).setTiporedondeo(tiposRedondeosSeleccionado);
                if (!listaConceptosRedondeosCrear.contains(listaConceptosRedondeos.get(index))) {
                    if (listaConceptosRedondeosModificar.isEmpty()) {
                        listaConceptosRedondeosModificar.add(listaConceptosRedondeos.get(index));
                    } else if (!listaConceptosRedondeosModificar.contains(listaConceptosRedondeos.get(index))) {
                        listaConceptosRedondeosModificar.add(listaConceptosRedondeos.get(index));
                    }
                }
            } else {
                filtradoListaConceptosRedondeos.get(index).setTiporedondeo(tiposRedondeosSeleccionado);
                if (!listaConceptosRedondeosCrear.contains(filtradoListaConceptosRedondeos.get(index))) {
                    if (listaConceptosRedondeosModificar.isEmpty()) {
                        listaConceptosRedondeosModificar.add(filtradoListaConceptosRedondeos.get(index));
                    } else if (!listaConceptosRedondeosModificar.contains(filtradoListaConceptosRedondeos.get(index))) {
                        listaConceptosRedondeosModificar.add(filtradoListaConceptosRedondeos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            cambiosPagina = false;
            permitirIndex = true;
            context.update("form:datosConceptosRedondeos");
        } else if (tipoActualizacion == 1) {
            nuevoConceptoRedondeo.setTiporedondeo(tiposRedondeosSeleccionado);
            context.update("formularioDialogos:nuevoTipo");
        } else if (tipoActualizacion == 2) {
            duplicarConceptoRedondeo.setTiporedondeo(tiposRedondeosSeleccionado);
            context.update("formularioDialogos:duplicarTipo");

        }
        lovfiltradoslistaTiposRedondeos = null;
        tiposRedondeosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVTiposRedondeos:globalFilter");
        context.execute("LOVTiposRedondeos.clearFilters()");
        context.execute("tiposRedondeosDialogo.hide()");
        //context.update("formularioDialogos:LOVTiposRedondeos");
    }

    public void cancelarCambiosTiposRedondeos() {
        lovfiltradoslistaTiposRedondeos = null;
        tiposRedondeosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTiposRedondeos:globalFilter");
        context.execute("LOVTiposRedondeos.clearFilters()");
        context.execute("tiposRedondeosDialogo.hide()");
    }

    public void actualizarConceptosRedondeos() {
        System.out.println("conceptosRedondeosSeleccionado : " + conceptosRedondeosSeleccionado.getConcepto().getDescripcion());
        RequestContext context = RequestContext.getCurrentInstance();

        if (!listaConceptosRedondeos.isEmpty()) {
            listaConceptosRedondeos.clear();
            listaConceptosRedondeos.add(conceptosRedondeosSeleccionado);
        } else {
            listaConceptosRedondeos.add(conceptosRedondeosSeleccionado);
        }

        context.reset("formularioDialogos:LOVConceptosRedondeos:globalFilter");
        context.execute("LOVConceptosRedondeos.clearFilters()");
        context.execute("conceptosRedondeosDialogo.hide()");
        //context.update("formularioDialogos:LOVConceptosRedondeos");
        context.update("form:datosConceptosRedondeos");
        filtradoListaConceptosRedondeos = null;
        conceptosRedondeosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void cancelarCambiosConceptosRedondeos() {
        filtradoListaConceptosRedondeos = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVConceptosRedondeos:globalFilter");
        context.execute("LOVConceptosRedondeos.clearFilters()");
        context.execute("conceptosRedondeosDialogo.hide()");
    }

    public void mostrarTodos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaConceptosRedondeos.isEmpty()) {
            listaConceptosRedondeos.clear();
            listaConceptosRedondeos = administrarConceptosRedondeos.consultarConceptosRedondeos();
        } else {
            listaConceptosRedondeos = administrarConceptosRedondeos.consultarConceptosRedondeos();
        }

        context.update("form:datosConceptosRedondeos");
        filtradoListaConceptosRedondeos = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
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
            context.update("formularioDialogos:conceptosDialogo");
            context.execute("conceptosDialogo.show()");
        } else if (dlg == 1) {
            context.update("formularioDialogos:tiposRedondeosDialogo");
            context.execute("tiposRedondeosDialogo.show()");
        } else if (dlg == 2) {
            context.update("formularioDialogos:conceptosRedondeosDialogo");
            context.execute("conceptosRedondeosDialogo.show()");
        }
    }

    //CANCELAR MODIFICACIONES
    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacion() {
        //CANCELAR MODIFICACIONES
        if (bandera == 1) {
            altoScrollConceptosRedondeos = "245";
            conceptoRedondeoConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoConcepto");
            conceptoRedondeoConcepto.setFilterStyle("display: none; visibility: hidden;");
            conceptoRedondeoTipoRedondeo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoTipoRedondeo");
            conceptoRedondeoTipoRedondeo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
            bandera = 0;
            filtradoListaConceptosRedondeos = null;
            tipoLista = 0;
        }

        listaConceptosRedondeosBorrar.clear();
        listaConceptosRedondeosCrear.clear();
        listaConceptosRedondeosModificar.clear();
        cambiosPagina = true;
        index = -1;
        secRegistro = null;
        k = 0;
        listaConceptosRedondeos = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosConceptosRedondeos");
        context.update("form:ACEPTAR");
    }

    public void cancelarCambioConceptosRedondeos() {
        lovfiltradoslistaConceptos = null;
        conceptosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
    }

    //MOSTRAR DATOS CELDA
    /**
     * Metodo que muestra los dialogos de editar con respecto a la lista real o
     * la lista filtrada y a la columna
     */
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarConceptosRedondeos = listaConceptosRedondeos.get(index);
            }
            if (tipoLista == 1) {
                editarConceptosRedondeos = filtradoListaConceptosRedondeos.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                context.update("formularioDialogos:editarConcepto");
                context.execute("editarConcepto.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editarTipoRedondeo");
                context.execute("editarTipoRedondeo.show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    //CREAR CONCEPTOS REDONDEO
    /**
     * Metodo que se encarga de agregar un nuevo concepto redondeo
     */
    public void agregarNuevoConceptoRedondeo() {

        mensajeValidacion = new String();
        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoConceptoRedondeo.getConcepto().getDescripcion() == " ") {
            mensajeValidacion = mensajeValidacion + " * Concepto\n";
            pasa++;
        }
        if (nuevoConceptoRedondeo.getTiporedondeo().getDescripcion().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Tipo Redondeo\n";
            pasa++;
        }

        if (pasa != 0) {
            context.update("formularioDialogos:validacionNuevoConceptoRedondeo");
            context.execute("validacionNuevoConceptoRedondeo.show()");
        }

        if (bandera == 1) {
            altoScrollConceptosRedondeos = "245";
            conceptoRedondeoConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoConcepto");
            conceptoRedondeoConcepto.setFilterStyle("display: none; visibility: hidden;");
            conceptoRedondeoTipoRedondeo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoTipoRedondeo");
            conceptoRedondeoTipoRedondeo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
            bandera = 0;
            filtradoListaConceptosRedondeos = null;
            tipoLista = 0;
        }
        //AGREGAR REGISTRO A LA LISTA NOVEDADES .
        k++;
        l = BigInteger.valueOf(k);
        nuevoConceptoRedondeo.setSecuencia(l);

        cambiosPagina = false;
        context.update("form:ACEPTAR");
        listaConceptosRedondeosCrear.add(nuevoConceptoRedondeo);
        listaConceptosRedondeos.add(nuevoConceptoRedondeo);
        nuevoConceptoRedondeo = new ConceptosRedondeos();
        context.update("form:datosConceptosRedondeos");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:aceptar");
        }
        context.execute("NuevoRegistroConceptosRedondeos.hide()");
        index = -1;
        secRegistro = null;
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("CONCEPTO")) {
            if (tipoNuevo == 1) {
                Concepto = nuevoConceptoRedondeo.getConcepto().getDescripcion();
            } else if (tipoNuevo == 2) {
                Concepto = duplicarConceptoRedondeo.getConcepto().getDescripcion();
            }
        } else if (Campo.equals("TIPO")) {
            if (tipoNuevo == 1) {
                TipoRedondeo = nuevoConceptoRedondeo.getTiporedondeo().getDescripcion();
            } else if (tipoNuevo == 2) {
                TipoRedondeo = duplicarConceptoRedondeo.getTiporedondeo().getDescripcion();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            if (tipoNuevo == 1) {
                nuevoConceptoRedondeo.getConcepto().setDescripcion(Concepto);
            } else if (tipoNuevo == 2) {
                duplicarConceptoRedondeo.getConcepto().setDescripcion(Concepto);
            }
            for (int i = 0; i < lovlistaConceptos.size(); i++) {
                if (lovlistaConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoConceptoRedondeo.setConcepto(lovlistaConceptos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoConcepto");
                } else if (tipoNuevo == 2) {
                    duplicarConceptoRedondeo.setConcepto(lovlistaConceptos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarConcepto");
                }
                lovlistaConceptos.clear();
                getLovlistaConceptos();
            } else {
                context.update("form:conceptosDialogo");
                context.execute("conceptosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoConcepto");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarConcepto");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TIPO")) {
            if (tipoNuevo == 1) {
                nuevoConceptoRedondeo.getTiporedondeo().setDescripcion(TipoRedondeo);
            } else if (tipoNuevo == 2) {
                duplicarConceptoRedondeo.getTiporedondeo().setDescripcion(TipoRedondeo);
            }

            for (int i = 0; i < lovlistaTiposRedondeos.size(); i++) {
                if (lovlistaTiposRedondeos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoConceptoRedondeo.setTiporedondeo(lovlistaTiposRedondeos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoTipo");
                } else if (tipoNuevo == 2) {
                    duplicarConceptoRedondeo.setTiporedondeo(lovlistaTiposRedondeos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarTipo");
                }
                lovlistaTiposRedondeos.clear();
                getLovlistaTiposRedondeos();
            } else {
                context.update("form:tiposRedondeosDialogo");
                context.execute("tiposRedondeosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoTipo");

                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarTipo");

                }
            }

        }
    }
    //LIMPIAR NUEVO REGISTRO

    /**
     * Metodo que limpia las casillas del nuevo Concepto Redondeo
     */
    public void limpiarNuevaConceptosRedondeos() {
        nuevoConceptoRedondeo = new ConceptosRedondeos();
        index = -1;
        secRegistro = null;
    }

    //DUPLICAR VC
    /**
     * Metodo que duplica un ConceptoRedondeo especifico dado por la posicion de
     * la fila
     */
    public void duplicarConceptosRedondeos() {
        if (index >= 0) {
            duplicarConceptoRedondeo = new ConceptosRedondeos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarConceptoRedondeo.setSecuencia(l);
                duplicarConceptoRedondeo.setConcepto(listaConceptosRedondeos.get(index).getConcepto());
                duplicarConceptoRedondeo.setTiporedondeo(listaConceptosRedondeos.get(index).getTiporedondeo());
            }
            if (tipoLista == 1) {

                duplicarConceptoRedondeo.setSecuencia(l);
                duplicarConceptoRedondeo.setConcepto(filtradoListaConceptosRedondeos.get(index).getConcepto());
                duplicarConceptoRedondeo.setTiporedondeo(filtradoListaConceptosRedondeos.get(index).getTiporedondeo());

            }
            System.out.println("Concepto Duplicar : " + listaConceptosRedondeos.get(index).getConcepto());
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:DuplicarRegistroConceptosRedondeos");
            context.execute("DuplicarRegistroConceptosRedondeos.show()");
            index = -1;
            secRegistro = null;
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * ConceptosRedondeos
     */
    public void confirmarDuplicar() {

        RequestContext context = RequestContext.getCurrentInstance();

        if (bandera == 1) {
            altoScrollConceptosRedondeos = "245";
            conceptoRedondeoConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoConcepto");
            conceptoRedondeoConcepto.setFilterStyle("display: none; visibility: hidden;");
            conceptoRedondeoTipoRedondeo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoTipoRedondeo");
            conceptoRedondeoTipoRedondeo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
            bandera = 0;
            filtradoListaConceptosRedondeos = null;
            tipoLista = 0;
        }
        //AGREGAR REGISTRO A LA LISTA NOVEDADES .
        k++;
        l = BigInteger.valueOf(k);
        duplicarConceptoRedondeo.setSecuencia(l);

        cambiosPagina = false;
        context.update("form:ACEPTAR");
        listaConceptosRedondeosCrear.add(duplicarConceptoRedondeo);
        listaConceptosRedondeos.add(duplicarConceptoRedondeo);
        duplicarConceptoRedondeo = new ConceptosRedondeos();
        context.update("form:datosConceptosRedondeos");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:aceptar");
        }
        context.execute("DuplicarRegistroConceptosRedondeos.hide()");
        index = -1;
        secRegistro = null;

        duplicarConceptoRedondeo = new ConceptosRedondeos();
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar ConceptoRedondeo
     */
    public void limpiarDuplicarConceptosRedondeos() {
        duplicarConceptoRedondeo = new ConceptosRedondeos();
    }

    //BORRAR VC
    /**
     * Metodo que borra los ConceptosRedondeos seleccionados
     */
    public void borrarConceptosRedondeos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listaConceptosRedondeosModificar.isEmpty() && listaConceptosRedondeosModificar.contains(listaConceptosRedondeos.get(index))) {
                    int modIndex = listaConceptosRedondeosModificar.indexOf(listaConceptosRedondeos.get(index));
                    listaConceptosRedondeosModificar.remove(modIndex);
                    listaConceptosRedondeosBorrar.add(listaConceptosRedondeos.get(index));
                } else if (!listaConceptosRedondeosCrear.isEmpty() && listaConceptosRedondeosCrear.contains(listaConceptosRedondeos.get(index))) {
                    int crearIndex = listaConceptosRedondeosCrear.indexOf(listaConceptosRedondeos.get(index));
                    listaConceptosRedondeosCrear.remove(crearIndex);
                } else {
                    listaConceptosRedondeosBorrar.add(listaConceptosRedondeos.get(index));
                }
                listaConceptosRedondeos.remove(index);
            }
            if (tipoLista == 1) {
                if (!listaConceptosRedondeosModificar.isEmpty() && listaConceptosRedondeosModificar.contains(filtradoListaConceptosRedondeos.get(index))) {
                    int modIndex = listaConceptosRedondeosModificar.indexOf(filtradoListaConceptosRedondeos.get(index));
                    listaConceptosRedondeosModificar.remove(modIndex);
                    listaConceptosRedondeosBorrar.add(filtradoListaConceptosRedondeos.get(index));
                } else if (!listaConceptosRedondeosCrear.isEmpty() && listaConceptosRedondeosCrear.contains(filtradoListaConceptosRedondeos.get(index))) {
                    int crearIndex = listaConceptosRedondeosCrear.indexOf(filtradoListaConceptosRedondeos.get(index));
                    listaConceptosRedondeosCrear.remove(crearIndex);
                } else {
                    listaConceptosRedondeosBorrar.add(filtradoListaConceptosRedondeos.get(index));
                }
                int VCIndex = listaConceptosRedondeos.indexOf(filtradoListaConceptosRedondeos.get(index));
                listaConceptosRedondeos.remove(VCIndex);
                filtradoListaConceptosRedondeos.remove(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosConceptosRedondeos");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
        }
    }
    //CTRL + F11 ACTIVAR/DESACTIVAR

    /**
     * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        if (bandera == 0) {
            altoScrollConceptosRedondeos = "223";
            conceptoRedondeoConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoConcepto");
            conceptoRedondeoConcepto.setFilterStyle("width: 60px");
            conceptoRedondeoTipoRedondeo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoTipoRedondeo");
            conceptoRedondeoTipoRedondeo.setFilterStyle("width: 60px");
            RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
            bandera = 1;

        } else if (bandera == 1) {
            altoScrollConceptosRedondeos = "245";
            conceptoRedondeoConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoConcepto");
            conceptoRedondeoConcepto.setFilterStyle("display: none; visibility: hidden;");
            conceptoRedondeoTipoRedondeo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoTipoRedondeo");
            conceptoRedondeoTipoRedondeo.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");

            bandera = 0;
            filtradoListaConceptosRedondeos = null;
            tipoLista = 0;
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (bandera == 1) {
            altoScrollConceptosRedondeos = "245";
            conceptoRedondeoConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoConcepto");
            conceptoRedondeoConcepto.setFilterStyle("display: none; visibility: hidden;");
            conceptoRedondeoTipoRedondeo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoTipoRedondeo");
            conceptoRedondeoTipoRedondeo.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");

            bandera = 0;
            filtradoListaConceptosRedondeos = null;
            tipoLista = 0;
        }

        listaConceptosRedondeosBorrar.clear();
        listaConceptosRedondeosCrear.clear();
        listaConceptosRedondeosModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaConceptosRedondeos = null;
        guardado = true;
    }

    /**
     * Metodo que activa el boton aceptar de la pantalla y dialogos
     */
    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    /**
     * Metodo que exporta datos a PDF
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosRedondeosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ConceptosRedondeosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosRedondeosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ConceptosRedondeosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }
    //EVENTO FILTRAR

    /**
     * Evento que cambia la lista reala a la filtrada
     */
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }
    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {

        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listaConceptosRedondeos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "CONCEPTOSREDONDEOS");
                System.out.println("resultado: " + resultado);
                if (resultado == 1) {
                    context.execute("errorObjetosDB.show()");
                } else if (resultado == 2) {
                    context.execute("confirmarRastro.show()");
                } else if (resultado == 3) {
                    context.execute("errorRegistroRastro.show()");
                } else if (resultado == 4) {
                    context.execute("errorTablaConRastro.show()");
                } else if (resultado == 5) {
                    context.execute("errorTablaSinRastro.show()");
                }
            } else {
                context.execute("seleccionarRegistro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("CONCEPTOSREDONDEOS")) {
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

    //GUARDAR
    public void guardarCambiosConceptosRedondeos() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones ConceptosRedondeos");

            if (!listaConceptosRedondeosBorrar.isEmpty()) {
                for (int i = 0; i < listaConceptosRedondeosBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaConceptosRedondeosBorrar.size());
                    administrarConceptosRedondeos.borrarConceptosRedondeos(listaConceptosRedondeosBorrar.get(i));
                }
                System.out.println("Entra");
                listaConceptosRedondeosBorrar.clear();
            }

            if (!listaConceptosRedondeosCrear.isEmpty()) {
                for (int i = 0; i < listaConceptosRedondeosCrear.size(); i++) {
                    System.out.println("Creando...");
                    administrarConceptosRedondeos.crearConceptosRedondeos(listaConceptosRedondeosCrear.get(i));
                }
                System.out.println("LimpiaLista");
                listaConceptosRedondeosCrear.clear();
            }
            if (!listaConceptosRedondeosModificar.isEmpty()) {
                administrarConceptosRedondeos.modificarConceptosRedondeos(listaConceptosRedondeosModificar);
                listaConceptosRedondeosModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaConceptosRedondeos = null;

            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = true;
            context.update("form:ACEPTAR");
            context.update("form:datosConceptosRedondeos");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:aceptar");
            //  k = 0;
        }
        index = -1;
        secRegistro = null;
    }

    public void recibirPagina(String paginaAnterior){
        this.paginaAnterior = paginaAnterior;
    }
    
    public String volverPaginaAnterior() {
        return paginaAnterior;
    }
    //GETTERS AND SETTERS
    /**
     * Metodo que obtiene la lista de ConceptosRedondeos de un Empleado, en caso
     * de que sea null hace el llamado al metodo de obtener ConceptosRedondeos
     * del empleado, en caso contrario no genera operaciones
     *
     * @return listS Lista de ConceptosRedondeos de una Persona
     */
    public List<ConceptosRedondeos> getListaConceptosRedondeos() {
        if (listaConceptosRedondeos == null) {
            listaConceptosRedondeos = administrarConceptosRedondeos.consultarConceptosRedondeos();
        }
        return listaConceptosRedondeos;
    }

    public void setListaConceptosRedondeos(List<ConceptosRedondeos> listaConceptosRedondeos) {
        this.listaConceptosRedondeos = listaConceptosRedondeos;
    }

    public List<ConceptosRedondeos> getLovlistaConceptosRedondeos() {
        if (lovlistaConceptosRedondeos == null) {
            lovlistaConceptosRedondeos = administrarConceptosRedondeos.consultarConceptosRedondeos();
        }
        return lovlistaConceptosRedondeos;
    }

    public void setLovlistaConceptosRedondeos(List<ConceptosRedondeos> lovlistaConceptosRedondeos) {
        this.lovlistaConceptosRedondeos = lovlistaConceptosRedondeos;
    }

    public List<ConceptosRedondeos> getLovfiltradoslistaConceptosRedondeos() {
        return lovfiltradoslistaConceptosRedondeos;
    }

    public void setLovfiltradoslistaConceptosRedondeos(List<ConceptosRedondeos> lovfiltradoslistaConceptosRedondeos) {
        this.lovfiltradoslistaConceptosRedondeos = lovfiltradoslistaConceptosRedondeos;
    }

    public ConceptosRedondeos getConceptosRedondeosSeleccionado() {
        return conceptosRedondeosSeleccionado;
    }

    public void setConceptosRedondeosSeleccionado(ConceptosRedondeos conceptosRedondeosSeleccionado) {
        this.conceptosRedondeosSeleccionado = conceptosRedondeosSeleccionado;
    }

    public List<ConceptosRedondeos> getFiltradoListaConceptosRedondeos() {
        return filtradoListaConceptosRedondeos;
    }

    public void setFiltrarConceptosRedondeos(List<ConceptosRedondeos> filtradoListaConceptosRedondeos) {
        this.filtradoListaConceptosRedondeos = filtradoListaConceptosRedondeos;
    }

    public ConceptosRedondeos getNuevoConceptoRedondeo() {
        return nuevoConceptoRedondeo;
    }

    public void setNuevoConceptoRedondeo(ConceptosRedondeos nuevoConceptoRedondeo) {
        this.nuevoConceptoRedondeo = nuevoConceptoRedondeo;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public ConceptosRedondeos getEditarConceptosRedondeos() {
        return editarConceptosRedondeos;
    }

    public void setEditarConceptosRedondeos(ConceptosRedondeos editarConceptoRedondeo) {
        this.editarConceptosRedondeos = editarConceptoRedondeo;
    }

    public ConceptosRedondeos getDuplicarConceptoRedondeo() {
        return duplicarConceptoRedondeo;
    }

    public void setDuplicarConceptoRedondeo(ConceptosRedondeos duplicarConceptoRedondeo) {
        this.duplicarConceptoRedondeo = duplicarConceptoRedondeo;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public List<Conceptos> getLovlistaConceptos() {
        if (lovlistaConceptos == null) {
            lovlistaConceptos = administrarConceptosRedondeos.lovConceptos();
        }
        return lovlistaConceptos;
    }

    public void setLovlistaConceptos(List<Conceptos> lovlistaConceptos) {
        this.lovlistaConceptos = lovlistaConceptos;
    }

    public List<Conceptos> getLovfiltradoslistaConceptos() {
        return lovfiltradoslistaConceptos;
    }

    public void setLovfiltradoslistaConceptos(List<Conceptos> lovfiltradoslistaConceptos) {
        this.lovfiltradoslistaConceptos = lovfiltradoslistaConceptos;
    }

    public Conceptos getConceptosSeleccionado() {
        return conceptosSeleccionado;
    }

    public void setConceptosSeleccionado(Conceptos conceptosSeleccionado) {
        this.conceptosSeleccionado = conceptosSeleccionado;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getAltoScrollConceptosRedondeos() {
        return altoScrollConceptosRedondeos;
    }

    public void setAltoScrollConceptosRedondeos(String altoScrollConceptosRedondeos) {
        this.altoScrollConceptosRedondeos = altoScrollConceptosRedondeos;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public List<TiposRedondeos> getLovlistaTiposRedondeos() {
        if (lovlistaTiposRedondeos == null) {
            lovlistaTiposRedondeos = administrarConceptosRedondeos.lovTiposRedondeos();
        }
        return lovlistaTiposRedondeos;
    }

    public void setLovlistaTiposRedondeos(List<TiposRedondeos> lovlistaTiposRedondeos) {
        this.lovlistaTiposRedondeos = lovlistaTiposRedondeos;
    }

    public List<TiposRedondeos> getLovfiltradoslistaTiposRedondeos() {
        return lovfiltradoslistaTiposRedondeos;
    }

    public void setLovfiltradoslistaTiposRedondeos(List<TiposRedondeos> lovfiltradoslistaTiposRedondeos) {
        this.lovfiltradoslistaTiposRedondeos = lovfiltradoslistaTiposRedondeos;
    }

    public TiposRedondeos getTiposRedondeosSeleccionado() {
        return tiposRedondeosSeleccionado;
    }

    public void setTiposRedondeosSeleccionado(TiposRedondeos tiposRedondeosSeleccionado) {
        this.tiposRedondeosSeleccionado = tiposRedondeosSeleccionado;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

}
