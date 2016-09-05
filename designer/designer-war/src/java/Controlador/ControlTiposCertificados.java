/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposCertificados;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposCertificadosInterface;
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
public class ControlTiposCertificados implements Serializable {

    @EJB
    AdministrarTiposCertificadosInterface administrarTiposCertificados;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposCertificados> listTiposCertificados;
    private List<TiposCertificados> filtrarTiposCertificados;
    private List<TiposCertificados> crearTiposCertificados;
    private List<TiposCertificados> modificarTiposCertificados;
    private List<TiposCertificados> borrarTiposCertificados;
    private TiposCertificados nuevoTiposCertificados;
    private TiposCertificados duplicarTiposCertificados;
    private TiposCertificados editarTiposCertificados;
    private TiposCertificados tiposCertificadosSeleccionado;
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

    public ControlTiposCertificados() {
        listTiposCertificados = null;
        crearTiposCertificados = new ArrayList<TiposCertificados>();
        modificarTiposCertificados = new ArrayList<TiposCertificados>();
        borrarTiposCertificados = new ArrayList<TiposCertificados>();
        permitirIndex = true;
        editarTiposCertificados = new TiposCertificados();
        nuevoTiposCertificados = new TiposCertificados();
        duplicarTiposCertificados = new TiposCertificados();
        guardado = true;
        tamano = 270;
        System.out.println("controlTiposCertificados Constructor");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlTiposCertificados PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposCertificados.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
private String paginaAnterior;
public void recibirPagina(String pagina){paginaAnterior = pagina;}
public String redirigirPaginaAnterior(){return paginaAnterior;}
    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlTiposCertificados.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarTiposCertificados.size();
            context.update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposCertificados eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listTiposCertificados.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listTiposCertificados.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listTiposCertificados.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarTiposCertificados.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarTiposCertificados.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarTiposCertificados.get(index).getSecuencia();
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposCertificados.asignarIndex \n");
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
            System.out.println("ERROR ControlTiposCertificados.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
            bandera = 0;
            filtrarTiposCertificados = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposCertificados.clear();
        crearTiposCertificados.clear();
        modificarTiposCertificados.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposCertificados = null;
        guardado = true;
        permitirIndex = true;
        getListTiposCertificados();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposCertificados == null || listTiposCertificados.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposCertificados.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosTiposCertificados");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
            bandera = 0;
            filtrarTiposCertificados = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposCertificados.clear();
        crearTiposCertificados.clear();
        modificarTiposCertificados.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposCertificados = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosTiposCertificados");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
            bandera = 0;
            filtrarTiposCertificados = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposCertificados(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearTiposCertificados.contains(listTiposCertificados.get(indice))) {
                    if (listTiposCertificados.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposCertificados.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposCertificados.size(); j++) {
                            if (j != indice) {
                                if (listTiposCertificados.get(indice).getCodigo() == listTiposCertificados.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposCertificados.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposCertificados.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposCertificados.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposCertificados.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposCertificados.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposCertificados.isEmpty()) {
                            modificarTiposCertificados.add(listTiposCertificados.get(indice));
                        } else if (!modificarTiposCertificados.contains(listTiposCertificados.get(indice))) {
                            modificarTiposCertificados.add(listTiposCertificados.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listTiposCertificados.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposCertificados.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposCertificados.size(); j++) {
                            if (j != indice) {
                                if (listTiposCertificados.get(indice).getCodigo() == listTiposCertificados.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposCertificados.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposCertificados.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposCertificados.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposCertificados.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposCertificados.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearTiposCertificados.contains(filtrarTiposCertificados.get(indice))) {
                    if (filtrarTiposCertificados.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposCertificados.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < listTiposCertificados.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposCertificados.get(indice).getCodigo() == listTiposCertificados.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposCertificados.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposCertificados.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposCertificados.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarTiposCertificados.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposCertificados.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposCertificados.isEmpty()) {
                            modificarTiposCertificados.add(filtrarTiposCertificados.get(indice));
                        } else if (!modificarTiposCertificados.contains(filtrarTiposCertificados.get(indice))) {
                            modificarTiposCertificados.add(filtrarTiposCertificados.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarTiposCertificados.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposCertificados.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < listTiposCertificados.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposCertificados.get(indice).getCodigo() == listTiposCertificados.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposCertificados.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposCertificados.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposCertificados.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarTiposCertificados.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposCertificados.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            context.update("form:datosTiposCertificados");
            context.update("form:ACEPTAR");
        }

    }

    public void borrandoTiposCertificados() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposCertificados");
                if (!modificarTiposCertificados.isEmpty() && modificarTiposCertificados.contains(listTiposCertificados.get(index))) {
                    int modIndex = modificarTiposCertificados.indexOf(listTiposCertificados.get(index));
                    modificarTiposCertificados.remove(modIndex);
                    borrarTiposCertificados.add(listTiposCertificados.get(index));
                } else if (!crearTiposCertificados.isEmpty() && crearTiposCertificados.contains(listTiposCertificados.get(index))) {
                    int crearIndex = crearTiposCertificados.indexOf(listTiposCertificados.get(index));
                    crearTiposCertificados.remove(crearIndex);
                } else {
                    borrarTiposCertificados.add(listTiposCertificados.get(index));
                }
                listTiposCertificados.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposCertificados ");
                if (!modificarTiposCertificados.isEmpty() && modificarTiposCertificados.contains(filtrarTiposCertificados.get(index))) {
                    int modIndex = modificarTiposCertificados.indexOf(filtrarTiposCertificados.get(index));
                    modificarTiposCertificados.remove(modIndex);
                    borrarTiposCertificados.add(filtrarTiposCertificados.get(index));
                } else if (!crearTiposCertificados.isEmpty() && crearTiposCertificados.contains(filtrarTiposCertificados.get(index))) {
                    int crearIndex = crearTiposCertificados.indexOf(filtrarTiposCertificados.get(index));
                    crearTiposCertificados.remove(crearIndex);
                } else {
                    borrarTiposCertificados.add(filtrarTiposCertificados.get(index));
                }
                int VCIndex = listTiposCertificados.indexOf(filtrarTiposCertificados.get(index));
                listTiposCertificados.remove(VCIndex);
                filtrarTiposCertificados.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosTiposCertificados");
            infoRegistro = "Cantidad de registros: " + listTiposCertificados.size();
            context.update("form:informacionRegistro");

            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            context.update("form:ACEPTAR");
        }

    }

    /*public void verificarBorrado() {
     System.out.println("Estoy en verificarBorrado");
     BigInteger contarRetiradosClasePension;

     try {
     System.err.println("Control Secuencia de ControlTiposCertificados ");
     if (tipoLista == 0) {
     contarRetiradosClasePension = administrarTiposCertificados.contarRetiradosClasePension(listTiposCertificados.get(index).getSecuencia());
     } else {
     contarRetiradosClasePension = administrarTiposCertificados.contarRetiradosClasePension(filtrarTiposCertificados.get(index).getSecuencia());
     }
     if (contarRetiradosClasePension.equals(new BigInteger("0"))) {
     System.out.println("Borrado==0");
     borrandoTiposCertificados();
     } else {
     System.out.println("Borrado>0");

     RequestContext context = RequestContext.getCurrentInstance();
     context.update("form:validacionBorrar");
     context.execute("validacionBorrar.show()");
     index = -1;
     contarRetiradosClasePension = new BigInteger("-1");

     }
     } catch (Exception e) {
     System.err.println("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
     }
     }

     */
    public void revisarDialogoGuardar() {

        if (!borrarTiposCertificados.isEmpty() || !crearTiposCertificados.isEmpty() || !modificarTiposCertificados.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }

    }

    public void guardarTiposCertificados() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarTiposCertificados");
            if (!borrarTiposCertificados.isEmpty()) {
                administrarTiposCertificados.borrarTiposCertificados(borrarTiposCertificados);
                //mostrarBorrados
                registrosBorrados = borrarTiposCertificados.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarTiposCertificados.clear();
            }
            if (!modificarTiposCertificados.isEmpty()) {
                administrarTiposCertificados.modificarTiposCertificados(modificarTiposCertificados);
                modificarTiposCertificados.clear();
            }
            if (!crearTiposCertificados.isEmpty()) {
                administrarTiposCertificados.crearTiposCertificados(crearTiposCertificados);
                crearTiposCertificados.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposCertificados = null;
            context.update("form:datosTiposCertificados");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTiposCertificados = listTiposCertificados.get(index);
            }
            if (tipoLista == 1) {
                editarTiposCertificados = filtrarTiposCertificados.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                context.update("formularioDialogos:editCodigo");
                context.execute("editCodigo.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editDescripcion");
                context.execute("editDescripcion.show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoTiposCertificados() {
        System.out.println("agregarNuevoTiposCertificados");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposCertificados.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTiposCertificados.getCodigo());

            for (int x = 0; x < listTiposCertificados.size(); x++) {
                if (listTiposCertificados.get(x).getCodigo() == nuevoTiposCertificados.getCodigo()) {
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
        if (nuevoTiposCertificados.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoTiposCertificados.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
                bandera = 0;
                filtrarTiposCertificados = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposCertificados.setSecuencia(l);

            crearTiposCertificados.add(nuevoTiposCertificados);

            listTiposCertificados.add(nuevoTiposCertificados);
            nuevoTiposCertificados = new TiposCertificados();
            context.update("form:datosTiposCertificados");
            infoRegistro = "Cantidad de registros: " + listTiposCertificados.size();
            context.update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            context.execute("nuevoRegistroTiposCertificados.hide()");
            index = -1;
            secRegistro = null;

        } else {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposCertificados() {
        System.out.println("limpiarNuevoTiposCertificados");
        nuevoTiposCertificados = new TiposCertificados();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposCertificados() {
        System.out.println("duplicandoTiposCertificados");
        if (index >= 0) {
            duplicarTiposCertificados = new TiposCertificados();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposCertificados.setSecuencia(l);
                duplicarTiposCertificados.setCodigo(listTiposCertificados.get(index).getCodigo());
                duplicarTiposCertificados.setDescripcion(listTiposCertificados.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarTiposCertificados.setSecuencia(l);
                duplicarTiposCertificados.setCodigo(filtrarTiposCertificados.get(index).getCodigo());
                duplicarTiposCertificados.setDescripcion(filtrarTiposCertificados.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarTE");
            context.execute("duplicarRegistroTiposCertificados.show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposCertificados.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposCertificados.getDescripcion());

        if (duplicarTiposCertificados.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposCertificados.size(); x++) {
                if (listTiposCertificados.get(x).getCodigo() == duplicarTiposCertificados.getCodigo()) {
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
        if (duplicarTiposCertificados.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarTiposCertificados.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarTiposCertificados.getSecuencia() + "  " + duplicarTiposCertificados.getCodigo());
            if (crearTiposCertificados.contains(duplicarTiposCertificados)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposCertificados.add(duplicarTiposCertificados);
            crearTiposCertificados.add(duplicarTiposCertificados);
            context.update("form:datosTiposCertificados");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            context.update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listTiposCertificados.size();
            context.update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
                bandera = 0;
                filtrarTiposCertificados = null;
                tipoLista = 0;
            }
            duplicarTiposCertificados = new TiposCertificados();
            RequestContext.getCurrentInstance().execute("duplicarRegistroTiposCertificados.hide()");

        } else {
            contador = 0;
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
        }
    }

    public void limpiarDuplicarTiposCertificados() {
        duplicarTiposCertificados = new TiposCertificados();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCertificadosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSCERTIFICADOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCertificadosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSCERTIFICADOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposCertificados.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSCERTIFICADOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                System.out.println("resultado: " + resultado);
                if (resultado == 1) {
                    context.execute("errorObjetosDB.show()");
                } else if (resultado == 2) {
                    context.execute("confirmarRastro.show()");
                } else if (resultado == 3) {
                    context.execute("errorRegistroRastro.show()");
                } else if (resultado == 4) {
                    context.execute("errorTablaConRastro.show()");
                } else if (resultado == 5) {
                    context.execute("errorTablaSinRastro.show()");
                }
            } else {
                context.execute("seleccionarRegistro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("TIPOSCERTIFICADOS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposCertificados> getListTiposCertificados() {
        if (listTiposCertificados == null) {
            System.out.println("ControlTiposCertificados getListTiposCertificados");
            listTiposCertificados = administrarTiposCertificados.consultarTiposCertificados();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposCertificados == null || listTiposCertificados.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposCertificados.size();
        }
        return listTiposCertificados;
    }

    public void setListTiposCertificados(List<TiposCertificados> listTiposCertificados) {
        this.listTiposCertificados = listTiposCertificados;
    }

    public List<TiposCertificados> getFiltrarTiposCertificados() {
        return filtrarTiposCertificados;
    }

    public void setFiltrarTiposCertificados(List<TiposCertificados> filtrarTiposCertificados) {
        this.filtrarTiposCertificados = filtrarTiposCertificados;
    }

    public TiposCertificados getNuevoTiposCertificados() {
        return nuevoTiposCertificados;
    }

    public void setNuevoTiposCertificados(TiposCertificados nuevoTiposCertificados) {
        this.nuevoTiposCertificados = nuevoTiposCertificados;
    }

    public TiposCertificados getDuplicarTiposCertificados() {
        return duplicarTiposCertificados;
    }

    public void setDuplicarTiposCertificados(TiposCertificados duplicarTiposCertificados) {
        this.duplicarTiposCertificados = duplicarTiposCertificados;
    }

    public TiposCertificados getEditarTiposCertificados() {
        return editarTiposCertificados;
    }

    public void setEditarTiposCertificados(TiposCertificados editarTiposCertificados) {
        this.editarTiposCertificados = editarTiposCertificados;
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

    public TiposCertificados getTiposCertificadosSeleccionado() {
        return tiposCertificadosSeleccionado;
    }

    public void setTiposCertificadosSeleccionado(TiposCertificados clasesPensionesSeleccionado) {
        this.tiposCertificadosSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
