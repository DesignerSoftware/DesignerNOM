package Controlador;

import Entidades.Declarantes;
import Entidades.Personas;
import Entidades.RetencionesMinimas;
import Entidades.TarifaDeseo;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarDeclarantesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
 * @author Victor Algarin
 */
@ManagedBean
@SessionScoped
public class ControlDeclarantes implements Serializable {

    @EJB
    AdministrarDeclarantesInterface administrarDeclarantes;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Declarantes> listaDeclarantes;
    private List<Declarantes> filtradoListaDeclarantes;
    private Declarantes declaranteSeleccionado;
    private Personas persona;
    private Column declarantesFechaInicial, declarantesFechaFinal, declarantesBooleano, declarantesPromedio, declarantesTarifa;
    private int tipoActualizacion;
    private int bandera;
    private boolean permitirIndex;
    private boolean aceptar;
    private BigInteger Minima;
    private String altoScrollDeclarantes;
    private List<Declarantes> listaDeclarantesModificar;
    private boolean guardado, guardarOk;
    public Declarantes nuevoDeclarante;
    private List<Declarantes> listaDeclarantesCrear;
    private BigInteger l;
    private int k;
    private String mensajeValidacion;
    private List<Declarantes> listaDeclarantesBorrar;
    private Declarantes editarDeclarantes;
    private int cualCelda, tipoLista;
    private boolean cambioEditor, aceptarEditar;
    private Declarantes duplicarDeclarante;
    private boolean cambiosPagina;
    private List<TarifaDeseo> lovlistaRetenciones;
    private List<TarifaDeseo> lovfiltradoslistaRetenciones;
    private TarifaDeseo retencionesSeleccionado;
    private Date fechaFinal;
    private Date fechaInicial;
    private Date fechaParametro;
    private String infoRegistro, infoRegistroLov;

