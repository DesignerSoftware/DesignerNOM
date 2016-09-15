/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.TiposTelefonos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposTelefonosInterface;
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
public class ControlTiposTelefonos implements Serializable {

    @EJB
    AdministrarTiposTelefonosInterface administrarTiposTelefonos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //LISTAS
    private List<TiposTelefonos> listaTiposTelefonos;
    private List<TiposTelefonos> filtradoListaTiposTelefonos;
    private TiposTelefonos tipoTelefonoSeleccionado;

    //Crear TiposTelefonos
    private TiposTelefonos nuevoTipoTelefono;
    private List<TiposTelefonos> listaTiposTelefonosCrear;
    private BigInteger l;
    private int k;
    private String mensajeValidacion;
    //Columnas Tabla TiposTelefonos
    private Column tiposTelefonosCodigos, tiposTelefonosNombres;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Otros
    private boolean aceptar;
    private int tipoActualizacion;
    private boolean permitirIndex;
    private int tipoLista;
    private int cualCelda;
    //editar celda
    private TiposTelefonos editarTipoTelefono;
    private boolean cambioEditor, aceptarEditar;
    //Modificar tipos Telefonos
    private List<TiposTelefonos> listaTiposTelefonosModificar;
    private boolean guardado, guardarOk;
    //Borrar Tipos Telefonos
    private List<TiposTelefonos> listaTiposTelefonosBorrar;
    //DUPLICAR
    private TiposTelefonos duplicarTipoTelefono;
    private String altoTabla;
    private String paginaAnterior;
    private boolean activarLov;
    private String infoRegistro;
    private DataTable tablaC;

