/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Paises;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPaisesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import Entidades.Ciudades;
import Entidades.Departamentos;
import Entidades.Festivos;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
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
public class ControlPaises implements Serializable {

   private static Logger log = Logger.getLogger(ControlPaises.class);

    @EJB
    AdministrarPaisesInterface administrarPaises;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Paises> listPaises;
    private List<Paises> filtrarPaises;
    private List<Paises> crearPaises;
    private List<Paises> modificarPaises;
    private List<Paises> borrarPaises;
    private Paises nuevoPaises;
    private Paises duplicarPaises;
    private Paises editarPaises;
    private Paises paisSeleccionado;
    //LOVS
    private List<Festivos> lovFestivos;
    private List<Festivos> lovFestivosFiltrar;
    private Festivos festivoSeleccionado;

    private List<Departamentos> lovDeptos;
    private List<Departamentos> lovDeptosFiltrar;
    private Departamentos deptoSeleccionado;
    private Departamentos deptoaux;

    private List<Ciudades> lovCiudades;
    private List<Ciudades> lovCiudadesFiltrar;
    private Ciudades ciudadSeleccionada;

    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private boolean activarLov;
    private String infoRegistro, infoRegistroFestivos, infoRegistrosCiudades, infoRegistroDepartamentos;

    public ControlPaises() {
        listPaises = null;
        crearPaises = new ArrayList<Paises>();
        modificarPaises = new ArrayList<Paises>();
        borrarPaises = new ArrayList<Paises>();
        permitirIndex = true;
        editarPaises = new Paises();
        nuevoPaises = new Paises();
        duplicarPaises = new Paises();
        guardado = true;
        tamano = 320;
        mapParametros.put("paginaAnterior", paginaAnterior);
        activarLov = true;
        lovCiudades = null;
        lovDeptos = null;
        lovFestivos = null;
        ciudadSeleccionada = null;
        festivoSeleccionado = null;
    }

