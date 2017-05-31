/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empresas;
import Entidades.SectoresEconomicos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSectoresEconomicosInterface;
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
@Named(value = "controlSectoresEconomicos")
@SessionScoped
public class ControlSectoresEconomicos implements Serializable {

    @EJB
    AdministrarSectoresEconomicosInterface administrarSectores;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<SectoresEconomicos> listSectores;
    private List<SectoresEconomicos> listSectoresFiltrar;
    private List<SectoresEconomicos> listSectoresCrear;
    private List<SectoresEconomicos> listSectoresBorrar;
    private List<SectoresEconomicos> listSectoresModificar;
    private SectoresEconomicos sectorEconomicoSeleccionado;
    private SectoresEconomicos nuevoSectorEconomico;
    private SectoresEconomicos duplicarSectorEconomico;
    private SectoresEconomicos editarSectorEconomico;
    //lov empresas
    private List<Empresas> lovEmpresas;
    private List<Empresas> lovEmpresasFiltrar;
    private Empresas empresaActual, empresaSeleccionada;
    //
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
    private boolean activarLov;
    private String infoRegistro, infoRegistroEmpresa;
    private DataTable tablaC;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlSectoresEconomicos() {
        listSectores = null;
        listSectoresCrear = new ArrayList<SectoresEconomicos>();
        listSectoresBorrar = new ArrayList<SectoresEconomicos>();
        listSectoresModificar = new ArrayList<SectoresEconomicos>();
        permitirIndex = true;
        editarSectorEconomico = new SectoresEconomicos();
        nuevoSectorEconomico = new SectoresEconomicos();
        nuevoSectorEconomico.setEmpresa(new Empresas());
        duplicarSectorEconomico = new SectoresEconomicos();
        tamano = 270;
        cualCelda = -1;
        sectorEconomicoSeleccionado = null;
        activarLov = true;
        lovEmpresas = null;
        empresaActual = null;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSectores.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        if (lovEmpresas == null) {
            getLovEmpresas();
        }
        if (empresaActual == null) {
            empresaActual = lovEmpresas.get(0);
        }
        listSectores = null;
        getListSectores();
        if (listSectores != null) {
            if (!listSectores.isEmpty()) {
                sectorEconomicoSeleccionado = listSectores.get(0);
            }
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        if (lovEmpresas == null) {
            getLovEmpresas();
        }
        if (empresaActual == null) {
            empresaActual = lovEmpresas.get(0);
        }
        listSectores = null;
        getListSectores();
        if (listSectores != null) {
            if (!listSectores.isEmpty()) {
                sectorEconomicoSeleccionado = listSectores.get(0);
            }
        }
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        /*if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
         
        } else {
            */
String pagActual = "sectoreseconomicos";
            
            
            


            
            
            
            
            
            
            if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);
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

    public String redirigir() {
        return paginaAnterior;
    }

    public void cambiarIndice(SectoresEconomicos sectores, int celda) {
        if (permitirIndex == true) {
            sectorEconomicoSeleccionado = sectores;
            cualCelda = celda;
            if (cualCelda == 0) {
                sectorEconomicoSeleccionado.getCodigo();
            } else if (cualCelda == 1) {
                sectorEconomicoSeleccionado.getDescripcion();
            } else if (cualCelda == 2) {
                sectorEconomicoSeleccionado.getEmpresa().getNombre();
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosSectores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSectores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listSectoresFiltrar = null;
            tamano = 270;
            RequestContext.getCurrentInstance().update("form:datosSectores");
            tipoLista = 0;
        }

        listSectoresBorrar.clear();
        listSectoresCrear.clear();
        listSectoresModificar.clear();
        sectorEconomicoSeleccionado = null;
        contarRegistros();
        k = 0;
        listSectores = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosSectores");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {  limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosSectores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSectores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSectores");
            bandera = 0;
            listSectoresFiltrar = null;
            tipoLista = 0;
            tamano = 270;
        }

        listSectoresBorrar.clear();
        listSectoresCrear.clear();
        listSectoresModificar.clear();
        sectorEconomicoSeleccionado = null;
        k = 0;
        listSectores = null;
        guardado = true;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSectores:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSectores:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosSectores");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSectores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSectores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSectores");
            bandera = 0;
            listSectoresFiltrar = null;
            tipoLista = 0;
        }
    }

    public void modificarSectoresEconomicos(SectoresEconomicos sector, String confirmarCambio, String valorConfirmar) {
        sectorEconomicoSeleccionado = sector;
        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listSectoresCrear.contains(sectorEconomicoSeleccionado)) {
                    if (listSectoresModificar.isEmpty()) {
                        listSectoresModificar.add(sectorEconomicoSeleccionado);
                    } else if (!listSectoresModificar.contains(sectorEconomicoSeleccionado)) {
                        listSectoresModificar.add(sectorEconomicoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else if (!listSectoresCrear.contains(sectorEconomicoSeleccionado)) {
                if (!listSectoresCrear.contains(sectorEconomicoSeleccionado)) {
                    if (listSectoresModificar.isEmpty()) {
                        listSectoresModificar.add(sectorEconomicoSeleccionado);
                    } else if (!listSectoresModificar.contains(sectorEconomicoSeleccionado)) {
                        listSectoresModificar.add(sectorEconomicoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                RequestContext.getCurrentInstance().update("form:datosSectores");
            }
        }
    }

    public void borrandoSectoresEconomicos() {
        if (sectorEconomicoSeleccionado != null) {
            if (!listSectoresModificar.isEmpty() && listSectoresModificar.contains(sectorEconomicoSeleccionado)) {
                listSectoresModificar.remove(listSectoresModificar.indexOf(sectorEconomicoSeleccionado));
                listSectoresBorrar.add(sectorEconomicoSeleccionado);
            } else if (!listSectoresCrear.isEmpty() && listSectoresCrear.contains(sectorEconomicoSeleccionado)) {
                listSectoresCrear.remove(listSectoresCrear.indexOf(sectorEconomicoSeleccionado));
            } else {
                listSectoresBorrar.add(sectorEconomicoSeleccionado);
            }
            listSectores.remove(sectorEconomicoSeleccionado);
            if (tipoLista == 1) {
                listSectoresFiltrar.remove(sectorEconomicoSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosSectores");
            contarRegistros();
            sectorEconomicoSeleccionado = null;
            guardado = true;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {

        if (!listSectoresBorrar.isEmpty() || !listSectoresCrear.isEmpty() || !listSectoresModificar.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarSectoresEconomicos() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listSectoresBorrar.isEmpty()) {
                    administrarSectores.borrarSector(listSectoresBorrar);
                    registrosBorrados = listSectoresBorrar.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    listSectoresBorrar.clear();
                }
                if (!listSectoresModificar.isEmpty()) {
                    administrarSectores.editarSector(listSectoresModificar);
                    listSectoresModificar.clear();
                }
                if (!listSectoresCrear.isEmpty()) {
                    administrarSectores.crearSector(listSectoresCrear);
                    listSectoresCrear.clear();
                }
                listSectores = null;
                getListSectores();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                sectorEconomicoSeleccionado = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosSectores");
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void editarCelda() {
        if (sectorEconomicoSeleccionado != null) {
            editarSectorEconomico = sectorEconomicoSeleccionado;

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
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoSectorEconomico() {
        System.out.println("agregarNuevoTiposCursos");
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (nuevoSectorEconomico.getDescripcion().equals(" ") || nuevoSectorEconomico.getDescripcion().equals("")) {
            mensajeValidacion = mensajeValidacion + " * Descripción \n";
            contador++;
        }
        if (nuevoSectorEconomico.getCodigo().equals(" ") || nuevoSectorEconomico.getCodigo().equals("")) {
            mensajeValidacion = mensajeValidacion + " * Codigo \n";
            contador++;
        }

        for (int i = 0; i < listSectores.size(); i++) {
            if (listSectores.get(i).getCodigo().equals(nuevoSectorEconomico.getCodigo())) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                duplicados++;
            }
            if (contador != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoSector");
                RequestContext.getCurrentInstance().execute("PF('validacionNuevoSector').show()");

            }
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosSectores:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSectores:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listSectoresFiltrar = null;
                tipoLista = 0;
                tamano = 270;
                RequestContext.getCurrentInstance().update("form:datosSectores");
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoSectorEconomico.setSecuencia(l);
            nuevoSectorEconomico.setEmpresa(empresaActual);
            listSectoresCrear.add(nuevoSectorEconomico);
            listSectores.add(nuevoSectorEconomico);
            contarRegistros();
            sectorEconomicoSeleccionado = nuevoSectorEconomico;
            nuevoSectorEconomico = new SectoresEconomicos();
            RequestContext.getCurrentInstance().update("form:datosSectores");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroSector').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoSector");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoSector').show()");
        }
    }

//    public void asignarIndex(SectoresEconomicos sector, int dlg, int LND) {
//        sectorEconomicoSeleccionado = sector;
//        RequestContext context = RequestContext.getCurrentInstance();
//        tipoActualizacion = LND;
//        if (dlg == 0) {
//        }
//    }
    public void mostrarDialogoEmpresas() {
        getLovEmpresas();
        contarRegistrosEmpresas();
        RequestContext.getCurrentInstance().update("formularioDialogos:empresaDialogo");
        RequestContext.getCurrentInstance().execute("PF('empresaDialogo').show()");

    }

    public void limpiarNuevoSectorEconomico() {
        nuevoSectorEconomico = new SectoresEconomicos();
        nuevoSectorEconomico.setEmpresa(new Empresas());
    }

    public void duplicandoSectoresEconomicos() {
        if (sectorEconomicoSeleccionado != null) {
            duplicarSectorEconomico = new SectoresEconomicos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarSectorEconomico.setSecuencia(l);
                duplicarSectorEconomico.setCodigo(sectorEconomicoSeleccionado.getCodigo());
                duplicarSectorEconomico.setDescripcion(sectorEconomicoSeleccionado.getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarSectorEconomico.setSecuencia(l);
                duplicarSectorEconomico.setCodigo(sectorEconomicoSeleccionado.getCodigo());
                duplicarSectorEconomico.setDescripcion(sectorEconomicoSeleccionado.getDescripcion());
                tamano = 270;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSector').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        RequestContext context = RequestContext.getCurrentInstance();
        int contador = 0;

        for (int i = 0; i < listSectores.size(); i++) {
            if (duplicarSectorEconomico.getCodigo().equals(listSectores.get(i).getCodigo())) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                contador++;
            }
        }

        if (contador == 0) {
            listSectores.add(duplicarSectorEconomico);
            listSectoresCrear.add(duplicarSectorEconomico);
            sectorEconomicoSeleccionado = duplicarSectorEconomico;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosSectores");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosSectores:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSectores:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listSectoresFiltrar = null;
                RequestContext.getCurrentInstance().update("form:datosSectores");
                tipoLista = 0;
            }
            duplicarSectorEconomico = new SectoresEconomicos();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroSector");
        RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSector').hide()");
    }

    public void limpiarDuplicarSectoresEconomicos() {
        duplicarSectorEconomico = new SectoresEconomicos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSectoresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SECTORESECONOMICOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSectoresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SECTORESECONOMICOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

//     public void actualizarEmpresa() {
//        RequestContext context = RequestContext.getCurrentInstance();
//        if (tipoActualizacion == 1) {
//            nuevoParametro.setEmpresa(empresaSeleccionada);
//            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresaParametro");
//        } else if (tipoActualizacion == 2) {
//            duplicarParametro.setEmpresa(empresaSeleccionada);
//            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresaParametro");
//        }
//        filtrarLovEmpresas = null;
//        empresaSeleccionada = new Empresas();
//        aceptar = true;
//        activoBtnsPaginas = true;
//        RequestContext.getCurrentInstance().update("form:novedadauto");
//        RequestContext.getCurrentInstance().update("form:incaPag");
//        RequestContext.getCurrentInstance().update("form:eliminarToda");
//        RequestContext.getCurrentInstance().update("form:procesoLiq");
//        RequestContext.getCurrentInstance().update("form:acumDif");
//        tipoActualizacion = -1;/*
//         RequestContext.getCurrentInstance().update("formularioLovEmpresa:EmpresaDialogo");
//         RequestContext.getCurrentInstance().update("formularioLovEmpresa:lovEmpresa");
//         RequestContext.getCurrentInstance().update("formularioLovEmpresa:aceptarE");*/
//
//        context.reset("formularioLovEmpresa:lovEmpresa:globalFilter");
//        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
//        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
//    }
//
//    public void cancelarCambioEmpresa() {
//        filtrarLovEmpresas = null;
//        empresaSeleccionada = new Empresas();
//        aceptar = true;
//        RequestContext context = RequestContext.getCurrentInstance();
//        activoBtnsPaginas = true;
//        RequestContext.getCurrentInstance().update("form:novedadauto");
//        RequestContext.getCurrentInstance().update("form:incaPag");
//        RequestContext.getCurrentInstance().update("form:eliminarToda");
//        RequestContext.getCurrentInstance().update("form:procesoLiq");
//        RequestContext.getCurrentInstance().update("form:acumDif");
//        tipoActualizacion = -1;
//        permitirIndex = true;
//        context.reset("formularioLovEmpresa:lovEmpresa:globalFilter");
//        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
//        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
//    }
    public void cambiarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        guardado = true;
        if (guardado) {
            empresaActual = empresaSeleccionada;
            listSectores = null;
            getListSectores();
            RequestContext.getCurrentInstance().update("form:datosSectores");
            RequestContext.getCurrentInstance().update("form:nombreEmpresa");
            RequestContext.getCurrentInstance().update("form:nitEmpresa");
            lovEmpresasFiltrar = null;
            aceptar = true;
            contarRegistros();
            context.reset("formularioDialogos:lovEmpresa:globalFilter");
            RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('empresaDialogo').hide()");
            RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresa");
            RequestContext.getCurrentInstance().update("formularioDialogos:empresaDialogo");
            RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
        }
    }

    public void cancelarCambioEmpresa() {
        lovEmpresasFiltrar = null;
        empresaSeleccionada = new Empresas();
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empresaDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresa");
        RequestContext.getCurrentInstance().update("formularioDialogos:empresaDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (sectorEconomicoSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(sectorEconomicoSeleccionado.getSecuencia(), "SECTORESECONOMICOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("SECTORESECONOMICOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void recordarSeleccion() {
        if (sectorEconomicoSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosSectores");
            tablaC.setSelection(sectorEconomicoSeleccionado);
        }
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposCursos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void contarRegistrosEmpresas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresa");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
    }

    ///////GETS Y SETS//////
    public List<SectoresEconomicos> getListSectores() {
        if (listSectores == null) {
            listSectores = administrarSectores.consultarSectoresEconomicosPorEmpresa(empresaActual.getSecuencia());
        }
        return listSectores;
    }

    public void setListSectores(List<SectoresEconomicos> listSectores) {
        this.listSectores = listSectores;
    }

    public List<SectoresEconomicos> getListSectoresFiltrar() {
        return listSectoresFiltrar;
    }

    public void setListSectoresFiltrar(List<SectoresEconomicos> listSectoresFiltrar) {
        this.listSectoresFiltrar = listSectoresFiltrar;
    }

    public SectoresEconomicos getSectorEconomicoSeleccionado() {
        return sectorEconomicoSeleccionado;
    }

    public void setSectorEconomicoSeleccionado(SectoresEconomicos sectorEconomicoSeleccionado) {
        this.sectorEconomicoSeleccionado = sectorEconomicoSeleccionado;
    }

    public SectoresEconomicos getNuevoSectorEconomico() {
        return nuevoSectorEconomico;
    }

    public void setNuevoSectorEconomico(SectoresEconomicos nuevoSectorEconomico) {
        this.nuevoSectorEconomico = nuevoSectorEconomico;
    }

    public SectoresEconomicos getDuplicarSectorEconomico() {
        return duplicarSectorEconomico;
    }

    public void setDuplicarSectorEconomico(SectoresEconomicos duplicarSectorEconomico) {
        this.duplicarSectorEconomico = duplicarSectorEconomico;
    }

    public SectoresEconomicos getEditarSectorEconomico() {
        return editarSectorEconomico;
    }

    public void setEditarSectorEconomico(SectoresEconomicos editarSectorEconomico) {
        this.editarSectorEconomico = editarSectorEconomico;
    }

    public List<Empresas> getLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarSectores.consultarEmpresas();
        }
        if (empresaActual == null) {
            empresaActual = lovEmpresas.get(0);
        }

        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public List<Empresas> getLovEmpresasFiltrar() {
        return lovEmpresasFiltrar;
    }

    public void setLovEmpresasFiltrar(List<Empresas> lovEmpresasFiltrar) {
        this.lovEmpresasFiltrar = lovEmpresasFiltrar;
    }

    public Empresas getEmpresaActual() {
        return empresaActual;
    }

    public void setEmpresaActual(Empresas empresaActual) {
        this.empresaActual = empresaActual;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
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

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosSectores");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroEmpresa() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEmpresa");
        infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

}
