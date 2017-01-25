package Controlador;

import Entidades.Empleados;
import Entidades.Personas;
import Entidades.Sets;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSetsInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
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
 * @author AndresPineda
 */
@ManagedBean
@SessionScoped
public class ControlSets implements Serializable {

    @EJB
    AdministrarSetsInterface administrarSets;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Vigencias Cargos
    private List<Sets> listSets;
    private List<Sets> filtrarSets;
    private Sets setSeleccionado;
    private Empleados empleado;
    private Personas per;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Columnas Tabla VC
    private Column setsFechaInicial, setsFechaFinal, setsPromedio, setsTipo, setsPorcentaje, setsTotalIngresos;
    public String altoTabla;
    //Otros
    private boolean aceptar;
    //modificar
    private List<Sets> listSetsModificar;
    private boolean guardado, guardarOk;
    //crear VC
    public Sets nuevoSet;
    private List<Sets> listSetsCrear;
    private BigInteger l;
    private int k;
    //borrar VC
    private List<Sets> listSetsBorrar;
    //editar celda
    private Sets editarSets;
    private int cualCelda, tipoLista;
    private boolean cambioEditor, aceptarEditar;
    //duplicar
    private Sets duplicarSet;
    private Date fechaParametro;
    private Date fechaIni, fechaFin;
    private String auxTipoSet;
    private BigDecimal auxPromedio;
    public String infoRegistro;
    private int tipoActualizacion;
    private String mensajeValidacion;
    private DataTable tablaC;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    /**
     * Constructor de ControlSet
     */
    public ControlSets() {

        listSets = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listSetsBorrar = new ArrayList<Sets>();
        //crear aficiones
        listSetsCrear = new ArrayList<Sets>();
        k = 0;
        //modificar aficiones
        listSetsModificar = new ArrayList<Sets>();
        //editar
        editarSets = new Sets();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevoSet = new Sets();
        duplicarSet = new Sets();
        setSeleccionado = null;
        altoTabla = "287";
        tipoActualizacion = 0;
        mensajeValidacion = " ";
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSets.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
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
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina();
        } else {
            String pagActual = "emplsets";
            //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParametros.put("paginaAnterior", pagActual);
            //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
            controlListaNavegacion.adicionarPagina(pagActual);
        }
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

    public void recibirEmpleado(Empleados empl) {
        empleado = empl;
        listSets = null;
        per = empleado.getPersona();
        getSetsEmpleado();
        if (listSets != null && !listSets.isEmpty()) {
            if (listSets.size() >= 1) {
                setSeleccionado = listSets.get(0);
            }
        }
    }

