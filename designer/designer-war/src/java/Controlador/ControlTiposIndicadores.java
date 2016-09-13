/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
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
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private Integer backUpCodigo;
    private String backUpDescripcion;

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
        System.out.println("controlTiposIndicadores Constructor");
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

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlTiposIndicadores.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarTiposIndicadores.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposIndicadores eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listTiposIndicadores.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listTiposIndicadores.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listTiposIndicadores.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarTiposIndicadores.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarTiposIndicadores.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarTiposIndicadores.get(index).getSecuencia();
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposIndicadores.asignarIndex \n");
            index = indice;
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
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposIndicadores = null;
        guardado = true;
        permitirIndex = true;
        getListTiposIndicadores();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposIndicadores == null || listTiposIndicadores.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposIndicadores.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
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
        index = -1;
        secRegistro = null;
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
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndicadores:descripcion");
            descripcion.setFilterStyle("width: 85%;");
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

    public void modificarTiposIndicadores(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearTiposIndicadores.contains(listTiposIndicadores.get(indice))) {
                    if (listTiposIndicadores.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposIndicadores.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposIndicadores.size(); j++) {
                            if (j != indice) {
                                if (listTiposIndicadores.get(indice).getCodigo() == listTiposIndicadores.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposIndicadores.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposIndicadores.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposIndicadores.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposIndicadores.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposIndicadores.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposIndicadores.isEmpty()) {
                            modificarTiposIndicadores.add(listTiposIndicadores.get(indice));
                        } else if (!modificarTiposIndicadores.contains(listTiposIndicadores.get(indice))) {
                            modificarTiposIndicadores.add(listTiposIndicadores.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listTiposIndicadores.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposIndicadores.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposIndicadores.size(); j++) {
                            if (j != indice) {
                                if (listTiposIndicadores.get(indice).getCodigo() == listTiposIndicadores.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposIndicadores.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposIndicadores.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposIndicadores.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposIndicadores.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposIndicadores.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearTiposIndicadores.contains(filtrarTiposIndicadores.get(indice))) {
                    if (filtrarTiposIndicadores.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposIndicadores.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < listTiposIndicadores.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposIndicadores.get(indice).getCodigo() == listTiposIndicadores.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposIndicadores.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposIndicadores.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposIndicadores.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarTiposIndicadores.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposIndicadores.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposIndicadores.isEmpty()) {
                            modificarTiposIndicadores.add(filtrarTiposIndicadores.get(indice));
                        } else if (!modificarTiposIndicadores.contains(filtrarTiposIndicadores.get(indice))) {
                            modificarTiposIndicadores.add(filtrarTiposIndicadores.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarTiposIndicadores.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposIndicadores.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < listTiposIndicadores.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposIndicadores.get(indice).getCodigo() == listTiposIndicadores.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposIndicadores.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposIndicadores.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposIndicadores.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarTiposIndicadores.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposIndicadores.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoTiposIndicadores() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposIndicadores");
                if (!modificarTiposIndicadores.isEmpty() && modificarTiposIndicadores.contains(listTiposIndicadores.get(index))) {
                    int modIndex = modificarTiposIndicadores.indexOf(listTiposIndicadores.get(index));
                    modificarTiposIndicadores.remove(modIndex);
                    borrarTiposIndicadores.add(listTiposIndicadores.get(index));
                } else if (!crearTiposIndicadores.isEmpty() && crearTiposIndicadores.contains(listTiposIndicadores.get(index))) {
                    int crearIndex = crearTiposIndicadores.indexOf(listTiposIndicadores.get(index));
                    crearTiposIndicadores.remove(crearIndex);
                } else {
                    borrarTiposIndicadores.add(listTiposIndicadores.get(index));
                }
                listTiposIndicadores.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposIndicadores ");
                if (!modificarTiposIndicadores.isEmpty() && modificarTiposIndicadores.contains(filtrarTiposIndicadores.get(index))) {
                    int modIndex = modificarTiposIndicadores.indexOf(filtrarTiposIndicadores.get(index));
                    modificarTiposIndicadores.remove(modIndex);
                    borrarTiposIndicadores.add(filtrarTiposIndicadores.get(index));
                } else if (!crearTiposIndicadores.isEmpty() && crearTiposIndicadores.contains(filtrarTiposIndicadores.get(index))) {
                    int crearIndex = crearTiposIndicadores.indexOf(filtrarTiposIndicadores.get(index));
                    crearTiposIndicadores.remove(crearIndex);
                } else {
                    borrarTiposIndicadores.add(filtrarTiposIndicadores.get(index));
                }
                int VCIndex = listTiposIndicadores.indexOf(filtrarTiposIndicadores.get(index));
                listTiposIndicadores.remove(VCIndex);
                filtrarTiposIndicadores.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
            infoRegistro = "Cantidad de registros: " + listTiposIndicadores.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        BigInteger contarVigenciasIndicadoresTipoIndicador;

        try {
            System.err.println("Control Secuencia de ControlTiposIndicadores ");
            if (tipoLista == 0) {
                contarVigenciasIndicadoresTipoIndicador = administrarTiposIndicadores.contarVigenciasIndicadoresTipoIndicador(listTiposIndicadores.get(index).getSecuencia());
            } else {
                contarVigenciasIndicadoresTipoIndicador = administrarTiposIndicadores.contarVigenciasIndicadoresTipoIndicador(filtrarTiposIndicadores.get(index).getSecuencia());
            }
            if (contarVigenciasIndicadoresTipoIndicador.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposIndicadores();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;
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
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
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
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
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
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTiposIndicadores = listTiposIndicadores.get(index);
            }
            if (tipoLista == 1) {
                editarTiposIndicadores = filtrarTiposIndicadores.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                PrimefacesContextUI.ejecutar("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                PrimefacesContextUI.ejecutar("PF('editDescripcion').show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
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

        System.out.println("contador " + contador);

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
            nuevoTiposIndicadores = new TiposIndicadores();
            RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
            infoRegistro = "Cantidad de registros: " + listTiposIndicadores.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroTiposIndicadores').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposIndicadores() {
        System.out.println("limpiarNuevoTiposIndicadores");
        nuevoTiposIndicadores = new TiposIndicadores();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposIndicadores() {
        System.out.println("duplicandoTiposIndicadores");
        if (index >= 0) {
            duplicarTiposIndicadores = new TiposIndicadores();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposIndicadores.setSecuencia(l);
                duplicarTiposIndicadores.setCodigo(listTiposIndicadores.get(index).getCodigo());
                duplicarTiposIndicadores.setDescripcion(listTiposIndicadores.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarTiposIndicadores.setSecuencia(l);
                duplicarTiposIndicadores.setCodigo(filtrarTiposIndicadores.get(index).getCodigo());
                duplicarTiposIndicadores.setDescripcion(filtrarTiposIndicadores.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroTiposIndicadores').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposIndicadores.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposIndicadores.getDescripcion());

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

            System.out.println("Datos Duplicando: " + duplicarTiposIndicadores.getSecuencia() + "  " + duplicarTiposIndicadores.getCodigo());
            if (crearTiposIndicadores.contains(duplicarTiposIndicadores)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposIndicadores.add(duplicarTiposIndicadores);
            crearTiposIndicadores.add(duplicarTiposIndicadores);
            RequestContext.getCurrentInstance().update("form:datosTiposIndicadores");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listTiposIndicadores.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

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
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroTiposIndicadores').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
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
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposIndicadoresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSINDICADORES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposIndicadores.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSINDICADORES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("TIPOSINDICADORES")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposIndicadores> getListTiposIndicadores() {
        if (listTiposIndicadores == null) {
            System.out.println("ControlTiposIndicadores getListTiposIndicadores");
            listTiposIndicadores = administrarTiposIndicadores.consultarTiposIndicadores();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposIndicadores == null || listTiposIndicadores.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposIndicadores.size();
        }
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

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
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
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