    public void limpiarListasValor() {
        lovCiudades = null;
        lovDeptos = null;
        lovFestivos = null;
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
            administrarPaises.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listPaises = null;
        getListPaises();
        if (listPaises != null) {
            if (!listPaises.isEmpty()) {
                paisSeleccionado = listPaises.get(0);
            }
        }
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
        String pagActual = "pais";
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

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            log.warn("Error ControlPaises eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(Paises pais, int celda) {
        paisSeleccionado = pais;
        cualCelda = celda;
        if (cualCelda == 0) {
            paisSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            paisSeleccionado.getNombre();
        }
        paisSeleccionado.getSecuencia();
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosPaises:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPaises:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPaises");
            bandera = 0;
            filtrarPaises = null;
            tipoLista = 0;
            tamano = 320;
        }

        borrarPaises.clear();
        crearPaises.clear();
        modificarPaises.clear();
        paisSeleccionado = null;
        k = 0;
        listPaises = null;
        guardado = true;
        permitirIndex = true;
        getListPaises();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosPaises");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosPaises:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPaises:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPaises");
            bandera = 0;
            filtrarPaises = null;
            tipoLista = 0;
            tamano = 320;
        }
        borrarPaises.clear();
        crearPaises.clear();
        modificarPaises.clear();
        paisSeleccionado = null;
        k = 0;
        listPaises = null;
        guardado = true;
        permitirIndex = true;
        RequestContext.getCurrentInstance().update("form:datosPaises");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 300;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPaises:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPaises:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosPaises");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 320;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPaises:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPaises:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPaises");
            bandera = 0;
            filtrarPaises = null;
            tipoLista = 0;
        }
    }

    public void modificarPaises(Paises pais) {
        paisSeleccionado = pais;
        if (!crearPaises.contains(paisSeleccionado)) {
            if (modificarPaises.isEmpty()) {
                modificarPaises.add(paisSeleccionado);
            } else if (!modificarPaises.contains(paisSeleccionado)) {
                modificarPaises.add(paisSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosPaises");
    }

    public void borrandoPaises() {
        if (paisSeleccionado != null) {
            if (!modificarPaises.isEmpty() && modificarPaises.contains(paisSeleccionado)) {
                int modIndex = modificarPaises.indexOf(paisSeleccionado);
                modificarPaises.remove(modIndex);
                borrarPaises.add(paisSeleccionado);
            } else if (!crearPaises.isEmpty() && crearPaises.contains(paisSeleccionado)) {
                int crearIndex = crearPaises.indexOf(paisSeleccionado);
                crearPaises.remove(crearIndex);
            } else {
                borrarPaises.add(paisSeleccionado);
            }
            listPaises.remove(paisSeleccionado);
            if (tipoLista == 1) {
                filtrarPaises.remove(paisSeleccionado);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosPaises");
            paisSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void verificarBorrado() {
        BigInteger contarDepartamentosPais;
        BigInteger contarFestivosPais;
        try {
            contarDepartamentosPais = administrarPaises.contarDepartamentosPais(paisSeleccionado.getSecuencia());
            contarFestivosPais = administrarPaises.contarFestivosPais(paisSeleccionado.getSecuencia());
            if (contarDepartamentosPais.equals(new BigInteger("0"))
                    && contarFestivosPais.equals(new BigInteger("0"))) {
                borrandoPaises();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                contarDepartamentosPais = new BigInteger("-1");
                contarFestivosPais = new BigInteger("-1");
            }
        } catch (Exception e) {
            log.error("ERROR ControlPaises verificarBorrado ERROR  ", e);
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarPaises.isEmpty() || !crearPaises.isEmpty() || !modificarPaises.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarPaises() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == false) {
            log.info("Realizando guardarPaises");
            if (!borrarPaises.isEmpty()) {
                administrarPaises.borrarPaises(borrarPaises);
                //mostrarBorrados
                registrosBorrados = borrarPaises.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarPaises.clear();
            }
            if (!modificarPaises.isEmpty()) {
                administrarPaises.modificarPaises(modificarPaises);
                modificarPaises.clear();
            }
            if (!crearPaises.isEmpty()) {
                administrarPaises.crearPaises(crearPaises);
                crearPaises.clear();
            }
            listPaises = null;
            getListPaises();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosPaises");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void editarCelda() {
        if (paisSeleccionado != null) {
            editarPaises = paisSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void guardarSalir() {
        guardarPaises();
        salir();
    }

    public void cancelarSalir() {
        cancelarModificacion();
        salir();
    }

    public void agregarNuevoPaises() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoPaises.getCodigo() == a) {
        } else {
            for (int x = 0; x < listPaises.size(); x++) {
                if (listPaises.get(x).getCodigo() == nuevoPaises.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado está en uso. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoPaises.getNombre() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

        } else if (nuevoPaises.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

        } else {
            contador++;
        }
        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                log.info("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosPaises:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosPaises:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPaises");
                bandera = 0;
                filtrarPaises = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoPaises.setSecuencia(l);
            crearPaises.add(nuevoPaises);
            listPaises.add(nuevoPaises);
            paisSeleccionado = nuevoPaises;
            RequestContext.getCurrentInstance().update("form:datosPaises");
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            nuevoPaises = new Paises();
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPaises').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoPais");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoPais').show()");
        }
    }

    public void limpiarNuevoPaises() {
        nuevoPaises = new Paises();
    }

    public void duplicandoPaises() {
        if (paisSeleccionado != null) {
            duplicarPaises = new Paises();
            k++;
            l = BigInteger.valueOf(k);
            duplicarPaises.setSecuencia(l);
            duplicarPaises.setCodigo(paisSeleccionado.getCodigo());
            duplicarPaises.setNombre(paisSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPaises').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        if (duplicarPaises.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listPaises.size(); x++) {
                if (listPaises.get(x).getCodigo() == duplicarPaises.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado está en uso. Por favor ingrese un código válido";
            } else {
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarPaises.getNombre() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else if (duplicarPaises.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            log.info("bandera");
            contador++;
        }
        if (contador == 2) {
            listPaises.add(duplicarPaises);
            crearPaises.add(duplicarPaises);
            paisSeleccionado = duplicarPaises;
            RequestContext.getCurrentInstance().update("form:datosPaises");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosPaises:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosPaises:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPaises");
                bandera = 0;
                filtrarPaises = null;
                tipoLista = 0;
            }
            duplicarPaises = new Paises();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPaises').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarPaises() {
        duplicarPaises = new Paises();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPaisesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Paises", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPaisesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Paises", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (paisSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(paisSeleccionado.getSecuencia(), "PAISES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("PAISES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosFestivos() {
        RequestContext.getCurrentInstance().update("form:infoRegistroFestivos");
    }

    public void contarRegistrosCiudades() {
        RequestContext.getCurrentInstance().update("form:infoRegistroCiudades");
    }

    public void contarRegistrosDepartamentos() {
        RequestContext.getCurrentInstance().update("form:infoRegistroDepartamentos");
    }

    public void cargarLovFestivos() {
        if (lovFestivos == null) {
            if (paisSeleccionado != null) {
                lovFestivos = administrarPaises.consultarFestivosPorPais(paisSeleccionado.getSecuencia());
            }
        }
    }

    public void cargarLovDeptos() {
        if (lovDeptos == null) {
            if (paisSeleccionado != null) {
                lovDeptos = administrarPaises.consultarDeptosPorPais(paisSeleccionado.getSecuencia());
            }
        }
    }

    public void cargarLovCiudades() {
        if (lovCiudades == null) {
            if (deptoSeleccionado != null) {
                lovCiudades = administrarPaises.consultarCiudadesPorDepto(deptoaux.getSecuencia());
            }
        }
    }

    public void mostrarDialogoFestivos() {
        lovFestivos = null;
        cargarLovFestivos();
        RequestContext.getCurrentInstance().update("form:festivosDialogo");
        RequestContext.getCurrentInstance().execute("PF('festivosDialogo').show()");
    }

    public void mostrarDialogoDeptos() {
        lovDeptos = null;
        cargarLovDeptos();
        RequestContext.getCurrentInstance().update("form:departamentosDialogo");
        RequestContext.getCurrentInstance().execute("PF('departamentosDialogo').show()");
    }

    public void mostrarDialogoCiudades() {
        lovCiudades = null;
        cargarLovCiudades();
        RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
    }

    public void seleccionarDepartamento() {
        deptoaux = deptoSeleccionado;
        log.info("Departamento seleccionado " + deptoaux.getNombre());
    }

    //*/*/*/*/*/*/*/*/GETS Y SETS*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Paises> getListPaises() {
        if (listPaises == null) {
            listPaises = administrarPaises.consultarPaises();
        }
        return listPaises;
    }

    public void setListPaises(List<Paises> listPaises) {
        this.listPaises = listPaises;
    }

    public List<Paises> getFiltrarPaises() {
        return filtrarPaises;
    }

    public void setFiltrarPaises(List<Paises> filtrarPaises) {
        this.filtrarPaises = filtrarPaises;
    }

    public Paises getNuevoPaises() {
        return nuevoPaises;
    }

    public void setNuevoPaises(Paises nuevoPaises) {
        this.nuevoPaises = nuevoPaises;
    }

    public Paises getDuplicarPaises() {
        return duplicarPaises;
    }

    public void setDuplicarPaises(Paises duplicarPaises) {
        this.duplicarPaises = duplicarPaises;
    }

    public Paises getEditarPaises() {
        return editarPaises;
    }

    public void setEditarPaises(Paises editarPaises) {
        this.editarPaises = editarPaises;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public Paises getPaisSeleccionado() {
        return paisSeleccionado;
    }

    public void setPaisSeleccionado(Paises paisSeleccionado) {
        this.paisSeleccionado = paisSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosPaises");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public List<Festivos> getLovFestivos() {
        return lovFestivos;
    }

    public void setLovFestivos(List<Festivos> lovFestivos) {
        this.lovFestivos = lovFestivos;
    }

    public List<Festivos> getLovFestivosFiltrar() {
        return lovFestivosFiltrar;
    }

    public void setLovFestivosFiltrar(List<Festivos> lovFestivosFiltrar) {
        this.lovFestivosFiltrar = lovFestivosFiltrar;
    }

    public Festivos getFestivoSeleccionado() {
        return festivoSeleccionado;
    }

    public void setFestivoSeleccionado(Festivos festivoSeleccionado) {
        this.festivoSeleccionado = festivoSeleccionado;
    }

    public List<Departamentos> getLovDeptos() {
        return lovDeptos;
    }

    public void setLovDeptos(List<Departamentos> lovDeptos) {
        this.lovDeptos = lovDeptos;
    }

    public List<Departamentos> getLovDeptosFiltrar() {
        return lovDeptosFiltrar;
    }

    public void setLovDeptosFiltrar(List<Departamentos> lovDeptosFiltrar) {
        this.lovDeptosFiltrar = lovDeptosFiltrar;
    }

    public Departamentos getDeptoSeleccionado() {
        return deptoSeleccionado;
    }

    public void setDeptoSeleccionado(Departamentos deptoSeleccionado) {
        this.deptoSeleccionado = deptoSeleccionado;
    }

    public List<Ciudades> getLovCiudades() {
        return lovCiudades;
    }

    public void setLovCiudades(List<Ciudades> lovCiudades) {
        this.lovCiudades = lovCiudades;
    }

    public List<Ciudades> getLovCiudadesFiltrar() {
        return lovCiudadesFiltrar;
    }

    public void setLovCiudadesFiltrar(List<Ciudades> lovCiudadesFiltrar) {
        this.lovCiudadesFiltrar = lovCiudadesFiltrar;
    }

    public Ciudades getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public String getInfoRegistroFestivos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovFestivos");
        infoRegistroFestivos = String.valueOf(tabla.getRowCount());
        return infoRegistroFestivos;
    }

    public void setInfoRegistroFestivos(String infoRegistroFestivos) {
        this.infoRegistroFestivos = infoRegistroFestivos;
    }

    public String getInfoRegistrosCiudades() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCiudades");
        infoRegistrosCiudades = String.valueOf(tabla.getRowCount());
        return infoRegistrosCiudades;
    }

    public void setInfoRegistrosCiudades(String infoRegistrosCiudades) {
        this.infoRegistrosCiudades = infoRegistrosCiudades;
    }

    public String getInfoRegistroDepartamentos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovDepartamentos");
        infoRegistroDepartamentos = String.valueOf(tabla.getRowCount());
        return infoRegistroDepartamentos;
    }

    public void setInfoRegistroDepartamentos(String infoRegistroDepartamentos) {
        this.infoRegistroDepartamentos = infoRegistroDepartamentos;
    }

}
