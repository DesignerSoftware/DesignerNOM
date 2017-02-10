/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.HvReferencias;
import Entidades.HVHojasDeVida;
import Entidades.Personas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarHvReferenciasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
public class ControlHvReferencias implements Serializable {

    @EJB
    AdministrarHvReferenciasInterface administrarHvReferencias;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<HvReferencias> listHvReferencias;
    private List<HvReferencias> filtrarHvReferencias;
    private List<HvReferencias> crearHvReferencias;
    private List<HvReferencias> modificarHvReferencias;
    private List<HvReferencias> borrarHvReferencias;
    private HvReferencias nuevoHvReferencia;
    private HvReferencias duplicarHvReferencia;
    private HvReferencias editarHvReferencia;
    private HvReferencias hvReferenciaSeleccionada;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex, activarLov;
    //RASTRO
    private Column cargo, nombre, numTelefono, numCelular;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger secuenciaPersona;
//Empleado
    private Personas personaSeleccionada;
    private Empleados empleado;
//otros
    private int tamano;
    private String infoRegistro;
    private HVHojasDeVida hvHojasDeVida;
    private List<HVHojasDeVida> listHVHojasDeVida;
    private DataTable tablaC;
    private String backUpDescripcion;
    private Long backUpTelefono;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlHvReferencias() {
        listHvReferencias = null;
        crearHvReferencias = new ArrayList<HvReferencias>();
        modificarHvReferencias = new ArrayList<HvReferencias>();
        borrarHvReferencias = new ArrayList<HvReferencias>();
        permitirIndex = true;
        editarHvReferencia = new HvReferencias();
        nuevoHvReferencia = new HvReferencias();
        nuevoHvReferencia.setTipo("PERSONALES");
        duplicarHvReferencia = new HvReferencias();
        empleado = new Empleados();
        secuenciaPersona = null;
        listHVHojasDeVida = new ArrayList<HVHojasDeVida>();
        guardado = true;
        tamano = 270;
        activarLov = true;
        hvHojasDeVida = new HVHojasDeVida();
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
        } else {
            String pagActual = "perreferencialaboral";
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

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarHvReferencias.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger secuencia) {

        secuenciaPersona = secuencia;
        listHvReferencias = null;
        empleado = administrarHvReferencias.empleadoActual(secuencia);
        getListHvReferencias();
        deshabilitarBotonLov();
        if (!listHvReferencias.isEmpty()) {
            hvReferenciaSeleccionada = listHvReferencias.get(0);
        }
    }

    public void mostrarNuevo() {
        System.err.println("nuevo Tipo Entrevista " + nuevoHvReferencia.getTipo());
    }

    public void cambiarIndice(HvReferencias hvReferencia, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);
        hvReferenciaSeleccionada = hvReferencia;
        if (permitirIndex == true) {
            cualCelda = celda;
            hvReferenciaSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpDescripcion = hvReferenciaSeleccionada.getNombrepersona();
                } else {
                    backUpDescripcion = hvReferenciaSeleccionada.getNombrepersona();
                }
            }
            if (cualCelda == 2) {
                if (tipoLista == 0) {
                    backUpTelefono = hvReferenciaSeleccionada.getTelefono();
                } else {
                    backUpTelefono = hvReferenciaSeleccionada.getTelefono();
                }
            }
        }
    }