    public boolean validarDatosRegistro(int i) {
        boolean retorno = true;
        if (i == 0) {
            Sets auxiliar = new Sets();
            auxiliar = setSeleccionado;
            int tam = 0;
            if (auxiliar.getTiposet() == null || auxiliar.getTiposet().isEmpty()) {
                retorno = false;
            } else {
                tam = auxiliar.getTiposet().length();
                if (tam == 0 || tam >= 2) {
                    retorno = false;
                }
            }
            if (tam == 1) {
                String tipo = auxiliar.getTiposet();
                System.out.println("Tipo : " + tipo);
                if (tipo.equalsIgnoreCase("1") || tipo.equalsIgnoreCase("2")) {
                    System.out.println("OK");
                } else {
                    retorno = false;
                    System.out.println("Entr :S");
                }
            }
            if (auxiliar.getPromedio() != null) {
                int signo = auxiliar.getPromedio().signum();
                if (signo <= 0) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        if (i == 1) {
            int tam = 0;
            if (nuevoSet.getTiposet() == null || nuevoSet.getTiposet().isEmpty()) {
                retorno = false;
            } else {
                tam = nuevoSet.getTiposet().length();
                if (tam == 0 || tam >= 2) {
                    retorno = false;
                }
            }
            if (tam == 1) {
                String tipo = nuevoSet.getTiposet();
                System.out.println("Tipo : " + tipo);
                if (tipo.equalsIgnoreCase("1") || tipo.equalsIgnoreCase("2")) {
                    System.out.println("OK");
                } else {
                    retorno = false;
                    System.out.println("Entr :S");
                }
            }
            if (nuevoSet.getPromedio() != null) {
                int signo = nuevoSet.getPromedio().signum();
                if (signo <= 0) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        if (i == 2) {
            int tam = 0;
            if (duplicarSet.getTiposet() == null || duplicarSet.getTiposet().isEmpty()) {
                retorno = false;
            } else {
                tam = duplicarSet.getTiposet().length();
                if (tam == 0 || tam >= 2) {
                    System.out.println("Duplicar error");
                    retorno = false;
                }
            }
            if (tam == 1) {
                String tipo = duplicarSet.getTiposet();
                System.out.println("Tipo : " + tipo);
                if (tipo.equalsIgnoreCase("1") || tipo.equalsIgnoreCase("2")) {
                    System.out.println("OK");
                } else {
                    retorno = false;
                    System.out.println("Entr :S");
                }
            }
            if (duplicarSet.getPromedio() != null) {
                System.out.println("Diferente");
                int signo = duplicarSet.getPromedio().signum();
                System.out.println("Signo = " + signo);
                if (signo <= 0) {
                    System.out.println("Signp fail");
                    retorno = false;
                }
            } else {
                System.out.println("Error fuck ");
                retorno = false;
            }
        }
        return retorno;
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (setSeleccionado == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else if (setSeleccionado != null) {
            if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("");
                RequestContext.getCurrentInstance().execute("PF('");
                tipoActualizacion = 0;
            }
        }
    }

    public void modificarSets(Sets sets) {
        if (validarDatosRegistro(0) == false) {
            setSeleccionado.setPromedio(auxPromedio);
            setSeleccionado.setTiposet(auxTipoSet);
        }
        if (!listSetsCrear.contains(setSeleccionado)) {
            if (listSetsModificar.isEmpty()) {
                listSetsModificar.add(setSeleccionado);
            } else if (!listSetsModificar.contains(setSeleccionado)) {
                listSetsModificar.add(setSeleccionado);
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
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
            Sets auxiliar = null;
            auxiliar = setSeleccionado;
            if (auxiliar.getFechafinal() != null) {
                if (auxiliar.getFechainicial().after(fechaParametro) && auxiliar.getFechainicial().before(auxiliar.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (auxiliar.getFechafinal() == null) {
                if (auxiliar.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (i == 1) {
            if (nuevoSet.getFechafinal() != null) {
                if (nuevoSet.getFechainicial().after(fechaParametro) && nuevoSet.getFechainicial().before(nuevoSet.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (nuevoSet.getFechafinal() == null) {
                if (nuevoSet.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (i == 2) {
            if (duplicarSet.getFechafinal() != null) {
                if (duplicarSet.getFechainicial().after(fechaParametro) && duplicarSet.getFechainicial().before(duplicarSet.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (duplicarSet.getFechafinal() == null) {
                if (duplicarSet.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public void modificarFechas(Sets sets, int c) {
        Sets auxiliar = null;
        auxiliar = setSeleccionado;
        if (auxiliar.getFechainicial() != null) {
            boolean retorno = false;
            setSeleccionado = sets;
            retorno = validarFechasRegistro(0);
            if (retorno == true) {
                cambiarIndice(sets, c);
                modificarSets(sets);
            } else {
                setSeleccionado.setFechafinal(fechaFin);
                setSeleccionado.setFechainicial(fechaIni);
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosSetsEmpleado");
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
        } else {
            setSeleccionado.setFechainicial(fechaIni);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosSetsEmpleado");
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    //Ubicacion Celda.
    /**
     * Metodo que obtiene la posicion dentro de la tabla Sets
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    public void cambiarIndice(Sets sets, int celda) {
        setSeleccionado = sets;
        cualCelda = celda;
        auxPromedio = setSeleccionado.getPromedio();
        auxTipoSet = setSeleccionado.getTiposet();
        fechaFin = setSeleccionado.getFechafinal();
        fechaIni = setSeleccionado.getFechainicial();
    }
    //GUARDAR

    /**
     * Metodo que guarda los cambios efectuados en la pagina Sets
     */
    public void guardarCambiosSets() {
        if (guardado == false) {
            if (!listSetsBorrar.isEmpty()) {
                for (int i = 0; i < listSetsBorrar.size(); i++) {
                    administrarSets.borrarSets(listSetsBorrar.get(i));
                }
                listSetsBorrar.clear();
            }
            if (!listSetsCrear.isEmpty()) {
                for (int i = 0; i < listSetsCrear.size(); i++) {
                    administrarSets.crearSets(listSetsCrear.get(i));
                }
                listSetsCrear.clear();
            }
            if (!listSetsModificar.isEmpty()) {
                administrarSets.modificarSets(listSetsModificar);
                listSetsModificar.clear();
            }
            listSets = null;
            getSetsEmpleado();
            if (listSets != null && !listSets.isEmpty()) {
                setSeleccionado = listSets.get(0);
            }

            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosSetsEmpleado");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }
    //CANCELAR MODIFICACIONES

    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacion() {
        if (bandera == 1) {
            cerrarFiltrado();
        }
        listSetsBorrar.clear();
        listSetsCrear.clear();
        listSetsModificar.clear();
        setSeleccionado = null;
        k = 0;
        listSets = null;
        getSetsEmpleado();
        guardado = true;
    }

    //MOSTRAR DATOS CELDA
    /**
     * Metodo que muestra los dialogos de editar con respecto a la lista real o
     * la lista filtrada y a la columna
     */
    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (setSeleccionado == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else if (setSeleccionado != null) {
            editarSets = setSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicial");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicial').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinal");
                RequestContext.getCurrentInstance().execute("PF('editarFechaFinal').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPorcentaje");
                RequestContext.getCurrentInstance().execute("PF('editarPorcentaje').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPromedio");
                RequestContext.getCurrentInstance().execute("PF('editarPromedio').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoSet");
                RequestContext.getCurrentInstance().execute("PF('editarTipoSet').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTotalIngresos");
                RequestContext.getCurrentInstance().execute("PF('editarTotalIngresos').show()");
                cualCelda = -1;
            }
        }
    }
    //CREAR VU

    /**
     * Metodo que se encarga de agregar un nuevo Set
     */
    public void agregarNuevoSet() {
        int fecha = 0;
        int pasa = 0;
        int contador = 0;
        mensajeValidacion = "";
        nuevoSet.setEmpleado(empleado);
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoSet.getFechainicial() == null || nuevoSet.getFechainicial().equals("")) {
            mensajeValidacion = " *Fecha\n";
        } else {
            if (listSets != null) {
                for (int i = 0; i < listSets.size(); i++) {
                    if (nuevoSet.getFechainicial().equals(listSets.get(i).getFechainicial())) {
                        fecha++;
                    }
                }
            }
            if (fecha > 0) {
                RequestContext.getCurrentInstance().update("form:validacionFechas");
                RequestContext.getCurrentInstance().execute("PF('validacionFechas').show()");
                pasa++;
            } else {
                contador++;
            }
        }
        boolean resp = validarDatosRegistro(1);
        if (contador == 1 && pasa == 0) {
            if (nuevoSet.getFechainicial() != null && resp == true) {
                if (validarFechasRegistro(1) == true) {
                    if (bandera == 1) {
                        cerrarFiltrado();
                    }
                    k++;
                    l = BigInteger.valueOf(k);
                    nuevoSet.setSecuencia(l);
                    nuevoSet.setEmpleado(empleado);
                    listSetsCrear.add(nuevoSet);
                    listSets.add(nuevoSet);
                    setSeleccionado = listSets.get(listSets.indexOf(nuevoSet));
                    contarRegistros();
                    nuevoSet = new Sets();
                    RequestContext.getCurrentInstance().update("form:datosSetsEmpleado");
                    RequestContext.getCurrentInstance().execute("PF('NuevoRegistroSet').hide()");
                    if (guardado) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
            }
        }
    }

    private void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTabla = "287";
        //CERRAR FILTRADO
        setsFechaInicial = (Column) c.getViewRoot().findComponent("form:datosSetsEmpleado:setsFechaInicial");
        setsFechaInicial.setFilterStyle("display: none; visibility: hidden;");
        setsFechaFinal = (Column) c.getViewRoot().findComponent("form:datosSetsEmpleado:setsFechaFinal");
        setsFechaFinal.setFilterStyle("display: none; visibility: hidden;");
        setsPorcentaje = (Column) c.getViewRoot().findComponent("form:datosSetsEmpleado:setsPorcentaje");
        setsPorcentaje.setFilterStyle("display: none; visibility: hidden;");
        setsPromedio = (Column) c.getViewRoot().findComponent("form:datosSetsEmpleado:setsPromedio");
        setsPromedio.setFilterStyle("display: none; visibility: hidden;");
        setsTipo = (Column) c.getViewRoot().findComponent("form:datosSetsEmpleado:setsTipo");
        setsTipo.setFilterStyle("display: none; visibility: hidden;");
        setsTotalIngresos = (Column) c.getViewRoot().findComponent("form:datosSetsEmpleado:setsTotalIngresos");
        setsTotalIngresos.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:datosSetsEmpleado");
        bandera = 0;
        filtrarSets = null;
        tipoLista = 0;
    }

    //LIMPIAR NUEVO REGISTRO
    /**
     * Metodo que limpia las casillas del nuevo Set
     */
    public void limpiarNuevaSets() {
        nuevoSet = new Sets();
    }

    //DUPLICAR VC
    /**
     * Metodo que duplica un Set especifico dado por la posicion de la fila
     */
    public void duplicarSets() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (setSeleccionado == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else if (setSeleccionado != null) {
            duplicarSet = new Sets();
            duplicarSet.setEmpleado(setSeleccionado.getEmpleado());
            duplicarSet.setFechainicial(setSeleccionado.getFechainicial());
            duplicarSet.setFechafinal(setSeleccionado.getFechafinal());
            duplicarSet.setPorcentaje(setSeleccionado.getPorcentaje());
            duplicarSet.setPromedio(setSeleccionado.getPromedio());
            duplicarSet.setTiposet(setSeleccionado.getTiposet());
            duplicarSet.setTotalingresos(setSeleccionado.getTotalingresos());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSet");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroSet').show()");
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
     */
    public void confirmarDuplicar() {
        RequestContext context = RequestContext.getCurrentInstance();
        int contador = 0;
        mensajeValidacion = " ";
        for (int j = 0; j < listSets.size(); j++) {
            if (duplicarSet.getFechainicial().equals(listSets.get(j).getFechainicial())) {
                contador++;
            }
        }
        if (contador > 0) {
            mensajeValidacion = "Fechas NO Repetidas";
            RequestContext.getCurrentInstance().update("form:validacionFechas");
            RequestContext.getCurrentInstance().execute("PF('validacionFechas').show()");
        } else {
            boolean resp = validarDatosRegistro(2);
            if (duplicarSet.getFechainicial() != null && resp == true) {
                if (validarFechasRegistro(2) == true) {
                    k++;
                    l = BigInteger.valueOf(k);
                    duplicarSet.setSecuencia(l);
                    listSets.add(duplicarSet);
                    listSetsCrear.add(duplicarSet);
                    setSeleccionado = listSets.get(listSets.indexOf(duplicarSet));
                    contarRegistros();
                    RequestContext.getCurrentInstance().update("form:datosSetsEmpleado");
                    RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroSet').hide()");
                    if (guardado) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                    if (bandera == 1) {
                        cerrarFiltrado();
                    }
                    duplicarSet = new Sets();
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
            }
        }
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar Set
     */
    public void limpiarDuplicarSets() {
        duplicarSet = new Sets();
    }

    //BORRAR VC
    /**
     * Metodo que borra los Sets seleccionados
     */
    public void borrarSets() {
        if (setSeleccionado == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else if (setSeleccionado != null) {
            if (!listSetsModificar.isEmpty() && listSetsModificar.contains(setSeleccionado)) {
                int modIndex = listSetsModificar.indexOf(setSeleccionado);
                listSetsModificar.remove(modIndex);
                listSetsBorrar.add(setSeleccionado);
            } else if (!listSetsCrear.isEmpty() && listSetsCrear.contains(setSeleccionado)) {
                int crearIndex = listSetsCrear.indexOf(setSeleccionado);
                listSetsCrear.remove(crearIndex);
            } else {
                listSetsBorrar.add(setSeleccionado);
            }
            listSets.remove(setSeleccionado);
            if (tipoLista == 1) {
                filtrarSets.remove(setSeleccionado);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosSetsEmpleado");
            setSeleccionado = null;
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }
    //CTRL + F11 ACTIVAR/DESACTIVAR

    /**
     * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            altoTabla = "267";
            setsFechaInicial = (Column) c.getViewRoot().findComponent("form:datosSetsEmpleado:setsFechaInicial");
            setsFechaInicial.setFilterStyle("width: 85% !important;");
            setsFechaFinal = (Column) c.getViewRoot().findComponent("form:datosSetsEmpleado:setsFechaFinal");
            setsFechaFinal.setFilterStyle("width: 85% !important;");
            setsPorcentaje = (Column) c.getViewRoot().findComponent("form:datosSetsEmpleado:setsPorcentaje");
            setsPorcentaje.setFilterStyle("width: 85% !important;");
            setsPromedio = (Column) c.getViewRoot().findComponent("form:datosSetsEmpleado:setsPromedio");
            setsPromedio.setFilterStyle("width: 85% !important;");
            setsTipo = (Column) c.getViewRoot().findComponent("form:datosSetsEmpleado:setsTipo");
            setsTipo.setFilterStyle("width: 85% !important;");
            setsTotalIngresos = (Column) c.getViewRoot().findComponent("form:datosSetsEmpleado:setsTotalIngresos");
            setsTotalIngresos.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosSetsEmpleado");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (bandera == 1) {
            cerrarFiltrado();
        }
        listSetsBorrar.clear();
        listSetsCrear.clear();
        listSetsModificar.clear();
        setSeleccionado = null;
        k = 0;
        listSets = null;
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
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSetsEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SetsPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSetsEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SetsXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }
    //EVENTO FILTRAR

    /**
     * Evento que cambia la lista reala a la filtrada
     */
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        setSeleccionado = null;
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }
    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (setSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(setSeleccionado.getSecuencia(), "SETS");
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
        } else if (administrarRastros.verificarHistoricosTabla("SETS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void recordarSeleccion() {
        if (setSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosSetsEmpleado");
            tablaC.setSelection(setSeleccionado);
        } else {
            setSeleccionado = null;
        }
        System.out.println("vigenciaSeleccionada: " + setSeleccionado);
    }

    //GETTERS AND SETTERS
    /**
     * Metodo que obtiene la lista de Sets de un Empleado, en caso de que sea
     * null hace el llamado al metodo de obtener Sets del empleado, en caso
     * contrario no genera operaciones
     *
     * @return listS Lista de Sets de un Empleado
     */
    public List<Sets> getSetsEmpleado() {
        if (listSets == null && empleado != null) {
            listSets = administrarSets.SetsEmpleado(empleado.getSecuencia());
        }
        return listSets;

    }

    public void setSetsEmpleado(List<Sets> sets) {
        this.listSets = sets;
    }

    /**
     * Get del empleado, en caso de existir lo retorna en caso contrario lo
     * obtiene y retorna
     *
     * @return empleado Empleado que esta usado en el momento
     */
    public Empleados getEmpleado() {
        return empleado;
    }

    public List<Sets> getFiltrarSets() {
        return filtrarSets;
    }

    public void setFiltrarSets(List<Sets> filtrarSet) {
        this.filtrarSets = filtrarSet;
    }

    public Sets getNuevoSet() {
        return nuevoSet;
    }

    public void setNuevoSet(Sets nuevoSet) {
        this.nuevoSet = nuevoSet;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public Sets getEditarSets() {
        return editarSets;
    }

    public void setEditarSets(Sets editarSet) {
        this.editarSets = editarSet;
    }

    public Sets getDuplicarSet() {
        return duplicarSet;
    }

    public void setDuplicarSet(Sets duplicarSet) {
        this.duplicarSet = duplicarSet;
    }

    public Personas getPer() {
        return per;
    }

    public void setPer(Personas per) {
        this.per = per;
    }

    public Sets getSetSeleccionado() {
        return setSeleccionado;
    }

    public void setSetSeleccionado(Sets setSeleccionado) {
        this.setSeleccionado = setSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosSetsEmpleado");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }
}
