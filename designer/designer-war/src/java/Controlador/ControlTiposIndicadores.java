/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposIndicadores;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposIndicadoresInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
public class ControlTiposIndicadores implements Serializable {

    @EJB
    AdministrarTiposIndicadoresInterface administrarTiposIndicadores;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposIndicadores> listTiposIndicadores;
    private List<TiposIndicadores> filtrarTiposIndicadores;
    private List<TiposIndicadores> crearTiposIndicadores;
    private List<TiposIndicadores> modificarTiposIndicadores;
    private List<TiposIndicadores> borrarTiposIndicadores;
    private TiposIndicadores nuevoTiposIndicadores;
    private TiposIndicadores duplicarTiposIndicadores;
    private TiposIndicadores editarTiposIndicadores;
    private TiposIndicadores tiposindicadoresSeleccionado;
    //otros
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
    private Integer backUpCodigo;
    private String backUpDescripcion,paginaanterior;

    public ControlTiposIndicadores() {
        listTiposIndicadores = null;
        crearTiposIndicadores = new ArrayList<TiposIndicadores>();
        modificarTiposIndicadores = new ArrayList<TiposIndicadores>();
        borrarTiposIndicadores = new ArrayList<TiposIndicadores>();
        permitirIndex = true;
        editarTiposIndicadores = new TiposIndicadores();
        nuevoTiposIndicadores = new TiposIndicadores();
        duplicarTiposIndicadores = new TiposIndicadores();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlTiposIndicadores PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposIndicadores.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    
    public void recibirPagina(String pagina){
        paginaanterior = pagina;
        listTiposIndicadores = null;
        getListTiposIndicadores();
        if(listTiposIndicadores != null){
            if(!listTiposIndicadores.isEmpty()){
                tiposindicadoresSeleccionado = listTiposIndicadores.get(0);
            }
        }
    }
    
    public String retornarPagina(){
        return paginaanterior;
    }
    
    public void cambiarIndice(TiposIndicadores tipo, int celda) {
        if (permitirIndex == true) {
            tiposindicadoresSeleccionado = tipo;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = tiposindicadoresSeleccionado.getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = tiposindicadoresSeleccionado.getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                tiposindicadoresSeleccionado.getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = tiposindicadoresSeleccionado.getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = tiposindicadoresSeleccionado.getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                tiposindicadoresSeleccionado.getSecuencia();
            }

        }
    }

