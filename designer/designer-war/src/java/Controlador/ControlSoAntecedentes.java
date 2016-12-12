/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.SoAntecedentes;
import Entidades.SoTiposAntecedentes;
import Entidades.TiposAccidentes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSoAntecedentesInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
@Named(value = "controlSoAntecedentes")
@SessionScoped
public class ControlSoAntecedentes implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarSoAntecedentesInterface administrarAntecedentes;

    private List<SoAntecedentes> listAntecedentes;
    private List<SoAntecedentes> listAntecedentesCrear;
    private List<SoAntecedentes> listAntecedentesModificar;
    private List<SoAntecedentes> listAntecedentesBorrar;
    private List<SoAntecedentes> listAntecedentesFiltrar;
    private SoAntecedentes antecedenteSeleccionado;
    private SoAntecedentes nuevoAntecedente;
    private SoAntecedentes duplicarAntecedente;
    private SoAntecedentes editarAntecedente;

    private List<SoTiposAntecedentes> lovTiposAntecedentes;
    private List<SoTiposAntecedentes> lovTiposAntecedentesFiltrar;
    private SoTiposAntecedentes tipoAntecedenteSeleccionado;

    private Column Codigo, Descripcion, TipoAntecedente;
    private boolean aceptar, guardado, activarLov;
    private int tipoActualizacion, tipoLista; //Activo/Desactivo Crtl + F11
    private int bandera;
    private BigInteger l;
    private int k;
    private boolean permitirIndex;
    private String infoRegistro, infoRegistroLov, paginaanterior;
    private String altoTabla, mensajeValidacion;
    private int cualCelda;

    public ControlSoAntecedentes() {
        permitirIndex = true;
        aceptar = true;
        tipoLista = 0;
        listAntecedentesCrear = new ArrayList<SoAntecedentes>();
        listAntecedentesModificar = new ArrayList<SoAntecedentes>();
        listAntecedentesBorrar = new ArrayList<SoAntecedentes>();
        lovTiposAntecedentes = null;
        listAntecedentes = null;
        guardado = true;
        activarLov = true;
        paginaanterior = " ";
        nuevoAntecedente = new SoAntecedentes();
        duplicarAntecedente = new SoAntecedentes();
        k = 0;
        altoTabla = "300";
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarAntecedentes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String pagina) {
        paginaanterior = pagina;
        listAntecedentes = null;
        getListAntecedentes();
        if (listAntecedentes != null) {
            if (!listAntecedentes.isEmpty()) {
                antecedenteSeleccionado = listAntecedentes.get(0);
            }
        }
    }

    public String retornarPaginaAnterior() {
        return paginaanterior;
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            Codigo = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:codigo");
            Codigo.setFilterStyle("display: none; visibility: hidden;");
            Descripcion = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:descripcion");
            Descripcion.setFilterStyle("display: none; visibility: hidden;");
            TipoAntecedente = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:tipoAntecedente");
            TipoAntecedente.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            bandera = 0;
            listAntecedentesFiltrar = null;
            tipoLista = 0;
            altoTabla = "300";
        }
        listAntecedentesBorrar.clear();
        listAntecedentesCrear.clear();
        listAntecedentesModificar.clear();
        k = 0;
        antecedenteSeleccionado = null;
        listAntecedentes = null;
        guardado = true;
        permitirIndex = true;
        getListAntecedentes();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosAntecedentes");
    }

    public void editarCelda() {
        if (antecedenteSeleccionado != null) {
            editarAntecedente = antecedenteSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigo");
                RequestContext.getCurrentInstance().execute("PF('editarCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcion').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoAntecedente");
                RequestContext.getCurrentInstance().execute("PF('editarTipoAntecedente').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void listaValoresBoton() {
        if (antecedenteSeleccionado != null) {
            if (cualCelda == 2) {
                contarRegistrosLov();
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposAntecedentesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposAntecedentesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void borrarAntecedente() {

        if (antecedenteSeleccionado != null) {
            if (!listAntecedentesModificar.isEmpty() && listAntecedentesModificar.contains(antecedenteSeleccionado)) {
                listAntecedentesModificar.remove(listAntecedentesModificar.indexOf(antecedenteSeleccionado));
                listAntecedentesBorrar.add(antecedenteSeleccionado);
            } else if (!listAntecedentesCrear.isEmpty() && listAntecedentesCrear.contains(antecedenteSeleccionado)) {
                listAntecedentesCrear.remove(listAntecedentesCrear.indexOf(antecedenteSeleccionado));
            } else {
                listAntecedentesBorrar.add(antecedenteSeleccionado);
            }
            listAntecedentes.remove(antecedenteSeleccionado);
            if (tipoLista == 1) {
                listAntecedentesFiltrar.remove(antecedenteSeleccionado);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            antecedenteSeleccionado = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void duplicarA() {
        if (antecedenteSeleccionado != null) {
            duplicarAntecedente = new SoAntecedentes();
            k++;
            l = BigInteger.valueOf(k);
            duplicarAntecedente.setSecuencia(l);
            duplicarAntecedente.setCodigo(antecedenteSeleccionado.getCodigo());
            duplicarAntecedente.setDescripcion(antecedenteSeleccionado.getDescripcion());
            duplicarAntecedente.setTipoantecedente(antecedenteSeleccionado.getTipoantecedente());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAntecedente");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroAntecedente').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        int pasaA = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarAntecedente.getCodigo() == null) {
            mensajeValidacion = " Campo código vacío \n";
            pasa++;
        }
        if (duplicarAntecedente.getTipoantecedente().getSecuencia() == null) {
            mensajeValidacion = "Campo Tipo Antecedente vacío \n";
            pasa++;
        }
        for (int i = 0; i < listAntecedentes.size(); i++) {
            if ((listAntecedentes.get(i).getCodigo().equals(duplicarAntecedente.getCodigo()))) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeAntecedente");
                RequestContext.getCurrentInstance().execute("PF('existeAntecedente').show()");
                pasaA++;
            }
            if (pasa != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoAntecedente");
                RequestContext.getCurrentInstance().execute("PF('validacionNuevoAntecedente').show()");
            }
        }

        if (pasa == 0 && pasaA == 0) {
            listAntecedentes.add(duplicarAntecedente);
            listAntecedentesCrear.add(duplicarAntecedente);
            antecedenteSeleccionado = duplicarAntecedente;
            getListAntecedentes();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                Codigo = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:codigo");
                Codigo.setFilterStyle("display: none; visibility: hidden;");
                Descripcion = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:descripcion");
                Descripcion.setFilterStyle("display: none; visibility: hidden;");
                TipoAntecedente = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:tipoAntecedente");
                TipoAntecedente.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "300";
                RequestContext.getCurrentInstance().update("form:datosAntecedentes");
                bandera = 0;
                listAntecedentesFiltrar = null;
                tipoLista = 0;
            }
            duplicarAntecedente = new SoAntecedentes();
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroAntecedente').hide()");
        }
    }

    public void limpiarDuplicarAntecedente() {
        duplicarAntecedente = new SoAntecedentes();
    }

    public void limpiarNuevoAntecedente() {
        nuevoAntecedente = new SoAntecedentes();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAntecedentesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SoAntecedentesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAntecedentesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SoAntecedentesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            Codigo = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:codigo");
            Codigo.setFilterStyle("width: 80% !important");
            Descripcion = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:descripcion");
            Descripcion.setFilterStyle("width: 80% !important");
            TipoAntecedente = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:tipoAntecedente");
            TipoAntecedente.setFilterStyle("width: 80% !important");
            altoTabla = "280";
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            bandera = 1;

        } else if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            Codigo = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:codigo");
            Codigo.setFilterStyle("display: none; visibility: hidden;");
            Descripcion = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:descripcion");
            Descripcion.setFilterStyle("display: none; visibility: hidden;");
            TipoAntecedente = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:tipoAntecedente");
            TipoAntecedente.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "300";
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            bandera = 0;
            listAntecedentesFiltrar = null;
            tipoLista = 0;
        }
    }

    public void agregarNuevoAntecedente() {
        Long a = null;
        int pasa = 0;
        int pasaA = 0;
        mensajeValidacion = " ";
        FacesContext c = FacesContext.getCurrentInstance();

        if (nuevoAntecedente.getCodigo() == null) {
            mensajeValidacion = "Campo código vacío \n";
            pasa++;
        }
        if (nuevoAntecedente.getTipoantecedente().getSecuencia() == null) {
            mensajeValidacion = " Campo Tipo Antecedente vacío\n";
            pasa++;
        }
        for (int i = 0; i < listAntecedentes.size(); i++) {
            if ((listAntecedentes.get(i).getCodigo().equals(nuevoAntecedente.getCodigo()))) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeAntecedente");
                RequestContext.getCurrentInstance().execute("PF('existeAntecedente').show()");
                pasaA++;
            }
        }

        if (pasa == 0 && pasaA == 0) {
            if (bandera == 1) {
                Codigo = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:codigo");
                Codigo.setFilterStyle("display: none; visibility: hidden;");
                Descripcion = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:descripcion");
                Descripcion.setFilterStyle("display: none; visibility: hidden;");
                TipoAntecedente = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:tipoAntecedente");
                TipoAntecedente.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosAntecedentes");
                bandera = 0;
                listAntecedentesFiltrar = null;
                tipoLista = 0;
                altoTabla = "300";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoAntecedente.setSecuencia(l);
            if (nuevoAntecedente.getTipoantecedente().getSecuencia() == null) {
                nuevoAntecedente.setTipoantecedente(new SoTiposAntecedentes());
            }
            listAntecedentesCrear.add(nuevoAntecedente);
            listAntecedentes.add(nuevoAntecedente);
            antecedenteSeleccionado = nuevoAntecedente;
            nuevoAntecedente = new SoAntecedentes();
            nuevoAntecedente.setTipoantecedente(new SoTiposAntecedentes());
            getListAntecedentes();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroAntecedente').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoAntecedente");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoAntecedente').show()");
        }
    }

    public void cambiarIndice(SoAntecedentes antecedente, int celda) {
        antecedenteSeleccionado = antecedente;
        cualCelda = celda;
        if (cualCelda == 0) {
            deshabilitarBotonLov();
            antecedenteSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            deshabilitarBotonLov();
            antecedenteSeleccionado.getDescripcion();
        } else if (cualCelda == 2) {
            antecedenteSeleccionado.getTipoantecedente().getDescripcion();
            habilitarBotonLov();
        }

    }

    public void modificarAntecedentes(SoAntecedentes antecedente) {
        antecedenteSeleccionado = antecedente;
        if (!listAntecedentesCrear.contains(antecedenteSeleccionado)) {
            if (listAntecedentesModificar.isEmpty()) {
                listAntecedentesModificar.add(antecedenteSeleccionado);
            } else if (!listAntecedentesModificar.contains(antecedenteSeleccionado)) {
                listAntecedentesModificar.add(antecedenteSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        RequestContext.getCurrentInstance().update("form:datosAntecedentes");
    }

    public void guardarCambiosAntecedentes() {
        try {
            if (guardado == false) {
                if (!listAntecedentesBorrar.isEmpty()) {
                    administrarAntecedentes.borrarAntecedente(listAntecedentesBorrar);
                    listAntecedentesBorrar.clear();
                }

                if (!listAntecedentesCrear.isEmpty()) {
                    administrarAntecedentes.crearAntecedente(listAntecedentesCrear);
                }
                listAntecedentesCrear.clear();
                if (!listAntecedentesModificar.isEmpty()) {
                    administrarAntecedentes.modificarAntecedente(listAntecedentesModificar);
                    listAntecedentesModificar.clear();
                }
                listAntecedentes = null;
                getListAntecedentes();
                contarRegistros();
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                deshabilitarBotonLov();
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosAntecedentes");
                permitirIndex = true;
                antecedenteSeleccionado = null;
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

               Codigo = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:codigo");
            Codigo.setFilterStyle("display: none; visibility: hidden;");
            Descripcion = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:descripcion");
            Descripcion.setFilterStyle("display: none; visibility: hidden;");
            TipoAntecedente = (Column) c.getViewRoot().findComponent("form:datosAntecedentes:tipoAntecedente");
            TipoAntecedente.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "300";
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
            bandera = 0;
            listAntecedentesFiltrar = null;
            tipoLista = 0;
        }

        listAntecedentesBorrar.clear();
        listAntecedentesCrear.clear();
        listAntecedentesModificar.clear();
        contarRegistros();
        antecedenteSeleccionado = null;
        k = 0;
        listAntecedentes = null;
        guardado = true;
        permitirIndex = true;

    }
    
     public void activarAceptar() {
        aceptar = false;
    }
    
    public void asignarIndex(SoAntecedentes antecedente, int dlg, int LND) {
        antecedenteSeleccionado = antecedente;
        tipoActualizacion = LND;
        if (dlg == 0) {
            getLovTiposAntecedentes();
            contarRegistrosLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposAntecedentesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposAntecedentesDialogo').show()");
        } 
    } 
     
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (antecedenteSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(antecedenteSeleccionado.getSecuencia(), "SOANTECEDENTES");
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
        } else if (administrarRastros.verificarHistoricosTabla("SOANTECEDENTES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }
    
    
    public void actualizarTiposAntecedentes() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
                antecedenteSeleccionado.setTipoantecedente(tipoAntecedenteSeleccionado);
                if (!listAntecedentesCrear.contains(antecedenteSeleccionado)) {
                    if (listAntecedentesModificar.isEmpty()) {
                        listAntecedentesModificar.add(antecedenteSeleccionado);
                    } else if (!listAntecedentesModificar.contains(antecedenteSeleccionado)) {
                        listAntecedentesModificar.add(antecedenteSeleccionado);
                    }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosAntecedentes");
        } else if (tipoActualizacion == 1) {
            nuevoAntecedente.setTipoantecedente(tipoAntecedenteSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAntecedente");
        } else if (tipoActualizacion == 2) {
            duplicarAntecedente.setTipoantecedente(tipoAntecedenteSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAntecedente");
        }
        lovTiposAntecedentesFiltrar = null;
        tipoAntecedenteSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        //cualCelda = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposAntecedentesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTipoAntecedente");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTA");
        context.reset("formularioDialogos:lovTipoAntecedente:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoAntecedente').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposAntecedentesDialogo').hide()");
    }

    
    public void cancelarCambioTiposAntecedentes() {
        lovTiposAntecedentesFiltrar = null;
        tipoAntecedenteSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposAntecedentesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTipoAntecedente");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTA");
        context.reset("formularioDialogos:lovTipoAntecedente:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoAntecedente').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposAntecedentesDialogo').hide()");
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

    public void contarRegistrosLov() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLov");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    /////////GETS Y SETS ////////////
    public List<SoAntecedentes> getListAntecedentes() {
        if(listAntecedentes == null){
           listAntecedentes = administrarAntecedentes.consultarAntecedentes();
        }
        return listAntecedentes;
    }

    public void setListAntecedentes(List<SoAntecedentes> listAntecedentes) {
        this.listAntecedentes = listAntecedentes;
    }

    public List<SoAntecedentes> getListAntecedentesFiltrar() {
        return listAntecedentesFiltrar;
    }

    public void setListAntecedentesFiltrar(List<SoAntecedentes> listAntecedentesFiltrar) {
        this.listAntecedentesFiltrar = listAntecedentesFiltrar;
    }

    public SoAntecedentes getAntecedenteSeleccionado() {
        return antecedenteSeleccionado;
    }

    public void setAntecedenteSeleccionado(SoAntecedentes antecedenteSeleccionado) {
        this.antecedenteSeleccionado = antecedenteSeleccionado;
    }

    public SoAntecedentes getNuevoAntecedente() {
        return nuevoAntecedente;
    }

    public void setNuevoAntecedente(SoAntecedentes nuevoAntecedente) {
        this.nuevoAntecedente = nuevoAntecedente;
    }

    public SoAntecedentes getDuplicarAntecedente() {
        return duplicarAntecedente;
    }

    public void setDuplicarAntecedente(SoAntecedentes duplicarAntecedente) {
        this.duplicarAntecedente = duplicarAntecedente;
    }

    public SoAntecedentes getEditarAntecedente() {
        return editarAntecedente;
    }

    public void setEditarAntecedente(SoAntecedentes editarAntecedente) {
        this.editarAntecedente = editarAntecedente;
    }

    public List<SoTiposAntecedentes> getLovTiposAntecedentes() {
        if(lovTiposAntecedentes == null){
            lovTiposAntecedentes = administrarAntecedentes.consultarTiposAntecedentes();
        }
        return lovTiposAntecedentes;
    }

    public void setLovTiposAntecedentes(List<SoTiposAntecedentes> lovTiposAntecedentes) {
        this.lovTiposAntecedentes = lovTiposAntecedentes;
    }

    public List<SoTiposAntecedentes> getLovTiposAntecedentesFiltrar() {
        return lovTiposAntecedentesFiltrar;
    }

    public void setLovTiposAntecedentesFiltrar(List<SoTiposAntecedentes> lovTiposAntecedentesFiltrar) {
        this.lovTiposAntecedentesFiltrar = lovTiposAntecedentesFiltrar;
    }

    public SoTiposAntecedentes getTipoAntecedenteSeleccionado() {
        return tipoAntecedenteSeleccionado;
    }

    public void setTipoAntecedenteSeleccionado(SoTiposAntecedentes tipoAntecedenteSeleccionado) {
        this.tipoAntecedenteSeleccionado = tipoAntecedenteSeleccionado;
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosAntecedentes");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroLov() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTipoAntecedente");
        infoRegistroLov = String.valueOf(tabla.getRowCount());
        return infoRegistroLov;
    }

    public void setInfoRegistroLov(String infoRegistroLov) {
        this.infoRegistroLov = infoRegistroLov;
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
