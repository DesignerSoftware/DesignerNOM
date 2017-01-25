/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposEducaciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposEducacionesInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
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
@Named(value = "controlTiposEducaciones")
@SessionScoped
public class ControlTiposEducaciones implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarTiposEducacionesInterface administrarTiposEducaciones;

    private List<TiposEducaciones> listaTiposEducaciones;
    private List<TiposEducaciones> filtradoListaTiposEducaciones;
    private List<TiposEducaciones> listaTiposEducacionesCrear;
    private List<TiposEducaciones> listaTiposEducacionesBorrar;
    private List<TiposEducaciones> listaTiposEducacionesModificar;
    private TiposEducaciones tipoEducacionSeleccionado;
    private TiposEducaciones nuevoTipoEducacion;
    private TiposEducaciones duplicarTipoEducacion;
    private TiposEducaciones editarTipoEducacion;
    private BigInteger l;
    private int k, bandera, tipoLista, cualCelda;
    private Column codigo, nombre, nivelEducativo;
    private boolean aceptar, permitirIndex, guardado, activarLov;
    private String altoTabla, inforegistro, mensajeValidacion;
    private DataTable tablaC;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTiposEducaciones() {
        listaTiposEducacionesCrear = new ArrayList<TiposEducaciones>();
        listaTiposEducacionesBorrar = new ArrayList<TiposEducaciones>();
        listaTiposEducacionesModificar = new ArrayList<TiposEducaciones>();
        permitirIndex = true;
        aceptar = true;
        tipoLista = 0;
        tipoEducacionSeleccionado = null;
        editarTipoEducacion = new TiposEducaciones();
        nuevoTipoEducacion = new TiposEducaciones();
        duplicarTipoEducacion = new TiposEducaciones();
        cualCelda = -1;
        altoTabla = "270";
        guardado = true;
        activarLov = true;
        listaTiposEducaciones = null;
        mapParametros.put("paginaAnterior", paginaAnterior);

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarRastros.obtenerConexion(ses.getId());
            administrarTiposEducaciones.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
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
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina();
        } else {
            String pagActual = "tipoeducacion";
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

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaTiposEducaciones = null;
        getListaTiposEducaciones();
        deshabilitarBotonLov();
        if (listaTiposEducaciones != null) {
            tipoEducacionSeleccionado = listaTiposEducaciones.get(0);
        }
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void editarCelda() {
        if (tipoEducacionSeleccionado != null) {
            editarTipoEducacion = tipoEducacionSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigosTiposEducaciones");
                RequestContext.getCurrentInstance().execute("PF('editarCodigosTiposEducaciones').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombresTiposEducaciones");
                RequestContext.getCurrentInstance().execute("PF('editarNombresTiposEducaciones').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNivelTiposEducaciones");
                RequestContext.getCurrentInstance().execute("PF('editarNivelTiposEducaciones').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void guardarCambiosTipoEducacion() {
        try {
            if (guardado == false) {
                if (!listaTiposEducacionesBorrar.isEmpty()) {
                    administrarTiposEducaciones.borrar(listaTiposEducacionesBorrar);
                    listaTiposEducacionesBorrar.clear();
                }
                if (!listaTiposEducacionesCrear.isEmpty()) {
                    administrarTiposEducaciones.crear(listaTiposEducacionesCrear);
                    listaTiposEducacionesCrear.clear();
                }
                if (!listaTiposEducacionesModificar.isEmpty()) {
                    administrarTiposEducaciones.editar(listaTiposEducacionesModificar);
                    listaTiposEducacionesModificar.clear();
                }

                listaTiposEducaciones = null;
                getListaTiposEducaciones();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                tipoEducacionSeleccionado = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTiposEducaciones");
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesCodigos");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesNombres");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            nivelEducativo = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesNivel");
            nivelEducativo.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtradoListaTiposEducaciones = null;
            tipoLista = 0;
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosTiposEducaciones");

        }

        listaTiposEducacionesBorrar.clear();
        listaTiposEducacionesCrear.clear();
        listaTiposEducacionesModificar.clear();
        tipoEducacionSeleccionado = null;
        contarRegistros();
        k = 0;
        listaTiposEducaciones = null;
        guardado = true;
        permitirIndex = true;

    }

    public void agregarNuevoTipoEducacion() {
        int pasa = 0;
        int pasaA = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";
        if (nuevoTipoEducacion.getNombre().equals(" ") || nuevoTipoEducacion.getNombre().equals("")) {
            mensajeValidacion = mensajeValidacion + " * Nombre de Tipo de Telefono \n";
            pasa++;
        }
        if (nuevoTipoEducacion.getCodigo() == 0) {
            mensajeValidacion = mensajeValidacion + " * Codigo \n";
            pasa++;
        }

        for (int i = 0; i < listaTiposEducaciones.size(); i++) {
            System.out.println("Nombres: " + listaTiposEducaciones.get(i).getNombre());

            if (listaTiposEducaciones.get(i).getNombre().equals(nuevoTipoEducacion.getNombre())) {
                System.out.println("Entro al IF Tipo Telefono");
                RequestContext.getCurrentInstance().update("formularioDialogos:existeNombre");
                RequestContext.getCurrentInstance().execute("PF('existeNombre').show()");
                pasaA++;
            }
            if (pasa != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoEducacion");
                RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoTelefono').show()");

            }
        }

        for (int i = 0; i < listaTiposEducaciones.size(); i++) {
            System.out.println("Codigos: " + listaTiposEducaciones.get(i).getCodigo());
            if (listaTiposEducaciones.get(i).getCodigo() == nuevoTipoEducacion.getCodigo()) {
                System.out.println("Entro al IF Tipo Telefono");
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                pasaA++;
            }
            if (pasa != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoTelefono");
                RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoTelefono').show()");

            }
        }

        if (nuevoTipoEducacion.getNombre().length() > 20) {
            RequestContext.getCurrentInstance().update("formularioDialogos:sobrepasaCaracteres");
            RequestContext.getCurrentInstance().execute("PF('sobrepasaCaracteres').show()");
            pasa++;
        }

        if (pasa == 0 && pasaA == 0) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesCodigos");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                nombre = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesNombres");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                nivelEducativo = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesNivel");
                nivelEducativo.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtradoListaTiposEducaciones = null;
                tipoLista = 0;
                altoTabla = "270";
                RequestContext.getCurrentInstance().update("form:datosTiposEducaciones");
            }
            //AGREGAR REGISTRO A LA LISTA CIUDADES.
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoEducacion.setSecuencia(l);
            listaTiposEducacionesCrear.add(nuevoTipoEducacion);
            listaTiposEducaciones.add(nuevoTipoEducacion);
            contarRegistros();
            tipoEducacionSeleccionado = nuevoTipoEducacion;
            nuevoTipoEducacion = new TiposEducaciones();

            RequestContext.getCurrentInstance().update("form:datosTiposEducaciones");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoEducacion').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoEducacion");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoEducacion').show()");
        }
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        if (bandera == 0) {
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesCodigos");
            codigo.setFilterStyle("width: 85%;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesNombres");
            nombre.setFilterStyle("width: 85%;");
            nivelEducativo = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesNivel");
            nivelEducativo.setFilterStyle("width: 85%;");
            altoTabla = "250";
            RequestContext.getCurrentInstance().update("form:datosTiposEducaciones");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesCodigos");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesNombres");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            nivelEducativo = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesNivel");
            nivelEducativo.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosTiposEducaciones");
            bandera = 0;
            filtradoListaTiposEducaciones = null;
            tipoLista = 0;
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposEducacionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TiposEducacionesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposEducacionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TiposEducacionesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    //LIMPIAR NUEVO REGISTRO DE TIPO DE TELEFONO
    public void limpiarNuevoTipoEducacion() {
        nuevoTipoEducacion = new TiposEducaciones();
    }

    public void borrarTiposEducaciones() {

        if (tipoEducacionSeleccionado != null) {

            if (!listaTiposEducacionesModificar.isEmpty() && listaTiposEducacionesModificar.contains(tipoEducacionSeleccionado)) {
                listaTiposEducacionesModificar.remove(listaTiposEducacionesModificar.indexOf(tipoEducacionSeleccionado));
                listaTiposEducacionesBorrar.add(tipoEducacionSeleccionado);
            } else if (!listaTiposEducacionesCrear.isEmpty() && listaTiposEducacionesCrear.contains(tipoEducacionSeleccionado)) {
                listaTiposEducacionesCrear.remove(listaTiposEducacionesCrear.indexOf(tipoEducacionSeleccionado));
            } else {
                listaTiposEducacionesBorrar.add(tipoEducacionSeleccionado);
            }
            listaTiposEducaciones.remove(tipoEducacionSeleccionado);

            if (tipoLista == 1) {
                filtradoListaTiposEducaciones.remove(tipoEducacionSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosTiposEducaciones");
            contarRegistros();
            tipoEducacionSeleccionado = null;
            guardado = true;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void cambiarIndice(TiposEducaciones tiposEducaciones, int celda) {
        if (permitirIndex == true) {
            tipoEducacionSeleccionado = tiposEducaciones;
            cualCelda = celda;
            if (cualCelda == 0) {
                tipoEducacionSeleccionado.getCodigo();

            } else if (cualCelda == 1) {
                tipoEducacionSeleccionado.getNombre();
            } else if (cualCelda == 2) {
                tipoEducacionSeleccionado.getNiveleducativo();
            }
        }
    }

    public void duplicarTiposEducaciones() {
        if (tipoEducacionSeleccionado != null) {
            duplicarTipoEducacion = new TiposEducaciones();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTipoEducacion.setSecuencia(l);
                duplicarTipoEducacion.setCodigo(tipoEducacionSeleccionado.getCodigo());
                duplicarTipoEducacion.setNombre(tipoEducacionSeleccionado.getNombre());
                duplicarTipoEducacion.setNiveleducativo(tipoEducacionSeleccionado.getNiveleducativo());
            }
            if (tipoLista == 1) {
                duplicarTipoEducacion.setSecuencia(l);
                duplicarTipoEducacion.setCodigo(tipoEducacionSeleccionado.getCodigo());
                duplicarTipoEducacion.setNombre(tipoEducacionSeleccionado.getNombre());
                duplicarTipoEducacion.setNiveleducativo(tipoEducacionSeleccionado.getNiveleducativo());
                altoTabla = "270";
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEducacion");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoEducacion').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void limpiarduplicarTipoEducacion() {
        duplicarTipoEducacion = new TiposEducaciones();
    }

    public void confirmarDuplicar() {

        RequestContext context = RequestContext.getCurrentInstance();
        int pasa = 0;

        for (int i = 0; i < listaTiposEducaciones.size(); i++) {
            if (duplicarTipoEducacion.getNombre().equals(listaTiposEducaciones.get(i).getNombre())) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeNombre");
                RequestContext.getCurrentInstance().execute("PF('existeNombre').show()");
                pasa++;
            }
            if (duplicarTipoEducacion.getCodigo() == listaTiposEducaciones.get(i).getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                pasa++;
            }
        }

        if (pasa == 0) {

            listaTiposEducaciones.add(duplicarTipoEducacion);
            listaTiposEducacionesCrear.add(duplicarTipoEducacion);
            tipoEducacionSeleccionado = duplicarTipoEducacion;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosTiposEducaciones");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                System.out.println("Desactivar");
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesCodigos");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                nombre = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesNombres");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                nivelEducativo = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesNivel");
                nivelEducativo.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtradoListaTiposEducaciones = null;
                tipoLista = 0;
                RequestContext.getCurrentInstance().update("form:datosTiposEducaciones");
                tipoLista = 0;
            }
            duplicarTipoEducacion = new TiposEducaciones();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTipoEducacion");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoEducacion').hide()");
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesCodigos");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesNombres");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            nivelEducativo = (Column) c.getViewRoot().findComponent("form:datosTiposEducaciones:tiposEducacionesNivel");
            nivelEducativo.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtradoListaTiposEducaciones = null;
            tipoLista = 0;
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosTiposEducaciones");
            tipoLista = 0;
        }
        listaTiposEducacionesBorrar.clear();
        listaTiposEducacionesCrear.clear();
        listaTiposEducacionesModificar.clear();
        contarRegistros();
        tipoEducacionSeleccionado = null;
        k = 0;
        listaTiposEducaciones = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposEducaciones");
    }

    public void modificarTiposEducaciones(TiposEducaciones tiposEducaciones, String confirmarCambio, String valorConfirmar) {
        tipoEducacionSeleccionado = tiposEducaciones;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaTiposEducacionesCrear.contains(tipoEducacionSeleccionado)) {
                    if (listaTiposEducacionesModificar.isEmpty()) {
                        listaTiposEducacionesModificar.add(tipoEducacionSeleccionado);
                    } else if (!listaTiposEducacionesModificar.contains(tipoEducacionSeleccionado)) {
                        listaTiposEducacionesModificar.add(tipoEducacionSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else if (!listaTiposEducacionesCrear.contains(tipoEducacionSeleccionado)) {

                if (listaTiposEducacionesModificar.isEmpty()) {
                    listaTiposEducacionesModificar.add(tipoEducacionSeleccionado);
                } else if (!listaTiposEducacionesModificar.contains(tipoEducacionSeleccionado)) {
                    listaTiposEducacionesModificar.add(tipoEducacionSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            RequestContext.getCurrentInstance().update("form:datosTiposEducaciones");
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoEducacionSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tipoEducacionSeleccionado.getSecuencia(), "TIPOSEDUCACIONES");
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSEDUCACIONES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        deshabilitarBotonLov();
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
    }

    /////////////////////////////GETTERS AND SETTERS//////////////////////
    public List<TiposEducaciones> getListaTiposEducaciones() {
        if (listaTiposEducaciones == null) {
            listaTiposEducaciones = administrarTiposEducaciones.TiposEducaciones();
        }
        return listaTiposEducaciones;
    }

    public void setListaTiposEducaciones(List<TiposEducaciones> listaTiposEducaciones) {
        this.listaTiposEducaciones = listaTiposEducaciones;
    }

    public List<TiposEducaciones> getFiltradoListaTiposEducaciones() {
        return filtradoListaTiposEducaciones;
    }

    public void setFiltradoListaTiposEducaciones(List<TiposEducaciones> filtradoListaTiposEducaciones) {
        this.filtradoListaTiposEducaciones = filtradoListaTiposEducaciones;
    }

    public TiposEducaciones getTipoEducacionSeleccionado() {
        return tipoEducacionSeleccionado;
    }

    public void setTipoEducacionSeleccionado(TiposEducaciones tipoEducacionSeleccionado) {
        this.tipoEducacionSeleccionado = tipoEducacionSeleccionado;
    }

    public TiposEducaciones getNuevoTipoEducacion() {
        return nuevoTipoEducacion;
    }

    public void setNuevoTipoEducacion(TiposEducaciones nuevoTipoEducacion) {
        this.nuevoTipoEducacion = nuevoTipoEducacion;
    }

    public TiposEducaciones getDuplicarTipoEducacion() {
        return duplicarTipoEducacion;
    }

    public void setDuplicarTipoEducacion(TiposEducaciones duplicarTipoEducacion) {
        this.duplicarTipoEducacion = duplicarTipoEducacion;
    }

    public TiposEducaciones getEditarTipoEducacion() {
        return editarTipoEducacion;
    }

    public void setEditarTipoEducacion(TiposEducaciones editarTipoEducacion) {
        this.editarTipoEducacion = editarTipoEducacion;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
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

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInforegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposEducaciones");
        inforegistro = String.valueOf(tabla.getRowCount());
        return inforegistro;
    }

    public void setInforegistro(String inforegistro) {
        this.inforegistro = inforegistro;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

}