    public void asignarIndex(TiposIndicadores tipo, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposIndicadores.asignarIndex \n");
            tiposindicadoresSeleccionado = tipo;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlTiposIndicadores.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }
    private String infoRegistro;

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
            bandera = 0;
            filtrarTiposIndicadores = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposIndicadores.clear();
        crearTiposIndicadores.clear();
        modificarTiposIndicadores.clear();
        tiposindicadoresSeleccionado = null;
        tiposindicadoresSeleccionado = null;
        k = 0;
        listTiposIndicadores = null;
        guardado = true;
        permitirIndex = true;
        getListTiposIndicadores();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
            bandera = 0;
            filtrarTiposIndicadores = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposIndicadores.clear();
        crearTiposIndicadores.clear();
        modificarTiposIndicadores.clear();
        tiposindicadoresSeleccionado = null;
        tiposindicadoresSeleccionado = null;
        k = 0;
        listTiposIndicadores = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
            bandera = 0;
            filtrarTiposIndicadores = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposIndicadores(TiposIndicadores tipo, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        tiposindicadoresSeleccionado = tipo;

        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearTiposIndicadores.contains(tiposindicadoresSeleccionado)) {
                        if (modificarTiposIndicadores.isEmpty()) {
                            modificarTiposIndicadores.add(tiposindicadoresSeleccionado);
                        } else if (!modificarTiposIndicadores.contains(tiposindicadoresSeleccionado)) {
                            modificarTiposIndicadores.add(tiposindicadoresSeleccionado);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                } else {
                    if (tiposindicadoresSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        tiposindicadoresSeleccionado.setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposIndicadores.size(); j++) {
                            if (tiposindicadoresSeleccionado.getCodigo() == listTiposIndicadores.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        tiposindicadoresSeleccionado.setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }
                if (tiposindicadoresSeleccionado.getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    tiposindicadoresSeleccionado.setDescripcion(backUpDescripcion);
                }
                if (tiposindicadoresSeleccionado.getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    tiposindicadoresSeleccionado.setDescripcion(backUpDescripcion);
                }

                if (banderita == true) {

                    if (guardado == true) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
            }
        } 
        RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoTiposIndicadores() {

        if (tiposindicadoresSeleccionado != null) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposIndicadores");
                if (!modificarTiposIndicadores.isEmpty() && modificarTiposIndicadores.contains(tiposindicadoresSeleccionado)) {
                    int modIndex = modificarTiposIndicadores.indexOf(tiposindicadoresSeleccionado);
                    modificarTiposIndicadores.remove(modIndex);
                    borrarTiposIndicadores.add(tiposindicadoresSeleccionado);
                } else if (!crearTiposIndicadores.isEmpty() && crearTiposIndicadores.contains(tiposindicadoresSeleccionado)) {
                    int crearIndex = crearTiposIndicadores.indexOf(tiposindicadoresSeleccionado);
                    crearTiposIndicadores.remove(crearIndex);
                } else {
                    borrarTiposIndicadores.add(tiposindicadoresSeleccionado);
                }
                listTiposIndicadores.remove(tiposindicadoresSeleccionado);
            }
            if (tipoLista == 1) {
                filtrarTiposIndicadores.remove(tiposindicadoresSeleccionado);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
            contarRegistros();
            tiposindicadoresSeleccionado = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        BigInteger contarVigenciasIndicadoresTipoIndicador;

        try {
            System.err.println("Control Secuencia de ControlTiposIndicadores ");
            if (tipoLista == 0) {
                contarVigenciasIndicadoresTipoIndicador = administrarTiposIndicadores.contarVigenciasIndicadoresTipoIndicador(tiposindicadoresSeleccionado.getSecuencia());
            } else {
                contarVigenciasIndicadoresTipoIndicador = administrarTiposIndicadores.contarVigenciasIndicadoresTipoIndicador(tiposindicadoresSeleccionado.getSecuencia());
            }
            if (contarVigenciasIndicadoresTipoIndicador.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposIndicadores();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                tiposindicadoresSeleccionado = null;
                contarVigenciasIndicadoresTipoIndicador = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposIndicadores verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposIndicadores.isEmpty() || !crearTiposIndicadores.isEmpty() || !modificarTiposIndicadores.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTiposIndicadores() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarTiposIndicadores");
            if (!borrarTiposIndicadores.isEmpty()) {
                administrarTiposIndicadores.borrarTiposIndicadores(borrarTiposIndicadores);
                //mostrarBorrados
                registrosBorrados = borrarTiposIndicadores.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarTiposIndicadores.clear();
            }
            if (!modificarTiposIndicadores.isEmpty()) {
                administrarTiposIndicadores.modificarTiposIndicadores(modificarTiposIndicadores);
                modificarTiposIndicadores.clear();
            }
            if (!crearTiposIndicadores.isEmpty()) {
                administrarTiposIndicadores.crearTiposIndicadores(crearTiposIndicadores);
                crearTiposIndicadores.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposIndicadores = null;
            RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        tiposindicadoresSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (tiposindicadoresSeleccionado != null) {
            if (tipoLista == 0) {
                editarTiposIndicadores = tiposindicadoresSeleccionado;
            }
            if (tipoLista == 1) {
                editarTiposIndicadores = tiposindicadoresSeleccionado;
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
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoTiposIndicadores() {
        System.out.println("agregarNuevoTiposIndicadores");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposIndicadores.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTiposIndicadores.getCodigo());

            for (int x = 0; x < listTiposIndicadores.size(); x++) {
                if (listTiposIndicadores.get(x).getCodigo() == nuevoTiposIndicadores.getCodigo()) {
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
        if (nuevoTiposIndicadores.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoTiposIndicadores.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
                bandera = 0;
                filtrarTiposIndicadores = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposIndicadores.setSecuencia(l);
            crearTiposIndicadores.add(nuevoTiposIndicadores);
            listTiposIndicadores.add(nuevoTiposIndicadores);
            tiposindicadoresSeleccionado = nuevoTiposIndicadores;
            nuevoTiposIndicadores = new TiposIndicadores();
            RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
            contarRegistros();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposIndicadores').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposIndicadores() {
        System.out.println("limpiarNuevoTiposIndicadores");
        nuevoTiposIndicadores = new TiposIndicadores();

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposIndicadores() {
        System.out.println("duplicandoTiposIndicadores");
        if (tiposindicadoresSeleccionado != null) {
            duplicarTiposIndicadores = new TiposIndicadores();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposIndicadores.setSecuencia(l);
                duplicarTiposIndicadores.setCodigo(tiposindicadoresSeleccionado.getCodigo());
                duplicarTiposIndicadores.setDescripcion(tiposindicadoresSeleccionado.getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarTiposIndicadores.setSecuencia(l);
                duplicarTiposIndicadores.setCodigo(tiposindicadoresSeleccionado.getCodigo());
                duplicarTiposIndicadores.setDescripcion(tiposindicadoresSeleccionado.getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposIndicadores').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        Integer a = 0;
        a = null;

        if (duplicarTiposIndicadores.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposIndicadores.size(); x++) {
                if (listTiposIndicadores.get(x).getCodigo() == duplicarTiposIndicadores.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarTiposIndicadores.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarTiposIndicadores.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 2) {
            if (crearTiposIndicadores.contains(duplicarTiposIndicadores)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposIndicadores.add(duplicarTiposIndicadores);
            crearTiposIndicadores.add(duplicarTiposIndicadores);
            tiposindicadoresSeleccionado = duplicarTiposIndicadores;
            RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
                bandera = 0;
                filtrarTiposIndicadores = null;
                tipoLista = 0;
            }
            duplicarTiposIndicadores = new TiposIndicadores();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposIndicadores').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposIndicadores() {
        duplicarTiposIndicadores = new TiposIndicadores();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposIndicadoresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSINDICADORES", false, false, "UTF-8", null, null);
        context.responseComplete();
        tiposindicadoresSeleccionado = null;
        tiposindicadoresSeleccionado = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposIndicadoresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSINDICADORES", false, false, "UTF-8", null, null);
        context.responseComplete();
        tiposindicadoresSeleccionado = null;
        tiposindicadoresSeleccionado = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (tiposindicadoresSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(tiposindicadoresSeleccionado.getSecuencia(), "TIPOSINDICADORES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSINDICADORES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposIndicadores eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");

    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposIndicadores> getListTiposIndicadores() {
        if (listTiposIndicadores == null) {
            listTiposIndicadores = administrarTiposIndicadores.consultarTiposIndicadores();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        return listTiposIndicadores;
    }

    public void setListTiposIndicadores(List<TiposIndicadores> listTiposIndicadores) {
        this.listTiposIndicadores = listTiposIndicadores;
    }

    public List<TiposIndicadores> getFiltrarTiposIndicadores() {
        return filtrarTiposIndicadores;
    }

    public void setFiltrarTiposIndicadores(List<TiposIndicadores> filtrarTiposIndicadores) {
        this.filtrarTiposIndicadores = filtrarTiposIndicadores;
    }

    public TiposIndicadores getNuevoTiposIndicadores() {
        return nuevoTiposIndicadores;
    }

    public void setNuevoTiposIndicadores(TiposIndicadores nuevoTiposIndicadores) {
        this.nuevoTiposIndicadores = nuevoTiposIndicadores;
    }

    public TiposIndicadores getDuplicarTiposIndicadores() {
        return duplicarTiposIndicadores;
    }

    public void setDuplicarTiposIndicadores(TiposIndicadores duplicarTiposIndicadores) {
        this.duplicarTiposIndicadores = duplicarTiposIndicadores;
    }

    public TiposIndicadores getEditarTiposIndicadores() {
        return editarTiposIndicadores;
    }

    public void setEditarTiposIndicadores(TiposIndicadores editarTiposIndicadores) {
        this.editarTiposIndicadores = editarTiposIndicadores;
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

    public TiposIndicadores getTiposIndicadoresSeleccionado() {
        return tiposindicadoresSeleccionado;
    }

    public void setTiposIndicadoresSeleccionado(TiposIndicadores clasesPensionesSeleccionado) {
        this.tiposindicadoresSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposIndicadores");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