    public ControlDeclarantes() {
        altoScrollDeclarantes = "270";
        permitirIndex = true;
        listaDeclarantes = null;
        aceptar = true;
        listaDeclarantesBorrar = new ArrayList<Declarantes>();
        listaDeclarantesCrear = new ArrayList<Declarantes>();
        k = 0;
        listaDeclarantesModificar = new ArrayList<Declarantes>();
        editarDeclarantes = new Declarantes();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        guardado = true;
        nuevoDeclarante = new Declarantes();
        duplicarDeclarante = new Declarantes();
        declaranteSeleccionado = null;
        cambiosPagina = true;
        fechaFinal = new Date("31/12/9999");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarDeclarantes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void actualizarRetenciones() {
        RequestContext context = RequestContext.getCurrentInstance();
        List<RetencionesMinimas> listaRetenciones = administrarDeclarantes.retencionesMinimasLista();
        RetencionesMinimas seleccionado = new RetencionesMinimas();
        System.out.println("retencionesSeleccionado.getSecuenciaRetencion() : " + retencionesSeleccionado.getSecuenciaRetencion());
        System.out.println("listaRetenciones : " + listaRetenciones.size());
        for (int j = 0; j < listaRetenciones.size(); j++) {
            System.out.println("listaRetenciones : " + listaRetenciones.get(j).getSecuencia());
        }
        for (int i = 0; i < listaRetenciones.size(); i++) {
            BigInteger secuencia = new BigInteger(listaRetenciones.get(i).getSecuencia().toString());
            System.out.println("secuencia : " + secuencia);
            if (secuencia.equals(retencionesSeleccionado.getSecuenciaRetencion())) {
                seleccionado = listaRetenciones.get(i);
                break;
            }
        }
        if (tipoActualizacion == 0) {
            declaranteSeleccionado.setRetencionminima(seleccionado);
            if (!listaDeclarantesCrear.contains(declaranteSeleccionado)) {
                if (listaDeclarantesModificar.isEmpty()) {
                    listaDeclarantesModificar.add(declaranteSeleccionado);
                } else if (!listaDeclarantesModificar.contains(declaranteSeleccionado)) {
                    listaDeclarantesModificar.add(declaranteSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            cambiosPagina = false;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosDeclarantes");
        } else if (tipoActualizacion == 1) {
            nuevoDeclarante.setRetencionminima(seleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDeclarante");
        } else if (tipoActualizacion == 2) {
            duplicarDeclarante.setRetencionminima(seleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDeclarante");
        }
        filtradoListaDeclarantes = null;
        retencionesSeleccionado = null;
        aceptar = true;
        declaranteSeleccionado = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVMinimas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVMinimas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('minimasDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVMinimas");
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (declaranteSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 3) {
                lovlistaRetenciones = null;
                getLovlistaRetenciones();
                contarRegistrosLov();
                RequestContext.getCurrentInstance().update("formularioDialogos:minimasDialogo");
                RequestContext.getCurrentInstance().execute("PF('minimasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void recibirPersona(Personas per) {
        persona = per;
        listaDeclarantes = null;
        getListaDeclarantes();
        if (listaDeclarantes != null) {
            if (!listaDeclarantes.isEmpty()) {
                declaranteSeleccionado = listaDeclarantes.get(0);
            }
        }
    }

    //AUTOCOMPLETAR
    public void modificarDeclarantes(Declarantes declarante, String confirmarCambio, String valorConfirmar) {
        declaranteSeleccionado = declarante;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        System.out.println("Entro a Modificar Declarantes");

        for (int i = 0; i < listaDeclarantes.size(); i++) {
            if (declaranteSeleccionado.getFechainicial().after(declaranteSeleccionado.getFechainicial()) && declaranteSeleccionado.getFechainicial().before(declaranteSeleccionado.getFechafinal())) {
                declaranteSeleccionado.setFechainicial(fechaInicial);
                RequestContext.getCurrentInstance().update("formularioDialogos:fechasTraslapadas");
                RequestContext.getCurrentInstance().execute("PF('fechasTraslapadas').show()");
                RequestContext.getCurrentInstance().update("form:datosDeclarantes");
                break;
            }
        }

        if (declaranteSeleccionado.getFechafinal().before(declaranteSeleccionado.getFechainicial())) {
            declaranteSeleccionado.setFechafinal(fechaFinal);
            RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
            RequestContext.getCurrentInstance().execute("PF('fechas').show()");
            RequestContext.getCurrentInstance().update("form:datosDeclarantes");
        }

        if (declaranteSeleccionado.getFechainicial().after(declaranteSeleccionado.getFechafinal())) {
            declaranteSeleccionado.setFechainicial(fechaInicial);
            RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
            RequestContext.getCurrentInstance().execute("PF('fechas').show()");
            RequestContext.getCurrentInstance().update("form:datosDeclarantes");
        }
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (!listaDeclarantesCrear.contains(declaranteSeleccionado)) {

                if (listaDeclarantesModificar.isEmpty()) {
                    listaDeclarantesModificar.add(declaranteSeleccionado);
                } else if (!listaDeclarantesModificar.contains(declaranteSeleccionado)) {
                    listaDeclarantesModificar.add(declaranteSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                }
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosDeclarantes");
        } else if (confirmarCambio.equalsIgnoreCase("MINIMA")) {
            declaranteSeleccionado.getRetencionminima().setRetencion(Minima);

            for (int i = 0; i < lovlistaRetenciones.size(); i++) {
                if ((lovlistaRetenciones.get(i).getRetencion().toString()).startsWith(valorConfirmar.toString().toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                List<RetencionesMinimas> listaRetenciones = administrarDeclarantes.retencionesMinimasLista();
                TarifaDeseo ratifa = lovlistaRetenciones.get(indiceUnicoElemento);
                RetencionesMinimas seleccionado = new RetencionesMinimas();
                for (int i = 0; i < listaRetenciones.size(); i++) {
                    if (listaRetenciones.get(i).getSecuencia().equals(ratifa.getSecuenciaRetencion())) {
                        seleccionado = listaRetenciones.get(i);
                        break;
                    }
                }
                declaranteSeleccionado.setRetencionminima(seleccionado);
                lovlistaRetenciones.clear();
                getLovlistaRetenciones();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:minimasDialogo");
                RequestContext.getCurrentInstance().execute("PF('minimasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void modificarFechas(Declarantes declarante, int c) {
        Declarantes auxiliar = null;
        RequestContext context = RequestContext.getCurrentInstance();
        auxiliar = declaranteSeleccionado;
        if (auxiliar.getFechainicial() != null && auxiliar.getFechafinal() != null) {
            boolean solapado = false;
            for (int y = 0; y < listaDeclarantes.size(); y++) {
                if (declaranteSeleccionado.getFechainicial().after(listaDeclarantes.get(y).getFechainicial()) && declaranteSeleccionado.getFechainicial().before(listaDeclarantes.get(y).getFechafinal())) {
                    solapado = true;
                    break;
                }
            }
            if (solapado == false) {
                boolean retorno = false;
                declaranteSeleccionado = declarante;
                retorno = validarFechasRegistro(0);
                if (retorno == true) {
                    cambiarIndice(declaranteSeleccionado, c);
                    modificarDeclarantes(declaranteSeleccionado, "N", "");
                } else {
                    declaranteSeleccionado.setFechafinal(fechaFinal);
                    declaranteSeleccionado.setFechainicial(fechaInicial);
                    RequestContext.getCurrentInstance().update("form:datosDeclarantes");
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            } else {
                declaranteSeleccionado.setFechainicial(fechaInicial);
                RequestContext.getCurrentInstance().update("formularioDialogos:fechasTraslapadas");
                RequestContext.getCurrentInstance().execute("PF('fechasTraslapadas').show()");
                RequestContext.getCurrentInstance().update("form:datosDeclarantes");
            }
        } else {
            declaranteSeleccionado.setFechainicial(fechaInicial);
            declaranteSeleccionado.setFechafinal(fechaFinal);
            RequestContext.getCurrentInstance().update("form:datosDeclarantes");
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    public boolean validarFechasRegistro(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        System.err.println("fechaparametro : " + fechaParametro);
        boolean retorno = true;
        if (i == 0) {
            Declarantes auxiliar = null;
            if (tipoLista == 0) {
                auxiliar = declaranteSeleccionado;
            }
            if (tipoLista == 1) {
                auxiliar = declaranteSeleccionado;
            }
            if (auxiliar.getFechainicial().before(auxiliar.getFechafinal())) {
                retorno = true;
            } else {
                retorno = false;
            }

        }
        if (i == 1) {
            if (nuevoDeclarante.getFechainicial().before(nuevoDeclarante.getFechafinal())) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarDeclarante.getFechainicial().before(duplicarDeclarante.getFechafinal())) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    //Ubicacion Celda.
    /**
     * Metodo que obtiene la posicion dentro de la tabla Declarantes
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    //UBICACION CELDA
    public void cambiarIndice(Declarantes declarante, int celda) {
        if (permitirIndex == true) {
            declaranteSeleccionado = declarante;
            cualCelda = celda;
            fechaFinal = declaranteSeleccionado.getFechafinal();
            fechaInicial = declaranteSeleccionado.getFechainicial();
            declaranteSeleccionado.getSecuencia();
            if (cualCelda == 3) {
                Minima = declaranteSeleccionado.getRetencionminima().getRetencion();
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.reset("formularioDialogos:LOVMinimas:globalFilter");
            RequestContext.getCurrentInstance().update("formularioDialogos:LOVMinimas");
        }
    }
    //FALTA GUARDAR

    public void asignarIndex(Declarantes declarante, int dlg, int LND) {
        System.out.println("Controlador.ControlDeclarantes.asignarIndex()");
        declaranteSeleccionado = declarante;
        tipoActualizacion = LND;

        if (dlg == 0) {
            lovlistaRetenciones = null;
            getLovlistaRetenciones();
            contarRegistrosLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:minimasDialogo");
            RequestContext.getCurrentInstance().execute("PF('minimasDialogo').show()");
        }
    }

    //CANCELAR MODIFICACIONES
    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacion() {
        //CANCELAR MODIFICACIONES
        if (bandera == 1) {
            cerrarFiltrado();
        }

        listaDeclarantesBorrar.clear();
        listaDeclarantesCrear.clear();
        listaDeclarantesModificar.clear();
        cambiosPagina = true;
        declaranteSeleccionado = null;
        k = 0;
        listaDeclarantes = null;
        getListaDeclarantes();
        contarRegistros();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosDeclarantes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cancelarCambioDeclarantes() {
        lovfiltradoslistaRetenciones = null;
        retencionesSeleccionado = null;
        aceptar = true;
        declaranteSeleccionado = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVMinimas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVMinimas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('minimasDialogo').hide()");
    }

    public void editarCelda() {
        if (declaranteSeleccionado != null) {
            editarDeclarantes = declaranteSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicial");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicial').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinal");
                RequestContext.getCurrentInstance().execute("PF('editarFechaFinal').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDeseado");
                RequestContext.getCurrentInstance().execute("PF('editarDeseado').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMinima");
                RequestContext.getCurrentInstance().execute("PF('editarMinima').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoDeclarante() {

        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoDeclarante.getFechainicial() != null && nuevoDeclarante.getFechafinal() != null) {
            boolean validacion1 = validarFechasRegistro(1);
            if (validacion1 == true) {
                boolean validacion2 = true;
                for (int i = 0; i < listaDeclarantes.size(); i++) {
                    if (nuevoDeclarante.getFechainicial().before(declaranteSeleccionado.getFechafinal())) {
                        validacion2 = false;
                        break;
                    }
                }
                if (validacion2 == true) {
                    if (bandera == 1) {
                        cerrarFiltrado();
                    }
                    //AGREGAR REGISTRO A LA LISTA NOVEDADES .
                    k++;
                    l = BigInteger.valueOf(k);
                    nuevoDeclarante.setSecuencia(l);
                    nuevoDeclarante.setPersona(persona);

                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    listaDeclarantesCrear.add(nuevoDeclarante);
                    listaDeclarantes.add(nuevoDeclarante);
                    declaranteSeleccionado = nuevoDeclarante;
                    contarRegistros();
                    nuevoDeclarante = new Declarantes();
                    RequestContext.getCurrentInstance().update("form:datosDeclarantes");
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                    RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDeclarantes').hide()");
                } else {
                    System.out.println("traslapacion de fechas");
                    RequestContext.getCurrentInstance().update("formularioDialogos:fechasTraslapadas");
                    RequestContext.getCurrentInstance().execute("PF('fechasTraslapadas').show()");
                }
            } else {
                System.out.println("error fechas ingresadas");
                RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
                RequestContext.getCurrentInstance().execute("PF('fechas').show()");
            }
        } else {
            System.out.println("fechas obligatorias");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoDeclarante').show()");
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("MINIMA")) {
            if (tipoNuevo == 1) {
                nuevoDeclarante.getRetencionminima().setRetencion(Minima);
            } else if (tipoNuevo == 2) {
                duplicarDeclarante.getRetencionminima().setRetencion(Minima);
            }
            for (int i = 0; i < lovlistaRetenciones.size(); i++) {
                if ((lovlistaRetenciones.get(i).getRetencion().toString()).startsWith(valorConfirmar.toString().toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                List<RetencionesMinimas> listaRetenciones = administrarDeclarantes.retencionesMinimasLista();
                TarifaDeseo ratifa = lovlistaRetenciones.get(indiceUnicoElemento);
                RetencionesMinimas seleccionado = new RetencionesMinimas();
                for (int i = 0; i < listaRetenciones.size(); i++) {
                    if (listaRetenciones.get(i).getSecuencia().equals(ratifa.getSecuenciaRetencion())) {
                        seleccionado = listaRetenciones.get(i);
                        break;
                    }
                }
                if (tipoNuevo == 1) {
                    nuevoDeclarante.setRetencionminima(seleccionado);
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTarifaDeseo");
                } else if (tipoNuevo == 2) {
                    duplicarDeclarante.setRetencionminima(seleccionado);
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTarifaDeseo");
                }
                lovlistaRetenciones.clear();
                getLovlistaRetenciones();
            } else {
                RequestContext.getCurrentInstance().update("form:minimasDialogo");
                RequestContext.getCurrentInstance().execute("PF('minimasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTarifaDeseo");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTarifaDeseo");
                }
            }
        }
    }
    //LIMPIAR NUEVO REGISTRO

    /**
     * Metodo que limpia las casillas del nuevo Declarante
     */
    public void limpiarNuevaDeclarantes() {
        nuevoDeclarante = new Declarantes();
        declaranteSeleccionado = null;
    }

    //DUPLICAR VC
    /**
     * Metodo que duplica un Declarante especifico dado por la posicion de la
     * fila
     */
    public void duplicarDeclarantes() {
        if (declaranteSeleccionado != null) {
            duplicarDeclarante = new Declarantes();
            k++;
            l = BigInteger.valueOf(k);

            duplicarDeclarante.setSecuencia(l);
            duplicarDeclarante.setPersona(persona);
            duplicarDeclarante.setFechainicial(declaranteSeleccionado.getFechainicial());
            duplicarDeclarante.setFechafinal(declaranteSeleccionado.getFechafinal());
            duplicarDeclarante.setRetenciondeseada(declaranteSeleccionado.getRetenciondeseada());
            duplicarDeclarante.setRetencionminima(declaranteSeleccionado.getRetencionminima());
            duplicarDeclarante.setDeclarante(declaranteSeleccionado.getDeclarante());

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDeclarante");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDeclarantes').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * Declarantes
     */
    public void confirmarDuplicar() {

        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarDeclarante.getFechainicial() != null && duplicarDeclarante.getFechafinal() != null) {
            boolean validacion1 = validarFechasRegistro(1);
            if (validacion1 == true) {
                boolean validacion2 = true;
                for (int i = 0; i < listaDeclarantes.size(); i++) {
                    if (duplicarDeclarante.getFechainicial().after(declaranteSeleccionado.getFechainicial()) && duplicarDeclarante.getFechainicial().before(declaranteSeleccionado.getFechafinal())) {
                        validacion2 = false;
                        break;
                    }
                }
                if (validacion2 == true) {
                    if (bandera == 1) {
                        cerrarFiltrado();
                    }
                    //AGREGAR REGISTRO A LA LISTA NOVEDADES .
                    k++;
                    l = BigInteger.valueOf(k);
                    duplicarDeclarante.setSecuencia(l);
                    duplicarDeclarante.setPersona(persona);

                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    listaDeclarantesCrear.add(duplicarDeclarante);
                    listaDeclarantes.add(duplicarDeclarante);
                    declaranteSeleccionado = duplicarDeclarante;
                    duplicarDeclarante = new Declarantes();
                    RequestContext.getCurrentInstance().update("form:datosDeclarantes");
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                    RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDeclarantes').hide()");
                } else {
                    System.out.println("traslapacion de fechas");
                    RequestContext.getCurrentInstance().update("formularioDialogos:fechasTraslapadas");
                    RequestContext.getCurrentInstance().execute("PF('fechasTraslapadas').show()");
                }
            } else {
                System.out.println("error fechas ingresadas");
                RequestContext.getCurrentInstance().execute("PF('fecha').show()");
            }
        } else {
            System.out.println("fechas obligatorias");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoDeclarante').show()");
        }

        duplicarDeclarante = new Declarantes();
    }

    public void limpiarDuplicarDeclarantes() {
        duplicarDeclarante = new Declarantes();
    }

    public void borrarDeclarantes() {
        if (declaranteSeleccionado != null) {
            if (!listaDeclarantesModificar.isEmpty() && listaDeclarantesModificar.contains(declaranteSeleccionado)) {
                int modIndex = listaDeclarantesModificar.indexOf(declaranteSeleccionado);
                listaDeclarantesModificar.remove(modIndex);
                listaDeclarantesBorrar.add(declaranteSeleccionado);
            } else if (!listaDeclarantesCrear.isEmpty() && listaDeclarantesCrear.contains(declaranteSeleccionado)) {
                int crearIndex = listaDeclarantesCrear.indexOf(declaranteSeleccionado);
                listaDeclarantesCrear.remove(crearIndex);
            } else {
                listaDeclarantesBorrar.add(declaranteSeleccionado);
            }
            listaDeclarantes.remove(declaranteSeleccionado);
            if (tipoLista == 1) {
                filtradoListaDeclarantes.remove(declaranteSeleccionado);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDeclarantes");
            declaranteSeleccionado = null;
            contarRegistros();

            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            altoScrollDeclarantes = "250";
            declarantesFechaInicial = (Column) c.getViewRoot().findComponent("form:datosDeclarantes:declarantesFechaInicial");
            declarantesFechaInicial.setFilterStyle("width: 85% !important");
            declarantesFechaFinal = (Column) c.getViewRoot().findComponent("form:datosDeclarantes:declarantesFechaFinal");
            declarantesFechaFinal.setFilterStyle("width: 85% !important");
            declarantesBooleano = (Column) c.getViewRoot().findComponent("form:datosDeclarantes:declarantesBooleano");
            declarantesBooleano.setFilterStyle("width: 85% !important");
            declarantesPromedio = (Column) c.getViewRoot().findComponent("form:datosDeclarantes:declarantesPromedio");
            declarantesPromedio.setFilterStyle("width: 85% !important");
            declarantesTarifa = (Column) c.getViewRoot().findComponent("form:datosDeclarantes:declarantesTarifa");
            declarantesTarifa.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosDeclarantes");
            bandera = 1;

        } else if (bandera == 1) {
            cerrarFiltrado();
        }
    }

    public void salir() {
        if (bandera == 1) {
            cerrarFiltrado();
        }
        listaDeclarantesBorrar.clear();
        listaDeclarantesCrear.clear();
        listaDeclarantesModificar.clear();
        declaranteSeleccionado = null;
        k = 0;
        listaDeclarantes = null;
        guardado = true;
    }

    private void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        declarantesFechaInicial = (Column) c.getViewRoot().findComponent("form:datosDeclarantes:declarantesFechaInicial");
        declarantesFechaInicial.setFilterStyle("display: none; visibility: hidden;");
        declarantesFechaFinal = (Column) c.getViewRoot().findComponent("form:datosDeclarantes:declarantesFechaFinal");
        declarantesFechaFinal.setFilterStyle("display: none; visibility: hidden;");
        declarantesBooleano = (Column) c.getViewRoot().findComponent("form:datosDeclarantes:declarantesBooleano");
        declarantesBooleano.setFilterStyle("display: none; visibility: hidden;");
        declarantesPromedio = (Column) c.getViewRoot().findComponent("form:datosDeclarantes:declarantesPromedio");
        declarantesPromedio.setFilterStyle("display: none; visibility: hidden;");
        declarantesTarifa = (Column) c.getViewRoot().findComponent("form:datosDeclarantes:declarantesTarifa");
        declarantesTarifa.setFilterStyle("display: none; visibility: hidden;");
        altoScrollDeclarantes = "270";
        RequestContext.getCurrentInstance().update("form:datosDeclarantes");
        bandera = 0;
        filtradoListaDeclarantes = null;
        tipoLista = 0;
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
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDeclarantesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "DeclarantesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDeclarantesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "DeclarantesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void verificarRastro() {

        RequestContext context = RequestContext.getCurrentInstance();
        if (declaranteSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(declaranteSeleccionado.getSecuencia(), "DECLARANTES");
            System.out.println("resultado: " + resultado);
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
        } else if (administrarRastros.verificarHistoricosTabla("DECLARANTES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    //GUARDAR
    public void guardarCambiosDeclarantes() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones Declarantes");

            if (!listaDeclarantesBorrar.isEmpty()) {
                for (int i = 0; i < listaDeclarantesBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaDeclarantesBorrar.size());
                    if (listaDeclarantesBorrar.get(i).getRetenciondeseada() == null) {
                        listaDeclarantesBorrar.get(i).setRetenciondeseada(null);
                    }

                    if (listaDeclarantesBorrar.get(i).getRetencionminima().getSecuencia() == null) {
                        listaDeclarantesBorrar.get(i).setRetencionminima(null);
                    }

                    administrarDeclarantes.borrarDeclarantes(listaDeclarantesBorrar.get(i));
                }
                System.out.println("Entra");
                listaDeclarantesBorrar.clear();
            }

            if (!listaDeclarantesCrear.isEmpty()) {
                for (int i = 0; i < listaDeclarantesCrear.size(); i++) {
                    System.out.println("Creando...");
                    if (listaDeclarantesCrear.get(i).getRetenciondeseada() == null) {
                        listaDeclarantesCrear.get(i).setRetenciondeseada(null);
                    }

                    if (listaDeclarantesCrear.get(i).getRetencionminima().getSecuencia() == null) {
                        listaDeclarantesCrear.get(i).setRetencionminima(null);
                    }
                    administrarDeclarantes.crearDeclarantes(listaDeclarantesCrear.get(i));
                }
                listaDeclarantesCrear.clear();
            }
            if (!listaDeclarantesModificar.isEmpty()) {
                administrarDeclarantes.modificarDeclarantes(listaDeclarantesModificar);
                listaDeclarantesModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaDeclarantes = null;
            getListaDeclarantes();
            contarRegistros();
            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosDeclarantes");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            //  k = 0;
        }
        declaranteSeleccionado = null;
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void contarRegistrosLov() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLov");
    }

    //GETTERS AND SETTERS
    public List<Declarantes> getListaDeclarantes() {
        if (listaDeclarantes == null && persona != null) {
            listaDeclarantes = administrarDeclarantes.declarantesPersona(persona.getSecuencia());
            if (!listaDeclarantes.isEmpty()) {
                declaranteSeleccionado = listaDeclarantes.get(0);
            }
        }
        return listaDeclarantes;
    }

    public void setListaDeclarantes(List<Declarantes> listaDeclarantes) {
        this.listaDeclarantes = listaDeclarantes;
    }

    public Personas getPersona() {
        return persona;
    }

    public List<Declarantes> getFiltradoListaDeclarantes() {
        return filtradoListaDeclarantes;
    }

    public void setFiltradoListaDeclarantes(List<Declarantes> filtradoListaDeclarantes) {
        this.filtradoListaDeclarantes = filtradoListaDeclarantes;
    }

    public Declarantes getNuevoDeclarante() {
        return nuevoDeclarante;
    }

    public void setNuevoDeclarante(Declarantes nuevoDeclarante) {
        this.nuevoDeclarante = nuevoDeclarante;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public Declarantes getEditarDeclarantes() {
        return editarDeclarantes;
    }

    public void setEditarDeclarantes(Declarantes editarDeclarante) {
        this.editarDeclarantes = editarDeclarante;
    }

    public Declarantes getDuplicarDeclarante() {
        return duplicarDeclarante;
    }

    public void setDuplicarDeclarante(Declarantes duplicarDeclarante) {
        this.duplicarDeclarante = duplicarDeclarante;
    }

    public List<TarifaDeseo> getLovlistaRetenciones() {
        if (lovlistaRetenciones == null) {
            lovlistaRetenciones = administrarDeclarantes.retencionesMinimas();
        }
        return lovlistaRetenciones;
    }

    public void setLovlistaRetenciones(List<TarifaDeseo> lovlistaRetenciones) {
        this.lovlistaRetenciones = lovlistaRetenciones;
    }

    public List<TarifaDeseo> getLovfiltradoslistaRetenciones() {
        return lovfiltradoslistaRetenciones;
    }

    public void setLovfiltradoslistaRetenciones(List<TarifaDeseo> lovfiltradoslistaRetenciones) {
        this.lovfiltradoslistaRetenciones = lovfiltradoslistaRetenciones;
    }

    public TarifaDeseo getRetencionesSeleccionado() {
        return retencionesSeleccionado;
    }

    public void setRetencionesSeleccionado(TarifaDeseo retencionesSeleccionado) {
        this.retencionesSeleccionado = retencionesSeleccionado;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getAltoScrollDeclarantes() {
        return altoScrollDeclarantes;
    }

    public void setAltoScrollDeclarantes(String altoScrollDeclarantes) {
        this.altoScrollDeclarantes = altoScrollDeclarantes;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public Declarantes getDeclaranteSeleccionado() {
        return declaranteSeleccionado;
    }

    public void setDeclaranteSeleccionado(Declarantes declaranteSeleccionado) {
        this.declaranteSeleccionado = declaranteSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDeclarantes");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroLov() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVMinimas");
        infoRegistroLov = String.valueOf(tabla.getRowCount());
        return infoRegistroLov;
    }

    public void setInfoRegistroLov(String infoRegistroLov) {
        this.infoRegistroLov = infoRegistroLov;
    }

}
