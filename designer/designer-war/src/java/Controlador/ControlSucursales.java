/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Ciudades;
import Entidades.Sucursales;
import Entidades.Bancos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarSucursalesInterface;
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
public class ControlSucursales implements Serializable {

    @EJB
    AdministrarSucursalesInterface administrarSucursales;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Sucursales> listSucursales;
    private List<Sucursales> filtrarSucursales;
    private List<Sucursales> crearSucursales;
    private List<Sucursales> modificarSucursales;
    private List<Sucursales> borrarSucursales;
    private Sucursales nuevoSucursales;
    private Sucursales duplicarSucursales;
    private Sucursales editarSucursales;
    private Sucursales sucursalSeleccionada;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    private boolean activarLov;
    //RASTRO
    private Column codigo, descripcion, banco, ciudad;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private List<Bancos> listaBancos;
    private List<Bancos> filtradoBancos;
    private Bancos bancoSeleccionado;
    private List<Ciudades> listaCiudades;
    private List<Ciudades> filtradoCiudades;
    private Ciudades ciudadSeleccionado;
    private String infoRegistro;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlSucursales() {
        listSucursales = null;
        crearSucursales = new ArrayList<Sucursales>();
        modificarSucursales = new ArrayList<Sucursales>();
        borrarSucursales = new ArrayList<Sucursales>();
        permitirIndex = true;
        editarSucursales = new Sucursales();
        nuevoSucursales = new Sucursales();
        nuevoSucursales.setBanco(new Bancos());
        nuevoSucursales.setCiudad(new Ciudades());
        duplicarSucursales = new Sucursales();
        duplicarSucursales.setBanco(new Bancos());
        duplicarSucursales.setCiudad(new Ciudades());
        listaBancos = null;
        filtradoBancos = null;
        listaCiudades = null;
        filtradoCiudades = null;
        guardado = true;
        tamano = 270;
        aceptar = true;
        activarLov = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSucursales.obtenerConexion(ses.getId());
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
            String pagActual = "sucursal";
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
        limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

    public String retornarPagina() {
        return paginaAnterior;
    }

    public void cambiarIndice(Sucursales sucursal, int celda) {
        if (permitirIndex == true) {
            sucursalSeleccionada = sucursal;
            cualCelda = celda;
            sucursalSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                deshabilitarLov();
                sucursalSeleccionada.getCodigo();
            }
            if (cualCelda == 1) {
                deshabilitarLov();
                sucursalSeleccionada.getNombre();
            }
            if (cualCelda == 2) {
                habilitarLov();
                sucursalSeleccionada.getBanco().getNombre();
            }
            if (cualCelda == 3) {
                habilitarLov();
                sucursalSeleccionada.getCiudad().getNombre();
            }
        }
    }

