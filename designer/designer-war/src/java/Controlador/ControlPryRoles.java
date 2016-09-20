/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.PryRoles;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPryRolesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
@Named(value = "controlPryRoles")
@SessionScoped
public class ControlPryRoles implements Serializable {

    @EJB
    AdministrarPryRolesInterface administrarPryRoles;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<PryRoles> listPryRoles;
    private List<PryRoles> filtarListPryRoles;
    private List<PryRoles> crearListPryRoles;
    private List<PryRoles> modificarListPryRoles;
    private List<PryRoles> borrarListPryRoles;
    private PryRoles pryRolSeleccionado;
    private PryRoles nuevopryRol;
    private PryRoles editarpryRol;
    private PryRoles duplicarpryRol;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigDecimal l;
    private boolean aceptar, guardado;
    private boolean permitirIndex;
    private Column codigo, descripcion;
    private int registrosBorrados;
    private String mensajeValidacion, paginaanterior;
    private int tamano;
    private boolean activarLov;
    private String infoRegistro;
    private DataTable tablaC;

    public ControlPryRoles() {
        listPryRoles = null;
        crearListPryRoles = new ArrayList<PryRoles>();
        borrarListPryRoles = new ArrayList<PryRoles>();
        modificarListPryRoles = new ArrayList<PryRoles>();
        permitirIndex = true;
        editarpryRol = new PryRoles();
        nuevopryRol = new PryRoles();
        duplicarpryRol = new PryRoles();
        guardado = true;
        tamano = 270;
        cualCelda = -1;
        pryRolSeleccionado = null;
        activarLov = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPryRoles.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaanterior = pagina;
        listPryRoles = null;
        getListPryRoles();
        deshabilitarBotonLov();
        if (listPryRoles != null) {
            pryRolSeleccionado = listPryRoles.get(0);
        }
        contarRegistros();
    }

    public String redirigir() {
        return paginaanterior;
    }