    public ControlTiposTelefonos() {
        listaTiposTelefonosCrear = new ArrayList<TiposTelefonos>();
        listaTiposTelefonosBorrar = new ArrayList<TiposTelefonos>();
        listaTiposTelefonosModificar = new ArrayList<TiposTelefonos>();
        permitirIndex = true;
        aceptar = true;
        tipoLista = 0;
        tipoTelefonoSeleccionado = null;
        //editar
        editarTipoTelefono = new TiposTelefonos();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        nuevoTipoTelefono = new TiposTelefonos();
        altoTabla = "270";
        guardado = true;
        activarLov = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposTelefonos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (tipoTelefonoSeleccionado != null) {
            if (tipoLista == 0) {
                editarTipoTelefono = tipoTelefonoSeleccionado;
            }
            if (tipoLista == 1) {
                editarTipoTelefono = tipoTelefonoSeleccionado;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigosTiposTelefonos");
                PrimefacesContextUI.ejecutar("PF('editarCodigosTiposTelefonos').show()");
                System.out.println("1");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombresTiposTelefonos");
                PrimefacesContextUI.ejecutar("PF('editarNombresTiposTelefonos').show()");
                System.out.println("2");
                cualCelda = -1;
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaTiposTelefonos = null;
        getListaTiposTelefonos();
        deshabilitarBotonLov();
        if (listaTiposTelefonos != null) {
            tipoTelefonoSeleccionado = listaTiposTelefonos.get(0);
        }
        contarRegistros();
    }

    public String redirigir() {
        return paginaAnterior;
    }

    //GUARDAR
    public void guardarCambiosTipoTelefono() {
        try {
            if (guardado == false) {
                System.out.println("listaTiposTelefonosBorrar: " + listaTiposTelefonosBorrar);
                System.out.println("listaTiposTelefonosModificar: " + listaTiposTelefonosModificar);
                System.out.println("listaTiposTelefonosCrear: " + listaTiposTelefonosCrear);
                if (!listaTiposTelefonosBorrar.isEmpty()) {
                    for (int i = 0; i < listaTiposTelefonosBorrar.size(); i++) {
                        administrarTiposTelefonos.borrarTipoTelefono(listaTiposTelefonosBorrar.get(i));
                    }
                    listaTiposTelefonosBorrar.clear();
                }
                if (!listaTiposTelefonosCrear.isEmpty()) {
                    for (int i = 0; i < listaTiposTelefonosCrear.size(); i++) {
                        administrarTiposTelefonos.crearTipoTelefono(listaTiposTelefonosCrear.get(i));
                    }
                }
                listaTiposTelefonosCrear.clear();
                if (!listaTiposTelefonosModificar.isEmpty()) {
                    administrarTiposTelefonos.modificarTipoTelefono(listaTiposTelefonosModificar);
                    listaTiposTelefonosModificar.clear();
                }

                listaTiposTelefonos = null;
                getListaTiposTelefonos();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                tipoTelefonoSeleccionado = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
            deshabilitarBotonLov();
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void salir() {
        if (bandera == 1) {
            System.out.println("Desactivar");
            FacesContext c = FacesContext.getCurrentInstance();
            tiposTelefonosCodigos = (Column) c.getViewRoot().findComponent("form:datosTiposTelefonos:tiposTelefonosCodigos");
            tiposTelefonosCodigos.setFilterStyle("display: none; visibility: hidden;");
            tiposTelefonosNombres = (Column) c.getViewRoot().findComponent("form:datosTiposTelefonos:tiposTelefonosNombres");
            tiposTelefonosNombres.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtradoListaTiposTelefonos = null;
            tipoLista = 0;
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
            tipoLista = 0;

        }

        listaTiposTelefonosBorrar.clear();
        listaTiposTelefonosCrear.clear();
        listaTiposTelefonosModificar.clear();
        tipoTelefonoSeleccionado = null;
        contarRegistros();
        k = 0;
        listaTiposTelefonos = null;
        guardado = true;
        permitirIndex = true;
        guardado = true;

    }

    //CREAR TIPO TELEFONO
    public void agregarNuevoTipoTelefono() {
        int pasa = 0;
        int pasaA = 0;
        mensajeValidacion = "";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoTipoTelefono.getNombre().equals(" ") || nuevoTipoTelefono.getNombre().equals("")) {
            mensajeValidacion = mensajeValidacion + " * Nombre de Tipo de Telefono \n";
            pasa++;
        }
        if (nuevoTipoTelefono.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + " * Codigo \n";
            pasa++;
        }
        System.out.println("Lista Tipos Telefonos tiene: " + listaTiposTelefonos.size());

        for (int i = 0; i < listaTiposTelefonos.size(); i++) {
            System.out.println("Nombres: " + listaTiposTelefonos.get(i).getNombre());

            if (listaTiposTelefonos.get(i).getNombre().equals(nuevoTipoTelefono.getNombre())) {
                System.out.println("Entro al IF Tipo Telefono");
                RequestContext.getCurrentInstance().update("formularioDialogos:existeNombre");
                PrimefacesContextUI.ejecutar("PF('existeNombre').show()");
                pasaA++;
            }
            if (pasa != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoTelefono");
                PrimefacesContextUI.ejecutar("PF('validacionNuevoTipoTelefono').show()");

            }
        }

        for (int i = 0; i < listaTiposTelefonos.size(); i++) {
            System.out.println("Codigos: " + listaTiposTelefonos.get(i).getCodigo());
            if (listaTiposTelefonos.get(i).getCodigo().compareTo(nuevoTipoTelefono.getCodigo()) == 0) {
                System.out.println("Entro al IF Tipo Telefono");
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                PrimefacesContextUI.ejecutar("PF('existeCodigo').show()");
                pasaA++;
            }
            if (pasa != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoTelefono");
                PrimefacesContextUI.ejecutar("PF('validacionNuevoTipoTelefono').show()");

            }
        }

        if (nuevoTipoTelefono.getNombre().length() > 20) {
            RequestContext.getCurrentInstance().update("formularioDialogos:sobrepasaCaracteres");
            PrimefacesContextUI.ejecutar("PF('sobrepasaCaracteres').show()");
            pasa++;
        }

        if (pasa == 0 && pasaA == 0) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                FacesContext c = FacesContext.getCurrentInstance();
                tiposTelefonosCodigos = (Column) c.getViewRoot().findComponent("form:datosTiposTelefonos:tiposTelefonosCodigos");
                tiposTelefonosCodigos.setFilterStyle("display: none; visibility: hidden;");
                tiposTelefonosNombres = (Column) c.getViewRoot().findComponent("form:datosTiposTelefonos:tiposTelefonosNombres");
                tiposTelefonosNombres.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtradoListaTiposTelefonos = null;
                tipoLista = 0;
                altoTabla = "270";
                RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA CIUDADES.
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoTelefono.setSecuencia(l);
            listaTiposTelefonosCrear.add(nuevoTipoTelefono);
            listaTiposTelefonos.add(nuevoTipoTelefono);
            modificarInfoRegistro(listaTiposTelefonos.size());
            tipoTelefonoSeleccionado = nuevoTipoTelefono;
            nuevoTipoTelefono = new TiposTelefonos();

            RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroTipoTelefono').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoTelefono");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoTipoTelefono').show()");
        }
    }
    //LIMPIAR NUEVO REGISTRO TIPO TELEFONO

    public void limpiarNuevoTipoTelefono() {
        nuevoTipoTelefono = new TiposTelefonos();
    }

    //FILTRADO
    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        if (bandera == 0) {
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            FacesContext c = FacesContext.getCurrentInstance();
            tiposTelefonosCodigos = (Column) c.getViewRoot().findComponent("form:datosTiposTelefonos:tiposTelefonosCodigos");
            tiposTelefonosCodigos.setFilterStyle("width: 85%;");
            tiposTelefonosNombres = (Column) c.getViewRoot().findComponent("form:datosTiposTelefonos:tiposTelefonosNombres");
            tiposTelefonosNombres.setFilterStyle("width: 85%;");
            altoTabla = "250";
            RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            FacesContext c = FacesContext.getCurrentInstance();
            tiposTelefonosCodigos = (Column) c.getViewRoot().findComponent("form:datosTiposTelefonos:tiposTelefonosCodigos");
            tiposTelefonosCodigos.setFilterStyle("display: none; visibility: hidden;");
            tiposTelefonosNombres = (Column) c.getViewRoot().findComponent("form:datosTiposTelefonos:tiposTelefonosNombres");
            tiposTelefonosNombres.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
            bandera = 0;
            filtradoListaTiposTelefonos = null;
            tipoLista = 0;
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposTelefonosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TiposTelefonosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposTelefonosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TiposTelefonosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    //LIMPIAR NUEVO REGISTRO DE TIPO DE TELEFONO
    public void limpiarNuevoTiposTelefonos() {
        nuevoTipoTelefono = new TiposTelefonos();
    }

    //BORRAR TIPO DE TELEFONO
    public void borrarTiposTelefonos() {

        if (tipoTelefonoSeleccionado != null) {

            if (!listaTiposTelefonosModificar.isEmpty() && listaTiposTelefonosModificar.contains(tipoTelefonoSeleccionado)) {
                listaTiposTelefonosModificar.remove(listaTiposTelefonosModificar.indexOf(tipoTelefonoSeleccionado));
                listaTiposTelefonosBorrar.add(tipoTelefonoSeleccionado);
            } else if (!listaTiposTelefonosCrear.isEmpty() && listaTiposTelefonosCrear.contains(tipoTelefonoSeleccionado)) {
                listaTiposTelefonosCrear.remove(listaTiposTelefonosCrear.indexOf(tipoTelefonoSeleccionado));
            } else {
                listaTiposTelefonosBorrar.add(tipoTelefonoSeleccionado);
            }
            listaTiposTelefonos.remove(tipoTelefonoSeleccionado);

            if (tipoLista == 1) {
                filtradoListaTiposTelefonos.remove(tipoTelefonoSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
            modificarInfoRegistro(listaTiposTelefonos.size());
            tipoTelefonoSeleccionado = null;
            guardado = true;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    //UBICACION CELDA
    public void cambiarIndice(TiposTelefonos tiposTelefonos, int celda) {
        if (permitirIndex == true) {
            tipoTelefonoSeleccionado = tiposTelefonos;
            cualCelda = celda;
            if (tipoLista == 0) {
                tipoTelefonoSeleccionado.getSecuencia();

            } else {
                tipoTelefonoSeleccionado.getSecuencia();

            }
        }
    }

    //DUPLICAR TIPO TELEFONO
    public void duplicarTT() {
        if (tipoTelefonoSeleccionado != null) {
            duplicarTipoTelefono = new TiposTelefonos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTipoTelefono.setSecuencia(l);
                duplicarTipoTelefono.setCodigo(tipoTelefonoSeleccionado.getCodigo());
                duplicarTipoTelefono.setNombre(tipoTelefonoSeleccionado.getNombre());
            }
            if (tipoLista == 1) {
                duplicarTipoTelefono.setSecuencia(l);
                duplicarTipoTelefono.setCodigo(tipoTelefonoSeleccionado.getCodigo());
                duplicarTipoTelefono.setNombre(tipoTelefonoSeleccionado.getNombre());
                altoTabla = "270";
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoTelefono");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroTipoTelefono').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    //LIMPIAR DUPLICAR
    public void limpiarduplicarTipoTelefono() {
        duplicarTipoTelefono = new TiposTelefonos();
    }

    public void confirmarDuplicar() {

        RequestContext context = RequestContext.getCurrentInstance();
        int pasa = 0;

        for (int i = 0; i < listaTiposTelefonos.size(); i++) {
            if (duplicarTipoTelefono.getNombre().equals(listaTiposTelefonos.get(i).getNombre())) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeNombre");
                PrimefacesContextUI.ejecutar("PF('existeNombre').show()");
                pasa++;
            }
            if (duplicarTipoTelefono.getCodigo().compareTo(listaTiposTelefonos.get(i).getCodigo()) == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                PrimefacesContextUI.ejecutar("PF('existeCodigo').show()");
                pasa++;
            }
        }

        if (pasa == 0) {

            listaTiposTelefonos.add(duplicarTipoTelefono);
            listaTiposTelefonosCrear.add(duplicarTipoTelefono);
            tipoTelefonoSeleccionado = duplicarTipoTelefono;
            modificarInfoRegistro(listaTiposTelefonos.size());
            RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                System.out.println("Desactivar");
                FacesContext c = FacesContext.getCurrentInstance();
                tiposTelefonosCodigos = (Column) c.getViewRoot().findComponent("form:datosTiposTelefonos:tiposTelefonosCodigos");
                tiposTelefonosCodigos.setFilterStyle("display: none; visibility: hidden;");
                tiposTelefonosNombres = (Column) c.getViewRoot().findComponent("form:datosTiposTelefonos:tiposTelefonosNombres");
                tiposTelefonosNombres.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
                bandera = 0;
                filtradoListaTiposTelefonos = null;
                tipoLista = 0;
                RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
                tipoLista = 0;
            }
            duplicarTipoTelefono = new TiposTelefonos();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTipoTelefono");
        PrimefacesContextUI.ejecutar("PF('DuplicarRegistroTipoTelefono').hide()");
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            tiposTelefonosCodigos = (Column) c.getViewRoot().findComponent("form:datosTiposTelefonos:tiposTelefonosCodigos");
            tiposTelefonosCodigos.setFilterStyle("display: none; visibility: hidden;");
            tiposTelefonosNombres = (Column) c.getViewRoot().findComponent("form:datosTiposTelefonos:tiposTelefonosNombres");
            tiposTelefonosNombres.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtradoListaTiposTelefonos = null;
            tipoLista = 0;
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
            tipoLista = 0;

        }

        listaTiposTelefonosBorrar.clear();
        listaTiposTelefonosCrear.clear();
        listaTiposTelefonosModificar.clear();
        contarRegistros();
        tipoTelefonoSeleccionado = null;
        k = 0;
        listaTiposTelefonos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
    }

    //AUTOCOMPLETAR
    public void modificarTiposTelefonos(TiposTelefonos tiposTelefonos, String confirmarCambio, String valorConfirmar) {
        tipoTelefonoSeleccionado = tiposTelefonos;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaTiposTelefonosCrear.contains(tipoTelefonoSeleccionado)) {

                    if (listaTiposTelefonosModificar.isEmpty()) {
                        listaTiposTelefonosModificar.add(tipoTelefonoSeleccionado);
                    } else if (!listaTiposTelefonosModificar.contains(tipoTelefonoSeleccionado)) {
                        listaTiposTelefonosModificar.add(tipoTelefonoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
            } else if (!listaTiposTelefonosCrear.contains(tipoTelefonoSeleccionado)) {

                if (listaTiposTelefonosModificar.isEmpty()) {
                    listaTiposTelefonosModificar.add(tipoTelefonoSeleccionado);
                } else if (!listaTiposTelefonosModificar.contains(tipoTelefonoSeleccionado)) {
                    listaTiposTelefonosModificar.add(tipoTelefonoSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            }
            RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
        }

    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (tipoTelefonoSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(tipoTelefonoSeleccionado.getSecuencia(), "TIPOSTELEFONOS");
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSTELEFONOS")) {
            PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
        }
    }

    public void recordarSeleccionTT() {
        if (tipoTelefonoSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosTiposTelefonos");
            tablaC.setSelection(tipoTelefonoSeleccionado);
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        deshabilitarBotonLov();
        tipoTelefonoSeleccionado = null;
        modificarInfoRegistro(filtradoListaTiposTelefonos.size());
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void contarRegistros() {
        if (listaTiposTelefonos != null) {
            modificarInfoRegistro(listaTiposTelefonos.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
    }

    //GETTER AND SETTERS
    public List<TiposTelefonos> getListaTiposTelefonos() {
        if (listaTiposTelefonos == null) {
            listaTiposTelefonos = administrarTiposTelefonos.tiposTelefonos();
            return listaTiposTelefonos;
        } else {
            return listaTiposTelefonos;
        }
    }

    public void setListaTiposTelefonos(List<TiposTelefonos> listaTiposTelefonos) {
        this.listaTiposTelefonos = listaTiposTelefonos;
    }

    public List<TiposTelefonos> getFiltradoListaTiposTelefonos() {
        return filtradoListaTiposTelefonos;
    }

    public void setFiltradoListaTiposTelefonos(List<TiposTelefonos> filtradoListaTiposTelefonos) {
        this.filtradoListaTiposTelefonos = filtradoListaTiposTelefonos;
    }

    public TiposTelefonos getNuevoTipoTelefono() {
        return nuevoTipoTelefono;
    }

    public void setNuevoTipoTelefono(TiposTelefonos nuevoTipoTelefono) {
        this.nuevoTipoTelefono = nuevoTipoTelefono;
    }

    public TiposTelefonos getEditarTipoTelefono() {
        return editarTipoTelefono;
    }

    public void setEditarTipoTelefono(TiposTelefonos editarTipoTelefono) {
        this.editarTipoTelefono = editarTipoTelefono;
    }

    public TiposTelefonos getDuplicarTipoTelefono() {
        return duplicarTipoTelefono;
    }

    public void setDuplicarTipoTelefono(TiposTelefonos duplicarTipoTelefono) {
        this.duplicarTipoTelefono = duplicarTipoTelefono;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public TiposTelefonos getTipoTelefonoSeleccionado() {
        return tipoTelefonoSeleccionado;
    }

    public void setTipoTelefonoSeleccionado(TiposTelefonos tipoTelefonoSeleccionado) {
        this.tipoTelefonoSeleccionado = tipoTelefonoSeleccionado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
