/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Aficiones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarAficionesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
@Named(value = "controlAficiones")
@SessionScoped
public class ControlAficiones implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarAficionesInterface administrarAficiones;

    private List<Aficiones> listAficiones;
    private List<Aficiones> filtrarAficiones;
    private List<Aficiones> crearAficiones;
    private List<Aficiones> borrarAficiones;
    private List<Aficiones> modificarAficiones;
    private Aficiones nuevaAficion;
    private Aficiones duplicarAficion;
    private Aficiones editarAficion;
    private Aficiones aficionSeleccionada;
    private int tamano;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private boolean permitirIndex;
    private Column codigo, descripcion;
    private int registrosBorrados;
    private String mensajeValidacion;
    private Integer a;
    private String infoRegistro, paginaanterior;
    private DataTable tablaC;
    private boolean activarLOV;
    private BigInteger verificarBorradoVigenciasDeportes;
    private BigInteger contadorDeportesPersonas;
    private BigInteger contadorParametrosInformes;

    public ControlAficiones() {
        listAficiones = null;
        crearAficiones = new ArrayList<Aficiones>();
        modificarAficiones = new ArrayList<Aficiones>();
        borrarAficiones = new ArrayList<Aficiones>();
        permitirIndex = true;
        editarAficion = new Aficiones();
        nuevaAficion = new Aficiones();
        duplicarAficion = new Aficiones();
        a = null;
        guardado = true;
        tamano = 270;
        paginaanterior = "";
        activarLOV = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarAficiones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPag(String pagina) {
        paginaanterior = pagina;
        listAficiones = null;
        getListAficiones();
        aficionSeleccionada = listAficiones.get(0);
    }

    public String retornarPagina() {
        return paginaanterior;
    }

    public void cambiarIndice(Aficiones aficion, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            aficionSeleccionada = aficion;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    aficionSeleccionada.getCodigo();
                }
                if (cualCelda == 1) {
                    aficionSeleccionada.getDescripcion();
                }
            } else {
                if (cualCelda == 0) {
                    aficionSeleccionada.getCodigo();
                }
                if (cualCelda == 1) {
                    aficionSeleccionada.getDescripcion();
                }
                aficionSeleccionada.getSecuencia();
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosAficion:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosAficion:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            //contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosAficion");
            bandera = 0;
            filtrarAficiones = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarAficiones.clear();
        crearAficiones.clear();
        modificarAficiones.clear();
        k = 0;
        listAficiones = null;
        aficionSeleccionada = null;
        guardado = true;
        permitirIndex = true;
        getListAficiones();
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:infoRegistro");
        RequestContext.getCurrentInstance().update("form:datosAficion");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosAficion:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosAficion:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosAficion");
            bandera = 0;
            filtrarAficiones = null;
            tipoLista = 0;
        }

        borrarAficiones.clear();
        crearAficiones.clear();
        modificarAficiones.clear();
        aficionSeleccionada = null;
        k = 0;
        listAficiones = null;
        guardado = true;
        permitirIndex = true;
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosAficion");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosAficion:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosAficion:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosAficion");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosAficion:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosAficion:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosAficion");
            bandera = 0;
            filtrarAficiones = null;
            tipoLista = 0;
        }
    }

    public void modificarAficion(Aficiones aficion, String confirmarCambio, String valorConfirmar) {
        aficionSeleccionada = aficion;
        int contador = 0;
        String codigoVacio = null;
        boolean coincidencias = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {

            if (aficionSeleccionada.getCodigo() == 0) {
                mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                coincidencias = false;
                aficionSeleccionada.setCodigo(aficionSeleccionada.getCodigo());
            } else {

                for (int j = 0; j < listAficiones.size(); j++) {
                    if (aficionSeleccionada.getSecuencia() != listAficiones.get(j).getSecuencia()) {
                        if (aficionSeleccionada.getCodigo() == (listAficiones.get(j).getCodigo())) {
                            contador++;
                        }
                    }
                }
                if (contador > 0) {
                    mensajeValidacion = "CODIGOS REPETIDOS";
                    coincidencias = false;
                    aficionSeleccionada.setCodigo(aficionSeleccionada.getCodigo());
                } else {
                    coincidencias = true;
                }
            }
        }

        if (confirmarCambio.equalsIgnoreCase("M")) {

            if (aficionSeleccionada.getDescripcion() == null || aficionSeleccionada.getDescripcion().isEmpty()) {
                mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                aficionSeleccionada.setDescripcion(aficionSeleccionada.getDescripcion());
                coincidencias = false;
            }
            for (int j = 0; j < listAficiones.size(); j++) {
                if (aficionSeleccionada.getSecuencia() != listAficiones.get(j).getSecuencia()) {
                    if (aficionSeleccionada.getDescripcion().equals(listAficiones.get(j).getDescripcion())) {
                        contador++;
                    }
                }
            }
            if (contador > 0) {
                mensajeValidacion = "MOTIVO REPETIDOS";
                coincidencias = false;
                aficionSeleccionada.setCodigo(aficionSeleccionada.getCodigo());
            } else {
                coincidencias = true;
            }
        }
        if (coincidencias == true) {
            if (!crearAficiones.contains(aficionSeleccionada)) {
                if (!modificarAficiones.contains(aficionSeleccionada)) {
                    modificarAficiones.add(aficionSeleccionada);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().update("form:validacionModificar");
            RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
        }
        RequestContext.getCurrentInstance().update("form:datosAficion");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrarAficion() {

        RequestContext context = RequestContext.getCurrentInstance();
        if (aficionSeleccionada != null) {

            if (tipoLista == 0) {
                System.out.println("Entro a BorrarDeporte");
                if (!modificarAficiones.isEmpty() && modificarAficiones.contains(aficionSeleccionada)) {
                    modificarAficiones.remove(modificarAficiones.indexOf(aficionSeleccionada));
                    borrarAficiones.add(aficionSeleccionada);
                } else if (!crearAficiones.isEmpty() && crearAficiones.contains(aficionSeleccionada)) {
                    crearAficiones.remove(crearAficiones.indexOf(aficionSeleccionada));
                } else {
                    borrarAficiones.add(aficionSeleccionada);
                }
                listAficiones.remove(aficionSeleccionada);
            }
            if (tipoLista == 1) {
                filtrarAficiones.remove(aficionSeleccionada);
                listAficiones.remove(aficionSeleccionada);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosAficion");
            aficionSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void guardarAficiones() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            if (!borrarAficiones.isEmpty()) {
                administrarAficiones.borrarAficiones(borrarAficiones);
                //mostrarBorrados
                registrosBorrados = borrarAficiones.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarAficiones.clear();
            }
            if (!crearAficiones.isEmpty()) {
                administrarAficiones.crearAficiones(crearAficiones);
                crearAficiones.clear();
            }
            if (!modificarAficiones.isEmpty()) {
                administrarAficiones.modificarAficiones(modificarAficiones);
                modificarAficiones.clear();
            }
            listAficiones = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosAficion");
            k = 0;
        }
        aficionSeleccionada = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (aficionSeleccionada != null) {
            if (tipoLista == 0) {
                editarAficion = aficionSeleccionada;
            }
            if (tipoLista == 1) {
                editarAficion = aficionSeleccionada;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
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
            RequestContext.getCurrentInstance().execute("seleccionarRegistro').show()");
        }
    }

    public void agregarNuevaAficion() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaAficion.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevaAficion.getCodigo());

            for (int x = 0; x < listAficiones.size(); x++) {
                if (listAficiones.get(x).getCodigo() == nuevaAficion.getCodigo()) {
                    duplicados++;
                }
            }
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevaAficion.getDescripcion() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                tamano = 270;
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosAficion:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosAficion:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosAficion");
                bandera = 0;
                filtrarAficiones = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevaAficion.setSecuencia(l);
            crearAficiones.add(nuevaAficion);
            listAficiones.add(nuevaAficion);
            aficionSeleccionada = nuevaAficion;
            nuevaAficion = new Aficiones();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosAficion");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroDeporte').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaAficion");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaAficion').show()");
            contador = 0;
        }
    }

    public void limpiarNuevaAficion() {
        nuevaAficion = new Aficiones();
        aficionSeleccionada = null;
    }

    public void duplicarAficiones() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (aficionSeleccionada != null) {
            duplicarAficion = new Aficiones();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarAficion.setSecuencia(l);
                duplicarAficion.setCodigo(aficionSeleccionada.getCodigo());
                duplicarAficion.setDescripcion(aficionSeleccionada.getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarAficion.setSecuencia(l);
                duplicarAficion.setCodigo(aficionSeleccionada.getCodigo());
                duplicarAficion.setDescripcion(aficionSeleccionada.getDescripcion());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDepor");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroDeporte').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR CONTROLDEPORTES");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarAficion.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "Existen campos vacios \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listAficiones.size(); x++) {
                if (listAficiones.get(x).getCodigo() == duplicarAficion.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " No pueden existir códigos repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarAficion.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "Existen campos vacíos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            if (crearAficiones.contains(duplicarAficion)) {
            }
            listAficiones.add(duplicarAficion);
            crearAficiones.add(duplicarAficion);
            RequestContext.getCurrentInstance().update("form:datosAficion");
            aficionSeleccionada = duplicarAficion;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                tamano = 270;
                codigo = (Column) c.getViewRoot().findComponent("form:datosAficion:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosAficion:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosAficion");
                bandera = 0;
                filtrarAficiones = null;
                tipoLista = 0;
            }
            duplicarAficion = new Aficiones();
            contarRegistros();
            RequestContext.getCurrentInstance().execute("duplicarRegistroDeporte').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarAficion");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarAficion').show()");
        }
    }

    public void limpiarDuplicarAficiones() {
        duplicarAficion = new Aficiones();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAficionExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "AFICIONES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAficionExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "AFICIONES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (aficionSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(aficionSeleccionada.getSecuencia(), "AFICIONES");
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
        } else if (administrarRastros.verificarHistoricosTabla("AFICIONES")) { // igual acá
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

//    public void recordarSeleccionAficion() {
//        if (aficionSeleccionada != null) {
//            FacesContext c = FacesContext.getCurrentInstance();
//            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosAficion");
//            tablaC.setSelection(aficionSeleccionada);
//        }
//    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    /////////////GETS Y SETS//////////
    public List<Aficiones> getListAficiones() {
        if(listAficiones == null){
            listAficiones = administrarAficiones.consultarAficiones();
        }
        return listAficiones;
    }

    public void setListAficiones(List<Aficiones> listAficiones) {
        this.listAficiones = listAficiones;
    }

    public List<Aficiones> getFiltrarAficiones() {
        return filtrarAficiones;
    }

    public void setFiltrarAficiones(List<Aficiones> filtrarAficiones) {
        this.filtrarAficiones = filtrarAficiones;
    }

    public Aficiones getNuevaAficion() {
        return nuevaAficion;
    }

    public void setNuevaAficion(Aficiones nuevaAficion) {
        this.nuevaAficion = nuevaAficion;
    }

    public Aficiones getDuplicarAficion() {
        return duplicarAficion;
    }

    public void setDuplicarAficion(Aficiones duplicarAficion) {
        this.duplicarAficion = duplicarAficion;
    }

    public Aficiones getEditarAficion() {
        return editarAficion;
    }

    public void setEditarAficion(Aficiones editarAficion) {
        this.editarAficion = editarAficion;
    }

    public Aficiones getAficionSeleccionada() {
        return aficionSeleccionada;
    }

    public void setAficionSeleccionada(Aficiones aficionSeleccionada) {
        this.aficionSeleccionada = aficionSeleccionada;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
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

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getInfoRegistro() {
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

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    
}