    public void cambiarIndice(PryRoles pryrol, int celda) {
        if (permitirIndex == true) {
            pryRolSeleccionado = pryrol;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    pryRolSeleccionado.getCodigo();
                } else if (cualCelda == 1) {
                    pryRolSeleccionado.getDescripcion();
                }
            } else if (cualCelda == 0) {
                pryRolSeleccionado.getCodigo();

            } else if (cualCelda == 1) {
                pryRolSeleccionado.getDescripcion();
            }
            pryRolSeleccionado.getSecuencia();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }
    
    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosPryRoles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPryRoles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtarListPryRoles = null;
            tamano = 270;
            RequestContext.getCurrentInstance().update("form:datosPryRoles");
            tipoLista = 0;
        }

        borrarListPryRoles.clear();
        crearListPryRoles.clear();
        modificarListPryRoles.clear();
        pryRolSeleccionado = null;
        contarRegistros();
        k = 0;
        listPryRoles = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosPryRoles");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }
    
    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosPryRoles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPryRoles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPryRoles");
            bandera = 0;
            filtarListPryRoles = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarListPryRoles.clear();
        crearListPryRoles.clear();
        modificarListPryRoles.clear();
        pryRolSeleccionado = null;
        k = 0;
        listPryRoles = null;
        guardado = true;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPryRoles:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPryRoles:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosPryRoles");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPryRoles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPryRoles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPryRoles");
            bandera = 0;
            filtarListPryRoles = null;
            tipoLista = 0;
        }
    }
    
    public void modificarPryRoles(PryRoles pryrol, String confirmarCambio, String valorConfirmar) {
        pryRolSeleccionado = pryrol;
        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!crearListPryRoles.contains(pryRolSeleccionado)) {
                    if (modificarListPryRoles.isEmpty()) {
                        modificarListPryRoles.add(pryRolSeleccionado);
                    } else if (!modificarListPryRoles.contains(pryRolSeleccionado)) {
                        modificarListPryRoles.add(pryRolSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else if (!crearListPryRoles.contains(pryRolSeleccionado)) {
                if (!crearListPryRoles.contains(pryRolSeleccionado)) {
                    if (modificarListPryRoles.isEmpty()) {
                        modificarListPryRoles.add(pryRolSeleccionado);
                    } else if (!modificarListPryRoles.contains(pryRolSeleccionado)) {
                        modificarListPryRoles.add(pryRolSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                RequestContext.getCurrentInstance().update("form:datosPryRoles");
            }
        }
    }

    public void borrandoPryRoles() {
        if (pryRolSeleccionado != null) {
            if (!modificarListPryRoles.isEmpty() && modificarListPryRoles.contains(pryRolSeleccionado)) {
                modificarListPryRoles.remove(modificarListPryRoles.indexOf(pryRolSeleccionado));
                borrarListPryRoles.add(pryRolSeleccionado);
            } else if (!crearListPryRoles.isEmpty() && crearListPryRoles.contains(pryRolSeleccionado)) {
                crearListPryRoles.remove(crearListPryRoles.indexOf(pryRolSeleccionado));
            } else {
                borrarListPryRoles.add(pryRolSeleccionado);
            }
            listPryRoles.remove(pryRolSeleccionado);
            if (tipoLista == 1) {
                filtarListPryRoles.remove(pryRolSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosPryRoles");
            modificarInfoRegistro(listPryRoles.size());
            pryRolSeleccionado = null;
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

        if (!borrarListPryRoles.isEmpty() || !crearListPryRoles.isEmpty() || !modificarListPryRoles.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarPryRoles() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!borrarListPryRoles.isEmpty()) {
                    administrarPryRoles.borrarPryRoles(borrarListPryRoles);
                    registrosBorrados = borrarListPryRoles.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarListPryRoles.clear();
                }
                if (!modificarListPryRoles.isEmpty()) {
                    administrarPryRoles.modificarPryRoles(modificarListPryRoles);
                    modificarListPryRoles.clear();
                }
                if (!crearListPryRoles.isEmpty()) {
                    administrarPryRoles.crearPryRoles(crearListPryRoles);
                    crearListPryRoles.clear();
                }
                listPryRoles = null;
                getListPryRoles();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                pryRolSeleccionado = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosPryRoles");
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }
    
    public void editarCelda() {
        if (pryRolSeleccionado != null) {
            editarpryRol = pryRolSeleccionado;

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

    public void agregarNuevoPryRoles() {
        System.out.println("agregarNuevoPryRoles");
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (nuevopryRol.getDescripcion().equals(" ") || nuevopryRol.getDescripcion().equals("")) {
            mensajeValidacion = mensajeValidacion + " * Descripción \n";
            contador++;
        }
        if (nuevopryRol.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + " * Codigo \n";
            contador++;
        }

        for (int i = 0; i < listPryRoles.size(); i++) {
            if (listPryRoles.get(i).getCodigo() == nuevopryRol.getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                duplicados++;
            }
            if (contador != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoPryRol");
                RequestContext.getCurrentInstance().execute("PF('validacionNuevoPryRol').show()");

            }
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosPryRoles:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosPryRoles:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtarListPryRoles = null;
                tipoLista = 0;
                tamano = 270;
                RequestContext.getCurrentInstance().update("form:datosPryRoles");
            }
            k++;
            l = BigDecimal.valueOf(k); 
            nuevopryRol.setSecuencia(l);
            crearListPryRoles.add(nuevopryRol);
            listPryRoles.add(nuevopryRol);
            modificarInfoRegistro(listPryRoles.size());
            pryRolSeleccionado = nuevopryRol;
            nuevopryRol = new PryRoles();
            RequestContext.getCurrentInstance().update("form:datosPryRoles");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPryRoles').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoPryRol");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoPryRol').show()");
        }
    }

    public void limpiarNuevoPryRoles() {
        nuevopryRol = new PryRoles();
    }

    public void duplicandoPryRoles() {
        if (pryRolSeleccionado != null) {
            duplicarpryRol = new PryRoles();
            k++;
            l = BigDecimal.valueOf(k);

            if (tipoLista == 0) {
                duplicarpryRol.setSecuencia(l);
                duplicarpryRol.setCodigo(pryRolSeleccionado.getCodigo());
                duplicarpryRol.setDescripcion(pryRolSeleccionado.getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarpryRol.setSecuencia(l);
                duplicarpryRol.setCodigo(pryRolSeleccionado.getCodigo());
                duplicarpryRol.setDescripcion(pryRolSeleccionado.getDescripcion());
                tamano = 270;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPryRoles').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        RequestContext context = RequestContext.getCurrentInstance();
        int contador = 0;

        for (int i = 0; i < listPryRoles.size(); i++) {
            if (duplicarpryRol.getCodigo() == listPryRoles.get(i).getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                contador++;
            }
        }

        if (contador == 0) {
            listPryRoles.add(duplicarpryRol);
            crearListPryRoles.add(duplicarpryRol);
            pryRolSeleccionado = duplicarpryRol;
            modificarInfoRegistro(listPryRoles.size());
            RequestContext.getCurrentInstance().update("form:datosPryRoles");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosPryRoles:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosPryRoles:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtarListPryRoles = null;
                RequestContext.getCurrentInstance().update("form:datosPryRoles");
                tipoLista = 0;
            }
            duplicarpryRol = new PryRoles();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroPryRoles");
        RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPryRoles').hide()");
    }

    public void limpiarDuplicarPryRoles() {
        duplicarpryRol = new PryRoles();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPryRolesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "PRYROLES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPryRolesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "PRYROLES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (pryRolSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(pryRolSeleccionado.getSecuencia().toBigInteger(), "TIPOSCURSOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSCURSOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
           RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void recordarSeleccion() {
        if (pryRolSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosPryRoles");
            tablaC.setSelection(pryRolSeleccionado);
        }
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            modificarInfoRegistro(listPryRoles.size());
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlPryRoles eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void modificarInfoRegistro(int valor){
        infoRegistro = String.valueOf(valor);
//        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }
    
    public void contarRegistros(){
        if(listPryRoles != null){
            modificarInfoRegistro(listPryRoles.size());
        } else{
            modificarInfoRegistro(0);
        }
    }
    
    public void deshabilitarBotonLov() {
        activarLov = true;
    }
    
    ///////gets y sets//////////
    public List<PryRoles> getListPryRoles() {
        if(listPryRoles == null){
            listPryRoles = administrarPryRoles.PryRoles();
        }
        return listPryRoles;
    }

    public void setListPryRoles(List<PryRoles> listPryRoles) {
        this.listPryRoles = listPryRoles;
    }

    public List<PryRoles> getFiltarListPryRoles() {
        return filtarListPryRoles;
    }

    public void setFiltarListPryRoles(List<PryRoles> filtarListPryRoles) {
        this.filtarListPryRoles = filtarListPryRoles;
    }

    public PryRoles getPryRolSeleccionado() {
        return pryRolSeleccionado;
    }

    public void setPryRolSeleccionado(PryRoles pryRolSeleccionado) {
        this.pryRolSeleccionado = pryRolSeleccionado;
    }

    public PryRoles getNuevopryRol() {
        return nuevopryRol;
    }

    public void setNuevopryRol(PryRoles nuevopryRol) {
        this.nuevopryRol = nuevopryRol;
    }

    public PryRoles getEditarpryRol() {
        return editarpryRol;
    }

    public void setEditarpryRol(PryRoles editarpryRol) {
        this.editarpryRol = editarpryRol;
    }

    public PryRoles getDuplicarpryRol() {
        return duplicarpryRol;
    }

    public void setDuplicarpryRol(PryRoles duplicarpryRol) {
        this.duplicarpryRol = duplicarpryRol;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
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

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    
    
}
