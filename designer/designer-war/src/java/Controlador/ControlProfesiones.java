/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Profesiones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarProfesionesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
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
@Named(value = "controlProfesiones")
@SessionScoped
public class ControlProfesiones implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarProfesionesInterface administrarProfesiones;

    private List<Profesiones> listaProfesiones;
    private List<Profesiones> filtradoListaProfesiones;
    private List<Profesiones> listaProfesionesCrear;
    private List<Profesiones> listaProfesionesBorrar;
    private List<Profesiones> listaProfesionesModificar;
    private Profesiones profesionSeleccionada;
    private Profesiones nuevaProfesion;
    private Profesiones duplicarProfesion;
    private Profesiones editarProfesion;
    private BigInteger l;
    private int k, bandera, tipoLista, cualCelda;
    private Column codigo, descripcion;
    private boolean aceptar, permitirIndex, guardado, activarLov;
    private String altoTabla, inforegistro, paginaanterior, mensajeValidacion;
    private DataTable tablaC;

    public ControlProfesiones() {
        listaProfesionesCrear = new ArrayList<Profesiones>();
        listaProfesionesBorrar = new ArrayList<Profesiones>();
        listaProfesionesModificar = new ArrayList<Profesiones>();
        permitirIndex = true;
        aceptar = true;
        tipoLista = 0;
        profesionSeleccionada = null;
        editarProfesion = new Profesiones();
        nuevaProfesion = new Profesiones();
        duplicarProfesion = new Profesiones();
        cualCelda = -1;
        altoTabla = "270";
        guardado = true;
        activarLov = true;
        listaProfesiones = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarRastros.obtenerConexion(ses.getId());
            administrarProfesiones.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaanterior = pagina;
        listaProfesiones = null;
        getListaProfesiones();
        deshabilitarBotonLov();
        if (listaProfesiones != null) {
            profesionSeleccionada = listaProfesiones.get(0);
        }
    }

    public String redirigir() {
        return paginaanterior;
    }

    public void editarCelda() {
        if (profesionSeleccionada != null) {
            editarProfesion = profesionSeleccionada;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigosProfesiones");
                RequestContext.getCurrentInstance().execute("PF('editarCodigosProfesiones').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionProfesiones");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionProfesiones').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void guardarCambiosProfesion() {
        try {
            if (guardado == false) {
                if (!listaProfesionesBorrar.isEmpty()) {
                    administrarProfesiones.borrar(listaProfesionesBorrar);
                    listaProfesionesBorrar.clear();
                }
                if (!listaProfesionesCrear.isEmpty()) {
                    administrarProfesiones.crear(listaProfesionesCrear);
                    listaProfesionesCrear.clear();
                }
                if (!listaProfesionesModificar.isEmpty()) {
                    administrarProfesiones.editar(listaProfesionesModificar);
                    listaProfesionesModificar.clear();
                }

                listaProfesiones = null;
                getListaProfesiones();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                profesionSeleccionada = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosProfesiones");
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosProfesiones:profesionesCodigos");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosProfesiones:profesionesNombres");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtradoListaProfesiones = null;
            tipoLista = 0;
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosProfesiones");
            tipoLista = 0;
        }

        listaProfesionesBorrar.clear();
        listaProfesionesCrear.clear();
        listaProfesionesModificar.clear();
        profesionSeleccionada = null;
        contarRegistros();
        k = 0;
        listaProfesiones = null;
        guardado = true;
        permitirIndex = true;
        guardado = true;

    }

    public void agregarNuevaProfesion() {
        int pasa = 0;
        int pasaA = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";
        if (nuevaProfesion.getDescripcion().equals(" ") || nuevaProfesion.getDescripcion().equals("")) {
            mensajeValidacion = mensajeValidacion + " * Descripción profesiones \n";
            pasa++;
        }
        if (nuevaProfesion.getCodigo() == 0) {
            mensajeValidacion = mensajeValidacion + " * Código \n";
            pasa++;
        }

        if (listaProfesiones != null) {

            for (int i = 0; i < listaProfesiones.size(); i++) {

                if (listaProfesiones.get(i).getDescripcion().equals(nuevaProfesion.getDescripcion())) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:existeNombre");
                    RequestContext.getCurrentInstance().execute("PF('existeNombre').show()");
                    pasaA++;
                }
                if (pasa != 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaProfesion");
                    RequestContext.getCurrentInstance().execute("PF('validacionNuevaProfesion').show()");

                }
            }

            for (int i = 0; i < listaProfesiones.size(); i++) {
                System.out.println("Codigos: " + listaProfesiones.get(i).getCodigo());
                if (listaProfesiones.get(i).getCodigo() == nuevaProfesion.getCodigo()) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                    RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                    pasaA++;
                }
                if (pasa != 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaProfesion");
                    RequestContext.getCurrentInstance().execute("PF('validacionNuevaProfesion').show()");

                }
            }
        }

        if (nuevaProfesion.getDescripcion().length() > 40) {
            RequestContext.getCurrentInstance().update("formularioDialogos:sobrepasaCaracteres");
            RequestContext.getCurrentInstance().execute("PF('sobrepasaCaracteres').show()");
            pasa++;
        }

        if (pasa == 0 && pasaA == 0) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosProfesiones:profesionesCodigos");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosProfesiones:profesionesNombres");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtradoListaProfesiones = null;
                tipoLista = 0;
                altoTabla = "270";
                RequestContext.getCurrentInstance().update("form:datosProfesiones");
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA CIUDADES.
            k++;
            l = BigInteger.valueOf(k);
            nuevaProfesion.setSecuencia(l);
            listaProfesionesCrear.add(nuevaProfesion);
            listaProfesiones.add(nuevaProfesion);
            contarRegistros();
            profesionSeleccionada = nuevaProfesion;
            nuevaProfesion = new Profesiones();

            RequestContext.getCurrentInstance().update("form:datosProfesiones");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroProfesion').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaProfesion");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaProfesion').show()");
        }
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        if (bandera == 0) {
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosProfesiones:profesionesCodigos");
            codigo.setFilterStyle("width: 85%");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosProfesiones:profesionesNombres");
            descripcion.setFilterStyle("width: 85%");
            altoTabla = "250";
            RequestContext.getCurrentInstance().update("form:datosProfesiones");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosProfesiones:profesionesCodigos");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosProfesiones:profesionesNombres");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosProfesiones");
            bandera = 0;
            filtradoListaProfesiones = null;
            tipoLista = 0;
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosProfesionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ProfesionesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosProfesionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ProfesionesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void limpiarNuevaProfesion() {
        nuevaProfesion = new Profesiones();
    }

    public void limpiarduplicarProfesion() {
        duplicarProfesion = new Profesiones();
    }

    public void borrarProfesiones() {

        if (profesionSeleccionada != null) {

            if (!listaProfesionesModificar.isEmpty() && listaProfesionesModificar.contains(profesionSeleccionada)) {
                listaProfesionesModificar.remove(listaProfesionesModificar.indexOf(profesionSeleccionada));
                listaProfesionesBorrar.add(profesionSeleccionada);
            } else if (!listaProfesionesCrear.isEmpty() && listaProfesionesCrear.contains(profesionSeleccionada)) {
                listaProfesionesCrear.remove(listaProfesionesCrear.indexOf(profesionSeleccionada));
            } else {
                listaProfesionesBorrar.add(profesionSeleccionada);
            }
            listaProfesiones.remove(profesionSeleccionada);

            if (tipoLista == 1) {
                filtradoListaProfesiones.remove(profesionSeleccionada);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosProfesiones");
            contarRegistros();
            profesionSeleccionada = null;
            guardado = true;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void cambiarIndice(Profesiones profesion, int celda) {
        profesionSeleccionada = profesion;
        System.out.println("profesion seleccionada : " + profesionSeleccionada);
        if (permitirIndex == true) {
            cualCelda = celda;
            if (cualCelda == 0) {
                profesionSeleccionada.getCodigo();

            } else if (cualCelda == 1) {
                profesionSeleccionada.getDescripcion();
            }
        }
    }

    public void duplicarProfesiones() {
        if (profesionSeleccionada != null) {
            duplicarProfesion = new Profesiones();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarProfesion.setSecuencia(l);
                duplicarProfesion.setCodigo(profesionSeleccionada.getCodigo());
                duplicarProfesion.setDescripcion(profesionSeleccionada.getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarProfesion.setSecuencia(l);
                duplicarProfesion.setCodigo(profesionSeleccionada.getCodigo());
                duplicarProfesion.setDescripcion(profesionSeleccionada.getDescripcion());
                altoTabla = "270";
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProfesion");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroProfesion').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {

        RequestContext context = RequestContext.getCurrentInstance();
        int pasa = 0;

        for (int i = 0; i < listaProfesiones.size(); i++) {
            if (duplicarProfesion.getDescripcion().equals(listaProfesiones.get(i).getDescripcion())) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeNombre");
                RequestContext.getCurrentInstance().execute("PF('existeNombre').show()");
                pasa++;
            }
            if (duplicarProfesion.getCodigo() == listaProfesiones.get(i).getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                pasa++;
            }
        }

        if (pasa == 0) {

            listaProfesiones.add(duplicarProfesion);
            listaProfesionesCrear.add(duplicarProfesion);
            profesionSeleccionada = duplicarProfesion;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosProfesiones");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                System.out.println("Desactivar");
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosProfesiones:profesionesCodigos");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosProfesiones:profesionesNombres");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtradoListaProfesiones = null;
                tipoLista = 0;
                RequestContext.getCurrentInstance().update("form:datosProfesiones");
                tipoLista = 0;
            }
            duplicarProfesion = new Profesiones();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroProfesion");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroProfesion').hide()");
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosProfesiones:profesionesCodigos");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosProfesiones:profesionesNombres");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtradoListaProfesiones = null;
            tipoLista = 0;
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosProfesiones");

        }

        listaProfesionesBorrar.clear();
        listaProfesionesCrear.clear();
        listaProfesionesModificar.clear();
        contarRegistros();
        profesionSeleccionada = null;
        k = 0;
        listaProfesiones = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosProfesiones");
    }

    public void modificarProfesiones(Profesiones profesion, String confirmarCambio, String valorConfirmar) {
        profesionSeleccionada = profesion;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaProfesionesCrear.contains(profesionSeleccionada)) {
                    if (listaProfesionesModificar.isEmpty()) {
                        listaProfesionesModificar.add(profesionSeleccionada);
                    } else if (!listaProfesionesModificar.contains(profesionSeleccionada)) {
                        listaProfesionesModificar.add(profesionSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else if (!listaProfesionesCrear.contains(profesionSeleccionada)) {

                if (listaProfesionesModificar.isEmpty()) {
                    listaProfesionesModificar.add(profesionSeleccionada);
                } else if (!listaProfesionesModificar.contains(profesionSeleccionada)) {
                    listaProfesionesModificar.add(profesionSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            RequestContext.getCurrentInstance().update("form:datosProfesiones");
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (profesionSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(profesionSeleccionada.getSecuencia(), "TIPOSEDUCACIONES");
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

    public void recordarSeleccionTT() {
        if (profesionSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosProfesiones");
            tablaC.setSelection(profesionSeleccionada);
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

    ////////SETS Y GETS ////////////
    public List<Profesiones> getListaProfesiones() {
        if (listaProfesiones == null) {
            listaProfesiones = administrarProfesiones.Profesiones();
        }
        return listaProfesiones;
    }

    public void setListaProfesiones(List<Profesiones> listaProfesiones) {
        this.listaProfesiones = listaProfesiones;
    }

    public List<Profesiones> getFiltradoListaProfesiones() {
        return filtradoListaProfesiones;
    }

    public void setFiltradoListaProfesiones(List<Profesiones> filtradoListaProfesiones) {
        this.filtradoListaProfesiones = filtradoListaProfesiones;
    }

    public Profesiones getProfesionSeleccionada() {
        return profesionSeleccionada;
    }

    public void setProfesionSeleccionada(Profesiones profesionSeleccionada) {
        this.profesionSeleccionada = profesionSeleccionada;
    }

    public Profesiones getNuevaProfesion() {
        return nuevaProfesion;
    }

    public void setNuevaProfesion(Profesiones nuevaProfesion) {
        this.nuevaProfesion = nuevaProfesion;
    }

    public Profesiones getDuplicarProfesion() {
        return duplicarProfesion;
    }

    public void setDuplicarProfesion(Profesiones duplicarProfesion) {
        this.duplicarProfesion = duplicarProfesion;
    }

    public Profesiones getEditarProfesion() {
        return editarProfesion;
    }

    public void setEditarProfesion(Profesiones editarProfesion) {
        this.editarProfesion = editarProfesion;
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosProfesiones");
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