//    public void asignarIndex(HvReferencias hvReferencia, int LND, int dig) {
//        try {
//            System.out.println("\n ENTRE A ControlHvReferencias.asignarIndex \n");
//            hvReferenciaSeleccionada = hvReferencia;
//            if (LND == 0) {
//                tipoActualizacion = 0;
//            } else if (LND == 1) {
//                tipoActualizacion = 1;
//                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
//            } else if (LND == 2) {
//                tipoActualizacion = 2;
//            }
//
//        } catch (Exception e) {
//            System.out.println("ERROR ControlHvReferencias.asignarIndex ERROR======" + e.getMessage());
//        }
//    }
    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
            numTelefono.setFilterStyle("display: none; visibility: hidden;");
            numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
            numCelular.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosHvReferencia");
            bandera = 0;
            filtrarHvReferencias = null;
            tipoLista = 0;
        }

        borrarHvReferencias.clear();
        crearHvReferencias.clear();
        modificarHvReferencias.clear();
        hvReferenciaSeleccionada = null;
        k = 0;
        listHvReferencias = null;
        guardado = true;
        permitirIndex = true;
        getListHvReferencias();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosHvReferencia");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
            numTelefono.setFilterStyle("display: none; visibility: hidden;");
            numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
            numCelular.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosHvReferencia");
            bandera = 0;
            filtrarHvReferencias = null;
            tipoLista = 0;
        }

        borrarHvReferencias.clear();
        crearHvReferencias.clear();
        modificarHvReferencias.clear();
        hvReferenciaSeleccionada = null;
        k = 0;
        listHvReferencias = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosHvReferencia");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
            nombre.setFilterStyle("width: 85% !important");
            cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
            cargo.setFilterStyle("width: 85% !important");
            numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
            numTelefono.setFilterStyle("width: 85% !important");
            numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
            numCelular.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosHvReferencia");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
            numTelefono.setFilterStyle("display: none; visibility: hidden;");
            numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
            numCelular.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosHvReferencia");
            bandera = 0;
            filtrarHvReferencias = null;
            tipoLista = 0;
        }
    }

    public void modificarHvReferencia(HvReferencias hvReferencia) {
        hvReferenciaSeleccionada = hvReferencia;
        if (!crearHvReferencias.contains(hvReferenciaSeleccionada)) {
            if (modificarHvReferencias.isEmpty()) {
                modificarHvReferencias.add(hvReferenciaSeleccionada);
            } else if (!modificarHvReferencias.contains(hvReferenciaSeleccionada)) {
                modificarHvReferencias.add(hvReferenciaSeleccionada);
            }
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:datosHvReferencia");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoHvReferencias() {

        if (hvReferenciaSeleccionada != null) {
            System.out.println("Entro a borrandoHvReferencias");
            if (!modificarHvReferencias.isEmpty() && modificarHvReferencias.contains(hvReferenciaSeleccionada)) {
                modificarHvReferencias.remove(modificarHvReferencias.indexOf(hvReferenciaSeleccionada));
                borrarHvReferencias.add(hvReferenciaSeleccionada);
            } else if (!crearHvReferencias.isEmpty() && crearHvReferencias.contains(hvReferenciaSeleccionada)) {
                crearHvReferencias.remove(crearHvReferencias.indexOf(hvReferenciaSeleccionada));
            } else {
                borrarHvReferencias.add(hvReferenciaSeleccionada);
            }
            listHvReferencias.remove(hvReferenciaSeleccionada);
            if (tipoLista == 1) {
                filtrarHvReferencias.remove(hvReferenciaSeleccionada);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            modificarInfoRegistro(listHvReferencias.size());
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosHvReferencia");
            hvReferenciaSeleccionada = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarHvReferencias.isEmpty() || !crearHvReferencias.isEmpty() || !modificarHvReferencias.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarHvReferencia() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarHvReferencia");
            if (!borrarHvReferencias.isEmpty()) {
                administrarHvReferencias.borrarHvReferencias(borrarHvReferencias);

                //mostrarBorrados
                registrosBorrados = borrarHvReferencias.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarHvReferencias.clear();
            }
            if (!crearHvReferencias.isEmpty()) {
                administrarHvReferencias.crearHvReferencias(crearHvReferencias);

                crearHvReferencias.clear();
            }
            if (!modificarHvReferencias.isEmpty()) {
                administrarHvReferencias.modificarHvReferencias(modificarHvReferencias);
                modificarHvReferencias.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listHvReferencias = null;
            getListHvReferencias();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosHvReferencia");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            k = 0;
            guardado = true;
        }
        hvReferenciaSeleccionada = null;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (hvReferenciaSeleccionada != null) {
            if (tipoLista == 0) {
                editarHvReferencia = hvReferenciaSeleccionada;
            }
            if (tipoLista == 1) {
                editarHvReferencia = hvReferenciaSeleccionada;
            }
            deshabilitarBotonLov();
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
                RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCargo");
                RequestContext.getCurrentInstance().execute("PF('editCargo').show()");
                cualCelda = -1;

            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTelefono");
                RequestContext.getCurrentInstance().execute("PF('editTelefono').show()");
                cualCelda = -1;

            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCelular");
                RequestContext.getCurrentInstance().execute("PF('editCelular').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoHvRefencias() {
        System.out.println("agregarNuevoHvRefencias");
        int contador = 0;
        nuevoHvReferencia.setHojadevida(new HVHojasDeVida());
        Short a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoHvReferencia.getNombrepersona() == (null)) {
            mensajeValidacion = "El campo Nombre es Obligatorio \n";
        } else {
            System.out.println("bandera");
            contador++;
        }

        System.out.println("secuencia persona" + empleado.getPersona().getSecuencia());
        listHVHojasDeVida = administrarHvReferencias.consultarHvHojasDeVida(empleado.getPersona().getSecuencia());
        if (listHVHojasDeVida == null) {
            System.err.println("ERROR NULO HVHOJASDEVIDA PARA LA SECUENCIA DE PERSONA :" + empleado.getPersona().getSecuencia());
        } else {
            hvHojasDeVida = listHVHojasDeVida.get(0);
            nuevoHvReferencia.setHojadevida(hvHojasDeVida);
        }
        nuevoHvReferencia.setTipo("PERSONALES");
        if (contador == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
                cargo.setFilterStyle("display: none; visibility: hidden;");
                numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
                numTelefono.setFilterStyle("display: none; visibility: hidden;");
                numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
                numCelular.setFilterStyle("display: none; visibility: hidden;");

                RequestContext.getCurrentInstance().update("form:datosHvReferencia");
                bandera = 0;
                filtrarHvReferencias = null;
                tipoLista = 0;
                tamano = 270;
            }
            System.out.println("Despues de la bandera");
            System.out.println("nueva referencia " + nuevoHvReferencia);
            k++;
            l = BigInteger.valueOf(k);
            nuevoHvReferencia.setSecuencia(l);
            crearHvReferencias.add(nuevoHvReferencia);
            if (listHvReferencias == null) {
                listHvReferencias = new ArrayList<HvReferencias>();
            }
            listHvReferencias.add(nuevoHvReferencia);
            hvReferenciaSeleccionada = nuevoHvReferencia;
            nuevoHvReferencia = new HvReferencias();
            RequestContext.getCurrentInstance().update("form:datosHvReferencia");
            modificarInfoRegistro(listHvReferencias.size());
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroHvReferencias').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaReferencia");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaReferencia').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoHvReferencias() {
        System.out.println("limpiarNuevoHvEntrevistas");
        nuevoHvReferencia = new HvReferencias();

    }

    //------------------------------------------------------------------------------
    public void duplicandoHvEntrevistas() {
        System.out.println("duplicandoHvEntrevistas");
        if (hvReferenciaSeleccionada != null) {
            duplicarHvReferencia = new HvReferencias();
            k++;
            l = BigInteger.valueOf(k);

            duplicarHvReferencia.setSecuencia(l);
            duplicarHvReferencia.setNombrepersona(hvReferenciaSeleccionada.getNombrepersona());
            duplicarHvReferencia.setCargo(hvReferenciaSeleccionada.getCargo());
            duplicarHvReferencia.setTelefono(hvReferenciaSeleccionada.getTelefono());
            duplicarHvReferencia.setCelular(hvReferenciaSeleccionada.getCelular());
            duplicarHvReferencia.setHojadevida(hvReferenciaSeleccionada.getHojadevida());
            duplicarHvReferencia.setTipo(hvReferenciaSeleccionada.getTipo());

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRRL");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroHvReferencias').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;

        if (duplicarHvReferencia.getNombrepersona().isEmpty()) {
            mensajeValidacion = "El campo Nombre es obligatorio";
        } else {
            contador++;
        }
        if (contador == 1) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarHvReferencia.setSecuencia(l);
            listHvReferencias.add(duplicarHvReferencia);
            crearHvReferencias.add(duplicarHvReferencia);
            hvReferenciaSeleccionada = duplicarHvReferencia;
            RequestContext.getCurrentInstance().update("form:datosHvReferencia");
            if (guardado == true) {
                guardado = false;
            }
            modificarInfoRegistro(listHvReferencias.size());
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
                cargo.setFilterStyle("display: none; visibility: hidden;");
                numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
                numTelefono.setFilterStyle("display: none; visibility: hidden;");
                numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
                numCelular.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosHvReferencia");
                bandera = 0;
                filtrarHvReferencias = null;
                tipoLista = 0;
                tamano = 270;
            }
            duplicarHvReferencia = new HvReferencias();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroHvReferencias').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarReferencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarReferencia').show()");
        }
    }

    public void limpiarDuplicarHvReferencias() {
        duplicarHvReferencia = new HvReferencias();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosHvReferenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "REFERENCIASPERSONALES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosHvReferenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "REFERENCIASPERSONALES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (hvReferenciaSeleccionada != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(hvReferenciaSeleccionada.getSecuencia(), "HVREFERENCIAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("HVREFERENCIAS")) { // igual acÃ¡
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlHvReferencias.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            modificarInfoRegistro(filtrarHvReferencias.size());
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlHvReferencias eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void contarRegistros() {
        if (listHvReferencias != null) {
            modificarInfoRegistro(listHvReferencias.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
    }

    public void recordarSeleccion() {
        if (hvReferenciaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosHvReferencia");
            tablaC.setSelection(hvReferenciaSeleccionada);
        }
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<HvReferencias> getListHvReferencias() {
        if (listHvReferencias == null) {
            listHvReferencias = administrarHvReferencias.consultarHvReferenciasPersonalesPorPersona(empleado.getPersona().getSecuencia());
        }
        return listHvReferencias;
    }

    public void setListHvReferencias(List<HvReferencias> listHvReferencias) {
        this.listHvReferencias = listHvReferencias;
    }

    public List<HvReferencias> getFiltrarHvReferencias() {
        return filtrarHvReferencias;
    }

    public void setFiltrarHvReferencias(List<HvReferencias> filtrarHvReferencias) {
        this.filtrarHvReferencias = filtrarHvReferencias;
    }

    public List<HvReferencias> getCrearHvReferencias() {
        return crearHvReferencias;
    }

    public void setCrearHvReferencias(List<HvReferencias> crearHvReferencias) {
        this.crearHvReferencias = crearHvReferencias;
    }

    public HvReferencias getNuevoHvReferencia() {
        return nuevoHvReferencia;
    }

    public void setNuevoHvReferencia(HvReferencias nuevoHvReferencia) {
        this.nuevoHvReferencia = nuevoHvReferencia;
    }

    public HvReferencias getDuplicarHvReferencia() {
        return duplicarHvReferencia;
    }

    public void setDuplicarHvReferencia(HvReferencias duplicarHvReferencia) {
        this.duplicarHvReferencia = duplicarHvReferencia;
    }

    public HvReferencias getEditarHvReferencia() {
        return editarHvReferencia;
    }

    public void setEditarHvReferencia(HvReferencias editarHvReferencia) {
        this.editarHvReferencia = editarHvReferencia;
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

    public BigInteger getSecuenciaPersona() {
        return secuenciaPersona;
    }

    public void setSecuenciaPersona(BigInteger secuenciaPersona) {
        this.secuenciaPersona = secuenciaPersona;
    }

    public Personas getPersonaSeleccionada() {
        return personaSeleccionada;
    }

    public void setPersonaSeleccionada(Personas personaSeleccionada) {
        this.personaSeleccionada = personaSeleccionada;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public HvReferencias getHvReferenciaSeleccionada() {
        return hvReferenciaSeleccionada;
    }

    public void setHvReferenciaSeleccionada(HvReferencias hvReferenciaSeleccionada) {
        this.hvReferenciaSeleccionada = hvReferenciaSeleccionada;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosHvReferencia");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public HVHojasDeVida getHvHojasDeVida() {
        return hvHojasDeVida;
    }

    public void setHvHojasDeVida(HVHojasDeVida hvHojasDeVida) {
        this.hvHojasDeVida = hvHojasDeVida;
    }

    public List<HVHojasDeVida> getListHVHojasDeVida() {
        return listHVHojasDeVida;
    }

    public void setListHVHojasDeVida(List<HVHojasDeVida> listHVHojasDeVida) {
        this.listHVHojasDeVida = listHVHojasDeVida;
    }

}