    public void asignarIndex(Sucursales sucursal, int LND, int dig) {
        sucursalSeleccionada = sucursal;
        tipoActualizacion = LND;
        if (dig == 2) {
            contarRegistrosBancos();
            RequestContext.getCurrentInstance().update("form:bancosDialogo");
            RequestContext.getCurrentInstance().execute("PF('bancosDialogo').show()");
            dig = -1;
        }
        if (dig == 3) {
            contarRegistrosCiudades();
            RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
            dig = -1;
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        if (sucursalSeleccionada != null) {

            if (cualCelda == 2) {
                contarRegistrosBancos();
                RequestContext.getCurrentInstance().update("form:bancosDialogo");
                RequestContext.getCurrentInstance().execute("PF('bancosDialogo').show()");
            }
            if (cualCelda == 3) {
                contarRegistrosCiudades();
                RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
            }
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosSucursales:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSucursales:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            banco = (Column) c.getViewRoot().findComponent("form:datosSucursales:banco");
            banco.setFilterStyle("display: none; visibility: hidden;");
            ciudad = (Column) c.getViewRoot().findComponent("form:datosSucursales:ciudad");
            ciudad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            bandera = 0;
            filtrarSucursales = null;
            tipoLista = 0;
        }

        borrarSucursales.clear();
        crearSucursales.clear();
        modificarSucursales.clear();
        sucursalSeleccionada = null;
        k = 0;
        listSucursales = null;
        guardado = true;
        permitirIndex = true;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosSucursales");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {  limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosSucursales:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSucursales:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            banco = (Column) c.getViewRoot().findComponent("form:datosSucursales:banco");
            banco.setFilterStyle("display: none; visibility: hidden;");
            ciudad = (Column) c.getViewRoot().findComponent("form:datosSucursales:ciudad");
            ciudad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            bandera = 0;
            filtrarSucursales = null;
            tipoLista = 0;
        }
        borrarSucursales.clear();
        crearSucursales.clear();
        modificarSucursales.clear();
        sucursalSeleccionada = null;
        k = 0;
        listSucursales = null;
        guardado = true;
        permitirIndex = true;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosSucursales");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSucursales:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSucursales:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            banco = (Column) c.getViewRoot().findComponent("form:datosSucursales:banco");
            banco.setFilterStyle("width: 85% !important;");
            ciudad = (Column) c.getViewRoot().findComponent("form:datosSucursales:ciudad");
            ciudad.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSucursales:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSucursales:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            banco = (Column) c.getViewRoot().findComponent("form:datosSucursales:banco");
            banco.setFilterStyle("display: none; visibility: hidden;");
            ciudad = (Column) c.getViewRoot().findComponent("form:datosSucursales:ciudad");
            ciudad.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosSucursales");
            bandera = 0;
            filtrarSucursales = null;
            tipoLista = 0;
        }
    }

    public void actualizarBancos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            sucursalSeleccionada.setBanco(bancoSeleccionado);
            if (!crearSucursales.contains(sucursalSeleccionada)) {
                if (modificarSucursales.isEmpty()) {
                    modificarSucursales.add(sucursalSeleccionada);
                } else if (!modificarSucursales.contains(sucursalSeleccionada)) {
                    modificarSucursales.add(sucursalSeleccionada);
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            nuevoSucursales.setBanco(bancoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
        } else if (tipoActualizacion == 2) {
            duplicarSucursales.setBanco(bancoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
        }
        filtradoBancos = null;
        bancoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext.getCurrentInstance().update("form:bancosDialogo");
        RequestContext.getCurrentInstance().update("form:lovBancos");
        RequestContext.getCurrentInstance().update("form:aceptarPer");
        context.reset("form:lovBancos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBancos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('bancosDialogo').hide()");
    }

    public void actualizarCiudades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            sucursalSeleccionada.setCiudad(ciudadSeleccionado);
            if (!crearSucursales.contains(sucursalSeleccionada)) {
                if (modificarSucursales.isEmpty()) {
                    modificarSucursales.add(sucursalSeleccionada);
                } else if (!modificarSucursales.contains(sucursalSeleccionada)) {
                    modificarSucursales.add(sucursalSeleccionada);
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            nuevoSucursales.setCiudad(ciudadSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
        } else if (tipoActualizacion == 2) {
            duplicarSucursales.setCiudad(ciudadSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
        }
        filtradoBancos = null;
        bancoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
        RequestContext.getCurrentInstance().update("form:lovCiudades");
        RequestContext.getCurrentInstance().update("form:aceptarCar");
        context.reset("form:lovCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').hide()");
    }

    public void cancelarCambioBancos() {
        filtradoBancos = null;
        bancoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:bancosDialogo");
        RequestContext.getCurrentInstance().update("form:lovBancos");
        RequestContext.getCurrentInstance().update("form:aceptarPer");
        context.reset("form:lovBancos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBancos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('bancosDialogo').hide()");
    }

    public void cancelarCambioCiudades() {
        filtradoCiudades = null;
        ciudadSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
        RequestContext.getCurrentInstance().update("form:lovCiudades");
        RequestContext.getCurrentInstance().update("form:aceptarCar");
        context.reset("form:lovCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').hide()");
    }

    public void modificarSucursales(Sucursales sucursal) {
        sucursalSeleccionada = sucursal;
        if (!crearSucursales.contains(sucursalSeleccionada)) {
            if (modificarSucursales.isEmpty()) {
                modificarSucursales.add(sucursalSeleccionada);
            } else if (!modificarSucursales.contains(sucursalSeleccionada)) {
                modificarSucursales.add(sucursalSeleccionada);
            }
            if (guardado == true) {
                guardado = false;
            }
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosSucursales");
    }

    public void verificarBorrado() {
        BigInteger vigenciasFormasPagosSucursal;
        if (sucursalSeleccionada != null) {

            vigenciasFormasPagosSucursal = administrarSucursales.contarVigenciasFormasPagosSucursal(sucursalSeleccionada.getSecuencia());
            if (vigenciasFormasPagosSucursal.equals(new BigInteger("0"))) {
                borrandoSucursales();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                vigenciasFormasPagosSucursal = new BigInteger("-1");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrandoSucursales() {

        if (sucursalSeleccionada != null) {
            if (!modificarSucursales.isEmpty() && modificarSucursales.contains(sucursalSeleccionada)) {
                int modIndex = modificarSucursales.indexOf(sucursalSeleccionada);
                modificarSucursales.remove(modIndex);
                borrarSucursales.add(sucursalSeleccionada);
            } else if (!crearSucursales.isEmpty() && crearSucursales.contains(sucursalSeleccionada)) {
                int crearIndex = crearSucursales.indexOf(sucursalSeleccionada);
                crearSucursales.remove(crearIndex);
            } else {
                borrarSucursales.add(sucursalSeleccionada);
            }
            listSucursales.remove(sucursalSeleccionada);
            if (tipoLista == 1) {
                filtrarSucursales.remove(sucursalSeleccionada);

            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            sucursalSeleccionada = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void revisarDialogoGuardar() {
        if (!borrarSucursales.isEmpty() || !crearSucursales.isEmpty() || !modificarSucursales.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarSucursales() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (guardado == false) {
                System.out.println("Realizando guardarSucursales");
                if (!borrarSucursales.isEmpty()) {
                    administrarSucursales.borrarSucursales(borrarSucursales);
                    registrosBorrados = borrarSucursales.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarSucursales.clear();
                }
                if (!modificarSucursales.isEmpty()) {
                    administrarSucursales.modificarSucursales(modificarSucursales);
                    modificarSucursales.clear();
                }
                if (!crearSucursales.isEmpty()) {
                    administrarSucursales.crearSucursales(crearSucursales);
                    crearSucursales.clear();
                }
                listSucursales = null;
                sucursalSeleccionada = null;
                k = 0;
                guardado = true;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            System.out.println("error en el guardado de sucursales : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Hubo un error en el guardaro. Por favor intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void editarCelda() {
        if (sucursalSeleccionada != null) {
            editarSucursales = sucursalSeleccionada;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigoS");
                RequestContext.getCurrentInstance().execute("PF('editCodigoS').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNombreS");
                RequestContext.getCurrentInstance().execute("PF('editNombreS').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editBancos");
                RequestContext.getCurrentInstance().execute("PF('editBancos').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCiudades");
                RequestContext.getCurrentInstance().execute("PF('editCiudades').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoSucursales() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoSucursales.getCodigo() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listSucursales.size(); x++) {
                if (listSucursales.get(x).getCodigo().equals(nuevoSucursales.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoSucursales.getNombre() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (nuevoSucursales.getBanco().getNombre() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            System.out.println("bandera");
            contador++;
        }
        if (nuevoSucursales.getCiudad().getNombre() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";

        } else {
            contador++;
        }
        if (contador == 4) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosSucursales:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSucursales:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                banco = (Column) c.getViewRoot().findComponent("form:datosSucursales:banco");
                banco.setFilterStyle("display: none; visibility: hidden;");
                ciudad = (Column) c.getViewRoot().findComponent("form:datosSucursales:ciudad");
                ciudad.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarSucursales = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoSucursales.setSecuencia(l);
            crearSucursales.add(nuevoSucursales);
            listSucursales.add(nuevoSucursales);
            sucursalSeleccionada = nuevoSucursales;
            nuevoSucursales = new Sucursales();
            nuevoSucursales.setCiudad(new Ciudades());
            nuevoSucursales.setBanco(new Bancos());
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            contarRegistros();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroSucursales').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaSucursal");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaSucursal').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoSucursales() {
        nuevoSucursales = new Sucursales();
        nuevoSucursales.setBanco(new Bancos());
        nuevoSucursales.setCiudad(new Ciudades());
    }

    public void cargarNuevoSucursales() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('nuevoRegistroSucursales').show()");
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSucursal");

    }

    public void duplicandoSucursales() {
        if (sucursalSeleccionada != null) {
            duplicarSucursales = new Sucursales();
            duplicarSucursales.setBanco(new Bancos());
            duplicarSucursales.setCiudad(new Ciudades());
            k++;
            l = BigInteger.valueOf(k);
            duplicarSucursales.setSecuencia(l);
            duplicarSucursales.setCodigo(sucursalSeleccionada.getCodigo());
            duplicarSucursales.setNombre(sucursalSeleccionada.getNombre());
            duplicarSucursales.setBanco(sucursalSeleccionada.getBanco());
            duplicarSucursales.setCiudad(sucursalSeleccionada.getCiudad());

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSucursales').show()");
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
        if (duplicarSucursales.getCodigo() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listSucursales.size(); x++) {
                if (listSucursales.get(x).getCodigo().equals(duplicarSucursales.getCodigo())) {
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

        if (duplicarSucursales.getNombre() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (duplicarSucursales.getBanco().getNombre() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (duplicarSucursales.getCiudad().getNombre() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 4) {
            listSucursales.add(duplicarSucursales);
            crearSucursales.add(duplicarSucursales);
            sucursalSeleccionada = duplicarSucursales;
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            contarRegistros();
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosSucursales:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSucursales:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                banco = (Column) c.getViewRoot().findComponent("form:datosSucursales:banco");
                banco.setFilterStyle("display: none; visibility: hidden;");
                ciudad = (Column) c.getViewRoot().findComponent("form:datosSucursales:ciudad");
                ciudad.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSucursales");
                bandera = 0;
                filtrarSucursales = null;
                tipoLista = 0;
            }
            duplicarSucursales = new Sucursales();
            duplicarSucursales.setCiudad(new Ciudades());
            duplicarSucursales.setBanco(new Bancos());
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSucursales').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarSucursales() {
        duplicarSucursales = new Sucursales();
        duplicarSucursales.setBanco(new Bancos());
        duplicarSucursales.setCiudad(new Ciudades());
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSucursalesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SUCURSALES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSucursalesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SUCURSALES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (sucursalSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(sucursalSeleccionada.getSecuencia(), "SUCURSALES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("SUCURSALES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlSucursales.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR ControlSucursales eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosBancos() {
        RequestContext.getCurrentInstance().update("form:infoRegistroBancos");
    }

    public void contarRegistrosCiudades() {
        RequestContext.getCurrentInstance().update("form:infoRegistroCiudades");
    }

    public void habilitarLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Sucursales> getListSucursales() {
        if (listSucursales == null) {
            listSucursales = administrarSucursales.consultarSucursales();
        }
        return listSucursales;
    }

    public void setListSucursales(List<Sucursales> listSucursales) {
        this.listSucursales = listSucursales;
    }

    public List<Sucursales> getFiltrarSucursales() {
        return filtrarSucursales;
    }

    public void setFiltrarSucursales(List<Sucursales> filtrarSucursales) {
        this.filtrarSucursales = filtrarSucursales;
    }

    public Sucursales getNuevoSucursales() {
        return nuevoSucursales;
    }

    public void setNuevoSucursales(Sucursales nuevoSucursales) {
        this.nuevoSucursales = nuevoSucursales;
    }

    public Sucursales getDuplicarSucursales() {
        return duplicarSucursales;
    }

    public void setDuplicarSucursales(Sucursales duplicarSucursales) {
        this.duplicarSucursales = duplicarSucursales;
    }

    public Sucursales getEditarSucursales() {
        return editarSucursales;
    }

    public void setEditarSucursales(Sucursales editarSucursales) {
        this.editarSucursales = editarSucursales;
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
    private String infoRegistroBancos;

    public List<Bancos> getListaBancos() {
        if (listaBancos == null) {
            listaBancos = administrarSucursales.consultarLOVBancos();
        }
        return listaBancos;
    }

    public void setListaBancos(List<Bancos> listaBancos) {
        this.listaBancos = listaBancos;
    }

    public List<Bancos> getFiltradoBancos() {
        return filtradoBancos;
    }

    public void setFiltradoBancos(List<Bancos> filtradoBancos) {
        this.filtradoBancos = filtradoBancos;
    }

    public Bancos getBancoSeleccionado() {
        return bancoSeleccionado;
    }

    public void setBancoSeleccionado(Bancos bancoSeleccionado) {
        this.bancoSeleccionado = bancoSeleccionado;
    }
    private String infoRegistroCiudades;

    public List<Ciudades> getListaCiudades() {
        if (listaCiudades == null) {
            listaCiudades = administrarSucursales.consultarLOVCiudades();
        }
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudades> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public List<Ciudades> getFiltradoCiudades() {
        return filtradoCiudades;
    }

    public void setFiltradoCiudades(List<Ciudades> filtradoCiudades) {
        this.filtradoCiudades = filtradoCiudades;
    }

    public Ciudades getCiudadSeleccionado() {
        return ciudadSeleccionado;
    }

    public void setCiudadSeleccionado(Ciudades ciudadSeleccionado) {
        this.ciudadSeleccionado = ciudadSeleccionado;
    }

    public Sucursales getSucursalSeleccionada() {
        return sucursalSeleccionada;
    }

    public void setSucursalSeleccionada(Sucursales sucursalSeleccionada) {
        this.sucursalSeleccionada = sucursalSeleccionada;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosSucursales");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroBancos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovBancos");
        infoRegistroBancos = String.valueOf(tabla.getRowCount());
        return infoRegistroBancos;
    }

    public void setInfoRegistroBancos(String infoRegistroBancos) {
        this.infoRegistroBancos = infoRegistroBancos;
    }

    public String getInfoRegistroCiudades() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCiudades");
        infoRegistroCiudades = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudades;
    }

    public void setInfoRegistroCiudades(String infoRegistroCiudades) {
        this.infoRegistroCiudades = infoRegistroCiudades;
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

}
