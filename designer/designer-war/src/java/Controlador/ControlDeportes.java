/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Deportes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarDeportesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlDeportes implements Serializable {

    @EJB
    AdministrarDeportesInterface administrarDeportes;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Deportes> listDeportes;
    private List<Deportes> filtrarDeportes;
    private List<Deportes> crearDeportes;
    private List<Deportes> modificarDeportes;
    private List<Deportes> borrarDeportes;
    private Deportes nuevoDeporte;
    private Deportes duplicarDeporte;
    private Deportes editarDeportes;
    private Deportes deporteSeleccionado;
    private int tamano;
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
    private Integer a;
    private BigInteger backUpCodigo;
    private String backUpDescripcion;
    private String infoRegistro;
    private DataTable tablaC;
    private boolean activarLOV;
    private BigInteger verificarBorradoVigenciasDeportes;
    private BigInteger contadorDeportesPersonas;
    private BigInteger contadorParametrosInformes;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlDeportes() {
        listDeportes = null;
        crearDeportes = new ArrayList<Deportes>();
        modificarDeportes = new ArrayList<Deportes>();
        borrarDeportes = new ArrayList<Deportes>();
        permitirIndex = true;
        editarDeportes = new Deportes();
        nuevoDeporte = new Deportes();
        duplicarDeporte = new Deportes();
        a = null;
        guardado = true;
        tamano = 270;
        paginaAnterior = "nominaf";
        activarLOV = true;
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
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina();
         System.out.println("navegar('Atras') : " + pag);
        } else {
            String pagActual = "deporte";
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
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
        }
        limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarDeportes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
        }
    }

    public void recibirPag(String pagina) {
        paginaAnterior = pagina;
        listDeportes = null;
        getListDeportes();
        if (listDeportes != null) {
            if (!listDeportes.isEmpty()) {
                deporteSeleccionado = listDeportes.get(0);
            }
        }
    }

    public String retornarPagina() {
        return paginaAnterior;
    }

    public void cambiarIndice(Deportes deporte, int celda) {
        if (permitirIndex == true) {
            deporteSeleccionado = deporte;
            cualCelda = celda;
            if (cualCelda == 0) {
                backUpCodigo = deporteSeleccionado.getCodigo();
            }
            if (cualCelda == 1) {
                backUpDescripcion = deporteSeleccionado.getNombre();
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosDeporte:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosDeporte:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            //contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosDeporte");
            bandera = 0;
            filtrarDeportes = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarDeportes.clear();
        crearDeportes.clear();
        modificarDeportes.clear();
        k = 0;
        listDeportes = null;
        deporteSeleccionado = null;
        guardado = true;
        permitirIndex = true;
        getListDeportes();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:infoRegistro");
        RequestContext.getCurrentInstance().update("form:datosDeporte");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {  limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosDeporte:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosDeporte:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosDeporte");
            bandera = 0;
            filtrarDeportes = null;
            tipoLista = 0;
        }

        borrarDeportes.clear();
        crearDeportes.clear();
        modificarDeportes.clear();
        deporteSeleccionado = null;
        k = 0;
        listDeportes = null;
        guardado = true;
        permitirIndex = true;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosDeporte");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosDeporte:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosDeporte:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosDeporte");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosDeporte:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosDeporte:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDeporte");
            bandera = 0;
            filtrarDeportes = null;
            tipoLista = 0;
        }
    }

    public void modificarDeporte(Deportes deporte) {
        deporteSeleccionado = deporte;
        if (!crearDeportes.contains(deporteSeleccionado)) {
            if (!modificarDeportes.contains(deporteSeleccionado)) {
                modificarDeportes.add(deporteSeleccionado);
            }
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosDeporte");
    }

    public void borrarDeporte() {

        RequestContext context = RequestContext.getCurrentInstance();
        if (deporteSeleccionado != null) {

            if (tipoLista == 0) {
                if (!modificarDeportes.isEmpty() && modificarDeportes.contains(deporteSeleccionado)) {
                    modificarDeportes.remove(modificarDeportes.indexOf(deporteSeleccionado));
                    borrarDeportes.add(deporteSeleccionado);
                } else if (!crearDeportes.isEmpty() && crearDeportes.contains(deporteSeleccionado)) {
                    crearDeportes.remove(crearDeportes.indexOf(deporteSeleccionado));
                } else {
                    borrarDeportes.add(deporteSeleccionado);
                }
                listDeportes.remove(deporteSeleccionado);
            }
            if (tipoLista == 1) {
                filtrarDeportes.remove(deporteSeleccionado);
                listDeportes.remove(deporteSeleccionado);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosDeporte");
            deporteSeleccionado = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void verificarBorrado() {
        verificarBorradoVigenciasDeportes = administrarDeportes.contarVigenciasDeportesDeporte(deporteSeleccionado.getSecuencia());
        contadorDeportesPersonas = administrarDeportes.contarPersonasDeporte(deporteSeleccionado.getSecuencia());
        contadorParametrosInformes = administrarDeportes.contarParametrosInformesDeportes(deporteSeleccionado.getSecuencia());
        if (!verificarBorradoVigenciasDeportes.equals(new BigInteger("0")) && !contadorDeportesPersonas.equals(new BigInteger("0")) && !contadorParametrosInformes.equals(new BigInteger("0"))) {

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            deporteSeleccionado = null;

            verificarBorradoVigenciasDeportes = new BigInteger("-1");
            contadorDeportesPersonas = new BigInteger("-1");
            contadorParametrosInformes = new BigInteger("-1");

        } else {
            borrarDeporte();
        }
    }

    public void guardarDeportes() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            if (!borrarDeportes.isEmpty()) {
                administrarDeportes.borrarDeportes(borrarDeportes);
                //mostrarBorrados
                registrosBorrados = borrarDeportes.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarDeportes.clear();
            }
            if (!crearDeportes.isEmpty()) {
                administrarDeportes.crearDeportes(crearDeportes);
                crearDeportes.clear();
            }
            if (!modificarDeportes.isEmpty()) {
                administrarDeportes.modificarDeportes(modificarDeportes);
                modificarDeportes.clear();
            }
            listDeportes = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosDeporte");
            k = 0;
        }
        deporteSeleccionado = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (deporteSeleccionado != null) {
            editarDeportes = deporteSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
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

    public void agregarNuevoDeportes() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoDeporte.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listDeportes.size(); x++) {
                if (listDeportes.get(x).getCodigo() == nuevoDeporte.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoDeporte.getNombre() == (null)) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 2) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                tamano = 270;
                codigo = (Column) c.getViewRoot().findComponent("form:datosDeporte:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosDeporte:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDeporte");
                bandera = 0;
                filtrarDeportes = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoDeporte.setSecuencia(l);
            crearDeportes.add(nuevoDeporte);
            deporteSeleccionado = nuevoDeporte;
            listDeportes.add(nuevoDeporte);
            nuevoDeporte = new Deportes();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosDeporte");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroDeporte').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoDeporte");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoDeporte').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoDeportes() {
        nuevoDeporte = new Deportes();
        deporteSeleccionado = null;

    }

    //------------------------------------------------------------------------------
    public void duplicarDeportes() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (deporteSeleccionado != null) {
            duplicarDeporte = new Deportes();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarDeporte.setSecuencia(l);
                duplicarDeporte.setCodigo(deporteSeleccionado.getCodigo());
                duplicarDeporte.setNombre(deporteSeleccionado.getNombre());
            }
            if (tipoLista == 1) {
                duplicarDeporte.setSecuencia(l);
                duplicarDeporte.setCodigo(deporteSeleccionado.getCodigo());
                duplicarDeporte.setNombre(deporteSeleccionado.getNombre());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDepor");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroDeporte').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarDeporte.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listDeportes.size(); x++) {
                if (listDeportes.get(x).getCodigo() == duplicarDeporte.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
            } else {
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarDeporte.getNombre() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            listDeportes.add(duplicarDeporte);
            crearDeportes.add(duplicarDeporte);
            RequestContext.getCurrentInstance().update("form:datosDeporte");
            deporteSeleccionado = duplicarDeporte;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                tamano = 270;
                codigo = (Column) c.getViewRoot().findComponent("form:datosDeporte:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosDeporte:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDeporte");
                bandera = 0;
                filtrarDeportes = null;
                tipoLista = 0;
            }
            duplicarDeporte = new Deportes();
            contarRegistros();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroDeporte').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarDeporte");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarDeporte').show()");
        }
    }

    public void limpiarDuplicarDeportes() {
        duplicarDeporte = new Deportes();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDeportesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "DEPORTES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDeportesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "DEPORTES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (deporteSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(deporteSeleccionado.getSecuencia(), "DEPORTES");
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
        } else if (administrarRastros.verificarHistoricosTabla("DEPORTES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void recordarSeleccionDeporte() {
        if (deporteSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosDeporte");
            tablaC.setSelection(deporteSeleccionado);
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    //------------------------------------------------------------------------------
    public List<Deportes> getListDeportes() {
        if (listDeportes == null) {
            listDeportes = administrarDeportes.consultarDeportes();
        }
        return listDeportes;
    }

    public void setListDeportes(List<Deportes> listDeportes) {
        this.listDeportes = listDeportes;
    }

    public List<Deportes> getFiltrarDeportes() {
        return filtrarDeportes;
    }

    public void setFiltrarDeportes(List<Deportes> filtrarDeportes) {
        this.filtrarDeportes = filtrarDeportes;
    }

    public Deportes getNuevoDeporte() {
        return nuevoDeporte;
    }

    public void setNuevoDeporte(Deportes nuevoDeporte) {
        this.nuevoDeporte = nuevoDeporte;
    }

    public Deportes getDuplicarDeporte() {
        return duplicarDeporte;
    }

    public void setDuplicarDeporte(Deportes duplicarDeporte) {
        this.duplicarDeporte = duplicarDeporte;
    }

    public Deportes getEditarDeportes() {
        return editarDeportes;
    }

    public void setEditarDeportes(Deportes editarDeportes) {
        this.editarDeportes = editarDeportes;
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

    public String getPaginaanterior() {
        return paginaAnterior;
    }

    public void setPaginaanterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public Deportes getDeporteSeleccionado() {
        return deporteSeleccionado;
    }

    public void setDeporteSeleccionado(Deportes deporteSeleccionado) {
        this.deporteSeleccionado = deporteSeleccionado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDeporte");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
