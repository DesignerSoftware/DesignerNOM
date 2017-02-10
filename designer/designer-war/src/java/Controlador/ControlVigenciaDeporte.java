package Controlador;

import Entidades.Deportes;
import Entidades.Empleados;
import Entidades.VigenciasDeportes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciaDeporteInterface;
import java.io.IOException;
import java.io.Serializable;
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

@ManagedBean
@SessionScoped
public class ControlVigenciaDeporte implements Serializable {

    @EJB
    AdministrarVigenciaDeporteInterface administrarVigenciaDeporte;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<VigenciasDeportes> listVigenciasDeportes;
    private List<VigenciasDeportes> filtrarListVigenciasDeportes;
    private VigenciasDeportes vigenciaTablaSeleccionada;
    private List<Deportes> listDeportes;
    private Deportes deporteSeleccionado;
    private List<Deportes> filtrarListDeportes;
    private int tipoActualizacion;
    private int bandera;
    private Column veFechaInicial, veFechaFinal, veDescripcion, veIndividual, veCIndividual, veGrupal, veCGrupal;
    private boolean aceptar;
    private List<VigenciasDeportes> listVigenciaDeporteModificar;
    private boolean guardado;
    public VigenciasDeportes nuevaVigenciaDeporte;
    private List<VigenciasDeportes> listVigenciaDeporteCrear;
    private BigInteger l;
    private int k;
    private List<VigenciasDeportes> listVigenciaDeporteBorrar;
    private VigenciasDeportes editarVigenciaDeporte;
    private int cualCelda, tipoLista;
    private VigenciasDeportes duplicarVigenciaDeporte;
    private String deporte;
    private boolean permitirIndex;
    private BigInteger backUpSecRegistro;
    private Empleados empleado;
    private Date fechaParametro;
    private Date fechaIni, fechaFin;
    private String altoTabla;
    private String infoRegistro;
    private String infoRegistroDeporte;
    private DataTable tablaC;
    private boolean activarLOV;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlVigenciaDeporte() {
        altoTabla = "305";
        listVigenciasDeportes = null;
        listDeportes = null;
        aceptar = true;
        listVigenciaDeporteBorrar = new ArrayList<VigenciasDeportes>();
        listVigenciaDeporteCrear = new ArrayList<VigenciasDeportes>();
        filtrarListDeportes = null;
        k = 0;
        listVigenciaDeporteModificar = new ArrayList<VigenciasDeportes>();
        editarVigenciaDeporte = new VigenciasDeportes();
        cualCelda = -1;
        tipoLista = 0;
        guardado = true;
        nuevaVigenciaDeporte = new VigenciasDeportes();
        nuevaVigenciaDeporte.setDeporte(new Deportes());
        nuevaVigenciaDeporte.setFechainicial(new Date());
        permitirIndex = true;
        backUpSecRegistro = null;
        empleado = new Empleados();
        activarLOV = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciaDeporte.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            vigenciaTablaSeleccionada = listVigenciasDeportes.get(0);
            System.out.println("El nombre del empleado es" + empleado.getPersona().getNombreCompleto());
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
            String pagActual = "vigenciasdeportes";
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

    public void recibirEmpleado(BigInteger secuencia) {
        listVigenciasDeportes = null;
        listDeportes = null;
        empleado = administrarVigenciaDeporte.empleadoActual(secuencia);
        getListVigenciasDeportes();
        deshabilitarBotonLov();
        if (listVigenciasDeportes != null) {
            if (!listVigenciasDeportes.isEmpty()) {
                vigenciaTablaSeleccionada = listVigenciasDeportes.get(0);
            }
        }
    }

    public void modificarVigenciaDeporte(VigenciasDeportes vigenciaDeportes) {
        vigenciaTablaSeleccionada = vigenciaDeportes;
        if (tipoLista == 0) {
            if (!listVigenciaDeporteCrear.contains(vigenciaTablaSeleccionada)) {
                if (listVigenciaDeporteModificar.isEmpty()) {
                    listVigenciaDeporteModificar.add(vigenciaTablaSeleccionada);
                } else if (!listVigenciaDeporteModificar.contains(vigenciaTablaSeleccionada)) {
                    listVigenciaDeporteModificar.add(vigenciaTablaSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    deshabilitarBotonLov();
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else if (!listVigenciaDeporteCrear.contains(vigenciaTablaSeleccionada)) {
            if (listVigenciaDeporteModificar.isEmpty()) {
                listVigenciaDeporteModificar.add(vigenciaTablaSeleccionada);
            } else if (!listVigenciaDeporteModificar.contains(vigenciaTablaSeleccionada)) {
                listVigenciaDeporteModificar.add(vigenciaTablaSeleccionada);
            }
            if (guardado == true) {
                guardado = false;
                deshabilitarBotonLov();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void modificarVigenciaDeporte(VigenciasDeportes vigenciaDeportes, String confirmarCambio, String valorConfirmar) {
        vigenciaTablaSeleccionada = vigenciaDeportes;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        if (confirmarCambio.equalsIgnoreCase("DEPORTES")) {
            if (tipoLista == 0) {
                vigenciaTablaSeleccionada.getDeporte().setNombre(deporte);
            } else {
                vigenciaTablaSeleccionada.getDeporte().setNombre(deporte);
            }
            for (int i = 0; i < listDeportes.size(); i++) {
                if (listDeportes.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaTablaSeleccionada.setDeporte(listDeportes.get(indiceUnicoElemento));
                } else {
                    vigenciaTablaSeleccionada.setDeporte(listDeportes.get(indiceUnicoElemento));
                }
                listDeportes.clear();
                getListDeportes();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("form:DeportesDialogo");
                RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').show()");
                tipoActualizacion = 0;
            }
            deshabilitarBotonLov();
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listVigenciaDeporteCrear.contains(vigenciaTablaSeleccionada)) {

                    if (listVigenciaDeporteModificar.isEmpty()) {
                        listVigenciaDeporteModificar.add(vigenciaTablaSeleccionada);
                    } else if (!listVigenciaDeporteModificar.contains(vigenciaTablaSeleccionada)) {
                        listVigenciaDeporteModificar.add(vigenciaTablaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                deshabilitarBotonLov();
                vigenciaTablaSeleccionada = null;
            } else {
                if (!listVigenciaDeporteCrear.contains(vigenciaTablaSeleccionada)) {

                    if (listVigenciaDeporteModificar.isEmpty()) {
                        listVigenciaDeporteModificar.add(vigenciaTablaSeleccionada);
                    } else if (!listVigenciaDeporteModificar.contains(vigenciaTablaSeleccionada)) {
                        listVigenciaDeporteModificar.add(vigenciaTablaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                deshabilitarBotonLov();
            }
        }
        RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("DEPORTES")) {
            if (tipoNuevo == 1) {
                deporte = nuevaVigenciaDeporte.getDeporte().getNombre();
            } else if (tipoNuevo == 2) {
                deporte = duplicarVigenciaDeporte.getDeporte().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("DEPORTES")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaDeporte.getDeporte().setNombre(deporte);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaDeporte.getDeporte().setNombre(deporte);
            }
            for (int i = 0; i < listDeportes.size(); i++) {
                if (listDeportes.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaDeporte.setDeporte(listDeportes.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigencias");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaDeporte.setDeporte(listDeportes.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigencias");
                }
                listDeportes.clear();
                getListDeportes();
            } else {
                RequestContext.getCurrentInstance().update("form:DeportesDialogo");
                RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigencias");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigencias");
                }
            }
        }
    }

    public void cambiarIndice(VigenciasDeportes vigenciaDeportes, int celda) {
        if (permitirIndex == true) {
            vigenciaTablaSeleccionada = vigenciaDeportes;
            cualCelda = celda;
            if (tipoLista == 0) {
                fechaFin = vigenciaTablaSeleccionada.getFechafinal();
                fechaIni = vigenciaTablaSeleccionada.getFechainicial();
                vigenciaTablaSeleccionada.getSecuencia();
                deshabilitarBotonLov();
                if (cualCelda == 2) {
                    contarRegistrosVD();
                    deporte = vigenciaTablaSeleccionada.getDeporte().getNombre();
                    habilitarBotonLov();
                }
            }
            if (tipoLista == 1) {
                fechaFin = vigenciaTablaSeleccionada.getFechafinal();
                fechaIni = vigenciaTablaSeleccionada.getFechainicial();
                vigenciaTablaSeleccionada.getSecuencia();
                deshabilitarBotonLov();
                if (cualCelda == 2) {
                    contarRegistrosVD();
                    deporte = vigenciaTablaSeleccionada.getDeporte().getNombre();
                    habilitarBotonLov();
                }
            }
        }
    }

    public void guardarSalir() {
        guardarCambios();
        salir();
    }

    public void cancelarSalir() {
        cancelarModificacion();
        salir();
    }

    public void guardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listVigenciaDeporteBorrar.isEmpty()) {
                    administrarVigenciaDeporte.borrarVigenciasDeportes(listVigenciaDeporteBorrar);
                    listVigenciaDeporteBorrar.clear();
                }
                if (!listVigenciaDeporteCrear.isEmpty()) {
                    administrarVigenciaDeporte.crearVigenciasDeportes(listVigenciaDeporteCrear);
                    listVigenciaDeporteCrear.clear();
                }
                if (!listVigenciaDeporteModificar.isEmpty()) {
                    administrarVigenciaDeporte.editarVigenciasDeportes(listVigenciaDeporteModificar);
                    listVigenciaDeporteModificar.clear();
                }
                listVigenciasDeportes = null;
                getListVigenciasDeportes();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistrosVD();
                vigenciaTablaSeleccionada = null;
            }

            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
            deshabilitarBotonLov();
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {

            veFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veFechaInicial");
            veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            veFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veFechaFinal");
            veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            veDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veDescripcion");
            veDescripcion.setFilterStyle("display: none; visibility: hidden;");
            veIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veIndividual");
            veIndividual.setFilterStyle("display: none; visibility: hidden;");
            veCIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veCIndividual");
            veCIndividual.setFilterStyle("display: none; visibility: hidden;");
            veGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veGrupal");
            veGrupal.setFilterStyle("display: none; visibility: hidden;");
            veCGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veCGrupal");
            veCGrupal.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
            bandera = 0;
            filtrarListVigenciasDeportes = null;
            tipoLista = 0;
            altoTabla = "305";
        }

        listVigenciaDeporteBorrar.clear();
        listVigenciaDeporteCrear.clear();
        listVigenciaDeporteModificar.clear();
        k = 0;
        listVigenciasDeportes = null;
        vigenciaTablaSeleccionada = null;
        guardado = true;
        permitirIndex = true;
        getListVigenciasDeportes();
        contarRegistrosVD();
        deshabilitarBotonLov();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:infoRegistro");
        RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void editarCelda() {

        if (vigenciaTablaSeleccionada != null) {
            if (tipoLista == 0) {
                editarVigenciaDeporte = vigenciaTablaSeleccionada;
            }
            if (tipoLista == 1) {
                editarVigenciaDeporte = vigenciaTablaSeleccionada;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicialD').show()");
                deshabilitarBotonLov();
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaFinalD').show()");
                deshabilitarBotonLov();
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionD");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionD').show()");
                habilitarBotonLov();
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarIndividualD");
                RequestContext.getCurrentInstance().execute("PF('editarIndividualD').show()");
                deshabilitarBotonLov();
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCIndividualD");
                RequestContext.getCurrentInstance().execute("PF('editarCIndividualD').show()");
                cualCelda = -1;
                deshabilitarBotonLov();
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarGrupalD");
                RequestContext.getCurrentInstance().execute("PF('editarGrupalD').show()");
                cualCelda = -1;
                deshabilitarBotonLov();
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCGrupalD");
                RequestContext.getCurrentInstance().execute("PF('editarCGrupalD').show()");
                cualCelda = -1;
                deshabilitarBotonLov();
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public boolean validarFechasRegistro(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (i == 0) {
            VigenciasDeportes auxiliar = null;
            if (tipoLista == 0) {
                auxiliar = vigenciaTablaSeleccionada;
            }
            if (tipoLista == 1) {
                auxiliar = vigenciaTablaSeleccionada;
            }
            if (auxiliar.getFechafinal() != null) {
                if (auxiliar.getFechainicial().after(fechaParametro) && auxiliar.getFechainicial().before(auxiliar.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
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
            if (nuevaVigenciaDeporte.getFechafinal() != null) {
                if (nuevaVigenciaDeporte.getFechainicial().after(fechaParametro) && nuevaVigenciaDeporte.getFechainicial().before(nuevaVigenciaDeporte.getFechafinal())) {
                    retorno = true;
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                    retorno = false;
                }
            } else if (nuevaVigenciaDeporte.getFechainicial().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarVigenciaDeporte.getFechafinal() != null) {
                if (duplicarVigenciaDeporte.getFechainicial().after(fechaParametro) && duplicarVigenciaDeporte.getFechainicial().before(duplicarVigenciaDeporte.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            } else if (duplicarVigenciaDeporte.getFechainicial().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificarFechas(VigenciasDeportes vigenciaDeportes, int c) {
        VigenciasDeportes auxiliar = null;
        if (tipoLista == 0) {
            auxiliar = vigenciaTablaSeleccionada;
        }
        if (tipoLista == 1) {
            auxiliar = vigenciaTablaSeleccionada;
        }
        if (auxiliar.getFechainicial() != null) {
            boolean retorno = false;
            if (auxiliar.getFechafinal() == null) {
                retorno = true;
            }
            if (auxiliar.getFechafinal() != null) {
                vigenciaTablaSeleccionada = vigenciaDeportes;
                retorno = validarFechasRegistro(0);
            }
            if (retorno == true) {
                cambiarIndice(vigenciaDeportes, c);
                modificarVigenciaDeporte(vigenciaDeportes);
            } else {
                if (tipoLista == 0) {
                    vigenciaTablaSeleccionada.setFechafinal(fechaFin);
                    vigenciaTablaSeleccionada.setFechainicial(fechaIni);
                }
                if (tipoLista == 1) {
                    vigenciaTablaSeleccionada.setFechafinal(fechaFin);
                    vigenciaTablaSeleccionada.setFechainicial(fechaIni);

                }
                deshabilitarBotonLov();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
                RequestContext.getCurrentInstance().execute("PF('form:errorFechas').show()");
            }
        } else {
            if (tipoLista == 0) {
                vigenciaTablaSeleccionada.setFechainicial(fechaIni);
            }
            if (tipoLista == 1) {
                vigenciaTablaSeleccionada.setFechainicial(fechaIni);

            }
            deshabilitarBotonLov();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    public void agregarNuevaVigenciaDeporte() {
        if (nuevaVigenciaDeporte.getFechainicial() != null && nuevaVigenciaDeporte.getDeporte() != null) {
            if (validarFechasRegistro(1) == true) {
                if (bandera == 1) {
                    altoTabla = "305";
                    //CERRAR FILTRADO
                    veFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veFechaInicial");
                    veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    veFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veFechaFinal");
                    veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    veDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veDescripcion");
                    veDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    veIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veIndividual");
                    veIndividual.setFilterStyle("display: none; visibility: hidden;");
                    veCIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veCIndividual");
                    veCIndividual.setFilterStyle("display: none; visibility: hidden;");
                    veGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veGrupal");
                    veGrupal.setFilterStyle("display: none; visibility: hidden;");
                    veCGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veCGrupal");
                    veCGrupal.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
                    bandera = 0;
                    filtrarListVigenciasDeportes = null;
                    tipoLista = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevaVigenciaDeporte.setSecuencia(l);
                nuevaVigenciaDeporte.setPersona(empleado.getPersona());
                listVigenciaDeporteCrear.add(nuevaVigenciaDeporte);
                listVigenciasDeportes.add(nuevaVigenciaDeporte);
                vigenciaTablaSeleccionada = nuevaVigenciaDeporte;
                nuevaVigenciaDeporte = new VigenciasDeportes();
                nuevaVigenciaDeporte.setDeporte(new Deportes());
                getListVigenciasDeportes();
                contarRegistrosVD();
                deshabilitarBotonLov();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:infoRegistro");
                RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigencias').hide()");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    public void limpiarNuevaVigenciaDeporte() {
        nuevaVigenciaDeporte = new VigenciasDeportes();
        nuevaVigenciaDeporte.setDeporte(new Deportes());

    }

    public void duplicarVigenciaDeporteM() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaTablaSeleccionada != null) {
            duplicarVigenciaDeporte = new VigenciasDeportes();

            if (tipoLista == 0) {

                duplicarVigenciaDeporte.setDeporte(vigenciaTablaSeleccionada.getDeporte());
                duplicarVigenciaDeporte.setFechafinal(vigenciaTablaSeleccionada.getFechafinal());
                duplicarVigenciaDeporte.setFechainicial(vigenciaTablaSeleccionada.getFechainicial());
                duplicarVigenciaDeporte.setPersona(vigenciaTablaSeleccionada.getPersona());
                duplicarVigenciaDeporte.setValorcualitativo(vigenciaTablaSeleccionada.getValorcualitativo());
                duplicarVigenciaDeporte.setValorcualitativogrupo(vigenciaTablaSeleccionada.getValorcualitativogrupo());
                duplicarVigenciaDeporte.setValorcuantitativo(vigenciaTablaSeleccionada.getValorcuantitativo());
                duplicarVigenciaDeporte.setValorcuantitativogrupo(vigenciaTablaSeleccionada.getValorcuantitativogrupo());

            }
            if (tipoLista == 1) {

                duplicarVigenciaDeporte.setDeporte(vigenciaTablaSeleccionada.getDeporte());
                duplicarVigenciaDeporte.setFechafinal(vigenciaTablaSeleccionada.getFechafinal());
                duplicarVigenciaDeporte.setFechainicial(vigenciaTablaSeleccionada.getFechainicial());
                duplicarVigenciaDeporte.setPersona(vigenciaTablaSeleccionada.getPersona());
                duplicarVigenciaDeporte.setValorcualitativo(vigenciaTablaSeleccionada.getValorcualitativo());
                duplicarVigenciaDeporte.setValorcualitativogrupo(vigenciaTablaSeleccionada.getValorcualitativogrupo());
                duplicarVigenciaDeporte.setValorcuantitativo(vigenciaTablaSeleccionada.getValorcuantitativo());
                duplicarVigenciaDeporte.setValorcuantitativogrupo(vigenciaTablaSeleccionada.getValorcuantitativogrupo());

            }
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigencias");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigencias').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        if (duplicarVigenciaDeporte.getFechainicial() != null && duplicarVigenciaDeporte.getDeporte() != null) {
            int repetido = 0;
            for (int i = 0; i < listVigenciasDeportes.size(); i++) {
                if (duplicarVigenciaDeporte.getFechainicial().equals(listVigenciasDeportes.get(i).getFechainicial()) && duplicarVigenciaDeporte.getDeporte().equals(listVigenciasDeportes.get(i).getDeporte())) {
                    repetido++;
                }
            }
            if (repetido == 0) {
                if (validarFechasRegistro(2) == true) {
                    k++;
                    l = BigInteger.valueOf(k);
                    duplicarVigenciaDeporte.setSecuencia(l);
                    duplicarVigenciaDeporte.setPersona(empleado.getPersona());
                    listVigenciasDeportes.add(duplicarVigenciaDeporte);
                    listVigenciaDeporteCrear.add(duplicarVigenciaDeporte);
                    vigenciaTablaSeleccionada = duplicarVigenciaDeporte;
                    getListVigenciasDeportes();
                    contarRegistrosVD();
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:infoRegistro");
                    RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
                    RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigencias').hide()");
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                    if (bandera == 1) {
                        altoTabla = "305";
                        veFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veFechaInicial");
                        veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                        veFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veFechaFinal");
                        veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                        veDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veDescripcion");
                        veDescripcion.setFilterStyle("display: none; visibility: hidden;");
                        veIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veIndividual");
                        veIndividual.setFilterStyle("display: none; visibility: hidden;");
                        veCIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veCIndividual");
                        veCIndividual.setFilterStyle("display: none; visibility: hidden;");
                        veGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veGrupal");
                        veGrupal.setFilterStyle("display: none; visibility: hidden;");
                        veCGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veCGrupal");
                        veCGrupal.setFilterStyle("display: none; visibility: hidden;");
                        RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
                        bandera = 0;
                        filtrarListVigenciasDeportes = null;
                        tipoLista = 0;
                    }
                    duplicarVigenciaDeporte = new VigenciasDeportes();
                    deshabilitarBotonLov();

                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorDuplicarVigenciaD').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    public void limpiarDuplicar() {
        duplicarVigenciaDeporte = new VigenciasDeportes();
        duplicarVigenciaDeporte.setDeporte(new Deportes());
    }

    public void borrarVigenciaDeporte() {

        if (vigenciaTablaSeleccionada != null) {
            if (!listVigenciaDeporteModificar.isEmpty() && listVigenciaDeporteModificar.contains(vigenciaTablaSeleccionada)) {
                int modIndex = listVigenciaDeporteModificar.indexOf(vigenciaTablaSeleccionada);
                listVigenciaDeporteModificar.remove(modIndex);
                listVigenciaDeporteBorrar.add(vigenciaTablaSeleccionada);
            } else if (!listVigenciaDeporteCrear.isEmpty() && listVigenciaDeporteCrear.contains(vigenciaTablaSeleccionada)) {
                int crearIndex = listVigenciaDeporteCrear.indexOf(vigenciaTablaSeleccionada);
                listVigenciaDeporteCrear.remove(crearIndex);
            } else {
                listVigenciaDeporteBorrar.add(vigenciaTablaSeleccionada);
            }
            listVigenciasDeportes.remove(vigenciaTablaSeleccionada);
            if (tipoLista == 1) {
                filtrarListVigenciasDeportes.remove(vigenciaTablaSeleccionada);
            }
            contarRegistrosVD();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
            vigenciaTablaSeleccionada = null;
            deshabilitarBotonLov();

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            altoTabla = "285";
            veFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veFechaInicial");
            veFechaInicial.setFilterStyle("width: 85% !important");
            veFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veFechaFinal");
            veFechaFinal.setFilterStyle("width: 85% !important");
            veDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veDescripcion");
            veDescripcion.setFilterStyle("width: 85% !important");
            veIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veIndividual");
            veIndividual.setFilterStyle("width: 85% !important");
            veCIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veCIndividual");
            veCIndividual.setFilterStyle("width: 85% !important");
            veGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veGrupal");
            veGrupal.setFilterStyle("width: 85% !important");
            veCGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veCGrupal");
            veCGrupal.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "305";
            veFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veFechaInicial");
            veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            veFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veFechaFinal");
            veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            veDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veDescripcion");
            veDescripcion.setFilterStyle("display: none; visibility: hidden;");
            veIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veIndividual");
            veIndividual.setFilterStyle("display: none; visibility: hidden;");
            veCIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veCIndividual");
            veCIndividual.setFilterStyle("display: none; visibility: hidden;");
            veGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veGrupal");
            veGrupal.setFilterStyle("display: none; visibility: hidden;");
            veCGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veCGrupal");
            veCGrupal.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
            bandera = 0;
            filtrarListVigenciasDeportes = null;
            tipoLista = 0;
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (bandera == 1) {
            altoTabla = "305";
            veFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veFechaInicial");
            veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            veFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veFechaFinal");
            veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            veDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veDescripcion");
            veDescripcion.setFilterStyle("display: none; visibility: hidden;");
            veIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veIndividual");
            veIndividual.setFilterStyle("display: none; visibility: hidden;");
            veCIndividual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veCIndividual");
            veCIndividual.setFilterStyle("display: none; visibility: hidden;");
            veGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veGrupal");
            veGrupal.setFilterStyle("display: none; visibility: hidden;");
            veCGrupal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasDeportes:veCGrupal");
            veCGrupal.setFilterStyle("display: none; visibility: hidden;");
            contarRegistrosVD();
            RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
            bandera = 0;
            filtrarListVigenciasDeportes = null;
            tipoLista = 0;
            deshabilitarBotonLov();
        }

        listVigenciaDeporteBorrar.clear();
        listVigenciaDeporteCrear.clear();
        listVigenciaDeporteModificar.clear();
        vigenciaTablaSeleccionada = null;
        k = 0;
        listVigenciasDeportes = null;
        guardado = true;

    }

    public void asignarIndex(VigenciasDeportes vigenciaDeportes, int LND) {
        vigenciaTablaSeleccionada = vigenciaDeportes;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
            habilitarBotonLov();
        } else if (LND == 1) {
            tipoActualizacion = 1;
            deshabilitarBotonLov();
        } else if (LND == 2) {
            tipoActualizacion = 2;
            deshabilitarBotonLov();
        }
        contarRegistroDeporte();
        contarRegistrosVD();
        RequestContext.getCurrentInstance().update("form:DeportesDialogo");
        RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').show()");
    }

    public void actualizarDeporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaTablaSeleccionada.setDeporte(deporteSeleccionado);
                if (!listVigenciaDeporteCrear.contains(vigenciaTablaSeleccionada)) {
                    if (listVigenciaDeporteModificar.isEmpty()) {
                        listVigenciaDeporteModificar.add(vigenciaTablaSeleccionada);
                    } else if (!listVigenciaDeporteModificar.contains(vigenciaTablaSeleccionada)) {
                        listVigenciaDeporteModificar.add(vigenciaTablaSeleccionada);
                    }
                }
            } else {
                vigenciaTablaSeleccionada.setDeporte(deporteSeleccionado);
                if (!listVigenciaDeporteCrear.contains(vigenciaTablaSeleccionada)) {
                    if (listVigenciaDeporteModificar.isEmpty()) {
                        listVigenciaDeporteModificar.add(vigenciaTablaSeleccionada);
                    } else if (!listVigenciaDeporteModificar.contains(vigenciaTablaSeleccionada)) {
                        listVigenciaDeporteModificar.add(vigenciaTablaSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaDeporte.setDeporte(deporteSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigencias");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaDeporte.setDeporte(deporteSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigencias");
        }
        filtrarListDeportes = null;
        deporteSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("form:DeportesDialogo");
        RequestContext.getCurrentInstance().update("form:lovDeportes");
        RequestContext.getCurrentInstance().update("form:aceptarD");

        context.reset("form:lovDeportes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovDeportes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').hide()");
    }

    public void cancelarCambioDeporte() {
        filtrarListDeportes = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        deporteSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovDeportes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovDeportes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:DeportesDialogo");
        RequestContext.getCurrentInstance().update("form:lovDeportes");
        RequestContext.getCurrentInstance().update("form:aceptarD");
    }

    public void listaValoresBoton() {
        if (vigenciaTablaSeleccionada != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 2) {
                habilitarBotonLov();
                contarRegistroDeporte();
                RequestContext.getCurrentInstance().update("form:DeportesDialogo");
                RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void habilitarBotonLov() {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasDeportesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasDeportesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }
    //EVENTO FILTRAR

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        deshabilitarBotonLov();
        contarRegistrosVD();
    }

    public void contarRegistroDeporte() {
        RequestContext.getCurrentInstance().update("form:infoRegistroDeporte");
    }

    public void contarRegistrosVD() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");

    }
//RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaTablaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(vigenciaTablaSeleccionada.getSecuencia(), "VIGENCIASDEPORTES");
            backUpSecRegistro = vigenciaTablaSeleccionada.getSecuencia();
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
            deshabilitarBotonLov();
        } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASDEPORTES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void recordarSeleccionVD() {
        if (vigenciaTablaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasDeportes");
            tablaC.setSelection(vigenciaTablaSeleccionada);
        }
    }
    //GETTERS AND SETTERS

    public List<VigenciasDeportes> getListVigenciasDeportes() {
        try {
            if (listVigenciasDeportes == null) {
                if (empleado.getPersona().getSecuencia() != null) {
                    listVigenciasDeportes = administrarVigenciaDeporte.listVigenciasDeportesPersona(empleado.getPersona().getSecuencia());
                }
            }
            return listVigenciasDeportes;
        } catch (Exception e) {
            System.out.println("Error...!! getListVigenciasDeportes : " + e.toString());
            return null;
        }
    }

    public void setListVigenciasDeportes(List<VigenciasDeportes> setListVigenciasDeportes) {
        this.listVigenciasDeportes = setListVigenciasDeportes;
    }

    public List<VigenciasDeportes> getFiltrarListVigenciasDeportes() {
        return filtrarListVigenciasDeportes;
    }

    public void setFiltrarListVigenciasDeportes(List<VigenciasDeportes> setFiltrarListVigenciasDeportes) {
        this.filtrarListVigenciasDeportes = setFiltrarListVigenciasDeportes;
    }

    public VigenciasDeportes getNuevaVigenciaDeporte() {
        return nuevaVigenciaDeporte;
    }

    public void setNuevaVigenciaDeporte(VigenciasDeportes setNuevaVigenciaDeporte) {
        this.nuevaVigenciaDeporte = setNuevaVigenciaDeporte;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public List<Deportes> getListDeportes() {
        listDeportes = administrarVigenciaDeporte.listDeportes();
        return listDeportes;
    }

    public void setListDeportes(List<Deportes> setListDeportes) {
        this.listDeportes = setListDeportes;
    }

    public List<Deportes> getFiltrarListDeportes() {
        return filtrarListDeportes;
    }

    public void setFiltrarListDeportes(List<Deportes> setFiltrarListDeportes) {
        this.filtrarListDeportes = setFiltrarListDeportes;
    }

    public VigenciasDeportes getEditarVigenciaDeporte() {
        return editarVigenciaDeporte;
    }

    public void setEditarVigenciaDeporte(VigenciasDeportes setEditarVigenciaDeporte) {
        this.editarVigenciaDeporte = setEditarVigenciaDeporte;
    }

    public VigenciasDeportes getDuplicarVigenciaDeporte() {
        return duplicarVigenciaDeporte;
    }

    public void setDuplicarVigenciaDeporte(VigenciasDeportes setDuplicarVigenciaDeporte) {
        this.duplicarVigenciaDeporte = setDuplicarVigenciaDeporte;
    }

    public Deportes getDeporteSeleccionado() {
        return deporteSeleccionado;
    }

    public void setDeporteSeleccionado(Deportes setDeporteSeleccionado) {
        this.deporteSeleccionado = setDeporteSeleccionado;
    }

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
        this.backUpSecRegistro = backUpSecRegistro;
    }

    public Empleados getEmpleado() {
        return empleado;
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasDeportes");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public String getInfoRegistroDeporte() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovDeportes");
        infoRegistroDeporte = String.valueOf(tabla.getRowCount());
        return infoRegistroDeporte;
    }

    public VigenciasDeportes getVigenciaTablaSeleccionada() {
//        getListVigenciasDeportes();
//        if (listVigenciasDeportes != null) {
//            int tam = listVigenciasDeportes.size();
//            if (tam > 0) {
//                vigenciaTablaSeleccionada = listVigenciasDeportes.get(0);
//            }
//        }
        return vigenciaTablaSeleccionada;
    }

    public void setVigenciaTablaSeleccionada(VigenciasDeportes vigenciaTablaSeleccionada) {
        this.vigenciaTablaSeleccionada = vigenciaTablaSeleccionada;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

}
