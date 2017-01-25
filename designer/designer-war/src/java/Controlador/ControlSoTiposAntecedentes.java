/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.SoTiposAntecedentes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSoTiposAntecedentesInterface;
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
@Named(value = "controlSoTiposAntecedentes")
@SessionScoped
public class ControlSoTiposAntecedentes implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarSoTiposAntecedentesInterface administrarTiposAntecedentes;

    private List<SoTiposAntecedentes> listTiposAntecedentes;
    private List<SoTiposAntecedentes> listTiposAntecedentesCrear;
    private List<SoTiposAntecedentes> listTiposAntecedentesModificar;
    private List<SoTiposAntecedentes> listTiposAntecedentesBorrar;
    private List<SoTiposAntecedentes> listTiposAntecedentesFiltrar;
    private SoTiposAntecedentes tipoantecedenteSeleccionado;
    private SoTiposAntecedentes nuevoTipoAntecedente;
    private SoTiposAntecedentes duplicarTipoAntecedente;
    private SoTiposAntecedentes editarTipoAntecedente;
    private Column codigo, descripcion;
    private boolean aceptar, guardado, activarLov, permitirIndex;
    private int tipoActualizacion, tipoLista, bandera, k, cualCelda;
    private BigInteger l;
    private String infoRegistro, altoTabla, mensajeValidacion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlSoTiposAntecedentes() {
        permitirIndex = true;
        aceptar = true;
        tipoLista = 0;
        listTiposAntecedentesCrear = new ArrayList<SoTiposAntecedentes>();
        listTiposAntecedentesModificar = new ArrayList<SoTiposAntecedentes>();
        listTiposAntecedentesBorrar = new ArrayList<SoTiposAntecedentes>();
        listTiposAntecedentes = null;
        guardado = true;
        activarLov = true;
        paginaAnterior = " ";
        nuevoTipoAntecedente = new SoTiposAntecedentes();
        duplicarTipoAntecedente = new SoTiposAntecedentes();
        k = 0;
        altoTabla = "300";
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposAntecedentes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listTiposAntecedentes = null;
        getListTiposAntecedentes();
        if (listTiposAntecedentes != null) {
            if (!listTiposAntecedentes.isEmpty()) {
                tipoantecedenteSeleccionado = listTiposAntecedentes.get(0);
            }
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listTiposAntecedentes = null;
        getListTiposAntecedentes();
        if (listTiposAntecedentes != null) {
            if (!listTiposAntecedentes.isEmpty()) {
                tipoantecedenteSeleccionado = listTiposAntecedentes.get(0);
            }
        }
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
            String pagActual = "tiposantecedentes";
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

    public String retornarPaginaAnterior() {
        return paginaAnterior;
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAntecedentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAntecedentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposAntecedentes");
            bandera = 0;
            listTiposAntecedentesFiltrar = null;
            tipoLista = 0;
            altoTabla = "300";
        }
        listTiposAntecedentesBorrar.clear();
        listTiposAntecedentesCrear.clear();
        listTiposAntecedentesModificar.clear();
        k = 0;
        tipoantecedenteSeleccionado = null;
        listTiposAntecedentes = null;
        guardado = true;
        permitirIndex = true;
        getListTiposAntecedentes();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTiposAntecedentes");
    }

    public void editarCelda() {
        if (tipoantecedenteSeleccionado != null) {
            editarTipoAntecedente = tipoantecedenteSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigo");
                RequestContext.getCurrentInstance().execute("PF('editarCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcion').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarTipoAntecedente() {

        if (tipoantecedenteSeleccionado != null) {
            if (!listTiposAntecedentesModificar.isEmpty() && listTiposAntecedentesModificar.contains(tipoantecedenteSeleccionado)) {
                listTiposAntecedentesModificar.remove(listTiposAntecedentesModificar.indexOf(tipoantecedenteSeleccionado));
                listTiposAntecedentesBorrar.add(tipoantecedenteSeleccionado);
            } else if (!listTiposAntecedentesCrear.isEmpty() && listTiposAntecedentesCrear.contains(tipoantecedenteSeleccionado)) {
                listTiposAntecedentesCrear.remove(listTiposAntecedentesCrear.indexOf(tipoantecedenteSeleccionado));
            } else {
                listTiposAntecedentesBorrar.add(tipoantecedenteSeleccionado);
            }
            listTiposAntecedentes.remove(tipoantecedenteSeleccionado);
            if (tipoLista == 1) {
                listTiposAntecedentesFiltrar.remove(tipoantecedenteSeleccionado);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosTiposAntecedentes");
            tipoantecedenteSeleccionado = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void duplicarTA() {
        if (tipoantecedenteSeleccionado != null) {
            duplicarTipoAntecedente = new SoTiposAntecedentes();
            k++;
            l = BigInteger.valueOf(k);
            duplicarTipoAntecedente.setSecuencia(l);
            duplicarTipoAntecedente.setCodigo(tipoantecedenteSeleccionado.getCodigo());
            duplicarTipoAntecedente.setDescripcion(tipoantecedenteSeleccionado.getDescripcion());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoAntecedente");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoAntecedente').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        int pasaA = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarTipoAntecedente.getCodigo() == null) {
            mensajeValidacion = " Campo código vacío \n";
            pasa++;
        }
        if (duplicarTipoAntecedente.getDescripcion() == null) {
            mensajeValidacion = "Campo Descripción vacío \n";
            pasa++;
        }
        for (int i = 0; i < listTiposAntecedentes.size(); i++) {
            if ((listTiposAntecedentes.get(i).getCodigo().equals(duplicarTipoAntecedente.getCodigo()))) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeTipoAntecedente");
                RequestContext.getCurrentInstance().execute("PF('existeTipoAntecedente').show()");
                pasaA++;
            }
            if (pasa != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoAntecedente");
                RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoAntecedente').show()");
            }
        }

        if (pasa == 0 && pasaA == 0) {
            listTiposAntecedentes.add(duplicarTipoAntecedente);
            listTiposAntecedentesCrear.add(duplicarTipoAntecedente);
            tipoantecedenteSeleccionado = duplicarTipoAntecedente;
            getListTiposAntecedentes();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosTiposAntecedentes");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAntecedentes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAntecedentes:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "300";
                RequestContext.getCurrentInstance().update("form:datosTiposAntecedentes");
                bandera = 0;
                listTiposAntecedentesFiltrar = null;
                tipoLista = 0;
            }
            duplicarTipoAntecedente = new SoTiposAntecedentes();
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoAntecedente').hide()");
        }
    }

    public void limpiarDuplicarTipoAntecedente() {
        duplicarTipoAntecedente = new SoTiposAntecedentes();
    }

    public void limpiarNuevoTipoAntecedente() {
        nuevoTipoAntecedente = new SoTiposAntecedentes();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposAntecedentesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SoTiposAntecedentesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposAntecedentesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SoTiposAntecedentesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAntecedentes:codigo");
            codigo.setFilterStyle("width: 80% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAntecedentes:descripcion");
            descripcion.setFilterStyle("width: 80% !important");
            altoTabla = "280";
            RequestContext.getCurrentInstance().update("form:datosTiposAntecedentes");
            bandera = 1;

        } else if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAntecedentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAntecedentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "300";
            RequestContext.getCurrentInstance().update("form:datosTiposAntecedentes");
            bandera = 0;
            listTiposAntecedentesFiltrar = null;
            tipoLista = 0;
        }
    }

    public void agregarNuevoTipoAntecedente() {
        Long a = null;
        int pasa = 0;
        int pasaA = 0;
        mensajeValidacion = " ";
        FacesContext c = FacesContext.getCurrentInstance();

        if (nuevoTipoAntecedente.getCodigo() == null) {
            mensajeValidacion = "Campo código vacío \n";
            pasa++;
        }
        if (nuevoTipoAntecedente.getDescripcion() == null) {
            mensajeValidacion = " Campo Descripción vacío\n";
            pasa++;
        }
        for (int i = 0; i < listTiposAntecedentes.size(); i++) {
            if ((listTiposAntecedentes.get(i).getCodigo().equals(nuevoTipoAntecedente.getCodigo()))) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeTipoAntecedente");
                RequestContext.getCurrentInstance().execute("PF('existeTipoAntecedente').show()");
                pasaA++;
            }
        }

        if (pasa == 0 && pasaA == 0) {
            if (bandera == 1) {
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAntecedentes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAntecedentes:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposAntecedentes");
                bandera = 0;
                listTiposAntecedentesFiltrar = null;
                tipoLista = 0;
                altoTabla = "300";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoAntecedente.setSecuencia(l);
            listTiposAntecedentesCrear.add(nuevoTipoAntecedente);
            listTiposAntecedentes.add(nuevoTipoAntecedente);
            tipoantecedenteSeleccionado = nuevoTipoAntecedente;
            nuevoTipoAntecedente = new SoTiposAntecedentes();
            getListTiposAntecedentes();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosTiposAntecedentes");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoAntecedente').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoAntecedente");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoAntecedente').show()");
        }
    }

    public void cambiarIndice(SoTiposAntecedentes tipoantecedente, int celda) {
        tipoantecedenteSeleccionado = tipoantecedente;
        cualCelda = celda;
        if (cualCelda == 0) {
            tipoantecedenteSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            tipoantecedenteSeleccionado.getDescripcion();
        }
    }

    public void modificarTiposAntecedentes(SoTiposAntecedentes antecedente) {
        tipoantecedenteSeleccionado = antecedente;
        if (!listTiposAntecedentesCrear.contains(tipoantecedenteSeleccionado)) {
            if (listTiposAntecedentesModificar.isEmpty()) {
                listTiposAntecedentesModificar.add(tipoantecedenteSeleccionado);
            } else if (!listTiposAntecedentesModificar.contains(tipoantecedenteSeleccionado)) {
                listTiposAntecedentesModificar.add(tipoantecedenteSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        RequestContext.getCurrentInstance().update("form:datosTiposAntecedentes");
    }

    public void guardarCambiosTiposAntecedentes() {
        try {
            if (guardado == false) {
                if (!listTiposAntecedentesBorrar.isEmpty()) {
                    administrarTiposAntecedentes.borrarTipoAntecedente(listTiposAntecedentesBorrar);
                    listTiposAntecedentesBorrar.clear();
                }

                if (!listTiposAntecedentesCrear.isEmpty()) {
                    administrarTiposAntecedentes.crearTipoAntecedente(listTiposAntecedentesCrear);
                }
                listTiposAntecedentesCrear.clear();
                if (!listTiposAntecedentesModificar.isEmpty()) {
                    administrarTiposAntecedentes.modificarTipoAntecedente(listTiposAntecedentesModificar);
                    listTiposAntecedentesModificar.clear();
                }
                listTiposAntecedentes = null;
                getListTiposAntecedentes();
                contarRegistros();
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosTiposAntecedentes");
                permitirIndex = true;
                tipoantecedenteSeleccionado = null;
            }
        } catch (Exception e) {
            System.out.println("Error guardando datos : " + e.getMessage());
            FacesMessage msg = new FacesMessage("Información", "Error en el guardado, Intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 1) {

            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAntecedentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAntecedentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "300";
            RequestContext.getCurrentInstance().update("form:datosTiposAntecedentes");
            bandera = 0;
            listTiposAntecedentesFiltrar = null;
            tipoLista = 0;
        }

        listTiposAntecedentesBorrar.clear();
        listTiposAntecedentesCrear.clear();
        listTiposAntecedentesModificar.clear();
        contarRegistros();
        tipoantecedenteSeleccionado = null;
        k = 0;
        listTiposAntecedentes = null;
        guardado = true;
        permitirIndex = true;

    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoantecedenteSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tipoantecedenteSeleccionado.getSecuencia(), "SOTIPOSANTECEDENTES");
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
        } else if (administrarRastros.verificarHistoricosTabla("SOTIPOSANTECEDENTES")) {
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

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    //////GETS Y SETS ////
    public List<SoTiposAntecedentes> getListTiposAntecedentes() {
        if (listTiposAntecedentes == null) {
            listTiposAntecedentes = administrarTiposAntecedentes.consultarTiposAntecedentes();
        }
        return listTiposAntecedentes;
    }

    public void setListTiposAntecedentes(List<SoTiposAntecedentes> listTiposAntecedentes) {
        this.listTiposAntecedentes = listTiposAntecedentes;
    }

    public List<SoTiposAntecedentes> getListTiposAntecedentesFiltrar() {
        return listTiposAntecedentesFiltrar;
    }

    public void setListTiposAntecedentesFiltrar(List<SoTiposAntecedentes> listTiposAntecedentesFiltrar) {
        this.listTiposAntecedentesFiltrar = listTiposAntecedentesFiltrar;
    }

    public SoTiposAntecedentes getTipoantecedenteSeleccionado() {
        return tipoantecedenteSeleccionado;
    }

    public void setTipoantecedenteSeleccionado(SoTiposAntecedentes tipoantecedenteSeleccionado) {
        this.tipoantecedenteSeleccionado = tipoantecedenteSeleccionado;
    }

    public SoTiposAntecedentes getNuevoTipoAntecedente() {
        return nuevoTipoAntecedente;
    }

    public void setNuevoTipoAntecedente(SoTiposAntecedentes nuevoTipoAntecedente) {
        this.nuevoTipoAntecedente = nuevoTipoAntecedente;
    }

    public SoTiposAntecedentes getDuplicarTipoAntecedente() {
        return duplicarTipoAntecedente;
    }

    public void setDuplicarTipoAntecedente(SoTiposAntecedentes duplicarTipoAntecedente) {
        this.duplicarTipoAntecedente = duplicarTipoAntecedente;
    }

    public SoTiposAntecedentes getEditarTipoAntecedente() {
        return editarTipoAntecedente;
    }

    public void setEditarTipoAntecedente(SoTiposAntecedentes editarTipoAntecedente) {
        this.editarTipoAntecedente = editarTipoAntecedente;
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

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposAntecedentes");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

}
